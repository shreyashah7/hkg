/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.reportbuilder.util;

import com.argusoft.generic.core.config.CoreApplicationConfig;
import com.argusoft.reportbuilder.common.constantutil.RbReportConstantUtils;
import com.argusoft.reportbuilder.model.RbReportField;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author mital
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = {CoreApplicationConfig.class})
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@Transactional
public class QueryStringUtilsTest {

    @Autowired
    private QueryStringUtils queryStringUtils;

    public QueryStringUtilsTest() {
    }

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
     * Test of validateSqlQuery method, of class QueryStringUtils.
     */
    @Test
    public void testValidateSqlQuery() {
        System.out.println("validateSqlQuery");
//        String query = "";
//        Boolean expResult = null;
//        Boolean result = QueryStringUtils.validateSqlQuery(query);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

    /**
     * Test of changeQueryByConfiguration method, of class QueryStringUtils.
     */
    @Test
    public void testChangeQueryByConfiguration() {
        System.out.println("changeQueryByConfiguration");
//        MasterReportDataBean masterReportDataBean = null;
//        String expResult = "";
//        String result = QueryStringUtils.changeQueryByConfiguration(masterReportDataBean);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

    /**
     * Test of retrieveSQLQuery method, of class QueryStringUtils.
     */
    @Test
    public void testRetrieveSQLQuery_Map_Map() {
        System.out.println("retrieveSQLQuery");
        Map<String, Map<String, Integer>> tableColumnsMap = new HashMap<>();
        Map<String, Integer> columnsMap = new HashMap<>();
        columnsMap.put("first_name", 3);
        columnsMap.put("middle_name", 4);
        columnsMap.put("last_name", 6);
        tableColumnsMap.put("first", columnsMap);
        columnsMap = new HashMap<>();
        columnsMap.put("first_name", 5);
        columnsMap.put("middle_name", 1);
        columnsMap.put("last_name", 2);
        tableColumnsMap.put("second", columnsMap);
        String resultQuery = queryStringUtils.retrieveSQLQuery(tableColumnsMap, null);
        assertNotNull(resultQuery);
    }

    /**
     * Test of retrieveSQLQuery method, of class QueryStringUtils.
     */
   // @Test
    public void testRetrieveSQLQuery_List() {
        System.out.println("retrieveSQLQuery");
        RbReportField field1 = new RbReportField(null, "username", "user_id", "um_system_user", RbReportConstantUtils.DBBaseType.RELATIONAL_DB,
                false, false, null, true, false, 1L, new Date(), "String");
        RbReportField field2 = new RbReportField(null, "First Name", "first_name", "um_user_contact", RbReportConstantUtils.DBBaseType.RELATIONAL_DB,
                false, false, null, true, false, 1L, new Date(), "String");
        field2.setJoinAttributes("{\"" + RbReportConstantUtils.JoinAttributeKey.FK_TABLE + "\" : \"um_system_user\","
                + "\"" + RbReportConstantUtils.JoinAttributeKey.FK_COLUMN + "\":\"id\","
                + "\"" + RbReportConstantUtils.JoinAttributeKey.LOCAL_COLUMN + "\":\"userobj\","
                + "\"" + RbReportConstantUtils.JoinAttributeKey.REL_TYPE + "\":\"11\"}");
        field2.setFilterAttributes("{\"=\":\"v\"}");
        RbReportField field3 = new RbReportField(null, "Degree", "degree", "um_contact_education_dtl", RbReportConstantUtils.DBBaseType.RELATIONAL_DB,
                false, false, null, true, false, 1L, new Date(), "String");
        field3.setJoinAttributes("{\"" + RbReportConstantUtils.JoinAttributeKey.FK_TABLE + "\" : \"um_user_contact\","
                + "\"" + RbReportConstantUtils.JoinAttributeKey.FK_COLUMN + "\":\"id\","
                + "\"" + RbReportConstantUtils.JoinAttributeKey.LOCAL_COLUMN + "\":\"user_contact\","
                + "\"" + RbReportConstantUtils.JoinAttributeKey.REL_TYPE + "\":\"1M\"}");
        String result = QueryStringUtils.retrieveSQLQuery(Arrays.asList(field1, field2, field3), null, null,null,null);
        assertNotNull(result);
    }

}
