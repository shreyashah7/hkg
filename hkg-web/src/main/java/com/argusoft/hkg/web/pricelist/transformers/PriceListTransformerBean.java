package com.argusoft.hkg.web.pricelist.transformers;

import com.argusoft.hkg.common.constantutil.HkSystemConstantUtil;
import com.argusoft.hkg.common.functionutil.FolderManagement;
import static com.argusoft.hkg.common.functionutil.FolderManagement.basePath;
import com.argusoft.hkg.core.HkFieldService;
import com.argusoft.hkg.core.HkFoundationService;
import com.argusoft.hkg.model.HkCaratRangeEntity;
import com.argusoft.hkg.model.HkFieldEntity;
import com.argusoft.hkg.model.HkPriceListDetailEntity;
import com.argusoft.hkg.model.HkPriceListDetailEntityPK;
import com.argusoft.hkg.model.HkPriceListEntity;
import com.argusoft.hkg.web.base.ResponseCode;
import com.argusoft.hkg.web.base.ResponseEntity;
import com.argusoft.hkg.web.pricelist.databeans.PriceListDataBean;
import com.argusoft.hkg.web.center.usermanagement.databeans.LoginDataBean;
import com.argusoft.hkg.web.util.TreeViewDataBean;
import com.google.inject.internal.Lists;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author rajkumar
 */
@Service
public class PriceListTransformerBean {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private LoginDataBean loginDataBean;

    @Autowired
    private HkFoundationService foundationService;

    @Autowired
    private HkFieldService fieldService;

    public String downloadTemplate() {
        Map<String, List<String>> values = foundationService.retrieveMapOfFieldsValuesForPriceList(loginDataBean.getCompanyId());
        Map<Integer, Object[]> data = new TreeMap<>();
        Integer rowNum = 1;

        //Prepare headers for columns
        List<String> rowHeaders = new ArrayList<>();

        if (!CollectionUtils.isEmpty(values)) {
            for (String key : values.keySet()) {
                rowHeaders.add(key);
            }
            rowHeaders.add("Original price (in $)");
            rowHeaders.add("Discount");
            rowHeaders.add("HKG price (value)");
        }
        data.put(rowNum++, rowHeaders.toArray());
        //Headers prepared

        //Prepare data
        List<List<String>> colls = new LinkedList<>();
        for (Map.Entry<String, List<String>> entry : values.entrySet()) {
            colls.add(entry.getValue());
        }
        //Make permutations of data
        Collection<List<String>> permutations = permutations(colls);
        //Store data in Map
        if (!CollectionUtils.isEmpty(permutations)) {
            for (List<String> list : permutations) {
                List<String> rowData = new ArrayList<>();
                if (!CollectionUtils.isEmpty(list)) {
                    for (String string : list) {
                        rowData.add(string);
                    }
                    data.put(rowNum++, rowData.toArray());
                }
            }
        }
        //Data prepared
        File tempDir = FolderManagement.checkIsExists(FolderManagement.basePath, FolderManagement.TEMP, null);
        StringBuilder filePath = new StringBuilder(tempDir.getPath());
        filePath.append(File.separator);

        //Make xls file and write data into it
        //Blank workbook
        XSSFWorkbook workbook = new XSSFWorkbook();

        //Create a blank sheet
        XSSFSheet sheet = workbook.createSheet("Price List Data");

        sheet.protectSheet("password");

        XSSFFont font = workbook.createFont();
        font.setFontHeightInPoints((short) 10);
        font.setFontName("Arial");
        font.setBold(true);
        font.setItalic(false);

//        CellStyle style = workbook.createCellStyle();
//        style.setDataFormat(workbook.createDataFormat().getFormat("00.00%"));
        CellStyle style1 = workbook.createCellStyle();;
        style1.setAlignment(CellStyle.ALIGN_CENTER);
        style1.setFont(font);

        CellStyle unlockedCellStyle = workbook.createCellStyle();
        unlockedCellStyle.setLocked(false);

        CellStyle unlockedWithPercentageCellStyle = workbook.createCellStyle();
        unlockedWithPercentageCellStyle.setLocked(false);
        unlockedWithPercentageCellStyle.setDataFormat(workbook.createDataFormat().getFormat("00.00%"));
        //Iterate over data and write to sheet
        Set<Integer> keyset = data.keySet();
        int rownum = 0;
        for (Integer key : keyset) {
            Row row = sheet.createRow(rownum++);
            Object[] objArr = data.get(key);
            int cellnum = 0;
            for (Object obj : objArr) {
                Cell cell = row.createCell(cellnum++);
                if (obj instanceof String) {
                    cell.setCellValue((String) obj);
                } else if (obj instanceof Integer) {
                    cell.setCellValue((Integer) obj);
                }
            }
            char firstLetter = 'A';
            char opCol = (char) (firstLetter + values.keySet().size());
            char discountCol = (char) (firstLetter + (values.keySet().size() + 1));
            if (rownum != 1) {
                Cell opCell = row.createCell(objArr.length);
                opCell.setCellType(XSSFCell.CELL_TYPE_NUMERIC);
                opCell.setCellStyle(unlockedCellStyle);

                Cell disCell = row.createCell(objArr.length + 1);
                disCell.setCellStyle(unlockedWithPercentageCellStyle);

                String formulaText = "" + opCol + rownum + "-(" + discountCol + rownum + "*" + opCol + rownum + ")";

                Cell cell = row.createCell(objArr.length + 2);
                cell.setCellFormula(formulaText);
                cell.setCellType(XSSFCell.CELL_TYPE_FORMULA);
            }
            if (rownum == 1) {
                row.setRowStyle(style1);
            }
        }
        try {
            sheet.createFreezePane(0, 1);
            String fileNamePath = filePath.toString() + "priceList-" + loginDataBean.getId() + ".xlsx";
            try (FileOutputStream out = new FileOutputStream(new File(fileNamePath))) {
                workbook.write(out);
            }

            return fileNamePath;
        } catch (IOException e) {
            log.error("Error:", e);
        }
        //xls file made
        return null;
    }

