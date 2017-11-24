package com.argusoft.hkg.web.center.sync.transformers;

import com.argusoft.generic.database.common.MongoGenericDao;
import com.argusoft.generic.database.exception.GenericDatabaseException;
import com.argusoft.hkg.sync.center.core.SyncCenterUserService;
import com.argusoft.hkg.sync.center.core.SyncRoleService;
import com.argusoft.hkg.web.center.security.WebSecurityNoSqlUtil;
import com.argusoft.hkg.web.center.usermanagement.databeans.LoginDataBean;
import com.argusoft.hkg.web.center.util.ApplicationUtil;
import com.argusoft.sync.center.model.SyncCenterUserDocument;
import com.argusoft.sync.center.model.SyncCenterUserRoleDocument;
import com.argusoft.sync.center.model.UmDesignationDocument;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 *
 * @author shruti
 */
@Service
public class SyncCenterLoginTransformer {

    @Autowired
    private SyncCenterUserService userCenterService;
    @Autowired
    private SyncRoleService roleCenterService;
    @Autowired
    private LoginDataBean hkLoginDataBean;
    @Autowired
    private MongoGenericDao mongoGenericDao;
//    @Autowired
//    private UserManagementServiceWrapper userManagementServiceWrapper;
    @Autowired
    private ApplicationUtil applicationUtil;
//    @Autowired
//    private HkEmployeeService employeeService;
    public static final String CUSTOM2 = "custom2";

