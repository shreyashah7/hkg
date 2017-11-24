package com.argusoft.hkg.web.util;

import com.argusoft.generic.database.exception.GenericDatabaseException;
import com.argusoft.hkg.common.UrlPatternKeyedMap;
import com.argusoft.hkg.common.constantutil.HkSystemConstantUtil;
import static com.argusoft.hkg.common.constantutil.HkSystemConstantUtil.RAP_CALC_FIELD_MAP;
import com.argusoft.hkg.common.functionutil.FolderManagement;
import com.argusoft.hkg.core.HkFoundationService;
import com.argusoft.hkg.model.HkSystemConfigurationEntity;
import com.argusoft.hkg.model.HkSystemConfigurationEntityPK;
import com.argusoft.hkg.nosql.core.HkFeatureService;
import com.argusoft.hkg.nosql.model.HkCalcMasterMapping;
import com.argusoft.hkg.sync.center.core.HkUMSyncService;
import com.argusoft.hkg.web.bootstrap.ReportBuilderInitializer;
import com.argusoft.hkg.web.bootstrap.RoleFeatureInitialization;
import com.argusoft.hkg.web.center.util.ApplicationUtil;
import com.argusoft.hkg.web.comparator.FeatureDataBeanComparator;
import com.argusoft.hkg.web.customfield.transformers.CustomFieldTransformerBean;
import com.argusoft.hkg.web.internationalization.transformers.LocalesTransformerBean;
import com.argusoft.hkg.web.master.transformers.CurrencyMasterTransformerBean;
import com.argusoft.hkg.web.master.transformers.MasterTransformerBean;
import com.argusoft.hkg.web.usermanagement.databeans.FeatureDataBean;
import com.argusoft.hkg.web.usermanagement.transformers.DepartmentTransformerBean;
import com.argusoft.hkg.web.usermanagement.transformers.DesignationTransformerBean;
import com.argusoft.hkg.web.usermanagement.transformers.EmployeeTransformerBean;
import com.argusoft.hkg.web.usermanagement.transformers.FeatureTransformerBean;
import com.argusoft.hkg.web.usermanagement.wrapper.UserManagementServiceWrapper;
import com.argusoft.usermanagement.common.core.UMCompanyService;
import com.argusoft.usermanagement.common.core.UMSystemConfigurationService;
import com.argusoft.usermanagement.common.model.UMCompany;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 *
 * @author kelvin
 */
@Service
@DependsOn("syncSqlInterceptor")
public class ApplicationMasterInitializer {
    @Autowired
    HkUMSyncService uMSyncService;
    @Autowired
    UMSystemConfigurationService systemConfigurationService;
    
    @Autowired
    RoleFeatureInitialization roleFeatureInitialization;
    @Autowired
    ReportBuilderInitializer reportBuilderInitializer;
    
    @Autowired
    CustomFieldTransformerBean customFieldTransformerBean;
    @Autowired
    DesignationTransformerBean designationTransformerBean;
    @Autowired
    FeatureTransformerBean featureTransformerBean;
    @Autowired
    CurrencyMasterTransformerBean currencyMasterTransformerBean;
    @Autowired
    MasterTransformerBean masterTransformerBean;
    @Autowired
    EmployeeTransformerBean employeeTransformerBean;
    @Autowired
    LocalesTransformerBean localesTransformerBean;
    @Autowired
    UserManagementServiceWrapper userManagementServiceWrapper;
    @Autowired
    DepartmentTransformerBean departmentTransformerBean;
    @Autowired
    ApplicationUtil applicationUtil;
    @Autowired
    HkFeatureService featureService;
    
    private Map<Long, List<FeatureDataBean>> mapRoleFeatures = null;
    
    public static Long hkAdminRole = 0l;
    public static String anonymousRoleId = null;
    public static String fullyAuthenticationRoleId = null;
    
    @Autowired
    private HkFoundationService hkFoundationService;
    @Autowired
    private UMCompanyService companyService;
    private UrlPatternKeyedMap mapUrlRoles;
    
    private final Logger LOGGER;
    
