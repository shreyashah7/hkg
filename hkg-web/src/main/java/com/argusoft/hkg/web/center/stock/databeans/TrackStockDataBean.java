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
 * @author shreya
 */
public class TrackStockDataBean {

    private Map<String, Map<String, Object>> featureCustomMapValue;
    private Map<String, Object> featureMap;
    private InvoiceDataBean invoiceDataBean;
    private ParcelDataBean parcelDataBean;
    private LotDataBean lotDataBean;
    private PacketDataBean packetDataBean;
    private Map<String, Object> trackStockCustom;
    private Map<String, String> trackStockDbType;
    private Map<String, String> proposedStatusMap;
    private SelectItem stock;

    public TrackStockDataBean() {
    }

    public TrackStockDataBean(SelectItem stock) {
        this.stock = stock;
    }
    
    public Map<String, Map<String, Object>> getFeatureCustomMapValue() {
        return featureCustomMapValue;
    }

    public void setFeatureCustomMapValue(Map<String, Map<String, Object>> featureCustomMapValue) {
        this.featureCustomMapValue = featureCustomMapValue;
    }

    public Map<String, Object> getFeatureMap() {
        return featureMap;
    }

    public void setFeatureMap(Map<String, Object> featureMap) {
        this.featureMap = featureMap;
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

    public Map<String, Object> getTrackStockCustom() {
        return trackStockCustom;
    }

    public void setTrackStockCustom(Map<String, Object> trackStockCustom) {
        this.trackStockCustom = trackStockCustom;
    }

    public Map<String, String> getTrackStockDbType() {
        return trackStockDbType;
    }

    public void setTrackStockDbType(Map<String, String> trackStockDbType) {
        this.trackStockDbType = trackStockDbType;
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

    @Override
    public String toString() {
        return "TrackStockDataBean{" + "featureCustomMapValue=" + featureCustomMapValue + ", featureMap=" + featureMap + ", invoiceDataBean=" + invoiceDataBean + ", parcelDataBean=" + parcelDataBean + ", lotDataBean=" + lotDataBean + ", packetDataBean=" + packetDataBean + ", trackStockCustom=" + trackStockCustom + ", trackStockDbType=" + trackStockDbType + ", proposedStatusMap=" + proposedStatusMap + ", stock=" + stock + '}';
    }

    
}
