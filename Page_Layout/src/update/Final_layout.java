package update;

import java.io.File;

public class Final_layout
{
	public static void main(String[] args) throws Exception
	{
		long a = System.currentTimeMillis();
		System.out.println("starting = "+a);
		for (int i = 1; i < 7; i++)
		{
			File f = new File("C:/Users/ATUL/Desktop/Page-layout/testing/input/" + i + ".tif");
			System.out.println("Thread " + i + " sent");
			ThreadClass t1 = new ThreadClass(f);
			t1.start();
		}
		long b = System.currentTimeMillis();
		System.out.println("final thead time = " + (b - a));

	}

}
