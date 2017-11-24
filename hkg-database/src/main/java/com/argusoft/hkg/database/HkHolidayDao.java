/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.database;

import com.argusoft.generic.database.common.GenericDao;
import com.argusoft.hkg.model.HkHolidayEntity;
import java.util.Date;
import java.util.List;

/**
 * DAO class for Holiday Entity.
 *
 * @author Mital
 */
public interface HkHolidayDao extends GenericDao<HkHolidayEntity, Long> {

    /**
     * Retrieves all the holidays.
     *
     * @param franchise Id of Franchise. If less than or equal to 0, holidays of
     * franchise with 0 will be returned.
     * @param archiveStatus False if active i.e. non-archived records required, True if archive i.e. deleted
     * record to be retrieved. When null, returns all records.
     * @return List of Holidays.
     */
    public List<HkHolidayEntity> retrieveAllHolidays(Long franchise, Boolean archiveStatus);

    /**
     * Search based on the title of the holiday.
     *
     * @param titleLike Search string to match the holiday title.
     * @param franchise Id of Franchise. If less than or equal to 0, holidays of
     * franchise with 0 will be returned.
     * @param archiveStatus False if active i.e. non-archived records required, True if archive i.e. deleted
     * record to be retrieved. When null, returns all records.
     * @return List of Holidays.
     */
    public List<String> searchPreviousYearsHolidaysByTitle(String titleLike, Long franchise, Boolean archiveStatus);

    /**
     * Retrieves Holidays by the given parameters.
     *
     * @param holidayTitle Title of Holiday, null if none.
     * @param startDate Start date of the holiday, null if none.
     * @param endDate End date of the holiday, null if none.
     * @param franchise Id of Franchise. If less than or equal to 0, holidays of
     * franchise with 0 will be returned.
     * @param archiveStatus False if active i.e. non-archived records required, True if archive i.e. deleted
     * record to be retrieved. When null, returns all records.
     * @return List of holidays.
     */
    public List<HkHolidayEntity> retrieveHolidaysByCriteria(String holidayTitle, Date startDate, Date endDate, boolean searchOnlyCurrentYear, Long franchise, Boolean archiveStatus);
}
