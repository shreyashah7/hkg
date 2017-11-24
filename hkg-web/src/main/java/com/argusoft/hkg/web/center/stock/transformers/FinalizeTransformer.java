/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.center.stock.transformers;

import com.argusoft.hkg.common.constantutil.HkSystemConstantUtil;
import com.argusoft.hkg.nosql.core.HkCustomFieldService;
import com.argusoft.hkg.nosql.core.HkFoundationDocumentService;
import com.argusoft.hkg.nosql.core.HkStockService;
import com.argusoft.hkg.nosql.core.HkUserService;
import com.argusoft.hkg.nosql.model.HkInvoiceDocument;
import com.argusoft.hkg.nosql.model.HkLotDocument;
import com.argusoft.hkg.nosql.model.HkPacketDocument;
import com.argusoft.hkg.nosql.model.HkParcelDocument;
import com.argusoft.hkg.nosql.model.HkPlanDocument;
import com.argusoft.hkg.web.center.stock.databeans.DynamicServiceInitDataBean;
import com.argusoft.hkg.web.center.stock.databeans.FinalizeServiceDataBean;
import com.argusoft.hkg.web.center.transformers.CenterCustomFieldTransformer;
import com.argusoft.hkg.web.center.usermanagement.databeans.LoginDataBean;
import com.argusoft.hkg.web.center.usermanagement.databeans.UMFeatureDetailDataBean;
import com.argusoft.hkg.web.center.util.ApplicationUtil;
import com.argusoft.hkg.web.master.databeans.MasterDataBean;
import com.argusoft.hkg.web.util.SelectItem;
import com.argusoft.sync.center.model.HkFieldDocument;
import com.argusoft.sync.center.model.HkMasterValueDocument;
import com.argusoft.sync.center.model.HkPriceListDetailDocument;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
 * @author siddharth
 */
@Service
public class FinalizeTransformer {

    @Autowired
    LoginDataBean loginDataBean;

    @Autowired
    ApplicationUtil applicationUtil;

    @Autowired
    StockTransformer stockTransformer;

    @Autowired
    HkFoundationDocumentService foundationService;

    @Autowired
    HkUserService userService;

    @Autowired
    CenterCustomFieldTransformer centerCustomFieldTransformer;

    @Autowired
    HkStockService stockService;

    @Autowired
    HkCustomFieldService fieldService;

    private static final Logger LOGGER = LoggerFactory.getLogger(FinalizeTransformer.class);

    public SelectItem retrieveStock(Map<String, List<String>> featureDbfieldNameMap) {
//        TODO check for packet id also
        Boolean retrievePacket = false;
        List<String> ids = featureDbfieldNameMap.get("lotId");
        if (CollectionUtils.isEmpty(ids)) {
            ids = featureDbfieldNameMap.get("packetId");
            retrievePacket = true;
        }
        Boolean invoiceFieldExist = false;
        Boolean parcelFieldExist = false;
        Boolean lotFieldExist = false;
        Boolean packetFieldExist = false;
        List<String> invoiceDbFieldNames = null;
        List<String> parcelDbFieldNames = null;
        List<String> lotDbFieldNames = null;
        List<String> packetDbFieldNames = null;
        if (!CollectionUtils.isEmpty(featureDbfieldNameMap)) {
            for (Map.Entry<String, List<String>> entry : featureDbfieldNameMap.entrySet()) {
                String featureName = entry.getKey();
                if (featureName.equalsIgnoreCase(HkSystemConstantUtil.Feature.INVOICE)) {
                    invoiceFieldExist = true;
                    invoiceDbFieldNames = entry.getValue();
                } else if (featureName.equalsIgnoreCase(HkSystemConstantUtil.Feature.PARCEL)) {
                    parcelFieldExist = true;
                    parcelDbFieldNames = entry.getValue();
                } else if (featureName.equalsIgnoreCase(HkSystemConstantUtil.Feature.LOT)) {
                    lotFieldExist = true;
                    lotDbFieldNames = entry.getValue();
                } else if (featureName.equalsIgnoreCase(HkSystemConstantUtil.Feature.PACKET)) {
                    packetFieldExist = true;
                    packetDbFieldNames = entry.getValue();
                }
            }
        }
        Long companyId = loginDataBean.getCompanyId();
        SelectItem selectItem = null;

        if (!retrievePacket && !CollectionUtils.isEmpty(ids)) {
            HkLotDocument lotDocument = stockService.retrieveLotById(new ObjectId(ids.get(0)));
            if (lotDocument != null && !CollectionUtils.isEmpty(featureDbfieldNameMap)) {
                ObjectId lotId = lotDocument.getId();
                ObjectId invoiceId = lotDocument.getInvoice();
                ObjectId parcelId = lotDocument.getParcel();
                selectItem = new SelectItem(lotId.toString(), parcelId.toString(), invoiceId.toString());
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
                if (lotFieldExist) {
                    Map<String, Object> lotFieldValueMap = lotDocument.getFieldValue().toMap();
                    if (!CollectionUtils.isEmpty(lotDbFieldNames)) {
                        Map<Object, Object> lotMap = new HashMap<>();
                        for (String lotField : lotDbFieldNames) {
                            Object value = lotFieldValueMap.get(lotField);
                            lotMap.put(lotField, value);
                        }
                        selectItem.setCustom4(lotMap);
                    }
                }

            } else {
                LOGGER.info("lot doc is null");
            }
        } else if (retrievePacket && !CollectionUtils.isEmpty(ids)) {
            HkPacketDocument packetDocument = stockService.retrievePacketById(new ObjectId(ids.get(0)),true);
            if (packetDocument != null && !CollectionUtils.isEmpty(featureDbfieldNameMap)) {
                ObjectId packetId = packetDocument.getId();
                ObjectId invoiceId = packetDocument.getInvoice();
                ObjectId parcelId = packetDocument.getParcel();
                ObjectId loti = packetDocument.getLot();
                selectItem = new SelectItem(packetId.toString(), parcelId.toString(), invoiceId.toString());
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
                if (lotFieldExist) {
                    HkLotDocument lotDocument = stockService.retrieveLotById(loti);
                    Map<String, Object> parcelFieldValueMap = new HashMap();
                    if (lotDocument != null) {
                        parcelFieldValueMap = lotDocument.getFieldValue().toMap();
                    }
                    if (!CollectionUtils.isEmpty(lotDbFieldNames) && lotDocument != null) {
                        Map<Object, Object> lotMap = new HashMap<>();
                        for (String parcelField : lotDbFieldNames) {
                            Object value = parcelFieldValueMap.get(parcelField);
                            lotMap.put(parcelField, value);
                        }

                        selectItem.setCustom4(lotMap);
                    }
                }
                if (packetFieldExist) {
                    Map<String, Object> lotFieldValueMap = packetDocument.getFieldValue().toMap();
                    if (!CollectionUtils.isEmpty(packetDbFieldNames)) {
                        Map<Object, Object> packetMap = new HashMap<>();
                        for (String lotField : packetDbFieldNames) {
                            Object value = lotFieldValueMap.get(lotField);
                            packetMap.put(lotField, value);
                        }
                        selectItem.setCustom5(packetMap);
                    }
                }

            }
        }
        return selectItem;
    }

