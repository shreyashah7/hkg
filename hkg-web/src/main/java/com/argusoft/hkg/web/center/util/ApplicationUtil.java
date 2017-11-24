package com.argusoft.hkg.web.center.util;

import com.argusoft.hkg.common.functionutil.PropertyFileReader;
import com.argusoft.hkg.web.center.usermanagement.databeans.CenterFranchiseDataBean;
import com.argusoft.hkg.web.center.usermanagement.databeans.LoginDataBean;
import com.argusoft.hkg.web.center.usermanagement.databeans.UMFeatureDetailDataBean;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 *
 * @author kelvin
 */
@Component
public class ApplicationUtil {

    @Autowired
    LoginDataBean hkLoginDataBean;
    private Map<String, LoginDataBean> users;
    private Map<String, String> systemConfigrationMap;
    private final Logger LOGGER;
    private Map<Long, Integer> rolePrecedence;
    private Map<Long, Map<String, Map<String, Object>>> featureFromTemplateMap;
    private Map<String, Map<String, Object>> centerFeatureFromTemplateMap;
    private CenterFranchiseDataBean centerFranchiseDataBean;
    public static String PROJECT_VERSION = "0.1";
    private Map<String, UMFeatureDetailDataBean> uMFeatureDetailDataBeanMap;
    private Map<String, Long> featureNameIdMap;

    public ApplicationUtil() {
        LOGGER = LoggerFactory.getLogger(this.getClass());
        users = new HashMap<>();
        PROJECT_VERSION = retrieveProjectVersion();
    }

    public Map<String, UMFeatureDetailDataBean> getuMFeatureDetailDataBeanMap() {
        return uMFeatureDetailDataBeanMap;
    }

    public void setuMFeatureDetailDataBeanMap(Map<String, UMFeatureDetailDataBean> uMFeatureDetailDataBeanMap) {
        this.uMFeatureDetailDataBeanMap = uMFeatureDetailDataBeanMap;
    }

    public CenterFranchiseDataBean getCenterFranchiseDataBean() {
        return centerFranchiseDataBean;
    }

    public void setCenterFranchiseDataBean(CenterFranchiseDataBean centerFranchiseDataBean) {
        this.centerFranchiseDataBean = centerFranchiseDataBean;
    }

    public Map<String, LoginDataBean> getUsers() {
        return users;
    }

    public Map<String, Map<String, Object>> getCenterFeatureFromTemplateMap() {
        return centerFeatureFromTemplateMap;
    }

    public void setCenterFeatureFromTemplateMap(Map<String, Map<String, Object>> centerFeatureFromTemplateMap) {
        this.centerFeatureFromTemplateMap = centerFeatureFromTemplateMap;
    }

    public Map<Long, Integer> getRolePrecedence() {
        return rolePrecedence;
    }

    public void setRolePrecedence(Map<Long, Integer> rolePrecedence) {
        this.rolePrecedence = rolePrecedence;
    }

    public void setUsers(Map<String, LoginDataBean> users) {
        this.users = users;
    }

    public Map<String, String> getSystemConfigrationMap() {
        return systemConfigrationMap;
    }

    public void setSystemConfigrationMap(Map<String, String> systemConfigrationMap) {
        this.systemConfigrationMap = systemConfigrationMap;
    }

    public Map<Long, Map<String, Map<String, Object>>> getFeatureFromTemplateMap() {
        return featureFromTemplateMap;
    }

    public void setFeatureFromTemplateMap(Map<Long, Map<String, Map<String, Object>>> featureFromTemplateMap) {
        this.featureFromTemplateMap = featureFromTemplateMap;
    }

    public void addUser() {
        if (hkLoginDataBean != null && StringUtils.hasText(hkLoginDataBean.getUserId())) {
            if (!users.containsKey(hkLoginDataBean.getUserId())) {
                LOGGER.debug(hkLoginDataBean.getUserId() + " added to application. 2" + hkLoginDataBean.toString());
                LOGGER.debug(hkLoginDataBean.toString());
                users.put(hkLoginDataBean.getUserId(), hkLoginDataBean);
            }
        }
    }

    public void removeUser(LoginDataBean hkLoginDataBean) {
        if (hkLoginDataBean != null && StringUtils.hasText(hkLoginDataBean.getUserId())) {
            if (users.containsKey(hkLoginDataBean.getUserId())) {
                LOGGER.debug(hkLoginDataBean.getUserId() + " remove from the application.");
                users.remove(hkLoginDataBean.getUserId());
            }
        }
    }

    public int getPrecedence(long role) {
        Integer precedence = rolePrecedence.get(role);
        if (precedence != null) {
            return precedence;
        } else {
            return 1;
        }
    }

    public final static String retrieveProjectVersion() {
        PropertyFileReader propertyFileReader = new PropertyFileReader();
        return propertyFileReader.getPropertyValue("hkg.build.version", "hkg.properties");
    }

    public Map<Long, String> getFeatureIdNameMap() {
        Map<String, UMFeatureDetailDataBean> umFeatureDetailDataBeanMap = getuMFeatureDetailDataBeanMap();
        Map<Long, String> featureIdName = new HashMap<>();
        for (Map.Entry<String, UMFeatureDetailDataBean> entrySet : umFeatureDetailDataBeanMap.entrySet()) {
            String key = entrySet.getKey();
            UMFeatureDetailDataBean value = entrySet.getValue();
            featureIdName.put(value.getId(), value.getFeatureName());
        }
        return featureIdName;
    }

    /**
     * @return the featureNameIdMap
     */
    public Map<String, Long> getFeatureNameIdMap() {
        return featureNameIdMap;
    }

    /**
     * @param featureNameIdMap the featureNameIdMap to set
     */
    public void setFeatureNameIdMap(Map<String, Long> featureNameIdMap) {
        this.featureNameIdMap = featureNameIdMap;
    }

}
