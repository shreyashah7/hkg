/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.center.sync.transformers;

import com.argusoft.hkg.model.HkHolidayEntity;
import com.argusoft.hkg.sync.center.core.HkUMSyncService;
import com.argusoft.hkg.sync.xmpp.util.SyncHelper;
import com.argusoft.hkg.sync.xmpp.util.SyncTransferType;
import com.argusoft.hkg.sync.xmpp.util.SyncTransformerAdapter;
import com.argusoft.hkg.web.center.util.ApplicationUtil;
import com.argusoft.sync.center.model.HkHolidayDocument;
import com.google.gson.Gson;
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
public class HkHolidayTransformer extends SyncTransformerAdapter {

    @Autowired
    private HkUMSyncService hkUMSyncService;

    private final Map<String, List<String>> queryParametersMap;
    private final Map<String, Object> idMap;
    Gson gson;
    @Autowired
    private ApplicationUtil applicationUtil;

    public HkHolidayTransformer() {
        this.idMap = new HashMap<>();
        this.queryParametersMap = new HashMap<>();
        gson = new Gson();
    }

    @Override
    public void save(Object object) {
        if (object != null && object instanceof HkHolidayDocument) {
            HkHolidayDocument holidaynew = (HkHolidayDocument) object;
            HkHolidayDocument holidayExisting = (HkHolidayDocument) hkUMSyncService.getDocumentById(holidaynew.getId(), HkHolidayDocument.class);
            
            if (holidayExisting != null && !isUpdatable(holidayExisting.getLastModifiedOn(), holidaynew.getLastModifiedOn())) {
                return;
            }
            hkUMSyncService.saveOrUpdateDocument(holidaynew);
        }
    }

    @Override
    public Object convertEntityToDocument(Object entityObject) {
        HkHolidayEntity entity = ((HkHolidayEntity) entityObject);
        idMap.put("id", entity.getId());
        queryParametersMap.put(SyncHelper.FRANCHISE_ID, null);
        HkHolidayDocument masterDocument = gson.fromJson(gson.toJson(entityObject), HkHolidayDocument.class);
        //As franchise 0 is not passed for any case at the moment this feature has not been tested
//        queryParametersMap.put(SyncHelper.FRANCHISE_ID, null);
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
