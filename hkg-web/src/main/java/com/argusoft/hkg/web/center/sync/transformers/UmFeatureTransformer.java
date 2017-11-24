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
import com.argusoft.hkg.web.usermanagement.databeans.FeatureDataBean;
import com.argusoft.sync.center.model.SyncCenterFeatureDocument;
import com.argusoft.usermanagement.common.model.UMFeature;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 *
 * @author shruti
 */
@Service
public class UmFeatureTransformer extends SyncTransformerAdapter {

    @Autowired
    HkUMSyncService hkUMSyncService;

    private final Map<String, List<String>> queryParametersMap;
    private final Map<String, Object> idMap;
    Gson gson;

    @PostConstruct
    public void init() {
        gson = new GsonBuilder().setExclusionStrategies(new ExclusionStrategy() {

            @Override
            public boolean shouldSkipField(FieldAttributes arg0) {
                if (arg0.getDeclaredClass().equals(UMFeature.class)) {
                    return true;
                }
                return false;
            }

            @Override
            public boolean shouldSkipClass(Class<?> arg0) {
                if (arg0.equals(Set.class)) {
                    return true;
                }
                return false;
            }
        }).create();
    }

    public UmFeatureTransformer() {
        this.idMap = new HashMap<>();
        this.queryParametersMap = new HashMap<>();
    }

    @Override
    public void save(Object object) {
        if (object != null && object instanceof SyncCenterFeatureDocument) {
            SyncCenterFeatureDocument document = (SyncCenterFeatureDocument) object;
            SyncCenterFeatureDocument currentDocument = (SyncCenterFeatureDocument) hkUMSyncService.getDocumentById(document.getId(), SyncCenterFeatureDocument.class);
            if (currentDocument != null && !isUpdatable(currentDocument.getModifiedOn(), document.getModifiedOn())) {
                return;
            }

            hkUMSyncService.saveOrUpdateDocument(document);
        }
    }

    @Override
    public Object convertEntityToDocument(Object entityObject) {
        SyncCenterFeatureDocument documentObject = gson.fromJson(gson.toJson(entityObject), SyncCenterFeatureDocument.class);
        idMap.put("id", ((UMFeature) entityObject).getId());
        queryParametersMap.put(SyncHelper.FRANCHISE_ID, Arrays.asList(((UMFeature) entityObject).getCompany().toString()));
        return documentObject;
    }

    @Override
    public Map<String, List<String>> getQueryParameters() {
        return queryParametersMap;
    }

    @Override
    public int getSyncTransferType() {
        return SyncTransferType.ONE_TO_MANY;
    }

    @Override
    public Map<String, Object> getidMap() {
        return idMap;
    }

    public List<SyncCenterFeatureDocument> convertFeatureDatabeanListToDocumentList(List<FeatureDataBean> dataBeans) {
        List<SyncCenterFeatureDocument> documents = null;
        if (!CollectionUtils.isEmpty(dataBeans)) {
            documents = new LinkedList<>();
            for (FeatureDataBean dataBean : dataBeans) {
                SyncCenterFeatureDocument fromJson = gson.fromJson(gson.toJson(dataBean), SyncCenterFeatureDocument.class);
                documents.add(fromJson);
                System.out.println("Databean= " + dataBean + "   " + fromJson);
            }
        }
        return documents;
    }
}
