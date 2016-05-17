package preprocessing;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.FilteredTextRenderListener;
import com.itextpdf.text.pdf.parser.LocationTextExtractionStrategy;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import com.itextpdf.text.pdf.parser.RegionTextRenderFilter;
import com.itextpdf.text.pdf.parser.RenderFilter;
import com.itextpdf.text.pdf.parser.TextExtractionStrategy;

public class ArabicTxt
{
	public void parsePdf(String pdf, String txt) throws IOException, DocumentException
	{
		PdfReader reader = new PdfReader(pdf);
//		PrintWriter out = new PrintWriter(new FileOutputStream(txt));
		Rectangle rect = new Rectangle(320, 50, 15000, 20000);
		RenderFilter filter = new RegionTextRenderFilter(rect);
		TextExtractionStrategy strategy;
		String str = new String();

		for (int i = 1; i <= reader.getNumberOfPages(); i++)
		{
			strategy = new FilteredTextRenderListener(new LocationTextExtractionStrategy(), filter);
			str = PdfTextExtractor.getTextFromPage(reader, i, strategy);
		}

		char ch = 65533;
		File fileDir = new File("C:/Users/Saurabh/Desktop/output.txt");
		Writer out2 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileDir), "UTF8"));
		for (int k = 0; k < str.length(); k++)
		{
			if (str.charAt(k) != ch)
			{
				out2.append(str.charAt(k));
			}
		}
		out2.flush();
		out2.close();
	}

}
