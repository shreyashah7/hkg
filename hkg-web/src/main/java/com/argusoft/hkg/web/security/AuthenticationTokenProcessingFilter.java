package com.argusoft.hkg.web.security;

import com.argusoft.hkg.web.config.WebApplicationInitializerConfig;
import com.argusoft.hkg.web.util.TokenUtils;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

/**
 * TokenProcessingFilter which intercept incoming request, fetch the token from
 * request header and validates it with user details.
 *
 * @author rajkumar
 */
public class AuthenticationTokenProcessingFilter extends GenericFilterBean {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(AuthenticationTokenProcessingFilter.class);
    private final UserDetailsService userService;

    public AuthenticationTokenProcessingFilter(UserDetailsService userService) {
        this.userService = userService;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
            ServletException,
            UnsupportedEncodingException {
        AntPathMatcher antPathMatcher = new AntPathMatcher();

        HttpServletRequest httpRequest = this.getAsHttpRequest(request);
        String path = httpRequest.getRequestURI().substring(httpRequest.getContextPath().length());
        boolean isCommonResource = antPathMatcher.match("/api/common/**", path);
        boolean isSessionResource = antPathMatcher.match("/api/common/getsession", path);
        boolean isUserOperationAftLogout = antPathMatcher.match("/api/common/adduseroperationbeforelogout", path);
        boolean isRetrieveFranchise = antPathMatcher.match("/api/centerfranchise/retrieve", path);
        boolean isRetrieveLocale = antPathMatcher.match("/api/init/retrieve/localefile", path);
        boolean isRetrieveAvtar = antPathMatcher.match("/api/init/retrieve/avtar", path);
        boolean isDeployServer = antPathMatcher.match("/api/sync/deployserver", path);
        boolean isTransactionLog = antPathMatcher.match("/api/transactionlog/**", path);
        boolean isApiResource = antPathMatcher.match("/api/**", path);
        boolean isSecureResource = antPathMatcher.match("/views/secure**", path);

        boolean isFileUploadResource = antPathMatcher.match("/api/fileUpload/uploadFile**", path);
        boolean isEventResource = antPathMatcher.match("/api/event/getbackgroundimage**", path);
        boolean isRuleResource = antPathMatcher.match("/api/executerule/postrule", path);
//        boolean isPriceListFileUploadResource = antPathMatcher.match("/api/pricelist/uploadpricelist**", path);

        String authToken = this.extractAuthTokenFromRequest(httpRequest);
        String userName = TokenUtils.getUserNameFromToken(authToken);

        //        LOGGER.debug("path=" + path);
        //        LOGGER.debug(path + "==" + ((isSessionResource || !isCommonResource || isUserOperationAftLogout) && !isRetrieveAvtar && !isRetrieveLocale && !isFileUploadResource && !isRetrieveFranchise && !isDeployServer) + "   " + (isApiResource || isSecureResource) + "   " + (userName != null && !StringUtils.isEmpty(userName) && !userName.equals("null")) + "AuthenticationTokenProcessingFilter dofilter   Authtoken= " + authToken + " userName " + userName
//        );

        //If api is for common stuff or file upload then bypass them
        if ((isSessionResource || !isCommonResource || isUserOperationAftLogout) && !isTransactionLog && !isFileUploadResource && !isRetrieveFranchise && !isDeployServer && !isRetrieveLocale && !isRetrieveAvtar && !isEventResource && !isRuleResource) {

            //Secure only /api/** and /secure/views/** resources
            if (isApiResource || isSecureResource) {
                //                LOGGER.debug(path + "==" + "AuthenticationTokenProcessingFilter isApiResource || isSecureResource" + userName + "  auth token: " + authToken);
                if (userName != null && !StringUtils.isEmpty(userName) && !userName.equals("null")) {
//                    LOGGER.debug(path + "==" + "this.userService.loadUserByUsername(userName) " + this.userService.loadUserByUsername(userName).getAuthorities());
                    //Fetch user details by username retrieved from AuthenticationToken
                    UserDetails userDetails = this.userService.loadUserByUsername(userName);
//                    LOGGER.debug("1. userdetails= " + userDetails.toString());
                    //Validate token
                    String magicKey;
                    if (httpRequest.getRequestURL() != null && !httpRequest.getRequestURL().toString().startsWith(WebApplicationInitializerConfig.CURRENT_IP)) {
                        magicKey = TokenUtils.MAGIC_KEY_MASTER;
                    } else {
                        magicKey = TokenUtils.MAGIC_KEY_CENTER;
                    }
//                    LOGGER.debug("magic key= " + magicKey + "2. TokenUtils.validateToken(authToken, userDetails) " + TokenUtils.validateToken(authToken, userDetails, magicKey));
                    if (TokenUtils.validateToken(authToken, userDetails, magicKey)) {

                        UsernamePasswordAuthenticationToken authentication
                                = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//                        LOGGER.debug("3. AuthenticationTokenProcessingFilter AUTHENTICATION: " + authentication.getPrincipal());
//                        LOGGER.debug("4. new WebAuthenticationDetailsSource().buildDetails(httpRequest) " + new WebAuthenticationDetailsSource().buildDetails(httpRequest).toString());
                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpRequest));
//                        LOGGER.debug("5. authentication.setDetails");
                        SecurityContextHolder.getContext().setAuthentication(authentication);
//                        LOGGER.debug("6. SecurityContextHolder.getContext()");
                        chain.doFilter(request, response);
                    } else {
                        //Return status 401 if token is not valid
//                        LOGGER.debug("7. AuthenticationTokenProcessingFilter SENDING UNAUTHORIZED");
                        ((HttpServletResponse) response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    }
                } else {
                    //Return status 401 if username in token is empty
                    ((HttpServletResponse) response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//                    chain.doFilter(request, response);
                }
            } else {
                chain.doFilter(request, response);
            }
        } else {
            chain.doFilter(request, response);
        }

    }

    private HttpServletRequest getAsHttpRequest(ServletRequest request) {
        if (!(request instanceof HttpServletRequest)) {
            throw new RuntimeException("Expecting an HTTP request");
        }

        return (HttpServletRequest) request;
    }

    private String extractAuthTokenFromRequest(HttpServletRequest httpRequest) {
        /*
         * Get token from header
         */
        String authToken = httpRequest.getHeader("X-Auth-Token");

        /*
         * If token not found get it from request parameter
         */
        if (authToken == null) {
            authToken = httpRequest.getParameter("token");
        }

        return authToken;
    }
}
