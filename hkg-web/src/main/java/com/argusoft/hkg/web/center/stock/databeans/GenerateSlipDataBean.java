/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.center.stock.databeans;

import com.argusoft.hkg.web.util.SelectItem;
import java.util.List;
import java.util.Map;
import org.bson.types.ObjectId;

/**
 *
 * @author shreya
 */
public class GenerateSlipDataBean {

    private Map<String, Map<String, Object>> featureCustomMapValue;
    private String id;
    private InvoiceDataBean invoiceDataBean;
    private ParcelDataBean parcelDataBean;
    private LotDataBean lotDataBean;
    private Map<String, Object> generateSlipCustom;
    private List<Map<Object, Object>> tabelHeaders;
    private List<SelectItem> generateSlipList;
    private SelectItem generateSlipData;
    private List<Map<Object, Object>> generalSearchTemplate;
    private Map<String, String> generateSlipDbType;
    private List<String> workAllotmentIds;
    private Boolean isPacket;
    private DynamicServiceInitDataBean dynamicServiceInitBean;

    public GenerateSlipDataBean() {
    }

    public GenerateSlipDataBean(List<Map<Object, Object>> tabelHeaders, List<SelectItem> generateSlipList, SelectItem generateSlipData, List<Map<Object, Object>> generalSearchTemplate) {
        this.tabelHeaders = tabelHeaders;
        this.generateSlipList = generateSlipList;
        this.generateSlipData = generateSlipData;
        this.generalSearchTemplate = generalSearchTemplate;
    }

    public Map<String, Map<String, Object>> getFeatureCustomMapValue() {
        return featureCustomMapValue;
    }

    public void setFeatureCustomMapValue(Map<String, Map<String, Object>> featureCustomMapValue) {
        this.featureCustomMapValue = featureCustomMapValue;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getIsPacket() {
        return isPacket;
    }

    public void setIsPacket(Boolean isPacket) {
        this.isPacket = isPacket;
    }

    public List<String> getWorkAllotmentIds() {
        return workAllotmentIds;
    }

    public void setWorkAllotmentIds(List<String> workAllotmentIds) {
        this.workAllotmentIds = workAllotmentIds;
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

    public Map<String, Object> getGenerateSlipCustom() {
        return generateSlipCustom;
    }

    public void setGenerateSlipCustom(Map<String, Object> generateSlipCustom) {
        this.generateSlipCustom = generateSlipCustom;
    }

    public List<Map<Object, Object>> getTabelHeaders() {
        return tabelHeaders;
    }

    public void setTabelHeaders(List<Map<Object, Object>> tabelHeaders) {
        this.tabelHeaders = tabelHeaders;
    }

    public List<SelectItem> getGenerateSlipList() {
        return generateSlipList;
    }

    public void setGenerateSlipList(List<SelectItem> generateSlipList) {
        this.generateSlipList = generateSlipList;
    }

    public SelectItem getGenerateSlipData() {
        return generateSlipData;
    }

    public void setGenerateSlipData(SelectItem generateSlipData) {
        this.generateSlipData = generateSlipData;
    }

    public List<Map<Object, Object>> getGeneralSearchTemplate() {
        return generalSearchTemplate;
    }

    public void setGeneralSearchTemplate(List<Map<Object, Object>> generalSearchTemplate) {
        this.generalSearchTemplate = generalSearchTemplate;
    }

    public Map<String, String> getGenerateSlipDbType() {
        return generateSlipDbType;
    }

    public void setGenerateSlipDbType(Map<String, String> generateSlipDbType) {
        this.generateSlipDbType = generateSlipDbType;
    }

    public DynamicServiceInitDataBean getDynamicServiceInitBean() {
        return dynamicServiceInitBean;
    }

    public void setDynamicServiceInitBean(DynamicServiceInitDataBean dynamicServiceInitBean) {
        this.dynamicServiceInitBean = dynamicServiceInitBean;
    }

    @Override
    public String toString() {
        return "GenerateSlipDataBean{" + "featureCustomMapValue=" + featureCustomMapValue + ", id=" + id + ", invoiceDataBean=" + invoiceDataBean + ", parcelDataBean=" + parcelDataBean + ", lotDataBean=" + lotDataBean + ", generateSlipCustom=" + generateSlipCustom + ", tabelHeaders=" + tabelHeaders + ", generateSlipList=" + generateSlipList + ", generateSlipData=" + generateSlipData + ", generalSearchTemplate=" + generalSearchTemplate + ", generateSlipDbType=" + generateSlipDbType + ", workAllotmentIds=" + workAllotmentIds + ", isPacket=" + isPacket + ", dynamicServiceInitBean=" + dynamicServiceInitBean + '}';
    }

}
