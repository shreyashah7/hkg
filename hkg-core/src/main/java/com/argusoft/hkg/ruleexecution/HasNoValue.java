package com.argusoft.hkg.ruleexecution;

import java.util.Map;
import java.util.Stack;

/**
 * HasNoValue operation- Return true if binding has null(no) value
 *
 * @author rajkumar
 */
public class HasNoValue extends Operation {

    public HasNoValue() {
        super("hnv");
    }

    @Override
    public HasNoValue copy() {
        return new HasNoValue();
    }

    @Override
    public int parse(Object[] tokens, int pos, Stack<Expression> stack, Map<String, String> fieldType) {
        if (pos - 1 >= 0 && tokens.length >= pos) {
            Object var = tokens[pos - 1];

            this.leftOperand = new Variable(var);
            stack.push(this);

            return pos;
        }
        throw new IllegalArgumentException("Cannot assign value to variable");
    }

    @Override
    public boolean interpret(Map<String, ?> bindings, Map<String, String> type, Map<String, String> componentTypes) {
        Variable v = (Variable) this.leftOperand;
        Object obj = bindings.get(v.getName());
        if (obj == null) {
            return true;
        } else {
            return false;
        }
    }

}
