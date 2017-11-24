package com.argusoft.hkg.ruleexecution;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

/**
 * Util class to get base type of right hand side expression
 *
 * @author rajkumar
 * @param <T> type of expression
 */
public class BaseType<T> implements Expression {

    public T value;
    public Class<T> type;

    public BaseType(T value, Class<T> type) {
        this.value = value;
        this.type = type;
    }

    public T getValue() {
        return this.value;
    }

    public Class<T> getType() {
        return this.type;
    }

    @Override
    public boolean interpret(Map<String, ?> bindings, Map<String, String> fieldtype, Map<String, String> componentTypes) {
        return true;
    }

    public static BaseType<?> getBaseType(Object string, String dbFieldType) throws ParseException {
        if (string == null) {
            throw new IllegalArgumentException("The provided string must not be null");
        }
        if (dbFieldType == null) {
            return new BaseType<>(string, Object.class);
        }

        if (dbFieldType.equalsIgnoreCase("String")) {
            return new BaseType<>(string.toString(), String.class);
        } else if (dbFieldType.equalsIgnoreCase("Date")) {
            DateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
            return new BaseType<>(formatter.parse(string.toString().replaceAll("'", "")), Date.class);
        } else if (dbFieldType.equalsIgnoreCase("Boolean")) {
            return new BaseType<>(Boolean.parseBoolean(string.toString()), Boolean.class);
        } else if (dbFieldType.equalsIgnoreCase("Integer")) {
            return new BaseType<>(Integer.parseInt(string.toString()), Integer.class);
        } else if (dbFieldType.equalsIgnoreCase("List")) {
            return new BaseType<>((ArrayList) string, ArrayList.class);
        } else if (dbFieldType.equalsIgnoreCase("Decimal") || dbFieldType.equalsIgnoreCase("Number") || dbFieldType.equalsIgnoreCase("Long")) {
            return new BaseType<>(Long.parseLong(string.toString()), Long.class);
        } else if (dbFieldType.equalsIgnoreCase("Double")) {
            return new BaseType<>(Double.parseDouble(string.toString()), Double.class);
        } else {
            return new BaseType<>(string, Object.class);
        }
    }
}
