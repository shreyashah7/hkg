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
import com.argusoft.sync.center.model.UmDesignationDocument;
import com.argusoft.usermanagement.common.model.UMRole;
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
public class UmDesignationTransformer extends SyncTransformerAdapter {

    @Autowired
    HkUMSyncService hkUmSyncService;

    private final Map<String, List<String>> queryParametersMap;
    private final Map<String, Object> idMap;

    public UmDesignationTransformer() {
        this.idMap = new HashMap<>();
        this.queryParametersMap = new HashMap<>();
    }

    @Override
    public void save(Object object) {
        if (object != null && object instanceof UmDesignationDocument) {
            UmDesignationDocument centerDesignationNew = (UmDesignationDocument) object;
            UmDesignationDocument centerDesignationExisting = hkUmSyncService.retrieveDesignationById(centerDesignationNew.getId());
            if (centerDesignationExisting != null && !isUpdatable(centerDesignationExisting.getModifiedOn(), centerDesignationNew.getModifiedOn())) {
                return;
            }
            hkUmSyncService.saveDesignation((UmDesignationDocument) object);
        }
    }

    @Override
    public Object convertEntityToDocument(Object entityObject) {
        Object centerDesignationDoc;
        idMap.put("id", ((UMRole) entityObject).getId());
        if (HkSyncConstantUtil.ENTITY_FIELD_MAP.containsKey(entityObject.getClass())) {
            centerDesignationDoc = (UmDesignationDocument) super.convertEntityToDocument(entityObject, UmDesignationDocument.class, null);
        } else {
            centerDesignationDoc = entityObject;
        }
        if (((UMRole) entityObject).getCompany().equals(0l)) {
            queryParametersMap.put(SyncHelper.FRANCHISE_ID, null);
        } else {
            queryParametersMap.put(SyncHelper.FRANCHISE_ID, Arrays.asList(((UMRole) entityObject).getCompany().toString()));
        }
        return centerDesignationDoc;
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
