/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.reportbuilder.database.bean;

import java.util.List;
import java.util.Map;

/**
 *
 * @author vipul
 */
public class MongoQueryDatabean {

    private String collectionName;
    private List<MongoFieldDatabean> mongoFieldDatabean;
    Map<String, String> criteria;

    public String getCollectionName() {
        return collectionName;
    }

    public void setCollectionName(String collectionName) {
        this.collectionName = collectionName;
    }

    public List<MongoFieldDatabean> getMongoFieldDatabean() {
        return mongoFieldDatabean;
    }

    public void setMongoFieldDatabean(List<MongoFieldDatabean> mongoFieldDatabean) {
        this.mongoFieldDatabean = mongoFieldDatabean;
    }

    public Map<String, String> getCriteria() {
        return criteria;
    }

    public void setCriteria(Map<String, String> criteria) {
        this.criteria = criteria;
    }

}
