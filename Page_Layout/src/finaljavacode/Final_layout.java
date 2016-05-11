package finaljavacode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FilenameUtils;

public class Final_layout
{
	
	public static void main(String[] args) throws Exception
	{

		List<File> files = new ArrayList<File>();

		// Process with ABBYY FineReader Engine
		File file = new File("C:/Users/ATUL/Desktop/Page-layout/Work/Mar11-2016/");

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
			if (FilenameUtils.getExtension(f.getName()).compareToIgnoreCase("pdf") == 0)
			{
				long start=System.currentTimeMillis();
				System.out.println(f.getName());
				String output = "C:/Users/ATUL/Desktop/Page-layout/Work/result/" + FilenameUtils.removeExtension(f.getName()) + "_inEnglish.pdf";
				System.out.println(output);
				Pdf_Cropping pc=new Pdf_Cropping();
				pc.cutter_For_PdfTopdf(f, output);
				long end=System.currentTimeMillis();
				System.out.println("time="+(end-start));
				System.out.println("done");
				System.out.println();
			}
			else
			{
				long start=System.currentTimeMillis();
				System.out.println(f.getName());
				String output = "C:/Users/ATUL/Desktop/Page-layout/Work/result/" + FilenameUtils.removeExtension(f.getName()) + "_inEnglish.tif";
				System.out.println(output);
				Tiff_Cropping pc=new Tiff_Cropping();
				pc.cutter(f, output);
				long end=System.currentTimeMillis();
				System.out.println("time="+(end-start));
				System.out.println("done");
				System.out.println();

			}

		}

		// String tiff = "11";
		// File tifffile = new File("C:/Users/ATUL/Desktop/Page-layout/" + tiff + ".tif");
		//
		// // String pdf = "Master_SPA";
		// // File pdffile = new File("C:/Users/ATUL/Desktop/Page-layout/" + pdf + ".pdf");
		//
		//
		//
		// if (FilenameUtils.getExtension(tiff.getName()).compareToIgnoreCase("pdf") == 0)
		//
		// // String output = "C:/Users/ATUL/Desktop/Page-layout/" + pdf + "-inEnglish.tif";
		// // Pdf_Cropping.cutter_For_PdfToImage(pdffile, output);
		//
		// // String output = "C:/Users/ATUL/Desktop/Page-layout/" + pdf + "-inEnglish.pdf";
		// // Pdf_Cropping.cutter_For_PdfTopdf(pdffile, output);
		//
		// // String output = "C:/Users/ATUL/Desktop/Page-layout/" + pdf + "-inEnglish.pdf";
		// // cutter_For_PdfToImageToPdf(pdffile, output);
		//
		// String output = "C:/Users/ATUL/Desktop/Page-layout/ram-" + tiff + "-inEnglish.tif";
		// Tiff_Cropping.cutter(tifffile, output);
		//
		// endTime = System.currentTimeMillis();
		// System.out.println("total time=" + (endTime - startTime));

	}

}
