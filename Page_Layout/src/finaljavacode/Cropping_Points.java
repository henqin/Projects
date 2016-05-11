package finaljavacode;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;

import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PDFPage;

class Cropping_Points
{
	public Cropping_Points()
	{
		super();
	}

	private int l1 = 0, l2 = 0, m1 = 0, m2 = 0, r1 = 0, r2 = 0,line=0, midmaximun = Integer.MIN_VALUE;

	public int getLine()
	{
		return line;
	}

	public void setLine(int line)
	{
		this.line = line;
	}

	public int getMidmaximun()
	{
		return midmaximun;
	}

	public void setMidmaximun(int midmaximun)
	{
		this.midmaximun = midmaximun;
	}

	public int getL1()
	{
		return l1;
	}

	public void setL1(int l1)
	{
		this.l1 = l1;
	}

	public int getL2()
	{
		return l2;
	}

	public void setL2(int l2)
	{
		this.l2 = l2;
	}

	public int getM1()
	{
		return m1;
	}

	public void setM1(int m1)
	{
		this.m1 = m1;
	}

	public int getM2()
	{
		return m2;
	}

	public void setM2(int m2)
	{
		this.m2 = m2;
	}

	public int getR1()
	{
		return r1;
	}

	public void setR1(int r1)
	{
		this.r1 = r1;
	}

	public int getR2()
	{
		return r2;
	}

	public void setR2(int r2)
	{
		this.r2 = r2;
	}

	public int line(BufferedImage img)
	{
		int line = 0;

		for (int i = (int) (0.0 * img.getWidth()); i < (int) (0.9 * img.getWidth()); i++)
		{

			for (int j = (int) (0.0 * img.getHeight()); j < (int) (0.6 * img.getHeight()); j++)
			{
				// System.out.println("i = "+ i+"  j = "+j);
				Color c1 = new Color(img.getRGB(i, j));
				Color c2 = new Color(img.getRGB(i + 1, j));
				Color c3 = new Color(img.getRGB(i + 2, j));
				Color c4 = new Color(img.getRGB(i + 3, j));

				int count = 0;
				int flag = 0;

				while ((c1.getRed() <= 240 && c1.getBlue() <= 240 && c1.getGreen() <= 240)
						|| (c2.getRed() <= 240 && c2.getBlue() <= 240 && c2.getGreen() <= 240)
						|| (c3.getRed() <= 240 && c3.getBlue() <= 240 && c3.getGreen() <= 240)
						|| (c4.getRed() <= 240 && c4.getBlue() <= 240 && c4.getGreen() <= 240)
						&& j < (int) (0.60 * img.getHeight()))
				{
					flag = 1;
					count++;
					j++;
					c1 = new Color(img.getRGB(i, j));
					c2 = new Color(img.getRGB(i + 1, j));
					c3 = new Color(img.getRGB(i + 2, j));
					c4 = new Color(img.getRGB(i + 3, j));
				}

				if (flag == 1)
					j--;

				if (count > (int) (0.1 * img.getHeight()))
				{
					line++;
					i = i + 6;
					break;
				}
			}
		}

		return line;
	}

