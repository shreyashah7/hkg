/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.center.stock.controllers;

import com.argusoft.hkg.web.base.ResponseCode;
import com.argusoft.hkg.web.base.ResponseEntity;
import com.argusoft.hkg.web.center.stock.databeans.IssueReceiveDataBean;
import com.argusoft.hkg.web.center.stock.transformers.IssueReceiveTransformer;
import com.argusoft.hkg.web.center.usermanagement.databeans.CenterDepartmentDataBean;
import com.argusoft.hkg.web.util.SelectItem;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author siddharth
 */
@RestController
@RequestMapping("/issue")
public class IssueReceiveController {

    @Autowired
    IssueReceiveTransformer issueReceiveTransformer;

    // Created by raj
    @RequestMapping(value = "/retrievemodifiers", method = RequestMethod.POST, produces = {"application/json"})
    public Map<String, List<String>> retrieveModifiers() {
        return issueReceiveTransformer.retrieveModifiers();
    }

    @RequestMapping(value = "/retrievestockbyid", method = RequestMethod.POST, produces = {"application/json"})
    public Map<String, Map<String, String>> retrieveStockById(@RequestBody Map<String, String> stockId) {
        return issueReceiveTransformer.retrieveStockById(stockId);
    }

    @RequestMapping(value = "/saveall", method = RequestMethod.POST, produces = {"application/json"})
    @ResponseStatus(value = HttpStatus.OK)
    public void saveAll(@RequestBody List<IssueReceiveDataBean> stocks) {
        issueReceiveTransformer.requestIssueReceive(stocks);
    }

    @RequestMapping(value = "/retrievebyslip", method = RequestMethod.POST, produces = {"application/json"})
    public List<IssueReceiveDataBean> retrieveStockBySlip(@RequestBody IssueReceiveDataBean stock) {
        return issueReceiveTransformer.retrieveStockBySlip(stock);
    }

    @RequestMapping(value = "/collect", method = RequestMethod.POST, produces = {"application/json"})
    @ResponseStatus(value = HttpStatus.OK)
    public void collect(@RequestBody List<IssueReceiveDataBean> stock) {
        issueReceiveTransformer.collect(stock);
    }

    @RequestMapping(value = "/issue", method = RequestMethod.POST, produces = {"application/json"})
    public ResponseEntity<String> issue(@RequestBody List<IssueReceiveDataBean> stock) {
        String slipNo = issueReceiveTransformer.issue(stock);
        if (slipNo == null) {
            return new ResponseEntity<>(null, ResponseCode.ERROR, "Error in issue", null, null, true);
        } else {
            return new ResponseEntity<>(null, ResponseCode.SUCCESS, "Issued successfully Slip Number: " + slipNo, null, null, true);
        }
    }

    @RequestMapping(value = "/receive", method = RequestMethod.POST, produces = {"application/json"})
    @ResponseStatus(value = HttpStatus.OK)
    public void receive(@RequestBody List<IssueReceiveDataBean> stock) {
        issueReceiveTransformer.receive(stock);
    }

    @RequestMapping(value = "/issueInward", method = RequestMethod.POST, produces = {"application/json"})
    public ResponseEntity<String> issueInward(@RequestBody List<IssueReceiveDataBean> stock) {
        Integer slipNo = issueReceiveTransformer.issueInward(stock);
        if (slipNo == null) {
            return new ResponseEntity<>(null, ResponseCode.ERROR, "Error in issue", null, null, true);
        } else {
            return new ResponseEntity<>(null, ResponseCode.SUCCESS, "Issued successfully Slip Number: " + slipNo, null, null, true);
        }
    }

    @RequestMapping(value = "/receiveinward", method = RequestMethod.POST, produces = {"application/json"})
    @ResponseStatus(value = HttpStatus.OK)
    public void receiveInward(@RequestBody List<IssueReceiveDataBean> stock) {
        issueReceiveTransformer.receiveInward(stock);
    }

    @RequestMapping(value = "/retrieveusersbydepartment", method = RequestMethod.POST)
    public List<CenterDepartmentDataBean> retrieveUsersDepartment(@RequestBody IssueReceiveDataBean stock) {
        return issueReceiveTransformer.retrieveDepartmentListInTreeViewSimple(stock.getDestDeptId());
    }

    @RequestMapping(value = "/reject", method = RequestMethod.POST, produces = {"application/json"})
    @ResponseStatus(value = HttpStatus.OK)
    public void reject(@RequestBody List<IssueReceiveDataBean> stock) {
        issueReceiveTransformer.reject(stock);
    }

    @RequestMapping(value = "/retrieveAssociatedDepartments", method = RequestMethod.POST, consumes = {"application/json"})
    public List<SelectItem> retrieveAssociatedDepartments(@RequestBody(required = false) Long depId) {
        List<SelectItem> deptList = issueReceiveTransformer.retrieveAssociatedDepts(depId);
        return deptList;
    }

    @RequestMapping(value = "/returnStock", method = RequestMethod.POST, produces = {"application/json"})
    @ResponseStatus(value = HttpStatus.OK)
    public void returnStock(@RequestBody List<IssueReceiveDataBean> stock) {
        issueReceiveTransformer.returnStock(stock);
    }

    @RequestMapping(value = "/retrievependingissued", method = RequestMethod.GET)
    public List<IssueReceiveDataBean> retrievePendingIssued(@RequestParam("requestType") String requestType) {
        List<IssueReceiveDataBean> result = issueReceiveTransformer.retrievePendingIssued(requestType);
        return result;
    }

    @RequestMapping(value = "/retrieveDesignationForDept", method = RequestMethod.POST)
    public List<SelectItem> retrieveDesignationForDept(@RequestBody Long depId) {
        List<SelectItem> desgList = issueReceiveTransformer.retrieveDesignationByDepartment(depId);
        return desgList;
    }
}