    /**
     * Raj: Make permutations for list of list of strings
     *
     * <ul>Example
     * <li>Input = [ [a,b,c] , [1,2,3,4] ]</li>
     * <li>Output = [ {a,1} , {a,2} , {a,3} , {a,4} , {b,1} , {b,2} , {b,3} ,
     * {b,4} , {c,1} , {c,2} , {c,3} , {c,4} ]
     * </li>
     * </ul>
     *
     * @param collections Original list of collections which elements have to be
     * combined.
     * @return collection of lists with all permutations of original list.
     */
    public static Collection<List<String>> permutations(List<List<String>> collections) {
        if (collections == null || collections.isEmpty()) {
            return Collections.emptyList();
        } else {
            Collection<List<String>> res = Lists.newArrayList();
            permutationsImpl(collections, res, 0, new LinkedList<>());
            return res;
        }
    }

    /**
     * Recursive implementation for permutation
     */
    private static void permutationsImpl(List<List<String>> ori, Collection<List<String>> res, int depth, List<String> current) {
        // if depth equals number of original collections, final reached, add and return
        if (depth == ori.size()) {
            res.add(current);
            return;
        }

        // iterate from current collection and copy 'current' element N times, one for each element
        Collection<String> currentCollection = ori.get(depth);
        for (String element : currentCollection) {
            List<String> copy = Lists.newArrayList(current);
            copy.add(element);
            permutationsImpl(ori, res, depth + 1, copy);
        }
    }

    public ResponseEntity uploadPriceList(MultipartFile file, String fileName, String modelName, String[] chunkNumber, String[] flowTotalChunks) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        file.transferTo(convFile);

