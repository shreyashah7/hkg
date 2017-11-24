package com.argusoft.hkg.web.config;

import com.argusoft.hkg.web.util.PackageUtil;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author kelvin
 */
@Component
public class CustomControllerFilter implements TypeFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomControllerFilter.class);
    public static final String WEB_PACKAGE = "com.argusoft.hkg.web";
    public static final String CENTER_PACKAGE = "com.argusoft.hkg.web.center";
    public static final String MASTER_CENTER_COMMON_PACKAGE = "com.argusoft.hkg.web.common";

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
//            Annotation[] annotations = clazz.getAnnotations();
//            for (int i = 0; i < annotations.length; i++) {
//                Annotation annotation = annotations[i];
//
//                LOGGER.debug("annotation.annotationType().getName()  " + annotation.annotationType().getName()
//                );
////                LOGGER.debug("annotation.annotationType().getTypeName() " + annotation.annotationType().getTypeName());
            if (clazz.isAnnotationPresent(RestController.class) || clazz.isAnnotationPresent(Controller.class) || clazz.isAnnotationPresent(ControllerAdvice.class)) {
                if (packageName.startsWith(MASTER_CENTER_COMMON_PACKAGE)) {
                    match = true;
                } else if (PackageUtil.isSqlDatabase == Boolean.FALSE && PackageUtil.isNoSqlDatabase) {
                    if (packageName.startsWith(CENTER_PACKAGE)) {
                        match = true;
                    }
                } else if (PackageUtil.isSqlDatabase && PackageUtil.isNoSqlDatabase) {
                    LOGGER.debug("MASTER PROFILE");
                    if (!packageName.startsWith(CENTER_PACKAGE) && packageName.startsWith(WEB_PACKAGE)) {
                        match = true;
                    }
                } 
//                    break;
            }
//            }

        } catch (ClassNotFoundException ex) {
            LOGGER.error(null, ex);
        }

        return match;
    }

}
