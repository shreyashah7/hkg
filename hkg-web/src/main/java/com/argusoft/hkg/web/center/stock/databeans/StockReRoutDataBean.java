/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.center.stock.databeans;


import com.argusoft.hkg.web.util.SelectItem;
import com.argusoft.usermanagement.common.model.UMUser;
import java.util.List;
import java.util.Map;

/**
 *
 * @author shreya
 */
public class StockReRoutDataBean {

    private Map<String, Map<String, Object>> featureCustomMapValue;
    private InvoiceDataBean invoiceDataBean;
    private ParcelDataBean parcelDataBean;
    private LotDataBean lotDataBean;
    private PacketDataBean packetDataBean;
    private String activityId;
    private String serviceId;
    private SelectItem stock;
    private List<UMUser> allotToUserList;
    private Map<String, Object> featureMap;

    public StockReRoutDataBean() {
    }

    public StockReRoutDataBean(Map<String, Map<String, Object>> featureCustomMapValue, InvoiceDataBean invoiceDataBean, ParcelDataBean parcelDataBean, LotDataBean lotDataBean, PacketDataBean packetDataBean, String activityId, String serviceId, SelectItem stock, List<UMUser> allotToUserList) {
        this.featureCustomMapValue = featureCustomMapValue;
        this.invoiceDataBean = invoiceDataBean;
        this.parcelDataBean = parcelDataBean;
        this.lotDataBean = lotDataBean;
        this.packetDataBean = packetDataBean;
        this.activityId = activityId;
        this.serviceId = serviceId;
        this.stock = stock;
        this.allotToUserList = allotToUserList;
    }

    public Map<String, Map<String, Object>> getFeatureCustomMapValue() {
        return featureCustomMapValue;
    }

    public void setFeatureCustomMapValue(Map<String, Map<String, Object>> featureCustomMapValue) {
        this.featureCustomMapValue = featureCustomMapValue;
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

    public PacketDataBean getPacketDataBean() {
        return packetDataBean;
    }

    public void setPacketDataBean(PacketDataBean packetDataBean) {
        this.packetDataBean = packetDataBean;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public SelectItem getStock() {
        return stock;
    }

    public void setStock(SelectItem stock) {
        this.stock = stock;
    }

    public List<UMUser> getAllotToUserList() {
        return allotToUserList;
    }

    public void setAllotToUserList(List<UMUser> allotToUserList) {
        this.allotToUserList = allotToUserList;
    }

    public Map<String, Object> getFeatureMap() {
        return featureMap;
    }

    public void setFeatureMap(Map<String, Object> featureMap) {
        this.featureMap = featureMap;
    }

    @Override
    public String toString() {
        return "StockReRoutDataBean{" + "featureCustomMapValue=" + featureCustomMapValue + ", invoiceDataBean=" + invoiceDataBean + ", parcelDataBean=" + parcelDataBean + ", lotDataBean=" + lotDataBean + ", packetDataBean=" + packetDataBean + ", activityId=" + activityId + ", serviceId=" + serviceId + ", stock=" + stock + ", allotToUserList=" + allotToUserList + '}';
    }

}
