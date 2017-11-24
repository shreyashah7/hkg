/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.center.sync.transformers;

import com.argusoft.hkg.nosql.model.HkSubFormValueDocument;
import com.argusoft.hkg.sync.center.core.HkUMSyncService;
import com.argusoft.hkg.sync.xmpp.util.SyncHelper;
import com.argusoft.hkg.sync.xmpp.util.SyncTransferType;
import com.argusoft.hkg.sync.xmpp.util.SyncTransformerAdapter;
import com.argusoft.hkg.web.center.util.ApplicationUtil;
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
public class HkSubFormValueTransformer extends SyncTransformerAdapter {

    private final Map<String, List<String>> queryParametersMap;
    private final Map<String, Object> idMap;

    @Autowired
    private HkUMSyncService hkUMSyncService;

    @Autowired
    private ApplicationUtil applicationUtil;

    public HkSubFormValueTransformer() {
        this.idMap = new HashMap<>();
        this.queryParametersMap = new HashMap<>();
    }

       @Override
    public void save(Object object) {
        if (object != null && object instanceof HkSubFormValueDocument) {
            HkSubFormValueDocument subFormValueDocumentNew = (HkSubFormValueDocument) object;
            HkSubFormValueDocument subFormValueDocumentExisting = (HkSubFormValueDocument) hkUMSyncService.getDocumentById(subFormValueDocumentNew.getId(), HkSubFormValueDocument.class);
            if (subFormValueDocumentNew.getFranchiseId() != 0l && subFormValueDocumentNew.getFranchiseId() != applicationUtil.getCenterFranchiseDataBean().getFranchiseId()) {
                subFormValueDocumentNew.setIsArchive(true);
            }
            if (subFormValueDocumentExisting != null && !isUpdatable(subFormValueDocumentExisting.getModifiedOn(), subFormValueDocumentNew.getModifiedOn())) {
                return;
            }
            hkUMSyncService.saveOrUpdateDocument(subFormValueDocumentNew);
        }
    }

    @Override
    public Object convertEntityToDocument(Object entityObject) {
        HkSubFormValueDocument subFormValueDocument = null;
        if (entityObject != null && entityObject instanceof HkSubFormValueDocument) {
            subFormValueDocument = (HkSubFormValueDocument) entityObject;
            idMap.put("id", subFormValueDocument.getId());
            queryParametersMap.put(SyncHelper.FRANCHISE_ID, null);
        }
        return subFormValueDocument;
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
