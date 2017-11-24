/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.common.functionutil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TimeZone;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.springframework.util.CollectionUtils;

/**
 * This class specifies the static final constants to be used in application.
 * Use these methods after checking it with sample data and if any query,
 * contact Author before editing..
 *
 * @author hardik
 */
public class HkSystemFunctionUtil {
    
    public static Date convertToStartDate(Date date) {
        Calendar calDate = Calendar.getInstance();
        if (date != null) {
            calDate.setTime(date);
        }
        calDate.set(Calendar.HOUR_OF_DAY, 0);
        calDate.set(Calendar.MINUTE, 0);
        calDate.set(Calendar.SECOND, 0);
        calDate.set(Calendar.MILLISECOND, 0);
        return calDate.getTime();
    }

    public static Date convertToEndDate(Date date) {
        Calendar calDate = Calendar.getInstance();
        if (date != null) {
            calDate.setTime(date);
        }
        calDate.set(Calendar.HOUR_OF_DAY, 23);
        calDate.set(Calendar.MINUTE, 59);
        calDate.set(Calendar.SECOND, 59);
        calDate.set(Calendar.MILLISECOND, 999);
        return calDate.getTime();
    }

    /**
     * Author Hardik
     * To get working days between 2 dates, pass weekends as "1,2" where 1 to 7 as sunday to saturday.
     * @param fromDate
     * @param toDate
     * @param weekends
     * @return 
     */
    public static int getTotalWorkingDaysBetweenDates(Date fromDate, Date toDate, String weekends) {
//        Calendar calTemp = Calendar.getInstance();

        int workingDays = 0;
        int offDaysTotal;
        DateTime startDate = new DateTime(fromDate);
        DateTime endDate = new DateTime(toDate);

        Days d = Days.daysBetween(startDate, endDate);
        int days = d.getDays();

        if (days >= 1) {
            String[] offDays = weekends.split(",");
            int[] offDaysInt = new int[offDays.length];
            for (int i = 0; i < offDays.length; i++) {
                offDaysInt[i] = Integer.parseInt(offDays[i]);
            }
            if (days % 7 == 0) {
                offDaysTotal = (days / 7) * offDays.length;
                workingDays = (short) (days - offDaysTotal);
            } else {
                offDaysTotal = (days / 7) * offDays.length;
                int remainingDays = days % 7;
                int dayNext = days - (days % 7) + 1;
                Calendar calR = Calendar.getInstance();
                calR.setTimeInMillis(toDate.getTime());
                for (int j = 0; j < remainingDays; j++) {
                    calR.set(Calendar.DAY_OF_MONTH, dayNext);
                    dayNext++;
                    for (int i = 0; i < offDays.length; i++) {
                        offDaysInt[i] = Integer.parseInt(offDays[i]);
                        if (calR.get(Calendar.DAY_OF_WEEK) == offDaysInt[i]) {
                            offDaysTotal++;
                        }
                    }
                }
                workingDays = days - offDaysTotal;
            }
        }
        return workingDays;
    }

    //Author Hardik
    //hours difference in float for same day, this will not consider day or month of date.
    public static float getNumOfHoursForSameDate(Date fromTime, Date toTime) {
        float hours = 0.0f;
        Calendar fromCal = Calendar.getInstance();
        fromCal.setTime(fromTime);
        Calendar toCal = Calendar.getInstance();
        toCal.setTime(toTime);
        hours = toCal.get(Calendar.HOUR_OF_DAY) - fromCal.get(Calendar.HOUR_OF_DAY);
        hours = hours + ((float) (toCal.get(Calendar.MINUTE) - fromCal.get(Calendar.MINUTE)) / 60);
        return hours;
    }

    /*
     * these 4 methods below are used to get absent dates when from date, to date and present dates are given
     * Author- Hardik
     */
    // You have to pass all 3 parameters to use these methods
    public static List<Date> getDateDifference(Date fromDate, Date toDate,
            List<Date> destinationList) {

        Set<Date> dateDiff = new HashSet<>();
        List<Date> sourceList = getDatesBetween(fromDate, toDate);

        if (!CollectionUtils.isEmpty(destinationList)) {
            destinationList = getFormatedDateList(destinationList);

            Set<Date> set1 = new HashSet<>(sourceList);
            Set<Date> set2 = new HashSet<>(destinationList);

            dateDiff = difference(set1, set2);
        } else {
            dateDiff = new HashSet<>(sourceList);
        }

        List<Date> diffList = new ArrayList<>(dateDiff);
        return diffList;
    }

    public static List<Date> getDatesBetween(final Date fromDate, final Date toDate) {

        List<Date> dates = new ArrayList<>();
        Calendar start = Calendar.getInstance();
        start.setTime(fromDate);
        Calendar end = Calendar.getInstance();
        end.setTime(toDate);

        for (Date date = start.getTime(); !start.after(end); start.add(
                Calendar.DATE, 1), date = start.getTime()) {
            // Do your job here with `date`.
            dates.add(new Date(date.getYear(), date.getMonth(), date.getDate()));
        }

        return dates;
    }

