import Tokens.*;
import Tokens.Operators.Divide;
import Tokens.Operators.Exponentiate;
import Tokens.Operators.Plus;
import Tokens.Operators.Times;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Side;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.scene.Scene;
import java.util.ArrayList;
import java.util.List;

public class TaylorSeries extends Application {
    private Stage stage;
    private static Canvas canvas;
    private static GraphicsContext gc;
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

    static double zoomTransform = 1;
    boolean isMouseDragging = false;

    int drvOrder = 1;
    boolean showDrv = false;
    boolean showCritPoints = false;

    Function mainFunction = new Function("sin(x)");
    Function taylorFunction = new Function("10000");
    List<Point> critPointList = new ArrayList<>();


    public void start(Stage primaryStage) {
        //init
        stage = primaryStage;
        stage.setTitle("Taylor Series");
        resetWindow();

        //create UI nodes
        canvas = createCanvas();

        tsRender = new Button("Render Taylor Series");
        tsRender.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                renderTS();
            }
        });

        textField = new TextField();
        textField.setPromptText("Type your function here");
        textField.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if(event.getCode().toString().equals("ENTER")) {
                    System.out.println(textField.getText());
                    mainFunction = new Function(textField.getText());
                    taylorPolynomial = new ArrayList<>();
                    taylorFunction = new Function("10000");
                    gc.clearRect(0, 0, 10000, 10000);
                    renderAxis();
                    renderFunction(mainFunction, Color.BLUE);
                    textField.clear();
                }
            }
        });


        //create
        TabPane toolTab = new TabPane();

        //create ts tab
        Tab taylorSeriesTab = new Tab("Taylor Series");
        GridPane tsGridPane = new GridPane();
        tsGridPane.setHgap(10);
        tsGridPane.add(tsRender, 0, 0);
        taylorSeriesTab.setContent(tsGridPane);
        tsGridPane.setStyle(" -fx-background-color: indianred;");
        tsGridPane.setPadding(new Insets(20, 10, 10, 10));

        toolTab.getTabs().add(taylorSeriesTab);
        toolTab.setSide(Side.TOP);

        //create drv tab
        Tab drvTab = new Tab("Derivative");
        GridPane drvGridPane = new GridPane();
        drvGridPane.setVgap(20);

        CheckBox drvCheckBox = new CheckBox("Render the derivative");
        drvCheckBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Hello");
                if(drvCheckBox.isSelected()) {
                    showDrv = true;
                }
                else {
                    showDrv = false;
                }
                updateRender();
            }
        });
        drvGridPane.add(drvCheckBox, 0, 0);

        GridPane drvOrderPane = new GridPane();
        ComboBox<Integer> orderChooser = new ComboBox(FXCollections.observableArrayList(1, 2, 3, 4));
        orderChooser.setValue(1);
        orderChooser.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                drvOrder = orderChooser.getValue();
                System.out.println(drvOrder);
                updateRender();
            }
        });
        drvOrderPane.add(orderChooser, 0, 0);
        Label drvOrderExp = new Label("   Set derivative order");
        drvOrderPane.add(drvOrderExp, 1, 0);
        drvGridPane.add(drvOrderPane, 0, 1);

        CheckBox criticalCheckBox = new CheckBox("Show critical points");
        drvCheckBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Hello");
                if(drvCheckBox.isSelected()) {
                    showDrv = true;
                }
                else {
                    showDrv = false;
                }
                updateRender();
            }
        });



        drvTab.setContent(drvGridPane);
        drvGridPane.setStyle(" -fx-background-color: mediumseagreen;");
        drvGridPane.setPadding(new Insets(20, 10, 10, 10));
        toolTab.getTabs().add(drvTab);




        //arrange nodes
        gc = canvas.getGraphicsContext2D();
        p = new BorderPane();
        p.setCenter(canvas);
        GridPane grid = new GridPane();
        grid.add(textField, 1, 0);
        textField.setMinWidth(1500);
        p.setTop(grid);
        p.setLeft(toolTab);

        graphicsScene = new Scene(p, width, height + 100);
        //start
        stage.setScene(graphicsScene);
        stage.show();
        critPointList.add(new Point(0, 1, Color.LIGHTBLUE));
        updateRender();

    }

    /**
     * A method, called whenever the Render Taylor Series button is pressed by the user, that sets generates a list of
     * tokens representing a Taylor Series of the appropriate order and uses that list to set the taylorFunction
     */
    private void renderTS() {
        if(taylorSeriesOrder != 0)
            taylorPolynomial.add(new Plus());
        taylorPolynomial.add(new LeftParens());
        taylorPolynomial.add(new Constant(mainFunction.takeDerivative(taylorSeriesOrder, taylorCentered)));
        taylorPolynomial.add(new Divide());
        taylorPolynomial.add(new Constant(Function.factorial(taylorSeriesOrder)));
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
        updateRender();
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
    private Canvas createCanvas() {

        Canvas newCanvas = new Canvas(width, height);
        newCanvas.addEventHandler(MouseEvent.MOUSE_MOVED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(!isMouseDragging) {
                    MouseXPos = event.getX();
                    MouseYPos = event.getY();
                }
            }
        });
        newCanvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, new EventHandler<MouseEvent>() {
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
                updateRender();
                isMouseDragging = false;
            }
        });
        newCanvas.addEventHandler(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                xOffset += deltaXCoord;
                yOffset += deltaYCoord;
                deltaXCoord = 0;
                deltaYCoord = 0;
            }
        });

        newCanvas.addEventHandler(ScrollEvent.SCROLL, new EventHandler<ScrollEvent>() {
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
                updateRender();
                isMouseDragging = false;

            }
        });
        return  newCanvas;
    }

    /**
     * A method to render the main function
     * @param functionToRender the function object to be rendered
     * @param renderColor the color with which the function shall be rendered
     */
    private void renderFunction(Function functionToRender, Color renderColor) {
        long startTime = System.currentTimeMillis();
        gc.beginPath();
        for(double d = MIN_X; d < MAX_X; d += (5 / (double) XRES * zoomTransform)){
            double yValue = Graph.getPixelSpace(d, functionToRender.computeFunc(d))[1];
            double xValue = Graph.getPixelSpace(d, functionToRender.computeFunc(d))[0];
            gc.lineTo(xValue, yValue);
        }
        gc.setStroke(renderColor);
        gc.stroke();
//        long endTime = System.currentTimeMillis();
//        System.out.println("Render time was: " + (endTime - startTime) + " milliseconds");
    }

    private void renderDerivative(int order) {
        gc.beginPath();
        for(double d = MIN_X; d < MAX_X; d += (5 / (double) XRES * zoomTransform)){
            double yValue = Graph.getPixelSpace(d, mainFunction.takeDerivative(order, d))[1];
            double xValue = Graph.getPixelSpace(d, 5)[0];
            gc.lineTo(xValue, yValue);
        }
        gc.setStroke(Color.MEDIUMSEAGREEN);
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

    private void updateRender() {
        gc.clearRect(0, 0, 10000, 10000);
        renderAxis();
        renderFunction(mainFunction, Color.BLUE);
        renderFunction(taylorFunction, Color.INDIANRED);
        if(showDrv) {
            System.out.println(drvOrder);
            renderDerivative(drvOrder);
        }
        for(Point p : critPointList) {
            p.drawPoint();
        }
    }

    public static double getZoomTransform() {
        return zoomTransform;
    }

    public static GraphicsContext getGc() {
        return gc;
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
    }
}
