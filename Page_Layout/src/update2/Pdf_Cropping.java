package update2;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FilenameUtils;

import com.itextpdf.text.Document;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfArray;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.FilteredTextRenderListener;
import com.itextpdf.text.pdf.parser.LocationTextExtractionStrategy;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import com.itextpdf.text.pdf.parser.RegionTextRenderFilter;
import com.itextpdf.text.pdf.parser.RenderFilter;
import com.itextpdf.text.pdf.parser.TextExtractionStrategy;

public class Pdf_Cropping
{
	public Pdf_Cropping()
	{
		super();
	}

	public void cutter_For_PdfTopdf1111(File file, String target) throws Exception
	{
		Cropping_Points cp = new Cropping_Points();
		cp.uncompressXRef(file.getAbsolutePath(), "C:/Users/ATUL/Desktop/Page-layout/testing/temp/temp.pdf");
		File temp = new File("C:/Users/ATUL/Desktop/Page-layout/testing/temp/temp.pdf");

		List<BufferedImage> images = cp.pdf_To_BufferedImage(temp);

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

				PdfArray leftBox = new PdfArray(
						new float[] { p.getL2() / 2, cropBox.getBottom(), midpoint / 2, cropBox.getTop() - p.getY1() / 2 });

				PdfImportedPage importedPage = copy.getImportedPage(reader, k + 1);
				pageN.put(PdfName.CROPBOX, leftBox);
				copy.addPage(importedPage);
				System.out.println("page " + (k + 1) + " is cropped.and y1 = " + p.getY1());

				// System.out.println(" width=" + img.getWidth());
				// System.out.println("left margin left " + (k + 1) + "=" + p.getL1());
				// System.out.println("left margin right " + (k + 1) + "=" + p.getL2());
				// System.out.println("middle margin left " + (k + 1) + "=" + p.getM1());
				// System.out.println("middle margin right " + (k + 1) + "=" + p.getM2());
				// System.out.println("right margin left " + (k + 1) + "=" + p.getR1());
				// System.out.println("right margin right " + (k + 1) + "=" + p.getR2());
				// System.out.println("middle margin lenght" + (k + 1) + "=" + p.getMidmaximun());
				// System.o0ut.println("---------------------------------------------------------------------");

				// -----------------------------------------------------------------------------------------
				// BufferedImage dest = img.getSubimage(p.getM1(), 0, p.getM2() - p.getM1(), img.getHeight());
				// ImageIO.write(dest, "jpg", new File("C:/Users/ATUL/Desktop/Page-layout/skip_mid/output" + (k + 1)
				// + "-middle-skip.jpg"));
				//
				// dest = img.getSubimage(0, 0, p.getL2(), img.getHeight());
				// ImageIO.write(dest, "jpg", new File("C:/Users/ATUL/Desktop/Page-layout/skip_left/output" + (k + 1)
				// + "-left-skip.jpg"));
				// dest = img.getSubimage(p.getR1(), 0, img.getWidth() - p.getR1(), img.getHeight());
				// ImageIO.write(dest, "jpg", new File("C:/Users/ATUL/Desktop/Page-layout/skip_right/output" + (k + 1)
				// + "-right-skip.jpg"));
			}
			else
			{
				PdfDictionary pageN = reader.getPageN(k + 1);
				Rectangle cropBox = reader.getCropBox(k + 1);

				PdfArray leftBox = new PdfArray(
						new float[] { p.getL2() / 2, cropBox.getBottom(), p.getR1() / 2, cropBox.getTop() - p.getY1() / 2 });

				PdfImportedPage importedPage = copy.getImportedPage(reader, k + 1);
				pageN.put(PdfName.CROPBOX, leftBox);
				copy.addPage(importedPage);
				System.out.println("page " + (k + 1) + " is not cropped.and y1 = " + p.getY1());

				// -----------------------------------------------------------------------------------------
				// System.out.println("width=" + (int) (0.01 * img.getWidth()));
				// System.out.println("middle margin left " + (k + 1) + "=" + p.getM1());
				// System.out.println("middle margin right " + (k + 1) + "=" + p.getM2());
				// System.out.println("middle margin lenght" + (k + 1) + "=" + p.getMidmaximun());
				// System.out.println("---------------------------------------------------------------------");
			}
		}
		document.close();
		targetStream.close();
		reader.close();
		inputstream.close();
		// temp.delete();
		images.clear();
	}

	public void cutter_For_PdfTopdf(File file, String target) throws Exception
	{
		Cropping_Points cp = new Cropping_Points();
		cp.uncompressXRef(file.getAbsolutePath(), "C:/Users/ATUL/Desktop/Page-layout/testing/temp/temp.pdf");
		File temp = new File("C:/Users/ATUL/Desktop/Page-layout/testing/temp/temp.pdf");

		List<BufferedImage> images = cp.pdf_To_BufferedImage1(temp);

		InputStream inputstream = new FileInputStream(temp);
		PdfReader reader = new PdfReader(inputstream);

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

				PdfArray leftBox = new PdfArray(
						new float[] { p.getL2() / 2, cropBox.getBottom(), midpoint / 2, cropBox.getTop() - p.getY1() / 2 });

				PdfImportedPage importedPage = copy.getImportedPage(reader, k + 1);
				pageN.put(PdfName.CROPBOX, leftBox);
				copy.addPage(importedPage);
				System.out.println("page " + (k + 1) + " is cropped.and y1 = " + p.getY1());

				leftBox.remove(0);
				importedPage.reset();
				float bottem = cropBox.getTop() - (p.getY2() / 2);
				if (bottem - 5 >= 0)
				{
					bottem = bottem - 5;
				}
				float top = (float) (bottem + cropBox.getTop() * .03);
				leftBox = new PdfArray(new float[] { cropBox.getLeft(), bottem, cropBox.getRight(), top });
				importedPage = copy.getImportedPage(reader, k + 1);
				pageN.put(PdfName.CROPBOX, leftBox);
				copy.addPage(importedPage);
				importedPage.reset();

				int x = (int) cropBox.getLeft();
				int y = (int) bottem;// (int) cropBox.getBottom();
				int width = (int) cropBox.getRight();
				int height = (int) top;// (int) (cropBox.getTop());// -(cropBox.getTop()*.85)

//				System.out.println("Page number " + (k + 1) + "dimension : left = " + x + " right = " + width + " upper = "
//						+ height + " bottem = " + y);
				final Rectangle selection = new Rectangle(x, y, width, height);
				final RenderFilter renderFilter = new RegionTextRenderFilter(selection);
				final LocationTextExtractionStrategy delegate = new LocationTextExtractionStrategy();
				final TextExtractionStrategy extractionStrategy = new FilteredTextRenderListener(delegate, renderFilter);
				String text = PdfTextExtractor.getTextFromPage(reader, k + 1, extractionStrategy);
				System.out.println(text);

				
				String line = text;
				String pattern = "((\\d+) [\\w ]+ (\\d+))|(Page (\\d+) [\\w ]+ (\\d+))|(Page (\\d+))";

				// Create a Pattern object
				Pattern r = Pattern.compile(pattern);

				// Now create matcher object.
				Matcher m = r.matcher(line);
				if (m.find())
				{
					System.out.println("Found group0 value: " + m.group(0));
					System.out.println("Found group1 value: " + m.group(1));
					System.out.println("Found group2 value: " + m.group(2));
					System.out.println("Found group3 value: " + m.group(3));
					System.out.println("Found group4 value: " + m.group(4));
					System.out.println("Found group5 value: " + m.group(5));
					
				}
				else
				{
					System.out.println("NO MATCH");
				}
				
			}
			else
			{
				PdfDictionary pageN = reader.getPageN(k + 1);
				Rectangle cropBox = reader.getCropBox(k + 1);
				PdfArray leftBox = new PdfArray(
						new float[] { cropBox.getLeft(), cropBox.getBottom(), p.getR1(), cropBox.getTop() - p.getY1() / 2 });
				PdfImportedPage importedPage = copy.getImportedPage(reader, k + 1);
				pageN.put(PdfName.CROPBOX, leftBox);
				copy.addPage(importedPage);
				System.out.println("page " + (k + 1) + " is not cropped.and y1 = " + p.getY1());

				leftBox.remove(0);
				importedPage.reset();
				float bottem = cropBox.getTop() - (p.getY2() / 2);
				if (bottem - 5 >= 0)
				{
					bottem = bottem - 5;
				}
				float top = (float) (bottem + cropBox.getTop() * .03);
				leftBox = new PdfArray(new float[] { cropBox.getLeft(), bottem, cropBox.getRight(), top });
				importedPage = copy.getImportedPage(reader, k + 1);
				pageN.put(PdfName.CROPBOX, leftBox);
				copy.addPage(importedPage);
				importedPage.reset();

				int x = (int) cropBox.getLeft();
				int y = (int) bottem;// cropBox.getBottom();
				int width = (int) cropBox.getRight();
				int height = (int) top;// (cropBox.getTop());// -(cropBox.getTop()*.85)

//				System.out.println("Page number " + (k + 1) + "dimension : left = " + x + "  right = " + width + "  upper = "
//						+ height + "  bottem = " + y);
				final Rectangle selection = new Rectangle(x, y, width, height);
				final RenderFilter renderFilter = new RegionTextRenderFilter(selection);
				final LocationTextExtractionStrategy delegate = new LocationTextExtractionStrategy();
				final TextExtractionStrategy extractionStrategy = new FilteredTextRenderListener(delegate, renderFilter);
				String text = PdfTextExtractor.getTextFromPage(reader, k + 1, extractionStrategy);
				System.out.println(text);

				String line = text;
				String pattern = "((\\d+) [\\w ]+ (\\d+))|(Page (\\d+) [\\w ]+ (\\d+))|(Page (\\d+))";

				// Create a Pattern object
				Pattern r = Pattern.compile(pattern);

				// Now create matcher object.
				Matcher m = r.matcher(line);
				if (m.find())
				{
					System.out.println("Found group0 value: " + m.group(0));
					System.out.println("Found group1 value: " + m.group(1));
					System.out.println("Found group2 value: " + m.group(2));
					System.out.println("Found group3 value: " + m.group(3));
					System.out.println("Found group4 value: " + m.group(4));
					System.out.println("Found group5 value: " + m.group(5));
				}
				else
				{
					System.out.println("NO MATCH");
				}
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
		Cropping_Points cp = new Cropping_Points();

		List<BufferedImage> input = cp.pdf_To_BufferedImage300(pdffile);
		// List<BufferedImage> images = tc.deskew(input, FilenameUtils.removeExtension(pdffile.getName()));
		// List<BufferedImage> output = new ArrayList<BufferedImage>();

		// for (int k = 0; k < images.size(); k++)
		// {
		// BufferedImage img = images.get(k);
		// Cropping_Points p = new Cropping_Points();
		// p = p.cutting_point(img);
		//
		// if (p.getMidmaximun() >= (int) (0.008 * img.getWidth()) && p.getLine() == 0)
		// {
		// int midpoint = (p.getM1() + p.getM2()) / 2;
		// if ((p.getL2() - (int) (0.002 * img.getWidth())) > 0)
		// p.setL2(p.getL2() - (int) (0.002 * img.getWidth()));
		// output.add(img.getSubimage(p.getL2(), 0, midpoint - p.getL2(), img.getHeight()));
		// System.out.println("page " + (k + 1) + " is cropped. and midlenth = "+p.getMidmaximun()+" and .007 = "+(int) (0.007 *
		// img.getWidth())+" and line = "+p.getLine());
		// }
		// else
		// {
		// output.add(img.getSubimage(p.getL2(), 0, p.getR1() - p.getL2(), img.getHeight()));
		// System.out.println("page " + (k + 1) + " is not cropped.and midlenth = "+p.getMidmaximun()+" and .007 = "+(int) (0.007
		// * img.getWidth())+" and m1="+p.getM1()+" m2=" + p.getM2());
		// }
		// }
		// tc.tiff_Maker(output, target);
		tc.tiff_Maker(input, "C:/Users/ATUL/Desktop/Page-layout/testing/output/"
				+ FilenameUtils.removeExtension(pdffile.getName()) + "_Image.tif");

	}
}
