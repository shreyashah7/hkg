/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.sync.xmpp.transformers;

import com.argusoft.generic.database.exception.GenericDatabaseException;
import com.argusoft.hkg.sync.xmpp.util.SyncHelper;
import static com.argusoft.hkg.sync.xmpp.util.SyncTransferType.ONE_TO_MANY;
import com.argusoft.hkg.sync.xmpp.util.SyncTransformerAdapter;
import com.argusoft.sync.center.model.UMUserIpAssociationDocument;
import com.argusoft.usermanagement.common.core.UMUserService;
import com.argusoft.usermanagement.common.database.UMUserDao;
import com.argusoft.usermanagement.common.model.UMUser;
import com.argusoft.usermanagement.common.model.UMUserIpAssociation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author shruti
 */
@Service
public class UmUserIpAssociationTransformer extends SyncTransformerAdapter {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(UmUserIpAssociationTransformer.class);

    private final Map<String, List<String>> queryParametersMap;
    private final Map<String, Object> idMap;

    private static final String ID = "id";
    @Autowired
    private UMUserService userService;

    public UmUserIpAssociationTransformer() {
        this.idMap = new HashMap<>();
        this.queryParametersMap = new HashMap<>();
    }

    @Override
    public void save(Object object) {

    }

    @Override
    public Object convertEntityToDocument(Object entityObject) {
        UMUserIpAssociationDocument userIpAssociationDocument = null;
        if (entityObject != null && entityObject instanceof UMUserIpAssociation) {
            try {
                UMUserIpAssociation userIpAssociation = (UMUserIpAssociation) entityObject;
                if (userIpAssociation.getIsActive()) {
                    userIpAssociationDocument = new UMUserIpAssociationDocument();
                    userIpAssociationDocument.setId(userIpAssociation.getId());
                    userIpAssociationDocument.setIpAddress(userIpAssociation.getIpAddress());
                    userIpAssociationDocument.setIsActive(userIpAssociation.getIsActive());
                    userIpAssociationDocument.setLastModifiedOn(userIpAssociation.getLastModifiedOn());
                    userIpAssociationDocument.setUserId(userIpAssociation.getUserId());
                    List<String> requires = new ArrayList<>();
                    requires.add(UMUserDao.COMPANY);
                    UMUser user = userService.retrieveUserById(userIpAssociation.getUserId(), requires);

                    idMap.put(ID, userIpAssociation.getId());
                    queryParametersMap.put(SyncHelper.FRANCHISE_ID, Arrays.asList(user.getCompany().toString()));
                } else {
                    return null;
                }
            } catch (GenericDatabaseException ex) {
                Logger.getLogger(UmUserIpAssociationTransformer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return userIpAssociationDocument;
    }

    @Override
    public int getSyncTransferType() {
        return ONE_TO_MANY;
    }

    @Override
    public Map<String, List<String>> getQueryParameters() {
        return Collections.unmodifiableMap(queryParametersMap);
    }

    @Override
    public Map<String, Object> getidMap() {
        return Collections.unmodifiableMap(idMap);
    }
}
