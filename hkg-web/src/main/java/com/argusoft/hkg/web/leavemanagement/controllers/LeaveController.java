/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.leavemanagement.controllers;

import com.argusoft.generic.database.exception.GenericDatabaseException;
import com.argusoft.hkg.common.constantutil.HkSystemConstantUtil;
import com.argusoft.hkg.web.base.PrimaryKey;
import com.argusoft.hkg.web.base.ResponseCode;
import com.argusoft.hkg.web.base.ResponseEntity;
import com.argusoft.hkg.web.base.controllers.BaseController;
import com.argusoft.hkg.web.leavemanagement.databeans.ApprovalDataBean;
import com.argusoft.hkg.web.leavemanagement.databeans.LeaveDataBean;
import com.argusoft.hkg.web.usermanagement.databeans.DepartmentDataBean;
import com.argusoft.hkg.web.center.usermanagement.databeans.LoginDataBean;
import com.argusoft.hkg.web.leavemanagement.databeans.LeaveWorkflowDataBean;
import com.argusoft.hkg.web.leavemanagement.databeans.RespondLeaveDataBean;
import com.argusoft.hkg.web.leavemanagement.transformers.LeaveTransformerBean;
import com.argusoft.hkg.web.usermanagement.transformers.MessagingTransformerBean;
import com.argusoft.hkg.web.util.SelectItem;
import com.argusoft.hkg.web.util.TreeViewDataBean;
import com.argusoft.usermanagement.common.model.UMDepartment;
import com.argusoft.usermanagement.common.model.UMUser;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Mansi Parekh
 */
@RestController
@RequestMapping("/leave")
public class LeaveController extends BaseController<LeaveDataBean, Long> {

    @Autowired
    LeaveTransformerBean leaveTransformerBean;
    @Autowired
    LoginDataBean loginDataBean;
    @Autowired
    MessagingTransformerBean messagingTransformerBean;

    /**
     * Mansi Parekh
     * @param user search string
     * @return List of users
     */
    @RequestMapping(value = "/workflow/retrieve/users", method = RequestMethod.POST, consumes = {"application/json"})
    public ResponseEntity<List<SelectItem>> retrieveUsers(@RequestBody(required = false) String user) {

        Long companyId = loginDataBean.getCompanyId();
        List<UMUser> umUsers = leaveTransformerBean.retrieveUsersByCompanyByStatus(companyId, user, Boolean.TRUE, true);
        List<SelectItem> hkSelectItems = messagingTransformerBean.getSelectItemListFromUserList(umUsers);
        return new ResponseEntity<>(hkSelectItems, ResponseCode.SUCCESS, "", null);
    }

    /**
     * Mansi Parekh
     * @param department search string
     * @return List of departments
     */
    @RequestMapping(value = "/workflow/retrieve/departments", method = RequestMethod.POST, consumes = {"application/json"})
    public ResponseEntity<List<SelectItem>> retrieveDepartments(@RequestBody(required = false) String department) {
        Long companyId = loginDataBean.getCompanyId();
        List<UMDepartment> uMDepartments = leaveTransformerBean.retrieveDepartmentsByCompanyByStatus(companyId, department, Boolean.TRUE);
        List<SelectItem> hkSelectItems = new ArrayList<>();
        if (!CollectionUtils.isEmpty(uMDepartments)) {
            for (UMDepartment uMDepartment : uMDepartments) {
                hkSelectItems.add(new SelectItem(uMDepartment.getId(), uMDepartment.getDeptName(), HkSystemConstantUtil.RecipientCodeType.DEPARTMENT));
            }
        }
        return new ResponseEntity<>(hkSelectItems, ResponseCode.SUCCESS, "", null);
    }

