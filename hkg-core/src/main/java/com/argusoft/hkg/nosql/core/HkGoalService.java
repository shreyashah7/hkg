package com.argusoft.hkg.nosql.core;

import com.argusoft.hkg.nosql.model.HkUserGoalStatusDocument;
import com.argusoft.sync.center.model.HkGoalTemplateDocument;
import java.util.Date;
import java.util.List;
import org.bson.types.ObjectId;

/**
 *
 * @author rajkumar
 */
public interface HkGoalService {

    public List<HkGoalTemplateDocument> retrieveGoalTemplateByService(Long nodeId, List<String> status);

    // ------------------------ User Goal Status Service starts ------------------------------------//
    public List<HkUserGoalStatusDocument> retrieveUserGoalStatusByGoalTemplateIds(List<Long> goaltemplateIds, List<String> status);

    public void saveUserGoalStatusByGoalTemplates(List<HkUserGoalStatusDocument> userGoalStatuses);

    public void saveUserGoalStatusByGoalTemplatesCopy(List<HkUserGoalStatusDocument> userGoalStatuses);

    public void updateUserGoalStatusByGoalTemplatesCopy(List<HkUserGoalStatusDocument> userGoalStatuses);

    public void updateUserGoalStatusByGoalTemplates(List<HkUserGoalStatusDocument> userGoalStatuses);

    public List<HkUserGoalStatusDocument> retrieveUserGoalStatusByActivityNodeId(Long nodeId);

    public HkUserGoalStatusDocument retrieveGoalStatusByUserGoalTemplateCurrentDate(Long userId, Long goalTemplateId, Date currentDate);

    public List<HkUserGoalStatusDocument> retrieveGoalStatusByUserAndStatus(Long userId, List<String> type);

    public List<HkUserGoalStatusDocument> retrieveGoalStatusByIds(List<ObjectId> ids);

    public List<HkUserGoalStatusDocument> retrieveGoalStatusByUserAndDateRange(Long userId, List<String> status, Date fromDate, Date toDate);
    // ------------------------ User Goal Status Service ends ------------------------------------//
}
