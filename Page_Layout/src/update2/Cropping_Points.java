package update2;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PDFPage;

class Cropping_Points
{
	public Cropping_Points()
	{
		super();
	}

	private int l1 = 0, l2 = 0, m1 = 0, m2 = 0, y1 = 0, y2 = 0, r1 = 0, r2 = 0, line = 0, midmaximun = Integer.MIN_VALUE;

	public int getY2()
	{
		return y2;
	}

	public void setY2(int y2)
	{
		this.y2 = y2;
	}

	public int getY1()
	{
		return y1;
	}

	public void setY1(int y1)
	{
		this.y1 = y1;
	}

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
				// System.out.println("i = "+ i+" j = "+j);
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

	public Cropping_Points cutting_point(BufferedImage img) throws IOException
	{
		Cropping_Points p = new Cropping_Points();

		// for header
		int counts_header[] = new int[img.getHeight()];
		int y1 = 0;

		for (int i = 0; i < img.getHeight() / 4; i++)
		{
			int count = 0;
			for (int j = (int) (0.05 * img.getWidth()); j < (int) (0.25 * img.getWidth()); j++)
			{
				Color c = new Color(img.getRGB(j, i));
				if (c.getRed() <= 240 && c.getBlue() <= 240 && c.getGreen() <= 240)
				{
					count++;
				}
			}
			counts_header[i] = count;
		}
		for (int i = 0; i < img.getHeight() / 4; i++)
		{
			int count = 0;
			for (int j = (int) (0.65 * img.getWidth()); j < (int) (0.75 * img.getWidth()); j++)
			{
				Color c = new Color(img.getRGB(j, i));
				if (c.getRed() <= 240 && c.getBlue() <= 240 && c.getGreen() <= 240)
				{
					count++;
				}
			}
			counts_header[i] = counts_header[i] + count;
		}

		for (int i = 0; i < (img.getHeight() / 4) - 4; i++)
		{
			if (counts_header[i] != 0 && counts_header[i + 1] != 0 && counts_header[i + 2] != 0)
			{
				y1 = i;
				break;
			}
		}
		if (y1 - 2 >= 0)
		{
			y1 = y1 - 2;
		}
		p.setY1(y1);

		// for footer
		int counts_footer[] = new int[img.getHeight()];
		int y2f = img.getHeight();
		for (int i = 0; i < img.getHeight(); i++)
		{
			int count = 0;
			for (int j = (int) (0.75 * img.getWidth()); j < (int) (img.getWidth()); j++)
			{
				Color c = new Color(img.getRGB(j, i));
				if (c.getRed() <= 240 && c.getBlue() <= 240 && c.getGreen() <= 240)
				{
					count++;
				}
			}
			counts_footer[i] = count;
		}

		for (int i = img.getHeight() - 2; i >= 0; i--)
		{
			if (counts_footer[i] != 0 && counts_footer[i - 1] != 0 && counts_footer[i - 2] != 0)
			{
				y2f = i;
				break;
			}
		}
		p.setY2(y2f);

		// for vertical cropping

		int counts[] = new int[img.getWidth()];
		for (int i = 0; i < img.getWidth(); i++)
		{
			int count = 0;
			for (int j = y1; j < (int) (0.40 * img.getHeight()); j++)
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
			{
				temp--;
			}
			if (lenth > maximun)
			{
				maximun = lenth;
				l1 = i;
				l2 = temp;
				if (temp < 0)
				{
					l2 = 0;
				}
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
			{
				temp++;
			}
			if (lenth > maximun)
			{
				maximun = lenth;
				r2 = i;
				r1 = temp;
				if (temp > img.getWidth())
				{
					r2 = img.getWidth();
				}
			}
			i = temp;
		}

		// for middle
		int m1 = 0, m2 = 0;
		int midmaximun = Integer.MIN_VALUE;
		for (int i = (int) (0.40 * img.getWidth()); i < (int) (0.60 * img.getWidth()); i++)
		{
			lenth = 0;
			temp = i;
			int flag = 0;
			while (counts[temp] == 0 && temp < (int) (0.60 * img.getWidth()))
			{
				lenth++;
				temp++;
				flag = 1;
			}
			if (flag == 1)
			{
				temp--;
			}
			if (lenth > midmaximun)
			{
				midmaximun = lenth;
				m1 = i;
				m2 = temp;
				if (temp < m1)
				{
					m2 = m1;
				}
			}
			i = temp;
		}

		int line = 0;
		for (int i = (int) (0.30 * img.getWidth()); i < (int) (0.70 * img.getWidth()); i++)
		{
			for (int j = (int) (0.1 * img.getHeight()); j < (int) (0.6 * img.getHeight()); j++)
			{
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
				{
					j--;
				}
				if (count > (int) (0.1 * img.getHeight()))
				{
					line++;
					i = i + 6;
					break;
				}
			}
		}
		// p.setY2(p.bottemY(img));
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

	public void uncompressXRef(String src, String dest) throws IOException, DocumentException
	{
		PdfReader reader = new PdfReader(src);
		PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(dest));
		stamper.close();
		reader.close();
	}

	public List<BufferedImage> pdf_To_BufferedImage(File file)
	{
		List<BufferedImage> Temp_Files = new ArrayList<BufferedImage>();
		PDDocument document = null;
		try
		{
			document = PDDocument.load(file);
			@SuppressWarnings("unchecked")
			List<PDPage> list = document.getDocumentCatalog().getAllPages();
			int pageNumber = 1;
			for (PDPage page : list)
			{
				BufferedImage i = page.convertToImage();
				Temp_Files.add(i);
				System.out.println("page " + pageNumber + " is converted in image.");
				pageNumber++;
			}
		} catch (IOException e)
		{
			e.printStackTrace();
		} finally
		{
			try
			{
				document.close();
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		return Temp_Files;
	}

	public List<BufferedImage> pdf_To_BufferedImage300(File file)
	{
		List<BufferedImage> Temp_Files = new ArrayList<BufferedImage>();
		PDDocument document = null;
		try
		{
			document = PDDocument.load(file);
			@SuppressWarnings("unchecked")
			List<PDPage> list = document.getDocumentCatalog().getAllPages();
			int pageNumber = 1;
			for (PDPage page : list)
			{
				BufferedImage i = page.convertToImage(BufferedImage.TYPE_BYTE_BINARY, 300);
				Temp_Files.add(i);
				System.out.println("page " + pageNumber + " is converted in image.");
				ImageIO.write(i, "png", new File(
						"C:/Users/ATUL/Desktop/Page-layout/image/" + pageNumber + "_" + file.getName().replace("pdf", "png")));
				pageNumber++;
			}
		} catch (IOException e)
		{
			e.printStackTrace();
		} finally
		{
			try
			{
				document.close();
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		return Temp_Files;
	}

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

			int w = 2 * (int) page.getBBox().getWidth();
			int h = 2 * (int) page.getBBox().getHeight();

			BufferedImage bufferedImage = new BufferedImage(w, h, BufferedImage.TYPE_BYTE_BINARY);

			Image img = page.getImage(w, h, null, null, true, true);
			// width , height , clip-rect , null for the ImageObserver , fill
			// background with white , block until drawing is done

			Graphics2D g = bufferedImage.createGraphics();
			g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

			g.drawImage(img, 0, 0, null);
			g.dispose();
			// System.out.println("page " + (pageNumber) + " is converted in image---pagesize=" + page.getCropBox().getWidth()
			// + " X " + page.getCropBox().getHeight() + " image size=" + img.getWidth(null) + " X " + img.getHeight(null));

			ImageIO.write(bufferedImage, "tif", new File("C:/Users/ATUL/Desktop/Page-layout/image/output" + i + ".tif"));
			System.out.println("page " + (i) + " is converted in image.");

			Temp_Files.add(bufferedImage);
		}
		raf.close();
		channel.close();
		buf.clear();

		return Temp_Files;

	}

	double angle(Point pt1, Point pt2, Point pt0)
	{
		double dx1 = pt1.x - pt0.x;
		double dy1 = pt1.y - pt0.y;
		double dx2 = pt2.x - pt0.x;
		double dy2 = pt2.y - pt0.y;
		return (dx1 * dx2 + dy1 * dy2) / Math.sqrt((dx1 * dx1 + dy1 * dy1) * (dx2 * dx2 + dy2 * dy2) + 1e-10);
	}

	int bottemY(BufferedImage img) throws IOException
	{
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		int y = Integer.MAX_VALUE;

		List<MatOfPoint> squares = new LinkedList<MatOfPoint>();
		Mat pyr = new Mat();
		Mat timg = new Mat();
		Mat gray = new Mat();

		ImageIO.write(img, "jpg", new File("C:/Users/ATUL/Desktop/Page-layout/testing/temp/temp.jpg"));
		Mat image = Highgui.imread("C:/Users/ATUL/Desktop/Page-layout/testing/temp/temp.jpg", Highgui.CV_LOAD_IMAGE_GRAYSCALE);

		// down-scale and upscale the image to filter out the noise
		Imgproc.pyrDown(image, pyr, new Size(image.cols() / 2, image.rows() / 2));
		Imgproc.pyrUp(pyr, timg, image.size());
		List<MatOfPoint> contours = new ArrayList<MatOfPoint>();

		Imgproc.threshold(image, gray, 230, 255, Imgproc.THRESH_BINARY);
		Imgproc.threshold(gray, gray, 0, 1, Imgproc.THRESH_BINARY_INV);
		Imgproc.findContours(gray, contours, new Mat(), Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);

		// test each contour
		MatOfPoint2f approxCurve = new MatOfPoint2f();
		for (int i = 0; i < contours.size(); i++)
		{
			MatOfPoint2f contour2f = new MatOfPoint2f(contours.get(i).toArray());
			double approxDistance = Imgproc.arcLength(contour2f, true) * 0.02;
			Imgproc.approxPolyDP(contour2f, approxCurve, approxDistance, false);
			if (approxCurve.toList().size() == 4 && Math.abs(Imgproc.contourArea(approxCurve)) > 1000
					&& Imgproc.isContourConvex(new MatOfPoint(approxCurve.toArray())))
			{
				double maxCosine = 0;
				for (int j = 2; j < 5; j++)
				{
					// find the maximum cosine of the angle between joint edges
					double cosine = Math.abs(angle(approxCurve.toList().get(j % 4), approxCurve.toList().get(j - 2),
							approxCurve.toList().get(j - 1)));
					maxCosine = Math.max(maxCosine, cosine);
				}
				// if cosines of all angles are small
				// (all angles are ~90 degree) then write quandrange
				// vertices to resultant sequence
				if (maxCosine < 0.3)
				{
					squares.add(new MatOfPoint(approxCurve.toArray()));
				}
			}
		}
		for (int i = 0; i < squares.size(); i++)
		{
			int tempY = (int) squares.get(i).toArray()[0].y;
			// System.out.println(tempY);
			if (tempY < y)
			{
				y = tempY;
			}
		}
		// System.out.println("bottem y = " + y);
		if (y == Integer.MAX_VALUE)
			y = img.getHeight();
		return y;
	}
}