/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.sync.center.model;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 *
 * @author shruti
 */
@Document(collection = "initialsetup")
public class InitialSetupInfo {

    @Id
    private Long id;
    private int status = -1;
    private Set<Integer> incompleteSetups = new HashSet<>();
    private boolean hasLocaleFilesTransfered = false;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Set<Integer> getIncompleteSetups() {
        return incompleteSetups;
    }

    public void setIncompleteSetups(Set<Integer> incompleteSetups) {
        this.incompleteSetups = incompleteSetups;
    }

    @Override
    public String toString() {
        return "InitialSetupInfo{" + "id=" + id + ", status=" + status + ", incompleteSetups=" + incompleteSetups + ", hasLocaleFilesTransfered=" + hasLocaleFilesTransfered + '}';
    }

    public boolean isHasLocaleFilesTransfered() {
        return hasLocaleFilesTransfered;
    }

    public void setHasLocaleFilesTransfered(boolean hasLocaleFilesTransfered) {
        this.hasLocaleFilesTransfered = hasLocaleFilesTransfered;
    }

}
