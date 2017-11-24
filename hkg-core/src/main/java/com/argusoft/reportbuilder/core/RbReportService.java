/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.reportbuilder.core;

import com.argusoft.reportbuilder.model.RbEmailReportConfigurationEntity;
import com.argusoft.reportbuilder.model.RbEmailReportStatusEntity;
import com.argusoft.reportbuilder.model.RbReport;
import com.argusoft.reportbuilder.model.RbReportField;
import com.argusoft.reportbuilder.model.RbTabularRelationEntity;
import com.argusoft.reportbuilder.model.RbUserReportDashboardEntity;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author vipul
 */
public interface RbReportService {

    /**
     * Mital: This method retrieves the table names which doesn't have
     * is_archive field as columns.
     *
     * @return Returns the table names with no is_archive field present in their
     * structure.
     */
    public Set<String> retrieveTablesWithIsArchiveFieldAbsent();

    /**
     * Mital: This method creates the foreign tables if they do not already
     * exist.
     *
     * @param tableNameSet Set of table names of foreign tables.
     */
    public void createForeignTables(Set<String> tableNameSet);

    /**
     * Gautam: This method will update Tabular relationship using xlsx file.
     */
    public void updateTabularRelationshipUsingXls();

    public Long createReport(RbReport rbReport);

    public RbReport retriveReportById(Long id, Boolean reportFieldSetRequired, Boolean reportAccessSetRequired);

    public List<RbReport> retriveAllReports(Boolean reportFieldSetRequired,Long companyId);

    public void updateReport(RbReport rbReport);

    public List<String> retrieveTableNames();

    public List<String> retrieveTableColumns(String tableName);

    public Map<String, Object> retrieveResults(String query, List<RbReportField> noSqlFieldList, Map<Long, String> featureNameIdMap, Set<Long> franchiseList);

    public Map<String, String> retriveQueryMetadata(String query);

    public Map<String, Object> retrieveDistinctColumnResults(String query, String distinctColumnName);

    public Map<String, Object> retrieveDistinctColumnValuesInRange(String query, String distinctColumnName, String searchTerm, Integer offSet, Integer limit);

    /**
     * This method store all the information of report in multiple table.
     *
     * @param rbReport
     * @param rbReportFieldList list of all field available in report.
     */
    public void createAllReportInformation(RbReport rbReport, List<RbReportField> rbReportFieldList);

    /**
     * This method returns all the column list of individual table specified in
     * table list.
     *
     * @param tableList
     * @return Map contains table name as key and value as list of columns.
     */
    public Map<String, Object> retrieveAllTableWithColumnsListByTableList(List<String> tableList);

    /**
     * To retrieve all the results of passed query
     *
     * @param query
     * @return List of records as List of string
     */
    public List<List<String>> retrieveAllResults(String query);

    /**
     * Mital: This method returns the relationship entities among given tables.
     *
     * @param tableNameList
     * @return
     */
    public List<RbTabularRelationEntity> retrieveRelationAmongTables(List<String> tableNameList, List<String> joinAttributeList);

    public List<RbTabularRelationEntity> retrieveCustomFieldRelationShip(List<String> customFieldTableName);
    
    public RbReport retrieveReportByFeature(Long featureId);
    
    public List<RbReport> retrieveExternalReportsByFeatureIds(List<Long> featureIds);
    
    public void copyReportsFromSourceToDestination(Long sourceFranchise,Long destinationFranchise);
    
    public void saveAllReports(List<RbReport> reports);
    
    public List<RbEmailReportConfigurationEntity> retrieveAllEmailConfigurations(boolean isReportRequired);
    
    public RbEmailReportConfigurationEntity retrieveEmailConfigurationById(Long configId);
    
    public RbEmailReportConfigurationEntity retrieveEmailConfigurationByReportId(Long reportId, Long userId);
    
    public void saveEmailConfiguration(RbEmailReportConfigurationEntity rbEmailReportConfigurationEntity);
    
    public void saveEmailConfigurationEntities(List<RbEmailReportConfigurationEntity> rbEmailReportConfigurationEntitys);
    
    public void saveEmailStatusEntities(List<RbEmailReportStatusEntity> rbEmailReportStatusEntitys);
    
    public List<RbEmailReportStatusEntity> retrievePendingReportEmailStatusTillDate(Date toDate);
    
    public List<RbEmailReportStatusEntity> retrieveReportEmailStatus(Long forUser, Date onTime, Long configurationId);
    
    public List<RbUserReportDashboardEntity> retrieveUserReportDashboardStatus(Long reportId, Long userId);
    
    public void updateUserReportDashboardStatus(RbUserReportDashboardEntity userReportDashboardEntity);
}
