package com.argusoft.hkg.ruleexecution;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Rule class to handle list of expression and execute rule
 *
 * @author rajkumar
 */
public class Rule {

    private List<Expression> expressions;

    public static class Builder {

        private List<Expression> expressions = new ArrayList<>();

        public Builder withExpression(Expression expr) {
            expressions.add(expr);
            return this;
        }

        public Rule build() {
            return new Rule(expressions);
        }
    }

    private Rule(List<Expression> expressions) {
        this.expressions = expressions;
    }

    public boolean eval(Map<String, ?> bindings, Map<String, String> type, Map<String, String> componentTypes) {
        boolean eval = false;
        for (Expression expression : expressions) {
            eval = expression.interpret(bindings, type, componentTypes);
            if (eval) {
                break;
            }
        }
        return eval;
    }
}
