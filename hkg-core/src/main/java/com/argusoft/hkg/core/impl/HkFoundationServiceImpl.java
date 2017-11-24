package com.argusoft.hkg.core.impl;

import com.argusoft.generic.database.search.SearchFactory;
import com.argusoft.hkg.common.constantutil.HkSystemConstantUtil;
import com.argusoft.hkg.core.HKCategoryService;
import com.argusoft.hkg.core.HkFieldService;
import com.argusoft.hkg.core.HkFoundationService;
import com.argusoft.hkg.core.HkNotificationService;
import com.argusoft.hkg.core.common.HkCoreService;
import com.argusoft.hkg.model.HkAssetEntity;
import com.argusoft.hkg.model.HkCaratRangeEntity;
import com.argusoft.hkg.model.HkCategoryEntity;
import com.argusoft.hkg.model.HkFieldEntity;
import com.argusoft.hkg.model.HkFranchiseRequirementEntity;
import com.argusoft.hkg.model.HkGoalPermissionEntity;
import com.argusoft.hkg.model.HkGoalTemplateEntity;
import com.argusoft.hkg.model.HkMasterEntity;
import com.argusoft.hkg.model.HkNotificationEntity;
import com.argusoft.hkg.model.HkPriceListDetailEntity;
import com.argusoft.hkg.model.HkPriceListEntity;
import com.argusoft.hkg.model.HkReferenceRateEntity;
import com.argusoft.hkg.model.HkSystemConfigurationEntity;
import com.argusoft.hkg.model.HkTheme;
import com.argusoft.hkg.model.HkValueEntity;
import com.argusoft.hkg.nosql.core.HkRuleService;
import com.argusoft.hkg.nosql.model.HkRuleSetDocument;
import com.argusoft.hkg.core.util.CategoryType;
import com.argusoft.hkg.core.util.MasterType;
import com.argusoft.internationalization.common.core.I18nService;
import com.argusoft.internationalization.common.model.I18nLabelEntity;
import com.argusoft.internationalization.common.model.I18nLabelPKEntity;
import com.argusoft.usermanagement.common.core.UMCompanyService;
import com.googlecode.genericdao.search.Filter;
import com.googlecode.genericdao.search.Search;
import com.googlecode.genericdao.search.Sort;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.jasypt.util.password.BasicPasswordEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

@Service
@Transactional
public class HkFoundationServiceImpl extends HkCoreService implements HkFoundationService {

    @Autowired
    HKCategoryService categoryService;
    @Autowired
    HkNotificationService notificationService;
    @Autowired
    I18nService i18nService;
    @Autowired
    UMCompanyService companyService;
    @Autowired
    HkRuleService ruleService;
    @Autowired
    HkFieldService fieldService;
    private static final String IS_ARCHIVE = "isArchive";

    private static class Fields {

        private static final class Asset {

            public static final String COMPANY = "franchise";
            public static final String CATEGORY = "category.id";
            public static final String CATEGORY_OBJ = "category";
            public static final String SERIALNUMBER = "serialNumber";
            public static final String ASSET_TYPE = "assetType";
            public static final String ASSET_NAME = "assetName";
            public static final String REMAINING_UNITS = "remainingUnits";
            public static final String STATUS = "status";
            public static final String ID = "id";
        }

        private static final class FranchiseRequirement {

            public static final String COMPANY = "franchise";
            public static final String ID = "id";
        }

        private static final class HkSystemConfiguration {

            public static final String SYSTEM_KEY = "hkSystemConfigurationEntityPK.systemKey";
            public static final String FRANCHISE = "hkSystemConfigurationEntityPK.franchise";
        }

        private static final class HkCurrencyReferenceRate {

            public static final String IS_ACTIVE = "isActive";
            public static final String UPDATED_ON = "lastModifiedOn";
            public static final String COMPANY = "franchise";
            public static final String CURRENCY = "code";
        }

        private static final class HkCurrency {

            public static final String IS_ACTIVE = "isActive";
            public static final String UPDATED_ON = "lastModifiedOn";
            public static final String COMPANY = "franchise";
            public static final String ID = "id";
        }

        private static final class HkValueMaster {

            public static final String KEY_CODE = "keyCode.code";
            public static final String FRANCHISE = "franchise";
        }

        private static final class HkTheme {

            public static final String STATUS = "status";
        }

        private static final class HkGoalPermission {

            public static final String ID = "id";
            public static final String ACCESS_OF_FEATURE = "accessOfFeature";
            public static final String DESIGNATION = "designation";
            public static final String IS_ARCHIVE = "isArchive";
        }

        private static final class HkGoalTemplate {

            public static final String ID = "id";
            public static final String NAME = "templateName";
            public static final String FOR_DESIGNATION = "forDesignation";
            public static final String FOR_DEPARTMENT = "forDepartment";
            public static final String FOR_SERVICE = "forService";
            public static final String FRANCHISE = "franchise";
            public static final String STATUS = "status";

        }

        private static final class HkCaratEntity {

            public static final String ID = "id";
            public static final String MIN_VALUE = "minValue";
            public static final String MAX_VALUE = "maxValue";
            public static final String STATUS = "status";
            public static final String FRANCHISE = "franchise";

        }

        private static final class HkPriceList {

            public static final String ID = "id";
            public static final String UPLOADED_BY = "uploadedBy";
            public static final String UPLOADED_ON = "uploadedOn";
            public static final String STATUS = "status";
            public static final String FRANCHISE = "franchise";

        }

        private static final class HkMasterEntity {

            public static final String CODE = "code";
        }

        private static final class HkFieldEntity {

            public static final String DB_FIELD_NAME = "dbFieldName";
        }
    }

    @Override
    public List<HkFranchiseRequirementEntity> retrieveFranchiseRequirements(Long companyId) {
        Search search = SearchFactory.getSearch(HkFranchiseRequirementEntity.class);
        search.addFilterEqual(Fields.FranchiseRequirement.COMPANY, companyId);
        return commonDao.search(search);
    }

    @Override
    public boolean createFranchiseRequirements(List<HkFranchiseRequirementEntity> requirements) {
        if (!CollectionUtils.isEmpty(requirements)) {
            commonDao.saveAll(requirements);
            return true;
        }
        return false;
    }

    @Override
    public boolean updateFranchiseRequirements(List<HkFranchiseRequirementEntity> requirements) {
        if (!CollectionUtils.isEmpty(requirements)) {
            commonDao.saveAll(requirements);
            return true;
        }
        return false;
    }

    @Override
    public boolean removeFranchiseRequirements(List<HkFranchiseRequirementEntity> requirements) {
        if (!CollectionUtils.isEmpty(requirements)) {
            for (HkFranchiseRequirementEntity requirementEntity : requirements) {
                requirementEntity.setIsArchive(true);
            }
            commonDao.saveAll(requirements);
            return true;
        }
        return false;
    }

    @Override
    public boolean removeAllFranchiseRequirements(Long companyId) {
        List<HkFranchiseRequirementEntity> requirements = retrieveFranchiseRequirements(companyId);
        if (!CollectionUtils.isEmpty(requirements)) {
            for (HkFranchiseRequirementEntity requirementEntity : requirements) {
                requirementEntity.setIsArchive(true);
            }

        }
        return true;
    }

    @Override
    public HkCategoryEntity retrieveAssetCategoryByPrefix(String prefix, Long companyId) {
//        System.out.println("Company is : "+companyId);
        return categoryService.retrieveCategoryByPrefix(prefix, companyId, CategoryType.ASSET);
    }

