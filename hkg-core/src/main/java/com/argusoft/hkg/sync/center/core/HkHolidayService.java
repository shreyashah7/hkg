/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.argusoft.hkg.sync.center.core;

import com.argusoft.sync.center.model.HkHolidayDocument;
import java.util.Date;
import java.util.List;

/**
 *
 * @author gautam
 */
public interface HkHolidayService {
    
    
    List<HkHolidayDocument> retrieveHolidaysByCriteria(String holidayTitle, Date startDate, Date endDate, boolean searchOnlyCurrentYear, Boolean archiveStatus);
}
