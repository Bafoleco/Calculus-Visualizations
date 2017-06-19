import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import java.awt.*;

public class TaylorSeries extends Application {
    private Stage stage;
    private Canvas canvas;
    private GraphicsContext gc;
    private BorderPane p;
    private Scene graphicsScene;
    private Button tsRender;
    private TextField textField;

    final static double MIN_X = -1;
    final static double MAX_X = 2;
    final static double MIN_Y = -1.1;
    final static double MAX_Y = 1.1;

    //pixels per unit

    final static int XRES = 400;
    final static int YRES = 400;
    Function myFunction = new Function();

    public void start(Stage primaryStage) {
        //init
        stage = primaryStage;
        stage.setTitle("Taylor Series");

        //create canvas
        int width = (int) ( Math.abs(MAX_X - MIN_X) * XRES );
        int height = (int)( Math.abs(MAX_Y - MIN_Y) * YRES );
        System.out.println("Real Canvas height =" + height );
        canvas = new Canvas(width, height);

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
        graphicsScene = new Scene(p, width, height);
        stage.setScene(graphicsScene);
        stage.show();
        renderFunction();
        renderAxis();
    }

    private void renderFunctionPix() {
        for(int i = 0; i < (int) (Math.abs(MAX_X - MIN_X) * XRES); i++) {
            int yValue = Graph.getPixelSpace(i, myFunction.computeFunc(Graph.getCoordinateSpace(i)))[1];
            gc.setFill(Color.BLUE);
            gc.fillRect(i, yValue, 1, 1);
        }
    }

    private void renderFunction() {
        for(double d = MIN_X; d < MAX_X; d += .001){
            int yValue = Graph.getPixelSpace(d, myFunction.computeFunc(d))[1];
            int xValue = Graph.getPixelSpace(d, myFunction.computeFunc(d))[0];

            gc.setFill(Color.BLUE);
            gc.fillRect(xValue, yValue, 1, 1);
        }
    }

    private void renderAxis() {
        for(int i = 0; i < (int) Math.abs(MAX_X - MIN_X) * XRES; i++) {
            int yValue = Graph.getPixelSpace(0, 0)[1];
            gc.setFill(Color.BLACK);
            gc.fillRect(i, yValue, 1, 1);
        }


        for(int i = 0; i < canvas.getHeight(); i++) {
            gc.fillRect(Graph.getPixelSpace(0, 1)[0], i, 1, 1);
        }

    }

    private void renderTS() {

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
}
