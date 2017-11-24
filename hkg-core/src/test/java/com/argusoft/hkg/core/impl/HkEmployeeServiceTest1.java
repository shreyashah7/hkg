/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.core.impl;

import com.argusoft.generic.core.config.CoreApplicationConfig;
import com.argusoft.hkg.common.constantutil.HkSystemConstantUtil;
import com.argusoft.hkg.core.HKCategoryService;
import com.argusoft.hkg.core.HkFoundationService;
import com.argusoft.hkg.core.HkHRService;
import com.argusoft.hkg.model.HkCategoryEntity;
import com.argusoft.hkg.model.HkEventEntity;
import com.argusoft.hkg.model.HkEventRecipientEntity;
import com.argusoft.hkg.model.HkEventRegistrationEntity;
import com.argusoft.hkg.model.HkEventRegistrationFieldEntity;
import com.argusoft.hkg.model.HkHolidayEntity;
import com.argusoft.hkg.model.HkShiftDepartmentEntity;
import com.argusoft.hkg.model.HkShiftDepartmentEntityPK;
import com.argusoft.hkg.model.HkShiftDtlEntity;
import com.argusoft.hkg.model.HkShiftEntity;
import com.argusoft.hkg.model.HkShiftRuleEntity;
import com.argusoft.hkg.model.HkShiftRuleEntityPK;
import com.argusoft.hkg.model.HkWorkflowApproverEntity;
import com.argusoft.hkg.model.HkWorkflowEntity;
import java.math.BigInteger;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.annotation.Resource;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

