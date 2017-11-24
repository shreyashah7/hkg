/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.usermanagement.transformers;

import com.argusoft.generic.database.exception.GenericDatabaseException;
import com.argusoft.hkg.common.constantutil.HkSystemConstantUtil;
import com.argusoft.hkg.common.functionutil.HkSystemFunctionUtil;
import com.argusoft.hkg.core.HkEmployeeService;
import com.argusoft.hkg.core.HkHRService;
import com.argusoft.hkg.model.HkShiftEntity;
import com.argusoft.hkg.model.HkUserWorkHistoryEntity;
import com.argusoft.hkg.web.usermanagement.databeans.EmployeeDataBean;
import com.argusoft.hkg.web.center.usermanagement.databeans.LoginDataBean;
import com.argusoft.hkg.web.usermanagement.wrapper.UserManagementServiceWrapper;
import com.argusoft.hkg.web.util.SelectItem;
import com.argusoft.usermanagement.common.core.UMDepartmentService;
import com.argusoft.usermanagement.common.core.UMUserService;
import com.argusoft.usermanagement.common.model.UMDepartment;
import com.argusoft.usermanagement.common.model.UMUser;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;

/**
 *
 * @author shreya
 */
@Service
public class TransferEmployeeTransformer {

    @Autowired
    HkHRService hrService;

    @Autowired
    UMDepartmentService departmentService;

    @Autowired
    LoginDataBean hkLoginDataBean;

    @Autowired
    UserManagementServiceWrapper userManagementServiceWrapper;

    @Autowired
    EmployeeTransformerBean employeeTransformerBean;

    @Autowired
    UMUserService userService;

    @Autowired
    private HkEmployeeService employeeService;

    public Map<String, List<SelectItem>> retrieveShiftsWithDepartmentName() throws GenericDatabaseException {
        Map<Long, Set<HkShiftEntity>> tempDeptShiftMap = hrService.retrieveShiftByDeptIds(null, hkLoginDataBean.getCompanyId());
        Map<String, List<SelectItem>> deptShiftMap = new LinkedHashMap<>();
        List<UMDepartment> uMDepartments = departmentService.retrieveDepartmentList(hkLoginDataBean.getCompanyId(), Boolean.TRUE, Boolean.FALSE, Boolean.TRUE);
        if (!CollectionUtils.isEmpty(uMDepartments)) {
            if (!CollectionUtils.isEmpty(tempDeptShiftMap)) {
                for (UMDepartment uMDepartment : uMDepartments) {
                    if (tempDeptShiftMap.containsKey(uMDepartment.getId())) {
                        String key = uMDepartment.getId().toString() + "$@$" + uMDepartment.getDeptName();
                        SelectItem selectItem = new SelectItem(uMDepartment.getId(), uMDepartment.getDeptName());
                        Set<HkShiftEntity> hkShiftEntitys = tempDeptShiftMap.get(uMDepartment.getId());
                        List<SelectItem> shiftSelectItems = new LinkedList<>();
                        if (!CollectionUtils.isEmpty(hkShiftEntitys)) {
                            for (HkShiftEntity shiftEntity : hkShiftEntitys) {
                                SelectItem item = new SelectItem(uMDepartment.getDeptName(), uMDepartment.getId(), shiftEntity.getId(), shiftEntity.getShiftTitle());
                                shiftSelectItems.add(item);
                            }
                        }
                        deptShiftMap.put(key, shiftSelectItems);
                    } else {
                        String key = uMDepartment.getId().toString() + "$@$" + uMDepartment.getDeptName();
                        HkShiftEntity defaultShift = hrService.retrieveDefaultShift(hkLoginDataBean.getCompanyId());
                        List<SelectItem> shiftSelectItems = null;
                        if (defaultShift != null) {
                            shiftSelectItems = new LinkedList<>();
                            SelectItem item = new SelectItem(uMDepartment.getDeptName(), uMDepartment.getId(), defaultShift.getId(), defaultShift.getShiftTitle());
                            shiftSelectItems.add(item);
                        }
                        deptShiftMap.put(key, shiftSelectItems);
                    }
                }
            } else {
                for (UMDepartment uMDepartment : uMDepartments) {
                    String key = uMDepartment.getId().toString() + "$@$" + uMDepartment.getDeptName();
                    deptShiftMap.put(key, null);
                }
            }

        }
        return deptShiftMap;
    }

