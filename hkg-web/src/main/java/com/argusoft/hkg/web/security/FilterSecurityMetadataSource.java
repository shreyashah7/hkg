package com.argusoft.hkg.web.security;

import com.argusoft.hkg.common.UrlPatternKeyedMap;
import com.argusoft.hkg.web.util.ApplicationMasterInitializer;
import com.argusoft.usermanagement.common.constants.UMUserManagementConstants;
import java.util.Collection;
import java.util.List;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.util.AntPathMatcher;

/**
 *
 * @author kevlin
 */
public class FilterSecurityMetadataSource implements
        FilterInvocationSecurityMetadataSource {
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(FilterSecurityMetadataSource.class);
    @Autowired
    private ApplicationMasterInitializer applicationMasterInitializer;
    private final Boolean securityEnabled = true;

    @Override
    public List<ConfigAttribute> getAttributes(Object object) {

        List<ConfigAttribute> attributes;
        FilterInvocation filterInvocation = (FilterInvocation) object;
        String requestedUrl = filterInvocation.getRequestUrl();
        String httpMethod = filterInvocation.getRequest().getMethod();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        UrlPatternKeyedMap urlRoleMap = applicationMasterInitializer.getMapUrlRoles();
        AntPathMatcher antPathMatcher = new AntPathMatcher();

        boolean isApiResource = antPathMatcher.match("/api/**", requestedUrl);
        boolean isSecureResource = antPathMatcher.match("/views/secure/**", requestedUrl);
//        LOGGER.debug("FilterSecurityMetadataSource, requestedUrl=" + requestedUrl);
        String roles = "SYSTEM_ROLE";
        if (requestedUrl.equals("/api/centerfranchise/retrieve")) {
            roles = UMUserManagementConstants.IS_AUTHENTICATED_FULLY + ","
                    + UMUserManagementConstants.IS_AUTHENTICATED_REMEMBERED;
        } else {
            if (securityEnabled) {
                if (urlRoleMap != null) {
//                    LOGGER.debug("url role map not null");
                    Object keyRoles = urlRoleMap.get(requestedUrl);
//                    LOGGER.debug("keyRoles=" + keyRoles);
                    if (keyRoles != null) {
                        roles = keyRoles.toString();
                    }
                } else {
                    LOGGER.debug("url role map null" + requestedUrl);
                }

            } else {
                if (isApiResource || isSecureResource) {
                    roles = UMUserManagementConstants.IS_AUTHENTICATED_FULLY + ","
                            + UMUserManagementConstants.IS_AUTHENTICATED_REMEMBERED;
                } else {
                    roles = UMUserManagementConstants.IS_AUTHENTICATED_ANONYMOUSLY;
                }
            }
        }
        attributes = SecurityConfig.createListFromCommaDelimitedString(roles);
        return attributes;
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return FilterInvocation.class.isAssignableFrom(clazz);
    }

}
