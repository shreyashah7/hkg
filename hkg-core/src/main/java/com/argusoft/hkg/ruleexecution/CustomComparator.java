package com.argusoft.hkg.ruleexecution;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Custom comparator to compare value of two object based on type
 *
 * @author rajkumar
 */
public class CustomComparator implements Comparator<Object> {

    public Map<String, String> types;
    public String varType;

    public CustomComparator() {
    }

    public CustomComparator(String varType) {
        this.varType = varType;
    }

    @Override
    public int compare(Object o1, Object o2) {
        if (varType != null) {
            if (o2 instanceof Integer) {
                Integer i1 = (Integer) o2;
                Integer i2 = Integer.parseInt(o1.toString());
                return i2.compareTo(i1);
            } else if (o2 instanceof Long) {
                Long i1 = (Long) o2;
                Long i2 = Long.parseLong(o1.toString());
                return i2.compareTo(i1);
            } else if (o2 instanceof Date) {
                Calendar instance = Calendar.getInstance();
                Calendar instance2 = Calendar.getInstance();
                instance.setTime((Date) o2);
                DateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
                try {
                    instance2.setTime(formatter.parse(o1.toString()));
                } catch (ParseException ex) {
                    Logger.getLogger(CustomComparator.class.getName()).log(Level.SEVERE, null, ex);
                }
                instance.set(Calendar.MILLISECOND, 0);
                instance2.set(Calendar.MILLISECOND, 0);
                return instance2.compareTo(instance);
            } else if (o2 instanceof Double) {
                Double i1 = (Double) o2;
                Double i2 = Double.parseDouble(o1.toString());
                return i2.compareTo(i1);
            }
        }
        if (o2 instanceof Integer) {
            Integer i1 = (Integer) o2;
            Integer i2 = (Integer) ((BaseType) o1).getValue();
            return i2.compareTo(i1);
        } else if (o2 instanceof Long) {
            Long i1 = (Long) o2;
            Long i2 = (Long) ((BaseType) o1).getValue();
            return i2.compareTo(i1);
        } else if (o2 instanceof Date) {
            System.out.println("In Date...");
            Calendar instance = Calendar.getInstance();
            Calendar instance2 = Calendar.getInstance();
            instance.setTime((Date) o2);
            instance2.setTime((Date) ((BaseType) o1).getValue());
            instance.set(Calendar.MILLISECOND, 0);
            instance2.set(Calendar.MILLISECOND, 0);
            return instance2.compareTo(instance);
        }else if (o2 instanceof Double) {
            Double i1 = (Double) o2;
            Double i2 = (Double) ((BaseType) o1).getValue();
            return i2.compareTo(i1);
        } else {
            return 0;
        }

    }

    public String getVarType() {
        return varType;
    }

    public void setVarType(String varType) {
        this.varType = varType;
    }

}
