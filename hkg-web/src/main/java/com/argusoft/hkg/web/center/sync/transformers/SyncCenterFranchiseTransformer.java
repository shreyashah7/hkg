/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.center.sync.transformers;
import com.argusoft.hkg.sync.center.core.SyncCenterFranchiseService;
import com.argusoft.hkg.web.center.usermanagement.databeans.CenterFranchiseDataBean;
import com.argusoft.sync.center.model.SyncCenterFranchiseDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author shruti
 */
@Service
public class SyncCenterFranchiseTransformer {

    @Autowired
    private SyncCenterFranchiseService centerFranchiseService;

    public SyncCenterFranchiseService getCenterFranchiseService() {
        return centerFranchiseService;
    }

    public void setCenterFranchiseService(SyncCenterFranchiseService centerFranchiseService) {
        this.centerFranchiseService = centerFranchiseService;
    }

    public CenterFranchiseDataBean retrieveById(Long id) {
        SyncCenterFranchiseDocument retrieveById = centerFranchiseService.retrieveById(id);
        return convertFranchiseDocumentToFranchiseDataBean(retrieveById);
    }

    public void addFranchise(CenterFranchiseDataBean dataBean) {
        dataBean.setId(1);
        centerFranchiseService.saveOrUpdate(convertFranchiseDataBeantToFranchiseDocument(dataBean));
    }

    public CenterFranchiseDataBean convertFranchiseDocumentToFranchiseDataBean(SyncCenterFranchiseDocument document) {
        CenterFranchiseDataBean dataBean = null;
        System.out.println("convertFranchiseDocumentToFranchiseDataBean " + document);
        if (document != null) {
            dataBean = new CenterFranchiseDataBean();
            dataBean.setId(document.getId().intValue());
            dataBean.setFranchiseName(document.getName());
            dataBean.setFranchiseAdminId(document.getFranchiseAdminId());
            dataBean.setFranchiseId(document.getFranchiseId());
        }
        System.out.println("databean: " + dataBean);
        return dataBean;
    }

    public SyncCenterFranchiseDocument convertFranchiseDataBeantToFranchiseDocument(CenterFranchiseDataBean dataBean) {
        SyncCenterFranchiseDocument document = null;
        if (dataBean != null) {
            document = new SyncCenterFranchiseDocument();
            document.setId((long) dataBean.getId());
            document.setFranchiseId(dataBean.getFranchiseId());
            document.setName(dataBean.getFranchiseName());
            document.setFranchiseAdminId(dataBean.getFranchiseAdminId());
        }
        return document;
    }
}
