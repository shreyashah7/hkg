/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.shift.transformers;

import com.argusoft.generic.database.common.GenericDao;
import com.argusoft.generic.database.exception.GenericDatabaseException;
import com.argusoft.hkg.common.constantutil.HkSystemConstantUtil;
import com.argusoft.hkg.common.functionutil.HkSystemFunctionUtil;
import com.argusoft.hkg.core.HkFieldService;
import com.argusoft.hkg.core.HkHRService;
import com.argusoft.hkg.model.HkEventEntity;
import com.argusoft.hkg.model.HkHolidayEntity;
import com.argusoft.hkg.model.HkShiftDepartmentEntity;
import com.argusoft.hkg.model.HkShiftDepartmentEntityPK;
import com.argusoft.hkg.model.HkShiftDtlEntity;
import com.argusoft.hkg.model.HkShiftEntity;
import com.argusoft.hkg.model.HkShiftRuleEntity;
import com.argusoft.hkg.model.HkShiftRuleEntityPK;
import com.argusoft.hkg.nosql.core.HkCustomFieldService;
import com.argusoft.hkg.nosql.model.CustomField;
import com.argusoft.hkg.web.customfield.transformers.CustomFieldTransformerBean;
import com.argusoft.hkg.web.eventmanagement.databeans.EventDataBean;
import com.argusoft.hkg.web.leavemanagement.databeans.HolidayDatabean;
import com.argusoft.hkg.web.shift.databeans.ShiftBreakDataBean;
import com.argusoft.hkg.web.shift.databeans.ShiftDataBean;
import com.argusoft.hkg.web.shift.databeans.TemporaryShiftDataBean;
import com.argusoft.hkg.web.usermanagement.databeans.DepartmentDataBean;
import com.argusoft.hkg.web.center.usermanagement.databeans.LoginDataBean;
import com.argusoft.hkg.web.usermanagement.transformers.DepartmentTransformerBean;
import com.argusoft.hkg.web.usermanagement.wrapper.UserManagementServiceWrapper;
import com.argusoft.hkg.web.util.TreeViewDataBean;
import com.argusoft.usermanagement.common.core.UMDepartmentService;
import com.argusoft.usermanagement.common.database.UMDepartmentDao;
import com.argusoft.usermanagement.common.model.UMDepartment;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 *
 * @author sidhdharth
 */
@Service
public class ShiftTransformerBean {

    @Autowired
    HkHRService hkHRService;

    @Autowired
    LoginDataBean hkLoginDataBean;

    @Autowired
    UserManagementServiceWrapper userManagementServiceWrapper;

    @Autowired
    DepartmentTransformerBean departmentTransformerBean;

    @Autowired
    HkCustomFieldService customFieldSevice;

    @Autowired
    HkFieldService fieldService;

    @Autowired
    UMDepartmentService departmentService;

    @Autowired
    CustomFieldTransformerBean customFieldTransformerBean;

    private static final Logger LOGGER = LoggerFactory.getLogger(ShiftTransformerBean.class);
    private static boolean notificationSent = false;
    private static boolean isFranchiseAdmin = false;

    public List<ShiftDataBean> retrieveShift() {
        Long franchise = hkLoginDataBean.getCompanyId();
        Long loggedInUserId = hkLoginDataBean.getId();
//        List<Long> retrieveUsersByRoleName = userManagementServiceWrapper.retrieveUsersByRoleName(HkSystemConstantUtil.ROLE.FRANCHISE_ADMIN, franchise);
//        if (!CollectionUtils.isEmpty(retrieveUsersByRoleName)) {
//            isFranchiseAdmin = false;
//            for (Long userId : retrieveUsersByRoleName) {
//                if (loggedInUserId.equals(userId)) {
//                    isFranchiseAdmin = true;
//                    break;
//                }
//            }
//        }
        List<HkShiftEntity> hkShiftEntitysFromDB = new ArrayList<>();
        if (franchise != null) {
            hkShiftEntitysFromDB = hkHRService.retrieveShifts(franchise);
        }
        List<Long> ids = new ArrayList<>();
        if (!CollectionUtils.isEmpty(hkShiftEntitysFromDB)) {
            for (HkShiftEntity hkShiftEntity : hkShiftEntitysFromDB) {
                if (!hkShiftEntity.getIsArchive()) {
                    ids.add(hkShiftEntity.getId());
                }
            }
        }
//        Map<Long, Map<HkSystemConstantUtil.SectionNameForCustomField, List<Map<Long, Map<String, Object>>>>> retrieveDocumentByInstanceId = customFieldSevice.retrieveDocumentByInstanceIds(ids, HkSystemConstantUtil.FeatureNameForCustomField.SHIFT, hkLoginDataBean.getCompanyId());

        List<ShiftDataBean> shiftDataBeans = new ArrayList<>();
//        Map<Long, List<TemporaryShiftDataBean>> tempShifts = new HashMap<>();
        Map<Long, ShiftDataBean> shifts = new HashMap<>();
        if (!CollectionUtils.isEmpty(hkShiftEntitysFromDB)) {
            for (HkShiftEntity hkShiftEntity : hkShiftEntitysFromDB) {
                ShiftDataBean shiftDataBean = new ShiftDataBean();
                shiftDataBean = this.convertHkShiftEntityToShiftDatabean(shiftDataBean, hkShiftEntity, false, null);
                shiftDataBeans.add(shiftDataBean);
            }
        }
        return shiftDataBeans;
    }

    private void convertShiftToTemporaryShiftDataBean(HkShiftEntity hkShiftEntity, TemporaryShiftDataBean temporaryShiftDataBean, Long parentShiftId) {
        Long companyId = hkLoginDataBean.getCompanyId();
        hkShiftEntity = hkHRService.retrieveShiftById(companyId, hkShiftEntity.getId());
        Set<HkShiftRuleEntity> hkShiftRuleEntitySet = hkShiftEntity.getHkShiftRuleEntitySet();
        temporaryShiftDataBean.setDefaultTempShift(hkShiftEntity.getIsDefault());
        temporaryShiftDataBean.setFranchiseAdmin(isFranchiseAdmin);
        if (hkShiftRuleEntitySet != null && !hkShiftRuleEntitySet.isEmpty()) {
            for (HkShiftRuleEntity hsre : hkShiftRuleEntitySet) {
                String ruleType = hsre.getHkShiftRuleEntityPK().getRuleType();
                if (ruleType.contains(HkSystemConstantUtil.ShiftRuleType.BEGINS)) {
                    temporaryShiftDataBean.setBeginRuleId(ruleType);
                    temporaryShiftDataBean.setSetRule(true);
                    temporaryShiftDataBean.setBeginsAction(hsre.getEventAction());
                    temporaryShiftDataBean.setBeginsDayCount(hsre.getDayCnt());
                    temporaryShiftDataBean.setBeginsEventOrHolidayId(hsre.getEventInstance().longValue());
                    temporaryShiftDataBean.setBeginsEventType(hsre.getEventType());
                    temporaryShiftDataBean.setTempShiftFromDate(HkSystemFunctionUtil.convertToClientDate(hkShiftEntity.getFrmDt(), hkLoginDataBean.getClientRawOffsetInMin()));
                    temporaryShiftDataBean.setTempShiftEndDate(HkSystemFunctionUtil.convertToClientDate(hkShiftEntity.getToDt(), hkLoginDataBean.getClientRawOffsetInMin()));
                } else if (ruleType.contains(HkSystemConstantUtil.ShiftRuleType.ENDS)) {
                    temporaryShiftDataBean.setEndRuleId(ruleType);
                    temporaryShiftDataBean.setSetRule(true);
                    temporaryShiftDataBean.setEndsAction(hsre.getEventAction());
                    temporaryShiftDataBean.setEndsDayCount(hsre.getDayCnt());
                    temporaryShiftDataBean.setEndsEventOrHolidayId(hsre.getEventInstance().longValue());
                    temporaryShiftDataBean.setEndsEventType(hsre.getEventType());
                    temporaryShiftDataBean.setTempShiftFromDate(HkSystemFunctionUtil.convertToClientDate(hkShiftEntity.getFrmDt(), hkLoginDataBean.getClientRawOffsetInMin()));
                    temporaryShiftDataBean.setTempShiftEndDate(HkSystemFunctionUtil.convertToClientDate(hkShiftEntity.getToDt(), hkLoginDataBean.getClientRawOffsetInMin()));
                } else {
                    temporaryShiftDataBean.setDateRangeRuleId(ruleType);
                    temporaryShiftDataBean.setDateRange(true);
                    temporaryShiftDataBean.setTempShiftFromDate(HkSystemFunctionUtil.convertToClientDate(hkShiftEntity.getFrmDt(), hkLoginDataBean.getClientRawOffsetInMin()));
                    temporaryShiftDataBean.setTempShiftEndDate(HkSystemFunctionUtil.convertToClientDate(hkShiftEntity.getToDt(), hkLoginDataBean.getClientRawOffsetInMin()));
                }
            }

            List<ShiftBreakDataBean> breakDataBeans = new ArrayList<>();

            Set<HkShiftDtlEntity> hkShiftDtlEntitySet = hkShiftEntity.getHkShiftDtlEntitySet();
            if (hkShiftDtlEntitySet != null && !hkShiftDtlEntitySet.isEmpty()) {
                for (HkShiftDtlEntity hsde : hkShiftDtlEntitySet) {
                    if (hsde.getSlotType().equals(HkSystemConstantUtil.SHIFT_TYPE.BREAK_TIME)) {
                        ShiftBreakDataBean breakDataBean = new ShiftBreakDataBean();

                        breakDataBean.setBreakEndTime(HkSystemFunctionUtil.convertToClientDate(hsde.getEndTime(), hkLoginDataBean.getClientRawOffsetInMin()));
                        breakDataBean.setBreakSlotTitle(hsde.getSlotTitle());
                        breakDataBean.setBreakStartTime(HkSystemFunctionUtil.convertToClientDate(hsde.getStrtTime(), hkLoginDataBean.getClientRawOffsetInMin()));
                        breakDataBean.setBreakId(hsde.getId());

                        breakDataBeans.add(breakDataBean);
                    } else if (hsde.getSlotType().equals(HkSystemConstantUtil.SHIFT_TYPE.MAIN_TIME)) {
                        temporaryShiftDataBean.setTempShiftStartTime(HkSystemFunctionUtil.convertToClientDate(hsde.getStrtTime(), hkLoginDataBean.getClientRawOffsetInMin()));
                        temporaryShiftDataBean.setTempShiftEndTime(HkSystemFunctionUtil.convertToClientDate(hsde.getEndTime(), hkLoginDataBean.getClientRawOffsetInMin()));
                    }
                }
            }
            temporaryShiftDataBean.setTempShiftBreakList(breakDataBeans);

            StringBuffer deptIds = new StringBuffer();
            Set<HkShiftDepartmentEntity> hkShiftDepartmentEntitySet = hkShiftEntity.getHkShiftDepartmentEntitySet();
            if (hkShiftDepartmentEntitySet != null && !hkShiftDepartmentEntitySet.isEmpty()) {
                Iterator<HkShiftDepartmentEntity> iterator = hkShiftDepartmentEntitySet.iterator();
                while (iterator.hasNext()) {
                    deptIds.append(iterator.next().getHkShiftDepartmentEntityPK().getDepartment());
                    if (iterator.hasNext()) {
                        deptIds.append(",");
                    }
                }
            }

            temporaryShiftDataBean.setDepartmentIdsForTempShift(deptIds.toString());
            temporaryShiftDataBean.setTempShiftStatus(true);
            temporaryShiftDataBean.setTempShiftId(hkShiftEntity.getId());
            temporaryShiftDataBean.setTempShiftName(hkShiftEntity.getShiftTitle());
            temporaryShiftDataBean.setTempShiftWorkingDayIds(hkShiftEntity.getWeekDays());
            temporaryShiftDataBean.setParentShiftId(parentShiftId);
        }
    }

