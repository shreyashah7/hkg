/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.rule;

import java.util.Map;

/**
 *
 * @author shruti
 */
public class DynamicClass {

    String name;
    Map<String, Object> map;

    public DynamicClass(String name, Map<String, Object> map) {
        this.name = name;
        this.map = map;
    }

    public DynamicClass() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, Object> getMap() {
        return map;
    }

    public void setMap(Map<String, Object> map) {
        this.map = map;
    }

    @Override
    public String toString() {
        return "DynamicClass{" + "name=" + name + ", map=" + map + '}';
    }

}
