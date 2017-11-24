/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.center.sync.transformers;

import com.argusoft.hkg.model.HkFieldEntity;
import com.argusoft.hkg.sync.center.core.HkUMSyncService;
import com.argusoft.hkg.sync.xmpp.util.HkSyncConstantUtil;
import com.argusoft.hkg.sync.xmpp.util.SyncHelper;
import com.argusoft.hkg.sync.xmpp.util.SyncTransferType;
import com.argusoft.hkg.sync.xmpp.util.SyncTransformerAdapter;
import com.argusoft.hkg.web.config.WebApplicationInitializerConfig;
import com.argusoft.sync.center.model.HkFieldDocument;
import com.argusoft.sync.center.model.HkSectionDocument;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author akta
 */
@Service
public class HkFieldTransformer extends SyncTransformerAdapter {

    private final Map<String, List<String>> queryParametersMap;
    private final Map<String, Object> idMap;

    @Autowired
    private HkUMSyncService hkUMSyncService;
    Gson gson;

    public HkFieldTransformer() {
        this.idMap = new HashMap<>();
        this.queryParametersMap = new HashMap<>();
        gson = new GsonBuilder().setExclusionStrategies(new ExclusionStrategy() {

            @Override
            public boolean shouldSkipField(FieldAttributes arg0) {
                return false;
            }

            @Override
            public boolean shouldSkipClass(Class<?> arg0) {
                if (arg0.equals(Set.class)) {
                    return true;
                }
                return false;
            }
        }).create();
    }

    @Override
    public void save(Object object) {
        if (object != null && object instanceof HkFieldDocument) {
            HkFieldDocument hkFieldDocNew = (HkFieldDocument) object;
            HkFieldDocument hkFieldDocExisting = (HkFieldDocument) hkUMSyncService.getDocumentById(hkFieldDocNew.getId(), HkFieldDocument.class);
            if (hkFieldDocExisting != null && !isUpdatable(hkFieldDocExisting.getLastModifiedOn(), hkFieldDocNew.getLastModifiedOn())) {
                return;
            }
            if (!WebApplicationInitializerConfig.IS_MASTER) {
                hkUMSyncService.saveOrUpdateDocumentTransactional(hkFieldDocNew);
            }else{
                hkUMSyncService.saveOrUpdateDocument(hkFieldDocNew);  
            }
        }
    }

    @Override
    public Object convertEntityToDocument(Object entityObject) {
        HkFieldDocument hkFieldDocument = new HkFieldDocument();
        idMap.put("id", ((HkFieldEntity) entityObject).getId());
        Long franchise = ((HkFieldEntity) entityObject).getFranchise();
        if (franchise.equals(0l)) {
            queryParametersMap.put(SyncHelper.FRANCHISE_ID, null);
        } else {
            queryParametersMap.put(SyncHelper.FRANCHISE_ID, Arrays.asList(String.valueOf(franchise)));
        }
        if (HkSyncConstantUtil.ENTITY_FIELD_MAP.containsKey(entityObject.getClass())) {
            hkFieldDocument = (HkFieldDocument) super.convertEntityToDocument(entityObject, HkFieldDocument.class, hkFieldDocument);
            if (((HkFieldEntity) entityObject).getSection() != null) {
                HkSectionDocument hkSectionDocument = new HkSectionDocument();
                hkSectionDocument.setId(((HkFieldEntity) entityObject).getSection().getId());
                hkFieldDocument.setSection(hkSectionDocument);
            }
        } else {
            hkFieldDocument = (HkFieldDocument) entityObject;
        }

        return hkFieldDocument;
    }

    @Override
    public Map<String, List<String>> getQueryParameters() {
        return Collections.unmodifiableMap(queryParametersMap);
    }

    @Override
    public int getSyncTransferType() {
        return SyncTransferType.ONE_TO_MANY;
    }

    @Override
    public Map<String, Object> getidMap() {
        return Collections.unmodifiableMap(idMap);
    }

}
