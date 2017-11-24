package com.argusoft.hkg.core.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.argusoft.hkg.common.constantutil.HkSystemConstantUtil;
import com.argusoft.hkg.core.HkFoundationService;
import com.argusoft.hkg.core.config.HkCoreApplicationConfig;
import com.argusoft.hkg.model.HkAssetDocumentEntity;
import com.argusoft.hkg.model.HkAssetDocumentEntityPK;
import com.argusoft.hkg.model.HkAssetEntity;
import com.argusoft.hkg.model.HkAssetPurchaserEntity;
import com.argusoft.hkg.model.HkAssetPurchaserEntityPK;
import com.argusoft.hkg.model.HkCategoryEntity;
import com.argusoft.hkg.model.HkReferenceRateEntity;
import com.argusoft.hkg.model.HkFranchiseRequirementEntity;
import com.argusoft.hkg.model.HkGoalPermissionEntity;
import com.argusoft.hkg.model.HkGoalTemplateEntity;
import com.argusoft.hkg.model.HkMasterEntity;
import com.argusoft.hkg.model.HkSystemConfigurationEntity;
import com.argusoft.hkg.model.HkValueEntity;
import java.util.Arrays;
import java.util.HashSet;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = {HkCoreApplicationConfig.class})
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@Transactional
public class HkFoundationServiceImplTest {

    private HkMasterEntity masterEntity;

    @Resource
    private HkFoundationService foundationService;

    HkCategoryEntity categoryEntity;
    HkSystemConfigurationEntity configEntity;
    HkAssetEntity assetEntity;
    List<HkFranchiseRequirementEntity> requirementEntities;

    List<HkGoalPermissionEntity> goalPermissionEntitys = new ArrayList<>();
    List<HkGoalTemplateEntity> goalTemplates = new ArrayList<>();
    private final Long companyId = 0l;

