package com.argusoft.hkg.ruleexecution;

import java.util.Map;

/**
 * Interface for expression
 *
 * @author rajkumar
 */
public interface Expression {

    public boolean interpret(final Map<String, ?> bindings, Map<String, String> type, Map<String, String> componentTypes);
}
