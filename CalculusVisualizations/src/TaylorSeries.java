import Tokens.*;
import Tokens.Operators.Divide;
import Tokens.Operators.Exponentiate;
import Tokens.Operators.Plus;
import Tokens.Operators.Times;
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
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.Scene;
import java.util.ArrayList;
import java.util.List;

public class TaylorSeries extends Application {

    private Stage stage;
    private static Canvas canvas;
    private GraphicsContext gc;
    private BorderPane p;
    private Scene graphicsScene;
    private Button tsRender;
    private TextField textField;

    //Window size tracking, b prefix indicates that it is the base value
    private static double MIN_X = 0;
    private static double MAX_X = 4;
    private static double MIN_Y = -1;
    private static double MAX_Y = 8;
    private final static double bMIN_X = -3.14;
    private final static double bMAX_X = 3.14;
    private final static double bMIN_Y = -1.1;
    private final static double bMAX_Y = 1.1;

    final static int XRES = 200;
    final static int YRES = 400;

    final static int width = (int) ( Math.abs(bMAX_X - bMIN_X) * XRES );
    final static int height = (int)( Math.abs(bMAX_Y - bMIN_Y) * YRES );

    private double deltaXCoord = 0;
    private double deltaYCoord = 0;

    private int taylorSeriesOrder = 0;
    private double taylorCentered = 0;

    private List<Token> taylorPolynomial = new ArrayList<>();

    double MouseXPos = 0;
    double MouseYPos = 0;

    double xOffset = 0;
    double yOffset = 0;

    double zoomTransform = 1;
    boolean isMouseDragging = false;

    Function mainFunction = new Function("2.718281828^x");
    Function taylorFunction = new Function("10000");

    public void start(Stage primaryStage) {
        //init
        stage = primaryStage;
        stage.setTitle("Taylor Series");
        resetWindow();
        setupCanvas();

        //create buttons and input box
        tsRender = new Button("Render Taylor Series");
        tsRender.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                renderTS();
            }
        });
        textField = new TextField();

        //arrange nodes
        gc = canvas.getGraphicsContext2D();
        p = new BorderPane();
        p.setCenter(canvas);
        GridPane grid = new GridPane();
        grid.add(tsRender, 0, 0);
        grid.add(textField, 1, 0);
        p.setBottom(grid);
        graphicsScene = new Scene(p, width, height + 100);
        //start
        stage.setScene(graphicsScene);
        stage.show();
        renderAxis();
        renderFunction(mainFunction, Color.BLUE);
    }

    private void renderTS() {
        if(taylorSeriesOrder != 0)
            taylorPolynomial.add(new Plus());
        taylorPolynomial.add(new LeftParens());
        taylorPolynomial.add(new Constant(mainFunction.takeDerivative(taylorSeriesOrder, taylorCentered)));
        taylorPolynomial.add(new Divide());
        taylorPolynomial.add(new Constant(factorial(taylorSeriesOrder)));
        taylorPolynomial.add(new RightParens());
        if(taylorSeriesOrder != 0) {
            taylorPolynomial.add(new Times());
            taylorPolynomial.add(new LeftParens());
            taylorPolynomial.add(new Variable());
            taylorPolynomial.add(new Exponentiate());
            taylorPolynomial.add(new Constant(taylorSeriesOrder));
            taylorPolynomial.add(new RightParens());

        }
        taylorSeriesOrder++;
        taylorFunction = new Function(taylorPolynomial);
        gc.clearRect(0, 0, 10000, 10000);
        renderAxis();
        renderFunction(mainFunction, Color.BLUE);
        renderFunction(taylorFunction, Color.RED);
    }

    /**
     * A method which resets the variables representing the portion of the graph rendered to their base value
     */
    private void resetWindow() {
        MIN_X = bMIN_X;
        MIN_Y = bMIN_Y;
        MAX_X = bMAX_X;
        MAX_Y = bMAX_Y;
    }

    /**
     * A helper method to create the canvas and its event handlers
     */
    private void setupCanvas() {

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
                deltaXCoord = (deltaXPix / XRES) * zoomTransform;
                deltaYCoord = (deltaYPix / YRES) * zoomTransform;
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
                renderFunction(taylorFunction, Color.RED);
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
                if(yScroll < 0) {
                    zoomTransform *= 1.1;
                } else if(yScroll > 0) {
                    zoomTransform *=.9;
                }
                double xDiff = Math.abs((bMIN_X - xOffset) - (bMAX_X  - xOffset));
                double newXDiff = zoomTransform * xDiff;
                double movNecessaryToAchieveX = (newXDiff - xDiff) / 2;
                MIN_X = (bMIN_X - xOffset) - movNecessaryToAchieveX;
                MAX_X = (bMAX_X  - xOffset) + movNecessaryToAchieveX;

                double yDiff = Math.abs((bMIN_Y - yOffset) - (bMAX_Y  - yOffset));
                double newYDiff = zoomTransform * yDiff;
                double mobNecessaryToAchieveY = (newYDiff - yDiff) / 2;
                MIN_Y = (bMIN_Y  + yOffset) - mobNecessaryToAchieveY;
                MAX_Y = (bMAX_Y  + yOffset) + mobNecessaryToAchieveY;
                gc.clearRect(0, 0, 10000, 10000);
                renderAxis();
                renderFunction(mainFunction, Color.BLUE);
                renderFunction(taylorFunction, Color.RED);
                isMouseDragging = false;

            }
        });
    }

    /**
     * A method to render the main function
     * @param functionToRender the function object to be rendered
     * @param renderColor the color with which the function shall be rendered
     */
    private void renderFunction(Function functionToRender, Color renderColor) {
        long startTime = System.currentTimeMillis();
        gc.beginPath();
        for(double d = MIN_X; d < MAX_X; d += (10 / (double) XRES * zoomTransform)){
            double yValue = Graph.getPixelSpace(d, functionToRender.computeFunc(d))[1];
            double xValue = Graph.getPixelSpace(d, functionToRender.computeFunc(d))[0];
            gc.lineTo(xValue, yValue);
        }
        gc.setStroke(renderColor);
        gc.stroke();
        long endTime = System.currentTimeMillis();
        System.out.println("Render time was: " + (endTime - startTime) + " milliseconds");
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

    public static int factorial(int x) {
        if(x == 0){
            return 1;
        }
        else if(x == 1) {
            return 1;
        }
        else if(x > 1) {
            return x * factorial(x-1);
        }
        else {
            return -1;
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

    public static void main(String[] args) {
     launch(args);
     Function function = new Function("sin(x)");
     for(int i = 0; i <= 10; i++) {
         System.out.println(i + "th derivative = " + function.takeDerivative(i, 0));
     }

    }
}