    @Override
    public List<HkCategoryEntity> copyMachinesFromFranchise(Long sourceFranchise, Long destinationFranchise) {
        List<HkCategoryEntity> categoriesToCreate = new LinkedList<>();
        HkCategoryEntity assetCategory = categoryService.retrieveCategoryByPrefix(HkSystemConstantUtil.FRANCHISE_REQUIREMENT_TYPE.MACHINES, sourceFranchise, CategoryType.ASSET);
        if (assetCategory != null) {
//            System.out.println("in core :  : :: : : "+destinationFranchise);
            Date currentDate = new Date();
            HkCategoryEntity categoryEntity = new HkCategoryEntity();
            categoryEntity.setCategoryPrefix(assetCategory.getCategoryPrefix());
            categoryEntity.setCategoryTitle(assetCategory.getCategoryTitle());
            categoryEntity.setCurrentIndex(1);
            categoryEntity.setStartIndex(1);
            categoryEntity.setDescription(assetCategory.getDescription());
            categoryEntity.setHaveDiamondProcessingMch(assetCategory.getHaveDiamondProcessingMch());
            categoryEntity.setIsActive(true);
            categoryEntity.setIsArchive(false);
            categoryEntity.setCreatedBy(1l);
            categoryEntity.setCreatedOn(currentDate);
            categoryEntity.setLastModifiedBy(1l);
            categoryEntity.setLastModifiedOn(currentDate);
            categoryEntity.setFranchise(destinationFranchise);

            categoryService.createCategory(categoryEntity, CategoryType.ASSET);
//            System.out.println("here :: : "+categoryEntity.getCategoryPrefix());

            List<HkCategoryEntity> childCategories = categoryService.retrieveAllChildCategories(assetCategory.getId());

            if (!CollectionUtils.isEmpty(childCategories)) {
                for (HkCategoryEntity childCategory : childCategories) {
                    HkCategoryEntity newCategoryEntity = new HkCategoryEntity();
                    newCategoryEntity.setCategoryPrefix(childCategory.getCategoryPrefix());
                    newCategoryEntity.setCategoryTitle(childCategory.getCategoryTitle());
                    newCategoryEntity.setCategoryType(childCategory.getCategoryType());
                    newCategoryEntity.setCurrentIndex(1);
                    newCategoryEntity.setStartIndex(1);
                    newCategoryEntity.setDescription(childCategory.getDescription());
                    newCategoryEntity.setHaveDiamondProcessingMch(childCategory.getHaveDiamondProcessingMch());
                    newCategoryEntity.setIsActive(true);
                    newCategoryEntity.setIsArchive(false);
                    newCategoryEntity.setCreatedBy(1l);
                    newCategoryEntity.setCreatedOn(currentDate);
                    newCategoryEntity.setLastModifiedBy(1l);
                    newCategoryEntity.setLastModifiedOn(currentDate);
                    newCategoryEntity.setFranchise(destinationFranchise);
                    newCategoryEntity.setParent(categoryEntity);

                    categoriesToCreate.add(newCategoryEntity);
                }
                commonDao.saveAll(categoriesToCreate);
            }
            categoriesToCreate.add(categoryEntity);
        }
        return categoriesToCreate;
    }

    @Override
    public List<HkAssetEntity> retrieveAssets(List<Long> categoryIds, Long companyId) {
        Search search = SearchFactory.getSearch(HkAssetEntity.class);
        search.addFilterEqual(Fields.Asset.COMPANY, companyId);
        search.addFilterIn(Fields.Asset.CATEGORY, categoryIds);
        return commonDao.search(search);
    }

    @Override
    public Map<Long, Integer> retrieveAssetCategorywiseCount(Long companyId) {
        Search search = SearchFactory.getSearch(HkAssetEntity.class);
        search.addFilterEqual(Fields.Asset.COMPANY, companyId);
        search.addField(Fields.Asset.REMAINING_UNITS);
        search.addField(Fields.Asset.ID);
        search.addField(Fields.Asset.CATEGORY_OBJ);
        search.addField(Fields.Asset.ASSET_TYPE);

        List<HkAssetEntity> results = commonDao.search(search);

        Map<Long, Integer> resultMap = null;
        if (!CollectionUtils.isEmpty(results)) {
            resultMap = new LinkedHashMap<>();
            for (HkAssetEntity assetEntity : results) {
                if (assetEntity.getCategory() != null) {
                    Integer value;
                    if (!resultMap.containsKey(assetEntity.getCategory().getId())) {
                        value = 0;
                    } else {
                        value = resultMap.get(assetEntity.getCategory().getId());
                    }
                    if (StringUtils.hasText(assetEntity.getAssetType())) {
                        if (assetEntity.getAssetType().equalsIgnoreCase(HkSystemConstantUtil.ASSET_TYPE.MANAGED)) {
                            value = value + 1;
                        } else {
                            value = value + assetEntity.getRemainingUnits();
                        }
                    }
                    resultMap.put(assetEntity.getCategory().getId(), value);
                }
            }
        }

        return resultMap;
    }

    public Map<Long, Integer> retrieveMachineCountByCategory(List<Long> requiredCatgetoryIds, Long franchise) {
        Map<Long, Integer> resultMap = this.retrieveAssetCategorywiseCount(franchise);
        if (!CollectionUtils.isEmpty(resultMap)) {
            Iterator<Long> itr = resultMap.keySet().iterator();
            while (itr.hasNext()) {
                Long categoryId = itr.next();
                if (!requiredCatgetoryIds.contains(categoryId)) {
                    itr.remove();
                }
            }
        }
        return resultMap;
    }

    @Override
    public List<String> retrieveSimilarNameSuggestion(String assetName, Long companyId) {
        Search search = SearchFactory.getSearch(HkAssetEntity.class);
        search.addFilterEqual(Fields.Asset.COMPANY, companyId);
        StringBuilder assetNameBuilder = new StringBuilder("%");
        assetNameBuilder.append(assetName);
        assetNameBuilder.append("%");
        search.addFilterILike(Fields.Asset.ASSET_NAME, assetNameBuilder.toString());

        search.addField(Fields.Asset.ASSET_NAME);

        return commonDao.search(search);
    }

    @Override
    public List<HkAssetEntity> retrieveAssetBasedonSearchCriteria(String searchText, Long companyId) {
        Search search = null;
        if (StringUtils.hasText(searchText) && searchText.length() >= HkSystemConstantUtil.MIN_SEARCH_LENGTH) {
            if (searchText.toUpperCase().contains(HkSystemConstantUtil.ASSET_SEARCH_CODE.CATEGORY)) {
                searchText = searchText.substring(searchText.toUpperCase().indexOf(HkSystemConstantUtil.ASSET_SEARCH_CODE.CATEGORY) + HkSystemConstantUtil.ASSET_SEARCH_CODE.CATEGORY.length()).trim();
                List<HkCategoryEntity> categoryEntities = categoryService.retrieveCategoryBasedonSearchCriteria(searchText, companyId, CategoryType.ASSET);
                if (!CollectionUtils.isEmpty(categoryEntities)) {
                    search = SearchFactory.getSearch(HkAssetEntity.class);
                    search.addFilterIn(Fields.Asset.CATEGORY_OBJ, categoryEntities);
                }
            } else {
                search = SearchFactory.getSearch(HkAssetEntity.class);
                search.addFilterEqual(Fields.Asset.COMPANY, companyId);
                StringBuilder assetNameBuilder = new StringBuilder("%");
                assetNameBuilder.append(searchText);
                assetNameBuilder.append("%");
                search.addFilterILike(Fields.Asset.ASSET_NAME, assetNameBuilder.toString());
            }
        }

        if (search != null) {
            search.addField(Fields.Asset.ID);
            search.addField(Fields.Asset.ASSET_NAME);
            search.addField(Fields.Asset.CATEGORY_OBJ);
            return commonDao.search(search);
        }
        return null;
    }

    @Override
    public boolean doesSerialNumberAlreadyExistForCategory(Integer serialNumber, Long category, Long skipAsset, Long companyId) {
        Search search = SearchFactory.getSearch(HkAssetEntity.class);
        search.addFilterEqual(Fields.Asset.SERIALNUMBER, serialNumber);
        search.addFilterEqual(Fields.Asset.CATEGORY, category);
        if (skipAsset != null) {
            search.addFilterNotEqual(Fields.Asset.ID, skipAsset);
        }
        search.addFilterEqual(Fields.Asset.COMPANY, companyId);
        return commonDao.count(search) > 0;
    }

    @Override
    public List<HkAssetEntity> retrieveAssets(Long category, Long companyId, boolean forIssue) {
        Search search = SearchFactory.getSearch(HkAssetEntity.class);
        if (companyId != null) {
            search.addFilterEqual(Fields.Asset.COMPANY, companyId);
        }
        if (forIssue) {
            search.addFilterOr(Filter.and(Filter.equal(Fields.Asset.ASSET_TYPE, HkSystemConstantUtil.ASSET_TYPE.MANAGED), Filter.equal(Fields.Asset.STATUS, HkSystemConstantUtil.ASSET_STATUS.AVAILABLE)), Filter.and(Filter.equal(Fields.Asset.ASSET_TYPE, HkSystemConstantUtil.ASSET_TYPE.NON_MANAGED), Filter.greaterThan(Fields.Asset.REMAINING_UNITS, 0)));
        }
        search.addFilterEqual(Fields.Asset.CATEGORY, category);
        return commonDao.search(search);
    }

    @Override
    public HkAssetEntity retrieveAsset(Long assetId) {
        return commonDao.find(HkAssetEntity.class, assetId);
    }

    @Override
    public boolean createAsset(HkAssetEntity assetentity) {
        boolean result = commonDao.save(assetentity);
        if (result && assetentity.getCategory() != null && assetentity.getAssetType() != null && assetentity.getAssetType().equalsIgnoreCase(HkSystemConstantUtil.ASSET_TYPE.MANAGED)) {
            HkCategoryEntity categoryEntity = categoryService.retrieveCategory(assetentity.getCategory().getId());
            if (categoryEntity != null && categoryEntity.getCurrentIndex().equals(assetentity.getSerialNumber())) {
                categoryEntity.setCurrentIndex(categoryEntity.getCurrentIndex() + 1);
                categoryService.updateCategory(categoryEntity, CategoryType.ASSET);
            }
        }
        return result;
    }

