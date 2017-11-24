/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.sync.center.core.impl;

import com.argusoft.generic.database.common.MongoGenericDao;
import com.argusoft.hkg.sync.center.core.HkCaratRangeService;
import com.argusoft.sync.center.model.HkCaratRangeDocument;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 *
 * @author shruti
 */
@Service
public class HkCaratRangeServiceImpl implements HkCaratRangeService {

    @Autowired
    private MongoGenericDao mongoGenericDao;

    private static class CaratRangeFields {

        private static final String ID = "id";

    }

    @Override
    public HkCaratRangeDocument getCaratRangeById(Long id) {
        return (HkCaratRangeDocument) mongoGenericDao.getMongoTemplate().findById(id, HkCaratRangeDocument.class, "caratrange");
    }

    @Override
    public Long saveOrUpdateCaratRange(HkCaratRangeDocument document) {
        mongoGenericDao.update(document);
        return document != null ? document.getId() : null;
    }

    @Override
    public List<HkCaratRangeDocument> retrieveCaratRangeByIds(List<Long> ids) {
        List<Criteria> criterias = new ArrayList<>();
        if (!CollectionUtils.isEmpty(ids)) {
            criterias.add(Criteria.where(CaratRangeFields.ID).in(ids));
        }

        return mongoGenericDao.findByCriteria(criterias, HkCaratRangeDocument.class);
    }

}
