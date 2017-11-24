/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.center.usermanagement.controllers;

import com.argusoft.hkg.web.base.ResponseCode;
import com.argusoft.hkg.web.base.ResponseEntity;
import com.argusoft.hkg.web.center.transformers.CenterFranchiseConfigrationTransformerBean;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author akta
 */
@RestController
@RequestMapping("/franchiseconfig")
public class CenterFranchiseConfigurationController {

    @Autowired
    CenterFranchiseConfigrationTransformerBean centerFranchiseConfigrationTransformerBean;

    @RequestMapping(value = "/retrieveallconfig", method = RequestMethod.GET)
    public ResponseEntity<Map<String, String>> retrieveAllConfigurations() {
        return new ResponseEntity<>(centerFranchiseConfigrationTransformerBean.retrieveAllConfiguration(), ResponseCode.SUCCESS, null, null);
    }
}
