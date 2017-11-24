/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.argusoft.hkg.web.center.leavemanagement.controllers;

import com.argusoft.hkg.web.base.ResponseCode;
import com.argusoft.hkg.web.base.ResponseEntity;
import com.argusoft.hkg.web.center.leavemanagement.transformers.CenterHolidayTransformer;
import com.argusoft.hkg.web.center.usermanagement.databeans.LoginDataBean;
import com.argusoft.hkg.web.leavemanagement.transformers.HolidayTransformerBean;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author gautam
 */
@RestController
@RequestMapping("/holiday")
public class CenterHolidayController {
    
    @Autowired
    LoginDataBean loginDataBean;
    @Autowired
    CenterHolidayTransformer holidayTransformerBean;
    
    @RequestMapping(value = "/retrieveholidaydates", method = RequestMethod.POST, produces = {"application/json"})
    public ResponseEntity<List<Date>> retrieveHolidayDates() throws IOException {
        return new ResponseEntity<List<Date>>(holidayTransformerBean.retieveAllHolidayDates(loginDataBean.getCompanyId(), true), ResponseCode.SUCCESS, null, null, false);
    }
    
}
