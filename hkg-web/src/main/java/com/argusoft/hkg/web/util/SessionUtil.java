/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.util;

import com.argusoft.hkg.model.HkHolidayEntity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

/**
 * This class is used to store the result sets which are required to keep in
 * session for some time. Clear the result sets after you use it. Also, this
 * class should take place of View Scope and Session scope data is needed
 * temporarily for more than one requests.
 *
 * @author Mital
 */
@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class SessionUtil implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<HkHolidayEntity> hkHolidayList;
    private Map<String, String> fileuploadMap;

    
    private Map<String,Object> reportColumnValueMap;
    
    private Map<String,Object> tempFilteredRecordList;
    
        
    /**
     * This method return holiday list which is in session scope.
     *
     * @return list of holiday.
     */
    public List<HkHolidayEntity> getHkHolidayList() {
        return hkHolidayList;
    }

    public Map<String, Object> getTempFilteredRecordList() {
        return tempFilteredRecordList;
    }

    public void setTempFilteredRecordList(Map<String, Object> tempFilteredRecordList) {
        this.tempFilteredRecordList = tempFilteredRecordList;
    }
    
    /**
     * This method sets holiday list into session.
     *
     * @param hkHolidayList
     */
    public void setHkHolidayList(List<HkHolidayEntity> hkHolidayList) {
        this.hkHolidayList = hkHolidayList;
    }

    public Map<String, String> getFileuploadMap() {
        return fileuploadMap;
    }

    public void setFileuploadMap(Map<String, String> fileuploadMap) {
        this.fileuploadMap = fileuploadMap;
    }

    public Map<String, Object> getReportColumnValueMap() {
        return reportColumnValueMap;
    }

    public void setReportColumnValueMap(Map<String, Object> reportColumnValueMap) {
        this.reportColumnValueMap = reportColumnValueMap;
    }
    

}
