/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.center.sync.transformers;

import com.argusoft.hkg.sync.center.core.HkUMSyncService;
import com.argusoft.hkg.sync.xmpp.util.SyncTransferType;
import com.argusoft.hkg.sync.xmpp.util.SyncTransformerAdapter;
import com.argusoft.hkg.web.center.security.WebSecurityNoSqlUtil;
import com.argusoft.sync.center.model.SyncCenterRoleFeatureDocument;
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
public class SyncCenterRoleFeatureTransformer extends SyncTransformerAdapter {

    @Autowired
    private HkUMSyncService hkUMSyncService;

    @Autowired
    private WebSecurityNoSqlUtil webSecurityNoSqlUtil;
    Map<String, Object> idMap = new HashMap<>();

    private static final String ID = "id";

    @Override
    public Object convertEntityToDocument(Object entityObject) {
        return null;
    }

    @Override
    public int getSyncTransferType() {
        return SyncTransferType.NONE;
    }

    @Override
    public void save(Object object) {
        if (object != null && object instanceof SyncCenterRoleFeatureDocument) {
            SyncCenterRoleFeatureDocument document = (SyncCenterRoleFeatureDocument) object;
            SyncCenterRoleFeatureDocument currentDocument = (SyncCenterRoleFeatureDocument) hkUMSyncService.getDocumentById(1l, SyncCenterRoleFeatureDocument.class);
            if (currentDocument != null && !isUpdatable(currentDocument.getModifiedOn(), document.getModifiedOn())) {
                return;
            }
            hkUMSyncService.saveOrUpdateDocument(document);
            webSecurityNoSqlUtil.init();
        }
    }

    @Override
    public boolean isStaticDocumentType() {
        return true;
    }

    @Override
    public Map<String, List<String>> getQueryParameters() {
        return null;
    }

    @Override
    public Map<String, Object> getidMap() {
        return idMap;
    }

}
