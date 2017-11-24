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
 * @author shreya
 */
public class PacketDataBean {

    private String id;
    private String workallocationId;
    private InvoiceDataBean invoiceDataBean;
    private ParcelDataBean parcelDataBean;
    private LotDataBean lotDataBean;
    private Map<String, Object> packetCustom;
    private Map<String, Object> ruleConfigMap;
    private Map<String, List<Object>> featureCustomMap;
    private Map<String, String> packetDbType;
    private List<PacketDataBean> packetList;
    private Map<String, List<String>> featureDbFieldMap;
    private Long sequenceNumber;
    private Long franchise;
    private Boolean isInStockOfLoggedInUser;
    private Map<String, Map<String, Object>> featureCustomMapValue;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(Long sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public Long getFranchise() {
        return franchise;
    }

    public void setFranchise(Long franchise) {
        this.franchise = franchise;
    }

    public String getWorkallocationId() {
        return workallocationId;
    }

    public void setWorkallocationId(String workallocationId) {
        this.workallocationId = workallocationId;
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

    public LotDataBean getLotDataBean() {
        return lotDataBean;
    }

    public void setLotDataBean(LotDataBean lotDataBean) {
        this.lotDataBean = lotDataBean;
    }

    public Map<String, Object> getPacketCustom() {
        return packetCustom;
    }

    public void setPacketCustom(Map<String, Object> packetCustom) {
        this.packetCustom = packetCustom;
    }

    public Map<String, List<Object>> getFeatureCustomMap() {
        return featureCustomMap;
    }

    public void setFeatureCustomMap(Map<String, List<Object>> featureCustomMap) {
        this.featureCustomMap = featureCustomMap;
    }

    public Map<String, String> getPacketDbType() {
        return packetDbType;
    }

    public void setPacketDbType(Map<String, String> packetDbType) {
        this.packetDbType = packetDbType;
    }

    public List<PacketDataBean> getPacketList() {
        return packetList;
    }

    public void setPacketList(List<PacketDataBean> packetList) {
        this.packetList = packetList;
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

    public void setFeatureDbFieldMap(Map<String, List<String>> FeatureDbFieldMap) {
        this.featureDbFieldMap = FeatureDbFieldMap;
    }

    public Map<String, Object> getRuleConfigMap() {
        return ruleConfigMap;
    }

    public void setRuleConfigMap(Map<String, Object> ruleConfigMap) {
        this.ruleConfigMap = ruleConfigMap;
    }

    public Boolean getIsInStockOfLoggedInUser() {
        return isInStockOfLoggedInUser;
    }

    public void setIsInStockOfLoggedInUser(Boolean isInStockOfLoggedInUser) {
        this.isInStockOfLoggedInUser = isInStockOfLoggedInUser;
    }

    @Override
    public String toString() {
        return "PacketDataBean{" + "id=" + id + ", workallocationId=" + workallocationId + ", invoiceDataBean=" + invoiceDataBean + ", parcelDataBean=" + parcelDataBean + ", lotDataBean=" + lotDataBean + ", packetCustom=" + packetCustom + ", ruleConfigMap=" + ruleConfigMap + ", featureCustomMap=" + featureCustomMap + ", packetDbType=" + packetDbType + ", packetList=" + packetList + ", FeatureDbFieldMap=" + featureDbFieldMap + ", sequenceNumber=" + sequenceNumber + ", franchise=" + franchise + ", featureCustomMapValue=" + featureCustomMapValue + '}';
    }

}
