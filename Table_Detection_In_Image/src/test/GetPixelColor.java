package test;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.imageio.ImageIO;

import java.util.*;

class GetPixelColor
{

	static List rectList = new ArrayList();

	// int y, x, tofind, col;
	/**
	 * @param args
	 *            the command line arguments
	 * @throws IOException
	 */
	public static void main(String args[]) throws IOException
	{
		int x1 = 0, x2 = 0, y1 = 0, y2 = 0;
		try
		{
			// read image file
			File file1 = new File("C:/Users/ATUL/Desktop/Table_Detect/1_stamp.jpg");
			BufferedImage image1 = ImageIO.read(file1);

			for (int y = 0; y < image1.getHeight(); y++)
			{
				for (int x = 0; x < image1.getWidth(); x++)
				{

					int c = image1.getRGB(x, y);
					Color color = new Color(c);
					if (color.getRed() < 30 && color.getGreen() < 30 && color.getBlue() < 30 && !contains(new Coordinate(x, y)))
					{
						x1 = x;
						y1 = y;
						for (int i = x; i < image1.getWidth(); i++)
						{
							c = image1.getRGB(i, y);
							color = new Color(c);
							if (!(color.getRed() < 30 && color.getGreen() < 30 && color.getBlue() < 30) || i == image1.getWidth())
							{
								x2 = i;
								break;
							}
						}
						for (int i = y; i < image1.getHeight(); i++)
						{
							c = image1.getRGB(x, i);
							color = new Color(c);
							if (!(color.getRed() < 30 && color.getGreen() < 30 && color.getBlue() < 30)
									|| i == image1.getHeight())
							{
								y2 = i;
								break;
							}
						}

						rectList.add(new Rectangle(new Coordinate(x1, y1), new Coordinate(x2, y2)));
					}
				}
			}
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		System.out.println("No of rectangles = " + rectList.size() + "\n");
		printRect();
		System.out.println("done="+rectList.size());
	}

	static void printRect() throws IOException
	{
		BufferedImage img=ImageIO.read(new File("C:/Users/ATUL/Desktop/Table_Detect/1_stamp.jpg"));
		Graphics2D g2d = img.createGraphics();
		g2d.setColor(Color.BLUE);
		Rectangle r = null;
		for (int i = 0; i < rectList.size(); i++)
		{
			r = (Rectangle) rectList.get(i);
			System.out.println("(" + r.a.x + "," + r.a.y + ")      (" + r.b.x + "," + r.b.y + ")");
			g2d.drawRect(r.a.x, r.a.y, r.b.x-r.a.x, r.b.y-r.a.y);
		}
		ImageIO.write(img, "png", new File("C:/Users/ATUL/Desktop/Table_Detect/rect.png"));
	}

	static boolean contains(Coordinate a)
	{
		Rectangle r = null;
		for (int i = 0; i < rectList.size(); i++)
		{
			r = (Rectangle) rectList.get(i);
			if (a.x >= r.a.x && a.x <= r.b.x && a.y >= r.a.y && a.y <= r.b.y)
				return true;
		}
		return false;
	}
}

class Rectangle
{
	Coordinate a, b;

	Rectangle(Coordinate a, Coordinate b)
	{
		this.a = a;
		this.b = b;
	}
}

class Coordinate
{
	int x, y;

	Coordinate(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
}