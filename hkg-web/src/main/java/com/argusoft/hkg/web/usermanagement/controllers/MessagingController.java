/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.usermanagement.controllers;

import com.argusoft.generic.database.exception.GenericDatabaseException;
import com.argusoft.hkg.common.constantutil.HkSystemConstantUtil;
import com.argusoft.hkg.web.base.PrimaryKey;
import com.argusoft.hkg.web.base.ResponseCode;
import com.argusoft.hkg.web.base.ResponseEntity;
import com.argusoft.hkg.web.base.controllers.BaseController;
import com.argusoft.hkg.web.center.usermanagement.databeans.LoginDataBean;
import com.argusoft.hkg.web.usermanagement.databeans.MessagingDataBean;
import com.argusoft.hkg.web.usermanagement.transformers.MessagingTransformerBean;
import com.argusoft.hkg.web.util.SelectItem;
import com.argusoft.usermanagement.common.exception.UMUserManagementException;
import com.argusoft.usermanagement.common.model.UMDepartment;
import com.argusoft.usermanagement.common.model.UMRole;
import com.argusoft.usermanagement.common.model.UMUser;
import com.argusoft.usermanagement.common.model.UMUserGroup;
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
 * @author mansi
 */
@RestController
@RequestMapping("/messaging")
public class MessagingController extends BaseController<MessagingDataBean, Long> {

    @Autowired
    MessagingTransformerBean messagingTransformerBean;
    @Autowired
    LoginDataBean loginDataBean;

    /*
     * Piyush Sanghani - 24/3/2014
     */
    @RequestMapping(value = "/retrieve/users", method = RequestMethod.POST, consumes = {"application/json"})
    public ResponseEntity<List<SelectItem>> retrieveUsers(@RequestBody(required = false) String user) {

        Long companyId = loginDataBean.getCompanyId();
        List<UMUser> umUsers = messagingTransformerBean.retrieveUsersByCompanyByStatus(companyId, user, Boolean.TRUE);
        List<SelectItem> hkSelectItems = messagingTransformerBean.getSelectItemListFromUserList(umUsers);
        return new ResponseEntity<>(hkSelectItems, ResponseCode.SUCCESS, "", null);
    }

    @RequestMapping(value = "/retrieve/usersbyfranchise", method = RequestMethod.POST, consumes = {"application/json"})
    public ResponseEntity<List<SelectItem>> retrieveUsersByFranchise(@RequestBody Map<String, Object> payload) {
        if (!CollectionUtils.isEmpty(payload)) {
            String user = payload.get("user").toString();
            Long companyId = Long.parseLong(payload.get("franchise").toString());
            List<UMUser> umUsers = messagingTransformerBean.retrieveUsersByCompanyByStatus(companyId, user, Boolean.TRUE);
            List<SelectItem> hkSelectItems = messagingTransformerBean.getSelectItemListFromUserList(umUsers);
            return new ResponseEntity<>(hkSelectItems, ResponseCode.SUCCESS, "", null);
        }
        return null;
    }

    /*
     * Piyush Sanghani - 24/3/2014
     */
    @RequestMapping(value = "/retrieve/roles", method = RequestMethod.POST, consumes = {"application/json"})
    public ResponseEntity<List<SelectItem>> retrieveRoles(@RequestBody(required = false) String role) {

        Long companyId = loginDataBean.getCompanyId();
        List<UMRole> uMRoles = messagingTransformerBean.retrieveRolesByCompanyByStatus(companyId, role, Boolean.TRUE);
        List<SelectItem> hkSelectItems = new ArrayList<>();
        if (!CollectionUtils.isEmpty(uMRoles)) {
            for (UMRole uMRole : uMRoles) {
                hkSelectItems.add(new SelectItem(uMRole.getId(), uMRole.getName(), HkSystemConstantUtil.RecipientCodeType.DESIGNATION));
            }
        }
        return new ResponseEntity<>(hkSelectItems, ResponseCode.SUCCESS, "", null);
    }

