package function;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class BufferedImageRotation
{

	public BufferedImageRotation()
	{
		super();
	}

	public BufferedImage rotate(BufferedImage img, double angle)
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

	public static void main(String args[]) throws IOException
	{

		BufferedImage img = null;
		img = ImageIO.read(new File("C:/Users/ATUL/Desktop/signature/sample1.jpg"));
		BufferedImageRotation b = new BufferedImageRotation();

		BufferedImage dest = b.rotate(img, 90);

		ImageIO.write(dest, "jpg", new File("C:/Users/ATUL/Desktop/signature/sample1_rotate.jpg"));
		System.out.println("done");
	}

}
