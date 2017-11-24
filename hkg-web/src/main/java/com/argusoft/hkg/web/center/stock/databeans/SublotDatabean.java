/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.center.stock.databeans;

import java.util.List;
import java.util.Map;

/**
 *
 * @author shruti
 */
public class SublotDatabean {
    private String id;
    private Map<String, Object> subLotCustom;
    private Map<String, String> subLotDbType;
    private List<String> dbFieldNames;
    private Boolean isArchive;
    private Boolean searchOnParameter;
    private Map<String, List<String>> uiFieldMap;
    private Map<String, Object> ruleConfigMap;
    private Map<String, Object> staticSearchParameters;
    private InvoiceDataBean invoiceDataBean;
    private ParcelDataBean parcelDataBean;

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the subLotCustom
     */
    public Map<String, Object> getSubLotCustom() {
        return subLotCustom;
    }

    /**
     * @param subLotCustom the subLotCustom to set
     */
    public void setSubLotCustom(Map<String, Object> subLotCustom) {
        this.subLotCustom = subLotCustom;
    }

    /**
     * @return the subLotDbType
     */
    public Map<String, String> getSubLotDbType() {
        return subLotDbType;
    }

    /**
     * @param subLotDbType the subLotDbType to set
     */
    public void setSubLotDbType(Map<String, String> subLotDbType) {
        this.subLotDbType = subLotDbType;
    }

    /**
     * @return the dbFieldNames
     */
    public List<String> getDbFieldNames() {
        return dbFieldNames;
    }

    /**
     * @param dbFieldNames the dbFieldNames to set
     */
    public void setDbFieldNames(List<String> dbFieldNames) {
        this.dbFieldNames = dbFieldNames;
    }

    /**
     * @return the isArchive
     */
    public Boolean getIsArchive() {
        return isArchive;
    }

    /**
     * @param isArchive the isArchive to set
     */
    public void setIsArchive(Boolean isArchive) {
        this.isArchive = isArchive;
    }

    /**
     * @return the searchOnParameter
     */
    public Boolean getSearchOnParameter() {
        return searchOnParameter;
    }

    /**
     * @param searchOnParameter the searchOnParameter to set
     */
    public void setSearchOnParameter(Boolean searchOnParameter) {
        this.searchOnParameter = searchOnParameter;
    }

    /**
     * @return the uiFieldMap
     */
    public Map<String, List<String>> getUiFieldMap() {
        return uiFieldMap;
    }

    /**
     * @param uiFieldMap the uiFieldMap to set
     */
    public void setUiFieldMap(Map<String, List<String>> uiFieldMap) {
        this.uiFieldMap = uiFieldMap;
    }

    /**
     * @return the staticSearchParameters
     */
    public Map<String, Object> getStaticSearchParameters() {
        return staticSearchParameters;
    }

    /**
     * @param staticSearchParameters the staticSearchParameters to set
     */
    public void setStaticSearchParameters(Map<String, Object> staticSearchParameters) {
        this.staticSearchParameters = staticSearchParameters;
    }

    /**
     * @return the invoiceDataBean
     */
    public InvoiceDataBean getInvoiceDataBean() {
        return invoiceDataBean;
    }

    /**
     * @param invoiceDataBean the invoiceDataBean to set
     */
    public void setInvoiceDataBean(InvoiceDataBean invoiceDataBean) {
        this.invoiceDataBean = invoiceDataBean;
    }

    /**
     * @return the parcelDataBean
     */
    public ParcelDataBean getParcelDataBean() {
        return parcelDataBean;
    }

    /**
     * @param parcelDataBean the parcelDataBean to set
     */
    public void setParcelDataBean(ParcelDataBean parcelDataBean) {
        this.parcelDataBean = parcelDataBean;
    }

    public Map<String, Object> getRuleConfigMap() {
        return ruleConfigMap;
    }

    public void setRuleConfigMap(Map<String, Object> ruleConfigMap) {
        this.ruleConfigMap = ruleConfigMap;
    }

    @Override
    public String toString() {
        return "SublotDatabean{" + "id=" + id + ", subLotCustom=" + subLotCustom + ", subLotDbType=" + subLotDbType + ", dbFieldNames=" + dbFieldNames + ", isArchive=" + isArchive + ", searchOnParameter=" + searchOnParameter + ", uiFieldMap=" + uiFieldMap + ", ruleConfigMap=" + ruleConfigMap + ", staticSearchParameters=" + staticSearchParameters + ", invoiceDataBean=" + invoiceDataBean + ", parcelDataBean=" + parcelDataBean + '}';
    }


}
