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
import com.argusoft.hkg.nosql.core.HkUserService;
import com.argusoft.hkg.nosql.model.HkFeatureFieldPermissionDocument;
import com.argusoft.hkg.nosql.model.HkInvoiceDocument;
import com.argusoft.hkg.nosql.model.HkIssueDocument;
import com.argusoft.hkg.nosql.model.HkLotDocument;
import com.argusoft.hkg.nosql.model.HkPacketDocument;
import com.argusoft.hkg.nosql.model.HkParcelDocument;
import com.argusoft.hkg.web.center.transformers.CenterCustomFieldTransformer;
import com.argusoft.hkg.web.center.usermanagement.databeans.LoginDataBean;
import com.argusoft.hkg.web.center.usermanagement.databeans.UMFeatureDetailDataBean;
import com.argusoft.hkg.web.center.util.ApplicationUtil;
import com.argusoft.hkg.web.util.SelectItem;
import com.argusoft.sync.center.model.HkFieldDocument;
import com.argusoft.sync.center.model.HkSectionDocument;
import com.argusoft.sync.center.model.HkSystemConfigurationDocument;
import com.argusoft.sync.center.model.SyncCenterUserDocument;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.bson.BasicBSONObject;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 *
 * @author brijesh
 */
@Service
public class EndOfDayTransformer {

    @Autowired
    HkCustomFieldService hkCustomFieldService;

    @Autowired
    HkCustomFieldService hkFieldService;

    @Autowired
    LoginDataBean loginDataBean;

    @Autowired
    HkStockService stockService;

    @Autowired
    HkCustomFieldService customFieldService;

    @Autowired
    CenterCustomFieldTransformer customFieldTransformerBean;

    @Autowired
    ApplicationUtil applicationUtil;

    @Autowired
    HkUserService hkUserService;

