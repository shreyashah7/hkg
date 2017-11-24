/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.util;

/**
 *
 * @author harshit
 */
public class HkSelect2DataBean {

    private Object id;
    private String text;

    public HkSelect2DataBean() {
    }

    public HkSelect2DataBean(Object id, String text) {
        this.id = id;
        this.text = text;
    }

    public Object getId() {
        return id;
    }

    public void setId(Object id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "HkSelect2DataBean{" + "id=" + id + ", text=" + text + '}';
    }

}
