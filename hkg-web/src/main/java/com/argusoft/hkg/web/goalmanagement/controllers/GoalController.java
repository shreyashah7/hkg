package com.argusoft.hkg.web.goalmanagement.controllers;

import com.argusoft.hkg.common.constantutil.HkSystemConstantUtil;
import com.argusoft.hkg.web.base.PrimaryKey;
import com.argusoft.hkg.web.base.ResponseEntity;
import com.argusoft.hkg.web.base.controllers.BaseController;
import com.argusoft.hkg.web.eventmanagement.databeans.EventDataBean;
import com.argusoft.hkg.web.goalmanagement.databeans.GoalSheetDataBean;
import com.argusoft.hkg.web.goalmanagement.databeans.GoalTemplateDataBean;
import com.argusoft.hkg.web.goalmanagement.transformers.GoalTransformerBean;
import com.argusoft.hkg.web.usermanagement.databeans.GoalPermissionDataBean;
import com.argusoft.hkg.web.util.SelectItem;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author rajkumar
 */
@RestController
@RequestMapping("/goal")
public class GoalController extends BaseController<EventDataBean, Long> {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private GoalTransformerBean goalTransformer;

    @Override
    public ResponseEntity<List<EventDataBean>> retrieveAll() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ResponseEntity<EventDataBean> retrieveById(PrimaryKey<Long> primaryKey) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ResponseEntity<PrimaryKey<Long>> update(EventDataBean t) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ResponseEntity<PrimaryKey<Long>> create(EventDataBean t) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ResponseEntity<EventDataBean> deleteById(PrimaryKey<Long> primaryKey) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ResponseEntity<Map<String, Object>> retrievePrerequisite() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @RequestMapping(value = "/retrievegoalpermissionbydesignations", method = RequestMethod.POST)
    public ResponseEntity<Map<String, List<String>>> retrieveGoalPermissionByDesignations(@RequestBody Map designationIds) {
        List<GoalPermissionDataBean> result = goalTransformer.retrieveGoalPermissionByDesignations((List) designationIds.get("designations"));
        Map<String, List<String>> result1 = new HashMap<>();
        if (!CollectionUtils.isEmpty(result)) {
            for (GoalPermissionDataBean goalPermissionDataBean : result) {
                if (result1.get(goalPermissionDataBean.getReferenceType()) == null) {
                    result1.put(goalPermissionDataBean.getReferenceType(), new ArrayList<String>());
                }

                result1.get(goalPermissionDataBean.getReferenceType()).add(goalPermissionDataBean.getReferenceInstance());
            }
        }
        return new ResponseEntity(result1, null, null, null);
    }

    @RequestMapping(value = "/savegoaltemplate", method = RequestMethod.POST)
    public ResponseEntity<Map<String, String>> saveGoalTemplate(@RequestBody GoalTemplateDataBean goalTemplateDataBean) {
        Long id = goalTransformer.saveGoalTemplate(goalTemplateDataBean);
        Map<String, String> result = new HashMap<>();
        if (id != null) {
            result.put("id", id.toString());
        }
        return new ResponseEntity(result, null, null, null);
    }

    @RequestMapping(value = "/retrieveallgoaltemplates", method = RequestMethod.POST)
    public ResponseEntity<List<GoalTemplateDataBean>> retrieveAllGoalTemplate() {
        List<GoalTemplateDataBean> goalTemplateDataBeans = goalTransformer.retrieveAllGoalTemplates();

        return new ResponseEntity(goalTemplateDataBeans, null, null, null);
    }

    @RequestMapping(value = "/retrievegoaltemplatebyid", method = RequestMethod.POST)
    public ResponseEntity<GoalTemplateDataBean> retrieveGoalTemplateById(@RequestBody Long id) {
        GoalTemplateDataBean result = goalTransformer.retrieveGoalTemplateById(id);
        return new ResponseEntity(result, null, null, null);
    }

    @RequestMapping(value = "/searchgoaltemplate", method = RequestMethod.GET, produces = {"application/json"})
    public List<GoalTemplateDataBean> searchGoalTemplate(@RequestParam("q") String query) {
        List<GoalTemplateDataBean> goalTemplateDataBeans = new ArrayList();
        goalTemplateDataBeans = goalTransformer.retrieveAllGoalTemplatesBySearch(query, Arrays.asList(HkSystemConstantUtil.GoalTemplateStatus.ACTIVE, HkSystemConstantUtil.GoalTemplateStatus.PENDING), false);
        return goalTemplateDataBeans;
    }

    @RequestMapping(value = "/retrieveactivegoaltemplatesbyservice", method = RequestMethod.POST)
    public List<GoalTemplateDataBean> retrieveActiveGoalTemplatesByService(@RequestBody Long serviceId) {
        List<GoalTemplateDataBean> goalTemplateDataBeans = new ArrayList();
        goalTemplateDataBeans = goalTransformer.retrieveActiveGoalTemplatesByService(serviceId);
        return goalTemplateDataBeans;
    }

    @RequestMapping(value = "/retrieveactivegoaltemplatesbydepartment", method = RequestMethod.POST)
    public List<GoalTemplateDataBean> retrieveActiveGoalTemplatesByDepartment(@RequestBody Long departmentId) {
        List<GoalTemplateDataBean> goalTemplateDataBeans = new ArrayList();
        goalTemplateDataBeans = goalTransformer.retrieveActiveGoalTemplatesByDepartment(departmentId);
        return goalTemplateDataBeans;
    }

