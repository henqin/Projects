package function;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class SelectFile
{
	public File selectPdf()
	{
		JFileChooser chooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("PDF", "pdf");
		chooser.setFileFilter(filter);
		chooser.setMultiSelectionEnabled(false);
		int returnVal = chooser.showOpenDialog(null);
		File file = null;
		if (returnVal == JFileChooser.APPROVE_OPTION)
		{
			file = chooser.getSelectedFile();

		}
		return file;
	}
}
