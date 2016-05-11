package test;

import static net.sourceforge.tess4j.ITessAPI.TRUE;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import javax.imageio.ImageIO;

import com.sun.jna.Pointer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.tess4j.TessAPI;
import net.sourceforge.tess4j.TessAPI1;
import net.sourceforge.tess4j.ITessAPI.ETEXT_DESC;
import net.sourceforge.tess4j.ITessAPI.TessBaseAPI;
import net.sourceforge.tess4j.ITessAPI.TessPageIterator;
import net.sourceforge.tess4j.ITessAPI.TessPageSegMode;
import net.sourceforge.tess4j.ITessAPI.TessResultIterator;
import net.sourceforge.tess4j.util.ImageIOHelper;
import net.sourceforge.tess4j.util.LoadLibs;

public class FooterExtraction
{
	@SuppressWarnings("static-access")
	public static void crop()
	{
		String target = "PSDA";
		int targetlen = target.length();

		testsimilarity.Main m = new testsimilarity.Main();

		TessAPI api = LoadLibs.getTessAPIInstance();
		TessBaseAPI handle = TessAPI1.TessBaseAPICreate();
		String datapath = "C:/Tesseract-OCR/tessdata/";
		String language = "eng";

		File tiff = new File("C:/Users/ATUL/Desktop/Page-layout/1.tif");
		BufferedImage image = null;
		@SuppressWarnings("unused")
		BufferedImage newimg = null;
		try
		{
			image = ImageIO.read(new FileInputStream(tiff));
			newimg = ImageIO.read(tiff);
		} catch (FileNotFoundException e)
		{

			e.printStackTrace();
		} catch (IOException e)
		{

			e.printStackTrace();
		}

		ByteBuffer buf = ImageIOHelper.convertImageData(image);
		int bpp = image.getColorModel().getPixelSize();
		int bytespp = bpp / 8;
		int bytespl = (int) Math.ceil(image.getWidth() * bpp / 8.0);

		api.TessBaseAPIInit3(handle, datapath, language);
		api.TessBaseAPISetPageSegMode(handle, TessPageSegMode.PSM_AUTO);
		api.TessBaseAPISetImage(handle, buf, image.getWidth(), image.getHeight(), bytespp, bytespl);

		int cropheight = (int) (0.75 * image.getHeight());
		api.TessBaseAPISetRectangle(handle, 0, cropheight, image.getWidth(), image.getHeight());

		ETEXT_DESC monitor = new ETEXT_DESC();
		TessAPI1.TessBaseAPIRecognize(handle, monitor);
		TessResultIterator ri = TessAPI1.TessBaseAPIGetIterator(handle);
		TessPageIterator pi = TessAPI1.TessResultIteratorGetPageIterator(ri);
		TessAPI1.TessPageIteratorBegin(pi);
		System.out.println("Bounding boxes:\nchar(s) left top right bottom confidence font-attributes");
		int level = TessAPI1.TessPageIteratorLevel.RIL_WORD;
		@SuppressWarnings("unused")
		int height = image.getHeight();
		do
		{
			int flag = 0;
			Pointer ptr = TessAPI1.TessResultIteratorGetUTF8Text(ri, level);
			String word = ptr.getString(0);
			TessAPI1.TessDeleteText(ptr);
			// System.out.println(word);
			// word="PSDA—Piccadiiy—Vo8-O71015";
			if (CheckSpecialCharacter(word))
			{
				int temp = word.length();
				String substring = "";
				for (int k = 0; k < temp; k++)
				{
					char c = word.charAt(k);
					int ch = (int) c;
					if ((ch >= 48 && ch <= 57) || (ch >= 65 && ch <= 90) || (ch >= 97 && ch <= 122))
						substring += c;
					else
					{
						int tl = substring.length();
						double sl = m.similar(target, substring);
						if (tl >= (targetlen - 1) && tl <= (targetlen + 1) && sl > 0.5)
						{
							flag = 1;
						}
						substring = "";
					}
				}
			}
			if (flag == 1 || (word.length() >= targetlen - 1 && word.length() <= targetlen + 1 && m.similar(target, word) > 0.8))
			{
				float confidence = TessAPI1.TessResultIteratorConfidence(ri, level);
				IntBuffer leftB = IntBuffer.allocate(1);
				IntBuffer topB = IntBuffer.allocate(1);
				IntBuffer rightB = IntBuffer.allocate(1);
				IntBuffer bottomB = IntBuffer.allocate(1);
				TessAPI1.TessPageIteratorBoundingBox(pi, level, leftB, topB, rightB, bottomB);
				int left = leftB.get();
				int top = topB.get();
				int right = rightB.get();
				int bottom = bottomB.get();
				System.out.println(String.format("%s %d %d %d %d %f", word, left, top, right, bottom, confidence));
			}
		} while (TessAPI1.TessPageIteratorNext(pi, level) == TRUE);
		// Pointer utf8Text = api.TessBaseAPIGetUTF8Text(handle);

	}

	public static boolean CheckSpecialCharacter(String s)
	{

		if (s == null || s.trim().isEmpty())
		{

			return false;
		}
		Pattern p = Pattern.compile("[^A-Za-z0-9]");
		Matcher m = p.matcher(s);
		// boolean b = m.matches();
		boolean b = m.find();
		if (b == true)
			return true;
		else
			return false;
	}

	public static void main(String[] args)
	{
		crop();

	}

}
