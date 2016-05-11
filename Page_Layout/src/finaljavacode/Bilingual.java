package finaljavacode;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import javax.imageio.ImageIO;

import net.sourceforge.tess4j.TessAPI;
import net.sourceforge.tess4j.TessAPI1;
import net.sourceforge.tess4j.ITessAPI.TessBaseAPI;
import net.sourceforge.tess4j.ITessAPI.TessPageIterator;
import net.sourceforge.tess4j.ITessAPI.TessPageSegMode;
import net.sourceforge.tess4j.util.ImageIOHelper;
import net.sourceforge.tess4j.util.LoadLibs;

class Bilingual
{
	public Bilingual()
	{
		super();
	}

	int bilingual;
	float skew_angle = 0;

	public void check(BufferedImage temp) throws Exception
	{
		TessAPI api = LoadLibs.getTessAPIInstance();
		TessBaseAPI handle = TessAPI1.TessBaseAPICreate();
		String datapath = "C:/Tesseract-OCR/tessdata/";
		String language = "eng";

		ByteArrayOutputStream os = new ByteArrayOutputStream();     // jugad
		ImageIO.write(temp, "jpg", os);                             //
		InputStream is = new ByteArrayInputStream(os.toByteArray());//

		BufferedImage image = ImageIO.read(is);

		int testorientation = 0;
		int testdirection = 0;
		int testorder = 2;
		int engflag = 0;
		int dualflag = 0;
		@SuppressWarnings("unused")
		int arabicflag = 0;
		ByteBuffer buf = ImageIOHelper.convertImageData(image);
		int bpp = image.getColorModel().getPixelSize();
		int bytespp = bpp / 8;
		int bytespl = (int) Math.ceil(image.getWidth() * bpp / 8.0);
		api.TessBaseAPIInit3(handle, datapath, language);
		api.TessBaseAPISetPageSegMode(handle, TessPageSegMode.PSM_AUTO_OSD);
		api.TessBaseAPISetImage(handle, buf, image.getWidth(), image.getHeight(), bytespp, bytespl);
		api.TessBaseAPISetRectangle(handle, (int) (0.01 * image.getWidth()), (int) (0.1 * image.getHeight()),
				(int) (0.3 * image.getWidth()), (int) (0.3 * image.getHeight()));
		IntBuffer orientation = IntBuffer.allocate(1);
		IntBuffer direction = IntBuffer.allocate(1);
		IntBuffer order = IntBuffer.allocate(1);
		FloatBuffer deskew_angle = FloatBuffer.allocate(1);
		TessPageIterator pi = api.TessBaseAPIAnalyseLayout(handle);

		if (pi != null)
		{
			api.TessPageIteratorOrientation(pi, orientation, direction, order, deskew_angle);
			if (orientation.get() == testorientation && direction.get() == testdirection && order.get() == testorder)
			{
				engflag = 1;
			}
		}
		api.TessBaseAPISetRectangle(handle, (int) (0.53 * image.getWidth()), (int) (0.05 * image.getHeight()),
				(int) (0.45 * image.getWidth()), (int) (0.45 * image.getHeight()));
		IntBuffer orientation1 = IntBuffer.allocate(1);
		IntBuffer direction1 = IntBuffer.allocate(1);
		IntBuffer order1 = IntBuffer.allocate(1);
		FloatBuffer deskew_angle1 = FloatBuffer.allocate(1);
		api.TessBaseAPIInitForAnalysePage(handle);
		pi = api.TessBaseAPIAnalyseLayout(handle);

		if (pi != null)
		{
			api.TessPageIteratorOrientation(pi, orientation1, direction1, order1, deskew_angle1);

			if (orientation1.get() != testorientation || direction1.get() != testdirection || order1.get() != testorder)
			{
				if (engflag == 1)
					dualflag = 1;
				else
					arabicflag = 1;
			}

		}

		api.TessBaseAPISetRectangle(handle, (int) (0.50 * image.getWidth()), (int) (0.5 * image.getHeight()),
				(int) (0.45 * image.getWidth()), (int) (0.3 * image.getHeight()));
		IntBuffer orientation2 = IntBuffer.allocate(1);
		IntBuffer direction2 = IntBuffer.allocate(1);
		IntBuffer order2 = IntBuffer.allocate(1);
		FloatBuffer deskew_angle2 = FloatBuffer.allocate(1);
		api.TessBaseAPIInitForAnalysePage(handle);
		pi = api.TessBaseAPIAnalyseLayout(handle);
		// api.TessPageIteratorParagraphInfo(pi, orientation, direction, order, direction);
		if (pi != null)
		{
			api.TessPageIteratorOrientation(pi, orientation2, direction2, order2, deskew_angle2);

			if (orientation2.get() != testorientation || direction2.get() != testdirection || order2.get() != testorder)
			{
				if (engflag == 1)
					dualflag = 1;
				else
					arabicflag = 1;
			}
		}
		if (dualflag == 1)
		{
			this.bilingual = 1;
			// System.out.println("bilingual");
		}
		else
		{
			this.bilingual = 0;
			// System.out.println("not bilingual");
		}

	}

