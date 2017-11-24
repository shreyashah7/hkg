/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.center.stock.transformers;

import com.argusoft.generic.database.exception.GenericDatabaseException;
import com.argusoft.hkg.common.constantutil.HkSystemConstantUtil;
import com.argusoft.hkg.common.functionutil.FolderManagement;
import com.argusoft.hkg.nosql.core.HkCustomFieldService;
import com.argusoft.hkg.nosql.core.HkStockService;
import com.argusoft.hkg.nosql.model.HkInvoiceDocument;
import com.argusoft.hkg.nosql.model.HkLotDocument;
import com.argusoft.hkg.nosql.model.HkPacketDocument;
import com.argusoft.hkg.nosql.model.HkParcelDocument;
import com.argusoft.hkg.nosql.model.HkSellDocument;
import com.argusoft.hkg.nosql.model.HkTransferDocument;
import com.argusoft.hkg.web.center.stock.databeans.PrintBarcodeDataBean;
import com.argusoft.hkg.web.center.usermanagement.databeans.LoginDataBean;
import com.argusoft.hkg.web.center.usermanagement.databeans.UMFeatureDetailDataBean;
import com.argusoft.hkg.web.center.util.ApplicationUtil;
import com.argusoft.hkg.web.center.util.GenerateBarcodeUtil;
import com.argusoft.hkg.web.util.SelectItem;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.dynamicreports.report.exception.DRException;
import net.sf.jasperreports.engine.JRException;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 *
 * @author shreya
 */
@Service
public class PrintBarcodeTransformer {

    private static final Logger LOGGER = LoggerFactory.getLogger(LotTransformer.class);

    final String SEPARATOR = ":";
    SimpleDateFormat mdyFormat = new SimpleDateFormat("dd/MM/yyyy");

    @Autowired
    LoginDataBean loginDataBean;
    @Autowired
    HkStockService stockService;
    @Autowired
    ApplicationUtil applicationUtil;
    @Autowired
    HkCustomFieldService customFieldService;
    @Autowired
    GenerateBarcodeUtil generateBarcodeUtil;