    @Override
    public boolean updateAsset(HkAssetEntity assetEntity, List<Long> notifyUserList) {
        if (assetEntity != null && StringUtils.hasText(assetEntity.getStatus()) && assetEntity.getStatus().equalsIgnoreCase(HkSystemConstantUtil.ASSET_STATUS.ARCHIVED)) {
            assetEntity.setIsArchive(true);
        }
        boolean result = commonDao.save(assetEntity);

        //  Send notification
        Map<String, Object> valuesMap = new HashMap<>();
        valuesMap.put(HkSystemConstantUtil.NotificationMessageParam.ASSET_NAME, assetEntity.getAssetName());
        HkNotificationEntity notification = createNotification(HkSystemConstantUtil.NotificationType.ASSET,
                HkSystemConstantUtil.NotificationInstanceType.UPDATE_ASSET, valuesMap, assetEntity.getId(), assetEntity.getFranchise());
        notificationService.sendNotification(notification, notifyUserList);

        return result;
    }

    @Override
    public HkCategoryEntity retrieveAssetCategory(Long categoryId) {
        return categoryService.retrieveCategory(categoryId);
    }

    @Override
    public boolean doesAssetCategoryNameExist(String categoryName, Long companyId, Long skipCategory) {
        return categoryService.doesCategoryNameExist(categoryName, companyId, skipCategory, CategoryType.ASSET);
    }

    @Override
    public boolean doesAssetCategoryPrefixExist(String categoryPrefix, Long companyId, Long skipCategory) {
        return categoryService.doesCategoryPrefixExist(categoryPrefix, companyId, skipCategory, CategoryType.ASSET);
    }

    @Override
    public List<HkCategoryEntity> retrieveAllAssetCategories(Long companyId) {

        return categoryService.retrieveAllCategories(companyId, CategoryType.ASSET);
    }

    @Override
    public List<HkCategoryEntity> retrieveAllRootAssetCategories(Long companyId) {
        return categoryService.retrieveAllRootCategories(companyId, CategoryType.ASSET);
    }

    @Override
    public boolean updateAssetCategory(HkCategoryEntity category) {
        return categoryService.updateCategory(category, CategoryType.ASSET);
    }

    @Override
    public boolean removeAssetCategory(Long categoryId) {
        categoryService.removeCategory(categoryId);
        List<HkAssetEntity> assetEntities = this.retrieveAssets(categoryId, null, false);
        if (!CollectionUtils.isEmpty(assetEntities)) {
            for (HkAssetEntity hkAssetEntity : assetEntities) {
                hkAssetEntity.setIsArchive(true);
                hkAssetEntity.setStatus(HkSystemConstantUtil.ASSET_STATUS.ARCHIVED);
            }
            commonDao.saveAll(assetEntities);
        }
        return true;
    }

    @Override
    public boolean createAssetCategory(HkCategoryEntity category) {
        return categoryService.createCategory(category, CategoryType.ASSET);
    }

    @Override
    public List<HkMasterEntity> retrieveMasters(long companyId, short userPrecedence, String type, Boolean isArchive) {
        Search search = SearchFactory.getSearch(HkMasterEntity.class);
        if (type != null) {
            search.addFilterEqual(HkFoundationService.MASTER.MASTER_TYPE, type);
        }
        search.addFilterIn(MASTER.COMPANY, getCompnies(companyId));
        if (isArchive != null) {
            search.addFilterIn(MASTER.IS_ARCHIVED, isArchive);
        }
        search.addFilterGreaterOrEqual(MASTER.PRECEDENCE, userPrecedence);
        return commonDao.search(search);
    }

    @Override
    public void copyMasterValuesFromFranchise(Long sourceFranchise, Long destinationFranchise) {
        Search search = SearchFactory.getActiveSearch(HkValueEntity.class);
        search.addFilterIn(MASTER.COMPANY, sourceFranchise);
        search.addFilterNotEqual(VALUES.MASTER_TPYE, "C");
        List<HkValueEntity> valueEntities = commonDao.search(search);
        if (!CollectionUtils.isEmpty(valueEntities)) {
            List<HkValueEntity> valueEntitiesForNF = new LinkedList<>();
            for (HkValueEntity valueEntity : valueEntities) {
                valueEntitiesForNF.add(new HkValueEntity(null, valueEntity.getShortcutCode(), valueEntity.getValueName(), valueEntity.getIsOftenUsed(), destinationFranchise, true, false, valueEntity.getCreatedBy(), new Date(), valueEntity.getLastModifiedBy(), new Date(), valueEntity.getKeyCode()));
            }
            commonDao.saveAll(valueEntitiesForNF);
        }
    }

    /**
     *
     * @param password
     * @param encryptedPassword
     * @return
     */
    @Override
    public boolean authenticateForEditMaster(String password, String encryptedPassword) {
//        TODO: Implementation 
//        Search search = SearchFactory.getSearch();
//        System.out.println("============== password: " + password + "encryptedPassword :" + encryptedPassword);
        BasicPasswordEncryptor basicPasswordEncryptor = new BasicPasswordEncryptor();
        return basicPasswordEncryptor.checkPassword(password, encryptedPassword);
    }

    @Override
    public boolean doesMasterNameExist(long companyId, String masterName) {
        Search search = SearchFactory.getSearch(HkMasterEntity.class);
        search.addFilterIn(MASTER.COMPANY, getCompnies(companyId));
        search.addFilterEqual(MASTER.NAME, masterName);
        return commonDao.count(search) > 0;
    }

    @Override
    public HkMasterEntity retrieveMaster(String masterCode, short userPrecedence, Long companyId) {
        Search search = SearchFactory.getSearch(HkMasterEntity.class);
        search.addFilterEqual(MASTER.CODE, masterCode);
        search.addFilterIn(MASTER.COMPANY, getCompnies(companyId));
        search.setDistinct(true);
        search.addFilterGreaterOrEqual(MASTER.PRECEDENCE, userPrecedence);
        search.addFetch(MASTER.VALUES);
        HkMasterEntity hkMasterEntity = (HkMasterEntity) commonDao.searchUnique(search);
        List<I18nLabelEntity> i18nLabelEntitys = i18nService.getLabelByKeyAndEntityAndTypeAndCompanyId(masterCode, null, "MASTER", companyId);
        if (hkMasterEntity != null) {
            List<HkValueEntity> hkValueEntityList = hkMasterEntity.getHkValueEntityList();
            if (!CollectionUtils.isEmpty(hkValueEntityList)) {
                Iterator<HkValueEntity> itr = hkValueEntityList.iterator();
                while (itr.hasNext()) {
                    HkValueEntity valueEntity = itr.next();
                    if (valueEntity.getFranchise() != companyId && valueEntity.getFranchise() != 0) {
                        itr.remove();
                    }
                }
            }

            if (!CollectionUtils.isEmpty(i18nLabelEntitys)) {
                Map<String, Map<String, String>> labelMap = new HashMap<>();
                for (I18nLabelEntity i18nLabelEntity : i18nLabelEntitys) {
                    Map<String, String> map = labelMap.get(i18nLabelEntity.getLabelPK().getKey());
                    if (map == null) {
                        map = new HashMap<>();
                        labelMap.put(i18nLabelEntity.getLabelPK().getKey(), map);
                    }
                    map.put(i18nLabelEntity.getLabelPK().getLanguage(), i18nLabelEntity.getText());
                }

                if (!CollectionUtils.isEmpty(hkValueEntityList)) {
                    for (HkValueEntity hkValueEntity : hkValueEntityList) {
                        if (hkValueEntity.getFranchise() == companyId || hkValueEntity.getFranchise() == 0) {
                            hkValueEntity.setTranslateValueMap(labelMap.get(hkValueEntity.getValueName()));
                        }
                    }
                }
            }
        }
        return hkMasterEntity;
    }

    @Override
    public HkMasterEntity retrieveMaster(String masterCode, short userPrecedence) {
        Search search = SearchFactory.getSearch(HkMasterEntity.class);
        search.addFilterEqual(MASTER.CODE, masterCode);
        search.setDistinct(true);
        search.addFilterGreaterOrEqual(MASTER.PRECEDENCE, userPrecedence);
        search.addFetch(MASTER.VALUES);
        HkMasterEntity hkMasterEntity = (HkMasterEntity) commonDao.searchUnique(search);
        return hkMasterEntity;
    }

