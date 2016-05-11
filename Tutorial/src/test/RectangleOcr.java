package test;

import net.sourceforge.tess4j.TessAPI;
import net.sourceforge.tess4j.TessAPI1;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import com.sun.jna.Pointer;

import net.sourceforge.tess4j.ITessAPI.TessBaseAPI;
import net.sourceforge.tess4j.ITessAPI.TessPageSegMode;
import net.sourceforge.tess4j.util.ImageIOHelper;
import net.sourceforge.tess4j.util.LoadLibs;

public class RectangleOcr {
	public static void testTessBaseAPIGetUTF8Text() throws Exception {
        
		TessAPI api=LoadLibs.getTessAPIInstance();
		TessBaseAPI handle=TessAPI1.TessBaseAPICreate();
		String datapath = "C:/Users/Saurabh/Desktop/project/Tess4J/tessdata";
		String language = "eng";
     String testResourcesDataPath ="C:/Users/Saurabh/Desktop/project/tess4j example/TESTTess/res";
        File tiff = new File(testResourcesDataPath, "eurotext.tif");
        BufferedImage image = ImageIO.read(new FileInputStream(tiff)); // require jai-imageio lib to read TIFF
        ByteBuffer buf = ImageIOHelper.convertImageData(image);
        int bpp = image.getColorModel().getPixelSize();
        int bytespp = bpp / 8;
        int bytespl = (int) Math.ceil(image.getWidth() * bpp / 8.0);
        api.TessBaseAPIInit3(handle, datapath, language);
        api.TessBaseAPISetPageSegMode(handle, TessPageSegMode.PSM_AUTO);
        api.TessBaseAPISetImage(handle, buf, image.getWidth(), image.getHeight(), bytespp, bytespl);
        int cropheight=(int) (0.75*image.getHeight());
        api.TessBaseAPISetRectangle(handle,  0,cropheight, image.getWidth(),image.getHeight());
        Pointer utf8Text = api.TessBaseAPIGetUTF8Text(handle);
        String result = utf8Text.getString(0);
        api.TessDeleteText(utf8Text);
        
        System.out.println(result);
    }
	public static void main(String[] args) {
		try {
			testTessBaseAPIGetUTF8Text();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
