/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.sync.xmppclient;

import com.argusoft.hkg.nosql.model.HkInvoiceDocument;
import com.argusoft.hkg.sql.sync.model.SyncXmppUser;
import com.argusoft.hkg.sync.SyncXmppUserService;
import com.argusoft.hkg.sync.center.core.HkUMSyncService;
import com.argusoft.hkg.sync.center.core.SyncTransactionLogService;
import com.argusoft.hkg.sync.center.core.impl.SyncTransactionLogServiceImpl.TransactionStatus;
import com.argusoft.hkg.sync.xmpp.chunk.SyncChunkFrame;
import com.argusoft.hkg.sync.xmpp.chunk.SyncChunkTransformer;
import com.argusoft.hkg.sync.xmpp.databeans.SyncXmppUserDatabean;
import com.argusoft.hkg.sync.xmpp.util.SyncEntityDocumentMapper;
import com.argusoft.hkg.sync.xmpp.util.SyncHelper;
import com.argusoft.hkg.sync.xmpp.util.SyncRecepientFilter;
import com.argusoft.hkg.sync.xmpp.util.SyncTransferType;
import com.argusoft.hkg.sync.xmpp.util.SyncTransformerInterface;
import com.argusoft.hkg.web.center.sync.transformers.SyncCenterXmppUserTransformer;
import com.argusoft.hkg.web.config.WebApplicationInitializerConfig;
import com.argusoft.hkg.web.sync.listner.SyncRecordedEntity;
import com.argusoft.hkg.web.sync.listner.SyncTransactionEntity;
import com.argusoft.hkg.web.sync.xmpp.transformers.SyncXmppUserTransformer;
import com.argusoft.sync.center.model.MetadataDocument;
import com.argusoft.sync.center.model.SyncCenterXmppUserDocument;
import com.argusoft.sync.center.model.SyncTransactionLogDocument;
import com.argusoft.sync.center.model.VcardDocument;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonSyntaxException;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.util.JSON;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import static java.lang.Boolean.FALSE;
import java.lang.reflect.Type;
import java.nio.channels.Channels;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.ReconnectionManager;
import static org.jivesoftware.smack.ReconnectionManager.getInstanceFor;
import org.jivesoftware.smack.SASLAuthentication;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;
import org.jivesoftware.smack.roster.RosterListener;
import org.jivesoftware.smack.sasl.SASLMechanism;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.iqregister.AccountManager;
import org.jivesoftware.smackx.vcardtemp.VCardManager;
import org.jivesoftware.smackx.vcardtemp.packet.VCard;
import org.json.JSONException;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 *
 * @author shruti
 */
@Service
public class SyncXmppClientImpl implements SyncXmppClient {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(SyncXmppClientImpl.class);
    @Autowired
    private SyncXmppUserDatabean hkXmppUserDatabean;

    @Autowired
    private SyncCenterXmppUserTransformer hkXmppUserDocumentTransformer;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private SyncEntityDocumentMapper entityDocumentMapper;

    @Autowired
    private SyncChunkTransformer chunkTransformer;

    @Autowired
    private SyncTransactionLogService syncTransactionLogService;

    @Autowired
    @Qualifier("syncFranchiseRecepientFilter")
    private SyncRecepientFilter recepientFilter;

    @Autowired
    private HkUMSyncService uMSyncService;
    private ReconnectionManager reconnectionManager;

    private XMPPTCPConnection connection;

    private ChatManager chatManager;

    private Roster roster;

    private Map<String, Object> vCards;

    private DB mongodb;

    private Set<String> masterJids;
    private final Gson gson;

//    private boolean isMongoDbConnected = false;
    private static final String FRANCHISE_ID = "FranchiseId";

    private int lastchunkid = -1;

    private Boolean isTigaseStatusCheckThreadRunning = false;
    private String senderJid;
    private final String resourcedId = "XMPP_ResourceId";
    private Thread tigaseStatusThread = null;
    private XMPPTCPConnectionConfiguration config;
    @Autowired
    private SyncHelper syncHelper;

    private boolean isXmppDownErrorSent = false;
    private final ExecutorService cachedThreadPool;
    private final ExecutorService fixedThreadPool;

    /**
     * @return the masterJids
     */
    public Set<String> getMasterJids() {
        return Collections.unmodifiableSet(masterJids);
    }

    /**
     * @param masterJids the masterJids to set
     */
    public void setMasterJids(Set<String> masterJids) {
        this.masterJids = masterJids;
    }

    private class Status {

        private static final String SUCCESS = "Success";
        private static final String FAILURE = "Failure";
    }

