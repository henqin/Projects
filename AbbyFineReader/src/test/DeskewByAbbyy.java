package test;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import com.abbyy.FREngine.Engine;
import com.abbyy.FREngine.IEngine;
import com.abbyy.FREngine.IEngineLoader;
import com.abbyy.FREngine.IImage;
import com.abbyy.FREngine.IImageDocument;
import com.abbyy.FREngine.ImageFileFormatEnum;

public class DeskewByAbbyy
{
	public static void main(String[] args)
	{
		try
		{
			System.out.println("Loading engine...");
			long a = System.currentTimeMillis();
			// Load the Engine
			IEngineLoader engineloader = Engine.CreateEngineOutprocLoader();
			IEngine engine = engineloader.GetEngineObject("SWTD-1000-0002-9871-7227-8145", null, null);//"SWTD-1000-0002-9871-7227-8145"
			long b = System.currentTimeMillis();
			System.out.println("Engine loading time = " + (b - a));

			try
			{
				// Load a predefined profile
				// engine.LoadPredefinedProfile("TextExtraction_Accuracy");
				ImageFileFormatEnum e = ImageFileFormatEnum.IFF_TiffBwCcittGroup4;
				IImageDocument img = engine.PrepareAndOpenImage(
						"C:/Users/ATUL/Desktop/Abbyy/input/3.jpg", null, null, null);
				img.CorrectSkew(1);
				IImage dst = img.getGrayImage();
				dst.WriteToFile("C:/Users/ATUL/Desktop/Abbyy/temp/3.tif", e, null, null);
				System.out.println("Deskew done.");
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
	}
	
	public BufferedImage rotate(BufferedImage img, double angle) throws IOException
	{
		double sin = Math.abs(Math.sin(Math.toRadians(angle))), cos = Math.abs(Math.cos(Math.toRadians(angle)));
		int w = img.getWidth(null), h = img.getHeight(null);
		int neww = (int) Math.floor(w * cos + h * sin), newh = (int) Math.floor(h * cos + w * sin);

		BufferedImage bimg = new BufferedImage(neww, newh, BufferedImage.TYPE_BYTE_BINARY);
		Graphics2D g = bimg.createGraphics();
		
		g.translate((neww - w) / 2, (newh - h) / 2);
		g.rotate(Math.toRadians(angle), w / 2, h / 2);
		g.drawRenderedImage(img, null);
		g.dispose();

		return (bimg);
	}

}
