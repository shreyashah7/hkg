/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.usermanagement.controllers;

import com.argusoft.generic.database.exception.GenericDatabaseException;
import com.argusoft.hkg.common.constantutil.HkSystemConstantUtil;
import com.argusoft.hkg.web.base.PrimaryKey;
import com.argusoft.hkg.web.base.ResponseCode;
import com.argusoft.hkg.web.base.ResponseEntity;
import com.argusoft.hkg.web.base.controllers.BaseController;
import com.argusoft.hkg.web.shift.databeans.ShiftDataBean;
import com.argusoft.hkg.web.usermanagement.databeans.EmployeeDataBean;
import com.argusoft.hkg.web.usermanagement.transformers.EmployeeTransformerBean;
import com.argusoft.hkg.web.usermanagement.transformers.TransferEmployeeTransformer;
import com.argusoft.hkg.web.util.SelectItem;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author shreya
 */
@RestController
@RequestMapping("/transferemployee")
public class TransferEmployeeController extends BaseController<EmployeeDataBean, Long> {

    @Autowired
    TransferEmployeeTransformer transferEmployeeTransformer;
    @Autowired
    EmployeeTransformerBean employeeTransformerBean;

    @Override
    public ResponseEntity<List<EmployeeDataBean>> retrieveAll() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ResponseEntity<EmployeeDataBean> retrieveById(PrimaryKey<Long> primaryKey) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ResponseEntity<PrimaryKey<Long>> update(EmployeeDataBean t) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ResponseEntity<PrimaryKey<Long>> create(EmployeeDataBean t) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ResponseEntity<EmployeeDataBean> deleteById(PrimaryKey<Long> primaryKey) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ResponseEntity<Map<String, Object>> retrievePrerequisite() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @RequestMapping(value = "/retrieveshiftwithdeptname", method = RequestMethod.GET, produces = {"application/json"})
    public ResponseEntity<Map<String, List<SelectItem>>> retrieveShiftsWithDepartmentName() throws GenericDatabaseException {
        Map<String, List<SelectItem>> deptShiftMap = transferEmployeeTransformer.retrieveShiftsWithDepartmentName();
        if (!CollectionUtils.isEmpty(deptShiftMap)) {
            return new ResponseEntity<>(deptShiftMap, ResponseCode.SUCCESS, null, null, false);
        } else {
            return new ResponseEntity<>(null, ResponseCode.FAILURE, null, null, false);
        }
    }

    @RequestMapping(value = "/retrieveemployeestatus", method = RequestMethod.GET, produces = {"application/json"})
    public ResponseEntity< List<SelectItem>> retrieveEmployeeStatus() throws GenericDatabaseException {
        List<SelectItem> fillStatusList = employeeTransformerBean.fillEmployeeStatusList();
        if (!CollectionUtils.isEmpty(fillStatusList)) {
            return new ResponseEntity<>(fillStatusList, ResponseCode.SUCCESS, null, null, false);
        } else {
            return new ResponseEntity<>(null, ResponseCode.FAILURE, null, null, false);
        }
    }

    @RequestMapping(value = "/retrieveeployeesbyshiftbydept", method = RequestMethod.POST)
    public ResponseEntity<List<EmployeeDataBean>> retrieveEmployeesByShiftByDept(@RequestBody Map<String, Object> map) throws GenericDatabaseException {
        //System.out.println("indie method");
        List<Long> shiftIds = new ArrayList<>();
        List<Long> deptIds = new ArrayList<>();
        if (!CollectionUtils.isEmpty(map)) {
            //System.out.println("inside not null map");
            if (map.get("shiftIds") != null && map.get("deptIds") != null) {
                String shiftTemp = map.get("shiftIds").toString();
                String deptTemp = map.get("deptIds").toString();
                if (!StringUtils.isEmpty(shiftTemp) && !StringUtils.isEmpty(deptTemp)) {
                    String[] shiftSplit = shiftTemp.split(",");
                    if (shiftSplit != null) {
                        for (String shiftSplit1 : shiftSplit) {
                            shiftIds.add(Long.parseLong(shiftSplit1));
                        }
                    }
                    String[] deptSplit = deptTemp.split(",");
                    if (deptSplit != null) {
                        for (String deptSplit1 : deptSplit) {
                            deptIds.add(Long.parseLong(deptSplit1));
                        }
                    }
                    //System.out.println("shiftIds :" + shiftIds);
                    //System.out.println("deptIds : " + deptIds);
                    List<EmployeeDataBean> employeeDataBeans = transferEmployeeTransformer.retrieveEmployeesByShiftByDept(shiftIds, deptIds);
                    if (!CollectionUtils.isEmpty(employeeDataBeans)) {
                        return new ResponseEntity<>(employeeDataBeans, ResponseCode.SUCCESS, null, null, false);
                    } else {
                        return new ResponseEntity<>(null, ResponseCode.SUCCESS, null, null, false);
                    }
                }

            }
        }
        return null;
    }

    @RequestMapping(value = "/transferemployee", method = RequestMethod.POST)
    public ResponseEntity<PrimaryKey<Long>> transferEmployeeByCriteria(@RequestBody List<EmployeeDataBean> employeeDataBeans) throws GenericDatabaseException {
        String response = transferEmployeeTransformer.transferEmployeeByCriteria(employeeDataBeans);
        if (response.equalsIgnoreCase(HkSystemConstantUtil.SUCCESS)) {
            if(!CollectionUtils.isEmpty(employeeDataBeans) && employeeDataBeans.size() == 1){
                return new ResponseEntity<>(null, ResponseCode.SUCCESS, "Employee transfered successfully", null);
            }else{
                return new ResponseEntity<>(null, ResponseCode.SUCCESS, "Employees transfered successfully", null);
            }
        } else {
            return new ResponseEntity<>(null, ResponseCode.FAILURE, "Employees can not be transferred", null);
        }
    }

}