    public List<MasterDataBean> retrieveValueOfMaster() {
        Long companyId = loginDataBean.getCompanyId();
        Map<String, HkFieldDocument> retrieveMapOfDBFieldNameWithEntity = fieldService.retrieveMapOfDBFieldNameWithEntity(Arrays.asList(HkSystemConstantUtil.PlanStaticFieldName.COLOR, HkSystemConstantUtil.PlanStaticFieldName.CUT, HkSystemConstantUtil.PlanStaticFieldName.CLARITY, HkSystemConstantUtil.PlanStaticFieldName.FLUROSCENE), companyId);
        List<String> masterKeyCode = null;
        Map<String, String> codeMasterName = new HashMap<>();
        if (!CollectionUtils.isEmpty(retrieveMapOfDBFieldNameWithEntity)) {
            LOGGER.info("retrieveMapOfDBFieldNameWithEntity is not empty");
            masterKeyCode = new ArrayList<>();
            for (Map.Entry<String, HkFieldDocument> entry : retrieveMapOfDBFieldNameWithEntity.entrySet()) {
                HkFieldDocument hkFieldEntity = entry.getValue();
                masterKeyCode.add(hkFieldEntity.getId().toString());
                codeMasterName.put(hkFieldEntity.getId().toString(), hkFieldEntity.getFieldLabel());
            }
        } else {
            LOGGER.info("retrieveMapOfDBFieldNameWithEntity is empty");
        }
        LOGGER.info("masterKeyCode::" + masterKeyCode);
        List<HkMasterValueDocument> valueEntitys = foundationService.retrieveMasterValueByCode(masterKeyCode);
        List<MasterDataBean> masterDataBeans = null;
        if (!CollectionUtils.isEmpty(valueEntitys)) {
            LOGGER.info("valueEntitys found");
            masterDataBeans = new ArrayList<>();
            for (HkMasterValueDocument valueEntity : valueEntitys) {
                MasterDataBean dataBean = this.convertModelToMasterDataBean(valueEntity, new MasterDataBean(), codeMasterName);
                masterDataBeans.add(dataBean);
            }
        } else {
            LOGGER.info("valueEntitys notttttt found");
        }
        return masterDataBeans;
    }

    private MasterDataBean convertModelToMasterDataBean(HkMasterValueDocument valueEntity, MasterDataBean masterDataBean, Map<String, String> codeMasterName) {
        if (masterDataBean == null) {
            masterDataBean = new MasterDataBean();
        }
        masterDataBean.setCode(valueEntity.getCode());
        String translatedValueName = valueEntity.getTranslatedValueName();
        if (StringUtils.hasText(translatedValueName)) {
            masterDataBean.setValue(translatedValueName);
        } else {
            masterDataBean.setValue(valueEntity.getValueName());
        }
        masterDataBean.setValueEntityId(valueEntity.getId());
        masterDataBean.setMasterName(codeMasterName.get(valueEntity.getCode()));
        return masterDataBean;
    }

