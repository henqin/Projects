package test;

import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint3f;
import org.opencv.core.Point;
import org.opencv.core.Point3;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

public class SemiCircle
{
	public static void main(String[] args)
	{
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		String name="76500_PTR-4805_SPA_";

		for (int k = 1; k <= 69; k++)
		{

			Mat src = Highgui.imread("C:/Users/ATUL/Desktop/Stampdetec/input/" +name+ k + ".tif", Highgui.CV_LOAD_IMAGE_COLOR);
			Mat src_gray = Highgui.imread("C:/Users/ATUL/Desktop/Stampdetec/input/"+name + k + ".tif", Highgui.CV_LOAD_IMAGE_GRAYSCALE);

//			Imgproc.GaussianBlur(src_gray, src_gray, new Size(9, 9), 2, 2);
//			Imgproc.threshold(src_gray, src_gray, 50, 255, Imgproc.THRESH_BINARY_INV);
			Highgui.imwrite("C:/Users/ATUL/Desktop/Stampdetec/mat/"+name + k + ".tif", src_gray);
			MatOfPoint3f circles = new MatOfPoint3f();
			Imgproc.HoughCircles(src_gray, circles, Imgproc.CV_HOUGH_GRADIENT, 1, src_gray.rows() / 8, 200, 100, 50, 250);

			List<Point3> cir_list = circles.toList();
			System.out.println("size of circlelist="+cir_list.size());
			for (int i = 0; i < cir_list.size(); i++)
			{
				
				Point center = new Point(cir_list.get(i).x, cir_list.get(i).y);
				int radius = (int) cir_list.get(i).z;
//				System.out.println("radius for " + k + " and "+i+" =" + radius);
				if (radius > 50)
				{
					Core.circle(src, center, 3, new Scalar(0, 255, 0), -1, 8, 0);
					Core.circle(src, center, radius, new Scalar(0, 0, 255), 3, 8, 0);
					Highgui.imwrite("C:/Users/ATUL/Desktop/Stampdetec/output/"+name + k + ".tif", src);
				}
			}

			System.out.println("image no " + k + " done and size = "+src.rows()+" X "+src.cols());
		}
		System.out.println("all done");

	}

