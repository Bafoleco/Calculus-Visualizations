import tokens.*;
import tokens.operators.*;
import tokens.bigoperations.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a function, a relation between a set of inputs and a set of allowable outputs.  It also includes
 * some tools used on functions and some necessary methods that should have been built into the Math class but weren't
 * @author Bay Foley-Cox
 */

public class Function {

    private final double deltaX = 0.001;
    private List<Token> expression;

    /**
     * This method creates a function from a string representing an expression by parsing that expression into
     * the tokens that make it up.
     * @param infixString represents a user input string representing a mathematical expression in infix notation
     */
    public Function(String infixString) {
        expression = tokenify(infixString);
    }
    /**
     * This method creates a function from a list of tokens representing an expression
     * @param infixList a list of tokens representing a mathematical expression in infix notation
     */
    public Function(List<Token> infixList) {
        expression = infixList;
    }

    /**
     * A function which parses a string in infix notation and creates a list of its constituent tokens
     * @param expression a user input string representing a mathematical expression in infix notation
     * @return the original expression, still in infix notation, represented as a ser
     */
    private static List<Token> tokenify(String expression) {
        //creates an ArrayList of tokens to be filled and eventually returned of tokens
        List<Token> tokenList = new ArrayList<>();

        //creates a list of characters from the original expression string
        List<Character> expChars = new ArrayList<>();
        for(int i = 0; i < expression.length(); i++) {
            expChars.add(expression.charAt(i));
        }
        int index = 0;
        boolean didNothing = false;
        while(index < expChars.size() && !didNothing) {
            //if the char is a digit, the token will be a constant, get all adjacent nums and create a constant token
            if(Character.isDigit(expChars.get(index))) {
                List<Character> digits = new ArrayList<>();
                digits.add(expChars.get(index));
                while((index < expChars.size() - 1) && (Character.isDigit(expChars.get(index + 1)) || expChars.get(index + 1).equals('.'))) {
                    index++;
                    digits.add(expChars.get(index));
                }
                //convert list of digit strings to constant token
                String valuesString = "";
                for(Character c : digits) {
                    valuesString += c.toString();
                }
                double value = Double.parseDouble(valuesString);
                tokenList.add(new Constant(value));
                index++;
            }

            //skip over a space
            else if(expChars.get(index).equals(' ')) {
                index++;
            }

            else if(expression.substring(index, index + 1).equals("x")){
                tokenList.add(new Variable());
                index++;
            }

            //if plus, add plus token
            else if(expression.substring(index).indexOf("+") == 0) {
                tokenList.add(new Plus());
                index++;
            }
            else if(expression.substring(index).indexOf("-") == 0) {
                tokenList.add(new Minus());
                index++;
            }
            //if times, add times token
            else if(expression.substring(index).indexOf("*") == 0) {
                tokenList.add(new Times());
                index++;
            }
            //if divide, add divide token
            else if(expression.substring(index).indexOf("/") == 0) {
                tokenList.add(new Divide());
                index++;
            }
            //if exponentiate add exponentiate token
            else if(expression.substring(index).indexOf("^") == 0)  {
                tokenList.add(new Exponentiate());
                index++;
            }
            //if sin, add sin token
            else if(expression.substring(index).indexOf("sin") == 0) {
                tokenList.add(new Sine());
                index += 3;
            }
            else if(expression.substring(index).indexOf("cos") == 0){
                tokenList.add(new Cosine());
                index += 3;
            }
            else if(expression.substring(index).indexOf("tan") == 0) {
                tokenList.add(new Tangent());
                index += 3;
            }
            else if(expression.substring(index).indexOf("sqrt") == 0) {
                tokenList.add(new Sqrt());
                index += 4;
            }
            else if(expression.substring(index).indexOf("ln") == 0 || expression.substring(index).indexOf("LN") == 0) {
                tokenList.add(new NaturalLog());
                index += 2;
            }
            else if(expression.substring(index).indexOf("log") == 0 || expression.substring(index).indexOf("Log") == 0 ||
                    expression.substring(index).indexOf("LOG") == 0) {
                tokenList.add(new LogBase10());
                index += 3;
            }
            else if(expression.substring(index).indexOf("abs") == 0 || expression.substring(index).indexOf("Abs") == 0) {
                tokenList.add(new AbsoluteValue());
                index += 3;
            }
            //TODO fix infinite loop of bars aren't closed
            else if(expChars.get(index).equals('|')) {
                tokenList.add(new AbsoluteValue());
                tokenList.add(new LeftParens());
                int searchIndex = index + 1;
                while (!expChars.get(searchIndex).equals('|')) {
                    searchIndex++;
                    System.out.println(searchIndex);
                }
                expChars.set(searchIndex, ')');
                index++;
            }

            else if(expression.substring(index).indexOf("floor") == 0 || expression.substring(index).indexOf("Floor") == 0) {
                tokenList.add(new Floor());
                index += 5;
            }
            else if(expression.substring(index).indexOf("Ceiling") == 0 || expression.substring(index).indexOf("ceiling") == 0) {
                tokenList.add(new Ceiling());
                index += 7;
            }
            else if(expression.substring(index).indexOf("Ceil") == 0 || expression.substring(index).indexOf("ceil") == 0) {
                tokenList.add(new Ceiling());
                index += 4;
            }
            //if left parenthesis, add that token
            else if(expChars.get(index).equals('(')) {
                tokenList.add(new LeftParens());
                index++;
            }
            //if right parenthesis, add that token
            else if(expChars.get(index).equals(')')) {
                tokenList.add(new RightParens());
                index++;
            }
            //if comma, add separator token
            else if(expChars.get(index).equals(',')) {
                tokenList.add(new Seperator());
                index++;
            }
            //math constants
            else if(expChars.get(index).equals('e')){
                tokenList.add(new Constant(Math.E));
                index++;
            }
            else if(expression.substring(index).indexOf("pi") == 0 || expression.substring(index).indexOf("Pi") == 0 ||
                    expression.substring(index).indexOf("PI") == 0){
                tokenList.add(new Constant(Math.PI));
                index+=2;
            }
            //if a character got here, it means the function was invalid
            else {
                didNothing = true;
            }
        }
        if(didNothing && index < expChars.size()) {
            tokenList.clear();
        }

        //post processing
        for(int i = 0; i < tokenList.size(); i++) {
            if(tokenList.get(i).getClass().getSimpleName().equals("Minus") && (i == 0 ||
                    !tokenList.get(i - 1).getClass().getSimpleName().equals("Variable") &&
                    !tokenList.get(i - 1).getClass().getSimpleName().equals("Constant")))
            {
                if(tokenList.get(i + 1).getClass().getSimpleName().equals("Constant")) {
                   tokenList.get(i + 1).setValue(-1 * tokenList.get(i + 1).getValue());
                    tokenList.remove(i);
                }
                else {
                    tokenList.get(i + 1).setIsNegated(true);
                    tokenList.remove(i);
                }
            }
        }
        return tokenList;
    }

