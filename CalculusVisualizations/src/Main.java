import Tokens.*;
import Tokens.Operators.Divide;
import Tokens.Operators.Exponentiate;
import Tokens.Operators.Plus;
import Tokens.Operators.Times;
import com.sun.tools.hat.internal.model.HackJavaValue;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import sun.tools.jconsole.inspector.XObject;

import java.util.ArrayList;
import java.util.List;

public class Main extends Application {
    private Stage stage;
    private Canvas canvas;
    private static GraphicsContext gc;
    private BorderPane p;
    private Scene graphicsScene;
    private Button tsRender;
    private TextField textField;
    private Slider drvApproxLoc;
    private Slider tsApproxLoc;

    //Window size tracking, b prefix indicates that it is the base value
    private static double MIN_X = 0;
    private static double MAX_X = 0;
    private static double MIN_Y = 0;
    private static double MAX_Y = 0;
    private final static double bMIN_X = -3.14;
    private final static double bMAX_X = 3.14;
    private final static double bMIN_Y = -1.1;
    private final static double bMAX_Y = 1.1;

    private final static int XRES = 200;
    private final static int YRES = 400;

    private final static int width = (int) ( Math.abs(bMAX_X - bMIN_X) * XRES );
    private final static int height = (int)( Math.abs(bMAX_Y - bMIN_Y) * YRES );

    private double deltaXCoord = 0;
    private double deltaYCoord = 0;

    private double MouseXPos = 0;
    private double MouseYPos = 0;

    private double xOffset = 0;
    private double yOffset = 0;

    private static double zoomTransform = 1;
    private boolean isMouseDragging = false;

    private Function mainFunction = new Function("sin(x)");

    //create visualizers
    private SecantLine secantDrawer = new SecantLine(0, 2, mainFunction);
    private DerivativeGraph derivativeGraphDrawer = new DerivativeGraph( 1, mainFunction);
    private TaylorSeries taylorSeriesDrawer = new TaylorSeries(1, 0, mainFunction);
    private RiemannSum riemannSumDrawer = new RiemannSum();

