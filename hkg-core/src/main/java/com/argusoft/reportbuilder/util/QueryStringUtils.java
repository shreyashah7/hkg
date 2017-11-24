/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.reportbuilder.util;

import com.argusoft.hkg.common.constantutil.HkSystemConstantUtil;
import com.argusoft.reportbuilder.common.constantutil.RbReportConstantUtils;
import com.argusoft.reportbuilder.core.bean.ConverterDataBean;
import com.argusoft.reportbuilder.core.bean.MasterReportDataBean;
import com.argusoft.reportbuilder.core.bean.RbFieldDataBean;
import com.argusoft.reportbuilder.model.RbReportField;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeMap;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 *
 * @author vipul
 */
@Service
public class QueryStringUtils {

    public static Boolean validateSqlQuery(String query) {

        List<String> queryString = new ArrayList();
        Boolean flag = Boolean.FALSE;
        List<String> failureMsg = new ArrayList<>();
        StringTokenizer stringTokenizer = new StringTokenizer(query);
        List<String> restrictedList = new ArrayList();

        String trimQuery = query.replaceAll("\\s+", "");
        trimQuery = trimQuery.replaceAll("'", "");

        char prev;
        char next;
        if (trimQuery.toLowerCase().contains("information_schema")) {
            flag = Boolean.TRUE;
            failureMsg.add("information_schema not allowed");
        } else {
            for (int i = 0; i < trimQuery.length(); i++) {
                if (i != 0) {
                    prev = trimQuery.charAt((i - 1));
                    if (trimQuery.charAt(i) == '=') {
                        next = trimQuery.charAt((i + 1));
                        if (prev == next) {
                            flag = Boolean.TRUE;
                            failureMsg.add("pattern not allowed");
                        }
                    }
                }

            }
            if (!flag) {
                while (stringTokenizer.hasMoreElements()) {
                    String word = (String) stringTokenizer.nextElement();
                    queryString.add(word);
                }

                restrictedList.add("insert");
                restrictedList.add("update");
                restrictedList.add("delete");
                restrictedList.add("drop");
                restrictedList.add("truncate");
                restrictedList.add("alter");
                restrictedList.add("create");
                restrictedList.add("function");
                restrictedList.add("procedure");
                restrictedList.add("trigger");
                restrictedList.add("sequence");
                restrictedList.add("view");
                restrictedList.add("information_schema");
                restrictedList.add("information_schema.");
                restrictedList.add("values");
                restrictedList.add("into");

                List<String> restrictedCharacters = new ArrayList();
                restrictedCharacters.add("#");
                restrictedCharacters.add("/*");
                restrictedCharacters.add("*/");
                restrictedCharacters.add("**");
                restrictedCharacters.add("~");
                restrictedCharacters.add("`");
                restrictedCharacters.add("--");

                for (String queryPart : queryString) {
                    if (restrictedList.contains(queryPart) || restrictedList.contains(queryPart.toLowerCase())) {
                        failureMsg.add(queryPart);
                        flag = Boolean.TRUE;
                    }
                    for (String characterPart : restrictedCharacters) {
                        if (queryPart.contains(characterPart)) {
                            failureMsg.add(characterPart);
                            flag = Boolean.TRUE;
                        }
                    }
                }
            }
        }
        if (flag) {
            return Boolean.FALSE;
//            for (String queryPart : failureMsg) {
//                System.out.println("Your query string is incorrect."+ queryPart + " not allowed");
//            }

        } else {
            return Boolean.TRUE;
//            System.out.println("Your query string is correct...........");
        }

    }
    /*
     * To retrieve converted query after applying converter to table.i.e if
     * table contains value dept and we want to convert it as department.
     *
     * @param query
     * @param coverterKeyValue
     * @return String of converted query.
     */

    public static String changeQueryByConfiguration(MasterReportDataBean masterReportDataBean) {
        if (masterReportDataBean != null) {
            List<RbFieldDataBean> fieldDataBeansList = masterReportDataBean.getColumns();
            String replaceByConverter = "";
            String filterString = "";
            if (masterReportDataBean.getQuery() != null && masterReportDataBean.getQuery().toLowerCase().contains("where")) {
                filterString = " AND ";
            } else {
                filterString = " WHERE ";
            }
            String appendedQuery = masterReportDataBean.getQuery();
            if (!StringUtils.isEmpty(appendedQuery)) {
                StringTokenizer totalFields = new StringTokenizer(appendedQuery.substring(7, appendedQuery.indexOf("from")), ",");
                List<String> totalFieldList = new ArrayList();
                while (totalFields.hasMoreElements()) {
                    String element = (String) totalFields.nextElement();
                    if (element.contains("(") && !element.contains(")")) {
                        while (totalFields.hasMoreElements()) {
                            String secondElement = (String) totalFields.nextElement();
                            element += secondElement;
                            if (secondElement.contains(")")) {
                                break;
                            }
                        }
                    }
                    totalFieldList.add(element);
                }
//                System.out.println("Field=" + totalFieldList);
                //Remove custom Fields
                if (fieldDataBeansList != null) {
                    Iterator<RbFieldDataBean> itr = fieldDataBeansList.iterator();
                    while (itr.hasNext()) {
                        RbFieldDataBean fieldDataBean = itr.next();
                        if (fieldDataBean.getDbBaseType().equals("MDB")) {
                            itr.remove();
                        }
                    }
                }
                Boolean isAnotherFilter = Boolean.FALSE;
                int i = 0;
                if (fieldDataBeansList != null) {
                    for (RbFieldDataBean fieldDataBean : fieldDataBeansList) {
                        //Set coli18n Flag
                        if (fieldDataBean.isColI18nRequired() == null) {
                            fieldDataBean.setColI18nRequired(Boolean.FALSE);
                        }
//                        Set alias null by default
                        if (fieldDataBean.getAlias() != null && fieldDataBean.getAlias().equals("")) {
                            fieldDataBean.setAlias(null);
                        }
                        if (fieldDataBean.getConverterDataBeanList() != null && fieldDataBean.getConverterDataBeanList().size() > 0) {

                            String fieldWitoutAlias = totalFieldList.get(i).toLowerCase();
                            if (totalFieldList.get(i).toLowerCase().contains(" as ")) {
                                fieldWitoutAlias = fieldWitoutAlias.substring(0, fieldWitoutAlias.indexOf(" as "));
                            }

                            replaceByConverter += "(CASE " + fieldWitoutAlias;
                            for (ConverterDataBean converterDataBean : fieldDataBean.getConverterDataBeanList()) {

                                replaceByConverter += " WHEN '" + converterDataBean.getKey() + "' THEN '" + converterDataBean.getValue() + "'";

                            }
//                        if (fieldDataBean.getAlias() != null) {
//                            replaceByConverter += " ELSE cast(" + fieldWitoutAlias + " as varchar) END) as \"" + fieldDataBean.getAlias() + "\" ";
//                        } else {
                            replaceByConverter += " ELSE cast(" + fieldWitoutAlias + " as varchar) END) as " + fieldDataBean.getColName() + " ";
//                        }

                            if (fieldDataBeansList.size() > 1 && (i == 0 || i < fieldDataBeansList.size() - 1)) {
                                replaceByConverter += " ,";
                            }

                        } else {
                            if (fieldDataBean.getFormat() != null) {
                                String fieldWitoutAlias = totalFieldList.get(i).toLowerCase();
                                if (totalFieldList.get(i).toLowerCase().contains(" as ")) {
                                    fieldWitoutAlias = fieldWitoutAlias.substring(0, fieldWitoutAlias.indexOf(" as ") - 1);
                                }
//                                System.out.println("fieldDataBean.getFormat()-- : " + fieldDataBean.getFormat().toLowerCase());
                                if (fieldDataBean.getDataType().toLowerCase().equals("varchar") && fieldDataBean.getFormat().toLowerCase().equals("minimum text")) {

//                                if (fieldDataBean.getAlias() != null) {
//                                    replaceByConverter += "substr(" + fieldWitoutAlias + "," + 1 + "," + RbReportConstantUtils.MINI_TEXT + ") as \"" + fieldDataBean.getAlias() + "\" ";
//
//                                } else {
                                    replaceByConverter += "substr(" + fieldWitoutAlias + "," + 1 + "," + RbReportConstantUtils.MINI_TEXT + ") as " + fieldDataBean.getColName() + " ";
//                                }

                                } else {
//                                    System.out.println("inside else if---");
//                                if (fieldDataBean.getAlias() != null) {
//                                    if (fieldDataBean.isColI18nRequired()) {
//                                        replaceByConverter += " translate(cast(to_char(" + fieldWitoutAlias + ",'" + fieldDataBean.getFormat() + "') as character varying),'0123456789','૦૧૨૩૪૫૬૭૮૯') as \"" + fieldDataBean.getAlias() + "\" ";
//                                    } else {
//                                        replaceByConverter += "to_char(" + fieldWitoutAlias + ",'" + fieldDataBean.getFormat() + "') as \"" + fieldDataBean.getAlias() + "\" ";
//                                    }
//
//                                } else {
                                    if (fieldDataBean.isColI18nRequired()) {
                                        replaceByConverter += " translate(cast(to_char(" + fieldWitoutAlias + ",'" + fieldDataBean.getFormat() + "') as character varying),'0123456789','૦૧૨૩૪૫૬૭૮૯') as " + fieldDataBean.getColName();
                                    } else {
                                        replaceByConverter += "substr(" + fieldWitoutAlias + "," + 1 + "," + RbReportConstantUtils.FULL_TEXT + ") as " + fieldDataBean.getColName() + " ";
//                                        replaceByConverter += "to_char(" + fieldWitoutAlias + ",'" + fieldDataBean.getFormat() + "') as " + fieldDataBean.getColName() + " ";
                                    }

//                                }
                                }
                            } else {
                                if (fieldDataBean.isColI18nRequired() && fieldDataBean.getFormat() == null) {

//                                if (fieldDataBean.getAlias() != null) {
//                                    replaceByConverter += " translate(cast(" + totalFieldList.get(i) + " as character varying),'0123456789','૦૧૨૩૪૫૬૭૮૯') as \"" + fieldDataBean.getAlias() + "\" ";
//                                } else {
                                    replaceByConverter += " translate(cast(" + totalFieldList.get(i) + " as character varying),'0123456789','૦૧૨૩૪૫૬૭૮૯') as " + fieldDataBean.getColName() + " ";
//                                }

                                } else {
                                    replaceByConverter += totalFieldList.get(i) + " ";
                                }
                            }
                            if (fieldDataBeansList.size() > 1 && (i == 0 || i < fieldDataBeansList.size() - 1)) {
                                replaceByConverter += " ,";
                            }

                        }
//                    if (fieldDataBean.getFilter() != null) {
//                        if (isAnotherFilter) {
////                            filterString += " AND " + totalFieldList.get(i) + " IN (" + bean.getFilter().toString().replaceAll("\\[|\\]", "") + ")";
////                            filterString += " AND " + totalFieldList.get(i) + bean.getFilter();
//                            filterString += " AND " + fieldDataBean.getColName() + fieldDataBean.getFilter();
//                        } else {
////                            filterString += totalFieldList.get(i) + " IN ('" + bean.getFilter().toString().replaceAll("\\[|\\]", "").replaceAll("\\, ", "','") + "')";
////                            filterString += totalFieldList.get(i) + bean.getFilter();
//                            filterString += fieldDataBean.getColName() + fieldDataBean.getFilter();
//                            isAnotherFilter = Boolean.TRUE;
//                        }
//                    }
                        i++;
                    }
                    replaceByConverter += " ";
                    StringBuffer sb = new StringBuffer(masterReportDataBean.getQuery());
                    String convertedQuery = masterReportDataBean.getQuery().replaceFirst(sb.substring("select".length() + 1, sb.indexOf("from")), replaceByConverter);
                    String orderByString = "";
                    if (convertedQuery.toLowerCase().contains("order by")) {
                        orderByString = convertedQuery.substring(convertedQuery.toLowerCase().lastIndexOf("order by"));
                        convertedQuery = convertedQuery.replace(convertedQuery.substring(convertedQuery.toLowerCase().lastIndexOf("order by")), "");
                    }

                    if (isAnotherFilter) {
                        convertedQuery += filterString;
                    }
//            if (masterReportDataBean.getOrderList() != null) {
//                String orderByString = masterReportDataBean.getOrderList().toString().replaceAll("\\[|\\]", "");
//                convertedQuery += " ORDER BY " + orderByString;
//            }
                    convertedQuery += orderByString;
//                    System.out.println("Final Query=" + convertedQuery.trim());
                    return convertedQuery.trim();
                }
            }

        }
        return null;

    }

//    public static void main(String[] args) {
//   //     QueryStringUtils.validateSqlQuery("select id from tables");
//
//        //   private Long id;
//   /* private String query;
//         private List<String> orderList;
//         private List<FieldDataBean> columns;
//         private String description;
//         private String reportName;
//         private Boolean externalReport;
//         private Boolean editable;
//         private String reportCode;*/
//        MasterReportDataBean bean = new MasterReportDataBean();
//
//        /*  private String colName;
//         private List<ConverterDataBean> converterDataBeanList;
//         private String filter;
//         private String format;
//         private String dataType;
//         private String alias;*/
//        List<FieldDataBean> columns = new ArrayList();
//
//        FieldDataBean field1 = new FieldDataBean();
//        List<ConverterDataBean> conv1 = null;//new ArrayList<>();
//        ConverterDataBean c1 = new ConverterDataBean();
//
//        c1.setKey(null);
//        c1.setValue(null);
////        conv1.add(c1);
//
//        field1.setColName("create_on");
//        field1.setAlias("tarikh");
////        field1.setFilter();
//        field1.setFormat("dd-mm-yy");
//        field1.setDataType("timpstamp");
//        //   field1.setColI18nRequired(Boolean.TRUE);
//        field1.setConverterDataBeanList(conv1);
//
//        FieldDataBean field2 = new FieldDataBean();
//        List<ConverterDataBean> conv2 = new ArrayList<>();
//        ConverterDataBean c2 = new ConverterDataBean();
//
//        c2.setKey(null);
//        c2.setValue(null);
//        conv2.add(c2);
//
//        field2.setColName(null);
//        field2.setAlias(null);
//        field2.setFilter(null);
//        field2.setFormat(null);
//        field2.setDataType(null);
//
//        field2.setConverterDataBeanList(conv2);
//
//        FieldDataBean field3 = new FieldDataBean();
//        List<ConverterDataBean> conv3 = new ArrayList<>();
//        ConverterDataBean c3 = new ConverterDataBean();
//
//        c3.setKey(null);
//        c3.setValue(null);
//        conv3.add(c3);
//
//        field3.setColName(null);
//        field3.setAlias(null);
//        field3.setFilter(null);
//        field3.setFormat(null);
//        field3.setDataType(null);
//
//        field3.setConverterDataBeanList(conv3);
//
//        columns.add(field1);
//        //columns.add(field2);
//        //columns.add(field3);
//
//        bean.setQuery("select rb_report_mst.created_on from rb_report_mst");
//        bean.setColumns(columns);
//
//        QueryStringUtils.changeQueryByConfiguration(bean);
//
//    }
    /**
     * Mital: This method retrieves the query based on given table and column
     * names, order by params and join columns.
     *
     * @param tableColumnsMap Map of tables and columns. Eg.key = "rb_user" and
     * value = {"first_name","user_id"}
     * @param orderByColumns [NOT BEING USED IN METHOD FOR NOW] Map of columns
     * and order by enum. Eg. key = "rb_user.first_name" and value = OrderBy.ASC
     * @return Returns the sql query.
     */
    public static String retrieveSQLQuery(Map<String, Map<String, Integer>> tableColumnsMap, Map<String, RbReportConstantUtils.OrderBy> orderByColumns) {
        String queryStr = null;

        Map<Integer, String> resultMapWithSeq = new TreeMap<>();
        if (!CollectionUtils.isEmpty(tableColumnsMap)) {
            for (String tableName : tableColumnsMap.keySet()) {
                Map<String, Integer> columnsMap = tableColumnsMap.get(tableName);
                for (Map.Entry<String, Integer> entry : columnsMap.entrySet()) {
                    String columnDefinition = tableName + "." + entry.getKey();
                    Integer seq = entry.getValue();
                    resultMapWithSeq.put(seq, columnDefinition);
                }
            }
        }

        if (!CollectionUtils.isEmpty(resultMapWithSeq)) {
            Set<String> tableNames = new HashSet<>();
            queryStr = "select ";
            String tableNameStr = "";
            for (Integer seq : resultMapWithSeq.keySet()) {
                tableNames.add(resultMapWithSeq.get(seq).trim().split("\\.")[0]);
                queryStr += resultMapWithSeq.get(seq).trim() + ", ";
            }
            for (String tableName : tableNames) {
                tableNameStr += tableName + ", ";
            }
            tableNameStr = tableNameStr.substring(0, tableNameStr.length() - 2);
            queryStr = queryStr.substring(0, queryStr.length() - 2);
            queryStr += " from " + tableNameStr;
        }
        return queryStr;
    }

