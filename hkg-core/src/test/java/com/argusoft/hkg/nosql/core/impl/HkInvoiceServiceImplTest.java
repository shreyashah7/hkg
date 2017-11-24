/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.nosql.core.impl;

import com.argusoft.generic.core.config.CoreApplicationConfig;
import com.argusoft.hkg.nosql.core.HkStockService;
import com.argusoft.hkg.nosql.model.HkInvoiceDocument;
import java.util.HashMap;
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
public class HkInvoiceServiceImplTest {

    @Autowired
    HkStockService instance;

    HkInvoiceDocument invoiceDocument;

    public HkInvoiceServiceImplTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        invoiceDocument = new HkInvoiceDocument();
        invoiceDocument.setIsArchive(Boolean.FALSE);
        invoiceDocument.setFranchiseId(9l);
        BasicBSONObject bSONObject = new BasicBSONObject();
        bSONObject.put("name invoice", "Dhwani invoice");
        invoiceDocument.setFieldValue(bSONObject);
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of saveOrUpdateInvoice method, of class HkInvoiceServiceImpl.
     */
    @Test
    public void testSaveOrUpdateInvoice() {
//        Map<String, Object> invoiceCustomMap = new HashMap<>();
//        Map<String, String> invoiceDbTypeMap = new HashMap<>();
//        invoiceCustomMap.put("name", "dhwani");
//        invoiceDbTypeMap.put("name", "String");
//        ObjectId id = instance.saveInvoice(invoiceCustomMap, invoiceDbTypeMap, 1l, 1l);
//        assertNotNull("Not created coated rough", id);
    }

    /**
     * Test of retrieveInvoiceById method, of class HkInvoiceServiceImpl.
     */
    //@Test
    public void testRetrieveInvoiceById() {
//        Map<String, Object> invoiceCustomMap = new HashMap<>();
//        Map<String, String> invoiceDbTypeMap = new HashMap<>();
//        invoiceCustomMap.put("name", "dhwani");
//        invoiceDbTypeMap.put("name", "String");
//        String id = instance.saveInvoice(invoiceCustomMap, invoiceDbTypeMap, 1l, 1l);
//        if (id != null) {
//            HkInvoiceDocument tempCoatedRoughDocument = instance.retrieveInvoiceById(new ObjectId(id));
//            assertNotNull("can not retrieve coated rough", tempCoatedRoughDocument);
//        } else {
//            System.out.println("Can not create in retrieve");
//        }
    }

    /**
     * Test of retrieveAllInvoice method, of class HkInvoiceServiceImpl.
     */
    //@Test
    public void testRetrieveAllInvoice() {
//        Map<String, Object> invoiceCustomMap = new HashMap<>();
//        Map<String, String> invoiceDbTypeMap = new HashMap<>();
//        invoiceCustomMap.put("name", "dhwani");
//        invoiceDbTypeMap.put("name", "String");
//        String id = instance.saveInvoice(invoiceCustomMap, invoiceDbTypeMap, 1l, 1l);
//        if (id != null) {
//            List<HkInvoiceDocument> tempCoatedRoughDocument = instance.retrieveAllInvoice(01l, false);
//            assertNotNull("can not retrieve coated rough", tempCoatedRoughDocument);
//        } else {
//            System.out.println("Can not create in retrieve");
//        }
    }

    /**
     * Test of deleteInvoice method, of class HkInvoiceServiceImpl.
     */
    //@Test
    public void testDeleteInvoice() {
//        Map<String, Object> invoiceCustomMap = new HashMap<>();
//        Map<String, String> invoiceDbTypeMap = new HashMap<>();
//        invoiceCustomMap.put("name", "dhwani");
//        invoiceDbTypeMap.put("name", "String");
//        String id = instance.saveInvoice(invoiceCustomMap, invoiceDbTypeMap, 1l, 1l);
//        if (id != null) {
//            instance.deleteInvoice(new ObjectId(id));
//        } else {
//            System.out.println("Can not create in retrieve");
//        }
    }

    //@Test
    public void retrieveInvoices() {
//        Map<String, Object> invoiceCustomMap = new HashMap<>();
//        Map<String, String> invoiceDbTypeMap = new HashMap<>();
//        invoiceCustomMap.put("name", "dhwani");
//        invoiceDbTypeMap.put("name", "String");
//        String saveOrUpdateInvoice = instance.saveInvoice(invoiceCustomMap, invoiceDbTypeMap, 1l, 1l);
//        if (saveOrUpdateInvoice != null) {
//            Map<String, Object> fieldValues = new HashMap<>();
//            fieldValues.put("name", "dhwani");
//            List<HkInvoiceDocument> retrieveInvoices = instance.retrieveInvoices(fieldValues, 01l, Boolean.FALSE);
//            assertNotNull("not null", retrieveInvoices);
//        }
    }

    //@Test
    public void retrieveInvoiceIds() {
//        Map<String, Object> invoiceCustomMap = new HashMap<>();
//        Map<String, String> invoiceDbTypeMap = new HashMap<>();
//        invoiceCustomMap.put("name", "dhwani");
//        invoiceDbTypeMap.put("name", "String");
//        String saveOrUpdateInvoice = instance.saveInvoice(invoiceCustomMap, invoiceDbTypeMap, 1l, 1l);
//        if (saveOrUpdateInvoice != null) {
//            Map<String, Object> fieldValues = new HashMap<>();
//            fieldValues.put("name", "dhwani");
//            List<ObjectId> retrieveInvoiceIds = instance.retrieveInvoiceIds(fieldValues, 01l, Boolean.FALSE);
//            for (ObjectId bigInteger : retrieveInvoiceIds) {
//                System.out.println("Id is :: " + bigInteger);
//            }
//            assertNotNull("not null", retrieveInvoiceIds);
//        }
    }

}
