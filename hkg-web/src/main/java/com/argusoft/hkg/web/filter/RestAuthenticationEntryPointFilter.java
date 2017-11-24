package com.argusoft.hkg.web.filter;

import com.argusoft.hkg.common.constantutil.HkSystemConstantUtil;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.util.StringUtils;

/**
 * Just return 401-unauthorized for every unauthorized request. The client side
 * catches this and handles login itself. called while without login you have
 * accessed any url
 */
public class RestAuthenticationEntryPointFilter implements AuthenticationEntryPoint {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    /**
     *
     * @param request
     * @param response
     * @param authException
     * @throws IOException
     */
    @Override
    public final void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        String message = null;
        if (authException instanceof BadCredentialsException) {
            message = HkSystemConstantUtil.WebSecurityConstant.INVALID_USERNAME_PASS_MSG;
        } else if (authException instanceof DisabledException) {
            message = HkSystemConstantUtil.WebSecurityConstant.ACCOUNT_DESABLE_MSG;
        } else if (authException instanceof InsufficientAuthenticationException) {
            //message = HkSystemConstantUtil.WebSecurityConstant.LOGIN_REQUIED_MSG;
        } else {
            message = HkSystemConstantUtil.WebSecurityConstant.UNAUTHORIZED_ACCESS_MSG;
        }
        if(StringUtils.hasText(message)) {
            response.getWriter().print(message);
        }
    }
}