    public ApplicationMasterInitializer() {
        LOGGER = LoggerFactory.getLogger(this.getClass());
    }
    
    public static Long getHkAdminRole() {
        return hkAdminRole;
    }
    
    public static void setHkAdminRole(Long hkAdminRole) {
        ApplicationMasterInitializer.hkAdminRole = hkAdminRole;
    }
    
    public static String getAnonymousRoleId() {
        return anonymousRoleId;
    }
    
    public static void setAnonymousRoleId(String anonymousRoleId) {
        ApplicationMasterInitializer.anonymousRoleId = anonymousRoleId;
    }
    
    public static String getFullyAuthenticationRoleId() {
        return fullyAuthenticationRoleId;
    }
    
    public static void setFullyAuthenticationRoleId(String fullyAuthenticationRoleId) {
        ApplicationMasterInitializer.fullyAuthenticationRoleId = fullyAuthenticationRoleId;
    }
    
    public UrlPatternKeyedMap getMapUrlRoles() {
        return mapUrlRoles;
    }
    
    public void setMapUrlRoles(UrlPatternKeyedMap mapUrlRoles) {
        this.mapUrlRoles = mapUrlRoles;
    }
    
    public Map<Long, List<FeatureDataBean>> getMapRoleFeatures() {
        return mapRoleFeatures;
    }
    
    public void setMapRoleFeatures(Map<Long, List<FeatureDataBean>> mapRoleFeatures) {
        this.mapRoleFeatures = mapRoleFeatures;
    }
    
