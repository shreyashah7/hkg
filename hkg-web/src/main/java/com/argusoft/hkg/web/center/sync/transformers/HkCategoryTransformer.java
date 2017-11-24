/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.center.sync.transformers;

import com.argusoft.hkg.model.HkCategoryEntity;
import com.argusoft.hkg.sync.center.core.HkUMSyncService;
import com.argusoft.hkg.sync.xmpp.util.SyncHelper;
import com.argusoft.hkg.sync.xmpp.util.SyncTransferType;
import com.argusoft.hkg.sync.xmpp.util.SyncTransformerAdapter;
import com.argusoft.sync.center.model.HkCategoryDocument;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author akta
 */
@Service
public class HkCategoryTransformer extends SyncTransformerAdapter {

    @Autowired
    private HkUMSyncService hkUMSyncService;

    private final Map<String, List<String>> queryParametersMap;
    private final Map<String, Object> idMap;
    Gson gson;

    public HkCategoryTransformer() {
        this.idMap = new HashMap<>();
        this.queryParametersMap = new HashMap<>();
        gson = new GsonBuilder().setExclusionStrategies(new ExclusionStrategy() {

            @Override
            public boolean shouldSkipField(FieldAttributes arg0) {
                if (arg0.getName().equals("parent")) {
                    return true;
                }
                return false;
            }

            @Override
            public boolean shouldSkipClass(Class<?> arg0) {
                return false;
            }
        }).create();
    }

    @Override
    public void save(Object object) {
        if (object != null && object instanceof HkCategoryDocument) {
            HkCategoryDocument categoryNew = (HkCategoryDocument) object;
            HkCategoryDocument categoryExisting = (HkCategoryDocument) hkUMSyncService.getDocumentById(categoryNew.getId(), HkCategoryDocument.class);
            if (categoryExisting != null && !isUpdatable(categoryExisting.getLastModifiedOn(), categoryNew.getLastModifiedOn())) {
                return;
            }
            hkUMSyncService.saveOrUpdateDocument(categoryNew);
        }
    }

    @Override
    public Object convertEntityToDocument(Object entityObject) {
        HkCategoryEntity entity = ((HkCategoryEntity) entityObject);
        idMap.put("id", entity.getId());
        HkCategoryDocument categoryDoc = gson.fromJson(gson.toJson(entityObject), HkCategoryDocument.class);
        if (entity.getParent() != null) {
            HkCategoryDocument categoryDocument = new HkCategoryDocument();
            categoryDocument.setId(entity.getParent().getId());
            categoryDoc.setParent(categoryDocument);
        }
        queryParametersMap.put(SyncHelper.FRANCHISE_ID, Arrays.asList(String.valueOf(entity.getFranchise())));
        return categoryDoc;
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
