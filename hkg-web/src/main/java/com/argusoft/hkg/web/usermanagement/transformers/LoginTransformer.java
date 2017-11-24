package com.argusoft.hkg.web.usermanagement.transformers;

import com.argusoft.generic.database.common.GenericDao;
import com.argusoft.generic.database.common.GenericDao.QueryOperators;
import com.argusoft.generic.database.exception.GenericDatabaseException;
import com.argusoft.hkg.common.functionutil.FolderManagement;
import com.argusoft.hkg.core.HkEmployeeService;
import com.argusoft.hkg.core.util.HkUserOperationEnum;
import com.argusoft.hkg.web.center.usermanagement.databeans.LoginDataBean;
import com.argusoft.hkg.web.center.util.ApplicationUtil;
import com.argusoft.hkg.web.util.ApplicationMasterInitializer;
import com.argusoft.usermanagement.common.core.UMRoleService;
import com.argusoft.usermanagement.common.core.UMUserService;
import com.argusoft.usermanagement.common.database.UMRoleDao;
import com.argusoft.usermanagement.common.database.UMUserDao;
import com.argusoft.usermanagement.common.model.UMRole;
import com.argusoft.usermanagement.common.model.UMUser;
import com.argusoft.usermanagement.common.model.UMUserRole;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 *
 * @author kelvin
 */
@Service
public class LoginTransformer {

    @Autowired
    private UMUserService umUserService;
    @Autowired
    private UMRoleService umRoleService;
    @Autowired
    private ApplicationUtil applicationUtil;
    @Autowired
    private HkEmployeeService employeeService;
    @Autowired
    private LoginDataBean hkLoginDataBean;

