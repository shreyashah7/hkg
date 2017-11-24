/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.reportbuilder.core;

import com.argusoft.generic.core.config.CoreApplicationConfig;
import com.argusoft.reportbuilder.model.RbReport;
import com.argusoft.reportbuilder.model.RbTabularRelationEntity;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.apache.commons.dbcp.BasicDataSource;
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
 * @author vipul
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = {CoreApplicationConfig.class})
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@Transactional
public class RbReportServiceTest {

    @Autowired
    RbReportService rbReportService;
    RbReport rbReport;

    @Autowired
    BasicDataSource dataSource;

    public RbReportServiceTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        rbReport = new RbReport();

        rbReport.setCreatedBy(1);
        rbReport.setCreatedOn(new Date());
        rbReport.setDescription("Description");
        rbReport.setReportName("report");
        rbReport.setIsExternalReport(true);
        rbReport.setStatus("ac");
//        rbReport.setFranchise(1);
        rbReport.setIsActive(Boolean.TRUE);
        rbReport.setIsArchive(Boolean.FALSE);
        rbReport.setLastModifiedBy(1);
        rbReport.setLastModifiedOn(new Date());
//        rbReport.setRbReportAccessSet(rbReportAccessSet);
    }

    @Test
    public void createRbReportTest() {
        Long id = rbReportService.createReport(rbReport);
        Assert.assertTrue("RbReport Not created", id != null);
    }

    // @Test
    public void retriveRbReportByIdTest() {
        RbReport rbReport = rbReportService.retriveReportById(2l, null, null);
        System.out.println("Id=" + rbReport.getId());
    }

    // @Test
    public void updateRbReportTest() {
        RbReport rbReportUpadate = rbReportService.retriveReportById(2l, null, null);
        rbReportUpadate.setDescription("Updated Description");
        rbReportService.updateReport(rbReportUpadate);

    }

    // @Test
    public void retriveAllRbReport() {
        List<RbReport> rbReportsList = rbReportService.retriveAllReports(null,2l);
        for (RbReport rbRecord : rbReportsList) {
            System.out.println("Id All=" + rbRecord.getId());
        }
    }

    @Test
    public void retriveQueryMetadata() {
//          DataSource dataSource;
//          JdbcTemplate JdbcTemplate;
        System.out.println("Start");
        System.out.println("Driver Name=" + dataSource.getDriverClassName());
        System.out.println("Driver Name=" + dataSource.getUsername());
        System.out.println("End");
    }

      @Test
    public void retriveConvertedResultTest() {
//        String results = reportBuilderService.retriveConvertedResults(null);
//        System.out.println("results=" + results);
    }

     @Test
    public void retrieveAllTableWithColumnsList() {
        List<String> tableList = new ArrayList<>();
        tableList.add("rb_report_mst");
        Map<String, Object> results = rbReportService.retrieveAllTableWithColumnsListByTableList(tableList);
        for (Map.Entry<String, Object> entry : results.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            System.out.println(key + "=" + value.toString());

        }

    }

    /**
     * Test of retrieveRelationAmongTables method, of class RbReportServiceImpl.
     */
    @Test
    public void testRetrieveRelationAmongTables() {
        System.out.println("retrieveRelationAmongTables");
        List<RbTabularRelationEntity> result = rbReportService.retrieveRelationAmongTables(
                Arrays.asList("um_system_user", "um_user_contact", "um_company_info", "um_contact_education_dtl"), null);
        System.out.println("result is " + result);
    }
}
