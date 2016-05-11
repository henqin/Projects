package test;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.commons.io.FilenameUtils;
import org.opencv.calib3d.Calib3d;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfDMatch;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.features2d.DMatch;
import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.FeatureDetector;
import org.opencv.features2d.Features2d;
import org.opencv.features2d.KeyPoint;
import org.opencv.highgui.Highgui;

import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PDFPage;

public class StampMatch1
{
	public StampMatch1()
	{
		super();
	}

	public static void main(String[] args) throws Exception
	{

		List<File> files = new ArrayList<File>();
		File file = new File("C:/Users/ATUL/Desktop/Stampdetec/");

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
				StampMatch1 s = new StampMatch1();
				s.Stamp(f);
			}
			else
			{
			}

		}
	}

	public void Stamp(File f) throws Exception
	{
		StampMatch1 s = new StampMatch1();
		String name = FilenameUtils.removeExtension(f.getName());
		List<BufferedImage> input = s.pdf_To_BufferedImage1(f, name);
		int pageNo = 1;
		for (BufferedImage img : input)
		{
			System.out.println("page " + pageNo + " has " + s.match(name + "_" + pageNo));
			pageNo++;
		}
	}

	public Mat bufferedImageToMat(BufferedImage bi)
	{
		Mat mat = new Mat(bi.getHeight(), bi.getWidth(), CvType.CV_8UC3);
		byte[] data = ((DataBufferByte) bi.getRaster().getDataBuffer()).getData();
		mat.put(0, 0, data);
		return mat;

	}

	public List<BufferedImage> pdf_To_BufferedImage1(File file, String name) throws Exception
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

			BufferedImage bufferedImage = new BufferedImage(w, h, BufferedImage.TYPE_3BYTE_BGR);

			Image img = page.getImage(w, h, null, null, true, true);
			// width , height , clip-rect , null for the ImageObserver , fill
			// background with white , block until drawing is done

			Graphics2D g = bufferedImage.createGraphics();
			g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

			g.drawImage(img, 0, 0, null);
			g.dispose();
			// System.out.println("page " + (pageNumber) + " is converted in image---pagesize=" + page.getCropBox().getWidth()
			// + " X " + page.getCropBox().getHeight() + "  image size=" + img.getWidth(null) + " X " + img.getHeight(null));

			ImageIO.write(bufferedImage, "tif", new File("C:/Users/ATUL/Desktop/Stampdetec/image/" + name + "_" + i + ".tif"));
			System.out.println("page " + (i) + " is converted in image.");

			Temp_Files.add(bufferedImage);
		}
		raf.close();
		channel.close();
		buf.clear();

		return Temp_Files;

	}

	public String match(String name)
	{
		String ans = "ok";
		try
		{
			System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
			String IMAGE_1_Path = "C:/Users/ATUL/Desktop/Stampdetec/stamp/s1.jpg";   // getting

			String feature = "C:/Users/ATUL/Desktop/Stampdetec/feature/" + name + ".tif";
			String output = "C:/Users/ATUL/Desktop/Stampdetec/output/" + name + ".tif";

			String IMAGE_2_Path = "C:/Users/ATUL/Desktop/Stampdetec/image/" + name + ".tif";

			Mat img_1 = Highgui.imread(IMAGE_1_Path, Highgui.CV_LOAD_IMAGE_GRAYSCALE);
			Mat img_2 = Highgui.imread(IMAGE_2_Path, Highgui.CV_LOAD_IMAGE_GRAYSCALE);

			if (img_1 == null || img_2 == null)
				System.out.println(" --(!) Error reading images \n");

			FeatureDetector detector = FeatureDetector.create(FeatureDetector.SURF); // (minHessian);//

			MatOfKeyPoint keypoints_1 = new MatOfKeyPoint();
			MatOfKeyPoint keypoints_2 = new MatOfKeyPoint();

			detector.detect(img_1, keypoints_1);
			detector.detect(img_2, keypoints_2);

			// -- Step 2: Calculate descriptors (feature vectors)
			DescriptorExtractor extractor = DescriptorExtractor.create(DescriptorExtractor.SIFT);// (minHessian);//

			Mat descriptors_1 = new Mat();
			Mat descriptors_2 = new Mat();

			extractor.compute(img_1, keypoints_1, descriptors_1);
			extractor.compute(img_2, keypoints_2, descriptors_2);

			// -- Step 3: Matching descriptor vectors using FLANN matcher
			DescriptorMatcher matcher = DescriptorMatcher.create(DescriptorMatcher.FLANNBASED);

			MatOfDMatch matches = new MatOfDMatch();

			matcher.match(descriptors_1, descriptors_2, matches);

			double max_dist = 0;
			double min_dist = 100;

			List<DMatch> matchesList = matches.toList();
			for (int i = 0; i < descriptors_1.rows(); i++)
			{
				double dist = matchesList.get(i).distance;
				if (dist < min_dist)
					min_dist = dist;

				if (dist > max_dist)
					max_dist = dist;
			}

			LinkedList<DMatch> good_matches = new LinkedList<DMatch>();

			for (int i = 0; i < descriptors_1.rows(); i++)
			{
				if (matchesList.get(i).distance <= Math.max(3 * min_dist, 0.02))
				{
					good_matches.addLast(matchesList.get(i));
				}
			}

			MatOfDMatch gm = new MatOfDMatch();
			gm.fromList(good_matches);

			// -- Draw only "good" matches
			Mat img_matches = new Mat();

			Features2d.drawMatches(img_1, keypoints_1, img_2, keypoints_2, gm, img_matches, new Scalar(255, 0, 0), new Scalar(0,
					0, 255), new MatOfByte(), 2);

			Highgui.imwrite(feature, img_matches);

			List<KeyPoint> Keypoint_Img1List = keypoints_1.toList();
			List<KeyPoint> Keypoint_Img2List = keypoints_2.toList();

			// *****************
			List<Point> obj = new LinkedList<Point>();
			List<Point> scene = new LinkedList<Point>();

			for (int i = 0; i < good_matches.size(); i++)
			{
				obj.add(Keypoint_Img1List.get(good_matches.get(i).queryIdx).pt);
				scene.add(Keypoint_Img2List.get(good_matches.get(i).trainIdx).pt);

			}

			MatOfPoint2f obj_p2f = new MatOfPoint2f();
			obj_p2f.fromList(obj);

			MatOfPoint2f scene_p2f = new MatOfPoint2f();
			scene_p2f.fromList(scene);

			Mat H = Calib3d.findHomography(obj_p2f, scene_p2f, Calib3d.RANSAC, 20);

			Mat Img1_Corners = new Mat(4, 1, CvType.CV_32FC2);
			Mat Img2_Corners = new Mat(4, 1, CvType.CV_32FC2);

			Img1_Corners.put(0, 0, new double[] { 0, 0 });
			Img1_Corners.put(1, 0, new double[] { img_1.cols(), 0 });
			Img1_Corners.put(2, 0, new double[] { img_1.cols(), img_1.rows() });
			Img1_Corners.put(3, 0, new double[] { 0, img_1.rows() });

			Point P3 = new Point(Img1_Corners.get(0, 0));
			Point P4 = new Point(Img1_Corners.get(2, 0));
			int area_original = (int) Math.abs(Math.abs(P3.x - P4.x) * Math.abs(P3.y - P4.y));

			Core.perspectiveTransform(Img1_Corners, Img2_Corners, H);
			Point P1 = new Point(Img2_Corners.get(0, 0));
			Point P2 = new Point(Img2_Corners.get(2, 0));
			int area_find = (int) Math.abs(Math.abs(P1.x - P2.x) * Math.abs(P1.y - P2.y));
			int ratio = (area_find * 100) / area_original;
			if (ratio > 70)
			{
				System.out.println(area_original + " " + area_find + " " + ratio + "stamp found in " + name);
				Core.rectangle(img_2, P1, P2, new Scalar(0, 0, 255), -1);
				Highgui.imwrite(output, img_2);
			}
		} catch (Exception e)
		{
			System.out.println(e);
			ans = "not ok";
		}
		return ans;
	}
}
