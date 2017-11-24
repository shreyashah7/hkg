/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.leavemanagement.controllers;

import com.argusoft.hkg.model.HkHolidayEntity;
import com.argusoft.hkg.web.base.PrimaryKey;
import com.argusoft.hkg.web.base.ResponseCode;
import com.argusoft.hkg.web.base.ResponseEntity;
import com.argusoft.hkg.web.base.controllers.BaseController;
import com.argusoft.hkg.web.center.usermanagement.databeans.LoginDataBean;
import com.argusoft.hkg.web.common.databeans.MessageDataBean;
import com.argusoft.hkg.web.leavemanagement.databeans.HolidayDatabean;
import com.argusoft.hkg.web.leavemanagement.transformers.HolidayTransformerBean;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author vipul
 */
@RestController
@RequestMapping("/holiday")
public class HolidayController extends BaseController<HolidayDatabean, Long> {

    @Autowired
    HolidayTransformerBean holidayTransformerBean;
    @Autowired
    LoginDataBean loginDataBean;

    /**
     * This returns all the holiday of current year for particular franchise.
     *
     * @return List of holiday.
     */
    @Override
    public ResponseEntity<List<HolidayDatabean>> retrieveAll() {
        List<HolidayDatabean> hkManageHolidayDatabeanList = null;
        hkManageHolidayDatabeanList = holidayTransformerBean.retieveAllHoliday(loginDataBean.getCompanyId(), Boolean.FALSE);
        return new ResponseEntity(hkManageHolidayDatabeanList, ResponseCode.SUCCESS, null, null, false);
    }

    /**
     * This method accepts xlsx file which contains list of holidays and
     * validates file's data.
     *
     * @return success of failure message .
     */
    @RequestMapping(value = "/uploadholidayworksheet", method = RequestMethod.POST, produces = {"application/json"})
    ResponseEntity<HolidayDatabean> uploadHolidayWorksheet(@RequestBody MultipartFile file, @RequestParam("fileName") String fileName) throws IOException {
        List<MessageDataBean> messageDataBeanList = new ArrayList<>();
        //System.out.println("file :"+file.getInputStream());
        messageDataBeanList = holidayTransformerBean.validateHolidayWorksheet(file.getInputStream());
        return new ResponseEntity<HolidayDatabean>(null, ResponseCode.SUCCESS, null, messageDataBeanList, false);
    }

    /**
     * This method download xlsx file which contains format for adding holiday
     * in to xlsx file.
     *
     * @return response which contains xslx file.
     */
    @RequestMapping(value = "/downloadholidaytemplate", method = RequestMethod.GET, produces = {"application/vnd.ms-excel"})
    @ResponseStatus(value = HttpStatus.OK)
    public void downloadHolidayTemplate(HttpServletResponse response) {
        holidayTransformerBean.downloadHolidayTemplate(response);
    }

    /**
     * This method download xlsx file which contains all the holiday of current
     * year.
     *
     * @return xlsx file.
     */
    @RequestMapping(value = "/downloadholidaylist", method = RequestMethod.GET, produces = {"application/vnd.ms-excel"})
    @ResponseStatus(value = HttpStatus.OK)
    public void downloadHolidayList(HttpServletResponse response) {

        holidayTransformerBean.downloadHolidayList(response);
    }

    @Override
    public ResponseEntity<HolidayDatabean> retrieveById(@RequestBody PrimaryKey<Long> primaryKey) {
        HolidayDatabean manageHolidayDatabean = null;
        manageHolidayDatabean = holidayTransformerBean.retieveHolidayById(primaryKey.getPrimaryKey());
        return new ResponseEntity(manageHolidayDatabean, ResponseCode.SUCCESS, null, null, false);
    }

    /**
     * This method removes holiday .
     *
     * @param primaryKey
     * @return success or failure message.
     */
    @Override
    public ResponseEntity<HolidayDatabean> deleteById(@RequestBody PrimaryKey<Long> primaryKey) {
        holidayTransformerBean.removeHoliday(primaryKey);
        return new ResponseEntity<HolidayDatabean>(null, ResponseCode.SUCCESS, "Holiday removed successfully", null, false);
    }

