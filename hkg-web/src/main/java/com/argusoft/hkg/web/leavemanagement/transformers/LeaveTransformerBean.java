/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.leavemanagement.transformers;

import com.argusoft.generic.database.exception.GenericDatabaseException;
import com.argusoft.hkg.common.constantutil.HkSystemConstantUtil;
import com.argusoft.hkg.common.functionutil.HkSystemFunctionUtil;
import com.argusoft.hkg.core.HkEmployeeService;
import com.argusoft.hkg.core.HkFieldService;
import com.argusoft.hkg.core.HkFoundationService;
import com.argusoft.hkg.core.HkHRService;
import com.argusoft.hkg.model.HkLeaveApprovalEntity;
import com.argusoft.hkg.model.HkLeaveEntity;
import com.argusoft.hkg.model.HkShiftDtlEntity;
import com.argusoft.hkg.model.HkShiftEntity;
import com.argusoft.hkg.model.HkSystemConfigurationEntity;
import com.argusoft.hkg.model.HkValueEntity;
import com.argusoft.hkg.model.HkWorkflowApproverEntity;
import com.argusoft.hkg.model.HkWorkflowApproverEntityPK;
import com.argusoft.hkg.model.HkWorkflowEntity;
import com.argusoft.hkg.nosql.core.HkCustomFieldService;
import com.argusoft.hkg.nosql.model.CustomField;
import com.argusoft.hkg.nosql.model.HkLotDocument;
import com.argusoft.hkg.nosql.model.HkPacketDocument;
import com.argusoft.hkg.web.base.PrimaryKey;
import com.argusoft.hkg.web.customfield.transformers.CustomFieldTransformerBean;
import com.argusoft.hkg.web.leavemanagement.databeans.ApprovalDataBean;
import com.argusoft.hkg.web.leavemanagement.databeans.LeaveDataBean;
import com.argusoft.hkg.web.leavemanagement.databeans.LeaveWorkflowDataBean;
import com.argusoft.hkg.web.leavemanagement.databeans.RespondLeaveDataBean;
import com.argusoft.hkg.web.usermanagement.databeans.DepartmentDataBean;
import com.argusoft.hkg.web.center.usermanagement.databeans.LoginDataBean;
import com.argusoft.hkg.web.usermanagement.transformers.DepartmentTransformerBean;
import com.argusoft.hkg.web.usermanagement.wrapper.UserManagementServiceWrapper;
import com.argusoft.hkg.web.util.SelectItem;
import com.argusoft.hkg.web.util.TreeViewDataBean;
import com.argusoft.usermanagement.common.core.UMDepartmentService;
import com.argusoft.usermanagement.common.core.UMUserService;
import com.argusoft.usermanagement.common.model.UMDepartment;
import com.argusoft.usermanagement.common.model.UMUser;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 *
 * @author mansi
 */
@Service
public class LeaveTransformerBean {

    @Autowired
    UserManagementServiceWrapper userManagementServiceWrapper;
    @Autowired
    DepartmentTransformerBean departmentTransformerBean;
    @Autowired
    LoginDataBean loginDataBean;
    @Autowired
    UMDepartmentService departmentService;
    @Autowired
    HkHRService hRService;
    @Autowired
    UMUserService userService;
    @Autowired
    HkEmployeeService employeeService;
    @Autowired
    HkCustomFieldService customFieldSevice;
    @Autowired
    HkFieldService fieldService;
    @Autowired
    CustomFieldTransformerBean customFieldTransformerBean;
    @Autowired
    UserManagementServiceWrapper umWrapper;
    @Autowired
    HkFoundationService foundationService;

    public LeaveTransformerBean() {
    }

    public List<UMUser> retrieveUsersByCompanyByStatus(Long companyId, String user, Boolean isActive, boolean isLoggedInRequired) {
        List<UMUser> users = userManagementServiceWrapper.retrieveUsersByCompanyByStatus(companyId, user, isActive, isLoggedInRequired, loginDataBean.getId());
        return users;
    }

    public List<UMDepartment> retrieveDepartmentsByCompanyByStatus(Long companyId, String department, Boolean isActive) {
        List<UMDepartment> departments = userManagementServiceWrapper.retrieveDepartmentsByCompanyByStatus(companyId, department, isActive);
        return departments;
    }

    public Map retrieveDepartmentsInTreeView(long companyId) {
        List<DepartmentDataBean> retrieveDepartmentListInTreeView = departmentTransformerBean.retrieveDepartmentListInTreeViewSimple(companyId);
        Map map = new HashMap();
        // all department tree view
        map.put("tree1", retrieveDepartmentListInTreeView);
        //now for having existing workflow department tree view
        List<Long> retrieveDepartmentIdsForExistingWorkflows = hRService.retrieveDepartmentIdsForExistingWorkflows(companyId);
        Map<Long, Long> parent = new HashMap<>();
        retrieveDepartmentMap(companyId, parent);
        int i = parent.size();
        while (i > 0 && retrieveDepartmentIdsForExistingWorkflows.size() > 0) {
            for (Map.Entry<Long, Long> entry : parent.entrySet()) {
                Long long1 = entry.getKey();
                Long long2 = entry.getValue();
                if (retrieveDepartmentIdsForExistingWorkflows.contains(long1)) {
                    if (!retrieveDepartmentIdsForExistingWorkflows.contains(long2)) {
                        retrieveDepartmentIdsForExistingWorkflows.add(long2);
                    }
                }
                i--;
            }
        }
        List<DepartmentDataBean> finalList = new ArrayList<>();
        convertForExistingWorkflow(retrieveDepartmentListInTreeView, finalList, retrieveDepartmentIdsForExistingWorkflows);
        map.put("tree2", finalList);
        return map;
    }

    public void finalparent(Long find, Map<Long, Long> parentmap, Long finalParent) {
        Long get = parentmap.get(find);
        if (!get.equals(new Long("0"))) {
            finalparent(get, parentmap, finalParent);
        } else {
            finalParent = get;
        }
    }

    private List<DepartmentDataBean> convertForExistingWorkflow(List<DepartmentDataBean> uMDepartments, List<DepartmentDataBean> hkTreeViewDataBeans, List<Long> retrieveDepartmentIdsForExistingWorkflows) {
        for (DepartmentDataBean uMDepartment : uMDepartments) {
            DepartmentDataBean departmentDataBean = new DepartmentDataBean();
            departmentDataBean.setId(uMDepartment.getId());
            departmentDataBean.setDisplayName(uMDepartment.getDisplayName());
            List<DepartmentDataBean> listChild;
            try {
                if (!CollectionUtils.isEmpty(uMDepartment.getChildren())) {
                    listChild = new ArrayList<>();
                    List<DepartmentDataBean> l = uMDepartment.getChildren();
                    List<DepartmentDataBean> l1 = convertForExistingWorkflow(new ArrayList<>(l), listChild, retrieveDepartmentIdsForExistingWorkflows);
                    departmentDataBean.setChildren(l1);
                }
            } catch (Exception e) {
            }
            if (retrieveDepartmentIdsForExistingWorkflows.contains(departmentDataBean.getId())) {
                hkTreeViewDataBeans.add(departmentDataBean);
            }
        }

        return hkTreeViewDataBeans;
    }

    public List<TreeViewDataBean> retrieveDepartmentsInMultiSelectTreeView(long companyId) {
        List<DepartmentDataBean> retrieveDepartmentListInTreeView = departmentTransformerBean.retrieveDepartmentListInTreeViewSimple(companyId);
        List<TreeViewDataBean> treeViewDataBeans = new ArrayList<>();
        if (!CollectionUtils.isEmpty(retrieveDepartmentListInTreeView)) {
            for (DepartmentDataBean departmentDataBean : retrieveDepartmentListInTreeView) {
                TreeViewDataBean treeViewDataBean = new TreeViewDataBean();
                convertInTreeBean(departmentDataBean, treeViewDataBean);
                treeViewDataBeans.add(treeViewDataBean);
            }
        }
        return treeViewDataBeans;
    }

    public void convertInTreeBean(DepartmentDataBean departmentDataBean, TreeViewDataBean treeViewDataBean) {
        treeViewDataBean.setId(departmentDataBean.getId());
        treeViewDataBean.setText(departmentDataBean.getDisplayName());
        treeViewDataBean.setIsChecked(false);
        List<DepartmentDataBean> children = departmentDataBean.getChildren();
        if (!CollectionUtils.isEmpty(children)) {
            List<TreeViewDataBean> child = new ArrayList<>();
            for (DepartmentDataBean departmentDataBean1 : children) {
                TreeViewDataBean dataBean = new TreeViewDataBean();
                convertInTreeBean(departmentDataBean1, dataBean);
                child.add(dataBean);
            }
            treeViewDataBean.setItems(child);
        }
    }

    public String createWorkflow(Long userId, long company, Long[] departmentIds, String approvers, Map<String, Object> leaveWorkflowCustom, Map<String, String> leaveWorkflowDbType) {
        String response = HkSystemConstantUtil.SUCCESS;
        try {
            List<HkWorkflowEntity> hkWorkflowEntitys = new ArrayList<>();
            Set<HkWorkflowApproverEntity> approverEntitys = new HashSet<>();

            String[] split = approvers.split(",");
            Map<Long, Set<HkWorkflowApproverEntity>> map = new HashMap<>();
            boolean isDefault = true;
            if (departmentIds != null && departmentIds.length > 0) {
                isDefault = false;
            }
            Map<Long, Long> parent = new HashMap<>();
            Map<Long, String> depIdNameMap = retrieveDepartmentMap(company, parent);
            String[] depNames = null;
            List<List<Long>> notifyUsers = new ArrayList<>();
            if (isDefault) {
                createworkflow(null, company, userId, split, approverEntitys, hkWorkflowEntitys, map, leaveWorkflowCustom, leaveWorkflowDbType, notifyUsers, depIdNameMap);
            } else {
                depNames = new String[departmentIds.length];
                for (int i = 0; i < departmentIds.length; i++) {
                    depNames[i] = depIdNameMap.get(departmentIds[i]);
                    createworkflow(departmentIds[i], company, userId, split, approverEntitys, hkWorkflowEntitys, map, leaveWorkflowCustom, leaveWorkflowDbType, notifyUsers, depIdNameMap);
                }
            }
            if (hkWorkflowEntitys.size() > 0) {
                HkWorkflowEntity[] workflowArray = new HkWorkflowEntity[hkWorkflowEntitys.size()];
                hkWorkflowEntitys.toArray(workflowArray);
                List<Long>[] notifyUserArray = new ArrayList[notifyUsers.size()];
                for (int i = 0; i < notifyUsers.size(); i++) {
                    notifyUserArray[i] = notifyUsers.get(i);
                }
                hRService.createWorkflow(workflowArray, depNames, notifyUserArray);
                for (HkWorkflowEntity entity : hkWorkflowEntitys) {
                    createOrUpdateCustomFields(entity.getId(), leaveWorkflowCustom, leaveWorkflowDbType);
                }
            }
        } catch (Exception e) {
            response = HkSystemConstantUtil.FAILURE;
            Logger.getLogger(LeaveTransformerBean.class.getName()).log(Level.SEVERE, null, e);
        }
        return response;
    }

