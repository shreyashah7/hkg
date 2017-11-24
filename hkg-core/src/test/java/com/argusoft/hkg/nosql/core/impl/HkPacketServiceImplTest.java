/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.nosql.core.impl;

import com.argusoft.generic.core.config.CoreApplicationConfig;
import com.argusoft.hkg.nosql.core.HkStockService;
import com.argusoft.hkg.nosql.model.HkPacketDocument;
import org.bson.BasicBSONObject;
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
public class HkPacketServiceImplTest {

    @Autowired
    private HkStockService instance;

    HkPacketDocument packetDocument;

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        packetDocument = new HkPacketDocument();
        packetDocument.setIsArchive(Boolean.FALSE);
        packetDocument.setFranchiseId(01l);      
        packetDocument.setFieldValue(new BasicBSONObject("name", "Dhwani"));
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of saveOrUpdate method, of class HkPacketServiceImpl.
     */
    @Test
    public void testSaveOrUpdatePacket() {
        System.out.println("save packet");
//        BigInteger id = instance.saveOrUpdatePacket(packetDocument);
//        assertNotNull("Not created coated rough", id);
    }

    /**
     * Test of retrievePacketById method, of class HkPacketServiceImpl.
     */
    @Test
    public void testRetrievePacketById() {
//        BigInteger id = instance.saveOrUpdatePacket(packetDocument);
//        if (id != null) {
//            HkPacketDocument tempCoatedRoughDocument = instance.retrievePacketById(id);
//            assertNotNull("can not retrieve coated rough", tempCoatedRoughDocument);
//        } else {
//            System.out.println("Can not create in retrieve");
//        }
    }

    /**
     * Test of retrieveAllPacket method, of class HkPacketServiceImpl.
     */
    @Test
    public void testRetrieveAllPacket() {
//        BigInteger id = instance.saveOrUpdatePacket(packetDocument);
//        if (id != null) {
//            List<HkPacketDocument> tempCoatedRoughDocument = instance.retrieveAllPacket(01l, false);
//            assertNotNull("can not retrieve coated rough", tempCoatedRoughDocument);
//        } else {
//            System.out.println("Can not create in retrieve");
//        }
    }

    /**
     * Test of deletePacket method, of class HkPacketServiceImpl.
     */
    @Test
    public void testDeletePacket() {
//        BigInteger id = instance.saveOrUpdatePacket(packetDocument);
//        if (id != null) {
//            instance.deletePacket(id);
//        } else {
//            System.out.println("Can not create in retrieve");
//        }
    }

    /**
     * Test of retrievePackets method, of class HkPacketServiceImpl.
     */
    @Test
    public void testRetrievePackets() {
//        Map<String, Object> fieldValues = new HashMap<>();
//        fieldValues.put("name", "Dhwani");
//        List<BigInteger> invoiceIds = new LinkedList<>();
//        invoiceIds.add(BigInteger.ZERO);
//        BigInteger saveOrUpdatePacket = instance.saveOrUpdatePacket(packetDocument);
//        if (saveOrUpdatePacket != null) {
//            List<HkPacketDocument> retrievePackets = instance.retrievePackets(fieldValues, invoiceIds, invoiceIds, invoiceIds, 01l, Boolean.FALSE);
//            assertNotNull("not null", retrievePackets);
//        }
    }

    /**
     * Test of retrievePacketIds method, of class HkPacketServiceImpl.
     */
    @Test
    public void testRetrievePacketIds() {
//        Map<String, Object> fieldValues = new HashMap<>();
//        fieldValues.put("name", "Dhwani");
//        List<BigInteger> invoiceIds = new LinkedList<>();
//        invoiceIds.add(BigInteger.ZERO);
//        BigInteger saveOrUpdateLot = instance.saveOrUpdatePacket(packetDocument);
//        if (saveOrUpdateLot != null) {
//            List<BigInteger> retrievePackets = instance.retrievePacketIds(fieldValues, invoiceIds, invoiceIds, invoiceIds, 01l, Boolean.FALSE);
//            assertNotNull("not null", retrievePackets);
//        }
    }

}
