/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.center.transformers;

import com.argusoft.hkg.web.center.usermanagement.databeans.MetadataDatabean;
import com.argusoft.sync.center.model.MetadataDocument;
import java.util.LinkedList;
import java.util.List;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

/**
 *
 * @author shruti
 */
@Service
public class MetadataTransformer {

    public MetadataDatabean convertMetadataDocumentToMetadataDataBean(MetadataDocument document, MetadataDatabean databean) {
        if (databean == null) {
            databean = new MetadataDatabean();
        }
        databean.setClassName(document.getClassName());
        databean.setCreatedOn(document.getCreatedOn());
        databean.setId(document.getId().toString());
        databean.setIdMap(document.getIdMap());
        databean.setIsArchive(document.isIsArchive());
        databean.setModifiedOn(document.getModifiedOn());
        databean.setStatus(document.getStatus());
        databean.setIsSqlEntity(document.isIsSqlEntity());
        return databean;
    }
    public MetadataDocument convertMetadataDatabeanToMetadataDocument(MetadataDatabean metadataDatabean, MetadataDocument document) {
        if (document == null) {
            document = new MetadataDocument();
        }
        document.setClassName(metadataDatabean.getClassName());
        document.setCreatedOn(metadataDatabean.getCreatedOn());
        document.setId(new ObjectId(metadataDatabean.getId()));
        document.setIdMap(metadataDatabean.getIdMap());
        document.setIsArchive(metadataDatabean.isIsArchive());
        document.setModifiedOn(metadataDatabean.getModifiedOn());
        document.setStatus(metadataDatabean.getStatus());
        document.setIsSqlEntity(metadataDatabean.isIsSqlEntity());
        return document;
    }

    public List<MetadataDatabean> convertMetadataDocumentListToDataBeanList(List<MetadataDocument> documents) {
        List<MetadataDatabean> databeans = null;
        if (documents != null) {
            databeans = new LinkedList<>();
            for (MetadataDocument document : documents) {
                databeans.add(convertMetadataDocumentToMetadataDataBean(document, null));
            }
        }
        return databeans;
    }
}
