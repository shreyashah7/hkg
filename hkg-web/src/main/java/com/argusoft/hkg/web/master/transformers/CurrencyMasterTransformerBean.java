/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.master.transformers;

import com.argusoft.hkg.common.constantutil.HkSystemConstantUtil;
import com.argusoft.hkg.core.HkFoundationService;
import com.argusoft.hkg.model.HkReferenceRateEntity;
import com.argusoft.hkg.nosql.core.HkCustomFieldService;
import com.argusoft.hkg.web.center.usermanagement.databeans.LoginDataBean;
import com.argusoft.hkg.web.master.databeans.CurrencyRateMasterDataBean;
import com.argusoft.hkg.web.util.SelectItem;
import com.argusoft.sync.center.model.HkReferenceRateDocument;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 *
 * @author dhwani
 */
@Service
public class CurrencyMasterTransformerBean {

    @Autowired
    HkFoundationService foundationService;

    @Autowired
    LoginDataBean loginDataBean;

    public HkReferenceRateEntity convertCurrencyRateMasterDataBeanToCurrencyMasterEntity(CurrencyRateMasterDataBean currencyRateMasterDataBean, HkReferenceRateEntity currencyRateEntity) {
        if (currencyRateEntity == null) {
            currencyRateEntity = new HkReferenceRateEntity();
        }
        currencyRateEntity.setIsActive(Boolean.TRUE);
        currencyRateEntity.setIsArchive(Boolean.FALSE);
        currencyRateEntity.setLastModifiedBy(loginDataBean.getId());
        currencyRateEntity.setLastModifiedOn(new Date());
        currencyRateEntity.setFranchise(loginDataBean.getCompanyId());
        return currencyRateEntity;

    }

    public boolean addReferenceRate(CurrencyRateMasterDataBean currencyRateMasterDataBean) {
        HkReferenceRateEntity currencyRateEntity = new HkReferenceRateEntity();
        currencyRateEntity = convertCurrencyRateMasterDataBeanToCurrencyMasterEntity(currencyRateMasterDataBean, currencyRateEntity);
        currencyRateEntity.setCode(currencyRateMasterDataBean.getCode());
        currencyRateEntity.setApplicableFrom(currencyRateMasterDataBean.getApplicableFrom());
        currencyRateEntity.setReferenceRate(currencyRateMasterDataBean.getReferenceRate());
        currencyRateEntity.setFranchise(0l);
        currencyRateEntity.setCreatedByFranchise(loginDataBean.getCompanyId());
//        currencyRateEntity.setPrevCurrencyRate(new HkCurrencyRateEntity(currencyRateMasterDataBean.getPrevReferenceRate()));
        return foundationService.addReferenceRate(currencyRateEntity);
    }

    public List<SelectItem> retrieveCurrencyFromDb() {
        List<SelectItem> selectItems = null;
        if (!CollectionUtils.isEmpty(HkSystemConstantUtil.CURRENCY_MASTER_MAP)) {
            Map<String, String> currencyMapUI = new HashMap<String, String>(HkSystemConstantUtil.CURRENCY_MASTER_MAP);
            selectItems = new ArrayList<>();
            String code = "";
            String currencyLabel = "";
            String description = "";
            if (!CollectionUtils.isEmpty(currencyMapUI)) {
                for (Map.Entry<String, String> entry : currencyMapUI.entrySet()) {
                    code = entry.getKey();
                    currencyLabel = code.concat(" ").concat("(").concat(entry.getValue()).concat(")");;
                    description = "";
                    int indexOf = currencyLabel.indexOf("'");
                    if (indexOf > 0) {
                        description = currencyLabel.substring(indexOf);
                    }
                    SelectItem selectItem = new SelectItem((String) code, currencyLabel, description);
                    selectItems.add(selectItem);
                }
            }
            Collections.sort(selectItems, NameComparator);
        }
        return selectItems;
    }

