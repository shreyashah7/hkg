/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.argusoft.hkg.web.center.leavemanagement.transformers;

import com.argusoft.hkg.sync.center.core.HkHolidayService;
import com.argusoft.hkg.web.leavemanagement.databeans.HolidayDatabean;
import com.argusoft.sync.center.model.HkHolidayDocument;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author gautam
 */
@Service
public class CenterHolidayTransformer {
    
    @Autowired
    HkHolidayService hkHolidayService;
    
    /**
     * This returns all the holiday of current year for particular franchise.
     *
     * @param franchiesId
     * @param isActive
     * @return List of holiday databeans sort by holiday start date.
     */
    public List<Date> retieveAllHolidayDates(Long franchiesId, boolean isActive) {
        Set<Date> dateList = new HashSet<>();
        List<HkHolidayDocument> hkHolidayEntityList = hkHolidayService.retrieveHolidaysByCriteria(null, null, null, true, Boolean.FALSE);
        List<HolidayDatabean> holidayDatabeansList = new ArrayList<>();
        if (hkHolidayEntityList != null && !hkHolidayEntityList.isEmpty()) {
            for(HkHolidayDocument holidayEntity : hkHolidayEntityList){
                Calendar startDate = Calendar.getInstance();
                Calendar endDate = Calendar.getInstance();
                startDate.setTime(holidayEntity.getStartDt());
                endDate.setTime(holidayEntity.getEndDt());
                if(startDate.getTime().equals(endDate.getTime())){
                    dateList.add(startDate.getTime());
                }else{
                    while(startDate.before(endDate)){
                        dateList.add(startDate.getTime());
                        startDate.add(Calendar.DATE, 1);
                    }
                    dateList.add(endDate.getTime());
                }
            }
            return new ArrayList<>(dateList);
        }

        return null;
    }
    
}
