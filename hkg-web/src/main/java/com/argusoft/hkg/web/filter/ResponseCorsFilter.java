/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.filter;

/**
 *
 * @author shruti
 */
import com.argusoft.hkg.web.center.usermanagement.databeans.LoginDataBean;
import com.argusoft.hkg.web.config.WebApplicationInitializerConfig;
import com.google.inject.Singleton;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/*
 * Allow CORS requests.
 */
@Singleton
public class ResponseCorsFilter implements Filter {
    private static final Logger LOGGER = LoggerFactory.getLogger(ResponseCorsFilter.class);
    @Autowired
    private LoginDataBean loginDataBean;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }

    public LoginDataBean getLoginDataBean() {
        return loginDataBean;
    }

    public void setLoginDataBean(LoginDataBean loginDataBean) {
        this.loginDataBean = loginDataBean;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
//        LOGGER.debug("RESTCORSFILTER, url=" + ((HttpServletRequest) servletRequest).getRequestURL());
        if (servletResponse instanceof HttpServletResponse && servletRequest instanceof HttpServletRequest) {
            HttpServletResponse response = ((HttpServletResponse) servletResponse);
            HttpServletRequest request = (HttpServletRequest) servletRequest;
            if (request.getHeader("Origin") != null && !request.getHeader("Origin").startsWith(WebApplicationInitializerConfig.CURRENT_IP)) {
//                LOGGER.debug("(HttpServletRequest) servletRequest) " + request.getRequestURL());
//                LOGGER.debug("origin= " + request.getHeader("Origin"));
                if (request.getMethod().equals("OPTIONS")) {
                    response.setStatus(200);
//                    LOGGER.debug("after setstatus");
                    addHeadersFor200Response(response, request);
                    return;
                }
                addHeadersFor200Response(response, request);
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    private void addHeadersFor200Response(HttpServletResponse response, HttpServletRequest request) {

        response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));

        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "ACL, CANCELUPLOAD, CHECKIN, CHECKOUT, COPY, DELETE, GET, HEAD, LOCK, MKCALENDAR, MKCOL, MOVE, OPTIONS, POST, PROPFIND, PROPPATCH, PUT, REPORT, SEARCH, UNCHECKOUT, UNLOCK, UPDATE, VERSION-CONTROL");
        response.setHeader("Access-Control-Allow-Headers", "Overwrite, Destination,X-Auth-Token, Content-Type, Depth, User-Agent, Translate, Range, Content-Range, Timeout, X-File-Size, X-Requested-With, If-Modified-Since, X-File-Name, Cache-Control, Location, Lock-Token, If");
        response.setHeader("Access-Control-Expose-Headers", "DAV, content-length, Allow");
        response.setHeader("Access-Control-MAX-AGE", "36000");
    }
}
