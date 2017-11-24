/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.center.stock.databeans;

import com.argusoft.hkg.web.util.SelectItem;
import java.util.List;
import java.util.Map;

/**
 *
 * @author shreya
 */
public class LotDataBean {

    private String id;
    private Map<String, Object> lotCustom;
    private Map<String, List<Object>> featureCustomMap;
    private Map<String, Map<String, Object>> featureCustomMapValue;
    private SelectItem selectItem;
    private Map<String, String> lotDbType;
    private Map<String, Object> ruleConfigMap;
    private InvoiceDataBean invoiceDataBean;
    private ParcelDataBean parcelDataBean;
    private List<LotDataBean> lotList;
    private Boolean hasPacket;
    private Long sequenceNumber;
    private Long franchise;
    private List<String> subLots;
    private Boolean loggedInFranchise;
    Map<String, List<String>> featureDbFieldMap;

    public Long getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(Long sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public List<String> getSubLots() {
        return subLots;
    }

    public void setSubLots(List<String> subLots) {
        this.subLots = subLots;
    }

    public Map<String, Object> getLotCustom() {
        return lotCustom;
    }

    public void setLotCustom(Map<String, Object> lotCustom) {
        this.lotCustom = lotCustom;
    }

    public Map<String, String> getLotDbType() {
        return lotDbType;
    }

    public void setLotDbType(Map<String, String> lotDbType) {
        this.lotDbType = lotDbType;
    }

    public Map<String, List<Object>> getFeatureCustomMap() {
        return featureCustomMap;
    }

    public void setFeatureCustomMap(Map<String, List<Object>> featureCustomMap) {
        this.featureCustomMap = featureCustomMap;
    }

    public InvoiceDataBean getInvoiceDataBean() {
        return invoiceDataBean;
    }

    public void setInvoiceDataBean(InvoiceDataBean invoiceDataBean) {
        this.invoiceDataBean = invoiceDataBean;
    }

    public ParcelDataBean getParcelDataBean() {
        return parcelDataBean;
    }

    public void setParcelDataBean(ParcelDataBean parcelDataBean) {
        this.parcelDataBean = parcelDataBean;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public SelectItem getSelectItem() {
        return selectItem;
    }

    public void setSelectItem(SelectItem selectItem) {
        this.selectItem = selectItem;
    }

    public List<LotDataBean> getLotList() {
        return lotList;
    }

    public void setLotList(List<LotDataBean> lotList) {
        this.lotList = lotList;
    }

    public Boolean isHasPacketTrue() {
        return hasPacket;
    }

    public void setHasPacket(Boolean hasPacket) {
        this.hasPacket = hasPacket;
    }

    public Map<String, Map<String, Object>> getFeatureCustomMapValue() {
        return featureCustomMapValue;
    }

    public void setFeatureCustomMapValue(Map<String, Map<String, Object>> featureCustomMapValue) {
        this.featureCustomMapValue = featureCustomMapValue;
    }

    public Map<String, List<String>> getFeatureDbFieldMap() {
        return featureDbFieldMap;
    }

    public void setFeatureDbFieldMap(Map<String, List<String>> featureDbFieldMap) {
        this.featureDbFieldMap = featureDbFieldMap;
    }

    public Long getFranchise() {
        return franchise;
    }

    public void setFranchise(Long franchise) {
        this.franchise = franchise;
    }

    public Map<String, Object> getRuleConfigMap() {
        return ruleConfigMap;
    }

    public void setRuleConfigMap(Map<String, Object> ruleConfigMap) {
        this.ruleConfigMap = ruleConfigMap;
    }

    public Boolean getLoggedInFranchise() {
        return loggedInFranchise;
    }

    public void setLoggedInFranchise(Boolean loggedInFranchise) {
        this.loggedInFranchise = loggedInFranchise;
    }

    @Override
    public String toString() {
        return "LotDataBean{" + "id=" + id + ", lotCustom=" + lotCustom + ", featureCustomMap=" + featureCustomMap + ", featureCustomMapValue=" + featureCustomMapValue + ", selectItem=" + selectItem + ", lotDbType=" + lotDbType + ", ruleConfigMap=" + ruleConfigMap + ", invoiceDataBean=" + invoiceDataBean + ", parcelDataBean=" + parcelDataBean + ", lotList=" + lotList + ", hasPacket=" + hasPacket + ", sequenceNumber=" + sequenceNumber + ", franchise=" + franchise + ", subLots=" + subLots + ", featureDbFieldMap=" + featureDbFieldMap + '}';
    }

    
}
