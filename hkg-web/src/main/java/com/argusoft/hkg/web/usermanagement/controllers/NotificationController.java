/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.usermanagement.controllers;

import com.argusoft.hkg.web.base.PrimaryKey;
import com.argusoft.hkg.web.base.ResponseEntity;
import com.argusoft.hkg.web.base.controllers.BaseController;
import com.argusoft.hkg.web.center.usermanagement.databeans.LoginDataBean;
import com.argusoft.hkg.web.usermanagement.databeans.NotificationConfigurationDataBean;
import com.argusoft.hkg.web.usermanagement.databeans.NotificationDataBean;
import com.argusoft.hkg.web.usermanagement.databeans.NotificationRecipientDataBean;
import com.argusoft.hkg.web.usermanagement.transformers.NotificationTransformerBean;
import java.util.ArrayList;
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
 * @author jyoti
 */
@RestController
@RequestMapping("/notification")
public class NotificationController extends BaseController<NotificationDataBean, Object> {

    @Autowired
    private LoginDataBean loginDataBean;

    @Autowired
    private NotificationTransformerBean notificationTransformerBean;

    @RequestMapping(value = "/retrievecount", method = RequestMethod.GET)
    public Map<String, Integer> retrieveNotificationCount() {
        return notificationTransformerBean.retrieveNotificationCount(loginDataBean.getId(), loginDataBean.getCompanyId());

    }

    @RequestMapping(value = "/retrievenotification", method = RequestMethod.GET, produces = {"application/json"})
    public List<NotificationRecipientDataBean> retrieveNotification() {
        return notificationTransformerBean.retrieveNotifications(loginDataBean.getId(), null, null, loginDataBean.getCompanyId());
    }

    @RequestMapping(value = "/retrievenotificationpopup", method = RequestMethod.GET, produces = {"application/json"})
    public List<NotificationRecipientDataBean> retrieveNotificationPopUp() {
        return notificationTransformerBean.retrieveNotificationsPopUp(loginDataBean.getId(), loginDataBean.getCompanyId());

    }

    @RequestMapping(value = "/remove", method = RequestMethod.POST, produces = {"application/json"})
    @ResponseStatus(value = HttpStatus.OK)
    public void delete(@RequestBody String notificationId) {
        notificationTransformerBean.removeNotifications(notificationId);
    }

    @Override
    public ResponseEntity<List<NotificationDataBean>> retrieveAll() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ResponseEntity<NotificationDataBean> retrieveById(PrimaryKey<Object> primaryKey) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ResponseEntity<PrimaryKey<Object>> update(NotificationDataBean t) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ResponseEntity<PrimaryKey<Object>> create(NotificationDataBean t) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ResponseEntity<NotificationDataBean> deleteById(PrimaryKey<Object> primaryKey) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ResponseEntity<Map<String, Object>> retrievePrerequisite() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @RequestMapping(value = "/createNotificationConfiguration", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    public void createNotificationConfiguration(@RequestBody NotificationConfigurationDataBean notificationDataBean) {
        notificationTransformerBean.createNotificationConfiguration(notificationDataBean);
    }

    @RequestMapping(value = "/retrieveAllNotificationConfigurations", method = RequestMethod.POST)
    public List<NotificationConfigurationDataBean> retrieveallNotificationConfigurations() {
        List<NotificationConfigurationDataBean> result = notificationTransformerBean.retrieveallNotificationConfigurations();
        return result;
    }

    @RequestMapping(value = "/updateNotificationConfiguration", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    public void updateNotificationConfiguration(@RequestBody NotificationConfigurationDataBean notificationDataBean) {
        notificationTransformerBean.updateNotificationConfiguration(notificationDataBean);
    }

    @RequestMapping(value = "/searchnotificationconfigurations", method = RequestMethod.GET, produces = {"application/json"})
    public List<NotificationConfigurationDataBean> searchNotification(@RequestParam("q") String query) {
        List<NotificationConfigurationDataBean> notifications = new ArrayList<>();
        notifications = notificationTransformerBean.searchNotificationConfiguration(query);
        return notifications;
    }

    @RequestMapping(value = "/retrieveNotificationConfigurationById")
    public NotificationConfigurationDataBean retrieveNotificationConfigurationById(@RequestBody Long id) {
        return notificationTransformerBean.retrieveNotificationConfigurationById(id);
    }
}
