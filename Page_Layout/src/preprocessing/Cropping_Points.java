package preprocessing;

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

	private int midpoint = 0, y1 = 0, y2 = 0, line = 0, midmaximun = Integer.MIN_VALUE;

	public int getMidpoint()
	{
		return midpoint;
	}

	public void setMidpoint(int midpoint)
	{
		this.midpoint = midpoint;
	}

	public int getY1()
	{
		return y1;
	}

	public void setY1(int y1)
	{
		this.y1 = y1;
	}

	public int getY2()
	{
		return y2;
	}

	public void setY2(int y2)
	{
		this.y2 = y2;
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

	public Cropping_Points cutting_point(BufferedImage bufferedImage) throws IOException
	{
		int imgWidth = bufferedImage.getWidth();
		int imgHeight = bufferedImage.getHeight();

		Cropping_Points p = new Cropping_Points();

		// for footer
		int horizontal[] = new int[imgHeight];
		int y2f = imgHeight;
		for (int i = 0; i < imgHeight; i++)
		{
			int count = 0;
			for (int j = (int) (0.15 * imgWidth); j < (int) (0.35 * imgWidth); j++)
			{
				Color c = new Color(bufferedImage.getRGB(j, i));
				if (c.getRed() <= 240 && c.getBlue() <= 240 && c.getGreen() <= 240)
				{
					count++;
				}
			}
			horizontal[i] = count;
		}

		for (int i = imgHeight - 2; i >= 0; i--)
		{
			if (horizontal[i] != 0 && horizontal[i - 1] != 0 && horizontal[i - 2] != 0 && horizontal[i - 3] != 0)
			{
				y2f = i;
				break;
			}
		}
		p.setY2(y2f);

		for (int i = y2f; i >= 0; i--)
		{
			if (horizontal[i] == 0 && horizontal[i - 1] == 0 && horizontal[i - 2] == 0 && horizontal[i - 3] == 0)
			{
				y2f = i - 3;
				break;
			}
		}
		p.setY1(y2f);

		// for vertical cropping
		int vertical[] = new int[imgWidth];
		for (int i = 0; i < imgWidth; i++)
		{
			int count = 0;
			for (int j = 0; j < (int) (0.40 * imgHeight); j++)
			{
				Color c = new Color(bufferedImage.getRGB(i, j));
				if (c.getRed() <= 240 && c.getBlue() <= 240 && c.getGreen() <= 240)
				{
					count++;
				}
			}
			vertical[i] = count;
		}

		for (int i = 0; i < (int) (0.20 * imgWidth); i++)
		{
			int count = 0;
			for (int j = (int) (0.40 * imgHeight); j < (int) (0.98 * imgHeight); j++)
			{
				Color c = new Color(bufferedImage.getRGB(i, j));
				if (c.getRed() <= 240 && c.getBlue() <= 240 && c.getGreen() <= 240)
				{
					count++;
				}
			}
			vertical[i] = vertical[i] + count;
		}

		for (int i = (int) (0.80 * imgWidth); i < imgWidth; i++)
		{
			int count = 0;
			for (int j = (int) (0.40 * imgHeight); j < (int) (0.98 * imgHeight); j++)
			{
				Color c = new Color(bufferedImage.getRGB(i, j));
				if (c.getRed() <= 240 && c.getBlue() <= 240 && c.getGreen() <= 240)
				{
					count++;
				}
			}
			vertical[i] = vertical[i] + count;
		}

		// for middle margin
		int lenth, temp;
		int m1 = 0, m2 = 0;
		int midmaximun = Integer.MIN_VALUE;
		for (int i = (int) (0.40 * imgWidth); i < (int) (0.60 * imgWidth); i++)
		{
			lenth = 0;
			temp = i;
			int flag = 0;
			while ((vertical[temp] == 0 || vertical[temp + 2] == 0 || vertical[temp + 5] == 0) && temp < (int) (0.60 * imgWidth))
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
		if (m1 == 0 || m2 == 0)
		{
			p.setMidpoint(imgWidth / 2);
		}
		else
		{
			p.setMidpoint((m1 + m2) / 2);
		}
		p.setMidmaximun(midmaximun + 1);

		// for counting vertical lines to detect the tables.
		int line = 0;
		for (int i = (int) (0.30 * imgWidth); i < (int) (0.70 * imgWidth); i++)
		{
			for (int j = (int) (0.1 * imgHeight); j < (int) (0.6 * imgHeight); j++)
			{
				Color c1 = new Color(bufferedImage.getRGB(i, j));
				Color c2 = new Color(bufferedImage.getRGB(i + 1, j));
				Color c3 = new Color(bufferedImage.getRGB(i + 2, j));
				Color c4 = new Color(bufferedImage.getRGB(i + 3, j));

				int count = 0;
				int flag = 0;

				while ((c1.getRed() <= 240 && c1.getBlue() <= 240 && c1.getGreen() <= 240)
						|| (c2.getRed() <= 240 && c2.getBlue() <= 240 && c2.getGreen() <= 240)
						|| (c3.getRed() <= 240 && c3.getBlue() <= 240 && c3.getGreen() <= 240)
						|| (c4.getRed() <= 240 && c4.getBlue() <= 240 && c4.getGreen() <= 240) && j < (int) (0.60 * imgHeight))
				{
					flag = 1;
					count++;
					j++;
					c1 = new Color(bufferedImage.getRGB(i, j));
					c2 = new Color(bufferedImage.getRGB(i + 1, j));
					c3 = new Color(bufferedImage.getRGB(i + 2, j));
					c4 = new Color(bufferedImage.getRGB(i + 3, j));
				}
				if (flag == 1)
				{
					j--;
				}
				if (count > (int) (0.1 * imgHeight))
				{
					line++;
					i = i + 6;
					break;
				}
			}
		}
		p.setLine(line);

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