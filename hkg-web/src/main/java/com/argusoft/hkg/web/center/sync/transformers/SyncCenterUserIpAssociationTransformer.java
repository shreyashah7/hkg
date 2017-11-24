/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.center.sync.transformers;

import com.argusoft.hkg.sync.center.core.HkUMSyncService;
import static com.argusoft.hkg.sync.xmpp.util.SyncTransferType.ONE_TO_MANY;
import com.argusoft.hkg.sync.xmpp.util.SyncTransformerAdapter;
import com.argusoft.sync.center.model.UMUserIpAssociationDocument;
import java.util.Map;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author shruti
 */
@Service
public class SyncCenterUserIpAssociationTransformer extends SyncTransformerAdapter {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(SyncCenterUserIpAssociationTransformer.class);
 
    @Autowired
    private HkUMSyncService hkUMSyncService;



    @Override
    public int getSyncTransferType() {
        return ONE_TO_MANY;
    }

    @Override
    public void save(Object object) {
        if (object != null && object instanceof UMUserIpAssociationDocument) {
            UMUserIpAssociationDocument document = (UMUserIpAssociationDocument) object;
            UMUserIpAssociationDocument currentDocument = (UMUserIpAssociationDocument) hkUMSyncService.getDocumentById(document.getUserId(), UMUserIpAssociationDocument.class);
            if (currentDocument != null && !isUpdatable(currentDocument.getLastModifiedOn(), document.getLastModifiedOn())) {
                return;
            }
          hkUMSyncService.saveOrUpdateDocument(document);
        }
    }

    @Override
    public boolean isStaticDocumentType() {
        return false;
    }
    public Map<String, Object> getidMap() {
        return null;
    }
}
