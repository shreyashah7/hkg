/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.assets.databeans;

/**
 *
 * @author dhwani
 */
public class FileDataBean {

    private Long id;
    private String fileName;
    private String filePath;
    private Boolean archiveStatus;

    public Boolean isArchiveStatus() {
        return archiveStatus;
    }

    public void setArchiveStatus(Boolean archiveStatus) {
        this.archiveStatus = archiveStatus;
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

}
