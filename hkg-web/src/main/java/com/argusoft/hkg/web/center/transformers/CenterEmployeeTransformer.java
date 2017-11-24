/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.center.transformers;

import com.argusoft.hkg.common.constantutil.HkSystemConstantUtil;
import com.argusoft.hkg.common.functionutil.FolderManagement;
import com.argusoft.hkg.common.functionutil.PropertyFileReader;
import com.argusoft.hkg.sync.center.core.HkUMSyncService;
import com.argusoft.hkg.sync.center.core.SyncCenterUserService;
import com.argusoft.sync.center.model.HkSystemConfigurationDocument;
import com.argusoft.sync.center.model.SyncCenterUserDocument;
import java.io.File;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 *
 * @author shruti
 */
@Service
public class CenterEmployeeTransformer {

    @Autowired
    private SyncCenterUserService centerUserService;

    @Autowired
    private HkUMSyncService hkUMSyncService;

    String folderName;

    @PostConstruct
    public void init() {
        String fileName = "jdbc.properties";
        PropertyFileReader propertyFileReader = new PropertyFileReader();
        folderName = propertyFileReader.getPropertyValue("repository.folder.name", fileName);
    }

    public String getProfileFullPath(Long id) {
        SyncCenterUserDocument loginEmp = centerUserService.retrieveUsersById(id);
        return loginEmp.getProfilePicturePath();

    }

    public String retrieveAvatar() {
        String path = null;
        HkSystemConfigurationDocument retrieveSystemConfigurationByKey = (HkSystemConfigurationDocument) hkUMSyncService.getDocumentById(HkSystemConstantUtil.FranchiseConfiguration.EMP_DEFAULT_IMAGE, HkSystemConfigurationDocument.class);
        if (retrieveSystemConfigurationByKey != null && !StringUtils.isEmpty(retrieveSystemConfigurationByKey.getKeyValue())) {

            path = FolderManagement.getPathOfImage(retrieveSystemConfigurationByKey.getKeyValue());
//            System.out.println("Avtar::: " + path);
            String[] split = path.split(System.getProperty("user.home"));
            if (split.length > 1) {
                path = System.getProperty("user.home") + File.separator + folderName + split[1];
            };
        }
        return path;
    }
}
