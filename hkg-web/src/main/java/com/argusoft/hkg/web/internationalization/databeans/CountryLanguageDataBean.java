/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.internationalization.databeans;

/**
 *
 * @author akta
 */
public class CountryLanguageDataBean {

    private String countryCode;
    private String languageCode;
    private Boolean isLeftToRight;
    private String encoding;

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public Boolean isIsLeftToRight() {
        return isLeftToRight;
    }

    public void setIsLeftToRight(Boolean isLeftToRight) {
        this.isLeftToRight = isLeftToRight;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }
}
