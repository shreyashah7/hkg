package com.argusoft.hkg.ruleexecution;

import java.text.ParseException;
import java.util.Map;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * IsAfter operation- Checks whether given value is after binding value
 *
 * @author rajkumar
 */
public class IsAfter extends Operation {

    public IsAfter() {
        super("iafter");
    }

    @Override
    public IsAfter copy() {
        return new IsAfter();
    }

    @Override
    public int parse(Object[] tokens, int pos, Stack<Expression> stack, Map<String, String> fieldType) {
        if (pos - 1 >= 0 && tokens.length >= pos + 1) {
            Object var = tokens[pos - 1];

            this.leftOperand = new Variable(var);
            try {
                this.rightOperand = BaseType.getBaseType(tokens[pos + 1], fieldType.get(((Variable) leftOperand).getName().toString()));
            } catch (ParseException ex) {
                Logger.getLogger(Equals.class.getName()).log(Level.SEVERE, null, ex);
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
            if (comparator.compare(type1, obj) < 0) {
                return true;
            }
        }
        return false;
    }

}
