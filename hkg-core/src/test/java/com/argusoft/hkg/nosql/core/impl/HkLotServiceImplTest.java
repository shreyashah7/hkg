/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.nosql.core.impl;

import com.argusoft.generic.core.config.CoreApplicationConfig;
import com.argusoft.hkg.nosql.core.HkStockService;
import com.argusoft.hkg.nosql.model.HkLotDocument;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.bson.BasicBSONObject;
import org.bson.types.ObjectId;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author dhwani
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = {CoreApplicationConfig.class})
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@Transactional
public class HkLotServiceImplTest {

    @Autowired
    private HkStockService instance;

    HkLotDocument lotDocument;

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        lotDocument = new HkLotDocument();
        lotDocument.setIsArchive(Boolean.FALSE);
        lotDocument.setFranchiseId(9l);
        lotDocument.setFieldValue(new BasicBSONObject("name", "Dhwani"));
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of saveOrUpdateLot method, of class HkLotServiceImpl.
     */
//    @Test
    public void testSaveOrUpdateLot() {
//        Map<String, Object> invoiceCustomMap = new HashMap<>();
//        Map<String, String> invoiceDbTypeMap = new HashMap<>();
//        invoiceCustomMap.put("name", "dhwani");
//        invoiceDbTypeMap.put("name", "String");
//        String invoiceId = invoiceService.saveInvoice(invoiceCustomMap, invoiceDbTypeMap, 1l, 1l);
//        String saveOrUpdateParcel = instance.createLot(invoiceCustomMap, invoiceDbTypeMap, 1l, 1l, new ObjectId(invoiceId), null);
//        System.out.println("");
    }

    /**
     * Test of retrieveLotById method, of class HkLotServiceImpl.
     */
    @Test
    public void testRetrieveLotById() {
        System.out.println("In test");
//        ObjectId id = instance.saveOrUpdateLot(lotDocument);
//        if (id != null) {
//            HkLotDocument tempCoatedRoughDocument = instance.retrieveLotById(id);
//            assertNotNull("can not retrieve coated rough", tempCoatedRoughDocument);
//        } else {
//            System.out.println("Can not create in retrieve");
//        }
    }

    /**
     * Test of retrieveAllLot method, of class HkLotServiceImpl.
     */
    @Test
    public void testRetrieveAllLot() {
//        ObjectId id = instance.saveOrUpdateLot(lotDocument);
//        if (id != null) {
//            List<HkLotDocument> tempCoatedRoughDocument = instance.retrieveAllLot(01l, false);
//            assertNotNull("can not retrieve coated rough", tempCoatedRoughDocument);
//        } else {
//            System.out.println("Can not create in retrieve");
//        }
    }

    /**
     * Test of deleteLot method, of class HkLotServiceImpl.
     */
    @Test
    public void testDeleteLot() {
//        ObjectId id = instance.saveOrUpdateLot(lotDocument);
//        if (id != null) {
//            instance.deleteLot(id);
//        } else {
//            System.out.println("Can not create in retrieve");
//        }
    }

    @Test
    public void retrieveLots() {
//        Map<String, Object> fieldValues = new HashMap<>();
//        fieldValues.put("name", "Dhwani");
//        List<ObjectId> invoiceIds = new LinkedList<>();
//        invoiceIds.add(ObjectId.ZERO);
//        ObjectId saveOrUpdateLot = instance.saveOrUpdateLot(lotDocument);
//        if (saveOrUpdateLot != null) {
//            List<HkLotDocument> retrieveLots = instance.retrieveLots(fieldValues, invoiceIds, invoiceIds, 01l, Boolean.FALSE);
//            assertNotNull("not null", retrieveLots);
//        }
    }

    @Test
    public void retrieveParcelIds() {
//        Map<String, Object> fieldValues = new HashMap<>();
//        fieldValues.put("name", "Dhwani");
//        List<ObjectId> invoiceIds = new LinkedList<>();
//        invoiceIds.add(ObjectId.ZERO);
//        ObjectId saveOrUpdateLot = instance.saveOrUpdateLot(lotDocument);
//        if (saveOrUpdateLot != null) {
//            List<ObjectId> retrieveLotIds = instance.retrieveLotIds(fieldValues, invoiceIds, invoiceIds, 01l, Boolean.FALSE);
//            assertNotNull("not null", retrieveLotIds);
//        }
    }
    
//    @Test
//    public void testMergeLot(){
//        Map<String, Object> invoiceCustomMap = new HashMap<>();
//        Map<String, String> invoiceDbTypeMap = new HashMap<>();
//        invoiceCustomMap.put("name", "dhwani");
//        invoiceDbTypeMap.put("name", "String");
//        String invoiceId = invoiceService.saveInvoice(invoiceCustomMap, invoiceDbTypeMap, 1l, 1l);
//        ObjectId lot1 = instance.saveLot(invoiceCustomMap, invoiceDbTypeMap, 1l, 1l, new ObjectId(invoiceId), null);
////        ObjectId lot2 = instance.saveLot(invoiceCustomMap, invoiceDbTypeMap, 1l, 1l, new ObjectId(invoiceId), null);
//        List<ObjectId> objectIds = new ArrayList<>();
////        objectIds.add(lot2);
//        objectIds.add(lot1);
//        ObjectId mergeLot = instance.mergeLot(lot1, objectIds, invoiceCustomMap, invoiceDbTypeMap, 1l, 1l);
//        System.out.println("Merged lot : "+mergeLot);
//    }
}
