/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.internationalization.controllers;

import com.argusoft.hkg.common.constantutil.HkSystemConstantUtil;
import com.argusoft.hkg.web.base.PrimaryKey;
import com.argusoft.hkg.web.base.ResponseCode;
import com.argusoft.hkg.web.base.ResponseEntity;
import com.argusoft.hkg.web.base.controllers.BaseController;
import com.argusoft.hkg.web.common.databeans.DatatableDataBean;
import com.argusoft.hkg.web.internationalization.databeans.LanguageDataBean;
import com.argusoft.hkg.web.internationalization.databeans.LocaleDataBean;
import com.argusoft.hkg.web.internationalization.databeans.SearchBeanForLabel;
import com.argusoft.hkg.web.internationalization.transformers.LocalesTransformerBean;
import com.argusoft.hkg.web.center.usermanagement.databeans.LoginDataBean;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author satyam
 */
@RestController
@RequestMapping("/locale")
public class LocalesController extends BaseController<LocaleDataBean, Long> {

    private static final Logger LOGGER = LoggerFactory.getLogger(LocalesController.class);

    @Autowired
    LocalesTransformerBean localesTransformerBean;
    @Autowired
    LoginDataBean loginDataBean;

    @Override
    public ResponseEntity<List<LocaleDataBean>> retrieveAll() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ResponseEntity<LocaleDataBean> retrieveById(PrimaryKey<Long> primaryKey) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ResponseEntity<PrimaryKey<Long>> update(LocaleDataBean t) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ResponseEntity<PrimaryKey<Long>> create(LocaleDataBean t) {
        return null;
        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ResponseEntity<LocaleDataBean> deleteById(PrimaryKey<Long> primaryKey) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @RequestMapping(value = "/createall", method = RequestMethod.POST, consumes = {"application/json"})
    public ResponseEntity<LocaleDataBean> createAll(@RequestBody List<LocaleDataBean> locales, HttpServletRequest request) {
        try {
            localesTransformerBean.createBulkLocales(locales, request);
            return new ResponseEntity<>(null, ResponseCode.SUCCESS, "Locales created", null);
        } catch (Exception e) {
            LOGGER.error("Locales could not created");
            return new ResponseEntity<>(null, ResponseCode.FAILURE, "Locales could not created", null);
        }
    }

    @RequestMapping(value = "/createlabel", method = RequestMethod.POST, consumes = {"application/json"})
    public ResponseEntity<LocaleDataBean> createLabelFromDirective(@RequestBody LocaleDataBean locales, HttpServletRequest request) {
        try {
            if (locales != null) {
                if (locales.getEntity() != null && locales.getEntity().equals("MSG.")) {
                    locales.setType("MESSAGE");
                }
            }

            localesTransformerBean.createLabelFromDirective(locales, request);
            return null;//new ResponseEntity<>(null, ResponseCode.SUCCESS, "Locales created", null);
        } catch (Exception e) {
            LOGGER.error("Locales could not created");
            return null;//new ResponseEntity<>(null, ResponseCode.FAILURE, "Locales could not created", null);
        }
    }

    @RequestMapping(value = "/retrieve/languages", method = RequestMethod.GET, produces = {"application/json"})
    public ResponseEntity<List<LanguageDataBean>> retrieveAllLanguages() {
        try {
            List<LanguageDataBean> retrieveAllLanguages = localesTransformerBean.retrieveAllLanguages();
            return new ResponseEntity<>(retrieveAllLanguages, ResponseCode.SUCCESS, "", null);
        } catch (Exception e) {
            LOGGER.error("Language could not retrieved", e);
            return new ResponseEntity<>(null, ResponseCode.FAILURE, "Language could not retrieved", null);
        }
    }

    @RequestMapping(value = "/retrieveasperscroll", method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity<List<LocaleDataBean>> retrieveAll(@RequestBody DatatableDataBean<Map> tableConfig) {
        try {
            LOGGER.info("Table configration of scrollable table : " + tableConfig);
            return new ResponseEntity<>(localesTransformerBean.retrieveLabelsByPage(tableConfig), ResponseCode.SUCCESS, null, null);
        } catch (Exception e) {
            LOGGER.info("Table configration of scrollable table : " + e);
            return new ResponseEntity<>(null, ResponseCode.FAILURE, "Language could not retrieved", null);
        }
    }

    @RequestMapping(value = "/retrieve/bysearchfields", method = RequestMethod.POST)
    ResponseEntity<List<LocaleDataBean>> retrieveLabelsBySearchFields(@RequestBody SearchBeanForLabel searchbean) {
        try {
            LOGGER.info("Table configration of scrollable table : " + searchbean);
            return new ResponseEntity<>(localesTransformerBean.retrieveLabelsBySearchCriteria(searchbean), ResponseCode.SUCCESS, null, null);
        } catch (Exception e) {
            LOGGER.info("Table configration of scrollable table : " + e);
            return new ResponseEntity<>(null, ResponseCode.FAILURE, "Language could not retrieved", null);
        }
    }

    @RequestMapping(value = "/updateall", method = RequestMethod.POST, consumes = {"application/json"})
    public ResponseEntity<LocaleDataBean> updateAll(@RequestBody List<LocaleDataBean> locales, HttpServletRequest request) {
        try {
            localesTransformerBean.updateBulkLocales(locales, request);
            return new ResponseEntity<>(null, ResponseCode.SUCCESS, "Translation saved", null);
        } catch (Exception e) {
            LOGGER.error("Translation could not be saved, please try again");
            return new ResponseEntity<>(null, ResponseCode.FAILURE, "Translation could not be saved, please try again", null);
        }
    }

    @Override
    public ResponseEntity<Map<String, Object>> retrievePrerequisite() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @RequestMapping(value = "/retrieveContentAsPerScroll", method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity<List<LocaleDataBean>> retrieveContentAsPerScroll(@RequestBody DatatableDataBean<Map> tableConfig) {
        try {
            LOGGER.info("Table configration of scrollable table : " + tableConfig);
            return new ResponseEntity<>(localesTransformerBean.retrieveLabelsByPage(tableConfig), ResponseCode.SUCCESS, null, null);
        } catch (Exception e) {
            LOGGER.info("Table configration of scrollable table : " + e);
            return new ResponseEntity<>(null, ResponseCode.FAILURE, "Language could not retrieved", null);
        }
    }

    @RequestMapping(value = "/retrieveContentTypeList", method = RequestMethod.POST)
    public ResponseEntity<Map<String, List<String>>> retrieveContentTypeList() {
        Map<String, List<String>> contentTypeMap = new HashMap<>();
        contentTypeMap.put("contentlist", HkSystemConstantUtil.I18N_CONTENT_TYPE_LIST);
        return new ResponseEntity<>(contentTypeMap, ResponseCode.SUCCESS, null, null);
    }

    @RequestMapping(value = "/initLabelMaster", method = RequestMethod.GET)
    public void initLocale() {
        localesTransformerBean.initLocales();
    }

}
