package com.argusoft.hkg.web.security;

import com.argusoft.hkg.common.constantutil.HkSystemConstantUtil;
import com.argusoft.hkg.web.usermanagement.wrapper.UserManagementServiceWrapper;
import com.argusoft.usermanagement.common.core.UMCompanyService;
import com.argusoft.usermanagement.common.core.UMUserService;
import com.argusoft.usermanagement.common.model.UMCompany;
import com.argusoft.usermanagement.common.model.UMUser;
import com.argusoft.usermanagement.common.model.UMUserRole;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
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
 * @author kelvin
 */
public class SpringSecurityUserService implements UserDetailsService {

    @Autowired
    UMUserService umUserService;
    @Autowired
    UserManagementServiceWrapper userManagementServiceWrapper;
    @Autowired
    UMCompanyService uMCompanyService;
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UMUser user = umUserService.getUserbyUserName(username, false, true, false, false, false, null);

        if (user != null) {
            Set<UMUserRole> roles = user.getUMUserRoleSet();
            List<GrantedAuthority> authorities = new ArrayList<>();
            if (!CollectionUtils.isEmpty(roles)) {
                for (UMUserRole umUserRole : roles) {
                    if (umUserRole.getIsActive() && !umUserRole.getIsArchive()) {
                        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(Long.toString(umUserRole.getuMUserRolePK().getRole()));
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
                UMCompany uMCompany = uMCompanyService.retrieveCompanyByCompanyId(user.getCompany());
                if (uMCompany == null || !uMCompany.getIsActive() || uMCompany.getIsArchive() || uMCompany.getStatus().trim().equalsIgnoreCase(HkSystemConstantUtil.INACTIVE) || uMCompany.getStatus().trim().equalsIgnoreCase(HkSystemConstantUtil.ARCHIVED)) {
                    accountNonLocked = false;
                }
            }
            if (!accountNonLocked) {
                LOGGER.error(username + " : Account Locked.");
                throw new DisabledException(HkSystemConstantUtil.WebSecurityConstant.ACCOUNT_DESABLE_MSG);
            }

            return new User(user.getUserId(), user.getPassword(), user.getIsActive(), true, true, accountNonLocked, authorities);
        } else {
            LOGGER.error(username + " : User not found");
            throw new BadCredentialsException(HkSystemConstantUtil.WebSecurityConstant.INVALID_USERNAME_PASS_MSG);
        }
    }
}
