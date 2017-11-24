package com.argusoft.hkg.sync.center.core.impl;


import com.argusoft.generic.database.common.MongoGenericDao;
import com.argusoft.hkg.sync.center.core.SyncCenterFranchiseService;
import com.argusoft.sync.center.model.SyncCenterFranchiseDocument;
import java.math.BigInteger;
import java.util.LinkedList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;

/**
 *
 * @author shruti
 */
public class SyncCenterFranchiseServiceImpl implements SyncCenterFranchiseService {

    @Autowired
    private MongoGenericDao mongoGenericDao;
    private static final Logger LOGGER = LoggerFactory.getLogger(SyncCenterFranchiseServiceImpl.class);

    @Override
    public Long saveOrUpdate(SyncCenterFranchiseDocument document) {
        LOGGER.debug("Class:CenterFranchiseServiceImpl  Method:saveOrUpdate");
//        LOGGER.debug("document: " + document);
        mongoGenericDao.update(document);
        return document.getId();
    }

    @Override
    public SyncCenterFranchiseDocument retrieveById(Long id) {
        LOGGER.debug("Class:CenterFranchiseServiceImpl  Method:retrieveById");
        LOGGER.debug("id: " + id);
//                List<Criteria> criterias=new LinkedList<>();
//        criterias.add(Criteria.where("id").is(1l));
//        List findByCriteria = mongoGenericDao.findByCriteria(criterias,SyncCenterFranchiseDocument.class);
//        System.out.println("findByCriteria " + findByCriteria);
        SyncCenterFranchiseDocument findById = mongoGenericDao.getMongoTemplate().findById(id, SyncCenterFranchiseDocument.class, "franchise");
        LOGGER.debug("output:  " + findById);
             return findById;

    }

}
