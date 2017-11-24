/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.center.usermanagement.controllers;

import com.argusoft.generic.database.exception.GenericDatabaseException;
import com.argusoft.hkg.common.constantutil.HkSystemConstantUtil;
import com.argusoft.hkg.web.base.ResponseCode;
import com.argusoft.hkg.web.base.ResponseEntity;
import com.argusoft.hkg.web.center.transformers.CenterCustomFieldTransformer;
import com.argusoft.hkg.web.center.usermanagement.databeans.LoginDataBean;
import com.argusoft.hkg.web.center.util.ApplicationUtil;
import com.argusoft.hkg.web.customfield.databeans.DependentFieldDataBean;
import com.argusoft.hkg.web.util.HkSelect2DataBean;
import com.argusoft.hkg.web.util.SelectItem;
import com.argusoft.sync.center.model.HkDepartmentDocument;
import com.argusoft.sync.center.model.SyncCenterUserDocument;
import com.argusoft.sync.center.model.UmCompanyDocument;
import com.argusoft.sync.center.model.UmDesignationDocument;
import com.argusoft.usermanagement.common.model.UMUser;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author akta
 */
@RestController
@RequestMapping("/customfield")
public class CenterCustomFieldController {

    private final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    @Autowired
    ApplicationUtil applicationUtil;

    @Autowired
    LoginDataBean loginDataBean;

    @Autowired
    CenterCustomFieldTransformer centerCustomFieldTransformer;

    @RequestMapping(value = "/retrievesectionandcustomfieldtemplate", method = RequestMethod.POST, consumes = {"application/json"})
    public Map<String, Object> retrieveSectionAndCustomFieldTemplateByFeatureName(@RequestBody String featureName) {
        return applicationUtil.getCenterFeatureFromTemplateMap().get(featureName);
    }

    @RequestMapping(value = "/retrieveallfeaturesection", method = RequestMethod.POST, consumes = {"application/json"})
    public @ResponseBody
    Map<String, Object> retrieveSectionAndCustomFieldByFeatureId(@RequestBody Map<String, Object> customFieldVersionDetail) {
        Long companyId = null;
        Integer customFieldVersionId = null;
        if (customFieldVersionDetail != null) {
            if (customFieldVersionDetail.get(HkSystemConstantUtil.CUSTOM_FIELD_VERSION) != null) {
                customFieldVersionId = Integer.parseInt(customFieldVersionDetail.get(HkSystemConstantUtil.CUSTOM_FIELD_VERSION).toString());
            }
            if (customFieldVersionDetail.get("companyId") != null) {
                try {
                    companyId = Long.parseLong(customFieldVersionDetail.get("companyId").toString());
                } catch (NumberFormatException ex) {
                    LOGGER.error(customFieldVersionDetail.get("companyId") + " : Company not found");
                }
            }
        }

        Integer version = 0;
        if (applicationUtil.getSystemConfigrationMap() != null && applicationUtil.getSystemConfigrationMap().get(HkSystemConstantUtil.CUSTOM_FIELD_VERSION) != null) {
            version = Integer.parseInt(applicationUtil.getSystemConfigrationMap().get(HkSystemConstantUtil.CUSTOM_FIELD_VERSION));
        }

        if ((companyId == null || !companyId.equals(loginDataBean.getCompanyId())) || (customFieldVersionId == null || !version.equals(customFieldVersionId))) {
            Map<String, Object> result = new HashMap<>();
            result.put(HkSystemConstantUtil.CUSTOM_FIELD_VERSION, version);
            if (!CollectionUtils.isEmpty(applicationUtil.getCenterFeatureFromTemplateMap())) {
                result.put("customFieldData", applicationUtil.getCenterFeatureFromTemplateMap());
            }
            return result;
        }
        return new HashMap<>();
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET, produces = {"application/json"})
    public List<HkSelect2DataBean> search(@RequestParam("q") String searchString) throws GenericDatabaseException {
        return centerCustomFieldTransformer.search(searchString);
    }