    public Double checkCaratRange(Map<String, Float> map) {
        if (!CollectionUtils.isEmpty(map)) {
            LOGGER.info("map :" + map);
            Long caratValue = stockService.retrieveCaratRangeFromCaratValue(map.get(HkSystemConstantUtil.PlanStaticFieldName.CARAT).doubleValue());
            if (caratValue != null) {
                Double retrievedPrice = stockService.calculatePriceFromPriceList(map.get("color").longValue(), map.get("cut").longValue(), map.get("clarity").longValue(), map.get("fluroscene").longValue(), caratValue);
                if (retrievedPrice != null) {
                    return retrievedPrice * caratValue;
                }
                return retrievedPrice;
            }
        }
        return null;
    }

    public String savePlan(FinalizeServiceDataBean finalizeServiceDataBean) {
//        if (finalizeServiceDataBean != null) {
//            String planType = finalizeServiceDataBean.getPlanType();
//            ObjectId lotId = null;
//            ObjectId packetId = null;
//            if (finalizeServiceDataBean.isHasPacket()) {
//                packetId = new ObjectId(finalizeServiceDataBean.getId());
//            } else {
//                lotId = new ObjectId(finalizeServiceDataBean.getId());
//            }
//            List<FinalizeServiceDataBean> finalizeServiceDataBeans = finalizeServiceDataBean.getFinalizeServiceDataBeans();
//            if (!CollectionUtils.isEmpty(finalizeServiceDataBeans)) {
//                Map<String, Double> priceMap = new HashMap<>();
//                Map<String, String> currencyCodeMap = new HashMap<>();
//                Map<String, String> finalizeDbType = finalizeServiceDataBean.getFinalizeDbType();
//                Map<String, String> uIFieldNameWithComponentTypes = null;
//
//                String pointerComponentType = null;
//                if (!CollectionUtils.isEmpty(finalizeDbType)) {
//                    List<String> dbNameList = new ArrayList<>();
//                    for (Map.Entry<String, String> entry : finalizeDbType.entrySet()) {
//                        String key = entry.getKey();
//                        dbNameList.add(key);
//                    }
//
//                    uIFieldNameWithComponentTypes = fieldService.retrieveUIFieldNameWithComponentTypes(dbNameList);
//                    pointerComponentType = HkSystemConstantUtil.CustomFieldComponentType.POINTER + "@*" + HkSystemConstantUtil.CustomFieldComponentType.MULTISELECT_DROPDOWN;
//                }
//                Map<String, BasicBSONObject> map = new HashMap<>();
//                for (FinalizeServiceDataBean serviceDataBean : finalizeServiceDataBeans) {
//                    if (serviceDataBean.getPrice() != null) {
//                        priceMap.put(serviceDataBean.getTag(), Double.parseDouble(serviceDataBean.getPrice().toString()));
//                    }
//                    if (serviceDataBean.getCurrencyCode() != null) {
//                        currencyCodeMap.put(serviceDataBean.getTag(), serviceDataBean.getCurrencyCode());
//                    }
//                    BasicBSONObject basicBSONObject = new BasicBSONObject();
//                    if (!CollectionUtils.isEmpty(serviceDataBean.getFinalizeCustom())) {
//                        for (Map.Entry<String, Object> entry : serviceDataBean.getFinalizeCustom().entrySet()) {
//                            String key = entry.getKey();
//                            Object value = entry.getValue();
//                            if (key != null && uIFieldNameWithComponentTypes.containsKey(key)) {
//                                String type = uIFieldNameWithComponentTypes.get(key);
//                                if (type.equals(HkSystemConstantUtil.CustomFieldComponentType.MULTISELECT_DROPDOWN) || type.equals(HkSystemConstantUtil.CustomFieldComponentType.USER_MULTISELECT) || type.equals(pointerComponentType)) {
//                                    if (serviceDataBean.getFinalizeCustom().containsKey(key)) {
//                                        serviceDataBean.getFinalizeCustom().put(key, HkSystemConstantUtil.DbFieldType.ARRAY);
//                                    }
//
//                                    if (serviceDataBean.getFinalizeCustom().containsKey(key)) {
//                                        String customVal = serviceDataBean.getFinalizeCustom().get(key).toString();
//                                        List<String> values = new ArrayList<>();
//                                        String[] valueArray = customVal.replace("\"", "").split(",");
//                                        for (String v : valueArray) {
//                                            values.add(v.replace("\"", ""));
//                                        }
//                                        serviceDataBean.getFinalizeCustom().put(key, values);
//                                    }
//                                }
//                            }
//                        }
//                        basicBSONObject = fieldService.makeBSONObject(serviceDataBean.getFinalizeCustom(), finalizeServiceDataBean.getFinalizeDbType(), HkSystemConstantUtil.Feature.FINALIZE, loginDataBean.getCompanyId(), null);
//
//                    }
//                    if (serviceDataBean.getColorId() != null) {
//                        basicBSONObject.put(HkSystemConstantUtil.PlanStaticFieldName.COLOR, serviceDataBean.getColorId());
//                    }
//                    if (serviceDataBean.getClarityId() != null) {
//                        basicBSONObject.put(HkSystemConstantUtil.PlanStaticFieldName.CLARITY, serviceDataBean.getClarityId());
//                    }
//                    if (serviceDataBean.getCutId() != null) {
//                        basicBSONObject.put(HkSystemConstantUtil.PlanStaticFieldName.CUT, serviceDataBean.getCutId());
//                    }
//                    if (serviceDataBean.getFlurosceneId() != null) {
//                        basicBSONObject.put(HkSystemConstantUtil.PlanStaticFieldName.FLUROSCENE, serviceDataBean.getFlurosceneId());
//                    }
//                    //System.out.println("serviceDataBean.getCaratId() :" + serviceDataBean.getCaratId());
//                    if (serviceDataBean.getCaratId() != null) {
//                        basicBSONObject.put(HkSystemConstantUtil.PlanStaticFieldName.CARATE_RANGE, serviceDataBean.getCaratId());
//                    }
//                    if (serviceDataBean.getPrice() != null) {
//                        basicBSONObject.put(HkSystemConstantUtil.PlanStaticFieldName.PRICE, serviceDataBean.getPrice());
//                    }
//                    if (serviceDataBean.getCaratValue() != null) {
//                        basicBSONObject.put(HkSystemConstantUtil.PlanStaticFieldName.CARAT, serviceDataBean.getCaratValue());
//                    }
//                    if (serviceDataBean.getCurrencyCode() != null) {
//                        basicBSONObject.put(HkSystemConstantUtil.PlanStaticFieldName.CURRENCY_CODE, serviceDataBean.getCurrencyCode());
//                    }
//                    basicBSONObject.put(HkSystemConstantUtil.PlanStaticFieldName.BREAKAGE, serviceDataBean.isBreakage());
//                    map.put(serviceDataBean.getTag(), basicBSONObject);
//
//                }
//                LOGGER.info("planType :" + planType);
//                LOGGER.info("map to be sent :" + map);
//                LOGGER.info("lot id :" + lotId);
//                LOGGER.info("packet id :" + packetId);
//                LOGGER.info("price map :" + priceMap);
//                LOGGER.info("currencyCodeMap :" + currencyCodeMap);
//                UMFeatureDetailDataBean feature = applicationUtil.getuMFeatureDetailDataBeanMap().get(HkSystemConstantUtil.Feature.PLAN);
//                Map<String, String> dbFieldWithFormulaMap = fieldService.retrieveFeatureNameWithDbFieldListForFormula(feature.getId(), loginDataBean.getCompanyId(), HkSystemConstantUtil.FeatureForFormulaEvaluation.ADD_PLAN, HkSystemConstantUtil.CustomFieldComponentType.FORMULA);
//
//                return stockService.savePlans(planType, map, loginDataBean.getCompanyId(), loginDataBean.getId(), lotId, packetId, priceMap, currencyCodeMap, dbFieldWithFormulaMap, finalizeServiceDataBean.getImproveFromId());
//            }
//        }
        return null;
    }

