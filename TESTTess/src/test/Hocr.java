package test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import com.sun.jna.Pointer;

import net.sourceforge.tess4j.TessAPI;
import net.sourceforge.tess4j.TessAPI1;
import net.sourceforge.tess4j.ITessAPI.TessBaseAPI;
import net.sourceforge.tess4j.ITessAPI.TessPageSegMode;
import net.sourceforge.tess4j.util.ImageIOHelper;
import net.sourceforge.tess4j.util.LoadLibs;

public class Hocr {
	public static void d() throws FileNotFoundException, IOException{
		TessAPI api=LoadLibs.getTessAPIInstance();
		TessBaseAPI handle=TessAPI1.TessBaseAPICreate();
		String datapath = "C:/Users/Saurabh/Desktop/project/Tess4J/tessdata";
		String language = "eng";
     String testResourcesDataPath ="C:/Users/Saurabh/Desktop/project/tess4j example/TESTTess/res";
	String filename = String.format("%s/%s", testResourcesDataPath, "eurotext.tif");
    File tiff =new File(filename);
    BufferedImage image = ImageIO.read(new FileInputStream(tiff)); // require jai-imageio lib to read TIFF
    ByteBuffer buf = ImageIOHelper.convertImageData(image);
    int bpp = image.getColorModel().getPixelSize();
    int bytespp = bpp / 8;
    int bytespl = (int) Math.ceil(image.getWidth() * bpp / 8.0);
    api.TessBaseAPISetPageSegMode(handle, TessPageSegMode.PSM_AUTO);
    api.TessBaseAPIInit3(handle, datapath, language);
    api.TessBaseAPISetImage(handle, buf, image.getWidth(), image.getHeight(), bytespp, bytespl);
    int page_number = 0;
    Pointer utf8Text = api.TessBaseAPIGetHOCRText(handle, page_number);
    String result = utf8Text.getString(0);
    System.out.println(result);
    api.TessDeleteText(utf8Text);
    }
    public static void main(String []Args){
    	try {
			d();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
}