    public List<SelectItem> searchLotsOrPackets(PrintBarcodeDataBean printBarcodeDataBean, List<String> invoiceDbFieldName, List<String> lotDbFieldName, List<String> packetDbFieldName, List<String> parcelDbFieldName, List<String> sellDbFieldName, List<String> transferDbFieldName) throws GenericDatabaseException {
        Long franchise = loginDataBean.getCompanyId();
        List<SelectItem> result = new ArrayList<>();
        String selectedField = null;
        if (!StringUtils.isEmpty(printBarcodeDataBean.getSelectedField())) {
            selectedField = printBarcodeDataBean.getSelectedField();
        }
        //----Retrieveing the feature wise custom field and its value from UI.--------
        Map<String, Map<String, Object>> featureCustomMapValue = printBarcodeDataBean.getFeatureCustomMapValue();
        Map<String, Object> invoiceFieldMap = new HashMap<>();
        Map<String, Object> parcelFieldMap = new HashMap<>();
        Map<String, Object> lotFieldMap = new HashMap<>();
        Map<String, Object> packetFieldMap = new HashMap<>();
        Map<String, Object> sellFieldMap = new HashMap<>();
        Map<String, Object> transferFieldMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(featureCustomMapValue)) {
            //-----If it is not empty the split the custom fields with their values in different freature map like invoiceFieldMap------
            for (Map.Entry<String, Map<String, Object>> entry : featureCustomMapValue.entrySet()) {
                String featureName = entry.getKey();
                Map<String, Object> map = entry.getValue();
                if (!StringUtils.isEmpty(featureName)) {
                    if (featureName.equalsIgnoreCase(HkSystemConstantUtil.Feature.INVOICE)) {
                        if (!CollectionUtils.isEmpty(map)) {
                            for (Map.Entry<String, Object> entry1 : map.entrySet()) {
                                String key = entry1.getKey();
                                Object value = entry1.getValue();
                                if (value != null && !StringUtils.isEmpty(value) && value != "null") {
                                    invoiceFieldMap.put(key, value);
                                }
                            }
                        }
                    } else if (featureName.equalsIgnoreCase(HkSystemConstantUtil.Feature.PARCEL)) {
                        if (!CollectionUtils.isEmpty(map)) {
                            for (Map.Entry<String, Object> entry1 : map.entrySet()) {
                                String key = entry1.getKey();
                                Object value = entry1.getValue();
                                if (value != null && !StringUtils.isEmpty(value) && value != "null") {
                                    parcelFieldMap.put(key, value);
                                }
                            }
                        }
                    } else if (featureName.equalsIgnoreCase(HkSystemConstantUtil.Feature.LOT)) {

                        if (!CollectionUtils.isEmpty(map)) {
                            for (Map.Entry<String, Object> entry1 : map.entrySet()) {
                                String key = entry1.getKey();
                                Object value = entry1.getValue();
                                if (value != null && !StringUtils.isEmpty(value) && value != "null") {
                                    lotFieldMap.put(key, value);
                                }
                            }
                        }
                    } else if (featureName.equalsIgnoreCase(HkSystemConstantUtil.Feature.PACKET)) {
                        for (Map.Entry<String, Object> entry1 : map.entrySet()) {
                            String key = entry1.getKey();
                            Object value = entry1.getValue();
                            if (value != null && !StringUtils.isEmpty(value) && value != "null") {
                                packetFieldMap.put(key, value);
                            }
                        }
                    } else if (featureName.equalsIgnoreCase(HkSystemConstantUtil.Feature.SELL)) {
                        for (Map.Entry<String, Object> entry1 : map.entrySet()) {
                            String key = entry1.getKey();
                            Object value = entry1.getValue();
                            if (value != null && !StringUtils.isEmpty(value) && value != "null") {
                                sellFieldMap.put(key, value);
                            }
                        }
                    } else if (featureName.equalsIgnoreCase(HkSystemConstantUtil.Feature.TRANSFER)) {
                        for (Map.Entry<String, Object> entry1 : map.entrySet()) {
                            String key = entry1.getKey();
                            Object value = entry1.getValue();
                            if (value != null && !StringUtils.isEmpty(value) && value != "null") {
                                transferFieldMap.put(key, value);
                            }
                        }
                    }
                }
            }
        }
        List<SelectItem> selectItems = new ArrayList<>();
        if (!StringUtils.isEmpty(selectedField)) {
            String pointerComponentType = HkSystemConstantUtil.CustomField.ComponentType.POINTER + "@*" + HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN;
            if (selectedField.equalsIgnoreCase(HkSystemConstantUtil.PRINT_BARCODE_FIELD_TYPE.INVOICE)) {

                if (!CollectionUtils.isEmpty(invoiceFieldMap)) {
                    List<HkInvoiceDocument> invoiceDocuments = null;
                    if (!CollectionUtils.isEmpty(invoiceFieldMap)) {
                        Iterator<Map.Entry<String, Object>> iter = invoiceFieldMap.entrySet().iterator();
                        while (iter.hasNext()) {
                            Map.Entry<String, Object> entry = iter.next();
                            String key = entry.getKey();
                            Object value = entry.getValue();
                            if (key.contains("$MS$") || key.contains("$UMS$") || key.contains("$AG$")) {
                                if (value instanceof String && !StringUtils.isEmpty(value)) {
                                    List<String> items = Arrays.asList(value.toString().split(","));
                                    invoiceFieldMap.put(key, items);
                                } else if (value instanceof Collection && !CollectionUtils.isEmpty((Collection<?>) value)) {
                                    List<String> items = Arrays.asList(value.toString().split(","));
                                    invoiceFieldMap.put(key, items);
                                } else {
                                    iter.remove();
                                }
                            }
                        }
                        UMFeatureDetailDataBean feature = applicationUtil.getuMFeatureDetailDataBeanMap().get(HkSystemConstantUtil.Feature.INVOICE);
                        if (feature != null) {
                            invoiceDocuments = stockService.retrieveInvoices(invoiceFieldMap, franchise, Boolean.FALSE, null, null,null,null);
                        }
                    }
                    if (!CollectionUtils.isEmpty(invoiceDbFieldName)) {

                        for (HkInvoiceDocument hkInvoiceDocument : invoiceDocuments) {
                            Map<String, Object> map = new HashMap<>();
                            SelectItem selectItem = new SelectItem("null", "null", hkInvoiceDocument.getId().toString(), "null");

                            List uiFieldListForInvoice = new ArrayList<>();
                            if (hkInvoiceDocument.getFieldValue() != null) {
                                Map<String, Object> map1 = hkInvoiceDocument.getFieldValue().toMap();
                                if (!CollectionUtils.isEmpty(map1)) {
//                                    if (map1.containsKey(HkSystemConstantUtil.AutoNumber.INVOICE_ID) && map1.get(HkSystemConstantUtil.AutoNumber.INVOICE_ID) != null) {
//                                        selectItem.setCommonId(map1.get(HkSystemConstantUtil.AutoNumber.INVOICE_ID).toString());
//                                    }
                                    for (Map.Entry<String, Object> entrySet : map1.entrySet()) {
                                        String key = entrySet.getKey();
                                        Object value = entrySet.getValue();
                                        uiFieldListForInvoice.add(key);
                                    }
                                }
                            }
                            Map<String, String> uiFieldListWithComponentTypeForInvoice = customFieldService.retrieveUIFieldNameWithComponentTypes(uiFieldListForInvoice);

                            HkInvoiceDocument oldInvoiceDocument = hkInvoiceDocument;
                            Map<String, Object> customMap = hkInvoiceDocument.getFieldValue().toMap();

                            if (!CollectionUtils.isEmpty(uiFieldListWithComponentTypeForInvoice)) {
                                for (Map.Entry<String, String> field : uiFieldListWithComponentTypeForInvoice.entrySet()) {
                                    if (field.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN) || field.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.USER_MULTISELECT) || field.getValue().equals(pointerComponentType)) {
                                        if (customMap != null && !customMap.isEmpty() && customMap.containsKey(field.getKey())) {
                                            String value = customMap.get(field.getKey()).toString().replaceAll("\\[", "").replaceAll("\\]", "");
                                            customMap.put(field.getKey(), value);
                                        }
                                    }
                                }
                                oldInvoiceDocument.getFieldValue().putAll(customMap);
                            }
                            for (String model : invoiceDbFieldName) {
                                if (oldInvoiceDocument.getFieldValue().toMap().containsKey(model)) {
                                    map.put(model, oldInvoiceDocument.getFieldValue().toMap().get(model));
                                }
                            }
                            selectItem.setCategoryCustom(map);
                            selectItem.setStatus(hkInvoiceDocument.getStatus());
                            selectItems.add(selectItem);
                        }
                        result.addAll(selectItems);
                    }
                }
            } else if (selectedField.equalsIgnoreCase(HkSystemConstantUtil.PRINT_BARCODE_FIELD_TYPE.PARCEL)) {

                List<HkInvoiceDocument> invoiceDocuments = null;

                if (!CollectionUtils.isEmpty(invoiceFieldMap)) {
                    Iterator<Map.Entry<String, Object>> iter = invoiceFieldMap.entrySet().iterator();
                    while (iter.hasNext()) {
                        Map.Entry<String, Object> entry = iter.next();
                        String key = entry.getKey();
                        Object value = entry.getValue();
                        if (key.contains("$MS$") || key.contains("$UMS$") || key.contains("$AG$")) {
                            if (value instanceof String && !StringUtils.isEmpty(value)) {
                                List<String> items = Arrays.asList(value.toString().split(","));
                                invoiceFieldMap.put(key, items);
                            } else if (value instanceof Collection && !CollectionUtils.isEmpty((Collection<?>) value)) {
                                List<String> items = Arrays.asList(value.toString().split(","));
                                invoiceFieldMap.put(key, items);
                            } else {
                                iter.remove();
                            }
                        }
                    }
                    UMFeatureDetailDataBean feature = applicationUtil.getuMFeatureDetailDataBeanMap().get(HkSystemConstantUtil.Feature.INVOICE);
                    if (feature != null) {
                        invoiceDocuments = stockService.retrieveInvoices(invoiceFieldMap, franchise, Boolean.FALSE, null, null,null,null);
                    }
                }
                List<ObjectId> invoiceIds = null;
                //-----Fetching the invoice IDs from the invoice documents for retrieving lots/packets.
                if (!CollectionUtils.isEmpty(invoiceDocuments)) {
                    invoiceIds = new ArrayList<>();
                    for (HkInvoiceDocument invoiceDocument : invoiceDocuments) {
                        invoiceIds.add(invoiceDocument.getId());
                    }
                }
                List<HkParcelDocument> parcelDocuments = null;

                if (!CollectionUtils.isEmpty(parcelFieldMap) || !CollectionUtils.isEmpty(invoiceIds)) {
                    Iterator<Map.Entry<String, Object>> iter = parcelFieldMap.entrySet().iterator();
                    while (iter.hasNext()) {
                        Map.Entry<String, Object> entry = iter.next();
                        String key = entry.getKey();
                        Object value = entry.getValue();
                        if (key.contains("$MS$") || key.contains("$UMS$") || key.contains("$AG$")) {
                            if (value instanceof String && !StringUtils.isEmpty(value)) {
                                List<String> items = Arrays.asList(value.toString().split(","));
                                parcelFieldMap.put(key, items);
                            } else if (value instanceof Collection && !CollectionUtils.isEmpty((Collection<?>) value)) {
                                List<String> items = Arrays.asList(value.toString().split(","));
                                parcelFieldMap.put(key, items);
                            } else {
                                iter.remove();
                            }
                        }
                    }
                    UMFeatureDetailDataBean feature = applicationUtil.getuMFeatureDetailDataBeanMap().get(HkSystemConstantUtil.Feature.PARCEL);
                    if (feature != null) {
                        parcelDocuments = stockService.retrieveParcels(parcelFieldMap, invoiceIds, null, franchise, Boolean.FALSE, null, HkSystemConstantUtil.StockStatus.NEW_ROUGH,null,null);
                    }
                }
                if (!CollectionUtils.isEmpty(parcelDocuments)) {
                    invoiceIds = new ArrayList<>();
                    for (HkParcelDocument parcelDocument : parcelDocuments) {
                        invoiceIds.add(parcelDocument.getInvoice());
                    }
                    List<HkInvoiceDocument> hkInvoiceDocuments = stockService.retrieveInvoices(invoiceIds, franchise, Boolean.FALSE);
                    if (!CollectionUtils.isEmpty(hkInvoiceDocuments)) {
                        for (HkParcelDocument parcelDocument : parcelDocuments) {
                            Map<String, Object> map = new HashMap<>();
                            SelectItem selectItem = new SelectItem("null", parcelDocument.getId().toString(), parcelDocument.getInvoice().toString());
                            if (!CollectionUtils.isEmpty(invoiceDbFieldName)) {
                                for (HkInvoiceDocument hkInvoiceDocument : hkInvoiceDocuments) {

                                    //----Converting invoice field values retrieved from mongo db of type e.g- multiselect to string------
                                    List uiFieldListForInvoice = new ArrayList<>();
                                    if (hkInvoiceDocument.getFieldValue() != null) {
                                        Map<String, Object> map1 = hkInvoiceDocument.getFieldValue().toMap();
                                        if (!CollectionUtils.isEmpty(map1)) {
//                                            if (map1.containsKey(HkSystemConstantUtil.AutoNumber.PARCEL_ID) && map1.get(HkSystemConstantUtil.AutoNumber.PARCEL_ID) != null) {
//                                                selectItem.setCommonId(map1.get(HkSystemConstantUtil.AutoNumber.PARCEL_ID).toString());
//                                            }
                                            for (Map.Entry<String, Object> entrySet : map1.entrySet()) {
                                                String key = entrySet.getKey();
                                                Object value = entrySet.getValue();
                                                uiFieldListForInvoice.add(key);
                                            }
                                        }
                                    }
                                    Map<String, String> uiFieldListWithComponentTypeForInvoice = customFieldService.retrieveUIFieldNameWithComponentTypes(uiFieldListForInvoice);
                                    HkInvoiceDocument oldInvoiceDocument = hkInvoiceDocument;
                                    Map<String, Object> customMap = hkInvoiceDocument.getFieldValue().toMap();

                                    //-----Changing the values to its orginal values like "3:E" as Piyush Sonani and replacing back in field values---
                                    if (!CollectionUtils.isEmpty(uiFieldListWithComponentTypeForInvoice)) {
                                        for (Map.Entry<String, String> field : uiFieldListWithComponentTypeForInvoice.entrySet()) {
                                            if (field.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN) || field.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.USER_MULTISELECT) || field.getValue().equals(pointerComponentType)) {
                                                if (customMap != null && !customMap.isEmpty() && customMap.containsKey(field.getKey())) {
                                                    String value = customMap.get(field.getKey()).toString().replaceAll("\\[", "").replaceAll("\\]", "");
                                                    customMap.put(field.getKey(), value);
                                                }
                                            }
                                        }
                                        oldInvoiceDocument.getFieldValue().putAll(customMap);
                                    }
                                    //-----placing the values retreieved from the db in category custom map which are configured for search field on UI.
                                    if (hkInvoiceDocument.getId().toString().equals(parcelDocument.getInvoice().toString())) {
                                        for (String model : invoiceDbFieldName) {
                                            if (oldInvoiceDocument.getFieldValue().toMap().containsKey(model)) {
                                                map.put(model, oldInvoiceDocument.getFieldValue().toMap().get(model));
                                            }
                                        }
                                        break;
                                    }
                                }
                            }
                            List uiFieldListForParcel = new ArrayList<>();
                            if (parcelDocument.getFieldValue() != null) {
                                Map<String, Object> map1 = parcelDocument.getFieldValue().toMap();
                                if (!CollectionUtils.isEmpty(map1)) {
                                    for (Map.Entry<String, Object> entrySet : map1.entrySet()) {
                                        String key = entrySet.getKey();
                                        Object value = entrySet.getValue();
                                        uiFieldListForParcel.add(key);
                                    }
                                }
                            }
                            Map<String, String> uiFieldListWithComponentTypeForParcel = customFieldService.retrieveUIFieldNameWithComponentTypes(uiFieldListForParcel);

                            HkParcelDocument oldparcelDocument = parcelDocument;
                            Map<String, Object> customMapforParcel = parcelDocument.getFieldValue().toMap();

                            //-----Changing the values to its orginal values like "3:E" as Piyush Sonani and replacing back in field values---
                            if (!CollectionUtils.isEmpty(uiFieldListWithComponentTypeForParcel)) {
                                for (Map.Entry<String, String> field : uiFieldListWithComponentTypeForParcel.entrySet()) {
                                    if (field.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN) || field.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.USER_MULTISELECT) || field.getValue().equals(pointerComponentType)) {
                                        if (customMapforParcel != null && !customMapforParcel.isEmpty() && customMapforParcel.containsKey(field.getKey())) {
                                            String value = customMapforParcel.get(field.getKey()).toString().replaceAll("\\[", "").replaceAll("\\]", "");
                                            customMapforParcel.put(field.getKey(), value);
                                        }
                                    }
                                }
                                oldparcelDocument.getFieldValue().putAll(customMapforParcel);
                            }
                            //-----placing the values retreieved from the db in category custom map which are configured for search field on UI.
                            if (!CollectionUtils.isEmpty(parcelDbFieldName)) {
                                for (String model : parcelDbFieldName) {
                                    if (oldparcelDocument.getFieldValue().toMap().containsKey(model)) {
                                        map.put(model, oldparcelDocument.getFieldValue().toMap().get(model));
                                    }
                                }
                            }
                            selectItem.setCategoryCustom(map);
                            selectItem.setStatus(parcelDocument.getStatus());
                            selectItems.add(selectItem);
                        }
                        result.addAll(selectItems);
                    }
                }
            } else if (selectedField.equalsIgnoreCase(HkSystemConstantUtil.PRINT_BARCODE_FIELD_TYPE.LOT) || selectedField.equalsIgnoreCase(HkSystemConstantUtil.PRINT_BARCODE_FIELD_TYPE.LOT_SLIP)) {

                List<HkInvoiceDocument> invoiceDocuments = null;
                if (!CollectionUtils.isEmpty(invoiceFieldMap)) {
                    Iterator<Map.Entry<String, Object>> iter = invoiceFieldMap.entrySet().iterator();
                    while (iter.hasNext()) {
                        Map.Entry<String, Object> entry = iter.next();
                        String key = entry.getKey();
                        Object value = entry.getValue();
                        if (key.contains("$MS$") || key.contains("$UMS$") || key.contains("$AG$")) {
                            if (value instanceof String && !StringUtils.isEmpty(value)) {
                                List<String> items = Arrays.asList(value.toString().split(","));
                                invoiceFieldMap.put(key, items);
                            } else if (value instanceof Collection && !CollectionUtils.isEmpty((Collection<?>) value)) {
                                List<String> items = Arrays.asList(value.toString().split(","));
                                invoiceFieldMap.put(key, items);
                            } else {
                                iter.remove();
                            }
                        }
                    }
                    UMFeatureDetailDataBean feature = applicationUtil.getuMFeatureDetailDataBeanMap().get(HkSystemConstantUtil.Feature.INVOICE);
                    if (feature != null) {
                        invoiceDocuments = stockService.retrieveInvoices(invoiceFieldMap, franchise, Boolean.FALSE, null, null,null,null);
                    }
                }
                List<ObjectId> invoiceIds = null;
                if (!CollectionUtils.isEmpty(invoiceDocuments)) {
                    invoiceIds = new ArrayList<>();
                    for (HkInvoiceDocument invoiceDocument : invoiceDocuments) {
                        invoiceIds.add(invoiceDocument.getId());
                    }
                }
                List<HkParcelDocument> parcelDocuments = null;
                if (!CollectionUtils.isEmpty(parcelFieldMap) || !CollectionUtils.isEmpty(invoiceIds)) {
                    Iterator<Map.Entry<String, Object>> iter = parcelFieldMap.entrySet().iterator();
                    while (iter.hasNext()) {
                        Map.Entry<String, Object> entry = iter.next();
                        String key = entry.getKey();
                        Object value = entry.getValue();
                        if (key.contains("$MS$") || key.contains("$UMS$") || key.contains("$AG$")) {
                            if (value instanceof String && !StringUtils.isEmpty(value)) {
                                List<String> items = Arrays.asList(value.toString().split(","));
                                parcelFieldMap.put(key, items);
                            } else if (value instanceof Collection && !CollectionUtils.isEmpty((Collection<?>) value)) {
                                List<String> items = Arrays.asList(value.toString().split(","));
                                parcelFieldMap.put(key, items);
                            } else {
                                iter.remove();
                            }
                        }
                    }
                    UMFeatureDetailDataBean feature = applicationUtil.getuMFeatureDetailDataBeanMap().get(HkSystemConstantUtil.Feature.PARCEL);
                    if (feature != null) {
                        parcelDocuments = stockService.retrieveParcels(parcelFieldMap, invoiceIds, null, franchise, Boolean.FALSE, null, HkSystemConstantUtil.StockStatus.NEW_ROUGH,null,null);
                    }
                }

                List<ObjectId> parcelIds = null;
                if (!CollectionUtils.isEmpty(parcelDocuments)) {
                    parcelIds = new ArrayList<>();
                    for (HkParcelDocument parcelDocument : parcelDocuments) {
                        parcelIds.add(parcelDocument.getId());
                    }
                }
                Set<HkLotDocument> lotDocuments = new HashSet<>();
                List<ObjectId> lotIds = new ArrayList<>();
                if (!CollectionUtils.isEmpty(invoiceIds) || !CollectionUtils.isEmpty(parcelIds) || !CollectionUtils.isEmpty(lotFieldMap) || !CollectionUtils.isEmpty(packetFieldMap)) {
                    Set<HkLotDocument> lotDocumentsWithoutPackets = new HashSet<>();
                    Set<HkLotDocument> finalRetrievedLotDocuments = new HashSet<>();
                    if (!CollectionUtils.isEmpty(lotFieldMap) || !CollectionUtils.isEmpty(invoiceIds) || !CollectionUtils.isEmpty(parcelIds)) {
                        Iterator<Map.Entry<String, Object>> iter = lotFieldMap.entrySet().iterator();
                        while (iter.hasNext()) {
                            Map.Entry<String, Object> entry = iter.next();
                            String key = entry.getKey();
                            Object value = entry.getValue();
                            if (key.contains("$MS$") || key.contains("$UMS$") || key.contains("$AG$")) {
                                if (value instanceof String && !StringUtils.isEmpty(value)) {
                                    List<String> items = Arrays.asList(value.toString().split(","));
                                    lotFieldMap.put(key, items);
                                } else if (value instanceof Collection && !CollectionUtils.isEmpty((Collection<?>) value)) {
                                    List<String> items = Arrays.asList(value.toString().split(","));
                                    lotFieldMap.put(key, items);
                                } else {
                                    iter.remove();
                                }
                            }
                        }
                        List<HkLotDocument> hkLotDocuments = new ArrayList<>();
                        if (selectedField.equalsIgnoreCase(HkSystemConstantUtil.PRINT_BARCODE_FIELD_TYPE.LOT)) {
                            hkLotDocuments = stockService.retrieveLotsOrPacketsByCriteria(lotFieldMap, invoiceIds, parcelIds, null, franchise, Boolean.FALSE, Boolean.FALSE);
                        }
                        if (!CollectionUtils.isEmpty(hkLotDocuments)) {
                            finalRetrievedLotDocuments.addAll(hkLotDocuments);
                        }
                    }
                    //----from the total all the lots retrieved seperate the lots having hasPacket as true/false.----
                    if (!CollectionUtils.isEmpty(finalRetrievedLotDocuments)) {
                        for (HkLotDocument hkLotDocument : finalRetrievedLotDocuments) {
                            if (!hkLotDocument.isHasPacket()) {
                                //---Lots with has packet as false.-----
                                lotDocumentsWithoutPackets.add(hkLotDocument);
                                lotDocuments.add(hkLotDocument);
                            }
                        }
                    }
                }
                if (!CollectionUtils.isEmpty(lotDocuments)) {
                    invoiceIds = new ArrayList<>();
                    parcelIds = new ArrayList<>();
                    for (HkLotDocument lotDocument : lotDocuments) {
                        invoiceIds.add(lotDocument.getInvoice());
                        parcelIds.add(lotDocument.getParcel());
                    }
                    //------Retreiving invoice and parcels associated with lots as lot document contains invoice id and parcel id.----
                    List<HkInvoiceDocument> hkInvoiceDocuments = stockService.retrieveInvoices(invoiceIds, franchise, Boolean.FALSE);
                    List<HkParcelDocument> hkParcelDocuments = stockService.retrieveParcels(parcelIds, franchise, Boolean.FALSE);
                    if (!CollectionUtils.isEmpty(hkInvoiceDocuments) && !CollectionUtils.isEmpty(hkParcelDocuments)) {
                        for (HkLotDocument lotDocument : lotDocuments) {
                            Map<String, Object> map = new HashMap<>();
                            SelectItem selectItem = new SelectItem(lotDocument.getId().toString(), lotDocument.getParcel().toString(), lotDocument.getInvoice().toString());

                            //----Converting lot field values retrieved from mongo db of type e.g- multiselect to string------
                            List uiFieldListForLot = new ArrayList<>();
                            if (lotDocument.getFieldValue() != null) {
                                Map<String, Object> map1 = lotDocument.getFieldValue().toMap();
                                if (!CollectionUtils.isEmpty(map1)) {
                                    if (selectedField.equalsIgnoreCase(HkSystemConstantUtil.PRINT_BARCODE_FIELD_TYPE.LOT)) {
                                        if (map1.containsKey(HkSystemConstantUtil.AutoNumber.LOT_ID) && map1.get(HkSystemConstantUtil.AutoNumber.LOT_ID) != null) {
                                            selectItem.setCommonId(map1.get(HkSystemConstantUtil.AutoNumber.LOT_ID).toString());
                                        }
                                    } else if (selectedField.equalsIgnoreCase(HkSystemConstantUtil.PRINT_BARCODE_FIELD_TYPE.LOT_SLIP)) {
                                        if (map1.containsKey(HkSystemConstantUtil.AutoNumber.LOT_SLIP_ID) && map1.get(HkSystemConstantUtil.AutoNumber.LOT_SLIP_ID) != null) {
                                            selectItem.setCommonId(map1.get(HkSystemConstantUtil.AutoNumber.LOT_SLIP_ID).toString());
                                        }
                                    }

                                    for (Map.Entry<String, Object> entrySet : map1.entrySet()) {
                                        String key = entrySet.getKey();
                                        Object value = entrySet.getValue();
                                        uiFieldListForLot.add(key);
                                    }
                                }
                            }
                            Map<String, String> uiFieldListWithComponentTypeForLot = customFieldService.retrieveUIFieldNameWithComponentTypes(uiFieldListForLot);
                            HkLotDocument oldlotDocument = lotDocument;
                            Map<String, Object> customMapforLot = lotDocument.getFieldValue().toMap();

                            //-----Changing the values to its orginal values like "3:E" as Piyush Sonani and replacing back in field values---
                            if (!CollectionUtils.isEmpty(uiFieldListWithComponentTypeForLot)) {
                                for (Map.Entry<String, String> field : uiFieldListWithComponentTypeForLot.entrySet()) {
                                    if (field.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN) || field.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.USER_MULTISELECT) || field.getValue().equals(pointerComponentType)) {
                                        if (customMapforLot != null && !customMapforLot.isEmpty() && customMapforLot.containsKey(field.getKey())) {
                                            String value = customMapforLot.get(field.getKey()).toString().replaceAll("\\[", "").replaceAll("\\]", "");
                                            customMapforLot.put(field.getKey(), value);
                                        }
                                    }
                                }
                                oldlotDocument.getFieldValue().putAll(customMapforLot);
                            }
                            //-----placing the values retreieved from the db in category custom map which are configured for search field on UI.
                            if (!CollectionUtils.isEmpty(lotDbFieldName)) {
                                for (String model : lotDbFieldName) {
                                    if (oldlotDocument.getFieldValue().toMap().containsKey(model)) {
                                        map.put(model, oldlotDocument.getFieldValue().toMap().get(model));
                                    }
                                }
                            }

                            if (!CollectionUtils.isEmpty(invoiceDbFieldName)) {
                                for (HkInvoiceDocument hkInvoiceDocument : hkInvoiceDocuments) {

                                    //----Converting invoice field values retrieved from mongo db of type e.g- multiselect to string------
                                    List uiFieldListForInvoice = new ArrayList<>();
                                    if (hkInvoiceDocument.getFieldValue() != null) {
                                        Map<String, Object> map1 = hkInvoiceDocument.getFieldValue().toMap();
                                        if (!CollectionUtils.isEmpty(map1)) {
                                            for (Map.Entry<String, Object> entrySet : map1.entrySet()) {
                                                String key = entrySet.getKey();
                                                Object value = entrySet.getValue();
                                                uiFieldListForInvoice.add(key);
                                            }
                                        }
                                    }
                                    Map<String, String> uiFieldListWithComponentTypeForInvoice = customFieldService.retrieveUIFieldNameWithComponentTypes(uiFieldListForInvoice);
                                    HkInvoiceDocument oldInvoiceDocument = hkInvoiceDocument;
                                    Map<String, Object> customMap = hkInvoiceDocument.getFieldValue().toMap();

                                    //-----Changing the values to its orginal values like "3:E" as Piyush Sonani and replacing back in field values---
                                    if (!CollectionUtils.isEmpty(uiFieldListWithComponentTypeForInvoice)) {
                                        for (Map.Entry<String, String> field : uiFieldListWithComponentTypeForInvoice.entrySet()) {
                                            if (field.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN) || field.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.USER_MULTISELECT) || field.getValue().equals(pointerComponentType)) {
                                                if (customMap != null && !customMap.isEmpty() && customMap.containsKey(field.getKey())) {
                                                    String value = customMap.get(field.getKey()).toString().replaceAll("\\[", "").replaceAll("\\]", "");
                                                    customMap.put(field.getKey(), value);
                                                }
                                            }
                                        }
                                        oldInvoiceDocument.getFieldValue().putAll(customMap);
                                    }
                                    //-----placing the values retreieved from the db in category custom map which are configured for search field on UI.
                                    if (hkInvoiceDocument.getId().toString().equals(lotDocument.getInvoice().toString())) {
                                        for (String model : invoiceDbFieldName) {
                                            if (oldInvoiceDocument.getFieldValue().toMap().containsKey(model)) {
                                                map.put(model, oldInvoiceDocument.getFieldValue().toMap().get(model));
                                            }
                                        }
                                        break;
                                    }
                                }
                            }
                            if (!CollectionUtils.isEmpty(parcelDbFieldName)) {
                                for (HkParcelDocument hkParcelDocument : hkParcelDocuments) {

                                    //----Converting parcel field values retrieved from mongo db of type e.g- multiselect to string------
                                    List uiFieldListForParcel = new ArrayList<>();
                                    if (hkParcelDocument.getFieldValue() != null) {
                                        Map<String, Object> map1 = hkParcelDocument.getFieldValue().toMap();
                                        if (!CollectionUtils.isEmpty(map1)) {
                                            for (Map.Entry<String, Object> entrySet : map1.entrySet()) {
                                                String key = entrySet.getKey();
                                                Object value = entrySet.getValue();
                                                uiFieldListForParcel.add(key);
                                            }
                                        }
                                    }

                                    Map<String, String> uiFieldListWithComponentTypeForParcel = customFieldService.retrieveUIFieldNameWithComponentTypes(uiFieldListForParcel);
                                    HkParcelDocument oldparcelDocument = hkParcelDocument;
                                    Map<String, Object> customMap = hkParcelDocument.getFieldValue().toMap();

                                    //-----Changing the values to its orginal values like "3:E" as Piyush Sonani and replacing back in field values---
                                    if (!CollectionUtils.isEmpty(uiFieldListWithComponentTypeForParcel)) {
                                        for (Map.Entry<String, String> field : uiFieldListWithComponentTypeForParcel.entrySet()) {
                                            if (field.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN) || field.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.USER_MULTISELECT) || field.getValue().equals(pointerComponentType)) {
                                                if (customMap != null && !customMap.isEmpty() && customMap.containsKey(field.getKey())) {
                                                    String value = customMap.get(field.getKey()).toString().replaceAll("\\[", "").replaceAll("\\]", "");
                                                    customMap.put(field.getKey(), value);
                                                }
                                            }
                                        }
                                        oldparcelDocument.getFieldValue().putAll(customMap);
                                    }
                                    //-----placing the values retreieved from the db in category custom map which are configured for search field on UI.
                                    if (hkParcelDocument.getId().toString().equals(lotDocument.getParcel().toString())) {
                                        for (String model : parcelDbFieldName) {
                                            if (oldparcelDocument.getFieldValue().toMap().containsKey(model)) {
                                                map.put(model, oldparcelDocument.getFieldValue().toMap().get(model));
                                            }
                                        }
                                        break;
                                    }
                                }
                            }
                            selectItem.setCategoryCustom(map);
                            selectItem.setStatus(lotDocument.getStatus());
                            selectItems.add(selectItem);
                        }
                        result.addAll(selectItems);
                    }
                }
            } else if (selectedField.equalsIgnoreCase(HkSystemConstantUtil.PRINT_BARCODE_FIELD_TYPE.PACKET) || selectedField.equalsIgnoreCase(HkSystemConstantUtil.PRINT_BARCODE_FIELD_TYPE.PACKET_SLIP)) {
                List<HkInvoiceDocument> invoiceDocuments = null;
                //-------Retrieving the invoice documents based on the field and the value given in the search field on UI.
                if (!CollectionUtils.isEmpty(invoiceFieldMap)) {
                    Iterator<Map.Entry<String, Object>> iter = invoiceFieldMap.entrySet().iterator();
                    while (iter.hasNext()) {
                        Map.Entry<String, Object> entry = iter.next();
                        String key = entry.getKey();
                        Object value = entry.getValue();
                        if (key.contains("$MS$") || key.contains("$UMS$") || key.contains("$AG$")) {
                            if (value instanceof String && !StringUtils.isEmpty(value)) {
                                List<String> items = Arrays.asList(value.toString().split(","));
                                invoiceFieldMap.put(key, items);
                            } else if (value instanceof Collection && !CollectionUtils.isEmpty((Collection<?>) value)) {
                                List<String> items = Arrays.asList(value.toString().split(","));
                                invoiceFieldMap.put(key, items);
                            } else {
                                iter.remove();
                            }
                        }
                    }
                    UMFeatureDetailDataBean feature = applicationUtil.getuMFeatureDetailDataBeanMap().get(HkSystemConstantUtil.Feature.INVOICE);
                    if (feature != null && !CollectionUtils.isEmpty(invoiceFieldMap)) {
                        invoiceDocuments = stockService.retrieveInvoices(invoiceFieldMap, franchise, Boolean.FALSE, null, null,null,null);
                    }
                }
                Set<HkPacketDocument> packetDocuments = new HashSet<>();
                List<ObjectId> invoiceIds = null;
                //-----Fetching the invoice IDs from the invoice documents for retrieving lots/packets.
                if (!CollectionUtils.isEmpty(invoiceDocuments)) {
                    invoiceIds = new ArrayList<>();
                    for (HkInvoiceDocument invoiceDocument : invoiceDocuments) {
                        invoiceIds.add(invoiceDocument.getId());
                    }
                }
                List<HkParcelDocument> parcelDocuments = null;
                //----Retrieving the parcel documents based on the field and values given in the search field on UI as well as the parcels belonging to particular invoice.
                if (!CollectionUtils.isEmpty(parcelFieldMap) || !CollectionUtils.isEmpty(invoiceIds)) {
                    Iterator<Map.Entry<String, Object>> iter = parcelFieldMap.entrySet().iterator();
                    while (iter.hasNext()) {
                        Map.Entry<String, Object> entry = iter.next();
                        String key = entry.getKey();
                        Object value = entry.getValue();
                        if (key.contains("$MS$") || key.contains("$UMS$") || key.contains("$AG$")) {
                            if (value instanceof String && !StringUtils.isEmpty(value)) {
                                List<String> items = Arrays.asList(value.toString().split(","));
                                parcelFieldMap.put(key, items);
                            } else if (value instanceof Collection && !CollectionUtils.isEmpty((Collection<?>) value)) {
                                List<String> items = Arrays.asList(value.toString().split(","));
                                parcelFieldMap.put(key, items);
                            } else {
                                iter.remove();
                            }
                        }
                    }
                    UMFeatureDetailDataBean feature = applicationUtil.getuMFeatureDetailDataBeanMap().get(HkSystemConstantUtil.Feature.PARCEL);
                    if (feature != null && !CollectionUtils.isEmpty(parcelFieldMap) || !CollectionUtils.isEmpty(invoiceIds)) {
                        parcelDocuments = stockService.retrieveParcels(parcelFieldMap, invoiceIds, null, franchise, Boolean.FALSE, null, HkSystemConstantUtil.StockStatus.NEW_ROUGH,null,null);
                    }
                }

                List<ObjectId> parcelIds = null;
                //-----Fetching the parcel IDs from the parcel documents for retrieving lots/packets.
                if (!CollectionUtils.isEmpty(parcelDocuments)) {
                    parcelIds = new ArrayList<>();
                    for (HkParcelDocument parcelDocument : parcelDocuments) {
                        parcelIds.add(parcelDocument.getId());
                    }
                }
                Set<HkLotDocument> lotDocuments = new HashSet<>();
                List<ObjectId> lotIds = new ArrayList<>();
                //----Sending invoiceIds,parcelIds retrieved from the documents from the search field above,lot field values or parcel field values any of the one exists-------
                if (!CollectionUtils.isEmpty(invoiceIds) || !CollectionUtils.isEmpty(parcelIds) || !CollectionUtils.isEmpty(lotFieldMap)) {
                    Set<HkLotDocument> lotDocumentsWithPackets = new HashSet<>();
                    Set<HkLotDocument> lotDocumentsWithoutPackets = new HashSet<>();
                    Set<HkLotDocument> finalRetrievedLotDocuments = new HashSet<>();
                    if (!CollectionUtils.isEmpty(lotFieldMap) || !CollectionUtils.isEmpty(invoiceIds) || !CollectionUtils.isEmpty(parcelIds)) {
                        Iterator<Map.Entry<String, Object>> iter = lotFieldMap.entrySet().iterator();
                        while (iter.hasNext()) {
                            Map.Entry<String, Object> entry = iter.next();
                            String key = entry.getKey();
                            Object value = entry.getValue();
                            if (key.contains("$MS$") || key.contains("$UMS$") || key.contains("$AG$")) {
                                if (value instanceof String && !StringUtils.isEmpty(value)) {
                                    List<String> items = Arrays.asList(value.toString().split(","));
                                    lotFieldMap.put(key, items);
                                } else if (value instanceof Collection && !CollectionUtils.isEmpty((Collection<?>) value)) {
                                    List<String> items = Arrays.asList(value.toString().split(","));
                                    lotFieldMap.put(key, items);
                                } else {
                                    iter.remove();
                                }
                            }
                        }
                        if (!CollectionUtils.isEmpty(invoiceIds) || !CollectionUtils.isEmpty(parcelIds) || !CollectionUtils.isEmpty(lotFieldMap)) {
                            List<HkLotDocument> hkLotDocuments = stockService.retrieveLots(lotFieldMap, invoiceIds, parcelIds, null, franchise, Boolean.FALSE);
                            if (!CollectionUtils.isEmpty(hkLotDocuments)) {
                                finalRetrievedLotDocuments.addAll(hkLotDocuments);
                            }
                        }
                    }
                    //----from the total all the lots retrieved seperate the lots having hasPacket as true/false.----
                    if (!CollectionUtils.isEmpty(finalRetrievedLotDocuments)) {
                        for (HkLotDocument hkLotDocument : finalRetrievedLotDocuments) {
                            if (!hkLotDocument.isHasPacket()) {
                                //---Lots with has packet as false.-----
                                lotDocumentsWithoutPackets.add(hkLotDocument);
                                lotDocuments.add(hkLotDocument);
                            } else {
                                //---Lots with has packet as true..------
                                lotDocumentsWithPackets.add(hkLotDocument);
                            }
                        }
                    }
                    //---Fetching lotIds from lot documents containing packets.(has packet=true)
                    if (!CollectionUtils.isEmpty(lotDocumentsWithPackets)) {
                        lotIds = new ArrayList<>();
                        for (HkLotDocument lotDocument : lotDocumentsWithPackets) {
                            lotIds.add(lotDocument.getId());
                        }
                    }
                    //---Retrieveing Packets from the packet field value and the associated lots.----
                    if (!CollectionUtils.isEmpty(packetFieldMap) || !CollectionUtils.isEmpty(invoiceIds) || !CollectionUtils.isEmpty(parcelIds) || !CollectionUtils.isEmpty(lotIds)) {
                        Iterator<Map.Entry<String, Object>> iterPackt = packetFieldMap.entrySet().iterator();
                        while (iterPackt.hasNext()) {
                            Map.Entry<String, Object> entry = iterPackt.next();
                            String key = entry.getKey();
                            Object value = entry.getValue();
                            if (key.contains("$MS$") || key.contains("$UMS$") || key.contains("$AG$")) {
                                if (value instanceof String && !StringUtils.isEmpty(value)) {
                                    List<String> items = Arrays.asList(value.toString().split(","));
                                    packetFieldMap.put(key, items);
                                } else if (value instanceof Collection && !CollectionUtils.isEmpty((Collection<?>) value)) {
                                    List<String> items = Arrays.asList(value.toString().split(","));
                                    packetFieldMap.put(key, items);
                                } else {
                                    iterPackt.remove();
                                }
                            }
                        }
                        if (selectedField.equalsIgnoreCase(HkSystemConstantUtil.PRINT_BARCODE_FIELD_TYPE.PACKET) && (!CollectionUtils.isEmpty(packetFieldMap) || !CollectionUtils.isEmpty(invoiceIds) || !CollectionUtils.isEmpty(parcelIds) || !CollectionUtils.isEmpty(lotIds))) {
                            packetDocuments.addAll(stockService.retrievePackets(packetFieldMap, invoiceIds, parcelIds, lotIds, franchise, Boolean.FALSE, null, null,null,null));
                        } else if (selectedField.equalsIgnoreCase(HkSystemConstantUtil.PRINT_BARCODE_FIELD_TYPE.PACKET_SLIP) && (!CollectionUtils.isEmpty(packetFieldMap) || !CollectionUtils.isEmpty(invoiceIds) || !CollectionUtils.isEmpty(parcelIds) || !CollectionUtils.isEmpty(lotIds))) {
                            packetDocuments.addAll(stockService.retrievePackets(packetFieldMap, invoiceIds, parcelIds, lotIds, franchise, Boolean.FALSE, null, null,null,null));
                        }
                    }
                }
                if (!CollectionUtils.isEmpty(packetDocuments)) {
                    selectItems = new ArrayList<>();
                    invoiceIds = new ArrayList<>();
                    parcelIds = new ArrayList<>();
                    lotIds = new ArrayList<>();
                    for (HkPacketDocument packetDocument : packetDocuments) {
                        invoiceIds.add(packetDocument.getInvoice());
                        parcelIds.add(packetDocument.getParcel());
                        lotIds.add(packetDocument.getLot());
                    }
                    //------Retreiving invoice and parcels associated with packets as packet document contains invoice id and parcel id.----
                    List<HkInvoiceDocument> hkInvoiceDocuments = stockService.retrieveInvoices(invoiceIds, franchise, Boolean.FALSE);
                    List<HkParcelDocument> hkParcelDocuments = stockService.retrieveParcels(parcelIds, franchise, Boolean.FALSE);
                    List<HkLotDocument> hklotDocuments = stockService.retrieveLotsByIds(lotIds, franchise, Boolean.FALSE, null, null, Boolean.TRUE);
                    if (!CollectionUtils.isEmpty(hkInvoiceDocuments) && !CollectionUtils.isEmpty(hkParcelDocuments)) {
                        for (HkPacketDocument packetDocument : packetDocuments) {
                            Map<String, Object> map = new HashMap<>();
                            SelectItem selectItem = new SelectItem(packetDocument.getLot().toString(), packetDocument.getParcel().toString(), packetDocument.getInvoice().toString(), packetDocument.getId().toString());

                            //----Converting packet field values retrieved from mongo db of type e.g- multiselect to string------
                            List uiFieldListForPacket = new ArrayList<>();
                            if (packetDocument.getFieldValue() != null) {
                                Map<String, Object> map1 = packetDocument.getFieldValue().toMap();
                                if (!CollectionUtils.isEmpty(map1)) {
                                    if (selectedField.equalsIgnoreCase(HkSystemConstantUtil.PRINT_BARCODE_FIELD_TYPE.PACKET)) {
                                        if (map1.containsKey(HkSystemConstantUtil.AutoNumber.PACKET_ID) && map1.get(HkSystemConstantUtil.AutoNumber.PACKET_ID) != null) {
                                            selectItem.setCommonId(map1.get(HkSystemConstantUtil.AutoNumber.PACKET_ID).toString());
                                        }
                                    } else if (selectedField.equalsIgnoreCase(HkSystemConstantUtil.PRINT_BARCODE_FIELD_TYPE.PACKET_SLIP)) {
                                        if (map1.containsKey(HkSystemConstantUtil.AutoNumber.PACKET_SLIP_ID) && map1.get(HkSystemConstantUtil.AutoNumber.PACKET_SLIP_ID) != null) {
                                            selectItem.setCommonId(map1.get(HkSystemConstantUtil.AutoNumber.PACKET_SLIP_ID).toString());
                                        }
                                    }
                                    for (Map.Entry<String, Object> entrySet : map1.entrySet()) {
                                        String key = entrySet.getKey();
                                        Object value = entrySet.getValue();
                                        uiFieldListForPacket.add(key);
                                    }
                                }
                            }

                            Map<String, String> uiFieldListWithComponentTypeForPacket = customFieldService.retrieveUIFieldNameWithComponentTypes(uiFieldListForPacket);
                            HkPacketDocument oldpacketDocument = packetDocument;
                            Map<String, Object> customMapForPacket = packetDocument.getFieldValue().toMap();

                            //-----Changing the values to its orginal values like "3:E" as Piyush Sonani and replacing back in field values---
                            if (!CollectionUtils.isEmpty(uiFieldListWithComponentTypeForPacket)) {
                                for (Map.Entry<String, String> field : uiFieldListWithComponentTypeForPacket.entrySet()) {
                                    if (field.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN) || field.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.USER_MULTISELECT) || field.getValue().equals(pointerComponentType)) {
                                        if (customMapForPacket != null && !customMapForPacket.isEmpty() && customMapForPacket.containsKey(field.getKey())) {
                                            String value = customMapForPacket.get(field.getKey()).toString().replaceAll("\\[", "").replaceAll("\\]", "");
                                            customMapForPacket.put(field.getKey(), value);
                                        }
                                    }
                                }
                                oldpacketDocument.getFieldValue().putAll(customMapForPacket);
                            }

                            //-----placing the values retreieved from the db in category custom map which are configured for search field on UI.
                            if (!CollectionUtils.isEmpty(packetDbFieldName)) {
                                for (String model : packetDbFieldName) {
                                    if (oldpacketDocument.getFieldValue().toMap().containsKey(model)) {
                                        map.put(model, oldpacketDocument.getFieldValue().toMap().get(model));
                                    }
                                }
                            }

                            if (!CollectionUtils.isEmpty(invoiceDbFieldName)) {
                                for (HkInvoiceDocument hkInvoiceDocument : hkInvoiceDocuments) {

                                    //----Converting invoice field values retrieved from mongo db of type e.g- multiselect to string------
                                    List uiFieldListForInvoice = new ArrayList<>();
                                    if (hkInvoiceDocument.getFieldValue() != null) {
                                        Map<String, Object> map1 = hkInvoiceDocument.getFieldValue().toMap();
                                        if (!CollectionUtils.isEmpty(map1)) {
                                            for (Map.Entry<String, Object> entrySet : map1.entrySet()) {
                                                String key = entrySet.getKey();
                                                Object value = entrySet.getValue();
                                                uiFieldListForInvoice.add(key);
                                            }
                                        }
                                    }

                                    Map<String, String> uiFieldListWithComponentTypeForInvoice = customFieldService.retrieveUIFieldNameWithComponentTypes(uiFieldListForInvoice);
                                    HkInvoiceDocument oldInvoiceDocument = hkInvoiceDocument;
                                    Map<String, Object> customMap = hkInvoiceDocument.getFieldValue().toMap();

                                    //-----Changing the values to its orginal values like "3:E" as Piyush Sonani and replacing back in field values---
                                    if (!CollectionUtils.isEmpty(uiFieldListWithComponentTypeForInvoice)) {
                                        for (Map.Entry<String, String> field : uiFieldListWithComponentTypeForInvoice.entrySet()) {
                                            if (field.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN) || field.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.USER_MULTISELECT) || field.getValue().equals(pointerComponentType)) {
                                                if (customMap != null && !customMap.isEmpty() && customMap.containsKey(field.getKey())) {
                                                    String value = customMap.get(field.getKey()).toString().replaceAll("\\[", "").replaceAll("\\]", "");
                                                    customMap.put(field.getKey(), value);
                                                }
                                            }
                                        }
                                        oldInvoiceDocument.getFieldValue().putAll(customMap);
                                    }

                                    //-----placing the values retreieved from the db in category custom map which are configured for search field on UI.
                                    if (oldInvoiceDocument.getId().toString().equals(packetDocument.getInvoice().toString())) {
                                        for (String model : invoiceDbFieldName) {
                                            if (oldInvoiceDocument.getFieldValue().toMap().containsKey(model)) {
                                                map.put(model, oldInvoiceDocument.getFieldValue().toMap().get(model));
                                            }
                                        }
                                        break;
                                    }
                                }
                            }
                            if (!CollectionUtils.isEmpty(parcelDbFieldName)) {
                                for (HkParcelDocument hkParcelDocument : hkParcelDocuments) {

                                    //----Converting parcel field values retrieved from mongo db of type e.g- multiselect to string------
                                    List uiFieldListForParcel = new ArrayList<>();
                                    if (hkParcelDocument.getFieldValue() != null) {
                                        Map<String, Object> map1 = hkParcelDocument.getFieldValue().toMap();
                                        if (!CollectionUtils.isEmpty(map1)) {
                                            for (Map.Entry<String, Object> entrySet : map1.entrySet()) {
                                                String key = entrySet.getKey();
                                                Object value = entrySet.getValue();
                                                uiFieldListForParcel.add(key);
                                            }
                                        }
                                    }

                                    Map<String, String> uiFieldListWithComponentTypeForParcel = customFieldService.retrieveUIFieldNameWithComponentTypes(uiFieldListForParcel);
                                    HkParcelDocument oldparcelDocument = hkParcelDocument;
                                    Map<String, Object> customMap = hkParcelDocument.getFieldValue().toMap();

                                    //-----Changing the values to its orginal values like "3:E" as Piyush Sonani and replacing back in field values---
                                    if (!CollectionUtils.isEmpty(uiFieldListWithComponentTypeForParcel)) {
                                        for (Map.Entry<String, String> field : uiFieldListWithComponentTypeForParcel.entrySet()) {
                                            if (field.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN) || field.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.USER_MULTISELECT) || field.getValue().equals(pointerComponentType)) {
                                                if (customMap != null && !customMap.isEmpty() && customMap.containsKey(field.getKey())) {
                                                    String value = customMap.get(field.getKey()).toString().replaceAll("\\[", "").replaceAll("\\]", "");
                                                    customMap.put(field.getKey(), value);
                                                }
                                            }
                                        }
                                        oldparcelDocument.getFieldValue().putAll(customMap);
                                    }

                                    //-----placing the values retreieved from the db in category custom map which are configured for search field on UI.
                                    if (oldparcelDocument.getId().toString().equals(packetDocument.getParcel().toString())) {
                                        for (String model : parcelDbFieldName) {
                                            if (oldparcelDocument.getFieldValue().toMap().containsKey(model)) {
                                                map.put(model, oldparcelDocument.getFieldValue().toMap().get(model));
                                            }
                                        }
                                        break;
                                    }
                                }
                            }
                            if (!CollectionUtils.isEmpty(lotDbFieldName)) {
                                for (HkLotDocument hkLotDocument : hklotDocuments) {

                                    //----Converting parcel field values retrieved from mongo db of type e.g- multiselect to string------
                                    List uiFieldListForLot = new ArrayList<>();
                                    if (hkLotDocument.getFieldValue() != null) {
                                        Map<String, Object> map1 = hkLotDocument.getFieldValue().toMap();
                                        if (!CollectionUtils.isEmpty(map1)) {
                                            for (Map.Entry<String, Object> entrySet : map1.entrySet()) {
                                                String key = entrySet.getKey();
                                                Object value = entrySet.getValue();
                                                uiFieldListForLot.add(key);
                                            }
                                        }
                                    }
                                    Map<String, String> uiFieldListWithComponentTypeForLot = customFieldService.retrieveUIFieldNameWithComponentTypes(uiFieldListForLot);
                                    HkLotDocument oldlotDocument = hkLotDocument;
                                    Map<String, Object> customMapforLot = hkLotDocument.getFieldValue().toMap();

                                    //-----Changing the values to its orginal values like "3:E" as Piyush Sonani and replacing back in field values---
                                    if (!CollectionUtils.isEmpty(uiFieldListWithComponentTypeForLot)) {
                                        for (Map.Entry<String, String> field : uiFieldListWithComponentTypeForLot.entrySet()) {
                                            if (field.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN) || field.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.USER_MULTISELECT) || field.getValue().equals(pointerComponentType)) {
                                                if (customMapforLot != null && !customMapforLot.isEmpty() && customMapforLot.containsKey(field.getKey())) {
                                                    String value = customMapforLot.get(field.getKey()).toString().replaceAll("\\[", "").replaceAll("\\]", "");
                                                    customMapforLot.put(field.getKey(), value);
                                                }
                                            }
                                        }
                                        oldlotDocument.getFieldValue().putAll(customMapforLot);
                                    }

                                    //-----placing the values retreieved from the db in category custom map which are configured for search field on UI.
                                    if (oldlotDocument.getId().toString().equals(packetDocument.getLot().toString())) {
                                        for (String model : lotDbFieldName) {
                                            if (oldlotDocument.getFieldValue().toMap().containsKey(model)) {
                                                map.put(model, oldlotDocument.getFieldValue().toMap().get(model));
                                            }
                                        }
                                        break;
                                    }
                                }
                            }
                            selectItem.setCategoryCustom(map);
                            selectItem.setStatus(packetDocument.getStatus());
                            selectItems.add(selectItem);
                        }
                        result.addAll(selectItems);
                    }
                }

            } else if (selectedField.equalsIgnoreCase(HkSystemConstantUtil.PRINT_BARCODE_FIELD_TYPE.SELL)) {
                List<HkInvoiceDocument> invoiceDocuments = null;
                if (!CollectionUtils.isEmpty(invoiceFieldMap)) {
                    Iterator<Map.Entry<String, Object>> iter = invoiceFieldMap.entrySet().iterator();
                    while (iter.hasNext()) {
                        Map.Entry<String, Object> entry = iter.next();
                        String key = entry.getKey();
                        Object value = entry.getValue();
                        if (key.contains("$MS$") || key.contains("$UMS$") || key.contains("$AG$")) {
                            if (value instanceof String && !StringUtils.isEmpty(value)) {
                                List<String> items = Arrays.asList(value.toString().split(","));
                                invoiceFieldMap.put(key, items);
                            } else if (value instanceof Collection && !CollectionUtils.isEmpty((Collection<?>) value)) {
                                List<String> items = Arrays.asList(value.toString().split(","));
                                invoiceFieldMap.put(key, items);
                            } else {
                                iter.remove();
                            }
                        }
                    }
                    UMFeatureDetailDataBean feature = applicationUtil.getuMFeatureDetailDataBeanMap().get(HkSystemConstantUtil.Feature.INVOICE);
                    if (feature != null) {
                        invoiceDocuments = stockService.retrieveInvoices(invoiceFieldMap, franchise, Boolean.FALSE, null, null,null,null);
                    }
                }
                Set<HkPacketDocument> packetDocuments = new HashSet<>();
                List<ObjectId> invoiceIds = null;
                if (!CollectionUtils.isEmpty(invoiceDocuments)) {
                    invoiceIds = new ArrayList<>();
                    for (HkInvoiceDocument invoiceDocument : invoiceDocuments) {
                        invoiceIds.add(invoiceDocument.getId());
                    }
                }
                List<HkParcelDocument> parcelDocuments = null;
                if (!CollectionUtils.isEmpty(parcelFieldMap) || !CollectionUtils.isEmpty(invoiceIds)) {
                    Iterator<Map.Entry<String, Object>> iter = parcelFieldMap.entrySet().iterator();
                    while (iter.hasNext()) {
                        Map.Entry<String, Object> entry = iter.next();
                        String key = entry.getKey();
                        Object value = entry.getValue();
                        if (key.contains("$MS$") || key.contains("$UMS$") || key.contains("$AG$")) {
                            if (value instanceof String && !StringUtils.isEmpty(value)) {
                                List<String> items = Arrays.asList(value.toString().split(","));
                                parcelFieldMap.put(key, items);
                            } else if (value instanceof Collection && !CollectionUtils.isEmpty((Collection<?>) value)) {
                                List<String> items = Arrays.asList(value.toString().split(","));
                                parcelFieldMap.put(key, items);
                            } else {
                                iter.remove();
                            }
                        }
                    }
                    UMFeatureDetailDataBean feature = applicationUtil.getuMFeatureDetailDataBeanMap().get(HkSystemConstantUtil.Feature.PARCEL);
                    if (feature != null) {
                        parcelDocuments = stockService.retrieveParcels(parcelFieldMap, invoiceIds, null, franchise, Boolean.FALSE, null, HkSystemConstantUtil.StockStatus.NEW_ROUGH,null,null);
                    }
                }
                List<ObjectId> parcelIds = null;
                //-----Fetching the parcel IDs from the parcel documents for retrieving lots/packets.
                if (!CollectionUtils.isEmpty(parcelDocuments)) {
                    parcelIds = new ArrayList<>();
                    for (HkParcelDocument parcelDocument : parcelDocuments) {
                        parcelIds.add(parcelDocument.getId());
                    }
                }
                Set<HkLotDocument> lotDocuments = new HashSet<>();
                List<ObjectId> lotIds = new ArrayList<>();
                List<ObjectId> lotIdsWithPacket = new ArrayList<>();
                List<ObjectId> lotIdsWithoutPacket = new ArrayList<>();
                //----Sending invoiceIds,parcelIds retrieved from the documents from the search field above,lot field values or parcel field values any of the one exists-------
                if (!CollectionUtils.isEmpty(invoiceIds) || !CollectionUtils.isEmpty(parcelIds) || !CollectionUtils.isEmpty(lotFieldMap) || !CollectionUtils.isEmpty(packetFieldMap)) {
                    Set<HkLotDocument> lotDocumentsWithPackets = new HashSet<>();
                    Set<HkLotDocument> lotDocumentsWithoutPackets = new HashSet<>();
                    Set<HkLotDocument> finalRetrievedLotDocuments = new HashSet<>();
                    if (!CollectionUtils.isEmpty(lotFieldMap) || !CollectionUtils.isEmpty(invoiceIds) || !CollectionUtils.isEmpty(parcelIds)) {
                        Iterator<Map.Entry<String, Object>> iter = lotFieldMap.entrySet().iterator();
                        while (iter.hasNext()) {
                            Map.Entry<String, Object> entry = iter.next();
                            String key = entry.getKey();
                            Object value = entry.getValue();
                            if (key.contains("$MS$") || key.contains("$UMS$") || key.contains("$AG$")) {
                                if (value instanceof String && !StringUtils.isEmpty(value)) {
                                    List<String> items = Arrays.asList(value.toString().split(","));
                                    lotFieldMap.put(key, items);
                                } else if (value instanceof Collection && !CollectionUtils.isEmpty((Collection<?>) value)) {
                                    List<String> items = Arrays.asList(value.toString().split(","));
                                    lotFieldMap.put(key, items);
                                } else {
                                    iter.remove();
                                }
                            }
                        }

                        List<HkLotDocument> hkLotDocuments = stockService.retrieveLotsOrPacketsByCriteria(lotFieldMap, invoiceIds, parcelIds, null, franchise, Boolean.FALSE, Boolean.FALSE);
                        if (!CollectionUtils.isEmpty(hkLotDocuments)) {
                            finalRetrievedLotDocuments.addAll(hkLotDocuments);
                        }
                    }
                    if (!CollectionUtils.isEmpty(finalRetrievedLotDocuments)) {
                        for (HkLotDocument hkLotDocument : finalRetrievedLotDocuments) {
                            if (!hkLotDocument.isHasPacket()) {
                                //---Lots with has packet as false.-----
                                lotDocumentsWithoutPackets.add(hkLotDocument);
                                lotDocuments.add(hkLotDocument);
                            } else {
                                //---Lots with has packet as true..------
                                lotDocumentsWithPackets.add(hkLotDocument);
                            }
                        }
                    }
                    //---Fetching lotIds from lot documents containing packets.(has packet=true)
                    if (!CollectionUtils.isEmpty(lotDocumentsWithPackets)) {
                        lotIdsWithPacket = new ArrayList<>();
                        for (HkLotDocument lotDocument : lotDocumentsWithPackets) {
                            lotIdsWithPacket.add(lotDocument.getId());
                        }
                    }
                    if (!CollectionUtils.isEmpty(lotDocumentsWithoutPackets)) {
                        lotIdsWithoutPacket = new ArrayList<>();
                        for (HkLotDocument lotDocument : lotDocumentsWithoutPackets) {
                            lotIdsWithoutPacket.add(lotDocument.getId());
                        }
                    }

                    //---Retrieveing Packets from the packet field value and the associated lots.----
                    if (!CollectionUtils.isEmpty(packetFieldMap) || !CollectionUtils.isEmpty(invoiceIds) || !CollectionUtils.isEmpty(parcelIds)) {
                        Iterator<Map.Entry<String, Object>> iterPackt = packetFieldMap.entrySet().iterator();
                        while (iterPackt.hasNext()) {
                            Map.Entry<String, Object> entry = iterPackt.next();
                            String key = entry.getKey();
                            Object value = entry.getValue();
                            if (key.contains("$MS$") || key.contains("$UMS$") || key.contains("$AG$")) {
                                if (value instanceof String && !StringUtils.isEmpty(value)) {
                                    List<String> items = Arrays.asList(value.toString().split(","));
                                    packetFieldMap.put(key, items);
                                } else if (value instanceof Collection && !CollectionUtils.isEmpty((Collection<?>) value)) {
                                    List<String> items = Arrays.asList(value.toString().split(","));
                                    packetFieldMap.put(key, items);
                                } else {
                                    iterPackt.remove();
                                }
                            }
                        }
                        if (!CollectionUtils.isEmpty(lotIdsWithPacket)) {
                            packetDocuments.addAll(stockService.retrieveLotsOrPacketsByCriteria(packetFieldMap, invoiceIds, parcelIds, lotIdsWithPacket, franchise, Boolean.FALSE, Boolean.TRUE));
                        }
                    }
                }
                List<ObjectId> packetIds = null;
                List<HkSellDocument> hkSellDocuments = new ArrayList<>();
                //-----Fetching the parcel IDs from the parcel documents for retrieving lots/packets.
                if (!CollectionUtils.isEmpty(packetDocuments)) {
                    packetIds = new ArrayList<>();
                    for (HkPacketDocument hkPacketDocument : packetDocuments) {
                        packetIds.add(hkPacketDocument.getId());
                    }
                }
                //---Retrieveing Packets from the packet field value and the associated lots.----
                if (!CollectionUtils.isEmpty(sellFieldMap) || !CollectionUtils.isEmpty(invoiceIds) || !CollectionUtils.isEmpty(parcelIds)) {
                    Iterator<Map.Entry<String, Object>> iterPackt = sellFieldMap.entrySet().iterator();
                    while (iterPackt.hasNext()) {
                        Map.Entry<String, Object> entry = iterPackt.next();
                        String key = entry.getKey();
                        Object value = entry.getValue();
                        if (key.contains("$MS$") || key.contains("$UMS$") || key.contains("$AG$")) {
                            if (value instanceof String && !StringUtils.isEmpty(value)) {
                                List<String> items = Arrays.asList(value.toString().split(","));
                                sellFieldMap.put(key, items);
                            } else if (value instanceof Collection && !CollectionUtils.isEmpty((Collection<?>) value)) {
                                List<String> items = Arrays.asList(value.toString().split(","));
                                sellFieldMap.put(key, items);
                            } else {
                                iterPackt.remove();
                            }
                        }
                    }
                    if (!CollectionUtils.isEmpty(packetIds) && !CollectionUtils.isEmpty(lotIdsWithPacket)) {
                        hkSellDocuments.addAll(stockService.retrieveSellDocuments(sellFieldMap, parcelIds, null, null));
                    } else {
                        hkSellDocuments.addAll(stockService.retrieveSellDocuments(sellFieldMap, parcelIds, null, null));
                    }
                }
//                if (!CollectionUtils.isEmpty(hkSellDocuments)) {
//                    selectItems = new ArrayList<>();
//                    invoiceIds = new ArrayList<>();
//                    parcelIds = new ArrayList<>();
//                    lotIds = new ArrayList<>();
//                    packetIds = new ArrayList<>();
//                    //------Retreiving invoice and parcels associated with packets as packet document contains invoice id and parcel id.----
//                    List<HkInvoiceDocument> hkInvoiceDocuments = stockService.retrieveInvoices(invoiceIds, franchise, Boolean.FALSE);
//                    List<HkParcelDocument> hkParcelDocuments = stockService.retrieveParcels(parcelIds, franchise, Boolean.FALSE);
//                    List<HkLotDocument> hklotDocuments = stockService.retrieveLotsByIds(lotIds, franchise, Boolean.FALSE, null, null, Boolean.TRUE);
//                    List<HkPacketDocument> hkPacketDocuments = stockService.retrievePacketsByIds(packetIds, franchise, Boolean.FALSE, null, Boolean.TRUE);
//                    if (!CollectionUtils.isEmpty(hkInvoiceDocuments) && !CollectionUtils.isEmpty(hkParcelDocuments) && (!CollectionUtils.isEmpty(hklotDocuments) || !CollectionUtils.isEmpty(hkPacketDocuments))) {
//                        for (HkSellDocument sellDocument : hkSellDocuments) {
//                            Map<String, Object> map = new HashMap<>();
//                            SelectItem selectItem = null;
//                            if (sellDocument.getPacket() != null) {
//                                selectItem = new SelectItem(sellDocument.getLot().toString(), sellDocument.getParcel().toString(), sellDocument.getInvoice().toString(), sellDocument.getPacket().toString());
//                            } else {
//                                selectItem = new SelectItem(sellDocument.getLot().toString(), sellDocument.getParcel().toString(), sellDocument.getInvoice().toString(), null);
//                            }
//                            selectItem.setSellId(sellDocument.getId().toString());
//                            List uiFieldListForSell = new ArrayList<>();
//                            if (sellDocument.getFieldValue() != null) {
//                                Map<String, Object> map1 = sellDocument.getFieldValue().toMap();
//                                if (!CollectionUtils.isEmpty(map1)) {
//                                    if (map1.containsKey(HkSystemConstantUtil.AutoNumber.SELL_ID) && map1.get(HkSystemConstantUtil.AutoNumber.SELL_ID) != null) {
//                                        selectItem.setCommonId(map1.get(HkSystemConstantUtil.AutoNumber.SELL_ID).toString());
//                                    }
//                                    for (Map.Entry<String, Object> entrySet : map1.entrySet()) {
//                                        String key = entrySet.getKey();
//                                        Object value = entrySet.getValue();
//                                        uiFieldListForSell.add(key);
//                                    }
//                                }
//                            }
//
//                            Map<String, String> uiFieldListWithComponentTypeForSell = customFieldService.retrieveUIFieldNameWithComponentTypes(uiFieldListForSell);
//                            HkSellDocument oldsellDocument = sellDocument;
//                            Map<String, Object> customMapForSell = sellDocument.getFieldValue().toMap();
//
//                            if (!CollectionUtils.isEmpty(uiFieldListWithComponentTypeForSell)) {
//                                for (Map.Entry<String, String> field : uiFieldListWithComponentTypeForSell.entrySet()) {
//                                    if (field.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN) || field.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.USER_MULTISELECT) || field.getValue().equals(pointerComponentType)) {
//                                        if (customMapForSell != null && !customMapForSell.isEmpty() && customMapForSell.containsKey(field.getKey())) {
//                                            String value = customMapForSell.get(field.getKey()).toString().replaceAll("\\[", "").replaceAll("\\]", "");
//                                            customMapForSell.put(field.getKey(), value);
//                                        }
//                                    }
//                                }
//                                oldsellDocument.getFieldValue().putAll(customMapForSell);
//                            }
//
//                            if (!CollectionUtils.isEmpty(sellDbFieldName)) {
//                                for (String model : sellDbFieldName) {
//                                    if (oldsellDocument.getFieldValue().toMap().containsKey(model)) {
//                                        map.put(model, oldsellDocument.getFieldValue().toMap().get(model));
//                                    }
//                                }
//                            }
//
//                            if (!CollectionUtils.isEmpty(packetDbFieldName) && !CollectionUtils.isEmpty(hkPacketDocuments)) {
//                                for (HkPacketDocument hkPacketDocument : hkPacketDocuments) {
//
//                                    List uiFieldListForPacket = new ArrayList<>();
//                                    if (hkPacketDocument.getFieldValue() != null) {
//                                        Map<String, Object> map1 = hkPacketDocument.getFieldValue().toMap();
//                                        if (!CollectionUtils.isEmpty(map1)) {
//                                            for (Map.Entry<String, Object> entrySet : map1.entrySet()) {
//                                                String key = entrySet.getKey();
//                                                Object value = entrySet.getValue();
//                                                uiFieldListForPacket.add(key);
//                                            }
//                                        }
//                                    }
//
//                                    Map<String, String> uiFieldListWithComponentTypeForPacket = customFieldService.retrieveUIFieldNameWithComponentTypes(uiFieldListForPacket);
//                                    HkPacketDocument oldPacketDocument = hkPacketDocument;
//                                    Map<String, Object> customMap = hkPacketDocument.getFieldValue().toMap();
//
//                                    if (!CollectionUtils.isEmpty(uiFieldListWithComponentTypeForPacket)) {
//                                        for (Map.Entry<String, String> field : uiFieldListWithComponentTypeForPacket.entrySet()) {
//                                            if (field.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN) || field.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.USER_MULTISELECT) || field.getValue().equals(pointerComponentType)) {
//                                                if (customMap != null && !customMap.isEmpty() && customMap.containsKey(field.getKey())) {
//                                                    String value = customMap.get(field.getKey()).toString().replaceAll("\\[", "").replaceAll("\\]", "");
//                                                    customMap.put(field.getKey(), value);
//                                                }
//                                            }
//                                        }
//                                        oldPacketDocument.getFieldValue().putAll(customMap);
//                                    }
//
//                                    if (oldPacketDocument.getId().toString().equals(sellDocument.getPacket().toString())) {
//                                        for (String model : sellDbFieldName) {
//                                            if (oldPacketDocument.getFieldValue().toMap().containsKey(model)) {
//                                                map.put(model, oldPacketDocument.getFieldValue().toMap().get(model));
//                                            }
//                                        }
//                                        break;
//                                    }
//                                }
//                            }
//
//                            if (!CollectionUtils.isEmpty(invoiceDbFieldName)) {
//                                for (HkInvoiceDocument hkInvoiceDocument : hkInvoiceDocuments) {
//
//                                    List uiFieldListForInvoice = new ArrayList<>();
//                                    if (hkInvoiceDocument.getFieldValue() != null) {
//                                        Map<String, Object> map1 = hkInvoiceDocument.getFieldValue().toMap();
//                                        if (!CollectionUtils.isEmpty(map1)) {
//                                            for (Map.Entry<String, Object> entrySet : map1.entrySet()) {
//                                                String key = entrySet.getKey();
//                                                Object value = entrySet.getValue();
//                                                uiFieldListForInvoice.add(key);
//                                            }
//                                        }
//                                    }
//
//                                    Map<String, String> uiFieldListWithComponentTypeForInvoice = customFieldService.retrieveUIFieldNameWithComponentTypes(uiFieldListForInvoice);
//                                    HkInvoiceDocument oldInvoiceDocument = hkInvoiceDocument;
//                                    Map<String, Object> customMap = hkInvoiceDocument.getFieldValue().toMap();
//
//                                    if (!CollectionUtils.isEmpty(uiFieldListWithComponentTypeForInvoice)) {
//                                        for (Map.Entry<String, String> field : uiFieldListWithComponentTypeForInvoice.entrySet()) {
//                                            if (field.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN) || field.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.USER_MULTISELECT) || field.getValue().equals(pointerComponentType)) {
//                                                if (customMap != null && !customMap.isEmpty() && customMap.containsKey(field.getKey())) {
//                                                    String value = customMap.get(field.getKey()).toString().replaceAll("\\[", "").replaceAll("\\]", "");
//                                                    customMap.put(field.getKey(), value);
//                                                }
//                                            }
//                                        }
//                                        oldInvoiceDocument.getFieldValue().putAll(customMap);
//                                    }
//
//                                    if (oldInvoiceDocument.getId().toString().equals(sellDocument.getInvoice().toString())) {
//                                        for (String model : invoiceDbFieldName) {
//                                            if (oldInvoiceDocument.getFieldValue().toMap().containsKey(model)) {
//                                                map.put(model, oldInvoiceDocument.getFieldValue().toMap().get(model));
//                                            }
//                                        }
//                                        break;
//                                    }
//                                }
//                            }
//                            if (!CollectionUtils.isEmpty(parcelDbFieldName)) {
//                                for (HkParcelDocument hkParcelDocument : hkParcelDocuments) {
//
//                                    //----Converting parcel field values retrieved from mongo db of type e.g- multiselect to string------
//                                    List uiFieldListForParcel = new ArrayList<>();
//                                    if (hkParcelDocument.getFieldValue() != null) {
//                                        Map<String, Object> map1 = hkParcelDocument.getFieldValue().toMap();
//                                        if (!CollectionUtils.isEmpty(map1)) {
//                                            for (Map.Entry<String, Object> entrySet : map1.entrySet()) {
//                                                String key = entrySet.getKey();
//                                                Object value = entrySet.getValue();
//                                                uiFieldListForParcel.add(key);
//                                            }
//                                        }
//                                    }
//
//                                    Map<String, String> uiFieldListWithComponentTypeForParcel = customFieldService.retrieveUIFieldNameWithComponentTypes(uiFieldListForParcel);
//                                    HkParcelDocument oldparcelDocument = hkParcelDocument;
//                                    Map<String, Object> customMap = hkParcelDocument.getFieldValue().toMap();
//
//                                    //-----Changing the values to its orginal values like "3:E" as Piyush Sonani and replacing back in field values---
//                                    if (!CollectionUtils.isEmpty(uiFieldListWithComponentTypeForParcel)) {
//                                        for (Map.Entry<String, String> field : uiFieldListWithComponentTypeForParcel.entrySet()) {
//                                            if (field.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN) || field.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.USER_MULTISELECT) || field.getValue().equals(pointerComponentType)) {
//                                                if (customMap != null && !customMap.isEmpty() && customMap.containsKey(field.getKey())) {
//                                                    String value = customMap.get(field.getKey()).toString().replaceAll("\\[", "").replaceAll("\\]", "");
//                                                    customMap.put(field.getKey(), value);
//                                                }
//                                            }
//                                        }
//                                        oldparcelDocument.getFieldValue().putAll(customMap);
//                                    }
//
//                                    //-----placing the values retreieved from the db in category custom map which are configured for search field on UI.
//                                    if (oldparcelDocument.getId().toString().equals(sellDocument.getParcel().toString())) {
//                                        for (String model : parcelDbFieldName) {
//                                            if (oldparcelDocument.getFieldValue().toMap().containsKey(model)) {
//                                                map.put(model, oldparcelDocument.getFieldValue().toMap().get(model));
//                                            }
//                                        }
//                                        break;
//                                    }
//                                }
//                            }
//                            if (!CollectionUtils.isEmpty(lotDbFieldName)) {
//                                for (HkLotDocument hkLotDocument : hklotDocuments) {
//
//                                    //----Converting parcel field values retrieved from mongo db of type e.g- multiselect to string------
//                                    List uiFieldListForLot = new ArrayList<>();
//                                    if (hkLotDocument.getFieldValue() != null) {
//                                        Map<String, Object> map1 = hkLotDocument.getFieldValue().toMap();
//                                        if (!CollectionUtils.isEmpty(map1)) {
//                                            for (Map.Entry<String, Object> entrySet : map1.entrySet()) {
//                                                String key = entrySet.getKey();
//                                                Object value = entrySet.getValue();
//                                                uiFieldListForLot.add(key);
//                                            }
//                                        }
//                                    }
//                                    Map<String, String> uiFieldListWithComponentTypeForLot = customFieldService.retrieveUIFieldNameWithComponentTypes(uiFieldListForLot);
//                                    HkLotDocument oldlotDocument = hkLotDocument;
//                                    Map<String, Object> customMapforLot = hkLotDocument.getFieldValue().toMap();
//
//                                    //-----Changing the values to its orginal values like "3:E" as Piyush Sonani and replacing back in field values---
//                                    if (!CollectionUtils.isEmpty(uiFieldListWithComponentTypeForLot)) {
//                                        for (Map.Entry<String, String> field : uiFieldListWithComponentTypeForLot.entrySet()) {
//                                            if (field.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN) || field.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.USER_MULTISELECT) || field.getValue().equals(pointerComponentType)) {
//                                                if (customMapforLot != null && !customMapforLot.isEmpty() && customMapforLot.containsKey(field.getKey())) {
//                                                    String value = customMapforLot.get(field.getKey()).toString().replaceAll("\\[", "").replaceAll("\\]", "");
//                                                    customMapforLot.put(field.getKey(), value);
//                                                }
//                                            }
//                                        }
//                                        oldlotDocument.getFieldValue().putAll(customMapforLot);
//                                    }
//
//                                    //-----placing the values retreieved from the db in category custom map which are configured for search field on UI.
//                                    if (oldlotDocument.getId().toString().equals(sellDocument.getLot().toString())) {
//                                        for (String model : lotDbFieldName) {
//                                            if (oldlotDocument.getFieldValue().toMap().containsKey(model)) {
//                                                map.put(model, oldlotDocument.getFieldValue().toMap().get(model));
//                                            }
//                                        }
//                                        break;
//                                    }
//                                }
//                            }
//                            selectItem.setCategoryCustom(map);
//                            selectItem.setStatus(sellDocument.getStatus());
//                            selectItems.add(selectItem);
//                        }
//                        result.addAll(selectItems);
//                    }
//                }
            } else if (selectedField.equalsIgnoreCase(HkSystemConstantUtil.PRINT_BARCODE_FIELD_TYPE.TRANSFER)) {
                List<HkInvoiceDocument> invoiceDocuments = null;
                if (!CollectionUtils.isEmpty(invoiceFieldMap)) {
                    Iterator<Map.Entry<String, Object>> iter = invoiceFieldMap.entrySet().iterator();
                    while (iter.hasNext()) {
                        Map.Entry<String, Object> entry = iter.next();
                        String key = entry.getKey();
                        Object value = entry.getValue();
                        if (key.contains("$MS$") || key.contains("$UMS$") || key.contains("$AG$")) {
                            if (value instanceof String && !StringUtils.isEmpty(value)) {
                                List<String> items = Arrays.asList(value.toString().split(","));
                                invoiceFieldMap.put(key, items);
                            } else if (value instanceof Collection && !CollectionUtils.isEmpty((Collection<?>) value)) {
                                List<String> items = Arrays.asList(value.toString().split(","));
                                invoiceFieldMap.put(key, items);
                            } else {
                                iter.remove();
                            }
                        }
                    }
                    UMFeatureDetailDataBean feature = applicationUtil.getuMFeatureDetailDataBeanMap().get(HkSystemConstantUtil.Feature.INVOICE);
                    if (feature != null) {
                        invoiceDocuments = stockService.retrieveInvoices(invoiceFieldMap, franchise, Boolean.FALSE, null, null,null,null);
                    }
                }
                Set<HkPacketDocument> packetDocuments = new HashSet<>();
                List<ObjectId> invoiceIds = null;
                if (!CollectionUtils.isEmpty(invoiceDocuments)) {
                    invoiceIds = new ArrayList<>();
                    for (HkInvoiceDocument invoiceDocument : invoiceDocuments) {
                        invoiceIds.add(invoiceDocument.getId());
                    }
                }
                List<HkParcelDocument> parcelDocuments = null;
                if (!CollectionUtils.isEmpty(parcelFieldMap) || !CollectionUtils.isEmpty(invoiceIds)) {
                    Iterator<Map.Entry<String, Object>> iter = parcelFieldMap.entrySet().iterator();
                    while (iter.hasNext()) {
                        Map.Entry<String, Object> entry = iter.next();
                        String key = entry.getKey();
                        Object value = entry.getValue();
                        if (key.contains("$MS$") || key.contains("$UMS$") || key.contains("$AG$")) {
                            if (value instanceof String && !StringUtils.isEmpty(value)) {
                                List<String> items = Arrays.asList(value.toString().split(","));
                                parcelFieldMap.put(key, items);
                            } else if (value instanceof Collection && !CollectionUtils.isEmpty((Collection<?>) value)) {
                                List<String> items = Arrays.asList(value.toString().split(","));
                                parcelFieldMap.put(key, items);
                            } else {
                                iter.remove();
                            }
                        }
                    }
                    UMFeatureDetailDataBean feature = applicationUtil.getuMFeatureDetailDataBeanMap().get(HkSystemConstantUtil.Feature.PARCEL);
                    if (feature != null) {
                        parcelDocuments = stockService.retrieveParcels(parcelFieldMap, invoiceIds, null, franchise, Boolean.FALSE, null, HkSystemConstantUtil.StockStatus.NEW_ROUGH,null,null);
                    }
                }
                List<ObjectId> parcelIds = null;
                //-----Fetching the parcel IDs from the parcel documents for retrieving lots/packets.
                if (!CollectionUtils.isEmpty(parcelDocuments)) {
                    parcelIds = new ArrayList<>();
                    for (HkParcelDocument parcelDocument : parcelDocuments) {
                        parcelIds.add(parcelDocument.getId());
                    }
                }
                Set<HkLotDocument> lotDocuments = new HashSet<>();
                List<ObjectId> lotIds = new ArrayList<>();
                List<ObjectId> lotIdsWithPacket = new ArrayList<>();
                List<ObjectId> lotIdsWithoutPacket = new ArrayList<>();
                //----Sending invoiceIds,parcelIds retrieved from the documents from the search field above,lot field values or parcel field values any of the one exists-------
                if (!CollectionUtils.isEmpty(invoiceIds) || !CollectionUtils.isEmpty(parcelIds) || !CollectionUtils.isEmpty(lotFieldMap) || !CollectionUtils.isEmpty(packetFieldMap)) {
                    Set<HkLotDocument> lotDocumentsWithPackets = new HashSet<>();
                    Set<HkLotDocument> lotDocumentsWithoutPackets = new HashSet<>();
                    Set<HkLotDocument> finalRetrievedLotDocuments = new HashSet<>();
                    if (!CollectionUtils.isEmpty(lotFieldMap) || !CollectionUtils.isEmpty(invoiceIds) || !CollectionUtils.isEmpty(parcelIds)) {
                        Iterator<Map.Entry<String, Object>> iter = lotFieldMap.entrySet().iterator();
                        while (iter.hasNext()) {
                            Map.Entry<String, Object> entry = iter.next();
                            String key = entry.getKey();
                            Object value = entry.getValue();
                            if (key.contains("$MS$") || key.contains("$UMS$") || key.contains("$AG$")) {
                                if (value instanceof String && !StringUtils.isEmpty(value)) {
                                    List<String> items = Arrays.asList(value.toString().split(","));
                                    lotFieldMap.put(key, items);
                                } else if (value instanceof Collection && !CollectionUtils.isEmpty((Collection<?>) value)) {
                                    List<String> items = Arrays.asList(value.toString().split(","));
                                    lotFieldMap.put(key, items);
                                } else {
                                    iter.remove();
                                }
                            }
                        }

                        List<HkLotDocument> hkLotDocuments = stockService.retrieveLotsOrPacketsByCriteria(lotFieldMap, invoiceIds, parcelIds, null, franchise, Boolean.FALSE, Boolean.FALSE);
                        if (!CollectionUtils.isEmpty(hkLotDocuments)) {
                            finalRetrievedLotDocuments.addAll(hkLotDocuments);
                        }
                    }
                    if (!CollectionUtils.isEmpty(finalRetrievedLotDocuments)) {
                        for (HkLotDocument hkLotDocument : finalRetrievedLotDocuments) {
                            if (!hkLotDocument.isHasPacket()) {
                                //---Lots with has packet as false.-----
                                lotDocumentsWithoutPackets.add(hkLotDocument);
                                lotDocuments.add(hkLotDocument);
                            } else {
                                //---Lots with has packet as true..------
                                lotDocumentsWithPackets.add(hkLotDocument);
                            }
                        }
                    }
                    //---Fetching lotIds from lot documents containing packets.(has packet=true)
                    if (!CollectionUtils.isEmpty(lotDocumentsWithPackets)) {
                        lotIdsWithPacket = new ArrayList<>();
                        for (HkLotDocument lotDocument : lotDocumentsWithPackets) {
                            lotIdsWithPacket.add(lotDocument.getId());
                        }
                    }
                    if (!CollectionUtils.isEmpty(lotDocumentsWithoutPackets)) {
                        lotIdsWithoutPacket = new ArrayList<>();
                        for (HkLotDocument lotDocument : lotDocumentsWithoutPackets) {
                            lotIdsWithoutPacket.add(lotDocument.getId());
                        }
                    }

                    //---Retrieveing Packets from the packet field value and the associated lots.----
                    if (!CollectionUtils.isEmpty(packetFieldMap) || !CollectionUtils.isEmpty(invoiceIds) || !CollectionUtils.isEmpty(parcelIds) || !CollectionUtils.isEmpty(lotIdsWithPacket)) {
                        Iterator<Map.Entry<String, Object>> iterPackt = packetFieldMap.entrySet().iterator();
                        while (iterPackt.hasNext()) {
                            Map.Entry<String, Object> entry = iterPackt.next();
                            String key = entry.getKey();
                            Object value = entry.getValue();
                            if (key.contains("$MS$") || key.contains("$UMS$") || key.contains("$AG$")) {
                                if (value instanceof String && !StringUtils.isEmpty(value)) {
                                    List<String> items = Arrays.asList(value.toString().split(","));
                                    packetFieldMap.put(key, items);
                                } else if (value instanceof Collection && !CollectionUtils.isEmpty((Collection<?>) value)) {
                                    List<String> items = Arrays.asList(value.toString().split(","));
                                    packetFieldMap.put(key, items);
                                } else {
                                    iterPackt.remove();
                                }
                            }
                        }
                        packetDocuments.addAll(stockService.retrieveLotsOrPacketsByCriteria(packetFieldMap, invoiceIds, parcelIds, lotIdsWithPacket, franchise, Boolean.FALSE, Boolean.TRUE));
                    }
                }
                List<ObjectId> packetIds = null;
                List<HkTransferDocument> hkTransferDocuments = new ArrayList<>();
                //-----Fetching the parcel IDs from the parcel documents for retrieving lots/packets.
                if (!CollectionUtils.isEmpty(packetDocuments)) {
                    packetIds = new ArrayList<>();
                    for (HkPacketDocument hkPacketDocument : packetDocuments) {
                        packetIds.add(hkPacketDocument.getId());
                    }
                }
                //---Retrieveing Packets from the packet field value and the associated lots.----
                if (!CollectionUtils.isEmpty(transferFieldMap) || !CollectionUtils.isEmpty(invoiceIds) || !CollectionUtils.isEmpty(parcelIds)) {
                    Iterator<Map.Entry<String, Object>> iterPackt = sellFieldMap.entrySet().iterator();
                    while (iterPackt.hasNext()) {
                        Map.Entry<String, Object> entry = iterPackt.next();
                        String key = entry.getKey();
                        Object value = entry.getValue();
                        if (key.contains("$MS$") || key.contains("$UMS$") || key.contains("$AG$")) {
                            if (value instanceof String && !StringUtils.isEmpty(value)) {
                                List<String> items = Arrays.asList(value.toString().split(","));
                                transferFieldMap.put(key, items);
                            } else if (value instanceof Collection && !CollectionUtils.isEmpty((Collection<?>) value)) {
                                List<String> items = Arrays.asList(value.toString().split(","));
                                transferFieldMap.put(key, items);
                            } else {
                                iterPackt.remove();
                            }
                        }
                    }
                    if (!CollectionUtils.isEmpty(packetIds) && !CollectionUtils.isEmpty(lotIdsWithPacket)) {
                        hkTransferDocuments.addAll(stockService.retrieveTransferDocuments(transferFieldMap, invoiceIds, parcelIds, lotIdsWithPacket, packetIds));
                    } else {
                        hkTransferDocuments.addAll(stockService.retrieveTransferDocuments(transferFieldMap, invoiceIds, parcelIds, lotIdsWithoutPacket, null));
                    }

                }
                if (!CollectionUtils.isEmpty(hkTransferDocuments)) {
                    selectItems = new ArrayList<>();
                    invoiceIds = new ArrayList<>();
                    parcelIds = new ArrayList<>();
                    lotIds = new ArrayList<>();
                    packetIds = new ArrayList<>();
                    for (HkTransferDocument hkTransferDocument : hkTransferDocuments) {
                        invoiceIds.add(hkTransferDocument.getInvoice());
                        parcelIds.add(hkTransferDocument.getParcel());
                        lotIds.add(hkTransferDocument.getLot());
                        packetIds.add(hkTransferDocument.getPacket());
                    }
                    //------Retreiving invoice and parcels associated with packets as packet document contains invoice id and parcel id.----
                    List<HkInvoiceDocument> hkInvoiceDocuments = stockService.retrieveInvoices(invoiceIds, franchise, Boolean.FALSE);
                    List<HkParcelDocument> hkParcelDocuments = stockService.retrieveParcels(parcelIds, franchise, Boolean.FALSE);
                    List<HkLotDocument> hklotDocuments = stockService.retrieveLotsByIds(lotIds, franchise, Boolean.FALSE, null, null, Boolean.TRUE);
                    List<HkPacketDocument> hkPacketDocuments = stockService.retrievePacketsByIds(packetIds, franchise, Boolean.FALSE, null, Boolean.TRUE);
                    if (!CollectionUtils.isEmpty(hkInvoiceDocuments) && !CollectionUtils.isEmpty(hkParcelDocuments) && (!CollectionUtils.isEmpty(hklotDocuments) || !CollectionUtils.isEmpty(hkPacketDocuments))) {
                        for (HkTransferDocument transferDocument : hkTransferDocuments) {
                            Map<String, Object> map = new HashMap<>();
                            SelectItem selectItem = null;
                            if (transferDocument.getPacket() != null) {
                                selectItem = new SelectItem(transferDocument.getLot().toString(), transferDocument.getParcel().toString(), transferDocument.getInvoice().toString(), transferDocument.getPacket().toString());
                            } else {
                                selectItem = new SelectItem(transferDocument.getLot().toString(), transferDocument.getParcel().toString(), transferDocument.getInvoice().toString(), null);
                            }
                            selectItem.setTransferId(transferDocument.getId().toString());

                            List uiFieldListForTransfer = new ArrayList<>();
                            if (transferDocument.getFieldValue() != null) {
                                Map<String, Object> map1 = transferDocument.getFieldValue().toMap();
                                if (!CollectionUtils.isEmpty(map1)) {
                                    if (map1.containsKey(HkSystemConstantUtil.AutoNumber.TRANSFER_ID) && map1.get(HkSystemConstantUtil.AutoNumber.TRANSFER_ID) != null) {
                                        selectItem.setCommonId(map1.get(HkSystemConstantUtil.AutoNumber.TRANSFER_ID).toString());
                                    }
                                    for (Map.Entry<String, Object> entrySet : map1.entrySet()) {
                                        String key = entrySet.getKey();
                                        Object value = entrySet.getValue();
                                        uiFieldListForTransfer.add(key);
                                    }
                                }
                            }

                            Map<String, String> uiFieldListWithComponentTypeForTransfer = customFieldService.retrieveUIFieldNameWithComponentTypes(uiFieldListForTransfer);
                            HkTransferDocument oldtransferDocument = transferDocument;
                            Map<String, Object> customMapForTransfer = transferDocument.getFieldValue().toMap();

                            if (!CollectionUtils.isEmpty(uiFieldListWithComponentTypeForTransfer)) {
                                for (Map.Entry<String, String> field : uiFieldListWithComponentTypeForTransfer.entrySet()) {
                                    if (field.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN) || field.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.USER_MULTISELECT) || field.getValue().equals(pointerComponentType)) {
                                        if (customMapForTransfer != null && !customMapForTransfer.isEmpty() && customMapForTransfer.containsKey(field.getKey())) {
                                            String value = customMapForTransfer.get(field.getKey()).toString().replaceAll("\\[", "").replaceAll("\\]", "");
                                            customMapForTransfer.put(field.getKey(), value);
                                        }
                                    }
                                }
                                oldtransferDocument.getFieldValue().putAll(customMapForTransfer);
                            }

                            if (!CollectionUtils.isEmpty(transferDbFieldName)) {
                                for (String model : transferDbFieldName) {
                                    if (oldtransferDocument.getFieldValue().toMap().containsKey(model)) {
                                        map.put(model, oldtransferDocument.getFieldValue().toMap().get(model));
                                    }
                                }
                            }

                            if (!CollectionUtils.isEmpty(packetDbFieldName) && !CollectionUtils.isEmpty(hkPacketDocuments)) {
                                for (HkPacketDocument hkPacketDocument : hkPacketDocuments) {

                                    List uiFieldListForPacket = new ArrayList<>();
                                    if (hkPacketDocument.getFieldValue() != null) {
                                        Map<String, Object> map1 = hkPacketDocument.getFieldValue().toMap();
                                        if (!CollectionUtils.isEmpty(map1)) {
                                            for (Map.Entry<String, Object> entrySet : map1.entrySet()) {
                                                String key = entrySet.getKey();
                                                Object value = entrySet.getValue();
                                                uiFieldListForPacket.add(key);
                                            }
                                        }
                                    }

                                    Map<String, String> uiFieldListWithComponentTypeForPacket = customFieldService.retrieveUIFieldNameWithComponentTypes(uiFieldListForPacket);
                                    HkPacketDocument oldPacketDocument = hkPacketDocument;
                                    Map<String, Object> customMap = hkPacketDocument.getFieldValue().toMap();

                                    if (!CollectionUtils.isEmpty(uiFieldListWithComponentTypeForPacket)) {
                                        for (Map.Entry<String, String> field : uiFieldListWithComponentTypeForPacket.entrySet()) {
                                            if (field.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN) || field.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.USER_MULTISELECT) || field.getValue().equals(pointerComponentType)) {
                                                if (customMap != null && !customMap.isEmpty() && customMap.containsKey(field.getKey())) {
                                                    String value = customMap.get(field.getKey()).toString().replaceAll("\\[", "").replaceAll("\\]", "");
                                                    customMap.put(field.getKey(), value);
                                                }
                                            }
                                        }
                                        oldPacketDocument.getFieldValue().putAll(customMap);
                                    }

                                    if (oldPacketDocument.getId().toString().equals(transferDocument.getPacket().toString())) {
                                        for (String model : sellDbFieldName) {
                                            if (oldPacketDocument.getFieldValue().toMap().containsKey(model)) {
                                                map.put(model, oldPacketDocument.getFieldValue().toMap().get(model));
                                            }
                                        }
                                        break;
                                    }
                                }
                            }

                            if (!CollectionUtils.isEmpty(invoiceDbFieldName)) {
                                for (HkInvoiceDocument hkInvoiceDocument : hkInvoiceDocuments) {

                                    List uiFieldListForInvoice = new ArrayList<>();
                                    if (hkInvoiceDocument.getFieldValue() != null) {
                                        Map<String, Object> map1 = hkInvoiceDocument.getFieldValue().toMap();
                                        if (!CollectionUtils.isEmpty(map1)) {
                                            for (Map.Entry<String, Object> entrySet : map1.entrySet()) {
                                                String key = entrySet.getKey();
                                                Object value = entrySet.getValue();
                                                uiFieldListForInvoice.add(key);
                                            }
                                        }
                                    }

                                    Map<String, String> uiFieldListWithComponentTypeForInvoice = customFieldService.retrieveUIFieldNameWithComponentTypes(uiFieldListForInvoice);
                                    HkInvoiceDocument oldInvoiceDocument = hkInvoiceDocument;
                                    Map<String, Object> customMap = hkInvoiceDocument.getFieldValue().toMap();

                                    if (!CollectionUtils.isEmpty(uiFieldListWithComponentTypeForInvoice)) {
                                        for (Map.Entry<String, String> field : uiFieldListWithComponentTypeForInvoice.entrySet()) {
                                            if (field.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN) || field.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.USER_MULTISELECT) || field.getValue().equals(pointerComponentType)) {
                                                if (customMap != null && !customMap.isEmpty() && customMap.containsKey(field.getKey())) {
                                                    String value = customMap.get(field.getKey()).toString().replaceAll("\\[", "").replaceAll("\\]", "");
                                                    customMap.put(field.getKey(), value);
                                                }
                                            }
                                        }
                                        oldInvoiceDocument.getFieldValue().putAll(customMap);
                                    }

                                    if (oldInvoiceDocument.getId().toString().equals(transferDocument.getInvoice().toString())) {
                                        for (String model : invoiceDbFieldName) {
                                            if (oldInvoiceDocument.getFieldValue().toMap().containsKey(model)) {
                                                map.put(model, oldInvoiceDocument.getFieldValue().toMap().get(model));
                                            }
                                        }
                                        break;
                                    }
                                }
                            }
                            if (!CollectionUtils.isEmpty(parcelDbFieldName)) {
                                for (HkParcelDocument hkParcelDocument : hkParcelDocuments) {

                                    //----Converting parcel field values retrieved from mongo db of type e.g- multiselect to string------
                                    List uiFieldListForParcel = new ArrayList<>();
                                    if (hkParcelDocument.getFieldValue() != null) {
                                        Map<String, Object> map1 = hkParcelDocument.getFieldValue().toMap();
                                        if (!CollectionUtils.isEmpty(map1)) {
                                            for (Map.Entry<String, Object> entrySet : map1.entrySet()) {
                                                String key = entrySet.getKey();
                                                Object value = entrySet.getValue();
                                                uiFieldListForParcel.add(key);
                                            }
                                        }
                                    }

                                    Map<String, String> uiFieldListWithComponentTypeForParcel = customFieldService.retrieveUIFieldNameWithComponentTypes(uiFieldListForParcel);
                                    HkParcelDocument oldparcelDocument = hkParcelDocument;
                                    Map<String, Object> customMap = hkParcelDocument.getFieldValue().toMap();

                                    //-----Changing the values to its orginal values like "3:E" as Piyush Sonani and replacing back in field values---
                                    if (!CollectionUtils.isEmpty(uiFieldListWithComponentTypeForParcel)) {
                                        for (Map.Entry<String, String> field : uiFieldListWithComponentTypeForParcel.entrySet()) {
                                            if (field.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN) || field.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.USER_MULTISELECT) || field.getValue().equals(pointerComponentType)) {
                                                if (customMap != null && !customMap.isEmpty() && customMap.containsKey(field.getKey())) {
                                                    String value = customMap.get(field.getKey()).toString().replaceAll("\\[", "").replaceAll("\\]", "");
                                                    customMap.put(field.getKey(), value);
                                                }
                                            }
                                        }
                                        oldparcelDocument.getFieldValue().putAll(customMap);
                                    }

                                    //-----placing the values retreieved from the db in category custom map which are configured for search field on UI.
                                    if (oldparcelDocument.getId().toString().equals(transferDocument.getParcel().toString())) {
                                        for (String model : parcelDbFieldName) {
                                            if (oldparcelDocument.getFieldValue().toMap().containsKey(model)) {
                                                map.put(model, oldparcelDocument.getFieldValue().toMap().get(model));
                                            }
                                        }
                                        break;
                                    }
                                }
                            }
                            if (!CollectionUtils.isEmpty(lotDbFieldName)) {
                                for (HkLotDocument hkLotDocument : hklotDocuments) {

                                    //----Converting parcel field values retrieved from mongo db of type e.g- multiselect to string------
                                    List uiFieldListForLot = new ArrayList<>();
                                    if (hkLotDocument.getFieldValue() != null) {
                                        Map<String, Object> map1 = hkLotDocument.getFieldValue().toMap();
                                        if (!CollectionUtils.isEmpty(map1)) {
                                            for (Map.Entry<String, Object> entrySet : map1.entrySet()) {
                                                String key = entrySet.getKey();
                                                Object value = entrySet.getValue();
                                                uiFieldListForLot.add(key);
                                            }
                                        }
                                    }
                                    Map<String, String> uiFieldListWithComponentTypeForLot = customFieldService.retrieveUIFieldNameWithComponentTypes(uiFieldListForLot);
                                    HkLotDocument oldlotDocument = hkLotDocument;
                                    Map<String, Object> customMapforLot = hkLotDocument.getFieldValue().toMap();

                                    //-----Changing the values to its orginal values like "3:E" as Piyush Sonani and replacing back in field values---
                                    if (!CollectionUtils.isEmpty(uiFieldListWithComponentTypeForLot)) {
                                        for (Map.Entry<String, String> field : uiFieldListWithComponentTypeForLot.entrySet()) {
                                            if (field.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN) || field.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.USER_MULTISELECT) || field.getValue().equals(pointerComponentType)) {
                                                if (customMapforLot != null && !customMapforLot.isEmpty() && customMapforLot.containsKey(field.getKey())) {
                                                    String value = customMapforLot.get(field.getKey()).toString().replaceAll("\\[", "").replaceAll("\\]", "");
                                                    customMapforLot.put(field.getKey(), value);
                                                }
                                            }
                                        }
                                        oldlotDocument.getFieldValue().putAll(customMapforLot);
                                    }

                                    //-----placing the values retreieved from the db in category custom map which are configured for search field on UI.
                                    if (oldlotDocument.getId().toString().equals(transferDocument.getLot().toString())) {
                                        for (String model : lotDbFieldName) {
                                            if (oldlotDocument.getFieldValue().toMap().containsKey(model)) {
                                                map.put(model, oldlotDocument.getFieldValue().toMap().get(model));
                                            }
                                        }
                                        break;
                                    }
                                }
                            }
                            selectItem.setCategoryCustom(map);
                            selectItem.setStatus(transferDocument.getStatus());
                            selectItems.add(selectItem);
                        }
                        result.addAll(selectItems);
                    }
                }

            }
        }
        return result;
    }

    public String prepareBarcode(List<String> barcodes, HttpServletResponse response, HttpServletRequest request) throws IOException, DRException, FileNotFoundException, JRException {
        String result = "F";
        if (!CollectionUtils.isEmpty(barcodes)) {
            Set<String> barcodeValues = new HashSet<>();
            barcodeValues.addAll(barcodes);
            generateBarcodeUtil.createReport("Barcodes", ".pdf", barcodeValues);
            result = "T";
        }
        return result;
    }

    /**
     * Shreya: Generate barcode for Autogenerated Custom Fields
     *
     * @param payload
     * @return path of barcode file
     * @throws IOException
     */
    public String printBarcode(String payload) throws IOException, FileNotFoundException, DRException, JRException {
        String tempFileName = "";
        if (!StringUtils.isEmpty(payload)) {
            tempFileName = FolderManagement.getTempFileName(loginDataBean.getCompanyId(), FolderManagement.FEATURE.COMMON, "Barcode", loginDataBean.getId(), payload + "_barcode");
            Set<String> barcodeValues = new HashSet<>();
            barcodeValues.add(payload);
            generateBarcodeUtil.createReport(tempFileName, ".pdf", barcodeValues);
        }
        return tempFileName;
    }

}
