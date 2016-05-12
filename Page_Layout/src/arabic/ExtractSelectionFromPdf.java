package arabic;

import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.*;

import java.awt.Container;
import java.awt.Point;
import java.io.IOException;
import javax.swing.*;

public class ExtractSelectionFromPdf {
    private static String filePath = "[file path to a pdf file]";

    private PdfViewer pdfViewer;

    public static void main(final String[] arguments) {
        SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				new ExtractSelectionFromPdf().launchGUI();
			}
		});
    }

    private void launchGUI() {
        final JFrame frame = new JFrame("Extract selected text from a pdf");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        final Container contentPane = frame.getContentPane();

        pdfViewer = new PdfViewer();
        contentPane.add(pdfViewer);

        pdfViewer.openDocument(filePath);

        final CustomGlassPane customGlassPane = new CustomGlassPane(this, contentPane);
        frame.setGlassPane(customGlassPane);
        customGlassPane.setVisible(true);

        frame.setBounds(60, 10, 1800, 1000);
        frame.setVisible(true);
    }

    public void handleSelection(final Point topLeft, final Point bottomRight) {
        final int width = bottomRight.x - topLeft.x;
        final int height = bottomRight.y - topLeft.x;
        final String text = parsePdf(topLeft.x, topLeft.y, width, height, filePath);
        System.out.println("text: " + text);
    }

    public String parsePdf(final int x, final int y, final int width, final int height, 
                           final String pdfFilePath) {
        String text = null;

        try {
            final PdfReader pdfReader = new PdfReader(pdfFilePath);
            final int pageNumber = pdfViewer.getCurrentPageNumber() + 1;
            System.out.println("Page number: " + pageNumber);
            final Rectangle selection = new Rectangle(x, y, width, height);
            final RenderFilter renderFilter = new RegionTextRenderFilter(selection);
            final LocationTextExtractionStrategy delegate 
                    = new LocationTextExtractionStrategy();
            final TextExtractionStrategy extractionStrategy 
                    = new FilteredTextRenderListener(delegate, renderFilter);
            text = PdfTextExtractor.getTextFromPage(pdfReader, pageNumber, 
                                                    extractionStrategy);
            pdfReader.close();
        } catch (final IOException e) {
            e.printStackTrace();
        }

        return text;
    }
}


