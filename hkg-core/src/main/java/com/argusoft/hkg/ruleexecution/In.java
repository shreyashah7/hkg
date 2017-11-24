package com.argusoft.hkg.ruleexecution;

import com.argusoft.hkg.common.constantutil.HkSystemConstantUtil;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
 * In operation- Checks to see right expression value in list of bindings, if
 * finds successfully return true otherwise false
 *
 * @author rajkumar
 */
public class In extends Operation {

    public In() {
        super("in");
    }

    @Override
    public In copy() {
        return new In();
    }

    @Override
    public int parse(Object[] tokens, int pos, Stack<Expression> stack, Map<String, String> fieldType) {
        if (pos - 1 >= 0 && tokens.length >= pos + 1) {
            Object var = tokens[pos - 1];

            this.leftOperand = new Variable(var);
            this.rightOperand = new BaseType(tokens[pos + 1], Object.class);

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
            List<Date> listOfDts = new ArrayList<Date>();
            if (type.get(v.getName().toString()) != null) {
                DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                if (type.get(v.getName().toString()).equals("Date")) {
                    Calendar instance = Calendar.getInstance();
                    if (!CollectionUtils.isEmpty(customSet)) {
                        for (Object object : customSet) {
                            try {
                                instance.setTime(formatter.parse(object.toString()));
                                instance.set(Calendar.MILLISECOND, 0);
                                listOfDts.add(instance.getTime());
                            } catch (ParseException ex) {
                                Logger.getLogger(In.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                        try {
                            instance.setTime(formatter.parse(obj.toString()));
                        } catch (ParseException ex) {
                            Logger.getLogger(In.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        instance.set(Calendar.MILLISECOND, 0);
                        if (listOfDts.contains(instance.getTime())) {
                            return true;
                        } else {
                            return false;
                        }
                    }
                }
                //String component. user multiselect and multiselect
                if (type.get(v.getName().toString()).equals("String")) {
                    if (componentTypes != null && componentTypes.get(v.getName().toString()) != null 
                            && (componentTypes.get(v.getName().toString()).equals(HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN))
                            || (componentTypes.get(v.getName().toString()).equals(HkSystemConstantUtil.CustomField.ComponentType.USER_MULTISELECT))) {
                        List<String> multiValues = new ArrayList<>();
                        for (String s : obj.toString().split(",")) {
                            multiValues.add(s.trim());
                        }
                        System.out.println("Inside in:::"+multiValues);
                        System.out.println("Inside in2:::"+customSet);
                        if (!CollectionUtils.isEmpty(customSet)) {
                            int count = 0;
                            for (String s : multiValues) {
                                if (customSet.contains(s)) {
                                    count++;
                                    break;
                                }
                            }
                            return count != 0;
                        } else {
                            return false;
                        }
                    }

                }

            }
            if (customSet.contains(obj.toString())) {
                return true;
            }
        } else {
            return false;
        }
        return false;
    }

}
