package preprocessing;

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

import com.abbyy.FREngine.CodePageEnum;
import com.abbyy.FREngine.Engine;
import com.abbyy.FREngine.IEngine;
import com.abbyy.FREngine.IEngineLoader;
import com.abbyy.FREngine.IImage;
import com.abbyy.FREngine.IImageDocument;
import com.abbyy.FREngine.IPlainText;
import com.abbyy.FREngine.ImageFileFormatEnum;
import com.abbyy.FREngine.TextEncodingTypeEnum;
import com.sun.media.jai.codec.FileSeekableStream;
import com.sun.media.jai.codec.ImageCodec;
import com.sun.media.jai.codec.ImageDecoder;
import com.sun.media.jai.codec.ImageEncoder;
import com.sun.media.jai.codec.SeekableStream;
import com.sun.media.jai.codec.TIFFEncodeParam;
import com.sun.media.jai.codec.TIFFField;

public class Tiff_CroppingTest
{

	public Tiff_CroppingTest()
	{
		super();
	}

	public void tiffPreProcessing(File inputfile, IEngine engine) throws IOException
	{
		Tiff_CroppingTest tc = new Tiff_CroppingTest();

		SeekableStream ss = new FileSeekableStream(inputfile);
		ImageDecoder decoder = ImageCodec.createImageDecoder("tiff", ss, null);
		int numPages = decoder.getNumPages();
		try
		{
			for (int i = 0; i < numPages; i++)
			{
				System.out.println("Image" + (i + 1) + " is processing....");
				PlanarImage op = new NullOpImage(decoder.decodeAsRenderedImage(i), null, null, OpImage.OP_IO_BOUND);
				BufferedImage bufferedImage = op.getAsBufferedImage();
				System.out.println("1. Image extracted.");

				bufferedImage = tc.deskewPerPage(bufferedImage, engine);

				Cropping_Points cp = new Cropping_Points();
				cp = cp.cutting_point(bufferedImage);

				BufferedImage dest = null;
				int imgHeight = bufferedImage.getHeight();
				int imgWidth = bufferedImage.getWidth();

				if (cp.getMidmaximun() >= (int) (0.01 * imgWidth) && cp.getLine() == 0)
				{
					int midpoint = cp.getMidpoint();
					dest = bufferedImage.getSubimage(0, 0, midpoint, imgHeight);
					ImageIO.write(dest, "png", new File("C:/Users/ATUL/Desktop/english/English-page" + (i + 1) + ".png"));
					
					dest = bufferedImage.getSubimage(midpoint, 0, imgWidth - midpoint, imgHeight);
					ImageIO.write(dest, "png", new File("C:/Users/ATUL/Desktop/english/Arabic-page" + (i + 1) + ".png"));
					System.out.println("3. Image cropped.");

					dest = bufferedImage.getSubimage(0, cp.getY1(), imgWidth, cp.getY2() - cp.getY1());
					String pageInfo = tc.extractText(engine, dest, i + 1);
					System.out.print("4. Image Footer : ");
					System.out.println(pageInfo);

				}
				else
				{
					dest = bufferedImage.getSubimage(0, 0, imgWidth, imgHeight);
					System.out.println("3. Image not cropped.");

					dest = bufferedImage.getSubimage(0, cp.getY1(), imgWidth, cp.getY2() - cp.getY1());
					String pageInfo = tc.extractText(engine, dest, i + 1);
					System.out.print("4. Image Footer : ");
					System.out.println(pageInfo);
				}
			}
		} finally
		{
			ss.close();
			System.gc();
			System.runFinalization();
		}
	}

	public String extractText(IEngine engine, BufferedImage dest, Integer pageNo) throws IOException
	{
		ImageIO.write(dest, "png", new File("C:/Users/ATUL/Desktop/english/English-pageNo" + pageNo + ".png"));
		IPlainText ip = engine.RecognizeImageAsPlainText("C:/Users/ATUL/Desktop/english/English-pageNo" + pageNo + ".png", null,
				null, null);
		return ip.getText();
	}