    public Map<String, List<CurrencyRateMasterDataBean>> retrieveArchivedCurrencyMap() {
        Map<String, List<HkReferenceRateEntity>> retrieveCurrentRateAndCurrencyHistory = foundationService.retrieveCurrentRateAndCurrencyHistory(15, loginDataBean.getCompanyId());
        Map<String, List<CurrencyRateMasterDataBean>> archivedRateMasterMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(retrieveCurrentRateAndCurrencyHistory)) {
            for (Map.Entry<String, List<HkReferenceRateEntity>> entry : retrieveCurrentRateAndCurrencyHistory.entrySet()) {
                List<HkReferenceRateEntity> currencyRateEntitys = entry.getValue();
                List<CurrencyRateMasterDataBean> rateMasterDataBeans = null;
                if (!CollectionUtils.isEmpty(currencyRateEntitys)) {
                    rateMasterDataBeans = new ArrayList<>();
                    for (HkReferenceRateEntity hkCurrencyRateEntity : currencyRateEntitys) {
                        CurrencyRateMasterDataBean rateMasterDataBean = null;
                        if (!hkCurrencyRateEntity.getIsArchive()) {
                            rateMasterDataBean = this.convertHkCurrencyRateEntityToCurrencyRateMasterDataBean(hkCurrencyRateEntity, new CurrencyRateMasterDataBean());
                            rateMasterDataBeans.add(rateMasterDataBean);
                        }
                    }
                }
                if (!CollectionUtils.isEmpty(rateMasterDataBeans)) {
                    archivedRateMasterMap.put(entry.getKey(), rateMasterDataBeans);
                }
            }
        }
        return archivedRateMasterMap;
    }

    public List<CurrencyRateMasterDataBean> retrieveCurrentRateList() {
        List<HkReferenceRateEntity> currentCurrencyRateList = foundationService.retrieveCurrentCurrencyRate(Boolean.TRUE, loginDataBean.getCompanyId());
        List<CurrencyRateMasterDataBean> rateMasterDataBeans = null;
        if (!CollectionUtils.isEmpty(currentCurrencyRateList)) {
            rateMasterDataBeans = new ArrayList<>();
            for (HkReferenceRateEntity hkCurrencyRateEntity : currentCurrencyRateList) {
                CurrencyRateMasterDataBean rateMasterDataBean = null;
                if (hkCurrencyRateEntity.getIsActive()) {
                    rateMasterDataBean = this.convertHkCurrencyRateEntityToCurrencyRateMasterDataBean(hkCurrencyRateEntity, new CurrencyRateMasterDataBean());
                    rateMasterDataBeans.add(rateMasterDataBean);
                }

            }
        }
        return rateMasterDataBeans;
    }

    private CurrencyRateMasterDataBean convertHkCurrencyRateEntityToCurrencyRateMasterDataBean(HkReferenceRateEntity currencyRateEntity, CurrencyRateMasterDataBean currencyRateMasterDataBean) {
        if (currencyRateMasterDataBean == null) {
            currencyRateMasterDataBean = new CurrencyRateMasterDataBean();
        }
        currencyRateMasterDataBean.setApplicableFrom(currencyRateEntity.getApplicableFrom());
        currencyRateMasterDataBean.setCode(currencyRateEntity.getCode());
        currencyRateMasterDataBean.setFranchise(currencyRateEntity.getFranchise());
        currencyRateMasterDataBean.setId(currencyRateEntity.getId());
        currencyRateMasterDataBean.setIsActive(currencyRateEntity.getIsActive());
        currencyRateMasterDataBean.setIsArchive(currencyRateEntity.getIsArchive());
        currencyRateMasterDataBean.setLastModifiedBy(currencyRateEntity.getLastModifiedBy());
        currencyRateMasterDataBean.setLastModifiedOn(currencyRateEntity.getLastModifiedOn());

        currencyRateMasterDataBean.setReferenceRate(currencyRateEntity.getReferenceRate());
        if (!CollectionUtils.isEmpty(HkSystemConstantUtil.CURRENCY_MASTER_MAP)) {
            if (HkSystemConstantUtil.CURRENCY_MASTER_MAP.containsKey(currencyRateEntity.getCode())) {
                currencyRateMasterDataBean.setCurrencyName(HkSystemConstantUtil.CURRENCY_MASTER_MAP.get(currencyRateEntity.getCode()));
            }
        }
        return currencyRateMasterDataBean;
    }

    public void updateReferenceRate(CurrencyRateMasterDataBean rateMasterDataBean) {
        foundationService.updateReferenceRate(rateMasterDataBean.getId(), rateMasterDataBean.getApplicableFrom(), Boolean.TRUE, loginDataBean.getId());
    }

    public static Comparator<SelectItem> NameComparator = new Comparator<SelectItem>() {
        @Override
        public int compare(SelectItem o1, SelectItem o2) {
            return o1.getLabel().compareToIgnoreCase(o2.getLabel());
        }
    };

    public Double retrieveReferenceRateForCurrency(String currency) {
        Double rate = null;
        if (StringUtils.hasText(currency)) {
            HkReferenceRateEntity referenceRateEntity = foundationService.retrieveActiveCurrencyRateByCode(currency);
            if (referenceRateEntity != null) {
                System.out.println("Not null");
                rate = referenceRateEntity.getReferenceRate();
            }
        }
        return rate;
    }
}
