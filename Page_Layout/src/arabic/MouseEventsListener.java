package arabic;

//File MouseEventsListener.java

import java.awt.*;
import java.awt.event.MouseEvent;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputAdapter;

public class MouseEventsListener extends MouseInputAdapter {
 private ExtractSelectionFromPdf extractSelectionFromPdf;
 private CustomGlassPane customGlassPane;
 private Container contentPane;
 private Point topLeftPoint;
 private Point bottomRightPoint;

 public MouseEventsListener(final ExtractSelectionFromPdf extractSelectionFromPdf,
                            final CustomGlassPane customGlassPane,
                            final Container contentPane) {
     this.extractSelectionFromPdf = extractSelectionFromPdf;
     this.customGlassPane = customGlassPane;
     this.contentPane = contentPane;
 }

 public void mousePressed(final MouseEvent mouseEvent) {
     topLeftPoint = mouseEvent.getPoint();

     redispatchMouseEvent(mouseEvent);
 }

 public void mouseDragged(final MouseEvent mouseEvent) {
     bottomRightPoint = mouseEvent.getPoint();

     redispatchMouseEvent(mouseEvent, topLeftPoint != null, false);
 }

 public void mouseReleased(final MouseEvent mouseEvent) {
     bottomRightPoint = mouseEvent.getPoint();

     redispatchMouseEvent(mouseEvent, true, true);
 }

 public void mouseMoved(final MouseEvent mouseEvent) {
     redispatchMouseEvent(mouseEvent);
 }

 public void mouseClicked(final MouseEvent mouseEvent) {
     redispatchMouseEvent(mouseEvent);
 }

 public void mouseEntered(final MouseEvent mouseEvent) {
     redispatchMouseEvent(mouseEvent);
 }

 public void mouseExited(final MouseEvent mouseEvent) {
     redispatchMouseEvent(mouseEvent);
 }

 private void redispatchMouseEvent(final MouseEvent mouseEvent) {
     redispatchMouseEvent(mouseEvent, false, false);
 }

 private void redispatchMouseEvent(final MouseEvent mouseEvent, 
                                   final boolean repaint,
                                   final boolean extract) {
     final Point glassPanePoint = mouseEvent.getPoint();
     final Point containerPoint = SwingUtilities.convertPoint(customGlassPane, 
                                                              glassPanePoint, 
                                                              contentPane);

     if (containerPoint.y >= 0) {
         final Component component
                 = SwingUtilities.getDeepestComponentAt(contentPane,
                                                        containerPoint.x,
                                                        containerPoint.y);

         if (component != null) {
             final Point componentPoint 
                     = SwingUtilities.convertPoint(customGlassPane, 
                                                   glassPanePoint,
                                                   component);

             // Forward events to the component under the glass pane.
             component.dispatchEvent(new MouseEvent(component,
                                                    mouseEvent.getID(),
                                                    mouseEvent.getWhen(),
                                                    mouseEvent.getModifiers(),
                                                    componentPoint.x,
                                                    componentPoint.y,
                                                    mouseEvent.getClickCount(),
                                                    mouseEvent.isPopupTrigger()));
         }
     }

     // Update the glass pane if requested.
     if (repaint) {
         if (extract) {
             extractSelectionFromPdf.handleSelection(topLeftPoint, bottomRightPoint);

             topLeftPoint = null;
             bottomRightPoint = null;
         }

         customGlassPane.setSelection(topLeftPoint, bottomRightPoint);
         customGlassPane.repaint();
     }
 }
}