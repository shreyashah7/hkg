/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.center.sync.transformers;

import com.argusoft.hkg.nosql.model.HkFeatureFieldPermissionDocument;
import com.argusoft.hkg.sync.center.core.HkUMSyncService;
import com.argusoft.hkg.sync.xmpp.util.SyncHelper;
import com.argusoft.hkg.sync.xmpp.util.SyncTransferType;
import com.argusoft.hkg.sync.xmpp.util.SyncTransformerAdapter;
import com.argusoft.sync.center.model.HkFieldDocument;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author shruti
 */
@Service
public class HkFeatureFieldPermissionTransformer extends SyncTransformerAdapter {

    @Autowired
    HkUMSyncService hkUMSyncService;

    private final Map<String, List<String>> queryParametersMap;
    private final Map<String, Object> idMap;
    private Gson gson;

    public HkFeatureFieldPermissionTransformer() {
        this.queryParametersMap = new HashMap<>();
        this.idMap = new HashMap<>();
        GsonBuilder gsonBuilder = new GsonBuilder();
        final JsonDeserializer<Number> numberDeserializer = (JsonElement json, Type typeOfT, JsonDeserializationContext context) -> {
            if (json != null) {
                if (json.getAsString().contains(".")) {
                    return json.getAsDouble();
                } else {
                    return json.getAsLong();
                }
            } else {
                return null;
            }
        };
        gsonBuilder.registerTypeAdapter(Number.class, numberDeserializer);

        gson = gsonBuilder.create();
//          final JsonSerializer<Object> dateSerializer = (Date src, Type typeOfSrc, JsonSerializationContext context) -> src == null ? null : new JsonPrimitive(src.getTime());
    }

    @Override
    public void save(Object object) {
        if (object != null && object instanceof HkFeatureFieldPermissionDocument) {
            HkFeatureFieldPermissionDocument document = (HkFeatureFieldPermissionDocument) object;
            HkFeatureFieldPermissionDocument currentDocument = (HkFeatureFieldPermissionDocument) hkUMSyncService.getDocumentById(document.getId(), HkFeatureFieldPermissionDocument.class);
            if (currentDocument != null && !isUpdatable(currentDocument.getLastModifiedOn(), document.getLastModifiedOn())) {
                return;
            }
            hkUMSyncService.saveOrUpdateDocument(document);
        }

    }

    @Override
    public Object convertEntityToDocument(Object entityObject) {
        HkFeatureFieldPermissionDocument document = null;
        if (entityObject != null && entityObject instanceof HkFeatureFieldPermissionDocument) {
            document = (HkFeatureFieldPermissionDocument) entityObject;
            document.setHkFieldEntity((HkFieldDocument) hkUMSyncService.getDocumentById(document.getFieldId(), HkFieldDocument.class));
            idMap.put("id", document.getId());
            queryParametersMap.put(SyncHelper.FRANCHISE_ID, null);
        }
        return document;
    }

    @Override
    public Map<String, List<String>> getQueryParameters() {
        return queryParametersMap;
    }

    @Override
    public int getSyncTransferType() {
        return SyncTransferType.SEND_TO_ALL;
    }

    @Override
    public Map<String, Object> getidMap() {
        return idMap;
    }
}
