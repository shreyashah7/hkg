/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.nosql.core.impl;

import com.argusoft.generic.core.config.CoreApplicationConfig;
import com.argusoft.generic.database.common.MongoGenericDao;
import com.argusoft.hkg.common.constantutil.HkSystemConstantUtil;
import com.argusoft.hkg.nosql.core.HkStockService;
import com.argusoft.hkg.nosql.model.HkLotDocument;
import com.argusoft.hkg.nosql.model.HkSellDocument;
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
 * @author shreya
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = {CoreApplicationConfig.class})
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@Transactional
public class HkSellServiceImplTest {

    @Autowired
    private HkStockService hkSellService;
    @Autowired
    private MongoGenericDao mongoGenericDao;
    HkSellDocument hkSellDocument;
    HkLotDocument lotDocument;

    public HkSellServiceImplTest() {
    }

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
        lotDocument.setFranchiseId(2l);
        lotDocument.setFieldValue(new BasicBSONObject("diamondColor", "Red"));
        lotDocument.setStatus(HkSystemConstantUtil.StockStatus.IN_PRODUCTION);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void sellLotTest() {
        BasicBSONObject bSONObject = new BasicBSONObject();
        bSONObject.put("lot sell", "shreya lot");
        mongoGenericDao.create(lotDocument);
        String sellLot = hkSellService.sellLot(bSONObject, 2l, 3l, lotDocument.getId(),null,null);
        System.out.println("sellLot :"+sellLot);
    }

}
