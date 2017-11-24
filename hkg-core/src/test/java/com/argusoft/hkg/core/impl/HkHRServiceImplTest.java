/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.core.impl;

import com.argusoft.hkg.core.config.HkCoreApplicationConfig;
import com.argusoft.hkg.common.constantutil.HkSystemConstantUtil;
import com.argusoft.hkg.core.HKCategoryService;
import com.argusoft.hkg.core.HkFoundationService;
import com.argusoft.hkg.core.HkHRService;
import com.argusoft.hkg.model.HkAssetDocumentEntity;
import com.argusoft.hkg.model.HkAssetDocumentEntityPK;
import com.argusoft.hkg.model.HkAssetEntity;
import com.argusoft.hkg.model.HkAssetIssueEntity;
import com.argusoft.hkg.model.HkAssetPurchaserEntity;
import com.argusoft.hkg.model.HkAssetPurchaserEntityPK;
import com.argusoft.hkg.model.HkCategoryEntity;
import com.argusoft.hkg.model.HkEventEntity;
import com.argusoft.hkg.model.HkEventRecipientEntity;
import com.argusoft.hkg.model.HkEventRecipientEntityPK;
import com.argusoft.hkg.model.HkEventRegistrationEntity;
import com.argusoft.hkg.model.HkEventRegistrationEntityPK;
import com.argusoft.hkg.model.HkEventRegistrationFieldEntity;
import com.argusoft.hkg.model.HkHolidayEntity;
import com.argusoft.hkg.model.HkShiftDepartmentEntity;
import com.argusoft.hkg.model.HkShiftDepartmentEntityPK;
import com.argusoft.hkg.model.HkShiftDtlEntity;
import com.argusoft.hkg.model.HkShiftEntity;
import com.argusoft.hkg.model.HkShiftRuleEntity;
import com.argusoft.hkg.model.HkWorkflowApproverEntity;
import com.argusoft.hkg.model.HkWorkflowEntity;
import com.argusoft.hkg.core.util.CategoryType;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import static org.junit.Assert.*;
import org.junit.Test;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

/**
 * Test class to test the methods of HkHRService.
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = {HkCoreApplicationConfig.class})
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@Transactional
public class HkHRServiceImplTest {

    /**
     * Services
     */
    @Resource
    private HkHRService hkHRService;
    @Resource
    private HKCategoryService hkCategoryService;

    @Resource
    HkFoundationService hkOperationsService;

    /**
     * Entities
     */
    private HkHolidayEntity holidayEntity;
    private HkWorkflowEntity workflowEntity;
    private Set<HkWorkflowApproverEntity> workflowApproverSet;
    private HkEventEntity eventEntity;
    private HkCategoryEntity eventCategoryEntity;
    private HkEventRecipientEntity eventRecipientEntity;
    private Set<HkEventRecipientEntity> eventRecipientSet = new HashSet<>();
    private Set<HkEventRegistrationEntity> eventRegSet = new HashSet<>();
    private Set<HkEventRegistrationFieldEntity> eventRegFieldSet = new HashSet<>();
    private HkEventRegistrationEntity eventRegistrationEntity;
    private HkShiftEntity hkShiftEntity;
    private HkShiftEntity hkTempShiftEntity;
    private HkShiftRuleEntity shiftRuleEntity;
    private HkShiftDtlEntity dtlEntity;

    public HkHRServiceImplTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        holidayEntity = new HkHolidayEntity();
        holidayEntity.setHolidayTitle("Kunchipudam");
        holidayEntity.setStartDt(new Date(1999 - 1900, 10, 10));
        holidayEntity.setEndDt(new Date(1999 - 1900, 10, 11));
        holidayEntity.setFranchise(0L);
        holidayEntity.setCreatedBy(1L);
        holidayEntity.setCreatedOn(new Date());
        holidayEntity.setLastModifiedBy(1L);
        holidayEntity.setLastModifiedOn(new Date());
        holidayEntity.setIsActive(true);
        holidayEntity.setIsArchive(false);
//
        workflowEntity = new HkWorkflowEntity(null, false, 1L, new Date(), 1L, new Date(), 0L);
        workflowEntity.setLastLevel(1);
        workflowEntity.setDepartment(1L);

        workflowApproverSet = new HashSet<>();
        HkWorkflowApproverEntity approver = new HkWorkflowApproverEntity(0L, "D", 2L);
        approver.setLevel(1);
        approver.setIsArchive(false);
        workflowApproverSet.add(approver);
        workflowEntity.setHkWorkflowApproverEntitySet(workflowApproverSet);

        eventCategoryEntity = new HkCategoryEntity();
        eventCategoryEntity.setCategoryTitle("Event Category");
        eventCategoryEntity.setCategoryType("EVENT");
        eventCategoryEntity.setCreatedBy(1L);
        eventCategoryEntity.setCreatedOn(new java.sql.Date(Calendar.getInstance().getTimeInMillis()));
        eventCategoryEntity.setFranchise(0L);
        eventCategoryEntity.setIsActive(true);
        eventCategoryEntity.setIsArchive(false);
        eventCategoryEntity.setLastModifiedBy(1L);
        eventCategoryEntity.setLastModifiedOn(new java.sql.Date(Calendar.getInstance().getTimeInMillis()));
        eventCategoryEntity.setHaveDiamondProcessingMch(Boolean.FALSE);

        eventEntity = new HkEventEntity();
        eventEntity.setAddress("A/66, GIDC");
        eventEntity.setBannerImageName("Argusoft Banner");
