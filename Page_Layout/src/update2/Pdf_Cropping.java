package update2;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import org.apache.commons.io.FilenameUtils;

import com.itextpdf.text.Document;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfArray;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfReader;

public class Pdf_Cropping
{
	public Pdf_Cropping()
	{
		super();
	}

	public void cutter_For_PdfTopdf(File file, String target) throws Exception
	{
		Cropping_Points cp = new Cropping_Points();
		cp.uncompressXRef(file.getAbsolutePath(), "C:/Users/ATUL/Desktop/Page-layout/testing/temp/temp.pdf");
		File temp = new File("C:/Users/ATUL/Desktop/Page-layout/testing/temp/temp.pdf");

		List<BufferedImage> images = cp.pdf_To_BufferedImage1(temp);

		InputStream inputstream = new FileInputStream(temp);
		final PdfReader reader = new PdfReader(inputstream);

		OutputStream targetStream = new FileOutputStream(target);
		Document document = new Document();
		PdfCopy copy = new PdfCopy(document, targetStream);
		document.open();

		for (int k = 0; k < images.size(); k++)
		{
			BufferedImage img = images.get(k);
			Cropping_Points p = new Cropping_Points();
			p = p.cutting_point(img);
			if (p.getMidmaximun() >= (int) (0.009 * img.getWidth()) && p.getLine() == 0)
			{
				int midpoint = (p.getM1() + p.getM2()) / 2;
				if ((p.getL2() - (int) (0.002 * img.getWidth())) > 0)
					p.setL2(p.getL2() - (int) (0.002 * img.getWidth()));
				PdfDictionary pageN = reader.getPageN(k + 1);
				Rectangle cropBox = reader.getCropBox(k + 1);
				PdfArray leftBox = new PdfArray(new float[] { p.getL2()/2, cropBox.getBottom(), midpoint/2,
						cropBox.getTop() - p.getY1()/2 });
				PdfImportedPage importedPage = copy.getImportedPage(reader, k + 1);
				pageN.put(PdfName.CROPBOX, leftBox);
				copy.addPage(importedPage);
				System.out.println("page " + (k + 1) + " is cropped.and y1 = " + p.getY1());
			}
			else
			{
				PdfDictionary pageN = reader.getPageN(k + 1);
				Rectangle cropBox = reader.getCropBox(k + 1);
				PdfArray leftBox = new PdfArray(new float[] { p.getL2()/2, cropBox.getBottom(), p.getR1()/2,
						cropBox.getTop() - p.getY1()/2 });
				PdfImportedPage importedPage = copy.getImportedPage(reader, k + 1);
				pageN.put(PdfName.CROPBOX, leftBox);
				copy.addPage(importedPage);
				System.out.println("page " + (k + 1) + " is not cropped.and y1 = " + p.getY1());
			}
		}
		document.close();
		targetStream.close();
		reader.close();
		inputstream.close();
		temp.delete();
		images.clear();
	}

	public void cutter_For_PdfToTiff(File pdffile, String target) throws IOException
	{

		Tiff_Cropping tc = new Tiff_Cropping();
		Cropping_Points cp=new Cropping_Points();
		
		List<BufferedImage> input = cp.pdf_To_BufferedImage300(pdffile);
//		List<BufferedImage> images = tc.deskew(input, FilenameUtils.removeExtension(pdffile.getName()));
//		List<BufferedImage> output = new ArrayList<BufferedImage>();

//		for (int k = 0; k < images.size(); k++)
//		{
//			BufferedImage img = images.get(k);
//			Cropping_Points p = new Cropping_Points();
//			p = p.cutting_point(img);
//
//			if (p.getMidmaximun() >= (int) (0.008 * img.getWidth()) && p.getLine() == 0)
//			{
//				int midpoint = (p.getM1() + p.getM2()) / 2;
//				if ((p.getL2() - (int) (0.002 * img.getWidth())) > 0)
//					p.setL2(p.getL2() - (int) (0.002 * img.getWidth()));
//				output.add(img.getSubimage(p.getL2(), 0, midpoint - p.getL2(), img.getHeight()));
//				System.out.println("page " + (k + 1) + " is cropped. and midlenth = "+p.getMidmaximun()+" and .007 = "+(int) (0.007 * img.getWidth())+" and line = "+p.getLine());
//			}
//			else
//			{
//				output.add(img.getSubimage(p.getL2(), 0, p.getR1() - p.getL2(), img.getHeight()));
//				System.out.println("page " + (k + 1) + " is not cropped.and midlenth = "+p.getMidmaximun()+" and .007 = "+(int) (0.007 * img.getWidth())+" and m1="+p.getM1()+"  m2=" + p.getM2());
//			}
//		}
//		tc.tiff_Maker(output, target);
		tc.tiff_Maker(input, "C:/Users/ATUL/Desktop/Page-layout/testing/output/" + FilenameUtils.removeExtension(pdffile.getName())
						+ "_Image.tif");
	
	}
}