    public static List<Date> getFormatedDateList(List<Date> list) {

        List<Date> newDateList = new ArrayList<>();
        for (Date date : list) {
            // Do your job here with `date`.
            newDateList.add(new Date(date.getYear(), date.getMonth(), date.getDate()));
        }

        return newDateList;
    }

    public static <T> Set<T> difference(Set<T> setA, Set<T> setB) {
        Set<T> tmp = new HashSet<>(setA);
        tmp.removeAll(setB);
        return tmp;
    }

    public static Date getDateWithoutTimeStamp(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.clear(Calendar.AM_PM);
        cal.clear(Calendar.MINUTE);
        cal.clear(Calendar.SECOND);
        cal.clear(Calendar.MILLISECOND);
        return cal.getTime();
    }
//Added by kvithlani to retrieve key from map by specifying value

    public static <T, E> T getKeyByValue(Map<T, E> map, E value) {
        if (value != null) {
            for (Entry<T, E> entry : map.entrySet()) {
                if (value.equals(entry.getValue())) {
                    return entry.getKey();
                }
            }
        }
        return null;
    }
    
    /*
     * Method for merging Start Date and Time
     */
    public static Date mergeDateAndTime(Date onDate, Date onTime) {
        Calendar onDateCal = Calendar.getInstance();
        onDateCal.setTime(onDate);
        Calendar onTimeCal = Calendar.getInstance();
        onTimeCal.setTime(onTime);
        onDateCal.set(Calendar.HOUR, onTimeCal.get(Calendar.HOUR));
        onDateCal.set(Calendar.MINUTE, onTimeCal.get(Calendar.MINUTE));
        onDateCal.set(Calendar.AM_PM, onTimeCal.get(Calendar.AM_PM));
        onDateCal.set(Calendar.SECOND, 0);
        onDateCal.set(Calendar.MILLISECOND, 0);
        return onDateCal.getTime();
    }
    
    /**
     * This method converts Client Date to Server Date
     *
     * @param inputDate
     * @param clientRawOffsetInMin
     * @return Server Date
     */
    public static Date convertToServerDate(Date inputDate, Integer clientRawOffsetInMin) {
        Date outDate = inputDate;
        if (inputDate != null && clientRawOffsetInMin != null) {
            clientRawOffsetInMin = clientRawOffsetInMin * 60000;
            Integer serverOffset = Calendar.getInstance().getTimeZone().getRawOffset();
            Integer timeToSubInMin = ((serverOffset + clientRawOffsetInMin) / 60000) * -1;

            Calendar outDateCal = Calendar.getInstance();
            outDateCal.setTime(inputDate);
            outDateCal.add(Calendar.MINUTE, timeToSubInMin);
            
            //Daylight Time Verification
            TimeZone timeZone = outDateCal.getTimeZone();
            if(timeZone.inDaylightTime(outDateCal.getTime())) {
                outDateCal.add(Calendar.MILLISECOND,(timeZone.getDSTSavings()*-1));
            }

            outDate = outDateCal.getTime();
        }        
        return outDate;
    }

    /**
     * This method converts Server Date to Client Date
     *
     * @param inputDate
     * @param clientRawOffsetInMin
     * @return Client Date
     */
    public static Date convertToClientDate(Date inputDate, Integer clientRawOffsetInMin) {
        Date outDate = inputDate;
        if (inputDate != null && clientRawOffsetInMin != null) {
            clientRawOffsetInMin = clientRawOffsetInMin * 60000;
            Integer serverOffset = Calendar.getInstance().getTimeZone().getRawOffset();
            Integer timeToSubInMin = ((serverOffset + clientRawOffsetInMin) / 60000);

            Calendar outDateCal = Calendar.getInstance();
            outDateCal.setTime(inputDate);
            outDateCal.add(Calendar.MINUTE, timeToSubInMin);
            //Daylight Time Verification
            TimeZone timeZone = outDateCal.getTimeZone();
            if(timeZone.inDaylightTime(outDateCal.getTime())) {
                outDateCal.add(Calendar.MILLISECOND,timeZone.getDSTSavings());
            }

            outDate = outDateCal.getTime();
        }
        return outDate;
    }
    
    public static Long durationInMin(Date startDate, Date endDate) {
        Long duration = null;
        if(startDate != null && endDate != null) {
            duration = (endDate.getTime() - startDate.getTime())/(60*60);
        }
        return duration;
    }

    public static String decodeMapKeyWithDot(String key) {
        return key.replace("_", ".");
    }

    public static Set<Long> getCompnies(Long companyId) {
        Set<Long> companyIds = new HashSet<>();
        companyIds.add(0l);
        if (companyId != null) {
            companyIds.add(companyId);
        }
        return companyIds;
    }
}