    public List<TreeViewDataBean> retrieveShiftTree() {
        List<ShiftDataBean> shiftDataBeans = retrieveShift();
        List<TreeViewDataBean> shiftTree = this.convertShiftsToTree(shiftDataBeans);
        return shiftTree;
    }

    private List<TreeViewDataBean> convertShiftsToTree(List<ShiftDataBean> shiftDataBeans) {
        List<TreeViewDataBean> shiftTree = new ArrayList<>();
        if (shiftDataBeans != null && !shiftDataBeans.isEmpty()) {
            for (ShiftDataBean sdb : shiftDataBeans) {
                TreeViewDataBean dataBean = new TreeViewDataBean();
                dataBean.setDisplayName(sdb.getShiftName());
                dataBean.setId(sdb.getId());
                List<TemporaryShiftDataBean> temporaryShifts = sdb.getTemporaryShifts();
                List<ShiftDataBean> overRideShift = sdb.getOverRideShifts();
                List<TreeViewDataBean> children = new ArrayList<>();
                if (temporaryShifts != null && !temporaryShifts.isEmpty()) {
                    for (TemporaryShiftDataBean temporaryShiftDataBean : temporaryShifts) {
                        TreeViewDataBean treeViewDataBean = new TreeViewDataBean();
                        // setters
                        treeViewDataBean.setId(temporaryShiftDataBean.getTempShiftId());
                        treeViewDataBean.setDisplayName(temporaryShiftDataBean.getTempShiftName());
                        children.add(treeViewDataBean);
                    }
                    Collections.sort(children, NameComparator);
//                    dataBean.setChildren(children);
                }
                if (overRideShift != null && !overRideShift.isEmpty()) {
                    LOGGER.info("in tree");
                    for (ShiftDataBean ovrShiftDataBean : overRideShift) {
                        LOGGER.info("ovrShiftDataBean.getid" + ovrShiftDataBean.getId());
                        LOGGER.info("ovrShiftDataBean.getShiftName()" + ovrShiftDataBean.getShiftName());
                        TreeViewDataBean treeViewDataBean = new TreeViewDataBean();
                        // setters
                        treeViewDataBean.setId(ovrShiftDataBean.getId());
                        treeViewDataBean.setDisplayName(ovrShiftDataBean.getShiftName());
                        children.add(treeViewDataBean);
                    }
                    Collections.sort(children, NameComparator);
//                    dataBean.setChildren(children);
                }
                dataBean.setChildren(children);
                shiftTree.add(dataBean);
            }
            Collections.sort(shiftTree, NameComparator);
        }
        return shiftTree;
    }
    public static Comparator<TreeViewDataBean> NameComparator = new Comparator<TreeViewDataBean>() {

        @Override
        public int compare(TreeViewDataBean o1, TreeViewDataBean o2) {
            return o1.getDisplayName().compareToIgnoreCase(o2.getDisplayName());
        }
    };

    private ShiftDataBean convertHkShiftEntityToShiftDatabean(ShiftDataBean shiftDataBean, HkShiftEntity hkShiftEntity, boolean isDefaultShift, Map<HkSystemConstantUtil.SectionNameForCustomField, List<Map<Long, Map<String, Object>>>> retrieveDocumentByInstanceId) {
        if (shiftDataBean == null) {
            shiftDataBean = new ShiftDataBean();
        }
        shiftDataBean.setFranchiseAdmin(isFranchiseAdmin);
        shiftDataBean.setId(hkShiftEntity.getId());
        shiftDataBean.setShiftName(hkShiftEntity.getShiftTitle());
        shiftDataBean.setWorkingDayIds(hkShiftEntity.getWeekDays());
        shiftDataBean.setStatus(true);
        shiftDataBean.setDefaultShift(hkShiftEntity.getIsDefault());
        List<ShiftBreakDataBean> breakDataBeans = new ArrayList<>();
        Set<HkShiftDtlEntity> hkShiftDtlEntitySet = hkShiftEntity.getHkShiftDtlEntitySet();
        if (hkShiftDtlEntitySet != null && !hkShiftDtlEntitySet.isEmpty()) {
            for (HkShiftDtlEntity hsde : hkShiftDtlEntitySet) {
                if (hsde.getSlotType().equals(HkSystemConstantUtil.SHIFT_TYPE.BREAK_TIME)) {
                    ShiftBreakDataBean breakDataBean = new ShiftBreakDataBean();

                    breakDataBean.setBreakEndTime(HkSystemFunctionUtil.convertToClientDate(hsde.getEndTime(), hkLoginDataBean.getClientRawOffsetInMin()));
                    breakDataBean.setBreakSlotTitle(hsde.getSlotTitle());
                    breakDataBean.setBreakStartTime(HkSystemFunctionUtil.convertToClientDate(hsde.getStrtTime(), hkLoginDataBean.getClientRawOffsetInMin()));
                    breakDataBean.setBreakId(hsde.getId());

                    breakDataBeans.add(breakDataBean);
                } else if (hsde.getSlotType().equals(HkSystemConstantUtil.SHIFT_TYPE.MAIN_TIME)) {
                    shiftDataBean.setShiftStartTime(HkSystemFunctionUtil.convertToClientDate(hsde.getStrtTime(), hkLoginDataBean.getClientRawOffsetInMin()));
                    shiftDataBean.setShiftEndTime(HkSystemFunctionUtil.convertToClientDate(hsde.getEndTime(), hkLoginDataBean.getClientRawOffsetInMin()));
                }
            }
        }
        shiftDataBean.setBreakList(breakDataBeans);
        if (!isDefaultShift) {
            StringBuffer deptIds = new StringBuffer();
            Set<HkShiftDepartmentEntity> hkShiftDepartmentEntitySet = hkShiftEntity.getHkShiftDepartmentEntitySet();
            for (HkShiftDepartmentEntity hkShiftDepartmentEntity : hkShiftDepartmentEntitySet) {
                if (hkShiftDepartmentEntity.getIsArchive()) {
                    hkShiftDepartmentEntitySet.remove(hkShiftDepartmentEntity);
                }
            }
            if (hkShiftDepartmentEntitySet != null && !hkShiftDepartmentEntitySet.isEmpty()) {
                Iterator<HkShiftDepartmentEntity> iterator = hkShiftDepartmentEntitySet.iterator();
                while (iterator.hasNext()) {
                    deptIds.append(iterator.next().getHkShiftDepartmentEntityPK().getDepartment());
                    if (iterator.hasNext()) {
                        deptIds.append(",");
                    }
                }
            }
            shiftDataBean.setDepartmentIds(deptIds.toString());

            Set<HkShiftEntity> subShifts = hkShiftEntity.getHkShiftEntitySet();
            if (subShifts != null && !subShifts.isEmpty()) {
                Long parentShiftId = hkShiftEntity.getId();
                List<TemporaryShiftDataBean> temporaryShiftDataBeans = new ArrayList<>();
                for (HkShiftEntity hse : subShifts) {
                    if (hse.getStatus().equalsIgnoreCase(HkSystemConstantUtil.ACTIVE)) {
                        TemporaryShiftDataBean temporaryShiftDataBean = new TemporaryShiftDataBean();
                        this.convertShiftToTemporaryShiftDataBean(hse, temporaryShiftDataBean, parentShiftId);
                        temporaryShiftDataBeans.add(temporaryShiftDataBean);
                    }
                    shiftDataBean.setTemporaryShifts(temporaryShiftDataBeans);
                }
            }
            Set<HkShiftEntity> overrideShifts = hkShiftEntity.getHkOverrideShiftEntitySet();
            if (overrideShifts != null && !overrideShifts.isEmpty()) {
//                LOGGER.info("overrideShifts are not empty");
                Long parentShiftId = hkShiftEntity.getId();
                List<ShiftDataBean> shiftDataBeans = new ArrayList<>();
                for (HkShiftEntity hse : overrideShifts) {
                    if (hse.getStatus().equalsIgnoreCase(HkSystemConstantUtil.ACTIVE)) {
                        LOGGER.info("here" + hse.getShiftTitle());
                        ShiftDataBean dataBean = new ShiftDataBean();
                        if (hse.getOverrideShiftFor() == null) {
                            this.convertHkShiftEntityToShiftDataBean(dataBean, hse, true);
                        } else {
                            this.convertHkShiftEntityToShiftDataBean(dataBean, hse, true);
                        }
                        shiftDataBeans.add(dataBean);
                    }
                    shiftDataBean.setOverRideShifts(shiftDataBeans);
                }
            } else {
//                LOGGER.info("overrideShifts ARE empty");
            }
        }

        if (retrieveDocumentByInstanceId != null) {
            List<Map<Long, Map<String, Object>>> maps = retrieveDocumentByInstanceId.get(HkSystemConstantUtil.SectionNameForCustomField.GENERAL);
            if (maps != null) {
                for (Map<Long, Map<String, Object>> map : maps) {
                    shiftDataBean.setShiftCustom(map.get(hkShiftEntity.getId()));
                }
            }
        }
        return shiftDataBean;
    }

