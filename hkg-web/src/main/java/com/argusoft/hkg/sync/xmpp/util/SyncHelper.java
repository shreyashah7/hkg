/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.sync.xmpp.util;

import com.argusoft.email.common.core.EmailService;
import com.argusoft.email.common.core.serviceimpl.EmailServiceImpl;
import com.argusoft.email.common.model.Attachment;
import com.argusoft.email.common.model.Email;
import com.argusoft.hkg.common.constantutil.HkSystemConstantUtil;
import com.argusoft.hkg.core.HkFoundationService;
import com.argusoft.hkg.model.HkSystemConfigurationEntity;
import com.argusoft.hkg.sync.center.core.HkUMSyncService;
import com.argusoft.hkg.web.config.WebApplicationInitializerConfig;
import com.argusoft.sync.center.model.HkSystemConfigurationDocument;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.context.WebApplicationContext;

/**
 *
 * @author shruti
 */
@Service
public class SyncHelper {
    static org.slf4j.Logger LOGGER = LoggerFactory.getLogger(SyncHelper.class);
    private static final String SYNC_MAIN_DIRECTORY = "Hkg_Sync";
    private final static String SYNC_TRANSACTION_LOG_MSG_DIRECTORY = "Tx_Log_Msgs";
    private final static String SYNC_TRANSACTION_LOG_ERROR_DIRECTORY = "Tx_Log_Errors";
    public final static String SYNC_MAIN_MSG_DIR_PATH = System.getProperty("user.home") + File.separator + SYNC_MAIN_DIRECTORY + File.separator + SYNC_TRANSACTION_LOG_MSG_DIRECTORY + File.separator;
    public final static String SYNC_MAIN_ERROR_DIR_PATH = System.getProperty("user.home") + File.separator + SYNC_MAIN_DIRECTORY + File.separator + SYNC_TRANSACTION_LOG_ERROR_DIRECTORY + File.separator;
    public final static String SYNC_MAIN_DIR_PATH = System.getProperty("user.home") + File.separator + SYNC_MAIN_DIRECTORY + File.separator;
    public static final String FRANCHISE_ID = "FranchiseId";
    public static final String IS_MASTER__STRING = "IsMaster";
    public static final String SYNC_EXCEPTION_MAIL_SUBJECT = "Sync exception notification";
    public static final String SYNC_EXCEPTION_MAIL_MESSAGE = "An exception was thrown by sync component. The exception log is as follows.";
    public static String SYNC_MAIL_TO = "";
    public final static String ERROR_LOG_FILES = System.getProperty("user.home") + File.separator + SYNC_MAIN_DIRECTORY + File.separator + "hkgerrorlog" + File.separator;
    private final static String ERROR_FILES_PREFIX = "ERROR_";

    @Autowired
    private EmailService emailService;
    @Autowired
    private HkUMSyncService hkUMSyncService;
    @Autowired
    private WebApplicationContext applicationContext;
    @PostConstruct
    public void init() {
        File file = new File(SYNC_MAIN_DIR_PATH);
        if (!file.exists()) {
            file.mkdir();
        }
        file = new File(SYNC_MAIN_ERROR_DIR_PATH);
        if (!file.exists()) {
            file.mkdir();
        }
        file = new File(ERROR_LOG_FILES);
        if (!file.exists()) {
            file.mkdir();
        }
        createTransactionLogMessageDirectory();
        String key = HkSystemConstantUtil.FranchiseConfiguration.DEFAULT_XMPP_EMAIL_ADDRESS;
        if (WebApplicationInitializerConfig.IS_MASTER) {
            HkFoundationService bean = applicationContext.getBean(HkFoundationService.class);
            HkSystemConfigurationEntity configuration = bean.retrieveSystemConfigurationByKey(key, 1l);
            if (configuration != null) {
                SYNC_MAIL_TO = configuration.getKeyValue();
            }
        } else {

            HkSystemConfigurationDocument documentById = (HkSystemConfigurationDocument) hkUMSyncService.getDocumentById(key, HkSystemConfigurationDocument.class);
            if (documentById != null) {
                System.out.println("documentById.getKeyValue() " + documentById.getKeyValue());
                SYNC_MAIL_TO = documentById.getKeyValue();
            }
        }
    }

    public void createTransactionLogMessageDirectory() {
        File file = new File(SYNC_MAIN_MSG_DIR_PATH);
        if (!file.exists()) {
            file.mkdir();
        }
    }