    public List<SelectItem> retrieveCurrencyCode() {
        return centerCustomFieldTransformer.retrieveCurrencyForComboForDynamicForm();
    }

    public List<FinalizeServiceDataBean> retrievePlan(String workAllotment, String status) {
//        HkWorkAllotmentDocument allotmentDocument = workAllotmentService.retrieveWorkAllotmentById(new ObjectId(workAllotment));
//
//        List<HkPlanDocument> planDocuments = stockService.retrieveEnteredPlansByLoggedInUsers(loginDataBean.getId(), loginDataBean.getCompanyId());
        List<FinalizeServiceDataBean> serviceDataBeans = null;
//        if (!CollectionUtils.isEmpty(planDocuments)) {
//            serviceDataBeans = new ArrayList<>();
//            for (HkPlanDocument planDocument : planDocuments) {
//                if (allotmentDocument.getUnitType().equals(HkSystemConstantUtil.WORKALLOTEMENT_UNIT_TYPE.LOT) && planDocument.getLot().equals(allotmentDocument.getUnitInstance())) {
//                    if (status != null && planDocument.getStatus().equalsIgnoreCase(status)) {
//                        FinalizeServiceDataBean finalizeServiceDataBean = this.convertPlanDocToDataBean(planDocument, new FinalizeServiceDataBean());
//                        serviceDataBeans.add(finalizeServiceDataBean);
//                    } else if (status == null) {
//                        FinalizeServiceDataBean finalizeServiceDataBean = this.convertPlanDocToDataBean(planDocument, new FinalizeServiceDataBean());
//                        serviceDataBeans.add(finalizeServiceDataBean);
//                    }
//                } else if (allotmentDocument.getUnitType().equals(HkSystemConstantUtil.WORKALLOTEMENT_UNIT_TYPE.PACKET) && (planDocument.getPacket() != null && planDocument.getPacket().equals(allotmentDocument.getUnitInstance()))) {
//                    if (status != null && planDocument.getStatus().equalsIgnoreCase(status)) {
//                        FinalizeServiceDataBean finalizeServiceDataBean = this.convertPlanDocToDataBean(planDocument, new FinalizeServiceDataBean());
//                        serviceDataBeans.add(finalizeServiceDataBean);
//                    } else if (status == null) {
//                        FinalizeServiceDataBean finalizeServiceDataBean = this.convertPlanDocToDataBean(planDocument, new FinalizeServiceDataBean());
//                        serviceDataBeans.add(finalizeServiceDataBean);
//                    }
//                }
//            }
//        } else {
//            LOGGER.info("plan doc not found");
//        }
        return serviceDataBeans;
    }

