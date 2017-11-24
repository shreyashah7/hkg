/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.argusoft.hkg.sync.center.core;

import com.argusoft.sync.center.model.UmDesignationDocument;
import java.util.List;

/**
 *
 * @author gautam
 */
public interface SyncRoleService {
    
    public static final String ID = "id";
    
    public List<UmDesignationDocument> retrieveRolesByIds(List<Long> roleIds);
    
}
