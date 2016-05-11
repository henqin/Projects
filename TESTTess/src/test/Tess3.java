package test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import javax.imageio.ImageIO;
import net.sourceforge.tess4j.*;
import net.sourceforge.tess4j.ITessAPI.TessBaseAPI;
import net.sourceforge.tess4j.ITessAPI.TessPageSegMode;
import net.sourceforge.tess4j.util.ImageIOHelper;
import net.sourceforge.tess4j.util.PdfUtilities;

public class Tess3 {
	public static void main(String[] args) throws IOException {
		String datapath = "C:/Users/Saurabh/Desktop/project/tess4j example/TESTTess/tessdata";
		String language = "eng";
		TessBaseAPI handle = TessAPI1.TessBaseAPICreate();

		File pdf = new File("C:/Users/Saurabh/Desktop/project/tess4j example/TESTTess/res/Eddy.pdf");
		File tiff = new File("C:/Users/Saurabh/Desktop/project/tess4j example/TESTTess/res/tmp.tif");
		try {
			tiff = PdfUtilities.convertPdf2Tiff(pdf);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		BufferedImage image = null;
//		BufferedImage newimg=null;
		try {
			image = ImageIO.read(new FileInputStream(tiff));
//			newimg=ImageIO.read(tiff);
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}
        
		ByteBuffer buf = ImageIOHelper.convertImageData(image);
		int bpp = image.getColorModel().getPixelSize();
		int bytespp = bpp / 8;
		int bytespl = (int) Math.ceil(image.getWidth() * bpp / 8.0);

		TessAPI1.TessBaseAPIInit3(handle, datapath, language);
		TessAPI1.TessBaseAPISetPageSegMode(handle, TessPageSegMode.PSM_AUTO_OSD);
		TessAPI1.TessBaseAPISetImage(handle, buf, image.getWidth(), image.getHeight(), bytespp, bytespl);

//		ETEXT_DESC monitor = new ETEXT_DESC();
//		TessPageIterator pt= TessAPI1.TessBaseAPIAnalyseLayout(handle);
		System.out.println("sau");
		IntBuffer orientation = IntBuffer.allocate(1);
	    IntBuffer direction = IntBuffer.allocate(1);
	    IntBuffer order = IntBuffer.allocate(1);
	    FloatBuffer deskew_angle = FloatBuffer.allocate(1);
	    int success = TessAPI1.TessBaseAPIRecognize(handle, null);
	    if (success == 0) {
	        TessAPI.TessPageIterator pi = TessAPI1.TessBaseAPIAnalyseLayout(handle);
	        TessAPI1.TessPageIteratorOrientation(pi, orientation, direction, order, deskew_angle);
	    }
	    System.out.println("sau2");
//	    
//		TessAPI1.TessBaseAPIRecognize(handle, monitor);
//		
//		TessResultIterator ri = TessAPI1.TessBaseAPIGetIterator(handle);
//		TessPageIterator pi = TessAPI1.TessResultIteratorGetPageIterator(ri);
//		
//		
//		TessAPI1.TessPageIteratorBegin(pi);
//		System.out.println("Bounding boxes:\nchar(s) left top right bottom confidence font-attributes");
//		int level = TessAPI1.TessPageIteratorLevel.RIL_WORD;
//		int height = image.getHeight();
//		do {
//			Pointer ptr = TessAPI1.TessResultIteratorGetUTF8Text(ri, level);
//			
//			
//			String word = ptr.getString(0);
//			
////	        StringGenericVector pars = new StringGenericVector();
////	        api = new TessBaseAPI();
////	        pars.addPut(new STRING("load_system_dawg"));
////	        pars.addPut(new STRING("load_freq_dawg"));
////	        pars.addPut(new STRING("load_punc_dawg"));
////	        pars.addPut(new STRING("load_number_dawg"));
////	        pars.addPut(new STRING("load_unambig_dawg"));
////	        pars.addPut(new STRING("load_bigram_dawg"));
////	        pars.addPut(new STRING("load_fixed_length_dawgs"));
////	        StringGenericVector parsValues = new StringGenericVector();
////	        parsValues.addPut(new STRING("0"));
////	        parsValues.addPut(new STRING("0"));
////	        parsValues.addPut(new STRING("0"));
////	        parsValues.addPut(new STRING("0"));
////	        parsValues.addPut(new STRING("0"));
////	        parsValues.addPut(new STRING("0"));
////	        parsValues.addPut(new STRING("0"));
////	        
////	        if (api.Init(".", "pol", 
////	                0, (ByteBuffer)null, 0, pars, parsValues, false) != 0) {
////	            throw new RuntimeException("Could not initialize tesseract.");
////	        }
//			
//			TessAPI1.TessDeleteText(ptr);
//			float confidence = TessAPI1.TessResultIteratorConfidence(ri, level);
//			IntBuffer leftB = IntBuffer.allocate(1);
//			IntBuffer topB = IntBuffer.allocate(1);
//			IntBuffer rightB = IntBuffer.allocate(1);
//			IntBuffer bottomB = IntBuffer.allocate(1);
//			TessAPI1.TessPageIteratorBoundingBox(pi, level, leftB, topB, rightB, bottomB);
//			int left = leftB.get();
//			int top = topB.get();
//			int right = rightB.get();
//			int bottom = bottomB.get();
//			System.out.print(String.format("%s %d %d %d %d %f", word, left, top, right, bottom, confidence));
//			
//			Graphics2D g2d = newimg.createGraphics();
//	      
//	        g2d.setColor(Color.BLACK);
//	        BasicStroke bs = new BasicStroke(2);
//	        g2d.setStroke(bs);
//	        // draw the black vertical and horizontal lines
//	        
//	            // unless divided by some factor, these lines were being
//	            // drawn outside the bound of the image..
//	            g2d.drawLine(left, top, right,bottom);
//	            g2d.dispose();
//	            ImageIO.write(newimg, "JPG", new File("C:/Users/Saurabh/Desktop/project/tess4j example/TESTTess/res/tmp2.jpg"));
//
//			IntBuffer boldB = IntBuffer.allocate(1);
//			IntBuffer italicB = IntBuffer.allocate(1);
//			IntBuffer underlinedB = IntBuffer.allocate(1);
//			IntBuffer monospaceB = IntBuffer.allocate(1);
//			IntBuffer serifB = IntBuffer.allocate(1);
//			IntBuffer smallcapsB = IntBuffer.allocate(1);
//			IntBuffer pointSizeB = IntBuffer.allocate(1);
//			IntBuffer fontIdB = IntBuffer.allocate(1);
//			String fontName = TessAPI1.TessResultIteratorWordFontAttributes(ri, boldB, italicB, underlinedB, monospaceB,
//					serifB, smallcapsB, pointSizeB, fontIdB);
//			boolean bold = boldB.get() == TRUE;
//			boolean italic = italicB.get() == TRUE;
//			boolean underlined = underlinedB.get() == TRUE;
//			boolean monospace = monospaceB.get() == TRUE;
//			boolean serif = serifB.get() == TRUE;
//			boolean smallcaps = smallcapsB.get() == TRUE;
//			int pointSize = pointSizeB.get();
//			int fontId = fontIdB.get();
//			System.out.println(String.format(
//					" font: %s, size: %d, font id: %d, bold: %b,"
//							+ " italic: %b, underlined: %b, monospace: %b, serif: %b, smallcap: %b",
//					fontName, pointSize, fontId, bold, italic, underlined, monospace, serif, smallcaps));
//		} while (TessAPI1.TessPageIteratorNext(pi, level) == TRUE);
	}
}