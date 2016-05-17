package preprocessing;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.media.jai.NullOpImage;
import javax.media.jai.OpImage;
import javax.media.jai.PlanarImage;

import com.abbyy.FREngine.IEngine;
import com.abbyy.FREngine.IImage;
import com.abbyy.FREngine.IImageDocument;
import com.abbyy.FREngine.IPlainText;
import com.abbyy.FREngine.ImageFileFormatEnum;
import com.sun.media.jai.codec.FileSeekableStream;
import com.sun.media.jai.codec.ImageCodec;
import com.sun.media.jai.codec.ImageDecoder;
import com.sun.media.jai.codec.SeekableStream;

public class Tiff_Cropping
{

	public Tiff_Cropping()
	{
		super();
	}

	public void tiffPreProcessing(File inputfile, IEngine engine) throws IOException
	{
		Tiff_Cropping tc = new Tiff_Cropping();

		SeekableStream ss = new FileSeekableStream(inputfile);
		ImageDecoder decoder = ImageCodec.createImageDecoder("tiff", ss, null);
		int numPages = decoder.getNumPages();
		try
		{
			for (int i = 0; i < numPages; i++)
			{
				System.out.println("Image " + (i + 1) + " is processing....");
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
					ImageIO.write(dest, "png", new File("C:/Users/ATUL/Desktop/english/Page_" + (i + 1) + "_Eng.png"));

					dest = bufferedImage.getSubimage(midpoint, 0, imgWidth - midpoint, imgHeight);
					ImageIO.write(dest, "png", new File("C:/Users/ATUL/Desktop/english/Page_" + (i + 1) + "_Ara.png"));
					System.out.println("3. Image cropped.");
				}
				else
				{
					dest = bufferedImage.getSubimage(0, 0, imgWidth, imgHeight);
					ImageIO.write(dest, "png", new File("C:/Users/ATUL/Desktop/english/Page_" + (i + 1) + "_Eng.png"));
					System.out.println("3. Image not cropped.");
				}
				dest = bufferedImage.getSubimage(0, cp.getY1()-5, imgWidth, cp.getY2() - cp.getY1()+10);
				String pageInfo = tc.extractText(engine, dest, i + 1);
				System.out.print("4. Image Footer : ");
				System.out.println(pageInfo);
				
				op.dispose();
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
		ImageIO.write(dest, "png", new File("C:/Users/ATUL/Desktop/english/Page_" + pageNo + "_Footer.png"));
		IPlainText ip = engine.RecognizeImageAsPlainText("C:/Users/ATUL/Desktop/english/English-pageNo" + pageNo + ".png", null,
				null, null);
		return ip.getText();
//		return null;
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

}