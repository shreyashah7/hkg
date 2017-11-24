/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.center.transformers;

import com.argusoft.hkg.common.constantutil.HkSystemConstantUtil;
import com.argusoft.hkg.model.HkValueEntity;
import com.argusoft.hkg.nosql.core.HkFoundationDocumentService;
import com.argusoft.hkg.web.center.usermanagement.databeans.LoginDataBean;
import com.argusoft.hkg.web.master.databeans.MasterDataBean;
import com.argusoft.hkg.web.util.SelectItem;
import com.argusoft.sync.center.model.HkMasterDocument;
import com.argusoft.sync.center.model.HkMasterValueDocument;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 *
 * @author akta
 */
@Service
public class CenterMasterTransformer {

    @Autowired
    LoginDataBean loginDataBean;

    @Autowired
    HkFoundationDocumentService foundationDocumentService;
    @Autowired
    HkFoundationDocumentService hkFoundationDocumentService;

    public static String RETRIEVE_ALL = "ALL";
    public static String RETRIEVE_BY_ID = "BY_ID";

    public List<MasterDataBean> retrieveMasters() {
        int userPrecedence = loginDataBean.getPrecedence();
        Long companyId = loginDataBean.getCompanyId();
        List<HkMasterDocument> hkMasterEntitys = hkFoundationDocumentService.retrieveMasters(companyId, (short) userPrecedence, null, Boolean.FALSE);
        List<MasterDataBean> masterDataBeans = new ArrayList<>();
        if (!CollectionUtils.isEmpty(hkMasterEntitys)) {
            for (HkMasterDocument hkMasterEntity : hkMasterEntitys) {
                MasterDataBean masterDataBean = new MasterDataBean();
                masterDataBean = this.convertHkMasterDocumentToMasterDatabean(masterDataBean, hkMasterEntity, RETRIEVE_ALL);
                masterDataBeans.add(masterDataBean);
            }
        }
        return masterDataBeans;
    }

    private MasterDataBean convertHkMasterEntityToMasterDatabean(MasterDataBean masterDataBean, HkMasterDocument masterdocument, String RETRIEVE_BY_ID) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private MasterDataBean convertHkMasterDocumentToMasterDatabean(MasterDataBean masterDataBean, HkMasterDocument hkMasterEntity, String retrieve) {
        if (masterDataBean == null) {
            masterDataBean = new MasterDataBean();
        }
        if (retrieve.equalsIgnoreCase(RETRIEVE_ALL)) {
            Map<String, List<String>> mapOfFeatureUsedInMaster = HkSystemConstantUtil.MASTER_FEATURE_MAP;
            masterDataBean.setMasterName(hkMasterEntity.getMasterName());
            masterDataBean.setCode(hkMasterEntity.getCode());
            masterDataBean.setId(hkMasterEntity.getCode());
            masterDataBean.setIsSensitiveMaster(hkMasterEntity.isIsSensitive());
            masterDataBean.setMasterType(hkMasterEntity.getMasterType());
            if (!HkSystemConstantUtil.MASTER_FEATURE_MAP.isEmpty()) {
                for (Map.Entry<String, List<String>> entry : mapOfFeatureUsedInMaster.entrySet()) {
                    String code = entry.getKey();
                    List<String> value = entry.getValue();
                    String replace = value.toString();
                    String usedInFeauture = replace.replaceAll("\\[", "").replaceAll("\\]", "");
                    if (masterDataBean.getCode().equalsIgnoreCase(code)) {
                        masterDataBean.setUsedInFeature(usedInFeauture.toString());
                    }
                }
            }
        } else {
            masterDataBean.setMasterName(hkMasterEntity.getMasterName());
            masterDataBean.setCode(hkMasterEntity.getCode());
            masterDataBean.setId(hkMasterEntity.getCode());
            masterDataBean.setIsSensitiveMaster(hkMasterEntity.isIsSensitive());
            masterDataBean.setMasterType(hkMasterEntity.getMasterType());
            List<String> listCode = new ArrayList<>();
            listCode.add(hkMasterEntity.getCode());
            List<HkMasterValueDocument> hkValueEntitys = hkFoundationDocumentService.retrieveMasterValueByCode(listCode);
            if (!CollectionUtils.isEmpty(hkValueEntitys)) {
                List<MasterDataBean> masterDataBeans = new ArrayList<>();
                for (HkMasterValueDocument hkValueEntity : hkValueEntitys) {
                    MasterDataBean dataBean = new MasterDataBean();
                    dataBean.setIsArchieve(!hkValueEntity.getIsActive());
                    dataBean.setIsOftenUsed(hkValueEntity.isIsOftenUsed());
                    dataBean.setShortcutCode(hkValueEntity.getShortcutCode());
                    String value = hkValueEntity.getValueName();
                    if (hkValueEntity.getTranslatedValueName() != null && !hkValueEntity.getTranslatedValueName().isEmpty()) {
                        value = hkValueEntity.getTranslatedValueName();
                    }
                    dataBean.setValue(value);
                    dataBean.setValueEntityId(hkValueEntity.getId());
                    dataBean.setCodeValue(hkValueEntity.getValueName());
//                    TODO if map is null then initialize it by new map else it will not allow to set value in UI
                    if (hkValueEntity.getTranslateValueMap() == null) {
                        dataBean.setLangaugeIdNameMap(new HashMap<String, String>());
                    } else {
                        dataBean.setLangaugeIdNameMap(hkValueEntity.getTranslateValueMap());
                    }
                    masterDataBeans.add(dataBean);
                }
                masterDataBean.setMasterDataBeans(masterDataBeans);
            }
        }
        return masterDataBean;
    }

