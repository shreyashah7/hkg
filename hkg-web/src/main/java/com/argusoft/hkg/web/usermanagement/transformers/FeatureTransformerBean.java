/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.usermanagement.transformers;

import com.argusoft.generic.database.common.GenericDao;
import com.argusoft.generic.database.exception.GenericDatabaseException;
import com.argusoft.hkg.common.constantutil.HkSystemConstantUtil;
import com.argusoft.hkg.web.usermanagement.databeans.FeatureDataBean;
import com.argusoft.hkg.web.center.usermanagement.databeans.LoginDataBean;
import com.argusoft.hkg.web.center.usermanagement.databeans.UMFeatureDetailDataBean;
import com.argusoft.hkg.web.center.util.ApplicationUtil;
import com.argusoft.hkg.web.usermanagement.wrapper.UserManagementServiceWrapper;
import com.argusoft.usermanagement.common.core.UMFeatureService;
import com.argusoft.usermanagement.common.database.UMFeatureDao;
import com.argusoft.usermanagement.common.model.UMFeature;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 *
 * @author piyush
 */
@Service
public class FeatureTransformerBean {

    @Autowired
    private UMFeatureService featureService;
    
    @Autowired
    UserManagementServiceWrapper userManagementServiceWrapper;
   
    @Autowired
    private LoginDataBean loginDataBean;
    
    @Autowired
    ApplicationUtil applicationUtil;