    @RequestMapping(value = "/retrieveactivegoaltemplatesbydesignation", method = RequestMethod.POST)
    public List<GoalTemplateDataBean> retrieveActiveGoalTemplatesByDesignation(@RequestBody Long designationId) {
        List<GoalTemplateDataBean> goalTemplateDataBeans = new ArrayList();
        goalTemplateDataBeans = goalTransformer.retrieveActiveGoalTemplatesByDesignation(designationId);
        return goalTemplateDataBeans;
    }

    @RequestMapping(value = "/saveGoalTemplates", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    public void saveGoalTemplates(@RequestBody List<GoalTemplateDataBean> goalTemplateDataBeans) {
        if (!CollectionUtils.isEmpty(goalTemplateDataBeans)) {
            goalTransformer.saveGoalTemplates(goalTemplateDataBeans);
        }
    }

    @RequestMapping(value = "/retrieveActiveGoalTemplatesBySearch", method = RequestMethod.GET)
    public List<GoalTemplateDataBean> retrieveActiveGoalTemplatesBySearch(@RequestParam("q") String query) {
        return goalTransformer.retrieveAllGoalTemplatesBySearch(query, Arrays.asList(HkSystemConstantUtil.GoalTemplateStatus.ACTIVE), true);
    }

    @RequestMapping(value = "/retrieveActiveAndPendingGoalTemplatesBySearch", method = RequestMethod.GET)
    public List<GoalTemplateDataBean> retrieveActiveAndPendingGoalTemplatesBySearch(@RequestParam("q") String query) {
        return goalTransformer.retrieveAllGoalTemplatesBySearch(query, Arrays.asList(HkSystemConstantUtil.GoalTemplateStatus.ACTIVE, HkSystemConstantUtil.GoalTemplateStatus.PENDING, HkSystemConstantUtil.GoalTemplateStatus.DISCARDED), true);
    }

    @RequestMapping(value = "/deleteActiveGoalTemplates", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    public void deleteActiveGoalTemplates(@RequestBody Map payload) {
        if (payload != null) {
            goalTransformer.deleteAllActiveGoalTemplates(payload);
        }
    }

    @RequestMapping(value = "/retrieveGoalTemplateModifiers", method = RequestMethod.POST)
    public Map<String, String> retrieveGoalTemplateModifiers() {
        return HkSystemConstantUtil.GOAL_TEMPLATE_MODIFIERS_MAP;
    }

    @RequestMapping(value = "/retrievependinggoalsheet", method = RequestMethod.POST)
    public List<GoalSheetDataBean> retrievePendingGoalSheet(@RequestBody Long userId) {
        List<GoalSheetDataBean> sheetDataBeans = new ArrayList<>();
        if (userId == -1) {
            sheetDataBeans = goalTransformer.retrieveUserGoalSheet(null, Arrays.asList(HkSystemConstantUtil.UserGoalTemplateStatus.PENDING));
        } else {
            sheetDataBeans = goalTransformer.retrieveUserGoalSheet(userId, Arrays.asList(HkSystemConstantUtil.UserGoalTemplateStatus.PENDING));
        }
        return sheetDataBeans;

    }

    @RequestMapping(value = "/retrievesubmittedgoalsheet", method = RequestMethod.POST)
    public List<GoalSheetDataBean> retrieveSubmittedGoalSheet(@RequestBody Map<String, Object> payload) throws ParseException {
        List<GoalSheetDataBean> sheetDataBeans = new ArrayList<>();
        Long userId = Long.parseLong(payload.get("userId").toString());
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        Date fromDate = formatter.parse(payload.get("fromDate").toString());
        Date toDate = formatter.parse(payload.get("toDate").toString());
        if (fromDate != null && toDate != null && userId != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(fromDate);
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            Date newFromDate = cal.getTime();

            cal.setTime(toDate);
            cal.set(Calendar.HOUR_OF_DAY, 23);
            cal.set(Calendar.MINUTE, 59);
            cal.set(Calendar.SECOND, 59);
            Date newToDate = cal.getTime();

            if (userId == -1) {
                sheetDataBeans = goalTransformer.retrieveSubmittedUserGoalSheet(null, Arrays.asList(HkSystemConstantUtil.UserGoalTemplateStatus.ATTENDED, HkSystemConstantUtil.UserGoalTemplateStatus.SUBMITTED), newFromDate, newToDate);
            } else {
                sheetDataBeans = goalTransformer.retrieveSubmittedUserGoalSheet(userId, Arrays.asList(HkSystemConstantUtil.UserGoalTemplateStatus.ATTENDED, HkSystemConstantUtil.UserGoalTemplateStatus.SUBMITTED), newFromDate, newToDate);
            }
        }
        return sheetDataBeans;
    }

    @RequestMapping(value = "/submitgoalsheet", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    public void submitGoalSheet(@RequestBody GoalSheetDataBean sheetDataBean) {
        goalTransformer.submitGoalSheet(sheetDataBean);

    }
    
    @RequestMapping(value = "/retrieveusersforgoalsheet",method = RequestMethod.POST)
    public Set<SelectItem> retrieveUsersForGoalSheet(){
        Set<SelectItem> result=new HashSet<>();
        result=goalTransformer.retrieveUsersForGoalSheet();
        return result;
    }
}
