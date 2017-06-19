import Tokens.*;
import Tokens.Operators.*;
import Tokens.BigOperations.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a function, a relation between a set of inputs and a set of allowable outputs.
 * @author Bay Foley-Cox
 */

public class Function {

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

        while(index < expChars.size()) {
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

            //if times, add times token
            else if(expression.substring(index).indexOf("*") == 0) {
                tokenList.add(new Times());
                index++;
            }
            //if times, add times token
            else if(expression.substring(index).indexOf("/") == 0) {
                tokenList.add(new Divide());
                index++;
            }

            //if sin, add sin token
            else if(expression.substring(index).indexOf("sin") == 0) {
                tokenList.add(new Sine());
                index += 3;
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
            if(t.getClass().getSimpleName().equals("Variable"))
                substitutedExpression.add(new Constant(x));
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

}
