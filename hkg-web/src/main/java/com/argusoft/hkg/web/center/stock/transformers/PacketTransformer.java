/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.center.stock.transformers;

import com.argusoft.hkg.common.constantutil.HkSystemConstantUtil;
import com.argusoft.hkg.common.functionutil.FolderManagement;
import com.argusoft.hkg.nosql.core.HkCustomFieldService;
import com.argusoft.hkg.nosql.core.HkStockService;
import com.argusoft.hkg.nosql.core.HkUserService;
import com.argusoft.hkg.nosql.model.HkInvoiceDocument;
import com.argusoft.hkg.nosql.model.HkLotDocument;
import com.argusoft.hkg.nosql.model.HkPacketDocument;
import com.argusoft.hkg.nosql.model.HkParcelDocument;
import com.argusoft.hkg.web.center.stock.databeans.PacketDataBean;
import com.argusoft.hkg.web.center.stock.databeans.RuleDetailsDataBean;
import com.argusoft.hkg.web.center.usermanagement.databeans.LoginDataBean;
import com.argusoft.hkg.web.center.usermanagement.databeans.UMFeatureDetailDataBean;
import com.argusoft.hkg.web.center.util.ApplicationUtil;
import com.argusoft.hkg.web.center.util.GenerateBarcodeUtil;
import com.argusoft.hkg.web.util.SelectItem;
import com.argusoft.sync.center.model.HkFieldDocument;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.sf.dynamicreports.report.exception.DRException;
import net.sf.jasperreports.engine.JRException;
import org.bson.BasicBSONObject;
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
public class PacketTransformer {

    @Autowired
    HkStockService stockService;

    @Autowired
    LoginDataBean loginDataBean;

    @Autowired
    HkCustomFieldService customFieldService;

    @Autowired
    ApplicationUtil applicationUtil;

    @Autowired
    StockTransformer stockTransformer;

    @Autowired
    GenerateBarcodeUtil generateBarcodeUtil;

    @Autowired
    HkUserService hkUserService;

    @Autowired
    RuleExecutionTransformer centerRuleExecutionTransformer;

    private static final Logger LOGGER = LoggerFactory.getLogger(PacketTransformer.class);

    public List<SelectItem> retrievePacketByLotId(Map<String, Object> map) {
        List<SelectItem> selectItems = null;
        List<ObjectId> lotIds = new ArrayList<>();
        if (!CollectionUtils.isEmpty(map)) {
            List<String> list = (List<String>) map.get("lotObjectId");
            ObjectId lotId = new ObjectId(list.get(0));
            List<ObjectId> invoiceIds = new ArrayList<>();
            List<ObjectId> parcelIds = new ArrayList<>();
            lotIds.add(lotId);
            List<String> invoiceDbFieldNames = (List<String>) map.get("invoiceDbFieldName");
            List<String> parcelDbFieldNames = (List<String>) map.get("parcelDbFieldName");
            List<String> lotDbFieldNames = (List<String>) map.get("lotDbFieldName");
            List<String> packetDbFieldNames = (List<String>) map.get("packetDbFieldName");

            Map<String, Object> map1 = (Map<String, Object>) map.get("ruleConfigMap");
            Map<String, String> fieldIdToNameMap = new HashMap<>();
            Set<Long> fieldIds = new HashSet<>();
            if (!CollectionUtils.isEmpty(map1) && map1.get("fieldIdNameMap") != null) {
                fieldIdToNameMap = (Map<String, String>) map1.get("fieldIdNameMap");
                for (String lng : fieldIdToNameMap.keySet()) {
                    fieldIds.add(Long.parseLong(lng));
                }
            }
            System.out.println("fieldIds-----" + fieldIds);
            HkLotDocument lotDocument = null;
            lotDocument = stockService.retrieveLotById(lotId);
            selectItems = new ArrayList<>();
            if (lotDocument != null) {
                SelectItem parcelItem = new SelectItem(lotDocument.getId().toString(), lotDocument.getParcel().toString(), lotDocument.getInvoice().toString());
                BasicBSONObject fieldValue = lotDocument.getFieldValue();
                if (fieldValue != null && !CollectionUtils.isEmpty(lotDbFieldNames)) {
                    Map<Object, Object> mapToSentOnUI = new HashMap<>();
                    for (String dbField : lotDbFieldNames) {
                        if (fieldValue.toMap().containsKey(dbField)) {
                            mapToSentOnUI.put(dbField, fieldValue.toMap().get(dbField));
                        }
                    }
                    parcelItem.setCustom4(mapToSentOnUI);
                }
                HkInvoiceDocument invoiceDocument = stockService.retrieveInvoiceById(lotDocument.getInvoice());

                if (invoiceDocument != null) {
                    invoiceIds.add(invoiceDocument.getId());
                    BasicBSONObject invoiceFieldValue = invoiceDocument.getFieldValue();
                    if (invoiceFieldValue != null && !CollectionUtils.isEmpty(invoiceDbFieldNames)) {
                        Map<Object, Object> mapToSentOnUI = new HashMap<>();
                        for (String dbField : invoiceDbFieldNames) {
                            if (invoiceFieldValue.toMap().containsKey(dbField)) {
                                mapToSentOnUI.put(dbField, invoiceFieldValue.toMap().get(dbField));
                            }
                        }
                        parcelItem.setCustom1(mapToSentOnUI);
                    }
                }
                selectItems.add(parcelItem);

                HkParcelDocument parcelDocument = stockService.retrieveParcelById(lotDocument.getParcel());

                if (parcelDocument != null) {
                    parcelIds.add(parcelDocument.getId());
                    BasicBSONObject parcelFieldValue = parcelDocument.getFieldValue();
                    if (parcelFieldValue != null && !CollectionUtils.isEmpty(parcelDbFieldNames)) {
                        Map<Object, Object> mapToSentOnUI = new HashMap<>();
                        for (String dbField : parcelDbFieldNames) {
                            if (parcelFieldValue.toMap().containsKey(dbField)) {
                                mapToSentOnUI.put(dbField, parcelFieldValue.toMap().get(dbField));
                            }
                        }
                        parcelItem.setCustom3(mapToSentOnUI);
                    }
                }
                selectItems.add(parcelItem);
            }

            List<HkPacketDocument> packetDocuments = stockService.retrievePackets(null, invoiceIds, parcelIds, lotIds, loginDataBean.getCompanyId(), Boolean.FALSE, null, null, null, null);
            Map<String, Map<String, RuleDetailsDataBean>> packetFieldRuleDetailsMap = null;
            if (!CollectionUtils.isEmpty(map1) && map1.get("featureName") != null) {
                packetFieldRuleDetailsMap = centerRuleExecutionTransformer.prepareDataAndExecuteScreenRules(packetDocuments, new ArrayList<>(fieldIds), map1.get("featureName").toString(), HkSystemConstantUtil.Feature.PACKET);
            }
            if (!CollectionUtils.isEmpty(packetDocuments)) {
                Map<String, String> mapOfDbFieldWithCurrencyCode = null;
                if (!CollectionUtils.isEmpty(packetDbFieldNames)) {
                    for (String dbField : packetDbFieldNames) {
                        String[] split = dbField.split("\\" + HkSystemConstantUtil.SEPERATOR_FOR_CUSTOM_FIELD_LABEL);
                        String componentType = split[1];
                        if (componentType.equals(HkSystemConstantUtil.CustomField.COMPONENT_CODE_MAP.get(HkSystemConstantUtil.CustomField.ComponentType.CURRENCY))) {
                            mapOfDbFieldWithCurrencyCode = customFieldService.mapOfDbFieldWithCurrencyCode(loginDataBean.getCompanyId());
                            break;
                        }
                    }
                }
                for (HkPacketDocument packetDocument : packetDocuments) {
                    Map<String, Object> mapToSentOnUI = new HashMap<>();
                    SelectItem selectItem = new SelectItem(packetDocument.getId().toString(), packetDocument.getLot().toString(), packetDocument.getParcel().toString(), packetDocument.getInvoice().toString());
                    selectItem.setCustom7(packetDocument.getFranchiseId());
                    selectItem.setOtherId(packetDocument.getSequenceNumber());
                    BasicBSONObject fieldValue = packetDocument.getFieldValue();
                    if (fieldValue != null) {
                        Map fieldValueMapFromDb = fieldValue.toMap();
                        if (!CollectionUtils.isEmpty(fieldValueMapFromDb) && !CollectionUtils.isEmpty(packetDbFieldNames)) {
                            if (packetDbFieldNames.indexOf(HkSystemConstantUtil.AutoNumber.PACKET_ID) == -1) {
                                mapToSentOnUI.put(HkSystemConstantUtil.AutoNumber.PACKET_ID, fieldValueMapFromDb.get(HkSystemConstantUtil.AutoNumber.PACKET_ID));
                            }
                            for (String packetField : packetDbFieldNames) {
                                String[] split = packetField.split("\\" + HkSystemConstantUtil.SEPERATOR_FOR_CUSTOM_FIELD_LABEL);
                                String componentType = split[1];
                                if (componentType.equals(HkSystemConstantUtil.CustomField.COMPONENT_CODE_MAP.get(HkSystemConstantUtil.CustomField.ComponentType.CURRENCY))) {
                                    if (fieldValue.toMap().containsKey(packetField) && fieldValue.toMap().get(packetField) != null) {
                                        mapToSentOnUI.put(packetField, fieldValue.toMap().get(packetField));
                                    }
                                    if (fieldValue.toMap().containsKey(packetField + HkSystemConstantUtil.CURRENCY_CODE_CUSTOM)) {
                                        mapToSentOnUI.put(packetField + HkSystemConstantUtil.CURRENCY_CODE_CUSTOM, fieldValue.toMap().get(packetField + HkSystemConstantUtil.CURRENCY_CODE_CUSTOM));
                                    } else {
                                        if (mapOfDbFieldWithCurrencyCode != null && !mapOfDbFieldWithCurrencyCode.isEmpty()) {
                                            if (mapOfDbFieldWithCurrencyCode.containsKey(packetField)) {
                                                String currencyCodeVal = mapOfDbFieldWithCurrencyCode.get(packetField);
                                                if (StringUtils.hasLength(currencyCodeVal) && StringUtils.hasText(currencyCodeVal)) {
                                                    String currencyCode = currencyCodeVal.toString().replaceAll("^\"|\"$", "");
                                                    mapToSentOnUI.put(packetField + HkSystemConstantUtil.CURRENCY_CODE_CUSTOM, currencyCode);
                                                }

                                            }
                                        }
                                    }
                                } else {
                                    mapToSentOnUI.put(packetField, fieldValueMapFromDb.get(packetField));
                                }
                            }
                        }
                    }
                    if (!CollectionUtils.isEmpty(mapToSentOnUI)) {
                        selectItem.setCategoryCustom(mapToSentOnUI);
                    }
                    //Set screen rule related details.
                    if (packetFieldRuleDetailsMap != null && !CollectionUtils.isEmpty(packetFieldRuleDetailsMap)) {
                        System.out.println("packetFieldRuleDetailsMap:::" + packetFieldRuleDetailsMap);
                        System.out.println("fieldIdToNameMap:::" + fieldIdToNameMap);
                        if (!CollectionUtils.isEmpty(packetFieldRuleDetailsMap.get(packetDocument.getId().toString()))) {
                            Map<String, RuleDetailsDataBean> screenRuleDetailsWithDbFieldName = new HashMap<>();
                            Map<String, RuleDetailsDataBean> ruleDetails = packetFieldRuleDetailsMap.get(packetDocument.getId().toString());
                            for (Map.Entry<String, RuleDetailsDataBean> entry : ruleDetails.entrySet()) {
                                if (fieldIdToNameMap.get(entry.getKey()) != null) {
                                    screenRuleDetailsWithDbFieldName.put(fieldIdToNameMap.get(entry.getKey()), entry.getValue());
                                }
                            }
                            selectItem.setScreenRuleDetailsWithDbFieldName(screenRuleDetailsWithDbFieldName);
                        }
                    }
                    selectItems.add(selectItem);
                }
            }
        }
        return selectItems;
    }

