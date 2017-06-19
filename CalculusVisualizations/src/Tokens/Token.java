package Tokens;
import Tokens.BigOperations.BigOperation;
import Tokens.Operators.Operator;
import java.util.ArrayList;
import java.util.List;

/**
 * A class representing tokens, the smallest unit of meaning in a mathematical expression
 * @author Bay Foley-Cox
 */
public abstract class Token {
    /**
     * An implementation of the shunting yard algorithm, this method converts from infix notation to reverse polish notation.
     * @param infixTokens a list of tokens in infix notation
     * @return a list of tokens, representing the sam expression as the original, in reverse polish notation.
     */
    public static List<Token> shuntingYard(List<Token> infixTokens) {
        List<Token> outputStack = new ArrayList<>();
        List<Token> operatorStack = new ArrayList<>();
        while(infixTokens.size() > 0) {
           Token workingToken =  infixTokens.get(0);
           //if constant add to output
           if(workingToken.getClass().getSimpleName().equals("Constant") || workingToken.getClass().getSimpleName().equals("Variable") ) {
               outputStack.add(workingToken);
           }
           //if function add to operator stack
           else if(workingToken.getClass().getSuperclass().getSimpleName().equals("BigOperation")) {
               operatorStack.add(workingToken);
           }
           //if seperator eg ","
           else if(workingToken.getClass().getSimpleName().equals("Seperator")){
               while (!operatorStack.get(operatorStack.size()-1).getClass().getSimpleName().equals("LeftParens")) {
                   outputStack.add(operatorStack.remove(operatorStack.size() - 1));
               }
            }
           //if operator
           else if(workingToken.getClass().getSuperclass().getSimpleName().equals("Operator")) {
               Token topToken;
               if(operatorStack.size() > 0) {
                    topToken = operatorStack.get(operatorStack.size()-1);
               }
               else {
                    topToken = null;
               }
               while(topToken != null && topToken.getClass().getSimpleName().equals("Operators")) {
                   topToken = operatorStack.get(operatorStack.size()-1);
                   if( (!((Operator) workingToken).isAssociativity() && ((Operator) workingToken).getPrecedence() <=
                            ((Operator) topToken).getPrecedence()) || ( ((Operator) workingToken).isAssociativity() &&
                            ((Operator) workingToken).getPrecedence() < ((Operator) topToken).getPrecedence()) ) {

                       outputStack.add(operatorStack.remove(operatorStack.size() - 1));
                   }
               }
               operatorStack.add(workingToken);
           }
           //if token is left parentheses add it to the operator stack
            else if(workingToken.getClass().getSimpleName().equals("LeftParens")) {
               operatorStack.add(workingToken);
            }
            //if token is right parentheses
            else if(workingToken.getClass().getSimpleName().equals("RightParens")) {
               while (!operatorStack.get(operatorStack.size()-1).getClass().getSimpleName().equals("LeftParens")) {
                   outputStack.add(operatorStack.remove(operatorStack.size() - 1));
               }
               //remove left parens
               operatorStack.remove(operatorStack.size()-1);
               if(operatorStack.size() > 0 && operatorStack.get(operatorStack.size()-1).getClass().getSuperclass().getSimpleName().equals("BigOperation")) {
                   outputStack.add(operatorStack.remove(operatorStack.size() - 1));
               }
           }
           infixTokens.remove(0);
        }
        //currently assuming no msitakes,
        //will add custom errors later
        while(operatorStack.size() > 0) {
            outputStack.add(operatorStack.remove(operatorStack.size() - 1));
        }
        return outputStack;
    }

    /**
     * A method to find the value of reverse polish notation expressions.
     * @param rpnTokens a string of tokens representing a mathematical expression in reverse polish notation
     * @return the value of the expression when evaluated.
     */
    public static double reversePolishCompute(List<Token> rpnTokens) {
        List<Double> stack = new ArrayList<>();
        for(Token t: rpnTokens) {
            if(t.getClass().getSimpleName().equals("Constant")) {
                stack.add(((Constant) t).getValue());
            }
            else if(t.getClass().getSuperclass().getSimpleName().equals("Operator")) {
                double a = stack.remove(stack.size() -  1);
                double b = stack.remove(stack.size() -  1);
                stack.add(((Operator)t).stackAction(a, b));
            }
            else if(t.getClass().getSuperclass().getSimpleName().equals("BigOperation")) {
                //grab required values from top of stack
                int inputNum = ((BigOperation)t).getArgsTaken();
                List<Double> inputList = new ArrayList<>();
                for(int i = 0; i < inputNum; i++) {
                    inputList.add(stack.remove(stack.size() - 1));
                }
                stack.add(((BigOperation)t).stackAction(inputList));
            }
        }
        return stack.get(0);
    }
}
