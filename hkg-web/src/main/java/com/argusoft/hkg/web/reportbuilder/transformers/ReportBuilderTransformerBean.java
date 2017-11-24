/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.reportbuilder.transformers;

import com.argusoft.generic.database.common.GenericDao;
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
import com.argusoft.hkg.model.HkSectionEntity;
import com.argusoft.hkg.model.HkSubFormFieldEntity;
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
import com.argusoft.hkg.web.center.usermanagement.databeans.LoginDataBean;
import com.argusoft.hkg.web.customfield.databeans.CustomFieldDataBean;
import com.argusoft.hkg.web.customfield.databeans.CustomFieldInfoDataBean;
import com.argusoft.hkg.web.customfield.databeans.SubEntityDataBean;
import com.argusoft.hkg.web.customfield.transformers.CustomFieldTransformerBean;
import com.argusoft.hkg.web.internationalization.databeans.LanguageDataBean;
import com.argusoft.hkg.web.internationalization.databeans.LocaleDataBean;
import com.argusoft.hkg.web.internationalization.databeans.SearchBeanForLabel;
import com.argusoft.hkg.web.internationalization.transformers.LocalesTransformerBean;
import com.argusoft.hkg.web.reportbuilder.databeans.RbTabularRelationDataBean;
import static com.argusoft.hkg.web.reportbuilder.transformers.ReportBuilderTemplate.contentRightAlignStyle;
import static com.argusoft.hkg.web.reportbuilder.transformers.ReportBuilderTemplate.rootStyle;
import com.argusoft.hkg.web.reportbuilder.wrapper.ReportBuliderServiceWrapper;
import com.argusoft.hkg.web.usermanagement.databeans.FeatureDataBean;
import com.argusoft.hkg.web.usermanagement.databeans.FieldDataBean;
import com.argusoft.hkg.web.usermanagement.databeans.FranchiseDataBean;
import com.argusoft.hkg.web.usermanagement.transformers.FranchiseTransformerBean;
import com.argusoft.hkg.web.usermanagement.wrapper.UserManagementServiceWrapper;
import com.argusoft.hkg.web.util.ApplicationMasterInitializer;
import com.argusoft.hkg.web.util.SelectItem;
import com.argusoft.hkg.web.util.SessionUtil;
import com.argusoft.reportbuilder.common.constantutil.RbReportConstantUtils;
import com.argusoft.reportbuilder.core.RbReportService;
import com.argusoft.reportbuilder.core.bean.MasterReportDataBean;
import com.argusoft.reportbuilder.core.bean.QueryDataBean;
import com.argusoft.reportbuilder.core.bean.QueryJoinDataBean;
import com.argusoft.reportbuilder.core.bean.QueryOrderByDataBean;
import com.argusoft.hkg.web.reportbuilder.databeans.RbEmailReportConfigurationDataBean;
import com.argusoft.hkg.web.util.AesEncrypter;
import com.argusoft.reportbuilder.core.bean.RbFieldDataBean;
import com.argusoft.reportbuilder.core.bean.RbReportTableDetailDataBean;
import com.argusoft.reportbuilder.model.RbEmailReportConfigurationEntity;
import com.argusoft.reportbuilder.model.RbEmailReportStatusEntity;
import com.argusoft.reportbuilder.model.RbEmailReportStatusEntityPK;
import com.argusoft.reportbuilder.model.RbReport;
import com.argusoft.reportbuilder.model.RbReportField;
import com.argusoft.reportbuilder.model.RbReportTableDtl;
import com.argusoft.reportbuilder.model.RbTabularRelationEntity;
import com.argusoft.reportbuilder.model.RbUserReportDashboardEntity;
import com.argusoft.reportbuilder.model.RbUserReportDashboardEntityPK;
import com.argusoft.reportbuilder.util.QueryStringUtils;
import com.argusoft.usermanagement.common.core.UMRoleService;
import com.argusoft.usermanagement.common.core.UMUserService;
import com.argusoft.usermanagement.common.database.UMRoleDao;
import com.argusoft.usermanagement.common.database.UMRoleFeatureDao;
import com.argusoft.usermanagement.common.model.UMRoleFeature;
import com.argusoft.usermanagement.common.model.UMUser;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.sql.SQLException;
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
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.toList;
import java.util.stream.Stream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.jasper.builder.export.JasperXlsExporterBuilder;
import net.sf.dynamicreports.jasper.builder.export.JasperXmlExporterBuilder;
import net.sf.dynamicreports.report.base.expression.AbstractSimpleExpression;
import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.col;
import static net.sf.dynamicreports.report.builder.DynamicReports.exp;
import static net.sf.dynamicreports.report.builder.DynamicReports.export;
import static net.sf.dynamicreports.report.builder.DynamicReports.grid;
import static net.sf.dynamicreports.report.builder.DynamicReports.grp;
import static net.sf.dynamicreports.report.builder.DynamicReports.report;
import static net.sf.dynamicreports.report.builder.DynamicReports.sbt;
import static net.sf.dynamicreports.report.builder.DynamicReports.stl;
import static net.sf.dynamicreports.report.builder.DynamicReports.type;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.builder.component.SubreportBuilder;
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
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.bson.types.ObjectId;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
//import org.apache.commons.io.IOUtils;

/**
 *
 * @author shreya
 */
@Service
public class ReportBuilderTransformerBean {

    @Autowired
    RbReportService rbReportService;

    @Autowired
    private ApplicationMasterInitializer applicationMasterInitializer;

    @Autowired
    LoginDataBean hkLoginDataBean;

    @Autowired
    private HkFieldService fieldService;

    @Autowired
    private HkFoundationService hkFoundationService;

    @Autowired
    UserManagementServiceWrapper userManagementServiceWrapper;

    @Autowired
    FranchiseTransformerBean franchiseTransformerBean;

    @Autowired
    private CustomFieldTransformerBean customFieldTransformerBean;

    @Autowired
    LocalesTransformerBean localesTransformerBean;

    @Autowired
    ReportBuliderServiceWrapper reportBuliderServiceWrapper;

    @Autowired
    SessionUtil sessionUtil;

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
    UMRoleService roleService;

    public Long createReport(MasterReportDataBean masterReportDataBean) throws GenericDatabaseException {
        RbReport rbReport = new RbReport();
        rbReport = convertMasterReportDataBeanToReportModel(masterReportDataBean, rbReport);
        rbReport.setCreatedBy(hkLoginDataBean.getId());
        rbReport.setCreatedOn(Calendar.getInstance().getTime());
        List<RbReportField> rbReportFieldList = new ArrayList<>();
        List<RbReportTableDtl> rbReportTableDtls = new ArrayList<>();
        if (masterReportDataBean.isExternalReport() == null) {
            masterReportDataBean.setExternalReport(Boolean.FALSE);
        }
        if (!masterReportDataBean.isExternalReport()) {
            List<RbFieldDataBean> fieldDataBeanList = masterReportDataBean.getColumns();
            if (fieldDataBeanList != null && !fieldDataBeanList.isEmpty()) {
                for (RbFieldDataBean fieldDataBean : fieldDataBeanList) {
                    RbReportField rbReportField = new RbReportField();
                    rbReportField = convertReportFieldDataBeanToReportFieldModel(fieldDataBean, rbReportField);
                    rbReportField.setDbFieldName(fieldDataBean.getColName());
                    rbReportField.setReport(rbReport);
                    rbReportFieldList.add(rbReportField);
                }
            }
            List<RbReportTableDetailDataBean> tableDtls = masterReportDataBean.getTableDtls();
            if (!CollectionUtils.isEmpty(tableDtls)) {
                for (RbReportTableDetailDataBean tableDtl : tableDtls) {
                    RbReportTableDtl rbReportTableDtl = new RbReportTableDtl();
                    rbReportTableDtl = convertReportTableDtlDataBeanToReportTableDtlModel(tableDtl, rbReportTableDtl);
                    rbReportTableDtl.setReport(rbReport);
                    rbReportTableDtls.add(rbReportTableDtl);
                }
            }
            rbReport.setRbReportTableDtls(rbReportTableDtls);
        }
        Long id = reportBuliderServiceWrapper.createReport(rbReport, rbReportFieldList, hkLoginDataBean.getCompanyId(), hkLoginDataBean.getId());
        return id;
    }

    public Map<String, Object> retrieveAllReports(boolean showAll, Integer offSet, Integer limit) {
        List<RbReport> reportList = rbReportService.retriveAllReports(true, hkLoginDataBean.getCompanyId());
        List<MasterReportDataBean> reportDataBeanList = new ArrayList();
        List<Long> userFeatureIds = new ArrayList<>();
        if (!showAll) {
            List<FeatureDataBean> userFeatureList = applicationMasterInitializer.generateMenuByRoles(hkLoginDataBean.getRoleIds(), hkLoginDataBean.isIsCompanyActivated());
            if (!CollectionUtils.isEmpty(userFeatureList)) {
                for (FeatureDataBean featureDataBean : userFeatureList) {
                    userFeatureIds.add(featureDataBean.getId());
                }
            }
        }
        if (reportList != null && !reportList.isEmpty()) {
            if (!CollectionUtils.isEmpty(userFeatureIds)) {
                Iterator<RbReport> itr = reportList.iterator();
                while (itr.hasNext()) {
                    RbReport rbReport = itr.next();
                    if (!userFeatureIds.contains(rbReport.getCustom1())) {
                        itr.remove();
                    }
                }
            }
            for (RbReport rbReport : reportList) {
                MasterReportDataBean reportDataBean = new MasterReportDataBean();
                reportDataBeanList.add(convertReportModelToMasterReportDataBean(rbReport, reportDataBean));
            }
        }
        List<MasterReportDataBean> newPaginatedList = new LinkedList<>();
        if (!CollectionUtils.isEmpty(reportDataBeanList) && limit != null && offSet != null) {
            for (int i = offSet; i < (offSet + limit); i++) {
                if (i < reportDataBeanList.size()) {
                    newPaginatedList.add(reportDataBeanList.get(i));
                }
            }
        } else {
            newPaginatedList.addAll(reportDataBeanList);
        }
        Map<String, Object> finalDataOfReport = new HashMap<>();
        finalDataOfReport.put("records", newPaginatedList);
        finalDataOfReport.put("totalRecords", reportDataBeanList.size());
        return finalDataOfReport;
    }

    public Boolean isReportNameExists(String reportName) {
        List<RbReport> reportList = rbReportService.retriveAllReports(false, hkLoginDataBean.getCompanyId());
        if (!CollectionUtils.isEmpty(reportList)) {
            List<String> reportNameList = new ArrayList<>();
            for (RbReport rbReport : reportList) {
                reportNameList.add(rbReport.getReportName());
            }
            if (!StringUtils.isEmpty(reportName)) {
                if (reportNameList.contains(reportName)) {
                    return true;
                }
            } else {
                return false;
            }
        }
        return false;
    }