//        eventEntity.setCategory(eventCategoryEntity);
        eventEntity.setContentColor("Red");
        eventEntity.setCreatedBy(1L);
        eventEntity.setCreatedOn(new Date());
        eventEntity.setDescription("Garba function");
        eventEntity.setEndTime(new Date(2014 - 1900, 11, 11, 11, 11, 11));
        eventEntity.setEventTitle("Garba at Argus");
        eventEntity.setFolderName("Garba");
        eventEntity.setFranchise(0L);
        eventEntity.setFrmDt(new Date(2014 - 1900, 11, 11));
        eventEntity.setInvitationTemplateName("Garba template");
        eventEntity.setIsArchive(false);
        eventEntity.setLabelColor("Blue");
        eventEntity.setLastModifiedBy(1L);
        eventEntity.setLastModifiedOn(new Date());
        eventEntity.setPublishedOn(new Date(2014 - 1900, 11, 5));
        eventEntity.setRegistrationFormName("Garba form");
        eventEntity.setRegistrationLastDt(new Date(2014 - 1900, 11, 10));
        eventEntity.setRegistrationType(HkSystemConstantUtil.EventRegistrationStatus.ONLINE);
        eventEntity.setStrtTime(new Date(2014 - 1900, 11, 11, 7, 7, 7));
        eventEntity.setToDt(new Date(2014 - 1900, 11, 11));

        eventRecipientSet = new HashSet<>();
        HkEventRecipientEntityPK hkEventRecipientEntityPK = new HkEventRecipientEntityPK();
        hkEventRecipientEntityPK.setReferenceInstance(1L);
        hkEventRecipientEntityPK.setReferenceType("E");
        eventRecipientEntity = new HkEventRecipientEntity();
        eventRecipientEntity.setHkEventRecipientEntityPK(hkEventRecipientEntityPK);
        eventRecipientEntity.setFranchise(0L);
        eventRecipientEntity.setIsArchive(Boolean.FALSE);
        eventRecipientEntity.setHkEventEntity(eventEntity);
        eventRecipientSet.add(eventRecipientEntity);
        eventEntity.setHkEventRecipientEntitySet(eventRecipientSet);
//
        eventRegFieldSet = new HashSet<>();
        HkEventRegistrationFieldEntity eventRegField = new HkEventRegistrationFieldEntity(null, "First Name", "alphabets", false, 1L, new Date());
        eventRegField.setEvent(eventEntity);
        eventRegFieldSet.add(eventRegField);
        HkEventRegistrationFieldEntity eventRegField1 = new HkEventRegistrationFieldEntity(null, "Last Name", "alphanum", false, 1L, new Date());
        eventRegField1.setEvent(eventEntity);
        eventRegFieldSet.add(eventRegField1);
        eventEntity.setHkEventRegistrationFieldEntitySet(eventRegFieldSet);
