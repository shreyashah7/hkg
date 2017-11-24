/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.center.usermanagement.controllers;

import com.argusoft.hkg.web.base.ResponseCode;
import com.argusoft.hkg.web.base.ResponseEntity;
import com.argusoft.hkg.web.center.transformers.CenterMessageDocumentTransformer;
import com.argusoft.hkg.web.center.usermanagement.databeans.LoginDataBean;
import com.argusoft.hkg.web.usermanagement.databeans.MessagingDataBean;
import java.util.HashMap;
import java.util.List;
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
@RequestMapping("/messaging")
public class CenterMessagingController {

    @Autowired
    CenterMessageDocumentTransformer centerMessageDocumentTransformer;

    @Autowired
    LoginDataBean loginDataBean;

    @RequestMapping(value = "/retrieve/alerts", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> retrieveAlerts() {
        Long userId = loginDataBean.getId();
        List<MessagingDataBean> retrieveAllMessagesByUser = centerMessageDocumentTransformer.retrieveAllMessagesByUser(userId, Boolean.FALSE, true);
        Map<String, Object> map = new HashMap<>();
        map.put("priority", retrieveAllMessagesByUser);
        map.put("count", centerMessageDocumentTransformer.retrieveunattendedMessageCount(userId, Boolean.FALSE, false));
        return new ResponseEntity<>(map, ResponseCode.SUCCESS, "", null);
    }

    @RequestMapping(value = "/retrieve/unreadmessages", method = RequestMethod.POST)
    public ResponseEntity<List<MessagingDataBean>> retrieveMessages() {
        Long userId = loginDataBean.getId();
        List<MessagingDataBean> retrieveAllMessagesByUser = centerMessageDocumentTransformer.retrieveAllMessagesByUser(userId, Boolean.FALSE, false);
        return new ResponseEntity<>(retrieveAllMessagesByUser, ResponseCode.SUCCESS, "", null);
    }
}
