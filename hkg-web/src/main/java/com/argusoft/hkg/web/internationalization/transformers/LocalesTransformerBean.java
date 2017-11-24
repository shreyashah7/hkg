/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.internationalization.transformers;

import com.argusoft.hkg.common.applicationscopeobject.HkApplicationScopeObjects;
import com.argusoft.hkg.common.constantutil.HkSystemConstantUtil;
import com.argusoft.hkg.web.common.databeans.DatatableDataBean;
import com.argusoft.hkg.web.internationalization.LabelType;
import com.argusoft.hkg.web.internationalization.databeans.CountryLanguageDataBean;
import com.argusoft.hkg.web.internationalization.databeans.LanguageDataBean;
import com.argusoft.hkg.web.internationalization.databeans.LocaleDataBean;
import com.argusoft.hkg.web.internationalization.databeans.SearchBeanForLabel;
import com.argusoft.hkg.web.internationalization.wrapper.InternationalizationServiceWrapper;
import com.argusoft.hkg.web.center.usermanagement.databeans.LoginDataBean;
import com.argusoft.hkg.web.config.WebApplicationInitializerConfig;
import com.argusoft.hkg.web.usermanagement.wrapper.UserManagementServiceWrapper;
import com.argusoft.hkg.web.util.ApplicationMasterInitializer;
import com.argusoft.hkg.web.util.SelectItem;
import com.argusoft.internationalization.common.core.I18nService;
import com.argusoft.internationalization.common.exception.I18nException;
import com.argusoft.internationalization.common.model.I18nCountryEntity;
import com.argusoft.internationalization.common.model.I18nLabelEntity;
import com.argusoft.internationalization.common.model.I18nLabelPKEntity;
import com.argusoft.internationalization.common.model.I18nLanguageEntity;
import com.argusoft.internationalization.common.model.I18nLanguagePKEntity;
import com.argusoft.usermanagement.common.model.UMCompany;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 *
 * @author satyam
 */
@Service
public class LocalesTransformerBean {

    @Autowired
    I18nService i18nService;

    @Autowired
    InternationalizationServiceWrapper internationalizationServiceWrapper;    
    @Autowired
    UserManagementServiceWrapper userManagementServiceWrapper;

    @Autowired
    LoginDataBean loginDataBean;

