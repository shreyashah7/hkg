package com.argusoft.hkg.web.center.usermanagement.databeans;

import com.argusoft.generic.database.exception.GenericDatabaseException;
import com.argusoft.hkg.common.constantutil.HkSystemConstantUtil;
import com.argusoft.hkg.web.usermanagement.transformers.LoginTransformer;
import com.argusoft.hkg.web.center.util.ApplicationUtil;
import com.argusoft.hkg.web.usermanagement.databeans.FeatureDataBean;
import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

/**
 * This bean is created on the first getter method of bean for this bean after
 * session created. This bean is destroyed when session is destroyed
 *
 * When Session is created, the bean is registered to the application When
 * Session is destroyed, the bean is removed from the application
 *
 * @author kelvin
 */
@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class LoginDataBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @Autowired
    private ApplicationUtil hkApplicationUtil;

    private Long id;
    private Long proxyId;
    private Long currentDesignation;
    private String userId;
    private String proxyUserId;
    private List<Long> roleIds = new LinkedList<>();
    private List<Long> proxyRoleIds = new LinkedList<>();
    private String firstName;
    private String proxyFirstName;
    private String lastName;
    private String proxyLastName;
    private String userCode;
    private String proxyUserCode;
    private Long department;
    private Long proxyDepartment;
    private Long companyId;
    //Stores the highest precedence(lowest value) for the user
    private Integer precedence;
    private Integer clientRawOffsetInMin;
    private Integer serverOffsetInMin;
    private boolean isCompanyActivated;
    private String encryptedPassword;
    private List<FeatureDataBean> features;
    private String prefferedLang = HkSystemConstantUtil.I18_LANGUAGE.DEFAULT;
    private boolean isHKAdmin;
    private boolean isFranchiseAdmin;
    private String theme;
    private boolean hasBg;
    private Date serverDate;
    private Boolean isProxyLogin=false;
    private Map<Long, String> reportsToUsers;
    private Map<String, Map<String, String[]>> mapOfFiles;
    private Map<Long, String> roleIdNameMap;

    public Date getServerDate() {
        return serverDate;
    }

    public void setServerDate(Date serverDate) {
        this.serverDate = serverDate;
    }

    public boolean getIsHKAdmin() {
        return isHKAdmin;
    }

    public void setIsHKAdmin(boolean isHKAdmin) {
        this.isHKAdmin = isHKAdmin;
    }


    public String getPrefferedLang() {
        return prefferedLang;
    }

    public void setPrefferedLang(String prefferedLang) {
        this.prefferedLang = prefferedLang;
    }

    public String getEncryptedPassword() {
        return encryptedPassword;
    }

    public void setEncryptedPassword(String encryptedPassword) {
        this.encryptedPassword = encryptedPassword;
    }

    public List<FeatureDataBean> getFeatures() {
        return features;
    }

    public void setFeatures(List<FeatureDataBean> features) {
        this.features = features;
    }

    /**
     * @return the highest precedence(lowest value) for the user
     */
    public Integer getPrecedence() {
        return precedence;
    }

    public void setPrecedence(Integer precedence) {
        this.precedence = precedence;
    }

    public LoginDataBean() {
    }

    public ApplicationUtil getHkApplicationUtil() {
        return hkApplicationUtil;
    }

    public void setHkApplicationUtil(ApplicationUtil hkApplicationUtil) {
        this.hkApplicationUtil = hkApplicationUtil;
    }

    public Integer getServerOffsetInMin() {
        return serverOffsetInMin;
    }

    public void setServerOffsetInMin(Integer serverOffsetInMin) {
        this.serverOffsetInMin = serverOffsetInMin;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getClientRawOffsetInMin() {
        return clientRawOffsetInMin;
    }

    public void setClientRawOffsetInMin(Integer clientRawOffsetInMin) {
        this.clientRawOffsetInMin = clientRawOffsetInMin;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<Long> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(List<Long> roleIds) {
        this.roleIds = roleIds;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public Long getDepartment() {
        return department;
    }

    public void setDepartment(Long department) {
        this.department = department;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public void registerSessionToApplication() throws GenericDatabaseException {
//        hkLoginTransformer.initLoginDataBean(this);
//        hkApplicationUtil.addUser(this);
//        System.out.println(this);
    }

    @PreDestroy
    public void removeSessionFromApplication() {
        hkApplicationUtil.removeUser(this);
    }

    public boolean isIsCompanyActivated() {
        return isCompanyActivated;
    }

    public void setIsCompanyActivated(boolean isCompanyActivated) {
        this.isCompanyActivated = isCompanyActivated;
    }

    public boolean getIsFranchiseAdmin() {
        return isFranchiseAdmin;
    }

    public void setIsFranchiseAdmin(boolean isFranchiseAdmin) {
        this.isFranchiseAdmin = isFranchiseAdmin;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public boolean isHasBg() {
        return hasBg;
    }

    public void setHasBg(boolean hasBg) {
        this.hasBg = hasBg;
    }

    public Map<Long, String> getReportsToUsers() {
        return reportsToUsers;
    }

    public void setReportsToUsers(Map<Long, String> reportsToUsers) {
        this.reportsToUsers = reportsToUsers;
    }

    public Long getProxyId() {
        return proxyId;
    }

    public void setProxyId(Long proxyId) {
        this.proxyId = proxyId;
    }

    public String getProxyUserId() {
        return proxyUserId;
    }

    public void setProxyUserId(String proxyUserId) {
        this.proxyUserId = proxyUserId;
    }

    public List<Long> getProxyRoleIds() {
        return proxyRoleIds;
    }

    public void setProxyRoleIds(List<Long> proxyRoleIds) {
        this.proxyRoleIds = proxyRoleIds;
    }

    public String getProxyFirstName() {
        return proxyFirstName;
    }

    public void setProxyFirstName(String proxyFirstName) {
        this.proxyFirstName = proxyFirstName;
    }

    public String getProxyLastName() {
        return proxyLastName;
    }

    public void setProxyLastName(String proxyLastName) {
        this.proxyLastName = proxyLastName;
    }

    public String getProxyUserCode() {
        return proxyUserCode;
    }

    public void setProxyUserCode(String proxyUserCode) {
        this.proxyUserCode = proxyUserCode;
    }

    public Long getProxyDepartment() {
        return proxyDepartment;
    }

    public void setProxyDepartment(Long proxyDepartment) {
        this.proxyDepartment = proxyDepartment;
    }

    public Boolean getIsProxyLogin() {
        return isProxyLogin;
    }

    public void setIsProxyLogin(Boolean isProxyLogin) {
        this.isProxyLogin = isProxyLogin;
    }

    public Long getCurrentDesignation() {
        return currentDesignation;
    }

    public void setCurrentDesignation(Long currentDesignation) {
        this.currentDesignation = currentDesignation;
    }

    public Map<Long, String> getRoleIdNameMap() {
        return roleIdNameMap;
    }

    public void setRoleIdNameMap(Map<Long, String> roleIdNameMap) {
        this.roleIdNameMap = roleIdNameMap;
    }

    @Override
    public String toString() {
        return "LoginDataBean{" + "hkApplicationUtil=" + hkApplicationUtil + ", id=" + id + ", proxyId=" + proxyId + ", currentDesignation=" + currentDesignation + ", userId=" + userId + ", proxyUserId=" + proxyUserId + ", roleIds=" + roleIds + ", proxyRoleIds=" + proxyRoleIds + ", firstName=" + firstName + ", proxyFirstName=" + proxyFirstName + ", lastName=" + lastName + ", proxyLastName=" + proxyLastName + ", userCode=" + userCode + ", proxyUserCode=" + proxyUserCode + ", department=" + department + ", proxyDepartment=" + proxyDepartment + ", companyId=" + companyId + ", precedence=" + precedence + ", clientRawOffsetInMin=" + clientRawOffsetInMin + ", serverOffsetInMin=" + serverOffsetInMin + ", isCompanyActivated=" + isCompanyActivated + ", encryptedPassword=" + encryptedPassword + ", features=" + features + ", prefferedLang=" + prefferedLang + ", isHKAdmin=" + isHKAdmin + ", isFranchiseAdmin=" + isFranchiseAdmin + ", theme=" + theme + ", hasBg=" + hasBg + ", serverDate=" + serverDate + ", isProxyLogin=" + isProxyLogin + ", reportsToUsers=" + reportsToUsers + ", mapOfFiles=" + mapOfFiles + ", roleIdNameMap=" + roleIdNameMap + '}';
    }

    public Map getMapOfFiles() {
        return mapOfFiles;
    }

    public void setMapOfFiles(Map<String, Map<String, String[]>> mapOfFiles) {
        this.mapOfFiles = mapOfFiles;
    }

}
