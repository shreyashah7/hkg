package com.argusoft.hkg.ruleexecution;

import java.util.Map;
import java.util.Stack;

public class Not extends Operation {

    public Not() {
        super("NOT");
    }

    public Not copy() {
        return new Not();
    }

    @Override
    public int parse(Object[] tokens, int pos, Stack<Expression> stack, Map<String, String> fieldType) {
        int i = findNextExpression(tokens, pos + 1, stack, fieldType);
        Expression right = stack.pop();

        this.rightOperand = right;
        stack.push(this);

        return i;
    }

    @Override
    public boolean interpret(final Map<String, ?> bindings, Map<String, String> fieldTypes, Map<String, String> componentTypes) {
        return !this.rightOperand.interpret(bindings, fieldTypes, componentTypes);
    }
}
