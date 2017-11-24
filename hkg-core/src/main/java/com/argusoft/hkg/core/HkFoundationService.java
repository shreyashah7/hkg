package com.argusoft.hkg.core;

import com.argusoft.hkg.model.HkAssetEntity;
import com.argusoft.hkg.model.HkCaratRangeEntity;
import com.argusoft.hkg.model.HkCategoryEntity;
import com.argusoft.hkg.model.HkReferenceRateEntity;
import com.argusoft.hkg.model.HkFranchiseRequirementEntity;
import com.argusoft.hkg.model.HkGoalPermissionEntity;
import com.argusoft.hkg.model.HkGoalTemplateEntity;
import com.argusoft.hkg.model.HkMasterEntity;
import com.argusoft.hkg.model.HkPriceListDetailEntity;
import com.argusoft.hkg.model.HkPriceListEntity;
import com.argusoft.hkg.model.HkSystemConfigurationEntity;
import com.argusoft.hkg.model.HkTheme;
import com.argusoft.hkg.model.HkValueEntity;
import com.argusoft.hkg.nosql.model.HkRuleSetDocument;
import com.argusoft.hkg.core.util.Fields;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Tejas
 *
 */
public interface HkFoundationService {

    /* ASSET MANAGEMENT*/
    /**
     * get Assets for category
     *
     * @param Category
     * @param companyId
     * @param forIssue
     * @return
     */
    public List<HkAssetEntity> retrieveAssets(Long Category, Long companyId, boolean forIssue);

    /**
     * get Asset by id
     *
     * @param assetId
     * @return
     */
    public HkAssetEntity retrieveAsset(Long assetId);

    /**
     * create asset
     *
     * @param HkAssetentity
     * @return
     */
    public boolean createAsset(HkAssetEntity HkAssetentity);

    /**
     * update Asset
     *
     * @param HkAssetentity
     * @return
     */
    public boolean updateAsset(HkAssetEntity HkAssetentity, List<Long> notifyUserList);

    /**
     * Get Category (with child categories) by id
     *
     * @param categoryId
     * @return
     */
    public HkCategoryEntity retrieveAssetCategory(Long categoryId);

    /**
     * Check whether a asset cateogry with specific name exists or not.
     *
     * @param categoryName
     * @param companyId
     * @param skipCategory
     * @return true is any 'non-archived' category exists for the given name,
     * false otherwise
     */
    public boolean doesAssetCategoryNameExist(String categoryName, Long companyId, Long skipCategory);

    /**
     * Check whether a asset cateogry with specific prefix exists or not.
     *
     * @param categoryPrefix
     * @param companyId
     * @param skipCategory
     * @return true is any 'non-archived' category exists for the given name,
     * false otherwise
     */
    public boolean doesAssetCategoryPrefixExist(String categoryPrefix, Long companyId, Long skipCategory);

    /**
     *
     * @param companyId
     * @return list of all categories which belong to the category type
     * specified. Child categories will not be fetched.
     */
    public List<HkCategoryEntity> retrieveAllAssetCategories(Long companyId);

    /**
     *
     * @param companyId
     * @return
     */
    public List<HkCategoryEntity> retrieveAllRootAssetCategories(Long companyId);

    /**
     *
     * @param category category entity to be updated
     * @return true if category was updated successfully, false otherwise.
     */
    public boolean updateAssetCategory(HkCategoryEntity category);

    /**
     *
     * @param categoryId to identify which category should be removed.
     * @return true if category was removed successfully, false otherwise.
     */
    public boolean removeAssetCategory(Long categoryId);

    /**
     *
     * @param category category entity to be created
     *
     * @return true if category was created successfully, false otherwise.
     */
    public boolean createAssetCategory(HkCategoryEntity category);

    /**
     * retrieve Assets based on the given Search Text
     *
     * @param searchText Search Criteria
     * @param companyId franchise id
     * @return list of asset references
     */
    public List<HkAssetEntity> retrieveAssetBasedonSearchCriteria(String searchText, Long companyId);

    /**
     * Check whether a serial number for the specific category exists or not.
     *
     * @param serialNumber serial number that needs to be verify
     * @param category category for which serial number uniqueness needs to be
     * checked
     * @param skipAsset Asset that needs to be skipped while verification
     * @param companyId franchise id
     * @return true if serial number for the specific category already exists
     * false otherwise
     */
    public boolean doesSerialNumberAlreadyExistForCategory(Integer serialNumber, Long category, Long skipAsset, Long companyId);

