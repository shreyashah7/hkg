package com.argusoft.hkg.web.usermanagement.controllers;

import com.argusoft.generic.database.exception.GenericDatabaseException;
import com.argusoft.hkg.core.HkEmployeeService;
import com.argusoft.hkg.core.util.HkUserOperationEnum;
import com.argusoft.hkg.web.base.ResponseCode;
import com.argusoft.hkg.web.base.ResponseEntity;
import com.argusoft.hkg.web.center.usermanagement.databeans.LoginDataBean;
import com.argusoft.hkg.web.center.util.ApplicationUtil;
import com.argusoft.hkg.web.config.WebApplicationInitializerConfig;
import com.argusoft.hkg.web.security.SpringSecurityUserService;
import com.argusoft.hkg.web.security.TokenTransfer;
import com.argusoft.hkg.web.usermanagement.databeans.FeatureDataBean;
import com.argusoft.hkg.web.usermanagement.transformers.LoginTransformer;
import com.argusoft.hkg.web.util.ApplicationMasterInitializer;
import com.argusoft.hkg.web.util.TokenUtils;
import com.argusoft.usermanagement.common.core.UMCompanyService;
import com.argusoft.usermanagement.common.core.UMUserService;
import com.argusoft.usermanagement.common.model.UMUser;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author satyajit
 */
@RestController
@RequestMapping("/common")
public class AuthorizationController {

    @Autowired
    private ApplicationMasterInitializer applicationMasterInitializer;
    @Autowired
    UMUserService umUserService;
    @Autowired
    UMCompanyService uMCompanyService;

    @Autowired
    private LoginDataBean loginDataBean;
    @Autowired
    private LoginTransformer hkLoginTransformer;

    @Autowired
    private ApplicationUtil hkApplicationUtil;
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private HkEmployeeService employeeService;
    @Autowired
    @Qualifier("authenticationManager")
    private AuthenticationManager authManager;

    @Autowired
    private SpringSecurityUserService userService;

    @RequestMapping(value = "/ping", method = RequestMethod.GET)
    public Boolean ping() {
        return true;
    }

    @RequestMapping(value = "/getmenu", method = RequestMethod.GET)
    public List<FeatureDataBean> getMenu() {
        List<Long> roleIds = loginDataBean.getRoleIds();
        return applicationMasterInitializer.generateMenuByRoles(roleIds, loginDataBean.isIsCompanyActivated());
    }

    @RequestMapping(value = "/refresh", method = RequestMethod.GET)
    public boolean refresh() {
        applicationMasterInitializer.initDesignations();
        return true;
    }

    @RequestMapping(value = "/getmetadata", method = RequestMethod.PUT)
    public List<FeatureDataBean> getMetaData(@RequestBody Long featureId) {
        return null;//webSecurityUtil.getViewMetaData(loginDataBean.getRoleIds(), featureId);
    }

    @RequestMapping(value = "/getbuildversion", method = RequestMethod.GET)
    public String getProjectBuildVersion() {
        LOGGER.debug("getProjectVersion() method called.");
        return ApplicationUtil.PROJECT_VERSION;
    }

    @RequestMapping(value = "/getserverdate", method = RequestMethod.GET)
    public Long getServerDate() {
        Date serverDate = new Date();
        return serverDate.getTime();
    }

    /**
     * @author Dhwani,shreya
     * @param clientTimezoneOffset for sending client offset to the server.
     * @return This method returns logged in user's information
     * @throws com.argusoft.generic.database.exception.GenericDatabaseException
     */
    @RequestMapping(value = "/getsession", method = RequestMethod.POST)
    public LoginDataBean getSession(@RequestBody Integer clientTimezoneOffset) throws GenericDatabaseException {
        hkLoginTransformer.initLoginDataBean();
        hkApplicationUtil.addUser();
//        loginDataBean.registerSessionToApplication();

        loginDataBean.setClientRawOffsetInMin(clientTimezoneOffset);
        
        LoginDataBean sessionDataBean = new LoginDataBean();
        sessionDataBean.setId(this.loginDataBean.getId());
        sessionDataBean.setProxyId(this.loginDataBean.getProxyId());
        sessionDataBean.setReportsToUsers(this.loginDataBean.getReportsToUsers());
        sessionDataBean.setFirstName(this.loginDataBean.getFirstName());
        sessionDataBean.setProxyFirstName(this.loginDataBean.getProxyFirstName());
        sessionDataBean.setLastName(this.loginDataBean.getLastName());
        sessionDataBean.setProxyLastName(this.loginDataBean.getProxyLastName());
        sessionDataBean.setCompanyId(this.loginDataBean.getCompanyId());
        sessionDataBean.setServerOffsetInMin(this.loginDataBean.getServerOffsetInMin());
        sessionDataBean.setFeatures(applicationMasterInitializer.generateMenuByRoles(loginDataBean.getRoleIds(), loginDataBean.isIsCompanyActivated()));
        sessionDataBean.setUserCode(this.loginDataBean.getUserCode());
        sessionDataBean.setProxyUserCode(this.loginDataBean.getProxyUserCode());
        sessionDataBean.setIsCompanyActivated(this.loginDataBean.isIsCompanyActivated());
        sessionDataBean.setPrefferedLang(this.loginDataBean.getPrefferedLang());
        sessionDataBean.setIsHKAdmin(this.loginDataBean.getIsHKAdmin());
        sessionDataBean.setIsFranchiseAdmin(this.loginDataBean.getIsFranchiseAdmin());
        sessionDataBean.setDepartment(this.loginDataBean.getDepartment());
        sessionDataBean.setProxyDepartment(this.loginDataBean.getProxyDepartment());
        sessionDataBean.setTheme(this.loginDataBean.getTheme());
        sessionDataBean.setServerDate(this.loginDataBean.getServerDate());
        sessionDataBean.setHasBg(this.loginDataBean.isHasBg());
        sessionDataBean.setRoleIds(this.loginDataBean.getRoleIds());
        sessionDataBean.setProxyRoleIds(this.loginDataBean.getProxyRoleIds());
        sessionDataBean.setIsProxyLogin(this.loginDataBean.getIsProxyLogin());
        sessionDataBean.setRoleIdNameMap(this.loginDataBean.getRoleIdNameMap());
        sessionDataBean.setCurrentDesignation(this.loginDataBean.getCurrentDesignation());
        return sessionDataBean;
    }

