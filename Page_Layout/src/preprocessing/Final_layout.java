package preprocessing;

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
				String english = "C:/Users/ATUL/Desktop/Page-layout/testing/output/" + FilenameUtils.removeExtension(f.getName())
						+ "_English.pdf";
				String arabic = "C:/Users/ATUL/Desktop/Page-layout/testing/output/" + FilenameUtils.removeExtension(f.getName())
						+ "_Arabic.pdf";

				Pdf_Cropping pc = new Pdf_Cropping();
				pc.pdfPreProcess(f);
//				pc.cutter_For_PdfTopdf(f, english, arabic);
				long end = System.currentTimeMillis();
				// System.out.println("output file is - " + output);
				System.out.println("and time = " + (end - start));
				System.out.println("done");
				System.out.println();
			}
			else
			{
			}
		}
		long final_E = System.currentTimeMillis();
		System.out.println("\nTotal time = " + (final_E - final_S));
	}
}