    @Before
    public void setUp() throws Exception {
        masterEntity = new HkMasterEntity("TEST");
        masterEntity.setCreatedBy(0L);
        masterEntity.setCreatedOn(new Date());
        masterEntity.setLastModifiedOn(new Date());
        masterEntity.setDescription("description");
        masterEntity.setFranchise(companyId);
        masterEntity.setIsActive(true);
        masterEntity.setIsArchive(false);
        masterEntity.setIsSensitive(false);
        masterEntity.setMasterName("TEST");
        masterEntity.setMasterType("B");
        masterEntity.setPrecedence((short) 0);

        HkValueEntity value = new HkValueEntity(null, 1, "Value", true, 0L, true, false, 0L, new Date(), 0L, new Date(), masterEntity);

        List<HkValueEntity> hkValueEntityList = new ArrayList<>();
        hkValueEntityList.add(value);
        masterEntity.setHkValueEntityList(hkValueEntityList);

        //FOR CATEGORIES...
        categoryEntity = new HkCategoryEntity();
        categoryEntity.setCategoryPrefix(HkSystemConstantUtil.ASSET_CATEGORY_MACHINES);
        categoryEntity.setCategoryTitle("Machines");
        categoryEntity.setCategoryType("AM");
        categoryEntity.setCreatedBy(1l);
        categoryEntity.setCreatedOn(new Date());
        categoryEntity.setCurrentIndex(101);
        categoryEntity.setFranchise(companyId);
        categoryEntity.setIsActive(true);
        categoryEntity.setIsArchive(false);
        categoryEntity.setLastModifiedBy(1l);
        categoryEntity.setLastModifiedOn(new Date());
        categoryEntity.setStartIndex(101);

        foundationService.createAssetCategory(categoryEntity);

        //Asset Object Preparation
        assetEntity = new HkAssetEntity(null, HkSystemConstantUtil.ASSET_TYPE.MANAGED, "Machines", true, HkSystemConstantUtil.ASSET_STATUS.AVAILABLE, false, 1, new Date(), 1, new Date(), companyId);
        assetEntity.setSerialNumber(categoryEntity.getCurrentIndex());
        assetEntity.setCategory(categoryEntity);

        HkAssetPurchaserEntity purchaserEntity = new HkAssetPurchaserEntity();
        HkAssetPurchaserEntityPK hkAssetPurchaserEntityPK = new HkAssetPurchaserEntityPK();
        // hkAssetPurchaserEntityPK.setAsset(assetEntity.getId());
        hkAssetPurchaserEntityPK.setPurchaseByInstance(1l);
        hkAssetPurchaserEntityPK.setPurchaseByType("AB");
        purchaserEntity.setIsArchive(false);
        purchaserEntity.setHkAssetPurchaserEntityPK(hkAssetPurchaserEntityPK);
        purchaserEntity.setHkAssetEntity(assetEntity);
        Set<HkAssetPurchaserEntity> assetPurchaserEntitySet = new LinkedHashSet<>();
        assetPurchaserEntitySet.add(purchaserEntity);

        assetEntity.setHkAssetPurchaserEntitySet(assetPurchaserEntitySet);

        HkAssetDocumentEntity assetDocumentEntity = new HkAssetDocumentEntity();
        HkAssetDocumentEntityPK assetDocumentEntityPK = new HkAssetDocumentEntityPK();
        assetDocumentEntityPK.setDocumentPath("abc.jpg");
        assetDocumentEntity.setHkAssetDocumentEntityPK(assetDocumentEntityPK);
        assetDocumentEntity.setHkAssetEntity(assetEntity);
        assetDocumentEntity.setIsArchive(false);
        assetDocumentEntity.setDocumentTitle("ABC");

        HkAssetDocumentEntity assetDocumentEntity1 = new HkAssetDocumentEntity();
        HkAssetDocumentEntityPK assetDocumentEntityPK1 = new HkAssetDocumentEntityPK();
        assetDocumentEntityPK1.setDocumentPath("xyz.jpg");
        assetDocumentEntity1.setHkAssetDocumentEntityPK(assetDocumentEntityPK1);
        assetDocumentEntity1.setHkAssetEntity(assetEntity);
        assetDocumentEntity1.setIsArchive(false);
        assetDocumentEntity1.setDocumentTitle("XYZ");

        Set<HkAssetDocumentEntity> assetDocumentEntities = new LinkedHashSet<>();
        assetDocumentEntities.add(assetDocumentEntity);
        assetDocumentEntities.add(assetDocumentEntity1);

        assetEntity.setHkAssetDocumentEntitySet(assetDocumentEntities);

        HkFranchiseRequirementEntity requirementEntity = new HkFranchiseRequirementEntity();
        requirementEntity.setFranchise(1l);
        requirementEntity.setIsArchive(false);
        requirementEntity.setReqId(1l);
        requirementEntity.setReqName("Administrator");
        requirementEntity.setReqType(HkSystemConstantUtil.FRANCHISE_REQUIREMENT_TYPE.DESIGNATION);
        requirementEntity.setRequiredValue(10);
        requirementEntity.setAcquiredValue(10);

        requirementEntities = new LinkedList<>();
        requirementEntities.add(requirementEntity);

        configEntity = new HkSystemConfigurationEntity("MAX_MSG", 1L);
        configEntity.setKeyValue("10");
        configEntity.setModifiedBy(1L);
        configEntity.setModifiedOn(new Date());
        configEntity.setIsArchive(false);


        Set<HkReferenceRateEntity> currencyRateSet = new HashSet<>();
        HkReferenceRateEntity currencyRateEntity = new HkReferenceRateEntity();
        currencyRateEntity.setCode("INR 1");
        currencyRateEntity.setApplicableFrom(new Date());
        currencyRateEntity.setIsActive(true);
        currencyRateEntity.setIsArchive(false);
        currencyRateEntity.setLastModifiedBy(1l);
        currencyRateEntity.setLastModifiedOn(new Date());
        currencyRateEntity.setReferenceRate(1l);
        currencyRateEntity.setFranchise(companyId);
        currencyRateSet.add(currencyRateEntity);

        HkGoalPermissionEntity goalPermissionEntity = new HkGoalPermissionEntity();
        goalPermissionEntity.setAccessOfFeature("M");
        goalPermissionEntity.setDesignation(1L);
        goalPermissionEntity.setIsArchive(false);
        goalPermissionEntity.setReferenceType("S");
        goalPermissionEntity.setReferenceInstance(1L);
        goalPermissionEntity.setLastModifiedBy(1L);
        goalPermissionEntity.setLastModifiedOn(new Date());
        goalPermissionEntitys.add(goalPermissionEntity);

        HkGoalPermissionEntity goalPermissionEntity1 = new HkGoalPermissionEntity();
        goalPermissionEntity1.setAccessOfFeature("M");
        goalPermissionEntity1.setDesignation(1L);
        goalPermissionEntity1.setIsArchive(false);
        goalPermissionEntity1.setReferenceType("S");
        goalPermissionEntity1.setReferenceInstance(2L);
        goalPermissionEntity1.setLastModifiedBy(1L);
        goalPermissionEntity1.setLastModifiedOn(new Date());
        goalPermissionEntitys.add(goalPermissionEntity1);

        HkGoalTemplateEntity goalTemplateEntity = new HkGoalTemplateEntity();
        goalTemplateEntity.setTemplateName("T1");
        goalTemplateEntity.setPeriod(1);
        goalTemplateEntity.setTemplateType("ABC");
        goalTemplateEntity.setStatus("A");
        goalTemplateEntity.setCreatedOn(new Date());
        goalTemplateEntity.setCreatedBy(1L);
        goalTemplateEntity.setForDepartment(1L);
        goalTemplateEntity.setForDesignation(1L);
        goalTemplateEntity.setForService(1L);
        goalTemplateEntity.setFranchise(1L);
        goalTemplateEntity.setLastModifiedBy(1L);
        goalTemplateEntity.setLastModifiedOn(new Date());
        goalTemplates.add(goalTemplateEntity);

        HkGoalTemplateEntity goalTemplateEntity1 = new HkGoalTemplateEntity();
        goalTemplateEntity1.setTemplateName("T1");
        goalTemplateEntity1.setPeriod(1);
        goalTemplateEntity1.setTemplateType("ABC");
        goalTemplateEntity1.setStatus("A");
        goalTemplateEntity1.setCreatedOn(new Date());
        goalTemplateEntity1.setCreatedBy(1L);
        goalTemplateEntity1.setForDepartment(1L);
        goalTemplateEntity1.setForDesignation(1L);
        goalTemplateEntity1.setForService(1L);
        goalTemplateEntity1.setFranchise(1L);
        goalTemplateEntity1.setLastModifiedBy(1L);
        goalTemplateEntity1.setLastModifiedOn(new Date());
        goalTemplates.add(goalTemplateEntity1);

        HkGoalTemplateEntity goalTemplateEntity2 = new HkGoalTemplateEntity();
        goalTemplateEntity2.setTemplateName("T1");
        goalTemplateEntity2.setPeriod(1);
        goalTemplateEntity2.setTemplateType("ABC");
        goalTemplateEntity2.setStatus("P");
        goalTemplateEntity2.setCreatedOn(new Date());
        goalTemplateEntity2.setCreatedBy(1L);
        goalTemplateEntity2.setForDepartment(1L);
        goalTemplateEntity2.setForDesignation(1L);
        goalTemplateEntity2.setForService(1L);
        goalTemplateEntity2.setFranchise(1L);
        goalTemplateEntity2.setLastModifiedBy(1L);
        goalTemplateEntity2.setLastModifiedOn(new Date());
        goalTemplates.add(goalTemplateEntity2);
    }

