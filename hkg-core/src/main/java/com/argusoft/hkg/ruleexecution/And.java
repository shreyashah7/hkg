package com.argusoft.hkg.ruleexecution;

import java.util.Map;
import java.util.Stack;

/**
 * AND operation class
 *
 * @author rajkumar
 */
public class And extends Operation {

    public And() {
        super("AND");
    }

    public And copy() {
        return new And();
    }

    @Override
    public boolean interpret(Map<String, ?> bindings, Map<String, String> types, Map<String, String> componentTypes) {
        return leftOperand.interpret(bindings, types, componentTypes) && rightOperand.interpret(bindings, types, componentTypes);
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
}