    /**
     * Mansi Parekh
     *
     * @return
     */
    @RequestMapping(value = "/workflow/retrievedepartmentcombine", method = RequestMethod.GET)
    public ResponseEntity<Map> retrieveDepartmentsCombineMethod() {
        Long company = loginDataBean.getCompanyId();
        Map map = leaveTransformerBean.retrieveDepartmentsInTreeView(company);
        TreeViewDataBean treeViewDataBean = new TreeViewDataBean();
        treeViewDataBean.setText("ROOT");
        treeViewDataBean.setIsChecked(true);
        treeViewDataBean.setItems(leaveTransformerBean.retrieveDepartmentsInMultiSelectTreeView(company));
        map.put("tree3", treeViewDataBean);
        return new ResponseEntity<>(map, ResponseCode.SUCCESS, "", null);
    }

    @RequestMapping(value = "/searchDepartment", method = RequestMethod.GET, produces = {"application/json"})
    public List<DepartmentDataBean> searchDepartment(@RequestParam("q") String searchWord) throws IOException {
        Long franchise = loginDataBean.getCompanyId();
        return leaveTransformerBean.searchDepartment(searchWord, franchise);
    }

    /**
     * Mansi Parekh
     */
    @RequestMapping(value = "/workflow/create", method = RequestMethod.POST, consumes = {"application/json"})
    public ResponseEntity<PrimaryKey<Long>> createWorkflow(@RequestBody Map workflowDetailsMap) {
        Long franchise = loginDataBean.getCompanyId();
        Long userId = loginDataBean.getId();
        List<Integer> ids = (ArrayList<Integer>) workflowDetailsMap.get("departmentIds");
        Long[] deptIds = new Long[ids.size()];
        for (int i = 0; i < ids.size(); i++) {
            Long long1 = Long.parseLong(ids.get(i).toString());
            deptIds[i] = long1;
        }
        String approvers = (String) workflowDetailsMap.get("approvers");
        Map<String, Object> leaveWorkflowCustom = (Map<String, Object>) workflowDetailsMap.get("leaveworkflowcustom");
        Map<String, String> leaveWorkflowDbType = (Map<String, String>) workflowDetailsMap.get("leaveworkflowdbtypes");
        String response = leaveTransformerBean.createWorkflow(userId, franchise, deptIds, approvers, leaveWorkflowCustom, leaveWorkflowDbType);
        if (response.equalsIgnoreCase(HkSystemConstantUtil.SUCCESS)) {
            return new ResponseEntity<>(null, ResponseCode.SUCCESS, "Workflow is successfully created", null);
        } else {
            return new ResponseEntity<>(null, ResponseCode.FAILURE, "Failed to create workflow", null);
        }
    }

    /**
     * Mansi Parekh
     */
    @RequestMapping(value = "/workflow/update", method = RequestMethod.POST, consumes = {"application/json"})
    public ResponseEntity<PrimaryKey<Long>> updateWorkflow(@RequestBody Map workflowDetailsMap) {
        Long franchise = loginDataBean.getCompanyId();
        Long userId = loginDataBean.getId();
        String id = (String) workflowDetailsMap.get("departmentId");
        Long long1 = Long.parseLong(id);
        Long deptId = long1;
        if (deptId.equals(0l)) {
            deptId = null;
        }

        String approvers = (String) workflowDetailsMap.get("approvers");
        Map<String, Object> leaveWorkflowCustom = (Map<String, Object>) workflowDetailsMap.get("leaveworkflowcustom");
        Map<String, String> leaveWorkflowDbType = (Map<String, String>) workflowDetailsMap.get("leaveworkflowdbtypes");
        String response = leaveTransformerBean.updateWorkflow(userId, franchise, deptId, approvers, leaveWorkflowCustom, leaveWorkflowDbType);
        if (response.equalsIgnoreCase(HkSystemConstantUtil.SUCCESS)) {
            return new ResponseEntity<>(null, ResponseCode.SUCCESS, "Workflow is successfully updated", null);
        } else {
            return new ResponseEntity<>(null, ResponseCode.FAILURE, "Failed to update workflow", null);
        }
    }

