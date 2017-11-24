/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.center.sync.transformers;

import com.argusoft.hkg.model.HkSectionEntity;
import com.argusoft.hkg.sync.center.core.HkUMSyncService;
import com.argusoft.hkg.sync.xmpp.util.SyncTransferType;
import com.argusoft.hkg.sync.xmpp.util.SyncTransformerAdapter;
import com.argusoft.hkg.web.config.WebApplicationInitializerConfig;
import com.argusoft.sync.center.model.HkSectionDocument;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.Date;
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
public class HkSectionTransformer extends SyncTransformerAdapter {

    @Autowired
    private HkUMSyncService hkUMSyncService;

    private final Map<String, List<String>> queryParametersMap;
    private final Map<String, Object> idMap;
    Gson gson;

    public HkSectionTransformer() {
        this.idMap = new HashMap<>();
        this.queryParametersMap = new HashMap<>();
        gson = new GsonBuilder().setExclusionStrategies(new ExclusionStrategy() {

            @Override
            public boolean shouldSkipField(FieldAttributes arg0) {
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

    @Override
    public void save(Object object) {
        if (object != null && object instanceof HkSectionDocument) {
            HkSectionDocument sectionNew = (HkSectionDocument) object;
            HkSectionDocument sectionExisting = (HkSectionDocument) hkUMSyncService.getDocumentById(sectionNew.getId(), HkSectionDocument.class);
            if (sectionExisting != null && !isUpdatable(sectionExisting.getLastModifiedOn(), sectionNew.getLastModifiedOn())) {
                return;
            }

            if (!WebApplicationInitializerConfig.IS_MASTER) {
                hkUMSyncService.saveOrUpdateDocumentTransactional(sectionNew);
            } else {
                hkUMSyncService.saveOrUpdateDocument(sectionNew);
            }
        }
    }

    @Override
    public Object convertEntityToDocument(Object entityObject) {
        HkSectionEntity entity = ((HkSectionEntity) entityObject);
        idMap.put("id", entity.getId());
        HkSectionDocument sectionDoc = gson.fromJson(gson.toJson(entityObject), HkSectionDocument.class);
        sectionDoc.setLastModifiedOn(new Date());
        return sectionDoc;
    }

    @Override
    public Map<String, List<String>> getQueryParameters() {
        return queryParametersMap;
    }

    @Override
    public int getSyncTransferType() {
        return SyncTransferType.SEND_TO_ALL;
    }

    @Override
    public Map<String, Object> getidMap() {
        return idMap;
    }
}
