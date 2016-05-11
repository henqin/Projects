package test;

import java.awt.Image;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.commons.io.FilenameUtils;
import org.ghost4j.document.PDFDocument;
import org.ghost4j.renderer.SimpleRenderer;

public class PdfToImage
{

	public void convertImage(File file)
	{
		List<File> list_Files = new ArrayList<File>();
		// System.load("C:/Users/Saurabh/Desktop/jar files/ghost4j-1.0.0/lib/win32-x86-64/gsdll64.dll");
		try
		{
//			String fileName = file.getName().replace(".pdf", "");
			// load PDF document
			PDFDocument document = new PDFDocument();
			document.load(file);

			// create renderer
			SimpleRenderer renderer1 = new SimpleRenderer();

			// set resolution (in DPI)
			renderer1.setResolution(300);
			List<Image> images = renderer1.render(document);

			// write images to files to disk as PNG
			try
			{
				for (int i = 0; i < images.size(); i++)
				{
					File imagefile=new File("C:/Users/ATUL/Desktop/Tutorial/output/"+FilenameUtils.getName(file.getName())+(i+1));
					ImageIO.write((RenderedImage) images.get(i), "png", imagefile);
					list_Files.add(imagefile);
					System.out.println(imagefile.getName() + " File created in Folder: ");
				}

			} catch (IOException e)
			{
				System.out.println("ERROR: " + e.getMessage());
			}

		} catch (Exception e)
		{
			System.out.println("ERROR: " + e.getMessage());
		}
	}

	public static void main(String[] args)
	{
//		String myLibraryPath = System.getProperty("user.dir");// or another absolute or relative path

//		System.setProperty("java.library.path", myLibraryPath);
//		System.loadLibrary("gsdll64");
//		System.load("C:/Users/Saurabh/Desktop/jar files/ghost4j-1.0.0/lib/win32-x86-64/gsdll64.dll");

		File file = new File("C:/Users/ATUL/Desktop/Tutorial/input/64236_JNU-DT462_SPA.pdf");
		PdfToImage p=new PdfToImage();
		p.convertImage(file);
	}
}
