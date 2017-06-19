import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.scene.Scene;

public class TaylorSeries extends Application {
    private Stage stage;

    private static Canvas canvas;
    private GraphicsContext gc;
    private BorderPane p;
    private Scene graphicsScene;
    private Button tsRender;
    private TextField textField;


    //Window size tracking
    // b means that the succeeding value is the base value
    static double MIN_X = -3.14;
    static double MAX_X = 3.14;
    static double MIN_Y = -1.1;
    static double MAX_Y = 1.1;
    static double bMIN_X = -3.14;
    static double bMAX_X = 3.14;
    static double bMIN_Y = -1.1;
    static double bMAX_Y = 1.1;

    final static int XRES = 200;
    final static int YRES = 400;

    final static int width = (int) ( Math.abs(MAX_X - MIN_X) * XRES );
    final static int height = (int)( Math.abs(MAX_Y - MIN_Y) * YRES );

    double deltaXCoord = 0;
    double deltaYCoord = 0;


    double MouseXPos = 0;
    double MouseYPos = 0;

    double xOffset = 0;
    double yOffset = 0;

    double zoomTransform = 1;
    boolean isMouseDragging = false;

    Function mainFunction = new Function("sin(x)");

    public void start(Stage primaryStage) {
        //init
        stage = primaryStage;
        stage.setTitle("Taylor Series");
        //create canvas
        canvas = new Canvas(width, height);

        canvas.addEventHandler(MouseEvent.MOUSE_MOVED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(!isMouseDragging) {
                    MouseXPos = event.getX();
                    MouseYPos = event.getY();
                }
            }
        });
        canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                isMouseDragging = true;
                double deltaXPix = event.getX() - MouseXPos;
                double deltaYPix = event.getY() - MouseYPos;
                deltaXCoord = (deltaXPix / XRES);
                deltaYCoord = (deltaYPix / YRES);
                double xDiff = Math.abs((bMIN_X - xOffset) - (bMAX_X  - xOffset));
                double newXDiff = zoomTransform * xDiff;
                double movNeccesaryToAcheiveX = (newXDiff - xDiff) / 2;
                MIN_X = (bMIN_X - deltaXCoord - xOffset) - movNeccesaryToAcheiveX;
                MAX_X = (bMAX_X  - deltaXCoord - xOffset) + movNeccesaryToAcheiveX;
                double yDiff = Math.abs((bMIN_Y - yOffset) - (bMAX_Y  - yOffset));
                double newYDiff = zoomTransform * yDiff;
                double movNeccesaryToAcheiveY = (newYDiff - yDiff) / 2;
                MIN_Y = (bMIN_Y  + yOffset + deltaYCoord) - movNeccesaryToAcheiveY;
                MAX_Y = (bMAX_Y  + yOffset + deltaYCoord) + movNeccesaryToAcheiveY;
                gc.clearRect(0, 0, 10000, 10000);
                renderAxis();
                renderFunction(mainFunction, Color.BLUE);
                isMouseDragging = false;

            }
        });
        canvas.addEventHandler(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                xOffset += deltaXCoord;
                yOffset += deltaYCoord;
                deltaXCoord = 0;
                deltaYCoord = 0;
            }
        });

        canvas.addEventHandler(ScrollEvent.SCROLL, new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent event) {
                double  yScroll = event.getDeltaY();
                System.out.println(zoomTransform);
                if(yScroll < 0) {
                    zoomTransform *= 1.1;
                } else if(yScroll > 0) {
                   zoomTransform *=.9;
                }

                double xDiff = Math.abs((bMIN_X - xOffset) - (bMAX_X  - xOffset));
                double newXDiff = zoomTransform * xDiff;
                double movNeccesaryToAcheiveX = (newXDiff - xDiff) / 2;
                MIN_X = (bMIN_X - xOffset) - movNeccesaryToAcheiveX;
                MAX_X = (bMAX_X  - xOffset) + movNeccesaryToAcheiveX;

                double yDiff = Math.abs((bMIN_Y - yOffset) - (bMAX_Y  - yOffset));
                double newYDiff = zoomTransform * yDiff;
                double movNeccesaryToAcheiveY = (newYDiff - yDiff) / 2;
                MIN_Y = (bMIN_Y  + yOffset) - movNeccesaryToAcheiveY;
                MAX_Y = (bMAX_Y  + yOffset) + movNeccesaryToAcheiveY;

                gc.clearRect(0, 0, 10000, 10000);
                renderAxis();
                renderFunction(mainFunction, Color.BLUE);
                isMouseDragging = false;

            }
        });


        //create buttons and input box
        tsRender = new Button("Render Taylor Series");
        tsRender.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                renderTS();
            }
        });
        textField = new TextField();

        gc = canvas.getGraphicsContext2D();
        p = new BorderPane();
        p.setCenter(canvas);
        GridPane grid = new GridPane();
        grid.add(tsRender, 0, 0);
        grid.add(textField, 1, 0);
        p.setBottom(grid);

        graphicsScene = new Scene(p, width, height + 100);
        stage.setScene(graphicsScene);
        stage.show();

        renderAxis();
        renderFunction(mainFunction, Color.BLUE);


    }

    private void renderTS() {
    }

    /**
     * A method to render the main function
     * @param functionToRender the function object to be rendered
     * @param renderColor the color with which the function shall be rendered
     */
    private void renderFunction(Function functionToRender, Color renderColor) {
        gc.beginPath();
        for(double d = MIN_X; d < MAX_X; d += (5 / (double) XRES)){
            double yValue = Graph.getPixelSpace(d, functionToRender.computeFunc(d))[1];
            double xValue = Graph.getPixelSpace(d, functionToRender.computeFunc(d))[0];
            gc.lineTo(xValue, yValue);
        }
        gc.setStroke(renderColor);
        gc.stroke();
    }

    /**
     * A method to render the graphs axis and labels
     */
    private void renderAxis() {
        for(int i = 0; i < getWidth(); i++) {
            double yValue = Graph.getPixelSpace(0, 0)[1];
            gc.setFill(Color.BLACK);
            gc.fillRect(i, yValue, 1, 1);

        }

        //Draw Y Axis
        for(int i = 0; i < canvas.getHeight(); i++) {
            gc.fillRect(Graph.getPixelSpace(0, 1)[0], i, 1, 1);
        }
    }


    public static double getMIN_Y() {
        return MIN_Y;
    }

    public static double getMAX_Y() {
        return MAX_Y;
    }

    public static int getXRES() {
        return XRES;
    }

    public static int getYRES() {
        return YRES;
    }

    public static double getMin_X() {
        return MIN_X;
    }

    public static double getMax_X() {
        return MAX_X;
    }

    public static int getWidth() {
        return width;
    }

    public static int getHeight() {
        return height;
    }
}
