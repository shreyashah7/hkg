/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.center.security;


import com.argusoft.hkg.common.constantutil.HkSystemConstantUtil;
import com.argusoft.hkg.sync.center.core.SyncCenterUserService;
import com.argusoft.sync.center.model.SyncCenterUserDocument;
import com.argusoft.sync.center.model.SyncCenterUserRoleDocument;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 *
 * @author shruti
 */
public class SpringSecurityNoSqlUserService implements UserDetailsService {

    @Autowired
    private SyncCenterUserService userCenterService;

    @Autowired
    private WebSecurityNoSqlUtil webSecurityNoSqlUtil;
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        LOGGER.debug("SpringSecurityNoSqlUserService loadUserByUsername username: " + username);
        if (webSecurityNoSqlUtil.getRoleFeatureDocument() == null) {
            webSecurityNoSqlUtil.init();
        }
        SyncCenterUserDocument user = userCenterService.getUserbyUserName(username, true, null);
//        LOGGER.debug("user=" + user);
        if (user != null) {
            List<SyncCenterUserRoleDocument> roles = user.getuMUserRoleSet();
            List<GrantedAuthority> authorities = new ArrayList<>();
            if (!CollectionUtils.isEmpty(roles)) {
                for (SyncCenterUserRoleDocument umUserRole : roles) {
                    if (umUserRole.getIsActive() && !umUserRole.getIsArchive()) {
                        SimpleGrantedAuthority authority;
                        authority = new SimpleGrantedAuthority(umUserRole.getRoleId().toString());
                        authorities.add(authority);
                    }
                }
            }

            boolean accountNonLocked = true;

            Date currentDate = new Date();
            if (StringUtils.hasText(user.getStatus()) && user.getStatus().trim().equalsIgnoreCase(HkSystemConstantUtil.EMPLOYEE_STATUS.RESIGNED.toString()) && (user.getExpiredOn() == null || user.getExpiredOn().before(currentDate))) {
                accountNonLocked = false;
            } else if (user.getJoiningDate() != null && user.getJoiningDate().after(currentDate)) {
                accountNonLocked = false;
            } else if (user.getCompany() != null && user.getCompany() != 0l) {
//                UMCompany uMCompany = uMCompanyService.retrieveCompanyByCompanyId(user.getCompany());
//                if (uMCompany == null || !uMCompany.getIsActive() || uMCompany.getIsArchive() || uMCompany.getStatus().trim().equalsIgnoreCase(HkSystemConstantUtil.INACTIVE) || uMCompany.getStatus().trim().equalsIgnoreCase(HkSystemConstantUtil.ARCHIVED)) {
//                    accountNonLocked = false;
//                }
            }
            if (!accountNonLocked) {
//                LOGGER.error(username + " : Account Locked.");
//                throw new DisabledException(HkSystemConstantUtil.WebSecurityConstant.ACCOUNT_DESABLE_MSG);
            }

            return new User(user.getUserId(), user.getPassword(), user.getIsActive(), true, true, accountNonLocked, authorities);
        } else {
            LOGGER.error(username + " : User not found");
            throw new BadCredentialsException(HkSystemConstantUtil.WebSecurityConstant.INVALID_USERNAME_PASS_MSG);
        }
    }

}
