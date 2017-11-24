/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.center.sync.transformers;

import com.argusoft.hkg.common.constantutil.HkSystemConstantUtil;
import com.argusoft.hkg.nosql.core.impl.HkCustomFieldServiceImpl;
import com.argusoft.hkg.nosql.model.GenericDocument;
import com.argusoft.hkg.nosql.model.HkSubFormValueDocument;
import com.argusoft.hkg.sync.center.core.HkUMSyncService;
import com.argusoft.hkg.sync.xmpp.util.SyncDocumentFieldMapper;
import com.argusoft.hkg.sync.xmpp.util.SyncHelper;
import com.argusoft.hkg.sync.xmpp.util.SyncTransformerAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.mongodb.util.JSON;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bson.BasicBSONObject;
import org.joda.time.DateTime;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 *
 * @author shruti
 */
@Service
@Scope("prototype")
public class HkGenericDocumentTransformer extends SyncTransformerAdapter {

    @Autowired
    HkUMSyncService hkUMSyncService;
    SyncDocumentFieldMapper.FieldInfo fieldInfo;
    private final Map<String, List<String>> queryParametersMap;
    private final Map<String, Object> idMap;
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(HkGenericDocumentTransformer.class);
    private static Gson gson;

    static {
        final JsonSerializer<Date> dateSerializer = (Date src, Type typeOfSrc, JsonSerializationContext context) -> src == null ? null : new JsonPrimitive(src.getTime());
        final JsonDeserializer<Date> dateDeserializer = (JsonElement json, Type typeOfT, JsonDeserializationContext context) -> {
            if (json != null) {
                try {
                    return new Date(json.getAsLong());
                } catch (NumberFormatException numberFormatException) {
                    return new Date(json.getAsString());
                }
            } else {
                return null;
            }
        };

        gson = new GsonBuilder().registerTypeAdapter(Date.class, dateSerializer)
                .registerTypeAdapter(Date.class, dateDeserializer).create();

    }

    public HkGenericDocumentTransformer() {
        this.idMap = new HashMap<>();
        this.queryParametersMap = new HashMap<>();
    }

