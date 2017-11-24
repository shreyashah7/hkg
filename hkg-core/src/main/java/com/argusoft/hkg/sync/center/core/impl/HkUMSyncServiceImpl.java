/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.sync.center.core.impl;

import com.argusoft.generic.core.mongotransaction.MongoTransaction;
import com.argusoft.generic.database.common.MongoGenericDao;
import com.argusoft.hkg.common.functionutil.HkSystemFunctionUtil;
import com.argusoft.hkg.sync.center.core.HkUMSyncService;
import com.argusoft.sync.center.model.MetadataDocument;
import com.argusoft.sync.center.model.SyncFileTransferDocument;
import com.argusoft.sync.center.model.UmDesignationDocument;
import com.argusoft.sync.center.model.VcardDocument;
import static java.lang.Class.forName;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.query.Criteria;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 *
 * @author harshit
 */
@Service
public class HkUMSyncServiceImpl implements HkUMSyncService {
    private static final String IS_ARCHIVE = "isArchive";
    @Autowired
    private MongoGenericDao mongoGenericDao;

    @Override
    public void saveOrUpdateDocument(Object document) {
        mongoGenericDao.update(document);
    }

    @Override
    public UmDesignationDocument retrieveDesignationById(Long id) {
        List<Criteria> criterias = new ArrayList<>();
        criterias.add(Criteria.where("id").is(id));
        return (UmDesignationDocument) mongoGenericDao.findOneByCriteria(criterias, UmDesignationDocument.class);
    }

    @Override
    public Long saveDesignation(UmDesignationDocument designationDocument) {
        mongoGenericDao.update(designationDocument);
        return (designationDocument != null) ? designationDocument.getId() : null;
    }

    @Override
    public Object getDocumentById(Object id, Class<?> documentClass) {
        String collectionName = documentClass.getAnnotation(Document.class).collection();
        if (StringUtils.isEmpty(collectionName)) {
            return mongoGenericDao.getMongoTemplate().findById(id, documentClass);
        } else {
            return mongoGenericDao.getMongoTemplate().findById(id, documentClass, collectionName);
        }
    }

    @Override
    public List<? extends Object> retrieveAllDocuments(Class<?> documentClass) {
        String collectionName = documentClass.getAnnotation(Document.class).collection();
        if (StringUtils.isEmpty(collectionName)) {
            return mongoGenericDao.getMongoTemplate().findAll((Class<? extends Object>) documentClass);
        } else {
            return mongoGenericDao.getMongoTemplate().findAll((Class<? extends Object>) documentClass, collectionName);
        }
    }

    @Override
    public Object retrieveObjectsFromMetadata(ObjectId metadataId) {
        MetadataDocument metadataDocument = (MetadataDocument) getDocumentById(metadataId, MetadataDocument.class);
        List<Criteria> criterias = new LinkedList<>();
        for (Map.Entry<String, Object> entrySet : metadataDocument.getIdMap().entrySet()) {
//            System.out.println("entrySet.getKey() " + entrySet.getKey() + "   " + entrySet.getValue().getClass() + "  " + entrySet.getValue().toString());
            criterias.add(Criteria.where(HkSystemFunctionUtil.decodeMapKeyWithDot(entrySet.getKey())).is(new ObjectId(entrySet.getValue().toString())));
        }
        Object findByCriteria = null;
        try {
            findByCriteria = mongoGenericDao.findOneByCriteria(criterias, forName(metadataDocument.getClassName()));
            System.out.println(metadataDocument.getClassName() + "" + findByCriteria);
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(HkUMSyncServiceImpl.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return findByCriteria;
    }

    @Override
    @MongoTransaction
    public void saveOrUpdateFileTransferDocument(SyncFileTransferDocument fileTransferDocument) {
        mongoGenericDao.update(fileTransferDocument);
    }

    @Override
    @MongoTransaction
    public void saveOrUpdateVcardDocument(VcardDocument vcardDocument) {
        mongoGenericDao.update(vcardDocument);
    }

    @Override
    public void removeFileTransferDocument(SyncFileTransferDocument fileTransferDocument) {
        mongoGenericDao.delete(fileTransferDocument);
    }

    @Override
    public List<VcardDocument> getRemainingVcardDocuments() {
        List<Criteria> criterias = new ArrayList<>();
        criterias.add(Criteria.where("isRosterEntryCreated").is(false));
        List vCards = mongoGenericDao.findByCriteria(criterias, VcardDocument.class);
        return (vCards != null) ? vCards : null;
    }

    @Override
    public long getCountofActiveDocuments(Class class1, String isArchiveFieldName, String isActiveFieldName) {
        List<Criteria> criterias = new LinkedList<>();
        criterias.add(new Criteria().andOperator(where(isActiveFieldName).is(true), where(isArchiveFieldName).is(false)));
        return mongoGenericDao.count(criterias, class1);
    }

    @MongoTransaction
    @Override
    public void saveOrUpdateDocumentTransactional(Object object) {
        mongoGenericDao.update(object);
    }

    @Override
    public List<?> getDocumentsWithLimit(long offset, long limit, Class<?> documentClass) {
        Query query = new Query();
        query.addCriteria(Criteria.where(IS_ARCHIVE).is(false));
        query.skip((int) offset);
        query.limit((int) limit);

        return mongoGenericDao.getMongoTemplate().find(query, documentClass);
        
    }
}
