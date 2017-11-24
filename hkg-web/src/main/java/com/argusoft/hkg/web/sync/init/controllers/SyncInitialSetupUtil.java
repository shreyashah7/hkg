/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.sync.init.controllers;

import com.argusoft.generic.database.common.CommonDAO;
import com.argusoft.generic.database.common.MongoGenericDao;
import com.argusoft.generic.database.search.SearchFactory;
import com.argusoft.hkg.common.constantutil.HkSystemConstantUtil;
import com.argusoft.hkg.core.HkFoundationService;
import com.argusoft.hkg.model.HkSystemConfigurationEntity;
import com.argusoft.hkg.nosql.model.HkInvoiceDocument;
import com.argusoft.hkg.nosql.model.HkIssueDocument;
import com.argusoft.hkg.nosql.model.HkLotDocument;
import com.argusoft.hkg.nosql.model.HkPacketDocument;
import com.argusoft.hkg.nosql.model.HkParcelDocument;
import com.argusoft.hkg.nosql.model.HkSubFormValueDocument;
import com.argusoft.hkg.sync.xmpp.util.SyncEntityDocumentMapper;
import com.argusoft.hkg.sync.xmpp.util.SyncTransferType;
import com.argusoft.hkg.sync.xmpp.util.SyncTransformerInterface;
import com.argusoft.sync.center.model.MetadataDocument;
import com.google.gson.Gson;
import com.googlecode.genericdao.search.Filter;
import com.googlecode.genericdao.search.Search;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import javax.annotation.PostConstruct;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author shruti
 */