    /**
     * Mital: This method returns the SQL query for the rdbms fields.
     *
     * @param sqlFieldList The list of fields of RDBMS tables.
     * @param joinColumnRelationship Relationship in JSON format.
     * @param visibilityStatus Status mentioning if to hide diamond data.
     * @return Returns the SQL query string. Returns null if inappropriate input
     * found.
     */
    public static String retrieveSQLQuery(List<RbReportField> sqlFieldList, String joinColumnRelationship, Boolean visibilityStatus, Boolean isNormalSubFormValue, Boolean viewCurrencyDataPermission) {
        boolean avoidNoSQLFields = false;
        String query = null;
        int intermiadiateCount = 11;
        if (!CollectionUtils.isEmpty(sqlFieldList)) {
            String primaryTable = null;
            Boolean isDistinctRequired = Boolean.FALSE;
            Boolean isAddressFieldIncluded = Boolean.FALSE;
            Set<String> columnNameSet = new LinkedHashSet<>();
            Set<String> groupByColumnSet = new HashSet<>();
            Set<String> filterSet = new HashSet<>();
            Set<String> allDbBaseNameSet = new HashSet<>();
            Set<String> userMultiSelectConditions = new HashSet<>();
            Map<String, String> joinColumnRelMap = new LinkedHashMap<>();
            Map<String, String> orderedJoinColumnRelMap = new LinkedHashMap<>();
            Map<String, String> aliasToDbBaseNameMap = new HashMap<>();

            //Initialize Set of diamond tables.
            Set<String> diamondTableSet = new HashSet<>();
            diamondTableSet.add("invoice");
            diamondTableSet.add("parcel");
            diamondTableSet.add("lot");
            diamondTableSet.add("packet");
            diamondTableSet.add("workallotment");
            //Initialize Conversion Map for fields.
            Map<String, Object> conversionMap = RbReportConstantUtils.Converter.FK_CONVERSION_MAP;
            //Initialize Parent table map.
            Map<String, Object> parentTableMap = RbReportConstantUtils.FranchiseRelationship.PARENT_TABLE_MAP;
            //Initialize FranchiseColumnType map.
            Map<String, String> franchiseColumnTypeMap = RbReportConstantUtils.FranchiseRelationship.FRANCHISE_COLUMN_TYPE;
            //Initialize Static Conversion map.
            Map<String, Object> staticConversionMap = RbReportConstantUtils.Converter.STATIC_CONVERSION_MAP;
            //Initialize User Relation map.
            Map<String, Object> specialRelationMap = RbReportConstantUtils.SpecialRelations.USER_RELATIONSHIP_MAP;
            //Initialize Address Relation map.
            Map<String, Object> addressRelationMap = RbReportConstantUtils.SpecialRelations.ADDRESS_RELATION_MAP;
            //Initialize Special condition map to restrict is archive like records.
            Map<String, Object> specialConditionMap = RbReportConstantUtils.SpecialRelations.SPECIAL_CONDITION_MAP;
            //Initialize intra table conversion map.
            Map<String, String> intraTableConversionMap = RbReportConstantUtils.Converter.INTRA_TABLE_CONVERSION_MAP;
            //Initialize inner document base name map.
            Map<String, String> innerDocumentToBaseNameMap = RbReportConstantUtils.INNER_DOCUMENT_BASE_NAME_MAP;
            //Initialize set of table names which got unique user multi select fields.
            Set<String> userMultiSelectExceptionSet = RbReportConstantUtils.Converter.USER_MULTISELECT_UNIQUE_SET;

            //Add user defined Relationship in feature level.
            if (!StringUtils.isEmpty(joinColumnRelationship)) {
                Type type = new TypeToken<List<Map<String, Object>>>() {
                }.getType();
                List<Map<String, Object>> joinAttributesList = (new Gson()).fromJson(joinColumnRelationship, type);
                int index = 0;
                for (Map<String, Object> joinAttributesMap : joinAttributesList) {
                    if (index == 0 && joinAttributesMap.get("joinAttributes") == null) {
                        for (RbReportField reportField : sqlFieldList) {
                            if (Objects.equals(reportField.getFeatureId(), new Double(joinAttributesMap.get("featureId").toString()).longValue())) {
                                String customAttributes = reportField.getCustomAttributes();
                                Boolean isSubFormValue = false;
                                String parentDbBaseName = null;
                                if (!StringUtils.isEmpty(customAttributes)) {
                                    Type subType = new TypeToken<Map<String, Object>>() {
                                    }.getType();
                                    Map<String, Object> customAttributesMap = (new Gson()).fromJson(reportField.getCustomAttributes(), subType);
                                    if (!CollectionUtils.isEmpty(customAttributesMap)) {
                                        if (customAttributesMap.get(RbReportConstantUtils.CustomAttribute.HK_PARENT_DB_BASE_NAME.toString()) != null) {
                                            parentDbBaseName = customAttributesMap.get(RbReportConstantUtils.CustomAttribute.HK_PARENT_DB_BASE_NAME.toString()).toString();
                                        }

                                        if (customAttributesMap.get(RbReportConstantUtils.CustomAttribute.IS_SUB_FORM_VALUE.toString()) != null) {
                                            isSubFormValue = (Boolean) customAttributesMap.get(RbReportConstantUtils.CustomAttribute.IS_SUB_FORM_VALUE.toString());
                                        }
                                    }
                                }
                                if (isSubFormValue && isNormalSubFormValue && !StringUtils.isEmpty(parentDbBaseName)) {
                                    primaryTable = parentDbBaseName;
                                } else {
                                    primaryTable = reportField.getDbBaseName();
                                }
                                //Check if this is any inner document field.
                                String colDef = reportField.getDbBaseName() + "." + reportField.getDbFieldName();
                                if (!CollectionUtils.isEmpty(innerDocumentToBaseNameMap)) {
                                    if (innerDocumentToBaseNameMap.containsKey(reportField.getDbBaseName().trim())) {
                                        primaryTable = innerDocumentToBaseNameMap.get(reportField.getDbBaseName());
                                    }
                                }
                                break;
                            }
                        }
                    }
                    if (joinAttributesMap.get("joinAttributes") != null) {
                        String joinAttribute = (String) joinAttributesMap.get("joinAttributes");
                        String leftRelationship = joinAttribute.split("=")[0].trim();
                        String rightRelationship = joinAttribute.split("=")[1].trim();
                        String leftRelDataType = null;
                        String rightRelDataType = null;
                        String leftRelComponentType = null;
                        String rightRelComponentType = null;
                        if (joinAttributesMap.get("localDataType") != null) {
                            leftRelDataType = (String) joinAttributesMap.get("localDataType");
                        }
                        if (joinAttributesMap.get("refDataType") != null) {
                            rightRelDataType = (String) joinAttributesMap.get("refDataType");
                        }
                        if (joinAttributesMap.get("localComponentType") != null) {
                            leftRelComponentType = (String) joinAttributesMap.get("localComponentType");
                        }
                        if (joinAttributesMap.get("refComponentType") != null) {
                            rightRelComponentType = (String) joinAttributesMap.get("refComponentType");
                        }
                        //Substitute address and special relation fields to original if provided by user.
                        if (!CollectionUtils.isEmpty(addressRelationMap) && addressRelationMap.get(leftRelationship) != null) {
                            type = new TypeToken<Map<String, String>>() {
                            }.getType();
                            Map<String, String> fieldAddressRelationMap = (new Gson()).fromJson(addressRelationMap.get(leftRelationship).toString(), type);
                            String addressTable = fieldAddressRelationMap.get(RbReportConstantUtils.AddressKey.ADDRESS_TABLE.toString());
                            String addressColumn = fieldAddressRelationMap.get(RbReportConstantUtils.AddressKey.ADDRESS_COLUMN.toString());
                            leftRelationship = addressTable + "." + addressColumn;
                        } else if (!CollectionUtils.isEmpty(specialRelationMap) && specialRelationMap.get(leftRelationship) != null) {
                            type = new TypeToken<Map<String, String>>() {
                            }.getType();
                            Map<String, String> fieldSpecialRelationMap = (new Gson()).fromJson(specialRelationMap.get(leftRelationship).toString(), type);
                            String fkTable = fieldSpecialRelationMap.get(RbReportConstantUtils.JoinAttributeKey.FK_TABLE.toString());
                            String showColumn = fieldSpecialRelationMap.get(RbReportConstantUtils.JoinAttributeKey.SHOW_COLUMN.toString());
                            leftRelationship = fkTable + "." + showColumn;
                        }
                        if (!CollectionUtils.isEmpty(addressRelationMap) && addressRelationMap.get(rightRelationship) != null) {
                            type = new TypeToken<Map<String, String>>() {
                            }.getType();
                            Map<String, String> fieldAddressRelationMap = (new Gson()).fromJson(addressRelationMap.get(rightRelationship).toString(), type);
                            String addressTable = fieldAddressRelationMap.get(RbReportConstantUtils.AddressKey.ADDRESS_TABLE.toString());
                            String addressColumn = fieldAddressRelationMap.get(RbReportConstantUtils.AddressKey.ADDRESS_COLUMN.toString());
                            rightRelationship = addressTable + "." + addressColumn;
                        } else if (!CollectionUtils.isEmpty(specialRelationMap) && specialRelationMap.get(rightRelationship) != null) {
                            type = new TypeToken<Map<String, String>>() {
                            }.getType();
                            Map<String, String> fieldSpecialRelationMap = (new Gson()).fromJson(specialRelationMap.get(rightRelationship).toString(), type);
                            String fkTable = fieldSpecialRelationMap.get(RbReportConstantUtils.JoinAttributeKey.FK_TABLE.toString());
                            String showColumn = fieldSpecialRelationMap.get(RbReportConstantUtils.JoinAttributeKey.SHOW_COLUMN.toString());
                            rightRelationship = fkTable + "." + showColumn;
                        }
                        String tableName = leftRelationship.split("\\.")[0].trim();
                        String refTableName = rightRelationship.split("\\.")[0].trim();
                        if ((leftRelComponentType == null || !leftRelComponentType.equals(HkSystemConstantUtil.CustomField.ComponentType.USER_MULTISELECT))
                                && (rightRelComponentType == null || !rightRelComponentType.equals(HkSystemConstantUtil.CustomField.ComponentType.USER_MULTISELECT))) {
                            if (leftRelDataType != null) {
                                if (leftRelDataType.equals("timestamp")) {
                                    leftRelationship = "TO_TIMESTAMP((" + leftRelationship.split("\\.")[0].trim() + ".\"fieldValue\"->>'" + leftRelationship.split("\\.")[1].trim() + "')::numeric/1000)";
                                } else {
                                    leftRelationship = leftRelationship.split("\\.")[0].trim() + ".\"fieldValue\"->>'" + leftRelationship.split("\\.")[1].trim() + "'::" + leftRelDataType;
                                }
                            }
                            if (rightRelDataType != null) {
                                if (rightRelDataType.equals("timestamp")) {
                                    rightRelationship = "TO_TIMESTAMP((" + rightRelationship.split("\\.")[0].trim() + ".\"fieldValue\"->>'" + rightRelationship.split("\\.")[1].trim() + "')::numeric/1000)";
                                } else {
                                    rightRelationship = rightRelationship.split("\\.")[0].trim() + ".\"fieldValue\"->>'" + rightRelationship.split("\\.")[1].trim() + "'::" + rightRelDataType;
                                }
                            }
                            joinColumnRelMap.put(tableName, leftRelationship + "=" + rightRelationship);
                        } else {
                            //If only left.
                            if ((leftRelComponentType != null && leftRelComponentType.equals(HkSystemConstantUtil.CustomField.ComponentType.USER_MULTISELECT))
                                    && (rightRelComponentType == null || !rightRelComponentType.equals(HkSystemConstantUtil.CustomField.ComponentType.USER_MULTISELECT))) {
                                String intermediateTableName = "intermediate" + (intermiadiateCount++);
                                String subQuery = new String(RbReportConstantUtils.userMultiselectSubQuery);
                                subQuery = subQuery.replace("???", tableName);
                                subQuery = subQuery.replace("~??~", leftRelationship.split("\\.")[1].trim());
                                subQuery = subQuery.replace("~table~", intermediateTableName);
                                joinColumnRelMap.put(subQuery, intermediateTableName + ".userid" + "=" + refTableName + "." + rightRelationship.split("\\.")[1].trim());
                                joinColumnRelMap.put(tableName, tableName + "._id" + "=" + intermediateTableName + ".objid");
                            } //If only right.
                            else if ((rightRelComponentType != null && rightRelComponentType.equals(HkSystemConstantUtil.CustomField.ComponentType.USER_MULTISELECT))
                                    && (leftRelComponentType == null || !leftRelComponentType.equals(HkSystemConstantUtil.CustomField.ComponentType.USER_MULTISELECT))) {
                                String intermediateTableName = "intermediate" + (intermiadiateCount++);
                                String subQuery = new String(RbReportConstantUtils.userMultiselectSubQuery);
                                subQuery = subQuery.replace("???", refTableName);
                                subQuery = subQuery.replace("~??~", rightRelationship.split("\\.")[1].trim());
                                subQuery = subQuery.replace("~table~", intermediateTableName);
                                joinColumnRelMap.put(subQuery, intermediateTableName + ".objid" + "=" + refTableName + "._id");
                                joinColumnRelMap.put(tableName, tableName + "." + leftRelationship.split("\\.")[1].trim() + "=" + intermediateTableName + ".userid");
                            } 
                            //Both sides
                            else {
                                String intermediateTableName = "intermediate" + (intermiadiateCount++);
                                String rightSubQuery = new String(RbReportConstantUtils.userMultiselectSubQuery);
                                rightSubQuery = rightSubQuery.replace("???", refTableName);
                                rightSubQuery = rightSubQuery.replace("~??~", rightRelationship.split("\\.")[1].trim());
                                rightSubQuery = rightSubQuery.replace("~table~", intermediateTableName);
                                joinColumnRelMap.put(rightSubQuery, intermediateTableName + ".objid" + "=" + refTableName + "._id");

                                String nextIntermediateTableName = "intermediate" + (intermiadiateCount++);
                                String leftSubQuery = new String(RbReportConstantUtils.userMultiselectSubQuery);
                                leftSubQuery = leftSubQuery.replace("???", tableName);
                                leftSubQuery = leftSubQuery.replace("~??~", leftRelationship.split("\\.")[1].trim());
                                leftSubQuery = leftSubQuery.replace("~table~", nextIntermediateTableName);
                                joinColumnRelMap.put(leftSubQuery, nextIntermediateTableName + ".userid" + "=" + intermediateTableName + ".userid");
                                joinColumnRelMap.put(tableName, tableName + "._id" + "=" + nextIntermediateTableName + ".objid");
                            }
                        }

                        //if child table is selected in join attributes
                        String parentTable = leftRelationship.split("\\.")[0].trim();
                        if (!CollectionUtils.isEmpty(parentTableMap) && parentTableMap.containsKey(parentTable)) {
                            type = new TypeToken<Map<String, String>>() {
                            }.getType();
                            String mainTable = parentTable;
                            while (parentTableMap.containsKey(mainTable)) {
                                Map<String, String> primaryParentTableMap = (new Gson()).fromJson(parentTableMap.get(mainTable).toString(), type);
                                if (!CollectionUtils.isEmpty(primaryParentTableMap)) {
                                    parentTable = primaryParentTableMap.get(RbReportConstantUtils.FranchiseRelationship.PARENT_TABLE);
                                    String parentColumn = primaryParentTableMap.get(RbReportConstantUtils.FranchiseRelationship.PARENT_COLUMN);
                                    String localColumn = primaryParentTableMap.get(RbReportConstantUtils.JoinAttributeKey.LOCAL_COLUMN.toString());
                                    if (localColumn.equals("instanceId")) {
                                        localColumn = "\"" + localColumn + "\"";
                                    }
                                    joinColumnRelMap.put(mainTable, mainTable + "." + localColumn + "=" + parentTable + "." + parentColumn);
                                    mainTable = parentTable;
                                } else {
                                    break;
                                }
                            }
                        }
                        //if child table is selected in join attributes
                        parentTable = rightRelationship.split("\\.")[0].trim();
                        if (!CollectionUtils.isEmpty(parentTableMap) && parentTableMap.containsKey(parentTable)) {
                            type = new TypeToken<Map<String, String>>() {
                            }.getType();
                            String mainTable = parentTable;
                            while (parentTableMap.containsKey(mainTable)) {
                                Map<String, String> primaryParentTableMap = (new Gson()).fromJson(parentTableMap.get(mainTable).toString(), type);
                                if (!CollectionUtils.isEmpty(primaryParentTableMap)) {
                                    parentTable = primaryParentTableMap.get(RbReportConstantUtils.FranchiseRelationship.PARENT_TABLE);
                                    String parentColumn = primaryParentTableMap.get(RbReportConstantUtils.FranchiseRelationship.PARENT_COLUMN);
                                    String localColumn = primaryParentTableMap.get(RbReportConstantUtils.JoinAttributeKey.LOCAL_COLUMN.toString());
                                    if (localColumn.equals("instanceId")) {
                                        localColumn = "\"" + localColumn + "\"";
                                    }
                                    joinColumnRelMap.put(mainTable, mainTable + "." + localColumn + "=" + parentTable + "." + parentColumn);
                                    mainTable = parentTable;
                                } else {
                                    break;
                                }
                            }
                        }
                    }
                }
            }

            Iterator<RbReportField> fieldItr = sqlFieldList.iterator();
            //Used for alias in case we need to join same tables like u1, u2 etc.
            int countTableAlias = 1;
            //Used for alias in special relationship.
            int countSpecialCaseAlias = 0;
            while (fieldItr.hasNext()) {
                RbReportField rbReportField = fieldItr.next();
                //  Check if the field is of relational database
                String reportFieldName = "\"" + (StringUtils.isEmpty(rbReportField.getReportFieldName())
                        ? rbReportField.getFieldLabel() : rbReportField.getReportFieldName()) + "\"";
                boolean isAggrigationNeeded = false;

                //Aggregation code
                if (RbReportConstantUtils.AggragateFields.contains(rbReportField.getDbBaseName())) {
                    isAggrigationNeeded = true;
                } else {
                    groupByColumnSet.add(reportFieldName);
                }
                if (rbReportField.getDbBaseType().equals(RbReportConstantUtils.DBBaseType.RELATIONAL_DB)) {
                    boolean multipleValues = false;

                    //  Let's store joins
                    if (StringUtils.isEmpty(rbReportField.getJoinAttributes())) {
                        //  When there is no join attributes defined, means that field's table is the primary table
                        //  from which the data will be fetched
//                        primaryTable = rbReportField.getDbBaseName();
                    } else {
                        //Check if Any user defined relationship exists on this field table.
                        if (joinColumnRelMap.get(rbReportField.getDbBaseName()) == null) {
                            //  if join attributes defined, we'll store it and use it in query
                            Type type = new TypeToken<Map<String, Object>>() {
                            }.getType();
                            Map<String, Object> joinAttributesMap = (new Gson()).fromJson(rbReportField.getJoinAttributes(), type);
                            String relType = (String) joinAttributesMap.get(RbReportConstantUtils.JoinAttributeKey.REL_TYPE.toString());
                            //  if relationship is many to many or one to many, means we'll have to aggregate the values in a single row
                            if (relType.equals(RbReportConstantUtils.RelType.ONE_TO_MANY)
                                    || relType.equals(RbReportConstantUtils.RelType.MANY_TO_MANY)) {
                                multipleValues = true;
                            }

                            if (StringUtils.isEmpty(joinAttributesMap.get(RbReportConstantUtils.JoinAttributeKey.JOIN_TABLE.toString()))) {
                                String localColumn = rbReportField.getDbBaseName()
                                        + ".\"" + joinAttributesMap.get(RbReportConstantUtils.JoinAttributeKey.LOCAL_COLUMN.toString()) + "\"";
                                String joinedColumn = joinAttributesMap.get(RbReportConstantUtils.JoinAttributeKey.FK_TABLE.toString())
                                        + ".\"" + joinAttributesMap.get(RbReportConstantUtils.JoinAttributeKey.FK_COLUMN.toString()) + "\"";
                                joinColumnRelMap.put(rbReportField.getDbBaseName().trim(), localColumn + " = " + joinedColumn);
                            } else {
                                String firstJoinedColumn = joinAttributesMap.get(RbReportConstantUtils.JoinAttributeKey.FK_TABLE.toString())
                                        + ".\"" + joinAttributesMap.get(RbReportConstantUtils.JoinAttributeKey.FK_COLUMN.toString()) + "\"";
                                String secondJoinedColumn = joinAttributesMap.get(RbReportConstantUtils.JoinAttributeKey.JOIN_TABLE.toString())
                                        + ".\"" + joinAttributesMap.get(RbReportConstantUtils.JoinAttributeKey.JOINED_FK_COLUMN.toString()) + "\"";
                                joinColumnRelMap.put(joinAttributesMap.get(RbReportConstantUtils.JoinAttributeKey.FK_TABLE.toString()).toString().trim(),
                                        firstJoinedColumn + " = " + secondJoinedColumn);

                                String localColumn = rbReportField.getDbBaseName()
                                        + ".\"" + joinAttributesMap.get(RbReportConstantUtils.JoinAttributeKey.LOCAL_COLUMN.toString()) + "\"";
                                String joinedColumn = joinAttributesMap.get(RbReportConstantUtils.JoinAttributeKey.JOIN_TABLE.toString())
                                        + ".\"" + joinAttributesMap.get(RbReportConstantUtils.JoinAttributeKey.JOINED_LOCAL_COLUMN.toString()) + "\"";
                                joinColumnRelMap.put(rbReportField.getDbBaseName().trim(), localColumn + " = " + joinedColumn);
                            }
                        }
                    }

                    String columnDefinition = rbReportField.getDbBaseName() + "." + rbReportField.getDbFieldName();
                    String convertedColumn = null;
                    String componentType = null;
                    Boolean isIncludeTime = false;
                    String timeFormat = null;
                    String customAttributes = rbReportField.getCustomAttributes();
                    if (!StringUtils.isEmpty(customAttributes)) {
                        Type type = new TypeToken<Map<String, Object>>() {
                        }.getType();
                        Map<String, Object> customAttributesMap = (new Gson()).fromJson(rbReportField.getCustomAttributes(), type);
                        if (!CollectionUtils.isEmpty(customAttributesMap)) {
                            componentType = (String) customAttributesMap.get(RbReportConstantUtils.CustomAttribute.COMPONENT_TYPE.toString());
                            if (customAttributesMap.get(RbReportConstantUtils.CustomAttribute.INCLUDE_TIME.toString()) != null) {
                                isIncludeTime = (Boolean) customAttributesMap.get(RbReportConstantUtils.CustomAttribute.INCLUDE_TIME.toString());
                            }
                        }
                    }
                    timeFormat = isIncludeTime ? "DD/MM/YYYY HH:MI AM" : "DD/MM/YYYY";

                    //Change intra column defination if any.
                    if (!CollectionUtils.isEmpty(intraTableConversionMap)) {
                        if (intraTableConversionMap.get(columnDefinition) != null) {
                            columnDefinition = intraTableConversionMap.get(columnDefinition);
                        }
                    }

                    //Special case for native and current address.
                    if (!CollectionUtils.isEmpty(addressRelationMap) && addressRelationMap.get(columnDefinition) != null) {
                        Type type = new TypeToken<Map<String, String>>() {
                        }.getType();
                        Map<String, String> fieldAddressRelationMap = (new Gson()).fromJson(addressRelationMap.get(columnDefinition).toString(), type);
//                        System.out.println("fieldSpecialRelationMap---------" + fieldSpecialRelationMap);
                        String addressTable = fieldAddressRelationMap.get(RbReportConstantUtils.AddressKey.ADDRESS_TABLE.toString());
                        String addressColumn = fieldAddressRelationMap.get(RbReportConstantUtils.AddressKey.ADDRESS_COLUMN.toString());
                        String addressType = fieldAddressRelationMap.get(RbReportConstantUtils.AddressKey.ADDRESS_TYPE.toString());
                        String addressColumnDef = addressTable + "." + addressColumn;
                        String cityQuery = null;
                        if (addressColumn.equals("city")) {
                            cityQuery = "(select concat_ws(',',l1.location_name,l2.location_name, "
                                    + "l3.location_name, l4.location_name) from hk_location_mst l1 \n"
                                    + "inner join hk_location_mst l2 \n"
                                    + "on l1.id = l2.parent \n"
                                    + "inner join hk_location_mst l3 \n"
                                    + "on l2.id = l3.parent \n"
                                    + "inner join hk_location_mst l4 \n"
                                    + "on l3.id = l4.parent where l4.id = um_contact_address_dtl.city::bigint \n"
                                    + ")";
                        }
                        String subQuery;
                        subQuery = "(select "
                                + (cityQuery == null ? addressColumnDef : cityQuery)
                                + " \n"
                                + "from um_contact_address_dtl \n"
                                + "inner join um_user_contact d \n"
                                + "on d.id = um_contact_address_dtl.user_contact \n"
                                + "where um_contact_address_dtl.address_type = '" + addressType + "' \n"
                                + "and d.userobj = um_user_contact.userobj "
                                + "and um_contact_address_dtl.is_archive = false )";

                        convertedColumn = subQuery;
                        isAddressFieldIncluded = Boolean.TRUE;
                        if (!isAggrigationNeeded) {
                            columnNameSet.add(convertedColumn + " " + reportFieldName);
                        } else {
                            columnNameSet.add("array_agg(" + convertedColumn + ") " + reportFieldName);
                        }
                        allDbBaseNameSet.add("um_user_contact");

                    } //Special case like user and his family details.
                    else if (!CollectionUtils.isEmpty(specialRelationMap) && specialRelationMap.get(columnDefinition) != null) {
                        Type type = new TypeToken<Map<String, String>>() {
                        }.getType();
                        Map<String, String> fieldSpecialRelationMap = (new Gson()).fromJson(specialRelationMap.get(columnDefinition).toString(), type);
//                        System.out.println("fieldSpecialRelationMap---------" + fieldSpecialRelationMap);
                        String fkTable = fieldSpecialRelationMap.get(RbReportConstantUtils.JoinAttributeKey.FK_TABLE.toString());
                        String fkColumn = fieldSpecialRelationMap.get(RbReportConstantUtils.JoinAttributeKey.FK_COLUMN.toString());
                        String showColumn = fieldSpecialRelationMap.get(RbReportConstantUtils.JoinAttributeKey.SHOW_COLUMN.toString());
                        String locColumn = fieldSpecialRelationMap.get(RbReportConstantUtils.JoinAttributeKey.LOCAL_COLUMN.toString());
                        String[] showColumns = showColumn.split(":");
                        String delimiter = fieldSpecialRelationMap.get(RbReportConstantUtils.JoinAttributeKey.DELIMETER.toString());
                        String aliasToTable = "contact" + countSpecialCaseAlias;
                        Boolean isNewTable = false;
                        if (!StringUtils.isEmpty(aliasToDbBaseNameMap.get(aliasToTable))) {
                            if (!aliasToDbBaseNameMap.get(aliasToTable).equals(fkTable)) {
                                countSpecialCaseAlias++;
                                aliasToTable = "contact" + countSpecialCaseAlias;
                                isNewTable = true;
                            }
                        } else {
                            countSpecialCaseAlias++;
                            aliasToTable = "contact" + countSpecialCaseAlias;
                            isNewTable = true;
                        }
                        String modifiedFkTable = fkTable + " " + aliasToTable;
                        convertedColumn = aliasToTable + "." + showColumn;
                        //If multiple columns are found in show column, then concat 
                        //them via delimiter
                        if (showColumns.length > 1) {
                            if (delimiter == null) {
                                delimiter = "";
                            }
                            String columnsString = null;
                            for (String field : showColumns) {
                                if (columnsString == null) {
                                    columnsString = aliasToTable + "." + field;
                                } else {
                                    columnsString += ", " + aliasToTable + "." + field;
                                }
                            }
                            convertedColumn = "(concat_ws('" + delimiter + "'," + columnsString + "))";
                        }
                        String columnName = convertedColumn;
                        //Timestamp never fulfill the above conversion map.Else It will be data input error.
                        if (rbReportField.getFieldDataType().equalsIgnoreCase("timestamp")) {
                            columnName = "to_char(cast(" + convertedColumn + " as date), '" + timeFormat + "')";
                        }
                        if (!isAggrigationNeeded) {
                            columnNameSet.add(columnName + " " + reportFieldName);
                        } else {
                            columnNameSet.add("array_agg(" + columnName + ") " + reportFieldName);
                        }
                        allDbBaseNameSet.add(aliasToTable);

                        if (isNewTable) {
                            aliasToDbBaseNameMap.put(aliasToTable, fkTable);
                            joinColumnRelMap.put(modifiedFkTable, aliasToTable + "." + locColumn + " = " + fkTable + "." + fkColumn);
                        }
                    } //Check if corresponding field is anyhow related to any other table
                    else if (!CollectionUtils.isEmpty(conversionMap) && conversionMap.get(columnDefinition) != null) {
                        Boolean isRepeatConversion = Boolean.TRUE;
                        String convertedColumnDef = columnDefinition;
                        String previousAlias = null;
                        while (isRepeatConversion) {
                            Type type = new TypeToken<Map<String, String>>() {
                            }.getType();
                            Map<String, String> fieldConversionMap = (new Gson()).fromJson(conversionMap.get(convertedColumnDef).toString(), type);
//                        System.out.println("Field Converion Map---" + fieldConversionMap);
                            String fkTable = fieldConversionMap.get(RbReportConstantUtils.JoinAttributeKey.FK_TABLE.toString().trim());
                            String fkColumn = fieldConversionMap.get(RbReportConstantUtils.JoinAttributeKey.FK_COLUMN.toString());
                            String showColumn = fieldConversionMap.get(RbReportConstantUtils.JoinAttributeKey.SHOW_COLUMN.toString());
                            Boolean isAnotherConversion = null;
                            if (fieldConversionMap.get(RbReportConstantUtils.JoinAttributeKey.IS_ANOTHER_CONVERSION.toString()) != null) {
                                isAnotherConversion = Boolean.parseBoolean(fieldConversionMap.get(RbReportConstantUtils.JoinAttributeKey.IS_ANOTHER_CONVERSION.toString()));
                            }
                            if (isAnotherConversion != null && isAnotherConversion) {
                                isRepeatConversion = Boolean.TRUE;
                            } else {
                                isRepeatConversion = Boolean.FALSE;
                            }
                            String[] showColumns = showColumn.split(":");
                            String delimiter = fieldConversionMap.get(RbReportConstantUtils.JoinAttributeKey.DELIMETER.toString());
                            //Special conversion case where one table joins itself like employee name and created by.
                            if (rbReportField.getDbBaseName().equals(fkTable.trim())) {
                                String aliasToTable = "u" + (countTableAlias);
                                Boolean isNewTable = false;
//                                if (!StringUtils.isEmpty(aliasToDbBaseNameMap.get(aliasToTable))) {
//                                    if (!aliasToDbBaseNameMap.get(aliasToTable).equals(fkTable)) {
//                                        countTableAlias++;
//                                        aliasToTable = "u" + countTableAlias;
//                                        isNewTable = true;
//                                    }
//                                } else {
                                countTableAlias++;
                                aliasToTable = "u" + countTableAlias;
                                isNewTable = true;
//                                }

                                String modifiedFkTable = fkTable + " " + aliasToTable;
                                convertedColumn = aliasToTable + "." + showColumn;
                                //If multiple columns are found in show column, then concat 
                                //them via delimiter
                                if (showColumns.length > 1) {
                                    if (delimiter == null) {
                                        delimiter = "";
                                    }
                                    String columnsString = null;
                                    for (String field : showColumns) {
                                        if (columnsString == null) {
                                            columnsString = aliasToTable + "." + field;
                                        } else {
                                            columnsString += ", " + aliasToTable + "." + field;
                                        }
                                    }
                                    convertedColumn = "(concat_ws('" + delimiter + "'," + columnsString + "))";
                                }
//                                columnNameSet.add(convertedColumn + " " + reportFieldName);
                                allDbBaseNameSet.add(aliasToTable);
                                if (isNewTable) {
                                    aliasToDbBaseNameMap.put(aliasToTable, fkTable);
                                    joinColumnRelMap.put(modifiedFkTable, aliasToTable + "." + fkColumn + " = " + (previousAlias == null ? convertedColumnDef.split("\\.")[0].trim() : previousAlias) + "." + convertedColumnDef.split("\\.")[1].trim());
                                }
                                if (isRepeatConversion) {
                                    previousAlias = aliasToTable;
                                }
                            }//If the same table repeats by itself in join.use alias
                            else if (allDbBaseNameSet.contains(fkTable)) {
                                Boolean isNewTable = false;
                                String aliasToTable = "join" + (countTableAlias);
//                                if (!StringUtils.isEmpty(aliasToDbBaseNameMap.get(aliasToTable))) {
//                                    if (!aliasToDbBaseNameMap.get(aliasToTable).equals(fkTable)) {
//                                        countTableAlias++;
//                                        aliasToTable = "join" + countTableAlias;
//                                        isNewTable = true;
//                                    }
//                                } else {
                                countTableAlias++;
                                aliasToTable = "join" + countTableAlias;
                                isNewTable = true;
//                                }

                                String modifiedFkTable = fkTable + " " + aliasToTable;
                                convertedColumn = aliasToTable + "." + showColumn;
                                //If multiple columns are found in show column, then concat 
                                //them via delimiter
                                if (showColumns.length > 1) {
                                    if (delimiter == null) {
                                        delimiter = "";
                                    }
                                    String columnsString = null;
                                    for (String field : showColumns) {
                                        if (columnsString == null) {
                                            columnsString = aliasToTable + "." + field;
                                        } else {
                                            columnsString += ", " + aliasToTable + "." + field;
                                        }
                                    }
                                    convertedColumn = "(concat_ws('" + delimiter + "'," + columnsString + "))";
                                }
//                                columnNameSet.add(convertedColumn + " " + reportFieldName);
                                allDbBaseNameSet.add(aliasToTable);
                                if (isNewTable) {
                                    aliasToDbBaseNameMap.put(aliasToTable, fkTable);
                                    joinColumnRelMap.put(modifiedFkTable, aliasToTable + "." + fkColumn + " = " + (previousAlias == null ? convertedColumnDef.split("\\.")[0].trim() : previousAlias) + "." + convertedColumnDef.split("\\.")[1].trim());
                                }
                                if (isRepeatConversion) {
                                    previousAlias = aliasToTable;
                                }
                            } else {
                                convertedColumn = fkTable + "." + showColumn;
                                //If multiple columns are found in show column, then concat 
                                //them via delimiter
                                if (showColumns.length > 1) {
                                    if (delimiter == null) {
                                        delimiter = "";
                                    }
                                    String columnsString = null;
                                    for (String field : showColumns) {
                                        if (columnsString == null) {
                                            columnsString = fkTable + "." + field;
                                        } else {
                                            columnsString += ", " + fkTable + "." + field;
                                        }
                                    }
                                    convertedColumn = "(concat_ws('" + delimiter + "'," + columnsString + "))";
                                }
//                                columnNameSet.add(convertedColumn + " " + reportFieldName);
                                allDbBaseNameSet.add(fkTable);
                                joinColumnRelMap.put(fkTable, fkTable + "." + fkColumn + " = " + convertedColumnDef);
                            }
                            String origFkTable = fieldConversionMap.get(RbReportConstantUtils.JoinAttributeKey.FK_TABLE.toString().trim());
                            String origShowColumn = fieldConversionMap.get(RbReportConstantUtils.JoinAttributeKey.SHOW_COLUMN.toString());
                            convertedColumnDef = origFkTable + "." + origShowColumn;
                        }
                        if (!isAggrigationNeeded) {
                            columnNameSet.add(convertedColumn + " " + reportFieldName);
                        } else {
                            columnNameSet.add("array_agg(" + convertedColumn + ") " + reportFieldName);
                        }
                    } else if (!StringUtils.isEmpty(componentType) && componentType.equalsIgnoreCase(HkSystemConstantUtil.CustomField.ComponentType.USER_MULTISELECT)) {
                        String[] columns = rbReportField.getDbFieldName().split(":");
                        String parentTable = null;
                        String condition = null;
                        //If column1:column2, then go into if condition.
                        if (columns.length > 1) {
                            //Find a relationship with parent table.
                            if (!CollectionUtils.isEmpty(parentTableMap) && parentTableMap.containsKey(rbReportField.getDbBaseName())) {
                                Type type = new TypeToken<Map<String, String>>() {
                                }.getType();
                                Map<String, String> fieldParentTableMap = (new Gson()).fromJson(parentTableMap.get(rbReportField.getDbBaseName()).toString(), type);
                                if (!CollectionUtils.isEmpty(fieldParentTableMap)) {
                                    parentTable = fieldParentTableMap.get(RbReportConstantUtils.FranchiseRelationship.PARENT_TABLE);
                                    String parentColumn = fieldParentTableMap.get(RbReportConstantUtils.FranchiseRelationship.PARENT_COLUMN);
                                    String localColumn = fieldParentTableMap.get(RbReportConstantUtils.JoinAttributeKey.LOCAL_COLUMN.toString());
                                    condition = parentTable + "." + parentColumn + " = " + rbReportField.getDbBaseName() + "." + localColumn;
                                }
                            } else {
                                //May come to work if parent table map is not configured properly.
                                //Desperate attemp to find a ralationship with any table in left join.
                                if (!CollectionUtils.isEmpty(joinColumnRelMap)) {
                                    if (joinColumnRelMap.containsKey(rbReportField.getDbBaseName())) {
                                        condition = joinColumnRelMap.get(rbReportField.getDbBaseName());
                                    } else {
                                        for (Map.Entry<String, String> entry : joinColumnRelMap.entrySet()) {
                                            String localCondition = entry.getValue().split("=")[0].trim();
                                            String foreignCondition = entry.getValue().split("=")[1].trim();
                                            if (localCondition.split("\\.")[0].equals(rbReportField.getDbBaseName()) || foreignCondition.split("\\.")[0].equals(rbReportField.getDbBaseName())) {
                                                condition = entry.getValue();
                                                break;
                                            }
                                        }
                                    }
                                }
                            }
                            String concatQuery = "select concat_ws(':'," + rbReportField.getDbBaseName() + "." + columns[0] + ","
                                    + rbReportField.getDbBaseName() + "." + columns[1] + ")from " + rbReportField.getDbBaseName();
                            if (condition != null) {
                                concatQuery = concatQuery + " where " + condition;
                                if (!userMultiSelectExceptionSet.contains(rbReportField.getDbBaseName().trim())) {
                                    //Adding user multi select conditions . so to remove from left join and avoid distinct.
                                    userMultiSelectConditions.add(condition);
                                }
                            }
                            if (userMultiSelectExceptionSet.contains(rbReportField.getDbBaseName().trim())) {
                                concatQuery = "concat_ws(':'," + rbReportField.getDbBaseName() + "." + columns[0] + ","
                                        + rbReportField.getDbBaseName() + "." + columns[1] + ")";
                            }
//                        isDistinctRequired = Boolean.TRUE;
                            if (!userMultiSelectExceptionSet.contains(rbReportField.getDbBaseName().trim())) {
                                if (!isAggrigationNeeded) {
                                    columnNameSet.add("(select array_to_string(array(" + concatQuery + "),','))" + " " + reportFieldName);
                                } else {
                                    columnNameSet.add("array_agg(" + "(select array_to_string(array(" + concatQuery + "),','))" + ") " + reportFieldName);
                                }
                            } else {
                                if (!isAggrigationNeeded) {
                                    columnNameSet.add("(" + concatQuery + ")" + " " + reportFieldName);
                                } else {
                                    columnNameSet.add("array_agg(" + "(" + concatQuery + ")" + ") " + reportFieldName);
                                }
                            }
                            allDbBaseNameSet.add(rbReportField.getDbBaseName());
                        } else {
                            String columnName = columnDefinition;
                            if (!isAggrigationNeeded) {
                                columnNameSet.add(columnName + " " + reportFieldName);
                            } else {
                                columnNameSet.add("array_agg(" + columnName + ") " + reportFieldName);
                            }
                            allDbBaseNameSet.add(rbReportField.getDbBaseName());
                        }
                    } else {
                        String columnName = columnDefinition;
                        if (rbReportField.getFieldDataType().equalsIgnoreCase("timestamp")) {
                            columnName = "to_char(cast(" + columnDefinition + " as timestamp), '" + timeFormat + "')";
                        }
                        if (!isAggrigationNeeded) {
                            columnNameSet.add(columnName + " " + reportFieldName);
                        } else {
                            columnNameSet.add("array_agg(" + columnName + ") " + reportFieldName);
                        }
                        allDbBaseNameSet.add(rbReportField.getDbBaseName());
                    }

                    //  Add column name in set
                    //  Mital: Below if commented to handle it later
//                    if (multipleValues) {
//                        //  comma separated aggregated values
//                        columnNameSet.add("STRING_AGG(" + rbReportField.getDbBaseName() + "." + rbReportField.getDbFieldName() + ", ',') "
//                                + reportFieldName);
//                    } else {
//                    columnNameSet.add(rbReportField.getDbBaseName() + "." + rbReportField.getDbFieldName() + " " + reportFieldName);
                    //  Mital: Commenting below code to handle it later
//                        groupByColumnSet.add(reportFieldName);
//                    }
                    //  Add filters in the set
                    if (!StringUtils.isEmpty(rbReportField.getFilterAttributes())) {
                        if ((!CollectionUtils.isEmpty(conversionMap) && conversionMap.get(columnDefinition) != null)
                                || (StringUtils.isEmpty(componentType)
                                || !(componentType.equals(HkSystemConstantUtil.CustomField.ComponentType.DROPDOWN) || componentType.equals(HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN)
                                || componentType.equals(HkSystemConstantUtil.CustomField.ComponentType.USER_MULTISELECT) || componentType.equals(HkSystemConstantUtil.CustomField.ComponentType.RADIO_BUTTON)
                                || RbReportConstantUtils.GOAL_TEMPLATE_VALIDATION_SET.contains(columnDefinition)
                                || RbReportConstantUtils.FIELDS_HANDLED_IN_TRANSFORMER.contains(rbReportField.getDbBaseName() + "." + rbReportField.getDbFieldName())))) {

                            Type type = new TypeToken<List<Map<String, Object>>>() {
                            }.getType();
                            List<Map<String, Object>> filterAttributesMapList = (new Gson()).fromJson(rbReportField.getFilterAttributes(), type);
                            if (!CollectionUtils.isEmpty(filterAttributesMapList)) {
//                                String column = rbReportField.getDbBaseName() + "." + rbReportField.getDbFieldName();
                                for (Map<String, Object> filterAttributeMap : filterAttributesMapList) {
                                    String filterQuery = null;
                                    String filterColumn = null;
                                    if (convertedColumn != null) {
                                        filterColumn = convertedColumn;
                                    } else {
                                        filterColumn = columnDefinition;
                                    }
                                    //Initialize Static Field Conversion map for current field if available.
                                    Map<String, String> fieldStaticConversionMap = null;
                                    if (!CollectionUtils.isEmpty(staticConversionMap) && staticConversionMap.containsKey(columnDefinition)) {
                                        type = new TypeToken<Map<String, String>>() {
                                        }.getType();
                                        fieldStaticConversionMap = (new Gson()).fromJson(staticConversionMap.get(columnDefinition).toString(), type);
                                    }
                                    String filterOperator = filterAttributeMap.get("filter").toString().trim();
                                    switch (rbReportField.getFieldDataType()) {
                                        case "varchar":
                                            String[] firstValues = null;
                                            if (!(filterOperator.equals("is null") || filterOperator.equals("is not null"))) {
                                                //Accept multivalues only in case of in and not in.
                                                if (filterOperator.equals("in") || filterOperator.equals("not in")) {
                                                    firstValues = filterAttributeMap.get("filterValFirst").toString().split(",");
                                                } else {
                                                    firstValues = new String[]{filterAttributeMap.get("filterValFirst").toString().trim()};
                                                }
                                                //If Static Substitution Required, then replace filter values with static code(key).
                                                if (!CollectionUtils.isEmpty(fieldStaticConversionMap)) {
                                                    for (int i = 0; i < firstValues.length; i++) {
                                                        if (fieldStaticConversionMap.containsValue(firstValues[i].trim())) {
                                                            for (Map.Entry<String, String> mapItem : fieldStaticConversionMap.entrySet()) {
                                                                if (firstValues[i].trim().equals(mapItem.getValue().trim())) {
                                                                    firstValues[i] = mapItem.getKey();
                                                                    break;
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                                String firstValueQuery = null;
                                                for (String firstValue : firstValues) {
                                                    if (StringUtils.isEmpty(firstValueQuery)) {
                                                        if (!filterOperator.equals("like")) {
                                                            firstValueQuery = "'" + firstValue.trim().toLowerCase() + "'";
                                                        }else{
                                                            firstValueQuery = firstValue.trim().toLowerCase();
                                                        }
                                                    } else {
                                                        firstValueQuery += ",'" + firstValue.trim().toLowerCase() + "'";
                                                    }
                                                }
                                                switch (filterOperator) {
                                                    case "in":
                                                    case "=":
                                                        filterQuery = " in " + "(" + firstValueQuery + ")";
                                                        break;
                                                    case "not in":
                                                    case "!=":
                                                        filterQuery = " not in " + "(" + firstValueQuery + ")";
                                                        break;
                                                    case "like":
                                                        filterQuery = " like '%" + firstValueQuery + "%' ";
                                                        break;
                                                }
                                                filterColumn = "lower(" + filterColumn + ")";
                                            } else {
                                                filterQuery = " " + filterOperator + " ";
                                            }
                                            break;
                                        case "int8":
                                            if (!(filterOperator.equals("is null") || filterOperator.equals("is not null"))) {
                                                String firstValue = filterAttributeMap.get("filterValFirst").toString();
                                                if (filterAttributeMap.get("filter").toString().equals("between")) {
                                                    String secondValue = filterAttributeMap.get("filterValSecond").toString();
                                                    filterQuery = " between " + firstValue + " and " + secondValue;
                                                } else {
                                                    filterQuery = filterAttributeMap.get("filter").toString() + firstValue;
                                                }
                                            } else {
                                                filterQuery = " " + filterOperator + " ";
                                            }
                                            break;
                                        case "timestamp":
                                            if (!(filterOperator.equals("is null") || filterOperator.equals("is not null"))) {
                                                String firstDate = filterAttributeMap.get("filterValFirst").toString().trim().substring(0, 10);
                                                if (filterAttributeMap.get("filter").toString().equals("between")) {
                                                    String secondDate = filterAttributeMap.get("filterValSecond").toString().trim().substring(0, 10);
                                                    filterQuery = " between '" + firstDate + "' and '" + secondDate + "'";
                                                } else {
                                                    filterQuery = filterAttributeMap.get("filter").toString() + " '" + firstDate + "'";
                                                }
                                                filterColumn = "cast(" + filterColumn + " as date)";
                                            } else {
                                                filterQuery = " " + filterOperator + " ";
                                            }
                                            break;
                                        case "double precision":
                                            if (!(filterOperator.equals("is null") || filterOperator.equals("is not null"))) {
                                                String firstDoubleValue = filterAttributeMap.get("filterValFirst").toString();
                                                if (filterAttributeMap.get("filter").toString().equals("between")) {
                                                    String secondValue = filterAttributeMap.get("filterValSecond").toString();
                                                    filterQuery = " between " + firstDoubleValue + " and " + secondValue;
                                                } else {
                                                    filterQuery = filterAttributeMap.get("filter").toString() + firstDoubleValue;
                                                }
                                            } else {
                                                filterQuery = " " + filterOperator + " ";
                                            }
                                            break;
                                        case "boolean":
                                            if (!(filterOperator.equals("is null") || filterOperator.equals("is not null"))) {
                                                String firstBooleanValue = filterAttributeMap.get("filterValFirst").toString();
                                                filterQuery = filterAttributeMap.get("filter").toString() + firstBooleanValue;
                                                filterColumn = " cast(" + filterColumn + " as boolean) ";
                                            } else {
                                                filterQuery = " " + filterOperator + " ";
                                            }
                                            break;
                                    }
                                    filterSet.add(filterColumn + filterQuery);
                                }
                            }
                        }
                    }
                } else {
                    if (avoidNoSQLFields) {
                        //  Remove the field from the list if it is not relational field
                        fieldItr.remove();
                    } else {
                        String columnToBeFetched = "";
                        String timestampFilterColumn = null;
                        String daterangeFilterColumnFrom = null;
                        String daterangeFilterColumnTo = null;
                        String currencyFilterColumn = null;
                        Type type = new TypeToken<Map<String, Object>>() {
                        }.getType();
                        Map<String, Object> customAttributesMap = (new Gson()).fromJson(rbReportField.getCustomAttributes(), type);

                        String columnDefinition = rbReportField.getDbBaseName() + "." + rbReportField.getDbFieldName();
                        String convertedColumn = null;
                        Boolean isIncludeTime = false;
                        Boolean isInnerDocument = false;
                        Boolean isInnerDocumentUserConversion = false;
                        String timeFormat = null;
                        if (!CollectionUtils.isEmpty(customAttributesMap)) {
                            if (customAttributesMap.get(RbReportConstantUtils.CustomAttribute.INCLUDE_TIME.toString()) != null) {
                                isIncludeTime = (Boolean) customAttributesMap.get(RbReportConstantUtils.CustomAttribute.INCLUDE_TIME.toString());
                            }
                        }
                        timeFormat = isIncludeTime ? "DD/MM/YYYY HH:MI AM" : "DD/MM/YYYY";
                        //  if custom attributes does not contain section, or section id found is null or empty
                        //  then fetch "fieldValues" field
//                        System.out.println("customAttributesMap is " + customAttributesMap);
//                        System.out.println(!customAttributesMap.containsKey(RbReportConstantUtils.CustomAttribute.SECTION_ID.toString()));
//                        System.out.println(StringUtils.isEmpty(customAttributesMap.get(RbReportConstantUtils.CustomAttribute.SECTION_ID.toString())));
                        //Check if this is any inner document field.
                        if (!CollectionUtils.isEmpty(innerDocumentToBaseNameMap)) {
                            if (innerDocumentToBaseNameMap.containsKey(rbReportField.getDbBaseName())) {
                                isInnerDocument = true;
                                rbReportField.setDbBaseName(innerDocumentToBaseNameMap.get(rbReportField.getDbBaseName()));
                            }
                            if (RbReportConstantUtils.INNER_DOCUMENT_USER_CONVERSION_SET.contains(columnDefinition)) {
                                isInnerDocumentUserConversion = true;
                            }
                        }
                        //Change intra column defination if any.
                        if (!CollectionUtils.isEmpty(intraTableConversionMap)) {
                            if (intraTableConversionMap.get(columnDefinition) != null) {
                                columnDefinition = intraTableConversionMap.get(columnDefinition);
                            }
                        }

                        if (CollectionUtils.isEmpty(customAttributesMap)
                                || !customAttributesMap.containsKey(RbReportConstantUtils.CustomAttribute.SECTION_NAME.toString())
                                || StringUtils.isEmpty(customAttributesMap.get(RbReportConstantUtils.CustomAttribute.SECTION_NAME.toString()))
                                || (isInnerDocument)) {

                            //Check if corresponding field is anyhow related to any other table
                            String dbBaseName = rbReportField.getDbBaseName();
                            if (customAttributesMap.containsKey(RbReportConstantUtils.CustomAttribute.IS_SUB_FORM_VALUE.toString()) && Boolean.parseBoolean(customAttributesMap.get(RbReportConstantUtils.CustomAttribute.IS_SUB_FORM_VALUE.toString()).toString())) {
                                String[] split = rbReportField.getDbFieldName().split(":");
                                if (split.length != 0 && !StringUtils.isEmpty(split[0])) {
//                                    System.out.println("split :" + split[0]);
                                    rbReportField.setDbFieldName(split[0]);
                                }
                            }
                            String dbFieldName = rbReportField.getDbFieldName();
                            //Will not work for inner documents.
                            if (!CollectionUtils.isEmpty(conversionMap) && conversionMap.get(columnDefinition) != null && !isInnerDocument) {
                                Boolean isRepeatConversion = Boolean.TRUE;
                                String convertedColumnDef = columnDefinition;
                                String previousAlias = null;
                                while (isRepeatConversion) {
                                    type = new TypeToken<Map<String, String>>() {
                                    }.getType();
                                    Map<String, String> fieldConversionMap = (new Gson()).fromJson(conversionMap.get(convertedColumnDef).toString(), type);
                                    System.out.println("Field Converion Map---" + fieldConversionMap);
                                    String fkTable = fieldConversionMap.get(RbReportConstantUtils.JoinAttributeKey.FK_TABLE.toString().trim());
                                    String fkColumn = fieldConversionMap.get(RbReportConstantUtils.JoinAttributeKey.FK_COLUMN.toString());
                                    String showColumn = fieldConversionMap.get(RbReportConstantUtils.JoinAttributeKey.SHOW_COLUMN.toString());
                                    Boolean isAnotherConversion = null;
                                    //System.out.println("fk table------------" + fkTable);
                                    if (fieldConversionMap.get(RbReportConstantUtils.JoinAttributeKey.IS_ANOTHER_CONVERSION.toString()) != null) {
                                        isAnotherConversion = Boolean.parseBoolean(fieldConversionMap.get(RbReportConstantUtils.JoinAttributeKey.IS_ANOTHER_CONVERSION.toString()));
                                    }
                                    if (isAnotherConversion != null && isAnotherConversion) {
                                        isRepeatConversion = Boolean.TRUE;
                                    } else {
                                        isRepeatConversion = Boolean.FALSE;
                                    }
                                    String[] showColumns = showColumn.split(":");
                                    String delimiter = fieldConversionMap.get(RbReportConstantUtils.JoinAttributeKey.DELIMETER.toString());
                                    //Special conversion case where one table joins itself like employee name and created by.
                                    if (rbReportField.getDbBaseName().equals(fkTable.trim())) {
                                        String aliasToTable = "custom_u" + (countTableAlias);
                                        Boolean isNewTable = false;
//                                        if (!StringUtils.isEmpty(aliasToDbBaseNameMap.get(aliasToTable))) {
//                                            if (!aliasToDbBaseNameMap.get(aliasToTable).equals(fkTable)) {
//                                                countTableAlias++;
//                                                aliasToTable = "custom_u" + countTableAlias;
//                                                isNewTable = true;
//                                            }
//                                        } else {
                                        countTableAlias++;
                                        aliasToTable = "custom_u" + countTableAlias;
                                        isNewTable = true;
//                                        }

                                        String modifiedFkTable = fkTable + " " + aliasToTable;
                                        if (!showColumn.contains("\"fieldValue\"")) {
                                            convertedColumn = aliasToTable + ".\"" + showColumn + "\"";
                                        } else {
                                            convertedColumn = aliasToTable + "." + showColumn + "";
                                        }
                                        //If multiple columns are found in show column, then concat 
                                        //them via delimiter
                                        if (showColumns.length > 1) {
                                            if (delimiter == null) {
                                                delimiter = "";
                                            }
                                            String columnsString = null;
                                            for (String field : showColumns) {
                                                if (columnsString == null) {
                                                    columnsString = aliasToTable + ".\"" + field + "\"";
                                                } else {
                                                    columnsString += ", " + aliasToTable + ".\"" + field + "\"";
                                                }
                                            }
                                            convertedColumn = "(concat_ws('" + delimiter + "'," + columnsString + "))";
                                        }
//                                columnNameSet.add(convertedColumn + " " + reportFieldName);
                                        allDbBaseNameSet.add(aliasToTable);
                                        if (isNewTable) {
                                            aliasToDbBaseNameMap.put(aliasToTable, fkTable);
                                            if (!fkColumn.contains("\"fieldValue\"")) {
                                                fkColumn = "\"" + fkColumn + "\"";
                                            }
                                            if (!convertedColumnDef.split("\\.")[1].trim().contains("\"fieldValue\"")) {
                                                joinColumnRelMap.put(modifiedFkTable, aliasToTable + "." + fkColumn + " = " + (previousAlias == null ? convertedColumnDef.split("\\.")[0].trim() : previousAlias) + ".\"" + convertedColumnDef.split("\\.")[1].trim() + "\"");
                                            } else {
                                                joinColumnRelMap.put(modifiedFkTable, aliasToTable + "." + fkColumn + " = " + (previousAlias == null ? convertedColumnDef.split("\\.")[0].trim() : previousAlias) + "." + convertedColumnDef.split("\\.")[1].trim() + "");
                                            }
                                        }
                                        if (isRepeatConversion) {
                                            previousAlias = aliasToTable;
                                        }
                                    } else if (allDbBaseNameSet.contains(fkTable)) {
                                        Boolean isNewTable = false;
                                        String aliasToTable = "custom_join" + (countTableAlias);
//                                        if (!StringUtils.isEmpty(aliasToDbBaseNameMap.get(aliasToTable))) {
//                                            if (!aliasToDbBaseNameMap.get(aliasToTable).equals(fkTable)) {
//                                                countTableAlias++;
//                                                aliasToTable = "custom_join" + countTableAlias;
//                                                isNewTable = true;
//                                            }
//                                        } else {
                                        countTableAlias++;
                                        aliasToTable = "custom_join" + countTableAlias;
                                        isNewTable = true;
//                                        }

                                        String modifiedFkTable = fkTable + " " + aliasToTable;
                                        if (!showColumn.contains("\"fieldValue\"")) {
                                            convertedColumn = aliasToTable + ".\"" + showColumn + "\"";
                                        } else {
                                            convertedColumn = aliasToTable + "." + showColumn + "";
                                        }
                                        //If multiple columns are found in show column, then concat 
                                        //them via delimiter
                                        if (showColumns.length > 1) {
                                            if (delimiter == null) {
                                                delimiter = "";
                                            }
                                            String columnsString = null;
                                            for (String field : showColumns) {
                                                if (columnsString == null) {
                                                    columnsString = aliasToTable + ".\"" + field + "\"";
                                                } else {
                                                    columnsString += ", " + aliasToTable + ".\"" + field + "\"";
                                                }
                                            }
                                            convertedColumn = "(concat_ws('" + delimiter + "'," + columnsString + "))";
                                        }
                                        allDbBaseNameSet.add(aliasToTable);
                                        if (isNewTable) {
                                            aliasToDbBaseNameMap.put(aliasToTable, fkTable);
                                            if (!fkColumn.contains("\"fieldValue\"")) {
                                                fkColumn = "\"" + fkColumn + "\"";
                                            }
                                            if (!convertedColumnDef.split("\\.")[1].trim().contains("\"fieldValue\"")) {
                                                joinColumnRelMap.put(modifiedFkTable, aliasToTable + "." + fkColumn + " = " + (previousAlias == null ? convertedColumnDef.split("\\.")[0].trim() : previousAlias) + ".\"" + convertedColumnDef.split("\\.")[1].trim() + "\"");
                                            } else {
                                                joinColumnRelMap.put(modifiedFkTable, aliasToTable + "." + fkColumn + " = " + (previousAlias == null ? convertedColumnDef.split("\\.")[0].trim() : previousAlias) + "." + convertedColumnDef.split("\\.")[1].trim() + "");
                                            }
                                        }
                                        if (isRepeatConversion) {
                                            previousAlias = aliasToTable;
                                        }
                                    } else {
                                        if (!showColumn.contains("\"fieldValue\"")) {
                                            convertedColumn = fkTable + ".\"" + showColumn + "\"";
                                        } else {
                                            convertedColumn = fkTable + "." + showColumn + "";
                                        }

                                        if (showColumns.length > 1) {
                                            if (delimiter == null) {
                                                delimiter = "";
                                            }
                                            String columnsString = null;
                                            for (String field : showColumns) {
                                                if (columnsString == null) {
                                                    columnsString = fkTable + ".\"" + field + "\"";
                                                } else {
                                                    columnsString += ", " + fkTable + ".\"" + field + "\"";
                                                }
                                            }
                                            convertedColumn = "(concat_ws('" + delimiter + "'," + columnsString + "))";
                                        }
                                        allDbBaseNameSet.add(fkTable);
                                        if (!fkColumn.contains("\"fieldValue\"")) {
                                            fkColumn = "\"" + fkColumn + "\"";
                                        }
                                        if (!convertedColumnDef.split("\\.")[1].trim().contains("\"fieldValue\"")) {
                                            joinColumnRelMap.put(fkTable, fkTable + "." + fkColumn + " = " + convertedColumnDef.split("\\.")[0].trim() + ".\"" + convertedColumnDef.split("\\.")[1].trim() + "\"");
                                        } else {
                                            joinColumnRelMap.put(fkTable, fkTable + "." + fkColumn + " = " + convertedColumnDef.split("\\.")[0].trim() + "." + convertedColumnDef.split("\\.")[1].trim() + "");
                                        }
                                    }
                                    String origFkTable = fieldConversionMap.get(RbReportConstantUtils.JoinAttributeKey.FK_TABLE.toString().trim());
                                    String origShowColumn = fieldConversionMap.get(RbReportConstantUtils.JoinAttributeKey.SHOW_COLUMN.toString());
                                    convertedColumnDef = origFkTable + "." + origShowColumn;
                                }
                            }

                            if (customAttributesMap.containsKey(RbReportConstantUtils.CustomAttribute.IS_DFF.toString()) && Boolean.parseBoolean(customAttributesMap.get(RbReportConstantUtils.CustomAttribute.IS_DFF.toString()).toString())) {
                                //System.out.println("inside is diff--------------");
                                if (isInnerDocument) {
                                    columnToBeFetched = "(" + columnDefinition.split("\\.")[0] + ".\"" + columnDefinition.split("\\.")[1] + "\")";
//                                    columnToBeFetched = "(json_extract_path_text(json_array_elements(" + columnDefinition.split("\\.")[0] + ".\"" + columnDefinition.split("\\.")[1] + "\"), '" + dbFieldName + "'))";
//                                    if (rbReportField.getFieldDataType().equals(HkSystemConstantUtil.CustomField.DataType.TIMESTAMP)) {
//                                        columnToBeFetched = "(json_extract_path_text(json_array_elements(" + columnDefinition.split("\\.")[0] + ".\"" + columnDefinition.split("\\.")[1] + "\"), '" + dbFieldName + "', '$date'))";
//                                        timestampFilterColumn = "TO_CHAR(TO_TIMESTAMP(cast(" + columnToBeFetched + " as numeric) / 1000), 'YYYY-MM-DD')";
//                                        columnToBeFetched = "TO_CHAR(TO_TIMESTAMP(cast(" + columnToBeFetched + " as numeric) / 1000), '" + timeFormat + "')";
//                                        if (!isAggrigationNeeded) {
//                                            columnNameSet.add(columnToBeFetched + " " + reportFieldName);
//                                        } else {
//                                            columnNameSet.add("array_agg(" + columnToBeFetched + ") " + reportFieldName);
//                                        }
//                                        allDbBaseNameSet.add(dbBaseName);
//                                    } else {
//                                        User conversion doesn't work for this time.
//                                        if (isInnerDocumentUserConversion) {
//                                            columnToBeFetched = "(select distinct concat_ws(' ', inner_document_user.first_name, inner_document_user.middle_name, "
//                                                    + "inner_document_user.last_name) from um_system_user inner_document_user join json_array_elements(" + columnDefinition.split("\\.")[0] + ".\"" + columnDefinition.split("\\.")[1] + "\")"
//                                                    + " inner_document_array on (json_extract_path_text(inner_document_array, '" + dbFieldName + "')::bigint) = inner_document_user.id)";
//                                        }
                                    if (!isAggrigationNeeded) {
                                        columnNameSet.add(columnToBeFetched + " " + reportFieldName);
                                    } else {
                                        columnNameSet.add("json_agg(" + columnToBeFetched + ") " + reportFieldName);
                                    }
                                    allDbBaseNameSet.add(dbBaseName);
//                                    }
                                } else if (rbReportField.getFieldDataType().equals(HkSystemConstantUtil.CustomField.DataType.TIMESTAMP)) {
                                    columnToBeFetched = "TO_CHAR(" + columnDefinition.split("\\.")[0] + ".\""
                                            + columnDefinition.split("\\.")[1] + "\"" + ", '" + timeFormat + "')";
                                    if (!isAggrigationNeeded) {
                                        columnNameSet.add(columnToBeFetched + " " + reportFieldName);
                                    } else {
                                        columnNameSet.add("array_agg(" + columnToBeFetched + ") " + reportFieldName);
                                    }
                                    allDbBaseNameSet.add(columnDefinition.split("\\.")[0]);

                                    timestampFilterColumn = "TO_CHAR(" + columnDefinition.split("\\.")[0] + ".\""
                                            + columnDefinition.split("\\.")[1] + "\"" + " , 'YYYY-MM-DD')";
                                } else {
                                    //If It contains any fieldValue, then double quote isn't required.
                                    if (!columnDefinition.split("\\.")[1].contains("\"fieldValue\"")) {
                                        columnToBeFetched = columnDefinition.split("\\.")[0] + ".\""
                                                + columnDefinition.split("\\.")[1] + "\"";
                                    } else {
                                        columnToBeFetched = columnDefinition.split("\\.")[0] + "."
                                                + columnDefinition.split("\\.")[1];
                                    }
                                    if (convertedColumn != null) {
                                        columnToBeFetched = convertedColumn;
                                    }
                                    //System.out.println("columntobe feted------------" + columnToBeFetched);
                                    if (!isAggrigationNeeded) {
                                        columnNameSet.add(columnToBeFetched + " " + reportFieldName);
                                    } else {
                                        columnNameSet.add("array_agg(" + columnToBeFetched + ") " + reportFieldName);
                                    }
                                    allDbBaseNameSet.add(columnDefinition.split("\\.")[0]);
                                }
                            } else {
                                //System.out.println("not is dff----------------");
                                String componentType = customAttributesMap.get(RbReportConstantUtils.CustomAttribute.COMPONENT_TYPE.toString()).toString();
                                String hkFieldId = customAttributesMap.get(RbReportConstantUtils.CustomAttribute.HK_FIELD_ID.toString()).toString();

                                //For sub entity purpose (only of general section)
                                if (!StringUtils.isEmpty(componentType) && componentType.equals(HkSystemConstantUtil.CustomField.ComponentType.SUBENTITY)) {

                                    String fkTable = "subformvalue";
                                    if (allDbBaseNameSet.contains(fkTable)) {
                                        Boolean isNewTable = false;
                                        String aliasToTable = "custom_joins_sub" + (countTableAlias);
//                                        if (!StringUtils.isEmpty(aliasToDbBaseNameMap.get(aliasToTable))) {
//                                            if (!aliasToDbBaseNameMap.get(aliasToTable).equals(fkTable)) {
//                                                countTableAlias++;
//                                                aliasToTable = "custom_joins_sub" + countTableAlias;
//                                                isNewTable = true;
//                                            }
//                                        } else {
                                        countTableAlias++;
                                        aliasToTable = "custom_joins_sub" + countTableAlias;
                                        isNewTable = true;
//                                        }
                                        fkTable += " " + aliasToTable;

                                        String subEntityValueQuery = "json_extract_path_text(" + aliasToTable + ".\"fieldValue\","
                                                + "(select sub_field_name from hk_sub_entity_field_info "
                                                + "where hk_sub_entity_field_info.is_droplist_field = true and hk_sub_entity_field_info.is_archive = false "
                                                + "and hk_sub_entity_field_info.parent_field = " + hkFieldId + "):: text)";
                                        if (rbReportField.getFieldDataType().equals(HkSystemConstantUtil.CustomField.DataType.TIMESTAMP)) {
                                            subEntityValueQuery = "json_extract_path_text(" + aliasToTable + ".\"fieldValue\","
                                                    + "(select sub_field_name from hk_sub_entity_field_info "
                                                    + "where hk_sub_entity_field_info.is_droplist_field = true and hk_sub_entity_field_info.is_archive = false "
                                                    + "and hk_sub_entity_field_info.parent_field = " + hkFieldId + "):: text, '$date')";
                                            columnToBeFetched = "TO_CHAR(TO_TIMESTAMP(cast(" + subEntityValueQuery + " as numeric) / 1000), '" + timeFormat + "')";
                                            timestampFilterColumn = "TO_CHAR(TO_TIMESTAMP(cast(" + subEntityValueQuery + " as numeric) / 1000), 'YYYY-MM-DD')";

                                            if (!isAggrigationNeeded) {
                                                columnNameSet.add(columnToBeFetched + " " + reportFieldName);
                                            } else {
                                                columnNameSet.add("array_agg(" + columnToBeFetched + ") " + reportFieldName);
                                            }
                                            allDbBaseNameSet.add(aliasToTable);
                                        } else {
                                            columnToBeFetched = subEntityValueQuery;
                                            if (!isAggrigationNeeded) {
                                                columnNameSet.add(columnToBeFetched + " " + reportFieldName);
                                            } else {
                                                columnNameSet.add("array_agg(" + columnToBeFetched + ") " + reportFieldName);
                                            }
                                            allDbBaseNameSet.add(aliasToTable);
                                        }
                                        if (isNewTable) {
                                            aliasToDbBaseNameMap.put(aliasToTable, "subformvalue");
                                            joinColumnRelMap.put(fkTable, aliasToTable + "._id=" + dbBaseName + ".\"fieldValue\"->>'" + dbFieldName + "'");

                                        }
                                    } else {
                                        String subEntityValueQuery = "json_extract_path_text(subformvalue.\"fieldValue\","
                                                + "(select sub_field_name from hk_sub_entity_field_info "
                                                + "where hk_sub_entity_field_info.is_droplist_field = true and hk_sub_entity_field_info.is_archive = false "
                                                + "and hk_sub_entity_field_info.parent_field = " + hkFieldId + "):: text)";
                                        if (rbReportField.getFieldDataType().equals(HkSystemConstantUtil.CustomField.DataType.TIMESTAMP)) {
                                            subEntityValueQuery = "json_extract_path_text(subformvalue.\"fieldValue\","
                                                    + "(select sub_field_name from hk_sub_entity_field_info "
                                                    + "where hk_sub_entity_field_info.is_droplist_field = true and hk_sub_entity_field_info.is_archive = false "
                                                    + "and hk_sub_entity_field_info.parent_field = " + hkFieldId + "):: text, '$date')";
                                            columnToBeFetched = "TO_CHAR(TO_TIMESTAMP(cast(" + subEntityValueQuery + " as numeric) / 1000), '" + timeFormat + "')";
                                            timestampFilterColumn = "TO_CHAR(TO_TIMESTAMP(cast(" + subEntityValueQuery + " as numeric) / 1000), 'YYYY-MM-DD')";

                                            if (!isAggrigationNeeded) {
                                                columnNameSet.add(columnToBeFetched + " " + reportFieldName);
                                            } else {
                                                columnNameSet.add("array_agg(" + columnToBeFetched + ") " + reportFieldName);
                                            }
                                            allDbBaseNameSet.add("subformvalue");
                                        } else {
                                            columnToBeFetched = subEntityValueQuery;
                                            if (!isAggrigationNeeded) {
                                                columnNameSet.add(columnToBeFetched + " " + reportFieldName);
                                            } else {
                                                columnNameSet.add("array_agg(" + columnToBeFetched + ") " + reportFieldName);
                                            }
                                            allDbBaseNameSet.add("subformvalue");
                                        }
                                        joinColumnRelMap.put("subformvalue", "subformvalue._id=" + dbBaseName + ".\"fieldValue\"->>'" + dbFieldName + "'");
                                    }
                                } else if (componentType.equals(HkSystemConstantUtil.CustomField.ComponentType.CURRENCY)) {
                                    columnToBeFetched = "concat_ws(' '," + "json_extract_path_text(" + dbBaseName + "." + "\"fieldValue\"" + ","
                                            + "'" + dbFieldName + "')," + "json_extract_path_text(" + dbBaseName + "." + "\"fieldValue\"" + ","
                                            + "'" + dbFieldName + "*CurrencyCode'))";
                                    currencyFilterColumn = "json_extract_path_text(" + dbBaseName + "." + "\"fieldValue\"" + ","
                                            + "'" + dbFieldName + "')";
                                    if (!isAggrigationNeeded) {
                                        columnNameSet.add(columnToBeFetched + " " + reportFieldName);
                                    } else {
                                        columnNameSet.add("array_agg(" + columnToBeFetched + ") " + reportFieldName);
                                    }
                                    allDbBaseNameSet.add(dbBaseName);
                                } else if (rbReportField.getFieldDataType().equals(HkSystemConstantUtil.CustomField.DataType.TIMESTAMP)) {
                                    if (!StringUtils.isEmpty(componentType) && componentType.equals(HkSystemConstantUtil.CustomField.ComponentType.DATE_RANGE)) {
                                        columnToBeFetched = "concat_ws(' to ',TO_CHAR(TO_TIMESTAMP(cast(json_extract_path_text(" + dbBaseName + "." + "\"fieldValue\"" + ","
                                                + "'" + "from" + dbFieldName + "'" + "," + "'" + "$date" + "') as numeric) / 1000), 'DD/MM/YYYY HH:MI AM')" + ","
                                                + "TO_CHAR(TO_TIMESTAMP(cast(json_extract_path_text(" + dbBaseName + "." + "\"fieldValue\"" + ","
                                                + "'" + "to" + dbFieldName + "'" + "," + "'" + "$date" + "') as numeric) / 1000), '" + timeFormat + "'))";
                                        daterangeFilterColumnFrom = "TO_CHAR(TO_TIMESTAMP(cast(json_extract_path_text(" + dbBaseName + "." + "\"fieldValue\"" + ","
                                                + "'" + "from" + dbFieldName + "'" + "," + "'" + "$date" + "') as numeric) / 1000), 'YYYY-MM-DD')";
                                        daterangeFilterColumnTo = "TO_CHAR(TO_TIMESTAMP(cast(json_extract_path_text(" + dbBaseName + "." + "\"fieldValue\"" + ","
                                                + "'" + "to" + dbFieldName + "'" + "," + "'" + "$date" + "') as numeric) / 1000), 'YYYY-MM-DD')";
                                    } else {
                                        columnToBeFetched = "TO_CHAR(TO_TIMESTAMP(cast(json_extract_path_text(" + dbBaseName + "." + "\"fieldValue\"" + ","
                                                + "'" + dbFieldName + "'" + "," + "'" + "$date" + "') as numeric) / 1000), '" + timeFormat + "')";
                                        timestampFilterColumn = "TO_CHAR(TO_TIMESTAMP(cast(json_extract_path_text(" + dbBaseName + "." + "\"fieldValue\"" + ","
                                                + "'" + dbFieldName + "'" + "," + "'" + "$date" + "') as numeric) / 1000), 'YYYY-MM-DD')";

                                    }
                                    if (!isAggrigationNeeded) {
                                        columnNameSet.add(columnToBeFetched + " " + reportFieldName);
                                    } else {
                                        columnNameSet.add("array_agg(" + columnToBeFetched + ") " + reportFieldName);
                                    }
                                    allDbBaseNameSet.add(dbBaseName);
                                } else if (customAttributesMap.containsKey(RbReportConstantUtils.CustomAttribute.IS_SUB_FORM_VALUE.toString()) && Boolean.parseBoolean(customAttributesMap.get(RbReportConstantUtils.CustomAttribute.IS_SUB_FORM_VALUE.toString()).toString()) && !StringUtils.isEmpty(customAttributesMap.get(RbReportConstantUtils.CustomAttribute.HK_PARENT_FIELD_NAME.toString()).toString())) {
                                    if (isNormalSubFormValue) {
                                        String fkTable = "subformvalue";
                                        String parentField = customAttributesMap.get(RbReportConstantUtils.CustomAttribute.HK_PARENT_FIELD_NAME.toString()).toString();
                                        String parentDbBaseName = customAttributesMap.get(RbReportConstantUtils.CustomAttribute.HK_PARENT_DB_BASE_NAME.toString()).toString();
                                        if (allDbBaseNameSet.contains(fkTable)) {
                                            Boolean isNewTable = false;
                                            String aliasToTable = "custom_joins" + (countTableAlias);
//                                            if (!StringUtils.isEmpty(aliasToDbBaseNameMap.get(aliasToTable))) {
//                                                if (!aliasToDbBaseNameMap.get(aliasToTable).equals(fkTable)) {
//                                                    countTableAlias++;
//                                                    aliasToTable = "custom_joins" + countTableAlias;
//                                                    isNewTable = true;
//                                                }
//                                            } else {
                                            countTableAlias++;
                                            aliasToTable = "custom_joins" + countTableAlias;
                                            isNewTable = true;
//                                            }
                                            fkTable += " " + aliasToTable;

                                            if (!StringUtils.isEmpty(componentType) && componentType.equals(HkSystemConstantUtil.CustomField.ComponentType.DATE_RANGE)) {
                                                columnToBeFetched = "TO_CHAR(TO_TIMESTAMP(cast(json_extract_path_text(" + aliasToTable + "." + "\"fieldValue\"" + ","
                                                        + "'" + dbFieldName + "'" + "," + "'" + "$date" + "') as numeric) / 1000), '" + timeFormat + "')";
                                                timestampFilterColumn = "TO_CHAR(TO_TIMESTAMP(cast(json_extract_path_text(" + aliasToTable + "." + "\"fieldValue\"" + ","
                                                        + "'" + dbFieldName + "'" + "," + "'" + "$date" + "') as numeric) / 1000), 'YYYY-MM-DD')";
                                            } else {
                                                columnToBeFetched = "json_extract_path_text(" + aliasToTable + "." + "\"fieldValue\"" + ","
                                                        + "'" + dbFieldName + "')";
                                            }
                                            if (!isAggrigationNeeded) {
                                                columnNameSet.add(columnToBeFetched + " " + reportFieldName);
                                            } else {
                                                columnNameSet.add("array_agg(" + columnToBeFetched + ") " + reportFieldName);
                                            }
                                            allDbBaseNameSet.add(aliasToTable);
                                            if (isNewTable) {
                                                aliasToDbBaseNameMap.put(aliasToTable, "subformvalue");

                                                joinColumnRelMap.put(fkTable, aliasToTable + "." + "_id" + " = " + parentDbBaseName + ".\"fieldValue\"->>'" + parentField + "'");
                                            }
                                        } else {
                                            if (!StringUtils.isEmpty(componentType) && componentType.equals(HkSystemConstantUtil.CustomField.ComponentType.DATE_RANGE)) {
                                                columnToBeFetched = "TO_CHAR(TO_TIMESTAMP(cast(json_extract_path_text(" + fkTable + "." + "\"fieldValue\"" + ","
                                                        + "'" + dbFieldName + "'" + "," + "'" + "$date" + "') as numeric) / 1000), '" + timeFormat + "')";
                                                timestampFilterColumn = "TO_CHAR(TO_TIMESTAMP(cast(json_extract_path_text(" + fkTable + "." + "\"fieldValue\"" + ","
                                                        + "'" + dbFieldName + "'" + "," + "'" + "$date" + "') as numeric) / 1000), 'YYYY-MM-DD')";
                                            } else {
                                                columnToBeFetched = "json_extract_path_text(" + fkTable + "." + "\"fieldValue\"" + ","
                                                        + "'" + dbFieldName + "')";
                                            }
                                            if (!isAggrigationNeeded) {
                                                columnNameSet.add(columnToBeFetched + " " + reportFieldName);
                                            } else {
                                                columnNameSet.add("array_agg(" + columnToBeFetched + ") " + reportFieldName);
                                            }
                                            allDbBaseNameSet.add(fkTable);
                                            joinColumnRelMap.put(fkTable, fkTable + "." + "_id" + " = " + parentDbBaseName + ".\"fieldValue\"->>'" + parentField + "'");
                                        }
                                    } else {
                                        columnToBeFetched = "json_extract_path_text(" + dbBaseName + "." + "\"fieldValue\"" + ","
                                                + "'" + dbFieldName + "')";
                                        if (!isAggrigationNeeded) {
                                            columnNameSet.add(columnToBeFetched + " " + reportFieldName);
                                        } else {
                                            columnNameSet.add("array_agg(" + columnToBeFetched + ") " + reportFieldName);
                                        }
                                        allDbBaseNameSet.add(dbBaseName);
                                    }

                                } else {
                                    columnToBeFetched = "json_extract_path_text(" + dbBaseName + "." + "\"fieldValue\"" + ","
                                            + "'" + dbFieldName + "')";
                                    if (!isAggrigationNeeded) {
                                        columnNameSet.add(columnToBeFetched + " " + reportFieldName);
                                    } else {
                                        columnNameSet.add("array_agg(" + columnToBeFetched + ") " + reportFieldName);
                                    }
                                    allDbBaseNameSet.add(dbBaseName);
                                }
                            }
                        } else {
                            //  if section found, then fetch the sectionList field
                            columnToBeFetched = rbReportField.getDbBaseName() + "." + "\"sectionList\"";
                            columnNameSet.add("json_agg(" + columnToBeFetched
                                    + ") as \"" + rbReportField.getDbBaseName() + "." + "sectionList\"");
                            groupByColumnSet.remove(reportFieldName);
                            allDbBaseNameSet.add(rbReportField.getDbBaseName());
                        }

                        //  Put the join and table in set
                        if (joinColumnRelMap.get(rbReportField.getDbBaseName()) == null
                                && !StringUtils.isEmpty(rbReportField.getJoinAttributes())) {
                            //  if join attributes defined, we'll store it and use it in query
                            type = new TypeToken<Map<String, Object>>() {
                            }.getType();
                            Map<String, Object> joinAttributesMap = (new Gson()).fromJson(rbReportField.getJoinAttributes(), type);
//                            String relType = (String) joinAttributesMap.get(RbReportConstantUtils.JoinAttributeKey.REL_TYPE.toString());
//                            //  if relationship is many to many or one to many, means we'll have to aggregate the values in a single row
//                            if (relType.equals(RbReportConstantUtils.RelType.ONE_TO_MANY)
//                                    || relType.equals(RbReportConstantUtils.RelType.MANY_TO_MANY)) {
//                                multipleValues = true;
//                            }

                            String localColumn = rbReportField.getDbBaseName()
                                    + ".\"" + joinAttributesMap.get(RbReportConstantUtils.JoinAttributeKey.LOCAL_COLUMN.toString()) + "\"";
                            String joinedColumn = joinAttributesMap.get(RbReportConstantUtils.JoinAttributeKey.FK_TABLE.toString())
                                    + "." + joinAttributesMap.get(RbReportConstantUtils.JoinAttributeKey.FK_COLUMN.toString());
                            joinColumnRelMap.put(rbReportField.getDbBaseName().trim(), localColumn + " = " + joinedColumn);
                        }

                        //  Put filtering on fields
                        //  Add filters in the set
                        if (!StringUtils.isEmpty(rbReportField.getFilterAttributes())) {
                            if (CollectionUtils.isEmpty(customAttributesMap)
                                    || !customAttributesMap.containsKey(RbReportConstantUtils.CustomAttribute.SECTION_NAME.toString())
                                    || StringUtils.isEmpty(customAttributesMap.get(RbReportConstantUtils.CustomAttribute.SECTION_NAME.toString()))
                                    || (!isInnerDocument)) {
                                if (!RbReportConstantUtils.FIELDS_HANDLED_IN_TRANSFORMER.contains(rbReportField.getDbBaseName() + "." + rbReportField.getDbFieldName())) {
                                    String componentType = null;
                                    if (!CollectionUtils.isEmpty(customAttributesMap)) {
                                        componentType = (String) customAttributesMap.get(RbReportConstantUtils.CustomAttribute.COMPONENT_TYPE.toString());
                                    }
                                    if ((StringUtils.isEmpty(componentType)
                                            || !(componentType.equals(HkSystemConstantUtil.CustomField.ComponentType.DROPDOWN) || componentType.equals(HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN)
                                            || componentType.equals(HkSystemConstantUtil.CustomField.ComponentType.USER_MULTISELECT) || componentType.equals(HkSystemConstantUtil.CustomField.ComponentType.RADIO_BUTTON)))) {
                                        type = new TypeToken<List<Map<String, Object>>>() {
                                        }.getType();
                                        List<Map<String, Object>> filterAttributesMapList = (new Gson()).fromJson(rbReportField.getFilterAttributes(), type);
                                        if (!CollectionUtils.isEmpty(filterAttributesMapList)) {
                                            for (Map<String, Object> filterAttributeMap : filterAttributesMapList) {
                                                String filterQuery = null;
                                                String filterColumn = null;
                                                String filterOperator = filterAttributeMap.get("filter").toString().trim();
                                                switch (rbReportField.getFieldDataType()) {
                                                    case "varchar":
                                                        String[] firstValues = null;
                                                        //Accept multivalues only in case of in and not in.
                                                        if (!(filterOperator.equals("is null") || filterOperator.equals("is not null"))) {
                                                            if (filterOperator.equals("in") || filterOperator.equals("not in")) {
                                                                firstValues = filterAttributeMap.get("filterValFirst").toString().split(",");
                                                            } else {
                                                                firstValues = new String[]{filterAttributeMap.get("filterValFirst").toString().trim()};
                                                            }
                                                            String firstValueQuery = null;
                                                            for (String firstValue : firstValues) {
                                                                if (StringUtils.isEmpty(firstValueQuery)) {
                                                                    if (!filterOperator.equals("like")) {
                                                                        firstValueQuery = "'" + firstValue.trim().toLowerCase() + "'";
                                                                    } else {
                                                                        firstValueQuery = firstValue.trim().toLowerCase();
                                                                    }
                                                                } else {
                                                                    firstValueQuery += ",'" + firstValue.trim().toLowerCase() + "'";
                                                                }
                                                            }
                                                            switch (filterOperator) {
                                                                case "in":
                                                                case "=":
                                                                    filterQuery = " in " + "(" + firstValueQuery + ")";
                                                                    break;
                                                                case "not in":
                                                                case "!=":
                                                                    filterQuery = " not in " + "(" + firstValueQuery + ")";
                                                                    break;
                                                                case "like":
                                                                    filterQuery = " like '%" + firstValueQuery + "%' ";
                                                                    break;
                                                            }
                                                            filterColumn = " lower( cast(" + columnToBeFetched + " as text)) ";
                                                        } else {
                                                            filterColumn = columnToBeFetched;
                                                            filterQuery = " " + filterOperator + " ";
                                                        }
                                                        break;
                                                    case "int8":
                                                        if (!(filterOperator.equals("is null") || filterOperator.equals("is not null"))) {
                                                            String firstValue = filterAttributeMap.get("filterValFirst").toString();
                                                            if (filterAttributeMap.get("filter").toString().equals("between")) {
                                                                String secondValue = filterAttributeMap.get("filterValSecond").toString();
                                                                filterQuery = " between " + firstValue + " and " + secondValue;
                                                            } else {
                                                                filterQuery = filterAttributeMap.get("filter").toString() + firstValue;
                                                            }
                                                            filterColumn = " cast(" + columnToBeFetched + " as numeric) ";
                                                        } else {
                                                            filterColumn = columnToBeFetched;
                                                            filterQuery = " " + filterOperator + " ";
                                                        }
                                                        break;
                                                    case "timestamp":
                                                        if (!(filterOperator.equals("is null") || filterOperator.equals("is not null"))) {
                                                            String firstDate = filterAttributeMap.get("filterValFirst").toString().trim().substring(0, 10);
                                                            if (componentType.equals(HkSystemConstantUtil.CustomField.ComponentType.DATE_RANGE)) {
                                                                filterQuery = " between cast(" + daterangeFilterColumnFrom + " as date) and cast(" + daterangeFilterColumnTo + " as date)";
                                                                if (filterAttributeMap.get("filter").toString().equals("!=")) {
                                                                    filterQuery = " not" + filterQuery;
                                                                }
                                                                filterColumn = " cast('" + firstDate + "' as date) ";
                                                            } else {
                                                                if (filterAttributeMap.get("filter").toString().equals("between")) {
                                                                    String secondDate = filterAttributeMap.get("filterValSecond").toString().trim().substring(0, 10);
                                                                    filterQuery = " between '" + firstDate + "' and '" + secondDate + "'";
                                                                } else {
                                                                    filterQuery = filterAttributeMap.get("filter").toString() + " '" + firstDate + "'";
                                                                }
                                                                filterColumn = " cast(" + timestampFilterColumn + " as date) ";
                                                            }
                                                        } else {
                                                            filterColumn = timestampFilterColumn;
                                                            filterQuery = " " + filterOperator + " ";
                                                        }
                                                        break;
                                                    case "double precision":
                                                        if (!(filterOperator.equals("is null") || filterOperator.equals("is not null"))) {
                                                            String firstDoubleValue = filterAttributeMap.get("filterValFirst").toString();
                                                            if (filterAttributeMap.get("filter").toString().equals("between")) {
                                                                String secondValue = filterAttributeMap.get("filterValSecond").toString();
                                                                filterQuery = " between " + firstDoubleValue + " and " + secondValue;
                                                            } else {
                                                                filterQuery = filterAttributeMap.get("filter").toString() + firstDoubleValue;
                                                            }
                                                            filterColumn = " cast(" + columnToBeFetched + " as numeric) ";
                                                            if (componentType.equals(HkSystemConstantUtil.CustomField.ComponentType.CURRENCY)) {
                                                                filterColumn = " cast(" + currencyFilterColumn + " as numeric) ";
                                                            }
                                                        } else {
                                                            filterColumn = columnToBeFetched;
                                                            if (componentType.equals(HkSystemConstantUtil.CustomField.ComponentType.CURRENCY)) {
                                                                filterColumn = currencyFilterColumn;
                                                            }
                                                            filterQuery = " " + filterOperator + " ";
                                                        }
                                                        break;
                                                    case "boolean":
                                                        if (!(filterOperator.equals("is null") || filterOperator.equals("is not null"))) {
                                                            String firstBooleanValue = filterAttributeMap.get("filterValFirst").toString();
                                                            filterQuery = filterAttributeMap.get("filter").toString() + firstBooleanValue;
                                                            filterColumn = " cast(" + columnToBeFetched + " as boolean) ";
                                                        } else {
                                                            filterQuery = " " + filterOperator + " ";
                                                            filterColumn = columnToBeFetched;
                                                        }
                                                        break;
                                                }
                                                filterSet.add(filterColumn + filterQuery);
                                            }
                                        }
                                    }
                                }
                            }
//                            type = new TypeToken<Map<String, Object>>() {
//                            }.getType();
//                            Map<String, Object> filterAttributesMap = (new Gson()).fromJson(rbReportField.getFilterAttributes(), type);
//                            for (String operator : filterAttributesMap.keySet()) {
//                                //  Many conditions to be added here: e.g. for '=', 'in' clause should be put
////                                filterSet.add(columnToBeFetched + " ->> '" + rbReportField.getDbFieldName() + "'"
//                                filterSet.add(columnToBeFetched + operator + "'" + filterAttributesMap.get(operator) + "'");
//                            }
                        }
                    }

                }
            }

            //  Check if sql field list is empty, query should be null
            //  as we have removed no sql fields from the list
            if (!CollectionUtils.isEmpty(sqlFieldList)) {
                query = "select ";
                if (!CollectionUtils.isEmpty(columnNameSet)) {
                    int index = 0;
                    for (String columnName : columnNameSet) {
                        if (index == 0 && isDistinctRequired) {
                            query += " distinct " + columnName + ", ";
                        } else {
                            query += columnName + ", ";
                        }
                        index++;
//                        System.out.println("query here is " + query);
                    }
                    query = query.substring(0, query.length() - 2);
                }

                if (StringUtils.isEmpty(primaryTable)) {
                    primaryTable = sqlFieldList.get(0).getDbBaseName();
                }
//                primaryTable = "act_activity_flow_node_route_info";
//                query += " from " + primaryTable;

                //Check if Primary table has any parent table in order to select franchise/company.
                Map<String, String> parentTableJoinRelMap = new LinkedHashMap<>();
                String parentTable = primaryTable;
                if (!CollectionUtils.isEmpty(parentTableMap) && parentTableMap.containsKey(primaryTable)) {
                    Type type = new TypeToken<Map<String, String>>() {
                    }.getType();
                    String mainTable = primaryTable;
                    while (parentTableMap.containsKey(mainTable)) {
                        Map<String, String> primaryParentTableMap = (new Gson()).fromJson(parentTableMap.get(mainTable).toString(), type);
                        if (!CollectionUtils.isEmpty(primaryParentTableMap)) {
                            parentTable = primaryParentTableMap.get(RbReportConstantUtils.FranchiseRelationship.PARENT_TABLE);
                            String parentColumn = primaryParentTableMap.get(RbReportConstantUtils.FranchiseRelationship.PARENT_COLUMN);
                            String localColumn = primaryParentTableMap.get(RbReportConstantUtils.JoinAttributeKey.LOCAL_COLUMN.toString());
                            if (localColumn.equals("instanceId")) {
                                localColumn = "\"" + localColumn + "\"";
                            }
                            parentTableJoinRelMap.put(parentTable, parentTable + "." + parentColumn + "=" + mainTable + "." + localColumn);
                            mainTable = parentTable;
                        } else {
                            break;
                        }
                    }
                }
                //Base table will be parent table.
//                primaryTable = parentTable;
                query += " from " + primaryTable;
                //Remove usermultiselect Relationship if any.
//                System.out.println("userMultiSelectConditions----" + userMultiSelectConditions);
//                System.out.println("parentTableJoinRelMap------" + parentTableJoinRelMap);
//                System.out.println("joinColumnRelMap---------" + joinColumnRelMap);
                if (!CollectionUtils.isEmpty(userMultiSelectConditions)) {
                    for (String multiSelectCondition : userMultiSelectConditions) {
                        String multiSelectLeftColumn = multiSelectCondition.split("=")[0].replace("\"", "").trim();
                        String multiSelectRightColumn = multiSelectCondition.split("=")[1].replace("\"", "").trim();
                        if (!CollectionUtils.isEmpty(parentTableJoinRelMap)) {
                            Iterator<Map.Entry<String, String>> iter = parentTableJoinRelMap.entrySet().iterator();
                            while (iter.hasNext()) {
                                Map.Entry<String, String> iterMap = iter.next();
                                String condition = iterMap.getValue();
                                String leftColumn = condition.split("=")[0].replace("\"", "").trim();
                                String rightColumn = condition.split("=")[1].replace("\"", "").trim();
                                if ((leftColumn.equals(multiSelectLeftColumn) || leftColumn.equals(multiSelectRightColumn))
                                        && (rightColumn.equals(multiSelectLeftColumn) || rightColumn.equals(multiSelectRightColumn))) {
                                    iter.remove();
                                }
                            }
                        }
                        if (!CollectionUtils.isEmpty(joinColumnRelMap)) {
                            Iterator<Map.Entry<String, String>> iter = joinColumnRelMap.entrySet().iterator();
                            while (iter.hasNext()) {
                                Map.Entry<String, String> iterMap = iter.next();
                                String condition = iterMap.getValue();
                                String leftColumn = condition.split("=")[0].replace("\"", "").trim();
                                String rightColumn = condition.split("=")[1].replace("\"", "").trim();
                                if ((leftColumn.equals(multiSelectLeftColumn) || leftColumn.equals(multiSelectRightColumn))
                                        && (rightColumn.equals(multiSelectLeftColumn) || rightColumn.equals(multiSelectRightColumn))) {
                                    iter.remove();
                                }
                            }
                        }
                    }
                }
                //Remove address relations if available(to avoid join which can cause multiple rows.).
//                if (isAddressFieldIncluded) {
//                    String addressTable = "um_contact_address_dtl";
//                    if (!CollectionUtils.isEmpty(parentTableJoinRelMap)) {
//                        Iterator<Map.Entry<String, String>> iter = parentTableJoinRelMap.entrySet().iterator();
//                        while (iter.hasNext()) {
//                            Map.Entry<String, String> iterMap = iter.next();
//                            String condition = iterMap.getValue();
//                            String leftColumn = condition.split("=")[0].replace("\"", "").trim();
//                            String rightColumn = condition.split("=")[1].replace("\"", "").trim();
//                            if ((leftColumn.split("\\.")[0].equals(addressTable) || rightColumn.split("\\.")[0].equals(addressTable))) {
//                                iter.remove();
//                            }
//                        }
//                    }
//                    if (!CollectionUtils.isEmpty(joinColumnRelMap)) {
//                        Iterator<Map.Entry<String, String>> iter = joinColumnRelMap.entrySet().iterator();
//                        while (iter.hasNext()) {
//                            Map.Entry<String, String> iterMap = iter.next();
//                            String condition = iterMap.getValue();
//                            String leftColumn = condition.split("=")[0].replace("\"", "").trim();
//                            String rightColumn = condition.split("=")[1].replace("\"", "").trim();
//                            if ((leftColumn.split("\\.")[0].equals(addressTable) || rightColumn.split("\\.")[0].equals(addressTable))) {
//                                iter.remove();
//                            }
//                        }
//                    }
//                }
                //Testing version. Don't know if it will work for all cases.
                if(isAddressFieldIncluded){
                    if(joinColumnRelMap.containsKey("um_user_contact") && !joinColumnRelMap.containsKey("um_system_user")){
                        joinColumnRelMap.put("um_system_user", "um_system_user" + "." + "id" + " = " + "um_user_contact" + ".\"" + "userobj"+ "\"");
                    }else if(!joinColumnRelMap.containsKey("um_user_contact") && joinColumnRelMap.containsKey("um_system_user")){
                        joinColumnRelMap.put("um_user_contact", "um_user_contact" + "." + "userobj" + " = " + "um_system_user" + ".\"" + "id"+ "\"");
                    }
                }
//                System.out.println("Parent Table Join Map---" + parentTableJoinRelMap);

                //Rearrange the join attributes. Relationship with primary table will be put first then likewise.
                //Normal Structure in map : table1 : table1.column1 = table2.column2 (key : value)
                //Key(table1) will be joined table and value will be on condition.
                if (!CollectionUtils.isEmpty(joinColumnRelMap)) {
//                    System.out.println("joinColumnRelMap----" + joinColumnRelMap);
                    Iterator<Map.Entry<String, String>> joinItr = joinColumnRelMap.entrySet().iterator();
                    while (joinItr.hasNext()) {
                        Map.Entry<String, String> joinEntry = joinItr.next();
                        String joinRelationship = joinEntry.getValue();
                        String localColumn = joinRelationship.split("=")[0];
                        String referenceColumn = joinRelationship.split("=")[1];
                        boolean removeElement = false;
                        //To make sure we do join between different tables but condionally related with primary
                        //swap if condition and key if needed.
                        if (referenceColumn.split("\\.")[0].replaceAll("(.*)\\(", "").trim().equals(primaryTable)) {
                            orderedJoinColumnRelMap.put(joinEntry.getKey(), joinEntry.getValue());
                            removeElement = true;
                        }
                        if (localColumn.split("\\.")[0].replaceAll("(.*)\\(", "").trim().equals(primaryTable)) {
                            orderedJoinColumnRelMap.put(referenceColumn.split("\\.")[0].trim(), referenceColumn + "=" + localColumn);
                            removeElement = true;
                        }
                        if (removeElement) {
                            joinItr.remove();
                        }
                    }
                    //After primary sorting, this will sort according to second, third table and so on..
                    if (!CollectionUtils.isEmpty(orderedJoinColumnRelMap)) {
                        while (!CollectionUtils.isEmpty(joinColumnRelMap)) {
                            Map<String, String> tempJoinColumnRelMap = new LinkedHashMap<>();
                            for (Map.Entry<String, String> orderJoinEntry : orderedJoinColumnRelMap.entrySet()) {
                                String[] tables = orderJoinEntry.getKey().split("\\s+");
                                String mainTable;
                                if (tables.length > 1) {
                                    mainTable = tables[tables.length - 1].replaceAll("(.*)\\(", "").trim();
                                } else {
                                    mainTable = tables[0].replaceAll("(.*)\\(", "").trim();
                                }
                                Iterator<Map.Entry<String, String>> joinItr1 = joinColumnRelMap.entrySet().iterator();
                                while (joinItr1.hasNext()) {
                                    Map.Entry<String, String> joinEntry = joinItr1.next();
                                    String joinRelationship = joinEntry.getValue();
                                    String localColumn = joinRelationship.split("=")[0];
                                    String referenceColumn = joinRelationship.split("=")[1];
                                    boolean removeElement = false;
                                    if (referenceColumn.split("\\.")[0].replace("(", "").trim().equals(mainTable)) {
                                        tempJoinColumnRelMap.put(joinEntry.getKey(), joinEntry.getValue());
                                        removeElement = true;
                                    }
                                    if (localColumn.split("\\.")[0].replace("(", "").trim().equals(mainTable)) {
                                        tempJoinColumnRelMap.put(referenceColumn.split("\\.")[0].trim(), referenceColumn + "=" + localColumn);
                                        removeElement = true;
                                    }
                                    if (removeElement) {
                                        joinItr1.remove();
                                    }
                                }
                            }
                            if (!CollectionUtils.isEmpty(tempJoinColumnRelMap)) {
                                orderedJoinColumnRelMap.putAll(tempJoinColumnRelMap);
                            } else {
                                Logger.getLogger(QueryStringUtils.class.getName()).log(Level.ERROR, "Relation chain breaks due to insufficient linking. Result may return unexpected. Adding Existing relationships.");
                                orderedJoinColumnRelMap.putAll(joinColumnRelMap);
                                break;
                            }
                        }
//                        System.out.println("orderedJoinColumnRelMapBeforeAddingParent----" + orderedJoinColumnRelMap);

//                        System.out.println("orderedJoinColumnRelMapAfterAddingParent----" + orderedJoinColumnRelMap);
                        for (String tableName : orderedJoinColumnRelMap.keySet()) {
                            query += " left join " + tableName;
                            query += " on " + orderedJoinColumnRelMap.get(tableName);
                            if (parentTableJoinRelMap.containsKey(tableName)) {
                                parentTableJoinRelMap.remove(tableName);
                            }
                        }
                        if (!CollectionUtils.isEmpty(parentTableJoinRelMap)) {
                            for (String tableName : parentTableJoinRelMap.keySet()) {
                                query += " left join " + tableName;
                                query += " on " + parentTableJoinRelMap.get(tableName);
                            }
                        }
                    } //Added in case there is no relationship exists with primary table.
                    //Else part can be removed if primary key issue get solved.
                    else {
                        //If No relationship with primary table found.
                        //WARNING : Unusual Behaviour Expected.

                        //Check if any relationship in main pool (joinColumnRelMap) contains primary table in key.Swap the
                        //Relationship if found.
                        if (!CollectionUtils.isEmpty(joinColumnRelMap)) {
                            if (joinColumnRelMap.containsKey(primaryTable)) {
                                String joinRelationship = joinColumnRelMap.get(primaryTable);
                                String localColumn = joinRelationship.split("=")[0];
                                String referenceColumn = joinRelationship.split("=")[1];
                                joinColumnRelMap.put(referenceColumn.split("\\.")[0].trim(), referenceColumn + "=" + localColumn);
                                joinColumnRelMap.remove(primaryTable);
                            }
                            //System.out.println("Relationship With Primary Table : " + primaryTable + " Doesn't Exists.");
                            //Adding rest of relationships.
                            for (String tableName : joinColumnRelMap.keySet()) {
                                query += " left join " + tableName;
                                query += " on " + joinColumnRelMap.get(tableName);
                                if (parentTableJoinRelMap.containsKey(tableName)) {
                                    parentTableJoinRelMap.remove(tableName);
                                }
                            }
                        }
                        //Adding parent table raltionship if exists.
                        if (!CollectionUtils.isEmpty(parentTableJoinRelMap)) {
                            for (String tableName : parentTableJoinRelMap.keySet()) {
                                query += " left join " + tableName;
                                query += " on " + parentTableJoinRelMap.get(tableName);
                            }
                        }
                    }
                } else {
                    //If no external join relationships are avialble except parent.
                    if (!CollectionUtils.isEmpty(parentTableJoinRelMap)) {
                        for (String tableName : parentTableJoinRelMap.keySet()) {
                            query += " left join " + tableName;
                            query += " on " + parentTableJoinRelMap.get(tableName);
                        }
                    }
                }

                //Add special conditions to the set (If available)
                if (!CollectionUtils.isEmpty(specialConditionMap)) {
                    for (String tableName : allDbBaseNameSet) {
                        if (specialConditionMap.containsKey(tableName)) {
                            Type type = new TypeToken<Map<String, String>>() {
                            }.getType();
                            Map<String, String> fieldConditionMap = (new Gson()).fromJson(specialConditionMap.get(tableName).toString(), type);
                            if (!CollectionUtils.isEmpty(fieldConditionMap)) {
                                String columnName = fieldConditionMap.get(RbReportConstantUtils.SpecialConditionParameter.CONDITION_COLUMN.toString());
                                String dateType = fieldConditionMap.get(RbReportConstantUtils.SpecialConditionParameter.CONDITION_DATA_TYPE.toString());
                                String value = fieldConditionMap.get(RbReportConstantUtils.SpecialConditionParameter.CONDITION_VALUE.toString());
                                String filterQuery = null;
                                if (dateType.equalsIgnoreCase("String")) {
                                    filterQuery = tableName + ".\"" + columnName + "\"=" + "'" + value + "'";
                                } else if (dateType.equalsIgnoreCase("Date") || dateType.equalsIgnoreCase("timestamp")) {
                                    filterQuery = "cast(" + tableName + ".\"" + columnName + "\" as date)=" + "'" + value + "'";
                                } else if (dateType.equalsIgnoreCase("boolean")) {
                                    filterQuery = "cast(" + tableName + ".\"" + columnName + "\" as boolean)=" + value;
                                } else {
                                    filterQuery = "cast(" + tableName + ".\"" + columnName + "\" as numeric)=" + value;
                                }
                                if (filterQuery != null) {
                                    filterSet.add(filterQuery);
                                }
                            }
                        }
                    }
                }
                if (!CollectionUtils.isEmpty(filterSet)) {
                    query += " where ";
                    for (String filter : filterSet) {
                        query += filter + " and ";
                    }
                    query = query.substring(0, query.length() - 5);
                }
                //Add Franchise Filter Template.
                boolean isFranchiseFilterAdded = false;
                if (!StringUtils.isEmpty(parentTable)) {
                    String prefix = parentTable.substring(0, 3);
                    String franchiseColumnName = null;
                    for (Map.Entry<String, String> entry : franchiseColumnTypeMap.entrySet()) {
                        String key = entry.getKey();
                        if (key != null && parentTable.startsWith(key)) {
                            franchiseColumnName = entry.getValue();
                        }
                    }

                    //If it is a mongo db Table
                    if (StringUtils.isEmpty(franchiseColumnName)) {
                        franchiseColumnName = franchiseColumnTypeMap.get(parentTable);
                    }
                    if (!StringUtils.isEmpty(franchiseColumnName)) {
                        String filterQuery = null;
                        if (CollectionUtils.isEmpty(filterSet)) {
                            filterQuery = " where " + parentTable + "." + franchiseColumnName + " = ANY(array[?])";
                        } else {
                            filterQuery = " and " + parentTable + "." + franchiseColumnName + " = ANY(array[?])";
                        }
                        isFranchiseFilterAdded = true;
                        query += filterQuery;
                    }
                }

                if (!CollectionUtils.isEmpty(allDbBaseNameSet) && visibilityStatus != null && (visibilityStatus == false)) {
                    String filterQuery = null;
                    for (String tableName : allDbBaseNameSet) {
                        if (diamondTableSet.contains(tableName)) {
                            if (CollectionUtils.isEmpty(filterSet) && filterQuery == null && !isFranchiseFilterAdded) {
                                filterQuery = " where " + "(" + tableName + ".\"haveValue\")::boolean is not true";
                            } else {
                                filterQuery = " and " + "(" + tableName + ".\"haveValue\")::boolean is not true";
                            }
                            query += filterQuery;
                        }
                    }
                }

                if (!CollectionUtils.isEmpty(groupByColumnSet)
                        && columnNameSet.size() >= groupByColumnSet.size()) {
                    query += " group by ";
                    for (String groupColumn : groupByColumnSet) {
                        for(String columnField : columnNameSet){
                            if(columnField.endsWith(groupColumn)){
                                query += columnField.substring(0,columnField.lastIndexOf(groupColumn)) + ", ";
                            }
                        }
                    }
                    query = query.substring(0, query.length() - 2);
                } else if (CollectionUtils.isEmpty(groupByColumnSet) && parentTable != null) {
                    if (diamondTableSet.contains(parentTable)) {
                        query += " group by " + parentTable + "._id";
                    } else {
                        query += " group by " + parentTable + ".id";
                    }
                }
                System.out.println("query now is " + query);
            }
        }
        return query;
    }

    public static String addTimestampConversion(String joinAttribute, List<RbReportField> sqlFieldList) {

        String localCondition = joinAttribute.split("=")[0];
        String referenceCondition = joinAttribute.split("=")[1];
        String localColumn = localCondition.split("\\.")[1].trim();
        String referenceColumn = referenceCondition.split("\\.")[1].trim();
        String localTable = localCondition.split("\\.")[0].trim();
        String referenceTable = referenceCondition.split("\\.")[0].trim();
        String leftJoinAttribute = localCondition;
        String rightJoinAttribute = referenceCondition;
        for (RbReportField rbReportField : sqlFieldList) {
            if (rbReportField.getDbBaseName().equals(localTable) && rbReportField.getDbFieldName().equals(localColumn)) {
                if (rbReportField.getFieldDataType().equals("timestamp")) {
                    localColumn += "::timestamp ";
                    leftJoinAttribute = localTable + "." + localColumn;
                }
            }
            if (rbReportField.getDbBaseName().equals(referenceTable) && rbReportField.getDbFieldName().equals(referenceColumn)) {
                if (rbReportField.getFieldDataType().equals("timestamp")) {
                    referenceColumn += "::timestamp ";
                    rightJoinAttribute = referenceTable + "." + referenceColumn;
                }
            }
        }
        return leftJoinAttribute + "=" + rightJoinAttribute;
    }
}