    //  Initialize User Login Session Data
    public void initLoginDataBean() throws GenericDatabaseException {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        SyncCenterUserDocument user = userCenterService.getUserbyUserName(username, true, Boolean.TRUE);
        if (user != null) {
            List<SyncCenterUserRoleDocument> userRoleSet = user.getuMUserRoleSet();
            if (user.getDepartmentId() != null) {
                hkLoginDataBean.setDepartment(user.getDepartmentId());
            }
//            hkLoginDataBean.setFirstName(user.getFirstName());
//            hkLoginDataBean.setLastName(user.getLastName());
//            hkLoginDataBean.setUserCode(user.getUserCode());
//            hkLoginDataBean.setId(user.getId());
//            hkLoginDataBean.setEncryptedPassword(user.getPassword());
//            hkLoginDataBean.setUserId(username);
            if (user.getCompany() != null) {
                hkLoginDataBean.setCompanyId(user.getCompany());
            }

            int precedence = 3;
            int precedenceItr = 1;
            List<Long> roles = new ArrayList<>();
            Map<Long, String> roleIdNameMap = new HashMap<>();
            Boolean isHKAdmin = Boolean.FALSE;
            Boolean isFranchiseAdmin = Boolean.FALSE;
            if (!CollectionUtils.isEmpty(userRoleSet)) {
                for (SyncCenterUserRoleDocument uMUserRole : userRoleSet) {
                    if (WebSecurityNoSqlUtil.getHkAdminRole().equals(uMUserRole.getRoleId())) {
                        isHKAdmin = Boolean.TRUE;
                    }
                    if (uMUserRole.getIsActive() && !uMUserRole.getIsArchive()) {
                        roles.add(uMUserRole.getRoleId().longValue());
                        precedenceItr = applicationUtil.getPrecedence(uMUserRole.getRoleId().longValue());
                        if (precedenceItr == 1) {
                            isFranchiseAdmin = Boolean.TRUE;
                        }
                        if (precedenceItr < precedence) {
                            precedence = precedenceItr;
                        }
                    }
//                if (uMUserRole.getIsActive() && !uMUserRole.getIsArchive()) {
//                    roles.add(uMUserRole.getRoleId());
////                    precedenceItr = applicationUtil.getPrecedence(uMUserRole.getRoleId());
////                    if (precedenceItr < precedence) {
////                        precedence = precedenceItr;
////                    }
//                }
                }
            }
            if(!CollectionUtils.isEmpty(roles)){
                List<UmDesignationDocument> retrievedRoles = roleCenterService.retrieveRolesByIds(roles);
                for(UmDesignationDocument role : retrievedRoles){
                    roleIdNameMap.put(role.getId(), role.getName());
                }
            }
            hkLoginDataBean.setIsHKAdmin(isHKAdmin);
            hkLoginDataBean.setPrecedence(precedence);
            hkLoginDataBean.setServerOffsetInMin(Calendar.getInstance().getTimeZone().getRawOffset() / (1000 * 60));
            hkLoginDataBean.setRoleIds(roles);

            if (hkLoginDataBean.getId() == null) {
                Map<Long, String> result = new LinkedHashMap<>();
                String currentUser = "";
                if (user.getFirstName() != null) {
                    currentUser += user.getFirstName();
                }
                if (user.getLastName() != null) {
                    currentUser += user.getLastName();
                }
                result.put(user.getId(), currentUser);
                hkLoginDataBean.setReportsToUsers(retrieveReportsToUser(user.getId(), result));

                hkLoginDataBean.setProxyDepartment(user.getDepartmentId());
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
            hkLoginDataBean.setDepartment(user.getDepartmentId());
            hkLoginDataBean.setFirstName(user.getFirstName());
            hkLoginDataBean.setLastName(user.getLastName());
            hkLoginDataBean.setUserCode(user.getUserCode());
            hkLoginDataBean.setServerDate(new Date());
            hkLoginDataBean.setId(user.getId());
            hkLoginDataBean.setEncryptedPassword(user.getPassword());
            hkLoginDataBean.setUserId(username);
            hkLoginDataBean.setCompanyId(user.getCompany().longValue());

            hkLoginDataBean.setIsHKAdmin(isHKAdmin);
            hkLoginDataBean.setIsFranchiseAdmin(isFranchiseAdmin);
            hkLoginDataBean.setPrecedence(precedence);
            hkLoginDataBean.setServerOffsetInMin(Calendar.getInstance().getTimeZone().getRawOffset() / (1000 * 60));
            if(hkLoginDataBean.getCurrentDesignation() == null || !roles.contains(hkLoginDataBean.getCurrentDesignation())){
                hkLoginDataBean.setCurrentDesignation(roles.get(0));
            }
            hkLoginDataBean.setRoleIds(roles);
            hkLoginDataBean.setRoleIdNameMap(roleIdNameMap);
            hkLoginDataBean.setHasBg(false);
//            if (theme != null && theme.length() > 0) {
//                String[] split = theme.split(",");
//                if (split[0].indexOf(FolderManagement.UNIQUE_SEPARATOR) == -1) {
//                    hkLoginDataBean.setTheme(split[0]);
//                } else {
            hkLoginDataBean.setTheme("default");
//                }
//                for (String split1 : split) {
//                    if (split1.indexOf(FolderManagement.UNIQUE_SEPARATOR) != -1) {
//                        hkLoginDataBean.setHasBg(true);
//                    }
//                }
        } else {
            hkLoginDataBean.setTheme("default");
        }
        if (StringUtils.hasText(user.getPreferredLanguage())) {
            hkLoginDataBean.setPrefferedLang(user.getPreferredLanguage());
        }
//            employeeService.createUserOperation(HkUserOperationEnum.LOGIN, new Date(), hkLoginDataBean.getId(), null, null, hkLoginDataBean.getCompanyId());
    }

    public Map<Long, String> retrieveReportsToUser(Long id, Map<Long, String> result) throws GenericDatabaseException {
        String customString = "" + id + ":E";

        List<Criteria> criterias = new LinkedList<>();
        criterias.add(Criteria.where(SyncCenterUserService.CUSTOM2).is(customString));
        criterias.add(Criteria.where(SyncCenterUserService.IS_ARCHIVE).is(Boolean.FALSE));
        List<SyncCenterUserDocument> users = mongoGenericDao.findByCriteria(criterias, SyncCenterUserDocument.class);
        if (!CollectionUtils.isEmpty(users)) {
            for (SyncCenterUserDocument centerUserDocument : users) {
                result.put(centerUserDocument.getId(), centerUserDocument.getFirstName() + " " + centerUserDocument.getLastName());
            }
        } else {
            result = null;
        }
        return result;
    }
}
