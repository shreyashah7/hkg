/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.usermanagement.transformers;

import com.argusoft.generic.database.exception.GenericDatabaseException;
import com.argusoft.hkg.common.constantutil.HkSystemConstantUtil;
import com.argusoft.hkg.core.HkFieldService;
import com.argusoft.hkg.nosql.core.HkCustomFieldService;
import com.argusoft.hkg.nosql.model.CustomField;
import com.argusoft.hkg.web.customfield.transformers.CustomFieldTransformerBean;
import com.argusoft.hkg.web.usermanagement.databeans.DepartmentDataBean;
import com.argusoft.hkg.web.center.usermanagement.databeans.LoginDataBean;
import com.argusoft.hkg.web.shift.transformers.ShiftTransformerBean;
import com.argusoft.hkg.web.usermanagement.wrapper.UserManagementServiceWrapper;
import com.argusoft.usermanagement.common.core.UMDepartmentService;
import com.argusoft.usermanagement.common.core.UMUserService;
import com.argusoft.usermanagement.common.model.UMDepartment;
import com.argusoft.usermanagement.common.model.UMUser;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 *
 * @author jyoti
 */
@Service
public class DepartmentTransformerBean {
    
    @Autowired
    UserManagementServiceWrapper userManagementServiceWrapper;
    @Autowired
    UMDepartmentService departmentService;
    
    @Autowired
    UMUserService userService;
    
    @Autowired
    LoginDataBean loginDataBean;
    
    @Autowired
    HkCustomFieldService customFieldSevice;
    @Autowired
    HkFieldService fieldService;
    
    @Autowired
    CustomFieldTransformerBean customFieldTransformerBean;
    
    @Autowired
    ShiftTransformerBean shiftTransformerBean;
    
    @Autowired
    ConfigurationTransformerBean configurationTransformerBean;
    
    List<DepartmentDataBean> sortedhkDepartmentDataBean = null;
    Map<Long, DepartmentDataBean> idToDepartmentDatabeanMap = null;
    
    public List<DepartmentDataBean> retrieveDepartmentListInTreeView(Long companyId) {
        List<UMDepartment> uMDepartmentList = userManagementServiceWrapper.retrieveDepartments(loginDataBean.getCompanyId(), Boolean.FALSE);
        List<DepartmentDataBean> hkTreeViewDataBeans = new ArrayList<>();
        return convertUMDepartmentToHkTreeViewDataBeanDatabean(uMDepartmentList, hkTreeViewDataBeans);
        
    }
    
    private List<DepartmentDataBean> convertUMDepartmentToHkTreeViewDataBeanDatabean(List<UMDepartment> uMDepartments, List<DepartmentDataBean> hkTreeViewDataBeans) {
        
        for (UMDepartment uMDepartment : uMDepartments) {
            if (uMDepartment.getIsActive()) {
                DepartmentDataBean departmentDataBean = new DepartmentDataBean();
                if (departmentDataBean.getDepartmentCustom() == null) {
                    departmentDataBean.setDepartmentCustom(new HashMap<String, Object>());
                }
                departmentDataBean.setId(uMDepartment.getId());
                departmentDataBean.setDisplayName(uMDepartment.getDeptName());
                departmentDataBean.setDeptName(uMDepartment.getDeptName());
                departmentDataBean.setIsActive(uMDepartment.getIsActive());
                departmentDataBean.setDescription(uMDepartment.getDescription());
                departmentDataBean.setShiftRotationDays(uMDepartment.getCustom1());
                departmentDataBean.setCompanyId(uMDepartment.getCompany());
                departmentDataBean.setIsDefaultDep(uMDepartment.getCustom4());
                if (uMDepartment.getParent() == null) {
                    departmentDataBean.setParentId(0L);
                    departmentDataBean.setParentName(HkSystemConstantUtil.NONE);
                } else {
                    departmentDataBean.setParentId(uMDepartment.getParent().getId());
                    departmentDataBean.setParentName(uMDepartment.getParent().getDeptName());
                }
                List<DepartmentDataBean> listChild;
                try {
                    if (!CollectionUtils.isEmpty(uMDepartment.getuMDepartmentSet())) {
                        listChild = new ArrayList<>();
                        Set<UMDepartment> l = uMDepartment.getuMDepartmentSet();
                        List<DepartmentDataBean> l1 = convertUMDepartmentToHkTreeViewDataBeanDatabean(new ArrayList<>(l), listChild);
                        
                        departmentDataBean.setChildren(l1);
                    }
                } catch (Exception e) {
                }
                hkTreeViewDataBeans.add(departmentDataBean);
            }
        }
        
        return hkTreeViewDataBeans;
    }
    