    public List<SelectItem> retrieveIssueStocks(List<String> invoiceDbFieldName, List<String> lotDbFieldName, List<String> packetDbFieldName, List<String> parcelDbFieldName, List<String> issueDbFieldName) {
        List<SelectItem> result = new ArrayList<>();
        ////////////////////////////////////////////////
        //System.out.println("----invoice dbfieldName-----" + invoiceDbFieldName);
        //System.out.println("----parcelDbFieldName dbfieldName-----" + parcelDbFieldName);
        //System.out.println("----lotDbFieldName dbfieldName-----" + lotDbFieldName);
        //System.out.println("----packetDbFieldName dbfieldName-----" + packetDbFieldName);
        //System.out.println("----issueDbFieldName dbfieldName-----" + issueDbFieldName);
        ////////////////////////////////////////////////
        Long franchise = loginDataBean.getCompanyId();
        BasicBSONObject lotBasicBSONObject = new BasicBSONObject();
        ArrayList<String> inStockOfValueList = new ArrayList<String>();
        inStockOfValueList.add(loginDataBean.getId() + ":E");

        lotBasicBSONObject.put(HkSystemConstantUtil.LotStaticFieldName.IN_STOCK_OF, inStockOfValueList);
        //System.out.println("-------lotBasicBson======" + lotBasicBSONObject);
////////////////////////////////////////////////////////////////////////////////////////
        BasicBSONObject packetBasicBSONObject = new BasicBSONObject();
        packetBasicBSONObject.put(HkSystemConstantUtil.PacketStaticFieldName.IN_STOCK_OF, inStockOfValueList);
        //System.out.println("-------packetBasicBSONObject======" + packetBasicBSONObject);

        /////////////////////////////////////////////////////////////////////////////
        List<HkLotDocument> lotDocuments = new ArrayList<>();
        List<HkPacketDocument> packetDocuments = new ArrayList<>();

        lotDocuments = (List<HkLotDocument>) stockService.retrieveLotsOrPacketsForIssueForEod(lotBasicBSONObject, Boolean.FALSE, franchise);
        //System.out.println("-----retrieveLotsForIssueForEod-----" + lotDocuments);
        packetDocuments = (List<HkPacketDocument>) stockService.retrieveLotsOrPacketsForIssueForEod(packetBasicBSONObject, Boolean.TRUE, franchise);
        //System.out.println("-----retrievePacketsForIssueForEod-----" + packetDocuments);

        List<ObjectId> lotIds = null;
        List<ObjectId> packetIds = null;
        List<SelectItem> selectItems = new ArrayList<>();
        if (!CollectionUtils.isEmpty(lotDocuments)) {
            List<ObjectId> invoiceIds = new ArrayList<>();
            List<ObjectId> parcelIds = new ArrayList<>();
            lotIds = new ArrayList<>();
            for (HkLotDocument lotDocument : lotDocuments) {
                invoiceIds.add(lotDocument.getInvoice());
                parcelIds.add(lotDocument.getParcel());
                lotIds.add(lotDocument.getId());
            }
            List<HkInvoiceDocument> hkInvoiceDocuments = stockService.retrieveInvoices(invoiceIds, franchise, Boolean.FALSE);
            List<HkParcelDocument> hkParcelDocuments = stockService.retrieveParcels(parcelIds, franchise, Boolean.FALSE);
            if (!CollectionUtils.isEmpty(hkInvoiceDocuments) && !CollectionUtils.isEmpty(hkParcelDocuments)) {
                for (HkLotDocument lotDocument : lotDocuments) {
                    Map<String, Object> map = new HashMap<>();
                    SelectItem selectItem = new SelectItem(lotDocument.getId().toString(), lotDocument.getParcel().toString(), lotDocument.getInvoice().toString());

                    if (lotDocument.getFieldValue().toMap().containsKey(HkSystemConstantUtil.AutoNumber.LOT_SLIP_ID)) {
                        selectItem.setLotSlipId(lotDocument.getFieldValue().toMap().get(HkSystemConstantUtil.AutoNumber.LOT_SLIP_ID));
                    }
                    if (lotDocument.getFieldValue().toMap().containsKey(HkSystemConstantUtil.AutoNumber.LOT_ID)) {
                        selectItem.setLotId(lotDocument.getFieldValue().toMap().get(HkSystemConstantUtil.AutoNumber.LOT_ID).toString());
                    }

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
                    if (!CollectionUtils.isEmpty(issueDbFieldName)) {
                        if (lotDocument.getEodIssueDocument() != null) {
                            HkIssueDocument issueDocument = lotDocument.getEodIssueDocument();
                            List uiFieldListForIssue = new ArrayList<>();
                            if (issueDocument.getFieldValue() != null) {
                                Map<String, Object> map1 = issueDocument.getFieldValue().toMap();
                                if (!CollectionUtils.isEmpty(map1)) {
                                    for (Map.Entry<String, Object> entrySet : map1.entrySet()) {
                                        String key = entrySet.getKey();
                                        Object value = entrySet.getValue();
                                        uiFieldListForIssue.add(key);
                                    }
                                }
                            }
                            Map<String, String> uiFieldListWithComponentTypeForIssue = hkFieldService.retrieveUIFieldNameWithComponentTypes(uiFieldListForIssue);
                            HkIssueDocument oldIssueDocument = issueDocument;
                            Map<String, Object> customMapForIssue = issueDocument.getFieldValue().toMap();
                            if (!CollectionUtils.isEmpty(uiFieldListWithComponentTypeForIssue)) {
                                for (Map.Entry<String, String> field : uiFieldListWithComponentTypeForIssue.entrySet()) {
                                    if (field.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN) || field.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.USER_MULTISELECT) || field.getValue().equals(pointerComponentType)) {
                                        if (customMapForIssue != null && !customMapForIssue.isEmpty() && customMapForIssue.containsKey(field.getKey())) {
                                            String value = customMapForIssue.get(field.getKey()).toString().replaceAll("\\[", "").replaceAll("\\]", "");
                                            customMapForIssue.put(field.getKey(), value);
                                        }
                                    }
                                }
                                oldIssueDocument.getFieldValue().putAll(customMapForIssue);
                            }
                            for (String model : issueDbFieldName) {
                                if (oldIssueDocument.getFieldValue().toMap().containsKey(model)) {
                                    map.put(model, oldIssueDocument.getFieldValue().toMap().get(model));
                                }
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

                            if (oldparcelDocument.getId().toString().equals(lotDocument.getParcel().toString())) {
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
            List<ObjectId> invoiceIds = new ArrayList<>();
            List<ObjectId> parcelIds = new ArrayList<>();
            lotIds = new ArrayList<>();
            for (HkPacketDocument packetDocument : packetDocuments) {
                invoiceIds.add(packetDocument.getInvoice());
                parcelIds.add(packetDocument.getParcel());
                lotIds.add(packetDocument.getLot());
            }
            List<HkInvoiceDocument> hkInvoiceDocuments = stockService.retrieveInvoices(invoiceIds, franchise, Boolean.FALSE);
            List<HkParcelDocument> hkParcelDocuments = stockService.retrieveParcels(parcelIds, franchise, Boolean.FALSE);
            List<HkLotDocument> hklotDocuments = stockService.retrieveLotsByIds(lotIds, franchise, Boolean.FALSE, null, null,Boolean.TRUE);

            if (!CollectionUtils.isEmpty(hkInvoiceDocuments) && !CollectionUtils.isEmpty(hkParcelDocuments) && !CollectionUtils.isEmpty(hklotDocuments)) {
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

                    if (oldpacketDocument.getFieldValue().toMap().containsKey(HkSystemConstantUtil.AutoNumber.PACKET_SLIP_ID)) {
                        selectItem.setPacketSlipId(oldpacketDocument.getFieldValue().toMap().get(HkSystemConstantUtil.AutoNumber.PACKET_SLIP_ID));
                    }
                    if (oldpacketDocument.getFieldValue().toMap().containsKey(HkSystemConstantUtil.AutoNumber.PACKET_ID)) {
                        selectItem.setPacketId(oldpacketDocument.getFieldValue().toMap().get(HkSystemConstantUtil.AutoNumber.PACKET_ID).toString());
                    }

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
                    if (!CollectionUtils.isEmpty(issueDbFieldName)) {
                        if (packetDocument.getEodIssueDocument() != null) {
                            HkIssueDocument issueDocument = packetDocument.getEodIssueDocument();
                            List uiFieldListForIssue = new ArrayList<>();
                            if (issueDocument.getFieldValue() != null) {
                                Map<String, Object> map1 = issueDocument.getFieldValue().toMap();
                                if (!CollectionUtils.isEmpty(map1)) {
                                    for (Map.Entry<String, Object> entrySet : map1.entrySet()) {
                                        String key = entrySet.getKey();
                                        Object value = entrySet.getValue();
                                        uiFieldListForIssue.add(key);
                                    }
                                }
                            }
                            Map<String, String> uiFieldListWithComponentTypeForIssue = hkFieldService.retrieveUIFieldNameWithComponentTypes(uiFieldListForIssue);
                            HkIssueDocument oldIssueDocument = issueDocument;
                            Map<String, Object> customMapForIssue = issueDocument.getFieldValue().toMap();
                            if (!CollectionUtils.isEmpty(uiFieldListWithComponentTypeForIssue)) {
                                for (Map.Entry<String, String> field : uiFieldListWithComponentTypeForIssue.entrySet()) {
                                    if (field.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN) || field.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.USER_MULTISELECT) || field.getValue().equals(pointerComponentType)) {
                                        if (customMapForIssue != null && !customMapForIssue.isEmpty() && customMapForIssue.containsKey(field.getKey())) {
                                            String value = customMapForIssue.get(field.getKey()).toString().replaceAll("\\[", "").replaceAll("\\]", "");
                                            customMapForIssue.put(field.getKey(), value);
                                        }
                                    }
                                }
                                oldIssueDocument.getFieldValue().putAll(customMapForIssue);
                            }
                            for (String model : issueDbFieldName) {
                                if (oldIssueDocument.getFieldValue().toMap().containsKey(model)) {
                                    map.put(model, oldIssueDocument.getFieldValue().toMap().get(model));
                                }
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
        //System.out.println("-----result-----from issue stocks-----" + result.toString());

        return result;

    }

    public List<SelectItem> retrieveReturnStocks(List<String> invoiceDbFieldName, List<String> lotDbFieldName, List<String> packetDbFieldName, List<String> parcelDbFieldName, List<String> issueDbFieldName) {
        List<SelectItem> result = new ArrayList<>();
//        ////////////////////////////////////////////////
        //System.out.println("----invoice dbfieldName-----" + invoiceDbFieldName);
        //System.out.println("----parcelDbFieldName dbfieldName-----" + parcelDbFieldName);
        //System.out.println("----lotDbFieldName dbfieldName-----" + lotDbFieldName);
        //System.out.println("----packetDbFieldName dbfieldName-----" + packetDbFieldName);
        //System.out.println("----issueDbFieldName dbfieldName-----" + issueDbFieldName);
        Long franchise = loginDataBean.getCompanyId();
        List<HkLotDocument> lotDocuments = new ArrayList<>();
        lotDocuments = (List<HkLotDocument>) stockService.retrieveLotsOrPacketsForReturnForEod(loginDataBean.getId(), Boolean.FALSE, franchise);
        //System.out.println("-----retrieveLotsForIssueForEod-----" + lotDocuments);
        List<HkPacketDocument> packetDocuments = new ArrayList<>();
        packetDocuments = (List<HkPacketDocument>) stockService.retrieveLotsOrPacketsForReturnForEod(loginDataBean.getId(), Boolean.TRUE, franchise);
        //System.out.println("-----retrievePacketsForIssueForEod-----" + packetDocuments);

        List<ObjectId> lotIds = null;
        List<ObjectId> packetIds = null;
        List<SelectItem> selectItems = new ArrayList<>();
        if (!CollectionUtils.isEmpty(lotDocuments)) {
            List<ObjectId> invoiceIds = new ArrayList<>();
            List<ObjectId> parcelIds = new ArrayList<>();
            lotIds = new ArrayList<>();
            for (HkLotDocument lotDocument : lotDocuments) {
                invoiceIds.add(lotDocument.getInvoice());
                parcelIds.add(lotDocument.getParcel());
                lotIds.add(lotDocument.getId());
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
                    if (lotDocument.getFieldValue().toMap().containsKey(HkSystemConstantUtil.AutoNumber.LOT_SLIP_ID)) {
                        selectItem.setLotSlipId(lotDocument.getFieldValue().toMap().get(HkSystemConstantUtil.AutoNumber.LOT_SLIP_ID));
                    }
                    if (lotDocument.getFieldValue().toMap().containsKey(HkSystemConstantUtil.AutoNumber.LOT_ID)) {
                        selectItem.setLotId(lotDocument.getFieldValue().toMap().get(HkSystemConstantUtil.AutoNumber.LOT_ID).toString());
                    }

                    if (!CollectionUtils.isEmpty(lotDbFieldName)) {
                        for (String model : lotDbFieldName) {
                            if (oldlotDocument.getFieldValue().toMap().containsKey(model)) {
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
                    if (!CollectionUtils.isEmpty(issueDbFieldName)) {
                        if (lotDocument.getEodIssueDocument() != null) {
                            HkIssueDocument issueDocument = lotDocument.getEodIssueDocument();
                            List uiFieldListForIssue = new ArrayList<>();
                            if (issueDocument.getFieldValue() != null) {
                                Map<String, Object> map1 = issueDocument.getFieldValue().toMap();
                                if (!CollectionUtils.isEmpty(map1)) {
                                    for (Map.Entry<String, Object> entrySet : map1.entrySet()) {
                                        String key = entrySet.getKey();
                                        Object value = entrySet.getValue();
                                        uiFieldListForIssue.add(key);
                                    }
                                }
                            }
                            Map<String, String> uiFieldListWithComponentTypeForIssue = hkFieldService.retrieveUIFieldNameWithComponentTypes(uiFieldListForIssue);
                            HkIssueDocument oldIssueDocument = issueDocument;
                            Map<String, Object> customMapForIssue = issueDocument.getFieldValue().toMap();
                            if (!CollectionUtils.isEmpty(uiFieldListWithComponentTypeForIssue)) {
                                for (Map.Entry<String, String> field : uiFieldListWithComponentTypeForIssue.entrySet()) {
                                    if (field.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN) || field.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.USER_MULTISELECT) || field.getValue().equals(pointerComponentType)) {
                                        if (customMapForIssue != null && !customMapForIssue.isEmpty() && customMapForIssue.containsKey(field.getKey())) {
                                            String value = customMapForIssue.get(field.getKey()).toString().replaceAll("\\[", "").replaceAll("\\]", "");
                                            customMapForIssue.put(field.getKey(), value);
                                        }
                                    }
                                }
                                oldIssueDocument.getFieldValue().putAll(customMapForIssue);
                            }
                            for (String model : issueDbFieldName) {
                                if (oldIssueDocument.getFieldValue().toMap().containsKey(model)) {
                                    map.put(model, oldIssueDocument.getFieldValue().toMap().get(model));
                                }
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
                            if (oldparcelDocument.getId().toString().equals(lotDocument.getParcel().toString())) {
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
            List<ObjectId> invoiceIds = new ArrayList<>();
            List<ObjectId> parcelIds = new ArrayList<>();
            lotIds = new ArrayList<>();
            for (HkPacketDocument packetDocument : packetDocuments) {
                invoiceIds.add(packetDocument.getInvoice());
                parcelIds.add(packetDocument.getParcel());
                lotIds.add(packetDocument.getLot());
            }
            List<HkInvoiceDocument> hkInvoiceDocuments = stockService.retrieveInvoices(invoiceIds, franchise, Boolean.FALSE);
            List<HkParcelDocument> hkParcelDocuments = stockService.retrieveParcels(parcelIds, franchise, Boolean.FALSE);
            List<HkLotDocument> hklotDocuments = stockService.retrieveLotsByIds(lotIds, franchise, Boolean.FALSE, null, null,Boolean.TRUE);

            if (!CollectionUtils.isEmpty(hkInvoiceDocuments) && !CollectionUtils.isEmpty(hkParcelDocuments) && !CollectionUtils.isEmpty(hklotDocuments)) {
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

                    if (oldpacketDocument.getFieldValue().toMap().containsKey(HkSystemConstantUtil.AutoNumber.PACKET_SLIP_ID)) {
                        selectItem.setPacketSlipId(oldpacketDocument.getFieldValue().toMap().get(HkSystemConstantUtil.AutoNumber.PACKET_SLIP_ID));
                    }
                    if (oldpacketDocument.getFieldValue().toMap().containsKey(HkSystemConstantUtil.AutoNumber.PACKET_ID)) {
                        selectItem.setPacketId(oldpacketDocument.getFieldValue().toMap().get(HkSystemConstantUtil.AutoNumber.PACKET_ID).toString());
                    }

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
                    if (!CollectionUtils.isEmpty(issueDbFieldName)) {
                        if (packetDocument.getEodIssueDocument() != null) {
                            HkIssueDocument issueDocument = packetDocument.getEodIssueDocument();
                            List uiFieldListForIssue = new ArrayList<>();
                            if (issueDocument.getFieldValue() != null) {
                                Map<String, Object> map1 = issueDocument.getFieldValue().toMap();
                                if (!CollectionUtils.isEmpty(map1)) {
                                    for (Map.Entry<String, Object> entrySet : map1.entrySet()) {
                                        String key = entrySet.getKey();
                                        Object value = entrySet.getValue();
                                        uiFieldListForIssue.add(key);
                                    }
                                }
                            }
                            Map<String, String> uiFieldListWithComponentTypeForIssue = hkFieldService.retrieveUIFieldNameWithComponentTypes(uiFieldListForIssue);
                            HkIssueDocument oldIssueDocument = issueDocument;
                            Map<String, Object> customMapForIssue = issueDocument.getFieldValue().toMap();
                            if (!CollectionUtils.isEmpty(uiFieldListWithComponentTypeForIssue)) {
                                for (Map.Entry<String, String> field : uiFieldListWithComponentTypeForIssue.entrySet()) {
                                    if (field.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN) || field.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.USER_MULTISELECT) || field.getValue().equals(pointerComponentType)) {
                                        if (customMapForIssue != null && !customMapForIssue.isEmpty() && customMapForIssue.containsKey(field.getKey())) {
                                            String value = customMapForIssue.get(field.getKey()).toString().replaceAll("\\[", "").replaceAll("\\]", "");
                                            customMapForIssue.put(field.getKey(), value);
                                        }
                                    }
                                }
                                oldIssueDocument.getFieldValue().putAll(customMapForIssue);
                            }
                            for (String model : issueDbFieldName) {
                                if (oldIssueDocument.getFieldValue().toMap().containsKey(model)) {
                                    map.put(model, oldIssueDocument.getFieldValue().toMap().get(model));
                                }
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
                            if (hkParcelDocument.getId().toString().equals(packetDocument.getParcel().toString())) {
                                for (String model : parcelDbFieldName) {
                                    if (hkParcelDocument.getFieldValue().toMap().containsKey(model)) {
                                        map.put(model, hkParcelDocument.getFieldValue().toMap().get(model));
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
        //System.out.println("-----result-----from return stocks-----" + result.toString());

        return result;

    }

    public List<SelectItem> retrieveRecieveStocks(List<String> invoiceDbFieldName, List<String> lotDbFieldName, List<String> packetDbFieldName, List<String> parcelDbFieldName, List<String> issueDbFieldName) {
        List<SelectItem> result = new ArrayList<>();
//        ////////////////////////////////////////////////
        //System.out.println("----invoice dbfieldName-----" + invoiceDbFieldName);
        //System.out.println("----parcelDbFieldName dbfieldName-----" + parcelDbFieldName);
        //System.out.println("----lotDbFieldName dbfieldName-----" + lotDbFieldName);
        //System.out.println("----packetDbFieldName dbfieldName-----" + packetDbFieldName);
        //System.out.println("----issueDbFieldName dbfieldName-----" + issueDbFieldName);
        //////////////////////////////
        BasicBSONObject issueBSONObject = new BasicBSONObject();
        ArrayList<String> inStockOfDepartmentList = new ArrayList<String>();
        inStockOfDepartmentList.add(loginDataBean.getDepartment() + ":D");

        issueBSONObject.put(HkSystemConstantUtil.IssueStaticFieldName.IN_STOCK_OF_DEPT, inStockOfDepartmentList);
        //////////////////////////////
        //System.out.println("----issueBSONObject -----" + issueBSONObject);
        Long franchise = loginDataBean.getCompanyId();
        List<HkLotDocument> lotDocuments = new ArrayList<>();
        if (issueBSONObject != null) {
            lotDocuments = (List<HkLotDocument>) stockService.retrieveLotsOrPacketsForRecieveForEod(issueBSONObject, Boolean.FALSE, franchise);
            //System.out.println("-----retrieveLotsOrPacketsForRecieveForEod-----" + lotDocuments);
        }
        List<HkPacketDocument> packetDocuments = new ArrayList<>();
        if (issueBSONObject != null) {
            packetDocuments = (List<HkPacketDocument>) stockService.retrieveLotsOrPacketsForRecieveForEod(issueBSONObject, Boolean.TRUE, franchise);
            //System.out.println("-----retrieveLotsOrPacketsForRecieveForEod-----" + packetDocuments);
        }

        List<ObjectId> lotIds = null;
        List<ObjectId> packetIds = null;
        List<SelectItem> selectItems = new ArrayList<>();
        if (!CollectionUtils.isEmpty(lotDocuments)) {
            List<ObjectId> invoiceIds = new ArrayList<>();
            List<ObjectId> parcelIds = new ArrayList<>();
            lotIds = new ArrayList<>();
            for (HkLotDocument lotDocument : lotDocuments) {
                invoiceIds.add(lotDocument.getInvoice());
                parcelIds.add(lotDocument.getParcel());
                lotIds.add(lotDocument.getId());
            }
            List<HkInvoiceDocument> hkInvoiceDocuments = stockService.retrieveInvoices(invoiceIds, franchise, Boolean.FALSE);
            List<HkParcelDocument> hkParcelDocuments = stockService.retrieveParcels(parcelIds, franchise, Boolean.FALSE);
            if (!CollectionUtils.isEmpty(hkInvoiceDocuments) && !CollectionUtils.isEmpty(hkParcelDocuments)) {
                for (HkLotDocument lotDocument : lotDocuments) {
                    Map<String, Object> map = new HashMap<>();
                    SelectItem selectItem = new SelectItem(lotDocument.getId().toString(), lotDocument.getParcel().toString(), lotDocument.getInvoice().toString());

                    /////////////////////////////////////////////////////////////////
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
                    if (lotDocument.getFieldValue().toMap().containsKey(HkSystemConstantUtil.AutoNumber.LOT_SLIP_ID)) {
                        selectItem.setLotSlipId(lotDocument.getFieldValue().toMap().get(HkSystemConstantUtil.AutoNumber.LOT_SLIP_ID));
                    }
                    if (lotDocument.getFieldValue().toMap().containsKey(HkSystemConstantUtil.AutoNumber.LOT_ID)) {
                        selectItem.setLotId(lotDocument.getFieldValue().toMap().get(HkSystemConstantUtil.AutoNumber.LOT_ID).toString());
                    }

                    if (!CollectionUtils.isEmpty(lotDbFieldName)) {
                        for (String model : lotDbFieldName) {
                            if (lotDocument.getFieldValue().toMap().containsKey(model)) {
                                map.put(model, lotDocument.getFieldValue().toMap().get(model));
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

                    if (!CollectionUtils.isEmpty(issueDbFieldName)) {
                        if (lotDocument.getEodIssueDocument() != null) {
                            HkIssueDocument issueDocument = lotDocument.getEodIssueDocument();
                            List uiFieldListForIssue = new ArrayList<>();
                            if (issueDocument.getFieldValue() != null) {
                                Map<String, Object> map1 = issueDocument.getFieldValue().toMap();
                                if (!CollectionUtils.isEmpty(map1)) {
                                    for (Map.Entry<String, Object> entrySet : map1.entrySet()) {
                                        String key = entrySet.getKey();
                                        Object value = entrySet.getValue();
                                        uiFieldListForIssue.add(key);
                                    }
                                }
                            }
                            Map<String, String> uiFieldListWithComponentTypeForIssue = hkFieldService.retrieveUIFieldNameWithComponentTypes(uiFieldListForIssue);
                            HkIssueDocument oldIssueDocument = issueDocument;
                            Map<String, Object> customMapForIssue = issueDocument.getFieldValue().toMap();
                            if (!CollectionUtils.isEmpty(uiFieldListWithComponentTypeForIssue)) {
                                for (Map.Entry<String, String> field : uiFieldListWithComponentTypeForIssue.entrySet()) {
                                    if (field.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN) || field.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.USER_MULTISELECT) || field.getValue().equals(pointerComponentType)) {
                                        if (customMapForIssue != null && !customMapForIssue.isEmpty() && customMapForIssue.containsKey(field.getKey())) {
                                            String value = customMapForIssue.get(field.getKey()).toString().replaceAll("\\[", "").replaceAll("\\]", "");
                                            customMapForIssue.put(field.getKey(), value);
                                        }
                                    }
                                }
                                oldIssueDocument.getFieldValue().putAll(customMapForIssue);
                            }
                            for (String model : issueDbFieldName) {
                                if (oldIssueDocument.getFieldValue().toMap().containsKey(model)) {
                                    map.put(model, oldIssueDocument.getFieldValue().toMap().get(model));
                                }
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

                            if (oldparcelDocument.getId().toString().equals(lotDocument.getParcel().toString())) {
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
            List<ObjectId> invoiceIds = new ArrayList<>();
            List<ObjectId> parcelIds = new ArrayList<>();
            lotIds = new ArrayList<>();
            for (HkPacketDocument packetDocument : packetDocuments) {
                invoiceIds.add(packetDocument.getInvoice());
                parcelIds.add(packetDocument.getParcel());
                lotIds.add(packetDocument.getLot());
            }
            List<HkInvoiceDocument> hkInvoiceDocuments = stockService.retrieveInvoices(invoiceIds, franchise, Boolean.FALSE);
            List<HkParcelDocument> hkParcelDocuments = stockService.retrieveParcels(parcelIds, franchise, Boolean.FALSE);
            List<HkLotDocument> hklotDocuments = stockService.retrieveLotsByIds(lotIds, franchise, Boolean.FALSE, null, null,Boolean.TRUE);

            if (!CollectionUtils.isEmpty(hkInvoiceDocuments) && !CollectionUtils.isEmpty(hkParcelDocuments) && !CollectionUtils.isEmpty(hklotDocuments)) {
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
                    if (oldpacketDocument.getFieldValue().toMap().containsKey(HkSystemConstantUtil.AutoNumber.PACKET_SLIP_ID)) {
                        selectItem.setPacketSlipId(oldpacketDocument.getFieldValue().toMap().get(HkSystemConstantUtil.AutoNumber.PACKET_SLIP_ID));
                    }
                    if (oldpacketDocument.getFieldValue().toMap().containsKey(HkSystemConstantUtil.AutoNumber.PACKET_ID)) {
                        selectItem.setPacketId(oldpacketDocument.getFieldValue().toMap().get(HkSystemConstantUtil.AutoNumber.PACKET_ID).toString());
                    }

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
                    if (!CollectionUtils.isEmpty(issueDbFieldName)) {
                        if (packetDocument.getEodIssueDocument() != null) {
                            HkIssueDocument issueDocument = packetDocument.getEodIssueDocument();
                            List uiFieldListForIssue = new ArrayList<>();
                            if (issueDocument.getFieldValue() != null) {
                                Map<String, Object> map1 = issueDocument.getFieldValue().toMap();
                                if (!CollectionUtils.isEmpty(map1)) {
                                    for (Map.Entry<String, Object> entrySet : map1.entrySet()) {
                                        String key = entrySet.getKey();
                                        Object value = entrySet.getValue();
                                        uiFieldListForIssue.add(key);
                                    }
                                }
                            }
                            Map<String, String> uiFieldListWithComponentTypeForIssue = hkFieldService.retrieveUIFieldNameWithComponentTypes(uiFieldListForIssue);
                            HkIssueDocument oldIssueDocument = issueDocument;
                            Map<String, Object> customMapForIssue = issueDocument.getFieldValue().toMap();
                            if (!CollectionUtils.isEmpty(uiFieldListWithComponentTypeForIssue)) {
                                for (Map.Entry<String, String> field : uiFieldListWithComponentTypeForIssue.entrySet()) {
                                    if (field.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN) || field.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.USER_MULTISELECT) || field.getValue().equals(pointerComponentType)) {
                                        if (customMapForIssue != null && !customMapForIssue.isEmpty() && customMapForIssue.containsKey(field.getKey())) {
                                            String value = customMapForIssue.get(field.getKey()).toString().replaceAll("\\[", "").replaceAll("\\]", "");
                                            customMapForIssue.put(field.getKey(), value);
                                        }
                                    }
                                }
                                oldIssueDocument.getFieldValue().putAll(customMapForIssue);
                            }
                            for (String model : issueDbFieldName) {
                                if (oldIssueDocument.getFieldValue().toMap().containsKey(model)) {
                                    map.put(model, oldIssueDocument.getFieldValue().toMap().get(model));
                                }
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
        //System.out.println("-----result-----from retrieveRecieveStocks -----" + result.toString());

        return result;

    }

    private Map<HkSectionDocument, List<HkFeatureFieldPermissionDocument>> retrieveFieldsConfigureForSearch() {

        List<HkFieldDocument> fieldEntityList = null;
        //Retrieved FeatureId
        UMFeatureDetailDataBean feature = applicationUtil.getuMFeatureDetailDataBeanMap().get("dayEnd");
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

    public List<SelectItem> retrieveUsersForInstockOfIssue() throws GenericDatabaseException {

        List<SyncCenterUserDocument> retrieveUsers = hkUserService.retrieveUsersByCompany(loginDataBean.getCompanyId(), Boolean.TRUE, Boolean.TRUE, loginDataBean.getId());
        List<SelectItem> selectItemListFromUserList = customFieldTransformerBean.getSelectItemListFromUserList(retrieveUsers);

        return selectItemListFromUserList;
    }

    public Map<String, List<String>> issueLotsOrPackets(List<String> scannedUniqueIds, String carrierBoy, String department) {

        Map<String, List<String>> result = new HashMap<>();
        Map<String, List<ObjectId>> lotIdAndlotSlipIdMap = new HashMap<>();
        Map<String, List<ObjectId>> packetIdAndPacketSlipIdMaps = new HashMap<>();
        Map<String, List<ObjectId>> totalNoOfLotsAndPacketIds = new HashMap<>();
        List<ObjectId> lotIds = new ArrayList<>();
        List<ObjectId> packetIds = new ArrayList<>();
        List<String> missingIds = new ArrayList<>();

        Long franchise = loginDataBean.getCompanyId();
        BasicBSONObject lotBasicBSONObject = new BasicBSONObject();
        ArrayList<String> inStockOfValueList = new ArrayList<String>();
        inStockOfValueList.add(loginDataBean.getId() + ":E");

        lotBasicBSONObject.put(HkSystemConstantUtil.LotStaticFieldName.IN_STOCK_OF, inStockOfValueList);
        //System.out.println("-------lotBasicBson======" + lotBasicBSONObject);
////////////////////////////////////////////////////////////////////////////////////////
        BasicBSONObject packetBasicBSONObject = new BasicBSONObject();
        packetBasicBSONObject.put(HkSystemConstantUtil.PacketStaticFieldName.IN_STOCK_OF, inStockOfValueList);
        //System.out.println("-------packetBasicBSONObject======" + packetBasicBSONObject);

        List<HkLotDocument> lotDocuments = new ArrayList<>();
        List<HkPacketDocument> packetDocuments = new ArrayList<>();

        lotDocuments = (List<HkLotDocument>) stockService.retrieveLotsOrPacketsForIssueForEod(lotBasicBSONObject, Boolean.FALSE, franchise);
        //System.out.println("-----retrieveLotsForIssueForEod-----" + lotDocuments);
        packetDocuments = (List<HkPacketDocument>) stockService.retrieveLotsOrPacketsForIssueForEod(packetBasicBSONObject, Boolean.TRUE, franchise);
        //System.out.println("-----retrievePacketsForIssueForEod-----" + packetDocuments);
        List<ObjectId> lotids = null;
        for (HkLotDocument lotDocument : lotDocuments) {
            if (lotDocument.getFieldValue().toMap().containsKey(HkSystemConstantUtil.AutoNumber.LOT_ID) && lotDocument.getFieldValue().toMap().get(HkSystemConstantUtil.AutoNumber.LOT_ID) != null) {
                if (!lotIdAndlotSlipIdMap.containsKey(lotDocument.getFieldValue().toMap().get(HkSystemConstantUtil.AutoNumber.LOT_ID))) {
                    lotids = new ArrayList<>();
                    lotids.add(lotDocument.getId());
                    lotIdAndlotSlipIdMap.put(lotDocument.getFieldValue().toMap().get(HkSystemConstantUtil.AutoNumber.LOT_ID).toString(), lotids);
                }
            }
            if (lotDocument.getFieldValue().toMap().containsKey(HkSystemConstantUtil.AutoNumber.LOT_SLIP_ID) && lotDocument.getFieldValue().toMap().get(HkSystemConstantUtil.AutoNumber.LOT_SLIP_ID) != null) {

                if (!lotIdAndlotSlipIdMap.containsKey(lotDocument.getFieldValue().toMap().get(HkSystemConstantUtil.AutoNumber.LOT_SLIP_ID))) {
                    lotids = new ArrayList<>();
                    lotids.add(lotDocument.getId());
                    lotIdAndlotSlipIdMap.put(lotDocument.getFieldValue().toMap().get(HkSystemConstantUtil.AutoNumber.LOT_SLIP_ID).toString(), lotids);
                } else if (lotIdAndlotSlipIdMap.containsKey(lotDocument.getFieldValue().toMap().get(HkSystemConstantUtil.AutoNumber.LOT_SLIP_ID))) {
                    lotids = new ArrayList<>();
                    lotids = lotIdAndlotSlipIdMap.get(lotDocument.getFieldValue().toMap().get(HkSystemConstantUtil.AutoNumber.LOT_SLIP_ID));
                    lotids.add(lotDocument.getId());
                    lotIdAndlotSlipIdMap.put(lotDocument.getFieldValue().toMap().get(HkSystemConstantUtil.AutoNumber.LOT_SLIP_ID).toString(), lotids);
                }
            }
        }
        List<ObjectId> packetids = null;
        for (HkPacketDocument packetDocument : packetDocuments) {
            if (packetDocument.getFieldValue().toMap().containsKey(HkSystemConstantUtil.AutoNumber.PACKET_ID) && packetDocument.getFieldValue().toMap().get(HkSystemConstantUtil.AutoNumber.PACKET_ID) != null) {
                if (!packetIdAndPacketSlipIdMaps.containsKey(packetDocument.getFieldValue().toMap().get(HkSystemConstantUtil.AutoNumber.PACKET_ID))) {
                    packetids = new ArrayList<>();
                    packetids.add(packetDocument.getId());
                    packetIdAndPacketSlipIdMaps.put(packetDocument.getFieldValue().toMap().get(HkSystemConstantUtil.AutoNumber.PACKET_ID).toString(), packetids);
                }
            }
            if (packetDocument.getFieldValue().toMap().containsKey(HkSystemConstantUtil.AutoNumber.PACKET_SLIP_ID) && packetDocument.getFieldValue().toMap().get(HkSystemConstantUtil.AutoNumber.PACKET_SLIP_ID) != null) {

                if (!packetIdAndPacketSlipIdMaps.containsKey(packetDocument.getFieldValue().toMap().get(HkSystemConstantUtil.AutoNumber.PACKET_SLIP_ID))) {
                    packetids = new ArrayList<>();
                    packetids.add(packetDocument.getId());
                    packetIdAndPacketSlipIdMaps.put(packetDocument.getFieldValue().toMap().get(HkSystemConstantUtil.AutoNumber.PACKET_SLIP_ID).toString(), packetids);
                } else if (packetIdAndPacketSlipIdMaps.containsKey(packetDocument.getFieldValue().toMap().get(HkSystemConstantUtil.AutoNumber.PACKET_SLIP_ID))) {
                    packetids = new ArrayList<>();
                    packetids = packetIdAndPacketSlipIdMaps.get(packetDocument.getFieldValue().toMap().get(HkSystemConstantUtil.AutoNumber.PACKET_SLIP_ID));
                    packetids.add(packetDocument.getId());
                    packetIdAndPacketSlipIdMaps.put(packetDocument.getFieldValue().toMap().get(HkSystemConstantUtil.AutoNumber.PACKET_SLIP_ID).toString(), packetids);
                }
            }
        }

        totalNoOfLotsAndPacketIds.putAll(lotIdAndlotSlipIdMap);
        totalNoOfLotsAndPacketIds.putAll(packetIdAndPacketSlipIdMaps);
        //System.out.println("-----------------packetIdAndPacketSlipIdMaps---------------" + packetIdAndPacketSlipIdMaps);
        //System.out.println("-----------------lotIdAndlotSlipIdMap---------------" + lotIdAndlotSlipIdMap);
        //System.out.println("-----------------totalNoOfLotsAndPacketIds---------------" + totalNoOfLotsAndPacketIds);

        for (String scannedUniqueId : scannedUniqueIds) {
            if (totalNoOfLotsAndPacketIds.containsKey(scannedUniqueId)) {
                if (lotIdAndlotSlipIdMap.containsKey(scannedUniqueId) && !packetIdAndPacketSlipIdMaps.containsKey(scannedUniqueId)) {
                    lotIds.addAll(lotIdAndlotSlipIdMap.get(scannedUniqueId));
                } else if (!lotIdAndlotSlipIdMap.containsKey(scannedUniqueId) && packetIdAndPacketSlipIdMaps.containsKey(scannedUniqueId)) {
                    packetIds.addAll(packetIdAndPacketSlipIdMaps.get(scannedUniqueId));
                }

            } else if (!totalNoOfLotsAndPacketIds.containsKey(scannedUniqueId)) {
                missingIds.add(scannedUniqueId);
            }

        }
        //System.out.println("-----------------missingIds---------------" + missingIds);
        List<ObjectId> finalLotIds = new ArrayList<>(new LinkedHashSet<ObjectId>(lotIds));
        List<ObjectId> finalPacketIds = new ArrayList<>(new LinkedHashSet<ObjectId>(packetIds));

        //System.out.println("-----------------finalLotIds---------------" + finalLotIds);
        //System.out.println("-----------------finalPacketIds---------------" + finalPacketIds);
        BasicBSONObject basicBSONObject = new BasicBSONObject();
        ArrayList<String> inStockOfIssueList = new ArrayList<String>();
        inStockOfIssueList.add(loginDataBean.getId() + ":E");
        ArrayList<String> inStockOfDepartmentList = new ArrayList<String>();
        inStockOfDepartmentList.add(department);
        ArrayList<String> carrierBoyList = new ArrayList<String>();
        carrierBoyList.add(carrierBoy);

        basicBSONObject.put(HkSystemConstantUtil.IssueStaticFieldName.IN_STOCK_OF, inStockOfIssueList);
        basicBSONObject.put(HkSystemConstantUtil.IssueStaticFieldName.IN_STOCK_OF_DEPT, inStockOfDepartmentList);
        basicBSONObject.put(HkSystemConstantUtil.IssueStaticFieldName.CARRIER_BOY, carrierBoyList);
        //System.out.println("-------basicBSONObject======" + basicBSONObject);
        if (missingIds.size() > 0) {
            result.put("failure", missingIds);
            return result;
        } else if ((finalLotIds.size() > 0 || finalPacketIds.size() > 0) && missingIds.size() == 0) {
            UMFeatureDetailDataBean feature = applicationUtil.getuMFeatureDetailDataBeanMap().get(HkSystemConstantUtil.Feature.ISSUE);
            Map<String, String> dbFieldWithFormulaMap = null;
//            Commmented by Shifa temporarly
//            customFieldService.retrieveFeatureNameWithDbFieldListForFormula(feature.getId(), loginDataBean.getCompanyId(), HkSystemConstantUtil.FeatureForFormulaEvaluation.ISSUE_STOCK, HkSystemConstantUtil.CustomField.ComponentType.FORMULA);

            Boolean issueLotsOrPacketsForEod = stockService.issueLotsOrPacketsForEod(basicBSONObject, finalLotIds, finalPacketIds, loginDataBean.getId(), dbFieldWithFormulaMap, loginDataBean.getCompanyId());
            if (issueLotsOrPacketsForEod) {
                result.put("success", null);
                return result;
            }
        }
        return null;
    }

    public Map<String, List<String>> recieveLotsOrPackets(List<String> scannedUniqueIds) {
        Map<String, List<String>> result = new HashMap<>();
        Map<String, List<ObjectId>> lotIdAndlotSlipIdMap = new HashMap<>();
        Map<String, List<ObjectId>> packetIdAndPacketSlipIdMaps = new HashMap<>();
        Map<String, List<ObjectId>> totalNoOfLotsAndPacketIds = new HashMap<>();
        List<ObjectId> lotIds = new ArrayList<>();
        List<ObjectId> packetIds = new ArrayList<>();
        List<String> missingIds = new ArrayList<>();

        BasicBSONObject issueBSONObject = new BasicBSONObject();
        ArrayList<String> inStockOfDepartmentList = new ArrayList<String>();
        inStockOfDepartmentList.add(loginDataBean.getDepartment() + ":D");

        issueBSONObject.put(HkSystemConstantUtil.IssueStaticFieldName.IN_STOCK_OF_DEPT, inStockOfDepartmentList);
        //////////////////////////////
        //System.out.println("----issueBSONObject -----" + issueBSONObject);
        Long franchise = loginDataBean.getCompanyId();
        List<HkLotDocument> lotDocuments = new ArrayList<>();
        if (issueBSONObject != null) {
            lotDocuments = (List<HkLotDocument>) stockService.retrieveLotsOrPacketsForRecieveForEod(issueBSONObject, Boolean.FALSE, franchise);
            //System.out.println("-----retrieveLotsForIssueForEod-----" + lotDocuments);
        }
        List<HkPacketDocument> packetDocuments = new ArrayList<>();
        if (issueBSONObject != null) {
            packetDocuments = (List<HkPacketDocument>) stockService.retrieveLotsOrPacketsForRecieveForEod(issueBSONObject, Boolean.TRUE, franchise);
            //System.out.println("-----retrievePacketsForIssueForEod-----" + packetDocuments);
        }
        List<ObjectId> lotids = null;
        for (HkLotDocument lotDocument : lotDocuments) {
            if (lotDocument.getFieldValue().toMap().containsKey(HkSystemConstantUtil.AutoNumber.LOT_ID) && lotDocument.getFieldValue().toMap().get(HkSystemConstantUtil.AutoNumber.LOT_ID) != null) {
                if (!lotIdAndlotSlipIdMap.containsKey(lotDocument.getFieldValue().toMap().get(HkSystemConstantUtil.AutoNumber.LOT_ID))) {
                    lotids = new ArrayList<>();
                    lotids.add(lotDocument.getId());
                    lotIdAndlotSlipIdMap.put(lotDocument.getFieldValue().toMap().get(HkSystemConstantUtil.AutoNumber.LOT_ID).toString(), lotids);
                }
            }
            if (lotDocument.getFieldValue().toMap().containsKey(HkSystemConstantUtil.AutoNumber.LOT_SLIP_ID) && lotDocument.getFieldValue().toMap().get(HkSystemConstantUtil.AutoNumber.LOT_SLIP_ID) != null) {

                if (!lotIdAndlotSlipIdMap.containsKey(lotDocument.getFieldValue().toMap().get(HkSystemConstantUtil.AutoNumber.LOT_SLIP_ID))) {
                    lotids = new ArrayList<>();
                    lotids.add(lotDocument.getId());
                    lotIdAndlotSlipIdMap.put(lotDocument.getFieldValue().toMap().get(HkSystemConstantUtil.AutoNumber.LOT_SLIP_ID).toString(), lotids);
                } else if (lotIdAndlotSlipIdMap.containsKey(lotDocument.getFieldValue().toMap().get(HkSystemConstantUtil.AutoNumber.LOT_SLIP_ID))) {
                    lotids = new ArrayList<>();
                    lotids = lotIdAndlotSlipIdMap.get(lotDocument.getFieldValue().toMap().get(HkSystemConstantUtil.AutoNumber.LOT_SLIP_ID));
                    lotids.add(lotDocument.getId());
                    lotIdAndlotSlipIdMap.put(lotDocument.getFieldValue().toMap().get(HkSystemConstantUtil.AutoNumber.LOT_SLIP_ID).toString(), lotids);
                }
            }
        }
        List<ObjectId> packetids = null;
        for (HkPacketDocument packetDocument : packetDocuments) {
            if (packetDocument.getFieldValue().toMap().containsKey(HkSystemConstantUtil.AutoNumber.PACKET_ID) && packetDocument.getFieldValue().toMap().get(HkSystemConstantUtil.AutoNumber.PACKET_ID) != null) {
                if (!packetIdAndPacketSlipIdMaps.containsKey(packetDocument.getFieldValue().toMap().get(HkSystemConstantUtil.AutoNumber.PACKET_ID))) {
                    packetids = new ArrayList<>();
                    packetids.add(packetDocument.getId());
                    packetIdAndPacketSlipIdMaps.put(packetDocument.getFieldValue().toMap().get(HkSystemConstantUtil.AutoNumber.PACKET_ID).toString(), packetids);
                }
            }
            if (packetDocument.getFieldValue().toMap().containsKey(HkSystemConstantUtil.AutoNumber.PACKET_SLIP_ID) && packetDocument.getFieldValue().toMap().get(HkSystemConstantUtil.AutoNumber.PACKET_SLIP_ID) != null) {

                if (!packetIdAndPacketSlipIdMaps.containsKey(packetDocument.getFieldValue().toMap().get(HkSystemConstantUtil.AutoNumber.PACKET_SLIP_ID))) {
                    packetids = new ArrayList<>();
                    packetids.add(packetDocument.getId());
                    packetIdAndPacketSlipIdMaps.put(packetDocument.getFieldValue().toMap().get(HkSystemConstantUtil.AutoNumber.PACKET_SLIP_ID).toString(), packetids);
                } else if (packetIdAndPacketSlipIdMaps.containsKey(packetDocument.getFieldValue().toMap().get(HkSystemConstantUtil.AutoNumber.PACKET_SLIP_ID))) {
                    packetids = new ArrayList<>();
                    packetids = packetIdAndPacketSlipIdMaps.get(packetDocument.getFieldValue().toMap().get(HkSystemConstantUtil.AutoNumber.PACKET_SLIP_ID));
                    packetids.add(packetDocument.getId());
                    packetIdAndPacketSlipIdMaps.put(packetDocument.getFieldValue().toMap().get(HkSystemConstantUtil.AutoNumber.PACKET_SLIP_ID).toString(), packetids);
                }
            }
        }

        totalNoOfLotsAndPacketIds.putAll(lotIdAndlotSlipIdMap);
        totalNoOfLotsAndPacketIds.putAll(packetIdAndPacketSlipIdMaps);
        //System.out.println("-----------------packetIdAndPacketSlipIdMaps---------------" + packetIdAndPacketSlipIdMaps);
        //System.out.println("-----------------lotIdAndlotSlipIdMap---------------" + lotIdAndlotSlipIdMap);
        //System.out.println("-----------------totalNoOfLotsAndPacketIds---------------" + totalNoOfLotsAndPacketIds);

        for (String scannedUniqueId : scannedUniqueIds) {
            if (totalNoOfLotsAndPacketIds.containsKey(scannedUniqueId)) {
                if (lotIdAndlotSlipIdMap.containsKey(scannedUniqueId) && !packetIdAndPacketSlipIdMaps.containsKey(scannedUniqueId)) {
                    lotIds.addAll(lotIdAndlotSlipIdMap.get(scannedUniqueId));
                } else if (!lotIdAndlotSlipIdMap.containsKey(scannedUniqueId) && packetIdAndPacketSlipIdMaps.containsKey(scannedUniqueId)) {
                    packetIds.addAll(packetIdAndPacketSlipIdMaps.get(scannedUniqueId));
                }

            } else if (!totalNoOfLotsAndPacketIds.containsKey(scannedUniqueId)) {
                missingIds.add(scannedUniqueId);
            }

        }
        //System.out.println("-----------------missingIds---------------" + missingIds);
        List<ObjectId> finalLotIds = new ArrayList<>(new LinkedHashSet<ObjectId>(lotIds));
        List<ObjectId> finalPacketIds = new ArrayList<>(new LinkedHashSet<ObjectId>(packetIds));

        //System.out.println("-----------------finalLotIds---------------" + finalLotIds);
        //System.out.println("-----------------finalPacketIds---------------" + finalPacketIds);

        if (missingIds.size() > 0) {
            result.put("failure", missingIds);
            return result;
        } else if ((finalLotIds.size() > 0 || finalPacketIds.size() > 0) && missingIds.size() == 0) {
            List<HkIssueDocument> hkIssueDocuments = new ArrayList<>();
            if (!CollectionUtils.isEmpty(finalLotIds)) {
                List<HkLotDocument> hkLotDocuments = stockService.retrieveLotsByIds(finalLotIds, franchise, Boolean.FALSE, null, null,Boolean.TRUE);
                for (HkLotDocument hkLotDocument : hkLotDocuments) {
                    hkIssueDocuments.add(hkLotDocument.getEodIssueDocument());
                }
            }
            if (!CollectionUtils.isEmpty(finalPacketIds)) {
                List<HkPacketDocument> hkPacketDocuments = stockService.retrievePacketsByIds(finalPacketIds, franchise, Boolean.FALSE, null,Boolean.TRUE);
                for (HkPacketDocument hkPacketDocument : hkPacketDocuments) {
                    hkIssueDocuments.add(hkPacketDocument.getEodIssueDocument());
                }
            }
            stockService.receiveLotsOrPacketsForEod(hkIssueDocuments, franchise);
            result.put("success", null);
            return result;
        }
        return null;
    }

    public Map<String, List<String>> returnLotsOrPackets(List<String> scannedUniqueIds) {
        Map<String, List<String>> result = new HashMap<>();
        Map<String, List<ObjectId>> lotIdAndlotSlipIdMap = new HashMap<>();
        Map<String, List<ObjectId>> packetIdAndPacketSlipIdMaps = new HashMap<>();
        Map<String, List<ObjectId>> totalNoOfLotsAndPacketIds = new HashMap<>();
        List<ObjectId> lotIds = new ArrayList<>();
        List<ObjectId> packetIds = new ArrayList<>();
        List<String> missingIds = new ArrayList<>();

        Long franchise = loginDataBean.getCompanyId();
        List<HkLotDocument> lotDocuments = new ArrayList<>();
        lotDocuments = (List<HkLotDocument>) stockService.retrieveLotsOrPacketsForReturnForEod(loginDataBean.getId(), Boolean.FALSE, franchise);
        //System.out.println("-----retrieveLotsForIssueForEod-----" + lotDocuments);
        List<HkPacketDocument> packetDocuments = new ArrayList<>();
        packetDocuments = (List<HkPacketDocument>) stockService.retrieveLotsOrPacketsForReturnForEod(loginDataBean.getId(), Boolean.TRUE, franchise);
        //System.out.println("-----retrievePacketsForIssueForEod-----" + packetDocuments);

        List<ObjectId> lotids = null;
        for (HkLotDocument lotDocument : lotDocuments) {
            if (lotDocument.getFieldValue().toMap().containsKey(HkSystemConstantUtil.AutoNumber.LOT_ID) && lotDocument.getFieldValue().toMap().get(HkSystemConstantUtil.AutoNumber.LOT_ID) != null) {
                if (!lotIdAndlotSlipIdMap.containsKey(lotDocument.getFieldValue().toMap().get(HkSystemConstantUtil.AutoNumber.LOT_ID))) {
                    lotids = new ArrayList<>();
                    lotids.add(lotDocument.getId());
                    lotIdAndlotSlipIdMap.put(lotDocument.getFieldValue().toMap().get(HkSystemConstantUtil.AutoNumber.LOT_ID).toString(), lotids);
                }
            }
            if (lotDocument.getFieldValue().toMap().containsKey(HkSystemConstantUtil.AutoNumber.LOT_SLIP_ID) && lotDocument.getFieldValue().toMap().get(HkSystemConstantUtil.AutoNumber.LOT_SLIP_ID) != null) {

                if (!lotIdAndlotSlipIdMap.containsKey(lotDocument.getFieldValue().toMap().get(HkSystemConstantUtil.AutoNumber.LOT_SLIP_ID))) {
                    lotids = new ArrayList<>();
                    lotids.add(lotDocument.getId());
                    lotIdAndlotSlipIdMap.put(lotDocument.getFieldValue().toMap().get(HkSystemConstantUtil.AutoNumber.LOT_SLIP_ID).toString(), lotids);
                } else if (lotIdAndlotSlipIdMap.containsKey(lotDocument.getFieldValue().toMap().get(HkSystemConstantUtil.AutoNumber.LOT_SLIP_ID))) {
                    lotids = new ArrayList<>();
                    lotids = lotIdAndlotSlipIdMap.get(lotDocument.getFieldValue().toMap().get(HkSystemConstantUtil.AutoNumber.LOT_SLIP_ID));
                    lotids.add(lotDocument.getId());
                    lotIdAndlotSlipIdMap.put(lotDocument.getFieldValue().toMap().get(HkSystemConstantUtil.AutoNumber.LOT_SLIP_ID).toString(), lotids);
                }
            }
        }
        List<ObjectId> packetids = null;
        for (HkPacketDocument packetDocument : packetDocuments) {
            if (packetDocument.getFieldValue().toMap().containsKey(HkSystemConstantUtil.AutoNumber.PACKET_ID) && packetDocument.getFieldValue().toMap().get(HkSystemConstantUtil.AutoNumber.PACKET_ID) != null) {
                if (!packetIdAndPacketSlipIdMaps.containsKey(packetDocument.getFieldValue().toMap().get(HkSystemConstantUtil.AutoNumber.PACKET_ID))) {
                    packetids = new ArrayList<>();
                    packetids.add(packetDocument.getId());
                    packetIdAndPacketSlipIdMaps.put(packetDocument.getFieldValue().toMap().get(HkSystemConstantUtil.AutoNumber.PACKET_ID).toString(), packetids);
                }
            }
            if (packetDocument.getFieldValue().toMap().containsKey(HkSystemConstantUtil.AutoNumber.PACKET_SLIP_ID) && packetDocument.getFieldValue().toMap().get(HkSystemConstantUtil.AutoNumber.PACKET_SLIP_ID) != null) {

                if (!packetIdAndPacketSlipIdMaps.containsKey(packetDocument.getFieldValue().toMap().get(HkSystemConstantUtil.AutoNumber.PACKET_SLIP_ID))) {
                    packetids = new ArrayList<>();
                    packetids.add(packetDocument.getId());
                    packetIdAndPacketSlipIdMaps.put(packetDocument.getFieldValue().toMap().get(HkSystemConstantUtil.AutoNumber.PACKET_SLIP_ID).toString(), packetids);
                } else if (packetIdAndPacketSlipIdMaps.containsKey(packetDocument.getFieldValue().toMap().get(HkSystemConstantUtil.AutoNumber.PACKET_SLIP_ID))) {
                    packetids = new ArrayList<>();
                    packetids = packetIdAndPacketSlipIdMaps.get(packetDocument.getFieldValue().toMap().get(HkSystemConstantUtil.AutoNumber.PACKET_SLIP_ID));
                    packetids.add(packetDocument.getId());
                    packetIdAndPacketSlipIdMaps.put(packetDocument.getFieldValue().toMap().get(HkSystemConstantUtil.AutoNumber.PACKET_SLIP_ID).toString(), packetids);
                }
            }
        }

        totalNoOfLotsAndPacketIds.putAll(lotIdAndlotSlipIdMap);
        totalNoOfLotsAndPacketIds.putAll(packetIdAndPacketSlipIdMaps);
        //System.out.println("-----------------packetIdAndPacketSlipIdMaps---------------" + packetIdAndPacketSlipIdMaps);
        //System.out.println("-----------------lotIdAndlotSlipIdMap---------------" + lotIdAndlotSlipIdMap);
        //System.out.println("-----------------totalNoOfLotsAndPacketIds---------------" + totalNoOfLotsAndPacketIds);

        for (String scannedUniqueId : scannedUniqueIds) {
            if (totalNoOfLotsAndPacketIds.containsKey(scannedUniqueId)) {
                if (lotIdAndlotSlipIdMap.containsKey(scannedUniqueId) && !packetIdAndPacketSlipIdMaps.containsKey(scannedUniqueId)) {
                    lotIds.addAll(lotIdAndlotSlipIdMap.get(scannedUniqueId));
                } else if (!lotIdAndlotSlipIdMap.containsKey(scannedUniqueId) && packetIdAndPacketSlipIdMaps.containsKey(scannedUniqueId)) {
                    packetIds.addAll(packetIdAndPacketSlipIdMaps.get(scannedUniqueId));
                }

            } else if (!totalNoOfLotsAndPacketIds.containsKey(scannedUniqueId)) {
                missingIds.add(scannedUniqueId);
            }

        }
        //System.out.println("-----------------missingIds---------------" + missingIds);
        List<ObjectId> finalLotIds = new ArrayList<>(new LinkedHashSet<ObjectId>(lotIds));
        List<ObjectId> finalPacketIds = new ArrayList<>(new LinkedHashSet<ObjectId>(packetIds));

        //System.out.println("-----------------finalLotIds---------------" + finalLotIds);
        //System.out.println("-----------------finalPacketIds---------------" + finalPacketIds);

        if (missingIds.size() > 0) {
            result.put("failure", missingIds);
            return result;
        } else if ((finalLotIds.size() > 0 || finalPacketIds.size() > 0) && missingIds.size() == 0) {
            List<HkIssueDocument> hkIssueDocuments = new ArrayList<>();
            if (!CollectionUtils.isEmpty(finalLotIds)) {
                List<HkLotDocument> hkLotDocuments = stockService.retrieveLotsByIds(finalLotIds, franchise, Boolean.FALSE, null, null,Boolean.TRUE);
                for (HkLotDocument hkLotDocument : hkLotDocuments) {
                    hkIssueDocuments.add(hkLotDocument.getEodIssueDocument());
                }
            }
            if (!CollectionUtils.isEmpty(finalPacketIds)) {
                List<HkPacketDocument> hkPacketDocuments = stockService.retrievePacketsByIds(finalPacketIds, franchise, Boolean.FALSE, null,Boolean.TRUE);
                for (HkPacketDocument hkPacketDocument : hkPacketDocuments) {
                    hkIssueDocuments.add(hkPacketDocument.getEodIssueDocument());
                }
            }
            stockService.returnLotsOrPacketsForEod(hkIssueDocuments, franchise);
            result.put("success", null);
            return result;
        }

        return null;
    }


    public List<SelectItem> retrieveUsers(String user) {
        Long companyId = loginDataBean.getCompanyId();
        List<SelectItem> hkSelectItems = new ArrayList<>();
        HkSystemConfigurationDocument hkSystemConfigurationDocument = stockService.retrieveSystemConfig(HkSystemConstantUtil.FranchiseConfiguration.CARRIER_BOY_DESIGNATION);
        if (hkSystemConfigurationDocument != null) {
            String design = hkSystemConfigurationDocument.getKeyValue();
            if (design != null) {
                List<SyncCenterUserDocument> centerUserDocuments = hkUserService.retrieveUsersByActivityNodeDesignation(Long.parseLong(design), user);
                hkSelectItems = this.getSelectItemListFromUserList(centerUserDocuments);
            }
        }
        return hkSelectItems;
    }

    public List<SelectItem> getSelectItemListFromUserList(List<SyncCenterUserDocument> centerUserDocuments) {
        List<SelectItem> hkSelectItems = new ArrayList<>();
        if (!CollectionUtils.isEmpty(centerUserDocuments)) {
            for (SyncCenterUserDocument syncCenterUserDocument : centerUserDocuments) {
                StringBuilder name = new StringBuilder();
                if (syncCenterUserDocument.getUserCode() != null) {
                    name.append(syncCenterUserDocument.getUserCode()).append(" - ");
                }
                name.append(syncCenterUserDocument.getFirstName());
                if (syncCenterUserDocument.getLastName() != null) {
                    name.append(" ").append(syncCenterUserDocument.getLastName());
                }
                hkSelectItems.add(new SelectItem(syncCenterUserDocument.getId(),name.toString(),HkSystemConstantUtil.RecipientCodeType.EMPLOYEE));
            }
        }
        return hkSelectItems;
    }

}
