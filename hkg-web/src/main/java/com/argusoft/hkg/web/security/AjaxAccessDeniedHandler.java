package com.argusoft.hkg.web.security;

import com.argusoft.hkg.common.constantutil.HkSystemConstantUtil;
import java.io.IOException;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.csrf.DefaultCsrfToken;

/**
 * Spring Security Access Denied handler, specialized for Ajax requests.
 */
/**
 *
 * @author alpesh
 */
public class AjaxAccessDeniedHandler implements AccessDeniedHandler {

    private final static Logger LOGGER = LoggerFactory.getLogger(AjaxAccessDeniedHandler.class);

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException deniedException) throws IOException, ServletException {
        LOGGER.debug("Access denied on url " + request.getRequestURI());
//        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
//        response.setContentType("applicaton/json");
        HttpSession session = request.getSession();
        LOGGER.debug("Session is " + session);
        LOGGER.debug("Session id = " + session.getId());
        LOGGER.debug("Session max interval=" + session.getMaxInactiveInterval());
        LOGGER.debug("Session last used=" + session.getLastAccessedTime());
        LOGGER.debug("Time now=" + new Date().getTime());
        LOGGER.debug("csrf:");
        Object csrf = request.getAttribute("_csrf");

        if (csrf == null) {
            LOGGER.debug("csrf is null");
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().print(HkSystemConstantUtil.WebSecurityConstant.ACCESS_DENIED_MSG);
        } else {
            LOGGER.debug(csrf.toString());
            if (csrf instanceof DefaultCsrfToken) {
                response.setStatus(HttpServletResponse.SC_OK);
                DefaultCsrfToken token = (DefaultCsrfToken) csrf;
                LOGGER.debug("Parm name " + token.getParameterName());
                LOGGER.debug("Token " + token.getToken());
            }

        }
        
        LOGGER.debug("Request:");
        LOGGER.debug(request.toString());
       
        LOGGER.debug("Response:");
        LOGGER.debug(response.toString());
       
        LOGGER.debug("Exception:");
        LOGGER.debug(deniedException.toString());
    }

}