    public List<DepartmentDataBean> retrieveDepartmentListInTreeViewSimple(Long companyId) {
        List<UMDepartment> uMDepartmentList = userManagementServiceWrapper.retrieveDepartments(loginDataBean.getCompanyId(), Boolean.FALSE);
        List<DepartmentDataBean> hkTreeViewDataBeans = new ArrayList<>();
        return convertUMDepartmentToHkTreeViewDataBeanDatabeanSimple(uMDepartmentList, hkTreeViewDataBeans);
    }
    
    private List<DepartmentDataBean> convertUMDepartmentToHkTreeViewDataBeanDatabeanSimple(List<UMDepartment> uMDepartments, List<DepartmentDataBean> hkTreeViewDataBeans) {
        for (UMDepartment uMDepartment : uMDepartments) {
            if (uMDepartment.getIsActive()) {
                DepartmentDataBean departmentDataBean = new DepartmentDataBean();
                departmentDataBean.setId(uMDepartment.getId());
                departmentDataBean.setDisplayName(uMDepartment.getDeptName());
                departmentDataBean.setCompanyId(uMDepartment.getCompany());
                departmentDataBean.setShiftRotationDays(uMDepartment.getCustom1());
                if (uMDepartment.getParent() == null) {
                    departmentDataBean.setParentId(0L);
                    departmentDataBean.setParentName(HkSystemConstantUtil.NONE);
                } else {
                    departmentDataBean.setParentId(uMDepartment.getParent().getId());
                    departmentDataBean.setParentName(uMDepartment.getParent().getDeptName());
                }
                List<DepartmentDataBean> listChild;
                try {
                    if (!CollectionUtils.isEmpty(uMDepartment.getuMDepartmentSet())) {
                        listChild = new ArrayList<>();
                        Set<UMDepartment> l = uMDepartment.getuMDepartmentSet();
                        List<DepartmentDataBean> l1 = convertUMDepartmentToHkTreeViewDataBeanDatabeanSimple(new ArrayList<>(l), listChild);
                        departmentDataBean.setChildren(l1);
                    }
                } catch (Exception e) {
                }
                hkTreeViewDataBeans.add(departmentDataBean);
            }
        }
        
        return hkTreeViewDataBeans;
    }
    
    public List<DepartmentDataBean> retrieveAllDepartmentBySearch(String name) throws GenericDatabaseException {
        List<UMDepartment> departments = userManagementServiceWrapper.searchDepartmentByName(name, loginDataBean.getCompanyId());
        Map<Long, Integer> existingUsers = userManagementServiceWrapper.retrieveUserCountsByDepartment(loginDataBean.getCompanyId());
        List<DepartmentDataBean> departmentDataBeansBySearch = new ArrayList<>();
        return convertUMDepartmentToDepartmentDataBean(departments, departmentDataBeansBySearch, existingUsers);
    }
    