    @Override
    public Map<String, HkMasterEntity> retrieveMaster(List<String> masterCodes, short userPrecedence) {
        if (!CollectionUtils.isEmpty(masterCodes)) {
            Search search = SearchFactory.getSearch(HkMasterEntity.class);
            search.addFilterIn(MASTER.CODE, masterCodes);
            search.addFilterGreaterOrEqual(MASTER.PRECEDENCE, userPrecedence);
            search.addFetch(MASTER.VALUES);
            List<HkMasterEntity> masterEntities = commonDao.search(search);

            Map<String, HkMasterEntity> masterDataMap = new LinkedHashMap<>();
            if (!CollectionUtils.isEmpty(masterEntities)) {
                for (HkMasterEntity hkMasterEntity : masterEntities) {
                    masterDataMap.put(hkMasterEntity.getCode(), hkMasterEntity);
                }
            }
            return masterDataMap;
        } else {
            return null;
        }
    }

    @Override
    public Map<String, HkMasterEntity> retrieveMaster(List<String> masterCodes) {
        if (!CollectionUtils.isEmpty(masterCodes)) {
            Search search = SearchFactory.getSearch(HkMasterEntity.class);
            search.addFilterIn(MASTER.CODE, masterCodes);
            search.addFetch(MASTER.VALUES);
            List<HkMasterEntity> masterEntities = commonDao.search(search);

            Map<String, HkMasterEntity> masterDataMap = new LinkedHashMap<>();
            if (!CollectionUtils.isEmpty(masterEntities)) {
                for (HkMasterEntity hkMasterEntity : masterEntities) {
                    masterDataMap.put(hkMasterEntity.getCode(), hkMasterEntity);
                }
            }
            return masterDataMap;
        } else {
            return null;
        }
    }

    @Override
    public boolean createMaster(HkMasterEntity hkMasterEntity) {
        //Don't trust the master entity for setting this value. :P
        hkMasterEntity.setMasterType(MasterType.CUSTOM.value());
        return commonDao.save(hkMasterEntity);
    }

    @Override
    public boolean updateMaster(HkMasterEntity hkMasterEntity, List<Long> notifyUserList, Long companyId, Boolean isDelete) {

        HkMasterEntity oldMasterEntity = commonDao.find(HkMasterEntity.class, hkMasterEntity.getCode());
        if (oldMasterEntity != null) {
            oldMasterEntity.setIsSensitive(hkMasterEntity.getIsSensitive());
            oldMasterEntity.setLastModifiedBy(hkMasterEntity.getLastModifiedBy());
            oldMasterEntity.setLastModifiedOn(hkMasterEntity.getLastModifiedOn());
            oldMasterEntity.setHkValueEntityList(hkMasterEntity.getHkValueEntityList());
            List<HkValueEntity> hkValueEntitys = oldMasterEntity.getHkValueEntityList();

            //store label
            if (!CollectionUtils.isEmpty(hkValueEntitys)) {
                List<I18nLabelEntity> i18nLabelEntitys = new ArrayList<>();
                for (HkValueEntity hkValueEntity : hkValueEntitys) {
                    if (!CollectionUtils.isEmpty(hkValueEntity.getTranslateValueMap())) {
                        for (Map.Entry<String, String> translatedValueMap : hkValueEntity.getTranslateValueMap().entrySet()) {
                            String language = translatedValueMap.getKey();
                            String value = translatedValueMap.getValue();
                            I18nLabelEntity i18nEngLabelEntity = new I18nLabelEntity();
                            I18nLabelPKEntity i18nEngLabelPKEntity = new I18nLabelPKEntity();
                            i18nEngLabelPKEntity.setCompany(hkValueEntity.getFranchise());
                            i18nEngLabelPKEntity.setCountry(HkSystemConstantUtil.COUNTRY_NZ);
                            i18nEngLabelPKEntity.setEntity(hkMasterEntity.getCode());
                            i18nEngLabelPKEntity.setKey(hkValueEntity.getValueName());
                            i18nEngLabelPKEntity.setLanguage(language);
                            i18nEngLabelPKEntity.setType("MASTER");
                            i18nEngLabelEntity.setLabelPK(i18nEngLabelPKEntity);
                            i18nEngLabelEntity.setLastModifiedBy(hkMasterEntity.getLastModifiedBy());
                            i18nEngLabelEntity.setLastModifiedOn(new Date());
                            i18nEngLabelEntity.setText(value);
                            i18nEngLabelEntity.setTranslationPending(Boolean.FALSE);
                            i18nEngLabelEntity.setEnvironment("w");
                            i18nLabelEntitys.add(i18nEngLabelEntity);
                        }
                    }
                }
                i18nService.updateAllLabels(i18nLabelEntitys);
            }
        }
        commonDao.getCurrentSession().merge(oldMasterEntity);
        //  Send notifications
        Map<String, Object> valuesMap = new HashMap<>();
        valuesMap.put(HkSystemConstantUtil.NotificationMessageParam.MASTER_NAME, hkMasterEntity.getMasterName());
        HkNotificationEntity notification = null;
        /*
         * a boolean field isDelete added by Shifa on 16 March 2015 so as to
         * identify whether master needs to be updated or deleted and
         * accordingly notification message needs to be sent
         */
        if (isDelete != null && isDelete) {
            notification = createNotification(HkSystemConstantUtil.NotificationType.MASTER,
                    HkSystemConstantUtil.NotificationInstanceType.DELETE_MASTER, valuesMap, null, companyId);
        } else {
            notification = createNotification(HkSystemConstantUtil.NotificationType.MASTER,
                    HkSystemConstantUtil.NotificationInstanceType.UPDATE_MASTER, valuesMap, null, companyId);
        }
        notificationService.sendNotification(notification, notifyUserList);
        return true;
    }

    @Override
    public boolean removeMaster(String masterCode) {
        HkMasterEntity masterEntity = commonDao.find(HkMasterEntity.class, masterCode);
        masterEntity.setIsArchive(true);
        //no need to archive values as values without master does not mean anything for us.
        commonDao.save(masterEntity);
        return true;
    }

    @Override
    public List<HkMasterEntity> searchMaster(long companyId, String masterName, short userPrecedence) {
        Search search = SearchFactory.getSearch(HkMasterEntity.class);
        search.addFilterIn(MASTER.COMPANY, getCompnies(companyId));
        search.addFilterILike(MASTER.NAME, "%" + masterName + "%");
        search.addFilterGreaterOrEqual(MASTER.PRECEDENCE, userPrecedence);
        search.addFilterEqual(MASTER.IS_ACTIVE, true);
//        search.addFetch(MASTER.VALUES);
        return commonDao.search(search);

    }

    @Override
    public List<Long> searchMasterValues(long companyId, String masterCode, String searchValue) {
        Search search = SearchFactory.getSearch(HkValueEntity.class);
        search.addFilterIn(MASTER.COMPANY, getCompnies(companyId));
        search.addFilterEqual(VALUES.MASTER_CODE, masterCode);
        search.addFilterILike(VALUES.VALUE_NAME, "%" + searchValue + "%");
        search.addField(VALUES.ID);
        return commonDao.search(search);
    }

    @Override
    public List<HkValueEntity> retrieveMasterValuesByCode(long companyId, List<String> masterCodes) {
        Search search = SearchFactory.getActiveSearch(HkValueEntity.class);
        search.addFilterIn(MASTER.COMPANY, getCompnies(companyId));
        search.addFilterIn(VALUES.MASTER_CODE, masterCodes);
        search.addSorts(Sort.asc(VALUES.MASTER_CODE), Sort.desc(VALUES.OFTEN_USED), Sort.asc(VALUES.SHORTCUT_CODE));
        return commonDao.search(search);
    }

    @Override
    public List<HkValueEntity> retrieveMasterValuesByCodeForCustom(long companyId, String masterCodes, String searchQuery) {
        System.out.println("Serch Query..." + searchQuery);
        Search search = SearchFactory.getActiveSearch(HkValueEntity.class);
        search.addFilterIn(MASTER.COMPANY, getCompnies(companyId));
        search.addFilterEqual(VALUES.MASTER_CODE, masterCodes);
        if (searchQuery != null && !searchQuery.isEmpty()) {
            boolean isShortcutCode;
            Integer scode = null;
            try {
                scode = Integer.parseInt(searchQuery);
                // is an integer!
                isShortcutCode = true;
            } catch (NumberFormatException e) {
                // not an integer!
                isShortcutCode = false;
            }
            System.out.println(" inside thiscoee" + searchQuery);
            if (isShortcutCode) {
                search.addFilterEqual(VALUES.SHORTCUT_CODE, scode);
//                search.addFilterOr(Filter.ilike(VALUES.VALUE_NAME, "%" + searchQuery + "%"), Filter.equal(VALUES.SHORTCUT_CODE, Integer.parseInt(searchQuery)));
            } else {
                search.addFilterILike(VALUES.VALUE_NAME, "%" + searchQuery + "%");
            }
        }
        search.addSorts(Sort.desc(VALUES.OFTEN_USED), Sort.asc(VALUES.SHORTCUT_CODE));
        return commonDao.search(search);

    }

    @Override
    public List<HkValueEntity> retrieveValueEntityListByValueIds(List<Long> valueIds) {
        Search search = SearchFactory.getActiveSearch(HkValueEntity.class);
        if (!CollectionUtils.isEmpty(valueIds)) {
            search.addFilterIn(VALUES.ID, valueIds);
        }
        return commonDao.search(search);
    }

