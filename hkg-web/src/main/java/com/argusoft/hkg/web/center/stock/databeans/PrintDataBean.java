package com.argusoft.hkg.web.center.stock.databeans;

import com.argusoft.hkg.web.util.SelectItem;
import java.util.List;
import java.util.Map;

/**
 *
 * @author rajkumar
 */
public class PrintDataBean {

    private Map<String, Map<String, Object>> featureCustomMapValue;

    private String id;
    private InvoiceDataBean invoiceDataBean;
    private ParcelDataBean parcelDataBean;
    private LotDataBean lotDataBean;
    private Map<String, Object> printCustom;
    private List<Map<Object, Object>> tabelHeaders;
    private List<SelectItem> printList;
    private SelectItem printData;
    private List<Map<Object, Object>> generalSearchTemplate;
    private Map<String, String> printDbType;
    Map<String, List<String>> featureDbFieldMap;
    private DynamicServiceInitDataBean dynamicServiceInitBean;

    public PrintDataBean() {
    }

    public Map<String, Map<String, Object>> getFeatureCustomMapValue() {
        return featureCustomMapValue;
    }

    public void setFeatureCustomMapValue(Map<String, Map<String, Object>> featureCustomMapValue) {
        this.featureCustomMapValue = featureCustomMapValue;
    }

    public PrintDataBean(List<Map<Object, Object>> tabelHeaders, List<SelectItem> printList, SelectItem printData, List<Map<Object, Object>> generalSearchTemplate) {
        this.tabelHeaders = tabelHeaders;
        this.printList = printList;
        this.printData = printData;
        this.generalSearchTemplate = generalSearchTemplate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Map<String, Object> getPrintCustom() {
        return printCustom;
    }

    public void setPrintCustom(Map<String, Object> printCustom) {
        this.printCustom = printCustom;
    }

    public List<Map<Object, Object>> getTabelHeaders() {
        return tabelHeaders;
    }

    public void setTabelHeaders(List<Map<Object, Object>> tabelHeaders) {
        this.tabelHeaders = tabelHeaders;
    }

    public List<SelectItem> getPrintList() {
        return printList;
    }

    public void setPrintList(List<SelectItem> printList) {
        this.printList = printList;
    }

    public SelectItem getPrintData() {
        return printData;
    }

    public void setPrintData(SelectItem printData) {
        this.printData = printData;
    }

    public List<Map<Object, Object>> getGeneralSearchTemplate() {
        return generalSearchTemplate;
    }

    public void setGeneralSearchTemplate(List<Map<Object, Object>> generalSearchTemplate) {
        this.generalSearchTemplate = generalSearchTemplate;
    }

    public Map<String, String> getPrintDbType() {
        return printDbType;
    }

    public void setPrintDbType(Map<String, String> printDbType) {
        this.printDbType = printDbType;
    }

    public Map<String, List<String>> getFeatureDbFieldMap() {
        return featureDbFieldMap;
    }

    public void setFeatureDbFieldMap(Map<String, List<String>> featureDbFieldMap) {
        this.featureDbFieldMap = featureDbFieldMap;
    }

    public DynamicServiceInitDataBean getDynamicServiceInitBean() {
        return dynamicServiceInitBean;
    }

    public void setDynamicServiceInitBean(DynamicServiceInitDataBean dynamicServiceInitBean) {
        this.dynamicServiceInitBean = dynamicServiceInitBean;
    }

    @Override
    public String toString() {
        return "PrintDataBean{" + "featureCustomMapValue=" + featureCustomMapValue + ", id=" + id + ", invoiceDataBean=" + invoiceDataBean + ", parcelDataBean=" + parcelDataBean + ", lotDataBean=" + lotDataBean + ", printCustom=" + printCustom + ", tabelHeaders=" + tabelHeaders + ", printList=" + printList + ", printData=" + printData + ", generalSearchTemplate=" + generalSearchTemplate + ", printDbType=" + printDbType + ", featureDbFieldMap=" + featureDbFieldMap + ", dynamicServiceInitBean=" + dynamicServiceInitBean + '}';
    }

    

}
