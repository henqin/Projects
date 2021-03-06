package arabic;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;

import org.apache.commons.io.FileUtils;
import org.apache.fontbox.encoding.Encoding;
import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfString;
import com.itextpdf.text.pdf.parser.LocationTextExtractionStrategy;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import com.itextpdf.text.pdf.parser.SimpleTextExtractionStrategy;
import com.itextpdf.text.pdf.parser.TextExtractionStrategy;

public class ArabicExtraction
{
	public void parsePdf(String pdf, String txt) throws IOException
	{
		System.out.println("***********************************************************************");
		PdfReader reader = new PdfReader(pdf);
		reader.getCatalog().put(PdfName.LANG, new PdfString("ar-EG"));
		PdfReaderContentParser parser = new PdfReaderContentParser(reader);
		PrintWriter out = new PrintWriter(new FileOutputStream(txt));
		TextExtractionStrategy strategy;
		for (int i = 1; i <= 1 /* reader.getNumberOfPages() */; i++)
		{
			strategy = parser.processContent(i, new LocationTextExtractionStrategy());
			out.println(strategy.getResultantText());
			System.out.println(strategy.getResultantText());
		}
		out.flush();
		out.close();
		System.out.println("***********************************************************************");
	}

	void testing(String in, String out) throws IOException
	{
		// byte[] pdfbytes =OutputStream.toByteArray();
		System.load("C:/Users/ATUL/Desktop/Arabic/itextsharp.dll");

		byte[] pdfbytes = FileUtils.readFileToByteArray(new File(in));
		PdfReader reader = new PdfReader(in);

		int pagenumber = reader.getNumberOfPages();
		PdfTextExtractor extractor = null;

		for (int i = 1; i <= pagenumber; i++)
		{
			System.out.println("============PAGE NUMBER " + i + "=============");
			String line = PdfTextExtractor.getTextFromPage(reader, i);
			System.out.println(line);
		}

	}

	void testing1(String in, String out)
	{
		PDFTextStripper pdfStripper = null;
		PDDocument pdDoc = null;
		COSDocument cosDoc = null;
		File file = new File(in);
		try
		{
			PDFParser parser = new PDFParser(new FileInputStream(file));
			parser.parse();
			cosDoc = parser.getDocument();
			pdfStripper = new PDFTextStripper();
			pdDoc = new PDDocument(cosDoc);
			// Document luceneDocument = LucenePDFDocument.getDocument( ... );

			pdfStripper.setStartPage(1);
			pdfStripper.setEndPage(5);
			String parsedText = pdfStripper.getText(pdDoc);
			System.out.println(parsedText);
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	void testing2(String in, String out) throws IOException
	{
		InputStream is = new FileInputStream(in);
		InputStreamReader i = new InputStreamReader(is);

		System.out.println("This text has \u0634\u0627\u062f\u062c\u0645\u0647\u0648\u0631 123,456 \u0645\u0646");
		// int data = i.read();
		// while (data != -1)
		// {
		// char theChar = (char) data;
		// if (data >= 65 && data <= 122)
		// System.out.print(theChar);
		// data = i.read();
		// }

		PDFTextStripper pdfStripper = null;
		PDDocument pdDoc = null;
		COSDocument cosDoc = null;
		File file = new File(in);
		PDFParser parser = new PDFParser(new FileInputStream(file));
		parser.parse();
		cosDoc = parser.getDocument();
		pdfStripper = new PDFTextStripper();
		pdDoc = new PDDocument(cosDoc);

		pdfStripper.setStartPage(1);
		pdfStripper.setEndPage(1);
		String parsedText = pdfStripper.getText(pdDoc);
		// System.out.println(parsedText);
	}

	void testing3(String in, String out) throws IOException, DocumentException
	{
		StringBuilder text = new StringBuilder();
//		PdfReader pdfReader = new PdfReader(fileName);
//		for (int page = 1; page <= pdfReader.NumberOfPages; page++)
//		{
//			SimpleTextExtractionStrategy strategy = new SimpleTextExtractionStrategy();
//			String currentText = PdfTextExtractor.getTextFromPage(pdfReader, page, strategy);
//			currentText = Encoding.UTF8
//					.GetString(ASCIIEncoding.Convert(Encoding.Default, Encoding.UTF8, Encoding.Default.GetBytes(currentText)));
//			text.Append(currentText);
//		}
//		pdfReader.Close();
//		System.out.println(text.toString());

	}

	public static void main(String[] args) throws IOException, DocumentException
	{
		new ArabicExtraction().testing3("C:/Users/ATUL/Desktop/Arabic/sample.pdf", "C:/Users/ATUL/Desktop/Arabic/result.txt");
	}
}