/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.center.sync.transformers;

import com.argusoft.hkg.model.HkCaratRangeEntity;
import com.argusoft.hkg.sync.center.core.HkUMSyncService;
import com.argusoft.hkg.sync.xmpp.util.SyncHelper;
import com.argusoft.hkg.sync.xmpp.util.SyncTransferType;
import com.argusoft.hkg.sync.xmpp.util.SyncTransformerAdapter;
import com.argusoft.sync.center.model.HkCaratRangeDocument;
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
public class HkCaratRangeTransformer extends SyncTransformerAdapter {

    @Autowired
    HkUMSyncService hkUMSyncService;

    private final Map<String, List<String>> queryParametersMap;
    private final Map<String, Object> idMap;

    public HkCaratRangeTransformer() {
        this.queryParametersMap = new HashMap<>();
        this.idMap = new HashMap<>();
    }

    @Override
    public void save(Object object) {
        if (object != null && object instanceof HkCaratRangeDocument) {
            HkCaratRangeDocument document = (HkCaratRangeDocument) object;
            HkCaratRangeDocument currentDocument = (HkCaratRangeDocument) hkUMSyncService.getDocumentById(document.getId(), HkCaratRangeDocument.class);
            if (currentDocument != null && !isUpdatable(currentDocument.getLastModifiedOn(), document.getLastModifiedOn())) {
                return;
            }

            hkUMSyncService.saveOrUpdateDocument(document);
        }

    }

    @Override
    public Object convertEntityToDocument(Object entityObject) {
        HkCaratRangeDocument documentObject = null;
        if (entityObject instanceof HkCaratRangeEntity) {
            HkCaratRangeEntity caratRangeEntity = (HkCaratRangeEntity) entityObject;
            idMap.put("id", caratRangeEntity.getId());
            if (caratRangeEntity.getFranchise() == 0l) {
                queryParametersMap.put(SyncHelper.FRANCHISE_ID, null);
            } else {
                queryParametersMap.put(SyncHelper.FRANCHISE_ID, Arrays.asList(((Long) caratRangeEntity.getFranchise()).toString()));
            }
            Gson gson = new Gson();
            documentObject = gson.fromJson(gson.toJson(entityObject), HkCaratRangeDocument.class);

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
