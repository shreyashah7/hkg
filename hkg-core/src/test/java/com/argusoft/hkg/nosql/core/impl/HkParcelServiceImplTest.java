/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.nosql.core.impl;

import com.argusoft.generic.core.config.CoreApplicationConfig;
import com.argusoft.hkg.nosql.core.HkStockService;
import com.argusoft.hkg.nosql.model.HkParcelDocument;
import org.junit.After;
import org.junit.AfterClass;
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
public class HkParcelServiceImplTest {

    @Autowired
    private HkStockService instance;

    HkParcelDocument parcelDocument;

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of saveOrUpdateParcel method, of class HkParcelServiceImpl.
     */
    @Test
    public void testSaveOrUpdateParcel() {
//        Map<String, Object> invoiceCustomMap = new HashMap<>();
//        Map<String, String> invoiceDbTypeMap = new HashMap<>();
//        invoiceCustomMap.put("name", "dhwani");
//        invoiceDbTypeMap.put("name", "String");
//        String invoiceId = invoiceService.saveInvoice(invoiceCustomMap, invoiceDbTypeMap, 1l, 1l);
//        ObjectId saveOrUpdateParcel = instance.saveParcel(invoiceCustomMap, invoiceDbTypeMap, 1l, 1l, new ObjectId(invoiceId));
        System.out.println("Save update parcel " );
//        BigInteger id = instance.saveOrUpdateParcel(parcelDocument);
//        assertNotNull("Not created coated rough", saveOrUpdateParcel);
    }

    /**
     * Test of retrieveParcelById method, of class HkParcelServiceImpl.
     */
    //@Test
    public void testRetrieveParcelById() {
//        BigInteger id = instance.saveOrUpdateParcel(parcelDocument);
//        if (id != null) {
//            HkParcelDocument tempCoatedRoughDocument = instance.retrieveParcelById(id);
//            assertNotNull("can not retrieve coated rough", tempCoatedRoughDocument);
//        } else {
//            System.out.println("Can not create in retrieve");
//        }
    }

    /**
     * Test of retrieveAllParcel method, of class HkParcelServiceImpl.
     */
//    //@Test
    public void testRetrieveAllParcel() {
//        BigInteger id = instance.saveOrUpdateParcel(parcelDocument);
//        if (id != null) {
//            List<HkParcelDocument> tempCoatedRoughDocument = instance.retrieveAllParcel(01l, false);
//            assertNotNull("can not retrieve coated rough", tempCoatedRoughDocument);
//        } else {
//            System.out.println("Can not create in retrieve");
//        }
    }

    /**
     * Test of deleteParcel method, of class HkParcelServiceImpl.
     */
    //@Test
    public void testDeleteParcel() {
//        BigInteger id = instance.saveOrUpdateParcel(parcelDocument);
//        if (id != null) {
//            instance.deleteParcel(id);
//        } else {
//            System.out.println("Can not create in retrieve");
//        }
    }

    //@Test
    public void retrieveParcels() {
//        Map<String, Object> fieldValues = new HashMap<>();
//        fieldValues.put("name", "Dhwani");
//        List<BigInteger> invoiceIds = new LinkedList<>();
//        invoiceIds.add(BigInteger.ONE);
//        BigInteger saveOrUpdateParcel = instance.saveOrUpdateParcel(parcelDocument);
//        if (saveOrUpdateParcel != null) {
//            List<HkParcelDocument> retrieveParcels = instance.retrieveParcels(fieldValues, invoiceIds, 01l, Boolean.FALSE);
//            assertNotNull("not null", retrieveParcels);
//        }
    }

    //@Test
    public void retrieveParcelIds() {
//        Map<String, Object> fieldValues = new HashMap<>();
//        fieldValues.put("name", "Dhwani");
//        List<BigInteger> invoiceIds = new LinkedList<>();
//        invoiceIds.add(BigInteger.ONE);
//        BigInteger saveOrUpdateParcel = instance.saveOrUpdateParcel(parcelDocument);
//        if (saveOrUpdateParcel != null) {
//            List<BigInteger> retrieveParcelIds = instance.retrieveParcelIds(fieldValues, invoiceIds, 01l, Boolean.FALSE);
//            assertNotNull("not null", retrieveParcelIds);
//        }
    }

}
