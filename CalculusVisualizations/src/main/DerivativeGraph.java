package main;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class DerivativeGraph extends Visualizer{
    private int order;
    private Color mainColor = Color.MEDIUMSEAGREEN;

    public DerivativeGraph(int order, Function function) {
        this.order = order;
        this.function = function;
    }

    public void draw() {
        if(showing) {
            GraphicsContext gc = Main.getGc();
            gc.beginPath();
            double MIN_X = Main.getMin_X();
            double MAX_X = Main.getMax_X();
            double zoomTransform = Main.getZoomTransform();
            int XRES = Main.getXRES();
            for(double d = MIN_X; d < MAX_X; d += (5 / (double) XRES * zoomTransform)){
                double yValue = Graph.getPixelSpace(d, function.takeDerivative(order, d))[1];
                double xValue = Graph.getPixelSpace(d, 5)[0];
                gc.lineTo(xValue, yValue);
            }
            gc.setStroke(mainColor);
            gc.stroke();
        }
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public void setMainColor(Color mainColor) {
        this.mainColor = mainColor;
    }
}

