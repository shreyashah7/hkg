/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.center.sync.transformers;

import com.argusoft.hkg.core.HkNotificationService;
import com.argusoft.hkg.model.HkNotificationConfigrationRecipientEntity;
import com.argusoft.hkg.model.HkNotificationConfigurationEntity;
import com.argusoft.hkg.sync.center.core.HkUMSyncService;
import com.argusoft.hkg.sync.xmpp.util.SyncHelper;
import com.argusoft.hkg.sync.xmpp.util.SyncTransferType;
import com.argusoft.hkg.sync.xmpp.util.SyncTransformerAdapter;
import com.argusoft.sync.center.model.HkNotificationConfigrationRecipientDocument;
import com.argusoft.sync.center.model.HkNotificationConfigurationDocument;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

/**
 *
 * @author akta
 */
@Service
public class HkNotificationConfigrationRecipientTransformer extends SyncTransformerAdapter {

    @Autowired
    HkUMSyncService hkUMSyncService;

    @Autowired
    ApplicationContext applicationContext;

    private final Map<String, List<String>> queryParametersMap;
    private final Map<String, Object> idMap;

    public HkNotificationConfigrationRecipientTransformer() {
        this.idMap = new HashMap<>();
        this.queryParametersMap = new HashMap<>();
    }

    @Override
    public void save(Object object) {
        if (object != null && object instanceof HkNotificationConfigrationRecipientDocument) {
            HkNotificationConfigrationRecipientDocument notificConfigRecNew = (HkNotificationConfigrationRecipientDocument) object;
            HkNotificationConfigrationRecipientDocument notificConfigRecExisting = (HkNotificationConfigrationRecipientDocument) hkUMSyncService.getDocumentById(notificConfigRecNew.getId(), HkNotificationConfigrationRecipientDocument.class);
            if (notificConfigRecExisting != null && !isUpdatable(notificConfigRecExisting.getLastModifiedOn(), notificConfigRecNew.getLastModifiedOn())) {
                return;
            }
            hkUMSyncService.saveOrUpdateDocument(notificConfigRecNew);
        }
    }

    @Override
    public Object convertEntityToDocument(Object entityObject) {
        HkNotificationConfigrationRecipientEntity entity = ((HkNotificationConfigrationRecipientEntity) entityObject);
        idMap.put(SyncHelper.encodeMapKeyWithDot("hkNotificationConfigrationRecipientEntityPK.notificationConfiguration"), entity.getHkNotificationConfigrationRecipientEntityPK().getNotificationConfiguration());
        idMap.put(SyncHelper.encodeMapKeyWithDot("hkNotificationConfigrationRecipientEntityPK.referenceInstance"), entity.getHkNotificationConfigrationRecipientEntityPK().getReferenceInstance());
        idMap.put(SyncHelper.encodeMapKeyWithDot("hkNotificationConfigrationRecipientEntityPK.referenceType"), entity.getHkNotificationConfigrationRecipientEntityPK().getReferenceType());
        HkNotificationConfigrationRecipientDocument notificConfigRecDoc = new HkNotificationConfigrationRecipientDocument();
        HkNotificationService notificationService = applicationContext.getBean(HkNotificationService.class);
        HkNotificationConfigurationEntity notificationConfig = notificationService.retrieveNotificationCnfigurationById(entity.getHkNotificationConfigrationRecipientEntityPK().getNotificationConfiguration());
        queryParametersMap.put(SyncHelper.FRANCHISE_ID, Arrays.asList(String.valueOf(notificationConfig.getFranchise())));
        notificConfigRecDoc.setHkNotificationConfigurationDocument(new HkNotificationConfigurationDocument(entity.getHkNotificationConfigrationRecipientEntityPK().getNotificationConfiguration()));
        notificConfigRecDoc.setId(entity.getHkNotificationConfigrationRecipientEntityPK().getNotificationConfiguration() + "-"
                + entity.getHkNotificationConfigrationRecipientEntityPK().getReferenceInstance() + "-"
                + entity.getHkNotificationConfigrationRecipientEntityPK().getReferenceType());
        notificConfigRecDoc.setIsArchive(entity.getIsArchive());
        notificConfigRecDoc.setReferenceInstance(entity.getHkNotificationConfigrationRecipientEntityPK().getReferenceInstance());
        notificConfigRecDoc.setReferenceType(entity.getHkNotificationConfigrationRecipientEntityPK().getReferenceType());
        notificConfigRecDoc.setLastModifiedOn(new Date());
        return notificConfigRecDoc;
    }

    @Override
    public Map<String, List<String>> getQueryParameters() {
        return Collections.unmodifiableMap(queryParametersMap);
    }

    @Override
    public int getSyncTransferType() {
        return SyncTransferType.ONE_TO_MANY;
    }

    @Override
    public Map<String, Object> getidMap() {
        return Collections.unmodifiableMap(idMap);
    }

}
