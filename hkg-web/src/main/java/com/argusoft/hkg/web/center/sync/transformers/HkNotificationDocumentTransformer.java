/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.center.sync.transformers;

import com.argusoft.hkg.model.HkNotificationEntity;
import com.argusoft.hkg.model.HkNotificationRecipientEntity;
import com.argusoft.hkg.model.HkNotificationRecipientEntityPK;
import com.argusoft.hkg.sync.center.core.HkUMSyncService;
import com.argusoft.hkg.sync.xmpp.util.SyncTransferType;
import com.argusoft.hkg.sync.xmpp.util.SyncTransformerAdapter;
import com.argusoft.sync.center.model.HkNotificationRecipientDocument;
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
public class HkNotificationDocumentTransformer extends SyncTransformerAdapter {

    @Autowired
    private HkUMSyncService hkUMSyncService;

    private final Map<String, List<String>> queryParametersMap;
    private final Map<String, Object> idMap;

    public HkNotificationDocumentTransformer() {
        this.idMap = new HashMap<>();
        this.queryParametersMap = new HashMap<>();
    }

    @Override
    public void save(Object object) {
        if (object != null && object instanceof HkNotificationRecipientDocument) {
            HkNotificationRecipientDocument document = (HkNotificationRecipientDocument) object;
            HkNotificationRecipientDocument currentDocument = (HkNotificationRecipientDocument) hkUMSyncService.getDocumentById(document.getId(), HkNotificationRecipientDocument.class);
            if (currentDocument != null && !isUpdatable(currentDocument.getLastModifiedOn(), document.getLastModifiedOn())) {
                return;
            }
            hkUMSyncService.saveOrUpdateDocument(document);
        }
    }

    @Override
    public Object convertEntityToDocument(Object entityObject) {
        HkNotificationRecipientDocument entity = ((HkNotificationRecipientDocument) entityObject);
        idMap.put("id", entity.getId());
//        entity.getHkNotificationEntity().setHkNotificationRecipientEntitySet(null);
//        Gson gson = new Gson();
//        gson.toJsonTree(entityObject, HkNotificationRecipientEntity.class).getAsJsonObject().get("hkNotificationEntity").getAsJsonObject().remove("hkNotificationRecipientEntitySet");
//        documentObject = gson.fromJson(gson.toJson(((HkNotificationRecipientEntity) entityObject)), HkNotificationRecipientDocument.class);
        HkNotificationRecipientEntity recipient = (HkNotificationRecipientEntity) super.convertEntityToDocument(entity, HkNotificationRecipientEntity.class, null);
        recipient.setHkNotificationRecipientEntityPK((HkNotificationRecipientEntityPK) super.convertEntityToDocument(entity.getHkNotificationRecipientEntityPK(), HkNotificationRecipientEntityPK.class, null));
        recipient.setHkNotificationEntity(new HkNotificationEntity(entity.getHkNotificationEntity().getId()));
        return recipient;
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
