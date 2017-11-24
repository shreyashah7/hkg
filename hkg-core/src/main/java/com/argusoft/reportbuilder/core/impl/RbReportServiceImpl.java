/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.reportbuilder.core.impl;

import com.argusoft.generic.database.common.CommonDAO;
import com.argusoft.generic.database.common.GenericDao;
import com.argusoft.generic.database.common.MongoGenericDao;
import com.argusoft.generic.database.exception.GenericDatabaseException;
import com.argusoft.generic.database.search.SearchFactory;
import com.argusoft.hkg.common.constantutil.HkSystemConstantUtil;
import com.argusoft.hkg.core.HkFieldService;
import com.argusoft.hkg.core.HkFoundationService;
import com.argusoft.hkg.model.HkValueEntity;
import com.argusoft.hkg.nosql.model.CustomField;
import com.argusoft.hkg.nosql.model.GenericDocument;
import com.argusoft.hkg.nosql.model.HkApplyLeaveDocument;
import com.argusoft.hkg.nosql.model.HkAssetDocument;
import com.argusoft.hkg.nosql.model.HkCategoryDocument;
import com.argusoft.hkg.nosql.model.HkDepartmentDocument;
import com.argusoft.hkg.nosql.model.HkEventDocument;
import com.argusoft.hkg.nosql.model.HkFranchiseDocument;
import com.argusoft.hkg.nosql.model.HkHolidayDocument;
import com.argusoft.hkg.nosql.model.HkLeaveWorkflowDocument;
import com.argusoft.hkg.nosql.model.HkShiftDocument;
import com.argusoft.hkg.nosql.model.HkTaskDocument;
import com.argusoft.hkg.nosql.model.HkUserDocument;
import com.argusoft.hkg.nosql.model.SectionDocument;
import com.argusoft.reportbuilder.common.constantutil.RbReportConstantUtils;
import com.argusoft.reportbuilder.core.RbReportService;
import com.argusoft.reportbuilder.core.bean.RbTabularRelationDataBean;
import com.argusoft.reportbuilder.database.EntityMetadataDao;
import com.argusoft.reportbuilder.database.RbReportDao;
import com.argusoft.reportbuilder.database.RbReportFieldDao;
import com.argusoft.reportbuilder.database.RbTabularRelationDao;
import com.argusoft.reportbuilder.model.RbEmailReportConfigurationEntity;
import com.argusoft.reportbuilder.model.RbEmailReportStatusEntity;
import com.argusoft.reportbuilder.model.RbReport;
import com.argusoft.reportbuilder.model.RbReportField;
import com.argusoft.reportbuilder.model.RbTabularRelationEntity;
import com.argusoft.reportbuilder.model.RbUserReportDashboardEntity;
import com.argusoft.usermanagement.common.core.UMFeatureService;
import com.argusoft.usermanagement.common.database.UMFeatureDao;
import com.argusoft.usermanagement.common.model.UMFeature;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.googlecode.genericdao.search.Filter;
import com.googlecode.genericdao.search.Search;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.dbcp.BasicDataSource;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.postgresql.jdbc4.Jdbc4Array;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 *
 * @author vipul
 */
@Service
@Transactional
@PropertySource({"classpath:jdbc.properties"})
public class RbReportServiceImpl implements RbReportService {

    @Autowired
    private CommonDAO commonDao;
    @Autowired
    RbReportDao rbReportDao;
    @Autowired
    EntityMetadataDao entityMetadataDao;
    @Autowired
    RbReportFieldDao rbReportFieldDao;
    @Autowired
    RbTabularRelationDao rbTabularRelationDao;
    @Autowired
    BasicDataSource dataSource;
    @Autowired
    private MongoGenericDao mongoGenericDao;
    @Autowired
    private Environment env;
    @Autowired
    private UMFeatureService featureService;
    @Autowired
    private HkFoundationService foundationService;
    @Autowired
    HkFieldService fieldService;

    public static class EmailReportConfigurationEntity {

        public static final String ID = "id";
        public static final String CREATED_BY = "createdBy";
        public static final String CREATED_ON = "createdOn";
        public static final String STATUS = "status";
        public static final String REPORT = "report";
        public static final String REPORT_ID = "report.id";
    }

    public static class EmailReportStatusEntity {

        public static final String ID = "id";
        public static final String CREATED_BY = "createdBy";
        public static final String CREATED_ON = "createdOn";
        public static final String STATUS = "status";
        public static final String EMAIL_SENT = "emailSent";
        public static final String EMAIL_REPORT_STATUS_ENTITY_PK = "rbEmailReportStatusEntityPK";
        public static final String EMAIL_REPORT_STATUS_ENTITY_PK_ON_TIME = "rbEmailReportStatusEntityPK.onTime";
        public static final String EMAIL_REPORT_STATUS_ENTITY_PK_FOR_USER = "rbEmailReportStatusEntityPK.forUser";
        public static final String EMAIL_REPORT_STATUS_ENTITY_PK_CONFIG_ID = "rbEmailReportStatusEntityPK.emailReportConfiguration";
    }

    public static class UserReportDashboardEntity {

        public static final String STATUS = "status";
        public static final String IS_ARCHIVE = "isArchive";
        public static final String USER_REPORT_DASHBOARD_STATUS_ENTITY_PK = "rbUserReportDashboardEntityPK";
        public static final String USER_REPORT_DASHBOARD_STATUS_ENTITY_PK_USER = "rbUserReportDashboardEntityPK.userId";
        public static final String USER_REPORT_DASHBOARD_STATUS_ENTITY_PK_REPORT = "rbUserReportDashboardEntityPK.reportId";
    }