    public String updatePacket(PacketDataBean packetDataBean) {
        Map<String, Object> packetCustom = packetDataBean.getPacketCustom();
        List<String> uiFieldList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(packetDataBean.getPacketDbType())) {
            for (Map.Entry<String, String> entrySet : packetDataBean.getPacketDbType().entrySet()) {
                uiFieldList.add(entrySet.getKey());
            }
        }
        Map<String, String> uiFieldMap = customFieldService.retrieveUIFieldNameWithComponentTypes(uiFieldList);
        UMFeatureDetailDataBean feature = applicationUtil.getuMFeatureDetailDataBeanMap().get(HkSystemConstantUtil.Feature.PACKET);
        Map<String, HkFieldDocument> mapForFeatureInvolvedInFormula = customFieldService.retrieveMapOfDbFieldNameForFeatureInvolvedInFormula(loginDataBean.getCompanyId(), HkSystemConstantUtil.Feature.PACKET, HkSystemConstantUtil.CustomField.ComponentType.FORMULA);

            List<HkFieldDocument> totalformulaList =customFieldService.retrieveAllFormulaList(feature.getId(), loginDataBean.getCompanyId(), HkSystemConstantUtil.FeatureForFormulaEvaluation.EDIT_PACKET, HkSystemConstantUtil.CustomField.ComponentType.FORMULA);
        //---------------- Last parameter is work allocation id ----------//
        //------------- currently I have passed null, integrate it with UI - dmehta ------------//
        return stockService.updatePacket(new ObjectId(packetDataBean.getId()), packetCustom, packetDataBean.getPacketDbType(), mapForFeatureInvolvedInFormula, uiFieldMap, totalformulaList, applicationUtil.getFeatureIdNameMap(), loginDataBean.getCompanyId(), loginDataBean.getId(), feature.getId());
    }

    public SelectItem retrievePacketById(String packetId) {
        HkPacketDocument packetDocument = stockService.retrievePacketById(new ObjectId(packetId), true);
        if (packetDocument != null) {
            ObjectId invoice = packetDocument.getInvoice();
            ObjectId parcelId = packetDocument.getParcel();
            ObjectId lotId = packetDocument.getLot();
            HkInvoiceDocument invoiceDocument = stockService.retrieveInvoiceById(invoice);
            HkParcelDocument parcelDocument = stockService.retrieveParcelById(parcelId);
            HkLotDocument lotDocument = stockService.retrieveLotById(lotId);
            if (invoiceDocument != null && parcelDocument != null && lotDocument != null) {
                SelectItem item = new SelectItem(packetId, lotId.toString(), parcelId.toString());
                item.setCustom1(packetDocument.getFieldValue().toMap());
                item.setCustom3(invoiceDocument.getFieldValue().toMap());
                item.setCustom4(parcelDocument.getFieldValue().toMap());
                item.setCustom5(lotDocument.getFieldValue().toMap());
                item.setCustom7(packetDocument.getFranchiseId());
                return item;
            } else {
                return null;
            }
        } else {
            return null;
        }

    }

    public Map<String, Object> createPacket(PacketDataBean packetDataBean) {
        Map<String, Object> resultMap = new HashMap<>();
        if (packetDataBean != null) {
            List<String> uiList = new ArrayList<>();
            if (!CollectionUtils.isEmpty(packetDataBean.getPacketCustom())) {
                for (Map.Entry<String, String> entry : packetDataBean.getPacketDbType().entrySet()) {
                    String key = entry.getKey();
                    String type = entry.getValue();
                    uiList.add(key);
                }
            }
            Map<String, Object> map1 = packetDataBean.getRuleConfigMap();
            Map<String, String> fieldIdToNameMap = new HashMap<>();
            Set<Long> fieldIds = new HashSet<>();
            if (!CollectionUtils.isEmpty(map1) && map1.get("fieldIdNameMap") != null) {
                fieldIdToNameMap = (Map<String, String>) map1.get("fieldIdNameMap");
                for (String lng : fieldIdToNameMap.keySet()) {
                    fieldIds.add(Long.parseLong(lng));
                }
            }
            System.out.println("fieldIds-----" + fieldIds);
            if (!CollectionUtils.isEmpty(uiList)) {
                Map<String, String> uIFieldNameWithComponentTypes = customFieldService.retrieveUIFieldNameWithComponentTypes(uiList);
                String pointerComponentType = HkSystemConstantUtil.CustomField.ComponentType.POINTER + "@*" + HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN;
                if (!CollectionUtils.isEmpty(packetDataBean.getPacketCustom())) {
                    for (Map.Entry<String, Object> entry : packetDataBean.getPacketCustom().entrySet()) {
                        String key = entry.getKey();
                        if (key != null && uIFieldNameWithComponentTypes.containsKey(key)) {
                            String type = uIFieldNameWithComponentTypes.get(key);
                            if (type.equals(HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN) || type.equals(HkSystemConstantUtil.CustomField.ComponentType.USER_MULTISELECT) || type.equals(pointerComponentType)) {
                                if (packetDataBean.getPacketDbType().containsKey(key)) {
                                    packetDataBean.getPacketDbType().put(key, HkSystemConstantUtil.CustomField.DbFieldType.ARRAY);
                                }
                                if (packetDataBean.getPacketCustom().containsKey(key)) {
                                    String customVal = packetDataBean.getPacketCustom().get(key).toString();
                                    List<String> values = new ArrayList<>();
                                    String[] valueArray = customVal.replace("\"", "").split(",");
                                    for (String v : valueArray) {
                                        values.add(v.replace("\"", ""));
                                    }
                                    packetDataBean.getPacketCustom().put(key, values);
                                }
                            }
                        }

                    }
                }
                BasicBSONObject bSONObject = customFieldService.makeBSONObject(packetDataBean.getPacketCustom(), packetDataBean.getPacketDbType(), HkSystemConstantUtil.Feature.PACKET, loginDataBean.getCompanyId(), null, null);
                if (bSONObject != null) {
                    UMFeatureDetailDataBean feature = applicationUtil.getuMFeatureDetailDataBeanMap().get(HkSystemConstantUtil.Feature.PACKET);
                        List<HkFieldDocument> totalformulaList= customFieldService.retrieveAllFormulaList(feature.getId(), loginDataBean.getCompanyId(), HkSystemConstantUtil.FeatureForFormulaEvaluation.ADD_PACKET, HkSystemConstantUtil.CustomField.ComponentType.FORMULA);
                    //Here loginDataBean.getcompanyId is passed to set createdByFranchise in lotDocument. and normal franchise will be lot's franchise itself
                    HkPacketDocument packetDocument = stockService.createpacket(bSONObject, totalformulaList, loginDataBean.getCompanyId(), loginDataBean.getId(), new ObjectId(packetDataBean.getInvoiceDataBean().getId()), new ObjectId(packetDataBean.getParcelDataBean().getId()), new ObjectId(packetDataBean.getLotDataBean().getId()), feature.getId(), packetDataBean.getSequenceNumber());
                    
                    Map<String, Map<String, RuleDetailsDataBean>> lotFieldRuleDetailsMap = null;
                    if (!CollectionUtils.isEmpty(map1) && map1.get("featureName") != null) {
                        lotFieldRuleDetailsMap = centerRuleExecutionTransformer.prepareDataAndExecuteScreenRules(Arrays.asList(packetDocument), new ArrayList<>(fieldIds), map1.get("featureName").toString(), HkSystemConstantUtil.Feature.PACKET);
                    }
                    //Set screen rule related details.
                    if (lotFieldRuleDetailsMap != null && !CollectionUtils.isEmpty(lotFieldRuleDetailsMap)) {
                        System.out.println("lotFieldRuleDetailsMap:::" + lotFieldRuleDetailsMap);
                        System.out.println("fieldIdToNameMap:::" + fieldIdToNameMap);
                        if (!CollectionUtils.isEmpty(lotFieldRuleDetailsMap.get(packetDocument.getId().toString()))) {
                            Map<String, RuleDetailsDataBean> screenRuleDetailsWithDbFieldName = new HashMap<>();
                            Map<String, RuleDetailsDataBean> ruleDetails = lotFieldRuleDetailsMap.get(packetDocument.getId().toString());
                            for (Map.Entry<String, RuleDetailsDataBean> entry : lotFieldRuleDetailsMap.get(packetDocument.getId().toString()).entrySet()) {
                                if (fieldIdToNameMap.get(entry.getKey()) != null) {
                                    screenRuleDetailsWithDbFieldName.put(fieldIdToNameMap.get(entry.getKey()), entry.getValue());
                                }
                            }
                            resultMap.put("screenRuleDetailsWithDbFieldName", screenRuleDetailsWithDbFieldName);
                        }
                    }
                    System.out.println("packetDocument.getId().toString():::" + packetDocument.getId().toString());
                    System.out.println("bSONObject:::" + bSONObject.toMap());
                    resultMap.put(packetDocument.getId().toString(), bSONObject.get(HkSystemConstantUtil.PacketStaticFieldName.PACKET_ID).toString());
                    resultMap.put("packetId", packetDocument.getId().toString());
                    resultMap.put(HkSystemConstantUtil.PacketStaticFieldName.PACKET_ID, bSONObject.get(HkSystemConstantUtil.PacketStaticFieldName.PACKET_ID).toString());
                }
            }
        }
        return resultMap;
    }