    @Test
    public void testCreateMaster() {
        assertTrue(foundationService.createMaster(masterEntity));
    }

    @Test
    public void testRetrieveMasters() {
        foundationService.createMaster(masterEntity);
        assertTrue(foundationService.retrieveMasters(0, (short) 0, null, Boolean.FALSE).size() > 0);
    }

    @Test
    public void testCopyMasterValuesFromFranchise() {
        foundationService.createMaster(masterEntity);
        assertTrue(foundationService.retrieveMasters(companyId, (short) 0, null, Boolean.FALSE).size() > 0);

        Long newCompanyId = 999l;
        foundationService.copyMasterValuesFromFranchise(companyId, newCompanyId);
        System.out.println("Values " + foundationService.searchMasterValues(newCompanyId, masterEntity.getCode(), "").size());
        assertTrue(foundationService.searchMasterValues(newCompanyId, masterEntity.getCode(), "").size() > 0);
    }

    @Test
    public void testRetrieveMasterMap() {
        foundationService.createMaster(masterEntity);
        List<String> masterCodes = new LinkedList<>();
        masterCodes.add(masterEntity.getCode());

        Map<String, HkMasterEntity> masterDataMap = foundationService.retrieveMaster(masterCodes, (short) 0);
        assertTrue(masterDataMap.size() > 0);
    }

    @Test
    public void testDoesMasterNameExist() {
        foundationService.createMaster(masterEntity);
        assertTrue(foundationService.doesMasterNameExist(0, "TEST"));
        assertFalse(foundationService.doesMasterNameExist(0, "NOTHING"));
    }

    @Test
    public void testRetrieveMaster() {
        foundationService.createMaster(masterEntity);

        HkMasterEntity masterEntityTemp = foundationService.retrieveMaster("TEST", (short) 0, 0l);
        assertNotNull(masterEntityTemp);
        assertNotNull(masterEntityTemp.getHkValueEntityList());
        assertTrue(masterEntityTemp.getHkValueEntityList().size() == 1);
        for (HkValueEntity value : masterEntityTemp.getHkValueEntityList()) {
//            System.out.println(value.getValueName());
            assertTrue(value.getValueName(), value != null);
        }

    }

