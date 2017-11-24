/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.center.sync.transformers;

import com.argusoft.hkg.model.HkLeaveEntity;
import com.argusoft.hkg.sync.center.core.HkUMSyncService;
import com.argusoft.hkg.sync.xmpp.util.HkSyncConstantUtil;
import com.argusoft.hkg.sync.xmpp.util.SyncHelper;
import com.argusoft.hkg.sync.xmpp.util.SyncTransferType;
import com.argusoft.hkg.sync.xmpp.util.SyncTransformerAdapter;
import com.argusoft.sync.center.model.HkLeaveDocument;
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
public class HkLeaveTransformer extends SyncTransformerAdapter {

    @Autowired
    private HkUMSyncService hkUMSyncService;

    private final Map<String, List<String>> queryParametersMap;
    private final Map<String, Object> idMap;

    public HkLeaveTransformer() {
        this.idMap = new HashMap<>();
        this.queryParametersMap = new HashMap<>();
    }

    @Override
    public void save(Object object) {
        if (object != null && object instanceof HkLeaveDocument) {
            HkLeaveDocument document = (HkLeaveDocument) object;
            HkLeaveDocument currentDocument = (HkLeaveDocument) hkUMSyncService.getDocumentById(document.getId(), HkLeaveDocument.class);
            if (currentDocument != null && !isUpdatable(currentDocument.getCreatedOn(), document.getCreatedOn())) {
                return ;
            }

            hkUMSyncService.saveOrUpdateDocument(document);
        }
    }

    public HkLeaveDocument retrieveById(Long id,HkLeaveDocument hkMasterValueDocument) {
        return (HkLeaveDocument) hkUMSyncService.getDocumentById(id,HkLeaveDocument.class);
    }

    @Override
    public Object convertEntityToDocument(Object entityObject) {
        Object documentObject = null;
        idMap.put("id", ((HkLeaveEntity) entityObject).getId());
        if (HkSyncConstantUtil.ENTITY_FIELD_MAP.containsKey(entityObject.getClass())) {
            documentObject = super.convertEntityToDocument(entityObject, HkLeaveDocument.class,null);
        }
        queryParametersMap.put(SyncHelper.FRANCHISE_ID, Arrays.asList("" + ((HkLeaveEntity) entityObject).getFranchise()));
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
