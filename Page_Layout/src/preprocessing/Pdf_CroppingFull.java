package preprocessing;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.io.Writer;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.util.PDFTextStripperByArea;

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
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import com.itextpdf.text.pdf.parser.RegionTextRenderFilter;
import com.itextpdf.text.pdf.parser.RenderFilter;
import com.itextpdf.text.pdf.parser.SimpleTextExtractionStrategy;
import com.itextpdf.text.pdf.parser.TextExtractionStrategy;
import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PDFPage;


import java.io.BufferedWriter;
import java.io.OutputStreamWriter;


public class Pdf_CroppingFull
{
	public Pdf_CroppingFull()
	{
		super();
	}

	public void pdfPreProcess(File inputfile) throws Exception
	{
		Pdf_CroppingFull pdfcrop = new Pdf_CroppingFull();
		Cropping_Points cp = new Cropping_Points();
		List<RdcaPreProcessData> rdcaprepracessdata = new ArrayList<RdcaPreProcessData>();

		cp.uncompressXRef(inputfile.getAbsolutePath(), "C:/Users/ATUL/Desktop/Page-layout/testing/temp/temp.pdf");
		File temp = new File("C:/Users/ATUL/Desktop/Page-layout/testing/temp/temp.pdf");

		// load a pdf from a file
		RandomAccessFile raf = new RandomAccessFile(temp, "r");
		FileChannel channel = raf.getChannel();
		ByteBuffer buf = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
		PDDocument document = null;
		try
		{
			PDFFile pdffile = new PDFFile(buf);
			document = PDDocument.load(temp);
			List allPages = document.getDocumentCatalog().getAllPages();

			InputStream inputstream = new FileInputStream(temp);
			PdfReader reader = new PdfReader(inputstream);

			// iterate through the number of pages
			for (int i = 1; i <= pdffile.getNumPages(); i++)
			{
				System.out.println("page " + (i) + " is processing...");
				BufferedImage img = pdfcrop.pdfPageToBufferedImage(pdffile.getPage(i));
				PDPage pdpage = (PDPage) allPages.get(i - 1);
				Boolean hasimage = pdfcrop.pageHasImage(pdpage);

				RdcaPreProcessData rdca = new RdcaPreProcessData();
				Cropping_Points p = new Cropping_Points();

				p = p.cutting_point(img);
				if (p.getMidmaximun() >= (int) (0.01 * img.getWidth()) && p.getLine() == 0)
				{
					int midpoint = p.getMidpoint();
					System.out.println("page is cropped.");

					rdca.setPhysicalPageNo(i);
					rdca.setActionid(null);
					rdca.setRequestid(null);
					rdca.setPageHasImage(hasimage);
					rdca.setEnglishTextExtract(false);
					rdca.setEnglishFileLocation(null);
					rdca.setEnglishCharFileLocation(null);
					rdca.setArabicTextExtract(false);
					rdca.setArabicFileLocation(null);
					rdca.setArabicCharFileLocation(null);
					rdca.setPageNo(null);
					rdca.setRevision(null);
					rdca.setMiddlePostion(midpoint);
					rdca.setBilingual(true);
					rdcaprepracessdata.add(rdca);

					// using pdfbox---
					Rectangle2D region = new Rectangle2D.Double(0, p.getY1() - 1, pdpage.getCropBox().getWidth(),
							pdpage.getCropBox().getHeight());
					// (p.getY2()-p.getY1())+1);
					String regionName = "region";
					PDFTextStripperByArea stripper;
					// ----------------------------------------
					String regionEnglish = "regionEnglish";
					String regionArabic = "regionArabic";

					Rectangle2D reEnglish = new Rectangle2D.Double(0, 0, midpoint, pdpage.getCropBox().getHeight());
					Rectangle2D reArabic = new Rectangle2D.Double(midpoint, 0, pdpage.getCropBox().getWidth(),
							pdpage.getCropBox().getHeight());
					// ----------------------------------------
					stripper = new PDFTextStripperByArea();
					stripper.addRegion(regionName, region);
					stripper.addRegion(regionEnglish, reEnglish);
					stripper.addRegion(regionArabic, reArabic);
					stripper.extractRegions(pdpage);
					System.out.println("****English text  = " + stripper.getTextForRegion(regionEnglish));

					System.out.println("****Arabic text = " + stripper.getTextForRegion(regionArabic));

					System.out.println("page number and revision = " + stripper.getTextForRegion(regionName));

					PrintWriter out = new PrintWriter(new FileOutputStream("C:/Users/ATUL/Desktop/english/Eng-page"+i+".txt"));
					out.println(stripper.getTextForRegion(regionEnglish));
					out.flush();
					out.close();
					
					PrintWriter out1 = new PrintWriter(new FileOutputStream("C:/Users/ATUL/Desktop/english/Ara-page"+i+".txt"));
					out1.println(stripper.getTextForRegion(regionArabic));
					out1.flush();
					out1.close();
					
					String str=stripper.getTextForRegion(regionArabic);
					char ch=65533;
		            File fileDir = new File("C:/Users/ATUL/Desktop/english/Ara-UTF8-page"+i+".txt");
		            Writer out2 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileDir), "UTF8"));
		            for(int k=0;k<str.length();k++)
		                if(str.charAt(k)!=ch)
		                    out2.append(str.charAt(k));


		            out2.flush();
		            out2.close();

				}
				else
				{
					System.out.println("page is not cropped.");

					rdca.setPhysicalPageNo(i);
					rdca.setActionid(null);
					rdca.setRequestid(null);
					rdca.setPageHasImage(hasimage);
					rdca.setEnglishTextExtract(false);
					rdca.setEnglishFileLocation(null);
					rdca.setEnglishCharFileLocation(null);
					rdca.setArabicTextExtract(null);
					rdca.setArabicFileLocation(null);
					rdca.setArabicCharFileLocation(null);
					rdca.setPageNo(null);
					rdca.setRevision(null);
					rdca.setMiddlePostion(null);
					rdca.setBilingual(false);
					rdcaprepracessdata.add(rdca);

					// using pdfbox---
					Rectangle2D region = new Rectangle2D.Double(0, p.getY1() - 1, pdpage.getCropBox().getWidth(),
							pdpage.getCropBox().getHeight());
					// (p.getY2()-p.getY1())+1);
					String regionName = "region";
					PDFTextStripperByArea stripper;
					// ----------------------------------------
					String regionEnglish = "regionEnglish";
					Rectangle2D reEnglish = new Rectangle2D.Double(0, 0, pdpage.getCropBox().getWidth(),
							pdpage.getCropBox().getHeight());
					// ----------------------------------------
					stripper = new PDFTextStripperByArea();
					stripper.addRegion(regionName, region);
					stripper.addRegion(regionEnglish, reEnglish);
					stripper.extractRegions(pdpage);
					System.out.println("****English text  = " + stripper.getTextForRegion(regionEnglish));

					System.out.println("page number and revision = " + stripper.getTextForRegion(regionName));

					PrintWriter out = new PrintWriter(new FileOutputStream("C:/Users/ATUL/Desktop/english/Eng-page"+i+".txt"));
					out.println(stripper.getTextForRegion(regionEnglish));
					out.flush();
					out.close();
				}

