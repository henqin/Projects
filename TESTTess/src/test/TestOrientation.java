package test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import javax.imageio.ImageIO;

import net.sourceforge.tess4j.ITessAPI.TessBaseAPI;
import net.sourceforge.tess4j.ITessAPI.TessPageIterator;
import net.sourceforge.tess4j.ITessAPI.TessPageSegMode;
import net.sourceforge.tess4j.ITessAPI.TessTextlineOrder;
import net.sourceforge.tess4j.ITessAPI.TessWritingDirection;
import net.sourceforge.tess4j.TessAPI;
import net.sourceforge.tess4j.TessAPI1;
import net.sourceforge.tess4j.util.ImageIOHelper;
import net.sourceforge.tess4j.util.LoadLibs;
import net.sourceforge.tess4j.util.PdfUtilities;
import net.sourceforge.tess4j.util.Utils;

public class TestOrientation
{
	public static void D() throws Exception
	{

		TessAPI api = LoadLibs.getTessAPIInstance();

		TessBaseAPI handle = TessAPI1.TessBaseAPICreate();
		File pdf = new File("C:/Users/Saurabh/Desktop/project/tess4j example/TESTTess/res/multipage-pdf.pdf");
		File tiff = new File("C:/Users/Saurabh/Desktop/project/tess4j example/TESTTess/res/tmp.tif");
		try
		{
			tiff = PdfUtilities.convertPdf2Tiff(pdf);
		} catch (IOException e1)
		{

			e1.printStackTrace();
		}
		String datapath = "C:/Users/Saurabh/Desktop/project/tess4j example/TESTTess/tessdata";
		String language = "eng";
		// int expResult = TessPageSegMode.PSM_AUTO_OSD;
		IntBuffer orientation = IntBuffer.allocate(1);
		IntBuffer direction = IntBuffer.allocate(1);
		IntBuffer order = IntBuffer.allocate(1);
		FloatBuffer deskew_angle = FloatBuffer.allocate(1);

		BufferedImage image = ImageIO.read(new FileInputStream(tiff)); // require jai-imageio lib to read TIFF
		ByteBuffer buf = ImageIOHelper.convertImageData(image);
		int bpp = image.getColorModel().getPixelSize();
		int bytespp = bpp / 8;
		int bytespl = (int) Math.ceil(image.getWidth() * bpp / 8.0);
		api.TessBaseAPIInit3(handle, datapath, language);
		api.TessBaseAPISetPageSegMode(handle, TessPageSegMode.PSM_AUTO_OSD);
		// int actualResult = api.TessBaseAPIGetPageSegMode(handle);

		api.TessBaseAPISetImage(handle, buf, image.getWidth(), image.getHeight(), bytespp, bytespl);
		int success = api.TessBaseAPIRecognize(handle, null);
		if (success == 0)
		{
			TessPageIterator pi = api.TessBaseAPIAnalyseLayout(handle);
			api.TessPageIteratorOrientation(pi, orientation, direction, order, deskew_angle);
			System.out.println("Orientation:" + orientation.get() + "\nWritingDirection:"
					+ Utils.getConstantName(direction.get(), TessWritingDirection.class) + " \nTextlineOrder:"
					+ Utils.getConstantName(order.get(), TessTextlineOrder.class) + "\nDeskew angle: " + deskew_angle.get());

		}

	}

	public static void main(String[] args)
	{

		try
		{
			D();
		} catch (Exception e)
		{

			e.printStackTrace();
		}

	}
}
