package com.argusoft.hkg.web.config;

import com.argusoft.hkg.web.center.config.WebApplicationNoSqlConfig;
import com.argusoft.hkg.web.filter.NoCacheFilter;
import com.argusoft.hkg.web.filter.ResponseCorsFilter;
import static java.lang.Boolean.FALSE;
import java.util.EnumSet;
import java.util.ResourceBundle;
import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.DispatcherServlet;

/**
 *
 * @author satyajit
 */
public class WebApplicationInitializerConfig implements WebApplicationInitializer {

    private static final String API_MAPPING_URL = "/api/*";
    private static final String SPRING_SECURITY_LOGOUT_URL = "/j_spring_security_logout";
    public static String projectDirectory = "";
    public static Boolean IS_MASTER = null;
    public static String CURRENT_IP = null;
    public static String MASTER_IP = null;
    public static String XMPP_HOSTNAME = null;
    public static String XMPP_DOMAIN = null;
    public static int XMPP_PORT = 5222;
//    public static Boolean IS_DEVELOPERPROFILE = true;
    public static int PER_CORE_THREADS;

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        WebApplicationContext context = getContext();
        projectDirectory = servletContext.getRealPath("i18n/");
        servletContext.addListener(new ContextLoaderListener(context));
        registerCorsFilter(servletContext);
        configureSpringSecurityFilter(servletContext);
        configureNoCacheFilter(servletContext);
        configureAPIDispatcherServlet(servletContext);
        registerConcurrentSessionControlListener(servletContext);
        ResourceBundle serverPropertiesBundle = ResourceBundle.getBundle("server");
        CURRENT_IP = serverPropertiesBundle.getString("servers.current.ip");
        XMPP_HOSTNAME = serverPropertiesBundle.getString("servers.xmpp.hostname");
        XMPP_DOMAIN = serverPropertiesBundle.getString("servers.xmpp.domainname");
        XMPP_PORT = Integer.parseInt(serverPropertiesBundle.getString("servers.xmpp.port"));
        if (serverPropertiesBundle.getString("servers.ismaster").equalsIgnoreCase("TRUE")) {
            IS_MASTER = Boolean.TRUE;
            MASTER_IP = serverPropertiesBundle.getString("servers.master.ip");
        } else {
            IS_MASTER = FALSE;
            MASTER_IP = serverPropertiesBundle.getString("servers.master.ip");
        }
    }

    private AnnotationConfigWebApplicationContext getContext() {
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        ResourceBundle serverPropertiesBundle = ResourceBundle.getBundle("server");
        CURRENT_IP = serverPropertiesBundle.getString("servers.current.ip");
        XMPP_HOSTNAME = serverPropertiesBundle.getString("servers.xmpp.hostname");
        XMPP_DOMAIN = serverPropertiesBundle.getString("servers.xmpp.domainname");
        XMPP_PORT = Integer.parseInt(serverPropertiesBundle.getString("servers.xmpp.port"));
//        IS_DEVELOPERPROFILE = valueOf(serverPropertiesBundle.getString("profile.isDeveloperProfile"));
        PER_CORE_THREADS = Integer.parseInt(serverPropertiesBundle.getString("server.sync.percorethreads"));
        if (serverPropertiesBundle.getString("servers.ismaster").equalsIgnoreCase("TRUE")) {
            IS_MASTER = Boolean.TRUE;
            MASTER_IP = serverPropertiesBundle.getString("servers.master.ip");
        } else {
            IS_MASTER = FALSE;
        }
        if (IS_MASTER) {
            context.register(WebApplicationConfig.class);
        } else {
            context.register(WebApplicationNoSqlConfig.class);
        }
        return context;
    }

    private void registerCorsFilter(ServletContext sc) {
        ResponseCorsFilter corsFilter = new ResponseCorsFilter();
        FilterRegistration fr = sc.addFilter("corsFilter", corsFilter);
        fr.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD), true, API_MAPPING_URL, SPRING_SECURITY_LOGOUT_URL);
    }

    private void configureNoCacheFilter(ServletContext sc) {
        //cache filter which will not cache json data in IE
        NoCacheFilter noCacheFilter = new NoCacheFilter();
        FilterRegistration fr = sc.addFilter("noCacheFilter", noCacheFilter);
        fr.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD), true, API_MAPPING_URL);
    }

    private void configureSpringSecurityFilter(ServletContext sc) {
        // spring security
        DelegatingFilterProxy delegatingFilterProxy = new DelegatingFilterProxy("springSecurityFilterChain");
        FilterRegistration fr = sc.addFilter("springSecurityFilterChain", delegatingFilterProxy);
        fr.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD), true, "/*");
    }

    private void configureAPIDispatcherServlet(ServletContext servletContext) {
        //registers api spring dispatcher servlet
        AnnotationConfigWebApplicationContext dispatcherContext = new AnnotationConfigWebApplicationContext();
        dispatcherContext.register(DispatcherConfig.class);

        DispatcherServlet dispatcherServlet = new DispatcherServlet(dispatcherContext);
        ServletRegistration.Dynamic dispatcher = servletContext.addServlet("api", dispatcherServlet);
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping(API_MAPPING_URL);
    }

    private void registerConcurrentSessionControlListener(ServletContext servletContext) {
        servletContext.addListener(HttpSessionEventPublisher.class);
    }
}
