package com.argusoft.hkg.ruleexecution;

import java.util.Map;
import java.util.Stack;

/**
 * Operation class to identify operations
 *
 * @author rajkumar
 */
public abstract class Operation implements Expression {

    protected String symbol;

    protected Expression leftOperand = null;
    protected Expression rightOperand = null;

    public Operation(String symbol) {
        this.symbol = symbol;
    }

    public abstract Operation copy();

    public String getSymbol() {
        return this.symbol;
    }

    public abstract int parse(final Object[] tokens, final int pos, final Stack<Expression> stack, Map<String, String> fieldType);

    protected Integer findNextExpression(Object[] tokens, int pos, Stack<Expression> stack, Map<String, String> type) {
        Operations operations = Operations.INSTANCE;

        for (int i = pos; i < tokens.length; i++) {
            Operation op = operations.getOperation(tokens[i]);
            if (op != null) {
                op = op.copy();
                // we found an operation
                i = op.parse(tokens, i, stack, type);

                return i;
            }
        }
        return null;
    }
}
