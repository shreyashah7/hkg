/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.ruleexecution;

import java.text.ParseException;
import java.util.Map;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * GreaterThanEquals Operation
 *
 * @author rajkumar
 */
public class GreaterThanEquals extends Operation {

    public GreaterThanEquals() {
        super("gte");
    }

    @Override
    public GreaterThanEquals copy() {
        return new GreaterThanEquals();
    }

    @Override
    public int parse(Object[] tokens, int pos, Stack<Expression> stack, Map<String, String> fieldType) {
        if (pos - 1 >= 0 && tokens.length >= pos + 1) {
            Object var = tokens[pos - 1];

            this.leftOperand = new Variable(var);
            try {
                this.rightOperand = BaseType.getBaseType(tokens[pos + 1], fieldType.get(((Variable) leftOperand).getName().toString()));
            } catch (ParseException ex) {
                Logger.getLogger(GreaterThan.class.getName()).log(Level.SEVERE, null, ex);
            }
            stack.push(this);

            return pos + 1;
        }
        throw new IllegalArgumentException("Cannot assign value to variable");
    }

    @Override
    public boolean interpret(Map<String, ?> bindings, Map<String, String> type, Map<String, String> componentTypes) {
        Variable v = (Variable) this.leftOperand;
        Object obj = bindings.get(v.getName());
        if (obj == null) {
            return false;
        }

        BaseType<?> type1 = (BaseType<?>) this.rightOperand;
        if (type1.getType().equals(obj.getClass())) {
            CustomComparator comparator = new CustomComparator(null);
            if (comparator.compare(type1, obj) < 0 || comparator.compare(type1, obj) == 0) {
                return true;
            }
        }
        return false;
    }

}
