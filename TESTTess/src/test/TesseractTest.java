package test;

import java.io.File;

import javax.imageio.ImageIO;

import net.sourceforge.tess4j.Tesseract;

public class TesseractTest
{
	public static void main(String[] args)
	{
		StringBuilder builder = new StringBuilder();
		try
		{
			File image = new File("C:/Users/ATUL/Desktop/1.jpg");

			Tesseract instance = Tesseract.getInstance();
			instance.setLanguage("eng");
			instance.setDatapath("");//C:/Tess4J/tessdata/
			ImageIO.scanForPlugins();
			String result = instance.doOCR(image);
			builder.append(result);
			System.out.println(result);
		} catch (Throwable e)
		{
			System.out.println("ERROR : " + e);
		}
		System.out.println("done");
	}

}
