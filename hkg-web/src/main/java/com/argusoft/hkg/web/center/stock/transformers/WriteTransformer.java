/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.center.stock.transformers;

import com.argusoft.hkg.common.constantutil.HkSystemConstantUtil;
import com.argusoft.hkg.common.constantutil.HkSystemConstantUtil.SERVICE_CODE;
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
import com.argusoft.hkg.web.center.stock.databeans.WriteServiceDataBean;
import com.argusoft.hkg.web.center.transformers.CenterCustomFieldTransformer;
import com.argusoft.hkg.web.center.usermanagement.databeans.LoginDataBean;
import com.argusoft.hkg.web.center.usermanagement.databeans.UMFeatureDetailDataBean;
import com.argusoft.hkg.web.center.util.ApplicationUtil;
import com.argusoft.hkg.web.master.databeans.MasterDataBean;
import com.argusoft.hkg.web.util.SelectItem;
import com.argusoft.sync.center.model.HkFieldDocument;
import com.argusoft.sync.center.model.HkMasterValueDocument;
import java.util.ArrayList;
import java.util.Arrays;
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
public class WriteTransformer {

    @Autowired
    LoginDataBean loginDataBean;

    @Autowired
    HkStockService stockService;

    @Autowired
    ApplicationUtil applicationUtil;

    @Autowired
    HkCustomFieldService fieldService;

    @Autowired
    StockTransformer stockTransformer;

    @Autowired
    HkFoundationDocumentService foundationService;

    @Autowired
    HkUserService userService;

    @Autowired
    CenterCustomFieldTransformer centerCustomFieldTransformer;

    private static final Logger LOGGER = LoggerFactory.getLogger(WriteTransformer.class);
    
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
                        Map<Object, Object> parcelMap = new HashMap<>();
                        for (String parcelField : lotDbFieldNames) {
                            Object value = parcelFieldValueMap.get(parcelField);
                            parcelMap.put(parcelField, value);
                        }

