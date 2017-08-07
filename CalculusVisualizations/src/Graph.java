/**
 * A class of static methods designed to handle the necessary conversions between the worlds of pixels and \
 * the worlds of coordinates
 * @author Bay Foley-Cox
 */
public class Graph {

    /**
     * Takes a point in coordinate space and finds the corresponding location in pixel space
     * @param x the x-value of the coordinate
     * @param y the y-value of the coordinate
     * @return an length 2 array of integers representing the points coordinates in pixel space. The 0 index represents
     * X, the 1 index represents Y.
     */
    public static double[] getPixelSpace(double x, double y) {
        double[] pixelSpace = new double[2];
        double maxX = Main.getMax_X();
        double maxY = Main.getMAX_Y();
        double minX = Main.getMin_X();
        double minY = Main.getMIN_Y();
        int xRes = Main.getXRES();
        int yRes = Main.getYRES();
        //get relative positions between max and min
        double relativeX = (x - minX) / Math.abs(maxX - minX);
        double relativeY = (y - minY) / Math.abs(maxY - minY);
        //transform
        pixelSpace[0] = (Main.getWidth() * relativeX);
        int pixHeight = (int) (Main.getHeight());
        double yValue = (pixHeight * relativeY);
        pixelSpace[1] = pixHeight - yValue;
        return pixelSpace;
    }

    public static double getLengthY(double y) {
        double minY = Main.getMIN_Y();
        double maxY = Main.getMAX_Y();
        int yRes = Main.getYRES();
        //get relative positions between max and min
        double relativeY = y / Math.abs(maxY - minY);
        double yValue = (Main.getHeight() * relativeY);
        return yValue;
    }

    public static double getLengthX(double x) {
        double minX = Main.getMin_X();
        double maxX = Main.getMax_X();
        int xRes = Main.getXRES();
        //get relative positions between max and min
        double relativeX = x / Math.abs(maxX - minX);
        double xValue = (Main.getWidth() * relativeX);
        return xValue;
    }

    /**@param x coordinate that corresponds to a column in pixel space
     * @return that column as an x coordinate in coordinate space
     */
    public static double getCoordinateSpace(int x) {
        double minX = Main.getMin_X();
        double maxY = Main.getMAX_Y();
        int xRes = Main.getXRES();
        double graphWidth = Math.abs(maxY - minX);
        int numPixColumns = (int) (graphWidth * xRes);
        double relativeX = (double) x / (double) numPixColumns;
        return minX + graphWidth * relativeX;
    }
}
