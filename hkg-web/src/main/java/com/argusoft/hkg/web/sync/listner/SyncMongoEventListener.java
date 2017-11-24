/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.sync.listner;

import com.argusoft.hkg.sync.center.core.HkUMSyncService;
import com.argusoft.hkg.sync.xmpp.util.SyncEntityDocumentMapper;
import com.argusoft.hkg.web.sync.mongotransaction.SyncMongoTransactionAspect;
import com.argusoft.sync.center.model.AuditTrail;
import com.argusoft.sync.center.model.MetadataDocument;
import com.argusoft.sync.center.model.SyncTransactionLogDocument;
import com.google.gson.Gson;
import com.mongodb.DBObject;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.MongoMappingEvent;

/**
 *
 * @author brijesh
 */
public class SyncMongoEventListener extends AbstractMongoEventListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(SyncMongoEventListener.class);
    private Gson gson = new Gson();
    @Autowired
    private SyncEntityDocumentMapper entityDocumentMapper;

    @Autowired
    private HkUMSyncService uMSyncService;

//    @Autowired
//    private LoginDataBean loginDataBean;
    @Override
    public void onBeforeDelete(DBObject dbo) {
        super.onBeforeDelete(dbo); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onAfterDelete(DBObject dbo) {
        super.onAfterDelete(dbo); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onAfterConvert(DBObject dbo, Object source) {
        super.onAfterConvert(dbo, source); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onAfterLoad(DBObject dbo) {
//        LOGGER.debug("On after Load.....");
        super.onAfterLoad(dbo); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onAfterSave(Object source, DBObject dbo) {
//            LOGGER.debug("--onAfterSave:--" + source.getClass().getName());

//            List<SyncTransformerInterface> transformerInterfaceList = entityDocumentMapper.getTransformerInstance(source.getClass());
        if (!source.getClass().equals(AuditTrail.class) && !source.getClass().equals(SyncTransactionLogDocument.class) && !source.getClass().equals(MetadataDocument.class)) {
            if (entityDocumentMapper.containsEntityMapping(source.getClass())) {
                // LOGGER.debug("--transformerInterfaceList != null---");
                if (SyncTransactionLog.getPerTransactionRecordedEntities() != null && SyncMongoTransactionAspect.TransactionContext.get() != null && SyncTransactionLog.getPerTransactionRecordedEntities().containsKey((Long) SyncMongoTransactionAspect.TransactionContext.get())) {
                    SyncRecordedEntity hkRecordedEntity = new SyncRecordedEntity();
                    hkRecordedEntity.setEntity(source);
                    hkRecordedEntity.setEntityName(source.getClass().getSimpleName());
                    SyncTransactionLog.getPerTransactionRecordedEntities().get((Long) SyncMongoTransactionAspect.TransactionContext.get()).add(hkRecordedEntity);
//                    LOGGER.debug("--onAfterSave: entity added--");
                } else {
                    LOGGER.debug("--onAfterSave:----transaction id not found in catalog------");

                }
            }
            super.onAfterSave(source, dbo); //To change body of generated methods, choose Tools | Templates.
            uMSyncService.saveOrUpdateDocument(new AuditTrail(gson.toJson(source), new Date(), source.getClass().getName(), false));
        }
    }

    @Override
    public void onBeforeSave(Object source, DBObject dbo
    ) {
//        LOGGER.debug("On before save called....." + source + "-------dbo-----" + dbo);
        super.onBeforeSave(source, dbo); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onBeforeConvert(Object source) {
        super.onBeforeConvert(source); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onApplicationEvent(MongoMappingEvent event
    ) {
        super.onApplicationEvent(event); //To change body of generated methods, choose Tools | Templates.
    }

}
