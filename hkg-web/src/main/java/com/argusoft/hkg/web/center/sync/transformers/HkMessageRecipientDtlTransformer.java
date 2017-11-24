/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.center.sync.transformers;

import com.argusoft.hkg.core.HkNotificationService;
import com.argusoft.hkg.model.HkMessageEntity;
import com.argusoft.hkg.model.HkMessageRecipientDtlEntity;
import com.argusoft.hkg.sync.center.core.HkUMSyncService;
import com.argusoft.hkg.sync.xmpp.util.SyncHelper;
import com.argusoft.hkg.sync.xmpp.util.SyncTransferType;
import com.argusoft.hkg.sync.xmpp.util.SyncTransformerAdapter;
import com.argusoft.sync.center.model.HkMessageDocument;
import com.argusoft.sync.center.model.HkMessageRecipientDtlDocument;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.Arrays;
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
public class HkMessageRecipientDtlTransformer extends SyncTransformerAdapter {

    @Autowired
    private HkUMSyncService hkUMSyncService;

    private final Map<String, List<String>> queryParametersMap;
    private final Map<String, Object> idMap;
    Gson gson;

    @Autowired
    ApplicationContext applicationContext;

    public HkMessageRecipientDtlTransformer() {
        this.idMap = new HashMap<>();
        this.queryParametersMap = new HashMap<>();
        gson = new GsonBuilder().setExclusionStrategies(new ExclusionStrategy() {

            @Override
            public boolean shouldSkipField(FieldAttributes arg0) {
                return false;
            }

            @Override
            public boolean shouldSkipClass(Class<?> arg0) {
                if (arg0.equals(HkMessageEntity.class)) {
                    return true;
                }
                return false;
            }
        }).create();
    }

    @Override
    public void save(Object object) {
        if (object != null && object instanceof HkMessageRecipientDtlDocument) {
            HkMessageRecipientDtlDocument sectionNew = (HkMessageRecipientDtlDocument) object;
            HkMessageRecipientDtlDocument sectionExisting = (HkMessageRecipientDtlDocument) hkUMSyncService.getDocumentById(sectionNew.getId(), HkMessageRecipientDtlDocument.class);
            if (sectionExisting != null && !isUpdatable(sectionExisting.getLastModifiedOn(), sectionNew.getLastModifiedOn())) {
                return;
            }
            hkUMSyncService.saveOrUpdateDocument(sectionNew);
        }
    }

    @Override
    public Object convertEntityToDocument(Object entityObject) {
        HkMessageRecipientDtlEntity entity = ((HkMessageRecipientDtlEntity) entityObject);
        idMap.put(SyncHelper.encodeMapKeyWithDot("hkMessageRecipientDtlPK.messageObj"), entity.getHkMessageRecipientDtlPK().getMessageObj());
        idMap.put(SyncHelper.encodeMapKeyWithDot("hkMessageRecipientDtlPK.messageTo"), entity.getHkMessageRecipientDtlPK().getMessageTo());
        HkMessageRecipientDtlDocument msgRecDtlDoc = gson.fromJson(gson.toJson(entityObject), HkMessageRecipientDtlDocument.class);
        msgRecDtlDoc.setLastModifiedOn(new Date());
        HkNotificationService notificationService = applicationContext.getBean(HkNotificationService.class);
        HkMessageEntity messageEntity = notificationService.retrieveMessageById(entity.getHkMessageRecipientDtlPK().getMessageObj(), false);
        queryParametersMap.put(SyncHelper.FRANCHISE_ID, Arrays.asList(messageEntity.getFranchise().toString()));
        HkMessageDocument hkMessageDocument = new HkMessageDocument();
        hkMessageDocument.setId(entity.getHkMessageRecipientDtlPK().getMessageObj());
        msgRecDtlDoc.setHkMessage(hkMessageDocument);
        msgRecDtlDoc.setLastModifiedOn(new Date());
        msgRecDtlDoc.setId(entity.getHkMessageRecipientDtlPK().getMessageObj() + "-" + entity.getHkMessageRecipientDtlPK().getMessageTo());
        return msgRecDtlDoc;
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
