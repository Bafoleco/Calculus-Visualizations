package main;

import com.sun.xml.internal.ws.api.model.wsdl.WSDLBoundOperation;
import javafx.scene.layout.Background;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.*;
import tokens.*;
import tokens.operators.Minus;
import tokens.operators.Plus;
import tokens.operators.Times;
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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.Scene;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;

public class Main extends Application {
    private static Stage stage;
    private static Canvas canvas;
    private static GraphicsContext gc;
    private BorderPane b;
    private Scene graphicsScene;
    private Button tsRender;
    private TextField inputField;
    private TabPane toolTab;
    private static Slider drvApproxLoc;
    private static Slider tsApproxLoc;
    private static Slider lowerBound;
    private static Slider upperBound;
    private static Slider tsErrorLoc;
    private static TextFlow console;
    private static ScrollPane consolePane;
    private Rectangle rightEdge;
    private Rectangle bottomEdge;
    private Color[] tabColors = {Color.INDIANRED, Color.MEDIUMSEAGREEN, Color.BLUEVIOLET, Color.GREY};
    //Window size tracking, b prefix indicates that it is the base value
    private static double MIN_X = 0;
    private static double MAX_X = 0;
    private static double MIN_Y = 0;
    private static double MAX_Y = 0;
    private static double bMIN_X = -3.14;
    private static double bMAX_X = 3.14;
    private static double bMIN_Y = -1.1;
    private static double bMAX_Y = 1.1;

    private final static int XRES = 200;
    private final static int YRES = 300;

    private final static int width = (int) (Math.abs(bMAX_X - bMIN_X) * XRES);
    private final static int height = (int) (Math.abs(bMAX_Y - bMIN_Y) * YRES);

    private double deltaXCoord = 0;
    private double deltaYCoord = 0;

    private double MouseXPos = 0;
    private double MouseYPos = 0;

    private double xOffset = 0;
    private double yOffset = 0;

    private static double zoomTransform = 1;
    private boolean isMouseDragging = false;

    //default function creation
    private static Function mainFunction = new Function("sin(x)");

    //settings
    private static double baseLineWeight = 2;
    private static int roundLevel = 3;

    //create visualizers
    private static SecantLine secantDrawer = new SecantLine(0, 2, mainFunction);
    private static DerivativeGraph derivativeGraphDrawer = new DerivativeGraph(1, mainFunction);
    private static TaylorSeries taylorSeriesDrawer = new TaylorSeries(1, 0, mainFunction);
    private static RiemannSum riemannSumDrawer = new RiemannSum(-1.571, 1.571, mainFunction);
    private static CriticalPoint criticalPointDrawer = new CriticalPoint(mainFunction);

