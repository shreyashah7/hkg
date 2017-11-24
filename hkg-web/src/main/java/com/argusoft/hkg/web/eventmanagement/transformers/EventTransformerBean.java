/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.eventmanagement.transformers;

import com.argusoft.generic.database.exception.GenericDatabaseException;
import com.argusoft.hkg.common.constantutil.HkSystemConstantUtil;
import com.argusoft.hkg.common.functionutil.FolderManagement;
import static com.argusoft.hkg.common.functionutil.FolderManagement.UNIQUE_SEPARATOR;
import com.argusoft.hkg.common.functionutil.HkSystemFunctionUtil;
import com.argusoft.hkg.core.HKCategoryService;
import com.argusoft.hkg.core.HkFieldService;
import com.argusoft.hkg.core.HkHRService;
import com.argusoft.hkg.model.HkCategoryEntity;
import com.argusoft.hkg.model.HkEventEntity;
import com.argusoft.hkg.model.HkEventRecipientEntity;
import com.argusoft.hkg.model.HkEventRecipientEntityPK;
import com.argusoft.hkg.model.HkEventRegistrationEntity;
import com.argusoft.hkg.model.HkEventRegistrationEntityPK;
import com.argusoft.hkg.model.HkEventRegistrationFieldEntity;
import com.argusoft.hkg.model.HkEventRegistrationFieldValueEntity;
import com.argusoft.hkg.nosql.core.HkCustomFieldService;
import com.argusoft.hkg.nosql.model.CustomField;
import com.argusoft.hkg.web.assets.databeans.CategoryDataBean;
import com.argusoft.hkg.web.customfield.transformers.CustomFieldTransformerBean;
import com.argusoft.hkg.web.eventmanagement.databeans.EventDataBean;
import com.argusoft.hkg.web.eventmanagement.databeans.EventRecipientDataBean;
import com.argusoft.hkg.web.eventmanagement.databeans.EventRegistrationFieldsDataBean;
import com.argusoft.hkg.web.eventmanagement.databeans.EventUserRegistrationDataBean;
import com.argusoft.hkg.web.eventmanagement.databeans.GuestDataBean;
import com.argusoft.hkg.web.internationalization.transformers.LocalesTransformerBean;
import com.argusoft.hkg.web.center.usermanagement.databeans.LoginDataBean;
import com.argusoft.hkg.web.usermanagement.wrapper.UserManagementServiceWrapper;
import com.argusoft.hkg.web.util.SessionUtil;
import com.argusoft.usermanagement.common.core.UMUserService;
import com.argusoft.usermanagement.common.model.UMContactAddress;
import com.argusoft.usermanagement.common.model.UMDepartment;
import com.argusoft.usermanagement.common.model.UMGroupContact;
import com.argusoft.usermanagement.common.model.UMUser;
import com.argusoft.usermanagement.common.model.UMUserRole;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.logging.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.xhtmlrenderer.pdf.ITextRenderer;

/**
 *
 * @author kuldeep
 */
@Service
public class EventTransformerBean {

    @Autowired
    HkHRService hrService;

    @Autowired
    HKCategoryService categoryService;

    @Autowired
    LoginDataBean loginDataBean;

    @Autowired
    SessionUtil sessionUtil;
    @Autowired
    UMUserService userService;
    @Autowired
    UserManagementServiceWrapper userManagementServiceWrapper;
    @Autowired
    HkCustomFieldService customFieldSevice;
    @Autowired
    HkFieldService fieldService;

    @Autowired
    CustomFieldTransformerBean customFieldTransformerBean;
    @Autowired
    LocalesTransformerBean localesTransformerBean;

    private final String SEPARATOR = ":";
    private static final String BANNER = "banner";
    private static final String BACKGROUND = "background";
    private static final String GALLERY = "gallery";
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Transactional
    public Long createEvent(EventDataBean eventDataBean) throws IOException {
        Set<Long> recipientIdSet = new HashSet<>();
        List<Long> notifyUsersList = null;
        Map<String, List<Long>> notifyUsersMap = new HashMap<>();
        Long userId = null;
        int count = 0;
        HkEventEntity eventEntity = new HkEventEntity();
        String prevTemplateName = eventEntity.getInvitationTemplateName();
        eventEntity = convertEventDataBeanToEventEntity(recipientIdSet, eventDataBean, eventEntity, false, null);

        List<String> featureNames = new ArrayList<>();
        Calendar todayCal = Calendar.getInstance();
        featureNames.add(HkSystemConstantUtil.Feature.EVENT_ADD_EDIT);
        Set<Long> userList = userManagementServiceWrapper.searchUsersByFeatureName(featureNames, loginDataBean.getCompanyId());
        if (eventDataBean.getPublishedOn().before(todayCal.getTime()) || eventDataBean.getPublishedOn().equals(todayCal.getTime())) {
            if (recipientIdSet != null) {
                notifyUsersList = new ArrayList<>();
                for (Long id : recipientIdSet) {
                    if (loginDataBean.getId().equals(id)) {
                    } else {
                        notifyUsersList.add(id);
                    }
                }
                notifyUsersMap.put("invitee", notifyUsersList);
            }
            if (userList != null) {
                notifyUsersList = null;
                for (Long id : userList) {
                    if (loginDataBean.getId().equals(id)) {
                    } else {
                        if (notifyUsersList != null) {
                            notifyUsersList.add(id);
                        } else {
                            notifyUsersList = new ArrayList<>();
                            notifyUsersList.add(id);
                        }
                    }
                }
                notifyUsersMap.put("haveFeature", notifyUsersList);
            }
        } else {
            if (userList != null) {
                notifyUsersList = new ArrayList<>();
                for (Long id : userList) {
                    if (loginDataBean.getId().equals(id)) {
                    } else {
                        notifyUsersList.add(id);
                    }
                }
                notifyUsersMap.put("haveFeature", notifyUsersList);
            }
        }
//        for (Long notifyUser : notifyUsersList) {
//            if (notifyUser.equals(loginDataBean.getId())) {
//                userId = notifyUser;
//                count++;
//            }
//        }
//        if (count >= 0) {
//            notifyUsersList.remove(userId);
//        }
        hrService.createEvent(eventEntity, notifyUsersMap);
        saveBannerAndTemplate(eventDataBean, eventEntity, prevTemplateName, false);
        hrService.updateEvent(eventEntity, null, null, false);
        createOrUpdateCustomField(eventEntity.getId(), eventDataBean);
        userManagementServiceWrapper.createLocaleForEntity(eventDataBean.getEventTitle(), "Event", loginDataBean.getId(), loginDataBean.getCompanyId());
        return eventEntity.getId();
    }

    public void updateEvent(EventDataBean eventDataBean) {
        Set<Long> recipientIdSet = new HashSet<>();
        List<Long> notifyUsersList = null;
        int count = 0;
        Long userId = null;
        HkEventEntity eventEntity = hrService.retrieveEventById(eventDataBean.getId(), true, true);
        String prevTemplateName = eventEntity.getInvitationTemplateName();
        List<String> recipientCodes = new ArrayList<>();
        eventEntity = convertEventDataBeanToEventEntity(recipientIdSet, eventDataBean, eventEntity, true, recipientCodes);

        List<String> featureNames = new ArrayList<>();
        featureNames.add(HkSystemConstantUtil.Feature.MANAGE_EVENT);
        Calendar todayCal = Calendar.getInstance();
        Set<Long> userList = userManagementServiceWrapper.searchUsersByFeatureName(featureNames, loginDataBean.getCompanyId());
        if ((eventEntity.getPublishedOn().before(todayCal.getTime())
                && eventEntity.getStatus().equals(HkSystemConstantUtil.EventStatus.CREATED))) {
            if (recipientIdSet != null) {
                notifyUsersList = new ArrayList<>();
                for (Long id : recipientIdSet) {
                    if (loginDataBean.getId().equals(id)) {
                    } else {
                        notifyUsersList.add(id);
                    }
                }
            }
            if (userList != null) {
                for (Long id : userList) {
                    if (loginDataBean.getId().equals(id)) {
                    } else {
                        if (notifyUsersList != null) {
                            notifyUsersList.add(id);
                        } else {
                            notifyUsersList = new ArrayList<>();
                            notifyUsersList.add(id);
                        }
                    }
                }
            }
        } else if (eventEntity.getStatus().equalsIgnoreCase(HkSystemConstantUtil.EventStatus.CREATED)) {
            if (userList != null) {
                notifyUsersList = new ArrayList<>();
                for (Long id : userList) {
                    if (loginDataBean.getId().equals(id)) {
                    } else {
                        notifyUsersList.add(id);
                    }
                }
            }
        } else if (eventEntity.getStatus().equalsIgnoreCase(HkSystemConstantUtil.EventStatus.UPCOMING)) {
            if (recipientIdSet != null) {
                notifyUsersList = new ArrayList<>();
                for (Long id : recipientIdSet) {
                    if (loginDataBean.getId().equals(id)) {
                    } else {
                        notifyUsersList.add(id);
                    }
                }
            }
            if (userList != null) {
                for (Long id : userList) {
                    if (loginDataBean.getId().equals(id)) {
                    } else {
                        if (notifyUsersList != null) {
                            notifyUsersList.add(id);
                        } else {
                            notifyUsersList = new ArrayList<>();
                            notifyUsersList.add(id);
                        }
                    }
                }
            }
        }
        Set<Long> recipientIdsSet = userManagementServiceWrapper.retrieveRecipientIds(recipientCodes);
        if (eventDataBean.getRegistrationFormUpdated()) {
            hrService.updateEvent(eventEntity, recipientIdsSet, notifyUsersList, true);
        } else {
            hrService.updateEvent(eventEntity, recipientIdsSet, notifyUsersList, false);
        }
        saveBannerAndTemplate(eventDataBean, eventEntity, prevTemplateName, true);
        hrService.updateEvent(eventEntity, null, null, false);
        createOrUpdateCustomField(eventEntity.getId(), eventDataBean);
        userManagementServiceWrapper.createLocaleForEntity(eventDataBean.getEventTitle(), "Event", loginDataBean.getId(), loginDataBean.getCompanyId());
    }

    public void createPhotoGallery(EventDataBean eventDataBean) throws IOException {
        HkEventEntity eventEntity = hrService.retrieveEventById(eventDataBean.getId(), true, true);
        if (!CollectionUtils.isEmpty(eventDataBean.getFileList())) {
            if (!StringUtils.isEmpty(eventEntity.getFolderName())) {
                FolderManagement.saveFile(null, null, eventEntity.getId(), null, eventDataBean.getFileList(), true);
            } else {
                StringBuilder random = new StringBuilder();
                random.append(FolderManagement.FEATURE.EVENT).append(UNIQUE_SEPARATOR).append(loginDataBean.getCompanyId()).append(UNIQUE_SEPARATOR).append(eventEntity.getId()).append(UNIQUE_SEPARATOR).append(FolderManagement.CONSTANT.GALLERY);
                eventEntity.setFolderName(random.toString());
                hrService.updateEvent(eventEntity, null, null, false);
                FolderManagement.saveFile(null, null, eventEntity.getId(), null, eventDataBean.getFileList(), true);
            }
        }
    }

