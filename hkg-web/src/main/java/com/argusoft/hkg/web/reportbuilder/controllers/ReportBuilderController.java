package com.argusoft.hkg.web.reportbuilder.controllers;

import com.argusoft.generic.database.exception.GenericDatabaseException;
import com.argusoft.hkg.web.base.PrimaryKey;
import com.argusoft.hkg.web.base.ResponseCode;
import com.argusoft.hkg.web.base.ResponseEntity;
import com.argusoft.hkg.web.base.controllers.BaseController;
import com.argusoft.hkg.web.common.databeans.DatatableDataBean;
import com.argusoft.hkg.web.reportbuilder.databeans.RbTabularRelationDataBean;
import com.argusoft.hkg.web.reportbuilder.transformers.ReportBuilderTransformerBean;
import com.argusoft.hkg.web.usermanagement.databeans.FieldDataBean;
import com.argusoft.hkg.web.util.SelectItem;
import com.argusoft.hkg.web.util.SessionUtil;
import com.argusoft.reportbuilder.core.bean.MasterReportDataBean;
import com.argusoft.reportbuilder.core.RbReportService;
import com.argusoft.hkg.web.reportbuilder.databeans.RbEmailReportConfigurationDataBean;
import com.argusoft.reportbuilder.util.QueryStringUtils;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import net.sf.dynamicreports.report.exception.DRException;
import net.sf.jasperreports.engine.JRException;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author kvithlani
 */
@RestController
@RequestMapping("/report")
public class ReportBuilderController extends BaseController<MasterReportDataBean, Long> {

    @Autowired
    ReportBuilderTransformerBean reportBuilderTransformerBean;

    @Autowired
    RbReportService rbReportService;
    @Autowired
    SessionUtil sessionUtil;

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    /**
     * To retrieve list of tables in database
     *
     * @return list of tables
     */
    @RequestMapping(value = "/retrievetablenames", method = RequestMethod.GET)
    public @ResponseBody
    ResponseEntity<Map<String, List<String>>> retrieveTableNames() {
        Map<String, List<String>> tableNameMap = new HashMap<>();
        tableNameMap.put("tableNames", reportBuilderTransformerBean.retrieveTableNames());
        return new ResponseEntity(tableNameMap, ResponseCode.SUCCESS, null, null);
    }

