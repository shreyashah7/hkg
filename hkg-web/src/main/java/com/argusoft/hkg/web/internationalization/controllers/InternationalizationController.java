/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.internationalization.controllers;

import com.argusoft.hkg.common.applicationscopeobject.HkApplicationScopeObjects;
import com.argusoft.hkg.web.internationalization.databeans.CountryLanguageDataBean;
import com.argusoft.hkg.web.internationalization.databeans.LanguageDataBean;
import com.argusoft.hkg.web.internationalization.databeans.LocaleDataBean;
import com.argusoft.hkg.web.internationalization.transformers.LocalesTransformerBean;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author akta
 */
@RestController
@RequestMapping("/internationalization")
public class InternationalizationController {

    @Autowired
    LocalesTransformerBean internationalizationTransformer;

 /*   @RequestMapping(value = "/create/label", method = RequestMethod.POST, consumes = {"application/json"})
    public void createLabel(@RequestBody List<LocaleDataBean> listAddLabelDatabean, HttpServletRequest request) throws IOException {
        internationalizationTransformer.createBulkLocales(listAddLabelDatabean, request);
    }

    @RequestMapping(value = "/retrieve/constantmap", method = RequestMethod.GET)
    public Map<String, Object> retrieveConstant() {
        Map<String, Object> constantsMap = new HashMap();
        Map<String, Object> typesMap = new HashMap();
        Map<String, Object> envMap = new HashMap();
        List<Object> entityList = new ArrayList<>();
        typesMap.put("LM", "Label");
        typesMap.put("VM", "Validation");
        typesMap.put("MM", "Master");
        typesMap.put("CM", "Content");
        typesMap.put("NM", "Notification");        
        constantsMap.put("types", typesMap);
        envMap.put("W", "Web");
        envMap.put("M", "Mobile");
        constantsMap.put("environments", envMap);
        entityList.add("Manage Locales");
        constantsMap.put("entities", entityList);
        return constantsMap;
    }

    @RequestMapping(value = "/retrieve/languages", method = RequestMethod.GET, produces = {"application/json"})
    List<LanguageDataBean> retrieveAllLanguages() {
        return internationalizationTransformer.retrieveAllLanguages();
    }

    @RequestMapping(value = "/retrieve/pendingtranslationlabels", method = RequestMethod.POST, consumes = {"application/json"})
    public List<LocaleDataBean> retrievePendingTranslationLabels(@RequestBody LanguageDataBean languageDatabean) {
        return internationalizationTransformer.retrieveLabelsByLanguage(null, languageDatabean);
    }

    @RequestMapping(value = "/update/labeldetails", method = RequestMethod.POST, consumes = {"application/json"})
    public void updateLabelDetails(@RequestBody LocaleDataBean labelDataBean, HttpServletRequest request) throws IOException {
        internationalizationTransformer.updateLabelDetails(labelDataBean, request);
    }

    @RequestMapping(value = "/retrieve/countryandlanguage", method = RequestMethod.GET)
    public Map<String, Object> retrieveCountryLanguages() {
        Map<String, Object> langAndCountryMap = new HashMap();
        langAndCountryMap.put("countries", HkApplicationScopeObjects.I18N_COUNTRY_MAP);
        langAndCountryMap.put("languages", HkApplicationScopeObjects.I18N_LANGUAGE_MAP);
        return langAndCountryMap;
    }

    @RequestMapping(value = "/create/countryandlanguage", method = RequestMethod.POST, consumes = {"application/json"})
    public Map<String, Object> createCountryLanguage(@RequestBody CountryLanguageDataBean hKCountryLanguageDataBean) {
        Map<String, Object> statusmap = new HashMap();
        statusmap.put("status", internationalizationTransformer.createCountryLanguage(hKCountryLanguageDataBean));
        return statusmap;
    }

    @RequestMapping(value = "/copy/label", method = RequestMethod.GET)
    public void copyLabelToPropertyFile(HttpServletRequest request) throws FileNotFoundException, IOException {
        internationalizationTransformer.generateJsonFilesOfLabels(request);
    }

    @RequestMapping(value = "/delete/labeldetails", method = RequestMethod.POST, consumes = {"application/json"})
    public void deleteLabelDetails(@RequestBody LocaleDataBean labelDataBean) {
        internationalizationTransformer.deleteLabelDetails(labelDataBean);
    }*/
}