    public Map<Long, String> retrieveDepartmentMap(Long companyId, Map<Long, Long> parent) {
        Map<Long, String> depIdNameMap = new HashMap<>();
        List<DepartmentDataBean> retrieveDepartmentListInTreeViewSimple = departmentTransformerBean.retrieveDepartmentListInTreeViewSimple(companyId);
        if (!CollectionUtils.isEmpty(retrieveDepartmentListInTreeViewSimple)) {
            for (DepartmentDataBean departmentDataBean : retrieveDepartmentListInTreeViewSimple) {
                parent.put(departmentDataBean.getId(), departmentDataBean.getParentId());
                convert(departmentDataBean, depIdNameMap, parent);
            }
        }
        return depIdNameMap;
    }

    public void convert(DepartmentDataBean dataBean, Map<Long, String> map, Map<Long, Long> parent) {
        map.put(dataBean.getId(), dataBean.getDisplayName());
        if (!CollectionUtils.isEmpty(dataBean.getChildren())) {
            for (DepartmentDataBean child : dataBean.getChildren()) {
                parent.put(child.getId(), dataBean.getId());
                convert(child, map, parent);
            }
        }
    }

    public void createOrUpdateCustomFields(Long id, Map<String, Object> leaveWorkflowCustom, Map<String, String> leaveWorkflowDbType) {
        Map<Long, Map<String, Object>> val = new HashMap<>();
        val.put(id, leaveWorkflowCustom);
        List<String> uiFieldList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(leaveWorkflowDbType)) {
            for (Map.Entry<String, String> entrySet : leaveWorkflowDbType.entrySet()) {
                uiFieldList.add(entrySet.getKey());
            }
        }
        Map<String, String> uiFieldMap = fieldService.retrieveUIFieldNameWithComponentTypes(uiFieldList);
        List<CustomField> makeCustomField = customFieldSevice.makeCustomField(val, leaveWorkflowDbType, uiFieldMap, HkSystemConstantUtil.FeatureNameForCustomField.LEAVEWORKFLOW.toString(), loginDataBean.getCompanyId(), id);
        Map<HkSystemConstantUtil.SectionNameForCustomField, List<CustomField>> map1 = new HashMap<>();
        map1.put(HkSystemConstantUtil.SectionNameForCustomField.GENERAL, makeCustomField);
        customFieldSevice.saveOrUpdate(id, HkSystemConstantUtil.FeatureNameForCustomField.LEAVEWORKFLOW, loginDataBean.getCompanyId(), map1);
    }

    public void createworkflow(Long long1, Long company, Long userId, String[] split, Set<HkWorkflowApproverEntity> approverEntitys, List<HkWorkflowEntity> hkWorkflowEntitys, Map<Long, Set<HkWorkflowApproverEntity>> map, Map<String, Object> leaveWorkflowCustom, Map<String, String> leaveWorkflowDbType, List<List<Long>> notifyusers, Map<Long, String> depIdNameMap) {
        approverEntitys.clear();
        HkWorkflowEntity retrieveWorkflowForDepartment = hRService.retrieveWorkflowForDepartment(long1, company, false);
        HkWorkflowEntity hkWorkflowEntity = null;
        boolean override = false;
        if (retrieveWorkflowForDepartment == null || retrieveWorkflowForDepartment.getDepartment() == null) {
            hkWorkflowEntity = new HkWorkflowEntity();
            hkWorkflowEntity.setCreatedBy(userId);
            hkWorkflowEntity.setCreatedOn(new Date());
            hkWorkflowEntity.setFranchise(company);
            hkWorkflowEntity.setIsArchive(false);
            hkWorkflowEntity.setHkWorkflowApproverEntitySet(null);
            hkWorkflowEntity.setDepartment(long1);
        } else {
            hkWorkflowEntity = retrieveWorkflowForDepartment;
            override = true;
        }
        hkWorkflowEntity.setLastModifiedBy(userId);
        hkWorkflowEntity.setLastModifiedOn(new Date());

        for (int j = 0; j < split.length; j++) {
            String string = split[j];
            String[] split1 = string.split(":");
            Long id = new Long(split1[0]);
            HkWorkflowApproverEntityPK approverEntityPK = new HkWorkflowApproverEntityPK();
            approverEntityPK.setReferenceInstance(id);
            approverEntityPK.setReferenceType(split1[1]);
            if (override) {
                approverEntityPK.setWorkflow(hkWorkflowEntity.getId());
            }
            HkWorkflowApproverEntity approverEntity = new HkWorkflowApproverEntity();
            approverEntity.setHkWorkflowApproverEntityPK(approverEntityPK);
            approverEntity.setIsArchive(false);
            approverEntity.setHkWorkflowEntity(hkWorkflowEntity);
            approverEntity.setLevel(j + 1);
            approverEntitys.add(approverEntity);
        }
        hkWorkflowEntity.setHkWorkflowApproverEntitySet(approverEntitys);
        hkWorkflowEntity.setLastLevel(split.length);
        List<Long> notify = new ArrayList<>();
        if (long1 != null) {
            List<String> code = new ArrayList<>();
            code.add(long1 + ":" + HkSystemConstantUtil.RecipientCodeType.DEPARTMENT);
            Set<Long> retrieveRecipientIds = userManagementServiceWrapper.retrieveRecipientIds(code);
            notify.addAll(retrieveRecipientIds);
            Set<Long> searchUsersByFeatureName = userManagementServiceWrapper.searchUsersByFeatureName(Arrays.asList(HkSystemConstantUtil.Feature.MANAGE_LEAVE_WORKFLOW), company);
            notify.addAll(searchUsersByFeatureName);
            notifyusers.add(notify);
        }
        if (override) {
            String get = depIdNameMap.get(hkWorkflowEntity.getDepartment());
            hkWorkflowEntitys.add(hkWorkflowEntity);
            hRService.updateWorkflow(hkWorkflowEntity, approverEntitys, get, notify);
            createOrUpdateCustomFields(hkWorkflowEntity.getId(), leaveWorkflowCustom, leaveWorkflowDbType);
        } else {
            hkWorkflowEntitys.add(hkWorkflowEntity);
            map.put(long1, approverEntitys);
        }
    }

    public String deleteWorkflow(Long userId, Long deptId) {
        String response = HkSystemConstantUtil.SUCCESS;
        try {
            HkWorkflowEntity retrieveWorkflowForDepartment = hRService.retrieveWorkflowForDepartment(deptId, loginDataBean.getCompanyId(), false);
            if (retrieveWorkflowForDepartment != null) {
                hRService.removeWorkflow(retrieveWorkflowForDepartment.getId(), userId);
            }
        } catch (Exception e) {
            response = HkSystemConstantUtil.FAILURE;
        }
        return response;
    }

    public String updateWorkflow(Long userId, long company, Long departmentId, String approvers, Map<String, Object> leaveWorkflowCustom, Map<String, String> leaveWorkflowDbType) {
        String response = HkSystemConstantUtil.SUCCESS;
        try {
            HkWorkflowEntity hkWorkflowEntity = hRService.retrieveWorkflowForDepartment(departmentId, company, false);
            if (hkWorkflowEntity != null) {
                String depname = null;
                List<Long> notify = new ArrayList<>();
                if (departmentId != null) {
                    UMDepartment retrieveDepartment = departmentService.retrieveDepartmentById(departmentId, false, false, false);
                    depname = retrieveDepartment.getDeptName();
                    List<String> code = new ArrayList<>();
                    code.add(departmentId + ":" + HkSystemConstantUtil.RecipientCodeType.DEPARTMENT);
                    Set<Long> retrieveRecipientIds = userManagementServiceWrapper.retrieveRecipientIds(code);
                    notify.addAll(retrieveRecipientIds);
                }
                Set<Long> searchUsersByFeatureName = userManagementServiceWrapper.searchUsersByFeatureName(Arrays.asList(HkSystemConstantUtil.Feature.MANAGE_LEAVE_WORKFLOW), company);
                notify.addAll(searchUsersByFeatureName);
                Set<HkWorkflowApproverEntity> approverEntitys = new HashSet<>();
                hkWorkflowEntity.setLastModifiedBy(userId);
                hkWorkflowEntity.setLastModifiedOn(new Date());
                hkWorkflowEntity.setHkWorkflowApproverEntitySet(null);
                String[] split = approvers.split(",");

                for (int j = 0; j < split.length; j++) {
                    String string = split[j];
                    String[] split1 = string.split(":");
                    Long id = new Long(split1[0]);
                    HkWorkflowApproverEntityPK approverEntityPK = new HkWorkflowApproverEntityPK();
                    approverEntityPK.setWorkflow(hkWorkflowEntity.getId());
                    approverEntityPK.setReferenceInstance(id);
                    approverEntityPK.setReferenceType(split1[1]);
                    HkWorkflowApproverEntity approverEntity = new HkWorkflowApproverEntity();
                    approverEntity.setHkWorkflowApproverEntityPK(approverEntityPK);
                    approverEntity.setIsArchive(false);
                    approverEntity.setHkWorkflowEntity(hkWorkflowEntity);
                    approverEntity.setLevel(j + 1);
                    approverEntitys.add(approverEntity);
                }
                hkWorkflowEntity.setLastLevel(split.length);
                hRService.updateWorkflow(hkWorkflowEntity, approverEntitys, depname, notify);
                createOrUpdateCustomFields(hkWorkflowEntity.getId(), leaveWorkflowCustom, leaveWorkflowDbType);
            }
        } catch (GenericDatabaseException | NumberFormatException e) {
            response = HkSystemConstantUtil.FAILURE;
            Logger.getLogger(LeaveTransformerBean.class.getName()).log(Level.SEVERE, null, e);
        }
        return response;
    }

    public LeaveWorkflowDataBean retrieveWorkflowByDepartmentId(Long depId, Long companyId) {
        HkWorkflowEntity retrieveWorkflowForDepartment = hRService.retrieveWorkflowForDepartment(depId, companyId, true);
        LeaveWorkflowDataBean dataBean = new LeaveWorkflowDataBean();
        if (retrieveWorkflowForDepartment != null) {
            Map<HkSystemConstantUtil.SectionNameForCustomField, List<Map<Long, Map<String, Object>>>> retrieveDocumentByInstanceId = customFieldTransformerBean.retrieveDocumentByInstanceId(retrieveWorkflowForDepartment.getId(), HkSystemConstantUtil.FeatureNameForCustomField.LEAVEWORKFLOW, loginDataBean.getCompanyId());
            this.convertLeaveWorkflowEntitytoLeaveWorkflowBean(dataBean, retrieveWorkflowForDepartment, true, retrieveDocumentByInstanceId);
            if (dataBean.getDepartment() != null && !dataBean.getDepartment().equals(new Long(0))) {
                try {
                    UMDepartment retrieveDepartment = departmentService.retrieveDepartmentById(dataBean.getDepartment(), false, false, false);
                    if (retrieveDepartment != null) {
                        dataBean.setDepartmentName(retrieveDepartment.getDeptName());
                    }
                } catch (GenericDatabaseException ex) {
                    Logger.getLogger(LeaveTransformerBean.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return dataBean;
    }

    public LeaveWorkflowDataBean convertLeaveWorkflowEntitytoLeaveWorkflowBean(LeaveWorkflowDataBean workflowDataBean, HkWorkflowEntity hkWorkflowEntity, boolean approvalDetailRequired, Map<HkSystemConstantUtil.SectionNameForCustomField, List<Map<Long, Map<String, Object>>>> retrieveDocumentByInstanceId) {
        if (workflowDataBean == null) {
            workflowDataBean = new LeaveWorkflowDataBean();
        }
        if (hkWorkflowEntity.getDepartment() != null) {
            workflowDataBean.setDepartment(hkWorkflowEntity.getDepartment());
            workflowDataBean.setIsDefault(false);
        } else {
            workflowDataBean.setDepartment(new Long(0));
            workflowDataBean.setIsDefault(true);
        }
        workflowDataBean.setFranchise(hkWorkflowEntity.getFranchise());
        workflowDataBean.setId(hkWorkflowEntity.getId());
        workflowDataBean.setIsArchive(hkWorkflowEntity.getIsArchive());
        workflowDataBean.setLastLevel(hkWorkflowEntity.getLastLevel());
        if (approvalDetailRequired && !CollectionUtils.isEmpty(hkWorkflowEntity.getHkWorkflowApproverEntitySet())) {
            Set<HkWorkflowApproverEntity> hkWorkflowApproverEntitySet = hkWorkflowEntity.getHkWorkflowApproverEntitySet();
            List<String> code = new ArrayList<>();
            List<Long> level = new ArrayList<>();
            Map<Long, String> levelcode = new HashMap<>();
            for (HkWorkflowApproverEntity hkWorkflowApproverEntity : hkWorkflowApproverEntitySet) {
                level.add(hkWorkflowApproverEntity.getLevel());
                String temp = hkWorkflowApproverEntity.getHkWorkflowApproverEntityPK().getReferenceInstance() + ":" + hkWorkflowApproverEntity.getHkWorkflowApproverEntityPK().getReferenceType();
                code.add(temp);
                levelcode.put(hkWorkflowApproverEntity.getLevel(), temp);
            }
            Collections.sort(level);
            Map<String, String> recipientNames = userManagementServiceWrapper.retrieveRecipientNames(code);
            List<SelectItem> list = new ArrayList<>();
            for (Long long1 : level) {
                String key = levelcode.get(long1);
                SelectItem item = new SelectItem(key, recipientNames.get(key));
                list.add(item);
            }
            workflowDataBean.setApprovalMap(list);
        }
        if (retrieveDocumentByInstanceId != null) {
            List<Map<Long, Map<String, Object>>> maps = retrieveDocumentByInstanceId.get(HkSystemConstantUtil.SectionNameForCustomField.GENERAL);
            if (maps != null) {
                for (Map<Long, Map<String, Object>> map : maps) {
                    workflowDataBean.setLeaveWorkflowCustom(map.get(hkWorkflowEntity.getId()));
                }
            }
        }
        return workflowDataBean;
    }

    /**
     * This met holiday.
     *
     * @param leaveDataBean
     * @return
     *
     */
    public int applyLeave(LeaveDataBean leaveDataBean) {
        HkSystemConfigurationEntity systemConfigurationEntity = foundationService.retrieveSystemConfigurationByKey(HkSystemConstantUtil.FranchiseConfiguration.LEAVE_MAX_YEARLY_LEAVES, loginDataBean.getCompanyId());
        HkSystemConfigurationEntity systemConfigurationEntity1 = foundationService.retrieveSystemConfigurationByKey(HkSystemConstantUtil.FranchiseConfiguration.ALLOW_LEAVE_BEYOND_LIMIT, loginDataBean.getCompanyId());

        if (systemConfigurationEntity != null && systemConfigurationEntity1 != null) {
            long compareTo = leaveDataBean.getToDate().getTime() - leaveDataBean.getFromDate().getTime();
            long days = TimeUnit.DAYS.convert(compareTo, TimeUnit.MILLISECONDS);
            if (days > Integer.parseInt(systemConfigurationEntity.getKeyValue()) && systemConfigurationEntity1.getKeyValue().equals("false")) {
                return 3;
            } else {

//        System.out.println("Remarks in apply leave==" + leaveDataBean.getRemarks());
                HkLeaveEntity leaveEntity = convertLeaveDatabeanToModel(leaveDataBean, null);
//        System.out.println("Remarks in apply leave Entity==" + leaveEntity.getFinalRemarks());
                List<String> statusList = new ArrayList<>();
                statusList.add(HkSystemConstantUtil.LeaveStatus.APPROVED);
                statusList.add(HkSystemConstantUtil.LeaveStatus.PENDING);
                HkWorkflowEntity workflowEntity = hRService.retrieveWorkflowForDepartment(loginDataBean.getDepartment(), loginDataBean.getCompanyId(), false);
                if (workflowEntity == null) {
                    return 0;
                }
                List<Long> userIds = employeeService.isUserOnLeave(loginDataBean.getId(), leaveEntity.getFrmDt(), leaveEntity.getToDt(), statusList, loginDataBean.getCompanyId());
                if (userIds != null && !userIds.isEmpty()) {
                    return 1;
                } else {
                    Set<Long> notifiedUsersSet = umWrapper.retrieveApproversByWorkflow(workflowEntity.getId(), 1);
                    List<Long> notifiedUsers = new ArrayList<Long>(notifiedUsersSet);
                    UMUser user = userService.retrieveUserById(loginDataBean.getId(), loginDataBean.getCompanyId());
                    String userCode = null;
                    String userName = null;
                    if (user != null) {
                        userCode = user.getUserCode();
                        userName = user.getUserId();
                    }
                    Long shiftId = user.getCustom4();
                    employeeService.applyLeave(leaveEntity, loginDataBean.getDepartment(), shiftId, userCode, userName, notifiedUsers);
                    this.createOrUpdateCustomField(leaveEntity.getId(), leaveDataBean);
                    return 2;
                }
            }
        } else {
            return 4;
        }
    }

    public void createOrUpdateCustomField(Long id, LeaveDataBean leaveDataBean) {
        Map<Long, Map<String, Object>> val = new HashMap<>();
        val.put(id, leaveDataBean.getLeaveCustom());
        List<String> uiFieldList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(leaveDataBean.getDbType())) {
            for (Map.Entry<String, String> entrySet : leaveDataBean.getDbType().entrySet()) {
                uiFieldList.add(entrySet.getKey());
            }
        }
        Map<String, String> uiFieldMap = fieldService.retrieveUIFieldNameWithComponentTypes(uiFieldList);
        List<CustomField> makeCustomField = customFieldSevice.makeCustomField(val, leaveDataBean.getDbType(), uiFieldMap, HkSystemConstantUtil.FeatureNameForCustomField.LEAVE.toString(), loginDataBean.getCompanyId(), id);
        Map<HkSystemConstantUtil.SectionNameForCustomField, List<CustomField>> map = new HashMap<>();
        map.put(HkSystemConstantUtil.SectionNameForCustomField.GENERAL, makeCustomField);
        Long saveOrUpdate = customFieldSevice.saveOrUpdate(id, HkSystemConstantUtil.FeatureNameForCustomField.LEAVE, loginDataBean.getCompanyId(), map);
    }

    private HkLeaveEntity convertLeaveDatabeanToModel(LeaveDataBean leaveDataBean, HkLeaveEntity leaveEntity) {
        if (leaveEntity == null) {
            leaveEntity = new HkLeaveEntity();
        }
        leaveEntity.setLeaveReason(leaveDataBean.getReasonId());
        leaveEntity.setFrmDt(HkSystemFunctionUtil.convertToServerDate(leaveDataBean.getFromDate(), loginDataBean.getClientRawOffsetInMin()));
        leaveEntity.setToDt(HkSystemFunctionUtil.convertToServerDate(leaveDataBean.getToDate(), loginDataBean.getClientRawOffsetInMin()));
        leaveEntity.setDescription(leaveDataBean.getDescription());
        leaveEntity.setCreatedOn(new Date());
        leaveEntity.setCreatedBy(loginDataBean.getId());
        leaveEntity.setStatus(HkSystemConstantUtil.LeaveStatus.PENDING);
        leaveEntity.setIsArchive(false);
        leaveEntity.setFranchise(loginDataBean.getCompanyId());
        leaveEntity.setForUser(loginDataBean.getId());
        leaveEntity.setFinalRemarks(leaveDataBean.getRemarks());
        leaveEntity.setTotalDays((float) HkSystemFunctionUtil.getTotalWorkingDaysBetweenDates(HkSystemFunctionUtil.convertToServerDate(leaveDataBean.getFromDate(), loginDataBean.getClientRawOffsetInMin()),
                HkSystemFunctionUtil.convertToServerDate(leaveDataBean.getToDate(), loginDataBean.getClientRawOffsetInMin()), "1"));
        return leaveEntity;
    }

    public List<LeaveDataBean> retrieveAllLeave() {
        List<String> statusList = new ArrayList<>();
        List<LeaveDataBean> leaveDataBeanList = new ArrayList<>();
        statusList.add(HkSystemConstantUtil.LeaveStatus.APPROVED);
        statusList.add(HkSystemConstantUtil.LeaveStatus.PENDING);
        statusList.add(HkSystemConstantUtil.LeaveStatus.CANCELED);
        statusList.add(HkSystemConstantUtil.LeaveStatus.DISAPPROVED);
        List<HkLeaveEntity> leaveEntityList = employeeService.retrieveLeavesByCriteria(loginDataBean.getId(), statusList, loginDataBean.getCompanyId(), false);
        List<Long> ids = new ArrayList<>();
        if (!CollectionUtils.isEmpty(leaveEntityList)) {
            for (HkLeaveEntity hkLeaveEntity : leaveEntityList) {
                if (!hkLeaveEntity.getIsArchive()) {
                    ids.add(hkLeaveEntity.getId());
                }
            }
        }
        Map<Long, Map<HkSystemConstantUtil.SectionNameForCustomField, List<Map<Long, Map<String, Object>>>>> retrieveDocumentByInstanceIds = customFieldSevice.retrieveDocumentByInstanceIds(ids, HkSystemConstantUtil.FeatureNameForCustomField.LEAVE, loginDataBean.getCompanyId());
        if (leaveEntityList != null) {
            leaveDataBeanList = convertLeaveEntityToDataBean(leaveEntityList, null);
        }
        return leaveDataBeanList;
    }

    private List<LeaveDataBean> convertLeaveEntityToDataBean(List<HkLeaveEntity> leaveEntityList, Map<Long, Map<HkSystemConstantUtil.SectionNameForCustomField, List<Map<Long, Map<String, Object>>>>> retrieveDocumentByInstanceIds) {
        List<LeaveDataBean> leaveDataBeanList = new ArrayList<>();
        for (HkLeaveEntity leaveEntity : leaveEntityList) {
            LeaveDataBean leaveDataBean = new LeaveDataBean();
            leaveDataBean.setId(leaveEntity.getId());
            leaveDataBean.setFromDate(HkSystemFunctionUtil.convertToClientDate(leaveEntity.getFrmDt(), loginDataBean.getClientRawOffsetInMin()));
            leaveDataBean.setToDate(HkSystemFunctionUtil.convertToClientDate(leaveEntity.getToDt(), loginDataBean.getClientRawOffsetInMin()));
            leaveDataBean.setDescription(leaveEntity.getDescription());
            UMUser user = userService.retrieveUserById(loginDataBean.getId(), loginDataBean.getCompanyId());
            String userCode = null;
            String userName = null;
            if (user != null) {
                userCode = user.getUserCode();
                userName = user.getUserId();
            }
            Long shiftId = user.getCustom4();
            HkShiftEntity retrieveShiftById = hRService.retrieveShiftById(leaveEntity.getFranchise(), shiftId);
            Integer shiftDuration = 0;
            Set<HkShiftDtlEntity> hkShiftDtlEntitySet = retrieveShiftById.getHkShiftDtlEntitySet();
            for (HkShiftDtlEntity hkShiftDtlEntity : hkShiftDtlEntitySet) {
                if (hkShiftDtlEntity.getSlotType().equalsIgnoreCase(HkSystemConstantUtil.SHIFT_TYPE.MAIN_TIME)) {
                    shiftDuration = hkShiftDtlEntity.getShiftDurationMin();
                }
            }
            if (shiftDuration == null) {
                shiftDuration = 24;
            } else {
                shiftDuration = shiftDuration / 60;
            }
            Float totalDays = leaveEntity.getTotalDays();
            if (totalDays != null) {
                Integer intValue = totalDays.intValue();
                totalDays -= totalDays.intValue();
                Float defaultValue = 0.0f;
                String days = "";
                if (intValue != 0) {
                    days = intValue.toString().concat("d ");
                }
                leaveDataBean.setTotalDays(days);
                if (!totalDays.equals(defaultValue)) {
                    totalDays = totalDays * shiftDuration;
                    int indexOf = totalDays.toString().indexOf(".");
                    if (totalDays.toString().length() > indexOf + 3) {
                        leaveDataBean.setTotalDays(days.concat(totalDays.toString().substring(0, indexOf + 3)).concat("hr"));
                    } else {
                        leaveDataBean.setTotalDays(days.concat(totalDays.toString().substring(0, totalDays.toString().length())).concat("hr"));
                    }

                }
            }
            leaveDataBean.setDays(leaveEntity.getTotalDays());
            leaveDataBean.setApplyedOn(leaveEntity.getCreatedOn());
            leaveDataBean.setRemarks(leaveEntity.getFinalRemarks());
            leaveDataBean.setRequestType(HkSystemConstantUtil.LeaveStatus.REQUEST_TYPE_LEAVE);
            if (leaveEntity.getStatus().equals(HkSystemConstantUtil.LeaveStatus.PENDING)) {
                leaveDataBean.setStatus(HkSystemConstantUtil.LEAVE_STATUS_MAP.get(HkSystemConstantUtil.LeaveStatus.PENDING));
            } else if (leaveEntity.getStatus().equals(HkSystemConstantUtil.LeaveStatus.APPROVED)) {
                leaveDataBean.setStatus(HkSystemConstantUtil.LEAVE_STATUS_MAP.get(HkSystemConstantUtil.LeaveStatus.APPROVED));
            } else if (leaveEntity.getStatus().equals(HkSystemConstantUtil.LeaveStatus.DISAPPROVED)) {
                leaveDataBean.setStatus(HkSystemConstantUtil.LEAVE_STATUS_MAP.get(HkSystemConstantUtil.LeaveStatus.DISAPPROVED));
            } else if (leaveEntity.getStatus().equals(HkSystemConstantUtil.LeaveStatus.CANCELED)) {
                leaveDataBean.setStatus(HkSystemConstantUtil.LEAVE_STATUS_MAP.get(HkSystemConstantUtil.LeaveStatus.CANCELED));
                leaveDataBean.setRequestType(HkSystemConstantUtil.LeaveStatus.REQUEST_TYPE_CANCELLATION);

            }
            leaveDataBean.setReasonId(leaveEntity.getLeaveReason());
            if (leaveEntity.getStatus().equals(HkSystemConstantUtil.LeaveStatus.PENDING)
                    || (leaveEntity.getStatus().equals(HkSystemConstantUtil.LeaveStatus.APPROVED) && leaveEntity.getFrmDt().compareTo(new Date()) >= 0)) {
                leaveDataBean.setEdit(Boolean.TRUE);
            } else {
                leaveDataBean.setEdit(Boolean.FALSE);
            }
            if (retrieveDocumentByInstanceIds != null) {
                Map<HkSystemConstantUtil.SectionNameForCustomField, List<Map<Long, Map<String, Object>>>> sectionWiseMap = retrieveDocumentByInstanceIds.get(leaveEntity.getId());
                if (sectionWiseMap != null) {
                    List<Map<Long, Map<String, Object>>> maps = sectionWiseMap.get(HkSystemConstantUtil.SectionNameForCustomField.GENERAL);
                    if (maps != null) {
                        for (Map<Long, Map<String, Object>> map : maps) {
                            leaveDataBean.setLeaveCustom(map.get(leaveEntity.getId()));
                        }
                    }
                }
            }
            if (leaveDataBean.getLeaveCustom() == null) {
                leaveDataBean.setLeaveCustom(new HashMap<String, Object>());
            }
            leaveDataBeanList.add(leaveDataBean);

        }
        return leaveDataBeanList;
    }

    public void deleteById(PrimaryKey<Long> primaryKey) {
        employeeService.cancelLeave(primaryKey.getPrimaryKey(), null, null, null);
    }

    public void archivedLeave(PrimaryKey<Long> primaryKey) {
        employeeService.archiveLeave(primaryKey.getPrimaryKey());
    }

    public int updateLeave(LeaveDataBean leaveDatabean) {
        HkSystemConfigurationEntity systemConfigurationEntity = foundationService.retrieveSystemConfigurationByKey(HkSystemConstantUtil.FranchiseConfiguration.LEAVE_MAX_YEARLY_LEAVES, loginDataBean.getCompanyId());
        HkSystemConfigurationEntity systemConfigurationEntity1 = foundationService.retrieveSystemConfigurationByKey(HkSystemConstantUtil.FranchiseConfiguration.ALLOW_LEAVE_BEYOND_LIMIT, loginDataBean.getCompanyId());

        if (systemConfigurationEntity != null && systemConfigurationEntity1 != null) {
            long compareTo = leaveDatabean.getToDate().getTime() - leaveDatabean.getFromDate().getTime();
            long days = TimeUnit.DAYS.convert(compareTo, TimeUnit.MILLISECONDS);
            if (days > Integer.parseInt(systemConfigurationEntity.getKeyValue()) && systemConfigurationEntity1.getKeyValue().equals("false")) {
                return 0;
            } else {
                List<String> statusList = new ArrayList<>();
                statusList.add(HkSystemConstantUtil.LeaveStatus.APPROVED);
                statusList.add(HkSystemConstantUtil.LeaveStatus.PENDING);
//<<<<<<< .mine
//        List<Long> userLeaveIds = employeeService.isUserOnLeave(loginDataBean.getId(), leaveDatabean.getFromDate(), leaveDatabean.getToDate(), statusList, loginDataBean.getCompanyId());
//=======
                List<Long> userLeaveIds = employeeService.isUserOnLeave(loginDataBean.getId(), HkSystemFunctionUtil.convertToServerDate(leaveDatabean.getFromDate(), loginDataBean.getClientRawOffsetInMin()), HkSystemFunctionUtil.convertToServerDate(leaveDatabean.getToDate(), loginDataBean.getClientRawOffsetInMin()), statusList, 1l);
                if (userLeaveIds.isEmpty() || (userLeaveIds.contains(leaveDatabean.getId()) && userLeaveIds.size() == 1)) {
                    HkLeaveEntity leaveEntity = employeeService.retrieveLeaveById(leaveDatabean.getId(), false);
                    leaveEntity = convertLeaveDatabeanToModel(leaveDatabean, leaveEntity);
                    List<HkLeaveApprovalEntity> leaveApprover = employeeService.retrieveLeaveApprovalEntitiesByCriteria(leaveDatabean.getId(), null, null, loginDataBean.getDepartment(), loginDataBean.getCompanyId());

                    UMUser user = userService.retrieveUserById(loginDataBean.getId(), loginDataBean.getCompanyId());
                    String empCode = null;
                    String empName = null;
                    List<Long> notifiedUsers = null;
                    if (user != null) {
                        empCode = user.getUserCode();
                        empName = user.getUserId();
                    }
                    if (leaveApprover != null && leaveApprover.size() > 0) {
                        Set<Long> notifiedUsersSet = umWrapper.retrieveApproversByWorkflow(leaveApprover.get(0).getWorkflow(), leaveApprover.get(0).getLevel());
                        notifiedUsers = new ArrayList<>(notifiedUsersSet);
                    }
                    employeeService.updateLeave(leaveEntity, empCode, empName, user.getCustom4(), notifiedUsers);
                    Map<Long, Map<String, Object>> val = new HashMap<>();
                    val.put(leaveEntity.getId(), leaveDatabean.getLeaveCustom());
                    List<String> uiFieldList = new ArrayList<>();
                    if (!CollectionUtils.isEmpty(leaveDatabean.getDbType())) {
                        for (Map.Entry<String, String> entrySet : leaveDatabean.getDbType().entrySet()) {
                            uiFieldList.add(entrySet.getKey());
                        }
                    }
                    Map<String, String> uiFieldMap = fieldService.retrieveUIFieldNameWithComponentTypes(uiFieldList);
                    //Pass this map to makecustomfieldService
                    List<CustomField> makeCustomField = customFieldSevice.makeCustomField(val, leaveDatabean.getDbType(), uiFieldMap, HkSystemConstantUtil.FeatureNameForCustomField.LEAVE.toString(), loginDataBean.getCompanyId(), leaveEntity.getId());
                    //After that make Map of Section and there customfield
                    Map<HkSystemConstantUtil.SectionNameForCustomField, List<CustomField>> map = new HashMap<>();
                    map.put(HkSystemConstantUtil.SectionNameForCustomField.GENERAL, makeCustomField);
                    //Pass this map to customFieldSevice.saveOrUpdate method.
                    Long saveOrUpdate = customFieldSevice.saveOrUpdate(leaveEntity.getId(), HkSystemConstantUtil.FeatureNameForCustomField.LEAVE, loginDataBean.getCompanyId(), map);
                    return 1;
                } else {
                    return 2;
                }
            }
        } else {
            return 3;
        }
    }

    public List<ApprovalDataBean> retrieveAllApproval(Boolean isAllLeave) throws GenericDatabaseException {
        List<String> statusList = new ArrayList<>();
        statusList.add(HkSystemConstantUtil.LeaveStatus.PENDING);
        if (isAllLeave) {
            statusList.add(HkSystemConstantUtil.LeaveStatus.APPROVED);
            statusList.add(HkSystemConstantUtil.LeaveStatus.DISAPPROVED);
        }
        List<HkLeaveApprovalEntity> resultList = employeeService.retrieveLeavesForApprover(statusList, loginDataBean.getId(), loginDataBean.getDepartment());
        List<ApprovalDataBean> approvalDataBeanList = null;
        Map<Long, UMUser> allUser = umWrapper.retrieveUsersByFranchise(loginDataBean.getCompanyId());
        Map<Long, String> allDepts = umWrapper.retrieveDepartmentsByFranchise(loginDataBean.getCompanyId());
        if (!CollectionUtils.isEmpty(resultList)) {
            approvalDataBeanList = new ArrayList<>();
            Iterator<HkLeaveApprovalEntity> iter = resultList.iterator();
            Calendar beforDate = Calendar.getInstance();
            beforDate.setTime(new Date());
            beforDate.add(Calendar.MONTH, -3);
            while (iter.hasNext()) {
                HkLeaveApprovalEntity leaveApproval = iter.next();
                if (isAllLeave && !(leaveApproval.getStatus().equals(HkSystemConstantUtil.LeaveStatus.PENDING)) && !(leaveApproval.getAttendedOn().compareTo(beforDate.getTime()) >= 0)) {
                    iter.remove();
                }
            }
            for (HkLeaveApprovalEntity leaveApproval : resultList) {
                ApprovalDataBean approvalDataBean = new ApprovalDataBean();
                approvalDataBean.setApprovalId(leaveApproval.getId());
                approvalDataBean.setId(leaveApproval.getLeaveRequest().getId());
                approvalDataBean.setUserId(leaveApproval.getLeaveRequest().getForUser());
                if (allUser.containsKey(leaveApproval.getLeaveRequest().getForUser())) {
                    UMUser uMUser = allUser.get(leaveApproval.getLeaveRequest().getForUser());
                    if (uMUser.getLastName() != null) {
                        approvalDataBean.setRequestFrom(uMUser.getUserCode() + "-" + uMUser.getFirstName() + " " + uMUser.getLastName());
                    } else {
                        approvalDataBean.setRequestFrom(uMUser.getUserCode() + "-" + uMUser.getFirstName());
                    }
                    approvalDataBean.setDepartment(allDepts.get(uMUser.getDepartment()));
                }
                approvalDataBean.setFromDate(HkSystemFunctionUtil.convertToClientDate(leaveApproval.getLeaveRequest().getFrmDt(), loginDataBean.getClientRawOffsetInMin()));
                approvalDataBean.setToDate(HkSystemFunctionUtil.convertToClientDate(leaveApproval.getLeaveRequest().getToDt(), loginDataBean.getClientRawOffsetInMin()));
                approvalDataBean.setDays(leaveApproval.getLeaveRequest().getTotalDays());
                approvalDataBean.setDescription(leaveApproval.getLeaveRequest().getDescription());
                if (leaveApproval.getLeaveRequest().getStatus().equals(HkSystemConstantUtil.LeaveStatus.CANCELED)) {
                    approvalDataBean.setRequestType(HkSystemConstantUtil.LeaveStatus.REQUEST_TYPE_CANCELLATION);
                } else {
                    approvalDataBean.setRequestType(HkSystemConstantUtil.LeaveStatus.REQUEST_TYPE_LEAVE);
                }
                approvalDataBean.setReason(getReasonById(leaveApproval.getLeaveRequest().getLeaveReason()));
                if (leaveApproval.getStatus().equals(HkSystemConstantUtil.LeaveStatus.PENDING)) {
                    approvalDataBean.setStatus(HkSystemConstantUtil.LEAVE_STATUS_MAP.get(HkSystemConstantUtil.LeaveStatus.PENDING));
                } else if (leaveApproval.getStatus().equals(HkSystemConstantUtil.LeaveStatus.APPROVED)) {
                    approvalDataBean.setStatus(HkSystemConstantUtil.LEAVE_STATUS_MAP.get(HkSystemConstantUtil.LeaveStatus.APPROVED));
                } else if (leaveApproval.getStatus().equals(HkSystemConstantUtil.LeaveStatus.CANCELED)) {
                    approvalDataBean.setStatus(HkSystemConstantUtil.LEAVE_STATUS_MAP.get(HkSystemConstantUtil.LeaveStatus.CANCELED));
                } else if (leaveApproval.getStatus().equals(HkSystemConstantUtil.LeaveStatus.DISAPPROVED)) {
                    approvalDataBean.setStatus(HkSystemConstantUtil.LEAVE_STATUS_MAP.get(HkSystemConstantUtil.LeaveStatus.DISAPPROVED));
                }
                if (approvalDataBean.getLeaveCustom() == null) {
                    approvalDataBean.setLeaveCustom(new HashMap<String, Object>());
                }
                approvalDataBean.setApplyedOn(leaveApproval.getLeaveRequest().getCreatedOn());
                approvalDataBean.setRemarks(leaveApproval.getRemarks());
                approvalDataBeanList.add(approvalDataBean);
            }

        }
        return approvalDataBeanList;

    }

    private String getReasonById(Long id) {
        HkValueEntity searchMasterValue = foundationService.searchMasterValue(id);
        return searchMasterValue.getValueName();
    }

    public void approveLeave(RespondLeaveDataBean respondLeaveDataBean) {
        List<HkLeaveApprovalEntity> leaveApprovalEntitys = employeeService.retrieveLeaveApprovalEntitiesByCriteria(respondLeaveDataBean.getId(), null, null, loginDataBean.getDepartment(), loginDataBean.getCompanyId());
        Set<Long> notifiedUsersSet = null;
        String empCode = null;
        String empName = null;
        String approverName = null;
        List<Long> notifiedUsers = null;
        if (leaveApprovalEntitys != null && leaveApprovalEntitys.size() > 0) {
            HkLeaveApprovalEntity approvalEntity = leaveApprovalEntitys.get(0);
            notifiedUsersSet = umWrapper.retrieveApproversByWorkflow(approvalEntity.getWorkflow(), approvalEntity.getLevel());
            notifiedUsers = new ArrayList<Long>(notifiedUsersSet);
            UMUser approverUser = userService.retrieveUserById(loginDataBean.getId(), loginDataBean.getCompanyId());
            UMUser empUser = userService.retrieveUserById(approvalEntity.getLeaveRequest().getForUser(), approvalEntity.getLeaveRequest().getFranchise());
            if (approverUser != null) {
                empCode = empUser.getUserCode();
                empName = empUser.getUserId();
                approverName = approverUser.getUserId();
            }
        }
//        System.out.println("Remarks.........." + respondLeaveDataBean.getRemarks());
        employeeService.respondLeave(respondLeaveDataBean.getId(), loginDataBean.getId(), respondLeaveDataBean.getRemarks(), HkSystemConstantUtil.LeaveStatus.APPROVED,
                empCode, empName, approverName, notifiedUsers);
        Map<Long, Map<String, Object>> val = new HashMap<>();
        val.put(respondLeaveDataBean.getId(), respondLeaveDataBean.getRespondCustom());
        List<String> uiFieldList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(respondLeaveDataBean.getDbType())) {
            for (Map.Entry<String, String> entrySet : respondLeaveDataBean.getDbType().entrySet()) {
                uiFieldList.add(entrySet.getKey());
            }
        }
        Map<String, String> uiFieldMap = fieldService.retrieveUIFieldNameWithComponentTypes(uiFieldList);
        List<CustomField> makeCustomField = customFieldSevice.makeCustomField(val, respondLeaveDataBean.getDbType(), uiFieldMap, HkSystemConstantUtil.FeatureNameForCustomField.RESPONDLEAVE.toString(), loginDataBean.getCompanyId(), respondLeaveDataBean.getId());
        Map<HkSystemConstantUtil.SectionNameForCustomField, List<CustomField>> map = new HashMap<>();
        map.put(HkSystemConstantUtil.SectionNameForCustomField.GENERAL, makeCustomField);
        Long saveOrUpdate = customFieldSevice.saveOrUpdate(respondLeaveDataBean.getId(), HkSystemConstantUtil.FeatureNameForCustomField.RESPONDLEAVE, loginDataBean.getCompanyId(), map);

    }

    public void disApproveLeave(RespondLeaveDataBean respondLeaveDataBean) {
        List<HkLeaveApprovalEntity> leaveApprovalEntitys = employeeService.retrieveLeaveApprovalEntitiesByCriteria(respondLeaveDataBean.getId(), null, null, loginDataBean.getDepartment(), loginDataBean.getCompanyId());
        Set<Long> notifiedUsersSet;
        String empCode = null;
        String empName = null;
        String approverName = null;
        List<Long> notifiedUsers = null;
        if (leaveApprovalEntitys != null && leaveApprovalEntitys.size() > 0) {
            HkLeaveApprovalEntity approvalEntity = leaveApprovalEntitys.get(0);
            notifiedUsersSet = umWrapper.retrieveApproversByWorkflow(approvalEntity.getWorkflow(), approvalEntity.getLevel());
            notifiedUsers = new ArrayList<>(notifiedUsersSet);
            UMUser approverUser = userService.retrieveUserById(loginDataBean.getId(), loginDataBean.getCompanyId());
            UMUser empUser = userService.retrieveUserById(approvalEntity.getLeaveRequest().getForUser(), approvalEntity.getLeaveRequest().getFranchise());
            if (approverUser != null) {
                empCode = empUser.getUserCode();
                empName = empUser.getUserId();
                approverName = approverUser.getUserId();
            }
        }
//        System.out.println("Remarks.........." + respondLeaveDataBean.getRemarks());
        employeeService.respondLeave(respondLeaveDataBean.getId(), loginDataBean.getId(), respondLeaveDataBean.getRemarks(), HkSystemConstantUtil.LeaveStatus.DISAPPROVED,
                empCode, empName, approverName, (List<Long>) notifiedUsers);
        Map<Long, Map<String, Object>> val = new HashMap<>();
        val.put(respondLeaveDataBean.getId(), respondLeaveDataBean.getRespondCustom());

        List<String> uiFieldList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(respondLeaveDataBean.getDbType())) {
            for (Map.Entry<String, String> entrySet : respondLeaveDataBean.getDbType().entrySet()) {
                uiFieldList.add(entrySet.getKey());
            }
        }
        Map<String, String> uiFieldMap = fieldService.retrieveUIFieldNameWithComponentTypes(uiFieldList);
        List<CustomField> makeCustomField = customFieldSevice.makeCustomField(val, respondLeaveDataBean.getDbType(), uiFieldMap, HkSystemConstantUtil.FeatureNameForCustomField.RESPONDLEAVE.toString(), loginDataBean.getCompanyId(), respondLeaveDataBean.getId());
        Map<HkSystemConstantUtil.SectionNameForCustomField, List<CustomField>> map = new HashMap<>();
        map.put(HkSystemConstantUtil.SectionNameForCustomField.GENERAL, makeCustomField);
        Long saveOrUpdate = customFieldSevice.saveOrUpdate(respondLeaveDataBean.getId(), HkSystemConstantUtil.FeatureNameForCustomField.RESPONDLEAVE, loginDataBean.getCompanyId(), map);

    }

    public List<ApprovalDataBean> retriveAllLeaveByUserId(Long userId) {
        List<HkLeaveEntity> leaveEntitys = employeeService.retrieveLeavesByCriteria(userId, null, loginDataBean.getCompanyId(), null);
        List<ApprovalDataBean> approvalDataBeans = new ArrayList<>();
        String deptName = "";
        String requestFrom = "";
        if (leaveEntitys != null && !leaveEntitys.isEmpty()) {
            try {
                UMUser user = userService.retrieveUserById(userId, null);
                requestFrom = user.getUserCode() + "-" + user.getFirstName() + " " + user.getLastName();
                UMDepartment department = departmentService.retrieveDepartmentById(user.getDepartment(), false, false, false);
                deptName = department.getDeptName();

            } catch (GenericDatabaseException ex) {
                Logger.getLogger(LeaveTransformerBean.class
                        .getName()).log(Level.SEVERE, null, ex);
            }

        }

        for (HkLeaveEntity hkLeaveEntity : leaveEntitys) {
            ApprovalDataBean approvalDataBean = new ApprovalDataBean();
            approvalDataBean.setId(hkLeaveEntity.getId());
            approvalDataBean.setRequestFrom(requestFrom);
            approvalDataBean.setReason(getReasonById(hkLeaveEntity.getLeaveReason()));
            approvalDataBean.setFromDate(HkSystemFunctionUtil.convertToClientDate(hkLeaveEntity.getFrmDt(), loginDataBean.getClientRawOffsetInMin()));
            approvalDataBean.setToDate(HkSystemFunctionUtil.convertToClientDate(hkLeaveEntity.getToDt(), loginDataBean.getClientRawOffsetInMin()));
            approvalDataBean.setDepartment(deptName);
            approvalDataBean.setApplyedOn(hkLeaveEntity.getCreatedOn());
            approvalDataBean.setDescription(hkLeaveEntity.getDescription());
            approvalDataBean.setDays(hkLeaveEntity.getTotalDays());
            if (hkLeaveEntity.getStatus().equals(HkSystemConstantUtil.LeaveStatus.PENDING)) {
                approvalDataBean.setStatus(HkSystemConstantUtil.LEAVE_STATUS_MAP.get(HkSystemConstantUtil.LeaveStatus.PENDING));
            } else if (hkLeaveEntity.getStatus().equals(HkSystemConstantUtil.LeaveStatus.APPROVED)) {
                approvalDataBean.setStatus(HkSystemConstantUtil.LEAVE_STATUS_MAP.get(HkSystemConstantUtil.LeaveStatus.APPROVED));
            } else if (hkLeaveEntity.getStatus().equals(HkSystemConstantUtil.LeaveStatus.DISAPPROVED)) {
                approvalDataBean.setStatus(HkSystemConstantUtil.LEAVE_STATUS_MAP.get(HkSystemConstantUtil.LeaveStatus.DISAPPROVED));
            } else if (hkLeaveEntity.getStatus().equals(HkSystemConstantUtil.LeaveStatus.CANCELED)) {
                approvalDataBean.setStatus(HkSystemConstantUtil.LEAVE_STATUS_MAP.get(HkSystemConstantUtil.LeaveStatus.CANCELED));
            }
            if (hkLeaveEntity.getStatus().equals(HkSystemConstantUtil.LeaveStatus.CANCELED)) {
                approvalDataBean.setRequestType(HkSystemConstantUtil.LeaveStatus.REQUEST_TYPE_CANCELLATION);
            } else {
                approvalDataBean.setRequestType(HkSystemConstantUtil.LeaveStatus.REQUEST_TYPE_LEAVE);
            }
            approvalDataBeans.add(approvalDataBean);
        }
        return approvalDataBeans;
    }

    public void cancelApproveLeave(Long leaveId) {
        employeeService.cancelApprovedLeave(leaveId, loginDataBean.getId(), "");
    }

    public List<ApprovalDataBean> searchLeaveForApprover(String searchWord) {
        List<Long> applierIds = umWrapper.searchUsersByName(searchWord, loginDataBean.getCompanyId());
        List<HkLeaveApprovalEntity> searchLeavesForApprover = employeeService.searchLeavesForApprover(searchWord, applierIds, loginDataBean.getDepartment(), loginDataBean.getId(), loginDataBean.getCompanyId());
        List<ApprovalDataBean> approvalDataBeans = new ArrayList<>();
        Map<Long, UMUser> allUser = umWrapper.retrieveUsersByFranchise(loginDataBean.getCompanyId());
        Map<Long, String> allDepts = umWrapper.retrieveDepartmentsByFranchise(loginDataBean.getCompanyId());
        if (searchLeavesForApprover != null && !CollectionUtils.isEmpty(searchLeavesForApprover)) {
            for (HkLeaveApprovalEntity hkLeaveApprovalEntity : searchLeavesForApprover) {
                HkLeaveEntity hkLeaveEntity = hkLeaveApprovalEntity.getLeaveRequest();
                if (hkLeaveEntity != null) {
                    ApprovalDataBean approvalDataBean = new ApprovalDataBean();
                    approvalDataBean.setId(hkLeaveEntity.getId());
                    if (allUser.containsKey(hkLeaveEntity.getForUser())) {
                        UMUser uMUser = allUser.get(hkLeaveEntity.getForUser());
                        if (uMUser.getLastName() != null) {
                            approvalDataBean.setRequestFrom(uMUser.getUserCode() + "-" + uMUser.getFirstName() + " " + uMUser.getLastName());
                        } else {
                            approvalDataBean.setRequestFrom(uMUser.getUserCode() + "-" + uMUser.getFirstName());
                        }
                        approvalDataBean.setDepartment(allDepts.get(uMUser.getDepartment()));
                    }
                    approvalDataBean.setReason(getReasonById(hkLeaveEntity.getLeaveReason()));
                    approvalDataBean.setFromDate(hkLeaveEntity.getFrmDt());
                    approvalDataBean.setToDate(hkLeaveEntity.getToDt());
                    approvalDataBean.setApplyedOn(hkLeaveEntity.getCreatedOn());
                    approvalDataBean.setDescription(hkLeaveEntity.getDescription());
                    approvalDataBean.setDays(hkLeaveEntity.getTotalDays());
                    approvalDataBean.setDescription(hkLeaveEntity.getDescription());
                    if (hkLeaveEntity.getStatus().equals(HkSystemConstantUtil.LeaveStatus.CANCELED)) {
                        approvalDataBean.setRequestType(HkSystemConstantUtil.LeaveStatus.REQUEST_TYPE_CANCELLATION);
                    } else {
                        approvalDataBean.setRequestType(HkSystemConstantUtil.LeaveStatus.REQUEST_TYPE_LEAVE);
                    }
                    approvalDataBean.setReason(getReasonById(hkLeaveEntity.getLeaveReason()));
                    if (hkLeaveEntity.getStatus().equals(HkSystemConstantUtil.LeaveStatus.PENDING)
                            || (hkLeaveEntity.getStatus().equals(HkSystemConstantUtil.LeaveStatus.APPROVED) && hkLeaveEntity.getFrmDt().compareTo(new Date()) >= 0)) {
                        approvalDataBean.setEdit(Boolean.TRUE);
                    } else {
                        approvalDataBean.setEdit(Boolean.FALSE);
                    }
                    if (hkLeaveApprovalEntity.getStatus().equals(HkSystemConstantUtil.LeaveStatus.PENDING)) {
                        approvalDataBean.setStatus(HkSystemConstantUtil.LEAVE_STATUS_MAP.get(HkSystemConstantUtil.LeaveStatus.PENDING));
                    } else if (hkLeaveApprovalEntity.getStatus().equals(HkSystemConstantUtil.LeaveStatus.APPROVED)) {
                        approvalDataBean.setStatus(HkSystemConstantUtil.LEAVE_STATUS_MAP.get(HkSystemConstantUtil.LeaveStatus.APPROVED));
                    } else if (hkLeaveApprovalEntity.getStatus().equals(HkSystemConstantUtil.LeaveStatus.CANCELED)) {
                        approvalDataBean.setStatus(HkSystemConstantUtil.LEAVE_STATUS_MAP.get(HkSystemConstantUtil.LeaveStatus.CANCELED));
                    } else if (hkLeaveApprovalEntity.getStatus().equals(HkSystemConstantUtil.LeaveStatus.DISAPPROVED)) {
                        approvalDataBean.setStatus(HkSystemConstantUtil.LEAVE_STATUS_MAP.get(HkSystemConstantUtil.LeaveStatus.DISAPPROVED));
                    }
                    /* if (hkLeaveEntity.getStatus().equals(HkSystemConstantUtil.LeaveStatus.PENDING)) {
                     approvalDataBean.setStatus(HkSystemConstantUtil.LEAVE_STATUS_MAP.get(HkSystemConstantUtil.LeaveStatus.PENDING));
                     } else if (hkLeaveEntity.getStatus().equals(HkSystemConstantUtil.LeaveStatus.APPROVED)) {
                     approvalDataBean.setStatus(HkSystemConstantUtil.LEAVE_STATUS_MAP.get(HkSystemConstantUtil.LeaveStatus.APPROVED));
                     } else if (hkLeaveEntity.getStatus().equals(HkSystemConstantUtil.LeaveStatus.DISAPPROVED)) {
                     approvalDataBean.setStatus(HkSystemConstantUtil.LEAVE_STATUS_MAP.get(HkSystemConstantUtil.LeaveStatus.DISAPPROVED));
                     } else if (hkLeaveEntity.getStatus().equals(HkSystemConstantUtil.LeaveStatus.CANCELED)) {
                     approvalDataBean.setStatus(HkSystemConstantUtil.LEAVE_STATUS_MAP.get(HkSystemConstantUtil.LeaveStatus.CANCELED));
                     }*/
                    approvalDataBean.setApplyedOn(hkLeaveEntity.getCreatedOn());
                    approvalDataBeans.add(approvalDataBean);
                }
            }
        }
        return approvalDataBeans;

    }

    public List<ApprovalDataBean> searchLeave(String searchWord, Long userId, Long franchise) {
        List<HkLeaveEntity> leaveEntitys = employeeService.searchMyLeaves(searchWord, userId, franchise);
        List<ApprovalDataBean> approvalDataBeans = new ArrayList<>();
        String deptName = "";
        String requestFrom = "";
        if (leaveEntitys != null && !leaveEntitys.isEmpty()) {
            try {
                UMUser user = userService.retrieveUserById(userId, null);
                requestFrom = user.getUserCode() + "-" + user.getFirstName() + " " + user.getLastName();
                UMDepartment department = departmentService.retrieveDepartmentById(user.getDepartment(), false, false, false);
                deptName = department.getDeptName();

            } catch (GenericDatabaseException ex) {
                Logger.getLogger(LeaveTransformerBean.class
                        .getName()).log(Level.SEVERE, null, ex);
            }

        }

        for (HkLeaveEntity hkLeaveEntity : leaveEntitys) {
            ApprovalDataBean approvalDataBean = new ApprovalDataBean();
            approvalDataBean.setId(hkLeaveEntity.getId());
            approvalDataBean.setRequestFrom(requestFrom);
            approvalDataBean.setReason(getReasonById(hkLeaveEntity.getLeaveReason()));
            approvalDataBean.setFromDate(HkSystemFunctionUtil.convertToClientDate(hkLeaveEntity.getFrmDt(), loginDataBean.getClientRawOffsetInMin()));
            approvalDataBean.setToDate(HkSystemFunctionUtil.convertToClientDate(hkLeaveEntity.getToDt(), loginDataBean.getClientRawOffsetInMin()));
            approvalDataBean.setDepartment(deptName);
            approvalDataBean.setApplyedOn(hkLeaveEntity.getCreatedOn());
            approvalDataBean.setDescription(hkLeaveEntity.getDescription());
            approvalDataBean.setReasonId(hkLeaveEntity.getLeaveReason());
            approvalDataBean.setDays(hkLeaveEntity.getTotalDays());
            if (hkLeaveEntity.getStatus().equals(HkSystemConstantUtil.LeaveStatus.PENDING)
                    || (hkLeaveEntity.getStatus().equals(HkSystemConstantUtil.LeaveStatus.APPROVED) && hkLeaveEntity.getFrmDt().compareTo(new Date()) >= 0)) {
                approvalDataBean.setEdit(Boolean.TRUE);
            } else {
                approvalDataBean.setEdit(Boolean.FALSE);
            }
            if (hkLeaveEntity.getStatus().equals(HkSystemConstantUtil.LeaveStatus.CANCELED)) {
                approvalDataBean.setRequestType(HkSystemConstantUtil.LeaveStatus.REQUEST_TYPE_CANCELLATION);
            } else {
                approvalDataBean.setRequestType(HkSystemConstantUtil.LeaveStatus.REQUEST_TYPE_LEAVE);
            }
            if (hkLeaveEntity.getStatus().equals(HkSystemConstantUtil.LeaveStatus.PENDING)) {
                approvalDataBean.setStatus(HkSystemConstantUtil.LEAVE_STATUS_MAP.get(HkSystemConstantUtil.LeaveStatus.PENDING));
            } else if (hkLeaveEntity.getStatus().equals(HkSystemConstantUtil.LeaveStatus.APPROVED)) {
                approvalDataBean.setStatus(HkSystemConstantUtil.LEAVE_STATUS_MAP.get(HkSystemConstantUtil.LeaveStatus.APPROVED));
            } else if (hkLeaveEntity.getStatus().equals(HkSystemConstantUtil.LeaveStatus.DISAPPROVED)) {
                approvalDataBean.setStatus(HkSystemConstantUtil.LEAVE_STATUS_MAP.get(HkSystemConstantUtil.LeaveStatus.DISAPPROVED));
            } else if (hkLeaveEntity.getStatus().equals(HkSystemConstantUtil.LeaveStatus.CANCELED)) {
                approvalDataBean.setStatus(HkSystemConstantUtil.LEAVE_STATUS_MAP.get(HkSystemConstantUtil.LeaveStatus.CANCELED));
            }
            approvalDataBeans.add(approvalDataBean);
        }
        return approvalDataBeans;
    }

    public List<DepartmentDataBean> searchDepartment(String search, Long companyId) {
        Map<Long, String> departments = userManagementServiceWrapper.searchDepartments(search, companyId);
        List<DepartmentDataBean> departmentdatabeans = new ArrayList<>();

        if (!CollectionUtils.isEmpty(departments)) {
            for (Map.Entry<Long, String> entry : departments.entrySet()) {
                DepartmentDataBean departmentdatabean = new DepartmentDataBean();
                departmentdatabean.setId(entry.getKey());
                departmentdatabean.setDisplayName(entry.getValue());
                departmentdatabeans.add(departmentdatabean);
            }
        }
        return departmentdatabeans;
    }

    public ApprovalDataBean searchLeaveForRespondById(Long leaveId) throws GenericDatabaseException {
        List<HkLeaveApprovalEntity> retrieveLeaveApprovalList = employeeService.retrieveLeaveApprovalEntitiesByCriteria(leaveId, null, null, null, null);
        ApprovalDataBean approvalDataBean = new ApprovalDataBean();
        if (retrieveLeaveApprovalList != null && retrieveLeaveApprovalList.size() > 0) {
            HkLeaveApprovalEntity leaveApproval = retrieveLeaveApprovalList.get(0);
            UMUser user = userService.retrieveUserById(leaveApproval.getLeaveRequest().getForUser(), null);
            UMDepartment department = departmentService.retrieveDepartmentById(user.getDepartment(), false, false, false);

            approvalDataBean.setId(leaveApproval.getLeaveRequest().getId());
            approvalDataBean.setUserId(leaveApproval.getLeaveRequest().getForUser());
            approvalDataBean.setRequestFrom(user.getUserCode() + "-" + user.getFirstName() + " " + user.getLastName());
            approvalDataBean.setDepartment(department.getDeptName());
            approvalDataBean.setFromDate(leaveApproval.getLeaveRequest().getFrmDt());
            approvalDataBean.setToDate(leaveApproval.getLeaveRequest().getToDt());
            approvalDataBean.setDays(leaveApproval.getLeaveRequest().getTotalDays());
            approvalDataBean.setDescription(leaveApproval.getLeaveRequest().getDescription());
            if (leaveApproval.getLeaveRequest().getStatus().equals(HkSystemConstantUtil.LeaveStatus.CANCELED)) {
                approvalDataBean.setRequestType(HkSystemConstantUtil.LeaveStatus.REQUEST_TYPE_CANCELLATION);
            } else {
                approvalDataBean.setRequestType(HkSystemConstantUtil.LeaveStatus.REQUEST_TYPE_LEAVE);
            }
            approvalDataBean.setReason(getReasonById(leaveApproval.getLeaveRequest().getLeaveReason()));
            if (leaveApproval.getStatus().equals(HkSystemConstantUtil.LeaveStatus.PENDING)) {
                approvalDataBean.setStatus(HkSystemConstantUtil.LEAVE_STATUS_MAP.get(HkSystemConstantUtil.LeaveStatus.PENDING));
            } else if (leaveApproval.getStatus().equals(HkSystemConstantUtil.LeaveStatus.APPROVED)) {
                approvalDataBean.setStatus(HkSystemConstantUtil.LEAVE_STATUS_MAP.get(HkSystemConstantUtil.LeaveStatus.APPROVED));
            } else if (leaveApproval.getStatus().equals(HkSystemConstantUtil.LeaveStatus.CANCELED)) {
                approvalDataBean.setStatus(HkSystemConstantUtil.LEAVE_STATUS_MAP.get(HkSystemConstantUtil.LeaveStatus.CANCELED));
            }
            Map<Long, Map<HkSystemConstantUtil.SectionNameForCustomField, List<Map<Long, Map<String, Object>>>>> retrieveDocumentByInstanceIds = customFieldSevice.retrieveDocumentByInstanceIds(null, HkSystemConstantUtil.FeatureNameForCustomField.LEAVE, loginDataBean.getCompanyId());
            if (retrieveDocumentByInstanceIds != null) {
                Map<HkSystemConstantUtil.SectionNameForCustomField, List<Map<Long, Map<String, Object>>>> sectionWiseMap = retrieveDocumentByInstanceIds.get(leaveApproval.getLeaveRequest().getId());
                if (sectionWiseMap != null) {
                    List<Map<Long, Map<String, Object>>> maps = sectionWiseMap.get(HkSystemConstantUtil.SectionNameForCustomField.GENERAL);
                    if (maps != null) {
                        for (Map<Long, Map<String, Object>> map : maps) {
                            approvalDataBean.setLeaveCustom(map.get(leaveApproval.getLeaveRequest().getId()));
                        }
                    }
                }
            }
            if (approvalDataBean.getLeaveCustom() == null) {
                approvalDataBean.setLeaveCustom(new HashMap<String, Object>());
            }
            approvalDataBean.setApplyedOn(leaveApproval.getLeaveRequest().getCreatedOn());
        }
        return approvalDataBean;
    }

    public List<SelectItem> retrieveLeaveReason() {
        List<HkValueEntity> valueEntitys = foundationService.retrieveMasterValuesByCode(loginDataBean.getCompanyId(), Arrays.asList(HkSystemConstantUtil.MasterCode.LEAVE_REASON));
        List<SelectItem> leaveReason = new ArrayList<>();
        if (!CollectionUtils.isEmpty(valueEntitys)) {
            for (HkValueEntity hkValueEntity : valueEntitys) {
                if (hkValueEntity.getIsActive()) {
                    String valueName = hkValueEntity.getValueName();
                    if (hkValueEntity.getTranslatedValueName() != null && !hkValueEntity.getTranslatedValueName().isEmpty()) {
                        valueName = hkValueEntity.getTranslatedValueName();
                    }
                    if (hkValueEntity.getShortcutCode() != null) {
                        leaveReason.add(new SelectItem(hkValueEntity.getId(), valueName, hkValueEntity.getShortcutCode()));
                    } else {
                        leaveReason.add(new SelectItem(hkValueEntity.getId(), valueName, 0));
                    }
                }
            }
        }
        return leaveReason;
    }

    public ApprovalDataBean addCustomDataToLeaveDb(ApprovalDataBean approvalDataBean) {
        Map<HkSystemConstantUtil.SectionNameForCustomField, List<Map<Long, Map<String, Object>>>> retrieveDocumentByInstanceId = customFieldTransformerBean.retrieveDocumentByInstanceId(approvalDataBean.getId(), HkSystemConstantUtil.FeatureNameForCustomField.LEAVE, loginDataBean.getCompanyId());
        if (!CollectionUtils.isEmpty(retrieveDocumentByInstanceId)) {
            List<Map<Long, Map<String, Object>>> maps = retrieveDocumentByInstanceId.get(HkSystemConstantUtil.SectionNameForCustomField.GENERAL);
            if (maps != null) {
                for (Map<Long, Map<String, Object>> map : maps) {
                    approvalDataBean.setLeaveCustom(map.get(approvalDataBean.getId()));
                    //System.out.println("customs----" + approvalDataBean.getLeaveCustom());
                }
            }
        }
        return approvalDataBean;
    }

    public void sendCommentNotification(Long id, String comment, Long leaveId, Long approvalId) {
        HkLeaveEntity leave = employeeService.retrieveLeaveById(leaveId, false);
        if (leave != null) {
            leave.setFinalRemarks(comment);
        }
        UMUser user = userService.retrieveUserById(loginDataBean.getId(), loginDataBean.getCompanyId());
        String empCode = null;
        String empName = null;
        if (user != null) {
            empCode = user.getUserCode();
            empName = user.getUserId();
        }
        employeeService.updateLeave(leave, empCode, empName, null, Arrays.asList(id));
        HkLeaveApprovalEntity leaveApprovalEntity = employeeService.retrieveHkLeaveApprovalEntityById(approvalId);
        if (leaveApprovalEntity != null) {
            leaveApprovalEntity.setRemarks(comment);
            employeeService.updateHkLeaveApprovalEntity(leaveApprovalEntity);
        }
    }

    public List<String> retrievePendingStock(Long userId) {
        // MM - Change this logic to show lots/packets which are in stock of specified user
        List<HkLotDocument> lots =  null; //allotmentService.retrieveLotsOrPacketsFromWorkAllocation(null, Arrays.asList(userId), null, Arrays.asList(HkSystemConstantUtil.ActivityServiceStatus.IN_PROCESS), null, loginDataBean.getCompanyId(), Boolean.FALSE, Boolean.TRUE);
        List<HkPacketDocument> packets = null; //allotmentService.retrieveLotsOrPacketsFromWorkAllocation(null, Arrays.asList(userId), null, Arrays.asList(HkSystemConstantUtil.ActivityServiceStatus.IN_PROCESS), null, loginDataBean.getCompanyId(), Boolean.TRUE, Boolean.TRUE);

        List<String> result = new ArrayList<>();

        if (!CollectionUtils.isEmpty(lots)) {
            for (HkLotDocument hkLotDocument : lots) {
                if (hkLotDocument.getFieldValue() != null) {
                    Map fieldMap = hkLotDocument.getFieldValue().toMap();

                    if (fieldMap.get(HkSystemConstantUtil.AutoNumber.LOT_ID) != null) {
                        result.add(fieldMap.get(HkSystemConstantUtil.AutoNumber.LOT_ID).toString());
                    }
                }
            }
        }
        if (!CollectionUtils.isEmpty(packets)) {
            for (HkPacketDocument packetDocument : packets) {
                if (packetDocument.getFieldValue() != null) {
                    Map fieldMap = packetDocument.getFieldValue().toMap();

                    if (fieldMap.get(HkSystemConstantUtil.AutoNumber.PACKET_ID) != null) {
                        result.add(fieldMap.get(HkSystemConstantUtil.AutoNumber.PACKET_ID).toString());
                    }
                }
            }
        }

        return result;
    }

    public LeaveDataBean retrieveById(Long primaryKey) {
        LeaveDataBean leaveDataBean = new LeaveDataBean();

        HkLeaveEntity leave = employeeService.retrieveLeaveById(primaryKey, true);
        List<LeaveDataBean> dataBeans = this.convertLeaveEntityToDataBean(Arrays.asList(leave), null);
        if (!CollectionUtils.isEmpty(dataBeans)) {
            leaveDataBean = dataBeans.get(0);
            Map<HkSystemConstantUtil.SectionNameForCustomField, List<Map<Long, Map<String, Object>>>> customFields = customFieldTransformerBean.retrieveDocumentByInstanceId(leave.getId(), HkSystemConstantUtil.FeatureNameForCustomField.LEAVE, loginDataBean.getCompanyId());
            if (!CollectionUtils.isEmpty(customFields)) {
                if (customFields != null) {
                    List<Map<Long, Map<String, Object>>> maps = customFields.get(HkSystemConstantUtil.SectionNameForCustomField.GENERAL);
                    if (maps != null) {
                        for (Map<Long, Map<String, Object>> map : maps) {
                            leaveDataBean.setLeaveCustom(map.get(leave.getId()));
                        }
                    }
                }
            }
        }

        return leaveDataBean;
    }

    public ApprovalDataBean retrieveApprovalByID(Long id) {
        HkLeaveApprovalEntity approver = employeeService.retrieveHkLeaveApprovalEntityById(id);

        Map<Long, UMUser> allUser = umWrapper.retrieveUsersByFranchise(loginDataBean.getCompanyId());
        Map<Long, String> allDepts = umWrapper.retrieveDepartmentsByFranchise(loginDataBean.getCompanyId());

        ApprovalDataBean approvalDataBean = new ApprovalDataBean();
        approvalDataBean.setApprovalId(approver.getId());
        approvalDataBean.setId(approver.getLeaveRequest().getId());
        approvalDataBean.setUserId(approver.getLeaveRequest().getForUser());
        if (allUser.containsKey(approver.getLeaveRequest().getForUser())) {
            UMUser uMUser = allUser.get(approver.getLeaveRequest().getForUser());
            if (uMUser.getLastName() != null) {
                approvalDataBean.setRequestFrom(uMUser.getUserCode() + "-" + uMUser.getFirstName() + " " + uMUser.getLastName());
            } else {
                approvalDataBean.setRequestFrom(uMUser.getUserCode() + "-" + uMUser.getFirstName());
            }
            approvalDataBean.setDepartment(allDepts.get(uMUser.getDepartment()));
        }
        approvalDataBean.setFromDate(HkSystemFunctionUtil.convertToClientDate(approver.getLeaveRequest().getFrmDt(), loginDataBean.getClientRawOffsetInMin()));
        approvalDataBean.setToDate(HkSystemFunctionUtil.convertToClientDate(approver.getLeaveRequest().getToDt(), loginDataBean.getClientRawOffsetInMin()));
        approvalDataBean.setDays(approver.getLeaveRequest().getTotalDays());
        approvalDataBean.setDescription(approver.getLeaveRequest().getDescription());
        if (approver.getLeaveRequest().getStatus().equals(HkSystemConstantUtil.LeaveStatus.CANCELED)) {
            approvalDataBean.setRequestType(HkSystemConstantUtil.LeaveStatus.REQUEST_TYPE_CANCELLATION);
        } else {
            approvalDataBean.setRequestType(HkSystemConstantUtil.LeaveStatus.REQUEST_TYPE_LEAVE);
        }
        approvalDataBean.setReason(getReasonById(approver.getLeaveRequest().getLeaveReason()));
        if (approver.getStatus().equals(HkSystemConstantUtil.LeaveStatus.PENDING)) {
            approvalDataBean.setStatus(HkSystemConstantUtil.LEAVE_STATUS_MAP.get(HkSystemConstantUtil.LeaveStatus.PENDING));
        } else if (approver.getStatus().equals(HkSystemConstantUtil.LeaveStatus.APPROVED)) {
            approvalDataBean.setStatus(HkSystemConstantUtil.LEAVE_STATUS_MAP.get(HkSystemConstantUtil.LeaveStatus.APPROVED));
        } else if (approver.getStatus().equals(HkSystemConstantUtil.LeaveStatus.CANCELED)) {
            approvalDataBean.setStatus(HkSystemConstantUtil.LEAVE_STATUS_MAP.get(HkSystemConstantUtil.LeaveStatus.CANCELED));
        } else if (approver.getStatus().equals(HkSystemConstantUtil.LeaveStatus.DISAPPROVED)) {
            approvalDataBean.setStatus(HkSystemConstantUtil.LEAVE_STATUS_MAP.get(HkSystemConstantUtil.LeaveStatus.DISAPPROVED));
        }
        if (approvalDataBean.getLeaveCustom() == null) {
            approvalDataBean.setLeaveCustom(new HashMap<>());
        }
        approvalDataBean.setApplyedOn(approver.getLeaveRequest().getCreatedOn());
        approvalDataBean.setRemarks(approver.getRemarks());
        Map<HkSystemConstantUtil.SectionNameForCustomField, List<Map<Long, Map<String, Object>>>> customFields = customFieldTransformerBean.retrieveDocumentByInstanceId(approver.getId(), HkSystemConstantUtil.FeatureNameForCustomField.RESPONDLEAVE, loginDataBean.getCompanyId());
        if (!CollectionUtils.isEmpty(customFields)) {
            if (customFields != null) {
                List<Map<Long, Map<String, Object>>> maps = customFields.get(HkSystemConstantUtil.SectionNameForCustomField.GENERAL);
                if (maps != null) {
                    for (Map<Long, Map<String, Object>> map : maps) {
                        approvalDataBean.setApproverCustom(map.get(approver.getId()));
                    }
                }
            }
        }
        Map<HkSystemConstantUtil.SectionNameForCustomField, List<Map<Long, Map<String, Object>>>> customFields1 = customFieldTransformerBean.retrieveDocumentByInstanceId(approver.getLeaveRequest().getId(), HkSystemConstantUtil.FeatureNameForCustomField.LEAVE, loginDataBean.getCompanyId());
        if (!CollectionUtils.isEmpty(customFields1)) {
            if (customFields1 != null) {
                List<Map<Long, Map<String, Object>>> maps = customFields1.get(HkSystemConstantUtil.SectionNameForCustomField.GENERAL);
                if (maps != null) {
                    for (Map<Long, Map<String, Object>> map : maps) {
                        approvalDataBean.setLeaveCustom(map.get(approver.getId()));
                    }
                }
            }
        }
        return approvalDataBean;
    }
}
