package com.argusoft.hkg.web.reportbuilder.transformers;

import com.argusoft.generic.database.exception.GenericDatabaseException;
import com.argusoft.hkg.common.constantutil.HkSystemConstantUtil;
import com.argusoft.hkg.common.functionutil.FolderManagement;
import static com.argusoft.hkg.common.functionutil.FolderManagement.TEMP;
import static com.argusoft.hkg.common.functionutil.FolderManagement.checkIsExists;
import com.argusoft.hkg.core.HkFieldService;
import com.argusoft.hkg.core.HkFoundationService;
import com.argusoft.hkg.core.HkNotificationService;
import com.argusoft.hkg.model.HkFieldEntity;
import com.argusoft.hkg.model.HkNotificationConfigurationEntity;
import com.argusoft.hkg.model.HkSystemConfigurationEntity;
import com.argusoft.hkg.model.HkValueEntity;
import com.argusoft.hkg.nosql.core.HkCustomFieldService;
import com.argusoft.hkg.nosql.core.HkRuleService;
import com.argusoft.hkg.nosql.core.HkStockService;
import com.argusoft.hkg.nosql.model.HkLotDocument;
import com.argusoft.hkg.nosql.model.HkPacketDocument;
import com.argusoft.hkg.nosql.model.HkRuleCriteriaDocument;
import com.argusoft.hkg.nosql.model.HkRuleDocument;
import com.argusoft.hkg.nosql.model.HkRuleSetDocument;
import com.argusoft.hkg.web.comparator.FeatureDataBeanComparator;
import com.argusoft.hkg.web.internationalization.databeans.LanguageDataBean;
import com.argusoft.hkg.web.internationalization.databeans.LocaleDataBean;
import com.argusoft.hkg.web.internationalization.databeans.SearchBeanForLabel;
import com.argusoft.hkg.web.internationalization.transformers.LocalesTransformerBean;
import com.argusoft.hkg.web.reportbuilder.databeans.RbUserDataBean;
import static com.argusoft.hkg.web.reportbuilder.transformers.ReportBuilderTemplate.contentRightAlignStyle;
import static com.argusoft.hkg.web.reportbuilder.transformers.ReportBuilderTemplate.rootStyle;
import com.argusoft.hkg.web.usermanagement.databeans.FeatureDataBean;
import com.argusoft.hkg.web.usermanagement.databeans.FranchiseDataBean;
import com.argusoft.hkg.web.usermanagement.transformers.FranchiseTransformerBean;
import com.argusoft.hkg.web.usermanagement.wrapper.UserManagementServiceWrapper;
import com.argusoft.hkg.web.util.ApplicationMasterInitializer;
import com.argusoft.reportbuilder.common.constantutil.RbReportConstantUtils;
import com.argusoft.reportbuilder.core.RbReportService;
import com.argusoft.reportbuilder.core.bean.MasterReportDataBean;
import com.argusoft.reportbuilder.core.bean.RbFieldDataBean;
import com.argusoft.reportbuilder.model.RbReport;
import com.argusoft.reportbuilder.model.RbReportField;
import com.argusoft.usermanagement.common.core.UMRoleService;
import com.argusoft.usermanagement.common.core.UMUserService;
import com.argusoft.usermanagement.common.model.UMFeature;
import com.argusoft.usermanagement.common.model.UMRole;
import com.argusoft.usermanagement.common.model.UMUser;
import com.argusoft.usermanagement.common.model.UMUserRole;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.ParseException;
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
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.toList;
import java.util.stream.Stream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.jasper.builder.export.JasperXlsExporterBuilder;
import net.sf.dynamicreports.jasper.builder.export.JasperXmlExporterBuilder;
import net.sf.dynamicreports.report.base.expression.AbstractSimpleExpression;
import static net.sf.dynamicreports.report.builder.DynamicReports.col;
import static net.sf.dynamicreports.report.builder.DynamicReports.export;
import static net.sf.dynamicreports.report.builder.DynamicReports.grid;
import static net.sf.dynamicreports.report.builder.DynamicReports.grp;
import static net.sf.dynamicreports.report.builder.DynamicReports.report;
import static net.sf.dynamicreports.report.builder.DynamicReports.sbt;
import static net.sf.dynamicreports.report.builder.DynamicReports.stl;
import static net.sf.dynamicreports.report.builder.DynamicReports.type;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.builder.grid.ColumnGridComponentBuilder;
import net.sf.dynamicreports.report.builder.grid.ColumnTitleGroupBuilder;
import net.sf.dynamicreports.report.builder.group.ColumnGroupBuilder;
import net.sf.dynamicreports.report.builder.style.ConditionalStyleBuilder;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.builder.subtotal.AggregationSubtotalBuilder;
import net.sf.dynamicreports.report.constant.GroupHeaderLayout;
import net.sf.dynamicreports.report.constant.HorizontalAlignment;
import net.sf.dynamicreports.report.constant.PageOrientation;
import net.sf.dynamicreports.report.constant.PageType;
import net.sf.dynamicreports.report.constant.SplitType;
import net.sf.dynamicreports.report.constant.VerticalAlignment;
import net.sf.dynamicreports.report.datasource.DRDataSource;
import net.sf.dynamicreports.report.definition.ReportParameters;
import net.sf.dynamicreports.report.exception.DRException;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import org.bson.types.ObjectId;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 * Class only used to generate reports for scheduled users.
 * @author gautam
 */
@Service
public class SchedulerReportTransformerBean {

    private RbUserDataBean userDataBean;

    @Autowired
    RbReportService rbReportService;
    
    @Autowired
    private HkFieldService fieldService;

    @Autowired
    private HkFoundationService hkFoundationService;
    
    @Autowired
    UMRoleService roleService;
    
    @Autowired
    UMUserService userService;
    
    @Autowired
    HkNotificationService hkNotificationService;
    
    @Autowired
    HkStockService hkStockService;

    @Autowired
    HkRuleService ruleService;

    @Autowired
    HkCustomFieldService customFieldSevice;

    @Autowired
    UserManagementServiceWrapper userManagementServiceWrapper;

    @Autowired
    FranchiseTransformerBean franchiseTransformerBean;

    @Autowired
    LocalesTransformerBean localesTransformerBean;
    
    //Initialize user data bean with corresponding value of user.
    //Used as a normal databean to store values of user for further use.
    public void initUserDataBean(UMUser user) {
        if (user != null) {
            userDataBean = new RbUserDataBean();
            int precedence = 3;
            Set<UMUserRole> userRoleSet = user.getUMUserRoleSet();
            List<Long> roles = new ArrayList<>();
            Boolean isHKAdmin = Boolean.FALSE;
            Boolean isFranchiseAdmin = Boolean.FALSE;
            for (UMUserRole uMUserRole : userRoleSet) {
                if (ApplicationMasterInitializer.getHkAdminRole().equals(uMUserRole.getuMUserRolePK().getRole())) {
                    isHKAdmin = Boolean.TRUE;
                }
                if (uMUserRole.getIsActive() && !uMUserRole.getIsArchive()) {
                    roles.add(uMUserRole.getuMUserRolePK().getRole());
                }
            }

            userDataBean.setDepartment(user.getDepartment());
            userDataBean.setFirstName(user.getFirstName());
            userDataBean.setLastName(user.getLastName());
            userDataBean.setUserCode(user.getUserCode());
            userDataBean.setServerDate(new Date());
            userDataBean.setId(user.getId());
            userDataBean.setEncryptedPassword(user.getPassword());
            userDataBean.setUserId(user.getUserId());
            userDataBean.setCompanyId(user.getCompany());

            userDataBean.setIsHKAdmin(isHKAdmin);
            userDataBean.setIsFranchiseAdmin(isFranchiseAdmin);
            userDataBean.setPrecedence(precedence);
            userDataBean.setServerOffsetInMin(Calendar.getInstance().getTimeZone().getRawOffset() / (1000 * 60));
            userDataBean.setRoleIds(roles);
            String theme = user.getContact().getFacebookPage();
            userDataBean.setHasBg(false);
            if (theme != null && theme.length() > 0) {
                String[] split = theme.split(",");
                if (split[0].indexOf(FolderManagement.UNIQUE_SEPARATOR) == -1) {
                    userDataBean.setTheme(split[0]);
                } else {
                    userDataBean.setTheme("default");
                }
                for (String split1 : split) {
                    if (split1.indexOf(FolderManagement.UNIQUE_SEPARATOR) != -1) {
                        userDataBean.setHasBg(true);
                    }
                }
            } else {
                userDataBean.setTheme("default");
            }
            if (StringUtils.hasText(user.getPreferredLanguage())) {
                userDataBean.setPrefferedLang(user.getPreferredLanguage());
            }
            userDataBean.setFeatures(this.generateMenuByRoles(userDataBean.getRoleIds(), userDataBean.isIsCompanyActivated()));
        }
    }
    
    public Map<Long, List<FeatureDataBean>> initRoleFeaturesMap() {
        //  Initialize this map from User Management service
        Map<UMRole, Set<UMFeature>> mapRoleFeature = null;
        try {
            mapRoleFeature = roleService.retrieveRoleFeaturesForMenu();
        } catch (GenericDatabaseException ex) {
            Logger.getLogger(SchedulerReportTransformerBean.class.getName()).log(Level.SEVERE, null, ex);
        }

        //Prepare set of default features, we want to give access....
        List<String> defaultFeatures = Arrays.asList(HkSystemConstantUtil.Feature.MANAGE_HOLIDAY, HkSystemConstantUtil.Feature.MANAGE_TASK, HkSystemConstantUtil.Feature.MANAGE_NOTIFICATION, HkSystemConstantUtil.Feature.APPLY_LEAVE, HkSystemConstantUtil.Feature.MANAGE_MESSAGE, HkSystemConstantUtil.Feature.MANAGE_EVENT);
        List<FeatureDataBean> defaultFeatureDataBeanList = new ArrayList<>();
        List<UMFeature> defaultFeatureList = userManagementServiceWrapper.retrieveFeatureByName(defaultFeatures);

        if (!CollectionUtils.isEmpty(defaultFeatureList)) {
            for (UMFeature uMFeature : defaultFeatureList) {
                FeatureDataBean featureDataBean = buildFeatureDataBeanTree(uMFeature, null, true);
                defaultFeatureDataBeanList.add(featureDataBean);
            }
        }

        Map<Long, List<FeatureDataBean>> mapRoleFeatures = new HashMap<>();        
        if (!CollectionUtils.isEmpty(mapRoleFeature)) {
            for (Map.Entry<UMRole, Set<UMFeature>> entry : mapRoleFeature.entrySet()) {

                UMRole role = entry.getKey();
                //    LOGGER.trace(" ROLE "+role);
                Set<UMFeature> featureSet = entry.getValue();
                List<FeatureDataBean> featureDatabeanList = new ArrayList<>();
                for (UMFeature uMFeature : featureSet) {
                    //  	LOGGER.trace(" feature "+uMFeature);
                    FeatureDataBean featureDataBean = buildFeatureDataBeanTree(uMFeature, null, false);
                    featureDatabeanList.add(featureDataBean);
                }

                if (!role.getName().equalsIgnoreCase(HkSystemConstantUtil.ROLE.SUPER_ADMIN)) {
                    featureDatabeanList.addAll(defaultFeatureDataBeanList);
                }

                mapRoleFeatures.put(role.getId(), featureDatabeanList);
            }
        }

        return mapRoleFeatures;
    }
    
    //Retrieve features available to current user.
    public List<FeatureDataBean> generateMenuByRoles(List<Long> roleIds, boolean isCompanyActivated) {
        Map<Long, List<FeatureDataBean>> mapRoleFeatures = this.initRoleFeaturesMap();
        Set<FeatureDataBean> features = new HashSet<>();
        for (Long role : mapRoleFeatures.keySet()) {
            if (roleIds.contains(role)) {
                features.addAll(mapRoleFeatures.get(role));
            }
        }
        return this.sortMenuBasedOnSeqNo(new ArrayList<>(features));
    }

    public List<FeatureDataBean> sortMenuBasedOnSeqNo(List<FeatureDataBean> featureDatabeans) {
        if (!CollectionUtils.isEmpty(featureDatabeans)) {
            Collections.sort(featureDatabeans, FeatureDataBeanComparator.getComparator(FeatureDataBeanComparator.SORT_SEQ_NO));
            for (FeatureDataBean featureDatabean : featureDatabeans) {
                if (featureDatabean.getChildren() != null) {
                    featureDatabean.setChildren(sortMenuBasedOnSeqNo(featureDatabean.getChildren()));
                }
            }
        }
        return featureDatabeans;
    }

    private FeatureDataBean buildFeatureDataBeanTree(UMFeature feature, FeatureDataBean featureDataBean, boolean defaultFeature) {
        featureDataBean = this.convertFeatureToFeatureDatabean(feature, featureDataBean);
        //Commented by Mayank 16-Sep-2014 No need child access while assigning parent features
        if (!defaultFeature) {
            if (!CollectionUtils.isEmpty(feature.getuMFeatureChildSet())) {
                for (UMFeature childFeature : feature.getuMFeatureChildSet()) {
                    FeatureDataBean childFeatureDataBean = buildFeatureDataBeanTree(childFeature, null, defaultFeature);
                    List<FeatureDataBean> children = featureDataBean.getChildren();
                    if (children == null) {
                        children = new ArrayList<>();
                        featureDataBean.setChildren(children);
                    }
                    children.add(childFeatureDataBean);
                }
            }
        }
        return featureDataBean;
    }
    
    public FeatureDataBean convertFeatureToFeatureDatabean(UMFeature feature, FeatureDataBean hkFeatureDatabean) {
        if (hkFeatureDatabean == null) {
            hkFeatureDatabean = new FeatureDataBean();
        }
        hkFeatureDatabean.setId(feature.getId());
        hkFeatureDatabean.setFeatureName(feature.getName());
        hkFeatureDatabean.setDisplayName(feature.getName());
        hkFeatureDatabean.setMenuLabel(feature.getMenuLabel());
        hkFeatureDatabean.setIsCrud(feature.getIsCrud());
        hkFeatureDatabean.setDescription(feature.getDescription());
        hkFeatureDatabean.setFeatureURL(feature.getFeatureUrl());
        hkFeatureDatabean.setMenuLabel(feature.getMenuLabel());
        hkFeatureDatabean.setPrecedence(feature.getPrecedence());
        if (feature.getParent() == null) {
            hkFeatureDatabean.setParentId(0L);
            hkFeatureDatabean.setParentName("Root");
        } else {
            hkFeatureDatabean.setParentId(feature.getParent().getId());
            hkFeatureDatabean.setParentName(feature.getParent().getName());
        }
        hkFeatureDatabean.setMenuType(feature.getMenuType());
        hkFeatureDatabean.setSeqNo(feature.getSeqNo());
        hkFeatureDatabean.setIsActive(feature.getIsActive());
        hkFeatureDatabean.setWebserviceUrl(feature.getWebserviceUrl());
        hkFeatureDatabean.setMenuCategory(feature.getMenuCategory());
        return hkFeatureDatabean;
    }
    
    public String prepareUserMultiSelectString(String[] values, Map<String, String> userCodeNameMap) {
        String finalValue = "";
        Map<String, String> recipientMap = new HashMap<>();
        if (values != null && values.length > 0) {
            finalValue = values.toString();
            for (String recipientCode : values) {
                if (!StringUtils.isEmpty(recipientCode)) {
                    if (!CollectionUtils.isEmpty(userCodeNameMap)) {
                        if (userCodeNameMap.containsKey(recipientCode)) {
                            String recipientType = recipientCode.split(":")[1];
                            String recipients = recipientMap.get(HkSystemConstantUtil.RECIPIENT_TEXT.get(recipientType));
                            if (!StringUtils.isEmpty(recipients)) {
                                recipients += ", " + userCodeNameMap.get(recipientCode);
                            } else {
                                recipients = userCodeNameMap.get(recipientCode);
                            }
                            recipientMap.put(HkSystemConstantUtil.RECIPIENT_TEXT.get(recipientType), recipients);
                        }
                    }
                }
            }
        }

        if (!recipientMap.isEmpty()) {
            finalValue = "";
            for (String recipientType : recipientMap.keySet()) {
                finalValue += recipientType + ": " + recipientMap.get(recipientType) + "; ";
            }
            finalValue = finalValue.substring(0, finalValue.length() - 2);
        }

        return finalValue;
    }

    public String prepareRuleSetString(List<Map<String, Object>> values) {
        String ruleStrng = null;
        if (!CollectionUtils.isEmpty(values)) {
            int ruleIndex = 0;
            for (Map<String, Object> value : values) {
                ruleIndex++;
                if (ruleStrng == null) {
                    ruleStrng = new String();
                }
                String connector = null;
                if (value.get("ruleName") != null) {
                    ruleStrng = ruleStrng + value.get("ruleName") + " - ";
                }
                if (value.containsKey("apply")) {
                    connector = value.get("apply").toString().equalsIgnoreCase("All") ? "And" : "Or";
                }
                if (value.get("criterias") != null) {
                    List<Map<String, Object>> criterias = (List<Map<String, Object>>) value.get("criterias");
                    if (!CollectionUtils.isEmpty(criterias)) {
                        if (value.get("ruleName") == null) {
                            ruleStrng = ruleStrng + "Criterias" + " - ";
                        }
                        int index = 0;
                        for (Map<String, Object> criteriaObj : criterias) {
                            index++;
                            if (criteriaObj.containsKey("entityName")) {
                                ruleStrng = ruleStrng + criteriaObj.get("entityName").toString() + ".";
                                if (criteriaObj.containsKey("field")) {
                                    Double fieldIdDouble = Double.parseDouble(criteriaObj.get("field").toString());
                                    Long fieldId = fieldIdDouble.longValue();
                                    HkFieldEntity fieldEntity = fieldService.retrieveCustomFieldByFieldId(fieldId);
                                    if (fieldEntity != null) {
                                        ruleStrng = ruleStrng + fieldEntity.getFieldLabel() + " ";
                                    }
                                    if (criteriaObj.get("operator") != null) {
                                        ruleStrng = ruleStrng + criteriaObj.get("operator").toString() + " ";
                                    }
                                    if (criteriaObj.get("value") != null) {
                                        ruleStrng = ruleStrng + criteriaObj.get("value").toString() + " ";
                                    }
                                    if (connector != null && index < criterias.size()) {
                                        ruleStrng += connector + " ";
                                    }
                                    if (index == criterias.size() && ruleIndex < values.size()) {
                                        ruleStrng += ", ";
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return ruleStrng;
    }

    public String createWebEmailMessage(HkNotificationConfigurationEntity configurationEntity) {
        String webEmailMessage = "";
        if (configurationEntity != null) {
            if (!StringUtils.isEmpty(configurationEntity.getWebMessage())) {
                webEmailMessage = "Web - " + configurationEntity.getWebMessage();
            } else {
                webEmailMessage = "Web - N/A ";
            }
            if (!StringUtils.isEmpty(webEmailMessage)) {
                if (!StringUtils.isEmpty(configurationEntity.getEmailMessage())) {
                    webEmailMessage = webEmailMessage + " , " + "Email - " + configurationEntity.getEmailMessage();
                } else {
                    webEmailMessage = webEmailMessage + " , " + "Email - N/A ";
                }
            } else {
                if (!StringUtils.isEmpty(configurationEntity.getEmailMessage())) {
                    webEmailMessage = "Email - " + configurationEntity.getEmailMessage();
                } else {
                    webEmailMessage = "Email - N/A ";
                }
            }
            return webEmailMessage;
        } else {
            webEmailMessage = "Web - N/A , Email - N/A";
            return webEmailMessage;
        }
    }

    private String createStringForNotificationConfig(Long notificationId) {
        if (notificationId != null) {
            HkNotificationConfigurationEntity notificationConfig = hkNotificationService.retrieveNotificationCnfigurationById(notificationId);
            String result = null;
            if (notificationConfig.getNotificationType().equals(HkSystemConstantUtil.NotificationConfigurationType.TIME_BASED)) {
                Calendar atTime = Calendar.getInstance();
                atTime.setTime(notificationConfig.getAtTime());
                String atTimeString = atTime.get(Calendar.HOUR_OF_DAY) + ":" + atTime.get(Calendar.MINUTE);
                String repetativeMode = HkSystemConstantUtil.TASK_REPETITIVE_MODE_MAP.get(notificationConfig.getRepeatativeMode());
                String repetativeEndMode = null;
                String repetativeModeValue = "";
                String repetativeEndModeValue = "";
                if (notificationConfig.getRepeatativeMode().equals(HkSystemConstantUtil.TaskRepetitiveMode.MONTHLY)) {
                    repetativeModeValue = " on " + notificationConfig.getMonthlyOnDay();
                }
                if (notificationConfig.getRepeatativeMode().equals(HkSystemConstantUtil.TaskRepetitiveMode.WEEKLY)) {
                    String[] namesOfDays = new String[]{"SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT"};
                    String weekValues = notificationConfig.getWeeklyOnDays();
                    if (!StringUtils.isEmpty(weekValues)) {
                        String[] dayValues = weekValues.split("|");
                        repetativeModeValue = " on ";
                        for (String dayValue : dayValues) {
                            repetativeModeValue += namesOfDays[Integer.valueOf(dayValue.trim()) - 1] + ",";
                        }
                        repetativeModeValue = repetativeModeValue.substring(0, repetativeModeValue.length());
                    }
                }
                //Repetative End mode value String
                if (notificationConfig.getEndRepeatMode().equals(HkSystemConstantUtil.TaskEndRepeatMode.ON_DATE)) {
                    repetativeEndMode = " on ";
                    repetativeEndModeValue = notificationConfig.getEndDate().toString();
                }
                if (notificationConfig.getEndRepeatMode().equals(HkSystemConstantUtil.TaskEndRepeatMode.AFTER_X_DAYS)) {
                    repetativeEndMode = " after ";
                    repetativeEndModeValue = notificationConfig.getAfterUnits().toString() + " days ";
                }
                if (notificationConfig.getEndRepeatMode().equals(HkSystemConstantUtil.TaskEndRepeatMode.AFTER_X_REPETITIONS)) {
                    repetativeEndMode = " after ";
                    repetativeEndModeValue = notificationConfig.getAfterUnits() + " repetitions ";
                }
                result = " at " + atTimeString + " Repeat-" + repetativeMode + repetativeModeValue + " Ends-" + repetativeEndMode + repetativeEndModeValue;
            } 
            return result;
        }
        return null;
    }

    public String createRuleSetStrng(String activityName, String serviceName, HkRuleSetDocument ruleSetDocument) {
        String result = "";
        if (!StringUtils.isEmpty(activityName)) {
            result = "Activity : " + activityName;
        }
        if (!StringUtils.isEmpty(serviceName)) {
            result = result + " , " + "Service : " + serviceName + " , ";
        }
        if (ruleSetDocument != null) {
            List<HkRuleDocument> hkRuleDocuments = ruleSetDocument.getRules();
            if (!CollectionUtils.isEmpty(hkRuleDocuments)) {
                String rulesStrng = "";
                for (HkRuleDocument hkRuleDocument : hkRuleDocuments) {
                    if (!StringUtils.isEmpty(hkRuleDocument.getRuleName())) {
                        if (!StringUtils.isEmpty(rulesStrng)) {
                            rulesStrng = rulesStrng + "," + hkRuleDocument.getRuleName();
                        } else {
                            rulesStrng = hkRuleDocument.getRuleName();
                        }
                    }
                    if (!CollectionUtils.isEmpty(hkRuleDocument.getCriterias())) {
                        String ruleCriteria = "";
                        for (HkRuleCriteriaDocument hkRuleCriteriaDocument : hkRuleDocument.getCriterias()) {
                            if (!StringUtils.isEmpty(ruleCriteria)) {
                                if (hkRuleCriteriaDocument.getEntityName() != null) {
                                    ruleCriteria = ruleCriteria + " " + hkRuleDocument.getApply() + " " + hkRuleCriteriaDocument.getEntityName() + ".";
                                    if (hkRuleCriteriaDocument.getDbFieldName() != null) {
                                        Map<String, HkFieldEntity> fields = fieldService.retrieveMapOfDBFieldNameWithEntity(Arrays.asList(hkRuleCriteriaDocument.getDbFieldName()), userDataBean.getCompanyId());
                                        if (fields != null && fields.get(hkRuleCriteriaDocument.getDbFieldName()) != null) {
                                            ruleCriteria = ruleCriteria + fields.get(hkRuleCriteriaDocument.getDbFieldName()).getFieldLabel();
                                            if (hkRuleCriteriaDocument.getOperator() != null) {
                                                ruleCriteria = ruleCriteria + " " + hkRuleCriteriaDocument.getOperator();
                                                if (hkRuleCriteriaDocument.getValue() != null) {
                                                    ruleCriteria = ruleCriteria + " " + hkRuleCriteriaDocument.getValue().toString();
                                                }
                                            }
                                        }

                                    }
                                }
                            } else {
                                if (hkRuleCriteriaDocument.getEntityName() != null) {
                                    ruleCriteria = hkRuleCriteriaDocument.getEntityName() + ".";
                                    if (hkRuleCriteriaDocument.getDbFieldName() != null) {
                                        Map<String, HkFieldEntity> fields = fieldService.retrieveMapOfDBFieldNameWithEntity(Arrays.asList(hkRuleCriteriaDocument.getDbFieldName()), userDataBean.getCompanyId());
                                        if (fields != null && fields.get(hkRuleCriteriaDocument.getDbFieldName()) != null) {
                                            ruleCriteria = ruleCriteria + fields.get(hkRuleCriteriaDocument.getDbFieldName()).getFieldLabel();
                                            if (hkRuleCriteriaDocument.getOperator() != null) {
                                                ruleCriteria = ruleCriteria + " " + hkRuleCriteriaDocument.getOperator();
                                                if (hkRuleCriteriaDocument.getValue() != null) {
                                                    ruleCriteria = ruleCriteria + " " + hkRuleCriteriaDocument.getValue().toString();
                                                }
                                            }
                                        }

                                    }
                                }
                            }
                        }
                        rulesStrng = rulesStrng + " Criterias : " + ruleCriteria;
                    } else {
                        rulesStrng = rulesStrng + "Criterias : N/A";
                    }
                }
                if (!StringUtils.isEmpty(rulesStrng)) {
                    result = result + " " + rulesStrng;
                }
            }
        }
        return result;
    }

//------By Shreya on 07 January 2015 for retrieving query results from sql in column name and value format.
    public Map<String, Object> retrieveQueryResults(String query, MasterReportDataBean masterReportDataBean, Boolean forPdf) throws GenericDatabaseException, SQLException, JSONException, ParseException {
        List<RbReportField> allFieldsList = new ArrayList<>();
        Map<Long, String> featureIdNameMap = new HashMap<>();
        List<Long> featureIds = new ArrayList<>();
        if (masterReportDataBean != null && !CollectionUtils.isEmpty(masterReportDataBean.getColumns())) {
            List<RbFieldDataBean> columns = masterReportDataBean.getColumns();
            for (RbFieldDataBean rbFieldDataBean : columns) {
//                if (rbFieldDataBean.getDbBaseType().equals(HkSystemConstantUtil.DBBaseType.MONGO_DB)) {
                RbReportField rbReportField = new RbReportField();
                rbReportField = convertReportFieldDataBeanToReportFieldModel(rbFieldDataBean, rbReportField);
                featureIds.add(rbFieldDataBean.getFeature());
                allFieldsList.add(rbReportField);
//                }
            }
            featureIdNameMap = this.retrieveFeatureNameByIds(featureIds);
        }
        //Retrieve Franchise information
        Set<Long> franchiseIds = new HashSet<>();
        if (CollectionUtils.isEmpty(masterReportDataBean.getFranchiseIds())) {
            if (userDataBean.getIsHKAdmin()) {
                List<FranchiseDataBean> retrieveAllFranchise = franchiseTransformerBean.retrieveAllFranchise(false);
                if (!CollectionUtils.isEmpty(retrieveAllFranchise)) {
                    for (FranchiseDataBean franchiseDataBean : retrieveAllFranchise) {
                        franchiseIds.add(franchiseDataBean.getId());
                    }
                }
            } else {
                franchiseIds.add(userDataBean.getCompanyId());
            }
        } else {
            for (Long id : masterReportDataBean.getFranchiseIds()) {
                franchiseIds.add(id);
            }
        }
        franchiseIds.add(0l);
        Map<String, Object> columnValuesMap = rbReportService.retrieveResults(query, allFieldsList, featureIdNameMap, franchiseIds);
        List<String> sectionNameList = new LinkedList<>();
        List<String> customFieldList = new LinkedList<>();
        Map<String, Map<String, List<RbFieldDataBean>>> sectionNameFieldsMap = new HashMap<>();
        Map<String, List<RbFieldDataBean>> fieldSectionMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(columnValuesMap)) {
            if (!CollectionUtils.isEmpty(masterReportDataBean.getColumns())) {
                Collections.sort(masterReportDataBean.getColumns(), new Comparator() {
                    @Override
                    public int compare(Object o1, Object o2) {
                        RbFieldDataBean oldfield = (RbFieldDataBean) o1;
                        RbFieldDataBean newfield = (RbFieldDataBean) o2;
                        return oldfield.getFieldSequence().compareTo(newfield.getFieldSequence());
                    }
                });
                for (RbFieldDataBean rbFieldDataBean : masterReportDataBean.getColumns()) {
                    if (rbFieldDataBean.getDbBaseType().equals(HkSystemConstantUtil.DBBaseType.MONGO_DB) && rbFieldDataBean.getSectionName() != null && !rbFieldDataBean.getIsDff()) {
//                        if (!CollectionUtils.isEmpty(fieldSectionMap) && fieldSectionMap.containsKey(rbFieldDataBean.getSectionName().toUpperCase())) {

                        if (fieldSectionMap.get(rbFieldDataBean.getSectionName().toUpperCase()) == null) {
                            List<RbFieldDataBean> fieldDataBeans = new ArrayList<>();
                            fieldDataBeans.add(rbFieldDataBean);
//                            Map<String, String> fieldLabelColNameMap = new HashMap<>();
//                            System.out.println("field label--" + rbFieldDataBean.getFieldLabel() + "alias--" + rbFieldDataBean.getAlias());
//                            
//                            if (!StringUtils.isEmpty(rbFieldDataBean.getAlias())) {
//                                fieldLabelColNameMap.put(rbFieldDataBean.getAlias(), rbFieldDataBean.getColName());
//                            } else {
//                                fieldLabelColNameMap.put(rbFieldDataBean.getFieldLabel(), rbFieldDataBean.getColName());
//                            }
                            fieldSectionMap.put(rbFieldDataBean.getSectionName().toUpperCase(), fieldDataBeans);
                        } else {
                            fieldSectionMap.get(rbFieldDataBean.getSectionName().toUpperCase()).add(rbFieldDataBean);
//                            Map<String, String> map = fieldSectionMap.get(rbFieldDataBean.getSectionName().toUpperCase());
//                            if (!StringUtils.isEmpty(rbFieldDataBean.getAlias())) {
//                                map.put(rbFieldDataBean.getAlias(), rbFieldDataBean.getColName());
//                            } else {
//                                map.put(rbFieldDataBean.getFieldLabel(), rbFieldDataBean.getColName());
//                            }

                        }
//                            fieldSectionMap.get(rbFieldDataBean.getSectionName().toUpperCase()).add(rbFieldDataBean.getColName());
//                        } else {
//                            List<String> fields = new ArrayList<>();
//                            fields.add(rbFieldDataBean.getColName());
//                            fieldSectionMap.put(rbFieldDataBean.getSectionName().toUpperCase(), fields);
//                        }
                        sectionNameList.add(rbFieldDataBean.getDbBaseName() + "." + "sectionList");
                        if (sectionNameFieldsMap.get(rbFieldDataBean.getDbBaseName() + "." + "sectionList") == null) {
                            Map<String, List<RbFieldDataBean>> sectionFieldMap = new HashMap<>();
                            List<RbFieldDataBean> rbFieldDataBeans = new ArrayList<>();
                            rbFieldDataBeans.add(rbFieldDataBean);
                            sectionFieldMap.put(rbFieldDataBean.getSectionName().toUpperCase(), rbFieldDataBeans);
                            sectionNameFieldsMap.put(rbFieldDataBean.getDbBaseName() + "." + "sectionList", sectionFieldMap);
                        } else {
                            Map<String, List<RbFieldDataBean>> map = sectionNameFieldsMap.get(rbFieldDataBean.getDbBaseName() + "." + "sectionList");
                            if (map.containsKey(rbFieldDataBean.getSectionName().toUpperCase())) {
                                map.get(rbFieldDataBean.getSectionName().toUpperCase()).add(rbFieldDataBean);
                            } else {
                                List<RbFieldDataBean> rbFieldDataBeans = new ArrayList<>();
                                rbFieldDataBeans.add(rbFieldDataBean);
                                map.put(rbFieldDataBean.getSectionName().toUpperCase(), rbFieldDataBeans);
                            }
                        }

                    }
                }
            }

            if (!CollectionUtils.isEmpty(allFieldsList) && !CollectionUtils.isEmpty(sectionNameList)) {
                //------By Shreya on 09 January 2015 for Applying fetching the fields from sectionList array of custom fields from available results
                for (Map.Entry<String, Object> entry : columnValuesMap.entrySet()) {
                    String column = entry.getKey();
                    if (!StringUtils.isEmpty(column)) {
                        if (column.equalsIgnoreCase("records")) {
                            List<LinkedHashMap<String, Object>> value = (ArrayList<LinkedHashMap<String, Object>>) entry.getValue();
                            if (!CollectionUtils.isEmpty(value)) {
                                for (LinkedHashMap<String, Object> linkedHashMap : value) {
                                    for (String sectionForComparing : sectionNameList) {
                                        if (linkedHashMap.containsKey(sectionForComparing)) {
                                            ArrayList valueOfSection = (ArrayList) linkedHashMap.get(sectionForComparing);
                                            if (valueOfSection != null) {
//                                                ResultSet resultSet = valueOfSection.getResultSet();
                                                if (!valueOfSection.isEmpty()) {
                                                    for (Object resultSet : valueOfSection) {
//                                                        Object string = resultSet.getObject(2);
                                                        Map<String, Object> pgObject = (Map<String, Object>) resultSet;
//                                                        if (pgObject.getType().equals("json")) {
                                                        JSONObject jsonObj = new JSONObject(pgObject);
                                                        String section = (String) jsonObj.get("sectionName");
                                                        ArrayList customFields = (ArrayList) jsonObj.get("customFields");
                                                        if (customFields.size() > 0) {
                                                            Map<String, Object> jsonObject = (Map<String, Object>) customFields.get(0);
                                                            Map<String, Object> fieldValue = (Map<String, Object>) jsonObject.get("fieldValue");
                                                            if (!CollectionUtils.isEmpty(fieldSectionMap) && fieldSectionMap.containsKey(section)) {
                                                                List<RbFieldDataBean> fieldList = fieldSectionMap.get(section);
                                                                if (!CollectionUtils.isEmpty(fieldList)) {
                                                                    for (RbFieldDataBean rbFieldDataBean : fieldList) {
                                                                        String reportFieldName = null;
                                                                        String colName = rbFieldDataBean.getColName();
                                                                        Boolean isIncludeTime = rbFieldDataBean.isIncludeTime() == null ? Boolean.FALSE : rbFieldDataBean.isIncludeTime();
                                                                        if (!StringUtils.isEmpty(rbFieldDataBean.getAlias())) {
                                                                            reportFieldName = rbFieldDataBean.getAlias();
                                                                        } else {
                                                                            reportFieldName = rbFieldDataBean.getFieldLabel();
                                                                        }
                                                                        if (rbFieldDataBean.getComponentType().equalsIgnoreCase(HkSystemConstantUtil.CustomField.ComponentType.DATE_RANGE)) {
                                                                            String dateRangeVal = "";
                                                                            if (fieldValue.containsKey("from" + colName)) {
                                                                                Object fromValue = fieldValue.get("from" + colName);
                                                                                if (fromValue.getClass().getSimpleName().equalsIgnoreCase("JSONObject")) {
                                                                                    JSONObject jsonObjGet = (JSONObject) fromValue;
                                                                                    if (jsonObjGet.has("$date")) {
                                                                                        if (dateRangeVal.equals("")) {
                                                                                            String formatDate = formatDate(Long.parseLong(jsonObjGet.get("$date").toString()), isIncludeTime);
                                                                                            dateRangeVal = formatDate;
                                                                                        }
//                                                                                        linkedHashMap.put(fieldLabel, jsonObjGet.get("$date"));
                                                                                    }
                                                                                }
                                                                                if (fieldValue.containsKey("to" + colName)) {
                                                                                    Object toValue = fieldValue.get("to" + colName);
                                                                                    if (toValue.getClass().getSimpleName().equalsIgnoreCase("JSONObject")) {
                                                                                        JSONObject jsonObjGet = (JSONObject) toValue;
                                                                                        if (!dateRangeVal.equals("")) {
                                                                                            String formatDate = formatDate(Long.parseLong(jsonObjGet.get("$date").toString()), isIncludeTime);
                                                                                            dateRangeVal = dateRangeVal + " to " + formatDate;
                                                                                        }
                                                                                    }
                                                                                }
                                                                                linkedHashMap.put(reportFieldName, dateRangeVal);
                                                                            } else {
                                                                                linkedHashMap.put(reportFieldName, null);
                                                                            }
                                                                        } else if (rbFieldDataBean.getComponentType().equalsIgnoreCase(HkSystemConstantUtil.CustomField.ComponentType.CURRENCY)) {
                                                                            String currencyWithCode = "";
                                                                            if (fieldValue.containsKey(colName)) {
                                                                                Object currencyValue = fieldValue.get(colName);
                                                                                currencyWithCode = currencyValue.toString();
                                                                                if (fieldValue.containsKey(colName + HkSystemConstantUtil.CURRENCY_CODE_CUSTOM)) {
                                                                                    Object currencyCode = fieldValue.get(colName + HkSystemConstantUtil.CURRENCY_CODE_CUSTOM);
                                                                                    currencyWithCode += " " + currencyCode.toString();
                                                                                }
                                                                                linkedHashMap.put(reportFieldName, currencyWithCode);
                                                                            } else {
                                                                                linkedHashMap.put(reportFieldName, null);
                                                                            }
                                                                        } else {
                                                                            if (fieldValue.containsKey(colName)) {
                                                                                Object get = fieldValue.get(colName);
                                                                                if (get.getClass().getSimpleName().equalsIgnoreCase("JSONObject")) {
                                                                                    JSONObject jsonObjGet = (JSONObject) get;
                                                                                    if (jsonObjGet.has("$date")) {
                                                                                        linkedHashMap.put(reportFieldName, formatDate(Long.parseLong(jsonObjGet.get("$date").toString()), isIncludeTime));
                                                                                    }
                                                                                } else {
                                                                                    linkedHashMap.put(reportFieldName, fieldValue.get(colName));
                                                                                }

                                                                            } else {
                                                                                linkedHashMap.put(reportFieldName, null);
                                                                            }
                                                                        }
                                                                    }
//                                                                    for (Map.Entry<String, String> entry1 : fieldMap.entrySet()) {
//                                                                        String fieldLabel = entry1.getKey();
//                                                                        String colName = entry1.getValue();
//                                                                        System.out.println("label-- : " + fieldLabel);
//                                                                        if (fieldValue.has(colName)) {
//                                                                            Object get = fieldValue.get(colName);
//                                                                            if (get.getClass().getSimpleName().equalsIgnoreCase("JSONObject")) {
//                                                                                JSONObject jsonObjGet = (JSONObject) get;
//                                                                                if (jsonObjGet.has("$date")) {
//                                                                                    linkedHashMap.put(fieldLabel, jsonObjGet.get("$date"));
//                                                                                }
//                                                                            } else {
//                                                                                linkedHashMap.put(fieldLabel, fieldValue.get(colName));
//                                                                            }
//
//                                                                        } else {
//                                                                            System.out.println("inside not null");
//                                                                            linkedHashMap.put(fieldLabel, null);
//                                                                        }
//                                                                    }

//                                                                    for (String label : fieldMap) {
//                                                                        System.out.println("label-- : " + label);
//                                                                        if (fieldValue.has(label)) {
//                                                                            Object get = fieldValue.get(label);
//                                                                            if (get.getClass().getSimpleName().equalsIgnoreCase("JSONObject")) {
//                                                                                JSONObject jsonObjGet = (JSONObject) get;
//                                                                                if (jsonObjGet.has("$date")) {
//                                                                                    linkedHashMap.put(label, jsonObjGet.get("$date"));
//                                                                                }
//                                                                            } else {
//                                                                                linkedHashMap.put(label, fieldValue.get(label));
//                                                                            }
//
//                                                                        } else {
//                                                                            System.out.println("inside not null");
//                                                                            linkedHashMap.put(label, null);
//                                                                        }
//                                                                    }
                                                                }
                                                            }
                                                        }

//                                                        }
                                                    }
                                                }
                                            } else {
                                                Map<String, List<RbFieldDataBean>> map = sectionNameFieldsMap.get(sectionForComparing);
                                                if (!CollectionUtils.isEmpty(map)) {
                                                    for (Map.Entry<String, List<RbFieldDataBean>> entry1 : map.entrySet()) {
                                                        String string = entry1.getKey();
                                                        List<RbFieldDataBean> list = entry1.getValue();
                                                        for (RbFieldDataBean rbFieldDataBean : list) {
                                                            linkedHashMap.put(rbFieldDataBean.getAlias(), null);
                                                        }

                                                    }
                                                }
                                            }
                                            linkedHashMap.remove(sectionForComparing);
                                        }
                                    }
                                }
                            }
                        }
                    }

                }
            }
        }
        //------By Shreya on 13 January 2015 for Applying converter for custom fields from available results
        Map<String, String> userCodeNameMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(columnValuesMap)) {
            Map<Long, String> keyCodeNameMap = new HashMap<>();
            Set<String> valueEntityIds = new HashSet<>();
            List<String> keyCodes = new ArrayList<>();
            //Initialize Conversion Map for fields.
            Map<String, Object> fkConversionMap = RbReportConstantUtils.Converter.FK_CONVERSION_MAP;
            //Initialize Static Conversion map.
            Map<String, Object> staticConversionMap = RbReportConstantUtils.Converter.STATIC_CONVERSION_MAP;
            if (!CollectionUtils.isEmpty(masterReportDataBean.getColumns())) {
                for (RbFieldDataBean rbFieldDataBean : masterReportDataBean.getColumns()) {
                    //For mongoDb non-general sections.
                    if (rbFieldDataBean.getDbBaseType().equalsIgnoreCase(HkSystemConstantUtil.DBBaseType.MONGO_DB) && rbFieldDataBean.getSectionName() != null && !rbFieldDataBean.getSectionName().equalsIgnoreCase("General")) {
                        valueEntityIds.add(rbFieldDataBean.getHkFieldId());
                    }
                    //For Fixed Fields.
                    if (rbFieldDataBean.getDbBaseType().equals(RbReportConstantUtils.DBBaseType.RELATIONAL_DB)) {
                        String componentType = rbFieldDataBean.getComponentType();
                        if (!StringUtils.isEmpty(componentType)
                                && (componentType.equals(HkSystemConstantUtil.CustomField.ComponentType.DROPDOWN) || componentType.equals(HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN)
                                || componentType.equals(HkSystemConstantUtil.CustomField.ComponentType.USER_MULTISELECT) || componentType.equals(HkSystemConstantUtil.CustomField.ComponentType.RADIO_BUTTON))) {
                            if (!StringUtils.isEmpty(rbFieldDataBean.getMasterCode())) {
                                valueEntityIds.add(rbFieldDataBean.getMasterCode());
                            }
                        }
                    }
                    //For Mongodb General Sections.
                    if (rbFieldDataBean.getDbBaseType().equalsIgnoreCase(HkSystemConstantUtil.DBBaseType.MONGO_DB) && (rbFieldDataBean.getSectionName() == null || rbFieldDataBean.getSectionName().equalsIgnoreCase("General"))) {
                        String componentType = rbFieldDataBean.getComponentType();
                        if (!StringUtils.isEmpty(componentType)
                                && (componentType.equals(HkSystemConstantUtil.CustomField.ComponentType.DROPDOWN) || componentType.equals(HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN)
                                || componentType.equals(HkSystemConstantUtil.CustomField.ComponentType.USER_MULTISELECT) || componentType.equals(HkSystemConstantUtil.CustomField.ComponentType.RADIO_BUTTON))) {
                            valueEntityIds.add(rbFieldDataBean.getHkFieldId());
                        }
                    }
                }
                keyCodes.addAll(valueEntityIds);
                List<HkValueEntity> hkValueEntitys = hkFoundationService.retrieveMasterValuesByCode(userDataBean.getCompanyId(), keyCodes);
                for (HkValueEntity hkValueEntity : hkValueEntitys) {
                    keyCodeNameMap.put(hkValueEntity.getId(), hkValueEntity.getValueName());
                }
            }
            Set<String> recipientsCodes = new HashSet<>();
//            List<Long> recipientsList = new ArrayList<>();
//            List<UMUser> usersList = new ArrayList<>();
//            Map<String, String> userCodeNameMap = new HashMap<>();
            List<LinkedHashMap<String, Object>> recordListForMultiSelect = (ArrayList<LinkedHashMap<String, Object>>) columnValuesMap.get("records");
            Iterator<LinkedHashMap<String, Object>> iteratorForMultiSelect = recordListForMultiSelect.iterator();
            while (iteratorForMultiSelect.hasNext()) {
                LinkedHashMap<String, Object> columnValueMap = iteratorForMultiSelect.next();
                if (!CollectionUtils.isEmpty(columnValueMap)) {
                    for (Map.Entry<String, Object> valueMap : columnValueMap.entrySet()) {
                        String key = valueMap.getKey();
                        Object value = valueMap.getValue();
//                        System.out.println("key : "+key);
//                        System.out.println("value : "+value);
                        if (value != null) {
                            for (RbFieldDataBean rbFields : masterReportDataBean.getColumns()) {
                                String reportFieldName = (StringUtils.isEmpty(rbFields.getAlias())
                                        ? rbFields.getFieldLabel() : rbFields.getAlias());
                                if ((key.equals(reportFieldName)) && rbFields.getDbBaseType().equals(HkSystemConstantUtil.DBBaseType.MONGO_DB) && rbFields.getSectionName() != null && !rbFields.getSectionName().equalsIgnoreCase("General")) {
                                    if (!StringUtils.isEmpty(rbFields.getComponentType())) {
                                        if (rbFields.getComponentType().equals(HkSystemConstantUtil.CustomField.ComponentType.USER_MULTISELECT)) {
                                            JSONArray array = (JSONArray) value;
                                            for (int i = 0; i < array.length(); i++) {
                                                recipientsCodes.add(array.get(i).toString());
                                            }
                                        }

                                    }
                                } else if ((key.equals(reportFieldName)) && rbFields.getDbBaseType().equals(HkSystemConstantUtil.DBBaseType.MONGO_DB) && (rbFields.getSectionName() == null || rbFields.getSectionName().equalsIgnoreCase("General"))) {
                                    if (!StringUtils.isEmpty(rbFields.getComponentType())) {
                                        if (rbFields.getComponentType().equals(HkSystemConstantUtil.CustomField.ComponentType.USER_MULTISELECT)) {
                                            Type type = new TypeToken<List<String>>() {
                                            }.getType();
                                            List<String> values = (new Gson()).fromJson(value.toString(), type);
                                            recipientsCodes.addAll(values);
                                        }
                                    }
                                } else if ((key.equals(reportFieldName)) && rbFields.getDbBaseType().equals(HkSystemConstantUtil.DBBaseType.RELATIONAL_DB)) {
                                    if (!StringUtils.isEmpty(rbFields.getComponentType())) {
                                        if (rbFields.getComponentType().equals(HkSystemConstantUtil.CustomField.ComponentType.USER_MULTISELECT)) {
                                            String values = value.toString();
                                            if (!StringUtils.isEmpty(values)) {
                                                String[] split = values.split(",");
                                                if (split != null) {
                                                    for (String data : split) {
                                                        recipientsCodes.add(data);
                                                    }
                                                }
                                            }
                                        }
                                    }

                                }
                            }
                        }
                    }
                }
            }
            if (!CollectionUtils.isEmpty(recipientsCodes)) {
                userCodeNameMap = userManagementServiceWrapper.retrieveRecipientNames(new ArrayList<>(recipientsCodes));
//                recipientsList.addAll(recipientsList);
//                List<UMUser> uMUsers = userManagementServiceWrapper.retrieveUsersForInvitees(recipientsList);
//                if (!CollectionUtils.isEmpty(uMUsers)) {
//                    usersList.addAll(uMUsers);
//                }
            }
            List<LinkedHashMap<String, Object>> recordList = (ArrayList<LinkedHashMap<String, Object>>) columnValuesMap.get("records");
            Iterator<LinkedHashMap<String, Object>> iterator = recordList.iterator();
            while (iterator.hasNext()) {
                LinkedHashMap<String, Object> columnValueMap = iterator.next();
                Map<String, String> currencyFieldCodeMap = new HashMap<>();
                if (!CollectionUtils.isEmpty(columnValueMap)) {
                    for (Map.Entry<String, Object> valueMap : columnValueMap.entrySet()) {
                        String key = valueMap.getKey();
                        Object value = valueMap.getValue();
                        if (value != null) {
                            for (RbFieldDataBean rbFields : masterReportDataBean.getColumns()) {
                                String reportFieldName = (StringUtils.isEmpty(rbFields.getAlias())
                                        ? rbFields.getFieldLabel() : rbFields.getAlias());
                                //For NoSql non-general Fields
                                if ((key.equals(reportFieldName)) && rbFields.getDbBaseType().equals(HkSystemConstantUtil.DBBaseType.MONGO_DB) && rbFields.getSectionName() != null && !rbFields.getSectionName().equalsIgnoreCase("General")) {
                                    if (RbReportConstantUtils.INNER_DOCUMENT_USER_CONVERSION_SET.contains(rbFields.getDbBaseName() + "." + rbFields.getColName())) {
                                        Double userIdDouble = Double.parseDouble(value.toString());
                                        Long userId = userIdDouble.longValue();
                                        String userValue = null;
                                        if (userId != null) {
                                            UMUser user = userService.retrieveUserById(userId, null);
                                            userValue = user.getFirstName() + " " + user.getMiddleName() + " " + user.getLastName();
                                        }
                                        columnValueMap.put(key, userValue);
                                    } else if (!StringUtils.isEmpty(rbFields.getComponentType())) {
                                        if (rbFields.getComponentType().equals(HkSystemConstantUtil.CustomField.ComponentType.DROPDOWN) || rbFields.getComponentType().equals(HkSystemConstantUtil.CustomField.ComponentType.RADIO_BUTTON)) {
                                            Integer intVal = (Integer) value;
                                            Long longVal = intVal.longValue();
                                            String valueName = keyCodeNameMap.get(longVal) == null ? value.toString() : keyCodeNameMap.get(longVal);
                                            columnValueMap.put(key, valueName);
                                        } else if (rbFields.getComponentType().equalsIgnoreCase(HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN)) {
                                            JSONArray array = (JSONArray) value;
                                            String values = "";
                                            for (int i = 0; i < array.length(); i++) {
                                                Long longVal = new Long(array.get(i).toString());
                                                String valueName = keyCodeNameMap.get(longVal);
                                                if (!values.equals("")) {
                                                    values += "," + valueName;
                                                } else {
                                                    values = valueName;
                                                }
                                            }
                                            columnValueMap.put(key, values);
                                        } else if (rbFields.getComponentType().equalsIgnoreCase(HkSystemConstantUtil.CustomField.ComponentType.USER_MULTISELECT)) {
                                            JSONArray array = (JSONArray) value;
                                            String finalValue = null;
                                            List<String> recipientCodes = new ArrayList<>();
                                            for (int i = 0; i < array.length(); i++) {
                                                recipientCodes.add(array.get(i).toString());
                                            }
                                            if (!CollectionUtils.isEmpty(recipientCodes)) {
                                                finalValue = this.prepareUserMultiSelectString(recipientCodes.toArray(new String[recipientCodes.size()]), userCodeNameMap);
                                            }

//                                            for (int i = 0; i < array.length(); i++) {
//                                                recipientCodes.add(array.get(i).toString());
//                                                if (!StringUtils.isEmpty(recipientCode)) {
////                                                    String[] split = recipientCode.split(":");
////                                                    Long id = null;
////                                                    String code = null;
////                                                    if (split != null) {
////                                                        if (!StringUtils.isEmpty(split[0]) && !StringUtils.isEmpty(split[1])) {
////                                                            id = Long.parseLong(split[0]);
////                                                            code = split[1];
////                                                            System.out.println("code : "+code);
//                                                    if (!CollectionUtils.isEmpty(userCodeNameMap)) {
//                                                        if (userCodeNameMap.containsKey(recipientCode)) {
//                                                            if (finalValue != null) {
//                                                                finalValue = finalValue + ", " + userCodeNameMap.get(recipientCode);
//                                                            } else {
//                                                                finalValue = userCodeNameMap.get(recipientCode);
//                                                            }
//                                                            System.out.println("final value is " + finalValue);
//                                                        }
//                                                    }
//                                                            if (code.equals(HkSystemConstantUtil.RecipientCodeType.DESIGNATION)) {
//                                                                for (UMUser user : usersList) {
//                                                                    if (!CollectionUtils.isEmpty(user.getUMUserRoleSet())) {
//                                                                        for (UMUserRole uMUserRole : user.getUMUserRoleSet()) {
//                                                                            if (uMUserRole.getuMUserRolePK().getRole() == id) {
//                                                                                String userName = null;
//                                                                                if (user.getLastName() != null) {
//                                                                                    userName = user.getFirstName() + " " + user.getLastName();
//                                                                                } else {
//                                                                                    userName = user.getFirstName();
//                                                                                }
//                                                                                if (finalValue != null) {
//                                                                                    if (!finalValue.contains(userName)) {
//                                                                                        finalValue = finalValue + "," + userName;
//                                                                                    }
//                                                                                } else {
//                                                                                    finalValue = userName;
//                                                                                }
//                                                                            }
//                                                                        }
//                                                                    }
//                                                                }
//                                                            } else if (code.equals(HkSystemConstantUtil.RecipientCodeType.DEPARTMENT)) {
//                                                                for (UMUser user : usersList) {
//                                                                    if (user.getDepartment().equals(id)) {
//                                                                        String userName = null;
//                                                                        if (user.getLastName() != null) {
//                                                                            userName = user.getFirstName() + " " + user.getLastName();
//                                                                        } else {
//                                                                            userName = user.getFirstName();
//                                                                        }
//                                                                        if (finalValue != null) {
//                                                                            if (!finalValue.contains(userName)) {
//                                                                                finalValue = finalValue + "," + userName;
//                                                                            }
//                                                                        } else {
//                                                                            finalValue = userName;
//                                                                        }
//                                                                    }
//                                                                }
//                                                            } else if (code.equals(HkSystemConstantUtil.RecipientCodeType.EMPLOYEE)) {
//                                                                for (UMUser user : usersList) {
//                                                                    if (user.getId().equals(id)) {
//                                                                        String userName = null;
//                                                                        if (user.getLastName() != null) {
//                                                                            userName = user.getFirstName() + " " + user.getLastName();
//                                                                        } else {
//                                                                            userName = user.getFirstName();
//                                                                        }
//                                                                        if (finalValue != null) {
//                                                                            if (!finalValue.contains(userName)) {
//                                                                                finalValue = finalValue + "," + userName;
//                                                                            }
//                                                                        } else {
//                                                                            finalValue = userName;
//                                                                        }
//                                                                    }
//                                                                }
//                                                            }
//                                                        }
//                                                    }
//                                                }
//                                            }
                                            columnValueMap.put(key, finalValue);
                                        } else if (rbFields.getComponentType().equalsIgnoreCase(HkSystemConstantUtil.CustomField.ComponentType.RADIO_BUTTON)) {
                                            Integer intVal = (Integer) value;
                                            Long longVal = intVal.longValue();
                                            String valueName = keyCodeNameMap.get(longVal);
                                            columnValueMap.put(key, valueName);
                                        } else if (rbFields.getComponentType().equalsIgnoreCase(HkSystemConstantUtil.CustomField.ComponentType.UPLOAD)) {
                                            String finalImageNames = null;
                                            Type type = new TypeToken<List<String>>() {
                                            }.getType();
                                            List<String> values = (new Gson()).fromJson(value.toString(), type);
                                            if (!CollectionUtils.isEmpty(values)) {
                                                for (String data : values) {
                                                    String imageName = data;
                                                    String fullImagePath = FolderManagement.getPathOfImage(imageName);
                                                    if (!StringUtils.isEmpty(fullImagePath)) {
                                                        if (finalImageNames != null) {
                                                            finalImageNames = finalImageNames + "," + fullImagePath;
                                                        } else {
                                                            finalImageNames = fullImagePath;
                                                        }
                                                    }
                                                }

                                            }
                                            columnValueMap.put(key, finalImageNames);
                                        } else if (rbFields.getComponentType().equalsIgnoreCase(HkSystemConstantUtil.CustomField.ComponentType.IMAGE)) {
                                            String imageName = value.toString();
                                            String fullImagePath = FolderManagement.getPathOfImage(imageName);
                                            columnValueMap.put(key, fullImagePath);
                                        } else if (rbFields.getComponentType().equalsIgnoreCase(HkSystemConstantUtil.CustomField.ComponentType.CURRENCY)) {
                                            String currencyValueWithCode = value.toString();
                                            Double currencyValue = Double.parseDouble(currencyValueWithCode.split(" ")[0].trim());
                                            String currencyCode = currencyValueWithCode.split(" ")[1].trim();
                                            columnValueMap.put(key, currencyValueWithCode);
                                            currencyFieldCodeMap.put(key + HkSystemConstantUtil.CURRENCY_CODE_CUSTOM, currencyCode);
                                        }
                                    }

                                } else if (rbFields.getDbBaseType().equals(HkSystemConstantUtil.DBBaseType.RELATIONAL_DB)) {
                                    //For Sql fields
                                    String columnDefinition = rbFields.getDbBaseName() + "." + rbFields.getColName();
                                    //Remove delimeter from goal template results.
                                    if (key.equals(reportFieldName) && RbReportConstantUtils.GOAL_TEMPLATE_VALIDATION_SET.contains(columnDefinition)) {
                                        if (columnValueMap.get(key) != null) {
                                            String result = columnValueMap.get(key).toString();
                                            result = result.replace(HkSystemConstantUtil.SEPARATOR_FOR_GOAL_TEMPLATE, " ");
                                            columnValueMap.put(key, result);
                                        }
                                    }
                                    //For conversion of description in notifications.
                                    if (key.equals(reportFieldName) && columnDefinition.equals("hk_notification_info.description")) {
                                        SearchBeanForLabel searchBean = new SearchBeanForLabel();
                                        List<LanguageDataBean> allLanguages = localesTransformerBean.retrieveAllLanguages();
                                        String prefferedLang = userDataBean.getPrefferedLang();
                                        LanguageDataBean prefferedLangDataBean = null;
                                        if (!CollectionUtils.isEmpty(allLanguages)) {
                                            for (LanguageDataBean dataBean : allLanguages) {
                                                if (dataBean.getCode().equals(prefferedLang)) {
                                                    prefferedLangDataBean = dataBean;
                                                    break;
                                                }
                                            }
                                        }

                                        if (prefferedLangDataBean != null) {
                                            searchBean.setLanguageDataBean(prefferedLangDataBean);
                                            searchBean.setType("NOTIFICATION");
                                            searchBean.setSearchText("");
                                            List<LocaleDataBean> allLabels = localesTransformerBean.retrieveLabelsBySearchCriteria(searchBean);
                                            String[] values = value.toString().split("@@");
                                            String instanceType = values[0];
                                            String description = values[1];
                                            String toBeTranslated = null;
                                            if (!CollectionUtils.isEmpty(allLabels)) {
                                                for (LocaleDataBean localeDataBean : allLabels) {
                                                    if (localeDataBean.getKey().trim().equals(instanceType)) {
                                                        toBeTranslated = localeDataBean.getText();
                                                        break;
                                                    }
                                                }
                                            }
                                            if (toBeTranslated != null) {
                                                Type type = new TypeToken<Map<String, String>>() {
                                                }.getType();
                                                Map<String, String> descriptionKeyValueMap = (new Gson()).fromJson(description, type);
                                                String translatedValue = toBeTranslated;
                                                for (Map.Entry<String, String> entry : descriptionKeyValueMap.entrySet()) {
                                                    String toBeReplacedKey = "{{" + entry.getKey().trim() + "}}";
                                                    String toBeReplacedValue = entry.getValue();
                                                    if (toBeReplacedValue.startsWith("D|")) {
                                                        Boolean isIncludeTime = rbFields.isIncludeTime() == null ? Boolean.FALSE : rbFields.isIncludeTime();
                                                        Long dateValue = Long.parseLong(toBeReplacedValue.substring(2, toBeReplacedValue.length()));
                                                        toBeReplacedValue = formatDate(dateValue, isIncludeTime);
                                                    }
                                                    translatedValue = translatedValue.replace(toBeReplacedKey, toBeReplacedValue);
                                                }
                                                columnValueMap.put(key, translatedValue);
                                            }
                                        }
                                    } else if (key.equals(reportFieldName) && columnDefinition.equals("hk_notification_configuration_info.web_email_message")) {
                                        if (columnValueMap.get(key) != null) {
                                            Long notificationConfigurationId = Long.parseLong(columnValueMap.get(key).toString());
                                            if (notificationConfigurationId != null) {
                                                HkNotificationConfigurationEntity notificationConfigurationEntity = hkNotificationService.retrieveNotificationCnfigurationById(notificationConfigurationId);
                                                String webEmailMessage = this.createWebEmailMessage(notificationConfigurationEntity);
                                                columnValueMap.put(key, webEmailMessage);
                                            }
                                        }

                                    } else if (key.equals(reportFieldName) && columnDefinition.equals("hk_notification_configuration_info.notification_criteria")) {
                                        if (columnValueMap.get(key) != null) {
                                            Long notificationConfigurationId = Long.parseLong(columnValueMap.get(key).toString());
                                            if (notificationConfigurationId != null) {
                                                String notificationCriteria = this.createStringForNotificationConfig(notificationConfigurationId);
                                                columnValueMap.put(key, notificationCriteria);
                                            }
                                        }

                                    } else if (key.equals(reportFieldName) && rbFields.getIsRule() != null && rbFields.getIsRule()) {
                                        String rules = value.toString().trim();
                                        Type type = new TypeToken<List<Map<String, Object>>>() {
                                        }.getType();
                                        List<Map<String, Object>> values = (new Gson()).fromJson(rules, type);
                                        String prepareRuleSetString = this.prepareRuleSetString(values);
                                        columnValueMap.put(key, prepareRuleSetString);
                                    } else if (key.equals(reportFieldName) && (CollectionUtils.isEmpty(fkConversionMap) || fkConversionMap.get(columnDefinition) == null)) {
                                        if (!StringUtils.isEmpty(rbFields.getComponentType())) {
                                            if (rbFields.getComponentType().equals(HkSystemConstantUtil.CustomField.ComponentType.DROPDOWN) || rbFields.getComponentType().equals(HkSystemConstantUtil.CustomField.ComponentType.RADIO_BUTTON)) {
                                                Long longVal = new Long(value.toString());
//                                                    System.out.println("longVal---" + longVal);
                                                String valueName = keyCodeNameMap.get(longVal) == null ? value.toString() : keyCodeNameMap.get(longVal);
//                                                    System.out.println("valueName---" + valueName);
//                                            value = valueName;
                                                columnValueMap.put(key, valueName);
                                            } else if (rbFields.getComponentType().equalsIgnoreCase(HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN)) {
                                                String[] array = value.toString().trim().split(",");
                                                String values = "";
                                                for (int i = 0; i < array.length; i++) {
                                                    Long longVal = new Long(array[i].toString());
//                                                        System.out.println("longVal---" + longVal);
                                                    String valueName = keyCodeNameMap.get(longVal);
//                                                        System.out.println("valueName---" + valueName);
                                                    if (!values.equals("")) {
                                                        values += "," + valueName;
                                                    } else {
                                                        values = valueName;
                                                    }
                                                }
                                                columnValueMap.put(key, values);
                                            } else if (rbFields.getComponentType().equalsIgnoreCase(HkSystemConstantUtil.CustomField.ComponentType.USER_MULTISELECT)) {
                                                String values = value.toString();
                                                String finalValue = null;
                                                if (!StringUtils.isEmpty(values)) {
                                                    finalValue = this.prepareUserMultiSelectString(values.split(","), userCodeNameMap);
//                                                    String[] split = values.split(",");
//                                                    if (split != null) {
//                                                        for (String recipientCode : split) {
//                                                            if (!StringUtils.isEmpty(recipientCode)) {
//                                                                if (!CollectionUtils.isEmpty(userCodeNameMap)) {
//                                                                    if (userCodeNameMap.containsKey(recipientCode)) {
//                                                                        if (finalValue != null) {
//                                                                            finalValue = finalValue + ", " + userCodeNameMap.get(recipientCode);
//                                                                        } else {
//                                                                            finalValue = userCodeNameMap.get(recipientCode);
//                                                                        }
//                                                                        System.out.println("...........final value is " + finalValue);
//                                                                    }
//                                                                }
//                                                            }
//                                                        }
//                                                    }
                                                }
                                                columnValueMap.put(key, finalValue);
                                            } else if (rbFields.getComponentType().equalsIgnoreCase(HkSystemConstantUtil.CustomField.ComponentType.UPLOAD) || rbFields.getComponentType().equalsIgnoreCase(HkSystemConstantUtil.CustomField.ComponentType.IMAGE)) {
                                                String imageName = value.toString();
                                                String fullImagePath = FolderManagement.getPathOfImage(imageName);
                                                columnValueMap.put(key, fullImagePath);
                                            }
                                        }

                                    }
                                    //Conversion applied if the field is available for static conversion.
                                    if (key.equals(reportFieldName) && !CollectionUtils.isEmpty(staticConversionMap) && staticConversionMap.containsKey(columnDefinition)) {
                                        Type type = new TypeToken<Map<String, String>>() {
                                        }.getType();
                                        Map<String, String> fieldStaticConversionMap = (new Gson()).fromJson(staticConversionMap.get(columnDefinition).toString(), type);

                                        if (!CollectionUtils.isEmpty(fieldStaticConversionMap)) {
                                            if (fieldStaticConversionMap.containsKey(value.toString().trim())) {
                                                columnValueMap.put(key, fieldStaticConversionMap.get(value.toString().trim()));
                                            }
                                        }
                                    }
                                } else if ((key.equals(reportFieldName)) && rbFields.getDbBaseType().equals(HkSystemConstantUtil.DBBaseType.MONGO_DB) && (rbFields.getSectionName() == null || rbFields.getSectionName().equalsIgnoreCase("General"))) {
                                    String columnDefinition = rbFields.getDbBaseName() + "." + rbFields.getColName();
                                    //For NOSQL General Fields.
                                    if (!StringUtils.isEmpty(rbFields.getComponentType())) {
                                        if (rbFields.getComponentType().equals(HkSystemConstantUtil.CustomField.ComponentType.DROPDOWN) && !RbReportConstantUtils.Converter.INTRA_TABLE_CONVERSION_MAP.keySet().contains(columnDefinition)) {
                                            Long longVal = new Long(value.toString());
//                                                    System.out.println("longVal---" + longVal);
                                            String valueName = keyCodeNameMap.get(longVal) == null ? value.toString() : keyCodeNameMap.get(longVal);
//                                                    System.out.println("valueName---" + valueName);
                                            columnValueMap.put(key, valueName);
                                        } else if (rbFields.getComponentType().equalsIgnoreCase(HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN)) {
                                            Type type = new TypeToken<List<String>>() {
                                            }.getType();
                                            List<String> array = (new Gson()).fromJson(value.toString(), type);
//                                            String[] array = value.toString().trim().split(",");
                                            String values = "";
                                            for (int i = 0; i < array.size(); i++) {
                                                Long longVal = new Long(array.get(i).toString());
//                                                        System.out.println("longVal---" + longVal);
                                                String valueName = keyCodeNameMap.get(longVal);
//                                                        System.out.println("valueName---" + valueName);
                                                if (!values.equals("")) {
                                                    values += "," + valueName;
                                                } else {
                                                    values = valueName;
                                                }
                                            }
//                                            System.out.println("values--"+values);
                                            columnValueMap.put(key, values);
                                        } else if (rbFields.getComponentType().equalsIgnoreCase(HkSystemConstantUtil.CustomField.ComponentType.USER_MULTISELECT)) {
                                            Type type = new TypeToken<List<String>>() {
                                            }.getType();
                                            List<String> values = (new Gson()).fromJson(value.toString(), type);
                                            String finalValue = null;
                                            if (!CollectionUtils.isEmpty(values)) {
                                                finalValue = this.prepareUserMultiSelectString(values.toArray(new String[values.size()]), userCodeNameMap);
                                            }
//                                            for (int i = 0; i < values.size(); i++) {
//                                                String recipientCode = values.get(i);
//                                                if (!StringUtils.isEmpty(recipientCode)) {
//                                                    if (!CollectionUtils.isEmpty(userCodeNameMap)) {
//                                                        if (userCodeNameMap.containsKey(recipientCode)) {
//                                                            if (finalValue != null) {
//                                                                finalValue = finalValue + ", " + userCodeNameMap.get(recipientCode);
//                                                            } else {
//                                                                finalValue = userCodeNameMap.get(recipientCode);
//                                                            }
//                                                            System.out.println("---------------final value is " + finalValue);
//                                                        }
//                                                    }
//                                                }
//                                            }
                                            columnValueMap.put(key, finalValue);
                                        } else if (rbFields.getComponentType().equalsIgnoreCase(HkSystemConstantUtil.CustomField.ComponentType.UPLOAD)) {
                                            String finalImageNames = null;
                                            Type type = new TypeToken<List<String>>() {
                                            }.getType();
                                            List<String> values = (new Gson()).fromJson(value.toString(), type);
                                            if (!CollectionUtils.isEmpty(values)) {
                                                for (String data : values) {
                                                    String imageName = data;
                                                    String fullImagePath = FolderManagement.getPathOfImage(imageName);
                                                    if (!StringUtils.isEmpty(fullImagePath)) {
                                                        if (finalImageNames != null) {
                                                            finalImageNames = finalImageNames + "," + fullImagePath;
                                                        } else {
                                                            finalImageNames = fullImagePath;
                                                        }
                                                    }
                                                }

                                            }
                                            columnValueMap.put(key, finalImageNames);
                                        } else if (rbFields.getComponentType().equalsIgnoreCase(HkSystemConstantUtil.CustomField.ComponentType.IMAGE)) {
                                            String imageName = value.toString();
                                            String fullImagePath = FolderManagement.getPathOfImage(imageName);
                                            columnValueMap.put(key, fullImagePath);
                                        } else if (rbFields.getComponentType().equalsIgnoreCase(HkSystemConstantUtil.CustomField.ComponentType.CURRENCY)) {
                                            String currencyValueWithCode = value.toString();
                                            if (!StringUtils.isEmpty(currencyValueWithCode)) {
                                                Double currencyValue = Double.parseDouble(currencyValueWithCode.split(" ")[0].trim());
                                                String currencyCode = currencyValueWithCode.split(" ")[1].trim();
                                                columnValueMap.put(key, currencyValueWithCode);
                                                currencyFieldCodeMap.put(key + HkSystemConstantUtil.CURRENCY_CODE_CUSTOM, currencyCode);
                                            }
                                        } else if (key.equals(reportFieldName) && (columnDefinition.equals("workallotment.lotNumber") || columnDefinition.equals("workallotmentaggregation.lotNumber"))) {
                                            if (!StringUtils.isEmpty(value)) {
                                                String[] values = value.toString().split("_type_@@_");
                                                if (values.length > 0) {
                                                    if (values[0] != null) {
                                                        String instanceType = values[0];
                                                        if (instanceType.equals(HkSystemConstantUtil.WORKALLOTEMENT_UNIT_TYPE.LOT)) {
                                                            if (values[1] != null) {
                                                                ObjectId instanceId = new ObjectId(values[1]);
                                                                HkLotDocument lotDocument = hkStockService.retrieveLotById(instanceId);
                                                                if (lotDocument != null) {
                                                                    if (lotDocument.getFieldValue().toMap().containsKey(HkSystemConstantUtil.AutoNumber.LOT_ID)) {
                                                                        columnValueMap.put(key, lotDocument.getFieldValue().toMap().get(HkSystemConstantUtil.AutoNumber.LOT_ID).toString());
                                                                    }
                                                                }
                                                            }

                                                        }
                                                    }
                                                }
                                            }
                                        } else if (key.equals(reportFieldName) && (columnDefinition.equals("workallotment.packetNumber") || columnDefinition.equals("workallotmentaggregation.packetNumber"))) {
                                            if (!StringUtils.isEmpty(value)) {
                                                String[] values = value.toString().split("_type_@@_");
                                                if (values.length > 0) {
                                                    if (values[0] != null) {
                                                        String instanceType = values[0];
                                                        if (instanceType.equals(HkSystemConstantUtil.WORKALLOTEMENT_UNIT_TYPE.PACKET)) {
                                                            if (values[1] != null) {
                                                                ObjectId instanceId = new ObjectId(values[1]);
                                                                HkPacketDocument packetDocument = hkStockService.retrievePacketById(instanceId,true);
                                                                if (packetDocument != null) {
                                                                    if (packetDocument.getFieldValue().toMap().containsKey(HkSystemConstantUtil.AutoNumber.PACKET_ID)) {
                                                                        columnValueMap.put(key, packetDocument.getFieldValue().toMap().get(HkSystemConstantUtil.AutoNumber.PACKET_ID).toString());
                                                                    }
                                                                }
                                                            }

                                                        }
                                                    }
                                                }
                                            }
                                        } else if (key.equals(reportFieldName) && (columnDefinition.equals("workallotment.ruleSuccessed"))) {
                                            if (!StringUtils.isEmpty(value)) {
                                                String[] values = value.toString().split("|");
                                                if (values.length > 0) {
                                                    if (values[0] != null) {
                                                        String ruleSetId = values[0];
                                                        if (!StringUtils.isEmpty(values[1])) {
                                                            String ruleId = values[1];
                                                            Integer rulInteger = new Integer(ruleId);
                                                            ObjectId ruleSetObjId = new ObjectId(ruleSetId);
                                                            HkRuleSetDocument hkRuleSetDocument = ruleService.retrieveRuleSetById(ruleSetObjId);
                                                            if (hkRuleSetDocument != null) {
                                                                List<HkRuleDocument> rules = hkRuleSetDocument.getRules();
                                                                if (!CollectionUtils.isEmpty(rules)) {
                                                                    for (HkRuleDocument rule : rules) {
                                                                        if (rule.getId() == rulInteger.intValue()) {
                                                                            columnValueMap.put(key, rule.getRuleName());
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                       } 
                                    }
                                    //Conversion applied if the field is available for static conversion.
                                    if (key.equals(reportFieldName) && !CollectionUtils.isEmpty(staticConversionMap) && staticConversionMap.containsKey(columnDefinition)) {
                                        Type type = new TypeToken<Map<String, String>>() {
                                        }.getType();
                                        Map<String, String> fieldStaticConversionMap = (new Gson()).fromJson(staticConversionMap.get(columnDefinition).toString(), type);

                                        if (!CollectionUtils.isEmpty(fieldStaticConversionMap)) {
                                            if (fieldStaticConversionMap.containsKey(value.toString().trim())) {
                                                columnValueMap.put(key, fieldStaticConversionMap.get(value.toString().trim()));
                                            }
                                        }
                                    }
                                }

                            }
                        }

                    }
                    //Send currency code separately.
//                    if (!CollectionUtils.isEmpty(currencyFieldCodeMap)) {
//                        columnValueMap.putAll(currencyFieldCodeMap);
//                    }
                }
            }
        }
//------By Shreya on 12 January 2015 for Applying filters for custom fields from available results

        if (!CollectionUtils.isEmpty(columnValuesMap)) {
            List<LinkedHashMap<String, Object>> recordList = (ArrayList<LinkedHashMap<String, Object>>) columnValuesMap.get("records");
            Iterator<LinkedHashMap<String, Object>> iterator = recordList.iterator();
            Map<String, Object> fkConversionMap = RbReportConstantUtils.Converter.FK_CONVERSION_MAP;
            while (iterator.hasNext()) {
                LinkedHashMap<String, Object> columnValueMap = iterator.next();
                Boolean rowInvalid = false;
                if (!CollectionUtils.isEmpty(columnValueMap)) {
                    for (Map.Entry<String, Object> valueMap : columnValueMap.entrySet()) {
                        String key = valueMap.getKey();
                        Object value = valueMap.getValue();
                        String filterValSecond = null;

                        for (RbFieldDataBean rbFields : masterReportDataBean.getColumns()) {
                            String reportFieldName = (StringUtils.isEmpty(rbFields.getAlias())
                                    ? rbFields.getFieldLabel() : rbFields.getAlias());
                            String columnDefinition = rbFields.getDbBaseName() + "." + rbFields.getColName();
                            //Filtration For mongodb non-general sections.
                            if (key.equals(reportFieldName) && rbFields.getDbBaseType().equals(HkSystemConstantUtil.DBBaseType.MONGO_DB) && rbFields.getSectionName() != null && !rbFields.getSectionName().equalsIgnoreCase("General")) {

                                if (!StringUtils.isEmpty(rbFields.getFilter())) {
                                    String rbFilter = rbFields.getFilter();
                                    JSONArray array;
                                    array = new JSONArray(rbFilter);
                                    for (int i = 0; i < array.length(); i++) {
                                        JSONObject filterJson = array.getJSONObject(i);
                                        if (filterJson.has("filter")) {
                                            String operator = (String) filterJson.get("filter");
                                            if (value == null && !(operator.equals("is null") || operator.equals("is not null"))) {
                                                rowInvalid = true;
                                                break;
                                            }
                                            if (filterJson.has("filterValFirst") && !(operator.equals("is null") || operator.equals("is not null"))) {
                                                String filterValFirst = filterJson.get("filterValFirst").toString();
                                                if (filterJson.has("filterValSecond")) {
                                                    filterValSecond = filterJson.get("filterValSecond").toString();
                                                }
                                                if (rbFields.getDataType().equals(HkSystemConstantUtil.CustomField.DataType.INT8)) {
                                                    Long dbValue = Long.parseLong(value.toString());
                                                    List<Long> firstValues = new ArrayList<>();
                                                    String[] arrays = filterValFirst.split(",");
                                                    for (int j = 0; j < arrays.length; j++) {
                                                        firstValues.add((Long.parseLong(arrays[j])));
                                                    }
                                                    switch (operator) {
                                                        case "=":
                                                            if (!firstValues.contains(dbValue)) {
                                                                rowInvalid = true;
                                                            }
                                                            break; //optional
                                                        case "!=":
                                                            if (firstValues.contains(dbValue)) {
                                                                rowInvalid = true;
                                                            }
                                                            break; //optional
                                                        case ">":
                                                            for (Long data : firstValues) {
                                                                if (data.compareTo(dbValue) > 0) {
                                                                    rowInvalid = true;
                                                                }
                                                            }
                                                            break;
                                                        case "<":
                                                            for (Long data : firstValues) {
                                                                if (data.compareTo(dbValue) < 0) {
                                                                    rowInvalid = true;
                                                                }
                                                            }
                                                            break;
                                                        case ">=":
                                                            for (Long data : firstValues) {
                                                                if (data.compareTo(dbValue) >= 0) {
                                                                    rowInvalid = true;
                                                                }
                                                            }
                                                            break;
                                                        case "<=":
                                                            for (Long data : firstValues) {
                                                                if (data.compareTo(dbValue) <= 0) {
                                                                    rowInvalid = true;
                                                                }
                                                            }
                                                            break;
                                                        case "between":
                                                            if (filterValSecond != null) {
                                                                Long filterSecond = Long.parseLong(filterValSecond);
                                                                for (Long data : firstValues) {
                                                                    if (data.compareTo(dbValue) >= 0 && filterSecond.compareTo(dbValue) <= 0) {
                                                                        rowInvalid = true;
                                                                    }
                                                                }
                                                            }
                                                            break;
                                                        default:
                                                            break;//Optional
                                                        }
                                                } else if (rbFields.getDataType().equals(HkSystemConstantUtil.CustomField.DataType.DOUBLE)) {
                                                    Double dbValue = null;
                                                    if (rbFields.getComponentType().equals(HkSystemConstantUtil.CustomField.ComponentType.CURRENCY)) {
                                                        dbValue = Double.parseDouble(value.toString().split(" ")[0].trim());
                                                    } else {
                                                        dbValue = Double.parseDouble(value.toString());
                                                    }
                                                    List<Double> firstValues = new ArrayList<>();
                                                    String[] arrays = filterValFirst.split(",");
                                                    for (int j = 0; j < arrays.length; j++) {
                                                        firstValues.add((Double.parseDouble(arrays[j])));
                                                    }
                                                    switch (operator) {
                                                        case "=":
                                                            if (!firstValues.contains(dbValue)) {
                                                                rowInvalid = true;
                                                            }
                                                            break; //optional
                                                        case "!=":
                                                            if (firstValues.contains(dbValue)) {
                                                                rowInvalid = true;
                                                            }
                                                            break; //optional
                                                        case ">":
                                                            for (Double data : firstValues) {
                                                                if (data.compareTo(dbValue) > 0) {
                                                                    rowInvalid = true;
                                                                }
                                                            }
                                                            break;
                                                        case "<":
                                                            for (Double data : firstValues) {
                                                                if (data.compareTo(dbValue) < 0) {
                                                                    rowInvalid = true;
                                                                }
                                                            }
                                                            break;
                                                        case ">=":
                                                            for (Double data : firstValues) {
                                                                if (data.compareTo(dbValue) >= 0) {
                                                                    rowInvalid = true;
                                                                }
                                                            }
                                                            break;
                                                        case "<=":
                                                            for (Double data : firstValues) {
                                                                if (data.compareTo(dbValue) <= 0) {
                                                                    rowInvalid = true;
                                                                }
                                                            }
                                                            break;
                                                        case "between":
                                                            if (filterValSecond != null) {
                                                                Double filterSecond = Double.parseDouble(filterValSecond);
                                                                for (Double data : firstValues) {
                                                                    if (data.compareTo(dbValue) >= 0 && filterSecond.compareTo(dbValue) <= 0) {
                                                                        rowInvalid = true;
                                                                    }
                                                                }
                                                            }
                                                            break;
                                                        default:
                                                            break;//Optional
                                                        }
                                                } else if (rbFields.getDataType().equals(HkSystemConstantUtil.CustomField.DataType.VARCHAR)) {

                                                    if (rbFields.getComponentType().equals(HkSystemConstantUtil.CustomField.ComponentType.USER_MULTISELECT)) {
                                                        List<String> firstValues = new ArrayList<>();
                                                        String[] arrays = null;
                                                        arrays = filterValFirst.split(",");

                                                        for (String array1 : arrays) {
                                                            firstValues.add(array1.trim());
                                                        }
                                                        String multiSelectFilterValue = null;
                                                        List<String> multiSelectValues = null;
                                                        boolean isExists = false;
                                                        switch (operator) {
                                                            case "=":
                                                                multiSelectValues = new ArrayList<>();
                                                                for (String firstValue : firstValues) {
                                                                    multiSelectFilterValue = this.prepareUserMultiSelectString(new String[]{firstValue}, userCodeNameMap);
                                                                    multiSelectValues.add(multiSelectFilterValue);
                                                                }

                                                                for (String dataValue : value.toString().split(";")) {
                                                                    String[] dataValues = dataValue.split(":")[1].trim().split(",");
                                                                    List<String> newList = new ArrayList<>();
                                                                    for (String item : dataValues) {
                                                                        isExists = false;
                                                                        Iterator<String> itr = multiSelectValues.iterator();
                                                                        while (itr.hasNext()) {
                                                                            String multiSelectValue = itr.next();
                                                                            String receipientType = multiSelectValue.split(":")[0].trim();
                                                                            if (receipientType.equals(dataValue.split(":")[0].trim())) {

                                                                                if (item.trim().equals(multiSelectValue.split(":")[1].trim())) {
                                                                                    isExists = true;
                                                                                    itr.remove();
                                                                                    break;
                                                                                }
                                                                            }
                                                                        }
                                                                        if (!isExists) {
                                                                            break;
                                                                        }
                                                                    }
                                                                    if (!isExists) {
                                                                        break;
                                                                    }
                                                                }
                                                                if (!isExists) {
                                                                    rowInvalid = true;
                                                                }
                                                                if (multiSelectValues.size() > 0) {
                                                                    rowInvalid = true;
                                                                }
                                                                break;
                                                            case "in":
                                                                multiSelectValues = new ArrayList<>();
                                                                for (String firstValue : firstValues) {
                                                                    multiSelectFilterValue = this.prepareUserMultiSelectString(new String[]{firstValue}, userCodeNameMap);
                                                                    multiSelectValues.add(multiSelectFilterValue);
                                                                }
                                                                isExists = false;
                                                                for (String multiSelectValue : multiSelectValues) {
                                                                    String receipientType = multiSelectValue.split(":")[0].trim();
                                                                    for (String dataValue : value.toString().split(";")) {
                                                                        if (receipientType.equals(dataValue.split(":")[0].trim())) {
                                                                            String[] dataValues = dataValue.split(":")[1].trim().split(",");
                                                                            List<String> newList = new ArrayList<>();
                                                                            for (String item : dataValues) {
                                                                                newList.add(item.trim());
                                                                            }
                                                                            if (newList.contains(multiSelectValue.split(":")[1].trim())) {
                                                                                isExists = true;
                                                                                break;
                                                                            }
                                                                        }
                                                                    }
                                                                    if (isExists) {
                                                                        break;
                                                                    }
                                                                }
                                                                if (!isExists) {
                                                                    rowInvalid = true;
                                                                }
                                                                break; //optional
                                                            case "!=":
                                                                multiSelectValues = new ArrayList<>();
                                                                for (String firstValue : firstValues) {
                                                                    multiSelectFilterValue = this.prepareUserMultiSelectString(new String[]{firstValue}, userCodeNameMap);
                                                                    multiSelectValues.add(multiSelectFilterValue);
                                                                }
                                                                int fieldCount = 0;
                                                                int filterFieldCount = multiSelectValues.size();
                                                                for (String dataValue : value.toString().split(";")) {
                                                                    String[] dataValues = dataValue.split(":")[1].trim().split(",");
                                                                    List<String> newList = new ArrayList<>();
                                                                    for (String item : dataValues) {
                                                                        fieldCount++;
                                                                        Iterator<String> itr = multiSelectValues.iterator();
                                                                        while (itr.hasNext()) {
                                                                            String multiSelectValue = itr.next();
                                                                            String receipientType = multiSelectValue.split(":")[0].trim();
                                                                            if (receipientType.equals(dataValue.split(":")[0].trim())) {
                                                                                if (item.trim().equals(multiSelectValue.split(":")[1].trim())) {
                                                                                    itr.remove();
                                                                                    break;
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                                if (filterFieldCount < fieldCount) {
                                                                    rowInvalid = false;
                                                                } else if (multiSelectValues.isEmpty()) {
                                                                    rowInvalid = true;
                                                                }
                                                                break;
                                                            case "not in":
                                                                multiSelectValues = new ArrayList<>();
                                                                for (String firstValue : firstValues) {
                                                                    multiSelectFilterValue = this.prepareUserMultiSelectString(new String[]{firstValue}, userCodeNameMap);
                                                                    multiSelectValues.add(multiSelectFilterValue);
                                                                }
                                                                isExists = false;
                                                                for (String multiSelectValue : multiSelectValues) {
                                                                    String receipientType = multiSelectValue.split(":")[0].trim();

                                                                    for (String dataValue : value.toString().split(";")) {
                                                                        if (receipientType.equals(dataValue.split(":")[0].trim())) {
                                                                            String[] dataValues = dataValue.split(":")[1].trim().split(",");
                                                                            List<String> newList = new ArrayList<>();
                                                                            for (String item : dataValues) {
                                                                                newList.add(item.trim());
                                                                            }
                                                                            if (newList.contains(multiSelectValue.split(":")[1].trim())) {
                                                                                isExists = true;
                                                                                break;
                                                                            }
                                                                        }
                                                                    }
                                                                    if (isExists) {
                                                                        break;
                                                                    }
                                                                }
                                                                if (isExists) {
                                                                    rowInvalid = true;
                                                                }
                                                                break; //optional
                                                            default:
                                                                break;//Optional
                                                            }
                                                    } else {
                                                        List<String> firstValues = new ArrayList<>();
                                                        String[] arrays = null;
                                                        if (operator.equals("in") || operator.equals("not in") || rbFields.getComponentType().equals(HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN)) {
                                                            arrays = filterValFirst.split(",");
                                                        } else {
                                                            arrays = new String[]{filterValFirst};
                                                        }
                                                        for (String array1 : arrays) {
                                                            firstValues.add(array1.trim().toLowerCase());
                                                        }
                                                        if (rbFields.getComponentType().equals(HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN)) {
                                                            List<String> multiValues = new ArrayList<>();
                                                            for (String s : value.toString().split(",")) {
                                                                multiValues.add(s.toLowerCase());
                                                            }
                                                            switch (operator) {
                                                                case "=":
                                                                    if (multiValues.size() != firstValues.size()) {
                                                                        rowInvalid = true;
                                                                        break;
                                                                    } else {
                                                                        int count = 0;
                                                                        for (String s : multiValues) {
                                                                            if (firstValues.contains(s)) {
                                                                                count++;
                                                                            }
                                                                        }
                                                                        if (count != multiValues.size()) {
                                                                            rowInvalid = true;
                                                                            break;
                                                                        }
                                                                    }
                                                                    break;
                                                                case "in":

                                                                    int count = 0;
                                                                    for (String s : multiValues) {
                                                                        if (firstValues.contains(s)) {
                                                                            count++;
                                                                            break;
                                                                        }
                                                                    }
                                                                    if (count == 0) {
                                                                        rowInvalid = true;
                                                                        break;
                                                                    }
                                                                    break; //optional
                                                                case "!=":
                                                                    int count1 = 0;
                                                                    for (String s : multiValues) {
                                                                        if (firstValues.contains(s)) {
                                                                            count1++;
                                                                        }
                                                                    }
                                                                    if (count1 == multiValues.size()) {
                                                                        rowInvalid = true;
                                                                        break;
                                                                    }
                                                                    break;
                                                                case "not in":
                                                                    int count2 = 0;
                                                                    for (String s : multiValues) {
                                                                        if (firstValues.contains(s)) {
                                                                            count2++;
                                                                            break;
                                                                        }
                                                                    }
                                                                    if (count2 > 0) {
                                                                        rowInvalid = true;
                                                                        break;
                                                                    }

                                                                    break; //optional
                                                                case "like":
                                                                    String firstValue = "%" + firstValues.get(0) + "%";
                                                                    firstValue = firstValue.replace("_", ".");
                                                                    firstValue = firstValue.replace("%", ".*?");
                                                                    int count3 = 0;
                                                                    for (String s : multiValues) {
                                                                        if (s.toString().toLowerCase().matches(firstValue)) {
                                                                            count3++;
                                                                            break;
                                                                        }
                                                                    }
                                                                    if (count3 == 0) {
                                                                        rowInvalid = true;
                                                                        break;
                                                                    }
                                                                    break;
                                                                default:
                                                                    break;//Optional
                                                            }
                                                        } else {
                                                            switch (operator) {
                                                                case "=":
                                                                case "in":
                                                                    if (!firstValues.contains(value.toString().toLowerCase())) {
                                                                        rowInvalid = true;
                                                                    }
                                                                    break; //optional
                                                                case "!=":
                                                                case "not in":
                                                                    if (firstValues.contains(value.toString().toLowerCase())) {
                                                                        rowInvalid = true;
                                                                    }
                                                                    break; //optional
                                                                case "like":
                                                                    String firstValue = "%" + firstValues.get(0) + "%";
                                                                    firstValue = firstValue.replace("_", ".");
                                                                    firstValue = firstValue.replace("%", ".*?");
                                                                    if (!value.toString().toLowerCase().matches(firstValue)) {
                                                                        rowInvalid = true;
                                                                    }
                                                                    break;
                                                                default:
                                                                    break;//Optional
                                                            }
                                                        }
                                                    }
                                                } else if (rbFields.getDataType().equals(HkSystemConstantUtil.CustomField.DataType.TIMESTAMP)) {
                                                    List<String> firstValues = new ArrayList<>();
                                                    String[] arrays = filterValFirst.split(",");
                                                    for (int j = 0; j < arrays.length; j++) {
                                                        firstValues.add(arrays[j].trim().substring(0, 10));
                                                    }
                                                    String firstDate = firstValues.get(0);
                                                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                                    Date first = simpleDateFormat.parse(firstDate);
                                                    Date second = null;
                                                    if (filterValSecond != null) {
                                                        second = simpleDateFormat.parse(filterValSecond.trim().substring(0, 10));
                                                    }
                                                    Date dbValue = null;
                                                    if (rbFields.getComponentType().equals(HkSystemConstantUtil.CustomField.ComponentType.DATE_RANGE)) {
                                                        String strgValue = (String) value;
                                                        Date fromDate = null;
                                                        Date toDate = null;
                                                        String[] strgValueList = strgValue.split("to");
                                                        if (!StringUtils.isEmpty(strgValueList[0]) && !StringUtils.isEmpty(strgValueList[1])) {
                                                            SimpleDateFormat simpleDateFormats = new SimpleDateFormat("dd/MM/yyyy");
                                                            fromDate = simpleDateFormats.parse(strgValueList[0].trim().substring(0, 10));
                                                            toDate = simpleDateFormats.parse(strgValueList[1].trim().substring(0, 10));
                                                        }
                                                        switch (operator) {
                                                            case "=":
                                                                if (fromDate.compareTo(first) > 0 || first.compareTo(toDate) > 0) {
                                                                    rowInvalid = true;
                                                                }
                                                                break;
                                                            case "!=":
                                                                if (fromDate.compareTo(first) <= 0 && first.compareTo(toDate) <= 0) {
                                                                    rowInvalid = true;
                                                                }
                                                                break;
                                                            default:
                                                                break;
                                                        }
                                                    } else {
                                                        SimpleDateFormat simpleDateFormats = new SimpleDateFormat("dd/MM/yyyy");
                                                        String stringDate = value.toString().trim().substring(0, 10);
                                                        dbValue = simpleDateFormats.parse(stringDate);
                                                        switch (operator) {
                                                            case "=":

                                                                if (!first.equals(dbValue)) {
                                                                    rowInvalid = true;
                                                                }
                                                                break; //optional
                                                            case "!=":
                                                                if (first.equals(dbValue)) {
                                                                    rowInvalid = true;
                                                                }
                                                                break; //optional
                                                            case ">":
                                                                if (first.before(dbValue)) {
                                                                    rowInvalid = true;
                                                                }
                                                                break;
                                                            case "<":
                                                                if (first.after(dbValue)) {
                                                                    rowInvalid = true;
                                                                }
                                                                break;
                                                            case ">=":
                                                                if (first.before(dbValue)) {
                                                                    rowInvalid = true;
                                                                }
                                                                break;
                                                            case "<=":
                                                                if (first.after(dbValue)) {
                                                                    rowInvalid = true;
                                                                }
                                                                break;
                                                            case "between":
                                                                if (second != null) {
                                                                    if (first.compareTo(dbValue) >= 0 && second.compareTo(dbValue) <= 0) {
                                                                        rowInvalid = true;
                                                                    }
                                                                }
                                                                break;
                                                            default:
                                                                break;
                                                        }
                                                    }

                                                } else if (rbFields.getDataType().equals(HkSystemConstantUtil.CustomField.DataType.BOOLEAN)) {
                                                    Boolean dbValue = null;
                                                    dbValue = Boolean.parseBoolean(value.toString());
                                                    Boolean firstValue = Boolean.parseBoolean(filterValFirst.trim());

                                                    if (operator.equals("=")) {
                                                        if (!firstValue.equals(dbValue)) {
                                                            rowInvalid = true;
                                                        }
                                                        break;
                                                    }
                                                }
                                            } else if (operator.equals("is null") || operator.equals("is not null")) {
                                                if (operator.equals("is null") && value != null) {
                                                    rowInvalid = true;
                                                    break;
                                                } else if (operator.equals("is not null") && value == null) {
                                                    rowInvalid = true;
                                                    break;
                                                }
                                            }
                                        }
                                        if (rowInvalid) {
                                            break;
                                        }
                                    }
                                }
                            } //Filtration for Fixed field with dropdown,multiselect component type.
                            else if (key.equals(reportFieldName) && rbFields.getDbBaseType().equals(HkSystemConstantUtil.DBBaseType.RELATIONAL_DB)
                                    && (CollectionUtils.isEmpty(fkConversionMap) || fkConversionMap.get(columnDefinition) == null)) {
                                if (!StringUtils.isEmpty(rbFields.getComponentType())
                                        && (rbFields.getComponentType().equals(HkSystemConstantUtil.CustomField.ComponentType.DROPDOWN)
                                        || rbFields.getComponentType().equals(HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN)
                                        || rbFields.getComponentType().equals(HkSystemConstantUtil.CustomField.ComponentType.USER_MULTISELECT)
                                        || rbFields.getComponentType().equals(HkSystemConstantUtil.CustomField.ComponentType.RADIO_BUTTON)
                                        || RbReportConstantUtils.GOAL_TEMPLATE_VALIDATION_SET.contains(columnDefinition)
                                        || RbReportConstantUtils.FIELDS_HANDLED_IN_TRANSFORMER.contains(columnDefinition))) {

                                    if (!StringUtils.isEmpty(rbFields.getFilter())) {
                                        String rbFilter = rbFields.getFilter();
                                        JSONArray array;
                                        array = new JSONArray(rbFilter);
                                        for (int i = 0; i < array.length(); i++) {
                                            JSONObject filterJson = array.getJSONObject(i);
                                            if (filterJson.has("filter")) {
                                                String operator = (String) filterJson.get("filter");
                                                if (value == null && !(operator.equals("is null") || operator.equals("is not null"))) {
                                                    rowInvalid = true;
                                                    break;
                                                }

                                                if (filterJson.has("filterValFirst") && !(operator.equals("is null") || operator.equals("is not null"))) {
                                                    String filterValFirst = filterJson.get("filterValFirst").toString();
                                                    if (filterJson.has("filterValSecond")) {
                                                        filterValSecond = filterJson.get("filterValSecond").toString();
                                                    }
                                                    if (rbFields.getDataType().equals(HkSystemConstantUtil.CustomField.DataType.VARCHAR)) {
                                                        if (rbFields.getComponentType().equals(HkSystemConstantUtil.CustomField.ComponentType.USER_MULTISELECT)) {
                                                            List<String> firstValues = new ArrayList<>();
                                                            String[] arrays = null;
                                                            arrays = filterValFirst.split(",");

                                                            for (String array1 : arrays) {
                                                                firstValues.add(array1.trim());
                                                            }
                                                            String multiSelectFilterValue = null;
                                                            List<String> multiSelectValues = null;
                                                            boolean isExists = false;
                                                            switch (operator) {
                                                                case "=":
                                                                    multiSelectValues = new ArrayList<>();
                                                                    for (String firstValue : firstValues) {
                                                                        multiSelectFilterValue = this.prepareUserMultiSelectString(new String[]{firstValue}, userCodeNameMap);
                                                                        multiSelectValues.add(multiSelectFilterValue);
                                                                    }

                                                                    for (String dataValue : value.toString().split(";")) {
                                                                        String[] dataValues = dataValue.split(":")[1].trim().split(",");
                                                                        List<String> newList = new ArrayList<>();
                                                                        for (String item : dataValues) {
                                                                            isExists = false;
                                                                            Iterator<String> itr = multiSelectValues.iterator();
                                                                            while (itr.hasNext()) {
                                                                                String multiSelectValue = itr.next();
                                                                                String receipientType = multiSelectValue.split(":")[0].trim();
                                                                                if (receipientType.equals(dataValue.split(":")[0].trim())) {

                                                                                    if (item.trim().equals(multiSelectValue.split(":")[1].trim())) {
                                                                                        isExists = true;
                                                                                        itr.remove();
                                                                                        break;
                                                                                    }
                                                                                }
                                                                            }
                                                                            if (!isExists) {
                                                                                break;
                                                                            }
                                                                        }
                                                                        if (!isExists) {
                                                                            break;
                                                                        }
                                                                    }
                                                                    if (!isExists) {
                                                                        rowInvalid = true;
                                                                    }
                                                                    if (multiSelectValues.size() > 0) {
                                                                        rowInvalid = true;
                                                                    }
                                                                    break;
                                                                case "in":
                                                                    multiSelectValues = new ArrayList<>();
                                                                    for (String firstValue : firstValues) {
                                                                        multiSelectFilterValue = this.prepareUserMultiSelectString(new String[]{firstValue}, userCodeNameMap);
                                                                        multiSelectValues.add(multiSelectFilterValue);
                                                                    }
                                                                    isExists = false;
                                                                    for (String multiSelectValue : multiSelectValues) {
                                                                        String receipientType = multiSelectValue.split(":")[0].trim();
                                                                        for (String dataValue : value.toString().split(";")) {
                                                                            if (receipientType.equals(dataValue.split(":")[0].trim())) {
                                                                                String[] dataValues = dataValue.split(":")[1].trim().split(",");
                                                                                List<String> newList = new ArrayList<>();
                                                                                for (String item : dataValues) {
                                                                                    newList.add(item.trim());
                                                                                }
                                                                                if (newList.contains(multiSelectValue.split(":")[1].trim())) {
                                                                                    isExists = true;
                                                                                    break;
                                                                                }
                                                                            }
                                                                        }
                                                                        if (isExists) {
                                                                            break;
                                                                        }
                                                                    }
                                                                    if (!isExists) {
                                                                        rowInvalid = true;
                                                                    }
                                                                    break; //optional
                                                                case "!=":
                                                                    multiSelectValues = new ArrayList<>();
                                                                    for (String firstValue : firstValues) {
                                                                        multiSelectFilterValue = this.prepareUserMultiSelectString(new String[]{firstValue}, userCodeNameMap);
                                                                        multiSelectValues.add(multiSelectFilterValue);
                                                                    }
                                                                    int fieldCount = 0;
                                                                    int filterFieldCount = multiSelectValues.size();
                                                                    for (String dataValue : value.toString().split(";")) {
                                                                        String[] dataValues = dataValue.split(":")[1].trim().split(",");
                                                                        List<String> newList = new ArrayList<>();
                                                                        for (String item : dataValues) {
                                                                            fieldCount++;
                                                                            Iterator<String> itr = multiSelectValues.iterator();
                                                                            while (itr.hasNext()) {
                                                                                String multiSelectValue = itr.next();
                                                                                String receipientType = multiSelectValue.split(":")[0].trim();
                                                                                if (receipientType.equals(dataValue.split(":")[0].trim())) {
                                                                                    if (item.trim().equals(multiSelectValue.split(":")[1].trim())) {
                                                                                        itr.remove();
                                                                                        break;
                                                                                    }
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                    if (filterFieldCount < fieldCount) {
                                                                        rowInvalid = false;
                                                                    } else if (multiSelectValues.isEmpty()) {
                                                                        rowInvalid = true;
                                                                    }
                                                                    break;
                                                                case "not in":
                                                                    multiSelectValues = new ArrayList<>();
                                                                    for (String firstValue : firstValues) {
                                                                        multiSelectFilterValue = this.prepareUserMultiSelectString(new String[]{firstValue}, userCodeNameMap);
                                                                        multiSelectValues.add(multiSelectFilterValue);
                                                                    }
                                                                    isExists = false;
                                                                    for (String multiSelectValue : multiSelectValues) {
                                                                        String receipientType = multiSelectValue.split(":")[0].trim();

                                                                        for (String dataValue : value.toString().split(";")) {
                                                                            if (receipientType.equals(dataValue.split(":")[0].trim())) {
                                                                                String[] dataValues = dataValue.split(":")[1].trim().split(",");
                                                                                List<String> newList = new ArrayList<>();
                                                                                for (String item : dataValues) {
                                                                                    newList.add(item.trim());
                                                                                }
                                                                                if (newList.contains(multiSelectValue.split(":")[1].trim())) {
                                                                                    isExists = true;
                                                                                    break;
                                                                                }
                                                                            }
                                                                        }
                                                                        if (isExists) {
                                                                            break;
                                                                        }
                                                                    }
                                                                    if (isExists) {
                                                                        rowInvalid = true;
                                                                    }
                                                                    break; //optional
                                                                default:
                                                                    break;//Optional
                                                                }
                                                        } else {
                                                            List<String> firstValues = new ArrayList<>();
                                                            String[] arrays = null;
                                                            if (operator.equals("in") || operator.equals("not in") || rbFields.getComponentType().equals(HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN)) {
                                                                arrays = filterValFirst.split(",");
                                                            } else {
                                                                arrays = new String[]{filterValFirst};
                                                            }
                                                            for (String array1 : arrays) {
                                                                firstValues.add(array1.trim().toLowerCase());
                                                            }
                                                            if (rbFields.getComponentType().equals(HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN)) {
                                                                List<String> multiValues = new ArrayList<>();
                                                                for (String s : value.toString().split(",")) {
                                                                    multiValues.add(s.toLowerCase());
                                                                }
                                                                switch (operator) {
                                                                    case "=":
                                                                        if (multiValues.size() != firstValues.size()) {
                                                                            rowInvalid = true;
                                                                            break;
                                                                        } else {
                                                                            int count = 0;
                                                                            for (String s : multiValues) {
                                                                                if (firstValues.contains(s)) {
                                                                                    count++;
                                                                                }
                                                                            }
                                                                            if (count != multiValues.size()) {
                                                                                rowInvalid = true;
                                                                                break;
                                                                            }
                                                                        }
                                                                        break;
                                                                    case "in":

                                                                        int count = 0;
                                                                        for (String s : multiValues) {
                                                                            if (firstValues.contains(s)) {
                                                                                count++;
                                                                                break;
                                                                            }
                                                                        }
                                                                        if (count == 0) {
                                                                            rowInvalid = true;
                                                                            break;
                                                                        }
                                                                        break; //optional
                                                                    case "!=":
                                                                        int count1 = 0;
                                                                        for (String s : multiValues) {
                                                                            if (firstValues.contains(s)) {
                                                                                count1++;
                                                                            }
                                                                        }
                                                                        if (count1 == multiValues.size()) {
                                                                            rowInvalid = true;
                                                                            break;
                                                                        }
                                                                        break;
                                                                    case "not in":
                                                                        int count2 = 0;
                                                                        for (String s : multiValues) {
                                                                            if (firstValues.contains(s)) {
                                                                                count2++;
                                                                                break;
                                                                            }
                                                                        }
                                                                        if (count2 > 0) {
                                                                            rowInvalid = true;
                                                                            break;
                                                                        }

                                                                        break; //optional
                                                                    case "like":
                                                                        String firstValue = "%" + firstValues.get(0) + "%";
                                                                        firstValue = firstValue.replace("_", ".");
                                                                        firstValue = firstValue.replace("%", ".*?");
                                                                        int count3 = 0;
                                                                        for (String s : multiValues) {
                                                                            if (s.toString().toLowerCase().matches(firstValue)) {
                                                                                count3++;
                                                                                break;
                                                                            }
                                                                        }
                                                                        if (count3 == 0) {
                                                                            rowInvalid = true;
                                                                            break;
                                                                        }
                                                                        break;
                                                                    default:
                                                                        break;//Optional
                                                                }
                                                            } else {
                                                                switch (operator) {
                                                                    case "=":
                                                                    case "in":
                                                                        if (!firstValues.contains(value.toString().toLowerCase())) {
                                                                            rowInvalid = true;
                                                                        }
                                                                        break; //optional
                                                                    case "!=":
                                                                    case "not in":
                                                                        if (firstValues.contains(value.toString().toLowerCase())) {
                                                                            rowInvalid = true;
                                                                        }
                                                                        break; //optional
                                                                    case "like":
                                                                        String firstValue = "%" + firstValues.get(0) + "%";
                                                                        firstValue = firstValue.replace("_", ".");
                                                                        firstValue = firstValue.replace("%", ".*?");
                                                                        if (!value.toString().toLowerCase().matches(firstValue)) {
                                                                            rowInvalid = true;
                                                                        }
                                                                        break;
                                                                    default:
                                                                        break;//Optional
                                                                }
                                                            }
                                                        }
                                                    }
                                                } else if (operator.equals("is null") || operator.equals("is not null")) {
                                                    if (operator.equals("is null") && value != null) {
                                                        rowInvalid = true;
                                                        break;
                                                    } else if (operator.equals("is not null") && value == null) {
                                                        rowInvalid = true;
                                                        break;
                                                    }
                                                }
                                            }
                                            if (rowInvalid) {
                                                break;
                                            }
                                        }
                                    }
                                }

                            } //Filtration for Mongodb General section and with component type of dropdown, muliselect etc.
                            else if (key.equals(reportFieldName) && rbFields.getDbBaseType().equals(HkSystemConstantUtil.DBBaseType.MONGO_DB) && (rbFields.getSectionName() == null || rbFields.getSectionName().equalsIgnoreCase("General"))) {
                                if (!StringUtils.isEmpty(rbFields.getComponentType())
                                        && (rbFields.getComponentType().equals(HkSystemConstantUtil.CustomField.ComponentType.DROPDOWN)
                                        || rbFields.getComponentType().equals(HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN)
                                        || rbFields.getComponentType().equals(HkSystemConstantUtil.CustomField.ComponentType.USER_MULTISELECT)
                                        || rbFields.getComponentType().equals(HkSystemConstantUtil.CustomField.ComponentType.RADIO_BUTTON)
                                        || RbReportConstantUtils.FIELDS_HANDLED_IN_TRANSFORMER.contains(columnDefinition))) {

                                    if (!StringUtils.isEmpty(rbFields.getFilter())) {
                                        String rbFilter = rbFields.getFilter();
                                        JSONArray array;
                                        array = new JSONArray(rbFilter);
                                        for (int i = 0; i < array.length(); i++) {
                                            JSONObject filterJson = array.getJSONObject(i);
                                            if (filterJson.has("filter")) {
                                                String operator = (String) filterJson.get("filter");
                                                if (value == null && !(operator.equals("is null") || operator.equals("is not null"))) {
                                                    rowInvalid = true;
                                                    break;
                                                }
                                                if (filterJson.has("filterValFirst") && !(operator.equals("is null") || operator.equals("is not null"))) {
                                                    String filterValFirst = filterJson.get("filterValFirst").toString();
                                                    if (filterJson.has("filterValSecond")) {
                                                        filterValSecond = filterJson.get("filterValSecond").toString();
                                                    }
                                                    if (rbFields.getDataType().equals(HkSystemConstantUtil.CustomField.DataType.VARCHAR)) {
                                                        if (rbFields.getComponentType().equals(HkSystemConstantUtil.CustomField.ComponentType.USER_MULTISELECT)) {
                                                            List<String> firstValues = new ArrayList<>();
                                                            String[] arrays = null;
                                                            arrays = filterValFirst.split(",");

                                                            for (String array1 : arrays) {
                                                                firstValues.add(array1.trim());
                                                            }
                                                            String multiSelectFilterValue = null;
                                                            List<String> multiSelectValues = null;
                                                            boolean isExists = false;
                                                            switch (operator) {
                                                                case "=":
                                                                    multiSelectValues = new ArrayList<>();
                                                                    for (String firstValue : firstValues) {
                                                                        multiSelectFilterValue = this.prepareUserMultiSelectString(new String[]{firstValue}, userCodeNameMap);
                                                                        multiSelectValues.add(multiSelectFilterValue);
                                                                    }

                                                                    for (String dataValue : value.toString().split(";")) {
                                                                        String[] dataValues = dataValue.split(":")[1].trim().split(",");
                                                                        List<String> newList = new ArrayList<>();
                                                                        for (String item : dataValues) {
                                                                            isExists = false;
                                                                            Iterator<String> itr = multiSelectValues.iterator();
                                                                            while (itr.hasNext()) {
                                                                                String multiSelectValue = itr.next();
                                                                                String receipientType = multiSelectValue.split(":")[0].trim();
                                                                                if (receipientType.equals(dataValue.split(":")[0].trim())) {

                                                                                    if (item.trim().equals(multiSelectValue.split(":")[1].trim())) {
                                                                                        isExists = true;
                                                                                        itr.remove();
                                                                                        break;
                                                                                    }
                                                                                }
                                                                            }
                                                                            if (!isExists) {
                                                                                break;
                                                                            }
                                                                        }
                                                                        if (!isExists) {
                                                                            break;
                                                                        }
                                                                    }
                                                                    if (!isExists) {
                                                                        rowInvalid = true;
                                                                    }
                                                                    if (multiSelectValues.size() > 0) {
                                                                        rowInvalid = true;
                                                                    }
                                                                    break;
                                                                case "in":
                                                                    multiSelectValues = new ArrayList<>();
                                                                    for (String firstValue : firstValues) {
                                                                        multiSelectFilterValue = this.prepareUserMultiSelectString(new String[]{firstValue}, userCodeNameMap);
                                                                        multiSelectValues.add(multiSelectFilterValue);
                                                                    }
                                                                    isExists = false;
                                                                    for (String multiSelectValue : multiSelectValues) {
                                                                        String receipientType = multiSelectValue.split(":")[0].trim();
                                                                        for (String dataValue : value.toString().split(";")) {
                                                                            if (receipientType.equals(dataValue.split(":")[0].trim())) {
                                                                                String[] dataValues = dataValue.split(":")[1].trim().split(",");
                                                                                List<String> newList = new ArrayList<>();
                                                                                for (String item : dataValues) {
                                                                                    newList.add(item.trim());
                                                                                }
                                                                                if (newList.contains(multiSelectValue.split(":")[1].trim())) {
                                                                                    isExists = true;
                                                                                    break;
                                                                                }
                                                                            }
                                                                        }
                                                                        if (isExists) {
                                                                            break;
                                                                        }
                                                                    }
                                                                    if (!isExists) {
                                                                        rowInvalid = true;
                                                                    }
                                                                    break; //optional
                                                                case "!=":
                                                                    multiSelectValues = new ArrayList<>();
                                                                    for (String firstValue : firstValues) {
                                                                        multiSelectFilterValue = this.prepareUserMultiSelectString(new String[]{firstValue}, userCodeNameMap);
                                                                        multiSelectValues.add(multiSelectFilterValue);
                                                                    }
                                                                    int fieldCount = 0;
                                                                    int filterFieldCount = multiSelectValues.size();
                                                                    for (String dataValue : value.toString().split(";")) {
                                                                        String[] dataValues = dataValue.split(":")[1].trim().split(",");
                                                                        List<String> newList = new ArrayList<>();
                                                                        for (String item : dataValues) {
                                                                            fieldCount++;
                                                                            Iterator<String> itr = multiSelectValues.iterator();
                                                                            while (itr.hasNext()) {
                                                                                String multiSelectValue = itr.next();
                                                                                String receipientType = multiSelectValue.split(":")[0].trim();
                                                                                if (receipientType.equals(dataValue.split(":")[0].trim())) {
                                                                                    if (item.trim().equals(multiSelectValue.split(":")[1].trim())) {
                                                                                        itr.remove();
                                                                                        break;
                                                                                    }
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                    if (filterFieldCount < fieldCount) {
                                                                        rowInvalid = false;
                                                                    } else if (multiSelectValues.isEmpty()) {
                                                                        rowInvalid = true;
                                                                    }
                                                                    break;
                                                                case "not in":
                                                                    multiSelectValues = new ArrayList<>();
                                                                    for (String firstValue : firstValues) {
                                                                        multiSelectFilterValue = this.prepareUserMultiSelectString(new String[]{firstValue}, userCodeNameMap);
                                                                        multiSelectValues.add(multiSelectFilterValue);
                                                                    }
                                                                    isExists = false;
                                                                    for (String multiSelectValue : multiSelectValues) {
                                                                        String receipientType = multiSelectValue.split(":")[0].trim();

                                                                        for (String dataValue : value.toString().split(";")) {
                                                                            if (receipientType.equals(dataValue.split(":")[0].trim())) {
                                                                                String[] dataValues = dataValue.split(":")[1].trim().split(",");
                                                                                List<String> newList = new ArrayList<>();
                                                                                for (String item : dataValues) {
                                                                                    newList.add(item.trim());
                                                                                }
                                                                                if (newList.contains(multiSelectValue.split(":")[1].trim())) {
                                                                                    isExists = true;
                                                                                    break;
                                                                                }
                                                                            }
                                                                        }
                                                                        if (isExists) {
                                                                            break;
                                                                        }
                                                                    }
                                                                    if (isExists) {
                                                                        rowInvalid = true;
                                                                    }
                                                                    break; //optional
                                                                default:
                                                                    break;//Optional
                                                                }
                                                        } else {
                                                            List<String> firstValues = new ArrayList<>();
                                                            String[] arrays = null;
                                                            if (operator.equals("in") || operator.equals("not in") || rbFields.getComponentType().equals(HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN)) {
                                                                arrays = filterValFirst.split(",");
                                                            } else {
                                                                arrays = new String[]{filterValFirst};
                                                            }
                                                            for (String array1 : arrays) {
                                                                firstValues.add(array1.trim().toLowerCase());
                                                            }
                                                            if (rbFields.getComponentType().equals(HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN)) {
                                                                List<String> multiValues = new ArrayList<>();
                                                                for (String s : value.toString().split(",")) {
                                                                    multiValues.add(s.toLowerCase());
                                                                }
                                                                switch (operator) {
                                                                    case "=":
                                                                        if (multiValues.size() != firstValues.size()) {
                                                                            rowInvalid = true;
                                                                            break;
                                                                        } else {
                                                                            int count = 0;
                                                                            for (String s : multiValues) {
                                                                                if (firstValues.contains(s)) {
                                                                                    count++;
                                                                                }
                                                                            }
                                                                            if (count != multiValues.size()) {
                                                                                rowInvalid = true;
                                                                                break;
                                                                            }
                                                                        }
                                                                        break;
                                                                    case "in":

                                                                        int count = 0;
                                                                        for (String s : multiValues) {
                                                                            if (firstValues.contains(s)) {
                                                                                count++;
                                                                                break;
                                                                            }
                                                                        }
                                                                        if (count == 0) {
                                                                            rowInvalid = true;
                                                                            break;
                                                                        }
                                                                        break; //optional
                                                                    case "!=":
                                                                        int count1 = 0;
                                                                        for (String s : multiValues) {
                                                                            if (firstValues.contains(s)) {
                                                                                count1++;
                                                                            }
                                                                        }
                                                                        if (count1 == multiValues.size()) {
                                                                            rowInvalid = true;
                                                                            break;
                                                                        }
                                                                        break;
                                                                    case "not in":
                                                                        int count2 = 0;
                                                                        for (String s : multiValues) {
                                                                            if (firstValues.contains(s)) {
                                                                                count2++;
                                                                                break;
                                                                            }
                                                                        }
                                                                        if (count2 > 0) {
                                                                            rowInvalid = true;
                                                                            break;
                                                                        }

                                                                        break; //optional
                                                                    case "like":
                                                                        String firstValue = "%" + firstValues.get(0) + "%";
                                                                        firstValue = firstValue.replace("_", ".");
                                                                        firstValue = firstValue.replace("%", ".*?");
                                                                        int count3 = 0;
                                                                        for (String s : multiValues) {
                                                                            if (s.toString().toLowerCase().matches(firstValue)) {
                                                                                count3++;
                                                                                break;
                                                                            }
                                                                        }
                                                                        if (count3 == 0) {
                                                                            rowInvalid = true;
                                                                            break;
                                                                        }
                                                                        break;
                                                                    default:
                                                                        break;//Optional
                                                                }
                                                            } else {
                                                                switch (operator) {
                                                                    case "=":
                                                                    case "in":
                                                                        if (!firstValues.contains(value.toString().toLowerCase())) {
                                                                            rowInvalid = true;
                                                                        }
                                                                        break; //optional
                                                                    case "!=":
                                                                    case "not in":
                                                                        if (firstValues.contains(value.toString().toLowerCase())) {
                                                                            rowInvalid = true;
                                                                        }
                                                                        break; //optional
                                                                    case "like":
                                                                        String firstValue = "%" + firstValues.get(0) + "%";
                                                                        firstValue = firstValue.replace("_", ".");
                                                                        firstValue = firstValue.replace("%", ".*?");
                                                                        if (!value.toString().toLowerCase().matches(firstValue)) {
                                                                            rowInvalid = true;
                                                                        }
                                                                        break;
                                                                    default:
                                                                        break;//Optional
                                                                }
                                                            }
                                                        }
                                                    }
                                                } else if (operator.equals("is null") || operator.equals("is not null")) {
                                                    if (operator.equals("is null") && value != null) {
                                                        rowInvalid = true;
                                                        break;
                                                    } else if (operator.equals("is not null") && value == null) {
                                                        rowInvalid = true;
                                                        break;
                                                    }
                                                }
                                            }
                                            if (rowInvalid) {
                                                break;
                                            }
                                        }
                                    }
                                }
                            }
                            if (rowInvalid) {
                                break;
                            }
                        }
                        if (rowInvalid) {
                            break;
                        }
                    }
                }
                if (rowInvalid) {
                    iterator.remove();
                }
            }
        }
        List<LinkedHashMap<String, Object>> recordList = (ArrayList<LinkedHashMap<String, Object>>) columnValuesMap.get("records");
        List<RbFieldDataBean> columns = masterReportDataBean.getColumns();
        Set<RbFieldDataBean> dbOrderColumns = new HashSet<>();
        List<String> orderColumns = new LinkedList<>();
        List<String> finalOrderColumnNames = new LinkedList<>();
        List<String> orderTypes = new LinkedList<>();
        Type orderCollectionType = new TypeToken<List<Map<String, Object>>>() {
        }.getType();
        List<Map<String, Object>> orderAttributeMapList = new Gson().fromJson(masterReportDataBean.getOrderAttributes(), orderCollectionType);
        if (!CollectionUtils.isEmpty(orderAttributeMapList)) {
//            System.out.println("orderAttributeMapList:" + orderAttributeMapList);
            Collections.sort(orderAttributeMapList, new Comparator() {
                @Override
                public int compare(Object o1, Object o2) {
                    Map<String, Object> m1 = (Map<String, Object>) o1;
                    Map<String, Object> m2 = (Map<String, Object>) o2;
                    if (m1.get("sequence") == null) {
                        return (m2.get("sequence") == null) ? 0 : -1;
                    }
                    if (m2.get("sequence") == null) {
                        return 1;
                    }
                    Double first = Double.parseDouble(m1.get("sequence").toString());
                    Double second = Double.parseDouble(m2.get("sequence").toString());
                    return first.compareTo(second);
                }
            });
            List<Map<String, Object>> finalMapList = new LinkedList<>();
            finalMapList.addAll(orderAttributeMapList);
            for (Map<String, Object> orderAttributeMap : orderAttributeMapList) {
                String columnName = orderAttributeMap.get("columnName").toString();
                orderColumns.add(columnName);
                orderTypes.add(orderAttributeMap.get("orderValue").toString());
            }

            if (!CollectionUtils.isEmpty(orderColumns)) {
                for (String orderColumn : orderColumns) {
                    String[] split = orderColumn.split("\\.");
                    if (split.length >= 2) {
                        String dbBaseName = split[0];
                        String dbFieldName = split[1];
                        for (RbFieldDataBean rbFieldDataBean : columns) {
                            if (dbBaseName.equals(rbFieldDataBean.getDbBaseName()) && dbFieldName.equals(rbFieldDataBean.getColName())) {
                                dbOrderColumns.add(rbFieldDataBean);
                                finalOrderColumnNames.add(rbFieldDataBean.getAlias());
                            }
                        }
                    }

                }
            }
            for (int i = 0; i < finalMapList.size(); i++) {
                if (finalMapList.get(i) != null) {
                    finalMapList.get(i).put("columnName", finalOrderColumnNames.get(i));
                }
            }
            Collections.sort(recordList, new Comparator() {
                @Override
                public int compare(Object o1, Object o2) {
                    Map<String, Object> m1 = (Map<String, Object>) o1;
                    Map<String, Object> m2 = (Map<String, Object>) o2;
                    int sizeCmp = 0;
                    for (int i = 0; i < finalMapList.size(); i++) {
                        if (finalMapList.get(i).get("orderValue").equals("asc")) {
                            if (m1.get(finalMapList.get(i).get("columnName")) == null) {
                                if (m2.get(finalMapList.get(i).get("columnName")) == null) {
                                    return 0; //equal
                                } else {
                                    return -1; // null is before other strings
                                }
                            } else if (m2.get(finalMapList.get(i).get("columnName")) == null) {
                                return 1;  // all other strings are after null
                            } else {
                                for (RbFieldDataBean rbFieldDataBean : dbOrderColumns) {
                                    if (rbFieldDataBean.getAlias().equals(finalMapList.get(i).get("columnName"))) {
                                        System.out.println("rbFieldDataBean.getAlias()" + rbFieldDataBean);
                                        if (rbFieldDataBean.getDataType().equals("varchar")) {
                                            return m1.get(finalMapList.get(i).get("columnName")).toString().toLowerCase().compareTo(m2.get(finalMapList.get(i).get("columnName")).toString().toLowerCase());
                                        } else if (rbFieldDataBean.getDataType().equals("int8")) {
                                            Long first = Long.parseLong(m1.get(finalMapList.get(i).get("columnName").toString()).toString());
                                            Long second = Long.parseLong(m2.get(finalMapList.get(i).get("columnName").toString()).toString());
                                            return first.compareTo(second);
                                        } else if (rbFieldDataBean.getDataType().equals("double precision")) {
                                            System.out.println("inside double");
                                            Double first = Double.parseDouble(m1.get(finalMapList.get(i).get("columnName").toString()).toString());
                                            Double second = Double.parseDouble(m2.get(finalMapList.get(i).get("columnName").toString()).toString());
                                            return first.compareTo(second);
                                        } else if (rbFieldDataBean.getDataType().equals("timestamp")) {
                                            SimpleDateFormat simpleDateFormats = new SimpleDateFormat("dd/MM/yyyy");
                                            Date first = null;
                                            try {
                                                first = simpleDateFormats.parse(m1.get(finalMapList.get(i).get("columnName").toString()).toString().trim().substring(0, 10));

                                            } catch (ParseException ex) {
                                                Logger.getLogger(ReportBuilderTransformerBean.class
                                                        .getName()).log(Level.SEVERE, null, ex);
                                            }
                                            Date second = null;
                                            try {
                                                second = simpleDateFormats.parse(m2.get(finalMapList.get(i).get("columnName").toString()).toString().trim().substring(0, 10));

                                            } catch (ParseException ex) {
                                                Logger.getLogger(ReportBuilderTransformerBean.class
                                                        .getName()).log(Level.SEVERE, null, ex);
                                            }
                                            return first.compareTo(second);
                                        }
                                    }
                                }

                            }
                        } else {
                            if (m2.get(finalMapList.get(i).get("columnName")) == null) {
                                if (m1.get(finalMapList.get(i).get("columnName")) == null) {
                                    return 0; //equal
                                } else {
                                    return -1; // null is before other strings
                                }
                            } else // this.member != null
                            if (m1.get(finalMapList.get(i).get("columnName")) == null) {
                                return 1;  // all other strings are after null
                            } else {
                                for (RbFieldDataBean rbFieldDataBean : dbOrderColumns) {
                                    if (rbFieldDataBean.getAlias().equals(finalMapList.get(i).get("columnName"))) {
                                        if (rbFieldDataBean.getDataType().equals("varchar")) {
                                            sizeCmp = m2.get(finalMapList.get(i).get("columnName")).toString().toLowerCase().compareTo(m1.get(finalMapList.get(i).get("columnName")).toString().toLowerCase());
                                        } else if (rbFieldDataBean.getDataType().equals("int8")) {
                                            Long first = Long.parseLong(m2.get(finalMapList.get(i).get("columnName").toString()).toString());
                                            Long second = Long.parseLong(m1.get(finalMapList.get(i).get("columnName").toString()).toString());
                                            sizeCmp = first.compareTo(second);
                                        } else if (rbFieldDataBean.getDataType().equals("double precision")) {
                                            Double first = Double.parseDouble(m2.get(finalMapList.get(i).get("columnName").toString()).toString());
                                            Double second = Double.parseDouble(m1.get(finalMapList.get(i).get("columnName").toString()).toString());
                                            sizeCmp = first.compareTo(second);
                                        } else if (rbFieldDataBean.getDataType().equals("timestamp")) {
                                            SimpleDateFormat simpleDateFormats = new SimpleDateFormat("dd/MM/yyyy");
                                            Date first = null;
                                            try {
                                                first = simpleDateFormats.parse(m2.get(finalMapList.get(i).get("columnName").toString()).toString().trim().substring(0, 10));

                                            } catch (ParseException ex) {
                                                Logger.getLogger(ReportBuilderTransformerBean.class
                                                        .getName()).log(Level.SEVERE, null, ex);
                                            }
                                            Date second = null;
                                            try {
                                                second = simpleDateFormats.parse(m1.get(finalMapList.get(i).get("columnName").toString()).toString().trim().substring(0, 10));

                                            } catch (ParseException ex) {
                                                Logger.getLogger(ReportBuilderTransformerBean.class
                                                        .getName()).log(Level.SEVERE, null, ex);
                                            }
                                            return first.compareTo(second);
                                        }
                                    }

                                }

                            }
                        }
                        if (sizeCmp != 0) {
                            return sizeCmp;
                        }
                    }
                    return sizeCmp;
                }
            });
        }
        //System.out.println("recordList :" + recordList);
        Iterator<LinkedHashMap<String, Object>> iterator = recordList.iterator();

        while (iterator.hasNext()) {
            LinkedHashMap<String, Object> columnValueMap = iterator.next();
            int lastCount = 0;
            if (!CollectionUtils.isEmpty(columnValueMap)) {
                for (Map.Entry<String, Object> valueMap : columnValueMap.entrySet()) {
                    String key = valueMap.getKey();
                    Object value = valueMap.getValue();
                    if (value != null) {
                        lastCount++;
                    }
                }
            }
            if (lastCount == 0) {
                iterator.remove();
            }
        }
        Boolean showCurrencyField = this.retrieveCurrencyConfiguration();
        Boolean viewCurrencyDataPermission = this.retrieveViewCurrencyDataRightsOfLoggedInUser();
        if (showCurrencyField != null && viewCurrencyDataPermission != null && (!showCurrencyField || (showCurrencyField && !viewCurrencyDataPermission))) {
            columnValuesMap = this.removeCurrencyFields(columnValuesMap, masterReportDataBean);
        }
        List<LinkedHashMap<String, Object>> recordListForSettingOffset = (ArrayList<LinkedHashMap<String, Object>>) columnValuesMap.get("records");

        //**********************  MITAL: JAVA-8 MUST BE SUPPORTED TO UNCOMMENT THIS CODE  **************************
        List<LinkedHashMap<String, Object>> list = new ArrayList<>();
        Type collectionType = new TypeToken<Map<String, Object>>() {
        }.getType();
        Map<String, Object> groupByColumnMap = new Gson().fromJson(masterReportDataBean.getGroupAttributes(), collectionType);
        List<String> groupByColumns = new ArrayList<>();
        String[] levelOneGroupByColumns = null;
        if (!CollectionUtils.isEmpty(groupByColumnMap) && groupByColumnMap.get("groupBy") != null) {
            List<Map<String, Object>> multipleGroupByList = (List<Map<String, Object>>) groupByColumnMap.get("groupBy");
            if (!CollectionUtils.isEmpty(multipleGroupByList)) {
                for (Map<String, Object> multipleGroupByMapObj : multipleGroupByList) {
                    Double level = Double.parseDouble(multipleGroupByMapObj.get("level").toString());
                    if (level.longValue() == 1l) {
                        levelOneGroupByColumns = multipleGroupByMapObj.get("fields").toString().trim().split(",");
                    }
//                    System.out.println("forPdf :" + forPdf);
                    if (forPdf) {
                        if (multipleGroupByMapObj != null) {
                            String groupByFields = multipleGroupByMapObj.get("fields").toString();
                            if (!StringUtils.isEmpty(groupByFields)) {
                                String[] split = groupByFields.split(",");
                                for (String split1 : split) {
                                    groupByColumns.add(split1);
                                }

                            }
                        }
                    } else {
                        groupByColumns = new ArrayList<>(Arrays.asList(levelOneGroupByColumns));
                    }

                }
            }
            if (showCurrencyField != null && !showCurrencyField) {
                if (!CollectionUtils.isEmpty(groupByColumns) && masterReportDataBean != null && !CollectionUtils.isEmpty(masterReportDataBean.getColumns())) {
                    Iterator<String> groupByColumnIterator = groupByColumns.iterator();
                    while (groupByColumnIterator.hasNext()) {
                        Boolean rowInvalid = false;
                        String groupByColumn = groupByColumnIterator.next();
                        for (RbFieldDataBean rbFieldDataBean : masterReportDataBean.getColumns()) {
                            String reportFieldName = (StringUtils.isEmpty(rbFieldDataBean.getAlias())
                                    ? rbFieldDataBean.getFieldLabel() : rbFieldDataBean.getAlias());
                            if (groupByColumn.equals(reportFieldName) && rbFieldDataBean.getComponentType().equals(HkSystemConstantUtil.CustomField.ComponentType.CURRENCY)) {
                                rowInvalid = true;
                                break;
                            }
                        }
                        if (rowInvalid) {
                            groupByColumnIterator.remove();
                        }
                    }
                }
                //System.out.println("groupByColumnIterator ::::::: in removeing group by::::" + groupByColumns);
            }
            Map<GroupingKey, List<Object>> groupRecords = new HashMap<>();
            if (forPdf) {
                groupRecords = this.groupRecords(recordListForSettingOffset, groupByColumns);
            } else {
                groupRecords = this.groupRecords(recordListForSettingOffset, groupByColumns);
            }
            //For empty result set
//            if (!CollectionUtils.isEmpty(groupRecords)) {
            Map<String, List<Object>> newGroupedMap = new LinkedHashMap<>();
                for (Map.Entry<GroupingKey, List<Object>> entrySet : groupRecords.entrySet()) {
                    GroupingKey key = entrySet.getKey();
                ArrayList<Object> keys = key.getKeys();
                String finalKey = null;
                for (Object item : keys) {
                    if (finalKey == null) {
                        if (item != null) {
                            finalKey = item.toString().trim();
                        } else {
                            finalKey = "NA";
                        }
                    } else {
                        if (item != null) {
                            finalKey += "," + item.toString().trim();
                        } else {
                            finalKey += "," + "NA";
                        }

                    }
                }
                List<Object> value = entrySet.getValue();
                newGroupedMap.put(finalKey, value);
            }
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("records", newGroupedMap);
            resultMap.put("totalRecords", newGroupedMap.size());
            return resultMap;
//            }
        }
        //**********************  MITAL: JAVA-8 MUST BE SUPPORTED TO UNCOMMENT THIS CODE  **************************
        return columnValuesMap;
    }

    public Map<String, Object> removeCurrencyFields(Map<String, Object> columnValuesMap, MasterReportDataBean masterReportDataBean) {
        if (!CollectionUtils.isEmpty(columnValuesMap) && masterReportDataBean != null && !CollectionUtils.isEmpty(masterReportDataBean.getColumns())) {
            List<LinkedHashMap<String, Object>> recordsList = (ArrayList<LinkedHashMap<String, Object>>) columnValuesMap.get("records");
            if (!CollectionUtils.isEmpty(recordsList)) {
                Iterator<LinkedHashMap<String, Object>> iterator = recordsList.iterator();
                while (iterator.hasNext()) {
                    LinkedHashMap<String, Object> dataMap = iterator.next();

                    Iterator<Map.Entry<String, Object>> iter = dataMap.entrySet().iterator();
                    while (iter.hasNext()) {
                        Map.Entry<String, Object> valueMap = iter.next();
                        String key = valueMap.getKey();
                        Object value = valueMap.getValue();
                        for (RbFieldDataBean rbFieldDataBean : masterReportDataBean.getColumns()) {

                            String reportFieldName = (StringUtils.isEmpty(rbFieldDataBean.getAlias())
                                    ? rbFieldDataBean.getFieldLabel() : rbFieldDataBean.getAlias());
                            if (key.equals(reportFieldName) && rbFieldDataBean.getComponentType().equals(HkSystemConstantUtil.CustomField.ComponentType.CURRENCY)) {
                                iter.remove();
                            }
                        }
                    }
                }

            }
        }
        System.out.println("columnValuesMap  ::::: in remove method :::::" + columnValuesMap);
        return columnValuesMap;
    }

    /**
     * ******************** SHREYA: JAVA-8 MUST BE SUPPORTED TO UNCOMMENT THIS
     * CODE ************************* This method is for preparing the data for
     * the pdf for which the grouping is applied
     */
    public List<Map<String, Object>> retrieveGroupedDataForReport(Map<String, List<Object>> groupRecords, List<String> groupByColumns, List<String> groupedWithoutArray, List<Map<String, Object>> orderByColumns) {
//        System.out.println("groupRecords :" + groupRecords);
        List<Map<String, Object>> finalListOfData = new LinkedList<>();
        if (groupRecords != null) {
            for (Map.Entry<String, List<Object>> entrySet : groupRecords.entrySet()) {
                List<Object> value = entrySet.getValue();
                for (Object value1 : value) {
                    Map<String, Object> map = new LinkedHashMap<>((Map<String, Object>) value1);
                    finalListOfData.add(map);
                }

            }
        }
        System.out.println("groupByColumns :" + groupedWithoutArray);
        if (!CollectionUtils.isEmpty(groupedWithoutArray)) {
            for (String groupByColumn : groupedWithoutArray) {
                for (Map<String, Object> finalListOfData1 : finalListOfData) {
                    if (finalListOfData1.containsKey(groupByColumn)) {
                        finalListOfData1.put(groupByColumn + "1", finalListOfData1.get(groupByColumn));
                    }
                }
            }
        }
        System.out.println("finalListOfData :" + finalListOfData);
        Collections.sort(finalListOfData, new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                Map<String, Object> m1 = (Map<String, Object>) o1;
                Map<String, Object> m2 = (Map<String, Object>) o2;
                int sizeCmp = 0;
                for (int i = 0; i < orderByColumns.size(); i++) {
                    if (orderByColumns.get(i).get("orderValue").equals("asc")) {
                        if (m1.get(orderByColumns.get(i).get("columnName")) == null) {
                            return (m2.get(orderByColumns.get(i).get("columnName")) == null) ? 0 : -1;
                        }
                        if (m2.get(orderByColumns.get(i).get("columnName")) == null) {
                            return 1;
                        }
                        switch (orderByColumns.get(i).get("columnName").getClass().getSimpleName()) {
                            case "String":
                                sizeCmp = m1.get(orderByColumns.get(i).get("columnName")).toString().toLowerCase().compareTo(m2.get(orderByColumns.get(i).get("columnName")).toString().toLowerCase());
                                break;
                            case "Long": {
                                Long first = Long.parseLong(m1.get(orderByColumns.get(i).get("columnName").toString()).toString());
                                Long second = Long.parseLong(m2.get(orderByColumns.get(i).get("columnName").toString()).toString());
                                sizeCmp = first.compareTo(second);
                                break;
                            }
                            case "Double": {
                                Double first = Double.parseDouble(m1.get(orderByColumns.get(i).get("columnName").toString()).toString());
                                Double second = Double.parseDouble(m2.get(orderByColumns.get(i).get("columnName").toString()).toString());
                                sizeCmp = first.compareTo(second);
                                break;
                            }
                            case "Date": {
                                SimpleDateFormat simpleDateFormats = new SimpleDateFormat("dd/MM/yyyy");
                                Date first = null;
                                try {
                                    first = simpleDateFormats.parse(m1.get(orderByColumns.get(i).get("columnName").toString()).toString().trim().substring(0, 10));
                                } catch (ParseException ex) {
                                    Logger.getLogger(ReportBuilderTransformerBean.class.getName()).log(Level.SEVERE, null, ex);
                                }
                                Date second = null;
                                try {
                                    second = simpleDateFormats.parse(m2.get(orderByColumns.get(i).get("columnName").toString()).toString().trim().substring(0, 10));
                                } catch (ParseException ex) {
                                    Logger.getLogger(ReportBuilderTransformerBean.class.getName()).log(Level.SEVERE, null, ex);
                                }
                                return first.compareTo(second);
                            }
                        }
                    } else {
                        if (m2.get(orderByColumns.get(i).get("columnName")) == null) {
                            return (m1.get(orderByColumns.get(i).get("columnName")) == null) ? 0 : -1;
                        }
                        if (m1.get(orderByColumns.get(i).get("columnName")) == null) {
                            return 1;
                        }
                        switch (orderByColumns.get(i).get("columnName").getClass().getSimpleName()) {
                            case "String":
                                sizeCmp = m2.get(orderByColumns.get(i).get("columnName")).toString().toLowerCase().compareTo(m1.get(orderByColumns.get(i).get("columnName")).toString().toLowerCase());
                                break;
                            case "Long": {
                                Long first = Long.parseLong(m2.get(orderByColumns.get(i).get("columnName").toString()).toString());
                                Long second = Long.parseLong(m1.get(orderByColumns.get(i).get("columnName").toString()).toString());
                                sizeCmp = first.compareTo(second);
                                break;
                            }
                            case "Double": {
                                Double first = Double.parseDouble(m2.get(orderByColumns.get(i).get("columnName").toString()).toString());
                                Double second = Double.parseDouble(m1.get(orderByColumns.get(i).get("columnName").toString()).toString());
                                sizeCmp = first.compareTo(second);
                                break;
                            }
                            case "Date": {
                                SimpleDateFormat simpleDateFormats = new SimpleDateFormat("dd/MM/yyyy");
                                Date first = null;
                                try {
                                    first = simpleDateFormats.parse(m2.get(orderByColumns.get(i).get("columnName").toString()).toString().trim().substring(0, 10));
                                } catch (ParseException ex) {
                                    Logger.getLogger(ReportBuilderTransformerBean.class.getName()).log(Level.SEVERE, null, ex);
                                }
                                Date second = null;
                                try {
                                    second = simpleDateFormats.parse(m1.get(orderByColumns.get(i).get("columnName").toString()).toString().trim().substring(0, 10));
                                } catch (ParseException ex) {
                                    Logger.getLogger(ReportBuilderTransformerBean.class.getName()).log(Level.SEVERE, null, ex);
                                }
                                return first.compareTo(second);
                            }
                        }
                    }
                    if (sizeCmp != 0) {
                        return sizeCmp;
                    }
                }
                return sizeCmp;
            }
        });
        Map<String, Object> pivotalRecord = new HashMap<>(finalListOfData.get(0));
        for (int i = 1; i < finalListOfData.size(); i++) {
            Map<String, Object> tempRecord = new HashMap<>(finalListOfData.get(i));
            this.applyGrouping(finalListOfData.get(i), pivotalRecord, groupByColumns, 0);
            pivotalRecord = tempRecord;
        }
        System.out.println("finalListOfData::::: final :::::" + finalListOfData);
        return finalListOfData;
    }

    public void applyGrouping(Map<String, Object> newRecord, Map<String, Object> pivotalRecord, List<String> groups, int index) {
//        System.out.println("groups ::::" + groups);
        if (index < groups.size()) {
            String groupingRecord = groups.get(index);
            List<String> group = new ArrayList<>();
            String[] split = groupingRecord.split(",");
            for (String split1 : split) {
                group.add(split1);
            }
//            System.out.println("group ::::" + group);
//            System.out.println("newRecord ::::" + newRecord);
//            System.out.println("pivotalRecord ::::" + pivotalRecord);
            Boolean blankFlag = false;
            for (String groupField : group) {
                if (newRecord.get(groupField) != null && !(newRecord.get(groupField).equals(pivotalRecord.get(groupField))) && newRecord.get(groupField) != "") {
                    blankFlag = true;
                    break;
                }
            }
//            System.out.println("blankFlag :::::" + blankFlag);
//            System.out.println("group :::: " + group);
//            System.out.println("newRecord :::" + newRecord);
            if (!blankFlag) {
                for (String groupFields : group) {
//                    System.out.println("groupFields ::::" + groupFields);
//                    System.out.println("newRecord.get(groupFields) :::::::" + newRecord.get(groupFields));
                    if (newRecord.get(groupFields) != null && newRecord.get(groupFields) != "") {
//                        System.out.println("newRecord.get(groupFields) ::::" + newRecord.get(groupFields));
                        newRecord.put(groupFields, "");
                    }
                }
//                System.out.println("newRecord aftr:::::" + newRecord);
                index = index + 1;
                this.applyGrouping(newRecord, pivotalRecord, groups, index);
            }
        }
    }

    private RbReportField convertReportFieldDataBeanToReportFieldModel(RbFieldDataBean fieldDataBean, RbReportField rbReportField) {
//        if (!StringUtils.isEmpty(fieldDataBean.getAlias())) {
        rbReportField.setReportFieldName(fieldDataBean.getAlias());
//        }
        rbReportField.setLastModifiedBy(userDataBean.getId());
        rbReportField.setLastModifiedOn(Calendar.getInstance().getTime());
        if (fieldDataBean.isColI18nRequired() != null) {
            rbReportField.setColI18nReq(fieldDataBean.isColI18nRequired());
        } else {
            rbReportField.setColI18nReq(Boolean.FALSE);
        }
        rbReportField.setFilterAttributes(fieldDataBean.getFilter());
        rbReportField.setJoinAttributes(fieldDataBean.getJoinAttributes());
        rbReportField.setOrderType(fieldDataBean.getOrderType());
        rbReportField.setIsArchive(Boolean.FALSE);
        rbReportField.setIsDefaultVisible(Boolean.TRUE);
        rbReportField.setRowI18nReq(Boolean.TRUE);
        if (fieldDataBean.getFieldSequence() != null) {
            rbReportField.setFieldSequence(fieldDataBean.getFieldSequence());
        }
        rbReportField.setDbFieldName(fieldDataBean.getColName());
        rbReportField.setFieldDataType(fieldDataBean.getDataType());
        rbReportField.setDbBaseName(fieldDataBean.getDbBaseName());
        rbReportField.setDbBaseType(fieldDataBean.getDbBaseType());
        rbReportField.setFeatureId(fieldDataBean.getFeature());
        rbReportField.setFieldLabel(fieldDataBean.getFieldLabel());
        Map<RbReportConstantUtils.CustomAttribute, Object> showTotalMap = new HashMap<>();
        if (fieldDataBean.getShowTotal() == null || !fieldDataBean.getShowTotal()) {
            showTotalMap.put(RbReportConstantUtils.CustomAttribute.SHOW_TOTAL, Boolean.FALSE);
        } else {
            showTotalMap.put(RbReportConstantUtils.CustomAttribute.SHOW_TOTAL, Boolean.TRUE);
        }
        if (StringUtils.isEmpty(fieldDataBean.getSectionName()) || fieldDataBean.getSectionName().equalsIgnoreCase("General")) {
            showTotalMap.put(RbReportConstantUtils.CustomAttribute.SECTION_NAME, null);
        } else {
            showTotalMap.put(RbReportConstantUtils.CustomAttribute.SECTION_NAME, fieldDataBean.getSectionName());
        }
        if (StringUtils.isEmpty(fieldDataBean.getComponentType())) {
            showTotalMap.put(RbReportConstantUtils.CustomAttribute.COMPONENT_TYPE, null);
        } else {
            showTotalMap.put(RbReportConstantUtils.CustomAttribute.COMPONENT_TYPE, fieldDataBean.getComponentType());
        }
        if (StringUtils.isEmpty(fieldDataBean.getHkFieldId())) {
            showTotalMap.put(RbReportConstantUtils.CustomAttribute.HK_FIELD_ID, null);
        } else {
            showTotalMap.put(RbReportConstantUtils.CustomAttribute.HK_FIELD_ID, fieldDataBean.getHkFieldId());
        }
        if (StringUtils.isEmpty(fieldDataBean.getMasterCode())) {
            showTotalMap.put(RbReportConstantUtils.CustomAttribute.MASTER_CODE, null);
        } else {
            showTotalMap.put(RbReportConstantUtils.CustomAttribute.MASTER_CODE, fieldDataBean.getMasterCode());
        }
        if (fieldDataBean.getIsDff() != null && fieldDataBean.getIsDff()) {
            showTotalMap.put(RbReportConstantUtils.CustomAttribute.IS_DFF, Boolean.TRUE);
        } else {
            showTotalMap.put(RbReportConstantUtils.CustomAttribute.IS_DFF, Boolean.FALSE);
        }
        if (fieldDataBean.isIncludeTime() != null && fieldDataBean.isIncludeTime()) {
            showTotalMap.put(RbReportConstantUtils.CustomAttribute.INCLUDE_TIME, Boolean.TRUE);
        } else {
            showTotalMap.put(RbReportConstantUtils.CustomAttribute.INCLUDE_TIME, Boolean.FALSE);
        }
        if (fieldDataBean.getIsSubFormValue() != null && fieldDataBean.getIsSubFormValue()) {
            showTotalMap.put(RbReportConstantUtils.CustomAttribute.IS_SUB_FORM_VALUE, Boolean.TRUE);
        } else {
            showTotalMap.put(RbReportConstantUtils.CustomAttribute.IS_SUB_FORM_VALUE, Boolean.FALSE);
        }
        if (!StringUtils.isEmpty(fieldDataBean.getParentDbFieldName())) {
            showTotalMap.put(RbReportConstantUtils.CustomAttribute.HK_PARENT_FIELD_NAME, fieldDataBean.getParentDbFieldName());
        } else {
            showTotalMap.put(RbReportConstantUtils.CustomAttribute.HK_PARENT_FIELD_NAME, null);
        }
        if (!StringUtils.isEmpty(fieldDataBean.getParentDbBaseName())) {
            showTotalMap.put(RbReportConstantUtils.CustomAttribute.HK_PARENT_DB_BASE_NAME, fieldDataBean.getParentDbBaseName());
        } else {
            showTotalMap.put(RbReportConstantUtils.CustomAttribute.HK_PARENT_DB_BASE_NAME, null);
        }
        rbReportField.setCustomAttributes(new Gson().toJson(showTotalMap));
        return rbReportField;
    }

    public byte[] generateDataForReport(MasterReportDataBean masterReportDataBean, String extension, HttpServletResponse response, HttpServletRequest request) throws GenericDatabaseException, SQLException, JSONException, ParseException, DRException, IOException, FileNotFoundException, JRException {
        byte[] result = null;
        String query = masterReportDataBean.getQuery();
        Map<String, Object> columnValue = this.retrieveQueryResults(query, masterReportDataBean, true);
        result = this.generateReport(masterReportDataBean, columnValue, extension, response, request, true, null, null, null);
        return result;
    }

//------By Shreya on 05 January 2015 for preparing the data and generating the report.
    public byte[] generateReport(MasterReportDataBean masterReportDataBean, Map<String, Object> columnValue, String extension, HttpServletResponse response, HttpServletRequest request, Boolean toDownload, List<Map<String, Object>> filterAttributes, List<Map<String, Object>> colorAttributes, List<String> hiddenFields) throws FileNotFoundException, DRException, IOException, JRException, GenericDatabaseException, SQLException, JSONException, ParseException {
//        System.out.println("columnValue :" + columnValue);
        //Title to be displayed on the report
        if (masterReportDataBean != null) {
            String title = masterReportDataBean.getReportName();
            //File Name for the report
            String fileName = masterReportDataBean.getReportName();
            List<Map<String, Object>> colorAttributeForReport = new LinkedList<>();
            List<TextColumnBuilder> textColumnBuildersList = new LinkedList<>();
            List<ColumnGroupBuilder> columnGrpBuilderList = new LinkedList<>();
            List<AggregationSubtotalBuilder> subTotalColumnBuilerList = new LinkedList<>();
            String query = masterReportDataBean.getQuery();
            List<String> finalOrderColumnNames = new LinkedList<>();
            List<Map<String, Object>> finalMapList = new LinkedList<>();
            Long index = 0l;
            StyleBuilder myStyle = stl.style().setBorder(stl.penThin()).setFontSize(8).setLeftPadding(3).setRightPadding(3);
            List<List<Object>> records = new LinkedList<>();
            List<String> columnList = new LinkedList<>();
            Object rowRecord = null;
            List<String> groupByFields = new LinkedList<>();
            List<String> tempGroupByFields = new LinkedList<>();
            List<String> orginalGroupByFieldsWithArray = new LinkedList<>();
            List<String> tempOrginalGroupByFieldsWithArray = new LinkedList<>();
            Boolean currencyFlag = this.retrieveCurrencyConfiguration();
            Boolean viewCurrencyDataPermission = this.retrieveViewCurrencyDataRightsOfLoggedInUser();
            Type collection = new TypeToken<Map<String, Object>>() {
            }.getType();
            Map<String, Object> groupByFieldMap = new Gson().fromJson(masterReportDataBean.getGroupAttributes(), collection);
            if (!CollectionUtils.isEmpty(groupByFieldMap)) {
                List<Map<String, Object>> multipleGroupByList = (List<Map<String, Object>>) groupByFieldMap.get("groupBy");
                if (!CollectionUtils.isEmpty(multipleGroupByList)) {
                    for (Map<String, Object> multipleGroupByMapObj : multipleGroupByList) {
                        if (multipleGroupByMapObj != null) {
                            String groupByFieldStrng = multipleGroupByMapObj.get("fields").toString();
                            if (!StringUtils.isEmpty(groupByFieldStrng)) {
                                String[] groupByColumnArr = groupByFieldStrng.split(",");
                                for (String groupByColumnArr1 : groupByColumnArr) {
                                    tempGroupByFields.add(groupByColumnArr1);
                                }
                                tempOrginalGroupByFieldsWithArray.add(groupByFieldStrng);
                            }
                        }
                    }
                }
            }
            //System.out.println("tempOrginalGroupByFieldsWithArray :::::: " + tempOrginalGroupByFieldsWithArray);
            if (!CollectionUtils.isEmpty(masterReportDataBean.getColumns()) && currencyFlag != null && viewCurrencyDataPermission != null && (!currencyFlag || (currencyFlag && !viewCurrencyDataPermission)) && !CollectionUtils.isEmpty(tempOrginalGroupByFieldsWithArray)) {
                for (String groupByFieldStrng : tempOrginalGroupByFieldsWithArray) {
                    if (!StringUtils.isEmpty(groupByFieldStrng)) {
                        String[] groupByColumnArr = groupByFieldStrng.split(",");
                        List<String> groupByColumnArryList = new LinkedList<>(Arrays.asList(groupByColumnArr));
                        Iterator<String> groupByColumnArryListIterator = groupByColumnArryList.iterator();
                        while (groupByColumnArryListIterator.hasNext()) {
                            Boolean rowInvalid = false;
                            String groupByColumnArr1 = groupByColumnArryListIterator.next();
                            for (RbFieldDataBean fieldDataBean : masterReportDataBean.getColumns()) {
                                String reportFieldName = (StringUtils.isEmpty(fieldDataBean.getAlias())
                                        ? fieldDataBean.getFieldLabel() : fieldDataBean.getAlias());
                                if (groupByColumnArr1.equals(reportFieldName) && fieldDataBean.getComponentType().equals(HkSystemConstantUtil.CustomField.ComponentType.CURRENCY)) {
                                    rowInvalid = true;
                                    break;
                                }
                            }
                            if (rowInvalid) {
                                groupByColumnArryListIterator.remove();
                            } else {
                                groupByFields.add(groupByColumnArr1);
                            }
                        }
                        orginalGroupByFieldsWithArray.add(StringUtils.arrayToCommaDelimitedString(groupByColumnArryList.toArray()));
                    }
                }
            } else {
                groupByFields.addAll(tempGroupByFields);
                orginalGroupByFieldsWithArray.addAll(tempOrginalGroupByFieldsWithArray);
            }
//            System.out.println("orginalGroupByFieldsWithArray ::::::: after remove::::: " + orginalGroupByFieldsWithArray);
//            System.out.println("groupByFields ::::: after remove :::: " + groupByFields);
            List<Long> associatedCurrencyList = new ArrayList<>();
            List<RbFieldDataBean> columnsForAssociatedList = masterReportDataBean.getColumns();
            if (!CollectionUtils.isEmpty(columnsForAssociatedList)) {
                for (RbFieldDataBean rbFieldDataBean : columnsForAssociatedList) {
                    if (rbFieldDataBean.getComponentType().equals(HkSystemConstantUtil.CustomField.ComponentType.CURRENCY) && rbFieldDataBean.getAssociatedCurrency() != null) {
                        associatedCurrencyList.add(rbFieldDataBean.getAssociatedCurrency().longValue());
                    }
                }
            }
//            Map<Long, HkCurrencyEntity> currencyIdEntityMap = this.retrieveCurrencyByIds(associatedCurrencyList);
            //System.out.println("currencyIdEntityMap :" + currencyIdEntityMap);
            List<Map<String, Object>> finalResultList = new LinkedList<>();
            if (!CollectionUtils.isEmpty(columnValue)) {
                List<String> orderColumns = new LinkedList<>();
                List<String> orderTypes = new LinkedList<>();
                Type orderCollectionType = new TypeToken<List<Map<String, Object>>>() {
                }.getType();
                List<Map<String, Object>> orderAttributeMapList = new Gson().fromJson(masterReportDataBean.getOrderAttributes(), orderCollectionType);
                if (!CollectionUtils.isEmpty(orderAttributeMapList)) {
                    Collections.sort(orderAttributeMapList, new Comparator() {
                        @Override
                        public int compare(Object o1, Object o2) {
                            Map<String, Object> m1 = (Map<String, Object>) o1;
                            Map<String, Object> m2 = (Map<String, Object>) o2;
                            if (m1.get("sequence") == null) {
                                return (m2.get("sequence") == null) ? 0 : -1;
                            }
                            if (m2.get("sequence") == null) {
                                return 1;
                            }
                            Double first = Double.parseDouble(m1.get("sequence").toString());
                            Double second = Double.parseDouble(m2.get("sequence").toString());
                            return first.compareTo(second);
                        }
                    });
                    List<RbFieldDataBean> dbColumns = masterReportDataBean.getColumns();

                    finalMapList.addAll(orderAttributeMapList);
                    for (Map<String, Object> orderAttributeMap : orderAttributeMapList) {
                        String columnName = orderAttributeMap.get("columnName").toString();
                        orderColumns.add(columnName);
                        orderTypes.add(orderAttributeMap.get("orderValue").toString());
                    }
                    if (!CollectionUtils.isEmpty(orderColumns)) {
                        for (String orderColumn : orderColumns) {
                            String[] split = orderColumn.split("\\.");
                            if (split.length >= 2) {
                                String dbBaseName = split[0];
                                String dbFieldName = split[1];
                                for (RbFieldDataBean rbFieldDataBean : dbColumns) {
                                    if (dbBaseName.equals(rbFieldDataBean.getDbBaseName()) && dbFieldName.equals(rbFieldDataBean.getColName())) {
                                        finalOrderColumnNames.add(rbFieldDataBean.getAlias());
                                    }
                                }
                            }

                        }
                    }
                }

                for (int i = 0; i < finalMapList.size(); i++) {
                    if (finalMapList.get(i) != null) {
                        finalMapList.get(i).put("columnName", finalOrderColumnNames.get(i));
                    }
                }
                if (!CollectionUtils.isEmpty(finalOrderColumnNames) && currencyFlag != null && viewCurrencyDataPermission != null && (!currencyFlag || (currencyFlag && !viewCurrencyDataPermission)) && !CollectionUtils.isEmpty(masterReportDataBean.getColumns())) {
                    Iterator<String> finalOrderColumnNamesIterator = finalOrderColumnNames.iterator();
                    while (finalOrderColumnNamesIterator.hasNext()) {
                        Boolean rowInvalid = false;
                        String colName = finalOrderColumnNamesIterator.next();
                        for (RbFieldDataBean rbFieldDataBean : masterReportDataBean.getColumns()) {
                            String reportFieldName = (StringUtils.isEmpty(rbFieldDataBean.getAlias())
                                    ? rbFieldDataBean.getFieldLabel() : rbFieldDataBean.getAlias());
                            if (colName.equals(reportFieldName) && rbFieldDataBean.getComponentType().equals(HkSystemConstantUtil.CustomField.ComponentType.CURRENCY)) {
                                rowInvalid = true;
                                break;
                            }
                        }
                        if (rowInvalid) {
                            Iterator<Map<String, Object>> finalMapListIterator = finalMapList.iterator();
                            while (finalMapListIterator.hasNext()) {
                                Map<String, Object> next = finalMapListIterator.next();
                                if (next != null) {
                                    String columnName = next.get("columnName").toString();
                                    if (columnName.equals(colName)) {
                                        next.remove(columnName);
                                    }
                                }

                            }
                            finalOrderColumnNamesIterator.remove();
                        }
                    }
                }
//                System.out.println("finalOrderColumnNames ::::: remove ::::::" + finalOrderColumnNames);
//                System.out.println("finalMapList :::::: remove :::::" + finalMapList);
                List<Map<String, Object>> tempMapForOrder = new LinkedList<>();
                Integer indexForOrdering = 0;
                for (String groupByField : groupByFields) {
                    int count = 0;
                    if (finalOrderColumnNames != null && finalOrderColumnNames.contains(groupByField) && finalMapList != null) {
                        for (int i = 0; i < finalMapList.size(); i++) {
                            if (finalMapList.get(i) != null) {
                                if (finalMapList.get(i).get("columnName").equals(groupByField)) {
                                    count++;
                                    finalMapList.get(i).put("sequence", (++indexForOrdering).doubleValue());
                                    tempMapForOrder.add(finalMapList.get(i));
                                }
                            }
                        }
                    }
//                    System.out.println("count :" + count);
                    if (count == 0) {
//                        System.out.println("inside count == 0");
                        Map<String, Object> map = new HashMap<>();
                        map.put("columnName", groupByField);
                        map.put("orderValue", "asc");
                        map.put("sequence", (++indexForOrdering).doubleValue());
                        tempMapForOrder.add(map);
                    }
                }
                for (Map<String, Object> finalMapList1 : finalMapList) {
                    if (!groupByFields.contains(finalMapList1.get("columnName"))) {
                        finalMapList1.put("sequence", (++indexForOrdering).doubleValue());
                        tempMapForOrder.add(finalMapList1);
                    }

                }
//                System.out.println("tempMapForOrder before :" + tempMapForOrder);

                //Adding Color attributes to each row conditionally
                List<ConditionalStyleBuilder> conditionalStyleBuilders = new LinkedList<>();
                if (!CollectionUtils.isEmpty(colorAttributes)) {
                    Collections.reverse(colorAttributes);
                    colorAttributeForReport.addAll(colorAttributes);
                    for (Map<String, Object> colorAttribute : colorAttributeForReport) {
                        if (colorAttribute.get("colorName") != null) {
                            String colorName = colorAttribute.get("colorName").toString();
                            ConditionalStyleBuilder condition1 = stl.conditionalStyle(new ConditionalExpressionBuilder(colorAttribute))
                                    .setBackgroundColor(Color.decode(colorName));
                            conditionalStyleBuilders.add(condition1);
                        }
                    }
                    myStyle = stl.style().setVerticalAlignment(VerticalAlignment.TOP).setBorder(stl.penThin()).setFontSize(8).setLeftPadding(3).setRightPadding(3)
                            .conditionalStyles(conditionalStyleBuilders.toArray(new ConditionalStyleBuilder[conditionalStyleBuilders.size()]));
                } else {
                    Type collectionType = new TypeToken<List<Map<String, Object>>>() {
                    }.getType();
                    List<Map<String, Object>> colorAttributeMapList = new Gson().fromJson(masterReportDataBean.getColorAttributes(), collectionType);
                    if (!CollectionUtils.isEmpty(colorAttributeMapList)) {
                        Collections.reverse(colorAttributeMapList);
                        colorAttributeForReport.addAll(colorAttributeMapList);

                        if (currencyFlag != null && viewCurrencyDataPermission != null && (!currencyFlag || (currencyFlag && !viewCurrencyDataPermission)) && !CollectionUtils.isEmpty(masterReportDataBean.getColumns())) {
                            for (Map<String, Object> colorAttribute : colorAttributeForReport) {
                                List<Map<String, Object>> colorAttributeList = (List<Map<String, Object>>) colorAttribute.get("columns");
                                Iterator<Map<String, Object>> colorAttributeListIterator = colorAttributeList.iterator();
                                while (colorAttributeListIterator.hasNext()) {
                                    Map<String, Object> mapValue = colorAttributeListIterator.next();
                                    String columnNameFromList = mapValue.get("label").toString();
                                    for (RbFieldDataBean rbFieldDataBean : masterReportDataBean.getColumns()) {
                                        String reportFieldName = (StringUtils.isEmpty(rbFieldDataBean.getAlias())
                                                ? rbFieldDataBean.getFieldLabel() : rbFieldDataBean.getAlias());
                                        if (columnNameFromList.equals(reportFieldName) && rbFieldDataBean.getComponentType().equals(HkSystemConstantUtil.CustomField.ComponentType.CURRENCY)) {
                                            colorAttributeListIterator.remove();
                                        }
                                    }

                                }
                            }
                        }
//                        System.out.println("colorAttributeForReport :::::: after remove " + colorAttributeForReport);
                        for (Map<String, Object> colorAttribute : colorAttributeForReport) {
                            if (colorAttribute.get("colorName") != null) {
                                String colorName = colorAttribute.get("colorName").toString();
                                ConditionalStyleBuilder condition1 = stl.conditionalStyle(new ConditionalExpressionBuilder(colorAttribute))
                                        .setBackgroundColor(Color.decode(colorName));
                                conditionalStyleBuilders.add(condition1);
                            }
                        }
                        myStyle = stl.style().setVerticalAlignment(VerticalAlignment.TOP).setBorder(stl.penThin()).setFontSize(8).setLeftPadding(3).setRightPadding(3)
                                .conditionalStyles(conditionalStyleBuilders.toArray(new ConditionalStyleBuilder[conditionalStyleBuilders.size()]));
                    }
                }
//                System.out.println("orginalGroupByFieldsWithArray :" + orginalGroupByFieldsWithArray);
                if (!CollectionUtils.isEmpty(groupByFields)) {
                    Map<String, List<Object>> map = new HashMap<>((Map<String, List<Object>>) columnValue.get("records"));
                    if (!CollectionUtils.isEmpty(groupByFields)) {
                        finalResultList = this.retrieveGroupedDataForReport(map, orginalGroupByFieldsWithArray, groupByFields, tempMapForOrder);
                    } else {
                        finalResultList = this.retrieveGroupedDataForReport(map, orginalGroupByFieldsWithArray, null, tempMapForOrder);
                    }

                } else {
                    finalResultList = (List<Map<String, Object>>) columnValue.get("records");
                }
                //System.out.println("finalResultList :" + finalResultList);
                List<Map<String, Object>> resultList = new LinkedList<>(finalResultList);
                if (!CollectionUtils.isEmpty(resultList)) {
                    Map<String, Object> mapValue = resultList.get(0);
                    if (!CollectionUtils.isEmpty(mapValue)) {
                        for (Map.Entry<String, Object> entry : mapValue.entrySet()) {
                            String columnName = entry.getKey();
                            columnList.add(columnName);
                        }
                    }
                    System.out.println("resultList :" + resultList);
                    for (Map<String, Object> results : resultList) {
                        List<Object> row = new LinkedList<>();
                        ++index;
                        row.add(index.toString());
                        for (Map.Entry<String, Object> entry : results.entrySet()) {
                            String column = entry.getKey();
                            if (!CollectionUtils.isEmpty(masterReportDataBean.getColumns())) {
                                int mapOldSeqCount = 0;
                                int mapNewSeqCount = 0;
                                for (RbFieldDataBean fieldDataBean : masterReportDataBean.getColumns()) {
                                    String columnTitle = "";
                                    char lastCharacter = column.charAt(column.length() - 1);
                                    if ((fieldDataBean.getAlias().equals(column)) || (lastCharacter == '1' && isContain(fieldDataBean.getAlias(), column.substring(0, column.length() - 1)))) {
                                        if (mapOldSeqCount == 0) {
                                            mapOldSeqCount = fieldDataBean.getFieldSequence();
                                            String dataType = fieldDataBean.getDataType();

                                            if (dataType.equals(HkSystemConstantUtil.CustomField.DataType.INT8)) {
                                                if (!StringUtils.isEmpty(entry.getValue())) {
                                                    rowRecord = Long.parseLong(entry.getValue().toString());
                                                } else {
                                                    rowRecord = 0l;
                                                }
                                            } else if (dataType.equals(HkSystemConstantUtil.CustomField.DataType.VARCHAR) || dataType.equals(HkSystemConstantUtil.CustomField.DataType.BOOLEAN)
                                                    || fieldDataBean.getComponentType().equals(HkSystemConstantUtil.CustomField.ComponentType.CURRENCY)) {
                                                if (entry.getValue() != null) {
                                                    rowRecord = entry.getValue().toString();
                                                } else {
                                                    rowRecord = null;
                                                }
                                            } else if (dataType.equals(HkSystemConstantUtil.CustomField.DataType.TIMESTAMP)) {
                                                if (entry.getValue() != null) {
                                                    if (!StringUtils.isEmpty(entry.getValue().toString())) {
                                                        rowRecord = entry.getValue().toString();
                                                    } else {
                                                        rowRecord = "";
                                                    }
                                                } else {
                                                    rowRecord = null;
                                                }
                                            } else if (dataType.equals(HkSystemConstantUtil.CustomField.DataType.DOUBLE)) {
                                                if (entry.getValue() != null) {
                                                    rowRecord = Double.parseDouble(entry.getValue().toString());
                                                } else {
                                                    rowRecord = 0d;
                                                }

                                            } else {
                                                if (entry.getValue() != null) {
                                                    rowRecord = entry.getValue().toString();
                                                } else {
                                                    rowRecord = null;
                                                }
                                            }
                                        } else {
                                            mapNewSeqCount = fieldDataBean.getFieldSequence();
                                            if (mapOldSeqCount > mapNewSeqCount) {
                                                mapOldSeqCount = mapNewSeqCount;
                                                String dataType = fieldDataBean.getDataType();
                                                if (dataType.equals(HkSystemConstantUtil.CustomField.DataType.INT8)) {
                                                    if (entry.getValue() != null) {
                                                        rowRecord = Long.parseLong(entry.getValue().toString());
                                                    } else {
                                                        rowRecord = 0l;
                                                    }
                                                } else if (dataType.equals(HkSystemConstantUtil.CustomField.DataType.VARCHAR) || dataType.equals(HkSystemConstantUtil.CustomField.DataType.BOOLEAN)
                                                        || fieldDataBean.getComponentType().equals(HkSystemConstantUtil.CustomField.ComponentType.CURRENCY)) {
                                                    if (entry.getValue() != null) {
                                                        rowRecord = entry.getValue().toString();
                                                    } else {
                                                        rowRecord = null;
                                                    }
                                                } else if (dataType.equals(HkSystemConstantUtil.CustomField.DataType.TIMESTAMP)) {
                                                    if (entry.getValue() != null) {
                                                        if (!StringUtils.isEmpty(entry.getValue().toString())) {
//                                                            Date dates = new Date(entry.getValue().toString());
//                                                            Long longDate = dates.getTime();
//                                                            rowRecord = formatDate(longDate);
                                                            rowRecord = entry.getValue().toString();
                                                        } else {
                                                            rowRecord = "";
                                                        }
                                                    } else {
                                                        rowRecord = null;
                                                    }
                                                } else if (dataType.equals(HkSystemConstantUtil.CustomField.DataType.DOUBLE)) {
                                                    if (entry.getValue() != null) {
                                                        rowRecord = Double.parseDouble(entry.getValue().toString());
                                                    } else {
                                                        rowRecord = 0d;
                                                    }

                                                } else {
                                                    if (entry.getValue() != null) {
                                                        rowRecord = entry.getValue().toString();
                                                    } else {
                                                        rowRecord = null;
                                                    }
                                                }
                                            }
                                        }
                                        row.add(rowRecord);
                                    }
                                }
                            }
//                            row.add(rowRecord);
                        }
                        records.add(row);
                    }
                }
            }
            //System.out.println("records before:" + records);
            Long currentIndex = 0l;
            int srIndex = 0;
            for (List<Object> record : records) {
//                System.out.println("record in loop :" + record);
                srIndex++;
//                System.out.println("record.get(1) :" + record.get(1));
                if (record.get(1) == null || record.get(1) == "") {
                    if (srIndex != 1) {
                        record.set(0, "");
                    } else {
                        currentIndex++;
                    }
                } else {
                    currentIndex++;
                    record.set(0, currentIndex.toString());
                }
            }
//            System.out.println("records after :" + records);
            List<String> dataBaseColumns = new LinkedList<>();
            List<ColumnGridComponentBuilder> gridBuilder = new LinkedList<>();
            dataBaseColumns.add("Sr_No");
            TextColumnBuilder<String> column = col.column("Sr. No", "Sr_No", type.stringType()).setStyle(contentRightAlignStyle)
                    .setTitleStyle(stl.style(rootStyle).setVerticalAlignment(VerticalAlignment.MIDDLE)
                            .setBorder(stl.penThin()).setFontSize(8)
                            .setHorizontalAlignment(HorizontalAlignment.CENTER)
                            .setBackgroundColor(new Color(177, 154, 204))
                            .bold());
            textColumnBuildersList.add(column);

            column.setStyle(myStyle).setHorizontalAlignment(HorizontalAlignment.RIGHT);
            Type collectionType = new TypeToken<Map<String, Object>>() {
            }.getType();
            Map<String, Object> groupByColumnMap = new Gson().fromJson(masterReportDataBean.getGroupAttributes(), collectionType);
            if (!CollectionUtils.isEmpty(groupByColumnMap) && groupByColumnMap.get("groups") != null && groupByColumnMap.get("groupBy") != null) {
                List<String> groupByColumns = new LinkedList<>();
                List<Map<String, Object>> multipleGroupByListVal = (List<Map<String, Object>>) groupByColumnMap.get("groupBy");
                if (!CollectionUtils.isEmpty(multipleGroupByListVal)) {
                    for (Map<String, Object> multipleGroupByMapObj : multipleGroupByListVal) {
                        if (multipleGroupByMapObj != null) {
                            String groupByFieldStrng = multipleGroupByMapObj.get("fields").toString();
                            if (!StringUtils.isEmpty(groupByFieldStrng)) {
                                String[] groupByColumnArr = groupByFieldStrng.split(",");
                                for (String groupByColumnArr1 : groupByColumnArr) {
                                    Boolean rowInvalid = false;
                                    if (currencyFlag != null && viewCurrencyDataPermission != null && (!currencyFlag || (currencyFlag && !viewCurrencyDataPermission)) && !CollectionUtils.isEmpty(masterReportDataBean.getColumns())) {
                                        for (RbFieldDataBean rbFieldDataBean : masterReportDataBean.getColumns()) {
                                            String reportFieldName = (StringUtils.isEmpty(rbFieldDataBean.getAlias())
                                                    ? rbFieldDataBean.getFieldLabel() : rbFieldDataBean.getAlias());
                                            if (groupByColumnArr1.equals(reportFieldName) && rbFieldDataBean.getComponentType().equals(HkSystemConstantUtil.CustomField.ComponentType.CURRENCY)) {
                                                rowInvalid = true;
                                            }
                                        }
                                        if (!rowInvalid) {
                                            groupByColumns.add(groupByColumnArr1);
                                        }
                                    } else {
                                        groupByColumns.add(groupByColumnArr1);
                                    }

                                }
                            }
                        }
                    }
                }
                System.out.println("groupByColumns :::::: in pdf :::::" + groupByColumns);
                gridBuilder.add(column);
                List<Map<String, Object>> groupsList = (List<Map<String, Object>>) groupByColumnMap.get("groups");
                //System.out.println("groupsList :::: before remove ::::" + groupsList);
                if (currencyFlag != null && viewCurrencyDataPermission != null && (!currencyFlag || (currencyFlag && !viewCurrencyDataPermission)) && !CollectionUtils.isEmpty(masterReportDataBean.getColumns())) {
                    Iterator<Map<String, Object>> groupsListIterator = groupsList.iterator();
                    while (groupsListIterator.hasNext()) {
                        Map<String, Object> groupDataMap = groupsListIterator.next();
                        String[] groupColumnArray = groupDataMap.get("groupItems").toString().split(",");
                        List<String> grpColumns = new LinkedList<>(Arrays.asList(groupColumnArray));
                        Iterator<String> grpColumnsIterator = grpColumns.iterator();
                        while (grpColumnsIterator.hasNext()) {
                            String coll = grpColumnsIterator.next();
                            for (RbFieldDataBean rbFieldDataBean : masterReportDataBean.getColumns()) {
                                String reportFieldName = (StringUtils.isEmpty(rbFieldDataBean.getAlias())
                                        ? rbFieldDataBean.getFieldLabel() : rbFieldDataBean.getAlias());
                                if (coll.equals(reportFieldName) && rbFieldDataBean.getComponentType().equals(HkSystemConstantUtil.CustomField.ComponentType.CURRENCY)) {
                                    grpColumnsIterator.remove();
                                }
                            }

                        }
                    }
                }
                System.out.println("groupsList :::: after remove ::::" + groupsList);
                System.out.println("columnList :" + columnList);
                if (!CollectionUtils.isEmpty(groupsList)) {
                    List<String> tempList = new LinkedList<>();
                    for (String columns : columnList) {
                        for (RbFieldDataBean fieldDataBean : masterReportDataBean.getColumns()) {
                            char lastCharacter = columns.charAt(columns.length() - 1);
                            if ((columns.equals(fieldDataBean.getAlias())) || (lastCharacter == '1' && isContain(fieldDataBean.getAlias(), columns.substring(0, columns.length() - 1)))) {
                                if ((columns.equals(fieldDataBean.getAlias()))) {
                                    if (!StringUtils.isEmpty(fieldDataBean.getAlias())) {
                                        tempList.add(fieldDataBean.getAlias().toLowerCase().replaceAll("\\s+", ""));
                                    } else {
                                        tempList.add(fieldDataBean.getEditedFieldLabel());
                                    }
                                } else if ((lastCharacter == '1' && isContain(fieldDataBean.getAlias(), columns.substring(0, columns.length() - 1)))) {
                                    if (!StringUtils.isEmpty(fieldDataBean.getAlias())) {
                                        tempList.add(fieldDataBean.getAlias().toLowerCase().replaceAll("\\s+", "") + "1");
                                    } else {
                                        tempList.add(fieldDataBean.getEditedFieldLabel());
                                    }
                                }

                            }
                        }
                    }
                    System.out.println("tempList :" + tempList);
                    Collections.sort(groupsList, new Comparator() {
                        @Override
                        public int compare(Object o1, Object o2) {
                            Map<String, Object> m1 = (Map<String, Object>) o1;
                            Map<String, Object> m2 = (Map<String, Object>) o2;
                            if (m1.get("sequence") == null) {
                                return (m2.get("sequence") == null) ? 0 : -1;
                            }
                            if (m2.get("sequence") == null) {
                                return 1;
                            }
                            Double first = Double.parseDouble(m1.get("sequence").toString());
                            Double second = Double.parseDouble(m2.get("sequence").toString());
                            return first.compareTo(second);
                        }
                    });
                    for (String groupByValues : groupByColumns) {
                        for (RbFieldDataBean fieldDataBean : masterReportDataBean.getColumns()) {
                            String colTitle = "";
                            if ((groupByValues.equals(fieldDataBean.getAlias()))) {
                                if (!StringUtils.isEmpty(fieldDataBean.getAlias())) {
                                    dataBaseColumns.add(fieldDataBean.getAlias().toLowerCase().replaceAll("\\s+", ""));
                                } else {
                                    dataBaseColumns.add(fieldDataBean.getEditedFieldLabel());
                                }
                                if (fieldDataBean.getAlias() == null || fieldDataBean.getAlias().equals("")) {
                                    colTitle = fieldDataBean.getFieldLabel();
                                } else {
                                    colTitle = fieldDataBean.getAlias();
                                }
                                if (CollectionUtils.isEmpty(hiddenFields) || !hiddenFields.contains(groupByValues)) {
                                    if (fieldDataBean.getDataType().equals(HkSystemConstantUtil.CustomField.DataType.INT8)) {
                                        if (!StringUtils.isEmpty(fieldDataBean.getAlias())) {
                                            TextColumnBuilder<Long> column1 = col.column(colTitle, fieldDataBean.getAlias().toLowerCase().replaceAll("\\s+", ""), type.longType()).setStyle(contentRightAlignStyle)
                                                    .setTitleStyle(stl.style(rootStyle).setVerticalAlignment(VerticalAlignment.MIDDLE)
                                                            .setBorder(stl.penThin()).setFontSize(8)
                                                            .setHorizontalAlignment(HorizontalAlignment.CENTER)
                                                            .setBackgroundColor(new Color(177, 154, 204))
                                                            .bold()).setTitleFixedHeight(50);
                                            column1.setStyle(myStyle).setHorizontalAlignment(HorizontalAlignment.RIGHT);
                                            textColumnBuildersList.add(column1);

                                            if (fieldDataBean.getShowTotal()) {
                                                AggregationSubtotalBuilder<Long> sbtColumn = sbt.sum(fieldDataBean.getAlias().toLowerCase().replaceAll("\\s+", ""), Long.class, column1).setLabel(colTitle + " " + "Total");
                                                subTotalColumnBuilerList.add(sbtColumn);
                                            }
                                            gridBuilder.add(column1);
                                        } else {
                                            TextColumnBuilder<Long> column1 = col.column(colTitle, fieldDataBean.getEditedFieldLabel(), type.longType()).setStyle(contentRightAlignStyle)
                                                    .setTitleStyle(stl.style(rootStyle).setVerticalAlignment(VerticalAlignment.MIDDLE)
                                                            .setBorder(stl.penThin()).setFontSize(8)
                                                            .setHorizontalAlignment(HorizontalAlignment.CENTER)
                                                            .setBackgroundColor(new Color(177, 154, 204))
                                                            .bold()).setTitleFixedHeight(50);
                                            column1.setStyle(myStyle).setHorizontalAlignment(HorizontalAlignment.RIGHT);
                                            textColumnBuildersList.add(column1);

                                            if (fieldDataBean.getShowTotal()) {
                                                System.out.println("group by label show total:" + fieldDataBean.getEditedFieldLabel() + "value :" + fieldDataBean.getShowTotal());
                                                AggregationSubtotalBuilder<Long> sbtColumn = sbt.sum(fieldDataBean.getEditedFieldLabel(), Long.class, column1).setLabel(colTitle + " " + "Total");
                                                subTotalColumnBuilerList.add(sbtColumn);
                                            }
                                            gridBuilder.add(column1);
                                        }

                                    } else if (fieldDataBean.getDataType().equals(HkSystemConstantUtil.CustomField.DataType.VARCHAR)
                                            || fieldDataBean.getDataType().equals(HkSystemConstantUtil.CustomField.DataType.BOOLEAN)
                                            || fieldDataBean.getComponentType().equals(HkSystemConstantUtil.CustomField.ComponentType.CURRENCY)) {
                                        TextColumnBuilder<String> column1;
                                        if (!StringUtils.isEmpty(fieldDataBean.getAlias())) {
                                            column1 = col.column(colTitle, fieldDataBean.getAlias().toLowerCase().replaceAll("\\s+", ""), type.stringType())
                                                    .setTitleStyle(stl.style(rootStyle).setVerticalAlignment(VerticalAlignment.MIDDLE)
                                                            .setBorder(stl.penThin()).setFontSize(8)
                                                            .setHorizontalAlignment(HorizontalAlignment.CENTER)
                                                            .setBackgroundColor(new Color(177, 154, 204))
                                                            .bold()).setTitleFixedHeight(50);
                                        } else {
                                            column1 = col.column(colTitle, fieldDataBean.getEditedFieldLabel(), type.stringType());
                                        }
                                        column1.setStyle(myStyle);
                                        textColumnBuildersList.add(column1);
                                        gridBuilder.add(column1);
                                    } else if (fieldDataBean.getDataType().equals(HkSystemConstantUtil.CustomField.DataType.TIMESTAMP)) {
                                        TextColumnBuilder<String> column1;
                                        if (!StringUtils.isEmpty(fieldDataBean.getAlias())) {
                                            column1 = col.column(colTitle, fieldDataBean.getAlias().toLowerCase().replaceAll("\\s+", ""), type.stringType())
                                                    .setTitleStyle(stl.style(rootStyle).setVerticalAlignment(VerticalAlignment.MIDDLE)
                                                            .setBorder(stl.penThin()).setFontSize(8)
                                                            .setHorizontalAlignment(HorizontalAlignment.CENTER)
                                                            .setBackgroundColor(new Color(177, 154, 204))
                                                            .bold()).setTitleFixedHeight(50);
                                        } else {
                                            column1 = col.column(colTitle, fieldDataBean.getEditedFieldLabel(), type.stringType())
                                                    .setTitleStyle(stl.style(rootStyle).setVerticalAlignment(VerticalAlignment.MIDDLE)
                                                            .setBorder(stl.penThin()).setFontSize(8)
                                                            .setHorizontalAlignment(HorizontalAlignment.CENTER)
                                                            .setBackgroundColor(new Color(177, 154, 204))
                                                            .bold()).setTitleFixedHeight(50);
                                        }
                                        column1.setStyle(myStyle);
                                        textColumnBuildersList.add(column1);
                                        gridBuilder.add(column1);
                                    } else if (fieldDataBean.getDataType().equals(HkSystemConstantUtil.CustomField.DataType.DOUBLE)) {
//                                    System.out.println("fieldDataBean :" + fieldDataBean);
                                        TextColumnBuilder<Double> column1;
                                        if (fieldDataBean.getComponentType().equals(HkSystemConstantUtil.CustomField.ComponentType.PERCENT)) {
                                            if (!StringUtils.isEmpty(fieldDataBean.getAlias())) {
                                                column1 = col.column(colTitle, fieldDataBean.getAlias().toLowerCase().replaceAll("\\s+", ""), type.doubleType()).setValueFormatter(ReportBuilderTemplate.createValueFormatter("%")).setStyle(contentRightAlignStyle)
                                                        .setTitleStyle(stl.style(rootStyle).setVerticalAlignment(VerticalAlignment.MIDDLE)
                                                                .setBorder(stl.penThin()).setFontSize(8)
                                                                .setHorizontalAlignment(HorizontalAlignment.CENTER)
                                                                .setBackgroundColor(new Color(177, 154, 204))
                                                                .bold()).setTitleFixedHeight(50);
                                            } else {
                                                column1 = col.column(colTitle, fieldDataBean.getEditedFieldLabel(), type.doubleType()).setValueFormatter(ReportBuilderTemplate.createValueFormatter("%")).setStyle(contentRightAlignStyle)
                                                        .setTitleStyle(stl.style(rootStyle).setVerticalAlignment(VerticalAlignment.MIDDLE)
                                                                .setBorder(stl.penThin()).setFontSize(8)
                                                                .setHorizontalAlignment(HorizontalAlignment.CENTER)
                                                                .setBackgroundColor(new Color(177, 154, 204))
                                                                .bold()).setTitleFixedHeight(50);

                                            }
                                            if (fieldDataBean.getShowTotal()) {
                                                AggregationSubtotalBuilder<Double> sbtColumn = sbt.sum(fieldDataBean.getAlias().toLowerCase().replaceAll("\\s+", ""), Double.class, column1).setLabel(colTitle + " " + "Total");
                                                subTotalColumnBuilerList.add(sbtColumn);
                                            }
                                            column1.setStyle(myStyle).setHorizontalAlignment(HorizontalAlignment.RIGHT);
                                            textColumnBuildersList.add(column1);
                                        } else if (fieldDataBean.getComponentType().equals(HkSystemConstantUtil.CustomField.ComponentType.ANGLE)) {
                                            if (!StringUtils.isEmpty(fieldDataBean.getAlias())) {
                                                column1 = col.column(colTitle, fieldDataBean.getAlias().toLowerCase().replaceAll("\\s+", ""), type.doubleType()).setValueFormatter(ReportBuilderTemplate.createValueFormatter("°")).setStyle(contentRightAlignStyle)
                                                        .setTitleStyle(stl.style(rootStyle).setVerticalAlignment(VerticalAlignment.MIDDLE)
                                                                .setBorder(stl.penThin()).setFontSize(8)
                                                                .setHorizontalAlignment(HorizontalAlignment.CENTER)
                                                                .setBackgroundColor(new Color(177, 154, 204))
                                                                .bold()).setTitleFixedHeight(50);
                                            } else {
                                                column1 = col.column(colTitle, fieldDataBean.getEditedFieldLabel(), type.doubleType()).setValueFormatter(ReportBuilderTemplate.createValueFormatter("°")).setStyle(contentRightAlignStyle)
                                                        .setTitleStyle(stl.style(rootStyle).setVerticalAlignment(VerticalAlignment.MIDDLE)
                                                                .setBorder(stl.penThin()).setFontSize(8)
                                                                .setHorizontalAlignment(HorizontalAlignment.CENTER)
                                                                .setBackgroundColor(new Color(177, 154, 204))
                                                                .bold()).setTitleFixedHeight(50);

                                            }
                                            if (fieldDataBean.getShowTotal()) {
                                                AggregationSubtotalBuilder<Double> sbtColumn = sbt.sum(fieldDataBean.getAlias().toLowerCase().replaceAll("\\s+", ""), Double.class, column1).setLabel(colTitle + " " + "Total");
                                                subTotalColumnBuilerList.add(sbtColumn);
                                            }
                                            column1.setStyle(myStyle).setHorizontalAlignment(HorizontalAlignment.RIGHT);
                                            textColumnBuildersList.add(column1);
                                        } else if (fieldDataBean.getComponentType().equals(HkSystemConstantUtil.CustomField.ComponentType.CURRENCY)) {
                                            //System.out.println("fieldDataBean.getAssociatedCurrency(); 1 ::::" + fieldDataBean.getAssociatedCurrency());
                                            String label = "";
                                            String pattern = "";
//                                            if (fieldDataBean.getAssociatedCurrency() != null && currencyIdEntityMap.get(fieldDataBean.getAssociatedCurrency().longValue()) != null) {
//                                                label = currencyIdEntityMap.get(fieldDataBean.getAssociatedCurrency().longValue()).getSymbol();
//                                                pattern = currencyIdEntityMap.get(fieldDataBean.getAssociatedCurrency().longValue()).getFormat();
//                                                //System.out.println("label :" + label + "pattern :" + pattern);
//                                            }
                                            if (!StringUtils.isEmpty(fieldDataBean.getAlias())) {
                                                column1 = col.column(colTitle, fieldDataBean.getAlias().toLowerCase().replaceAll("\\s+", ""), type.doubleType()).setValueFormatter(ReportBuilderTemplate.createCurrencyValueFormatter(label, pattern)).setStyle(contentRightAlignStyle)
                                                        .setTitleStyle(stl.style(rootStyle).setVerticalAlignment(VerticalAlignment.MIDDLE)
                                                                .setBorder(stl.penThin()).setFontSize(8)
                                                                .setHorizontalAlignment(HorizontalAlignment.CENTER)
                                                                .setBackgroundColor(new Color(177, 154, 204))
                                                                .bold()).setTitleFixedHeight(50);
                                            } else {
                                                column1 = col.column(colTitle, fieldDataBean.getEditedFieldLabel(), type.doubleType()).setValueFormatter(ReportBuilderTemplate.createCurrencyValueFormatter(label, pattern)).setStyle(contentRightAlignStyle)
                                                        .setTitleStyle(stl.style(rootStyle).setVerticalAlignment(VerticalAlignment.MIDDLE)
                                                                .setBorder(stl.penThin()).setFontSize(8)
                                                                .setHorizontalAlignment(HorizontalAlignment.CENTER)
                                                                .setBackgroundColor(new Color(177, 154, 204))
                                                                .bold()).setTitleFixedHeight(50);

                                            }
                                            if (fieldDataBean.getShowTotal()) {
                                                AggregationSubtotalBuilder<Double> sbtColumn = sbt.sum(fieldDataBean.getAlias().toLowerCase().replaceAll("\\s+", ""), Double.class, column1).setLabel(colTitle + " " + "Total");
                                                subTotalColumnBuilerList.add(sbtColumn);
                                            }
                                            column1.setStyle(myStyle).setHorizontalAlignment(HorizontalAlignment.RIGHT);
                                            textColumnBuildersList.add(column1);
                                        } else {
//                                        System.out.println("inside double");
                                            if (!StringUtils.isEmpty(fieldDataBean.getAlias())) {
                                                column1 = col.column(colTitle, fieldDataBean.getAlias().toLowerCase().replaceAll("\\s+", ""), type.doubleType()).setStyle(contentRightAlignStyle)
                                                        .setTitleStyle(stl.style(rootStyle).setVerticalAlignment(VerticalAlignment.MIDDLE)
                                                                .setBorder(stl.penThin()).setFontSize(8)
                                                                .setHorizontalAlignment(HorizontalAlignment.CENTER)
                                                                .setBackgroundColor(new Color(177, 154, 204))
                                                                .bold()).setTitleFixedHeight(50);
                                            } else {
                                                column1 = col.column(colTitle, fieldDataBean.getEditedFieldLabel(), type.doubleType()).setStyle(contentRightAlignStyle)
                                                        .setTitleStyle(stl.style(rootStyle).setVerticalAlignment(VerticalAlignment.MIDDLE)
                                                                .setBorder(stl.penThin()).setFontSize(8)
                                                                .setHorizontalAlignment(HorizontalAlignment.CENTER)
                                                                .setBackgroundColor(new Color(177, 154, 204))
                                                                .bold()).setTitleFixedHeight(50);

                                            }
                                            if (fieldDataBean.getShowTotal()) {
                                                AggregationSubtotalBuilder<Double> sbtColumn = sbt.sum(fieldDataBean.getAlias().toLowerCase().replaceAll("\\s+", ""), Double.class, column1).setLabel(colTitle + " " + "Total");
                                                subTotalColumnBuilerList.add(sbtColumn);
                                            }
                                            column1.setStyle(myStyle).setHorizontalAlignment(HorizontalAlignment.RIGHT);
                                            textColumnBuildersList.add(column1);
                                        }
                                        gridBuilder.add(column1);

                                    } else {
                                        TextColumnBuilder<String> column1;
                                        if (!StringUtils.isEmpty(fieldDataBean.getAlias())) {
                                            column1 = col.column(colTitle, fieldDataBean.getAlias().toLowerCase().replaceAll("\\s+", ""), type.stringType())
                                                    .setTitleStyle(stl.style(rootStyle).setVerticalAlignment(VerticalAlignment.MIDDLE)
                                                            .setBorder(stl.penThin()).setFontSize(8)
                                                            .setHorizontalAlignment(HorizontalAlignment.CENTER)
                                                            .setBackgroundColor(new Color(177, 154, 204))
                                                            .bold()).setTitleFixedHeight(50);
                                        } else {
                                            column1 = col.column(colTitle, fieldDataBean.getEditedFieldLabel(), type.stringType())
                                                    .setTitleStyle(stl.style(rootStyle).setVerticalAlignment(VerticalAlignment.MIDDLE)
                                                            .setBorder(stl.penThin()).setFontSize(8)
                                                            .setHorizontalAlignment(HorizontalAlignment.CENTER)
                                                            .setBackgroundColor(new Color(177, 154, 204))
                                                            .bold()).setTitleFixedHeight(50);
                                        }
                                        column1.setStyle(myStyle);
                                        textColumnBuildersList.add(column1);
                                        gridBuilder.add(column1);
                                    }
                                }

                            }
                        }
                    }
                    if (groupsList.size() == 1) {
                        Map<String, Object> firstGroup = groupsList.get(0);
                        if (!CollectionUtils.isEmpty(firstGroup)) {
                            String[] firstGroupByColumnArr = firstGroup.get("groupItems").toString().split(",");
                            List<String> firstGroupByColumns = Arrays.asList(firstGroupByColumnArr);
                            if (!CollectionUtils.isEmpty(firstGroupByColumns)) {
                                for (String firstGroupByColumn : firstGroupByColumns) {
                                    for (RbFieldDataBean fieldDataBean : masterReportDataBean.getColumns()) {
                                        String colTitle = "";
                                        if ((firstGroupByColumn.equals(fieldDataBean.getAlias()))) {
                                            if (!StringUtils.isEmpty(fieldDataBean.getAlias())) {
                                                dataBaseColumns.add(fieldDataBean.getAlias().toLowerCase().replaceAll("\\s+", ""));
                                            } else {
                                                dataBaseColumns.add(fieldDataBean.getEditedFieldLabel());
                                            }
                                            if (fieldDataBean.getAlias() == null || fieldDataBean.getAlias().equals("")) {
                                                colTitle = fieldDataBean.getFieldLabel();
                                            } else {
                                                colTitle = fieldDataBean.getAlias();
                                            }
                                            if (CollectionUtils.isEmpty(hiddenFields) || !hiddenFields.contains(firstGroupByColumn)) {
                                                if (fieldDataBean.getDataType().equals(HkSystemConstantUtil.CustomField.DataType.INT8)) {
                                                    if (!StringUtils.isEmpty(fieldDataBean.getAlias())) {
                                                        TextColumnBuilder<Long> column1 = col.column(colTitle, fieldDataBean.getAlias().toLowerCase().replaceAll("\\s+", ""), type.longType()).setStyle(contentRightAlignStyle)
                                                                .setTitleStyle(stl.style(rootStyle).setVerticalAlignment(VerticalAlignment.MIDDLE)
                                                                        .setBorder(stl.penThin()).setFontSize(8)
                                                                        .setHorizontalAlignment(HorizontalAlignment.CENTER)
                                                                        .setBackgroundColor(new Color(177, 154, 204))
                                                                        .bold()).setTitleFixedHeight(50);
                                                        column1.setStyle(myStyle).setHorizontalAlignment(HorizontalAlignment.RIGHT);
                                                        textColumnBuildersList.add(column1);

                                                        if (fieldDataBean.getShowTotal()) {
                                                            AggregationSubtotalBuilder<Long> sbtColumn = sbt.sum(fieldDataBean.getAlias().toLowerCase().replaceAll("\\s+", ""), Long.class, column1).setLabel(colTitle + " " + "Total");
                                                            subTotalColumnBuilerList.add(sbtColumn);
                                                        }
                                                        gridBuilder.add(column1);
                                                    } else {
                                                        TextColumnBuilder<Long> column1 = col.column(colTitle, fieldDataBean.getEditedFieldLabel(), type.longType()).setStyle(contentRightAlignStyle)
                                                                .setTitleStyle(stl.style(rootStyle).setVerticalAlignment(VerticalAlignment.MIDDLE)
                                                                        .setBorder(stl.penThin()).setFontSize(8)
                                                                        .setHorizontalAlignment(HorizontalAlignment.CENTER)
                                                                        .setBackgroundColor(new Color(177, 154, 204))
                                                                        .bold()).setTitleFixedHeight(50);
                                                        column1.setStyle(myStyle).setHorizontalAlignment(HorizontalAlignment.RIGHT);
                                                        textColumnBuildersList.add(column1);

                                                        if (fieldDataBean.getShowTotal()) {
                                                            AggregationSubtotalBuilder<Long> sbtColumn = sbt.sum(fieldDataBean.getEditedFieldLabel(), Long.class, column1).setLabel(colTitle + " " + "Total");
                                                            subTotalColumnBuilerList.add(sbtColumn);
                                                        }
                                                        gridBuilder.add(column1);
                                                    }
                                                } else if (fieldDataBean.getDataType().equals(HkSystemConstantUtil.CustomField.DataType.VARCHAR)
                                                        || fieldDataBean.getDataType().equals(HkSystemConstantUtil.CustomField.DataType.BOOLEAN)
                                                        || fieldDataBean.getComponentType().equals(HkSystemConstantUtil.CustomField.ComponentType.CURRENCY)) {
                                                    TextColumnBuilder<String> column1;
                                                    if (!StringUtils.isEmpty(fieldDataBean.getAlias())) {
                                                        column1 = col.column(colTitle, fieldDataBean.getAlias().toLowerCase().replaceAll("\\s+", ""), type.stringType())
                                                                .setTitleStyle(stl.style(rootStyle).setVerticalAlignment(VerticalAlignment.MIDDLE)
                                                                        .setBorder(stl.penThin()).setFontSize(8)
                                                                        .setHorizontalAlignment(HorizontalAlignment.CENTER)
                                                                        .setBackgroundColor(new Color(177, 154, 204))
                                                                        .bold()).setTitleFixedHeight(50);
                                                    } else {
                                                        column1 = col.column(colTitle, fieldDataBean.getEditedFieldLabel(), type.stringType())
                                                                .setTitleStyle(stl.style(rootStyle).setVerticalAlignment(VerticalAlignment.MIDDLE)
                                                                        .setBorder(stl.penThin()).setFontSize(8)
                                                                        .setHorizontalAlignment(HorizontalAlignment.CENTER)
                                                                        .setBackgroundColor(new Color(177, 154, 204))
                                                                        .bold()).setTitleFixedHeight(50);
                                                    }
                                                    column1.setStyle(myStyle);
                                                    textColumnBuildersList.add(column1);
                                                    gridBuilder.add(column1);
                                                } else if (fieldDataBean.getDataType().equals(HkSystemConstantUtil.CustomField.DataType.TIMESTAMP)) {
                                                    TextColumnBuilder<String> column1;
                                                    if (!StringUtils.isEmpty(fieldDataBean.getAlias())) {
                                                        column1 = col.column(colTitle, fieldDataBean.getAlias().toLowerCase().replaceAll("\\s+", ""), type.stringType())
                                                                .setTitleStyle(stl.style(rootStyle).setVerticalAlignment(VerticalAlignment.MIDDLE)
                                                                        .setBorder(stl.penThin()).setFontSize(8)
                                                                        .setHorizontalAlignment(HorizontalAlignment.CENTER)
                                                                        .setBackgroundColor(new Color(177, 154, 204))
                                                                        .bold()).setTitleFixedHeight(50);
                                                    } else {
                                                        column1 = col.column(colTitle, fieldDataBean.getEditedFieldLabel(), type.stringType())
                                                                .setTitleStyle(stl.style(rootStyle).setVerticalAlignment(VerticalAlignment.MIDDLE)
                                                                        .setBorder(stl.penThin()).setFontSize(8)
                                                                        .setHorizontalAlignment(HorizontalAlignment.CENTER)
                                                                        .setBackgroundColor(new Color(177, 154, 204))
                                                                        .bold()).setTitleFixedHeight(50);
                                                    }
                                                    column1.setStyle(myStyle);
                                                    textColumnBuildersList.add(column1);
                                                    gridBuilder.add(column1);
                                                } else if (fieldDataBean.getDataType().equals(HkSystemConstantUtil.CustomField.DataType.DOUBLE)) {
                                                    TextColumnBuilder<Double> column1;
                                                    if (fieldDataBean.getComponentType().equals(HkSystemConstantUtil.CustomField.ComponentType.PERCENT)) {
                                                        if (!StringUtils.isEmpty(fieldDataBean.getAlias())) {
                                                            column1 = col.column(colTitle, fieldDataBean.getAlias().toLowerCase().replaceAll("\\s+", ""), type.doubleType()).setValueFormatter(ReportBuilderTemplate.createValueFormatter("%")).setStyle(contentRightAlignStyle)
                                                                    .setTitleStyle(stl.style(rootStyle).setVerticalAlignment(VerticalAlignment.MIDDLE)
                                                                            .setBorder(stl.penThin()).setFontSize(8)
                                                                            .setHorizontalAlignment(HorizontalAlignment.CENTER)
                                                                            .setBackgroundColor(new Color(177, 154, 204))
                                                                            .bold()).setTitleFixedHeight(50);
                                                        } else {
                                                            column1 = col.column(colTitle, fieldDataBean.getEditedFieldLabel(), type.doubleType()).setValueFormatter(ReportBuilderTemplate.createValueFormatter("%")).setStyle(contentRightAlignStyle)
                                                                    .setTitleStyle(stl.style(rootStyle).setVerticalAlignment(VerticalAlignment.MIDDLE)
                                                                            .setBorder(stl.penThin()).setFontSize(8)
                                                                            .setHorizontalAlignment(HorizontalAlignment.CENTER)
                                                                            .setBackgroundColor(new Color(177, 154, 204))
                                                                            .bold()).setTitleFixedHeight(50);

                                                        }
                                                        if (fieldDataBean.getShowTotal()) {
                                                            AggregationSubtotalBuilder<Double> sbtColumn = sbt.sum(fieldDataBean.getAlias().toLowerCase().replaceAll("\\s+", ""), Double.class, column1).setLabel(colTitle + " " + "Total");
                                                            subTotalColumnBuilerList.add(sbtColumn);
                                                        }
                                                        column1.setStyle(myStyle).setHorizontalAlignment(HorizontalAlignment.RIGHT);
                                                        textColumnBuildersList.add(column1);
                                                    } else if (fieldDataBean.getComponentType().equals(HkSystemConstantUtil.CustomField.ComponentType.ANGLE)) {
                                                        if (!StringUtils.isEmpty(fieldDataBean.getAlias())) {
                                                            column1 = col.column(colTitle, fieldDataBean.getAlias().toLowerCase().replaceAll("\\s+", ""), type.doubleType()).setValueFormatter(ReportBuilderTemplate.createValueFormatter("°")).setStyle(contentRightAlignStyle)
                                                                    .setTitleStyle(stl.style(rootStyle).setVerticalAlignment(VerticalAlignment.MIDDLE)
                                                                            .setBorder(stl.penThin()).setFontSize(8)
                                                                            .setHorizontalAlignment(HorizontalAlignment.CENTER)
                                                                            .setBackgroundColor(new Color(177, 154, 204))
                                                                            .bold()).setTitleFixedHeight(50);
                                                        } else {
                                                            column1 = col.column(colTitle, fieldDataBean.getEditedFieldLabel(), type.doubleType()).setValueFormatter(ReportBuilderTemplate.createValueFormatter("°")).setStyle(contentRightAlignStyle)
                                                                    .setTitleStyle(stl.style(rootStyle).setVerticalAlignment(VerticalAlignment.MIDDLE)
                                                                            .setBorder(stl.penThin()).setFontSize(8)
                                                                            .setHorizontalAlignment(HorizontalAlignment.CENTER)
                                                                            .setBackgroundColor(new Color(177, 154, 204))
                                                                            .bold()).setTitleFixedHeight(50);

                                                        }
                                                        if (fieldDataBean.getShowTotal()) {
                                                            AggregationSubtotalBuilder<Double> sbtColumn = sbt.sum(fieldDataBean.getAlias().toLowerCase().replaceAll("\\s+", ""), Double.class, column1).setLabel(colTitle + " " + "Total");
                                                            subTotalColumnBuilerList.add(sbtColumn);
                                                        }
                                                        column1.setStyle(myStyle).setHorizontalAlignment(HorizontalAlignment.RIGHT);
                                                        textColumnBuildersList.add(column1);
                                                    } else if (fieldDataBean.getComponentType().equals(HkSystemConstantUtil.CustomField.ComponentType.CURRENCY)) {
                                                        String label = "";
                                                        String pattern = "";
                                                        //System.out.println("fieldDataBean.getAssociatedCurrency(); 2 ::::" + fieldDataBean.getAssociatedCurrency());
//                                                        if (fieldDataBean.getAssociatedCurrency() != null && currencyIdEntityMap.get(fieldDataBean.getAssociatedCurrency().longValue()) != null) {
//                                                            label = currencyIdEntityMap.get(fieldDataBean.getAssociatedCurrency().longValue()).getSymbol();
//                                                            pattern = currencyIdEntityMap.get(fieldDataBean.getAssociatedCurrency().longValue()).getFormat();
//                                                            //System.out.println("label :" + label + "pattern :" + pattern);
//                                                        }
                                                        if (!StringUtils.isEmpty(fieldDataBean.getAlias())) {
                                                            column1 = col.column(colTitle, fieldDataBean.getAlias().toLowerCase().replaceAll("\\s+", ""), type.doubleType()).setValueFormatter(ReportBuilderTemplate.createCurrencyValueFormatter(label, pattern)).setStyle(contentRightAlignStyle)
                                                                    .setTitleStyle(stl.style(rootStyle).setVerticalAlignment(VerticalAlignment.MIDDLE)
                                                                            .setBorder(stl.penThin()).setFontSize(8)
                                                                            .setHorizontalAlignment(HorizontalAlignment.CENTER)
                                                                            .setBackgroundColor(new Color(177, 154, 204))
                                                                            .bold()).setTitleFixedHeight(50);
                                                        } else {
                                                            column1 = col.column(colTitle, fieldDataBean.getEditedFieldLabel(), type.doubleType()).setValueFormatter(ReportBuilderTemplate.createCurrencyValueFormatter(label, pattern)).setStyle(contentRightAlignStyle)
                                                                    .setTitleStyle(stl.style(rootStyle).setVerticalAlignment(VerticalAlignment.MIDDLE)
                                                                            .setBorder(stl.penThin()).setFontSize(8)
                                                                            .setHorizontalAlignment(HorizontalAlignment.CENTER)
                                                                            .setBackgroundColor(new Color(177, 154, 204))
                                                                            .bold()).setTitleFixedHeight(50);

                                                        }
                                                        if (fieldDataBean.getShowTotal()) {
                                                            AggregationSubtotalBuilder<Double> sbtColumn = sbt.sum(fieldDataBean.getAlias().toLowerCase().replaceAll("\\s+", ""), Double.class, column1).setLabel(colTitle + " " + "Total");
                                                            subTotalColumnBuilerList.add(sbtColumn);
                                                        }
                                                        column1.setStyle(myStyle).setHorizontalAlignment(HorizontalAlignment.RIGHT);
                                                        textColumnBuildersList.add(column1);
                                                    } else {
                                                        if (!StringUtils.isEmpty(fieldDataBean.getAlias())) {
                                                            column1 = col.column(colTitle, fieldDataBean.getAlias().toLowerCase().replaceAll("\\s+", ""), type.doubleType()).setStyle(contentRightAlignStyle)
                                                                    .setTitleStyle(stl.style(rootStyle).setVerticalAlignment(VerticalAlignment.MIDDLE)
                                                                            .setBorder(stl.penThin()).setFontSize(8)
                                                                            .setHorizontalAlignment(HorizontalAlignment.CENTER)
                                                                            .setBackgroundColor(new Color(177, 154, 204))
                                                                            .bold()).setTitleFixedHeight(50);
                                                        } else {
                                                            column1 = col.column(colTitle, fieldDataBean.getEditedFieldLabel(), type.doubleType()).setStyle(contentRightAlignStyle)
                                                                    .setTitleStyle(stl.style(rootStyle).setVerticalAlignment(VerticalAlignment.MIDDLE)
                                                                            .setBorder(stl.penThin()).setFontSize(8)
                                                                            .setHorizontalAlignment(HorizontalAlignment.CENTER)
                                                                            .setBackgroundColor(new Color(177, 154, 204))
                                                                            .bold()).setTitleFixedHeight(50);

                                                        }
                                                        if (fieldDataBean.getShowTotal()) {
                                                            AggregationSubtotalBuilder<Double> sbtColumn = sbt.sum(fieldDataBean.getAlias().toLowerCase().replaceAll("\\s+", ""), Double.class, column1).setLabel(colTitle + " " + "Total");
                                                            subTotalColumnBuilerList.add(sbtColumn);
                                                        }
//                                                    System.out.println("inside double");
                                                        column1.setStyle(myStyle).setHorizontalAlignment(HorizontalAlignment.RIGHT);
                                                        textColumnBuildersList.add(column1);
                                                    }
                                                    gridBuilder.add(column1);

                                                } else {
                                                    TextColumnBuilder<String> column1;
                                                    if (!StringUtils.isEmpty(fieldDataBean.getAlias())) {
                                                        column1 = col.column(colTitle, fieldDataBean.getAlias().toLowerCase().replaceAll("\\s+", ""), type.stringType())
                                                                .setTitleStyle(stl.style(rootStyle).setVerticalAlignment(VerticalAlignment.MIDDLE)
                                                                        .setBorder(stl.penThin()).setFontSize(8)
                                                                        .setHorizontalAlignment(HorizontalAlignment.CENTER)
                                                                        .setBackgroundColor(new Color(177, 154, 204))
                                                                        .bold()).setTitleFixedHeight(50);
                                                    } else {
                                                        column1 = col.column(colTitle, fieldDataBean.getEditedFieldLabel(), type.stringType())
                                                                .setTitleStyle(stl.style(rootStyle).setVerticalAlignment(VerticalAlignment.MIDDLE)
                                                                        .setBorder(stl.penThin()).setFontSize(8)
                                                                        .setHorizontalAlignment(HorizontalAlignment.CENTER)
                                                                        .setBackgroundColor(new Color(177, 154, 204))
                                                                        .bold()).setTitleFixedHeight(50);
                                                    }
                                                    column1.setStyle(myStyle);
                                                    textColumnBuildersList.add(column1);
                                                    gridBuilder.add(column1);
                                                }
                                            }

                                        }
                                    }
                                }
                            }
                        }
                    }
                    if (groupsList.size() > 1) {
                        for (int i = 0; i < groupsList.size(); i++) {
                            Map<String, Object> group = groupsList.get(i);
                            String groupName = group.get("groupName").toString();
                            List<TextColumnBuilder> columnBuilderList = new LinkedList<>();
                            String[] groupColumnArray = group.get("groupItems").toString().split(",");
                            List<String> grpColumns = Arrays.asList(groupColumnArray);
                            for (String grpColumn : grpColumns) {
                                for (RbFieldDataBean fieldDataBean : masterReportDataBean.getColumns()) {
                                    String colTitle = "";
                                    if ((grpColumn.equals(fieldDataBean.getAlias()))) {
                                        if (!StringUtils.isEmpty(fieldDataBean.getAlias())) {
                                            dataBaseColumns.add(fieldDataBean.getAlias().toLowerCase().replaceAll("\\s+", ""));
                                        } else {
                                            dataBaseColumns.add(fieldDataBean.getEditedFieldLabel());
                                        }
                                        if (fieldDataBean.getAlias() == null || fieldDataBean.getAlias().equals("")) {
                                            colTitle = fieldDataBean.getFieldLabel();
                                        } else {
                                            colTitle = fieldDataBean.getAlias();
                                        }
                                        if (CollectionUtils.isEmpty(hiddenFields) || !hiddenFields.contains(grpColumn)) {
                                            if (fieldDataBean.getDataType().equals(HkSystemConstantUtil.CustomField.DataType.INT8)) {
                                                if (!StringUtils.isEmpty(fieldDataBean.getAlias())) {
                                                    TextColumnBuilder<Long> column1 = col.column(colTitle, fieldDataBean.getAlias().toLowerCase().replaceAll("\\s+", ""), type.longType()).setStyle(contentRightAlignStyle)
                                                            .setTitleStyle(stl.style(rootStyle).setVerticalAlignment(VerticalAlignment.MIDDLE)
                                                                    .setBorder(stl.penThin()).setFontSize(8)
                                                                    .setHorizontalAlignment(HorizontalAlignment.CENTER)
                                                                    .setBackgroundColor(new Color(177, 154, 204))
                                                                    .bold()).setTitleFixedHeight(50);
                                                    column1.setStyle(myStyle).setHorizontalAlignment(HorizontalAlignment.RIGHT);
                                                    textColumnBuildersList.add(column1);

                                                    if (fieldDataBean.getShowTotal()) {
                                                        AggregationSubtotalBuilder<Long> sbtColumn = sbt.sum(fieldDataBean.getAlias().toLowerCase().replaceAll("\\s+", ""), Long.class, column1).setLabel(colTitle + " " + "Total");
                                                        subTotalColumnBuilerList.add(sbtColumn);
                                                    }
                                                    columnBuilderList.add(column1);
                                                } else {
                                                    TextColumnBuilder<Long> column1 = col.column(colTitle, fieldDataBean.getEditedFieldLabel(), type.longType()).setStyle(contentRightAlignStyle)
                                                            .setTitleStyle(stl.style(rootStyle).setVerticalAlignment(VerticalAlignment.MIDDLE)
                                                                    .setBorder(stl.penThin()).setFontSize(8)
                                                                    .setHorizontalAlignment(HorizontalAlignment.CENTER)
                                                                    .setBackgroundColor(new Color(177, 154, 204))
                                                                    .bold()).setTitleFixedHeight(50);
                                                    column1.setStyle(myStyle).setHorizontalAlignment(HorizontalAlignment.RIGHT);
                                                    textColumnBuildersList.add(column1);

                                                    if (fieldDataBean.getShowTotal()) {
                                                        AggregationSubtotalBuilder<Long> sbtColumn = sbt.sum(fieldDataBean.getEditedFieldLabel(), Long.class, column1).setLabel(colTitle + " " + "Total");
                                                        subTotalColumnBuilerList.add(sbtColumn);
                                                    }
                                                    columnBuilderList.add(column1);
                                                }
                                            } else if (fieldDataBean.getDataType().equals(HkSystemConstantUtil.CustomField.DataType.VARCHAR)
                                                    || fieldDataBean.getDataType().equals(HkSystemConstantUtil.CustomField.DataType.BOOLEAN)
                                                    || fieldDataBean.getComponentType().equals(HkSystemConstantUtil.CustomField.ComponentType.CURRENCY)) {
                                                TextColumnBuilder<String> column1;
                                                if (!StringUtils.isEmpty(fieldDataBean.getAlias())) {
                                                    column1 = col.column(colTitle, fieldDataBean.getAlias().toLowerCase().replaceAll("\\s+", ""), type.stringType())
                                                            .setTitleStyle(stl.style(rootStyle).setVerticalAlignment(VerticalAlignment.MIDDLE)
                                                                    .setBorder(stl.penThin()).setFontSize(8)
                                                                    .setHorizontalAlignment(HorizontalAlignment.CENTER)
                                                                    .setBackgroundColor(new Color(177, 154, 204))
                                                                    .bold()).setTitleFixedHeight(50);
                                                    column1.setStyle(myStyle);
                                                    textColumnBuildersList.add(column1);
                                                } else {
                                                    column1 = col.column(colTitle, fieldDataBean.getEditedFieldLabel(), type.stringType());
                                                    column1.setStyle(myStyle);
                                                    textColumnBuildersList.add(column1);
                                                }
                                                columnBuilderList.add(column1);
                                            } else if (fieldDataBean.getDataType().equals(HkSystemConstantUtil.CustomField.DataType.TIMESTAMP)) {
                                                if (!StringUtils.isEmpty(fieldDataBean.getAlias())) {
                                                    TextColumnBuilder<String> column1 = col.column(colTitle, fieldDataBean.getAlias().toLowerCase().replaceAll("\\s+", ""), type.stringType())
                                                            .setTitleStyle(stl.style(rootStyle).setVerticalAlignment(VerticalAlignment.MIDDLE)
                                                                    .setBorder(stl.penThin()).setFontSize(8)
                                                                    .setHorizontalAlignment(HorizontalAlignment.CENTER)
                                                                    .setBackgroundColor(new Color(177, 154, 204))
                                                                    .bold()).setTitleFixedHeight(50);
                                                    column1.setStyle(myStyle);
                                                    textColumnBuildersList.add(column1);
                                                    columnBuilderList.add(column1);
                                                } else {
                                                    TextColumnBuilder<String> column1 = col.column(colTitle, fieldDataBean.getEditedFieldLabel(), type.stringType())
                                                            .setTitleStyle(stl.style(rootStyle).setVerticalAlignment(VerticalAlignment.MIDDLE)
                                                                    .setBorder(stl.penThin()).setFontSize(8)
                                                                    .setHorizontalAlignment(HorizontalAlignment.CENTER)
                                                                    .setBackgroundColor(new Color(177, 154, 204))
                                                                    .bold()).setTitleFixedHeight(50);
                                                    column1.setStyle(myStyle);
                                                    textColumnBuildersList.add(column1);
                                                    columnBuilderList.add(column1);
                                                }
                                            } else if (fieldDataBean.getDataType().equals(HkSystemConstantUtil.CustomField.DataType.DOUBLE)) {
                                                TextColumnBuilder<Double> column1;
                                                if (fieldDataBean.getComponentType().equals(HkSystemConstantUtil.CustomField.ComponentType.PERCENT)) {
                                                    if (!StringUtils.isEmpty(fieldDataBean.getAlias())) {
                                                        column1 = col.column(colTitle, fieldDataBean.getAlias().toLowerCase().replaceAll("\\s+", ""), type.doubleType()).setValueFormatter(ReportBuilderTemplate.createValueFormatter("%")).setStyle(contentRightAlignStyle)
                                                                .setTitleStyle(stl.style(rootStyle).setVerticalAlignment(VerticalAlignment.MIDDLE)
                                                                        .setBorder(stl.penThin()).setFontSize(8)
                                                                        .setHorizontalAlignment(HorizontalAlignment.CENTER)
                                                                        .setBackgroundColor(new Color(177, 154, 204))
                                                                        .bold()).setTitleFixedHeight(50);
                                                    } else {
                                                        column1 = col.column(colTitle, fieldDataBean.getEditedFieldLabel(), type.doubleType()).setValueFormatter(ReportBuilderTemplate.createValueFormatter("%")).setStyle(contentRightAlignStyle)
                                                                .setTitleStyle(stl.style(rootStyle).setVerticalAlignment(VerticalAlignment.MIDDLE)
                                                                        .setBorder(stl.penThin()).setFontSize(8)
                                                                        .setHorizontalAlignment(HorizontalAlignment.CENTER)
                                                                        .setBackgroundColor(new Color(177, 154, 204))
                                                                        .bold()).setTitleFixedHeight(50);

                                                    }
                                                    column1.setStyle(myStyle).setHorizontalAlignment(HorizontalAlignment.RIGHT);
                                                    textColumnBuildersList.add(column1);

                                                    if (fieldDataBean.getShowTotal()) {
                                                        AggregationSubtotalBuilder<Double> sbtColumn = sbt.sum(fieldDataBean.getEditedFieldLabel(), Double.class, column1).setLabel(colTitle + " " + "Total");
                                                        subTotalColumnBuilerList.add(sbtColumn);
                                                    }
                                                    columnBuilderList.add(column1);
                                                } else if (fieldDataBean.getComponentType().equals(HkSystemConstantUtil.CustomField.ComponentType.ANGLE)) {
                                                    if (!StringUtils.isEmpty(fieldDataBean.getAlias())) {
                                                        column1 = col.column(colTitle, fieldDataBean.getAlias().toLowerCase().replaceAll("\\s+", ""), type.doubleType()).setValueFormatter(ReportBuilderTemplate.createValueFormatter("°")).setStyle(contentRightAlignStyle)
                                                                .setTitleStyle(stl.style(rootStyle).setVerticalAlignment(VerticalAlignment.MIDDLE)
                                                                        .setBorder(stl.penThin()).setFontSize(8)
                                                                        .setHorizontalAlignment(HorizontalAlignment.CENTER)
                                                                        .setBackgroundColor(new Color(177, 154, 204))
                                                                        .bold()).setTitleFixedHeight(50);
                                                    } else {
                                                        column1 = col.column(colTitle, fieldDataBean.getEditedFieldLabel(), type.doubleType()).setValueFormatter(ReportBuilderTemplate.createValueFormatter("°")).setStyle(contentRightAlignStyle)
                                                                .setTitleStyle(stl.style(rootStyle).setVerticalAlignment(VerticalAlignment.MIDDLE)
                                                                        .setBorder(stl.penThin()).setFontSize(8)
                                                                        .setHorizontalAlignment(HorizontalAlignment.CENTER)
                                                                        .setBackgroundColor(new Color(177, 154, 204))
                                                                        .bold()).setTitleFixedHeight(50);

                                                    }
                                                    column1.setStyle(myStyle).setHorizontalAlignment(HorizontalAlignment.RIGHT);
                                                    textColumnBuildersList.add(column1);

                                                    if (fieldDataBean.getShowTotal()) {
                                                        AggregationSubtotalBuilder<Double> sbtColumn = sbt.sum(fieldDataBean.getEditedFieldLabel(), Double.class, column1).setLabel(colTitle + " " + "Total");
                                                        subTotalColumnBuilerList.add(sbtColumn);
                                                    }
                                                    columnBuilderList.add(column1);
                                                } else if (fieldDataBean.getComponentType().equals(HkSystemConstantUtil.CustomField.ComponentType.CURRENCY)) {
                                                    String label = "";
                                                    String pattern = "";
//                                                    if (fieldDataBean.getAssociatedCurrency() != null && currencyIdEntityMap.get(fieldDataBean.getAssociatedCurrency().longValue()) != null) {
//                                                        label = currencyIdEntityMap.get(fieldDataBean.getAssociatedCurrency().longValue()).getSymbol();
//                                                        pattern = currencyIdEntityMap.get(fieldDataBean.getAssociatedCurrency().longValue()).getFormat();
//                                                    }
                                                    if (!StringUtils.isEmpty(fieldDataBean.getAlias())) {
                                                        column1 = col.column(colTitle, fieldDataBean.getAlias().toLowerCase().replaceAll("\\s+", ""), type.doubleType()).setValueFormatter(ReportBuilderTemplate.createCurrencyValueFormatter(label, pattern)).setStyle(contentRightAlignStyle)
                                                                .setTitleStyle(stl.style(rootStyle).setVerticalAlignment(VerticalAlignment.MIDDLE)
                                                                        .setBorder(stl.penThin()).setFontSize(8)
                                                                        .setHorizontalAlignment(HorizontalAlignment.CENTER)
                                                                        .setBackgroundColor(new Color(177, 154, 204))
                                                                        .bold()).setTitleFixedHeight(50);
                                                    } else {
                                                        column1 = col.column(colTitle, fieldDataBean.getEditedFieldLabel(), type.doubleType()).setValueFormatter(ReportBuilderTemplate.createCurrencyValueFormatter(label, pattern)).setStyle(contentRightAlignStyle)
                                                                .setTitleStyle(stl.style(rootStyle).setVerticalAlignment(VerticalAlignment.MIDDLE)
                                                                        .setBorder(stl.penThin()).setFontSize(8)
                                                                        .setHorizontalAlignment(HorizontalAlignment.CENTER)
                                                                        .setBackgroundColor(new Color(177, 154, 204))
                                                                        .bold()).setTitleFixedHeight(50);
                                                    }
                                                    column1.setStyle(myStyle).setHorizontalAlignment(HorizontalAlignment.RIGHT);
                                                    textColumnBuildersList.add(column1);

                                                    if (fieldDataBean.getShowTotal()) {
                                                        AggregationSubtotalBuilder<Double> sbtColumn = sbt.sum(fieldDataBean.getEditedFieldLabel(), Double.class, column1).setLabel(colTitle + " " + "Total");
                                                        subTotalColumnBuilerList.add(sbtColumn);
                                                    }
                                                    columnBuilderList.add(column1);
                                                } else {
                                                    if (!StringUtils.isEmpty(fieldDataBean.getAlias())) {
                                                        column1 = col.column(colTitle, fieldDataBean.getAlias().toLowerCase().replaceAll("\\s+", ""), type.doubleType()).setStyle(contentRightAlignStyle)
                                                                .setTitleStyle(stl.style(rootStyle).setVerticalAlignment(VerticalAlignment.MIDDLE)
                                                                        .setBorder(stl.penThin()).setFontSize(8)
                                                                        .setHorizontalAlignment(HorizontalAlignment.CENTER)
                                                                        .setBackgroundColor(new Color(177, 154, 204))
                                                                        .bold()).setTitleFixedHeight(50);
                                                        column1.setStyle(myStyle).setHorizontalAlignment(HorizontalAlignment.RIGHT);
                                                        textColumnBuildersList.add(column1);
                                                    } else {
                                                        column1 = col.column(colTitle, fieldDataBean.getEditedFieldLabel(), type.doubleType()).setStyle(contentRightAlignStyle)
                                                                .setTitleStyle(stl.style(rootStyle).setVerticalAlignment(VerticalAlignment.MIDDLE)
                                                                        .setBorder(stl.penThin()).setFontSize(8)
                                                                        .setHorizontalAlignment(HorizontalAlignment.CENTER)
                                                                        .setBackgroundColor(new Color(177, 154, 204))
                                                                        .bold()).setTitleFixedHeight(50);
                                                        column1.setStyle(myStyle).setHorizontalAlignment(HorizontalAlignment.RIGHT);
                                                        textColumnBuildersList.add(column1);

                                                    }
                                                    if (fieldDataBean.getShowTotal()) {
                                                        AggregationSubtotalBuilder<Double> sbtColumn = sbt.sum(fieldDataBean.getEditedFieldLabel(), Double.class, column1).setLabel(colTitle + " " + "Total");
                                                        subTotalColumnBuilerList.add(sbtColumn);
                                                    }
                                                    columnBuilderList.add(column1);
                                                }

                                            } else {
                                                if (!StringUtils.isEmpty(fieldDataBean.getAlias())) {
                                                    TextColumnBuilder<String> column1 = col.column(colTitle, fieldDataBean.getAlias().toLowerCase().replaceAll("\\s+", ""), type.stringType())
                                                            .setTitleStyle(stl.style(rootStyle).setVerticalAlignment(VerticalAlignment.MIDDLE)
                                                                    .setBorder(stl.penThin()).setFontSize(8)
                                                                    .setHorizontalAlignment(HorizontalAlignment.CENTER)
                                                                    .setBackgroundColor(new Color(177, 154, 204))
                                                                    .bold()).setTitleFixedHeight(50);
                                                    column1.setStyle(myStyle);
                                                    textColumnBuildersList.add(column1);
                                                    columnBuilderList.add(column1);
                                                } else {
                                                    TextColumnBuilder<String> column1 = col.column(colTitle, fieldDataBean.getEditedFieldLabel(), type.stringType())
                                                            .setTitleStyle(stl.style(rootStyle).setVerticalAlignment(VerticalAlignment.MIDDLE)
                                                                    .setBorder(stl.penThin()).setFontSize(8)
                                                                    .setHorizontalAlignment(HorizontalAlignment.CENTER)
                                                                    .setBackgroundColor(new Color(177, 154, 204))
                                                                    .bold()).setTitleFixedHeight(50);
                                                    column1.setStyle(myStyle);
                                                    textColumnBuildersList.add(column1);
                                                    columnBuilderList.add(column1);
                                                }
                                            }
                                        }

                                    }
                                }
                            }
                            TextColumnBuilder[] textBuilders = columnBuilderList.toArray(new TextColumnBuilder[columnBuilderList.size()]);
                            ColumnTitleGroupBuilder GrpTitlesBuilder = grid.titleGroup(groupName, textBuilders).setTitleFixedHeight(50).setTitleStyle(stl.style(rootStyle).setVerticalAlignment(VerticalAlignment.MIDDLE)
                                    .setBorder(stl.penThin()).setFontSize(8).setTopPadding(5)
                                    .setHorizontalAlignment(HorizontalAlignment.CENTER)
                                    .setVerticalAlignment(VerticalAlignment.MIDDLE)
                                    .setBackgroundColor(new Color(177, 154, 204))
                                    .bold());
                            gridBuilder.add(GrpTitlesBuilder);
                        }
                    }
                    tempList.removeAll(dataBaseColumns);
                    System.out.println("tempList : " + tempList);
                    for (String tempList1 : tempList) {
                        for (RbFieldDataBean fieldDataBean : masterReportDataBean.getColumns()) {
                            String colTitle = "";
                            char lastCharacter = tempList1.charAt(tempList1.length() - 1);
                            if ((tempList1.equals(fieldDataBean.getAlias().toLowerCase().replaceAll("\\s+", ""))) || (lastCharacter == '1' && isContain(fieldDataBean.getAlias().toLowerCase().replaceAll("\\s+", ""), tempList1.substring(0, tempList1.length() - 1)))) {
                                if ((tempList1.equals(fieldDataBean.getAlias().toLowerCase().replaceAll("\\s+", "")))) {
                                    if (!StringUtils.isEmpty(fieldDataBean.getAlias())) {
                                        dataBaseColumns.add(fieldDataBean.getAlias().toLowerCase().replaceAll("\\s+", ""));
                                    } else {
                                        dataBaseColumns.add(fieldDataBean.getEditedFieldLabel());
                                    }
                                } else if ((lastCharacter == '1' && isContain(fieldDataBean.getAlias().toLowerCase().replaceAll("\\s+", ""), tempList1.substring(0, tempList1.length() - 1)))) {
                                    if (!StringUtils.isEmpty(fieldDataBean.getAlias())) {
                                        dataBaseColumns.add(fieldDataBean.getAlias().toLowerCase().replaceAll("\\s+", "") + "1");
                                    } else {
                                        dataBaseColumns.add(fieldDataBean.getEditedFieldLabel() + "1");
                                    }
                                }
                            }
                            if ((tempList1.equals(fieldDataBean.getAlias().toLowerCase().replaceAll("\\s+", ""))) ) {
                                if (fieldDataBean.getAlias() == null || fieldDataBean.getAlias().equals("")) {
                                    colTitle = fieldDataBean.getFieldLabel();
                                } else {
                                    colTitle = fieldDataBean.getAlias();
                                }
                                if (CollectionUtils.isEmpty(hiddenFields) || !hiddenFields.contains(tempList1)) {
                                    if (fieldDataBean.getDataType().equals(HkSystemConstantUtil.CustomField.DataType.INT8)) {
                                        if (!StringUtils.isEmpty(fieldDataBean.getAlias())) {
                                            TextColumnBuilder<Long> column1 = col.column(colTitle, fieldDataBean.getAlias().toLowerCase().replaceAll("\\s+", ""), type.longType()).setStyle(contentRightAlignStyle)
                                                    .setTitleStyle(stl.style(rootStyle).setVerticalAlignment(VerticalAlignment.MIDDLE)
                                                            .setBorder(stl.penThin()).setFontSize(8)
                                                            .setHorizontalAlignment(HorizontalAlignment.CENTER)
                                                            .setBackgroundColor(new Color(177, 154, 204))
                                                            .bold());
                                            column1.setStyle(myStyle).setHorizontalAlignment(HorizontalAlignment.RIGHT);
                                            textColumnBuildersList.add(column1);

                                            if (fieldDataBean.getShowTotal()) {
                                                AggregationSubtotalBuilder<Long> sbtColumn = sbt.sum(fieldDataBean.getAlias().toLowerCase().replaceAll("\\s+", ""), Long.class, column1).setLabel(colTitle + " " + "Total");
                                                subTotalColumnBuilerList.add(sbtColumn);
                                            }
                                            gridBuilder.add(column1);
                                        } else {
                                            TextColumnBuilder<Long> column1 = col.column(colTitle, fieldDataBean.getEditedFieldLabel(), type.longType()).setStyle(contentRightAlignStyle)
                                                    .setTitleStyle(stl.style(rootStyle).setVerticalAlignment(VerticalAlignment.MIDDLE)
                                                            .setBorder(stl.penThin()).setFontSize(8)
                                                            .setHorizontalAlignment(HorizontalAlignment.CENTER)
                                                            .setBackgroundColor(new Color(177, 154, 204))
                                                            .bold());
                                            column1.setStyle(myStyle).setHorizontalAlignment(HorizontalAlignment.RIGHT);
                                            textColumnBuildersList.add(column1);

                                            if (fieldDataBean.getShowTotal()) {
                                                AggregationSubtotalBuilder<Long> sbtColumn = sbt.sum(fieldDataBean.getEditedFieldLabel(), Long.class, column1).setLabel(colTitle + " " + "Total");
                                                subTotalColumnBuilerList.add(sbtColumn);
                                            }
                                            gridBuilder.add(column1);
                                        }
                                    } else if (fieldDataBean.getDataType().equals(HkSystemConstantUtil.CustomField.DataType.VARCHAR)
                                            || fieldDataBean.getDataType().equals(HkSystemConstantUtil.CustomField.DataType.BOOLEAN)
                                            || fieldDataBean.getComponentType().equals(HkSystemConstantUtil.CustomField.ComponentType.CURRENCY)) {
                                        TextColumnBuilder<String> column1;
                                        if (!StringUtils.isEmpty(fieldDataBean.getAlias())) {
                                            column1 = col.column(colTitle, fieldDataBean.getAlias().toLowerCase().replaceAll("\\s+", ""), type.stringType())
                                                    .setTitleStyle(stl.style(rootStyle).setVerticalAlignment(VerticalAlignment.MIDDLE)
                                                            .setBorder(stl.penThin()).setFontSize(8)
                                                            .setHorizontalAlignment(HorizontalAlignment.CENTER)
                                                            .setBackgroundColor(new Color(177, 154, 204))
                                                            .bold());
                                            column1.setStyle(myStyle);
                                            textColumnBuildersList.add(column1);
                                        } else {
                                            column1 = col.column(colTitle, fieldDataBean.getEditedFieldLabel(), type.stringType())
                                                    .setTitleStyle(stl.style(rootStyle).setVerticalAlignment(VerticalAlignment.MIDDLE)
                                                            .setBorder(stl.penThin()).setFontSize(8)
                                                            .setHorizontalAlignment(HorizontalAlignment.CENTER)
                                                            .setBackgroundColor(new Color(177, 154, 204))
                                                            .bold());
                                            column1.setStyle(myStyle);
                                            textColumnBuildersList.add(column1);
                                        }
                                        gridBuilder.add(column1);
                                    } else if (fieldDataBean.getDataType().equals(HkSystemConstantUtil.CustomField.DataType.TIMESTAMP)) {
                                        if (!StringUtils.isEmpty(fieldDataBean.getAlias())) {
                                            TextColumnBuilder<String> column1 = col.column(colTitle, fieldDataBean.getAlias().toLowerCase().replaceAll("\\s+", ""), type.stringType())
                                                    .setTitleStyle(stl.style(rootStyle).setVerticalAlignment(VerticalAlignment.MIDDLE)
                                                            .setBorder(stl.penThin()).setFontSize(8)
                                                            .setHorizontalAlignment(HorizontalAlignment.CENTER)
                                                            .setBackgroundColor(new Color(177, 154, 204))
                                                            .bold());
                                            column1.setStyle(myStyle);
                                            textColumnBuildersList.add(column1);
                                            gridBuilder.add(column1);
                                        } else {
                                            TextColumnBuilder<String> column1 = col.column(colTitle, fieldDataBean.getEditedFieldLabel(), type.stringType())
                                                    .setTitleStyle(stl.style(rootStyle).setVerticalAlignment(VerticalAlignment.MIDDLE)
                                                            .setBorder(stl.penThin()).setFontSize(8)
                                                            .setHorizontalAlignment(HorizontalAlignment.CENTER)
                                                            .setBackgroundColor(new Color(177, 154, 204))
                                                            .bold());
                                            column1.setStyle(myStyle);
                                            textColumnBuildersList.add(column1);
                                            gridBuilder.add(column1);
                                        }
                                    } else if (fieldDataBean.getDataType().equals(HkSystemConstantUtil.CustomField.DataType.DOUBLE)) {
                                        TextColumnBuilder<Double> column1;
                                        if (fieldDataBean.getComponentType().equals(HkSystemConstantUtil.CustomField.ComponentType.PERCENT)) {
                                            if (!StringUtils.isEmpty(fieldDataBean.getAlias())) {
                                                column1 = col.column(colTitle, fieldDataBean.getAlias().toLowerCase().replaceAll("\\s+", ""), type.doubleType()).setValueFormatter(ReportBuilderTemplate.createValueFormatter("%")).setStyle(contentRightAlignStyle)
                                                        .setTitleStyle(stl.style(rootStyle).setVerticalAlignment(VerticalAlignment.MIDDLE)
                                                                .setBorder(stl.penThin()).setFontSize(8)
                                                                .setHorizontalAlignment(HorizontalAlignment.CENTER)
                                                                .setBackgroundColor(new Color(177, 154, 204))
                                                                .bold());
                                                column1.setStyle(myStyle).setHorizontalAlignment(HorizontalAlignment.RIGHT);
                                                textColumnBuildersList.add(column1);
                                            } else {
                                                column1 = col.column(colTitle, fieldDataBean.getEditedFieldLabel(), type.doubleType()).setValueFormatter(ReportBuilderTemplate.createValueFormatter("%")).setStyle(contentRightAlignStyle)
                                                        .setTitleStyle(stl.style(rootStyle).setVerticalAlignment(VerticalAlignment.MIDDLE)
                                                                .setBorder(stl.penThin()).setFontSize(8)
                                                                .setHorizontalAlignment(HorizontalAlignment.CENTER)
                                                                .setBackgroundColor(new Color(177, 154, 204))
                                                                .bold());
                                                column1.setStyle(myStyle).setHorizontalAlignment(HorizontalAlignment.RIGHT);
                                                textColumnBuildersList.add(column1);

                                            }
                                            if (fieldDataBean.getShowTotal()) {
                                                AggregationSubtotalBuilder<Double> sbtColumn = sbt.sum(fieldDataBean.getEditedFieldLabel(), Double.class, column1).setLabel(colTitle + " " + "Total");
                                                subTotalColumnBuilerList.add(sbtColumn);
                                            }
//                                            gridBuilder.add(column1);
                                        } else if (fieldDataBean.getComponentType().equals(HkSystemConstantUtil.CustomField.ComponentType.ANGLE)) {
                                            if (!StringUtils.isEmpty(fieldDataBean.getAlias())) {
                                                column1 = col.column(colTitle, fieldDataBean.getAlias().toLowerCase().replaceAll("\\s+", ""), type.doubleType()).setValueFormatter(ReportBuilderTemplate.createValueFormatter("°")).setStyle(contentRightAlignStyle)
                                                        .setTitleStyle(stl.style(rootStyle).setVerticalAlignment(VerticalAlignment.MIDDLE)
                                                                .setBorder(stl.penThin()).setFontSize(8)
                                                                .setHorizontalAlignment(HorizontalAlignment.CENTER)
                                                                .setBackgroundColor(new Color(177, 154, 204))
                                                                .bold());
                                                column1.setStyle(myStyle).setHorizontalAlignment(HorizontalAlignment.RIGHT);
                                                textColumnBuildersList.add(column1);
                                            } else {
                                                column1 = col.column(colTitle, fieldDataBean.getEditedFieldLabel(), type.doubleType()).setValueFormatter(ReportBuilderTemplate.createValueFormatter("°")).setStyle(contentRightAlignStyle)
                                                        .setTitleStyle(stl.style(rootStyle).setVerticalAlignment(VerticalAlignment.MIDDLE)
                                                                .setBorder(stl.penThin()).setFontSize(8)
                                                                .setHorizontalAlignment(HorizontalAlignment.CENTER)
                                                                .setBackgroundColor(new Color(177, 154, 204))
                                                                .bold());
                                                column1.setStyle(myStyle).setHorizontalAlignment(HorizontalAlignment.RIGHT);
                                                textColumnBuildersList.add(column1);

                                            }
                                            if (fieldDataBean.getShowTotal()) {
                                                AggregationSubtotalBuilder<Double> sbtColumn = sbt.sum(fieldDataBean.getEditedFieldLabel(), Double.class, column1).setLabel(colTitle + " " + "Total");
                                                subTotalColumnBuilerList.add(sbtColumn);
                                            }
//                                            gridBuilder.add(column1);
                                        } else if (fieldDataBean.getComponentType().equals(HkSystemConstantUtil.CustomField.ComponentType.CURRENCY)) {
                                            String label = "";
                                            String pattern = "";
//                                            if (fieldDataBean.getAssociatedCurrency() != null && currencyIdEntityMap.get(fieldDataBean.getAssociatedCurrency().longValue()) != null) {
//                                                label = currencyIdEntityMap.get(fieldDataBean.getAssociatedCurrency().longValue()).getSymbol();
//                                                pattern = currencyIdEntityMap.get(fieldDataBean.getAssociatedCurrency().longValue()).getFormat();
//                                            }
                                            if (!StringUtils.isEmpty(fieldDataBean.getAlias())) {
                                                column1 = col.column(colTitle, fieldDataBean.getAlias().toLowerCase().replaceAll("\\s+", ""), type.doubleType()).setValueFormatter(ReportBuilderTemplate.createCurrencyValueFormatter(label, pattern)).setStyle(contentRightAlignStyle)
                                                        .setTitleStyle(stl.style(rootStyle).setVerticalAlignment(VerticalAlignment.MIDDLE)
                                                                .setBorder(stl.penThin()).setFontSize(8)
                                                                .setHorizontalAlignment(HorizontalAlignment.CENTER)
                                                                .setBackgroundColor(new Color(177, 154, 204))
                                                                .bold());
                                            } else {
                                                column1 = col.column(colTitle, fieldDataBean.getEditedFieldLabel(), type.doubleType()).setValueFormatter(ReportBuilderTemplate.createCurrencyValueFormatter(label, pattern)).setStyle(contentRightAlignStyle)
                                                        .setTitleStyle(stl.style(rootStyle).setVerticalAlignment(VerticalAlignment.MIDDLE)
                                                                .setBorder(stl.penThin()).setFontSize(8)
                                                                .setHorizontalAlignment(HorizontalAlignment.CENTER)
                                                                .setBackgroundColor(new Color(177, 154, 204))
                                                                .bold());
                                            }
                                            column1.setStyle(myStyle).setHorizontalAlignment(HorizontalAlignment.RIGHT);
                                            textColumnBuildersList.add(column1);

                                            if (fieldDataBean.getShowTotal()) {
                                                AggregationSubtotalBuilder<Double> sbtColumn = sbt.sum(fieldDataBean.getEditedFieldLabel(), Double.class, column1).setLabel(colTitle + " " + "Total");
                                                subTotalColumnBuilerList.add(sbtColumn);
                                            }
//                                            gridBuilder.add(column1);
                                        } else {
                                            if (!StringUtils.isEmpty(fieldDataBean.getAlias())) {
                                                column1 = col.column(colTitle, fieldDataBean.getAlias().toLowerCase().replaceAll("\\s+", ""), type.doubleType()).setStyle(contentRightAlignStyle)
                                                        .setTitleStyle(stl.style(rootStyle).setVerticalAlignment(VerticalAlignment.MIDDLE)
                                                                .setBorder(stl.penThin()).setFontSize(8)
                                                                .setHorizontalAlignment(HorizontalAlignment.CENTER)
                                                                .setBackgroundColor(new Color(177, 154, 204))
                                                                .bold());
                                            } else {
                                                column1 = col.column(colTitle, fieldDataBean.getEditedFieldLabel(), type.doubleType()).setStyle(contentRightAlignStyle)
                                                        .setTitleStyle(stl.style(rootStyle).setVerticalAlignment(VerticalAlignment.MIDDLE)
                                                                .setBorder(stl.penThin()).setFontSize(8)
                                                                .setHorizontalAlignment(HorizontalAlignment.CENTER)
                                                                .setBackgroundColor(new Color(177, 154, 204))
                                                                .bold());
                                            }
//                                        System.out.println("inside double");
                                            column1.setStyle(myStyle).setHorizontalAlignment(HorizontalAlignment.RIGHT);
                                            textColumnBuildersList.add(column1);

                                            if (fieldDataBean.getShowTotal()) {
                                                AggregationSubtotalBuilder<Double> sbtColumn = sbt.sum(fieldDataBean.getEditedFieldLabel(), Double.class, column1).setLabel(colTitle + " " + "Total");
                                                subTotalColumnBuilerList.add(sbtColumn);
                                            }
                                        }
                                        gridBuilder.add(column1);
                                    } else {
                                        if (!StringUtils.isEmpty(fieldDataBean.getAlias())) {
                                            TextColumnBuilder<String> column1 = col.column(colTitle, fieldDataBean.getAlias().toLowerCase().replaceAll("\\s+", ""), type.stringType())
                                                    .setTitleStyle(stl.style(rootStyle).setVerticalAlignment(VerticalAlignment.MIDDLE)
                                                            .setBorder(stl.penThin()).setFontSize(8)
                                                            .setHorizontalAlignment(HorizontalAlignment.CENTER)
                                                            .setBackgroundColor(new Color(177, 154, 204))
                                                            .bold());
                                            column1.setStyle(myStyle);
                                            textColumnBuildersList.add(column1);
                                            gridBuilder.add(column1);
                                        } else {
                                            TextColumnBuilder<String> column1 = col.column(colTitle, fieldDataBean.getEditedFieldLabel(), type.stringType())
                                                    .setTitleStyle(stl.style(rootStyle).setVerticalAlignment(VerticalAlignment.MIDDLE)
                                                            .setBorder(stl.penThin()).setFontSize(8)
                                                            .setHorizontalAlignment(HorizontalAlignment.CENTER)
                                                            .setBackgroundColor(new Color(177, 154, 204))
                                                            .bold());
                                            column1.setStyle(myStyle);
                                            textColumnBuildersList.add(column1);
                                            gridBuilder.add(column1);
                                        }
                                    }
                                }

                            }
                        }
                    }
                } else {
                    if (!CollectionUtils.isEmpty(columnList)) {
                        if (!CollectionUtils.isEmpty(masterReportDataBean.getColumns())) {
                            for (String columns : columnList) {
                                int oldSeqCount = 0;
                                int newSeqCount = 0;
                                for (RbFieldDataBean fieldDataBean : masterReportDataBean.getColumns()) {
                                    String colTitle = "";
                                    char lastCharacter = columns.charAt(columns.length() - 1);
                                    if ((columns.equals(fieldDataBean.getAlias())) || (lastCharacter == '1' && isContain(fieldDataBean.getAlias(), columns.substring(0, columns.length() - 1)))) {
                                        if (columns.equals(fieldDataBean.getAlias())) {
                                            if (!StringUtils.isEmpty(fieldDataBean.getAlias())) {
                                                dataBaseColumns.add(fieldDataBean.getAlias().toLowerCase().replaceAll("\\s+", ""));
                                            } else {
                                                dataBaseColumns.add(fieldDataBean.getEditedFieldLabel());
                                            }
                                        } else if ((lastCharacter == '1' && isContain(fieldDataBean.getAlias().toLowerCase().replaceAll("\\s+", ""), columns.substring(0, columns.length() - 1)))) {
                                            if (!StringUtils.isEmpty(fieldDataBean.getAlias())) {
                                                dataBaseColumns.add(fieldDataBean.getAlias().toLowerCase().replaceAll("\\s+", "") + "1");
                                            } else {
                                                dataBaseColumns.add(fieldDataBean.getEditedFieldLabel() + "1");
                                            }
                                        }
                                    }
                                    if ((columns.equals(fieldDataBean.getAlias()))) {
                                        if (CollectionUtils.isEmpty(hiddenFields) || !hiddenFields.contains(columns)) {
                                            if (oldSeqCount == 0) {
                                                oldSeqCount = fieldDataBean.getFieldSequence();
                                                if (fieldDataBean.getAlias() == null || fieldDataBean.getAlias().equals("")) {
                                                    colTitle = fieldDataBean.getFieldLabel();
                                                } else {
                                                    colTitle = fieldDataBean.getAlias();
                                                }
                                                if (fieldDataBean.getDataType().equals(HkSystemConstantUtil.CustomField.DataType.INT8)) {
                                                    if (!StringUtils.isEmpty(fieldDataBean.getAlias())) {
                                                        TextColumnBuilder<Long> column1 = col.column(colTitle, fieldDataBean.getAlias().toLowerCase().replaceAll("\\s+", ""), type.longType()).setStyle(contentRightAlignStyle)
                                                                .setTitleStyle(stl.style(rootStyle).setVerticalAlignment(VerticalAlignment.MIDDLE)
                                                                        .setBorder(stl.penThin()).setFontSize(8)
                                                                        .setHorizontalAlignment(HorizontalAlignment.CENTER)
                                                                        .setBackgroundColor(new Color(177, 154, 204))
                                                                        .bold());
                                                        column1.setStyle(myStyle).setHorizontalAlignment(HorizontalAlignment.RIGHT);
                                                        textColumnBuildersList.add(column1);

                                                        if (fieldDataBean.getShowTotal()) {
                                                            AggregationSubtotalBuilder<Long> sbtColumn = sbt.sum(fieldDataBean.getAlias().toLowerCase().replaceAll("\\s+", ""), Long.class, column1).setLabel(colTitle + " " + "Total");
                                                            subTotalColumnBuilerList.add(sbtColumn);
                                                        }
                                                    } else {
                                                        TextColumnBuilder<Long> column1 = col.column(colTitle, fieldDataBean.getEditedFieldLabel(), type.longType()).setStyle(contentRightAlignStyle)
                                                                .setTitleStyle(stl.style(rootStyle).setVerticalAlignment(VerticalAlignment.MIDDLE)
                                                                        .setBorder(stl.penThin()).setFontSize(8)
                                                                        .setHorizontalAlignment(HorizontalAlignment.CENTER)
                                                                        .setBackgroundColor(new Color(177, 154, 204))
                                                                        .bold());
                                                        column1.setStyle(myStyle).setHorizontalAlignment(HorizontalAlignment.RIGHT);
                                                        textColumnBuildersList.add(column1);

                                                        if (fieldDataBean.getShowTotal()) {
                                                            AggregationSubtotalBuilder<Long> sbtColumn = sbt.sum(fieldDataBean.getEditedFieldLabel(), Long.class, column1).setLabel(colTitle + " " + "Total");
                                                            subTotalColumnBuilerList.add(sbtColumn);
                                                        }
                                                    }
                                                } else if (fieldDataBean.getDataType().equals(HkSystemConstantUtil.CustomField.DataType.VARCHAR)
                                                        || fieldDataBean.getDataType().equals(HkSystemConstantUtil.CustomField.DataType.BOOLEAN)
                                                        || fieldDataBean.getComponentType().equals(HkSystemConstantUtil.CustomField.ComponentType.CURRENCY)) {
                                                    TextColumnBuilder<String> column1;
                                                    if (!StringUtils.isEmpty(fieldDataBean.getAlias())) {
                                                        column1 = col.column(colTitle, fieldDataBean.getAlias().toLowerCase().replaceAll("\\s+", ""), type.stringType())
                                                                .setTitleStyle(stl.style(rootStyle).setVerticalAlignment(VerticalAlignment.MIDDLE)
                                                                        .setBorder(stl.penThin()).setFontSize(8)
                                                                        .setHorizontalAlignment(HorizontalAlignment.CENTER)
                                                                        .setBackgroundColor(new Color(177, 154, 204))
                                                                        .bold());
                                                        column1.setStyle(myStyle);
                                                        textColumnBuildersList.add(column1);
                                                    } else {
                                                        column1 = col.column(colTitle, fieldDataBean.getEditedFieldLabel(), type.stringType())
                                                                .setTitleStyle(stl.style(rootStyle).setVerticalAlignment(VerticalAlignment.MIDDLE)
                                                                        .setBorder(stl.penThin()).setFontSize(8)
                                                                        .setHorizontalAlignment(HorizontalAlignment.CENTER)
                                                                        .setBackgroundColor(new Color(177, 154, 204))
                                                                        .bold());
                                                        column1.setStyle(myStyle);
                                                        textColumnBuildersList.add(column1);
                                                    }
                                                } else if (fieldDataBean.getDataType().equals(HkSystemConstantUtil.CustomField.DataType.TIMESTAMP)) {
                                                    if (!StringUtils.isEmpty(fieldDataBean.getAlias())) {
                                                        TextColumnBuilder<String> column1 = col.column(colTitle, fieldDataBean.getAlias().toLowerCase().replaceAll("\\s+", ""), type.stringType())
                                                                .setTitleStyle(stl.style(rootStyle).setVerticalAlignment(VerticalAlignment.MIDDLE)
                                                                        .setBorder(stl.penThin()).setFontSize(8)
                                                                        .setHorizontalAlignment(HorizontalAlignment.CENTER)
                                                                        .setBackgroundColor(new Color(177, 154, 204))
                                                                        .bold());
                                                        column1.setStyle(myStyle);
                                                        textColumnBuildersList.add(column1);
                                                    } else {
                                                        TextColumnBuilder<String> column1 = col.column(colTitle, fieldDataBean.getEditedFieldLabel(), type.stringType())
                                                                .setTitleStyle(stl.style(rootStyle).setVerticalAlignment(VerticalAlignment.MIDDLE)
                                                                        .setBorder(stl.penThin()).setFontSize(8)
                                                                        .setHorizontalAlignment(HorizontalAlignment.CENTER)
                                                                        .setBackgroundColor(new Color(177, 154, 204))
                                                                        .bold());
                                                        column1.setStyle(myStyle);
                                                        textColumnBuildersList.add(column1);
                                                    }
                                                } else if (fieldDataBean.getDataType().equals(HkSystemConstantUtil.CustomField.DataType.DOUBLE)) {
                                                    TextColumnBuilder<Double> column1;
                                                    if (fieldDataBean.getComponentType().equals(HkSystemConstantUtil.CustomField.ComponentType.PERCENT)) {
                                                        if (!StringUtils.isEmpty(fieldDataBean.getAlias())) {
                                                            column1 = col.column(colTitle, fieldDataBean.getAlias().toLowerCase().replaceAll("\\s+", ""), type.doubleType()).setValueFormatter(ReportBuilderTemplate.createValueFormatter("%")).setStyle(contentRightAlignStyle)
                                                                    .setTitleStyle(stl.style(rootStyle).setVerticalAlignment(VerticalAlignment.MIDDLE)
                                                                            .setBorder(stl.penThin()).setFontSize(8)
                                                                            .setHorizontalAlignment(HorizontalAlignment.CENTER)
                                                                            .setBackgroundColor(new Color(177, 154, 204))
                                                                            .bold());
                                                            column1.setStyle(myStyle).setHorizontalAlignment(HorizontalAlignment.RIGHT);
                                                            textColumnBuildersList.add(column1);
                                                        } else {
                                                            column1 = col.column(colTitle, fieldDataBean.getEditedFieldLabel(), type.doubleType()).setValueFormatter(ReportBuilderTemplate.createValueFormatter("%")).setStyle(contentRightAlignStyle)
                                                                    .setTitleStyle(stl.style(rootStyle).setVerticalAlignment(VerticalAlignment.MIDDLE)
                                                                            .setBorder(stl.penThin()).setFontSize(8)
                                                                            .setHorizontalAlignment(HorizontalAlignment.CENTER)
                                                                            .setBackgroundColor(new Color(177, 154, 204))
                                                                            .bold());
                                                            column1.setStyle(myStyle).setHorizontalAlignment(HorizontalAlignment.RIGHT);
                                                            textColumnBuildersList.add(column1);

                                                        }
                                                        if (fieldDataBean.getShowTotal()) {
                                                            AggregationSubtotalBuilder<Double> sbtColumn = sbt.sum(fieldDataBean.getEditedFieldLabel(), Double.class, column1).setLabel(colTitle + " " + "Total");
                                                            subTotalColumnBuilerList.add(sbtColumn);
                                                        }
                                                    } else if (fieldDataBean.getComponentType().equals(HkSystemConstantUtil.CustomField.ComponentType.ANGLE)) {
                                                        if (!StringUtils.isEmpty(fieldDataBean.getAlias())) {
                                                            column1 = col.column(colTitle, fieldDataBean.getAlias().toLowerCase().replaceAll("\\s+", ""), type.doubleType()).setValueFormatter(ReportBuilderTemplate.createValueFormatter("°")).setStyle(contentRightAlignStyle)
                                                                    .setTitleStyle(stl.style(rootStyle).setVerticalAlignment(VerticalAlignment.MIDDLE)
                                                                            .setBorder(stl.penThin()).setFontSize(8)
                                                                            .setHorizontalAlignment(HorizontalAlignment.CENTER)
                                                                            .setBackgroundColor(new Color(177, 154, 204))
                                                                            .bold());
                                                        } else {
                                                            column1 = col.column(colTitle, fieldDataBean.getEditedFieldLabel(), type.doubleType()).setValueFormatter(ReportBuilderTemplate.createValueFormatter("°")).setStyle(contentRightAlignStyle)
                                                                    .setTitleStyle(stl.style(rootStyle).setVerticalAlignment(VerticalAlignment.MIDDLE)
                                                                            .setBorder(stl.penThin()).setFontSize(8)
                                                                            .setHorizontalAlignment(HorizontalAlignment.CENTER)
                                                                            .setBackgroundColor(new Color(177, 154, 204))
                                                                            .bold());
                                                            column1.setStyle(myStyle).setHorizontalAlignment(HorizontalAlignment.RIGHT);
                                                            textColumnBuildersList.add(column1);

                                                        }
                                                        if (fieldDataBean.getShowTotal()) {
                                                            AggregationSubtotalBuilder<Double> sbtColumn = sbt.sum(fieldDataBean.getEditedFieldLabel(), Double.class, column1).setLabel(colTitle + " " + "Total");
                                                            subTotalColumnBuilerList.add(sbtColumn);
                                                        }
                                                    } else if (fieldDataBean.getComponentType().equals(HkSystemConstantUtil.CustomField.ComponentType.CURRENCY)) {
                                                        String label = "";
                                                        String pattern = "";
//                                                        if (fieldDataBean.getAssociatedCurrency() != null && currencyIdEntityMap.get(fieldDataBean.getAssociatedCurrency().longValue()) != null) {
//                                                            label = currencyIdEntityMap.get(fieldDataBean.getAssociatedCurrency().longValue()).getSymbol();
//                                                            pattern = currencyIdEntityMap.get(fieldDataBean.getAssociatedCurrency().longValue()).getFormat();
//                                                        }
                                                        if (!StringUtils.isEmpty(fieldDataBean.getAlias())) {
                                                            column1 = col.column(colTitle, fieldDataBean.getAlias().toLowerCase().replaceAll("\\s+", ""), type.doubleType()).setValueFormatter(ReportBuilderTemplate.createCurrencyValueFormatter(label, pattern)).setStyle(contentRightAlignStyle)
                                                                    .setTitleStyle(stl.style(rootStyle).setVerticalAlignment(VerticalAlignment.MIDDLE)
                                                                            .setBorder(stl.penThin()).setFontSize(8)
                                                                            .setHorizontalAlignment(HorizontalAlignment.CENTER)
                                                                            .setBackgroundColor(new Color(177, 154, 204))
                                                                            .bold());
                                                        } else {
                                                            column1 = col.column(colTitle, fieldDataBean.getEditedFieldLabel(), type.doubleType()).setValueFormatter(ReportBuilderTemplate.createCurrencyValueFormatter(label, pattern)).setStyle(contentRightAlignStyle)
                                                                    .setTitleStyle(stl.style(rootStyle).setVerticalAlignment(VerticalAlignment.MIDDLE)
                                                                            .setBorder(stl.penThin()).setFontSize(8)
                                                                            .setHorizontalAlignment(HorizontalAlignment.CENTER)
                                                                            .setBackgroundColor(new Color(177, 154, 204))
                                                                            .bold());
                                                        }
                                                        column1.setStyle(myStyle).setHorizontalAlignment(HorizontalAlignment.RIGHT);
                                                        textColumnBuildersList.add(column1);

                                                        if (fieldDataBean.getShowTotal()) {
                                                            AggregationSubtotalBuilder<Double> sbtColumn = sbt.sum(fieldDataBean.getEditedFieldLabel(), Double.class, column1).setLabel(colTitle + " " + "Total");
                                                            subTotalColumnBuilerList.add(sbtColumn);
                                                        }
                                                    } else {
                                                        if (!StringUtils.isEmpty(fieldDataBean.getAlias())) {
                                                            column1 = col.column(colTitle, fieldDataBean.getAlias().toLowerCase().replaceAll("\\s+", ""), type.doubleType()).setStyle(contentRightAlignStyle)
                                                                    .setTitleStyle(stl.style(rootStyle).setVerticalAlignment(VerticalAlignment.MIDDLE)
                                                                            .setBorder(stl.penThin()).setFontSize(8)
                                                                            .setHorizontalAlignment(HorizontalAlignment.CENTER)
                                                                            .setBackgroundColor(new Color(177, 154, 204))
                                                                            .bold());
                                                            column1.setStyle(myStyle).setHorizontalAlignment(HorizontalAlignment.RIGHT);
//                                                        System.out.println("inside double");
                                                            textColumnBuildersList.add(column1);

                                                            if (fieldDataBean.getShowTotal()) {
                                                                AggregationSubtotalBuilder<Double> sbtColumn = sbt.sum(fieldDataBean.getEditedFieldLabel(), Double.class, column1).setLabel(colTitle + " " + "Total");
                                                                subTotalColumnBuilerList.add(sbtColumn);
                                                            }
                                                        } else {
                                                            column1 = col.column(colTitle, fieldDataBean.getEditedFieldLabel(), type.doubleType()).setStyle(contentRightAlignStyle)
                                                                    .setTitleStyle(stl.style(rootStyle).setVerticalAlignment(VerticalAlignment.MIDDLE)
                                                                            .setBorder(stl.penThin()).setFontSize(8)
                                                                            .setHorizontalAlignment(HorizontalAlignment.CENTER)
                                                                            .setBackgroundColor(new Color(177, 154, 204))
                                                                            .bold());
                                                            column1.setStyle(myStyle).setHorizontalAlignment(HorizontalAlignment.RIGHT);
                                                            textColumnBuildersList.add(column1);

                                                            if (fieldDataBean.getShowTotal()) {
                                                                AggregationSubtotalBuilder<Double> sbtColumn = sbt.sum(fieldDataBean.getEditedFieldLabel(), Double.class, column1).setLabel(colTitle + " " + "Total");
                                                                subTotalColumnBuilerList.add(sbtColumn);
                                                            }
                                                        }
                                                    }

                                                } else {
                                                    if (!StringUtils.isEmpty(fieldDataBean.getAlias())) {
                                                        TextColumnBuilder<String> column1 = col.column(colTitle, fieldDataBean.getAlias().toLowerCase().replaceAll("\\s+", ""), type.stringType())
                                                                .setTitleStyle(stl.style(rootStyle).setVerticalAlignment(VerticalAlignment.MIDDLE)
                                                                        .setBorder(stl.penThin()).setFontSize(8)
                                                                        .setHorizontalAlignment(HorizontalAlignment.CENTER)
                                                                        .setBackgroundColor(new Color(177, 154, 204))
                                                                        .bold());
                                                        column1.setStyle(myStyle);
                                                        textColumnBuildersList.add(column1);
                                                    } else {
                                                        TextColumnBuilder<String> column1 = col.column(colTitle, fieldDataBean.getEditedFieldLabel(), type.stringType())
                                                                .setTitleStyle(stl.style(rootStyle).setVerticalAlignment(VerticalAlignment.MIDDLE)
                                                                        .setBorder(stl.penThin()).setFontSize(8)
                                                                        .setHorizontalAlignment(HorizontalAlignment.CENTER)
                                                                        .setBackgroundColor(new Color(177, 154, 204))
                                                                        .bold());
                                                        column1.setStyle(myStyle);
                                                        textColumnBuildersList.add(column1);
                                                    }
                                                }
                                            } else {
                                                newSeqCount = fieldDataBean.getFieldSequence();
                                                if (oldSeqCount > newSeqCount) {
                                                    oldSeqCount = newSeqCount;
                                                    if (fieldDataBean.getAlias() == null || fieldDataBean.getAlias().equals("")) {
                                                        colTitle = fieldDataBean.getFieldLabel();
                                                    } else {
                                                        colTitle = fieldDataBean.getAlias();
                                                    }
                                                    if (fieldDataBean.getDataType().equals(HkSystemConstantUtil.CustomField.DataType.INT8)) {
                                                        if (!StringUtils.isEmpty(fieldDataBean.getAlias())) {
                                                            TextColumnBuilder<Long> column1 = col.column(colTitle, fieldDataBean.getAlias().toLowerCase().replaceAll("\\s+", ""), type.longType()).setStyle(contentRightAlignStyle)
                                                                    .setTitleStyle(stl.style(rootStyle).setVerticalAlignment(VerticalAlignment.MIDDLE)
                                                                            .setBorder(stl.penThin()).setFontSize(8)
                                                                            .setHorizontalAlignment(HorizontalAlignment.CENTER)
                                                                            .setBackgroundColor(new Color(177, 154, 204))
                                                                            .bold());
                                                            column1.setStyle(myStyle).setHorizontalAlignment(HorizontalAlignment.RIGHT);
                                                            textColumnBuildersList.add(column1);

                                                            if (fieldDataBean.getShowTotal()) {
                                                                AggregationSubtotalBuilder<Long> sbtColumn = sbt.sum(fieldDataBean.getAlias().toLowerCase().replaceAll("\\s+", ""), Long.class, column1).setLabel(colTitle + " " + "Total");
                                                                subTotalColumnBuilerList.add(sbtColumn);
                                                            }
                                                        } else {
                                                            TextColumnBuilder<Long> column1 = col.column(colTitle, fieldDataBean.getEditedFieldLabel(), type.longType()).setStyle(contentRightAlignStyle)
                                                                    .setTitleStyle(stl.style(rootStyle).setVerticalAlignment(VerticalAlignment.MIDDLE)
                                                                            .setBorder(stl.penThin()).setFontSize(8)
                                                                            .setHorizontalAlignment(HorizontalAlignment.CENTER)
                                                                            .setBackgroundColor(new Color(177, 154, 204))
                                                                            .bold());
                                                            column1.setStyle(myStyle).setHorizontalAlignment(HorizontalAlignment.RIGHT);
                                                            textColumnBuildersList.add(column1);

                                                            if (fieldDataBean.getShowTotal()) {
                                                                AggregationSubtotalBuilder<Long> sbtColumn = sbt.sum(fieldDataBean.getEditedFieldLabel(), Long.class, column1).setLabel(colTitle + " " + "Total");
                                                                subTotalColumnBuilerList.add(sbtColumn);
                                                            }
                                                        }
                                                    } else if (fieldDataBean.getDataType().equals(HkSystemConstantUtil.CustomField.DataType.VARCHAR)
                                                            || fieldDataBean.getDataType().equals(HkSystemConstantUtil.CustomField.DataType.BOOLEAN)
                                                            || fieldDataBean.getComponentType().equals(HkSystemConstantUtil.CustomField.ComponentType.CURRENCY)) {
                                                        if (!StringUtils.isEmpty(fieldDataBean.getAlias())) {
                                                            TextColumnBuilder<String> column1 = col.column(colTitle, fieldDataBean.getAlias().toLowerCase().replaceAll("\\s+", ""), type.stringType())
                                                                    .setTitleStyle(stl.style(rootStyle).setVerticalAlignment(VerticalAlignment.MIDDLE)
                                                                            .setBorder(stl.penThin()).setFontSize(8)
                                                                            .setHorizontalAlignment(HorizontalAlignment.CENTER)
                                                                            .setBackgroundColor(new Color(177, 154, 204))
                                                                            .bold());
                                                            column1.setStyle(myStyle);
                                                            textColumnBuildersList.add(column1);
                                                        } else {
                                                            TextColumnBuilder<String> column1 = col.column(colTitle, fieldDataBean.getEditedFieldLabel(), type.stringType())
                                                                    .setTitleStyle(stl.style(rootStyle).setVerticalAlignment(VerticalAlignment.MIDDLE)
                                                                            .setBorder(stl.penThin()).setFontSize(8)
                                                                            .setHorizontalAlignment(HorizontalAlignment.CENTER)
                                                                            .setBackgroundColor(new Color(177, 154, 204))
                                                                            .bold());
                                                            column1.setStyle(myStyle);
                                                            textColumnBuildersList.add(column1);
                                                        }
                                                    } else if (fieldDataBean.getDataType().equals(HkSystemConstantUtil.CustomField.DataType.DOUBLE)) {
                                                        TextColumnBuilder<Double> column1;
                                                        if (fieldDataBean.getComponentType().equals(HkSystemConstantUtil.CustomField.ComponentType.PERCENT)) {
                                                            if (!StringUtils.isEmpty(fieldDataBean.getAlias())) {
                                                                column1 = col.column(colTitle, fieldDataBean.getAlias().toLowerCase().replaceAll("\\s+", ""), type.doubleType()).setValueFormatter(ReportBuilderTemplate.createValueFormatter("%")).setStyle(contentRightAlignStyle)
                                                                        .setTitleStyle(stl.style(rootStyle).setVerticalAlignment(VerticalAlignment.MIDDLE)
                                                                                .setBorder(stl.penThin()).setFontSize(8)
                                                                                .setHorizontalAlignment(HorizontalAlignment.CENTER)
                                                                                .setBackgroundColor(new Color(177, 154, 204))
                                                                                .bold());
                                                                column1.setStyle(myStyle).setHorizontalAlignment(HorizontalAlignment.RIGHT);
                                                                textColumnBuildersList.add(column1);
                                                            } else {
                                                                column1 = col.column(colTitle, fieldDataBean.getEditedFieldLabel(), type.doubleType()).setValueFormatter(ReportBuilderTemplate.createValueFormatter("%")).setStyle(contentRightAlignStyle)
                                                                        .setTitleStyle(stl.style(rootStyle).setVerticalAlignment(VerticalAlignment.MIDDLE)
                                                                                .setBorder(stl.penThin()).setFontSize(8)
                                                                                .setHorizontalAlignment(HorizontalAlignment.CENTER)
                                                                                .setBackgroundColor(new Color(177, 154, 204))
                                                                                .bold());
                                                                column1.setStyle(myStyle).setHorizontalAlignment(HorizontalAlignment.RIGHT);
                                                                textColumnBuildersList.add(column1);

                                                            }
                                                            if (fieldDataBean.getShowTotal()) {
                                                                AggregationSubtotalBuilder<Double> sbtColumn = sbt.sum(fieldDataBean.getEditedFieldLabel(), Double.class, column1).setLabel(colTitle + " " + "Total");
                                                                subTotalColumnBuilerList.add(sbtColumn);
                                                            }
                                                        } else if (fieldDataBean.getComponentType().equals(HkSystemConstantUtil.CustomField.ComponentType.ANGLE)) {
                                                            if (!StringUtils.isEmpty(fieldDataBean.getAlias())) {
                                                                column1 = col.column(colTitle, fieldDataBean.getAlias().toLowerCase().replaceAll("\\s+", ""), type.doubleType()).setValueFormatter(ReportBuilderTemplate.createValueFormatter("°")).setStyle(contentRightAlignStyle)
                                                                        .setTitleStyle(stl.style(rootStyle).setVerticalAlignment(VerticalAlignment.MIDDLE)
                                                                                .setBorder(stl.penThin()).setFontSize(8)
                                                                                .setHorizontalAlignment(HorizontalAlignment.CENTER)
                                                                                .setBackgroundColor(new Color(177, 154, 204))
                                                                                .bold());
                                                                column1.setStyle(myStyle).setHorizontalAlignment(HorizontalAlignment.RIGHT);
                                                                textColumnBuildersList.add(column1);
                                                            } else {
                                                                column1 = col.column(colTitle, fieldDataBean.getEditedFieldLabel(), type.doubleType()).setValueFormatter(ReportBuilderTemplate.createValueFormatter("°")).setStyle(contentRightAlignStyle)
                                                                        .setTitleStyle(stl.style(rootStyle).setVerticalAlignment(VerticalAlignment.MIDDLE)
                                                                                .setBorder(stl.penThin()).setFontSize(8)
                                                                                .setHorizontalAlignment(HorizontalAlignment.CENTER)
                                                                                .setBackgroundColor(new Color(177, 154, 204))
                                                                                .bold());
                                                                column1.setStyle(myStyle).setHorizontalAlignment(HorizontalAlignment.RIGHT);
                                                                textColumnBuildersList.add(column1);

                                                            }
                                                            if (fieldDataBean.getShowTotal()) {
                                                                AggregationSubtotalBuilder<Double> sbtColumn = sbt.sum(fieldDataBean.getEditedFieldLabel(), Double.class, column1).setLabel(colTitle + " " + "Total");
                                                                subTotalColumnBuilerList.add(sbtColumn);
                                                            }
                                                        } else if (fieldDataBean.getComponentType().equals(HkSystemConstantUtil.CustomField.ComponentType.CURRENCY)) {
                                                            String label = "";
                                                            String pattern = "";
//                                                            if (fieldDataBean.getAssociatedCurrency() != null && currencyIdEntityMap.get(fieldDataBean.getAssociatedCurrency().longValue()) != null) {
//                                                                label = currencyIdEntityMap.get(fieldDataBean.getAssociatedCurrency().longValue()).getSymbol();
//                                                                pattern = currencyIdEntityMap.get(fieldDataBean.getAssociatedCurrency().longValue()).getFormat();
//                                                            }
                                                            if (!StringUtils.isEmpty(fieldDataBean.getAlias())) {
                                                                column1 = col.column(colTitle, fieldDataBean.getAlias().toLowerCase().replaceAll("\\s+", ""), type.doubleType()).setValueFormatter(ReportBuilderTemplate.createCurrencyValueFormatter(label, pattern)).setStyle(contentRightAlignStyle)
                                                                        .setTitleStyle(stl.style(rootStyle).setVerticalAlignment(VerticalAlignment.MIDDLE)
                                                                                .setBorder(stl.penThin()).setFontSize(8)
                                                                                .setHorizontalAlignment(HorizontalAlignment.CENTER)
                                                                                .setBackgroundColor(new Color(177, 154, 204))
                                                                                .bold());
                                                            } else {
                                                                column1 = col.column(colTitle, fieldDataBean.getEditedFieldLabel(), type.doubleType()).setValueFormatter(ReportBuilderTemplate.createCurrencyValueFormatter(label, pattern)).setStyle(contentRightAlignStyle)
                                                                        .setTitleStyle(stl.style(rootStyle).setVerticalAlignment(VerticalAlignment.MIDDLE)
                                                                                .setBorder(stl.penThin()).setFontSize(8)
                                                                                .setHorizontalAlignment(HorizontalAlignment.CENTER)
                                                                                .setBackgroundColor(new Color(177, 154, 204))
                                                                                .bold());
                                                            }
                                                            column1.setStyle(myStyle).setHorizontalAlignment(HorizontalAlignment.RIGHT);
                                                            textColumnBuildersList.add(column1);

                                                            if (fieldDataBean.getShowTotal()) {
                                                                AggregationSubtotalBuilder<Double> sbtColumn = sbt.sum(fieldDataBean.getEditedFieldLabel(), Double.class, column1).setLabel(colTitle + " " + "Total");
                                                                subTotalColumnBuilerList.add(sbtColumn);
                                                            }
                                                        } else {
                                                            if (!StringUtils.isEmpty(fieldDataBean.getAlias())) {
                                                                column1 = col.column(colTitle, fieldDataBean.getAlias().toLowerCase().replaceAll("\\s+", ""), type.doubleType()).setStyle(contentRightAlignStyle)
                                                                        .setTitleStyle(stl.style(rootStyle).setVerticalAlignment(VerticalAlignment.MIDDLE)
                                                                                .setBorder(stl.penThin()).setFontSize(8)
                                                                                .setHorizontalAlignment(HorizontalAlignment.CENTER)
                                                                                .setBackgroundColor(new Color(177, 154, 204))
                                                                                .bold());
                                                                column1.setStyle(myStyle).setHorizontalAlignment(HorizontalAlignment.RIGHT);
                                                                textColumnBuildersList.add(column1);

                                                                if (fieldDataBean.getShowTotal()) {
                                                                    AggregationSubtotalBuilder<Double> sbtColumn = sbt.sum(fieldDataBean.getEditedFieldLabel(), Double.class, column1).setLabel(colTitle + " " + "Total");
                                                                    subTotalColumnBuilerList.add(sbtColumn);
                                                                }
                                                            } else {
                                                                column1 = col.column(colTitle, fieldDataBean.getEditedFieldLabel(), type.doubleType()).setStyle(contentRightAlignStyle)
                                                                        .setTitleStyle(stl.style(rootStyle).setVerticalAlignment(VerticalAlignment.MIDDLE)
                                                                                .setBorder(stl.penThin()).setFontSize(8)
                                                                                .setHorizontalAlignment(HorizontalAlignment.CENTER)
                                                                                .setBackgroundColor(new Color(177, 154, 204))
                                                                                .bold());
                                                                column1.setStyle(myStyle).setHorizontalAlignment(HorizontalAlignment.RIGHT);
                                                                textColumnBuildersList.add(column1);

                                                                if (fieldDataBean.getShowTotal()) {
                                                                    AggregationSubtotalBuilder<Double> sbtColumn = sbt.sum(fieldDataBean.getEditedFieldLabel(), Double.class, column1).setLabel(colTitle + " " + "Total");
                                                                    subTotalColumnBuilerList.add(sbtColumn);
                                                                }
                                                            }
//                                                        System.out.println("inside double");
                                                        }

                                                    } else if (fieldDataBean.getDataType().equals(HkSystemConstantUtil.CustomField.DataType.TIMESTAMP)) {
                                                        if (!StringUtils.isEmpty(fieldDataBean.getAlias())) {
                                                            TextColumnBuilder<String> column1 = col.column(colTitle, fieldDataBean.getAlias().toLowerCase().replaceAll("\\s+", ""), type.stringType())
                                                                    .setTitleStyle(stl.style(rootStyle).setVerticalAlignment(VerticalAlignment.MIDDLE)
                                                                            .setBorder(stl.penThin()).setFontSize(8)
                                                                            .setHorizontalAlignment(HorizontalAlignment.CENTER)
                                                                            .setBackgroundColor(new Color(177, 154, 204))
                                                                            .bold());
                                                            column1.setStyle(myStyle);
                                                            textColumnBuildersList.add(column1);
                                                        } else {
                                                            TextColumnBuilder<String> column1 = col.column(colTitle, fieldDataBean.getEditedFieldLabel(), type.stringType())
                                                                    .setTitleStyle(stl.style(rootStyle).setVerticalAlignment(VerticalAlignment.MIDDLE)
                                                                            .setBorder(stl.penThin()).setFontSize(8)
                                                                            .setHorizontalAlignment(HorizontalAlignment.CENTER)
                                                                            .setBackgroundColor(new Color(177, 154, 204))
                                                                            .bold());
                                                            column1.setStyle(myStyle);
                                                            textColumnBuildersList.add(column1);
                                                        }
                                                    } else {
                                                        if (!StringUtils.isEmpty(fieldDataBean.getAlias())) {
                                                            TextColumnBuilder<String> column1 = col.column(colTitle, fieldDataBean.getAlias().toLowerCase().replaceAll("\\s+", ""), type.stringType())
                                                                    .setTitleStyle(stl.style(rootStyle).setVerticalAlignment(VerticalAlignment.MIDDLE)
                                                                            .setBorder(stl.penThin()).setFontSize(8)
                                                                            .setHorizontalAlignment(HorizontalAlignment.CENTER)
                                                                            .setBackgroundColor(new Color(177, 154, 204))
                                                                            .bold());
                                                            column1.setStyle(myStyle);
                                                            textColumnBuildersList.add(column1);
                                                        } else {
                                                            TextColumnBuilder<String> column1 = col.column(colTitle, fieldDataBean.getEditedFieldLabel(), type.stringType())
                                                                    .setTitleStyle(stl.style(rootStyle).setVerticalAlignment(VerticalAlignment.MIDDLE)
                                                                            .setBorder(stl.penThin()).setFontSize(8)
                                                                            .setHorizontalAlignment(HorizontalAlignment.CENTER)
                                                                            .setBackgroundColor(new Color(177, 154, 204))
                                                                            .bold());
                                                            column1.setStyle(myStyle);
                                                            textColumnBuildersList.add(column1);
                                                        }
                                                    }
                                                }
                                            }
                                        }

                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                if (!CollectionUtils.isEmpty(columnList)) {
                    System.out.println("columnList :" + columnList);
                    if (!CollectionUtils.isEmpty(masterReportDataBean.getColumns())) {
                        for (String columns : columnList) {
                            int oldSeqCount = 0;
                            int newSeqCount = 0;
                            for (RbFieldDataBean fieldDataBean : masterReportDataBean.getColumns()) {
                                String colTitle = "";
                                char lastCharacter = columns.charAt(columns.length() - 1);
                                if ((columns.equals(fieldDataBean.getAlias())) || (lastCharacter == '1' && isContain(fieldDataBean.getAlias(), columns.substring(0, columns.length() - 1)))) {
                                    if (columns.equals(fieldDataBean.getAlias())) {
                                        if (!StringUtils.isEmpty(fieldDataBean.getAlias())) {
                                            dataBaseColumns.add(fieldDataBean.getAlias().toLowerCase().replaceAll("\\s+", ""));
                                        } else {
                                            dataBaseColumns.add(fieldDataBean.getEditedFieldLabel());
                                        }
                                    } else if ((lastCharacter == '1' && isContain(fieldDataBean.getAlias(), columns.substring(0, columns.length() - 1)))) {
                                        if (!StringUtils.isEmpty(fieldDataBean.getAlias())) {
                                            dataBaseColumns.add(fieldDataBean.getAlias().toLowerCase().replaceAll("\\s+", "") + "1");
                                        } else {
                                            dataBaseColumns.add(fieldDataBean.getEditedFieldLabel() + "1");
                                        }
                                    }
                                }
                                if ((columns.equals(fieldDataBean.getAlias()))) {
                                    if (CollectionUtils.isEmpty(hiddenFields) || !hiddenFields.contains(columns)) {
                                        if (oldSeqCount == 0) {
                                            oldSeqCount = fieldDataBean.getFieldSequence();
                                            if (fieldDataBean.getAlias() == null || fieldDataBean.getAlias().equals("")) {
                                                colTitle = fieldDataBean.getFieldLabel();
                                            } else {
                                                colTitle = fieldDataBean.getAlias();
                                            }
                                            if (fieldDataBean.getDataType().equals(HkSystemConstantUtil.CustomField.DataType.INT8)) {
                                                if (!StringUtils.isEmpty(fieldDataBean.getAlias())) {
                                                    TextColumnBuilder<Long> column1 = col.column(colTitle, fieldDataBean.getAlias().toLowerCase().replaceAll("\\s+", ""), type.longType()).setStyle(contentRightAlignStyle)
                                                            .setTitleStyle(stl.style(rootStyle).setVerticalAlignment(VerticalAlignment.MIDDLE)
                                                                    .setBorder(stl.penThin()).setFontSize(8)
                                                                    .setHorizontalAlignment(HorizontalAlignment.CENTER)
                                                                    .setBackgroundColor(new Color(177, 154, 204))
                                                                    .bold());
                                                    column1.setStyle(myStyle).setHorizontalAlignment(HorizontalAlignment.RIGHT);
                                                    textColumnBuildersList.add(column1);

                                                    if (fieldDataBean.getShowTotal()) {
                                                        AggregationSubtotalBuilder<Long> sbtColumn = sbt.sum(fieldDataBean.getAlias().toLowerCase().replaceAll("\\s+", ""), Long.class, column1).setLabel(colTitle + " " + "Total");
                                                        subTotalColumnBuilerList.add(sbtColumn);
                                                    }
                                                } else {
                                                    TextColumnBuilder<Long> column1 = col.column(colTitle, fieldDataBean.getEditedFieldLabel(), type.longType()).setStyle(contentRightAlignStyle)
                                                            .setTitleStyle(stl.style(rootStyle).setVerticalAlignment(VerticalAlignment.MIDDLE)
                                                                    .setBorder(stl.penThin()).setFontSize(8)
                                                                    .setHorizontalAlignment(HorizontalAlignment.CENTER)
                                                                    .setBackgroundColor(new Color(177, 154, 204))
                                                                    .bold());
                                                    column1.setStyle(myStyle).setHorizontalAlignment(HorizontalAlignment.RIGHT);
                                                    textColumnBuildersList.add(column1);

                                                    if (fieldDataBean.getShowTotal()) {
                                                        AggregationSubtotalBuilder<Long> sbtColumn = sbt.sum(fieldDataBean.getEditedFieldLabel(), Long.class, column1).setLabel(colTitle + " " + "Total");
                                                        subTotalColumnBuilerList.add(sbtColumn);
                                                    }
                                                }
                                            } else if (fieldDataBean.getDataType().equals(HkSystemConstantUtil.CustomField.DataType.VARCHAR)
                                                    || fieldDataBean.getDataType().equals(HkSystemConstantUtil.CustomField.DataType.BOOLEAN)
                                                    || fieldDataBean.getComponentType().equals(HkSystemConstantUtil.CustomField.ComponentType.CURRENCY)) {
                                                TextColumnBuilder<String> column1;
                                                if (!StringUtils.isEmpty(fieldDataBean.getAlias())) {
                                                    column1 = col.column(colTitle, fieldDataBean.getAlias().toLowerCase().replaceAll("\\s+", ""), type.stringType())
                                                            .setTitleStyle(stl.style(rootStyle).setVerticalAlignment(VerticalAlignment.MIDDLE)
                                                                    .setBorder(stl.penThin()).setFontSize(8)
                                                                    .setHorizontalAlignment(HorizontalAlignment.CENTER)
                                                                    .setBackgroundColor(new Color(177, 154, 204))
                                                                    .bold());
                                                    column1.setStyle(myStyle);
                                                    textColumnBuildersList.add(column1);
                                                } else {
                                                    column1 = col.column(colTitle, fieldDataBean.getEditedFieldLabel(), type.stringType())
                                                            .setTitleStyle(stl.style(rootStyle).setVerticalAlignment(VerticalAlignment.MIDDLE)
                                                                    .setBorder(stl.penThin()).setFontSize(8)
                                                                    .setHorizontalAlignment(HorizontalAlignment.CENTER)
                                                                    .setBackgroundColor(new Color(177, 154, 204))
                                                                    .bold());
                                                    column1.setStyle(myStyle);
                                                    textColumnBuildersList.add(column1);
                                                }
//                                        ColumnGroupBuilder grpBuilder = grp.group(column1);
//                                        columnGrpBuilderList.add(grpBuilder);

                                            } else if (fieldDataBean.getDataType().equals(HkSystemConstantUtil.CustomField.DataType.TIMESTAMP)) {
                                                if (!StringUtils.isEmpty(fieldDataBean.getAlias())) {
                                                    TextColumnBuilder<String> column1 = col.column(colTitle, fieldDataBean.getAlias().toLowerCase().replaceAll("\\s+", ""), type.stringType())
                                                            .setTitleStyle(stl.style(rootStyle).setVerticalAlignment(VerticalAlignment.MIDDLE)
                                                                    .setBorder(stl.penThin()).setFontSize(8)
                                                                    .setHorizontalAlignment(HorizontalAlignment.CENTER)
                                                                    .setBackgroundColor(new Color(177, 154, 204))
                                                                    .bold());
                                                    column1.setStyle(myStyle);
                                                    textColumnBuildersList.add(column1);
                                                } else {
                                                    TextColumnBuilder<String> column1 = col.column(colTitle, fieldDataBean.getEditedFieldLabel(), type.stringType())
                                                            .setTitleStyle(stl.style(rootStyle).setVerticalAlignment(VerticalAlignment.MIDDLE)
                                                                    .setBorder(stl.penThin()).setFontSize(8)
                                                                    .setHorizontalAlignment(HorizontalAlignment.CENTER)
                                                                    .setBackgroundColor(new Color(177, 154, 204))
                                                                    .bold());
                                                    column1.setStyle(myStyle);
                                                    textColumnBuildersList.add(column1);
                                                }
                                            } else if (fieldDataBean.getDataType().equals(HkSystemConstantUtil.CustomField.DataType.DOUBLE)) {
                                                TextColumnBuilder<Double> column1;
                                                if (fieldDataBean.getComponentType().equals(HkSystemConstantUtil.CustomField.ComponentType.PERCENT)) {
                                                    if (!StringUtils.isEmpty(fieldDataBean.getAlias())) {
                                                        column1 = col.column(colTitle, fieldDataBean.getAlias().toLowerCase().replaceAll("\\s+", ""), type.doubleType()).setValueFormatter(ReportBuilderTemplate.createValueFormatter("%")).setStyle(contentRightAlignStyle)
                                                                .setTitleStyle(stl.style(rootStyle).setVerticalAlignment(VerticalAlignment.MIDDLE)
                                                                        .setBorder(stl.penThin()).setFontSize(8)
                                                                        .setHorizontalAlignment(HorizontalAlignment.CENTER)
                                                                        .setBackgroundColor(new Color(177, 154, 204))
                                                                        .bold());
                                                        column1.setStyle(myStyle).setHorizontalAlignment(HorizontalAlignment.RIGHT);
                                                        textColumnBuildersList.add(column1);
                                                    } else {
                                                        column1 = col.column(colTitle, fieldDataBean.getEditedFieldLabel(), type.doubleType()).setValueFormatter(ReportBuilderTemplate.createValueFormatter("%")).setStyle(contentRightAlignStyle)
                                                                .setTitleStyle(stl.style(rootStyle).setVerticalAlignment(VerticalAlignment.MIDDLE)
                                                                        .setBorder(stl.penThin()).setFontSize(8)
                                                                        .setHorizontalAlignment(HorizontalAlignment.CENTER)
                                                                        .setBackgroundColor(new Color(177, 154, 204))
                                                                        .bold());
                                                        column1.setStyle(myStyle).setHorizontalAlignment(HorizontalAlignment.RIGHT);
                                                        textColumnBuildersList.add(column1);
                                                    }
                                                    if (fieldDataBean.getShowTotal()) {
                                                        AggregationSubtotalBuilder<Double> sbtColumn = sbt.sum(fieldDataBean.getEditedFieldLabel(), Double.class, column1).setLabel(colTitle + " " + "Total");
                                                        subTotalColumnBuilerList.add(sbtColumn);
                                                    }
                                                } else if (fieldDataBean.getComponentType().equals(HkSystemConstantUtil.CustomField.ComponentType.ANGLE)) {
                                                    if (!StringUtils.isEmpty(fieldDataBean.getAlias())) {
                                                        column1 = col.column(colTitle, fieldDataBean.getAlias().toLowerCase().replaceAll("\\s+", ""), type.doubleType()).setValueFormatter(ReportBuilderTemplate.createValueFormatter("°")).setStyle(contentRightAlignStyle)
                                                                .setTitleStyle(stl.style(rootStyle).setVerticalAlignment(VerticalAlignment.MIDDLE)
                                                                        .setBorder(stl.penThin()).setFontSize(8)
                                                                        .setHorizontalAlignment(HorizontalAlignment.CENTER)
                                                                        .setBackgroundColor(new Color(177, 154, 204))
                                                                        .bold());
                                                        column1.setStyle(myStyle).setHorizontalAlignment(HorizontalAlignment.RIGHT);
                                                        textColumnBuildersList.add(column1);
                                                    } else {
                                                        column1 = col.column(colTitle, fieldDataBean.getEditedFieldLabel(), type.doubleType()).setValueFormatter(ReportBuilderTemplate.createValueFormatter("°")).setStyle(contentRightAlignStyle)
                                                                .setTitleStyle(stl.style(rootStyle).setVerticalAlignment(VerticalAlignment.MIDDLE)
                                                                        .setBorder(stl.penThin()).setFontSize(8)
                                                                        .setHorizontalAlignment(HorizontalAlignment.CENTER)
                                                                        .setBackgroundColor(new Color(177, 154, 204))
                                                                        .bold());
                                                        column1.setStyle(myStyle).setHorizontalAlignment(HorizontalAlignment.RIGHT);
                                                        textColumnBuildersList.add(column1);
                                                    }
                                                    if (fieldDataBean.getShowTotal()) {
                                                        AggregationSubtotalBuilder<Double> sbtColumn = sbt.sum(fieldDataBean.getEditedFieldLabel(), Double.class, column1).setLabel(colTitle + " " + "Total");
                                                        subTotalColumnBuilerList.add(sbtColumn);
                                                    }
                                                } else if (fieldDataBean.getComponentType().equals(HkSystemConstantUtil.CustomField.ComponentType.CURRENCY)) {
                                                    String label = "";
                                                    String pattern = "";
//                                                    if (fieldDataBean.getAssociatedCurrency() != null && currencyIdEntityMap.get(fieldDataBean.getAssociatedCurrency().longValue()) != null) {
//                                                        label = currencyIdEntityMap.get(fieldDataBean.getAssociatedCurrency().longValue()).getSymbol();
//                                                        pattern = currencyIdEntityMap.get(fieldDataBean.getAssociatedCurrency().longValue()).getFormat();
//                                                    }
                                                    if (!StringUtils.isEmpty(fieldDataBean.getAlias())) {
                                                        column1 = col.column(colTitle, fieldDataBean.getAlias().toLowerCase().replaceAll("\\s+", ""), type.doubleType()).setValueFormatter(ReportBuilderTemplate.createCurrencyValueFormatter(label, pattern)).setStyle(contentRightAlignStyle)
                                                                .setTitleStyle(stl.style(rootStyle).setVerticalAlignment(VerticalAlignment.MIDDLE)
                                                                        .setBorder(stl.penThin()).setFontSize(8)
                                                                        .setHorizontalAlignment(HorizontalAlignment.CENTER)
                                                                        .setBackgroundColor(new Color(177, 154, 204))
                                                                        .bold());
                                                    } else {
                                                        column1 = col.column(colTitle, fieldDataBean.getEditedFieldLabel(), type.doubleType()).setValueFormatter(ReportBuilderTemplate.createCurrencyValueFormatter(label, pattern)).setStyle(contentRightAlignStyle)
                                                                .setTitleStyle(stl.style(rootStyle).setVerticalAlignment(VerticalAlignment.MIDDLE)
                                                                        .setBorder(stl.penThin()).setFontSize(8)
                                                                        .setHorizontalAlignment(HorizontalAlignment.CENTER)
                                                                        .setBackgroundColor(new Color(177, 154, 204))
                                                                        .bold());
                                                    }
                                                    column1.setStyle(myStyle).setHorizontalAlignment(HorizontalAlignment.RIGHT);
                                                    textColumnBuildersList.add(column1);
                                                    if (fieldDataBean.getShowTotal()) {
                                                        AggregationSubtotalBuilder<Double> sbtColumn = sbt.sum(fieldDataBean.getEditedFieldLabel(), Double.class, column1).setLabel(colTitle + " " + "Total");
                                                        subTotalColumnBuilerList.add(sbtColumn);
                                                    }
                                                } else {
                                                    if (!StringUtils.isEmpty(fieldDataBean.getAlias())) {
                                                        column1 = col.column(colTitle, fieldDataBean.getAlias().toLowerCase().replaceAll("\\s+", ""), type.doubleType()).setStyle(contentRightAlignStyle)
                                                                .setTitleStyle(stl.style(rootStyle).setVerticalAlignment(VerticalAlignment.MIDDLE)
                                                                        .setBorder(stl.penThin()).setFontSize(8)
                                                                        .setHorizontalAlignment(HorizontalAlignment.CENTER)
                                                                        .setBackgroundColor(new Color(177, 154, 204))
                                                                        .bold());
                                                        column1.setStyle(myStyle).setHorizontalAlignment(HorizontalAlignment.RIGHT);
                                                        textColumnBuildersList.add(column1);
                                                    } else {
                                                        column1 = col.column(colTitle, fieldDataBean.getEditedFieldLabel(), type.doubleType()).setStyle(contentRightAlignStyle)
                                                                .setTitleStyle(stl.style(rootStyle).setVerticalAlignment(VerticalAlignment.MIDDLE)
                                                                        .setBorder(stl.penThin()).setFontSize(8)
                                                                        .setHorizontalAlignment(HorizontalAlignment.CENTER)
                                                                        .setBackgroundColor(new Color(177, 154, 204))
                                                                        .bold());
                                                        column1.setStyle(myStyle).setHorizontalAlignment(HorizontalAlignment.RIGHT);
                                                        textColumnBuildersList.add(column1);
                                                    }
                                                    if (fieldDataBean.getShowTotal()) {
                                                        AggregationSubtotalBuilder<Double> sbtColumn = sbt.sum(fieldDataBean.getEditedFieldLabel(), Double.class, column1).setLabel(colTitle + " " + "Total");
                                                        subTotalColumnBuilerList.add(sbtColumn);
                                                    }
//                                                System.out.println("inside double");
                                                }

                                            } else {
                                                if (!StringUtils.isEmpty(fieldDataBean.getAlias())) {
                                                    TextColumnBuilder<String> column1 = col.column(colTitle, fieldDataBean.getAlias().toLowerCase().replaceAll("\\s+", ""), type.stringType())
                                                            .setTitleStyle(stl.style(rootStyle).setVerticalAlignment(VerticalAlignment.MIDDLE)
                                                                    .setBorder(stl.penThin()).setFontSize(8)
                                                                    .setHorizontalAlignment(HorizontalAlignment.CENTER)
                                                                    .setBackgroundColor(new Color(177, 154, 204))
                                                                    .bold());
                                                    column1.setStyle(myStyle);
                                                    textColumnBuildersList.add(column1);
                                                } else {
                                                    TextColumnBuilder<String> column1 = col.column(colTitle, fieldDataBean.getEditedFieldLabel(), type.stringType())
                                                            .setTitleStyle(stl.style(rootStyle).setVerticalAlignment(VerticalAlignment.MIDDLE)
                                                                    .setBorder(stl.penThin()).setFontSize(8)
                                                                    .setHorizontalAlignment(HorizontalAlignment.CENTER)
                                                                    .setBackgroundColor(new Color(177, 154, 204))
                                                                    .bold());
                                                    column1.setStyle(myStyle);
                                                    textColumnBuildersList.add(column1);
                                                }
                                            }
                                        } else {
                                            newSeqCount = fieldDataBean.getFieldSequence();
                                            if (oldSeqCount > newSeqCount) {
                                                oldSeqCount = newSeqCount;
                                                if (fieldDataBean.getAlias() == null || fieldDataBean.getAlias().equals("")) {
                                                    colTitle = fieldDataBean.getFieldLabel();
                                                } else {
                                                    colTitle = fieldDataBean.getAlias();
                                                }
                                                if (fieldDataBean.getDataType().equals(HkSystemConstantUtil.CustomField.DataType.INT8)) {
                                                    if (!StringUtils.isEmpty(fieldDataBean.getAlias())) {
                                                        TextColumnBuilder<Long> column1 = col.column(colTitle, fieldDataBean.getAlias().toLowerCase().replaceAll("\\s+", ""), type.longType()).setStyle(contentRightAlignStyle)
                                                                .setTitleStyle(stl.style(rootStyle).setVerticalAlignment(VerticalAlignment.MIDDLE)
                                                                        .setBorder(stl.penThin()).setFontSize(8)
                                                                        .setHorizontalAlignment(HorizontalAlignment.CENTER)
                                                                        .setBackgroundColor(new Color(177, 154, 204))
                                                                        .bold());
                                                        column1.setStyle(myStyle).setHorizontalAlignment(HorizontalAlignment.RIGHT);
                                                        textColumnBuildersList.add(column1);

                                                        if (fieldDataBean.getShowTotal()) {
                                                            AggregationSubtotalBuilder<Long> sbtColumn = sbt.sum(fieldDataBean.getAlias().toLowerCase().replaceAll("\\s+", ""), Long.class, column1).setLabel(colTitle + " " + "Total");
                                                            subTotalColumnBuilerList.add(sbtColumn);
                                                        }
                                                    } else {
                                                        TextColumnBuilder<Long> column1 = col.column(colTitle, fieldDataBean.getEditedFieldLabel(), type.longType()).setStyle(contentRightAlignStyle)
                                                                .setTitleStyle(stl.style(rootStyle).setVerticalAlignment(VerticalAlignment.MIDDLE)
                                                                        .setBorder(stl.penThin()).setFontSize(8)
                                                                        .setHorizontalAlignment(HorizontalAlignment.CENTER)
                                                                        .setBackgroundColor(new Color(177, 154, 204))
                                                                        .bold());
                                                        column1.setStyle(myStyle).setHorizontalAlignment(HorizontalAlignment.RIGHT);
                                                        textColumnBuildersList.add(column1);

                                                        if (fieldDataBean.getShowTotal()) {
                                                            AggregationSubtotalBuilder<Long> sbtColumn = sbt.sum(fieldDataBean.getEditedFieldLabel(), Long.class, column1).setLabel(colTitle + " " + "Total");
                                                            subTotalColumnBuilerList.add(sbtColumn);
                                                        }
                                                    }
                                                } else if (fieldDataBean.getDataType().equals(HkSystemConstantUtil.CustomField.DataType.VARCHAR)
                                                        || fieldDataBean.getDataType().equals(HkSystemConstantUtil.CustomField.DataType.BOOLEAN)
                                                        || fieldDataBean.getComponentType().equals(HkSystemConstantUtil.CustomField.ComponentType.CURRENCY)) {
                                                    if (!StringUtils.isEmpty(fieldDataBean.getAlias())) {
                                                        TextColumnBuilder<String> column1 = col.column(colTitle, fieldDataBean.getAlias().toLowerCase().replaceAll("\\s+", ""), type.stringType())
                                                                .setTitleStyle(stl.style(rootStyle).setVerticalAlignment(VerticalAlignment.MIDDLE)
                                                                        .setBorder(stl.penThin()).setFontSize(8)
                                                                        .setHorizontalAlignment(HorizontalAlignment.CENTER)
                                                                        .setBackgroundColor(new Color(177, 154, 204))
                                                                        .bold());
                                                        column1.setStyle(myStyle);
                                                        textColumnBuildersList.add(column1);
                                                    } else {
                                                        TextColumnBuilder<String> column1 = col.column(colTitle, fieldDataBean.getEditedFieldLabel(), type.stringType())
                                                                .setTitleStyle(stl.style(rootStyle).setVerticalAlignment(VerticalAlignment.MIDDLE)
                                                                        .setBorder(stl.penThin()).setFontSize(8)
                                                                        .setHorizontalAlignment(HorizontalAlignment.CENTER)
                                                                        .setBackgroundColor(new Color(177, 154, 204))
                                                                        .bold());
                                                        column1.setStyle(myStyle);
                                                        textColumnBuildersList.add(column1);
                                                    }
                                                } else if (fieldDataBean.getDataType().equals(HkSystemConstantUtil.CustomField.DataType.DOUBLE)) {
                                                    TextColumnBuilder<Double> column1;
                                                    if (fieldDataBean.getComponentType().equals(HkSystemConstantUtil.CustomField.ComponentType.PERCENT)) {
                                                        if (!StringUtils.isEmpty(fieldDataBean.getAlias())) {
                                                            column1 = col.column(colTitle, fieldDataBean.getAlias().toLowerCase().replaceAll("\\s+", ""), type.doubleType()).setValueFormatter(ReportBuilderTemplate.createValueFormatter("%")).setStyle(contentRightAlignStyle)
                                                                    .setTitleStyle(stl.style(rootStyle).setVerticalAlignment(VerticalAlignment.MIDDLE)
                                                                            .setBorder(stl.penThin()).setFontSize(8)
                                                                            .setHorizontalAlignment(HorizontalAlignment.CENTER)
                                                                            .setBackgroundColor(new Color(177, 154, 204))
                                                                            .bold());
                                                            column1.setStyle(myStyle).setHorizontalAlignment(HorizontalAlignment.RIGHT);
                                                            textColumnBuildersList.add(column1);
                                                        } else {
                                                            column1 = col.column(colTitle, fieldDataBean.getEditedFieldLabel(), type.doubleType()).setValueFormatter(ReportBuilderTemplate.createValueFormatter("%")).setStyle(contentRightAlignStyle)
                                                                    .setTitleStyle(stl.style(rootStyle).setVerticalAlignment(VerticalAlignment.MIDDLE)
                                                                            .setBorder(stl.penThin()).setFontSize(8)
                                                                            .setHorizontalAlignment(HorizontalAlignment.CENTER)
                                                                            .setBackgroundColor(new Color(177, 154, 204))
                                                                            .bold());
                                                            column1.setStyle(myStyle).setHorizontalAlignment(HorizontalAlignment.RIGHT);
                                                            textColumnBuildersList.add(column1);
                                                        }
                                                        if (fieldDataBean.getShowTotal()) {
                                                            AggregationSubtotalBuilder<Double> sbtColumn = sbt.sum(fieldDataBean.getEditedFieldLabel(), Double.class, column1).setLabel(colTitle + " " + "Total");
                                                            subTotalColumnBuilerList.add(sbtColumn);
                                                        }
                                                    } else if (fieldDataBean.getComponentType().equals(HkSystemConstantUtil.CustomField.ComponentType.ANGLE)) {
                                                        if (!StringUtils.isEmpty(fieldDataBean.getAlias())) {
                                                            column1 = col.column(colTitle, fieldDataBean.getAlias().toLowerCase().replaceAll("\\s+", ""), type.doubleType()).setValueFormatter(ReportBuilderTemplate.createValueFormatter("°")).setStyle(contentRightAlignStyle)
                                                                    .setTitleStyle(stl.style(rootStyle).setVerticalAlignment(VerticalAlignment.MIDDLE)
                                                                            .setBorder(stl.penThin()).setFontSize(8)
                                                                            .setHorizontalAlignment(HorizontalAlignment.CENTER)
                                                                            .setBackgroundColor(new Color(177, 154, 204))
                                                                            .bold());
                                                            column1.setStyle(myStyle).setHorizontalAlignment(HorizontalAlignment.RIGHT);
                                                            textColumnBuildersList.add(column1);
                                                        } else {
                                                            column1 = col.column(colTitle, fieldDataBean.getEditedFieldLabel(), type.doubleType()).setValueFormatter(ReportBuilderTemplate.createValueFormatter("°")).setStyle(contentRightAlignStyle)
                                                                    .setTitleStyle(stl.style(rootStyle).setVerticalAlignment(VerticalAlignment.MIDDLE)
                                                                            .setBorder(stl.penThin()).setFontSize(8)
                                                                            .setHorizontalAlignment(HorizontalAlignment.CENTER)
                                                                            .setBackgroundColor(new Color(177, 154, 204))
                                                                            .bold());
                                                            column1.setStyle(myStyle).setHorizontalAlignment(HorizontalAlignment.RIGHT);
                                                            textColumnBuildersList.add(column1);
                                                        }
                                                        if (fieldDataBean.getShowTotal()) {
                                                            AggregationSubtotalBuilder<Double> sbtColumn = sbt.sum(fieldDataBean.getEditedFieldLabel(), Double.class, column1).setLabel(colTitle + " " + "Total");
                                                            subTotalColumnBuilerList.add(sbtColumn);
                                                        }
                                                    } else if (fieldDataBean.getComponentType().equals(HkSystemConstantUtil.CustomField.ComponentType.CURRENCY)) {
                                                        String label = "";
                                                        String pattern = "";
//                                                        if (fieldDataBean.getAssociatedCurrency() != null && currencyIdEntityMap.get(fieldDataBean.getAssociatedCurrency().longValue()) != null) {
//                                                            label = currencyIdEntityMap.get(fieldDataBean.getAssociatedCurrency().longValue()).getSymbol();
//                                                            pattern = currencyIdEntityMap.get(fieldDataBean.getAssociatedCurrency().longValue()).getFormat();
//                                                        }
                                                        if (!StringUtils.isEmpty(fieldDataBean.getAlias())) {
                                                            column1 = col.column(colTitle, fieldDataBean.getAlias().toLowerCase().replaceAll("\\s+", ""), type.doubleType()).setValueFormatter(ReportBuilderTemplate.createCurrencyValueFormatter(label, pattern)).setStyle(contentRightAlignStyle)
                                                                    .setTitleStyle(stl.style(rootStyle).setVerticalAlignment(VerticalAlignment.MIDDLE)
                                                                            .setBorder(stl.penThin()).setFontSize(8)
                                                                            .setHorizontalAlignment(HorizontalAlignment.CENTER)
                                                                            .setBackgroundColor(new Color(177, 154, 204))
                                                                            .bold());
                                                        } else {
                                                            column1 = col.column(colTitle, fieldDataBean.getEditedFieldLabel(), type.doubleType()).setValueFormatter(ReportBuilderTemplate.createCurrencyValueFormatter(label, pattern)).setStyle(contentRightAlignStyle)
                                                                    .setTitleStyle(stl.style(rootStyle).setVerticalAlignment(VerticalAlignment.MIDDLE)
                                                                            .setBorder(stl.penThin()).setFontSize(8)
                                                                            .setHorizontalAlignment(HorizontalAlignment.CENTER)
                                                                            .setBackgroundColor(new Color(177, 154, 204))
                                                                            .bold());
                                                        }
                                                        column1.setStyle(myStyle).setHorizontalAlignment(HorizontalAlignment.RIGHT);
                                                        textColumnBuildersList.add(column1);
                                                        if (fieldDataBean.getShowTotal()) {
                                                            AggregationSubtotalBuilder<Double> sbtColumn = sbt.sum(fieldDataBean.getEditedFieldLabel(), Double.class, column1).setLabel(colTitle + " " + "Total");
                                                            subTotalColumnBuilerList.add(sbtColumn);
                                                        }
                                                    } else {
                                                        if (!StringUtils.isEmpty(fieldDataBean.getAlias())) {
                                                            column1 = col.column(colTitle, fieldDataBean.getAlias().toLowerCase().replaceAll("\\s+", ""), type.doubleType()).setStyle(contentRightAlignStyle)
                                                                    .setTitleStyle(stl.style(rootStyle).setVerticalAlignment(VerticalAlignment.MIDDLE)
                                                                            .setBorder(stl.penThin()).setFontSize(8)
                                                                            .setHorizontalAlignment(HorizontalAlignment.CENTER)
                                                                            .setBackgroundColor(new Color(177, 154, 204))
                                                                            .bold());
                                                            column1.setStyle(myStyle).setHorizontalAlignment(HorizontalAlignment.RIGHT);
                                                            textColumnBuildersList.add(column1);
                                                        } else {
                                                            column1 = col.column(colTitle, fieldDataBean.getEditedFieldLabel(), type.doubleType()).setStyle(contentRightAlignStyle)
                                                                    .setTitleStyle(stl.style(rootStyle).setVerticalAlignment(VerticalAlignment.MIDDLE)
                                                                            .setBorder(stl.penThin()).setFontSize(8)
                                                                            .setHorizontalAlignment(HorizontalAlignment.CENTER)
                                                                            .setBackgroundColor(new Color(177, 154, 204))
                                                                            .bold());
                                                            column1.setStyle(myStyle).setHorizontalAlignment(HorizontalAlignment.RIGHT);
                                                            textColumnBuildersList.add(column1);
                                                        }
                                                        if (fieldDataBean.getShowTotal()) {
                                                            AggregationSubtotalBuilder<Double> sbtColumn = sbt.sum(fieldDataBean.getEditedFieldLabel(), Double.class, column1).setLabel(colTitle + " " + "Total");
                                                            subTotalColumnBuilerList.add(sbtColumn);
                                                        }
//                                                    System.out.println("inside double");
                                                    }

                                                } else if (fieldDataBean.getDataType().equals(HkSystemConstantUtil.CustomField.DataType.TIMESTAMP)) {
                                                    if (!StringUtils.isEmpty(fieldDataBean.getAlias())) {
                                                        TextColumnBuilder<String> column1 = col.column(colTitle, fieldDataBean.getAlias().toLowerCase().replaceAll("\\s+", ""), type.stringType())
                                                                .setTitleStyle(stl.style(rootStyle).setVerticalAlignment(VerticalAlignment.MIDDLE)
                                                                        .setBorder(stl.penThin()).setFontSize(8)
                                                                        .setHorizontalAlignment(HorizontalAlignment.CENTER)
                                                                        .setBackgroundColor(new Color(177, 154, 204))
                                                                        .bold());
                                                        column1.setStyle(myStyle);
                                                        textColumnBuildersList.add(column1);
                                                    } else {
                                                        TextColumnBuilder<String> column1 = col.column(colTitle, fieldDataBean.getEditedFieldLabel(), type.stringType())
                                                                .setTitleStyle(stl.style(rootStyle).setVerticalAlignment(VerticalAlignment.MIDDLE)
                                                                        .setBorder(stl.penThin()).setFontSize(8)
                                                                        .setHorizontalAlignment(HorizontalAlignment.CENTER)
                                                                        .setBackgroundColor(new Color(177, 154, 204))
                                                                        .bold());
                                                        column1.setStyle(myStyle);
                                                        textColumnBuildersList.add(column1);
                                                    }
                                                } else {
                                                    if (!StringUtils.isEmpty(fieldDataBean.getAlias())) {
                                                        TextColumnBuilder<String> column1 = col.column(colTitle, fieldDataBean.getAlias().toLowerCase().replaceAll("\\s+", ""), type.stringType())
                                                                .setTitleStyle(stl.style(rootStyle).setVerticalAlignment(VerticalAlignment.MIDDLE)
                                                                        .setBorder(stl.penThin()).setFontSize(8)
                                                                        .setHorizontalAlignment(HorizontalAlignment.CENTER)
                                                                        .setBackgroundColor(new Color(177, 154, 204))
                                                                        .bold());
                                                        column1.setStyle(myStyle);
                                                        textColumnBuildersList.add(column1);
                                                    } else {
                                                        TextColumnBuilder<String> column1 = col.column(colTitle, fieldDataBean.getEditedFieldLabel(), type.stringType())
                                                                .setTitleStyle(stl.style(rootStyle).setVerticalAlignment(VerticalAlignment.MIDDLE)
                                                                        .setBorder(stl.penThin()).setFontSize(8)
                                                                        .setHorizontalAlignment(HorizontalAlignment.CENTER)
                                                                        .setBackgroundColor(new Color(177, 154, 204))
                                                                        .bold());
                                                        column1.setStyle(myStyle);
                                                        textColumnBuildersList.add(column1);
                                                    }
                                                }
                                            }
                                        }
                                    }

                                }
                            }
                        }
                    }
                }
            }
            if (!CollectionUtils.isEmpty(colorAttributeForReport) && !CollectionUtils.isEmpty(groupByFields)) {
                for (String groupByField : groupByFields) {
                    dataBaseColumns.add(groupByField.toLowerCase().replaceAll("\\s+", "") + "1");
                    List<RbFieldDataBean> columns = masterReportDataBean.getColumns();
                    if (!CollectionUtils.isEmpty(columns)) {
                        for (RbFieldDataBean fieldDataBean : columns) {
                            if (fieldDataBean.getAlias().equals(groupByField)) {
                                String colTitle = groupByField + "1";
                                String colName = groupByField.toLowerCase().replaceAll("\\s+", "") + "1";
                                if (fieldDataBean.getDataType().equals(HkSystemConstantUtil.CustomField.DataType.VARCHAR)
                                        || fieldDataBean.getDataType().equals(HkSystemConstantUtil.CustomField.DataType.BOOLEAN)
                                        || fieldDataBean.getComponentType().equals(HkSystemConstantUtil.CustomField.ComponentType.CURRENCY)) {
                                    TextColumnBuilder<String> column1 = col.column(colTitle, colName, type.stringType());
                                    column1.setStyle(myStyle);
                                    ColumnGroupBuilder grpBuilder = grp.group(column1).setHideColumn(true).setHeaderLayout(GroupHeaderLayout.EMPTY);
                                    columnGrpBuilderList.add(grpBuilder);
                                } else if (fieldDataBean.getDataType().equals(HkSystemConstantUtil.CustomField.DataType.INT8)) {
                                    TextColumnBuilder<Long> column1 = col.column(colTitle, colName, type.longType()).setStyle(contentRightAlignStyle);
                                    column1.setStyle(myStyle).setHorizontalAlignment(HorizontalAlignment.RIGHT);
                                    ColumnGroupBuilder grpBuilder = grp.group(column1).setHideColumn(true).setHeaderLayout(GroupHeaderLayout.EMPTY);
                                    columnGrpBuilderList.add(grpBuilder);
                                } else if (fieldDataBean.getDataType().equals(HkSystemConstantUtil.CustomField.DataType.DOUBLE)) {
                                    TextColumnBuilder<Double> column1 = col.column(colTitle, colName, type.doubleType()).setStyle(contentRightAlignStyle);
                                    column1.setStyle(myStyle).setHorizontalAlignment(HorizontalAlignment.RIGHT);
                                    ColumnGroupBuilder grpBuilder = grp.group(column1).setHideColumn(true).setHeaderLayout(GroupHeaderLayout.EMPTY);
                                    columnGrpBuilderList.add(grpBuilder);
                                } else if (fieldDataBean.getDataType().equals(HkSystemConstantUtil.CustomField.DataType.TIMESTAMP)) {
                                    TextColumnBuilder<Date> column1 = col.column(colTitle, colName, type.dateType()).setStyle(contentRightAlignStyle);
                                    column1.setStyle(myStyle).setHorizontalAlignment(HorizontalAlignment.RIGHT);
                                    ColumnGroupBuilder grpBuilder = grp.group(column1).setHideColumn(true).setHeaderLayout(GroupHeaderLayout.EMPTY);
                                    columnGrpBuilderList.add(grpBuilder);
                                }

                            }
                        }
                    }
                }
            }
            createReport(response, request, fileName, filterAttributes, title, extension, createDataSource(records, dataBaseColumns), finalResultList, hiddenFields,
                    subTotalColumnBuilerList, gridBuilder, columnGrpBuilderList, textColumnBuildersList.toArray(new TextColumnBuilder[textColumnBuildersList.size()]));
            if (toDownload) {
                byte[] fileBytes = downloadPdf(fileName, extension, request, response);
                return fileBytes;
            }

        }
        return null;
    }
    //------By Shreya on 05 January 2015 for creating data source for the report..

    private JRDataSource createDataSource(List<List<Object>> records, List<String> columnNamesList) {
        String columnsNames = columnNamesList.toString().replaceAll("\\[|\\]", "").replaceAll(" ", "");
        DRDataSource dataSource = new DRDataSource(columnsNames.split(","));
        List<Object> rowList;
//        System.out.println("records :" + records);
        for (List<Object> row : records) {
//            System.out.println("row :" + row);
            rowList = new LinkedList<>();
            for (Object fieldValue : row) {
                if (fieldValue != null) {
                    if (fieldValue.equals("t")) {
                        rowList.add("true");
                    } else if (fieldValue.equals("f")) {
                        rowList.add("false");
                    } else {
                        rowList.add(fieldValue);
                    }
                } else {
                    rowList.add("N/A");
                }
            }
            dataSource.add(rowList.toArray());
        }
        return dataSource;
    }
//------By Shreya on 05 January 2015 for creating the pdf or excel report

    private void createReport(HttpServletResponse response, HttpServletRequest request, String fileName, List<Map<String, Object>> filterAttributes, String title, String extension, JRDataSource dataSource, List<Map<String, Object>> resultMap, List<String> hiddenFields, List<AggregationSubtotalBuilder> subTotalColumnBuilerList, List<ColumnGridComponentBuilder> gridBuilder, List<ColumnGroupBuilder> columnGroupBuilderList, TextColumnBuilder<?>... columns) throws FileNotFoundException, DRException, IOException, JRException {
        AggregationSubtotalBuilder[] subTotals = subTotalColumnBuilerList.toArray(new AggregationSubtotalBuilder[subTotalColumnBuilerList.size()]);
        ColumnGridComponentBuilder[] gridBuilders = gridBuilder.toArray(new ColumnGridComponentBuilder[gridBuilder.size()]);
        ColumnGroupBuilder[] groupBuilders = columnGroupBuilderList.toArray(new ColumnGroupBuilder[columnGroupBuilderList.size()]);
        System.out.println("groupBuilders size :" + subTotals.length);
        JasperReportBuilder report = report();
        report.setTemplate(ReportBuilderTemplate.reportTemplate)
                .columns(columns)
                .title(ReportBuilderTemplate.createTitleComponent(null, title, request,"RIGHT",Boolean.TRUE))
                .pageFooter(ReportBuilderTemplate.createFooterComponent())
                .setDataSource(dataSource)
                //                .groupBy(groupBuilders)
                .setNoDataSplitType(SplitType.PREVENT)
                .setSummarySplitType(SplitType.PREVENT)
                .setDetailSplitType(SplitType.PREVENT);

        if (!CollectionUtils.isEmpty(subTotalColumnBuilerList)) {
            report.subtotalsAtSummary(subTotals);
        }
        if (!CollectionUtils.isEmpty(columnGroupBuilderList)) {
            report.groupBy(groupBuilders);
        }
        if (!CollectionUtils.isEmpty(filterAttributes)) {
            report.pageHeader(ReportBuilderTemplate.createHeaderComponent(filterAttributes, resultMap, hiddenFields));
        }
        if (!CollectionUtils.isEmpty(gridBuilder)) {
            report.columnGrid(gridBuilders);
        }
        File tempDir = checkIsExists(FolderManagement.getBasePath(), TEMP, null);
        StringBuilder tempFilePath = new StringBuilder(tempDir.getPath());
//        fileName = "report" + userDataBean.getId();
        tempFilePath.append(File.separator).append(fileName);

        if (columns.length > 8) {
            report.setPageFormat(PageType.A4, PageOrientation.LANDSCAPE);
        }
        if (extension.equalsIgnoreCase(".pdf")) {
            report.toPdf(export.pdfExporter(tempFilePath.toString() + ".pdf"));
        }
        if (extension.equalsIgnoreCase(".xls")) {
            JasperXlsExporterBuilder xlsExporter = export.xlsExporter(tempFilePath.toString() + ".xls")
                    .setDetectCellType(Boolean.TRUE)
                    .setIgnorePageMargins(Boolean.TRUE)
                    .setWhitePageBackground(Boolean.FALSE)
                    .setRemoveEmptySpaceBetweenColumns(Boolean.TRUE)
                    .setRemoveEmptySpaceBetweenRows(Boolean.TRUE);
            report.toXls(xlsExporter);
        }
        if (extension.equalsIgnoreCase(".xml")) {
            JasperXmlExporterBuilder xmlExporter = export.xmlExporter(tempFilePath.toString() + ".xml");
            report.toXml(xmlExporter);
        }
    }
//------By Shreya on 05 January 2015 for downloading the genearted report from the context path

    public byte[] downloadPdf(String fileName, String extension, HttpServletRequest request, HttpServletResponse response) {
        File file = null;
        File tempDir = checkIsExists(FolderManagement.getBasePath(), TEMP, null);
        StringBuilder tempFilePath = new StringBuilder(tempDir.getPath());
//        fileName = "report" + userDataBean.getId();
        tempFilePath.append(File.separator).append(fileName);
        if (extension.equals(".pdf")) {
            file = new File(tempFilePath.toString() + ".pdf");
        }
        if (extension.equals(".xls")) {
            file = new File(tempFilePath.toString() + ".xls");
        }
        if (extension.equals(".xml")) {
            file = new File(tempFilePath.toString() + ".xml");
        }
        InputStream is;
        try {
            is = new FileInputStream(file);
            long length = file.length();
            if (length > Integer.MAX_VALUE) {
            }
            byte[] report = new byte[(int) length];
            int offset = 0;
            int numRead = 0;
            while (offset < report.length && (numRead = is.read(report, offset, report.length - offset)) >= 0) {
                offset += numRead;
            }
            if (offset < report.length) {
                throw new IOException("Could not completely read file " + file.getName());
            }
            is.close();
            if (response != null) {
                if (extension.equals(".pdf")) {
                    response.setContentType("application/pdf");
                }
                if (extension.equals(".xls")) {
                    response.setContentType("application/vnd.ms-excel");
                }
                if (!StringUtils.isEmpty(fileName)) {
                    fileName = fileName + "-" + new Date().getTime();
                }
                response.setHeader("Content-Disposition", "attachment; filename=" + fileName + extension);
                response.getOutputStream().write(report);
                response.setContentLength(report.length);
                response.getOutputStream().flush();
                response.getOutputStream().close();
            }
            return report;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public MasterReportDataBean retrieveReportById(Long reportId) {
        RbReport report = rbReportService.retriveReportById(reportId, true, null);
        if (report != null) {
            MasterReportDataBean masterReportDataBean = new MasterReportDataBean();
            masterReportDataBean = convertReportModelToMasterReportDataBean(report, masterReportDataBean);
            return masterReportDataBean;
        }
        return null;
    }

    private MasterReportDataBean convertReportModelToMasterReportDataBean(RbReport report, MasterReportDataBean masterReportDataBean) {
        masterReportDataBean.setReportName(report.getReportName());
        masterReportDataBean.setReportGroup(report.getReportGroup());
        masterReportDataBean.setDescription(report.getDescription());
        masterReportDataBean.setEditable(report.getIsEditable());
        masterReportDataBean.setExternalReport(report.getIsExternalReport());
        masterReportDataBean.setQuery(report.getReportQuery());
        masterReportDataBean.setId(report.getId());
        masterReportDataBean.setReportCode(report.getReportCode());
        masterReportDataBean.setStatus(report.getStatus());
        masterReportDataBean.setJoinAttributes(report.getJoinAttributes());
        masterReportDataBean.setGroupAttributes(report.getGroupJson());
        masterReportDataBean.setOrderAttributes(report.getOrderJson());
        masterReportDataBean.setColorAttributes(report.getColorJson());
        if (!report.getIsExternalReport()) {
            List<Long> fieldIds = new LinkedList<>();
            List<RbReportField> reportFieldList = report.getRbReportFieldSet();
            List<RbFieldDataBean> fieldDataBeanList = new ArrayList<>();
            if (reportFieldList != null && !reportFieldList.isEmpty()) {
                for (RbReportField rbReportField : reportFieldList) {
                    RbFieldDataBean fieldDataBean = this.convertReportFieldEntityToDataBean(rbReportField);
                    if (fieldDataBean != null) {
                        if (fieldDataBean.getHkFieldId() != null && fieldDataBean.getComponentType().equals(HkSystemConstantUtil.CustomField.ComponentType.CURRENCY)) {
                            fieldIds.add(Long.parseLong(fieldDataBean.getHkFieldId()));
                        }
                        fieldDataBeanList.add(fieldDataBean);
                    }
                }
            }
            List<HkFieldEntity> fieldEntitys = fieldService.retrieveFieldsByIds(fieldIds, false);
            Map<Long, Integer> fieldIdAssociatedIdMap = new HashMap<>();
            for (HkFieldEntity fieldEntity : fieldEntitys) {
                fieldIdAssociatedIdMap.put(fieldEntity.getId(), fieldEntity.getAssociatedCurrency());
            }
            for (RbFieldDataBean fieldDataBean : fieldDataBeanList) {
                fieldDataBean.setAssociatedCurrency(fieldIdAssociatedIdMap.get(Long.parseLong(fieldDataBean.getHkFieldId())));
            }
//            System.out.println("fieldDataBeanList : "+fieldDataBeanList);
            masterReportDataBean.setColumns(fieldDataBeanList);
        }
        return masterReportDataBean;
    }

    private RbFieldDataBean convertReportFieldEntityToDataBean(RbReportField rbReportField) {
        if (rbReportField != null && !rbReportField.getIsArchive()) {
            RbFieldDataBean rbFieldDataBean = new RbFieldDataBean();
            rbFieldDataBean.setAlias(rbReportField.getReportFieldName());
            rbFieldDataBean.setColI18nRequired(rbReportField.getColI18nReq());
            rbFieldDataBean.setColName(rbReportField.getDbFieldName());
            rbFieldDataBean.setDataType(rbReportField.getFieldDataType());
            rbFieldDataBean.setDbBaseName(rbReportField.getDbBaseName());
            if (!StringUtils.isEmpty(rbReportField.getFieldLabel())) {
                rbFieldDataBean.setEditedFieldLabel(rbReportField.getFieldLabel().toLowerCase().replaceAll("\\s+", ""));
            }
            rbFieldDataBean.setDbBaseType(rbReportField.getDbBaseType());
            rbFieldDataBean.setFeature(rbReportField.getFeatureId());
            rbFieldDataBean.setFieldSequence(rbReportField.getFieldSequence());
            rbFieldDataBean.setFieldLabel(rbReportField.getFieldLabel());
            rbFieldDataBean.setOrderType(rbReportField.getOrderType());
            rbFieldDataBean.setId(rbReportField.getId());
            rbFieldDataBean.setFilter(rbReportField.getFilterAttributes());
            rbFieldDataBean.setJoinAttributes(rbReportField.getJoinAttributes());
            Map<RbReportConstantUtils.CustomAttribute, Object> showTotalMap = null;
            if (!StringUtils.isEmpty(rbReportField.getCustomAttributes())) {
                Type collectionType = new TypeToken<Map<RbReportConstantUtils.CustomAttribute, Object>>() {
                }.getType();
                showTotalMap = new Gson().fromJson(rbReportField.getCustomAttributes(), collectionType);
                rbFieldDataBean.setShowTotal((Boolean) showTotalMap.get(RbReportConstantUtils.CustomAttribute.SHOW_TOTAL));
                if (showTotalMap.get(RbReportConstantUtils.CustomAttribute.SECTION_NAME) != null) {
                    rbFieldDataBean.setSectionName(showTotalMap.get(RbReportConstantUtils.CustomAttribute.SECTION_NAME).toString());
                }
                if (showTotalMap.get(RbReportConstantUtils.CustomAttribute.COMPONENT_TYPE) != null) {
                    rbFieldDataBean.setComponentType(showTotalMap.get(RbReportConstantUtils.CustomAttribute.COMPONENT_TYPE).toString());
                }
                if (showTotalMap.get(RbReportConstantUtils.CustomAttribute.HK_FIELD_ID) != null) {
                    rbFieldDataBean.setHkFieldId(showTotalMap.get(RbReportConstantUtils.CustomAttribute.HK_FIELD_ID).toString());
                }
                if (showTotalMap.get(RbReportConstantUtils.CustomAttribute.MASTER_CODE) != null) {
                    rbFieldDataBean.setMasterCode(showTotalMap.get(RbReportConstantUtils.CustomAttribute.MASTER_CODE).toString());
                }
                if (showTotalMap.get(RbReportConstantUtils.CustomAttribute.IS_DFF) != null) {
                    rbFieldDataBean.setIsDff(Boolean.parseBoolean(showTotalMap.get(RbReportConstantUtils.CustomAttribute.IS_DFF).toString()));
                } else {
                    rbFieldDataBean.setIsDff(Boolean.FALSE);
                }
                if (showTotalMap.get(RbReportConstantUtils.CustomAttribute.IS_RULE) != null) {
                    rbFieldDataBean.setIsRule(Boolean.parseBoolean(showTotalMap.get(RbReportConstantUtils.CustomAttribute.IS_RULE).toString()));
                } else {
                    rbFieldDataBean.setIsRule(Boolean.FALSE);
                }
                if (showTotalMap.get(RbReportConstantUtils.CustomAttribute.INCLUDE_TIME) != null) {
                    rbFieldDataBean.setIncludeTime(Boolean.parseBoolean(showTotalMap.get(RbReportConstantUtils.CustomAttribute.INCLUDE_TIME).toString()));
                } else {
                    rbFieldDataBean.setIncludeTime(Boolean.FALSE);
                }
                if (showTotalMap.get(RbReportConstantUtils.CustomAttribute.IS_SUB_FORM_VALUE) != null) {
                    rbFieldDataBean.setIsSubFormValue(Boolean.parseBoolean(showTotalMap.get(RbReportConstantUtils.CustomAttribute.IS_SUB_FORM_VALUE).toString()));
                } else {
                    rbFieldDataBean.setIsSubFormValue(Boolean.FALSE);
                }
                if (showTotalMap.get(RbReportConstantUtils.CustomAttribute.HK_PARENT_FIELD_NAME) != null) {
                    rbFieldDataBean.setParentDbFieldName(showTotalMap.get(RbReportConstantUtils.CustomAttribute.HK_PARENT_FIELD_NAME).toString());
                }
                if (showTotalMap.get(RbReportConstantUtils.CustomAttribute.HK_PARENT_DB_BASE_NAME) != null) {
                    rbFieldDataBean.setParentDbBaseName(showTotalMap.get(RbReportConstantUtils.CustomAttribute.HK_PARENT_DB_BASE_NAME).toString());
                }

            }
            return rbFieldDataBean;
        }
        return null;
    }

    public Map<Long, String> retrieveFeatureNameByIds(List<Long> featureIds) throws GenericDatabaseException {
        Map<Long, String> featureIdNameMap = new HashMap<>();
        featureIdNameMap = userManagementServiceWrapper.retrieveFeatureNamesByIds(featureIds, false);
        return featureIdNameMap;

    }

    private String formatDate(Long longDate, Boolean isIncludeTime) throws ParseException {
        Date date = new Date(longDate);
        String timeFormat = isIncludeTime ? HkSystemConstantUtil.CUSTOM_FIELD_DATE_TIME_FORMAT : HkSystemConstantUtil.CUSTOM_FIELD_DATE_FORMAT;
        SimpleDateFormat dateFormatter = new SimpleDateFormat(timeFormat);
        String dates = dateFormatter.format(date);
        return dates;
    }

    //**********************  MITAL: JAVA-8 MUST BE SUPPORTED TO UNCOMMENT THIS CODE  **************************
    public Map<GroupingKey, List<Object>> groupRecords(List<LinkedHashMap<String, Object>> mapList, List<String> groupByColumns) {
        Map<GroupingKey, List<Object>> groupedResult = null;
//        System.out.println("groupByColumns :" + groupByColumns);
        if (!CollectionUtils.isEmpty(groupByColumns)) {
            Map<String, Object> mapArr[] = new LinkedHashMap[mapList.size()];
            if (!CollectionUtils.isEmpty(mapList)) {
                int count = 0;
                for (LinkedHashMap<String, Object> map : mapList) {
                    mapArr[count++] = map;
                }
            }
            Stream<Map<String, Object>> people = Stream.of(mapArr);
            groupedResult = people
                    .collect(Collectors.groupingBy(p -> new GroupingKey(p, groupByColumns), LinkedHashMap::new, Collectors.mapping((Map<String, Object> p) -> p, toList())));
            System.out.println(groupedResult);
        }
        return groupedResult;
    }

    public static class GroupingKey {

        ArrayList<Object> keys;

//        public KeyObj(Object... objs) {
//            keys = new ArrayList<Object>();
//
//            for (int i = 0; i < objs.length; i++) {
//                keys.add(objs[i]);
//            }
//        }
        public GroupingKey(Map<String, Object> map, List<String> cols) {
            keys = new ArrayList<>();

            for (String col : cols) {
                keys.add(map.get(col));
//                System.out.println("col is " + col);
//                System.out.println("map is " + map.get(col));
            }
        }

        // Add appropriate isEqual() ... you IDE should generate this
        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final GroupingKey other = (GroupingKey) obj;
            if (!Objects.equals(this.keys, other.keys)) {
                return false;
            }
            return true;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 37 * hash + Objects.hashCode(this.keys);
            return hash;
        }

        @Override
        public String toString() {
            return keys + "";
        }

        public ArrayList<Object> getKeys() {
            return keys;
        }

        public void setKeys(ArrayList<Object> keys) {
            this.keys = keys;
        }

    }

    //**********************  MITAL: JAVA-8 MUST BE SUPPORTED TO UNCOMMENT THIS CODE  **************************
    public MasterReportDataBean retrieveReportByFeature(Long featureId) {
        MasterReportDataBean masterReportDataBean = new MasterReportDataBean();
        if (featureId != null) {
            RbReport rbReport = rbReportService.retrieveReportByFeature(featureId);
            if (rbReport != null) {
                masterReportDataBean.setReportName(rbReport.getReportName());
                masterReportDataBean.setDescription(rbReport.getDescription());
                masterReportDataBean.setEditable(rbReport.getIsEditable());
                masterReportDataBean.setExternalReport(rbReport.getIsExternalReport());
                masterReportDataBean.setQuery(rbReport.getReportQuery());
                masterReportDataBean.setId(rbReport.getId());
                masterReportDataBean.setReportCode(rbReport.getReportCode());
                masterReportDataBean.setStatus(rbReport.getStatus());
                masterReportDataBean.setJoinAttributes(rbReport.getJoinAttributes());
                masterReportDataBean.setFeatureId(rbReport.getCustom1());
            }
        }
        return masterReportDataBean;
    }

    private class ConditionalExpressionBuilder extends AbstractSimpleExpression<Boolean> {

        private static final long serialVersionUID = 1L;
        private Map<String, Object> colMap;

        public ConditionalExpressionBuilder(Map<String, Object> colMap) {
            this.colMap = colMap;
        }

        public Map<String, Object> getColMap() {
            return colMap;
        }

        public void setColMap(Map<String, Object> colMap) {
            this.colMap = colMap;
        }

        @Override
        public Boolean evaluate(ReportParameters reportParameters) {
            Map<String, Object> colorAttributeMap = this.colMap;
            Boolean finalResult = null;
            List<Boolean> resultList = new ArrayList<>();
            String combinationType = colorAttributeMap.get("combinationType").toString();
            if (colorAttributeMap.get("columns") != null) {
                List<Map<String, Object>> columnsList = (List<Map<String, Object>>) colorAttributeMap.get("columns");
                for (Map<String, Object> columnsList1 : columnsList) {
                    Boolean result = false;
                    if (columnsList1 != null) {
                        String columnName = null;
                        if (columnsList1.get("isGroupBy") != null) {
                            if (columnsList1.get("isGroupBy").equals(Boolean.FALSE)) {
                                columnName = columnsList1.get("label").toString();
                            } else {
                                columnName = columnsList1.get("label").toString() + "1";
                            }
                        } else {
                            columnName = columnsList1.get("label").toString();
                        }
                        String newColumnName = columnName.toLowerCase().replaceAll("\\s+", "");
                        String dataType = columnsList1.get("type").toString();
                        String operator = columnsList1.get("operator").toString();
                        String componentType = columnsList1.get("componentType").toString();
                        if (!(operator.equals("is null") || operator.equals("is not null"))) {
                            if (dataType.equals(HkSystemConstantUtil.CustomField.DataType.INT8)) {
                                Long reportValue = reportParameters.getValue(newColumnName);
                                if (operator.equals("=")) {
                                    if (columnsList1.get("filterValue") != null) {
                                        Long filteredValue = Long.parseLong(columnsList1.get("filterValue").toString());
                                        result = filteredValue.equals(reportValue);
                                    }
                                } else if (operator.equals("!=")) {
                                    if (columnsList1.get("filterValue") != null) {
                                        Long filteredValue = Long.parseLong(columnsList1.get("filterValue").toString());
                                        result = !(filteredValue.equals(reportValue));
                                    }
                                } else if (operator.equals("<")) {
                                    if (columnsList1.get("filterValue") != null) {
                                        Long filteredValue = Long.parseLong(columnsList1.get("filterValue").toString());
                                        if (reportValue.compareTo(filteredValue) < 0) {
                                            result = true;
                                        }
                                    }
                                } else if (operator.equals(">")) {
                                    if (columnsList1.get("filterValue") != null) {
                                        Long filteredValue = Long.parseLong(columnsList1.get("filterValue").toString());
                                        if (reportValue.compareTo(filteredValue) > 0) {
                                            result = true;
                                        }
                                    }
                                } else if (operator.equals("<=")) {
                                    if (columnsList1.get("filterValue") != null) {
                                        Long filteredValue = Long.parseLong(columnsList1.get("filterValue").toString());
                                        if (reportValue.compareTo(filteredValue) <= 0) {
                                            result = true;
                                        }
                                    }
                                } else if (operator.equals(">=")) {
                                    if (columnsList1.get("filterValue") != null) {
                                        Long filteredValue = Long.parseLong(columnsList1.get("filterValue").toString());
                                        if (reportValue.compareTo(filteredValue) >= 0) {
                                            result = true;
                                        }
                                    }
                                } else if (operator.equals("between")) {
                                    if (columnsList1.get("filterValue") != null) {
                                        Long filteredFirstValue = Long.parseLong(columnsList1.get("filterValue").toString());
                                        Long filteredSecondValue = Long.parseLong(columnsList1.get("filterValueSecond").toString());
                                        if (filteredFirstValue.compareTo(reportValue) < 0 && reportValue.compareTo(filteredSecondValue) < 0) {
                                            result = true;
                                        }
                                    }
                                }
                            } else if (dataType.equals(HkSystemConstantUtil.CustomField.DataType.VARCHAR)) {
                                String reportValue = reportParameters.getValue(newColumnName);
                                System.out.println("reportValue varchar :" + reportValue);
                                if (operator.equals("=")) {
                                    if (columnsList1.get("filterValue") != null) {
                                        String filteredValue = columnsList1.get("filterValue").toString().toLowerCase();
                                        result = filteredValue.equals(reportValue.toLowerCase());
                                    }
                                } else if (operator.equals("!=")) {
                                    if (columnsList1.get("filterValue") != null) {
                                        String filteredValue = columnsList1.get("filterValue").toString().toLowerCase();
                                        result = !(filteredValue.equals(reportValue.toLowerCase()));
                                    }
                                } else if (operator.equals("in")) {
                                    if (columnsList1.get("filterValue") != null) {
                                        String filteredValue = columnsList1.get("filterValue").toString().toLowerCase();
                                        List<String> firstValues = new ArrayList<>();
                                        String[] arrays = null;
                                        if (operator.equals("in") || operator.equals("not in")) {
                                            arrays = filteredValue.split(",");
                                        } else {
                                            arrays = new String[]{filteredValue};
                                        }
                                        for (String array1 : arrays) {
                                            firstValues.add(array1.trim());
                                        }
                                        if (firstValues.contains(reportValue.toString().toLowerCase())) {
                                            result = true;
                                        }
                                    }
                                } else if (operator.equals("not in")) {
                                    if (columnsList1.get("filterValue") != null) {
                                        String filteredValue = columnsList1.get("filterValue").toString().toLowerCase();
                                        List<String> firstValues = new ArrayList<>();
                                        String[] arrays = null;
                                        if (operator.equals("in") || operator.equals("not in")) {
                                            arrays = filteredValue.split(",");
                                        } else {
                                            arrays = new String[]{filteredValue};
                                        }
                                        for (String array1 : arrays) {
                                            firstValues.add(array1.trim());
                                        }
                                        if (!firstValues.contains(reportValue.toString().toLowerCase())) {
                                            result = true;
                                        }
                                    }
                                } else if (operator.equals("like")) {
                                    String filteredValue = "%" + columnsList1.get("filterValue").toString().toLowerCase() + "%";
                                    filteredValue = filteredValue.replace("_", ".");
                                    filteredValue = filteredValue.replace("%", ".*?");
                                    if (reportValue.toString().toLowerCase().matches(filteredValue)) {
                                        result = true;
                                    }
                                }
                            } else if (dataType.equals(HkSystemConstantUtil.CustomField.DataType.DOUBLE)) {
                                String reportStringValue = reportParameters.getValue(newColumnName).toString();
                                Double reportValue = null;
                                if (componentType.equals(HkSystemConstantUtil.CustomField.ComponentType.CURRENCY)) {
                                    reportValue = Double.parseDouble(reportStringValue.split(" ")[0].trim());
                                } else {
                                    reportValue = reportParameters.getValue(newColumnName);
                                }
                                if (operator.equals("=")) {
                                    if (columnsList1.get("filterValue") != null) {
                                        Double filteredValue = Double.parseDouble(columnsList1.get("filterValue").toString());
                                        result = filteredValue.equals(reportValue);
                                    }
                                } else if (operator.equals("!=")) {
                                    if (columnsList1.get("filterValue") != null) {
                                        Double filteredValue = Double.parseDouble(columnsList1.get("filterValue").toString());
                                        result = !(filteredValue.equals(reportValue));
                                    }
                                } else if (operator.equals("<")) {
                                    if (columnsList1.get("filterValue") != null) {
                                        Double filteredValue = Double.parseDouble(columnsList1.get("filterValue").toString());
                                        if (reportValue.compareTo(filteredValue) < 0) {
                                            result = true;
                                        }
                                    }
                                } else if (operator.equals(">")) {
                                    if (columnsList1.get("filterValue") != null) {
                                        Double filteredValue = Double.parseDouble(columnsList1.get("filterValue").toString());
                                        if (reportValue.compareTo(filteredValue) > 0) {
                                            result = true;
                                        }
                                    }
                                } else if (operator.equals("<=")) {
                                    if (columnsList1.get("filterValue") != null) {
                                        Double filteredValue = Double.parseDouble(columnsList1.get("filterValue").toString());
                                        if (reportValue.compareTo(filteredValue) <= 0) {
                                            result = true;
                                        }
                                    }
                                } else if (operator.equals(">=")) {
                                    if (columnsList1.get("filterValue") != null) {
                                        Double filteredValue = Double.parseDouble(columnsList1.get("filterValue").toString());
                                        if (reportValue.compareTo(filteredValue) >= 0) {
                                            result = true;
                                        }
                                    }
                                } else if (operator.equals("between")) {
                                    if (columnsList1.get("filterValue") != null) {
                                        Double filteredFirstValue = Double.parseDouble(columnsList1.get("filterValue").toString());
                                        Double filteredSecondValue = Double.parseDouble(columnsList1.get("filterValueSecond").toString());
                                        if (filteredFirstValue.compareTo(reportValue) < 0 && reportValue.compareTo(filteredSecondValue) < 0) {
                                            result = true;
                                        }
                                    }
                                }
                            } else if (dataType.equals(HkSystemConstantUtil.CustomField.DataType.TIMESTAMP)) {
                                String reportValue = reportParameters.getValue(newColumnName);
                                String filteredValue = columnsList1.get("filterValue").toString();
                                String filteredSecondValue = null;
                                if (operator.equals("between")) {
                                    filteredSecondValue = columnsList1.get("filterValueSecond").toString();
                                }
                                componentType = columnsList1.get("componentType").toString();
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                SimpleDateFormat simpleDateFormats = new SimpleDateFormat("dd/MM/yyyy");
                                Date first = null;
                                Date reportDate = null;
                                try {
                                    first = simpleDateFormat.parse(filteredValue.trim().substring(0, 10));
                                } catch (ParseException ex) {
                                    Logger.getLogger(ReportBuilderTransformerBean.class.getName()).log(Level.SEVERE, null, ex);
                                }
                                try {
                                    reportDate = simpleDateFormats.parse(reportValue.trim().substring(0, 10));
                                } catch (ParseException ex) {
                                    Logger.getLogger(ReportBuilderTransformerBean.class.getName()).log(Level.SEVERE, null, ex);
                                }
                                if (componentType != null && componentType.equals(HkSystemConstantUtil.CustomField.ComponentType.DATE_RANGE)) {
                                    Date fromDate = null;
                                    Date toDate = null;
                                    String[] strgValueList = reportValue.split("to");
                                    if (!StringUtils.isEmpty(strgValueList[0]) && !StringUtils.isEmpty(strgValueList[1])) {
                                        simpleDateFormats = new SimpleDateFormat("dd/MM/yyyy");
                                        try {
                                            fromDate = simpleDateFormats.parse(strgValueList[0].trim().substring(0, 10));
                                        } catch (ParseException ex) {
                                            Logger.getLogger(ReportBuilderTransformerBean.class.getName()).log(Level.SEVERE, null, ex);
                                        }
                                        try {
                                            toDate = simpleDateFormats.parse(strgValueList[1].trim().substring(0, 10));
                                        } catch (ParseException ex) {
                                            Logger.getLogger(ReportBuilderTransformerBean.class.getName()).log(Level.SEVERE, null, ex);
                                        }
                                    }
                                    if (operator.equals("=")) {
                                        if (fromDate.compareTo(first) <= 0 && first.compareTo(toDate) <= 0) {
                                            result = true;
                                        }

                                    } else if (operator.equals("!=")) {
                                        if (fromDate.compareTo(first) > 0 || first.compareTo(toDate) > 0) {
                                            result = true;
                                        }
                                    }
                                } else {

                                    if (operator.equals("=")) {
                                        if (first.equals(reportDate)) {
                                            result = true;
                                        }
                                    } else if (operator.equals("!=")) {
                                        if (!first.equals(reportDate)) {
                                            result = true;
                                        }
                                    } else if (operator.equals("<")) {
                                        if (first.after(reportDate)) {
                                            result = true;
                                        }
                                    } else if (operator.equals(">")) {
                                        if (first.before(reportDate)) {
                                            result = true;
                                        }
                                    } else if (operator.equals("<=")) {
                                        if (first.after(reportDate) || first.equals(reportDate)) {
                                            result = true;
                                        }
                                    } else if (operator.equals(">=")) {
                                        if (first.before(reportDate) || first.equals(reportDate)) {
                                            result = true;
                                        }
                                    } else if (operator.equals("between")) {
                                        Date second = null;
                                        if (filteredSecondValue != null) {
                                            try {
                                                second = simpleDateFormat.parse(filteredSecondValue.trim().substring(0, 10));
                                            } catch (ParseException ex) {
                                                Logger.getLogger(ReportBuilderTransformerBean.class.getName()).log(Level.SEVERE, null, ex);
                                            }
                                        }
                                        if (first.compareTo(reportDate) < 0 && reportDate.compareTo(second) < 0) {
                                            result = true;
                                        }
                                    }
                                }

                            } else if (dataType.equals(HkSystemConstantUtil.CustomField.DataType.BOOLEAN)) {
                                Boolean reportValue = Boolean.parseBoolean(reportParameters.getValue(newColumnName));
                                if (operator.equals("=")) {
                                    if (columnsList1.get("filterValue") != null) {
                                        Boolean filteredValue = Boolean.parseBoolean(columnsList1.get("filterValue").toString());
                                        result = filteredValue.equals(reportValue);
                                    }
                                } else if (operator.equals("!=")) {
                                    if (columnsList1.get("filterValue") != null) {
                                        Boolean filteredValue = Boolean.parseBoolean(columnsList1.get("filterValue").toString());
                                        result = !(filteredValue.equals(reportValue));
                                    }
                                }
                            }
                        } else {
                            if (operator.equals("is null")) {
                                result = (reportParameters.getValue(newColumnName).toString().trim().equals("N/A"));
                            } else if (operator.equals("is not null")) {
                                result = !(reportParameters.getValue(newColumnName).toString().trim().equals("N/A"));
                            }
                        }
                    }
                    resultList.add(result);
                }
                if (combinationType.equals("ANY")) {
                    int index = 0;
                    for (Boolean resultList1 : resultList) {
                        if (index == 0) {
                            finalResult = resultList1;
                        } else {
                            finalResult = finalResult || resultList1;
                        }
                        index++;
                    }
                } else {
                    int index = 0;
                    for (Boolean resultList1 : resultList) {
                        if (index == 0) {
                            finalResult = resultList1;
                        } else {
                            finalResult = finalResult && resultList1;
                        }
                        index++;
                    }
                }
            }
            return finalResult;
        }
    }
    
    public Boolean retrieveCurrencyConfiguration() {
        Boolean currencyConfiguration = false;
        HkSystemConfigurationEntity configurationEntity = hkFoundationService.retrieveSystemConfigurationByKey(HkSystemConstantUtil.FranchiseConfiguration.CURR_VISIBILITY_STATUS, userDataBean.getCompanyId());
        if (configurationEntity != null) {
            String currencyFlag = configurationEntity.getKeyValue();
            if (currencyFlag.equals("on")) {
                currencyConfiguration = Boolean.TRUE;
            } else {
                currencyConfiguration = Boolean.FALSE;
            }

        }
        return currencyConfiguration;
    }

    public Boolean retrieveViewCurrencyDataRightsOfLoggedInUser() {
        List<FeatureDataBean> features = userDataBean.getFeatures();
        if (!CollectionUtils.isEmpty(features)) {
            for (FeatureDataBean featureDataBean : features) {
                if (featureDataBean.getFeatureName().equals("viewCurrencyData") && featureDataBean.getMenuType().equals("EI")) {
                    return Boolean.TRUE;
                }
            }
        }
        return Boolean.FALSE;
    }
    
    private static boolean isContain(String source, String subItem) {
        String pattern = "\\b" + subItem + "\\b";
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(source);
        return m.find();
    }

}
