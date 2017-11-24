/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.center.stock.transformers;

import com.argusoft.generic.database.exception.GenericDatabaseException;
import com.argusoft.hkg.common.constantutil.HkSystemConstantUtil;
import com.argusoft.hkg.nosql.core.HkCustomFieldService;
import com.argusoft.hkg.nosql.core.HkStockService;
import com.argusoft.hkg.nosql.model.HkFeatureFieldPermissionDocument;
import com.argusoft.hkg.nosql.model.HkInvoiceDocument;
import com.argusoft.hkg.nosql.model.HkLotDocument;
import com.argusoft.hkg.nosql.model.HkPacketDocument;
import com.argusoft.hkg.nosql.model.HkParcelDocument;
import com.argusoft.hkg.web.center.stock.databeans.StatusChangeDataBean;
import com.argusoft.hkg.web.center.usermanagement.databeans.LoginDataBean;
import com.argusoft.hkg.web.center.usermanagement.databeans.UMFeatureDetailDataBean;
import com.argusoft.hkg.web.center.util.ApplicationUtil;
import com.argusoft.hkg.web.util.SelectItem;
import com.argusoft.sync.center.model.HkFieldDocument;
import com.argusoft.sync.center.model.HkSectionDocument;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.bson.BasicBSONObject;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 *
 * @author brijesh
 */
@Service
public class ChangeStatusTransformer {

    @Autowired
    HkCustomFieldService hkFieldService;

    @Autowired
    LoginDataBean loginDataBean;

    @Autowired
    HkStockService stockService;

    @Autowired
    ApplicationUtil applicationUtil;

