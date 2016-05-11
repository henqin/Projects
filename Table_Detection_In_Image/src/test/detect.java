//package test;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import org.opencv.core.Core;
//import org.opencv.core.Mat;
//import org.opencv.core.MatOfInt;
//import org.opencv.core.MatOfPoint;
//import org.opencv.core.Point;
//import org.opencv.core.Size;
//import org.opencv.highgui.Highgui;
//import org.opencv.imgproc.Imgproc;
//
//class Detect
//{
//	public static void main(String[] args)
//	{
//
//	}
//
//	double angle(Point pt1, Point pt2, Point pt0)   // double angle( cv::Point pt1, cv::Point pt2, cv::Point pt0 )
//	{
//		double dx1 = pt1.x - pt0.x;
//		double dy1 = pt1.y - pt0.y;
//		double dx2 = pt2.x - pt0.x;
//		double dy2 = pt2.y - pt0.y;
//		return (dx1 * dx2 + dy1 * dy2) / Math.sqrt((dx1 * dx1 + dy1 * dy1) * (dx2 * dx2 + dy2 * dy2) + 1e-10);
//
//	}
//
//	List<Point> findSquaresInImage(Mat image)         //(std::vector<std::vector<cv::Point> >)findSquaresInImage:(cv::Mat)_image
//{
//    //std::vector<std::vector<cv::Point> > squares;
//    List<Point> squares =new List<Point>();
//	
//    //cv::Mat pyr, timg, gray0(_image.size(), CV_8U), gray;
//    Mat pyr=new Mat();
//    Mat timg=new Mat();
//    Mat gray0=new Mat();
//    Mat gray=new Mat();
//    
//    
//    int thresh = 50, N = 11;
//    
//    //cv::pyrDown(_image, pyr, cv::Size(_image.cols/2, _image.rows/2));
//    Size s=new Size(image.cols()/2, image.rows()/2);
//    Imgproc.pyrDown(image, pyr, s);
//    
//    //cv::pyrUp(pyr, timg, _image.size());
//    Imgproc.pyrUp(pyr, timg, image.size());
//
//    
//    //std::vector<std::vector<cv::Point> > contours;
//    List<MatOfPoint> contours=new ArrayList<MatOfPoint>();
//    
//    for( int c = 0; c < 3; c++ ) 
//    {
//        int ch[] = {c, 0};
//        MatOfInt moi=new MatOfInt();
//        
//        //mixChannels(&timg, 1, &gray0, 1, ch, 1);
//       Core.mixChannels(timg, gray0,);
//        for( int l = 0; l < N; l++ ) 
//        {
//            if( l == 0 ) {
//                cv::Canny(gray0, gray, 0, thresh, 5);
//                cv::dilate(gray, gray, cv::Mat(), cv::Point(-1,-1));
//            }
//            else {
//            	
//                gray = gray0 >= (l+1)*255/N;
//            }
//            cv::findContours(gray, contours, CV_RETR_LIST, CV_CHAIN_APPROX_SIMPLE);
//            std::vector<cv::Point> approx;
//            for( size_t i = 0; i < contours.size(); i++ )
//            {
//                cv::approxPolyDP(cv::Mat(contours[i]), approx, arcLength(cv::Mat(contours[i]), true)*0.02, true);
//                if( approx.size() == 4 && fabs(contourArea(cv::Mat(approx))) > 1000 && cv::isContourConvex(cv::Mat(approx))) {
//                    double maxCosine = 0;
//
//                    for( int j = 2; j < 5; j++ )
//                    {
//                        double cosine = fabs(angle(approx[j%4], approx[j-2], approx[j-1]));
//                        maxCosine = MAX(maxCosine, cosine);
//                    }
//
//                    if( maxCosine < 0.3 ) {
//                        squares.push_back(approx);
//                    }
//                }
//            }
//        }
//    }
//    return squares;
//}
//}