    private FinalizeServiceDataBean convertPlanDocToDataBean(HkPlanDocument planDocument, FinalizeServiceDataBean serviceDataBean) {
//        if (serviceDataBean == null) {
//            serviceDataBean = new FinalizeServiceDataBean();
//        }
//        serviceDataBean.setPlanObjectId(planDocument.getId().toString());
//        serviceDataBean.setTag(planDocument.getTag());
//        serviceDataBean.setPlanType(planDocument.getPlanType());
//        serviceDataBean.setCreatedBy(planDocument.getCreatedBy());
//        if (planDocument.getReferencePlan() != null) {
//            serviceDataBean.setReferencePlan(planDocument.getReferencePlan().toString());
//        }
////        List<HkPriceHistoryDocument> priceHistoryDocuments = planDocument.getHkPriceHistoryList();
//        BasicBSONObject fieldValue = planDocument.getFieldValue();
//        if (fieldValue != null) {
//            Map<String, Object> map = fieldValue.toMap();
//            map.put("tag", planDocument.getTag());
//            serviceDataBean.setFinalizeCustom(map);
//            if (!CollectionUtils.isEmpty(map)) {
//                for (Map.Entry<String, Object> entry : map.entrySet()) {
//                    String key = entry.getKey();
//                    Object value = entry.getValue();
//                    if (key.equalsIgnoreCase(HkSystemConstantUtil.AutoNumber.PLAN_ID)) {
//                        serviceDataBean.setSequentialPlanId(value.toString());
//                    } else if (key.equalsIgnoreCase(HkSystemConstantUtil.PlanStaticFieldName.COLOR)) {
//                        serviceDataBean.setColorId((Long) value);
//                    } else if (key.equalsIgnoreCase(HkSystemConstantUtil.PlanStaticFieldName.CUT)) {
//                        serviceDataBean.setCutId((Long) value);
//                    } else if (key.equalsIgnoreCase(HkSystemConstantUtil.PlanStaticFieldName.CARATE_RANGE)) {
//                        serviceDataBean.setCaratId((Long) value);
//                    } else if (key.equalsIgnoreCase(HkSystemConstantUtil.PlanStaticFieldName.CLARITY)) {
//                        serviceDataBean.setClarityId((Long) value);
//                    } else if (key.equalsIgnoreCase(HkSystemConstantUtil.PlanStaticFieldName.PRICE)) {
//                        serviceDataBean.setPrice(value);
//                    } else if (key.equalsIgnoreCase(HkSystemConstantUtil.PlanStaticFieldName.CARAT)) {
//                        serviceDataBean.setCaratValue(value);
//                    } else if (key.equalsIgnoreCase(HkSystemConstantUtil.PlanStaticFieldName.FLUROSCENE)) {
//                        serviceDataBean.setFlurosceneId((Long) value);
//                    } else if (key.equalsIgnoreCase(HkSystemConstantUtil.PlanStaticFieldName.CURRENCY_CODE)) {
//                        serviceDataBean.setCurrencyCode((String) value);
//                    } else if (key.equalsIgnoreCase(HkSystemConstantUtil.PlanStaticFieldName.BREAKAGE)) {
//                        serviceDataBean.setBreakage((Boolean) value);
//                    }
//                }
//            }
//        }
        return serviceDataBean;
    }

    public Boolean submitPlans(Map<String, String> map) {
//        if (!CollectionUtils.isEmpty(map) && map.get("plan") != null && map.get("workallocation") != null) {
//
//            ObjectId planId = new ObjectId(map.get("plan"));
//            ObjectId workallocationId = new ObjectId(map.get("workallocation"));
//
//            LOGGER.info("planId :" + planId);
//            LOGGER.info("workallocationId : " + workallocationId);
//
//            return stockService.finalizePlan(planId, loginDataBean.getId(), loginDataBean.getCompanyId(), workallocationId);
//        }
        return false;
    }