//
        //Shifts
        hkShiftEntity = new HkShiftEntity();
        hkShiftEntity.setCreatedBy(1L);
        hkShiftEntity.setCreatedOn(new Date());
        hkShiftEntity.setFranchise(0);
        hkShiftEntity.setFrmDt(new Date());
        hkShiftEntity.setHasRule(false);
        hkShiftEntity.setIsArchive(false);
        hkShiftEntity.setIsDefault(true);
        hkShiftEntity.setShiftTitle("TEST SHIFT");
        hkShiftEntity.setStatus(HkSystemConstantUtil.ACTIVE);
        hkShiftEntity.setToDt(new Date(new Date().getTime() + 1000000000L));
        hkShiftEntity.setLastModifiedBy(0);
        hkShiftEntity.setLastModifiedOn(new Date());
        hkShiftEntity.setWeekDays("1,2,3");
        Set<HkShiftDepartmentEntity> hkShiftDepartmentEntitySet = new HashSet<>();
        HkShiftDepartmentEntity shiftDepartment = new HkShiftDepartmentEntity();
        shiftDepartment.setHkShiftEntity(hkShiftEntity);
        shiftDepartment.setIsArchive(false);
        shiftDepartment.setLastModifiedOn(new Date());
        shiftDepartment.setHkShiftDepartmentEntityPK(new HkShiftDepartmentEntityPK(0, 1l));
        hkShiftDepartmentEntitySet.add(shiftDepartment);
        hkShiftEntity.setHkShiftDepartmentEntitySet(hkShiftDepartmentEntitySet);

        hkTempShiftEntity = new HkShiftEntity();
        hkTempShiftEntity.setCreatedBy(1L);
        hkTempShiftEntity.setCreatedOn(new Date());
        hkTempShiftEntity.setFranchise(0);
        hkTempShiftEntity.setFrmDt(new Date());
        hkTempShiftEntity.setHasRule(false);
        hkTempShiftEntity.setIsArchive(false);
        hkTempShiftEntity.setIsDefault(true);
        hkTempShiftEntity.setShiftTitle("TEST SHIFT TEMP");
        hkTempShiftEntity.setStatus(HkSystemConstantUtil.ACTIVE);
        hkTempShiftEntity.setToDt(new Date(new Date().getTime() + 500000000L));
        hkTempShiftEntity.setLastModifiedBy(0);
        hkTempShiftEntity.setLastModifiedOn(new Date());
        hkTempShiftEntity.setWeekDays("1,2,3");

        shiftRuleEntity = new HkShiftRuleEntity();
        shiftRuleEntity.setDayCnt(5);
        shiftRuleEntity.setEventAction("BEFORE");
        shiftRuleEntity.setEventInstance(new BigInteger("1"));
        shiftRuleEntity.setEventType("H");
        shiftRuleEntity.setHkShiftEntity(hkShiftEntity);
        shiftRuleEntity.setIsArchive(false);

        dtlEntity = new HkShiftDtlEntity();
        dtlEntity.setStrtTime(new Date());
        dtlEntity.setSlotType(HkSystemConstantUtil.SHIFT_TYPE.MAIN_TIME);
        dtlEntity.setSlotTitle(HkSystemConstantUtil.SHIFT_TYPE.BREAK_TIME);
        dtlEntity.setShift(hkShiftEntity);
        dtlEntity.setIsArchive(false);
        dtlEntity.setEndTime(new Date(new Date().getTime() + 900000L));
        dtlEntity.setEffectedFrm(new Date());
        dtlEntity.setEffectedEnd(new Date(new Date().getTime() + 2592000000L));
        dtlEntity.setCreatedOn(new Date());
        dtlEntity.setCreatedBy(1);
        dtlEntity.setShiftDurationMin(100);
        dtlEntity.setWeekDays("1,2,3");

        //hkShiftEntity.setHkShiftDtlEntitySet(shiftDtlEntitySet );
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of saveHoliday method, of class HkHRService.
     */
    @Test
    public void testSaveHoliday() {
        hkHRService.saveHoliday(holidayEntity, null);
    }

    @Test
    public void testRetrieveHolidayById() {
        hkHRService.saveHoliday(holidayEntity, Arrays.asList(2L));

        HkHolidayEntity result = hkHRService.retrieveHolidayById(holidayEntity.getId());
        assertEquals(holidayEntity, result);
    }

    /**
     * Test of removeHoliday method, of class HkHRService.
     */
    @Test
    public void testRemoveHoliday_HkHolidayEntity() {
        hkHRService.saveHoliday(holidayEntity, null);

        Long franchise = 0L;
        List<HkHolidayEntity> holidayList = hkHRService.retrieveAllHolidays(franchise, false);
        if (CollectionUtils.isEmpty(holidayList)) {
            fail("Holiday couldn't be created before deleting.");
        } else {
            holidayEntity = holidayList.get(0);
            hkHRService.removeHoliday(holidayEntity, null);
        }
    }

    /**
     * Test of removeHoliday method, of class HkHRService.
     */
    @Test
    public void testRemoveHoliday_Long() {
        hkHRService.saveHoliday(holidayEntity, null);

        Long franchise = 0L;
        List<HkHolidayEntity> holidayList = hkHRService.retrieveAllHolidays(franchise, false);
        if (CollectionUtils.isEmpty(holidayList)) {
            fail("Holiday couldn't be created before deleting.");
        } else {
            holidayEntity = holidayList.get(0);
            hkHRService.removeHoliday(holidayEntity.getId(), null);
        }
    }

    /**
     * Test of saveAllHolidays method, of class HkHRService.
     */
    @Test
    public void testSaveAllHolidays() {
        HkHolidayEntity holidayEntity1 = new HkHolidayEntity();
        holidayEntity1.setHolidayTitle("Kunchivaalam");
        holidayEntity1.setStartDt(new Date());
        holidayEntity1.setEndDt(new Date());
        holidayEntity1.setFranchise(0L);
        holidayEntity1.setCreatedBy(1L);
        holidayEntity1.setCreatedOn(new Date());
        holidayEntity1.setLastModifiedBy(1L);
        holidayEntity1.setLastModifiedOn(new Date());
        holidayEntity1.setIsActive(true);
        holidayEntity1.setIsArchive(false);

        List<HkHolidayEntity> holidayList = new ArrayList<>();
        holidayList.add(holidayEntity);
        holidayList.add(holidayEntity1);

        hkHRService.saveAllHolidays(holidayList);
    }

    /**
     * Test of retrieveAllHolidays method, of class HkHRService.
     */
    @Test
    public void testRetrieveAllHolidays() {

        //  create holiday first
        hkHRService.saveHoliday(holidayEntity, null);

        assertFalse(CollectionUtils.isEmpty(hkHRService.retrieveAllHolidays(0L, false)));
    }

    /**
     * Test of searchHolidaysByTitle method, of class HkHRService.
     */
    @Test
    public void testSearchHolidaysByTitle() {
        //  create holiday first
        hkHRService.saveHoliday(holidayEntity, null);

        String holidayTitle = "IpU";
        boolean archiveStatus = false;
        Long franchise = -1L;
        List<String> expResult = new ArrayList<>();
        expResult.add(holidayEntity.getHolidayTitle());

        List<String> result = hkHRService.searchPreviousYearsHolidaysByTitle(holidayTitle, franchise, archiveStatus);
        assertEquals(expResult, result);
    }

    /**
     * Test of retrieveHolidaysByCriteria method, of class HkHRService.
     */
    @Test
    public void testRetrieveHolidaysByCriteria() {
        //  create holiday first
        hkHRService.saveHoliday(holidayEntity, null);

        String title = null;
        Date startDate = new Date(1999 - 1900, 10, 10);
        Date endDate = new Date(1999 - 1900, 10, 11);
        Long franchise = 0L;
        Boolean archiveStatus = false;

        List<HkHolidayEntity> result = hkHRService.retrieveHolidaysByCriteria(title, startDate, endDate, false, franchise, archiveStatus);
        assertTrue(result.size() > 0);
    }

    /**
     * Test of createWorkflow method, of class HkHRService.
     */
    @Test
    public void testCreateWorkflow_HkWorkflowEntity() {
        hkHRService.createWorkflow(workflowEntity, null, null);

        HkWorkflowEntity resultWorkflow = hkHRService.retrieveWorkflowById(workflowEntity.getId(), false);
        assertEquals(workflowEntity, resultWorkflow);
    }

    /**
     * Test of createWorkflow method, of class HkHRService.
     */
    @Test
    public void testCreateWorkflow_List() {
        HkWorkflowEntity[] workflowArr = new HkWorkflowEntity[1];
        workflowArr[0] = workflowEntity;
        hkHRService.createWorkflow(workflowArr, null, null);

        HkWorkflowEntity resultWorkflow = hkHRService.retrieveWorkflowById(workflowEntity.getId(), false);
        assertEquals("Created workflow successfully", workflowEntity, resultWorkflow);
    }

    /**
     * Test of updateWorkflow method, of class HkHRService.
     */
    @Test
    public void testUpdateWorkflow() {
        hkHRService.createWorkflow(workflowEntity, null, null);
        HkWorkflowEntity resultWorkflow = hkHRService.retrieveWorkflowById(workflowEntity.getId(), false);
        resultWorkflow.setLastLevel(1);
        workflowApproverSet = new HashSet<>();
        HkWorkflowApproverEntity approvers = new HkWorkflowApproverEntity(resultWorkflow.getId(), "E", 3L);
        approvers.setLevel(1);
        approvers.setIsArchive(false);
        approvers.setHkWorkflowEntity(resultWorkflow);
        workflowApproverSet.add(approvers);
        resultWorkflow.setHkWorkflowApproverEntitySet(workflowApproverSet);
        hkHRService.updateWorkflow(resultWorkflow, workflowApproverSet, null, null);

        HkWorkflowEntity resultWorkflow1 = hkHRService.retrieveWorkflowById(resultWorkflow.getId(), false);
        assertEquals("Workflow updated successfully", workflowEntity, resultWorkflow1);
    }

    /**
     * Test of removeWorkflow method, of class HkHRService.
     */
    @Test
    public void testRemoveWorkflow() {
        hkHRService.createWorkflow(workflowEntity, null, null);

        hkHRService.removeWorkflow(workflowEntity.getId(), 2L);

        HkWorkflowEntity resultWorkflow = hkHRService.retrieveWorkflowById(workflowEntity.getId(), false);
        assertEquals(true, resultWorkflow.getIsArchive());
    }

    /**
     * Test of retrieveDepartmentIdsForExistingWorkflows method, of class
     * HkHRService.
     */
    @Test
    public void testRetrieveDepartmentIdsForExistingWorkflows() {
        hkHRService.createWorkflow(workflowEntity, null, null);

        List<Long> resultList = hkHRService.retrieveDepartmentIdsForExistingWorkflows(0L);
        assertTrue(resultList.size() > 0);
    }

    /**
     * Test of retrieveWorkflowById method, of class HkHRService.
     */
    @Test
    public void testRetrieveWorkflowById() {
        hkHRService.createWorkflow(workflowEntity, null, null);

        HkWorkflowEntity resultWorkflow = hkHRService.retrieveWorkflowById(workflowEntity.getId(), false);
        assertEquals(workflowEntity, resultWorkflow);
    }

    /**
     * Test of retrieveWorkflowApproversByWorkflowId method, of class
     * HkHRService.
     */
    @Test
    public void testRetrieveWorkflowApproversByWorkflowId() {
        hkHRService.createWorkflow(workflowEntity, null, null);

        List<HkWorkflowApproverEntity> resultList = hkHRService.retrieveWorkflowApproversByWorkflowId(workflowEntity.getId(), null, Boolean.FALSE);
        assertTrue(resultList.size() > 0);
    }

    @Test

    public void testRetrieveWorkflowApprover() {
        hkHRService.createWorkflow(workflowEntity, null, null);
        List<HkWorkflowApproverEntity> resultList = hkHRService.retrieveWorkFlowApprover(1L, workflowEntity.getDepartment());
        assertTrue(resultList.isEmpty());
    }

    /**
     * Test of retrieveWorkflowForDepartment method, of class HkHRService.
     */
    @Test
    public void testRetrieveWorkflowForDepartment() {
        hkHRService.createWorkflow(workflowEntity, null, null);

        HkWorkflowEntity resultWorkflow = hkHRService.retrieveWorkflowForDepartment(workflowEntity.getDepartment().longValue(), 0L, false);

        assertEquals(workflowEntity.getDepartment(), resultWorkflow.getDepartment());
    }

    /**
     * Test of issueAssets method, of class HkHRServiceImpl.
     */