    public SyncXmppClientImpl() {
        this.masterJids = new HashSet<>();
        int availableProcessors = Runtime.getRuntime().availableProcessors();
        int totalThreads = availableProcessors * WebApplicationInitializerConfig.PER_CORE_THREADS;
        this.fixedThreadPool = Executors.newFixedThreadPool(availableProcessors * WebApplicationInitializerConfig.PER_CORE_THREADS);
        this.cachedThreadPool = Executors.newCachedThreadPool();
        final JsonSerializer<Date> dateSerializer = (Date src, Type typeOfSrc, JsonSerializationContext context) -> src == null ? null : new JsonPrimitive(src.getTime());
        @SuppressWarnings("deprecation")
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

    /**
     *
     */
    @PostConstruct
    @Override
    public void connect() {
        vCards = new HashMap<>();
        try {
            masterJids.add("master@" + WebApplicationInitializerConfig.XMPP_DOMAIN);
            List<? extends Object> retrieveAllDocuments = uMSyncService.retrieveAllDocuments(VcardDocument.class);
            if (!CollectionUtils.isEmpty(retrieveAllDocuments)) {
                for (Object retrieveAllDocument : retrieveAllDocuments) {
                    VcardDocument vcard = (VcardDocument) retrieveAllDocument;
                    boolean isMaster = Boolean.parseBoolean(vcard.getFields().get(SyncHelper.IS_MASTER__STRING));
                    if (isMaster) {
                        masterJids.add(vcard.getJid());
                    }
                    vCards.put(vcard.getId(), vcard);
                }
            }
            SmackConfiguration.setDefaultPacketReplyTimeout(30000);
            SASLAuthentication.blacklistSASLMechanism(SASLMechanism.DIGESTMD5);
            SASLAuthentication.blacklistSASLMechanism(SASLMechanism.CRAMMD5);
            SASLAuthentication.blacklistSASLMechanism(SASLMechanism.GSSAPI);
            SASLAuthentication.blacklistSASLMechanism("X-OAUTH2");
            SASLAuthentication.blacklistSASLMechanism("SCRAM-SHA-1");

            config = XMPPTCPConnectionConfiguration.builder().setHost(WebApplicationInitializerConfig.XMPP_HOSTNAME)
                    .setPort(WebApplicationInitializerConfig.XMPP_PORT)
                    .setServiceName(WebApplicationInitializerConfig.XMPP_DOMAIN)
                    .setSecurityMode(SecurityMode.disabled)
                    .setDebuggerEnabled(false)
                    .setCompressionEnabled(false)
                    .build();
            connection = new XMPPTCPConnection(config);
            connection.setUseStreamManagement(true);
            connection.setUseStreamManagementResumption(true);

            connection.connect();

            LOGGER.debug("xmpp connected.");
            addReconnectionManager();
            addConnectionListener();
            /**
             * This is a temporary call to avoid blocking issue in mongodb.
             */
            uMSyncService.retrieveAllDocuments(HkInvoiceDocument.class);
            LOGGER.debug("WebApplicationInitializerConfig.IS_MASTER " + WebApplicationInitializerConfig.IS_MASTER);
            if (WebApplicationInitializerConfig.IS_MASTER) {
                SyncXmppUserTransformer xmppUserTransformer = applicationContext.getBean(SyncXmppUserTransformer.class);
                hkXmppUserDatabean = xmppUserTransformer.getHkXmppUserByFranchiseId(0l);
                if (hkXmppUserDatabean == null || (hkXmppUserDatabean != null && !hkXmppUserDatabean.getUserCreatedInTigase())) {
                    createMasterXmppUser(xmppUserTransformer);
                }
                if (hkXmppUserDatabean.getUserCreatedInTigase()) {
                    login(hkXmppUserDatabean.getUserName(), hkXmppUserDatabean.getPassword(), resourcedId);
                }

                createRemainingXmppUserInTigase();

            } else {
                SyncCenterXmppUserDocument xmppUserDocument = hkXmppUserDocumentTransformer.getUserById();
                LOGGER.debug("xmppUserDocument " + xmppUserDocument);
                if (xmppUserDocument != null) {
                    hkXmppUserDocumentTransformer.convertHkXmppUserDocumentToHkXmppUserDatabean(xmppUserDocument, hkXmppUserDatabean);
                    LOGGER.debug("hkXmppUserDatabean" + hkXmppUserDatabean);
                    if ((hkXmppUserDatabean != null && hkXmppUserDatabean.getUserName() != null)) {
                        login(hkXmppUserDatabean.getUserName(), hkXmppUserDatabean.getPassword(), resourcedId);
                    }
                }
            }
            isTigaseStatusCheckThreadRunning = false;
            tigaseStatusThread = null;

        } catch (SmackException ex) {
            if (!isTigaseStatusCheckThreadRunning) {
                startThreadToCheckTigaseStatus();
            }
            if (!isXmppDownErrorSent) {
                syncHelper.sendEmail(ex, "Xmpp connection error", "Xmpp connection error occured. To check exception details please open attachment.");
                isXmppDownErrorSent = true;
            }
        } catch (IOException | XMPPException ex) {
            Logger.getLogger(SyncXmppClientImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        senderJid = hkXmppUserDatabean.getUserName() + "@" + WebApplicationInitializerConfig.XMPP_DOMAIN;
    }

    /**
     * This method will add all remaining user for that franchise is all ready
     * created but due to some reason these users were not created in tigase
     * server database
     */
    public void createRemainingXmppUserInTigase() {
        LOGGER.debug("createRemainingXmppUserInTigase method called");
        SyncXmppUserService syncXmppUserService = applicationContext.getBean(SyncXmppUserService.class);
        List<SyncXmppUser> usersByUserCreatedInTigase = syncXmppUserService.retrieveUserByUserCreatedInTigase(false);
        for (SyncXmppUser xmppUser : usersByUserCreatedInTigase) {
            LOGGER.debug("remaining user in tigase=" + xmppUser.getUserName());
            LOGGER.debug("remaining user in tigase=" + xmppUser.getFranchise());
            try {
                VcardDocument vcardDocument = (VcardDocument) uMSyncService.getDocumentById(xmppUser.getFranchise().toString(), VcardDocument.class);
                Map<String, String> properties = vcardDocument.getFields();
                Boolean addUser = addUser(xmppUser.getUserName(), xmppUser.getPassword(), null);
                if (addUser) {
                    addVCard(xmppUser.getUserName(), xmppUser.getPassword(), properties);
                    xmppUser.setUserCreatedInTigase(addUser);
                    syncXmppUserService.createUser(xmppUser);
                    createRosterEntry(xmppUser.getUserName());
                }

            } catch (SmackException.NoResponseException | XMPPException.XMPPErrorException | SmackException.NotConnectedException ex) {
                Logger.getLogger(SyncXmppClientImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void deleteExtraUsersFromXMPPServer() {
        SyncXmppUserService syncXmppUserService = applicationContext.getBean(SyncXmppUserService.class);
        List<SyncXmppUser> retrieveAllActiveUsers = syncXmppUserService.retrieveAllActiveUsers();
        Map<String, SyncXmppUser> xmppUserMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(retrieveAllActiveUsers)) {
            for (SyncXmppUser user : retrieveAllActiveUsers) {
                xmppUserMap.put(user.getUserName(), user);
            }
        }
        Set<RosterEntry> entries = roster.getEntries();
        for (RosterEntry entry : entries) {
            if (xmppUserMap.get(entry.getUser().split("@")[0]) == null) {
                try {
                    XMPPTCPConnection deleteExtraXmppUsers = new XMPPTCPConnection(config);
                    roster.removeEntry(entry);
                    deleteExtraXmppUsers.connect();
                    deleteExtraXmppUsers.login(entry.getUser().split("@")[0], "argusadmin");
                    AccountManager accountManager = AccountManager.getInstance(deleteExtraXmppUsers);
                    accountManager.deleteAccount();
                } catch (IOException | XMPPException | SmackException ex) {
                }
            }
        }

    }

    /**
     * This method will used to check continuous whether tigase server is
     * available or not and if available it will connect with it. This method is
     * useful when during initial setup there was Tigase server not available
     * for first time
     */
    public void startThreadToCheckTigaseStatus() {
        LOGGER.debug("in startThreadToCheckTigaseStatus ");
        isTigaseStatusCheckThreadRunning = true;
        if (tigaseStatusThread == null) {
            tigaseStatusThread = new Thread(new Runnable() {
                private final int initialtime = 1000;
                private final int inceamentRate = 25;
                private final long maxSleepTime = 60000;
                private int currentSleepTime = initialtime;

                @Override
                public void run() {
                    while (isTigaseStatusCheckThreadRunning) {
                        LOGGER.debug("initialtime " + initialtime + "   " + inceamentRate + "   " + currentSleepTime);
                        try {
                            Thread.sleep(currentSleepTime);
                            if ((((currentSleepTime * inceamentRate) / 100) + currentSleepTime) < maxSleepTime) {
                                currentSleepTime += (currentSleepTime * inceamentRate) / 100;
                            }
                            connect();
                        } catch (InterruptedException ex) {
                            LOGGER.debug(ex + "");
                        }
                    }
                }
            });
            tigaseStatusThread.start();
        }

    }

    public void createMasterXmppUser(SyncXmppUserTransformer xmppUserTransformer) {
        try {
            String masterXmppUsername = "master";
            String masterXmppPswd = "argusadmin";
            if (hkXmppUserDatabean == null) {
                hkXmppUserDatabean = new SyncXmppUserDatabean();
                hkXmppUserDatabean.setUserCreatedInTigase(false);
            }
            hkXmppUserDatabean.setUserName(masterXmppUsername);
            hkXmppUserDatabean.setPassword(masterXmppPswd);

            SyncXmppUser hkXmppUser = xmppUserTransformer.convertHkXmppUserDatabeanToHkXmppUser(hkXmppUserDatabean, null);
            Boolean isUserAddedTigase = addUser(masterXmppUsername, masterXmppPswd, null);
            hkXmppUser.setUserCreatedInTigase(isUserAddedTigase);
            if (isUserAddedTigase) {
                Map<String, String> properties = new HashMap<>();
                properties.put(FRANCHISE_ID, "0");
                properties.put(SyncHelper.IS_MASTER__STRING, "true");
                addVCard(hkXmppUser.getUserName(), hkXmppUser.getPassword(), properties);
                hkXmppUserDatabean.setUserCreatedInTigase(true);
            }
            xmppUserTransformer.saveXmppUser(hkXmppUser);
        } catch (SmackException.NoResponseException | XMPPException.XMPPErrorException | SmackException.NotConnectedException ex) {
            Logger.getLogger(SyncXmppClientImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void login(String userName, String password, String resourceId) {
        try {
            roster = Roster.getInstanceFor(connection);
            addRoasterListener();
            connection.login(userName, password, resourceId);
            roster.reloadAndWait();
            chatManager = ChatManager.getInstanceFor(connection);
            addChatListener();
            displayBuddyList();
            LOGGER.debug("login success=" + userName);
            processBatchMessagesOnConnection();
            createRemainingRosterEntries();
        } catch (XMPPException | SmackException | IOException ex) {
            syncHelper.sendEmail(ex, "Xmpp Login error", "Xmpp Login error occured. To check exception details please open attachment.");
            Logger.getLogger(SyncXmppClientImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void createRemainingRosterEntries() {
        List<VcardDocument> remainingVcardDocuments = uMSyncService.getRemainingVcardDocuments();
        if (!CollectionUtils.isEmpty(remainingVcardDocuments)) {
            for (VcardDocument remainingVcardDocument : remainingVcardDocuments) {
                if (createRosterEntry(remainingVcardDocument.getJid().split("@")[0])) {
                    remainingVcardDocument.setIsRosterEntryCreated(true);
                    uMSyncService.saveOrUpdateDocument(remainingVcardDocument);
                }
            }
        }
    }

    private void addReconnectionManager() {
        reconnectionManager = getInstanceFor(connection);
        reconnectionManager.disableAutomaticReconnection();

    }

    public void addChatListener() {
        chatManager.addChatListener((Chat chat, boolean createdLocally) -> {
            chat.addMessageListener(SyncXmppClientImpl.this);
        });
    }

    public void addConnectionListener() {
        connection.addConnectionListener(new ConnectionListener() {

            @Override
            public void connected(XMPPConnection connection) {
                LOGGER.debug("XMPP connection connected");
                createRemainingXmppUserInTigase();

            }

            @Override
            public void authenticated(XMPPConnection arg0, boolean arg1) {
                isXmppDownErrorSent = false;
                LOGGER.debug("XMPP connection authenticated");

            }

            @Override
            public void connectionClosed() {
                LOGGER.debug("XMPP connection connectionClosed");
            }

            @Override
            public void connectionClosedOnError(Exception e) {
                LOGGER.debug("XMPP connection connectionClosedOnError");
                startThreadToCheckTigaseStatus();
            }

            @Override
            public void reconnectionSuccessful() {
                LOGGER.debug("XMPP connection reconnectionSuccessful");
            }

            @Override
            public void reconnectingIn(int seconds) {
                LOGGER.debug("XMPP connection reconnectingIn " + seconds + " seconds");
            }

            @Override
            public void reconnectionFailed(Exception e) {
                LOGGER.debug("XMPP connection reconnectionFailed");
            }
        });
    }

    public void processBatchMessagesOnConnection() {
        LOGGER.debug("processBatchMessagesOnConnection start");
        Collection<RosterEntry> entries = roster.getEntries();
        sendBatchMessages();
        LOGGER.debug("processBatchMessagesOnConnection end");
    }

    public boolean createRosterEntry(String userId) {
        try {
            if (roster != null) {
                roster.createEntry(userId + "@" + WebApplicationInitializerConfig.XMPP_DOMAIN, userId, null);
                return true;
            } else {
                return false;
            }
        } catch (SmackException.NotLoggedInException | SmackException.NoResponseException | XMPPException.XMPPErrorException | SmackException.NotConnectedException ex) {
            Logger.getLogger(SyncXmppClientImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public void insertRecords(String jsonObj, String documentName) throws ClassNotFoundException, Exception {
        LOGGER.debug("Insert entity:   doc: " + documentName);
        //TODO: convert JSON to Object type and insert into database
        if (!StringUtils.isEmpty(jsonObj)) {
            try {
                Class<?> documentClass = Class.forName(documentName);

                Object obj = gson.fromJson(jsonObj, documentClass);
                if (documentClass.equals(VcardDocument.class)) {
                    VcardDocument vcard = (VcardDocument) obj;
                    roster.createEntry(vcard.getJid(), vcard.getJid().split("@")[0], null);
                }
                SyncTransformerInterface transformerInstance = entityDocumentMapper.getDocumentTransformerInstance(documentClass);
                if (transformerInstance != null) {
                    transformerInstance.save(obj);
                }
            } catch (ClassNotFoundException | JsonSyntaxException | InstantiationException | IllegalAccessException exception) {
                if (exception instanceof JsonParseException || exception instanceof JSONException) {
                    LOGGER.debug("exception1 : jsonobj=" + jsonObj);
                    syncHelper.sendEmail(exception, "Json Exception occured", "An exception was thrown while parsing Json. To check exception details please open attachment. The Json string is: </br>" + jsonObj);
                }
            }
        }

    }

     public void processIncomingMessage(String jsonObj, String from, String threadId) throws ClassNotFoundException {
        SyncChunkFrame chunkFrame = null;
        Long transactionId = null;
        SyncTransactionLogDocument transactionLogDocument = null;
        try {
            chunkFrame = gson.fromJson(jsonObj, SyncChunkFrame.class);
            transactionLogDocument = syncTransactionLogService.retrieve(chunkFrame.getId(), from);
            transactionId = chunkFrame.getId();
            if (from != null) {
                from = from.split("/")[0];
            }
            LOGGER.debug("------>processIncomingMessage , chunkframe.getId" + chunkFrame.getId() + "  chunkFrame.getChunkId()=" + chunkFrame.getChunkId() + "  fin" + chunkFrame.getFin() + " , from= " + from + " lastchunkid=" + lastchunkid + "  " + (chunkFrame.getChunkId() == (lastchunkid + 1)));
            lastchunkid = chunkFrame.getChunkId();
        } catch (Exception exception) {
            if (transactionLogDocument == null) {
                transactionLogDocument = syncTransactionLogService.retrieveByChatThreadAndReceiverJid(threadId, from);
            }
            if (exception instanceof JsonParseException || exception instanceof JSONException) {
                LOGGER.debug("Exception2 : jsonObj" + jsonObj);
                syncHelper.sendEmail(exception, "Json Exception occured", "An exception was thrown while parsing Json. To check exception details please open attachment. The Json string is: </br>", new File(SyncHelper.SYNC_MAIN_DIR_PATH + SyncChunkTransformer.SYNC_PREFIX + chunkFrame.getId()));
            }
            sendResendRequest(chunkFrame, threadId, transactionId, from);
            new File(SyncHelper.SYNC_MAIN_DIR_PATH + SyncChunkTransformer.SYNC_PREFIX + chunkFrame.getId()).delete();

            exception.printStackTrace();
        }
        if (chunkFrame.getFin() < 3) {
            chunkTransformer.extractDataFromChunk(chunkFrame);
            if (chunkFrame.getFin() == 1) {
                try {
                    convertToTransactionEntity(chunkFrame.getId(), transactionLogDocument, from, chunkFrame);
                    SyncChunkFrame chunkFrame1 = new SyncChunkFrame(chunkFrame.getId(), 3, 0, Status.SUCCESS);
                    transactionLogDocument = new SyncTransactionLogDocument();
                    transactionLogDocument.setSentDate(new Date());
                    transactionLogDocument.setNoOfRetry(0);
                    transactionLogDocument.setLastReadChar(0l);
                    transactionLogDocument.setIsSent(false);
                    transactionLogDocument.setStatus(TransactionStatus.IN_PROCESS);
                    transactionLogDocument.setId(UUID.randomUUID().toString());
                    transactionLogDocument.setTransactionId(chunkFrame.getId());
                    transactionLogDocument.setEntityMetadataList(new LinkedList<MetadataDocument>());
                    transactionLogDocument.setIsAckMsg(true);

//                    }
                    sendMessage(gson.toJson(chunkFrame1), from, null, transactionLogDocument);
                } catch (JsonSyntaxException exception) {
                    sendResendRequest(chunkFrame, threadId, transactionId, from);
                    syncHelper.sendEmail(exception, "Json Exception occured", "An exception was thrown while parsing Json. To check exception details please open attachment. The Json string is: </br>", new File(SyncHelper.SYNC_MAIN_DIR_PATH + SyncChunkTransformer.SYNC_PREFIX + chunkFrame.getId()));
                }
            }
        } else if (chunkFrame.getFin() == 3) {

            if (transactionLogDocument == null) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(SyncXmppClientImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
                transactionLogDocument = syncTransactionLogService.retrieve(chunkFrame.getId(), from);
            }
            if (transactionLogDocument != null) {
                new File(SyncHelper.SYNC_MAIN_MSG_DIR_PATH + transactionLogDocument.getMessageFile()).delete();
                transactionLogDocument.setErrorFile(null);
                transactionLogDocument.setMessageFile(null);
                transactionLogDocument.setStatus(TransactionStatus.DELIVERED);
                syncTransactionLogService.save(transactionLogDocument);
            }
        } else if (chunkFrame.getFin() == 4) {
            if (transactionLogDocument == null) {
                transactionLogDocument = syncTransactionLogService.retrieve(chunkFrame.getId(), from);
            }
            transactionLogDocument.setIsSent(FALSE);
            transactionLogDocument.setStatus(TransactionStatus.ERROR_R);
            syncTransactionLogService.save(transactionLogDocument);
            resendMessage(chunkFrame.getId(), from, transactionLogDocument);
        }

    }

    private void sendResendRequest(SyncChunkFrame chunkFrame, String threadId, Long transactionId, String from) {
        SyncChunkFrame chunkFrame1 = new SyncChunkFrame((chunkFrame != null ? chunkFrame.getId() : 0l), 4, 0, threadId);
        SyncTransactionLogDocument transactionLogDocument = new SyncTransactionLogDocument();
        transactionLogDocument.setSentDate(new Date());
        transactionLogDocument.setNoOfRetry(0);
        transactionLogDocument.setLastReadChar(0l);
        transactionLogDocument.setIsSent(false);
        transactionLogDocument.setStatus(TransactionStatus.IN_PROCESS);
        transactionLogDocument.setId(UUID.randomUUID().toString());
        transactionLogDocument.setEntityMetadataList(new LinkedList<MetadataDocument>());
        transactionLogDocument.setIsAckMsg(true);
        if (chunkFrame != null) {
            transactionLogDocument.setTransactionId(chunkFrame.getId());
            new File(SyncHelper.SYNC_MAIN_DIR_PATH + SyncChunkTransformer.SYNC_PREFIX + chunkFrame.getId()).delete();
        }
        sendMessage(gson.toJson(chunkFrame1), from, null, transactionLogDocument);

    }

    public void convertToTransactionEntity(Long id, SyncTransactionLogDocument transactionLogDocument, String from, SyncChunkFrame chunkFrame) {
        File file = new File(syncHelper.SYNC_MAIN_DIR_PATH + "Sync" + id);
        LOGGER.debug("convertToTransactionEntity from file=Sync" + id);
        //TODO: check if the file size is equal to chunkframe.getSize().
        try (FileReader fileReader = new FileReader(file)) {
            SyncTransactionEntity transactionEntity = gson.fromJson(fileReader, SyncTransactionEntity.class);
            List<SyncRecordedEntity> hkRecordedEntitys = transactionEntity.getHkRecordedEntitys();
            for (SyncRecordedEntity hkRecordedEntity : hkRecordedEntitys) {
                insertRecords((String) hkRecordedEntity.getEntity(), hkRecordedEntity.getEntityName());
            }
            file.delete();
        } catch (FileNotFoundException ex) {
            sendResendRequest(chunkFrame, null, id, from);
            Logger.getLogger(SyncXmppClientImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException | ClassNotFoundException ex) {
            sendResendRequest(chunkFrame, null, id, from);
            Logger.getLogger(SyncXmppClientImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception exception) {
            sendResendRequest(chunkFrame, null, id, from);
            if (exception instanceof JsonParseException || exception instanceof JSONException) {
                syncHelper.sendEmail(exception, "Json Exception occured", "An exception was thrown while parsing Json. To check exception details please open attachment. The Json string is: </br>", file);
            }
            exception.printStackTrace();
            String newFileName1 = SyncHelper.SYNC_MAIN_DIR_PATH + File.separator + "Deleted" + new Date().getTime() + "Sync" + id;
            file.renameTo(new File(newFileName1));
            LOGGER.debug("Exception 4 : " + newFileName1);
        }
    }

    /**
     *
     * @param type
     * @param jid jabber id
     */
    public void createPresencePacket(Presence.Type type, String jid) throws SmackException.NotConnectedException {
        Presence subscribe = new Presence(type);
        subscribe.setTo(jid);
        connection.sendPacket(subscribe);
    }

    public void addRoasterListener() {
        roster.addRosterListener(new RosterListener() {
            @Override
            public void entriesDeleted(Collection<String> addresses) {
                LOGGER.debug("entry deleted= " + addresses);
            }

            @Override
            public void entriesUpdated(Collection<String> addresses) {
                LOGGER.debug("entry updated= " + addresses);
            }

            @Override
            public void presenceChanged(Presence presence) {
                LOGGER.debug("Presence changed: " + presence.getFrom() + " " + presence);
                if (presence.isAvailable()) {
                }
            }

            @Override
            public void entriesAdded(Collection<String> addresses) {
                VCardManager cardManager = VCardManager.getInstanceFor(connection);
                for (Iterator<String> iterator = addresses.iterator(); iterator.hasNext();) {
                    try {
                        String jid = iterator.next();
                        VCard vcard = cardManager.loadVCard(jid);
                        if (vcard != null) {
                            LOGGER.debug("========> Vcard");
                            LOGGER.debug("getJabberId  " + vcard.getJabberId());
                            LOGGER.debug("franchiseId " + vcard.getField(FRANCHISE_ID));
                            boolean isMaster = Boolean.parseBoolean(vcard.getField(syncHelper.IS_MASTER__STRING));
                            if (isMaster) {
                                masterJids.add(vcard.getJabberId());
                            }
                            vCards.put(vcard.getField(FRANCHISE_ID), vcard);
                        }
                    } catch (SmackException.NoResponseException | XMPPException.XMPPErrorException | SmackException.NotConnectedException ex) {
                        Logger.getLogger(SyncXmppClientImpl.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                LOGGER.debug("entries added" + addresses);
            }
        });
    }

    /**
     *
     * @param jid
     * @param password
     * @param properties - a map of all properties that you might need to query
     * in order to filter roster entry to send data.
     */
    public void addVCard(String jid, String password, Map<String, String> properties) {
        LOGGER.debug("addVCard " + jid);
        VcardDocument vcardDocument = new VcardDocument();
        try {
            XMPPTCPConnection saveVcardConnection = new XMPPTCPConnection(config);
            saveVcardConnection.connect();
            LOGGER.debug("addvcard connection successful");
            saveVcardConnection.login(jid, password, "vCard_Resource");
            VCard card = new VCard();
            jid += "@" + WebApplicationInitializerConfig.XMPP_DOMAIN;

            vcardDocument.setJid(jid);
            card.setJabberId(jid);
            if (properties != null) {
                vcardDocument.setId(properties.get(FRANCHISE_ID));
                vcardDocument.setFields(properties);
                for (Map.Entry<String, String> entrySet : properties.entrySet()) {
                    card.setField(entrySet.getKey(), entrySet.getValue());
                }
            }
            VCardManager cardManager = VCardManager.getInstanceFor(saveVcardConnection);
            cardManager.saveVCard(card);
            saveVcardConnection.disconnect();
        } catch (SmackException ex) {
            Logger.getLogger(SyncXmppClientImpl.class.getName()).log(Level.SEVERE, null, ex);
            jid += "@" + WebApplicationInitializerConfig.XMPP_DOMAIN;
            vcardDocument.setJid(jid);
            if (properties != null) {
                vcardDocument.setId(properties.get(FRANCHISE_ID));
                vcardDocument.setFields(properties);
            }
            if (!jid.startsWith("master")) {
                vCards.put(properties.get(FRANCHISE_ID), vcardDocument);
            }
        } catch (IOException | XMPPException ex) {
            Logger.getLogger(SyncXmppClientImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (!jid.startsWith("master")) {
            uMSyncService.saveOrUpdateVcardDocument(vcardDocument);
        }
    }

    @Override
    public Boolean addUser(String userid, String password, Map<String, String> properties) throws SmackException.NoResponseException, XMPPException.XMPPErrorException, SmackException.NotConnectedException {
        try {
            XMPPTCPConnection addUserOnlyConnection = new XMPPTCPConnection(config);
            addUserOnlyConnection.connect();
            AccountManager accountManager = AccountManager.getInstance(addUserOnlyConnection);
            Map<String, String> map = new HashMap<>();
            map.put("name", userid);
            map.put("first", userid);
            map.put("last", userid);
            map.put("email", userid + "@gmail.com");
            userid = userid + "@" + WebApplicationInitializerConfig.XMPP_DOMAIN;
            accountManager.createAccount(userid, password, map);
            addUserOnlyConnection.disconnect();
            return true;
        } catch (SmackException | IOException | XMPPException ex) {
            Logger.getLogger(SyncXmppClientImpl.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public SyncRecordedEntity transform(SyncTransformerInterface transformerInterface, SyncRecordedEntity hkRecordedEntity, Object entity, Class<?> entityClass, SyncTransactionLogDocument transactionLogDocument, boolean transformStaticDocument) {
        MetadataDocument metadataDocument;
        SyncRecordedEntity hkRecordedEntityCopy = new SyncRecordedEntity();
        if (hkRecordedEntity != null) {
            hkRecordedEntityCopy.setEntity(hkRecordedEntity.getEntity());
            hkRecordedEntityCopy.setEntityName(hkRecordedEntity.getEntityName());
            hkRecordedEntityCopy.setIsDirty(hkRecordedEntity.isIsDirty());
            hkRecordedEntityCopy.setMetadataDocument(hkRecordedEntity.getMetadataDocument());
        }
        try {
            if ((!transformerInterface.isStaticDocumentType()) || transformStaticDocument) {
                Object documentObject = transformerInterface.convertEntityToDocument(entity);
                if (documentObject == null) {
                    return hkRecordedEntityCopy;
                }
                String jsonDocumentObject = gson.toJson(documentObject);
                String documentName = documentObject.getClass().getName();
                LOGGER.debug("first documentObject.getClass().getName() " + documentName);
                hkRecordedEntityCopy.setEntityName(documentName);
                hkRecordedEntityCopy.setEntity(jsonDocumentObject);
                metadataDocument = createMetadataDocument(entityClass.getName(), !entityClass.isAnnotationPresent(Document.class), transformerInterface.getidMap());
                uMSyncService.saveOrUpdateDocument(metadataDocument);
                hkRecordedEntityCopy.setMetadataDocument(metadataDocument);
            } else {
                return null;
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            metadataDocument = createMetadataDocument(entityClass.getName(), !entityClass.isAnnotationPresent(Document.class), transformerInterface.getidMap());
            uMSyncService.saveOrUpdateDocument(metadataDocument);
            List<MetadataDocument> entityMetadataList = transactionLogDocument.getEntityMetadataList();
            entityMetadataList.add(metadataDocument);
            transactionLogDocument.setEntityMetadataList(entityMetadataList);
            saveTransactionLogOnError(transactionLogDocument, null, null, ex, false);
        }
        return hkRecordedEntityCopy;
    }

    private MetadataDocument createMetadataDocument(String className, boolean isSqlEntity, Map<String, Object> idmMap) {
        MetadataDocument metadataDocument = new MetadataDocument();
        metadataDocument.setClassName(className);
        metadataDocument.setCreatedOn(new Date());
        metadataDocument.setModifiedOn(new Date());
        metadataDocument.setIsArchive(FALSE);
        metadataDocument.setIsSqlEntity(isSqlEntity);
        metadataDocument.setIdMap(idmMap);
        return metadataDocument;
    }

    public Map<String, List<SyncRecordedEntity>> setMapValues(SyncTransformerInterface transformerInterface, Map<String, List<SyncRecordedEntity>> entityRecepientMap, SyncRecordedEntity hkRecordedEntity) {
        List<SyncRecordedEntity> list;
        if (transformerInterface.getSyncTransferType() == SyncTransferType.SEND_TO_ALL) {
            if (entityRecepientMap.containsKey("All")) {
                list = entityRecepientMap.get("All");
            } else {
                list = new LinkedList<>();
            }
            list.add(hkRecordedEntity);
            entityRecepientMap.put("All", list);
        } else if (transformerInterface.getSyncTransferType() == SyncTransferType.SEND_TO_MASTERS) {
            Set<String> recepient = recepientFilter.filterRecepient(vCards, transformerInterface.getQueryParameters());
            for (String recepient1 : recepient) {
                if (!masterJids.contains(recepient1)) {
                    if (entityRecepientMap.containsKey(recepient1)) {
                        list = entityRecepientMap.get(recepient1);
                    } else {
                        list = new LinkedList<>();
                    }
                    list.add(hkRecordedEntity);
                    entityRecepientMap.put(recepient1, list);
                }
            }
            for(String masterJid:masterJids){
                if (entityRecepientMap.containsKey(masterJid)) {
                    list = entityRecepientMap.get(masterJid);
                } else {
                    list = new LinkedList<>();
                }
                list.add(hkRecordedEntity);
                entityRecepientMap.put(masterJid, list);
            }
//            if (entityRecepientMap.containsKey("MASTERS")) {
//                list = entityRecepientMap.get("MASTERS");
//            } else {
//                list = new LinkedList<>();
//            }
//            list.add(hkRecordedEntity);
//            entityRecepientMap.put("MASTERS", list);
        } else {
            Set<String> recepient = recepientFilter.filterRecepient(vCards, transformerInterface.getQueryParameters());
            for (String recepient1 : recepient) {
                if (entityRecepientMap.containsKey(recepient1)) {
                    list = entityRecepientMap.get(recepient1);
                } else {
                    list = new LinkedList<>();
                }
                list.add(hkRecordedEntity);
                entityRecepientMap.put(recepient1, list);
            }
        }
        return entityRecepientMap;
    }

    public Map<String, List<SyncRecordedEntity>> transformEntityToDocument(SyncTransactionEntity hkTransactionEntity, SyncTransactionLogDocument syncTransactionLogDocument) {
        try {
            List<SyncTransformerInterface> transformerInterfaceList;
            Map<String, List<SyncRecordedEntity>> entityRecepientMap = new HashMap<>();
            List<SyncRecordedEntity> hkRecordedEntitys = hkTransactionEntity.getHkRecordedEntitys();
            Map<Class<?>, Object> mapStaticDocuments = new HashMap<>();
            Iterator<SyncRecordedEntity> iterator = hkRecordedEntitys.iterator();
            SyncRecordedEntity hkRecordedEntity = new SyncRecordedEntity();
            while (iterator.hasNext()) {
                hkRecordedEntity = iterator.next();
                Object entity = hkRecordedEntity.getEntity();
                Class<? extends Object> entityClass = entity.getClass();
                transformerInterfaceList = entityDocumentMapper.getTransformerInstance(entityClass);
                if (transformerInterfaceList.size() == 1) {
                    SyncRecordedEntity transform = transform(transformerInterfaceList.get(0), hkRecordedEntity, entity, entityClass, syncTransactionLogDocument, false);
                    if (transform != null) {
                        hkRecordedEntity = transform;
                        entityRecepientMap = setMapValues(transformerInterfaceList.get(0), entityRecepientMap, hkRecordedEntity);
                    } else {
                        mapStaticDocuments.put(entity.getClass(), entity);
                        LOGGER.debug("put it in map" + hkRecordedEntity.getEntityName());
                        iterator.remove();
                    }
                } else {
                    for (SyncTransformerInterface transformerInterface : transformerInterfaceList) {
                        SyncRecordedEntity transform = transform(transformerInterface, hkRecordedEntity, entity, entityClass, syncTransactionLogDocument, false);
                        if (transform != null) {
                            hkRecordedEntity = transform;
                            entityRecepientMap = setMapValues(transformerInterface, entityRecepientMap, hkRecordedEntity);
                        } else {
                            mapStaticDocuments.put(entity.getClass(), entity);
                            LOGGER.debug("put it in map" + hkRecordedEntity.getEntityName());
                            iterator.remove();
                        }
                    }
                }
            }
            if (mapStaticDocuments.size() > 0) {
                for (Map.Entry<Class<?>, Object> entrySet : mapStaticDocuments.entrySet()) {
                    Class<?> key = entrySet.getKey();
                    Object entity = entrySet.getValue();
                    transformerInterfaceList = entityDocumentMapper.getTransformerInstance(key);
                    for (SyncTransformerInterface transformerInterface : transformerInterfaceList) {
                        if (transformerInterface.isStaticDocumentType()) {
                            LOGGER.debug("second documentObject.getClass().getName() " + entity.getClass().getName());
                            SyncRecordedEntity transform = transform(transformerInterface, hkRecordedEntity, entity, key, syncTransactionLogDocument, true);
                            if (transform != null) {
                                hkRecordedEntity = transform;
                                entityRecepientMap = setMapValues(transformerInterface, entityRecepientMap, hkRecordedEntity);
                            }
                        }
                    }
                }
            }
            return entityRecepientMap;

        } catch (InstantiationException ex) {
            Logger.getLogger(SyncXmppClientImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(SyncXmppClientImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(SyncXmppClientImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public void transformAndSaveEntityTodocument(SyncTransactionEntity hkTransactionEntity) throws InstantiationException, IllegalAccessException {
        List<SyncRecordedEntity> hkRecordedEntitys = hkTransactionEntity.getHkRecordedEntitys();
        for (SyncRecordedEntity hkRecordedEntity : hkRecordedEntitys) {
            List<SyncTransformerInterface> transformerInterfaceList = entityDocumentMapper.getTransformerInstance(hkRecordedEntity.getEntity().getClass());

            for (SyncTransformerInterface transformerInterface : transformerInterfaceList) {
                Object documentObject = transformerInterface.convertEntityToDocument(hkRecordedEntity.getEntity());
                String collection = documentObject.getClass().getAnnotation(Document.class
                ).collection();
                if (StringUtils.isEmpty(collection)) {
                    char[] toCharArray = documentObject.getClass().getSimpleName().toCharArray();
                    toCharArray[0] = (Character.toLowerCase(toCharArray[0]));
                    collection = new String(toCharArray);
                }

                DBCollection table = mongodb.getCollection(collection);
                String replaceFirst = gson.toJson(documentObject).replaceFirst("id", "_id");

                BasicDBObject basicDBObject = (BasicDBObject) JSON.parse(replaceFirst);

                table.save(basicDBObject);
            }
        }
    }

    public void sendBatchMessages() {

        cachedThreadPool.execute(() -> {

            List<SyncTransactionLogDocument> pendingTransactions = syncTransactionLogService.retrievePendingTransactions();
            for (SyncTransactionLogDocument pendingTransaction : pendingTransactions) {
                if (!StringUtils.isEmpty(pendingTransaction.getReceiverJid())) {
                    String[] message = chunkTransformer.divideMessageIntoChunks(new File(SyncHelper.SYNC_MAIN_MSG_DIR_PATH + pendingTransaction.getMessageFile()), pendingTransaction.getTransactionId().longValue(), pendingTransaction.getLastReadChar().intValue());
                    sendMessage(message, pendingTransaction.getReceiverJid(), pendingTransaction);
                } else {

                }
            }
        });

    }

    public SyncTransactionLogDocument sendMessage(String[] message, String to, SyncTransactionLogDocument syncTransactionLogDocument) {
        if (!isBatchMessage(to)) {
            Chat chat = chatManager.createChat(to, this);
            syncTransactionLogDocument.setReceiverJid(to);
            syncTransactionLogDocument.setSenderJid(senderJid);
            syncTransactionLogDocument.setChatThreadId(chat.getThreadID());
            try {
                for (int i = 0; i < message.length; i++) {
                    chat.sendMessage(message[i]);
                    syncTransactionLogDocument.setIsSent(true);
                    syncTransactionLogDocument.setStatus(TransactionStatus.SENT);
                }
            } catch (SmackException.NotConnectedException ex) {
                syncTransactionLogDocument.setIsSent(false);
                syncTransactionLogDocument.setStatus(TransactionStatus.XMPP_DOWN);
                Logger.getLogger(SyncXmppClientImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
            //syncTransactionLogDocument.setIsSent(true); do this after proper catch of exception or error
            //put setissent=true in try block and set false in catch block

            if (CollectionUtils.isEmpty(syncTransactionLogDocument.getEntityMetadataList())) {
                syncTransactionLogDocument.setEntityMetadataList(new LinkedList<MetadataDocument>());
            }
            syncTransactionLogService.save(syncTransactionLogDocument);
            LOGGER.debug("Xmpp message sent " + message.length + syncTransactionLogDocument.getTransactionId());
        } else {
            LOGGER.debug("add message to batch started, sendMessage(String[] message, String to)");
            syncTransactionLogDocument.setReceiverJid(to);
            syncTransactionLogDocument.setSenderJid(senderJid);
            syncTransactionLogDocument.setChatThreadId(null);
            syncTransactionLogDocument.setIsSent(false);
            syncTransactionLogDocument.setStatus(TransactionStatus.XMPP_DOWN);
            if (CollectionUtils.isEmpty(syncTransactionLogDocument.getEntityMetadataList())) {
                syncTransactionLogDocument.setEntityMetadataList(new LinkedList<MetadataDocument>());
            }
            syncTransactionLogService.save(syncTransactionLogDocument);
        }

        return syncTransactionLogDocument;
    }

    public void sendMessage(String message, String to, Chat chat, SyncTransactionLogDocument transactionLogDocument) {
        if (chat == null) {
            chat = chatManager.createChat(to, this);
        }
        transactionLogDocument.setReceiverJid(to);
        transactionLogDocument.setSenderJid(senderJid);
        transactionLogDocument.setChatThreadId(chat.getThreadID());
        if (!isBatchMessage(to)) {
            try {
                chat.sendMessage(message);
            } catch (SmackException.NotConnectedException ex) {
                Logger.getLogger(SyncXmppClientImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
            transactionLogDocument.setIsSent(true);
            transactionLogDocument.setStatus(TransactionStatus.SENT);
            if (CollectionUtils.isEmpty(transactionLogDocument.getEntityMetadataList())) {
                transactionLogDocument.setEntityMetadataList(new LinkedList<MetadataDocument>());
            }
            LOGGER.debug("Xmpp message sent " + message);
        } else {
            LOGGER.debug("add message to batch started, sendMessage(String[] message, String to)");
            transactionLogDocument.setReceiverJid(to);
            transactionLogDocument.setSenderJid(senderJid);
            transactionLogDocument.setChatThreadId(null);
            transactionLogDocument.setIsSent(false);
            transactionLogDocument.setStatus(TransactionStatus.XMPP_DOWN);
            if (CollectionUtils.isEmpty(transactionLogDocument.getEntityMetadataList())) {
                transactionLogDocument.setEntityMetadataList(new LinkedList<MetadataDocument>());
            }

        }
        syncTransactionLogService.save(transactionLogDocument);
    }

    public void sendMessage(SyncTransactionEntity hkTransactionEntity, String from) throws XMPPException, SmackException.NotConnectedException {
        fixedThreadPool.execute(new Runnable() {

            @Override
            public void run() {
                SyncTransactionLogDocument syncTransactionLogDocument;
                if (StringUtils.isEmpty(from)) {
                    syncTransactionLogDocument = new SyncTransactionLogDocument();
                    syncTransactionLogDocument.setSentDate(new Date());
                    syncTransactionLogDocument.setNoOfRetry(0);
                    syncTransactionLogDocument.setLastReadChar(0l);
                    syncTransactionLogDocument.setIsSent(false);
                    syncTransactionLogDocument.setStatus(TransactionStatus.IN_PROCESS);
                    syncTransactionLogDocument.setId(UUID.randomUUID().toString());
                    syncTransactionLogDocument.setTransactionId(hkTransactionEntity.getTransactionId());
                    syncTransactionLogDocument.setEntityMetadataList(new LinkedList<MetadataDocument>());
                } else {
                    syncTransactionLogDocument = syncTransactionLogService.retrieve(hkTransactionEntity.getTransactionId(), from);
                    if (syncTransactionLogDocument != null) {
                        int noOfRetry = syncTransactionLogDocument.getNoOfRetry();
                        syncTransactionLogDocument.setNoOfRetry(noOfRetry + 1);
                        if (noOfRetry >= 3) {
                            LOGGER.warn("Max retries eached. Please contact administrator. " + syncTransactionLogDocument.getTransactionId());
                            return;
                        }
                    } else {
                        LOGGER.warn("No transaction log document found for: " + from + " , transactionid: " + hkTransactionEntity.getTransactionId());
                        return;
                    }
                }
                Map<String, List<SyncRecordedEntity>> recepientEntityMap = transformEntityToDocument(hkTransactionEntity, syncTransactionLogDocument);

                List<MetadataDocument> metadataDocuments = new LinkedList<>();
//                List<SyncRecordedEntity> entities;
//                if (recepientEntityMap.containsKey("MASTERS")) {
//
//                    try {
//                        entities = recepientEntityMap.get("MASTERS");
//                        List<SyncRecordedEntity> entitiesTmp = new LinkedList<>();
//                        for (SyncRecordedEntity entity : recepientEntityMap.get("MASTERS")) {
//                            metadataDocuments.add(entity.getMetadataDocument());
//                            entity.setMetadataDocument(null);
//                            entitiesTmp.add(entity);
//                        }
//                        hkTransactionEntity.setHkRecordedEntitys(entitiesTmp);
//                        for (String jid : masterJids) {
//                            SyncTransactionEntity transactionEntity = new SyncTransactionEntity();
//                            List<SyncRecordedEntity> recordedEntitys = recepientEntityMap.get(jid);
//                            if (recordedEntitys != null) {
//                                    //This is a similar loop as given above but I have itrated here to reduce no. of iterations
//                                //Entities to be sent to all masters wont be iterated again for each user.
//                                entitiesTmp = new LinkedList<>();
//                                for (SyncRecordedEntity entity : recordedEntitys) {
//                                    metadataDocuments.add(entity.getMetadataDocument());
//                                    entity.setMetadataDocument(null);
//                                    entitiesTmp.add(entity);
//                                }
//                                List<SyncRecordedEntity> hkRecordedEntitys = hkTransactionEntity.getHkRecordedEntitys();
//                                hkRecordedEntitys.addAll(recordedEntitys);
//
//                                transactionEntity.setHkRecordedEntitys(hkRecordedEntitys);
//                                transactionEntity.setId(hkTransactionEntity.getId());
//                                transactionEntity.setTransactionId(hkTransactionEntity.getTransactionId());
//                                transactionEntity.setTimestamp(hkTransactionEntity.getTimestamp());
//                                System.out.println("hkTransactionEntity.getHkRecordedEntitys() " + hkRecordedEntitys);
//                                //remove this jid from map to avoid duplicate msgs to a user.
//                                recepientEntityMap.remove(jid);
//                            }
//                            try {
//
//                                LOGGER.debug("---->init send message for= " + jid + " thread id=" + Thread.currentThread().getId());
//
//                                String fileName = hkTransactionEntity.getTransactionId() + "_" + new Date().getTime() + "";
//                                RandomAccessFile file = new RandomAccessFile(SyncHelper.SYNC_MAIN_MSG_DIR_PATH + fileName, "rw");
//                                OutputStream newOutputStream = Channels.newOutputStream(file.getChannel());
////
//                                newOutputStream.write(gson.toJson(transactionEntity).getBytes());
//                                syncTransactionLogDocument.setEntityMetadataList(metadataDocuments);
//                                syncTransactionLogDocument.setMessageFile(fileName);
//                                String[] message = chunkTransformer.divideMessageIntoChunks(new File(SyncHelper.SYNC_MAIN_MSG_DIR_PATH + fileName), new Long(hkTransactionEntity.getTransactionId()), 0);
//                                sendMessage(message, jid, syncTransactionLogDocument);
//                                file.close();
//                                syncTransactionLogDocument = copyTransactionLogDocument(syncTransactionLogDocument);
//                            } catch (IOException ex) {
//                                saveTransactionLogOnError(syncTransactionLogDocument, hkTransactionEntity.getTransactionId(), from, ex, true);
//                                Logger
//                                        .getLogger(SyncXmppClientImpl.class
//                                                .getName()).log(Level.SEVERE, null, ex);
//                            }
//
//                        }
//                    } catch (Exception e) {
//                        saveTransactionLogOnError(syncTransactionLogDocument, null, from, e, false);
//                    }
//                }
                if (recepientEntityMap.containsKey("All")) {
                    try {
                        List<SyncRecordedEntity> entitiesTmp = new LinkedList<>();
                        for (SyncRecordedEntity entity : recepientEntityMap.get("All")) {
                            metadataDocuments.add(entity.getMetadataDocument());
                            entity.setMetadataDocument(null);
                            entitiesTmp.add(entity);
                        }
                        hkTransactionEntity.setHkRecordedEntitys(entitiesTmp);
                        for (Map.Entry<String, Object> entry : vCards.entrySet()) {
                            String jid;
                            if (entry.getValue() instanceof VcardDocument) {
                                VcardDocument vcardDocument = (VcardDocument) entry.getValue();
                                jid = vcardDocument.getJid();
                            } else if (entry.getValue() instanceof VCard) {
                                VCard vcardDocument = (VCard) entry.getValue();
                                jid = vcardDocument.getJabberId();
                            } else {
                                jid = null;
                            }
                            if (jid.equals(senderJid)) {
                                continue;
                            }

                            try {

                                List<SyncRecordedEntity> recordedEntitys = recepientEntityMap.get(jid);
                                if (recordedEntitys != null) {
                                    //This is a similar loop as given above but I have itrated here to reduce no. of iterations
                                    //Entities to be sent to all  wont be iterated again for each user.
                                    for (SyncRecordedEntity entity : recordedEntitys) {
                                        metadataDocuments.add(entity.getMetadataDocument());
                                        entity.setMetadataDocument(null);
                                        entitiesTmp.add(entity);
                                    }
                                    List<SyncRecordedEntity> hkRecordedEntitys = hkTransactionEntity.getHkRecordedEntitys();
                                    hkRecordedEntitys.addAll(recordedEntitys);
                                    hkTransactionEntity.setHkRecordedEntitys(hkRecordedEntitys);
                                }

                                LOGGER.debug("---->init send message for= " + jid + " thread id=" + Thread.currentThread().getId());
//                                if (recepientEntityMap.containsKey(jid)) {

//                                }
                                String fileName = hkTransactionEntity.getTransactionId() + "_" + new Date().getTime() + "";
                                RandomAccessFile file = new RandomAccessFile(SyncHelper.SYNC_MAIN_MSG_DIR_PATH + fileName, "rw");
                                OutputStream newOutputStream = Channels.newOutputStream(file.getChannel());
//
                                newOutputStream.write(gson.toJson(hkTransactionEntity).getBytes());
                                syncTransactionLogDocument.setEntityMetadataList(metadataDocuments);
                                syncTransactionLogDocument.setMessageFile(fileName);
                                String[] message = chunkTransformer.divideMessageIntoChunks(new File(SyncHelper.SYNC_MAIN_MSG_DIR_PATH + fileName), new Long(hkTransactionEntity.getTransactionId()), 0);
                                sendMessage(message, jid, syncTransactionLogDocument);
                                syncTransactionLogDocument = copyTransactionLogDocument(syncTransactionLogDocument);
                                file.close();

                            } catch (IOException ex) {
                                saveTransactionLogOnError(syncTransactionLogDocument, hkTransactionEntity.getTransactionId(), from, ex, true);
                                Logger
                                        .getLogger(SyncXmppClientImpl.class
                                                .getName()).log(Level.SEVERE, null, ex);
                            }

                        }
                    } catch (Exception e) {
                        saveTransactionLogOnError(syncTransactionLogDocument, null, from, e, false);
                    }
                } else {

                    for (Map.Entry<String, List<SyncRecordedEntity>> entrySet : recepientEntityMap.entrySet()) {
                        String receiver = entrySet.getKey();
                        List<SyncRecordedEntity> value = entrySet.getValue();
                        List<SyncRecordedEntity> recordedEntitys = new LinkedList<>();
                        try {
                            LOGGER.debug("---->init send message for=" + receiver);
                            for (SyncRecordedEntity entity : value) {
                                metadataDocuments.add(entity.getMetadataDocument());
                                entity.setMetadataDocument(null);
                                recordedEntitys.add(entity);
                            }
                            hkTransactionEntity.setHkRecordedEntitys(recordedEntitys);
                            String fileName = hkTransactionEntity.getTransactionId() + "_" + new Date().getTime() + "";
                            RandomAccessFile file = new RandomAccessFile(SyncHelper.SYNC_MAIN_MSG_DIR_PATH + fileName, "rw");
                            OutputStream newOutputStream = Channels.newOutputStream(file.getChannel());
                            newOutputStream.write(gson.toJson(hkTransactionEntity).getBytes());
                            syncTransactionLogDocument.setEntityMetadataList(metadataDocuments);
                            syncTransactionLogDocument.setMessageFile(fileName);
                            String[] message = chunkTransformer.divideMessageIntoChunks(new File(SyncHelper.SYNC_MAIN_MSG_DIR_PATH + fileName), new Long(hkTransactionEntity.getTransactionId()), 0);
                            sendMessage(message, receiver, syncTransactionLogDocument);
                            file.close();
                        } catch (IOException ex) {
                            Logger.getLogger(SyncXmppClientImpl.class
                                    .getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }
        });
    }

    public SyncTransactionLogDocument copyTransactionLogDocument(SyncTransactionLogDocument syncTransactionLogDocument) {
        if (syncTransactionLogDocument == null) {
            return null;
        }
        SyncTransactionLogDocument transactionLogDocument = new SyncTransactionLogDocument();
        transactionLogDocument.setChatThreadId(syncTransactionLogDocument.getChatThreadId());
        transactionLogDocument.setErrorFile(syncTransactionLogDocument.getErrorFile());
        transactionLogDocument.setEntityMetadataList(syncTransactionLogDocument.getEntityMetadataList());
        transactionLogDocument.setIsAckMsg(syncTransactionLogDocument.getIsAckMsg());
        transactionLogDocument.setIsSent(syncTransactionLogDocument.getIsSent());
        transactionLogDocument.setLastReadChar(syncTransactionLogDocument.getLastReadChar());
        transactionLogDocument.setMessageFile(syncTransactionLogDocument.getMessageFile());
        transactionLogDocument.setNoOfRetry(syncTransactionLogDocument.getNoOfRetry());
        transactionLogDocument.setSenderJid(syncTransactionLogDocument.getSenderJid());
        transactionLogDocument.setSentDate(syncTransactionLogDocument.getSentDate());
        transactionLogDocument.setStatus(syncTransactionLogDocument.getStatus());
        transactionLogDocument.setTransactionId(syncTransactionLogDocument.getTransactionId());
        return transactionLogDocument;
    }

    /**
     *
     * @param syncTransactionLogDocument
     * @param transactionId
     * @param jid
     * @param ex
     * @param isResendRequired
     */
    private void saveTransactionLogOnError(SyncTransactionLogDocument syncTransactionLogDocument, Long transactionId, String jid, Exception ex, boolean isResendRequired) {
        if (isResendRequired) {
            resendMessage(transactionId, jid, syncTransactionLogDocument);
        }
        if (syncTransactionLogDocument != null) {
            syncTransactionLogDocument.setStatus(TransactionStatus.ERROR);
            syncTransactionLogDocument.setErrorFile(SyncHelper.writeErrorToFile(ex, syncTransactionLogDocument.getReceiverJid() + syncTransactionLogDocument.getTransactionId() + "" + new Date().getTime()));
            syncTransactionLogService.save(syncTransactionLogDocument);
        }

        Logger.getLogger(SyncXmppClientImpl.class
                .getName()).log(Level.SEVERE, null, ex);
    }

    private void resendMessage(Long transationId, String receiver, SyncTransactionLogDocument transactionDocToSend) {
        LOGGER.debug("in resendMessage. transactionid= " + transationId + " , recipient jid=" + receiver + " ,transactionDocToSend" + transactionDocToSend);

        if (transactionDocToSend == null) {
            transactionDocToSend = syncTransactionLogService.retrieve(transationId, receiver);
        }
        transactionDocToSend.setNoOfRetry(transactionDocToSend.getNoOfRetry() + 1);
        String[] message = chunkTransformer.divideMessageIntoChunks(new File(SyncHelper.SYNC_MAIN_MSG_DIR_PATH + transactionDocToSend.getMessageFile()), transactionDocToSend.getTransactionId().longValue(), transactionDocToSend.getLastReadChar().intValue());
        sendMessage(message, receiver, transactionDocToSend);

    }

    private boolean isBatchMessage(String user) {
        if (!connection.isConnected()) {
            return true;
        }
        return false;
    }

    public void displayBuddyList() {

        Collection<RosterEntry> entries = roster.getEntries();
//        VCardManager cardManager = VCardManager.getInstanceFor(connection);
        LOGGER.debug("\n\n" + entries.size() + " buddy(ies):");
        for (RosterEntry r : entries) {
            String jid = r.getUser();
            Presence presence = roster.getPresence(jid);
            LOGGER.debug(jid + "   " + r.getStatus() + "   " + r.getType() + "  " + presence.getType());
        }
    }

    @PreDestroy
    public void disconnect() {
        LOGGER.debug("========>DisConnecting...");
        try {
            connection.disconnect();
            cachedThreadPool.awaitTermination(1, TimeUnit.SECONDS);
            fixedThreadPool.awaitTermination(1, TimeUnit.SECONDS);
            cachedThreadPool.shutdownNow();
            fixedThreadPool.shutdownNow();
        } catch (Exception e) {
            LOGGER.debug(e.getMessage());
        }

    }

    @Override
    public void processMessage(Chat chat, Message message) {
        if (message.getType() == Message.Type.chat) {
            try {
                processIncomingMessage(message.getBody(), message.getFrom(), chat.getThreadID());

            } catch (ClassNotFoundException ex) {
                Logger.getLogger(SyncXmppClientImpl.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
