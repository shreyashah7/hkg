package com.argusoft.hkg.web.usermanagement.databeans;

/**
 *
 * @author satyajit
 */
public class SystemConfigurationDatabean {

    private String systemKey;
    private String keyValue;

    public String getSystemKey() {
        return systemKey;
    }

    public void setSystemKey(String systemKey) {
        this.systemKey = systemKey;
    }

    public String getKeyValue() {
        return keyValue;
    }

    public void setKeyValue(String keyValue) {
        this.keyValue = keyValue;
    }

    public Boolean isIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
    private Boolean isActive;
}
