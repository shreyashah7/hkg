/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.reportbuilder.database.hibernate;

import com.argusoft.reportbuilder.database.EntityMetadataDao;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

/**
 *
 * @author vipul
 */
@Repository
public class EntityMetadataDaoImpl implements EntityMetadataDao {

    @Autowired
    BasicDataSource dataSource;

    @Override
    public Map<String, String> retriveQueryMetadata(String query) {
        final Map<String, String> queryMetadataMap = new LinkedHashMap<>();
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        List<ResultSet> rsList = jdbcTemplate.query(query + " limit 1", new RowMapper<ResultSet>() {
            public ResultSet mapRow(ResultSet rs, int rowNum) throws SQLException {
                ResultSetMetaData rsmd = rs.getMetaData();
                for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                    queryMetadataMap.put(rsmd.getColumnName(i), rsmd.getColumnTypeName(i));
                }
                return rs;
            }
        }
        );
        jdbcTemplate = null;
        return queryMetadataMap;
    }

    @Override
    public Map<String, Object> retrievePartialResults(String queryString, Integer offSet, Integer limit) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        Map<String, Object> partialResults = new HashMap<>();
        int totalRecords = jdbcTemplate.queryForInt("select count(*) from (" + queryString + ")as temp");

        if (limit != null && offSet != null) {
            queryString += " limit " + limit + " offset " + offSet;
        }
//        List<Map<String, Object>> rows = jdbcTemplate.queryForList(queryString);
        final List<LinkedHashMap<String, Object>> resultList = new ArrayList<>();
        List<ResultSet> rows = jdbcTemplate.query(queryString, new RowMapper<ResultSet>() {
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
        jdbcTemplate = null;
        partialResults.put("totalRecords", totalRecords);
//        partialResults.put("records", rows);
        partialResults.put("records", resultList);
        return partialResults;

    }

    public List<List<String>> retrieveAllResults(String queryString) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        final List<List<String>> resultList = new LinkedList<>();
        List<Object> rows = jdbcTemplate.query(queryString, new RowMapper<Object>() {
            @Override
            public ResultSet mapRow(ResultSet rs, int i) throws SQLException {
                ResultSetMetaData resultSetMetaData = rs.getMetaData();
                List<String> row = new LinkedList<>();
                int count = 1;
                while (count <= resultSetMetaData.getColumnCount()) {
                    row.add(rs.getString(count));
                    count++;
                }
                resultList.add(row);
                return rs;
            }
        });
        return resultList;
    }

    @Override
    public List<String> retrieveTableNames() {
        List<String> listOfAllTables = new ArrayList<>();
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        List<Map<String, Object>> tablesName = jdbcTemplate.queryForList("SELECT table_name FROM information_schema.tables WHERE table_schema='public' AND table_type='BASE TABLE'");
        for (Map<String, Object> m : tablesName) {
            listOfAllTables.add((String) m.get("table_name"));
        }
        jdbcTemplate = null;
        return listOfAllTables;
    }

    @Override
    public List<String> retrieveTableColumns(String tableName) {
        List<String> listOfColumns = new ArrayList<>();
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        List<Map<String, Object>> columnsNames = jdbcTemplate.queryForList("select column_name from INFORMATION_SCHEMA.COLUMNS where table_name = '" + tableName + "'");
        for (Map<String, Object> m : columnsNames) {
            listOfColumns.add((String) m.get("column_name"));
        }
        jdbcTemplate = null;
        return listOfColumns;
    }

    @Override
    public Map<String, Object> retrieveDistinctColumnResults(String query, String distinctColumnName) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        Map<String, Object> distinctColumnResultMap = new HashMap<>();
        query = "select distinct(" + distinctColumnName + ") from (" + query + ")as temp";
        int totalRecords = jdbcTemplate.queryForInt("select count(*) from (" + query + ")as countTable");
        if (totalRecords <= 100) {
            List<String> data = jdbcTemplate.query(query, new RowMapper<String>() {
                public String mapRow(ResultSet rs, int rowNum)
                        throws SQLException {
                    return rs.getString(1);
                }
            });
            distinctColumnResultMap.put("columnValues", data);
        } else {
            distinctColumnResultMap.put("manyValues", totalRecords);
        }
        jdbcTemplate = null;
        return distinctColumnResultMap;
    }

    @Override
    public Map<String, Object> retrieveConvertedResult(String query) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        System.out.println("Queryyyyyyyyyyyy=" + query);
        List<Map<String, Object>> convertedResultsMapList = jdbcTemplate.queryForList(query);
        Map<String, Object> convertedResultMap = new HashMap<>();
        for (Map<String, Object> m : convertedResultsMapList) {
            System.out.println("map=" + m);

        }

        convertedResultMap.put("convertedData", convertedResultsMapList);
        return convertedResultMap;
    }

    @Override
    public Map<String, Object> retrieveAllTableWithColumnsListByTableList(List<String> tableList) {
        List<String> allTableNames = retrieveTableNames();
        Map<String, Object> tableNameWithColumnNameMap = new LinkedHashMap<>();
        for (String tableName : allTableNames) {
            if (tableList.contains(tableName)) {
                tableNameWithColumnNameMap.put(tableName, retrieveTableColumns(tableName));
            }
        }

        return tableNameWithColumnNameMap;
    }

    @Override
    public Map<String, Object> retrieveDistinctColumnValuesInRange(String query, String distinctColumnName, String searchTerm, Integer offSet, Integer limit) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        Map<String, Object> distinctColumnResultMap = new HashMap<>();
        query = "select distinct(" + distinctColumnName + ") from (" + query + ")as temp";
        String countQuery = "select count(*) from (" + query + ")as countTable";
//        int totalRecords = jdbcTemplate.queryForInt(countQuery);
        if (searchTerm != null) {
            query += " where " + distinctColumnName + " like '%" + searchTerm + "%'";
        }
        query += " limit " + limit + " offset " + offSet;
        List<String> data = jdbcTemplate.query(query, new RowMapper<String>() {
            public String mapRow(ResultSet rs, int rowNum)
                    throws SQLException {
                return rs.getString(1);
            }
        });
        Iterator<String> itr = data.iterator();
        while(itr.hasNext()){
            if(itr.next() == null){
                itr.remove();
            }
        }
        int totalRecords = data.size();
        distinctColumnResultMap.put("total", totalRecords);
        distinctColumnResultMap.put("columnValues", data);

        jdbcTemplate = null;
        return distinctColumnResultMap;
    }

}
