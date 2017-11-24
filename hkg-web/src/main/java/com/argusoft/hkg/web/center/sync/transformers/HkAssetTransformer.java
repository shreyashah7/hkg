/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.center.sync.transformers;

import com.argusoft.hkg.model.HkAssetEntity;
import com.argusoft.hkg.model.HkAssetIssueEntity;
import com.argusoft.hkg.model.HkCategoryEntity;
import com.argusoft.hkg.sync.center.core.HkUMSyncService;
import com.argusoft.hkg.sync.xmpp.util.SyncHelper;
import com.argusoft.hkg.sync.xmpp.util.SyncTransferType;
import com.argusoft.hkg.sync.xmpp.util.SyncTransformerAdapter;
import com.argusoft.sync.center.model.HkAssetDocument;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author akta
 */
@Service
public class HkAssetTransformer extends SyncTransformerAdapter {

    @Autowired
    private HkUMSyncService hkUMSyncService;

    private final Map<String, List<String>> queryParametersMap;
    private final Map<String, Object> idMap;
    Gson gson;

    public HkAssetTransformer() {
        this.idMap = new HashMap<>();
        this.queryParametersMap = new HashMap<>();
        gson = new GsonBuilder().setExclusionStrategies(new ExclusionStrategy() {

            @Override
            public boolean shouldSkipField(FieldAttributes arg0) {
                return false;
            }

            @Override
            public boolean shouldSkipClass(Class<?> arg0) {
                if (arg0.equals(Set.class) || arg0.equals(HkCategoryEntity.class) || arg0.equals(HkAssetIssueEntity.class)) {
                    return true;
                }
                return false;
            }
        }).create();
    }

    @Override
    public void save(Object object) {
        if (object != null && object instanceof HkAssetDocument) {
            HkAssetDocument messageNew = (HkAssetDocument) object;
            HkAssetDocument messageExisting = (HkAssetDocument) hkUMSyncService.getDocumentById(messageNew.getId(), HkAssetDocument.class);
            if (messageExisting != null && !isUpdatable(messageExisting.getLastModifiedOn(), messageNew.getLastModifiedOn())) {
                return;
            }
            hkUMSyncService.saveOrUpdateDocument(messageNew);
        }
    }

    @Override
    public Object convertEntityToDocument(Object entityObject) {
        HkAssetEntity entity = ((HkAssetEntity) entityObject);
        idMap.put("id", entity.getId());
        HkAssetDocument msgDoc = gson.fromJson(gson.toJson(entityObject), HkAssetDocument.class);
        queryParametersMap.put(SyncHelper.FRANCHISE_ID, Arrays.asList(String.valueOf(entity.getFranchise())));
        return msgDoc;
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

}