    // Method for fetching searched custom field ids
    @RequestMapping(value = "/retrievecustomfieldbyseachcriteria", method = RequestMethod.POST, consumes = {"application/json"})
    public ResponseEntity<Map<String, List<SelectItem>>> retrieveCustomFieldBySeachCriteria(@RequestBody String featureName) throws GenericDatabaseException {
        Map<String, List<SelectItem>> retrieveCustomFieldBySearch = centerCustomFieldTransformer.retrieveCustomFieldBySearch(featureName);
        return new ResponseEntity<>(retrieveCustomFieldBySearch, ResponseCode.SUCCESS, "", null, true);
    }

    //Method to be called when data could not be found in local storage
    @RequestMapping(value = "/retrieveSearchField", method = RequestMethod.POST, consumes = {"application/json"})
    public ResponseEntity<Map<String, Object>> retrieveSearchField(@RequestBody String featureName) throws GenericDatabaseException {
        Map<String, Object> retrieveCustomFieldTemplateBySearch = centerCustomFieldTransformer.retrieveCustomFieldTemplateBySearch(featureName);
        return new ResponseEntity<>(retrieveCustomFieldTemplateBySearch, ResponseCode.SUCCESS, "", null, true);
    }

    @RequestMapping(value = "/retrieveRecipientNames", method = RequestMethod.POST, consumes = {"application/json"})
    public Map<String, String> retrieveRecipientNames(@RequestBody String recipientIds) {
        return centerCustomFieldTransformer.retrieveRecipientNames(recipientIds);
    }

    @RequestMapping(value = "/retrieveCustomNamesOfComponentIds", method = RequestMethod.POST, consumes = {"application/json"})
    public Map<String, String> retrieveValuesOfComponentIds(@RequestBody Map<String, List<String>> customIds) {
        Map<String, String> idsWithValueName = null;
        idsWithValueName = centerCustomFieldTransformer.retrieveValuesOfComponentIds(customIds);
        return idsWithValueName;
    }

    @RequestMapping(value = "/retrieveSubEntities", method = RequestMethod.POST, consumes = {"application/json"})
    public ResponseEntity<List<Map<String, Object>>> retrieveSubEntities(@RequestBody Long fieldId) {
        List<Map<String, Object>> retrieveSubEntities = centerCustomFieldTransformer.retrieveSubEntities(fieldId);
        if (!CollectionUtils.isEmpty(retrieveSubEntities)) {
            return new ResponseEntity<>(retrieveSubEntities, ResponseCode.SUCCESS, "", null, true);
        }
        return new ResponseEntity<>(retrieveSubEntities, ResponseCode.FAILURE, "No subentities", null, true);
    }

