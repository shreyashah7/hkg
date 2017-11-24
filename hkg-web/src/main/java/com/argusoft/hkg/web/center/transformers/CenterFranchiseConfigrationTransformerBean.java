/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.center.transformers;

import com.argusoft.hkg.nosql.core.HkFoundationDocumentService;
import com.argusoft.sync.center.model.HkSystemConfigurationDocument;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author akta
 */
@Service
public class CenterFranchiseConfigrationTransformerBean {

    @Autowired
    HkFoundationDocumentService hkFoundationDocumentService;

    public Map<String, String> retrieveAllConfiguration() {
        List<HkSystemConfigurationDocument> systemConfigurationEntityList = hkFoundationDocumentService.retrieveSystemConfigurationByFranchise(null);
        Map<String, String> systemConfigMap = new HashMap<>();
        if (systemConfigurationEntityList != null && !systemConfigurationEntityList.isEmpty()) {
            for (HkSystemConfigurationDocument systemConfigurationEntity : systemConfigurationEntityList) {
                systemConfigMap.put(systemConfigurationEntity.getId(), systemConfigurationEntity.getKeyValue());
            }
        }
        return systemConfigMap;
    }
}