	public Cropping_Points cutting_point(BufferedImage img)
	{
		Cropping_Points p = new Cropping_Points();

		int counts[] = new int[img.getWidth()];

		for (int i = 0; i < img.getWidth(); i++)
		{
			int count = 0;

			for (int j = (int) (0.07 * img.getHeight()); j < (int) (0.40 * img.getHeight()); j++)
			{
				Color c = new Color(img.getRGB(i, j));
				if (c.getRed() <= 240 && c.getBlue() <= 240 && c.getGreen() <= 240)
				{
					count++;
				}
			}
			counts[i] = count;
		}
		for (int i = 0; i < (int) (0.20 * img.getWidth()); i++)
		{
			int count = 0;

			for (int j = (int) (0.40 * img.getHeight()); j < (int) (0.98 * img.getHeight()); j++)
			{
				Color c = new Color(img.getRGB(i, j));
				if (c.getRed() <= 240 && c.getBlue() <= 240 && c.getGreen() <= 240)
				{
					count++;
				}
			}
			counts[i] = counts[i] + count;
		}

//		for (int i = (int) (0.45 * img.getWidth()); i < (int) (0.55 * img.getWidth()); i++)
//		{
//			int count = 0;
//
//			for (int j = (int) (0.60 * img.getHeight()); j < (int) (0.80 * img.getHeight()); j++)
//			{
//				Color c = new Color(img.getRGB(i, j));
//				if (c.getRed() <= 240 && c.getBlue() <= 240 && c.getGreen() <= 240)
//				{
//					count++;
//				}
//			}
//			counts[i] = counts[i] + count;
//		}

		for (int i = (int) (0.80 * img.getWidth()); i < img.getWidth(); i++)
		{
			int count = 0;

			for (int j = (int) (0.40 * img.getHeight()); j < (int) (0.98 * img.getHeight()); j++)
			{
				Color c = new Color(img.getRGB(i, j));
				if (c.getRed() <= 240 && c.getBlue() <= 240 && c.getGreen() <= 240)
				{
					count++;
				}
			}
			counts[i] = counts[i] + count;
		}

		// for left margin------
		int lenth, temp;
		int l1 = 0, l2 = 0;
		int maximun = Integer.MIN_VALUE;
		for (int i = 0; i < (int) (0.20 * img.getWidth()); i++)
		{
			lenth = 0;
			temp = i;

			int flag = 0;

			while (counts[temp] == 0 && temp < (int) (0.15 * img.getWidth()))
			{
				lenth++;
				temp++;
				flag = 1;
			}

			if (flag == 1)
				temp--;

			if (lenth > maximun)
			{
				maximun = lenth;
				l1 = i;
				l2 = temp;
				if (temp < 0)
					l2 = 0;
			}
			i = temp;
		}

		// for right margin------
		int r1 = 0, r2 = 0;
		maximun = Integer.MIN_VALUE;
		for (int i = img.getWidth() - 1; i > (int) (0.80 * img.getWidth()); i--)
		{
			lenth = 0;
			temp = i;

			int flag = 0;

			while (counts[temp] == 0 && temp > (int) (0.85 * img.getWidth()))
			{
				lenth++;
				temp--;
				flag = 1;
			}

			if (flag == 1)
				temp++;

			if (lenth > maximun)
			{
				maximun = lenth;
				r2 = i;
				r1 = temp;
				if (temp > img.getWidth())
					r2 = img.getWidth();
			}
			i = temp;
		}

		// for middle
		int m1 = 0, m2 = 0;
		int midmaximun = Integer.MIN_VALUE;
		for (int i = (int) (0.45 * img.getWidth()); i < (int) (0.55 * img.getWidth()); i++)
		{
			lenth = 0;
			temp = i;

			int flag = 0;

			while (counts[temp] == 0 && temp < (int) (0.55 * img.getWidth()))
			{
				lenth++;
				temp++;
				flag = 1;
			}

			if (flag == 1)
				temp--;

			if (lenth > midmaximun)
			{
				midmaximun = lenth;
				m1 = i;
				m2 = temp;
				if (temp < m1)
					m2 = m1;
			}
			i = temp;
		}
		
		int line = 0;
//
//		for (int i = (int) (0.0 * img.getWidth()); i < (int) (0.9 * img.getWidth()); i++)
//		{
//
//			for (int j = (int) (0.0 * img.getHeight()); j < (int) (0.6 * img.getHeight()); j++)
//			{
//				// System.out.println("i = "+ i+"  j = "+j);
//				Color c1 = new Color(img.getRGB(i, j));
//				Color c2 = new Color(img.getRGB(i + 1, j));
//				Color c3 = new Color(img.getRGB(i + 2, j));
//				Color c4 = new Color(img.getRGB(i + 3, j));
//
//				int count = 0;
//				int flag = 0;
//
//				while ((c1.getRed() <= 240 && c1.getBlue() <= 240 && c1.getGreen() <= 240)
//						|| (c2.getRed() <= 240 && c2.getBlue() <= 240 && c2.getGreen() <= 240)
//						|| (c3.getRed() <= 240 && c3.getBlue() <= 240 && c3.getGreen() <= 240)
//						|| (c4.getRed() <= 240 && c4.getBlue() <= 240 && c4.getGreen() <= 240)
//						&& j < (int) (0.60 * img.getHeight()))
//				{
//					flag = 1;
//					count++;
//					j++;
//					c1 = new Color(img.getRGB(i, j));
//					c2 = new Color(img.getRGB(i + 1, j));
//					c3 = new Color(img.getRGB(i + 2, j));
//					c4 = new Color(img.getRGB(i + 3, j));
//				}
//
//				if (flag == 1)
//					j--;
//
//				if (count > (int) (0.1 * img.getHeight()))
//				{
//					line++;
//					i = i + 6;
//					break;
//				}
//			}
//		}
		p.setLine(line);



		p.setL1(l1);
		p.setL2(l2);
		p.setM1(m1);
		p.setM2(m2);
		p.setR1(r1);
		p.setR2(r2);
		p.setMidmaximun(midmaximun + 1);

		counts = null;
		return p;
	}

