/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.systemconfiguration.transformers;

import com.argusoft.generic.database.exception.GenericDatabaseException;
import com.argusoft.hkg.common.constantutil.HkSystemConstantUtil;
import com.argusoft.hkg.common.functionutil.FolderManagement;
import com.argusoft.hkg.core.HkFieldService;
import com.argusoft.hkg.core.HkFoundationService;
import com.argusoft.hkg.model.HkSystemConfigurationEntity;
import com.argusoft.hkg.model.HkSystemConfigurationEntityPK;
import com.argusoft.hkg.model.HkValueEntity;
import com.argusoft.hkg.nosql.core.HkCustomFieldService;
import com.argusoft.hkg.web.center.usermanagement.databeans.LoginDataBean;
import com.argusoft.hkg.web.usermanagement.transformers.FeatureTransformerBean;
import com.argusoft.hkg.web.usermanagement.wrapper.UserManagementServiceWrapper;
import com.argusoft.hkg.web.util.AesEncrypter;
import com.argusoft.hkg.web.util.SelectItem;
import com.argusoft.usermanagement.common.core.UMUserService;
import com.argusoft.usermanagement.common.model.UMRole;
import com.argusoft.usermanagement.common.model.UMUser;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import org.jasypt.util.password.BasicPasswordEncryptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 *
 * @author kuldeep
 */
@Service
public class FranchiseConfigrationTransformerBean implements ApplicationContextAware{

    @Autowired
    LoginDataBean loginDataBean;

    @Autowired
    HkFoundationService foundationService;

    @Autowired
    public BasicPasswordEncryptor basicPasswordEncryptor;

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public UserManagementServiceWrapper userManagementServiceWrapper;

    @Autowired
    HkFoundationService operationsService;

    @Autowired
    UMUserService uMUserService;

    @Autowired
    HkFieldService fieldService;

    @Autowired
    HkCustomFieldService customFieldService;

    @Autowired
    FeatureTransformerBean featureTransformerBean;
    
    ApplicationContext applicationContext;

    @Async
    public void updateUserCode(List<Long> types,Long companyId) throws GenericDatabaseException {
        Map<Long, List<UMUser>> retrievedUsers = userManagementServiceWrapper.retrieveUsersByType(companyId, Boolean.FALSE, types);
        for (Map.Entry<Long, List<UMUser>> entry : retrievedUsers.entrySet()) {
            List<UMUser> list = new ArrayList<>();
            for (UMUser uMUser : entry.getValue()) {
                StringBuilder userCode = new StringBuilder();
                String key = "EMP_TYPE_" + uMUser.getContact().getCustom1();
                HkSystemConfigurationEntity seq = operationsService.retrieveSystemConfigurationByKey(key, uMUser.getCompany());
                String newvalue = null;
                if (seq != null) {
                    newvalue = seq.getKeyValue();
                }
                if (uMUser.getType() != null) {
                    DecimalFormat decimalFormat = new DecimalFormat(newvalue);
                    userCode.append(decimalFormat.format(Integer.valueOf(uMUser.getType())));
                    uMUser.setUserCode(userCode.toString());
                    list.add(uMUser);
                }
            }
            uMUserService.createAllUser(list);

        }
    }

