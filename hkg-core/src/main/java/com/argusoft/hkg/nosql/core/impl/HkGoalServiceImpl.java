package com.argusoft.hkg.nosql.core.impl;

import com.argusoft.generic.core.mongotransaction.MongoTransaction;
import com.argusoft.generic.database.common.MongoGenericDao;
import com.argusoft.hkg.nosql.core.HkGoalService;
import com.argusoft.hkg.nosql.model.HkUserGoalStatusDocument;
import com.argusoft.sync.center.model.HkGoalTemplateDocument;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 *
 * @author rajkumar
 */
@Service
public class HkGoalServiceImpl implements HkGoalService {

    private static final String FOR_SERVICE = "forService";
    private static final String STATUS = "status";

    private static final class UserGoalFields {

        private static final String ID = "id";
        private static final String STATUS = "status";
        private static final String GOAL_TEMPLATE = "goalTemplate";
        private static final String ACTIVITY_NODE = "activityNode";
        private static final String FOR_USER = "forUser";
        private static final String FROM_DATE = "fromDate";
        private static final String TO_DATE = "toDate";
    }

    @Autowired
    private MongoGenericDao mongoGenericDao;

    @Override
    public List<HkGoalTemplateDocument> retrieveGoalTemplateByService(Long nodeId, List<String> status) {
        List<Criteria> criterias = new ArrayList<>();
        List<HkGoalTemplateDocument> documents = null;
        criterias.add(Criteria.where(FOR_SERVICE).is(nodeId));
        if (!CollectionUtils.isEmpty(status)) {
            criterias.add(Criteria.where(STATUS).in(status));
        }

        documents = mongoGenericDao.findByCriteria(criterias, HkGoalTemplateDocument.class);
        return documents;
    }

    // ------------------------ User Goal Status Service starts ------------------------------------//
    @Override
    public List<HkUserGoalStatusDocument> retrieveUserGoalStatusByGoalTemplateIds(List<Long> goaltemplateIds, List<String> status) {
        List<Criteria> criterias = new ArrayList<>();
        List<HkUserGoalStatusDocument> documents = new ArrayList<>();
        if (goaltemplateIds != null) {
            criterias.add(Criteria.where(UserGoalFields.GOAL_TEMPLATE).in(goaltemplateIds));
            if (!CollectionUtils.isEmpty(status)) {
                criterias.add(Criteria.where(UserGoalFields.STATUS).in(status));
            }
            documents = mongoGenericDao.findByCriteria(criterias, HkUserGoalStatusDocument.class);
        }

        return documents;
    }

    @Override
    @MongoTransaction
    public void saveUserGoalStatusByGoalTemplates(List<HkUserGoalStatusDocument> userGoalStatuses) {
        if (!CollectionUtils.isEmpty(userGoalStatuses)) {
            mongoGenericDao.createAll(userGoalStatuses);
        }
    }
    
    @Override
//    @MongoTransaction
    public void saveUserGoalStatusByGoalTemplatesCopy(List<HkUserGoalStatusDocument> userGoalStatuses) {
        if (!CollectionUtils.isEmpty(userGoalStatuses)) {
            mongoGenericDao.createAll(userGoalStatuses);
        }
    }

    @Override
//    @MongoTransaction
    public void updateUserGoalStatusByGoalTemplatesCopy(List<HkUserGoalStatusDocument> userGoalStatuses) {
        if (!CollectionUtils.isEmpty(userGoalStatuses)) {
            for (HkUserGoalStatusDocument userGoalStatuse : userGoalStatuses) {
                mongoGenericDao.update(userGoalStatuse);
            }
        }
    }

    @Override
    @MongoTransaction
    public void updateUserGoalStatusByGoalTemplates(List<HkUserGoalStatusDocument> userGoalStatuses) {
        if (!CollectionUtils.isEmpty(userGoalStatuses)) {
            for (HkUserGoalStatusDocument hkUserGoalStatusDocument : userGoalStatuses) {
                mongoGenericDao.update(hkUserGoalStatusDocument);
            }
        }
    }

    @Override
    public List<HkUserGoalStatusDocument> retrieveUserGoalStatusByActivityNodeId(Long nodeId) {
        List<Criteria> criterias = new ArrayList<>();
        List<HkUserGoalStatusDocument> documents = new ArrayList<>();
        if (nodeId != null) {
            criterias.add(Criteria.where(UserGoalFields.ACTIVITY_NODE).is(nodeId));
            documents = mongoGenericDao.findByCriteria(criterias, HkUserGoalStatusDocument.class);
        }
        return documents;
    }

    @Override
    public HkUserGoalStatusDocument retrieveGoalStatusByUserGoalTemplateCurrentDate(Long userId, Long goalTemplateId, Date currentDate) {
        List<Criteria> criterias = new ArrayList<>();

        if (userId != null) {
            criterias.add(Criteria.where(UserGoalFields.FOR_USER).is(userId));
        }
        if (goalTemplateId != null) {
            criterias.add(Criteria.where(UserGoalFields.GOAL_TEMPLATE).is(goalTemplateId));
        }
        if (currentDate != null) {
            criterias.add(Criteria.where(UserGoalFields.FROM_DATE).lte(currentDate)
                    .and(UserGoalFields.TO_DATE).gte(currentDate));
        }
        HkUserGoalStatusDocument document = (HkUserGoalStatusDocument) mongoGenericDao.findOneByCriteria(criterias, HkUserGoalStatusDocument.class);
        return document;
    }

    @Override
    public List<HkUserGoalStatusDocument> retrieveGoalStatusByUserAndStatus(Long userId, List<String> type) {
        List<Criteria> criterias = new ArrayList<>();

        if (userId != null) {
            criterias.add(Criteria.where(UserGoalFields.FOR_USER).is(userId));
        }
        if (!CollectionUtils.isEmpty(type)) {
            criterias.add(Criteria.where(UserGoalFields.STATUS).in(type));
        }

        List<HkUserGoalStatusDocument> documents = mongoGenericDao.findByCriteria(criterias, HkUserGoalStatusDocument.class);
        return documents;
    }

    @Override
    public List<HkUserGoalStatusDocument> retrieveGoalStatusByIds(List<ObjectId> ids) {
        List<HkUserGoalStatusDocument> documents = new ArrayList<>();

        if (!CollectionUtils.isEmpty(ids)) {
            List<Criteria> criterias = new ArrayList<>();
            criterias.add(Criteria.where(UserGoalFields.ID).in(ids));
            documents = mongoGenericDao.findByCriteria(criterias, HkUserGoalStatusDocument.class);
        }
        return documents;
    }

    @Override
    public List<HkUserGoalStatusDocument> retrieveGoalStatusByUserAndDateRange(Long userId, List<String> status, Date fromDate, Date toDate) {
        List<Criteria> criterias = new ArrayList<>();

        if (userId != null) {
            criterias.add(Criteria.where(UserGoalFields.FOR_USER).is(userId));
        }
        if (status != null) {
            criterias.add(Criteria.where(UserGoalFields.STATUS).in(status));
        }
        if (fromDate != null && toDate != null) {
            criterias.add(Criteria.where(UserGoalFields.TO_DATE).gte(fromDate).lte(toDate));
        }
        return mongoGenericDao.findByCriteria(criterias, HkUserGoalStatusDocument.class);
    }

    // ------------------------ User Goal Status Service ends ------------------------------------//
}
