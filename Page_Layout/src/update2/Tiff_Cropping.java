package update2;

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

import com.abbyy.FREngine.BaseLanguageLetterSetEnum;
import com.abbyy.FREngine.DictionaryTypeEnum;
import com.abbyy.FREngine.Engine;
import com.abbyy.FREngine.FREngineModuleEnum;
import com.abbyy.FREngine.FileExportFormatEnum;
import com.abbyy.FREngine.IBaseLanguage;
import com.abbyy.FREngine.IDictionaryDescription;
import com.abbyy.FREngine.IDictionaryDescriptions;
import com.abbyy.FREngine.IEngine;
import com.abbyy.FREngine.IEngineLoader;
import com.abbyy.FREngine.IFRDocument;
import com.abbyy.FREngine.IImage;
import com.abbyy.FREngine.IImageDocument;
import com.abbyy.FREngine.ILanguageDatabase;
import com.abbyy.FREngine.IPageProcessingParams;
import com.abbyy.FREngine.IPlainText;
import com.abbyy.FREngine.IRecognizerParams;
import com.abbyy.FREngine.ITextLanguage;
import com.abbyy.FREngine.ImageFileFormatEnum;
import com.abbyy.FREngine.LanguageIdEnum;
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
			System.out.println("Loading engine...");
			long a = System.currentTimeMillis();
			// Load the Engine
			IEngineLoader engineloader = Engine.CreateEngineOutprocLoader();
			IEngine engine = engineloader.GetEngineObject("SWTD-1000-0002-9871-7227-8145", null, null);
			long b = System.currentTimeMillis();
			System.out.println("Engine loading time = " + (b - a));

			try
			{
				// Load a predefined profile
				// engine.LoadPredefinedProfile("TextExtraction_Accuracy");

				ImageFileFormatEnum e = ImageFileFormatEnum.IFF_TiffBwCcittGroup4;
				for (int i = 0; i <= input.size(); i++)
				{
					ImageIO.write(input.get(i), "tif",
							new File("C:/Users/ATUL/Desktop/Abbyy/temp/" + name + "_" + (i + 1) + ".tif"));
					IImageDocument img = engine.PrepareAndOpenImage(
							"C:/Users/ATUL/Desktop/Abbyy/temp/" + name + "_" + (i + 1) + ".tif", null, null, null);
					img.CorrectSkew(1);
					IImage dst = img.getGrayImage();
					dst.WriteToFile("C:/Users/ATUL/Desktop/Abbyy/temp/" + name + "_skew_" + (i + 1) + ".tif", e, null, null);
					output.add(ImageIO.read(new File("C:/Users/ATUL/Desktop/Abbyy/temp/" + name + "_skew_" + (i + 1) + ".tif")));
					System.out.println(name + "_" + (i + 1) + " deskewed.");
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

	public List<String> pageNo(List<BufferedImage> input, String name)
	{
		List<String> output = new ArrayList<String>();
		try
		{
			System.out.println("Loading engine...");
			long a = System.currentTimeMillis();
			// Load the Engine
			IEngineLoader engineloader = Engine.CreateEngineOutprocLoader();
			IEngine engine = engineloader.GetEngineObject("SWTD-1000-0002-9871-8054-3276", null, null);
			// SWTD-1000-0002-9871-8054-3276 char
			// SWTD-1000-0002-9871-7227-8145 page
			long b = System.currentTimeMillis();
			System.out.println("Engine loading time = " + (b - a));

			try
			{
				// Load a predefined profile
				engine.LoadPredefinedProfile("TextExtraction_Accuracy");

				for (int i = 0; i < input.size(); i++)
				{
					long aa = System.currentTimeMillis();
					// IFRDocument document = engine.CreateFRDocument();
					ImageIO.write(input.get(i), "tif",
							new File("C:/Users/ATUL/Desktop/Abbyy/temp/" + name + "_" + (i + 1) + ".tif"));

					IPlainText ip = engine.RecognizeImageAsPlainText(
							"C:/Users/ATUL/Desktop/Abbyy/temp/" + name + "_" + (i + 1) + ".tif", null, null, null);
					
					ip.SaveToAsciiXMLFile("C:/Users/ATUL/Desktop/Abbyy/ip-" + (i + 1) + ".xml");
					
//					System.out.println(ip.getText());

					// document.AddImageFile(
					// "C:/Users/ATUL/Desktop/Abbyy/temp/" + name + "_" + (i + 1) + ".tif", null, null);
					// document.Process(null, null, null);
					// System.out.println("for page no "+(i+1)+" = "+document.getPlainText().getText());
					//
					// document.Export("C:/Users/ATUL/Desktop/Abbyy/"+(i+1)+".xml", FileExportFormatEnum.FEF_XML, null);;

					long bb = System.currentTimeMillis();
					System.out.println("time for page no " + (i + 1) + " = " + (bb - aa));
					// output.add(document.getPlainText().getText());
					// document.getPageFlushingPolicy();
					// document.Release();
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
		String filename = FilenameUtils.removeExtension(tifffile.getName());
		long a = System.currentTimeMillis();
		System.out.println(filename + " is extracting...");
		List<BufferedImage> images = tc.tiff_Extractor(tifffile);
		long b = System.currentTimeMillis();
		System.out.println(filename + " extracting time = " + (b - a));

		System.out.println(filename + " is deskewing...");
		// List<BufferedImage> images = tc.deskew(input, filename);
		long c = System.currentTimeMillis();
		System.out.println(filename + " deskewing time = " + (c - b));

		List<BufferedImage> output = new ArrayList<BufferedImage>();

		List<BufferedImage> pagenumber = new ArrayList<BufferedImage>();

		System.out.println(filename + " is cropping...");
		for (int k = 53; k < 64; k++)
		{
			BufferedImage img = images.get(k);
			Cropping_Points p = new Cropping_Points();
			p = p.cutting_point(img);

			int midpoint = (p.getM1() + p.getM2()) / 2;

			if (p.getMidmaximun() >= (int) (0.008 * img.getWidth()) && p.getLine() == 0)
			{

				if ((p.getL2() - (int) (0.002 * img.getWidth())) > 0)
					p.setL2(p.getL2() - (int) (0.002 * img.getWidth()));
				output.add(img.getSubimage(p.getL2(), p.getY1(), midpoint - p.getL2(), p.getY2() - p.getY1()));
				System.out.println(filename + "_" + (k + 1) + " is cropped.and bottem y=" + p.getY2());
			}
			else
			{
				output.add(img.getSubimage(p.getL2(), p.getY1(), p.getR1() - p.getL2(), p.getY2() - p.getY1()));
				System.out.println(filename + "_" + (k + 1) + " is not cropped.and bottem y=" + p.getY2());
			}

			// for finding the pagenumber
			int pX = (int) (img.getWidth() * 0.75);
			int pY = (int) (p.getY2() * 0.95);
			int pwidth = p.getR1() - pX;
			int pheight = p.getY2() - pY;
			pagenumber.add(img.getSubimage(pX, pY, pwidth, pheight));
		}
		long d = System.currentTimeMillis();
		System.out.println(filename + " cropping time = " + (d - c));

		tc.tiff_Maker(output, result);
		tc.tiff_Maker(pagenumber, "C:/Users/ATUL/Desktop/Page-layout/testing/output/"
				+ FilenameUtils.removeExtension(tifffile.getName()) + "_bottem.tif");
		// tc.pageNo(pagenumber, filename);
		tc.pageNo(output, filename);
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
			System.out.println(FilenameUtils.removeExtension(tiff.getName()) + "_" + (j + 1) + " is extracted.");
		}
		ss.close();
		return images;
	}
}
