package test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FilenameUtils;

import com.abbyy.FREngine.Engine;
import com.abbyy.FREngine.FileExportFormatEnum;
import com.abbyy.FREngine.IBaseLanguage;
import com.abbyy.FREngine.IEngine;
import com.abbyy.FREngine.IEngineLoader;
import com.abbyy.FREngine.IFRDocument;
import com.abbyy.FREngine.IImage;
import com.abbyy.FREngine.IImageDocument;
import com.abbyy.FREngine.IImageProcessingParams;
import com.abbyy.FREngine.IPDFExportParams;
import com.abbyy.FREngine.IPageProcessingParams;
import com.abbyy.FREngine.ImageFileFormatEnum;
import com.abbyy.FREngine.PDFExportScenarioEnum;

public class AbbyyTesting
{

	public AbbyyTesting()
	{
		super();
	}

	public static void main(String[] args) throws Exception
	{
		AbbyyTesting abbyy = new AbbyyTesting();
		long final_S = System.currentTimeMillis();
		List<File> files = new ArrayList<File>();

		// Process with ABBYY FineReader Engine
		File file = new File("C:/Users/ATUL/Desktop/Abbyy/input/");
		for (File f : file.listFiles())
		{
			if (f.isDirectory())
			{
			}
			else
			{
				files.add(f);
			}
		}

		for (File f : files)
		{
			long start = System.currentTimeMillis();
			System.out.println(f.getPath());
			String output_rtf = "C:/Users/ATUL/Desktop/Abbyy/output_rtf/" + FilenameUtils.removeExtension(f.getName()) + ".rtf";
			String output_pdf = "C:/Users/ATUL/Desktop/Abbyy/output_rtf/" + FilenameUtils.removeExtension(f.getName()) + ".pdf";

			abbyy.fineReader(f.getPath(), output_rtf, output_pdf);
			// abbyy.deskew();
			// abbyy.test(output_rtf);
			long end = System.currentTimeMillis();
			// System.out.println("output file is - " + output_rtf);
			System.out.println("and take time = " + (end - start));

		}
		long final_E = System.currentTimeMillis();
		System.out.println("\nTotal time = " + (final_E - final_S));
	}

	public void fineReader(String input, String output_rtf, String output_pdf)
	{
		try
		{
			System.out.println("Loading engine...");
			long a = System.currentTimeMillis();
			// Load the Engine
			IEngineLoader engineloader = Engine.CreateEngineOutprocLoader();
			IEngine engine = engineloader.GetEngineObject("SWTD-1000-0002-9871-8054-3276", null, null);// "SWTD-1000-0002-9871-7227-8145"
			long b = System.currentTimeMillis();
			System.out.println("Engine loading time = " + (b - a));
			try
			{
				// Load a predefined profile
				engine.LoadPredefinedProfile("DocumentConversion_Accuracy");

				// Process images
				IFRDocument document = engine.CreateFRDocument();
				String imagePath = input;
				document.AddImageFile(imagePath, null, null);
				document.Process(null, null, null);
				System.out.println(document.getPages().getCount());
				
				document.getPlainText().getText();

				String rtfExportPath = output_rtf;
				document.Export(rtfExportPath, FileExportFormatEnum.FEF_RTF, null);

				IPDFExportParams pdfParams = engine.CreatePDFExportParams();
				pdfParams.setScenario(PDFExportScenarioEnum.PES_Balanced);

				String pdfExportPath = output_pdf;
				document.Export(pdfExportPath, FileExportFormatEnum.FEF_PDF, pdfParams);
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

	void test(String imagePath)
	{
		try
		{
			IEngine engine = Engine.Load("C:/Program Files (x86)/ABBYY SDK/10/FineReader Engine/Bin", "SWTD10000002987180543276",
					null, null);
			try
			{
				IPageProcessingParams p = engine.CreatePageProcessingParams();
				p.LoadFromFile(imagePath);

				IImageProcessingParams pp = engine.CreateImageProcessingParams();
				pp.LoadFromFile(imagePath);

				IBaseLanguage b = engine.CreateBaseLanguage();
				b.LoadFromFile(imagePath);
				System.out.println("done");

				IImageDocument i = engine.PrepareAndOpenImage("C:/Users/ATUL/Desktop/3.tif", null, null, null);
				System.out.println(i.getSkewAngle());
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

	public void deskew()
	{

		try
		{
			// Load the Engine
			IEngine engine = Engine.Load("C:/Program Files (x86)/ABBYY SDK/10/FineReader Engine/Bin", "SWTD10000002987180543276",
					null, null);
			try
			{
				// Load a predefined profile
				engine.LoadPredefinedProfile("TextExtraction_Accuracy");

				ImageFileFormatEnum e = ImageFileFormatEnum.IFF_TiffBwCcittGroup4;
				for (int i = 1; i <= 1; i++)
				{
					long a = System.currentTimeMillis();
					// Process images

					IImageDocument img = engine.PrepareAndOpenImage("C:/Users/ATUL/Desktop/Abbyy/image/11.tif"/* +i+".jpg" */,
							null, null, null);
					Double d = img.getSkewAngle();
					img.CorrectSkew(1);
					IImage dst = img.getGrayImage();

					dst.WriteToFile("C:/Users/ATUL/Desktop/Abbyy/image/11_skew.tif"/* +i+"_skew.tif" */, e, null, null);
					long b = System.currentTimeMillis();
					System.out.println("time = " + (b - a) + " for image " + i + "skew angle before=" + d + " and image skew ="
							+ img.getIsSkewCorrected() + " and skew angle = " + img.getSkewAngle());

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
