 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.sync.init.controllers;

import com.argusoft.generic.database.exception.GenericDatabaseException;
import com.argusoft.hkg.common.constantutil.HkSystemConstantUtil;
import com.argusoft.hkg.common.functionutil.FolderManagement;
import com.argusoft.hkg.core.HkFoundationService;
import com.argusoft.hkg.model.HkSystemConfigurationEntity;
import com.argusoft.hkg.nosql.model.HkPlanDocument;
import com.argusoft.hkg.nosql.model.HkSellDocument;
import com.argusoft.hkg.nosql.model.HkTransferDocument;
import com.argusoft.hkg.sync.center.core.HkUMSyncService;
import com.argusoft.hkg.sync.xmpp.databeans.SyncXmppUserDatabean;
import com.argusoft.hkg.sync.xmpp.util.CountDatabean;
import com.argusoft.hkg.web.center.sync.restclient.SyncRestClient;
import static com.argusoft.hkg.web.center.sync.restclient.SyncRestClient.CLASS_NAME;
import static com.argusoft.hkg.web.center.sync.restclient.SyncRestClient.LIMIT;
import static com.argusoft.hkg.web.center.sync.restclient.SyncRestClient.OFFSET;
import com.argusoft.hkg.web.center.util.ApplicationUtil;
import com.argusoft.hkg.web.config.WebApplicationInitializerConfig;
import com.argusoft.hkg.web.sync.xmpp.transformers.SyncMenuTransformer;
import com.argusoft.hkg.web.sync.xmpp.transformers.SyncXmppUserTransformer;
import com.argusoft.hkg.web.usermanagement.wrapper.UserManagementServiceWrapper;
import com.argusoft.internationalization.common.core.I18nService;
import com.argusoft.internationalization.common.model.I18nLanguageEntity;
import com.argusoft.sync.center.model.CenterCompanyFeatureSectionDocument;
import com.argusoft.sync.center.model.SyncCenterRoleFeatureDocument;
import com.argusoft.usermanagement.common.model.UMUser;
import com.googlecode.genericdao.search.Filter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author shruti
 */