    public void start(Stage primaryStage) {
        //init
        stage = primaryStage;
        stage.setTitle("Calculus Visualizer");
        resetWindow();

        //create UI nodes
        canvas = createCanvas();
        inputField = new TextField();
        inputField.setPromptText("Type your function here");
        inputField.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode().toString().equals("ENTER")) {
                    Function newFunction = new Function(inputField.getText());
                    if (newFunction.getExpression().size() > 0) {
                        resetMainFunction(newFunction);
                    } else {
                        writeConsole("ERROR: main.Function.tokenify() was not able to parse entered expression. This "
                                + "indicates the entered expression was invalid.\nPlease try again with a valid expression.\n", true);
                    }
                }
            }
        });

        //create toolTab
        toolTab = new TabPane();
        toolTab.addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                for (int i = 0; i < toolTab.getTabs().size(); i++) {
                    if (toolTab.getSelectionModel().isSelected(i)) {
                        updateUIColor(tabColors[i]);
                    }
                }
            }
        });

        toolTab.setSide(Side.TOP);
        //create ts tab
        toolTab.getTabs().add(createTsTab());
        //create drv tab
        toolTab.getTabs().add(createDrvTab());
        //create integral tab
        toolTab.getTabs().add(createIntegralTab());

        toolTab.getTabs().add(createSettingsTab());

        //create console
        console = new TextFlow();
        console.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                consolePane.setVvalue(consolePane.getVmax());

            }
        });
        consolePane = new ScrollPane();
        consolePane.setMinHeight(130);
        consolePane.setMaxHeight(130);
        consolePane.setContent(console);
        consolePane.setStyle("-fx-border-color: black;");
        consolePane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        consolePane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        rightEdge = new Rectangle(28, 1, Color.INDIANRED);
        bottomEdge = new Rectangle(1, 50, Color.INDIANRED);

        GridPane canvasPane = new GridPane();
        canvasPane.add(canvas, 0, 0);
        canvasPane.add(consolePane, 0, 1);
        //arrange nodes
        gc = canvas.getGraphicsContext2D();
        gc.setLineWidth(baseLineWeight);
        b = new BorderPane();
        b.setTop(inputField);
        b.setLeft(toolTab);
        b.setCenter(canvasPane);
        b.setRight(rightEdge);
        b.setBottom(bottomEdge);
        rightEdge.minWidth(50);

        graphicsScene = new Scene(b);
        graphicsScene.getStylesheets().add("main/Stylesheet.css");
        //start
        stage.setScene(graphicsScene);
        stage.setMinHeight(600);
        stage.setMinWidth(1200);
        stage.show();
        attachChangeListeners();
        updateRender();
        updateUISize();
    }

    /**
     * A helper method which creates a tab with all the controls for visualization of the Taylor and Maclaurin series
     *
     * @return a Tab full of various controls with a light red background
     */
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
                taylorSeriesDrawer.setxPos(0);
                tsApproxLoc.setValue(0);
                updateRender();
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
                if (!taylorSeriesDrawer.isMaclaurin()) {
                    taylorSeriesDrawer.setxPos(tsApproxLoc.getValue());
                } else {
                    tsApproxLoc.setValue(0);
                }
                updateRender();
            }
        });
        tsGridPane.add(tsApproxLoc, 0, 3);

        CheckBox enableErrorVis = new CheckBox("Enable error information");
        enableErrorVis.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                taylorSeriesDrawer.setErrorVisOn(enableErrorVis.isSelected());
                updateRender();
            }
        });
        tsGridPane.add(enableErrorVis, 0, 4);

        tsErrorLoc = new Slider(bMIN_X, bMAX_X, (bMAX_X + bMIN_X) / 2);
        tsErrorLoc.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                taylorSeriesDrawer.setTsErrorLoc(tsErrorLoc.getValue());
                updateRender();
            }
        });
        tsGridPane.add(tsErrorLoc, 0, 5);

        tsGridPane.setStyle(" -fx-background-color: indianred;");
        tsGridPane.setPadding(new Insets(20, 10, 10, 10));

        taylorSeriesTab.setContent(tsGridPane);
        taylorSeriesTab.setClosable(false);

        return taylorSeriesTab;
    }

    /**
     * A helper method which creates a tab with all controls for the derivative visualizations
     *
     * @return a Tab full of various controls, with a light green background.
     */
    private Tab createDrvTab() {
        Tab drvTab = new Tab("Derivative");
        GridPane drvGridPane = new GridPane();
        drvGridPane.setVgap(20);
        CheckBox critCheckBox = new CheckBox("Render Critical Points");

        CheckBox drvCheckBox = new CheckBox("Render the derivative");
        drvCheckBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                derivativeGraphDrawer.setShowing(drvCheckBox.isSelected());
                if(!derivativeGraphDrawer.showing &&criticalPointDrawer.showing) {
                    critCheckBox.fire();
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

        drvGridPane.add(secantCheckBox, 0, 2);
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


        critCheckBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(derivativeGraphDrawer.showing) {
                    criticalPointDrawer.setShowing(critCheckBox.isSelected());
                    updateRender();
                } else if(criticalPointDrawer.showing) {
                    criticalPointDrawer.setShowing(critCheckBox.isSelected());
                    updateRender();
                    writeConsole("ERROR: the derivative graph must be enabled for critical points to be drawn", true);

                }
                else {
                    critCheckBox.selectedProperty().setValue(false);
                    writeConsole("ERROR: the derivative graph must be enabled for critical points to be drawn", true);
                }
            }
        });

        drvGridPane.add(critCheckBox, 0, 7);

        drvGridPane.setStyle(" -fx-background-color: mediumseagreen;");
        drvGridPane.setPadding(new Insets(20, 10, 10, 10));
        drvTab.setContent(drvGridPane);
        drvTab.setClosable(false);
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
                writeConsole("You have entered the Riemann sum visualization", false);
                updateRender();
            }
        });

        //TODO
        //prevent upper and lower bounds from crossing
        //upper and lower bounds slider
        lowerBound = new Slider(bMIN_X, bMAX_X, -1.571);
        lowerBound.setTooltip(new Tooltip("Controls the lower bound of integration"));
        lowerBound.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                riemannSumDrawer.setLowerBound(lowerBound.getValue());
                updateRender();
            }
        });
        upperBound = new Slider(bMIN_X, bMAX_X, 1.571);
        upperBound.setTooltip(new Tooltip("Controls the upper bound of integration"));
        upperBound.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                riemannSumDrawer.setUpperBound(upperBound.getValue());
                updateRender();
            }
        });
        integralGridPane.add(lowerBound, 0, 1);
        integralGridPane.add(upperBound, 0, 2);

        Slider numSteps = new Slider(1, 64, 1);
        numSteps.setShowTickLabels(true);
        numSteps.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                riemannSumDrawer.setNumSteps((int) numSteps.getValue());
                numSteps.setValue((int) numSteps.getValue());
                updateRender();
            }
        });
        integralGridPane.add(numSteps, 0, 3);
        //create UI to allow for choice of summation mode
        GridPane riemannOrderPane = new GridPane();
        ComboBox<String> orderChooser = new ComboBox(FXCollections.observableArrayList("1: Left Side",
                "2: Right Side", "3: Minimum", "4: Maximum", "5: Middle"));
        orderChooser.setValue("1: Left Side");
        orderChooser.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                riemannSumDrawer.setMode(Integer.parseInt(orderChooser.getValue().substring(0, 1)));
                updateRender();
            }
        });
        riemannOrderPane.add(orderChooser, 0, 0);
        Label riemmanTypeExp = new Label("   Set Riemann type");
        riemannOrderPane.add(riemmanTypeExp, 1, 0);
        integralGridPane.add(riemannOrderPane, 0, 4);

        integralGridPane.add(riemannCheckBox, 0, 0);
        integralGridPane.setStyle(" -fx-background-color: blueviolet;");
        integralGridPane.setPadding(new Insets(20, 10, 10, 10));
        integralTab.setContent(integralGridPane);
        integralTab.setClosable(false);
        return integralTab;
    }

    /**
     * A helper method to create the tab holding controls for program settings
     * @return the tab of UI controls
     */
    private Tab createSettingsTab() {
        Tab settingsTab = new Tab("Settings");
        GridPane settingsGridPane = new GridPane();
        settingsGridPane.setVgap(20);

        Label setLineWt = new Label("Enter desired line wt.");
        TextField inputField = new TextField();
        inputField.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode().toString().equals("ENTER")) {
                    double inputValue = Double.parseDouble(inputField.getText());
                    if (inputValue >= 0.5 && inputValue <= 8) {
                        baseLineWeight = Double.parseDouble(inputField.getText());
                    }
                    //11:50
                    else
                        writeConsole("ERROR: entered wt. not within accepted range 0.5-8\n", true);
                    updateRender();
                }
            }
        });
        GridPane wtPane = new GridPane();
        wtPane.add(inputField, 0, 0);
        wtPane.add(setLineWt, 1, 0);
        wtPane.setHgap(10);
        settingsGridPane.add(wtPane, 0, 0);
        settingsGridPane.add(new Label("Set UI colors:"), 0,1 );
        Label setTSColor = new Label("Taylor Series");
        ColorPicker tsColorPicker = new ColorPicker();
        tsColorPicker.setValue(Color.INDIANRED);
        tsColorPicker.valueProperty().addListener(new ChangeListener<Color>() {
            @Override
            public void changed(ObservableValue<? extends Color> observable, Color oldValue, Color newValue) {
                tabColors[0] = newValue;
                updateTabColors();
                taylorSeriesDrawer.setMainColor(newValue);
            }
        });
        GridPane tsColorPane = new GridPane();
        tsColorPane.add(tsColorPicker, 0, 0);
        tsColorPane.add(setTSColor, 1, 0);
        tsColorPane.setHgap(10);
        settingsGridPane.add(tsColorPane, 0, 2);
        Label setDrvColor = new Label("Derivative visualiser");
        ColorPicker drvColorPicker = new ColorPicker();
        drvColorPicker.setValue(Color.MEDIUMSEAGREEN);
        drvColorPicker.valueProperty().addListener(new ChangeListener<Color>() {
            @Override
            public void changed(ObservableValue<? extends Color> observable, Color oldValue, Color newValue) {
                tabColors[1] = newValue;
                updateTabColors();
                derivativeGraphDrawer.setMainColor(newValue);
                secantDrawer.setMainColor(newValue);
            }
        });
        GridPane drvColorPane = new GridPane();
        drvColorPane.add(drvColorPicker, 0, 0);
        drvColorPane.add(setDrvColor, 1, 0);
        drvColorPane.setHgap(10);
        settingsGridPane.add(drvColorPane, 0, 3);

        Label setIntColor = new Label("Riemann Sum Visualiser");
        ColorPicker intColorPicker = new ColorPicker();
        intColorPicker.setValue(Color.BLUEVIOLET);
        intColorPicker.valueProperty().addListener(new ChangeListener<Color>() {
            @Override
            public void changed(ObservableValue<? extends Color> observable, Color oldValue, Color newValue) {
                tabColors[2] = newValue;
                updateTabColors();
                riemannSumDrawer.setMainColor(newValue);
            }
        });
        GridPane intColorPane = new GridPane();
        intColorPane.add(intColorPicker, 0, 0);
        intColorPane.add(setIntColor, 1, 0);
        intColorPane.setHgap(10);
        settingsGridPane.add(intColorPane, 0, 4);

        Label setSetColor = new Label("Settings Menu");
        ColorPicker setColorPicker = new ColorPicker();
        setColorPicker.setValue(Color.GREY);
        setColorPicker.valueProperty().addListener(new ChangeListener<Color>() {
            @Override
            public void changed(ObservableValue<? extends Color> observable, Color oldValue, Color newValue) {
                tabColors[3] = newValue;
                updateTabColors();
                updateUIColor(newValue);
            }
        });
        GridPane setColorPane = new GridPane();
        setColorPane.add(setColorPicker, 0, 0);
        setColorPane.add(setSetColor, 1, 0);
        setColorPane.setHgap(10);
        settingsGridPane.add(setColorPane, 0, 5);
        Button resetWindowButton = new Button("Reset Window");
        resetWindowButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                resetWindow();
                updateRender();
            }
        });
        Button originToCenter = new Button("Set Origin to Center");
        originToCenter.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                xOffset = 0;
                yOffset = 0;
                double xDiff = Math.abs(bMIN_X - bMAX_X);
                double newXDiff = zoomTransform * xDiff;
                double movNecessaryToAchieveX = (newXDiff - xDiff) / 2;
                MIN_X = bMIN_X  - movNecessaryToAchieveX;
                MAX_X = bMAX_X + movNecessaryToAchieveX;
                double yDiff = Math.abs(bMIN_Y - bMAX_Y);
                double newYDiff = zoomTransform * yDiff;
                double mobNecessaryToAchieveY = (newYDiff - yDiff) / 2;
                MIN_Y = bMIN_Y - mobNecessaryToAchieveY;
                MAX_Y = bMAX_Y + mobNecessaryToAchieveY;
                updateRender();
            }
        });
        settingsGridPane.add(resetWindowButton, 0, 6);
        settingsGridPane.add(originToCenter, 0, 7);
        settingsGridPane.setPadding(new Insets(20, 10, 10, 10));
        settingsGridPane.setStyle("-fx-background-color: grey;");
        settingsTab.setContent(settingsGridPane);
        settingsTab.setClosable(false);
        return settingsTab;
    }

    /**
     * A method which resets the variables representing the portion of the graph rendered to their base value and
     * resets the transformations applied to the window.
     */
    private void resetWindow() {
        MIN_X = bMIN_X;
        MIN_Y = bMIN_Y;
        MAX_X = bMAX_X;
        MAX_Y = bMAX_Y;
        zoomTransform = 1;
        xOffset = 0;
        yOffset = 0;
    }

    /**
     * A helper method to create the canvas and its event handlers
     */
    private Canvas createCanvas() {

        Canvas newCanvas = new Canvas(width, height) {
            public boolean isResizable() {
                return true;
            }
        };
        newCanvas.widthProperty();

        newCanvas.addEventHandler(MouseEvent.MOUSE_MOVED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (!isMouseDragging) {
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
                double xDiff = Math.abs((bMIN_X - xOffset) - (bMAX_X - xOffset));
                double newXDiff = zoomTransform * xDiff;
                double movNeccesaryToAcheiveX = (newXDiff - xDiff) / 2;
                MIN_X = (bMIN_X - deltaXCoord - xOffset) - movNeccesaryToAcheiveX;
                MAX_X = (bMAX_X - deltaXCoord - xOffset) + movNeccesaryToAcheiveX;
                double yDiff = Math.abs((bMIN_Y - yOffset) - (bMAX_Y - yOffset));
                double newYDiff = zoomTransform * yDiff;
                double movNeccesaryToAcheiveY = (newYDiff - yDiff) / 2;
                MIN_Y = (bMIN_Y + yOffset + deltaYCoord) - movNeccesaryToAcheiveY;
                MAX_Y = (bMAX_Y + yOffset + deltaYCoord) + movNeccesaryToAcheiveY;
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
                double yScroll = event.getDeltaY();
                if (yScroll < 0) {
                    zoomTransform *= 1.1;
                } else if (yScroll > 0) {
                    zoomTransform *= .9;
                }
                double xDiff = Math.abs((bMIN_X - xOffset) - (bMAX_X - xOffset));
                double newXDiff = zoomTransform * xDiff;
                double movNecessaryToAchieveX = (newXDiff - xDiff) / 2;
                MIN_X = (bMIN_X - xOffset) - movNecessaryToAchieveX;
                MAX_X = (bMAX_X - xOffset) + movNecessaryToAchieveX;

                double yDiff = Math.abs((bMIN_Y - yOffset) - (bMAX_Y - yOffset));
                double newYDiff = zoomTransform * yDiff;
                double mobNecessaryToAchieveY = (newYDiff - yDiff) / 2;
                MIN_Y = (bMIN_Y + yOffset) - mobNecessaryToAchieveY;
                MAX_Y = (bMAX_Y + yOffset) + mobNecessaryToAchieveY;
                updateRender();
                isMouseDragging = false;

            }
        });
        return newCanvas;
    }

    /**
     * A method to render the main function
     *
     * @param functionToRender the function object to be rendered
     * @param renderColor      the color with which the function shall be rendered
     */
    public static void renderFunction(Function functionToRender, Color renderColor, boolean safeRender) {
        if(safeRender) {
            gc.beginPath();
            gc.setLineWidth(baseLineWeight);
            boolean isNaN = true;
            boolean willBeNaN = false;
            double delta = 1 / (double) XRES * zoomTransform;
            if (renderColor.equals(Color.INDIANRED)) {
                delta *= 10;
            }
            for (double d = MIN_X; d < MAX_X; d += delta) {
                double yValue = Graph.getPixelSpace(d, functionToRender.computeFunc(d))[1];
                double xValue = Graph.getPixelSpace(d, functionToRender.computeFunc(d))[0];
                isNaN = new Double(yValue).isNaN();
                if (isNaN)
                    yValue = 0;
                gc.lineTo(xValue, yValue);
                willBeNaN = new Double(functionToRender.computeFunc(d + delta)).isNaN();
                //if the values produced by the function change from being NaN to not NaN
                if (isNaN != willBeNaN) {
                    if (isNaN = true) {
                        gc.setStroke(Color.TRANSPARENT);
                        gc.stroke();
                        gc.beginPath();
                    } else {
                        gc.setStroke(renderColor);
                        gc.stroke();
                        gc.beginPath();
                    }
                }
            }
            gc.setStroke(renderColor);
            gc.stroke();
        }
        else {
            gc.beginPath();
            gc.setLineWidth(baseLineWeight);
            double delta = 1 / (double) XRES * zoomTransform;
            if (renderColor.equals(Color.INDIANRED)) {
                delta *= 10;
            }
            for (double d = MIN_X; d < MAX_X; d += delta) {
                double yValue = Graph.getPixelSpace(d, functionToRender.computeFunc(d))[1];
                double xValue = Graph.getPixelSpace(d, functionToRender.computeFunc(d))[0];
                gc.lineTo(xValue, yValue);
            }
            gc.setStroke(renderColor);
            gc.stroke();
        }
    }

    /**
     * A method to render the graphs axis
     */
    private static void renderAxis() {
        //Draw X axis
        drawLineSegment(getMin_X(), 0, getMax_X(), 0, Color.BLACK, -1 * baseLineWeight / 3);
        //Draw Y axis
        drawLineSegment(0, getMIN_Y(), 0, getMAX_Y(), Color.BLACK, -1 * baseLineWeight / 3);
    }

    /**
     * Called every time something in the program occurs which changes the image on the canvas, this method first
     * clears the canvas and then re-renders the necessary features and resets variables.
     */
    private static void updateRender() {
        gc.clearRect(0, 0, 10000, 10000);
        riemannSumDrawer.draw();
        renderAxis();
        renderFunction(mainFunction, Color.BLUE, mainFunction.isSafeRender());
        derivativeGraphDrawer.draw();
        taylorSeriesDrawer.draw();
        secantDrawer.draw();
        criticalPointDrawer.draw();
        updateSliders();
        criticalPointDrawer.cullLists();

    }

    /**
     * Updates the mins and maxes of UI sliders to changing window settings.
     */
    private static void updateSliders() {
        drvApproxLoc.setMax(Math.max(MAX_X, drvApproxLoc.getValue()));
        drvApproxLoc.setMin(Math.min(MIN_X, drvApproxLoc.getValue()));
        tsApproxLoc.setMax(Math.max(MAX_X, tsApproxLoc.getValue()));
        tsApproxLoc.setMin(Math.min(MIN_X, tsApproxLoc.getValue()));
        tsErrorLoc.setMax(Math.max(MAX_X, tsErrorLoc.getValue()));
        tsErrorLoc.setMin(Math.min(MIN_X, tsErrorLoc.getValue()));
        lowerBound.setMax(Math.max(MAX_X, lowerBound.getValue()));
        lowerBound.setMin(Math.min(MIN_X, lowerBound.getValue()));
        upperBound.setMax(Math.max(MAX_X, upperBound.getValue()));
        upperBound.setMin(Math.min(MIN_X, upperBound.getValue()));

    }

    /**
     * A method to write text to write text to the program console. Either as red error text or black message text.
     *
     * @param text    the text to be written to the console
     * @param isError true means the text will be rendered as red error text, false means the text will be rendered as
     *                black message text.
     */
    public static void writeConsole(String text, boolean isError) {
        if (isError) {
            Text newErrorText = new Text(text);
            newErrorText.setFill(Color.RED);
            newErrorText.setFont(Font.font("Monospaced", FontPosture.REGULAR, 11.0));
            console.getChildren().add(newErrorText);
        } else {
            Text newText = new Text(text);
            newText.setFill(Color.BLACK);
            newText.setFont(Font.font("Monospaced", FontPosture.REGULAR, 11.0));
            console.getChildren().add(newText);
        }
        Text blankText = new Text(" \n");
        blankText.setFill(Color.BLACK);
        blankText.setFont(Font.font("Monospaced", FontPosture.REGULAR, 11.0));
        console.getChildren().add(blankText);
    }

    /**
     * A function which draws a line between two points.
     *
     * @param xOne x coordinate of point one
     * @param yOne y coordinate of point one
     * @param xTwo x coordinate of point two
     * @param yTwo y coordinate of point two
     * @param color the color of the line
     */
    public static void drawLine(double xOne, double yOne, double xTwo, double yTwo, Color color) {
        double slope = (yTwo - yOne) / (xTwo - xOne);
        List<Token> linearEquation = new ArrayList<>();
        linearEquation.add(new Constant(yOne));
        linearEquation.add(new Plus());
        linearEquation.add(new Constant(slope));
        linearEquation.add(new Times());
        linearEquation.add(new LeftParens());
        linearEquation.add(new Variable());
        linearEquation.add(new Minus());
        linearEquation.add(new Constant(xOne));
        linearEquation.add(new RightParens());
        renderFunction(new Function(linearEquation), color, false);
    }

    /**
     * A function which draws a line segment between two points.
     *
     * @param xOne x coordinate of point one
     * @param yOne y coordinate of point one
     * @param xTwo x coordinate of point two
     * @param yTwo y coordinate of point two
     */
    public static void drawLineSegment(double xOne, double yOne, double xTwo, double yTwo, Color color, double deltaW) {
        gc.setLineWidth(baseLineWeight + deltaW);
        gc.beginPath();
        gc.lineTo(Graph.getPixelSpace(xOne, 0)[0], Graph.getPixelSpace(0, yOne)[1]);
        gc.lineTo(Graph.getPixelSpace(xTwo, 0)[0], Graph.getPixelSpace(0, yTwo)[1]);
        gc.setStroke(color);
        gc.stroke();
    }

    /**
     * To ensure a well functioning and attractive application at all window sizes, it is necessary for the UI elements
     * to be resized as the window is. This method is called by listeners on the stage objects width and height properties
     * and handles all such changes.
     */
    private void updateUISize() {
        //update canvas size
        double width = stage.getWidth();

        double height = stage.getHeight();

        double toolTabWidth = toolTab.getWidth();
        double rightEdgeWidth = rightEdge.getWidth();
        double bottomEdgeHeight = bottomEdge.getHeight();

        double middleWidth = width - toolTabWidth - rightEdgeWidth;

        double consoleHeight = consolePane.getHeight();
        double inputBarHeight = inputField.getHeight();
        double canvasHeight = height - consoleHeight - inputBarHeight - bottomEdgeHeight;
        toolTab.setMinHeight(height - inputBarHeight - bottomEdgeHeight);
        toolTab.setMaxHeight(height - inputBarHeight - bottomEdgeHeight);
        rightEdge.setHeight(height - bottomEdgeHeight - inputBarHeight);
        consolePane.setPrefWidth(middleWidth);
        inputField.setPrefWidth(width);
        bottomEdge.setWidth(width);
        canvas.setWidth(middleWidth);
        canvas.setHeight(canvasHeight);

        //Update graph for new window size
        double newViewLength = canvas.getWidth() / XRES;
        double viewLengthDelta = Math.abs(bMAX_X - bMIN_X) - newViewLength;
        bMAX_X = bMAX_X - viewLengthDelta;
        double xDiff = Math.abs((bMIN_X - xOffset) - (bMAX_X - xOffset));
        double newXDiff = zoomTransform * xDiff;
        double movNeccesaryToAcheiveX = (newXDiff - xDiff) / 2;
        MIN_X = (bMIN_X - deltaXCoord - xOffset) - movNeccesaryToAcheiveX;
        MAX_X = (bMAX_X - deltaXCoord - xOffset) + movNeccesaryToAcheiveX;


        double newViewHeight = canvas.getHeight() / (double) YRES;
        double viewHeightDelta = Math.abs(bMAX_Y - bMIN_Y) - newViewHeight;
        bMAX_Y = bMAX_Y - viewHeightDelta;
        double yDiff = Math.abs((bMIN_Y - yOffset) - (bMAX_Y - yOffset));
        double newYDiff = zoomTransform * yDiff;
        double movNeccesaryToAcheiveY = (newYDiff - yDiff) / 2;
        MIN_Y = (bMIN_Y + yOffset + deltaYCoord) - movNeccesaryToAcheiveY;
        MAX_Y = (bMAX_Y + yOffset + deltaYCoord) + movNeccesaryToAcheiveY;
        updateRender();
    }

    /**
     * Attaches change listeners to the stage which track its size and update UI elements when appropriate.
     */
    private void attachChangeListeners() {
        stage.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                updateUISize();
            }
        });
        stage.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                updateUISize();
            }
        });
    }

    /**
     * Changes the color of the bottom and right edges
     *
     * @param color the color to change to
     */
    private void updateUIColor(Color color) {
        rightEdge.fillProperty().setValue(color);
        bottomEdge.fillProperty().setValue(color);
    }

    /**
     * A static method which returns a number represented as a rounded string
     *
     * @param toRound a double representing an unrounded number
     * @return a String representing the input rounded to the correct number of decimal places.
     */
    public static String round(double toRound) {
        double minVisible = Math.pow(10, -1 * (roundLevel - 1));
        if (toRound > -minVisible && toRound < minVisible) {
            toRound = 0;
            String zeroOut = "0.";
            for (int i = 0; i < roundLevel; i++) {
                zeroOut += "0";
            }
            return zeroOut;
        }
        String asString = Double.toString(toRound);

        String outputString = asString.substring(0, asString.indexOf(".") + roundLevel);
        int leastShown = Integer.parseInt(Character.toString(asString.charAt(asString.indexOf(".") + roundLevel)));
        char greatestUnshown = asString.charAt(asString.indexOf(".") + roundLevel + 1);

        if (Integer.parseInt(Character.toString(greatestUnshown)) >= 5) {
            outputString += (leastShown + 1);
        } else {
            outputString += leastShown;
        }
        return outputString;
    }

    /**
     * Updates tab colors according to the tab colors array
     */
    private void updateTabColors() {
        for (int i = 0; i < toolTab.getTabs().size(); i++) {
            toolTab.getTabs().get(i).getContent().setStyle("-fx-background-color: #" +
                    tabColors[i].toString().substring(2) + ";");
        }
    }

    /**
     * A function to switch the main function being rendered. It resets the visualizers and re-renders the canvas.
     * @param newFunction the function which will replace the current mainFunction
     */
    public static void resetMainFunction(Function newFunction) {
        mainFunction = newFunction;
        secantDrawer.setFunction(mainFunction);
        derivativeGraphDrawer.setFunction(mainFunction);
        taylorSeriesDrawer.setFunction(mainFunction);
        riemannSumDrawer.setFunction(mainFunction);
        criticalPointDrawer.setFunction(mainFunction);
        updateRender();
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

    public static double getWidth() {
        return canvas.getWidth();
    }

    public static double getHeight() {
        return canvas.getHeight();
    }

    public static CriticalPoint getCriticalPointDrawer() {
        return criticalPointDrawer;
    }

    public static void main(String[] args) {
        launch(args);
    }
}