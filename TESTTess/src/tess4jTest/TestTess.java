package tess4jTest;

import java.io.File;
import net.sourceforge.tess4j.*;

public class TestTess {

    public static void main(String[] args) {
        File image = new File("C:/Users/Saurabh/Desktop/project/tess4j example/TESTTess/res/eurotext.tif");
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