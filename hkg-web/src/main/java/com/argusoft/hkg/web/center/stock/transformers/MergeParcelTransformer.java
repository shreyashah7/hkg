package com.argusoft.hkg.web.center.stock.transformers;

import com.argusoft.hkg.common.constantutil.HkSystemConstantUtil;
import com.argusoft.hkg.nosql.core.HkCustomFieldService;
import com.argusoft.hkg.nosql.core.HkStockService;
import com.argusoft.hkg.nosql.model.HkParcelDocument;
import com.argusoft.hkg.nosql.model.HkRoughStockDetailDocument;
import com.argusoft.hkg.web.center.stock.databeans.MergeParcelDataBean;
import com.argusoft.hkg.web.center.stock.databeans.RoughStockDetailDataBean;
import com.argusoft.hkg.web.center.usermanagement.databeans.LoginDataBean;
import com.argusoft.hkg.web.center.usermanagement.databeans.UMFeatureDetailDataBean;
import com.argusoft.hkg.web.center.util.ApplicationUtil;
import com.argusoft.sync.center.model.HkFieldDocument;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.bson.BasicBSONObject;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 *
 * @author rajkumar
 */
@Service
public class MergeParcelTransformer {

    @Autowired
    HkStockService stockService;

    @Autowired
    LoginDataBean loginDataBean;

    @Autowired
    ApplicationUtil applicationUtil;

    @Autowired
    HkCustomFieldService fieldService;

