package main;

import javafx.scene.paint.Color;

public class SecantLine extends Visualizer{
    private double xPos;
    private double deltaX;
    private Color mainColor = Color.MEDIUMSEAGREEN;

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
            new Point(xOne, yOne, mainColor).drawPoint();
            new Point(xTwo, yTwo, mainColor).drawPoint();
            Main.drawLine(xOne, yOne, xTwo, yTwo, mainColor);
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
