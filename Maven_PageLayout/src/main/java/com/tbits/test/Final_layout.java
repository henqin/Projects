package com.tbits.test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FilenameUtils;

public class Final_layout
{
	public static void main(String[] args) throws Exception
	{
		long final_S = System.currentTimeMillis();
		List<File> files = new ArrayList<File>();
		File file = new File("C:/Users/ATUL/Desktop/Page-layout/testing/input/");
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
				long start = System.currentTimeMillis();
				System.out.println(f.getName());
				String output = "C:/Users/ATUL/Desktop/Page-layout/testing/output/" + FilenameUtils.removeExtension(f.getName())
						+ "_English.pdf";
				Pdf_Cropping pc = new Pdf_Cropping();
				pc.cutter_For_PdfToTiff(f, output);
				long end = System.currentTimeMillis();
				System.out.println("output file is - " + output);
				System.out.println("and time = " + (end - start));
				System.out.println("done");
				System.out.println();
			}
			else
			{
				long start = System.currentTimeMillis();
				System.out.println(f.getName());
				String output = "C:/Users/ATUL/Desktop/Page-layout/testing/output/" + FilenameUtils.removeExtension(f.getName())
						+ "_English.tif";
				System.out.println(output);
				Tiff_Cropping tc = new Tiff_Cropping();
				tc.cutter(f, output);
				long end = System.currentTimeMillis();
				System.out.println("time=" + (end - start));
				System.out.println("done");
				System.out.println();
			}
		}
		long final_E = System.currentTimeMillis();
		System.out.println("\nTotal time = " + (final_E - final_S));
	}
}
