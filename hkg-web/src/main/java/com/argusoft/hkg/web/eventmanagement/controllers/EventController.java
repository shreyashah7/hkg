/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.eventmanagement.controllers;

import com.argusoft.generic.database.exception.GenericDatabaseException;
import com.argusoft.hkg.common.constantutil.HkSystemConstantUtil;
import com.argusoft.hkg.common.functionutil.FolderManagement;
import com.argusoft.hkg.common.functionutil.HkSystemFunctionUtil;
import com.argusoft.hkg.core.util.CategoryType;
import com.argusoft.hkg.web.assets.databeans.CategoryDataBean;
import com.argusoft.hkg.web.assets.transformers.CategoryTransformerBean;
import com.argusoft.hkg.web.base.PrimaryKey;
import com.argusoft.hkg.web.base.ResponseCode;
import com.argusoft.hkg.web.base.ResponseEntity;
import com.argusoft.hkg.web.base.controllers.BaseController;
import com.argusoft.hkg.web.eventmanagement.databeans.EventDataBean;
import com.argusoft.hkg.web.eventmanagement.databeans.EventRegistrationFieldsDataBean;
import com.argusoft.hkg.web.eventmanagement.databeans.EventUserRegistrationDataBean;
import com.argusoft.hkg.web.eventmanagement.transformers.EventTransformerBean;
import com.argusoft.hkg.web.center.usermanagement.databeans.LoginDataBean;
import com.argusoft.hkg.web.util.SelectItem;
import com.argusoft.hkg.web.util.SessionUtil;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author kuldeep
 */
@RestController
@RequestMapping("/event")
public class EventController extends BaseController<EventDataBean, Long> {

    @Autowired
    EventTransformerBean eventTransformerBean;
    @Autowired
    CategoryTransformerBean categoryTransformerBean;
    @Autowired
    LoginDataBean loginDataBean;
    @Autowired
    SessionUtil sessionUtil;
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    private final static String UPCOMING_EVENTS = "upcomingEvents";
    private final static String COMPLETED_EVENTS = "completedEvents";

    @Override
    public ResponseEntity<List<EventDataBean>> retrieveAll() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ResponseEntity<EventDataBean> retrieveById(@RequestBody PrimaryKey<Long> primaryKey) {
        EventDataBean eventDataBean = eventTransformerBean.retrieveEventById(primaryKey.getPrimaryKey());
        return new ResponseEntity<>(eventDataBean, ResponseCode.SUCCESS, null, null);
    }

    @Override
    public ResponseEntity<PrimaryKey<Long>> update(@Valid @RequestBody EventDataBean eventDataBean) {
        eventTransformerBean.updateEvent(eventDataBean);
        if(eventDataBean.getStatus().equals(HkSystemConstantUtil.EventStatus.CANCELLED)){
            return new ResponseEntity<>(null, ResponseCode.SUCCESS, "Event cancelled successfully.", null);
        }else{
            return new ResponseEntity<>(null, ResponseCode.SUCCESS, "Event updated successfully.", null);
        }
        
    }