    public void updateBulkLocales(List<LocaleDataBean> labelDataBeans, HttpServletRequest request) {
        List<I18nLabelEntity> finalList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(labelDataBeans)) {
            List<I18nLabelPKEntity> labelsPKList = new ArrayList<>();
            Map<I18nLabelPKEntity, LocaleDataBean> labelPkAndLocalDataBean = new HashMap<>();
            for (LocaleDataBean localeDataBean : labelDataBeans) {
                I18nLabelPKEntity i18nLabelPKEntity = new I18nLabelPKEntity(localeDataBean.getKey(), localeDataBean.getLanguage(), localeDataBean.getCountry(), localeDataBean.getType(), localeDataBean.getEntity(), localeDataBean.getCompany());
                labelPkAndLocalDataBean.put(i18nLabelPKEntity, localeDataBean);
                labelsPKList.add(i18nLabelPKEntity);
            }
            List<I18nLabelEntity> updateThisLabelEntityList = i18nService.retrieveLabelsByPKList(labelsPKList);
            for (I18nLabelEntity i18nLabelEntity : updateThisLabelEntityList) {
                LocaleDataBean localeDB = labelPkAndLocalDataBean.get(i18nLabelEntity.getLabelPK());
                i18nLabelEntity.setLastModifiedBy(loginDataBean.getId());
                i18nLabelEntity.setLastModifiedOn(Calendar.getInstance().getTime());
                if (!localeDB.getText().trim().isEmpty()) {
                    i18nLabelEntity.setText(localeDB.getText());
                }
            }
            if (!CollectionUtils.isEmpty(updateThisLabelEntityList)) {
                List<I18nLabelEntity> entitys = new ArrayList<>();
                List<I18nLabelPKEntity> pkList = new ArrayList<>();
                Map<String, String> keyAndTextMap = new HashMap<>();
                for (I18nLabelEntity i18nLabelEntity : updateThisLabelEntityList) {
                    if (i18nLabelEntity.getLabelPK().getType().equalsIgnoreCase(LabelType.REPORT.toString())) {
                        entitys.add(i18nLabelEntity);
                        keyAndTextMap.put(i18nLabelEntity.getLabelPK().getKey(), i18nLabelEntity.getText());
                    }
                }
                for (I18nLabelEntity entity : entitys) {
                    I18nLabelPKEntity labelPK = new I18nLabelPKEntity();
                    labelPK.setCompany(entity.getLabelPK().getCompany());
                    labelPK.setCountry(entity.getLabelPK().getCountry());
                    labelPK.setKey(entity.getLabelPK().getKey());
                    labelPK.setEntity("Menu");
                    labelPK.setLanguage(entity.getLabelPK().getLanguage());
                    labelPK.setType(LabelType.LABEL.toString());
                    pkList.add(labelPK);
                }
                finalList.addAll(updateThisLabelEntityList);
                List<I18nLabelEntity> i18nFeatureLabelEntityList = i18nService.retrieveLabelsByPKList(pkList);
                if (!CollectionUtils.isEmpty(i18nFeatureLabelEntityList)) {
                    for (I18nLabelEntity i18nFeatureLabelEntity : i18nFeatureLabelEntityList) {
                        i18nFeatureLabelEntity.setLastModifiedBy(loginDataBean.getId());
                        i18nFeatureLabelEntity.setLastModifiedOn(Calendar.getInstance().getTime());
                        if (keyAndTextMap != null && !keyAndTextMap.isEmpty() && !keyAndTextMap.get(i18nFeatureLabelEntity.getLabelPK().getKey()).trim().isEmpty()) {
                            i18nFeatureLabelEntity.setText(keyAndTextMap.get(i18nFeatureLabelEntity.getLabelPK().getKey()));
                            finalList.add(i18nFeatureLabelEntity);
                        }
                    }
                }
            }
            i18nService.updateAllLabels(finalList);
            String i18npath = request.getSession().getServletContext().getRealPath("i18n/");
            String modelEntity = null;
            String[] filesList = new File(i18npath).list();
            List<String> finalFileList = new LinkedList<>();
            List<I18nLanguageEntity> retriveActiveLanguages = i18nService.retriveActiveLanguages();
            StringBuilder languages = null;
            List<String> languageList = new LinkedList<>();
            for (I18nLanguageEntity i18nLanguageEntity : retriveActiveLanguages) {
                languages = new StringBuilder();
                languages.append(i18nLanguageEntity.getLanguagePK().getCode());
                languages.append(loginDataBean.getCompanyId());
                languages.append(".json");
                languageList.add(languages.toString());
            }
            if (!CollectionUtils.isEmpty(languageList)) {
                for (String filesListName : filesList) {
                    for (String langName : languageList) {
                        if (!StringUtils.isEmpty(langName)) {
                            if (filesListName.equalsIgnoreCase(langName)) {
                                finalFileList.add(langName);
                            }
                        }
                    }
                }
            }
            JsonParser jsonParser = new JsonParser();
            for (String filename : finalFileList) {
                String currentFilePath = request.getSession().getServletContext().getRealPath("i18n/" + filename);

                JsonReader jsonReader = null;//new JsonReader(new FileReader("jsonFile.json"));
                try {
                    jsonReader = new JsonReader(new FileReader(currentFilePath));
                    jsonReader.setLenient(true);
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(LocalesTransformerBean.class.getName()).log(Level.SEVERE, null, ex);
                }
                JsonObject currentFilesJson = (JsonObject) jsonParser.parse(jsonReader);
                for (I18nLabelEntity i18nLabelEntity : finalList) {
                    if (i18nLabelEntity.getLabelPK().getEntity().contains(".")) {
                        modelEntity = i18nLabelEntity.getLabelPK().getEntity();
                    } else {
                        modelEntity = i18nLabelEntity.getLabelPK().getEntity() + ".";
                    }
                    currentFilesJson.addProperty(modelEntity + i18nLabelEntity.getLabelPK().getKey(), i18nLabelEntity.getText());
                }
                try {
                    Files.write(Paths.get(currentFilePath), currentFilesJson.toString().getBytes());
                    jsonReader.close();
                } catch (IOException ex) {
                    Logger.getLogger(LocalesTransformerBean.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

    }

    public void createBulkLocales(List<LocaleDataBean> labelDataBeans, HttpServletRequest request) {
        if (!CollectionUtils.isEmpty(labelDataBeans)) {
            List<I18nLabelEntity> labels = convertAddLabelDatabeanToLabelModel(labelDataBeans);
            i18nService.addBulkLabels(labels);
            String i18npath = request.getSession().getServletContext().getRealPath("i18n/");
            String[] filesList = new File(i18npath).list();
            List<String> finalFileList = new LinkedList<>();
            List<I18nLanguageEntity> retriveActiveLanguages = i18nService.retriveActiveLanguages();
            StringBuilder languages = null;
            List<String> languageList = new LinkedList<>();
            for (I18nLanguageEntity i18nLanguageEntity : retriveActiveLanguages) {
                languages = new StringBuilder();
                languages.append(i18nLanguageEntity.getLanguagePK().getCode());
                languages.append(loginDataBean.getCompanyId());
                languages.append(".json");
                languageList.add(languages.toString());
            }
            if (!CollectionUtils.isEmpty(languageList)) {
                for (String filesListName : filesList) {
                    for (String langName : languageList) {
                        if (!StringUtils.isEmpty(langName)) {
                            if (filesListName.equalsIgnoreCase(langName)) {
                                finalFileList.add(langName);
                            }
                        }
                    }
                }
            }
            JsonParser jsonParser = new JsonParser();
            for (String filename : finalFileList) {
                String currentFilePath = request.getSession().getServletContext().getRealPath("i18n/" + filename);
                FileReader fileReader = null;
                try {
                    fileReader = new FileReader(currentFilePath);
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(LocalesTransformerBean.class.getName()).log(Level.SEVERE, null, ex);
                }
                JsonObject currentFilesJson = (JsonObject) jsonParser.parse(fileReader);
                for (I18nLabelEntity i18nLabelEntity : labels) {
                    currentFilesJson.addProperty(i18nLabelEntity.getLabelPK().getKey(), i18nLabelEntity.getText());
                }
                try {
                    Files.write(Paths.get(currentFilePath), currentFilesJson.toString().getBytes());
                    fileReader.close();
                } catch (IOException ex) {
                    Logger.getLogger(LocalesTransformerBean.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    private List<I18nLabelEntity> convertAddLabelDatabeanToLabelModel(List<LocaleDataBean> listAddLabelDatabean) {
        List<I18nLabelEntity> labels = new ArrayList<>();
        for (LocaleDataBean addLabelDatabean : listAddLabelDatabean) {
            I18nLabelEntity label = new I18nLabelEntity();
            label.setText(addLabelDatabean.getText());
            label.setEnvironment(addLabelDatabean.getEnvironment());
            label.setLastModifiedBy(loginDataBean.getId());
            String finalKey = addLabelDatabean.getText().trim().replace(" ", "");
            label.setLabelPK(new I18nLabelPKEntity(finalKey, null, null, addLabelDatabean.getType(), addLabelDatabean.getEntity(), loginDataBean.getCompanyId()));
            labels.add(label);
        }
        return labels;
    }

    public LanguageDataBean convertLanguageModelToInternationalizationLanguageDatabean(I18nLanguageEntity language) {

        LanguageDataBean languageDatabean = new LanguageDataBean();
        languageDatabean.setCode(language.getLanguagePK().getCode());
        languageDatabean.setCountry(language.getLanguagePK().getCountry());
        languageDatabean.setName(language.getName());

        return languageDatabean;
    }

    public LocaleDataBean convertLabelModelToLabelDataBean(I18nLabelEntity labelModel, LocaleDataBean labelDataBean) {
        String key = labelModel.getLabelPK().getKey();
        labelDataBean.setCountry(labelModel.getLabelPK().getCountry());
        labelDataBean.setKey(labelModel.getLabelPK().getKey());
        labelDataBean.setLanguage(labelModel.getLabelPK().getLanguage());
        labelDataBean.setEnvironment(labelModel.getEnvironment());
        labelDataBean.setText(labelModel.getText());
        labelDataBean.setDefaultText(labelModel.getText());
        labelDataBean.setTranslationPending(labelModel.getTranslationPending());
        labelDataBean.setType(labelModel.getLabelPK().getType());
        labelDataBean.setEntity(labelModel.getLabelPK().getEntity());
        labelDataBean.setCompany(labelModel.getLabelPK().getCompany());
        return labelDataBean;
    }

    public I18nLabelEntity convertLabelDataBeanToLabelModel(LocaleDataBean labelDataBean, I18nLabelEntity labelModel) {

        labelModel.setEnvironment(labelDataBean.getEnvironment());
        String entity = labelDataBean.getEntity();
        String key = labelDataBean.getKey();
        String modelEntity = key.substring(0, key.indexOf("."));
        if (!modelEntity.trim().equals(entity.trim())) {
            String labelText = key.substring(key.indexOf("."));
            String newKey = entity.concat(labelText);
            labelModel.setLabelPK(new I18nLabelPKEntity(newKey, labelDataBean.getLanguage(), labelDataBean.getCountry(), labelDataBean.getType(), labelDataBean.getEntity(), loginDataBean.getCompanyId()));
        }
        labelModel.setText(labelDataBean.getText());
        labelModel.setTranslationPending(labelDataBean.isTranslationPending());

        return labelModel;
    }

    public List<LocaleDataBean> retrieveLocales(String searchString, String category, String language) {

        return null;
    }

    //This method is useful in the server side pagination
    public List<LocaleDataBean> retrieveLabelsByPage(DatatableDataBean<Map> tableConfig) {
        Map customParameters = tableConfig.getCustomParameters();
        String languageCode = (String) customParameters.get("language");
        String categoryOfLocale = (String) customParameters.get("category");
        List<I18nLabelEntity> englishLabels = internationalizationServiceWrapper.searchLabels(tableConfig.getSearch().getValue(), LabelType.valueOf(categoryOfLocale), HkSystemConstantUtil.I18_LANGUAGE.ENGLISH, loginDataBean.getCompanyId(), tableConfig.getStart(), tableConfig.getLength());
        List<LocaleDataBean> localeDataBeans = new ArrayList<>();
        if (!CollectionUtils.isEmpty(englishLabels)) {
            Map<String, I18nLabelEntity> labelsMap = new HashMap<>();
            for (I18nLabelEntity label : englishLabels) {
                labelsMap.put(label.getLabelPK().getKey(), label);
            }
            List<I18nLabelEntity> searchLabels = internationalizationServiceWrapper.searchLabels(tableConfig.getSearch().getValue(), LabelType.valueOf(categoryOfLocale), languageCode, loginDataBean.getCompanyId(), tableConfig.getStart(), tableConfig.getLength());
            for (I18nLabelEntity i18nLabelEntity : searchLabels) {
                LocaleDataBean localeDataBean = new LocaleDataBean();
                localeDataBean.setDefaultText(labelsMap.get(i18nLabelEntity.getLabelPK().getKey()).getText());
                this.convertLabelModelToLabelDataBean(i18nLabelEntity, localeDataBean);
                localeDataBeans.add(localeDataBean);
            }
        }
        return localeDataBeans;
    }

    public List<LocaleDataBean> retrieveLabelsBySearchCriteria(SearchBeanForLabel searchBean) {
        LabelType labelType = LabelType.valueOf(searchBean.getType());
        Long company = searchBean.getLanguageDataBean().getCompany();
        if (company == null) {
            company = loginDataBean.getCompanyId();
        }
        String entity = searchBean.getEntity();
        if (labelType == LabelType.CONTENT || labelType == LabelType.REPORT) {
            String get = HkSystemConstantUtil.I18N_CONTENT_TYPE_MAP.get(entity.toLowerCase());
            entity = get;
        }
        List<I18nLabelEntity> englishLabels = internationalizationServiceWrapper.retrieveLabels(searchBean.getSearchText(), labelType, HkSystemConstantUtil.I18_LANGUAGE.ENGLISH, company, entity);
        List<I18nLabelPKEntity> pklist = new ArrayList<>();

        List<LocaleDataBean> localeDataBeans = new ArrayList<>();
        if (!CollectionUtils.isEmpty(englishLabels)) {
            Map<String, I18nLabelEntity> labelsMap = new HashMap<>();
            for (I18nLabelEntity label : englishLabels) {
                labelsMap.put(label.getLabelPK().getKey(), label);
                I18nLabelPKEntity i18nLabelPKEntity = null;
                if (searchBean.getEntity() != null && searchBean.getLanguageDataBean().getCompany() != null) {
                    i18nLabelPKEntity = new I18nLabelPKEntity(label.getLabelPK().getKey(), searchBean.getLanguageDataBean().getCode(), searchBean.getLanguageDataBean().getCountry(), label.getLabelPK().getType(), searchBean.getEntity(), searchBean.getLanguageDataBean().getCompany());
                } else {
                    i18nLabelPKEntity = new I18nLabelPKEntity(label.getLabelPK().getKey(), searchBean.getLanguageDataBean().getCode(), searchBean.getLanguageDataBean().getCountry(), label.getLabelPK().getType(), label.getLabelPK().getEntity(), label.getLabelPK().getCompany());
                }

                pklist.add(i18nLabelPKEntity);
            }
            List<I18nLabelEntity> searchLabels = i18nService.retrieveLabelsByPKList(pklist);
            for (I18nLabelEntity i18nLabelEntity : searchLabels) {
                LocaleDataBean localeDataBean = new LocaleDataBean();
                this.convertLabelModelToLabelDataBean(i18nLabelEntity, localeDataBean);
                localeDataBean.setDefaultText(labelsMap.get(i18nLabelEntity.getLabelPK().getKey()).getText());
                localeDataBeans.add(localeDataBean);
            }
        }
        Collections.sort(localeDataBeans, new Comparator() {

            @Override
            public int compare(Object o1, Object o2) {
                LocaleDataBean label = (LocaleDataBean) o1;
                LocaleDataBean labelTemp = (LocaleDataBean) o2;
                return label.getDefaultText().compareToIgnoreCase(labelTemp.getDefaultText());
            }
        });
        return localeDataBeans;
    }

    public List<SelectItem> retrieveLanguages() {
        List<SelectItem> languages = null;
        List<I18nLanguageEntity> languageEntities = i18nService.retriveActiveLanguages();
        if (!CollectionUtils.isEmpty(languageEntities)) {
            languages = new LinkedList<>();
            for (I18nLanguageEntity languageEntity : languageEntities) {
                languages.add(new SelectItem(languageEntity.getLanguagePK().getCode(), languageEntity.getName()));
            }
        }
        return languages;
    }

    public List<LocaleDataBean> retrieveLabelsByLanguage(String letter, LanguageDataBean languageDatabean) {
        String code = languageDatabean.getCode();
        String country = languageDatabean.getCountry();
        if (code != null && country != null) {
            List<I18nLabelEntity> englishLabels = i18nService.retriveLabelsByLetterAndLanguage(letter, new I18nLanguageEntity(HkSystemConstantUtil.I18_LANGUAGE.ENGLISH, HkSystemConstantUtil.COUNTRY_NZ));
            Map<String, I18nLabelEntity> labelsMap = null;
            List<LocaleDataBean> labelDataBeans = null;
            if (englishLabels != null && englishLabels.size() > 0) {
                labelsMap = new HashMap<>();
                List<String> keys = new ArrayList<>();
                for (I18nLabelEntity label : englishLabels) {
                    labelsMap.put(label.getLabelPK().getKey(), label);
                    keys.add(label.getLabelPK().getKey());
                }

                List<I18nLabelEntity> labels = i18nService.retriveLabelsByKeysAndLanguage(keys, new I18nLanguageEntity(code, country));

                labelDataBeans = new LinkedList<>();
                for (I18nLabelEntity label : labels) {
                    I18nLabelEntity labelModel = labelsMap.get(label.getLabelPK().getKey());
                    LocaleDataBean labelDataBean = new LocaleDataBean();
                    labelDataBean.setDefaultText(labelModel.getText());
                    labelDataBean = this.convertLabelModelToLabelDataBean(label, labelDataBean);
                    labelDataBeans.add(labelDataBean);
                }
                Collections.sort(labelDataBeans, new Comparator() {

                    @Override
                    public int compare(Object o1, Object o2) {
                        LocaleDataBean label = (LocaleDataBean) o1;
                        LocaleDataBean labelTemp = (LocaleDataBean) o2;
                        return label.getDefaultText().compareToIgnoreCase(labelTemp.getDefaultText());
                    }
                });
            }
            if (labelDataBeans != null) {
                return labelDataBeans;
            };
        }
        return null;
    }

    public List<LanguageDataBean> retrieveAllLanguages() {
        List<I18nLanguageEntity> languages = i18nService.retriveActiveLanguages();
        List<LanguageDataBean> languageDatabeanList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(languages)) {
            for (I18nLanguageEntity language : languages) {
                languageDatabeanList.add(convertLanguageModelToInternationalizationLanguageDatabean(language));
            }
        }
        return languageDatabeanList;
    }

    public void updateLabelDetails(LocaleDataBean labelDataBean, HttpServletRequest request) throws FileNotFoundException, IOException {
        I18nLabelEntity label = i18nService.retriveLabelByPK(new I18nLabelPKEntity(labelDataBean.getKey(), labelDataBean.getLanguage(), labelDataBean.getCountry(), labelDataBean.getType(), labelDataBean.getEntity(), loginDataBean.getCompanyId()));
        if (label != null) {
            I18nLabelEntity labelModel = convertLabelDataBeanToLabelModel(labelDataBean, label);
            if (labelModel != null) {
                i18nService.updateLabel(labelModel);
                String i18npath = request.getSession().getServletContext().getRealPath("i18n/" + labelModel.getLabelPK().getLanguage().toLowerCase() + ".json");
                JsonParser jsonParser = new JsonParser();
                FileReader fileReader = new FileReader(i18npath);
                JsonObject currentFilesJson = (JsonObject) jsonParser.parse(fileReader);
                currentFilesJson.remove(labelModel.getLabelPK().getKey());
                currentFilesJson.addProperty(labelModel.getLabelPK().getKey(), labelModel.getText());
                Files.write(Paths.get(i18npath), currentFilesJson.toString().getBytes());
                fileReader.close();
            }
        }
    }

    public String createCountryLanguage(CountryLanguageDataBean hKCountryLanguageDataBean) {
        if (i18nService.retriveLanguageByPK(new I18nLanguagePKEntity(hKCountryLanguageDataBean.getLanguageCode().toUpperCase(), hKCountryLanguageDataBean.getCountryCode().toUpperCase())) != null) {
            return "Country and Language exist";
        } else {
            I18nCountryEntity i18nCountryEntity = new I18nCountryEntity();
            try {
                i18nCountryEntity = i18nService.retriveCountryByCode(hKCountryLanguageDataBean.getCountryCode());
            } catch (I18nException ex) {
                i18nCountryEntity = new I18nCountryEntity();
                i18nCountryEntity.setCode(hKCountryLanguageDataBean.getCountryCode());
                i18nCountryEntity.setCreatedBy(1L);
                i18nCountryEntity.setName((HkApplicationScopeObjects.I18N_COUNTRY_MAP.get(hKCountryLanguageDataBean.getCountryCode()).toString()));
                i18nCountryEntity.setCreatedOn(Calendar.getInstance().getTime());
                i18nService.addCountry(i18nCountryEntity);
            }
            I18nLanguageEntity i18nLanguageEntity = new I18nLanguageEntity();
            i18nLanguageEntity.setCharacterEncoding(hKCountryLanguageDataBean.getEncoding());
            i18nLanguageEntity.setCountry1(i18nCountryEntity);
            i18nLanguageEntity.setCreatedBy(1L);
            i18nLanguageEntity.setIsLeftToRight(hKCountryLanguageDataBean.isIsLeftToRight());
            i18nLanguageEntity.setName(HkApplicationScopeObjects.I18N_LANGUAGE_MAP.get(hKCountryLanguageDataBean.getLanguageCode()).toString());
            i18nLanguageEntity.setCreatedOn(Calendar.getInstance().getTime());
            I18nLanguagePKEntity i18nLanguagePKEntity = new I18nLanguagePKEntity();
            i18nLanguagePKEntity.setCode(hKCountryLanguageDataBean.getLanguageCode());
            i18nLanguagePKEntity.setCountry(hKCountryLanguageDataBean.getCountryCode());
            i18nLanguageEntity.setLanguagePK(i18nLanguagePKEntity);
            i18nService.addLanguage(i18nLanguageEntity);
            return null;
        }
    }

    public void copyLabelToPropertyFile(HttpServletRequest request) throws FileNotFoundException, IOException {
        List<I18nLanguageEntity> listlang = i18nService.retriveActiveLanguages();
        Map<String, Properties> mapLangInputStream = new HashMap();
        for (I18nLanguageEntity i18nLanguageEntity : listlang) {
            InputStream inputStream = new FileInputStream(request.getSession().getServletContext().getRealPath("i18n/messages_" + i18nLanguageEntity.getLanguagePK().getCode().toLowerCase() + ".properties"));
            Properties properties = new Properties();
            properties.load(inputStream);
            mapLangInputStream.put(i18nLanguageEntity.getLanguagePK().getCode(), properties);
        }

        List<I18nLabelEntity> listlabel = i18nService.retrieveLabelsChangedAfterDate(null, null);
        for (I18nLabelEntity i18nLabelEntity : listlabel) {
            String langCode = i18nLabelEntity.getLabelPK().getLanguage();
            Properties currentProperty = mapLangInputStream.get(langCode);
            currentProperty.setProperty(i18nLabelEntity.getLabelPK().getKey(), i18nLabelEntity.getText());
        }
        Set<String> keyMapLangInputStream = mapLangInputStream.keySet();
        for (String key : keyMapLangInputStream) {
            OutputStream outputStream = new FileOutputStream(request.getSession().getServletContext().getRealPath("i18n/messages_" + key.toLowerCase() + ".properties"));
            Properties properties = mapLangInputStream.get(key);
            properties.store(outputStream, null);
        }
    }

    public void generateJsonFilesOfLabels(HttpServletRequest request) throws FileNotFoundException, IOException {
        //Retrieve all active languages
        List<I18nLanguageEntity> activeLanguages = i18nService.retriveActiveLanguages();
        //Created map language as key and it's final json as value
        Map<String, JsonObject> languageAndItsJsonOfLabels = new HashMap();
        //Creating file from scratch so as value creted new json object
        for (I18nLanguageEntity language : activeLanguages) {
            languageAndItsJsonOfLabels.put(language.getLanguagePK().getCode(), new JsonObject());
        }
        //Retrieve all labels
        List<I18nLabelEntity> allLabels = i18nService.retrieveLabelsChangedAfterDate(null, null);
        //Add label and it's text as per it's respective laguage's json
        for (I18nLabelEntity label : allLabels) {
            languageAndItsJsonOfLabels.get(label.getLabelPK().getLanguage()).addProperty(label.getLabelPK().getKey(), label.getText());
        }
        //Write all json of active languages in respective file.
        Set<String> keyMapLangInputStream = languageAndItsJsonOfLabels.keySet();
        for (String key : keyMapLangInputStream) {
            Files.write(Paths.get(request.getSession().getServletContext().getRealPath("i18n/" + key.toLowerCase() + ".json")), new Gson().toJson(languageAndItsJsonOfLabels.get(key)).getBytes());
        }
    }

    public void deleteLabelDetails(LocaleDataBean labelDataBean) {
        if (labelDataBean.getKey() != null) {
//            i18nService.removeLabelByKeyLanguage(null, labelDataBean.getKey());
        }
    }

    public I18nLabelEntity convertLabelDatabeanToEntity(LocaleDataBean addLabelDatabean) {

        I18nLabelEntity label = new I18nLabelEntity();
        String modelEntity = null;
        if (addLabelDatabean.getEntity().contains(".")) {
            modelEntity = addLabelDatabean.getEntity().substring(0, addLabelDatabean.getEntity().indexOf("."));
        } else {
            modelEntity = addLabelDatabean.getEntity();
        }

        //.substring(0, addLabelDatabean.getEntity().indexOf("."));
        label.setText(addLabelDatabean.getText());
        label.setEnvironment(addLabelDatabean.getEnvironment());
        label.setLastModifiedBy(loginDataBean.getId());
        String finalKey = addLabelDatabean.getText().trim().replace(" ", "");
        label.setLabelPK(new I18nLabelPKEntity(finalKey, addLabelDatabean.getLanguage(), addLabelDatabean.getCountry(), addLabelDatabean.getType(), modelEntity, loginDataBean.getCompanyId()));
        return label;
    }

    public synchronized void createLabelFromDirective(LocaleDataBean locales, HttpServletRequest request) throws FileNotFoundException {
        if (locales != null) {
            I18nLabelEntity label = convertLabelDatabeanToEntity(locales);
            i18nService.addLabelAsync(label);
            String i18npath = request.getSession().getServletContext().getRealPath("i18n/");
            String[] filesList = new File(i18npath).list();
            List<String> finalFileList = new LinkedList<>();
            List<I18nLanguageEntity> retriveActiveLanguages = i18nService.retriveActiveLanguages();
            StringBuilder languages = null;
            List<String> languageList = new LinkedList<>();
            for (I18nLanguageEntity i18nLanguageEntity : retriveActiveLanguages) {
                languages = new StringBuilder();
                languages.append(i18nLanguageEntity.getLanguagePK().getCode());
                languages.append(loginDataBean.getCompanyId());
                languages.append(".json");
                languageList.add(languages.toString());
            }
            if (!CollectionUtils.isEmpty(languageList)) {
                for (String filesListName : filesList) {
                    for (String langName : languageList) {
                        if (!StringUtils.isEmpty(langName)) {
                            if (filesListName.equalsIgnoreCase(langName)) {
                                finalFileList.add(langName);
                            }
                        }
                    }
                }
            }
            JsonParser jsonParser = new JsonParser();
            for (String filename : finalFileList) {
                String currentFilePath = request.getSession().getServletContext().getRealPath("i18n/" + filename);

                JsonReader jsonReader;//new JsonReader(new FileReader("jsonFile.json"));
                try {
                    jsonReader = new JsonReader(new FileReader(currentFilePath));
                    jsonReader.setLenient(true);
                    JsonObject currentFilesJson = (JsonObject) jsonParser.parse(jsonReader);
                    currentFilesJson.addProperty(locales.getEntity() + label.getLabelPK().getKey(), label.getText());
                    Files.write(Paths.get(currentFilePath), currentFilesJson.toString().getBytes());
                    jsonReader.close();
                } catch (IOException ex) {
                    Logger.getLogger(LocalesTransformerBean.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
     public void initLocales() {
        I18nCountryEntity countryEntity = null;
        try {
            countryEntity = i18nService.retriveCountryByCode(HkSystemConstantUtil.COUNTRY_NZ);
        } catch (I18nException ex) {
            java.util.logging.Logger.getLogger(ApplicationMasterInitializer.class.getName()).log(Level.SEVERE, null, ex.getMessage());
        }
        if (countryEntity == null) {
            countryEntity = new I18nCountryEntity(HkSystemConstantUtil.COUNTRY_NZ, "India", true, false, 1l, new Date());
            i18nService.addCountry(countryEntity);
        }

        List<String> languageStrList = new LinkedList<>();
        List<I18nLanguageEntity> activeLanguages = i18nService.retriveActiveLanguages();
        if (!org.springframework.util.CollectionUtils.isEmpty(activeLanguages)) {
            for (I18nLanguageEntity languageEntity : activeLanguages) {
                languageStrList.add(languageEntity.getLanguagePK().getCode());
            }
        }

        I18nLanguageEntity languageEntity;
        if (!languageStrList.contains(HkSystemConstantUtil.I18_LANGUAGE.ENGLISH)) {
            languageEntity = new I18nLanguageEntity(new I18nLanguagePKEntity(HkSystemConstantUtil.I18_LANGUAGE.ENGLISH, HkSystemConstantUtil.COUNTRY_NZ), "English", true, true, false, 1l, new Date());
            languageEntity.setCountry1(countryEntity);
            i18nService.addLanguage(languageEntity);
            languageStrList.add(HkSystemConstantUtil.I18_LANGUAGE.ENGLISH);
        }

        if (!languageStrList.contains(HkSystemConstantUtil.I18_LANGUAGE.GUJARATI)) {
            languageEntity = new I18nLanguageEntity(new I18nLanguagePKEntity(HkSystemConstantUtil.I18_LANGUAGE.GUJARATI, HkSystemConstantUtil.COUNTRY_NZ), "Gujarati", true, true, false, 1l, new Date());
            languageEntity.setCountry1(countryEntity);
            i18nService.addLanguage(languageEntity);
            languageStrList.add(HkSystemConstantUtil.I18_LANGUAGE.GUJARATI);
        }

        if (!languageStrList.contains(HkSystemConstantUtil.I18_LANGUAGE.HINDI)) {
            languageEntity = new I18nLanguageEntity(new I18nLanguagePKEntity(HkSystemConstantUtil.I18_LANGUAGE.HINDI, HkSystemConstantUtil.COUNTRY_NZ), "Hindi", true, true, false, 1l, new Date());
            languageEntity.setCountry1(countryEntity);
            i18nService.addLanguage(languageEntity);
            languageStrList.add(HkSystemConstantUtil.I18_LANGUAGE.HINDI);
        }

        if (!org.springframework.util.CollectionUtils.isEmpty(languageStrList)) {
            List<I18nLabelEntity> staticLabels = new LinkedList<>();
            for (String language : languageStrList) {
                for (Map.Entry<String, String> entry : HkSystemConstantUtil.I18_NOTIFICATION_MAP.entrySet()) {
                    String key = entry.getKey();
                    String message = entry.getValue();
                    I18nLabelEntity i18nLabelEntity = new I18nLabelEntity(new I18nLabelPKEntity(key, language, HkSystemConstantUtil.COUNTRY_NZ, LabelType.NOTIFICATION.name(), HkSystemConstantUtil.I18_ENTITY.NOTIFICATION, 0l), message, 1l, new Date(), "w");
                    i18nLabelEntity.setCustom3b(Boolean.FALSE);
                    i18nLabelEntity.setTranslationPending(Boolean.FALSE);
                    staticLabels.add(i18nLabelEntity);
                }
            }
            i18nService.updateAllLabels(staticLabels);
        }

        Map<Long, UMCompany> activeFranchises = userManagementServiceWrapper.retrieveActiveFranchises(true);
        Map<Long, Map<String, List<I18nLabelEntity>>> franchiseWiseLangmap = new LinkedHashMap<>();
        List<Long> franchises = new LinkedList<>();
        franchises.add(0l);
        if (!org.springframework.util.CollectionUtils.isEmpty(activeFranchises)) {
            for (Long franchiseId : activeFranchises.keySet()) {
                franchises.add(franchiseId);
            }
        }
        List<I18nLabelEntity> i18nLabelEntitys = internationalizationServiceWrapper.retrieveLabelsForFranchises(null, franchises);
        Map<String, I18nLabelEntity> franchiseZeroExtraDataMap = new LinkedHashMap<>();

        if (!org.springframework.util.CollectionUtils.isEmpty(i18nLabelEntitys)) {
            Map<String, List<I18nLabelEntity>> innerMap;
            for (I18nLabelEntity i18nLabelEntity : i18nLabelEntitys) {
                Long companyId = i18nLabelEntity.getLabelPK().getCompany();
                String labelPK = i18nLabelEntity.getLabelPK().toString();
                if (companyId == 0l) {
                    franchiseZeroExtraDataMap.put(labelPK, i18nLabelEntity);
                }
                if (!franchiseWiseLangmap.containsKey(companyId)) {
                    innerMap = new LinkedHashMap<>();
                    List<I18nLabelEntity> tempi18Entitys = new ArrayList<>();
                    tempi18Entitys.add(i18nLabelEntity);
                    innerMap.put(i18nLabelEntity.getLabelPK().getLanguage(), tempi18Entitys);
                } else {
                    innerMap = franchiseWiseLangmap.get(companyId);
                    if (!org.springframework.util.CollectionUtils.isEmpty(innerMap)) {
                        List<I18nLabelEntity> tempi18Entitys;
                        if (!innerMap.containsKey(i18nLabelEntity.getLabelPK().getLanguage())) {
                            tempi18Entitys = new ArrayList<>();
                        } else {
                            tempi18Entitys = innerMap.get(i18nLabelEntity.getLabelPK().getLanguage());
                        }
                        tempi18Entitys.add(i18nLabelEntity);
                        innerMap.put(i18nLabelEntity.getLabelPK().getLanguage(), tempi18Entitys);
                    }
                }
                franchiseWiseLangmap.put(companyId, innerMap);
            }
        }

        if (!org.springframework.util.CollectionUtils.isEmpty(franchises)) {
            for (Long franchiseId : franchises) {
                if (franchiseId != 0) {
                    Map<String, I18nLabelEntity> franchiseZeroExtraDataMapNew = new LinkedHashMap<>();
                    franchiseZeroExtraDataMapNew.putAll(franchiseZeroExtraDataMap);
                    copyNewLabelsFromFranchise(franchiseZeroExtraDataMapNew, franchiseWiseLangmap.get(franchiseId), franchiseId);
                }
                writeLocalsInFile(franchiseId, franchiseWiseLangmap, languageStrList);
            }
        }
    }

    private void copyNewLabelsFromFranchise(Map<String, I18nLabelEntity> franchiseZeroExtraDataMap, Map<String, List<I18nLabelEntity>> labelsMap, Long franchise) {
        if (!org.springframework.util.CollectionUtils.isEmpty(labelsMap)) {
            for (String language : labelsMap.keySet()) {
                List<I18nLabelEntity> labelEntities = labelsMap.get(language);
                for (I18nLabelEntity labelEntity : labelEntities) {
                    String labelPK = labelEntity.getLabelPK().toString();
                    labelPK = labelPK.replaceFirst("company=" + labelEntity.getLabelPK().getCompany(), "company=0");
                    if (franchiseZeroExtraDataMap.containsKey(labelPK)) {
                        franchiseZeroExtraDataMap.remove(labelPK);
                    }
                }
            }
        }
        List<I18nLabelEntity> newLabels = new LinkedList<>();
        for (I18nLabelEntity labelEntity : franchiseZeroExtraDataMap.values()) {
            I18nLabelEntity i18nLabelEntity = new I18nLabelEntity(new I18nLabelPKEntity(labelEntity.getLabelPK().getKey(), labelEntity.getLabelPK().getLanguage(), labelEntity.getLabelPK().getCountry(), labelEntity.getLabelPK().getType(), labelEntity.getLabelPK().getEntity(), franchise), labelEntity.getText(), labelEntity.getLastModifiedBy(), labelEntity.getLastModifiedOn(), labelEntity.getEnvironment());
            i18nLabelEntity.setCustom3b(labelEntity.getCustom3b());
            i18nLabelEntity.setTranslationPending(labelEntity.getTranslationPending());
            newLabels.add(i18nLabelEntity);
        }
        internationalizationServiceWrapper.updateLabels(newLabels);
    }

    private void writeLocalsInFile(Long franchiseId, Map<Long, Map<String, List<I18nLabelEntity>>> franchiseWiseLangmap, List<String> languageStrList) {
        Map<String, List<I18nLabelEntity>> langEntityMap = franchiseWiseLangmap.get(franchiseId);
        if (!org.springframework.util.CollectionUtils.isEmpty(langEntityMap)) {
            for (String language : languageStrList) {
                List<I18nLabelEntity> writeForI18nLabelEntitys = langEntityMap.get(language);
                if (!org.springframework.util.CollectionUtils.isEmpty(writeForI18nLabelEntitys)) {
                    String modelEntity;
                    boolean result = true;
                    if (!org.springframework.util.CollectionUtils.isEmpty(langEntityMap) && langEntityMap.containsKey(language)) {
                        StringBuilder fileNameBuilder = new StringBuilder();
                        fileNameBuilder.append(WebApplicationInitializerConfig.projectDirectory);
                        fileNameBuilder.append("/");
                        fileNameBuilder.append(language.toUpperCase());
                        fileNameBuilder.append(franchiseId);
                        fileNameBuilder.append(".json");
                        File i18File = new File(fileNameBuilder.toString());
                        if (!i18File.exists()) {
                            try {
                                result = i18File.createNewFile();

                            } catch (IOException ex) {
                                java.util.logging.Logger.getLogger(ApplicationMasterInitializer.class
                                        .getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                        if (result) {
                            JsonObject currentFilesJson = new JsonObject();
                            for (I18nLabelEntity finalI18nLabelEntity : writeForI18nLabelEntitys) {

                                if (finalI18nLabelEntity.getLabelPK().getEntity().contains(".")) {
                                    modelEntity = finalI18nLabelEntity.getLabelPK().getEntity();
                                } else {
                                    modelEntity = finalI18nLabelEntity.getLabelPK().getEntity() + ".";
                                }
                                currentFilesJson.addProperty(modelEntity + finalI18nLabelEntity.getLabelPK().getKey(), finalI18nLabelEntity.getText());
                            }
                            try {
                                Files.write(Paths.get(fileNameBuilder.toString()), currentFilesJson.toString().getBytes());

                            } catch (IOException ex) {
                                java.util.logging.Logger.getLogger(ApplicationMasterInitializer.class
                                        .getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }
                }
            }
        }
    }

    public void generateLanguagePropertyFiles(Long franchiseId) {

        List<String> languageStrList = new LinkedList<>();
        List<I18nLanguageEntity> activeLanguages = i18nService.retriveActiveLanguages();
        if (!org.springframework.util.CollectionUtils.isEmpty(activeLanguages)) {
            for (I18nLanguageEntity languageEntity : activeLanguages) {
                languageStrList.add(languageEntity.getLanguagePK().getCode());
            }
        }

        List<Long> franchises = null;
        Map<Long, Map<String, List<I18nLabelEntity>>> franchiseWiseLangmap = new LinkedHashMap<>();
        franchises = new LinkedList<>();
        franchises.add(franchiseId);
        List<I18nLabelEntity> i18nLabelEntitys = internationalizationServiceWrapper.retrieveLabelsForFranchises(null, franchises);

        if (!org.springframework.util.CollectionUtils.isEmpty(i18nLabelEntitys)) {
            Map<String, List<I18nLabelEntity>> innerMap;
            for (I18nLabelEntity i18nLabelEntity : i18nLabelEntitys) {
                if (!franchiseWiseLangmap.containsKey(i18nLabelEntity.getLabelPK().getCompany())) {
                    innerMap = new LinkedHashMap<>();
                    List<I18nLabelEntity> tempi18Entitys = new ArrayList<>();
                    tempi18Entitys.add(i18nLabelEntity);
                    innerMap.put(i18nLabelEntity.getLabelPK().getLanguage(), tempi18Entitys);
                } else {
                    innerMap = franchiseWiseLangmap.get(i18nLabelEntity.getLabelPK().getCompany());
                    if (!org.springframework.util.CollectionUtils.isEmpty(innerMap)) {
                        List<I18nLabelEntity> tempi18Entitys;
                        if (!innerMap.containsKey(i18nLabelEntity.getLabelPK().getLanguage())) {
                            tempi18Entitys = new ArrayList<>();
                        } else {
                            tempi18Entitys = innerMap.get(i18nLabelEntity.getLabelPK().getLanguage());
                        }
                        tempi18Entitys.add(i18nLabelEntity);
                        innerMap.put(i18nLabelEntity.getLabelPK().getLanguage(), tempi18Entitys);
                    }
                }
                franchiseWiseLangmap.put(i18nLabelEntity.getLabelPK().getCompany(), innerMap);
            }
        }

        if (franchiseId != null) {
            writeLocalsInFile(franchiseId, franchiseWiseLangmap, languageStrList);
        }

    }

}