    /**
     * A method to find the value of the function it operates on at a given input
     * @param x the member of the domain at which the function shall be evaluated
     * @return the unique member of the codomain corresponding with x
     */
    public double computeFunc(double x) {
        //creates a new list of tokens from the one the function object contains by substituting variable tokens with
        //constant tokens of value x
        List<Token> substitutedExpression = new ArrayList<>();
        for (Token t : this.expression) {
            if(t.getClass().getSimpleName().equals("Variable")) {
                if (t.getIsNegated())
                    substitutedExpression.add(new Constant(-1 * x));
                substitutedExpression.add(new Constant(x));
            }
            else
                substitutedExpression.add(t);
        }
        //replace substitutedExpression written in infix notation with the same expression written in reverse polish
        //notation
        substitutedExpression = Token.shuntingYard(substitutedExpression);
        //ensures no point will be rendered if division by zero or other arithmetic error
        //setting value to double max value ensures it won't appear on the screen
        double value = Double.MAX_VALUE;
        try {
            value = Token.reversePolishCompute(substitutedExpression);
        } catch (ArithmeticException e) {
            System.out.println("Arithmetic Exception: Likely division by zero");
        }
        return value;
    }

    public double takeDerivative(int order, double xPoint) {

        if(order == 0) {
            return this.computeFunc(xPoint);
        } else if(order == 1) {
            return (this.computeFunc(xPoint + deltaX) - this.computeFunc(xPoint - deltaX)) / ( 2 * deltaX);
        }
        else if(order > 1) {
            return (this.takeDerivative(order - 1, xPoint + deltaX) -
                    this.takeDerivative(order - 1, xPoint - deltaX)) / (2 * deltaX);
        }
        else {
            System.out.println("Error");
            return -1;
        }
    }

    public static double factorial(int x) {
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

    public List<Token> getExpression() {
        return expression;
    }
}
