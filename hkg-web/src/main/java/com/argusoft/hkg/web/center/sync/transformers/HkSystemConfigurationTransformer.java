/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.center.sync.transformers;

import com.argusoft.hkg.common.constantutil.HkSystemConstantUtil;
import com.argusoft.hkg.common.functionutil.FolderManagement;
import com.argusoft.hkg.common.functionutil.PropertyFileReader;
import com.argusoft.hkg.model.HkSystemConfigurationEntity;
import com.argusoft.hkg.model.HkSystemConfigurationEntityPK;
import com.argusoft.hkg.sync.center.core.HkSystemConfigurationService;
import com.argusoft.hkg.sync.xmpp.util.HkSyncConstantUtil;
import com.argusoft.hkg.sync.xmpp.util.SyncHelper;
import com.argusoft.hkg.sync.xmpp.util.SyncTransferType;
import com.argusoft.hkg.sync.xmpp.util.SyncTransformerAdapter;
import com.argusoft.hkg.web.center.sync.restclient.SyncRestUtil;
import com.argusoft.hkg.web.center.util.ApplicationUtil;
import com.argusoft.hkg.web.config.WebApplicationInitializerConfig;
import com.argusoft.sync.center.model.HkSystemConfigurationDocument;
import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 *
 * @author akta
 */
@Service
public class HkSystemConfigurationTransformer extends SyncTransformerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(HkSystemConfigurationTransformer.class);
    @Autowired
    HkSystemConfigurationService systemConfigurationService;
    @Autowired
    private ApplicationUtil applicationUtil;
    @Autowired
    private SyncRestUtil syncRestUtil;
    private final Map<String, List<String>> queryParametersMap;
    private final Map<String, Object> idMap;
    private String folderName;

    public HkSystemConfigurationTransformer() {
        this.idMap = new HashMap<>();
        this.queryParametersMap = new HashMap<>();
    }

    @PostConstruct
    public void init() {
        String fileName = "jdbc.properties";
        PropertyFileReader propertyFileReader = new PropertyFileReader();
        folderName = propertyFileReader.getPropertyValue("repository.folder.name", fileName);
    }

    @Override
    public void save(Object object) {
        if (object != null && object instanceof HkSystemConfigurationDocument) {
            HkSystemConfigurationDocument systemConfigurationDocumentNew = (HkSystemConfigurationDocument) object;
            String id = systemConfigurationDocumentNew.getId();
            HkSystemConfigurationDocument systemConfigurationDocumentExisting = systemConfigurationService.getSystemConfigBySystemKey(id);
            if (systemConfigurationDocumentExisting != null && !isUpdatable(systemConfigurationDocumentExisting.getModifiedOn(), systemConfigurationDocumentNew.getModifiedOn())) {
                return;
            }
            systemConfigurationService.saveSystemConfig((HkSystemConfigurationDocument) object);
            if (id.equals(HkSystemConstantUtil.FranchiseConfiguration.EMP_DEFAULT_IMAGE)) {
                getAvtar(systemConfigurationDocumentNew.getKeyValue());
            }
        }
    }

    @Override
    public Object convertEntityToDocument(Object entityObject) {
        HkSystemConfigurationEntity entity = (HkSystemConfigurationEntity) entityObject;
        HkSystemConfigurationEntityPK hkSystemConfigurationEntityPK = entity.getHkSystemConfigurationEntityPK();
        long franchise = entity.getHkSystemConfigurationEntityPK().getFranchise();
        if (hkSystemConfigurationEntityPK.getSystemKey().equals(HkSystemConstantUtil.FranchiseConfiguration.DEFAULT_XMPP_EMAIL_ADDRESS)) {
            queryParametersMap.put(SyncHelper.FRANCHISE_ID, null);
        } else {
            queryParametersMap.put(SyncHelper.FRANCHISE_ID, Arrays.asList(String.valueOf(franchise)));
        }
        idMap.put(SyncHelper.encodeMapKeyWithDot("hkSystemConfigurationEntityPK.systemKey"), hkSystemConfigurationEntityPK.getSystemKey());
        idMap.put(SyncHelper.encodeMapKeyWithDot("hkSystemConfigurationEntityPK.franchise"), hkSystemConfigurationEntityPK.getFranchise());
        Object systemConfigurationDocument = new HkSystemConfigurationDocument();
        if (HkSyncConstantUtil.ENTITY_FIELD_MAP.containsKey(entityObject.getClass())) {
            systemConfigurationDocument = super.convertEntityToDocument(entityObject, HkSystemConfigurationDocument.class, systemConfigurationDocument);
            systemConfigurationDocument = super.convertEntityToDocument(((HkSystemConfigurationEntity) entityObject).getHkSystemConfigurationEntityPK(), HkSystemConfigurationDocument.class, systemConfigurationDocument);
        }

        return systemConfigurationDocument;
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

    private void getAvtar(String keyValue) {
        String userHome = System.getProperty("user.home");
        System.out.println("keyvalue: " + keyValue);
        if (!StringUtils.isEmpty(keyValue)) {
            String pathOfImage = FolderManagement.getPathOfImage(keyValue);
            String path = userHome + File.separator + folderName + pathOfImage.split(userHome)[1];
//        String path = "/home/shruti/hkg/FRANCHISE/" + companyId + "/COMMON/COMMON~@2~@1430313198597*test1.png";
            LOGGER.debug("PATH:::: " + path);

            syncRestUtil.saveFile(WebApplicationInitializerConfig.MASTER_IP + "api/init/retrieve/avtar", applicationUtil.getCenterFranchiseDataBean().getFranchiseId().toString(), null, path, MediaType.APPLICATION_OCTET_STREAM_VALUE);
            LOGGER.debug("File Saved");
        }
    }
}
