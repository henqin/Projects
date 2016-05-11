package com.tbits.test;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.commons.io.FilenameUtils;
import org.ghost4j.document.PDFDocument;
import org.ghost4j.renderer.SimpleRenderer;

import com.sun.media.jai.codec.ImageCodec;
import com.sun.media.jai.codec.ImageEncoder;
import com.sun.media.jai.codec.TIFFEncodeParam;
import com.sun.media.jai.codec.TIFFField;
import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PDFPage;

public class PdfToTiff
{
	public static void main(String[] args) throws Exception
	{
		List<File> files = new ArrayList<File>();
		File file = new File("C:/Users/ATUL/Desktop/Page-layout/testing/input/");
		for (File f : file.listFiles())
		{
			if (f.isDirectory())
			{
			}
			else
			{
				files.add(f);
			}
		}

		for (File f : files)
		{
			if (FilenameUtils.getExtension(f.getName()).compareToIgnoreCase("pdf") == 0)
			{
				String output = "C:/Users/ATUL/Desktop/Page-layout/testing/output/" + FilenameUtils.removeExtension(f.getName())
						+ "_English.pdf";
				PdfToTiff pt = new PdfToTiff();
				pt.tiff_Maker(pt.pdf_To_BufferedImage1(f), output);
			}

		}
		System.out.println("done");
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

			int w = 4*(int) page.getBBox().getWidth();
			int h = 4*(int) page.getBBox().getHeight();

			System.out.println("w= " + w + "  h=" + h);

			BufferedImage bufferedImage = new BufferedImage(w, h, BufferedImage.TYPE_BYTE_BINARY);

			Image img = page.getImage(w, h, null, null, true, true);
			// width , height , clip-rect , null for the ImageObserver , fill
			// background with white , block until drawing is done

			Graphics2D g = bufferedImage.createGraphics();
			g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

			g.drawImage(img, 0, 0, null);
			g.dispose();

			ImageIO.write(bufferedImage, "tif", new File("C:/Users/ATUL/Desktop/Page-layout/image/300_output" + i + ".tif"));
			System.out.println("page " + (i) + " is converted in image.");

			Temp_Files.add(bufferedImage);
		}
		raf.close();
		channel.close();
		buf.clear();

		return Temp_Files;

	}

	
	
	public List<BufferedImage> pdf_To_BufferedImage300(File file)
	{
		List<BufferedImage> Temp_Files = new ArrayList<BufferedImage>();
		try
		{
			System.load("C:/Tess4J/lib/win32-x86-64/gsdll64.dll");
			try
			{
				PDFDocument document = new PDFDocument();
				document.load(file);
				SimpleRenderer renderer1 = new SimpleRenderer();
				renderer1.setResolution(300);

				List<Image> images = renderer1.render(document);
				renderer1 = null;
				document = null;

				int page = 1;
				for (int i = 0; i < images.size(); i++)
				{
					int w = images.get(i).getWidth(null);
					int h = images.get(i).getHeight(null);
					System.out.println("dimension for image " + page + " = " + w + " X " + h);
					BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_BYTE_BINARY);
					Graphics2D g = bi.createGraphics();
					g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
					g.drawImage(images.get(i), 0, 0, null);
					g.dispose();

					Temp_Files.add(bi);
					page++;
				}
			} catch (Exception e)
			{
				System.out.println("ERROR: " + e.getMessage());
			}
		} finally
		{

		}
		return Temp_Files;
	}

	public void tiff_Maker(List<BufferedImage> output, String result) throws IOException
	{
		TIFFEncodeParam params = new TIFFEncodeParam();

		OutputStream out = new FileOutputStream(result);
		List<BufferedImage> imageList = new ArrayList<BufferedImage>();
		for (int i = 1; i < output.size(); i++)
		{
			imageList.add(output.get(i));
		}
		params.setWriteTiled(true);
		params.setCompression(TIFFEncodeParam.COMPRESSION_GROUP4);
		params.setExtraImages(imageList.iterator());

		TIFFField[] extras = new TIFFField[2];
		extras[0] = new TIFFField(282, TIFFField.TIFF_RATIONAL, 1,
				(Object) new long[][] { { (long) 300, (long) 1 }, { (long) 0, (long) 0 } });
		extras[1] = new TIFFField(283, TIFFField.TIFF_RATIONAL, 1,
				(Object) new long[][] { { (long) 300, (long) 1 }, { (long) 0, (long) 0 } });
		params.setExtraFields(extras);

		ImageEncoder encoder = ImageCodec.createImageEncoder("tiff", out, params);
		encoder.encode(output.get(0));
		out.close();

		output = null;
		imageList.clear();
		imageList = null;
		encoder = null;
		params = null;
	}

}
