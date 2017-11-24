/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.center.sync.transformers;

import com.argusoft.hkg.sync.xmpp.util.SyncTransformerAdapter;
import com.argusoft.hkg.web.usermanagement.databeans.FeatureDataBean;
import com.argusoft.sync.center.model.SyncCenterFeatureDocument;
import com.google.gson.Gson;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.stereotype.Service;

/**
 *
 * @author shruti
 */
@Service
public class SyncCenterFeatureTransformer extends SyncTransformerAdapter {

    public FeatureDataBean convertFeatureDocumentToFeatureDataBean(SyncCenterFeatureDocument featureDocument) {
        FeatureDataBean featureDataBean = null;
//        Gson gson = new Gson();
        if (featureDocument != null) {
            featureDataBean = new FeatureDataBean();
            featureDataBean.setCssClass(featureDocument.getCssClass());
            featureDataBean.setDescription(featureDocument.getDescription());
            featureDataBean.setDisplayName(featureDocument.getDisplayName());
            featureDataBean.setFeatureName(featureDocument.getName());
            featureDataBean.setFeatureURL(featureDocument.getFeatureURL());
            featureDataBean.setIsActive(featureDocument.getIsActive());
            featureDataBean.setIsCrud(featureDocument.getIsCrud());
            featureDataBean.setMenuCategory(featureDocument.getMenuCategory());
            featureDataBean.setMenuLabel(featureDocument.getMenuLabel());
            featureDataBean.setMenuType(featureDocument.getMenuType());
            featureDataBean.setParentId(featureDocument.getParentId());
            featureDataBean.setParentName(featureDocument.getParentName());
            featureDataBean.setPrecedence(featureDocument.getPrecedence());
            featureDataBean.setSeqNo(featureDocument.getSeqNo());
            featureDataBean.setWebserviceUrl(featureDocument.getWebserviceUrl());

        }
        return featureDataBean;
    }

    public Set<FeatureDataBean> convertFeatureDocumentListToFeatureDataBeanList(List<SyncCenterFeatureDocument> featureDocuments) {
        Set<FeatureDataBean> featureDataBeans = null;
        Gson gson = new Gson();
        if (featureDocuments != null) {
            featureDataBeans = new HashSet<>();
            for (SyncCenterFeatureDocument featureDocument : featureDocuments) {
                FeatureDataBean fromJson = gson.fromJson(gson.toJson(featureDocument), FeatureDataBean.class);
                featureDataBeans.add(fromJson);
            }
        }
        return featureDataBeans;
    }

    @Override
    public void save(Object object) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object convertEntityToDocument(Object entityObject) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Map<String, List<String>> getQueryParameters() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Map<String, Object> getidMap() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
