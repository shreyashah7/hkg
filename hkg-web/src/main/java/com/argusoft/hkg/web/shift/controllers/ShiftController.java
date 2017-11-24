/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.shift.controllers;

import com.argusoft.generic.database.exception.GenericDatabaseException;
import com.argusoft.hkg.web.base.PrimaryKey;
import com.argusoft.hkg.web.base.ResponseCode;
import com.argusoft.hkg.web.base.ResponseEntity;
import com.argusoft.hkg.web.base.controllers.BaseController;
import com.argusoft.hkg.web.eventmanagement.databeans.EventDataBean;
import com.argusoft.hkg.web.leavemanagement.databeans.HolidayDatabean;
import com.argusoft.hkg.web.master.controllers.MasterController;
import com.argusoft.hkg.web.shift.databeans.ShiftDataBean;
import com.argusoft.hkg.web.shift.databeans.TemporaryShiftDataBean;
import com.argusoft.hkg.web.shift.transformers.ShiftTransformerBean;
import com.argusoft.hkg.web.center.usermanagement.databeans.LoginDataBean;
import com.argusoft.hkg.web.util.TreeViewDataBean;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author sidhdharth
 */
@RestController
@RequestMapping("/shift")
public class ShiftController extends BaseController<ShiftDataBean, Long> {

    @Autowired
    ShiftTransformerBean shiftTransformerBean;

    @Autowired
    LoginDataBean loginDataBean;

    private static final Logger LOGGER = LoggerFactory.getLogger(MasterController.class);

    @Override
    public ResponseEntity<List<ShiftDataBean>> retrieveAll() {

        List<ShiftDataBean> allShifts = shiftTransformerBean.retrieveShift();
        return new ResponseEntity<>(allShifts, ResponseCode.SUCCESS, "", null);
    }

    @Override
    public ResponseEntity<ShiftDataBean> retrieveById(@RequestBody PrimaryKey<Long> primaryKey) {
        if (primaryKey.getPrimaryKey() != null) {
            return new ResponseEntity<ShiftDataBean>(shiftTransformerBean.retrieveCustomFieldData(primaryKey.getPrimaryKey()), ResponseCode.SUCCESS, "", null);
        }
        return null;
    }

    @Override
    public ResponseEntity<PrimaryKey<Long>> update(@Valid @RequestBody ShiftDataBean shiftDataBean) {
        boolean status = shiftDataBean.isStatus();
        Long companyId = loginDataBean.getCompanyId();
        String msg = shiftTransformerBean.updateShift(shiftDataBean, companyId);
        return new ResponseEntity<>(null, ResponseCode.SUCCESS, msg, null);
    }

    @Override
    public ResponseEntity<PrimaryKey<Long>> create(@Valid @RequestBody ShiftDataBean t) {
        Long shiftId = shiftTransformerBean.createShift(t);
        PrimaryKey<Long> pk = new PrimaryKey<>();
        pk.setPrimaryKey(shiftId);
        return new ResponseEntity<>(pk, ResponseCode.SUCCESS, "Shift created successfully", null);
    }

    @RequestMapping(value = "/createTempShift", method = RequestMethod.POST, produces = {"application/json"})
    public ResponseEntity<PrimaryKey<Long>> createTempShift(@Valid @RequestBody TemporaryShiftDataBean t) {
        Long shiftId = shiftTransformerBean.createTemporaryShift(t);

        PrimaryKey<Long> pk = new PrimaryKey<>();
        pk.setPrimaryKey(shiftId);
        if (shiftId != null) {
            return new ResponseEntity<>(pk, ResponseCode.SUCCESS, "Temporary shift created successfully", null);
        }
        return new ResponseEntity<>(pk, ResponseCode.FAILURE, "Temporary shift already created for this shift", null);
    }

    @RequestMapping(value = "/updateTempShift", method = RequestMethod.POST, produces = {"application/json"})
    public ResponseEntity<PrimaryKey<Long>> updateTempShift(@RequestBody TemporaryShiftDataBean t) {
        shiftTransformerBean.updateTemporaryShift(t);

        return new ResponseEntity<>(null, ResponseCode.SUCCESS, "Shift updated successfully", null);
    }

    @Override
    public ResponseEntity<ShiftDataBean> deleteById(@RequestBody PrimaryKey<Long> primaryKey) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @RequestMapping(value = "/retrieveDepartmentList", method = RequestMethod.GET, produces = {"application/json"})
    public TreeViewDataBean retrieveAllDepartmentsMutliSelectTree() {
        Long company = loginDataBean.getCompanyId();
        TreeViewDataBean treeViewDataBean = new TreeViewDataBean();
        treeViewDataBean.setText("ROOT");
        treeViewDataBean.setIsChecked(true);
        treeViewDataBean.setItems(shiftTransformerBean.retrieveDepartmentsInMultiSelectTreeView(company));
        return treeViewDataBean;
    }

    @RequestMapping(value = "/retrieveHolidayList", method = RequestMethod.GET, produces = {"application/json"})
    public ResponseEntity<List<HolidayDatabean>> retrieveHolidayList() {
        Long company = loginDataBean.getCompanyId();
        List<HolidayDatabean> retieveAllHoliday = shiftTransformerBean.retieveAllHoliday(company, true);
        return new ResponseEntity<List<HolidayDatabean>>(retieveAllHoliday, ResponseCode.SUCCESS, "", null);
    }

    @RequestMapping(value = "/retrieveevents", method = RequestMethod.GET, produces = {"application/json"})
    public ResponseEntity<List<EventDataBean>> retrieveAllEvents() {
        List<EventDataBean> eventDataBeans = shiftTransformerBean.retrieveUpcomingEvents();
        return new ResponseEntity<List<EventDataBean>>(eventDataBeans, ResponseCode.SUCCESS, "", null);
    }

    @RequestMapping(value = "/retrieveShiftsTree", method = RequestMethod.GET, produces = {"application/json"})
    public ResponseEntity<List<TreeViewDataBean>> retrieveAllShiftsTree() {
        List<TreeViewDataBean> retrieveShiftTree = shiftTransformerBean.retrieveShiftTree();
        return new ResponseEntity<>(retrieveShiftTree, ResponseCode.SUCCESS, "", null);
    }

    @RequestMapping(value = "/retrieveDefaultShift", method = RequestMethod.GET, produces = {"application/json"})
    public ResponseEntity<ShiftDataBean> retrieveDefaultShift() {
        Long companyId = loginDataBean.getCompanyId();
        ShiftDataBean retrieveDefaultShift = shiftTransformerBean.retrieveDefaultShift(companyId);
        return new ResponseEntity<ShiftDataBean>(retrieveDefaultShift, ResponseCode.SUCCESS, "", null);
    }

    @RequestMapping(value = "/searchShift", method = RequestMethod.GET, produces = {"application/json"})
    public List<ShiftDataBean> searchShifts(@RequestParam("q") String searchString) throws GenericDatabaseException {
        Long companyId = loginDataBean.getCompanyId();
        List<ShiftDataBean> shiftDataBeans = shiftTransformerBean.searchShift(companyId, searchString);
        return shiftDataBeans;
    }

    @Override
    public ResponseEntity<Map<String, Object>> retrievePrerequisite() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @RequestMapping(value = "/checkusers", method = RequestMethod.PUT, produces = {"application/json"})
    public ResponseEntity<Boolean> doesUserExistForShift(@RequestBody Long shiftId) {
        Long companyId = loginDataBean.getCompanyId();
        return new ResponseEntity<>(shiftTransformerBean.userExistForShift(shiftId, companyId), ResponseCode.SUCCESS, null, null, false);
    }
}