    /**
     * get Assets for categoryIds
     *
     * @param categoryIds List of category
     * @param companyId franchise id
     * @return list of asset references
     */
    public List<HkAssetEntity> retrieveAssets(List<Long> categoryIds, Long companyId);

    /**
     * get Assets count group by category
     *
     * @param companyId franchise id
     * @return map of asset count
     */
    public Map<Long, Integer> retrieveAssetCategorywiseCount(Long companyId);

    /**
     * get names of assets same as suggested name
     *
     * @param assetName suggested name for asset
     * @param companyId franchise id
     * @return list of similar asset names
     */
    public List<String> retrieveSimilarNameSuggestion(String assetName, Long companyId);

    /**
     * get requirements of Designations and Machines associated with the
     * Franchise
     *
     * @param companyId franchise id
     * @return list of franchise requirements
     */
    public List<HkFranchiseRequirementEntity> retrieveFranchiseRequirements(Long companyId);

    /**
     * create franchise requirements
     *
     * @param requirements
     * @return
     */
    public boolean createFranchiseRequirements(List<HkFranchiseRequirementEntity> requirements);

    /**
     * update franchise requirements
     *
     * @param requirements
     * @return
     */
    public boolean updateFranchiseRequirements(List<HkFranchiseRequirementEntity> requirements);

    /**
     * remove franchise requirements
     *
     * @param requirements
     * @return
     */
    public boolean removeFranchiseRequirements(List<HkFranchiseRequirementEntity> requirements);

    /**
     * remove franchise requirements
     *
     * @param companyId franchise id
     * @return
     */
    public boolean removeAllFranchiseRequirements(Long companyId);

    public static class MASTER implements Fields {

        public static final String NAME = "masterName";
        public static final String CODE = "code";
        public static final String VALUES = "hkValueEntityList";
        public static final String PRECEDENCE = "precedence";
        public static final String MASTER_TYPE = "masterType";
        public static final String COMPANY = "franchise";

    }

    public static class VALUES implements Fields {

        public static final String MASTER_CODE = "keyCode.code";
        public static final String NAME = "masterName";
        public static final String ID = "id";
        public static final String VALUE_NAME = "valueName";
        public static final String OFTEN_USED = "isOftenUsed";
        public static final String MASTER_TPYE = "keyCode.masterType";
        public static final String SHORTCUT_CODE = "shortcutCode";

    }

    /* MANAGE MASTERS */
    /**
     * retrieve Masters based on precedence. Make sure to send proper precedence
     * value from loginDataBean.
     *
     * @param companyId
     * @param userPrecedence
     * @param type
     * @param isArchive
     * @return
     */
    public List<HkMasterEntity> retrieveMasters(long companyId, short userPrecedence, String type, Boolean isArchive);

    /**
     * Copy Master values from franchise to destination franchise.
     *
     * @param sourceFranchise
     * @param destinationFranchise
     */
    public void copyMasterValuesFromFranchise(Long sourceFranchise, Long destinationFranchise);

    /**
     * checks if mastername already exists for specified franchise
     *
     * @param companyId
     * @param masterName
     * @return
     */
    public boolean doesMasterNameExist(long companyId, String masterName);

    /**
     *
     * @param masterCode
     * @param userPrecedence
     * @return
     */
    public HkMasterEntity retrieveMaster(String masterCode, short userPrecedence, Long companyId);

    public HkMasterEntity retrieveMaster(String masterCode, short userPrecedence);

    /**
     * This methods retrieves the master object along with values for the
     * specified master codes.
     *
     * @param masterCodes
     * @param userPrecedence
     * @return list of master entities grouped by master code
     */
    public Map<String, HkMasterEntity> retrieveMaster(List<String> masterCodes, short userPrecedence);

    /**
     * Retrieve Master entities by list of master codes
     *
     * @param masterCodes
     * @return
     */
    public Map<String, HkMasterEntity> retrieveMaster(List<String> masterCodes);

    /**
     * Creates a new Custom Master
     *
     * @param hkMasterEntity
     * @return
     */
    public boolean createMaster(HkMasterEntity hkMasterEntity);

//	public boolean updateMaster(HkMasterEntity hkMasterEntity);
    public boolean removeMaster(String masterCode);

    /**
     * Search Masters based on precedence. Make sure to send proper precedence
     * value from loginDataBean.
     *
     * @param companyId
     * @param masterName
     * @param userPrecedence
     * @return
     */
    public List<HkMasterEntity> searchMaster(long companyId, String masterName, short userPrecedence);

