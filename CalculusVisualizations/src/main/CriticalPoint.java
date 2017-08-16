package main;

import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class CriticalPoint extends Visualizer {
    private List<Double> criticalPointCoords = new ArrayList<>();
    private List<Point> criticalPoints = new ArrayList<>();
    private Color mainColor = Color.MEDIUMSEAGREEN;

    public CriticalPoint(Function function) {
        this.function = function;
    }

    @Override
    public void draw() {

       if(showing) {
           for(Point p : criticalPoints) {
               p.drawPoint();
           }
       }
    }

    public void addPoint(double d) {
            criticalPoints.add(new Point(d, function.computeFunc(d), mainColor.darker().darker()));
    }

    public void setMainColor(Color mainColor) {
        this.mainColor = mainColor;
    }

    public void cullLists() {
        criticalPoints.clear();
        criticalPointCoords.clear();
    }
}
