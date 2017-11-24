package com.argusoft.hkg.common.applicationscopeobject;

import com.argusoft.internationalization.common.core.I18nService;
import com.argusoft.internationalization.common.model.I18nCountryEntity;
import com.argusoft.internationalization.common.model.I18nLanguageEntity;
import com.argusoft.internationalization.common.model.I18nLanguagePKEntity;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author akta
 */
@Service
public class HkApplicationScopeObjects {

    public static Map<String, Object> TYPE_MAP = new HashMap();
    public static Map<String, Object> ENVIRONMENT_MAP = new HashMap();
    public static List<Object> ENTITY_LIST = new ArrayList();
    public static Map<String, Object> I18N_LANGUAGE_MAP = new HashMap();
    public static Map<String, Object> I18N_COUNTRY_MAP = new HashMap();
    @Autowired
    private I18nService i18nService;
    private static final Logger LOGGER = Logger.getLogger(HkApplicationScopeObjects.class.getName());

    public HkApplicationScopeObjects() {
    }

    @PostConstruct
    public void init() {
        LOGGER.log(Level.INFO, "Initializing sync component's type,environment,entity list map");
        Properties internationalization_prop = new Properties();
        try {
            internationalization_prop.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("internationalization.properties"));
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
        Set<String> setKeys = internationalization_prop.stringPropertyNames();
        Iterator it = setKeys.iterator();
        while (it.hasNext()) {
            String current_key = it.next().toString();
            String current_element = current_key.substring(current_key.lastIndexOf("."));
            if (current_key.startsWith("type.key.")) {
                String type_key = internationalization_prop.getProperty(current_key);
                String type_value = internationalization_prop.getProperty("type.value" + current_element);
                TYPE_MAP.put(type_key, type_value);
            } else if (current_key.startsWith("environment.key.")) {
                String env_key = internationalization_prop.getProperty(current_key);
                String env_value = internationalization_prop.getProperty("environment.value" + current_element);
                ENVIRONMENT_MAP.put(env_key, env_value);
            } else if (current_key.startsWith("entity.")) {
                ENTITY_LIST.add(internationalization_prop.getProperty(current_key));
            }
        }

        LOGGER.log(Level.INFO, "Initializing Internationalization Language map and country map");
        I18N_COUNTRY_MAP.put("IN", "India");
        I18N_COUNTRY_MAP.put("US", "United States");
        I18N_COUNTRY_MAP.put("CN", "China");
        I18N_COUNTRY_MAP.put("BE", "Belgium");

        I18N_LANGUAGE_MAP.put("gu", "Gujarati");
        I18N_LANGUAGE_MAP.put("hi", "Hindi");
        I18N_LANGUAGE_MAP.put("en", "English");

        //code to insert india,us in internationalization country and english,hindi,gujarati in language_master
        List<I18nCountryEntity> listi18nCountry = i18nService.retriveActiveCountries();
        Map<String, String> countrymap = new HashMap();
        for (I18nCountryEntity i18nCountryEntity : listi18nCountry) {
            countrymap.put(i18nCountryEntity.getCode(), i18nCountryEntity.getName());
        }
        I18nCountryEntity i18nCountryEntityIN = new I18nCountryEntity();
        i18nCountryEntityIN.setCode("IN");
        i18nCountryEntityIN.setName("India");
        i18nCountryEntityIN.setCreatedBy(1L);
        i18nCountryEntityIN.setCreatedOn(Calendar.getInstance().getTime());
        if (countrymap.get("IN") == null) {
            i18nService.addCountry(i18nCountryEntityIN);
        }
        I18nCountryEntity i18nCountryEntityUS = new I18nCountryEntity();
        i18nCountryEntityUS.setCode("US");
        i18nCountryEntityUS.setName("United States");
        i18nCountryEntityUS.setCreatedBy(1L);
        i18nCountryEntityUS.setCreatedOn(Calendar.getInstance().getTime());
        if (countrymap.get("US") == null) {
            i18nService.addCountry(i18nCountryEntityUS);
        }

        I18nLanguageEntity i18nLanguageEntityGU = new I18nLanguageEntity();
        i18nLanguageEntityGU.setCharacterEncoding("UTF-8");
        i18nLanguageEntityGU.setCountry1(i18nCountryEntityIN);
        i18nLanguageEntityGU.setCreatedBy(1L);
        i18nLanguageEntityGU.setCreatedOn(Calendar.getInstance().getTime());
        i18nLanguageEntityGU.setIsLeftToRight(true);
        i18nLanguageEntityGU.setName("Gujarati");
        i18nLanguageEntityGU.setLanguagePK(new I18nLanguagePKEntity("GU", "IN"));

        I18nLanguageEntity i18nLanguageEntityHI = new I18nLanguageEntity();
        i18nLanguageEntityHI.setCharacterEncoding("UTF-8");
        i18nLanguageEntityHI.setCountry1(i18nCountryEntityIN);
        i18nLanguageEntityHI.setCreatedBy(1L);
        i18nLanguageEntityHI.setCreatedOn(Calendar.getInstance().getTime());
        i18nLanguageEntityHI.setIsLeftToRight(true);
        i18nLanguageEntityHI.setName("Hindi");
        i18nLanguageEntityHI.setLanguagePK(new I18nLanguagePKEntity("HI", "IN"));

        I18nLanguageEntity i18nLanguageEntityEN = new I18nLanguageEntity();
        i18nLanguageEntityEN.setCharacterEncoding("UTF-8");
        i18nLanguageEntityEN.setCountry1(i18nCountryEntityUS);
        i18nLanguageEntityEN.setCreatedBy(1L);
        i18nLanguageEntityEN.setCreatedOn(Calendar.getInstance().getTime());
        i18nLanguageEntityEN.setIsLeftToRight(true);
        i18nLanguageEntityEN.setName("English");
        i18nLanguageEntityEN.setLanguagePK(new I18nLanguagePKEntity("EN", "US"));

        List<I18nLanguageEntity> listActiveLang = i18nService.retriveActiveLanguages();
        List<I18nLanguagePKEntity> listlangPK = new ArrayList();
        for (I18nLanguageEntity i18nLanguageEntity : listActiveLang) {
            listlangPK.add(i18nLanguageEntity.getLanguagePK());
        }
        if (!listlangPK.contains(new I18nLanguagePKEntity("GU", "IN"))) {
            i18nService.addLanguage(i18nLanguageEntityGU);
        }
        if (!listlangPK.contains(new I18nLanguagePKEntity("HI", "IN"))) {
            i18nService.addLanguage(i18nLanguageEntityHI);
        }
        if (!listlangPK.contains(new I18nLanguagePKEntity("EN", "US"))) {
            i18nService.addLanguage(i18nLanguageEntityEN);
        }
    }

}