    @Override
    public Map<Long, HkValueEntity> retrieveMapOfIdAndValueEntity(List<Long> valueIds) {
        Map<Long, HkValueEntity> map = new HashMap<>();
        Search search = SearchFactory.getActiveSearch(HkValueEntity.class);
        if (!CollectionUtils.isEmpty(valueIds)) {
            search.addFilterIn(VALUES.ID, valueIds);
        }
        List<HkValueEntity> hkValueEntitys = commonDao.search(search);
        if (!CollectionUtils.isEmpty(hkValueEntitys)) {
            for (HkValueEntity hkValueEntity : hkValueEntitys) {
                map.put(hkValueEntity.getId(), hkValueEntity);
            }
        }
        return map;
    }

    @Override
    public Map<String, List<HkValueEntity>> mapOfKeyIdWithValueEntities(Long companyId) {
        Map<String, List<HkValueEntity>> map = new HashMap<>();
        Search search = SearchFactory.getActiveSearch(HkValueEntity.class);

        List<HkValueEntity> hkValueEntitys = commonDao.search(search);
        if (!CollectionUtils.isEmpty(hkValueEntitys)) {
            List<HkValueEntity> valEntities = null;
            for (HkValueEntity hkValueEntity : hkValueEntitys) {
                if (map.containsKey(hkValueEntity.getKeyCode().getCode())) {
                    valEntities = map.get(hkValueEntity.getKeyCode().getCode());

                } else {
                    valEntities = new ArrayList<>();
                }
                valEntities.add(hkValueEntity);
                map.put(hkValueEntity.getKeyCode().getCode(), valEntities);
            }
        }
        return map;
    }

    @Override
    public void removeAllValueEntitiesOfMasterKey(Long keycode) {
        Search search = SearchFactory.getActiveSearch(HkValueEntity.class);
        if (keycode != null) //To change body of generated methods, choose Tools | Templates.
        {
            search.addFilterEqual(VALUES.MASTER_CODE, keycode);
        }
        List<HkValueEntity> valueEntities = commonDao.search(search);
        if (!CollectionUtils.isEmpty(valueEntities)) {
            for (HkValueEntity valueEntity : valueEntities) {
                valueEntity.setIsArchive(true);
            }
            commonDao.saveAll(valueEntities);

        }
    }

    @Override
    public HkValueEntity searchMasterValue(long valueId) {
        return commonDao.find(HkValueEntity.class, valueId);
    }

    @Override
    public void saveSystemConfigurations(List<HkSystemConfigurationEntity> configList) {
        for (HkSystemConfigurationEntity config : configList) {
            config.setModifiedOn(new Date());
        }
        commonDao.saveAll(configList);
    }

    @Override
    public List<HkSystemConfigurationEntity> retrieveSystemConfigurationByFranchise(Long franchise) {
        Search search = SearchFactory.getSearch(HkSystemConfigurationEntity.class);
        search.addFilterEqual(Fields.HkSystemConfiguration.FRANCHISE, franchise);
        return commonDao.search(search);
    }

    @Override
    public HkSystemConfigurationEntity retrieveSystemConfigurationByKey(String key, Long franchise) {
        Search search = SearchFactory.getSearch(HkSystemConfigurationEntity.class);
        search.addFilterEqual(Fields.HkSystemConfiguration.FRANCHISE, franchise);
        search.addFilterEqual(Fields.HkSystemConfiguration.SYSTEM_KEY, key);
        return (HkSystemConfigurationEntity) commonDao.searchUnique(search);
    }

    @Override
    public List<HkSystemConfigurationEntity> retrieveAllSystemConfigurationByKey(String key) {
        Search search = SearchFactory.getSearch(HkSystemConfigurationEntity.class);
        search.addFilterEqual(Fields.HkSystemConfiguration.SYSTEM_KEY, key);
        return commonDao.search(search);
    }

    @Override
    public List<HkMasterEntity> retrieveAllMasters() {
        Search search = SearchFactory.getSearch(HkMasterEntity.class);
        return commonDao.search(search);
    }

    @Override
    public void createAllMasters(List<HkMasterEntity> masterEntitys) {
        if (masterEntitys != null) {
            commonDao.saveAll(masterEntitys);
        }
    }

    /*
     * ---------------------------------- Mange Reference Rate
     * ----------------------------
     */
    @Override
    public boolean addReferenceRate(HkReferenceRateEntity currencyRateEntity) {
        if (currencyRateEntity != null && currencyRateEntity.getCode() != null) {
            Search search = new Search(HkReferenceRateEntity.class);
            search.addFilterEqual(Fields.HkCurrencyReferenceRate.IS_ACTIVE, true);
            search.addFilterEqual(Fields.HkCurrencyReferenceRate.CURRENCY, currencyRateEntity.getCode());
            search.addFilterEqual(Fields.HkCurrencyReferenceRate.COMPANY, currencyRateEntity.getFranchise());
            search.addSort(Sort.desc(Fields.HkCurrencyReferenceRate.UPDATED_ON));
            List<HkReferenceRateEntity> currencyRateEntitys = commonDao.search(search);
            if (!CollectionUtils.isEmpty(currencyRateEntitys)) {
                for (HkReferenceRateEntity hkCurrencyRateEntity : currencyRateEntitys) {
                    hkCurrencyRateEntity.setIsActive(false);
                    hkCurrencyRateEntity.setLastModifiedBy(currencyRateEntity.getLastModifiedBy());
                    hkCurrencyRateEntity.setLastModifiedOn(new Date());
                    currencyRateEntity.setCode(hkCurrencyRateEntity.getCode());
                }
            }
            currencyRateEntity.setLastModifiedOn(new Date());
            currencyRateEntity.setIsActive(true);
            currencyRateEntity.setIsArchive(false);
            return commonDao.save(currencyRateEntity);
        } else {
            return false;
        }
    }

    @Override
    public boolean updateReferenceRate(Long refId, Date applicableFrom, Boolean isActive, Long modifiedBy) {
        boolean result = false;
        if (refId != null && applicableFrom != null && isActive != null && modifiedBy != null) {
            HkReferenceRateEntity referenceRate = commonDao.find(HkReferenceRateEntity.class, refId);
            referenceRate.setApplicableFrom(applicableFrom);
            referenceRate.setLastModifiedBy(modifiedBy);
            referenceRate.setLastModifiedOn(new Date());
            referenceRate.setIsActive(isActive);
            result = true;
        }
        return result;
    }

    @Override
    public List<HkReferenceRateEntity> retrieveCurrentCurrencyRate(Boolean isActive, Long franchiseId) {
        if (franchiseId != null) {
            Search search = new Search(HkReferenceRateEntity.class);
            if (isActive != null) {
                search.addFilterEqual(Fields.HkCurrencyReferenceRate.IS_ACTIVE, isActive);
            }
            if (franchiseId <= 0l) {
                search.addFilterEqual(Fields.HkCurrencyReferenceRate.COMPANY, franchiseId);
            } else {
                search.addFilterIn(Fields.HkCurrencyReferenceRate.COMPANY, (Object[]) new Long[]{0L, franchiseId});
            }

            return commonDao.search(search);
        } else {
            return null;
        }
    }

    @Override
    public Map<String, List<HkReferenceRateEntity>> retrieveCurrentRateAndCurrencyHistory(int numberOfRecords, Long franchiseId) {
        Map<String, List<HkReferenceRateEntity>> currencyCodeHistoryMap = null;
        if (franchiseId != null) {
            Search search = new Search(HkReferenceRateEntity.class);
            if (franchiseId <= 0l) {
                search.addFilterEqual(Fields.HkCurrencyReferenceRate.COMPANY, franchiseId);
            } else {
                search.addFilterIn(Fields.HkCurrencyReferenceRate.COMPANY, (Object[]) new Long[]{0L, franchiseId});
            }
            search.addSort(Sort.desc(Fields.HkCurrencyReferenceRate.UPDATED_ON));
//            search.setFirstResult(0);
            search.setMaxResults(numberOfRecords);
            List<HkReferenceRateEntity> currencyRateEntitys = commonDao.search(search);
            if (!CollectionUtils.isEmpty(currencyRateEntitys)) {
                currencyCodeHistoryMap = new LinkedHashMap<>();
                List<HkReferenceRateEntity> referenceRateEntityList;
                for (HkReferenceRateEntity hkCurrencyRateEntity : currencyRateEntitys) {
                    if (!currencyCodeHistoryMap.containsKey(hkCurrencyRateEntity.getCode())) {
                        referenceRateEntityList = new ArrayList<>();
                        referenceRateEntityList.add(hkCurrencyRateEntity);
                    } else {
                        referenceRateEntityList = currencyCodeHistoryMap.get(hkCurrencyRateEntity.getCode());
                        referenceRateEntityList.add(hkCurrencyRateEntity);
                    }
                    currencyCodeHistoryMap.put(hkCurrencyRateEntity.getCode(), referenceRateEntityList);
                }
            }
        }
        return currencyCodeHistoryMap;
    }

