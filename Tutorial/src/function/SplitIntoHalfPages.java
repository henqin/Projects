package function;

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

import javax.imageio.ImageIO;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfArray;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PDFPage;

public class SplitIntoHalfPages
{
	public static void main(String[] args) throws IOException, DocumentException
	{
		SplitIntoHalfPages s=new SplitIntoHalfPages();
		s.uncompressXRef("C:/Users/ATUL/Desktop/Arabic/sample.pdf", "C:/Users/ATUL/Desktop/Arabic/sample1.pdf");
		s.Simple_splitIntoHalfPages(new File("C:/Users/ATUL/Desktop/Arabic/sample1.pdf"), new File("C:/Users/ATUL/Desktop/Arabic/english.pdf"), new File("C:/Users/ATUL/Desktop/Arabic/arabic.pdf"));
	}
	
	public void uncompressXRef(String src, String dest) throws IOException, DocumentException
	{
		PdfReader reader = new PdfReader(src);
		PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(dest));
		stamper.close();
		reader.close();
	}
	
	public void Simple_splitIntoHalfPages(File source, File dest1, File dest2) throws IOException, DocumentException
	{
		InputStream is = new FileInputStream(source);
		final PdfReader reader = new PdfReader(is);
		RandomAccessFile raf = new RandomAccessFile(source, "r");
		FileChannel channel = raf.getChannel();
		ByteBuffer buf = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
		PDFFile pdf = new PDFFile(buf);

		try
		{
			OutputStream os1 = new FileOutputStream(dest1);
			OutputStream os2 = new FileOutputStream(dest2);

			Document d1 = new Document();
			Document d2 = new Document();
			PdfCopy c1 = new PdfCopy(d1, os1);
			PdfCopy c2 = new PdfCopy(d2, os2);
			d1.open();
			d2.open();

			for (int page = 1; page <= reader.getNumberOfPages(); page++)
			{
				PDFPage pdfpage = pdf.getPage(page);
				int width = (int) pdfpage.getBBox().getWidth();
				int height = (int) pdfpage.getBBox().getHeight();
				Image image = pdfpage.getImage(width, height, null, null, true, true);

				ImageIO.write((BufferedImage) (image), "png", new File("C:/Users/ATUL/Desktop/Page-layout/z" + page + ".png"));

				PdfDictionary pageN = reader.getPageN(page);
				Rectangle cropBox = reader.getCropBox(page);
				
				PdfArray leftBox = new PdfArray(new float[] { cropBox.getLeft(), cropBox.getBottom(), cropBox.getRight() / 2,
						cropBox.getTop() });
				PdfArray rightBox = new PdfArray(new float[] { cropBox.getRight() / 2, cropBox.getBottom(), cropBox.getRight(),
						cropBox.getTop() });

				PdfImportedPage importedPage = c1.getImportedPage(reader, page);
				pageN.put(PdfName.CROPBOX, leftBox);
				c1.addPage(importedPage);
				c1.clearTextWrap();
				
				pageN.put(PdfName.CROPBOX, rightBox);
				c2.addPage(importedPage);
				c2.clearTextWrap();
			}

			d1.close();
			d2.close();
			os1.close();
			os2.close();
			c1.close();
			c2.close();
		} finally
		{
			raf.close();
			reader.close();
			is.close();
		}
	}
}