    @Test
    public void testUpdateMaster() {
        foundationService.createMaster(masterEntity);
        HkValueEntity value2 = new HkValueEntity(null, 1, "Value2", true, 0L, true, false, 0L, new Date(), 0L, new Date(), masterEntity);
        HkMasterEntity masterEntityTemp = foundationService.retrieveMaster("TEST", (short) 0);
        masterEntityTemp.getHkValueEntityList().add(value2);
        foundationService.updateMaster(masterEntityTemp, null, 0l, null);
        HkMasterEntity masterEntityTemp2 = foundationService.retrieveMaster("TEST", (short) 0, 0l);
        assertNotNull(masterEntityTemp2);
        assertNotNull(masterEntityTemp2.getHkValueEntityList());
        assertTrue(masterEntityTemp2.getHkValueEntityList().size() == 2);

    }

    @Test
    public void testRemoveMaster() {

//		foundationService.createMaster(masterEntity);
//		foundationService.removeMaster(masterEntity.getCode());
//		assertTrue(foundationService.retrieveMasters(0).size() == 0);
    }

    @Test
    public void testSearchMaster() {
        foundationService.createMaster(masterEntity);
        assertTrue(foundationService.searchMaster(masterEntity.getFranchise(), masterEntity.getMasterName(), masterEntity.getPrecedence()).size() > 0);

    }

    /**
     * Test of retrieveAssets method, of class HkOperationsServiceImpl.
     */
    @Test
    public void testRetrieveAssets() {
//        System.out.println("retrieveAssets");
        long category = categoryEntity.getId();
        long companyId = 1l;

        foundationService.createAsset(assetEntity);

        List<HkAssetEntity> result = foundationService.retrieveAssets(category, companyId, false);
        assertNotNull(result);
    }

    /**
     * Test of retrieveAsset method, of class HkOperationsServiceImpl.
     */
    @Test
    public void testRetrieveAsset() {
//        System.out.println("retrieveAsset");

        foundationService.createAsset(assetEntity);

        long assetId = assetEntity.getId();

        HkAssetEntity expResult = assetEntity;
        HkAssetEntity result = foundationService.retrieveAsset(assetId);
        assertEquals(expResult, result);
    }

    /**
     * Test of createAsset method, of class HkOperationsServiceImpl.
     */
    @Test
    public void testCreateAsset() {
//        System.out.println("createAsset");

        boolean expResult = true;
        boolean result = foundationService.createAsset(assetEntity);
        assertEquals(expResult, result);
    }

    /**
     * Test of updateAsset method, of class HkOperationsServiceImpl.
     */
    @Test
    public void testUpdateAsset() {
//        System.out.println("updateAsset");

        boolean expResult = true;
        boolean result = foundationService.createAsset(assetEntity);
        assertEquals(expResult, result);

        assetEntity.setAssetName("Pencils");
        expResult = true;
        result = foundationService.updateAsset(assetEntity, null);
        assertEquals(expResult, result);
    }

    /**
     * Test of retrieveAssetCategory method, of class HkOperationsServiceImpl.
     */
    @Test
    public void testRetrieveAssetCategory() {
//        System.out.println("retrieveAssetCategory");
        long categoryId = categoryEntity.getId();
        HkCategoryEntity expResult = categoryEntity;
        HkCategoryEntity result = foundationService.retrieveAssetCategory(categoryId);
        assertEquals(expResult, result);
    }

    /**
     * Test of doesAssetCategoryNameExist method, of class
     * HkOperationsServiceImpl.
     */
    @Test
    public void testDoesAssetCategoryNameExist() {
//        System.out.println("doesAssetCategoryNameExist");
        String categoryName = "Machines";
        boolean expResult = true;
        boolean result = foundationService.doesAssetCategoryNameExist(categoryName, companyId, null);
        assertEquals(expResult, result);
    }

    /**
     * Test of doesAssetCategoryPrefixExist method, of class
     * HkOperationsServiceImpl.
     */
    @Test
    public void testDoesAssetCategoryPrefixExist() {
//        System.out.println("doesAssetCategoryPrefixExist");
        String categoryPrefix = HkSystemConstantUtil.ASSET_CATEGORY_MACHINES;
        boolean expResult = true;
        boolean result = foundationService.doesAssetCategoryPrefixExist(categoryPrefix, companyId, null);
        assertEquals(expResult, result);
    }

    /**
     * Test of retrieveAllAssetCategories method, of class
     * HkOperationsServiceImpl.
     */
    @Test
    public void testRetrieveAllAssetCategories() {
//        System.out.println("retrieveAllAssetCategories");
        List<HkCategoryEntity> result = foundationService.retrieveAllAssetCategories(companyId);
        assertNotNull(result);
    }

    /**
     * Test of retrieveAllRootAssetCategories method, of class
     * HkOperationsServiceImpl.
     */
    @Test
    public void testRetrieveAllRootAssetCategories() {
//        System.out.println("retrieveAllRootAssetCategories");
        List<HkCategoryEntity> result = foundationService.retrieveAllRootAssetCategories(companyId);
        assertNotNull(result);
    }

