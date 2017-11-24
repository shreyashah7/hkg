/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.center.sync.transformers;

import com.argusoft.hkg.sync.center.core.HkUMSyncService;
import com.argusoft.hkg.sync.xmpp.util.SyncTransferType;
import com.argusoft.hkg.sync.xmpp.util.SyncTransformerAdapter;
import com.argusoft.hkg.sync.xmppclient.SyncXmppClient;
import com.argusoft.hkg.web.center.usermanagement.databeans.CenterFranchiseDataBean;
import com.argusoft.hkg.web.center.util.ApplicationUtil;
import com.argusoft.sync.center.model.VcardDocument;
import com.google.gson.Gson;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author shruti
 */
@Service
public class SyncVcardTransformer extends SyncTransformerAdapter {

    @Autowired
    HkUMSyncService hkUMSyncService;

    @Autowired
    private ApplicationUtil applicationUtil;

    @Autowired
    private SyncXmppClient xmppClient;

    private final Map<String, Object> idMap;
    private Gson gson;

    public SyncVcardTransformer() {
        this.idMap = new HashMap<>();
        gson = new Gson();
//          final JsonSerializer<Object> dateSerializer = (Date src, Type typeOfSrc, JsonSerializationContext context) -> src == null ? null : new JsonPrimitive(src.getTime());
    }

    @Override
    public void save(Object object) {
        if (object != null && object instanceof VcardDocument) {
            VcardDocument document = (VcardDocument) object;
            CenterFranchiseDataBean centerFranchiseDataBean = applicationUtil.getCenterFranchiseDataBean();
            if (centerFranchiseDataBean != null && !document.getId().equals(centerFranchiseDataBean.getFranchiseId().toString())) {
                VcardDocument currentDocument = (VcardDocument) hkUMSyncService.getDocumentById(document.getId(), VcardDocument.class);
                if (currentDocument != null && !isUpdatable(currentDocument.getLastModifiedOn(), document.getLastModifiedOn())) {
                    return;
                }
                if (xmppClient.createRosterEntry(document.getJid().split("@")[0])) {
                    document.setIsRosterEntryCreated(true);
                } else {
                    document.setIsRosterEntryCreated(false);
                }
                hkUMSyncService.saveOrUpdateDocument(document);
            }
        }

    }

    @Override
    public Object convertEntityToDocument(Object entityObject) {
        VcardDocument document = null;
        if (entityObject != null && entityObject instanceof VcardDocument) {
            document = (VcardDocument) entityObject;
            idMap.put("id", document.getId());
        }
        return document;
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
