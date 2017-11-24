package com.argusoft.hkg.web.center.stock.transformers;

import com.argusoft.generic.database.exception.GenericDatabaseException;
import com.argusoft.hkg.common.constantutil.HkSystemConstantUtil;
import com.argusoft.hkg.nosql.core.HkCustomFieldService;
import com.argusoft.hkg.nosql.core.HkFeatureService;
import com.argusoft.hkg.nosql.core.HkFoundationDocumentService;
import com.argusoft.hkg.nosql.core.HkStockService;
import com.argusoft.hkg.nosql.model.HkInvoiceDocument;
import com.argusoft.hkg.nosql.model.HkLotDocument;
import com.argusoft.hkg.nosql.model.HkPacketDocument;
import com.argusoft.hkg.nosql.model.HkParcelDocument;
import com.argusoft.hkg.sync.center.core.HkCaratRangeService;
import com.argusoft.hkg.sync.center.core.HkSystemConfigurationService;
import com.argusoft.hkg.web.center.stock.databeans.DynamicServiceInitDataBean;
import com.argusoft.hkg.web.center.stock.databeans.EditValueDataBean;
import com.argusoft.hkg.web.center.stock.databeans.StockDataBean;
import com.argusoft.hkg.web.center.transformers.CenterCustomFieldTransformer;
import com.argusoft.hkg.web.center.usermanagement.databeans.LoginDataBean;
import com.argusoft.hkg.web.util.SelectItem;
import com.argusoft.sync.center.model.SyncCenterFeatureDocument;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bson.BasicBSONObject;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 *
 * @author rajkumar
 */
@Service
public class StockTransformer {

    @Autowired
    CenterCustomFieldTransformer customFieldTransformerBean;

    @Autowired
    LoginDataBean loginDataBean;

    @Autowired
    HkCustomFieldService customFieldService;

    @Autowired
    HkStockService stockService;

    @Autowired
    HkSystemConfigurationService systemConfigurationService;

    @Autowired
    HkFeatureService featureService;

    @Autowired
    HkFoundationDocumentService foundationDocumentService;

    @Autowired
    HkCaratRangeService caratRangeService;

