/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.reportbuilder.wrapper;

import com.argusoft.generic.database.exception.GenericDatabaseException;
import com.argusoft.hkg.common.constantutil.HkSystemConstantUtil;
import com.argusoft.hkg.web.internationalization.LabelType;
import com.argusoft.hkg.web.usermanagement.databeans.FeatureDataBean;
import com.argusoft.hkg.web.center.usermanagement.databeans.LoginDataBean;
import com.argusoft.hkg.web.usermanagement.transformers.FeatureTransformerBean;
import com.argusoft.hkg.web.usermanagement.wrapper.UserManagementServiceWrapper;
import com.argusoft.hkg.web.util.ApplicationMasterInitializer;
import com.argusoft.internationalization.common.core.I18nService;
import com.argusoft.internationalization.common.model.I18nLabelEntity;
import com.argusoft.internationalization.common.model.I18nLabelPKEntity;
import com.argusoft.reportbuilder.core.RbReportService;
import com.argusoft.reportbuilder.model.RbReport;
import com.argusoft.reportbuilder.model.RbReportField;
import com.argusoft.usermanagement.common.core.UMRoleService;
import com.argusoft.usermanagement.common.model.UMFeature;
import com.argusoft.usermanagement.common.model.UMRole;
import com.argusoft.usermanagement.common.model.UMRoleFeature;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

/**
 *
 * @author Gautam
 */
@Service
@Transactional
public class ReportBuliderServiceWrapper {

    @Autowired
    LoginDataBean hkLoginDataBean;

    @Autowired
    RbReportService rbReportService;

    @Autowired
    I18nService i18nService;

    @Autowired
    UMRoleService roleService;
    
    @Autowired
    private ApplicationMasterInitializer applicationMasterInitializer;

    @Autowired
    UserManagementServiceWrapper userManagementServiceWrapper;

    @Autowired
    FeatureTransformerBean featureTransformerBean;

    /**
     * Create report and also add a feature for the same.
     *
     * @param rbReport
     * @param rbReportFieldList
     * @param companyId
     * @param modifiedBy
     * @return
     * @throws GenericDatabaseException
     */
    public Long createReport(RbReport rbReport, List<RbReportField> rbReportFieldList, Long companyId, Long modifiedBy) throws GenericDatabaseException {

        FeatureDataBean hkSystemFeaturesDatabean = new FeatureDataBean();
        hkSystemFeaturesDatabean.setFeatureName("rb_" + rbReport.getReportName().replaceAll(" ", ""));
        hkSystemFeaturesDatabean.setMenuLabel(rbReport.getReportName());
        hkSystemFeaturesDatabean.setDescription(rbReport.getReportGroup() == null ? null:rbReport.getReportGroup().toString());
        hkSystemFeaturesDatabean.setIsCrud(false);
        hkSystemFeaturesDatabean.setIsActive(true);
        hkSystemFeaturesDatabean.setMenuCategory("report");
        hkSystemFeaturesDatabean.setMenuType(HkSystemConstantUtil.ReportFeatureTypes.MENU_ITEM);
        hkSystemFeaturesDatabean.setPrecedence(2);

        Long featureId = featureTransformerBean.createFeature(hkSystemFeaturesDatabean);

        if (featureId != null) {
            rbReport.setCustom1(featureId);
        }
        rbReportService.createAllReportInformation(rbReport, rbReportFieldList);
        UMRole hkAdminRole = userManagementServiceWrapper.retrieveRole(HkSystemConstantUtil.ROLE.HK_ADMIN, null, true,hkLoginDataBean.getPrecedence());
        if (hkAdminRole != null && featureId != null) {
            Set<UMRoleFeature> umRoleFeatureSet = hkAdminRole.getUMRoleFeatureSet();
            if (!CollectionUtils.isEmpty(umRoleFeatureSet)) {
                UMRoleFeature roleFeature = new UMRoleFeature();
                roleFeature.setFeature(new UMFeature(featureId));
                roleFeature.setRole(hkAdminRole);
                roleFeature.setAllowToCreate(true);
                roleFeature.setAllowToDelete(true);
                roleFeature.setAllowToUpdate(true);
                roleFeature.setIsActive(true);
                roleFeature.setIsArchive(false);
                roleFeature.setCompany(hkLoginDataBean.getCompanyId());
                umRoleFeatureSet.add(roleFeature);
                hkAdminRole.setUMRoleFeatureSet(umRoleFeatureSet);
                userManagementServiceWrapper.updateDesignation(hkAdminRole, true);
                applicationMasterInitializer.initDesignations();
            }

        }
        this.createLocaleEntityForReport(rbReport, rbReportFieldList, companyId, modifiedBy);
//        userManagementServiceWrapper.createLocaleForEntity(rbReport.getReportName(), "Feature", hkLoginDataBean.getId(), hkLoginDataBean.getCompanyId());
        return rbReport.getId();
    }

