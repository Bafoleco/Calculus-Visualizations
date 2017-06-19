public class Graph {
    //Precondition: x,y within range
    public static int[] getPixelSpace(double x, double y) {
        int[] pixelSpace = new int[2];
        double maxX = TaylorSeries.getMax_X();
        double maxY = TaylorSeries.getMAX_Y();
        double minX = TaylorSeries.getMin_X();
        double minY = TaylorSeries.getMIN_Y();
        int xRes = TaylorSeries.getXRES();
        int yRes = TaylorSeries.getYRES();
        //get relative positions between max and min
        double relativeX = (x - minX) / Math.abs(maxX - minX);

        double relativeY = (y - minY) / Math.abs(maxY - minY);
        //transform
        pixelSpace[0] = (int) (Math.abs(maxX - minX) * xRes * relativeX) - 1;
        System.out.println("maxY = " + maxY + "minY = " + minY + yRes);
        int pixHeight = (int) (Math.abs(maxY - minY) * yRes);
        System.out.println("pixheight = " + pixHeight);
        int yValue = (int) (pixHeight * relativeY);
        pixelSpace[1] = pixHeight - yValue;
        return pixelSpace;

    }



    //@param int x coordinate that corresponds to a column in pixel space
    //@return that column as an x coordinate
    public static double getCoordinateSpace(int x) {
        double minX = TaylorSeries.getMin_X();
        double maxY = TaylorSeries.getMAX_Y();
        int xRes = TaylorSeries.getXRES();
        double graphWidth = Math.abs(maxY - minX);
        int numPixColumns = (int) (graphWidth * xRes);
        double relativeX = (double) x / (double) numPixColumns;
        return minX + graphWidth * relativeX;
    }
}