    public void sendEmail(Exception exception, String mailSubject, String message, File... attachments) {
        String key = HkSystemConstantUtil.FranchiseConfiguration.DEFAULT_XMPP_EMAIL_ADDRESS;
        if (WebApplicationInitializerConfig.IS_MASTER) {
            HkFoundationService bean = applicationContext.getBean(HkFoundationService.class);
            HkSystemConfigurationEntity configuration = bean.retrieveSystemConfigurationByKey(key, 1l);
            if (configuration != null) {
                SYNC_MAIL_TO = configuration.getKeyValue();
            } else {
                LOGGER.warn("Default mail address is not set. Could not send error message");
                return;
            }
            System.out.println("configuration.getKeyValue() " + configuration.getKeyValue());
        } else {

            HkSystemConfigurationDocument documentById = (HkSystemConfigurationDocument) hkUMSyncService.getDocumentById(key, HkSystemConfigurationDocument.class);
            if (documentById != null) {
                System.out.println("documentById.getKeyValue() " + documentById.getKeyValue());
                SYNC_MAIL_TO = documentById.getKeyValue();
            } else {
                LOGGER.warn("Default mail address is not set. Could not send error message");
                return;
            }
        }
        Writer writer = new StringWriter();
        PrintWriter attchmentWriter = new PrintWriter(writer);
        exception.printStackTrace(attchmentWriter);
        String attachmentContent = writer.toString();

        Email email = new Email();
        String sendToList[];
        String mailId = SyncHelper.SYNC_MAIL_TO;
        if (!StringUtils.isEmpty(mailId)) {

            sendToList = new String[]{mailId};

            email.setTo(sendToList);
//        String mailSubject = SyncHelper.SYNC_EXCEPTION_MAIL_SUBJECT;

            email.setSubject(mailSubject);
            StringBuilder msg = new StringBuilder();
            msg.append("<html xmlns='http://www.w3.org/1999/xhtml'>");
            msg.append("<head>");
            msg.append("<meta http-equiv='Content-Type' content='text/html; charset=iso-8859-1' />");
            msg.append("<title>HKG Service Exception</title>");
            msg.append("</head>");
            msg.append("<body>");
            msg.append("<div style='margin:0 auto;color:#00008B; font-family: Arial, Helvetica, sans-serif; font-size:12px;' > <hr style='border-bottom:solid 1px #00008B; border-top:none; border-left:none; border-right:none; margin-bottom:5px;'/>");
            msg.append("<h3>");
            msg.append(message);

            msg.append("</h3>");
            msg.append("<hr style='border-bottom:solid 1px #00008B; border-top:none; border-left:none; border-right:none; margin-bottom:5px;'/>");
            msg.append("<br/>This email was sent from " + WebApplicationInitializerConfig.CURRENT_IP + " ip.<br/><div>With Best Wishes, <br/>Team HKG.</div>");
            msg.append("</body>");
            msg.append("</html>");

            email.setMessageBody(msg.toString());
            Attachment attachment1 = new Attachment();
            attachment1.setFileContent(attachmentContent.getBytes());
            attachment1.setFileName(ERROR_LOG_FILES + "EXCEPTION_ " + new Date().getTime() + ".txt");
            if (attachments == null || attachments.length == 0) {
                email.setAttachment(new Attachment[]{attachment1});
            } else {
                Attachment[] attachmentArray = new Attachment[attachments.length + 1];
                int i = 0;
                for (; i < attachments.length; i++) {
                    try {
                        attachmentArray[i] = new Attachment();
                        attachmentArray[i].setFileContent(Files.readAllBytes(Paths.get(attachments[i].getPath())));
                        attachmentArray[i].setFileName(attachments[i].getName());
                    } catch (IOException ex) {
                        Logger.getLogger(SyncHelper.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                attachmentArray[i] = attachment1;
                email.setAttachment(attachmentArray);
            }

            email.setConnectionType("DEFAULT");

            if (emailService == null) {
                //System.out.println("Email Service null...");
                emailService = new EmailServiceImpl();
            } else {
                //System.out.println("Email Service not null...");
            }

            emailService.sendMail(email);
        }
    }

    public static String encodeMapKeyWithDot(String key) {
        return key.replace(".", "_");
    }

    public static String decodeMapKeyWithDot(String key) {
        return key.replace("_", ".");
    }

    public static String writeErrorToFile(Exception exception, String suffix) {
        try (PrintWriter printWriter = new PrintWriter(new File(ERROR_LOG_FILES + ERROR_FILES_PREFIX + suffix))) {
            exception.printStackTrace(printWriter);
            printWriter.flush();
            return ERROR_FILES_PREFIX + suffix;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SyncHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
