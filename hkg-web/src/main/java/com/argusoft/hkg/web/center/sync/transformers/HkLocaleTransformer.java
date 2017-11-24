/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.center.sync.transformers;

import com.argusoft.hkg.sync.xmpp.util.SyncHelper;
import com.argusoft.hkg.sync.xmpp.util.SyncTransferType;
import com.argusoft.hkg.sync.xmpp.util.SyncTransformerAdapter;
import com.argusoft.hkg.web.config.WebApplicationInitializerConfig;
import com.argusoft.hkg.web.internationalization.transformers.LocalesTransformerBean;
import com.argusoft.internationalization.common.model.I18nLabelEntity;
import com.argusoft.internationalization.common.model.I18nLabelPKEntity;
import com.argusoft.sync.center.model.HkLocaleDocument;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.stereotype.Service;

/**
 *
 * @author akta
 */
@Service
public class HkLocaleTransformer extends SyncTransformerAdapter {

    private final Map<String, List<String>> queryParametersMap;
    private final Map<String, Object> idMap;

    public HkLocaleTransformer() {
        this.queryParametersMap = new HashMap<>();
        this.idMap = new HashMap<>();
    }

    @Override
    public void save(Object object) throws FileNotFoundException {
        if (object != null && object instanceof HkLocaleDocument) {
            HkLocaleDocument localDocument = (HkLocaleDocument) object;
            StringBuilder fileName = new StringBuilder();
            fileName.append(localDocument.getLanguage());
            fileName.append(localDocument.getCompany());
            fileName.append(".json");
            String currentFilePath = WebApplicationInitializerConfig.projectDirectory;
            JsonParser jsonParser = new JsonParser();
            JsonReader jsonReader = null;//new JsonReader(new FileReader("jsonFile.json"));
            try {
                jsonReader = new JsonReader(new FileReader(currentFilePath + "/" + fileName));
                jsonReader.setLenient(true);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(LocalesTransformerBean.class.getName()).log(Level.SEVERE, null, ex);
            }
            JsonObject currentFilesJson = (JsonObject) jsonParser.parse(jsonReader);
            String modelEntity = null;
            if (localDocument.getEntity().contains(".")) {
                modelEntity = localDocument.getEntity();
            } else {
                modelEntity = localDocument.getEntity() + ".";
            }
            currentFilesJson.addProperty(modelEntity + localDocument.getKey(), localDocument.getText());
            try {
                Files.write(Paths.get(currentFilePath + "/" + fileName), currentFilesJson.toString().getBytes());
                jsonReader.close();
            } catch (IOException ex) {
                Logger.getLogger(LocalesTransformerBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public Object convertEntityToDocument(Object entityObject) {
        HkLocaleDocument localDocument = new HkLocaleDocument();
        I18nLabelEntity labelEntity = (I18nLabelEntity) entityObject;
        I18nLabelPKEntity labelPK = labelEntity.getLabelPK();
        idMap.put(SyncHelper.encodeMapKeyWithDot("labelPK.key"), labelPK.getKey());
        idMap.put(SyncHelper.encodeMapKeyWithDot("labelPK.language"), labelPK.getLanguage());
        idMap.put(SyncHelper.encodeMapKeyWithDot("labelPK.country"), labelPK.getCountry());
        idMap.put(SyncHelper.encodeMapKeyWithDot("labelPK.type"), labelPK.getType());
        idMap.put(SyncHelper.encodeMapKeyWithDot("labelPK.entity"), labelPK.getEntity());
        idMap.put(SyncHelper.encodeMapKeyWithDot("labelPK.company"), labelPK.getCompany());
        localDocument.setCompany(labelEntity.getLabelPK().getCompany());
        localDocument.setKey(labelEntity.getLabelPK().getKey());
        localDocument.setLanguage(labelEntity.getLabelPK().getLanguage());
        localDocument.setLastModifiedOn(labelEntity.getLastModifiedOn());
        localDocument.setText(labelEntity.getText());
        localDocument.setEntity(labelEntity.getLabelPK().getEntity());
        queryParametersMap.put(SyncHelper.FRANCHISE_ID, Arrays.asList(String.valueOf(labelEntity.getLabelPK().getCompany())));
        return localDocument;
    }

    @Override
    public Map<String, List<String>> getQueryParameters() {
        return Collections.unmodifiableMap(queryParametersMap);
    }

    @Override
    public int getSyncTransferType() {
        return SyncTransferType.ONE_TO_MANY;
    }

    @Override
    public Map<String, Object> getidMap() {
        return Collections.unmodifiableMap(idMap);
    }
}