    @RequestMapping(value = "/adduseroperationbeforelogout", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public void addUserOperationBeforeLogout() {
        employeeService.createUserOperation(HkUserOperationEnum.LOGOUT, new Date(), loginDataBean.getId(), null, loginDataBean.getId(), loginDataBean.getCompanyId());
        loginDataBean.removeSessionFromApplication();
    }

    /**
     * Authenticate user given username and password
     *
     * @author Raj
     * @param map of username and password
     * @param httpRequest
     * @return TokenTransfer which contains AuthenticationToken,Username and
     * Password
     * @throws UsernameNotFoundException
     */
    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public TokenTransfer authenticate(@RequestBody Map map, HttpServletRequest httpRequest) throws UsernameNotFoundException {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(map.get("userName"), map.get("password"));
        //Authenticate user
        LOGGER.debug("1");
        Authentication authentication = this.authManager.authenticate(authenticationToken);
        LOGGER.debug("2");
        SecurityContextHolder.getContext().setAuthentication(authentication);
        LOGGER.debug("3");
        UserDetails userDetails = userService.loadUserByUsername(map.get("userName").toString());
        //Set userdetails in TokenTransfer
        String magicKey;
        if (httpRequest.getRequestURL() != null && !httpRequest.getRequestURL().toString().startsWith(WebApplicationInitializerConfig.CURRENT_IP)) {
            magicKey = TokenUtils.MAGIC_KEY_MASTER;
        } else {
            magicKey = TokenUtils.MAGIC_KEY_CENTER;
        }
        LOGGER.debug("magicKey  " + magicKey);
        TokenTransfer tokenTransfer = new TokenTransfer(TokenUtils.createToken(userDetails, magicKey));
        tokenTransfer.setUsername(userDetails.getUsername());
        tokenTransfer.setPassword(userDetails.getPassword());

        return tokenTransfer;
    }

    /**
     * Raj: Authenticate for proxy login, takes the th id of user and
     * authenticate against username only
     *
     * @param map which contains id
     * @param httpRequest
     * @return authentication token
     * @throws UsernameNotFoundException
     * @throws GenericDatabaseException
     */
    @RequestMapping(value = "/authenticateProxy", method = RequestMethod.POST)
    public TokenTransfer authenticateProxy(@RequestBody Map map, HttpServletRequest httpRequest) throws UsernameNotFoundException, GenericDatabaseException {
        //System.out.println("map.get(\"id\") " + map.get("id"));
        UMUser user = umUserService.retrieveUserById(Long.valueOf(map.get("id").toString()), null);
        if (user != null) {
            Authentication authentication = new UsernamePasswordAuthenticationToken(user.getUserId(), null, null);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            UserDetails userDetails = userService.loadUserByUsername(user.getUserId());
            String magicKey;
            if (httpRequest.getRequestURL() != null && !httpRequest.getRequestURL().toString().startsWith(WebApplicationInitializerConfig.CURRENT_IP)) {
                magicKey = TokenUtils.MAGIC_KEY_MASTER;
            } else {
                magicKey = TokenUtils.MAGIC_KEY_CENTER;
            }
            TokenTransfer tokenTransfer = new TokenTransfer(TokenUtils.createToken(userDetails, magicKey));
            tokenTransfer.setUsername(userDetails.getUsername());
            tokenTransfer.setPassword(userDetails.getPassword());
            return tokenTransfer;
        }
        return null;
    }

    @RequestMapping(value = "/changepreferreddesignation", method = RequestMethod.POST)
    public void changePreferredDesignation(@RequestBody Long designationId) {
        if (designationId != null) {
            this.loginDataBean.setCurrentDesignation(designationId);
        }
    }
}
