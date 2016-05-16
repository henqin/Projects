package test;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import org.apache.pdfbox.exceptions.CryptographyException;
import org.apache.pdfbox.exceptions.InvalidPasswordException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDStream;
import org.apache.pdfbox.util.Matrix;
import org.apache.pdfbox.util.PDFTextStripper;
import org.apache.pdfbox.util.TextPosition;

public class PrintTextLocations extends PDFTextStripper
{

	public static StringBuilder tWord = new StringBuilder();
	public static String seek = "Building";
	public static String[] seekA = { "Building" };
	public static String key = "NVT-E-LA-Vo3-110216";
	public static List<String> wordList = new ArrayList<String>();
	public static boolean is1stChar = true;
	public static boolean lineMatch;
	public static int pageNo = 1;
	public static double lastYVal;
	
	static Matrix m=null;

	public PrintTextLocations() throws IOException
	{
		super.setSortByPosition(true);
	}

	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception
	{
		PDDocument document = null;
		try
		{
			File input = new File("C:/Users/ATUL/Desktop/Arabic/sample1.pdf");// file
			// name-------------------------------------------
			document = PDDocument.load(input);
			if (document.isEncrypted())
			{
				document.decrypt("");
			}
			PrintTextLocations printer = new PrintTextLocations();
			List<PDPage> allPages = document.getDocumentCatalog().getAllPages();

			for (int i = 0; i <1 /*allPages.size()*/; i++)
			{
				PDPage page =allPages.get(i);
				PDStream contents = page.getContents();

				if (contents != null)
				{
					printer.processStream(page, page.findResources(), page.getContents().getStream());
				}
				pageNo += 1;
				//break;
			}
			
			System.out.println();
//			for (int i = 0; i < m.length; i++)
//			{
//				
//			}
		} finally
		{
			if (document != null)
			{
				for (String s : wordList)
				{
					System.out.println(s);

				}
				document.close();
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	void getwordlocation(File f) throws IOException, CryptographyException, InvalidPasswordException
	{
		/*PrintTextLocations printer = new PrintTextLocations();
		printer.key="NVT-E-LA-Vo3-110216";
		printer.wordList=new ArrayList<String>();
		PDPage page =allPages.get(k);
		PDStream contents = page.getContents();

		if (contents != null)
		{
			printer.processStream(page, page.findResources(), page.getContents().getStream());
		}
		int size=printer.wordList.size();
		String s=printer.wordList.get(size-1);
		System.out.println(k+"page= "+printer.wordList.get(size-1));

		StringTokenizer st=new StringTokenizer(s);
		//int x=Integer.parseInt(st.nextToken());
		Float f1=Float.parseFloat(st.nextToken());
		Float f2=Float.parseFloat(st.nextToken());
		
		//int y=Integer.parseInt(st.nextToken());
		System.out.println(k+"page= "+f1+" "+f2);
		System.out.println(" ");
		*/
		//-------------------
		PDDocument document = null;
		try
		{
			File input = new File("C:/Users/ATUL/Desktop/Arabic/sample1.pdf");// file
			document = PDDocument.load(input);
			if (document.isEncrypted())
			{
				document.decrypt("");
			}
			PrintTextLocations printer = new PrintTextLocations();
			List<PDPage> allPages = document.getDocumentCatalog().getAllPages();

			for (int i = 0; i < allPages.size(); i++)
			{
				PDPage page =allPages.get(i);
				PDStream contents = page.getContents();

				if (contents != null)
				{
					printer.processStream(page, page.findResources(), page.getContents().getStream());
				}
				pageNo += 1;
				// break;
			}
		} finally
		{
			if (document != null)
			{
				for (String s : wordList)
				{
					System.out.println(s);

				}
				document.close();
			}
		}

		
	}

	@Override
	protected void processTextPosition(TextPosition text)
	{
		String tChar = text.getCharacter();
		 System.out.println("String[" + text.getXDirAdj() + "," + text.getYDirAdj() + " fs=" + text.getFontSize() + " xscale="
		 + text.getXScale() + " height=" + text.getHeightDir() + " space=" + text.getWidthOfSpace() + " width="
		 + text.getWidthDirAdj() + "]----------------" + text.getCharacter());
		String REGEX = "[,.\\[\\](:*;!?)/]";
		char c = tChar.charAt(0);
		
		m=text.getTextPos();
		
		lineMatch = matchCharLine(text);
		if ((!tChar.matches(REGEX)) && (!Character.isWhitespace(c)))
		{
			if ((!is1stChar) && (lineMatch == true))
			{
				appendChar(tChar);
			}
			else if (is1stChar == true)
			{
				setWordCoord(text, tChar);
			}
		}
		else
		{
			endWord();
		}
	}

	protected void appendChar(String tChar)
	{
		tWord.append(tChar);
		is1stChar = false;
	}

	protected void setWordCoord(TextPosition text, String tChar)
	{
		tWord.append(roundVal(Float.valueOf(text.getXDirAdj()))).append(" ").append(roundVal(Float.valueOf(text.getYDirAdj())))
				.append(" ").append(tChar);
		is1stChar = false;
	}

	protected void endWord()
	{
		String newWord = tWord.toString().replaceAll("[^\\x00-\\x7F]", "");
		//System.out.println("new word=" + newWord);
		String sWord = newWord.substring(newWord.lastIndexOf(' ') + 1);
		//System.out.println("s word=" + sWord);
		if (!"".equals(sWord))
		{
			if (key.equalsIgnoreCase(sWord))
			{
				wordList.add(newWord);
			}
			else if ("SHOWMETHEMONEY".equals(seek))
			{
				wordList.add(newWord);
			}
		}
		tWord.delete(0, tWord.length());
		is1stChar = true;
	}

	protected boolean matchCharLine(TextPosition text)
	{
		Double yVal = roundVal(Float.valueOf(text.getYDirAdj()));
		if (yVal.doubleValue() == lastYVal)
		{
			return true;
		}
		lastYVal = yVal.doubleValue();
		endWord();
		return false;
	}

	protected Double roundVal(Float yVal)
	{
		DecimalFormat rounded = new DecimalFormat("0.0'0'");
		Double yValDub = new Double(rounded.format(yVal));
		return yValDub;
	}
}
