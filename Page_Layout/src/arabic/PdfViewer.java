package arabic;
//
////File PdfViewer.java
//
//import java.util.ResourceBundle;
import javax.swing.*;
//import org.icepdf.ri.common.*;
//import org.icepdf.ri.common.views.DocumentViewController;
//import org.icepdf.ri.util.PropertiesManager;
//
public class PdfViewer extends JPanel {
// private final SwingController controller;
//
 public PdfViewer() {
//     controller = new SwingController();
//     controller.setIsEmbeddedComponent(true);
//
//     final String bundleName = PropertiesManager.DEFAULT_MESSAGE_BUNDLE;
//     final ResourceBundle messageBundle = ResourceBundle.getBundle(bundleName);
//     final Properties systemProperties = System.getProperties();
//     final PropertiesManager properties = new PropertiesManager(systemProperties,
//                                                                messageBundle);
//
//     properties.set(PropertiesManager.PROPERTY_DEFAULT_ZOOM_LEVEL, "1");
//
//     final SwingViewBuilder factory = new SwingViewBuilder(controller, properties);
//
//     final DocumentViewController viewController 
//             = controller.getDocumentViewController();
//     viewController.setAnnotationCallback(new MyAnnotationCallback(viewController));
//
//     final JScrollPane scrollPane = new JScrollPane(factory.buildViewerPanel());
//     final int horizontalPolicy = ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS;
//     final int verticalPolicy = ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS;
//     scrollPane.setHorizontalScrollBarPolicy(horizontalPolicy);
//     scrollPane.setVerticalScrollBarPolicy(verticalPolicy);
//     add(scrollPane);
 }
//
 public void openDocument(final String filePath) {
//     controller.openDocument(filePath);
 }
//
 public int getCurrentPageNumber() {
     return 0;//controller.getCurrentPageNumber();
 }
}
