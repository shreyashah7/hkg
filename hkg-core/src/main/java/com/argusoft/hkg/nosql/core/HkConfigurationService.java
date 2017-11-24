/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.nosql.core;

import com.argusoft.hkg.nosql.model.HkAssociatedDeptDocument;
import com.argusoft.hkg.nosql.model.HkCriteriaSetDocument;
import com.argusoft.hkg.nosql.model.HkDepartmentConfigDocument;
import com.argusoft.hkg.nosql.model.HkFeatureFieldPermissionDocument;
import com.argusoft.hkg.nosql.model.HkRoleFeatureModifierDocument;
import com.argusoft.sync.center.model.HkSectionDocument;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author shifa
 */
public interface HkConfigurationService {

    /**
     *
     * @param hkDepartmentConfigDocument is the document which needs to be saved
     * @return
     */
    public Long createConfiguration(HkDepartmentConfigDocument hkDepartmentConfigDocument);

    /**
     * This method retrieves document according to departmentId
     *
     * @param depId is the department Id
     * @param companyId
     * @return HkDepartmentConfigDocument document
     */
    public HkDepartmentConfigDocument retrieveDocumentByDepartmentId(Long depId, Long companyId);

    /**
     * Retrieve department list that are already configured.
     *
     * @param companyId
     * @return
     */
    public List<Long> retrieveAvailableDepartmentConfigurations(Long companyId);

    /**
     * This method search all the document whose department name matches with
     * search query
     *
     * @param query is the search string
     * @return list of documents matching with search string
     */
    public List<HkDepartmentConfigDocument> searchConfigurationsByDepartmentName(String query);

    /**
     * This method retrieves mapOfAssociatedDesignationIdWithDocuments
     *
     * @param depId is the department Id
     *
     * @return
     */
//    public Map<Long, List<HkFeatureFieldPermissionDocument>> retrieveMapOfDesignationIdWithListOfpermissionDocument(Long depId, Long companyid);

    /**
     * This method retrieves map of associated Department with dep id as their
     * key and whole associated document as key
     *
     * @param deptId is the departmentId
     * @return
     */
    public Map<Long, HkAssociatedDeptDocument> retrieveMapOfAssociatedDepartmentsForDept(Long deptId);

    /**
     * This method creates or updates HkFeatureFieldPermissionDocument document
     *
     * @param fieldPermissionDocs
     */
    public void createOrUpdateHkPermissionDocumentsForDesignation(List<HkFeatureFieldPermissionDocument> fieldPermissionDocs);

    public Map<Long, HkCriteriaSetDocument> mapOfretrieveRuleListForDepartment(Long depId);

    public Long retrieveMaxCountOfRule(Long companyId);

    // Retrieve stock room,default department,associated department and department id fields only
    public List<HkDepartmentConfigDocument> retrieveConfigDocumentsForRemovingDepartment();

    public void retrieveConfigDocumentsForRemovingDepartment(Long depId);
// retrieve Associated designation

    public List<HkDepartmentConfigDocument> retrieveAssociatedDesignationsFieldForDocuments();

    public void updateAllConfigDocuments(List<HkDepartmentConfigDocument> configDocs);

    public List<HkDepartmentConfigDocument> retrieveAllDepartmentConfigurationDocumentsForProcessFlow(Long companyId);

    /**
     * This method returns featurefieldpermissiondocuments according to feature
     * key and whole associated document as key
     *
     * @param featureId is the featureId     
     * @param roleIds is the list of roleIds
     * @param searchFlag is used to check whether document is being retrieved
     * for search template or not
     * @param companyId id of comapny
     * @param sectionCode code of section
     * @return
     */
    public List<HkFeatureFieldPermissionDocument> retrieveListOfPermissionDocuments(List<Long> roleIds, Long featureId, Boolean searchFlag, Long companyId, String sectionCode);
    // Method made by Shifa to fetch permission document from configuration document as per restructuring

    public Map<HkSectionDocument, List<HkFeatureFieldPermissionDocument>> retrieveFeatureFieldPermissionForSearchFromConfigDocument(Long deptId, Long featureId, List<Long> designationIds, Long companyId);

//    public List<HkFeatureFieldPermissionDocument> retrieveListOfPermissionDocuments(Long department, List<Long> roleIds, Long get);
    
    public void saveRoleFeatureModifiers(List<HkRoleFeatureModifierDocument> roleFeatureModifierDocuments);

    public List<HkRoleFeatureModifierDocument> retrieveModifiersByDesignations(List<Long> designationIds, Long franchiseId, Long featureId);


}
