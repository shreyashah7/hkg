/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.center.sync.transformers;

import com.argusoft.hkg.sync.center.core.HkUMSyncService;
import com.argusoft.hkg.sync.xmpp.util.SyncHelper;
import com.argusoft.hkg.sync.xmpp.util.SyncTransferType;
import com.argusoft.hkg.sync.xmpp.util.SyncTransformerAdapter;
import com.argusoft.hkg.web.center.util.ApplicationUtil;
import com.argusoft.hkg.web.center.util.CenterApplicationInitializer;
import com.argusoft.sync.center.model.CenterCompanyFeatureSectionDocument;
import com.argusoft.sync.center.model.CenterCustomFieldDocument;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author akta
 */
@Service
public class HkCustomFieldTransformer extends SyncTransformerAdapter {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(HkCustomFieldTransformer.class);
    private Map<String, List<String>> queryParametersMap;
    @Autowired
    private HkUMSyncService hkUMSyncService;
    @Autowired
    private CenterApplicationInitializer centerApplicationUtil;
    @Autowired
    private ApplicationUtil applicationUtil;
    private  Map<String, Object> idMap;

    @PostConstruct
    public void init() {
        queryParametersMap = new HashMap<>();
        this.idMap = new HashMap<>();
    }

    @Override
    public void save(Object object) {
        if (object != null && object instanceof CenterCustomFieldDocument) {
            CenterCustomFieldDocument centerCustomFieldDocumentNew = (CenterCustomFieldDocument) object;
            CenterCompanyFeatureSectionDocument centerCompanyFeatureSectionDocument = (CenterCompanyFeatureSectionDocument) hkUMSyncService.getDocumentById(1, CenterCompanyFeatureSectionDocument.class);
            if (centerCompanyFeatureSectionDocument != null && !isUpdatable(centerCompanyFeatureSectionDocument.getModifiedOn(), centerCustomFieldDocumentNew.getLastModifiedOn())) {
                return;
            }
            if (centerCompanyFeatureSectionDocument != null) {
                Map<String, Map<String, Object>> featureSectionMap = centerCompanyFeatureSectionDocument.getFeatureSectionMap();
                if (featureSectionMap == null) {
                    featureSectionMap = new HashMap<>();
                }
                Map<String, Object> sectionFieldMap = featureSectionMap.get(centerCustomFieldDocumentNew.getFeature());
                if (sectionFieldMap == null) {
                    sectionFieldMap = new HashMap<>();
                    featureSectionMap.put(centerCustomFieldDocumentNew.getFeature(), sectionFieldMap);
                }
                sectionFieldMap.put(centerCustomFieldDocumentNew.getSectionName(), centerCustomFieldDocumentNew.getObject());
                hkUMSyncService.saveOrUpdateDocument(centerCompanyFeatureSectionDocument);
                applicationUtil.setCenterFeatureFromTemplateMap(centerCompanyFeatureSectionDocument.getFeatureSectionMap());
                centerApplicationUtil.updateCustomFieldVersion();
            }
        }
    }

    @Override
    public Object convertEntityToDocument(Object entityObject) {
        CenterCustomFieldDocument customFieldDocument = (CenterCustomFieldDocument) entityObject;
        if (customFieldDocument.getFranchise().equals(0l)) {
            queryParametersMap.put(SyncHelper.FRANCHISE_ID, null);
        } else {
            queryParametersMap.put(SyncHelper.FRANCHISE_ID, Arrays.asList(customFieldDocument.getFranchise().toString()));
        }
        return customFieldDocument;
    }

    @Override
    public Map<String, List<String>> getQueryParameters() {
        return Collections.unmodifiableMap(queryParametersMap);
    }

    @Override
    public int getSyncTransferType() {
        return SyncTransferType.ONE_TO_MANY;
    }

    @Override
    public Map<String, Object> getidMap() {
        return null;
    }

}
