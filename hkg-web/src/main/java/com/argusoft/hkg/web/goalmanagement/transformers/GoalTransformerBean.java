package com.argusoft.hkg.web.goalmanagement.transformers;

import com.argusoft.hkg.common.constantutil.HkSystemConstantUtil;
import com.argusoft.hkg.core.HkFieldService;
import com.argusoft.hkg.core.HkFoundationService;
import com.argusoft.hkg.model.HkGoalPermissionEntity;
import com.argusoft.hkg.model.HkGoalTemplateEntity;
import com.argusoft.hkg.nosql.core.HkCustomFieldService;
import com.argusoft.hkg.nosql.core.HkGoalService;
import com.argusoft.hkg.nosql.core.HkRuleService;
import com.argusoft.hkg.nosql.model.CustomField;
import com.argusoft.hkg.nosql.model.HkRuleDocument;
import com.argusoft.hkg.nosql.model.HkRuleSetDocument;
import com.argusoft.hkg.nosql.model.HkUserGoalStatusDocument;
import com.argusoft.hkg.web.customfield.transformers.CustomFieldTransformerBean;
import com.argusoft.hkg.web.goalmanagement.databeans.GoalTemplateDataBean;
import com.argusoft.hkg.web.usermanagement.databeans.GoalPermissionDataBean;
import com.argusoft.hkg.web.center.usermanagement.databeans.LoginDataBean;
import com.argusoft.hkg.web.goalmanagement.databeans.GoalSheetDataBean;
import com.argusoft.hkg.web.usermanagement.databeans.RuleDatabean;
import com.argusoft.hkg.web.usermanagement.databeans.RuleSetDataBean;
import com.argusoft.hkg.web.usermanagement.transformers.RuleManagementTransformerBean;
import com.argusoft.hkg.web.usermanagement.wrapper.UserManagementServiceWrapper;
import com.argusoft.hkg.web.util.SelectItem;
import com.argusoft.usermanagement.common.model.UMDepartment;
import com.argusoft.usermanagement.common.model.UMRole;
import com.argusoft.usermanagement.common.model.UMUser;
import com.argusoft.usermanagement.common.model.UMUserRole;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 *
 * @author rajkumar
 */
@Service
public class GoalTransformerBean {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private HkFoundationService foundationService;

    @Autowired
    LoginDataBean loginDataBean;

    @Autowired
    RuleManagementTransformerBean ruleManagementTransformerBean;

    @Autowired
    UserManagementServiceWrapper userManagementServiceWrapper;
    
    @Autowired
    HkRuleService ruleService;

    @Autowired
    HkCustomFieldService customFieldSevice;

    @Autowired
    HkFieldService fieldService;

    @Autowired
    CustomFieldTransformerBean customFieldTransformerBean;

    @Autowired
    HkGoalService goalService;

    public List<GoalPermissionDataBean> retrieveGoalPermissionByDesignations(List<Long> designationIds) {
        List<HkGoalPermissionEntity> goalPermissions = foundationService.retrieveActiveGoalPermissionByDesignations(designationIds);
        List<GoalPermissionDataBean> result = new ArrayList<>();
        if (!CollectionUtils.isEmpty(goalPermissions)) {
            for (HkGoalPermissionEntity hkGoalPermissionEntity : goalPermissions) {
                result.add(this.convertRolePermissionEntityToDataBean(hkGoalPermissionEntity, null));
            }
        } else {
            LOGGER.info("goalPermission is empty");
        }
        return result;
    }

    public GoalPermissionDataBean convertRolePermissionEntityToDataBean(HkGoalPermissionEntity permissionEntity, GoalPermissionDataBean permissionDataBean) {
        if (permissionDataBean == null) {
            permissionDataBean = new GoalPermissionDataBean();
        }

        permissionDataBean.setId(permissionEntity.getId());
        permissionDataBean.setAccessOfFeature(permissionEntity.getAccessOfFeature());
        permissionDataBean.setDesignation(permissionEntity.getDesignation());
        permissionDataBean.setReferenceInstance(Long.toString(permissionEntity.getReferenceInstance()));
        permissionDataBean.setReferenceType(permissionEntity.getReferenceType());

        return permissionDataBean;
    }

