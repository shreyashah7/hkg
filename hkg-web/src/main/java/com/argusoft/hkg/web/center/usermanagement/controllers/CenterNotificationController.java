/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.center.usermanagement.controllers;

import com.argusoft.hkg.web.center.transformers.CenterNotificationDocumentTransformer;
import com.argusoft.hkg.web.center.usermanagement.databeans.LoginDataBean;
import com.argusoft.hkg.web.usermanagement.databeans.NotificationRecipientDataBean;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author akta
 */
@RestController
@RequestMapping("/notification")
public class CenterNotificationController {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    @Autowired
    CenterNotificationDocumentTransformer centerNotificationDocumentTransformer;

    @Autowired
    LoginDataBean centerLoginDataBean;

    @RequestMapping(value = "/retrievecount", method = RequestMethod.GET)
    public Map<String, Integer> retrieveNotificationCount() {
        return centerNotificationDocumentTransformer.retrieveNotificationCount(centerLoginDataBean.getId(), centerLoginDataBean.getCompanyId());

    }

    @RequestMapping(value = "/retrievenotificationpopup", method = RequestMethod.GET, produces = {"application/json"})
    public List<NotificationRecipientDataBean> retrieveNotificationPopUp() {
        LOGGER.debug("centerLoginDataBean.getId()=" + centerLoginDataBean.getId() + ", centerLoginDataBean.getCompanyId()=" + centerLoginDataBean.getCompanyId());
        return centerNotificationDocumentTransformer.retrieveNotificationsPopUp(centerLoginDataBean.getId(), centerLoginDataBean.getCompanyId());

    }

    @RequestMapping(value = "/retrievenotification", method = RequestMethod.GET, produces = {"application/json"})
    public List<NotificationRecipientDataBean> retrieveNotification() {
        return centerNotificationDocumentTransformer.retrieveNotifications(centerLoginDataBean.getId(), null, null, centerLoginDataBean.getCompanyId());
    }

    @RequestMapping(value = "/remove", method = RequestMethod.POST, produces = {"application/json"})
    @ResponseStatus(value = HttpStatus.OK)
    public void delete(@RequestBody String notificationId) {
        centerNotificationDocumentTransformer.removeNotifications(notificationId);
    }
}
