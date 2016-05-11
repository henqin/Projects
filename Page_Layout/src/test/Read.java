package test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Read
{
	public static void main(String[] args) throws IOException
	{
		BufferedImage b=ImageIO.read(new File("C:/Users/ATUL/Desktop/Page-layout/testing/11.tif"));
		System.out.println(b);
	}

}
