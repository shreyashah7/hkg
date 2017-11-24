/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.nosql.core.impl;

import com.argusoft.generic.core.config.CoreApplicationConfig;
import com.argusoft.hkg.nosql.core.HkCustomFieldService;
import com.argusoft.hkg.nosql.model.HkSubFormValueDocument;
import java.sql.Array;
import java.util.ArrayList;
import java.util.List;
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
import org.springframework.util.CollectionUtils;

/**
 *
 * @author dhwani
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = {CoreApplicationConfig.class})
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@Transactional
public class HkSubFormValueServiceImplTest {

    @Autowired
    private HkCustomFieldService instance;

    private HkSubFormValueDocument subFormValueDocument;

    public HkSubFormValueServiceImplTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        subFormValueDocument = new HkSubFormValueDocument();
        subFormValueDocument.setFranchiseId(1l);
        subFormValueDocument.setInstanceId(1l);
        subFormValueDocument.setIsArchive(Boolean.FALSE);
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of saveSubFormValues method, of class HkSubFormValueServiceImpl.
     */
//    @Test
    public void testSaveSubFormValues() {
        List<HkSubFormValueDocument> subFormValueDocuments = new ArrayList<>();
        subFormValueDocuments.add(subFormValueDocument);
        instance.saveSubFormValues(subFormValueDocuments);
    }

    /**
     * Test of retrieveSubFormValueByInstance method, of class
     * HkSubFormValueServiceImpl.
     */
    @Test
    public void testRetrieveSubFormValueByInstance() {
        List<HkSubFormValueDocument> subFormValueDocuments = new ArrayList<>();
        subFormValueDocuments.add(subFormValueDocument);
        instance.saveSubFormValues(subFormValueDocuments);
        List<HkSubFormValueDocument> listToUpdate = instance.retrieveSubFormValueByInstance(1l,false);
        assertNotNull("not retrieved", listToUpdate);
//        HkSubFormValueDocument subFormValueDocumentTemp = new HkSubFormValueDocument();
//        subFormValueDocumentTemp.setFranchiseId(1l);
//        subFormValueDocumentTemp.setInstanceId(1l);
//        subFormValueDocumentTemp.setIsArchive(Boolean.FALSE);
//        BasicBSONObject basicBSONObject = new BasicBSONObject("myNAme", "dhwani");
//        subFormValueDocumentTemp.setFieldValue(basicBSONObject);
//        List<HkSubFormValueDocument> newList = new ArrayList<>();
//        newList.add(subFormValueDocumentTemp);
//        instance.saveSubFormValues(newList, listToUpdate);
    }

    /**
     * Test of retrieveSubFormValueById method, of class
     * HkSubFormValueServiceImpl.
     */
//    @Test
    public void testRetrieveSubFormValueById() {
        List<HkSubFormValueDocument> subFormValueDocuments = new ArrayList<>();
        subFormValueDocuments.add(subFormValueDocument);
        instance.saveSubFormValues(subFormValueDocuments);
        List<HkSubFormValueDocument> subFormValueDocuments1 = instance.retrieveSubFormValueByInstance(1l,false);
        if (!CollectionUtils.isEmpty(subFormValueDocuments)) {
            ObjectId id = new ObjectId();
            for (HkSubFormValueDocument hkSubFormValueDocument : subFormValueDocuments1) {
                id = hkSubFormValueDocument.getId();
            }
            HkSubFormValueDocument hkSubFormValueDocument = instance.retrieveSubFormValueById(id);
            assertNotNull("cant retrieve", hkSubFormValueDocument);
        }
    }

}
