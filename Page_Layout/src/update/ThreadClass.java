package update;

import java.io.File;
import org.apache.commons.io.FilenameUtils;

public class ThreadClass extends Thread
{
	File fileName;

	public ThreadClass()
	{
	}

	public ThreadClass(File fileName)
	{
		this.fileName = fileName;
	}

	@Override
	public void run()
	{
		String output = "C:/Users/ATUL/Desktop/Page-layout/testing/output/" + FilenameUtils.removeExtension(fileName.getName())
				+ "_English.tif";
		System.out.println(output);
		Tiff_Cropping tc = new Tiff_Cropping();
		try
		{
			tc.cutter(fileName, output);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		System.out.println(fileName.getName() + " done");
		System.out.println("ending = "+System.currentTimeMillis());

	}

}
