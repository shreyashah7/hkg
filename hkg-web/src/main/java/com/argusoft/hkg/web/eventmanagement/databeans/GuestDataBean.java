/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.eventmanagement.databeans;

/**
 *
 * @author dax
 */
public class GuestDataBean {

    private String name;
    private String relation;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    @Override
    public String toString() {
        return "GuestDataBean{" + "name=" + name + ", relation=" + relation + '}';
    }

}
