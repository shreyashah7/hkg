/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.nosql.model;

import java.util.Date;
import javax.persistence.Id;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 *
 * @author rajkumar
 */
@Document(collection = "slipno_generator")
public class HkSlipNoGeneratorDocument {

    @Id
    private ObjectId id;
    private Long department;
    private Integer slipNo;
    private Date slipDate;

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public Long getDepartment() {
        return department;
    }

    public void setDepartment(Long department) {
        this.department = department;
    }

    public Integer getSlipNo() {
        return slipNo;
    }

    public void setSlipNo(Integer slipNo) {
        this.slipNo = slipNo;
    }

    public Date getSlipDate() {
        return slipDate;
    }

    public void setSlipDate(Date slipDate) {
        this.slipDate = slipDate;
    }

    @Override
    public String toString() {
        return "HkSlipNoGeneratorDocument{" + "id=" + id + ", department=" + department + ", slipNo=" + slipNo + ", slipDate=" + slipDate + '}';
    }

}
