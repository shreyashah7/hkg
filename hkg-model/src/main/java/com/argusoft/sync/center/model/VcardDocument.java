/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.sync.center.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 *
 * @author shruti
 */
@Document(collection = "vcard")
public class VcardDocument {

    @Id
    private String _id;
    private String jid;
    private Map<String, String> fields;
    private Date lastModifiedOn;
    private boolean isRosterEntryCreated;

    public VcardDocument() {
        fields = new HashMap<>();
    }

    public String getId() {
        return _id;
    }

    public void setId(String _id) {
        this._id = _id;
    }

    public String getJid() {
        return jid;
    }

    public void setJid(String jid) {
        this.jid = jid;
    }

    public Map<String, String> getFields() {
        return fields;
    }

    public void setFields(Map<String, String> fields) {
        this.fields = fields;
    }

    /**
     * @return the lastModifiedOn
     */
    public Date getLastModifiedOn() {
        return lastModifiedOn;
    }

    /**
     * @param lastModifiedOn the lastModifiedOn to set
     */
    public void setLastModifiedOn(Date lastModifiedOn) {
        this.lastModifiedOn = lastModifiedOn;
    }

    /**
     * @return the isRosterEntryCreated
     */
    public boolean isIsRosterEntryCreated() {
        return isRosterEntryCreated;
    }

    /**
     * @param isRosterEntryCreated the isRosterEntryCreated to set
     */
    public void setIsRosterEntryCreated(boolean isRosterEntryCreated) {
        this.isRosterEntryCreated = isRosterEntryCreated;
    }

}