    public void createConfiguration(Map<String, String> configurationMap) throws GenericDatabaseException {
        List<HkSystemConfigurationEntity> systemConfigurationEntityList = new ArrayList<>();
        systemConfigurationEntityList = convertConfigurationMaptoSystemConfigurationEntityList(configurationMap, systemConfigurationEntityList);

        if (configurationMap != null && configurationMap.containsKey(HkSystemConstantUtil.FranchiseConfiguration.EMP_DEFAULT_IMAGE)) {
            try {
                FolderManagement.saveFile(null, null, null, null, Arrays.asList(configurationMap.get(HkSystemConstantUtil.FranchiseConfiguration.EMP_DEFAULT_IMAGE)), false);
            } catch (IOException ex) {
                java.util.logging.Logger.getLogger(FranchiseConfigrationTransformerBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        foundationService.saveSystemConfigurations(systemConfigurationEntityList);
        List<HkValueEntity> valueEntityList = foundationService.retrieveMasterValuesByCode(loginDataBean.getCompanyId(), Arrays.asList(HkSystemConstantUtil.MasterCode.EMPLOYEE_TYPES));
        List<Long> employeeTypes = new ArrayList<>();
        if (valueEntityList != null) {
            for (HkValueEntity valueEntity : valueEntityList) {
                employeeTypes.add(valueEntity.getId());
            }
        }
        FranchiseConfigrationTransformerBean bean = applicationContext.getBean(this.getClass());
        bean.updateUserCode(employeeTypes,loginDataBean.getCompanyId());
    }

    public boolean checkOldPassword(String oldPassword) {
        HkSystemConfigurationEntity systemConfigurationEntity = foundationService.retrieveSystemConfigurationByKey(HkSystemConstantUtil.FranchiseConfiguration.MASTERS_AUTH_PWD, loginDataBean.getCompanyId());
        if (systemConfigurationEntity != null) {
            return basicPasswordEncryptor.checkPassword(oldPassword, systemConfigurationEntity.getKeyValue());
        }
        return false;
    }

    public String uploadFile(String fileName) throws FileNotFoundException, IOException {
        String tempFileName = null;
        tempFileName = FolderManagement.getTempFileName(loginDataBean.getCompanyId(), FolderManagement.FEATURE.COMMON, null, loginDataBean.getId(), fileName);
        return tempFileName;
    }

    public List<SelectItem> retrieveEmployeeTypes() {
        List<HkValueEntity> valueEntityList = foundationService.retrieveMasterValuesByCode(loginDataBean.getCompanyId(), Arrays.asList(HkSystemConstantUtil.MasterCode.EMPLOYEE_TYPES));
        List<SelectItem> employeeTypes = new ArrayList<>();
        if (valueEntityList != null) {
            for (HkValueEntity valueEntity : valueEntityList) {
                String valueName = valueEntity.getValueName();
                if (valueEntity.getTranslatedValueName() != null && !valueEntity.getTranslatedValueName().isEmpty()) {
                    valueName = valueEntity.getTranslatedValueName();
                }
                employeeTypes.add(new SelectItem(valueEntity.getId(), valueName));
            }
        }

        return employeeTypes;
    }

    public Map<String, String> retrieveAllConfiguration() {
        List<HkSystemConfigurationEntity> systemConfigurationEntityList = foundationService.retrieveSystemConfigurationByFranchise(loginDataBean.getCompanyId());
        Map<String, String> systemConfigMap = new HashMap<>();
        if (systemConfigurationEntityList != null && !systemConfigurationEntityList.isEmpty()) {
            for (HkSystemConfigurationEntity systemConfigurationEntity : systemConfigurationEntityList) {
                systemConfigMap.put(systemConfigurationEntity.getHkSystemConfigurationEntityPK().getSystemKey(), systemConfigurationEntity.getKeyValue());
            }
        }
        return systemConfigMap;
    }

    public List<HkSystemConfigurationEntity> convertConfigurationMaptoSystemConfigurationEntityList(Map<String, String> configurationMap, List<HkSystemConfigurationEntity> systemConfigurationEntityList) {
        if (configurationMap != null && !configurationMap.isEmpty()) {
            for (Map.Entry<String, String> entry : configurationMap.entrySet()) {
                String configKey = entry.getKey();
                String configValue = entry.getValue();
                if (configKey.equals(HkSystemConstantUtil.FranchiseConfiguration.MASTERS_AUTH_PWD)) {
                    configValue = basicPasswordEncryptor.encryptPassword(configValue);
                }
                if (configKey.equals(HkSystemConstantUtil.FranchiseConfiguration.ANALYTICS_ENGINE_PWD)) {
                    configValue = AesEncrypter.encrypt(configValue);
                }
                HkSystemConfigurationEntityPK systemConfigurationEntityPK = new HkSystemConfigurationEntityPK(configKey, loginDataBean.getCompanyId());
                HkSystemConfigurationEntity systemConfigurationEntity = new HkSystemConfigurationEntity(systemConfigurationEntityPK, false, configValue, loginDataBean.getId(), Calendar.getInstance().getTime());
                systemConfigurationEntityList.add(systemConfigurationEntity);
            }
        }
        if (configurationMap != null && !configurationMap.containsKey(HkSystemConstantUtil.FranchiseConfiguration.EMP_DEFAULT_IMAGE)) {
            String configKey = HkSystemConstantUtil.FranchiseConfiguration.EMP_DEFAULT_IMAGE;
            String configValue = "";
            HkSystemConfigurationEntityPK systemConfigurationEntityPK = new HkSystemConfigurationEntityPK(configKey, loginDataBean.getCompanyId());
            HkSystemConfigurationEntity systemConfigurationEntity = new HkSystemConfigurationEntity(systemConfigurationEntityPK, false, configValue, loginDataBean.getId(), Calendar.getInstance().getTime());
            systemConfigurationEntity.setIsArchive(true);
            systemConfigurationEntityList.add(systemConfigurationEntity);
        }
        return systemConfigurationEntityList;
    }

    public List<SelectItem> retrieveDesignations(boolean showRootDesignations) {
        List<UMRole> listUMRole = userManagementServiceWrapper.retrieveDesignations(loginDataBean.getCompanyId(), loginDataBean.getPrecedence(), showRootDesignations);
        List<SelectItem> listFranchiseDesignations = new ArrayList<>();
        if (!CollectionUtils.isEmpty(listUMRole)) {
            Collections.sort(listUMRole, new Comparator() {
                @Override
                public int compare(Object o1, Object o2) {
                    UMRole role1 = (UMRole) o1;
                    UMRole role2 = (UMRole) o2;
                    return role1.getCreatedOn().compareTo(role2.getCreatedOn());
                }
            });
            if (!CollectionUtils.isEmpty(listUMRole)) {
                for (UMRole role : listUMRole) {
                    SelectItem selectItem = new SelectItem(role.getId().toString(), role.getName());
                    listFranchiseDesignations.add(selectItem);
                }
            }
        }
        return listFranchiseDesignations;
    }

    @Override
    public void setApplicationContext(ApplicationContext ac) throws BeansException {
        applicationContext = ac;
    }
}