//    @Test
    public void testIssueAssets() {
        HkCategoryEntity categoryEntity;

        //Category Creation
        categoryEntity = new HkCategoryEntity();
        categoryEntity.setCategoryPrefix("SD");
        categoryEntity.setCategoryTitle("Machines");
        categoryEntity.setCategoryType("AM");
        categoryEntity.setCreatedBy(1l);
        categoryEntity.setCreatedOn(new Date());
        categoryEntity.setCurrentIndex(1);
        categoryEntity.setFranchise(1l);
        categoryEntity.setIsActive(true);
        categoryEntity.setIsArchive(false);
        categoryEntity.setLastModifiedBy(1l);
        categoryEntity.setLastModifiedOn(new Date());
        categoryEntity.setStartIndex(1);

        hkOperationsService.createAssetCategory(categoryEntity);

        //Managed Asset Object Preparation
        HkAssetEntity assetEntity = new HkAssetEntity(null, HkSystemConstantUtil.ASSET_TYPE.MANAGED, "Machines", true, HkSystemConstantUtil.ASSET_STATUS.AVAILABLE, false, 1, new Date(), 1, new Date(), 1l);
        assetEntity.setSerialNumber(categoryEntity.getCurrentIndex());
        assetEntity.setCategory(categoryEntity);

        HkAssetPurchaserEntity purchaserEntity = new HkAssetPurchaserEntity();
        HkAssetPurchaserEntityPK hkAssetPurchaserEntityPK = new HkAssetPurchaserEntityPK();
        hkAssetPurchaserEntityPK.setAsset(assetEntity.getId());
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

        hkOperationsService.createAsset(assetEntity);

        //Non-Managed Asset Object Preparation
        HkAssetEntity assetEntity1 = new HkAssetEntity(null, HkSystemConstantUtil.ASSET_TYPE.NON_MANAGED, "Pencils", true, HkSystemConstantUtil.ASSET_STATUS.AVAILABLE, false, 1, new Date(), 1, new Date(), 1l);
        assetEntity1.setSerialNumber(categoryEntity.getCurrentIndex());
        assetEntity1.setRemainingUnits(100);
        assetEntity1.setCategory(categoryEntity);

        assetEntity1.setHkAssetPurchaserEntitySet(assetPurchaserEntitySet);

        assetEntity1.setHkAssetDocumentEntitySet(assetDocumentEntities);

        hkOperationsService.createAsset(assetEntity1);

        HkAssetIssueEntity assetIssueEntity = new HkAssetIssueEntity(null, "E", 1l, new Date(), HkSystemConstantUtil.ASSET_STATUS.AVAILABLE, 1l, new Date(), 1l, new Date(), false, 1l);
        assetIssueEntity.setAsset(assetEntity);

        HkAssetIssueEntity assetIssueEntity1 = new HkAssetIssueEntity(null, "E", 1l, new Date(), HkSystemConstantUtil.ASSET_STATUS.AVAILABLE, 1l, new Date(), 1l, new Date(), false, 1l);
        assetIssueEntity1.setAsset(assetEntity1);
        assetIssueEntity1.setIssuedUnits(15);

        List<HkAssetIssueEntity> assetIssueEntities = new LinkedList<>();
        assetIssueEntities.add(assetIssueEntity);
        assetIssueEntities.add(assetIssueEntity1);

        boolean expResult = true;
        boolean result = hkHRService.issueAssets(assetIssueEntities, null, null, null, null, null, null);
        assertEquals(expResult, result);
    }