    @Override
    public List<HkReferenceRateEntity> retrieveAllCurrencyRates(Boolean isActive, Long franchiseId) {
        if (franchiseId != null) {
            Search search = new Search(HkReferenceRateEntity.class);
            if (isActive != null) {
                search.addFilterEqual(Fields.HkCurrencyReferenceRate.IS_ACTIVE, isActive);
            }
            if (franchiseId <= 0l) {
                search.addFilterEqual(Fields.HkCurrencyReferenceRate.COMPANY, franchiseId);
            } else {
                search.addFilterIn(Fields.HkCurrencyReferenceRate.COMPANY, (Object[]) new Long[]{0L, franchiseId});
            }
            search.addFilterEqual(Fields.HkCurrencyReferenceRate.COMPANY, franchiseId);
            return commonDao.search(search);
        } else {
            return null;
        }
    }

    @Override
    public void createAllCurrencyRates(List<HkReferenceRateEntity> currencyRateEntitys) {
        if (currencyRateEntitys != null) {
            commonDao.saveAll(currencyRateEntitys);
        }
    }

    @Override
    public List<HkValueEntity> retrieveMasterValuesOfSameKeyCodeByCustomFieldId(Long id) {
        Search search = new Search(HkValueEntity.class);
        search.addFilterEqual(Fields.HkValueMaster.KEY_CODE, id);
        return commonDao.search(search);
    }

    @Override
    public List<HkValueEntity> retrieveAllValueEntities() {
        Search search = new Search(HkValueEntity.class);
        return commonDao.search(search);
    }

    @Override
    public void createAllThemes(List<HkTheme> themes) {
        commonDao.saveAll(themes);
    }

    @Override
    public List<HkTheme> retrieveAllThemes(String status) {
        Search search = new Search(HkTheme.class);
        if (status != null) {
            search.addFilterEqual(Fields.HkTheme.STATUS, status);
        }
        return commonDao.search(search);
    }

    @Override
    public void addGoalPermissions(List<HkGoalPermissionEntity> goalPermissionEntitys) {
        commonDao.saveAll(goalPermissionEntitys);
    }

    @Override
    public void updateGoalPermissions(List<HkGoalPermissionEntity> goalPermissionEntitys, List<String> type) {
        Search search = new Search(HkGoalPermissionEntity.class);

        if (!CollectionUtils.isEmpty(goalPermissionEntitys)) {

            search.addFilterEqual(Fields.HkGoalPermission.DESIGNATION, goalPermissionEntitys.get(0).getDesignation());
            if (!CollectionUtils.isEmpty(type)) {
                search.addFilterIn(Fields.HkGoalPermission.ACCESS_OF_FEATURE, type);
            }
            search.addFilterEqual(Fields.HkGoalPermission.IS_ARCHIVE, false);
            List<HkGoalPermissionEntity> activeGoalPermissions = commonDao.search(search);

            if (!CollectionUtils.isEmpty(activeGoalPermissions)) {
                Iterator iterator = activeGoalPermissions.iterator();
                while (iterator.hasNext()) {
                    HkGoalPermissionEntity activePermission = (HkGoalPermissionEntity) iterator.next();
                    Iterator iterator1 = goalPermissionEntitys.iterator();
                    while (iterator1.hasNext()) {
                        HkGoalPermissionEntity goalPermissionEntity = (HkGoalPermissionEntity) iterator1.next();
                        if (activePermission.getDesignation() == goalPermissionEntity.getDesignation() && activePermission.getReferenceType().equals(goalPermissionEntity.getReferenceType()) && activePermission.getReferenceInstance() == goalPermissionEntity.getReferenceInstance()) {
                            iterator.remove();
                            iterator1.remove();
                            break;
                        }
                    }

                }
                List<HkGoalPermissionEntity> goalPermissionEntitys1 = new ArrayList<>();
                for (HkGoalPermissionEntity hkGoalPermissionEntity : activeGoalPermissions) {
                    hkGoalPermissionEntity.setIsArchive(true);
                    goalPermissionEntitys1.add(hkGoalPermissionEntity);
                }
                commonDao.saveAll(goalPermissionEntitys1);
            }
            if (!CollectionUtils.isEmpty(goalPermissionEntitys)) {
                commonDao.saveAll(goalPermissionEntitys);
            }
        }
    }

    @Override
    public void deleteGoalPermissions(List<Long> permissions) {
        Search search = new Search(HkGoalPermissionEntity.class);

        if (permissions != null) {
            search.addFilterIn(Fields.HkGoalPermission.ID, permissions);
        }
        List<HkGoalPermissionEntity> goalPermissions = commonDao.search(search);

        if (!CollectionUtils.isEmpty(goalPermissions)) {
            List<HkGoalPermissionEntity> result = new ArrayList<>();

            for (HkGoalPermissionEntity hkGoalPermissionEntity : goalPermissions) {
                hkGoalPermissionEntity.setIsArchive(Boolean.TRUE);
                result.add(hkGoalPermissionEntity);
            }
            commonDao.saveAll(result);
        }

    }

    @Override
    public HkGoalPermissionEntity retrieveGoalPermissionById(Long id) {
        return commonDao.find(HkGoalPermissionEntity.class, id);
    }

    @Override
    public List<HkGoalPermissionEntity> retrieveActiveGoalPermissionByDesignation(Long designation) {
        Search search = new Search(HkGoalPermissionEntity.class);

        search.addFilterEqual(Fields.HkGoalPermission.DESIGNATION, designation);
        search.addFilterEqual(Fields.HkGoalPermission.IS_ARCHIVE, false);

        return commonDao.search(search);
    }

    @Override
    public List<HkGoalPermissionEntity> retrieveGoalPermissions(String accessOfFeature, long designation) {
        Search search = new Search(HkGoalPermissionEntity.class);

        if (accessOfFeature != null) {
            search.addFilterLike(Fields.HkGoalPermission.ACCESS_OF_FEATURE, accessOfFeature);
        }
        search.addFilterEqual(Fields.HkGoalPermission.DESIGNATION, designation);

        search.addFilterEqual(Fields.HkGoalPermission.IS_ARCHIVE, Boolean.FALSE);

        return commonDao.search(search);
    }

    @Override
    public void saveGoalTemplates(List<HkGoalTemplateEntity> goalTemplates) {
        commonDao.saveAll(goalTemplates);
    }

    @Override
    public Long saveGoalTemplate(HkGoalTemplateEntity goalTemplate) {
        commonDao.save(goalTemplate);
        return goalTemplate.getId();
    }

    @Override
    public List<HkGoalTemplateEntity> retrieveActiveGoalTemplatesByDesignation(Long designation, Long franchise) {
        Search search = new Search(HkGoalTemplateEntity.class);

        search.addFilterEqual(Fields.HkGoalTemplate.FOR_DESIGNATION, designation);
        search.addFilterLike(Fields.HkGoalTemplate.STATUS, HkSystemConstantUtil.GoalTemplateStatus.ACTIVE);
        if (franchise != null) {
            search.addFilterEqual(Fields.HkGoalTemplate.FRANCHISE, franchise);
        }

        return commonDao.search(search);
    }

    @Override
    public List<HkGoalTemplateEntity> retrieveActiveGoalTemplatesByService(Long service, Long franchise) {
        Search search = new Search(HkGoalTemplateEntity.class);

        search.addFilterEqual(Fields.HkGoalTemplate.FOR_SERVICE, service);
        search.addFilterLike(Fields.HkGoalTemplate.STATUS, HkSystemConstantUtil.GoalTemplateStatus.ACTIVE);
        if (franchise != null) {
            search.addFilterEqual(Fields.HkGoalTemplate.FRANCHISE, franchise);
        }

        return commonDao.search(search);
    }

    @Override
    public List<HkGoalTemplateEntity> retrieveActiveGoalTemplatesByDepartment(Long department, Long franchise) {
        Search search = new Search(HkGoalTemplateEntity.class);

        search.addFilterEqual(Fields.HkGoalTemplate.FOR_DEPARTMENT, department);
        search.addFilterLike(Fields.HkGoalTemplate.STATUS, HkSystemConstantUtil.GoalTemplateStatus.ACTIVE);
        if (franchise != null) {
            search.addFilterEqual(Fields.HkGoalTemplate.FRANCHISE, franchise);
        }

        return commonDao.search(search);
    }

    @Override
    public List<HkGoalTemplateEntity> retrievePendingGoalTemplates(Long franchise) {
        Search search = new Search(HkGoalTemplateEntity.class);

        search.addFilterEqual(Fields.HkGoalTemplate.FRANCHISE, franchise);
        search.addFilterLike(Fields.HkGoalTemplate.STATUS, HkSystemConstantUtil.GoalTemplateStatus.PENDING);

        return commonDao.search(search);
    }