    public Long saveGoalTemplate(GoalTemplateDataBean goalTemplateDataBean) {
        if (StringUtils.isEmpty(goalTemplateDataBean.getStatus())) {
            goalTemplateDataBean.setFranchise(loginDataBean.getCompanyId());
            HkGoalTemplateEntity goalTemplateEntity = convertGoalDataBeanToModel(goalTemplateDataBean, null);
            goalTemplateEntity.setStatus(HkSystemConstantUtil.GoalTemplateStatus.PENDING);
            HkRuleSetDocument ruleSetDocument = new HkRuleSetDocument();
            if (goalTemplateDataBean.getType().equals(HkSystemConstantUtil.GoalTemplateType.NUMERIC)) {
                ruleSetDocument = ruleManagementTransformerBean.convertRuleSetDatabeanToRuleSetDocument(HkSystemConstantUtil.CREATE_OPERATION, goalTemplateDataBean.getRuleList(), ruleSetDocument);
                ruleSetDocument.setId(null);
                ruleSetDocument.setCreatedOn(new Date());
                ruleSetDocument.setCreatedBy(loginDataBean.getId());
                ruleSetDocument.setLastModifiedOn(new Date());
                ruleSetDocument.setLastModifiedBy(loginDataBean.getId());
            }

            Long id = foundationService.saveGoalTemplateWithRules(goalTemplateEntity, ruleSetDocument);
            this.createCustomField(id, goalTemplateDataBean);
            return id;
        } else {
            if (goalTemplateDataBean.getStatus().equals(HkSystemConstantUtil.GoalTemplateStatus.PENDING) || goalTemplateDataBean.getStatus().equals(HkSystemConstantUtil.GoalTemplateStatus.DISCARDED)) {
                HkGoalTemplateEntity goalTemplateEntity = convertGoalDataBeanToModel(goalTemplateDataBean, null);
                List<HkGoalTemplateEntity> entitys = new ArrayList<>();
                goalTemplateEntity.setFranchise(loginDataBean.getCompanyId());
                HkRuleSetDocument ruleSetDocument = new HkRuleSetDocument();
                if (goalTemplateDataBean.getType().equals(HkSystemConstantUtil.GoalTemplateType.NUMERIC)) {
                    ruleSetDocument = ruleManagementTransformerBean.convertRuleSetDatabeanToRuleSetDocument(HkSystemConstantUtil.CREATE_OPERATION, goalTemplateDataBean.getRuleList(), ruleSetDocument);
                    ruleSetDocument.setId(null);
//                    ruleSetDocument.setCreatedOn(new Date());
//                    ruleSetDocument.setCreatedBy(loginDataBean.getId());
                    ruleSetDocument.setLastModifiedOn(new Date());
                    ruleSetDocument.setLastModifiedBy(loginDataBean.getId());
                }
                if (goalTemplateDataBean.getStatus().equals(HkSystemConstantUtil.GoalTemplateStatus.DISCARDED)) {
                    goalTemplateEntity.setStatus(HkSystemConstantUtil.GoalTemplateStatus.PENDING);
                }
                Long id = foundationService.saveGoalTemplateWithRules(goalTemplateEntity, ruleSetDocument);
                this.createCustomField(id, goalTemplateDataBean);
                return id;
//                entitys.add(goalTemplateEntity);

//                foundationService.saveGoalTemplates(entitys);
            } else if (goalTemplateDataBean.getStatus().equals(HkSystemConstantUtil.GoalTemplateStatus.ACTIVE)) {
                HkGoalTemplateEntity originalObject = foundationService.retrieveGoalTemplateById(goalTemplateDataBean.getId());
                HkGoalTemplateEntity goalTemplateEntity = convertGoalDataBeanToModel(goalTemplateDataBean, null);
                Comparator<HkGoalTemplateEntity> comparator = new Comparator<HkGoalTemplateEntity>() {
                    @Override
                    public int compare(HkGoalTemplateEntity o1, HkGoalTemplateEntity o2) {
                        if ((o1.getId() == o2.getId()) && (o1.getForService() == o2.getForService()) && (o1.getForDepartment() == o2.getForDepartment()) && (o1.getForDesignation() == o2.getForDesignation())) {
                            return 0;
                        } else {
                            return 1;
                        }
                    }
                };
                int compareResult = comparator.compare(originalObject, goalTemplateEntity);
                HkRuleSetDocument ruleSetDocument = new HkRuleSetDocument();
                if (goalTemplateDataBean.getType().equals(HkSystemConstantUtil.GoalTemplateType.NUMERIC)) {
                    ruleSetDocument = ruleManagementTransformerBean.convertRuleSetDatabeanToRuleSetDocument(HkSystemConstantUtil.CREATE_OPERATION, goalTemplateDataBean.getRuleList(), ruleSetDocument);
                    ruleSetDocument.setId(null);
                    ruleSetDocument.setLastModifiedOn(new Date());
                    ruleSetDocument.setLastModifiedBy(loginDataBean.getId());
                }

                if (compareResult == 0) {
                    goalTemplateEntity.setFranchise(loginDataBean.getCompanyId());
//                    foundationService.saveGoalTemplate(goalTemplateEntity);
                    Long id = foundationService.saveGoalTemplateWithRules(goalTemplateEntity, ruleSetDocument);
                    this.createCustomField(id, goalTemplateDataBean);
                    return id;
                } else {
                    goalTemplateEntity.setId(null);
                    originalObject.setStatus(HkSystemConstantUtil.GoalTemplateStatus.PENDING);
                    goalTemplateEntity.setFranchise(loginDataBean.getCompanyId());
//                    return foundationService.saveGoalTemplate(goalTemplateEntity);
                    Long id = foundationService.saveGoalTemplateWithRules(goalTemplateEntity, ruleSetDocument);
                    this.createCustomField(id, goalTemplateDataBean);
                    return id;
                }
            }
        }
        return null;
    }

