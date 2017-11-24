package com.argusoft.hkg.nosql.model;

import javax.persistence.Id;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 *
 * @author Mayank
 */
@Document(collection = "subformvalue")
public class HkSubFormValueDocument extends GenericDocument implements Cloneable {

    @Id
    private ObjectId id;
    private ObjectId sourceId;    

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public ObjectId getSourceId() {
        return sourceId;
    }

    public void setSourceId(ObjectId sourceId) {
        this.sourceId = sourceId;
    }
        
    @Override
    public HkSubFormValueDocument clone() throws CloneNotSupportedException {
        return (HkSubFormValueDocument) super.clone();
    }

}
