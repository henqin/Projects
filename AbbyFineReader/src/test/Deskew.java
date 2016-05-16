package test;

import com.abbyy.FREngine.Engine;
import com.abbyy.FREngine.IEngine;
import com.abbyy.FREngine.IEngineLoader;
import com.abbyy.FREngine.IImage;
import com.abbyy.FREngine.IImageDocument;
import com.abbyy.FREngine.ImageFileFormatEnum;

public class Deskew
{
	public void deskew()
	{
		try
		{
			// Load the Engine
			IEngineLoader engineloader = Engine.CreateEngineOutprocLoader();
			IEngine engine = engineloader.GetEngineObject("SWTD-1000-0002-9871-8054-3276", null, null);// "SWTD-1000-0002-9871-7227-8145"
			try
			{
				// Load a predefined profile
				engine.LoadPredefinedProfile("TextExtraction_Accuracy");
				ImageFileFormatEnum e = ImageFileFormatEnum.IFF_TiffBwCcittGroup4;
				for (int i = 1; i <= 1; i++)
				{
					// Process images
					IImageDocument img = engine.PrepareAndOpenImage("C:/Users/ATUL/Desktop/Abbyy/image/11.tif"/* +i+".jpg" */,
							null, null, null);
					img.CorrectSkew(1);
					IImage dst = img.getGrayImage();
					dst.WriteToFile("C:/Users/ATUL/Desktop/Abbyy/image/11_skew.tif", e, null, null);
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
	}
}
