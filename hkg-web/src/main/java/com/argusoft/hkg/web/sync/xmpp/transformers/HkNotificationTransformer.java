/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.sync.xmpp.transformers;

import com.argusoft.hkg.core.HkNotificationService;
import com.argusoft.hkg.model.HkNotificationRecipientEntity;
import com.argusoft.hkg.sync.HkSyncSqlGenericService;
import com.argusoft.hkg.sync.xmpp.util.SyncHelper;
import com.argusoft.hkg.sync.xmpp.util.SyncTransferType;
import com.argusoft.hkg.sync.xmpp.util.SyncTransformerAdapter;
import com.argusoft.sync.center.model.HkNotificationDocument;
import com.argusoft.sync.center.model.HkNotificationRecipientDocument;
import com.argusoft.sync.center.model.HkNotificationRecipientDocumentPK;
import com.argusoft.usermanagement.common.core.UMUserService;
import com.argusoft.usermanagement.common.exception.UMUserManagementException;
import com.argusoft.usermanagement.common.model.UMUser;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author shruti
 */
@Service
public class HkNotificationTransformer extends SyncTransformerAdapter {

    private final Map<String, List<String>> queryParametersMap;
    private final Map<String, Object> idMap;

    @Autowired
    UMUserService uMUserService;

    @Autowired
    HkNotificationService hkNotificationService;

    @Autowired
    HkSyncSqlGenericService hkSyncSqlGenericService;

    public HkNotificationTransformer() {
        this.idMap = new HashMap<>();
        this.queryParametersMap = new HashMap<>();
    }

    @Override
    public void save(Object object) {
        if (object != null && object instanceof HkNotificationRecipientEntity) {
            HkNotificationRecipientEntity entity = (HkNotificationRecipientEntity) object;
            HkNotificationRecipientEntity notificationRecipientEntityExisting = hkNotificationService.retrieveNotificationRecEntity(entity.getHkNotificationRecipientEntityPK());
            if (notificationRecipientEntityExisting != null && !isUpdatable(notificationRecipientEntityExisting.getLastModifiedOn(), entity.getLastModifiedOn())) {
                return;
            }
            entity.setHkNotificationEntity(null);
//            System.out.println(" entity.getHkNotificationEntity() " + entity.getHkNotificationEntity());
            hkSyncSqlGenericService.updateEntity(object);
        }

    }

    @Override
    public Object convertEntityToDocument(Object entityObject) {
        HkNotificationRecipientEntity entity = ((HkNotificationRecipientEntity) entityObject);
        long forUser = entity.getHkNotificationRecipientEntityPK().getForUser();
        try {
            idMap.put(SyncHelper.encodeMapKeyWithDot("hkNotificationRecipientEntityPK.notification"), entity.getHkNotificationRecipientEntityPK().getNotification());
            idMap.put(SyncHelper.encodeMapKeyWithDot("hkNotificationRecipientEntityPK.forUser"), entity.getHkNotificationRecipientEntityPK().getForUser());
            UMUser userbyId = uMUserService.getUserbyId(forUser, false, false, false, false);
            queryParametersMap.put(SyncHelper.FRANCHISE_ID, Arrays.asList(userbyId.getCompany().toString()));
        } catch (UMUserManagementException ex) {
            Logger.getLogger(HkNotificationTransformer.class.getName()).log(Level.SEVERE, null, ex);
        }
//        entity.getHkNotificationEntity().setHkNotificationRecipientEntitySet(null);
//        Gson gson = new Gson();
//        gson.toJsonTree(entityObject, HkNotificationRecipientEntity.class).getAsJsonObject().get("hkNotificationEntity").getAsJsonObject().remove("hkNotificationRecipientEntitySet");
//        documentObject = gson.fromJson(gson.toJson(((HkNotificationRecipientEntity) entityObject)), HkNotificationRecipientDocument.class);
        HkNotificationRecipientDocument recipient = (HkNotificationRecipientDocument) super.convertEntityToDocument(entity, HkNotificationRecipientDocument.class, null);
        recipient.setHkNotificationRecipientEntityPK((HkNotificationRecipientDocumentPK) super.convertEntityToDocument(entity.getHkNotificationRecipientEntityPK(), HkNotificationRecipientDocumentPK.class, null));
        recipient.setHkNotificationEntity((HkNotificationDocument) super.convertEntityToDocument(entity.getHkNotificationEntity(), HkNotificationDocument.class, null));
        recipient.setId(entity.getHkNotificationRecipientEntityPK().getForUser() + "-" + entity.getHkNotificationRecipientEntityPK().getNotification());
        return recipient;
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