    public List<SelectItem> retrieveReportTitles() {
        List<RbReport> reportList = rbReportService.retriveAllReports(null, hkLoginDataBean.getCompanyId());
        List<SelectItem> hkSelectItemList = new ArrayList<>();
        if (reportList != null && !reportList.isEmpty()) {
            for (RbReport rbReport : reportList) {
                SelectItem selectItem = new SelectItem(rbReport.getId(), rbReport.getReportName());
                hkSelectItemList.add(selectItem);
            }
        }
        return hkSelectItemList;
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

    public List<String> retrieveTableNames() {
        return rbReportService.retrieveTableNames();
    }

    public Map<String, Object> retrieveTableColumns(List<String> tableNameList) {
        return rbReportService.retrieveAllTableWithColumnsListByTableList(tableNameList);
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
                                        Map<String, HkFieldEntity> fields = fieldService.retrieveMapOfDBFieldNameWithEntity(Arrays.asList(hkRuleCriteriaDocument.getDbFieldName()), hkLoginDataBean.getCompanyId());
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
                                        Map<String, HkFieldEntity> fields = fieldService.retrieveMapOfDBFieldNameWithEntity(Arrays.asList(hkRuleCriteriaDocument.getDbFieldName()), hkLoginDataBean.getCompanyId());
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
            if (hkLoginDataBean.getIsHKAdmin()) {
                List<FranchiseDataBean> retrieveAllFranchise = franchiseTransformerBean.retrieveAllFranchise(false);
                if (!CollectionUtils.isEmpty(retrieveAllFranchise)) {
                    for (FranchiseDataBean franchiseDataBean : retrieveAllFranchise) {
                        franchiseIds.add(franchiseDataBean.getId());
                    }
                }
            } else {
                franchiseIds.add(hkLoginDataBean.getCompanyId());
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
                List<HkValueEntity> hkValueEntitys = hkFoundationService.retrieveMasterValuesByCode(hkLoginDataBean.getCompanyId(), keyCodes);
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
                                        String prefferedLang = hkLoginDataBean.getPrefferedLang();
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
        sessionUtil.setReportColumnValueMap(new HashMap<>(columnValuesMap));

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
            System.out.println("recordListForSettingOffset :" + recordListForSettingOffset);
            Map<GroupingKey, List<Object>> groupRecords = new HashMap<>();
            if (forPdf) {
                groupRecords = this.groupRecords(recordListForSettingOffset, groupByColumns);
            } else {
                groupRecords = this.groupRecords(recordListForSettingOffset, groupByColumns);
            }
            System.out.println("groupRecords :" + groupRecords);
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
            sessionUtil.setReportColumnValueMap(new HashMap<>(resultMap));
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
        //System.out.println("columnValuesMap  ::::: in remove method :::::" + columnValuesMap);
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
        //System.out.println("groupByColumns :" + groupedWithoutArray);
        if (!CollectionUtils.isEmpty(groupedWithoutArray)) {
            for (String groupByColumn : groupedWithoutArray) {
                for (Map<String, Object> finalListOfData1 : finalListOfData) {
                    if (finalListOfData1.containsKey(groupByColumn)) {
                        finalListOfData1.put(groupByColumn + "1", finalListOfData1.get(groupByColumn));
                    }
                }
            }
        }
        //System.out.println("finalListOfData :" + finalListOfData);
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
                                    Logger.getLogger(ReportBuilderTransformerBean.class
                                            .getName()).log(Level.SEVERE, null, ex);
                                }
                                Date second = null;
                                try {
                                    second = simpleDateFormats.parse(m2.get(orderByColumns.get(i).get("columnName").toString()).toString().trim().substring(0, 10));

                                } catch (ParseException ex) {
                                    Logger.getLogger(ReportBuilderTransformerBean.class
                                            .getName()).log(Level.SEVERE, null, ex);
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
                                    Logger.getLogger(ReportBuilderTransformerBean.class
                                            .getName()).log(Level.SEVERE, null, ex);
                                }
                                Date second = null;
                                try {
                                    second = simpleDateFormats.parse(m1.get(orderByColumns.get(i).get("columnName").toString()).toString().trim().substring(0, 10));

                                } catch (ParseException ex) {
                                    Logger.getLogger(ReportBuilderTransformerBean.class
                                            .getName()).log(Level.SEVERE, null, ex);
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

    public Map<String, String> retriveQueryMetadata(String query) {
        if (!StringUtils.isEmpty(query)) {
            return rbReportService.retriveQueryMetadata(query);
        }
        return null;
    }

    public Map<String, Object> retrieveDistinctColumnResults(String query, String columnName) {
        return rbReportService.retrieveDistinctColumnResults(query, columnName);
    }

    public Map<String, Object> retrieveLimitedColumnValues(String columnName, String searchString, Integer offSet, Integer limit, Boolean isGrouped) {
        Map<String, Object> columnValueMap = sessionUtil.getReportColumnValueMap();
        Map<String, Object> reportData = new HashMap<>();
        List<String> dataToSend = new ArrayList<>();
        if (!CollectionUtils.isEmpty(columnValueMap)) {
            Set<String> finalList = new HashSet<>();
            List<LinkedHashMap<String, Object>> recordListForSettingOffset = new ArrayList<>();
            if (!isGrouped) {
                recordListForSettingOffset = (ArrayList<LinkedHashMap<String, Object>>) columnValueMap.get("records");
            } else {
                Map<String, List<Object>> groupRecords = (Map<String, List<Object>>) columnValueMap.get("records");
                for (Map.Entry<String, List<Object>> entrySet : groupRecords.entrySet()) {
                    String key = entrySet.getKey();
                    List<Object> value = entrySet.getValue();
                    for (Object value1 : value) {
                        if (value1 != null) {
                            recordListForSettingOffset.add((LinkedHashMap<String, Object>) value1);
                        }
                    }
                }
            }

            for (LinkedHashMap<String, Object> row : recordListForSettingOffset) {
                if (row.get(columnName) != null) {
                    finalList.add(row.get(columnName).toString());
                }
            }
            Iterator<String> itr = finalList.iterator();
            while (itr.hasNext()) {
                String value = itr.next();
                if (!StringUtils.isEmpty(value) && !StringUtils.isEmpty(searchString)) {
                    if (!value.toLowerCase().contains(searchString.toLowerCase())) {
                        itr.remove();
                    }
                }

            }
            List<String> data = new ArrayList<>(finalList);

            for (int i = offSet; i < (offSet + limit); i++) {
                if (i < finalList.size()) {
                    dataToSend.add(data.get(i));
                }
            }
            reportData.put("total", finalList.size());
            reportData.put("columnValues", dataToSend);
        }
        return reportData;
    }

    public String generateReportLink(String reportCode, Boolean isEditable) {
//        SystemConfiguration systemConfiguration = systemConfigurationService.retrieveSystemConfigurationByKey("PENTAHO_URL");
        String pentahoLink = "http://badrifoundation.org:9090/pentaho/content/saiku-ui/survey.html?solution=survey_files&path=&dimension_prefetch=false&biplugin=true&userid=survey&password=argusadmin";
        String reportLink = "";
        if (isEditable) {
            reportLink = pentahoLink + "&action=" + reportCode + ".saiku#query/open/" + reportCode + ".saiku";
        } else {
            reportLink = pentahoLink + "&mode=view&action=" + reportCode + ".saiku#query/open/" + reportCode + ".saiku";
        }
        return reportLink;
    }

    public void generateExcelSheet(MasterReportDataBean masterReportDataBean, HttpSession session) {
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook();
        XSSFSheet sheet = xssfWorkbook.createSheet(masterReportDataBean.getReportName());
        List<List<String>> reportData = rbReportService.retrieveAllResults(masterReportDataBean.getQuery());
        if (reportData != null && !reportData.isEmpty()) {
            int rowNum = 0;
            int cellNum = 0;
            Row headerRow = sheet.createRow(rowNum);

            XSSFFont font = xssfWorkbook.createFont();
            font.setFontName("Arial");
            font.setBold(true);
            font.setItalic(false);

            CellStyle headerStyle = xssfWorkbook.createCellStyle();
            headerStyle.setAlignment(CellStyle.ALIGN_CENTER);
            headerStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
            headerStyle.setFont(font);

            List<RbFieldDataBean> fieldDataBeanList = masterReportDataBean.getColumns();

            if (fieldDataBeanList != null && !fieldDataBeanList.isEmpty()) {
                List<String> joinColNameList = new ArrayList<>();
                for (RbFieldDataBean fieldDataBean : fieldDataBeanList) {

                    Cell headerCell = headerRow.createCell(cellNum);
                    if (fieldDataBean.getAlias() != null && !fieldDataBean.getAlias().equals("")) {
                        headerCell.setCellValue(fieldDataBean.getAlias());
                    } else {
                        String colTitle = "";
                        String joinColName = "";
                        colTitle = fieldDataBean.getColName();
                        joinColName = colTitle.substring(colTitle.indexOf(".") + 1);
                        if (!joinColNameList.contains(joinColName)) {
                            colTitle = colTitle.substring(colTitle.indexOf(".") + 1);
                        }
                        joinColNameList.add(colTitle);
                        headerCell.setCellValue(colTitle);
                    }
                    headerCell.setCellStyle(headerStyle);
                    cellNum++;
                }
            }
            ++rowNum;

            for (List<String> row : reportData) {
                Row rowData = sheet.createRow(++rowNum);
                cellNum = 0;
                if (row != null && !row.isEmpty()) {
                    for (String fieldValue : row) {
                        Cell cell = rowData.createCell(cellNum);
                        if (fieldValue == null || fieldValue.equals("")) {
                            fieldValue = "N/A";
                        }
                        if (!fieldDataBeanList.get(cellNum).isColI18nRequired() && fieldDataBeanList.get(cellNum).getDataType().equals("int8")) {
//                            cell.setCellType(Cell.CELL_TYPE_NUMERIC);
                            cell.setCellValue(Double.parseDouble(fieldValue));
                        } else {
                            cell.setCellValue(fieldValue);
                        }
                        cellNum++;
                    }
                }
            }
            for (int i = 0; i < cellNum; i++) {
                sheet.autoSizeColumn(i);
            }
        }
        session.setAttribute("xlsReport", xssfWorkbook);

    }

    public void downlodExcelSheet(HttpSession session, HttpServletResponse response) {
        if (session.getAttribute("xlsReport") != null) {
            XSSFWorkbook xssfWorkbook = (XSSFWorkbook) session.getAttribute("xlsReport");
            try {
                //Write the workbook in file system
                response.setContentType("application/vnd.ms-excel");
                response.setHeader("Content-Disposition", "attachment; filename=" + xssfWorkbook.getSheetName(0) + ".xlsx");
                OutputStream out = response.getOutputStream();

                xssfWorkbook.write(out);
                response.flushBuffer();
                response.getOutputStream().flush();
                response.getOutputStream().close();
                out.flush();
                out.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public RbReport convertMasterReportDataBeanToReportModel(MasterReportDataBean masterReportDataBean, RbReport rbReport) {
        if (rbReport == null) {
            rbReport = new RbReport();
        }
//        rbReport.setCreatedBy(hkLoginDataBean.getId());
//        rbReport.setCreatedOn(Calendar.getInstance().getTime());
        rbReport.setDescription(masterReportDataBean.getDescription());
        rbReport.setReportName(masterReportDataBean.getReportName());
        rbReport.setReportGroup(masterReportDataBean.getReportGroup());
        rbReport.setCompany(hkLoginDataBean.getCompanyId());
        if (masterReportDataBean.isExternalReport() != null) {
            rbReport.setIsExternalReport(masterReportDataBean.isExternalReport());
        } else {
            rbReport.setIsExternalReport(Boolean.FALSE);
        }
        if (masterReportDataBean.isEditable() != null) {
            rbReport.setIsEditable(masterReportDataBean.isEditable());
        } else {
            rbReport.setIsEditable(Boolean.FALSE);
        }
        if (masterReportDataBean.getReportCode() != null) {
            rbReport.setReportCode(masterReportDataBean.getReportCode());
        }
        rbReport.setGroupJson(masterReportDataBean.getGroupAttributes());
        rbReport.setOrderJson(masterReportDataBean.getOrderAttributes());
        rbReport.setColorJson(masterReportDataBean.getColorAttributes());
        rbReport.setIsActive(Boolean.TRUE);
        rbReport.setIsArchive(Boolean.FALSE);
        rbReport.setLastModifiedBy(hkLoginDataBean.getId());
        rbReport.setLastModifiedOn(Calendar.getInstance().getTime());
        if (masterReportDataBean.getQuery() != null) {
            rbReport.setReportQuery(masterReportDataBean.getQuery().trim());
        }
        rbReport.setStatus("A");
        rbReport.setJoinAttributes(masterReportDataBean.getJoinAttributes());
        return rbReport;

    }

    private RbReportField convertReportFieldDataBeanToReportFieldModel(RbFieldDataBean fieldDataBean, RbReportField rbReportField) {
//        if (!StringUtils.isEmpty(fieldDataBean.getAlias())) {
        rbReportField.setReportFieldName(fieldDataBean.getAlias());
//        }
        rbReportField.setLastModifiedBy(hkLoginDataBean.getId());
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
        rbReportField.setTableName(fieldDataBean.getTableName());
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
        if (!StringUtils.isEmpty(fieldDataBean.getParentFieldLabel())) {
            showTotalMap.put(RbReportConstantUtils.CustomAttribute.HK_PARENT_FIELD_LABEL, fieldDataBean.getParentFieldLabel());
        } else {
            showTotalMap.put(RbReportConstantUtils.CustomAttribute.HK_PARENT_FIELD_LABEL, null);
        }
        if (fieldDataBean.getIsRule() != null && fieldDataBean.getIsRule()) {
            showTotalMap.put(RbReportConstantUtils.CustomAttribute.IS_RULE, Boolean.TRUE);
        } else {
            showTotalMap.put(RbReportConstantUtils.CustomAttribute.IS_RULE, Boolean.FALSE);
        }
        rbReportField.setCustomAttributes(new Gson().toJson(showTotalMap));
        return rbReportField;
    }

    private RbReportTableDtl convertReportTableDtlDataBeanToReportTableDtlModel(RbReportTableDetailDataBean rbReportTableDetailDataBean, RbReportTableDtl rbReportTableDtl) {
        rbReportTableDtl.setIsArchive(Boolean.FALSE);
        rbReportTableDtl.setLastModifiedBy(hkLoginDataBean.getId());
        rbReportTableDtl.setLastModifiedOn(new Date());
        rbReportTableDtl.setTableName(rbReportTableDetailDataBean.getTableName());
        rbReportTableDtl.setTableSequence(rbReportTableDetailDataBean.getTableSeq());
        return rbReportTableDtl;
    }

    private RbReportTableDetailDataBean convertReportTableDtlModeToReportTableDtllDataBean(RbReportTableDetailDataBean rbReportTableDetailDataBean, RbReportTableDtl rbReportTableDtl) {
        rbReportTableDetailDataBean.setId(rbReportTableDtl.getId());
        rbReportTableDetailDataBean.setTableName(rbReportTableDtl.getTableName());
        rbReportTableDetailDataBean.setTableSeq(rbReportTableDtl.getTableSequence());
        return rbReportTableDetailDataBean;
    }

    public void generateDataForReport(MasterReportDataBean masterReportDataBean, String extension, HttpServletResponse response, HttpServletRequest request) throws GenericDatabaseException, SQLException, JSONException, ParseException, DRException, IOException, FileNotFoundException, JRException {
        String query = masterReportDataBean.getQuery();
        Map<String, Object> columnValue = this.retrieveQueryResults(query, masterReportDataBean, true);
        this.generateReport(masterReportDataBean, columnValue, extension, response, request, true, null, null, null);
    }

//------By Shreya on 05 January 2015 for preparing the data and generating the report.
    public void generateReport(MasterReportDataBean masterReportDataBean, Map<String, Object> columnValue, String extension, HttpServletResponse response, HttpServletRequest request, Boolean toDownload, List<Map<String, Object>> filterAttributes, List<Map<String, Object>> colorAttributes, List<String> hiddenFields) throws FileNotFoundException, DRException, IOException, JRException, GenericDatabaseException, SQLException, JSONException, ParseException {
//        System.out.println("columnValue :" + columnValue);
        //Title to be displayed on the report
        if (masterReportDataBean != null) {
            String title = masterReportDataBean.getReportName();
            //File Name for the report
            String fileName = masterReportDataBean.getReportName();
            List<RbFieldDataBean> rbFieldDataBeansList = masterReportDataBean.getColumns();
            Collections.sort(rbFieldDataBeansList, new Comparator() {
                @Override
                public int compare(Object o1, Object o2) {
                    RbFieldDataBean oldfield = (RbFieldDataBean) o1;
                    RbFieldDataBean newfield = (RbFieldDataBean) o2;
                    return oldfield.getFieldSequence().compareTo(newfield.getFieldSequence());
                }
            });
            Map<String, List<RbFieldDataBean>> mapOfTableAndColumn = new LinkedHashMap<>();
            List<RbReportTableDetailDataBean> tableDtls = masterReportDataBean.getTableDtls();
            Collections.sort(tableDtls, new Comparator() {
                @Override
                public int compare(Object o1, Object o2) {
                    RbReportTableDetailDataBean oldfield = (RbReportTableDetailDataBean) o1;
                    RbReportTableDetailDataBean newfield = (RbReportTableDetailDataBean) o2;
                    return oldfield.getTableSeq().compareTo(newfield.getTableSeq());
                }
            });
            if (!CollectionUtils.isEmpty(tableDtls)) {
                for (RbReportTableDetailDataBean tableDtl : tableDtls) {
                    for (RbFieldDataBean fieldDataBean : rbFieldDataBeansList) {
                        String[] tableNames = fieldDataBean.getTableName().split(",");
                        List<String> tableNameList = new ArrayList<String>(Arrays.asList(tableNames));
                        if (tableNameList.contains(tableDtl.getTableName())) {
                            List<RbFieldDataBean> fieldsList = null;
                            if (mapOfTableAndColumn.containsKey(tableDtl.getTableName())) {
                                fieldsList = mapOfTableAndColumn.get(tableDtl.getTableName());
                                if (!CollectionUtils.isEmpty(fieldsList)) {
                                    fieldsList.add(fieldDataBean);
                                }
                            } else {
                                fieldsList = new ArrayList<>();
                                fieldsList.add(fieldDataBean);
                            }
                            mapOfTableAndColumn.put(tableDtl.getTableName(), fieldsList);
                        }
                    }
                }
            }
            System.out.println("mapOfTableAndColumn :" + mapOfTableAndColumn);
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
            Map<String, List<List<Object>>> mapOfTableNameRecords = new LinkedHashMap<>();
            List<String> columnList = new LinkedList<>();
            List<SubreportBuilder> subReportComponentBuilderList = new ArrayList<>();

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
            //  Map<Long, HkCurrencyEntity> currencyIdEntityMap = this.retrieveCurrencyByIds(associatedCurrencyList);
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
                System.out.println("finalResultList :" + finalResultList);
                List<Map<String, Object>> resultList = new LinkedList<>(finalResultList);
                if (!CollectionUtils.isEmpty(resultList)) {
                    Map<String, Object> newRowMap = new LinkedHashMap<>();
                    Map<String, Object> mapValue = resultList.get(0);
                    if (!CollectionUtils.isEmpty(mapValue)) {
                        for (Map.Entry<String, Object> entry : mapValue.entrySet()) {
                            String columnName = entry.getKey();
                            columnList.add(columnName);
                        }
                    }
                    if (!CollectionUtils.isEmpty(groupByFieldMap)) {
                        Boolean isTotalRowRequired = Boolean.FALSE;
                        if (!CollectionUtils.isEmpty(masterReportDataBean.getColumns())) {
                            for (RbFieldDataBean rbFieldDataBean : masterReportDataBean.getColumns()) {
                                if (rbFieldDataBean.getShowTotal()) {
                                    isTotalRowRequired = Boolean.TRUE;
                                }
                            }
                        }
                        if (isTotalRowRequired) {
                            Map<String, Object> firstValueList = resultList.get(0);
                            if (!CollectionUtils.isEmpty(firstValueList)) {
                                for (Map.Entry<String, Object> entry : firstValueList.entrySet()) {
                                    String column = entry.getKey();
                                    Object value = entry.getValue();
                                    for (RbFieldDataBean fieldDataBean : masterReportDataBean.getColumns()) {
                                        char lastCharacter = column.charAt(column.length() - 1);
                                        if ((fieldDataBean.getAlias().equals(column)) || (lastCharacter == '1' && isContain(fieldDataBean.getAlias(), column.substring(0, column.length() - 1)))) {
                                            String dataType = fieldDataBean.getDataType();
                                            if (dataType.equals(HkSystemConstantUtil.CustomField.DataType.INT8)) {
                                                if (((fieldDataBean.getAlias() + "1").equals(column))) {
                                                    if (value != null && value != "") {
                                                        newRowMap.put(fieldDataBean.getAlias() + "1", Long.parseLong(value.toString()));
                                                    } else {
                                                        newRowMap.put(fieldDataBean.getAlias() + "1", 0l);
                                                    }
                                                } else {
                                                    if (value != null && value != "") {
                                                        newRowMap.put(fieldDataBean.getAlias(), Long.parseLong(value.toString()));
                                                    } else {
                                                        newRowMap.put(fieldDataBean.getAlias(), 0l);
                                                    }
                                                }
                                            } else if (dataType.equals(HkSystemConstantUtil.CustomField.DataType.VARCHAR) || dataType.equals(HkSystemConstantUtil.CustomField.DataType.BOOLEAN)
                                                    || fieldDataBean.getComponentType().equals(HkSystemConstantUtil.CustomField.ComponentType.CURRENCY)) {
                                                if (((fieldDataBean.getAlias() + "1").equals(column))) {
                                                    newRowMap.put(fieldDataBean.getAlias() + "1", "");
                                                } else {
                                                    newRowMap.put(fieldDataBean.getAlias(), "");
                                                }
                                            } else if (dataType.equals(HkSystemConstantUtil.CustomField.DataType.TIMESTAMP)) {
                                                if (((fieldDataBean.getAlias() + "1").equals(column))) {
                                                    newRowMap.put(fieldDataBean.getAlias() + "1", "");
                                                } else {
                                                    newRowMap.put(fieldDataBean.getAlias(), "");
                                                }
                                            } else if (dataType.equals(HkSystemConstantUtil.CustomField.DataType.DOUBLE)) {
                                                if (((fieldDataBean.getAlias() + "1").equals(column))) {
                                                    if (value != null && value != "") {
                                                        newRowMap.put(fieldDataBean.getAlias() + "1", Double.parseDouble(value.toString()));
                                                    } else {
                                                        newRowMap.put(fieldDataBean.getAlias() + "1", 0d);
                                                    }
                                                } else {
                                                    if (value != null && value != "") {
                                                        newRowMap.put(fieldDataBean.getAlias(), Double.parseDouble(value.toString()));
                                                    } else {
                                                        newRowMap.put(fieldDataBean.getAlias(), 0d);
                                                    }

                                                }
                                            } else {
                                                if (((fieldDataBean.getAlias() + "1").equals(column))) {
                                                    newRowMap.put(fieldDataBean.getAlias() + "1", "");
                                                } else {
                                                    newRowMap.put(fieldDataBean.getAlias(), "");
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            System.out.println("newRowMap :" + newRowMap);
                            for (int i = 1; i < resultList.size(); i++) {
                                Map<String, Object> results = resultList.get(i);
                                for (Map.Entry<String, Object> entry : results.entrySet()) {
                                    String key = entry.getKey();
                                    Object value = entry.getValue();
                                    if (newRowMap.containsKey(key) && newRowMap.get(key) != null) {
                                        if (newRowMap.get(key).getClass().equals(Long.class)) {
                                            Long resultValue = Long.parseLong(newRowMap.get(key).toString());
                                            if (value != "" && value != null) {
                                                resultValue = resultValue + Long.parseLong(value.toString());
                                                newRowMap.put(key, resultValue);
                                            }

                                        } else if (newRowMap.get(key).getClass().equals(Double.class)) {
                                            Double resultValue = Double.parseDouble(newRowMap.get(key).toString());
                                            if (value != "" && value != null) {
                                                resultValue = resultValue + Double.parseDouble(value.toString());
                                                newRowMap.put(key, resultValue);
                                            }

                                        }
                                    }
                                }
                            }
                        }
                        System.out.println("newRowMap after :" + newRowMap);
                        for (Map.Entry<String, Object> entrySet : newRowMap.entrySet()) {
                            String key = entrySet.getKey();
                            Object value = entrySet.getValue();
                            for (RbFieldDataBean fieldDataBean : masterReportDataBean.getColumns()) {
                                char lastCharacter = key.charAt(key.length() - 1);
                                if ((fieldDataBean.getAlias().equals(key)) || (lastCharacter == '1' && isContain(fieldDataBean.getAlias(), key.substring(0, key.length() - 1)))) {
                                    if (fieldDataBean.getShowTotal()) {
                                        if (value.getClass().equals(Long.class)) {
                                            newRowMap.put(key, key + " Total = " + value.toString());
                                        } else if (newRowMap.get(key).getClass().equals(Double.class)) {
                                            newRowMap.put(key, key + " Total = " + value.toString());
                                        }
                                    } else {
                                        newRowMap.put(key, "");
                                    }

                                }
                            }

                        }
                        Object rowRecord = null;
                        for (Map<String, Object> results : resultList) {
                            List<Object> row = new LinkedList<>();
                            for (Map.Entry<String, Object> entry : results.entrySet()) {
                                String column = entry.getKey();
                                if (!CollectionUtils.isEmpty(masterReportDataBean.getColumns())) {
                                    for (RbFieldDataBean fieldDataBean : masterReportDataBean.getColumns()) {
                                        char lastCharacter = column.charAt(column.length() - 1);
                                        if ((fieldDataBean.getAlias().equals(column)) || (lastCharacter == '1' && isContain(fieldDataBean.getAlias(), column.substring(0, column.length() - 1)))) {
                                            String dataType = fieldDataBean.getDataType();
                                            if (dataType.equals(HkSystemConstantUtil.CustomField.DataType.INT8)) {
                                                if (entry.getValue() != null) {
                                                    rowRecord = entry.getValue().toString();
                                                } else {
                                                    rowRecord = null;
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
                                                    rowRecord = entry.getValue().toString();
                                                } else {
                                                    rowRecord = null;
                                                }

                                            } else {
                                                if (entry.getValue() != null) {
                                                    rowRecord = entry.getValue().toString();
                                                } else {
                                                    rowRecord = null;
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
                        if (!CollectionUtils.isEmpty(newRowMap)) {
                            records.add(new LinkedList<>(newRowMap.values()));
                        }
                    } else {
                        for (Map.Entry<String, List<RbFieldDataBean>> entrySet : mapOfTableAndColumn.entrySet()) {
                            Object rowRecord = null;
                            String tableName = entrySet.getKey();
                            List<RbFieldDataBean> value = entrySet.getValue();
                            records = new LinkedList<>();
                            for (Map<String, Object> results : resultList) {
                                List<Object> row = new LinkedList<>();
                                for (Map.Entry<String, Object> entry : results.entrySet()) {
                                    String column = entry.getKey();
                                    if (!CollectionUtils.isEmpty(value)) {
                                        for (RbFieldDataBean fieldDataBean : value) {
                                            char lastCharacter = column.charAt(column.length() - 1);
                                            if ((fieldDataBean.getAlias().equals(column)) || (lastCharacter == '1' && isContain(fieldDataBean.getAlias(), column.substring(0, column.length() - 1)))) {
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
                                                row.add(rowRecord);
                                            }
                                        }
                                    }
                                }
                                System.out.println("row :" + row);
                                records.add(row);
                            }
                            mapOfTableNameRecords.put(tableName, records);
                        }
                    }

                }
            }
            List<String> dataBaseColumns = new LinkedList<>();
            List<ColumnGridComponentBuilder> gridBuilder = new LinkedList<>();
            Type collectionType = new TypeToken<Map<String, Object>>() {
            }.getType();
            Map<String, Object> groupByColumnMap = new Gson().fromJson(masterReportDataBean.getGroupAttributes(), collectionType);
            System.out.println("groupByColumnMap :" + groupByColumnMap);
            if (!CollectionUtils.isEmpty(groupByColumnMap) && groupByColumnMap.get("groups") != null && groupByColumnMap.get("groupBy") != null) {
                System.out.println("inside groupby column map");
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
//                gridBuilder.add(column);
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
                if (!CollectionUtils.isEmpty(groupsList)) {
                    System.out.println("groupslist :::" + groupsList);
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
                            if ((tempList1.equals(fieldDataBean.getAlias().toLowerCase().replaceAll("\\s+", "")))) {
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
                    System.out.println("insdie else");
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
                System.out.println("columnList :" + columnList);
                if (!CollectionUtils.isEmpty(columnList) && !CollectionUtils.isEmpty(groupByColumnMap)) {
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
                                            String alias = fieldDataBean.getAlias();
                                            if (alias.contains("$")) {
                                                alias = alias.replaceAll("\\$", "@dollar@");
                                            }
                                            dataBaseColumns.add(alias.toLowerCase().replaceAll("\\s+", ""));
                                        }
                                    } else if ((lastCharacter == '1' && isContain(fieldDataBean.getAlias(), columns.substring(0, columns.length() - 1)))) {
                                        if (!StringUtils.isEmpty(fieldDataBean.getAlias())) {
                                            String alias = fieldDataBean.getAlias();
                                            if (alias.contains("$")) {
                                                alias = alias.replaceAll("\\$", "@dollar@");
                                            }
                                            dataBaseColumns.add(alias.toLowerCase().replaceAll("\\s+", "") + "1");
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
                                                    String alias = fieldDataBean.getAlias();
                                                    if (alias.contains("$")) {
                                                        alias = alias.replaceAll("\\$", "@dollar@");
                                                    }
                                                    TextColumnBuilder<String> column1 = col.column(colTitle, alias.toLowerCase().replaceAll("\\s+", ""), type.stringType()).setStyle(contentRightAlignStyle)
                                                            .setTitleStyle(stl.style(rootStyle).setVerticalAlignment(VerticalAlignment.MIDDLE)
                                                                    .setBorder(stl.penThin()).setFontSize(8)
                                                                    .setHorizontalAlignment(HorizontalAlignment.CENTER)
                                                                    .setBackgroundColor(new Color(177, 154, 204))
                                                                    .bold());
                                                    column1.setStyle(myStyle).setHorizontalAlignment(HorizontalAlignment.RIGHT);
                                                    textColumnBuildersList.add(column1);
                                                }
                                            } else if (fieldDataBean.getDataType().equals(HkSystemConstantUtil.CustomField.DataType.VARCHAR)
                                                    || fieldDataBean.getDataType().equals(HkSystemConstantUtil.CustomField.DataType.BOOLEAN)
                                                    || fieldDataBean.getComponentType().equals(HkSystemConstantUtil.CustomField.ComponentType.CURRENCY)) {
                                                TextColumnBuilder<String> column1;
                                                if (!StringUtils.isEmpty(fieldDataBean.getAlias())) {
                                                    String alias = fieldDataBean.getAlias();
                                                    if (alias.contains("$")) {
                                                        alias = alias.replaceAll("\\$", "@dollar@");
                                                    }
                                                    column1 = col.column(colTitle, alias.toLowerCase().replaceAll("\\s+", ""), type.stringType())
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
                                                    String alias = fieldDataBean.getAlias();
                                                    if (alias.contains("$")) {
                                                        alias = alias.replaceAll("\\$", "@dollar@");
                                                    }
                                                    TextColumnBuilder<String> column1 = col.column(colTitle, alias.toLowerCase().replaceAll("\\s+", ""), type.stringType())
                                                            .setTitleStyle(stl.style(rootStyle).setVerticalAlignment(VerticalAlignment.MIDDLE)
                                                                    .setBorder(stl.penThin()).setFontSize(8)
                                                                    .setHorizontalAlignment(HorizontalAlignment.CENTER)
                                                                    .setBackgroundColor(new Color(177, 154, 204))
                                                                    .bold());
                                                    column1.setStyle(myStyle);
                                                    textColumnBuildersList.add(column1);
                                                }
                                            } else if (fieldDataBean.getDataType().equals(HkSystemConstantUtil.CustomField.DataType.DOUBLE)) {
                                                TextColumnBuilder<String> column1;
                                                if (fieldDataBean.getComponentType().equals(HkSystemConstantUtil.CustomField.ComponentType.PERCENT)) {
                                                    if (!StringUtils.isEmpty(fieldDataBean.getAlias())) {
                                                        String alias = fieldDataBean.getAlias();
                                                        if (alias.contains("$")) {
                                                            alias = alias.replaceAll("\\$", "@dollar@");
                                                        }
                                                        column1 = col.column(colTitle, alias.toLowerCase().replaceAll("\\s+", ""), type.stringType()).setValueFormatter(ReportBuilderTemplate.createStringValueFormatter("%")).setStyle(contentRightAlignStyle)
                                                                .setTitleStyle(stl.style(rootStyle).setVerticalAlignment(VerticalAlignment.MIDDLE)
                                                                        .setBorder(stl.penThin()).setFontSize(8)
                                                                        .setHorizontalAlignment(HorizontalAlignment.CENTER)
                                                                        .setBackgroundColor(new Color(177, 154, 204))
                                                                        .bold());
                                                        column1.setStyle(myStyle).setHorizontalAlignment(HorizontalAlignment.RIGHT);
                                                        textColumnBuildersList.add(column1);
                                                    }
                                                } else if (fieldDataBean.getComponentType().equals(HkSystemConstantUtil.CustomField.ComponentType.ANGLE)) {
                                                    if (!StringUtils.isEmpty(fieldDataBean.getAlias())) {
                                                        String alias = fieldDataBean.getAlias();
                                                        if (alias.contains("$")) {
                                                            alias = alias.replaceAll("\\$", "@dollar@");
                                                        }
                                                        column1 = col.column(colTitle, alias.toLowerCase().replaceAll("\\s+", ""), type.stringType()).setValueFormatter(ReportBuilderTemplate.createStringValueFormatter("°")).setStyle(contentRightAlignStyle)
                                                                .setTitleStyle(stl.style(rootStyle).setVerticalAlignment(VerticalAlignment.MIDDLE)
                                                                        .setBorder(stl.penThin()).setFontSize(8)
                                                                        .setHorizontalAlignment(HorizontalAlignment.CENTER)
                                                                        .setBackgroundColor(new Color(177, 154, 204))
                                                                        .bold());
                                                        column1.setStyle(myStyle).setHorizontalAlignment(HorizontalAlignment.RIGHT);
                                                        textColumnBuildersList.add(column1);
                                                    }
                                                } else if (fieldDataBean.getComponentType().equals(HkSystemConstantUtil.CustomField.ComponentType.CURRENCY)) {
                                                    String label = "";
                                                    String pattern = "";
                                                    if (!StringUtils.isEmpty(fieldDataBean.getAlias())) {
                                                        String alias = fieldDataBean.getAlias();
                                                        if (alias.contains("$")) {
                                                            alias = alias.replaceAll("\\$", "@dollar@");
                                                        }
                                                        column1 = col.column(colTitle, alias.toLowerCase().replaceAll("\\s+", ""), type.stringType()).setValueFormatter(ReportBuilderTemplate.createStringCurrencyValueFormatter(label, pattern)).setStyle(contentRightAlignStyle)
                                                                .setTitleStyle(stl.style(rootStyle).setVerticalAlignment(VerticalAlignment.MIDDLE)
                                                                        .setBorder(stl.penThin()).setFontSize(8)
                                                                        .setHorizontalAlignment(HorizontalAlignment.CENTER)
                                                                        .setBackgroundColor(new Color(177, 154, 204))
                                                                        .bold());
                                                        column1.setStyle(myStyle).setHorizontalAlignment(HorizontalAlignment.RIGHT);
                                                        textColumnBuildersList.add(column1);
                                                    }

                                                } else {
                                                    if (!StringUtils.isEmpty(fieldDataBean.getAlias())) {
                                                        String alias = fieldDataBean.getAlias();
                                                        if (alias.contains("$")) {
                                                            alias = alias.replaceAll("\\$", "@dollar@");
                                                        }
                                                        column1 = col.column(colTitle, alias.toLowerCase().replaceAll("\\s+", ""), type.stringType()).setStyle(contentRightAlignStyle)
                                                                .setTitleStyle(stl.style(rootStyle).setVerticalAlignment(VerticalAlignment.MIDDLE)
                                                                        .setBorder(stl.penThin()).setFontSize(8)
                                                                        .setHorizontalAlignment(HorizontalAlignment.CENTER)
                                                                        .setBackgroundColor(new Color(177, 154, 204))
                                                                        .bold());
                                                        column1.setStyle(myStyle).setHorizontalAlignment(HorizontalAlignment.RIGHT);
                                                        textColumnBuildersList.add(column1);
                                                    }
                                                }

                                            } else {
                                                if (!StringUtils.isEmpty(fieldDataBean.getAlias())) {
                                                    String alias = fieldDataBean.getAlias();
                                                    if (alias.contains("$")) {
                                                        alias = alias.replaceAll("\\$", "@dollar@");
                                                    }
                                                    TextColumnBuilder<String> column1 = col.column(colTitle, alias.toLowerCase().replaceAll("\\s+", ""), type.stringType())
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
                                                        TextColumnBuilder<String> column1 = col.column(colTitle, fieldDataBean.getAlias().toLowerCase().replaceAll("\\s+", ""), type.stringType()).setStyle(contentRightAlignStyle)
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
                                                        TextColumnBuilder<String> column1 = col.column(colTitle, fieldDataBean.getEditedFieldLabel(), type.stringType()).setStyle(contentRightAlignStyle)
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

                if (!CollectionUtils.isEmpty(columnList) && CollectionUtils.isEmpty(groupByColumnMap)) {
                    System.out.println("columnList :" + columnList);
                    System.out.println("mapOfTableAndColumn :" + mapOfTableAndColumn);
                    if (!CollectionUtils.isEmpty(mapOfTableAndColumn)) {
                        for (Map.Entry<String, List<RbFieldDataBean>> entrySet : mapOfTableAndColumn.entrySet()) {
                            dataBaseColumns = new LinkedList<>();
                            textColumnBuildersList = new LinkedList<>();
                            String key = entrySet.getKey();
                            List<RbFieldDataBean> fieldColumns = entrySet.getValue();
                            if (!CollectionUtils.isEmpty(fieldColumns)) {
                                for (RbFieldDataBean fieldDataBean : fieldColumns) {
                                    int oldSeqCount = 0;
                                    int newSeqCount = 0;
                                    for (String columns : columnList) {
                                        String colTitle = "";
                                        char lastCharacter = columns.charAt(columns.length() - 1);
                                        if ((columns.equals(fieldDataBean.getAlias())) || (lastCharacter == '1' && isContain(fieldDataBean.getAlias(), columns.substring(0, columns.length() - 1)))) {
                                            if (columns.equals(fieldDataBean.getAlias())) {
                                                if (!StringUtils.isEmpty(fieldDataBean.getAlias())) {
                                                    String alias = fieldDataBean.getAlias();
                                                    if (alias.contains("$")) {
                                                        alias = alias.replaceAll("\\$", "@dollar@");
                                                    }
                                                    dataBaseColumns.add(alias.toLowerCase().replaceAll("\\s+", ""));
                                                }
                                            } else if ((lastCharacter == '1' && isContain(fieldDataBean.getAlias(), columns.substring(0, columns.length() - 1)))) {
                                                if (!StringUtils.isEmpty(fieldDataBean.getAlias())) {
                                                    String alias = fieldDataBean.getAlias();
                                                    if (alias.contains("$")) {
                                                        alias = alias.replaceAll("\\$", "@dollar@");
                                                    }
                                                    dataBaseColumns.add(alias.toLowerCase().replaceAll("\\s+", "") + "1");
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
                                                            String alias = fieldDataBean.getAlias();
                                                            if (alias.contains("$")) {
                                                                alias = alias.replaceAll("\\$", "@dollar@");
                                                            }
                                                            TextColumnBuilder<Long> column1 = col.column(colTitle, alias.toLowerCase().replaceAll("\\s+", ""), type.longType()).setStyle(contentRightAlignStyle)
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
                                                                String alias = fieldDataBean.getAlias();
                                                                if (alias.contains("$")) {
                                                                    alias = alias.replaceAll("\\$", "@dollar@");
                                                                }
                                                                column1 = col.column(colTitle, alias.toLowerCase().replaceAll("\\s+", ""), type.doubleType()).setStyle(contentRightAlignStyle)
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
                                System.out.println("dataBaseColumns :" + dataBaseColumns);
                                SubreportBuilder subreport = cmp.subreport(new SubreportExpression(key, textColumnBuildersList, subTotalColumnBuilerList))
                                        .setDataSource(createDataSource(mapOfTableNameRecords.get(key), dataBaseColumns));
                                subReportComponentBuilderList.add(subreport);
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
                                    TextColumnBuilder<String> column1 = col.column(colTitle, colName, type.stringType()).setStyle(contentRightAlignStyle);
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
            System.out.println("dataBaseColumns :" + dataBaseColumns);
            Boolean groupReport = Boolean.FALSE;
            if (!CollectionUtils.isEmpty(groupByColumnMap)) {
                groupReport = Boolean.TRUE;
                createReport(response, request, fileName, filterAttributes, title, extension, createDataSource(records, dataBaseColumns), finalResultList, hiddenFields,
                        subTotalColumnBuilerList, gridBuilder, columnGrpBuilderList, subReportComponentBuilderList, groupReport, textColumnBuildersList.toArray(new TextColumnBuilder[textColumnBuildersList.size()]));
            } else {
                createReport(response, request, fileName, filterAttributes, title, extension, null, finalResultList, hiddenFields,
                        subTotalColumnBuilerList, gridBuilder, columnGrpBuilderList, subReportComponentBuilderList, groupReport, textColumnBuildersList.toArray(new TextColumnBuilder[textColumnBuildersList.size()]));
            }
            if (toDownload) {
                downloadPdf(fileName, extension, request, response);
            }

        }

    }
    //------By Shreya on 05 January 2015 for creating data source for the report..

    private JRDataSource createDataSource(List<List<Object>> records, List<String> columnNamesList) {
        String columnsNames = columnNamesList.toString().replaceAll("\\[|\\]", "").replaceAll(" ", "");
        System.out.println("columnsNames :" + columnsNames);
        System.out.println("columnsNames size :" + columnsNames.length());
        System.out.println("records :" + records);
        DRDataSource dataSource = new DRDataSource(columnsNames.split(","));
        List<Object> rowList;
        for (List<Object> row : records) {
            rowList = new LinkedList<>();
            for (Object fieldValue : row) {
                if (fieldValue != null) {
                    rowList.add(fieldValue);
                } else {
                    rowList.add("N/A");
                }
            }
            dataSource.add(rowList.toArray());
        }
        return dataSource;
    }

    //-----By Shreya on 21st Oct for creating empty data source in case of subreport..-----------    
    private JRDataSource createParentDataSource() {
        return new JREmptyDataSource(1);
    }

    //------By Shreya on 05 January 2015 for creating the pdf or excel report
    private void createReport(HttpServletResponse response, HttpServletRequest request, String fileName, List<Map<String, Object>> filterAttributes, String title, String extension, JRDataSource dataSource, List<Map<String, Object>> resultMap, List<String> hiddenFields, List<AggregationSubtotalBuilder> subTotalColumnBuilerList, List<ColumnGridComponentBuilder> gridBuilder, List<ColumnGroupBuilder> columnGroupBuilderList, List<SubreportBuilder> subReportComponentBuilderList, Boolean groupReport, TextColumnBuilder<?>... columns) throws FileNotFoundException, DRException, IOException, JRException {
        AggregationSubtotalBuilder[] subTotals = subTotalColumnBuilerList.toArray(new AggregationSubtotalBuilder[subTotalColumnBuilerList.size()]);
        ColumnGridComponentBuilder[] gridBuilders = gridBuilder.toArray(new ColumnGridComponentBuilder[gridBuilder.size()]);
        ColumnGroupBuilder[] groupBuilders = columnGroupBuilderList.toArray(new ColumnGroupBuilder[columnGroupBuilderList.size()]);
        SubreportBuilder[] subReportBuilders = subReportComponentBuilderList.toArray(new SubreportBuilder[subReportComponentBuilderList.size()]);
        JasperReportBuilder report = report();
        report.setTemplate(ReportBuilderTemplate.reportTemplate);
        if (groupReport) {
            report.columns(columns)
                    .setDataSource(dataSource);
//            if (!CollectionUtils.isEmpty(subTotalColumnBuilerList)) {
//                report.subtotalsAtSummary(subTotals);
//            }
            if (!CollectionUtils.isEmpty(columnGroupBuilderList)) {
                report.groupBy(groupBuilders);
            }
            if (!CollectionUtils.isEmpty(filterAttributes)) {
                report.pageHeader(ReportBuilderTemplate.createHeaderComponent(filterAttributes, resultMap, hiddenFields));
            }
            if (!CollectionUtils.isEmpty(gridBuilder)) {
                report.columnGrid(gridBuilders);
            }
        } else {
            report.setDataSource(createParentDataSource())
                    .detail(subReportBuilders);
        }
        report.title(ReportBuilderTemplate.createTitleComponent(null, title, request, "RIGHT", Boolean.TRUE))
                .pageFooter(ReportBuilderTemplate.createFooterComponent())
                //                .groupBy(groupBuilders)
                .setNoDataSplitType(SplitType.PREVENT)
                .setSummarySplitType(SplitType.PREVENT)
                .setDetailSplitType(SplitType.PREVENT);

//        
        File tempDir = checkIsExists(FolderManagement.getBasePath(), TEMP, null);
        StringBuilder tempFilePath = new StringBuilder(tempDir.getPath());
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
    public void downloadPdf(String fileName, String extension, HttpServletRequest request, HttpServletResponse response) {
        File file = null;
        File tempDir = checkIsExists(FolderManagement.getBasePath(), TEMP, null);
        StringBuilder tempFilePath = new StringBuilder(tempDir.getPath());
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

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public List<String> retrieveColumnNamesFromQuery(String query) {
        //  String query = "select t1.f1,t2.f2,max(t3.f3),t4.f4,to_char(t5.f5,'dd mm yyyy',25,25,26,25) from table";
        StringTokenizer totalFields = new StringTokenizer(query.substring(7, query.indexOf("from")), ",");
        List<String> coloumnNameList = new ArrayList();
        String element = "";
        Boolean endBracket = false;
        while (totalFields.hasMoreElements()) {
            element = totalFields.nextElement().toString();
            if (element.toLowerCase().contains("case")) {
                element = element.substring(element.toLowerCase().indexOf("case") + 4, element.toLowerCase().indexOf("when")).trim();
            } else if (element.toLowerCase().contains(" as ")) {
                element = element.substring(0, element.toLowerCase().indexOf(" as ")).trim();
            }
            if (endBracket) {
                if (element.contains(")")) {
                    endBracket = false;
                }
            } else {
                if (element.contains("(") && element.contains(")")) {
                    coloumnNameList.add(element.substring(element.lastIndexOf("(") + 1, element.indexOf(")")));
                } else if (element.contains("(")) {
                    coloumnNameList.add(element.substring(element.lastIndexOf("(") + 1));
                    endBracket = true;
                } else if (!element.contains("'") && !element.contains(")")) {
                    coloumnNameList.add(element);
                }
            }
        }
        return coloumnNameList;
    }

    private MasterReportDataBean convertReportModelToMasterReportDataBean(RbReport report, MasterReportDataBean masterReportDataBean) {
        if (masterReportDataBean == null) {
            masterReportDataBean = new MasterReportDataBean();
        }
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
            List<RbReportTableDetailDataBean> reportTableDetailDataBeans = new ArrayList<>();
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
            if (!CollectionUtils.isEmpty(report.getRbReportTableDtls())) {
                for (RbReportTableDtl rbReportTableDtl : report.getRbReportTableDtls()) {
                    RbReportTableDetailDataBean rbReportTableDetailDataBean = new RbReportTableDetailDataBean();
                    rbReportTableDetailDataBean = convertReportTableDtlModeToReportTableDtllDataBean(rbReportTableDetailDataBean, rbReportTableDtl);
                    reportTableDetailDataBeans.add(rbReportTableDetailDataBean);
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
            masterReportDataBean.setColumns(fieldDataBeanList);
            masterReportDataBean.setTableDtls(reportTableDetailDataBeans);
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
            rbFieldDataBean.setTableName(rbReportField.getTableName());
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
                if (showTotalMap.get(RbReportConstantUtils.CustomAttribute.HK_PARENT_FIELD_LABEL) != null && !StringUtils.isEmpty(showTotalMap.get(RbReportConstantUtils.CustomAttribute.HK_PARENT_FIELD_LABEL))) {
                    rbFieldDataBean.setParentFieldLabel(showTotalMap.get(RbReportConstantUtils.CustomAttribute.HK_PARENT_FIELD_LABEL).toString());
                }

            }
            return rbFieldDataBean;
        }
        return null;
    }

    public String generateQuery(QueryDataBean queryDataBean) {
        String query = null;
        if (queryDataBean != null) {
            if (!queryDataBean.getSelectedTables().isEmpty() && !queryDataBean.getSelectedColumns().isEmpty()) {
                query = "select ";
                int index = 1;
                for (String column : queryDataBean.getSelectedColumns().split(",")) {
                    if (query.indexOf(column.substring(column.indexOf(".") + 1)) > 0) {
                        column = column + " as " + column.replace('.', '_');
                    }
                    if (index == queryDataBean.getSelectedColumns().split(",").length) {
                        query = query + column;
                    } else {
                        query = query + column + ",";
                    }
                    index++;
                }
                query = query + " from " + queryDataBean.getSelectedTables();

                if (!CollectionUtils.isEmpty(queryDataBean.getJoinList())) {
                    query = query + " where ";
                    int joinIndex = 1;
                    for (QueryJoinDataBean joinDataBean : queryDataBean.getJoinList()) {
                        if (joinDataBean.getFirstColumn().equals("") || joinDataBean.getSecondColumn().equals("")) {
                            query = query.substring(0, query.indexOf(" where"));
                        } else {
                            String joinStatement = joinDataBean.getFirstColumn() + joinDataBean.getJoinType() + joinDataBean.getSecondColumn();
                            if (joinIndex == queryDataBean.getJoinList().size()) {
                                query = query + joinStatement;
                            } else {
                                query = query + joinStatement + " and ";
                            }
                        }
                        joinIndex++;
                    }
                }

                if (!CollectionUtils.isEmpty(queryDataBean.getOrderMap())) {
                    query = query + " order by ";
                    int orderIndex = 1;
                    for (QueryOrderByDataBean order : queryDataBean.getOrderMap()) {
                        if (orderIndex == queryDataBean.getOrderMap().size()) {
                            query = query + order.getColumn() + " " + order.getOrderValue();
                        } else {
                            query = query + order.getColumn() + " " + order.getOrderValue() + ",";
                        }
                        orderIndex++;
                    }
                }
                return query;
            }
        }
        return null;
    }

    public String generateQueryBasedOnFeatureFields(MasterReportDataBean masterReportDataBean) {
        String query = null;
        Boolean isNormalSubFormValue = true;
        Map<String, List<RbFieldDataBean>> dbNameFieldMap = new HashMap<>();
        List<RbReportField> sqlReportFields = new LinkedList<>();
        if (masterReportDataBean != null) {
            List<RbFieldDataBean> columns = masterReportDataBean.getColumns();
            if (!CollectionUtils.isEmpty(columns)) {
                for (RbFieldDataBean rbFieldDataBean : columns) {
                    List<RbFieldDataBean> fieldDataBeans = new ArrayList<>();
                    if (!CollectionUtils.isEmpty(dbNameFieldMap)) {
                        if (dbNameFieldMap.containsKey(rbFieldDataBean.getDbBaseName())) {
                            fieldDataBeans = dbNameFieldMap.get(rbFieldDataBean.getDbBaseName());
                        } else {
                            fieldDataBeans = new ArrayList<>();
                        }
                    } else {
                        fieldDataBeans = new ArrayList<>();
                    }
                    fieldDataBeans.add(rbFieldDataBean);
                    dbNameFieldMap.put(rbFieldDataBean.getDbBaseName(), fieldDataBeans);
                    RbReportField reportField = new RbReportField();
                    reportField = convertReportFieldDataBeanToReportFieldModel(rbFieldDataBean, reportField);
                    reportField.setFieldLabel(reportField.getFieldLabel());
                    reportField.setReportFieldName(reportField.getReportFieldName());
                    sqlReportFields.add(reportField);
                }
//                System.out.println("dbNameFieldMap :"+dbNameFieldMap);
                if (dbNameFieldMap.keySet().size() == 1 && dbNameFieldMap.containsKey("subformvalue")) {
                    isNormalSubFormValue = false;
                } else {
                    isNormalSubFormValue = true;
                }

//                sqlReportFields.get(0).setJoinAttributes(null);
//                System.out.println("columns.get(0)--" + columns.get(0));
//                for (RbFieldDataBean rbFieldDataBean : columns) {
//                    if (rbFieldDataBean.getDbBaseType() == null) {
//                        rbFieldDataBean.setDbBaseType("RDB");
//                    }
//                    if (!rbFieldDataBean.getDbBaseType().equals("MDB")) {
//                        String tableName = rbFieldDataBean.getDbBaseName();
//                        String columnName = rbFieldDataBean.getColName();
//                        Integer fieldSequence = rbFieldDataBean.getFieldSequence();
//                        if (tableColumnsMap.get(tableName) == null) {
//                            tableColumnsMap.put(tableName, new HashMap<String, Integer>());
//                        }
//                        tableColumnsMap.get(tableName).put(columnName, fieldSequence);
//                    }
//                }
//
//                for (Map.Entry<String, Map<String, Integer>> entry : tableColumnsMap.entrySet()) {
//                    String tableName = entry.getKey();
//                    Map<String, Integer> columNameList = entry.getValue();
//                    if (!CollectionUtils.isEmpty(columNameList)) {
//                        for (Map.Entry<String, Integer> entry1 : columNameList.entrySet()) {
//                            String columnName = entry1.getKey();
//                            for (RbFieldDataBean rbFieldDataBean : columns) {
//                                if (rbFieldDataBean.getDbBaseName().equals(tableName) && rbFieldDataBean.getColName().equals(columnName)) {
//                                    if (rbFieldDataBean.getOrderType() != null) {
//                                        String columnNameDetail = tableName + "." + columnName;
//                                        if (rbFieldDataBean.getOrderType().equalsIgnoreCase("asc")) {
//                                            orderByColumns.put(columnNameDetail, RbReportConstantUtils.OrderBy.ASC);
//                                        }
//                                        if (rbFieldDataBean.getOrderType().equalsIgnoreCase("desc")) {
//                                            orderByColumns.put(columnNameDetail, RbReportConstantUtils.OrderBy.DESC);
//                                        }
//                                    }
//                                }
//                            }
//
//                        }
//                    }
//                }
//                System.out.println("flag ---"+isNormalSubFormValue);
                Boolean visibilityStatus = this.retrieveCurrencyConfiguration();
                Boolean viewCurrencyDataPermission = this.retrieveViewCurrencyDataRightsOfLoggedInUser();
                query = QueryStringUtils.retrieveSQLQuery(sqlReportFields, masterReportDataBean.getJoinAttributes(), visibilityStatus, isNormalSubFormValue, viewCurrencyDataPermission);
            }
        }

        return query;
    }

    public FieldDataBean convertFieldEntityToFieldDataBean(HkFieldEntity fieldEntity, FieldDataBean fieldDataBean) {
        fieldDataBean.setComponentType(fieldEntity.getComponentType());
        fieldDataBean.setDbFieldName(fieldEntity.getDbFieldName());
        fieldDataBean.setFieldLabel(fieldEntity.getFieldLabel());
        fieldDataBean.setEditedFieldLabel(fieldEntity.getFieldLabel().toLowerCase().replaceAll("\\s+", ""));
        fieldDataBean.setFieldType(fieldEntity.getFieldType());
        fieldDataBean.setId(fieldEntity.getId());
        fieldDataBean.setUiFieldName(fieldEntity.getUiFieldName());
        fieldDataBean.setValidationPattern(fieldEntity.getValidationPattern());
        fieldDataBean.setDbBaseName(fieldEntity.getDbBaseName());
        fieldDataBean.setDbBaseType(fieldEntity.getDbBaseType());
        fieldDataBean.setFeature(fieldEntity.getFeature());
        fieldDataBean.setAssociatedCurrency(fieldEntity.getAssociatedCurrency());
        fieldDataBean.setIsSubFormValue(Boolean.FALSE);
        return fieldDataBean;
    }
//------By Shreya on 13 January 2015 for ui-select of feature and fields

    public Map<String, List<FieldDataBean>> retrieveFieldsWithFeatureName() throws GenericDatabaseException {
        Map<String, List<FieldDataBean>> featureFieldMap = null;
        List<HkFieldEntity> fieldEntitys = fieldService.retrieveAllFieldsByCompanyId(hkLoginDataBean.getCompanyId());
        List<Long> featureIds = new ArrayList<>();
        if (!CollectionUtils.isEmpty(fieldEntitys)) {
            for (HkFieldEntity hkFieldEntity : fieldEntitys) {
                featureIds.add(hkFieldEntity.getFeature());
            }
        }
        List<FieldDataBean> fieldList = null;
        Map<Long, String> featureIdNameMap = userManagementServiceWrapper.retrieveFeatureNamesByIds(featureIds, false);
        if (!CollectionUtils.isEmpty(featureIdNameMap)) {
            for (HkFieldEntity fieldEntity : fieldEntitys) {
                if (featureIdNameMap.containsKey(fieldEntity.getFeature())) {
                    FieldDataBean fieldDataBean = new FieldDataBean();
                    fieldDataBean = convertFieldEntityToFieldDataBean(fieldEntity, fieldDataBean);
                    String featureName = featureIdNameMap.get(fieldEntity.getFeature());
                    if (!CollectionUtils.isEmpty(featureFieldMap)) {
                        if (featureFieldMap.containsKey(featureName)) {
                            featureFieldMap.get(featureName).add(fieldDataBean);
                        } else {
                            fieldList = new ArrayList<>();
                            fieldList.add(fieldDataBean);
                        }
                    } else {
                        featureFieldMap = new HashMap<>();
                        fieldList = new ArrayList<>();
                        fieldList.add(fieldDataBean);
                    }
                    featureFieldMap.put(featureName, fieldList);

                }
            }
        }
        return featureFieldMap;

    }

    public FieldDataBean convertSubFieldEntityToFieldDataBean(HkSubFormFieldEntity subFormFieldEntity, FieldDataBean fieldDataBean) {
        fieldDataBean.setComponentType(subFormFieldEntity.getComponentType());
        fieldDataBean.setDbBaseName(subFormFieldEntity.getParentField().getDbBaseName());
        fieldDataBean.setDbBaseType(HkSystemConstantUtil.DBBaseType.MONGO_DB);
        fieldDataBean.setDbFieldName(subFormFieldEntity.getSubFieldName() + ":" + subFormFieldEntity.getParentField().getId());
        fieldDataBean.setEditedFieldLabel(subFormFieldEntity.getSubFieldLabel().toLowerCase().replaceAll("\\s+", ""));
        fieldDataBean.setFieldLabel(subFormFieldEntity.getParentField().getFieldLabel() + "-" + subFormFieldEntity.getSubFieldLabel());
        fieldDataBean.setFeature(subFormFieldEntity.getParentField().getFeature());
        fieldDataBean.setFieldSequence(subFormFieldEntity.getSequenceNo());
        fieldDataBean.setFieldType(subFormFieldEntity.getSubFieldType());
        fieldDataBean.setId(subFormFieldEntity.getId());
        fieldDataBean.setIsCustom(Boolean.TRUE);
        fieldDataBean.setUiFieldName(subFormFieldEntity.getSubFieldLabel());
        fieldDataBean.setValidationPattern(subFormFieldEntity.getValidationPattern());
        fieldDataBean.setIsSubFormValue(Boolean.TRUE);
        fieldDataBean.setParentDbFieldName(subFormFieldEntity.getParentField().getDbFieldName());
        fieldDataBean.setParentDbBaseName(subFormFieldEntity.getParentField().getDbBaseName());
        return fieldDataBean;
    }

    public FieldDataBean convertSubFieldEntityAsFeatureToFieldDataBean(HkSubFormFieldEntity subFormFieldEntity, FieldDataBean fieldDataBean) {
        fieldDataBean.setComponentType(subFormFieldEntity.getComponentType());
        fieldDataBean.setDbBaseName("subformvalue");
        fieldDataBean.setDbBaseType(HkSystemConstantUtil.DBBaseType.MONGO_DB);
        fieldDataBean.setDbFieldName(subFormFieldEntity.getSubFieldName() + ":" + subFormFieldEntity.getParentField().getId());
        fieldDataBean.setEditedFieldLabel(subFormFieldEntity.getSubFieldLabel().toLowerCase().replaceAll("\\s+", ""));
        fieldDataBean.setFieldLabel(subFormFieldEntity.getSubFieldLabel());
        fieldDataBean.setFeature(subFormFieldEntity.getParentField().getFeature());
        fieldDataBean.setFieldSequence(subFormFieldEntity.getSequenceNo());
        fieldDataBean.setFieldType(subFormFieldEntity.getSubFieldType());
        fieldDataBean.setId(subFormFieldEntity.getId());
        fieldDataBean.setIsCustom(Boolean.TRUE);
        fieldDataBean.setUiFieldName(subFormFieldEntity.getSubFieldLabel());
        fieldDataBean.setValidationPattern(subFormFieldEntity.getValidationPattern());
        fieldDataBean.setIsSubFormValue(Boolean.TRUE);
        fieldDataBean.setParentDbFieldName(subFormFieldEntity.getParentField().getDbFieldName());
        fieldDataBean.setParentDbBaseName(subFormFieldEntity.getParentField().getDbBaseName());
        //Parent field label is added to identify subentity as feature in edit mode.
        fieldDataBean.setParentFieldLabel(subFormFieldEntity.getParentField().getFieldLabel());
        return fieldDataBean;
    }

    public Map<Long, List<FieldDataBean>> retrieveSubEntityFieldsWithAssociatedSubField(Set<Long> subEntityFeatureIds, Boolean isFeature) throws GenericDatabaseException {
        Map<Long, List<FieldDataBean>> subFieldValueMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(subEntityFeatureIds)) {
            List<HkSubFormFieldEntity> hkSubFormFieldEntitys = fieldService.retrieveSetOfSubEntitiesByListOfFieldIds(new ArrayList(subEntityFeatureIds));
//            System.out.println("hkSubFormFieldEntitys :" + hkSubFormFieldEntitys);
            if (!CollectionUtils.isEmpty(hkSubFormFieldEntitys)) {
                for (HkSubFormFieldEntity hkSubFormFieldEntity : hkSubFormFieldEntitys) {
                    List<FieldDataBean> subFormValueList = null;
                    if (!CollectionUtils.isEmpty(subFieldValueMap)) {
                        if (subFieldValueMap.containsKey(hkSubFormFieldEntity.getParentField().getId())) {
                            subFormValueList = subFieldValueMap.get(hkSubFormFieldEntity.getParentField().getId());
                        } else {
                            subFormValueList = new ArrayList<>();
                        }
                    } else {
                        subFieldValueMap = new HashMap<>();
                        subFormValueList = new ArrayList<>();
                    }
                    FieldDataBean fieldDataBean = new FieldDataBean();
                    if (isFeature) {
                        fieldDataBean = convertSubFieldEntityAsFeatureToFieldDataBean(hkSubFormFieldEntity, fieldDataBean);
                    } else {
                        fieldDataBean = convertSubFieldEntityToFieldDataBean(hkSubFormFieldEntity, fieldDataBean);
                    }
                    subFormValueList.add(fieldDataBean);
                    subFieldValueMap.put(hkSubFormFieldEntity.getParentField().getId(), subFormValueList);
                }
            }
        }
        return subFieldValueMap;
    }

    public Map<Long, String> retrieveFeatureNameByIds(List<Long> featureIds) throws GenericDatabaseException {
        Map<Long, String> featureIdNameMap = new HashMap<>();
        featureIdNameMap = userManagementServiceWrapper.retrieveFeatureNamesByIds(featureIds, false);
        return featureIdNameMap;

    }

    public Long updateReport(MasterReportDataBean masterReportDataBean) throws GenericDatabaseException {

        RbReport rbReport = rbReportService.retriveReportById(masterReportDataBean.getId(), true, null);

        if (masterReportDataBean.getStatus().equals("A")) {
            rbReport = convertMasterReportDataBeanToReportModel(masterReportDataBean, rbReport);
            List<RbReportField> rbReportFieldList = new ArrayList<>();
            List<RbReportTableDtl> rbReportTableDtlList = new ArrayList<>();
            if (masterReportDataBean.isExternalReport() == null) {
                masterReportDataBean.setExternalReport(Boolean.FALSE);
            }
            if (!masterReportDataBean.isExternalReport()) {
                List<RbFieldDataBean> fieldDataBeanList = masterReportDataBean.getColumns();
                List<RbFieldDataBean> existingFields = new ArrayList<>();
                List<RbReportField> fieldEntitySet = rbReport.getRbReportFieldSet();
                List<RbReportTableDetailDataBean> reportTableDataBeanList = masterReportDataBean.getTableDtls();
                System.out.println("reportTableDataBeanList :" + reportTableDataBeanList);
                List<RbReportTableDetailDataBean> reportTableDtlsExistingList = new ArrayList<>();
                List<RbReportTableDtl> reportTableDtlEntitySet = rbReport.getRbReportTableDtls();
                System.out.println("reportTableDtlEntitySet :" + reportTableDtlEntitySet);

                List<String> columnNameList = retrieveColumnNamesFromQuery(masterReportDataBean.getQuery());

                if (fieldDataBeanList != null && !fieldDataBeanList.isEmpty()) {
                    //int count = 0;
                    for (RbFieldDataBean fieldDataBean : fieldDataBeanList) {
                        for (RbReportField rbReportField : fieldEntitySet) {
                            if (Objects.equals(rbReportField.getId(), fieldDataBean.getId())) {
                                rbReportField = convertReportFieldDataBeanToReportFieldModel(fieldDataBean, rbReportField);
                                rbReportField.setDbFieldName(fieldDataBean.getColName());
                                rbReportField.setReport(rbReport);
                                rbReportFieldList.add(rbReportField);
                                existingFields.add(fieldDataBean);
                                //count++;
                            }
                        }
                    }
                    fieldDataBeanList.removeAll(existingFields);
                    for (RbFieldDataBean fieldDataBean : fieldDataBeanList) {
                        RbReportField rbReportField = new RbReportField();
                        rbReportField = convertReportFieldDataBeanToReportFieldModel(fieldDataBean, rbReportField);
                        rbReportField.setDbFieldName(fieldDataBean.getColName());
                        rbReportField.setReport(rbReport);
                        rbReportFieldList.add(rbReportField);
                        //count++;
                    }
                    for (RbReportField rbReportField : fieldEntitySet) {
                        if (!rbReportFieldList.contains(rbReportField)) {
                            rbReportField.setIsArchive(Boolean.TRUE);
                            rbReportFieldList.add(rbReportField);
                        }
                    }
                } else {
                    for (RbReportField rbReportField : fieldEntitySet) {
                        rbReportField.setIsArchive(Boolean.TRUE);
                    }
                }

                if (!CollectionUtils.isEmpty(reportTableDataBeanList)) {
                    for (RbReportTableDetailDataBean rbReportTableDetailDataBean : reportTableDataBeanList) {
                        for (RbReportTableDtl rbReportTableDtl : reportTableDtlEntitySet) {
                            if (Objects.equals(rbReportTableDtl.getId(), rbReportTableDetailDataBean.getId())) {
                                rbReportTableDtl = convertReportTableDtlDataBeanToReportTableDtlModel(rbReportTableDetailDataBean, rbReportTableDtl);
                                rbReportTableDtl.setReport(rbReport);
                                rbReportTableDtlList.add(rbReportTableDtl);
                                reportTableDtlsExistingList.add(rbReportTableDetailDataBean);
                            }
                        }
                    }
                    reportTableDataBeanList.removeAll(reportTableDtlsExistingList);
                    for (RbReportTableDetailDataBean rbReportTableDetailDataBean : reportTableDataBeanList) {
                        RbReportTableDtl rbReportTableDtl = new RbReportTableDtl();
                        rbReportTableDtl = convertReportTableDtlDataBeanToReportTableDtlModel(rbReportTableDetailDataBean, rbReportTableDtl);
                        rbReportTableDtl.setReport(rbReport);
                        rbReportTableDtlList.add(rbReportTableDtl);
                    }
                    for (RbReportTableDtl reportTableDtl : reportTableDtlEntitySet) {
                        if (!rbReportTableDtlList.contains(reportTableDtl)) {
                            reportTableDtl.setIsArchive(Boolean.TRUE);
                            rbReportTableDtlList.add(reportTableDtl);
                        }
                    }
                } else {
                    for (RbReportTableDtl reportTableDtl : reportTableDtlEntitySet) {
                        reportTableDtl.setIsArchive(Boolean.TRUE);
                    }
                }
            }
            rbReport.setRbReportFieldSet(rbReportFieldList);
            rbReport.setRbReportTableDtls(rbReportTableDtlList);
        } else {
            rbReport.setStatus("RM");
            rbReport.setIsArchive(Boolean.TRUE);
            rbReport.setIsActive(Boolean.FALSE);
        }
        reportBuliderServiceWrapper.updateReportAndFeature(rbReport);
//        rbReportService.updateReport(rbReport);
        return rbReport.getId();
    }

    public Map<String, Map<String, List<FieldDataBean>>> retrieveSubEntityFieldsAsFeature(Set<Long> subEntityFeatureIds) throws GenericDatabaseException {
        Map<String, Map<String, List<FieldDataBean>>> subFeatureSectionFieldMap = new HashMap<>();
        Map<Long, List<FieldDataBean>> subEntityFieldMap = new HashMap<>();
        List<HkFieldEntity> hkSubFieldEntitys = fieldService.retrieveFieldsByIds(new ArrayList(subEntityFeatureIds), false);
        subEntityFieldMap = this.retrieveSubEntityFieldsWithAssociatedSubField(subEntityFeatureIds, true);
        if (!CollectionUtils.isEmpty(hkSubFieldEntitys)) {
            for (HkFieldEntity hkSubFieldEntity : hkSubFieldEntitys) {
                Map<String, List<FieldDataBean>> sectionFieldMapData = new HashMap<>();
                if (!CollectionUtils.isEmpty(subEntityFieldMap) && subEntityFieldMap.containsKey(hkSubFieldEntity.getId())) {
                    List<FieldDataBean> fieldDataBeanList = subEntityFieldMap.get(hkSubFieldEntity.getId());
                    sectionFieldMapData.put("General", fieldDataBeanList);
                    subFeatureSectionFieldMap.put(hkSubFieldEntity.getDbBaseName() + "." + hkSubFieldEntity.getFieldLabel(), sectionFieldMapData);
                }

            }
        }
        return subFeatureSectionFieldMap;
    }

    public Map<String, Map<String, List<FieldDataBean>>> retrieveFeatureSectionFieldMap() throws GenericDatabaseException {
        List<HkFieldEntity> fieldEntitys = fieldService.retrieveAllFieldsByCompanyId(hkLoginDataBean.getCompanyId());
        Set<Long> featureIds = new HashSet<>();
        Map<String, Map<String, List<FieldDataBean>>> subFeatureSectionFieldMap = new HashMap<>();
        Set<Long> subEntityFeatureIds = new HashSet<>();
        Set<Long> pointerIds = new HashSet<>();
        Map<Long, HkFieldEntity> pointerParentFieldMap = new HashMap<>();
        Map<Long, List<FieldDataBean>> subEntityFieldMap = new HashMap<>();
        Map<String, String> fieldsDbFieldNameToID = new HashMap<>();
        List<String> pricelistConstants = new ArrayList<>();
        List<String> invoiceConstants = new ArrayList<>();
        invoiceConstants.add(HkSystemConstantUtil.InvoiceStaticFields.TYPE_OF_INVOICE);
        pricelistConstants.add(HkSystemConstantUtil.PlanStaticFieldName.CLARITY);
        pricelistConstants.add(HkSystemConstantUtil.PlanStaticFieldName.COLOR);
        pricelistConstants.add(HkSystemConstantUtil.PlanStaticFieldName.CUT);
        pricelistConstants.add(HkSystemConstantUtil.PlanStaticFieldName.FLUROSCENE);
        if (!CollectionUtils.isEmpty(fieldEntitys)) {
            for (HkFieldEntity hkFieldEntity : fieldEntitys) {
                if (hkFieldEntity.getComponentType().equals(HkSystemConstantUtil.CustomField.ComponentType.SUBENTITY)) {
                    subEntityFeatureIds.add(hkFieldEntity.getId());
                }
                if (hkFieldEntity.getComponentType().equals(HkSystemConstantUtil.CustomField.ComponentType.POINTER)) {
                    Type type = new TypeToken<Map<String, Object>>() {
                    }.getType();
                    Map<String, Object> validationPatternMap = (new Gson()).fromJson(hkFieldEntity.getValidationPattern().trim(), type);
                    if (!CollectionUtils.isEmpty(validationPatternMap) && validationPatternMap.containsKey("pointer")) {
                        pointerIds.add(Long.parseLong(validationPatternMap.get("pointer").toString()));
                    }
                }
                if (hkFieldEntity.getDbBaseName().equals("plan") && pricelistConstants.contains(hkFieldEntity.getDbFieldName())) {
                    fieldsDbFieldNameToID.put(hkFieldEntity.getDbFieldName(), hkFieldEntity.getId().toString());
                }
                if (hkFieldEntity.getDbBaseName().equals("invoice") && invoiceConstants.contains(hkFieldEntity.getDbFieldName())) {
                    fieldsDbFieldNameToID.put(hkFieldEntity.getDbFieldName(), hkFieldEntity.getId().toString());
                }
                featureIds.add(hkFieldEntity.getFeature());
            }
        }
        if (!CollectionUtils.isEmpty(subEntityFeatureIds)) {
//            System.out.println("subEntityFeatureIds :" + subEntityFeatureIds);
            subEntityFieldMap = this.retrieveSubEntityFieldsWithAssociatedSubField(subEntityFeatureIds, false);
            subFeatureSectionFieldMap = this.retrieveSubEntityFieldsAsFeature(subEntityFeatureIds);
        }

        if (!CollectionUtils.isEmpty(pointerIds)) {
            List<HkFieldEntity> fieldEntityList = fieldService.retrieveFieldsByIds(new ArrayList<>(pointerIds), false);
            if (!CollectionUtils.isEmpty(fieldEntityList)) {
                for (HkFieldEntity entity : fieldEntityList) {
                    pointerParentFieldMap.put(entity.getId(), entity);
                }
            }
        }

//        System.out.println("subEntityFieldMap :" + subEntityFieldMap);
        Map<String, Map<String, List<FieldDataBean>>> featureSectionFieldMapData = new HashMap<>();

        Map<Long, String> featureIdNameMap = userManagementServiceWrapper.retrieveFeatureNamesByIds(new ArrayList<>(featureIds), false);
        List<Long> companyIds = new ArrayList<>();
        companyIds.add(hkLoginDataBean.getCompanyId());
        Map<Long, Map<Long, Map<HkSectionEntity, List<HkFieldEntity>>>> companyFeatureSectionsMap = fieldService.retrieveCompanyFeatureSectionsMap(new ArrayList<>(featureIds), companyIds);
        if (!CollectionUtils.isEmpty(companyFeatureSectionsMap)) {
            for (Map.Entry<Long, Map<Long, Map<HkSectionEntity, List<HkFieldEntity>>>> entry : companyFeatureSectionsMap.entrySet()) {
//                if (entry.getKey().equals(hkLoginDataBean.getCompanyId())) {
                Map<Long, Map<HkSectionEntity, List<HkFieldEntity>>> featureSectionFieldMap = entry.getValue();
                if (!CollectionUtils.isEmpty(featureSectionFieldMap)) {
                    for (Map.Entry<Long, Map<HkSectionEntity, List<HkFieldEntity>>> entry1 : featureSectionFieldMap.entrySet()) {
                        Long featureId = entry1.getKey();
                        Map<HkSectionEntity, List<HkFieldEntity>> sectionFieldMap = entry1.getValue();
                        if (!CollectionUtils.isEmpty(sectionFieldMap)) {
                            if (featureIdNameMap.containsKey(featureId)) {
                                Map<String, List<FieldDataBean>> sectionFieldMapData = new HashMap<>();
                                for (Map.Entry<HkSectionEntity, List<HkFieldEntity>> entry2 : sectionFieldMap.entrySet()) {
                                    List<HkFieldEntity> fieldEntityList = entry2.getValue();
                                    List<FieldDataBean> fieldDataBeanList = new ArrayList<>();
                                    List<FieldDataBean> subFieldList = new ArrayList<>();
                                    for (HkFieldEntity hkFieldEntity : fieldEntityList) {
                                        Boolean isDff = null;
                                        Long pointerId = null;
                                        if (!StringUtils.isEmpty(hkFieldEntity.getValidationPattern()) && !hkFieldEntity.getValidationPattern().equals("{}") && !hkFieldEntity.getValidationPattern().equalsIgnoreCase("p1")) {
                                            Type type = new TypeToken<Map<String, Object>>() {
                                            }.getType();
                                            Map<String, Object> validationPatternMap = (new Gson()).fromJson(hkFieldEntity.getValidationPattern().trim(), type);
                                            if (!CollectionUtils.isEmpty(validationPatternMap) && validationPatternMap.containsKey("isDff")) {
                                                isDff = Boolean.valueOf(validationPatternMap.get("isDff").toString().trim());

                                            }
                                            if (!CollectionUtils.isEmpty(validationPatternMap) && validationPatternMap.containsKey("pointer")) {
                                                pointerId = Long.parseLong(validationPatternMap.get("pointer").toString());
                                            }
                                        }
                                        if (hkFieldEntity.getDbBaseType() != null && hkFieldEntity.getDbBaseType().equals(HkSystemConstantUtil.DBBaseType.MONGO_DB) && !hkFieldEntity.getIsCustomField() && isDff != null && isDff.equals(Boolean.FALSE)) {
                                        } else {
                                            //-----By Shreya Added Code for adding value of sub entity in field list
                                            if (hkFieldEntity.getComponentType().equals(HkSystemConstantUtil.CustomField.ComponentType.SUBENTITY)) {
                                                if (!CollectionUtils.isEmpty(subEntityFieldMap) && subEntityFieldMap.containsKey(hkFieldEntity.getId())) {
                                                    if (!CollectionUtils.isEmpty(subEntityFieldMap.get(hkFieldEntity.getId()))) {
                                                        subFieldList.addAll(subEntityFieldMap.get(hkFieldEntity.getId()));
                                                    }
                                                }
                                            }
                                            FieldDataBean fieldDataBean = new FieldDataBean();
                                            fieldDataBean = convertFieldEntityToFieldDataBean(hkFieldEntity, fieldDataBean);
                                            //Change pointer component type and data type according to parent.
                                            if (pointerId != null) {
                                                if (pointerParentFieldMap.containsKey(pointerId)) {
                                                    HkFieldEntity fieldEntity = pointerParentFieldMap.get(pointerId);
                                                    fieldDataBean.setFieldType(fieldEntity.getFieldType());
                                                    fieldDataBean.setComponentType(fieldEntity.getComponentType());
                                                }
                                            }
                                            if (fieldDataBean.getValidationPattern().equalsIgnoreCase("p1") || fieldDataBean.getValidationPattern().equals("{}")) {
                                                fieldDataBean.setValidationPattern("{}");
                                            }
                                            //Add master code to cut, caret range, color and flourscence of price list.
                                            String masterCode = null;
                                            if (fieldDataBean.getDbBaseName().equals("hk_price_list_dtl")) {
                                                if (fieldDataBean.getDbFieldName().equals("clarity")) {
                                                    masterCode = fieldsDbFieldNameToID.get(HkSystemConstantUtil.PlanStaticFieldName.CLARITY);
                                                }
                                                if (fieldDataBean.getDbFieldName().equals("color")) {
                                                    masterCode = fieldsDbFieldNameToID.get(HkSystemConstantUtil.PlanStaticFieldName.COLOR);
                                                }
                                                if (fieldDataBean.getDbFieldName().equals("cut")) {
                                                    masterCode = fieldsDbFieldNameToID.get(HkSystemConstantUtil.PlanStaticFieldName.CUT);
                                                }
                                                if (fieldDataBean.getDbFieldName().equals("fluorescence")) {
                                                    masterCode = fieldsDbFieldNameToID.get(HkSystemConstantUtil.PlanStaticFieldName.FLUROSCENE);
                                                }
                                                if (fieldDataBean.getDbFieldName().equals("type_of_invoice")) {
                                                    masterCode = fieldsDbFieldNameToID.get(HkSystemConstantUtil.InvoiceStaticFields.TYPE_OF_INVOICE);
                                                }
                                            }
                                            //Add master code to cut, caret range, color and flourscence of price list.
                                            if (fieldDataBean.getDbBaseName().equals("invoice")) {
                                                if (fieldDataBean.getDbFieldName().equals("type_of_invoice")) {
                                                    masterCode = fieldsDbFieldNameToID.get(HkSystemConstantUtil.InvoiceStaticFields.TYPE_OF_INVOICE);
                                                }
                                            }
                                            if (masterCode != null) {
                                                Type type = new TypeToken<Map<String, Object>>() {
                                                }.getType();
                                                Map<String, Object> validationPatternMap = (new Gson()).fromJson(fieldDataBean.getValidationPattern().trim(), type);
                                                if (validationPatternMap == null) {
                                                    validationPatternMap = new HashMap<>();
                                                }
                                                if (validationPatternMap.get("masterCode") == null) {
                                                    validationPatternMap.put("masterCode", masterCode);
                                                    fieldDataBean.setValidationPattern((new Gson()).toJson(validationPatternMap, type));
                                                }
                                            }
                                            fieldDataBeanList.add(fieldDataBean);
                                        }
                                    }
                                    if (entry2.getKey() != null && !StringUtils.isEmpty(entry2.getKey().getSectionName()) && !CollectionUtils.isEmpty(fieldDataBeanList)) {
                                        sectionFieldMapData.put(entry2.getKey().getSectionName(), fieldDataBeanList);
                                    } else if (!CollectionUtils.isEmpty(fieldDataBeanList)) {
                                        if (!CollectionUtils.isEmpty(subFieldList)) {
//                                            System.out.println("subFieldList :" + subFieldList);
                                            fieldDataBeanList.addAll(subFieldList);
                                        }
                                        sectionFieldMapData.put("General", fieldDataBeanList);
                                    }
                                }
                                featureSectionFieldMapData.put(featureIdNameMap.get(featureId), sectionFieldMapData);
                            }
                        }
                    }
                }
//                }
            }
        }
        if (!CollectionUtils.isEmpty(subFeatureSectionFieldMap)) {
            featureSectionFieldMapData.putAll(subFeatureSectionFieldMap);
        }
        return featureSectionFieldMapData;

    }

    public RbTabularRelationDataBean convertTabularRelationEntityToDataBean(RbTabularRelationEntity tabularRelationEntity, RbTabularRelationDataBean tabularRelationDataBean) {
        tabularRelationDataBean.setId(tabularRelationEntity.getId());
        tabularRelationDataBean.setRelationLeftToRight(tabularRelationEntity.getRelationLeftToRight());
        tabularRelationDataBean.setTable1(tabularRelationEntity.getTable1());
        tabularRelationDataBean.setTable1Column(tabularRelationEntity.getTable1Column());
        tabularRelationDataBean.setTable2(tabularRelationEntity.getTable2());
        tabularRelationDataBean.setTable2Column(tabularRelationEntity.getTable2Column());
        tabularRelationDataBean.setJoinTable(tabularRelationEntity.getJoinTable());
        tabularRelationDataBean.setJoinColumnTable1(tabularRelationEntity.getJoinColumnTable1());
        tabularRelationDataBean.setJoinColumnTable2(tabularRelationEntity.getJoinColumnTable2());
        return tabularRelationDataBean;
    }

    public RbTabularRelationEntity convertTabularRelationDataBeanToEntity(RbTabularRelationEntity tabularRelationEntity, RbTabularRelationDataBean tabularRelationDataBean) {
        tabularRelationEntity.setRelationLeftToRight(tabularRelationDataBean.getRelationLeftToRight());
        tabularRelationEntity.setTable1(tabularRelationDataBean.getTable1());
        tabularRelationEntity.setTable1Column(tabularRelationDataBean.getTable1Column());
        tabularRelationEntity.setTable2(tabularRelationDataBean.getTable2());
        tabularRelationEntity.setTable2Column(tabularRelationDataBean.getTable2Column());
        return tabularRelationEntity;
    }

    public List<RbTabularRelationDataBean> retrieveTableRelationship(MasterReportDataBean masterReportDataBean) throws JSONException {
        Set<String> tableNameSet = new HashSet<>();
        Set<String> customFieldTableNameSet = new HashSet<>();
        String fieldName = null;
        int count = 0;
        List<String> joinAttributeList = new ArrayList<>();
        List<RbTabularRelationDataBean> relationDataBeans = new ArrayList<>();
        Map<String, List<RbTabularRelationDataBean>> fieldRelationMap = new LinkedHashMap<>();
        List<String> tableNameList = new ArrayList<>();
        List<String> customFieldTableNames = new ArrayList<>();
        if (masterReportDataBean != null) {
            if (!StringUtils.isEmpty(masterReportDataBean.getJoinAttributes())) {
                JSONArray array;
                array = new JSONArray(masterReportDataBean.getJoinAttributes());
                for (int i = 0; i < array.length(); i++) {
                    JSONObject obj = array.getJSONObject(i);
                    String joinAttributes = obj.getString("joinAttributes");
                    if (!StringUtils.isEmpty(joinAttributes) && !joinAttributes.equals("null")) {
                        String[] table2 = joinAttributes.split("=");
                        if (table2.length > 0) {
                            String[] tableName = table2[1].split("\\.");
                            if (tableName.length > 0) {
                                joinAttributeList.add(tableName[0].trim());
                            }
                        }
                    }
                }
            }
            List<RbFieldDataBean> columns = masterReportDataBean.getColumns();
            if (!CollectionUtils.isEmpty(columns)) {
                for (RbFieldDataBean rbFieldDataBean : columns) {
                    if (rbFieldDataBean.getDbBaseType().equals(HkSystemConstantUtil.DBBaseType.RELATIONAL_DB)) {
                        String tableName = rbFieldDataBean.getDbBaseName();
                        tableNameSet.add(tableName);
                    } else {
                        customFieldTableNameSet.add(rbFieldDataBean.getDbBaseName());
                    }
                }
                customFieldTableNames.addAll(customFieldTableNameSet);
                tableNameList.addAll(tableNameSet);
            }
            List<RbTabularRelationEntity> tabularRelationEntitys = rbReportService.retrieveRelationAmongTables(tableNameList, joinAttributeList);
            List<RbTabularRelationEntity> customFieldTabularRelationEntitys = rbReportService.retrieveCustomFieldRelationShip(customFieldTableNames);

            if (!CollectionUtils.isEmpty(tabularRelationEntitys)) {
                for (RbTabularRelationEntity rbTabularRelationEntity : tabularRelationEntitys) {
                    RbTabularRelationDataBean tabularRelationDataBean = new RbTabularRelationDataBean();
                    tabularRelationDataBean = convertTabularRelationEntityToDataBean(rbTabularRelationEntity, tabularRelationDataBean);
                    relationDataBeans.add(tabularRelationDataBean);
                }
            }
            if (!CollectionUtils.isEmpty(customFieldTabularRelationEntitys)) {
                for (RbTabularRelationEntity rbTabularRelationEntity : customFieldTabularRelationEntitys) {
                    RbTabularRelationDataBean tabularRelationDataBean = new RbTabularRelationDataBean();
                    tabularRelationDataBean = convertTabularRelationEntityToDataBean(rbTabularRelationEntity, tabularRelationDataBean);
                    relationDataBeans.add(tabularRelationDataBean);
                }
            }
//            if (!CollectionUtils.isEmpty(tabularRelationEntitys)) {
//                for (RbTabularRelationEntity rbTabularRelationEntity : tabularRelationEntitys) {
//                    String table1 = rbTabularRelationEntity.getTable1();
////                    String table1Column = rbTabularRelationEntity.getTable1Column();
////                    if (!StringUtils.isEmpty(table1) && !StringUtils.isEmpty(table1Column)) {
////                        fieldName = table1 + "." + table1Column;
////                    }
//                    RbTabularRelationDataBean tabularRelationDataBean = new RbTabularRelationDataBean();
//                    tabularRelationDataBean = convertTabularRelationEntityToDataBean(rbTabularRelationEntity, tabularRelationDataBean);
//                    if (fieldRelationMap.containsKey(table1)) {
//                        List<RbTabularRelationDataBean> list = fieldRelationMap.get(table1);
//                        for (RbTabularRelationDataBean rbTabularRelationDataBean : list) {
//                            if (tabularRelationDataBean.getTable2().equals(rbTabularRelationDataBean.getTable2())) {
//                                count++;
//                            }
//                        }
//                        if (count == 0) {
//                            fieldRelationMap.get(table1).add(tabularRelationDataBean);
//                        }
//                    } else {
//                        List<RbTabularRelationDataBean> dataBeans = new ArrayList<>();
//                        dataBeans.add(tabularRelationDataBean);
//                        fieldRelationMap.put(table1, dataBeans);
//                    }
//                }
//            }
//            if (!CollectionUtils.isEmpty(customFieldTabularRelationEntitys)) {
//                for (RbTabularRelationEntity rbTabularRelationEntity : customFieldTabularRelationEntitys) {
//                    RbTabularRelationDataBean tabularRelationDataBean = new RbTabularRelationDataBean();
//                    tabularRelationDataBean = convertTabularRelationEntityToDataBean(rbTabularRelationEntity, tabularRelationDataBean);
//                    if (!CollectionUtils.isEmpty(fieldRelationMap) && fieldRelationMap.containsKey(rbTabularRelationEntity.getTable1())) {
//                        fieldRelationMap.get(rbTabularRelationEntity.getTable1()).add(tabularRelationDataBean);
//                    } else {
//                        List<RbTabularRelationDataBean> dataBeans = new ArrayList<>();
//                        dataBeans.add(tabularRelationDataBean);
//                        fieldRelationMap.put(rbTabularRelationEntity.getTable1(), dataBeans);
//                    }
//                }
//            }
        }

        return relationDataBeans;
    }

    public MasterReportDataBean retrieveColumnMetadata(MasterReportDataBean masterReportDataBean) {
        if (masterReportDataBean != null) {
            if (!CollectionUtils.isEmpty(masterReportDataBean.getColumns())) {
                Map<String, Object> fkConversionMap = RbReportConstantUtils.Converter.FK_CONVERSION_MAP;
                for (RbFieldDataBean rbFieldDataBean : masterReportDataBean.getColumns()) {
                    String columnDefinition = rbFieldDataBean.getDbBaseName() + "." + rbFieldDataBean.getColName();
                    String convertedColumn = null;
                    //Change in data type in case of subentity.
                    if (rbFieldDataBean.getComponentType().equals(HkSystemConstantUtil.CustomField.ComponentType.SUBENTITY)) {
                        String dataType = null;
                        CustomFieldInfoDataBean customFieldInfoDataBean = customFieldTransformerBean.retrieveFieldById(Long.parseLong(rbFieldDataBean.getHkFieldId()));
                        if (customFieldInfoDataBean != null) {
                            CustomFieldDataBean customFieldDataBean = customFieldInfoDataBean.getCustomFieldDataBean();
                            if (customFieldDataBean != null) {
                                Set<SubEntityDataBean> subEntityDataBeans = customFieldDataBean.getSubEntityDataBean();
                                if (!CollectionUtils.isEmpty(subEntityDataBeans)) {
                                    for (SubEntityDataBean subEntityDataBean : subEntityDataBeans) {
                                        if (subEntityDataBean.isIsDroplistField()) {
                                            dataType = subEntityDataBean.getSubFieldType();
                                            if (dataType.equals("String")) {
                                                dataType = "varchar";
                                                if (!StringUtils.isEmpty(subEntityDataBean.getValidationPattern()) && !subEntityDataBean.getValidationPattern().equals("{}")) {
                                                    Type type = new TypeToken<Map<String, String>>() {
                                                    }.getType();
                                                    Map<String, String> validationPatternMap = (new Gson()).fromJson(subEntityDataBean.getValidationPattern(), type);
                                                    if (!CollectionUtils.isEmpty(validationPatternMap) && validationPatternMap.containsKey("allowedTypes")) {
                                                        if (validationPatternMap.get("allowedTypes").equals("Numeric")) {
                                                            dataType = "int8";
                                                        }
                                                    }
                                                }
                                            } else if (dataType.equals("Date") || dataType.equals("timestamp")) {
                                                dataType = "timestamp";
                                            } else if (dataType.equals("StringArray")) {
                                                dataType = "varchar";
                                            } else if (dataType.equals("Double")) {
                                                dataType = "double precision";
                                            } else {
                                                dataType = "int8";
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        if (dataType != null) {
                            rbFieldDataBean.setDataType(dataType);
                        }
                    } else if (!CollectionUtils.isEmpty(fkConversionMap) && fkConversionMap.get(columnDefinition) != null) {
                        Boolean isRepeatConversion = Boolean.TRUE;
                        String convertedColumnDef = columnDefinition;
                        while (isRepeatConversion) {
                            Type type = new TypeToken<Map<String, String>>() {
                            }.getType();
                            Map<String, String> fieldConversionMap = (new Gson()).fromJson(fkConversionMap.get(convertedColumnDef).toString(), type);
                            String fkTable = fieldConversionMap.get(RbReportConstantUtils.JoinAttributeKey.FK_TABLE.toString());
                            String fkColumn = fieldConversionMap.get(RbReportConstantUtils.JoinAttributeKey.FK_COLUMN.toString());
                            String localColumn = fieldConversionMap.get(RbReportConstantUtils.JoinAttributeKey.SHOW_COLUMN.toString());
                            Boolean isAnotherConversion = null;
                            if (fieldConversionMap.get(RbReportConstantUtils.JoinAttributeKey.IS_ANOTHER_CONVERSION.toString()) != null) {
                                isAnotherConversion = Boolean.parseBoolean(fieldConversionMap.get(RbReportConstantUtils.JoinAttributeKey.IS_ANOTHER_CONVERSION.toString()));
                            }
                            if (isAnotherConversion != null && isAnotherConversion) {
                                isRepeatConversion = Boolean.TRUE;
                            } else {
                                isRepeatConversion = Boolean.FALSE;
                            }
                            convertedColumn = fkTable + "." + localColumn;
                        }

                        rbFieldDataBean.setConvertedColumn(convertedColumn);
                    } else if (rbFieldDataBean.getDbBaseType().equals(HkSystemConstantUtil.DBBaseType.MONGO_DB) && rbFieldDataBean.getSectionName() != null && !rbFieldDataBean.getSectionName().equals("General")) {
                        if (rbFieldDataBean.getComponentType().equals(HkSystemConstantUtil.CustomField.ComponentType.DROPDOWN) || rbFieldDataBean.getComponentType().equals(HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN) || rbFieldDataBean.getComponentType().equals(HkSystemConstantUtil.CustomField.ComponentType.USER_MULTISELECT)) {
                            rbFieldDataBean.setDataType("varchar");
                        }
                    } else if (!CollectionUtils.isEmpty(fkConversionMap) && fkConversionMap.get(columnDefinition) == null && (rbFieldDataBean.getComponentType().equals(HkSystemConstantUtil.CustomField.ComponentType.DROPDOWN) || rbFieldDataBean.getComponentType().equals(HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN) || rbFieldDataBean.getComponentType().equals(HkSystemConstantUtil.CustomField.ComponentType.USER_MULTISELECT))) {
                        rbFieldDataBean.setDataType("varchar");
                    }
                }
            }
        }
        return masterReportDataBean;
    }

    public Map<String, Object> retrievePaginatedData(Integer offSet, Integer limit, Boolean isFilter, Boolean isGroupedCheck, String sortColumn, String sortDirection, String sortColumnType, List<Map<String, String>> filterOptions) {
        Map<String, Object> columnValueMap = new HashMap<>();
        boolean isGrouped = false;
        if (isGroupedCheck != null && isGroupedCheck == true) {
            isGrouped = true;
        }
        if (isFilter) {
            columnValueMap = sessionUtil.getReportColumnValueMap();
        } else {
            columnValueMap = sessionUtil.getTempFilteredRecordList();
        }
        Map<String, Object> reportColumnValueMap = new HashMap<>();
        List<LinkedHashMap<String, Object>> finalList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(columnValueMap)) {
            List<LinkedHashMap<String, Object>> originalRecordsList = null;
            if (!isGrouped) {
                originalRecordsList = new ArrayList<>((ArrayList<LinkedHashMap<String, Object>>) columnValueMap.get("records"));
            } else {
                Map<String, List<Object>> groupRecords = (Map<String, List<Object>>) columnValueMap.get("records");
                List<LinkedHashMap<String, Object>> extractedList = new ArrayList<>();
                for (Map.Entry<String, List<Object>> entry : groupRecords.entrySet()) {
                    for (Object valueMap : entry.getValue()) {
                        extractedList.add((LinkedHashMap<String, Object>) valueMap);
                    }
                }
                originalRecordsList = new ArrayList<>(extractedList);
            }
            //Apply filters from the data table.
            if (!CollectionUtils.isEmpty(filterOptions)) {
                Iterator<LinkedHashMap<String, Object>> recordsItr = originalRecordsList.iterator();
                while (recordsItr.hasNext()) {
                    LinkedHashMap<String, Object> currentRow = recordsItr.next();
                    boolean rowInvalid = false;
                    for (Map<String, String> filterObj : filterOptions) {
                        String columnName = filterObj.get("filterColumn");
                        String dataType = filterObj.get("filterColumnType");
                        String filterValue = filterObj.get("filterValue");
                        if (columnName != null && dataType != null && filterValue != null) {
                            if (currentRow.get(columnName) == null) {
                                rowInvalid = true;
                                break;
                            } else {
                                String value = currentRow.get(columnName).toString().trim();
                                if (!value.toLowerCase().contains(filterValue.toLowerCase())) {
                                    rowInvalid = true;
                                    break;
                                }
                            }
                        }
                    }
                    if (rowInvalid) {
                        recordsItr.remove();
                    }
                }
            }
            //Apply sorting options if available.
            List<LinkedHashMap<String, Object>> recordListForSettingOffset = (ArrayList<LinkedHashMap<String, Object>>) originalRecordsList;
            if (sortColumn != null && sortDirection != null && sortColumnType != null) {
                Collections.sort(recordListForSettingOffset, new Comparator() {
                    @Override
                    public int compare(Object o1, Object o2) {
                        Map<String, Object> m1 = (Map<String, Object>) o1;
                        Map<String, Object> m2 = (Map<String, Object>) o2;
                        int sizeCmp = 0;
                        if (sortDirection.equals("asc")) {
                            if (m1.get(sortColumn) == null) {
                                if (m2.get(sortColumn) == null) {
                                    return 0; //equal
                                } else {
                                    return -1; // null is before other strings
                                }
                            } else // this.member != null
                            if (m2.get(sortColumn) == null) {
                                return 1;  // all other strings are after null
                            } else {
                                if (sortColumnType.equals("string")) {
                                    sizeCmp = m1.get(sortColumn).toString().toLowerCase().compareTo(m2.get(sortColumn).toString().toLowerCase());
                                } else if (sortColumnType.equals("number")) {
                                    Long first = Long.parseLong(m1.get(sortColumn).toString());
                                    Long second = Long.parseLong(m2.get(sortColumn).toString());
                                    sizeCmp = first.compareTo(second);
                                } else if (sortColumnType.equals("double")) {
                                    Double first = Double.parseDouble(m1.get(sortColumn).toString());
                                    Double second = Double.parseDouble(m2.get(sortColumn).toString());
                                    sizeCmp = first.compareTo(second);
                                } else if (sortColumnType.equals("date")) {
                                    SimpleDateFormat simpleDateFormats = new SimpleDateFormat("dd/MM/yyyy");
                                    Date first = null;
                                    try {
                                        first = simpleDateFormats.parse(m1.get(sortColumn).toString().trim().substring(0, 10));

                                    } catch (ParseException ex) {
                                        Logger.getLogger(ReportBuilderTransformerBean.class
                                                .getName()).log(Level.SEVERE, null, ex);
                                    }
                                    Date second = null;
                                    try {
                                        second = simpleDateFormats.parse(m2.get(sortColumn).toString().trim().substring(0, 10));

                                    } catch (ParseException ex) {
                                        Logger.getLogger(ReportBuilderTransformerBean.class
                                                .getName()).log(Level.SEVERE, null, ex);
                                    }
                                    return first.compareTo(second);
                                }
                            }
                        } else {
                            if (m2.get(sortColumn) == null) {
                                if (m1.get(sortColumn) == null) {
                                    return 0;
                                } else {
                                    return -1;
                                }
                            } else if (m1.get(sortColumn) == null) {
                                return 1;
                            } else {
                                if (sortColumnType.equals("string")) {
                                    sizeCmp = m2.get(sortColumn).toString().toLowerCase().compareTo(m1.get(sortColumn).toString().toLowerCase());
                                } else if (sortColumnType.equals("number")) {
                                    Long first = Long.parseLong(m2.get(sortColumn).toString());
                                    Long second = Long.parseLong(m1.get(sortColumn).toString());
                                    sizeCmp = first.compareTo(second);
                                } else if (sortColumnType.equals("double")) {
                                    Double first = Double.parseDouble(m2.get(sortColumn).toString());
                                    Double second = Double.parseDouble(m1.get(sortColumn).toString());
                                    sizeCmp = first.compareTo(second);
                                } else if (sortColumnType.equals("date")) {
                                    SimpleDateFormat simpleDateFormats = new SimpleDateFormat("dd/MM/yyyy");
                                    Date first = null;
                                    try {
                                        first = simpleDateFormats.parse(m2.get(sortColumn).toString().trim().substring(0, 10));

                                    } catch (ParseException ex) {
                                        Logger.getLogger(ReportBuilderTransformerBean.class
                                                .getName()).log(Level.SEVERE, null, ex);
                                    }
                                    Date second = null;
                                    try {
                                        second = simpleDateFormats.parse(m1.get(sortColumn).toString().trim().substring(0, 10));

                                    } catch (ParseException ex) {
                                        Logger.getLogger(ReportBuilderTransformerBean.class
                                                .getName()).log(Level.SEVERE, null, ex);
                                    }
                                    return first.compareTo(second);
                                }
                            }
                        }
                        if (sizeCmp != 0) {
                            return sizeCmp;
                        }
                        return sizeCmp;
                    }
                });
            }

            for (int i = offSet; i < (offSet + limit); i++) {
                if (i < recordListForSettingOffset.size()) {
                    finalList.add(recordListForSettingOffset.get(i));
                }
            }
            reportColumnValueMap.put("totalRecords", recordListForSettingOffset.size());
            reportColumnValueMap.put("records", finalList);

        }
        return reportColumnValueMap;
    }

    private String formatDate(Long longDate, Boolean isIncludeTime) throws ParseException {
        Date date = new Date(longDate);
        String timeFormat = isIncludeTime ? HkSystemConstantUtil.CUSTOM_FIELD_DATE_TIME_FORMAT : HkSystemConstantUtil.CUSTOM_FIELD_DATE_FORMAT;
        SimpleDateFormat dateFormatter = new SimpleDateFormat(timeFormat);
        String dates = dateFormatter.format(date);
        return dates;
    }

    public Map<String, Object> retrieveFilteredData(Map<String, Object> filterMap, Boolean isGrouped, List<String> groupByColumns) throws JSONException, ParseException {
        Map<String, Object> columnValueMap = sessionUtil.getReportColumnValueMap();
        List<LinkedHashMap<String, Object>> finalList = new ArrayList<>();
        Map<String, Object> filteredDataMap = new HashMap<>();
        Map<String, Object> sessionFilteredDataMap = new HashMap<>();
        List<LinkedHashMap<String, Object>> recordListForSettingOffset = new ArrayList<>();
        List<LinkedHashMap<String, Object>> filteredListOfRecord = new ArrayList<>();
        if (!isGrouped) {
            recordListForSettingOffset = (ArrayList<LinkedHashMap<String, Object>>) columnValueMap.get("records");
            filteredListOfRecord = new ArrayList<>(recordListForSettingOffset);
        } else {
            Map<String, List<Object>> groupRecords = (Map<String, List<Object>>) columnValueMap.get("records");
            for (Map.Entry<String, List<Object>> entrySet : groupRecords.entrySet()) {
                String key = entrySet.getKey();
                List<Object> value = entrySet.getValue();
                for (Object value1 : value) {
                    if (value1 != null) {
                        recordListForSettingOffset.add((LinkedHashMap<String, Object>) value1);
                    }
                }
            }
            filteredListOfRecord = new ArrayList<>(recordListForSettingOffset);
        }

        if (!CollectionUtils.isEmpty(recordListForSettingOffset)) {
            if (!CollectionUtils.isEmpty(filterMap)) {
                Iterator<LinkedHashMap<String, Object>> itr = filteredListOfRecord.iterator();
                while (itr.hasNext()) {
                    LinkedHashMap<String, Object> row = itr.next();
                    Boolean rowInvalid = false;
                    for (Map.Entry<String, Object> entrySet : filterMap.entrySet()) {
                        String columnLabel = entrySet.getKey();
                        List<String> filterValue = null;
                        Object filterObj = entrySet.getValue();
                        if (columnLabel != null && filterObj != null) {
                            Type type = new TypeToken<Map<String, Object>>() {
                            }.getType();
                            Map<String, Object> filterAttributes = (new Gson()).fromJson(filterObj.toString(), type);
                            if (!CollectionUtils.isEmpty(filterAttributes)) {
                                String operator = filterAttributes.get("operator").toString();
                                String filterType = filterAttributes.get("type").toString();
                                String componentType = filterAttributes.get("componentType").toString();
                                Object secondVal = null;
                                if (filterAttributes.get("filterValueSecond") != null) {
                                    secondVal = filterAttributes.get("filterValueSecond").toString();
                                }
                                if (row.get(columnLabel) != null && !(operator.equals("is null") || operator.equals("is not null"))) {
                                    if (filterType.equals(HkSystemConstantUtil.CustomField.DataType.VARCHAR)) {
                                        if (filterAttributes.get("filterValue") != null) {
                                            List<String> firstValues = new ArrayList<>();
                                            String[] arrays = null;
                                            if (operator.equals("in") || operator.equals("not in")) {
                                                arrays = filterAttributes.get("filterValue").toString().split(",");
                                            } else {
                                                arrays = new String[]{filterAttributes.get("filterValue").toString()};
                                            }
                                            for (String array1 : arrays) {
                                                firstValues.add(array1.trim().toLowerCase());
                                            }
                                            switch (operator) {
                                                case "=":
                                                case "in":
                                                    if (!firstValues.contains(row.get(columnLabel).toString().toLowerCase())) {
                                                        rowInvalid = true;
                                                    }
                                                    break; //optional
                                                case "!=":
                                                case "not in":
                                                    if (firstValues.contains(row.get(columnLabel).toString().toLowerCase())) {
                                                        rowInvalid = true;
                                                    }
                                                    break; //optional
                                                case "like":
                                                    String firstValue = "%" + firstValues.get(0) + "%";
                                                    firstValue = firstValue.replace("_", ".");
                                                    firstValue = firstValue.replace("%", ".*?");
                                                    if (!row.get(columnLabel).toString().toLowerCase().matches(firstValue)) {
                                                        rowInvalid = true;
                                                    }
                                                    break;
                                                default:
                                                    break;//Optional
                                            }
                                        }
                                    } else if (filterType.equals(HkSystemConstantUtil.CustomField.DataType.INT8)) {
                                        Long firstVal = Long.parseLong(filterAttributes.get("filterValue").toString());
                                        Long rowValue = Long.parseLong(row.get(columnLabel).toString());
                                        switch (operator) {
                                            case "=":
                                                if (!firstVal.equals(rowValue)) {
                                                    rowInvalid = true;
                                                }
                                                break;
                                            case "!=":
                                                if (firstVal.equals(rowValue)) {
                                                    rowInvalid = true;
                                                }
                                                break;
                                            case ">":
                                                if (firstVal.compareTo(rowValue) >= 0) {
                                                    rowInvalid = true;
                                                }
                                                break;
                                            case "<":
                                                if (firstVal.compareTo(rowValue) <= 0) {
                                                    rowInvalid = true;
                                                }
                                                break;
                                            case ">=":
                                                if (firstVal.compareTo(rowValue) > 0) {
                                                    rowInvalid = true;
                                                }
                                                break;
                                            case "<=":
                                                if (firstVal.compareTo(rowValue) < 0) {
                                                    rowInvalid = true;
                                                }
                                                break;
                                            case "between":
                                                if (secondVal != null) {
                                                    Long secondVals = Long.parseLong(secondVal.toString());
                                                    if (firstVal.compareTo(rowValue) > 0 || secondVals.compareTo(rowValue) < 0) {
                                                        rowInvalid = true;
                                                    }
                                                }

                                                break;
                                            default:
                                                break;
                                        }
                                    } else if (filterType.equals(HkSystemConstantUtil.CustomField.DataType.DOUBLE)) {
                                        Double firstVal = Double.parseDouble(filterAttributes.get("filterValue").toString());
                                        Double rowValue = null;
                                        if (componentType.equals(HkSystemConstantUtil.CustomField.ComponentType.CURRENCY)) {
                                            rowValue = Double.parseDouble(row.get(columnLabel).toString().split(" ")[0].trim());
                                        } else {
                                            rowValue = Double.parseDouble(row.get(columnLabel).toString());
                                        }
                                        switch (operator) {
                                            case "=":
                                                if (!firstVal.equals(rowValue)) {
                                                    rowInvalid = true;
                                                }
                                                break;
                                            case "!=":
                                                if (firstVal.equals(rowValue)) {
                                                    rowInvalid = true;
                                                }
                                                break;
                                            case ">":
                                                if (firstVal.compareTo(rowValue) >= 0) {
                                                    rowInvalid = true;
                                                }
                                                break;
                                            case "<":
                                                if (firstVal.compareTo(rowValue) <= 0) {
                                                    rowInvalid = true;
                                                }
                                                break;
                                            case ">=":
                                                if (firstVal.compareTo(rowValue) > 0) {
                                                    rowInvalid = true;
                                                }
                                                break;
                                            case "<=":
                                                if (firstVal.compareTo(rowValue) < 0) {
                                                    rowInvalid = true;
                                                }
                                                break;
                                            case "between":
                                                if (secondVal != null) {
                                                    Double secondVals = Double.parseDouble(secondVal.toString());
                                                    if (firstVal.compareTo(rowValue) > 0 || secondVals.compareTo(rowValue) < 0) {
                                                        rowInvalid = true;
                                                    }
                                                }

                                                break;
                                            default:
                                                break;
                                        }
                                    } else if (filterType.equals(HkSystemConstantUtil.CustomField.DataType.TIMESTAMP)) {
                                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                                        Date dateObj = simpleDateFormat.parse(row.get(columnLabel).toString().trim().substring(0, 10));
                                        SimpleDateFormat simpleDateFormatter = new SimpleDateFormat("yyyy-MM-dd");
                                        Date firstDateObj = simpleDateFormatter.parse(filterAttributes.get("filterValue").toString().trim().substring(0, 10));
                                        Date secondDate = null;
                                        if (secondVal != null) {
                                            secondDate = simpleDateFormatter.parse(secondVal.toString().trim().substring(0, 10));
                                        }
                                        switch (operator) {
                                            case "=":
                                                if (!firstDateObj.equals(dateObj)) {
                                                    rowInvalid = true;
                                                }
                                                break; //optional
                                            case "!=":
                                                if (firstDateObj.equals(dateObj)) {
                                                    rowInvalid = true;
                                                }
                                                break; //optional
                                            case ">":
                                                if (firstDateObj.after(dateObj) || firstDateObj.equals(dateObj)) {
                                                    rowInvalid = true;
                                                }
                                                break;
                                            case "<":
                                                if (firstDateObj.before(dateObj) || firstDateObj.equals(dateObj)) {
                                                    rowInvalid = true;
                                                }
                                                break;
                                            case ">=":
                                                if (firstDateObj.after(dateObj)) {
                                                    rowInvalid = true;
                                                }
                                                break;
                                            case "<=":
                                                if (firstDateObj.before(dateObj)) {
                                                    rowInvalid = true;
                                                }
                                                break;
                                            case "between":
                                                if (secondDate != null) {
                                                    if (!(firstDateObj.before(dateObj) && secondDate.after(dateObj))) {
                                                        rowInvalid = true;
                                                    }
                                                }

                                                break;
                                            default:
                                                break;
                                        }
                                    } else if (filterType.equals(HkSystemConstantUtil.CustomField.DataType.BOOLEAN)) {
                                        Boolean dbValue = null;
                                        dbValue = Boolean.parseBoolean(row.get(columnLabel).toString().trim());
                                        Boolean firstValue = Boolean.parseBoolean(filterAttributes.get("filterValue").toString().trim());

                                        if (operator.equals("=")) {
                                            if (!firstValue.equals(dbValue)) {
                                                rowInvalid = true;
                                            }
                                            break;
                                        }
                                    }
                                    if (rowInvalid) {
                                        break;
                                    }
                                } else if (operator.equals("is null") || operator.equals("is not null")) {
                                    if (operator.equals("is null") && row.get(columnLabel) != null) {
                                        rowInvalid = true;
                                        break;
                                    } else if (operator.equals("is not null") && row.get(columnLabel) == null) {
                                        rowInvalid = true;
                                        break;
                                    }
                                } else {
                                    rowInvalid = true;
                                    break;
                                }
                            }
                        }
                        if (rowInvalid) {
                            break;
                        }
                    }
                    if (rowInvalid) {
                        itr.remove();
                    }
                }

            }
        }
        for (int i = 0; i < (10); i++) {
            if (i < filteredListOfRecord.size()) {
                finalList.add(filteredListOfRecord.get(i));
            }
        }
        if (isGrouped) {
            Map<String, List<Object>> finalListMap = new LinkedHashMap<>();
            Map<String, List<Object>> filteredListOfRecordMap = new LinkedHashMap<>();
            Map<GroupingKey, List<Object>> tempFinalListMap = this.groupRecords(finalList, groupByColumns);
            if (!CollectionUtils.isEmpty(tempFinalListMap)) {
                for (Map.Entry<GroupingKey, List<Object>> entrySet : tempFinalListMap.entrySet()) {
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
                    finalListMap.put(finalKey, value);
                }
            }
            Map<GroupingKey, List<Object>> tempFilteredListOfRecordMap = this.groupRecords(filteredListOfRecord, groupByColumns);
            if (!CollectionUtils.isEmpty(tempFilteredListOfRecordMap)) {
                for (Map.Entry<GroupingKey, List<Object>> entrySet : tempFilteredListOfRecordMap.entrySet()) {
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
                    filteredListOfRecordMap.put(finalKey, value);
                }
            }
            sessionFilteredDataMap.put("records", filteredListOfRecordMap);
            sessionFilteredDataMap.put("totalRecords", filteredListOfRecordMap.size());
            filteredDataMap.put("records", finalListMap);
            filteredDataMap.put("totalRecords", filteredListOfRecordMap.size());
            sessionUtil.setTempFilteredRecordList(sessionFilteredDataMap);
        } else {
            sessionFilteredDataMap.put("records", filteredListOfRecord);
            sessionFilteredDataMap.put("totalRecords", filteredListOfRecord.size());
            filteredDataMap.put("records", finalList);
            filteredDataMap.put("totalRecords", filteredListOfRecord.size());
            sessionUtil.setTempFilteredRecordList(sessionFilteredDataMap);
        }

        //**********************  MITAL: JAVA-8 MUST BE SUPPORTED TO UNCOMMENT THIS CODE  **************************
        //  Mital: Uncomment below code after getting group by columns
//        Type collectionType = new TypeToken<Map<String, Object>>() {
//        }.getType();
//        Map<String, Object> groupByColumnMap = new Gson().fromJson(masterReportDataBean.getGroupAttributes(), collectionType);
//        if (!CollectionUtils.isEmpty(groupByColumnMap) && groupByColumnMap.get("groupBy") != null) {
//            String[] groupByColumnArr = groupByColumnMap.get("groupBy").toString().split(",");
//            List<String> groupByColumns = Arrays.asList(groupByColumnArr);
        this.groupRecords(recordListForSettingOffset, null);
//        }
        //**********************  MITAL: JAVA-8 MUST BE SUPPORTED TO UNCOMMENT THIS CODE  **************************
        return filteredDataMap;
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
                                Long reportValue = new Long(reportParameters.getValue(newColumnName).toString());
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
        Boolean currencyConfiguration = Boolean.FALSE;
        HkSystemConfigurationEntity configurationEntity = hkFoundationService.retrieveSystemConfigurationByKey(HkSystemConstantUtil.FranchiseConfiguration.CURR_VISIBILITY_STATUS, hkLoginDataBean.getCompanyId());
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
        List<FeatureDataBean> features = applicationMasterInitializer.generateMenuByRoles(hkLoginDataBean.getRoleIds(), true);
        if (!CollectionUtils.isEmpty(features)) {
            for (FeatureDataBean featureDataBean : features) {
                if (featureDataBean.getFeatureName().equals("viewCurrencyData") && featureDataBean.getMenuType().equals("EI")) {
                    return Boolean.TRUE;
                }
            }
        }
        return Boolean.FALSE;
    }

    public RbEmailReportConfigurationDataBean retrieveConfigurationByReportId(Long reportId) {
        RbEmailReportConfigurationDataBean rbEmailReportConfigurationDataBean = null;
        RbEmailReportConfigurationEntity rbEmailReportConfigurationEntity = rbReportService.retrieveEmailConfigurationByReportId(reportId, hkLoginDataBean.getId());
        if (rbEmailReportConfigurationEntity != null) {
            rbEmailReportConfigurationDataBean = new RbEmailReportConfigurationDataBean();
            rbEmailReportConfigurationDataBean.setId(rbEmailReportConfigurationEntity.getId());
            rbEmailReportConfigurationDataBean.setActualEndDate(rbEmailReportConfigurationEntity.getActualEndDate());
            rbEmailReportConfigurationDataBean.setAfterUnits(rbEmailReportConfigurationEntity.getAfterUnits());
            rbEmailReportConfigurationDataBean.setAtTime(rbEmailReportConfigurationEntity.getAtTime());
            rbEmailReportConfigurationDataBean.setEndDate(rbEmailReportConfigurationEntity.getEndDate());
            rbEmailReportConfigurationDataBean.setEndRepeatMode(rbEmailReportConfigurationEntity.getEndRepeatMode());
            rbEmailReportConfigurationDataBean.setMonthlyOnDay(rbEmailReportConfigurationEntity.getMonthlyOnDay());
            rbEmailReportConfigurationDataBean.setPdfAttachment(rbEmailReportConfigurationEntity.isPdfAttachment());
            rbEmailReportConfigurationDataBean.setRepeatativeMode(rbEmailReportConfigurationEntity.getRepeatativeMode());
            rbEmailReportConfigurationDataBean.setRepetitionCnt(rbEmailReportConfigurationEntity.getRepetitionCnt());
            rbEmailReportConfigurationDataBean.setReportId(reportId);
            rbEmailReportConfigurationDataBean.setStatus(rbEmailReportConfigurationEntity.getStatus());
            rbEmailReportConfigurationDataBean.setWeeklyOnDays(rbEmailReportConfigurationEntity.getWeeklyOnDays());
            rbEmailReportConfigurationDataBean.setXlsAttachment(rbEmailReportConfigurationEntity.isXlsAttachment());
        }
        return rbEmailReportConfigurationDataBean;
    }

    public void saveEmailConfiguration(RbEmailReportConfigurationDataBean rbEmailReportConfigurationDataBean) throws GenericDatabaseException {
        if (rbEmailReportConfigurationDataBean != null) {
            RbEmailReportConfigurationEntity rbEmailReportConfigurationEntity = null;
            if (rbEmailReportConfigurationDataBean.getId() != null) {
                rbEmailReportConfigurationEntity = rbReportService.retrieveEmailConfigurationById(rbEmailReportConfigurationDataBean.getId());
            }
            rbEmailReportConfigurationEntity = this.convertEmailConfigurationDataBeanToModel(rbEmailReportConfigurationEntity, rbEmailReportConfigurationDataBean);
            rbReportService.saveEmailConfiguration(rbEmailReportConfigurationEntity);
            this.generateTimeBasedReportEmailNotification(rbEmailReportConfigurationEntity);
        }
    }

    private RbEmailReportConfigurationEntity convertEmailConfigurationDataBeanToModel(RbEmailReportConfigurationEntity rbEmailReportConfigurationEntity, RbEmailReportConfigurationDataBean rbEmailReportConfigurationDataBean) {
        boolean isCreateMode = true;
        if (rbEmailReportConfigurationEntity == null) {
            rbEmailReportConfigurationEntity = new RbEmailReportConfigurationEntity();
            isCreateMode = false;
        }
        if (rbEmailReportConfigurationDataBean != null) {
            rbEmailReportConfigurationEntity.setActualEndDate(rbEmailReportConfigurationDataBean.getActualEndDate());
            rbEmailReportConfigurationEntity.setAfterUnits(rbEmailReportConfigurationDataBean.getAfterUnits());
            rbEmailReportConfigurationEntity.setAtTime(rbEmailReportConfigurationDataBean.getAtTime());
            rbEmailReportConfigurationEntity.setEndDate(rbEmailReportConfigurationDataBean.getEndDate());
            rbEmailReportConfigurationEntity.setEndRepeatMode(rbEmailReportConfigurationDataBean.getEndRepeatMode());
            rbEmailReportConfigurationEntity.setMonthlyOnDay(rbEmailReportConfigurationDataBean.getMonthlyOnDay());
            rbEmailReportConfigurationEntity.setPdfAttachment(rbEmailReportConfigurationDataBean.isPdfAttachment());
            rbEmailReportConfigurationEntity.setXlsAttachment(rbEmailReportConfigurationDataBean.isXlsAttachment());
            rbEmailReportConfigurationEntity.setRepeatativeMode(rbEmailReportConfigurationDataBean.getRepeatativeMode());
            rbEmailReportConfigurationEntity.setRepetitionCnt(rbEmailReportConfigurationDataBean.getRepetitionCnt());
            rbEmailReportConfigurationEntity.setStatus(HkSystemConstantUtil.ACTIVE);
            rbEmailReportConfigurationEntity.setWeeklyOnDays(rbEmailReportConfigurationDataBean.getWeeklyOnDays());
            rbEmailReportConfigurationEntity.setLastModifiedBy(hkLoginDataBean.getId());
            rbEmailReportConfigurationEntity.setLastModifiedOn(new Date());
            if (!isCreateMode) {
                rbEmailReportConfigurationEntity.setCreatedBy(hkLoginDataBean.getId());
                rbEmailReportConfigurationEntity.setCreatedOn(new Date());
                rbEmailReportConfigurationEntity.setFranchise(hkLoginDataBean.getCompanyId());
            }
            RbReport report = rbReportService.retriveReportById(rbEmailReportConfigurationDataBean.getReportId(), null, null);
            rbEmailReportConfigurationEntity.setReport(report);
        }
        return rbEmailReportConfigurationEntity;
    }

    /**
     * Code to manage dashboard reports.
     */
    public Boolean retrieveDashboardStatus(Long reportId) {
        List<RbUserReportDashboardEntity> userReportDashboardStatusEntites = rbReportService.retrieveUserReportDashboardStatus(reportId, hkLoginDataBean.getId());
        if (!userReportDashboardStatusEntites.isEmpty()) {
            RbUserReportDashboardEntity userReportDashboardStatus = userReportDashboardStatusEntites.get(0);
            if (userReportDashboardStatus != null) {
                return true;
            }
        }
        return false;
    }

    public void updateDashboardStatus(Long reportId, boolean isDashboardEnabled) {
        if (reportId != null) {
            RbUserReportDashboardEntityPK dashboardEntityPK = new RbUserReportDashboardEntityPK(hkLoginDataBean.getId(), reportId);
            RbUserReportDashboardEntity userReportDashboardEntity = new RbUserReportDashboardEntity(dashboardEntityPK);
            if (isDashboardEnabled) {
                userReportDashboardEntity.setStatus(RbReportConstantUtils.DASHBOARD);
            } else {
                userReportDashboardEntity.setStatus(RbReportConstantUtils.NOT_DASHBOARD);
            }
            userReportDashboardEntity.setIsArchive(Boolean.FALSE);
            rbReportService.updateUserReportDashboardStatus(userReportDashboardEntity);
        }
    }

    public List<MasterReportDataBean> retrieveDashboardReports() throws GenericDatabaseException {
        List<MasterReportDataBean> reportDataBeans = new ArrayList<>();
        Map<GenericDao.QueryOperators, Object> criterias1 = new HashMap<>();
        List<String> requires = new ArrayList<>();
        requires.add(UMRoleFeatureDao.FEATURE_SET);
        Map<String, Object> inMap = new HashMap<>();
        Map<String, Object> equalMap1 = new HashMap<>();

        inMap.put(UMRoleFeatureDao.ROLE_ID, hkLoginDataBean.getRoleIds());
        equalMap1.put(UMRoleDao.IS_ARCHIVE, Boolean.FALSE);
        equalMap1.put(UMRoleFeatureDao.COMPANY, hkLoginDataBean.getCompanyId());

        criterias1.put(GenericDao.QueryOperators.IN, inMap);
        criterias1.put(GenericDao.QueryOperators.EQUAL, equalMap1);
        List<UMRoleFeature> roleFeatures = roleService.retrieveRoleFeatures(null, criterias1, requires);

        List<Long> reportFeatureIds = new ArrayList<>();
        if (!CollectionUtils.isEmpty(roleFeatures)) {
            for (UMRoleFeature roleFeature : roleFeatures) {
                if (roleFeature.getFeature().getMenuType().equals(HkSystemConstantUtil.ReportFeatureTypes.MENU_ITEM)) {
                    reportFeatureIds.add(roleFeature.getFeature().getId());
                }
            }
            List<RbReport> reports = rbReportService.retrieveExternalReportsByFeatureIds(reportFeatureIds);
            Set<Long> roleWiseReportIds = new HashSet<>();
            if (!CollectionUtils.isEmpty(reports)) {
                for (RbReport report : reports) {
                    roleWiseReportIds.add(report.getId());
                }
            }
            List<RbUserReportDashboardEntity> userReportDashboardStatusEntites = rbReportService.retrieveUserReportDashboardStatus(null, hkLoginDataBean.getId());
            Set<Long> dashboardWiseReportIds = new HashSet<>();
            if (!CollectionUtils.isEmpty(userReportDashboardStatusEntites)) {
                for (RbUserReportDashboardEntity entity : userReportDashboardStatusEntites) {
                    dashboardWiseReportIds.add(entity.getRbUserReportDashboardEntityPK().getReportId());
                }
            }
            if (!CollectionUtils.isEmpty(dashboardWiseReportIds)) {
                dashboardWiseReportIds.retainAll(roleWiseReportIds);
                if (!CollectionUtils.isEmpty(reports) && !CollectionUtils.isEmpty(dashboardWiseReportIds)) {
                    for (RbReport report : reports) {
                        if (dashboardWiseReportIds.contains(report.getId())) {
                            reportDataBeans.add(this.convertReportModelToMasterReportDataBean(report, null));
                        }
                    }
                }
            }
            return reportDataBeans;
        }

        return null;
    }

    public Map<String, String> retrieveAnalyticsCredentials() {
        List<HkSystemConfigurationEntity> systemConfigurationEntityList = hkFoundationService.retrieveSystemConfigurationByFranchise(hkLoginDataBean.getCompanyId());
        Map<String, String> systemConfigMap = new HashMap<>();
        if (systemConfigurationEntityList != null && !systemConfigurationEntityList.isEmpty()) {
            for (HkSystemConfigurationEntity systemConfigurationEntity : systemConfigurationEntityList) {
                String key = systemConfigurationEntity.getHkSystemConfigurationEntityPK().getSystemKey();
                if (key != null && key.equals(HkSystemConstantUtil.FranchiseConfiguration.ANALYTICS_ENGINE_USERNAME)) {
                    systemConfigMap.put(systemConfigurationEntity.getHkSystemConfigurationEntityPK().getSystemKey(), systemConfigurationEntity.getKeyValue());
                }
                if (key != null && key.equals(HkSystemConstantUtil.FranchiseConfiguration.ANALYTICS_ENGINE_PWD)) {
                    systemConfigMap.put(systemConfigurationEntity.getHkSystemConfigurationEntityPK().getSystemKey(), AesEncrypter.decrypt(systemConfigurationEntity.getKeyValue()));
                }
                if (key != null && key.equals(HkSystemConstantUtil.FranchiseConfiguration.ANALYTICS_SERVER_URL)) {
                    systemConfigMap.put(systemConfigurationEntity.getHkSystemConfigurationEntityPK().getSystemKey(), systemConfigurationEntity.getKeyValue());
                }
            }
        }
        return systemConfigMap;
    }

    private static boolean isContain(String source, String subItem) {
        String pattern = "\\b" + subItem + "\\b";
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(source);
        return m.find();
    }

    /**
     *
     */
    public void generateTimeBasedReportEmailNotification(RbEmailReportConfigurationEntity emailConfiguration) throws GenericDatabaseException {
        System.out.println("-------------In Report Email Notification transformer-----------------");

        Calendar todayCal = Calendar.getInstance();
//        todayCal.set(Calendar.HOUR_OF_DAY, 0);
//        todayCal.set(Calendar.MINUTE, 0);
//        todayCal.set(Calendar.SECOND, 0);
//        todayCal.set(Calendar.MILLISECOND, 0);

        /* Check if reports need to be generated */
        List<RbEmailReportStatusEntity> reportEmailStatusEntities = new ArrayList<>();
        if (emailConfiguration != null) {
            if (emailConfiguration.getEndRepeatMode().equals(HkSystemConstantUtil.TaskEndRepeatMode.AFTER_X_REPETITIONS)
                    || emailConfiguration.getEndRepeatMode().equals(HkSystemConstantUtil.TaskEndRepeatMode.AFTER_X_DAYS)) {
                if (emailConfiguration.getRepetitionCnt() == null) {
                    emailConfiguration.setRepetitionCnt(0);
                }
            }
            Boolean isGenerate = this.checkReportEmailGeneration(emailConfiguration, todayCal);
            if (isGenerate) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(emailConfiguration.getAtTime());
                Calendar clone = (Calendar) todayCal.clone();
                clone.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY));
                clone.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE));
                clone.set(Calendar.SECOND, calendar.get(Calendar.SECOND));
                clone.set(Calendar.MILLISECOND, calendar.get(Calendar.MILLISECOND));
                System.out.println("todayCal.compareTo(clone)----" + todayCal.compareTo(clone));
                List<RbEmailReportStatusEntity> emailStatusEntitys = rbReportService.retrieveReportEmailStatus(emailConfiguration.getCreatedBy(), null, emailConfiguration.getId());
                if (todayCal.compareTo(clone) <= 0) {

                    boolean isSameConfigurationExists = false;

                    if (!CollectionUtils.isEmpty(emailStatusEntitys)) {

                        for (RbEmailReportStatusEntity emailStatusEntity : emailStatusEntitys) {
                            System.out.println("emailStatusEntity.getRbEmailReportStatusEntityPK().getOnTime()---" + emailStatusEntity.getRbEmailReportStatusEntityPK().getOnTime());
                            System.out.println("clone.getTime()---" + clone.getTime());
                            System.out.println("emailStatusEntity.getRbEmailReportStatusEntityPK().getOnTime().compareTo(clone.getTime())" + emailStatusEntity.getRbEmailReportStatusEntityPK().getOnTime().compareTo(clone.getTime()));
                            if (emailStatusEntity.getRbEmailReportStatusEntityPK().getOnTime().compareTo(clone.getTime()) == 0) {
                                isSameConfigurationExists = true;
                                emailStatusEntity.setEmailSent(false);
                                emailStatusEntity.setStatus(HkSystemConstantUtil.ReportEmailStatus.PENDING);
                                emailStatusEntity.setGeneratedOn(new Date());
                            } else {
                                if (emailStatusEntity.getStatus().equals(HkSystemConstantUtil.ReportEmailStatus.PENDING)) {
                                    emailStatusEntity.setStatus(HkSystemConstantUtil.ReportEmailStatus.EXPIRED);
                                }
                            }
                            reportEmailStatusEntities.add(emailStatusEntity);
                        }
                        System.out.println("isSameConfigurationExists-----" + isSameConfigurationExists);
                    }
                    if (!isSameConfigurationExists) {
                        RbEmailReportStatusEntity emailStatusEntity = new RbEmailReportStatusEntity();
                        RbEmailReportStatusEntityPK emailStatusPk = new RbEmailReportStatusEntityPK(emailConfiguration.getId(), clone.getTime(), emailConfiguration.getCreatedBy());
                        emailStatusEntity.setRbEmailReportStatusEntityPK(emailStatusPk);
                        emailStatusEntity.setEmailSent(false);
                        emailStatusEntity.setGeneratedOn(new Date());
                        emailStatusEntity.setStatus(HkSystemConstantUtil.ReportEmailStatus.PENDING);
                        reportEmailStatusEntities.add(emailStatusEntity);
                    }
                } else {
                    if (!CollectionUtils.isEmpty(emailStatusEntitys)) {
                        for (RbEmailReportStatusEntity emailStatusEntity : emailStatusEntitys) {
                            //Expire all the scheduled mails. New mails will be generated on mid-night for next day.
                            if (emailStatusEntity.getStatus().equals(HkSystemConstantUtil.ReportEmailStatus.PENDING)) {
                                emailStatusEntity.setStatus(HkSystemConstantUtil.ReportEmailStatus.EXPIRED);
                            }
                            reportEmailStatusEntities.add(emailStatusEntity);
                        }
                    }
                }
            }
        }
        if (!CollectionUtils.isEmpty(reportEmailStatusEntities)) {
            rbReportService.saveEmailStatusEntities(reportEmailStatusEntities);
        }

        /* End of check */
        System.out.println("-------------End of report email Notification transformer------------------");
    }

    public Boolean checkReportEmailGeneration(RbEmailReportConfigurationEntity emailNotificationConfigurationEntity, Calendar notificationCal) {
        boolean result = false;
        Calendar dueDate = Calendar.getInstance();
        //  Set the duedate to current date first
        dueDate.setTimeInMillis(notificationCal.getTimeInMillis());
        //  if repeatation mode is after x repetitions and if the after units are over i.e. all the repetations are generated,
        //  if rep mode is after x days and if 
        //  if rep mode is on date and if the date is gone,
        //  return null
        //  Task should not be weekly, and if end repeat mode is after x reps and after units is <= actual rep count
        if ((!emailNotificationConfigurationEntity.getRepeatativeMode().equals(HkSystemConstantUtil.TaskRepetitiveMode.WEEKLY)
                && emailNotificationConfigurationEntity.getEndRepeatMode().equals(HkSystemConstantUtil.TaskEndRepeatMode.AFTER_X_REPETITIONS)
                && emailNotificationConfigurationEntity.getAfterUnits() <= emailNotificationConfigurationEntity.getRepetitionCnt())
                || (emailNotificationConfigurationEntity.getEndRepeatMode().equals(HkSystemConstantUtil.TaskEndRepeatMode.ON_DATE)
                && emailNotificationConfigurationEntity.getEndDate().before(notificationCal.getTime()))) {
            return result;
        }
        if (emailNotificationConfigurationEntity.getEndRepeatMode().equals(HkSystemConstantUtil.TaskEndRepeatMode.AFTER_X_DAYS)) {
            int passedDays = (int) ((notificationCal.getTime().getTime() - emailNotificationConfigurationEntity.getCreatedOn().getTime()) / (1000 * 60 * 60 * 24));
            if (passedDays >= emailNotificationConfigurationEntity.getAfterUnits()) {
                return result;
            }
        }
        if (emailNotificationConfigurationEntity.getRepeatativeMode().equals(HkSystemConstantUtil.TaskRepetitiveMode.DAILY)) {
            result = true;
        } else if (emailNotificationConfigurationEntity.getRepeatativeMode().equals(HkSystemConstantUtil.TaskRepetitiveMode.WEEKLY)) {
            String[] onDays = emailNotificationConfigurationEntity.getWeeklyOnDays().split("\\" + HkSystemConstantUtil.SEPARATOR_PI);
            int countCheck = 0;
            if (emailNotificationConfigurationEntity.getEndRepeatMode().equals(HkSystemConstantUtil.TaskEndRepeatMode.AFTER_X_REPETITIONS)) {
                Date lstModifiedOn = emailNotificationConfigurationEntity.getLastModifiedOn();
                Calendar calTemp = Calendar.getInstance();
                calTemp.setTime(lstModifiedOn);
                calTemp.set(Calendar.HOUR_OF_DAY, 0);
                calTemp.set(Calendar.MINUTE, 0);
                calTemp.set(Calendar.SECOND, 0);
                calTemp.set(Calendar.MILLISECOND, 0);
                Calendar c = Calendar.getInstance();
                c.setTime(lstModifiedOn);
                Integer dayOfWeek = c.get(Calendar.DAY_OF_WEEK);

                int checkCountOfWeek = 0;
                for (int count = 0; count < onDays.length; count++) {
                    if (onDays[count].equals(dayOfWeek.toString())) {
                        checkCountOfWeek++;
                    }
                }

                if (checkCountOfWeek > 0) {
                    countCheck = (emailNotificationConfigurationEntity.getAfterUnits() * onDays.length) - 1;
                } else {
                    countCheck = (emailNotificationConfigurationEntity.getAfterUnits() * onDays.length);
                }
            }

            if (emailNotificationConfigurationEntity.getEndRepeatMode().equals(HkSystemConstantUtil.TaskEndRepeatMode.AFTER_X_REPETITIONS)
                    && (countCheck) <= emailNotificationConfigurationEntity.getRepetitionCnt()) {
                return false;
            }
            for (int count = 0; count < onDays.length; count++) {
                if (onDays[count].equals(Integer.toString(notificationCal.get(Calendar.DAY_OF_WEEK)))) {
                    int diff = 0;
                    if (count + 1 == onDays.length) {
                        diff = 7 - Integer.valueOf(onDays[count]) + Integer.valueOf(onDays[0]) - 1;
                        if (diff >= 7) {
                            diff = diff - 7;
                        }
                    } else {
                        diff = Integer.valueOf(onDays[count + 1]) - Integer.valueOf(onDays[count]) - 1;
                        if (diff < 0) {
                            diff = 7 + diff;
                        }
                    }
                    dueDate.add(Calendar.DATE, diff);
                    result = true;
                    break;
                }
            }
            //  logic: e.g.- weekdays = {4,7}, onDate=25/2/2014 i.e. Thursday (Day=4) today
            //  so as per (after) this loop, nextDay = 6
            //  previousDay = 4
            //  so days to be added = 6-4=2
            //  thus, due date set would be, 25+2 = 27/2/2014 i.e. Saturday (Day=7)

        } else if (emailNotificationConfigurationEntity.getRepeatativeMode().equals(HkSystemConstantUtil.TaskRepetitiveMode.MONTHLY)) {
            Integer monthDate = emailNotificationConfigurationEntity.getMonthlyOnDay();
            if (notificationCal.get(Calendar.DATE) == monthDate) {
                //  if notificationConfigurationEntity gen date is same as the given month date, result true
                result = true;
            } else if (monthDate == 31 //  if given date is 31 and today's date is 30 and month is 4,6,9,11 then result = true
                    && (notificationCal.get(Calendar.DATE) == 30)
                    && (notificationCal.get(Calendar.MONTH) == Calendar.APRIL
                    || notificationCal.get(Calendar.MONTH) == Calendar.JUNE
                    || notificationCal.get(Calendar.MONTH) == Calendar.SEPTEMBER
                    || notificationCal.get(Calendar.MONTH) == Calendar.NOVEMBER)) {
                result = true;
            } else if (notificationCal.get(Calendar.MONTH) == Calendar.FEBRUARY //  if today's month is february
                    && monthDate > 28 && notificationCal.get(Calendar.DATE) >= 28) {
                if (notificationCal.get(Calendar.YEAR) % 4 != 0 //  if it's not a leap year and date is 28, result = true where given month date > 28
                        || (notificationCal.get(Calendar.YEAR) % 4 == 0 && notificationCal.get(Calendar.DATE) == 29)) {   //  if leap year, and date 29, and monthdate > 28, result = true
                    result = true;
                }
            }
        }

        return result;
    }

    private class SubreportExpression extends AbstractSimpleExpression<JasperReportBuilder> {

        private static final long serialVersionUID = 1L;
        private String title;
        private List<TextColumnBuilder> columns;
        private List<AggregationSubtotalBuilder> subTotalColumnBuilerList;

        public SubreportExpression(String title, List<TextColumnBuilder> columns, List<AggregationSubtotalBuilder> subTotalColumnBuilerList) {
            this.title = title;
            this.columns = columns;
            this.subTotalColumnBuilerList = subTotalColumnBuilerList;
        }

        @Override
        public JasperReportBuilder evaluate(ReportParameters reportParameters) {
            TextColumnBuilder[] textColumnBuilders = columns.toArray(new TextColumnBuilder[columns.size()]);
            AggregationSubtotalBuilder[] subTotals = subTotalColumnBuilerList.toArray(new AggregationSubtotalBuilder[subTotalColumnBuilerList.size()]);
            JasperReportBuilder report = report();
            report.setTemplate(ReportBuilderTemplate.reportTemplate)
                    .title(ReportBuilderTemplate.createTitleComponent(null, title, null, "CENTER", Boolean.FALSE));
            report.addColumn(textColumnBuilders);
            if (!CollectionUtils.isEmpty(subTotalColumnBuilerList)) {
                report.subtotalsAtSummary(subTotals);
            }

            return report;
        }
    }

}