    public MasterDataBean retrieveMasterById(String code) {
        int precedence = loginDataBean.getPrecedence();
        HkMasterDocument hkMasterEntity = hkFoundationDocumentService.retrieveMaster(code, (short) precedence, loginDataBean.getCompanyId());
        MasterDataBean masterDataBean = new MasterDataBean();
        masterDataBean = this.convertHkMasterDocumentToMasterDatabean(masterDataBean, hkMasterEntity, RETRIEVE_BY_ID);
        return masterDataBean;
    }

    public List<SelectItem> retrieveAllValuesForMasters(String keyCode, String searchQuery) {

        List<SelectItem> selectItems = null;

        List<HkMasterValueDocument> valueEntities = hkFoundationDocumentService.retrieveMasterValueByCodeForCustomMasters(keyCode, searchQuery);

        if (!CollectionUtils.isEmpty(valueEntities)) {
            selectItems = new ArrayList<>();

            for (HkMasterValueDocument valueEntity : valueEntities) {
                SelectItem selectItem = new SelectItem(valueEntity.getId(), valueEntity.getValueName());
                selectItem.setShortcutCode(valueEntity.getShortcutCode());
                selectItem.setIsActive(valueEntity.isIsOftenUsed());
                selectItems.add(selectItem);
            }

        }
        return selectItems;
    }

    public Map<String, List<SelectItem>> retrieveAllValuesForMasters(List<String> keyCodes, String searchQuery) {
        Map<String, List<SelectItem>> selectItems = new HashMap<>();
        List<HkMasterValueDocument> valueEntities = hkFoundationDocumentService.retrieveMasterValueByCode(keyCodes);

        if (!CollectionUtils.isEmpty(valueEntities)) {
            for (HkMasterValueDocument valueEntity : valueEntities) {
                SelectItem selectItem = new SelectItem(valueEntity.getId(), valueEntity.getValueName());
                selectItem.setShortcutCode(valueEntity.getShortcutCode());
                selectItem.setIsActive(valueEntity.getIsActive());
                if (selectItems.get(valueEntity.getCode()) == null) {
                    selectItems.put(valueEntity.getCode(), new ArrayList<>());
                }
                selectItems.get(valueEntity.getCode()).add(selectItem);
            }
        }
        return selectItems;
    }
}