    @Override
    public void save(Object object) {
        Class<?> documentClass = object.getClass();
        fieldInfo = SyncDocumentFieldMapper.getFieldInfo(documentClass);
        if (object != null) {
            try {
                if (object instanceof GenericDocument) {
                    GenericDocument document = (GenericDocument) object;
                    BasicBSONObject bsonObject = (BasicBSONObject) JSON.parse(document.getFieldValue().toMap().get("key").toString());
                    if (!documentClass.equals(HkSubFormValueDocument.class)) {
                        bsonObject = makeBSONObject(bsonObject.toMap());
                    }
//                    System.out.println("document.getFieldValue().toMap().get(\"key\")  " + document.getFieldValue().toMap().get("key"));
//                    BasicBSONObject bsonObject = makeBSONObject(((BasicBSONObject) document.getFieldValue().toMap().get("key")).toMap());
                    document.setFieldValue(bsonObject);
                }
                Method method = documentClass.getMethod(fieldInfo.getIdMethodName());
                LOGGER.debug(documentClass.getSimpleName() + ", methodname: " + fieldInfo.getIdMethodName() + " : " + method);
                method.setAccessible(true);
                Object id = method.invoke(object);
                Object currentDocument = hkUMSyncService.getDocumentById(id, documentClass);
                if (!StringUtils.isEmpty(fieldInfo.getModifiedOnFieldName())) {
                    Method modifiedOnMethod = documentClass.getMethod(fieldInfo.getModifiedOnFieldName());
                    modifiedOnMethod.setAccessible(true);
                    if (currentDocument != null && !isUpdatable((Date) modifiedOnMethod.invoke(currentDocument), (Date) modifiedOnMethod.invoke(object))) {
                        return;
                    }
                }
                hkUMSyncService.saveOrUpdateDocument(object);
            } catch (SecurityException | IllegalArgumentException | IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
                Logger.getLogger(HkGenericDocumentTransformer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

//    public void save(String jsonString, Class<?> class1) {
//        System.out.println("IN SAVE::::::");
////        Gson gson = new Gson();
//
//        try {
////            JsonObject jsonObject = gson.fromJson(jsonString, JsonObject.class);
////            BasicDBObject basicDBObject = (BasicDBObject) JSON.parse(jsonObject.get("fieldValue").toString());
////            Object object = gson.fromJson(jsonObject, class1);
////            ((GenericDocument) object).setFieldValue(basicDBObject);
//
//            save(gson.fromJson(jsonString, class1));
//        } catch (SecurityException ex) {
//            Logger.getLogger(HkGenericDocumentTransformer.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (IllegalArgumentException ex) {
//            Logger.getLogger(HkGenericDocumentTransformer.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
    @Override
    public Object convertEntityToDocument(Object entityObject
    ) {
        try {

            Class<?> documentClass = entityObject.getClass();
            if (entityObject instanceof GenericDocument) {
                GenericDocument document = (GenericDocument) entityObject;
                if (!document.getFieldValue().containsKey("key")) {
                    document.setFieldValue(new BasicBSONObject("key", document.getFieldValue().toString()));
                }
            }
            fieldInfo = SyncDocumentFieldMapper.getFieldInfo(documentClass);
            idMap.put("id", documentClass.getMethod(fieldInfo.getIdMethodName()).invoke(entityObject));
            if (!CollectionUtils.isEmpty(fieldInfo.getFranchiseIdMethodName())) {
                for (String methodName : fieldInfo.getFranchiseIdMethodName()) {
                    Method method = documentClass.getMethod(methodName);
                    method.setAccessible(true);
                    Object invoke = method.invoke(entityObject);
//                    LOGGER.debug(invoke + " method.invoke(entityObject).toString() " + invoke.toString());
                   
//                if (documentClass.equals(HkSubFormValueDocument.class)) {
//                    queryParametersMap.put(SyncHelper.FRANCHISE_ID, null);
//                } else {
                    if (invoke != null) {
                        queryParametersMap.put(SyncHelper.FRANCHISE_ID, Arrays.asList(invoke.toString()));
                    }
                }

//                }
            }

            return entityObject;
        } catch (SecurityException | IllegalArgumentException | IllegalAccessException | NoSuchMethodException | InvocationTargetException ex) {
            Logger.getLogger(HkGenericDocumentTransformer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public Map<String, List<String>> getQueryParameters() {
        return Collections.unmodifiableMap(queryParametersMap);
    }

    @Override
    public int getSyncTransferType() {
        return fieldInfo.getSyncTrasferType();
    }

    @Override
    public Map<String, Object> getidMap() {
        return Collections.unmodifiableMap(idMap);
    }

    @Override
    public int getSyncTransferType(Class<?> class1) {
        if (fieldInfo == null) {
            fieldInfo = SyncDocumentFieldMapper.getFieldInfo(class1);
        }
        return fieldInfo.getSyncTrasferType();
    }

    public BasicBSONObject makeBSONObject(Map<String, Object> val) {
        if (!CollectionUtils.isEmpty(val)) {
//            List<String> uiFieldList = new ArrayList<>();
//            for (Map.Entry<String, String> entrySet : dbTypeMap.entrySet()) {
//                uiFieldList.add(entrySet.getKey());
//
//            }

            BasicBSONObject basicBSONObject = new BasicBSONObject();

            for (Map.Entry<String, Object> entry : val.entrySet()) {
                String colName = entry.getKey();
                Object value = entry.getValue();
                String[] split = colName.split("\\$");
//                LOGGER.debug(colName + "  split::::::::::::: " + split.length);
                String dbType = "";
                String componentType = "";
                if (split.length >= 3) {
                    componentType = split[1];
                    dbType = split[2];
                }

                if (colName.contains(HkSystemConstantUtil.CURRENCY_CODE_CUSTOM)) {
                    dbType = "String";
                }
//                String pointerComponentType = HkSystemConstantUtil.CustomFieldComponentType.POINTER + "@*" + HkSystemConstantUtil.CustomFieldComponentType.MULTISELECT_DROPDOWN;

//                if (componentType.equals(HkSystemConstantUtil.COMPONENT_CODE.get(HkSystemConstantUtil.CustomFieldComponentType.MULTISELECT_DROPDOWN)) || componentType.equals(HkSystemConstantUtil.COMPONENT_CODE.get(HkSystemConstantUtil.CustomFieldComponentType.USER_MULTISELECT))) {
//                    if (dbTypeMap.containsKey(uiField.getKey())) {
//                        dbTypeMap.put(uiField.getKey(), HkSystemConstantUtil.CustomField.DbFieldType.ARRAY);
//                    }
//
//                    if (val.containsKey(uiField.getKey())) {
//                        String value = val.get(uiField.getKey()).toString();
//                        List<String> values = new ArrayList<>();
//                        String[] valueArray = value.replace("\"", "").split(",");
//                        for (String v : valueArray) {
//                            values.add(v.replace("\"", ""));
//                        }
//
//                        val.put(uiField.getKey(), values);
//                    }
//                }
                //System.out.println("Col namew:::::::::" + colName + "---------" + "dbtype" + dbType);
                if (dbType != null && value != null && !"".equals(value.toString())) {
                    switch (dbType) {
                        case HkSystemConstantUtil.CustomField.DbFieldType.DATE:
//                            if (value instanceof Integer) {
                            //                            DateTime dateTime = new DateTime(value.toString());
                            basicBSONObject.put(colName, value);
//                            } else {
//                                SimpleDateFormat sdf = new SimpleDateFormat(HkSystemConstantUtil.CUSTOM_FIELD_DATE_FORMAT);
//                                try {
//                                    basicBSONObject.put(colName, sdf.parse(value.toString()));
//                                } catch (ParseException ex) {
//                                    Logger.getLogger(HkCustomFieldServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
//                                }
//                            }
                            break;
                        case HkSystemConstantUtil.CustomField.DbFieldType.DATE_TIME:
                            if (value instanceof Integer) {
//                                dateTime = new DateTime(value.toString());
                                basicBSONObject.put(colName, new DateTime(value));
                            } else {
                                SimpleDateFormat sdf = new SimpleDateFormat(HkSystemConstantUtil.CUSTOM_FIELD_DATE_TIME_FORMAT);
                                try {
                                    basicBSONObject.put(colName, sdf.parse(value.toString()));
                                } catch (ParseException ex) {
                                    Logger.getLogger(HkCustomFieldServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                            break;
                        case HkSystemConstantUtil.CustomField.DbFieldType.TIME:
                            if (value instanceof Integer) {
                                basicBSONObject.put(colName, new DateTime(value.toString()).toDate());
                            } else {
//                                SimpleDateFormat sdf = new SimpleDateFormat(HkSystemConstantUtil.CUSTOM_FIELD_TIME_FORMAT);
//                                try {
                                basicBSONObject.put(colName, ((Date) value).getTime());
//                                } catch (ParseException ex) {
//                                    Logger.getLogger(HkCustomFieldServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
//                                }
                            }
                            break;
                        case HkSystemConstantUtil.CustomField.DbFieldType.DOUBLE:
                            basicBSONObject.put(colName, Double.parseDouble(value.toString()));
                            break;
                        case HkSystemConstantUtil.CustomField.DbFieldType.INTEGER:
                            basicBSONObject.put(colName, Integer.parseInt(value.toString()));
                            break;
                        case HkSystemConstantUtil.CustomField.DbFieldType.LONG:
                            basicBSONObject.put(colName, Long.parseLong(value.toString()));
                            break;
//                        case HkSystemConstantUtil.CustomField.DbFieldType.STRING:
//                            basicBSONObject.put(colName, value.toString());
//                            break;
//                        case HkSystemConstantUtil.CustomField.DbFieldType.STRING_ARRAY:
//                            List<String> Array = new ArrayList<>();
//                            Array.add(value.toString());
//                            basicBSONObject.put(colName, (ArrayList) value);
//                            break;
//                        case HkSystemConstantUtil.CustomField.DbFieldType.ARRAY:
//                            basicBSONObject.put(colName, (ArrayList<Long>) value);
//                            break;
//                        case HkSystemConstantUtil.CustomField.DbFieldType.IMAGE:
//                            String imageName;
//                            if (value instanceof String) {
//                                imageName = value.toString();
//                            } else {
//                                ArrayList imageArray = (ArrayList) value;
//                                imageName = imageArray.get(0).toString();
//                            }
//                            String oldImageName = imageName;
//                            basicBSONObject.put(colName, imageName);
//                            break;
                        case HkSystemConstantUtil.CustomField.DbFieldType.FILE:
                            ArrayList fileNameArray = (ArrayList) value;
                            basicBSONObject.put(colName, fileNameArray);
                            break;
                        case HkSystemConstantUtil.CustomField.DbFieldType.BOOLEAN:
                            basicBSONObject.put(colName, Boolean.parseBoolean(value.toString()));
                            break;
                        case HkSystemConstantUtil.CustomField.DbFieldType.OBJECT_ID:
                            basicBSONObject.put(colName, value.toString());
                            break;
                        default:
                            basicBSONObject.put(colName, value);
                    }
                }
            }
//            fieldValues.putAll(basicBSONObject.toMap());
//                    System.out.println("field value size after ::: " + fieldValues.size());

            return basicBSONObject;

        }
//                    System.out.println("Basic bson size ::: " + basicBSONObject.toMap().size());
//                    System.out.println("field value size before ::: " + fieldValues.size());

        return null;
    }
}
