package com.argusoft.hkg.ruleexecution;

import java.util.Map;
import java.util.Stack;

/**
 * OR operation
 *
 * @author rajkumar
 */
public class Or extends Operation {

    public Or() {
        super("OR");
    }

    public Or copy() {
        return new Or();
    }

    @Override
    public int parse(Object[] tokens, int pos, Stack<Expression> stack, Map<String, String> fieldType) {
        Expression left = stack.pop();
        int i = findNextExpression(tokens, pos + 1, stack, fieldType);
        Expression right = stack.pop();

        this.leftOperand = left;
        this.rightOperand = right;

        stack.push(this);

        return i;
    }

    @Override
    public boolean interpret(Map<String, ?> bindings, Map<String, String> type, Map<String, String> componentTypes) {
        return leftOperand.interpret(bindings, type, componentTypes) || rightOperand.interpret(bindings, type, componentTypes);
    }

}