    public List<FinalizeServiceDataBean> retrieveAccessiblePlan(Map<String, String> map) {
        List<FinalizeServiceDataBean> finalizeServiceDataBeans = null;
//        if (!CollectionUtils.isEmpty(map)) {
//            String planType = map.get("plan");
//            String nodeId = map.get("nodeId");
//            String objectId = map.get("objectId");
//            String packet = map.get("isPacket");
//            Boolean isPacket = false;
//            if (packet.equalsIgnoreCase("true")) {
//                isPacket = true;
//            }
//            LOGGER.info("planType " + planType);
//            LOGGER.info("node id " + nodeId);
//            LOGGER.info("object id " + objectId);
//            LOGGER.info("is packet " + isPacket);
//            try {
//                List<HkPlanDocument> planDocuments = stockService.retrieveExistingPlanEntities(planType, Long.parseLong(nodeId), loginDataBean.getCompanyId(), new ObjectId(objectId), isPacket, loginDataBean.getId());
//
//                //System.out.println("planDocuments:::" + planDocuments);
//                if (!CollectionUtils.isEmpty(planDocuments)) {
//                    finalizeServiceDataBeans = new ArrayList<>();
//                    List<Long> createdByList = new ArrayList<Long>();
//                    for (HkPlanDocument planDocument : planDocuments) {
//                        createdByList.add(planDocument.getCreatedBy());
//                        FinalizeServiceDataBean finalizeServiceDataBean = this.convertPlanDocToDataBean(planDocument, new FinalizeServiceDataBean());
//                        finalizeServiceDataBean.setId(planDocument.getId().toString());
//                        finalizeServiceDataBeans.add(finalizeServiceDataBean);
//                    }
//                    if (!CollectionUtils.isEmpty(createdByList)) {
//                        List<SyncCenterUserDocument> centerUserDocuments = userService.retrieveUsersByIds(createdByList);
//                        if (!CollectionUtils.isEmpty(centerUserDocuments)) {
//                            for (FinalizeServiceDataBean serviceDataBean : finalizeServiceDataBeans) {
//                                for (SyncCenterUserDocument syncCenterUserDocument : centerUserDocuments) {
//                                    if (serviceDataBean.getCreatedBy() == syncCenterUserDocument.getId()) {
//                                        Map<String, Object> nameMap = new HashMap<>();
//                                        if (loginDataBean.getId() != serviceDataBean.getCreatedBy()) {
//                                            serviceDataBean.setNameOfCreatedBy(syncCenterUserDocument.getFirstName().concat(" ").concat(syncCenterUserDocument.getLastName()));
//                                        } else {
//                                            serviceDataBean.setNameOfCreatedBy("You");
//                                        }
//                                        nameMap.put("createdByName", serviceDataBean.getNameOfCreatedBy());
//                                        nameMap.putAll(serviceDataBean.getFinalizeCustom());
//                                        serviceDataBean.setFinalizeCustom(nameMap);
//                                        break;
//                                    }
//                                }
//                            }
//                        }
//                    }
//                    return finalizeServiceDataBeans;
//                } else {
//                    LOGGER.info("plan doc not found");
//                }
//            } catch (NumberFormatException e) {
//                LOGGER.info("Number format exception found.");
//            }
//
//        }
        return finalizeServiceDataBeans;

    }

    public Boolean deletePlan(String planId) {
//        return stockService.deleteMainTagPlan(new ObjectId(planId), loginDataBean.getId());
        return null;
    }