        Map<String, List<String>> values = foundationService.retrieveMapOfFieldsValuesForPriceList(loginDataBean.getCompanyId());
        Map<Integer, Object[]> data = new TreeMap<>();
        Integer dataNumber = 0;
        //Prepare headers for columns
        List<String> rowHeaders = new ArrayList<>();

        if (!CollectionUtils.isEmpty(values)) {
            for (String key : values.keySet()) {
                rowHeaders.add(key);
            }
            rowHeaders.add("Original price (in $)");
            rowHeaders.add("Discount");
            rowHeaders.add("HKG price (value)");
        }
        //Prepare data
        List<List<String>> colls = new LinkedList<>();
        for (Map.Entry<String, List<String>> entry : values.entrySet()) {
            colls.add(entry.getValue());
        }
        //Make permutations of data
        Collection<List<String>> permutations = permutations(colls);

        try (FileInputStream fileInputStream = new FileInputStream(convFile)) {
            XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);

            //Get first/desired sheet from the workbook
            XSSFSheet sheet = workbook.getSheetAt(0);

            //Iterate through each rows one by one
            Iterator<Row> rowIterator = sheet.iterator();
            Integer rowNumber = 0;
            Boolean headersMatched = false;
            Boolean requiredPriceError = false;
            Boolean combinationsNotMatched = false;
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                //For each row, iterate through all the columns
                Iterator<Cell> cellIterator = row.cellIterator();
                if (rowNumber != 0) {
                    Integer cellNumber = 0;
                    List<String> combinations = new ArrayList<>();
                    List<Object> vals = new ArrayList<>();
                    while (cellIterator.hasNext()) {
                        Cell cell = cellIterator.next();
                        if (cellNumber < values.keySet().size()) {
                            combinations.add(cell.getStringCellValue());
                            vals.add(cell.getStringCellValue());
                        } else if (cellNumber == values.keySet().size()) {
                            Double cellVal = null;
                            if ((Double) cell.getNumericCellValue() != null) {
                                if (cell.getCellType() == XSSFCell.CELL_TYPE_NUMERIC) {
                                    cellVal = (Double) cell.getNumericCellValue();
                                    vals.add((Double) cell.getNumericCellValue());
                                }
                            }
                            if (cellVal == null || cellVal.isNaN()) {
                                requiredPriceError = true;
                                break;
                            }
                        } else if (cellNumber == values.keySet().size() + 1) {
                            vals.add(cell.getNumericCellValue());
                        } else if (cellNumber == values.keySet().size() + 2) {
                            vals.add(cell.getNumericCellValue());
                        }
                        ++cellNumber;
                    }
                    if (permutations.contains(combinations)) {
                        permutations.remove(combinations);
                    } else {
                        combinationsNotMatched = true;
                        break;
                    }
                    data.put(dataNumber++, vals.toArray());
                } else {
                    List<String> uploadedRowHeaders = new ArrayList<>();
                    while (cellIterator.hasNext()) {
                        Cell cell = cellIterator.next();
                        uploadedRowHeaders.add(cell.getStringCellValue());
                    }
                    if (uploadedRowHeaders.equals(rowHeaders)) {
                        headersMatched = true;
                        data.put(dataNumber++, uploadedRowHeaders.toArray());
                    } else {
                        headersMatched = false;
                        break;
                    }
                }
                ++rowNumber;
            }
            if (!headersMatched) {
                return new ResponseEntity(null, ResponseCode.FAILURE, "Headers of sheet dows not matched with DB.Please download new template and try again.", null);
            } else {
            }

            if (permutations.isEmpty() && !combinationsNotMatched) {
            } else {
                return new ResponseEntity(null, ResponseCode.FAILURE, "There are some problem in combinations, please download new template and try again.", null);
            }

            if (requiredPriceError) {
                return new ResponseEntity(null, ResponseCode.FAILURE, "All prices are required, please fill price for all combinations.", null);
            } else {
            }

        }

        String tempFileName = FolderManagement.getTempFileName(loginDataBean.getCompanyId(), "PriceList", null, loginDataBean.getId(), fileName);
        //Store file in TEMP folder
        FolderManagement.storeFileInTemp(tempFileName, file.getBytes(), true);

        PriceListDataBean priceListDataBean = new PriceListDataBean();
        priceListDataBean.setData(data);
        priceListDataBean.setFileName(tempFileName);
        return new ResponseEntity(priceListDataBean, ResponseCode.SUCCESS, "Valid file uploaded", null);
    }

    public void savePriceList(PriceListDataBean priceListDataBean) throws IOException {
        Map<String, HkFieldEntity> fields = fieldService.retrieveMapOfDBFieldNameWithEntity(Arrays.asList(HkSystemConstantUtil.PlanStaticFieldName.CLARITY, HkSystemConstantUtil.PlanStaticFieldName.COLOR, HkSystemConstantUtil.PlanStaticFieldName.CUT, HkSystemConstantUtil.PlanStaticFieldName.FLUROSCENE), loginDataBean.getCompanyId());
        Map<String, String> fieldsNameToCode = new HashMap<>();
        Map<String, String> fieldsDbFieldNameToID = new HashMap<>();
        if (!CollectionUtils.isEmpty(fields)) {
            for (Map.Entry<String, HkFieldEntity> entry : fields.entrySet()) {
                fieldsNameToCode.put(entry.getValue().getFieldLabel(), String.valueOf(entry.getValue().getId()));
                fieldsDbFieldNameToID.put(String.valueOf(entry.getValue().getDbFieldName()), String.valueOf(entry.getValue().getId()));
            }
        }
        Map<String, String> valToCodeMap = new LinkedHashMap<>();
        Map<String, Long> caratRanges = new HashMap<>();
        Integer rowNumber = 1;
        Object[] headersList = priceListDataBean.getData().get(0);
        List<String> codeList = new ArrayList<>();
        for (int index = 1; index < headersList.length - 3; index++) {
            valToCodeMap.put((String) headersList[index], fieldsNameToCode.get((String) headersList[index]));
            codeList.add(valToCodeMap.get((String) headersList[index]));
        }
        Map<String, Map<String, Long>> codeToValMap = foundationService.retrieveMapOfValuesFromCode(codeList, loginDataBean.getCompanyId());
        List<HkCaratRangeEntity> activeCaratRanges = foundationService.retrieveCaratRangeByFranchiseAndStatus(loginDataBean.getCompanyId(), Arrays.asList(HkSystemConstantUtil.ACTIVE),null);
        if (!CollectionUtils.isEmpty(activeCaratRanges)) {
            for (HkCaratRangeEntity hkCaratRangeEntity : activeCaratRanges) {
                StringBuilder builder = new StringBuilder();
                builder.append(hkCaratRangeEntity.getMinValue()).append(" - ").append(hkCaratRangeEntity.getMaxValue());
                caratRanges.put(builder.toString(), hkCaratRangeEntity.getId());
            }
        }
        List<HkPriceListDetailEntity> priceListDetailEntity = new ArrayList<>();
        Integer seqNo = 0;
        while (priceListDataBean.getData().get(rowNumber) != null) {
            Object[] valuesOfRow = priceListDataBean.getData().get(rowNumber);
            HkPriceListDetailEntity priceListDetailEntity1 = new HkPriceListDetailEntity();

            if (valuesOfRow != null && valuesOfRow.length > 0 && !CollectionUtils.isEmpty(caratRanges)) {
                priceListDetailEntity1.setCaratRange(caratRanges.get((String) valuesOfRow[0]));
            }
            for (int index = 0; index < codeList.size(); index++) {
                if (codeList.get(index).equals(fieldsDbFieldNameToID.get(HkSystemConstantUtil.PlanStaticFieldName.CLARITY))) {
                    priceListDetailEntity1.setClarity(codeToValMap.get(fieldsDbFieldNameToID.get(HkSystemConstantUtil.PlanStaticFieldName.CLARITY)).get((String) valuesOfRow[index + 1]));
                } else if (codeList.get(index).equals(fieldsDbFieldNameToID.get(HkSystemConstantUtil.PlanStaticFieldName.COLOR))) {
                    priceListDetailEntity1.setColor(codeToValMap.get(fieldsDbFieldNameToID.get(HkSystemConstantUtil.PlanStaticFieldName.COLOR)).get((String) valuesOfRow[index + 1]));
                } else if (codeList.get(index).equals(fieldsDbFieldNameToID.get(HkSystemConstantUtil.PlanStaticFieldName.CUT))) {
                    priceListDetailEntity1.setCut(codeToValMap.get(fieldsDbFieldNameToID.get(HkSystemConstantUtil.PlanStaticFieldName.CUT)).get((String) valuesOfRow[index + 1]));
                } else if (codeList.get(index).equals(fieldsDbFieldNameToID.get(HkSystemConstantUtil.PlanStaticFieldName.FLUROSCENE))) {
                    priceListDetailEntity1.setFluorescence(codeToValMap.get(fieldsDbFieldNameToID.get(HkSystemConstantUtil.PlanStaticFieldName.FLUROSCENE)).get((String) valuesOfRow[index + 1]));
                }
            }
            priceListDetailEntity1.setOriginalPrice(Double.valueOf(String.valueOf(valuesOfRow[codeList.size() + 1])));
            if (Float.valueOf(String.valueOf(valuesOfRow[codeList.size() + 2])) != null) {
                priceListDetailEntity1.setDiscount(Float.valueOf(String.valueOf(valuesOfRow[codeList.size() + 2])));
            }
            priceListDetailEntity1.setHkgPrice(Double.valueOf(String.valueOf(valuesOfRow[codeList.size() + 3])));
            HkPriceListDetailEntityPK priceListDetailEntityPK = new HkPriceListDetailEntityPK();
            priceListDetailEntityPK.setSequenceNumber(seqNo++);
            priceListDetailEntity1.setHkPriceListDetailEntityPK(priceListDetailEntityPK);
            priceListDetailEntity1.setIsArchive(Boolean.FALSE);
            priceListDetailEntity.add(priceListDetailEntity1);
            //At last increase row number
            rowNumber++;
        }
        if (!CollectionUtils.isEmpty(priceListDetailEntity)) {
            foundationService.archiveOldPriceList(loginDataBean.getCompanyId());
            HkPriceListEntity pricelist = this.uploadSheet(priceListDataBean);
            if (pricelist != null) {
                for (HkPriceListDetailEntity hkPriceListDetailEntity : priceListDetailEntity) {
                    hkPriceListDetailEntity.setHkPriceListEntity(pricelist);
//                    hkPriceListDetailEntity.getHkPriceListDetailEntityPK().setPriceList(pricelist.getId());
                }
//                pricelist.setHkPriceListDetailEntityCollection(priceListDetailEntity);
//                foundationService.savePriceListDetailEntities(priceListDetailEntity);
//                pricelist.setUploadedOn(pricelist.getUploadedOn());
                pricelist.setHkPriceListDetailEntityCollection(priceListDetailEntity);
                foundationService.savePriceList(pricelist);
            }
        }
    }

    private HkPriceListEntity uploadSheet(PriceListDataBean priceListDataBean) throws IOException {
        if (priceListDataBean.getFileName() != null) {
            Date dat = new Date();
            Long companyId = 0l;
            long time = dat.getTime();
            String changedFileName = FolderManagement.changeFileName(priceListDataBean.getFileName(), time);
            StringBuilder tempFilePath = new StringBuilder(basePath);
            tempFilePath.append(File.separator).append(FolderManagement.TEMP).append(File.separator).append(priceListDataBean.getFileName());
            FolderManagement.moveFile(tempFilePath.toString(), changedFileName, time, false);

            HkPriceListEntity priceListEntity = new HkPriceListEntity();
            priceListEntity.setFranchise(companyId);
            priceListEntity.setStatus(HkSystemConstantUtil.ACTIVE);
            priceListEntity.setUploadedBy(loginDataBean.getId());
            priceListEntity.setUploadedOn(new Date());
            priceListEntity.setUploadedFileName(changedFileName);
            
            
//            foundationService.savePriceList(priceListEntity);
            return priceListEntity;
//            return priceListEntity;
        }
        return null;
    }

    public Map<Integer, Map<String, List<PriceListDataBean>>> preparePriceListData() {
        List<HkPriceListEntity> allPriceLists = foundationService.retrieveAllPriceLists(loginDataBean.getCompanyId());
        SimpleDateFormat dateFormattor = new SimpleDateFormat("yyyy");
        SimpleDateFormat monthFormattor = new SimpleDateFormat("MM");

        Map<Integer, Map<String, List<PriceListDataBean>>> result = new LinkedHashMap<>();
        if (!CollectionUtils.isEmpty(allPriceLists)) {
            for (HkPriceListEntity hkPriceListEntity : allPriceLists) {
                Date datetime = hkPriceListEntity.getUploadedOn();
                Integer year = Integer.valueOf(dateFormattor.format(datetime));
                Integer month = Integer.valueOf(monthFormattor.format(datetime));

                if (result.get(year) == null) {
                    result.put(year, new LinkedHashMap<>());
                }
                if (result.get(year).get(new DateFormatSymbols().getMonths()[month - 1]) == null) {
                    result.get(year).put(new DateFormatSymbols().getMonths()[month - 1], new ArrayList<>());
                }

                PriceListDataBean priceListDataBean = new PriceListDataBean();

                priceListDataBean.setFileName(hkPriceListEntity.getUploadedFileName());
                priceListDataBean.setUploadedOn(hkPriceListEntity.getUploadedOn());
                priceListDataBean.setId(hkPriceListEntity.getId());

                result.get(year).get(new DateFormatSymbols().getMonths()[month - 1]).add(priceListDataBean);
            }
        }
        return result;
    }

    public List<TreeViewDataBean> retrievePriceLists() {
        Map<Integer, Map<String, List<PriceListDataBean>>> result = this.preparePriceListData();

        List<TreeViewDataBean> dataBeans = new ArrayList<>();
        if (!CollectionUtils.isEmpty(result)) {
            for (Map.Entry<Integer, Map<String, List<PriceListDataBean>>> entry : result.entrySet()) {
                TreeViewDataBean dataBean = new TreeViewDataBean();
                dataBean.setDisplayName(String.valueOf(entry.getKey()));
                List<TreeViewDataBean> beans = new ArrayList<>();
                Integer count = 0;
                for (Map.Entry<String, List<PriceListDataBean>> entry1 : entry.getValue().entrySet()) {
                    TreeViewDataBean bean = new TreeViewDataBean();

                    bean.setId(entry.getKey());
                    bean.setDisplayName(entry1.getKey());
                    bean.setCategoryCount(entry1.getValue().size());
                    count = count + entry1.getValue().size();
                    beans.add(bean);
                }

                dataBean.setCategoryCount(count);
                dataBean.setChildren(beans);
                dataBeans.add(dataBean);
            }
        }
        return dataBeans;
    }

    public List<PriceListDataBean> retrievepricelistByMonthYear(TreeViewDataBean treeViewDataBean) {
        Map<Integer, Map<String, List<PriceListDataBean>>> result = this.preparePriceListData();

        if (!CollectionUtils.isEmpty(result)) {
            Map<String, List<PriceListDataBean>> record = result.get(Integer.valueOf(String.valueOf(treeViewDataBean.getId())));

            if (!CollectionUtils.isEmpty(record)) {
                return record.get(treeViewDataBean.getDisplayName());
            }
        }
        return null;
    }
}
