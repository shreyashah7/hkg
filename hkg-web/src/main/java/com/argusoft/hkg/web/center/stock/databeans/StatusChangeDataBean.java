/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.center.stock.databeans;

import com.argusoft.hkg.web.util.SelectItem;
import java.util.Map;

/**
 *
 * @author brijesh
 */
public class StatusChangeDataBean {

    private Map<String, Map<String, Object>> featureCustomMapValue;
    private Map<String, Object> featureMap;
    private InvoiceDataBean invoiceDataBean;
    private ParcelDataBean parcelDataBean;
    private LotDataBean lotDataBean;
    private PacketDataBean packetDataBean;
    private Map<String, Object> statusChangeCustom;
    private Map<String, String> statusChangeDbType;
    private String status;
    private String proposedStatus;
    private Map<String, String> proposedStatusMap;
    private SelectItem stock;

    public StatusChangeDataBean() {
    }

    public StatusChangeDataBean(SelectItem stock) {
        this.stock = stock;
    }

    public StatusChangeDataBean(Map<String, Map<String, Object>> featureCustomMapValue, InvoiceDataBean invoiceDataBean, ParcelDataBean parcelDataBean, LotDataBean lotDataBean, PacketDataBean packetDataBean, Map<String, Object> statusChangeCustom, Map<String, String> statusChangeDbType, String status, String proposedStatus, Map<String, String> proposedStatusMap, SelectItem stock) {
        this.featureCustomMapValue = featureCustomMapValue;
        this.invoiceDataBean = invoiceDataBean;
        this.parcelDataBean = parcelDataBean;
        this.lotDataBean = lotDataBean;
        this.packetDataBean = packetDataBean;
        this.statusChangeCustom = statusChangeCustom;
        this.statusChangeDbType = statusChangeDbType;
        this.status = status;
        this.proposedStatus = proposedStatus;
        this.proposedStatusMap = proposedStatusMap;
        this.stock = stock;
    }

    public String getProposedStatus() {
        return proposedStatus;
    }

    public void setProposedStatus(String proposedStatus) {
        this.proposedStatus = proposedStatus;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Map<String, String> getProposedStatusMap() {
        return proposedStatusMap;
    }

    public void setProposedStatusMap(Map<String, String> proposedStatusMap) {
        this.proposedStatusMap = proposedStatusMap;
    }

    public SelectItem getStock() {
        return stock;
    }

    public void setStock(SelectItem stock) {
        this.stock = stock;
    }

    public PacketDataBean getPacketDataBean() {
        return packetDataBean;
    }

    public void setPacketDataBean(PacketDataBean packetDataBean) {
        this.packetDataBean = packetDataBean;
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

    public Map<String, Object> getStatusChangeCustom() {
        return statusChangeCustom;
    }

    public void setStatusChangeCustom(Map<String, Object> statusChangeCustom) {
        this.statusChangeCustom = statusChangeCustom;
    }

    public Map<String, String> getStatusChangeDbType() {
        return statusChangeDbType;
    }

    public void setStatusChangeDbType(Map<String, String> statusChangeDbType) {
        this.statusChangeDbType = statusChangeDbType;
    }

    public Map<String, Map<String, Object>> getFeatureCustomMapValue() {
        return featureCustomMapValue;
    }

    public void setFeatureCustomMapValue(Map<String, Map<String, Object>> featureCustomMapValue) {
        this.featureCustomMapValue = featureCustomMapValue;
    }

    public StatusChangeDataBean(Map<String, Map<String, Object>> featureCustomMapValue) {
        this.featureCustomMapValue = featureCustomMapValue;
    }

    public Map<String, Object> getFeatureMap() {
        return featureMap;
    }

    public void setFeatureMap(Map<String, Object> featureMap) {
        this.featureMap = featureMap;
    }

    @Override
    public String toString() {
        return "StatusChangeDataBean{" + "featureCustomMapValue=" + featureCustomMapValue + ", invoiceDataBean=" + invoiceDataBean + ", parcelDataBean=" + parcelDataBean + ", lotDataBean=" + lotDataBean + ", packetDataBean=" + packetDataBean + ", statusChangeCustom=" + statusChangeCustom + ", statusChangeDbType=" + statusChangeDbType + ", status=" + status + ", proposedStatus=" + proposedStatus + ", proposedStatusMap=" + proposedStatusMap + ", stock=" + stock + '}';
    }

}