    public void createLocaleEntityForReport(RbReport rbReport, List<RbReportField> rbReportFieldList, Long companyId, Long modifiedBy) {
        if (!CollectionUtils.isEmpty(rbReportFieldList)) {
            List<I18nLabelEntity> i18nLabelEntitys = new ArrayList<>();

            I18nLabelEntity i18nLabelForReportEntity = new I18nLabelEntity();
            I18nLabelPKEntity i18nLabelForReportPkEntity = new I18nLabelPKEntity();
            i18nLabelForReportPkEntity.setCompany(companyId);
            i18nLabelForReportPkEntity.setCountry(HkSystemConstantUtil.COUNTRY_NZ);
            i18nLabelForReportPkEntity.setEntity("Menu");
            i18nLabelForReportPkEntity.setKey(rbReport.getReportName());
            i18nLabelForReportPkEntity.setType(LabelType.LABEL.toString());
            i18nLabelForReportEntity.setLabelPK(i18nLabelForReportPkEntity);
            i18nLabelForReportEntity.setLastModifiedBy(modifiedBy);
            i18nLabelForReportEntity.setLastModifiedOn(new Date());
            i18nLabelForReportEntity.setText(rbReport.getReportName());
            i18nLabelForReportEntity.setTranslationPending(Boolean.FALSE);
            i18nLabelForReportEntity.setEnvironment("w");
            i18nLabelEntitys.add(i18nLabelForReportEntity);

            for (RbReportField rbReportField : rbReportFieldList) {
                I18nLabelEntity i18nLabelEntity = new I18nLabelEntity();
                I18nLabelPKEntity i18nLabelPKEntity = new I18nLabelPKEntity();
                i18nLabelPKEntity.setCompany(companyId);
                i18nLabelPKEntity.setCountry(HkSystemConstantUtil.COUNTRY_NZ);
                i18nLabelPKEntity.setEntity(HkSystemConstantUtil.I18N_CONTENT_TYPE_MAP.get("report"));
                i18nLabelPKEntity.setKey(rbReportField.getReportFieldName());
                i18nLabelPKEntity.setType(LabelType.REPORT.toString());
                i18nLabelEntity.setLabelPK(i18nLabelPKEntity);
                i18nLabelEntity.setLastModifiedBy(modifiedBy);
                i18nLabelEntity.setLastModifiedOn(new Date());
                i18nLabelEntity.setText(rbReportField.getReportFieldName());
                i18nLabelEntity.setTranslationPending(Boolean.FALSE);
                i18nLabelEntity.setEnvironment("w");
                i18nLabelEntitys.add(i18nLabelEntity);
            }
            //System.out.println("i18nLabelEntitys : "+i18nLabelEntitys);
            i18nService.addBulkLabels(i18nLabelEntitys);
        }
    }

    /**
     * Update the report and archive both feature and report in case report have
     * archived.
     *
     * @param rbReport
     * @return
     * @throws GenericDatabaseException
     */
    public Long updateReportAndFeature(RbReport rbReport) throws GenericDatabaseException {

        Long featureId = null;
        UMFeature umFeature = null;
        if (rbReport.getCustom1() != null) {
            featureId = rbReport.getCustom1();
            if (featureId != null) {
                umFeature = featureTransformerBean.retrieveFeatureByFeatureId(featureId);
            }
        }

        if (!rbReport.getIsActive() || rbReport.getIsArchive()) {
            if (umFeature != null) {
                umFeature.setIsActive(false);
                umFeature.setIsArchive(true);
                umFeature.setModifiedBy(hkLoginDataBean.getId());
                umFeature.setModifiedOn(new Date());
            }
        } else {
            if (umFeature == null) {
                FeatureDataBean hkSystemFeaturesDatabean = new FeatureDataBean();
                hkSystemFeaturesDatabean.setFeatureName("rb_" + rbReport.getReportName().replaceAll(" ", ""));
                hkSystemFeaturesDatabean.setMenuLabel(rbReport.getReportName());
                hkSystemFeaturesDatabean.setDescription(rbReport.getReportGroup() == null ? null:rbReport.getReportGroup().toString());
                hkSystemFeaturesDatabean.setIsCrud(false);
                hkSystemFeaturesDatabean.setIsActive(true);
                hkSystemFeaturesDatabean.setMenuCategory("report");
                hkSystemFeaturesDatabean.setMenuType(HkSystemConstantUtil.ReportFeatureTypes.MENU_ITEM);
                hkSystemFeaturesDatabean.setPrecedence(2);

                Long id = featureTransformerBean.createFeature(hkSystemFeaturesDatabean);

                if (id != null) {
                    rbReport.setCustom1(id);
                }
            } else {
                umFeature.setName("rb_" + rbReport.getReportName().replaceAll(" ", ""));
                umFeature.setMenuLabel(rbReport.getReportName());
                umFeature.setDescription(rbReport.getReportGroup() == null ? null:rbReport.getReportGroup().toString());
                umFeature.setModifiedBy(hkLoginDataBean.getId());
                umFeature.setMenuCategory("report");
                umFeature.setModifiedOn(new Date());
                umFeature.setIsActive(true);
                umFeature.setIsArchive(false);
                userManagementServiceWrapper.createLocaleForEntity(umFeature.getMenuLabel(), "Feature", hkLoginDataBean.getId(), hkLoginDataBean.getCompanyId());
            }
        }
        rbReportService.updateReport(rbReport);
        if (!CollectionUtils.isEmpty(rbReport.getRbReportFieldSet())) {
//            this.createLocaleEntityForReport(rbReport, rbReport.getRbReportFieldSet(), hkLoginDataBean.getCompanyId(), hkLoginDataBean.getId());
        }
        return rbReport.getId();
    }
}