    /**
     * Test of updateAssetCategory method, of class HkOperationsServiceImpl.
     */
    @Test
    public void testUpdateAssetCategory() {
//        System.out.println("updateAssetCategory");
        HkCategoryEntity category = categoryEntity;
        category.setCategoryTitle("Pencils");
        boolean expResult = true;
        boolean result = foundationService.updateAssetCategory(category);
        assertEquals(expResult, result);
    }

    /**
     * Test of removeAssetCategory method, of class HkOperationsServiceImpl.
     */
    @Test
    public void testRemoveAssetCategory() {
//        System.out.println("removeAssetCategory");
        long categoryId = categoryEntity.getId();
        boolean expResult = true;
        boolean result = foundationService.removeAssetCategory(categoryId);
        assertEquals(expResult, result);
    }

    /**
     * Test of createAssetCategory method, of class HkOperationsServiceImpl.
     */
    @Test
    public void testCreateAssetCategory() {
//        System.out.println("createAssetCategory");
        HkCategoryEntity category = new HkCategoryEntity();
        category.setCategoryPrefix("PE");
        category.setCategoryTitle("Pencils");
        category.setCategoryType("AM");
        category.setCreatedBy(1l);
        category.setCreatedOn(new Date());
        category.setCurrentIndex(1);
        category.setFranchise(1l);
        category.setIsActive(true);
        category.setIsArchive(false);
        category.setLastModifiedBy(1l);
        category.setLastModifiedOn(new Date());
        category.setStartIndex(1);;

        category.setCategoryTitle("Pendils");
        category.setId(null);

        boolean expResult = true;
        boolean result = foundationService.createAssetCategory(category);
        assertEquals(expResult, result);
    }

    /**
     * Test of doesSerialNumberExistForCategory method, of class
     * HkOperationsServiceImpl.
     */
    @Test
    public void testDoesSerialNumberAlreadyExistForCategory() {
//        System.out.println("updateAsset");

        boolean expResult = true;
        boolean result = foundationService.createAsset(assetEntity);
        assertEquals(expResult, result);

        Integer serialNumber = 101;
        Long category = categoryEntity.getId();
        Long skippedAsset = assetEntity.getId();

        expResult = true;
        result = foundationService.doesSerialNumberAlreadyExistForCategory(serialNumber, category, null, companyId);
        assertEquals(expResult, result);

        expResult = false;
        result = foundationService.doesSerialNumberAlreadyExistForCategory(serialNumber, category, skippedAsset, companyId);
        assertEquals(expResult, result);
    }

    /**
     * Test of retrieveAssetCategorywiseCount method, of class
     * HkOperationsServiceImpl.
     */
    @Test
    public void testRetrieveAssetCategorywiseCount() {
//        System.out.println("retrieveAssetCategorywiseCount");

        boolean expResult = true;
        boolean result = foundationService.createAsset(assetEntity);
        assertEquals(expResult, result);

        Map<Long, Integer> resultMap = foundationService.retrieveAssetCategorywiseCount(companyId);
        assertNotNull(resultMap);
    }

    /**
     * Test of retrieveSimilarNameSuggestion method, of class
     * HkOperationsServiceImpl.
     */
    @Test
    public void testRetrieveSimilarNameSuggestion() {
//        System.out.println("retrieveSimilarNameSuggestion");

        boolean expResult = true;
        boolean result = foundationService.createAsset(assetEntity);
        assertEquals(expResult, result);

        String nameSuggestion = "Machines";
        List<String> assetNames = foundationService.retrieveSimilarNameSuggestion(nameSuggestion, companyId);
        assertEquals(!CollectionUtils.isEmpty(assetNames), expResult);
    }

    /**
     * Test of retrieveAssetBasedonSearchCriteria method, of class
     * HkOperationsServiceImpl.
     */
    @Test
    public void testAssetBasedonSearchCriteria() {
//        System.out.println("retrieveAssetBasedonSearchCriteria");

        boolean expResult = true;
        boolean result = foundationService.createAsset(assetEntity);
        assertEquals(expResult, result);

        String nameSuggestion = "Hello @C Mac";
        List<HkAssetEntity> assetEntities = foundationService.retrieveAssetBasedonSearchCriteria(nameSuggestion, companyId);
        assertEquals(!CollectionUtils.isEmpty(assetEntities), expResult);
    }

