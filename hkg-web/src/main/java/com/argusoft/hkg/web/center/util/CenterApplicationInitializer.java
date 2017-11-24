/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.center.util;

import com.argusoft.hkg.common.constantutil.HkSystemConstantUtil;
import static com.argusoft.hkg.common.constantutil.HkSystemConstantUtil.RAP_CALC_FIELD_MAP;
import static com.argusoft.hkg.common.constantutil.HkSystemConstantUtil.RAP_CALC_FOURC_VALUE_MAP;
import com.argusoft.hkg.common.functionutil.FolderManagement;
import com.argusoft.hkg.nosql.core.HkCustomFieldService;
import com.argusoft.hkg.nosql.core.HkFeatureService;
import com.argusoft.hkg.nosql.core.HkFoundationDocumentService;
import com.argusoft.hkg.nosql.core.HkRapCalcService;
import com.argusoft.hkg.nosql.model.HkCalcMasterDocument;
import com.argusoft.hkg.sync.center.core.HkUMSyncService;
import com.argusoft.hkg.web.center.sync.transformers.SyncCenterFranchiseTransformer;
import com.argusoft.hkg.web.center.usermanagement.databeans.UMFeatureDetailDataBean;
import com.argusoft.hkg.web.config.WebApplicationInitializerConfig;
import com.argusoft.sync.center.model.CenterCompanyFeatureSectionDocument;
import com.argusoft.sync.center.model.SyncCenterFeatureDocument;
import com.argusoft.sync.center.model.UmDesignationDocument;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 *
 * @author akta
 */
@Service
public class CenterApplicationInitializer {

//    private Map<String, Map<String, Object>> featureFromTemplateMap;
    private static final Logger LOGGER = LoggerFactory.getLogger(CenterApplicationInitializer.class);
    @Autowired
    private HkCustomFieldService customFieldService;
    @Autowired
    private HkFeatureService hkFeatureService;
    @Autowired
    private ApplicationUtil applicationUtil;
    @Autowired
    private HkUMSyncService hkUMSyncService;
    @Autowired
    private SyncCenterFranchiseTransformer centerFranchiseTransformer;
    @Autowired
    HkFeatureService featureService;

    @Autowired
    private HkRapCalcService calcService;

    @Autowired
    HkFoundationDocumentService foundationDocumentService;
//    private Map<String, String> systemConfigrationMap;

//    public Map<String, Map<String, Object>> getFeatureFromTemplateMap() {
//        return featureFromTemplateMap;
//    }
//
//    public void setFeatureFromTemplateMap(Map<String, Map<String, Object>> featureFromTemplateMap) {
//        this.featureFromTemplateMap = featureFromTemplateMap;
//    }
//
//    public Map<String, String> getSystemConfigrationMap() {
//        return systemConfigrationMap;
//    }
//
//    public void setSystemConfigrationMap(Map<String, String> systemConfigrationMap) {
//        this.systemConfigrationMap = systemConfigrationMap;
//    }
    @PostConstruct
    public void initCenterApplicationUtil() {

        if (!WebApplicationInitializerConfig.IS_MASTER) {
            LOGGER.debug("========== Center Init call ===========");
            CenterCompanyFeatureSectionDocument centerCompanyFeatureSectionDocument;
            centerCompanyFeatureSectionDocument = customFieldService.retrieveCenterCompanyFeatureSectionDocumentById(1l);
            if (centerCompanyFeatureSectionDocument != null && centerCompanyFeatureSectionDocument.getFeatureSectionMap() != null) {
                applicationUtil.setCenterFeatureFromTemplateMap(centerCompanyFeatureSectionDocument.getFeatureSectionMap());
            }
            //Creating Base Folder for the System
            FolderManagement.createBaseFolder();
            applicationUtil.setRolePrecedence(retrieveDesignationPrecedence());
            initFeatureDetail();
            applicationUtil.setCenterFranchiseDataBean(centerFranchiseTransformer.retrieveById(1l));
            //START: Rap calc field initialization
            initRapCalc();
            //END: Rap calc field initialization
            
            Map<String, Long> featureNameIdMap = new HashMap<>();
            List<SyncCenterFeatureDocument> menuList = featureService.retrieveAllFeaturesByListOfMenuType(Arrays.asList("DM","DMI"), Boolean.TRUE, null);
            for (SyncCenterFeatureDocument menuItem : menuList) {
                featureNameIdMap.put(menuItem.getName(), menuItem.getId());
            }
            applicationUtil.setFeatureNameIdMap(featureNameIdMap);
            System.out.println("applicationUtil.setFeatureNameIdMap(featureNameIdMap); " + applicationUtil.getFeatureNameIdMap());

        }

    }

    public void updateCustomFieldVersion() {
        Map<String, String> systemConfigrationMap = applicationUtil.getSystemConfigrationMap();
        if (systemConfigrationMap == null) {
            systemConfigrationMap = new HashMap<>();
        }
        Integer customFieldVersion = 1;
        if (systemConfigrationMap.get(HkSystemConstantUtil.CUSTOM_FIELD_VERSION) != null) {
            customFieldVersion = Integer.parseInt(systemConfigrationMap.get(HkSystemConstantUtil.CUSTOM_FIELD_VERSION));
            customFieldVersion++;
        }
        systemConfigrationMap.put(HkSystemConstantUtil.CUSTOM_FIELD_VERSION, customFieldVersion.toString());
        applicationUtil.setSystemConfigrationMap(systemConfigrationMap);
    }