    @Override
    public HkGoalTemplateEntity retrieveGoalTemplateById(Long id) {
        return commonDao.find(HkGoalTemplateEntity.class, id);
    }

    @Override
    public List<HkGoalPermissionEntity> retrieveActiveGoalPermissionByDesignations(List<Long> designations) {
        Search search = new Search(HkGoalPermissionEntity.class);

        search.addFilterIn(Fields.HkGoalPermission.DESIGNATION, designations);
        search.addFilterEqual(Fields.HkGoalPermission.IS_ARCHIVE, false);

        return commonDao.search(search);
    }

    @Override
    public Long saveGoalTemplateWithRules(HkGoalTemplateEntity goalTemplateEntity, HkRuleSetDocument ruleSetDocument) {
        if (goalTemplateEntity.getTemplateType().equals(HkSystemConstantUtil.GoalTemplateType.NUMERIC)) {
            ruleService.saveOrUpdateRuleSet(ruleSetDocument);
            goalTemplateEntity.setRealizationRule(ruleSetDocument.getId().toString());
        }
        commonDao.save(goalTemplateEntity);
        return goalTemplateEntity.getId();
    }

    @Override
    public List<HkGoalTemplateEntity> retrieveActiveGoalTemplatesByFranchise(Long franchise) {
        Search search = new Search(HkGoalTemplateEntity.class);

        search.addFilterEqual(Fields.HkGoalTemplate.FRANCHISE, franchise);

        return commonDao.search(search);
    }

    @Override
    public List<HkGoalTemplateEntity> retrieveGoalTemplates(Long franchise, String status) {
        Search search = new Search(HkGoalTemplateEntity.class);

        if (franchise != null) {
            search.addFilterEqual(Fields.HkGoalTemplate.FRANCHISE, franchise);
        }
        if (status != null) {
            search.addFilterEqual(Fields.HkGoalTemplate.STATUS, status);
        }

        return commonDao.search(search);
    }

    @Override
    public List<HkGoalTemplateEntity> searchGoalTemplateByName(String goalTemplateName, long companyId, List<String> status, Boolean applyLimit) {
        Search search = null;
        if (applyLimit) {
            if (StringUtils.hasText(goalTemplateName) && goalTemplateName.length() >= HkSystemConstantUtil.MIN_SEARCH_LENGTH) {
                search = new Search(HkGoalTemplateEntity.class);
                search.addFilterEqual(Fields.HkGoalTemplate.FRANCHISE, companyId);
                search.addFilterIn(Fields.HkGoalTemplate.STATUS, status);
                search.addFilterILike(Fields.HkGoalTemplate.NAME, "%" + goalTemplateName + "%");
            }
        } else {
            search = new Search(HkGoalTemplateEntity.class);
            search.addFilterEqual(Fields.HkGoalTemplate.FRANCHISE, companyId);
            search.addFilterIn(Fields.HkGoalTemplate.STATUS, status);
            search.addFilterILike(Fields.HkGoalTemplate.NAME, "%" + goalTemplateName + "%");
        }
        if (search != null) {
            return commonDao.search(search);
        }
        return null;
    }

