package com.argusoft.hkg.web.security;

import com.argusoft.hkg.common.constantutil.HkSystemConstantUtil;
import com.argusoft.usermanagement.common.core.UMUserService;
import com.argusoft.usermanagement.common.model.UMUser;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

/**
 * Spring Security success handler, specialized for Ajax requests.
 */
public class AjaxAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication)
            throws IOException, ServletException {

//        LOGGER.debug("RequestContextHolder.currentRequestAttributes():::::::: " + RequestContextHolder.currentRequestAttributes());
//        LOGGER.debug("RequestContextHolder.currentRequestAttributes():::::::: " + SecurityContextHolder.getContext().getAuthentication());
//        Authentication authentication1 = SecurityContextHolder.getContext().getAuthentication();
//            SecurityContextHolder.getContext().setAuthentication(authentication1);
        LOGGER.debug("Authenticated..");

        //  -- Custom Second Level Authentication & Authorization Flow goes here ----
        String username = authentication.getName();      
        try {
            String ipAddress = request.getRemoteAddr();
            System.out.println("@@@@User Id " + username + " IP " + ipAddress);

            //  Custom Authentication for example ip based
        } catch (Exception e) {
            throw new AccessDeniedException(HkSystemConstantUtil.WebSecurityConstant.UNAUTHORIZED_ACCESS_MSG);
        }

        response.setStatus(HttpServletResponse.SC_OK);

    }
}
