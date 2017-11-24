/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.center.sync.transformers;

import com.argusoft.hkg.model.HkNotificationConfigurationEntity;
import com.argusoft.hkg.sync.center.core.HkUMSyncService;
import com.argusoft.hkg.sync.xmpp.util.SyncHelper;
import com.argusoft.hkg.sync.xmpp.util.SyncTransferType;
import com.argusoft.hkg.sync.xmpp.util.SyncTransformerAdapter;
import com.argusoft.sync.center.model.HkNotificationConfigurationDocument;
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
public class HkNotificationConfigurationTransformer extends SyncTransformerAdapter {

    @Autowired
    HkUMSyncService hkUMSyncService;

    private final Map<String, List<String>> queryParametersMap;
    private final Map<String, Object> idMap;

    public HkNotificationConfigurationTransformer() {
        this.idMap = new HashMap<>();
        this.queryParametersMap = new HashMap<>();
    }

    @Override
    public void save(Object object) {
        if (object != null && object instanceof HkNotificationConfigurationDocument) {
            HkNotificationConfigurationDocument notificConfigNew = (HkNotificationConfigurationDocument) object;
            HkNotificationConfigurationDocument notificConfigExisting = (HkNotificationConfigurationDocument) hkUMSyncService.getDocumentById(notificConfigNew.getId(), HkNotificationConfigurationDocument.class);
            if (notificConfigExisting != null && !isUpdatable(notificConfigExisting.getLastModifiedOn(), notificConfigNew.getLastModifiedOn())) {
                return;
            }
            hkUMSyncService.saveOrUpdateDocument(notificConfigNew);
        }
    }

    @Override
    public Object convertEntityToDocument(Object entityObject) {
        HkNotificationConfigurationEntity entity = ((HkNotificationConfigurationEntity) entityObject);
        idMap.put("id", entity.getId());
        HkNotificationConfigurationDocument notificConfigDoc = (HkNotificationConfigurationDocument) super.convertEntityToDocument(entity, HkNotificationConfigurationDocument.class, null);
        queryParametersMap.put(SyncHelper.FRANCHISE_ID, Arrays.asList(String.valueOf(entity.getFranchise())));
        return notificConfigDoc;
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
