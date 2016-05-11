package kumar.ram;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class Pdf_To_Image
{
	public static void main(String args[])
	{

		try
		{
			// PrintStream ram = new PrintStream(new
			// FileOutputStream("C:/Users/ATUL/Desktop/BlankPage_Pdf/output_222.txt"));
			// System.setOut(ram);

			File input = new File("C:/Users/ATUL/Desktop/BlankPage_Pdf/111.png");
			BufferedImage image = ImageIO.read(input);
			int width = image.getWidth();
			int height = image.getHeight();

			int lower = (int) (width * 0.45);
			int upper = (int) (width * 0.55);

			int[] black_pixel = new int[upper - lower];
			int i, j, k;

			int final_min = Integer.MAX_VALUE, final_max = Integer.MIN_VALUE, temp;
			for (k = 0, j = lower; j < upper; j++)
			{
				temp = 0;							// intialize temp
				for (i = 0; i < height; i++)
				{
					Color c = new Color(image.getRGB(j, i));
					if (!(c.getRed() >= 240 && c.getBlue() >= 240 && c
							.getGreen() >= 240))  // count black pixel
						temp++;
				}
				black_pixel[k] = temp;
				k++;
				if (temp < final_min)
					final_min = temp;
				if (temp > final_max)
					final_max = temp;
			}

			final_min = final_min + 5;
			int final_i = 0, final_j = 0, min, max;
			int final_lenth = Integer.MIN_VALUE;

			for (i = 0; i < black_pixel.length; i++)
			{
				if (black_pixel[i] <= final_min)
				{
					min = i;
					while (black_pixel[i] <= final_min)
						i++;
					i--;
					max = i;
					if (final_lenth < max - min + 1)
					{
						final_i = min;
						final_j = max;
						final_lenth = max - min + 1;
					}
				}
				i++;
			}

			int ans = (int) ((final_i + final_j) / 2) + lower;

			System.out.println("final_i=" + final_i + " final_j=" + final_j);

			// colomn no=504 value=0 width=1007 height=557
			// for(k=final_i;k<=final_j;k++)
			// System.out.println("pixel no "+k+" = "+black_pixel[k]);
			// System.out.println("max_value ="+final_max+"  min_value="+final_min);

			Image i1 = ImageIO.read(new File(
					"C:/Users/ATUL/Desktop/BlankPage_Pdf/111.png"));

			int x = 0, y = 0, w = ans, h = height;
			BufferedImage bi = new BufferedImage(w, h,
					BufferedImage.TYPE_INT_ARGB);
			bi.getGraphics()
					.drawImage(i1, 0, 0, w, h, x, y, x + w, y + h, null);
			ImageIO.write(bi, "png", new File(
					"C:/Users/ATUL/Desktop/BlankPage_Pdf/111_crop_1.png"));

			x = ans;
			y = 0;
			w = width - ans;
			h = height;
			BufferedImage bi2 = new BufferedImage(w, h,
					BufferedImage.TYPE_INT_ARGB);
			bi2.getGraphics().drawImage(i1, 0, 0, w, h, x, y, x + w, y + h,
					null);
			ImageIO.write(bi2, "png", new File(
					"C:/Users/ATUL/Desktop/BlankPage_Pdf/111_crop_11.png"));

		}

		catch (Exception e)
		{
			System.out.println(e);
		}
	}
}

/*
 * public static void main(String args[])
 * {
 * BufferedImage img = null;
 * File f = null;
 * //read image
 * try
 * {
 * f = new File("C:/Users/ATUL/Desktop/BlankPage_Pdf/1.jpg");
 * img = ImageIO.read(f);
 * }
 * catch(IOException e){System.out.println(e);}
 * //get image width and height
 * int width = img.getWidth();
 * int height = img.getHeight();
 * //convert to grayscale
 * for(int y = 0; y < height; y++)
 * {
 * for(int x = 0; x < width; x++)
 * {
 * int p = img.getRGB(x,y);
 * int a = (p>>24)&0xff;
 * int r = (p>>16)&0xff;
 * int g = (p>>8)&0xff;
 * int b = p&0xff;
 * //calculate average
 * int avg = (r+g+b)/3;
 * //replace RGB value with avg
 * p = (a<<24) | (avg<<16) | (avg<<8) | avg;
 * img.setRGB(x, y, p);
 * }
 * }
 * //write image
 * try
 * {
 * f = new File("C:/Users/ATUL/Desktop/BlankPage_Pdf/1_out.jpg");
 * ImageIO.write(img, "jpg", f);
 * }
 * catch(IOException e){System.out.println(e);}
 * }
 */

/*
 * class Grayscale
 * {
 * public static void main1(String args[])throws IOException{
 * BufferedImage img = null;
 * File f = null;
 * //read image
 * try{
 * f = new File("D:\\Image\\Taj.jpg");
 * img = ImageIO.read(f);
 * }catch(IOException e){
 * System.out.println(e);
 * }
 * //get image width and height
 * int width = img.getWidth();
 * int height = img.getHeight();
 * //convert to grayscale
 * for(int y = 0; y < height; y++){
 * for(int x = 0; x < width; x++){
 * int p = img.getRGB(x,y);
 * int a = (p>>24)&0xff;
 * int r = (p>>16)&0xff;
 * int g = (p>>8)&0xff;
 * int b = p&0xff;
 * //calculate average
 * int avg = (r+g+b)/3;
 * //replace RGB value with avg
 * p = (a<<24) | (avg<<16) | (avg<<8) | avg;
 * img.setRGB(x, y, p);
 * }
 * }
 * //write image
 * try{
 * f = new File("D:\\Image\\Output.jpg");
 * ImageIO.write(img, "jpg", f);
 * }catch(IOException e){
 * System.out.println(e);
 * }
 * }//main() ends here
 * }//class ends here
 */

// System.out.println("S.No: " + count + " Red: " + c.getRed() +"  Green: " +
// c.getGreen() + " Blue: " + c.getBlue());
// System.out.print(" 	value = " +(255-(int)(c.getRed() *
// 0.299)-(int)(c.getGreen() * 0.587)-(int)(c.getBlue() *0.114)));
// System.out.println("S.N.-"+count+" value="+image.getData().getSample(i, j,
// 0));
// ans=ans+((int)(c.getRed() * 0.299)+(int)(c.getGreen() *
// 0.587)+(int)(c.getBlue() *0.114));
// ans=ans+(int)c.getAlpha();
// ans=(int)(ans/height);
