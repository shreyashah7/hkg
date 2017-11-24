/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.usermanagement.databeans;

import java.util.Map;

/**
 *
 * @author mansi
 */
public class EmpEducationDetalDataBean {

    private Long id;
    private Long degree;
    private Long passingYear;
    private String empPercentage;
    private Long university;
    private Long medium;
    private Map<String, Object> educationCustom;// for custom field data
    private Map<String, String> educationDbType; //for get filed wise dbtype

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDegree() {
        return degree;
    }

    public void setDegree(Long degree) {
        this.degree = degree;
    }

    public Long getPassingYear() {
        return passingYear;
    }

    public void setPassingYear(Long passingYear) {
        this.passingYear = passingYear;
    }

    public String getEmpPercentage() {
        return empPercentage;
    }

    public void setEmpPercentage(String empPercentage) {
        this.empPercentage = empPercentage;
    }

    public Long getUniversity() {
        return university;
    }

    public void setUniversity(Long university) {
        this.university = university;
    }

    public Long getMedium() {
        return medium;
    }

    public void setMedium(Long medium) {
        this.medium = medium;
    }

    public Map<String, Object> getEducationCustom() {
        return educationCustom;
    }

    public void setEducationCustom(Map<String, Object> educationCustom) {
        this.educationCustom = educationCustom;
    }

    public Map<String, String> getEducationDbType() {
        return educationDbType;
    }

    public void setEducationDbType(Map<String, String> educationDbType) {
        this.educationDbType = educationDbType;
    }

}
