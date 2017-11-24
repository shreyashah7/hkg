/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.center.sync.transformers;

import com.argusoft.hkg.model.HkPriceListDetailEntity;
import com.argusoft.hkg.sync.center.core.HkUMSyncService;
import com.argusoft.hkg.sync.xmpp.util.HkSyncConstantUtil;
import com.argusoft.hkg.sync.xmpp.util.SyncHelper;
import com.argusoft.hkg.sync.xmpp.util.SyncTransferType;
import com.argusoft.hkg.sync.xmpp.util.SyncTransformerAdapter;
import com.argusoft.sync.center.model.HkPriceListDetailDocument;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author harshit
 */
@Service
public class HkPriceListDetailEntityTransformer extends SyncTransformerAdapter {

    @Autowired
    private HkUMSyncService hkUMSyncService;
    @Autowired
    HkPriceListEntityTransformer hkPriceListEntityTransformer;
    private final Map<String, List<String>> queryParametersMap;
    private final Map<String, Object> idMap;

    public HkPriceListDetailEntityTransformer() {
        this.queryParametersMap = new HashMap<>();
        this.idMap = new HashMap<>();
    }

    @Override
    public void save(Object object) {
        if (object != null && object instanceof HkPriceListDetailDocument) {
            HkPriceListDetailDocument document = (HkPriceListDetailDocument) object;
            HkPriceListDetailDocument currentDocument = (HkPriceListDetailDocument) hkUMSyncService.getDocumentById(document.getId(), HkPriceListDetailDocument.class);
            if (currentDocument != null && !isUpdatable(currentDocument.getLastModifiedOn(), document.getLastModifiedOn())) {
                return;
            }
            hkUMSyncService.saveOrUpdateDocument(document);
        }

    }

    @Override
    public Object convertEntityToDocument(Object entityObject) {
        HkPriceListDetailDocument hkPriceListDetailDocument = new HkPriceListDetailDocument();
        HkPriceListDetailEntity hkPriceListDetailEntity = (HkPriceListDetailEntity) entityObject;
        idMap.put(SyncHelper.encodeMapKeyWithDot("hkPriceListDetailEntityPK.priceList"), hkPriceListDetailEntity.getHkPriceListDetailEntityPK().getPriceList());
        idMap.put(SyncHelper.encodeMapKeyWithDot("hkPriceListDetailEntityPK.sequenceNumber"), hkPriceListDetailEntity.getHkPriceListDetailEntityPK().getSequenceNumber());
        if (HkSyncConstantUtil.ENTITY_FIELD_MAP.containsKey(entityObject.getClass())) {
            hkPriceListDetailDocument = (HkPriceListDetailDocument) super.convertEntityToDocument(entityObject, HkPriceListDetailDocument.class, hkPriceListDetailDocument);
            hkPriceListDetailDocument = (HkPriceListDetailDocument) super.convertEntityToDocument(((HkPriceListDetailEntity) entityObject).getHkPriceListDetailEntityPK(), HkPriceListDetailDocument.class, hkPriceListDetailDocument);
            if (hkPriceListDetailEntity.getHkPriceListEntity() != null) {
                if (hkPriceListDetailEntity.getHkPriceListEntity().getFranchise() == 0l) {
                    queryParametersMap.put(SyncHelper.FRANCHISE_ID, null);
                } else {
                    queryParametersMap.put(SyncHelper.FRANCHISE_ID, Arrays.asList(String.valueOf(hkPriceListDetailEntity.getHkPriceListEntity().getFranchise())));
                }
            }

            hkPriceListDetailDocument.setLastModifiedOn(new Date());
            hkPriceListDetailDocument.setId(hkPriceListDetailEntity.getHkPriceListDetailEntityPK().getPriceList() + "-" + hkPriceListDetailEntity.getHkPriceListDetailEntityPK().getSequenceNumber());

        }
        return hkPriceListDetailDocument;
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
