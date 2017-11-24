/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.center.sync.transformers;

import com.argusoft.hkg.sync.center.core.SyncCenterUserRoleService;
import com.argusoft.sync.center.model.SyncCenterUserRoleDocument;
import com.argusoft.usermanagement.common.model.UMUserRole;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author shruti
 */
@Service
public class SyncCenterUserRoleTransformer {

    @Autowired
    SyncCenterUserRoleService userRoleService;

    public SyncCenterUserRoleTransformer() {
    }

    public SyncCenterUserRoleService getRoleService() {
        return userRoleService;
    }

    public void setRoleService(SyncCenterUserRoleService roleService) {
        this.userRoleService = roleService;
    }

    public SyncCenterUserRoleDocument converteUserRoleToUserRoleCenterDocument(UMUserRole role) {
        SyncCenterUserRoleDocument roleCenterDocument = null;
        if (role != null) {

            roleCenterDocument = new SyncCenterUserRoleDocument();
            roleCenterDocument.setIsActive(role.getIsActive());
            roleCenterDocument.setIsArchive(role.getIsArchive());
            if (role.getuMUserRolePK() != null) {
                roleCenterDocument.setRoleId(role.getuMUserRolePK().getRole());
                roleCenterDocument.setId(role.getuMUserRolePK().getRole() + "-" + role.getUserId());
            }
            roleCenterDocument.setUserId(role.getuMUserRolePK().getUserobj());
            userRoleService.saveOrUpdate(roleCenterDocument);
        }
        return roleCenterDocument;
    }

    public List<SyncCenterUserRoleDocument> convertUserRoleListToUserRoleCenterList(Set<UMUserRole> uMUserRoles) {
        List<SyncCenterUserRoleDocument> list = null;
        if (uMUserRoles != null) {
            list = new ArrayList<>();
            for (UMUserRole role : uMUserRoles) {
                list.add(converteUserRoleToUserRoleCenterDocument(role));
            }
        }
        return list;
    }
}
