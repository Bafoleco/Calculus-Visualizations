import javafx.scene.canvas.GraphicsContext;

public class RiemannSum extends Visualizer{

    double lowerBound;

    double upperBound;

    int numSteps;


    @Override
    public void draw() {
        if(showing) {
            GraphicsContext gc = Main.getGc();
            double area = 0;
            double stepSize = Math.abs(lowerBound - upperBound) / numSteps;

            for(double d = lowerBound; d < upperBound; d += stepSize ) {

                double pixHeight = Graph.getPixelSpace(0, getValue(d, stepSize))[1];
                double height = getValue(d, stepSize);

                double leftCornerY = Graph.getPixelSpace(d, 0)[1];
                double leftCornerX = Graph.getPixelSpace(d, 0)[0];
                double rightCornerY = Graph.getPixelSpace(d + stepSize, 0)[0];
                gc.rect(leftCornerX, leftCornerY, rightCornerY - d, pixHeight );
            }

        }
    }

    /**
     * A method to get the value of a function at an interval, value depends on mode of riemann sun selected
     * temporarily using left hand for all
     */
    private double getValue(double xPos, double stepSize) {
        return 0;
    }
}
