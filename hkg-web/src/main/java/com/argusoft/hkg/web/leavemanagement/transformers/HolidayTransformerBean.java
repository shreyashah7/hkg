/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.leavemanagement.transformers;

import com.argusoft.hkg.common.constantutil.HkSystemConstantUtil;
import com.argusoft.hkg.common.functionutil.HkSystemFunctionUtil;
import com.argusoft.hkg.core.HkFieldService;
import com.argusoft.hkg.core.HkHRService;
import com.argusoft.hkg.model.HkHolidayEntity;
import com.argusoft.hkg.nosql.core.HkCustomFieldService;
import com.argusoft.hkg.nosql.model.CustomField;
import com.argusoft.hkg.web.base.PrimaryKey;
import com.argusoft.hkg.web.base.ResponseCode;
import com.argusoft.hkg.web.common.databeans.MessageDataBean;
import com.argusoft.hkg.web.customfield.transformers.CustomFieldTransformerBean;
import com.argusoft.hkg.web.leavemanagement.databeans.HolidayDatabean;
import com.argusoft.hkg.web.center.usermanagement.databeans.LoginDataBean;
import com.argusoft.hkg.web.usermanagement.wrapper.UserManagementServiceWrapper;
import com.argusoft.hkg.web.util.SessionUtil;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletResponse;
import jxl.WorkbookSettings;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 *
 * @author vipul
 */
@Service
public class HolidayTransformerBean {

    @Autowired
    HkHRService hRService;
    @Autowired
    LoginDataBean loginDataBean;
    @Autowired
    SessionUtil hkSessionUtil;
    @Autowired
    HkCustomFieldService customFieldSevice;
    @Autowired
    HkFieldService fieldService;
    @Autowired
    UserManagementServiceWrapper umWrapper;

    @Autowired
    CustomFieldTransformerBean customFieldTransformerBean;