    /**
     * Mansi Parekh
     */
    @RequestMapping(value = "/workflow/delete", method = RequestMethod.POST)
    public ResponseEntity<PrimaryKey<Long>> deleteWorkflow(@RequestBody String deptId) {
        Long userId = loginDataBean.getId();
        String response = leaveTransformerBean.deleteWorkflow(userId, Long.parseLong(deptId));
        if (response.equalsIgnoreCase(HkSystemConstantUtil.SUCCESS)) {
            return new ResponseEntity<>(null, ResponseCode.SUCCESS, "Workflow is successfully deleted", null);
        } else {
            return new ResponseEntity<>(null, ResponseCode.FAILURE, "Failed to update workflow", null);
        }
    }

    /**
     * Mansi Parekh
     */
    @RequestMapping(value = "/workflow/retrieveWorkflowByDepId", method = RequestMethod.POST)
    public ResponseEntity<LeaveWorkflowDataBean> retrieveWorkflowByDepartmentId(@RequestBody String depId) {
        Long franchise = loginDataBean.getCompanyId();
        Long dep = null;
        if (depId != null && !depId.equals("0")) {
            dep = new Long(depId);
        }
        LeaveWorkflowDataBean leaveWorkflowDataBean = leaveTransformerBean.retrieveWorkflowByDepartmentId(dep, franchise);
        return new ResponseEntity<>(leaveWorkflowDataBean, ResponseCode.SUCCESS, "", null);
    }

    /**
     * vipul vaghela
     */
    @Override
    public ResponseEntity<List<LeaveDataBean>> retrieveAll() {
        return new ResponseEntity<List<LeaveDataBean>>(leaveTransformerBean.retrieveAllLeave(), ResponseCode.SUCCESS, null, null, false);
    }

    /**
     * vipul vaghela
     */
    @Override
    public ResponseEntity<LeaveDataBean> retrieveById(PrimaryKey<Long> primaryKey) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * vipul vaghela
     */
    @Override
    public ResponseEntity<PrimaryKey<Long>> update(@Valid @RequestBody LeaveDataBean leaveDatabean) {
        if (leaveDatabean.getFromDate().compareTo(leaveDatabean.getToDate()) > 0) {
            return new ResponseEntity<PrimaryKey<Long>>(null, ResponseCode.FAILURE, "To Date can not before from date.", null, false);
        }
        int response = leaveTransformerBean.updateLeave(leaveDatabean);
        if (response == 2) {
            return new ResponseEntity<PrimaryKey<Long>>(null, ResponseCode.FAILURE, "Leave already applied on specified date.", null, false);
        } else if (response == 0) {
            return new ResponseEntity<PrimaryKey<Long>>(null, ResponseCode.FAILURE, "Can not apply leave beyond limit.", null, false);
        } else if (response == 3) {
            return new ResponseEntity<PrimaryKey<Long>>(null, ResponseCode.FAILURE, "Contact admin to set the default limit of leave", null, false);
        } else {
            return new ResponseEntity<PrimaryKey<Long>>(null, ResponseCode.SUCCESS, "Leave updated successfully.", null);
        }
    }

