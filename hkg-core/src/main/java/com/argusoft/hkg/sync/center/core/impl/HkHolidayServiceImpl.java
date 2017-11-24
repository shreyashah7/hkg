/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.sync.center.core.impl;

import com.argusoft.generic.database.common.MongoGenericDao;
import com.argusoft.hkg.sync.center.core.HkHolidayService;
import com.argusoft.sync.center.model.HkHolidayDocument;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

/**
 *
 * @author gautam
 */
@Service
public class HkHolidayServiceImpl implements HkHolidayService {

    @Autowired
    MongoGenericDao mongoGenericDao;

    public static final String HOLIDAY_TITILE = "holidayTitle";
    public static final String START_DATE = "startDt";
    public static final String END_DATE = "endDt";
    public static final String IS_ARCHIVE = "isArchive";

    @Override
    public List<HkHolidayDocument> retrieveHolidaysByCriteria(String holidayTitle, Date startDate, Date endDate, boolean searchOnlyCurrentYear, Boolean archiveStatus) {
        Query query = new Query();
        if (holidayTitle != null) {
            query.addCriteria(Criteria.where(HOLIDAY_TITILE).regex(holidayTitle, "i"));
        }
        if (endDate != null) {
            query.addCriteria(Criteria.where(END_DATE).lte(endDate));
        }
        //To allow querying on same field we have to use "and" or "or" operator.
        if (searchOnlyCurrentYear && startDate != null) {
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.DAY_OF_YEAR, 1);
            query.addCriteria(new Criteria().andOperator(Criteria.where(START_DATE).gte(startDate), Criteria.where(START_DATE).gte(cal.getTime())));
        } else if (searchOnlyCurrentYear) {
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.DAY_OF_YEAR, 1);
            query.addCriteria(Criteria.where(START_DATE).gte(cal.getTime()));
        } else if (startDate != null) {
            query.addCriteria(Criteria.where(START_DATE).gte(startDate));
        }
        query.addCriteria(Criteria.where(IS_ARCHIVE).is(archiveStatus));

        return mongoGenericDao.getMongoTemplate().find(query, HkHolidayDocument.class);
    }

}