    @Override
    public void updateGoalTemplates(List<HkGoalTemplateEntity> goalTemplateEntitys) {
        Map<String, List<HkGoalTemplateEntity>> goalTemplatesMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(goalTemplateEntitys)) {
            List<HkGoalTemplateEntity> activeGoalTemplates = new ArrayList<>();
            List<HkGoalTemplateEntity> finalList = new ArrayList<>();
            if (goalTemplateEntitys.get(0).getForService() != null) {
                activeGoalTemplates = this.retrieveActiveGoalTemplatesByService(goalTemplateEntitys.get(0).getForService(), null);
            } else if (goalTemplateEntitys.get(0).getForDesignation() != null) {
                activeGoalTemplates = this.retrieveActiveGoalTemplatesByDesignation(goalTemplateEntitys.get(0).getForDesignation(), null);
            } else if (goalTemplateEntitys.get(0).getForDepartment() != null) {
                activeGoalTemplates = this.retrieveActiveGoalTemplatesByDepartment(goalTemplateEntitys.get(0).getForDepartment(), null);
            }
            for (HkGoalTemplateEntity goalTemplateEntity : goalTemplateEntitys) {

                if (goalTemplatesMap.get(goalTemplateEntity.getStatus()) == null) {
                    goalTemplatesMap.put(goalTemplateEntity.getStatus(), new ArrayList<HkGoalTemplateEntity>());
                }
                String status = goalTemplateEntity.getStatus();
                if (goalTemplateEntity.getStatus().equals(HkSystemConstantUtil.GoalTemplateStatus.PENDING)) {
                    goalTemplateEntity.setStatus(HkSystemConstantUtil.GoalTemplateStatus.ACTIVE);
                }
                goalTemplatesMap.get(status).add(goalTemplateEntity);
            }
            if (goalTemplatesMap.get(HkSystemConstantUtil.GoalTemplateStatus.PENDING) != null) {
//                commonDao.saveAll(goalTemplatesMap.get(HkSystemConstantUtil.GoalTemplateStatus.PENDING));
                finalList.addAll(goalTemplatesMap.get(HkSystemConstantUtil.GoalTemplateStatus.PENDING));
            }

            if (goalTemplatesMap.get(HkSystemConstantUtil.GoalTemplateStatus.ACTIVE) != null) {
                Iterator iterator = goalTemplatesMap.get(HkSystemConstantUtil.GoalTemplateStatus.ACTIVE).iterator();
                Iterator iterator1 = activeGoalTemplates.iterator();

                while (iterator.hasNext()) {
                    HkGoalTemplateEntity templateEntity = (HkGoalTemplateEntity) iterator.next();
                    while (iterator1.hasNext()) {
                        HkGoalTemplateEntity templateEntity1 = (HkGoalTemplateEntity) iterator1.next();
                        if (templateEntity.getId() == templateEntity1.getId()) {
                            iterator.remove();
                            iterator1.remove();
                            break;
                        }
                    }
                }
                List<HkGoalTemplateEntity> goalTemplatesToClone = new ArrayList<>();
                for (HkGoalTemplateEntity hkGoalTemplateEntity : goalTemplatesMap.get(HkSystemConstantUtil.GoalTemplateStatus.ACTIVE)) {
                    hkGoalTemplateEntity.setId(null);
                    goalTemplatesToClone.add(hkGoalTemplateEntity);
                }
                if (!CollectionUtils.isEmpty(goalTemplatesToClone)) {
//                    commonDao.saveAll(goalTemplatesToClone);
                    finalList.addAll(goalTemplatesToClone);
                }
                if (!CollectionUtils.isEmpty(activeGoalTemplates)) {
                    List<HkGoalTemplateEntity> goalTemplatesToRemove = new ArrayList<>();
                    for (HkGoalTemplateEntity hkGoalTemplateEntity : activeGoalTemplates) {
                        hkGoalTemplateEntity.setStatus(HkSystemConstantUtil.GoalTemplateStatus.DISCARDED);
                        goalTemplatesToRemove.add(hkGoalTemplateEntity);
                    }
//                    commonDao.saveAll(goalTemplatesToRemove);
                    finalList.addAll(goalTemplatesToRemove);
                }
            }
            if (!CollectionUtils.isEmpty(finalList)) {
                commonDao.saveAll(finalList);
            }
        }
    }

    @Override
    public void addCaratRanges(List<HkCaratRangeEntity> caratRangeEntities) {
        commonDao.saveAll(caratRangeEntities);
    }

    @Override
    public List<HkCaratRangeEntity> retrieveCaratRangeByFranchiseAndStatus(Long franchise, List<String> status, List<Long> caratId) {
        Search search = new Search(HkCaratRangeEntity.class);

        search.addFilterIn(Fields.HkCaratEntity.FRANCHISE, getCompnies(franchise));
        if (!CollectionUtils.isEmpty(status)) {
            search.addFilterIn(Fields.HkCaratEntity.STATUS, status);
        }
        if (!CollectionUtils.isEmpty(caratId)) {
            search.addFilterIn(Fields.HkCaratEntity.ID, caratId);
        }

        return commonDao.search(search);
    }

    @Override
    public void deactivateCaratRanges(List<Long> ids) {
        if (!CollectionUtils.isEmpty(ids)) {
            Search search = new Search(HkCaratRangeEntity.class);

            search.addFilterIn(Fields.HkCaratEntity.ID, ids);
            List listOfCarat = commonDao.search(search);

            if (!CollectionUtils.isEmpty(listOfCarat)) {
                List<HkCaratRangeEntity> toArchive = new ArrayList<>();
                for (Object object : listOfCarat) {
                    HkCaratRangeEntity caratRangeEntity = (HkCaratRangeEntity) object;
                    caratRangeEntity.setStatus(HkSystemConstantUtil.ARCHIVED);
                    toArchive.add(caratRangeEntity);
                }

                commonDao.saveAll(toArchive);
            }
        }
    }

    @Override
    public Map<String, List<String>> retrieveMapOfFieldsValuesForPriceList(Long franchise) {
        Map<String, List<String>> result = new LinkedHashMap<>();

        //Retrieve active carat ranges
        List<HkCaratRangeEntity> caratRanges = this.retrieveCaratRangeByFranchiseAndStatus(franchise, Arrays.asList(HkSystemConstantUtil.ACTIVE), null);
        if (!CollectionUtils.isEmpty(caratRanges)) {
            for (HkCaratRangeEntity hkCaratRangeEntity : caratRanges) {
                if (result.get("Carat range") == null) {
                    result.put("Carat range", new ArrayList<>());
                }
                StringBuilder builder = new StringBuilder();
                builder.append(hkCaratRangeEntity.getMinValue()).append(" - ").append(hkCaratRangeEntity.getMaxValue());
                result.get("Carat range").add(builder.toString());
            }
        }

        List<String> priceListCodes = new ArrayList<>();
        //Retrieve fields of class PriceListCode from Constant Util

        Map<String, HkFieldEntity> fields = fieldService.retrieveMapOfDBFieldNameWithEntity(Arrays.asList(HkSystemConstantUtil.PlanStaticFieldName.CLARITY, HkSystemConstantUtil.PlanStaticFieldName.COLOR, HkSystemConstantUtil.PlanStaticFieldName.CUT, HkSystemConstantUtil.PlanStaticFieldName.FLUROSCENE), franchise);
        Map<String, String> fieldsName = new HashMap<>();
        if (!CollectionUtils.isEmpty(fields)) {
            for (Map.Entry<String, HkFieldEntity> entry : fields.entrySet()) {
                priceListCodes.add(String.valueOf(entry.getValue().getId()));
                fieldsName.put(String.valueOf(entry.getValue().getId()), entry.getValue().getFieldLabel());
            }
        }
        //For each code, retrieve values from master
        if (!CollectionUtils.isEmpty(priceListCodes)) {
            List<HkValueEntity> values = this.retrieveMasterValuesByCode(franchise, priceListCodes);
            if (!CollectionUtils.isEmpty(values)) {
                for (HkValueEntity hkValueEntity : values) {
                    if (result.get(fieldsName.get(hkValueEntity.getKeyCode().getCode())) == null) {
                        result.put(fieldsName.get(hkValueEntity.getKeyCode().getCode()), new ArrayList<>());
                    }
                    result.get(fieldsName.get(hkValueEntity.getKeyCode().getCode())).add(hkValueEntity.getValueName());
                }
            }
        }
        return result;
    }

    @Override
    public Map<String, Map<String, Long>> retrieveMapOfValuesFromCode(List<String> codeList, Long franchise) {
        if (!CollectionUtils.isEmpty(codeList)) {
            Search search = new Search(HkValueEntity.class);
            Map<String, Map<String, Long>> result = new LinkedHashMap<>();

            search.addFilterIn(Fields.HkValueMaster.KEY_CODE, codeList);
            search.addFilterIn(Fields.HkValueMaster.FRANCHISE, getCompnies(franchise));
            List<HkValueEntity> values = commonDao.search(search);

            if (!CollectionUtils.isEmpty(values)) {
                for (HkValueEntity hkValueEntity : values) {
                    if (result.get(hkValueEntity.getKeyCode().getCode()) == null) {
                        result.put(hkValueEntity.getKeyCode().getCode(), new LinkedHashMap<>());
                    }
                    result.get(hkValueEntity.getKeyCode().getCode()).put(hkValueEntity.getValueName(), hkValueEntity.getId());
                }
            }
            return result;
        }
        return null;
    }

    @Override
    public Long savePriceList(HkPriceListEntity priceListEntity) {
        commonDao.save(priceListEntity);
        return priceListEntity.getId();
    }

    @Override
    public void savePriceListDetailEntities(List<HkPriceListDetailEntity> priceListDetailEntities) {
        commonDao.saveAll(priceListDetailEntities);
    }

    @Override
    public void archiveOldPriceList(Long companyId) {
        Search search = new Search(HkPriceListEntity.class);

        search.addFilterIn(Fields.HkPriceList.FRANCHISE, getCompnies(companyId));
        search.addFilterEqual(Fields.HkPriceList.STATUS, HkSystemConstantUtil.ACTIVE);
        //Retrieve active price list of franchise
        List<HkPriceListEntity> activePriceList = commonDao.search(search);

        if (!CollectionUtils.isEmpty(activePriceList)) {
            for (HkPriceListEntity hkPriceListEntity : activePriceList) {
                //Set status to inactive and save
                hkPriceListEntity.setStatus(HkSystemConstantUtil.INACTIVE);

                if (!CollectionUtils.isEmpty(hkPriceListEntity.getHkPriceListDetailEntityCollection())) {
                    for (HkPriceListDetailEntity hkPriceListDetailEntity : hkPriceListEntity.getHkPriceListDetailEntityCollection()) {
                        hkPriceListDetailEntity.setIsArchive(Boolean.TRUE);
                    }
                }
            }

            commonDao.saveAll(activePriceList);
        }
    }

    @Override
    public List<HkPriceListEntity> retrieveAllPriceLists(Long companyId) {
        Search search = new Search(HkPriceListEntity.class);

        search.addFilterIn(Fields.HkPriceList.FRANCHISE, getCompnies(companyId));

        return commonDao.search(search);
    }

    @Override
    public void createAllMasterValues(List<HkValueEntity> values) {
        if (!CollectionUtils.isEmpty(values)) {
            commonDao.saveAll(values);
        }
    }

    @Override
    public List<HkGoalTemplateEntity> retrieveGoalTemplates(List<Long> ids) {
        if (!CollectionUtils.isEmpty(ids)) {
            Search search = new Search(HkGoalTemplateEntity.class);

            search.addFilterIn(Fields.HkGoalTemplate.ID, ids);

            return commonDao.search(search);
        } else {
            return null;
        }
    }

    @Override
    public Map<Double, Long> retrieveMapOfCaratRangeToId(List<Double> listOfCarats, Long companyId) {
        Map<Double, Long> result = new HashMap<>();
        if (!CollectionUtils.isEmpty(listOfCarats)) {
            Search search = new Search(HkCaratRangeEntity.class);

            search.addFilterEqual(Fields.HkCaratEntity.FRANCHISE, getCompnies(companyId));
            search.addFilterEqual(Fields.HkCaratEntity.STATUS, HkSystemConstantUtil.ACTIVE);
            List<HkCaratRangeEntity> caratRanges = commonDao.search(search);

            if (!CollectionUtils.isEmpty(caratRanges)) {
                for (Double caratVal : listOfCarats) {
                    for (HkCaratRangeEntity hkCaratRangeEntity : caratRanges) {
                        if (caratVal.floatValue() >= hkCaratRangeEntity.getMinValue() && caratVal.floatValue() <= hkCaratRangeEntity.getMaxValue()) {
                            result.put(caratVal, hkCaratRangeEntity.getId());
                            break;
                        }
                    }
                }
            }
        }
        return result;
    }

    @Override
    public Map< String, String> retrieveCustomfieldMasterMappingForCalc(List<Long> featureList) {
        Search search = SearchFactory.getSearch(HkFieldEntity.class);
        search.addFilterIn(HkFieldServiceImpl.HkFieldEntityField.FEATURE, featureList);
        search.addFilterEqual(IS_ARCHIVE, false);
//            search.addFetches(HkFieldServiceImpl.HkFieldEntityField.DB_FIELD_NAME, HkFieldServiceImpl.HkFieldEntityField.ID);
            List<HkFieldEntity> fieldList = commonDao.search(search);
            if (!CollectionUtils.isEmpty(fieldList)) {
                Map<String, String> fieldMap = new HashMap<>();
                for (HkFieldEntity field : fieldList) {
                    fieldMap.put(field.getDbFieldName(), field.getId().toString());
                }
                return fieldMap;

        }
        return null;
    }

    @Override
    public void saveMasterValueEntity(HkValueEntity hkValueEntity) {
        commonDao.save(hkValueEntity);
    }

    @Override
    public HkReferenceRateEntity retrieveActiveCurrencyRateByCode(String currencyCode) {
        if (StringUtils.hasText(currencyCode)) {
            Search search = new Search(HkReferenceRateEntity.class);
            search.addFilterEqual(Fields.HkCurrencyReferenceRate.IS_ACTIVE, Boolean.TRUE);
            search.addFilterEqual(Fields.HkCurrencyReferenceRate.CURRENCY, currencyCode);
            return (HkReferenceRateEntity) commonDao.searchUnique(search);
        } else {
            return null;
        }
    }

}