	public void DeskewAngle(BufferedImage temp) throws Exception
	{
		TessAPI api = LoadLibs.getTessAPIInstance();
		
		TessBaseAPI handle = TessAPI1.TessBaseAPICreate();
		String datapath = "C:/Tesseract-OCR/tessdata/";
		String language = "eng";

		ByteArrayOutputStream os = new ByteArrayOutputStream();     // jugad
		ImageIO.write(temp, "jpg", os);                             //
		InputStream is = new ByteArrayInputStream(os.toByteArray());//

		BufferedImage image = ImageIO.read(is);

		ByteBuffer buf = ImageIOHelper.convertImageData(image);
		int bpp = image.getColorModel().getPixelSize();
		int bytespp = bpp / 8;
		int bytespl = (int) Math.ceil(image.getWidth() * bpp / 8.0);

		api.TessBaseAPIInit3(handle, datapath, language);
		api.TessBaseAPISetPageSegMode(handle, TessPageSegMode.PSM_AUTO_OSD);
		api.TessBaseAPISetImage(handle, buf, image.getWidth(), (int) (0.4 * image.getHeight()), bytespp, bytespl);
		// api.TessBaseAPISetRectangle(handle, 0, 0,image.getWidth(), (int)(0.4*image.getHeight()));

		IntBuffer orientation = IntBuffer.allocate(1);
		IntBuffer direction = IntBuffer.allocate(1);
		IntBuffer order = IntBuffer.allocate(1);
		FloatBuffer deskew_angle = FloatBuffer.allocate(1);

		TessPageIterator pi = api.TessBaseAPIAnalyseLayout(handle);

		if (pi != null)
		{
			api.TessPageIteratorOrientation(pi, orientation, direction, order, deskew_angle);
			this.skew_angle = deskew_angle.get();
		}
		else
			this.skew_angle = 0;
		
		
		pi=null;
		orientation.clear();
		direction.clear();
		order.clear();
		deskew_angle.clear();
		buf.clear();
		
		is.close();
		os.close();
		
		handle=null;
		api=null;
	}

	
	public static void main(String[] args)
	{

		try
		{
			for (int i = 1; i <= 5; i++)
			{
				File tiff = new File("C:/Users/ATUL/Desktop/Page-layout/image/output" + i + ".jpg");
				Bilingual b = new Bilingual();
				BufferedImage temp = ImageIO.read(tiff);

				long start = System.currentTimeMillis();
				b.check(temp);
				if (b.bilingual == 1)
					System.out.println("page no " + i + " is bilangual.");
				else
					System.out.println("page no " + i + " is not bilangual.");
				// b.DeskewAngle(temp);
				// temp = rotate(temp, -Math.toDegrees(b.skew_angle));
				long end = System.currentTimeMillis();

				// ImageIO.write(temp, "jpg", new File("C:/Users/ATUL/Desktop/Page-layout/image/skew_w_rect" + i + ".jpg"));

				System.out.println("time=" + (end - start));
				// System.out.println(i + " angle=" + Math.toDegrees(b.skew_angle));
			}

		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public BufferedImage rotate(BufferedImage img, double angle) throws IOException
	{
		double sin = Math.abs(Math.sin(Math.toRadians(angle))), cos = Math.abs(Math.cos(Math.toRadians(angle)));

		int w = img.getWidth(null), h = img.getHeight(null);

		int neww = (int) Math.floor(w * cos + h * sin), newh = (int) Math.floor(h * cos + w * sin);

		BufferedImage bimg = new BufferedImage(neww, newh, BufferedImage.TYPE_3BYTE_BGR);
		Graphics2D g = bimg.createGraphics();

		g.translate((neww - w) / 2, (newh - h) / 2);
		g.rotate(Math.toRadians(angle), w / 2, h / 2);
		g.drawRenderedImage(img, null);
		g.dispose();
		return (bimg);
	}

}