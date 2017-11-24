/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.sync.xmpp.transformers;

import com.argusoft.hkg.common.constantutil.HkSystemConstantUtil;
import com.argusoft.hkg.common.functionutil.FolderManagement;
import com.argusoft.hkg.core.HkFoundationService;
import com.argusoft.hkg.model.HkSystemConfigurationEntity;
import com.argusoft.hkg.sync.center.core.SyncCenterUserService;
import com.argusoft.hkg.sync.xmpp.util.SyncHelper;
import static com.argusoft.hkg.sync.xmpp.util.SyncTransferType.ONE_TO_MANY;
import com.argusoft.hkg.sync.xmpp.util.SyncTransformerAdapter;
import com.argusoft.hkg.web.center.sync.transformers.SyncCenterUserRoleTransformer;
import com.argusoft.hkg.web.usermanagement.wrapper.UserManagementServiceWrapper;
import com.argusoft.sync.center.model.SyncCenterUserDocument;
import com.argusoft.usermanagement.common.core.UMUserService;
import com.argusoft.usermanagement.common.model.UMContactDocument;
import com.argusoft.usermanagement.common.model.UMUser;
import com.argusoft.usermanagement.common.model.UMUserContact;
import com.argusoft.usermanagement.common.model.UMUserRole;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.LazyInitializationException;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 *
 * @author shruti
 */
@Service
public class UmUserTransformer extends SyncTransformerAdapter {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(UmUserTransformer.class);
    @Autowired
    private SyncCenterUserRoleTransformer userRoleTransformer;
    @Autowired
    private SyncCenterUserService userCenterService;
    private final Map<String, List<String>> queryParametersMap;
    private final Map<String, Object> idMap;

    public final String PROFILE = "PROFILE";
    @Autowired
    private HkFoundationService foundationService;
    private static final String ID = "id";
    @Autowired
    private UMUserService userService;
    @Autowired
    private UserManagementServiceWrapper userManagementServiceWrapper;

    public UmUserTransformer() {
        this.idMap = new HashMap<>();
        this.queryParametersMap = new HashMap<>();
    }

    @Override
    public void save(Object object) {

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
            /*
             * This sleep is kept to prevent sync process to start before
             * thumbnail is created so that profilepicture is retrieved from the
             * actual folder and not from TEMP folder.
             */
            try {
                Thread.sleep(5000);
            } catch (InterruptedException ex) {
                Logger.getLogger(UmUserTransformer.class.getName()).log(Level.SEVERE, null, ex);
            }
            centerDocument.setProfilePicturePath(getProfileFullPath(uMUser));
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
            Set<UMUserRole> umUserRoleSet = null;
            try {
                if (!CollectionUtils.isEmpty(uMUser.getUMUserRoleSet())) {
                    umUserRoleSet = uMUser.getUMUserRoleSet();
                } else {
                    umUserRoleSet = new HashSet(userManagementServiceWrapper.retrieveUserRolesByUserId(uMUser.getId(), false, false));
                    uMUser.setUMUserRoleSet(umUserRoleSet);
                }
            } catch (LazyInitializationException e) {
                umUserRoleSet = new HashSet(userManagementServiceWrapper.retrieveUserRolesByUserId(uMUser.getId(), false, false));
                uMUser.setUMUserRoleSet(umUserRoleSet);
            }
            for (UMUserRole role : umUserRoleSet) {
                roleList.add(role.getuMUserRolePK().getRole());
            }
            centerDocument.setuMUserRoleSet(userRoleTransformer.convertUserRoleListToUserRoleCenterList(umUserRoleSet));
            centerDocument.setRoleId(roleList);

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
    public Map<String, List<String>> getQueryParameters() {
        return Collections.unmodifiableMap(queryParametersMap);
    }

    @Override
    public Map<String, Object> getidMap() {
        return Collections.unmodifiableMap(idMap);
    }

    public String retrieveAvatar(Long companyId) {
        String path = null;
        HkSystemConfigurationEntity retrieveSystemConfigurationByKey = foundationService.retrieveSystemConfigurationByKey(HkSystemConstantUtil.FranchiseConfiguration.EMP_DEFAULT_IMAGE, companyId);
        if (retrieveSystemConfigurationByKey != null) {
            path = FolderManagement.getPathOfImage(retrieveSystemConfigurationByKey.getKeyValue());
        }
        return path;
    }

    public String getProfileFullPath(UMUser user) {
        String path = null;
        UMUser loginEmp = userService.retrieveUserById(user.getId(), user.getCompany());

        UMUserContact uMUserContact = loginEmp.getContact();
        String imageName = null;

        Set<UMContactDocument> umContactDocumentSet = uMUserContact.getuMContactDocumentSet();
        if (!CollectionUtils.isEmpty(umContactDocumentSet)) {
            for (UMContactDocument contactDocument : umContactDocumentSet) {
                if (!contactDocument.getIsArchive()) {
                    if (contactDocument.getDocumentType().equalsIgnoreCase(PROFILE)) {
                        imageName = contactDocument.getDocumentName();
                    }
                }
            }
        }
        if (imageName != null) {
            path = FolderManagement.getPathOfImage(imageName);
        }
        return path;
    }
}
