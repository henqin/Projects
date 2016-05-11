package finaljavacode;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

import javax.imageio.ImageIO;

/**
 * complete
 * 
 * @author ATUL
 *
 */

public class Page_Orientation
{

	public static void main(String[] args) throws IOException,
			InterruptedException
	{
		String input = "C:/Users/ATUL/Desktop/orientation/e.tif";
		String output = "C:/Users/ATUL/Desktop/orientation/e_rotate.jpg";

		BufferedImage img = ImageIO.read(new File(input));
		// make temp image;
		String WorkingDir = ("C:/Tesseract-OCR/Example/");

		File Temp_Image = new File(WorkingDir + "test.jpg");
		if (Temp_Image.exists())
			Temp_Image.delete();
		ImageIO.write(img, "jpg", Temp_Image);

		List<String> lines = Arrays.asList("cd C:/Tesseract-OCR/", "tesseract "
				+ WorkingDir + "test.jpg " + WorkingDir
				+ "test1.txt -l eng -psm 0 2>> " + WorkingDir + "temp.txt",
				"exit");
		Path Bat_file = Paths.get(WorkingDir + "tess.bat");
		Files.write(Bat_file, lines, Charset.forName("UTF-8"));
		int deg = 0;

		Runtime runtime = Runtime.getRuntime();
		Process p1 = runtime.exec("cmd /c  " + WorkingDir + "tess.bat");
		p1.waitFor();

		try (BufferedReader br = new BufferedReader(new FileReader(WorkingDir
				+ "temp.txt")))
		{

			String sCurrentLine;

			while ((sCurrentLine = br.readLine()) != null)
			{

				System.out.println(sCurrentLine);
				StringTokenizer st = new StringTokenizer(sCurrentLine);
				while (st.hasMoreElements())
				{
					String temp = st.nextToken();
					if (temp.equalsIgnoreCase("degrees:"))
						deg = Integer.parseInt(st.nextToken());
				}

			}

		} catch (IOException e)
		{
			e.printStackTrace();
		}

		System.out.println("value=" + deg);

		img = rotate(img, deg);

		ImageIO.write(img, "jpg", new File(output));

		File text = new File(WorkingDir + "temp.txt");

		if (text.exists())
			text.delete();
		text.deleteOnExit();
		Files.delete(Bat_file);
		Temp_Image.delete();
		System.out.println("done");

	}

	public static BufferedImage rotate(BufferedImage img, double angle)
			throws IOException
	{
		double sin = Math.abs(Math.sin(Math.toRadians(angle))), cos = Math
				.abs(Math.cos(Math.toRadians(angle)));

		int w = img.getWidth(null), h = img.getHeight(null);

		int neww = (int) Math.floor(w * cos + h * sin), newh = (int) Math
				.floor(h * cos + w * sin);

		BufferedImage bimg = new BufferedImage(neww, newh,
				BufferedImage.TYPE_3BYTE_BGR);
		Graphics2D g = bimg.createGraphics();

		g.translate((neww - w) / 2, (newh - h) / 2);
		g.rotate(Math.toRadians(angle), w / 2, h / 2);
		g.drawRenderedImage(img, null);
		g.dispose();
		return (bimg);
	}
}

/*
 * // create a new list of arguments for our process String[] list = {"cmd.exe",
 * "tesseract"};
 * // create the process builder ProcessBuilder pb = new ProcessBuilder(list);
 * try { // start the subprocess System.out.println("Starting the process..");
 * pb.start(); pb.redirectOutput(new File("C:/Users/ATUL/Desktop/ANS.txt")); }
 * catch (IOException ex) { ex.printStackTrace();
 * } } }
 */
/*
 * import java.io.IOException; import java.io.StringWriter;
 * import org.apache.commons.io.IOUtils;
 * class Sample { public static void main(String args[]) throws IOException {
 * String command =
 * "cmd /c start cmd /k E:\\vol231.exe -f E:\\KOHMOHOJOJO-PC-20140714-152414.raw imageinfo > output.txt"
 * ; Process p = Runtime.getRuntime().exec(command);
 * StringWriter writer = new StringWriter(); IOUtils.copy(p.getInputStream(),
 * writer, encoding); String theString = writer.toString(); }
 * }
 */