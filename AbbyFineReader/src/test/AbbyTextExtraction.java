package test;

import com.abbyy.FREngine.CodePageEnum;
import com.abbyy.FREngine.Engine;
import com.abbyy.FREngine.IEngine;
import com.abbyy.FREngine.IEngineLoader;
import com.abbyy.FREngine.IPlainText;
import com.abbyy.FREngine.TextEncodingTypeEnum;

public class AbbyTextExtraction
{
	public static void main(String[] args)
	{
		try
		{
			// Load the Engine
			IEngineLoader engineloader = Engine.CreateEngineOutprocLoader();
			IEngine engine = engineloader.GetEngineObject("SWTD-1000-0002-9871-8054-3276", null, null);
			// "SWTD-1000-0002-9871-7227-8145"
			try
			{
				// Load a predefined profile
				engine.LoadPredefinedProfile("TextExtraction_Accuracy");
				IPlainText ip = engine.RecognizeImageAsPlainText("C:/Users/ATUL/Desktop/Abbyy/input/inputFile.jpg", null, null,
						null);
				ip.SaveToAsciiXMLFile("C:/Users/ATUL/Desktop/Abbyy/input/inputfile.xml");
				ip.SaveToTextFile("C:/Users/ATUL/Desktop/Abbyy/input/inputfile.txt", TextEncodingTypeEnum.TET_Simple,CodePageEnum.CP_Latin);
				System.out.println("done.");
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