    @PostConstruct
    @SuppressWarnings("deprecation")
    private void initHkApplicationUtil() {
        LOGGER.debug("========== Master Init call ===========");
        roleFeatureInitialization.UpdateFeatureDetailUsingXls();
        roleFeatureInitialization.createFeatureSectionField();
        reportBuilderInitializer.createForeignTables();
        reportBuilderInitializer.fillTableSetWithIsArchiveFieldAbsent();
        reportBuilderInitializer.updateTabularRelationshipUsingXls();
        roleFeatureInitialization.appendComponentTypeInDbFieldName();
        
        applicationUtil.setSystemConfigrationMap(systemConfigurationService.retrieveAllSystemConfigurations());

//        Map<Long, HkCurrencyEntity> currencyMasterMap = currencyMasterTransformerBean.retrieveCurrencyMap();
        try {
//            applicationUtil.setFeatureFromTemplateMap(customFieldTransformerBean.retrieveFeatureSectionMap(currencyMasterMap));
            customFieldTransformerBean.createFeatureSectionMap(false, null);
        } catch (GenericDatabaseException ex) {
            java.util.logging.Logger.getLogger(ApplicationMasterInitializer.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        designationTransformerBean.initRoleAndUser(HkSystemConstantUtil.ROLE.SUPER_ADMIN,
                Arrays.asList(HkSystemConstantUtil.Feature.MANAGE_FRANCHISE, HkSystemConstantUtil.Feature.MANAGE_FEATURE, HkSystemConstantUtil.Feature.MANAGE_LOCATION),
                -1, HkSystemConstantUtil.USER.SUPER_ADMIN, "sadmin@123", "Super", null, "Admin");
        
        masterTransformerBean.initMasters();
        
        employeeTransformerBean.initThemes();
        
        localesTransformerBean.initLocales();
// Create Default Department at deploy time
        departmentTransformerBean.createDefaultDepartment();
        //Creating Base Folder for the System
        FolderManagement.createBaseFolder();
        
        featureTransformerBean.initFeatureDetail();
        Map<String, String> systemConfigrationMap = applicationUtil.getSystemConfigrationMap();
        if (!CollectionUtils.isEmpty(systemConfigrationMap)) {
            if (!systemConfigrationMap.containsKey(HkSystemConstantUtil.FranchiseConfiguration.DEFAULT_XMPP_EMAIL_ADDRESS)) {
                List<Long> retrieveUsersByRole = userManagementServiceWrapper.retrieveUsersByRoleName("HK Admin", 1l);
                System.out.println("retrieveUsersByRole  " + retrieveUsersByRole);
                if (!CollectionUtils.isEmpty(retrieveUsersByRole)) {
                    UMCompany company = companyService.retrieveCompanyByCompanyId(1l);
                    if (company != null) {
                        List<HkSystemConfigurationEntity> configurationEntitys = new LinkedList<>();
                        configurationEntitys.add(new HkSystemConfigurationEntity(new HkSystemConfigurationEntityPK(HkSystemConstantUtil.FranchiseConfiguration.DEFAULT_XMPP_EMAIL_ADDRESS, 1), false, company.getEmailAddress(), 0, new Date()));
                        hkFoundationService.saveSystemConfigurations(configurationEntitys);
                    }
                }
            }
        }
        this.initDesignations();
        Map<String, Long> featureNameIdMap = new HashMap<>();

        //START: Rap calc field initialization
        Map<Long, String> retrieveFeatureIdWithNameMap = userManagementServiceWrapper.retrieveFeatureIdWithNameMap();
        List<Long> featureList = new LinkedList<>();
        for (Map.Entry<Long, String> entrySet : retrieveFeatureIdWithNameMap.entrySet()) {
            Long key = entrySet.getKey();
            String value = entrySet.getValue();
            if (HkSystemConstantUtil.Feature.ROUGHCALC.equals(value)) {
                featureList.add(key);
            }
            featureNameIdMap.put(value, key);
        }
        applicationUtil.setFeatureNameIdMap(featureNameIdMap);
        Map< String, String> customfieldMap = hkFoundationService.retrieveCustomfieldMasterMappingForCalc(featureList);
        if (!CollectionUtils.isEmpty(customfieldMap)) {
            HkSystemConstantUtil.RAP_CALC_FIELD_MAP = customfieldMap;
            HkCalcMasterMapping calcMasterMapping = new HkCalcMasterMapping();
            uMSyncService.retrieveAllDocuments(HkCalcMasterMapping.class);
            calcMasterMapping.setId("COL");
            calcMasterMapping.setMasterId(RAP_CALC_FIELD_MAP.get(HkSystemConstantUtil.FourC.COLOR));
            uMSyncService.saveOrUpdateDocument(calcMasterMapping);

            calcMasterMapping = new HkCalcMasterMapping();
            calcMasterMapping.setId("CLR");
            calcMasterMapping.setMasterId(RAP_CALC_FIELD_MAP.get(HkSystemConstantUtil.FourC.QUALITY));
            uMSyncService.saveOrUpdateDocument(calcMasterMapping);

            calcMasterMapping = new HkCalcMasterMapping();
            calcMasterMapping.setId("SIZE");
            calcMasterMapping.setMasterId(RAP_CALC_FIELD_MAP.get(HkSystemConstantUtil.FourC.CARAT));
            uMSyncService.saveOrUpdateDocument(calcMasterMapping);

            calcMasterMapping = new HkCalcMasterMapping();
            calcMasterMapping.setId("SHAPE");
            calcMasterMapping.setMasterId(RAP_CALC_FIELD_MAP.get(HkSystemConstantUtil.FourC.SHAPE));
            uMSyncService.saveOrUpdateDocument(calcMasterMapping);

            calcMasterMapping = new HkCalcMasterMapping();
            calcMasterMapping.setId("RDate");
            calcMasterMapping.setMasterId(RAP_CALC_FIELD_MAP.get(HkSystemConstantUtil.FourC.R_DATE));
            uMSyncService.saveOrUpdateDocument(calcMasterMapping);

        }
        
        System.out.println("RAP_CALC_FIELD_MAP " + HkSystemConstantUtil.RAP_CALC_FIELD_MAP);
        //END: Rap calc field initialization
    }
    
    public void initDesignations() {
        this.setMapRoleFeatures(designationTransformerBean.initRoleFeaturesMap());
        this.setMapUrlRoles(designationTransformerBean.initMapUrlRoles());
        applicationUtil.setRolePrecedence(userManagementServiceWrapper.retrieveDesignationPrecedence());
    }
    
    public List<FeatureDataBean> generateMenuByRoles(List<Long> roleIds, boolean isCompanyActivated) {
        
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
}
