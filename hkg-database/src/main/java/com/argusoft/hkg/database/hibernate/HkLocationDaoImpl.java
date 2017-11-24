/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.database.hibernate;

import com.argusoft.generic.database.common.impl.BaseAbstractGenericDao;
import com.argusoft.hkg.database.HkLocationDao;
import com.argusoft.hkg.model.HkLocationEntity;
import java.util.LinkedList;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

/**
 *
 * @author heena
 */
@Repository
public class HkLocationDaoImpl extends BaseAbstractGenericDao<HkLocationEntity, Long> implements HkLocationDao {

    private static final String LOCATION_NAME = "locationName";
    private static final String IS_ACTIVE = "isActive";
    private static final String IS_ARCHIVE = "isArchive";
    private static final String PARENT_LOCATION = "parent.id";
    private static final String TYPE = "locationType";

    @Override
    public List<HkLocationEntity> retrieveAllLocations(Boolean isActive) {

        Session session=getCurrentSession();
        Criteria criteria= session.createCriteria(HkLocationEntity.class);
        criteria.addOrder(Order.asc(LOCATION_NAME));
        if (isActive != null) {
        criteria.add(Restrictions.eq(IS_ACTIVE, isActive));
        }
        criteria.add(Restrictions.eq(IS_ARCHIVE, Boolean.FALSE));
        return criteria.list();
    }

    @Override
    public List<HkLocationEntity> getAllLocationsByParent(Long parent) {
        List<Criterion> CriterionList = new LinkedList<>();
        CriterionList.add(Restrictions.eq(PARENT_LOCATION, parent));
        return super.findByCriteriaList(CriterionList);
    }

    @Override
    public List<HkLocationEntity> getLocationsByLocationType(String locationLevel, Boolean isActive) {
        List<Criterion> criterions = new LinkedList<>();
        if (locationLevel != null) {
            criterions.add(Restrictions.eq(TYPE, locationLevel));
        }
        if (isActive != null) {
            criterions.add(Restrictions.eq(IS_ACTIVE, isActive));
        }

        return super.findByCriteriaList(criterions);
    }

    @Override
    public List<HkLocationEntity> retrieveLocationsByCriteria(String locationLevel, Long parentId, Boolean isActive) {
        List<Criterion> criterions = new LinkedList<>();
        if (locationLevel != null) {
            criterions.add(Restrictions.eq(TYPE, locationLevel));
        }
        if (parentId != null) {
            criterions.add(Restrictions.eq(PARENT_LOCATION, parentId));
        }
        if (isActive != null) {
            criterions.add(Restrictions.eq(IS_ACTIVE, isActive));
        }

        criterions.add(Restrictions.eq(IS_ARCHIVE, false));

        return super.findByCriteriaList(criterions);
    }

}