    /**
     * vipul vaghela
     */
    @Override
    public ResponseEntity<PrimaryKey<Long>> create(@Valid @RequestBody LeaveDataBean leavDataBean) {
        if (leavDataBean.getFromDate().compareTo(leavDataBean.getToDate()) > 0) {
            return new ResponseEntity<PrimaryKey<Long>>(null, ResponseCode.FAILURE, "To Date can not before from date.", null, false);
        }

        int applyedLeaveStatus = leaveTransformerBean.applyLeave(leavDataBean);
        if (applyedLeaveStatus == 0) {
            return new ResponseEntity<PrimaryKey<Long>>(null, ResponseCode.FAILURE, "Leave workflow is not configured.", null, false);
        } else if (applyedLeaveStatus == 1) {
            return new ResponseEntity<PrimaryKey<Long>>(null, ResponseCode.FAILURE, "Leave already applied on specified date.", null, false);
        } else if (applyedLeaveStatus == 3) {
            return new ResponseEntity<PrimaryKey<Long>>(null, ResponseCode.FAILURE, "You can not apply leave beyond the limit", null, false);
        } else if (applyedLeaveStatus == 4) {
            return new ResponseEntity<PrimaryKey<Long>>(null, ResponseCode.FAILURE, "Contact admin to configure limit the number of leaves", null, false);
        } else {
            return new ResponseEntity<PrimaryKey<Long>>(null, ResponseCode.SUCCESS, "Leave applied successfully.", null);
        }
    }

    /**
     * vipul vaghela
     */
    @Override
    public ResponseEntity<LeaveDataBean> deleteById(@RequestBody PrimaryKey<Long> primaryKey) {
        leaveTransformerBean.deleteById(primaryKey);
        return new ResponseEntity<LeaveDataBean>(null, ResponseCode.SUCCESS, " Leave cancelled.", null);
    }

    /**
     * vipul vaghela
     */
    @RequestMapping(value = "/retrieveLeaveReason", method = RequestMethod.POST)
    public ResponseEntity<List<SelectItem>> retrieveLeaveReason() {
        List<SelectItem> leaveReason = leaveTransformerBean.retrieveLeaveReason();
        return new ResponseEntity<List<SelectItem>>(leaveReason, ResponseCode.SUCCESS, null, null, false);
    }

    /**
     * vipul vaghela
     */
    @RequestMapping(value = "/archiveLeave", method = RequestMethod.POST)
    public ResponseEntity<LeaveDataBean> archiveLeave(@RequestBody PrimaryKey<Long> primaryKey) {
        leaveTransformerBean.archivedLeave(primaryKey);
        return new ResponseEntity<LeaveDataBean>(null, ResponseCode.SUCCESS, "Leave archived successfully.", null);
    }

    /**
     * vipul vaghela
     */
    @RequestMapping(value = "/retrieveAllApproval", method = RequestMethod.POST)
    public ResponseEntity<List<ApprovalDataBean>> retrieveAllApproval(@RequestBody Boolean isAllLeave) throws GenericDatabaseException {
        List<ApprovalDataBean> approvalDataBeans = leaveTransformerBean.retrieveAllApproval(isAllLeave);
        return new ResponseEntity<List<ApprovalDataBean>>(approvalDataBeans, ResponseCode.SUCCESS, null, null, true);
    }

    /**
     * vipul vaghela
     */
    @RequestMapping(value = "/approveLeave", method = RequestMethod.POST)
    public ResponseEntity<ApprovalDataBean> approveLeave(@RequestBody RespondLeaveDataBean respondLeaveDataBean) throws GenericDatabaseException {
        leaveTransformerBean.approveLeave(respondLeaveDataBean);
        return new ResponseEntity<ApprovalDataBean>(null, ResponseCode.SUCCESS, "Leave approved successfully.", null, true);
    }

    /**
     * vipul vaghela
     */
    @RequestMapping(value = "/disApproveLeave", method = RequestMethod.POST)
    public ResponseEntity<ApprovalDataBean> disApproveLeave(@RequestBody RespondLeaveDataBean respondLeaveDataBean) throws GenericDatabaseException {
        leaveTransformerBean.disApproveLeave(respondLeaveDataBean);
        return new ResponseEntity<ApprovalDataBean>(null, ResponseCode.SUCCESS, "Leave disapproved successfully.", null, true);
    }

    /**
     * vipul vaghela
     */
    @RequestMapping(value = "/retriveAllLeaveByUserId", method = RequestMethod.POST)
    public ResponseEntity<List<ApprovalDataBean>> retriveAllLeaveByUserId(@RequestBody Long userId) {
        return new ResponseEntity<List<ApprovalDataBean>>(leaveTransformerBean.retriveAllLeaveByUserId(userId), ResponseCode.SUCCESS, null, null, true);
    }