	public BufferedImage deskewPerPage(BufferedImage bufferedImage, IEngine engine)
	{
		try
		{
			try
			{
				// engine.LoadPredefinedProfile("TextExtraction_Accuracy");
				ImageFileFormatEnum e = ImageFileFormatEnum.IFF_PngBwPng;
				ImageIO.write(bufferedImage, "png", new File("C:/Users/ATUL/Desktop/Abbyy/temp/temp.png"));
				IImageDocument img = engine.PrepareAndOpenImage("C:/Users/ATUL/Desktop/Abbyy/temp/temp.png", null, null, null);
				img.CorrectSkew(1);
				IImage dst = img.getGrayImage();
				dst.WriteToFile("C:/Users/ATUL/Desktop/Abbyy/temp/skew.png", e, null, null);
				bufferedImage = ImageIO.read(new File("C:/Users/ATUL/Desktop/Abbyy/temp/skew.png"));
				System.out.println("2. Image deskewed.");
			} finally
			{
				System.gc();
				System.runFinalization();
			}
		} catch (Exception ex)
		{
			System.out.println(ex.getMessage());
		}
		return bufferedImage;
	}

	

	
	
	public void tiffPreProcessingFull(File inputfile, IEngine engine) throws IOException
	{
		Tiff_CroppingTest tc = new Tiff_CroppingTest();

		SeekableStream ss = new FileSeekableStream(inputfile);
		ImageDecoder decoder = ImageCodec.createImageDecoder("tiff", ss, null);
		int numPages = decoder.getNumPages();
		try
		{
			for (int i = 0; i < numPages; i++)
			{
				System.out.println("Image" + (i + 1) + " is processing....");
				PlanarImage op = new NullOpImage(decoder.decodeAsRenderedImage(i), null, null, OpImage.OP_IO_BOUND);
				BufferedImage bufferedImage = op.getAsBufferedImage();
				System.out.println("1. Image extracted.");

				bufferedImage = tc.deskewPerPageFull(bufferedImage, engine);

				Cropping_Points cp = new Cropping_Points();
				cp = cp.cutting_point(bufferedImage);

				BufferedImage dest = null;
				int imgHeight = bufferedImage.getHeight();
				int imgWidth = bufferedImage.getWidth();

				if (cp.getMidmaximun() >= (int) (0.01 * imgWidth) && cp.getLine() == 0)
				{
					int midpoint = cp.getMidpoint();
					dest = bufferedImage.getSubimage(0, 0, midpoint, imgHeight);
					System.out.println("3. Image cropped.");

					tc.extractTextFileFull(engine, dest, "", i + 1, true);
					System.out.println("4. Image convert into text and xml file.");

					dest = bufferedImage.getSubimage(0, cp.getY1(), imgWidth, cp.getY2() - cp.getY1());
					String pageInfo = tc.extractTextFileFull(engine, dest, "number", i + 1, false);
					System.out.print("5. Image Footer : ");
					System.out.println(pageInfo);

					dest = bufferedImage.getSubimage(midpoint, 0, imgWidth - midpoint, imgHeight);
					ImageIO.write(dest, "png", new File("C:/Users/ATUL/Desktop/english/Arabic-page" + (i + 1) + ".png"));
				}
				else
				{
					dest = bufferedImage.getSubimage(0, 0, imgWidth, imgHeight);
					System.out.println("3. Image not cropped.");

					tc.extractTextFileFull(engine, dest, "", i + 1, true);
					System.out.println("4. Image convert into text and xml file.");

					dest = bufferedImage.getSubimage(0, cp.getY1(), imgWidth, cp.getY2() - cp.getY1());
					String pageInfo = tc.extractTextFileFull(engine, dest, "number", i + 1, false);
					System.out.print("5. Image Footer : ");
					System.out.println(pageInfo);
				}
			}
		} finally
		{
			ss.close();
			System.gc();
			System.runFinalization();
		}
	}

