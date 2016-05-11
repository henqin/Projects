package test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Collections;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

/**
 * Simple shape detector program.
 * It loads an image and tries to find simple shapes (rectangle, triangle, circle, etc) in it.
 * This program is a modified version of `squares.cpp` found in the OpenCV sample dir.
 */

public class ShapeDetection
{
	static Mat dst;
	static Mat src;

	/**
	 * Helper function to find a cosine of angle between vectors
	 * from pt0->pt1 and pt0->pt2
	 */
	static double angle(Point pt1, Point pt2, Point pt0)
	{
		double dx1 = pt1.x - pt0.x;
		double dy1 = pt1.y - pt0.y;
		double dx2 = pt2.x - pt0.x;
		double dy2 = pt2.y - pt0.y;
		return (dx1 * dx2 + dy1 * dy2) / Math.sqrt((dx1 * dx1 + dy1 * dy1) * (dx2 * dx2 + dy2 * dy2) + 1e-10);
	}

	/**
	 * Helper function to display text in the center of a contour
	 */
	public static void setLabel(String label, MatOfPoint contour)
	{
		int fontface = Core.FONT_HERSHEY_SIMPLEX;
		double scale = 0.4;
		int thickness = 1;
		int[] baseline = { 0 };

		if (label == "RECT")
		{
			List<MatOfPoint> rectangle = new ArrayList<>();
			rectangle.add(contour);
			Core.polylines(dst, rectangle, true, new Scalar(0, 0, 0), Core.LINE_4);
		}
		Size text = Core.getTextSize(label, fontface, scale, thickness, baseline);
		Rect r = new Rect();
		r = Imgproc.boundingRect(contour);
		Point pt = new Point(r.x + ((r.width - text.width) / 2), r.y + ((r.height + text.height) / 2));
		Core.rectangle(dst, pt, new Point(pt.x + text.width, pt.y - text.height), new Scalar(255, 255, 255), Core.FILLED);
		Core.putText(dst, label, pt, fontface, scale, new Scalar(0, 255, 0), thickness, 8, false);
	}

	public static void main(String[] args)
	{
		long t1 = System.currentTimeMillis();
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

		int count = 0;
		String name = "76500_PTR-4805_SPA";

		for (int k = 1; k <= 62; k++)
		{

			String img_path = "C:/Users/ATUL/Desktop/Stampdetec/input/" + name + "_" + k + ".tif";
			// cv::Mat src = cv::imread("polygon.png");
			src = Highgui.imread(img_path, Highgui.CV_LOAD_IMAGE_COLOR);
		
			// Imgproc.threshold(src,src,127,255,Imgproc.THRESH_TOZERO);
			// src = src.submat(src.rows(), src.rows()/2, 0, src.cols());
			// int erosion_size = 2;
			// int dilation_size = 2;
			// Mat element = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(2 * erosion_size + 1, 2 * erosion_size +
			// 1));
			// Imgproc.erode(src, src, element);
			// Mat element1 = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(2 * dilation_size + 1,
			// 2 * dilation_size + 1));
			// Imgproc.dilate(src, src, element1);

			// Convert to grayscale
			Mat gray = Highgui.imread(img_path, Highgui.CV_LOAD_IMAGE_GRAYSCALE);
			
			// Imgproc.threshold(gray,gray,127,255,Imgproc.THRESH_TOZERO);
			// gray = gray.submat(gray.rows() , gray.rows()/2, 0, gray.cols());

			// Mat m2 = new Mat(src.size(), 0);
			// Imgproc.GaussianBlur(gray, m2, new Size(0, 0), 20);
			// Core.addWeighted(gray, 2.0, m2, 1.0, 0, m2);
			// gray = m2;
			// Imgproc.erode(gray, gray, element);
			// Imgproc.dilate(gray, gray, element1);

			// Use Canny instead of threshold to catch squares with gradient shading
			Mat canny = new Mat();
			double thresh = 25;
			Imgproc.Canny(gray, canny, 0, thresh, 5, true);
			Highgui.imwrite("C:/Users/ATUL/Desktop/Stampdetec/output/" + name + "_" + k + ".tif", canny);

			// Find contours
			List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
			Imgproc.findContours(canny.clone(), contours, new Mat(), Imgproc.RETR_CCOMP, Imgproc.CHAIN_APPROX_SIMPLE);

			MatOfPoint2f approx = new MatOfPoint2f();
			dst = src.clone();

			for (int i = 0; i < contours.size(); i++)
			{
				// Approximate contour with accuracy proportional
				// to the contour perimeter
				Imgproc.approxPolyDP(new MatOfPoint2f(contours.get(i).toArray()), approx,
						Imgproc.arcLength(new MatOfPoint2f(contours.get(i).toArray()), true) * 0.02, true);

				// Skip small or non-convex objects
				if (Math.abs(Imgproc.contourArea(contours.get(i))) < 100
						|| !Imgproc.isContourConvex(new MatOfPoint(approx.toArray())))
					continue;

				if (approx.toList().size() == 3)
				{
					// setLabel("TRI", contours.get(i)); // Triangles
				}
				else if (approx.toList().size() >= 4 && approx.toList().size() <= 6)
				{
					// // Number of vertices of polygonal curve
					// int vtc = approx.toList().size();
					//
					// // Get the cosines of all corners
					// LinkedList<Double> cos = new LinkedList<>();
					// for (int j = 2; j < vtc + 1; j++)
					// cos.addLast(angle(approx.toList().get(j % vtc), approx.toList().get(j - 2), approx.toList().get(j - 1)));
					//
					// // Sort ascending the cosine values
					// Collections.sort(cos);
					//
					// // Get the lowest and the highest cosine
					// Double mincos = cos.getFirst();
					// Double maxcos = cos.getLast();
					//
					// // Use the degrees obtained above and the number of vertices
					// // to determine the shape of the contour
					// if (vtc == 4 && mincos >= -0.1 && maxcos <= 0.3)
					// setLabel("RECT", contours.get(i));
					// else if (vtc == 5 && mincos >= -0.34 && maxcos <= -0.27)
					// setLabel("PENTA", contours.get(i));
					// else if (vtc == 6 && mincos >= -0.55 && maxcos <= -0.45)
					// setLabel("HEXA", contours.get(i));
				}
				else
				{
					// Detect and label circles
					double area = Imgproc.contourArea(contours.get(i));
					Rect r = new Rect();
					r = Imgproc.boundingRect(contours.get(i));
					Point p1 = r.tl();
					Point p2 = r.br();
					Point p3 = new Point((p1.x + p2.x) / 2, (p1.y + p2.y) / 2);
					int radius = r.width / 2;

					if (Math.abs(1 - ((double) r.width / r.height)) <= 0.4
							&& Math.abs(1 - (area / (Math.PI * Math.pow(radius, 2)))) <= 0.4)
					{
						setLabel("CIR", contours.get(i));
						System.out.println("page no =" + k);
						Core.circle(dst, p3, radius, new Scalar(0, 255, 0), 10);
						count++;
					}
				}
			}

			Highgui.imwrite("C:/Users/ATUL/Desktop/Stampdetec/mapping/" + name + "_" + k + ".tif", src);
			Highgui.imwrite("C:/Users/ATUL/Desktop/Stampdetec/mat/" + name + "_" + k + ".tif", dst);
		}
		System.out.println("total =" + count);
		long t2 = System.currentTimeMillis();
		System.out.println(t2 - t1);
	}
}