/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.center.sync.transformers;

import com.argusoft.hkg.model.HkMasterEntity;
import com.argusoft.hkg.sync.center.core.HkUMSyncService;
import com.argusoft.hkg.sync.xmpp.util.SyncHelper;
import com.argusoft.hkg.sync.xmpp.util.SyncTransferType;
import com.argusoft.hkg.sync.xmpp.util.SyncTransformerAdapter;
import com.argusoft.hkg.web.center.util.ApplicationUtil;
import com.argusoft.sync.center.model.HkMasterDocument;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author akta
 */
@Service
public class HkMasterTransformer extends SyncTransformerAdapter {
    
    @Autowired
    private HkUMSyncService hkUMSyncService;
    
    private final Map<String, List<String>> queryParametersMap;
    private final Map<String, Object> idMap;
    Gson gson;
    @Autowired
    private ApplicationUtil applicationUtil;
    
    public HkMasterTransformer() {
        this.idMap = new HashMap<>();
        this.queryParametersMap = new HashMap<>();
        gson = new GsonBuilder().setExclusionStrategies(new ExclusionStrategy() {
            
            @Override
            public boolean shouldSkipField(FieldAttributes arg0) {
                if (arg0.getName().equals("hkValueEntityList")) {
                    return true;
                }
                return false;
            }
            
            @Override
            public boolean shouldSkipClass(Class<?> arg0) {
                return false;
            }
        }).create();
    }
    
    @Override
    public void save(Object object) {
        if (object != null && object instanceof HkMasterDocument) {
            HkMasterDocument masterNew = (HkMasterDocument) object;
            HkMasterDocument masterExisting = (HkMasterDocument) hkUMSyncService.getDocumentById(masterNew.getCode(), HkMasterDocument.class);
            if (masterNew.getFranchise() != 0l && masterNew.getFranchise() != applicationUtil.getCenterFranchiseDataBean().getFranchiseId()) {
                masterNew.setIsArchive(true);
            }
            if (masterExisting != null && !isUpdatable(masterExisting.getLastModifiedOn(), masterNew.getLastModifiedOn())) {
                return;
            }
            hkUMSyncService.saveOrUpdateDocument(masterNew);
        }
    }
    
    @Override
    public Object convertEntityToDocument(Object entityObject) {
        HkMasterEntity entity = ((HkMasterEntity) entityObject);
        idMap.put("code", entity.getCode());
//        queryParametersMap.put(SyncHelper.FRANCHISE_ID, null);
        HkMasterDocument masterDocument = gson.fromJson(gson.toJson(entityObject), HkMasterDocument.class);
        //As franchise 0 is not passed for any case at the moment this feature has not been tested
        queryParametersMap.put(SyncHelper.FRANCHISE_ID, null);
        return masterDocument;
    }
    
    @Override
    public Map<String, List<String>> getQueryParameters() {
        return queryParametersMap;
    }
    
    @Override
    public int getSyncTransferType() {
        return SyncTransferType.ONE_TO_MANY;
    }
    
    @Override
    public Map<String, Object> getidMap() {
        return idMap;
    }
}
