/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.center.sync.transformers;

import com.argusoft.hkg.model.HkGoalTemplateEntity;
import com.argusoft.hkg.sync.center.core.HkUMSyncService;
import com.argusoft.hkg.sync.xmpp.util.SyncHelper;
import com.argusoft.hkg.sync.xmpp.util.SyncTransferType;
import com.argusoft.hkg.sync.xmpp.util.SyncTransformerAdapter;
import com.argusoft.sync.center.model.HkGoalTemplateDocument;
import com.google.gson.Gson;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author shruti
 */
@Service
public class HkGoalTemplateTransformer extends SyncTransformerAdapter {

    @Autowired
    HkUMSyncService hkUMSyncService;

    private final Map<String, List<String>> queryParametersMap;
    private final Map<String, Object> idMap;

    public HkGoalTemplateTransformer() {
        this.queryParametersMap = new HashMap<>();
        this.idMap = new HashMap<>();
    }

    @Override
    public void save(Object object) {
        if (object != null && object instanceof HkGoalTemplateDocument) {
            HkGoalTemplateDocument document = (HkGoalTemplateDocument) object;
            HkGoalTemplateDocument currentDocument = (HkGoalTemplateDocument) hkUMSyncService.getDocumentById(document.getId(), HkGoalTemplateDocument.class);
            if (currentDocument != null && !isUpdatable(currentDocument.getLastModifiedOn(), document.getLastModifiedOn())) {
                return;
            }

            hkUMSyncService.saveOrUpdateDocument(document);
        }

    }

    @Override
    public Object convertEntityToDocument(Object entityObject) {
        HkGoalTemplateDocument documentObject = null;
        if (entityObject instanceof HkGoalTemplateEntity) {
            HkGoalTemplateEntity goalTemplateEntity = (HkGoalTemplateEntity) entityObject;
            queryParametersMap.put(SyncHelper.FRANCHISE_ID, Arrays.asList((goalTemplateEntity.getFranchise()).toString()));
            idMap.put("id", goalTemplateEntity.getId());
            Gson gson = new Gson();
            documentObject = gson.fromJson(gson.toJson(entityObject), HkGoalTemplateDocument.class);
            
        }
        
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
}