/**
 * Test class for HkEmployeeService.
 *
 * @author Mital
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = {CoreApplicationConfig.class})
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = false)
@Transactional
public class HkEmployeeServiceTest1 {

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
    private Set<HkEventRecipientEntity> eventRecipientSet = new HashSet<>();
    private Set<HkEventRegistrationFieldEntity> eventRegFieldSet = new HashSet<>();
    private HkEventRegistrationEntity eventRegistrationEntity;
    private HkShiftEntity hkShiftEntity;
    private HkShiftEntity hkTempShiftEntity;
    private HkShiftRuleEntity shiftRuleEntity;
    private HkShiftDtlEntity dtlEntity;

    public HkEmployeeServiceTest1() {
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

        workflowEntity = new HkWorkflowEntity(null, false, 1L, new Date(), 1L, new Date(), 0L);
        workflowEntity.setLastLevel(1);
        workflowEntity.setDepartment(1L);

        workflowApproverSet = new HashSet<>();
        HkWorkflowApproverEntity approver = new HkWorkflowApproverEntity(0L, "D", 2L);
        approver.setLevel(1);
        approver.setIsArchive(false);
        workflowApproverSet.add(approver);
        workflowEntity.setHkWorkflowApproverEntitySet(workflowApproverSet);

        eventCategoryEntity = new HkCategoryEntity(null, "Event Category", null, 1L, new Date(), 1L, new Date(), true, false, 1L, false);

        eventEntity = new HkEventEntity();
        eventEntity.setAddress("A/66, GIDC");
        eventEntity.setBannerImageName("Argusoft Banner");
        eventEntity.setCategory(eventCategoryEntity);
        eventEntity.setContentColor("Red");
        eventEntity.setCreatedBy(1L);
        eventEntity.setCreatedOn(new Date());
        eventEntity.setDescription("Garba function");
        eventEntity.setEndTime(new Date(2014 - 1900, 11, 11, 11, 11, 11));
        eventEntity.setEventTitle("Garba at Argus");
        eventEntity.setFolderName("Garba");
        eventEntity.setFranchise(1L);
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
        HkEventRecipientEntity recipient = new HkEventRecipientEntity(0L, "D", 1L);
        recipient.setFranchise(1L);
        recipient.setIsArchive(false);
        eventRecipientSet.add(recipient);
        eventEntity.setHkEventRecipientEntitySet(eventRecipientSet);

        eventRegFieldSet = new HashSet<>();
        HkEventRegistrationFieldEntity eventRegField = new HkEventRegistrationFieldEntity(112L, "First Name", "alphabets", false, 1L, new Date());
        eventRegFieldSet.add(eventRegField);
        HkEventRegistrationFieldEntity eventRegField1 = new HkEventRegistrationFieldEntity(113L, "Last Name", "alphanum", false, 1L, new Date());
        eventRegFieldSet.add(eventRegField1);
        eventEntity.setHkEventRegistrationFieldEntitySet(eventRegFieldSet);

        eventRegistrationEntity = new HkEventRegistrationEntity();

        //Shifts
        hkShiftEntity = new HkShiftEntity();
        hkShiftEntity.setCreatedBy(1L);
        hkShiftEntity.setCreatedOn(new Date());
        hkShiftEntity.setFranchise(0);
        hkShiftEntity.setFrmDt(new Date());
        hkShiftEntity.setHasRule(false);
        hkShiftEntity.setIsArchive(false);
        hkShiftEntity.setIsDefault(false);
        hkShiftEntity.setShiftTitle("TEST SHIFT");
        hkShiftEntity.setStatus("T");
        hkShiftEntity.setToDt(new Date(new Date().getTime() + 1000000000L));
        hkShiftEntity.setLastModifiedBy(0);
        hkShiftEntity.setLastModifiedOn(new Date());
        hkShiftEntity.setWeekDays("MTWTF");
        Set<HkShiftDepartmentEntity> hkShiftDepartmentEntitySet = new HashSet<HkShiftDepartmentEntity>();
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
        hkTempShiftEntity.setStatus("T");
        hkTempShiftEntity.setToDt(new Date(new Date().getTime() + 500000000L));
        hkTempShiftEntity.setLastModifiedBy(0);
        hkTempShiftEntity.setLastModifiedOn(new Date());
        hkTempShiftEntity.setWeekDays("MTWTF");

        shiftRuleEntity = new HkShiftRuleEntity();
        shiftRuleEntity.setDayCnt(5);
        shiftRuleEntity.setEventAction("A");
        shiftRuleEntity.setEventInstance(new BigInteger("1"));
        shiftRuleEntity.setEventType("H");
        shiftRuleEntity.setHkShiftEntity(hkShiftEntity);
        shiftRuleEntity.setHkShiftRuleEntityPK(new HkShiftRuleEntityPK(1,"R"));
        shiftRuleEntity.setIsArchive(false);

        dtlEntity = new HkShiftDtlEntity();
        dtlEntity.setStrtTime(new Date());
        dtlEntity.setSlotType("BREAK");
        dtlEntity.setSlotTitle("TEA BREAK");
        //dtlEntity.setShift(hkShiftEntity);
        dtlEntity.setIsArchive(false);
        dtlEntity.setEndTime(new Date(new Date().getTime() + 900000L));
        dtlEntity.setEffectedFrm(new Date());
        dtlEntity.setEffectedEnd(new Date(new Date().getTime() + 2592000000L));
        dtlEntity.setCreatedOn(new Date());
        dtlEntity.setCreatedBy(1);
        dtlEntity.setShift(hkShiftEntity);
        dtlEntity.setWeekDays("M");

        //hkShiftEntity.setHkShiftDtlEntitySet(shiftDtlEntitySet );
    }

    @After
    public void tearDown() {
    }
    
    /**
     * Test method for
     * {@link com.argusoft.hkg.core.impl.HkHRServiceImpl#createShift(com.argusoft.hkg.model.HkShiftEntity)}.
     */
    @Test
    public void testCreateShift() {
        hkShiftEntity.setHasRule(true);
        
        Set<HkShiftRuleEntity> ruleSet = new HashSet<>();
        ruleSet.add(shiftRuleEntity);
        hkShiftEntity.setHkShiftRuleEntitySet(ruleSet);
        
//        hkShiftEntity.setHkShiftDepartmentEntitySet(null);
        
        Set<HkShiftDtlEntity> dtlSet = new HashSet<>();
        dtlSet.add(dtlEntity);
        hkShiftEntity.setHkShiftDtlEntitySet(dtlSet);
        
        hkHRService.createShift(hkShiftEntity, null, null, false, 55L);
        assertNotNull(hkShiftEntity.getId());
        
        dtlSet = new HashSet<>();
        dtlEntity = new HkShiftDtlEntity();
        dtlEntity.setStrtTime(new Date());
        dtlEntity.setSlotType("MAIN");
        dtlEntity.setSlotTitle(null);
        //dtlEntity.setShift(hkShiftEntity);
        dtlEntity.setIsArchive(false);
        dtlEntity.setEndTime(new Date(new Date().getTime() + 900000L));
        dtlEntity.setEffectedFrm(new Date());
        dtlEntity.setCreatedOn(new Date());
        dtlEntity.setCreatedBy(1);
        dtlEntity.setShift(hkShiftEntity);
        dtlEntity.setWeekDays("M");
        dtlSet.add(dtlEntity);
        hkShiftEntity.setHkShiftDtlEntitySet(dtlSet);
//        hkHRService.updateShift(hkShiftEntity, null);
    }
}
