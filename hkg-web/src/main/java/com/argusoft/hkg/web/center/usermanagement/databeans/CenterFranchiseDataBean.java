/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.center.usermanagement.databeans;

/**
 *
 * @author shruti
 */
public class CenterFranchiseDataBean {

    private int id;
    private String franchiseName;
    private String franchiseAdminId;
    private Long franchiseId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFranchiseName() {
        return franchiseName;
    }

    public void setFranchiseName(String franchiseName) {
        this.franchiseName = franchiseName;
    }

    public String getFranchiseAdminId() {
        return franchiseAdminId;
    }

    public void setFranchiseAdminId(String franchiseAdminId) {
        this.franchiseAdminId = franchiseAdminId;
    }

    public Long getFranchiseId() {
        return franchiseId;
    }

    public void setFranchiseId(Long franchiseId) {
        this.franchiseId = franchiseId;
    }

    @Override
    public String toString() {
        return "CenterFranchiseDataBean{" + "id=" + id + ", franchiseName=" + franchiseName + ", franchiseAdminId=" + franchiseAdminId + ", franchiseId=" + franchiseId + '}';
    }

}
