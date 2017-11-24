/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.sync.listner;

import com.argusoft.sync.center.model.MetadataDocument;

/**
 *
 * @author brijesh
 */
public class SyncRecordedEntity {

    private static Long hkRecordedEntityid = 1l;

    public SyncRecordedEntity() {
        this.id = hkRecordedEntityid;
        hkRecordedEntityid++;
    }

    private Long id;
    private String entityName;
    private Object entity;
    private boolean isDirty;
    private MetadataDocument metadataDocument;
    public Long getId() {
        return id;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public Object getEntity() {
        return entity;
    }

    public void setEntity(Object entity) {
        this.entity = entity;
    }

    public boolean isIsDirty() {
        return isDirty;
    }

    public void setIsDirty(boolean isDirty) {
        this.isDirty = isDirty;
    }

    public static Long getHkRecordedEntityid() {
        return hkRecordedEntityid;
    }

    public static void setHkRecordedEntityid(Long hkRecordedEntityid) {
        SyncRecordedEntity.hkRecordedEntityid = hkRecordedEntityid;
    }

    public MetadataDocument getMetadataDocument() {
        return metadataDocument;
    }

    public void setMetadataDocument(MetadataDocument metadataDocument) {
        this.metadataDocument = metadataDocument;
    }

    @Override
    public String toString() {
        return "HkRecordedEntity{" + "id=" + id + ", entityName=" + entityName + ", entity=" + entity + ", isDirty=" + isDirty + '}';
    }

}
