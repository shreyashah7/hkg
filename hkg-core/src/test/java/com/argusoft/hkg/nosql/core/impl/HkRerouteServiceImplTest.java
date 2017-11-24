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
import java.util.Date;
import javax.annotation.Resource;
import org.bson.BasicBSONObject;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
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
public class HkRerouteServiceImplTest {

    @Autowired
    private MongoGenericDao mongoGenericDao;
    @Autowired
    HkStockService hkRerouteService;
    HkLotDocument lotDocument;
    Date currentDate = new Date();
    Long companyId = 2l;
    Long userId = 3l;

    public HkRerouteServiceImplTest() {
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
        lotDocument.setFranchiseId(companyId);
        lotDocument.setFieldValue(new BasicBSONObject("diamondColor", "Red"));
        lotDocument.setStatus(HkSystemConstantUtil.StockStatus.TERMINATED);

    }

    @After
    public void tearDown() {
    }
}