    /**
     * This method updates given holiday.
     *
     * @param hkManageHolidayDatabean
     * @return success or failure message.
     */
    @Override
    public ResponseEntity<PrimaryKey<Long>> update(@Valid @RequestBody HolidayDatabean hkManageHolidayDatabean) {
        if (hkManageHolidayDatabean.isForceEdit() != null && hkManageHolidayDatabean.isForceEdit()) {
            holidayTransformerBean.updateHoliday(hkManageHolidayDatabean);
            return new ResponseEntity<PrimaryKey<Long>>(null, ResponseCode.SUCCESS, "Holiday saved successfully", null, false);
        }
        List<HkHolidayEntity> hkHolidayEntitysList = holidayTransformerBean.isHolidayExists(hkManageHolidayDatabean);
        int priority = 0;
        String msg = "";

        List<MessageDataBean> messageDataBeanList = new ArrayList<>();
        if (!hkHolidayEntitysList.isEmpty()) {
            for (HkHolidayEntity hkHolidayEntity : hkHolidayEntitysList) {
                if ((hkManageHolidayDatabean.getEndDt().compareTo(hkHolidayEntity.getEndDt()) == 0
                        || hkManageHolidayDatabean.getStartDt().compareTo(hkHolidayEntity.getStartDt()) == 0
                        || (hkManageHolidayDatabean.getStartDt().compareTo(hkHolidayEntity.getEndDt()) < 0
                        && hkManageHolidayDatabean.getStartDt().compareTo(hkHolidayEntity.getStartDt()) > 0))
                        && !hkHolidayEntity.getId().equals(hkManageHolidayDatabean.getId())) {
                    priority = 1;
                    msg = hkManageHolidayDatabean.getTitle() + " overlapping with " + hkHolidayEntity.getHolidayTitle();
                    MessageDataBean messageDataBean = new MessageDataBean(1l, ResponseCode.WARNING, msg);
                    messageDataBeanList.add(messageDataBean);
                }
                if (hkHolidayEntity.getHolidayTitle().equals(hkManageHolidayDatabean.getTitle()) && !hkHolidayEntity.getId().equals(hkManageHolidayDatabean.getId())) {
                    MessageDataBean messageDataBean = new MessageDataBean(1l, ResponseCode.FAILURE, hkManageHolidayDatabean.getTitle() + " already exists");
                    messageDataBeanList.add(messageDataBean);
                    priority = 2;
                }
            }
            if (priority == 1 || priority == 2) {
                return new ResponseEntity<PrimaryKey<Long>>(null, ResponseCode.WARNING, null, messageDataBeanList, false);
            } else {
                holidayTransformerBean.updateHoliday(hkManageHolidayDatabean);
                return new ResponseEntity<PrimaryKey<Long>>(null, ResponseCode.SUCCESS, "Holiday saved successfully", null, false);
            }

        } else {
            holidayTransformerBean.updateHoliday(hkManageHolidayDatabean);
            return new ResponseEntity<PrimaryKey<Long>>(null, ResponseCode.SUCCESS, "Holiday saved successfully", null, false);
        }

    }

