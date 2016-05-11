package test;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

public class Table_Line_Detection
{
	static List rectList = new ArrayList();

	public static void main(String[] args) throws IOException
	{
		BufferedImage image = ImageIO.read(new File("C:/Users/ATUL/Desktop/Table_Detect/1_stamp.jpg"));
		testing(image);
	}

	static void testing(BufferedImage img) throws IOException
	{

		int[][] counts = new int[img.getWidth()][img.getHeight()];

		for (int i = 0; i < img.getWidth(); i++)
		{
			for (int j = 0; j < img.getHeight(); j++)
			{
				Color c = new Color(img.getRGB(i, j));
				if (c.getRed() <= 240 && c.getBlue() <= 240 && c.getGreen() <= 240)
				{
					counts[i][j] = 1;
				}
			}
		}

		int x1=0, x2=0, y1=0, y2 = 0;

		for (int y = 0; y < img.getHeight(); y++)
		{
			for (int x = 0; x < img.getWidth(); x++)
			{

				int c = counts[x][y];

				if (c == 1)
				{
					x1 = x;
					y1 = y;
					for (int i = x; i < img.getWidth(); i++)
					{
						c = counts[i][y];// img.getRGB(i, y);

						if (c == 0)
						{
							x2 = i;
							break;
						}
					}
					for (int i = y; i < img.getHeight(); i++)
					{
						c = counts[x][i];
						if (c == 0)
						{
							y2 = i;
							break;
						}
					}

					if ((x2 - x1 > (int) (.005 * img.getWidth())) && (y2 - y1 > (int) (.01 * img.getHeight())))
						rectList.add(new Rectangle(new Coordinate(x1, y1), new Coordinate(x2, y2)));
				}
			}
		}

		/*
		 * List<Line_Points> vertical_line = new ArrayList<Line_Points>();
		 * for (int i = 0; i < img.getWidth(); i++)
		 * {
		 * for (int j = 0; j < img.getHeight(); j++)
		 * {
		 * int lenth = 0;
		 * int y1 = j;
		 * int flag = 0;
		 * while (counts[i][j] == 1)
		 * {
		 * flag = 1;
		 * lenth++;
		 * j++;
		 * }
		 * int y2 = j - 1;
		 * if (flag == 1)
		 * j--;
		 * if (lenth > (int) (.01 * img.getHeight()))
		 * {
		 * vertical_line.add(new Line_Points(i, y1, y2));
		 * }
		 * }
		 * }
		 * System.out.println("total line=" + vertical_line.size());
		 * Graphics2D g2d = img.createGraphics();
		 * g2d.setColor(Color.BLUE);
		 * // draw the black vertical and horizontal lines
		 * for (Line_Points l : vertical_line)
		 * {
		 * g2d.drawLine(l.getX(), l.getY1(), l.getX(), l.getY2());
		 * }
		 * ImageIO.write(img, "png", new File("C:/Users/ATUL/Desktop/Table_Detect/result.png"));
		 * System.out.println("done");
		 * g2d.dispose();
		 * List<Line_Points> horizontal_line = new ArrayList<Line_Points>();
		 * for (int j = 0; j < img.getHeight(); j++)
		 * {
		 * for (int i = 0; i < img.getWidth(); i++)
		 * {
		 * int lenth = 0;
		 * int x1 = i;
		 * int flag = 0;
		 * while (counts[i][j] == 1)
		 * {
		 * flag = 1;
		 * lenth++;
		 * i++;
		 * }
		 * int x2 = i - 1;
		 * if (flag == 1)
		 * i--;
		 * if (lenth > (int) (.008 * img.getWidth()))
		 * {
		 * horizontal_line.add(new Line_Points(j, x1, x2));
		 * }
		 * }
		 * }
		 * System.out.println("total line=" + vertical_line.size());
		 * Graphics2D g2d1 = img.createGraphics();
		 * g2d1.setColor(Color.GREEN);
		 * for (Line_Points l : horizontal_line)
		 * {
		 * g2d1.drawLine(l.getY1(), l.getX(), l.getY2(), l.getX());
		 * }
		 * ImageIO.write(img, "png", new File("C:/Users/ATUL/Desktop/Table_Detect/result2.png"));
		 */
		printRect();
		System.out.println("done="+rectList.size());
	}
	
	
	static void printRect() throws IOException
	{
		BufferedImage img=ImageIO.read(new File("C:/Users/ATUL/Desktop/Table_Detect/1_stamp.jpg"));
		Graphics2D g2d = img.createGraphics();
		g2d.setColor(Color.GREEN);
		Rectangle r = null;
		for (int i = 0; i < rectList.size(); i++)
		{
			r = (Rectangle) rectList.get(i);
			System.out.println("(" + r.a.x + "," + r.a.y + ")      (" + r.b.x + "," + r.b.y + ")");
			g2d.drawRect(r.a.x, r.a.y, r.b.x-r.a.x, r.b.y-r.a.y);
		}
		ImageIO.write(img, "png", new File("C:/Users/ATUL/Desktop/Table_Detect/rect-1.png"));
	}
}