    public List<EmployeeDataBean> retrieveEmployeesByShiftByDept(List<Long> shiftIds, List<Long> deptIds) {
        List<EmployeeDataBean> employeeDataBeans = new ArrayList<>();
        if (!CollectionUtils.isEmpty(shiftIds) && !CollectionUtils.isEmpty(deptIds)) {
            List<UMUser> users = userManagementServiceWrapper.retrieveUsersByShiftByDepartment(hkLoginDataBean.getCompanyId(), shiftIds, deptIds, true, null);
            if (!CollectionUtils.isEmpty(users)) {
                for (UMUser user : users) {
                    EmployeeDataBean employeeDataBean = new EmployeeDataBean();
                    employeeDataBean = employeeTransformerBean.convertUMUserToEmployeeDataBean(employeeDataBean, user);
                    employeeDataBeans.add(employeeDataBean);
                }
            }
        }
        return employeeDataBeans;
    }

    public String transferEmployeeByCriteria(@RequestBody List<EmployeeDataBean> employeeDataBeans) {
        //System.out.println("employeeDataBeans :" + employeeDataBeans);
        String response = HkSystemConstantUtil.FAILURE;
        if (!CollectionUtils.isEmpty(employeeDataBeans)) {
            List<Long> userIds = new ArrayList<>();
            for (EmployeeDataBean employeeDataBean : employeeDataBeans) {
                userIds.add(employeeDataBean.getId());
            }
            List<UMUser> users = userService.retrieveUsersByUserList(userIds, null, null, null, true);
            if (!CollectionUtils.isEmpty(users)) {
                for (UMUser user : users) {
                    for (EmployeeDataBean employeeDataBean : employeeDataBeans) {
                        if (user.getId().equals(employeeDataBean.getId())) {
                            user.setStatus(employeeDataBean.getWorkstatus().toString());
                            user.setDepartment(employeeDataBean.getDepartmentId());
                            user.setCustom4(employeeDataBean.getWorkshift());
                            user.setCustom2(employeeDataBean.getReportsToId());
                            user.setExpiredOn(HkSystemFunctionUtil.convertToServerDate(employeeDataBean.getRelievingDate(), hkLoginDataBean.getClientRawOffsetInMin()));
                        }
                    }
                }
                userService.updateAllUsers(users);
                List<HkUserWorkHistoryEntity> workHistoryEntitys = new ArrayList<>();
                for (UMUser user : users) {

                    HkUserWorkHistoryEntity userWorkHistoryEntity = employeeService.retrieveShiftForUserFromUserWorkHistory(user.getId());
                    if (userWorkHistoryEntity != null) {
                        //get the existing work history entity
                        userWorkHistoryEntity.setEffectedTo(new Date());
                        userWorkHistoryEntity.setIsArchive(true);
                        workHistoryEntitys.add(userWorkHistoryEntity);
                    }
                    HkUserWorkHistoryEntity userHistory = new HkUserWorkHistoryEntity();
                    userHistory.setDepartment(user.getDepartment());
                    userHistory.setDesignation(userManagementServiceWrapper.retrieveDesignationStrForHistory(user));
                    userHistory.setEffectedFrm(new Date());
                    userHistory.setIsArchive(false);
                    userHistory.setReportsTo(user.getCustom2());
                    userHistory.setShift(user.getCustom4());
                    userHistory.setUserId(user.getId());
                    workHistoryEntitys.add(userHistory);
                }
                userManagementServiceWrapper.createOrUpdateWorkHistory(workHistoryEntitys);
                response = HkSystemConstantUtil.SUCCESS;
            }
        }
        return response;
    }

}
