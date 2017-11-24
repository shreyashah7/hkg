/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.center.sync.transformers;

import com.argusoft.hkg.common.functionutil.FolderManagement;
import com.argusoft.hkg.common.functionutil.PropertyFileReader;
import com.argusoft.hkg.sync.center.core.HkUMSyncService;
import com.argusoft.hkg.sync.center.core.SyncCenterUserService;
import com.argusoft.hkg.sync.xmpp.util.SyncHelper;
import static com.argusoft.hkg.sync.xmpp.util.SyncTransferType.ONE_TO_MANY;
import com.argusoft.hkg.sync.xmpp.util.SyncTransformerAdapter;
import com.argusoft.hkg.web.center.sync.restclient.SyncRestUtil;
import com.argusoft.hkg.web.config.WebApplicationInitializerConfig;
import com.argusoft.sync.center.model.SyncCenterUserDocument;
import com.argusoft.usermanagement.common.model.UMUser;
import com.argusoft.usermanagement.common.model.UMUserRole;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.LazyInitializationException;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 *
 * @author shruti
 */
@Service
public class SyncCenterUserTransformer extends SyncTransformerAdapter {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(SyncCenterUserTransformer.class);
    @Autowired
    SyncCenterUserRoleTransformer userRoleTransformer;
    @Autowired
    private SyncRestUtil syncRestUtil;
    @Autowired
    private SyncCenterUserService userCenterService;
    public String PROFILE = "PROFILE";
    public String OTHER = "OTHER";
    public String BACKGROUND = "BACKGROUND";
    public String TMP_FILES_BASE;
    @Autowired
    private HkUMSyncService hkUMSyncService;
    private final Map<String, List<String>> queryParametersMap;
    private final Map<String, Object> idMap;
    String folderName;

    private static final String ID = "id";

    public SyncCenterUserTransformer() {
        this.idMap = new HashMap<>();
        this.queryParametersMap = new HashMap<>();
        String fileName = "jdbc.properties";
        PropertyFileReader propertyFileReader = new PropertyFileReader();
        folderName = propertyFileReader.getPropertyValue("repository.folder.name", fileName);
        TMP_FILES_BASE = System.getProperty("user.home") + File.separator + folderName + File.separator + "TEMP" + File.separator;
    }

    @Override
    public Object convertEntityToDocument(Object entityObject) {
        SyncCenterUserDocument centerDocument = null;
        if (entityObject != null && entityObject instanceof UMUser) {
            UMUser uMUser = (UMUser) entityObject;

            centerDocument = new SyncCenterUserDocument();
            centerDocument.setCompany(uMUser.getCompany());
//            centerDocument.setCreatedOn(uMUser.getCreatedOn());
            if (uMUser.getDepartment() != null) {
                centerDocument.setDepartmentId(uMUser.getDepartment());
            }

            centerDocument.setEmailAddress(uMUser.getEmailAddress());
            centerDocument.setExpiredOn(uMUser.getExpiredOn());
            centerDocument.setFirstName(uMUser.getFirstName());
            centerDocument.setMiddleName(uMUser.getMiddleName());
            centerDocument.setLastName(uMUser.getLastName());
            centerDocument.setId(uMUser.getId());
            centerDocument.setIsActive(uMUser.getIsActive());
            centerDocument.setIsArchive(uMUser.getIsArchive());
            centerDocument.setPassword(uMUser.getPassword());
//            centerDocument.setPreferredLanguage(uMUser.getPreferredLanguage());
            centerDocument.setStatus(uMUser.getStatus());
            centerDocument.setUserCode(uMUser.getUserCode());
            centerDocument.setUserId(uMUser.getUserId());
            centerDocument.setCustom2(uMUser.getCustom2());
            if (uMUser.getModifiedOn() != null) {
                centerDocument.setModifiedOn(uMUser.getModifiedOn());
            } else {
                centerDocument.setModifiedOn(new Date());
            }
            List<Long> roleList = new LinkedList<>();
            if (!CollectionUtils.isEmpty(uMUser.getUMUserRoleSet())) {
                for (UMUserRole role : uMUser.getUMUserRoleSet()) {
                    roleList.add(role.getuMUserRolePK().getRole());
                }
            }
            centerDocument.setRoleId(roleList);
            try {
                centerDocument.setuMUserRoleSet(userRoleTransformer.convertUserRoleListToUserRoleCenterList(uMUser.getUMUserRoleSet()));
            } catch (LazyInitializationException lazyInitializationException) {
                LOGGER.error(lazyInitializationException.getMessage());
            }
            idMap.put(ID, uMUser.getId());
            queryParametersMap.put(SyncHelper.FRANCHISE_ID, Arrays.asList(uMUser.getCompany().toString()));
        }
        return centerDocument;
    }

