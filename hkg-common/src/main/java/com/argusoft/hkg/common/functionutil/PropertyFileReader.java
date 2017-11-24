package com.argusoft.hkg.common.functionutil;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.stereotype.Service;

/**
 *
 * @author piyush
 */
@Service
public class PropertyFileReader {

    /**
     * This method reads property value from property file by property key
     *
     *
     * @param propertyKey
     * @param propertyFilename
     * @return property value
     */
    public String getPropertyValue(String propertyKey, String propertyFilename) {
        String keyVal = "";
        try {
            Logger.getLogger(PropertyFileReader.class.getName()).log(Level.INFO, "In getPropertyValue method.......");
            Properties properties = new Properties();
            InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(propertyFilename);
            if (inputStream == null) {
                throw new FileNotFoundException("property file '" + propertyFilename + "' not found in the classpath");
            }
            properties.load(inputStream);
            keyVal = properties.getProperty(propertyKey);
        } catch (FileNotFoundException fileNotFoundException) {
            Logger.getLogger(PropertyFileReader.class.getName()).log(Level.INFO, fileNotFoundException.getMessage());
        } catch (IOException iOException) {
            Logger.getLogger(PropertyFileReader.class.getName()).log(Level.INFO, iOException.getMessage());
        }
        return keyVal;
    }
}
