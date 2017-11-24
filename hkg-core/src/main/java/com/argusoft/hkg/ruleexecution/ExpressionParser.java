package com.argusoft.hkg.ruleexecution;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parser for expression we provide,Parse the expression and prepare stack for
 * operation and variable
 *
 * @author rajkumar
 */
public class ExpressionParser {

    private static final Operations operations = Operations.INSTANCE;

    public static Expression fromString(String expr, Map<String, String> type) {
        Stack<Expression> stack = new Stack<>();

        List<String> tokensList = new ArrayList<String>();
        Pattern regex = Pattern.compile("[^\\s\"']+|\"[^\"]*\"|'[^']*'");
        Matcher regexMatcher = regex.matcher(expr);
        while (regexMatcher.find()) {
            tokensList.add(regexMatcher.group());
        }
        String[] tokens = tokensList.toArray(new String[tokensList.size()]);
        for (int i = 0; i < tokens.length; i++) {
            Operation op = operations.getOperation(tokens[i]);
            if (op != null) {
                op = op.copy();
                i = op.parse(tokens, i, stack, type);
            }
        }

        return stack.pop();
    }
}
