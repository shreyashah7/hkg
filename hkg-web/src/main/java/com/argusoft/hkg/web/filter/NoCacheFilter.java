package com.argusoft.hkg.web.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Completely disable browser caching.
 *
 * @author Satyajit
 */
public class NoCacheFilter implements Filter {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    /**
     * Place this filter into service.
     *
     * @param filterConfig the filter configuration object used by a servlet
     * container to pass information to a filter during initialization
     * @throws ServletException to inform the container to not place the filter
     * into service
     */
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    /**
     * Set cache header directives.
     *
     * @param servletRequest provides data including parameter name and values,
     * attributes, and an input stream
     * @param servletResponse assists a servlet in sending a response to the
     * client
     * @param filterChain gives a view into the invocation chain of a filtered
     * request
     * @throws java.io.IOException
     * @throws javax.servlet.ServletException
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;

        String requestUrl = httpServletRequest.getRequestURL().toString();
        // set cache directives
        httpServletResponse.setHeader(HTTPCacheHeaderFilter.CACHE_CONTROL.getName(), "no-cache");
        httpServletResponse.setHeader(HTTPCacheHeaderFilter.PRAGMA.getName(), "no-cache");
        httpServletResponse.setDateHeader(HTTPCacheHeaderFilter.EXPIRES.getName(), -1);
        filterChain.doFilter(servletRequest, servletResponse);
    }

    /**
     * Take this filter out of service.
     */
    public void destroy() {
    }
}