    public UMFeature convertFeatureDatabeanToFeature(FeatureDataBean hkFeatureDatabean, UMFeature feature, String operation) {

        if (operation.equalsIgnoreCase(HkSystemConstantUtil.CREATE_OPERATION)) {
            feature = new UMFeature();
            feature.setCreatedBy(loginDataBean.getId());
            feature.setCreatedOn(new Date());
            feature.setModifiedBy(loginDataBean.getId());
            feature.setModifiedOn(new Date());
        } else if (operation.equalsIgnoreCase(HkSystemConstantUtil.UPDATE_OPERATION)) {
            feature.setModifiedBy(loginDataBean.getId());
            feature.setModifiedOn(new Date());
        }
        feature.setName(hkFeatureDatabean.getFeatureName());
        feature.setMenuLabel(hkFeatureDatabean.getMenuLabel());
        feature.setDescription(hkFeatureDatabean.getDescription());
        feature.setPrecedence(hkFeatureDatabean.getPrecedence());
        feature.setMenuType(hkFeatureDatabean.getMenuType());
        feature.setSeqNo(hkFeatureDatabean.getSeqNo());
        feature.setFeatureUrl(hkFeatureDatabean.getFeatureURL());

        if (hkFeatureDatabean.getParentId() == null || (hkFeatureDatabean.getParentId() != null && hkFeatureDatabean.getParentId().equals(0L))) {
            feature.setParent(null);
        } else {
            feature.setParent(new UMFeature(hkFeatureDatabean.getParentId()));
        }

        feature.setIsCrud(Boolean.valueOf(hkFeatureDatabean.getIsCrud()));
        feature.setCompany(loginDataBean.getCompanyId());
        feature.setIsActive(Boolean.valueOf(hkFeatureDatabean.getIsActive()));
        feature.setIsArchive(!Boolean.valueOf(hkFeatureDatabean.getIsActive()));
        feature.setWebserviceUrl(hkFeatureDatabean.getWebserviceUrl());
        feature.setMenuCategory(hkFeatureDatabean.getMenuCategory());

        return feature;
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

    public List<FeatureDataBean> retrieveSystemFeaturesByListOfType(List<String> menuTypeVals, Long companyId, boolean isActive) {
        if (companyId == null) {
            companyId = loginDataBean.getCompanyId();
        }
        List<FeatureDataBean> featureDatabeans = new ArrayList<>();
        List<UMFeature> retrieveAllFeaturesByListOfMenuType = featureService.retrieveAllFeaturesByListOfMenuType(menuTypeVals, companyId, isActive, true);
        for (UMFeature feature : retrieveAllFeaturesByListOfMenuType) {
            featureDatabeans.add(convertFeatureToFeatureDatabean(feature, new FeatureDataBean()));
        }
        return featureDatabeans;
    }

    public List<FeatureDataBean> retrieveAllFeaturesByListOfMenuType(List<String> menuTypeVals, Long companyId, boolean isActive, String search) {
        if (companyId == null) {
            companyId = loginDataBean.getCompanyId();
        }
        List<FeatureDataBean> featureDatabeans = new ArrayList<>();
        List<UMFeature> retrieveAllFeaturesByListOfMenuType = userManagementServiceWrapper.retrieveAllFeaturesByListOfMenuType(menuTypeVals, companyId, isActive, search);

        for (UMFeature feature : retrieveAllFeaturesByListOfMenuType) {
            featureDatabeans.add(convertFeatureToFeatureDatabean(feature, new FeatureDataBean()));
        }
        return featureDatabeans;
    }

    public Long createFeature(FeatureDataBean hkSystemFeaturesDatabean) throws GenericDatabaseException {
        UMFeature feature = null;
        feature = this.convertFeatureDatabeanToFeature(hkSystemFeaturesDatabean, feature, HkSystemConstantUtil.CREATE_OPERATION);
        Long id = featureService.createFeature(feature);
        userManagementServiceWrapper.createLocaleForEntity(feature.getMenuLabel(), "Feature", loginDataBean.getId(), loginDataBean.getCompanyId());
        return id;
    }

    public Boolean updateFeature(FeatureDataBean hkSystemFeaturesDatabean) {
        try {
            UMFeature feature = featureService.retrieveFeatureById(hkSystemFeaturesDatabean.getId(), null);
            if (feature.getName().equals(hkSystemFeaturesDatabean.getFeatureName())) {
                feature = this.convertFeatureDatabeanToFeature(hkSystemFeaturesDatabean, feature, HkSystemConstantUtil.UPDATE_OPERATION);
                featureService.updateFeature(feature);
                userManagementServiceWrapper.createLocaleForEntity(feature.getMenuLabel(), "Feature", loginDataBean.getId(), loginDataBean.getCompanyId());
                return Boolean.TRUE;
            } else {
                if (this.isFeatureNameExists(hkSystemFeaturesDatabean.getFeatureName(), loginDataBean.getCompanyId())) {
                    feature = this.convertFeatureDatabeanToFeature(hkSystemFeaturesDatabean, feature, HkSystemConstantUtil.UPDATE_OPERATION);
                    featureService.updateFeature(feature);
                    userManagementServiceWrapper.createLocaleForEntity(feature.getMenuLabel(), "Feature", loginDataBean.getId(), loginDataBean.getCompanyId());
                    return Boolean.TRUE;
                } else {
                    return Boolean.FALSE;
                }
            }
        } catch (GenericDatabaseException ex) {
            Logger.getLogger(FeatureTransformerBean.class.getName()).log(Level.SEVERE, null, ex);
            return Boolean.FALSE;
        }
    }

    public List<FeatureDataBean> retrieveFeaturesByStatusBySeqnoByCompany(Boolean status, Boolean seqNo, Long company) {
        try {
            Map<String, Object> equal = new HashMap<>();
            equal.put(UMFeatureDao.COMPANY, company);
            equal.put(UMFeatureDao.IS_ACTIVE, status);
            Map<GenericDao.QueryOperators, Object> criterias = new HashMap<>();
            criterias.put(GenericDao.QueryOperators.EQUAL, equal);
            List<String> order = new ArrayList<>();
            order.add(UMFeatureDao.SEQ_NO);
            criterias.put(GenericDao.QueryOperators.ORDER_ASC, order);
            List<UMFeature> features = featureService.retrieveFeatures(null, criterias, null);

            List<FeatureDataBean> featureDataBeans = null;
            if (!CollectionUtils.isEmpty(features)) {
                featureDataBeans = new ArrayList<>();
                for (UMFeature feature : features) {
                    featureDataBeans.add(convertFeatureToFeatureDatabean(feature, new FeatureDataBean()));
                }
            }
            return featureDataBeans;
        } catch (GenericDatabaseException ex) {
            Logger.getLogger(FeatureTransformerBean.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public Boolean isFeatureNameExists(String featureName, Long company) {
        try {
            Map<String, Object> equal = new HashMap<>();
            equal.put(UMFeatureDao.NAME, featureName);
            equal.put(UMFeatureDao.COMPANY, company);
            equal.put(UMFeatureDao.IS_ACTIVE, true);
            Map<GenericDao.QueryOperators, Object> criterias = new HashMap<>();
            criterias.put(GenericDao.QueryOperators.EQUAL, equal);
            List<UMFeature> features = featureService.retrieveFeatures(null, criterias, null);

            if (CollectionUtils.isEmpty(features)) {
                return Boolean.TRUE;
            }
        } catch (GenericDatabaseException ex) {
            Logger.getLogger(FeatureTransformerBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Boolean.FALSE;
    }

    public UMFeature retrieveFeatureByFeatureId(Long id) {
        try {
            return featureService.retrieveFeatureById(id, null);
        } catch (GenericDatabaseException ex) {
            Logger.getLogger(FeatureTransformerBean.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public List<UMFeature> retrieveFeaturesByParent(Long parentFeatureId) {
        try {
            Map<String, Object> equal = new HashMap<>();
            equal.put(UMFeatureDao.PARENT, new UMFeature(parentFeatureId));
            equal.put(UMFeatureDao.IS_ACTIVE, true);
            Map<GenericDao.QueryOperators, Object> criterias = new HashMap<>();
            criterias.put(GenericDao.QueryOperators.EQUAL, equal);
            List<UMFeature> features = featureService.retrieveFeatures(null, criterias, null);
            return features;
        } catch (GenericDatabaseException ex) {
            Logger.getLogger(FeatureTransformerBean.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public Map<String, List<FeatureDataBean>> retrieveSystemFeaturesByCategory(Long company) throws GenericDatabaseException {
        Map<String, Object> equal = new HashMap<>();
        equal.put(UMFeatureDao.COMPANY, company);
        equal.put(UMFeatureDao.IS_ACTIVE, Boolean.TRUE);

        Map<String, Object> in = new HashMap<>();
        in.put(UMFeatureDao.MENU_TYPE, Arrays.asList(HkSystemConstantUtil.FeatureTypes.MENU, HkSystemConstantUtil.FeatureTypes.DYNAMIC_MENU, HkSystemConstantUtil.FeatureTypes.DYNAMIC_ENTITY));
        Map<GenericDao.QueryOperators, Object> criterias = new HashMap<>();
        criterias.put(GenericDao.QueryOperators.EQUAL, equal);
        List<String> order = new ArrayList<>();
        order.add(UMFeatureDao.SEQ_NO);
        criterias.put(GenericDao.QueryOperators.ORDER_ASC, order);
        List<UMFeature> features = featureService.retrieveFeatures(null, criterias, null);
        Map<String, List<FeatureDataBean>> map = new HashMap();
        if (!CollectionUtils.isEmpty(features)) {
            for (UMFeature uMFeature : features) {
                FeatureDataBean dataBean = new FeatureDataBean();
                dataBean.setId(uMFeature.getId());
                dataBean.setDisplayName(uMFeature.getMenuLabel());
                if (uMFeature.getMenuCategory() == null || uMFeature.getMenuCategory().trim().length() == 0) {
                    List<FeatureDataBean> get = map.get("General");
                    if (get == null) {
                        get = new ArrayList<>();
                    }
                    get.add(dataBean);
                    map.put("General", get);
                } else {
                    List<FeatureDataBean> get = map.get(uMFeature.getMenuCategory());
                    if (get == null) {
                        get = new ArrayList<>();
                    }
                    get.add(dataBean);
                    map.put(uMFeature.getMenuCategory(), get);
                }
            }
        }
        return map;
    }

    public String saveSequencing(List<Long> featureIds) throws GenericDatabaseException {
        String response = HkSystemConstantUtil.FAILURE;
        try {
            Map<String, Object> featureIdMap = new HashMap<>();
            featureIdMap.put(UMFeatureDao.ID, featureIds);
            Map<GenericDao.QueryOperators, Object> criterias = new HashMap<>();
            criterias.put(GenericDao.QueryOperators.IN, featureIdMap);
            List<UMFeature> features = featureService.retrieveFeatures(null, criterias, null);
            if (!CollectionUtils.isEmpty(features)) {
                for (UMFeature uMFeature : features) {
                    int indexOf = featureIds.indexOf(uMFeature.getId());
                    uMFeature.setSeqNo(indexOf + 1);
                }
                featureService.createOrUpdateAllFeatures(features);
            }
            response = HkSystemConstantUtil.SUCCESS;
        } catch (Exception e) {
        }
        return response;
    }
    
    public void initFeatureDetail() {
        List<UMFeature> uMFeatures = userManagementServiceWrapper.retrieveFeatures(null);
        if (!CollectionUtils.isEmpty(uMFeatures)) {
            Map<String, UMFeatureDetailDataBean> uMFeatureDetailDataBeanMap = new HashMap<>();
            for (UMFeature uMFeature : uMFeatures) {
                UMFeatureDetailDataBean featureDetailDataBean = new UMFeatureDetailDataBean();
                featureDetailDataBean.setId(uMFeature.getId());
                featureDetailDataBean.setDescription(uMFeature.getDescription());
                featureDetailDataBean.setDisplayName(uMFeature.getMenuLabel());
                featureDetailDataBean.setFeatureName(uMFeature.getName());
                featureDetailDataBean.setFeatureURL(uMFeature.getFeatureUrl());
                featureDetailDataBean.setIsCrud(uMFeature.getIsCrud());
                featureDetailDataBean.setMenuCategory(uMFeature.getMenuCategory());
                featureDetailDataBean.setMenuLabel(uMFeature.getMenuLabel());
                featureDetailDataBean.setMenuType(uMFeature.getMenuType());
                featureDetailDataBean.setParentId(uMFeature.getParentId());
                if (uMFeature.getParent() != null) {
                    featureDetailDataBean.setParentName(uMFeature.getParent().getName());
                }
                featureDetailDataBean.setPrecedence(uMFeature.getPrecedence());
                featureDetailDataBean.setSeqNo(uMFeature.getSeqNo());
                featureDetailDataBean.setWebserviceUrl(uMFeature.getWebserviceUrl());
                uMFeatureDetailDataBeanMap.put(featureDetailDataBean.getFeatureName(), featureDetailDataBean);
            }
            applicationUtil.setuMFeatureDetailDataBeanMap(uMFeatureDetailDataBeanMap);
        }
    }

}
