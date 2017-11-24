/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.center.sync.restclient;

import com.argusoft.hkg.common.constantutil.HkSystemConstantUtil;
import com.argusoft.hkg.model.HkAssetEntity;
import com.argusoft.hkg.model.HkCaratRangeEntity;
import com.argusoft.hkg.model.HkCategoryEntity;
import com.argusoft.hkg.model.HkFeatureSectionEntity;
import com.argusoft.hkg.model.HkFieldEntity;
import com.argusoft.hkg.model.HkGoalTemplateEntity;
import com.argusoft.hkg.model.HkHolidayEntity;
import com.argusoft.hkg.model.HkLeaveEntity;
import com.argusoft.hkg.model.HkMasterEntity;
import com.argusoft.hkg.model.HkMessageEntity;
import com.argusoft.hkg.model.HkMessageRecipientDtlEntity;
import com.argusoft.hkg.model.HkNotificationRecipientEntity;
import com.argusoft.hkg.model.HkPriceListEntity;
import com.argusoft.hkg.model.HkReferenceRateEntity;
import com.argusoft.hkg.model.HkSectionEntity;
import com.argusoft.hkg.model.HkSubFormFieldEntity;
import com.argusoft.hkg.model.HkSystemConfigurationEntity;
import com.argusoft.hkg.model.HkValueEntity;
import com.argusoft.hkg.nosql.model.HkCalcFourCDiscountDocument;
import com.argusoft.hkg.nosql.model.HkCalcMasterDocument;
import com.argusoft.hkg.nosql.model.HkCalcRateDetailDocument;
import com.argusoft.hkg.nosql.model.HkCriteriaSetDocument;
import com.argusoft.hkg.nosql.model.HkDepartmentConfigDocument;
import com.argusoft.hkg.nosql.model.HkFeatureFieldPermissionDocument;
import com.argusoft.hkg.nosql.model.HkInvoiceDocument;
import com.argusoft.hkg.nosql.model.HkIssueDocument;
import com.argusoft.hkg.nosql.model.HkLotDocument;
import com.argusoft.hkg.nosql.model.HkPacketDocument;
import com.argusoft.hkg.nosql.model.HkParcelDocument;
import com.argusoft.hkg.nosql.model.HkPersonCapabilityDocument;
import com.argusoft.hkg.nosql.model.HkPlanAccessPermissionDocument;
import com.argusoft.hkg.nosql.model.HkPlanDocument;
import com.argusoft.hkg.nosql.model.HkPurchaseDocument;
import com.argusoft.hkg.nosql.model.HkRoleFeatureModifierDocument;
import com.argusoft.hkg.nosql.model.HkRuleSetDocument;
import com.argusoft.hkg.nosql.model.HkSellDocument;
import com.argusoft.hkg.nosql.model.HkSubFormFieldDocument;
import com.argusoft.hkg.nosql.model.HkSubFormValueDocument;
import com.argusoft.hkg.nosql.model.HkSubLotDocument;
import com.argusoft.hkg.nosql.model.HkTransferDocument;
import com.argusoft.hkg.nosql.model.HkUserGoalStatusDocument;
import com.argusoft.hkg.sync.center.core.HkUMSyncService;
import com.argusoft.hkg.sync.center.core.SyncCenterRoleFeatureService;
import com.argusoft.hkg.sync.xmpp.databeans.SyncXmppUserDatabean;
import com.argusoft.hkg.sync.xmpp.util.CountDatabean;
import com.argusoft.hkg.sync.xmpp.util.SyncEntityDocumentMapper;
import com.argusoft.hkg.sync.xmpp.util.SyncTransformerInterface;
import com.argusoft.hkg.sync.xmppclient.SyncXmppClient;
import com.argusoft.hkg.web.base.ResponseCode;
import com.argusoft.hkg.web.base.ResponseEntity;
import com.argusoft.hkg.web.center.security.WebSecurityNoSqlUtil;
import com.argusoft.hkg.web.center.sync.transformers.HkGenericDocumentTransformer;
import com.argusoft.hkg.web.center.sync.transformers.SyncCenterFranchiseTransformer;
import com.argusoft.hkg.web.center.sync.transformers.SyncCenterUserTransformer;
import com.argusoft.hkg.web.center.sync.transformers.SyncCenterXmppUserTransformer;
import com.argusoft.hkg.web.center.usermanagement.databeans.CenterFranchiseDataBean;
import com.argusoft.hkg.web.center.usermanagement.databeans.LoginDataBean;
import com.argusoft.hkg.web.center.util.ApplicationUtil;
import com.argusoft.hkg.web.center.util.CenterApplicationInitializer;
import com.argusoft.hkg.web.config.WebApplicationInitializerConfig;
import com.argusoft.sync.center.model.CenterCompanyFeatureSectionDocument;
import com.argusoft.sync.center.model.HkAssetDocument;
import com.argusoft.sync.center.model.HkCaratRangeDocument;
import com.argusoft.sync.center.model.HkCategoryDocument;
import com.argusoft.sync.center.model.HkDepartmentDocument;
import com.argusoft.sync.center.model.HkFeatureSectionDocument;
import com.argusoft.sync.center.model.HkFieldDocument;
import com.argusoft.sync.center.model.HkGoalTemplateDocument;
import com.argusoft.sync.center.model.HkHolidayDocument;
import com.argusoft.sync.center.model.HkLeaveDocument;
import com.argusoft.sync.center.model.HkMasterDocument;
import com.argusoft.sync.center.model.HkMasterValueDocument;
import com.argusoft.sync.center.model.HkMessageDocument;
import com.argusoft.sync.center.model.HkMessageRecipientDtlDocument;
import com.argusoft.sync.center.model.HkNotificationRecipientDocument;
import com.argusoft.sync.center.model.HkPriceListDocument;
import com.argusoft.sync.center.model.HkReferenceRateDocument;
import com.argusoft.sync.center.model.HkSectionDocument;
import com.argusoft.sync.center.model.HkSystemConfigurationDocument;
import com.argusoft.sync.center.model.InitialSetupInfo;
import com.argusoft.sync.center.model.SyncCenterFeatureDocument;
import com.argusoft.sync.center.model.SyncCenterRoleFeatureDocument;
import com.argusoft.sync.center.model.SyncCenterUserDocument;
import com.argusoft.sync.center.model.SyncCenterXmppUserDocument;
import com.argusoft.sync.center.model.UmCompanyDocument;
import com.argusoft.sync.center.model.UmDesignationDocument;
import com.argusoft.sync.center.model.VcardDocument;
import com.argusoft.usermanagement.common.model.UMCompany;
import com.argusoft.usermanagement.common.model.UMDepartment;
import com.argusoft.usermanagement.common.model.UMFeature;
import com.argusoft.usermanagement.common.model.UMRole;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URI;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.util.CollectionUtils;
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
@RequestMapping("/sync")
public class SyncRestClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(SyncRestClient.class);
    @Autowired
    private SyncCenterFranchiseTransformer centerFranchiseTransformer;
    @Autowired
    private HkGenericDocumentTransformer genericDocumentTransformer;
    @Autowired
    private SyncXmppUserDatabean hkXmppUserDatabean;
    @Autowired
    private SyncCenterUserTransformer centerUserTransformer;
    @Autowired
    private SyncXmppClient xmppClient;
    @Autowired
    private SyncCenterRoleFeatureService roleFeatureService;
    @Autowired
    private WebSecurityNoSqlUtil webSecurityNoSqlUtil;
    @Autowired
    private SyncEntityDocumentMapper entityDocumentMapper;
    @Autowired
    private SyncCenterXmppUserTransformer hkXmppUserDocumentTransformer;
    @Autowired
    private HkUMSyncService hkUMSyncService;
    @Autowired
    private ApplicationUtil applicationUtil;
    @Autowired
    private CenterApplicationInitializer applicationInitializer;
    private static boolean isFirstRequest = true;
    private final Gson gson;
    public final static String CONTENT_TYPE = "application/json;charset=UTF-8";
    private final static String MASTER_URL = WebApplicationInitializerConfig.MASTER_IP;
    private int statusCode = 200;
    private Type typeOfMap = new TypeToken<Map<String, String>>() {
    }.getType();
    private Type typeOfList = new TypeToken<List<String>>() {
    }.getType();
    public final static String OFFSET = "offset";
    public final static String LIMIT = "limit";
    public final static String COMPANY_ID = "comapnyId";
    public final static String IS_ARCHIVE_FIELD_NAME = "isArchiveFieldName";
    public final static String IS_ACTIVE_FIELD_NAME = "isActiveFieldName";
    public final static String CLASS_NAME = "className";
    public final static String IS_MONGO_DOC = "isMongoDocument";
    private static final List<DocumentRequest> DOCUMENT_REQUEST = new LinkedList<>();
    private Long companyId = 0l;
    private int status = -1;
    private Set<Integer> incompleteSetups = new HashSet<>();
    private boolean hasLocaleFilesTransfered = false;
    private InitialSetupInfo initialSetupInfo;

    static {

        DocumentRequest documentRequest = new DocumentRequest(HkCaratRangeEntity.class, HkCaratRangeDocument.class, "franchise", null, "false");
        DOCUMENT_REQUEST.add(documentRequest);

//        documentRequest = new DocumentRequest(HkCaratRangeEntity.class, HkCaratRangeDocument.class, "franchise", null, "false");
//        DOCUMENT_REQUEST.add(documentRequest);
        documentRequest = new DocumentRequest(UMDepartment.class, HkDepartmentDocument.class, "company", "isArchive", "false");
        DOCUMENT_REQUEST.add(documentRequest);

        documentRequest = new DocumentRequest(UMCompany.class, UmCompanyDocument.class, null, "isArchive", "false");
        DOCUMENT_REQUEST.add(documentRequest);

        documentRequest = new DocumentRequest(UMRole.class, UmDesignationDocument.class, "company", "isArchive", "false");
        DOCUMENT_REQUEST.add(documentRequest);

        documentRequest = new DocumentRequest(HkNotificationRecipientEntity.class, HkNotificationRecipientDocument.class, "hkNotificationEntity.franchise", "isArchive", "false");
        DOCUMENT_REQUEST.add(documentRequest);

        documentRequest = new DocumentRequest(HkLeaveEntity.class, HkLeaveDocument.class, "franchise", "isArchive", "false");
        DOCUMENT_REQUEST.add(documentRequest);

        documentRequest = new DocumentRequest(HkHolidayEntity.class, HkHolidayDocument.class, "franchise", "isArchive", "false");
        DOCUMENT_REQUEST.add(documentRequest);

        documentRequest = new DocumentRequest(HkSystemConfigurationEntity.class, HkSystemConfigurationDocument.class, "hkSystemConfigurationEntityPK.franchise", "isArchive", "false");
        DOCUMENT_REQUEST.add(documentRequest);

        documentRequest = new DocumentRequest(HkValueEntity.class, HkMasterValueDocument.class, null, "isArchive", "false");
        DOCUMENT_REQUEST.add(documentRequest);

//        documentRequest = new DocumentRequest(HkPriceListDetailEntity.class, HkPriceListDetailDocument.class, "hkPriceListEntity.franchise", "isArchive", "false");
//        DOCUMENT_REQUEST.add(documentRequest);
        documentRequest = new DocumentRequest(HkPriceListEntity.class, HkPriceListDocument.class, "franchise", null, "false");
        DOCUMENT_REQUEST.add(documentRequest);

        documentRequest = new DocumentRequest(HkFieldEntity.class, HkFieldDocument.class, "franchise", "isArchive", "false");
        DOCUMENT_REQUEST.add(documentRequest);

        documentRequest = new DocumentRequest(HkRuleSetDocument.class, HkRuleSetDocument.class, "franchise", "isArchive", "true");
        DOCUMENT_REQUEST.add(documentRequest);

        documentRequest = new DocumentRequest(VcardDocument.class, VcardDocument.class, null, null, "true");
        DOCUMENT_REQUEST.add(documentRequest);

        documentRequest = new DocumentRequest(HkSubFormValueDocument.class, HkSubFormValueDocument.class, "franchiseId", "isArchive", "true");
        DOCUMENT_REQUEST.add(documentRequest);

        documentRequest = new DocumentRequest(HkFeatureFieldPermissionDocument.class, HkFeatureFieldPermissionDocument.class, "franchise", "isArchive", "true");
        DOCUMENT_REQUEST.add(documentRequest);

        documentRequest = new DocumentRequest(HkPurchaseDocument.class, HkPurchaseDocument.class, null, "isArchive", "true");
        DOCUMENT_REQUEST.add(documentRequest);

        documentRequest = new DocumentRequest(HkSubFormFieldEntity.class, HkSubFormFieldDocument.class, null, "isArchive", "false");
        DOCUMENT_REQUEST.add(documentRequest);

        documentRequest = new DocumentRequest(HkReferenceRateEntity.class, HkReferenceRateDocument.class, null, "isArchive", "false");
        DOCUMENT_REQUEST.add(documentRequest);

        documentRequest = new DocumentRequest(HkMessageEntity.class, HkMessageDocument.class, "franchise", "isArchive", "false");
        DOCUMENT_REQUEST.add(documentRequest);

        documentRequest = new DocumentRequest(HkRoleFeatureModifierDocument.class, HkRoleFeatureModifierDocument.class, "franchise", "isArchive", "true");
        DOCUMENT_REQUEST.add(documentRequest);

        documentRequest = new DocumentRequest(HkMessageRecipientDtlEntity.class, HkMessageRecipientDtlDocument.class, "hkMessage.franchise", "isArchive", "false");
        DOCUMENT_REQUEST.add(documentRequest);

//        documentRequest = new DocumentRequest(SyncFileTransferDocument.class, SyncFileTransferDocument.class, "company", null, "true");
//        DOCUMENT_REQUEST.add(documentRequest);
        documentRequest = new DocumentRequest(HkCriteriaSetDocument.class, HkCriteriaSetDocument.class, "franchiseId", "isArchive", "true");
        DOCUMENT_REQUEST.add(documentRequest);

        documentRequest = new DocumentRequest(HkPersonCapabilityDocument.class, HkPersonCapabilityDocument.class, "franchise", null, "true");
        DOCUMENT_REQUEST.add(documentRequest);

        documentRequest = new DocumentRequest(HkUserGoalStatusDocument.class, HkUserGoalStatusDocument.class, "franchiseId", null, "true");
        DOCUMENT_REQUEST.add(documentRequest);

        documentRequest = new DocumentRequest(HkPlanAccessPermissionDocument.class, HkPlanAccessPermissionDocument.class, "franchiseId", null, "true");
        DOCUMENT_REQUEST.add(documentRequest);

        documentRequest = new DocumentRequest(HkCategoryEntity.class, HkCategoryDocument.class, "franchise", "isArchive", "false");
        DOCUMENT_REQUEST.add(documentRequest);

        documentRequest = new DocumentRequest(HkAssetEntity.class, HkAssetDocument.class, "franchise", "isArchive", "false");
        DOCUMENT_REQUEST.add(documentRequest);

        documentRequest = new DocumentRequest(HkMasterEntity.class, HkMasterDocument.class, null, "isArchive", "false");
        DOCUMENT_REQUEST.add(documentRequest);

        documentRequest = new DocumentRequest(HkGoalTemplateEntity.class, HkGoalTemplateDocument.class, "franchise", null, "false");
        DOCUMENT_REQUEST.add(documentRequest);

        documentRequest = new DocumentRequest(HkDepartmentConfigDocument.class, HkDepartmentConfigDocument.class, "franchise", "isArchive", "true");
        DOCUMENT_REQUEST.add(documentRequest);

        documentRequest = new DocumentRequest(HkInvoiceDocument.class, HkInvoiceDocument.class, "franchiseId", "isArchive", "true");
        DOCUMENT_REQUEST.add(documentRequest);

        documentRequest = new DocumentRequest(HkSubLotDocument.class, HkSubLotDocument.class, "franchiseId", "isArchive", "true");
        DOCUMENT_REQUEST.add(documentRequest);

        documentRequest = new DocumentRequest(HkLotDocument.class, HkLotDocument.class, "franchiseId", "isArchive", "true");
        DOCUMENT_REQUEST.add(documentRequest);

        documentRequest = new DocumentRequest(HkPacketDocument.class, HkPacketDocument.class, "franchiseId", "isArchive", "true");
        DOCUMENT_REQUEST.add(documentRequest);

        documentRequest = new DocumentRequest(HkParcelDocument.class, HkParcelDocument.class, "franchiseId", "isArchive", "true");
        DOCUMENT_REQUEST.add(documentRequest);

        documentRequest = new DocumentRequest(HkPlanDocument.class, HkPlanDocument.class, "franchiseId", "isArchive", "true");
        DOCUMENT_REQUEST.add(documentRequest);

        documentRequest = new DocumentRequest(HkSellDocument.class, HkSellDocument.class, "franchiseId", "isArchive", "true");
        DOCUMENT_REQUEST.add(documentRequest);

        documentRequest = new DocumentRequest(HkTransferDocument.class, HkTransferDocument.class, "franchiseId", "isArchive", "true");
        DOCUMENT_REQUEST.add(documentRequest);

        documentRequest = new DocumentRequest(HkIssueDocument.class, HkIssueDocument.class, "franchiseId", "isArchive", "true");
        DOCUMENT_REQUEST.add(documentRequest);

    }

    public SyncRestClient() {
        final JsonSerializer<Date> dateSerializer = (Date src, Type typeOfSrc, JsonSerializationContext context) -> src == null ? null : new JsonPrimitive(src.getTime());
        final JsonDeserializer<Date> dateDeserializer = (JsonElement json, Type typeOfT, JsonDeserializationContext context) -> {
            if (json != null) {
                try {
                    return new Date(json.getAsLong());
                } catch (NumberFormatException numberFormatException) {
                    return new Date(json.getAsString());
                }
            } else {
                return null;
            }
        };

        gson = new GsonBuilder().registerTypeAdapter(Date.class, dateSerializer)
                .registerTypeAdapter(Date.class, dateDeserializer).create();

    }

    @PostConstruct
    public void init() {
//        initialSetupInfo = (InitialSetupInfo) hkUMSyncService.getDocumentById(1l, InitialSetupInfo.class);
//       LOGGER.debug("Load initial setup: ");
//        if (initialSetupInfo != null) {
//           LOGGER.debug("Setting initial setup");
//            status = initialSetupInfo.getStatus();
//            incompleteSetups = initialSetupInfo.getIncompleteSetups();
//            hasLocaleFilesTransfered = initialSetupInfo.isHasLocaleFilesTransfered();
//        } else {
//            initialSetupInfo = new InitialSetupInfo();
//            initialSetupInfo.setId(1l);
//            initialSetupInfo.setHasLocaleFilesTransfered(false);
//
//        }
    }

    @RequestMapping(value = "/deployserver", method = RequestMethod.POST)
    @SuppressWarnings("unchecked")
    public ResponseEntity<Object> getDataFromMaster(@RequestBody Map<String, String> paramMap, HttpServletResponse httpServletResponse) throws UnsupportedEncodingException, IOException {
        String j_username, j_password;
        LOGGER.debug("param map: " + paramMap + " master url:" + MASTER_URL);
        j_password = paramMap.get("password");
        j_username = paramMap.get("userName");
        statusCode = 400;
        LOGGER.debug("First request" + isFirstRequest);
//        if (isFirstRequest) {
        status = -1;
        ResponseEntity<String> errorEntity = null;
        initialSetupInfo = (InitialSetupInfo) hkUMSyncService.getDocumentById(1l, InitialSetupInfo.class);
        LOGGER.debug("Load initial setup: ");
        if (initialSetupInfo != null) {
            LOGGER.debug("Setting initial setup");
            status = initialSetupInfo.getStatus();
            incompleteSetups = initialSetupInfo.getIncompleteSetups();
            hasLocaleFilesTransfered = initialSetupInfo.isHasLocaleFilesTransfered();
        } else {

            initialSetupInfo = new InitialSetupInfo();
            initialSetupInfo.setId(1l);
            initialSetupInfo.setHasLocaleFilesTransfered(false);
        }
        LOGGER.debug("Status: " + status);
        if (status != 100) {
            Object authResponse;
            try {

                Map<String, String> nameValuePairs = new HashMap<>(2);
                nameValuePairs.put("userName", j_username);
                nameValuePairs.put("password", j_password);

                String credentialString = gson.toJson(nameValuePairs, typeOfMap);
                LOGGER.debug("before Authenticate request");
                authResponse = executeRequest(new HttpPost(), MASTER_URL + "api/common/authenticate", null, credentialString, AuthenticationToken.class);
            } catch (HttpHostConnectException e) {
                LOGGER.debug("Master Refused connection");
                httpServletResponse.setStatus(404);
                return new ResponseEntity(null, ResponseCode.FAILURE, "Master server Refused connection.", null, true);
            } catch (Exception e) {
                return new ResponseEntity(null, ResponseCode.FAILURE, e.getMessage(), null, true);
            }
            try {
                LOGGER.debug("authResponse " + authResponse);
                if (authResponse instanceof ResponseEntity) {
                    initialSetupInfo.setStatus(status);
                    hkUMSyncService.saveOrUpdateDocument(initialSetupInfo);
                    LOGGER.debug("  if (authResponse instanceof ResponseEntity) {");
                    httpServletResponse.setStatus(statusCode);
                    return (ResponseEntity<Object>) authResponse;
                } else if (authResponse instanceof AuthenticationToken) {
                    LOGGER.debug("else if (authResponse instanceof AuthenticationToken)");
                    if (status < 1) {
                        status = 0;
                        String authToken = ((AuthenticationToken) authResponse).getToken();
                        Object object;
                        object = getInitData1(j_username, authToken);
                        if (object instanceof ResponseEntity) {
                            initialSetupInfo.setStatus(status);
                            hkUMSyncService.saveOrUpdateDocument(initialSetupInfo);
                            httpServletResponse.setStatus(statusCode);
                            return (ResponseEntity<Object>) object;
                        }

                        Map<String, Long> companyMap = new HashMap<>(1);
                        LOGGER.debug("companyId====" + companyId);
                        companyMap.put("franchiseId", companyId);
                        Type companyMapType = new TypeToken<Map<String, Long>>() {
                        }.getType();
                        String companyString = gson.toJson(companyMap, companyMapType);

                        object = getInitData2(authToken, companyString);
                        if (object instanceof ResponseEntity) {
                            incompleteSetups.add(status);
                            errorEntity = (ResponseEntity<String>) object;
                        }
                        object = getInitData3(authToken);
                        if (object instanceof ResponseEntity) {
                            incompleteSetups.add(status);
                            errorEntity = (ResponseEntity<String>) object;
                        }
                        object = getInitData4(authToken, companyString);
                        if (object instanceof ResponseEntity) {
                            incompleteSetups.add(status);
                            errorEntity = (ResponseEntity<String>) object;
                        }
                        status = 4;
                        object = getInitData5(authToken, companyString);
                        if (object instanceof ResponseEntity) {
                            incompleteSetups.add(status);
                            errorEntity = (ResponseEntity<String>) object;
                        }
                        status = 5;

                        getLocaleFiles(authToken);
                        for (int i = 0; i < DOCUMENT_REQUEST.size(); i++) {
                            DocumentRequest documentRequest = DOCUMENT_REQUEST.get(i);
                            LOGGER.debug(" === Object 1  : " + documentRequest);
                            object = executeRequest(new HttpPost(), MASTER_URL + "api/init/retrieve/documents", authToken, documentRequest.getEntityClass(), documentRequest.getDocumentClass(), companyId, documentRequest.getCompanyFieldName(), documentRequest.getIsArchiveFieldName(), documentRequest.getIsMongoDocument());
                            if (object != null) {
                                incompleteSetups.add(status);
                                errorEntity = (ResponseEntity<String>) object;
                            }
                            status++;
                        }

                        //Always keep this at the end
                        Object rapCalcData = getRapCalcData(authToken);
                        if (rapCalcData instanceof ResponseEntity) {
                            incompleteSetups.add(99);
                            errorEntity = (ResponseEntity<String>) rapCalcData;
                        }
                        status = 99;
//                        getAvtar();
                    } else {
                        String authToken = ((AuthenticationToken) authResponse).getToken();
                        Object object = null;
                        CenterFranchiseDataBean centerFranchiseDataBean = centerFranchiseTransformer.retrieveById(1l);
                        applicationUtil.setCenterFranchiseDataBean(centerFranchiseDataBean);
                        companyId = centerFranchiseDataBean.getFranchiseId();
                        Map<String, Long> companyMap = new HashMap<>(1);
                        companyMap.put("franchiseId", companyId);
                        Type companyMapType = new TypeToken<Map<String, Long>>() {
                        }.getType();
                        String companyString = gson.toJson(companyMap, companyMapType);
                        Set<Integer> tmpList = new HashSet<>();
                        if (!hasLocaleFilesTransfered) {
                            getLocaleFiles(authToken);
                        }
                        for (Integer stageId : incompleteSetups) {
                            LOGGER.debug("INCOMPLETE status: " + stageId);
                            if (stageId == 1) {
                                object = getInitData2(authToken, companyString);
                                LOGGER.debug("" + object);
                                if (object instanceof ResponseEntity) {
                                    tmpList.add(stageId);
                                    errorEntity = (ResponseEntity<String>) object;
                                }
                            } else if (stageId == 2) {
                                object = getInitData3(authToken);
                                LOGGER.debug("" + object);
                                if (object instanceof ResponseEntity) {
                                    tmpList.add(stageId);
                                    errorEntity = (ResponseEntity<String>) object;
                                }
                            } else if (stageId == 3) {
                                object = getInitData4(authToken, companyString);
                                LOGGER.debug("" + object);
                                if (object instanceof ResponseEntity) {
                                    tmpList.add(stageId);
                                    errorEntity = (ResponseEntity<String>) object;
                                }
                                status = 4;
                            } else if (stageId == 4) {
                                object = getInitData5(authToken, companyString);
                                LOGGER.debug("" + object);
                                if (object instanceof ResponseEntity) {
                                    tmpList.add(stageId);
                                    errorEntity = (ResponseEntity<String>) object;
                                }
                                status = 4;
                            } else if (stageId == 99) {
                                Object rapCalcData = getRapCalcData(authToken);
                                if (rapCalcData instanceof ResponseEntity) {
                                    tmpList.add(99);
                                    errorEntity = (ResponseEntity<String>) rapCalcData;
                                }
                                status = 99;

                            } else if (stageId - 5 > (DOCUMENT_REQUEST.size() - 9)) {
                                for (int i = DOCUMENT_REQUEST.size() - 9; i < DOCUMENT_REQUEST.size(); i++) {
                                    DocumentRequest documentRequest = DOCUMENT_REQUEST.get(i);
                                    object = executeRequest(new HttpPost(), MASTER_URL + "api/init/retrieve/documents", authToken, documentRequest.getEntityClass(), documentRequest.getDocumentClass(), companyId, documentRequest.getCompanyFieldName(), documentRequest.getIsArchiveFieldName(), documentRequest.getIsMongoDocument());
                                    if (object != null) {
                                        tmpList.add(status);
                                        errorEntity = (ResponseEntity<String>) object;
                                    }
                                    status++;
                                }
                                break;
                            } else {
                                DocumentRequest documentRequest = DOCUMENT_REQUEST.get(stageId - 5);
                                object = executeRequest(new HttpPost(), MASTER_URL + "api/init/retrieve/documents", authToken, documentRequest.getEntityClass(), documentRequest.getDocumentClass(), companyId, documentRequest.getCompanyFieldName(), documentRequest.getIsArchiveFieldName(), documentRequest.getIsMongoDocument());
                                LOGGER.debug("" + object);
                                if (object != null) {
                                    tmpList.add(stageId);
                                    errorEntity = (ResponseEntity<String>) object;
                                }
                            }
                        }

//                        getAvtar();
                        incompleteSetups = tmpList;
                    }

                    if (statusCode == 200 && errorEntity == null) {
                        status = 100;
                    }

//                    status = -100;
                    isFirstRequest = false;
                    initialSetupInfo.setStatus(status);
                    initialSetupInfo.setHasLocaleFilesTransfered(hasLocaleFilesTransfered);
                    initialSetupInfo.setIncompleteSetups(incompleteSetups);
                    hkUMSyncService.saveOrUpdateDocument(initialSetupInfo);
                    webSecurityNoSqlUtil.init();
                    applicationInitializer.initCenterApplicationUtil();

                }
            } catch (HttpHostConnectException e) {
                LOGGER.debug("Master Refused connection");
                statusCode = 404;
            } catch (Exception e) {
                e.printStackTrace(System.out);
                statusCode = 500;
            }
        } else {
            statusCode = 200;
            isFirstRequest = false;
        }
//        } else {
//            statusCode = 200;
//        }
        return new ResponseEntity(initialSetupInfo, ResponseCode.SUCCESS, null, null, false);
    }

    private Object getInitData1(String j_username, String authToken) {

        try {
            Object object = executeRequest(new HttpPost(), MASTER_URL + "api/common/getsession", authToken, "-330", LoginDataBean.class
            );
            if (object instanceof ResponseEntity) {
                return object;

            }
            LoginDataBean centerLoginDataBean = (LoginDataBean) object;

            status = 1;
            if (centerLoginDataBean != null && (centerLoginDataBean.getIsFranchiseAdmin()
                    || centerLoginDataBean.getIsHKAdmin())) {
                companyId = centerLoginDataBean.getCompanyId();
                addFranchiseData(companyId, j_username);
            } else {
                statusCode = 401;
                return new ResponseEntity(null, ResponseCode.FAILURE, HkSystemConstantUtil.WebSecurityConstant.UNAUTHORIZED_ACCESS_MSG, null, true);
            }
            return centerLoginDataBean;
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(SyncRestClient.class.getName()).log(Level.SEVERE, null, ex);
            statusCode = 500;
            return new ResponseEntity(ex, ResponseCode.ERROR, null, null, false);
        }
    }

    private Object getInitData2(String authToken, String companyString) {
        try {
            Object object = executeRequest(new HttpPost(), MASTER_URL + "api/init/retrieve/xmppCredentials", authToken, companyString, SyncCenterXmppUserDocument.class);
            LOGGER.debug("object restclient=" + object);
            if (object instanceof ResponseEntity) {
                return object;
            }
            SyncCenterXmppUserDocument hkXmppUserDocument = (SyncCenterXmppUserDocument) object;
            hkXmppUserDocumentTransformer.saveHkXmppUser(hkXmppUserDocumentTransformer.convertHkXmppUserDocumentToHkXmppUserDatabean(hkXmppUserDocument, hkXmppUserDatabean));
            status = 2;
            VcardDocument vcardDocument = new VcardDocument();
            vcardDocument.setId("0");
            vcardDocument.setJid("master@" + WebApplicationInitializerConfig.XMPP_DOMAIN);
            Map<String, String> map = new HashMap<>();
            map.put("FranchiseId", "0");
            vcardDocument.setFields(map);
            hkUMSyncService.saveOrUpdateDocument(vcardDocument);
            ExecutorService executorService1 = Executors.newCachedThreadPool();
            executorService1.execute(() -> {
                xmppClient.connect();
            });
            return hkXmppUserDocument;
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(SyncRestClient.class.getName()).log(Level.SEVERE, null, ex);
            statusCode = 500;
            return new ResponseEntity(ex, ResponseCode.ERROR, null, null, false);
        }
    }

    private Object getInitData3(String authToken) {
        try {
            Object object = executeRequest(new HttpGet(), MASTER_URL + "api/init/retrieve/rolefeature", authToken, null, SyncCenterRoleFeatureDocument.class);
            if (object instanceof ResponseEntity) {
                return object;
            }
            SyncCenterRoleFeatureDocument roleFeatureDocument = (SyncCenterRoleFeatureDocument) object;
            roleFeatureDocument.setId(1l);
            roleFeatureService.saveOrUpdate(roleFeatureDocument);

            object = executeRequest(new HttpPost(), MASTER_URL + "api/init/retrieve/documents", authToken, UMFeature.class, SyncCenterFeatureDocument.class, 0l, "company", "isArchive", "false");
            if (object instanceof ResponseEntity) {
                return object;
            }

            object = executeRequest(new HttpPost(), MASTER_URL + "api/init/retrieve/documents", authToken, HkSectionEntity.class, HkSectionDocument.class, null, null, "isArchive", "false");
            if (object instanceof ResponseEntity) {
                return object;
            }

            object = executeRequest(new HttpPost(), MASTER_URL + "api/init/retrieve/documents", authToken, HkFeatureSectionEntity.class, HkFeatureSectionDocument.class, null, null, "isArchive", "false");
            if (object instanceof ResponseEntity) {
                return object;
            }

            status = 3;
            return roleFeatureDocument;
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(SyncRestClient.class.getName()).log(Level.SEVERE, null, ex);
            statusCode = 500;
            return new ResponseEntity(ex, ResponseCode.ERROR, null, null, false);
        } catch (Exception e) {
            java.util.logging.Logger.getLogger(SyncRestClient.class.getName()).log(Level.SEVERE, null, e);
            statusCode = 500;
            return new ResponseEntity(e, ResponseCode.ERROR, null, null, false);
        }
    }

    private Object getInitData4(String authToken, String companyString) {
        try {
            Object object = executeRequest(new HttpPost(), MASTER_URL + "api/init/retrieve/users", authToken, companyString, typeOfList);
            if (object instanceof ResponseEntity) {
                return object;
            }
            List<String> users = (List<String>) object;
            for (String user : users) {
                SyncCenterUserDocument userDocument = gson.fromJson(user, SyncCenterUserDocument.class);
                centerUserTransformer.save(userDocument);
            }
            return object;
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(SyncRestClient.class.getName()).log(Level.SEVERE, null, ex);
            statusCode = 500;
            return new ResponseEntity(ex, ResponseCode.ERROR, null, null, false);
        }
    }

    private Object getInitData5(String authToken, String companyString) {
        try {
            Object object = executeRequest(new HttpPost(), MASTER_URL + "api/init/retrieve/featuresectionmap", authToken, companyString, CenterCompanyFeatureSectionDocument.class);
            if (object instanceof ResponseEntity) {
                return object;
            }
            CenterCompanyFeatureSectionDocument centerCompanyFeatureSectionDocument = (CenterCompanyFeatureSectionDocument) object;
            hkUMSyncService.saveOrUpdateDocument(centerCompanyFeatureSectionDocument);
            applicationUtil.setCenterFeatureFromTemplateMap(centerCompanyFeatureSectionDocument.getFeatureSectionMap());
            Map<String, String> systemConfigMap = applicationUtil.getSystemConfigrationMap();
            if (systemConfigMap == null) {
                systemConfigMap = new HashMap<>();
            }
            systemConfigMap.put(HkSystemConstantUtil.CUSTOM_FIELD_VERSION, centerCompanyFeatureSectionDocument.getCustomFieldVersion());
            applicationUtil.setSystemConfigrationMap(systemConfigMap);
            LOGGER.debug("CenterCompanyFeatureSectionDocument created.");
            return object;
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(SyncRestClient.class.getName()).log(Level.SEVERE, null, ex);
            statusCode = 500;
            return new ResponseEntity(ex, ResponseCode.ERROR, null, null, false);
        }
    }

    private void getAvtar() {
        HkSystemConfigurationDocument documentById = (HkSystemConfigurationDocument) hkUMSyncService.getDocumentById(HkSystemConstantUtil.FranchiseConfiguration.EMP_DEFAULT_IMAGE, HkSystemConfigurationDocument.class);
        System.out.println("documentbyid: " + documentById);
        if (documentById != null) {
            String keyValue = documentById.getKeyValue();
            System.out.println("keyvalue" + keyValue);
            if (!StringUtils.isEmpty(keyValue)) {
                String path = "/home/shruti/hkg/FRANCHISE/" + companyId + "/COMMON/" + keyValue;
//        String path = "/home/shruti/hkg/FRANCHISE/" + companyId + "/COMMON/COMMON~@2~@1430313198597*test1.png";
//                System.out.println("PATH:::: " + path);
                saveFile(MASTER_URL + "api/init/retrieve/avtar", companyId.toString(), null, path, MediaType.APPLICATION_OCTET_STREAM_VALUE);
//                System.out.println("File Saved");
            }
        }
    }

    private Object executeRequest(HttpRequestBase httpRequestBase, String uri, String authToken, Class<?> entityClass, Class<?> documentClass, Long companyId, String companyFieldName, String isArchiveFieldName, String isMongoDocument) {
        try {
//            LOGGER.debug("COMPANYID= " + companyId.toString());
            Map<String, String> PropertyNameMap = new HashMap<>();
            PropertyNameMap.put(COMPANY_ID, ((companyId != null) ? companyId.toString() : null));
            PropertyNameMap.put("companyFieldName", companyFieldName);
//            if (isArchiveFieldName != null) {
            PropertyNameMap.put(IS_ARCHIVE_FIELD_NAME, isArchiveFieldName);
//            }
            PropertyNameMap.put(CLASS_NAME, entityClass.getName());
            PropertyNameMap.put(IS_MONGO_DOC, isMongoDocument);

            Object object = executeRequest(httpRequestBase, uri, authToken, gson.toJson(PropertyNameMap), typeOfList);
            if (object instanceof ResponseEntity) {
                return (ResponseEntity<String>) object;
            }
            List<String> documentList = (List<String>) object;
            statusCode = convertAndSaveDocument(documentList, documentClass);
            if (statusCode != 200) {
                return new ResponseEntity(new Exception(), ResponseCode.ERROR, null, null, false);
            }

        } catch (IOException ex) {
            statusCode = 500;
            ex.printStackTrace();
            return new ResponseEntity(ex, ResponseCode.ERROR, null, null, false);
        }
        return null;
    }

    private HttpPost createPostRequest(String uri, Header[] headers, String jsessionid, String payload) {
        HttpPost httpPost = new HttpPost(uri);
        if (jsessionid != null) {
            httpPost.setHeader("Cookie", jsessionid);
        }
        httpPost.addHeader("Content-Type", CONTENT_TYPE);
        if (payload != null) {
            BasicHttpEntity basicHttpEntity = new BasicHttpEntity();
            basicHttpEntity.setContent(new ByteArrayInputStream(payload.getBytes()));
            httpPost.setEntity(basicHttpEntity);
        }
        if (headers != null) {
            httpPost.setHeaders(headers);
        }
        return httpPost;
    }

    private HttpGet createGetRequest(String uri, Header[] headers, String jsessionid) {
        HttpGet httpGet = new HttpGet(uri);
        if (jsessionid != null) {
            httpGet.setHeader("Cookie", jsessionid);
        }
        httpGet.addHeader("Content-Type", CONTENT_TYPE);

        if (headers != null) {
            httpGet.setHeaders(headers);
        }
        return httpGet;
    }

    private void addFranchiseData(long franchiseId, String franchiseAdminId) {
        CenterFranchiseDataBean dataBean = new CenterFranchiseDataBean();
        dataBean.setFranchiseAdminId(franchiseAdminId);
        dataBean.setFranchiseId(franchiseId);
        dataBean.setId(1);
        applicationUtil.setCenterFranchiseDataBean(dataBean);
        centerFranchiseTransformer.addFranchise(dataBean);
    }

    private Object readData(HttpResponse httpResponse, Class<?> class1) throws IOException {
        HttpEntity entity = httpResponse.getEntity();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(entity.getContent()));
        String jsonString = bufferedReader.readLine();
