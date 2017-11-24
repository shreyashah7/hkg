/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.center.sync.transformers;

import com.argusoft.hkg.nosql.model.HkRuleSetDocument;
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
public class HkRuleTransformer extends SyncTransformerAdapter {

    @Autowired
    HkUMSyncService hkUMSyncService;
    private final Map<String, List<String>> queryParametersMap;
    private final Map<String, Object> idMap;

    public HkRuleTransformer() {
        this.queryParametersMap = new HashMap<>();
        this.idMap = new HashMap<>();
    }

    @Override
    public void save(Object object) {
        if (object != null && object instanceof HkRuleSetDocument) {
            HkRuleSetDocument document = (HkRuleSetDocument) object;
            HkRuleSetDocument currentDocument = (HkRuleSetDocument) hkUMSyncService.getDocumentById(document.getId(), HkRuleSetDocument.class);
            if (currentDocument != null && !isUpdatable(currentDocument.getLastModifiedOn(), document.getLastModifiedOn())) {
                return ;
            }

            hkUMSyncService.saveOrUpdateDocument(document);
        }
        
    }

    @Override
    public Object convertEntityToDocument(Object entityObject) {
        HkRuleSetDocument document = (HkRuleSetDocument) entityObject;
        idMap.put("id", document.getId());
        queryParametersMap.put(SyncHelper.FRANCHISE_ID, Arrays.asList(document.getFranchise().toString()));
        return entityObject;
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