	public BufferedImage rotate(BufferedImage img, double angle) throws IOException
	{
		double sin = Math.abs(Math.sin(Math.toRadians(angle))), cos = Math.abs(Math.cos(Math.toRadians(angle)));
		int w = img.getWidth(null), h = img.getHeight(null);
		int neww = (int) Math.floor(w * cos + h * sin), newh = (int) Math.floor(h * cos + w * sin);

		BufferedImage bimg = new BufferedImage(neww, newh, BufferedImage.TYPE_BYTE_BINARY);
		Graphics2D g = bimg.createGraphics();

		g.translate((neww - w) / 2, (newh - h) / 2);
		g.rotate(Math.toRadians(angle), w / 2, h / 2);
		g.drawRenderedImage(img, null);
		g.dispose();

		return (bimg);
	}

	// ----------------------------------------------------------------------------
	// not used..
	public List<BufferedImage> pdf_To_BufferedImage(File file) throws IOException
	{
		List<BufferedImage> Temp_Files = new ArrayList<BufferedImage>();

		PDDocument document = PDDocument.load(file);
		@SuppressWarnings("unchecked")
		List<PDPage> list = document.getDocumentCatalog().getAllPages();

		int pageNumber = 1;
		for (PDPage page : list)
		{
			BufferedImage i = page.convertToImage();
			ImageIO.write(i, "tif", new File("C:/Users/ATUL/Desktop/Page-layout/image/output" + pageNumber + ".tif"));

			Temp_Files.add(i);
			System.out.println("page " + pageNumber + " is converted in image.");
			pageNumber++;
			i = null;
		}
		document.close();

		return Temp_Files;
	}

	// ----------------------------------------------------------------------------
	// this one only for calculation on pdf---------
	public List<BufferedImage> pdf_To_BufferedImage1(File file) throws Exception
	{
		List<BufferedImage> Temp_Files = new ArrayList<BufferedImage>();

		// load a pdf from a file
		RandomAccessFile raf = new RandomAccessFile(file, "r");
		FileChannel channel = raf.getChannel();
		ByteBuffer buf = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
		PDFFile pdffile = new PDFFile(buf);

		// get number of pages
		int No_of_pages = pdffile.getNumPages();

		// iterate through the number of pages
		for (int i = 1; i <= No_of_pages; i++)
		{
			PDFPage page = pdffile.getPage(i);

			int w = (int) page.getBBox().getWidth();
			int h = (int) page.getBBox().getHeight();

			BufferedImage bufferedImage = new BufferedImage(w, h, BufferedImage.TYPE_BYTE_BINARY);

			Image img = page.getImage(w, h, null, null, true, true);
			// width , height , clip-rect , null for the ImageObserver , fill
			// background with white , block until drawing is done

			Graphics2D g = bufferedImage.createGraphics();
			g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

			g.drawImage(img, 0, 0, null);
			g.dispose();
			// System.out.println("page " + (pageNumber) + " is converted in image---pagesize=" + page.getCropBox().getWidth()
			// + " X " + page.getCropBox().getHeight() + "  image size=" + img.getWidth(null) + " X " + img.getHeight(null));

			ImageIO.write(bufferedImage, "tif", new File("C:/Users/ATUL/Desktop/Page-layout/image/output" + i + ".tif"));
			System.out.println("page " + (i) + " is converted in image.");

			Temp_Files.add(bufferedImage);
		}
		raf.close();
		channel.close();
		buf.clear();

		return Temp_Files;

	}

}