//        ObjectMapper objectMapper = new ObjectMapper();
//        Object readValue = objectMapper.readValue(jsonString, class1);
        LOGGER.debug("fromjson=== " + jsonString + ", class1=" + class1.getSimpleName());
//        Object fromJson = gson.fromJson(jsonString, type);
        return gson.fromJson(jsonString, class1);
//        return readValue;
    }

    private Object readData(HttpResponse httpResponse, Type type) throws IOException {
        HttpEntity entity = httpResponse.getEntity();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(entity.getContent()));
        String jsonString = bufferedReader.readLine();
        LOGGER.debug("fromjson=== " + jsonString);

        Object fromJson = gson.fromJson(jsonString, type);

        return fromJson;
    }

    private Object executeRequest(HttpRequestBase httpRequestBase, String uri, String authToken, String payload, Class<?> class1) throws IOException {
        LOGGER.debug("Request uri" + uri + "    authtoken " + authToken + "    payload " + payload);
        httpRequestBase.setURI(URI.create(uri));
        if (authToken != null) {
            httpRequestBase.setHeader("X-Auth-Token", authToken);
        }

        httpRequestBase.addHeader("Content-Type", CONTENT_TYPE);
        httpRequestBase.addHeader("Accept", CONTENT_TYPE + ", */*");

        if (payload != null) {

            BasicHttpEntity basicHttpEntity = new BasicHttpEntity();
            basicHttpEntity.setContent(new ByteArrayInputStream(payload.getBytes()));
            ((HttpEntityEnclosingRequest) httpRequestBase).setEntity(basicHttpEntity);
        }

        HttpClient httpClient = new DefaultHttpClient();
        HttpResponse httpResponse = httpClient.execute(httpRequestBase);
        statusCode = httpResponse.getStatusLine().getStatusCode();
        LOGGER.debug("url " + uri + " statuscode: " + statusCode + " " + httpResponse.getStatusLine().getReasonPhrase());
        if (statusCode == 200) {
            return readData(httpResponse, class1);
        } else if (statusCode == 401 && uri.contains("common/authenticate")) {
            HttpEntity entity = httpResponse.getEntity();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(entity.getContent()));
            String jsonString = bufferedReader.readLine();
            return new ResponseEntity(null, ResponseCode.ERROR, jsonString, null, true);
        } else {
            return new ResponseEntity(new Exception(), ResponseCode.ERROR, null, null, false);
        }

    }

    private Object executeRequest(HttpRequestBase httpRequestBase, String uri, String authToken, String payload, Type type) throws IOException {
        LOGGER.debug("Request uri" + uri + "    authtoken " + authToken + "    payload " + payload);
        httpRequestBase.setURI(URI.create(uri));
        if (authToken != null) {
            httpRequestBase.setHeader("X-Auth-Token", authToken);
        }
        httpRequestBase.addHeader("Content-Type", CONTENT_TYPE);
        if (payload != null) {
            BasicHttpEntity basicHttpEntity = new BasicHttpEntity();
            basicHttpEntity.setContent(new ByteArrayInputStream(payload.getBytes()));
            ((HttpPost) httpRequestBase).setEntity(basicHttpEntity);
        }

        HttpClient httpClient = new DefaultHttpClient();
        HttpResponse httpResponse = httpClient.execute(httpRequestBase);
        statusCode = httpResponse.getStatusLine().getStatusCode();
        LOGGER.debug("url " + uri + " statuscode: " + statusCode + " " + httpResponse.getStatusLine().getReasonPhrase());
        if (statusCode == 200) {
            return readData(httpResponse, type);
        } else {
            return new ResponseEntity(new Exception(), ResponseCode.ERROR, null, null, false);
        }

    }

    public int convertAndSaveDocument(List<String> documentStringList, Class<?> class1) {
        statusCode = 200;
        try {
            LOGGER.debug("class1.getGenericSuperclass().getClass() " + class1.getGenericSuperclass().getTypeName());
//            if (class1.getGenericSuperclass().getTypeName().equals(GenericDocument.class.getName())) {
//                for (String documentStringList1 : documentStringList) {
//                    genericDocumentTransformer.save(documentStringList1, class1);
//                }
//                return statusCode;
//            }
            SyncTransformerInterface documentTransformerClass = entityDocumentMapper.getDocumentTransformerInstance(class1);
            System.out.println(class1 + "  documentTransformerClass " + documentTransformerClass);
            if (documentTransformerClass != null) {

                if (documentStringList != null) {
                    try {
//                        if (documentTransformerClass instanceof HkGenericDocumentTransformer && (class1.getGenericSuperclass().getTypeName().equals(GenericDocument.class.getName()))) {
//                            for (String documentStringList1 : documentStringList) {
//                                genericDocumentTransformer.save(documentStringList1, class1);
//                            }
//                        } else {
                        for (String documentStringList1 : documentStringList) {
                            Object fromJson = gson.fromJson(documentStringList1, class1);
                            documentTransformerClass.save(fromJson);
                        }
//                        }
                        LOGGER.debug(class1.getName() + " inserted successfully.");
                    } catch (Exception e) {
                        LOGGER.info("Exception: ", e);
                        statusCode = 500;
                    }
                }
            } else {
                LOGGER.info("Exception: Could not transform class: " + class1.getName());
                statusCode = 500;
            }
        } catch (InstantiationException ex) {
            statusCode = 500;
            ex.printStackTrace();
        } catch (IllegalAccessException ex) {
            statusCode = 500;
            ex.printStackTrace();
        }
        return statusCode;
    }

    private void getLocaleFiles(String authToken) throws IOException {
        HttpGet httpGet = new HttpGet(MASTER_URL + "api/init/retrieve/localefilenames");
        if (authToken != null) {
            httpGet.setHeader("X-Auth-Token", authToken);
        }
        httpGet.addHeader("Accept", CONTENT_TYPE + ", */*");
        HttpClient httpClient = new DefaultHttpClient();
        HttpResponse httpResponse = httpClient.execute(httpGet);
        statusCode = httpResponse.getStatusLine().getStatusCode();
        LOGGER.debug("status= " + statusCode);
        if (statusCode == 200) {
            String filePath = WebApplicationInitializerConfig.projectDirectory + File.separator;
            HttpEntity entity = httpResponse.getEntity();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(entity.getContent()));
            String jsonString = bufferedReader.readLine();
            Type typeOfList = new TypeToken<List<String>>() {
            }.getType();
            List<String> filenames = gson.fromJson(jsonString, typeOfList);
            if (!CollectionUtils.isEmpty(filenames)) {
                for (String filename : filenames) {
//                    String[] split = filename.split("i18n" + File.separator);
                    String file = filename.substring(filename.lastIndexOf(File.separator) + 1);
                    saveFile(MASTER_URL + "api/init/retrieve/localefile", filename, authToken, filePath + file, MediaType.APPLICATION_OCTET_STREAM_VALUE);
                }
                hasLocaleFilesTransfered = true;
            }
        }
    }

    @Async
    public void saveFile(String uri, String sourceFilename, String authToken, String destinationFileName, String contentType) {
        try {
            HttpPost httpPost = new HttpPost(uri);
            if (authToken != null) {
                httpPost.setHeader("X-Auth-Token", authToken);
            }
            LOGGER.debug("Filename: " + sourceFilename);
            httpPost.addHeader("Content-Type", contentType);
            httpPost.addHeader("Accept", CONTENT_TYPE + ", */*");
            BasicHttpEntity basicHttpEntity = new BasicHttpEntity();
            basicHttpEntity.setContent(new ByteArrayInputStream(sourceFilename.getBytes()));
            httpPost.setEntity(basicHttpEntity);
//            httpGet.setParams(new BasicHttpParams().setParameter("fileName", "/home/shruti/HKG (copy)/trunk/hkg-ui/src/main/webapp/i18n/EN(IN)0.json"));
            HttpClient httpClient = new DefaultHttpClient();
            HttpResponse httpResponse = httpClient.execute(httpPost);
            statusCode = httpResponse.getStatusLine().getStatusCode();
            LOGGER.debug("status= " + statusCode);
            if (statusCode == 200) {
                HttpEntity entity = httpResponse.getEntity();
                LOGGER.debug("##### entity.isStreaming() " + entity.isStreaming());

                FileOutputStream fileOutputStream = new FileOutputStream(new File(destinationFileName));
                entity.writeTo(fileOutputStream);

                fileOutputStream.flush();
                fileOutputStream.close();

            }
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(SyncRestClient.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

    }

    public Object getRapCalcData(String authToken) {
        long limit = 10000;
        Object documents = null;
        long count = getCountFromMaster(HkCalcMasterDocument.class, authToken);
        long offset = 0;
        Type typeOfListofClass = new TypeToken<List<HkCalcMasterDocument>>() {
        }.getType();
        while (offset <= limit) {

            documents = getDocuments(offset, limit, HkCalcMasterDocument.class, authToken, "isArchive", "isActive", typeOfListofClass);
            if (documents instanceof ResponseEntity) {
                return documents;
            }
            offset += limit - 1;
            convertAndSaveDocument((List<String>) documents, HkCalcMasterDocument.class);
        }

        count = getCountFromMaster(HkCalcFourCDiscountDocument.class, authToken);
        offset = 0;
        typeOfListofClass = new TypeToken<List<HkCalcFourCDiscountDocument>>() {
        }.getType();
        while (offset <= limit) {
            documents = getDocuments(offset, limit, HkCalcFourCDiscountDocument.class, authToken, "isArchive", "isActive", typeOfListofClass);
            if (documents instanceof ResponseEntity) {
                return documents;
            }
            offset += limit - 1;
            convertAndSaveDocument((List<String>) documents, HkCalcFourCDiscountDocument.class);
        }

        count = getCountFromMaster(HkCalcRateDetailDocument.class, authToken);
        offset = 0;
        typeOfListofClass = new TypeToken<List<HkCalcRateDetailDocument>>() {
        }.getType();
        while (offset <= limit) {
            documents = getDocuments(offset, limit, HkCalcRateDetailDocument.class, authToken, "isArchive", "isActive", typeOfListofClass);
            if (documents instanceof ResponseEntity) {
                return documents;
            }
            offset += limit - 1;
            convertAndSaveDocument((List<String>) documents, HkCalcRateDetailDocument.class);
        }
        return documents;
    }

    public long getCountFromMaster(Class<?> documentClass, String authToken) {

        try {
            Object object = executeRequest(new HttpPost(), MASTER_URL + "api/init/retrieve/count", authToken, documentClass.getName(), CountDatabean.class);
            if (object != null && object instanceof ResponseEntity) {
                return -999;
            }
            return ((CountDatabean) object).getCount();

        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(SyncRestClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    public Object getDocuments(Long offset, Long limit, Class<?> documentClass, String authToken, String isArchiveFieldName, String isActiveFieldName, Type typeOfListofClass) {
        try {

            Map<String, String> PropertyNameMap = new HashMap<>();
            PropertyNameMap.put(OFFSET, offset.toString());
            PropertyNameMap.put(LIMIT, limit.toString());
            PropertyNameMap.put(IS_ARCHIVE_FIELD_NAME, isArchiveFieldName);
            PropertyNameMap.put(isActiveFieldName, isArchiveFieldName);
            PropertyNameMap.put(CLASS_NAME, documentClass.getName());

            Object object = executeRequest(new HttpPost(), MASTER_URL + "api/init/retrieve/docswithlimit", authToken, gson.toJson(PropertyNameMap), typeOfListofClass);
            if (object != null && object instanceof ResponseEntity) {
                return null;
            }
            return object;

        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(SyncRestClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}

class AuthenticationToken {

    private String username;
    private String password;
    private String token;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "AuthenticationToken{" + "username=" + username + ", password=" + password + ", token=" + token + '}';
    }

}

class DocumentRequest {

    private Class<?> entityClass;
    private Class<?> documentClass;
    private String companyFieldName;
    private String isArchiveFieldName;
    private String isMongoDocument;

    public DocumentRequest() {
    }

    public Class<?> getEntityClass() {
        return entityClass;
    }

    public void setEntityClass(Class<?> entityClass) {
        this.entityClass = entityClass;
    }

    public Class<?> getDocumentClass() {
        return documentClass;
    }

    public void setDocumentClass(Class<?> documentClass) {
        this.documentClass = documentClass;
    }

    public String getCompanyFieldName() {
        return companyFieldName;
    }

    public void setCompanyFieldName(String companyFieldName) {
        this.companyFieldName = companyFieldName;
    }

    public String getIsArchiveFieldName() {
        return isArchiveFieldName;
    }

    public void setIsArchiveFieldName(String isArchiveFieldName) {
        this.isArchiveFieldName = isArchiveFieldName;
    }

    public String getIsMongoDocument() {
        return isMongoDocument;
    }

    public void setIsMongoDocument(String isMongoDocument) {
        this.isMongoDocument = isMongoDocument;
    }

    public DocumentRequest(Class<?> entityClass, Class<?> documentClass, String companyFieldName, String isArchiveFieldName, String isMongoDocument) {
        this.entityClass = entityClass;
        this.documentClass = documentClass;
        this.companyFieldName = companyFieldName;
        this.isArchiveFieldName = isArchiveFieldName;
        this.isMongoDocument = isMongoDocument;
    }

}
