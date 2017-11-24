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
public class PrintBarcodeDataBean {
    
    private Map<String, Map<String, Object>> featureCustomMapValue;
    private Map<String, Object> featureMap;
    private InvoiceDataBean invoiceDataBean;
    private ParcelDataBean parcelDataBean;
    private LotDataBean lotDataBean;
    private PacketDataBean packetDataBean;
    private Map<String, Object> printBarcodeCustom;
    private Map<String, String> printBarcodeDbType;
    private List<String> fieldIds;
    private String selectedField;
    private SelectItem stock;

    public PrintBarcodeDataBean() {
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

    public List<String> getFieldIds() {
        return fieldIds;
    }

    public void setFieldIds(List<String> fieldIds) {
        this.fieldIds = fieldIds;
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

    public Map<String, Object> getPrintBarcodeCustom() {
        return printBarcodeCustom;
    }

    public void setPrintBarcodeCustom(Map<String, Object> printBarcodeCustom) {
        this.printBarcodeCustom = printBarcodeCustom;
    }

    public Map<String, String> getPrintBarcodeDbType() {
        return printBarcodeDbType;
    }

    public void setPrintBarcodeDbType(Map<String, String> printBarcodeDbType) {
        this.printBarcodeDbType = printBarcodeDbType;
    }

    public SelectItem getStock() {
        return stock;
    }

    public void setStock(SelectItem stock) {
        this.stock = stock;
    }

    public String getSelectedField() {
        return selectedField;
    }

    public void setSelectedField(String selectedField) {
        this.selectedField = selectedField;
    }

    @Override
    public String toString() {
        return "PrintBarcodeDataBean{" + "featureCustomMapValue=" + featureCustomMapValue + ", featureMap=" + featureMap + ", invoiceDataBean=" + invoiceDataBean + ", parcelDataBean=" + parcelDataBean + ", lotDataBean=" + lotDataBean + ", packetDataBean=" + packetDataBean + ", printBarcodeCustom=" + printBarcodeCustom + ", printBarcodeDbType=" + printBarcodeDbType + ", fieldIds=" + fieldIds + ", selectedField=" + selectedField + ", stock=" + stock + '}';
    }

}