    public void createCustomField(Long id, GoalTemplateDataBean goalTemplateDataBean) {
        //Create custom field
        Map<Long, Map<String, Object>> val = new HashMap<>();
        val.put(id, goalTemplateDataBean.getGoalCustom());
        List<String> uiFieldList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(goalTemplateDataBean.getDbType())) {
            for (Map.Entry<String, String> entrySet : goalTemplateDataBean.getDbType().entrySet()) {
                uiFieldList.add(entrySet.getKey());
            }
        }
        Map<String, String> uiFieldMap = fieldService.retrieveUIFieldNameWithComponentTypes(uiFieldList);
        //Pass this map to makecustomfieldService
        List<CustomField> makeCustomField = customFieldSevice.makeCustomField(val, goalTemplateDataBean.getDbType(), uiFieldMap, HkSystemConstantUtil.FeatureNameForCustomField.GOAL.toString(), loginDataBean.getCompanyId(), id);
        //After that make Map of Section and there customfield
        Map<HkSystemConstantUtil.SectionNameForCustomField, List<CustomField>> map = new HashMap<>();
        map.put(HkSystemConstantUtil.SectionNameForCustomField.GENERAL, makeCustomField);
        //Pass this map to customFieldSevice.saveOrUpdate method.
        customFieldSevice.saveOrUpdate(id, HkSystemConstantUtil.FeatureNameForCustomField.GOAL, loginDataBean.getCompanyId(), map);
    }

    public HkGoalTemplateEntity convertGoalDataBeanToModel(GoalTemplateDataBean goalTemplateDataBean, HkGoalTemplateEntity goalTemplateEntity) {
        if (goalTemplateEntity == null) {
            goalTemplateEntity = new HkGoalTemplateEntity();
        }

        goalTemplateEntity.setId(goalTemplateDataBean.getId());
        goalTemplateEntity.setCreatedBy(loginDataBean.getId());
        goalTemplateEntity.setCreatedOn(new Date());
        goalTemplateEntity.setTemplateName(goalTemplateDataBean.getName());
        goalTemplateEntity.setTemplateType(goalTemplateDataBean.getType());
        goalTemplateEntity.setDescription(goalTemplateDataBean.getDescription());
        goalTemplateEntity.setPeriod(goalTemplateDataBean.getPeriod());
        goalTemplateEntity.setCopyOfTemplate(goalTemplateDataBean.getCopyOf());
        if (goalTemplateDataBean.getType().equals(HkSystemConstantUtil.GoalTemplateType.NUMERIC)) {
//            if (goalTemplateDataBean.isIsGenVal()) {
            goalTemplateEntity.setGeneralValidation(goalTemplateDataBean.getGenvaltype() + HkSystemConstantUtil.SEPARATOR_FOR_GOAL_TEMPLATE + goalTemplateDataBean.getGenvalvalue());
//            }
//            if (goalTemplateDataBean.isIsValGoalAchieved()) {
            goalTemplateEntity.setSuccessValue(goalTemplateDataBean.getValgoalachievedtype() + HkSystemConstantUtil.SEPARATOR_FOR_GOAL_TEMPLATE + goalTemplateDataBean.getValgoalachievedvalue());
//            }
//            if (goalTemplateDataBean.isIsValGoalNotAchieved()) {
            goalTemplateEntity.setFailureValue(goalTemplateDataBean.getValgoalnotachievedtype() + HkSystemConstantUtil.SEPARATOR_FOR_GOAL_TEMPLATE + goalTemplateDataBean.getValgoalnotachievedvalue());
//            }
        }
        goalTemplateEntity.setFranchise(goalTemplateDataBean.getFranchise());
        goalTemplateEntity.setForService(goalTemplateDataBean.getFor_service());
        goalTemplateEntity.setForDesignation(goalTemplateDataBean.getFor_designation());
        goalTemplateEntity.setForDepartment(goalTemplateDataBean.getFor_department());

        goalTemplateEntity.setLastModifiedBy(loginDataBean.getId());
        goalTemplateEntity.setLastModifiedOn(new Date());

        goalTemplateEntity.setStatus(goalTemplateDataBean.getStatus());

        return goalTemplateEntity;
    }

    public List<GoalTemplateDataBean> retrieveAllGoalTemplates() {
        List<HkGoalTemplateEntity> goalTemplates = foundationService.retrieveActiveGoalTemplatesByFranchise(loginDataBean.getCompanyId());
        List<GoalTemplateDataBean> result = new ArrayList<>();

        if (!CollectionUtils.isEmpty(goalTemplates)) {
            for (HkGoalTemplateEntity hkGoalTemplateEntity : goalTemplates) {
                GoalTemplateDataBean goalTemplateDataBean = this.convertGoalTemplateEntityToDataBean(hkGoalTemplateEntity, null);
                if (!StringUtils.isEmpty(hkGoalTemplateEntity.getRealizationRule())) {
                    ObjectId objectId = new ObjectId(hkGoalTemplateEntity.getRealizationRule());
                    RuleSetDataBean ruleList = ruleManagementTransformerBean.retrieveRuleById(objectId, loginDataBean.getCompanyId());
                    goalTemplateDataBean.setRuleList(ruleList);
                }
                if (hkGoalTemplateEntity.getForDepartment() != null) {
                    UMDepartment department = userManagementServiceWrapper.retrieveDepartment(hkGoalTemplateEntity.getForDepartment());
                    goalTemplateDataBean.setNameOfAssociation(department.getDeptName());
                } else if (hkGoalTemplateEntity.getForDesignation() != null) {
                    UMRole designation = userManagementServiceWrapper.retrieveDesignation(hkGoalTemplateEntity.getForDesignation());
                    goalTemplateDataBean.setNameOfAssociation(designation.getName());
                } 
                Map<HkSystemConstantUtil.SectionNameForCustomField, List<Map<Long, Map<String, Object>>>> customFields = customFieldTransformerBean.retrieveDocumentByInstanceId(hkGoalTemplateEntity.getId(), HkSystemConstantUtil.FeatureNameForCustomField.GOAL, loginDataBean.getCompanyId());
                if (!CollectionUtils.isEmpty(customFields)) {
                    if (customFields != null) {
                        List<Map<Long, Map<String, Object>>> maps = customFields.get(HkSystemConstantUtil.SectionNameForCustomField.GENERAL);
                        if (maps != null) {
                            for (Map<Long, Map<String, Object>> map : maps) {
                                goalTemplateDataBean.setGoalCustom(map.get(goalTemplateDataBean.getId()));
                            }
                        }
                    }
                }
                if (goalTemplateDataBean.getGoalCustom() == null) {
                    goalTemplateDataBean.setGoalCustom(new HashMap<String, Object>());
                }
                result.add(goalTemplateDataBean);
            }
        }
        return result;
    }

    public GoalTemplateDataBean convertGoalTemplateEntityToDataBean(HkGoalTemplateEntity goalTemplateEntity, GoalTemplateDataBean goalTemplateDataBean) {
        if (goalTemplateDataBean == null) {
            goalTemplateDataBean = new GoalTemplateDataBean();
        }
        goalTemplateDataBean.setId(goalTemplateEntity.getId());

        goalTemplateDataBean.setName(goalTemplateEntity.getTemplateName());
        goalTemplateDataBean.setType(goalTemplateEntity.getTemplateType());
        goalTemplateDataBean.setDescription(goalTemplateEntity.getDescription());
        goalTemplateDataBean.setPeriod(goalTemplateEntity.getPeriod());

        goalTemplateDataBean.setFor_department(goalTemplateEntity.getForDepartment());
        goalTemplateDataBean.setFor_designation(goalTemplateEntity.getForDesignation());
        goalTemplateDataBean.setFor_service(goalTemplateEntity.getForService());
        goalTemplateDataBean.setFranchise(goalTemplateEntity.getFranchise());
        //Set general validation section
        if (!StringUtils.isEmpty(goalTemplateEntity.getGeneralValidation())) {
            String[] splitData = goalTemplateEntity.getGeneralValidation().split(HkSystemConstantUtil.SEPARATOR_FOR_GOAL_TEMPLATE);
            goalTemplateDataBean.setGenvaltype(splitData[0]);
            goalTemplateDataBean.setGenvalvalue(splitData[1]);

            goalTemplateDataBean.setIsGenVal(Boolean.TRUE);
        }
        //Set if goal achieved is set
        if (!StringUtils.isEmpty(goalTemplateEntity.getSuccessValue())) {
            String[] splitData = goalTemplateEntity.getSuccessValue().split(HkSystemConstantUtil.SEPARATOR_FOR_GOAL_TEMPLATE);
            goalTemplateDataBean.setValgoalachievedtype(splitData[0]);
            goalTemplateDataBean.setValgoalachievedvalue(splitData[1]);

            goalTemplateDataBean.setIsValGoalAchieved(Boolean.TRUE);
        }
        //Set if goal not achieved is set
        if (!StringUtils.isEmpty(goalTemplateEntity.getFailureValue())) {
            String[] splitData = goalTemplateEntity.getFailureValue().split(HkSystemConstantUtil.SEPARATOR_FOR_GOAL_TEMPLATE);
            goalTemplateDataBean.setValgoalnotachievedtype(splitData[0]);
            goalTemplateDataBean.setValgoalnotachievedvalue(splitData[1]);

            goalTemplateDataBean.setIsValGoalNotAchieved(Boolean.TRUE);
        }

        goalTemplateDataBean.setStatus(goalTemplateEntity.getStatus());
        return goalTemplateDataBean;
    }

    public GoalTemplateDataBean retrieveGoalTemplateById(Long id) {
        HkGoalTemplateEntity goalTemplate = foundationService.retrieveGoalTemplateById(id);
        GoalTemplateDataBean goalTemplateDataBean = this.convertGoalTemplateEntityToDataBean(goalTemplate, null);

        if (!StringUtils.isEmpty(goalTemplate.getRealizationRule())) {
            ObjectId objectId = new ObjectId(goalTemplate.getRealizationRule());
            RuleSetDataBean ruleList = ruleManagementTransformerBean.retrieveRuleById(objectId, loginDataBean.getCompanyId());
            goalTemplateDataBean.setRuleList(ruleList);
        }
        Map<HkSystemConstantUtil.SectionNameForCustomField, List<Map<Long, Map<String, Object>>>> customFields = customFieldTransformerBean.retrieveDocumentByInstanceId(goalTemplate.getId(), HkSystemConstantUtil.FeatureNameForCustomField.GOAL, loginDataBean.getCompanyId());
        if (!CollectionUtils.isEmpty(customFields)) {
            if (customFields != null) {
                List<Map<Long, Map<String, Object>>> maps = customFields.get(HkSystemConstantUtil.SectionNameForCustomField.GENERAL);
                if (maps != null) {
                    for (Map<Long, Map<String, Object>> map : maps) {
                        goalTemplateDataBean.setGoalCustom(map.get(goalTemplateDataBean.getId()));
                    }
                }
            }
        }
        if (goalTemplateDataBean.getGoalCustom() == null) {
            goalTemplateDataBean.setGoalCustom(new HashMap<String, Object>());
        }
        return goalTemplateDataBean;
    }

    public List<GoalTemplateDataBean> retrieveAllGoalTemplatesBySearch(String query, List<String> status, Boolean applyLimit) {
        List<HkGoalTemplateEntity> goalTemplates = foundationService.searchGoalTemplateByName(query, loginDataBean.getCompanyId(), status, applyLimit);

        List<GoalTemplateDataBean> goalTemplateDataBeans = new ArrayList<>();

        if (!CollectionUtils.isEmpty(goalTemplates)) {
            for (HkGoalTemplateEntity hkGoalTemplateEntity : goalTemplates) {
                GoalTemplateDataBean goalTemplateDataBean = new GoalTemplateDataBean();
                goalTemplateDataBean = this.convertGoalTemplateEntityToDataBean(hkGoalTemplateEntity, goalTemplateDataBean);

                if (hkGoalTemplateEntity.getForDepartment() != null) {
                    UMDepartment department = userManagementServiceWrapper.retrieveDepartment(hkGoalTemplateEntity.getForDepartment());
                    goalTemplateDataBean.setNameOfAssociation(department.getDeptName());
                } else if (hkGoalTemplateEntity.getForDesignation() != null) {
                    UMRole designation = userManagementServiceWrapper.retrieveDesignation(hkGoalTemplateEntity.getForDesignation());
                    goalTemplateDataBean.setNameOfAssociation(designation.getName());
                }
                Map<HkSystemConstantUtil.SectionNameForCustomField, List<Map<Long, Map<String, Object>>>> customFields = customFieldTransformerBean.retrieveDocumentByInstanceId(hkGoalTemplateEntity.getId(), HkSystemConstantUtil.FeatureNameForCustomField.GOAL, loginDataBean.getCompanyId());
                if (!CollectionUtils.isEmpty(customFields)) {
                    if (customFields != null) {
                        List<Map<Long, Map<String, Object>>> maps = customFields.get(HkSystemConstantUtil.SectionNameForCustomField.GENERAL);
                        if (maps != null) {
                            for (Map<Long, Map<String, Object>> map : maps) {
                                goalTemplateDataBean.setGoalCustom(map.get(goalTemplateDataBean.getId()));
                            }
                        }
                    }
                }
                if (goalTemplateDataBean.getGoalCustom() == null) {
                    goalTemplateDataBean.setGoalCustom(new HashMap<String, Object>());
                }
                goalTemplateDataBeans.add(goalTemplateDataBean);
            }
        }

        return goalTemplateDataBeans;
    }

    public List<GoalTemplateDataBean> retrieveActiveGoalTemplatesByService(Long serviceId) {
        List<GoalTemplateDataBean> result = new ArrayList<>();
        if (serviceId != null) {

            List<HkGoalTemplateEntity> goalTemplates = foundationService.retrieveActiveGoalTemplatesByService(serviceId, null);
            if (!CollectionUtils.isEmpty(goalTemplates)) {
                for (HkGoalTemplateEntity hkGoalTemplateEntity : goalTemplates) {
                    GoalTemplateDataBean goalTemplateDataBean = new GoalTemplateDataBean();

                    goalTemplateDataBean = this.convertGoalTemplateEntityToDataBean(hkGoalTemplateEntity, goalTemplateDataBean);
                    if (hkGoalTemplateEntity.getForDepartment() != null) {
                        UMDepartment department = userManagementServiceWrapper.retrieveDepartment(hkGoalTemplateEntity.getForDepartment());
                        goalTemplateDataBean.setNameOfAssociation(department.getDeptName());
                    } else if (hkGoalTemplateEntity.getForDesignation() != null) {
                        UMRole designation = userManagementServiceWrapper.retrieveDesignation(hkGoalTemplateEntity.getForDesignation());
                        goalTemplateDataBean.setNameOfAssociation(designation.getName());
                    } 
                    Map<HkSystemConstantUtil.SectionNameForCustomField, List<Map<Long, Map<String, Object>>>> customFields = customFieldTransformerBean.retrieveDocumentByInstanceId(hkGoalTemplateEntity.getId(), HkSystemConstantUtil.FeatureNameForCustomField.GOAL, loginDataBean.getCompanyId());
                    if (!CollectionUtils.isEmpty(customFields)) {
                        if (customFields != null) {
                            List<Map<Long, Map<String, Object>>> maps = customFields.get(HkSystemConstantUtil.SectionNameForCustomField.GENERAL);
                            if (maps != null) {
                                for (Map<Long, Map<String, Object>> map : maps) {
                                    goalTemplateDataBean.setGoalCustom(map.get(goalTemplateDataBean.getId()));
                                }
                            }
                        }
                    }
                    if (goalTemplateDataBean.getGoalCustom() == null) {
                        goalTemplateDataBean.setGoalCustom(new HashMap<String, Object>());
                    }
                    result.add(goalTemplateDataBean);
                }
            }
        }
        return result;
    }

    public List<GoalTemplateDataBean> retrieveActiveGoalTemplatesByDepartment(Long departmentId) {
        List<GoalTemplateDataBean> result = new ArrayList<>();
        if (departmentId != null) {

            List<HkGoalTemplateEntity> goalTemplates = foundationService.retrieveActiveGoalTemplatesByDepartment(departmentId, null);
            if (!CollectionUtils.isEmpty(goalTemplates)) {
                for (HkGoalTemplateEntity hkGoalTemplateEntity : goalTemplates) {
                    GoalTemplateDataBean goalTemplateDataBean = new GoalTemplateDataBean();

                    goalTemplateDataBean = this.convertGoalTemplateEntityToDataBean(hkGoalTemplateEntity, goalTemplateDataBean);
                    if (hkGoalTemplateEntity.getForDepartment() != null) {
                        UMDepartment department = userManagementServiceWrapper.retrieveDepartment(hkGoalTemplateEntity.getForDepartment());
                        goalTemplateDataBean.setNameOfAssociation(department.getDeptName());
                    } else if (hkGoalTemplateEntity.getForDesignation() != null) {
                        UMRole designation = userManagementServiceWrapper.retrieveDesignation(hkGoalTemplateEntity.getForDesignation());
                        goalTemplateDataBean.setNameOfAssociation(designation.getName());
                    } 
                    Map<HkSystemConstantUtil.SectionNameForCustomField, List<Map<Long, Map<String, Object>>>> customFields = customFieldTransformerBean.retrieveDocumentByInstanceId(hkGoalTemplateEntity.getId(), HkSystemConstantUtil.FeatureNameForCustomField.GOAL, loginDataBean.getCompanyId());
                    if (!CollectionUtils.isEmpty(customFields)) {
                        if (customFields != null) {
                            List<Map<Long, Map<String, Object>>> maps = customFields.get(HkSystemConstantUtil.SectionNameForCustomField.GENERAL);
                            if (maps != null) {
                                for (Map<Long, Map<String, Object>> map : maps) {
                                    goalTemplateDataBean.setGoalCustom(map.get(goalTemplateDataBean.getId()));
                                }
                            }
                        }
                    }
                    if (goalTemplateDataBean.getGoalCustom() == null) {
                        goalTemplateDataBean.setGoalCustom(new HashMap<String, Object>());
                    }
                    result.add(goalTemplateDataBean);
                }
            }
        }
        return result;
    }

    public List<GoalTemplateDataBean> retrieveActiveGoalTemplatesByDesignation(Long designationId) {
        List<GoalTemplateDataBean> result = new ArrayList<>();
        if (designationId != null) {
            List<HkGoalTemplateEntity> goalTemplates = foundationService.retrieveActiveGoalTemplatesByDesignation(designationId, null);
            if (!CollectionUtils.isEmpty(goalTemplates)) {
                for (HkGoalTemplateEntity hkGoalTemplateEntity : goalTemplates) {
                    GoalTemplateDataBean goalTemplateDataBean = new GoalTemplateDataBean();

                    goalTemplateDataBean = this.convertGoalTemplateEntityToDataBean(hkGoalTemplateEntity, goalTemplateDataBean);
                    if (hkGoalTemplateEntity.getForDepartment() != null) {
                        UMDepartment department = userManagementServiceWrapper.retrieveDepartment(hkGoalTemplateEntity.getForDepartment());
                        goalTemplateDataBean.setNameOfAssociation(department.getDeptName());
                    } else if (hkGoalTemplateEntity.getForDesignation() != null) {
                        UMRole designation = userManagementServiceWrapper.retrieveDesignation(hkGoalTemplateEntity.getForDesignation());
                        goalTemplateDataBean.setNameOfAssociation(designation.getName());
                    } 
                    Map<HkSystemConstantUtil.SectionNameForCustomField, List<Map<Long, Map<String, Object>>>> customFields = customFieldTransformerBean.retrieveDocumentByInstanceId(hkGoalTemplateEntity.getId(), HkSystemConstantUtil.FeatureNameForCustomField.GOAL, loginDataBean.getCompanyId());
                    if (!CollectionUtils.isEmpty(customFields)) {
                        if (customFields != null) {
                            List<Map<Long, Map<String, Object>>> maps = customFields.get(HkSystemConstantUtil.SectionNameForCustomField.GENERAL);
                            if (maps != null) {
                                for (Map<Long, Map<String, Object>> map : maps) {
                                    goalTemplateDataBean.setGoalCustom(map.get(goalTemplateDataBean.getId()));
                                }
                            }
                        }
                    }
                    if (goalTemplateDataBean.getGoalCustom() == null) {
                        goalTemplateDataBean.setGoalCustom(new HashMap<String, Object>());
                    }
                    result.add(goalTemplateDataBean);
                }
            }
        }
        return result;
    }

    public void saveGoalTemplates(List<GoalTemplateDataBean> goalTemplateDataBeans) {
        List<HkGoalTemplateEntity> goalTemplateEntitys = new ArrayList<>();
        if (!CollectionUtils.isEmpty(goalTemplateDataBeans)) {
            for (GoalTemplateDataBean goalTemplateDataBean : goalTemplateDataBeans) {
                HkGoalTemplateEntity goalTemplateEntity = new HkGoalTemplateEntity();
                goalTemplateEntity = this.convertGoalDataBeanToModel(goalTemplateDataBean, null);
                HkRuleSetDocument ruleSetDocument = new HkRuleSetDocument();
                if (goalTemplateDataBean.getType().equals(HkSystemConstantUtil.GoalTemplateType.NUMERIC)) {
                    ruleSetDocument = ruleManagementTransformerBean.convertRuleSetDatabeanToRuleSetDocument(HkSystemConstantUtil.CREATE_OPERATION, goalTemplateDataBean.getRuleList(), ruleSetDocument);
                    for (HkRuleDocument hkRuleDocument : ruleSetDocument.getRules()) {
                        hkRuleDocument.setIsActive(Boolean.TRUE);
                        hkRuleDocument.setIsArchive(Boolean.FALSE);
                    }
                    ruleSetDocument.setId(null);
                    ruleSetDocument.setIsActive(Boolean.TRUE);
                    ruleSetDocument.setIsArchive(Boolean.FALSE);
                    ruleSetDocument.setFranchise(loginDataBean.getCompanyId());
                    ruleSetDocument.setLastModifiedOn(new Date());
                    ruleSetDocument.setLastModifiedBy(loginDataBean.getId());
                    ruleService.saveOrUpdateRuleSet(ruleSetDocument);
                    goalTemplateEntity.setRealizationRule(ruleSetDocument.getId().toString());
                }
                goalTemplateEntitys.add(goalTemplateEntity);
            }
            foundationService.updateGoalTemplates(goalTemplateEntitys);
        }
    }

    public void deleteAllActiveGoalTemplates(Map mapOfVals) {
        List<HkGoalTemplateEntity> goalTemplates = new ArrayList<>();
        if (mapOfVals.get("type").toString().equals(HkSystemConstantUtil.RecipientCodeType.DEPARTMENT)) {
            goalTemplates = foundationService.retrieveActiveGoalTemplatesByDepartment(Long.parseLong(mapOfVals.get("val").toString()), loginDataBean.getCompanyId());
        } else if (mapOfVals.get("type").toString().equals(HkSystemConstantUtil.RecipientCodeType.DESIGNATION)) {
            goalTemplates = foundationService.retrieveActiveGoalTemplatesByDesignation(Long.parseLong(mapOfVals.get("val").toString()), loginDataBean.getCompanyId());
        } else if (mapOfVals.get("type").toString().equals(HkSystemConstantUtil.RecipientCodeType.SERVICE)) {
            goalTemplates = foundationService.retrieveActiveGoalTemplatesByService(Long.parseLong(mapOfVals.get("val").toString()), loginDataBean.getCompanyId());
        }
        if (!CollectionUtils.isEmpty(goalTemplates)) {
            List<HkGoalTemplateEntity> toDelete = new ArrayList<>();
            for (HkGoalTemplateEntity hkGoalTemplateEntity : goalTemplates) {
                hkGoalTemplateEntity.setStatus(HkSystemConstantUtil.GoalTemplateStatus.DISCARDED);
                toDelete.add(hkGoalTemplateEntity);
            }
            foundationService.saveGoalTemplates(goalTemplates);
        }
    }

    public List<GoalSheetDataBean> retrieveUserGoalSheet(Long userId, List<String> type) {
        List<HkUserGoalStatusDocument> goalSheetDocuments = new ArrayList<>();
        if (userId == null) {
            goalSheetDocuments = goalService.retrieveGoalStatusByUserAndStatus(loginDataBean.getId(), type);
        } else {
            goalSheetDocuments = goalService.retrieveGoalStatusByUserAndStatus(userId, type);
        }
        List<GoalSheetDataBean> result = new ArrayList<>();

        if (!CollectionUtils.isEmpty(goalSheetDocuments)) {
            List<Long> activityGroupIds = new ArrayList<>();
            List<Long> goalTemplateIds = new ArrayList<>();
//            Map<Long, ActActivityFlowGroupEntity> mapOfIdToGroup = new HashMap<>();
//            Map<Long, ActActivityFlowNodeEntity> mapOfIdToNode = new HashMap<>();
            Map<Long, HkGoalTemplateEntity> mapOfIdToGoalTemplates = new HashMap<>();
            Map<Long, UMDepartment> mapOfIdToDepartment = new HashMap<>();
            Map<Long, UMRole> mapOfIdToDesignation = new HashMap<>();

            for (HkUserGoalStatusDocument hkUserGoalStatusDocument : goalSheetDocuments) {
                if (hkUserGoalStatusDocument.getActivityGroup() != null) {
                    activityGroupIds.add(hkUserGoalStatusDocument.getActivityGroup());
                    goalTemplateIds.add(hkUserGoalStatusDocument.getGoalTemplate());
                } else {
                    goalTemplateIds.add(hkUserGoalStatusDocument.getGoalTemplate());
                }
            }

//            if (!CollectionUtils.isEmpty(activityGroupIds)) {
//                List<ActActivityFlowGroupEntity> activityFlowGroups = activityFlowService.retrieveActivityFlowGroups(activityGroupIds);
//                if (!CollectionUtils.isEmpty(activityFlowGroups)) {
//                    for (ActActivityFlowGroupEntity actActivityFlowGroupEntity : activityFlowGroups) {
//
//                        mapOfIdToGroup.put(actActivityFlowGroupEntity.getId(), actActivityFlowGroupEntity);
//
//                        if (!CollectionUtils.isEmpty(actActivityFlowGroupEntity.getActivityFlowNodes())) {
//                            for (ActActivityFlowNodeEntity actActivityFlowNodeEntity : actActivityFlowGroupEntity.getActivityFlowNodes()) {
//                                mapOfIdToNode.put(actActivityFlowNodeEntity.getId(), actActivityFlowNodeEntity);
//                            }
//                        }
//                    }
//                }
//            } else {
//                //System.out.println("Activity Group ids are null");
//            }
            if (!CollectionUtils.isEmpty(goalTemplateIds)) {
                List<HkGoalTemplateEntity> goalTemplates = foundationService.retrieveGoalTemplates(goalTemplateIds);

                if (!CollectionUtils.isEmpty(goalTemplates)) {
                    List<Long> deptIds = new ArrayList<>();
                    List<Long> designationIds = new ArrayList<>();

                    for (HkGoalTemplateEntity hkGoalTemplateEntity : goalTemplates) {
                        mapOfIdToGoalTemplates.put(hkGoalTemplateEntity.getId(), hkGoalTemplateEntity);

                        if (hkGoalTemplateEntity.getForDepartment() != null) {
                            deptIds.add(hkGoalTemplateEntity.getForDepartment());
                        }
                        if (hkGoalTemplateEntity.getForDesignation() != null) {
                            designationIds.add(hkGoalTemplateEntity.getForDesignation());
                        }
                    }

                    if (!CollectionUtils.isEmpty(deptIds)) {
                        mapOfIdToDepartment = userManagementServiceWrapper.retrieveDepartmentMapByIds(deptIds, true);
                    }

                    if (!CollectionUtils.isEmpty(designationIds)) {
                        List<UMRole> roles = userManagementServiceWrapper.retrieveDesignationByIds(designationIds);
                        if (!CollectionUtils.isEmpty(roles)) {
                            for (UMRole uMRole : roles) {
                                mapOfIdToDesignation.put(uMRole.getId(), uMRole);
                            }
                        }
                    }

                }
            } else {
                //System.out.println("Goal template ids are null");
            }

            for (HkUserGoalStatusDocument hkUserGoalStatusDocument : goalSheetDocuments) {
                GoalSheetDataBean sheetDataBean = new GoalSheetDataBean();

                sheetDataBean.setId(hkUserGoalStatusDocument.getId().toString());
                sheetDataBean.setMinTarget(hkUserGoalStatusDocument.getMinTarget());
                sheetDataBean.setGoalName(mapOfIdToGoalTemplates.get(hkUserGoalStatusDocument.getGoalTemplate()).getTemplateName());
                sheetDataBean.setMaxTarget(hkUserGoalStatusDocument.getMaxTarget());
                sheetDataBean.setFromDate(hkUserGoalStatusDocument.getFromDate());
                sheetDataBean.setToDate(hkUserGoalStatusDocument.getToDate());
                sheetDataBean.setGoalAchieved(hkUserGoalStatusDocument.getGoalAchieved());
                sheetDataBean.setUser(hkUserGoalStatusDocument.getForUser());
                sheetDataBean.setGoalType(hkUserGoalStatusDocument.getGoalType());

                if (hkUserGoalStatusDocument.getActivityGroup() == null) {
                    HkGoalTemplateEntity goalTmplt = mapOfIdToGoalTemplates.get(hkUserGoalStatusDocument.getGoalTemplate());
                    if (goalTmplt != null) {
                        if (goalTmplt.getForDepartment() != null) {
                            UMDepartment dept = mapOfIdToDepartment.get(goalTmplt.getForDepartment());
                            sheetDataBean.setDepartment(dept.getDeptName());
                        }
                        if (goalTmplt.getForDesignation() != null) {
                            UMRole role = mapOfIdToDesignation.get(goalTmplt.getForDesignation());
                            sheetDataBean.setDesignation(role.getName());
                        }
                    }
                } 
//                else {
//                    ActActivityFlowGroupEntity group = mapOfIdToGroup.get(hkUserGoalStatusDocument.getActivityGroup());
//                    ActActivityFlowNodeEntity node = mapOfIdToNode.get(hkUserGoalStatusDocument.getActivityNode());
//
//                    sheetDataBean.setActivityGroup(group.getGroupName());
//                    sheetDataBean.setActivityNode(node.getAssociatedService().getServiceName());
//                }

                if (hkUserGoalStatusDocument.getStatus().equals(HkSystemConstantUtil.UserGoalTemplateStatus.SUBMITTED)) {
                    if (hkUserGoalStatusDocument.getGoalAchieved()) {
                        sheetDataBean.setStatus("Achieved");
                    } else {
                        sheetDataBean.setStatus("Not Achieved");
                    }
                } else if (hkUserGoalStatusDocument.getStatus().equals(HkSystemConstantUtil.UserGoalTemplateStatus.ATTENDED)) {
                    sheetDataBean.setStatus("Attended");
                }

                result.add(sheetDataBean);
            }
        } else {
            //System.out.println("GoalSheet is null");
        }

        return result;
    }

    public void submitGoalSheet(GoalSheetDataBean sheetDataBean) {
        List<HkUserGoalStatusDocument> userGoalStatus = goalService.retrieveGoalStatusByIds(Arrays.asList(new ObjectId(sheetDataBean.getId())));
        if (!CollectionUtils.isEmpty(userGoalStatus)) {
            HkUserGoalStatusDocument userGoal = userGoalStatus.get(0);

            userGoal.setTargetCount(sheetDataBean.getTarget());
            userGoal.setStatus(HkSystemConstantUtil.UserGoalTemplateStatus.SUBMITTED);
            userGoal.setGoalAchieved(sheetDataBean.isGoalAchieved());
            userGoal.setLastModifiedBy(loginDataBean.getId());
            userGoal.setLastModifiedOn(new Date());

            goalService.updateUserGoalStatusByGoalTemplates(Arrays.asList(userGoal));
        }
    }

    public List<GoalSheetDataBean> retrieveSubmittedUserGoalSheet(Long userId, List<String> status, Date fromDate, Date toDate) {
        List<HkUserGoalStatusDocument> goalSheetDocuments = goalService.retrieveGoalStatusByUserAndDateRange(userId, status, fromDate, toDate);
        List<GoalSheetDataBean> result = new ArrayList<>();

        if (!CollectionUtils.isEmpty(goalSheetDocuments)) {
            List<Long> activityGroupIds = new ArrayList<>();
            List<Long> goalTemplateIds = new ArrayList<>();
//            Map<Long, ActActivityFlowGroupEntity> mapOfIdToGroup = new HashMap<>();
//            Map<Long, ActActivityFlowNodeEntity> mapOfIdToNode = new HashMap<>();
            Map<Long, HkGoalTemplateEntity> mapOfIdToGoalTemplates = new HashMap<>();
            Map<Long, UMDepartment> mapOfIdToDepartment = new HashMap<>();
            Map<Long, UMRole> mapOfIdToDesignation = new HashMap<>();

            for (HkUserGoalStatusDocument hkUserGoalStatusDocument : goalSheetDocuments) {
                if (hkUserGoalStatusDocument.getActivityGroup() != null) {
                    activityGroupIds.add(hkUserGoalStatusDocument.getActivityGroup());
                    goalTemplateIds.add(hkUserGoalStatusDocument.getGoalTemplate());
                } else {
                    goalTemplateIds.add(hkUserGoalStatusDocument.getGoalTemplate());
                }
            }

//            if (!CollectionUtils.isEmpty(activityGroupIds)) {
//                List<ActActivityFlowGroupEntity> activityFlowGroups = activityFlowService.retrieveActivityFlowGroups(activityGroupIds);
//                if (!CollectionUtils.isEmpty(activityFlowGroups)) {
//                    for (ActActivityFlowGroupEntity actActivityFlowGroupEntity : activityFlowGroups) {
//
//                        mapOfIdToGroup.put(actActivityFlowGroupEntity.getId(), actActivityFlowGroupEntity);
//
//                        if (!CollectionUtils.isEmpty(actActivityFlowGroupEntity.getActivityFlowNodes())) {
//                            for (ActActivityFlowNodeEntity actActivityFlowNodeEntity : actActivityFlowGroupEntity.getActivityFlowNodes()) {
//                                mapOfIdToNode.put(actActivityFlowNodeEntity.getId(), actActivityFlowNodeEntity);
//                            }
//                        }
//                    }
//                }
//            } else {
//                //System.out.println("Activity Group ids are null");
//            }
            if (!CollectionUtils.isEmpty(goalTemplateIds)) {
                List<HkGoalTemplateEntity> goalTemplates = foundationService.retrieveGoalTemplates(goalTemplateIds);

                if (!CollectionUtils.isEmpty(goalTemplates)) {
                    List<Long> deptIds = new ArrayList<>();
                    List<Long> designationIds = new ArrayList<>();

                    for (HkGoalTemplateEntity hkGoalTemplateEntity : goalTemplates) {
                        mapOfIdToGoalTemplates.put(hkGoalTemplateEntity.getId(), hkGoalTemplateEntity);

                        if (hkGoalTemplateEntity.getForDepartment() != null) {
                            deptIds.add(hkGoalTemplateEntity.getForDepartment());
                        }
                        if (hkGoalTemplateEntity.getForDesignation() != null) {
                            designationIds.add(hkGoalTemplateEntity.getForDesignation());
                        }
                    }

                    if (!CollectionUtils.isEmpty(deptIds)) {
                        mapOfIdToDepartment = userManagementServiceWrapper.retrieveDepartmentMapByIds(deptIds, true);
                    }

                    if (!CollectionUtils.isEmpty(designationIds)) {
                        List<UMRole> roles = userManagementServiceWrapper.retrieveDesignationByIds(designationIds);
                        if (!CollectionUtils.isEmpty(roles)) {
                            for (UMRole uMRole : roles) {
                                mapOfIdToDesignation.put(uMRole.getId(), uMRole);
                            }
                        }
                    }

                }
            } else {
                //System.out.println("Goal template ids are null");
            }

            for (HkUserGoalStatusDocument hkUserGoalStatusDocument : goalSheetDocuments) {
                GoalSheetDataBean sheetDataBean = new GoalSheetDataBean();

                sheetDataBean.setId(hkUserGoalStatusDocument.getId().toString());
                sheetDataBean.setMinTarget(hkUserGoalStatusDocument.getMinTarget());
                sheetDataBean.setGoalName(mapOfIdToGoalTemplates.get(hkUserGoalStatusDocument.getGoalTemplate()).getTemplateName());
                sheetDataBean.setMaxTarget(hkUserGoalStatusDocument.getMaxTarget());
                sheetDataBean.setFromDate(hkUserGoalStatusDocument.getFromDate());
                sheetDataBean.setToDate(hkUserGoalStatusDocument.getToDate());
                sheetDataBean.setGoalAchieved(hkUserGoalStatusDocument.getGoalAchieved());
                sheetDataBean.setUser(hkUserGoalStatusDocument.getForUser());
                sheetDataBean.setGoalType(hkUserGoalStatusDocument.getGoalType());
                sheetDataBean.setRealizedCount(hkUserGoalStatusDocument.getRealizedCount());

                if (hkUserGoalStatusDocument.getActivityGroup() == null) {
                    HkGoalTemplateEntity goalTmplt = mapOfIdToGoalTemplates.get(hkUserGoalStatusDocument.getGoalTemplate());
                    if (goalTmplt != null) {
                        if (goalTmplt.getForDepartment() != null) {
                            UMDepartment dept = mapOfIdToDepartment.get(goalTmplt.getForDepartment());
                            sheetDataBean.setDepartment(dept.getDeptName());
                        }
                        if (goalTmplt.getForDesignation() != null) {
                            UMRole role = mapOfIdToDesignation.get(goalTmplt.getForDesignation());
                            sheetDataBean.setDesignation(role.getName());
                        }
                    }
                } 
//                else {
////                    ActActivityFlowGroupEntity group = mapOfIdToGroup.get(hkUserGoalStatusDocument.getActivityGroup());
////                    ActActivityFlowNodeEntity node = mapOfIdToNode.get(hkUserGoalStatusDocument.getActivityNode());
//
//                    sheetDataBean.setActivityGroup(group.getGroupName());
//                    sheetDataBean.setActivityNode(node.getAssociatedService().getServiceName());
//                }

                if (hkUserGoalStatusDocument.getStatus().equals(HkSystemConstantUtil.UserGoalTemplateStatus.SUBMITTED)) {
                    if (hkUserGoalStatusDocument.getGoalAchieved()) {
                        sheetDataBean.setStatus("Achieved");
                    } else {
                        sheetDataBean.setStatus("Not Achieved");
                    }
                } else if (hkUserGoalStatusDocument.getStatus().equals(HkSystemConstantUtil.UserGoalTemplateStatus.ATTENDED)) {
                    sheetDataBean.setStatus("Attended");
                }

                result.add(sheetDataBean);
            }
        } else {
            //System.out.println("GoalSheet is null");
        }
        return result;
    }

    public Set<SelectItem> retrieveUsersForGoalSheet() {
        Set<SelectItem> result = new HashSet<>();
        List<HkGoalPermissionEntity> activetemplates = foundationService.retrieveGoalPermissions("GS", loginDataBean.getRoleIds().get(0));
        if (!CollectionUtils.isEmpty(activetemplates)) {

            List<Long> designationIds = new ArrayList<>();
            List<Long> departmentIds = new ArrayList<>();
            List<Long> nodeIds = new ArrayList<>();
            List<Long> userids = new ArrayList<>();
            for (HkGoalPermissionEntity hkGoalTemplateEntity : activetemplates) {

                if (hkGoalTemplateEntity.getReferenceType().equals(HkSystemConstantUtil.RecipientCodeType.DESIGNATION)) {
                    designationIds.add(hkGoalTemplateEntity.getReferenceInstance());
                }
                if (hkGoalTemplateEntity.getReferenceType().equals(HkSystemConstantUtil.RecipientCodeType.DEPARTMENT)) {
                    departmentIds.add(hkGoalTemplateEntity.getReferenceInstance());
                }
                if (hkGoalTemplateEntity.getReferenceType().equals(HkSystemConstantUtil.RecipientCodeType.SERVICE)) {
                    nodeIds.add(hkGoalTemplateEntity.getReferenceInstance());
                }
            }
            if (!CollectionUtils.isEmpty(designationIds)) {
                List<UMUserRole> userRoles = userManagementServiceWrapper.retrieveUserRolesByRoleIds(designationIds, true);
                if (!CollectionUtils.isEmpty(userRoles)) {
                    for (UMUserRole uMUserRole : userRoles) {
                        if (userids.indexOf(uMUserRole.getUserId()) == -1) {
                            SelectItem selectItem = new SelectItem(null, uMUserRole.getUserId(), null, uMUserRole.getuMUser().getFirstName() + " " + uMUserRole.getuMUser().getLastName());
                            result.add(selectItem);
                            userids.add(uMUserRole.getUserId());
                        }
                    }
                }
            } else {
                //System.out.println("designation ids are null");
            }
            if (!CollectionUtils.isEmpty(departmentIds)) {
                List<UMUser> usersInDept = userManagementServiceWrapper.retrieveUsersByCompanyIdsByDepartment(null, departmentIds, false, null);
                if (!CollectionUtils.isEmpty(usersInDept)) {
                    for (UMUser uMUser : usersInDept) {
                        if (userids.indexOf(uMUser.getId()) == -1) {
                            SelectItem selectItem = new SelectItem(null, uMUser.getId(), null, uMUser.getFirstName() + " " + uMUser.getLastName());
                            result.add(selectItem);
                            userids.add(uMUser.getId());
                        }
                    }
                }
            } else {
                //System.out.println("department ids are null");
            }
//            if (!CollectionUtils.isEmpty(nodeIds)) {
//                List<ActActivityFlowNodeEntity> flowNodes = activityFlowService.retrieveActivityFlowNodes(nodeIds);
//                if (!CollectionUtils.isEmpty(flowNodes)) {
//                    List<Long> ids = new ArrayList<>();
//                    for (ActActivityFlowNodeEntity node : flowNodes) {
//                        ids.add(node.getCustom1());
//                    }
//
//                    if (!CollectionUtils.isEmpty(ids)) {
//                        List<UMUserRole> userRoles = userManagementServiceWrapper.retrieveUserRolesByRoleIds(ids, true);
//                        if (!CollectionUtils.isEmpty(userRoles)) {
//                            for (UMUserRole uMUserRole : userRoles) {
//                                if (userids.indexOf(uMUserRole.getUserId()) == -1) {
//                                    SelectItem selectItem = new SelectItem(null, uMUserRole.getUserId(), null, uMUserRole.getuMUser().getFirstName() + " " + uMUserRole.getuMUser().getLastName());
//                                    result.add(selectItem);
//                                    userids.add(uMUserRole.getUserId());
//                                }
//                            }
//                        }
//                    } else {
//                        //System.out.println("ids is null");
//                    }
//                } else {
//                    //System.out.println("flow nodes is null");
//                }
//            } else {
//                //System.out.println("node ids are null");
//            }
        } else {
            //System.out.println("active templates is null");
        }
        return result;
    }

}