    @Override
    public int getSyncTransferType() {
        return ONE_TO_MANY;
    }

    @Override
    public void save(Object object) {
        if (object != null && object instanceof SyncCenterUserDocument) {
            SyncCenterUserDocument document = (SyncCenterUserDocument) object;
            SyncCenterUserDocument currentDocument = (SyncCenterUserDocument) hkUMSyncService.getDocumentById(document.getUserId(), SyncCenterUserDocument.class);
            if (currentDocument != null && !isUpdatable(currentDocument.getModifiedOn(), document.getModifiedOn())) {
                return;
            }
            String profilePicturePath = document.getProfilePicturePath();
            if (!StringUtils.isEmpty(profilePicturePath)) {
//                document.setProfilePicturePath(profilePicturePath);
                LOGGER.debug("$$profilePicturePath  " + profilePicturePath);
                String[] splitUserHomeDir = profilePicturePath.split( File.separator);
//                String[] split1 = profilePicturePath.split(File.separator);
//                System.out.println("profilePicturePath.split(folderName + File.separator);" + split1[split1.length - 1]);
//                profilePicturePath = profilePicturePath.replace(splitUserHomeDir[0], System.getProperty("user.home") + File.separator);
//                String[] split = profilePicturePath.split("PROFILE/");
                profilePicturePath = FolderManagement.getPathOfImage(splitUserHomeDir[splitUserHomeDir.length - 1]);
                File file = new File(profilePicturePath.substring(0, (profilePicturePath.lastIndexOf(File.separator) + 1)));
                if (!file.exists()) {
                    file.mkdirs();
                }
                                syncRestUtil.saveFile(WebApplicationInitializerConfig.MASTER_IP + "api/init/retrieve/localefile", document.getProfilePicturePath(), null, profilePicturePath, MediaType.APPLICATION_OCTET_STREAM_VALUE);
                file = new File(profilePicturePath);
                try {
                    FolderManagement.createThumbnail(file, profilePicturePath);

                } catch (IOException ex) {
                    Logger.getLogger(SyncCenterUserTransformer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            document.setProfilePicturePath(profilePicturePath);
            hkUMSyncService.saveOrUpdateDocument(document);
        }
    }

    public SyncCenterUserDocument retrieveByUsername(String username) {
        return userCenterService.getUserbyUserName(username, false, Boolean.TRUE);
    }

    @Override
    public boolean isStaticDocumentType() {
        return false;
    }

    @Override
    public Map<String, List<String>> getQueryParameters() {
        return queryParametersMap;
    }

    @Override
    public Map<String, Object> getidMap() {
        return idMap;
    }

    public String uploadFile(String fileName, String fileType, Long companyId, Long userId) throws FileNotFoundException, IOException {
        String tempFileName = null;
        if (fileType.equals(PROFILE) || fileType.equals(OTHER) || fileType.equals(BACKGROUND)) {
            tempFileName = FolderManagement.getTempFileName(companyId, FolderManagement.FEATURE.USER, fileType, userId, fileName);
        }
        return tempFileName;
    }

}
