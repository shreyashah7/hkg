/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.nosql.core.impl;

import com.argusoft.generic.database.common.MongoGenericDao;
import com.argusoft.hkg.nosql.core.HkFeatureService;
import com.argusoft.sync.center.model.SyncCenterFeatureDocument;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 *
 * @author akta
 */
@Service
public class HkFeatureServiceImpl implements HkFeatureService {

    @Autowired
    private MongoGenericDao mongoGenericDao;

    private final static String IS_ACTIVE = "isActive";
    private final static String IS_ARCHIVE = "isArchive";
    private final static String MENU_TYPE = "menuType";
    private static final String MENU_LABEL = "menuLabel";

    private final static String FEATURE_NAME = "name";

    @Override
    public List<SyncCenterFeatureDocument> retrieveAllFeatures(Boolean isActive) {
        List<Criteria> list = new ArrayList<>();
        list.add(Criteria.where(IS_ACTIVE).is(isActive));
        return mongoGenericDao.findByCriteria(list, SyncCenterFeatureDocument.class);
    }

    @Override
    public List<SyncCenterFeatureDocument> retrieveAllFeaturesByListOfMenuType(List<String> listOfMenuTypes, Boolean isActive, String searchparam) {
        List<Criteria> syncCenterFeatCrit = new ArrayList<>();
        syncCenterFeatCrit.add(Criteria.where(IS_ACTIVE).is(isActive));
        syncCenterFeatCrit.add(Criteria.where(MENU_TYPE).in(listOfMenuTypes));
        if (searchparam != null) {
            syncCenterFeatCrit.add(Criteria.where(MENU_LABEL).regex("^" + searchparam, "i"));
        }
        return mongoGenericDao.findByCriteria(syncCenterFeatCrit, SyncCenterFeatureDocument.class);
    }

    @Override
    public SyncCenterFeatureDocument retireveFeatureByName(String featureName) {
        List<Criteria> list = new ArrayList<>();
        list.add(Criteria.where(FEATURE_NAME).is(featureName));
        return (SyncCenterFeatureDocument) mongoGenericDao.findOneByCriteria(list, SyncCenterFeatureDocument.class);
    }

    @Override
    public Map<Long, String> mapOfFeatureIdWithFeatureName() {
        Map<Long, String> mapOfFeatureIdWithFeatureName = null;
        List<SyncCenterFeatureDocument> allFeatures = this.retrieveAllFeatures(Boolean.TRUE);
        if (!CollectionUtils.isEmpty(allFeatures)) {
            mapOfFeatureIdWithFeatureName = new HashMap<>();
            for (SyncCenterFeatureDocument allFeature : allFeatures) {
                mapOfFeatureIdWithFeatureName.put(allFeature.getId(), allFeature.getName());
            }
        }
        return mapOfFeatureIdWithFeatureName;
    }

}
