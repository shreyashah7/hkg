package com.argusoft.hkg.web.config;

import com.argusoft.hkg.sync.center.core.impl.SyncCenterFranchiseServiceImpl;
import com.argusoft.hkg.web.util.PackageUtil;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 *
 * @author kelvin
 */
@Component
public class CustomComponentServiceFilter implements TypeFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(SyncCenterFranchiseServiceImpl.class);
    public static final String WEB_PACKAGE = "com.argusoft.hkg.web";
    public static final String CENTER_PACKAGE = "com.argusoft.hkg.web.center";
    public static final String SYNC_PACKAGE = "com.argusoft.hkg.sync.xmpp";

    @Override
    public boolean match(MetadataReader metadataReader, MetadataReaderFactory mrf) throws IOException {

        String className = metadataReader.getClassMetadata().getClassName();
        Boolean match = false;

//        PackageUtil.readPropertyFile();
        String packageName = "";
        try {
            Class<?> clazz = Class.forName(className);
            Package packageObject = clazz.getPackage();

            packageName = packageObject.getName();

//                LOGGER.debug("annotation.annotationType().getName()  " + annotation.annotationType().getName());
//                LOGGER.debug("annotation.annotationType().getTypeName() " + annotation.annotationType().getTypeName());
            if (packageName.startsWith(SYNC_PACKAGE)) {
                match = true;
            } else {
                if (clazz.isAnnotationPresent(Service.class) || clazz.isAnnotationPresent(Component.class)) {
                    if (PackageUtil.isSqlDatabase == Boolean.FALSE && PackageUtil.isNoSqlDatabase) {
                        if (packageName.startsWith(CENTER_PACKAGE)) {
                                                   match = true;
                        }
                    } else if (PackageUtil.isSqlDatabase && PackageUtil.isNoSqlDatabase) {
                        LOGGER.debug("MASTER PROFILE SERVICE SCAN");
                        if (packageName.startsWith(CENTER_PACKAGE) || packageName.startsWith(WEB_PACKAGE)) {
                            match = true;
                        }
                    }
                }
            }
        } catch (ClassNotFoundException ex) {
            LOGGER.error(null, ex);
        }

        return match;
    }

}
