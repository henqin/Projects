package test;

import org.opencv.core.Core;
import org.opencv.core.Core.MinMaxLocResult;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;
import org.opencv.core.Size;

/**
 * Example of template javacv (opencv) template matching using the last java build
 *
 * We need 4 default parameters like this
 * "C:\Users\Waldema\Desktop\bg.jpg" "C:\Users\Waldema\Desktop\logosiemens.jpg" "C:\Users\Waldema\Desktop\imageToFind.jpg" 100 200
 *
 * @author Waldemar Neto
 */
public class TemplateMatching
{
	static int i = 0;

	public static void main(String[] args)
	{
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		String templatePath = "C:/Users/ATUL/Desktop/Stampdetec/test/so1.png";
		Mat template = Highgui.imread(templatePath, Highgui.CV_LOAD_IMAGE_ANYDEPTH);
		for (int j = 1; j <= 69; j++)
		{
			String filePath = "C:/Users/ATUL/Desktop/Stampdetec/test/input/76500_PTR-4805_SPA_" + j + ".tif";

			Mat src = Highgui.imread(filePath, Highgui.CV_LOAD_IMAGE_ANYDEPTH);

			// down-scale and upscale the image to filter out the noise
			Mat tmp = new Mat();
			Imgproc.pyrDown(src, tmp, new Size(src.cols() / 2, src.rows() / 2));
			Imgproc.pyrUp(tmp, src, src.size());

			Mat tmp2 = new Mat();
			Imgproc.pyrDown(template, tmp2, new Size(template.cols() / 2, template.rows() / 2));
			Imgproc.pyrUp(tmp2, template, template.size());

			final Mat result = new Mat();

			Imgproc.matchTemplate(src, template, result, Imgproc.TM_CCOEFF_NORMED);

			MinMaxLocResult m = Core.minMaxLoc(result);

			System.out.println("page " + j + " maximum correlation is " + m.maxVal);
			Point tl = new Point();
			Point br = new Point();
			tl.x = m.maxLoc.x;
			tl.y = m.maxLoc.y;
			br.x = m.maxLoc.x + template.width();
			br.y = m.maxLoc.y + template.height();
			if (m.maxVal > .6)
				Core.rectangle(src, tl, br, new Scalar(0, 0, 0), -1);

			Highgui.imwrite("C:/Users/ATUL/Desktop/Stampdetec/test/output/76500_PTR-4805_SPA_" + j + ".tif", src);
			i++;
		}
		System.out.println("done");
	}
}
