package com.argusoft.hkg.core.common;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.argusoft.generic.database.common.CommonDAO;
import com.argusoft.hkg.model.HkNotificationEntity;
import com.google.gson.Gson;
import java.util.Date;
import java.util.Map;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.util.CollectionUtils;

public abstract class HkCoreService {

    @Autowired
    protected CommonDAO commonDao;

    protected List<Long> getCompnies(Long companyId) {
        List<Long> companyIds = new ArrayList<>();
        companyIds.add(0l);
        if (companyId != null && companyId != 0) {
            companyIds.add(companyId);
        }
        return companyIds;
    }

    public Criterion getDateRestrictions(String dateString, String dateColumn) {
        return Restrictions.or(
                Restrictions.sqlRestriction("to_char(" + dateColumn + ", 'DD-MM-YYYY') like '%" + dateString.replaceAll("\\s+", "") + "%'"),
                Restrictions.sqlRestriction("to_char(" + dateColumn + ", 'DD-MM-YY') like '%" + dateString.replaceAll("\\s+", "") + "%'"),
                Restrictions.sqlRestriction("to_char(" + dateColumn + ", 'DD/MM/YYYY') like '%" + dateString.replaceAll("\\s+", "") + "%'"),
                Restrictions.sqlRestriction("to_char(" + dateColumn + ", 'DD/MM/YY') like '%" + dateString.replaceAll("\\s+", "") + "%'"),
                Restrictions.sqlRestriction("to_char(" + dateColumn + ", 'DDTHFMMONTHYYYY') like upper('%" + dateString.replaceAll("\\s+", "") + "%')"),
                Restrictions.sqlRestriction("to_char(" + dateColumn + ", 'DDFMMONTHYYYY') like upper('%" + dateString.replaceAll("\\s+", "") + "%')"));
    }

    public HkNotificationEntity createNotification(String notificationType, String instanceType, Map<String, Object> valuesMap, Long instanceId, Long franchise) {
        String description = " ";
        if (!CollectionUtils.isEmpty(valuesMap)) {
            Gson gson = new Gson();
            description = gson.toJson(valuesMap);
        }
        HkNotificationEntity notification = new HkNotificationEntity(null, notificationType, new Date(), description, franchise, false);
        notification.setInstanceId(instanceId);
        notification.setInstanceType(instanceType);
        return notification;
    }

}
