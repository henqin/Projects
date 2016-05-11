package finaljavacode;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

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
		// TODO Auto-generated constructor stub
	}

	
	// ----------------------------------------------------------------------------------------

	public void cutter_For_PdfTopdf(File file, String target) throws Exception
	{
		Cropping_Points cp = new Cropping_Points();
		List<BufferedImage> images = cp.pdf_To_BufferedImage(file);

		InputStream inputstream = new FileInputStream(file);
		final PdfReader reader = new PdfReader(inputstream);

		OutputStream targetStream = new FileOutputStream(target);
		Document document = new Document();
		PdfCopy copy = new PdfCopy(document, targetStream);
		document.open();

		for (int k = 0; k < images.size(); k++)
		{

			BufferedImage img = images.get(k);
			Cropping_Points p = new Cropping_Points();

//			System.out.println("page "+(k+1)+" line="+p.line(img));
			
			p = p.cutting_point(img);

			if (p.getMidmaximun() >= (int) (0.009 * img.getWidth())&&p.getLine()==0)
			{
				int midpoint = (p.getM1() + p.getM2()) / 2;
				if ((p.getL2() - (int) (0.002 * img.getWidth())) > 0)
					p.setL2(p.getL2() - (int) (0.002 * img.getWidth()));

				PdfDictionary pageN = reader.getPageN(k + 1);
				Rectangle cropBox = reader.getCropBox(k + 1);
				
				PdfArray leftBox = new PdfArray(new float[] { p.getL2()/2, cropBox.getBottom(), midpoint/2, cropBox.getTop() });
				
				PdfImportedPage importedPage = copy.getImportedPage(reader, k + 1);
				pageN.put(PdfName.CROPBOX, leftBox);
				copy.addPage(importedPage);

				// -----------------------------------------------------------------------------------------
//				System.out.println("---------------------------------------------------------------------");
//
//				System.out.println("marginal width=" + (int) (0.009 * img.getWidth()));
//				System.out.println("middle margin left  " + (k + 1) + "=" + p.getM1());
//				System.out.println("middle margin right " + (k + 1) + "=" + p.getM2());
//				System.out.println("middle margin lenght" + (k + 1) + "=" + p.getMidmaximun());
//
//				BufferedImage dest=null;
//				if(p.getM2() - p.getM1()>1){System.out.println("mid croped");
//				dest = img.getSubimage(p.getM1(), 0, p.getM2() - p.getM1(), img.getHeight());
//				ImageIO.write(dest, "jpg", new File("C:/Users/ATUL/Desktop/Page-layout/skip_mid/output" + (k + 1)
//						+ "-middle-skip.jpg"));}
//
//				if(p.getL2()>1){System.out.println("left croped");
//				dest = img.getSubimage(0, 0, p.getL2(), img.getHeight());
//				ImageIO.write(dest, "jpg", new File("C:/Users/ATUL/Desktop/Page-layout/skip_left/output" + (k + 1)
//						+ "-left-skip.jpg"));}
//				if(img.getWidth() - p.getR1()>1){System.out.println("right croped");
//				dest = img.getSubimage(p.getR1(), 0, img.getWidth() - p.getR1(), img.getHeight());
//				ImageIO.write(dest, "jpg", new File("C:/Users/ATUL/Desktop/Page-layout/skip_right/output" + (k + 1)
//						+ "-right-skip.jpg"));}
				System.out.println("page " + (k + 1) + " cropped.");
			}
			else
			{
				PdfDictionary pageN = reader.getPageN(k + 1);
				Rectangle cropBox = reader.getCropBox(k + 1);
				
				PdfArray leftBox = new PdfArray(new float[] { p.getL2()/2, cropBox.getBottom(), p.getR1()/2, cropBox.getTop() });
				
				PdfImportedPage importedPage = copy.getImportedPage(reader, k + 1);
				pageN.put(PdfName.CROPBOX, leftBox);
				copy.addPage(importedPage);
//				// -----------------------------------------------------------------------------------------
//				System.out.println("width=" + (int) (0.01 * img.getWidth()));
//				System.out.println("middle margin left  " + (k + 1) + "=" + p.getM1());
//				System.out.println("middle margin right " + (k + 1) + "=" + p.getM2());
//				System.out.println("middle margin lenght" + (k + 1) + "=" + p.getMidmaximun());
				System.out.println("page " + (k + 1) + " not cropped.");
			}
		}
//				System.out.println("---------------------------------------------------------------------");
		document.close();
		reader.close();
	}

	//-----------------------------------------------------------------------------------------
	
	public void cutter_For_PdfToImage(File file, String target) throws Exception
	{
		Cropping_Points cs = new Cropping_Points();
		List<BufferedImage> images = cs.pdf_To_BufferedImage(file);
		List<BufferedImage> output = new ArrayList<BufferedImage>();

		for (int k = 0; k < images.size(); k++)
		{
			BufferedImage img = images.get(k);

			 Bilingual b = new Bilingual();
			 b.DeskewAngle(img);
			
			 img = cs.rotate(img, -Math.toDegrees(b.skew_angle));

			Cropping_Points p = new Cropping_Points();
			p = p.cutting_point(img);

			if (p.getMidmaximun() >= (int) (0.009 * img.getWidth()))
			{
				int midpoint = (p.getM1() + p.getM2()) / 2;

				if ((p.getL2() - (int) (0.002 * img.getWidth())) > 0)
					p.setL2(p.getL2() - (int) (0.002 * img.getWidth()));

				output.add(img.getSubimage(p.getL2(), 0, midpoint - p.getL2(), img.getHeight()));

				// BufferedImage dest = null;
				// if (p.getM2() - p.getM1() > 0)
				// {
				// dest = img.getSubimage(p.getM1(), 0, p.getM2() - p.getM1(), img.getHeight());
				// ImageIO.write(dest, "jpg", new File("C:/Users/ATUL/Desktop/Page-layout/skip_mid/output" + (k + 1)
				// + "-middle-skip.jpg"));
				// }
				// if (p.getL2() > 0)
				// {
				// dest = img.getSubimage(0, 0, p.getL2(), img.getHeight());
				// ImageIO.write(dest, "jpg", new File("C:/Users/ATUL/Desktop/Page-layout/skip_left/output" + (k + 1)
				// + "-left-skip.jpg"));
				// }
				//
				// if (img.getWidth() - p.getR1() > 0)
				// {
				// dest = img.getSubimage(p.getR1(), 0, img.getWidth() - p.getR1(), img.getHeight());
				// ImageIO.write(dest, "jpg", new File("C:/Users/ATUL/Desktop/Page-layout/skip_right/output" + (k + 1)
				// + "-right-skip.jpg"));
				// }
				// System.out.println("---------------------------------------------------------------------");
				System.out.println("page " + (k + 1) + " cropped.");
			}
			else
			{
				output.add(img.getSubimage(p.getL2(), 0, p.getR1() - p.getL2(), img.getHeight()));

				// System.out.println("left corner " + (k + 1) + "=" + p.getL2());
				// System.out.println("mid  point  " + (k + 1) + "=" + ((p.getM1() + p.getM2()) / 2));
				// System.out.println("right corner" + (k + 1) + "=" + p.getR1());
				// System.out.println("width  " + (k + 1) + "=" + img.getWidth());
				// System.out.println("height " + (k + 1) + "=" + img.getHeight());
				// System.out.println("---------------------------------------------------------------------");
				System.out.println("page " + (k + 1) + " not cropped.");
			}
			// b = null;
			img = null;
			p = null;
		}
		Tiff_Cropping tc = new Tiff_Cropping();
		tc.tiff_Maker(output, target);
		images.clear();
		output.clear();
	}

	
	// ----------------------------------------------------------------------------------------
	// incomplete--
	public void cutter_For_PdfToImageToPdf(File file, String target) throws Exception
	{
		Cropping_Points cp = new Cropping_Points();
		List<BufferedImage> images = cp.pdf_To_BufferedImage1(file);
		List<BufferedImage> output = new ArrayList<BufferedImage>();

		for (int k = 0; k < images.size(); k++)
		{
			BufferedImage img = images.get(k);

			Bilingual b = new Bilingual();
			b.DeskewAngle(img);
			img = cp.rotate(img, -Math.toDegrees(b.skew_angle));

			Cropping_Points p = new Cropping_Points();
			p = p.cutting_point(img);

			if (p.getMidmaximun() >= (int) (0.009 * img.getWidth()))
			{

				int midpoint = (p.getM1() + p.getM2()) / 2;
				if ((p.getL2() - (int) (0.002 * img.getWidth())) > 0)
					p.setL2(p.getL2() - (int) (0.002 * img.getWidth()));

				output.add(img.getSubimage(p.getL2(), 0, midpoint - p.getL2(), img.getHeight()));

				BufferedImage dest = img.getSubimage(p.getM1(), 0, p.getM2() - p.getM1(), img.getHeight());
				ImageIO.write(dest, "jpg", new File("C:/Users/ATUL/Desktop/Page-layout/skip_mid/output" + (k + 1)
						+ "-middle-skip.jpg"));

				dest = img.getSubimage(0, 0, p.getL2(), img.getHeight());
				ImageIO.write(dest, "jpg", new File("C:/Users/ATUL/Desktop/Page-layout/skip_left/output" + (k + 1)
						+ "-left-skip.jpg"));

				dest = img.getSubimage(p.getR1(), 0, img.getWidth() - p.getR1(), img.getHeight());
				ImageIO.write(dest, "jpg", new File("C:/Users/ATUL/Desktop/Page-layout/skip_right/output" + (k + 1)
						+ "-right-skip.jpg"));

			}
			else
			{
				output.add(img.getSubimage(p.getL2(), 0, p.getR1() - p.getL2(), img.getHeight()));

				System.out.println("minimum width=" + (int) (0.01 * img.getWidth()));
				System.out.println("total width=" + img.getWidth());
				System.out.println("middle margin left  " + (k + 1) + "=" + p.getM1());
				System.out.println("middle margin right " + (k + 1) + "=" + p.getM2());
				System.out.println("middle margin lenght" + (k + 1) + "=" + p.getMidmaximun());
				System.out.println("---------------------------------------------------------------------");

				System.out.println("page no " + (k + 1) + " is not bilingual");
			}
		}
		Tiff_Cropping tc = new Tiff_Cropping();
		tc.tiff_Maker(output, target);
	}

}
