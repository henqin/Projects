package kumar.ram;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfArray;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfReader;
import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PDFPage;

public class testing
{
	public static int pageExtraction(Image image)
	{
		int ans = 0;
		try
		{
			BufferedImage img = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_RGB);
			// Draw the image on to the buffered image
			Graphics2D bGr = img.createGraphics();
			bGr.drawImage(image, 0, 0, null);
			bGr.dispose();

			int lower = (int) (img.getWidth() * 0.45);
			int upper = (int) (img.getWidth() * 0.55);

			int height = img.getHeight();

			int[] black_pixel = new int[upper - lower + 1];
			int i, j, k;

			int final_min = Integer.MAX_VALUE, temp;
			for (k = 0, j = lower; j < upper; j++)
			{
				temp = 0;							// intialize temp
				for (i = 0; i < height; i++)
				{
					Color c = new Color(img.getRGB(j, i));
					if (!(c.getRed() >= 240 && c.getBlue() >= 240 && c.getGreen() >= 240))  // count black pixel
						temp++;
				}
				black_pixel[k] = temp;
				k++;
				if (temp < final_min)
					final_min = temp;
			}

			final_min = final_min + 5;

			int final_i = 0, final_j = 0, min, max;
			int final_lenth = Integer.MIN_VALUE;

			for (i = 0; i < black_pixel.length - 1; i++)
			{
				if (black_pixel[i] <= final_min)
				{
					min = i;
					while (black_pixel[i] <= final_min)
						i++;
					i--;
					max = i;
					if (final_lenth < max - min + 1)
					{
						final_i = min;
						final_j = max;
						final_lenth = max - min + 1;
					}
				}
				i++;
			}

			ans = (int) ((final_i + final_j) / 2) + lower;
			// System.out.println("ram");
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return ans;
	}

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

	static void Simple_splitIntoHalfPages(File source, File target, File t2) throws IOException, DocumentException
	{
		InputStream i = new FileInputStream(source);
		final PdfReader reader = new PdfReader(i);
		RandomAccessFile raf = new RandomAccessFile(source, "r");
		FileChannel channel = raf.getChannel();
		ByteBuffer buf = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
		PDFFile pdf = new PDFFile(buf);

		try (OutputStream targetStream = new FileOutputStream(target))
		{
			OutputStream ts2 = new FileOutputStream(t2);

			Document document = new Document();
			Document d2 = new Document();
			PdfCopy copy = new PdfCopy(document, targetStream);
			PdfCopy c2 = new PdfCopy(d2, ts2);
			document.open();
			d2.open();

			for (int page = 1; page <= reader.getNumberOfPages(); page++)
			{
				PDFPage pdfpage = pdf.getPage(page);
				int width = (int) pdfpage.getBBox().getWidth();
				int height = (int) pdfpage.getBBox().getHeight();
				Image image = pdfpage.getImage(width, height, null, null, true, true);
				int col = pageExtraction(image);
				System.out.println("page no=" + page + "	col_number" + col);
				PdfDictionary pageN = reader.getPageN(page);
				Rectangle cropBox = reader.getCropBox(page);
				PdfArray leftBox = new PdfArray(new float[] { cropBox.getLeft(), cropBox.getBottom(), col, cropBox.getTop() });
				PdfArray rightBox = new PdfArray(
						new float[] { col + 1, cropBox.getBottom(), cropBox.getRight(), cropBox.getTop() });

				PdfImportedPage importedPage = copy.getImportedPage(reader, page);
				pageN.put(PdfName.CROPBOX, leftBox);
				copy.addPage(importedPage);
				pageN.put(PdfName.CROPBOX, rightBox);
				c2.addPage(importedPage);
			}

			document.close();
			d2.close();
		} finally
		{
			raf.close();
			reader.close();
		}
	}

	public static void main(String args[])
	{
		// for simple splitting
		// pdf----------------------------------------------
		File f1 = new File("C:/Users/ATUL/Desktop/BlankPage_Pdf/ss.pdf");
		File f2 = new File("C:/Users/ATUL/Desktop/BlankPage_Pdf/ss_ans1.pdf");
		File f3 = new File("C:/Users/ATUL/Desktop/BlankPage_Pdf/ss_ans2.pdf");
		try
		{

			Simple_splitIntoHalfPages(f1, f2, f3);
		} catch (Exception e)
		{
			System.out.println("aa");
		}
		;

		// end--------------------------------------------------------
		System.out.println("done");

	}

}

