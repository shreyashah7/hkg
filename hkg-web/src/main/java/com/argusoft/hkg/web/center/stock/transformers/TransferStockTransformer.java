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
import com.argusoft.hkg.web.center.stock.databeans.StockDataBean;
import com.argusoft.hkg.web.center.usermanagement.databeans.LoginDataBean;
import com.argusoft.hkg.web.center.usermanagement.databeans.UMFeatureDetailDataBean;
import com.argusoft.hkg.web.center.util.ApplicationUtil;
import com.argusoft.hkg.web.util.SelectItem;
import com.argusoft.sync.center.model.HkFieldDocument;
import com.argusoft.sync.center.model.HkSectionDocument;
import java.util.ArrayList;
import java.util.HashMap;
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
public class TransferStockTransformer {

    @Autowired
    HkCustomFieldService customFieldService;

    @Autowired
    HkStockService stockService;

    @Autowired
    LoginDataBean loginDataBean;

    @Autowired
    StockTransformer stockTransformer;

    @Autowired
    ApplicationUtil applicationUtil;

    public StockDataBean searchedStocks(List<String> invoiceDbFieldName, List<String> lotDbFieldName, List<String> packetDbFieldName, List<String> parcelDbFieldName) throws GenericDatabaseException {

        List<Map<Object, Object>> listOfTableHeader = new ArrayList<>();
        List<HkInvoiceDocument> invoiceDocuments = new ArrayList<>();
        List<ObjectId> invoiceIds = new ArrayList<>();
        List<HkParcelDocument> parcelDocuments = new ArrayList<>();
        List<HkLotDocument> lotDocuments = new ArrayList<>();
        List<HkPacketDocument> packetDocuments = new ArrayList<>();
        List<ObjectId> parcelIds = new ArrayList<>();
        List<ObjectId> lotIds = new ArrayList<>();
        List<Long> forUserIds = new ArrayList<>();
        List<String> statusList = new ArrayList<>();
        List<String> serviceCodeList = new ArrayList<>();
        List<SelectItem> selectItems = new ArrayList<>();
        List<ObjectId> workAllotmentIds = new ArrayList<>();
//        ////////////////////////////////////////////////
        Long franchise = loginDataBean.getCompanyId();

        forUserIds.add(loginDataBean.getId());
        statusList.add(HkSystemConstantUtil.ActivityServiceStatus.IN_PROCESS);
        serviceCodeList.add(HkSystemConstantUtil.SERVICE_CODE.TRANSFER_LOT);
        if (!CollectionUtils.isEmpty(forUserIds) || !CollectionUtils.isEmpty(statusList) || !CollectionUtils.isEmpty(serviceCodeList)) {
//            lotDocuments = hkWorkAllotmentService.retrieveLotsOrPacketsFromWorkAllocation(null, forUserIds, null, statusList, serviceCodeList, franchise, Boolean.FALSE, Boolean.TRUE);
//            packetDocuments = hkWorkAllotmentService.retrieveLotsOrPacketsFromWorkAllocation(null, forUserIds, null, statusList, serviceCodeList, franchise, Boolean.TRUE, Boolean.TRUE);
        }//            lotDocuments = hkWorkAllotmentService.retrieveLots(lotFieldMap, invoiceIds, parcelIds, null, franchise, Boolean.FALSE);
        //System.out.println("-----transfer lots-----" + lotDocuments);
        if (!CollectionUtils.isEmpty(lotDocuments)) {

            for (HkLotDocument lotDocument : lotDocuments) {
                invoiceIds.add(lotDocument.getInvoice());
                parcelIds.add(lotDocument.getParcel());
//                workAllotmentIds.add(lotDocument.getWorkAllotmentId());
            }
            List<HkInvoiceDocument> hkInvoiceDocuments = new ArrayList<>();
            if (!CollectionUtils.isEmpty(invoiceIds)) {
                hkInvoiceDocuments = stockService.retrieveInvoices(invoiceIds, franchise, Boolean.FALSE);
            }
            List<HkParcelDocument> hkParcelDocuments = new ArrayList<>();
            if (!CollectionUtils.isEmpty(parcelIds)) {
                hkParcelDocuments = stockService.retrieveParcels(parcelIds, franchise, Boolean.FALSE);
            }

//            List<HkInvoiceDocument> hkInvoiceDocuments = stockService.retrieveInvoices(invoiceIds, franchise, Boolean.FALSE);
//            List<HkParcelDocument> hkParcelDocuments = stockService.retrieveParcels(parcelIds, franchise, Boolean.FALSE);
            for (HkLotDocument lotDocument : lotDocuments) {
                Map<String, Object> map = new HashMap<>();
                SelectItem selectItem = new SelectItem(null, lotDocument.getParcel().toString(), lotDocument.getInvoice().toString());

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
                if (!CollectionUtils.isEmpty(lotDbFieldName)) {
                    for (String model : lotDbFieldName) {
                        if (oldlotDocument.getFieldValue().toMap().containsKey(model)) {
                            map.put(model, oldlotDocument.getFieldValue().toMap().get(model));
                        }
                    }
                }
                if (!CollectionUtils.isEmpty(hkInvoiceDocuments) && !CollectionUtils.isEmpty(hkParcelDocuments)) {
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

                    List uiFieldListForParcel = new ArrayList<>();
                    for (HkParcelDocument parcelDocument : hkParcelDocuments) {
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
                selectItems.add(selectItem);
//                    System.out.println("----select item final-----" + selectItems.toString());
            }
        }
        if (!CollectionUtils.isEmpty(packetDocuments)) {
            invoiceIds = new ArrayList<>();
            parcelIds = new ArrayList<>();
            lotIds = new ArrayList<>();
            for (HkPacketDocument packetDocument : packetDocuments) {
                invoiceIds.add(packetDocument.getInvoice());
                parcelIds.add(packetDocument.getParcel());
//                workAllotmentIds.add(packetDocument.getWorkAllotmentId());
                lotIds.add(packetDocument.getLot());
            }
            List<HkInvoiceDocument> hkInvoiceDocuments = stockService.retrieveInvoices(invoiceIds, franchise, Boolean.FALSE);
            List<HkParcelDocument> hkParcelDocuments = stockService.retrieveParcels(parcelIds, franchise, Boolean.FALSE);
            List<HkLotDocument> hkLotDocuments = stockService.retrieveLotsByIds(lotIds, franchise, Boolean.FALSE, null, null,Boolean.TRUE);

            if (!CollectionUtils.isEmpty(hkInvoiceDocuments) && !CollectionUtils.isEmpty(hkParcelDocuments) && !CollectionUtils.isEmpty(hkLotDocuments)) {
                for (HkPacketDocument packetDocument : packetDocuments) {
                    Map<String, Object> map = new HashMap<>();

                    SelectItem selectItem = new SelectItem(null, packetDocument.getParcel().toString(), packetDocument.getInvoice().toString(), packetDocument.getLot().toString());
                    selectItem.setStatus(packetDocument.getId().toString());
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

                    for (String model : packetDbFieldName) {
                        if (oldpacketDocument.getFieldValue().toMap().containsKey(model)) {
                            map.put(model, oldpacketDocument.getFieldValue().toMap().get(model));
                        }
                    }
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
                            for (String model : parcelDbFieldName) {
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
                            for (String model : lotDbFieldName) {
                                if (oldlotDocument.getFieldValue().toMap().containsKey(model)) {
                                    map.put(model, oldlotDocument.getFieldValue().toMap().get(model));
                                }
                            }
                            break;
                        }
                    }
                    selectItem.setCategoryCustom(map);
                    selectItems.add(selectItem);
                }

            }
        }
        if (!CollectionUtils.isEmpty(selectItems)) {
            StockDataBean stockDataBean = new StockDataBean(listOfTableHeader, selectItems, null, null);
//            if (!CollectionUtils.isEmpty(workAllotmentIds)) {
//                DynamicServiceInitDataBean nodeInfo = stockTransformer.retrieveNodeInfo(workAllotmentIds, franchise, HkSystemConstantUtil.SERVICE_CODE.TRANSFER_LOT);
//                //System.out.println("------nodeInfo------------" + nodeInfo);
//                stockDataBean.setDynamicServiceInitBean(nodeInfo);
//            }
            return stockDataBean;
        } else {
            return null;
        }

    }

    public Object transferStock(StockDataBean stockDataBean) throws GenericDatabaseException {
        BasicBSONObject bSONObject = customFieldService.makeBSONObject(stockDataBean.getStockCustom(), stockDataBean.getStockDbType(), HkSystemConstantUtil.Feature.TRANSFER, loginDataBean.getCompanyId(), null,null);
        // Formula method calll
        UMFeatureDetailDataBean feature = applicationUtil.getuMFeatureDetailDataBeanMap().get(HkSystemConstantUtil.Feature.TRANSFER);
        
        Map<String, String> dbFieldWithFormulaMap = null;
//        Commmented by Shifa temporarly
//        customFieldService.retrieveFeatureNameWithDbFieldListForFormula(feature.getId(), loginDataBean.getCompanyId(), HkSystemConstantUtil.FeatureForFormulaEvaluation.TRANSFER_STOCK, HkSystemConstantUtil.CustomField.ComponentType.FORMULA);
        Object transferNumber = null;
        if (stockDataBean.getLotDataBean() != null && stockDataBean.getPacketDataBean() != null) {
            //System.out.println("inside packet :");
            transferNumber = stockService.transferLot(bSONObject, loginDataBean.getCompanyId(), loginDataBean.getId(), null, new ObjectId(stockDataBean.getPacketDataBean().getId()), dbFieldWithFormulaMap);
        } else {
            transferNumber = stockService.transferLot(bSONObject, loginDataBean.getCompanyId(), loginDataBean.getId(), new ObjectId(stockDataBean.getLotDataBean().getId()), null, dbFieldWithFormulaMap);
        }
        return transferNumber;
    }

    private List<HkFeatureFieldPermissionDocument> retrieveAllFields() {
        //Retrieved FeatureId
        UMFeatureDetailDataBean feature = applicationUtil.getuMFeatureDetailDataBeanMap().get("stocktransfer");
        List<HkFeatureFieldPermissionDocument> fieldPermissionEntitys = new ArrayList<>();
        if (feature != null) {
            fieldPermissionEntitys = customFieldService.retrieveFeatureFieldPermissions(feature.getId(), loginDataBean.getRoleIds());
        }
        return fieldPermissionEntitys;
    }

    public StockDataBean retrieveStockByWorkAllotmentId(String workAllotmentId, Boolean isPacket) throws GenericDatabaseException {
        List<HkLotDocument> lotDocuments = new ArrayList<>();
        List<HkPacketDocument> packetDocuments = new ArrayList<>();
        List<ObjectId> invoiceIds = new ArrayList<>();
        List<ObjectId> parcelIds = new ArrayList<>();
        List<ObjectId> lotIds = new ArrayList<>();
        List<Long> forUserIds = new ArrayList<>();
        List<String> statusList = new ArrayList<>();
        List<String> serviceCodeList = new ArrayList<>();
        List<ObjectId> workAllotmentIds = new ArrayList<>();
        List<HkFeatureFieldPermissionDocument> retrieveFieldsConfigureForSearch = null;
        Long franchise = loginDataBean.getCompanyId();
        if (workAllotmentId != null) {
            workAllotmentIds.add(new ObjectId(workAllotmentId));
        }

        forUserIds.add(loginDataBean.getId());
        statusList.add(HkSystemConstantUtil.ActivityServiceStatus.IN_PROCESS);
        serviceCodeList.add(HkSystemConstantUtil.SERVICE_CODE.TRANSFER_LOT);
        if (!isPacket) {
            List<String> invoiceParentDbFieldName = null;
            List<String> parcelParentDbFieldName = null;
            List<String> lotParentDbFieldName = null;
            List<String> packetParentDbFieldName = null;
//            lotDocuments = hkWorkAllotmentService.retrieveLotsOrPacketsFromWorkAllocation(workAllotmentIds, forUserIds, null, statusList, serviceCodeList, franchise, Boolean.FALSE, Boolean.TRUE);
            retrieveFieldsConfigureForSearch = this.retrieveAllFields();
            for (HkFeatureFieldPermissionDocument hkFeatureFieldPermissionEntity : retrieveFieldsConfigureForSearch) {
                //Add parent fields.
                if (hkFeatureFieldPermissionEntity.getParentViewFlag()) {
                    String dbFieldName = hkFeatureFieldPermissionEntity.getHkFieldEntity().getDbFieldName();
                    if (hkFeatureFieldPermissionEntity.getEntityName().equalsIgnoreCase(HkSystemConstantUtil.Feature.INVOICE)) {
                        if (CollectionUtils.isEmpty(invoiceParentDbFieldName)) {
                            invoiceParentDbFieldName = new ArrayList<>();
                        }
                        invoiceParentDbFieldName.add(dbFieldName);
                    } else if (hkFeatureFieldPermissionEntity.getEntityName().equalsIgnoreCase(HkSystemConstantUtil.Feature.PARCEL)) {
                        if (CollectionUtils.isEmpty(parcelParentDbFieldName)) {
                            parcelParentDbFieldName = new ArrayList<>();
                        }
                        parcelParentDbFieldName.add(dbFieldName);
                    } else if (hkFeatureFieldPermissionEntity.getEntityName().equalsIgnoreCase(HkSystemConstantUtil.Feature.LOT)) {
                        if (CollectionUtils.isEmpty(lotParentDbFieldName)) {
                            lotParentDbFieldName = new ArrayList<>();
                        }
                        lotParentDbFieldName.add(dbFieldName);
                    } else if (hkFeatureFieldPermissionEntity.getEntityName().equalsIgnoreCase(HkSystemConstantUtil.Feature.PACKET)) {
                        if (CollectionUtils.isEmpty(packetParentDbFieldName)) {
                            packetParentDbFieldName = new ArrayList<>();
                        }
                        packetParentDbFieldName.add(dbFieldName);
                    }
                }

            }

            if (!CollectionUtils.isEmpty(lotDocuments) && lotDocuments.size() == 1) {
                HkLotDocument lotDocument = lotDocuments.get(0);
                SelectItem selectItem = new SelectItem(null, lotDocument.getParcel().toString(), lotDocument.getInvoice().toString(), lotDocument.getId().toString());
                invoiceIds.add(lotDocument.getInvoice());
                parcelIds.add(lotDocument.getParcel());
                List<HkInvoiceDocument> hkInvoiceDocuments = stockService.retrieveInvoices(invoiceIds, franchise, Boolean.FALSE);
                List<HkParcelDocument> hkParcelDocuments = stockService.retrieveParcels(parcelIds, franchise, Boolean.FALSE);
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
                Map<String, String> uiFieldListWithComponentTypeForLot = customFieldService.retrieveUIFieldNameWithComponentTypes(uiFieldListForLot);
                String pointerComponentType = HkSystemConstantUtil.CustomField.ComponentType.POINTER + "@*" + HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN;

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
                }

                //Parent fields for lot.
                if (!CollectionUtils.isEmpty(lotParentDbFieldName)) {
                    Map<Object, Object> map = new HashMap<>();
                    for (String lotField : lotParentDbFieldName) {
                        if (customMapforLot.get(lotField) != null) {
                            map.put(lotField, customMapforLot.get(lotField));
                        }
                    }
                    selectItem.setCustom4(map);
                }

                List uiFieldListForInvoice = new ArrayList<>();
                if (lotDocument.getFieldValue() != null) {
                    Map<String, Object> map1 = lotDocument.getFieldValue().toMap();
                    if (!CollectionUtils.isEmpty(map1)) {
                        for (Map.Entry<String, Object> entrySet : map1.entrySet()) {
                            String key = entrySet.getKey();
                            Object value = entrySet.getValue();
                            uiFieldListForInvoice.add(key);
                        }
                    }
                }
                Map<String, String> uiFieldListWithComponentTypeForInvoice = customFieldService.retrieveUIFieldNameWithComponentTypes(uiFieldListForInvoice);

                Map<String, Object> customMapforInvoice = hkInvoiceDocument.getFieldValue().toMap();

                if (!CollectionUtils.isEmpty(uiFieldListWithComponentTypeForInvoice)) {
                    for (Map.Entry<String, String> field : uiFieldListWithComponentTypeForInvoice.entrySet()) {
                        if (field.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN) || field.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.USER_MULTISELECT) || field.getValue().equals(pointerComponentType)) {
                            if (customMapforInvoice != null && !customMapforInvoice.isEmpty() && customMapforInvoice.containsKey(field.getKey())) {
                                String value = customMapforInvoice.get(field.getKey()).toString().replaceAll("\\[", "").replaceAll("\\]", "");
                                customMapforInvoice.put(field.getKey(), value);
                            }
                        }
                    }
                }
                //Parent fields for invoice.
                if (!CollectionUtils.isEmpty(invoiceParentDbFieldName)) {
                    Map<Object, Object> map = new HashMap<>();
                    for (String invoiceField : invoiceParentDbFieldName) {
                        if (customMapforInvoice.get(invoiceField) != null) {
                            map.put(invoiceField, customMapforInvoice.get(invoiceField));
                        }
                    }
                    selectItem.setCustom1(map);
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
                Map<String, String> uiFieldListWithComponentTypeForParcel = customFieldService.retrieveUIFieldNameWithComponentTypes(uiFieldListForParcel);

                Map<String, Object> customMapforParcel = hkParcelDocument.getFieldValue().toMap();

                if (!CollectionUtils.isEmpty(uiFieldListWithComponentTypeForParcel)) {
                    for (Map.Entry<String, String> field : uiFieldListWithComponentTypeForParcel.entrySet()) {
                        if (field.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN) || field.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.USER_MULTISELECT) || field.getValue().equals(pointerComponentType)) {
                            if (customMapforParcel != null && !customMapforParcel.isEmpty() && customMapforParcel.containsKey(field.getKey())) {
                                String value = customMapforParcel.get(field.getKey()).toString().replaceAll("\\[", "").replaceAll("\\]", "");
                                customMapforParcel.put(field.getKey(), value);
                            }
                        }
                    }
                }
                //Parent fields for invoice.
                if (!CollectionUtils.isEmpty(parcelParentDbFieldName)) {
                    Map<Object, Object> map = new HashMap<>();
                    for (String parcelField : parcelParentDbFieldName) {
                        if (customMapforParcel.get(parcelField) != null) {
                            map.put(parcelField, customMapforParcel.get(parcelField));
                        }
                    }
                    selectItem.setCustom3(map);
                }
                return new StockDataBean(null, null, selectItem, null);
            } else {
                return null;
            }

        } else {
            List<String> invoiceParentDbFieldName = null;
            List<String> parcelParentDbFieldName = null;
            List<String> lotParentDbFieldName = null;
            List<String> packetParentDbFieldName = null;
//            packetDocuments = hkWorkAllotmentService.retrieveLotsOrPacketsFromWorkAllocation(workAllotmentIds, forUserIds, null, statusList, serviceCodeList, franchise, Boolean.TRUE, Boolean.TRUE);
            retrieveFieldsConfigureForSearch = this.retrieveAllFields();
            for (HkFeatureFieldPermissionDocument hkFeatureFieldPermissionEntity : retrieveFieldsConfigureForSearch) {
                //Add parent fields.
                if (hkFeatureFieldPermissionEntity.getParentViewFlag()) {
                    String dbFieldName = hkFeatureFieldPermissionEntity.getHkFieldEntity().getDbFieldName();
                    if (hkFeatureFieldPermissionEntity.getEntityName().equalsIgnoreCase(HkSystemConstantUtil.Feature.INVOICE)) {
                        if (CollectionUtils.isEmpty(invoiceParentDbFieldName)) {
                            invoiceParentDbFieldName = new ArrayList<>();
                        }
                        invoiceParentDbFieldName.add(dbFieldName);
                    } else if (hkFeatureFieldPermissionEntity.getEntityName().equalsIgnoreCase(HkSystemConstantUtil.Feature.PARCEL)) {
                        if (CollectionUtils.isEmpty(parcelParentDbFieldName)) {
                            parcelParentDbFieldName = new ArrayList<>();
                        }
                        parcelParentDbFieldName.add(dbFieldName);
                    } else if (hkFeatureFieldPermissionEntity.getEntityName().equalsIgnoreCase(HkSystemConstantUtil.Feature.LOT)) {
                        if (CollectionUtils.isEmpty(lotParentDbFieldName)) {
                            lotParentDbFieldName = new ArrayList<>();
                        }
                        lotParentDbFieldName.add(dbFieldName);
                    } else if (hkFeatureFieldPermissionEntity.getEntityName().equalsIgnoreCase(HkSystemConstantUtil.Feature.PACKET)) {
                        if (CollectionUtils.isEmpty(packetParentDbFieldName)) {
                            packetParentDbFieldName = new ArrayList<>();
                        }
                        packetParentDbFieldName.add(dbFieldName);
                    }
                }

            }

            if (!CollectionUtils.isEmpty(packetDocuments) && packetDocuments.size() == 1) {
                HkPacketDocument packetDocument = packetDocuments.get(0);

                SelectItem selectItem = new SelectItem(null, packetDocument.getParcel().toString(), packetDocument.getInvoice().toString(), packetDocument.getLot().toString());
                selectItem.setStatus(packetDocument.getId().toString());
                invoiceIds.add(packetDocument.getInvoice());
                parcelIds.add(packetDocument.getParcel());
                lotIds.add(packetDocument.getLot());
                List<HkInvoiceDocument> hkInvoiceDocuments = stockService.retrieveInvoices(invoiceIds, franchise, Boolean.FALSE);
                List<HkParcelDocument> hkParcelDocuments = stockService.retrieveParcels(parcelIds, franchise, Boolean.FALSE);
                List<HkLotDocument> hkLotDocuments = null;
                hkLotDocuments = stockService.retrieveLotsByIds(lotIds, franchise, Boolean.FALSE, null, null,Boolean.TRUE);
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
                Map<String, String> uiFieldListWithComponentTypeForPacket = customFieldService.retrieveUIFieldNameWithComponentTypes(uiFieldListForPacket);
                String pointerComponentType = HkSystemConstantUtil.CustomField.ComponentType.POINTER + "@*" + HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN;

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
                }

                //Parent fields for Packet.
                if (!CollectionUtils.isEmpty(packetParentDbFieldName)) {
                    Map<Object, Object> map = new HashMap<>();
                    for (String lotField : packetParentDbFieldName) {
                        if (customMapforPacket.get(lotField) != null) {
                            map.put(lotField, customMapforPacket.get(lotField));
                        }
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
                Map<String, String> uiFieldListWithComponentTypeForLot = customFieldService.retrieveUIFieldNameWithComponentTypes(uiFieldListForLot);

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
                }

                //Parent fields for lot.
                if (!CollectionUtils.isEmpty(lotParentDbFieldName)) {
                    Map<Object, Object> map = new HashMap<>();
                    for (String lotField : lotParentDbFieldName) {
                        if (customMapforLot.get(lotField) != null) {
                            map.put(lotField, customMapforLot.get(lotField));
                        }
                    }
                    selectItem.setCustom4(map);
                }

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

                Map<String, Object> customMapforInvoice = hkInvoiceDocument.getFieldValue().toMap();

                if (!CollectionUtils.isEmpty(uiFieldListWithComponentTypeForInvoice)) {
                    for (Map.Entry<String, String> field : uiFieldListWithComponentTypeForInvoice.entrySet()) {
                        if (field.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN) || field.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.USER_MULTISELECT) || field.getValue().equals(pointerComponentType)) {
                            if (customMapforInvoice != null && !customMapforInvoice.isEmpty() && customMapforInvoice.containsKey(field.getKey())) {
                                String value = customMapforInvoice.get(field.getKey()).toString().replaceAll("\\[", "").replaceAll("\\]", "");
                                customMapforInvoice.put(field.getKey(), value);
                            }
                        }
                    }
                }
                //Parent fields for invoice.
                if (!CollectionUtils.isEmpty(invoiceParentDbFieldName)) {
                    Map<Object, Object> map = new HashMap<>();
                    for (String invoiceField : invoiceParentDbFieldName) {
                        if (customMapforInvoice.get(invoiceField) != null) {
                            map.put(invoiceField, customMapforInvoice.get(invoiceField));
                        }
                    }
                    selectItem.setCustom1(map);
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
                Map<String, String> uiFieldListWithComponentTypeForParcel = customFieldService.retrieveUIFieldNameWithComponentTypes(uiFieldListForParcel);

                Map<String, Object> customMapforParcel = hkParcelDocument.getFieldValue().toMap();

                if (!CollectionUtils.isEmpty(uiFieldListWithComponentTypeForParcel)) {
                    for (Map.Entry<String, String> field : uiFieldListWithComponentTypeForParcel.entrySet()) {
                        if (field.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN) || field.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.USER_MULTISELECT) || field.getValue().equals(pointerComponentType)) {
                            if (customMapforParcel != null && !customMapforParcel.isEmpty() && customMapforParcel.containsKey(field.getKey())) {
                                String value = customMapforParcel.get(field.getKey()).toString().replaceAll("\\[", "").replaceAll("\\]", "");
                                customMapforParcel.put(field.getKey(), value);
                            }
                        }
                    }
                }
                //Parent fields for invoice.
                if (!CollectionUtils.isEmpty(parcelParentDbFieldName)) {
                    Map<Object, Object> map = new HashMap<>();
                    for (String parcelField : parcelParentDbFieldName) {
                        if (customMapforParcel.get(parcelField) != null) {
                            map.put(parcelField, customMapforParcel.get(parcelField));
                        }
                    }
                    selectItem.setCustom3(map);
                }
                return new StockDataBean(null, null, selectItem, null);

            } else {
                return null;
            }
        }
    }

    private Map<HkSectionDocument, List<HkFeatureFieldPermissionDocument>> retrieveFieldsConfigureForSearch() {

        List<HkFieldDocument> fieldEntityList = null;
        //Retrieved FeatureId
        UMFeatureDetailDataBean feature = applicationUtil.getuMFeatureDetailDataBeanMap().get("stocktransfer");
        Map<HkSectionDocument, List<HkFeatureFieldPermissionDocument>> fieldPermissionEntitys = new HashMap<>();
        if (feature != null) {
            fieldPermissionEntitys = customFieldService.retrieveFeatureFieldPermissionForSearch(feature.getId(), loginDataBean.getRoleIds());
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