    public void start(Stage primaryStage) {
        //init
        stage = primaryStage;
        stage.setTitle("Taylor Series");
        resetWindow();

        //create UI nodes
        canvas = createCanvas();

        textField = new TextField();
        textField.setPromptText("Type your function here");
        textField.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if(event.getCode().toString().equals("ENTER")) {
                    System.out.println(textField.getText());
                    mainFunction = new Function(textField.getText());
                    gc.clearRect(0, 0, 10000, 10000);
                    renderAxis();
                    renderFunction(mainFunction, Color.BLUE);
                    textField.clear();
                    secantDrawer.setFunction(mainFunction);
                    derivativeGraphDrawer.setFunction(mainFunction);
                    taylorSeriesDrawer.setFunction(mainFunction);
                }
            }
        });

        //create
        TabPane toolTab = new TabPane();
        toolTab.setSide(Side.TOP);

        //create ts tab
        toolTab.getTabs().add(createTsTab());

        //create drv tab
        toolTab.getTabs().add(createDrvTab());

        //create integral tab
        toolTab.getTabs().add(createIntegralTab());


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
        updateRender();

    }

    private Tab createTsTab() {
        Tab taylorSeriesTab = new Tab("Taylor Series");
        GridPane tsGridPane = new GridPane();
        tsGridPane.setVgap(20);

        CheckBox taylorCheckBox = new CheckBox("Render Taylor Polynomial");
        taylorCheckBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                taylorSeriesDrawer.setShowing(taylorCheckBox.isSelected());
                updateRender();
            }
        });
        tsGridPane.add(taylorCheckBox, 0, 0);

        CheckBox isMaclaurinCheckBox = new CheckBox("Is Maclaurin");
        isMaclaurinCheckBox.fire();
        isMaclaurinCheckBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                taylorSeriesDrawer.setMaclaurin(isMaclaurinCheckBox.isSelected());
                updateRender();
                System.out.println(taylorSeriesDrawer.isMaclaurin());
            }
        });
        tsGridPane.add(isMaclaurinCheckBox, 0, 1);

        GridPane tsOrderPane = new GridPane();
        ComboBox<Integer> orderChooser = new ComboBox(FXCollections.observableArrayList(1, 2, 3, 4));
        orderChooser.setValue(1);
        orderChooser.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                taylorSeriesDrawer.setOrder(orderChooser.getValue());
                updateRender();
            }
        });
        tsOrderPane.add(orderChooser, 0, 0);
        Label tsOrderExp = new Label("   Set Taylor Polynomial order");
        tsOrderPane.add(tsOrderExp, 1, 0);
        tsGridPane.add(tsOrderPane, 0, 2);

        tsApproxLoc = new Slider(bMIN_X, bMAX_X, (bMAX_X + bMIN_X) / 2);
        tsApproxLoc.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if(!taylorSeriesDrawer.isMaclaurin()) {
                    taylorSeriesDrawer.setxPos(tsApproxLoc.getValue());
                    taylorSeriesDrawer.createTaylorPolynomials();
                } else {
                    tsApproxLoc.setValue(0);
                }
                updateRender();
            }
        });
        tsGridPane.add(tsApproxLoc, 0, 3);



        taylorSeriesTab.setContent(tsGridPane);
        tsGridPane.setStyle(" -fx-background-color: indianred;");
        tsGridPane.setPadding(new Insets(20, 10, 10, 10));
        return taylorSeriesTab;
    }

    /**
     * A helper method which creates a tab with all controls for the derivative visualizations
     * @return a Tab full of various controls, with a light green background.
     */
    private Tab createDrvTab() {
        Tab drvTab = new Tab("Derivative");
        GridPane drvGridPane = new GridPane();
        drvGridPane.setVgap(20);

        CheckBox drvCheckBox = new CheckBox("Render the derivative");
        drvCheckBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                derivativeGraphDrawer.setShowing(drvCheckBox.isSelected());
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
                derivativeGraphDrawer.setOrder(orderChooser.getValue());
                updateRender();
            }
        });
        drvOrderPane.add(orderChooser, 0, 0);
        Label drvOrderExp = new Label("   Set derivative order");
        drvOrderPane.add(drvOrderExp, 1, 0);
        drvGridPane.add(drvOrderPane, 0, 1);


        CheckBox secantCheckBox = new CheckBox("Enable secant tool");
        secantCheckBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                secantDrawer.setShowing(secantCheckBox.isSelected());
                updateRender();
            }
        });

        drvApproxLoc = new Slider(bMIN_X, bMAX_X, (bMAX_X + bMIN_X) / 2);
        drvApproxLoc.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                secantDrawer.setxPos(drvApproxLoc.getValue());
                updateRender();
            }
        });

        drvGridPane.add(secantCheckBox, 0 , 2);
        drvGridPane.add(drvApproxLoc, 0, 4);
        drvGridPane.add(new Label("Value of h"), 0, 5);

        Slider hValueSlider = new Slider(0.01, 2, 1);
        hValueSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                secantDrawer.setDeltaX(hValueSlider.getValue());
                updateRender();
            }
        });
        drvGridPane.add(hValueSlider, 0, 6);


        drvGridPane.setStyle(" -fx-background-color: mediumseagreen;");
        drvGridPane.setPadding(new Insets(20, 10, 10, 10));
        drvTab.setContent(drvGridPane);
        return drvTab;
    }


    private Tab createIntegralTab() {
        Tab integralTab = new Tab("Integral");
        GridPane integralGridPane = new GridPane();
        integralGridPane.setVgap(20);

        CheckBox riemannCheckBox = new CheckBox("Render the Riemann Sum");
        riemannCheckBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                riemannSumDrawer.setShowing(riemannCheckBox.isSelected());
                updateRender();
            }
        });

        integralGridPane.add(riemannCheckBox, 0, 0);
        integralGridPane.setStyle(" -fx-background-color: blueviolet;");
        integralGridPane.setPadding(new Insets(20, 10, 10, 10));
        integralTab.setContent(integralGridPane);
        return integralTab;
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
    public static void renderFunction(Function functionToRender, Color renderColor) {
        gc.beginPath();
        for(double d = MIN_X; d < MAX_X; d += (5 / (double) XRES * zoomTransform)){
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

    private void updateRender() {
        gc.clearRect(0, 0, 10000, 10000);

        renderAxis();
        renderFunction(mainFunction, Color.BLUE);
        drvApproxLoc.setMax(MAX_X);
        drvApproxLoc.setMin(MIN_X);
        tsApproxLoc.setMax(MAX_X);
        tsApproxLoc.setMin(MIN_X);
        secantDrawer.draw();
        derivativeGraphDrawer.draw();
        taylorSeriesDrawer.draw();
        System.out.println("Hello");

    }

    public static void drawLine(double xOne, double yOne, double xTwo, double yTwo) {
        double slope = (yTwo - yOne) / (xTwo - xOne);
        String expression = new Double(yOne).toString() + "+" + new Double(slope).toString() + "*(x-" +
                new Double(xOne).toString() + ")";
        renderFunction(new Function(expression), Color.MEDIUMSEAGREEN);
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