    @Override
    public void createForeignTables(final Set<String> tableNameSet) {
        try {

            Set<String> nonEntityTables = new HashSet<>();
            nonEntityTables.add("subformvalue");
            nonEntityTables.add("issue");
            nonEntityTables.add("sell");
            nonEntityTables.add("transfer");
            nonEntityTables.add("ruleset");
            nonEntityTables.add("workallotmentaggregation");
            nonEntityTables.add("workallotment");
            nonEntityTables.add("usergoalstatus");
            nonEntityTables.add("personcapability");
            nonEntityTables.add("personcapability");
            nonEntityTables.add("fieldsequence");
            nonEntityTables.add("plan");
            if (!CollectionUtils.isEmpty(tableNameSet)) {
                int tableNameSetSize = tableNameSet.size();
                String tableNameString = "";
                for (String tableName : tableNameSet) {
                    if (tableName.equals(HkSystemConstantUtil.featureDocumentMap.get((HkSystemConstantUtil.Feature.MANAGE_EMPLOYEE)))) {
                        tableName = RbReportConstantUtils.USER_FOREIGN_TABLE;
                    }
                    tableNameString += "'" + tableName.toLowerCase() + "',";
                }
                tableNameString = tableNameString.substring(0, tableNameString.length() - 1);

                JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

                String tableExistQuery = "SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES \n"
                        + "WHERE TABLE_SCHEMA='public'\n"
                        + " AND TABLE_TYPE = 'FOREIGN TABLE'\n"
                        + " AND TABLE_NAME in (" + tableNameString + ")";
                jdbcTemplate.query(tableExistQuery, new RowMapper<ResultSet>() {

                    @Override
                    public ResultSet mapRow(ResultSet rs, int i) throws SQLException {
                        String tableName = rs.getString(1);
                        if (tableNameSet.contains(tableName)) {
                            tableNameSet.remove(tableName);
                        }
                        return rs;
                    }
                });

                //System.out.println("final list is " + tableNameSet);
                String mongoHost = env.getProperty("mongo.host");
                String mongoPort = env.getProperty("mongo.port");
                String mongoDBName = env.getProperty("mongo.dbname");
                String mongoUsername = env.getProperty("mongo.username");
                String mongoPassword = env.getProperty("mongo.password");
                String postgresUsername = env.getProperty("jdbc.username");

                if (!CollectionUtils.isEmpty(tableNameSet)) {
                    //  if table name set size is same as the new set size, that means there is possible chance that foreign tables
                    //  are being created first time so we'll create extension mongo_fdw
                    //  if table name set size is not same as the new set size, means foreign table do exist, so no need to create extension
//                    if (tableNameSetSize == tableNameSet.size()) {
                    //  Create extension mongo_fdw
                    String createExtensionQuery = "CREATE EXTENSION IF NOT EXISTS mongo_fdw;";
                    jdbcTemplate.execute(createExtensionQuery);

                    Integer countMongoServer = null;
                    try {
                        //  Check if hkg_mongo_server exists
                        String checkMongoServerQuery = "select count(foreign_server_name) from information_Schema.foreign_servers"
                                + " where foreign_server_name = 'hkg_mongo_server'";
                        countMongoServer = jdbcTemplate.queryForObject(checkMongoServerQuery, Integer.class);
                    } catch (org.springframework.dao.EmptyResultDataAccessException e) {
                    }

                    if (countMongoServer == null || countMongoServer < 1) {
                        //  Create hkg_mongo_server if it does not exist
                        String mongoServerQuery = "CREATE SERVER hkg_mongo_server"
                                + " FOREIGN DATA WRAPPER mongo_fdw"
                                + " OPTIONS (address '" + mongoHost + "', port '" + mongoPort + "');";
                        jdbcTemplate.execute(mongoServerQuery);

                        //  Create user mapping postgres user
                        String userMappingQuery = "CREATE USER MAPPING FOR " + postgresUsername
                                + " SERVER hkg_mongo_server";
                        if (!StringUtils.isEmpty(mongoUsername)) {
                            userMappingQuery += " OPTIONS (username '" + mongoUsername + "'";
                            if (!StringUtils.isEmpty(mongoPassword)) {
                                userMappingQuery += ", password '" + mongoPassword + "');";
                            } else {
                                userMappingQuery += ");";
                            }
                        }
                        jdbcTemplate.execute(userMappingQuery);
                    }
//                    }

                    //System.out.println("table name set is " + tableNameSet);
                    for (String tableName : tableNameSet) {
                        if (!nonEntityTables.contains(tableNameSet)) {
                            if (tableName.equals("user")) {
                                tableName = RbReportConstantUtils.USER_FOREIGN_TABLE;
                            }
                            String createTableQuery = "CREATE FOREIGN TABLE IF NOT EXISTS " + tableName
                                    + "   (_id name ,"
                                    + "    \"featureName\" text ,"
                                    + "    \"instanceId\" bigint ,";

                            //Add fixed fields if necessary.
                            if (RbReportConstantUtils.FIXED_FORIEGN_TABLE_ATTRIBUTES.containsKey(tableName)) {
                                Map<String, String> fixedAttributeMap = RbReportConstantUtils.FIXED_FORIEGN_TABLE_ATTRIBUTES.get(tableName);
                                if (!CollectionUtils.isEmpty(fixedAttributeMap)) {
                                    for (Map.Entry<String, String> entry : fixedAttributeMap.entrySet()) {
                                        createTableQuery += "  \"" + entry.getKey() + "\"  " + entry.getValue() + " ,";
                                    }
                                }
                            }
                            createTableQuery += "    \"fieldValue\" json ,"
                                    + "    \"franchiseId\" bigint ,"
                                    + "    \"sectionList\" json )"
                                    + "   SERVER hkg_mongo_server"
                                    + "   OPTIONS (database '" + mongoDBName + "', collection '" + tableName + "');";
                            jdbcTemplate.execute(createTableQuery);
                        }

                    }
                    //Foreign table for subentity.
                    String subEntityTableQuery = "CREATE FOREIGN TABLE IF NOT EXISTS " + "subformvalue"
                            + "   (_id name ,"
                            + "    \"featureName\" text ,"
                            + "    \"instanceId\" bigint ,"
                            + "    \"fieldValue\" json ,"
                            + "    \"franchiseId\" bigint ,"
                            + "    \"isArchive\" boolean )"
                            + "   SERVER hkg_mongo_server"
                            + "   OPTIONS (database '" + mongoDBName + "', collection '" + "subformvalue" + "');";
                    jdbcTemplate.execute(subEntityTableQuery);
                    //Foreign table for ruleset.
                    String rulesetTableQuery = "CREATE FOREIGN TABLE IF NOT EXISTS " + "ruleset"
                            + "   (_id name ,"
                            + "    \"rules\" json ,"
                            + "    \"franchise\" bigint ,"
                            + "    \"isActive\" boolean ,"
                            + "    \"createdBy\" bigint ,"
                            + "    \"createdOn\" timestamp ,"
                            + "    \"lastModifiedBy\" bigint ,"
                            + "    \"lastModifiedOn\" timestamp ,"
                            + "    \"isArchive\" boolean )"
                            + "   SERVER hkg_mongo_server"
                            + "   OPTIONS (database '" + mongoDBName + "', collection '" + "ruleset" + "');";
                    jdbcTemplate.execute(rulesetTableQuery);
                    //Foreign table for workAllocation.
                    String workAllocationTableQuery = "CREATE FOREIGN TABLE IF NOT EXISTS " + "workallotment"
                            + "   (_id name ,"
                            + "    \"forUser\" bigint ,"
                            + "    \"forDesignation\" bigint ,"
                            + "    \"currentActivityFlowNode\" bigint ,"
                            + "    \"currentActivityFlowGroup\" bigint ,"
                            + "    \"previousActivityFlowNode\" bigint ,"
                            + "    \"previousActivityFlowGroup\" bigint ,"
                            + "    \"succededNodeRoute\" bigint ,"
                            + "    \"activityVersion\" bigint ,"
                            + "    \"serviceCode\" text ,"
                            + "    \"comments\" text ,"
                            + "    \"unitType\" text ,"
                            + "    \"unitInstance\" name ,"
                            + "    \"ruleSuccessed\" text ,"
                            + "    \"status\" text ,"
                            + "    \"createdOn\" timestamp ,"
                            + "    \"createdBy\" bigint ,"
                            + "    \"lastModifiedOn\" timestamp ,"
                            + "    \"lastModifiedBy\" bigint ,"
                            + "    \"linkedWith\" name ,"
                            + "    \"franchise\" bigint ,"
                            + "    \"duration\" numeric ,"
                            + "    \"isReRouted\" boolean ,"
                            + "    \"haveValue\" boolean )"
                            + "   SERVER hkg_mongo_server"
                            + "   OPTIONS (database '" + mongoDBName + "', collection '" + "workallotment" + "');";

                    jdbcTemplate.execute(workAllocationTableQuery);
                    //Foreign table for workAllocation.
                    String workAggregationTableQuery = "CREATE FOREIGN TABLE IF NOT EXISTS " + "workallotmentaggregation"
                            + "   (_id name ,"
                            + "    \"activityFlowNode\" bigint ,"
                            + "    \"activityFlowGroup\" bigint ,"
                            + "    \"activityVersion\" bigint ,"
                            + "    \"unitType\" text ,"
                            + "    \"unitInstance\" name ,"
                            + "    \"status\" text ,"
                            + "    \"franchise\" bigint ,"
                            + "    \"duration\" numeric ,"
                            + "    \"noOfOccurences\" numeric)"
                            + "   SERVER hkg_mongo_server"
                            + "   OPTIONS (database '" + mongoDBName + "', collection '" + "workallotmentaggregation" + "');";

                    jdbcTemplate.execute(workAggregationTableQuery);
                    //Foreign table for usergoalstatus.
                    String goalSheetQuery = "CREATE FOREIGN TABLE IF NOT EXISTS " + "usergoalstatus"
                            + "   (_id name ,"
                            + "    \"fromDate\" timestamp ,"
                            + "    \"toDate\" timestamp ,"
                            + "    \"goalTemplate\" bigint ,"
                            + "    \"forUser\" bigint ,"
                            + "    \"activityNode\" bigint ,"
                            + "    \"activityGroup\" bigint ,"
                            + "    \"franchiseId\" bigint ,"
                            + "    \"goalAchieved\" boolean ,"
                            + "    \"minTarget\" integer ,"
                            + "    \"maxTarget\" integer ,"
                            + "    \"targetCount\" integer ,"
                            + "    \"realizedCount\" integer ,"
                            + "    \"createdBy\" bigint ,"
                            + "    \"createdOn\" timestamp ,"
                            + "    \"lastModifiedBy\" bigint ,"
                            + "    \"lastModifiedOn\" timestamp ,"
                            + "    \"goalType\" text ,"
                            + "    \"status\" text )"
                            + "   SERVER hkg_mongo_server"
                            + "   OPTIONS (database '" + mongoDBName + "', collection '" + "usergoalstatus" + "');";
                    jdbcTemplate.execute(goalSheetQuery);

                    //Foreign table for issue.
                    String issueQuery = "CREATE FOREIGN TABLE IF NOT EXISTS " + "issue"
                            + "   (_id name ,"
                            + "    \"invoice\" name ,"
                            + "    \"parcel\" name ,"
                            + "    \"lot\" name ,"
                            + "    \"fieldValue\" json ,"
                            + "    \"packet\" name ,"
                            + "    \"createdBy\" bigint ,"
                            + "    \"featureName\" text ,"
                            + "    \"instanceId\" bigint ,"
                            + "    \"franchiseId\" bigint ,"
                            + "    \"isArchive\" boolean ,"
                            + "    \"createdOn\" timestamp ,"
                            + "    \"lastModifiedBy\" bigint ,"
                            + "    \"lastModifiedOn\" timestamp ,"
                            + "    \"issueTo\" bigint ,"
                            + "    \"statusHistoryList\" json ,"
                            + "    \"status\" text )"
                            + "   SERVER hkg_mongo_server"
                            + "   OPTIONS (database '" + mongoDBName + "', collection '" + "issue" + "');";

                    jdbcTemplate.execute(issueQuery);

                    //Foreign table for personcapability.
                    String personCapabilityQuery = "CREATE FOREIGN TABLE IF NOT EXISTS " + "personcapability"
                            + "   (_id name ,"
                            + "    \"forPerson\" bigint ,"
                            + "    \"propertyName\" text ,"
                            + "    \"propertyValue\" bigint ,"
                            + "    \"totalFinalPlans\" int ,"
                            + "    \"succeededFinalPlans\" int ,"
                            + "    \"failedFinalPlans\" int ,"
                            + "    \"successRatio\" float ,"
                            + "    \"breakagePercentage\" float ,"
                            + "    \"isArchive\" boolean ,"
                            + "    \"createdOn\" timestamp ,"
                            + "    \"lastModifiedOn\" timestamp ,"
                            + "    \"franchise\" bigint ,"
                            + "    \"status\" text )"
                            + "   SERVER hkg_mongo_server"
                            + "   OPTIONS (database '" + mongoDBName + "', collection '" + "personcapability" + "');";

                    jdbcTemplate.execute(personCapabilityQuery);

                    //Foreign table for fieldsequence.
                    String fieldSequenceQuery = "CREATE FOREIGN TABLE IF NOT EXISTS " + "fieldsequence"
                            + "   (_id name ,"
                            + "    \"forEntity\" bigint ,"
                            + "    \"fieldName\" text ,"
                            + "    \"pattern\" text ,"
                            + "    \"applicableFrom\" timestamp ,"
                            + "    \"applicableTill\" timestamp ,"
                            + "    \"prefixCode\" text ,"
                            + "    \"startingIndex\" bigint ,"
                            + "    \"currentIndex\" bigint ,"
                            + "    \"franchise\" bigint ,"
                            + "    \"isArchive\" boolean ,"
                            + "    \"lastModifiedBy\" bigint ,"
                            + "    \"lastModifiedOn\" timestamp ,"
                            + "    \"status\" text )"
                            + "   SERVER hkg_mongo_server"
                            + "   OPTIONS (database '" + mongoDBName + "', collection '" + "fieldsequence" + "');";

                    jdbcTemplate.execute(fieldSequenceQuery);
                    //Foreign table for transfer stock.
                    String transferStockQuery = "CREATE FOREIGN TABLE IF NOT EXISTS " + "transfer"
                            + "   (_id name ,"
                            + "    \"invoice\" name ,"
                            + "    \"parcel\" name ,"
                            + "    \"lot\" name ,"
                            + "    \"fieldValue\" json ,"
                            + "    \"createdBy\" bigint ,"
                            + "    \"featureName\" text ,"
                            + "    \"instanceId\" bigint ,"
                            + "    \"franchiseId\" bigint ,"
                            + "    \"isArchive\" boolean ,"
                            + "    \"createdOn\" timestamp ,"
                            + "    \"lastModifiedBy\" bigint ,"
                            + "    \"lastModifiedOn\" timestamp ,"
                            + "    \"status\" text )"
                            + "   SERVER hkg_mongo_server"
                            + "   OPTIONS (database '" + mongoDBName + "', collection '" + "transfer" + "');";

                    jdbcTemplate.execute(transferStockQuery);
                    //Foreign table for sell stock.
                    String sellStockQuery = "CREATE FOREIGN TABLE IF NOT EXISTS " + "sell"
                            + "   (_id name ,"
                            + "    \"invoice\" name ,"
                            + "    \"parcel\" name ,"
                            + "    \"lot\" name ,"
                            + "    \"fieldValue\" json ,"
                            + "    \"createdBy\" bigint ,"
                            + "    \"featureName\" text ,"
                            + "    \"instanceId\" bigint ,"
                            + "    \"franchiseId\" bigint ,"
                            + "    \"isArchive\" boolean ,"
                            + "    \"createdOn\" timestamp ,"
                            + "    \"lastModifiedBy\" bigint ,"
                            + "    \"lastModifiedOn\" timestamp ,"
                            + "    \"status\" text )"
                            + "   SERVER hkg_mongo_server"
                            + "   OPTIONS (database '" + mongoDBName + "', collection '" + "sell" + "');";

                    jdbcTemplate.execute(sellStockQuery);
                    //Foreign table for plan.
                    String planStockQuery = "CREATE FOREIGN TABLE IF NOT EXISTS " + "plan"
                            + "   (_id name ,"
                            + "    \"invoice\" name ,"
                            + "    \"parcel\" name ,"
                            + "    \"lot\" name ,"
                            + "    \"packet\" name ,"
                            + "    \"fieldValue\" json ,"
                            + "    \"createdBy\" bigint ,"
                            + "    \"featureName\" text ,"
                            + "    \"instanceId\" bigint ,"
                            + "    \"franchiseId\" bigint ,"
                            + "    \"isArchive\" boolean ,"
                            + "    \"createdOn\" timestamp ,"
                            + "    \"lastModifiedBy\" bigint ,"
                            + "    \"lastModifiedOn\" timestamp ,"
                            + "    \"statusHistoryList\" json ,"
                            + "    \"hkPriceHistoryList\" json ,"
                            + "    \"tag\" text ,"
                            + "    \"planType\" text ,"
                            + "    \"status\" text )"
                            + "   SERVER hkg_mongo_server"
                            + "   OPTIONS (database '" + mongoDBName + "', collection '" + "plan" + "');";

                    jdbcTemplate.execute(planStockQuery);
                }

            }
        } catch (Exception ex) {
            Logger.getLogger(RbReportServiceImpl.class
                    .getName()).log(Level.SEVERE, "Exception found in createForeignTables() method: ", ex);
        }
    }