    public void createOrUpdateCustomField(Long id, EventDataBean eventDataBean) {
        Map<Long, Map<String, Object>> val = new HashMap<>();
        val.put(id, eventDataBean.getEventCustom());
        List<String> uiFieldList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(eventDataBean.getEventDbType())) {
            for (Map.Entry<String, String> entrySet : eventDataBean.getEventDbType().entrySet()) {
                uiFieldList.add(entrySet.getKey());
            }
        }
        Map<String, String> uiFieldMap = fieldService.retrieveUIFieldNameWithComponentTypes(uiFieldList);
        List<CustomField> eventCustomField = customFieldSevice.makeCustomField(val, eventDataBean.getEventDbType(), uiFieldMap, HkSystemConstantUtil.FeatureNameForCustomField.EVENT.toString(), loginDataBean.getCompanyId(), id);
        Map<Long, Map<String, Object>> reg = new HashMap<>();
        reg.put(id, eventDataBean.getRegCustom());
        uiFieldList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(eventDataBean.getRegDbType())) {
            for (Map.Entry<String, String> entrySet : eventDataBean.getRegDbType().entrySet()) {
                uiFieldList.add(entrySet.getKey());
            }
        }
        uiFieldMap = fieldService.retrieveUIFieldNameWithComponentTypes(uiFieldList);
        List<CustomField> regCustomField = customFieldSevice.makeCustomField(reg, eventDataBean.getRegDbType(), uiFieldMap, HkSystemConstantUtil.FeatureNameForCustomField.EVENT.toString(), loginDataBean.getCompanyId(), id);
        Map<Long, Map<String, Object>> invi = new HashMap<>();
        invi.put(id, eventDataBean.getInvitationCustom());
        uiFieldList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(eventDataBean.getInvitationDbType())) {
            for (Map.Entry<String, String> entrySet : eventDataBean.getInvitationDbType().entrySet()) {
                uiFieldList.add(entrySet.getKey());
            }
        }
        uiFieldMap = fieldService.retrieveUIFieldNameWithComponentTypes(uiFieldList);
        List<CustomField> inviCustomField = customFieldSevice.makeCustomField(invi, eventDataBean.getInvitationDbType(), uiFieldMap, HkSystemConstantUtil.FeatureNameForCustomField.EVENT.toString(), loginDataBean.getCompanyId(), id);
        Map<HkSystemConstantUtil.SectionNameForCustomField, List<CustomField>> map = new HashMap<>();
        map.put(HkSystemConstantUtil.SectionNameForCustomField.GENERAL, eventCustomField);
        map.put(HkSystemConstantUtil.SectionNameForCustomField.REGISTRATION, regCustomField);
        map.put(HkSystemConstantUtil.SectionNameForCustomField.INVITATIONCARD, inviCustomField);
        customFieldSevice.saveOrUpdate(id, HkSystemConstantUtil.FeatureNameForCustomField.EVENT, loginDataBean.getCompanyId(), map);
    }

    public List<EventDataBean> searchEvents(String searchString) {
        List<HkEventEntity> events = hrService.searchEvents(searchString, loginDataBean.getCompanyId());
        List<EventDataBean> eventDataBeans = new ArrayList<>();
        if (events != null && !CollectionUtils.isEmpty(events)) {
            for (HkEventEntity event : events) {
                EventDataBean eventDataBean = new EventDataBean();
                eventDataBean.setId(event.getId());
                eventDataBean.setEventTitle(event.getEventTitle());
                eventDataBean.setFromDate(HkSystemFunctionUtil.convertToClientDate(event.getFrmDt(), loginDataBean.getClientRawOffsetInMin()));
                eventDataBean.setStatus(HkSystemConstantUtil.EVENT_STATUS_MAP.get(event.getStatus()));
                eventDataBean.setCategory(event.getCategory().getId());
                eventDataBean.setCategoryName(event.getCategory().getCategoryTitle());
                eventDataBeans.add(eventDataBean);
            }

        }
        return eventDataBeans;

    }

    public void saveBannerAndTemplate(EventDataBean eventDataBean, HkEventEntity eventEntity, String prevTemplateName, boolean updateEvent) {
        try {
            String bannerImage = null;
            String tempFileName = null;
            String invitationBackgroundFilePath = null;
            String backgroundFilePath = null;
            if (!updateEvent && eventDataBean.getBannerImageName() != null) {
                bannerImage = FolderManagement.changeFileName(eventDataBean.getBannerImageName(), eventEntity.getId());
                FolderManagement.saveFile(null, null, eventEntity.getId(), null, Arrays.asList(eventDataBean.getBannerImageName()), true);
            }

            if (eventDataBean.getInvitationTemplateName() != null && !eventDataBean.getInvitationTemplateName().equalsIgnoreCase("ND") && eventDataBean.getTemplateHtml() != null) {
                //Check if template has been changed or not
                if (!eventDataBean.getInvitationTemplateName().equals(prevTemplateName)) {
                    invitationBackgroundFilePath = eventDataBean.getInvitationTemplateName();
                    invitationBackgroundFilePath = FolderManagement.changeFileName(invitationBackgroundFilePath, eventEntity.getId());
                    String stringToReplace = "id=\"templateBackground\" style=\"height: 400px; width: 700px;";
                    String replaceString = stringToReplace + "background:url(api/event/getbackgroundimage?file_name=" + invitationBackgroundFilePath + ");background-repeat:no-repeat;background-size: 700px 400px;";
                    eventDataBean.setTemplateHtml(eventDataBean.getTemplateHtml().replace(stringToReplace, replaceString));
                    backgroundFilePath = invitationBackgroundFilePath.substring(0, invitationBackgroundFilePath.lastIndexOf("."));
                    backgroundFilePath += ".html";
                    FolderManagement.saveFile(null, null, eventEntity.getId(), null, Arrays.asList(eventDataBean.getInvitationTemplateName()), false);
                    if (updateEvent) {
                        eventEntity.setInvitationTemplateName(backgroundFilePath);
                    }
                    Map<String, Map<String, byte[]>> invitationTemplateMap = new HashMap<>();
                    Map<String, byte[]> fileDataMap = new HashMap<>();
                    fileDataMap.put(backgroundFilePath, eventDataBean.getTemplateHtml().getBytes());
                    invitationTemplateMap.put("BACKGROUND", fileDataMap);
                    FolderManagement.saveFile(loginDataBean.getCompanyId(), FolderManagement.FEATURE.EVENT, eventEntity.getId(), invitationTemplateMap, null, false);
                } else {
                    Map<String, Map<String, byte[]>> invitationTemplateMap = new HashMap<>();
                    Map<String, byte[]> fileDataMap = new HashMap<>();
                    fileDataMap.put(eventEntity.getInvitationTemplateName(), eventDataBean.getTemplateHtml().getBytes());
                    invitationTemplateMap.put("BACKGROUND", fileDataMap);
                    FolderManagement.saveFile(loginDataBean.getCompanyId(), FolderManagement.FEATURE.EVENT, eventEntity.getId(), invitationTemplateMap, null, false);
                }

            } else if (eventDataBean.getInvitationTemplateName() != null && eventDataBean.getInvitationTemplateName().equalsIgnoreCase("ND")) {
                eventDataBean.setInvitationTemplateName(null);

            } else {
                tempFileName = FolderManagement.getTempFileName(loginDataBean.getCompanyId(), FolderManagement.FEATURE.EVENT, FolderManagement.CONSTANT.BACKGROUND, loginDataBean.getId(), null);
                invitationBackgroundFilePath = FolderManagement.changeFileName(tempFileName, eventEntity.getId());
                eventDataBean.setTemplateHtml(eventDataBean.getTemplateHtml());
                backgroundFilePath = invitationBackgroundFilePath;
                backgroundFilePath += ".html";
                if (updateEvent) {
                    eventEntity.setInvitationTemplateName(backgroundFilePath);
                }
                Map<String, Map<String, byte[]>> invitationTemplateMap = new HashMap<>();
                Map<String, byte[]> fileDataMap = new HashMap<>();
                fileDataMap.put(backgroundFilePath, eventDataBean.getTemplateHtml().getBytes());
                invitationTemplateMap.put("BACKGROUND", fileDataMap);
                FolderManagement.saveFile(loginDataBean.getCompanyId(), FolderManagement.FEATURE.EVENT, eventEntity.getId(), invitationTemplateMap, null, false);
            }
            if (!updateEvent) {
                eventEntity.setBannerImageName(bannerImage);
                eventEntity.setInvitationTemplateName(backgroundFilePath);
            }
        } catch (IOException ex) {
            LOGGER.error(ex.toString());
        }
    }

    public EventDataBean retrieveEventById(Long eventId) {
        HkEventEntity eventEntity = hrService.retrieveEventById(eventId, true, true);
        Map<HkSystemConstantUtil.SectionNameForCustomField, List<Map<Long, Map<String, Object>>>> retrieveDocumentByInstanceId = customFieldTransformerBean.retrieveDocumentByInstanceId(eventId, HkSystemConstantUtil.FeatureNameForCustomField.EVENT, loginDataBean.getCompanyId());
        return convertEventEntityToEventDataBean(eventEntity, new EventDataBean(), true, true, false, retrieveDocumentByInstanceId);
    }

    public List<EventDataBean> retrieveUpcomingEvents(Long categoryId, Boolean haveAddEditRights) {
        List<HkEventEntity> eventEntityList = hrService.retrieveUpcomingEvents(categoryId, loginDataBean.getDepartment(), loginDataBean.getId(), loginDataBean.getCompanyId(), haveAddEditRights);
        return convertEventEntityListToEventDataBeanList(eventEntityList, haveAddEditRights);
    }

    public List<EventDataBean> retrieveCompletedEvents(Long categoryId, Boolean haveAddEditRights) {
        List<HkEventEntity> eventEntityList = hrService.retrieveCompletedEvents(categoryId, loginDataBean.getDepartment(), loginDataBean.getId(), loginDataBean.getCompanyId(), haveAddEditRights);
        return convertEventEntityListToEventDataBeanList(eventEntityList, haveAddEditRights);
    }

    public void removeEvent(Long eventId) {
        EventDataBean eventDataBean = this.retrieveEventById(eventId);
        userManagementServiceWrapper.deleteLocaleForEntity(eventDataBean.getEventTitle(), "Event", "CONTENT", loginDataBean.getId(), loginDataBean.getCompanyId());
        hrService.removeEvent(eventId, loginDataBean.getId());

    }

    public void archiveEvent(Long eventId) {
        EventDataBean eventDataBean = this.retrieveEventById(eventId);
        userManagementServiceWrapper.deleteLocaleForEntity(eventDataBean.getEventTitle(), "Event", "CONTENT", loginDataBean.getId(), loginDataBean.getCompanyId());
        hrService.archiveEvent(eventId, loginDataBean.getId());
    }

    public boolean doesEventNameExist(String eventName, Long eventIdToSkip) {
        return hrService.doesEventNameExist(eventName, loginDataBean.getCompanyId(), eventIdToSkip);
    }

    public Map<Long, String> retrieveRegistrationFormNames() {
        return hrService.retrieveEventFormNames(loginDataBean.getCompanyId());
    }

    public List<EventRegistrationFieldsDataBean> retrieveRegistrationFieldsByEventId(Long eventId) {
        List<HkEventRegistrationFieldEntity> eventRegistrationFieldEntityList = hrService.retrieveRegistrationFieldsByEventId(eventId);
        List<EventRegistrationFieldsDataBean> eventRegistrationFieldsDataBeanList = new ArrayList<>();
        if (eventRegistrationFieldEntityList != null && !eventRegistrationFieldEntityList.isEmpty()) {
            for (HkEventRegistrationFieldEntity eventRegistrationFieldEntity : eventRegistrationFieldEntityList) {
                EventRegistrationFieldsDataBean eventRegistrationFieldsDataBean = new EventRegistrationFieldsDataBean();
                eventRegistrationFieldsDataBean = convertEventRegFieldEntityToEventRegFieldDataBean(eventRegistrationFieldEntity, eventRegistrationFieldsDataBean);
                //Set id to null so that it will be treated as new fields for paricular event
//                eventRegistrationFieldsDataBean.setId(null);
                eventRegistrationFieldsDataBeanList.add(eventRegistrationFieldsDataBean);
            }
        }
        return eventRegistrationFieldsDataBeanList;
    }

    public String uploadFile(String fileName, String fileType) throws FileNotFoundException, IOException {
        String tempFileName = null;
        if (fileType.equals(BANNER)) {
            tempFileName = FolderManagement.getTempFileName(loginDataBean.getCompanyId(), FolderManagement.FEATURE.EVENT, FolderManagement.CONSTANT.BANNER, loginDataBean.getId(), fileName);
//            FolderManagement.storeFileInTemp(tempFileName, file.getBytes(), true);
        } else if (fileType.equals(BACKGROUND)) {
            tempFileName = FolderManagement.getTempFileName(loginDataBean.getCompanyId(), FolderManagement.FEATURE.EVENT, FolderManagement.CONSTANT.BACKGROUND, loginDataBean.getId(), fileName);
//            FolderManagement.storeFileInTemp(tempFileName, file.getBytes(), false);
        } else if (fileType.equals(GALLERY)) {
            tempFileName = FolderManagement.getTempFileName(loginDataBean.getCompanyId(), FolderManagement.FEATURE.EVENT, FolderManagement.CONSTANT.GALLERY, loginDataBean.getId(), fileName);
//        FolderManagement.storeFileInTemp(tempFileName, file.getBytes(), false);
            if (sessionUtil.getFileuploadMap() != null) {
//            System.out.println("in if ............ ");
                sessionUtil.getFileuploadMap().put(tempFileName, fileName);
            } else {
                Map<String, String> fileUploadMap = new HashMap<String, String>();
                fileUploadMap.put(tempFileName, fileName);
                sessionUtil.setFileuploadMap(fileUploadMap);
            }
            return tempFileName;
        }
        return tempFileName;
    }

    public Map<String, List<String>> retrieveImagePaths(Long eventId) throws IOException {
        HkEventEntity eventEntity = hrService.retrieveEventById(eventId, true, true);
        Map<String, List<String>> map = null;
        if (!StringUtils.isEmpty(eventEntity.getFolderName())) {
            map = new LinkedHashMap<>();
            List<String> names = new ArrayList<>();
            List<String> allFilesOfFolder = FolderManagement.getAllFilesOfFolder(eventEntity.getFolderName(), Boolean.FALSE);
            if (!CollectionUtils.isEmpty(allFilesOfFolder)) {
                map.put("src", allFilesOfFolder);
            }
        }
        return map;
    }

    public Map<String, List<String>> retrieveImageThumbnailPaths(Long eventId) throws IOException {
        HkEventEntity eventEntity = hrService.retrieveEventById(eventId, true, true);
        Map<String, List<String>> map = null;
        if (!StringUtils.isEmpty(eventEntity.getFolderName())) {
            map = new LinkedHashMap<>();
            List<String> names = new ArrayList<>();
            List<String> allFilesOfFolder = FolderManagement.getAllFilesOfFolder(eventEntity.getFolderName(), Boolean.TRUE);
            if (!CollectionUtils.isEmpty(allFilesOfFolder)) {
                map.put("src", allFilesOfFolder);
            }
        }
        return map;
    }

    public List<EventDataBean> convertEventEntityListToEventDataBeanList(List<HkEventEntity> eventEntityList, Boolean haveAddEditRights) {
        List<EventDataBean> eventDataBeanList = new ArrayList<>();
        Date todayCal = HkSystemFunctionUtil.getDateWithoutTimeStamp(new Date());
        String name = null;
        String addressString = null;
        if (eventEntityList != null && !eventEntityList.isEmpty()) {
            UMUser user = userService.retrieveUserById(loginDataBean.getId(), loginDataBean.getCompanyId());
            if (user != null) {
                if (!StringUtils.isEmpty(user.getFirstName()) && !StringUtils.isEmpty(user.getLastName())) {
                    name = user.getFirstName() + " " + user.getLastName();
                }
                Set<UMContactAddress> umContactAddressSet = user.getContact().getuMContactAddressSet();
                if (umContactAddressSet != null && !umContactAddressSet.isEmpty()) {
                    for (UMContactAddress uMContactAddress : umContactAddressSet) {
                        addressString = uMContactAddress.getAddressLine1();
                    }
                }
            }
            for (HkEventEntity hkEventEntity : eventEntityList) {
                if (hkEventEntity.getHkEventRecipientEntitySet() != null && !hkEventEntity.getHkEventRecipientEntitySet().isEmpty()) {
                    Set<HkEventRecipientEntity> eventRecipientEntitySet = hkEventEntity.getHkEventRecipientEntitySet();
                    int count = 0;
                    for (HkEventRecipientEntity eventRecipientEntity : eventRecipientEntitySet) {
                        if (eventRecipientEntity.getHkEventRecipientEntityPK().getReferenceInstance() == loginDataBean.getId()
                                && eventRecipientEntity.getHkEventEntity().getPublishedOn().after(todayCal) && !haveAddEditRights) {
                            count++;
                        }
                    }
                    if (count == 0) {
                        EventDataBean eventDataBean = new EventDataBean();
                        eventDataBean = convertEventEntityToEventDataBean(hkEventEntity, eventDataBean, false, false, true, null);
                        eventDataBean.setRecipientAdultCount(hkEventEntity.getAdultCount());
                        eventDataBean.setRecipientChildCount(hkEventEntity.getChildCount());
                        eventDataBean.setEmployeeName(name);
                        eventDataBean.setEmployeeAddress(addressString);
                        if (user.getContact() != null && user.getContact().getEmailAddress() != null) {
                            eventDataBean.setEmployeeEmail(user.getContact().getEmailAddress());
                        }
                        eventDataBean.setEmployeePhoneNo(user.getMobileNumber());
                        eventDataBeanList.add(eventDataBean);
                    }

                }

            }
        }
        return eventDataBeanList;
    }

    public HkEventEntity convertEventDataBeanToEventEntity(Set recipientIdSet, EventDataBean eventDataBean, HkEventEntity eventEntity, boolean editEvent, List<String> recipientCodes) {
        HkEventEntity retrievedEventForRegistration = null;
        if (!editEvent) {
            eventEntity.setCreatedBy(loginDataBean.getId());
            eventEntity.setCreatedOn(Calendar.getInstance().getTime());
            eventEntity.setIsArchive(false);
        }
        eventEntity.setAddress(eventDataBean.getAddress());
        if (eventDataBean.getBannerImageName() != null) {
//            moveTempFileToFolder(eventDataBean.getBannerImageName(), BANNER, true);
            eventEntity.setBannerImageName(eventDataBean.getBannerImageName());
        } else {
            eventEntity.setBannerImageName(null);
        }
        if (eventDataBean.getInvitationTemplateName() != null && !eventDataBean.getInvitationTemplateName().equalsIgnoreCase("ND")) {
            String templateImageName = eventDataBean.getInvitationTemplateName();
            String templateName = templateImageName.substring(0, templateImageName.lastIndexOf("."));
            templateName += ".html";
            eventEntity.setInvitationTemplateName(templateName);
        }
        if (eventDataBean.getCategory() != null) {
            HkCategoryEntity categoryEntity = categoryService.retrieveCategory(eventDataBean.getCategory());
            eventEntity.setCategory(categoryEntity);
        }
        eventEntity.setContentColor(eventDataBean.getContentColor());
        eventEntity.setDescription(eventDataBean.getDescription());
        eventEntity.setEndTime(eventDataBean.getEndTime());
        eventEntity.setEventTitle(eventDataBean.getEventTitle());
        eventEntity.setFranchise(loginDataBean.getCompanyId());
        eventEntity.setFrmDt(HkSystemFunctionUtil.convertToServerDate(eventDataBean.getFromDate(), loginDataBean.getClientRawOffsetInMin()));
        eventEntity.setLabelColor(eventDataBean.getLabelColor());
        eventEntity.setLastModifiedBy(loginDataBean.getId());
        eventEntity.setLastModifiedOn(Calendar.getInstance().getTime());
        eventEntity.setPublishedOn(HkSystemFunctionUtil.convertToServerDate(eventDataBean.getPublishedOn(), loginDataBean.getClientRawOffsetInMin()));
        if (eventDataBean.getRegistrationFormEventId() == null) {
            eventEntity.setRegistrationFormName(eventDataBean.getRegistrationFormName());
        } else {
            retrievedEventForRegistration = hrService.retrieveEventById(eventDataBean.getRegistrationFormEventId(), false, editEvent);
            if (retrievedEventForRegistration != null && !StringUtils.isEmpty(retrievedEventForRegistration.getRegistrationFormName())) {
                eventEntity.setRegistrationFormName(retrievedEventForRegistration.getRegistrationFormName());
            }
        }
        eventEntity.setRegistrationLastDt(HkSystemFunctionUtil.convertToServerDate(eventDataBean.getRegistrationLastDate(), loginDataBean.getClientRawOffsetInMin()));
        eventEntity.setRegistrationType(eventDataBean.getRegistrationType());
        eventEntity.setStrtTime(eventDataBean.getStrtTime());
        if (eventDataBean.getToDate() == null) {
            eventEntity.setToDt(eventEntity.getFrmDt());
        } else {
            eventEntity.setToDt(HkSystemFunctionUtil.convertToServerDate(eventDataBean.getToDate(), loginDataBean.getClientRawOffsetInMin()));
        }
        List<EventRecipientDataBean> eventRecipientList = eventDataBean.getEventRecipientDataBeanList();
        Set<HkEventRecipientEntity> eventRecipientEntitySet = new HashSet();
        if (eventRecipientList != null && !eventRecipientList.isEmpty()) {
            List<Long> otherDepts = new LinkedList<>();
            for (EventRecipientDataBean eventRecipientDataBean : eventRecipientList) {
                HkEventRecipientEntity eventRecipientEntity;

                if (!editEvent) {
                    HkEventRecipientEntityPK hkEventRecipientEntityPK = new HkEventRecipientEntityPK();
                    hkEventRecipientEntityPK.setReferenceInstance(eventRecipientDataBean.getRecipientInstance());
                    hkEventRecipientEntityPK.setReferenceType(eventRecipientDataBean.getRecipientType());
                    eventRecipientEntity = new HkEventRecipientEntity();
                    eventRecipientEntity.setHkEventRecipientEntityPK(hkEventRecipientEntityPK);
                    eventRecipientEntity.setFranchise(loginDataBean.getCompanyId());
                    eventRecipientEntity.setIsArchive(Boolean.FALSE);
                    eventRecipientEntity.setHkEventEntity(eventEntity);
                } else {
                    eventRecipientEntity = new HkEventRecipientEntity(eventEntity.getId(), eventRecipientDataBean.getRecipientType(), eventRecipientDataBean.getRecipientInstance());
                    eventRecipientEntity.setFranchise(loginDataBean.getCompanyId());
                    recipientCodes.add(eventRecipientDataBean.getRecipientInstance() + SEPARATOR + eventRecipientDataBean.getRecipientType());
                }

                if (eventRecipientDataBean.getRecipientType().equalsIgnoreCase(HkSystemConstantUtil.RecipientCodeType.FRANCHISE_DEPARTMENT)) {
                    otherDepts.add(eventRecipientDataBean.getRecipientInstance());
                    List<UMUser> usersByDepartmentId = userManagementServiceWrapper.retrieveUsersByCompanyByDepartment(null, eventRecipientDataBean.getRecipientInstance(), true, null);
                    if (!CollectionUtils.isEmpty(usersByDepartmentId)) {
                        for (UMUser uMUser : usersByDepartmentId) {
                            recipientIdSet.add(uMUser.getId());
                        }
                    }
                } else if (eventRecipientDataBean.getRecipientType().equalsIgnoreCase(HkSystemConstantUtil.RecipientCodeType.FRANCHISE)) {
                    List<UMUser> usersByFranchiseId = userManagementServiceWrapper.retrieveUsersByCompany(eventRecipientDataBean.getRecipientInstance(), true, true, loginDataBean.getId());
                    if (!CollectionUtils.isEmpty(usersByFranchiseId)) {
                        for (UMUser uMUser : usersByFranchiseId) {
                            recipientIdSet.add(uMUser.getId());
                        }
                    }
                } else if (eventRecipientDataBean.getRecipientType().equalsIgnoreCase(HkSystemConstantUtil.RecipientCodeType.EMPLOYEE)) {
                    recipientIdSet.add(eventRecipientDataBean.getRecipientInstance());
                } else if (eventRecipientDataBean.getRecipientType().equalsIgnoreCase(HkSystemConstantUtil.RecipientCodeType.DEPARTMENT)) {
                    List<UMUser> usersByDepartmentId = userManagementServiceWrapper.retrieveUsersByCompanyByDepartment(loginDataBean.getCompanyId(), eventRecipientDataBean.getRecipientInstance(), true, loginDataBean.getId());
                    if (!CollectionUtils.isEmpty(usersByDepartmentId)) {
                        for (UMUser uMUser : usersByDepartmentId) {
                            recipientIdSet.add(uMUser.getId());
                        }
                    }
                } else if (eventRecipientDataBean.getRecipientType().equalsIgnoreCase(HkSystemConstantUtil.RecipientCodeType.GROUP)) {
                    List<UMGroupContact> groupContacts = userManagementServiceWrapper.retrieveGroupContacts(eventRecipientDataBean.getRecipientInstance(), true);
                    if (!CollectionUtils.isEmpty(groupContacts)) {
                        for (UMGroupContact groupContact : groupContacts) {
                            recipientIdSet.add(groupContact.getUMUserContact().getUserobj().getId());
                        }
                    }
                } else if (eventRecipientDataBean.getRecipientType().equalsIgnoreCase(HkSystemConstantUtil.RecipientCodeType.ACTIVITY)) {
                } else if (eventRecipientDataBean.getRecipientType().equalsIgnoreCase(HkSystemConstantUtil.RecipientCodeType.DESIGNATION)) {
                    List<UMUserRole> retrieveUserRoleByRoleId = userManagementServiceWrapper.retrieveUserRolesByRoleId(eventRecipientDataBean.getRecipientInstance(), true);
                    if (!CollectionUtils.isEmpty(retrieveUserRoleByRoleId)) {
                        for (UMUserRole uMUserRole : retrieveUserRoleByRoleId) {
                            recipientIdSet.add(uMUserRole.getuMUser().getId());
                        }
                    }
                }
                eventRecipientEntitySet.add(eventRecipientEntity);
            }

            if (!CollectionUtils.isEmpty(otherDepts) && !CollectionUtils.isEmpty(eventRecipientEntitySet)) {
                Map<Long, UMDepartment> departmentMap = userManagementServiceWrapper.retrieveDepartmentMapByIds(otherDepts, true);
                if (!CollectionUtils.isEmpty(departmentMap)) {
                    for (HkEventRecipientEntity eventRecipientEntity : eventRecipientEntitySet) {
                        if (departmentMap.containsKey(eventRecipientEntity.getHkEventRecipientEntityPK().getReferenceInstance())) {
                            eventRecipientEntity.setFranchise(departmentMap.get(eventRecipientEntity.getHkEventRecipientEntityPK().getReferenceInstance()).getCompany());
                        }
                    }
                }
            }
        }
        if (editEvent) {
            eventEntity.setStatus(HkSystemFunctionUtil.getKeyByValue(HkSystemConstantUtil.EVENT_STATUS_MAP, eventDataBean.getStatus()));
        }
        eventEntity.setHkEventRecipientEntitySet(eventRecipientEntitySet);

        //Set registration fields 
        if (eventDataBean.getRegistrationFormEventId() == null) {
            List<EventRegistrationFieldsDataBean> registrationFieldsDataBeanList = eventDataBean.getRegistrationFieldsDataBean();
            if (registrationFieldsDataBeanList != null && !registrationFieldsDataBeanList.isEmpty()) {
                Set<HkEventRegistrationFieldEntity> eventRegistrationFieldEntitySet;

                eventRegistrationFieldEntitySet = new HashSet<>();

                for (EventRegistrationFieldsDataBean eventRegistrationFieldsDataBean : registrationFieldsDataBeanList) {
                    HkEventRegistrationFieldEntity eventRegistrationFieldEntity = new HkEventRegistrationFieldEntity();
                    eventRegistrationFieldEntity = convertRegistrationFiedlDataBeanToRegistrationFieldEntity(eventRegistrationFieldsDataBean, eventRegistrationFieldEntity);
                    eventRegistrationFieldEntity.setEvent(eventEntity);
                    eventRegistrationFieldEntity.setIsArchive(Boolean.FALSE);
                    eventRegistrationFieldEntitySet.add(eventRegistrationFieldEntity);
                }
                eventEntity.setHkEventRegistrationFieldEntitySet(eventRegistrationFieldEntitySet);
            }
        } else {
            List<HkEventRegistrationFieldEntity> registrationFieldsDataBeanList = hrService.retrieveRegistrationFieldsByEventId(eventDataBean.getRegistrationFormEventId());
            if (!CollectionUtils.isEmpty(registrationFieldsDataBeanList)) {
                Set<HkEventRegistrationFieldEntity> eventRegistrationFieldEntitySet;
                eventRegistrationFieldEntitySet = new HashSet<>();
                for (HkEventRegistrationFieldEntity hkEventRegistrationFieldEntitys : registrationFieldsDataBeanList) {
                    HkEventRegistrationFieldEntity eventRegistrationFieldEntity = new HkEventRegistrationFieldEntity();
                    eventRegistrationFieldEntity.setComponentType(HkSystemConstantUtil.CustomField.ComponentType.TEXT_FIELD);
                    eventRegistrationFieldEntity.setFieldName(hkEventRegistrationFieldEntitys.getFieldName());
                    eventRegistrationFieldEntity.setValidationPattern(hkEventRegistrationFieldEntitys.getValidationPattern());
                    eventRegistrationFieldEntity.setLastModifiedBy(loginDataBean.getId());
                    eventRegistrationFieldEntity.setLastModifiedOn(Calendar.getInstance().getTime());
                    eventRegistrationFieldEntity.setEvent(eventEntity);
                    eventRegistrationFieldEntity.setIsArchive(Boolean.FALSE);
                    eventRegistrationFieldEntitySet.add(eventRegistrationFieldEntity);
                }
                eventEntity.setHkEventRegistrationFieldEntitySet(eventRegistrationFieldEntitySet);
            }
        }
        return eventEntity;
    }

    public HkEventRegistrationFieldEntity convertRegistrationFiedlDataBeanToRegistrationFieldEntity(EventRegistrationFieldsDataBean eventRegistrationFieldsDataBean, HkEventRegistrationFieldEntity eventRegistrationFieldEntity) {
//        eventRegistrationFieldEntity.setId(eventRegistrationFieldsDataBean.getId());
        eventRegistrationFieldEntity.setComponentType(HkSystemConstantUtil.CustomField.ComponentType.TEXT_FIELD);
        eventRegistrationFieldEntity.setFieldName(eventRegistrationFieldsDataBean.getFieldName());
        eventRegistrationFieldEntity.setValidationPattern(eventRegistrationFieldsDataBean.getValidationPattern());
        eventRegistrationFieldEntity.setLastModifiedBy(loginDataBean.getId());
        eventRegistrationFieldEntity.setLastModifiedOn(Calendar.getInstance().getTime());
        return eventRegistrationFieldEntity;
    }

    public EventDataBean convertEventEntityToEventDataBean(HkEventEntity eventEntity, EventDataBean eventDataBean, boolean recipientsRequired, boolean registrationFieldsRequired, boolean registrationUserDetail, Map<HkSystemConstantUtil.SectionNameForCustomField, List<Map<Long, Map<String, Object>>>> retrieveDocumentByInstanceId) {
        eventDataBean.setAddress(eventEntity.getAddress());
        eventDataBean.setAdultCount(eventEntity.getAdultCount());
        eventDataBean.setUserAdultCount(0);
        eventDataBean.setUserChildCount(0);
        if (eventEntity.getBannerImageName() != null) {
            eventDataBean.setBannerImageName((eventEntity.getBannerImageName()));
        }
        if (eventEntity.getCategory() != null) {
            eventDataBean.setCategory(eventEntity.getCategory().getId());
            eventDataBean.setCategoryDataBean(convertCategoryEntityToCategoryDataBean(eventEntity.getCategory(), new CategoryDataBean()));
        }
        eventDataBean.setCreatedBy(eventEntity.getCreatedBy());
        eventDataBean.setFranchise(eventEntity.getFranchise());
        eventDataBean.setChildCount(eventEntity.getChildCount());
        eventDataBean.setContentColor(eventEntity.getContentColor());
        eventDataBean.setDescription(eventEntity.getDescription());
        if (!StringUtils.isEmpty(eventEntity.getFolderName())) {
            eventDataBean.setFolderName(eventEntity.getFolderName());
        } else {
            eventDataBean.setFolderName(null);
        }
        eventDataBean.setEndTime(eventEntity.getEndTime());
        eventDataBean.setEventTitle(eventEntity.getEventTitle());
        eventDataBean.setFromDate(HkSystemFunctionUtil.convertToClientDate(eventEntity.getFrmDt(), loginDataBean.getClientRawOffsetInMin()));
        eventDataBean.setGuestCount(eventEntity.getGuestCount());
        eventDataBean.setId(eventEntity.getId());
        if (eventEntity.getInvitationTemplateName() != null) {
            eventDataBean.setInvitationTemplateName(eventEntity.getInvitationTemplateName());
            eventDataBean.setInvitationTemplatePath((eventEntity.getInvitationTemplateName()));
        }
        eventDataBean.setLabelColor(eventEntity.getLabelColor());
        eventDataBean.setNotAttendingCount(eventEntity.getNotAttendingCount());
        eventDataBean.setPublishedOn(HkSystemFunctionUtil.convertToClientDate(eventEntity.getPublishedOn(), loginDataBean.getClientRawOffsetInMin()));
        eventDataBean.setRegistrationCount(eventEntity.getRegistrationCount());
        eventDataBean.setRegistrationFormName(eventEntity.getRegistrationFormName());
        eventDataBean.setRegistrationLastDate(HkSystemFunctionUtil.convertToClientDate(eventEntity.getRegistrationLastDt(), loginDataBean.getClientRawOffsetInMin()));
        eventDataBean.setRegistrationType(eventEntity.getRegistrationType());
        eventDataBean.setStatus(HkSystemConstantUtil.EVENT_STATUS_MAP.get(eventEntity.getStatus()));
        eventDataBean.setStrtTime(eventEntity.getStrtTime());
        eventDataBean.setToDate(HkSystemFunctionUtil.convertToClientDate(eventEntity.getToDt(), loginDataBean.getClientRawOffsetInMin()));

        if (eventEntity.getHkEventRecipientEntitySet() != null && !eventEntity.getHkEventRecipientEntitySet().isEmpty()) {
            Set<HkEventRecipientEntity> eventRecipientEntitySet = eventEntity.getHkEventRecipientEntitySet();
            List<String> recipientCodes = new ArrayList<>();
            for (HkEventRecipientEntity eventRecipientEntity : eventRecipientEntitySet) {
                if (!eventRecipientEntity.getIsArchive()) {
                    recipientCodes.add(eventRecipientEntity.getHkEventRecipientEntityPK().getReferenceInstance() + SEPARATOR + eventRecipientEntity.getHkEventRecipientEntityPK().getReferenceType());
                }
            }

            Set<Long> inviteeIdsSet = userManagementServiceWrapper.retrieveRecipientIds(recipientCodes);

            eventDataBean.setEmployeeCount(inviteeIdsSet.size());
            if (inviteeIdsSet != null && !inviteeIdsSet.isEmpty()) {
                for (Long userId : inviteeIdsSet) {
                    if (loginDataBean.getId().equals(userId)) {
                        eventDataBean.setEnableRegister(Boolean.TRUE);
                    }
                }
            }

            if (recipientsRequired) {
                Map<String, String> inviteeNamesMap = userManagementServiceWrapper.retrieveRecipientNames(recipientCodes);
                List<EventRecipientDataBean> eventRecipientDataBeanList = new ArrayList<>();
                for (HkEventRecipientEntity eventRecipientEntity : eventRecipientEntitySet) {
                    EventRecipientDataBean eventRecipientDataBean = new EventRecipientDataBean();
                    eventRecipientDataBean.setRecipientInstance(eventRecipientEntity.getHkEventRecipientEntityPK().getReferenceInstance());
                    eventRecipientDataBean.setRecipientType(eventRecipientEntity.getHkEventRecipientEntityPK().getReferenceType());
                    eventRecipientDataBean.setRecipientValue(inviteeNamesMap.get(eventRecipientDataBean.getRecipientInstance() + SEPARATOR + eventRecipientDataBean.getRecipientType()));
                    eventRecipientDataBeanList.add(eventRecipientDataBean);
                }
                eventDataBean.setEventRecipientDataBeanList(eventRecipientDataBeanList);
            }
        }
        //convert registrationFieldEntityToRegistrationFieldsDatabean if required
        if (registrationFieldsRequired) {
            Set<HkEventRegistrationFieldEntity> eventRegistrationFieldEntitySet = eventEntity.getHkEventRegistrationFieldEntitySet();
            if (eventRegistrationFieldEntitySet != null && !eventRegistrationFieldEntitySet.isEmpty()) {
                List<EventRegistrationFieldsDataBean> eventRegistrationFieldsDataBeanList = new ArrayList<>();
                for (HkEventRegistrationFieldEntity eventRegistrationFieldEntity : eventRegistrationFieldEntitySet) {
                    EventRegistrationFieldsDataBean eventRegistrationFieldsDataBean = new EventRegistrationFieldsDataBean();
                    eventRegistrationFieldsDataBean = convertEventRegFieldEntityToEventRegFieldDataBean(eventRegistrationFieldEntity, eventRegistrationFieldsDataBean);
                    eventRegistrationFieldsDataBeanList.add(eventRegistrationFieldsDataBean);
                }
                eventDataBean.setRegistrationFieldsDataBean(eventRegistrationFieldsDataBeanList);
            }

        }
        if (registrationUserDetail) {
            List<HkEventRegistrationEntity> eventRegistrationEntitys = hrService.retrieveUserEventRegistrationEntities(eventEntity.getId(), loginDataBean.getId(), Boolean.FALSE);

            if (eventRegistrationEntitys != null && !eventRegistrationEntitys.isEmpty()) {
                for (HkEventRegistrationEntity eventRegistrationEntity : eventRegistrationEntitys) {

                    if (eventRegistrationEntity.getStatus().equals(HkSystemConstantUtil.ACTIVE)) {
//                    convertRegistartionModelToDataBean(here, eventDataBean);
                        eventDataBean.setRegistrationStatus(null);
                    } else {
                        boolean isattending = eventRegistrationEntity.getStatus().equals(HkSystemConstantUtil.EventUserRegistrationStatus.ATTENDING);
                        convertRegistartionModelToDataBean(eventRegistrationEntity, eventDataBean);
                        eventDataBean.setRegistrationStatus(isattending);
                    }
                }
            }

        }

        List<HkEventRegistrationEntity> retrieveUserEventRegistrationEntities = hrService.retrieveUserEventRegistrationEntities(eventEntity.getId(), null, Boolean.FALSE);
        if (!CollectionUtils.isEmpty(retrieveUserEventRegistrationEntities) && eventDataBean.getEmployeeCount() != null) {
            eventDataBean.setPendingCount(eventDataBean.getEmployeeCount() - retrieveUserEventRegistrationEntities.size());
        } else {
            if (eventDataBean.getEmployeeCount() != null) {
                eventDataBean.setPendingCount(eventDataBean.getEmployeeCount());
            } else {
                eventDataBean.setPendingCount(0);
            }
        }

        if (retrieveDocumentByInstanceId != null) {
            List<Map<Long, Map<String, Object>>> maps = retrieveDocumentByInstanceId.get(HkSystemConstantUtil.SectionNameForCustomField.GENERAL);
            if (maps != null) {
                for (Map<Long, Map<String, Object>> map : maps) {
                    eventDataBean.setEventCustom(map.get(eventEntity.getId()));
                }
            }
            List<Map<Long, Map<String, Object>>> regmaps = retrieveDocumentByInstanceId.get(HkSystemConstantUtil.SectionNameForCustomField.REGISTRATION);
            if (regmaps != null) {
                for (Map<Long, Map<String, Object>> map : regmaps) {
                    eventDataBean.setRegCustom(map.get(eventEntity.getId()));
                }
            }
            List<Map<Long, Map<String, Object>>> invimaps = retrieveDocumentByInstanceId.get(HkSystemConstantUtil.SectionNameForCustomField.INVITATIONCARD);
            if (invimaps != null) {
                for (Map<Long, Map<String, Object>> map : invimaps) {
                    eventDataBean.setInvitationCustom(map.get(eventEntity.getId()));
                }
            }
        }
        return eventDataBean;

    }

    private EventRegistrationFieldsDataBean convertEventRegFieldEntityToEventRegFieldDataBean(HkEventRegistrationFieldEntity eventRegistrationFieldEntity, EventRegistrationFieldsDataBean eventRegistrationFieldsDataBean) {
        eventRegistrationFieldsDataBean.setId(eventRegistrationFieldEntity.getId());
        eventRegistrationFieldsDataBean.setComponentType(eventRegistrationFieldEntity.getComponentType());
        eventRegistrationFieldsDataBean.setFieldName(eventRegistrationFieldEntity.getFieldName());
        eventRegistrationFieldsDataBean.setValidationPattern(eventRegistrationFieldEntity.getValidationPattern());
        eventRegistrationFieldsDataBean.setDefaultValues(eventRegistrationFieldEntity.getDefaultValues());
        return eventRegistrationFieldsDataBean;
    }

    private CategoryDataBean convertCategoryEntityToCategoryDataBean(HkCategoryEntity hkCategoryEntity, CategoryDataBean hkCategoryDataBean) {
        if (hkCategoryDataBean == null) {
            hkCategoryDataBean = new CategoryDataBean();
        }
        hkCategoryDataBean.setId(hkCategoryEntity.getId());
        hkCategoryDataBean.setDescription(hkCategoryEntity.getDescription());
        hkCategoryDataBean.setDisplayName(hkCategoryEntity.getCategoryTitle());
        hkCategoryDataBean.setCompanyId(hkCategoryEntity.getFranchise());
        if (hkCategoryEntity.getParent() == null) {
            hkCategoryDataBean.setParentId(0L);
            hkCategoryDataBean.setParentName(HkSystemConstantUtil.NONE);
        } else {
            hkCategoryDataBean.setParentId(hkCategoryEntity.getParent().getId());
            hkCategoryDataBean.setParentName(hkCategoryEntity.getParent().getCategoryTitle());
        }
        hkCategoryDataBean.setStartIndex(hkCategoryEntity.getCurrentIndex().toString());
        hkCategoryDataBean.setCategoryPrefix(hkCategoryEntity.getCategoryPrefix());
        if (hkCategoryEntity.getIsActive()) {
            hkCategoryDataBean.setStatus("Active");
        } else {
            hkCategoryDataBean.setStatus("Remove");
        }
        hkCategoryDataBean.setType(hkCategoryEntity.getCategoryType());
        hkCategoryDataBean.setHaveDiamondProcessingMch(hkCategoryEntity.getHaveDiamondProcessingMch());
        return hkCategoryDataBean;
    }

    public List<EventDataBean> retrieveEventsforUser() {
        Long id = loginDataBean.getId();
        List<String> statusList = new ArrayList<>();
        statusList.add(HkSystemConstantUtil.EventStatus.ONGOING);
        statusList.add(HkSystemConstantUtil.EventStatus.UPCOMING);
        List<HkEventEntity> eventList = hrService.retrieveEventsByCriteria(statusList, null, Boolean.FALSE, true, true, false);
        List<EventDataBean> eventDataBeans = new ArrayList<>();
        Map<Long, EventDataBean> eventsMap = new HashMap<>();
        if (eventList != null && !eventList.isEmpty()) {
            for (HkEventEntity hkEventEntity : eventList) {
                EventDataBean eventDataBean = new EventDataBean();
                eventDataBean = convertEventEntityToEventDataBean(hkEventEntity, eventDataBean, false, true, false, null);

                eventsMap.put(eventDataBean.getId(), eventDataBean);
                eventDataBeans.add(eventDataBean);
            }
        }

        List<HkEventRegistrationEntity> eventRegistrationEntitys = hrService.retrieveUserEventRegistrationEntities(null, id, Boolean.FALSE);

        if (eventRegistrationEntitys != null && !eventRegistrationEntitys.isEmpty()) {
            for (HkEventRegistrationEntity here : eventRegistrationEntitys) {

                EventDataBean eventDataBean = eventsMap.get(here.getHkEventRegistrationEntityPK().getEvent());
                if (here.getStatus().equals(HkSystemConstantUtil.ACTIVE)) {
//                    convertRegistartionModelToDataBean(here, eventDataBean);
                    eventDataBean.setRegistrationStatus(null);
                } else {

                    boolean isattending = here.getStatus().equals(HkSystemConstantUtil.EventUserRegistrationStatus.ATTENDING);
                    convertRegistartionModelToDataBean(here, eventDataBean);
                    eventDataBean.setRegistrationStatus(isattending);
                }
            }
        }

        return eventDataBeans;
    }

    private void convertRegistartionModelToDataBean(HkEventRegistrationEntity eventRegistrationEntity, EventDataBean eventDataBean) {

        eventDataBean.setAdultCount(eventRegistrationEntity.getAdultCount());
        eventDataBean.setChildCount(eventRegistrationEntity.getChildCount());
        eventDataBean.setUserAdultCount(eventRegistrationEntity.getAdultCount());
        eventDataBean.setUserChildCount(eventRegistrationEntity.getChildCount());
        eventDataBean.setGuestCount(eventRegistrationEntity.getGuestCount());
        if (eventRegistrationEntity.getStatus().equals(HkSystemConstantUtil.EventUserRegistrationStatus.NOT_ATTENDING)) {
            eventDataBean.setReason(eventRegistrationEntity.getReason());
        }
        List<GuestDataBean> guests = new ArrayList<>();
        String guestInfo = eventRegistrationEntity.getGuestInfo();
        StringTokenizer st = new StringTokenizer(guestInfo, ",");
        while (st.hasMoreTokens()) {
            String guest = st.nextToken();

            StringTokenizer st1 = new StringTokenizer(guest, "|");
            GuestDataBean gdb = new GuestDataBean();

            gdb.setName(st1.nextToken());
            gdb.setRelation(st1.nextToken());

            guests.add(gdb);
        }
        eventDataBean.setGuests(guests);

    }

    public void registerForEvent(EventDataBean eventDataBean) {
        HkEventRegistrationEntity eventRegistrationModel = convertEventRegistrationDataBeanToModel(eventDataBean, new HkEventRegistrationEntity());
        Map<Long, String> fieldMap = new HashMap<>();
        List<EventRegistrationFieldsDataBean> registrationFieldsDataBeans = eventDataBean.getRegistrationFieldsDataBean();
        if (registrationFieldsDataBeans != null && registrationFieldsDataBeans.isEmpty() == false) {
            for (EventRegistrationFieldsDataBean erfdb : registrationFieldsDataBeans) {
                fieldMap.put(erfdb.getId(), erfdb.getValue());
            }
        }
//        if (eventRegistrationModel.getStatus() == null) {
//            eventRegistrationModel.setStatus(HkSystemConstantUtil.EventUserRegistrationStatus.ATTENDING);
//        }
        hrService.registerForEvent(eventRegistrationModel, fieldMap);
    }

    public void editRegistrationForEvent(EventDataBean eventDataBean) {
        HkEventRegistrationEntity eventRegistrationModel = convertEventRegistrationDataBeanToModel(eventDataBean, new HkEventRegistrationEntity());
        Map<Long, String> fieldMap = new HashMap<>();
        List<EventRegistrationFieldsDataBean> registrationFieldsDataBeans = eventDataBean.getRegistrationFieldsDataBean();
        if (registrationFieldsDataBeans != null && registrationFieldsDataBeans.isEmpty() == false) {
            for (EventRegistrationFieldsDataBean erfdb : registrationFieldsDataBeans) {
                fieldMap.put(erfdb.getId(), erfdb.getValue());
            }
        }
        hrService.editEventRegistration(eventRegistrationModel, fieldMap);
    }

    private HkEventRegistrationEntity convertEventRegistrationDataBeanToModel(EventDataBean registrationDataBean, HkEventRegistrationEntity registrationEntity) {
        if (registrationEntity == null) {
            registrationEntity = new HkEventRegistrationEntity();
        }
        HkEventRegistrationEntityPK eventRegistrationEntityPK = new HkEventRegistrationEntityPK(registrationDataBean.getId(), loginDataBean.getId());
        registrationEntity.setHkEventRegistrationEntityPK(eventRegistrationEntityPK);
        if (registrationDataBean.getAdultCount() != null && registrationDataBean.getChildCount() != null) {
            registrationEntity.setAdultCount(registrationDataBean.getAdultCount());

            registrationEntity.setChildCount(registrationDataBean.getChildCount());
        }
        if (registrationDataBean.isIsAttending() == true) {
            registrationEntity.setAdultCount(0);
            registrationEntity.setChildCount(0);
            registrationEntity.setGuestCount(0);
            registrationEntity.setReason(registrationDataBean.getReason());
            registrationEntity.setStatus(HkSystemConstantUtil.EventUserRegistrationStatus.NOT_ATTENDING);
        } else {
            registrationEntity.setStatus(HkSystemConstantUtil.EventUserRegistrationStatus.ATTENDING);
        }
        if (registrationDataBean.getGuestCount() != null) {
            registrationEntity.setGuestCount(registrationDataBean.getGuestCount());
        }

        registrationEntity.setLastModifiedOn(new Date());

        StringBuffer guestInfo = new StringBuffer("");
        List<GuestDataBean> guests = registrationDataBean.getGuests();
        if (guests != null && !guests.isEmpty() && !registrationDataBean.isIsAttending()) {
            for (GuestDataBean gdb : guests) {
                guestInfo.append(gdb.getName()).append("|").append(gdb.getRelation()).append(",");
            }
            guestInfo.replace(guestInfo.lastIndexOf(","), guestInfo.length(), "");
        } else {
            registrationEntity.setGuestCount(0);
        }
        registrationEntity.setGuestInfo(guestInfo.toString());
        return registrationEntity;
    }

    public List<EventRegistrationFieldsDataBean> retrieveCustomValues(Long eventId) {
        List<HkEventRegistrationFieldValueEntity> eventRegistrationFieldValues = hrService.retrieveEventRegistrationFieldValues(eventId, loginDataBean.getId());
        List<EventRegistrationFieldsDataBean> fieldsDataBeans = new ArrayList<>();
        if (eventRegistrationFieldValues != null && !eventRegistrationFieldValues.isEmpty()) {
            for (HkEventRegistrationFieldValueEntity herfve : eventRegistrationFieldValues) {
                EventRegistrationFieldsDataBean registrationFieldsDataBean = new EventRegistrationFieldsDataBean();

                registrationFieldsDataBean.setValue(herfve.getFieldValue());
                convertEventRegFieldEntityToEventRegFieldDataBean(herfve.getHkEventRegistrationFieldEntity(), registrationFieldsDataBean);

                fieldsDataBeans.add(registrationFieldsDataBean);
            }
        }
        return fieldsDataBeans;
    }

    public List<EventRegistrationFieldsDataBean> retrieveCustomValuesByUser(Long eventId, Long userId) {
        List<HkEventRegistrationFieldValueEntity> eventRegistrationFieldValues = hrService.retrieveEventRegistrationFieldValues(eventId, userId);
        List<EventRegistrationFieldsDataBean> fieldsDataBeans = new ArrayList<>();
        if (eventRegistrationFieldValues != null && !eventRegistrationFieldValues.isEmpty()) {
            for (HkEventRegistrationFieldValueEntity herfve : eventRegistrationFieldValues) {
                EventRegistrationFieldsDataBean registrationFieldsDataBean = new EventRegistrationFieldsDataBean();

                registrationFieldsDataBean.setValue(herfve.getFieldValue());
                convertEventRegFieldEntityToEventRegFieldDataBean(herfve.getHkEventRegistrationFieldEntity(), registrationFieldsDataBean);

                fieldsDataBeans.add(registrationFieldsDataBean);
            }
        }
        return fieldsDataBeans;
    }

    public void cancelregistration(Long eventId) {
        hrService.cancelEventRegistration(eventId, loginDataBean.getId());
    }

    public List<EventUserRegistrationDataBean> retrieveUserRegistrationEntities(Long eventId) throws GenericDatabaseException {
        HkEventEntity retrieveEventById = hrService.retrieveEventById(eventId, true, false);
        Set<HkEventRecipientEntity> hkEventRecipientEntitySet = retrieveEventById.getHkEventRecipientEntitySet();
        List<String> recipientCodes = new ArrayList<>();
        List<Long> userIds = new ArrayList<>();
        List<Long> iniviteeList = new ArrayList<>();
        String name = null;
        List<EventUserRegistrationDataBean> eventRegistrationDataBeanList = new ArrayList<>();
        for (HkEventRecipientEntity eventRecipientEntity : hkEventRecipientEntitySet) {
            recipientCodes.add(eventRecipientEntity.getHkEventRecipientEntityPK().getReferenceInstance() + SEPARATOR + eventRecipientEntity.getHkEventRecipientEntityPK().getReferenceType());
        }
        Map<Long, String> retrievedUsersMap = new HashMap<>();
        Set<Long> inviteeIdsSet = userManagementServiceWrapper.retrieveRecipientIds(recipientCodes);
        if (inviteeIdsSet != null && !inviteeIdsSet.isEmpty()) {
            iniviteeList.addAll(inviteeIdsSet);
            retrievedUsersMap = userManagementServiceWrapper.retrieveUserNamesByIds(iniviteeList);
            List<HkEventRegistrationEntity> retrieveUserEventRegistrationEntities = hrService.retrieveUserEventRegistrationEntities(eventId, null, Boolean.FALSE);
            if (!CollectionUtils.isEmpty(retrieveUserEventRegistrationEntities)) {
                for (HkEventRegistrationEntity hkEventRegistrationEntity : retrieveUserEventRegistrationEntities) {
                    userIds.add(hkEventRegistrationEntity.getHkEventRegistrationEntityPK().getUserId());
                }
                if (!CollectionUtils.isEmpty(userIds)) {
                    for (HkEventRegistrationEntity hkEventRegistrationEntity : retrieveUserEventRegistrationEntities) {
                        EventUserRegistrationDataBean eventUserRegistrationDataBean = new EventUserRegistrationDataBean();
                        eventUserRegistrationDataBean.setAdultCount(hkEventRegistrationEntity.getAdultCount());
                        eventUserRegistrationDataBean.setChildCount(hkEventRegistrationEntity.getChildCount());
                        eventUserRegistrationDataBean.setEmpName(retrievedUsersMap.get(hkEventRegistrationEntity.getHkEventRegistrationEntityPK().getUserId()));
                        eventUserRegistrationDataBean.setEventId(hkEventRegistrationEntity.getHkEventRegistrationEntityPK().getEvent());
                        eventUserRegistrationDataBean.setGuestCount(hkEventRegistrationEntity.getGuestCount());
                        eventUserRegistrationDataBean.setUserId(hkEventRegistrationEntity.getHkEventRegistrationEntityPK().getUserId());
                        eventUserRegistrationDataBean.setStatus(hkEventRegistrationEntity.getStatus());
                        eventUserRegistrationDataBean.setReason(hkEventRegistrationEntity.getReason());
                        List<GuestDataBean> guests = new ArrayList<>();
                        String guestInfo = hkEventRegistrationEntity.getGuestInfo();
                        StringTokenizer st = new StringTokenizer(guestInfo, ",");
                        while (st.hasMoreTokens()) {
                            String guest = st.nextToken();

                            StringTokenizer st1 = new StringTokenizer(guest, "|");
                            GuestDataBean gdb = new GuestDataBean();

                            gdb.setName(st1.nextToken());
                            gdb.setRelation(st1.nextToken());

                            guests.add(gdb);
                        }
                        eventUserRegistrationDataBean.setGuests(guests);
                        eventRegistrationDataBeanList.add(eventUserRegistrationDataBean);
                    }
                }

            }
            for (Long invitee : iniviteeList) {
                if (!CollectionUtils.isEmpty(userIds)) {
                    if (!userIds.contains(invitee)) {
                        EventUserRegistrationDataBean eventUserRegistrationDataBean = new EventUserRegistrationDataBean();
                        eventUserRegistrationDataBean.setAdultCount(0);
                        eventUserRegistrationDataBean.setChildCount(0);
                        eventUserRegistrationDataBean.setEmpName(retrievedUsersMap.get(invitee));
                        eventUserRegistrationDataBean.setEventId(eventId);
                        eventUserRegistrationDataBean.setGuestCount(0);
                        eventUserRegistrationDataBean.setUserId(invitee);
                        eventUserRegistrationDataBean.setStatus(null);
                        eventRegistrationDataBeanList.add(eventUserRegistrationDataBean);
                        eventUserRegistrationDataBean.setReason(null);
                    }
                } else {
                    EventUserRegistrationDataBean eventUserRegistrationDataBean = new EventUserRegistrationDataBean();
                    eventUserRegistrationDataBean.setAdultCount(0);
                    eventUserRegistrationDataBean.setChildCount(0);
                    eventUserRegistrationDataBean.setEmpName(retrievedUsersMap.get(invitee));
                    eventUserRegistrationDataBean.setEventId(eventId);
                    eventUserRegistrationDataBean.setGuestCount(0);
                    eventUserRegistrationDataBean.setUserId(invitee);
                    eventUserRegistrationDataBean.setStatus(null);
                    eventRegistrationDataBeanList.add(eventUserRegistrationDataBean);
                    eventUserRegistrationDataBean.setReason(null);
                }

            }
        }

        return eventRegistrationDataBeanList;
    }

    public String generatePdf(Long eventId, boolean isAttendingListOnly) throws GenericDatabaseException {
        HkEventEntity retrieveEventById = hrService.retrieveEventById(eventId, true, false);
        Set<HkEventRecipientEntity> hkEventRecipientEntitySet = retrieveEventById.getHkEventRecipientEntitySet();
        List<String> recipientCodes = new ArrayList<>();
        List<Long> userIds = new ArrayList<>();
        List<String> printedPdfUserId = new ArrayList<>();
        List<Long> iniviteeList = new ArrayList<>();
        String pathOfTemplate = FolderManagement.getPathOfImage(retrieveEventById.getInvitationTemplateName());
        String onlyPathOFTemplate = FolderManagement.getOnlyPathOfFile(retrieveEventById.getInvitationTemplateName());
        String content = null;
        File tempDir = FolderManagement.checkIsExists(FolderManagement.basePath, FolderManagement.TEMP, null);
        StringBuilder filePath = new StringBuilder(tempDir.getPath());
//        System.out.println("filePath : " + filePath);
        filePath.append(File.separator);
        try {

            content = new String(Files.readAllBytes(Paths.get(pathOfTemplate)));
            content = content.replaceAll("<div id=\"templateImg\">"
                    + "<img flow-img=\"\\$flow.files\\[0\\]\" height=\"400px\" width=\"700px\" id=\"imgId\" ng-show=\"false\" class=\"ng-hide\" src=\"\">"
                    + "</div>", "");
            content = content.substring(0, content.indexOf("<div id=\"footer\" class=\"modal-footer"));
            content = content.replaceAll("api/event/getbackgroundimage\\?file_name=", onlyPathOFTemplate);
//            System.out.println("=== Contant : " + content);
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(EventTransformerBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        for (HkEventRecipientEntity eventRecipientEntity : hkEventRecipientEntitySet) {
            recipientCodes.add(eventRecipientEntity.getHkEventRecipientEntityPK().getReferenceInstance() + SEPARATOR + eventRecipientEntity.getHkEventRecipientEntityPK().getReferenceType());
        }
        Set<Long> inviteeIdsSet = userManagementServiceWrapper.retrieveRecipientIds(recipientCodes);
//        Set<Long> inviteeIdsSet = new HashSet<>();
//        inviteeIdsSet.add(1l);
//        inviteeIdsSet.add(2l);
//        inviteeIdsSet.add(3l);
//        inviteeIdsSet.add(4l);
//        inviteeIdsSet.add(5l);
//        inviteeIdsSet.add(6l);
        if (inviteeIdsSet != null && !inviteeIdsSet.isEmpty()) {
            Map<Long, UMUser> retrievedUsersMap = new HashMap<>();
            Map<Long, String> usersAddressMap = new HashMap<>();
            iniviteeList.addAll(inviteeIdsSet);
            List<UMUser> retrieveUsers = userManagementServiceWrapper.retrieveUsersForInvitees(iniviteeList);
            if (!CollectionUtils.isEmpty(retrieveUsers)) {
                for (UMUser uMUser : retrieveUsers) {
                    retrievedUsersMap.put(uMUser.getId(), uMUser);
                    Set<UMContactAddress> umContactAddressSet = uMUser.getContact().getuMContactAddressSet();
                    if (!CollectionUtils.isEmpty(umContactAddressSet)) {
                        for (UMContactAddress umContactAddress : umContactAddressSet) {
                            if (umContactAddress.getAddressType().equals("CURRENT")) {
                                usersAddressMap.put(uMUser.getId(), umContactAddress.getAddressLine1());
                            }
                        }
                    }
                }
            }
            List<HkEventRegistrationEntity> retrieveUserEventRegistrationEntities = hrService.retrieveUserEventRegistrationEntities(eventId, null, Boolean.FALSE);
            if (!CollectionUtils.isEmpty(retrieveUserEventRegistrationEntities)) {
                for (HkEventRegistrationEntity hkEventRegistrationEntity : retrieveUserEventRegistrationEntities) {
                    if (hkEventRegistrationEntity.getStatus().equals(HkSystemConstantUtil.EventUserRegistrationStatus.ATTENDING)) {
                        userIds.add(hkEventRegistrationEntity.getHkEventRegistrationEntityPK().getUserId());
                    }
                }
                if (!CollectionUtils.isEmpty(userIds)) {

                    for (HkEventRegistrationEntity hkEventRegistrationEntity : retrieveUserEventRegistrationEntities) {
                        if (hkEventRegistrationEntity.getStatus().equals(HkSystemConstantUtil.EventUserRegistrationStatus.ATTENDING)) {
                            OutputStream os = null;
                            try {
                                String newContant = content;

                                UMUser uMUser = retrievedUsersMap.get(hkEventRegistrationEntity.getHkEventRegistrationEntityPK().getUserId());
                                newContant = newContant.replaceAll("\\$Employee Name", uMUser.getFirstName() + " " + uMUser.getLastName());
                                newContant = newContant.replaceAll("\\$Employee Address Line 1 \\$Employee Address Line 2", usersAddressMap.get(hkEventRegistrationEntity.getHkEventRegistrationEntityPK().getUserId()));
                                newContant = newContant.replaceAll("\\$Employee Email", uMUser.getEmailAddress() != null ? uMUser.getEmailAddress() : "");
                                newContant = newContant.replaceAll("\\$Employee Phone No.", uMUser.getMobileNumber() != null ? uMUser.getMobileNumber() : "");
                                newContant = newContant.replaceAll("\\$Total adults accompaning", String.valueOf(hkEventRegistrationEntity.getAdultCount()));
                                newContant = newContant.replaceAll("\\$Total children accompaning", String.valueOf(hkEventRegistrationEntity.getChildCount()));
                                String fileNamePath = filePath.toString() + (eventId) + "-" + hkEventRegistrationEntity.getHkEventRegistrationEntityPK().getUserId() + ".pdf";
                                os = new FileOutputStream(fileNamePath);
                                ITextRenderer renderer = new ITextRenderer();
                                renderer.setDocumentFromString(newContant);
                                renderer.layout();
                                try {
                                    renderer.createPDF(os);
                                    printedPdfUserId.add(fileNamePath);
                                } catch (DocumentException ex) {
                                    java.util.logging.Logger.getLogger(EventTransformerBean.class.getName()).log(Level.SEVERE, null, ex);
                                } finally {
                                    try {
                                        os.flush();
                                        os.close();
                                    } catch (IOException ex) {
                                        java.util.logging.Logger.getLogger(EventTransformerBean.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                }
                            } catch (FileNotFoundException ex) {
                                java.util.logging.Logger.getLogger(EventTransformerBean.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }
                }

            }
            if (!isAttendingListOnly) {
                for (Long invitee : iniviteeList) {
                    if (!userIds.contains(invitee)) {
                        OutputStream os = null;
                        try {
                            String newContant = content;
                            UMUser uMUser = retrievedUsersMap.get(invitee);
                            newContant = newContant.replaceAll("\\$Employee Name", uMUser.getFirstName() + " " + uMUser.getLastName());
                            newContant = newContant.replaceAll("\\$Employee Address Line 1 \\$Employee Address Line 2", usersAddressMap.get(uMUser.getId()));
                            newContant = newContant.replaceAll("\\$Employee Email", uMUser.getEmailAddress() != null ? uMUser.getEmailAddress() : "");
                            newContant = newContant.replaceAll("\\$Employee Phone No.", uMUser.getMobileNumber() != null ? uMUser.getMobileNumber() : "");
                            newContant = newContant.replaceAll("\\$Total adults accompaning", String.valueOf(0));
                            newContant = newContant.replaceAll("\\$Total children accompaning", String.valueOf(0));
                            String fileNamePath = filePath.toString() + (eventId) + "-" + uMUser.getId() + ".pdf";
                            os = new FileOutputStream(fileNamePath);
                            ITextRenderer renderer = new ITextRenderer();
                            renderer.setDocumentFromString(newContant);
                            renderer.layout();
                            try {
                                renderer.createPDF(os);
                                printedPdfUserId.add(fileNamePath);
                            } catch (DocumentException ex) {
                                java.util.logging.Logger.getLogger(EventTransformerBean.class.getName()).log(Level.SEVERE, null, ex);
                            } finally {
                                try {
                                    os.flush();
                                    os.close();
                                } catch (IOException ex) {
                                    java.util.logging.Logger.getLogger(EventTransformerBean.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                        } catch (FileNotFoundException ex) {
                            java.util.logging.Logger.getLogger(EventTransformerBean.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }
        }

        if (!CollectionUtils.isEmpty(printedPdfUserId)) {
            OutputStream out = null;
            try {
                List<InputStream> list = new ArrayList<InputStream>();
                for (String fileName : printedPdfUserId) {
                    try {
                        list.add(new FileInputStream(new File(fileName)));
                    } catch (FileNotFoundException ex) {
                        java.util.logging.Logger.getLogger(EventTransformerBean.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                out = new FileOutputStream(new File(filePath.toString() + retrieveEventById.getEventTitle() + ".pdf"));
                doMerge(list, out);
                return filePath.toString() + retrieveEventById.getEventTitle() + ".pdf";
            } catch (FileNotFoundException ex) {
                java.util.logging.Logger.getLogger(EventTransformerBean.class.getName()).log(Level.SEVERE, null, ex);
            } catch (DocumentException | IOException ex) {
                java.util.logging.Logger.getLogger(EventTransformerBean.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    out.close();
                } catch (IOException ex) {
                    java.util.logging.Logger.getLogger(EventTransformerBean.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return null;
    }

    public static void doMerge(List<InputStream> list, OutputStream outputStream)
            throws DocumentException, IOException {
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, outputStream);
        document.open();
        PdfContentByte cb = writer.getDirectContent();

        for (InputStream in : list) {
            PdfReader reader = new PdfReader(in);
            for (int i = 1; i <= reader.getNumberOfPages(); i++) {
                document.newPage();
                //import the page from source pdf
                PdfImportedPage page = writer.getImportedPage(reader, i);
                //add the page to the destination pdf
                cb.addTemplate(page, 0, 0);
            }
            in.close();
        }
        outputStream.flush();
        document.close();
        outputStream.close();
    }

    public String generatePdfForSelf(Long eventId) throws GenericDatabaseException {
        HkEventEntity retrieveEventById = hrService.retrieveEventById(eventId, true, false);
        String pathOfTemplate = FolderManagement.getPathOfImage(retrieveEventById.getInvitationTemplateName());
        String onlyPathOFTemplate = FolderManagement.getOnlyPathOfFile(retrieveEventById.getInvitationTemplateName());
        String content = null;
        File tempDir = FolderManagement.checkIsExists(FolderManagement.basePath, FolderManagement.TEMP, null);
        StringBuilder filePath = new StringBuilder(tempDir.getPath());
        filePath.append(File.separator);
        try {

            content = new String(Files.readAllBytes(Paths.get(pathOfTemplate)));
            content = content.replaceAll("<div id=\"templateImg\">"
                    + "<img flow-img=\"\\$flow.files\\[0\\]\" height=\"400px\" width=\"700px\" id=\"imgId\" ng-show=\"false\" class=\"ng-hide\" src=\"\">"
                    + "</div>", "");
            content = content.substring(0, content.indexOf("<div id=\"footer\" class=\"modal-footer"));
            content = content.replaceAll("api/event/getbackgroundimage\\?file_name=", onlyPathOFTemplate);
//            System.out.println("=== Contant : " + content);
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(EventTransformerBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        UMUser loggedInUser = userService.retrieveUserById(loginDataBean.getId(), loginDataBean.getCompanyId());
        if (loggedInUser != null) {
            String adultCounts = null;
            String childCounts = null;
            List<HkEventRegistrationEntity> userRegistration = hrService.retrieveUserEventRegistrationEntities(eventId, loginDataBean.getId(), Boolean.FALSE);
            if (!CollectionUtils.isEmpty(userRegistration)) {
                for (HkEventRegistrationEntity registrationEntity : userRegistration) {
                    int adultCount = registrationEntity.getAdultCount();
                    adultCounts = String.valueOf(adultCount);
                    int childCount = registrationEntity.getChildCount();
                    childCounts = String.valueOf(childCount);
                }

            } else {
                adultCounts = String.valueOf(0);
                childCounts = String.valueOf(0);
            }
            List<String> printedPdfUserId = new ArrayList<>();
            String address = null;

            Set<UMContactAddress> umContactAddressSet = loggedInUser.getContact().getuMContactAddressSet();
            if (!CollectionUtils.isEmpty(umContactAddressSet)) {
                for (UMContactAddress umContactAddress : umContactAddressSet) {
                    if (umContactAddress.getAddressType().equals("CURRENT")) {
                        address = umContactAddress.getAddressLine1();
                    }
                }
            }

            OutputStream os = null;
            try {
                String newContant = content;
                newContant = newContant.replaceAll("\\$Employee Name", loggedInUser.getFirstName() + " " + loggedInUser.getLastName());
                newContant = newContant.replaceAll("\\$Employee Address Line 1 \\$Employee Address Line 2", address);
                newContant = newContant.replaceAll("\\$Employee Email", loggedInUser.getEmailAddress() != null ? loggedInUser.getEmailAddress() : "");
                newContant = newContant.replaceAll("\\$Employee Phone No.", loggedInUser.getMobileNumber() != null ? loggedInUser.getMobileNumber() : "");
                newContant = newContant.replaceAll("\\$Total adults accompaning", adultCounts);
                newContant = newContant.replaceAll("\\$Total children accompaning", childCounts);
                String fileNamePath = filePath.toString() + (eventId) + "-" + loggedInUser.getId() + ".pdf";
                os = new FileOutputStream(fileNamePath);
                ITextRenderer renderer = new ITextRenderer();
                renderer.setDocumentFromString(newContant);
                renderer.layout();
                try {
                    renderer.createPDF(os);
                    printedPdfUserId.add(fileNamePath);
                } catch (DocumentException ex) {
                    java.util.logging.Logger.getLogger(EventTransformerBean.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    try {
                        os.flush();
                        os.close();
                    } catch (IOException ex) {
                        java.util.logging.Logger.getLogger(EventTransformerBean.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            } catch (FileNotFoundException ex) {
                java.util.logging.Logger.getLogger(EventTransformerBean.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (!CollectionUtils.isEmpty(printedPdfUserId)) {
                OutputStream out = null;
                try {
                    List<InputStream> list = new ArrayList<InputStream>();
                    for (String fileName : printedPdfUserId) {
                        try {
                            list.add(new FileInputStream(new File(fileName)));
                        } catch (FileNotFoundException ex) {
                            java.util.logging.Logger.getLogger(EventTransformerBean.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    out = new FileOutputStream(new File(filePath.toString() + retrieveEventById.getEventTitle() + ".pdf"));
                    doMerge(list, out);
                    return filePath.toString() + retrieveEventById.getEventTitle() + ".pdf";
                } catch (FileNotFoundException ex) {
                    java.util.logging.Logger.getLogger(EventTransformerBean.class.getName()).log(Level.SEVERE, null, ex);
                } catch (DocumentException | IOException ex) {
                    java.util.logging.Logger.getLogger(EventTransformerBean.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    try {
                        out.close();
                    } catch (IOException ex) {
                        java.util.logging.Logger.getLogger(EventTransformerBean.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
        return null;
    }
}