    private List<DepartmentDataBean> convertUMDepartmentToDepartmentDataBean(List<UMDepartment> uMDepartments, List<DepartmentDataBean> hkDepartmentDataBeans, Map<Long, Integer> existingUsers) {
        
        if (hkDepartmentDataBeans == null) {
            hkDepartmentDataBeans = new ArrayList<>();
        }
        
        if (uMDepartments != null) {
            
            for (UMDepartment uMDepartment : uMDepartments) {
                if (uMDepartment.getIsActive()) {
                    DepartmentDataBean departmentDataBean = new DepartmentDataBean();
                    if (!CollectionUtils.isEmpty(existingUsers)) {
                        for (Map.Entry<Long, Integer> entry : existingUsers.entrySet()) {
                            if (uMDepartment.getId().equals(entry.getKey())) {
                                departmentDataBean.setExistingUserCount(entry.getValue());
                            } else {
                                departmentDataBean.setExistingUserCount(0);
                            }
                        }
                    }
                    departmentDataBean.setId(uMDepartment.getId());
                    departmentDataBean.setDescription(uMDepartment.getDescription());
                    departmentDataBean.setShiftRotationDays(uMDepartment.getCustom1());
                    departmentDataBean.setDisplayName(uMDepartment.getDeptName());
                    departmentDataBean.setIsActive(uMDepartment.getIsActive());
                    departmentDataBean.setCompanyId(uMDepartment.getCompany());
                    if (uMDepartment.getParent() == null) {
                        departmentDataBean.setParentId(0L);
                        departmentDataBean.setParentName(HkSystemConstantUtil.NONE);
                    } else {
                        departmentDataBean.setParentId(uMDepartment.getParent().getId());
                        departmentDataBean.setParentName(uMDepartment.getParent().getDeptName());
                    }
                    hkDepartmentDataBeans.add(departmentDataBean);
                }
                
            }
        }
        return hkDepartmentDataBeans;
        
    }
    
    public boolean isDepartmentNameExists(String departmentName, Long id) throws GenericDatabaseException {
        boolean doesExist = userManagementServiceWrapper.isDepartmentNameExist(departmentName, id, loginDataBean.getCompanyId());
        return doesExist;
    }
    
    private UMDepartment convertDepartmentDataBeanToDepartmentEntity(DepartmentDataBean hkDepartmentDatabean, UMDepartment uMDepartment, String categoryType) {
        if (uMDepartment == null) {
            uMDepartment = new UMDepartment();
        }
        uMDepartment.setDeptName(hkDepartmentDatabean.getDisplayName());
        //Make department global.
        uMDepartment.setCompany(0l);
        uMDepartment.setDescription(hkDepartmentDatabean.getDescription());
        uMDepartment.setCustom1(hkDepartmentDatabean.getShiftRotationDays());
        if (uMDepartment.getCustom1() != null && uMDepartment.getCustom2() == null) {
            uMDepartment.setCustom2(new Date());
        }
        uMDepartment.setLastModifiedBy(loginDataBean.getId());
        uMDepartment.setLastModifiedOn(new Date());

//        Set null for root category
        if (hkDepartmentDatabean.getParentId().equals(0l)) {
            uMDepartment.setParent(null);
        } else {
            uMDepartment.setParent(new UMDepartment(hkDepartmentDatabean.getParentId()));
        }
        
        return uMDepartment;
    }
    