    @RequestMapping(value = "/retrieve/rolesbyfranchise", method = RequestMethod.POST, consumes = {"application/json"})
    public ResponseEntity<List<SelectItem>> retrieveRolesByFranchise(@RequestBody Map<String, Object> payload) {
        if (!CollectionUtils.isEmpty(payload)) {
            String role = payload.get("role").toString();
            Long franchise = Long.parseLong(payload.get("franchise").toString());

            List<UMRole> uMRoles = messagingTransformerBean.retrieveRolesByCompanyByStatus(franchise, role, Boolean.TRUE);
            List<SelectItem> hkSelectItems = new ArrayList<>();
            if (!CollectionUtils.isEmpty(uMRoles)) {
                for (UMRole uMRole : uMRoles) {
                    hkSelectItems.add(new SelectItem(uMRole.getId(), uMRole.getName(), HkSystemConstantUtil.RecipientCodeType.DESIGNATION));
                }
            }
            return new ResponseEntity<>(hkSelectItems, ResponseCode.SUCCESS, "", null);
        }
        return null;
    }

    /*
     * Piyush Sanghani - 24/3/2014
     */
    @RequestMapping(value = "/retrieve/departments", method = RequestMethod.POST, consumes = {"application/json"})
    public ResponseEntity<List<SelectItem>> retrieveDepartments(@RequestBody(required = false) String department) {

        Long companyId = loginDataBean.getCompanyId();
        List<UMDepartment> uMDepartments = messagingTransformerBean.retrieveDepartmentsByCompanyByStatus(companyId, department, Boolean.TRUE);
        List<SelectItem> hkSelectItems = new ArrayList<>();
        if (!CollectionUtils.isEmpty(uMDepartments)) {
            for (UMDepartment uMDepartment : uMDepartments) {
                hkSelectItems.add(new SelectItem(uMDepartment.getId(), uMDepartment.getDeptName(), HkSystemConstantUtil.RecipientCodeType.DEPARTMENT));
            }
        }
        return new ResponseEntity<>(hkSelectItems, ResponseCode.SUCCESS, "", null);
    }

    @RequestMapping(value = "/retrieve/departmentsbyfranchise", method = RequestMethod.POST, consumes = {"application/json"})
    public ResponseEntity<List<SelectItem>> retrieveDepartmentsByFranchise(@RequestBody Map<String, Object> payload) {
        if (!CollectionUtils.isEmpty(payload)) {
            String department = payload.get("department").toString();
            Long franchise = Long.parseLong(payload.get("franchise").toString());

            List<UMDepartment> uMDepartments = messagingTransformerBean.retrieveDepartmentsByCompanyByStatus(franchise, department, Boolean.TRUE);
            List<SelectItem> hkSelectItems = new ArrayList<>();
            if (!CollectionUtils.isEmpty(uMDepartments)) {
                for (UMDepartment uMDepartment : uMDepartments) {
                    hkSelectItems.add(new SelectItem(uMDepartment.getId(), uMDepartment.getDeptName(), HkSystemConstantUtil.RecipientCodeType.DEPARTMENT));
                }
            }
            return new ResponseEntity<>(hkSelectItems, ResponseCode.SUCCESS, "", null);
        }
        return null;
    }

    @RequestMapping(value = "/retrieve/departmentsofotherfranchise", method = RequestMethod.POST, consumes = {"application/json"})
    public ResponseEntity<List<SelectItem>> retrieveDepartmentsOfOtherFranchise(@RequestBody String department) {
        return new ResponseEntity<>(messagingTransformerBean.searchDepartmentsOfOtherCompany(department, Boolean.TRUE), ResponseCode.SUCCESS, "", null);
    }

    /*
     * Piyush Sanghani - 24/3/2014
     */
    @RequestMapping(value = "/retrieve/groups", method = RequestMethod.POST, consumes = {"application/json"})
    public ResponseEntity<List<SelectItem>> retrieveGroups(@RequestBody(required = false) String group) {

        Long companyId = loginDataBean.getCompanyId();
        List<UMUserGroup> uMUserGroups = messagingTransformerBean.retrieveGroupsByCompanyByStatus(companyId, group, Boolean.TRUE);
        List<SelectItem> hkSelectItems = new ArrayList<>();
        if (!CollectionUtils.isEmpty(uMUserGroups)) {
            for (UMUserGroup userGroup : uMUserGroups) {
                hkSelectItems.add(new SelectItem(userGroup.getId(), userGroup.getName(), HkSystemConstantUtil.RecipientCodeType.GROUP));
            }
        }
        return new ResponseEntity<>(hkSelectItems, ResponseCode.SUCCESS, "", null);
    }