    public Boolean editEnteredPlan(FinalizeServiceDataBean finalizeServiceDataBean) {
//        if (finalizeServiceDataBean != null) {
//            String planType = finalizeServiceDataBean.getPlanType();
////            ObjectId lotId = null;
////            ObjectId packetId = null;
////            if (finalizeServiceDataBean.isHasPacket()) {
////                packetId = new ObjectId(finalizeServiceDataBean.getId());
////            } else {
////                lotId = new ObjectId(finalizeServiceDataBean.getId());
////            }
//            List<FinalizeServiceDataBean> writeServiceDataBeans = finalizeServiceDataBean.getFinalizeServiceDataBeans();
//            if (!CollectionUtils.isEmpty(writeServiceDataBeans)) {
//                Map<String, Double> priceMap = new HashMap<>();
//                Map<String, String> currencyCodeMap = new HashMap<>();
//                Map<String, String> writeDbType = finalizeServiceDataBean.getFinalizeDbType();
//                Map<String, String> uIFieldNameWithComponentTypes = null;
//                List<BasicBSONObject> basicBSONObjects = new ArrayList<>();
//                String pointerComponentType = null;
//                if (!CollectionUtils.isEmpty(writeDbType)) {
//                    List<String> dbNameList = new ArrayList<>();
//                    for (Map.Entry<String, String> entry : writeDbType.entrySet()) {
//                        String key = entry.getKey();
//                        dbNameList.add(key);
//                    }
//
//                    uIFieldNameWithComponentTypes = fieldService.retrieveUIFieldNameWithComponentTypes(dbNameList);
//                    pointerComponentType = HkSystemConstantUtil.CustomFieldComponentType.POINTER + "@*" + HkSystemConstantUtil.CustomFieldComponentType.MULTISELECT_DROPDOWN;
//                }
//                Map<String, BasicBSONObject> map = new HashMap<>();
//                Map<String, BasicBSONObject> oldMap = new HashMap<>();
//                Map<String, Map<String, BasicBSONObject>> oldMapToSent = new HashMap<>();
//                for (FinalizeServiceDataBean serviceDataBean : writeServiceDataBeans) {
//                    BasicBSONObject basicBSONObject = new BasicBSONObject();
//                    if (serviceDataBean.getPrice() != null) {
//                        priceMap.put(serviceDataBean.getTag(), Double.parseDouble(serviceDataBean.getPrice().toString()));
//                    }
//                    if (serviceDataBean.getCurrencyCode() != null) {
//                        currencyCodeMap.put(serviceDataBean.getTag(), serviceDataBean.getCurrencyCode());
//                    }
//                    if (serviceDataBean.getColorId() != null) {
//                        basicBSONObject.put(HkSystemConstantUtil.PlanStaticFieldName.COLOR, serviceDataBean.getColorId());
//                    }
//                    if (serviceDataBean.getClarityId() != null) {
//                        basicBSONObject.put(HkSystemConstantUtil.PlanStaticFieldName.CLARITY, serviceDataBean.getClarityId());
//                    }
//                    if (serviceDataBean.getCutId() != null) {
//                        basicBSONObject.put(HkSystemConstantUtil.PlanStaticFieldName.CUT, serviceDataBean.getCutId());
//                    }
//                    if (serviceDataBean.getFlurosceneId() != null) {
//                        basicBSONObject.put(HkSystemConstantUtil.PlanStaticFieldName.FLUROSCENE, serviceDataBean.getFlurosceneId());
//                    }
//                    if (serviceDataBean.getCaratId() != null) {
//                        basicBSONObject.put(HkSystemConstantUtil.PlanStaticFieldName.CARATE_RANGE, serviceDataBean.getCaratId());
//                    }
//                    if (serviceDataBean.getPrice() != null) {
//                        basicBSONObject.put(HkSystemConstantUtil.PlanStaticFieldName.PRICE, serviceDataBean.getPrice());
//                    }
//                    if (serviceDataBean.getCaratValue() != null) {
//                        basicBSONObject.put(HkSystemConstantUtil.PlanStaticFieldName.CARAT, serviceDataBean.getCaratValue());
//                    }
//                    if (serviceDataBean.getCurrencyCode() != null) {
//                        basicBSONObject.put(HkSystemConstantUtil.PlanStaticFieldName.CURRENCY_CODE, serviceDataBean.getCurrencyCode());
//                    }
//
//                    if (!CollectionUtils.isEmpty(serviceDataBean.getFinalizeCustom()) && !CollectionUtils.isEmpty(uIFieldNameWithComponentTypes)) {
//                        for (Map.Entry<String, Object> entry : serviceDataBean.getFinalizeCustom().entrySet()) {
//                            String key = entry.getKey();
//                            Object value = entry.getValue();
//                            if (key != null && uIFieldNameWithComponentTypes.containsKey(key)) {
//                                String type = uIFieldNameWithComponentTypes.get(key);
//                                if (type.equals(HkSystemConstantUtil.CustomFieldComponentType.MULTISELECT_DROPDOWN) || type.equals(HkSystemConstantUtil.CustomFieldComponentType.USER_MULTISELECT) || type.equals(pointerComponentType)) {
//                                    if (serviceDataBean.getFinalizeCustom().containsKey(key)) {
//                                        serviceDataBean.getFinalizeCustom().put(key, HkSystemConstantUtil.DbFieldType.ARRAY);
//                                    }
//
//                                    if (serviceDataBean.getFinalizeCustom().containsKey(key)) {
//                                        String customVal = serviceDataBean.getFinalizeCustom().get(key).toString();
//                                        List<String> values = new ArrayList<>();
//                                        String[] valueArray = customVal.replace("\"", "").split(",");
//                                        for (String v : valueArray) {
//                                            values.add(v.replace("\"", ""));
//                                        }
//                                        serviceDataBean.getFinalizeCustom().put(key, values);
//                                    }
//                                }
//                            }
//                        }
//
//                        basicBSONObject = fieldService.makeBSONObject(serviceDataBean.getFinalizeCustom(), finalizeServiceDataBean.getFinalizeDbType(), HkSystemConstantUtil.Feature.WRITE, loginDataBean.getCompanyId(), null);
//
//                    }
//
//                    basicBSONObject.put(HkSystemConstantUtil.PlanStaticFieldName.BREAKAGE, serviceDataBean.isBreakage());
//                    if (serviceDataBean.getPlanObjectId() != null) {
//                        oldMap.put(serviceDataBean.getPlanObjectId(), basicBSONObject);
////                        oldMapToSent.put(serviceDataBean.getPlanObjectId(),basicBSONObject);
//                    } else {
//                        map.put(serviceDataBean.getTag(), basicBSONObject);
//                    }
//                }
//                List<ObjectId> objectIds = null;
//                if (!CollectionUtils.isEmpty(finalizeServiceDataBean.getDeletedIds())) {
//                    objectIds = new ArrayList<>();
//                    for (String id : finalizeServiceDataBean.getDeletedIds()) {
//                        objectIds.add(new ObjectId(id));
//                    }
//                }
//                LOGGER.info("mainTag :" + finalizeServiceDataBean.getPlanObjectId());
//                LOGGER.info("deletePlanIds :" + objectIds);
//                LOGGER.info("BasicBSONObject :" + map);
//                LOGGER.info("price map :" + priceMap);
//                LOGGER.info("currencyCodeMap :" + currencyCodeMap);
//                LOGGER.info("currencyCodeMap :" + oldMap);
//                UMFeatureDetailDataBean feature = applicationUtil.getuMFeatureDetailDataBeanMap().get(HkSystemConstantUtil.Feature.PLAN);
//                Map<String, String> dbFieldWithFormulaMap = fieldService.retrieveFeatureNameWithDbFieldListForFormula(feature.getId(), loginDataBean.getCompanyId(), HkSystemConstantUtil.FeatureForFormulaEvaluation.ADD_PLAN, HkSystemConstantUtil.CustomFieldComponentType.FORMULA);
//
//                return stockService.editPlans(new ObjectId(finalizeServiceDataBean.getPlanObjectId()), oldMap, objectIds, map, loginDataBean.getCompanyId(), loginDataBean.getId(), priceMap, currencyCodeMap, dbFieldWithFormulaMap);
////                return true;
//            } else {
//                LOGGER.info("collection not found");
//            }
//        }
        return false;
    }