//
//    /**
//     * Test of searchPreviousYearsHolidaysByTitle method, of class HkHRService.
//     */
//    //@Test
//    public void testSearchPreviousYearsHolidaysByTitle() {
//        System.out.println("searchPreviousYearsHolidaysByTitle");
//        String holidayTitle = "";
//        Long franchise = null;
//        Boolean archiveStatus = null;
//        List<String> expResult = null;
//        List<String> result = hkHRService.searchPreviousYearsHolidaysByTitle(holidayTitle, franchise, archiveStatus);
//        assertEquals(expResult, result);
//    }
//
//    /**
//     * Test of isHoliday method, of class HkHRService.
//     */
//    //@Test
//    public void testIsHoliday() {
//        System.out.println("isHoliday");
//        Date searchDate = null;
//        Long franchise = null;
//        boolean expResult = false;
//        boolean result = hkHRService.isHoliday(searchDate, franchise);
//        assertEquals(expResult, result);
//    }
//

    /**
     * Test of createEvent method, of class HkHRService.
     */
    @Test
    public void testCreateEvent() {
        hkCategoryService.createCategory(eventCategoryEntity, CategoryType.EVENT);
//        System.out.println("category created");
        eventEntity.setCategory(eventCategoryEntity);
        hkHRService.createEvent(eventEntity, new LinkedHashMap<>());
        HkEventEntity resultEvent = hkHRService.retrieveEventById(eventEntity.getId(), true, true);
        assertEquals(eventEntity, resultEvent);
    }

    /**
     * Test of updateEvent method, of class HkHRService.
     */
    @Test
    public void testUpdateEvent() {
        hkCategoryService.createCategory(eventCategoryEntity, CategoryType.EVENT);
        eventEntity.setCategory(eventCategoryEntity);
        hkHRService.createEvent(eventEntity, new LinkedHashMap<>());
        Set<Long> newUsersSet = new HashSet<>();
        newUsersSet.add(1L);
        eventEntity.setAddress("Townhall, Gandhinagar");
        hkHRService.updateEvent(eventEntity, newUsersSet, null, false);
        HkEventEntity resultEvent = hkHRService.retrieveEventById(eventEntity.getId(), true, false);
        assertEquals(eventEntity.getAddress(), resultEvent.getAddress());
    }

    /**
     * Test of searchEvent method, of class HkHRService.
     */
    @Test
    public void testSearchEvent() {
        hkCategoryService.createCategory(eventCategoryEntity, CategoryType.EVENT);
        eventEntity.setCategory(eventCategoryEntity);
        hkHRService.createEvent(eventEntity, new LinkedHashMap<>());
        List<HkEventEntity> resultList = hkHRService.searchEvents("@C " + eventCategoryEntity.getCategoryTitle(), eventEntity.getFranchise());
        assertTrue(resultList.size() > 0);
    }

    /**
     * Test of retrieveEventsByCriteria method, of class HkHRService.
     */
    @Test
    public void testRetrieveEventsByCriteria() {
        hkCategoryService.createCategory(eventCategoryEntity, CategoryType.EVENT);
        eventEntity.setCategory(eventCategoryEntity);
        hkHRService.createEvent(eventEntity, new LinkedHashMap<>());
        List<String> statusList = new ArrayList<>();
        statusList.add(HkSystemConstantUtil.EventStatus.CREATED);
        statusList.add(HkSystemConstantUtil.EventStatus.UPCOMING);
        List<HkEventEntity> result = hkHRService.retrieveEventsByCriteria(null, null, Boolean.FALSE, true, true, true);
        assertTrue(result.size() > 0);
    }

    /**
     * Test of retrieveRegistrationFieldsByEventId method, of class HkHRService.
     */
    @Test
    public void testRetrieveRegistrationFieldsByEventId() {
        hkCategoryService.createCategory(eventCategoryEntity, CategoryType.EVENT);
        eventEntity.setCategory(eventCategoryEntity);
        hkHRService.createEvent(eventEntity, new LinkedHashMap<>());
        List<HkEventRegistrationFieldEntity> resultList = hkHRService.retrieveRegistrationFieldsByEventId(eventEntity.getId());
        assertEquals(new ArrayList<>(eventRegFieldSet), resultList);
    }

    /**
     * Test of retrieveUpcomingEvents method, of class HkHRService.
     */
    //@Test
    public void testRetrieveUpcomingEvents() {
        hkCategoryService.createCategory(eventCategoryEntity, CategoryType.EVENT);
        eventEntity.setCategory(eventCategoryEntity);
        hkHRService.createEvent(eventEntity, new LinkedHashMap<>());
        List<HkEventEntity> result = hkHRService.retrieveUpcomingEvents(eventCategoryEntity.getId(), null, null, eventEntity.getFranchise(), false);
        if (!CollectionUtils.isEmpty(result)) {
            for (HkEventEntity event : result) {
                assertTrue(event.getPublishedOn().after(new Date()));
            }
        }
    }

    /**
     * Test of retrieveCompletedEvents method, of class HkHRService.
     */
    @Test
    public void testRetrieveCompletedEvents() {
        hkCategoryService.createCategory(eventCategoryEntity, CategoryType.EVENT);
        eventEntity.setCategory(eventCategoryEntity);
        hkHRService.createEvent(eventEntity, new LinkedHashMap<>());
        List<HkEventEntity> result = hkHRService.retrieveCompletedEvents(eventCategoryEntity.getId(), null, null, eventEntity.getFranchise(), false);
        if (!CollectionUtils.isEmpty(result)) {
            for (HkEventEntity event : result) {
                assertTrue(event.getToDt().before(new Date()));
            }
        }
    }

    /**
     * Test of retrieveActiveEventsCount method, of class HkHRService.
     */
    @Test
    public void testRetrieveActiveEventsCount() {
        hkCategoryService.createCategory(eventCategoryEntity, CategoryType.EVENT);
        eventEntity.setCategory(eventCategoryEntity);
        hkHRService.createEvent(eventEntity, new LinkedHashMap<>());
        Map<HkCategoryEntity, Integer> result = hkHRService.retrieveActiveEventsCount(eventEntity.getFranchise());
        assertTrue(result.size() > 0);
    }

    /**
     * Test of retrieveEventById method, of class HkHRService.
     */
    @Test
    public void testRetrieveEventById() {
        hkCategoryService.createCategory(eventCategoryEntity, CategoryType.EVENT);
        eventEntity.setCategory(eventCategoryEntity);
        hkHRService.createEvent(eventEntity, new LinkedHashMap<>());
        HkEventEntity result = hkHRService.retrieveEventById(eventEntity.getId(), true, true);
        assertEquals("retrievedEvent : " + eventEntity.getId(), eventEntity, result);
    }

    /**
     * Test of retrieveUserEventRegistrationEntities method, of class
     * HkHRService.
     */
    @Test
    public void testRetrieveUserEventRegistrationEntities() {
        hkCategoryService.createCategory(eventCategoryEntity, CategoryType.EVENT);
        eventEntity.setCategory(eventCategoryEntity);
        hkHRService.createEvent(eventEntity, new LinkedHashMap<>());
        eventRegistrationEntity = new HkEventRegistrationEntity();
        HkEventRegistrationEntityPK regPk = new HkEventRegistrationEntityPK();
        regPk.setEvent(eventEntity.getId());
        regPk.setUserId(1L);
        eventRegistrationEntity.setHkEventRegistrationEntityPK(regPk);
        eventRegistrationEntity.setAdultCount(4);
        eventRegistrationEntity.setChildCount(4);
        eventRegistrationEntity.setGuestCount(4);
        eventRegistrationEntity.setGuestInfo("family member");
        eventRegistrationEntity.setLastModifiedOn(new Date());
        eventRegistrationEntity.setStatus(HkSystemConstantUtil.EventUserRegistrationStatus.ATTENDING);
        eventRegistrationEntity.setIsArchive(false);
        eventRegistrationEntity.setHkEventEntity(eventEntity);

        hkHRService.registerForEvent(eventRegistrationEntity, null);
        List<HkEventRegistrationEntity> result = hkHRService.retrieveUserEventRegistrationEntities(eventEntity.getId(), 1L, false);
        assertTrue(result.size() > 0);
    }

    @Test
    public void testRegisterForEvent() {
        hkCategoryService.createCategory(eventCategoryEntity, CategoryType.EVENT);
        eventEntity.setCategory(eventCategoryEntity);
        hkHRService.createEvent(eventEntity, new LinkedHashMap<>());
        eventRegistrationEntity = new HkEventRegistrationEntity();
        eventRegistrationEntity.setAdultCount(1);
        eventRegistrationEntity.setChildCount(2);
        eventRegistrationEntity.setGuestCount(3);
        eventRegistrationEntity.setGuestInfo("ABC");
        eventRegistrationEntity.setLastModifiedOn(new Date());
        eventRegistrationEntity.setStatus(HkSystemConstantUtil.EventUserRegistrationStatus.ATTENDING);
        eventRegistrationEntity.setIsArchive(false);
        eventRegistrationEntity.setHkEventEntity(eventEntity);
        eventRegistrationEntity.setHkEventRegistrationEntityPK(new HkEventRegistrationEntityPK(eventEntity.getId(), 1L));
        hkHRService.registerForEvent(eventRegistrationEntity, null);
        List<HkEventRegistrationEntity> result = hkHRService.retrieveUserEventRegistrationEntities(eventEntity.getId(), 1L, false);
        assertTrue(result.size() > 0);
    }

    @Test
    public void testEditEventRegistration() {
        hkCategoryService.createCategory(eventCategoryEntity, CategoryType.EVENT);
        eventEntity.setCategory(eventCategoryEntity);
        hkHRService.createEvent(eventEntity, new LinkedHashMap<>());
        eventRegistrationEntity = new HkEventRegistrationEntity();
        eventRegistrationEntity.setAdultCount(3);
        eventRegistrationEntity.setChildCount(2);
        eventRegistrationEntity.setGuestCount(3);
        eventRegistrationEntity.setGuestInfo("CDC");
        eventRegistrationEntity.setStatus(HkSystemConstantUtil.EventUserRegistrationStatus.ATTENDING);
        eventRegistrationEntity.setIsArchive(false);
        eventRegistrationEntity.setHkEventEntity(eventEntity);
        eventRegistrationEntity.setHkEventRegistrationEntityPK(new HkEventRegistrationEntityPK(eventEntity.getId(), 1L));
        hkHRService.registerForEvent(eventRegistrationEntity, null);
        List<HkEventRegistrationEntity> result = hkHRService.retrieveUserEventRegistrationEntities(eventEntity.getId(), 1L, false);
        for (HkEventRegistrationEntity hkEventRegistrationEntity : result) {
            hkEventRegistrationEntity.setAdultCount(23);
            hkHRService.editEventRegistration(hkEventRegistrationEntity, null);
            assertTrue(eventRegistrationEntity.getAdultCount() == hkEventRegistrationEntity.getAdultCount());
        }
    }

    @Test
    public void testCancelEventRegistration() {
        hkCategoryService.createCategory(eventCategoryEntity, CategoryType.EVENT);
        eventEntity.setCategory(eventCategoryEntity);
        hkHRService.createEvent(eventEntity, new LinkedHashMap<>());
        eventRegistrationEntity = new HkEventRegistrationEntity();
        eventRegistrationEntity.setAdultCount(3);
        eventRegistrationEntity.setChildCount(2);
        eventRegistrationEntity.setGuestCount(3);
        eventRegistrationEntity.setGuestInfo("CDC");
        eventRegistrationEntity.setStatus(HkSystemConstantUtil.EventUserRegistrationStatus.ATTENDING);
        eventRegistrationEntity.setIsArchive(false);
        eventRegistrationEntity.setHkEventEntity(eventEntity);
        eventRegistrationEntity.setHkEventRegistrationEntityPK(new HkEventRegistrationEntityPK(eventEntity.getId(), 1L));
        hkHRService.registerForEvent(eventRegistrationEntity, null);
        hkHRService.cancelEventRegistration(eventEntity.getId(), 1L);
        assertEquals(true, eventRegistrationEntity.getIsArchive());
    }

    @Test
    public void testupdateEvents() {
        HkEventEntity events = new HkEventEntity();
        events.setAddress("A/66, GIDC");
        events.setBannerImageName("Argusoft Banner");
        events.setContentColor("Red");
        events.setCreatedBy(1L);
        events.setCreatedOn(new Date());
        events.setDescription("Saree Day");
        events.setEndTime(new Date(2014 - 1900, 11, 11, 11, 11, 11));
        events.setEventTitle("Traditional Day at Argus");
        events.setFolderName("traditional day");
        events.setFranchise(0L);
        events.setFrmDt(new Date(2014 - 1900, 11, 11));
        events.setInvitationTemplateName("traditional day template");
        events.setIsArchive(false);
        events.setLabelColor("Blue");
        events.setLastModifiedBy(1L);
        events.setLastModifiedOn(new Date());
        events.setPublishedOn(new Date(2014 - 1900, 11, 5));
        events.setRegistrationFormName("Garba form");
        events.setRegistrationLastDt(new Date(2014 - 1900, 11, 10));
        events.setRegistrationType(HkSystemConstantUtil.EventRegistrationStatus.ONLINE);
        events.setStrtTime(new Date(2014 - 1900, 11, 11, 7, 7, 7));
        events.setToDt(new Date(2014 - 1900, 11, 11));
        hkCategoryService.createCategory(eventCategoryEntity, CategoryType.EVENT);
        events.setCategory(eventCategoryEntity);
        hkHRService.createEvent(events, new LinkedHashMap<>());
        eventEntity.setCategory(eventCategoryEntity);
        hkHRService.createEvent(eventEntity, new LinkedHashMap<>());
        List<HkEventEntity> eventList = new LinkedList<>();
        eventList.add(eventEntity);
        eventList.add(events);
        hkHRService.updateEvents(eventList, null);
    }

    @Test
    public void testRemoveEvent() {
        hkCategoryService.createCategory(eventCategoryEntity, CategoryType.EVENT);
        eventEntity.setCategory(eventCategoryEntity);
        hkHRService.createEvent(eventEntity, new LinkedHashMap<>());
        hkHRService.removeEvent(eventEntity.getId(), 1L);
        assertEquals(true, eventEntity.getIsArchive());
    }

    @Test
    public void testArchiveEvent() {
        hkCategoryService.createCategory(eventCategoryEntity, CategoryType.EVENT);
        eventEntity.setCategory(eventCategoryEntity);
        hkHRService.createEvent(eventEntity, new LinkedHashMap<>());
        hkHRService.archiveEvent(eventEntity.getId(), 1L);
        assertEquals(HkSystemConstantUtil.EventStatus.COMPLETED_ARCHIVED, eventEntity.getStatus());
    }

    /**
     * Test method for
     * {@link com.argusoft.hkg.core.impl.HkHRServiceImpl#createShift(com.argusoft.hkg.model.HkShiftEntity)}.
     */
    @Test
    public void testCreateShift() {
        hkHRService.createShift(hkShiftEntity, null, null, false, null);
        assertNotNull(hkShiftEntity.getId());
    }

    /**
     * Test method for
     * {@link com.argusoft.hkg.core.impl.HkHRServiceImpl#updateShift(com.argusoft.hkg.model.HkShiftEntity)}.
     */