    @RequestMapping(value = "/cancelApproveLeave", method = RequestMethod.POST)
    public ResponseEntity<ApprovalDataBean> cancelApproveLeave(@RequestBody Long leaveId) {
        leaveTransformerBean.cancelApproveLeave(leaveId);
        return new ResponseEntity<ApprovalDataBean>(null, ResponseCode.SUCCESS, "Leave canceled successfully.", null, true);
    }

    /**
     * vipul vaghela this method is implemented later.
     */
    @RequestMapping(value = "/searchLeave", method = RequestMethod.GET, produces = {"application/json"})
    public List<ApprovalDataBean> searchLeave(@RequestParam("q") String searchWord) throws IOException {
        Long franchise = loginDataBean.getCompanyId();
        Long userId = loginDataBean.getId();
        return leaveTransformerBean.searchLeave(searchWord, userId, franchise);
    }

    @RequestMapping(value = "/searchLeaveForApprover", method = RequestMethod.GET, produces = {"application/json"})
    public List<ApprovalDataBean> searchLeaveForApprover(@RequestParam("q") String searchWord) throws IOException {
        return leaveTransformerBean.searchLeaveForApprover(searchWord);
    }

    @RequestMapping(value = "/searchLeaveForRespondById", method = RequestMethod.POST, produces = {"application/json"})
    public ApprovalDataBean searchLeaveForRespondById(@RequestBody Long leaveId) throws IOException, GenericDatabaseException {
        return leaveTransformerBean.searchLeaveForRespondById(leaveId);
    }

    @Override
    public ResponseEntity<Map<String, Object>> retrievePrerequisite() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @RequestMapping(value = "/addCustomDataToLeaveDataBean", method = RequestMethod.POST, produces = {"application/json"})
    public ApprovalDataBean addCustomDataToLeaveDataBean(@RequestBody ApprovalDataBean approvalDataBean) {
        //System.out.println("inside customs---->>");
        return leaveTransformerBean.addCustomDataToLeaveDb(approvalDataBean);
    }

    @RequestMapping(value = "/sendCommentNotification", method = RequestMethod.POST)
    public ResponseEntity<String> sendCommentNotification(@RequestBody String[] request) {
        Long userId = Long.parseLong(request[0]);
        String comment = request[1];
        Long leaveId = Long.parseLong(request[2]);
        Long approvalId = Long.parseLong(request[3]);
        leaveTransformerBean.sendCommentNotification(userId, comment, leaveId, approvalId);
        return new ResponseEntity<>(null, ResponseCode.SUCCESS, "Comment on leave saved successfully", null, true);
    }

    @RequestMapping(value = "/retrievePendingStock", method = RequestMethod.POST)
    public Map<String, List<String>> retrievePendingStock(@RequestBody Long userId) {
        Map<String, List<String>> result = new HashMap<>();
        if (userId != null) {
            List<String> stocks = leaveTransformerBean.retrievePendingStock(userId);
            result.put("PendingStock", stocks);
        }
        return result;
    }

    @RequestMapping(value = "/retrieveLeaveById", method = RequestMethod.POST)
    public ResponseEntity<LeaveDataBean> retrieveLeaveById(@RequestBody Long leaveId) {
        return new ResponseEntity<>(leaveTransformerBean.retrieveById(leaveId), ResponseCode.SUCCESS, null, null, true);
    }

    @RequestMapping(value = "/retrieveApprovalByID", method = RequestMethod.POST)
    public ResponseEntity<ApprovalDataBean> retrieveApproverById(@RequestBody Long id) {
        return new ResponseEntity<>(leaveTransformerBean.retrieveApprovalByID(id), ResponseCode.SUCCESS, null, null, true);
    }
}