    public Long createShift(ShiftDataBean shiftDataBean) {
        boolean defaultShift = shiftDataBean.isDefaultShift();
        Long companyId = hkLoginDataBean.getCompanyId();
        HkShiftEntity hkShiftEntityFromDb = null;
        LOGGER.info("shiftDataBean.isOverrideShift()" + shiftDataBean.isOverrideShift());
        if (shiftDataBean.isOverrideShift()) {
            hkShiftEntityFromDb = hkHRService.retrieveShiftById(companyId, shiftDataBean.getId());
            LOGGER.info("hkShiftEntityFromDb" + hkShiftEntityFromDb);
        }
        HkShiftEntity hkShiftEntity = this.convertShiftDataBeanToHkShiftEntity(shiftDataBean, new HkShiftEntity(), defaultShift, true, hkShiftEntityFromDb);
        hkShiftEntity.setFranchise(companyId);
        userManagementServiceWrapper.createLocaleForEntity(shiftDataBean.getShiftName(), "Shift", hkLoginDataBean.getId(), hkLoginDataBean.getCompanyId());
        Set<Long> searchUsersByFeatureName = userManagementServiceWrapper.searchUsersByFeatureName(Arrays.asList(HkSystemConstantUtil.Feature.MANAGE_SHIFT), companyId);
        List<Long> userList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(searchUsersByFeatureName)) {
            userList.addAll(searchUsersByFeatureName);
        }
        if (defaultShift) {
//            List<Long> retrieveAllUsersByFranchise = userManagementServiceWrapper.retrieveAllUsersByFranchise(companyId);
            hkHRService.createDefaultShift(hkShiftEntity, userList);
            createOrUpdateCustomField(hkShiftEntity.getId(), shiftDataBean);
            return hkShiftEntity.getId();
        } else {
            hkShiftEntity.setIsDefault(false);
            String departmentIds = shiftDataBean.getDepartmentIds();
            List<String> parseString = parseString(departmentIds);
            List<Long> notify = new ArrayList<>();
            Set<Long> retrieveRecipientIds = userManagementServiceWrapper.retrieveRecipientIds(parseString);
            if (!CollectionUtils.isEmpty(retrieveRecipientIds)) {
                notify.addAll(retrieveRecipientIds);
            }

            if (!CollectionUtils.isEmpty(searchUsersByFeatureName)) {
                Iterator<Long> itr = searchUsersByFeatureName.iterator();
                while (itr.hasNext()) {
                    Long userId = itr.next();
                    if (!CollectionUtils.isEmpty(notify) && notify.contains(userId)) {
                        itr.remove();
                    }
                }
                notify.addAll(searchUsersByFeatureName);
            }
            Long[] deptIds = null;
            String deptId = departmentIds;
            if (deptId != null && !deptId.equals("")) {
                String[] splits = deptId.split(",");
                deptIds = new Long[splits.length];
                int i = 0;
                for (String s : splits) {
                    deptIds[i++] = Long.parseLong(s);
                }
            }
            List<UMDepartment> retrieveDepartments = userManagementServiceWrapper.retrieveDepartments(companyId, Boolean.TRUE);
            String departmentNames = "";
            if (!CollectionUtils.isEmpty(retrieveDepartments)) {
                if (deptIds != null) {
                    for (Long id : deptIds) {
                        for (UMDepartment uMDepartment : retrieveDepartments) {
                            if (id.equals(uMDepartment.getId())) {
                                String deptName = uMDepartment.getDeptName();
                                departmentNames += deptName + ",";
                            }
                        }
                    }
                    if (StringUtils.hasText(departmentNames)) {
                        departmentNames = departmentNames.substring(0, departmentNames.length() - 1);
                    }
                }
            }
            hkHRService.createShift(hkShiftEntity, departmentNames, notify, false, null);
            createOrUpdateCustomField(hkShiftEntity.getId(), shiftDataBean);
            return hkShiftEntity.getId();
        }
    }

    public Long createTemporaryShift(TemporaryShiftDataBean temporaryShiftDataBean) {
        boolean tempShiftAvailable = false;
        Long companyId = hkLoginDataBean.getCompanyId();
        HkShiftEntity hkShiftEntity = new HkShiftEntity();
        long parentShiftId = temporaryShiftDataBean.getParentShiftId();
//        LOGGER.info("parent" + parentShiftId);
        this.convertTemporaryShiftDatabeanToModel(temporaryShiftDataBean, hkShiftEntity);
        HkShiftEntity retrieveShiftById = hkHRService.retrieveShiftById(companyId, parentShiftId, true, false, true, false);
        Set<HkShiftEntity> hkTempShiftEntitySet = retrieveShiftById.getHkShiftEntitySet();
        if (!CollectionUtils.isEmpty(hkTempShiftEntitySet)) {
            for (HkShiftEntity hkShiftEntity1 : hkTempShiftEntitySet) {
                if (hkShiftEntity1.getStatus().equalsIgnoreCase(HkSystemConstantUtil.ACTIVE)) {
                    tempShiftAvailable = true;
                }
            }
        }
        if (!tempShiftAvailable) {
            boolean isDefaultShift = retrieveShiftById.getIsDefault();
            List<Long> notify = new ArrayList<>();
            List<Long> retrieveRecipientIds = userManagementServiceWrapper.searchUsersByShift(parentShiftId, companyId);
            Set<Long> searchUsersByFeatureName = userManagementServiceWrapper.searchUsersByFeatureName(Arrays.asList(HkSystemConstantUtil.Feature.MANAGE_SHIFT), companyId);
            if (!CollectionUtils.isEmpty(retrieveRecipientIds)) {
                notify.addAll(retrieveRecipientIds);
            }
            if (!CollectionUtils.isEmpty(searchUsersByFeatureName)) {
                Iterator<Long> itr = searchUsersByFeatureName.iterator();
                while (itr.hasNext()) {
                    Long userId = itr.next();
                    if (!CollectionUtils.isEmpty(notify) && notify.contains(userId)) {
                        itr.remove();
                    }
                }
                notify.addAll(searchUsersByFeatureName);
            }
            if (!isDefaultShift) {
                Set< HkShiftDepartmentEntity> hkShiftDepartmentEntitySet = retrieveShiftById.getHkShiftDepartmentEntitySet();
                String departmentIds = "";
                List<Long> deptIds = new ArrayList<>();
                if (!CollectionUtils.isEmpty(hkShiftDepartmentEntitySet)) {
                    for (HkShiftDepartmentEntity hkShiftDepartmentEntity : hkShiftDepartmentEntitySet) {
                        long id = hkShiftDepartmentEntity.getHkShiftDepartmentEntityPK().getDepartment();
                        departmentIds += id + ",";
                        deptIds.add(id);
                    }
                    if (departmentIds != null && !StringUtils.isEmpty(departmentIds)) {
                        departmentIds = departmentIds.substring(0, departmentIds.length() - 1);
                    }
                }
                List<UMDepartment> retrieveDepartments = userManagementServiceWrapper.retrieveDepartments(companyId, Boolean.TRUE);
                String departmentNames = "";
                if (!CollectionUtils.isEmpty(retrieveDepartments)) {
                    if (deptIds != null) {
                        for (Long id : deptIds) {
                            for (UMDepartment uMDepartment : retrieveDepartments) {
                                if (id.equals(uMDepartment.getId())) {
                                    String deptName = uMDepartment.getDeptName();
                                    departmentNames += deptName + ",";
                                }
                            }
                        }
                        if (departmentNames != null && !StringUtils.isEmpty(departmentNames)) {
                            departmentNames = departmentNames.substring(0, departmentNames.length() - 1);
                        }
                    }
                }

                hkShiftEntity.setIsDefault(false);
                hkHRService.createShift(hkShiftEntity, departmentNames, notify, true, parentShiftId);
            } else {
                hkShiftEntity.setIsDefault(true);
                hkHRService.createShift(hkShiftEntity, null, notify, true, parentShiftId);
            }
            userManagementServiceWrapper.createLocaleForEntity(temporaryShiftDataBean.getTempShiftName(), "Shift", hkLoginDataBean.getId(), hkLoginDataBean.getCompanyId());
            return hkShiftEntity.getId();
        }
        return hkShiftEntity.getId();
    }

    private void convertTemporaryShiftDatabeanToModel(TemporaryShiftDataBean temporaryShiftDataBean, HkShiftEntity hkShiftEntity) {
        Long franchise = hkLoginDataBean.getCompanyId();

        hkShiftEntity.setCreatedBy(hkLoginDataBean.getId());
        hkShiftEntity.setCreatedOn(new Date());
        hkShiftEntity.setFranchise(franchise);
        hkShiftEntity.setFrmDt(HkSystemFunctionUtil.convertToServerDate(temporaryShiftDataBean.getTempShiftFromDate(), hkLoginDataBean.getClientRawOffsetInMin()));
        hkShiftEntity.setHasRule(temporaryShiftDataBean.isSetRule());
        hkShiftEntity.setIsArchive(false);
        hkShiftEntity.setLastModifiedBy(hkLoginDataBean.getId());
        hkShiftEntity.setLastModifiedOn(new Date());
        hkShiftEntity.setShiftTitle(temporaryShiftDataBean.getTempShiftName());
        hkShiftEntity.setToDt(HkSystemFunctionUtil.convertToServerDate(temporaryShiftDataBean.getTempShiftEndDate(), hkLoginDataBean.getClientRawOffsetInMin()));
        hkShiftEntity.setWeekDays(temporaryShiftDataBean.getTempShiftWorkingDayIds());
        hkShiftEntity.setStatus(HkSystemConstantUtil.ACTIVE);
        List<HkShiftEntity> hkShiftEntitysFromDB = hkHRService.retrieveShifts(franchise);
        HkShiftEntity parentShift = null;
        if (hkShiftEntitysFromDB != null && !hkShiftEntitysFromDB.isEmpty()) {
            for (HkShiftEntity entity : hkShiftEntitysFromDB) {
                if (entity.getId().equals(temporaryShiftDataBean.getParentShiftId())) {
                    parentShift = entity;
                }
            }
        }

        hkShiftEntity.setTemporaryShiftFor(parentShift);
        Set<HkShiftDtlEntity> hkShiftDtlEntitys = new HashSet<>();
        HkShiftDtlEntity hkShiftDtlEntity = new HkShiftDtlEntity();
        hkShiftDtlEntity.setCreatedBy(hkLoginDataBean.getId());
        hkShiftDtlEntity.setCreatedOn(new Date());
        hkShiftDtlEntity.setStrtTime(HkSystemFunctionUtil.convertToServerDate(temporaryShiftDataBean.getTempShiftStartTime(), hkLoginDataBean.getClientRawOffsetInMin()));
        hkShiftDtlEntity.setEndTime(HkSystemFunctionUtil.convertToServerDate(temporaryShiftDataBean.getTempShiftEndTime(), hkLoginDataBean.getClientRawOffsetInMin()));
        hkShiftDtlEntity.setIsArchive(false);
        hkShiftDtlEntity.setShift(hkShiftEntity);
        hkShiftDtlEntity.setSlotType(HkSystemConstantUtil.SHIFT_TYPE.MAIN_TIME);
        hkShiftDtlEntity.setWeekDays(temporaryShiftDataBean.getTempShiftWorkingDayIds());
        hkShiftDtlEntitys.add(hkShiftDtlEntity);
        List<ShiftBreakDataBean> breakList = temporaryShiftDataBean.getTempShiftBreakList();
        if (!CollectionUtils.isEmpty(breakList)) {
            HkShiftDtlEntity shiftDtlEntity = null;
            for (ShiftBreakDataBean shiftBreakDataBean : breakList) {
                shiftDtlEntity = new HkShiftDtlEntity();
                shiftDtlEntity.setCreatedBy(hkLoginDataBean.getCompanyId());
                shiftDtlEntity.setCreatedOn(new Date());
                shiftDtlEntity.setStrtTime(HkSystemFunctionUtil.convertToServerDate(shiftBreakDataBean.getBreakStartTime(), hkLoginDataBean.getClientRawOffsetInMin()));
                shiftDtlEntity.setEndTime(HkSystemFunctionUtil.convertToServerDate(shiftBreakDataBean.getBreakEndTime(), hkLoginDataBean.getClientRawOffsetInMin()));
                shiftDtlEntity.setIsArchive(false);
                shiftDtlEntity.setShift(hkShiftEntity);
                shiftDtlEntity.setSlotTitle(shiftBreakDataBean.getBreakSlotTitle());
                shiftDtlEntity.setSlotType(HkSystemConstantUtil.SHIFT_TYPE.BREAK_TIME);
                shiftDtlEntity.setEffectedFrm(HkSystemFunctionUtil.convertToServerDate(temporaryShiftDataBean.getTempShiftFromDate(), hkLoginDataBean.getClientRawOffsetInMin()));
                shiftDtlEntity.setEffectedEnd(HkSystemFunctionUtil.convertToServerDate(temporaryShiftDataBean.getTempShiftEndDate(), hkLoginDataBean.getClientRawOffsetInMin()));
                shiftDtlEntity.setWeekDays(temporaryShiftDataBean.getTempShiftWorkingDayIds());
                hkShiftDtlEntitys.add(shiftDtlEntity);
            }
        }
        hkShiftEntity.setHkShiftDtlEntitySet(hkShiftDtlEntitys);

        Set<HkShiftRuleEntity> hkShiftRuleEntitys = new HashSet<>();

        if (temporaryShiftDataBean.isSetRule()) {
            // 2 entry
            HkShiftRuleEntity hsreBegin = new HkShiftRuleEntity();
            hsreBegin.setDayCnt(temporaryShiftDataBean.getBeginsDayCount());
            hsreBegin.setEventAction(temporaryShiftDataBean.getBeginsAction().charAt(0) + "");
            hsreBegin.setEventInstance(BigInteger.valueOf(temporaryShiftDataBean.getBeginsEventOrHolidayId().longValue()));
            hsreBegin.setEventType(temporaryShiftDataBean.getBeginsEventType().charAt(0) + "");
            hsreBegin.setHkShiftEntity(hkShiftEntity);
            hsreBegin.setIsArchive(false);
            HkShiftRuleEntityPK beginRuleEntityPK = new HkShiftRuleEntityPK();
            beginRuleEntityPK.setRuleType(HkSystemConstantUtil.ShiftRuleType.BEGINS);
            hsreBegin.setHkShiftRuleEntityPK(beginRuleEntityPK);

            HkShiftRuleEntity hsreEnd = new HkShiftRuleEntity();
            hsreEnd.setDayCnt(temporaryShiftDataBean.getEndsDayCount());
            hsreEnd.setEventAction(temporaryShiftDataBean.getEndsAction().charAt(0) + "");
            hsreEnd.setIsArchive(false);
            hsreEnd.setEventInstance(BigInteger.valueOf(temporaryShiftDataBean.getEndsEventOrHolidayId().longValue()));
            hsreEnd.setEventType(temporaryShiftDataBean.getEndsEventType().charAt(0) + "");
            hsreEnd.setHkShiftEntity(hkShiftEntity);
            HkShiftRuleEntityPK endRuleEntityPK = new HkShiftRuleEntityPK();
            endRuleEntityPK.setRuleType(HkSystemConstantUtil.ShiftRuleType.ENDS);
            hsreEnd.setHkShiftRuleEntityPK(endRuleEntityPK);

            hkShiftRuleEntitys.add(hsreBegin);
            hkShiftRuleEntitys.add(hsreEnd);
        } else {
            // 1 entry
            HkShiftRuleEntity hkShiftRuleEntity = new HkShiftRuleEntity();
            hkShiftRuleEntity.setIsArchive(false);
            hkShiftRuleEntity.setHkShiftEntity(hkShiftEntity);
            HkShiftRuleEntityPK hsrepk = new HkShiftRuleEntityPK();
            hsrepk.setRuleType(HkSystemConstantUtil.ShiftRuleType.DATE_RANGE);
            hkShiftRuleEntity.setHkShiftRuleEntityPK(hsrepk);
            hkShiftRuleEntitys.add(hkShiftRuleEntity);
        }
        hkShiftEntity.setHkShiftRuleEntitySet(hkShiftRuleEntitys);

    }

    private HkShiftEntity convertShiftDataBeanToHkShiftEntity(ShiftDataBean shiftDataBean, HkShiftEntity hkShiftEntity, boolean defaultShift, boolean createShift, HkShiftEntity hkShiftEntityFromDb) {
        Long franchise = hkLoginDataBean.getCompanyId();
        Long userId = hkLoginDataBean.getId();
        Set<HkShiftDtlEntity> hkShiftDtlEntitys = new HashSet<>();

        hkShiftEntity.setCreatedBy(userId);
        hkShiftEntity.setCreatedOn(new Date());
        hkShiftEntity.setIsArchive(false);
        hkShiftEntity.setLastModifiedBy(userId);
        hkShiftEntity.setLastModifiedOn(new Date());
        hkShiftEntity.setFrmDt(HkSystemFunctionUtil.convertToServerDate(new Date(), hkLoginDataBean.getClientRawOffsetInMin()));
        hkShiftEntity.setShiftTitle(shiftDataBean.getShiftName());
        hkShiftEntity.setStatus(HkSystemConstantUtil.ACTIVE);
        hkShiftEntity.setOverrideShiftFor(hkShiftEntityFromDb);
        HkShiftDtlEntity hkShiftDtlEntity = new HkShiftDtlEntity();
        hkShiftDtlEntity.setIsArchive(false);
        hkShiftEntity.setWeekDays(shiftDataBean.getWorkingDayIds());
        hkShiftDtlEntity.setCreatedBy(userId);
        hkShiftDtlEntity.setCreatedOn(new Date());
        hkShiftDtlEntity.setStrtTime(HkSystemFunctionUtil.convertToServerDate(shiftDataBean.getShiftStartTime(), hkLoginDataBean.getClientRawOffsetInMin()));
        hkShiftDtlEntity.setEndTime(HkSystemFunctionUtil.convertToServerDate(shiftDataBean.getShiftEndTime(), hkLoginDataBean.getClientRawOffsetInMin()));
        hkShiftDtlEntity.setShift(hkShiftEntity);
        hkShiftDtlEntity.setSlotType(HkSystemConstantUtil.SHIFT_TYPE.MAIN_TIME);
        hkShiftDtlEntity.setWeekDays(shiftDataBean.getWorkingDayIds());
        hkShiftDtlEntitys.add(hkShiftDtlEntity);
        List<ShiftBreakDataBean> breakList = shiftDataBean.getBreakList();
        if (!CollectionUtils.isEmpty(breakList)) {
            HkShiftDtlEntity shiftDtlEntity = null;
            for (ShiftBreakDataBean shiftBreakDataBean : breakList) {
                shiftDtlEntity = new HkShiftDtlEntity();
                shiftDtlEntity.setCreatedBy(userId);
                shiftDtlEntity.setCreatedOn(new Date());
                shiftDtlEntity.setStrtTime(HkSystemFunctionUtil.convertToServerDate(shiftBreakDataBean.getBreakStartTime(), hkLoginDataBean.getClientRawOffsetInMin()));
                shiftDtlEntity.setEndTime(HkSystemFunctionUtil.convertToServerDate(shiftBreakDataBean.getBreakEndTime(), hkLoginDataBean.getClientRawOffsetInMin()));
                shiftDtlEntity.setIsArchive(false);
                shiftDtlEntity.setWeekDays(shiftDataBean.getWorkingDayIds());
                shiftDtlEntity.setShift(hkShiftEntity);
                shiftDtlEntity.setSlotTitle(shiftBreakDataBean.getBreakSlotTitle());
                shiftDtlEntity.setSlotType(HkSystemConstantUtil.SHIFT_TYPE.BREAK_TIME);
                shiftDtlEntity.setEffectedFrm(new Date());
                hkShiftDtlEntitys.add(shiftDtlEntity);
            }
        }
        hkShiftEntity.setHkShiftDtlEntitySet(hkShiftDtlEntitys);
        if (!defaultShift) {

            Set<HkShiftDepartmentEntity> hkShiftDepartmentEntitys = new HashSet<>();
            hkShiftEntity.setHkShiftDepartmentEntitySet(hkShiftDepartmentEntitys);
            String departmentIds = shiftDataBean.getDepartmentIds();
            if (departmentIds != null && !departmentIds.equals("")) {
                String[] splits = departmentIds.split(",");
                Long[] deptIds = new Long[splits.length];
                int i = 0;
                for (String s : splits) {
                    deptIds[i++] = Long.parseLong(s);
                }
                for (Long deptId : deptIds) {
                    HkShiftDepartmentEntityPK hsdepk = new HkShiftDepartmentEntityPK();
                    hsdepk.setDepartment(deptId);
                    HkShiftDepartmentEntity hkShiftDepartmentEntity = new HkShiftDepartmentEntity(hsdepk);
                    hkShiftDepartmentEntity.setLastModifiedOn(new Date());
                    hkShiftDepartmentEntity.setIsArchive(false);
                    hkShiftDepartmentEntity.setHkShiftEntity(hkShiftEntity);
                    hkShiftDepartmentEntitys.add(hkShiftDepartmentEntity);
//                    LOGGER.info("---dept created" + hkShiftDepartmentEntitys);
                }

            }
        }

        return hkShiftEntity;
    }

    public List<TreeViewDataBean> retrieveDepartmentsInMultiSelectTreeView(long companyId) {
        List<DepartmentDataBean> retrieveDepartmentListInTreeView = departmentTransformerBean.retrieveDepartmentListInTreeView(companyId);
        List<TreeViewDataBean> treeViewDataBeans = new ArrayList<>();
        if (!CollectionUtils.isEmpty(retrieveDepartmentListInTreeView)) {
            for (DepartmentDataBean departmentDataBean : retrieveDepartmentListInTreeView) {
                TreeViewDataBean treeViewDataBean = new TreeViewDataBean();
                this.convertInTreeBean(departmentDataBean, treeViewDataBean);
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
                this.convertInTreeBean(departmentDataBean1, dataBean);
                child.add(dataBean);
            }
            treeViewDataBean.setItems(child);
        }
    }

    public List<HolidayDatabean> retieveAllHoliday(Long franchiesId, boolean isActive) {
        List<HkHolidayEntity> hkHolidayEntityList = hkHRService.retrieveHolidaysByCriteria(null, null, null, true, franchiesId, Boolean.FALSE);
        Iterator<HkHolidayEntity> iter = hkHolidayEntityList.iterator();
        Calendar beforDate = Calendar.getInstance();
//        Calendar cal = Calendar.getInstance();
        beforDate.set(Calendar.HOUR_OF_DAY, 0);
        beforDate.set(Calendar.MINUTE, 0);
        beforDate.set(Calendar.SECOND, 0);
        beforDate.set(Calendar.MILLISECOND, 0);

        while (iter.hasNext()) {
            HkHolidayEntity holidayEntity = iter.next();
            Date startDate = holidayEntity.getStartDt();
            Date beforeDate = beforDate.getTime();
            if ((((Long) startDate.getTime()).compareTo((Long) beforeDate.getTime()) < 0)) {
                iter.remove();
            }
        }
        List<HolidayDatabean> holidayDatabeansList = new ArrayList<>();
        if (hkHolidayEntityList != null && !hkHolidayEntityList.isEmpty()) {
            for (HkHolidayEntity hkHolidayEntity : hkHolidayEntityList) {
                holidayDatabeansList.add(this.convertHolidayModelToDatabean(hkHolidayEntity));
            }
            Collections.sort(holidayDatabeansList, new Comparator() {
                @Override
                public int compare(Object o1, Object o2) {
                    HolidayDatabean holidayDb = (HolidayDatabean) o1;
                    HolidayDatabean holidayDbTemp = (HolidayDatabean) o2;
                    return holidayDb.getStartDt().compareTo(holidayDbTemp.getStartDt());
                }
            });
        }
        return holidayDatabeansList;
    }

    /**
     * This method converts holiday model to databean.
     *
     * @param hkHolidayEntity
     * @return HolidayDatabean
     */
    private HolidayDatabean convertHolidayModelToDatabean(HkHolidayEntity hkHolidayEntity) {
        HolidayDatabean holidayDatabean = new HolidayDatabean();
        Date currentDate = new Date();
        if (hkHolidayEntity != null) {
            if ((hkHolidayEntity.getEndDt().compareTo(currentDate)) < 0) {
                holidayDatabean.setEditFlage(Boolean.TRUE);
            } else {
                holidayDatabean.setEditFlage(Boolean.FALSE);
            }
            Date startDt = hkHolidayEntity.getStartDt();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yy");
            String startYear = dateFormat.format(startDt);
            holidayDatabean.setId(hkHolidayEntity.getId());
            holidayDatabean.setTitle(hkHolidayEntity.getHolidayTitle().concat("'").concat(startYear));
            holidayDatabean.setStartDt(hkHolidayEntity.getStartDt());
            holidayDatabean.setEndDt(hkHolidayEntity.getEndDt());
        }
        return holidayDatabean;
    }

    public List<EventDataBean> retrieveUpcomingEvents() {
        List<HkEventEntity> eventEntityList = hkHRService.retrieveUpcomingEvents(null, null, null, hkLoginDataBean.getCompanyId(), false);
        return this.convertEventEntityListToEventDataBeanList(eventEntityList);
    }

    public List<EventDataBean> convertEventEntityListToEventDataBeanList(List<HkEventEntity> eventEntityList) {
        List<EventDataBean> eventDataBeanList = new ArrayList<>();
        if (eventEntityList != null && !eventEntityList.isEmpty()) {
            for (HkEventEntity hkEventEntity : eventEntityList) {
                EventDataBean eventDataBean = new EventDataBean();
                eventDataBean = this.convertEventEntityToEventDataBean(hkEventEntity, eventDataBean);

                eventDataBeanList.add(eventDataBean);
            }
        }
        return eventDataBeanList;
    }

    public EventDataBean convertEventEntityToEventDataBean(HkEventEntity eventEntity, EventDataBean eventDataBean) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yy");
        Date frmDt = eventEntity.getFrmDt();
        String fromYear = dateFormat.format(frmDt);
        eventDataBean.setId(eventEntity.getId());
        eventDataBean.setEventTitle(eventEntity.getEventTitle().concat("'".concat(fromYear)));
        eventDataBean.setFromDate(frmDt);
        eventDataBean.setToDate(eventEntity.getToDt());
        return eventDataBean;
    }

    public ShiftDataBean retrieveDefaultShift(Long companyId) {
        HkShiftEntity retrieveDefaultShift = hkHRService.retrieveDefaultShift(companyId);
        ShiftDataBean shiftDataBean = null;
        if (retrieveDefaultShift != null) {
            Map<Long, ShiftDataBean> shifts = new HashMap<>();
            Map<HkSystemConstantUtil.SectionNameForCustomField, List<Map<Long, Map<String, Object>>>> get = customFieldTransformerBean.retrieveDocumentByInstanceId(retrieveDefaultShift.getId(), HkSystemConstantUtil.FeatureNameForCustomField.SHIFT, companyId);
            shiftDataBean = new ShiftDataBean();
            shiftDataBean = this.convertHkShiftEntityToShiftDatabean(new ShiftDataBean(), retrieveDefaultShift, true, get);
            shifts.put(shiftDataBean.getId(), shiftDataBean);
        }

        return shiftDataBean;
    }

    public String updateShift(ShiftDataBean shiftDataBean, Long companyId) {
        boolean status = shiftDataBean.isStatus();
        List<Long> notify = new ArrayList<>();
        Long shiftId = shiftDataBean.getId();
        String msg = "";
        if (!status && !shiftDataBean.isDefaultShift()) {
            List<Long> searchUsersByShift = userManagementServiceWrapper.searchUsersByShift(shiftId, companyId);
            if (CollectionUtils.isEmpty(searchUsersByShift)) {
//                notify.addAll(searchUsersByShift);
                Set<Long> searchUsersByFeatureName = userManagementServiceWrapper.searchUsersByFeatureName(Arrays.asList(HkSystemConstantUtil.Feature.MANAGE_SHIFT), companyId);
                if (!CollectionUtils.isEmpty(searchUsersByFeatureName)) {
                    notify.addAll(searchUsersByFeatureName);
                }
                hkHRService.removeShift(shiftId, hkLoginDataBean.getId(), notify);
                msg = "Shift removed.";
                userManagementServiceWrapper.deleteLocaleForEntity(shiftDataBean.getShiftName(), "SHIFT", "CONTENT", hkLoginDataBean.getId(), companyId);
                return msg;
            }
            return null;

        } else {
            HkShiftEntity hkShiftEntityFromDb = null;
            boolean defaultShift = shiftDataBean.isDefaultShift();
            if (defaultShift) {
                hkShiftEntityFromDb = hkHRService.retrieveDefaultShift(companyId);
            } else {
                hkShiftEntityFromDb = hkHRService.retrieveShiftById(companyId, shiftId);
            }
            HkShiftEntity hkShiftEntity = this.convertShiftDataBeanToHkShiftEntityForUpdate(shiftDataBean, hkShiftEntityFromDb, defaultShift, false);
            hkShiftEntity.setIsDefault(defaultShift);

            if (notificationSent) {
                if (!hkShiftEntity.getIsDefault()) {
                    List<Long> searchUsersByShift = userManagementServiceWrapper.searchUsersByShift(shiftId, companyId);
                    if (!CollectionUtils.isEmpty(searchUsersByShift)) {
                        notify.addAll(searchUsersByShift);
                    }
                    Set<Long> searchUsersByFeatureName = userManagementServiceWrapper.searchUsersByFeatureName(Arrays.asList(HkSystemConstantUtil.Feature.MANAGE_SHIFT), companyId);
                    if (!CollectionUtils.isEmpty(searchUsersByFeatureName)) {
                        Iterator<Long> itr = searchUsersByFeatureName.iterator();
                        while (itr.hasNext()) {
                            Long userId = itr.next();
                            if (!CollectionUtils.isEmpty(notify) && notify.contains(userId)) {
                                itr.remove();
                            }
                        }
                        notify.addAll(searchUsersByFeatureName);
                    }
                } else {
                    notify = userManagementServiceWrapper.retrieveAllUsersByFranchise(companyId);
                }
            }

            hkHRService.updateShift(hkShiftEntity, notify);
            notificationSent = false;
            createOrUpdateCustomField(hkShiftEntity.getId(), shiftDataBean);
            userManagementServiceWrapper.createLocaleForEntity(shiftDataBean.getShiftName(), "Shift", hkLoginDataBean.getId(), hkLoginDataBean.getCompanyId());
            msg = "Shift updated.";
            return msg;
        }
    }

    public String updateTemporaryShift(TemporaryShiftDataBean temporaryShiftDataBean) {
        Long companyId = hkLoginDataBean.getCompanyId();
        boolean status = temporaryShiftDataBean.isTempShiftStatus();
        Long shiftId = temporaryShiftDataBean.getTempShiftId();
        Long userId = hkLoginDataBean.getId();
        HkShiftEntity hkShiftEntityFromDb = hkHRService.retrieveShiftById(companyId, shiftId);
        boolean isDefault = hkShiftEntityFromDb.getTemporaryShiftFor().getIsDefault();
        HkShiftEntity hkShiftEntity = new HkShiftEntity();
        List<Long> notify = new ArrayList<>();
        Set<Long> searchUsersByFeatureName = userManagementServiceWrapper.searchUsersByFeatureName(Arrays.asList(HkSystemConstantUtil.Feature.MANAGE_SHIFT), companyId);
        List<Long> searchUsersByShift = userManagementServiceWrapper.searchUsersByShift(shiftId, companyId);
        if (!CollectionUtils.isEmpty(searchUsersByShift)) {
            notify.addAll(searchUsersByShift);
        }
        if (!CollectionUtils.isEmpty(searchUsersByFeatureName)) {
            Iterator<Long> itr = searchUsersByFeatureName.iterator();
            while (itr.hasNext()) {
                Long userIdTemp = itr.next();
                if (!CollectionUtils.isEmpty(notify) && notify.contains(userIdTemp)) {
                    itr.remove();
                }
            }
            notify.addAll(searchUsersByFeatureName);
        }
        String msg = "";
        if (!status) {
            hkHRService.removeShift(shiftId, userId, notify);
            msg = "Shift removed.";
            userManagementServiceWrapper.deleteLocaleForEntity(temporaryShiftDataBean.getTempShiftName(), "SHIFT", "CONTENT", hkLoginDataBean.getId(), companyId);
            return msg;
        } else {
            hkShiftEntity = this.convertTemporaryShiftDatabeanToModelForUpdate(temporaryShiftDataBean, hkShiftEntityFromDb);
            if (!notificationSent) {
                notify = new ArrayList<>();
            }
            hkHRService.updateShift(hkShiftEntity, notify);
            notificationSent = false;
            userManagementServiceWrapper.createLocaleForEntity(temporaryShiftDataBean.getTempShiftName(), "Shift", hkLoginDataBean.getId(), hkLoginDataBean.getCompanyId());
            msg = "Shift updated";
            return msg;
        }

    }

    private HkShiftEntity convertShiftDataBeanToHkShiftEntityForUpdate(ShiftDataBean shiftDataBean, HkShiftEntity hkShiftEntity, boolean defaultShift, boolean createShift) {
        Long franchise = hkLoginDataBean.getCompanyId();
        Long userId = hkLoginDataBean.getId();
        Set<HkShiftDtlEntity> hkShiftDtlEntitySet = hkShiftEntity.getHkShiftDtlEntitySet();
        HkShiftDtlEntity hkShiftDtlEntity = new HkShiftDtlEntity();
        for (HkShiftDtlEntity hkShiftDtlEntity1 : hkShiftDtlEntitySet) {
            if (hkShiftDtlEntity1.getSlotType().equals(HkSystemConstantUtil.SHIFT_TYPE.MAIN_TIME)) {
                hkShiftDtlEntity = hkShiftDtlEntity1;
            }
        }
        hkShiftEntity.setId(shiftDataBean.getId());
        hkShiftEntity.setShiftTitle(shiftDataBean.getShiftName());
        hkShiftEntity.setWeekDays(shiftDataBean.getWorkingDayIds());
        Set<HkShiftDtlEntity> hkShiftDtlEntitys = new HashSet<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        String strtTimeDb = dateFormat.format(hkShiftDtlEntity.getStrtTime());
        String endTimDb = dateFormat.format(hkShiftDtlEntity.getEndTime());
        String strtTimeUI = dateFormat.format(shiftDataBean.getShiftStartTime());
        String endTimeUI = dateFormat.format(shiftDataBean.getShiftEndTime());
        if (!strtTimeUI.equalsIgnoreCase(strtTimeDb)) {
            notificationSent = true;
            hkShiftDtlEntity.setStrtTime(HkSystemFunctionUtil.convertToServerDate(shiftDataBean.getShiftStartTime(), hkLoginDataBean.getClientRawOffsetInMin()));
        }
        if (!endTimeUI.equalsIgnoreCase(endTimDb)) {
            notificationSent = true;
            hkShiftDtlEntity.setEndTime(HkSystemFunctionUtil.convertToServerDate(shiftDataBean.getShiftEndTime(), hkLoginDataBean.getClientRawOffsetInMin()));
        }
        hkShiftDtlEntity.setShift(hkShiftEntity);
        hkShiftDtlEntity.setSlotType(HkSystemConstantUtil.SHIFT_TYPE.MAIN_TIME);
        hkShiftDtlEntity.setWeekDays(shiftDataBean.getWorkingDayIds());
        hkShiftDtlEntitys.add(hkShiftDtlEntity);
        List<ShiftBreakDataBean> breakList = shiftDataBean.getBreakList();
        Set<HkShiftDtlEntity> breakSet = hkShiftDtlEntitySet;
        Iterator<HkShiftDtlEntity> breakItr = breakSet.iterator();
        if (!CollectionUtils.isEmpty(breakSet)) {
            while (breakItr.hasNext()) {
                HkShiftDtlEntity mainTime = breakItr.next();
                if (mainTime != null && mainTime.getSlotType() != null && mainTime.getSlotType().equalsIgnoreCase(HkSystemConstantUtil.SHIFT_TYPE.MAIN_TIME)) {
                    breakItr.remove();
                }
            }
        }
        if (!CollectionUtils.isEmpty(breakSet) && CollectionUtils.isEmpty(breakList)) {
            notificationSent = true;
        }
        if (!CollectionUtils.isEmpty(breakList)) {
            HkShiftDtlEntity shiftDtlEntity = null;
            for (ShiftBreakDataBean shiftBreakDataBean : breakList) {
                if (shiftBreakDataBean.getBreakId() != null) {
                    for (HkShiftDtlEntity breakDtlEntity : hkShiftDtlEntitySet) {
                        if (shiftBreakDataBean.getBreakId().equals(breakDtlEntity.getId())) {
                            String breakStrtTimeDb = dateFormat.format(breakDtlEntity.getStrtTime());
                            String breakEndTimDb = dateFormat.format(breakDtlEntity.getEndTime());
                            String breakstrtTimeUI = dateFormat.format(shiftBreakDataBean.getBreakStartTime());
                            String breakendTimeUI = dateFormat.format(shiftBreakDataBean.getBreakEndTime());
                            if (!breakStrtTimeDb.equalsIgnoreCase(breakstrtTimeUI) || !breakendTimeUI.equalsIgnoreCase(breakEndTimDb)) {
                                notificationSent = true;
                            }
                            shiftDtlEntity = breakDtlEntity;
                            shiftDtlEntity.setStrtTime(HkSystemFunctionUtil.convertToServerDate(shiftBreakDataBean.getBreakStartTime(), hkLoginDataBean.getClientRawOffsetInMin()));
                            shiftDtlEntity.setEndTime(HkSystemFunctionUtil.convertToServerDate(shiftBreakDataBean.getBreakEndTime(), hkLoginDataBean.getClientRawOffsetInMin()));
                            shiftDtlEntity.setIsArchive(false);
                            shiftDtlEntity.setShift(hkShiftEntity);
                            shiftDtlEntity.setSlotTitle(shiftBreakDataBean.getBreakSlotTitle());
                            shiftDtlEntity.setSlotType(HkSystemConstantUtil.SHIFT_TYPE.BREAK_TIME);
                            shiftDtlEntity.setWeekDays(shiftDataBean.getWorkingDayIds());
                            hkShiftDtlEntitys.add(shiftDtlEntity);
                        }
                    }
                } else {
                    notificationSent = true;
                    shiftDtlEntity = new HkShiftDtlEntity();
                    shiftDtlEntity.setCreatedBy(userId);
                    shiftDtlEntity.setCreatedOn(new Date());
                    shiftDtlEntity.setEffectedFrm(new Date());
                    shiftDtlEntity.setIsArchive(false);
                    shiftDtlEntity.setStrtTime(HkSystemFunctionUtil.convertToServerDate(shiftBreakDataBean.getBreakStartTime(), hkLoginDataBean.getClientRawOffsetInMin()));
                    shiftDtlEntity.setEndTime(HkSystemFunctionUtil.convertToServerDate(shiftBreakDataBean.getBreakEndTime(), hkLoginDataBean.getClientRawOffsetInMin()));
                    shiftDtlEntity.setIsArchive(false);
                    shiftDtlEntity.setShift(hkShiftEntity);
                    shiftDtlEntity.setSlotTitle(shiftBreakDataBean.getBreakSlotTitle());
                    shiftDtlEntity.setSlotType(HkSystemConstantUtil.SHIFT_TYPE.BREAK_TIME);
                    shiftDtlEntity.setWeekDays(shiftDataBean.getWorkingDayIds());
                    hkShiftDtlEntitys.add(shiftDtlEntity);
                }
            }
        }
        hkShiftEntity.setHkShiftDtlEntitySet(hkShiftDtlEntitys);
        if (!defaultShift) {
            Set<HkShiftDepartmentEntity> hkShiftDepartmentEntitys = new HashSet<>();
            hkShiftEntity.setHkShiftDepartmentEntitySet(hkShiftDepartmentEntitys);
            String departmentIds = shiftDataBean.getDepartmentIds();
            if (departmentIds != null && !departmentIds.equals("")) {
                String[] splits = departmentIds.split(",");
                Long[] deptIds = new Long[splits.length];
                int i = 0;
                for (String s : splits) {
                    deptIds[i++] = Long.parseLong(s);
                }
                for (Long deptId : deptIds) {
                    HkShiftDepartmentEntityPK hsdepk = new HkShiftDepartmentEntityPK();
                    hsdepk.setDepartment(deptId);
                    HkShiftDepartmentEntity hkShiftDepartmentEntity = new HkShiftDepartmentEntity(hsdepk);
                    hkShiftDepartmentEntity.setLastModifiedOn(new Date());
                    hkShiftDepartmentEntity.setIsArchive(false);
                    hkShiftDepartmentEntity.setHkShiftEntity(hkShiftEntity);
                    hkShiftDepartmentEntitys.add(hkShiftDepartmentEntity);
                }

            }
        }

        return hkShiftEntity;
    }

    public List<ShiftDataBean> searchShift(Long franchise, String searchText) throws GenericDatabaseException {
        if (StringUtils.hasText(searchText) && searchText.length() >= HkSystemConstantUtil.MIN_SEARCH_LENGTH) {
            List<ShiftDataBean> shiftDataBeans = new ArrayList<>();
            if (searchText.toUpperCase().contains(HkSystemConstantUtil.SHIFT_SEARCH_CODE.DEPARTMENT)) {
                List<Long> departmentIds = new ArrayList<>();
                searchText = searchText.substring(searchText.toUpperCase().indexOf(HkSystemConstantUtil.SHIFT_SEARCH_CODE.DEPARTMENT) + HkSystemConstantUtil.SHIFT_SEARCH_CODE.DEPARTMENT.length()).trim();
                Map<Long, String> searchDepartments = userManagementServiceWrapper.searchDepartmentsWithoutWorkFlow(searchText, franchise);
                String departmentNames = "";
                if (!CollectionUtils.isEmpty(searchDepartments)) {
                    for (Map.Entry<Long, String> entry : searchDepartments.entrySet()) {
                        Long deptId = entry.getKey();
                        departmentNames += entry.getValue() + "-";
                        departmentIds.add(deptId);
                    }
                    if (departmentNames != null && !StringUtils.isEmpty(departmentNames)) {
                        departmentNames = departmentNames.substring(0, departmentNames.length() - 1);
                    }
                }
                List<HkShiftEntity> searchShifts = hkHRService.searchShifts(franchise, searchText, departmentIds);
                if (!CollectionUtils.isEmpty(searchShifts)) {
                    for (HkShiftEntity hkShiftEntity : searchShifts) {
                        ShiftDataBean shiftDataBean = new ShiftDataBean();
                        shiftDataBean.setId(hkShiftEntity.getId());
                        shiftDataBean.setShiftName(hkShiftEntity.getShiftTitle());
                        shiftDataBean.setDepartmentNames(departmentNames);
                        shiftDataBeans.add(shiftDataBean);
                    }
                    return shiftDataBeans;
                }
            } else {
                List<HkShiftEntity> searchShifts = hkHRService.searchShifts(franchise, searchText);
                if (!CollectionUtils.isEmpty(searchShifts)) {
                    List<Long> deptIds = new ArrayList<>();
                    Map<String, Object> equal = new HashMap<>();
                    List<String> projections = new ArrayList<>();
                    equal.put(UMDepartmentDao.COMPANY, getCompnies(franchise));
                    Map<GenericDao.QueryOperators, Object> criterias = new HashMap<>();
                    criterias.put(GenericDao.QueryOperators.EQUAL, equal);
                    projections.add(UMDepartmentDao.ID);
                    projections.add(UMDepartmentDao.DEPT_NAME);
                    List<UMDepartment> retrieveDepartments = departmentService.retrieveDepartments(projections, criterias, projections);

                    for (HkShiftEntity hkShiftEntity : searchShifts) {
                        String departmentNames = "";
                        ShiftDataBean shiftDataBean = new ShiftDataBean();
                        shiftDataBean.setId(hkShiftEntity.getId());
                        shiftDataBean.setShiftName(hkShiftEntity.getShiftTitle());
                        Set<HkShiftDepartmentEntity> hkShiftDepartmentEntitySet = new HashSet<>();
                        if (hkShiftEntity.getTemporaryShiftFor() == null) {
                            hkShiftDepartmentEntitySet = hkShiftEntity.getHkShiftDepartmentEntitySet();
                        } else {
                            hkShiftDepartmentEntitySet = hkShiftEntity.getTemporaryShiftFor().getHkShiftDepartmentEntitySet();
                        }
                        if (!CollectionUtils.isEmpty(hkShiftDepartmentEntitySet)) {
                            for (HkShiftDepartmentEntity hkShiftDepartmentEntity : hkShiftDepartmentEntitySet) {
                                Long departmentId = null;
                                if (!hkShiftDepartmentEntity.getIsArchive()) {
                                    departmentId = hkShiftDepartmentEntity.getHkShiftDepartmentEntityPK().getDepartment();
                                    deptIds.add(departmentId);
                                }

                            }
                        }
                        if (!CollectionUtils.isEmpty(retrieveDepartments)) {
                            for (UMDepartment uMDepartment : retrieveDepartments) {
                                for (Long id : deptIds) {
                                    if (id.equals(uMDepartment.getId())) {
                                        departmentNames += uMDepartment.getDeptName() + "-";
                                        break;
                                    }
                                }

                            }
                        }
                        if (departmentNames != null && !StringUtils.isEmpty(departmentNames)) {
                            departmentNames = departmentNames.substring(0, departmentNames.length() - 1);
                        }
                        shiftDataBean.setDepartmentNames(departmentNames);
                        shiftDataBeans.add(shiftDataBean);
                    }
                }
                return shiftDataBeans;
            }
        }
        return null;
    }

    private ShiftDataBean convertHkShiftEntityToShiftDataBean(ShiftDataBean shiftDataBean, HkShiftEntity hkShiftEntity, boolean subShift) {
        if (shiftDataBean == null) {
            shiftDataBean = new ShiftDataBean();
        }
        hkShiftEntity = hkHRService.retrieveShiftById(hkLoginDataBean.getCompanyId(), hkShiftEntity.getId());
        shiftDataBean.setId(hkShiftEntity.getId());
        shiftDataBean.setShiftName(hkShiftEntity.getShiftTitle());
        if (subShift) {
            shiftDataBean.setWorkingDayIds(hkShiftEntity.getWeekDays());
            List<ShiftBreakDataBean> breakDataBeans = new ArrayList<>();
            Set<HkShiftDtlEntity> hkShiftDtlEntitySet = hkShiftEntity.getHkShiftDtlEntitySet();
            if (hkShiftDtlEntitySet != null && !hkShiftDtlEntitySet.isEmpty()) {
                for (HkShiftDtlEntity hsde : hkShiftDtlEntitySet) {
                    if (hsde.getSlotType().equals(HkSystemConstantUtil.SHIFT_TYPE.BREAK_TIME)) {
                        ShiftBreakDataBean breakDataBean = new ShiftBreakDataBean();

                        breakDataBean.setBreakEndTime(HkSystemFunctionUtil.convertToClientDate(hsde.getEndTime(), hkLoginDataBean.getClientRawOffsetInMin()));
                        breakDataBean.setBreakSlotTitle(hsde.getSlotTitle());
                        breakDataBean.setBreakStartTime(HkSystemFunctionUtil.convertToClientDate(hsde.getStrtTime(), hkLoginDataBean.getClientRawOffsetInMin()));
                        breakDataBean.setBreakId(hsde.getId());

                        breakDataBeans.add(breakDataBean);
                    } else if (hsde.getSlotType().equals(HkSystemConstantUtil.SHIFT_TYPE.MAIN_TIME)) {
                        shiftDataBean.setShiftStartTime(HkSystemFunctionUtil.convertToClientDate(hsde.getStrtTime(), hkLoginDataBean.getClientRawOffsetInMin()));
                        shiftDataBean.setShiftEndTime(HkSystemFunctionUtil.convertToClientDate(hsde.getEndTime(), hkLoginDataBean.getClientRawOffsetInMin()));
                    }
                }
            }
            shiftDataBean.setBreakList(breakDataBeans);
            StringBuffer deptIds = new StringBuffer();
            Set<HkShiftDepartmentEntity> hkShiftDepartmentEntitySet = hkShiftEntity.getHkShiftDepartmentEntitySet();
            for (HkShiftDepartmentEntity hkShiftDepartmentEntity : hkShiftDepartmentEntitySet) {
                if (hkShiftDepartmentEntity.getIsArchive()) {
                    hkShiftDepartmentEntitySet.remove(hkShiftDepartmentEntity);
                }
            }
            if (hkShiftDepartmentEntitySet != null && !hkShiftDepartmentEntitySet.isEmpty()) {
                Iterator<HkShiftDepartmentEntity> iterator = hkShiftDepartmentEntitySet.iterator();
                while (iterator.hasNext()) {
                    deptIds.append(iterator.next().getHkShiftDepartmentEntityPK().getDepartment());
                    if (iterator.hasNext()) {
                        deptIds.append(",");
                    }
                }
            }
            shiftDataBean.setDepartmentIds(deptIds.toString());

        }
        return shiftDataBean;
    }

    private HkShiftEntity convertTemporaryShiftDatabeanToModelForUpdate(TemporaryShiftDataBean temporaryShiftDataBean, HkShiftEntity hkShiftEntity) {
        Long franchise = hkLoginDataBean.getCompanyId();
        hkShiftEntity.setFranchise(franchise);
        hkShiftEntity.setIsArchive(false);
        hkShiftEntity.setLastModifiedBy(hkLoginDataBean.getId());
        hkShiftEntity.setLastModifiedOn(new Date());
        hkShiftEntity.setShiftTitle(temporaryShiftDataBean.getTempShiftName());
        hkShiftEntity.setWeekDays(temporaryShiftDataBean.getTempShiftWorkingDayIds());

        Set<HkShiftDtlEntity> hkShiftDtlEntitySet = hkShiftEntity.getHkShiftDtlEntitySet();
        HkShiftDtlEntity hkShiftDtlEntity = new HkShiftDtlEntity();
        Set<HkShiftDtlEntity> hkShiftDtlEntitys = new HashSet<>();
        for (HkShiftDtlEntity hkShiftDtlEntity1 : hkShiftDtlEntitySet) {
            if (hkShiftDtlEntity1.getSlotType().equals(HkSystemConstantUtil.SHIFT_TYPE.MAIN_TIME)) {
                hkShiftDtlEntity = hkShiftDtlEntity1;
            }
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        String strtTimeDb = dateFormat.format(hkShiftDtlEntity.getStrtTime());
        String endTimDb = dateFormat.format(hkShiftDtlEntity.getEndTime());
        String strtTimeUI = dateFormat.format(temporaryShiftDataBean.getTempShiftStartTime());
        String endTimeUI = dateFormat.format(temporaryShiftDataBean.getTempShiftEndTime());
        Boolean timeChanged = false;
        if (!strtTimeUI.equals(strtTimeDb)) {
            notificationSent = true;
            timeChanged = true;
            hkShiftDtlEntity.setStrtTime(HkSystemFunctionUtil.convertToServerDate(temporaryShiftDataBean.getTempShiftStartTime(), hkLoginDataBean.getClientRawOffsetInMin()));
        }
        if (!endTimeUI.equals(endTimDb)) {
            notificationSent = true;
            timeChanged = true;
            hkShiftDtlEntity.setEndTime(HkSystemFunctionUtil.convertToServerDate(temporaryShiftDataBean.getTempShiftEndTime(), hkLoginDataBean.getClientRawOffsetInMin()));
        }

        hkShiftDtlEntity.setIsArchive(false);
        hkShiftDtlEntity.setShift(hkShiftEntity);
        hkShiftDtlEntity.setSlotType(HkSystemConstantUtil.SHIFT_TYPE.MAIN_TIME);
        hkShiftDtlEntity.setWeekDays(temporaryShiftDataBean.getTempShiftWorkingDayIds());
        hkShiftDtlEntitys.add(hkShiftDtlEntity);
        Set<HkShiftRuleEntity> hkShiftRuleEntitys = new HashSet<>();
        Set<HkShiftRuleEntity> hkShiftRuleEntitySet = hkShiftEntity.getHkShiftRuleEntitySet();
        HkShiftRuleEntity hsreBegin = new HkShiftRuleEntity();
        HkShiftRuleEntity hsreEnd = new HkShiftRuleEntity();
        HkShiftRuleEntity hkShiftRuleEntity = new HkShiftRuleEntity();
        HkShiftEntity entity = new HkShiftEntity();
        boolean detailChanged = false;
        Date frmDt = hkShiftEntity.getFrmDt();
        Date toDt = hkShiftEntity.getToDt();
        Date tempShiftFromDate = temporaryShiftDataBean.getTempShiftFromDate();
        Date tempShiftEndDate = temporaryShiftDataBean.getTempShiftEndDate();
        SimpleDateFormat formateDate = new SimpleDateFormat("dd/MM/yy");
        if (!formateDate.format(frmDt).equalsIgnoreCase(formateDate.format(tempShiftFromDate)) || !formateDate.format(toDt).equalsIgnoreCase(formateDate.format(tempShiftEndDate))) {
            notificationSent = true;
        }
        for (HkShiftRuleEntity hkShiftRuleEntityItr : hkShiftRuleEntitySet) {
            if (temporaryShiftDataBean.isSetRule() && hkShiftEntity.getHasRule()) {
                if (temporaryShiftDataBean.getBeginRuleId().equals(hkShiftRuleEntityItr.getHkShiftRuleEntityPK().getRuleType()) && (!temporaryShiftDataBean.getBeginsEventType().contains(hkShiftRuleEntityItr.getEventType()) || !hkShiftRuleEntityItr.getDayCnt().equals(temporaryShiftDataBean.getBeginsDayCount()) || !hkShiftRuleEntityItr.getEventInstance().equals(BigInteger.valueOf(temporaryShiftDataBean.getBeginsEventOrHolidayId())))) {
                    detailChanged = true;
                    break;
                } else if (!detailChanged && temporaryShiftDataBean.getEndRuleId().equals(hkShiftRuleEntityItr.getHkShiftRuleEntityPK().getRuleType()) && (!temporaryShiftDataBean.getEndsEventType().contains(hkShiftRuleEntityItr.getEventType()) || !hkShiftRuleEntityItr.getDayCnt().equals(temporaryShiftDataBean.getEndsDayCount()) || !hkShiftRuleEntityItr.getEventInstance().equals(BigInteger.valueOf(temporaryShiftDataBean.getEndsEventOrHolidayId())))) {
                    detailChanged = true;
                    break;
                }

            } else if (!temporaryShiftDataBean.isSetRule() && !hkShiftEntity.getHasRule()) {
                if (!formateDate.format(frmDt).equalsIgnoreCase(formateDate.format(tempShiftFromDate)) || !formateDate.format(toDt).equalsIgnoreCase(formateDate.format(tempShiftEndDate))) {
                    detailChanged = true;
                    break;
                }

            } else {
                detailChanged = true;
                break;
            }
        }
        if (detailChanged && temporaryShiftDataBean.isSetRule()) {
            hsreBegin.setDayCnt(temporaryShiftDataBean.getBeginsDayCount());
            hsreBegin.setEventAction(temporaryShiftDataBean.getBeginsAction().charAt(0) + "");
            hsreBegin.setEventInstance(BigInteger.valueOf(temporaryShiftDataBean.getBeginsEventOrHolidayId().longValue()));
            hsreBegin.setEventType(temporaryShiftDataBean.getBeginsEventType().charAt(0) + "");
            hsreBegin.setHkShiftEntity(hkShiftEntity);
            HkShiftRuleEntityPK hkShiftRuleEntityPK = new HkShiftRuleEntityPK();
            HkShiftRuleEntityPK hkShiftRuleEntityPKEnd = new HkShiftRuleEntityPK();
            //            HkShiftRuleEntityPK beginRuleEntityPK = hkShiftRuleEntityPK;
            hkShiftRuleEntityPK.setRuleType(HkSystemConstantUtil.ShiftRuleType.BEGINS);
            hsreBegin.setHkShiftRuleEntityPK(hkShiftRuleEntityPK);
            hsreEnd.setDayCnt(temporaryShiftDataBean.getEndsDayCount());
            hsreEnd.setEventAction(temporaryShiftDataBean.getEndsAction().charAt(0) + "");
            hsreEnd.setEventInstance(BigInteger.valueOf(temporaryShiftDataBean.getEndsEventOrHolidayId().longValue()));
            hsreEnd.setEventType(temporaryShiftDataBean.getEndsEventType().charAt(0) + "");
            hsreEnd.setHkShiftEntity(hkShiftEntity);
            hkShiftRuleEntityPKEnd.setRuleType(HkSystemConstantUtil.ShiftRuleType.ENDS);
            hsreEnd.setHkShiftRuleEntityPK(hkShiftRuleEntityPKEnd);
            hkShiftRuleEntitys.add(hsreBegin);
            hkShiftRuleEntitys.add(hsreEnd);
        } else if (detailChanged && !temporaryShiftDataBean.isSetRule()) {
            // 1 entry
            HkShiftRuleEntityPK hkShiftRuleEntityPK = new HkShiftRuleEntityPK();
            hkShiftRuleEntity.setHkShiftEntity(hkShiftEntity);
            hkShiftRuleEntityPK.setRuleType(HkSystemConstantUtil.ShiftRuleType.DATE_RANGE);
            hkShiftRuleEntity.setHkShiftRuleEntityPK(hkShiftRuleEntityPK);
            hkShiftRuleEntitys.add(hkShiftRuleEntity);
        }

        List<ShiftBreakDataBean> breakList = temporaryShiftDataBean.getTempShiftBreakList();

        Set<HkShiftDtlEntity> breakSet = hkShiftDtlEntitySet;
        Iterator<HkShiftDtlEntity> breakItr = breakSet.iterator();
        if (!CollectionUtils.isEmpty(breakSet)) {
            while (breakItr.hasNext()) {
                HkShiftDtlEntity mainTime = breakItr.next();
                if (mainTime != null && mainTime.getSlotType() != null && mainTime.getSlotType().equalsIgnoreCase(HkSystemConstantUtil.SHIFT_TYPE.MAIN_TIME)) {
                    breakItr.remove();
                }
            }
        }
        if (!CollectionUtils.isEmpty(breakSet) && CollectionUtils.isEmpty(breakList)) {
            notificationSent = true;
        }
        if (!CollectionUtils.isEmpty(breakList)) {
            HkShiftDtlEntity shiftDtlEntity = null;
            for (ShiftBreakDataBean shiftBreakDataBean : breakList) {
                if (shiftBreakDataBean.getBreakId() != null) {
                    for (HkShiftDtlEntity breakDtlEntity : hkShiftDtlEntitySet) {
                        if (shiftBreakDataBean.getBreakId().equals(breakDtlEntity.getId())) {
                            shiftDtlEntity = breakDtlEntity;
                            if (detailChanged) {
                                shiftDtlEntity.setCreatedBy(hkLoginDataBean.getId());
                                shiftDtlEntity.setCreatedOn(new Date());
                            }
                            String strtBreakTimeDb = dateFormat.format(breakDtlEntity.getStrtTime());
                            String endBreakTimDb = dateFormat.format(breakDtlEntity.getEndTime());
                            String strtBreakTimeUI = dateFormat.format(shiftBreakDataBean.getBreakStartTime());
                            String endBreakTimeUI = dateFormat.format(shiftBreakDataBean.getBreakEndTime());
                            if (!strtBreakTimeDb.equalsIgnoreCase(strtBreakTimeUI)) {
                                notificationSent = true;
                                shiftDtlEntity.setStrtTime(HkSystemFunctionUtil.convertToServerDate(shiftBreakDataBean.getBreakStartTime(), hkLoginDataBean.getClientRawOffsetInMin()));
                            }
                            if (!endBreakTimDb.equalsIgnoreCase(endBreakTimeUI)) {
                                notificationSent = true;
                                shiftDtlEntity.setEndTime(HkSystemFunctionUtil.convertToServerDate(shiftBreakDataBean.getBreakEndTime(), hkLoginDataBean.getClientRawOffsetInMin()));
                            }
                            shiftDtlEntity.setIsArchive(false);
                            shiftDtlEntity.setShift(hkShiftEntity);
                            shiftDtlEntity.setSlotTitle(shiftBreakDataBean.getBreakSlotTitle());
                            shiftDtlEntity.setSlotType(HkSystemConstantUtil.SHIFT_TYPE.BREAK_TIME);
                            shiftDtlEntity.setWeekDays(temporaryShiftDataBean.getTempShiftWorkingDayIds());
                            hkShiftDtlEntitys.add(shiftDtlEntity);
                        }
                    }
                } else {
                    notificationSent = true;
                    shiftDtlEntity = new HkShiftDtlEntity();
                    shiftDtlEntity.setCreatedBy(hkLoginDataBean.getId());
                    shiftDtlEntity.setCreatedOn(new Date());
                    shiftDtlEntity.setEffectedFrm(new Date());
                    shiftDtlEntity.setIsArchive(false);
                    shiftDtlEntity.setStrtTime(HkSystemFunctionUtil.convertToServerDate(shiftBreakDataBean.getBreakStartTime(), hkLoginDataBean.getClientRawOffsetInMin()));
                    shiftDtlEntity.setEndTime(HkSystemFunctionUtil.convertToServerDate(shiftBreakDataBean.getBreakEndTime(), hkLoginDataBean.getClientRawOffsetInMin()));
                    shiftDtlEntity.setIsArchive(false);
                    shiftDtlEntity.setShift(hkShiftEntity);
                    shiftDtlEntity.setSlotTitle(shiftBreakDataBean.getBreakSlotTitle());
                    shiftDtlEntity.setSlotType(HkSystemConstantUtil.SHIFT_TYPE.BREAK_TIME);
                    shiftDtlEntity.setWeekDays(temporaryShiftDataBean.getTempShiftWorkingDayIds());
                    hkShiftDtlEntitys.add(shiftDtlEntity);
                }
            }
        }
        hkShiftEntity.setHkShiftDtlEntitySet(hkShiftDtlEntitys);

        hkShiftEntity.setFrmDt(HkSystemFunctionUtil.convertToServerDate(temporaryShiftDataBean.getTempShiftFromDate(), hkLoginDataBean.getClientRawOffsetInMin()));
        hkShiftEntity.setToDt(HkSystemFunctionUtil.convertToServerDate(temporaryShiftDataBean.getTempShiftEndDate(), hkLoginDataBean.getClientRawOffsetInMin()));
        hkShiftEntity.setHasRule(temporaryShiftDataBean.isSetRule());
        if (detailChanged) {
            hkShiftEntity.setCreatedOn(new Date());
            hkShiftEntity.setCreatedBy(hkLoginDataBean.getId());
            hkShiftDtlEntity.setCreatedOn(new Date());
            hkShiftDtlEntity.setCreatedOn(new Date());
            hkShiftEntity.setHkShiftRuleEntitySet(hkShiftRuleEntitys);
        }
        return hkShiftEntity;
    }

    private List<String> parseString(String depId) {
        Long[] deptIds = null;
        String departmentIds = depId;
        if (departmentIds != null && !departmentIds.equals("")) {
            String[] splits = departmentIds.split(",");
            deptIds = new Long[splits.length];
            int i = 0;
            for (String s : splits) {
                deptIds[i++] = Long.parseLong(s);
            }
        }
        List<String> code = new ArrayList<>();
        List<Long> notify = new ArrayList<>();
        if (deptIds != null) {

            for (Long deptId : deptIds) {
                code.add(deptId + ":" + HkSystemConstantUtil.RecipientCodeType.DEPARTMENT);
            }

        }
        return code;
    }

    public void createOrUpdateCustomField(Long id, ShiftDataBean shiftDataBean) {
        Map<Long, Map<String, Object>> val = new HashMap<>();
        val.put(id, shiftDataBean.getShiftCustom());
        List<String> uiFieldList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(shiftDataBean.getShiftDbType())) {
            for (Map.Entry<String, String> entrySet : shiftDataBean.getShiftDbType().entrySet()) {
                uiFieldList.add(entrySet.getKey());
            }
        }
        Map<String, String> uiFieldMap = fieldService.retrieveUIFieldNameWithComponentTypes(uiFieldList);
        List<CustomField> makeCustomField = customFieldSevice.makeCustomField(val, shiftDataBean.getShiftDbType(), uiFieldMap, HkSystemConstantUtil.FeatureNameForCustomField.SHIFT.toString(), hkLoginDataBean.getCompanyId(), id);
        Map<HkSystemConstantUtil.SectionNameForCustomField, List<CustomField>> map = new HashMap<>();
        map.put(HkSystemConstantUtil.SectionNameForCustomField.GENERAL, makeCustomField);
        customFieldSevice.saveOrUpdate(id, HkSystemConstantUtil.FeatureNameForCustomField.SHIFT, hkLoginDataBean.getCompanyId(), map);
    }

    public boolean userExistForShift(Long shiftId, Long companyId) {
        List<Long> searchUsersByShift = userManagementServiceWrapper.searchUsersByShift(shiftId, companyId);
        if (!CollectionUtils.isEmpty(searchUsersByShift)) {
            return true;
        }
        return false;
    }

    public ShiftDataBean retrieveCustomFieldData(Long shiftId) {
        Map<HkSystemConstantUtil.SectionNameForCustomField, List<Map<Long, Map<String, Object>>>> retrieveDocumentByInstanceId = customFieldTransformerBean.retrieveDocumentByInstanceId(shiftId, HkSystemConstantUtil.FeatureNameForCustomField.SHIFT, hkLoginDataBean.getCompanyId());
        ShiftDataBean shiftDataBean = null;
        if (!CollectionUtils.isEmpty(retrieveDocumentByInstanceId)) {
            List<Map<Long, Map<String, Object>>> maps = retrieveDocumentByInstanceId.get(HkSystemConstantUtil.SectionNameForCustomField.GENERAL);
            if (maps != null) {
                for (Map<Long, Map<String, Object>> map : maps) {
                    if (map.get(shiftId) != null) {
                        shiftDataBean = new ShiftDataBean();
                        shiftDataBean.setShiftCustom(map.get(shiftId));
                        break;
                    }
                }
            }
        }
        return shiftDataBean;
    }

    private List<Long> getCompnies(Long companyId) {
        List<Long> companyIds = new ArrayList<>();
        companyIds.add(0l);
        if (companyId != null) {
            companyIds.add(companyId);
        }
        return companyIds;
    }

    public Boolean checkIfAnyShiftAssociatedWithDepartment(Long depId) {
        Boolean isShiftAssociated = false;
        Map<Long, String> shiftsByDepartmentList = hkHRService.retrieveShiftsByDepartment(hkLoginDataBean.getCompanyId(), depId);
        if (!CollectionUtils.isEmpty(shiftsByDepartmentList)) {
            isShiftAssociated = true;
        }
        return isShiftAssociated;
    }

}
