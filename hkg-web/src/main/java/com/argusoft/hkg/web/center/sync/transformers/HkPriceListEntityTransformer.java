/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.center.sync.transformers;

import com.argusoft.hkg.model.HkPriceListDetailEntity;
import com.argusoft.hkg.model.HkPriceListEntity;
import com.argusoft.hkg.nosql.core.HkStockService;
import com.argusoft.hkg.sync.center.core.HkUMSyncService;
import com.argusoft.hkg.sync.xmpp.util.SyncHelper;
import com.argusoft.hkg.sync.xmpp.util.SyncTransferType;
import com.argusoft.hkg.sync.xmpp.util.SyncTransformerAdapter;
import com.argusoft.sync.center.model.HkPriceListDetailDocument;
import com.argusoft.sync.center.model.HkPriceListDocument;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 *
 * @author harshit
 */
@Service
public class HkPriceListEntityTransformer extends SyncTransformerAdapter {

    @Autowired
    private HkUMSyncService hkUMSyncService;
    @Autowired
    private HkStockService hkStockService;
    private final Map<String, List<String>> queryParametersMap;
    private final Map<String, Object> idMap;

    Gson gson;

    public HkPriceListEntityTransformer() {
        this.idMap = new HashMap<>();
        this.queryParametersMap = new HashMap<>();

        gson = new GsonBuilder().setExclusionStrategies(new ExclusionStrategy() {

            @Override
            public boolean shouldSkipField(FieldAttributes arg0) {
                if (arg0.getName().equals("hkPriceListEntity")) {
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

//        if (object != null && object instanceof HkPriceListDocument) {
//            HkPriceListDocument document = (HkPriceListDocument) object;
//            HkPriceListDocument currentDocument = (HkPriceListDocument) hkUMSyncService.getDocumentById(document.getId(), HkPriceListDocument.class);
//            if (currentDocument != null && !isUpdatable(currentDocument.getLastModifiedOn(), document.getLastModifiedOn())) {
//                return;
//            }
//            hkStockService.reCaluclatePriceForPlannedPlans(document, document.getFranchise(), document.getUploadedBy());
//            hkUMSyncService.saveOrUpdateDocument(document);
//        }
    }

    @Override
    public Object convertEntityToDocument(Object entityObject) {

        HkPriceListEntity entity = ((HkPriceListEntity) entityObject);
        idMap.put("id", entity.getId());
        HkPriceListDocument hkPriceListDocument = gson.fromJson(gson.toJson(entity), HkPriceListDocument.class);
        hkPriceListDocument.setLastModifiedOn(new Date());
        if (!CollectionUtils.isEmpty(entity.getHkPriceListDetailEntityCollection())) {
            Collection<HkPriceListDetailEntity> hkPriceListDetailEntityCollection = entity.getHkPriceListDetailEntityCollection();
            List<HkPriceListDetailDocument> priceListDetailDocs = new LinkedList<>();
            for (HkPriceListDetailEntity priceListDetailEntity : hkPriceListDetailEntityCollection) {
                HkPriceListDetailDocument priceListDetailDocument = gson.fromJson(gson.toJson(priceListDetailEntity), HkPriceListDetailDocument.class);
                priceListDetailDocument.setId(priceListDetailEntity.getHkPriceListDetailEntityPK().getPriceList() + "_" + priceListDetailEntity.getHkPriceListDetailEntityPK().getSequenceNumber());
                priceListDetailDocs.add(priceListDetailDocument);
            }
            hkPriceListDocument.setHkPriceListDetailEntityCollection(priceListDetailDocs);
        }
        if (entity.getFranchise() == 0l) {
            queryParametersMap.put(SyncHelper.FRANCHISE_ID, null);
        } else {
            queryParametersMap.put(SyncHelper.FRANCHISE_ID, Arrays.asList(hkPriceListDocument.getFranchise().toString()));
        }
        return hkPriceListDocument;
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