    public Long createDepartment(DepartmentDataBean hkDepartmentDatabean) {
        
        UMDepartment uMDepartment = this.convertDepartmentDataBeanToDepartmentEntity(hkDepartmentDatabean, null, HkSystemConstantUtil.CATEGORY_TYPE_EVENT);
        uMDepartment.setIsActive(Boolean.TRUE);
        uMDepartment.setIsArchive(Boolean.FALSE);
        uMDepartment.setCreatedBy(loginDataBean.getId());
        uMDepartment.setCreatedOn(new Date());
        
        Long depId = departmentService.createDepartment(uMDepartment);
        //Make a map sectionwise id(Key) and customField map(Value)
        Map<Long, Map<String, Object>> val = new HashMap<>();
        val.put(depId, hkDepartmentDatabean.getDepartmentCustom());
        List<String> uiFieldList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(hkDepartmentDatabean.getDbType())) {
            for (Map.Entry<String, String> entrySet : hkDepartmentDatabean.getDbType().entrySet()) {
                uiFieldList.add(entrySet.getKey());
            }
        }
        Map<String, String> uiFieldMap = fieldService.retrieveUIFieldNameWithComponentTypes(uiFieldList);
        //Pass this map to makecustomfieldService
        List<CustomField> makeCustomField = customFieldSevice.makeCustomField(val, hkDepartmentDatabean.getDbType(), uiFieldMap, HkSystemConstantUtil.FeatureNameForCustomField.DEPARTMENT.toString(), loginDataBean.getCompanyId(), depId);
        //After that make Map of Section and there customfield
        Map<HkSystemConstantUtil.SectionNameForCustomField, List<CustomField>> map = new HashMap<>();
        map.put(HkSystemConstantUtil.SectionNameForCustomField.GENERAL, makeCustomField);
        //Pass this map to customFieldSevice.saveOrUpdate method.
        Long saveOrUpdate = customFieldSevice.saveOrUpdate(depId, HkSystemConstantUtil.FeatureNameForCustomField.DEPARTMENT, loginDataBean.getCompanyId(), map);
        userManagementServiceWrapper.createLocaleForEntity(hkDepartmentDatabean.getDisplayName(), "Department", loginDataBean.getId(), loginDataBean.getCompanyId());
        return depId;
    }
    
    public void updateDepartment(DepartmentDataBean hkDepartmentDataBean) {
        
        UMDepartment uMDepartment = null;
        try {
            uMDepartment = departmentService.retrieveDepartmentById(hkDepartmentDataBean.getId(), false, false, false);
        } catch (GenericDatabaseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (uMDepartment != null) {
            uMDepartment.setDeptName(hkDepartmentDataBean.getDisplayName());
            uMDepartment.setDescription(hkDepartmentDataBean.getDescription());
            uMDepartment.setCustom1(hkDepartmentDataBean.getShiftRotationDays());
            uMDepartment.setLastModifiedBy(loginDataBean.getId());
            uMDepartment.setLastModifiedOn(new Date());
            if (hkDepartmentDataBean.getParentId() != null) {
                uMDepartment.setParent(new UMDepartment(hkDepartmentDataBean.getParentId()));
            }
            if (hkDepartmentDataBean.getParentId() == 0) {
                uMDepartment.setParent(null);
            }
            departmentService.updateDepartment(uMDepartment);
            Map<Long, Map<String, Object>> val = new HashMap<>();
            val.put(uMDepartment.getId(), hkDepartmentDataBean.getDepartmentCustom());
            List<String> uiFieldList = new ArrayList<>();
            if (!CollectionUtils.isEmpty(hkDepartmentDataBean.getDbType())) {
                for (Map.Entry<String, String> entrySet : hkDepartmentDataBean.getDbType().entrySet()) {
                    uiFieldList.add(entrySet.getKey());
                }
            }
            Map<String, String> uiFieldMap = fieldService.retrieveUIFieldNameWithComponentTypes(uiFieldList);
            List<CustomField> makeCustomField = customFieldSevice.makeCustomField(val, hkDepartmentDataBean.getDbType(), uiFieldMap, HkSystemConstantUtil.FeatureNameForCustomField.DEPARTMENT.toString(), loginDataBean.getCompanyId(), uMDepartment.getId());
            Map<HkSystemConstantUtil.SectionNameForCustomField, List<CustomField>> map = new HashMap<>();
            map.put(HkSystemConstantUtil.SectionNameForCustomField.GENERAL, makeCustomField);
            Long saveOrUpdate = customFieldSevice.saveOrUpdate(uMDepartment.getId(), HkSystemConstantUtil.FeatureNameForCustomField.DEPARTMENT, loginDataBean.getCompanyId(), map);
            userManagementServiceWrapper.createLocaleForEntity(hkDepartmentDataBean.getDisplayName(), "Department", loginDataBean.getId(), loginDataBean.getCompanyId());
        }
    }
    
    public boolean removeDepartment(Long primaryKey) {
        DepartmentDataBean departmentDataBean = this.retrieveDepartmentByDepartmentId(primaryKey);
        userManagementServiceWrapper.deleteLocaleForEntity(departmentDataBean.getDisplayName(), "Department", "CONTENT", loginDataBean.getId(), loginDataBean.getCompanyId());
        userManagementServiceWrapper.removeDepartment(primaryKey);
        return true;
    }
    
    private DepartmentDataBean convertUMDepartmentToDepartmentDataBean(DepartmentDataBean departmentDataBean, UMDepartment umdepartment) {
        departmentDataBean.setId(umdepartment.getId());
        departmentDataBean.setDisplayName(umdepartment.getDeptName());
        departmentDataBean.setIsActive(umdepartment.getIsActive());
        departmentDataBean.setDescription(umdepartment.getDescription());
        departmentDataBean.setShiftRotationDays(umdepartment.getCustom1());
        departmentDataBean.setCompanyId(umdepartment.getCompany());
        
        return departmentDataBean;
    }
    
    public DepartmentDataBean retrieveDepartmentByDepartmentId(Long deptId) {
        UMDepartment umDept = userManagementServiceWrapper.retrieveDepartment(deptId);
        DepartmentDataBean departmentDataBean = new DepartmentDataBean();
        departmentDataBean = convertUMDepartmentToDepartmentDataBean(departmentDataBean, umDept);
        
        return departmentDataBean;
        
    }
    
    public DepartmentDataBean addCustomDataToDepartmentDataBean(DepartmentDataBean departmentDataBean) {
        
        Map<HkSystemConstantUtil.SectionNameForCustomField, List<Map<Long, Map<String, Object>>>> retrieveDocumentByInstanceId = customFieldTransformerBean.retrieveDocumentByInstanceId(departmentDataBean.getId(), HkSystemConstantUtil.FeatureNameForCustomField.DEPARTMENT, loginDataBean.getCompanyId());
        if (!CollectionUtils.isEmpty(retrieveDocumentByInstanceId)) {
            List<Map<Long, Map<String, Object>>> maps = retrieveDocumentByInstanceId.get(HkSystemConstantUtil.SectionNameForCustomField.GENERAL);
            if (maps != null) {
                for (Map<Long, Map<String, Object>> map : maps) {
                    departmentDataBean.setDepartmentCustom(map.get(departmentDataBean.getId()));
                    
                }
            }
        }
        return departmentDataBean;
    }
    
    public int activeUsersCountInDepartment(Long id) {
        
        int existingUsers = userManagementServiceWrapper.retrieveUserCountOfDepartment(id);
        return existingUsers;
    }
    
    public void createDefaultDepartment() {
        List<UMDepartment> deptList = userManagementServiceWrapper.retrieveDefaultDepartment(0l);
        if (CollectionUtils.isEmpty(deptList)) {
            System.out.println("Dep list is empty");
            // No default department,then create it
            UMDepartment uMDepartment = new UMDepartment();
            uMDepartment.setDeptName("Default Department");
            //Make department global.
            uMDepartment.setCompany(0l);
            uMDepartment.setCreatedOn(new Date());
            uMDepartment.setLastModifiedOn(new Date());
            uMDepartment.setIsActive(Boolean.TRUE);
            uMDepartment.setIsArchive(Boolean.FALSE);
            // Custom4 is the field which marks department as default department
            uMDepartment.setCustom4(Boolean.TRUE);
//        Set null for root category
            uMDepartment.setParent(null);
            departmentService.createDepartment(uMDepartment);
        }
    }
    
    public Map<String, Boolean> checkIfDepartmentIsPresentInAnyFeature(Long depId) {
        Map<String, Boolean> mapToBeSend = new HashMap<>();
        int countForConfigDoc = configurationTransformerBean.retrieveCountIfDepartmentExistInAnyDepartmentConfigDocument(depId);
        if (countForConfigDoc != 0) {
            mapToBeSend.put("Department Configuration", Boolean.TRUE);
        }
        Boolean isShiftExistWithDep = shiftTransformerBean.checkIfAnyShiftAssociatedWithDepartment(depId);
        if (isShiftExistWithDep) {
            mapToBeSend.put("Shift", Boolean.TRUE);
        }
        int existingUsers = userManagementServiceWrapper.retrieveUserCountOfDepartment(depId);
        if (existingUsers > 0) {
            mapToBeSend.put("Employees", Boolean.TRUE);
        }
        Boolean checkIfDepIsInDesignation = userManagementServiceWrapper.checkIfDepartmentIsInDesignation(depId, loginDataBean.getCompanyId());
        if (checkIfDepIsInDesignation) {
            mapToBeSend.put("Designation", Boolean.TRUE);
        }
        return mapToBeSend;
    }
}
