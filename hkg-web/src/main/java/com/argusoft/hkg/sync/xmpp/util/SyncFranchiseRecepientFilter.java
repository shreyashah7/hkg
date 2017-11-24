/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.sync.xmpp.util;

import com.argusoft.hkg.web.center.util.ApplicationUtil;
import com.argusoft.hkg.web.config.WebApplicationInitializerConfig;
import com.argusoft.sync.center.model.VcardDocument;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.jivesoftware.smackx.vcardtemp.packet.VCard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 *
 * @author shruti
 */
@Service
public class SyncFranchiseRecepientFilter implements SyncRecepientFilter {

    private static final String FRANCHISE_ID = "FranchiseId";
    @Autowired
    private ApplicationUtil applicationUtil;

    private String currentFranchise = "0";

    public void init() {
        if (!WebApplicationInitializerConfig.IS_MASTER) {
            currentFranchise = ((Integer) applicationUtil.getCenterFranchiseDataBean().getId()).toString();
        }
    }

    @Override
    public Set<String> filterRecepient(Map<String, Object> vcards, Map<String, List<String>> queryParams) {
        Set<String> recepients = null;
        if (vcards != null && queryParams != null) {
            recepients = new HashSet<>();
            if (queryParams.containsKey(FRANCHISE_ID)) {
                List<String> franchiseList = queryParams.get(FRANCHISE_ID);
                if (!CollectionUtils.isEmpty(franchiseList)) {
                    for (String franchise : franchiseList) {
                        if (franchise.equals(currentFranchise)) {
                            continue;
                        }
                        if (vcards.containsKey(franchise)) {
                            recepients.add(getJid(vcards.get(franchise)));
                        }
                    }
                } else {
                    for (Map.Entry<String, Object> entrySet : vcards.entrySet()) {
                        recepients.add(getJid(entrySet.getValue()));
                    }
                    return recepients;
                }

            }
            if (!WebApplicationInitializerConfig.IS_MASTER && vcards.containsKey("0")) {
                recepients.add(getJid(vcards.get("0")));
            }

        }
        return recepients;
    }

    private String getJid(Object object) {
        if (object instanceof VCard) {
            VCard value = (VCard) object;
            return value.getJabberId();
        } else if (object instanceof VcardDocument) {
            VcardDocument document = (VcardDocument) object;
            return document.getJid();
        }
        return null;
    }
}