    public List<SelectItem> searchLot(StatusChangeDataBean statusChangeDataBean, List<String> invoiceDbFieldName, List<String> lotDbFieldName, List<String> packetDbFieldName, List<String> parcelDbFieldName) throws GenericDatabaseException {
        //System.out.println("----invoice dbfieldName-----" + invoiceDbFieldName);
        //System.out.println("----parcelDbFieldName dbfieldName-----" + parcelDbFieldName);
        //System.out.println("----lotDbFieldName dbfieldName-----" + lotDbFieldName);
        //System.out.println("----packetDbFieldName dbfieldName-----" + packetDbFieldName);
        /////////////////////////////////////////////////////////////////////
        List<SelectItem> result = new ArrayList<>();
        List<String> statusList = new ArrayList<>();
        if (statusChangeDataBean.getStatus() != null) {
            statusList.add(statusChangeDataBean.getStatus());
        }
        List<String> proposedStatusList = new ArrayList<>();
        if (statusChangeDataBean.getProposedStatus() != null) {
            proposedStatusList.add(statusChangeDataBean.getProposedStatus());
        }
        Long franchise = loginDataBean.getCompanyId();
        Map<String, Map<String, Object>> featureCustomMapValue = statusChangeDataBean.getFeatureCustomMapValue();
        Map<String, Object> invoiceFieldMap = new HashMap<>();
        Map<String, Object> parcelFieldMap = new HashMap<>();
        Map<String, Object> lotFieldMap = new HashMap<>();
        Map<String, Object> packetFieldMap = new HashMap<>();

        if (!CollectionUtils.isEmpty(featureCustomMapValue)) {
            for (Map.Entry<String, Map<String, Object>> entry : featureCustomMapValue.entrySet()) {
                String featureName = entry.getKey();
                Map<String, Object> map = entry.getValue();
                if (!StringUtils.isEmpty(featureName)) {
                    if (featureName.equalsIgnoreCase(HkSystemConstantUtil.Feature.INVOICE)) {

                        if (!CollectionUtils.isEmpty(map)) {
                            for (Map.Entry<String, Object> entry1 : map.entrySet()) {
                                String key = entry1.getKey();
                                Object value = entry1.getValue();
//                                if (value != null && !StringUtils.isEmpty(value) && value != "null") {
                                invoiceFieldMap.put(key, value);
//                                }
                            }
                        }
                    } else if (featureName.equalsIgnoreCase(HkSystemConstantUtil.Feature.PARCEL)) {
                        if (!CollectionUtils.isEmpty(map)) {
                            for (Map.Entry<String, Object> entry1 : map.entrySet()) {
                                String key = entry1.getKey();
                                Object value = entry1.getValue();
                                parcelFieldMap.put(key, value);
                            }
                        }
                    } else if (featureName.equalsIgnoreCase(HkSystemConstantUtil.Feature.LOT)) {

                        if (!CollectionUtils.isEmpty(map)) {
                            for (Map.Entry<String, Object> entry1 : map.entrySet()) {
                                String key = entry1.getKey();
                                Object value = entry1.getValue();

                                lotFieldMap.put(key, value);
                            }
                        }
                    } else if (featureName.equalsIgnoreCase(HkSystemConstantUtil.Feature.PACKET)) {
                        for (Map.Entry<String, Object> entry1 : map.entrySet()) {
                            String key = entry1.getKey();
                            Object value = entry1.getValue();

                            packetFieldMap.put(key, value);
                        }
                    }
                }
            }
        }
        Set<HkPacketDocument> packetDocuments = new HashSet<>();
        List<HkInvoiceDocument> invoiceDocuments = null;
        if (!CollectionUtils.isEmpty(invoiceFieldMap)) {
            Iterator<Map.Entry<String, Object>> iter = invoiceFieldMap.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry<String, Object> entry = iter.next();
                String key = entry.getKey();
                Object value = entry.getValue();
                if (key.contains("$MS$") || key.contains("$UMS$") || key.contains("$AG$")) {
                    //System.out.println("value :" + value + value.getClass().getSimpleName());
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
            //System.out.println("invoiceFieldMap :" + invoiceFieldMap);
            UMFeatureDetailDataBean feature = applicationUtil.getuMFeatureDetailDataBeanMap().get(HkSystemConstantUtil.Feature.INVOICE);
            if (feature != null && !CollectionUtils.isEmpty(invoiceFieldMap)) {
//                Map<String, String> uiFieldMap = hkFieldService.retrieveCustomUIFieldNameWithComponentTypes(feature.getId(), loginDataBean.getCompanyId());
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
                    //System.out.println("value :" + value + value.getClass().getSimpleName());
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
            if (feature != null && (!CollectionUtils.isEmpty(parcelFieldMap) || !CollectionUtils.isEmpty(invoiceIds))) {
//                Map<String, String> uiFieldMap = hkFieldService.retrieveCustomUIFieldNameWithComponentTypes(feature.getId(), loginDataBean.getCompanyId());
                parcelDocuments = stockService.retrieveParcels(parcelFieldMap, invoiceIds, null, franchise, Boolean.FALSE, null,HkSystemConstantUtil.StockStatus.NEW_ROUGH,null,null);
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
        if (!CollectionUtils.isEmpty(invoiceIds) || !CollectionUtils.isEmpty(parcelIds) || !CollectionUtils.isEmpty(lotFieldMap) || !CollectionUtils.isEmpty(statusList) || !CollectionUtils.isEmpty(proposedStatusList)) {
            Set<HkLotDocument> lotDocumentsWithPackets = new HashSet<>();
            Set<HkLotDocument> lotDocumentsWOPackets = new HashSet<>();
            Set<HkLotDocument> lotDocuments1 = new HashSet<>();
            if (!CollectionUtils.isEmpty(lotFieldMap) || !CollectionUtils.isEmpty(invoiceIds) || !CollectionUtils.isEmpty(parcelIds) || !CollectionUtils.isEmpty(statusList) || !CollectionUtils.isEmpty(proposedStatusList)) {
                Iterator<Map.Entry<String, Object>> iter = lotFieldMap.entrySet().iterator();
                while (iter.hasNext()) {
                    Map.Entry<String, Object> entry = iter.next();
                    String key = entry.getKey();
                    Object value = entry.getValue();
                    if (key.contains("$MS$") || key.contains("$UMS$") || key.contains("$AG$")) {
                        //System.out.println("value :" + value + value.getClass().getSimpleName());
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
                if (!CollectionUtils.isEmpty(lotFieldMap) || !CollectionUtils.isEmpty(invoiceIds) || !CollectionUtils.isEmpty(parcelIds) || !CollectionUtils.isEmpty(statusList) || !CollectionUtils.isEmpty(proposedStatusList)) {
                    List<HkLotDocument> retrieveLots = stockService.retrieveLotsByCriteria(lotFieldMap, invoiceIds, parcelIds, null, statusList, proposedStatusList, franchise, Boolean.FALSE, Boolean.FALSE);
                    if (!CollectionUtils.isEmpty(retrieveLots)) {
                        lotDocuments1.addAll(retrieveLots);
                    }
                }

            }
            if (!CollectionUtils.isEmpty(lotDocuments1)) {
                for (HkLotDocument hkLotDocument : lotDocuments1) {
                    if (!hkLotDocument.isHasPacket()) {
                        lotDocumentsWOPackets.add(hkLotDocument);
                        lotDocuments.add(hkLotDocument);
                    } else {
                        lotDocumentsWithPackets.add(hkLotDocument);
                    }
                }
            }
            if (!CollectionUtils.isEmpty(lotDocumentsWithPackets)) {
                lotIds = new ArrayList<>();
                for (HkLotDocument lotDocument : lotDocumentsWithPackets) {
                    lotIds.add(lotDocument.getId());
                }
            }
            if (!CollectionUtils.isEmpty(packetFieldMap) || !CollectionUtils.isEmpty(invoiceIds) || !CollectionUtils.isEmpty(parcelIds) || !CollectionUtils.isEmpty(lotIds) || !CollectionUtils.isEmpty(statusList) || !CollectionUtils.isEmpty(proposedStatusList)) {
                Iterator<Map.Entry<String, Object>> iter = packetFieldMap.entrySet().iterator();
                while (iter.hasNext()) {
                    Map.Entry<String, Object> entry = iter.next();
                    String key = entry.getKey();
                    Object value = entry.getValue();
                    if (key.contains("$MS$") || key.contains("$UMS$") || key.contains("$AG$")) {
                        //System.out.println("value :" + value + value.getClass().getSimpleName());
                        if (value instanceof String && !StringUtils.isEmpty(value)) {
                            List<String> items = Arrays.asList(value.toString().split(","));
                            packetFieldMap.put(key, items);
                        } else if (value instanceof Collection && !CollectionUtils.isEmpty((Collection<?>) value)) {
                            List<String> items = Arrays.asList(value.toString().split(","));
                            packetFieldMap.put(key, items);
                        } else {
                            iter.remove();
                        }
                    }
                }
                if (!CollectionUtils.isEmpty(packetFieldMap) || !CollectionUtils.isEmpty(invoiceIds) || !CollectionUtils.isEmpty(parcelIds) || !CollectionUtils.isEmpty(lotIds) || !CollectionUtils.isEmpty(statusList) || !CollectionUtils.isEmpty(proposedStatusList)) {
                    packetDocuments.addAll(stockService.retrieveLotsByCriteria(packetFieldMap, invoiceIds, parcelIds, lotIds, statusList, proposedStatusList, franchise, Boolean.FALSE, Boolean.TRUE));
                }
            }
        }
//            if (!CollectionUtils.isEmpty(packetFieldMap)) {
//                packetDocuments.addAll(stockService.retrievePackets(packetFieldMap, null, null, null, franchise, Boolean.FALSE));
//            }
        List<SelectItem> selectItems = new ArrayList<>();
        if (!CollectionUtils.isEmpty(lotDocuments)) {
            invoiceIds = new ArrayList<>();
            parcelIds = new ArrayList<>();
            for (HkLotDocument lotDocument : lotDocuments) {
                invoiceIds.add(lotDocument.getInvoice());
                parcelIds.add(lotDocument.getParcel());
            }
            List<HkInvoiceDocument> hkInvoiceDocuments = stockService.retrieveInvoices(invoiceIds, franchise, Boolean.FALSE);
            List<HkParcelDocument> hkParcelDocuments = stockService.retrieveParcels(parcelIds, franchise, Boolean.FALSE);
            if (!CollectionUtils.isEmpty(hkInvoiceDocuments) && !CollectionUtils.isEmpty(hkParcelDocuments)) {
                for (HkLotDocument lotDocument : lotDocuments) {
                    Map<String, Object> map = new HashMap<>();
                    SelectItem selectItem = new SelectItem(lotDocument.getId().toString(), lotDocument.getParcel().toString(), lotDocument.getInvoice().toString());

                    //////////////////////////////////////////
                    List uiFieldListForLot = new ArrayList<>();
                    if (lotDocument.getFieldValue() != null) {
                        Map<String, Object> map1 = lotDocument.getFieldValue().toMap();
                        if (!CollectionUtils.isEmpty(map1)) {
                            for (Map.Entry<String, Object> entrySet : map1.entrySet()) {
                                String key = entrySet.getKey();
                                Object value = entrySet.getValue();
                                uiFieldListForLot.add(key);
                            }
                        }
                    }
                    Map<String, String> uiFieldListWithComponentTypeForLot = hkFieldService.retrieveUIFieldNameWithComponentTypes(uiFieldListForLot);
                    String pointerComponentType = HkSystemConstantUtil.CustomField.ComponentType.POINTER + "@*" + HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN;

                    HkLotDocument oldlotDocument = lotDocument;
                    Map<String, Object> customMapforLot = lotDocument.getFieldValue().toMap();

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
                    //////////////////////////////////////////

                    if (!CollectionUtils.isEmpty(lotDbFieldName)) {
                        for (String model : lotDbFieldName) {
                            if (oldlotDocument.getFieldValue().toMap().containsKey(model)) {
                                //System.out.println("----------------lotDocument.getFieldValue()-----------------" + oldlotDocument.getFieldValue().toMap());
                                map.put(model, oldlotDocument.getFieldValue().toMap().get(model));
                            }
                        }
                    }
                    if (!CollectionUtils.isEmpty(invoiceDbFieldName)) {
                        for (HkInvoiceDocument hkInvoiceDocument : hkInvoiceDocuments) {
                            if (hkInvoiceDocument.getId().toString().equals(lotDocument.getInvoice().toString())) {
                                for (String model : invoiceDbFieldName) {
                                    if (hkInvoiceDocument.getFieldValue().toMap().containsKey(model)) {
                                        map.put(model, hkInvoiceDocument.getFieldValue().toMap().get(model));
                                    }
                                }
                                break;
                            }
                        }
                    }

                    if (!CollectionUtils.isEmpty(parcelDbFieldName)) {
                        for (HkParcelDocument hkParcelDocument : hkParcelDocuments) {

/////////////////////////////////////////////////////////////////////////////////
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

                            Map<String, String> uiFieldListWithComponentTypeForParcel = hkFieldService.retrieveUIFieldNameWithComponentTypes(uiFieldListForParcel);
                            HkParcelDocument oldparcelDocument = hkParcelDocument;
                            Map<String, Object> customMap = hkParcelDocument.getFieldValue().toMap();

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
                            ///////////////////////////////////////////////////////////////////////////
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
            List<HkInvoiceDocument> hkInvoiceDocuments = stockService.retrieveInvoices(invoiceIds, franchise, Boolean.FALSE);
            List<HkParcelDocument> hkParcelDocuments = stockService.retrieveParcels(parcelIds, franchise, Boolean.FALSE);
            List<HkLotDocument> hklotDocuments = stockService.retrieveLotsByIds(lotIds, franchise, Boolean.FALSE, null, null, Boolean.TRUE);

            if (!CollectionUtils.isEmpty(hkInvoiceDocuments) && !CollectionUtils.isEmpty(hkParcelDocuments)) {
                for (HkPacketDocument packetDocument : packetDocuments) {
                    Map<String, Object> map = new HashMap<>();
                    SelectItem selectItem = new SelectItem(packetDocument.getLot().toString(), packetDocument.getParcel().toString(), packetDocument.getInvoice().toString(), packetDocument.getId().toString());

                    /////////////////////////////////////////////////////////////////////////////////
                    List uiFieldListForPacket = new ArrayList<>();
                    if (packetDocument.getFieldValue() != null) {
                        Map<String, Object> map1 = packetDocument.getFieldValue().toMap();
                        if (!CollectionUtils.isEmpty(map1)) {
                            for (Map.Entry<String, Object> entrySet : map1.entrySet()) {
                                String key = entrySet.getKey();
                                Object value = entrySet.getValue();
                                uiFieldListForPacket.add(key);
                            }
                        }
                    }

                    Map<String, String> uiFieldListWithComponentTypeForPacket = hkFieldService.retrieveUIFieldNameWithComponentTypes(uiFieldListForPacket);
                    String pointerComponentType = HkSystemConstantUtil.CustomField.ComponentType.POINTER + "@*" + HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN;
                    HkPacketDocument oldpacketDocument = packetDocument;
                    Map<String, Object> customMapForPacket = packetDocument.getFieldValue().toMap();

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
                    ///////////////////////////////////////////////////////////////////////////

                    if (!CollectionUtils.isEmpty(packetDbFieldName)) {
                        for (String model : packetDbFieldName) {
                            if (oldpacketDocument.getFieldValue().toMap().containsKey(model)) {
                                map.put(model, oldpacketDocument.getFieldValue().toMap().get(model));
                            }
                        }
                    }
                    if (!CollectionUtils.isEmpty(invoiceDbFieldName)) {
                        for (HkInvoiceDocument hkInvoiceDocument : hkInvoiceDocuments) {
                            if (hkInvoiceDocument.getId().toString().equals(packetDocument.getInvoice().toString())) {
                                for (String model : invoiceDbFieldName) {
                                    if (hkInvoiceDocument.getFieldValue().toMap().containsKey(model)) {
                                        map.put(model, hkInvoiceDocument.getFieldValue().toMap().get(model));
                                    }
                                }
                                break;
                            }
                        }
                    }
                    if (!CollectionUtils.isEmpty(parcelDbFieldName)) {
                        for (HkParcelDocument hkParcelDocument : hkParcelDocuments) {

                            /////////////////////////////////////////////////////////////////////////////////
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

                            Map<String, String> uiFieldListWithComponentTypeForParcel = hkFieldService.retrieveUIFieldNameWithComponentTypes(uiFieldListForParcel);
                            HkParcelDocument oldparcelDocument = hkParcelDocument;
                            Map<String, Object> customMap = hkParcelDocument.getFieldValue().toMap();

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
                            ///////////////////////////////////////////////////////////////////////////

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

                            //////////////////////////////////////////
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
                            Map<String, String> uiFieldListWithComponentTypeForLot = hkFieldService.retrieveUIFieldNameWithComponentTypes(uiFieldListForLot);
                            HkLotDocument oldlotDocument = hkLotDocument;
                            Map<String, Object> customMapforLot = hkLotDocument.getFieldValue().toMap();

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
                            //////////////////////////////////////////

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
        //System.out.println("-----result-----from status change-----" + result.toString());

        return result;

    }

    public List<StatusChangeDataBean> retrieveStockByLotIdOrPacketId(List<ObjectId> lotOrPacketIds, String feature, Map<String, List<String>> featureDbfieldNameMap) throws GenericDatabaseException {
        //System.out.println("-----lotOrPacketId-----" + lotOrPacketId);

        //System.out.println("-----feature-----" + feature);
        //System.out.println("-----isMultipleLotOrPacket----" + isMultipleLotOrPacket);
        List<String> invoiceDbFieldNames = null;
        List<String> parcelDbFieldNames = null;
        List<String> lotDbFieldNames = null;
        List<String> packetDbFieldNames = null;
        List<StatusChangeDataBean> statusChangeDataBeanList = new ArrayList<>();
        for (Map.Entry<String, List<String>> entry : featureDbfieldNameMap.entrySet()) {
            String featureName = entry.getKey();
            if (featureName.equalsIgnoreCase("invoiceDbFieldName")) {
                invoiceDbFieldNames = featureDbfieldNameMap.get("invoiceDbFieldName");
            } else if (featureName.equalsIgnoreCase("parcelDbFieldName")) {
                parcelDbFieldNames = featureDbfieldNameMap.get("parcelDbFieldName");
            } else if (featureName.equalsIgnoreCase("lotDbFieldName")) {
                lotDbFieldNames = featureDbfieldNameMap.get("lotDbFieldName");
            } else if (featureName.equalsIgnoreCase("packetDbFieldName")) {
                packetDbFieldNames = featureDbfieldNameMap.get("packetDbFieldName");
            }
        }
        //System.out.println("packetDbFieldNames :" + packetDbFieldNames);
//        Map<String, String> praposedStatusMap = new HashMap<>();
//        List<String> proKeySet = HkSystemConstantUtil.PROPOSED_STATUS_MAP.get(status);
//        for (String statusMap1 : proKeySet) {
//            praposedStatusMap.put(statusMap1, statusMap1);
//        }
//        if (isMultipleLotOrPacket != null && !isMultipleLotOrPacket) {

        List<ObjectId> invoiceIds = new ArrayList<>();
        List<ObjectId> parcelIds = new ArrayList<>();
        List<ObjectId> lotIds = new ArrayList<>();
        Long franchise = loginDataBean.getCompanyId();
//        HkPacketDocument packetDocument = null;
//        HkLotDocument lotDocument = null;
        List<HkLotDocument> hkLotDocumentList = new ArrayList<>();
        List<HkPacketDocument> hkPacketDocumentList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(lotOrPacketIds) && feature.equals(HkSystemConstantUtil.Feature.PACKET)) {
            hkPacketDocumentList = stockService.retrievePacketsByIds(lotOrPacketIds, loginDataBean.getCompanyId(), Boolean.FALSE, null, Boolean.TRUE);
//                lotDocument = stockService.retrieveLotById(packetDocument.getLot());
        } else if (!CollectionUtils.isEmpty(lotOrPacketIds) && feature.equals(HkSystemConstantUtil.Feature.LOT)) {
            hkLotDocumentList = stockService.retrieveLotsByIds(lotOrPacketIds, loginDataBean.getCompanyId(), Boolean.FALSE, null, Boolean.FALSE, Boolean.TRUE);
        }
        if (!CollectionUtils.isEmpty(hkLotDocumentList)) {
            for (HkLotDocument lotDocument : hkLotDocumentList) {
                if (lotDocument != null) {
                    Map<Object, Object> map = new HashMap<>();
                    SelectItem selectItem = new SelectItem(lotDocument.getId().toString(), lotDocument.getParcel().toString(), lotDocument.getInvoice().toString());
                    invoiceIds.add(lotDocument.getInvoice());
                    parcelIds.add(lotDocument.getParcel());
                    List<HkInvoiceDocument> hkInvoiceDocuments = stockService.retrieveInvoices(invoiceIds, franchise, Boolean.FALSE);
                    List<HkParcelDocument> hkParcelDocuments = stockService.retrieveParcels(parcelIds, franchise, Boolean.FALSE);
                    if (!CollectionUtils.isEmpty(hkInvoiceDocuments) && hkInvoiceDocuments.size() == 1 && !CollectionUtils.isEmpty(hkParcelDocuments) && hkParcelDocuments.size() == 1) {
                        HkInvoiceDocument hkInvoiceDocument = hkInvoiceDocuments.get(0);
                        HkParcelDocument hkParcelDocument = hkParcelDocuments.get(0);

                        List uiFieldListForLot = new ArrayList<>();
                        if (lotDocument.getFieldValue() != null) {
                            Map<String, Object> map1 = lotDocument.getFieldValue().toMap();
                            if (!CollectionUtils.isEmpty(map1)) {
                                for (Map.Entry<String, Object> entrySet : map1.entrySet()) {
                                    String key = entrySet.getKey();
                                    Object value = entrySet.getValue();
                                    uiFieldListForLot.add(key);
                                }
                            }
                        }
                        Map<String, String> uiFieldListWithComponentTypeForLot = hkFieldService.retrieveUIFieldNameWithComponentTypes(uiFieldListForLot);
                        String pointerComponentType = HkSystemConstantUtil.CustomField.ComponentType.POINTER + "@*" + HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN;

                        HkLotDocument oldlotDocument = lotDocument;
                        Map<String, Object> customMapforLot = lotDocument.getFieldValue().toMap();

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
//                    selectItem.setCustom1(oldlotDocument.getFieldValue().toMap());
                        Map<String, Object> lotFieldValueMap = oldlotDocument.getFieldValue().toMap();
                        if (!CollectionUtils.isEmpty(lotDbFieldNames)) {
                            map = new HashMap<>();
                            for (String lotField : lotDbFieldNames) {
                                Object value = lotFieldValueMap.get(lotField);
                                map.put(lotField, value);
                            }
                            selectItem.setCustom1(map);
                        }
                        Map<Object, Object> invoicefieldmap = new HashMap<>();
                        if (hkInvoiceDocument.getId().toString().equals(lotDocument.getInvoice().toString())) {
                            Map<String, Object> invoiceFieldValueMap = hkInvoiceDocument.getFieldValue().toMap();
                            if (!CollectionUtils.isEmpty(invoiceDbFieldNames)) {
                                map = new HashMap<>();
                                for (String invoiceField : invoiceDbFieldNames) {
                                    Object value = invoiceFieldValueMap.get(invoiceField);
                                    map.put(invoiceField, value);
                                }
                                selectItem.setCustom3(map);
                            }
//                        selectItem.setCustom3(hkInvoiceDocument.getFieldValue().toMap());
                        }
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

                        Map<String, String> uiFieldListWithComponentTypeForParcel = hkFieldService.retrieveUIFieldNameWithComponentTypes(uiFieldListForParcel);
                        HkParcelDocument oldparcelDocument = hkParcelDocument;
                        Map<String, Object> customMap = hkParcelDocument.getFieldValue().toMap();

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
                        Map<Object, Object> parcelfieldmap = new HashMap<>();
                        if (hkParcelDocument.getId().toString().equals(lotDocument.getParcel().toString())) {
                            Map<String, Object> parcelFieldValueMap = oldparcelDocument.getFieldValue().toMap();
                            if (!CollectionUtils.isEmpty(parcelDbFieldNames)) {
                                map = new HashMap<>();
                                for (String parcelField : parcelDbFieldNames) {
                                    Object value = parcelFieldValueMap.get(parcelField);
                                    map.put(parcelField, value);
                                }
                                selectItem.setCustom4(map);
                            }
                        }
                    }
                    statusChangeDataBeanList.add(new StatusChangeDataBean(selectItem));
                }
            }
            return statusChangeDataBeanList;
        }
        if (!CollectionUtils.isEmpty(hkPacketDocumentList)) {
            for (HkPacketDocument packetDocument : hkPacketDocumentList) {
                if (packetDocument != null) {
                    Map<Object, Object> map = new HashMap<>();
                    SelectItem selectItem = new SelectItem(packetDocument.getLot().toString(), packetDocument.getParcel().toString(), packetDocument.getInvoice().toString());
                    selectItem.setId(packetDocument.getId().toString());
                    invoiceIds.add(packetDocument.getInvoice());
                    parcelIds.add(packetDocument.getParcel());
                    lotIds.add(packetDocument.getLot());
                    List<HkInvoiceDocument> hkInvoiceDocuments = stockService.retrieveInvoices(invoiceIds, franchise, Boolean.FALSE);
                    List<HkParcelDocument> hkParcelDocuments = stockService.retrieveParcels(parcelIds, franchise, Boolean.FALSE);
                    List<HkLotDocument> hkLotDocuments = stockService.retrieveLotsByIds(lotIds, franchise, Boolean.FALSE, null, null, Boolean.TRUE);
                    if (!CollectionUtils.isEmpty(hkInvoiceDocuments) && hkInvoiceDocuments.size() == 1 && !CollectionUtils.isEmpty(hkParcelDocuments) && hkParcelDocuments.size() == 1 && !CollectionUtils.isEmpty(hkLotDocuments) && hkLotDocuments.size() == 1) {
                        HkInvoiceDocument hkInvoiceDocument = hkInvoiceDocuments.get(0);
                        HkParcelDocument hkParcelDocument = hkParcelDocuments.get(0);
                        HkLotDocument hkLotDocument = hkLotDocuments.get(0);

                        List uiFieldListForPacket = new ArrayList<>();
                        if (packetDocument.getFieldValue() != null) {
                            Map<String, Object> map1 = packetDocument.getFieldValue().toMap();
                            if (!CollectionUtils.isEmpty(map1)) {
                                for (Map.Entry<String, Object> entrySet : map1.entrySet()) {
                                    String key = entrySet.getKey();
                                    Object value = entrySet.getValue();
                                    uiFieldListForPacket.add(key);
                                }
                            }
                        }
                        Map<String, String> uiFieldListWithComponentTypeForPacket = hkFieldService.retrieveUIFieldNameWithComponentTypes(uiFieldListForPacket);
                        String pointerComponentTypePacket = HkSystemConstantUtil.CustomField.ComponentType.POINTER + "@*" + HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN;

                        HkPacketDocument oldPacketDocument = packetDocument;
                        Map<String, Object> customMapforPacket = packetDocument.getFieldValue().toMap();

                        if (!CollectionUtils.isEmpty(uiFieldListWithComponentTypeForPacket)) {
                            for (Map.Entry<String, String> field : uiFieldListWithComponentTypeForPacket.entrySet()) {
                                if (field.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN) || field.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.USER_MULTISELECT) || field.getValue().equals(pointerComponentTypePacket)) {
                                    if (customMapforPacket != null && !customMapforPacket.isEmpty() && customMapforPacket.containsKey(field.getKey())) {
                                        String value = customMapforPacket.get(field.getKey()).toString().replaceAll("\\[", "").replaceAll("\\]", "");
                                        customMapforPacket.put(field.getKey(), value);
                                    }
                                }
                            }
                            oldPacketDocument.getFieldValue().putAll(customMapforPacket);
                        }
                        Map<String, Object> packetFieldValueMap = oldPacketDocument.getFieldValue().toMap();
                        if (!CollectionUtils.isEmpty(packetDbFieldNames)) {
                            map = new HashMap<>();
                            for (String packetField : packetDbFieldNames) {
                                Object value = packetFieldValueMap.get(packetField);
                                map.put(packetField, value);
                            }
                            selectItem.setCustom5(map);
                        }

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
                        Map<String, String> uiFieldListWithComponentTypeForLot = hkFieldService.retrieveUIFieldNameWithComponentTypes(uiFieldListForLot);
                        String pointerComponentType = HkSystemConstantUtil.CustomField.ComponentType.POINTER + "@*" + HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN;

                        HkLotDocument oldlotDocument = hkLotDocument;
                        Map<String, Object> customMapforLot = hkLotDocument.getFieldValue().toMap();

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
                        if (hkLotDocument.getId().toString().equals(packetDocument.getLot().toString())) {
                            Map<String, Object> lotFieldValueMap = oldlotDocument.getFieldValue().toMap();
                            if (!CollectionUtils.isEmpty(lotDbFieldNames)) {
                                map = new HashMap<>();
                                for (String lotField : lotDbFieldNames) {
                                    Object value = lotFieldValueMap.get(lotField);
                                    map.put(lotField, value);
                                }
                                selectItem.setCustom1(map);
                            }
                        }

                        Map<Object, Object> invoicefieldmap = new HashMap<>();
                        if (hkInvoiceDocument.getId().toString().equals(packetDocument.getInvoice().toString())) {
                            Map<String, Object> invoiceFieldValueMap = hkInvoiceDocument.getFieldValue().toMap();
                            if (!CollectionUtils.isEmpty(invoiceDbFieldNames)) {
                                map = new HashMap<>();
                                for (String invoiceField : invoiceDbFieldNames) {
                                    Object value = invoiceFieldValueMap.get(invoiceField);
                                    map.put(invoiceField, value);
                                }
                                selectItem.setCustom3(map);
                            }
                        }
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

                        Map<String, String> uiFieldListWithComponentTypeForParcel = hkFieldService.retrieveUIFieldNameWithComponentTypes(uiFieldListForParcel);
                        HkParcelDocument oldparcelDocument = hkParcelDocument;
                        Map<String, Object> customMap = hkParcelDocument.getFieldValue().toMap();

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
                        Map<Object, Object> parcelfieldmap = new HashMap<>();
                        if (hkParcelDocument.getId().toString().equals(packetDocument.getParcel().toString())) {
                            Map<String, Object> parcelFieldValueMap = oldparcelDocument.getFieldValue().toMap();
                            if (!CollectionUtils.isEmpty(parcelDbFieldNames)) {
                                map = new HashMap<>();
                                for (String parcelField : parcelDbFieldNames) {
                                    Object value = parcelFieldValueMap.get(parcelField);
                                    map.put(parcelField, value);
                                }
                                selectItem.setCustom4(map);
                            }
                        }
                    }
                    statusChangeDataBeanList.add(new StatusChangeDataBean(selectItem));
                }
            }
            return statusChangeDataBeanList;
        }
        return null;
    }

    public Boolean changeSave(Map<String, Object> lotCustom, Map<String, Object> packetCustom, Map<String, String> lotDbtype, Map<String, String> packetDbtype, List<ObjectId> lotIds, List<ObjectId> packetIds, String status) throws GenericDatabaseException {
        Boolean statusChanged = Boolean.FALSE;
        UMFeatureDetailDataBean feature = applicationUtil.getuMFeatureDetailDataBeanMap().get(HkSystemConstantUtil.Feature.LOT);
        Map<String, String> dbFieldWithFormulaMap =null;
        // Commmented by Shifa temporarly
//        hkFieldService.retrieveFeatureNameWithDbFieldListForFormula(feature.getId(), loginDataBean.getCompanyId(), HkSystemConstantUtil.FeatureForFormulaEvaluation.EDIT_LOT, HkSystemConstantUtil.CustomField.ComponentType.FORMULA);
        Map<String, HkFieldDocument> mapForFeatureInvolvedInFormula = hkFieldService.retrieveMapOfDbFieldNameForFeatureInvolvedInFormula(loginDataBean.getCompanyId(), HkSystemConstantUtil.Feature.LOT, HkSystemConstantUtil.CustomField.ComponentType.FORMULA);

        if (!CollectionUtils.isEmpty(lotIds)) {
            BasicBSONObject bSONObject = hkFieldService.makeBSONObject(lotCustom, lotDbtype, HkSystemConstantUtil.Feature.LOT, loginDataBean.getCompanyId(), null,null);
            statusChanged = stockService.changeStatus(bSONObject, packetIds, lotIds, null, status, loginDataBean.getCompanyId(), loginDataBean.getId(), dbFieldWithFormulaMap, mapForFeatureInvolvedInFormula, applicationUtil.getFeatureIdNameMap());
        }
        if (!CollectionUtils.isEmpty(packetIds)) {
            BasicBSONObject bSONObject = hkFieldService.makeBSONObject(packetCustom, packetDbtype, HkSystemConstantUtil.Feature.PACKET, loginDataBean.getCompanyId(), null,null);
            statusChanged = stockService.changeStatus(bSONObject, packetIds, lotIds, null, status, loginDataBean.getCompanyId(), loginDataBean.getId(), dbFieldWithFormulaMap, mapForFeatureInvolvedInFormula, applicationUtil.getFeatureIdNameMap());

        }
        return statusChanged;
    }

    private Map<HkSectionDocument, List<HkFeatureFieldPermissionDocument>> retrieveFieldsConfigureForSearch() {

        List<HkFieldDocument> fieldEntityList = null;
        //Retrieved FeatureId
        UMFeatureDetailDataBean feature = applicationUtil.getuMFeatureDetailDataBeanMap().get("statuschange");
        Map<HkSectionDocument, List<HkFeatureFieldPermissionDocument>> fieldPermissionEntitys = new HashMap<>();
        if (feature != null) {
            fieldPermissionEntitys = hkFieldService.retrieveFeatureFieldPermissionForSearch(feature.getId(), loginDataBean.getRoleIds());
            if (!CollectionUtils.isEmpty(fieldPermissionEntitys)) {
                fieldEntityList = new LinkedList<>();
                for (Map.Entry<HkSectionDocument, List<HkFeatureFieldPermissionDocument>> entry : fieldPermissionEntitys.entrySet()) {
                    HkSectionDocument hkSectionEntity = entry.getKey();
                    List<HkFeatureFieldPermissionDocument> list = entry.getValue();
                    if (!CollectionUtils.isEmpty(list)) {
                        for (HkFeatureFieldPermissionDocument hkFeatureFieldPermissionEntity : list) {
                            if (hkFeatureFieldPermissionEntity.getHkFieldEntity() != null) {
                                fieldEntityList.add(hkFeatureFieldPermissionEntity.getHkFieldEntity());
                            }
                        }
                    }
                }

            }
        }
        return fieldPermissionEntitys;
    }

}
