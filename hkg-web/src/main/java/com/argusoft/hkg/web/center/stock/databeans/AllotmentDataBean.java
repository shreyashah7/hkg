
package com.argusoft.hkg.web.center.stock.databeans;

import com.argusoft.hkg.web.util.SelectItem;
import java.util.List;
import java.util.Map;

/**
 *
 * @author gautam
 */
public class AllotmentDataBean {
    
    private String id;
    private List<Map<Object, Object>> tabelHeaders;
    private Map<String, Object> stockCustom;
    private Map<String, Map<String, Object>> featureCustomMapValue;
    private List<SelectItem> stockList;
    private SelectItem stock;
    private String type;
    private List<Map<Object, Object>> generalSearchTemplate;
    private Map<String, String> stockDbType;
    private List<String> allotmentIds;
    private List<String> stockIds;
    private String allocationPropertyName;

    public AllotmentDataBean() {
    }
    
    public AllotmentDataBean(List<Map<Object, Object>> tabelHeaders, List<SelectItem> stockList, SelectItem stock, List<Map<Object, Object>> generalSearchTemplate) {
        this.tabelHeaders = tabelHeaders;
        this.stockList = stockList;
        this.stock = stock;
        this.generalSearchTemplate = generalSearchTemplate;
    }

    public AllotmentDataBean(String id, InvoiceDataBean invoiceDataBean, ParcelDataBean parcelDataBean, LotDataBean lotDataBean, List<Map<Object, Object>> tabelHeaders, List<SelectItem> stockList, SelectItem stock, List<Map<Object, Object>> generalSearchTemplate, Map<String, String> stockDbType, List<String> allotmentIds) {
        this.id = id;
        this.tabelHeaders = tabelHeaders;
        this.stockList = stockList;
        this.stock = stock;
        this.generalSearchTemplate = generalSearchTemplate;
        this.stockDbType = stockDbType;
        this.allotmentIds = allotmentIds;
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

    public Map<String, String> getStockDbType() {
        return stockDbType;
    }

    public void setStockDbType(Map<String, String> stockDbType) {
        this.stockDbType = stockDbType;
    }

    public List<String> getAllotmentIds() {
        return allotmentIds;
    }

    public void setAllotmentIds(List<String> allotmentIds) {
        this.allotmentIds = allotmentIds;
    }

    public List<String> getStockIds() {
        return stockIds;
    }

    public void setStockIds(List<String> stockIds) {
        this.stockIds = stockIds;
    }

    public Map<String, Object> getStockCustom() {
        return stockCustom;
    }

    public void setStockCustom(Map<String, Object> stockCustom) {
        this.stockCustom = stockCustom;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAllocationPropertyName() {
        return allocationPropertyName;
    }

    public void setAllocationPropertyName(String allocationPropertyName) {
        this.allocationPropertyName = allocationPropertyName;
    }

    public Map<String, Map<String, Object>> getFeatureCustomMapValue() {
        return featureCustomMapValue;
    }

    public void setFeatureCustomMapValue(Map<String, Map<String, Object>> featureCustomMapValue) {
        this.featureCustomMapValue = featureCustomMapValue;
    }

    @Override
    public String toString() {
        return "AllotmentDataBean{" + "id=" + id + ", tabelHeaders=" + tabelHeaders + ", stockCustom=" + stockCustom + ", featureCustomMapValue=" + featureCustomMapValue + ", stockList=" + stockList + ", stock=" + stock + ", type=" + type + ", generalSearchTemplate=" + generalSearchTemplate + ", stockDbType=" + stockDbType + ", allotmentIds=" + allotmentIds + ", stockIds=" + stockIds + ", allocationPropertyName=" + allocationPropertyName + '}';
    }

}