@Service
@Transactional
public class SyncInitialSetupUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(SyncInitialSetupUtil.class);
    @Autowired
    private CommonDAO commonDAO;
    @Autowired
    private MongoGenericDao mongoGenericDao;
    @Autowired
    private SyncEntityDocumentMapper entityDocumentMapper;

    @Autowired
    private HkFoundationService foundationService;
    private Set<ObjectId> lotIds;
    private Set<ObjectId> packetIds;
    private Set<ObjectId> issueIds;
    private Gson gson = new Gson();

    public SyncInitialSetupUtil() {
        this.lotIds = new HashSet<>();
        this.issueIds = new HashSet<>();
        this.packetIds = new HashSet<>();
    }

    @PostConstruct
    public void init() {
//        System.out.println("$$$umUserTransformer.retrieveAvatar(2l) " + umUserTransformer.retrieveAvatar(2l));
//        UMUser umUser = new UMUser(3l);
//        umUser.setCompany(2);
//        System.out.println("$$$umUserTransformer.retrieveAvatar(2l) " + umUserTransformer.getProfileFullPath(3l));
    }

    public void clearValues() {
        this.lotIds = new HashSet<>();
        this.issueIds = new HashSet<>();
        this.packetIds = new HashSet<>();
    }

    public List<Object> retrieveEntityByCompany(String isArchieveFieldName, String companyFieldName, Long value, Class<?> entityClass) {
        Search search = new Search(entityClass);
        LOGGER.debug("isArchieveFieldName  " + isArchieveFieldName);
        LOGGER.debug("companyFieldName  " + companyFieldName + " " + value + entityClass.getName());
        if (isArchieveFieldName != null) {
            search.addFilterEqual(isArchieveFieldName, Boolean.FALSE);
        }
        if (companyFieldName != null) {
            Set<Long> companies = new HashSet<>();
            companies.add(0l);
            companies.add(value);
            search.addFilterIn(companyFieldName, companies);
        }
        List search1 = commonDAO.search(search);
        if (value != null && value != 1l) {
            if (entityClass.equals(HkSystemConfigurationEntity.class)) {
                search1.add(foundationService.retrieveSystemConfigurationByKey(HkSystemConstantUtil.FranchiseConfiguration.DEFAULT_XMPP_EMAIL_ADDRESS, 1l));
            }
        }
        return search1;
    }

    public List<Object> retrieveAllActiveEntity(String isArchieveFieldName, Class<?> entityClass) {
        Search search = new Search(entityClass);
        LOGGER.debug(entityClass.getName() + "isArchieveFieldName  " + isArchieveFieldName);
        if (isArchieveFieldName != null) {
            search.addFilterEqual(isArchieveFieldName, Boolean.FALSE);
        }

        List search1 = commonDAO.search(search);
        LOGGER.debug("output: " + search1);
        return search1;
    }

    public List<Object> retrieveDocumentByCompany(String isArchieveFieldName, String companyFieldName, Long value, Class<?> entityClass) {
        List<Criteria> criterias = new LinkedList<>();
        if (!entityClass.equals(HkSubFormValueDocument.class)) {
            Set<Long> companies = new HashSet<>();
            companies.add(0l);
            companies.add(value);
            criterias.add(Criteria.where(companyFieldName).in(companies));
        }
        if (isArchieveFieldName != null) {
            criterias.add(Criteria.where(isArchieveFieldName).is(false));
        }
        if (entityClass.equals(HkInvoiceDocument.class) || entityClass.equals(HkLotDocument.class) || entityClass.equals(HkParcelDocument.class)) {
            criterias.add(where("status").ne(HkSystemConstantUtil.StockStatus.FINISHED));
        } else if (entityClass.equals(HkIssueDocument.class)) {
            criterias.add(where("id").in(issueIds));
        }
        LOGGER.debug("isArchieveFieldName  " + isArchieveFieldName);
        LOGGER.debug("companyFieldName  " + companyFieldName + " " + value + entityClass.getName());
        List findByCriteria = mongoGenericDao.findByCriteria(criterias, entityClass);
//        lotIds = new HashSet<>();
        if (entityClass.equals(HkLotDocument.class) && findByCriteria != null) {
            clearValues();
            HkLotDocument document = null;
            for (Object findByCriteria1 : findByCriteria) {
                document = (HkLotDocument) findByCriteria1;
                lotIds.add(document.getId());
                if (document.getIssueDocument() != null) {
                    issueIds.add(document.getIssueDocument().getId());
                } else if (document.getEodIssueDocument() != null) {
                    issueIds.add(document.getEodIssueDocument().getId());
                }
            }
        }
        if (entityClass.equals(HkPacketDocument.class) && findByCriteria != null) {
            HkPacketDocument document = null;
//            packetIds = new HashSet<>();
            for (Object findByCriteria1 : findByCriteria) {
                document = (HkPacketDocument) findByCriteria1;
                packetIds.add(document.getId());
                if (document.getIssueDocument() != null) {
                    issueIds.add(document.getIssueDocument().getId());
                } else if (document.getEodIssueDocument() != null) {
                    issueIds.add(document.getEodIssueDocument().getId());
                }
            }
        }
        return findByCriteria;
    }

    public List<String> retrieveWorkAllotment(Long companyId, Class<?> entityClass) throws InstantiationException, IllegalAccessException {
        List<String> stringDocuments = null;
        List<SyncTransformerInterface> transformerInstances = entityDocumentMapper.getTransformerInstance(entityClass);
        MetadataDocument metadataDocument = null;
        if (transformerInstances != null) {

            List<? extends Object> entities = null;
            List<Criteria> criterias = new LinkedList<>();
            criterias.add(Criteria.where("franchise").is(companyId));

            criterias.add(new Criteria().orOperator((where("unitType").is("L").and("unitInstance").in(lotIds)), where("unitType").is("P").and("unitInstance").in(packetIds)));
            List findByCriteria = mongoGenericDao.findByCriteria(criterias, entityClass);
            if (findByCriteria != null) {
                stringDocuments = new LinkedList<>();
                SyncTransformerInterface transformerInterface = transformerInstances.get(0);
                for (Object findByCriteria1 : findByCriteria) {
                    Object document = transformerInterface.convertEntityToDocument(findByCriteria1);
                    stringDocuments.add(gson.toJson(document));
                }
            }

        }
        return stringDocuments;
    }

    public List<String> retrieveSellAndTransfer(Long companyId, Class<?> entityClass) throws InstantiationException, IllegalAccessException {
        List<String> stringDocuments = null;
        List<SyncTransformerInterface> transformerInstances = entityDocumentMapper.getTransformerInstance(entityClass);
        if (transformerInstances != null) {

            List<? extends Object> entities = null;
            List<Criteria> criterias = new LinkedList<>();
            criterias.add(Criteria.where("franchise").is(companyId));
//            String[] status = new String[2];
//            status[0] = StockStatus.IN_PRODUCTION;
//            status[1] = StockStatus.TERMINATED;
            criterias.add(new Criteria().orOperator(where("lot").in(lotIds), where("packet").in(packetIds)));
            List findByCriteria = mongoGenericDao.findByCriteria(criterias, entityClass);
            if (findByCriteria != null) {
                stringDocuments = new LinkedList<>();
                SyncTransformerInterface transformerInterface = transformerInstances.get(0);
                for (Object findByCriteria1 : findByCriteria) {

                    Object document = transformerInterface.convertEntityToDocument(findByCriteria1);
                    stringDocuments.add(gson.toJson(document));
                }
            }

        }
        return stringDocuments;
    }

    public List<String> retrievePlanDocument(Long companyId, Class<?> entityClass) throws InstantiationException, IllegalAccessException {
        List<String> stringDocuments = null;
        List<SyncTransformerInterface> transformerInstances = entityDocumentMapper.getTransformerInstance(entityClass);
        if (transformerInstances != null) {
            List<Criteria> criterias = new LinkedList<>();
            criterias.add(Criteria.where("franchiseId").is(companyId));
            criterias.add(new Criteria().orOperator((where("lot").in(lotIds)), (where("packet").in(packetIds))));
            List findByCriteria = mongoGenericDao.findByCriteria(criterias, entityClass);
            if (findByCriteria != null) {
                stringDocuments = new LinkedList<>();
                MetadataDocument metadataDocument = null;
                SyncTransformerInterface transformerInterface = transformerInstances.get(0);
                for (Object findByCriteria1 : findByCriteria) {

                    findByCriteria1 = transformerInterface.convertEntityToDocument(findByCriteria1);
//                        metadataDocument = new MetadataDocument();
//                        metadataDocument.setClassName(findByCriteria1.getClass().getName());
//                        metadataDocument.setCreatedOn(new Date());
//                        metadataDocument.setModifiedOn(new Date());
//                        metadataDocument.setIsArchive(FALSE);
//                        metadataDocument.setIsSqlEntity(false);
//                        metadataDocument.setIdMap(transformerInterface.getidMap());
//                        hkUMSyncService.saveOrUpdateDocument(metadataDocument);
                    stringDocuments.add(gson.toJson(findByCriteria1));
                }
            }
        }
        return stringDocuments;
    }

    /**
     *
     * @param entityClass
     * @param isArchieveFieldName
     * @param companyFieldName
     * @param value
     * @param isMongoDocument
     * @return
     */
    public List<String> retrieveJsonDocuments(Class<?> entityClass, String isArchieveFieldName, String companyFieldName, Long value, boolean isMongoDocument) {
        List<String> jsonObjects = null;
        try {

            List<SyncTransformerInterface> transformerInstances = entityDocumentMapper.getTransformerInstance(entityClass);
            if (transformerInstances != null) {
                SyncTransformerInterface transformerInstance = transformerInstances.get(0);
                List<? extends Object> entities = null;
                if (isMongoDocument) {
                    int transferType;
                    if (transformerInstance.getClass().getSimpleName().contains("HkGenericDocumentTransformer")) {
                        transferType = transformerInstance.getSyncTransferType(entityClass);
                    } else {
                        transferType = transformerInstance.getSyncTransferType();
                    }

                    if (transferType == SyncTransferType.SEND_TO_ALL) {
                        entities = mongoGenericDao.getMongoTemplate().findAll(entityClass);
                    } else {
                        entities = retrieveDocumentByCompany(isArchieveFieldName, companyFieldName, value, entityClass);
                    }
                } else {
                    if (transformerInstance.getSyncTransferType() == SyncTransferType.SEND_TO_ALL) {
                        entities = retrieveAllActiveEntity(isArchieveFieldName, entityClass);
                    } else {
                        entities = retrieveEntityByCompany(isArchieveFieldName, companyFieldName, value, entityClass);
                    }
                }
                if (entities != null) {
                    jsonObjects = convertToJson(entities, transformerInstance, !isMongoDocument);

                }
            }

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SyncInitialSetupUtil.class
                    .getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SyncInitialSetupUtil.class
                    .getName()).log(Level.SEVERE, null, ex);

        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(SyncInitialSetupUtil.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return jsonObjects;
    }

    public List<String> retrieveJsonDocuments(Class<?> entityClass, Map<Integer, Map<String, Object>> criteriaMap) throws InstantiationException, IllegalAccessException {
        List<String> jsonObjects = null;
        LOGGER.debug("criteriaMap " + criteriaMap);
        if (criteriaMap != null) {
            List<SyncTransformerInterface> transformerInstances = entityDocumentMapper.getTransformerInstance(entityClass);
            if (transformerInstances != null) {
                for (SyncTransformerInterface transformerInstance : transformerInstances) {
                    List<Object> entities = retrieveEntityByCriteria(entityClass, criteriaMap);
                    LOGGER.debug("entities: " + entities);
                    jsonObjects = convertToJson(entities, transformerInstance, true);
                    LOGGER.debug("jsonObjects " + jsonObjects);
                }
            }
        }
        return jsonObjects;
    }

    public List<String> convertToJson(List<? extends Object> objects, SyncTransformerInterface transformerInterface, boolean isSqlEntity) {
        List<String> jsonObjects = new LinkedList<>();
        MetadataDocument metadataDocument = null;
        for (Object object : objects) {
            Object documentObject = transformerInterface.convertEntityToDocument(object);
//            metadataDocument = new MetadataDocument();
//            metadataDocument.setClassName(object.getClass().getName());
//            metadataDocument.setCreatedOn(new Date());
//            metadataDocument.setModifiedOn(new Date());
//            metadataDocument.setIsArchive(FALSE);
//            metadataDocument.setIsSqlEntity(isSqlEntity);
//            metadataDocument.setIdMap(transformerInterface.getidMap());
//            hkUMSyncService.saveOrUpdateDocument(metadataDocument);
            String toJson = gson.toJson(documentObject);
            jsonObjects.add(toJson);
        }
        return jsonObjects;
    }

    public List<Object> retrieveEntityByCriteria(Class<?> entityClass, Map<Integer, Map<String, Object>> criteriaMap) {
        Search search = SearchFactory.getSearch(entityClass);
        for (Map.Entry<Integer, Map<String, Object>> entrySet : criteriaMap.entrySet()) {
            Integer operator = entrySet.getKey();
            Map<String, Object> fieldValueMap = entrySet.getValue();
            for (Map.Entry<String, Object> entrySet1 : fieldValueMap.entrySet()) {
                String key1 = entrySet1.getKey();
                Object value1 = entrySet1.getValue();

                LOGGER.debug("Key: " + key1 + " " + value1);

                if (operator == Filter.OP_IN) {
                    LOGGER.debug("##IN" + ((Collection) value1).toArray());
                    Filter filter = Filter.in(key1, ((Collection) value1).toArray());
                    search.addFilter(filter);
                } else if (operator == Filter.OP_NOT_IN) {
                    search.addFilterNotIn(key1, (Collection) value1);
                } else {
                    Filter filter = new Filter(key1, value1, operator);
                    search.addFilter(filter);
                }

            }
        }
//        List<? extends Object> retrieveAllDocuments = hkUMSyncService.retrieveAllDocuments(MetadataDocument.class);
//        for (Object retrieveAllDocument : retrieveAllDocuments) {
//            MetadataDocument metadataDocument = (MetadataDocument) retrieveAllDocument;
//            if (metadataDocument.isIsSqlEntity()) {
//                try {
//                    Search search1 = new Search(Class.forName(metadataDocument.getClassName()));
//                    for (Map.Entry<String, Object> entrySet : metadataDocument.getIdMap().entrySet()) {
//                        search1.addFilterEqual(entrySet.getKey(), entrySet.getValue());
//                    }
//                    List search2 = commonDAO.search(search1);
//                    if (!CollectionUtils.isEmpty(search2)) {
//                        for (Object search21 : search2) {
//                            System.out.println("" + search21);
//                        }
//                    }
//                } catch (ClassNotFoundException ex) {
//                    java.util.logging.Logger.getLogger(SyncInitialSetupUtil.class.getName()).log(Level.SEVERE, null, ex);
//                }
//
//            } else {
//                List<Criteria> criterias = new LinkedList<>();
//                for (Map.Entry<String, Object> entrySet : metadataDocument.getIdMap().entrySet()) {
//                    criterias.add(Criteria.where(entrySet.getKey()).is(entrySet.getValue()));
//                       for (Object search21 : findByCriteria) {
//                            System.out.println("" + search21);
//                        }
//                    }
//                } catch (ClassNotFoundException ex) {
//                    java.util.logging.Logger.getLogger(SyncInitialSetupUtil.class.getName()).log(Level.SEVERE, null, ex);
//                }    }
//                List findByCriteria;
//                try {
//                    findByCriteria = mongoGenericDao.findByCriteria(criterias, forName(metadataDocument.getClassName()));
//                    if (!CollectionUtils.isEmpty(findByCriteria)) {
//                        for (Object search21 : findByCriteria) {
//                            System.out.println("" + search21);
//                        }
//                    }
//                } catch (ClassNotFoundException ex) {
//                    java.util.logging.Logger.getLogger(SyncInitialSetupUtil.class.getName()).log(Level.SEVERE, null, ex);
//                }
//
//            }
//        }
        return commonDAO.search(search);
    }
}