    /**
     *
     * @param password password to authenticate
     * @param companyId franchiseId to the user belongs to.
     * @return
     */
    public boolean authenticateForEditMaster(String password, String encryptedPassword);

    /**
     *
     * @param hkMasterEntity
     * @param notifyUserList
     * @param companyId
     * @param password password to authenticate
     * @return
     */
    public boolean updateMaster(HkMasterEntity hkMasterEntity, List<Long> notifyUserList, Long companyId, Boolean isDelete);

    /**
     *
     * @author Shifa Salheen
     * @param id of customfield which would be keycode
     * @return list of HkValueEntity
     */
    public List<HkValueEntity> retrieveMasterValuesOfSameKeyCodeByCustomFieldId(Long id);

    /**
     *
     * @author Shifa Salheen
     * @return list of HkValueEntity
     */
    public List<HkValueEntity> retrieveAllValueEntities();

    /**
     * This method removes all the value entities for particular key, method to
     * be used when you delete masters,its associated values to be deleted
     *
     * @param keycode is the id of keymaster to which the values belong
     * @author SHifa Salheen
     * @return
     */
    public void removeAllValueEntitiesOfMasterKey(Long keycode);

    public List<Long> searchMasterValues(long companyId, String masterCode, String valueName);

    /**
     * This method saves the system configuration entities.
     *
     * @param configList The list of system configuration entities.
     */
    public void saveSystemConfigurations(List<HkSystemConfigurationEntity> configList);

    /**
     * This method retrieves the system configuration entities for given
     * franchise.
     *
     * @param franchise The id of the franchise.
     * @return Returns the list of configurations for given franchise.
     */
    public List<HkSystemConfigurationEntity> retrieveSystemConfigurationByFranchise(Long franchise);

    /**
     * This method retrieves configuration object for given key.
     *
     * @param key The key of configuration.
     * @param franchise The id of the franchise.
     * @return Returns the object that matches given key.
     */
    public HkSystemConfigurationEntity retrieveSystemConfigurationByKey(String key, Long franchise);

    /**
     * This method retrieves list of configuration for given key for all
     * franchise.
     *
     * @param key The key of configuration.
     * @return Returns the List of object of configuration that matches given
     * key irrespective of franchise.
     */
    public List<HkSystemConfigurationEntity> retrieveAllSystemConfigurationByKey(String key);

    /**
     * USe this method when you want to retrieve values for any master. Only
     * active and non-archive values will be returned.
     *
     * @param companyId
     * @param masterCodes
     * @return
     */
    public List<HkValueEntity> retrieveMasterValuesByCode(long companyId, List<String> masterCodes);

    public List<HkValueEntity> retrieveMasterValuesByCodeForCustom(long companyId, String masterCodes, String searchQuery);

    /**
     * USe this method when you want to retrieve value from Id.
     *
     * @param valueId
     *
     * @return
     */
    public HkValueEntity searchMasterValue(long valueId);

    /**
     * USe this method when you want to retrieve value from Id.
     *
     * @param valueIds are list of value id of
     * @author Shifa Salheen
     * @return list of HkValueEntity
     */
    public List<HkValueEntity> retrieveValueEntityListByValueIds(List<Long> valueIds);

    /**
     * Use this method to copy machines information from one franchise to
     * another
     *
     * @param sourceFranchise The id of the source franchise
     * @param destinationFranchise The id of the destination franchise
     * @return list of categories created
     */
    public List<HkCategoryEntity> copyMachinesFromFranchise(Long sourceFranchise, Long destinationFranchise);

    /**
     * Use this method to retrieve asset category for the franchise based on
     * prefix value
     *
     * @param prefix The prefix for the asset category
     * @param franchise The id of the franchise
     * @return instance of HkCategoryEntity
     */
    public HkCategoryEntity retrieveAssetCategoryByPrefix(String prefix, Long franchise);

    public List<HkMasterEntity> retrieveAllMasters();

    public void createAllMasters(List<HkMasterEntity> masterEntitys);

    /*---------------------------------- Mange Reference Rate ----------------------------*/
    /**
     * Use this method to add the reference rate
     *
     * @param currencyRateEntity Currency Rate Entity
     * @return boolean update/create record
     */
    public boolean addReferenceRate(HkReferenceRateEntity currencyRateEntity);

    /**
     * Use this method to update the reference rate Nothing can be edited rather
     * than applicable from
     *
     * @param refId id of reference rate
     * @param applicableFrom date from which the rate can be applicable
     * @param isActive status of reference rate
     * @param modifiedBy Id of the user modified the rate detail
     * @return boolean update/create record
     */
    public boolean updateReferenceRate(Long refId, Date applicableFrom, Boolean isActive, Long modifiedBy);

