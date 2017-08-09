import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class RiemannSum extends Visualizer{

    double lowerBound;
    double upperBound;
    int numSteps = 1;
    String areaUnderCurve;

    /**
     * Mode 1 = left
     * Mode 2 = right
     * Mode 3 = minimum
     * Mode 4 = maximum
     * Mode 5 = middle
     */
    int mode = 1;

    public RiemannSum(double lowerBound, double upperBound, Function function) {
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
        this.function = function;
    }

    @Override
    public void draw() {
        if(showing) {
            GraphicsContext gc = Main.getGc();
            double area = 0;
            double length = Math.abs(lowerBound - upperBound);
            double stepSize = length / numSteps;
            for(double d = lowerBound; d < upperBound - 0.01; d += stepSize ) {
                double height = getValue(d, stepSize);
                area += stepSize * height;
                double pixHeight = Graph.getLengthY(height);

                double leftCornerX = Graph.getPixelSpace(d, 0)[0];
                double leftCornerY;
                if(height < 0) {
                    leftCornerY = Graph.getPixelSpace(d, 0)[1];
                    gc.setFill(Color.RED);
                }
                else {
                    leftCornerY = Graph.getPixelSpace(d, getValue(d, stepSize))[1];
                    gc.setFill(Color.GREEN);
                }
                    double width = Graph.getLengthX(stepSize);

                gc.fillRect(leftCornerX, leftCornerY, width, Math.abs(pixHeight));
            }
            new Point(lowerBound, function.computeFunc(lowerBound), Color.BLUEVIOLET).drawPoint();
            new Point(upperBound, function.computeFunc(upperBound), Color.BLUEVIOLET).drawPoint();
            //Draw lines under points
            //TODO rewrite using the drawlineSegment method
            double baseLineWidth = gc.getLineWidth();
            gc.setLineWidth(baseLineWidth + 1);
            gc.beginPath();
            gc.lineTo(Graph.getPixelSpace(lowerBound, 0)[0], Graph.getPixelSpace(0, 0)[1]);
            gc.lineTo(Graph.getPixelSpace(lowerBound, 0)[0], Graph.getPixelSpace(0,
                    function.computeFunc(lowerBound))[1] - Point.getOuterSize() / 2);
            gc.setStroke(Color.BLUEVIOLET);
            gc.stroke();
            gc.beginPath();
            gc.lineTo(Graph.getPixelSpace(upperBound, 0)[0], Graph.getPixelSpace(0, 0)[1]);
            gc.lineTo(Graph.getPixelSpace(upperBound, 0)[0], Graph.getPixelSpace(0, function.computeFunc(upperBound))[1]);
            gc.setStroke(Color.BLUEVIOLET);
            gc.stroke();
            gc.setLineWidth(baseLineWidth);
            areaUnderCurve = Main.round(area);
            gc.setFill(Color.BLACK);
            gc.setFont(Font.font(15));
            gc.fillText( "Area under the curve = "
                            + areaUnderCurve + "u^2", Graph.getPixelSpace(Main.getMin_X(), 0)[0] + 20,
                    Graph.getPixelSpace(0, Main.getMIN_Y())[1] - 20);
        }
    }

    /**
     * A method to get the value of a function at an interval, value depends on mode of riemann sun selected
     * this value will be used as the height of the rectangle
     */
    private double getValue(double xPos, double stepSize) {
        if(mode == 1) {
            return function.computeFunc(xPos);
        }
        else if(mode == 2) {
            return function.computeFunc(xPos + stepSize);
        }
        else if(mode == 3) {
            double leftValue = function.computeFunc(xPos);
            double rightValue = function.computeFunc(xPos + stepSize);
            if(leftValue < rightValue) {
                return leftValue;
            } else {
                return rightValue;
            }
        } else if(mode == 4) {
            double leftValue = function.computeFunc(xPos);
            double rightValue = function.computeFunc(xPos + stepSize);
            if (leftValue > rightValue) {
                return leftValue;
            } else {
                return rightValue;
            }
        }
        else if(mode == 5) {
            return function.computeFunc(xPos + .5 * stepSize);
        } else {
            Main.writeConsole("ERROR: Mode of Riemann summation not found");
            return 0;
        }
    }

    public void setLowerBound(double lowerBound) {
        this.lowerBound = lowerBound;
    }

    public void setUpperBound(double upperBound) {
        this.upperBound = upperBound;
    }

    public void setNumSteps(int numSteps) {
        this.numSteps = numSteps;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }
}
