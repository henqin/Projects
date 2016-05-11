package arabic;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.fontbox.cmap.CMap;
import org.apache.fontbox.cmap.CMapParser;
import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;

public class PDFTextParser
{

	// Extract text from PDF Document
	static String pdftoText(String fileName) throws FileNotFoundException, IOException
	{
		String parsedText = null;
		PDFTextStripper pdfStripper = null;
		File file = new File(fileName);

		
		PDFParser parser;
		parser = new PDFParser(new FileInputStream(file));
		parser.parse();
		
		COSDocument cosDoc = parser.getDocument();
		pdfStripper = new PDFTextStripper();
		PDDocument pdDoc = new PDDocument(cosDoc);
		pdfStripper.setStartPage(1);
		pdfStripper.setEndPage(2);
		parsedText = pdfStripper.getText(pdDoc);
		cosDoc.close();
		pdDoc.close();
		return parsedText;
	}

	public static void main(String args[]) throws FileNotFoundException, IOException
	{
		System.out.println(pdftoText("C:/Users/ATUL/Desktop/Arabic/sample.pdf"));
	}

}

