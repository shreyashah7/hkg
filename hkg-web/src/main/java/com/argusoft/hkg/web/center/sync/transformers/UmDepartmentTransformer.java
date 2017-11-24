/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.center.sync.transformers;

import com.argusoft.hkg.sync.center.core.HkUMSyncService;
import com.argusoft.hkg.sync.xmpp.util.HkSyncConstantUtil;
import com.argusoft.hkg.sync.xmpp.util.SyncHelper;
import com.argusoft.hkg.sync.xmpp.util.SyncTransferType;
import com.argusoft.hkg.sync.xmpp.util.SyncTransformerAdapter;
import com.argusoft.sync.center.model.HkDepartmentDocument;
import com.argusoft.usermanagement.common.model.UMDepartment;
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
public class UmDepartmentTransformer extends SyncTransformerAdapter {

    @Autowired
    HkUMSyncService hkUMSyncService;

    private final Map<String, List<String>> queryParametersMap;
    private final Map<String, Object> idMap;

    public UmDepartmentTransformer() {
        this.idMap = new HashMap<>();
        this.queryParametersMap = new HashMap<>();
    }

    @Override
    public void save(Object object) {
        if (object != null && object instanceof HkDepartmentDocument) {
            HkDepartmentDocument document = (HkDepartmentDocument) object;
            HkDepartmentDocument currentDocument = (HkDepartmentDocument) hkUMSyncService.getDocumentById(document.getId(), HkDepartmentDocument.class);
            if (currentDocument != null && !isUpdatable(currentDocument.getLastModifiedOn(), document.getLastModifiedOn())) {
                return;
            }

            hkUMSyncService.saveOrUpdateDocument(document);
        }
    }

    @Override
    public Object convertEntityToDocument(Object entityObject) {
        Object documentObject = null;
        if (HkSyncConstantUtil.ENTITY_FIELD_MAP.containsKey(entityObject.getClass())) {
            documentObject = super.convertEntityToDocument(entityObject, HkDepartmentDocument.class, null);
        }
        idMap.put("id", ((UMDepartment) entityObject).getId());
        if (((UMDepartment) entityObject).getCompany().equals(0l)) {
            queryParametersMap.put(SyncHelper.FRANCHISE_ID, null);
        } else {
            queryParametersMap.put(SyncHelper.FRANCHISE_ID, Arrays.asList(((UMDepartment) entityObject).getCompany().toString()));
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
