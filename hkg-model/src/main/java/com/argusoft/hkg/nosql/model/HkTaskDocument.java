/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.argusoft.hkg.nosql.model;

import java.math.BigInteger;
import javax.persistence.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 *
 * @author vipul
 */
@Document(collection = "task")
public class HkTaskDocument extends GenericDocument{
    
    @Id
    private BigInteger id;

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "HkTaskDocument{" + "id=" + id + '}';
    }
}
