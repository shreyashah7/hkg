package com.argusoft.hkg.web.center.usermanagement.controllers;

import com.argusoft.generic.database.exception.GenericDatabaseException;
import com.argusoft.hkg.sync.center.core.HkUMSyncService;
import com.argusoft.hkg.sync.center.core.SyncCenterUserService;
import com.argusoft.hkg.web.center.security.SpringSecurityNoSqlUserService;
import com.argusoft.hkg.web.center.security.WebSecurityNoSqlUtil;
import com.argusoft.hkg.web.center.sync.transformers.SyncCenterLoginTransformer;
import com.argusoft.hkg.web.center.usermanagement.databeans.LoginDataBean;
import com.argusoft.hkg.web.center.util.ApplicationUtil;
import com.argusoft.hkg.web.config.WebApplicationInitializerConfig;
import com.argusoft.hkg.web.security.TokenTransfer;
import com.argusoft.hkg.web.usermanagement.databeans.FeatureDataBean;
import com.argusoft.hkg.web.util.TokenUtils;
import com.argusoft.sync.center.model.SyncCenterUserDocument;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
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
 * @author shruti
 */
@RestController
@RequestMapping("/common")
public class CenterAuthorizationController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CenterAuthorizationController.class);
    @Autowired
    private WebSecurityNoSqlUtil webSecurityNoSqlUtil;
    @Autowired
    private LoginDataBean loginDataBean;
    @Autowired
    private SyncCenterLoginTransformer hkLoginTransformer;
    @Autowired
    private ApplicationUtil hkApplicationUtil;
    @Autowired
    @Qualifier("authenticationManager")
    private AuthenticationManager authManager;
    @Autowired
    SpringSecurityNoSqlUserService userService;
