/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.rule;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author shruti
 */
@XmlRootElement(name = "rule-execution-set")
public class RuleSet {

    private String name;
    private String description;
    private String rule;

    public RuleSet(String name, String description, String rule) {
        this.name = name;
        this.description = description;
        this.rule = rule;
    }

    public RuleSet() {
    }

    public String getName() {
        return name;
    }

    @XmlElement
    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    @XmlElement
    public void setDescription(String description) {
        this.description = description;
    }

    public String getRule() {
        return rule;
    }

    @XmlElement(name = "code")
    public void setRule(String rule) {
        this.rule = rule;
    }

    @Override
    public String toString() {
        return "RuleSet{" + "name=" + name + ", description=" + description + ", rule=" + rule + '}';
    }

}
