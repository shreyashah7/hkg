/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.center.sync.transformers;

import com.argusoft.hkg.common.functionutil.FolderManagement;
import com.argusoft.hkg.sync.center.core.HkUMSyncService;
import com.argusoft.hkg.sync.xmpp.util.SyncHelper;
import com.argusoft.hkg.sync.xmpp.util.SyncTransferType;
import com.argusoft.hkg.sync.xmpp.util.SyncTransformerAdapter;
import com.argusoft.sync.center.model.SyncFileTransferDocument;
import com.google.gson.Gson;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import java.io.IOException;
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
 * @author akta
 */
@Service
public class SyncFileTransferDocumentTransformer extends SyncTransformerAdapter {

    @Autowired
    HkUMSyncService hkUMSyncService;

    private final Map<String, List<String>> queryParametersMap;
    private final Map<String, Object> idMap;
    Gson gson;

    public SyncFileTransferDocumentTransformer() {
        this.idMap = new HashMap<>();
        this.queryParametersMap = new HashMap<>();
        gson = new Gson();
    }

    @Override
    public void save(Object object) {
        System.out.println("==== SyncFileTransferDocument ========" + (object != null) + "   " + (object instanceof SyncFileTransferDocument));
        if (object != null && object instanceof SyncFileTransferDocument) {
            try {
                SyncFileTransferDocument document = (SyncFileTransferDocument) object;
                SyncFileTransferDocument currentDocument = (SyncFileTransferDocument) hkUMSyncService.getDocumentById(document.getId(), SyncFileTransferDocument.class);
                if (currentDocument != null && !isUpdatable(currentDocument.getLastModifiedOn(), document.getLastModifiedOn())) {
                    return;
                }
                byte[] fileData = Base64.decode(document.getFileData());
                String filePath = FolderManagement.getPathOfFileForSync(document.getFileName());
                System.out.println("File Path :  " + filePath);
                System.out.println("file Data : " + fileData);
                FolderManagement.writeByteData(filePath, fileData, false);
                document.setFileData(null);
                hkUMSyncService.saveOrUpdateDocument(document);
            } catch (IOException ex) {
                Logger.getLogger(SyncFileTransferDocumentTransformer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    @Override
    public Object convertEntityToDocument(Object entityObject) {

        SyncFileTransferDocument syncFileTransferDocument = (SyncFileTransferDocument) entityObject;
//        if (WebApplicationInitializerConfig.IS_MASTER) {
//            hkUMSyncService.removeFileTransferDocument(syncFileTransferDocument);
//        } else {
        try {
            System.out.println("syncFileTransferDocument.getFileName() " + syncFileTransferDocument.getFileName());
            String filePath = FolderManagement.getPathOfImage(syncFileTransferDocument.getFileName());

            System.out.println("==== filePath : " + filePath);

            byte[] fileData = FolderManagement.getFileDataFromDirectPath(filePath);

            System.out.println(" ==== Byte : " + fileData);
            if (fileData != null) {
                syncFileTransferDocument.setFileData(Base64.encode(fileData));
            }

            queryParametersMap.put(SyncHelper.FRANCHISE_ID, Arrays.asList("0"));
            idMap.put("id", syncFileTransferDocument.getId());
            return syncFileTransferDocument;
        } catch (IOException ex) {
            Logger.getLogger(SyncFileTransferDocumentTransformer.class.getName()).log(Level.SEVERE, null, ex);
        }
//        }
        return null;
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