//    @Test
    public void testUpdateShift() {
        hkHRService.createShift(hkShiftEntity, null, null, false, null);
        hkShiftEntity = hkHRService.retrieveShifts(0l).get(0);
        HkShiftDepartmentEntity shiftDepartment = new HkShiftDepartmentEntity();
        hkShiftEntity.setShiftTitle("Updated test");
        shiftDepartment.setHkShiftEntity(hkShiftEntity);
        shiftDepartment.setIsArchive(false);
        shiftDepartment.setLastModifiedOn(new Date());
        shiftDepartment.setHkShiftDepartmentEntityPK(new HkShiftDepartmentEntityPK(hkShiftEntity.getId(), 2l));
        Set<HkShiftDepartmentEntity> hkShiftDepartmentEntitys = new HashSet<>();
        hkShiftDepartmentEntitys.add(shiftDepartment);
        hkShiftEntity.setHkShiftDepartmentEntitySet(hkShiftDepartmentEntitys);

        dtlEntity.setId(null);
        dtlEntity.setShift(hkShiftEntity);
        dtlEntity.setSlotTitle("LUNCH BREAK");
        dtlEntity.setWeekDays(hkShiftEntity.getWeekDays());

        Set<HkShiftDtlEntity> hkShiftDtlEntitySet = new HashSet<>();
        hkShiftDtlEntitySet.add(dtlEntity);
        hkShiftEntity.setHkShiftDtlEntitySet(hkShiftDtlEntitySet);

        hkHRService.updateShift(hkShiftEntity, null);
        assertNotNull(hkHRService.retrieveShifts(0l).get(0));
        //hkShiftDtlEntitySet = hkHRService.retrieveShifts(0).get(0).getHkShiftDtlEntitySet();
//		for (HkShiftDtlEntity hkShiftDtlEntity : hkShiftDtlEntitySet) {
//			if(hkShiftDtlEntity.getSlotTitle().contains("TEA"))
//				assertTrue(hkShiftDtlEntity.getIsArchive());
//			else if(hkShiftDtlEntity.getSlotTitle().contains("TEA"))
//				assertTrue(hkShiftDtlEntity.getIsArchive());
//		}
//        assertTrue(hkHRService.retrieveShifts(0).get(0).getHkShiftDepartmentEntitySet().size() == 2);
    }

    @Test
    public void testRemoveShift() {
        hkHRService.createShift(hkShiftEntity, null, null, false, null);
        assertTrue(hkHRService.retrieveShifts(0l).size() > 0);
        hkHRService.removeShift(hkShiftEntity.getId(), 1L, null);
    }

    /**
     * Test method for
     * {@link com.argusoft.hkg.core.impl.HkHRServiceImpl#retrieveShifts(long)}.
     */
    @Test
    public void testRetrieveShifts() {
        hkHRService.createShift(hkShiftEntity, null, null, false, null);
        assertNotNull(hkShiftEntity.getId());
        assertNotNull(hkHRService.retrieveShifts(0l).size() == 1);
    }

    /**
     * Test method for
     * {@link com.argusoft.hkg.core.impl.HkHRServiceImpl#searchShifts(long, java.lang.String)}.
     */
    @Test
    public void testSearchShifts() {
        hkHRService.createShift(hkShiftEntity, null, null, false, null);
        assertNotNull(String.valueOf(hkHRService.searchShifts(0, "TEST").size()), hkHRService.searchShifts(0, "TEST").size() == 1);
    }

}