    public Map<Long, Date> retrievePriceList() {
        return stockService.retrievePriceListIdAndDate(loginDataBean.getCompanyId());

    }

    public Map retrieveValueFromPriceList(Map<String, Map<String, Object>> payload) {

        Map result = new HashMap<>();
        Map<String, Object> pricelist = payload.get("pricelist");
        Long pricelistId = null;
        if (pricelist != null) {
            pricelistId = Long.valueOf(pricelist.get("id").toString());
        }
        List<HkPriceListDetailDocument> pricelistDtls = stockService.retrievePriceListDetailDocumentsFromPriceListId(pricelistId);
        //System.out.println("pricelistDtls::::" + pricelistDtls);
        if (!CollectionUtils.isEmpty(pricelistDtls)) {
            for (Map.Entry<String, Map<String, Object>> entry : payload.entrySet()) {
                if (entry.getKey() != "pricelist") {
                    Long caratId = null;
                    Long cut = null;
                    Long clarity = null;
                    Long color = null;
                    Long fluorescence = null;
                    if (entry.getValue().get("cut") != null) {
                        cut = Long.valueOf(entry.getValue().get("cut").toString());
                    }
                    //System.out.println("cut::" + cut);
                    if (entry.getValue().get("carat") != null) {
                        caratId = Long.valueOf(entry.getValue().get("carat").toString());
                    }
                    //System.out.println("carat::" + caratId);
                    if (entry.getValue().get("clarity") != null) {
                        clarity = Long.valueOf(entry.getValue().get("clarity").toString());
                    }
                    //System.out.println("clarity::" + clarity);
                    if (entry.getValue().get("color") != null) {
                        color = Long.valueOf(entry.getValue().get("color").toString());
                    }
                    //System.out.println("color::" + color);
                    if (entry.getValue().get("fluorescence") != null) {
                        fluorescence = Long.valueOf(entry.getValue().get("fluorescence").toString());
                    }
                    //System.out.println("fluorescence::" + fluorescence);

                    HkPriceListDetailDocument matchedPriceListDetailDocument = null;
                    for (HkPriceListDetailDocument hkPriceListDetailDocument : pricelistDtls) {
                        //System.out.println("hkPriceListDetailDocument:::" + hkPriceListDetailDocument);
                        if (hkPriceListDetailDocument.getClarity().equals(clarity)) {
                            if (hkPriceListDetailDocument.getColor().equals(color)) {
                                if (hkPriceListDetailDocument.getCut().equals(cut)) {
                                    if (hkPriceListDetailDocument.getCaratRange().equals(caratId)) {
                                        if (fluorescence != null && hkPriceListDetailDocument.getFluorescence() != null) {
                                            if (hkPriceListDetailDocument.getFluorescence().equals(fluorescence)) {
                                                matchedPriceListDetailDocument = hkPriceListDetailDocument.clone();
                                                break;
                                            }
                                        } else {
                                            matchedPriceListDetailDocument = hkPriceListDetailDocument.clone();
                                            break;
                                        }
                                    }
                                }
                            }
                        }

                    }
                    if (matchedPriceListDetailDocument != null) {
                        //System.out.println("Value matched");
                        result.put(entry.getKey(), matchedPriceListDetailDocument.getHkgPrice());
                    } else {
                        result.put(entry.getKey(), "N/A");
                        //System.out.println("Value not matched");
                    }
                }
            }
        }
        return result;
    }

}