    /**
     * This method create new holiday.
     *
     * @param holiday
     *
     */
    public void createHoliday(HolidayDatabean holiday) {
        HkHolidayEntity holidayEntity = new HkHolidayEntity();
        holidayEntity = convertHolidayDatabeanToModel(holiday, "create", holidayEntity);
        umWrapper.createLocaleForEntity(holidayEntity.getHolidayTitle(), "Holiday", loginDataBean.getId(), loginDataBean.getCompanyId());
        hRService.saveHoliday(holidayEntity, umWrapper.retrieveAllUsersByFranchise(loginDataBean.getCompanyId()));
        Map<Long, Map<String, Object>> val = new HashMap<>();
        val.put(holidayEntity.getId(), holiday.getHolidayCustom());

        List<String> uiFieldList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(holiday.getDbType())) {
            for (Map.Entry<String, String> entrySet : holiday.getDbType().entrySet()) {
                uiFieldList.add(entrySet.getKey());
            }
        }
        Map<String, String> uiFieldMap = fieldService.retrieveUIFieldNameWithComponentTypes(uiFieldList);
        //Pass this map to makecustomfieldService
        List<CustomField> makeCustomField = customFieldSevice.makeCustomField(val, holiday.getDbType(),uiFieldMap, HkSystemConstantUtil.FeatureNameForCustomField.HOLIDAY.toString(), loginDataBean.getCompanyId(), holidayEntity.getId());
        //After that make Map of Section and there customfield
        Map<HkSystemConstantUtil.SectionNameForCustomField, List<CustomField>> map = new HashMap<>();
        map.put(HkSystemConstantUtil.SectionNameForCustomField.GENERAL, makeCustomField);
        //Pass this map to customFieldSevice.saveOrUpdate method.
        Long saveOrUpdate = customFieldSevice.saveOrUpdate(holidayEntity.getId(), HkSystemConstantUtil.FeatureNameForCustomField.HOLIDAY, loginDataBean.getCompanyId(), map);

    }

    /**
     * This method updates given holiday.
     *
     * @param holiday
     *
     */
    public void updateHoliday(HolidayDatabean holiday) {
        // HkHolidayEntity holidayEntity = convertHolidayDatabeanToModel(holiday, "update");
        HkHolidayEntity holidayEntity = hRService.retrieveHolidayById(holiday.getId());
        List<Long> notifyUserIds = new ArrayList<>();
        if (HkSystemFunctionUtil.convertToServerDate(holiday.getStartDt(), loginDataBean.getClientRawOffsetInMin())
                .compareTo(holidayEntity.getStartDt()) == 0
                && HkSystemFunctionUtil.convertToServerDate(holiday.getEndDt(), loginDataBean.getClientRawOffsetInMin())
                .compareTo(holidayEntity.getEndDt()) == 0) {
            List<String> featureList = new ArrayList<>();
            featureList.add(HkSystemConstantUtil.Feature.HOLIDAY_ADD_EDIT);
            notifyUserIds = new ArrayList<>(umWrapper.searchUsersByFeatureName(featureList, loginDataBean.getCompanyId()));
        } else {
            notifyUserIds = umWrapper.retrieveAllUsersByFranchise(loginDataBean.getCompanyId());
        }
        HkHolidayEntity holidayEntityForUpdate = convertHolidayDatabeanToModel(holiday, "update", holidayEntity);
        umWrapper.createLocaleForEntity(holidayEntity.getHolidayTitle(), "Holiday", loginDataBean.getId(), loginDataBean.getCompanyId());
        hRService.saveHoliday(holidayEntityForUpdate, notifyUserIds);
        Map<Long, Map<String, Object>> val = new HashMap<>();
        val.put(holiday.getId(), holiday.getHolidayCustom());

        List<String> uiFieldList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(holiday.getDbType())) {
            for (Map.Entry<String, String> entrySet : holiday.getDbType().entrySet()) {
                uiFieldList.add(entrySet.getKey());
            }
        }
        Map<String, String> uiFieldMap = fieldService.retrieveUIFieldNameWithComponentTypes(uiFieldList);
        //Pass this map to makecustomfieldService
        List<CustomField> makeCustomField = customFieldSevice.makeCustomField(val, holiday.getDbType(),uiFieldMap, HkSystemConstantUtil.FeatureNameForCustomField.HOLIDAY.toString(), loginDataBean.getCompanyId(), holiday.getId());
        //After that make Map of Section and there customfield
        Map<HkSystemConstantUtil.SectionNameForCustomField, List<CustomField>> map = new HashMap<>();
        map.put(HkSystemConstantUtil.SectionNameForCustomField.GENERAL, makeCustomField);
        //Pass this map to customFieldSevice.saveOrUpdate method.
        Long saveOrUpdate = customFieldSevice.saveOrUpdate(holiday.getId(), HkSystemConstantUtil.FeatureNameForCustomField.HOLIDAY, loginDataBean.getCompanyId(), map);

    }

    /**
     * This method converts holiday databean to model.
     *
     * @param holiday
     * @param action either create or update
     * @return HkHolidayEntity
     */
    private HkHolidayEntity convertHolidayDatabeanToModel(HolidayDatabean holiday, String action, HkHolidayEntity holidayEntity) {
//        HkHolidayEntity holidayEntity = new HkHolidayEntity();
        holidayEntity.setId(holiday.getId());
        holidayEntity.setHolidayTitle(holiday.getTitle());
        holidayEntity.setStartDt(HkSystemFunctionUtil.convertToServerDate(holiday.getStartDt(), loginDataBean.getClientRawOffsetInMin()));
        holidayEntity.setEndDt(HkSystemFunctionUtil.convertToServerDate(holiday.getEndDt(), loginDataBean.getClientRawOffsetInMin()));
        holidayEntity.setFranchise(loginDataBean.getCompanyId());
        if (action != null && action.equalsIgnoreCase("create")) {
            holidayEntity.setCreatedBy(loginDataBean.getId());
            holidayEntity.setCreatedOn(new Date());
        }
        holidayEntity.setLastModifiedBy(loginDataBean.getId());
        holidayEntity.setLastModifiedOn(new Date());
        holidayEntity.setIsActive(true);
        holidayEntity.setIsArchive(false);
        return holidayEntity;
    }

    /**
     * This returns all the holiday of current year for particular franchise.
     *
     * @param franchiesId
     * @param isActive
     * @return List of holiday databeans sort by holiday start date.
     */
    public List<HolidayDatabean> retieveAllHoliday(Long franchiesId, boolean isActive) {
        List<HkHolidayEntity> hkHolidayEntityList = hRService.retrieveHolidaysByCriteria(null, null, null, true, franchiesId, Boolean.FALSE);
        List<HolidayDatabean> holidayDatabeansList = new ArrayList<>();
        if (hkHolidayEntityList != null && !hkHolidayEntityList.isEmpty()) {
            //  Map<Long, Map<HkSystemConstantUtil.SectionNameForCustomField, List<Map<Long, Map<String, Object>>>>> retrieveDocumentByInstanceIds = customFieldSevice.retrieveDocumentByInstanceIds(null, HkSystemConstantUtil.FeatureNameForCustomField.HOLIDAY, loginDataBean.getCompanyId());
            holidayDatabeansList = convertHolidayModelToDatabean(hkHolidayEntityList);
            Collections.sort(holidayDatabeansList, new Comparator() {
                @Override
                public int compare(Object o1, Object o2) {
                    HolidayDatabean holidayDb = (HolidayDatabean) o1;
                    HolidayDatabean holidayDbTemp = (HolidayDatabean) o2;
                    return holidayDb.getStartDt().compareTo(holidayDbTemp.getStartDt());
                }
            });
        }

        return holidayDatabeansList;
    }

    /**
     * This method converts holiday model to databean.
     *
     * @param hkHolidayEntity
     * @return HolidayDatabean
     */
    private HolidayDatabean convertHolidayModelToDatabean(HkHolidayEntity hkHolidayEntity, Map<Long, Map<HkSystemConstantUtil.SectionNameForCustomField, List<Map<Long, Map<String, Object>>>>> retrieveDocumentByInstanceIds) {
        HolidayDatabean holidayDatabean = new HolidayDatabean();
        Date currentDate = new Date();
        if (hkHolidayEntity != null) {
            if ((hkHolidayEntity.getEndDt().compareTo(currentDate)) < 0) {
                holidayDatabean.setEditFlage(Boolean.TRUE);
            } else {
                holidayDatabean.setEditFlage(Boolean.FALSE);
            }
            holidayDatabean.setId(hkHolidayEntity.getId());
            holidayDatabean.setTitle(hkHolidayEntity.getHolidayTitle());
            holidayDatabean.setStartDt(HkSystemFunctionUtil.convertToClientDate(hkHolidayEntity.getStartDt(), loginDataBean.getClientRawOffsetInMin()));
            holidayDatabean.setEndDt(HkSystemFunctionUtil.convertToClientDate(hkHolidayEntity.getEndDt(), loginDataBean.getClientRawOffsetInMin()));

        }
        if (!CollectionUtils.isEmpty(retrieveDocumentByInstanceIds)) {
//            System.out.println("Map not null");
            Map<HkSystemConstantUtil.SectionNameForCustomField, List<Map<Long, Map<String, Object>>>> sectionWiseMap = retrieveDocumentByInstanceIds.get(hkHolidayEntity.getId());
//            System.out.println("sectionWiseMap : " + sectionWiseMap);
            if (sectionWiseMap != null) {
                List<Map<Long, Map<String, Object>>> maps = sectionWiseMap.get(HkSystemConstantUtil.SectionNameForCustomField.GENERAL);
//                System.out.println("maps :" + maps);
                if (maps != null) {
                    for (Map<Long, Map<String, Object>> map : maps) {
                        holidayDatabean.setHolidayCustom(map.get(hkHolidayEntity.getId()));
                    }
                }
            }
        }
        if (holidayDatabean.getHolidayCustom() == null) {
            holidayDatabean.setHolidayCustom(new HashMap<String, Object>());
        }
        return holidayDatabean;
    }

    /**
     * This method removes holiday by holiday Id.
     *
     * @param holidayId
     */
    public void removeHoliday(PrimaryKey<Long> holidayId) {
        HolidayDatabean holidayDatabean = this.retieveHolidayById(holidayId.getPrimaryKey());
        hRService.removeHoliday(holidayId.getPrimaryKey(), umWrapper.retrieveAllUsersByFranchise(loginDataBean.getCompanyId()));
        //System.out.println("holidayDatabean :" + holidayDatabean);
        umWrapper.deleteLocaleForEntity(holidayDatabean.getTitle(), "Holiday", "CONTENT", loginDataBean.getId(), loginDataBean.getCompanyId());
    }

    /**
     * This method collect all the holiday from xlsx file and validates file's
     * data.
     *
     * @param inputStream
     * @return list of messageDatabeans which contains success,failure and
     * warning messages.
     */
    public List<MessageDataBean> validateHolidayWorksheet(InputStream inputStream) {
        List<MessageDataBean> messageDatabeanList = new ArrayList<>();
        List<HolidayDatabean> hkHolidayDataBeanList;
        XSSFWorkbook workbook = null;
        XSSFSheet sheet = null;
        try {
            WorkbookSettings ws = new WorkbookSettings();
            ws.setLocale(new Locale("en", "EN"));
            workbook = new XSSFWorkbook(inputStream);
        } catch (Exception ex) {
            //System.out.println("insde scathc");
            MessageDataBean messageDataBean = new MessageDataBean(null, ResponseCode.FAILURE, "Please upload valid file.");
            messageDatabeanList.add(messageDataBean);
        }
        if (workbook != null) {
            sheet = workbook.getSheetAt(0);
        }
        if (sheet != null) {
            if (sheet.getLastRowNum() < 1) {
                MessageDataBean messageDataBean = new MessageDataBean(null, ResponseCode.FAILURE, "Can not upload file because uploaded excel sheet is blank.");
                messageDatabeanList.add(messageDataBean);
            } else {
                int columns = sheet.getRow(0).getLastCellNum();
                int row = sheet.getLastRowNum() + 1;
                Boolean isXmlDataHeadingInProperSequence = false;
                XSSFRow data = sheet.getRow(0);
                XSSFCell cell = data.getCell(0);

                String title = null;
                if (cell != null) {
                    title = cell.toString();
                }
                if (title != null && title.trim().equalsIgnoreCase("Reason for holiday")) {
                    String fromDate = data.getCell(1).toString();
                    if (fromDate != null && fromDate.trim().equalsIgnoreCase("Start date(dd/mm/yyyy)")) {
                        String toDate = data.getCell(2).toString();
                        if (toDate != null && toDate.trim().equalsIgnoreCase("End date(dd/mm/yyyy)")) {
                            isXmlDataHeadingInProperSequence = true;
                        }

                    }
                }
                if (isXmlDataHeadingInProperSequence) {
                    hkHolidayDataBeanList = new ArrayList<>();
                    for (int i = 1; i < row; i++) {
                        Map<String, String> messageMap = new HashMap<>();
                        HolidayDatabean hkHolidayDataBean = new HolidayDatabean();
                        data = sheet.getRow(i);
                        Date fromDate = null;
                        Date toDate = null;
                        if (sheet.getRow(i) != null && sheet.getRow(i).getLastCellNum() > 3) {
                            MessageDataBean messageDataBean = new MessageDataBean(null, ResponseCode.FAILURE, "you can not enter value at column " + sheet.getRow(i).getLastCellNum() + ".Only enter value at specified column.");
                            messageDatabeanList.add(messageDataBean);
                        }
                        for (int j = 0; j < columns; j++) {
                            if (data != null) {
                                cell = data.getCell(j);
                                if (j == 0) {
                                    if (cell != null && cell.toString().length() > 0) {
                                        hkHolidayDataBean.setTitle(cell.toString());
                                    } else {
                                        MessageDataBean messageDataBean = new MessageDataBean(null, ResponseCode.FAILURE, "Reason for holiday should not be blank at row " + i);
                                        messageDatabeanList.add(messageDataBean);
                                    }
                                    if (cell != null && cell.toString().length() > 100) {
                                        MessageDataBean messageDataBean = new MessageDataBean(null, ResponseCode.FAILURE, cell.toString() + " is too long - Max limit is 100 " + i);
                                        messageDatabeanList.add(messageDataBean);
                                    }
                                } else if (j == 1) {
                                    if (cell != null && cell.toString().length() > 0) {
                                        try {
                                            fromDate = cell.getDateCellValue();
                                            hkHolidayDataBean.setStartDt(cell.getDateCellValue());
                                        } catch (Exception e) {
                                            MessageDataBean messageDataBean = new MessageDataBean(null, ResponseCode.FAILURE, "Specifies appropriate value for Start date at row  " + i);
                                            messageDatabeanList.add(messageDataBean);
                                        }

                                    } else {

                                        MessageDataBean messageDataBean = new MessageDataBean(null, ResponseCode.FAILURE, "Blank Start date is not allowed for row  " + i);
                                        messageDatabeanList.add(messageDataBean);
                                    }
                                } else if (j == 2) {
                                    if (cell != null && cell.toString().trim().length() > 0) {
                                        try {
                                            toDate = cell.getDateCellValue();
                                            hkHolidayDataBean.setEndDt(toDate);
                                        } catch (Exception e) {
                                            MessageDataBean messageDataBean = new MessageDataBean(null, ResponseCode.FAILURE, "Specifies appropriate value for End date at row  " + i);
                                            messageDatabeanList.add(messageDataBean);
                                        }

                                    } else {
                                        MessageDataBean messageDataBean = new MessageDataBean(null, ResponseCode.FAILURE, "Blank End date is not allowed for row  " + i);
                                        messageDatabeanList.add(messageDataBean);
                                    }
                                }
                            }
                        }
                        //System.out.println("fromDate :"+fromDate);
                        //System.out.println("toDate :"+toDate);
                        if (fromDate != null && toDate != null) {
                            //System.out.println("inside not null");
                            if (fromDate.compareTo(toDate) > 0) {
                                MessageDataBean messageDataBean = new MessageDataBean(null, ResponseCode.FAILURE, "End date can not before start date at row " + i);
                                messageDatabeanList.add(messageDataBean);
                            }
                            Calendar fromCal = Calendar.getInstance();
                            fromCal.setTime(fromDate);

                            Calendar toCal = Calendar.getInstance();
                            toCal.setTime(toDate);

                            Calendar currCal = Calendar.getInstance();
                            currCal.setTime(new Date());
                            int currentYear = currCal.get(Calendar.YEAR);
                            int fromYear = fromCal.get(Calendar.YEAR);
                            int toYear = toCal.get(Calendar.YEAR);
                            if (fromYear != currentYear || toYear != currentYear) {
                                MessageDataBean messageDataBean = new MessageDataBean(null, ResponseCode.FAILURE, "Start date or end date  should be current year's date for row Number " + i);
                                messageDatabeanList.add(messageDataBean);
                            }

                        }

                        //validating data against Database
                        List<HkHolidayEntity> hkHolidayEntitysList = isHolidayExists(hkHolidayDataBean);
                        int priority = 0;
                        if (hkHolidayEntitysList != null && !hkHolidayEntitysList.isEmpty() && hkHolidayEntitysList.size() > 0) {
                            for (HkHolidayEntity hkHolidayEntity : hkHolidayEntitysList) {
                                if ((hkHolidayDataBean.getEndDt().compareTo(hkHolidayEntity.getEndDt()) == 0
                                        || hkHolidayDataBean.getStartDt().compareTo(hkHolidayEntity.getStartDt()) == 0
                                        || (hkHolidayDataBean.getStartDt().compareTo(hkHolidayEntity.getEndDt()) < 0
                                        && hkHolidayDataBean.getStartDt().compareTo(hkHolidayEntity.getStartDt()) > 0))
                                        && !hkHolidayEntity.getId().equals(hkHolidayDataBean.getId())) {
                                    priority = 1;
                                    // break;
                                }
                                if (hkHolidayEntity.getHolidayTitle().equals(hkHolidayDataBean.getTitle())) {
                                    priority = 2;
                                }
                                if (priority == 1) {
                                    MessageDataBean messageDataBean = new MessageDataBean(null, ResponseCode.WARNING, hkHolidayDataBean.getTitle() + " overlapping with " + hkHolidayEntity.getHolidayTitle());
                                    messageDatabeanList.add(messageDataBean);
                                } else if (priority == 2) {
                                    MessageDataBean messageDataBean = new MessageDataBean(1l, ResponseCode.FAILURE, hkHolidayDataBean.getTitle() + " already exists");
                                    messageDatabeanList.add(messageDataBean);
                                }
                            }

                        }
                        for (HolidayDatabean databean : hkHolidayDataBeanList) {
                            if (databean.getTitle() != null && hkHolidayDataBean.getTitle() != null && databean.getTitle().equalsIgnoreCase(hkHolidayDataBean.getTitle())) {
                                MessageDataBean messageDataBean = new MessageDataBean(null, ResponseCode.FAILURE, hkHolidayDataBean.getTitle() + " is already exists");
                                messageDatabeanList.add(messageDataBean);
                            }
                            if (hkHolidayDataBean.getEndDt() != null
                                    && databean.getEndDt() != null && hkHolidayDataBean.getStartDt() != null
                                    && databean.getStartDt() != null) {
                                if ((hkHolidayDataBean.getEndDt().compareTo(databean.getEndDt()) == 0
                                        || hkHolidayDataBean.getStartDt().compareTo(databean.getStartDt()) == 0
                                        || (hkHolidayDataBean.getStartDt().compareTo(databean.getEndDt()) < 0
                                        && hkHolidayDataBean.getStartDt().compareTo(databean.getStartDt()) > 0))) {
                                    MessageDataBean messageDataBean = new MessageDataBean(null, ResponseCode.WARNING, hkHolidayDataBean.getTitle() + " overlapping with " + databean.getTitle());
                                    messageDatabeanList.add(messageDataBean);
                                }
                            }
                        }
                        hkHolidayDataBeanList.add(hkHolidayDataBean);
                    }
                    //System.out.println("hkHolidayDataBeanList :"+hkHolidayDataBeanList);
                    List<HkHolidayEntity> hkHolidayEntityList = new ArrayList<>();
                    for (HolidayDatabean hkHolidayDataBean : hkHolidayDataBeanList) {
                        HkHolidayEntity holidayEntity = new HkHolidayEntity();
                        holidayEntity = this.convertHolidayDatabeanToModel(hkHolidayDataBean, "create", holidayEntity);
                        hkHolidayEntityList.add(holidayEntity);
                    }
                    hkSessionUtil.setHkHolidayList(hkHolidayEntityList);
                    if (messageDatabeanList.isEmpty()) {
                        if (!hkHolidayDataBeanList.isEmpty()) {
                            hRService.saveAllHolidays(hkHolidayEntityList);
                            hkSessionUtil.setHkHolidayList(null);
                            MessageDataBean messageDataBean = new MessageDataBean(null, ResponseCode.SUCCESS, "File Uploaded Successfully");
                            messageDatabeanList.add(messageDataBean);
                        }

                    }
                } else {
                    MessageDataBean messageDataBean = new MessageDataBean(null, ResponseCode.FAILURE, "Field Must be in sequence e.g Reason for holiday,Start date(dd/mm/yyyy),and End date(dd/mm/yyyy)");
                    messageDatabeanList.add(messageDataBean);
                }

            }
        }
        List<MessageDataBean> uniqueResult = new ArrayList<MessageDataBean>();
        Set<String> titles = new HashSet<String>();
        for (MessageDataBean item : messageDatabeanList) {
            if (titles.add(item.getMessage())) {
                uniqueResult.add(item);
            }
        }
        return uniqueResult;
    }

    /**
     * This method checks given holiday exists or not.
     *
     * @param holiday
     * @return list of holiday entity.
     */
    public List<HkHolidayEntity> isHolidayExists(HolidayDatabean holiday) {
        if (holiday != null && holiday.getTitle() != null) {
            HkHolidayEntity hkHolidayEntity = new HkHolidayEntity();
            hkHolidayEntity = convertHolidayDatabeanToModel(holiday, null, hkHolidayEntity);
            List<HkHolidayEntity> holidayEntitysList = hRService.retrieveHolidaysByCriteria(hkHolidayEntity.getHolidayTitle(), hkHolidayEntity.getStartDt(), hkHolidayEntity.getEndDt(), true, loginDataBean.getCompanyId(), Boolean.FALSE);

            if (holidayEntitysList != null) {
                return holidayEntitysList;
            }
        }
        return null;
    }

    /**
     * This method download xlsx file which contains format for adding holiday
     * in to xlsx file.
     *
     * @param response
     */
    public void downloadHolidayTemplate(HttpServletResponse response) {
        try {
            //Write the workbook in file system
            InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("templates/HolidayListFormatForupload.xlsx");
            URL resource = this.getClass().getClassLoader().getResource("templates/HolidayListFormatForupload.xlsx");
            File xslFile = new File(resource.toURI());
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment; filename=" + xslFile.getName());
            OutputStream out = response.getOutputStream();
            workbook.write(out);
            response.flushBuffer();
            response.getOutputStream().flush();
            response.getOutputStream().close();
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method download xlsx file which contains all the holiday of current
     * year.
     *
     * @param response
     */
    public void downloadHolidayList(HttpServletResponse response) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Holiday List");

        int rownum = 0;
        XSSFRow headerRow = sheet.createRow(rownum);

        XSSFCell reasonHoliday = headerRow.createCell(0);
        reasonHoliday.setCellValue((String) "Reason For Holiday");

        XSSFCell fromDate = headerRow.createCell(1);
        fromDate.setCellValue((String) "Start date(dd/mm/yyyy)");

        XSSFCell endDate = headerRow.createCell(2);
        endDate.setCellValue((String) "End date(dd/mm/yyyy)");
        List<HkHolidayEntity> hkHolidayEntityList = hRService.retrieveHolidaysByCriteria(null, null, null, true, loginDataBean.getCompanyId(), Boolean.FALSE);
        if (hkHolidayEntityList != null) {
            for (HkHolidayEntity holidayEntity : hkHolidayEntityList) {
                XSSFRow row = sheet.createRow(++rownum);
                int cellnum = 0;
                XSSFCell cell = row.createCell(cellnum++);
                cell.setCellValue((String) holidayEntity.getHolidayTitle());

                DateFormat dateFormate = new SimpleDateFormat("dd/MM/yyyy");
                String startDate = dateFormate.format(holidayEntity.getStartDt());
                String endDate1 = dateFormate.format(holidayEntity.getEndDt());

                cell = row.createCell(cellnum++);
                cell.setCellValue(startDate);

                cell = row.createCell(cellnum++);
                cell.setCellValue(endDate1);
            }
        }
        try {
            //Write the workbook in file system
            Calendar c = Calendar.getInstance();

            String fileName = "Holidays_" + c.get(Calendar.YEAR);
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName + ".xlsx");
            OutputStream out = response.getOutputStream();
            workbook.write(out);
            response.flushBuffer();
            response.getOutputStream().flush();
            response.getOutputStream().close();
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is used to add holiday which is overlapping on same date of
     * existing holiday.
     *
     *
     * @return Boolean.
     */
    public Boolean forceHolidayAdd() {
        List<HkHolidayEntity> holidayEntitysList = hkSessionUtil.getHkHolidayList();
        if (holidayEntitysList != null && !holidayEntitysList.isEmpty()) {
            hRService.saveAllHolidays(holidayEntitysList);
            hkSessionUtil.setHkHolidayList(null);
            return true;
        } else {
            return false;
        }
    }

    /**
     * This returns all the reason for holiday of previous year for particular
     * franchise.
     *
     * @return List of reason for holiday.
     */
    public List<String> retrievePreviousYearDistinctHoliday(String object, Long franchieID, Boolean archiveStatus) {
        List<String> previousYearHoliday = null;
        previousYearHoliday = hRService.searchPreviousYearsHolidaysByTitle(null, franchieID, Boolean.FALSE);
        if (previousYearHoliday != null) {
            return previousYearHoliday;
        }
        return previousYearHoliday;
    }

    public HolidayDatabean retieveHolidayById(Long holidayId) {
        HkHolidayEntity holidayEntity = hRService.retrieveHolidayById(holidayId);
        HolidayDatabean holidayDatabean = null;
        Map<HkSystemConstantUtil.SectionNameForCustomField, List<Map<Long, Map<String, Object>>>> retrieveDocumentByInstanceId = customFieldTransformerBean.retrieveDocumentByInstanceId(holidayId, HkSystemConstantUtil.FeatureNameForCustomField.HOLIDAY, loginDataBean.getCompanyId());
        Map<Long, Map<HkSystemConstantUtil.SectionNameForCustomField, List<Map<Long, Map<String, Object>>>>> map = new HashMap<>();
        map.put(holidayId, retrieveDocumentByInstanceId);
        if (holidayEntity != null) {
            holidayDatabean = this.convertHolidayModelToDatabean(holidayEntity, map);
        }

        return holidayDatabean;
    }

    public List<HolidayDatabean> searchHoliday(String query, Long franchiseID) {
        List<HolidayDatabean> holidayDatabeansList = new ArrayList<>();
        holidayDatabeansList = convertHolidayModelToDatabean(hRService.searchHolidays(query, franchiseID));
        return holidayDatabeansList;
    }

    private List<HolidayDatabean> convertHolidayModelToDatabean(List<HkHolidayEntity> hkHolidayEntityList) {
        List<HolidayDatabean> holidayDatabeansList = new ArrayList<>();
        if (hkHolidayEntityList != null && !CollectionUtils.isEmpty(hkHolidayEntityList)) {
            for (HkHolidayEntity hkHolidayEntity : hkHolidayEntityList) {
                HolidayDatabean holidayDatabean = new HolidayDatabean();
                Date currentDate = new Date();
                if (hkHolidayEntity != null) {
                    if ((hkHolidayEntity.getStartDt().compareTo(currentDate)) < 0) {
                        holidayDatabean.setEditFlage(Boolean.FALSE);
                    } else {
                        holidayDatabean.setEditFlage(Boolean.TRUE);
                    }
                    holidayDatabean.setId(hkHolidayEntity.getId());
                    holidayDatabean.setTitle(hkHolidayEntity.getHolidayTitle());
                    holidayDatabean.setStartDt(HkSystemFunctionUtil.convertToClientDate(hkHolidayEntity.getStartDt(), loginDataBean.getClientRawOffsetInMin()));
                    holidayDatabean.setEndDt(HkSystemFunctionUtil.convertToClientDate(hkHolidayEntity.getEndDt(), loginDataBean.getClientRawOffsetInMin()));

                }

                holidayDatabeansList.add(holidayDatabean);
            }
        }
        return holidayDatabeansList;
    }
    
    /**
     * This returns all the holiday of current year for particular franchise.
     *
     * @param franchiesId
     * @param isActive
     * @return List of holiday databeans sort by holiday start date.
     */
    public List<Date> retieveAllHolidayDates(Long franchiesId, boolean isActive) {
        Set<Date> dateList = new HashSet<>();
        List<HkHolidayEntity> hkHolidayEntityList = hRService.retrieveHolidaysByCriteria(null, null, null, true, franchiesId, Boolean.FALSE);
        List<HolidayDatabean> holidayDatabeansList = new ArrayList<>();
        if (hkHolidayEntityList != null && !hkHolidayEntityList.isEmpty()) {
            for(HkHolidayEntity holidayEntity : hkHolidayEntityList){
                Calendar startDate = Calendar.getInstance();
                Calendar endDate = Calendar.getInstance();
                startDate.setTime(holidayEntity.getStartDt());
                endDate.setTime(holidayEntity.getEndDt());
                if(startDate.getTime().equals(endDate.getTime())){
                    dateList.add(startDate.getTime());
                }else{
                    while(startDate.before(endDate)){
                        dateList.add(startDate.getTime());
                        startDate.add(Calendar.DATE, 1);
                    }
                    dateList.add(endDate.getTime());
                }
            }
            return new ArrayList<>(dateList);
        }

        return null;
    }
}