				// // using pdfbox---
				// Rectangle2D region = new Rectangle2D.Double(0,p.getY1()-1,
				// pdpage.getCropBox().getWidth(),pdpage.getCropBox().getHeight());
				// //(p.getY2()-p.getY1())+1);
				// String regionName = "region";
				// PDFTextStripperByArea stripper;
				//// ----------------------------------------
				// String regionEnglish = "regionEnglish";
				// String regionArabic = "regionArabic";
				//
				// Rectangle2D reEnglish = new Rectangle2D.Double(0,0, pdpage.getCropBox().getWidth()/
				// 2,pdpage.getCropBox().getHeight());
				// Rectangle2D reArabic = new Rectangle2D.Double(pdpage.getCropBox().getWidth()/ 2,0,
				// pdpage.getCropBox().getWidth(),pdpage.getCropBox().getHeight());
				//// ----------------------------------------
				// stripper = new PDFTextStripperByArea();
				// stripper.addRegion(regionName, region);
				// stripper.addRegion(regionEnglish, reEnglish);
				// stripper.addRegion(regionArabic, reArabic);
				// stripper.extractRegions(pdpage);
				// System.out.println("****English text = "+stripper.getTextForRegion(regionEnglish));
				//
				// System.out.println("****Arabic text = "+stripper.getTextForRegion(regionArabic));
				//
				// System.out.println("page number and revision = "+stripper.getTextForRegion(regionName));

