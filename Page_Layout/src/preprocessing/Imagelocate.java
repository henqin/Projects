package preprocessing;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.graphics.xobject.PDXObjectImage;

public class Imagelocate
{
	public static void main(String[] args) throws IOException
	{
	PDDocument document = null; 
	document = PDDocument.load("C:/Users/ATUL/Desktop/75008-PRVA-2711.pdf"); 
	List<PDPage> pages = document.getDocumentCatalog().getAllPages();
	for(int i=0;i<pages.size();i++)
	{
		PDPage page = pages.get(i);
		PDResources resources = page.getResources();
		Map<String, PDXObjectImage> pageImages = resources.getImages();
		if (pageImages != null) 
		{ 
			System.out.println("page "+(i+1)+" have image.");
//			Iterator imageIter = pageImages.keySet().iterator();
//			while (imageIter.hasNext()) 
//			{
//				String key = (String) imageIter.next();
//				PDXObjectImage image = (PDXObjectImage) pageImages.get(key);
//				image.write2OutputStream(null/* some output stream */);
//			}
		}
		else
		{
			System.out.println("page "+(i+1)+" have not not image.");
		}
	}
	}
}