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
 * @author brijesh
 */
public class StockDataBean {

    private String id;
    private InvoiceDataBean invoiceDataBean;
    private ParcelDataBean parcelDataBean;
    private LotDataBean lotDataBean;
    private LotDataBean packetDataBean;
    private PacketDataBean databeanOfPacket;
    private Map<String, Object> stockCustom;
    private List<Map<Object, Object>> tabelHeaders;
    private List<SelectItem> stockList;
    private SelectItem stock;
    private List<Map<Object, Object>> generalSearchTemplate;
    private Map<String, String> stockDbType;
    private List<String> allotmentIds;
    private String parentID;
    private String type;
    private List<String> idsToMerge;
    private List<Map<String, Object>> stockDataForSplit;
    private DynamicServiceInitDataBean dynamicServiceInitBean;
    private Map<String, Map<String, Object>> featureCustomMapValue;
    private Map<String, Object> featureMap;

    public StockDataBean() {
    }

    public StockDataBean(List<Map<Object, Object>> tabelHeaders, List<SelectItem> stockList, SelectItem stock, List<Map<Object, Object>> generalSearchTemplate) {
        this.tabelHeaders = tabelHeaders;
        this.stockList = stockList;
        this.stock = stock;
        this.generalSearchTemplate = generalSearchTemplate;
    }

    public Map<String, String> getStockDbType() {
        return stockDbType;
    }

    public void setStockDbType(Map<String, String> stockDbType) {
        this.stockDbType = stockDbType;
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

    public LotDataBean getPacketDataBean() {
        return packetDataBean;
    }

    public void setPacketDataBean(LotDataBean packetDataBean) {
        this.packetDataBean = packetDataBean;
    }

    public LotDataBean getLotDataBean() {
        return lotDataBean;
    }

    public void setLotDataBean(LotDataBean lotDataBean) {
        this.lotDataBean = lotDataBean;
    }

    public Map<String, Object> getStockCustom() {
        return stockCustom;
    }

    public void setStockCustom(Map<String, Object> stockCustom) {
        this.stockCustom = stockCustom;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Map<Object, Object>> getTabelHeaders() {
        return tabelHeaders;
    }

    public void setTabelHeaders(List<Map<Object, Object>> tabelHeaders) {
        this.tabelHeaders = tabelHeaders;
    }

    public List<SelectItem> getStockList() {
        return stockList;
    }

    public void setStockList(List<SelectItem> stockList) {
        this.stockList = stockList;
    }

    public SelectItem getStock() {
        return stock;
    }

    public void setStock(SelectItem stock) {
        this.stock = stock;
    }

    public List<Map<Object, Object>> getGeneralSearchTemplate() {
        return generalSearchTemplate;
    }

    public void setGeneralSearchTemplate(List<Map<Object, Object>> generalSearchTemplate) {
        this.generalSearchTemplate = generalSearchTemplate;
    }

    public List<String> getAllotmentIds() {
        return allotmentIds;
    }

    public void setAllotmentIds(List<String> allotmentIds) {
        this.allotmentIds = allotmentIds;
    }

    public String getParentID() {
        return parentID;
    }

    public void setParentID(String parentID) {
        this.parentID = parentID;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getIdsToMerge() {
        return idsToMerge;
    }

    public void setIdsToMerge(List<String> idsToMerge) {
        this.idsToMerge = idsToMerge;
    }

    public List<Map<String, Object>> getStockDataForSplit() {
        return stockDataForSplit;
    }

    public void setStockDataForSplit(List<Map<String, Object>> stockDataForSplit) {
        this.stockDataForSplit = stockDataForSplit;
    }

    public DynamicServiceInitDataBean getDynamicServiceInitBean() {
        return dynamicServiceInitBean;
    }

    public void setDynamicServiceInitBean(DynamicServiceInitDataBean dynamicServiceInitBean) {
        this.dynamicServiceInitBean = dynamicServiceInitBean;
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

    public PacketDataBean getDatabeanOfPacket() {
        return databeanOfPacket;
    }

    public void setDatabeanOfPacket(PacketDataBean databeanOfPacket) {
        this.databeanOfPacket = databeanOfPacket;
    }

    
    @Override
    public String toString() {
        return "StockDataBean{" + "id=" + id + ", invoiceDataBean=" + invoiceDataBean + ", parcelDataBean=" + parcelDataBean + ", lotDataBean=" + lotDataBean + ", stockCustom=" + stockCustom + ", tabelHeaders=" + tabelHeaders + ", stockList=" + stockList + ", stock=" + stock + ", generalSearchTemplate=" + generalSearchTemplate + ", stockDbType=" + stockDbType + ", allotmentIds=" + allotmentIds + ", parentID=" + parentID + ", type=" + type + ", idsToMerge=" + idsToMerge + ", stockDataForSplit=" + stockDataForSplit + ", dynamicServiceInitBean=" + dynamicServiceInitBean + ", featureCustomMapValue=" + featureCustomMapValue + ", featureMap=" + featureMap + '}';
    }


}
