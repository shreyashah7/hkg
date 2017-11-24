/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.sync.center.core;

import com.argusoft.sync.center.model.SyncCenterRoleFeatureDocument;




/**
 *
 * @author shruti
 */
public interface SyncCenterRoleFeatureService {

    /**
     * This method is use to create/update
     *
     * @author shruti
     * @param document object of SyncCenterRoleFeatureDocument
     * @return id of Integer
     */
    public Long saveOrUpdate(SyncCenterRoleFeatureDocument document);

    /**
     * This method is used to retrieve
     *
     * @author shruti
     * @param id of document
     * @return List of SyncCenterRoleFeatureDocument or null
     */
    public SyncCenterRoleFeatureDocument retrieveById(Long id);

}
