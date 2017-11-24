/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.center.sync.transformers;

import com.argusoft.hkg.nosql.model.HkParcelDocument;
import com.argusoft.hkg.sync.center.core.HkUMSyncService;
import com.argusoft.hkg.sync.xmpp.util.SyncHelper;
import com.argusoft.hkg.sync.xmpp.util.SyncTransferType;
import com.argusoft.hkg.sync.xmpp.util.SyncTransformerAdapter;
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
public class HkParcelTransformer extends SyncTransformerAdapter {

    @Autowired
    HkUMSyncService hkUMSyncService;
   
    private final Map<String, List<String>> queryParametersMap;
    private final Map<String, Object> idMap;

    public HkParcelTransformer() {
        this.idMap = new HashMap<>();
        this.queryParametersMap = new HashMap<>();
    }

    @Override
    public void save(Object object) {
        if (object != null && object instanceof HkParcelDocument) {
            HkParcelDocument document = (HkParcelDocument) object;
            HkParcelDocument currentDocument = (HkParcelDocument) hkUMSyncService.getDocumentById(document.getId(), HkParcelDocument.class);
            if (currentDocument != null && !isUpdatable(currentDocument.getLastModifiedOn(), document.getLastModifiedOn())) {
                return ;
            }
            hkUMSyncService.saveOrUpdateDocument(document);
        }
        
    }

    @Override
    public Object convertEntityToDocument(Object entityObject) {
        HkParcelDocument document = null;
        if (entityObject != null && entityObject instanceof HkParcelDocument) {
            document = (HkParcelDocument) entityObject;
            idMap.put("id", document.getId());
            queryParametersMap.put(SyncHelper.FRANCHISE_ID, Arrays.asList(document.getFranchiseId().toString()));
        }
        return document;
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
