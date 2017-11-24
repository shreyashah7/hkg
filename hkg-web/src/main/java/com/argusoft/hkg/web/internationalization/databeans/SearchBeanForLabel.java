/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.internationalization.databeans;

/**
 *
 * @author satyam
 */
public class SearchBeanForLabel {

    private String entity;

    /**
     * Get the value of entity
     *
     * @return the value of entity
     */
    public String getEntity() {
        return entity;
    }

    /**
     * Set the value of entity
     *
     * @param entity new value of entity
     */
    public void setEntity(String entity) {
        this.entity = entity;
    }

    private String searchText;

    /**
     * Get the value of searchText
     *
     * @return the value of searchText
     */
    public String getSearchText() {
        return searchText;
    }

    /**
     * Set the value of searchText
     *
     * @param searchText new value of searchText
     */
    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    private String type;

    /**
     * Get the value of type
     *
     * @return the value of type
     */
    public String getType() {
        return type;
    }

    /**
     * Set the value of type
     *
     * @param type new value of type
     */
    public void setType(String type) {
        this.type = type;
    }

    private LanguageDataBean languageDataBean;

    /**
     * Get the value of languageDataBean
     *
     * @return the value of languageDataBean
     */
    public LanguageDataBean getLanguageDataBean() {
        return languageDataBean;
    }

    /**
     * Set the value of languageDataBean
     *
     * @param languageDataBean new value of languageDataBean
     */
    public void setLanguageDataBean(LanguageDataBean languageDataBean) {
        this.languageDataBean = languageDataBean;
    }

    @Override
    public String toString() {
        return "SearchBeanForLabel{" + "entity=" + entity + ", searchText=" + searchText + ", type=" + type + ", languageDataBean=" + languageDataBean + '}';
    }

}
