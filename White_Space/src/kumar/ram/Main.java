package kumar.ram;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.commons.io.FilenameUtils;


import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfArray;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfReader;
import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PDFPage;

public class Main 
{
	public static void main(String args[])
	{
		//for simple splitting pdf----------------------------------------------
		File f1=new File("C:/Users/ATUL/Desktop/BlankPage_Pdf/ws1.pdf");
		File f2=new File("C:/Users/ATUL/Desktop/BlankPage_Pdf/ws1_ans1.pdf");
		File f3=new File("C:/Users/ATUL/Desktop/BlankPage_Pdf/ws1_ans2.pdf");
		try{
		InputStream i=new FileInputStream(f1);
		Simple_splitIntoHalfPages(i,f2,f3);
		}
		catch(Exception e){System.out.println("aa");};

		//end--------------------------------------------------------
		System.out.println("done");
		
		
	}
	
	public List<File> convertImage(File file) throws Exception 
	{
		
		System.out.println("running-------06");
		List<File> Temp_Files = new ArrayList<File>();
		
		if((FilenameUtils.getExtension(file.getAbsolutePath())).compareTo("pdf")!=0)  //if file is not pdf then direct add to the temp file
		{
			Temp_Files.add(file);
			return Temp_Files;
		}
		
		String outputFile = file.getName().substring(0,file.getName().lastIndexOf("."));
        
		//  load a pdf from a file
        RandomAccessFile raf = new RandomAccessFile(file, "r");
        FileChannel channel = raf.getChannel();
        ByteBuffer buf = channel.map(FileChannel.MapMode.READ_ONLY,0, channel.size());
        PDFFile pdffile = new PDFFile(buf);
        
        
        
        int No_of_pages = pdffile.getNumPages();  //   get number of pages
        //  iterate through the number of pages
        for (int i = 1; i <=No_of_pages; i++) 
        {
            PDFPage page = pdffile.getPage(i);
 
            int w=4*(int) page.getBBox().getWidth();
            int h=4*(int) page.getBBox().getHeight();
            
            System.out.println("---------------------------------------width="+(int) page.getBBox().getWidth());
            System.out.println("---------------------------------------height="+(int) page.getBBox().getHeight());
            //  create new image
            
            BufferedImage bufferedImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
            
            Image img = page.getImage(w , h , null , null , true , true); 
            //width , height , clip-rect , null for the ImageObserver , fill background with white , block until drawing is done
                    
            
            Graphics g = bufferedImage.createGraphics();
            g.drawImage(img, 0, 0, null);
            g.dispose();
 
            File asd = new File("C:/Users/ATUL/Desktop/BlankPage_Pdf/"+"Output_"+ outputFile + i + ".png");
            if (asd.exists()) 
            	asd.delete();
            
            ImageIO.write(bufferedImage, "png", asd);
            Temp_Files.add(asd);
        }
        raf.close();
        
        System.out.println("end-------06");    
        return Temp_Files;
        
		
    }
	
	static void  Simple_splitIntoHalfPages(InputStream source, File target,File t2) throws IOException, DocumentException
	{
	    final PdfReader reader = new PdfReader(source);

	    try (   OutputStream targetStream = new FileOutputStream(target)    )
	    {
	    	OutputStream ts2 = new FileOutputStream(t2);
	    	
	        Document document = new Document();
	        Document d2=new Document();
	        PdfCopy copy = new PdfCopy(document, targetStream);
	        PdfCopy c2 = new PdfCopy(d2, ts2);
	        document.open();
	        d2.open();

	        for (int page = 1; page <= reader.getNumberOfPages(); page++)
	        {
	            PdfDictionary  pageN = reader.getPageN(page);
	            Rectangle cropBox = reader.getCropBox(page);
	            PdfArray leftBox = new PdfArray(new float[]{cropBox.getLeft(), cropBox.getBottom(), (cropBox.getLeft() + cropBox.getRight()) / 2.0f, cropBox.getTop()});
	            PdfArray rightBox = new PdfArray(new float[]{(cropBox.getLeft() + cropBox.getRight()) / 2.0f, cropBox.getBottom(), cropBox.getRight(), cropBox.getTop()});

	            PdfImportedPage importedPage = copy.getImportedPage(reader, page);
	            pageN.put(PdfName.CROPBOX, leftBox);
	            copy.addPage(importedPage);
	            pageN.put(PdfName.CROPBOX, rightBox);
	            c2.addPage(importedPage);
	        }

	        document.close();
	        d2.close();
	    }
	    finally
	    {
	        reader.close();
	    }
	}
}





