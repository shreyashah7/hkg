package com.argusoft.hkg.ruleexecution;

import java.util.Map;

/**
 * Variable class to store variable
 *
 * @author rajkumar
 */
public class Variable implements Expression {

    private Object name;

    public Variable(Object name) {
        this.name = name;
    }

    public Object getName() {
        return this.name;
    }

    @Override
    public boolean interpret(Map<String, ?> bindings, Map<String, String> fieldtype, Map<String, String> componentTypes) {
        return true;
    }
}