//    public SelectItem retriveStockFromWorkAllocation(Map<String, List<String>> featureDbfieldNameMap) {
//        Long companyId = loginDataBean.getCompanyId();
//        List<HkPacketDocument> packetDocuments = workAllotmentService.retrieveLotsOrPacketsFromWorkAllocation(null, Arrays.asList(loginDataBean.getId()), null, Arrays.asList(HkSystemConstantUtil.ActivityServiceStatus.IN_PROCESS), Arrays.asList(HkSystemConstantUtil.SERVICE_CODE.EDIT_PACKET), companyId, Boolean.TRUE, Boolean.TRUE);
//        Set<ObjectId> workAllotmentIds = new HashSet<>();
//        if (!CollectionUtils.isEmpty(packetDocuments)) {
//            for (HkPacketDocument document : packetDocuments) {
////                workAllotmentIds.add(document.getWorkAllotmentId());
//            }
//        }
//        SelectItem item = new SelectItem();
//        List<SelectItem> packetItems = null;
//        DynamicServiceInitDataBean dynamicServiceInitDataBean = null;
//        if ((!CollectionUtils.isEmpty(packetDocuments)) && !CollectionUtils.isEmpty(featureDbfieldNameMap)) {
//            List<HkWorkAllotmentDocument> workAllotmentDocuments = workAllotmentService.retrieveworkAllotmentsByIds(new ArrayList<ObjectId>(workAllotmentIds), companyId);
//            if (!CollectionUtils.isEmpty(workAllotmentDocuments)) {
//                dynamicServiceInitDataBean = stockTransformer.retrieveNodeInfo(new ArrayList<ObjectId>(workAllotmentIds), companyId, HkSystemConstantUtil.SERVICE_CODE.WRITE_SERVICE);
//                List<DynamicServiceInitDataBean> dynamicServiceInitDataBeans = dynamicServiceInitDataBean.getDynamicServiceInitDataBeans();
//            } else {
//                // LOGGER.info("work allotment docs are empty");
//            }
//
//            Boolean invoiceFieldExist = false;
//            Boolean parcelFieldExist = false;
//            Boolean lotFieldExist = false;
//            Boolean packetFieldExist = false;
//            List<String> invoiceDbFieldNames = null;
//            List<String> parcelDbFieldNames = null;
//            List<String> lotDbFieldNames = null;
//            List<String> packetDbFieldNames = null;
//            for (Map.Entry<String, List<String>> entry : featureDbfieldNameMap.entrySet()) {
//                String featureName = entry.getKey();
//                if (featureName.equalsIgnoreCase(HkSystemConstantUtil.Feature.INVOICE)) {
//                    invoiceFieldExist = true;
//                    invoiceDbFieldNames = entry.getValue();
//                } else if (featureName.equalsIgnoreCase(HkSystemConstantUtil.Feature.PARCEL)) {
//                    parcelFieldExist = true;
//                    parcelDbFieldNames = entry.getValue();
//                } else if (featureName.equalsIgnoreCase(HkSystemConstantUtil.Feature.LOT)) {
//                    lotFieldExist = true;
//                    lotDbFieldNames = entry.getValue();
//                } else if (featureName.equalsIgnoreCase(HkSystemConstantUtil.Feature.PACKET)) {
//                    packetFieldExist = true;
//                    packetDbFieldNames = entry.getValue();
//                }
//            }
//
//            //            retrieve packet info
//            if (!CollectionUtils.isEmpty(packetDocuments)) {
//                List<ObjectId> invoiceIds = new ArrayList<>();
//                List<ObjectId> parcelIds = new ArrayList<>();
//                List<ObjectId> lotIds = new ArrayList<>();
//                for (HkPacketDocument packetDocument : packetDocuments) {
//                    invoiceIds.add(packetDocument.getInvoice());
//                    parcelIds.add(packetDocument.getParcel());
//                    lotIds.add(packetDocument.getLot());
//                }
//                List<HkInvoiceDocument> invoiceDocuments = null;
//                if (invoiceFieldExist) {
//                    UMFeatureDetailDataBean invoiceFeature = null;
//                    invoiceFeature = applicationUtil.getuMFeatureDetailDataBeanMap().get(HkSystemConstantUtil.Feature.INVOICE);;
//                    if (invoiceFeature != null) {
//                        Map<String, String> uiFieldMapForInvoice = new HashMap<>();
////                        uiFieldMapForInvoice = customFieldService.retrieveCustomUIFieldNameWithComponentTypes(invoiceFeature.getId(), companyId);
//                        invoiceDocuments = stockService.retrieveInvoices(null, companyId, Boolean.FALSE, invoiceIds, null);
//                    }
//                }
//                List<HkParcelDocument> parcelDocuments = null;
//                if (parcelFieldExist) {
//                    UMFeatureDetailDataBean parcelFeature = null;
//                    parcelFeature = applicationUtil.getuMFeatureDetailDataBeanMap().get(HkSystemConstantUtil.Feature.PARCEL);;
//                    if (parcelFeature != null) {
//                        Map<String, String> uiFieldMapForParcel = new HashMap<>();
////                        uiFieldMapForParcel = customFieldService.retrieveCustomUIFieldNameWithComponentTypes(parcelFeature.getId(), companyId);
//                        parcelDocuments = stockService.retrieveParcels(null, null, parcelIds, companyId, Boolean.FALSE, null, HkSystemConstantUtil.StockStatus.NEW_ROUGH);
//                    }
//                }
//                List<HkLotDocument> lotDocuments = null;
//                if (lotFieldExist) {
//                    UMFeatureDetailDataBean lotFeature = null;
//                    lotFeature = applicationUtil.getuMFeatureDetailDataBeanMap().get(HkSystemConstantUtil.Feature.LOT);
//                    if (lotFeature != null) {
//                        Map<String, String> uiFieldMapForLot = new HashMap<>();
////                        uiFieldMapForLot = customFieldService.retrieveCustomUIFieldNameWithComponentTypes(lotFeature.getId(), companyId);
//                        lotDocuments = stockService.retrieveLots(null, null, null, lotIds, companyId, Boolean.FALSE);
//                    }
//                }
//                packetItems = new ArrayList<>();
//                for (HkPacketDocument packetDocument : packetDocuments) {
//                    Map<Object, Object> mapToSentOnUI = new HashMap<>();
//                    SelectItem selectItem = new SelectItem(packetDocument.getLot().toString(), packetDocument.getParcel().toString(), packetDocument.getInvoice().toString(), packetDocument.getId().toString());
////                    selectItem.setStatus(packetDocument.getWorkAllotmentId().toString());
//                    if (packetFieldExist && !CollectionUtils.isEmpty(packetDbFieldNames)) {
//                        BasicBSONObject fieldValue = packetDocument.getFieldValue();
//                        if (packetDbFieldNames.indexOf(HkSystemConstantUtil.MANDATORY_FIELD.CARAT_OF_PACKET) == -1) {
//                            mapToSentOnUI.put(HkSystemConstantUtil.MANDATORY_FIELD.CARAT_OF_PACKET, fieldValue.toMap().get(HkSystemConstantUtil.MANDATORY_FIELD.CARAT_OF_PACKET));
//                        }
//                        if (fieldValue != null) {
//                            Map fieldValueMapFromDb = fieldValue.toMap();
//                            if (!CollectionUtils.isEmpty(fieldValueMapFromDb)) {
//                                for (String packetField : packetDbFieldNames) {
//                                    mapToSentOnUI.put(packetField, fieldValueMapFromDb.get(packetField));
//                                }
//                            }
//                        }
//                    }
//                    if (invoiceFieldExist && !CollectionUtils.isEmpty(invoiceDbFieldNames) && !CollectionUtils.isEmpty(invoiceDocuments)) {
//                        for (HkInvoiceDocument invoiceDocument : invoiceDocuments) {
//                            if (invoiceDocument.getId().toString().equals(packetDocument.getInvoice().toString())) {
//                                BasicBSONObject fieldValue = invoiceDocument.getFieldValue();
//                                if (fieldValue != null) {
//                                    Map fieldValueMapFromDb = fieldValue.toMap();
//                                    if (!CollectionUtils.isEmpty(fieldValueMapFromDb)) {
//                                        for (String invoiceField : invoiceDbFieldNames) {
//                                            mapToSentOnUI.put(invoiceField, fieldValueMapFromDb.get(invoiceField));
//                                        }
//                                    }
//                                    break;
//                                }
//                            }
//                        }
//                    }
//                    if (parcelFieldExist && !CollectionUtils.isEmpty(parcelDbFieldNames) && !CollectionUtils.isEmpty(parcelDocuments)) {
//                        for (HkParcelDocument parcelDocument : parcelDocuments) {
//                            if (parcelDocument.getId().toString().equals(packetDocument.getParcel().toString())) {
//                                BasicBSONObject fieldValue = parcelDocument.getFieldValue();
//                                if (fieldValue != null) {
//                                    Map fieldValueMapFromDb = fieldValue.toMap();
//                                    if (!CollectionUtils.isEmpty(fieldValueMapFromDb)) {
//                                        for (String parcelField : parcelDbFieldNames) {
//                                            mapToSentOnUI.put(parcelField, fieldValueMapFromDb.get(parcelField));
//                                        }
//                                    }
//                                }
//                                break;
//                            }
//                        }
//                    }
//                    if (lotFieldExist && !CollectionUtils.isEmpty(lotDbFieldNames) && !CollectionUtils.isEmpty(lotDocuments)) {
//                        for (HkLotDocument lotDocument : lotDocuments) {
//                            if (lotDocument.getId().toString().equals(packetDocument.getLot().toString())) {
//                                BasicBSONObject fieldValue = lotDocument.getFieldValue();
//                                if (fieldValue != null) {
//                                    Map fieldValueMapFromDb = fieldValue.toMap();
//                                    if (!CollectionUtils.isEmpty(fieldValueMapFromDb)) {
//                                        for (String lotField : lotDbFieldNames) {
//                                            mapToSentOnUI.put(lotField, fieldValueMapFromDb.get(lotField));
//                                        }
//                                    }
//                                    break;
//                                }
//                            }
//                        }
//                    }
//                    selectItem.setCustom1(mapToSentOnUI);
//                    packetItems.add(selectItem);
//                }
//            }
//        }
//        item.setCustom2(packetItems);
//        item.setServiceInitDataBean(dynamicServiceInitDataBean);
//        return item;
//    }
    public List<SelectItem> retrievePacketById(Map<String, Object> featureDbfieldNameMap) {
        List<SelectItem> selectItems = new ArrayList<>();
        if (!CollectionUtils.isEmpty(featureDbfieldNameMap)) {
            List<String> ids = (List<String>) featureDbfieldNameMap.get("packetId");
            Boolean invoiceFieldExist = false;
            Boolean parcelFieldExist = false;
            Boolean lotFieldExist = false;
            Boolean packetFieldExist = false;
            List<String> invoiceDbFieldNames = null;
            List<String> parcelDbFieldNames = null;
            List<String> lotDbFieldNames = null;
            List<String> packetDbFieldNames = null;
            Map<String, Object> map1 = (Map<String, Object>) featureDbfieldNameMap.get("ruleConfigMap");
            Map<String, String> fieldIdToNameMap = new HashMap<>();
            Set<Long> fieldIds = new HashSet<>();
            if (!CollectionUtils.isEmpty(map1) && map1.get("fieldIdNameMap") != null) {
                fieldIdToNameMap = (Map<String, String>) map1.get("fieldIdNameMap");
                for (String lng : fieldIdToNameMap.keySet()) {
                    fieldIds.add(Long.parseLong(lng));
                }
            }
            System.out.println("fieldIds-----" + fieldIds);
            for (Map.Entry<String, Object> entry : featureDbfieldNameMap.entrySet()) {
                String featureName = entry.getKey();
                if (featureName.equalsIgnoreCase(HkSystemConstantUtil.Feature.INVOICE)) {
                    invoiceFieldExist = true;
                    invoiceDbFieldNames = (List<String>) entry.getValue();
                } else if (featureName.equalsIgnoreCase(HkSystemConstantUtil.Feature.PARCEL)) {
                    parcelFieldExist = true;
                    parcelDbFieldNames = (List<String>) entry.getValue();
                } else if (featureName.equalsIgnoreCase(HkSystemConstantUtil.Feature.LOT)) {
                    lotFieldExist = true;
                    lotDbFieldNames = (List<String>) entry.getValue();
                } else if (featureName.equalsIgnoreCase(HkSystemConstantUtil.Feature.PACKET)) {
                    packetFieldExist = true;
                    packetDbFieldNames = (List<String>) entry.getValue();
                }
            }
            Long companyId = loginDataBean.getCompanyId();

            if (!CollectionUtils.isEmpty(ids)) {
                HkPacketDocument packetDocument = stockService.retrievePacketById(new ObjectId(ids.get(0)), true);
                if (packetDocument != null && !CollectionUtils.isEmpty(featureDbfieldNameMap)) {
                    ObjectId packetId = packetDocument.getId();
                    ObjectId invoiceId = packetDocument.getInvoice();
                    ObjectId parcelId = packetDocument.getParcel();
                    ObjectId lotId = packetDocument.getLot();
                    SelectItem selectItem = null;
                    selectItem = new SelectItem(packetId.toString(), lotId.toString(), parcelId.toString(), invoiceId.toString());
                    if (invoiceFieldExist) {
                        HkInvoiceDocument invoiceDocument = stockService.retrieveInvoiceById(invoiceId);
                        Map<String, Object> invoiceFieldValueMap = invoiceDocument.getFieldValue().toMap();
                        if (!CollectionUtils.isEmpty(invoiceDbFieldNames)) {
                            Map<Object, Object> invoiceMap = new HashMap<>();
                            for (String invoiceField : invoiceDbFieldNames) {
                                Object value = invoiceFieldValueMap.get(invoiceField);
                                invoiceMap.put(invoiceField, value);
                            }
                            selectItem.setCustom1(invoiceMap);
                        }
                    }
                    if (parcelFieldExist) {
                        HkParcelDocument parcelDocument = stockService.retrieveParcelById(parcelId);
                        Map<String, Object> parcelFieldValueMap = new HashMap();
                        if (parcelDocument != null) {
                            parcelFieldValueMap = parcelDocument.getFieldValue().toMap();
                        }
                        if (!CollectionUtils.isEmpty(parcelDbFieldNames) && parcelDocument != null) {
                            Map<Object, Object> parcelMap = new HashMap<>();
                            for (String parcelField : parcelDbFieldNames) {
                                Object value = parcelFieldValueMap.get(parcelField);
                                parcelMap.put(parcelField, value);
                            }

                            selectItem.setCustom3(parcelMap);
                        }
                    }
                    HkLotDocument lotDocument = stockService.retrieveLotById(lotId);
                    if (lotFieldExist) {
                        Map<String, Object> lotFieldValueMap = new HashMap();
                        if (lotDocument != null) {
                            if (lotDocument.getFieldValue() != null) {
                                lotFieldValueMap = lotDocument.getFieldValue().toMap();
                            }
                        }
                        if (!CollectionUtils.isEmpty(lotDbFieldNames) && !CollectionUtils.isEmpty(lotFieldValueMap)) {
                            Map<Object, Object> lotMap = new HashMap<>();
                            for (String lotField : lotDbFieldNames) {
                                Object value = lotFieldValueMap.get(lotField);
                                lotMap.put(lotField, value);
                            }

                            selectItem.setCustom4(lotMap);
                        }
                    }
                    if (packetFieldExist) {
                        Map<String, Map<String, RuleDetailsDataBean>> packetFieldRuleDetailsMap = null;
                        if (!CollectionUtils.isEmpty(map1) && map1.get("featureName") != null) {
                            packetFieldRuleDetailsMap = centerRuleExecutionTransformer.prepareDataAndExecuteScreenRules(Arrays.asList(packetDocument), new ArrayList<>(fieldIds), map1.get("featureName").toString(), HkSystemConstantUtil.Feature.PACKET);
                        }
                        Map<String, Object> packetFieldValueMap = packetDocument.getFieldValue().toMap();
                        if (!CollectionUtils.isEmpty(packetDbFieldNames)) {
                            Map<String, String> mapOfDbFieldWithCurrencyCode = null;
                            for (String dbField : packetDbFieldNames) {
                                String[] split = dbField.split("\\" + HkSystemConstantUtil.SEPERATOR_FOR_CUSTOM_FIELD_LABEL);
                                String componentType = split[1];
                                if (componentType.equals(HkSystemConstantUtil.CustomField.COMPONENT_CODE_MAP.get(HkSystemConstantUtil.CustomField.ComponentType.CURRENCY))) {
                                    mapOfDbFieldWithCurrencyCode = customFieldService.mapOfDbFieldWithCurrencyCode(loginDataBean.getCompanyId());
                                    break;
                                }
                            }
                            Map<Object, Object> packetMap = new HashMap<>();
                            if (packetFieldValueMap != null) {
                                packetMap.put(HkSystemConstantUtil.LotStaticFieldName.STOCK_CARAT, packetFieldValueMap.get(HkSystemConstantUtil.PacketStaticFieldName.CARAT));
                                packetMap.put(HkSystemConstantUtil.LotStaticFieldName.STOCK_PIECES, packetFieldValueMap.get(HkSystemConstantUtil.PacketStaticFieldName.PIECES));
                            }
                            for (String packetField : packetDbFieldNames) {
                                String[] split = packetField.split("\\" + HkSystemConstantUtil.SEPERATOR_FOR_CUSTOM_FIELD_LABEL);
                                String componentType = split[1];
                                if (componentType.equals(HkSystemConstantUtil.CustomField.COMPONENT_CODE_MAP.get(HkSystemConstantUtil.CustomField.ComponentType.CURRENCY))) {
                                    if (packetFieldValueMap.containsKey(packetField) && packetFieldValueMap.get(packetField) != null) {
                                        packetMap.put(packetField, packetFieldValueMap.get(packetField));
                                    }
                                    if (packetFieldValueMap.containsKey(packetField + HkSystemConstantUtil.CURRENCY_CODE_CUSTOM)) {
                                        packetMap.put(packetField + HkSystemConstantUtil.CURRENCY_CODE_CUSTOM, packetFieldValueMap.get(packetField + HkSystemConstantUtil.CURRENCY_CODE_CUSTOM));
                                    } else {
                                        if (mapOfDbFieldWithCurrencyCode != null && !mapOfDbFieldWithCurrencyCode.isEmpty()) {
                                            if (mapOfDbFieldWithCurrencyCode.containsKey(packetField)) {
                                                String currencyCodeVal = mapOfDbFieldWithCurrencyCode.get(packetField);
                                                if (StringUtils.hasLength(currencyCodeVal) && StringUtils.hasText(currencyCodeVal)) {
                                                    String currencyCode = currencyCodeVal.toString().replaceAll("^\"|\"$", "");
                                                    packetMap.put(packetField + HkSystemConstantUtil.CURRENCY_CODE_CUSTOM, currencyCode);
                                                }

                                            }
                                        }
                                    }
                                } else {
                                    packetMap.put(packetField, packetFieldValueMap.get(packetField));
                                }
                            }
                            selectItem.setCustom5(packetMap);
                        }
                        selectItems.add(selectItem);
                        List<HkPacketDocument> packetDocuments = null;
                        System.out.println("-------------------------------------split packet retrieve--------------------------------------");
                        LOGGER.info("packetId : " + packetId);
                        if (packetId != null) {
                            packetDocuments = stockService.retrievePackets(null, null, null, null, loginDataBean.getCompanyId(), Boolean.FALSE, null, null, packetId, HkSystemConstantUtil.PacketStaticValueOfFields.PIECES);
                        }
                        if (!CollectionUtils.isEmpty(packetDocuments)) {
                            LOGGER.info("packetDocuments size : " + packetDocuments.size());
                            Map<String, String> mapOfDbFieldWithCurrencyCode = null;
                            LOGGER.info("packetDbFieldNames : " + packetDbFieldNames);
                            if (!CollectionUtils.isEmpty(packetDbFieldNames)) {
                                for (String dbField : packetDbFieldNames) {
                                    String[] split = dbField.split("\\" + HkSystemConstantUtil.SEPERATOR_FOR_CUSTOM_FIELD_LABEL);
                                    String componentType = split[1];
                                    if (componentType.equals(HkSystemConstantUtil.CustomField.COMPONENT_CODE_MAP.get(HkSystemConstantUtil.CustomField.ComponentType.CURRENCY))) {
                                        mapOfDbFieldWithCurrencyCode = customFieldService.mapOfDbFieldWithCurrencyCode(loginDataBean.getCompanyId());
                                        break;
                                    }
                                }
                            }
                            for (HkPacketDocument document : packetDocuments) {
                                Map<String, Object> mapToSentOnUI = new HashMap<>();
                                SelectItem selectItem1 = new SelectItem(document.getId().toString(), document.getLot().toString(), document.getParcel().toString(), document.getInvoice().toString());
                                BasicBSONObject fieldValue = document.getFieldValue();
                                if (fieldValue != null) {
                                    Map fieldValueMapFromDb = fieldValue.toMap();
                                    if (!CollectionUtils.isEmpty(fieldValueMapFromDb) && !CollectionUtils.isEmpty(packetDbFieldNames)) {
                                        if (packetDbFieldNames.indexOf(HkSystemConstantUtil.AutoNumber.PACKET_ID) == -1) {
                                            mapToSentOnUI.put(HkSystemConstantUtil.AutoNumber.PACKET_ID, fieldValueMapFromDb.get(HkSystemConstantUtil.AutoNumber.PACKET_ID));
                                        }
                                        LOGGER.info("---fieldValueMapFromDb--outside for loop--" + fieldValueMapFromDb);
                                        for (String packetField : packetDbFieldNames) {
                                            String[] split = packetField.split("\\" + HkSystemConstantUtil.SEPERATOR_FOR_CUSTOM_FIELD_LABEL);
                                            String componentType = split[1];
                                            if (componentType.equals(HkSystemConstantUtil.CustomField.COMPONENT_CODE_MAP.get(HkSystemConstantUtil.CustomField.ComponentType.CURRENCY))) {
                                                if (fieldValue.toMap().containsKey(packetField) && fieldValue.toMap().get(packetField) != null) {
                                                    mapToSentOnUI.put(packetField, fieldValue.toMap().get(packetField));
                                                }
                                                if (fieldValue.toMap().containsKey(packetField + HkSystemConstantUtil.CURRENCY_CODE_CUSTOM)) {
                                                    mapToSentOnUI.put(packetField + HkSystemConstantUtil.CURRENCY_CODE_CUSTOM, fieldValue.toMap().get(packetField + HkSystemConstantUtil.CURRENCY_CODE_CUSTOM));
                                                } else {
                                                    if (mapOfDbFieldWithCurrencyCode != null && !mapOfDbFieldWithCurrencyCode.isEmpty()) {
                                                        if (mapOfDbFieldWithCurrencyCode.containsKey(packetField)) {
                                                            String currencyCodeVal = mapOfDbFieldWithCurrencyCode.get(packetField);
                                                            if (StringUtils.hasLength(currencyCodeVal) && StringUtils.hasText(currencyCodeVal)) {
                                                                String currencyCode = currencyCodeVal.toString().replaceAll("^\"|\"$", "");
                                                                mapToSentOnUI.put(packetField + HkSystemConstantUtil.CURRENCY_CODE_CUSTOM, currencyCode);
                                                            }

                                                        }
                                                    }
                                                }
                                            } else {
                                                LOGGER.info("here----" + packetField + "---fieldValueMapFromDb--" + fieldValueMapFromDb.get(packetField));
                                                mapToSentOnUI.put(packetField, fieldValueMapFromDb.get(packetField));
                                            }
                                        }
                                    }
                                }
                                if (!CollectionUtils.isEmpty(mapToSentOnUI)) {
                                    LOGGER.info("mapToSentOnUI : " + mapToSentOnUI);
                                    selectItem1.setCategoryCustom(mapToSentOnUI);
                                }
                                //Set screen rule related details.                   
                                selectItems.add(selectItem1);
                            }
                        }
                        System.out.println("----------------------------------done-----------------------------------");
                        //Set screen rule related details.

                        if (packetFieldRuleDetailsMap != null && !CollectionUtils.isEmpty(packetFieldRuleDetailsMap)) {
                            System.out.println("lotFieldRuleDetailsMap:::" + packetFieldRuleDetailsMap);
                            System.out.println("fieldIdToNameMap:::" + fieldIdToNameMap);
                            if (!CollectionUtils.isEmpty(packetFieldRuleDetailsMap.get(packetDocument.getId().toString()))) {
                                Map<String, RuleDetailsDataBean> screenRuleDetailsWithDbFieldName = new HashMap<>();
                                Map<String, RuleDetailsDataBean> ruleDetails = packetFieldRuleDetailsMap.get(packetDocument.getId().toString());
                                for (Map.Entry<String, RuleDetailsDataBean> entry : ruleDetails.entrySet()) {
                                    if (fieldIdToNameMap.get(entry.getKey()) != null) {
                                        screenRuleDetailsWithDbFieldName.put(fieldIdToNameMap.get(entry.getKey()), entry.getValue());
                                    }
                                }
                                selectItem.setScreenRuleDetailsWithDbFieldName(screenRuleDetailsWithDbFieldName);
                            }
                        }
                    }

                }
            }

        }
        return selectItems;
    }

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

    public Map<String, Object> getNextPacketSequence(String lotObjectId) {
        Map<String, Object> packetSequenceMap = null;
        Long nextPacketSequence = null;
        if (lotObjectId != null && StringUtils.hasText(lotObjectId) && StringUtils.hasLength(lotObjectId)) {
            nextPacketSequence = stockService.getNextPacketSequence(loginDataBean.getCompanyId(), new ObjectId(lotObjectId));
        }
        if (nextPacketSequence != null) {
            packetSequenceMap = new HashMap<>();
            packetSequenceMap.put("sequenceNumber", nextPacketSequence);
        }
        return packetSequenceMap;
    }

    public Boolean isPacketSequenceExist(Map<String, String> franchiseSeqNumber) {
        Boolean lotSequenceExist = Boolean.FALSE;
        if (!CollectionUtils.isEmpty(franchiseSeqNumber)) {
            Long seqNumber = new Long(franchiseSeqNumber.get("seqNumber"));
            ObjectId lotId = new ObjectId(franchiseSeqNumber.get("lotId"));
            if (seqNumber != null && lotId != null) {
                lotSequenceExist = stockService.isPacketSequenceExist(seqNumber, loginDataBean.getCompanyId(), lotId);
            }
        }
        return lotSequenceExist;
    }

    private Map<String, Object> prepareAndValidateFieldMap(Map<String, Object> map, Map<String, Object> fieldMap) {
        Iterator<Map.Entry<String, Object>> iter = map.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<String, Object> entry = iter.next();
            String key = entry.getKey();
            Object value = entry.getValue();
            if (key.contains("$MS$") || key.contains("$UMS$") || key.contains("$AG$")) {
                if (value instanceof String && !StringUtils.isEmpty(value)) {
                    List<String> items = Arrays.asList(value.toString().split(","));
                    fieldMap.put(key, items);
                } else if (value instanceof Collection && !CollectionUtils.isEmpty((Collection<?>) value)) {
                    List<String> items = Arrays.asList(value.toString().split(","));
                    fieldMap.put(key, items);
                } else {
                    iter.remove();
                }
            } else {
                fieldMap.put(key, value);
            }
        }
        return fieldMap;
    }

    public List<SelectItem> searchPacket(PacketDataBean packetDataBean) {
        LOGGER.info("packetDataBean getFeatureCustomMapValue " + packetDataBean.getFeatureCustomMapValue());
        Long franchise = loginDataBean.getCompanyId();
        Map<String, List<String>> featureDbFieldMap = packetDataBean.getFeatureDbFieldMap();
        List<String> invoiceDbField = null;
        List<String> parcelDbField = null;
        List<String> lotDbField = null;
        List<String> packetDbField = null;
        if (!CollectionUtils.isEmpty(featureDbFieldMap)) {
            if (!CollectionUtils.isEmpty(featureDbFieldMap.get("invoice"))) {
                invoiceDbField = featureDbFieldMap.get("invoice");
            }
            if (!CollectionUtils.isEmpty(featureDbFieldMap.get("parcel"))) {
                parcelDbField = featureDbFieldMap.get("parcel");
            }
            if (!CollectionUtils.isEmpty(featureDbFieldMap.get("lot"))) {
                lotDbField = featureDbFieldMap.get("lot");
            }
            if (!CollectionUtils.isEmpty(featureDbFieldMap.get("packet"))) {
                packetDbField = featureDbFieldMap.get("packet");
            }
        }
        Map<String, Object> map1 = packetDataBean.getRuleConfigMap();
        Map<String, String> fieldIdToNameMap = new HashMap<>();
        Set<Long> fieldIds = new HashSet<>();
        if (!CollectionUtils.isEmpty(map1) && map1.get("fieldIdNameMap") != null) {
            fieldIdToNameMap = (Map<String, String>) map1.get("fieldIdNameMap");
            for (String lng : fieldIdToNameMap.keySet()) {
                fieldIds.add(Long.parseLong(lng));
            }
        }
//        System.out.println("fieldIds-----" + fieldIds);
        LOGGER.info(" invoiceDbField " + invoiceDbField);
        LOGGER.info(" parcelDbField " + parcelDbField);
        LOGGER.info(" lotDbField " + lotDbField);
        LOGGER.info(" packetDbField " + packetDbField);
        Map<String, Map<String, Object>> featureCustomMapValue = packetDataBean.getFeatureCustomMapValue();
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
                            this.prepareAndValidateFieldMap(map, invoiceFieldMap);
                        }
                    } else if (featureName.equalsIgnoreCase(HkSystemConstantUtil.Feature.PARCEL)) {
                        if (!CollectionUtils.isEmpty(map)) {
                            this.prepareAndValidateFieldMap(map, parcelFieldMap);
                        }
                    } else if (featureName.equalsIgnoreCase(HkSystemConstantUtil.Feature.LOT)) {
                        if (!CollectionUtils.isEmpty(map)) {
                            this.prepareAndValidateFieldMap(map, lotFieldMap);
                        }
                    } else if (featureName.equalsIgnoreCase(HkSystemConstantUtil.Feature.PACKET)) {
                        if (!CollectionUtils.isEmpty(map)) {
                            if (packetDataBean.getIsInStockOfLoggedInUser()) {
                                map.remove(HkSystemConstantUtil.PacketStaticFieldName.IN_STOCK_OF);
                            }
                            this.prepareAndValidateFieldMap(map, packetFieldMap);
                        }
                    }
                }
            }

            List<HkInvoiceDocument> invoiceDocuments = null;
            LOGGER.info(" invoiceFieldMap " + invoiceFieldMap);
            if (!CollectionUtils.isEmpty(invoiceFieldMap)) {
                invoiceDocuments = stockService.retrieveInvoices(invoiceFieldMap, franchise, Boolean.FALSE, null, null,null,null);
                if (CollectionUtils.isEmpty(invoiceDocuments)) {
                    LOGGER.info("invoice not found");
                    return null;
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
            LOGGER.info(" parcelFieldMap " + parcelFieldMap);
            if (!CollectionUtils.isEmpty(parcelFieldMap) || !CollectionUtils.isEmpty(invoiceIds)) {
                parcelDocuments = stockService.retrieveParcels(parcelFieldMap, invoiceIds, null, franchise, Boolean.FALSE, null, HkSystemConstantUtil.StockStatus.NEW_ROUGH,null,null);
                if (CollectionUtils.isEmpty(parcelDocuments)) {
                    LOGGER.info("parcel not found");
                    return null;
                }
            }
            List<ObjectId> parcelIds = null;
            if (!CollectionUtils.isEmpty(parcelDocuments)) {
                parcelIds = new ArrayList<>();
                for (HkParcelDocument parcelDocument : parcelDocuments) {
                    parcelIds.add(parcelDocument.getId());
                }
            }
            LOGGER.info(" lotFieldMap " + lotFieldMap);
            List<HkLotDocument> lotDocuments = null;
            if (!CollectionUtils.isEmpty(invoiceIds) || !CollectionUtils.isEmpty(parcelIds) || !CollectionUtils.isEmpty(lotFieldMap)) {
                lotDocuments = stockService.retrieveLots(lotFieldMap, invoiceIds, parcelIds, null, franchise, Boolean.FALSE);
                if (CollectionUtils.isEmpty(lotDocuments)) {
                    LOGGER.info("lot not found");
                    return null;
                }
            }
            List<ObjectId> lotIds = null;
            if (!CollectionUtils.isEmpty(lotDocuments)) {
                lotIds = new ArrayList<>();
                for (HkLotDocument lotDocument : lotDocuments) {
                    lotIds.add(lotDocument.getId());
                }
            }
            List<HkPacketDocument> packetDocuments = null;
            LOGGER.info(" packetFieldMap " + packetFieldMap + "invoiceIds" + invoiceIds + "parcelIds" + parcelIds + "lotIds" + lotIds);
            if (!CollectionUtils.isEmpty(invoiceIds) || !CollectionUtils.isEmpty(parcelIds) || !CollectionUtils.isEmpty(lotIds) || !CollectionUtils.isEmpty(packetFieldMap)) {
                String inStockOf = null;
                Long pieces = null;
                if (packetDataBean.getIsInStockOfLoggedInUser()) {
                    inStockOf = loginDataBean.getId().toString().concat(":").concat("E");
                    pieces = HkSystemConstantUtil.PacketStaticValueOfFields.PIECES;

                }
                packetDocuments = stockService.retrievePackets(packetFieldMap, invoiceIds, parcelIds, lotIds, franchise, Boolean.FALSE, inStockOf, null, null, pieces);
            }
            List<SelectItem> selectItems = null;
            if (!CollectionUtils.isEmpty(packetDocuments)) {
                Map<String, Map<String, RuleDetailsDataBean>> packetFieldRuleDetailsMap = null;
                if (!CollectionUtils.isEmpty(map1) && map1.get("featureName") != null) {
                    packetFieldRuleDetailsMap = centerRuleExecutionTransformer.prepareDataAndExecuteScreenRules(packetDocuments, new ArrayList<>(fieldIds), map1.get("featureName").toString(), HkSystemConstantUtil.Feature.PACKET);
                }
                selectItems = new ArrayList<>();
                invoiceIds = new ArrayList<>();
                parcelIds = new ArrayList<>();
                lotIds = new ArrayList<>();
                for (HkPacketDocument packetDocument : packetDocuments) {
                    invoiceIds.add(packetDocument.getInvoice());
                    parcelIds.add(packetDocument.getParcel());
                    lotIds.add(packetDocument.getLot());
                }
                List<HkInvoiceDocument> hkInvoiceDocuments = null;
                List<HkParcelDocument> hkParcelDocuments = null;
                List<HkLotDocument> lotDoc = null;
                if (!CollectionUtils.isEmpty(invoiceDbField)) {
                    hkInvoiceDocuments = stockService.retrieveInvoices(invoiceIds, franchise, Boolean.FALSE);
                }
                if (!CollectionUtils.isEmpty(parcelDbField)) {
                    hkParcelDocuments = stockService.retrieveParcels(parcelIds, franchise, Boolean.FALSE);
                }

                lotDoc = stockService.retrieveLots(null, invoiceIds, parcelIds, lotIds, franchise, Boolean.FALSE);

                for (HkPacketDocument packetDocument : packetDocuments) {
//                        if (lotDocument.isHasPacket().equals(lotDataBean.isHasPacketTrue())) {
                    Map<String, Object> map = new HashMap<>();
                    SelectItem selectItem = new SelectItem(packetDocument.getId().toString(), packetDocument.getLot().toString(), packetDocument.getParcel().toString(), packetDocument.getInvoice().toString());
                    BasicBSONObject fieldValue = packetDocument.getFieldValue();
                    if (fieldValue != null) {
                        map.put(HkSystemConstantUtil.AutoNumber.PACKET_ID, fieldValue.toMap().get(HkSystemConstantUtil.AutoNumber.PACKET_ID));
                        if (!CollectionUtils.isEmpty(packetDbField)) {
                            for (String dbField : packetDbField) {
                                if (fieldValue.toMap().containsKey(dbField) && fieldValue.toMap().get(dbField) != null) {
                                    map.put(dbField, fieldValue.toMap().get(dbField));
                                }
                            }
                        }
                        if (!CollectionUtils.isEmpty(invoiceDbField)) {
                            for (HkInvoiceDocument hkInvoiceDocument : hkInvoiceDocuments) {
                                if (hkInvoiceDocument.getId().toString().equals(packetDocument.getInvoice().toString())) {
                                    BasicBSONObject invoiceFieldValue = hkInvoiceDocument.getFieldValue();
                                    if (invoiceFieldValue != null) {
                                        for (String dbField : invoiceDbField) {
                                            if (invoiceFieldValue.toMap().containsKey(dbField)) {
                                                map.put(dbField, invoiceFieldValue.toMap().get(dbField));
                                            }
                                        }
                                    }
                                    break;
                                }
                            }
                        }
                        if (!CollectionUtils.isEmpty(parcelDbField)) {
                            for (HkParcelDocument hkParcelDocument : hkParcelDocuments) {
                                if (hkParcelDocument.getId().toString().equals(packetDocument.getParcel().toString())) {
                                    for (String dbField : parcelDbField) {
                                        if (hkParcelDocument.getFieldValue().toMap().containsKey(dbField)) {
                                            map.put(dbField, hkParcelDocument.getFieldValue().toMap().get(dbField));
                                        }
                                    }
                                    break;
                                }
                            }
                        }
                        for (HkLotDocument lotDocument : lotDoc) {
                            if (lotDocument.getId().toString().equals(packetDocument.getLot().toString())) {
                                map.put(HkSystemConstantUtil.LotStaticFieldName.STOCK_CARAT, lotDocument.getStockCarat());
                                map.put(HkSystemConstantUtil.LotStaticFieldName.STOCK_PIECES, lotDocument.getStockPieces());
                                if (!CollectionUtils.isEmpty(lotDbField)) {
                                    for (String dbField : lotDbField) {
                                        if (lotDocument.getFieldValue().toMap().containsKey(dbField)) {
                                            map.put(dbField, lotDocument.getFieldValue().toMap().get(dbField));
                                        }
                                    }
                                }
                                break;
                            }
                        }
                        selectItem.setCategoryCustom(map);

                        //Set screen rule related details.
                        if (packetFieldRuleDetailsMap != null && !CollectionUtils.isEmpty(packetFieldRuleDetailsMap)) {
                            System.out.println("lotFieldRuleDetailsMap:::" + packetFieldRuleDetailsMap);
                            System.out.println("fieldIdToNameMap:::" + fieldIdToNameMap);
                            if (!CollectionUtils.isEmpty(packetFieldRuleDetailsMap.get(packetDocument.getId().toString()))) {
                                Map<String, RuleDetailsDataBean> screenRuleDetailsWithDbFieldName = new HashMap<>();
                                Map<String, RuleDetailsDataBean> ruleDetails = packetFieldRuleDetailsMap.get(packetDocument.getId().toString());
                                for (Map.Entry<String, RuleDetailsDataBean> entry : ruleDetails.entrySet()) {
                                    if (fieldIdToNameMap.get(entry.getKey()) != null) {
                                        screenRuleDetailsWithDbFieldName.put(fieldIdToNameMap.get(entry.getKey()), entry.getValue());

                                    }
                                }
                                selectItem.setScreenRuleDetailsWithDbFieldName(screenRuleDetailsWithDbFieldName);
                            }

                        }
                        selectItems.add(selectItem);
                    }
//                        }
                }
                return selectItems;
            } else {
                LOGGER.info("packet docs not found");
            }
        }
        return null;
    }

    public Boolean deletePacket(String packetId) {
        Boolean success = false;
        if (StringUtils.hasText(packetId) && StringUtils.hasLength(packetId)) {
            success = stockService.deletePacket(new ObjectId(packetId));
        }
        return success;
    }

    public List<SelectItem> retrieveSplitPacket(Map<String, Object> map) {
        List<SelectItem> selectItems = null;
        List<ObjectId> packetIds = new ArrayList<>();
        LOGGER.info("map : " + map);
        if (!CollectionUtils.isEmpty(map)) {
            List<String> list = (List<String>) map.get("packetObjectId");
            ObjectId packetId = new ObjectId(list.get(0));
            LOGGER.info("packetId : " + packetId);
            List<ObjectId> invoiceIds = new ArrayList<>();
            List<ObjectId> parcelIds = new ArrayList<>();
            packetIds.add(packetId);
            List<String> invoiceDbFieldNames = (List<String>) map.get("invoice");
            List<String> parcelDbFieldNames = (List<String>) map.get("parcel");
            List<String> lotDbFieldNames = (List<String>) map.get("lot");
            List<String> packetDbFieldNames = (List<String>) map.get("packet");
            LOGGER.info("invoiceDbFieldNames : " + invoiceDbFieldNames);
            LOGGER.info("parcelDbFieldNames : " + parcelDbFieldNames);
            LOGGER.info("lotDbFieldNames : " + lotDbFieldNames);
            LOGGER.info("packetDbFieldNames : " + packetDbFieldNames);
            Map<String, Object> map1 = (Map<String, Object>) map.get("ruleConfigMap");
            Map<String, String> fieldIdToNameMap = new HashMap<>();
            Set<Long> fieldIds = new HashSet<>();
            if (!CollectionUtils.isEmpty(map1) && map1.get("fieldIdNameMap") != null) {
                fieldIdToNameMap = (Map<String, String>) map1.get("fieldIdNameMap");
                for (String lng : fieldIdToNameMap.keySet()) {
                    fieldIds.add(Long.parseLong(lng));
                }
            }
            List<HkPacketDocument> packetDocuments = null;
            if (packetId != null) {
                packetDocuments = stockService.retrievePackets(null, null, null, null, loginDataBean.getCompanyId(), Boolean.FALSE, null, null, packetId, null);
            }
            System.out.println("fieldIds-----" + fieldIds);
            LOGGER.info("packetDocuments : " + packetDocuments);
            HkLotDocument lotDocument = null;
            if (!CollectionUtils.isEmpty(packetDocuments)) {
                lotDocument = stockService.retrieveLotById(packetDocuments.get(0).getLot());
            }

            selectItems = new ArrayList<>();
            if (lotDocument != null) {
                SelectItem parcelItem = new SelectItem(lotDocument.getId().toString(), lotDocument.getParcel().toString(), lotDocument.getInvoice().toString());
                BasicBSONObject fieldValue = lotDocument.getFieldValue();
                if (fieldValue != null && !CollectionUtils.isEmpty(lotDbFieldNames)) {
                    Map<Object, Object> mapToSentOnUI = new HashMap<>();
                    for (String dbField : lotDbFieldNames) {
                        if (fieldValue.toMap().containsKey(dbField)) {
                            mapToSentOnUI.put(dbField, fieldValue.toMap().get(dbField));
                        }
                    }
                    parcelItem.setCustom4(mapToSentOnUI);
                }
                HkInvoiceDocument invoiceDocument = stockService.retrieveInvoiceById(lotDocument.getInvoice());

                if (invoiceDocument != null) {
                    invoiceIds.add(invoiceDocument.getId());
                    BasicBSONObject invoiceFieldValue = invoiceDocument.getFieldValue();
                    if (invoiceFieldValue != null && !CollectionUtils.isEmpty(invoiceDbFieldNames)) {
                        Map<Object, Object> mapToSentOnUI = new HashMap<>();
                        for (String dbField : invoiceDbFieldNames) {
                            if (invoiceFieldValue.toMap().containsKey(dbField)) {
                                mapToSentOnUI.put(dbField, invoiceFieldValue.toMap().get(dbField));
                            }
                        }
                        parcelItem.setCustom1(mapToSentOnUI);
                    }
                }
                selectItems.add(parcelItem);

                HkParcelDocument parcelDocument = stockService.retrieveParcelById(lotDocument.getParcel());

                if (parcelDocument != null) {
                    parcelIds.add(parcelDocument.getId());
                    BasicBSONObject parcelFieldValue = parcelDocument.getFieldValue();
                    if (parcelFieldValue != null && !CollectionUtils.isEmpty(parcelDbFieldNames)) {
                        Map<Object, Object> mapToSentOnUI = new HashMap<>();
                        for (String dbField : parcelDbFieldNames) {
                            if (parcelFieldValue.toMap().containsKey(dbField)) {
                                mapToSentOnUI.put(dbField, parcelFieldValue.toMap().get(dbField));
                            }
                        }
                        parcelItem.setCustom3(mapToSentOnUI);
                    }
                }
                selectItems.add(parcelItem);
            }

            Map<String, Map<String, RuleDetailsDataBean>> packetFieldRuleDetailsMap = null;
            if (!CollectionUtils.isEmpty(map1) && map1.get("featureName") != null) {
                packetFieldRuleDetailsMap = centerRuleExecutionTransformer.prepareDataAndExecuteScreenRules(packetDocuments, new ArrayList<>(fieldIds), map1.get("featureName").toString(), HkSystemConstantUtil.Feature.PACKET);
            }
            if (!CollectionUtils.isEmpty(packetDocuments)) {
                LOGGER.info("packetDocuments size : " + packetDocuments.size());
                Map<String, String> mapOfDbFieldWithCurrencyCode = null;
                if (!CollectionUtils.isEmpty(packetDbFieldNames)) {
                    for (String dbField : packetDbFieldNames) {
                        String[] split = dbField.split("\\" + HkSystemConstantUtil.SEPERATOR_FOR_CUSTOM_FIELD_LABEL);
                        String componentType = split[1];
                        if (componentType.equals(HkSystemConstantUtil.CustomField.COMPONENT_CODE_MAP.get(HkSystemConstantUtil.CustomField.ComponentType.CURRENCY))) {
                            mapOfDbFieldWithCurrencyCode = customFieldService.mapOfDbFieldWithCurrencyCode(loginDataBean.getCompanyId());
                            break;
                        }
                    }
                }
                for (HkPacketDocument packetDocument : packetDocuments) {
                    Map<String, Object> mapToSentOnUI = new HashMap<>();
                    SelectItem selectItem = new SelectItem(packetDocument.getId().toString(), packetDocument.getLot().toString(), packetDocument.getParcel().toString(), packetDocument.getInvoice().toString());
                    selectItem.setCustom7(packetDocument.getFranchiseId());
                    selectItem.setOtherId(packetDocument.getSequenceNumber());
                    BasicBSONObject fieldValue = packetDocument.getFieldValue();
                    if (fieldValue != null) {
                        Map fieldValueMapFromDb = fieldValue.toMap();
                        if (!CollectionUtils.isEmpty(fieldValueMapFromDb) && !CollectionUtils.isEmpty(packetDbFieldNames)) {
                            if (packetDbFieldNames.indexOf(HkSystemConstantUtil.AutoNumber.PACKET_ID) == -1) {
                                mapToSentOnUI.put(HkSystemConstantUtil.AutoNumber.PACKET_ID, fieldValueMapFromDb.get(HkSystemConstantUtil.AutoNumber.PACKET_ID));
                            }
                            LOGGER.info("---fieldValueMapFromDb--outside for loop--" + fieldValueMapFromDb);
                            for (String packetField : packetDbFieldNames) {
                                String[] split = packetField.split("\\" + HkSystemConstantUtil.SEPERATOR_FOR_CUSTOM_FIELD_LABEL);
                                String componentType = split[1];
                                if (componentType.equals(HkSystemConstantUtil.CustomField.COMPONENT_CODE_MAP.get(HkSystemConstantUtil.CustomField.ComponentType.CURRENCY))) {
                                    if (fieldValue.toMap().containsKey(packetField) && fieldValue.toMap().get(packetField) != null) {
                                        mapToSentOnUI.put(packetField, fieldValue.toMap().get(packetField));
                                    }
                                    if (fieldValue.toMap().containsKey(packetField + HkSystemConstantUtil.CURRENCY_CODE_CUSTOM)) {
                                        mapToSentOnUI.put(packetField + HkSystemConstantUtil.CURRENCY_CODE_CUSTOM, fieldValue.toMap().get(packetField + HkSystemConstantUtil.CURRENCY_CODE_CUSTOM));
                                    } else {
                                        if (mapOfDbFieldWithCurrencyCode != null && !mapOfDbFieldWithCurrencyCode.isEmpty()) {
                                            if (mapOfDbFieldWithCurrencyCode.containsKey(packetField)) {
                                                String currencyCodeVal = mapOfDbFieldWithCurrencyCode.get(packetField);
                                                if (StringUtils.hasLength(currencyCodeVal) && StringUtils.hasText(currencyCodeVal)) {
                                                    String currencyCode = currencyCodeVal.toString().replaceAll("^\"|\"$", "");
                                                    mapToSentOnUI.put(packetField + HkSystemConstantUtil.CURRENCY_CODE_CUSTOM, currencyCode);
                                                }

                                            }
                                        }
                                    }
                                } else {
                                    LOGGER.info("here----" + packetField + "---fieldValueMapFromDb--" + fieldValueMapFromDb.get(packetField));
                                    mapToSentOnUI.put(packetField, fieldValueMapFromDb.get(packetField));
                                }
                            }
                        }
                    }
                    if (!CollectionUtils.isEmpty(mapToSentOnUI)) {
                        selectItem.setCategoryCustom(mapToSentOnUI);
                    }
                    //Set screen rule related details.
                    if (packetFieldRuleDetailsMap != null && !CollectionUtils.isEmpty(packetFieldRuleDetailsMap)) {
                        System.out.println("packetFieldRuleDetailsMap:::" + packetFieldRuleDetailsMap);
                        System.out.println("fieldIdToNameMap:::" + fieldIdToNameMap);
                        if (!CollectionUtils.isEmpty(packetFieldRuleDetailsMap.get(packetDocument.getId().toString()))) {
                            Map<String, RuleDetailsDataBean> screenRuleDetailsWithDbFieldName = new HashMap<>();
                            Map<String, RuleDetailsDataBean> ruleDetails = packetFieldRuleDetailsMap.get(packetDocument.getId().toString());
                            for (Map.Entry<String, RuleDetailsDataBean> entry : ruleDetails.entrySet()) {
                                if (fieldIdToNameMap.get(entry.getKey()) != null) {
                                    screenRuleDetailsWithDbFieldName.put(fieldIdToNameMap.get(entry.getKey()), entry.getValue());
                                }
                            }
                            selectItem.setScreenRuleDetailsWithDbFieldName(screenRuleDetailsWithDbFieldName);
                        }
                    }
                    selectItems.add(selectItem);
                }
            }
        }
        return selectItems;
    }
}