@RestController
@RequestMapping("/init")
public class SyncCenterInitController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SyncCenterInitController.class);
    @Autowired
    SyncMenuTransformer menuTransformer;

    @Autowired
    HkFoundationService foundationService;

    @Autowired
    private UserManagementServiceWrapper userManagementServiceWrapper;

    @Autowired
    SyncXmppUserTransformer hkXmppUserTransformer;

    @Autowired
    ApplicationUtil applicationUtil;

    @Autowired
    SyncInitialSetupUtil initialSetupUtil;

    @Autowired
    I18nService i18nService;

    @Autowired
    private HkUMSyncService syncService;
    private Long franchise;

    public UserManagementServiceWrapper getUserManagementServiceWrapper() {
        return userManagementServiceWrapper;
    }

    public void setUserManagementServiceWrapper(UserManagementServiceWrapper userManagementServiceWrapper) {
        this.userManagementServiceWrapper = userManagementServiceWrapper;
    }

    @RequestMapping(value = "/retrieve/users", method = RequestMethod.POST)
    public List<String> getUserByFranchiseId(@RequestBody Map<String, Long> paramMap) throws GenericDatabaseException, InstantiationException, IllegalAccessException {
        Long franchiseId = paramMap.get("franchiseId");
        LOGGER.debug("##########franchiseId: " + franchiseId);
//        List<Long> retrieveAllUsersByFranchise = userManagementServiceWrapper.retrieveAllUsersByFranchise(franchiseId);
//        List<String> requires = new ArrayList<>();
//        requires.add("uMUserRoleSet");
//        List<SyncCenterUserDocument> users = new ArrayList<>();
//        for (Long long1 : retrieveAllUsersByFranchise) {
//            UMUser retrieveUserById = umUserService.retrieveUserById(long1, requires);
//            users.add((SyncCenterUserDocument) userTransformer.convertEntityToDocument(retrieveUserById));
//        }
        Map<Integer, Map<String, Object>> criteriaMap = new HashMap<>();
        Map<String, Object> fieldValueMap = new HashMap<>();
        List<Object> valueList = new LinkedList<>();
//        valueList.add(0l);
//        valueList.add(1l);
//        if (!franchiseId.equals(1l) && !franchiseId.equals(0l)) {
        valueList.add(franchiseId);
//        }
        fieldValueMap.put("company", valueList);
        criteriaMap.put(Filter.OP_IN, fieldValueMap);
        fieldValueMap = new HashMap<>();
        fieldValueMap.put("isArchive", Boolean.FALSE);
        criteriaMap.put(Filter.OP_EQUAL, fieldValueMap);
        return initialSetupUtil.retrieveJsonDocuments(UMUser.class, criteriaMap);
//        return userManagementServiceWrapper.retrieveUsers(retrieveAllUsersByFranchise, false);
    }

    @RequestMapping(value = "/retrieve/rolefeature", method = RequestMethod.GET)
    public SyncCenterRoleFeatureDocument getRoleFeatureDocument() {
        return (SyncCenterRoleFeatureDocument) menuTransformer.convertEntityToDocument(null);
    }

    @RequestMapping(value = "/retrieve/count", method = RequestMethod.POST)
    public CountDatabean getCountForDocument(@RequestBody String documentName) throws ClassNotFoundException {
        CountDatabean countDatabean = new CountDatabean();
        countDatabean.setCount(syncService.getCountofActiveDocuments(Class.forName(documentName), "isArchive", "isActive"));
        return countDatabean;
    }

    @RequestMapping(value = "/retrieve/docswithlimit", method = RequestMethod.POST)
    public List<?> getDocumentsList(@RequestBody Map<String, String> paramMap) throws ClassNotFoundException {
        return syncService.getDocumentsWithLimit(Long.parseLong(paramMap.get(OFFSET)), Long.parseLong(paramMap.get(LIMIT)), Class.forName(paramMap.get(CLASS_NAME)));

    }

    @RequestMapping(value = "/retrieve/xmppCredentials", method = RequestMethod.POST)
    public SyncXmppUserDatabean getXmppUserInfo(@RequestBody Map<String, Long> paramMap) {
        Long franchiseId = paramMap.get("franchiseId");
        franchise = franchiseId;
        menuTransformer.setFranchiseId(franchiseId);

        return (SyncXmppUserDatabean) hkXmppUserTransformer.getHkXmppUserByFranchiseId(franchiseId);
    }

    @RequestMapping(value = "/retrieve/featuresectionmap", method = RequestMethod.POST)
    public CenterCompanyFeatureSectionDocument getFeatureSectionMap(@RequestBody Map<String, Long> paramMap) {
        Long franchiseId = paramMap.get("franchiseId");
        CenterCompanyFeatureSectionDocument centerCompanyFeatureSectionDocument = new CenterCompanyFeatureSectionDocument();
        centerCompanyFeatureSectionDocument.setFeatureSectionMap(applicationUtil.getFeatureFromTemplateMap().get(0l));
        centerCompanyFeatureSectionDocument.setId(1);
        centerCompanyFeatureSectionDocument.setModifiedOn(new Date());
        centerCompanyFeatureSectionDocument.setCustomFieldVersion(applicationUtil.getSystemConfigrationMap().get(HkSystemConstantUtil.CUSTOM_FIELD_VERSION));
        return centerCompanyFeatureSectionDocument;
    }

    @RequestMapping(value = "/retrieve/documents", method = RequestMethod.POST)
    public List<String> getListValues(@RequestBody Map<String, String> paramMap) throws InstantiationException, ClassNotFoundException, IllegalAccessException {
        LOGGER.debug(franchise + "   ");
        String companyFieldName = paramMap.get("companyFieldName");
        String isArchieveFieldName = paramMap.get("isArchieveFieldName");
        String className = paramMap.get("className");
        Boolean isMongoDocument = Boolean.valueOf(paramMap.get("isMongoDocument"));

        Long franchiseId = null;
        if (!StringUtils.isEmpty(companyFieldName)) {
            String franchiseString;
            franchiseString = paramMap.get(SyncRestClient.COMPANY_ID);

            if (franchiseString != null) {
                franchiseId = Long.parseLong(franchiseString);
            }
        }
        LOGGER.debug("Class name: " + className + " , isMongoDocument" + isMongoDocument + " ");
        Class<?> forName = Class.forName(className);
        if (forName.equals(HkPlanDocument.class)) {
            return initialSetupUtil.retrievePlanDocument(franchise, forName);
        } else if ((forName.equals(HkSellDocument.class)) || (forName.equals(HkTransferDocument.class))) {
            return initialSetupUtil.retrieveSellAndTransfer(franchise, forName);
        }
        return initialSetupUtil.retrieveJsonDocuments(forName, isArchieveFieldName, companyFieldName, franchiseId, isMongoDocument);
    }

    @RequestMapping(value = "/retrieve/localefilenames", method = RequestMethod.GET)
    public List<String> getI18nFiles() {
        String filePath = WebApplicationInitializerConfig.projectDirectory + File.separator;
        System.out.println("filePath " + filePath);
        List<String> fileNames = new LinkedList<>();
        List<I18nLanguageEntity> activeLanguages = i18nService.retriveActiveLanguages();
        for (I18nLanguageEntity activeLanguage : activeLanguages) {
            String fileName = filePath + activeLanguage.getLanguagePK().getCode() + franchise + ".json";
            fileNames.add(fileName);
        }
        return fileNames;
    }

    @RequestMapping(value = "/retrieve/localefile", method = RequestMethod.POST, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public byte[] getFile(@RequestBody String filename, HttpServletRequest request) throws IOException {

        System.out.println("filename: " + filename);
//        InputStream in = request.getServletContext().getResourceAsStream(filename);

        return FileUtils.readFileToByteArray(new File(filename));
//        IOUtils.toByteArray(in);
    }

    @RequestMapping(value = "/retrieve/avtar", method = RequestMethod.POST, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public byte[] getdefaultImage(@RequestBody String companyIdString, HttpServletRequest request) throws IOException {
        String path = null;
        Long companyId = Long.parseLong(companyIdString);
        HkSystemConfigurationEntity retrieveSystemConfigurationByKey = foundationService.retrieveSystemConfigurationByKey(HkSystemConstantUtil.FranchiseConfiguration.EMP_DEFAULT_IMAGE, companyId);
        if (retrieveSystemConfigurationByKey != null) {
            path = FolderManagement.getPathOfImage(retrieveSystemConfigurationByKey.getKeyValue());
        }
//        InputStream in = request.getServletContext().getResourceAsStream(filename);
        System.out.println("retrieveAvatar: " + path);
        if (!StringUtils.isEmpty(path)) {
            return FileUtils.readFileToByteArray(new File(path));
        }
        return null;
//        IOUtils.toByteArray(in);
    }

    public void uploadFile(HttpServletRequest request, HttpServletResponse response, File file) {
        OutputStream outStream;
        // get MIME type of the file
        try (FileInputStream inputStream = new FileInputStream(file)) {
            // get MIME type of the file
            String mimeType = request.getServletContext().getMimeType(file.getPath());
            if (mimeType == null) {
                // set to binary type if MIME mapping not found
                mimeType = "application/octet-stream";
            }
            System.out.println("MIME type: " + mimeType);
            // set content attributes for the response
            response.setContentType(mimeType);
            response.setContentLength((int) file.length());
            // set headers for the response
            String headerKey = "Content-Disposition";
            String headerValue = String.format("attachment; filename=\"%s\"",
                    file.getName());
            response.setHeader(headerKey, headerValue);
            // get output stream of the response
            outStream = response.getOutputStream();
            byte[] buffer = new byte[1000];
            int bytesRead = -1;
            // write bytes read from the input stream into the output stream
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, bytesRead);
            }
            outStream.close();
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(SyncCenterInitController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