//    @Autowired
//    private HkEmployeeService employeeService;
    @Autowired
    HkUMSyncService hkUMSyncService;

    @Autowired
    SyncCenterUserService centerUserService;

    @RequestMapping(value = "/ping", method = RequestMethod.GET)
    public Boolean ping() {
        return true;
    }

    @RequestMapping(value = "/getmenu", method = RequestMethod.GET)
    public List<FeatureDataBean> getMenu() {
        List<Long> roleIds = loginDataBean.getRoleIds();
        return webSecurityNoSqlUtil.generateMenuByRoles(roleIds, loginDataBean.isIsCompanyActivated());
    }

    @RequestMapping(value = "/refresh", method = RequestMethod.GET)
    public boolean refresh() {
        webSecurityNoSqlUtil.init();
        return true;
    }

    @RequestMapping(value = "/getmetadata", method = RequestMethod.PUT)
    public List<FeatureDataBean> getMetaData(@RequestBody Long featureId) {
        return null;//webSecurityNoSqlUtil.getViewMetaData(loginDataBean.getRoleIds(), featureId);
    }

    @RequestMapping(value = "/getbuildversion", method = RequestMethod.GET)
    public String getProjectBuildVersion() {
        LOGGER.debug("getProjectVersion() method called.");
        return ApplicationUtil.PROJECT_VERSION;
    }

    /**
     * @author Dhwani,shreya
     * @param clientTimezoneOffset for sending client offset to the server.
     * @return This method returns logged in user's information
     */
    @RequestMapping(value = "/getsession", method = RequestMethod.POST)
    public LoginDataBean getSession(@RequestBody Integer clientTimezoneOffset) {
        LoginDataBean sessionDataBean = new LoginDataBean();
        try {
            hkLoginTransformer.initLoginDataBean();
            hkApplicationUtil.addUser();
//        loginDataBean.registerSessionToApplication();
            LOGGER.debug("IN GET SESSOIN");
            loginDataBean.setClientRawOffsetInMin(clientTimezoneOffset);

            sessionDataBean.setId(this.loginDataBean.getId());
            sessionDataBean.setProxyId(this.loginDataBean.getProxyId());
            sessionDataBean.setReportsToUsers(this.loginDataBean.getReportsToUsers());
            sessionDataBean.setFirstName(this.loginDataBean.getFirstName());
            sessionDataBean.setProxyFirstName(this.loginDataBean.getProxyFirstName());
            sessionDataBean.setLastName(this.loginDataBean.getLastName());
            sessionDataBean.setProxyLastName(this.loginDataBean.getProxyLastName());
            sessionDataBean.setCompanyId(this.loginDataBean.getCompanyId());
            sessionDataBean.setServerOffsetInMin(this.loginDataBean.getServerOffsetInMin());
            sessionDataBean.setFeatures(webSecurityNoSqlUtil.generateMenuByRoles(loginDataBean.getRoleIds(), loginDataBean.isIsCompanyActivated()));
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
            sessionDataBean.setCurrentDesignation(this.loginDataBean.getCurrentDesignation());
            sessionDataBean.setRoleIdNameMap(this.loginDataBean.getRoleIdNameMap());
        } catch (GenericDatabaseException ex) {
            java.util.logging.Logger.getLogger(CenterAuthorizationController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return sessionDataBean;
    }

    /**
     * Authenticate for proxy login, takes the th id of user and authenticate
     * against username only
     *
     * @param map which contains id
     * @param httpRequest
     * @return authentication token
     * @throws UsernameNotFoundException
     * @throws GenericDatabaseException
     */
    @RequestMapping(value = "/authenticateProxy", method = RequestMethod.POST)
    public TokenTransfer authenticateProxy(@RequestBody Map map, HttpServletRequest httpRequest) throws UsernameNotFoundException, GenericDatabaseException {
        LOGGER.debug("IN AUTHENTICATE PROXY" + map.get("id"));

        SyncCenterUserDocument user = centerUserService.retrieveUsersById(Long.parseLong((String) map.get("id")));

        LOGGER.debug("uSER!=NULL" + user);
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
            LOGGER.debug("magicKey  " + magicKey);
            TokenTransfer tokenTransfer = new TokenTransfer(TokenUtils.createToken(userDetails, magicKey));
            tokenTransfer.setUsername(userDetails.getUsername());
            tokenTransfer.setPassword(userDetails.getPassword());
            return tokenTransfer;
        }
        return null;
    }

    @RequestMapping(value = "/adduseroperationbeforelogout", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public void addUserOperationBeforeLogout() {
//        employeeService.createUserOperation(HkUserOperationEnum.LOGOUT, new Date(), loginDataBean.getId(), null, loginDataBean.getId(), loginDataBean.getCompanyId());
        loginDataBean.removeSessionFromApplication();
    }

    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public TokenTransfer authenticate(@RequestBody Map map, HttpServletRequest httpRequest) throws UsernameNotFoundException {
        LOGGER.debug("/authenticate ");
        UsernamePasswordAuthenticationToken authenticationToken
                = new UsernamePasswordAuthenticationToken(map.get("userName"), map.get("password"));
        LOGGER.debug("authentication token generated : " + authenticationToken.toString());
        //Authenticate user
        LOGGER.debug("authenticating user");
        Authentication authentication = this.authManager.authenticate(authenticationToken);
        LOGGER.debug("1");
        SecurityContextHolder.getContext().setAuthentication(authentication);
        LOGGER.debug("2");
        UserDetails userDetails = userService.loadUserByUsername(map.get("userName").toString());
        LOGGER.debug("userDetails " + userDetails);
        //Set userdetails in TokenTransfer
        String magicKey;
        if (httpRequest.getRequestURL() != null && !httpRequest.getRequestURL().toString().startsWith(WebApplicationInitializerConfig.CURRENT_IP)) {
            magicKey = TokenUtils.MAGIC_KEY_MASTER;
        } else {
            magicKey = TokenUtils.MAGIC_KEY_CENTER;
        }
        TokenTransfer tokenTransfer = new TokenTransfer(TokenUtils.createToken(userDetails, magicKey));
        LOGGER.debug("userDetails.getUsername() " + userDetails.getUsername());
        tokenTransfer.setUsername(userDetails.getUsername());
        tokenTransfer.setPassword(userDetails.getPassword());

        return tokenTransfer;
    }
    
    @RequestMapping(value = "/changepreferreddesignation", method = RequestMethod.POST)
    public void changePreferredDesignation(@RequestBody Long designationId) {
        if (designationId != null) {
            this.loginDataBean.setCurrentDesignation(designationId);
            this.loginDataBean.setRoleIds(Arrays.asList(designationId));
        }
    }
}
