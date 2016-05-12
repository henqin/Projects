package arabic;

//File CustomGlassPane.java

import java.awt.*;
import javax.swing.JComponent;

public class CustomGlassPane extends JComponent {
 private Point topLeftPoint;
 private Point bottomRightPoint;

 public CustomGlassPane(final ExtractSelectionFromPdf extractSelectionFromPdf, 
                        final Container contentPane) {
     final MouseEventsListener listener 
             = new MouseEventsListener(extractSelectionFromPdf, this, contentPane);
     addMouseListener(listener);
     addMouseMotionListener(listener);
 }

 public void setSelection(final Point topLeftPoint, final Point bottomRightPoint) {
     this.topLeftPoint = topLeftPoint;
     this.bottomRightPoint = bottomRightPoint;
 }

 protected void paintComponent(final Graphics graphics) {
     if (topLeftPoint != null && bottomRightPoint != null) {
         graphics.setColor(Color.BLACK);
         graphics.drawRect(topLeftPoint.x, 
                           topLeftPoint.y,
                           bottomRightPoint.x - topLeftPoint.x, 
                           bottomRightPoint.y - topLeftPoint.y);
     }
 }
}


