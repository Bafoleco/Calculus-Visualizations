package main;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class SecantLine extends Visualizer{
    private double xPos;
    private double deltaX;
    private Color mainColor = Color.MEDIUMSEAGREEN;
    private String slope;

    public SecantLine(double x, double dx, Function function) {
        xPos = x;
        deltaX = dx;
        this.function = function;
    }

    public void draw() {
        if(showing) {
            double xOne = xPos;
            double yOne = function.computeFunc(xPos);
            double xTwo = xPos + deltaX;
            double yTwo = function.computeFunc(xPos + deltaX);
            slope = Main.round(Main.drawLine(xOne, yOne, xTwo, yTwo, mainColor));
            Main.getGc().setFill(Color.BLACK);
            Main.getGc().setFont(Font.font(15));
            Main.getGc().fillText( "Secant Slope: " + slope, Graph.getPixelSpace(xPos, 0)[0] + 20,
                    (Graph.getPixelSpace(0, Main.getMainFunction().computeFunc(xPos))[1]) + 20);



            new Point(xOne, yOne, mainColor).drawPoint();
            new Point(xTwo, yTwo, mainColor).drawPoint();
        }
    }

    public void setxPos(double xPos) {
        this.xPos = xPos;
    }

    public void setDeltaX(double deltaX) {
        this.deltaX = deltaX;
    }

    public void setMainColor(Color mainColor) {
        this.mainColor = mainColor;
    }
}
