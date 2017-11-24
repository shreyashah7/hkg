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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bson.BasicBSONObject;
import org.bson.types.ObjectId;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 *
 * @author brijesh
 */
@Service
public class SellStockTransformer {

    @Autowired
    HkCustomFieldService customFieldService;

    @Autowired
    HkStockService stockService;

    @Autowired
    LoginDataBean loginDataBean;
 
    @Autowired
    ApplicationUtil applicationUtil;

    @Autowired
    StockTransformer stockTransformer;
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(SellStockTransformer.class);

    public StockDataBean searchedStocks(List<String> invoiceDbFieldName, List<String> lotDbFieldName, List<String> packetDbFieldName, List<String> parcelDbFieldName) throws GenericDatabaseException {
        //System.out.println("----invoice dbfieldName-----" + invoiceDbFieldName);
        //System.out.println("----parcelDbFieldName dbfieldName-----" + parcelDbFieldName);
        //System.out.println("----lotDbFieldName dbfieldName-----" + lotDbFieldName);
        //System.out.println("----packetDbFieldName dbfieldName-----" + packetDbFieldName);
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
        ////////////////////////////////////////////////
        Long franchise = loginDataBean.getCompanyId();

        forUserIds.add(loginDataBean.getId());
        statusList.add(HkSystemConstantUtil.ActivityServiceStatus.IN_PROCESS);
        serviceCodeList.add(HkSystemConstantUtil.SERVICE_CODE.SELL_LOT);
       
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

            for (HkLotDocument lotDocument : lotDocuments) {
                Map<String, Object> map = new HashMap<>();
//                    System.out.println("---lotId----" + lotDocument.getId().toString());

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
                if (!CollectionUtils.isEmpty(lotDbFieldName)) {
                    for (String model : lotDbFieldName) {
                        if (oldlotDocument.getFieldValue().toMap().containsKey(model)) {
                            map.put(model, oldlotDocument.getFieldValue().toMap().get(model));
                        }
                    }
                }
                if (!CollectionUtils.isEmpty(hkInvoiceDocuments) || !CollectionUtils.isEmpty(hkParcelDocuments)) {
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
            List<HkLotDocument> hkLotDocuments = stockService.retrieveLotsByIds(lotIds, franchise, Boolean.FALSE, null, null, Boolean.TRUE);

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
//                DynamicServiceInitDataBean nodeInfo = stockTransformer.retrieveNodeInfo(workAllotmentIds, franchise, HkSystemConstantUtil.SERVICE_CODE.SELL_LOT);
//                //System.out.println("------nodeInfo------------" + nodeInfo);
//                stockDataBean.setDynamicServiceInitBean(nodeInfo);
//            }
            return stockDataBean;
        } else {
            return null;
        }
    }

    public Object sellStock(StockDataBean stockDataBean) throws GenericDatabaseException {
        BasicBSONObject bSONObject = customFieldService.makeBSONObject(stockDataBean.getStockCustom(), stockDataBean.getStockDbType(), HkSystemConstantUtil.Feature.SELL, loginDataBean.getCompanyId(), null,null);
        UMFeatureDetailDataBean feature = applicationUtil.getuMFeatureDetailDataBeanMap().get(HkSystemConstantUtil.Feature.SELL);
        Map<String, String> dbFieldWithFormulaMap = null;
//        Commmented by Shifa temporarly
//        customFieldService.retrieveFeatureNameWithDbFieldListForFormula(feature.getId(), loginDataBean.getCompanyId(), HkSystemConstantUtil.FeatureForFormulaEvaluation.SELL_STOCK, HkSystemConstantUtil.CustomField.ComponentType.FORMULA);
        Object sellNumber = null;
        if (stockDataBean.getLotDataBean() != null && stockDataBean.getPacketDataBean() != null) {
            sellNumber = stockService.sellLot(bSONObject, loginDataBean.getCompanyId(), loginDataBean.getId(), null, new ObjectId(stockDataBean.getPacketDataBean().getId()), dbFieldWithFormulaMap);
        } else {
            sellNumber = stockService.sellLot(bSONObject, loginDataBean.getCompanyId(), loginDataBean.getId(), new ObjectId(stockDataBean.getLotDataBean().getId()), null, dbFieldWithFormulaMap);
        }

        return sellNumber;
    }

    private List<HkFeatureFieldPermissionDocument> retrieveAllFields() {

        List<HkFieldDocument> fieldEntityList = null;
        //Retrieved FeatureId
        UMFeatureDetailDataBean feature = applicationUtil.getuMFeatureDetailDataBeanMap().get("stocksell");
        List<HkFeatureFieldPermissionDocument> fieldPermissionEntitys = new ArrayList<>();
        if (feature != null) {
            fieldPermissionEntitys = customFieldService.retrieveFeatureFieldPermissions(feature.getId(), loginDataBean.getRoleIds());
        }
        return fieldPermissionEntitys;
    }
}
