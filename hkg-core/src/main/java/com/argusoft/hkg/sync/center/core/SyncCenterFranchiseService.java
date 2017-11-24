/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.sync.center.core;


import com.argusoft.sync.center.model.SyncCenterFranchiseDocument;

/**
 *
 * @author shruti
 */
public interface SyncCenterFranchiseService {

    /**
     * This method is use to create/update
     *
     * @author shruti
     * @param document object of SyncCenterFranchiseDocument
     * @return id of document
     */
    public Long saveOrUpdate(SyncCenterFranchiseDocument document);

    /**
     * This method is used to retrieveById
     *
     * @author shruti
     * @param id of document
     * @return List of SyncCenterFranchiseDocument or null
     */
    public SyncCenterFranchiseDocument retrieveById(Long id);
}
