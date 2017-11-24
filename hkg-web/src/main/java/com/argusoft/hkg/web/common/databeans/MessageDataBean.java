/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.common.databeans;

import com.argusoft.hkg.web.base.ResponseCode;
import java.util.Map;

/**
 *
 * @author Tejas
 */
public class MessageDataBean {

    private Long id;
    private int responseCode;
    private String message;
    private Map translateValueMap;

    public MessageDataBean(Long id, ResponseCode responseCode, String message, Map translateValueMap) {
        super();
        this.id = id;
        this.responseCode = responseCode.getValue();
        this.message = message;
        this.translateValueMap = translateValueMap;
    }

    public MessageDataBean(Long id, ResponseCode responseCode, String message) {
        super();
        this.id = id;
        this.responseCode = responseCode.getValue();
        this.message = message;
    }

    public void setResponseCode(ResponseCode responseCode) {
        this.responseCode = responseCode.getValue();
    }

    public Long getId() {
        return id;
    }

    public Map getTranslateValueMap() {
        return translateValueMap;
    }

    public void setTranslateValueMap(Map translateValueMap) {
        this.translateValueMap = translateValueMap;
    }

    public String getMessage() {
        return message;
    }

    public int getResponseCode() {
        return responseCode;
    }

}