                        selectItem.setCustom4(parcelMap);
                    }
                }
                if (packetFieldExist) {
                    Map<String, Object> lotFieldValueMap = packetDocument.getFieldValue().toMap();
                    if (!CollectionUtils.isEmpty(packetDbFieldNames)) {
                        Map<Object, Object> lotMap = new HashMap<>();
                        for (String lotField : packetDbFieldNames) {
                            Object value = lotFieldValueMap.get(lotField);
                            lotMap.put(lotField, value);
                        }
                        selectItem.setCustom5(lotMap);
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
        Map<String, String> codeMasterName = null;
        if (!CollectionUtils.isEmpty(retrieveMapOfDBFieldNameWithEntity)) {
            codeMasterName = new HashMap<>();
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

    public String savePlan(WriteServiceDataBean writeServiceDataBean) {
//        if (writeServiceDataBean != null) {
//            String planType = writeServiceDataBean.getPlanType();
//            ObjectId lotId = null;
//            ObjectId packetId = null;
//            if (writeServiceDataBean.isHasPacket()) {
//                packetId = new ObjectId(writeServiceDataBean.getId());
//            } else {
//                lotId = new ObjectId(writeServiceDataBean.getId());
//            }
//            List<WriteServiceDataBean> writeServiceDataBeans = writeServiceDataBean.getWriteServiceDataBeans();
//            if (!CollectionUtils.isEmpty(writeServiceDataBeans)) {
//                Map<String, Double> priceMap = new HashMap<>();
//                Map<String, String> currencyCodeMap = new HashMap<>();
//                Map<String, String> writeDbType = writeServiceDataBean.getWriteDbType();
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
//                for (WriteServiceDataBean serviceDataBean : writeServiceDataBeans) {
//                    if (serviceDataBean.getPrice() != null) {
//                        priceMap.put(serviceDataBean.getTag(), Double.parseDouble(serviceDataBean.getPrice().toString()));
//                    }
//                    if (serviceDataBean.getCurrencyCode() != null) {
//                        currencyCodeMap.put(serviceDataBean.getTag(), serviceDataBean.getCurrencyCode());
//                    }
//                    BasicBSONObject basicBSONObject = new BasicBSONObject();
//                    if (!CollectionUtils.isEmpty(serviceDataBean.getWriteCustom())) {
//                        for (Map.Entry<String, Object> entry : serviceDataBean.getWriteCustom().entrySet()) {
//                            String key = entry.getKey();
//                            Object value = entry.getValue();
//                            if (key != null && uIFieldNameWithComponentTypes.containsKey(key)) {
//                                String type = uIFieldNameWithComponentTypes.get(key);
//                                if (type.equals(HkSystemConstantUtil.CustomFieldComponentType.MULTISELECT_DROPDOWN) || type.equals(HkSystemConstantUtil.CustomFieldComponentType.USER_MULTISELECT) || type.equals(pointerComponentType)) {
//                                    if (serviceDataBean.getWriteCustom().containsKey(key)) {
//                                        serviceDataBean.getWriteCustom().put(key, HkSystemConstantUtil.DbFieldType.ARRAY);
//                                    }
//
//                                    if (serviceDataBean.getWriteCustom().containsKey(key)) {
//                                        String customVal = serviceDataBean.getWriteCustom().get(key).toString();
//                                        List<String> values = new ArrayList<>();
//                                        String[] valueArray = customVal.replace("\"", "").split(",");
//                                        for (String v : valueArray) {
//                                            values.add(v.replace("\"", ""));
//                                        }
//                                        serviceDataBean.getWriteCustom().put(key, values);
//                                    }
//                                }
//                            }
//                        }
//
//                        basicBSONObject = fieldService.makeBSONObject(serviceDataBean.getWriteCustom(), writeServiceDataBean.getWriteDbType(), HkSystemConstantUtil.Feature.WRITE, loginDataBean.getCompanyId(), null);
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
//                }
//                UMFeatureDetailDataBean feature = applicationUtil.getuMFeatureDetailDataBeanMap().get(HkSystemConstantUtil.Feature.PLAN);
//                Map<String, String> dbFieldWithFormulaMap = fieldService.retrieveFeatureNameWithDbFieldListForFormula(feature.getId(), loginDataBean.getCompanyId(), HkSystemConstantUtil.FeatureForFormulaEvaluation.ADD_PLAN, HkSystemConstantUtil.CustomFieldComponentType.FORMULA);
//                LOGGER.info("planType" + planType);
//                LOGGER.info("map" + map);
//                LOGGER.info("priceMap" + priceMap);
//                LOGGER.info("currencyCodeMap" + currencyCodeMap);
//                LOGGER.info("dbFieldWithFormulaMap" + dbFieldWithFormulaMap);
//                LOGGER.info("improve from" + writeServiceDataBean.getImproveFromId());
//                return stockService.savePlans(planType, map, loginDataBean.getCompanyId(), loginDataBean.getId(), lotId, packetId, priceMap, currencyCodeMap, dbFieldWithFormulaMap, writeServiceDataBean.getImproveFromId());
//
//            }
//        }
        return null;
    }

    public List<SelectItem> retrieveCurrencyCode() {
        return centerCustomFieldTransformer.retrieveCurrencyForComboForDynamicForm();
    }

    public List<WriteServiceDataBean> retrievePlan(String workallotmentId, String status) {
//        HkWorkAllotmentDocument allotmentDocument = workAllotmentService.retrieveWorkAllotmentById(new ObjectId(workallotmentId));
//        List<HkPlanDocument> planDocuments = stockService.retrieveEnteredPlansByLoggedInUsers(loginDataBean.getId(), loginDataBean.getCompanyId());
        List<WriteServiceDataBean> serviceDataBeans = null;
//        if (!CollectionUtils.isEmpty(planDocuments) && allotmentDocument != null) {
//            serviceDataBeans = new ArrayList<>();
//            for (HkPlanDocument planDocument : planDocuments) {
//                if (allotmentDocument.getUnitType().equals(HkSystemConstantUtil.WORKALLOTEMENT_UNIT_TYPE.LOT) && planDocument.getLot().equals(allotmentDocument.getUnitInstance())) {
//                    if (status != null && planDocument.getStatus().equalsIgnoreCase(status)) {
//                        WriteServiceDataBean writeServiceDataBean = this.convertPlanDocToDataBean(planDocument, new WriteServiceDataBean());
//                        serviceDataBeans.add(writeServiceDataBean);
//                    } else if (status == null) {
//                        WriteServiceDataBean writeServiceDataBean = this.convertPlanDocToDataBean(planDocument, new WriteServiceDataBean());
//                        serviceDataBeans.add(writeServiceDataBean);
//                    }
//                } else if (allotmentDocument.getUnitType().equals(HkSystemConstantUtil.WORKALLOTEMENT_UNIT_TYPE.PACKET) && (planDocument.getPacket() != null && planDocument.getPacket().equals(allotmentDocument.getUnitInstance()))) {
//                    if (status != null && planDocument.getStatus().equalsIgnoreCase(status)) {
//                        WriteServiceDataBean writeServiceDataBean = this.convertPlanDocToDataBean(planDocument, new WriteServiceDataBean());
//                        serviceDataBeans.add(writeServiceDataBean);
//                    } else if (status == null) {
//                        WriteServiceDataBean writeServiceDataBean = this.convertPlanDocToDataBean(planDocument, new WriteServiceDataBean());
//                        serviceDataBeans.add(writeServiceDataBean);
//                    }
//                }
//            }
//        } else {
//            LOGGER.info("plan doc not found");
//        }
        return serviceDataBeans;
    }

    private WriteServiceDataBean convertPlanDocToDataBean(HkPlanDocument planDocument, WriteServiceDataBean serviceDataBean) {
//        if (serviceDataBean == null) {
//            serviceDataBean = new WriteServiceDataBean();
//        }
//        serviceDataBean.setPlanObjectId(planDocument.getId().toString());
//        serviceDataBean.setTag(planDocument.getTag());
//        serviceDataBean.setPlanType(planDocument.getPlanType());
//        serviceDataBean.setCreatedBy(planDocument.getCreatedBy());
//        if (planDocument.getReferencePlan() != null) {
//            serviceDataBean.setReferencePlan(planDocument.getReferencePlan().toString());
//        }
//        List<HkPriceHistoryDocument> priceHistoryDocuments = planDocument.getHkPriceHistoryList();
//        BasicBSONObject fieldValue = planDocument.getFieldValue();
//        if (fieldValue != null) {
//            Map<String, Object> map = fieldValue.toMap();
//            map.put("tag", planDocument.getTag());
//            serviceDataBean.setWriteCustom(map);
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

    public Boolean submitPlans(Map<String, List<String>> map) {
//        if (!CollectionUtils.isEmpty(map)) {
//            List<ObjectId> workallocationId = new ArrayList<>();
//            List<ObjectId> planId = new ArrayList<>();
//            ObjectId bestPlanObjectId = null;
//            for (Map.Entry<String, List<String>> entry : map.entrySet()) {
//                String key = entry.getKey();
//                if (key.equalsIgnoreCase("plan")) {
//                    List<String> value = entry.getValue();
//                    for (String id : value) {
//                        planId.add(new ObjectId(id));
//                    }
//                } else if (key.equalsIgnoreCase("work")) {
//                    List<String> value = entry.getValue();
//                    for (String id : value) {
//                        workallocationId.add(new ObjectId(id));
//                    }
//                } else if (key.equalsIgnoreCase("bestPlanId")) {
//                    List<String> value = entry.getValue();
//                    bestPlanObjectId = new ObjectId(value.get(0).toString());
//                }
//
//            }
//            LOGGER.info("planId :" + planId);
//            LOGGER.info("workallocationId : " + workallocationId);
//            UMFeatureDetailDataBean feature = applicationUtil.getuMFeatureDetailDataBeanMap().get(HkSystemConstantUtil.Feature.PLAN);
//            Map<String, String> dbFieldWithFormulaMap = fieldService.retrieveFeatureNameWithDbFieldListForFormula(feature.getId(), loginDataBean.getCompanyId(), HkSystemConstantUtil.FeatureForFormulaEvaluation.ADD_PLAN, HkSystemConstantUtil.CustomFieldComponentType.FORMULA);
//
//            return stockService.submitPlans(loginDataBean.getId(), planId, workallocationId, loginDataBean.getCompanyId(), bestPlanObjectId, dbFieldWithFormulaMap);
//        }
        return false;
    }

    public List<WriteServiceDataBean> retrieveAccessiblePlan(Map<String, String> map) {
        List<WriteServiceDataBean> writeServiceDataBeans = null;
//        if (!CollectionUtils.isEmpty(map)) {
//            String planType = map.get("plan");
//            String nodeId = map.get("nodeId");
//            String objectId = map.get("objectId");
//            String packet = map.get("isPacket");
//            String workallotmentId = map.get("workallotmentId");
//            Boolean isPacket = false;
//            if (packet.equalsIgnoreCase("true")) {
//                isPacket = true;
//            }
//            LOGGER.info("planType " + planType);
//            LOGGER.info("node id " + nodeId);
//            LOGGER.info("object id " + objectId);
//            LOGGER.info("is packet " + isPacket);
//            try {
//                HkWorkAllotmentDocument allotmentDocument = workAllotmentService.retrieveWorkAllotmentById(new ObjectId(workallotmentId));
//                List<HkPlanDocument> planDocuments = stockService.retrieveExistingPlanEntities(planType, Long.parseLong(nodeId), loginDataBean.getCompanyId(), new ObjectId(objectId), isPacket, loginDataBean.getId());
//                if (!CollectionUtils.isEmpty(planDocuments) && allotmentDocument != null) {
//                    writeServiceDataBeans = new ArrayList<>();
//                    List<Long> createdByList = new ArrayList<Long>();
//                    for (HkPlanDocument planDocument : planDocuments) {
//                        if (allotmentDocument.getUnitType().equals(HkSystemConstantUtil.WORKALLOTEMENT_UNIT_TYPE.LOT) && planDocument.getLot().equals(allotmentDocument.getUnitInstance())) {
//                            createdByList.add(planDocument.getCreatedBy());
//                            WriteServiceDataBean writeServiceDataBean = this.convertPlanDocToDataBean(planDocument, new WriteServiceDataBean());
//                            writeServiceDataBeans.add(writeServiceDataBean);
//                        } else if (allotmentDocument.getUnitType().equals(HkSystemConstantUtil.WORKALLOTEMENT_UNIT_TYPE.PACKET) && (planDocument.getPacket() != null && planDocument.getPacket().equals(allotmentDocument.getUnitInstance()))) {
//                            createdByList.add(planDocument.getCreatedBy());
//                            WriteServiceDataBean writeServiceDataBean = this.convertPlanDocToDataBean(planDocument, new WriteServiceDataBean());
//                            writeServiceDataBeans.add(writeServiceDataBean);
//                        }
//
//                    }
//                    if (!CollectionUtils.isEmpty(createdByList)) {
//                        List<SyncCenterUserDocument> centerUserDocuments = userService.retrieveUsersByIds(createdByList);
//                        if (!CollectionUtils.isEmpty(centerUserDocuments)) {
//                            for (WriteServiceDataBean serviceDataBean : writeServiceDataBeans) {
//                                for (SyncCenterUserDocument syncCenterUserDocument : centerUserDocuments) {
//                                    if (serviceDataBean.getCreatedBy() == syncCenterUserDocument.getId()) {
//                                        Map<String, Object> nameMap = new HashMap<>();
//                                        if (loginDataBean.getId() != serviceDataBean.getCreatedBy()) {
//                                            serviceDataBean.setNameOfCreatedBy(syncCenterUserDocument.getFirstName().concat(" ").concat(syncCenterUserDocument.getLastName()));
//                                        } else {
//                                            serviceDataBean.setNameOfCreatedBy("You");
//                                        }
//                                        nameMap.put("createdByName", serviceDataBean.getNameOfCreatedBy());
//                                        nameMap.putAll(serviceDataBean.getWriteCustom());
//                                        serviceDataBean.setWriteCustom(nameMap);
//                                        break;
//                                    }
//                                }
//                            }
//                        }
//                    }
//                    return writeServiceDataBeans;
//                } else {
//                    LOGGER.info("plan doc not found");
//                }
//            } catch (NumberFormatException e) {
//                LOGGER.info("Number format exception found.");
//            }
//
//        }
        return writeServiceDataBeans;

    }

    public Boolean deletePlan(String planId) {
        return null;
//        return stockService.deleteMainTagPlan(new ObjectId(planId), loginDataBean.getId());
    }

    public Boolean editEnteredPlan(WriteServiceDataBean writeServiceDataBean) {
//        if (writeServiceDataBean != null) {
//            String planType = writeServiceDataBean.getPlanType();
////            ObjectId lotId = null;
////            ObjectId packetId = null;
////            if (writeServiceDataBean.isHasPacket()) {
////                packetId = new ObjectId(writeServiceDataBean.getId());
////            } else {
////                lotId = new ObjectId(writeServiceDataBean.getId());
////            }
//            List<WriteServiceDataBean> writeServiceDataBeans = writeServiceDataBean.getWriteServiceDataBeans();
//            if (!CollectionUtils.isEmpty(writeServiceDataBeans)) {
//                Map<String, Double> priceMap = new HashMap<>();
//                Map<String, String> currencyCodeMap = new HashMap<>();
//                Map<String, String> writeDbType = writeServiceDataBean.getWriteDbType();
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
//                for (WriteServiceDataBean serviceDataBean : writeServiceDataBeans) {
//                    if (serviceDataBean.getPrice() != null) {
//                        priceMap.put(serviceDataBean.getTag(), Double.parseDouble(serviceDataBean.getPrice().toString()));
//                    }
//                    currencyCodeMap.put(serviceDataBean.getTag(), serviceDataBean.getCurrencyCode());
//                    BasicBSONObject basicBSONObject = new BasicBSONObject();
//                    if (!CollectionUtils.isEmpty(serviceDataBean.getWriteCustom()) && !CollectionUtils.isEmpty(uIFieldNameWithComponentTypes)) {
//                        for (Map.Entry<String, Object> entry : serviceDataBean.getWriteCustom().entrySet()) {
//                            String key = entry.getKey();
//                            Object value = entry.getValue();
//                            if (key != null && uIFieldNameWithComponentTypes.containsKey(key)) {
//                                String type = uIFieldNameWithComponentTypes.get(key);
//                                if (type.equals(HkSystemConstantUtil.CustomFieldComponentType.MULTISELECT_DROPDOWN) || type.equals(HkSystemConstantUtil.CustomFieldComponentType.USER_MULTISELECT) || type.equals(pointerComponentType)) {
//                                    if (serviceDataBean.getWriteCustom().containsKey(key)) {
//                                        serviceDataBean.getWriteCustom().put(key, HkSystemConstantUtil.DbFieldType.ARRAY);
//                                    }
//
//                                    if (serviceDataBean.getWriteCustom().containsKey(key)) {
//                                        String customVal = serviceDataBean.getWriteCustom().get(key).toString();
//                                        List<String> values = new ArrayList<>();
//                                        String[] valueArray = customVal.replace("\"", "").split(",");
//                                        for (String v : valueArray) {
//                                            values.add(v.replace("\"", ""));
//                                        }
//                                        serviceDataBean.getWriteCustom().put(key, values);
//                                    }
//                                }
//                            }
//                        }
//
//                        basicBSONObject = fieldService.makeBSONObject(serviceDataBean.getWriteCustom(), writeServiceDataBean.getWriteDbType(), HkSystemConstantUtil.Feature.WRITE, loginDataBean.getCompanyId(), null);
//
//                    }
//                    LOGGER.info("color id :" + serviceDataBean.getColorId());
//                    basicBSONObject.put(HkSystemConstantUtil.PlanStaticFieldName.COLOR, serviceDataBean.getColorId());
//                    basicBSONObject.put(HkSystemConstantUtil.PlanStaticFieldName.CLARITY, serviceDataBean.getClarityId());
//                    basicBSONObject.put(HkSystemConstantUtil.PlanStaticFieldName.CUT, serviceDataBean.getCutId());
//                    basicBSONObject.put(HkSystemConstantUtil.PlanStaticFieldName.FLUROSCENE, serviceDataBean.getFlurosceneId());
//                    basicBSONObject.put(HkSystemConstantUtil.PlanStaticFieldName.CARATE_RANGE, serviceDataBean.getCaratId());
//                    basicBSONObject.put(HkSystemConstantUtil.PlanStaticFieldName.PRICE, serviceDataBean.getPrice());
//                    basicBSONObject.put(HkSystemConstantUtil.PlanStaticFieldName.CARAT, serviceDataBean.getCaratValue());
//                    basicBSONObject.put(HkSystemConstantUtil.PlanStaticFieldName.CURRENCY_CODE, serviceDataBean.getCurrencyCode());
//                    basicBSONObject.put(HkSystemConstantUtil.PlanStaticFieldName.BREAKAGE, serviceDataBean.isBreakage());
//                    if (serviceDataBean.getPlanObjectId() != null) {
//                        oldMap.put(serviceDataBean.getPlanObjectId(), basicBSONObject);
////                        oldMapToSent.put(serviceDataBean.getPlanObjectId(),basicBSONObject);
//                    } else {
//                        map.put(serviceDataBean.getTag(), basicBSONObject);
//                    }
//                }
//                List<ObjectId> objectIds = null;
//                if (!CollectionUtils.isEmpty(writeServiceDataBean.getDeletedIds())) {
//                    objectIds = new ArrayList<>();
//                    for (String id : writeServiceDataBean.getDeletedIds()) {
//                        objectIds.add(new ObjectId(id));
//                    }
//                }
//                LOGGER.info("mainTag :" + writeServiceDataBean.getPlanObjectId());
//                LOGGER.info("deletePlanIds :" + objectIds);
//                LOGGER.info("BasicBSONObject :" + map);
//                LOGGER.info("price map :" + priceMap);
//                LOGGER.info("currencyCodeMap :" + currencyCodeMap);
//                LOGGER.info("currencyCodeMap :" + oldMap);
//                UMFeatureDetailDataBean feature = applicationUtil.getuMFeatureDetailDataBeanMap().get(HkSystemConstantUtil.Feature.PLAN);
//                Map<String, String> dbFieldWithFormulaMap = fieldService.retrieveFeatureNameWithDbFieldListForFormula(feature.getId(), loginDataBean.getCompanyId(), HkSystemConstantUtil.FeatureForFormulaEvaluation.ADD_PLAN, HkSystemConstantUtil.CustomFieldComponentType.FORMULA);
//
//                return stockService.editPlans(new ObjectId(writeServiceDataBean.getPlanObjectId()), oldMap, objectIds, map, loginDataBean.getCompanyId(), loginDataBean.getId(), priceMap, currencyCodeMap, dbFieldWithFormulaMap);
////                return true;
//            } else {
//                LOGGER.info("collection not found");
//            }
//        }
        return false;
    }
}
