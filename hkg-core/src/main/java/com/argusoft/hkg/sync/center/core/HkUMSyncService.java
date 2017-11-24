/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.sync.center.core;

import com.argusoft.sync.center.model.SyncFileTransferDocument;
import com.argusoft.sync.center.model.UmDesignationDocument;
import com.argusoft.sync.center.model.VcardDocument;
import java.util.List;
import org.bson.types.ObjectId;

/**
 *
 * @author shruti
 */
public interface HkUMSyncService {

    /**
     * This method is used to create/update
     *
     * @author harshit
     * @param document object of sync document
     * @return
     */
    public void saveOrUpdateDocument(Object document);
    
    public UmDesignationDocument retrieveDesignationById(Long id);

    public Long saveDesignation(UmDesignationDocument designationDocument);

    /**
     * * This method is used to retrieve document details by id.
     *
     * @author shruti
     * @param id
     * @param documentClass
     * @return
     */
    public Object getDocumentById(Object id, Class<?> documentClass);

    public List<? extends Object> retrieveAllDocuments(Class<?> documentClass);
    
    public void saveOrUpdateFileTransferDocument(SyncFileTransferDocument fileTransferDocument);
    
    public void removeFileTransferDocument(SyncFileTransferDocument fileTransferDocument);

    public Object retrieveObjectsFromMetadata(ObjectId metadataId);

    public void saveOrUpdateVcardDocument(VcardDocument vcardDocument);

    public List<VcardDocument> getRemainingVcardDocuments();

    public long getCountofActiveDocuments(Class class1, String isArchiveFieldName, String isActiveFieldName);
    public void saveOrUpdateDocumentTransactional(Object object);

    public List<?> getDocumentsWithLimit(long offset, long limit, Class<?> documentClass);
}
