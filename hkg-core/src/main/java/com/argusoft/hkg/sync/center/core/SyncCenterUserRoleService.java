/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.sync.center.core;

import com.argusoft.sync.center.model.SyncCenterUserRoleDocument;




/**
 *
 * @author shruti
 */
public interface SyncCenterUserRoleService {

    /**
     * This method is use to create/update
     *
     * @author shruti
     * @param document object of HkCoatedRoughDocument
     *
     */
    public void saveOrUpdate(SyncCenterUserRoleDocument document);

    /**
     * This method is use to retrieve All
     *
     * @author shruti
     * @param franchise id of franchise
     * @param isActive status
     * @return List of HkCoatedRoughDocument
     */
    public SyncCenterUserRoleDocument retrieve();

}