	public void ShapeDetection()
	{
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

		for (int k = 1; k <= 1; k++)
		{

			// cv::Mat color = cv::imread("../houghCircles.png");
			// cv::namedWindow("input"); cv::imshow("input", color);
			Mat color = Highgui.imread("C:/Users/ATUL/Desktop/Stampdetec/" + k + ".tif", Highgui.CV_LOAD_IMAGE_COLOR);

			// cv::Mat canny;
			Mat canny = new Mat();

			// cv::Mat gray;
			// /// Convert it to gray
			// cv::cvtColor( color, gray, CV_BGR2GRAY );
			Mat gray = Highgui.imread("C:/Users/ATUL/Desktop/Stampdetec/" + k + ".tif", Highgui.CV_LOAD_IMAGE_GRAYSCALE);

			// compute canny (don't blur with that image quality!!)
			// cv::Canny(gray, canny, 200,20);
			// cv::namedWindow("canny2"); cv::imshow("canny2", canny>0);
			Imgproc.Canny(gray, canny, 200, 20);

			// std::vector<cv::Vec3f> circles;
			MatOfPoint3f circles = new MatOfPoint3f();

			// / Apply the Hough Transform to find the circles
			// cv::HoughCircles( gray, circles, CV_HOUGH_GRADIENT, 1, 60, 200, 20, 0, 0 );
			Imgproc.HoughCircles(gray, circles, Imgproc.CV_HOUGH_GRADIENT, 1, 60, 200, 20, 0, 0);

			List<Point3> cir_list = circles.toList();
			// / Draw the circles detected
			// for( size_t i = 0; i < circles.size(); i++ )
			for (int i = 0; i < cir_list.size(); i++)
			{
				// Point center(cvRound(circles[i][0]), cvRound(circles[i][1]));
				Point center = new Point(cir_list.get(i).x, cir_list.get(i).y);

				// int radius = cvRound(circles[i][2]);
				int radius = (int) cir_list.get(i).z;

				// cv::circle( color, center, 3, Scalar(0,255,255), -1);
				Core.circle(color, center, radius, new Scalar(0, 255, 0));

				// cv::circle( color, center, radius, Scalar(0,0,255), 1 );
			}

			Highgui.imwrite("C:/Users/ATUL/Desktop/Stampdetec/circle_" + k + ".tif", color);

			// compute distance transform:
			// cv::Mat dt;
			Mat dt = new Mat();

			// Mat canny_inv=canny.inv();
			// Mat invertcolormatrix= new Mat(canny.rows(),canny.cols(), canny.type(), new Scalar(255,255,255));
			// Core.subtract(invertcolormatrix, canny, canny);

			// cv::distanceTransform(255-(canny>0), dt, CV_DIST_L2 ,3);
			// cv::namedWindow("distance transform"); cv::imshow("distance transform", dt/255.0f);
			Imgproc.distanceTransform(canny, dt, Imgproc.CV_DIST_L2, 3);
			Highgui.imwrite("C:/Users/ATUL/Desktop/Stampdetec/distance" + k + ".tif", dt);

			// // test for semi-circles:
			float minInlierDist = 2.0f;
			// for( size_t i = 0; i < circles.size(); i++ )
			for (int i = 0; i < cir_list.size(); i++)
			{
				// test inlier percentage:
				// sample the circle and check for distance to the next edge
				int counter = 0;
				int inlier = 0;

				// cv::Point2f center((circles[i][0]), (circles[i][2]));
				Point center = new Point(cir_list.get(i).x, cir_list.get(i).z); // z point

				// float radius = (circles[i][2]);
				float radius = (float) cir_list.get(i).z;

				// maximal distance of inlier might depend on the size of the circle
				float maxInlierDist = radius / 25.0f;
				if (maxInlierDist < minInlierDist)
					maxInlierDist = minInlierDist;

				// TODO: maybe paramter incrementation might depend on circle size!
				for (float t = 0; t < 2 * 3.14159265359f; t += 0.1f)
				{
					counter++;
					// float cX = radius*Math.cos(t) + circles[i][0];
					// float cY = radius*Math.sin(t) + circles[i][3];
					float cX = (float) (radius * Math.cos(t) + cir_list.get(i).x);
					float cY = (float) (radius * Math.sin(t) + cir_list.get(i).z);

					float[] data = new float[3];
					// if(dt.at<float>(cY,cX) < maxInlierDist)
					try
					{
						if (dt.get((int) cX, (int) cY, data) < maxInlierDist)
						{
							inlier++;
							// cv::circle(color, cv::Point2i(cX,cY),3, cv::Scalar(0,255,0));
							Core.circle(color, new Point(cX, cY), 3, new Scalar(0, 0, 255));
						}
						else
						{
							// cv::circle(color, cv::Point2i(cX,cY),3, cv::Scalar(255,0,0));
							Core.circle(color, new Point(cX, cY), 3, new Scalar(255, 0, 0));
						}
					} catch (Exception e)
					{
						System.out.println(e);
						continue;
					}
				}
				// std::cout << 100.0f*(float)inlier/(float)counter << " % of a circle with radius " << radius << " detected" <<
				// std::endl;
				System.out.println((100.0f * (float) inlier / (float) counter) + " % of a circle with radius " + radius
						+ " detected");
			}

			// cv::namedWindow("output"); cv::imshow("output", color);
			// cv::imwrite("houghLinesComputed.png", color);
			Highgui.imwrite("C:/Users/ATUL/Desktop/Stampdetec/final" + k + ".tif", color);

			// cv::waitKey(-1);
			// return 0;
		}
		System.out.println("done");

	}
}
