/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.database.hibernate;

import com.argusoft.generic.database.common.impl.BaseAbstractGenericDao;
import com.argusoft.hkg.database.HkHolidayDao;
import com.argusoft.hkg.model.HkHolidayEntity;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

/**
 * Implementation class for HkHolidayDao.
 *
 * @author Mital
 */
@Repository
public class HkHolidayDaoImpl extends BaseAbstractGenericDao<HkHolidayEntity, Long> implements HkHolidayDao {

    private static final Property IS_ARCHIVE = Property.forName("isArchive");
    private static final Property TITLE = Property.forName("holidayTitle");
    private static final Property FRANCHISE = Property.forName("franchise");
    private static final Property START_DATE = Property.forName("startDt");
    private static final Property END_DATE = Property.forName("endDt");

    @Override
    public List<HkHolidayEntity> retrieveAllHolidays(Long franchise, Boolean archiveStatus) {
        Criteria criteria = getCurrentSession().createCriteria(HkHolidayEntity.class);

        if (franchise <= 0) {
            criteria.add(FRANCHISE.eq(0L));
        } else {
            //  Get all the franchise specific and default holidays if other than 0 is passed as franchise
            criteria.add(FRANCHISE.in(new Long[]{0L, franchise}));
        }

        if (archiveStatus != null) {
            //  As isActive is not used for now, let's ignore it for this module.
            criteria.add(IS_ARCHIVE.eq(archiveStatus));
        }

        return criteria.list();
    }

    @Override
    public List<String> searchPreviousYearsHolidaysByTitle(String titleLike, Long franchise, Boolean archiveStatus) {
        Criteria criteria = getCurrentSession().createCriteria(HkHolidayEntity.class);

        if (titleLike != null) {
            criteria.add(TITLE.like(titleLike, MatchMode.ANYWHERE).ignoreCase());
        }

        //  Holiday should be from previous years
        Calendar todayCal = Calendar.getInstance();
        todayCal.set(Calendar.DAY_OF_MONTH, 31);
        todayCal.set(Calendar.MONTH, Calendar.DECEMBER);
        todayCal.set(Calendar.YEAR, todayCal.get(Calendar.YEAR) - 1);
        criteria.add(START_DATE.le(todayCal.getTime()));

        if (franchise <= 0) {
            criteria.add(FRANCHISE.eq(0L));
        } else {
            //  Get all the franchise specific and default holidays if other than 0 is passed as franchise
            criteria.add(FRANCHISE.in(new Long[]{0L, franchise}));
        }

        if (archiveStatus != null) {
            //  As isActive is not used for now, let's ignore it for this module.
            criteria.add(IS_ARCHIVE.eq(archiveStatus));
        }

        criteria.setProjection(Projections.distinct(TITLE));

        return criteria.list();
    }

    @Override
    public List<HkHolidayEntity> retrieveHolidaysByCriteria(String holidayTitle, Date startDate, Date endDate, boolean searchOnlyCurrentYear, Long franchise, Boolean archiveStatus) {
        Criteria criteria = getCurrentSession().createCriteria(HkHolidayEntity.class);

        //  if one of the given fields is not null, add below criteria
        if (holidayTitle != null || (startDate != null && endDate != null)) {
            criteria.add(Restrictions.or((holidayTitle == null ? TITLE.like(holidayTitle, MatchMode.EXACT) : TITLE.like(holidayTitle, MatchMode.EXACT).ignoreCase()),
                    //  Check if the start date and end dates overlap with the fields in database
                    Restrictions.and(START_DATE.le(endDate), END_DATE.ge(startDate))));
        }

        //  Get all the holidays that are before given start date
        if (startDate != null && endDate == null) {
            criteria.add(END_DATE.ge(startDate));
        }

        //  Get all the holidays that are before given end date
        if (startDate == null && endDate != null) {
            criteria.add(START_DATE.le(endDate));
        }

        if (searchOnlyCurrentYear) {
            Calendar todayCal = Calendar.getInstance();
            criteria.add(Restrictions.or(Restrictions.sqlRestriction("to_char(start_dt, 'YYYY') like '" + todayCal.get(Calendar.YEAR) + "'"),
                    Restrictions.sqlRestriction("to_char(end_dt, 'YYYY') like '" + todayCal.get(Calendar.YEAR) + "'")));
        }

        if (franchise <= 0) {
            criteria.add(FRANCHISE.eq(0L));
        } else {
            //  Get all the franchise specific and default holidays if other than 0 is passed as franchise
            criteria.add(FRANCHISE.in(new Long[]{0L, franchise}));
        }

        if (archiveStatus != null) {
            //  As isActive is not used for now, let's ignore it for this module.
            criteria.add(IS_ARCHIVE.eq(archiveStatus));
        }

        return criteria.list();
    }
}
