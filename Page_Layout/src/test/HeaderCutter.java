package test;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class HeaderCutter
{
	public static void main(String[] args)
	{
		BufferedImage img = null;

		int counts_header[] = new int[img.getHeight() / 4];
		int y1 = 0;

		for (int i = 0; i < img.getHeight() / 4; i++)
		{
			int count = 0;

			for (int j = (int) (0.05 * img.getWidth()); j < (int) (0.30 * img.getWidth()); j++)
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
			counts_header[i] = count;
		}

		for (int i = 0; i < (img.getHeight() / 4) - 4; i++)
		{
			if (counts_header[i] != 0 && counts_header[i + 1] != 0 && counts_header[i + 2] != 0)
			{
				y1 = i;
				break;
			}
		}
	}
}
