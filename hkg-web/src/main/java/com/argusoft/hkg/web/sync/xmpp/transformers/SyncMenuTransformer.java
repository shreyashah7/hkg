package com.argusoft.hkg.web.sync.xmpp.transformers;

import com.argusoft.generic.database.common.GenericDao.QueryOperators;
import com.argusoft.generic.database.exception.GenericDatabaseException;
import com.argusoft.hkg.sync.xmpp.util.SyncHelper;
import static com.argusoft.hkg.sync.xmpp.util.SyncTransferType.ONE_TO_MANY;
import com.argusoft.hkg.sync.xmpp.util.SyncTransformerAdapter;
import com.argusoft.hkg.web.usermanagement.databeans.FeatureDataBean;
import com.argusoft.hkg.web.util.ApplicationMasterInitializer;
import com.argusoft.sync.center.model.SyncCenterFeatureDocument;
import com.argusoft.sync.center.model.SyncCenterRoleFeatureDocument;
import com.argusoft.usermanagement.common.core.UMRoleService;
import com.argusoft.usermanagement.common.database.UMRoleDao;
import com.argusoft.usermanagement.common.model.UMCompany;
import com.argusoft.usermanagement.common.model.UMFeature;
import com.argusoft.usermanagement.common.model.UMRole;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates and open the template
 * in the editor.
 */
/**
 *
 * @author shruti
 */
@Service
public class SyncMenuTransformer extends SyncTransformerAdapter {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(SyncMenuTransformer.class);
    @Autowired
    private ApplicationMasterInitializer applicationMasterInitializer;

    @Autowired
    private UMRoleService roleService;
    private final Map<String, Object> idMap = new HashMap<>();
    private final Map<String, List<String>> queryParametersMap;

    private static final String ID = "id";
    private Long franchiseId;

    public SyncMenuTransformer() {
        this.queryParametersMap = new HashMap<>();
    }

    public Long getFranchiseId() {
        return franchiseId;
    }

    public void setFranchiseId(Long franchiseId) {
        this.franchiseId = franchiseId;
    }

    @Override
    public Object convertEntityToDocument(Object entityObject) {
        SyncCenterRoleFeatureDocument roleFeatureDocument = new SyncCenterRoleFeatureDocument();
        try {
            applicationMasterInitializer.initDesignations();
            Long companyId = 0l;
            Date modifiedOn = null;
            if (entityObject != null) {
                Class<? extends Object> type = entityObject.getClass();
                if (type.equals(UMRole.class)) {
                    UMRole role = ((UMRole) entityObject);
                    companyId = role.getCompany();
                    modifiedOn = (role.getModifiedOn() != null) ? role.getModifiedOn() : role.getCreatedOn();
//                    if (companyId.equals(0l)) {
//                        queryParametersMap.put(SyncHelper.FRANCHISE_ID, null);
//                    } else {
//                        queryParametersMap.put(SyncHelper.FRANCHISE_ID, companyId.toString());
//                    }
                    LOGGER.debug("ROLE Companyid= " + companyId);
                } else if (type.equals(UMCompany.class)) {
                    UMCompany company = ((UMCompany) entityObject);
                    companyId = company.getId();
                    modifiedOn = (company.getModifiedOn() != null) ? company.getModifiedOn() : company.getCreatedOn();
//                    queryParametersMap.put(SyncHelper.FRANCHISE_ID, companyId.toString());
                    LOGGER.debug("company Companyid= " + companyId);
                } else if (type.equals(UMFeature.class)) {
                    UMFeature feature = (UMFeature) entityObject;
                    companyId = feature.getCompany();
                    modifiedOn = new Date();
//                    queryParametersMap.put(SyncHelper.FRANCHISE_ID, companyId.toString());
                    LOGGER.debug("feature Companyid= " + companyId);
                } else {
                    return null;
                }
            } else {
                companyId = franchiseId;
                modifiedOn = new Date();
            }
            Map<String, Object> equal = new HashMap<>();
            equal.put(UMRoleDao.IS_ACTIVE, true);
            equal.put(UMRoleDao.IS_ARCHIVE, false);
            Map<String, Object> in = new HashMap<>();
            HashSet<Long> companies = new HashSet<Long>();
            companies.add(companyId);
            companies.add(0l);
            in.put(UMRoleDao.COMPANY, companies);
            Map<QueryOperators, Object> criteria = new HashMap<>();
            criteria.put(QueryOperators.EQUAL, equal);
            criteria.put(QueryOperators.IN, in);
            List<String> projectionList = new LinkedList<>();
            projectionList.add(ID);
            List<UMRole> retrieveRoles = roleService.retrieveRoles(projectionList, criteria, null);
            Map<Long, List<FeatureDataBean>> tmpMapRoleFeatures = applicationMasterInitializer.getMapRoleFeatures();
            Map<Long, Set<FeatureDataBean>> mapRoleFeatures = new HashMap<>();

            for (UMRole role : retrieveRoles) {
                mapRoleFeatures.put(role.getId(), new HashSet<>(tmpMapRoleFeatures.get(role.getId())));
            }
            roleFeatureDocument.setId(1l);
            Gson gson = new GsonBuilder().create();
            LOGGER.debug("webSecurityUtil company: " + companyId);
            roleFeatureDocument.setMapUrlRoles(gson.toJson(applicationMasterInitializer.getMapUrlRoles()));

            String str1 = gson.toJson(mapRoleFeatures);

            Type typeOfHashMap = new TypeToken<Map<Long, List<SyncCenterFeatureDocument>>>() {
            }.getType();
            Map<Long, List<SyncCenterFeatureDocument>> map1 = gson.fromJson(str1, typeOfHashMap);
//            roleFeatureDocument.setModifiedOn(modifiedOn);
            roleFeatureDocument.setMapRoleFeatures(map1);
            if (companyId.equals(0l)) {
                queryParametersMap.put(SyncHelper.FRANCHISE_ID, null);
            } else {
                queryParametersMap.put(SyncHelper.FRANCHISE_ID, Arrays.asList(companyId.toString()));
            }
            roleFeatureDocument.setModifiedOn(modifiedOn);
            idMap.put(ID, 1l);

        } catch (GenericDatabaseException ex) {
            Logger.getLogger(SyncMenuTransformer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return roleFeatureDocument;
    }

    @Override
    public int getSyncTransferType() {
        return ONE_TO_MANY;
    }

    @Override
    public void save(Object object) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isStaticDocumentType() {
        return true;
    }

    @Override
    public Map<String, List<String>> getQueryParameters() {
        return queryParametersMap;
    }

    @Override
    public Map<String, Object> getidMap() {
        return idMap;
    }
}