    @RequestMapping(value = "/retrieveprerequisite", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> retrievePrerequisite() {
        Map<String, Object> prerequisiteMap = new HashMap();
        Map<String, Object> customFieldPagePrerequisite = centerCustomFieldTransformer.retrieveCustomFieldPagePrerequisite();
        prerequisiteMap.put("customfieldvalues", customFieldPagePrerequisite);
        return new ResponseEntity<>(prerequisiteMap, ResponseCode.SUCCESS, "", null, true);
    }

    @RequestMapping(value = "/retrievedesignationbasedfields", method = RequestMethod.POST, consumes = {"application/json"})
    public Map<String, List<DependentFieldDataBean>> retrieveDesignationBasedFields(@RequestBody String featureName) {
        return centerCustomFieldTransformer.retrieveDesignationBasedFieldsFromConfiguration(featureName);
    }

    @RequestMapping(value = "/retrieveusersbydesignation", method = RequestMethod.POST, consumes = {"application/json"})
    public ResponseEntity<List<SelectItem>> retrieveUsersByDesignation(@RequestBody(required = false) List<String> searchCriteria) {
        List<SelectItem> hkSelectItems = null;
        LOGGER.info("searchCriteria" + "size " + searchCriteria + searchCriteria.size());
        if (!CollectionUtils.isEmpty(searchCriteria) && searchCriteria.size() == 3) {
            String search = searchCriteria.get(0);
            String desgnation = searchCriteria.get(1);
            String isDynamic = searchCriteria.get(2);
            List<Long> designationIds = new ArrayList<>();
            if (desgnation != null) {
                int indexOf = desgnation.indexOf(",");
                if (indexOf > 0) {
                    String[] split = desgnation.split(",");
                    if (split.length > 0) {
                        for (String string : split) {
                            designationIds.add(Long.parseLong(string));
                        }
                    }
                } else {
                    designationIds.add(Long.parseLong(desgnation));
                }
            }
            LOGGER.info("search string" + search + "desgnation string" + desgnation + "isDynamic string" + isDynamic);
            if (isDynamic != null) {
                LOGGER.info("designationIds" + designationIds);
                List<SyncCenterUserDocument> centerUserDocuments = centerCustomFieldTransformer.retrieveCenterUsersByDesignation(designationIds, search);
                LOGGER.info("centerUserDocuments" + centerUserDocuments);
                hkSelectItems = centerCustomFieldTransformer.getSelectItemListFromCenterUserList(centerUserDocuments);
            }
            return new ResponseEntity<>(hkSelectItems, ResponseCode.SUCCESS, "", null);
        } else {
            return new ResponseEntity<>(null, ResponseCode.FAILURE, "", null);
        }
    }

    @RequestMapping(value = "/retrieveroles", method = RequestMethod.POST, consumes = {"application/json"})
    public ResponseEntity<List<SelectItem>> retrieveRoles(@RequestBody(required = false) String role) {

        Long companyId = loginDataBean.getCompanyId();
        List<UmDesignationDocument> uMRoles = centerCustomFieldTransformer.retrieveRolesByCompanyByStatus(companyId, role, Boolean.TRUE);
        List<SelectItem> hkSelectItems = new ArrayList<>();
        if (!CollectionUtils.isEmpty(uMRoles)) {
            for (UmDesignationDocument uMRole : uMRoles) {
                hkSelectItems.add(new SelectItem(uMRole.getId(), uMRole.getName(), HkSystemConstantUtil.RecipientCodeType.DESIGNATION));
            }
        }
        return new ResponseEntity<>(hkSelectItems, ResponseCode.SUCCESS, "", null);
    }

    @RequestMapping(value = "/defaultSelection", method = RequestMethod.POST, consumes = {"application/json"})
    public ResponseEntity<Map<String, String>> defaultSelection(@RequestBody List<String> user) {
        return new ResponseEntity<>(centerCustomFieldTransformer.retrieveRecipientNames(user), ResponseCode.SUCCESS, "", null);
    }

    @RequestMapping(value = "/retrieveDepartmentList", method = RequestMethod.POST, consumes = {"application/json"})
    public ResponseEntity<List<SelectItem>> retrieveDepartmentList(@RequestBody(required = false) String department) {

        Long companyId = loginDataBean.getCompanyId();
        List<HkDepartmentDocument> uMDepartments = centerCustomFieldTransformer.retrieveDepartmentsByCompanyByStatus(companyId, department, Boolean.TRUE);
        List<SelectItem> hkSelectItems = new ArrayList<>();
        if (!CollectionUtils.isEmpty(uMDepartments)) {
            for (HkDepartmentDocument uMDepartment : uMDepartments) {
                hkSelectItems.add(new SelectItem(uMDepartment.getId(), uMDepartment.getDeptName(), HkSystemConstantUtil.RecipientCodeType.DEPARTMENT));
            }
        }
        return new ResponseEntity<>(hkSelectItems, ResponseCode.SUCCESS, "", null);
    }

    @RequestMapping(value = "/retrieveusers", method = RequestMethod.POST, consumes = {"application/json"})
    public ResponseEntity<List<SelectItem>> retrieveUsers(@RequestBody(required = false) String user) {
        Long companyId = loginDataBean.getCompanyId();
        List<SyncCenterUserDocument> umUsers = centerCustomFieldTransformer.retrieveUsersByCompanyByStatus(companyId, user, Boolean.TRUE);
        List<SelectItem> hkSelectItems = centerCustomFieldTransformer.getSelectItemListFromUserList(umUsers);
        return new ResponseEntity<>(hkSelectItems, ResponseCode.SUCCESS, "", null);
    }

    @RequestMapping(value = "/retrieveusersByDept", method = RequestMethod.POST, consumes = {"application/json"})
    @ResponseBody
    public ResponseEntity<List<SelectItem>> retrieveUsersByDept(@RequestBody(required = false) Map details) {
        String search = details.get("search").toString();
        String departmentIds = details.get("deptIds").toString();
        List<Long> deptIds = new ArrayList<>();
        if (departmentIds != null) {
            String[] deparr = departmentIds.split(",");
            for (String dep : deparr) {
                deptIds.add(Long.parseLong(dep));
            }
        }
        List<SyncCenterUserDocument> umUsers = centerCustomFieldTransformer.retrieveUsersbyDepartment(deptIds, loginDataBean.getCompanyId(), search, Boolean.TRUE);
        List<SelectItem> hkSelectItems = centerCustomFieldTransformer.getSelectItemListFromUserList(umUsers);
        return new ResponseEntity<>(hkSelectItems, ResponseCode.SUCCESS, "", null);
    }

    @RequestMapping(value = "/retrieveusersByDesg", method = RequestMethod.POST, consumes = {"application/json"})
    @ResponseBody
    public ResponseEntity<List<SelectItem>> retrieveUsersByDesg(@RequestBody(required = false) Map details) {
        String search = details.get("search").toString();
        String departmentIds = details.get("desgIds").toString();
        List<Long> deptIds = new ArrayList<>();
        if (departmentIds != null) {
            String[] deparr = departmentIds.split(",");
            for (String dep : deparr) {
                deptIds.add(Long.parseLong(dep));
            }
        }
        List<SyncCenterUserDocument> umUsers = centerCustomFieldTransformer.retrieveUsersbyRole(deptIds, loginDataBean.getCompanyId(), search, Boolean.TRUE);
        List<SelectItem> hkSelectItems = centerCustomFieldTransformer.getSelectItemListFromUserList(umUsers);
        return new ResponseEntity<>(hkSelectItems, ResponseCode.SUCCESS, "", null);
    }

    @RequestMapping(value = "/retrieveValueNamesForSelect", method = RequestMethod.GET)
    public String retrieveValueNameForSelect(@RequestParam("param") String param) {
        String retrieveValueNameForSelect = centerCustomFieldTransformer.retrieveValueNameForSelect(param);
        return retrieveValueNameForSelect;
    }

    @RequestMapping(value = "/retrieveCaratRangeNames", method = RequestMethod.GET)
    public String retrieveCaratRangeNames(@RequestParam("param") String param) {
        String retrieveCaratRangeNames = centerCustomFieldTransformer.retrieveCaratRangeNames(param);
        return retrieveCaratRangeNames;
    }

    @RequestMapping(value = "/retrieveValueNamesForMultiSelect", method = RequestMethod.GET)
    public String retrieveValueNamesForMultiSelect(@RequestParam("param") String param) {
        String retrieveValueNamesForMultiSelect = centerCustomFieldTransformer.retrieveValueNamesForMultiSelect(param);
        return retrieveValueNamesForMultiSelect;
    }

    @RequestMapping(value = "/retrieveSubEntityNames", method = RequestMethod.GET)
    public String retrieveSubEntityNames(@RequestParam("param") String param, @RequestParam("fieldId") String fieldId) {
        String retrieveSubEntityNames = centerCustomFieldTransformer.retrieveSubEntityNames(param, fieldId);
        return retrieveSubEntityNames;
    }

    // this method is for ajax call
    @RequestMapping(value = "/retrieveRecipientNamesForCustomUserMultiSelect", method = RequestMethod.GET, produces = {"application/json"})
    public Map<String, String> retrieveRecipientNamesForCustomUserMultiSelect(@RequestParam("param") String param) {
        Map<String, String> recipientNamesMap = centerCustomFieldTransformer.retrieveRecipientNames(param);
        return recipientNamesMap;
    }

    @RequestMapping(value = "/retrieveObjectIdDetailsFromMongo", method = RequestMethod.GET)
    public Map<String, String> retrieveObjectIdDetailsFromMongo(@RequestParam("param") String fieldId, @RequestParam("param2") String invoiceId, @RequestParam("param3") String parcelId, @RequestParam("param4") String lotId, @RequestParam("param5") String packetId) throws GenericDatabaseException {
        Map<String, String> customFieldMap = new HashMap<>();
        String instanceValueFromMongo = centerCustomFieldTransformer.retrieveObjectIdValueFromMongoForConstraint(Long.parseLong(fieldId), invoiceId, parcelId, lotId, packetId);
        customFieldMap.put("instanceValueFromMongo", instanceValueFromMongo);
        return customFieldMap;
    }

    @RequestMapping(value = "/retrieveValueFromMongoForFormula", method = RequestMethod.GET)
    public Map<String, String> retrieveValueFromMongoForFormula(@RequestParam("param") List<String> dbFieldList, @RequestParam("param2") String invoiceId, @RequestParam("param3") String parcelId, @RequestParam("param4") String lotId, @RequestParam("param5") String packetId) throws GenericDatabaseException {
        Map<String, String> instanceValueFromMongoForFormula = centerCustomFieldTransformer.retrieveValueFromMongoForFormula(dbFieldList, invoiceId, parcelId, lotId, packetId);
        return instanceValueFromMongoForFormula;

    }

    @RequestMapping(value = "/retrievecurrencyCodeForDynamicForm", method = RequestMethod.GET, produces = {"application/json"})
    public ResponseEntity<List<SelectItem>> retrievecurrencyCodeForDynamicForm() {
        List<SelectItem> selectItems = centerCustomFieldTransformer.retrieveAllCurrencyByLoginId();
        return new ResponseEntity<List<SelectItem>>(selectItems, ResponseCode.SUCCESS, "", null);
    }

    @RequestMapping(value = "/createDropDownListForSubEntity", method = RequestMethod.POST, consumes = {"application/json"})
    public List<SelectItem> createDropDownListForSubEntity(@RequestBody Long customFieldId) {
        List<SelectItem> dropDownListForSubEntity = centerCustomFieldTransformer.createDropDownListForSubEntity(customFieldId);
        return dropDownListForSubEntity;
    }

    @RequestMapping(value = "/makeValuesforcaraterange", method = RequestMethod.GET, produces = {"application/json"})
    public ResponseEntity<Map<Object, String>> makeValuesForCarateRange() {
        Map<Object, String> makeValuesForCarateRange = centerCustomFieldTransformer.makeValuesForCarateRange();
        return new ResponseEntity<>(makeValuesForCarateRange, ResponseCode.SUCCESS, "", null);
    }

    @RequestMapping(value = "/makevaluesformachineassets", method = RequestMethod.GET, produces = {"application/json"})
    public ResponseEntity<Map<Long, String>> makeValuesForMachineAssets() {
        Map<Long, String> makeValuesForMachineAssets = centerCustomFieldTransformer.makeValuesForMachineAssets();
        return new ResponseEntity<>(makeValuesForMachineAssets, ResponseCode.SUCCESS, "", null);
    }

    @RequestMapping(value = "/searchautogenerated", method = RequestMethod.GET, produces = {"application/json"})
    public ResponseEntity<Map<String, Object>> searchAutogenerated(@RequestParam("q") String searchString, @RequestParam("field_name") String dbFieldName, @RequestParam("page_limit") String page_limit, @RequestParam("page") String page) {
        Map<String, Object> result = null;
        Integer offSet, limit;
        limit = Integer.parseInt(page_limit);
        offSet = Integer.parseInt(page) * limit;
        if (dbFieldName != null) {
            result = centerCustomFieldTransformer.retrieveAutogeneratedFields(dbFieldName, searchString, limit, offSet);
        }
        return new ResponseEntity<>(result, ResponseCode.SUCCESS, "", null);
    }

    @RequestMapping(value = "/retrievefranchises", method = RequestMethod.POST, consumes = {"application/json"})
    public ResponseEntity<List<SelectItem>> retrieveFranchises(@RequestBody(required = false) String franchise) {

        List<UmCompanyDocument> comapnies = centerCustomFieldTransformer.searchCompaniesByName(franchise);
        List<SelectItem> hkSelectItems = new ArrayList<>();
        if (!CollectionUtils.isEmpty(comapnies)) {
            for (UmCompanyDocument umCompany : comapnies) {
                hkSelectItems.add(new SelectItem(umCompany.getId(), umCompany.getName(), HkSystemConstantUtil.RecipientCodeType.FRANCHISE));
            }
        }
        return new ResponseEntity<>(hkSelectItems, ResponseCode.SUCCESS, "", null);
    }

    @RequestMapping(value = "/retrieveExceptionsDependantOnField", method = RequestMethod.GET)
    public List<SelectItem> retrieveExceptionsDependantOnField(@RequestParam("param") String fieldId, @RequestParam("param2") String valueId) {
        System.out.println("Field ID..." + fieldId + "---val" + valueId);
        List<SelectItem> exceptionsDependantOnField = null;
        if (fieldId != null && !"undefined".equals(fieldId) && StringUtils.hasText(valueId) && !"undefined".equals(valueId) && !valueId.equals("null")) {
            System.out.println("call server frm controller");
            exceptionsDependantOnField = centerCustomFieldTransformer.retrieveExceptionsDependantOnField(Long.parseLong(fieldId), Long.parseLong(valueId));

        } else {
            exceptionsDependantOnField = centerCustomFieldTransformer.retrieveExceptionsDependantOnField(Long.parseLong(fieldId), null);

        }
        System.out.println("returm" + exceptionsDependantOnField);
        return exceptionsDependantOnField;
    }

    @RequestMapping(value = "/checkuniqueness", method = RequestMethod.POST)
    public ResponseEntity<Boolean> checkuniqueness(@RequestParam("modelValue") String modelValue, @RequestParam("modelName") String modelName, @RequestParam("featureName") String featureName) {

        return new ResponseEntity<>(centerCustomFieldTransformer.checkUniqueness(modelName, featureName, modelValue), ResponseCode.SUCCESS, null, null, false);
    }

    @RequestMapping(value = "/checkuniquenessForFields", method = RequestMethod.POST, consumes = {"application/json"})
    @ResponseBody
    public ResponseEntity<Boolean> checkuniquenessForFields(@RequestBody Map<String, String> mapOfFields, @RequestParam("modelValue") String modelValue, @RequestParam("modelName") String modelName, @RequestParam("featureName") String featureName) {
        System.out.println("mapppppp of fields" + mapOfFields);
        Map mapOfField = null;
        return new ResponseEntity<>(centerCustomFieldTransformer.checkUniquenessForFields(mapOfField, modelName, modelValue, featureName), ResponseCode.SUCCESS, null, null, false);
    }

    @RequestMapping(value = "/retrievedesignationbasedfieldsbysection", method = RequestMethod.POST, consumes = {"application/json"})
    public Map<String, List<DependentFieldDataBean>> retrieveDesignationBasedFieldsBySection(@RequestBody String[] parameters) {
        Map<String, List<DependentFieldDataBean>> resMap = null;
        if (parameters != null && parameters.length > 0) {
            String featureName = parameters[0];
            String sectionCode = parameters[1];
            System.out.println("Feature name :: "+featureName + "   section code "+sectionCode);
            resMap = centerCustomFieldTransformer.retrieveDesignationBasedFieldsFromConfigurationBySection(featureName, sectionCode);
        }
        return resMap;
    }
    
    @RequestMapping(value = "/retrieveReferenceRateForCurrencyCode", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Double>> retrieveReferenceRateForCurrencyCode(@RequestBody String currency) {
        System.out.println("Currency ::: " + currency);
        Double rate = centerCustomFieldTransformer.retrieveReferenceRateForCurrencyForCenter(currency);
        System.out.println("Rate ********************************* " + rate);
        Map<String, Double> res = new HashMap<>();
        if (rate != null) {
            res.put(currency, rate);
        }
        return new ResponseEntity<>(res, ResponseCode.SUCCESS, null, null);
    }
    
}
