package function;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.commons.io.FilenameUtils;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PDFPage;

public class PdfToImageConversion
{
	public PdfToImageConversion()
	{
		super();
	}

	public void pdfToImage(File file) throws Exception
	{

		try
		{
			String outputFile = file.getName().substring(0, file.getName().lastIndexOf("."));

			// load a pdf from a file
			RandomAccessFile raf = new RandomAccessFile(file, "r");
			FileChannel channel = raf.getChannel();
			ByteBuffer buf = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
			PDFFile pdffile = new PDFFile(buf);

			int No_of_pages = pdffile.getNumPages();  // get number of pages
			// iterate through the number of pages
			for (int i = 1; i <= No_of_pages; i++)
			{
				PDFPage page = pdffile.getPage(i);

				int w = 4 * (int) page.getBBox().getWidth();
				int h = 4 * (int) page.getBBox().getHeight();

				System.out
						.println("page dimension=" + (int) page.getBBox().getWidth() + " x " + (int) page.getBBox().getHeight());
				// create new image

				BufferedImage bufferedImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);

				Image img = page.getImage(w, h, null, null, true, true);
				// width , height , clip-rect , null for the ImageObserver , fill background with white , block until drawing is
				// done

				Graphics g = bufferedImage.createGraphics();
				g.drawImage(img, 0, 0, null);
				g.dispose();

				File asd = new File("C:/Users/ATUL/Desktop/Tutorial/output/" + outputFile + i + ".tif");
				ImageIO.write(bufferedImage, "png", asd);
				System.out.println("page no " + i + " is converted in image");

			}
			raf.close();
		} catch (Exception e)
		{
			System.out.println("file not supported...");
			System.out.println(e);
		}
	}

	public void uncompressXRef(String src, String dest) throws IOException, DocumentException
	{
		PdfReader reader = new PdfReader(src);
		PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(dest));
		stamper.close();
		reader.close();
	}

	public static void main(String[] args) throws Exception
	{

		List<File> files = new ArrayList<File>();

		// Process with ABBYY FineReader Engine
		File file = new File("C:/Users/ATUL/Desktop/Tutorial/input/");

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
				PdfToImageConversion p = new PdfToImageConversion();
				p.uncompressXRef(f.getAbsolutePath(), "C:/Users/ATUL/Desktop/Tutorial/temp/"+f.getName());
				File temp = new File("C:/Users/ATUL/Desktop/Tutorial/temp/"+f.getName());

				PdfToImageConversion pti = new PdfToImageConversion();
				pti.pdfToImage(temp);
				long end = System.currentTimeMillis();
				System.out.println("time = " + (end - start));
				System.out.println("done");
				System.out.println();
			}
		}
	}
}
