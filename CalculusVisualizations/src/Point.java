import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.*;

/**
 * Created by Bay Foley-Cox on 6/28/17.
 */
public class Point {
    double x;
    double y;
    int outerSize = 15;
    int innerSize = 5;
    Color color;
    public Point(double x, double y, Color color) {
        this.x = x;
        this.y = y;
        this.color = color;
    }
    public void drawPoint() {
        double zoom = TaylorSeries.getZoomTransform();
        GraphicsContext gc = TaylorSeries.getGc();
        gc.setFill(new Color(color.getRed(), color.getGreen(), color.getBlue(), .8));
        double pixX = Graph.getPixelSpace(x, y)[0];
        double pixY = Graph.getPixelSpace(x, y)[1];
        gc.fillOval(pixX -  (outerSize /2) , pixY - (outerSize/ 2), (outerSize) , (outerSize) );
        gc.setFill(color.darker());
        gc.fillOval(pixX - (innerSize / 2) , pixY - (innerSize/ 2), (innerSize) , (innerSize) );
        gc.setFill(new Color(color.darker().getRed(), color.darker().getGreen(), color.darker().getBlue(), .8));

    }

}
