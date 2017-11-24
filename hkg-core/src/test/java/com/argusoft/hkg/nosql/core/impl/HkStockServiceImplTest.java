/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.nosql.core.impl;

import com.argusoft.generic.core.config.CoreApplicationConfig;
import com.argusoft.generic.database.common.MongoGenericDao;
import com.argusoft.hkg.nosql.core.HkStockService;
import com.argusoft.hkg.nosql.model.HkPersonCapabilityDocument;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
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
 * @author shreya
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = {CoreApplicationConfig.class})
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@Transactional
public class HkStockServiceImplTest {

    @Autowired
    HkStockService hkStockService;
    @Autowired
    MongoGenericDao mongoGenericDao;
    HkPersonCapabilityDocument hkPersonCapabilityDocument;

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        hkPersonCapabilityDocument = new HkPersonCapabilityDocument();
        hkPersonCapabilityDocument.setCreatedOn(new Date());
        hkPersonCapabilityDocument.setForPerson(2l);
        hkPersonCapabilityDocument.setFranchise(2l);
        hkPersonCapabilityDocument.setPropertyName("color");
        hkPersonCapabilityDocument.setPropertyValue(7l);
        hkPersonCapabilityDocument.setTotalFinalPlans(1);
        hkPersonCapabilityDocument.setSucceededFinalPlans(1);
        hkPersonCapabilityDocument.setStatus("A");
    }

    @After
    public void tearDown() {
    }

    @Test
    public void updatePersonCapabilityForProperty() {
        mongoGenericDao.create(hkPersonCapabilityDocument);
        Map<String, Long> expectedPropertyValueMap = new HashMap<>();
        Map<String, Long> actualPropertyValueMap = new HashMap<>();
        expectedPropertyValueMap.put("color", 10l);
        expectedPropertyValueMap.put("carat", 7l);
        actualPropertyValueMap.put("color", 8l);
        actualPropertyValueMap.put("carat", 7l);
        hkStockService.updatePersonCapabilityForProperty(2l, expectedPropertyValueMap, actualPropertyValueMap, 2l);
    }

    @Test
    public void nextSequenceNoForSlip() {
//        Calendar cal = Calendar.getInstance();
//        Integer slipNo = hkStockService.nextSequenceNoForSlip(cal.getTime());
//        System.out.println("slipNo::::" + slipNo);
    }
}