    /**
     * Test of retrieveFranchiseRequirements method, of class
     * HkOperationsServiceImpl.
     */
    @Test
    public void testRetrieveFranchiseRequirements() {
//        System.out.println("retrieveFranchiseRequirements");

        boolean expResult = true;
        boolean result = foundationService.createFranchiseRequirements(requirementEntities);
        assertEquals(expResult, result);

        List<HkFranchiseRequirementEntity> retrieveRequirements = foundationService.retrieveFranchiseRequirements(1l);
        assertNotNull(retrieveRequirements);
    }

    /**
     * Test of createFranchiseRequirements method, of class
     * HkOperationsServiceImpl.
     */
    @Test
    public void testCreateFranchiseRequirements() {
//        System.out.println("createFranchiseRequirements");
        boolean expResult = true;
        boolean result = foundationService.createFranchiseRequirements(requirementEntities);
        assertEquals(expResult, result);
    }

    /**
     * Test of updateFranchiseRequirements method, of class
     * HkOperationsServiceImpl.
     */
    @Test
    public void testUpdateFranchiseRequirements() {
//        System.out.println("updateFranchiseRequirements");
        boolean expResult = true;
        boolean result = foundationService.createFranchiseRequirements(requirementEntities);
        assertEquals(expResult, result);

        for (HkFranchiseRequirementEntity requirementEntity : requirementEntities) {
            requirementEntity.setRequiredValue(15);
        }

        result = foundationService.updateFranchiseRequirements(requirementEntities);
        assertEquals(expResult, result);
    }

    /**
     * Test of removeFranchiseRequirements method, of class
     * HkOperationsServiceImpl.
     */
    @Test
    public void testRemoveFranchiseRequirements() {
//        System.out.println("removeFranchiseRequirements");
        boolean expResult = true;
        boolean result = foundationService.createFranchiseRequirements(requirementEntities);
        assertEquals(expResult, result);

        result = foundationService.removeFranchiseRequirements(requirementEntities);
        assertEquals(expResult, result);
    }

    /**
     * Test of removeAllFranchiseRequirements method, of class
     * HkOperationsServiceImpl.
     */
    @Test
    public void testRemoveAllFranchiseRequirements() {
//        System.out.println("removeAllFranchiseRequirements");
        boolean expResult = true;
        boolean result = foundationService.createFranchiseRequirements(requirementEntities);
        assertEquals(expResult, result);

        result = foundationService.removeAllFranchiseRequirements(1l);
        assertEquals(expResult, result);
    }

    /**
     * Test of saveSystemConfigurations method, of class
     * HkFoundationServiceImpl.
     */
    @Test
    public void testSaveSystemConfigurations() {
//        System.out.println("saveSystemConfigurations");
        foundationService.saveSystemConfigurations(Arrays.asList(configEntity));

        HkSystemConfigurationEntity resultConfig = foundationService.retrieveSystemConfigurationByKey(configEntity.getHkSystemConfigurationEntityPK().getSystemKey(), configEntity.getHkSystemConfigurationEntityPK().getFranchise());
        assertEquals(configEntity, resultConfig);
    }

    /**
     * Test of retrieveSystemConfigurationByFranchise method, of class
     * HkFoundationServiceImpl.
     */
    @Test
    public void testRetrieveSystemConfigurationByFranchise() {
//        System.out.println("retrieveSystemConfigurationByFranchise");
        foundationService.saveSystemConfigurations(Arrays.asList(configEntity));

        List<HkSystemConfigurationEntity> result = foundationService.retrieveSystemConfigurationByFranchise(configEntity.getHkSystemConfigurationEntityPK().getFranchise());
        assertTrue(result.size() > 0);
//        System.out.println("result is " + result);
    }

    /**
     * Test of retrieveSystemConfigurationByKey method, of class
     * HkFoundationServiceImpl.
     */
    @Test
    public void testRetrieveSystemConfigurationByKey() {
//        System.out.println("retrieveSystemConfigurationByKey");
        foundationService.saveSystemConfigurations(Arrays.asList(configEntity));

        HkSystemConfigurationEntity resultConfig = foundationService.retrieveSystemConfigurationByKey(configEntity.getHkSystemConfigurationEntityPK().getSystemKey(), configEntity.getHkSystemConfigurationEntityPK().getFranchise());
        assertEquals(configEntity, resultConfig);
    }

//    @Test
    public void testCopyMachinesFromFranchise() {
//        System.out.println("copyMachinesFromFranchise");

        this.testCreateAsset();

        Long newCompanyId = 200l;
        foundationService.copyMachinesFromFranchise(companyId, newCompanyId);

        HkCategoryEntity machineCategory = foundationService.retrieveAssetCategoryByPrefix(HkSystemConstantUtil.ASSET_CATEGORY_MACHINES, newCompanyId);

        assertNotNull(machineCategory);

        List<HkAssetEntity> assets = foundationService.retrieveAssets(machineCategory.getId(), newCompanyId, true);
        assertNotNull(assets);
    }

