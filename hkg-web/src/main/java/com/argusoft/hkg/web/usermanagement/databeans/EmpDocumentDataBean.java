/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.usermanagement.databeans;

/**
 *
 * @author mansi
 */
public class EmpDocumentDataBean {

    private Long id;
    private String documentName;
    private String documentType;
    //UMContactDocument->remarks
    private String notes;
    private String documentFileData;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getDocumentFileData() {
        return documentFileData;
    }

    public void setDocumentFileData(String documentFileData) {
        this.documentFileData = documentFileData;
    }
    
}
