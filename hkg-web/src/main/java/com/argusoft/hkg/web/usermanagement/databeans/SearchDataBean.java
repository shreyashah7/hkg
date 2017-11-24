/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.argusoft.hkg.web.usermanagement.databeans;

/**
 *
 * @author sidhdharth
 */
public class SearchDataBean {
    private Object id;
    private String displayResult;

    public Object getId() {
        return id;
    }

    public void setId(Object id) {
        this.id = id;
    }

    public String getDisplayResult() {
        return displayResult;
    }

    public void setDisplayResult(String displayResult) {
        this.displayResult = displayResult;
    }

    @Override
    public String toString() {
        return "SearchDataBean{" + "id=" + id + ", displayResult=" + displayResult + '}';
    }
    
    
}