    @Override
    public ResponseEntity<PrimaryKey<Long>> create(@Valid @RequestBody EventDataBean eventDataBean) {
        if (HkSystemFunctionUtil.convertToServerDate(eventDataBean.getPublishedOn(), loginDataBean.getClientRawOffsetInMin()).equals(HkSystemFunctionUtil.getDateWithoutTimeStamp(new Date()))) {
            try {
                if (eventTransformerBean.createEvent(eventDataBean) != null) {
                    return new ResponseEntity<>(null, ResponseCode.SUCCESS, "Event created and published successfully.", null);
                } else {
                    return new ResponseEntity<>(null, ResponseCode.FAILURE, "Could not save details, please try again.", null);
                }
            } catch (IOException ex) {
                java.util.logging.Logger.getLogger(EventController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            try {
                if (eventTransformerBean.createEvent(eventDataBean) != null) {
                    return new ResponseEntity<>(null, ResponseCode.SUCCESS, "Event created successfully.", null);
                } else {
                    return new ResponseEntity<>(null, ResponseCode.FAILURE, "Could not save details, please try again.", null);
                }
            } catch (IOException ex) {
                java.util.logging.Logger.getLogger(EventController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return new ResponseEntity<>(null, ResponseCode.FAILURE, "Could not save details, please try again.", null);
    }

    @Override
    public ResponseEntity<EventDataBean> deleteById(@RequestBody PrimaryKey<Long> primaryKey
    ) {
        eventTransformerBean.removeEvent(primaryKey.getPrimaryKey());
        return new ResponseEntity<>(null, ResponseCode.SUCCESS, "Event removed successfully.", null);
    }

    @RequestMapping(value = "/archieveevent", method = RequestMethod.POST)
    public ResponseEntity<Long> archieveEvent(@RequestBody Long eventId
    ) {
        eventTransformerBean.archiveEvent(eventId);
        return new ResponseEntity<>(null, ResponseCode.SUCCESS, "Event Archived successfully.", null);
    }

    @RequestMapping(value = "/searchevent", method = RequestMethod.GET, produces = {"application/json"})
    public List<EventDataBean> searchEvent(@RequestParam("q") String query
    ) {
        return eventTransformerBean.searchEvents(query);
    }

    @RequestMapping(value = "/retrieveeventcategories", method = RequestMethod.GET)
    public ResponseEntity<List<CategoryDataBean>> retrieveEventCategories() {
        return new ResponseEntity<>(categoryTransformerBean.retrieveCategoryList(CategoryType.EVENT, false), ResponseCode.SUCCESS, null, null);
    }

    @RequestMapping(value = "/retriactiveeveeventcategories", method = RequestMethod.GET)
    public ResponseEntity<List<CategoryDataBean>> retrieveActiveEventCategories() {
        return new ResponseEntity<>(categoryTransformerBean.retrieveCategoryList(CategoryType.EVENT, true), ResponseCode.SUCCESS, null, null);
    }

    @RequestMapping(value = "/doescategorynameexist", method = RequestMethod.POST)
    public ResponseEntity<PrimaryKey<Long>> doesCategoryNameExist(@RequestBody CategoryDataBean categoryDataBean
    ) {
        if (categoryDataBean.getDisplayName() != null && categoryTransformerBean.doesCategoryNameExist(categoryDataBean.getDisplayName(), CategoryType.EVENT, categoryDataBean.getId())) {
            return new ResponseEntity<>(null, ResponseCode.WARNING, categoryDataBean.getDisplayName() + " already given.", null, false);
        } else {
            return new ResponseEntity<>(null, ResponseCode.SUCCESS, null, null, false);
        }
    }

    @RequestMapping(value = "/doeseventnameexist", method = RequestMethod.POST)
    public ResponseEntity<PrimaryKey<Long>> doesEventNameExist(@RequestBody EventDataBean eventDataBean
    ) {
        if (eventDataBean.getEventTitle() != null && eventTransformerBean.doesEventNameExist(eventDataBean.getEventTitle(), eventDataBean.getId())) {
            return new ResponseEntity<>(null, ResponseCode.WARNING, eventDataBean.getEventTitle() + " already given.", null, false);
        } else {
            return new ResponseEntity<>(null, ResponseCode.SUCCESS, null, null, false);
        }
    }

    @RequestMapping(value = "/createeventcategory", method = RequestMethod.POST)
    public ResponseEntity<PrimaryKey<Long>> createEventCategory(@RequestBody CategoryDataBean categoryDataBean
    ) {
        if (categoryTransformerBean.createCategory(categoryDataBean, CategoryType.EVENT)) {
            return new ResponseEntity<>(null, ResponseCode.SUCCESS, "Category added successfully.", null);
        } else {
            return new ResponseEntity<>(null, ResponseCode.FAILURE, "Could not save details, please try again.", null);
        }
    }

    @RequestMapping(value = "/retrievecategorysuggestions", method = RequestMethod.GET)
    public ResponseEntity<List<SelectItem>> retrieveCategorySuggestions() {
        return new ResponseEntity<>(categoryTransformerBean.searchExistingCategoryNames(null, CategoryType.EVENT), ResponseCode.SUCCESS, null, null);
    }

    @RequestMapping(value = "/updateeventcategory", method = RequestMethod.POST)
    public ResponseEntity<PrimaryKey<Long>> updateEventCategory(@RequestBody CategoryDataBean categoryDataBean
    ) {
        String successMsg, failureMsg;
        if (!categoryDataBean.getStatus().equals("Active")) {
            successMsg = "Event category removed successfully.";
            failureMsg = "Event category could not be removed. Try again.";
        } else {
            successMsg = "Category saved successfully.";
            failureMsg = "Could not save details, please try again.";
        }
        if (categoryTransformerBean.updateCategory(categoryDataBean, CategoryType.EVENT)) {
            return new ResponseEntity<>(null, ResponseCode.SUCCESS, successMsg, null);
        } else {
            return new ResponseEntity<>(null, ResponseCode.FAILURE, failureMsg, null);
        }
    }

    @RequestMapping(value = "/uploadfile", method = RequestMethod.POST)
    public ResponseEntity<Map> uploadFile(@RequestBody String[] info) throws FileNotFoundException, IOException {
        String fileName = info[0];
        String fileType = info[1];
        String filenameformat = eventTransformerBean.uploadFile(fileName, fileType);
        Map result = new HashMap();
        result.put("filename", filenameformat);
        return new ResponseEntity<>(result, ResponseCode.SUCCESS, "", null);
    }

    @RequestMapping(value = "/createphotogallery", method = RequestMethod.POST)
    public ResponseEntity<PrimaryKey<Long>> createPhotoGallery(@RequestBody EventDataBean eventDataBean) throws IOException {
        if (eventDataBean != null) {
            eventTransformerBean.createPhotoGallery(eventDataBean);
            return new ResponseEntity<>(null, ResponseCode.SUCCESS, "Gallery created successfully.", null);
        } else {
            return new ResponseEntity<>(null, ResponseCode.FAILURE, "Could not create gallery!", null);
        }
    }

    @RequestMapping(value = "/retrieveimagepaths", method = RequestMethod.POST)
    public ResponseEntity<Map<String, List<String>>> retrieveImagePaths(@RequestBody Long eventId) throws IOException {
        Map<String, List<String>> retrieveImagePaths = eventTransformerBean.retrieveImagePaths(eventId);
        return new ResponseEntity<>(retrieveImagePaths, ResponseCode.SUCCESS, null, null);
    }

    @RequestMapping(value = "/retrieveimagethumbnailpaths", method = RequestMethod.POST)
    public ResponseEntity<Map<String, List<String>>> retrieveImageThumbnailPaths(@RequestBody Long eventId) throws IOException {
        Map<String, List<String>> retrieveImagePaths = eventTransformerBean.retrieveImagePaths(eventId);
        return new ResponseEntity<>(retrieveImagePaths, ResponseCode.SUCCESS, null, null);
    }

    @RequestMapping(value = "/retrieveevents", method = RequestMethod.POST)
    public ResponseEntity<Map<String, List<EventDataBean>>> retrieveAllEvents(@RequestBody Map result) {
        Map<String, List<EventDataBean>> eventsMap = new HashMap<>();
        Long categoryId = null;
        String value = result.get("categoryId").toString();
        if (!StringUtils.isEmpty(value) && !value.equalsIgnoreCase("null")) {
            categoryId = new Long(value);
        }
        Boolean haveAddEditRights = (Boolean) result.get("haveAddEditRights");
        eventsMap.put(UPCOMING_EVENTS, eventTransformerBean.retrieveUpcomingEvents(categoryId, haveAddEditRights));
        eventsMap.put(COMPLETED_EVENTS, eventTransformerBean.retrieveCompletedEvents(categoryId, haveAddEditRights));
        return new ResponseEntity<>(eventsMap, ResponseCode.SUCCESS, null, null);
    }

    @RequestMapping(value = "/retrieveregistrationformnames", method = RequestMethod.GET)
    public ResponseEntity<Map<Long, String>> retrieveRegistrationFormNames() {
        return new ResponseEntity<>(eventTransformerBean.retrieveRegistrationFormNames(), ResponseCode.SUCCESS, null, null);
    }

    @RequestMapping(value = "/retrieveregistrationfields", method = RequestMethod.POST)
    public ResponseEntity<List<EventRegistrationFieldsDataBean>> retrieveRegistrationFieldsByEventId(@RequestBody Long eventId
    ) {
        return new ResponseEntity<>(eventTransformerBean.retrieveRegistrationFieldsByEventId(eventId), ResponseCode.SUCCESS, null, null);
    }
    
    @RequestMapping(value = "/getimage", method = RequestMethod.GET)
    @ResponseBody
    public FileSystemResource getFile(@RequestParam("file_name") String fileName
    ) {
        if (fileName != null && !fileName.equals("")) {
            String pathFromImageName = FolderManagement.getPathOfImage(fileName);
            return new FileSystemResource(pathFromImageName);
        }
        return null;
    }
    
    @RequestMapping(value = "/getbackgroundimage", method = RequestMethod.GET)
    @ResponseBody
    public FileSystemResource getBackgroundFile(@RequestParam("file_name") String fileName
    ) {
        if (fileName != null && !fileName.equals("")) {
            String pathFromImageName = FolderManagement.getPathOfImage(fileName);
            return new FileSystemResource(pathFromImageName);
        }
        return null;
    }

    @RequestMapping(value = "/retrievebyuser", method = RequestMethod.GET)
    public ResponseEntity<List<EventDataBean>> retrieveEventsByUser() {
        List<EventDataBean> eventDataBeans = eventTransformerBean.retrieveEventsforUser();
        return new ResponseEntity<>(eventDataBeans, ResponseCode.SUCCESS, null, null);
    }

    @RequestMapping(value = "/registerforevent", method = RequestMethod.POST)
    public ResponseEntity registerForEvent(@RequestBody EventDataBean eventRegistrationDataBean
    ) {
        try {
            eventTransformerBean.registerForEvent(eventRegistrationDataBean);
        } catch (Exception e) {
            return new ResponseEntity(null, ResponseCode.ERROR, "Could not register. Try again.", null);
        }
        return new ResponseEntity(null, ResponseCode.SUCCESS, "Registered Successfully.", null);
    }

    @RequestMapping(value = "/editRegistrationforevent", method = RequestMethod.POST)
    public ResponseEntity editRegistrationForEvent(@RequestBody EventDataBean eventRegistrationDataBean) {
        try {
            eventTransformerBean.editRegistrationForEvent(eventRegistrationDataBean);
        } catch (Exception e) {
            return new ResponseEntity(null, ResponseCode.ERROR, "Could not update. Try again.", null);
        }
        return new ResponseEntity(null, ResponseCode.SUCCESS, "Registration Updated Successfully.", null);
    }

    @RequestMapping(value = "/retrievecustomvalues", method = RequestMethod.POST)
    public ResponseEntity<List<EventRegistrationFieldsDataBean>> retrieveCustomValues(@RequestBody Long eventId
    ) {
        List<EventRegistrationFieldsDataBean> customValues = eventTransformerBean.retrieveCustomValues(eventId);
        return new ResponseEntity<>(customValues, ResponseCode.SUCCESS, "", null);
    }
    
    @RequestMapping(value = "/retrievecustomvaluesbyuser", method = RequestMethod.POST)
    public ResponseEntity<List<EventRegistrationFieldsDataBean>> retrieveCustomValuesByUser(@RequestBody Map data) {
        Long eventId = null;
        Long userId = null;
        if(!CollectionUtils.isEmpty(data)){
            if(data.containsKey("eventId") && data.get("eventId") != null && data.containsKey("userId") && data.get("userId") != null){
                eventId = ((Integer)data.get("eventId")).longValue();
                userId = ((Integer)data.get("userId")).longValue();
            }
        }
        List<EventRegistrationFieldsDataBean> customValues = eventTransformerBean.retrieveCustomValuesByUser(eventId,userId);
        return new ResponseEntity<>(customValues, ResponseCode.SUCCESS, "", null);
    }

    @RequestMapping(value = "/cancelregistration", method = RequestMethod.POST)
    public ResponseEntity cancelRegistration(@RequestBody Long eventId
    ) {
        eventTransformerBean.cancelregistration(eventId);
        return new ResponseEntity(null, ResponseCode.SUCCESS, "Registration cancelled successfully.", null);
    }

    @Override
    public ResponseEntity<Map<String, Object>> retrievePrerequisite() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @RequestMapping(value = "/addCustomDataToCategoryDataBean", method = RequestMethod.POST)
    public CategoryDataBean addCustomDataToCategoryDataBean(@RequestBody CategoryDataBean categoryDataBean
    ) {
        return categoryTransformerBean.addCustomDataToCategoryDataBean(categoryDataBean);
    }

    @RequestMapping(value = "/retrieveUserRegistrationEntities", method = RequestMethod.POST)
    public List<EventUserRegistrationDataBean> retrieveUserRegistrationEntities(@RequestBody Long eventId) throws GenericDatabaseException {
        return eventTransformerBean.retrieveUserRegistrationEntities(eventId);
    }

    @RequestMapping(value = "/generatepdf", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public void generatePdf(HttpServletResponse response, HttpServletRequest httpRequest) throws GenericDatabaseException {
        Map<String, String[]> parameterMap = httpRequest.getParameterMap();
        Long eventId = Long.parseLong(parameterMap.get("eventId")[0]);
        String printType = parameterMap.get("printType")[0];
        boolean isAttendeOnly = true;
        if (!printType.equals("Attending")) {
            isAttendeOnly = false;
        }
        String fileId = eventTransformerBean.generatePdf(eventId, isAttendeOnly);
        if (fileId != null) {
            try {
                response.setContentType("application/octet-stream");
                response.setHeader("Content-Disposition", "attachment;filename =" + eventId + ".pdf");
                String pathFromImageName = fileId;
                Path pathVarible = Paths.get(pathFromImageName);
                byte[] data = Files.readAllBytes(pathVarible);
                response.getOutputStream().write(data);
            } catch (IOException e) {
            }
        }
    }
    
    @RequestMapping(value = "/generatepdfforself", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public void generatePdfForSelf(HttpServletResponse response, HttpServletRequest httpRequest) throws GenericDatabaseException {
        Map<String, String[]> parameterMap = httpRequest.getParameterMap();
        Long eventId = Long.parseLong(parameterMap.get("eventId")[0]);
        String fileId = eventTransformerBean.generatePdfForSelf(eventId);
        if (fileId != null) {
            try {
                response.setContentType("application/octet-stream");
                response.setHeader("Content-Disposition", "attachment;filename =" + eventId + ".pdf");
                String pathFromImageName = fileId;
                Path pathVarible = Paths.get(pathFromImageName);
                byte[] data = Files.readAllBytes(pathVarible);
                response.getOutputStream().write(data);
            } catch (IOException e) {
            }
        }
    }
}