    public Map<Long, Integer> retrieveDesignationPrecedence() {

        //search.addField(UMRoleDao.PRECEDENCE);
        Map<Long, Integer> map = new HashMap<>();
        List<UmDesignationDocument> roles = (List<UmDesignationDocument>) hkUMSyncService.retrieveAllDocuments(UmDesignationDocument.class);
        for (UmDesignationDocument designationDocument : roles) {
            map.put(designationDocument.getId(), designationDocument.getPrecedence());
        }
        return map;
    }

    private void initFeatureDetail() {
        List<SyncCenterFeatureDocument> centerFeatureDocuments = hkFeatureService.retrieveAllFeatures(Boolean.TRUE);
        if (!CollectionUtils.isEmpty(centerFeatureDocuments)) {
            Map<String, UMFeatureDetailDataBean> uMFeatureDetailDataBeanMap = new HashMap<>();
            for (SyncCenterFeatureDocument centerFeatureDocument : centerFeatureDocuments) {
                UMFeatureDetailDataBean featureDetailDataBean = new UMFeatureDetailDataBean();
                featureDetailDataBean.setId(centerFeatureDocument.getId());
                featureDetailDataBean.setDescription(centerFeatureDocument.getDescription());
                featureDetailDataBean.setDisplayName(centerFeatureDocument.getMenuLabel());
                featureDetailDataBean.setFeatureName(centerFeatureDocument.getName());
                featureDetailDataBean.setFeatureURL(centerFeatureDocument.getFeatureURL());
                featureDetailDataBean.setIsCrud(centerFeatureDocument.getIsCrud());
                featureDetailDataBean.setMenuCategory(centerFeatureDocument.getMenuCategory());
                featureDetailDataBean.setMenuLabel(centerFeatureDocument.getMenuLabel());
                featureDetailDataBean.setMenuType(centerFeatureDocument.getMenuType());
                featureDetailDataBean.setParentId(centerFeatureDocument.getParentId());
                featureDetailDataBean.setParentName(centerFeatureDocument.getParentName());
                featureDetailDataBean.setPrecedence(centerFeatureDocument.getPrecedence());
                featureDetailDataBean.setSeqNo(centerFeatureDocument.getSeqNo());
                featureDetailDataBean.setWebserviceUrl(centerFeatureDocument.getWebserviceUrl());
                uMFeatureDetailDataBeanMap.put(featureDetailDataBean.getFeatureName(), featureDetailDataBean);
            }
            applicationUtil.setuMFeatureDetailDataBeanMap(uMFeatureDetailDataBeanMap);
        }
    }

    private void initRapCalc() {
        SyncCenterFeatureDocument roughcalcfeature = featureService.retireveFeatureByName(HkSystemConstantUtil.Feature.ROUGHCALC);
        List<Long> featureList = new LinkedList<>();
        if (roughcalcfeature != null) {
            featureList.add(roughcalcfeature.getId());
        }
        Map< String, String> customfieldMap = foundationDocumentService.retrieveCustomfieldMasterMappingForCalc(featureList);
        if (!CollectionUtils.isEmpty(customfieldMap)) {
            HkSystemConstantUtil.RAP_CALC_FIELD_MAP = customfieldMap;
        }
        if (!CollectionUtils.isEmpty(RAP_CALC_FIELD_MAP)) {
            String masterCode = RAP_CALC_FIELD_MAP.get(HkSystemConstantUtil.FourC.COLOR);
            RAP_CALC_FOURC_VALUE_MAP.put(HkSystemConstantUtil.FourC.COLOR, getSortedValueListByMasterCode(masterCode));

            masterCode = RAP_CALC_FIELD_MAP.get(HkSystemConstantUtil.FourC.SHAPE);
            RAP_CALC_FOURC_VALUE_MAP.put(HkSystemConstantUtil.FourC.SHAPE, getSortedValueListByMasterCode(masterCode));

            masterCode = RAP_CALC_FIELD_MAP.get(HkSystemConstantUtil.FourC.QUALITY);
            RAP_CALC_FOURC_VALUE_MAP.put(HkSystemConstantUtil.FourC.QUALITY, getSortedValueListByMasterCode(masterCode));

            System.out.println("RAP_CALC_FOURC_VALUE_MAP : " + RAP_CALC_FOURC_VALUE_MAP);
        }
    }

    public List<Long> getSortedValueListByMasterCode(String masterCode) {
        List<Long> ids = null;
        if (masterCode != null) {
            List<HkCalcMasterDocument> masterDocuments = calcService.getCalcMastersyMasterCode(masterCode);
            if (!CollectionUtils.isEmpty(masterDocuments)) {
                ids = new LinkedList<>();
                Collections.sort(masterDocuments);
                for (HkCalcMasterDocument master : masterDocuments) {
                    ids.add(master.getMasterValueId());
                }
            }
        }
        return ids;
    }
}
