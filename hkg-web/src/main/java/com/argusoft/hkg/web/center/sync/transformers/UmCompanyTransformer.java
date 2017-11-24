/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.center.sync.transformers;

import com.argusoft.hkg.sync.center.core.HkUMSyncService;
import com.argusoft.hkg.sync.xmpp.util.SyncTransferType;
import com.argusoft.hkg.sync.xmpp.util.SyncTransformerAdapter;
import com.argusoft.sync.center.model.UmCompanyDocument;
import com.argusoft.usermanagement.common.model.UMCompany;
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
public class UmCompanyTransformer extends SyncTransformerAdapter {
    
    @Autowired
    private HkUMSyncService hkUMSyncService;
    
    private final Map<String, Object> idMap;
    
    public UmCompanyTransformer() {
        this.idMap = new HashMap<>();
    }
    
    @Override
    public void save(Object object) {
        if (object != null && object instanceof UmCompanyDocument) {
            UmCompanyDocument document = (UmCompanyDocument) object;
            UmCompanyDocument currentDocument = (UmCompanyDocument) hkUMSyncService.getDocumentById(document.getId(), UmCompanyDocument.class);
            if (currentDocument != null && !isUpdatable(currentDocument.getLastModifiedOn(), document.getLastModifiedOn())) {
                return;
            }
            
            hkUMSyncService.saveOrUpdateDocument(document);
        }
        
    }
    
    @Override
    public Object convertEntityToDocument(Object entityObject) {
        UmCompanyDocument documentObject = new UmCompanyDocument();
        if (entityObject instanceof UMCompany) {
            UMCompany comapny = (UMCompany) entityObject;
            idMap.put("id", comapny.getId());
            documentObject.setId(comapny.getId());
            documentObject.setIsArchive(comapny.getIsArchive());
            documentObject.setLastModifiedOn(comapny.getModifiedOn());
            documentObject.setName(comapny.getName());
            documentObject.setCompanyCode(comapny.getCompanyCode());
        }
        return documentObject;
    }
    
    @Override
    public Map<String, List<String>> getQueryParameters() {
        return null;
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
