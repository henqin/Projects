package tess4jTest;

import java.io.File;
import net.sourceforge.tess4j.*;

public class Testtess2 {

    public static void main(String[] args) {
        org.apache.log4j.PropertyConfigurator.configure("C:/Users/Saurabh/Desktop/project/tess4j example/TESTTess/res/log4j.properties.txt"); // sets properties file for log4j
        
        File image = new File("C:/Users/Saurabh/Desktop/project/tess4j example/TESTTess/res/eurotext.pdf");
        Tesseract tessInst = new Tesseract();
        tessInst.setDatapath("C:/Users/Saurabh/Desktop/project/tess4j example/TESTTess/tessdata");
        
        try {
            String result= tessInst.doOCR(image);
            System.out.println(result);
        } catch (TesseractException e) {
            System.err.println(e.getMessage());
        }

    }

}