    public StockDataBean retrieveSearchedLotsAndPackets(Boolean isMerge) throws GenericDatabaseException {

        List<String> listOfModel = new ArrayList<>();
        List<Map<Object, Object>> listOfTableHeader = new ArrayList<>();
        List<ObjectId> invoiceIds = new ArrayList<>();
        List<HkLotDocument> lotDocuments = new ArrayList<>();
        List<HkPacketDocument> packetDocuments = new ArrayList<>();
        List<ObjectId> parcelIds = new ArrayList<>();
        List<ObjectId> lotIds = new ArrayList<>();
        List<Long> forUserIds = new ArrayList<>();
        List<String> statusList = new ArrayList<>();
        List<String> serviceCodeList = new ArrayList<>();
        List<SelectItem> selectItems = new ArrayList<>();
        List<Map<Object, Object>> featureMapGeneralSection = new ArrayList<>();
        Map<String, Object> featureMap = new HashMap<>();
        if (isMerge) {
            featureMap = customFieldTransformerBean.retrieveCustomFieldTemplateBySearch("stockmerge");
        } else {
            featureMap = customFieldTransformerBean.retrieveCustomFieldTemplateBySearch("stocksplit");
        }
        if (featureMap != null) {
            featureMapGeneralSection = (List<Map<Object, Object>>) featureMap.get("genralSection");
            for (Map<Object, Object> feature : featureMapGeneralSection) {
                Map<Object, Object> tableHeder = new HashMap<>();
                listOfModel.add((String) feature.get("model"));
                tableHeder.put("name", feature.get("model"));
                tableHeder.put("displayName", feature.get("label"));
                listOfTableHeader.add(tableHeder);
            }
        }
        Long franchise = loginDataBean.getCompanyId();

        forUserIds.add(loginDataBean.getId());
        statusList.add(HkSystemConstantUtil.ActivityServiceStatus.IN_PROCESS);
        if (isMerge) {
            serviceCodeList.add(HkSystemConstantUtil.SERVICE_CODE.MERGE_STOCK);
        } else {
            serviceCodeList.add(HkSystemConstantUtil.SERVICE_CODE.SPLIT_STOCK);
        }
//        if (!CollectionUtils.isEmpty(forUserIds) || !CollectionUtils.isEmpty(statusList) || !CollectionUtils.isEmpty(serviceCodeList)) {
//            lotDocuments = hkWorkAllotmentService.retrieveLotsOrPacketsFromWorkAllocation(null, forUserIds, null, statusList, serviceCodeList, franchise, Boolean.FALSE, Boolean.TRUE);
//            packetDocuments = hkWorkAllotmentService.retrieveLotsOrPacketsFromWorkAllocation(null, forUserIds, null, statusList, serviceCodeList, franchise, Boolean.TRUE, Boolean.TRUE);
//        }

        if (!CollectionUtils.isEmpty(lotDocuments)) {

            for (HkLotDocument lotDocument : lotDocuments) {
                invoiceIds.add(lotDocument.getInvoice());
                parcelIds.add(lotDocument.getParcel());
            }
            List<HkInvoiceDocument> hkInvoiceDocuments = stockService.retrieveInvoices(invoiceIds, franchise, Boolean.FALSE);
            List<HkParcelDocument> hkParcelDocuments = stockService.retrieveParcels(parcelIds, franchise, Boolean.FALSE);
            if (!CollectionUtils.isEmpty(hkInvoiceDocuments) && !CollectionUtils.isEmpty(hkParcelDocuments)) {
                for (HkLotDocument lotDocument : lotDocuments) {
                    Map<String, Object> map = new HashMap<>();

                    SelectItem selectItem = new SelectItem(null, lotDocument.getParcel().toString(), lotDocument.getInvoice().toString(), lotDocument.getId().toString());

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
                    Map<String, String> uiFieldListWithComponentTypeForLot = customFieldService.retrieveUIFieldNameWithComponentTypes(uiFieldListForLot);
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

                    for (String model : listOfModel) {
                        if (oldlotDocument.getFieldValue().toMap().containsKey(model)) {
                            map.put(model, oldlotDocument.getFieldValue().toMap().get(model));
                        }
                    }
                    for (HkInvoiceDocument hkInvoiceDocument : hkInvoiceDocuments) {
                        for (String model : listOfModel) {
                            if (hkInvoiceDocument.getFieldValue().toMap().containsKey(model)) {
                                map.put(model, hkInvoiceDocument.getFieldValue().toMap().get(model));
                            }
                        }
                    }

                    List uiFieldListForParcel = new ArrayList<>();
                    for (HkParcelDocument parcelDocument : hkParcelDocuments) {
                        if (parcelDocument.getFieldValue() != null) {
                            Map<String, Object> map1 = parcelDocument.getFieldValue().toMap();
                            if (!CollectionUtils.isEmpty(map1)) {
                                for (Map.Entry<String, Object> entrySet : map1.entrySet()) {
                                    String key = entrySet.getKey();
                                    uiFieldListForParcel.add(key);
                                }
                            }
                        }
                    }

                    Map<String, String> uiFieldListWithComponentTypeForParcel = customFieldService.retrieveUIFieldNameWithComponentTypes(uiFieldListForParcel);
                    for (HkParcelDocument hkParcelDocument : hkParcelDocuments) {
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

                        if (hkParcelDocument.getId().toString().equals(lotDocument.getParcel().toString())) {
                            for (String model : listOfModel) {
                                if (oldparcelDocument.getFieldValue().toMap().containsKey(model)) {
                                    map.put(model, oldparcelDocument.getFieldValue().toMap().get(model));
                                }
                            }
                            selectItem.setParcelId(hkParcelDocument.getId().toString());
                            break;
                        }
                    }
                    selectItem.setCategoryCustom(map);
                    selectItems.add(selectItem);
                }

            }
        }
        if (!CollectionUtils.isEmpty(packetDocuments)) {
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
            List<HkLotDocument> hkLotDocuments = stockService.retrieveLotsByIds(lotIds, franchise, Boolean.FALSE, null, null, Boolean.TRUE);

            if (!CollectionUtils.isEmpty(hkInvoiceDocuments) && !CollectionUtils.isEmpty(hkParcelDocuments) && !CollectionUtils.isEmpty(hkLotDocuments)) {
                for (HkPacketDocument packetDocument : packetDocuments) {
                    Map<String, Object> map = new HashMap<>();

                    SelectItem selectItem = new SelectItem(null, packetDocument.getParcel().toString(), packetDocument.getInvoice().toString(), packetDocument.getLot().toString());

                    List uiFieldListForPacket = new ArrayList<>();
                    if (packetDocument.getFieldValue() != null) {
                        Map<String, Object> map1 = packetDocument.getFieldValue().toMap();
                        if (!CollectionUtils.isEmpty(map1)) {
                            for (Map.Entry<String, Object> entrySet : map1.entrySet()) {
                                String key = entrySet.getKey();
                                uiFieldListForPacket.add(key);
                            }
                        }
                    }
                    Map<String, String> uiFieldListWithComponentTypeForPacket = customFieldService.retrieveUIFieldNameWithComponentTypes(uiFieldListForPacket);
                    String pointerComponentType = HkSystemConstantUtil.CustomField.ComponentType.POINTER + "@*" + HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN;

                    HkPacketDocument oldpacketDocument = packetDocument;
                    Map<String, Object> customMapforPacket = packetDocument.getFieldValue().toMap();

                    if (!CollectionUtils.isEmpty(uiFieldListWithComponentTypeForPacket)) {
                        for (Map.Entry<String, String> field : uiFieldListWithComponentTypeForPacket.entrySet()) {
                            if (field.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN) || field.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.USER_MULTISELECT) || field.getValue().equals(pointerComponentType)) {
                                if (customMapforPacket != null && !customMapforPacket.isEmpty() && customMapforPacket.containsKey(field.getKey())) {
                                    String value = customMapforPacket.get(field.getKey()).toString().replaceAll("\\[", "").replaceAll("\\]", "");
                                    customMapforPacket.put(field.getKey(), value);
                                }
                            }
                        }
                        oldpacketDocument.getFieldValue().putAll(customMapforPacket);
                    }

                    for (String model : listOfModel) {
                        if (oldpacketDocument.getFieldValue().toMap().containsKey(model)) {
                            map.put(model, oldpacketDocument.getFieldValue().toMap().get(model));
                        }
                    }
                    for (HkInvoiceDocument hkInvoiceDocument : hkInvoiceDocuments) {
                        if (hkInvoiceDocument.getId().toString().equals(packetDocument.getInvoice().toString())) {
                            for (String model : listOfModel) {
                                if (hkInvoiceDocument.getFieldValue().toMap().containsKey(model)) {
                                    map.put(model, hkInvoiceDocument.getFieldValue().toMap().get(model));
                                }
                            }
                            break;
                        }
                    }

                    List uiFieldListForParcel = new ArrayList<>();
                    for (HkParcelDocument parcelDocument : hkParcelDocuments) {
                        if (parcelDocument.getFieldValue() != null) {
                            Map<String, Object> map1 = parcelDocument.getFieldValue().toMap();
                            if (!CollectionUtils.isEmpty(map1)) {
                                for (Map.Entry<String, Object> entrySet : map1.entrySet()) {
                                    String key = entrySet.getKey();
                                    uiFieldListForParcel.add(key);
                                }
                            }
                        }
                    }

                    Map<String, String> uiFieldListWithComponentTypeForParcel = customFieldService.retrieveUIFieldNameWithComponentTypes(uiFieldListForParcel);
                    for (HkParcelDocument hkParcelDocument : hkParcelDocuments) {
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

                        if (hkParcelDocument.getId().toString().equals(packetDocument.getParcel().toString())) {
                            for (String model : listOfModel) {
                                if (oldparcelDocument.getFieldValue().toMap().containsKey(model)) {
                                    map.put(model, oldparcelDocument.getFieldValue().toMap().get(model));
                                }
                            }
                            break;
                        }
                    }

                    List uiFieldListForLot = new ArrayList<>();
                    for (HkLotDocument lotDocument : hkLotDocuments) {
                        if (lotDocument.getFieldValue() != null) {
                            Map<String, Object> map1 = lotDocument.getFieldValue().toMap();
                            if (!CollectionUtils.isEmpty(map1)) {
                                for (Map.Entry<String, Object> entrySet : map1.entrySet()) {
                                    String key = entrySet.getKey();
                                    uiFieldListForLot.add(key);
                                }
                            }
                        }
                    }
                    Map<String, String> uiFieldListWithComponentTypeForLot = customFieldService.retrieveUIFieldNameWithComponentTypes(uiFieldListForLot);
                    for (HkLotDocument hkLotDocument : hkLotDocuments) {
                        HkLotDocument oldlotDocument = hkLotDocument;
                        Map<String, Object> customMap = hkLotDocument.getFieldValue().toMap();

                        if (!CollectionUtils.isEmpty(uiFieldListWithComponentTypeForLot)) {
                            for (Map.Entry<String, String> field : uiFieldListWithComponentTypeForLot.entrySet()) {
                                if (field.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN) || field.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.USER_MULTISELECT) || field.getValue().equals(pointerComponentType)) {
                                    if (customMap != null && !customMap.isEmpty() && customMap.containsKey(field.getKey())) {
                                        String value = customMap.get(field.getKey()).toString().replaceAll("\\[", "").replaceAll("\\]", "");
                                        customMap.put(field.getKey(), value);
                                    }
                                }
                            }
                            oldlotDocument.getFieldValue().putAll(customMap);
                        }

                        if (hkLotDocument.getId().toString().equals(packetDocument.getParcel().toString())) {
                            for (String model : listOfModel) {
                                if (oldlotDocument.getFieldValue().toMap().containsKey(model)) {
                                    map.put(model, oldlotDocument.getFieldValue().toMap().get(model));
                                }
                            }
                            selectItem.setLotId(hkLotDocument.getFieldValue().toMap().get("lotID").toString());
                            break;
                        }
                    }
                    selectItem.setCategoryCustom(map);
                    //If packet than store packet ID in status field
                    selectItem.setStatus(packetDocument.getId().toString());
                    selectItems.add(selectItem);
                }

            }
        }
        return new StockDataBean(listOfTableHeader, selectItems, null, featureMapGeneralSection);
    }

    public Object mergeStock(StockDataBean stockDataBean) {
        if (stockDataBean != null) {
            List<ObjectId> workAllotmentIds = new ArrayList<>();
            for (String string : stockDataBean.getAllotmentIds()) {
                workAllotmentIds.add(new ObjectId(string));
            }
            ObjectId parent = new ObjectId(stockDataBean.getParentID());

            if (stockDataBean.getType().equals("Lot")) {
//                code added by siddhu
                List<String> uiList = null;
                Map<String, String> stockDbType = stockDataBean.getStockDbType();
                if (!CollectionUtils.isEmpty(stockDbType)) {
                    uiList = new ArrayList<>(stockDbType.keySet());
                }
                Map<String, String> uIFieldNameWithComponentTypes = customFieldService.retrieveUIFieldNameWithComponentTypes(uiList);
                String pointerComponentType = HkSystemConstantUtil.CustomField.ComponentType.POINTER + "@*" + HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN;
                Map<String, Object> stockCustom = stockDataBean.getStockCustom();
                if (!CollectionUtils.isEmpty(stockCustom)) {
                    for (Map.Entry<String, Object> entry : stockCustom.entrySet()) {
                        String key = entry.getKey();
                        Object value = entry.getValue();
                        if (key != null && uIFieldNameWithComponentTypes.containsKey(key)) {
                            String type = uIFieldNameWithComponentTypes.get(key);
                            if (type.equals(HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN) || type.equals(HkSystemConstantUtil.CustomField.ComponentType.USER_MULTISELECT) || type.equals(pointerComponentType)) {
                                if (stockDbType.containsKey(key)) {
                                    stockDbType.put(key, HkSystemConstantUtil.CustomField.DbFieldType.ARRAY);
                                }

                                if (stockCustom.containsKey(key)) {
                                    String customVal = stockCustom.get(key).toString();
                                    List<String> values = new ArrayList<>();
                                    String[] valueArray = customVal.replace("\"", "").split(",");
                                    for (String v : valueArray) {
                                        values.add(v.replace("\"", ""));
                                    }
                                    stockCustom.put(key, values);
                                }
                            }
                        }

                    }
                }
                // code ends here
                BasicBSONObject bSONObject = customFieldService.makeBSONObject(stockCustom, stockDbType, HkSystemConstantUtil.Feature.LOT, loginDataBean.getCompanyId(), null, null);
                List<ObjectId> idsTomerge = new ArrayList<>();
                for (String idToMerge : stockDataBean.getIdsToMerge()) {
                    idsTomerge.add(new ObjectId(idToMerge));
                }
                String response = stockService.mergeLot(parent, idsTomerge, Arrays.asList(bSONObject), loginDataBean.getCompanyId(), loginDataBean.getId(), workAllotmentIds);
                if (!StringUtils.isEmpty(response)) {
                    return response;
                } else {
                    return null;
                }
            } else if (stockDataBean.getType().equals("Packet")) {
                //                code added by siddhu
                List<String> uiList = null;
                Map<String, String> stockDbType = stockDataBean.getStockDbType();
                if (!CollectionUtils.isEmpty(stockDbType)) {
                    uiList = new ArrayList<>(stockDbType.keySet());
                }
                Map<String, String> uIFieldNameWithComponentTypes = customFieldService.retrieveUIFieldNameWithComponentTypes(uiList);
                String pointerComponentType = HkSystemConstantUtil.CustomField.ComponentType.POINTER + "@*" + HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN;
                Map<String, Object> stockCustom = stockDataBean.getStockCustom();
                if (!CollectionUtils.isEmpty(stockCustom)) {
                    for (Map.Entry<String, Object> entry : stockCustom.entrySet()) {
                        String key = entry.getKey();
                        Object value = entry.getValue();
                        if (key != null && uIFieldNameWithComponentTypes.containsKey(key)) {
                            String type = uIFieldNameWithComponentTypes.get(key);
                            if (type.equals(HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN) || type.equals(HkSystemConstantUtil.CustomField.ComponentType.USER_MULTISELECT) || type.equals(pointerComponentType)) {
                                if (stockDbType.containsKey(key)) {
                                    stockDbType.put(key, HkSystemConstantUtil.CustomField.DbFieldType.ARRAY);
                                }

                                if (stockCustom.containsKey(key)) {
                                    String customVal = stockCustom.get(key).toString();
                                    List<String> values = new ArrayList<>();
                                    String[] valueArray = customVal.replace("\"", "").split(",");
                                    for (String v : valueArray) {
                                        values.add(v.replace("\"", ""));
                                    }
                                    stockCustom.put(key, values);
                                }
                            }
                        }

                    }
                }
                // code ends here
                BasicBSONObject bSONObject = customFieldService.makeBSONObject(stockCustom, stockDbType, HkSystemConstantUtil.Feature.PACKET, loginDataBean.getCompanyId(), null, null);
                List<ObjectId> idsTomerge = new ArrayList<>();
                for (String idToMerge : stockDataBean.getIdsToMerge()) {
                    idsTomerge.add(new ObjectId(idToMerge));
                }
                return stockService.mergePacket(parent, idsTomerge, Arrays.asList(bSONObject), null, loginDataBean.getCompanyId(), loginDataBean.getId(), workAllotmentIds);
            }
        }
        return null;
    }

    public List<String> splitStock(StockDataBean stockDataBean) {
        if (stockDataBean != null) {
            ObjectId workAllotmentId = new ObjectId();
            if (!CollectionUtils.isEmpty(stockDataBean.getAllotmentIds())) {
                workAllotmentId = new ObjectId(stockDataBean.getAllotmentIds().get(0));
            }
            ObjectId parent = new ObjectId(stockDataBean.getParentID());
            ObjectId idTosplit = new ObjectId(stockDataBean.getId());
            if (stockDataBean.getType().equals("Lot")) {
                List<BasicBSONObject> datas = new ArrayList<>();
                for (Map<String, Object> map : stockDataBean.getStockDataForSplit()) {
                    //                code added by siddhu
                    List<String> uiList = null;
                    Map<String, String> stockDbType = stockDataBean.getStockDbType();
                    if (!CollectionUtils.isEmpty(stockDbType)) {
                        uiList = new ArrayList<>(stockDbType.keySet());
                    }
                    Map<String, String> uIFieldNameWithComponentTypes = customFieldService.retrieveUIFieldNameWithComponentTypes(uiList);
                    String pointerComponentType = HkSystemConstantUtil.CustomField.ComponentType.POINTER + "@*" + HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN;
                    Map<String, Object> stockCustom = map;
                    if (!CollectionUtils.isEmpty(stockCustom)) {
                        for (Map.Entry<String, Object> entry : stockCustom.entrySet()) {
                            String key = entry.getKey();
                            Object value = entry.getValue();
                            if (key != null && uIFieldNameWithComponentTypes.containsKey(key)) {
                                String type = uIFieldNameWithComponentTypes.get(key);
                                if (type.equals(HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN) || type.equals(HkSystemConstantUtil.CustomField.ComponentType.USER_MULTISELECT) || type.equals(pointerComponentType)) {
                                    if (stockDbType.containsKey(key)) {
                                        stockDbType.put(key, HkSystemConstantUtil.CustomField.DbFieldType.ARRAY);
                                    }

                                    if (stockCustom.containsKey(key)) {
                                        String customVal = stockCustom.get(key).toString();
                                        List<String> values = new ArrayList<>();
                                        String[] valueArray = customVal.replace("\"", "").split(",");
                                        for (String v : valueArray) {
                                            values.add(v.replace("\"", ""));
                                        }
                                        stockCustom.put(key, values);
                                    }
                                }
                            }

                        }
                    }
                    // code ends here
                    BasicBSONObject bSONObject = new BasicBSONObject();
                    bSONObject = customFieldService.makeBSONObject(stockCustom, stockDbType, HkSystemConstantUtil.Feature.LOT, loginDataBean.getCompanyId(), null, null);
                    datas.add(bSONObject);
                }
                List<String> splitLot = stockService.splitLot(parent, idTosplit, datas, loginDataBean.getCompanyId(), loginDataBean.getId(), workAllotmentId);
//                if (!CollectionUtils.isEmpty(splitLot)) {
                return splitLot;
//                } else {
//                    return null;
//                }
            } else if (stockDataBean.getType().equals("Packet")) {
                System.out.println("stockDataBean getStockDataForSplit::: " + stockDataBean.getStockDataForSplit());
                System.out.println("stockDataBean getParentID::: " + stockDataBean.getParentID());
                System.out.println("stockDataBean getId()::: " + stockDataBean.getId());
                List<BasicBSONObject> datas = new ArrayList<>();
                Map<String, String> stockDbType = stockDataBean.getStockDbType();
                for (Map<String, Object> map : stockDataBean.getStockDataForSplit()) {
                    if (!CollectionUtils.isEmpty(map)) {
                        BasicBSONObject bSONObject = new BasicBSONObject();
                        //                code added by siddhu
                        List<String> uiList = null;
                        System.out.println("map ::" + map);
                        map.remove(HkSystemConstantUtil.PacketStaticFieldName.PACKET_ID);
                        System.out.println("stockDataBean.getDatabeanOfPacket()" + stockDataBean.getDatabeanOfPacket());
                        if (stockDataBean.getDatabeanOfPacket() != null) {
                            Map<String, Object> packetCustom = stockDataBean.getDatabeanOfPacket().getPacketCustom();
                            if (!CollectionUtils.isEmpty(packetCustom)) {
                                Object id = packetCustom.get(HkSystemConstantUtil.PacketStaticFieldName.PACKET_ID);
                                System.out.println("id " + id);
                                if (id != null) {
                                    String packetId = id.toString();
                                    String finalPacketId = null;
                                    String leadingString = null;
                                    System.out.println("packetId.substring(packetId.lastIndexOf(\"-\") + 1, packetId.length()).length()" + packetId.substring(packetId.lastIndexOf("-") + 1, packetId.length()).length());
                                    if (packetId.substring(packetId.lastIndexOf("-") + 1, packetId.length()).length() == 3) {
                                        finalPacketId = packetId.substring(packetId.length() - 1, packetId.length());
                                        System.out.println("finalPacketId" + finalPacketId);
                                        leadingString = packetId.substring(0, packetId.length() - 1);
                                        System.out.println("leadingString" + leadingString);
                                    } else if (packetId.substring(packetId.lastIndexOf("-") + 1, packetId.length()).length() == 4) {
                                        finalPacketId = packetId.substring(packetId.length() - 2, packetId.length());
                                        System.out.println("finalPacketId" + finalPacketId);
                                        leadingString = packetId.substring(0, packetId.length() - 2);
                                        System.out.println("leadingString" + leadingString);
                                    }
                                    System.out.println("finalPacketId" + finalPacketId);
                                    if (finalPacketId != null) {
                                        if (finalPacketId.length() == 1) {
                                            System.out.println("finalPacketId length 1" + finalPacketId);
                                            if (!finalPacketId.equalsIgnoreCase("Z")) {
                                                Character ch = finalPacketId.charAt(0);
                                                ch++;
                                                finalPacketId = ch.toString();
                                                System.out.println("final 1: " + ch);
                                            } else {
                                                finalPacketId = "AA";
                                                System.out.println("final 2: " + finalPacketId);
                                            }
                                        } else if (finalPacketId.length() == 2) {
                                            if (!finalPacketId.endsWith("Z")) {
                                                Character ch = finalPacketId.charAt(1);
                                                ch++;
                                                finalPacketId = finalPacketId.substring(0, 1).concat(ch.toString());
                                                System.out.println("" + finalPacketId);
                                            } else {
                                                Character ch = finalPacketId.charAt(0);
                                                ch++;
                                                finalPacketId = ch.toString().concat("A");
                                                System.out.println("----" + finalPacketId);
                                            }
                                        }
                                        finalPacketId = leadingString.concat(finalPacketId);
                                        map.put(HkSystemConstantUtil.PacketStaticFieldName.PACKET_ID, finalPacketId);
                                        System.out.println("key from map" + map.get(HkSystemConstantUtil.PacketStaticFieldName.PACKET_ID));
                                    }

                                }
                            } else {
                                return null;
                            }
                        } else {
                            return null;
                        }
                        
                        if (!CollectionUtils.isEmpty(stockDbType)) {
                            uiList = new ArrayList<>(stockDbType.keySet());
                        }
                        Map<String, String> uIFieldNameWithComponentTypes = customFieldService.retrieveUIFieldNameWithComponentTypes(uiList);
                        String pointerComponentType = HkSystemConstantUtil.CustomField.ComponentType.POINTER + "@*" + HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN;
                        Map<String, Object> stockCustom = map;
                        if (!CollectionUtils.isEmpty(stockCustom)) {
                            for (Map.Entry<String, Object> entry : stockCustom.entrySet()) {
                                String key = entry.getKey();
                                Object value = entry.getValue();
                                if (key != null && uIFieldNameWithComponentTypes.containsKey(key)) {
                                    String type = uIFieldNameWithComponentTypes.get(key);
                                    if (type.equals(HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN) || type.equals(HkSystemConstantUtil.CustomField.ComponentType.USER_MULTISELECT) || type.equals(pointerComponentType)) {
                                        if (stockDbType.containsKey(key)) {
                                            stockDbType.put(key, HkSystemConstantUtil.CustomField.DbFieldType.ARRAY);
                                        }

                                        if (stockCustom.containsKey(key)) {
                                            String customVal = stockCustom.get(key).toString();
                                            List<String> values = new ArrayList<>();
                                            String[] valueArray = customVal.replace("\"", "").split(",");
                                            for (String v : valueArray) {
                                                values.add(v.replace("\"", ""));
                                            }
                                            stockCustom.put(key, values);
                                        }
                                    }
                                }

                            }
                        }
                        // code ends here
                        bSONObject = customFieldService.makeBSONObject(stockCustom, stockDbType, HkSystemConstantUtil.Feature.PACKET, loginDataBean.getCompanyId(), null, null);
                        datas.add(bSONObject);
                    }
                }
                List<String> splitPacket = stockService.splitPacket(parent, idTosplit, datas, loginDataBean.getCompanyId(), loginDataBean.getId(),stockDbType);
//                if (!CollectionUtils.isEmpty(splitPacket)) {
                return splitPacket;
//                } else {
//                    return null;
//                }
            }
        }
        return null;
    }

    public EditValueDataBean retrieveLotsAndPacketsEditValue(Map<String, List<String>> featureDbfieldNameMap) {
        EditValueDataBean result = new EditValueDataBean();
        Long companyId = loginDataBean.getCompanyId();
        List<HkLotDocument> lotDocuments = stockService.retrieveLotsByIds(null, loginDataBean.getCompanyId(), false, Arrays.asList(HkSystemConstantUtil.StockStatus.IN_PRODUCTION), false, Boolean.TRUE);

        List<HkPacketDocument> packetDocuments = stockService.retrievePacketsByIds(null, loginDataBean.getCompanyId(), false, Arrays.asList(HkSystemConstantUtil.StockStatus.IN_PRODUCTION), Boolean.TRUE);
        List<SelectItem> lotItems = null;
        List<SelectItem> packetItems = null;
        if ((!CollectionUtils.isEmpty(lotDocuments) || !CollectionUtils.isEmpty(packetDocuments)) && !CollectionUtils.isEmpty(featureDbfieldNameMap)) {
            List<String> invoiceDbFieldNames = null;
            List<String> parcelDbFieldNames = null;
            List<String> lotDbFieldNames = null;
            List<String> packetDbFieldNames = null;
            for (Map.Entry<String, List<String>> entry : featureDbfieldNameMap.entrySet()) {
                String featureName = entry.getKey();
                if (featureName.equalsIgnoreCase(HkSystemConstantUtil.Feature.INVOICE)) {
                    invoiceDbFieldNames = entry.getValue();
                } else if (featureName.equalsIgnoreCase(HkSystemConstantUtil.Feature.PARCEL)) {
                    parcelDbFieldNames = entry.getValue();
                } else if (featureName.equalsIgnoreCase(HkSystemConstantUtil.Feature.LOT)) {
                    lotDbFieldNames = entry.getValue();
                } else if (featureName.equalsIgnoreCase(HkSystemConstantUtil.Feature.PACKET)) {
                    packetDbFieldNames = entry.getValue();
                }
            }
            Map<String, String> uiFieldMapForParcel = new HashMap<>();
            Map<String, String> uiFieldMapForInvoice = new HashMap<>();
            SyncCenterFeatureDocument invoiceFeature = null;
            SyncCenterFeatureDocument parcelFeature = null;
            SyncCenterFeatureDocument lotFeature = null;
            if (!CollectionUtils.isEmpty(lotDocuments)) {
                List<ObjectId> invoiceIds = new ArrayList<>();
                List<ObjectId> parcelIds = new ArrayList<>();
                for (HkLotDocument lotDocument : lotDocuments) {
                    invoiceIds.add(lotDocument.getInvoice());
                    parcelIds.add(lotDocument.getParcel());
                }
                List<HkInvoiceDocument> invoiceDocuments = null;
                invoiceFeature = featureService.retireveFeatureByName(HkSystemConstantUtil.Feature.INVOICE);
                if (invoiceFeature != null) {
                    invoiceDocuments = stockService.retrieveInvoices(null, companyId, Boolean.FALSE, invoiceIds, null,null,null);
                }
                List<HkParcelDocument> parcelDocuments = null;
                parcelFeature = featureService.retireveFeatureByName(HkSystemConstantUtil.Feature.PARCEL);
                if (parcelFeature != null) {
                    parcelDocuments = stockService.retrieveParcels(null, null, parcelIds, companyId, Boolean.FALSE, null, HkSystemConstantUtil.StockStatus.NEW_ROUGH,null,null);
                }
                lotItems = new ArrayList<>();
                for (HkLotDocument lotDocument : lotDocuments) {
                    Map<String, Object> mapToSentOnUI = new HashMap<>();
                    SelectItem selectItem = new SelectItem(lotDocument.getFieldValue().get(HkSystemConstantUtil.AutoNumber.LOT_ID), lotDocument.getId().toString());
                    selectItem.setDescription("lot");
                    BasicBSONObject fieldValue = lotDocument.getFieldValue();
                    if (fieldValue != null) {
                        Map fieldValueMapFromDb = fieldValue.toMap();
                        if (!CollectionUtils.isEmpty(fieldValueMapFromDb) && !CollectionUtils.isEmpty(lotDbFieldNames)) {
                            for (String lotField : lotDbFieldNames) {
                                mapToSentOnUI.put(lotField, fieldValueMapFromDb.get(lotField));
                            }
                        }
                    }

                    if (!CollectionUtils.isEmpty(invoiceDocuments)) {
                        for (HkInvoiceDocument invoiceDocument : invoiceDocuments) {
                            if (invoiceDocument.getId().toString().equals(lotDocument.getInvoice().toString())) {
                                BasicBSONObject fieldValue2 = invoiceDocument.getFieldValue();
                                if (fieldValue2 != null) {
                                    Map fieldValueMapFromDb = fieldValue2.toMap();
                                    if (!CollectionUtils.isEmpty(fieldValueMapFromDb) && !CollectionUtils.isEmpty(invoiceDbFieldNames)) {
                                        for (String invoiceField : invoiceDbFieldNames) {
                                            mapToSentOnUI.put(invoiceField, fieldValueMapFromDb.get(invoiceField));
                                        }
                                    }
                                    break;
                                }
                            }
                        }
                    }
                    if (!CollectionUtils.isEmpty(parcelDocuments)) {
                        for (HkParcelDocument parcelDocument : parcelDocuments) {
                            if (parcelDocument.getId().toString().equals(lotDocument.getParcel().toString())) {
                                BasicBSONObject fieldValue3 = parcelDocument.getFieldValue();
                                if (fieldValue3 != null) {
                                    Map fieldValueMapFromDb = fieldValue3.toMap();
                                    if (!CollectionUtils.isEmpty(fieldValueMapFromDb) && !CollectionUtils.isEmpty(parcelDbFieldNames)) {
                                        for (String parcelField : parcelDbFieldNames) {
                                            mapToSentOnUI.put(parcelField, fieldValueMapFromDb.get(parcelField));
                                        }
                                    }
                                }

                                break;
                            }
                        }
                    }

                    selectItem.setCategoryCustom(mapToSentOnUI);
                    lotItems.add(selectItem);
                }
            }
//            retrieve packet info
            if (!CollectionUtils.isEmpty(packetDocuments)) {
                List<ObjectId> invoiceIds = new ArrayList<>();
                List<ObjectId> parcelIds = new ArrayList<>();
                List<ObjectId> lotIds = new ArrayList<>();
                for (HkPacketDocument packetDocument : packetDocuments) {
                    invoiceIds.add(packetDocument.getInvoice());
                    parcelIds.add(packetDocument.getParcel());
                    lotIds.add(packetDocument.getLot());
                }
                List<HkInvoiceDocument> invoiceDocuments = null;
                if (!CollectionUtils.isEmpty(uiFieldMapForInvoice)) {
                    invoiceDocuments = stockService.retrieveInvoices(null, companyId, Boolean.FALSE, invoiceIds, null,null,null);
                } else {
                    invoiceFeature = featureService.retireveFeatureByName(HkSystemConstantUtil.Feature.INVOICE);
                    if (invoiceFeature != null) {
//                        uiFieldMapForInvoice = customFieldService.retrieveCustomUIFieldNameWithComponentTypes(invoiceFeature.getId(), companyId);
                        invoiceDocuments = stockService.retrieveInvoices(null, companyId, Boolean.FALSE, invoiceIds, null,null,null);
                    }
                }
                List<HkParcelDocument> parcelDocuments = null;
                if (!CollectionUtils.isEmpty(uiFieldMapForParcel)) {
                    parcelDocuments = stockService.retrieveParcels(null, null, parcelIds, companyId, Boolean.FALSE, null, HkSystemConstantUtil.StockStatus.NEW_ROUGH,null,null);
                } else {
                    parcelFeature = featureService.retireveFeatureByName(HkSystemConstantUtil.Feature.PARCEL);
                    if (parcelFeature != null) {
//                        uiFieldMapForParcel = customFieldService.retrieveCustomUIFieldNameWithComponentTypes(parcelFeature.getId(), companyId);
                        parcelDocuments = stockService.retrieveParcels(null, null, parcelIds, companyId, Boolean.FALSE, null, HkSystemConstantUtil.StockStatus.NEW_ROUGH,null,null);
                    }
                }
                lotFeature = featureService.retireveFeatureByName(HkSystemConstantUtil.Feature.LOT);
                if (lotFeature != null) {
//                    uiFieldMapForLot = customFieldService.retrieveCustomUIFieldNameWithComponentTypes(lotFeature.getId(), companyId);
                    lotDocuments = stockService.retrieveLots(null, null, null, lotIds, companyId, Boolean.FALSE);
                }
                packetItems = new ArrayList<>();
                for (HkPacketDocument packetDocument : packetDocuments) {
                    Map<String, Object> mapToSentOnUI = new HashMap<>();
                    SelectItem selectItem = new SelectItem(packetDocument.getFieldValue().get(HkSystemConstantUtil.AutoNumber.PACKET_ID), packetDocument.getId().toString());
                    selectItem.setDescription("packet");
                    BasicBSONObject fieldValue = packetDocument.getFieldValue();
                    if (fieldValue != null) {
                        Map fieldValueMapFromDb = fieldValue.toMap();
                        if (!CollectionUtils.isEmpty(fieldValueMapFromDb) && !CollectionUtils.isEmpty(packetDbFieldNames)) {
                            for (String packetField : packetDbFieldNames) {
                                mapToSentOnUI.put(packetField, fieldValueMapFromDb.get(packetField));
                            }
                        }
                    }

                    for (HkInvoiceDocument invoiceDocument : invoiceDocuments) {
                        if (invoiceDocument.getId().toString().equals(packetDocument.getInvoice().toString())) {
                            BasicBSONObject fieldValue1 = invoiceDocument.getFieldValue();
                            if (fieldValue1 != null) {
                                Map fieldValueMapFromDb = fieldValue1.toMap();
                                if (!CollectionUtils.isEmpty(fieldValueMapFromDb) && !CollectionUtils.isEmpty(invoiceDbFieldNames)) {
                                    for (String invoiceField : invoiceDbFieldNames) {
                                        mapToSentOnUI.put(invoiceField, fieldValueMapFromDb.get(invoiceField));
                                    }
                                }
                                break;
                            }
                        }
                    }
                    for (HkParcelDocument parcelDocument : parcelDocuments) {
                        if (parcelDocument.getId().toString().equals(packetDocument.getParcel().toString())) {
                            BasicBSONObject fieldValue4 = parcelDocument.getFieldValue();
                            if (fieldValue4 != null) {
                                Map fieldValueMapFromDb = fieldValue4.toMap();
                                if (!CollectionUtils.isEmpty(fieldValueMapFromDb) && !CollectionUtils.isEmpty(parcelDbFieldNames)) {
                                    for (String parcelField : parcelDbFieldNames) {
                                        mapToSentOnUI.put(parcelField, fieldValueMapFromDb.get(parcelField));
                                    }
                                }
                            }
                            break;
                        }
                    }

                    for (HkLotDocument lotDocument : lotDocuments) {
                        if (lotDocument.getId().toString().equals(packetDocument.getLot().toString())) {
                            BasicBSONObject fieldValue5 = lotDocument.getFieldValue();
                            if (fieldValue5 != null) {
                                Map fieldValueMapFromDb = fieldValue5.toMap();
                                if (!CollectionUtils.isEmpty(fieldValueMapFromDb) && !CollectionUtils.isEmpty(lotDbFieldNames)) {
                                    for (String lotField : lotDbFieldNames) {
                                        mapToSentOnUI.put(lotField, fieldValueMapFromDb.get(lotField));
                                    }
                                }
                                break;
                            }
                        }
                    }
                    selectItem.setCategoryCustom(mapToSentOnUI);
                    packetItems.add(selectItem);
                }
            }
            if (!CollectionUtils.isEmpty(lotItems)) {
                if (!CollectionUtils.isEmpty(packetItems)) {
                    lotItems.addAll(packetItems);
                }
                result.setStockList(lotItems);
                return result;
            }
        }
        result.setStockList(packetItems);

        return result;
    }

    public List<EditValueDataBean> retrievePlansByLotOrPacket(Map<String, List<String>> payload) {
        //System.out.println("payload:::::::::" + payload);
        List<EditValueDataBean> result = new ArrayList<>();
//        if (!CollectionUtils.isEmpty(payload)) {
//            List<String> lotIds = payload.get("lot");
//            List<ObjectId> lotObjectIds = new ArrayList<>();
//            List<String> packetIds = payload.get("packet");
//            List<ObjectId> packetObjectIds = new ArrayList<>();
//            if (!CollectionUtils.isEmpty(lotIds)) {
//                for (String string : lotIds) {
//                    lotObjectIds.add(new ObjectId(string));
//                }
//            } else {
//                //System.out.println("Lot ids null");
//            }
//            if (!CollectionUtils.isEmpty(packetIds)) {
//                for (String string : packetIds) {
//                    packetObjectIds.add(new ObjectId(string));
//                }
//            } else {
//                //System.out.println("Packet ids null");
//            }
//            List<HkPlanDocument> plans = stockService.retrievePlansAccordingToModifierForLotOrPacket(HkSystemConstantUtil.SERVICE_MODIFIER.PLAN, lotObjectIds, packetObjectIds);
//            //System.out.println("plans::::" + plans);
//
//            if (!CollectionUtils.isEmpty(plans)) {
//                Set<Long> valIds = new HashSet<>();
//                Set<Long> caratIds = new HashSet<>();
//                for (HkPlanDocument hkPlanDocument : plans) {
//                    BasicBSONObject fieldValues = hkPlanDocument.getFieldValue();
//                    Map<String, Object> map = fieldValues.toMap();
//                    //System.out.println("map::::" + map);
//                    if (!CollectionUtils.isEmpty(map)) {
//                        if (map.get(HkSystemConstantUtil.PlanStaticFieldName.CUT) != null) {
//                            valIds.add(Long.parseLong(map.get(HkSystemConstantUtil.PlanStaticFieldName.CUT).toString()));
//                        }
//                        if (map.get(HkSystemConstantUtil.PlanStaticFieldName.COLOR) != null) {
//                            valIds.add(Long.parseLong(map.get(HkSystemConstantUtil.PlanStaticFieldName.COLOR).toString()));
//                        }
//                        if (map.get(HkSystemConstantUtil.PlanStaticFieldName.CLARITY) != null) {
//                            valIds.add(Long.parseLong(map.get(HkSystemConstantUtil.PlanStaticFieldName.CLARITY).toString()));
//                        }
//                        if (map.get(HkSystemConstantUtil.PlanStaticFieldName.FLUROSCENE) != null) {
//                            valIds.add(Long.parseLong(map.get(HkSystemConstantUtil.PlanStaticFieldName.FLUROSCENE).toString()));
//                        }
//                        if (map.get(HkSystemConstantUtil.PlanStaticFieldName.CARAT) != null) {
//                            caratIds.add(Long.parseLong(map.get(HkSystemConstantUtil.PlanStaticFieldName.CARAT).toString()));
//                        }
//                    }
//                }
//                Map<Long, String> valIdToNameMap = new HashMap<>();
//                Map<Long, String> caratMap = new HashMap<>();
//                if (!CollectionUtils.isEmpty(caratIds)) {
//                    List<HkCaratRangeDocument> carats = caratRangeService.retrieveCaratRangeByIds(new ArrayList<>(caratIds));
//                    if (!CollectionUtils.isEmpty(carats)) {
//                        for (HkCaratRangeDocument hkCaratRangeDocument : carats) {
//                            caratMap.put(hkCaratRangeDocument.getId(), hkCaratRangeDocument.getMinValue() + " - " + hkCaratRangeDocument.getMaxValue());
//                        }
//                    }
//                }
//                if (!CollectionUtils.isEmpty(valIds)) {
//                    List<HkMasterValueDocument> values = foundationDocumentService.retrieveValueEntities(new ArrayList<>(valIds));
//                    if (!CollectionUtils.isEmpty(values)) {
//                        for (HkMasterValueDocument hkMasterValueDocument : values) {
//                            valIdToNameMap.put(hkMasterValueDocument.getId(), hkMasterValueDocument.getTranslatedValueName());
//                        }
//                    }
//                } else {
//                    //System.out.println("valIds is null");
//                }
//
//                for (HkPlanDocument hkPlanDocument : plans) {
//                    EditValueDataBean valueDataBean = new EditValueDataBean();
//
//                    valueDataBean.setPlanId(hkPlanDocument.getId().toString());
//                    if (hkPlanDocument.getPacket() != null) {
//                        valueDataBean.setStockId(hkPlanDocument.getPacket().toString());
//                        valueDataBean.setStockType("packet");
//                    } else if (hkPlanDocument.getLot() != null) {
//                        valueDataBean.setStockId(hkPlanDocument.getLot().toString());
//                        valueDataBean.setStockType("lot");
//                    }
//                    BasicBSONObject fieldValues = hkPlanDocument.getFieldValue();
//                    Map<String, Object> map = fieldValues.toMap();
//                    //System.out.println("map2::::" + map);
//                    if (!CollectionUtils.isEmpty(map)) {
//                        valueDataBean.setPlanNumber(map.get(HkSystemConstantUtil.AutoNumber.PLAN_ID).toString());
//                        valueDataBean.setPrice(map.get(HkSystemConstantUtil.PlanStaticFieldName.PRICE));
//
//                        if (map.get(HkSystemConstantUtil.PlanStaticFieldName.CUT) != null) {
//                            valueDataBean.setCut(valIdToNameMap.get(map.get(HkSystemConstantUtil.PlanStaticFieldName.CUT)));
//                            valueDataBean.setCutId(Long.parseLong(map.get(HkSystemConstantUtil.PlanStaticFieldName.CUT).toString()));
//                        } else {
//                            valueDataBean.setCut("N/A");
//                        }
//                        if (map.get(HkSystemConstantUtil.PlanStaticFieldName.COLOR) != null) {
//                            valueDataBean.setColor(valIdToNameMap.get(map.get(HkSystemConstantUtil.PlanStaticFieldName.COLOR)));
//                            valueDataBean.setColorId(Long.parseLong(map.get(HkSystemConstantUtil.PlanStaticFieldName.COLOR).toString()));
//                        } else {
//                            valueDataBean.setCut("N/A");
//                        }
//                        if (map.get(HkSystemConstantUtil.PlanStaticFieldName.CLARITY) != null) {
//                            valueDataBean.setClarity(valIdToNameMap.get(map.get(HkSystemConstantUtil.PlanStaticFieldName.CLARITY)));
//                            valueDataBean.setClarityId(Long.parseLong(map.get(HkSystemConstantUtil.PlanStaticFieldName.CLARITY).toString()));
//                        } else {
//                            valueDataBean.setCut("N/A");
//                        }
//                        if (map.get(HkSystemConstantUtil.PlanStaticFieldName.FLUROSCENE) != null) {
//                            valueDataBean.setFluroscene(valIdToNameMap.get(map.get(HkSystemConstantUtil.PlanStaticFieldName.FLUROSCENE)));
//                            valueDataBean.setFlurosceneId(Long.parseLong(map.get(HkSystemConstantUtil.PlanStaticFieldName.FLUROSCENE).toString()));
//                        } else {
//                            valueDataBean.setCut("N/A");
//                        }
//                        if (map.get(HkSystemConstantUtil.PlanStaticFieldName.CARAT) != null) {
//                            valueDataBean.setCarat(caratMap.get(map.get(HkSystemConstantUtil.PlanStaticFieldName.CARAT)));
//                            valueDataBean.setCaratId(Long.parseLong(map.get(HkSystemConstantUtil.PlanStaticFieldName.CARAT).toString()));
//                        } else {
//                            valueDataBean.setCarat("N/A");
//                        }
//                    }
//                    result.add(valueDataBean);
//                }
//            } else {
//                //System.out.println("Plans not found");
//            }
//        }
        return result;
    }

    public Boolean editValues(List<Map<String, Object>> payload) {
//        if (!CollectionUtils.isEmpty(payload)) {
//            List<ObjectId> planIds = new ArrayList<>();
//
//            for (Map<String, Object> map : payload) {
//                ObjectId planId = new ObjectId(map.get("planId").toString());
//
//                planIds.add(planId);
//            }
//            //System.out.println("planIds:::" + planIds);
//            if (!CollectionUtils.isEmpty(planIds)) {
//                List<HkPlanDocument> plans = stockService.retrievePlansByIds(planIds, loginDataBean.getCompanyId());
//                //System.out.println("plans::::" + plans);
//                Map<ObjectId, HkPlanDocument> mapOfPlans = new HashMap<>();
//                if (!CollectionUtils.isEmpty(plans)) {
//                    for (HkPlanDocument hkPlanDocument : plans) {
//                        mapOfPlans.put(hkPlanDocument.getId(), hkPlanDocument);
//                    }
//                    List<HkPlanDocument> listOfPlans = new ArrayList<>();
//
//                    for (Map<String, Object> map : payload) {
//                        ObjectId planId = new ObjectId(map.get("planId").toString());
//                        Object price = map.get("previousPrice");
//
//                        HkPlanDocument newPlan = mapOfPlans.get(planId);
//                        Map fieldMap = newPlan.getFieldValue().toMap();
//                        fieldMap.put(HkSystemConstantUtil.PlanStaticFieldName.PRICE, price);
//
//                        //System.out.println("fieldMap::::" + fieldMap);
//                        newPlan.setFieldValue(new BasicBSONObject(fieldMap));
//                        listOfPlans.add(newPlan);
//                    }
//                    //System.out.println("listOfPlans:::" + listOfPlans);
//                    if (!CollectionUtils.isEmpty(listOfPlans)) {
//                        return stockService.updatePlanForEditValue(listOfPlans, loginDataBean.getId(), Boolean.TRUE);
//                    }
//                }
//            }
//        }
        return false;
    }
}