    /**
     * Use to retrieve Current currency rates for all the currency
     *
     * @param isActive retrieve according to active flag
     * @param franchiseId Id of the franchise
     * @return List of HkCurrencyRateEntity
     */
    public List<HkReferenceRateEntity> retrieveCurrentCurrencyRate(Boolean isActive, Long franchiseId);

    /**
     * Use this method to retrieve the current rate and history of the currency
     * of the particular days
     *
     * @param numberOfRecords describes the how many records of history is
     * required
     * @param franchiseId Id of the franchise
     * @return Map of Code and it's history List with current active rate
     */
    public Map<String, List<HkReferenceRateEntity>> retrieveCurrentRateAndCurrencyHistory(int numberOfRecords, Long franchiseId);

    /**
     * Use this method to retrieve All currencyRates
     *
     * @param isActive pass true to retrieve active and pass false to retrieve
     * inactive
     * @param franchiseId Id of the franchise
     * @return List of HkCurrencyRateEntity
     */
    public List<HkReferenceRateEntity> retrieveAllCurrencyRates(Boolean isActive, Long franchiseId);

    public HkReferenceRateEntity retrieveActiveCurrencyRateByCode(String currencyCode);

    /**
     * Using this method in test case only
     *
     * @param currencyRateEntitys list of HkCurrencyRateEntity
     */
    public void createAllCurrencyRates(List<HkReferenceRateEntity> currencyRateEntitys);

    public void createAllThemes(List<HkTheme> themes);

    public List<HkTheme> retrieveAllThemes(String status);

    /**
     * Raj: Method to add list of goal permissions
     *
     * @param goalPermissionEntitys HkGoalPermissionEntitys to save
     */
    public void addGoalPermissions(List<HkGoalPermissionEntity> goalPermissionEntitys);

    /**
     * Raj: Method to update list of goal permissions
     *
     * @param goalPermissionEntitys entities to update
     */
    public void updateGoalPermissions(List<HkGoalPermissionEntity> goalPermissionEntitys, List<String> type);

    /**
     * delete goal permission whose id in the list
     *
     * @param permissions ids of GoalPermissions to remove
     */
    public void deleteGoalPermissions(List<Long> permissions);

    /**
     * retrieve Goal permission by id
     *
     * @param id of goal permission
     * @return HkGoalPermissionEntity with matching id
     */
    public HkGoalPermissionEntity retrieveGoalPermissionById(Long id);

    /**
     * Retrieve active goal permission by designation
     *
     * @param designation id of designation
     * @return HkGoalPermissionEntity with given designation
     */
    public List<HkGoalPermissionEntity> retrieveActiveGoalPermissionByDesignation(Long designation);

    /**
     * Retrieve Goal permission by accessOfFeature and designation
     *
     * @param accessOfFeature type of feature
     * @param designation id of designation
     * @return HkGoalPermissionEntity with matching parameters
     */
    public List<HkGoalPermissionEntity> retrieveGoalPermissions(String accessOfFeature, long designation);

    /**
     * save Goal template
     *
     * @param goalTemplates GoalTemplates to save
     */
    public void saveGoalTemplates(List<HkGoalTemplateEntity> goalTemplates);

    public Long saveGoalTemplate(HkGoalTemplateEntity goalTemplate);

    /**
     * save goal templates with rules
     *
     * @param goalTemplateEntity
     * @param ruleSetDocument
     * @return
     */
    public Long saveGoalTemplateWithRules(HkGoalTemplateEntity goalTemplateEntity, HkRuleSetDocument ruleSetDocument);

    /**
     * Retrieve active goal template by designation Id
     *
     * @param designation id of designation
     * @param franchise
     * @return HkGoalTemplateEntity with matching designation id
     */
    public List<HkGoalTemplateEntity> retrieveActiveGoalTemplatesByDesignation(Long designation, Long franchise);

    /**
     * retrieve all active goal templates by franchise id
     *
     * @param franchise
     * @return
     */
    public List<HkGoalTemplateEntity> retrieveActiveGoalTemplatesByFranchise(Long franchise);

    public List<HkGoalTemplateEntity> retrieveGoalTemplates(Long franchise, String status);

    /**
     * search goal templates by name
     *
     * @param goalTemplateName
     * @param companyId
     * @param status
     * @param applyLimit
     * @return
     */
    public List<HkGoalTemplateEntity> searchGoalTemplateByName(String goalTemplateName, long companyId, List<String> status, Boolean applyLimit);