    /**
     * To retrieve list of columns of particular tables
     *
     * @param tableNameList
     * @return list of columns table wise
     */
    @RequestMapping(value = "/retrievetablecolumns", method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity<Map<String, List<String>>> retrieveTableColumns(@RequestBody List<String> tableNameList) {
        Map<String, Object> columnsMap = new HashMap<>();
        columnsMap.put("columns", reportBuilderTransformerBean.retrieveTableColumns(tableNameList));
        return new ResponseEntity(columnsMap, ResponseCode.SUCCESS, null, null);
    }

    /**
     * To retrieve report table with server side pagination
     *
     * @param retrieveParams "query" as key and "querystring" as value,"count"
     * as key and number of records per page as value,"page" as key and offset
     * as value
     * @param masterReportDataBean
     * @param session
     * @return
     */
    @RequestMapping(value = "/retrievereporttable", method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity<Map<String, Object>> retrieveReportTable(@RequestBody MasterReportDataBean masterReportDataBean, HttpSession session) {
        String query = null;
        Map<String, Object> invalidQueryMap = new HashMap<>();
        try {
            if (masterReportDataBean != null) {
                //Query will be regenerated for each request.
                query = reportBuilderTransformerBean.generateQueryBasedOnFeatureFields(masterReportDataBean);
//                query = masterReportDataBean.getConvertedQuery();
                Map<String, Object> reportDataMap = reportBuilderTransformerBean.retrieveQueryResults(query, masterReportDataBean, false);
//                //System.out.println("reportDataMap---" + reportDataMap);
                //System.out.println("returning result...");
                return new ResponseEntity(reportDataMap, ResponseCode.SUCCESS, null, null, false);
            }
        } catch (NullPointerException e) {
            LOGGER.error("NPE in retrieveReportTable() method: ", e);
        } catch (IndexOutOfBoundsException e) {
            LOGGER.error("Error in retrieveReportTable() method: ", e);
        } catch (Exception e) {
            LOGGER.error("Exception in retrieveReportTable() method: ", e);
            invalidQueryMap.put("exceptionMsg", e.getMessage());
            return new ResponseEntity(invalidQueryMap, ResponseCode.SUCCESS, null, null);
        }

        return null;
    }

    /**
     * To retrieve distinct records of particular column for filter
     *
     * @param distinctColumnMap
     * @return
     */
    @RequestMapping(value = "/retrievelimitedcolumnvalues", method = RequestMethod.GET)
    public @ResponseBody
    ResponseEntity<Map<String, Object>> retrieveLimitedColumnValues(@RequestParam("q") String searchString, @RequestParam("col_name") String columnName, @RequestParam("page_limit") String page_limit, @RequestParam("page") String page, HttpSession session, @RequestParam("isGrouped") String isGroup) {
        Integer offSet, limit;
        Boolean isGrouped = null;
        limit = Integer.parseInt(page_limit);
        offSet = Integer.parseInt(page) * limit;
        if (searchString.equals("")) {
            searchString = null;
        }
        if (!StringUtils.isEmpty(isGroup)) {
            isGrouped = Boolean.parseBoolean(isGroup.toString());
        } else {
            isGrouped = Boolean.FALSE;
        }
        return new ResponseEntity(reportBuilderTransformerBean.retrieveLimitedColumnValues(columnName, searchString, offSet, limit, isGrouped), ResponseCode.SUCCESS, null, null);
    }

    /**
     * To retrieve converted query as per user configuration
     *
     * @param masterReportDataBean
     * @return converted query
     */
    @RequestMapping(value = "/configurereport", method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity<Map<String, String>> configureReportDataBean(@RequestBody MasterReportDataBean masterReportDataBean) {
        Map<String, String> convertedQueryMap = new HashMap<>();
        convertedQueryMap.put("convertedQuery", QueryStringUtils.changeQueryByConfiguration(masterReportDataBean));
        return new ResponseEntity(convertedQueryMap, ResponseCode.SUCCESS, null, null);
    }

    /**
     * To save report details with its configuration
     *
     * @param masterReportDataBean
     * @return
     */
    @RequestMapping(value = "/savereport", method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity<PrimaryKey<Long>> saveReport(@RequestBody MasterReportDataBean masterReportDataBean) throws GenericDatabaseException {
        if (reportBuilderTransformerBean.createReport(masterReportDataBean) != null) {
            return new ResponseEntity<PrimaryKey<Long>>(null, ResponseCode.SUCCESS, "Report saved successfully.", null);
        } else {
            return new ResponseEntity<PrimaryKey<Long>>(null, ResponseCode.FAILURE, "Report cannot be saved.", null);

        }
    }

    @RequestMapping(value = "/retrievereporttitles", method = RequestMethod.GET)
    public @ResponseBody
    ResponseEntity<List<SelectItem>> retrieveReportTitles() {
        return new ResponseEntity(reportBuilderTransformerBean.retrieveReportTitles(), ResponseCode.SUCCESS, null, null);
    }

    @RequestMapping(value = "/retrievereport", method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity<MasterReportDataBean> retrieveReport(@RequestBody Long reportId, HttpSession session) {
        MasterReportDataBean masterReportDataBean = reportBuilderTransformerBean.retrieveReportById(reportId);
//        session.setAttribute("selectedReport", masterReportDataBean);
        return new ResponseEntity<MasterReportDataBean>(masterReportDataBean, ResponseCode.SUCCESS, null, null);
    }

    @RequestMapping(value = "/retrievereportlink", method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity<Map<String, String>> retrieveReportLink(@RequestBody Map<String, Object> reportCodeMap) {

        Map<String, String> reportLinkMap = new HashMap<>();
        if (reportCodeMap != null && !reportCodeMap.isEmpty()) {
            reportLinkMap.put("reportLink", reportBuilderTransformerBean.generateReportLink(reportCodeMap.get("reportCode").toString(), (Boolean) reportCodeMap.get("editable")));
        }
        return new ResponseEntity(reportLinkMap, ResponseCode.SUCCESS, null, null);
    }

    @RequestMapping(value = "/generateexcel", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public void generateExcel(HttpSession session) {
        MasterReportDataBean masterReportDataBean = (MasterReportDataBean) session.getAttribute("selectedReport");
        String query = null;
        if (session.getAttribute("filterQuery") != null) {
            query = session.getAttribute("filterQuery").toString();
        } else {
            query = session.getAttribute("reportQuery").toString();
        }
        masterReportDataBean.setQuery(query);
        reportBuilderTransformerBean.generateExcelSheet(masterReportDataBean, session);
    }

    @RequestMapping(value = "/downloadexcel", method = RequestMethod.GET, produces = {"application/vnd.ms-excel"})
    @ResponseStatus(value = HttpStatus.OK)
    public void downloadExcel(@RequestParam(value = "reportId") Long reportId, HttpServletResponse response, HttpServletRequest request) throws FileNotFoundException, DRException, IOException, JRException, GenericDatabaseException, SQLException, JSONException, ParseException {
        MasterReportDataBean masterReportDataBean = reportBuilderTransformerBean.retrieveReportById(reportId);
        reportBuilderTransformerBean.generateDataForReport(masterReportDataBean, ".xls", response, request);
    }

    @RequestMapping(value = "/generatepdf", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public void generatePdf(@RequestBody Long reportId, HttpServletResponse response, HttpServletRequest request) throws FileNotFoundException, DRException, IOException, JRException, GenericDatabaseException, SQLException, JSONException, ParseException {
        MasterReportDataBean masterReportDataBean = reportBuilderTransformerBean.retrieveReportById(reportId);
        reportBuilderTransformerBean.generateDataForReport(masterReportDataBean, ".pdf", response, request);
    }

    @RequestMapping(value = "/downloadpdf", method = RequestMethod.GET, produces = {"application/pdf"})
    @ResponseBody
    @ResponseStatus(value = HttpStatus.OK)
    public void downloadPdf(@RequestParam(value = "reportId") Long reportId, HttpServletRequest request, HttpServletResponse response) throws DRException, IOException, FileNotFoundException, JRException, GenericDatabaseException, SQLException, JSONException, ParseException {
        MasterReportDataBean masterReportDataBean = reportBuilderTransformerBean.retrieveReportById(reportId);
        reportBuilderTransformerBean.generateDataForReport(masterReportDataBean, ".pdf", response, request);
    }
    
    @RequestMapping(value = "/downloadxml", method = RequestMethod.GET, produces = {"application/xml"})
    @ResponseBody
    @ResponseStatus(value = HttpStatus.OK)
    public void downloadXML(@RequestParam(value = "reportId") Long reportId, HttpServletRequest request, HttpServletResponse response) throws DRException, IOException, FileNotFoundException, JRException, GenericDatabaseException, SQLException, JSONException, ParseException {
        MasterReportDataBean masterReportDataBean = reportBuilderTransformerBean.retrieveReportById(reportId);
        reportBuilderTransformerBean.generateDataForReport(masterReportDataBean, ".xml", response, request);
    }

    @RequestMapping(value = "/retrieveAll", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Map<String, Object>> retrieveAll(@RequestBody Map dataMap) {
        Integer offSet = null;
        Integer limit = null;
        Boolean showAll = null;
        if (!CollectionUtils.isEmpty(dataMap)) {
            String offsetStrng = dataMap.get("offSet").toString();
            String limitStrng = dataMap.get("limit").toString();
            if (dataMap.get("showAll") != null) {
                String isShowAll = dataMap.get("showAll").toString();
                showAll = Boolean.parseBoolean(isShowAll);
            }
            if (!StringUtils.isEmpty(offsetStrng) && !StringUtils.isEmpty(limitStrng)) {
                Integer offsetTemp = Integer.parseInt(offsetStrng);
                limit = Integer.parseInt(limitStrng);
                offSet = (offsetTemp - 1) * limit;
            }
        }
        Map<String, Object> retrieveAllReports = reportBuilderTransformerBean.retrieveAllReports(showAll, offSet, limit);
        return new ResponseEntity<Map<String, Object>>(retrieveAllReports, ResponseCode.SUCCESS, "", null);
    }

    @Override
    public ResponseEntity<List<MasterReportDataBean>> retrieveAll() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ResponseEntity<MasterReportDataBean> retrieveById(PrimaryKey<Long> primaryKey
    ) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ResponseEntity<PrimaryKey<Long>> update(@RequestBody MasterReportDataBean t
    ) {
        Long id = null;
        try {
            id = reportBuilderTransformerBean.updateReport(t);
        } catch (GenericDatabaseException ex) {
            java.util.logging.Logger.getLogger(ReportBuilderController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ResponseEntity<>(new PrimaryKey<>(id), ResponseCode.SUCCESS, null, null);
    }

    @Override
    public ResponseEntity<PrimaryKey<Long>> create(MasterReportDataBean t
    ) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ResponseEntity<MasterReportDataBean> deleteById(PrimaryKey<Long> primaryKey
    ) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    //MEthod created for testing only
    @RequestMapping(value = "/retrievedata", method = RequestMethod.POST)
    public @ResponseBody
    Object retrieveData(@RequestBody DatatableDataBean tableConfig
    ) {
        if (tableConfig != null) {
            LOGGER.debug(tableConfig.toString());
        }
        HashMap<String, Object> map = new HashMap();
        int offset = (int) tableConfig.getStart();
        int limit = (int) tableConfig.getLength();
        LOGGER.debug("select id,category,status,created_on from hk_system_asset_mst order by id offset " + offset + " limit " + limit);
        map.put("records", rbReportService.retrieveAllResults("select id,category,status,created_on from hk_system_asset_mst order by id offset " + offset + " limit " + limit));
        return map;
    }

    //MEthod created for testing only
    @RequestMapping(value = "/retrievedata2", method = RequestMethod.POST)
    public @ResponseBody
    Object retrieveData2() {
        HashMap<String, Object> map = new HashMap();
        map.put("records", rbReportService.retrieveAllResults("select id,category,status,created_on from hk_system_asset_mst where id<300 order by id"));
        return map;
//        return reportDataMap.get("records");
    }

    @Override
    public ResponseEntity<Map<String, Object>> retrievePrerequisite() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * To generate query
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/generatequery", method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity<Map<String, String>> generateQuery(@RequestBody MasterReportDataBean masterReportDataBean, HttpSession session) throws ClassNotFoundException, SQLException {
        String query = reportBuilderTransformerBean.generateQueryBasedOnFeatureFields(masterReportDataBean);
        Map<String, String> resultMap = new HashMap<>();
        if (query != null) {
            resultMap.put("query", query);
            session.setAttribute("reportQuery", query);
        }
//        String table1 = tableMap.get("table1");
//        String table2 = tableMap.get("table2");
//        String columnString = tableMap.get("columns");
//        System.out.println(table1 + "     " + table2);
//        Class.forName("org.postgresql.Driver"); // load Oracle driver
//        Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/hkg", "postgres", "argusadmin");
//        String query = null;
//        Statement st = conn.createStatement();
//        ResultSet rs = null;
//        DatabaseMetaData meta = conn.getMetaData();
//        List<String> foreignKetTables = new ArrayList<>();
//        boolean hasForeignKey = false;
//        String table1fk = null, table2fk = null, midtablefk1 = null, midtablefk2 = null;
//        String midtable = null;
//        rs = meta.getExportedKeys(conn.getCatalog(), null, table1);
//        while (rs.next()) {
//            String fkTable = rs.getString("FKTABLE_NAME");
//            foreignKetTables.add(fkTable);
//            if (fkTable.equals(table2)) {
//                table2fk = rs.getString("FKCOLUMN_NAME");
//                table1fk = rs.getString("PKCOLUMN_NAME");
//                hasForeignKey = true;
//            }
//        }
//        rs.close();
//        if (!hasForeignKey) {
//            for (String string : foreignKetTables) {
//                ResultSet importedKeys = meta.getImportedKeys(conn.getCatalog(), null, string);
//                while (importedKeys.next()) {
//                    String fktable = importedKeys.getString("PKTABLE_NAME");
//                    if (fktable.equals(table2)) {
//                        midtable = string;
//                        midtablefk2 = importedKeys.getString("FKCOLUMN_NAME");
//                        table2fk = importedKeys.getString("PKCOLUMN_NAME");
//                        hasForeignKey = true;
//
//                    } else if (importedKeys.getString("PKTABLE_NAME").equals(table1)) {
//                        midtablefk1 = importedKeys.getString("FKCOLUMN_NAME");
//                        table1fk = importedKeys.getString("PKCOLUMN_NAME");
//                    }
//                }
//                importedKeys.close();
//            }
//            if (hasForeignKey) {
//                query = "select " + columnString + " from " + table1 + " , " + midtable + " , " + table2 + " where " + table1 + "." + table1fk + " = " + midtable + "." + midtablefk1 + " and " + table2 + "." + table2fk + " = " + midtable + "." + midtablefk2 + " ";
//            }
//
//        } else {
//            query = "select " + columnString + " from " + table1 + " , " + table2 + " where " + table1 + "." + table1fk + " = " + table2 + "." + table2fk + " ";
//        }
//        HashMap<String, String> map = new HashMap<>();
//        map.put("query", query);
//
//        System.out.println("+++++++++++QUERY++++++++++++++= \n" + query);
        return new ResponseEntity<>(resultMap, ResponseCode.SUCCESS, null, null);
    }

    @RequestMapping(value = "/retrievefieldswithfeaturename", method = RequestMethod.GET)
    public ResponseEntity<Map<String, List<FieldDataBean>>> retrieveFieldsWithFeatureName() throws GenericDatabaseException {
        Map<String, List<FieldDataBean>> featureFieldMap = reportBuilderTransformerBean.retrieveFieldsWithFeatureName();
        if (!CollectionUtils.isEmpty(featureFieldMap)) {
            return new ResponseEntity<>(featureFieldMap, ResponseCode.SUCCESS, null, null, false);
        } else {
            return new ResponseEntity<>(null, ResponseCode.FAILURE, null, null, false);
        }
    }

    @RequestMapping(value = "/retrievefeature", method = RequestMethod.POST)
    public ResponseEntity<Map<Long, String>> retrieveFeatureNameByIds(@RequestBody List<Long> fratureIds) throws GenericDatabaseException {
        Map<Long, String> resultMap = reportBuilderTransformerBean.retrieveFeatureNameByIds(fratureIds);
        if (!CollectionUtils.isEmpty(resultMap)) {
            return new ResponseEntity<>(resultMap, ResponseCode.SUCCESS, null, null, false);
        } else {
            return new ResponseEntity<>(null, ResponseCode.FAILURE, null, null, false);
        }
    }

    @RequestMapping(value = "/retrievefeaturesectionfieldmap", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Map<String, List<FieldDataBean>>>> retrieveFeatureSectionFieldMap() throws GenericDatabaseException {
        Map<String, Map<String, List<FieldDataBean>>> featureSectionFieldMap = reportBuilderTransformerBean.retrieveFeatureSectionFieldMap();
        if (!CollectionUtils.isEmpty(featureSectionFieldMap)) {
            return new ResponseEntity<>(featureSectionFieldMap, ResponseCode.SUCCESS, null, null, false);
        } else {
            return new ResponseEntity<>(null, ResponseCode.FAILURE, null, null, false);
        }
    }

    @RequestMapping(value = "/retrievetablerelationship", method = RequestMethod.POST)
    public ResponseEntity<List<RbTabularRelationDataBean>> retrieveTableRelationship(@RequestBody MasterReportDataBean masterReportDataBean) throws GenericDatabaseException, JSONException {
        List<RbTabularRelationDataBean> relationList = reportBuilderTransformerBean.retrieveTableRelationship(masterReportDataBean);
        if (!CollectionUtils.isEmpty(relationList)) {
            return new ResponseEntity<>(relationList, ResponseCode.SUCCESS, null, null, false);
        } else {
            return new ResponseEntity<>(null, ResponseCode.FAILURE, null, null, false);
        }
    }

    @RequestMapping(value = "/retreivecolumnmetadata", method = RequestMethod.POST)
    public ResponseEntity<MasterReportDataBean> retrieveColumnMetadata(@RequestBody MasterReportDataBean masterReportDataBean) throws GenericDatabaseException, JSONException {
        MasterReportDataBean dataBean = reportBuilderTransformerBean.retrieveColumnMetadata(masterReportDataBean);
        if (dataBean != null) {
            return new ResponseEntity<>(dataBean, ResponseCode.SUCCESS, null, null, false);
        } else {
            return new ResponseEntity<>(null, ResponseCode.FAILURE, null, null, false);
        }
    }

    @RequestMapping(value = "/generatefilteredpdf", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    public void generateFilteredPdf(@RequestBody Map resultMap, HttpServletRequest request, HttpServletResponse response) throws GenericDatabaseException, JSONException, DRException, IOException, FileNotFoundException, JRException, SQLException, ParseException {
        Long reportId = Long.parseLong(resultMap.get("reportId").toString());
        MasterReportDataBean masterReportDataBean = reportBuilderTransformerBean.retrieveReportById(reportId);
        String extension = (String) resultMap.get("extension");
        if (!CollectionUtils.isEmpty(sessionUtil.getTempFilteredRecordList())) {
            Map<String, Object> tempFilteredRecordList = sessionUtil.getTempFilteredRecordList();
            List<Map<String, Object>> filterAttributes = (List<Map<String, Object>>) resultMap.get("filterAttributes");
            if (!CollectionUtils.isEmpty(filterAttributes)) {
                //System.out.println("filterAttributes---" + filterAttributes);
            }
            List<Map<String, Object>> colorAttributes = (List<Map<String, Object>>) resultMap.get("colorAttributes");
            if (!CollectionUtils.isEmpty(colorAttributes)) {
                //System.out.println("colorAttributes---" + colorAttributes);
            }
            List<String> hiddenFields = (List<String>) resultMap.get("hiddenFields");
            reportBuilderTransformerBean.generateReport(masterReportDataBean, tempFilteredRecordList, extension, response, request, false, filterAttributes, colorAttributes, hiddenFields);
        } else {

            if (!CollectionUtils.isEmpty(sessionUtil.getReportColumnValueMap())) {
                Map<String, Object> tempFilteredRecordList = new HashMap<>(sessionUtil.getReportColumnValueMap());
                if (!CollectionUtils.isEmpty(tempFilteredRecordList)) {
                    //System.out.println("tempFilteredRecordList :" + tempFilteredRecordList);
                    List<Map<String, Object>> colorAttributes = (List<Map<String, Object>>) resultMap.get("colorAttributes");
                    if (!CollectionUtils.isEmpty(colorAttributes)) {
                        //System.out.println("colorAttributes---" + colorAttributes);
                    }
                    reportBuilderTransformerBean.generateReport(masterReportDataBean, tempFilteredRecordList, extension, response, request, false, null, colorAttributes, null);
                    //System.out.println("sessionUtil.getReportColumnValueMap() :" + sessionUtil.getReportColumnValueMap());
                }
            }
        }
    }

    @RequestMapping(value = "/downloadfilteredpdf", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public void downloadFilteredPdf(@RequestParam("reportId") Long reportId, @RequestParam("extension") String extension, HttpServletRequest request, HttpServletResponse response) throws GenericDatabaseException, JSONException, DRException, IOException, FileNotFoundException, JRException, SQLException, ParseException {
        MasterReportDataBean masterReportDataBean = reportBuilderTransformerBean.retrieveReportById(reportId);
        if (masterReportDataBean != null) {
            String fileName = masterReportDataBean.getReportName();
            reportBuilderTransformerBean.downloadPdf(fileName, extension, request, response);
        }
    }

    @RequestMapping(value = "/retrievepaginateddata", method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity<Map<String, Object>> retrievePaginatedData(@RequestBody Map dataMap) {
        Integer offSet = null;
        Integer limit = null;
        Boolean isFilter = null;
        Boolean isGrouped = null;
        String sortColumn = null;
        String sortDirection = null;
        String sortColumnType = null;
        if (!CollectionUtils.isEmpty(dataMap)) {
            String offsetStrng = dataMap.get("offSet").toString();
            String limitStrng = dataMap.get("limit").toString();
            String isFilterStrng = dataMap.get("isFilter").toString();
            if (dataMap.get("isGrouped") != null) {
                String isGroupedString = dataMap.get("isGrouped").toString();
                isGrouped = Boolean.parseBoolean(isGroupedString);
            }
            if (dataMap.get("sortColumn") != null) {
                sortColumn = dataMap.get("sortColumn").toString();
            }
            if (dataMap.get("sortDirection") != null) {
                sortDirection = dataMap.get("sortDirection").toString();
            }
            if (dataMap.get("sortColumnType") != null) {
                sortColumnType = dataMap.get("sortColumnType").toString();
            }
            List<Map<String, String>> filterOptions = (List<Map<String, String>>) dataMap.get("filterOptions");
            if (!StringUtils.isEmpty(offsetStrng) && !StringUtils.isEmpty(limitStrng) && !StringUtils.isEmpty(isFilterStrng)) {
                Integer offsetTemp = Integer.parseInt(offsetStrng);
                limit = Integer.parseInt(limitStrng);
                offSet = (offsetTemp - 1) * limit;
                isFilter = Boolean.parseBoolean(isFilterStrng);
            }

            Map<String, Object> reportColumnValueMap = reportBuilderTransformerBean.retrievePaginatedData(offSet, limit, isFilter, isGrouped, sortColumn, sortDirection, sortColumnType, filterOptions);
            return new ResponseEntity(reportColumnValueMap, ResponseCode.SUCCESS, null, null, false);
        }
        return null;
    }

    @RequestMapping(value = "/clearreportdata", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public void clearReportData() {
        if (sessionUtil.getReportColumnValueMap() != null) {
            sessionUtil.setReportColumnValueMap(null);
        }
        if (sessionUtil.getTempFilteredRecordList() != null) {
            sessionUtil.setTempFilteredRecordList(null);
        }
    }

    @RequestMapping(value = "/retrievefiltereddata", method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity<Map<String, Object>> retrieveFilteredData(@RequestBody Map<String, Object> filterMap) throws JSONException, ParseException {
        Boolean isGrouped = null;
        List<String> groupByColumns = new ArrayList<>();
        Map<String, Object> filteredMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(filterMap)) {
            if (filterMap.get("isGrouped") != null) {
                isGrouped = Boolean.parseBoolean(filterMap.get("isGrouped").toString());
            } else {
                isGrouped = Boolean.FALSE;
            }
            if (filterMap.get("filters") != null) {
                filteredMap = (Map<String, Object>) filterMap.get("filters");
            }
            if (filterMap.get("groupBy") != null) {
                String[] groupByColumnArr = filterMap.get("groupBy").toString().split(",");
                groupByColumns = Arrays.asList(groupByColumnArr);
            }
        }
        Map<String, Object> map = reportBuilderTransformerBean.retrieveFilteredData(filteredMap, isGrouped, groupByColumns);
        return new ResponseEntity(map, ResponseCode.SUCCESS, null, null, false);

    }

    @RequestMapping(value = "/retrievereportbyfeature", method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity<MasterReportDataBean> retrieveReportByFeature(@RequestBody Long featureId) throws JSONException, ParseException {
        MasterReportDataBean masterReportDataBean = reportBuilderTransformerBean.retrieveReportByFeature(featureId);
        return new ResponseEntity(masterReportDataBean, ResponseCode.SUCCESS, null, null, false);

    }

    @RequestMapping(value = "/checkreportnameexists", method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity<Boolean> isReportNameExists(@RequestBody String reportName
    ) {
        Boolean result = reportBuilderTransformerBean.isReportNameExists(reportName);
        return new ResponseEntity(result, ResponseCode.SUCCESS, null, null, false);

    }

    @RequestMapping(value = "/retrievecurrencyconfiguration", method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity<Boolean> retrieveCurrencyConfiguration() {
        Boolean result = reportBuilderTransformerBean.retrieveCurrencyConfiguration();
        return new ResponseEntity(result, ResponseCode.SUCCESS, null, null, false);

    }
    
    @RequestMapping(value = "/viewcurrencydatarights", method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity<Boolean> retrieveViewCurrencyDataRightsOfLoggedInUser() {
        Boolean result = reportBuilderTransformerBean.retrieveViewCurrencyDataRightsOfLoggedInUser();
        return new ResponseEntity(result, ResponseCode.SUCCESS, null, null, false);

    }
    
    @RequestMapping(value = "/retrieveemailconfigurationbyreportid", method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity<RbEmailReportConfigurationDataBean> retrieveEmailConfigurationByReportId(@RequestBody Long reportId) {
        RbEmailReportConfigurationDataBean result = reportBuilderTransformerBean.retrieveConfigurationByReportId(reportId);
        return new ResponseEntity(result, ResponseCode.SUCCESS, null, null, false);
    }
    
    @RequestMapping(value = "/saveemailconfiguration", method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity<Boolean> saveEmailConfiguration(@RequestBody RbEmailReportConfigurationDataBean rbEmailReportConfigurationDataBean) throws GenericDatabaseException {
        reportBuilderTransformerBean.saveEmailConfiguration(rbEmailReportConfigurationDataBean);
        return new ResponseEntity(null, ResponseCode.SUCCESS, null, null, false);
    }
    
    @RequestMapping(value = "/retrievedashboardstatus", method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity<Boolean> retrieveDashboardStatus(@RequestBody Long reportId) {
        
        Boolean res = reportBuilderTransformerBean.retrieveDashboardStatus(reportId);
        return new ResponseEntity(res, ResponseCode.SUCCESS, null, null, false);
    }
    
    @RequestMapping(value = "/updatedashboardstatus", method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity<Boolean> updateDashboardStatus(@RequestBody Map<String, Object> requestMap) {
        Long reportId = Long.parseLong(requestMap.get("reportId").toString());
        boolean dashboardStatus = (boolean)requestMap.get("dashboardStatus");
        reportBuilderTransformerBean.updateDashboardStatus(reportId, dashboardStatus);
        return new ResponseEntity(null, ResponseCode.SUCCESS, null, null, false);
    }
    
    @RequestMapping(value = "/retrievedashboardreports", method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity<List<MasterReportDataBean>> retrieveDashboardReports() throws GenericDatabaseException {
        List<MasterReportDataBean> reports = reportBuilderTransformerBean.retrieveDashboardReports();
        return new ResponseEntity(reports, ResponseCode.SUCCESS, null, null, false);
    }
    
    @RequestMapping(value = "/retrieveanalyticscrendential", method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity<Map<String, String>> retrieveAnalyticsCredential() {
        
        Map<String, String> res = reportBuilderTransformerBean.retrieveAnalyticsCredentials();
        return new ResponseEntity(res, ResponseCode.SUCCESS, null, null, false);
    }

}
