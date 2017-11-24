package com.argusoft.hkg.web.center.usermanagement.controllers;

import com.argusoft.hkg.web.center.sync.transformers.SyncCenterFranchiseTransformer;
import com.argusoft.hkg.web.center.usermanagement.databeans.CenterFranchiseDataBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author shruti
 */
@RestController
@RequestMapping("/centerfranchise")
public class CenterFranchiseController {

    @Autowired
    SyncCenterFranchiseTransformer centerFranchiseTransformer;

    @RequestMapping(value = "/retrieve", method = RequestMethod.GET)
    public CenterFranchiseDataBean retrieveFranchise() {
        return centerFranchiseTransformer.retrieveById(1l);
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    public void addFranchise(CenterFranchiseDataBean centerFranchiseDataBean) {
        centerFranchiseTransformer.addFranchise(centerFranchiseDataBean);
    }
}
