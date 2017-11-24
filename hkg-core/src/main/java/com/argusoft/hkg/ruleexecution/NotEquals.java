package com.argusoft.hkg.ruleexecution;

import com.argusoft.hkg.common.constantutil.HkSystemConstantUtil;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 * NotEquals class
 *
 * @author rajkumar
 */
public class NotEquals extends Operation {

    public NotEquals() {
        super("ne");
    }

    @Override
    public NotEquals copy() {
        return new NotEquals();
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

        if (type1.getValue() != null) {

            String tempStr = type1.getValue().toString().replaceAll("'", "").replaceAll("\\[", "").replaceAll("\\]", "");
            String tempStr2 = tempStr.replaceAll(", ", ",").replaceAll(" ,", ",");
            Set customSet = StringUtils.commaDelimitedListToSet(tempStr2);
            if (type.get(v.getName().toString()) != null) {
                //String component. user multiselect and multiselect
                if (type.get(v.getName().toString()).equals("String")) {
                    if (componentTypes != null && componentTypes.get(v.getName().toString()) != null
                            && (componentTypes.get(v.getName().toString()).equals(HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN))
                            || (componentTypes.get(v.getName().toString()).equals(HkSystemConstantUtil.CustomField.ComponentType.USER_MULTISELECT))) {
                        List<String> multiValues = new ArrayList<>();
                        for (String s : obj.toString().split(",")) {
                            multiValues.add(s.trim());
                        }
                        System.out.println("Inside not equal:::"+multiValues);
                        System.out.println("Inside not equal2:::"+customSet);
                        if (!CollectionUtils.isEmpty(customSet)) {
                            int count = 0;
                            for (String s : multiValues) {
                                if (customSet.contains(s)) {
                                    count++;
                                }
                            }
                            return count != multiValues.size();
                        } else {
                            return false;
                        }
                    }
                }
            }
        }

        if (type1.getType().equals(obj.getClass())) {
            if (!type1.getValue().equals(obj)) {
                return true;
            }
            if (obj instanceof Date) {
                Calendar instance = Calendar.getInstance();
                Calendar instance2 = Calendar.getInstance();
                instance.setTime((Date) obj);
                instance2.setTime((Date) type1.getValue());
                instance.set(Calendar.MILLISECOND, 0);
                instance2.set(Calendar.MILLISECOND, 0);
                if (instance.getTimeInMillis() != instance2.getTimeInMillis()) {
                    return true;
                }
            }
        }
        return false;
    }

}