    @Autowired
    HkCustomFieldService customFieldService;

//    public List<SelectItem> searchParcel(ParcelDataBean parcelDataBean) {
//        Map<String, List<String>> featureDbFieldMap = parcelDataBean.getFeatureDbFieldMap();
//        List<String> invoiceDbField = null;
//        List<String> parcelDbField = null;
//        if (!CollectionUtils.isEmpty(featureDbFieldMap)) {
//            if (!CollectionUtils.isEmpty(featureDbFieldMap.get("invoice"))) {
//                invoiceDbField = featureDbFieldMap.get("invoice");
//            }
//            if (!CollectionUtils.isEmpty(featureDbFieldMap.get("parcel"))) {
//                parcelDbField = featureDbFieldMap.get("parcel");
//            }
//
//        }
//        Long franchise = loginDataBean.getCompanyId();
//        Map<String, Map<String, Object>> featureCustomMapValue = parcelDataBean.getFeatureCustomMapValue();
//        Map<String, Object> invoiceFieldMap = new HashMap<>();
//        Map<String, Object> parcelFieldMap = new HashMap<>();
//        if (!CollectionUtils.isEmpty(featureCustomMapValue)) {
//            for (Map.Entry<String, Map<String, Object>> entry : featureCustomMapValue.entrySet()) {
//                String featureName = entry.getKey();
//                Map<String, Object> map = entry.getValue();
//                if (!StringUtils.isEmpty(featureName)) {
//                    if (featureName.equalsIgnoreCase(HkSystemConstantUtil.Feature.INVOICE)) {
//                        if (!CollectionUtils.isEmpty(map)) {
//                            for (Map.Entry<String, Object> entry1 : map.entrySet()) {
//                                String key = entry1.getKey();
//                                Object value = entry1.getValue();
//                                invoiceFieldMap.put(key, value);
//                            }
//                        }
//
//                    } else if (featureName.equalsIgnoreCase(HkSystemConstantUtil.Feature.PARCEL)) {
//                        if (!CollectionUtils.isEmpty(map)) {
//                            for (Map.Entry<String, Object> entry1 : map.entrySet()) {
//                                String key = entry1.getKey();
//                                Object value = entry1.getValue();
//                                parcelFieldMap.put(key, value);
//                            }
//                        }
//
//                    }
//                }
//            }
//            List<HkInvoiceDocument> invoiceDocuments = null;
//            if (!CollectionUtils.isEmpty(invoiceFieldMap)) {
//                Iterator<Map.Entry<String, Object>> iter = invoiceFieldMap.entrySet().iterator();
//                while (iter.hasNext()) {
//                    Map.Entry<String, Object> entry = iter.next();
//                    String key = entry.getKey();
//                    Object value = entry.getValue();
//                    if (key.contains("$MS$") || key.contains("$UMS$") || key.contains("$AG$")) {
//                        System.out.println("value :" + value + value.getClass().getSimpleName());
//                        if (value instanceof String && !StringUtils.isEmpty(value)) {
//                            List<String> items = Arrays.asList(value.toString().split(","));
//                            invoiceFieldMap.put(key, items);
//                        } else if (value instanceof Collection && !CollectionUtils.isEmpty((Collection<?>) value)) {
//                            List<String> items = Arrays.asList(value.toString().split(","));
//                            invoiceFieldMap.put(key, items);
//                        } else {
//                            iter.remove();
//                        }
//                    }
//                }
//                if (!CollectionUtils.isEmpty(invoiceFieldMap)) {
//                    invoiceDocuments = stockService.retrieveInvoices(invoiceFieldMap, franchise, Boolean.FALSE, null, null,null,null);
//                }
//            }
//            List<ObjectId> invoiceIds = null;
//            if (!CollectionUtils.isEmpty(invoiceDocuments)) {
//                invoiceIds = new ArrayList<>();
//                for (HkInvoiceDocument invoiceDocument : invoiceDocuments) {
//                    invoiceIds.add(invoiceDocument.getId());
//                }
//            }
//            List<HkParcelDocument> parcelDocuments = null;
//            if (!CollectionUtils.isEmpty(parcelFieldMap) || !CollectionUtils.isEmpty(invoiceIds)) {
//                Iterator<Map.Entry<String, Object>> iter = parcelFieldMap.entrySet().iterator();
//                while (iter.hasNext()) {
//                    Map.Entry<String, Object> entry = iter.next();
//                    String key = entry.getKey();
//                    Object value = entry.getValue();
//                    if (key.contains("$MS$") || key.contains("$UMS$") || key.contains("$AG$")) {
//                        System.out.println("value :" + value + value.getClass().getSimpleName());
//                        if (value instanceof String && !StringUtils.isEmpty(value)) {
//                            List<String> items = Arrays.asList(value.toString().split(","));
//                            parcelFieldMap.put(key, items);
//                        } else if (value instanceof Collection && !CollectionUtils.isEmpty((Collection<?>) value)) {
//                            List<String> items = Arrays.asList(value.toString().split(","));
//                            parcelFieldMap.put(key, items);
//                        } else {
//                            iter.remove();
//                        }
//                    }
//                }
//
//                if (!CollectionUtils.isEmpty(parcelFieldMap) || !CollectionUtils.isEmpty(invoiceIds)) {
//                    parcelDocuments = stockService.retrieveParcels(parcelFieldMap, invoiceIds, null, franchise, Boolean.FALSE, Boolean.FALSE);
//                }
//            }
//            if (!CollectionUtils.isEmpty(parcelDocuments)) {
//                List<SelectItem> selectItems = new ArrayList<>();
//                invoiceIds = new ArrayList<>();
//                for (HkParcelDocument parcelDocument : parcelDocuments) {
//                    invoiceIds.add(parcelDocument.getInvoice());
//                }
//                List<HkInvoiceDocument> hkInvoiceDocuments = stockService.retrieveInvoices(invoiceIds, franchise, Boolean.FALSE);
//                if (!CollectionUtils.isEmpty(hkInvoiceDocuments)) {
//                    for (HkParcelDocument parcelDocument : parcelDocuments) {
//                        Map<String, Object> map = new HashMap<>();
//                        SelectItem selectItem = new SelectItem(parcelDocument.getId().toString(), parcelDocument.getInvoice().toString());
//                        BasicBSONObject fieldValue = parcelDocument.getFieldValue();
//                        if (fieldValue != null) {
//                            map.put(HkSystemConstantUtil.MANDATORY_FIELD.CARAT_OF_PARCEL, fieldValue.toMap().get(HkSystemConstantUtil.MANDATORY_FIELD.CARAT_OF_PARCEL));
//                        }
//                        if (fieldValue != null && !CollectionUtils.isEmpty(parcelDbField)) {
////                            if (parcelDbField.indexOf(HkSystemConstantUtil.AutoNumber.PARCEL_ID) == -1) {
////                                map.put(HkSystemConstantUtil.AutoNumber.PARCEL_ID, fieldValue.toMap().get(HkSystemConstantUtil.AutoNumber.PARCEL_ID));
////                            }
//                            for (String dbField : parcelDbField) {
//                                if (fieldValue.toMap().containsKey(dbField)) {
//                                    map.put(dbField, fieldValue.toMap().get(dbField));
//                                }
//                            }
//                        }
//                        for (HkInvoiceDocument hkInvoiceDocument : hkInvoiceDocuments) {
//                            if (hkInvoiceDocument.getId().toString().equals(parcelDocument.getInvoice().toString())) {
//                                BasicBSONObject invoiceFieldValue = hkInvoiceDocument.getFieldValue();
//                                if (invoiceFieldValue != null && !CollectionUtils.isEmpty(invoiceDbField)) {
//                                    for (String dbField : invoiceDbField) {
//                                        if (invoiceFieldValue.toMap().containsKey(dbField)) {
//                                            map.put(dbField, invoiceFieldValue.toMap().get(dbField));
//                                        }
//                                    }
//                                }
//                                selectItem.setDescription(invoiceFieldValue.toMap().get(HkSystemConstantUtil.InvoiceStaticFields.INVOICE_NUMBER).toString());
//                                break;
//                            }
//                        }
//                        selectItem.setCategoryCustom(map);
//                        selectItems.add(selectItem);
//                    }
//                }
//                return selectItems;
//            } else {
//                return null;
//            }
//
//        } else {
//            return null;
//        }
//    }
//
//    public SelectItem retrieveParcelsByIds(List<String> parcelIds) {
//        SelectItem result = new SelectItem();
//        List<ObjectId> newParcelIds = new ArrayList<>();
//        for (String string : parcelIds) {
//            newParcelIds.add(new ObjectId(string));
//        }
//        List<HkParcelDocument> parcels = stockService.retrieveParcels(newParcelIds, null, null);
//
//        if (!CollectionUtils.isEmpty(parcels)) {
//            List<String> invoiceParentDbFieldName = null;
//            List<String> parcelParentDbFieldName = null;
//
//            List<HkFeatureFieldPermissionDocument> permissions = this.retrieveAllFields("parcelmerge");
//
//            for (HkFeatureFieldPermissionDocument hkFeatureFieldPermissionEntity : permissions) {
//                if (hkFeatureFieldPermissionEntity.getParentViewFlag()) {
//                    String dbFieldName = hkFeatureFieldPermissionEntity.getHkFieldEntity().getDbFieldName();
//                    if (hkFeatureFieldPermissionEntity.getEntityName().equalsIgnoreCase(HkSystemConstantUtil.Feature.INVOICE)) {
//                        if (CollectionUtils.isEmpty(invoiceParentDbFieldName)) {
//                            invoiceParentDbFieldName = new ArrayList<>();
//                        }
//                        invoiceParentDbFieldName.add(dbFieldName);
//                    } else if (hkFeatureFieldPermissionEntity.getEntityName().equalsIgnoreCase(HkSystemConstantUtil.Feature.PARCEL)) {
//                        if (CollectionUtils.isEmpty(parcelParentDbFieldName)) {
//                            parcelParentDbFieldName = new ArrayList<>();
//                        }
//                        parcelParentDbFieldName.add(dbFieldName);
//                    }
//                }
//            }
//
//            List<SelectItem> selectItemList = new ArrayList<>();
//            SelectItem finalResult = new SelectItem();
////            List<ObjectId> invoiceIds = new ArrayList<>();
//            for (HkParcelDocument parcelDocument : parcels) {
//                SelectItem selectItem = new SelectItem(null, parcelDocument.getId().toString(), parcelDocument.getInvoice().toString(), null);
////                invoiceIds.add(parcelDocument.getInvoice());
//                List<HkInvoiceDocument> hkInvoiceDocuments = stockService.retrieveInvoices(Arrays.asList(parcelDocument.getInvoice()), loginDataBean.getCompanyId(), Boolean.FALSE);
//
//                HkInvoiceDocument hkInvoiceDocument = hkInvoiceDocuments.get(0);
//
//                List uiFieldListForParcel = new ArrayList<>();
//                if (parcelDocument.getFieldValue() != null) {
//                    Map<String, Object> map1 = parcelDocument.getFieldValue().toMap();
//                    if (!CollectionUtils.isEmpty(map1)) {
//                        for (Map.Entry<String, Object> entrySet : map1.entrySet()) {
//                            String key = entrySet.getKey();
//                            uiFieldListForParcel.add(key);
//                        }
//                    }
//                }
//                Map<String, String> uiFieldListWithComponentTypeForParcel = fieldService.retrieveUIFieldNameWithComponentTypes(uiFieldListForParcel);
//                String pointerComponentType = HkSystemConstantUtil.CustomFieldComponentType.POINTER + "@*" + HkSystemConstantUtil.CustomFieldComponentType.MULTISELECT_DROPDOWN;
//
//                Map<String, Object> customMapforParcel = parcelDocument.getFieldValue().toMap();
//
//                if (!CollectionUtils.isEmpty(uiFieldListWithComponentTypeForParcel)) {
//                    for (Map.Entry<String, String> field : uiFieldListWithComponentTypeForParcel.entrySet()) {
//                        if (field.getValue().equals(HkSystemConstantUtil.CustomFieldComponentType.MULTISELECT_DROPDOWN) || field.getValue().equals(HkSystemConstantUtil.CustomFieldComponentType.USER_MULTISELECT) || field.getValue().equals(pointerComponentType)) {
//                            if (customMapforParcel != null && !customMapforParcel.isEmpty() && customMapforParcel.containsKey(field.getKey())) {
//                                String value = customMapforParcel.get(field.getKey()).toString().replaceAll("\\[", "").replaceAll("\\]", "");
//                                customMapforParcel.put(field.getKey(), value);
//                            }
//                        }
//                    }
//                }
//
//                //Parent fields for parcel.
//                if (!CollectionUtils.isEmpty(parcelParentDbFieldName)) {
//                    Map<Object, Object> map = new HashMap<>();
//                    for (String parcelField : parcelParentDbFieldName) {
//                        if (customMapforParcel.get(parcelField) != null) {
//                            map.put(parcelField, customMapforParcel.get(parcelField));
//                        }
//                    }
//                    selectItem.setCustom4(map);
//                }
//
//                List uiFieldListForInvoice = new ArrayList<>();
//                if (parcelDocument.getFieldValue() != null) {
//                    Map<String, Object> map1 = parcelDocument.getFieldValue().toMap();
//                    if (!CollectionUtils.isEmpty(map1)) {
//                        for (Map.Entry<String, Object> entrySet : map1.entrySet()) {
//                            String key = entrySet.getKey();
//                            uiFieldListForInvoice.add(key);
//                        }
//                    }
//                }
//                Map<String, String> uiFieldListWithComponentTypeForInvoice = fieldService.retrieveUIFieldNameWithComponentTypes(uiFieldListForInvoice);
//
//                Map<String, Object> customMapforInvoice = hkInvoiceDocument.getFieldValue().toMap();
//
//                if (!CollectionUtils.isEmpty(uiFieldListWithComponentTypeForInvoice)) {
//                    for (Map.Entry<String, String> field : uiFieldListWithComponentTypeForInvoice.entrySet()) {
//                        if (field.getValue().equals(HkSystemConstantUtil.CustomFieldComponentType.MULTISELECT_DROPDOWN) || field.getValue().equals(HkSystemConstantUtil.CustomFieldComponentType.USER_MULTISELECT) || field.getValue().equals(pointerComponentType)) {
//                            if (customMapforInvoice != null && !customMapforInvoice.isEmpty() && customMapforInvoice.containsKey(field.getKey())) {
//                                String value = customMapforInvoice.get(field.getKey()).toString().replaceAll("\\[", "").replaceAll("\\]", "");
//                                customMapforInvoice.put(field.getKey(), value);
//                            }
//                        }
//                    }
//                }
//                //Parent fields for invoice.
//                if (!CollectionUtils.isEmpty(invoiceParentDbFieldName)) {
//                    Map<Object, Object> map = new HashMap<>();
//                    for (String invoiceField : invoiceParentDbFieldName) {
//                        if (customMapforInvoice.get(invoiceField) != null) {
//                            map.put(invoiceField, customMapforInvoice.get(invoiceField));
//                        }
//                    }
//                    selectItem.setCustom1(map);
//                }
//
//                selectItemList.add(selectItem);
//            }
//            finalResult.setCustom2(selectItemList);
//            return finalResult;
//        }
//        return result;
//    }
//
//    private List<HkFeatureFieldPermissionDocument> retrieveAllFields(String featureName) {
//
//        UMFeatureDetailDataBean feature = applicationUtil.getuMFeatureDetailDataBeanMap().get(featureName);
//        List<HkFeatureFieldPermissionDocument> fieldPermissionEntitys = new ArrayList<>();
//        if (feature != null) {
//            fieldPermissionEntitys = fieldService.retrieveFeatureFieldPermissions(feature.getId(), loginDataBean.getRoleIds());
//        }
//        return fieldPermissionEntitys;
//    }
//    public Object mergeParcels(StockDataBean stockDataBean) {
//        Boolean result = Boolean.FALSE;
//        if (stockDataBean != null) {
//            ObjectId parent = new ObjectId(stockDataBean.getParentID());
//
//            List<String> uiList = null;
//            Map<String, String> stockDbType = stockDataBean.getStockDbType();
//            if (!CollectionUtils.isEmpty(stockDbType)) {
//                uiList = new ArrayList<>(stockDbType.keySet());
//            }
//            Map<String, String> uIFieldNameWithComponentTypes = customFieldService.retrieveUIFieldNameWithComponentTypes(uiList);
//            String pointerComponentType = HkSystemConstantUtil.CustomFieldComponentType.POINTER + "@*" + HkSystemConstantUtil.CustomFieldComponentType.MULTISELECT_DROPDOWN;
//            Map<String, Object> stockCustom = stockDataBean.getStockCustom();
//            if (!CollectionUtils.isEmpty(stockCustom)) {
//                for (Map.Entry<String, Object> entry : stockCustom.entrySet()) {
//                    String key = entry.getKey();
//                    Object value = entry.getValue();
//                    if (key != null && uIFieldNameWithComponentTypes.containsKey(key)) {
//                        String type = uIFieldNameWithComponentTypes.get(key);
//                        if (type.equals(HkSystemConstantUtil.CustomFieldComponentType.MULTISELECT_DROPDOWN) || type.equals(HkSystemConstantUtil.CustomFieldComponentType.USER_MULTISELECT) || type.equals(pointerComponentType)) {
//                            if (stockDbType.containsKey(key)) {
//                                stockDbType.put(key, HkSystemConstantUtil.DbFieldType.ARRAY);
//                            }
//
//                            if (stockCustom.containsKey(key)) {
//                                String customVal = stockCustom.get(key).toString();
//                                List<String> values = new ArrayList<>();
//                                String[] valueArray = customVal.replace("\"", "").split(",");
//                                for (String v : valueArray) {
//                                    values.add(v.replace("\"", ""));
//                                }
//                                stockCustom.put(key, values);
//                            }
//                        }
//                    }
//
//                }
//            }
//            BasicBSONObject bSONObject = customFieldService.makeBSONObject(stockCustom, stockDbType, HkSystemConstantUtil.Feature.PARCEL, loginDataBean.getCompanyId(), null);
//            List<ObjectId> idsTomerge = new ArrayList<>();
//            for (String idToMerge : stockDataBean.getIdsToMerge()) {
//                idsTomerge.add(new ObjectId(idToMerge));
//            }
//            result = stockService.mergeParcel(parent, idsTomerge, Arrays.asList(bSONObject), loginDataBean.getCompanyId(), loginDataBean.getId());
//            return result;
//        }
//        return null;
//    }
    public Boolean mergeParcel(MergeParcelDataBean mergeParcelDataBean) {
        Boolean result = Boolean.FALSE;
        if (mergeParcelDataBean != null) {
            List<ObjectId> idsToMerge = new ArrayList<>();
            if (!CollectionUtils.isEmpty(mergeParcelDataBean.getRoughStockDetailDataBeans())) {
                List<RoughStockDetailDataBean> roughStockDetailDataBeans = mergeParcelDataBean.getRoughStockDetailDataBeans();
                List<HkRoughStockDetailDocument> hkRoughStockDetailDocuments = new ArrayList<>();
                for (RoughStockDetailDataBean roughStockDetailDataBean : roughStockDetailDataBeans) {
                    HkRoughStockDetailDocument hkRoughStockDetailDocument = new HkRoughStockDetailDocument();
                    hkRoughStockDetailDocument = this.convertRoughStockDetailDataBeanToModel(hkRoughStockDetailDocument, roughStockDetailDataBean, loginDataBean.getId());
                    hkRoughStockDetailDocuments.add(hkRoughStockDetailDocument);
                    idsToMerge.add(hkRoughStockDetailDocument.getParcel());
                }
                stockService.updateRoughStockDetailsWithParcel(hkRoughStockDetailDocuments, null);
            }

            String invoiceId = stockService.saveInvoice(null, null, null, 0l, loginDataBean.getId(), null,null, null, null);
            ;
            BasicBSONObject basicBSONObject = new BasicBSONObject();
            basicBSONObject.put(HkSystemConstantUtil.ParcelStaticFieldName.ORIGINAL_CARAT, mergeParcelDataBean.getMergedCarat());
            basicBSONObject.put(HkSystemConstantUtil.ParcelStaticFieldName.ORIGINAL_PIECES, mergeParcelDataBean.getMergedPieces());
            basicBSONObject.put(HkSystemConstantUtil.ParcelStaticFieldName.ORIGINAL_RATES, mergeParcelDataBean.getMergedRate());
            basicBSONObject.put(HkSystemConstantUtil.ParcelStaticFieldName.ORIGINAL_AMOUNT, mergeParcelDataBean.getMergedAmountInDollar());
            Boolean mergeParcelResult = stockService.mergeParcel(new ObjectId(invoiceId), idsToMerge, Arrays.asList(basicBSONObject), 0l, loginDataBean.getCompanyId());
            return mergeParcelResult;
        }
        return result;

    }

