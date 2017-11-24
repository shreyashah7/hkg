/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.reportbuilder.database;

import java.util.List;
import java.util.Map;

/**
 *
 * @author vipul
 */
public interface EntityMetadataDao {

    /**
     * To retrieve list of tables for conntected database
     *
     * @return List of all the table of database
     */
    public List<String> retrieveTableNames();

    /**
     * To retrieve list of columns of tables for particular
     *
     * @param tableName
     * @return List of all the table of database
     */
    public List<String> retrieveTableColumns(String tableName);

    /**
     * To retrieve Map of Query Metadata like columns name ,columns type for
     * particular query.Map contains columns name as key and columns type as
     * value.
     *
     * @param query
     * @return List of all the table of database
     */
    public Map<String, String> retriveQueryMetadata(String query);

    /**
     * To retrieve partial data from specified query.By providing offset and
     * limit .
     *
     * @param offset
     * @param limit
     * @return Map of partial table records of database.
     */
    public Map<String, Object> retrievePartialResults(String query, Integer offSet, Integer limit);

    /**
     * To retrieve distinct records of selected column from query for filtering
     *
     * @param query Pass query
     * @param distinctColumnName pass column name from query for which distinct
     * results are needed
     * @return returns map containing "columnValues" as a key which contains all
     * the values of column as List<String>
     * If there are more than 100 distinct values then it will return
     * "manyValues" as a key in map and total of distinct values as a value of
     * int type.
     */
    public Map<String, Object> retrieveDistinctColumnResults(String query, String distinctColumnName);

    public Map<String, Object> retrieveDistinctColumnValuesInRange(String query, String distinctColumnName, String searchTerm, Integer offSet, Integer limit);

    /**
     * To retrieve converted result after applying converter to table.i.e if
     * table contains value dept and we want to convert it as department.
     *
     * @param query
     * @return Map of converted records of table.
     */
    public Map<String, Object> retrieveConvertedResult(String query);

    /**
     * This method returns all the of database with column list of individual
     * table.
     *
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
}
