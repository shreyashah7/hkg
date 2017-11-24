package com.argusoft.hkg.ruleexecution;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.util.CollectionUtils;

/**
 * NotInBetween operation
 *
 * @author rajkumar
 */
public class NotInBetween extends Operation {

    public NotInBetween() {
        super("nib");
    }

    @Override
    public NotInBetween copy() {
        return new NotInBetween();
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
        BaseType<?> rightVal = (BaseType<?>) this.rightOperand;
        List objValues = new ArrayList();
        objValues = Arrays.asList(((String) rightVal.getValue()).replaceAll("'", "").split("\\s*,\\s*"));
        if (!CollectionUtils.isEmpty(objValues)) {

            Object first = objValues.get(0);
            Object second = objValues.get(1);
            CustomComparator comparator = new CustomComparator(type.get(v.getName().toString()));
            if (comparator.compare(first, obj) < 0 && comparator.compare(second, obj) < 0) {
                return true;
            } else if (comparator.compare(second, obj) > 0 && comparator.compare(first, obj) > 0) {
                return true;
            }
        } else {
            return false;
        }
        return false;
    }

}