    public HkRoughStockDetailDocument convertRoughStockDetailDataBeanToModel(HkRoughStockDetailDocument hkRoughStockDetailDocument, RoughStockDetailDataBean roughStockDetailDataBean, Long createdBy) {
        hkRoughStockDetailDocument.setAction(HkSystemConstantUtil.RoughStockActions.DEBIT);
        hkRoughStockDetailDocument.setOperation(HkSystemConstantUtil.RoughStockOperations.MERGE);
        hkRoughStockDetailDocument.setCarat(roughStockDetailDataBean.getChangedCarat());
        hkRoughStockDetailDocument.setCreatedBy(createdBy);
        hkRoughStockDetailDocument.setCreatedOn(new Date());
        hkRoughStockDetailDocument.setExchangeRate(roughStockDetailDataBean.getChangedExchangeRate());
//        HkParcelDocument hkParcelDocument = stockService.retrieveParcelById(new ObjectId(roughStockDetailDataBean.getParcel()));
        hkRoughStockDetailDocument.setParcel(new ObjectId(roughStockDetailDataBean.getParcel()));
        hkRoughStockDetailDocument.setPieces(roughStockDetailDataBean.getChangedPieces());
        hkRoughStockDetailDocument.setRate(roughStockDetailDataBean.getChangedRate());
//        hkRoughStockDetailDocument.setYear(hkParcelDocument.getYear());
        hkRoughStockDetailDocument.setEmpId(loginDataBean.getId());
        hkRoughStockDetailDocument.setDepId(loginDataBean.getDepartment());
        return hkRoughStockDetailDocument;
    }
}
