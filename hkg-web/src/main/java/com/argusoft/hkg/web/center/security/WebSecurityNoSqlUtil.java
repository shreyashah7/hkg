package com.argusoft.hkg.web.center.security;

import com.argusoft.generic.database.exception.GenericDatabaseException;
import com.argusoft.hkg.common.UrlPatternKeyedMap;
import com.argusoft.hkg.sync.center.core.SyncCenterRoleFeatureService;
import com.argusoft.hkg.web.center.sync.transformers.SyncCenterFeatureTransformer;
import com.argusoft.hkg.web.comparator.FeatureDataBeanComparator;
import com.argusoft.hkg.web.usermanagement.databeans.FeatureDataBean;
import com.argusoft.sync.center.model.SyncCenterFeatureDocument;
import com.argusoft.sync.center.model.SyncCenterRoleFeatureDocument;
import com.google.gson.Gson;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 *
 * @author shruti
 */
@Service
public class WebSecurityNoSqlUtil {

    @Autowired
    private SyncCenterRoleFeatureService roleFeatureService;
    @Autowired
    SyncCenterFeatureTransformer featureTransformer;
    private SyncCenterRoleFeatureDocument roleFeatureDocument;
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    private UrlPatternKeyedMap mapUrlRoles;
    private static Long hkAdminRole = 0l;
    private Map<Long, FeatureDataBean> mapFeatureIdFeature = null;
    private Map<Long, Set<FeatureDataBean>> mapRoleFeatures = null;

    @PostConstruct
    public void init() {
        try {
            if (roleFeatureService != null) {
                roleFeatureDocument = roleFeatureService.retrieveById(1l);
                //  Initializing and associates authorities with url for access control (Authorization)
                this.initRoleFeaturesMap();
                this.initializeMapUrlRoles();
            }
        } catch (GenericDatabaseException ex) {
            java.util.logging.Logger.getLogger(WebSecurityNoSqlUtil.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public WebSecurityNoSqlUtil() {

    }

    public SyncCenterRoleFeatureDocument getRoleFeatureDocument() {
        return roleFeatureDocument;
    }

    public static Long getHkAdminRole() {
        return hkAdminRole;
    }

    public static void setHkAdminRole(Long hkAdminRole) {
        WebSecurityNoSqlUtil.hkAdminRole = hkAdminRole;
    }

    public UrlPatternKeyedMap getMapUrlRoles() {
        return mapUrlRoles;
    }

    public void setMapUrlRoles(UrlPatternKeyedMap mapUrlRoles) {
        this.mapUrlRoles = mapUrlRoles;
    }

    private void initializeMapUrlRoles() {
        if (roleFeatureService != null) {
            roleFeatureDocument = roleFeatureService.retrieveById(1l);
            Gson gson = new Gson();
            if (roleFeatureDocument != null) {
                mapUrlRoles = gson.fromJson(roleFeatureDocument.getMapUrlRoles(), UrlPatternKeyedMap.class);
            } else {
                mapUrlRoles = new UrlPatternKeyedMap();
                mapUrlRoles.put("/css/**", "IS_AUTHENTICATED_ANONYMOUSLY");
                mapUrlRoles.put("/", "IS_AUTHENTICATED_ANONYMOUSLY");
                mapUrlRoles.put("/i18n/**", "IS_AUTHENTICATED_ANONYMOUSLY");
                mapUrlRoles.put("/api/common/getbuildversion", "IS_AUTHENTICATED_ANONYMOUSLY");
                mapUrlRoles.put("/ico/**", "IS_AUTHENTICATED_ANONYMOUSLY");
                mapUrlRoles.put("/images/**", "IS_AUTHENTICATED_ANONYMOUSLY");
                mapUrlRoles.put("/scripts/**", "IS_AUTHENTICATED_ANONYMOUSLY");
                mapUrlRoles.put("/index.html", "IS_AUTHENTICATED_ANONYMOUSLY");
                mapUrlRoles.put("/api/sync/deployserver", "IS_AUTHENTICATED_ANONYMOUSLY");
                mapUrlRoles.put("/api/centerfranchise/retrieve", "IS_AUTHENTICATED_ANONYMOUSLY");

            }

        }

    }

    public void initRoleFeaturesMap() throws GenericDatabaseException {
        if (roleFeatureDocument != null) {
            Map<Long, List<SyncCenterFeatureDocument>> mapRoleFeatures1 = roleFeatureDocument.getMapRoleFeatures();
            if (mapRoleFeatures1 != null) {
                mapRoleFeatures = new HashMap<>(mapRoleFeatures1.size());
                for (Map.Entry<Long, List<SyncCenterFeatureDocument>> entry : mapRoleFeatures1.entrySet()) {
                    Long long1 = entry.getKey();
                    List<SyncCenterFeatureDocument> list = entry.getValue();
                    Set<FeatureDataBean> databeanList = featureTransformer.convertFeatureDocumentListToFeatureDataBeanList(list);
                    mapRoleFeatures.put(long1, databeanList);
                }
            }

        }

    }

    public List<FeatureDataBean> generateMenuByRoles(List<Long> roleIds, boolean isCompanyActivated) {

        Set<FeatureDataBean> features = new HashSet<>();
        for (Long role : mapRoleFeatures.keySet()) {
            if (roleIds.contains(role)) {
                Set<FeatureDataBean> get = mapRoleFeatures.get(role);
                for (FeatureDataBean featureDataBean : get) {
                    features.add(featureDataBean);
                }
            }
        }
        LinkedList<FeatureDataBean> linkedList = new LinkedList<>();
        linkedList.addAll(features);
        return this.sortMenuBasedOnSeqNo(linkedList);
    }

    public List<FeatureDataBean> sortMenuBasedOnSeqNo(List<FeatureDataBean> featureDatabeans) {
        if (!CollectionUtils.isEmpty(featureDatabeans)) {
            Collections.sort(featureDatabeans, FeatureDataBeanComparator.getComparator(FeatureDataBeanComparator.SORT_SEQ_NO));
            for (FeatureDataBean featureDatabean : featureDatabeans) {
                if (featureDatabean.getChildren() != null) {
                    featureDatabean.setChildren(sortMenuBasedOnSeqNo(featureDatabean.getChildren()));
                }
            }
        }
        return featureDatabeans;
    }

}
