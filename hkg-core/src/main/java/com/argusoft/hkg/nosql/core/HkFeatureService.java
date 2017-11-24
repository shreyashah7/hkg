/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.nosql.core;

import com.argusoft.sync.center.model.SyncCenterFeatureDocument;
import java.util.List;
import java.util.Map;

/**
 *
 * @author akta
 */
public interface HkFeatureService {

    public List<SyncCenterFeatureDocument> retrieveAllFeatures(Boolean isActive);

    public List<SyncCenterFeatureDocument> retrieveAllFeaturesByListOfMenuType(List<String> listOfMenuTypes, Boolean isActive, String searchparam);

    public SyncCenterFeatureDocument retireveFeatureByName(String featureName);

    public Map<Long, String> mapOfFeatureIdWithFeatureName();
}