    /**
     * Retrieve Active Goal template by service Id
     *
     * @param service id of service
     * @param franchise
     * @return HkGoalTemplateEntity with matching service id
     */
    public List<HkGoalTemplateEntity> retrieveActiveGoalTemplatesByService(Long service, Long franchise);

    /**
     * Retrieve Active Goal template by department Id
     *
     * @param department id of department
     * @param franchise
     * @return HkGoalTemplateEntity with matching department id
     */
    public List<HkGoalTemplateEntity> retrieveActiveGoalTemplatesByDepartment(Long department, Long franchise);

    /**
     * Retrieve Pending Goal template by franchise Id
     *
     * @param franchise id of franchise
     * @return HkGoalTemplateEntities with matching franchise id
     */
    public List<HkGoalTemplateEntity> retrievePendingGoalTemplates(Long franchise);

    /**
     * Retrieve Goal template by ID
     *
     * @param id of GoalTemplate
     * @return HkGoalTemplateEntity with matching id
     */
    public HkGoalTemplateEntity retrieveGoalTemplateById(Long id);

    /**
     * retrieve active goal permission by designation
     *
     * @param designations
     * @return
     */
    public List<HkGoalPermissionEntity> retrieveActiveGoalPermissionByDesignations(List<Long> designations);

    /**
     * Update goal templates
     *
     * @param goalTemplateEntitys
     */
    public void updateGoalTemplates(List<HkGoalTemplateEntity> goalTemplateEntitys);

    /**
     * Save list of carat range entities
     *
     * @param caratRangeEntities list of entities to save
     */
    public void addCaratRanges(List<HkCaratRangeEntity> caratRangeEntities);

    /**
     * Retrieves Carat entities by franchise Id and status
     *
     * @param franchise id
     * @param status Active or Archived
     * @return List of carat entities satisfying criteria
     */
    public List<HkCaratRangeEntity> retrieveCaratRangeByFranchiseAndStatus(Long franchise, List<String> status, List<Long> caratId);

    /**
     * deactivates carat ranges with ids
     *
     * @param ids of carat entities to deactivate
     */
    public void deactivateCaratRanges(List<Long> ids);

    /**
     * Retrieve Map of column and associated values for price list
     *
     * @param franchise companyId
     * @return Map of fields and associated values
     */
    public Map<String, List<String>> retrieveMapOfFieldsValuesForPriceList(Long franchise);

    /**
     * Retrieve list of all values of master based on list of key
     *
     * @param codeList list of code key
     * @param franchise companyID
     * @return Map of key to List of values
     */
    public Map<String, Map<String, Long>> retrieveMapOfValuesFromCode(List<String> codeList, Long franchise);

    /**
     * Save price list entity
     *
     * @param priceListEntity entity to save
     * @return Id of entity
     */
    public Long savePriceList(HkPriceListEntity priceListEntity);

    /**
     * Save list of price list detail entities
     *
     * @param priceListDetailEntities list of entities to save
     */
    public void savePriceListDetailEntities(List<HkPriceListDetailEntity> priceListDetailEntities);

    /**
     * Archive old price list information
     *
     * @param companyId franchise id
     */
    public void archiveOldPriceList(Long companyId);

    /**
     * retrieve all price list of comapany
     *
     * @param companyId
     * @return list of all price list associated with franchise
     */
    public List<HkPriceListEntity> retrieveAllPriceLists(Long companyId);

    public void createAllMasterValues(List<HkValueEntity> values);

    public List<HkGoalTemplateEntity> retrieveGoalTemplates(List<Long> ids);

    /**
     * Retrieve map of carat value to related carat id map
     *
     * @param listOfCarats List of carat values
     * @param companyId franchise Id
     * @return Map of carat value to carat ID
     */
    public Map<Double, Long> retrieveMapOfCaratRangeToId(List<Double> listOfCarats, Long companyId);

    public Map<Long, HkValueEntity> retrieveMapOfIdAndValueEntity(List<Long> valueIds);

    public Map<String, List<HkValueEntity>> mapOfKeyIdWithValueEntities(Long companyId);

    /**
     *
     * @param featureList
     * @return map of custom field dbBaseName:customfield id mapping.
     */
    public Map<String, String> retrieveCustomfieldMasterMappingForCalc(List<Long> featureList);

    public void saveMasterValueEntity(HkValueEntity hkValueEntity);
}
