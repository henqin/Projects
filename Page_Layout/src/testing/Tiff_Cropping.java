package testing;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.media.jai.NullOpImage;
import javax.media.jai.OpImage;
import javax.media.jai.PlanarImage;

import org.apache.commons.io.FilenameUtils;

import com.abbyy.FREngine.Engine;
import com.abbyy.FREngine.IEngine;
import com.abbyy.FREngine.IEngineLoader;
import com.abbyy.FREngine.IImage;
import com.abbyy.FREngine.IImageDocument;
import com.abbyy.FREngine.ImageFileFormatEnum;
import com.sun.media.jai.codec.FileSeekableStream;
import com.sun.media.jai.codec.ImageCodec;
import com.sun.media.jai.codec.ImageDecoder;
import com.sun.media.jai.codec.ImageEncoder;
import com.sun.media.jai.codec.SeekableStream;
import com.sun.media.jai.codec.TIFFEncodeParam;
import com.sun.media.jai.codec.TIFFField;

public class Tiff_Cropping
{
	public Tiff_Cropping()
	{
		super();
	}

	public List<BufferedImage> deskew(List<BufferedImage> input, String name)
	{
		List<BufferedImage> output = new ArrayList<BufferedImage>();
		try
		{
			// Load the Engine
			IEngineLoader engineloader = Engine.CreateEngineOutprocLoader();
			IEngine engine = engineloader.GetEngineObject("SWTD-1000-0002-9871-7227-8145", null, null);
			try
			{
				// Load a predefined profile
				// engine.LoadPredefinedProfile("TextExtraction_Accuracy");
				ImageFileFormatEnum e = ImageFileFormatEnum.IFF_TiffBwCcittGroup4;
				for (int i = 0; i <= input.size(); i++)
				{
					ImageIO.write(input.get(i), "tif", new File("C:/Users/ATUL/Desktop/Abbyy/temp/" + name + "_" + i + ".tif"));
					IImageDocument img = engine.PrepareAndOpenImage("C:/Users/ATUL/Desktop/Abbyy/temp/" + name + "_" + i + ".tif",
							null, null, null);
					img.CorrectSkew(1);
					IImage dst = img.getGrayImage();
					dst.WriteToFile("C:/Users/ATUL/Desktop/Abbyy/temp/" + name + "_skew_" + i + ".tif", e, null, null);
					output.add(ImageIO.read(new File("C:/Users/ATUL/Desktop/Abbyy/temp/" + name + "_skew_" + i + ".tif")));
					System.out.println("image " + i + " deskewed.");
				}
			} finally
			{
				engine = null;
				System.gc();
				System.runFinalization();
				Engine.Unload();
			}
		} catch (Exception ex)
		{
			System.out.println(ex.getMessage());
		}
		return output;
	}

	public void cutter(File tifffile, String result) throws Exception
	{
		Tiff_Cropping tc = new Tiff_Cropping();
		List<BufferedImage> input = tc.tiff_Extractor(tifffile);
		List<BufferedImage> images = tc.deskew(input, FilenameUtils.removeExtension(tifffile.getName()));
		List<BufferedImage> output = new ArrayList<BufferedImage>();

		for (int k = 0; k < images.size(); k++)
		{
			BufferedImage img = images.get(k);
			Cropping_Points p = new Cropping_Points();
			p = p.cutting_point(img);
			if (p.getMidmaximun() >= (int) (0.009 * img.getWidth()) && p.getLine() == 0)
			{
				int midpoint = (p.getM1() + p.getM2()) / 2;
				if ((p.getL2() - (int) (0.002 * img.getWidth())) > 0)
					p.setL2(p.getL2() - (int) (0.002 * img.getWidth()));
				output.add(img.getSubimage(p.getL2(), 0, midpoint - p.getL2(), img.getHeight()));
				System.out.println("page " + (k + 1) + " is cropped.and y1 = " + p.getY1());

			}
			else
			{
				output.add(img.getSubimage(p.getL2(), 0, p.getR1() - p.getL2(), img.getHeight()));
				System.out.println("page " + (k + 1) + " is not cropped.and y1 = " + p.getY1());
			}
		}
		tc.tiff_Maker(output, result);
	}

	public void tiff_Maker(List<BufferedImage> output, String result) throws IOException
	{
		TIFFEncodeParam params = new TIFFEncodeParam();
		OutputStream out = new FileOutputStream(result);
		List<BufferedImage> imageList = new ArrayList<BufferedImage>();
		for (int i = 1; i < output.size(); i++)
		{
			imageList.add(output.get(i));
		}
		params.setWriteTiled(true);
		params.setCompression(TIFFEncodeParam.COMPRESSION_GROUP4);
		params.setExtraImages(imageList.iterator());
		TIFFField[] extras = new TIFFField[2];
		extras[0] = new TIFFField(282, TIFFField.TIFF_RATIONAL, 1,
				(Object) new long[][] { { (long) 300, (long) 1 }, { (long) 0, (long) 0 } });
		extras[1] = new TIFFField(283, TIFFField.TIFF_RATIONAL, 1,
				(Object) new long[][] { { (long) 300, (long) 1 }, { (long) 0, (long) 0 } });
		params.setExtraFields(extras);
		ImageEncoder encoder = ImageCodec.createImageEncoder("tiff", out, params);
		encoder.encode(output.get(0));
		out.close();
	}

	public List<BufferedImage> tiff_Extractor(File tiff) throws IOException
	{
		List<BufferedImage> images = new ArrayList<BufferedImage>();
		SeekableStream ss = new FileSeekableStream(tiff);
		ImageDecoder decoder = ImageCodec.createImageDecoder("tiff", ss, null);
		int numPages = decoder.getNumPages();
		for (int j = 0; j < numPages; j++)
		{
			PlanarImage op = new NullOpImage(decoder.decodeAsRenderedImage(j), null, null, OpImage.OP_IO_BOUND);
			images.add(op.getAsBufferedImage());
			ImageIO.write(op.getAsBufferedImage(), "tif", new File("C:/Users/ATUL/Desktop/Page-layout/image/"
					+ FilenameUtils.removeExtension(tiff.getName()) + "_image_" + (j + 1) + ".tif"));
			System.out.println("writting image " + (j + 1));
		}
		ss.close();
		return images;
	}
}