    /**
     * This method create new holiday.
     *
     * @param hkManageHolidayDatabean
     * @return success or failure message.
     */
    @Override
    public ResponseEntity<PrimaryKey<Long>> create(@Valid @RequestBody HolidayDatabean hkManageHolidayDatabean) {
        if (hkManageHolidayDatabean.isForceEdit() != null && hkManageHolidayDatabean.isForceEdit()) {
            holidayTransformerBean.createHoliday(hkManageHolidayDatabean);
            return new ResponseEntity<PrimaryKey<Long>>(null, ResponseCode.SUCCESS, "Holidays list updated successfully", null, false);
        }
        List<HkHolidayEntity> hkHolidayEntitysList = holidayTransformerBean.isHolidayExists(hkManageHolidayDatabean);
        int priority = 0;
        String msg = "";
        List<MessageDataBean> messageDataBeanList = new ArrayList<>();
        if (!hkHolidayEntitysList.isEmpty()) {
            for (HkHolidayEntity hkHolidayEntity : hkHolidayEntitysList) {
                if ((hkManageHolidayDatabean.getEndDt().compareTo(hkHolidayEntity.getEndDt()) == 0
                        || hkManageHolidayDatabean.getStartDt().compareTo(hkHolidayEntity.getStartDt()) == 0
                        || (hkManageHolidayDatabean.getStartDt().compareTo(hkHolidayEntity.getEndDt()) < 0
                        && hkManageHolidayDatabean.getStartDt().compareTo(hkHolidayEntity.getStartDt()) > 0))
                        && !hkHolidayEntity.getId().equals(hkManageHolidayDatabean.getId())) {
                    priority = 1;
                    msg = hkManageHolidayDatabean.getTitle() + " overlapping with " + hkHolidayEntity.getHolidayTitle();
                    MessageDataBean messageDataBean = new MessageDataBean(1l, ResponseCode.WARNING, msg);
                    messageDataBeanList.add(messageDataBean);
                }
                if (hkHolidayEntity.getHolidayTitle().equals(hkManageHolidayDatabean.getTitle())) {
                    priority = 2;
                    MessageDataBean messageDataBean = new MessageDataBean(1l, ResponseCode.FAILURE, hkManageHolidayDatabean.getTitle() + " already exists");
                    messageDataBeanList.add(messageDataBean);

                }
            }
            if (priority == 1 || priority == 2) {
                return new ResponseEntity<PrimaryKey<Long>>(null, ResponseCode.SUCCESS, null, messageDataBeanList, false);
            } else {
                holidayTransformerBean.createHoliday(hkManageHolidayDatabean);
                return new ResponseEntity<PrimaryKey<Long>>(null, ResponseCode.SUCCESS, "Holidays list updated successfully", null, false);
            }
        } else {
            holidayTransformerBean.createHoliday(hkManageHolidayDatabean);
            return new ResponseEntity<PrimaryKey<Long>>(null, ResponseCode.SUCCESS, "Holidays list updated successfully", null, false);
        }

    }

    /**
     * This method is used to add holiday which is overlapping on same date of
     * existing holiday.
     *
     *
     * @return success or failure message.
     */
    @RequestMapping(value = "/forceHolidayAdd", method = RequestMethod.POST)
    public ResponseEntity<MessageDataBean> forceHolidayAdd() {
        ResponseEntity<MessageDataBean> responseEntity;
        if (holidayTransformerBean.forceHolidayAdd()) {
            responseEntity = new ResponseEntity(null, ResponseCode.SUCCESS, "Holidays list updated successfully", null, false);
        } else {
            responseEntity = new ResponseEntity(null, ResponseCode.FAILURE, "Holidays list can not be updated. Try again", null, false);
        }
        return responseEntity;
    }

    /**
     * This returns all the reason for holiday of previous year for particular
     * franchise.
     *
     * @return List of reason for holiday.
     */
    @RequestMapping(value = "/retrievePreviousYearDistinctHoliday", method = RequestMethod.POST)
    public ResponseEntity<List<String>> retrievePreviousYearDistinctHoliday() {
        List<String> previousYearHolidayList = null;
        previousYearHolidayList = holidayTransformerBean.retrievePreviousYearDistinctHoliday(null, loginDataBean.getCompanyId(), Boolean.FALSE);
        return new ResponseEntity<List<String>>(previousYearHolidayList, ResponseCode.SUCCESS, null, null, false);
    }

    @RequestMapping(value = "/searchHoliday", method = RequestMethod.GET, produces = {"application/json"})
    public List<HolidayDatabean> searchHoliday(@RequestParam("q") String query) throws IOException {
        return holidayTransformerBean.searchHoliday(query, loginDataBean.getCompanyId());//, Boolean.FALSE);
    }

    @Override
    public ResponseEntity<Map<String, Object>> retrievePrerequisite() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @RequestMapping(value = "/retrieveholidaydates", method = RequestMethod.POST, produces = {"application/json"})
    public ResponseEntity<List<Date>> retrieveHolidayDates() throws IOException {
        return new ResponseEntity<List<Date>>(holidayTransformerBean.retieveAllHolidayDates(loginDataBean.getCompanyId(), true), ResponseCode.SUCCESS, null, null, false);
    }
}