    //  Initialize User Login Session Data
    public void initLoginDataBean() throws GenericDatabaseException {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        UMUser user = umUserService.getUserbyUserName(username, true, true, false, false, false, Boolean.TRUE);
        if (user != null) {
            int precedenceItr = 1;
            int precedence = 3;
            Set<UMUserRole> userRoleSet = user.getUMUserRoleSet();
            List<Long> roles = new ArrayList<>();
            Map<Long, String> roleIdNameMap = new HashMap<>();
            Boolean isHKAdmin = Boolean.FALSE;
            Boolean isFranchiseAdmin = Boolean.FALSE;
            for (UMUserRole uMUserRole : userRoleSet) {
                if (ApplicationMasterInitializer.getHkAdminRole().equals(uMUserRole.getuMUserRolePK().getRole())) {
                    isHKAdmin = Boolean.TRUE;
                }
                if (uMUserRole.getIsActive() && !uMUserRole.getIsArchive()) {
                    roles.add(uMUserRole.getuMUserRolePK().getRole());
                    precedenceItr = applicationUtil.getPrecedence(uMUserRole.getuMUserRolePK().getRole());
                    if (precedenceItr == 1) {
                        isFranchiseAdmin = Boolean.TRUE;
                    }
                    if (precedenceItr < precedence) {
                        precedence = precedenceItr;
                    }
                }
            }
            if(!CollectionUtils.isEmpty(roles)){
                Map<GenericDao.QueryOperators, Object> criterias = new HashMap<>();
                Map<String, Object> inMap = new HashMap<>();

                inMap.put(UMRoleDao.ID, roles);

                criterias.put(GenericDao.QueryOperators.IN, inMap);
                List<UMRole> retrievedRoles = umRoleService.retrieveRoles(null, criterias, null);
                for(UMRole role : retrievedRoles){
                    roleIdNameMap.put(role.getId(), role.getName());
                }
            }
            if (hkLoginDataBean.getId() == null) {
                Map<Long, String> result = new LinkedHashMap<>();
                StringBuilder currentUser = new StringBuilder("");
                if (user.getFirstName() != null) {
                    currentUser.append(user.getFirstName());
                }
                if (user.getLastName() != null) {
                    currentUser.append(" ");
                    currentUser.append(user.getLastName());
                }
                result.put(user.getId(), currentUser.toString());
                hkLoginDataBean.setReportsToUsers(retrieveReportsToUser(user.getId(), result));

                hkLoginDataBean.setProxyDepartment(user.getDepartment());
                hkLoginDataBean.setProxyFirstName(user.getFirstName());
                hkLoginDataBean.setProxyLastName(user.getLastName());
                hkLoginDataBean.setProxyUserCode(user.getUserCode());
                hkLoginDataBean.setProxyId(user.getId());
                hkLoginDataBean.setProxyUserId(username);
                hkLoginDataBean.setProxyRoleIds(roles);
            } else if(!hkLoginDataBean.getProxyId().equals(user.getId())){
                hkLoginDataBean.setIsProxyLogin(true);
            } else if(hkLoginDataBean.getProxyId().equals(user.getId())){
                hkLoginDataBean.setIsProxyLogin(false);
            }

            hkLoginDataBean.setDepartment(user.getDepartment());
            hkLoginDataBean.setFirstName(user.getFirstName());
            hkLoginDataBean.setLastName(user.getLastName());
            hkLoginDataBean.setUserCode(user.getUserCode());
            hkLoginDataBean.setServerDate(new Date());
            hkLoginDataBean.setId(user.getId());
            hkLoginDataBean.setEncryptedPassword(user.getPassword());
            hkLoginDataBean.setUserId(username);
            hkLoginDataBean.setCompanyId(user.getCompany());

            hkLoginDataBean.setIsHKAdmin(isHKAdmin);
            hkLoginDataBean.setIsFranchiseAdmin(isFranchiseAdmin);
            hkLoginDataBean.setPrecedence(precedence);
            hkLoginDataBean.setServerOffsetInMin(Calendar.getInstance().getTimeZone().getRawOffset() / (1000 * 60));
            if(hkLoginDataBean.getCurrentDesignation() == null || !roles.contains(hkLoginDataBean.getCurrentDesignation())){
                hkLoginDataBean.setCurrentDesignation(roles.get(0));
            }
            hkLoginDataBean.setRoleIdNameMap(roleIdNameMap);
            hkLoginDataBean.setRoleIds(roles);
            String theme = user.getContact().getFacebookPage();
            hkLoginDataBean.setHasBg(false);
            if (theme != null && theme.length() > 0) {
                String[] split = theme.split(",");
                if (split[0].indexOf(FolderManagement.UNIQUE_SEPARATOR) == -1) {
                    hkLoginDataBean.setTheme(split[0]);
                } else {
                    hkLoginDataBean.setTheme("default");
                }
                for (String split1 : split) {
                    if (split1.indexOf(FolderManagement.UNIQUE_SEPARATOR) != -1) {
                        hkLoginDataBean.setHasBg(true);
                    }
                }
            } else {
                hkLoginDataBean.setTheme("default");
            }
            if (StringUtils.hasText(user.getPreferredLanguage())) {
                hkLoginDataBean.setPrefferedLang(user.getPreferredLanguage());
            }

            employeeService.createUserOperation(HkUserOperationEnum.LOGIN, new Date(), hkLoginDataBean.getId(), null, null, hkLoginDataBean.getCompanyId());
        }

    }

    public Map<Long, String> retrieveReportsToUser(Long id, Map<Long, String> result) throws GenericDatabaseException {
        String customString = "" + id + ":E";
        Map<String, Object> likeQuery = new HashMap<>();
        Map<String, Object> equalQuery = new HashMap<>();
        likeQuery.put(UMUserDao.CUSTOM2, customString);
        equalQuery.put(UMUserDao.IS_ARCHIVE, Boolean.FALSE);
        Map<QueryOperators, Object> criteria = new HashMap<>();
        criteria.put(QueryOperators.ILIKE, likeQuery);
        criteria.put(QueryOperators.EQUAL, equalQuery);
        List<UMUser> users = umUserService.retrieveUsers(null, criteria, null);

        if (!CollectionUtils.isEmpty(users)) {
            for (UMUser uMUser : users) {
                result.put(uMUser.getId(), uMUser.getFirstName() + " " + uMUser.getLastName());
            }
        } else {
            result = null;
        }
        return result;
    }
}
