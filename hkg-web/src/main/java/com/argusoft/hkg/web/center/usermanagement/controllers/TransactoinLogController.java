/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.center.usermanagement.controllers;

import com.argusoft.hkg.web.center.transformers.TransactionLogTransformer;
import com.argusoft.hkg.web.center.usermanagement.databeans.TransactionLogDatabean;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author shruti
 */
@RestController
@RequestMapping("/transactionlog")
public class TransactoinLogController {

    @Autowired
    private TransactionLogTransformer transactionLogTransformer;

    @RequestMapping(value = "/retrieve", method = GET)
    public List<TransactionLogDatabean> getTransactionLogDatabeans() {
        return transactionLogTransformer.getAllTransactionLog();
    }
    @RequestMapping(value = "/resend", method = POST)
    @ResponseStatus(value = HttpStatus.OK)
    public void resend(@RequestBody List<TransactionLogDatabean> logDatabeans) {
        transactionLogTransformer.resend(logDatabeans);
    }

    @RequestMapping(value = "/retrieve", method = POST)
    public List<TransactionLogDatabean> getTransactionLogDatabeansByCriteria(@RequestBody Map<String, String> map) {
        Date fromDate = null;
        Date toDate = null;
        if (map.get("fromDate") != null) {
            fromDate = new Date(Long.parseLong(map.get("fromDate")));
        }
        if (map.get("toDate") != null) {
            toDate = new Date(Long.parseLong(map.get("toDate")));
        }
//        System.out.println("map.get(\"franchise\")  " + map.get("franchise"));
        return transactionLogTransformer.getTransactionLogByCriteria(map.get("status"), map.get("franchise"), fromDate, toDate);
    }
  
    @RequestMapping(value = "/retrieve/prerequisite", method = GET)
    public Map<String, Object> retrieveAllStatus() {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("status", transactionLogTransformer.getstatusList());
        List<String> franchiseList = transactionLogTransformer.getFranchiseList();
        hashMap.put("franchise", franchiseList);
        hashMap.put("currentFranchise", (franchiseList != null ? franchiseList.get(0) : null));
        return hashMap;
    }
}