    @Override
    public void updateTabularRelationshipUsingXls() {
        try {
            ClassLoader cLoader = this.getClass().getClassLoader();
            InputStream inpstr = cLoader.getResourceAsStream("tabularRelationshipDetail.xlsx");
            List<RbTabularRelationDataBean> tabularRelationshipList = new ArrayList<>();

            Workbook wb = WorkbookFactory.create(inpstr);
            if (wb != null) {
                Sheet sheet = wb.getSheetAt(0);
                if (sheet != null) {
                    int i = 0;
                    for (Row row : sheet) {
                        if (i != 0) {
                            RbTabularRelationDataBean rbTabularRelationEntity = new RbTabularRelationDataBean();
                            if (row.getCell(1) != null && !StringUtils.isEmpty(row.getCell(1).getStringCellValue())) {
                                rbTabularRelationEntity.setTable1(row.getCell(1).getStringCellValue().trim());
                            }
                            if (row.getCell(2) != null && !StringUtils.isEmpty(row.getCell(2).getStringCellValue())) {
                                rbTabularRelationEntity.setTable1Column(row.getCell(2).getStringCellValue().trim());
                            }
                            if (row.getCell(3) != null && !StringUtils.isEmpty(row.getCell(3).getStringCellValue())) {
                                rbTabularRelationEntity.setTable2(row.getCell(3).getStringCellValue().trim());
                            }
                            if (row.getCell(4) != null && !StringUtils.isEmpty(row.getCell(4).getStringCellValue())) {
                                rbTabularRelationEntity.setTable2Column(row.getCell(4).getStringCellValue().trim());
                            }
                            if (row.getCell(5) != null && !StringUtils.isEmpty(row.getCell(5).getStringCellValue())) {
                                rbTabularRelationEntity.setJoinTable(row.getCell(5).getStringCellValue().trim());
                            }
                            if (row.getCell(6) != null && !StringUtils.isEmpty(row.getCell(6).getStringCellValue())) {
                                rbTabularRelationEntity.setJoinColumnTable1(row.getCell(6).getStringCellValue().trim());
                            }
                            if (row.getCell(7) != null && !StringUtils.isEmpty(row.getCell(7).getStringCellValue())) {
                                rbTabularRelationEntity.setJoinColumnTable2(row.getCell(7).getStringCellValue().trim());
                            }
                            if (row.getCell(8) != null && !StringUtils.isEmpty(row.getCell(8).getStringCellValue())) {
                                rbTabularRelationEntity.setRelationLeftToRight(row.getCell(8).getStringCellValue().trim());
                            }
                            tabularRelationshipList.add(rbTabularRelationEntity);
                        }
                        i++;
                    }
//                    System.out.println("RbTabularRelationEntities---" + tabularRelationshipList);
                }
            }

            if (!CollectionUtils.isEmpty(tabularRelationshipList)) {
                List<RbTabularRelationEntity> retrievedTabularRelationList = rbTabularRelationDao.retrieveAll();
                if (!CollectionUtils.isEmpty(retrievedTabularRelationList)) {
                    for (RbTabularRelationEntity tabularRelation : retrievedTabularRelationList) {
                        Boolean isExists = Boolean.FALSE;
                        for (RbTabularRelationDataBean tabularRelation1 : tabularRelationshipList) {
                            String table1 = tabularRelation.getTable1();
                            String table2 = tabularRelation.getTable2();
                            if ((table1.equals(tabularRelation1.getTable1()) || table1.equals(tabularRelation1.getTable2()))
                                    && (table2.equals(tabularRelation1.getTable1()) || table2.equals(tabularRelation1.getTable2()))) {
                                isExists = Boolean.TRUE;
                                break;
                            }
                        }
                        if (!isExists) {
                            tabularRelation.setIsArchive(Boolean.TRUE);
                        }
                    }
                    Iterator<RbTabularRelationDataBean> itr = tabularRelationshipList.iterator();
                    while (itr.hasNext()) {
                        RbTabularRelationDataBean rbTabularRelationDataBean = itr.next();
                        String table1 = rbTabularRelationDataBean.getTable1();
                        String table2 = rbTabularRelationDataBean.getTable2();
                        for (RbTabularRelationEntity dbTabularEntity : retrievedTabularRelationList) {
                            if ((table1.equals(dbTabularEntity.getTable1()) || table1.equals(dbTabularEntity.getTable2()))
                                    && (table2.equals(dbTabularEntity.getTable1()) || table2.equals(dbTabularEntity.getTable2()))) {
                                dbTabularEntity.setTable1(table1);
                                dbTabularEntity.setTable2(table2);
                                dbTabularEntity.setTable1Column(rbTabularRelationDataBean.getTable1Column());
                                dbTabularEntity.setTable2Column(rbTabularRelationDataBean.getTable2Column());
                                dbTabularEntity.setJoinTable(rbTabularRelationDataBean.getJoinTable());
                                dbTabularEntity.setJoinColumnTable1(rbTabularRelationDataBean.getJoinColumnTable1());
                                dbTabularEntity.setJoinColumnTable2(rbTabularRelationDataBean.getJoinColumnTable2());
                                dbTabularEntity.setRelationLeftToRight(rbTabularRelationDataBean.getRelationLeftToRight());
                                dbTabularEntity.setIsArchive(Boolean.FALSE);
                                itr.remove();
                                break;
                            }
                        }
                    }
                    if (!CollectionUtils.isEmpty(tabularRelationshipList)) {
                        List<RbTabularRelationEntity> tabularRelationEntityList = new ArrayList<>();
                        for (RbTabularRelationDataBean tabularRelationDataBean : tabularRelationshipList) {
                            RbTabularRelationEntity dbTabularEntity = this.convertTabularRelationDataBeanToEntity(tabularRelationDataBean);
                            tabularRelationEntityList.add(dbTabularEntity);
                        }
                        rbTabularRelationDao.saveOrUpdateAll(tabularRelationEntityList);
                    }
                } else {
                    List<RbTabularRelationEntity> tabularRelationEntityList = new ArrayList<>();
                    for (RbTabularRelationDataBean tabularRelationDataBean : tabularRelationshipList) {
                        RbTabularRelationEntity dbTabularEntity = this.convertTabularRelationDataBeanToEntity(tabularRelationDataBean);
                        tabularRelationEntityList.add(dbTabularEntity);
                    }
                    rbTabularRelationDao.saveOrUpdateAll(tabularRelationEntityList);

                }
            }
        } catch (IOException | InvalidFormatException ex) {
            Logger.getLogger(RbReportServiceImpl.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    private RbTabularRelationEntity convertTabularRelationDataBeanToEntity(RbTabularRelationDataBean rbTabularRelationDataBean) {
        RbTabularRelationEntity rbTabularRelationEntity = new RbTabularRelationEntity();
        if (rbTabularRelationDataBean != null) {
            rbTabularRelationEntity.setTable1(rbTabularRelationDataBean.getTable1());
            rbTabularRelationEntity.setTable2(rbTabularRelationDataBean.getTable2());
            rbTabularRelationEntity.setTable1Column(rbTabularRelationDataBean.getTable1Column());
            rbTabularRelationEntity.setTable2Column(rbTabularRelationDataBean.getTable2Column());
            rbTabularRelationEntity.setJoinTable(rbTabularRelationDataBean.getJoinTable());
            rbTabularRelationEntity.setJoinColumnTable1(rbTabularRelationDataBean.getJoinColumnTable1());
            rbTabularRelationEntity.setJoinColumnTable2(rbTabularRelationDataBean.getJoinColumnTable2());
            rbTabularRelationEntity.setRelationLeftToRight(rbTabularRelationDataBean.getRelationLeftToRight());
            rbTabularRelationEntity.setIsArchive(Boolean.FALSE);
        }
        return rbTabularRelationEntity;
    }

    @Override
    public Set<String> retrieveTablesWithIsArchiveFieldAbsent() {
        final Set<String> resultSet = new HashSet<>();
        String query = "SELECT distinct(table_name)\n"
                + "FROM information_schema.columns\n"
                + "WHERE table_schema = 'public'\n"
                + "and table_name not in\n"
                + "(select table_name from information_schema.columns where table_schema = 'public' and column_name = 'is_archive')";
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.query(query, new RowMapper<ResultSet>() {

            @Override
            public ResultSet mapRow(ResultSet rs, int i) throws SQLException {
                String tableName = rs.getString(1);
                resultSet.add(tableName);
                return rs;
            }
        });
        return resultSet;
    }

    @Override
    public Long createReport(RbReport rbReport) {
        return rbReportDao.create(rbReport);
    }

    @Override
    public RbReport retriveReportById(Long id, Boolean reportFieldSetRequired, Boolean reportAccessSetRequired) {
        return rbReportDao.retrieveReportById(id, reportFieldSetRequired, reportAccessSetRequired);
    }

    @Override
    public List<RbReport> retriveAllReports(Boolean reportFieldSetRequired, Long companyId) {
        return rbReportDao.retrieveAllReports(reportFieldSetRequired, companyId);
    }

    @Override
    public void updateReport(RbReport rbReport) {
        rbReportDao.update(rbReport);
    }

    @Override
    public List<String> retrieveTableNames() {
        return entityMetadataDao.retrieveTableNames();
    }

    @Override
    public List<String> retrieveTableColumns(String tableName) {
        return entityMetadataDao.retrieveTableColumns(tableName);
    }

    @Override
    public Map<String, Object> retrieveResults(String query, List<RbReportField> fieldList, Map<Long, String> featureNameIdMap, Set<Long> franchiseList) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        final List<LinkedHashMap<String, Object>> resultList = new ArrayList<>();
        Map<String, Object> partialResults = new HashMap<>();
        if (!StringUtils.isEmpty(query)) {
//            int totalRecords = jdbcTemplate.queryForInt("select count(*) from (" + query + ")as temp");
            //  Mital: Commenting below code as it's not working proper
//            if (limit != null && offSet != null) {
//                query += " limit " + limit + " offset " + offSet;
//            }
            String franchiseIds = null;
            if (!CollectionUtils.isEmpty(franchiseList)) {
                for (Long franchiseId : franchiseList) {
                    if (StringUtils.isEmpty(franchiseIds)) {
                        franchiseIds = franchiseId.toString();
                    } else {
                        franchiseIds += "," + franchiseId.toString();
                    }
                }
            }
            query = query.replace("?", franchiseIds);
            System.out.println("Query before Executing---" + query);
            List<ResultSet> rows = jdbcTemplate.query(query, new RowMapper<ResultSet>() {
                @Override
                public ResultSet mapRow(ResultSet rs, int i) throws SQLException {
                    ResultSetMetaData resultSetMetaData = rs.getMetaData();
                    LinkedHashMap<String, Object> row = new LinkedHashMap<>();
                    int count = 1;
                    while (count <= resultSetMetaData.getColumnCount()) {
                        row.put(resultSetMetaData.getColumnName(count), rs.getObject(count));
                        count++;
                    }
                    resultList.add(row);
                    return rs;
                }
            });
            /**
             * Code to separate aggregated fields to different rows *
             */
            Set<String> aggregateDbBaseNames = new HashSet<>();
            Map<String, Set<String>> dbBaseNameToFieldName = new HashMap<>();
            Map<String, String> fieldLabelToDBName = new HashMap<>();
            Map<String, String> fieldLabelToDataType = new HashMap<>();

            if (!CollectionUtils.isEmpty(fieldList)) {
                //Add aggregated fields, inner documents and section list fields to be evaluted.
                for (RbReportField reportField : fieldList) {
                    if (RbReportConstantUtils.AggragateFields.contains(reportField.getDbBaseName())
                            || RbReportConstantUtils.INNER_DOCUMENT_BASE_NAME_MAP.keySet().contains(reportField.getDbBaseName())) {
                        aggregateDbBaseNames.add(reportField.getDbBaseName());
                        if (dbBaseNameToFieldName.get(reportField.getDbBaseName()) == null) {
                            dbBaseNameToFieldName.put(reportField.getDbBaseName(), new HashSet<String>());
                        }
                        dbBaseNameToFieldName.get(reportField.getDbBaseName()).add(reportField.getReportFieldName());
                        fieldLabelToDBName.put(reportField.getReportFieldName(), reportField.getDbFieldName());
                        fieldLabelToDataType.put(reportField.getReportFieldName(), reportField.getFieldDataType());
                    } else if (reportField.getDbBaseType().equals(HkSystemConstantUtil.DBBaseType.MONGO_DB)) {
                        Type type = new TypeToken<Map<RbReportConstantUtils.CustomAttribute, Object>>() {
                        }.getType();
                        Map<RbReportConstantUtils.CustomAttribute, Object> innerMap = (new Gson()).fromJson(reportField.getCustomAttributes(), type);
                        if (innerMap.containsKey(RbReportConstantUtils.CustomAttribute.SECTION_NAME)) {
                            if (innerMap.get(RbReportConstantUtils.CustomAttribute.SECTION_NAME) != null) {
                                aggregateDbBaseNames.add(reportField.getDbBaseName());
                                if (dbBaseNameToFieldName.get(reportField.getDbBaseName()) == null) {
                                    dbBaseNameToFieldName.put(reportField.getDbBaseName(), new HashSet<String>());
                                }
                                dbBaseNameToFieldName.get(reportField.getDbBaseName()).add(reportField.getDbBaseName() + ".sectionList");
                            }
                        }
                    }
                }
            }

            List<LinkedHashMap<String, Object>> subRows = new ArrayList<>();

            if (!CollectionUtils.isEmpty(resultList) && !CollectionUtils.isEmpty(aggregateDbBaseNames)) {
                Iterator<LinkedHashMap<String, Object>> itr = resultList.iterator();
                while (itr.hasNext()) {
                    LinkedHashMap<String, Object> resultEntry = itr.next();
                    int newRowCount = 0;
                    int maxLength = 0;
                    boolean isRowReplaced = false;
                    LinkedHashMap<String, Object> resultEntry1 = new LinkedHashMap<>(resultEntry);
                    Map<String, List<Object>> modifiedArrgegatedMap = new HashMap<>();
                    for (String dbBaseName : aggregateDbBaseNames) {
                        Set<Map<String, Object>> modifiedAggregatedSetPerDb = new HashSet<>();
                        List<Map<String, Object>> modifiedAggregatedListPerDb = new ArrayList<>();
                        List<String> fields = new ArrayList<>(dbBaseNameToFieldName.get(dbBaseName));
                        String firstField = fields.get(0);
                        boolean isSectionList = false;
                        if (firstField.equals(dbBaseName + ".sectionList")) {
                            isSectionList = true;
                        }
                        Jdbc4Array fieldValue = null;
                        Object[] values = null;
                        Object sectionValue = null;
                        //Retrieve primary aggregated value.
                        //Three cases may appear.
                        //SectionList--- json aggregate
                        //inner document list--- json aggregate
                        //Others like relational---array aggregate.
                        if (isSectionList) {
                            Type type = new TypeToken<List<List<Object>>>() {
                            }.getType();
                            List<List<Object>> innerArray = (new Gson()).fromJson(resultEntry1.get(firstField).toString().trim(), type);
                            if (innerArray == null) {
                                sectionValue = null;
                            } else {
                                sectionValue = innerArray.get(0);
                            }
                        } else if (!RbReportConstantUtils.INNER_DOCUMENT_BASE_NAME_MAP.keySet().contains(dbBaseName)) {
                            fieldValue = (Jdbc4Array) resultEntry1.get(firstField);
                        } else {
                            Type type = new TypeToken<List<List<Object>>>() {
                            }.getType();
                            List<List<Object>> innerArray = (new Gson()).fromJson(resultEntry1.get(firstField).toString().trim(), type);
                            List<Object> list = innerArray.get(0);
                            if (list == null) {
                                values = new Object[]{null};
                            } else {
                                values = (Object[]) list.toArray();
                            }
                        }
                        /**
                         * Create map of unique rows extracted from aggregate
                         * values.
                         */
                        try {
                            if (isSectionList) {
                                Map<String, Object> modifiedMapPerField = new HashMap<>();
                                List<String> fields1 = new ArrayList<>(dbBaseNameToFieldName.get(dbBaseName));
                                for (String dbField : fields1) {
                                    if (sectionValue != null) {
                                        modifiedMapPerField.put(dbField, sectionValue);
                                    } else {
                                        modifiedMapPerField.put(dbField, null);
                                    }
                                }
                                modifiedAggregatedListPerDb.add(modifiedMapPerField);
                            } else if (RbReportConstantUtils.INNER_DOCUMENT_BASE_NAME_MAP.keySet().contains(dbBaseName)) {
                                //For inner document, this will be executed only once.
                                for (Object value : values) {
                                    Map<String, Object> modifiedMapPerField = new HashMap<>();
                                    List<String> fields1 = new ArrayList<>(dbBaseNameToFieldName.get(dbBaseName));
                                    for (String dbField : fields1) {
                                        if (value != null) {
                                            Map<String, Object> actualValue = (Map<String, Object>) value;
                                            Object valueToPut = actualValue.get(fieldLabelToDBName.get(dbField));
                                            if (fieldLabelToDataType.get(dbField).equals(HkSystemConstantUtil.CustomField.DataType.TIMESTAMP)) {
                                                Map<String, Object> dataValueMap = (Map<String, Object>) valueToPut;
                                                Double dateValue = Double.parseDouble(dataValueMap.get("$date").toString());
                                                String dateString = formatDate(dateValue.longValue(), true);
                                                valueToPut = dateString;
                                            }
                                            modifiedMapPerField.put(dbField, valueToPut);
                                        } else {
                                            modifiedMapPerField.put(dbField, null);
                                        }
                                    }
                                    modifiedAggregatedListPerDb.add(modifiedMapPerField);
//                                    System.out.println("modifiedMapPerField111111-----------" + modifiedMapPerField);
                                }
                            } else {
                                values = (Object[]) fieldValue.getArray();
                                for (int i = 0; i < values.length; i++) {
                                    Map<String, Object> modifiedMapPerField = new HashMap<>();
                                    List<String> fields1 = new ArrayList<>(dbBaseNameToFieldName.get(dbBaseName));
                                    for (String dbField : fields1) {
                                        Jdbc4Array fieldValuesArray = (Jdbc4Array) resultEntry1.get(dbField);
                                        Object[] fieldValues = (Object[]) fieldValuesArray.getArray();
                                        if (i >= fieldValues.length) {
                                            modifiedMapPerField.put(dbField, null);
                                        } else {
                                            modifiedMapPerField.put(dbField, fieldValues[i]);
                                        }
                                    }
                                    //Added to an array list to allow duplicates as input will be a json array, it will be
                                    //unique per lot/packet etc.
                                    modifiedAggregatedSetPerDb.add(modifiedMapPerField);
//                                    System.out.println("modifiedMapPerField-----------" + modifiedMapPerField);
                                }
                            }
                            /**
                             * Merge again while removing duplicate data.
                             */
                            if (!CollectionUtils.isEmpty(modifiedAggregatedSetPerDb) || !CollectionUtils.isEmpty(modifiedAggregatedListPerDb)) {
                                modifiedAggregatedListPerDb.addAll(modifiedAggregatedSetPerDb);
                                for (Map<String, Object> uniqueRow : modifiedAggregatedListPerDb) {
                                    for (Map.Entry<String, Object> entry : uniqueRow.entrySet()) {
                                        if (modifiedArrgegatedMap.get(entry.getKey()) == null) {
                                            modifiedArrgegatedMap.put(entry.getKey(), new ArrayList<>());
                                        }
                                        modifiedArrgegatedMap.get(entry.getKey()).add(entry.getValue());
                                    }
                                }
                            }
//                            System.out.println("modifiedAggregatedListPerDb-------------------" + modifiedAggregatedListPerDb);
//                            System.out.println("modifiedAggregatedListPerDb.size-------------------" + modifiedAggregatedListPerDb.size());
                        } catch (SQLException ex) {
                            Logger.getLogger(RbReportServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (ParseException ex) {
                            Logger.getLogger(RbReportServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    if (!CollectionUtils.isEmpty(modifiedArrgegatedMap)) {
                        /**
                         * Determine the maximum length per row.
                         */
                        for (Map.Entry<String, List<Object>> entry : modifiedArrgegatedMap.entrySet()) {
                            if (!CollectionUtils.isEmpty(entry.getValue())) {
                                if (entry.getValue().size() > maxLength) {
                                    maxLength = entry.getValue().size();
                                }
                            }
                        }
                        resultEntry1.putAll(modifiedArrgegatedMap);
                    }
//                    System.out.println("MaxLength-------------------" + maxLength);
                    /**
                     * Recreate the values into separate rows. Null if no value
                     * is present but aggregated.
                     */
                    for (int i = 0; i < maxLength; i++) {
                        LinkedHashMap<String, Object> resultEntry2 = new LinkedHashMap<>(resultEntry1);
                        for (String dbBaseName : aggregateDbBaseNames) {
                            List<String> fields = new ArrayList<>(dbBaseNameToFieldName.get(dbBaseName));
                            for (String dbField : fields) {
                                List<Object> fieldValuesArray = (List<Object>) resultEntry1.get(dbField);
                                Object[] fieldValues = (Object[]) fieldValuesArray.toArray();
                                if (i >= fieldValues.length) {
                                    resultEntry2.put(dbField, null);
                                } else {
                                    resultEntry2.put(dbField, fieldValues[i]);
                                }
                            }
                        }
//                        System.out.println("resultEntry2-----------" + resultEntry2);
                        subRows.add(resultEntry2);
                        isRowReplaced = true;
                    }
                    if (isRowReplaced) {
                        itr.remove();
                    }
                }
            }
            /**
             * Replace freshly generated rows.
             */
            if (!CollectionUtils.isEmpty(subRows)) {
                resultList.addAll(subRows);
            }
            /**
             * Code to separate aggregated fields to different rows ends*
             */

            jdbcTemplate = null;
            partialResults.put("totalRecords", resultList.size());
            partialResults.put("records", resultList);
            return partialResults;
        } else {
            System.out.println("noSqlFieldList--" + fieldList.size());
            List<Criteria> criterias = new ArrayList<>();
//            criterias.add(Criteria.where("franchiseId").is(3L));
            Map<Long, List<RbReportField>> featureFieldMap = new HashMap<>();
            LinkedHashMap<String, Object> row = new LinkedHashMap<>();
            List<GenericDocument> documents = null;
            Map<Long, List<GenericDocument>> documentList = new HashMap<>();
            if (!CollectionUtils.isEmpty(fieldList) && !CollectionUtils.isEmpty(featureNameIdMap)) {
                for (Map.Entry<Long, String> entry : featureNameIdMap.entrySet()) {
                    String featureName = entry.getValue();
                    System.out.println("featureName---" + featureName);
                    if (featureName != null) {
                        String feature = HkSystemConstantUtil.ActualFeatureCustomFieldFeatureMap.get(featureName);
                        System.out.println("feature--" + feature);

                        if (feature != null && feature.equals(HkSystemConstantUtil.FeatureNameForCustomField.USER.toString())) {
                            documents = mongoGenericDao.findByCriteria(criterias, HkUserDocument.class
                            );
                        } else if (feature != null && feature.equals(HkSystemConstantUtil.FeatureNameForCustomField.DEPARTMENT.toString())) {
                            documents = mongoGenericDao.findByCriteria(criterias, HkDepartmentDocument.class
                            );
                        } else if (feature != null && feature.equals(HkSystemConstantUtil.FeatureNameForCustomField.FRANCHISE.toString())) {
                            documents = mongoGenericDao.findByCriteria(criterias, HkFranchiseDocument.class
                            );
                        } else if (feature != null && feature.equals(HkSystemConstantUtil.FeatureNameForCustomField.LEAVEWORKFLOW.toString())) {
                            documents = mongoGenericDao.findByCriteria(criterias, HkLeaveWorkflowDocument.class
                            );
                        } else if (feature != null && feature.equals(HkSystemConstantUtil.FeatureNameForCustomField.ASSET.toString())) {
                            documents = mongoGenericDao.findByCriteria(criterias, HkAssetDocument.class
                            );
                        } else if (feature != null && feature.equals(HkSystemConstantUtil.FeatureNameForCustomField.CATEGORY.toString())) {
                            documents = mongoGenericDao.findByCriteria(criterias, HkCategoryDocument.class
                            );
                        } else if (feature != null && feature.equals(HkSystemConstantUtil.FeatureNameForCustomField.HOLIDAY.toString())) {
                            documents = mongoGenericDao.findByCriteria(criterias, HkHolidayDocument.class
                            );
                        } else if (feature != null && feature.equals(HkSystemConstantUtil.FeatureNameForCustomField.LEAVE.toString())) {
                            documents = mongoGenericDao.findByCriteria(criterias, HkApplyLeaveDocument.class
                            );
                        } else if (feature != null && feature.equals(HkSystemConstantUtil.FeatureNameForCustomField.EVENT.toString())) {
                            documents = mongoGenericDao.findByCriteria(criterias, HkEventDocument.class
                            );
                        } else if (feature != null && feature.equals(HkSystemConstantUtil.FeatureNameForCustomField.TASK.toString())) {
                            documents = mongoGenericDao.findByCriteria(criterias, HkTaskDocument.class
                            );
                        } else if (feature != null && feature.equals(HkSystemConstantUtil.FeatureNameForCustomField.SHIFT.toString())) {
                            documents = mongoGenericDao.findByCriteria(criterias, HkShiftDocument.class
                            );
                        }
                    }
                    documentList.put(entry.getKey(), documents);
                }
                for (RbReportField rbReportField : fieldList) {
                    if (featureFieldMap.containsKey(rbReportField.getFeatureId())) {
                        featureFieldMap.get(rbReportField.getFeatureId()).add(rbReportField);
                    } else {
                        List<RbReportField> reportList = new ArrayList<>();
                        reportList.add(rbReportField);
                        featureFieldMap.put(rbReportField.getFeatureId(), reportList);
                    }
                }
                for (Map.Entry<Long, List<GenericDocument>> featureDocument : documentList.entrySet()) {
                    if (featureFieldMap.containsKey(featureDocument.getKey())) {
                        List<GenericDocument> featureDocuments = featureDocument.getValue();
                        List<RbReportField> rbFieldList = featureFieldMap.get(featureDocument.getKey());
                        if (!CollectionUtils.isEmpty(featureDocuments)) {
                            for (GenericDocument genericDocument : featureDocuments) {
                                if (genericDocument.getFieldValue() != null) {
                                    for (RbReportField rbReportField : rbFieldList) {
                                        if (genericDocument.getFieldValue().toMap().containsKey(rbReportField.getDbFieldName())) {
                                            if (!StringUtils.isEmpty(rbReportField.getReportFieldName())) {
                                                row.put(rbReportField.getReportFieldName(), genericDocument.getFieldValue().toMap().get(rbReportField.getDbFieldName()));
                                            } else {
                                                row.put(rbReportField.getFieldLabel(), genericDocument.getFieldValue().toMap().get(rbReportField.getDbFieldName()));
                                            }

                                        }
                                    }
                                }
                                if (!CollectionUtils.isEmpty(genericDocument.getSectionList())) {
                                    for (SectionDocument sectionDocument : genericDocument.getSectionList()) {
                                        if (!CollectionUtils.isEmpty(sectionDocument.getCustomFields())) {
                                            for (CustomField customField : sectionDocument.getCustomFields()) {
                                                for (RbReportField rbReportField : rbFieldList) {
                                                    if (customField.getFieldValue().toMap().containsKey(rbReportField.getDbFieldName())) {
                                                        if (!StringUtils.isEmpty(rbReportField.getReportFieldName())) {
                                                            row.put(rbReportField.getReportFieldName(), customField.getFieldValue().toMap().get(rbReportField.getDbFieldName()));
                                                        } else {
                                                            row.put(rbReportField.getFieldLabel(), customField.getFieldValue().toMap().get(rbReportField.getDbFieldName()));
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                                System.out.println("row---" + row);
                                resultList.add(row);
                            }
                        }

                    }

                }
            }
            System.out.println("resultList---" + resultList);
            partialResults.put("records", resultList);
        }
        return partialResults;
    }

    @Override
    public Map<String, String> retriveQueryMetadata(String query) {
        if (query != null) {
            return entityMetadataDao.retriveQueryMetadata(query);
        }
        return null;
    }

    @Override
    public Map<String, Object> retrieveDistinctColumnResults(String query, String distinctColumnName) {
        return entityMetadataDao.retrieveDistinctColumnResults(query, distinctColumnName);
    }

    @Override
    public void createAllReportInformation(RbReport rbReport, List<RbReportField> rbReportFieldList) {
        rbReportDao.create(rbReport);
        //List for storing report field without converter and formatter pk
        List<RbReportField> insertList = new ArrayList();
        //List for storing report field with converter and formatter pk
        List<RbReportField> updateList = new ArrayList();
        for (RbReportField reportField : rbReportFieldList) {
            RbReportField newReportField = new RbReportField();
//            newReportField.setRbFieldConverterSet(reportField.getRbFieldConverterSet());
//            newReportField.setRbFieldFormatterSet(reportField.getRbFieldFormatterSet());
//
//            reportField.setRbFieldConverterSet(null);
//            reportField.setRbFieldFormatterSet(null);
            updateList.add(newReportField);
            insertList.add(reportField);
        }
        rbReportFieldDao.createAllRbReportField(insertList);
        int i = 0;
        for (RbReportField rbReportField : insertList) {
            //Set of field formatter and converter
//            Set<RbFieldFormatter> formatterSet = updateList.get(i).getRbFieldFormatterSet();
//            Set<RbFieldConverter> converterSet = updateList.get(i).getRbFieldConverterSet();
//
//            //Set pk of formatter and converter after inserting report fields and get the id of that
//            if (formatterSet != null && !formatterSet.isEmpty()) {
//                for (RbFieldFormatter rbFieldFormatter : formatterSet) {
//                    RbFieldFormatterPK rbFieldFormatterPK = rbFieldFormatter.getRbFieldFormatterPK();
//                    rbFieldFormatterPK.setReportField(rbReportField.getId());
//                }
//            }
//            if (converterSet != null && !converterSet.isEmpty()) {
//                for (RbFieldConverter rbFieldConverter : converterSet) {
//                    RbFieldConverterPK rbFieldConverterPK = rbFieldConverter.getRbFieldConverterPK();
//                    rbFieldConverterPK.setReportField(rbReportField.getId());
//                }
//            }
//            rbReportField.setRbFieldConverterSet(converterSet);
//            rbReportField.setRbFieldFormatterSet(formatterSet);
            i++;
        }
    }

    @Override
    public Map<String, Object> retrieveAllTableWithColumnsListByTableList(List<String> tableList) {
        return entityMetadataDao.retrieveAllTableWithColumnsListByTableList(tableList);
    }

    @Override
    public List<List<String>> retrieveAllResults(String query) {
        return entityMetadataDao.retrieveAllResults(query);
    }

    @Override
    public Map<String, Object> retrieveDistinctColumnValuesInRange(String query, String distinctColumnName, String searchTerm, Integer offSet, Integer limit) {
        return entityMetadataDao.retrieveDistinctColumnValuesInRange(query, distinctColumnName, searchTerm, offSet, limit);
    }

    @Override
    public List<RbTabularRelationEntity> retrieveRelationAmongTables(List<String> tableNameList, List<String> joinAttributeList) {
        List<RbTabularRelationEntity> resultList = null;
        if (!CollectionUtils.isEmpty(tableNameList) && tableNameList.size() > 1) {
            List<String> tableWithJoinList = new ArrayList<>(tableNameList);
            if (!CollectionUtils.isEmpty(joinAttributeList)) {
                for (String joinAttr : joinAttributeList) {
                    if (!tableWithJoinList.contains(joinAttr)) {
                        tableWithJoinList.add(joinAttr);
                    }
                }
            }
            System.out.println("tableWithJoinList---" + tableWithJoinList);
            Search search = SearchFactory.getSearch(RbTabularRelationEntity.class
            );
            search.addFilterEqual(
                    "isArchive", false);
            search.addFilterAnd(
                    Filter.in("table1", tableWithJoinList), Filter.in("table2", tableWithJoinList));

            resultList = commonDao.search(search);
        }

        return resultList;
    }

    @Override
    public List<RbTabularRelationEntity> retrieveCustomFieldRelationShip(List<String> customFieldTableNames) {
        List<RbTabularRelationEntity> resultList = null;

        if (!CollectionUtils.isEmpty(customFieldTableNames)) {
            Search search = SearchFactory.getSearch(RbTabularRelationEntity.class
            );
            search.addFilterIn(
                    "table2", customFieldTableNames);
            resultList = commonDao.search(search);
        }
        return resultList;
    }

    @Override
    public RbReport retrieveReportByFeature(Long featureId) {
        RbReport rbReport = null;

        if (featureId != null) {
            Search search = SearchFactory.getSearch(RbReport.class
            );
            search.addFilterEqual(
                    "custom1", featureId);
            rbReport = (RbReport) commonDao.searchUnique(search);
        }
        return rbReport;
    }

    @Override
    public List<RbReport> retrieveExternalReportsByFeatureIds(List<Long> featureIds) {
        if (!CollectionUtils.isEmpty(featureIds)) {
            Search search = SearchFactory.getSearch(RbReport.class);
            search.addFilterIn("custom1", featureIds);
            search.addFilterEqual("isExternalReport", true);
            List<RbReport> result = commonDao.search(search);
            return result;
        }
        return null;
    }

    @Override
    public void saveAllReports(List<RbReport> reports) {
        if (!CollectionUtils.isEmpty(reports)) {
            commonDao.saveAll(reports);
        }
    }

    @Override
    public void copyReportsFromSourceToDestination(Long sourceFranchise, Long destinationFranchise) {
        List<RbReport> sourceReports = this.retriveAllReports(Boolean.TRUE, sourceFranchise);
        List<RbReport> destinationReports = new ArrayList<>();

        if (!CollectionUtils.isEmpty(sourceReports)) {
            List<HkValueEntity> oldReportGroups = foundationService.retrieveMasterValuesByCode(sourceFranchise, Arrays.asList(HkSystemConstantUtil.MasterCode.REPORT_GROUP));
            List<HkValueEntity> newReportGroups = new ArrayList<>();
            Map<Long, HkValueEntity> oldToNewReportGroupMap = new HashMap<>();

            if (!CollectionUtils.isEmpty(oldReportGroups)) {
                for (HkValueEntity hkValueEntity : oldReportGroups) {
                    try {
                        HkValueEntity valueEntity = hkValueEntity.clone();

                        valueEntity.setId(null);
                        valueEntity.setFranchise(destinationFranchise);

                        newReportGroups.add(valueEntity);
                        oldToNewReportGroupMap.put(hkValueEntity.getId(), valueEntity);

                    } catch (CloneNotSupportedException ex) {
                        Logger.getLogger(RbReportServiceImpl.class
                                .getName()).log(Level.SEVERE, null, ex);
                    }
                }

                foundationService.createAllMasterValues(newReportGroups);
            }
            Map<GenericDao.QueryOperators, Object> criterias = new HashMap<>();
            Map<String, Object> equalMap = new HashMap<>();

            equalMap.put(UMFeatureDao.MENU_TYPE, "RMI");
            equalMap.put(UMFeatureDao.IS_ARCHIVE, Boolean.FALSE);
            equalMap.put(UMFeatureDao.COMPANY, sourceFranchise);

            criterias.put(GenericDao.QueryOperators.EQUAL, equalMap);

            List<UMFeature> oldReportFeatures = new ArrayList<>();
            List<UMFeature> newReportFeatures = new ArrayList<>();
            Map<Long, UMFeature> oldToNewFeatureMap = new HashMap<>();
            try {
                oldReportFeatures = featureService.retrieveFeatures(null, criterias, null);

                if (!CollectionUtils.isEmpty(oldReportFeatures)) {
                    for (UMFeature uMFeature : oldReportFeatures) {
                        UMFeature feature = new UMFeature(null, uMFeature.getName(), uMFeature.getIsCrud(), uMFeature.getCreatedBy(), new Date(), uMFeature.getIsActive(), uMFeature.getIsArchive());
                        feature.setCompany(destinationFranchise);
                        feature.setMenuCategory(uMFeature.getMenuCategory());
                        feature.setMenuLabel(uMFeature.getMenuLabel());
                        feature.setMenuType(uMFeature.getMenuType());
                        feature.setPrecedence(uMFeature.getPrecedence());
                        if (!StringUtils.isEmpty(uMFeature.getDescription())) {
                            feature.setDescription(oldToNewReportGroupMap.get(Long.parseLong(uMFeature.getDescription())).getId().toString());
                        }
                        newReportFeatures.add(feature);
                        oldToNewFeatureMap.put(uMFeature.getId(), feature);
                    }

                    featureService.createAllFeature(newReportFeatures);

                }

            } catch (GenericDatabaseException ex) {
                Logger.getLogger(RbReportServiceImpl.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
//            Type type1 = new TypeToken<Map<String, Object>>() {
//            }.getType();
//            List<HkFieldEntity> fields = fieldService.retrieveAllCustomFieldsByCompanyId(destinationFranchise);
//            Map<Long, HkFieldEntity> oldtoNewFields = new HashMap<>();
//            if (!CollectionUtils.isEmpty(fields)) {
//                for (HkFieldEntity hkFieldEntity : fields) {
//                    oldtoNewFields.put(hkFieldEntity.getSourceFieldId(), hkFieldEntity);
//                }
//            }
//            Gson gson = new GsonBuilder().disableHtmlEscaping().create();

            for (RbReport rbReport : sourceReports) {
                try {
                    RbReport report = rbReport.clone();

                    report.setId(null);
                    report.setCompany(destinationFranchise);
                    if (oldToNewFeatureMap.get(rbReport.getCustom1()) != null) {
                        report.setCustom1(oldToNewFeatureMap.get(rbReport.getCustom1()).getId());
                    }
                    if (rbReport.getReportGroup() != null && oldToNewReportGroupMap.get(rbReport.getReportGroup()) != null) {
                        report.setReportGroup(oldToNewReportGroupMap.get(rbReport.getReportGroup()).getId());
                    }

                    if (!CollectionUtils.isEmpty(rbReport.getRbReportFieldSet())) {
                        List<RbReportField> newReportFields = new ArrayList<>();
                        for (RbReportField rbReportField : rbReport.getRbReportFieldSet()) {
                            RbReportField reportField = rbReportField.clone();

                            reportField.setId(null);
                            reportField.setReport(report);
//                            String customAttrs = rbReportField.getCustomAttributes();
//
//                            Map customAttrsMap = (new Gson()).fromJson(customAttrs, type1);

//                            if (!CollectionUtils.isEmpty(customAttrsMap) && customAttrsMap.get(RbReportConstantUtils.CustomAttribute.HK_FIELD_ID.toString()) != null) {
//
//                                if (oldtoNewFields.get(Long.parseLong((String) customAttrsMap.get(RbReportConstantUtils.CustomAttribute.HK_FIELD_ID.toString()))) != null) {
//                                    customAttrsMap.put(RbReportConstantUtils.CustomAttribute.HK_FIELD_ID.toString(), oldtoNewFields.get(Long.parseLong((String) customAttrsMap.get(RbReportConstantUtils.CustomAttribute.HK_FIELD_ID.toString()))).getId().toString());
//                                }
//                            }
//                            reportField.setCustomAttributes(gson.toJson(customAttrsMap));
                            newReportFields.add(reportField);
                        }
                        report.setRbReportFieldSet(newReportFields);
                    }
                    destinationReports.add(report);

                } catch (CloneNotSupportedException ex) {
                    Logger.getLogger(RbReportServiceImpl.class
                            .getName()).log(Level.SEVERE, null, ex);
                }

            }
            this.saveAllReports(destinationReports);
        }
    }

    @Override
    public RbEmailReportConfigurationEntity retrieveEmailConfigurationByReportId(Long reportId, Long userId) {
        RbEmailReportConfigurationEntity rbEmailReportConfigurationEntity = null;

        if (reportId != null) {
            Search search = new Search(RbEmailReportConfigurationEntity.class
            );
            search.addFilterEqual(EmailReportConfigurationEntity.REPORT_ID, reportId);

            search.addFilterEqual(EmailReportConfigurationEntity.CREATED_BY, userId);

            search.addFilterEqual(EmailReportConfigurationEntity.STATUS, HkSystemConstantUtil.ACTIVE);
            rbEmailReportConfigurationEntity = (RbEmailReportConfigurationEntity) commonDao.searchUnique(search);
        }
        return rbEmailReportConfigurationEntity;
    }

    @Override
    public void saveEmailConfiguration(RbEmailReportConfigurationEntity rbEmailReportConfigurationEntity) {
        if (rbEmailReportConfigurationEntity != null) {
            commonDao.save(rbEmailReportConfigurationEntity);
        }
    }

    @Override
    public RbEmailReportConfigurationEntity retrieveEmailConfigurationById(Long configId) {
        RbEmailReportConfigurationEntity rbEmailReportConfigurationEntity = null;

        if (configId != null) {
            Search search = new Search(RbEmailReportConfigurationEntity.class
            );
            search.addFilterEqual(EmailReportConfigurationEntity.ID, configId);
            rbEmailReportConfigurationEntity = (RbEmailReportConfigurationEntity) commonDao.searchUnique(search);
        }
        return rbEmailReportConfigurationEntity;
    }

    @Override
    public void saveEmailStatusEntities(List<RbEmailReportStatusEntity> rbEmailReportStatusEntitys) {
        if (!CollectionUtils.isEmpty(rbEmailReportStatusEntitys)) {
            commonDao.saveAll(rbEmailReportStatusEntitys);
        }
    }

    @Override
    public List<RbEmailReportConfigurationEntity> retrieveAllEmailConfigurations(boolean isReportRequired) {
        List<RbEmailReportConfigurationEntity> rbEmailReportConfigurationEntitys = null;
        Search search = new Search(RbEmailReportConfigurationEntity.class
        );
        if (isReportRequired) {
            search.addFetch(EmailReportConfigurationEntity.REPORT);
        }

        search.addFilterEqual(EmailReportConfigurationEntity.STATUS, HkSystemConstantUtil.ACTIVE);
        rbEmailReportConfigurationEntitys = commonDao.search(search);
        return rbEmailReportConfigurationEntitys;
    }

    @Override
    public List<RbEmailReportStatusEntity> retrievePendingReportEmailStatusTillDate(Date toDate) {
        List<RbEmailReportStatusEntity> rbEmailStatusEntitys = null;
        Search search = new Search(RbEmailReportStatusEntity.class
        );
        search.addFilterEqual(EmailReportStatusEntity.STATUS, HkSystemConstantUtil.ReportEmailStatus.PENDING);

        search.addFilterEqual(EmailReportStatusEntity.EMAIL_SENT, Boolean.FALSE);

        search.addFilterLessOrEqual(EmailReportStatusEntity.EMAIL_REPORT_STATUS_ENTITY_PK_ON_TIME, toDate);
        rbEmailStatusEntitys = commonDao.search(search);
        return rbEmailStatusEntitys;
    }
    
    @Override
    public List<RbEmailReportStatusEntity> retrieveReportEmailStatus(Long forUser, Date onTime, Long configurationId) {
        List<RbEmailReportStatusEntity> rbEmailStatusEntitys = null;
        Search search = new Search(RbEmailReportStatusEntity.class
        );
        if (onTime != null) {
            search.addFilterEqual(EmailReportStatusEntity.EMAIL_REPORT_STATUS_ENTITY_PK_ON_TIME, onTime);
        }
        if (configurationId != null) {
            search.addFilterEqual(EmailReportStatusEntity.EMAIL_REPORT_STATUS_ENTITY_PK_CONFIG_ID, configurationId);
        }
        if (forUser != null) {
            search.addFilterEqual(EmailReportStatusEntity.EMAIL_REPORT_STATUS_ENTITY_PK_FOR_USER, forUser);
        }
        rbEmailStatusEntitys = commonDao.search(search);
        return rbEmailStatusEntitys;
    }

    @Override
    public void saveEmailConfigurationEntities(List<RbEmailReportConfigurationEntity> rbEmailReportConfigurationEntitys) {
        if (!CollectionUtils.isEmpty(rbEmailReportConfigurationEntitys)) {
            commonDao.saveAll(rbEmailReportConfigurationEntitys);
        }
    }

    @Override
    public List<RbUserReportDashboardEntity> retrieveUserReportDashboardStatus(Long reportId, Long userId) {
        List<RbUserReportDashboardEntity> rbUserReportDashboardEntity = null;
        Search search = new Search(RbUserReportDashboardEntity.class
        );
        search.addFilterEqual(UserReportDashboardEntity.IS_ARCHIVE, Boolean.FALSE);
        search.addFilterEqual(UserReportDashboardEntity.STATUS, RbReportConstantUtils.DASHBOARD);
        if (reportId != null) {
            search.addFilterEqual(UserReportDashboardEntity.USER_REPORT_DASHBOARD_STATUS_ENTITY_PK_REPORT, reportId);
        }
        if (userId != null) {
            search.addFilterEqual(UserReportDashboardEntity.USER_REPORT_DASHBOARD_STATUS_ENTITY_PK_USER, userId);
        }
        rbUserReportDashboardEntity = commonDao.search(search);
        return rbUserReportDashboardEntity;
    }

    @Override
    public void updateUserReportDashboardStatus(RbUserReportDashboardEntity userReportDashboardEntity) {
        if (userReportDashboardEntity != null) {
            commonDao.save(userReportDashboardEntity);
        }
    }

    private String formatDate(Long longDate, Boolean isIncludeTime) throws ParseException {
        Date date = new Date(longDate);
        String timeFormat = isIncludeTime ? HkSystemConstantUtil.CUSTOM_FIELD_DATE_TIME_FORMAT : HkSystemConstantUtil.CUSTOM_FIELD_DATE_FORMAT;
        SimpleDateFormat dateFormatter = new SimpleDateFormat(timeFormat);
        String dates = dateFormatter.format(date);
        return dates;
    }

}
