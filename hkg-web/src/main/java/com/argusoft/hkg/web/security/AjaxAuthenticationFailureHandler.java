package com.argusoft.hkg.web.security;

import com.argusoft.hkg.common.constantutil.HkSystemConstantUtil;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InsufficientAuthenticationException;

/**
 * Returns a 401 error code (Unauthorized) to the client, when Ajax
 * authentication fails.
 *
 * @author kelvin
 */
public class AjaxAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {
        LOGGER.debug("Authentication failed on url " + request.getRequestURI());
//        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        String message;
        if (authException instanceof BadCredentialsException) {
            message = HkSystemConstantUtil.WebSecurityConstant.INVALID_USERNAME_PASS_MSG;
        } else if (authException instanceof DisabledException) {
            message = HkSystemConstantUtil.WebSecurityConstant.ACCOUNT_DESABLE_MSG;
        } else if (authException instanceof InsufficientAuthenticationException) {
            message = HkSystemConstantUtil.WebSecurityConstant.LOGIN_REQUIED_MSG;
        } else {
            message = authException.getMessage();
        }
        response.getWriter().print(message);
    }
}