    @Test
    public void testRetrieveCurrentRateAndCurrencyHistory() {
        for (int i = 0; i < 20; i++) {
       
            HkReferenceRateEntity currencyRateEntity = new HkReferenceRateEntity();
            currencyRateEntity.setCode("ZZ##" + i);
            currencyRateEntity.setApplicableFrom(new Date());
            currencyRateEntity.setIsActive(true);
            currencyRateEntity.setIsArchive(false);
            currencyRateEntity.setLastModifiedBy(1l);
            currencyRateEntity.setLastModifiedOn(new Date());
            currencyRateEntity.setReferenceRate(1l);
            currencyRateEntity.setFranchise(companyId);
            foundationService.addReferenceRate(currencyRateEntity);
        }

       
        Map<String, List<HkReferenceRateEntity>> refeRateEntitys = foundationService.retrieveCurrentRateAndCurrencyHistory(15, companyId);
//        System.out.println("Map size ******************** " + refeRateEntitys.size());
        assertNotNull("retrieve all currencies", refeRateEntitys);
    }

    @Test
    public void testRetrieveAllCurrencyRates() {
        this.testAddReferenceRate();
        List<HkReferenceRateEntity> currencyRateEntitys = foundationService.retrieveAllCurrencyRates(true, companyId);
        assertNotNull("retrieve all currency rates", currencyRateEntitys);
    }


    @Test
    public void testAddReferenceRate() {
        HkReferenceRateEntity currencyRateEntity = new HkReferenceRateEntity(null, new Date(), true, false, 1l, null, companyId);
        boolean result = foundationService.addReferenceRate(currencyRateEntity);
        assertTrue(result);
    }

    @Test
    public void testUpdateReferenceRate() {
        HkReferenceRateEntity currencyRateEntity = new HkReferenceRateEntity(null, new Date(), true, false, 1l, null, companyId);
        boolean result = foundationService.addReferenceRate(currencyRateEntity);
        assertTrue(result);
        result = foundationService.updateReferenceRate(currencyRateEntity.getId(), new Date(), true, 1l);
        assertTrue(result);

    }

    /**
     * Test of addGoalPermissions method, of class HkFoundationServiceImpl.
     */
    @Test
    public void testAddGoalPermissions() {
        System.out.println("~~~~~~~addGoalPermissions~~~~~~~~~");

        foundationService.addGoalPermissions(goalPermissionEntitys);
    }

    /**
     * Test of updateGoalPermissions method, of class HkFoundationServiceImpl.
     */
    @Test
    public void testUpdateGoalPermissions() {
        System.out.println("updateGoalPermissions");
        List<HkGoalPermissionEntity> goalPermissionEntitys1 = new ArrayList<>();

        foundationService.addGoalPermissions(goalPermissionEntitys);

        HkGoalPermissionEntity goalPermissionEntity = new HkGoalPermissionEntity();
        goalPermissionEntity.setId(goalPermissionEntitys.get(0).getId());
        goalPermissionEntity.setAccessOfFeature("M");
        goalPermissionEntity.setDesignation(1L);
        goalPermissionEntity.setIsArchive(false);
        goalPermissionEntity.setReferenceType("S");
        goalPermissionEntity.setReferenceInstance(1L);
        goalPermissionEntity.setLastModifiedBy(1L);
        goalPermissionEntity.setLastModifiedOn(new Date());

        goalPermissionEntitys1.add(goalPermissionEntity);
        foundationService.updateGoalPermissions(goalPermissionEntitys1, null);
        List<HkGoalPermissionEntity> activePermissions = foundationService.retrieveActiveGoalPermissionByDesignation(1L);

        assertNotNull(activePermissions);
    }

    /**
     * Test of retrieveGoalPermissionById method, of class
     * HkFoundationServiceImpl.
     */
    @Test
    public void testRetrieveGoalPermissionById() {
        System.out.println("retrieveGoalPermissionById");

        foundationService.addGoalPermissions(goalPermissionEntitys);
        HkGoalPermissionEntity retrieveGoalPermissionById = foundationService.retrieveGoalPermissionById(goalPermissionEntitys.get(0).getId());

        assertEquals(retrieveGoalPermissionById.getId(), goalPermissionEntitys.get(0).getId());
    }

    /**
     * Test of retrieveGoalPermissions method, of class HkFoundationServiceImpl.
     */
    @Test
    public void testRetrieveGoalPermissions() {
        System.out.println("retrieveGoalPermissions");

        foundationService.addGoalPermissions(goalPermissionEntitys);

        List<HkGoalPermissionEntity> retrieveGoalPermissions = foundationService.retrieveGoalPermissions("M", 1L);
        System.out.println("retrieveGoalPermissions::" + retrieveGoalPermissions.size());
        assertEquals(retrieveGoalPermissions.size(), 2);
    }