				// Rectangle cropBox = reader.getCropBox(i);
				//
				// float bottem = cropBox.getTop() - p.getY2();
				// float top = cropBox.getTop() - p.getY1();// (float) (bottem + cropBox.getTop() * .03);
				//
				// int x = (int) cropBox.getLeft();
				// int y = (int) cropBox.getBottom();// (int) bottem;//
				// int width = (int) cropBox.getRight();
				// int height = (int) (cropBox.getTop());// -(cropBox.getTop()*.85)//(int) top;//
				//
				// final Rectangle selection = new Rectangle(x, y, width, height);
				// final RenderFilter renderFilter = new RegionTextRenderFilter(selection);
				// final LocationTextExtractionStrategy delegate = new LocationTextExtractionStrategy();
				// final TextExtractionStrategy extractionStrategy = new FilteredTextRenderListener(delegate, renderFilter);
				// String text = PdfTextExtractor.getTextFromPage(reader, i, extractionStrategy);
				// System.out.println("text = " + text);
			}
		} finally
		{
			if (document != null)
			{
				document.close();
			}
			raf.close();
			channel.close();
			buf.clear();
			temp.delete();
		}
	}

	public void parsePdf(String pdf, String txt) throws IOException
	{
		PdfReader reader = new PdfReader(pdf);
		PdfReaderContentParser parser = new PdfReaderContentParser(reader);
		PrintWriter out = new PrintWriter(new FileOutputStream(txt));
		TextExtractionStrategy strategy;
		for (int i = 1; i <= reader.getNumberOfPages(); i++)
		{
			strategy = parser.processContent(i, new SimpleTextExtractionStrategy());
			out.println(strategy.getResultantText());
		}
		out.flush();
		out.close();
	}

	Boolean pageHasImage(PDPage page) throws IOException
	{
		PrintImageLocations printer = new PrintImageLocations();
		printer.processStream(page, page.findResources(), page.getContents().getStream());
		if (printer.isFlag())
		{
			System.out.println("page has image.");
			return true;
		}
		else
		{
			System.out.println("page has not image.");
			return false;
		}
	}

	public BufferedImage pdfPageToBufferedImage(PDFPage page)
	{
		int w = (int) page.getBBox().getWidth();
		int h = (int) page.getBBox().getHeight();
		BufferedImage bufferedImage = new BufferedImage(w, h, BufferedImage.TYPE_BYTE_BINARY);
		Image img = page.getImage(w, h, null, null, true, true);
		Graphics2D g = bufferedImage.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g.drawImage(img, 0, 0, null);
		g.dispose();
		System.out.println("page is converted into image.");
		return bufferedImage;
	}

	public void cutter_For_PdfTopdf(File file, String englishfile, String arabicfile) throws Exception
	{
		Cropping_Points cp = new Cropping_Points();
		cp.uncompressXRef(file.getAbsolutePath(), "C:/Users/ATUL/Desktop/Page-layout/testing/temp/temp.pdf");
		File temp = new File("C:/Users/ATUL/Desktop/Page-layout/testing/temp/temp.pdf");

		PrintImageLocations pil = new PrintImageLocations();
		Boolean[] imagelist = pil.image(temp);
		List<BufferedImage> images = cp.pdf_To_BufferedImage1(temp);

		InputStream inputstream = new FileInputStream(temp);
		PdfReader reader = new PdfReader(inputstream);

		OutputStream os1 = new FileOutputStream(englishfile);
		OutputStream os2 = new FileOutputStream(arabicfile);

		Document document1 = new Document();
		Document document2 = new Document();

		PdfCopy pdfcopy1 = new PdfCopy(document1, os1);
		PdfCopy pdfcopy2 = new PdfCopy(document2, os2);

		document1.open();
		document2.open();

		for (int k = 0; k < images.size(); k++)
		{
			BufferedImage img = images.get(k);
			Cropping_Points p = new Cropping_Points();
			p = p.cutting_point(img);
			if (p.getMidmaximun() >= (int) (0.009 * img.getWidth()) && p.getLine() == 0)
			{
				int midpoint = p.getMidpoint();

				PdfDictionary pageN = reader.getPageN(k + 1);
				Rectangle cropBox = reader.getCropBox(k + 1);

				PdfArray leftBox = new PdfArray(
						new float[] { cropBox.getLeft(), cropBox.getBottom(), midpoint / 2, cropBox.getTop() });
				PdfArray rightBox = new PdfArray(
						new float[] { midpoint / 2, cropBox.getBottom(), cropBox.getRight(), cropBox.getTop() });

				PdfImportedPage importedPage = pdfcopy1.getImportedPage(reader, k + 1);
				pageN.put(PdfName.CROPBOX, leftBox);
				pdfcopy1.addPage(importedPage);
				pdfcopy1.clearTextWrap();

				pageN.put(PdfName.CROPBOX, rightBox);
				pdfcopy2.addPage(importedPage);
				pdfcopy2.clearTextWrap();

				System.out.println("page " + (k + 1) + " is cropped.");
				//
				// leftBox.remove(0);
				// importedPage.reset();
				float bottem = cropBox.getTop() - (p.getY2() / 2);
				if (bottem - 5 >= 0)
				{
					bottem = bottem - 5;
				}
				float top = (float) (bottem + cropBox.getTop() * .03);
				// leftBox = new PdfArray(new float[] { cropBox.getLeft(), bottem, cropBox.getRight(), top });
				// importedPage = copy.getImportedPage(reader, k + 1);
				// pageN.put(PdfName.CROPBOX, leftBox);
				// copy.addPage(importedPage);
				// importedPage.reset();
				//
				int x = (int) cropBox.getLeft();
				int y = (int) cropBox.getBottom();// (int) bottem;//
				int width = (int) cropBox.getRight();
				int height = (int) (cropBox.getTop());// -(cropBox.getTop()*.85)//(int) top;//

				final Rectangle selection = new Rectangle(x, y, width, height);
				final RenderFilter renderFilter = new RegionTextRenderFilter(selection);
				final LocationTextExtractionStrategy delegate = new LocationTextExtractionStrategy();
				final TextExtractionStrategy extractionStrategy = new FilteredTextRenderListener(delegate, renderFilter);
				String text = PdfTextExtractor.getTextFromPage(reader, k + 1, extractionStrategy);
				System.out.println(text);
				//
				// String line = text;
				// String pattern = "((\\d+) [\\w ]+ (\\d+))|(Page (\\d+) [\\w ]+ (\\d+))|(Page (\\d+))";
				//
				// // Create a Pattern object
				// Pattern r = Pattern.compile(pattern);
				//
				// // Now create matcher object.
				// Matcher m = r.matcher(line);
				// if (m.find())
				// {
				// System.out.println("Found group0 value: " + m.group(0));
				// System.out.println("Found group1 value: " + m.group(1));
				// System.out.println("Found group2 value: " + m.group(2));
				// System.out.println("Found group3 value: " + m.group(3));
				// System.out.println("Found group4 value: " + m.group(4));
				// System.out.println("Found group5 value: " + m.group(5));
				//
				// }
				// else
				// {
				// System.out.println("NO MATCH");
				// }

			}
			else
			{
				PdfDictionary pageN = reader.getPageN(k + 1);
				Rectangle cropBox = reader.getCropBox(k + 1);
				PdfArray leftBox = new PdfArray(new float[] { cropBox.getLeft(), cropBox.getBottom(), cropBox.getRight(),
						cropBox.getTop() - p.getY1() / 2 });
				PdfImportedPage importedPage = pdfcopy1.getImportedPage(reader, k + 1);
				pageN.put(PdfName.CROPBOX, leftBox);
				pdfcopy1.addPage(importedPage);

				System.out.println("page " + (k + 1) + " is not cropped.");

				leftBox.remove(0);
				importedPage.reset();
				float bottem = cropBox.getTop() - (p.getY2() / 2);
				if (bottem - 5 >= 0)
				{
					bottem = bottem - 5;
				}
				float top = (float) (bottem + cropBox.getTop() * .03);
				// leftBox = new PdfArray(new float[] { cropBox.getLeft(), bottem, cropBox.getRight(), top });
				// importedPage = copy.getImportedPage(reader, k + 1);
				// pageN.put(PdfName.CROPBOX, leftBox);
				// copy.addPage(importedPage);
				// importedPage.reset();
				//
				int x = (int) cropBox.getLeft();
				int y = (int) cropBox.getBottom();// (int) bottem;//
				int width = (int) cropBox.getRight();
				int height = (int) (cropBox.getTop());// -(cropBox.getTop()*.85)//(int) top;//

				System.out.println("Page number " + (k + 1) + "dimension : left = " + x + " right = " + width + " upper = "
						+ height + " bottem = " + y);
				final Rectangle selection = new Rectangle(x, y, width, height);
				final RenderFilter renderFilter = new RegionTextRenderFilter(selection);
				final LocationTextExtractionStrategy delegate = new LocationTextExtractionStrategy();
				final TextExtractionStrategy extractionStrategy = new FilteredTextRenderListener(delegate, renderFilter);
				String text = PdfTextExtractor.getTextFromPage(reader, k + 1, extractionStrategy);
				System.out.println(text);
				//
				// String line = text;
				// String pattern = "((\\d+) [\\w ]+ (\\d+))|(Page (\\d+) [\\w ]+ (\\d+))|(Page (\\d+))";
				//
				// // Create a Pattern object
				// Pattern r = Pattern.compile(pattern);
				//
				// // Now create matcher object.
				// Matcher m = r.matcher(line);
				// if (m.find())
				// {
				// System.out.println("Found group0 value: " + m.group(0));
				// System.out.println("Found group1 value: " + m.group(1));
				// System.out.println("Found group2 value: " + m.group(2));
				// System.out.println("Found group3 value: " + m.group(3));
				// System.out.println("Found group4 value: " + m.group(4));
				// System.out.println("Found group5 value: " + m.group(5));
				// }
				// else
				// {
				// System.out.println("NO MATCH");
				// }
			}

		}
		document1.close();
		document2.close();

		os1.close();
		os2.close();

		pdfcopy1.close();
		pdfcopy2.close();

		reader.close();
		inputstream.close();

		temp.delete();
		images.clear();
	}

}
