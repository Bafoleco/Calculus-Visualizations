package main;

import tokens.*;
import tokens.operators.*;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.List;

public class TaylorSeries extends Visualizer {
    private int order;
    private double xPos;
    private boolean maclaurin = true;
    private List<Function> taylorPolynomials;
    private boolean errorVisOn = false;
    private double tsErrorLoc;
    private String error;

    public TaylorSeries(int order, double xPos, Function function) {
        this.order = order;
        this.xPos = xPos;
        this.function = function;
        createTaylorPolynomials();
    }

    public void draw() {
        if (showing) {
            Main.renderFunction(taylorPolynomials.get(order - 1), Color.INDIANRED);
            new Point(xPos, function.computeFunc(xPos), Color.INDIANRED).drawPoint();
            if (errorVisOn) {
                calculateError();
                double realValue = function.computeFunc(tsErrorLoc);
                double errorValue = taylorPolynomials.get(order - 1).computeFunc(tsErrorLoc);
                new Point(tsErrorLoc, errorValue, Color.INDIANRED.darker()).drawPoint();
                new Point(tsErrorLoc, realValue, Color.INDIANRED.darker()).drawPoint();
                Main.drawLineSegment(tsErrorLoc, errorValue, tsErrorLoc, realValue, Color.INDIANRED.darker(), 0.0);
                GraphicsContext gc = Main.getGc();
                gc.setFill(Color.BLACK);
                gc.setFont(Font.font(15));
                gc.fillText("Error: "
                                + error, Graph.getPixelSpace(tsErrorLoc, 0)[0] + 20,
                        Graph.getPixelSpace(0, realValue)[1]);
            }
        }
    }

    /**
     *To avoid the computationally intensive task of calculating the Taylor Series having to be done on every frame rendered,
     * This function pregenertes the Taylor Polynomials required whenever a new function is entered
     */
    public void createTaylorPolynomials() {
        List<Function> newTaylorPolynomials = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            List<Token> taylorPolynomial = new ArrayList<>();
            for (int j = 0; j < i + 1; j++) {
                if(j != 0)
                    taylorPolynomial.add(new Plus());
                taylorPolynomial.add(new LeftParens());
                taylorPolynomial.add(new Constant(function.takeDerivative(j, xPos)));
                taylorPolynomial.add(new Divide());
                taylorPolynomial.add(new Constant(Function.factorial(j)));
                taylorPolynomial.add(new RightParens());
                if(j != 0) {
                    taylorPolynomial.add(new Times());
                    taylorPolynomial.add(new LeftParens());
                    taylorPolynomial.add(new LeftParens());
                    taylorPolynomial.add(new Variable());
                    taylorPolynomial.add(new Minus());
                    taylorPolynomial.add(new Constant(xPos));
                    taylorPolynomial.add(new RightParens());
                    taylorPolynomial.add(new Exponentiate());
                    taylorPolynomial.add(new Constant(j));
                    taylorPolynomial.add(new RightParens());
                }
            }
            newTaylorPolynomials.add(new Function(taylorPolynomial));
        }
        taylorPolynomials = newTaylorPolynomials;
    }

    public void calculateError() {
        double errorValue  = taylorPolynomials.get(order - 1).computeFunc(tsErrorLoc) - function.computeFunc(tsErrorLoc);
        error = Main.round(errorValue);
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public void setFunction(Function function) {
        this.function = function;
        createTaylorPolynomials();
    }

    public void setxPos(double xPos) {
        this.xPos = xPos;
    }

    public void setMaclaurin(boolean maclaurin) {
        this.maclaurin = maclaurin;
    }

    public boolean isMaclaurin() {
        return maclaurin;
    }

    public void setErrorVisOn(boolean errorVisOn) {
        this.errorVisOn = errorVisOn;
    }

    public void setTsErrorLoc(double tsErrorLoc) {
        this.tsErrorLoc = tsErrorLoc;
    }
}

