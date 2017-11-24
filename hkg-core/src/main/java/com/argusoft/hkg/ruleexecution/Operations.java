package com.argusoft.hkg.ruleexecution;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Stores list of operations supported
 *
 * @author rajkumar
 */
public enum Operations {

    /**
     * Application of the Singleton pattern using enum *
     */
    INSTANCE;

    private final Map<Object, Operation> operations = new HashMap<>();

    public void registerOperation(Operation op, String symbol) {
        if (!operations.containsKey(symbol)) {
            operations.put(symbol, op);
        }
    }

    public void registerOperation(Operation op) {
        if (!operations.containsKey(op.getSymbol())) {
            operations.put(op.getSymbol(), op);
        }
    }

    public Operation getOperation(Object symbol) {
        return this.operations.get(symbol);
    }

    public Set<Object> getDefinedSymbols() {
        return this.operations.keySet();
    }
}
