/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.center.sync.transformers;

import com.argusoft.hkg.model.HkValueEntity;
import com.argusoft.hkg.sync.center.core.HkUMSyncService;
import com.argusoft.hkg.sync.xmpp.util.HkSyncConstantUtil;
import com.argusoft.hkg.sync.xmpp.util.SyncHelper;
import com.argusoft.hkg.sync.xmpp.util.SyncTransferType;
import com.argusoft.hkg.sync.xmpp.util.SyncTransformerAdapter;
import com.argusoft.hkg.web.center.util.ApplicationUtil;
import com.argusoft.sync.center.model.HkMasterValueDocument;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author harshit
 */
@Service
public class SyncMasterValueTransformer extends SyncTransformerAdapter {

    @Autowired
    private HkUMSyncService hkUMSyncService;

    private final Map<String, List<String>> queryParametersMap;
    private final Map<String, Object> idMap;

    @Autowired
    private ApplicationUtil applicationUtil;

    public SyncMasterValueTransformer() {
        this.idMap = new HashMap<>();
        this.queryParametersMap = new HashMap<>();
    }

    @Override
    public void save(Object object) {
        if (object != null && object instanceof HkMasterValueDocument) {
            HkMasterValueDocument document = (HkMasterValueDocument) object;
            if (document.getFranchise() != 0l && document.getFranchise() != applicationUtil.getCenterFranchiseDataBean().getFranchiseId()) {
                document.setIsArchive(true);
            }
            HkMasterValueDocument currentDocument = (HkMasterValueDocument) hkUMSyncService.getDocumentById(document.getId(), HkMasterValueDocument.class);
            if (currentDocument != null && !isUpdatable(currentDocument.getLastModifiedOn(), document.getLastModifiedOn())) {
                return;
            }

            hkUMSyncService.saveOrUpdateDocument(document);
        }
    }

    public HkMasterValueDocument retrieveById(Long id, HkMasterValueDocument hkMasterValueDocument) {
        return (HkMasterValueDocument) hkUMSyncService.getDocumentById(id, HkMasterValueDocument.class);
    }

    @Override
    public Object convertEntityToDocument(Object entityObject) {
        idMap.put("id", ((HkValueEntity) entityObject).getId());
        long franchise = ((HkValueEntity) entityObject).getFranchise();
        System.out.println("franchise: " + franchise + " comp: " + (franchise == 0l));
            queryParametersMap.put(SyncHelper.FRANCHISE_ID, null);
        HkMasterValueDocument documentObject = null;
        if (HkSyncConstantUtil.ENTITY_FIELD_MAP.containsKey(entityObject.getClass())) {
            documentObject = (HkMasterValueDocument) super.convertEntityToDocument(entityObject, HkMasterValueDocument.class, null);
            documentObject.setCode(((HkValueEntity) entityObject).getKeyCode().getCode());
        }
        return documentObject;
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