    /*
     * Piyush Sanghani - 24/3/2014
     */
    @RequestMapping(value = "/retrieve/activities", method = RequestMethod.POST, consumes = {"application/json"})
    public ResponseEntity<List<SelectItem>> retrieveActivities(@RequestBody(required = false) String activity) {

        Long companyId = loginDataBean.getCompanyId();
        List<UMUser> umUsers = messagingTransformerBean.retrieveUsersByCompanyByStatus(companyId, activity, Boolean.TRUE);
        List<SelectItem> hkSelectItems = messagingTransformerBean.getSelectItemListFromUserList(umUsers);
        return new ResponseEntity<>(hkSelectItems, ResponseCode.SUCCESS, "", null);
    }

    @RequestMapping(value = "/savemessage", method = RequestMethod.POST)
    public ResponseEntity<PrimaryKey<Long>> saveMessage(@Valid @RequestBody MessagingDataBean hkMessagingDataBean) throws UMUserManagementException {
        Long franchise = loginDataBean.getCompanyId();
        Long userId = loginDataBean.getId();
        hkMessagingDataBean.setFranchise(franchise);
        String response = messagingTransformerBean.saveMessage(hkMessagingDataBean, userId);
        if (response.equalsIgnoreCase(HkSystemConstantUtil.SUCCESS)) {
            return new ResponseEntity<>(null, ResponseCode.SUCCESS, "Message is successfully sent", null);
        } else {
            return new ResponseEntity<>(null, ResponseCode.FAILURE, "Failed to save message", null);
        }
    }

    @RequestMapping(value = "/savemessagepopup", method = RequestMethod.POST)
    public ResponseEntity<PrimaryKey<Long>> saveMessagePopup(@Valid @RequestBody MessagingDataBean hkMessagingDataBean) throws UMUserManagementException {
        Long franchise = loginDataBean.getCompanyId();
        Long userId = loginDataBean.getId();
        hkMessagingDataBean.setFranchise(franchise);
        String response = messagingTransformerBean.saveMessage(hkMessagingDataBean, userId);
        if (response.equalsIgnoreCase(HkSystemConstantUtil.SUCCESS)) {
            return new ResponseEntity<>(null, ResponseCode.SUCCESS, "", null);
        } else {
            return new ResponseEntity<>(null, ResponseCode.FAILURE, "", null);
        }
    }

    @RequestMapping(value = "/retrieve/messages", method = RequestMethod.GET, produces = {"application/json"})
    public ResponseEntity<List<MessagingDataBean>> retrieveMessages(@RequestParam("message") String message) throws GenericDatabaseException {
        Long companyId = loginDataBean.getCompanyId();
        Long userId = loginDataBean.getId();
        List<MessagingDataBean> messageBeans = messagingTransformerBean.retrieveMessagesByCompanyByStatus(userId, companyId, message, Boolean.FALSE, true);
        return new ResponseEntity<>(messageBeans, ResponseCode.SUCCESS, "", null);
    }

    @RequestMapping(value = "/archiveMessages", method = RequestMethod.POST)
    public ResponseEntity<PrimaryKey<Long>> archiveMessages(@RequestBody List<MessagingDataBean> messages) throws UMUserManagementException {
        String response = messagingTransformerBean.archiveMessage(messages);
        String text = "Message";
        if (messages.size() > 1) {
            text = "Messages";
        }
        if (response.equalsIgnoreCase(HkSystemConstantUtil.SUCCESS)) {
            return new ResponseEntity<>(null, ResponseCode.SUCCESS, text + " archived successfully", null);
        } else {
            return new ResponseEntity<>(null, ResponseCode.FAILURE, "Failed to archive " + text.toLowerCase(), null);
        }
    }

