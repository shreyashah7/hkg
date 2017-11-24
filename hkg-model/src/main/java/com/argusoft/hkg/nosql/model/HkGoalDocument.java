package com.argusoft.hkg.nosql.model;

import java.math.BigInteger;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 *
 * @author rajkumar
 */
@Document(collection = "goal")
public class HkGoalDocument extends GenericDocument {

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
        return "HkUserDocument{" + "id=" + id + '}';
    }

}
