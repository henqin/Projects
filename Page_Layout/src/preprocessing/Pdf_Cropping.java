package preprocessing;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;

import com.itextpdf.text.Rectangle;
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

public class Pdf_Cropping
{
	public Pdf_Cropping()
	{
		super();
	}

	public void pdfPreProcess(File inputfile) throws Exception
	{
		Pdf_Cropping pdfcrop = new Pdf_Cropping();
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
				System.out.println("Page " + (i) + " is processing...");
				PDPage pdpage = (PDPage) allPages.get(i - 1);
				Boolean isImage = pdfcrop.pageHasImage(pdpage);

				BufferedImage bufferedImage = pdfcrop.pdfPageToBufferedImage(pdffile.getPage(i));

				Cropping_Points p = new Cropping_Points();

				p = p.cutting_point(bufferedImage);

				int pageWidth = (int) pdpage.getCropBox().getWidth();
				int pageHeight = (int) pdpage.getCropBox().getHeight();
//				if (!isImage)
//				{
//					if (p.getMidmaximun() >= (int) (0.01 * bufferedImage.getWidth()) && p.getLine() == 0)
//					{
//						int midpoint = p.getMidpoint();
//						System.out.println("3. Page is cropped.");
//						pdfcrop.parsePdf(reader, i, 0, 0, midpoint, pageHeight,
//								"C:/Users/ATUL/Desktop/english/pdf/Page" + i + "_Eng.txt");
//						pdfcrop.parsePdf(reader, i, midpoint, 0, pageWidth - midpoint, pageHeight,
//								"C:/Users/ATUL/Desktop/english/pdf/Page" + i + "_Ara.txt");
//					}
//					else
//					{
//						System.out.println("3. Page is not cropped.");
//						pdfcrop.parsePdf(reader, i, 0, 0, pageWidth, pageHeight,
//								"C:/Users/ATUL/Desktop/english/pdf/Page" + i + "_Eng.txt");
//					}
					pdfcrop.parsePdf(reader, i, 0, pageHeight-p.getY2()-10, pageWidth, pageHeight-p.getY1()+10,
							"C:/Users/ATUL/Desktop/english/pdf/Page" + i + "_PageInfo.txt");
//				}
//				else
//				{
//					System.out.println("page is Image");
//				}
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
		Rectangle cropBox = reader.getCropBox(1);
		int x = (int) cropBox.getLeft();
		int y = (int) cropBox.getBottom();// (int) bottem;//
		int width = (int) cropBox.getRight();
		int height = (int) (cropBox.getTop());// -(cropBox.getTop()*.85)//(int) top;//

		Rectangle selection = new Rectangle(x, y, width, height);
		RenderFilter renderFilter = new RegionTextRenderFilter(selection);
		LocationTextExtractionStrategy delegate = new LocationTextExtractionStrategy();
		TextExtractionStrategy extractionStrategy = new FilteredTextRenderListener(delegate, renderFilter);
		String text = PdfTextExtractor.getTextFromPage(reader, 1, extractionStrategy);
		System.out.println(text);
	}

	public String parsePdf(PdfReader pdfReader, int pageNumber, int x, int y, int width, int height, String txtFilePath)
			throws IOException
	{
		String text = null;
		PrintWriter out = new PrintWriter(new FileOutputStream(txtFilePath));
		Rectangle selection = new Rectangle(x, y, width, height);
		RenderFilter renderFilter = new RegionTextRenderFilter(selection);
		LocationTextExtractionStrategy delegate = new LocationTextExtractionStrategy();
		TextExtractionStrategy extractionStrategy = new FilteredTextRenderListener(delegate, renderFilter);
		text = PdfTextExtractor.getTextFromPage(pdfReader, pageNumber, extractionStrategy);
		System.out.println("Page Info : "+text);
		System.out.println("*******************************************************************");
		out.println(text);
		out.flush();
		out.close();
		return text;
	}

	Boolean pageHasImage(PDPage page) throws IOException
	{
		PrintImageLocations printer = new PrintImageLocations();
		printer.processStream(page, page.findResources(), page.getContents().getStream());
		if (printer.isFlag())
		{
			System.out.println("1. Page has image.");
			return true;
		}
		else
		{
			System.out.println("1. Page has not image.");
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
		System.out.println("2. page is converted into image.");
		return bufferedImage;
	}
}