    @RequestMapping(value = "/archiveTemplate", method = RequestMethod.POST)
    public ResponseEntity<PrimaryKey<Long>> archiveTemplate(@RequestBody Long id) throws UMUserManagementException {
        String response = messagingTransformerBean.archiveTemplate(id);
        if (response.equalsIgnoreCase(HkSystemConstantUtil.SUCCESS)) {
            return new ResponseEntity<>(null, ResponseCode.SUCCESS, "Template archived successfully", null);
        } else {
            return new ResponseEntity<>(null, ResponseCode.FAILURE, "Failed to archive Template", null);
        }
    }

    @Override
    public ResponseEntity<List<MessagingDataBean>> retrieveAll() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ResponseEntity<MessagingDataBean> retrieveById(@RequestBody PrimaryKey<Long> primaryKey) {
        MessagingDataBean messagingDataBean = null;
        try {
            messagingDataBean = messagingTransformerBean.retrieveById(primaryKey.getPrimaryKey());
            return new ResponseEntity<>(messagingDataBean, ResponseCode.SUCCESS, "", null);
        } catch (GenericDatabaseException ex) {
            return new ResponseEntity<>(messagingDataBean, ResponseCode.FAILURE, "", null);
        }

    }

    @Override
    public ResponseEntity<PrimaryKey<Long>> update(MessagingDataBean t) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ResponseEntity<PrimaryKey<Long>> create(MessagingDataBean t) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ResponseEntity<MessagingDataBean> deleteById(PrimaryKey<Long> primaryKey) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @RequestMapping(value = "/retrieve/messagesbytype", method = RequestMethod.POST, consumes = {"application/json"})
    public ResponseEntity<List<MessagingDataBean>> retrieveMessagesByType(@RequestBody String type) throws GenericDatabaseException {
        Long userId = loginDataBean.getId();
        Long companyId = loginDataBean.getCompanyId();
        List<MessagingDataBean> messagingDataBeans = messagingTransformerBean.retrieveAllMessagesByUserByType(userId, companyId, Boolean.FALSE, type);
        return new ResponseEntity<>(messagingDataBeans, ResponseCode.SUCCESS, "", null);
    }

    @RequestMapping(value = "/retrieve/alerts", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> retrieveAlerts() {
        Long userId = loginDataBean.getId();
        List<MessagingDataBean> retrieveAllMessagesByUser = messagingTransformerBean.retrieveAllMessagesByUser(userId, Boolean.FALSE, true);
        Map<String, Object> map = new HashMap<>();
        map.put("priority", retrieveAllMessagesByUser);
        map.put("count", messagingTransformerBean.retrieveunattendedMessageCount(userId, Boolean.FALSE, false));
        return new ResponseEntity<>(map, ResponseCode.SUCCESS, "", null);
    }

    @RequestMapping(value = "/retrieve/unreadmessages", method = RequestMethod.POST)
    public ResponseEntity<List<MessagingDataBean>> retrieveMessages() {
        Long userId = loginDataBean.getId();
        List<MessagingDataBean> retrieveAllMessagesByUser = messagingTransformerBean.retrieveAllMessagesByUser(userId, Boolean.FALSE, false);
        return new ResponseEntity<>(retrieveAllMessagesByUser, ResponseCode.SUCCESS, "", null);
    }

    @RequestMapping(value = "/mark/closed", method = RequestMethod.POST, consumes = {"application/json"})
    public ResponseEntity<PrimaryKey<Long>> markAsClosed(@RequestBody MessagingDataBean hkMessagingDataBean) {
        messagingTransformerBean.markAsClosed(hkMessagingDataBean);
        return new ResponseEntity<>(null, ResponseCode.SUCCESS, "", null);
    }

    @RequestMapping(value = "/searchByMessage", method = RequestMethod.GET, produces = {"application/json"})
    public List<MessagingDataBean> searchByMessageText(@RequestParam("q") String searchString) throws GenericDatabaseException {
        List<MessagingDataBean> messagingDataBeans = messagingTransformerBean.searchByMessage(searchString);
        return messagingDataBeans;
    }

    @Override
    public ResponseEntity<Map<String, Object>> retrievePrerequisite() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
