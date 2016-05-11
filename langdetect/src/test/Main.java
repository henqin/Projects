package test;

import java.util.StringTokenizer;

import com.cybozu.labs.langdetect.Detector;
import com.cybozu.labs.langdetect.DetectorFactory;
import com.cybozu.labs.langdetect.LangDetectException;

public class Main {
    public static void main(String[] args) throws LangDetectException {
        DetectorFactory.loadProfile("C:/Users/ATUL/Documents/work/Projects/langdetect/temp/");
        StringTokenizer s = new StringTokenizer("the h 争 博 ");
        while (s.hasMoreElements()) {
            Detector d = DetectorFactory.create();
            String t = (String) s.nextElement();
            try{
            d.append(t);}
            catch(Exception e)
            {
                continue;
            }
            String a = d.detect();
            System.out.println("output=" + a);

        }
        System.out.println("done");

    }

}
