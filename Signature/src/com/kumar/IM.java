package com.kumar;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import org.opencv.calib3d.Calib3d;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfDMatch;
import org.opencv.core.MatOfKeyPoint;
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

public class IM
{
	public static void main(String args[] )
	{
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		
		String IMAGE_1_Path = "C:/Users/ATUL/Desktop/signature/s11.jpg";   // getting image 1 path
		String IMAGE_2_Path = "C:/Users/ATUL/Desktop/signature/sample1.jpg";	//getting image 2 path
		String name="s_01_s_04.png";
		Mat img_1 = Highgui.imread(IMAGE_1_Path,Highgui.CV_LOAD_IMAGE_GRAYSCALE);
		Mat img_2 = Highgui.imread(IMAGE_2_Path,Highgui.CV_LOAD_IMAGE_GRAYSCALE);

		if( img_1==null || img_2==null )
			System.out.println(" --(!) Error reading images \n"); 

	//-- Step 1: Detect the keypoints using SURF Detector

		FeatureDetector detector = FeatureDetector.create(FeatureDetector.SURF); //(minHessian);//
		
		MatOfKeyPoint keypoints_1 = new MatOfKeyPoint();
		MatOfKeyPoint keypoints_2 = new MatOfKeyPoint();

		detector.detect( img_1, keypoints_1 );
		detector.detect( img_2, keypoints_2 );

	//-- Step 2: Calculate descriptors (feature vectors)
		DescriptorExtractor extractor = DescriptorExtractor.create(DescriptorExtractor.SIFT);//(minHessian);//
			
		//surf-sift---186
		//sift-sift-----91
		//surf-surf-----25
		//sift-surf----43
		Mat descriptors_1=new Mat();
		Mat descriptors_2= new Mat();

		extractor.compute( img_1, keypoints_1, descriptors_1 );
		extractor.compute( img_2, keypoints_2, descriptors_2 );

	//-- Step 3: Matching descriptor vectors using FLANN matcher
		DescriptorMatcher matcher = DescriptorMatcher.create(DescriptorMatcher.FLANNBASED);
		//FlannBasedMatcher matcher;
		
		MatOfDMatch matches = new MatOfDMatch();
		
		matcher.match( descriptors_1, descriptors_2, matches );

		double max_dist = 0; double min_dist = 100;

		List<DMatch> matchesList = matches.toList();
	
	//-- Quick calculation of max and min distances between keypoints
		for( int i = 0; i < descriptors_1.rows(); i++ )
		{ 
			double dist = matchesList.get(i).distance ;
			if( dist < min_dist ) min_dist = dist;
		
			if( dist > max_dist ) max_dist = dist;
		}

		System.out.println("-- Max dist :"+max_dist );
		System.out.println("-- Min dist :"+ min_dist );

	//-- Draw only "good" matches (i.e. whose distance is less than 2*min_dist,
	//-- or a small arbitary value ( 0.02 ) in the event that min_dist is very
	//-- small)
	//-- PS.- radiusMatch can also be used here.

		LinkedList<DMatch> good_matches = new LinkedList<DMatch>();

		for( int i = 0; i < descriptors_1.rows(); i++ )
		{ 
			if( matchesList.get(i).distance <= Math.max(3*min_dist, 0.02) )
			{ 
				good_matches.addLast(matchesList.get(i)); 
			}
		}
		
		MatOfDMatch gm = new MatOfDMatch();
		gm.fromList(good_matches);
		
	//-- Draw only "good" matches
		Mat img_matches =new Mat();

		Features2d.drawMatches(img_1 , keypoints_1 , img_2 , keypoints_2 , gm , img_matches,
				new Scalar(255, 0, 0),new Scalar(0, 0, 255), new MatOfByte(), 2);
	
		
	//-- Show detected matches
		try{
		File outputFile = new File("C:/Users/ATUL/Desktop/signature/"+name);
		Highgui.imwrite(outputFile.getAbsolutePath(), img_matches);
		} 
		catch (Exception e) {System.out.println("");}
		
		
		for( int i = 0; i < (int)good_matches.size(); i++ )
		{ 
			System.out.println( "-- Good Match ["+i+"] Keypoint 1:"+good_matches.get(i).queryIdx+"   -- Keypoint 2:"+ good_matches.get(i).trainIdx); 
		}
		
	//-- Localize the object
		List<KeyPoint> Keypoint_Img1List = keypoints_1.toList();
		List<KeyPoint> Keypoint_Img2List = keypoints_2.toList();
	
		List<Point> obj=new LinkedList<Point>();
		List<Point> scene=new LinkedList<Point>();

		for( int i = 0; i < good_matches.size(); i++ )
		{
			obj.add(Keypoint_Img1List.get(good_matches.get(i).queryIdx).pt);
			scene.add(Keypoint_Img2List.get(good_matches.get(i).trainIdx).pt);
		}
		  
		MatOfPoint2f obj_p2f = new MatOfPoint2f();
		obj_p2f.fromList(obj);
		  
		MatOfPoint2f scene_p2f = new MatOfPoint2f();
		scene_p2f.fromList(scene);
			
		Mat H = Calib3d.findHomography(obj_p2f, scene_p2f,Calib3d.RANSAC, 20);

		Mat Img1_Corners = new Mat(4, 1, CvType.CV_32FC2);
		Mat Img2_Corners = new Mat(4, 1, CvType.CV_32FC2);
		  
		Img1_Corners.put(0, 0, new double[] { 0, 0 });
		Img1_Corners.put(1, 0, new double[] { img_1.cols(), 0 });
		Img1_Corners.put(2, 0, new double[] { img_1.cols(), img_1.rows() });
		Img1_Corners.put(3, 0, new double[] { 0, img_1.rows() });
			
		Core.perspectiveTransform(Img1_Corners, Img2_Corners, H);
		///       src, dest, mat
		  
	//-- Draw lines between the corners (the mapped object in the scene - image_2 )
		Core.line(img_2, new Point(Img2_Corners.get(0,0)),new Point(Img2_Corners.get(1,0)),new Scalar(0, 255, 0),4);
		Core.line(img_2, new Point(Img2_Corners.get(1,0)),new Point(Img2_Corners.get(2,0)),new Scalar(0, 255, 0),4);
		Core.line(img_2, new Point(Img2_Corners.get(2,0)),new Point(Img2_Corners.get(3,0)),new Scalar(0, 255, 0),4);
		Core.line(img_2, new Point(Img2_Corners.get(3,0)),new Point(Img2_Corners.get(0,0)),new Scalar(0, 255, 0),4);

	//-- Show detected matches
		System.out.println("ram");
		System.out.println(new Point(Img2_Corners.get(0,0)));
		  
		  //Mat o1 = new Mat();
		//Core.absdiff(img_1, img_2, o1);
				//   src1     src2    destination
		//Mat output1 = new Mat();
		//Imgproc.threshold(o1,output1,100,255,Imgproc.THRESH_TOZERO);
		try 
		{
			Highgui.imwrite("C:/Users/ATUL/Desktop/signature/result.png", img_2);
		} 
		catch (Exception e) {System.out.println("");}
	}

}




