/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.ruleexecution;

import java.util.Map;
import java.util.Stack;

/**
 *
 * @author siddharth
 */
public class RestrictByIP extends Operation {

    public RestrictByIP() {
        super("rbi");
    }

    @Override
    public RestrictByIP copy() {
        return new RestrictByIP();
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
        System.out.println("RestrictByIP ----- obj----" + obj);
        if (obj == null) {
            System.out.println("returns true");
            return false;
        } else {
            System.out.println("returns false");
            return true;
        }
    }
}