	public String extractTextFileFull(IEngine engine, BufferedImage dest, String name, Integer pageNo, Boolean xml)
			throws IOException
	{
		ImageIO.write(dest, "png", new File("C:/Users/ATUL/Desktop/english/English-page" + name + pageNo + ".png"));

		IPlainText ip = engine.RecognizeImageAsPlainText("C:/Users/ATUL/Desktop/english/English-page" + name + pageNo + ".png",
				null, null, null);
		if (xml)
		{
			ip.SaveToAsciiXMLFile("C:/Users/ATUL/Desktop/english/English-page" + name + pageNo + ".xml");
			ip.SaveToTextFile("C:/Users/ATUL/Desktop/english/English-page" + name + pageNo + ".txt",
					TextEncodingTypeEnum.TET_Simple, CodePageEnum.CP_Latin);
			return null;
		}
		else
		{
			return ip.getText();
		}
	}

	public BufferedImage deskewPerPageFull(BufferedImage bufferedImage, IEngine engine)
	{
		try
		{
			try
			{
				// engine.LoadPredefinedProfile("TextExtraction_Accuracy");
				ImageFileFormatEnum e = ImageFileFormatEnum.IFF_PngBwPng;
				ImageIO.write(bufferedImage, "png", new File("C:/Users/ATUL/Desktop/Abbyy/temp/temp.png"));
				IImageDocument img = engine.PrepareAndOpenImage("C:/Users/ATUL/Desktop/Abbyy/temp/temp.png", null, null, null);
				img.CorrectSkew(1);
				IImage dst = img.getGrayImage();
				dst.WriteToFile("C:/Users/ATUL/Desktop/Abbyy/temp/skew.png", e, null, null);
				bufferedImage = ImageIO.read(new File("C:/Users/ATUL/Desktop/Abbyy/temp/skew.png"));
				System.out.println("2. Image deskewed.");
			} finally
			{
				System.gc();
				System.runFinalization();
			}
		} catch (Exception ex)
		{
			System.out.println(ex.getMessage());
		}
		return bufferedImage;
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

					// System.out.println(ip.getText());

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
		Tiff_CroppingTest tc = new Tiff_CroppingTest();
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
		for (int k = 0; k < images.size(); k++)
		{
			BufferedImage img = images.get(k);
			Cropping_Points p = new Cropping_Points();
			p = p.cutting_point(img);

			BufferedImage dest = null;
			int midpoint = p.getMidpoint();

			if (p.getMidmaximun() >= (int) (0.01 * img.getWidth()) && p.getLine() == 0)
			{

				output.add(img.getSubimage(0, 0, midpoint, img.getHeight()));
				dest = img.getSubimage(0, 0, midpoint, img.getHeight());
				ImageIO.write(dest, "png", new File("C:/Users/ATUL/Desktop/english/English-page" + (k + 1) + ".png"));

				dest = img.getSubimage(midpoint, 0, img.getWidth() - midpoint, img.getHeight());
				ImageIO.write(dest, "png", new File("C:/Users/ATUL/Desktop/english/Arabic-page" + (k + 1) + ".png"));
				System.out.println(filename + "_" + (k + 1) + " is cropped.");
			}
			else
			{
				output.add(img.getSubimage(0, 0, img.getWidth(), img.getHeight()));

				dest = img.getSubimage(0, 0, img.getWidth(), img.getHeight());
				ImageIO.write(dest, "png", new File("C:/Users/ATUL/Desktop/english/English-page" + (k + 1) + ".png"));
				System.out.println(filename + "_" + (k + 1) + " is not cropped.");
			}

			// for finding the pagenumber
			int pX = 0;
			int pY = (int) (p.getY1() - (0.001 * img.getHeight()));
			int pwidth = img.getWidth();
			int pheight = (int) (p.getY2() - p.getY1() + 2 * (0.001 * img.getHeight()));
			pagenumber.add(img.getSubimage(pX, pY, pwidth, pheight));
			dest = img.getSubimage(pX, pY, pwidth, pheight);
			ImageIO.write(dest, "png", new File("C:/Users/ATUL/Desktop/english/Number-page" + (k + 1) + ".png"));

		}
		long d = System.currentTimeMillis();
		System.out.println(filename + " cropping time = " + (d - c));

		tc.tiff_Maker(output, result);
		tc.tiff_Maker(pagenumber, "C:/Users/ATUL/Desktop/Page-layout/testing/output/"
				+ FilenameUtils.removeExtension(tifffile.getName()) + "_bottem.tif");
		// tc.pageNo(pagenumber, filename);
		// tc.pageNo(output, filename);
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
