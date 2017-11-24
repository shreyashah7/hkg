///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package com.argusoft.activity.core.impl;
//
//import com.argusoft.activity.common.constantutil.ActSystemConstantUtil;
//import com.argusoft.activity.core.ActActivityFlowService;
//import com.argusoft.activity.core.expection.ActivityCoreException;
//import com.argusoft.activity.model.ActActivityFlowEntity;
//import com.argusoft.activity.model.ActActivityFlowGroupEntity;
//import com.argusoft.activity.model.ActActivityFlowNodeEntity;
//import com.argusoft.activity.model.ActActivityFlowNodeRouteEntity;
//import com.argusoft.activity.model.ActActivityFlowVersionEntity;
//import com.argusoft.activity.model.ActServiceAssociationEntity;
//import com.argusoft.activity.model.ActServiceEntity;
//import com.argusoft.generic.core.config.CoreApplicationConfig;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.HashSet;
//import java.util.LinkedHashSet;
//import java.util.LinkedList;
//import java.util.List;
//import java.util.Set;
//import javax.annotation.Resource;
//import org.junit.After;
//import org.junit.AfterClass;
//import org.junit.Assert;
//import static org.junit.Assert.assertNotNull;
//import org.junit.Before;
//import org.junit.BeforeClass;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//import org.springframework.test.context.support.AnnotationConfigContextLoader;
//import org.springframework.test.context.transaction.TransactionConfiguration;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.util.CollectionUtils;
//
///**
// * Test class to test the methods of ActActivityFlowService.
// *
// * @author Mital
// */
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = {CoreApplicationConfig.class})
//@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
//@Transactional
//public class ActActivityFlowServiceImplTest {
//
//    /**
//     * Services
//     */
//    @Resource
//    private ActActivityFlowService activityFlowService;
//
//    /**
//     * Entities
//     */
//    private List<ActServiceEntity> serviceEntities;
//    private List<ActServiceEntity> associatedServiceEntities;
//
//    private ActActivityFlowEntity activityFlowEntity;
//    private ActActivityFlowVersionEntity flowVersionEntity;
//    private ActActivityFlowNodeRouteEntity nodeRouteEntity;
//
//    private final Date currentDate = new Date();
//    private final Long companyId = currentDate.getTime();
//
//    public ActActivityFlowServiceImplTest() {
//    }
//
//    @BeforeClass
//    public static void setUpClass() {
//    }
//
//    @AfterClass
//    public static void tearDownClass() {
//    }
//
//    @Before
//    public void setUp() {
//
//        Long userId = 1l;
//
//        //Generate Services
//        serviceEntities = new ArrayList<>();
//
//        serviceEntities.add(new ActServiceEntity(null, "101", "Assortment Required?", "/addplan", "A", false, companyId, 1l, new Date(), 1l, new Date()));
//        serviceEntities.add(new ActServiceEntity(null, "102", "Received at assortment", "/addplan", "A", false, companyId, 1l, new Date(), 1l, new Date()));
//        serviceEntities.add(new ActServiceEntity(null, "103", "Assort stones, enter rough prediction for each category", "/addplan", "A", false, companyId, 1l, new Date(), 1l, new Date()));
//        serviceEntities.add(new ActServiceEntity(null, "104", "Packet creation required here?", "/addplan", "A", false, companyId, 1l, new Date(), 1l, new Date()));
//        serviceEntities.add(new ActServiceEntity(null, "105", "Create lots", "/addplan", "A", false, companyId, 1l, new Date(), 1l, new Date()));
//        serviceEntities.add(new ActServiceEntity(null, "106", "Packet creation and barcoding", "/addplan", "A", false, companyId, 1l, new Date(), 1l, new Date()));
//        serviceEntities.add(new ActServiceEntity(null, "107", "Is sub lot required?", "/addplan", "A", false, companyId, 1l, new Date(), 1l, new Date()));
//        serviceEntities.add(new ActServiceEntity(null, "108", "Sub lot creation", "/addplan", "A", false, companyId, 1l, new Date(), 1l, new Date()));
//        serviceEntities.add(new ActServiceEntity(null, "109", "Is individual estimation required?", "/addplan", "A", false, companyId, 1l, new Date(), 1l, new Date()));
//        serviceEntities.add(new ActServiceEntity(null, "110", "Enter rough prediction for each packet", "/addplan", "A", false, companyId, 1l, new Date(), 1l, new Date()));
//
//        activityFlowService.saveAllServices(serviceEntities);
//
//        //Associate Services
//        associatedServiceEntities = new LinkedList<>();
//        associatedServiceEntities.add(activityFlowService.retrieveServiceByCode("102", companyId));
//        associatedServiceEntities.add(activityFlowService.retrieveServiceByCode("103", companyId));
//        associatedServiceEntities.add(activityFlowService.retrieveServiceByCode("104", companyId));
//
//        activityFlowService.saveAllServices(serviceEntities);
//
//        //Create Activity Flow
//        activityFlowEntity = new ActActivityFlowEntity();
//        activityFlowEntity.setCompany(companyId);
//        activityFlowEntity.setDescription("Harikrishna Process Description");
//        activityFlowEntity.setFlowName("Harikrishna Process");
//        activityFlowEntity.setIsArchive(false);
//        activityFlowEntity.setLastModifiedBy(userId);
//        activityFlowEntity.setLastModifiedOn(currentDate);
//        activityFlowEntity.setStatus(ActSystemConstantUtil.STATUS.ACTIVE);
//
//        //Create Activity Flow Version
//        flowVersionEntity = new ActActivityFlowVersionEntity();
//        flowVersionEntity.setActivity(activityFlowEntity);
//        flowVersionEntity.setCompany(companyId);
//        flowVersionEntity.setCreatedBy(userId);
//        flowVersionEntity.setCreatedOn(currentDate);
//        flowVersionEntity.setIsArchive(false);
//        flowVersionEntity.setLastModifiedBy(userId);
//        flowVersionEntity.setLastModifiedOn(currentDate);
//        flowVersionEntity.setStatus(ActSystemConstantUtil.STATUS.PENDING);
//        flowVersionEntity.setVersionCode(new Float(0.1));
//
//        activityFlowEntity.setCurrentVersion(flowVersionEntity);
//
//        Set<ActActivityFlowVersionEntity> versionList = new LinkedHashSet<>();
//        versionList.add(flowVersionEntity);
//        activityFlowEntity.setActivityFlowVersions(versionList);
//
//        //Define Activity Flow Group
//        ActActivityFlowGroupEntity planningGroupEntity = new ActActivityFlowGroupEntity();
//        planningGroupEntity.setActivityVersion(flowVersionEntity);
//        planningGroupEntity.setGroupName("Planning");
//        planningGroupEntity.setDescription("Activity for Planning");
//        planningGroupEntity.setIsArchive(false);
//        planningGroupEntity.setLastModifiedBy(userId);
//        planningGroupEntity.setLastModifiedOn(currentDate);
//        planningGroupEntity.setStatus(ActSystemConstantUtil.STATUS.ACTIVE);
//
//        ActActivityFlowGroupEntity assortmentGroupEntity = new ActActivityFlowGroupEntity();
//        assortmentGroupEntity.setActivityVersion(flowVersionEntity);
//        assortmentGroupEntity.setGroupName("Assortment");
//        assortmentGroupEntity.setDescription("Activity for Assortment");
//        assortmentGroupEntity.setIsArchive(false);
//        assortmentGroupEntity.setLastModifiedBy(userId);
//        assortmentGroupEntity.setLastModifiedOn(currentDate);
//        assortmentGroupEntity.setStatus(ActSystemConstantUtil.STATUS.ACTIVE);
//
//        //Define Activity Flow Nodes
//        ActActivityFlowNodeEntity node101 = new ActActivityFlowNodeEntity();
//        node101.setActivityGroup(planningGroupEntity);
//        node101.setActivityVersion(flowVersionEntity);
//        node101.setAssociatedService(activityFlowService.retrieveServiceByCode("101", companyId));
//        node101.setIsArchive(false);
//        node101.setIsRoot(true);
//        node101.setLastModifiedBy(userId);
//        node101.setLastModifiedOn(currentDate);
//
//        ActActivityFlowNodeEntity node101Route = new ActActivityFlowNodeEntity();
//        node101Route.setIsArchive(false);
//        node101Route.setLastModifiedBy(userId);
//        node101Route.setLastModifiedOn(currentDate);
//
//        //  node101.setNextService(activityFlowService.retrieveServiceByCode("102", companyId));
//        Set<ActActivityFlowNodeEntity> planningGroupNodes = new LinkedHashSet<>();
//        planningGroupNodes.add(node101);
//
//        planningGroupEntity.setActivityFlowNodes(planningGroupNodes);
//
//        ActActivityFlowNodeEntity node102 = new ActActivityFlowNodeEntity();
//        node102.setActivityGroup(assortmentGroupEntity);
//        node102.setActivityVersion(flowVersionEntity);
//        node102.setAssociatedService(activityFlowService.retrieveServiceByCode("102", companyId));
//        node102.setIsArchive(false);
//        node102.setIsRoot(true);
//        node102.setLastModifiedBy(userId);
//        node102.setLastModifiedOn(currentDate);
//        // node102.setNextService(activityFlowService.retrieveServiceByCode("103", companyId));
//
//        ActActivityFlowNodeEntity node103 = new ActActivityFlowNodeEntity();
//        node103.setActivityGroup(assortmentGroupEntity);
//        node103.setActivityVersion(flowVersionEntity);
//        node103.setAssociatedService(activityFlowService.retrieveServiceByCode("103", companyId));
//        node103.setIsArchive(false);
//        node103.setIsRoot(false);
//        node103.setLastModifiedBy(userId);
//        node103.setLastModifiedOn(currentDate);
//        // node103.setNextService(activityFlowService.retrieveServiceByCode("104", companyId));
//
//        ActActivityFlowNodeEntity node104 = new ActActivityFlowNodeEntity();
//        node104.setActivityGroup(assortmentGroupEntity);
//        node104.setActivityVersion(flowVersionEntity);
//        node104.setAssociatedService(activityFlowService.retrieveServiceByCode("104", companyId));
//        node104.setIsArchive(false);
//        node104.setIsRoot(false);
//        node104.setLastModifiedBy(userId);
//        node104.setLastModifiedOn(currentDate);
//        // node104.setNextService(activityFlowService.retrieveServiceByCode("105", companyId));
//        //  node104.setNodeStatus(ActSystemConstantUtil.NODE_STATUS.YES);
//
//        ActActivityFlowNodeEntity node104N = new ActActivityFlowNodeEntity();
//        node104N.setActivityGroup(assortmentGroupEntity);
//        node104N.setActivityVersion(flowVersionEntity);
//        node104N.setAssociatedService(activityFlowService.retrieveServiceByCode("104", companyId));
//        node104N.setIsArchive(false);
//        node104N.setIsRoot(false);
//        node104N.setLastModifiedBy(userId);
//        node104N.setLastModifiedOn(currentDate);
//        // node104N.setNextService(activityFlowService.retrieveServiceByCode("101", companyId));
//        //node104N.setNodeStatus(ActSystemConstantUtil.NODE_STATUS.NO);
//
//        ActActivityFlowNodeEntity node105 = new ActActivityFlowNodeEntity();
//        node105.setActivityGroup(assortmentGroupEntity);
//        node105.setActivityVersion(flowVersionEntity);
//        node105.setAssociatedService(activityFlowService.retrieveServiceByCode("105", companyId));
//        node105.setIsArchive(false);
//        node105.setIsRoot(false);
//        node105.setLastModifiedBy(userId);
//        node105.setLastModifiedOn(currentDate);
//        //node105.setNextService(activityFlowService.retrieveServiceByCode("106", companyId));
//
//        ActActivityFlowNodeEntity node106 = new ActActivityFlowNodeEntity();
//        node106.setActivityGroup(assortmentGroupEntity);
//        node106.setActivityVersion(flowVersionEntity);
//        node106.setAssociatedService(activityFlowService.retrieveServiceByCode("106", companyId));
//        node106.setIsArchive(false);
//        node106.setIsRoot(false);
//        node106.setLastModifiedBy(userId);
//        node106.setLastModifiedOn(currentDate);
//        // node106.setNextService(activityFlowService.retrieveServiceByCode("107", companyId));
//
//        ActActivityFlowNodeEntity node107 = new ActActivityFlowNodeEntity();
//        node107.setActivityGroup(assortmentGroupEntity);
//        node107.setActivityVersion(flowVersionEntity);
//        node107.setAssociatedService(activityFlowService.retrieveServiceByCode("107", companyId));
//        node107.setIsArchive(false);
//        node107.setIsRoot(false);
//        node107.setLastModifiedBy(userId);
//        node107.setLastModifiedOn(currentDate);
//        //node107.setNextService(activityFlowService.retrieveServiceByCode("108", companyId));
//        // node107.setNodeStatus(ActSystemConstantUtil.NODE_STATUS.YES);
//
//        ActActivityFlowNodeEntity node107N = new ActActivityFlowNodeEntity();
//        node107N.setActivityGroup(assortmentGroupEntity);
//        node107N.setActivityVersion(flowVersionEntity);
//        node107N.setAssociatedService(activityFlowService.retrieveServiceByCode("107", companyId));
//        node107N.setIsArchive(false);
//        node107N.setIsRoot(true);
//        node107N.setLastModifiedBy(userId);
//        node107N.setLastModifiedOn(currentDate);
//        //node107N.setNextService(activityFlowService.retrieveServiceByCode("109", companyId));
//        //node107N.setNodeStatus(ActSystemConstantUtil.NODE_STATUS.NO);
//
//        ActActivityFlowNodeEntity node108 = new ActActivityFlowNodeEntity();
//        node108.setActivityGroup(assortmentGroupEntity);
//        node108.setActivityVersion(flowVersionEntity);
//        node108.setAssociatedService(activityFlowService.retrieveServiceByCode("108", companyId));
//        node108.setIsArchive(false);
//        node108.setIsRoot(false);
//        node108.setLastModifiedBy(userId);
//        node108.setLastModifiedOn(currentDate);
//        // node108.setNextService(activityFlowService.retrieveServiceByCode("107", companyId));
//
//        ActActivityFlowNodeEntity node109 = new ActActivityFlowNodeEntity();
//        node109.setActivityGroup(assortmentGroupEntity);
//        node109.setActivityVersion(flowVersionEntity);
//        node109.setAssociatedService(activityFlowService.retrieveServiceByCode("109", companyId));
//        node109.setIsArchive(false);
//        node109.setIsRoot(false);
//        node109.setLastModifiedBy(userId);
//        node109.setLastModifiedOn(currentDate);
//        //  node109.setNextService(activityFlowService.retrieveServiceByCode("110", companyId));
//        //  node109.setNodeStatus(ActSystemConstantUtil.NODE_STATUS.YES);
//
//        ActActivityFlowNodeEntity node109N = new ActActivityFlowNodeEntity();
//        node109N.setActivityGroup(assortmentGroupEntity);
//        node109N.setActivityVersion(flowVersionEntity);
//        node109N.setAssociatedService(activityFlowService.retrieveServiceByCode("109", companyId));
//        node109N.setIsArchive(false);
//        node109N.setIsRoot(false);
//        node109N.setLastModifiedBy(userId);
//        node109N.setLastModifiedOn(currentDate);
//        //  node109N.setNextService(activityFlowService.retrieveServiceByCode("101", companyId));
//        //   node109N.setNodeStatus(ActSystemConstantUtil.NODE_STATUS.NO);
//
//        ActActivityFlowNodeEntity node110 = new ActActivityFlowNodeEntity();
//        node110.setActivityGroup(assortmentGroupEntity);
//        node110.setActivityVersion(flowVersionEntity);
//        node110.setAssociatedService(activityFlowService.retrieveServiceByCode("110", companyId));
//        node110.setIsArchive(false);
//        node110.setIsRoot(false);
//        node110.setLastModifiedBy(userId);
//        node110.setLastModifiedOn(currentDate);
//
//        Set<ActActivityFlowNodeEntity> assortmentGroupNodes = new LinkedHashSet<>();
//        assortmentGroupNodes.add(node102);
//        assortmentGroupNodes.add(node103);
//        assortmentGroupNodes.add(node104);
//        assortmentGroupNodes.add(node104N);
//        assortmentGroupNodes.add(node105);
//        assortmentGroupNodes.add(node106);
//        assortmentGroupNodes.add(node107);
//        assortmentGroupNodes.add(node107N);
//        assortmentGroupNodes.add(node108);
//        assortmentGroupNodes.add(node109);
//        assortmentGroupNodes.add(node109N);
//        assortmentGroupNodes.add(node110);
//
//        assortmentGroupEntity.setActivityFlowNodes(assortmentGroupNodes);
//
//        Set<ActActivityFlowGroupEntity> groupEntities = new LinkedHashSet<>();
//        groupEntities.add(planningGroupEntity);
//        groupEntities.add(assortmentGroupEntity);
//
//        flowVersionEntity.setActivityFlowGroups(groupEntities);
//
//        flowVersionEntity.setActivityFlowNodes(assortmentGroupNodes);
//
//        nodeRouteEntity = new ActActivityFlowNodeRouteEntity();
//        nodeRouteEntity.setActivityVersion(flowVersionEntity);
//        nodeRouteEntity.setCurrentNode(node109);
//        nodeRouteEntity.setNextNode(node110);
//        nodeRouteEntity.setIsArchive(false);
//        nodeRouteEntity.setLastModifiedBy(userId);
//        nodeRouteEntity.setLastModifiedOn(new Date());
//        nodeRouteEntity.setNodeStatus(ActSystemConstantUtil.STATUS.ACTIVE);
//    }
//
//    @After
//    public void tearDown() {
//    }
//
//    /**
//     * Test of saveService method, of class ActActivityFlowService.
//     */
//    @Test
//    public void testSaveAllServices() {
//        System.out.println("saveAllServices");
//        boolean expResult = true;
//        boolean result = activityFlowService.saveAllServices(serviceEntities);
//        Assert.assertEquals(expResult, result);
//    }
//
//    /**
//     * Test of saveServiceAssociation method, of class ActActivityFlowService.
//     */
//    @Test
//    public void testSaveServiceAssociation() {
//        System.out.println("saveServiceAssociation");
//        boolean expResult = true;
//        boolean result = activityFlowService.saveServiceAssociation(activityFlowService.retrieveServiceByCode("101", companyId), associatedServiceEntities);
//        Assert.assertEquals(expResult, result);
//    }
//
//    /**
//     * Test of retrieveAssociatedServices method, of class
//     * ActActivityFlowService.
//     */
//    @Test
//    public void testRetrieveAssociatedServices() {
//        System.out.println("retrieveAssociatedServices");
//        List<ActServiceAssociationEntity> serviceAssociationEntities = activityFlowService.retrieveAssociatedServices(activityFlowService.retrieveServiceByCode("101", companyId).getId());
//        Assert.assertNotNull(serviceAssociationEntities);
//    }
//
//    /**
//     * Test of saveService method, of class ActActivityFlowServiceImpl.
//     */
//    @Test
//    public void testSaveService() {
//        System.out.println("saveService");
//        boolean expResult = true;
//        boolean result = activityFlowService.saveService(serviceEntities.get(0));
//        Assert.assertEquals(expResult, result);
//    }
//
//    /**
//     * Test of removeService method, of class ActActivityFlowServiceImpl.
//     */
//    @Test
//    public void testRemoveService() {
//        System.out.println("removeService");
//        ActServiceEntity serviceEntity = serviceEntities.get(0);
//        boolean expResult = true;
//        boolean result = activityFlowService.saveService(serviceEntity);
//        Assert.assertEquals(expResult, result);
//
//        result = activityFlowService.removeService(serviceEntity.getId());
//        Assert.assertEquals(expResult, result);
//    }
//
//    /**
//     * Test of retrieveAllServices method, of class ActActivityFlowServiceImpl.
//     */
//    @Test
//    public void testRetrieveAllServices() {
//        System.out.println("retrieveAllServices");
//        boolean expResult = true;
//        boolean result = activityFlowService.saveAllServices(serviceEntities);
//        Assert.assertEquals(expResult, result);
//
//        List<ActServiceEntity> services = activityFlowService.retrieveAllServices(companyId, ActSystemConstantUtil.SERVICE_TYPE.DYNAMIC);
//        Assert.assertNotNull(services);
//    }
//
//    /**
//     * Test of retrieveServiceByCode method, of class
//     * ActActivityFlowServiceImpl.
//     */
//    @Test
//    public void testRetrieveServiceByCode() {
//        System.out.println("retrieveServiceByCode");
//        ActServiceEntity serviceEntity = serviceEntities.get(0);
//        boolean expResult = true;
//        boolean result = activityFlowService.saveService(serviceEntity);
//        Assert.assertEquals(expResult, result);
//
//        ActServiceEntity serviceEntityTemp = activityFlowService.retrieveServiceByCode(serviceEntity.getServiceCode(), companyId);
//        Assert.assertEquals(serviceEntity.getServiceName(), serviceEntityTemp.getServiceName());
//    }
//
//    /**
//     * Test of saveActivityFlow method, of class ActActivityFlowServiceImpl.
//     */
//    @Test
//    public void testSaveActivityFlow() {
//        System.out.println("saveActivityFlow");
//        boolean expResult = true;
//        boolean result = activityFlowService.saveActivityFlow(activityFlowEntity);
//        Assert.assertEquals(expResult, result);
//    }
//
//    /**
//     * Test of initActivityFlow method, of class ActActivityFlowServiceImpl.
//     *
//     * @throws java.lang.Exception
//     */
//    @Test
//    public void testInitActivityFlow() throws Exception {
//        System.out.println("initActivityFlow");
//        activityFlowEntity = activityFlowService.initActivityFlow("HKG Diamonds", companyId, 1l);
//        Assert.assertNotNull(activityFlowEntity);
//        Assert.assertNotNull(activityFlowEntity.getId());
//        Assert.assertNotNull(activityFlowEntity.getCurrentVersion());
//        Assert.assertNotNull(activityFlowEntity.getCurrentVersion().getId());
//    }
//
//    /**
//     * Test of retrieveActivityFlowByCompany method, of class
//     * ActActivityFlowServiceImpl.
//     *
//     * @throws java.lang.Exception
//     */
//    @Test
//    public void testRetrieveActivityFlowByCompany() throws Exception {
//        System.out.println("retrieveActivityFlowByCompany");
//        activityFlowEntity = activityFlowService.initActivityFlow("HKG Diamonds", companyId, 1l);
//        Assert.assertNotNull(activityFlowEntity);
//        List<ActActivityFlowEntity> activityFlowEntities = activityFlowService.retrieveActivityFlowByCompany(companyId, null, true);
//        Assert.assertTrue(activityFlowEntities.size() > 0);
//    }
//
//    /**
//     * Test of doesActivityFlowExistForCompany method, of class
//     * ActActivityFlowServiceImpl.
//     *
//     * @throws java.lang.Exception
//     */
//    @Test
//    public void testDoesActivityFlowExistForCompany() throws Exception {
//        System.out.println("doesActivityFlowExistForCompany");
//        boolean result = activityFlowService.doesActivityFlowExistForCompany(companyId);
//        Assert.assertFalse(result);
//        activityFlowEntity = activityFlowService.initActivityFlow("HKG Diamonds", companyId, 1l);
//        Assert.assertNotNull(activityFlowEntity);
//        result = activityFlowService.doesActivityFlowExistForCompany(companyId);
//        Assert.assertTrue(result);
//    }
//
//    @Test
//    public void testRetrieveActivityFlowVersion() throws ActivityCoreException {
//        System.out.println("RetrieveActivityFlowVersion");
//        boolean result;
//        result = activityFlowService.saveActivityFlow(activityFlowEntity);
//        Assert.assertTrue(result);
//        result = activityFlowService.saveActivityFlowVersion(flowVersionEntity);
//        Assert.assertTrue(result);
//        ActActivityFlowVersionEntity activityFlowVersionEntity = activityFlowService.retrieveActivityFlowVersion(flowVersionEntity.getId(), true);
//        assertNotNull("Flow Version retrieved ", activityFlowVersionEntity);
//    }
//
//    @Test
//    public void testSaveActivityFlowVersion() throws ActivityCoreException {
//        System.out.println("SaveActivityFlowVersion");
//        boolean result = false;
//        result = activityFlowService.saveActivityFlow(activityFlowEntity);
//        Assert.assertTrue(result);
//        result = activityFlowService.saveActivityFlowVersion(flowVersionEntity);
//        Assert.assertTrue(result);
//    }
//
//    @Test
//    public void testSaveActivityNode() throws ActivityCoreException {
//        System.out.println("SaveActivityNode");
//        boolean result = false;
//        activityFlowEntity = activityFlowService.initActivityFlow("HKG Diamonds", companyId, 1l);
//        Assert.assertNotNull(activityFlowEntity);
//        ActActivityFlowGroupEntity addActivityFlowGroup = activityFlowService.addActivityFlowGroup("Sakht Testing..", 1d, 40d, null, 1l, activityFlowEntity.getCurrentVersion().getId());
//        Assert.assertNotNull(addActivityFlowGroup);
//        ActActivityFlowNodeEntity node110 = new ActActivityFlowNodeEntity();
//        node110.setActivityGroup(addActivityFlowGroup);
//        node110.setActivityVersion(addActivityFlowGroup.getActivityVersion());
//        node110.setAssociatedService(activityFlowService.retrieveServiceByCode("110", companyId));
//        node110.setIsArchive(false);
//        node110.setIsRoot(false);
//        node110.setLastModifiedBy(1l);
//        node110.setLastModifiedOn(currentDate);
//        result = activityFlowService.saveActivityNode(node110);
//        Assert.assertTrue(result);
//    }
//
//    @Test
//    public void testSaveActivityGroup() throws ActivityCoreException {
//        System.out.println("SaveActivityGroup");
//        boolean result = false;
//        result = activityFlowService.saveActivityFlow(activityFlowEntity);
//        Assert.assertTrue(result);
//        result = activityFlowService.saveActivityFlowVersion(flowVersionEntity);
//        Assert.assertTrue(result);
//        Long userId = 1l;
//        ActActivityFlowGroupEntity planningGroupEntity = new ActActivityFlowGroupEntity();
//        planningGroupEntity.setActivityVersion(flowVersionEntity);
//        planningGroupEntity.setGroupName("Planning");
//        planningGroupEntity.setDescription("Activity for Planning");
//        planningGroupEntity.setIsArchive(false);
//        planningGroupEntity.setLastModifiedBy(userId);
//        planningGroupEntity.setLastModifiedOn(currentDate);
//        planningGroupEntity.setStatus(ActSystemConstantUtil.STATUS.ACTIVE);
//        result = activityFlowService.saveActivityGroup(planningGroupEntity);
//        Assert.assertTrue(result);
//    }
//
//    @Test
//    public void testPublishActivityFlowVersion() throws ActivityCoreException {
//        System.out.println("PublishActivityFlowVersion");
//        boolean result = false;
//        result = activityFlowService.saveActivityFlow(activityFlowEntity);
//        Assert.assertTrue(result);
//        result = activityFlowService.saveActivityFlowVersion(flowVersionEntity);
//        Assert.assertTrue(result);
//        Long userId = 1l;
//        ActActivityFlowGroupEntity planningGroupEntity = new ActActivityFlowGroupEntity();
//        planningGroupEntity.setActivityVersion(flowVersionEntity);
//        planningGroupEntity.setGroupName("Planning");
//        planningGroupEntity.setDescription("Activity for Planning");
//        planningGroupEntity.setIsArchive(false);
//        planningGroupEntity.setLastModifiedBy(userId);
//        planningGroupEntity.setLastModifiedOn(currentDate);
//        planningGroupEntity.setStatus(ActSystemConstantUtil.STATUS.ACTIVE);
//        result = activityFlowService.saveActivityGroup(planningGroupEntity);
//        Assert.assertTrue(result);
//        result = activityFlowService.publishActivityFlowVersion(activityFlowEntity.getCurrentVersion().getId(), 1l);
//        Assert.assertTrue(result);
//    }
//
//    @Test
//    public void testSaveActivityNodeRoute() throws ActivityCoreException {
//        System.out.println("SaveActivityNodeRoute");
//        boolean result = false;
//        result = activityFlowService.saveActivityFlow(activityFlowEntity);
//        Assert.assertTrue(result);
//        result = activityFlowService.saveActivityFlowVersion(flowVersionEntity);
//        Assert.assertTrue(result);
//        ActActivityFlowGroupEntity planningGroupEntity = new ActActivityFlowGroupEntity();
//        planningGroupEntity.setActivityVersion(flowVersionEntity);
//        planningGroupEntity.setGroupName("Planning");
//        planningGroupEntity.setDescription("Activity for Planning");
//        planningGroupEntity.setIsArchive(false);
//        planningGroupEntity.setLastModifiedBy(1L);
//        planningGroupEntity.setLastModifiedOn(currentDate);
//        planningGroupEntity.setStatus(ActSystemConstantUtil.STATUS.ACTIVE);
//        result = activityFlowService.saveActivityFlowGroup(planningGroupEntity);
//        Assert.assertTrue(result);
//        ActActivityFlowNodeEntity node110 = new ActActivityFlowNodeEntity();
//        node110.setActivityGroup(planningGroupEntity);
//        node110.setActivityVersion(flowVersionEntity);
//        node110.setAssociatedService(activityFlowService.retrieveServiceByCode("110", companyId));
//        node110.setIsArchive(false);
//        node110.setIsRoot(false);
//        node110.setLastModifiedBy(1L);
//        node110.setLastModifiedOn(currentDate);
//        result = activityFlowService.saveActivityNode(node110);
//        Assert.assertTrue(result);
//        ActActivityFlowNodeEntity node109 = new ActActivityFlowNodeEntity();
//        node109.setActivityGroup(planningGroupEntity);
//        node109.setActivityVersion(flowVersionEntity);
//        node109.setAssociatedService(activityFlowService.retrieveServiceByCode("110", companyId));
//        node109.setIsArchive(false);
//        node109.setIsRoot(false);
//        node109.setLastModifiedBy(1L);
//        node109.setLastModifiedOn(currentDate);
//        result = activityFlowService.saveActivityNode(node110);
//        Assert.assertTrue(result);
//        result = activityFlowService.saveActivityFlowNodeRoute(nodeRouteEntity);
//        Assert.assertTrue(result);
//    }
//
//    @Test
//    public void testDeleteActivityGroup() throws ActivityCoreException {
//        System.out.println("DeleteActivityGroup");
//        boolean result = false;
//        result = activityFlowService.saveActivityFlow(activityFlowEntity);
//        Assert.assertTrue(result);
//        result = activityFlowService.saveActivityFlowVersion(flowVersionEntity);
//        Assert.assertTrue(result);
//        Long userId = 1l;
//        ActActivityFlowGroupEntity planningGroupEntity = new ActActivityFlowGroupEntity();
//        planningGroupEntity.setActivityVersion(flowVersionEntity);
//        planningGroupEntity.setGroupName("Planning");
//        planningGroupEntity.setDescription("Activity for Planning");
//        planningGroupEntity.setIsArchive(false);
//        planningGroupEntity.setLastModifiedBy(userId);
//        planningGroupEntity.setLastModifiedOn(currentDate);
//        planningGroupEntity.setStatus(ActSystemConstantUtil.STATUS.ACTIVE);
//        result = activityFlowService.saveActivityGroup(planningGroupEntity);
//        Assert.assertTrue(result);
//        result = activityFlowService.deleteActivityGroup(planningGroupEntity.getId(), userId);
//        Assert.assertTrue(result);
//    }
//
//    @Test
//    public void testDeleteActivityFlowNode() throws ActivityCoreException {
//        System.out.println("DeleteActivityFlowNode");
//        boolean result = false;
//        result = activityFlowService.saveActivityFlow(activityFlowEntity);
//        Assert.assertTrue(result);
//        result = activityFlowService.saveActivityFlowVersion(flowVersionEntity);
//        Assert.assertTrue(result);
//        Long userId = 1l;
//        ActActivityFlowGroupEntity planningGroupEntity = new ActActivityFlowGroupEntity();
//        planningGroupEntity.setActivityVersion(flowVersionEntity);
//        planningGroupEntity.setGroupName("Planning");
//        planningGroupEntity.setDescription("Activity for Planning");
//        planningGroupEntity.setIsArchive(false);
//        planningGroupEntity.setLastModifiedBy(userId);
//        planningGroupEntity.setLastModifiedOn(currentDate);
//        planningGroupEntity.setStatus(ActSystemConstantUtil.STATUS.ACTIVE);
//        result = activityFlowService.saveActivityFlowGroup(planningGroupEntity);
//        Assert.assertTrue(result);
//        ActActivityFlowNodeEntity node110 = new ActActivityFlowNodeEntity();
//        node110.setActivityGroup(planningGroupEntity);
//        node110.setActivityVersion(flowVersionEntity);
//        node110.setAssociatedService(activityFlowService.retrieveServiceByCode("110", companyId));
//        node110.setIsArchive(false);
//        node110.setIsRoot(false);
//        node110.setLastModifiedBy(userId);
//        node110.setLastModifiedOn(currentDate);
//        result = activityFlowService.saveActivityNode(node110);
//        Assert.assertTrue(result);
//        result = activityFlowService.deleteActivityFlowNode(node110.getId(), userId);
//        Assert.assertTrue(result);
//    }
//
//    @Test
//    public void testDeleteActivityFlowNodeRoute() throws ActivityCoreException {
//        System.out.println("DeleteActivityFlowNodeRoute");
//        boolean result = false;
//        result = activityFlowService.saveActivityFlow(activityFlowEntity);
//        Assert.assertTrue(result);
//        result = activityFlowService.saveActivityFlowVersion(flowVersionEntity);
//        Assert.assertTrue(result);
//        ActActivityFlowGroupEntity planningGroupEntity = new ActActivityFlowGroupEntity();
//        planningGroupEntity.setActivityVersion(flowVersionEntity);
//        planningGroupEntity.setGroupName("Planning");
//        planningGroupEntity.setDescription("Activity for Planning");
//        planningGroupEntity.setIsArchive(false);
//        planningGroupEntity.setLastModifiedBy(1L);
//        planningGroupEntity.setLastModifiedOn(currentDate);
//        planningGroupEntity.setStatus(ActSystemConstantUtil.STATUS.ACTIVE);
//        result = activityFlowService.saveActivityFlowGroup(planningGroupEntity);
//        Assert.assertTrue(result);
//        ActActivityFlowNodeEntity node110 = new ActActivityFlowNodeEntity();
//        node110.setActivityGroup(planningGroupEntity);
//        node110.setActivityVersion(flowVersionEntity);
//        node110.setAssociatedService(activityFlowService.retrieveServiceByCode("110", companyId));
//        node110.setIsArchive(false);
//        node110.setIsRoot(false);
//        node110.setLastModifiedBy(1L);
//        node110.setLastModifiedOn(currentDate);
//        result = activityFlowService.saveActivityNode(node110);
//        Assert.assertTrue(result);
//        ActActivityFlowNodeEntity node109 = new ActActivityFlowNodeEntity();
//        node109.setActivityGroup(planningGroupEntity);
//        node109.setActivityVersion(flowVersionEntity);
//        node109.setAssociatedService(activityFlowService.retrieveServiceByCode("110", companyId));
//        node109.setIsArchive(false);
//        node109.setIsRoot(false);
//        node109.setLastModifiedBy(1L);
//        node109.setLastModifiedOn(currentDate);
//        result = activityFlowService.saveActivityNode(node110);
//        Assert.assertTrue(result);
//        result = activityFlowService.saveActivityFlowNodeRoute(nodeRouteEntity);
//        Assert.assertTrue(result);
//        result = activityFlowService.deleteActivityFlowNodeRoute(nodeRouteEntity.getId(), 1l);
//        Assert.assertTrue(result);
//    }
//
//    @Test
//    public void addActivityFlowGroup() throws ActivityCoreException {
//        Boolean result = false;
//        activityFlowEntity = activityFlowService.initActivityFlow("HKG Diamonds", companyId, 1l);
//        Assert.assertNotNull(activityFlowEntity);
//        ActActivityFlowGroupEntity addActivityFlowGroup = activityFlowService.addActivityFlowGroup("final Testing..", 1d, 40d, null, 1l, activityFlowEntity.getCurrentVersion().getId());
//        Assert.assertNotNull(addActivityFlowGroup);
//
//        ActActivityFlowNodeEntity node101 = new ActActivityFlowNodeEntity();
//        node101.setActivityGroup(addActivityFlowGroup);
//        node101.setActivityVersion(activityFlowEntity.getCurrentVersion());
//        node101.setAssociatedService(activityFlowService.retrieveServiceByCode("101", companyId));
//        node101.setIsArchive(false);
//        node101.setIsRoot(true);
//        node101.setLastModifiedBy(1l);
//        node101.setLastModifiedOn(currentDate);
//        result = activityFlowService.saveActivityNode(node101);
//        Assert.assertTrue(result);
//
//        ActActivityFlowNodeEntity node102 = new ActActivityFlowNodeEntity();
//        node102.setActivityGroup(addActivityFlowGroup);
//        node102.setActivityVersion(activityFlowEntity.getCurrentVersion());
//        node102.setAssociatedService(activityFlowService.retrieveServiceByCode("102", companyId));
//        node102.setIsArchive(false);
//        node102.setIsRoot(true);
//        node102.setLastModifiedBy(1l);
//        node102.setLastModifiedOn(currentDate);
//        result = activityFlowService.saveActivityNode(node102);
//        Assert.assertTrue(result);
//        // node102.setNextService(activityFlowService.retrieveServiceByCode("103", companyId));
//
//        ActActivityFlowNodeEntity node103 = new ActActivityFlowNodeEntity();
//        node103.setActivityGroup(addActivityFlowGroup);
//        node103.setActivityVersion(activityFlowEntity.getCurrentVersion());
//        node103.setAssociatedService(activityFlowService.retrieveServiceByCode("103", companyId));
//        node103.setIsArchive(false);
//        node103.setIsRoot(false);
//        node103.setLastModifiedBy(1l);
//        node103.setLastModifiedOn(currentDate);
//        result = activityFlowService.saveActivityNode(node103);
//        Assert.assertTrue(result);
//    }
//
//    @Test
//    public void addActivityFlowVersion() throws ActivityCoreException {
//        activityFlowEntity = activityFlowService.initActivityFlow("HKG Diamonds", companyId, 1l);
//        Assert.assertNotNull(activityFlowEntity);
//    }
//
//    @Test
//    public void retireActivityFlowVersion() throws ActivityCoreException {
//        boolean result = false;
//        this.testSaveActivityNodeRoute();
//        ActActivityFlowVersionEntity flowVersion = new ActActivityFlowVersionEntity();
//        flowVersion.setActivity(activityFlowEntity);
//        flowVersion.setCompany(companyId);
//        flowVersion.setCreatedBy(1l);
//        flowVersion.setCreatedOn(currentDate);
//        flowVersion.setIsArchive(false);
//        flowVersion.setLastModifiedBy(1l);
//        flowVersion.setLastModifiedOn(currentDate);
//        flowVersion.setStatus(ActSystemConstantUtil.STATUS.PENDING);
//        flowVersion.setVersionCode(new Float(0.2));
//
//        ActActivityFlowGroupEntity planningGroupEntity = new ActActivityFlowGroupEntity();
//        planningGroupEntity.setActivityVersion(flowVersion);
//        planningGroupEntity.setGroupName("Planning");
//        planningGroupEntity.setDescription("Activity for Planning");
//        planningGroupEntity.setIsArchive(false);
//        planningGroupEntity.setLastModifiedBy(1L);
//        planningGroupEntity.setX(516d);
//        planningGroupEntity.setY(155d);
//        planningGroupEntity.setLastModifiedOn(currentDate);
//        planningGroupEntity.setStatus(ActSystemConstantUtil.STATUS.ACTIVE);
//        Set<ActActivityFlowGroupEntity> groupSet = new HashSet<>();
//        groupSet.add(planningGroupEntity);
//        flowVersion.setActivityFlowGroups(groupSet);
//        Set<ActActivityFlowNodeEntity> nodeSet = new HashSet<>();
//        ActActivityFlowNodeEntity node110 = new ActActivityFlowNodeEntity();
//        node110.setActivityGroup(planningGroupEntity);
//        node110.setActivityVersion(flowVersion);
//        node110.setAssociatedService(activityFlowService.retrieveServiceByCode("110", companyId));
//        node110.setIsArchive(false);
//        node110.setIsRoot(false);
//        node110.setX(516d);
//        node110.setY(155d);
//        node110.setLastModifiedBy(1L);
//        node110.setLastModifiedOn(currentDate);
//        nodeSet.add(node110);
//        ActActivityFlowNodeEntity node109 = new ActActivityFlowNodeEntity();
//        node109.setActivityGroup(planningGroupEntity);
//        node109.setActivityVersion(flowVersion);
//        node109.setAssociatedService(activityFlowService.retrieveServiceByCode("110", companyId));
//        node109.setIsArchive(false);
//        node109.setIsRoot(false);
//        node109.setX(516d);
//        node109.setY(155d);
//        node109.setLastModifiedBy(1L);
//        node109.setLastModifiedOn(currentDate);
//        nodeSet.add(node109);
//        flowVersion.setActivityFlowNodes(nodeSet);
//        Set<ActActivityFlowNodeRouteEntity> routeSet = new HashSet<>();
//        ActActivityFlowNodeRouteEntity nodeRoute = new ActActivityFlowNodeRouteEntity();
//        nodeRoute.setActivityVersion(flowVersion);
//        nodeRoute.setCurrentNode(node109);
//        nodeRoute.setNextNode(node110);
//        nodeRoute.setIsArchive(false);
//        nodeRoute.setLastModifiedBy(1l);
//        nodeRoute.setLastModifiedOn(new Date());
//        nodeRoute.setNodeStatus(ActSystemConstantUtil.STATUS.ACTIVE);
//        routeSet.add(nodeRoute);
//        flowVersion.setActivityFlowNodeRoutes(routeSet);
//        result = activityFlowService.saveActivityFlowVersion(flowVersion);
//        Assert.assertTrue(result);
//        activityFlowService.publishActivityFlowVersion(flowVersion.getId(), companyId);
//        result = activityFlowService.retireActivityFlowVersion(nodeRouteEntity.getId());
//        Assert.assertTrue(result);
//    }
//
//    @Test
//    public void retrieveServicesUsedByActivityFlowNodes() throws ActivityCoreException {
//        this.testSaveActivityNodeRoute();
//        List<String> versionStatuses = new ArrayList<>();
//        versionStatuses.add(ActSystemConstantUtil.STATUS.ACTIVE);
//        versionStatuses.add(ActSystemConstantUtil.STATUS.PENDING);
//        List<ActServiceEntity> serviceList = activityFlowService.retrieveServicesUsedByActivityFlowNodes(companyId, versionStatuses, null, null, null);
//        Assert.assertNotNull(serviceList);
//    }
//}