    /**
     * Test of deleteGoalPermissions method, of class HkFoundationServiceImpl.
     */
    @Test
    public void testDeleteGoalPermissions() {
        System.out.println("deleteGoalPermissions");
        foundationService.addGoalPermissions(goalPermissionEntitys);
        List<Long> list = new ArrayList<>();

        for (HkGoalPermissionEntity hkGoalPermissionEntity : goalPermissionEntitys) {
            list.add(hkGoalPermissionEntity.getId());
        }
        foundationService.deleteGoalPermissions(list);
        HkGoalPermissionEntity retrieveGoalPermissionById = foundationService.retrieveGoalPermissionById(goalPermissionEntitys.get(0).getId());

        assertEquals(retrieveGoalPermissionById.getIsArchive(), true);
    }

    /**
     * Test of retrieveActiveGoalPermissionByDesignation method, of class
     * HkFoundationServiceImpl.
     */
    @Test
    public void testRetrieveActiveGoalPermissionByDesignation() {
        System.out.println("retrieveActiveGoalPermissionByDesignation");
        foundationService.addGoalPermissions(goalPermissionEntitys);
        List<HkGoalPermissionEntity> retrieveActiveGoalPermissionByDesignation = foundationService.retrieveActiveGoalPermissionByDesignation(1L);

        assertEquals(retrieveActiveGoalPermissionByDesignation.size(), 2L);
    }

    /**
     * Test of saveGoalTemplates method, of class HkFoundationServiceImpl.
     */
    @Test
    public void testSaveGoalTemplates() {
        System.out.println("saveGoalTemplates");
        foundationService.saveGoalTemplates(goalTemplates);
    }

    /**
     * Test of retrieveActiveGoalTemplatesByDesignation method, of class
     * HkFoundationServiceImpl.
     */
    @Test
    public void testRetrieveActiveGoalTemplatesByDesignation() {
        System.out.println("retrieveActiveGoalTemplatesByDesignation");
        foundationService.saveGoalTemplates(goalTemplates);
        List<HkGoalTemplateEntity> retrieveActiveGoalTemplatesByDesignation = foundationService.retrieveActiveGoalTemplatesByDesignation(1L, null);

        assertEquals(retrieveActiveGoalTemplatesByDesignation.size(), 2L);
    }

    /**
     * Test of retrieveActiveGoalTemplatesByService method, of class
     * HkFoundationServiceImpl.
     */
    @Test
    public void testRetrieveActiveGoalTemplatesByService() {
        System.out.println("retrieveActiveGoalTemplatesByService");
        foundationService.saveGoalTemplates(goalTemplates);
        List<HkGoalTemplateEntity> retrieveActiveGoalTemplatesByDesignation = foundationService.retrieveActiveGoalTemplatesByService(1L, null);

        assertEquals(retrieveActiveGoalTemplatesByDesignation.size(), 2L);
    }

    /**
     * Test of retrieveActiveGoalTemplatesByDepartment method, of class
     * HkFoundationServiceImpl.
     */
    @Test
    public void testRetrieveActiveGoalTemplatesByDepartment() {
        foundationService.saveGoalTemplates(goalTemplates);
        System.out.println("retrieveActiveGoalTemplatesByDepartment");
        List<HkGoalTemplateEntity> retrieveActiveGoalTemplatesByDesignation = foundationService.retrieveActiveGoalTemplatesByDepartment(1L, null);

        assertEquals(retrieveActiveGoalTemplatesByDesignation.size(), 2L);
    }

    /**
     * Test of retrievePendingGoalTemplates method, of class
     * HkFoundationServiceImpl.
     */
    @Test
    public void testRetrievePendingGoalTemplates() {
        System.out.println("retrievePendingGoalTemplates");
        foundationService.saveGoalTemplates(goalTemplates);
        List<HkGoalTemplateEntity> retrieveActiveGoalTemplatesByDesignation = foundationService.retrievePendingGoalTemplates(1L);

        assertEquals(retrieveActiveGoalTemplatesByDesignation.size(), 1L);
    }

    /**
     * Test of retrieveGoalTemplateById method, of class
     * HkFoundationServiceImpl.
     */
    @Test
    public void testRetrieveGoalTemplateById() {
        System.out.println("retrieveGoalTemplateById");
        foundationService.saveGoalTemplates(goalTemplates);
        HkGoalTemplateEntity retrieveGoalTemplateById = foundationService.retrieveGoalTemplateById(goalTemplates.get(0).getId());

        assertEquals(retrieveGoalTemplateById.getId(), goalTemplates.get(0).getId());
    }
}
