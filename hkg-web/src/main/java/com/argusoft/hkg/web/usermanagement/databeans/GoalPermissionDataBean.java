package com.argusoft.hkg.web.usermanagement.databeans;

/**
 *
 * @author rajkumar
 */
public class GoalPermissionDataBean {

    private Long id;
    private Long designation;
    private String referenceType;
    private String referenceInstance;
    private String accessOfFeature;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDesignation() {
        return designation;
    }

    public void setDesignation(Long designation) {
        this.designation = designation;
    }

    public String getReferenceType() {
        return referenceType;
    }

    public void setReferenceType(String referenceType) {
        this.referenceType = referenceType;
    }

    public String getAccessOfFeature() {
        return accessOfFeature;
    }

    public void setAccessOfFeature(String accessOfFeature) {
        this.accessOfFeature = accessOfFeature;
    }

    public String getReferenceInstance() {
        return referenceInstance;
    }

    public void setReferenceInstance(String referenceInstance) {
        this.referenceInstance = referenceInstance;
    }
    
    @Override
    public String toString() {
        return "GoalPermissionDataBean{" + "id=" + id + ", designation=" + designation + ", referenceType=" + referenceType + ", referenceInstance=" + referenceInstance + ", accessOfFeature=" + accessOfFeature + '}';
    }

}
