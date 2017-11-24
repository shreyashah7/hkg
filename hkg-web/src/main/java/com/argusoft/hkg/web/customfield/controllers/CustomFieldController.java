/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.customfield.controllers;

import com.argusoft.generic.database.exception.GenericDatabaseException;
import com.argusoft.hkg.common.constantutil.HkSystemConstantUtil;
import com.argusoft.hkg.nosql.model.HkSubFormValueDocument;
import com.argusoft.hkg.web.base.PrimaryKey;
import com.argusoft.hkg.web.base.ResponseCode;
import com.argusoft.hkg.web.base.ResponseEntity;
import com.argusoft.hkg.web.base.controllers.BaseController;
import com.argusoft.hkg.web.center.usermanagement.databeans.LoginDataBean;
import com.argusoft.hkg.web.center.util.ApplicationUtil;
import com.argusoft.hkg.web.customfield.databeans.CustomFieldDataBean;
import com.argusoft.hkg.web.customfield.databeans.CustomFieldInfoDataBean;
import com.argusoft.hkg.web.customfield.databeans.DependentFieldDataBean;
import com.argusoft.hkg.web.customfield.databeans.FeatureDetailDataBean;
import com.argusoft.hkg.web.customfield.databeans.SubEntityValueDataBean;
import com.argusoft.hkg.web.customfield.databeans.SubEntityValueExceptionDataBean;
import com.argusoft.hkg.web.customfield.transformers.CustomFieldTransformerBean;
import com.argusoft.hkg.web.usermanagement.databeans.DepartmentDataBean;
import com.argusoft.hkg.web.usermanagement.databeans.DesignationDataBean;
import com.argusoft.hkg.web.usermanagement.transformers.DepartmentTransformerBean;
import com.argusoft.hkg.web.usermanagement.transformers.DesignationTransformerBean;
import com.argusoft.hkg.web.util.HkSelect2DataBean;
import com.argusoft.hkg.web.util.SelectItem;
import com.argusoft.sync.center.model.SyncCenterUserDocument;
import com.argusoft.usermanagement.common.model.UMCompany;
import com.argusoft.usermanagement.common.model.UMDepartment;
import com.argusoft.usermanagement.common.model.UMRole;
import com.argusoft.usermanagement.common.model.UMUser;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
 * @author akta
 */
@RestController
@RequestMapping("/customfield")
public class CustomFieldController extends BaseController<CustomFieldInfoDataBean, Long> {

    @Autowired
    private CustomFieldTransformerBean customFieldTransformerBean;
    @Autowired
    private ApplicationUtil applicationUtil;
    @Autowired
    private LoginDataBean loginDataBean;
    @Autowired
    DepartmentTransformerBean departmentTransformerBean;
    @Autowired
    DesignationTransformerBean designationTransformerBean;
    private final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @RequestMapping(value = "/retrievefeatures", method = RequestMethod.GET)
    public Map<String, Object> retrieveFeatures() {
        return customFieldTransformerBean.retrieveCustomFieldPagePrerequisite();
    }
// Method added by Shifa to temporarily provide the list of customfields which will be accessible to a feature

    @RequestMapping(value = "/retriveAccesibleCustomFieldForFeature", method = RequestMethod.POST)
    public Map<String, Object> retriveAccesibleCustomFieldForFeature() {
        Map<String, Object> customMap = new HashMap<>();
        List<CustomFieldDataBean> customFieldList = new ArrayList<>();
        CustomFieldDataBean customFieldDataBean1 = new CustomFieldDataBean();
        customFieldDataBean1.setLabel("multiselect");
        customFieldDataBean1.setSeqNo(1);
        customFieldDataBean1.setType("number");

        customFieldList.add(customFieldDataBean1);
        CustomFieldDataBean customFieldDataBean2 = new CustomFieldDataBean();
        customFieldDataBean2.setLabel("Dear");
        customFieldDataBean1.setSeqNo(2);
        customFieldDataBean2.setType("multiSelect");
        customFieldList.add(customFieldDataBean2);
        customMap.put("genralSection", customFieldList);
        return customMap;

    }

    @RequestMapping(value = "/searchCustomFields", method = RequestMethod.GET)
    public Map<String, String> retrieveEntitiesWithCustomFieldsForFormula(@RequestParam("search") String search) {
        Map<String, String> suggestedOption = customFieldTransformerBean.retrieveEntitiesWithCustomFieldsForFormula(search);
        return suggestedOption;
    }

    @RequestMapping(value = "/searchCustomFieldsForPointer", method = RequestMethod.GET)
    public Map<Long, String> retrieveEntitiesWithCustomFieldsForPointer(@RequestParam("search") String search) {
        Map<Long, String> suggestedOption = customFieldTransformerBean.retrieveEntitiesWithCustomFieldsForPointer(search);
        return suggestedOption;
    }

    @RequestMapping(value = "/searchCustomFieldsForConstraints", method = RequestMethod.GET)
    public Map<Long, String> retrieveEntitiesWithCustomFieldsForConstraints(@RequestParam("search") String search) {
        String searchArray[] = search.split("_");
        String searchType = searchArray[0];
        String searchParam = searchArray[1];
        Map<Long, String> suggestedOption = customFieldTransformerBean.retrieveEntitiesWithCustomFieldsForConstraints(searchType, searchParam);
        return suggestedOption;
    }

    @RequestMapping(value = "/retrieveObjectIdDetailsFromMongo", method = RequestMethod.GET)
    public Map<String, String> retrieveObjectIdDetailsFromMongo(@RequestParam("param") String fieldId, @RequestParam("param2") String invoiceId, @RequestParam("param3") String parcelId, @RequestParam("param4") String lotId, @RequestParam("param5") String packetId) throws GenericDatabaseException {
        Map<String, String> customFieldMap = new HashMap<>();
        String instanceValueFromMongo = customFieldTransformerBean.retrieveObjectIdValueFromMongoForConstraint(Long.parseLong(fieldId), invoiceId, parcelId, lotId, packetId);
        customFieldMap.put("instanceValueFromMongo", instanceValueFromMongo);
        return customFieldMap;

    }

    @RequestMapping(value = "/retrieveValueNamesForMultiSelect", method = RequestMethod.GET)
    public List<String> retrieveValueNamesForMultiSelect(@RequestParam("param") String param) {

        List<String> valueList = customFieldTransformerBean.retrieveValueNamesForMultiSelect(param);
        return valueList;
    }

//    @RequestMapping(value = "/retrieveValueNamesForSelect", method = RequestMethod.GET)
//    public String retrieveValueNameForSelect(@RequestParam("param") String param) {
//        String retrieveValueNameForSelect = customFieldTransformerBean.retrieveValueNameForSelect(param);
//        return retrieveValueNameForSelect;
//    }
    @RequestMapping(value = "/retrieveSubEntityNames", method = RequestMethod.GET)
    public String retrieveSubEntityNames(@RequestParam("param") String param, @RequestParam("fieldId") String fieldId) {
        String retrieveSubEntityNames = customFieldTransformerBean.retrieveSubEntityNames(param, fieldId);
        return retrieveSubEntityNames;
    }

    @RequestMapping(value = "/retrieveTreeViewFeatures", method = RequestMethod.GET)
    public List<FeatureDetailDataBean> retrieveTreeViewFeatures() {

        List<FeatureDetailDataBean> treeViewFeatures = customFieldTransformerBean.retrieveTreeViewFeatures();
        return treeViewFeatures;
    }

    @RequestMapping(value = "/retrieveSubentityTreeViewFeatures", method = RequestMethod.GET)
    public List<FeatureDetailDataBean> retrieveSubEntityTreeViewFeatures() {

        List<FeatureDetailDataBean> treeViewFeatures = customFieldTransformerBean.retrieveSubentityTreeViewFeatures();
        return treeViewFeatures;
    }

    @RequestMapping(value = "/retrieveFeatureOrSectionCustomFields", method = RequestMethod.POST)
    public List<CustomFieldDataBean> retrieveFeatureOrSectionCustomFields(@RequestBody Map<String, Object> info) {
        Boolean isSection = (Boolean) info.get("isSection");
        Long sectionId;
        Long featureId;
        if (isSection) {
            sectionId = Long.parseLong(info.get("sectionId").toString());
            featureId = Long.parseLong(info.get("featureId").toString());
        } else {
            sectionId = null;
            featureId = Long.parseLong(info.get("featureId").toString());
        }
        List<CustomFieldDataBean> customData = customFieldTransformerBean.retrieveFeatureOrSectionCustomFields(isSection, sectionId, featureId, loginDataBean.getCompanyId());
        return customData;
    }

    @RequestMapping(value = "updateSequenceNum", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    public void updateSequenceNum(@RequestBody Map<Long, String> sequenceNumWithFieldId) {
        customFieldTransformerBean.updateSequenceNumber(sequenceNumWithFieldId);

    }

    @RequestMapping(value = "/retrieveValueFromMongoForFormula", method = RequestMethod.GET)
    public Map<String, String> retrieveValueFromMongoForFormula(@RequestParam("param") List<String> dbFieldList, @RequestParam("param2") String invoiceId, @RequestParam("param3") String parcelId, @RequestParam("param4") String lotId, @RequestParam("param5") String packetId) throws GenericDatabaseException {
        Map<String, String> instanceValueFromMongoForFormula = customFieldTransformerBean.retrieveValueFromMongoForFormula(dbFieldList, invoiceId, parcelId, lotId, packetId);
        return instanceValueFromMongoForFormula;

    }

    @RequestMapping(value = "/retrieveFeatureNameMapOfDbFieldList", method = RequestMethod.GET)
    public Map<String, String> retrieveFeatureNameMapOfDbFieldList(@RequestParam("param") List<String> dbFieldList) throws GenericDatabaseException {
        Map<String, String> dbListWithFeatureName = customFieldTransformerBean.retrieveFeatureNameMapOfDbFieldList(dbFieldList);
        return dbListWithFeatureName;

    }

    @RequestMapping(value = "/retrievesectionandcustomfieldsbyid", method = RequestMethod.POST, consumes = {"application/json"})
    public Map<String, Object> retrieveSectionAndCustomFieldByFeatureId(@RequestBody Long featureId) {
        return customFieldTransformerBean.retrieveSectionAndCustomFieldByFeatureId(featureId);
    }

    @RequestMapping(value = "/retrievesectionandcustomfields", method = RequestMethod.POST, consumes = {"application/json"})
    public Object retrieveSectionAndCustomFieldByFeatureName(@RequestBody String featureName) {
        Map<String, Object> customFieldMap = new HashMap<>();
//        Map<String, Object> get0 = applicationUtil.getFeatureFromTemplateMap().get(0l);
        Map<String, Map<String, Object>> get1 = applicationUtil.getFeatureFromTemplateMap().get(loginDataBean.getCompanyId());
        if (get1 != null) {
            return get1.get(featureName);
        }
        return customFieldMap;
//        if (get0 != null) {
//            customFieldMap.putAll(get0);
//        }
//        if (get1 != null) {
//            customFieldMap.putAll(get1);
//        }
//        return customFieldMap.get(applicationUtil.getFeatureFromTemplateMap().get(loginDataBean.getCompanyId()));
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
            if (!CollectionUtils.isEmpty(applicationUtil.getFeatureFromTemplateMap())) {
                result.put("customFieldData", applicationUtil.getFeatureFromTemplateMap().get(loginDataBean.getCompanyId()));
            }
            return result;
        }
        return new HashMap<>();
    }

    @RequestMapping(value = "/retrievesectionandcustomfieldtemplate", method = RequestMethod.POST, consumes = {"application/json"})
    public Map<String, Object> retrieveSectionAndCustomFieldTemplateByFeatureName(@RequestBody String featureName) {
        return customFieldTransformerBean.retrieveSectionAndCustomFieldTemplateByFeatureName(featureName);
    }

    @RequestMapping(value = "/retrieveRecipientNames", method = RequestMethod.POST, consumes = {"application/json"})
    public Map<String, String> retrieveRecipientNames(@RequestBody String recipientIds) {
        return customFieldTransformerBean.retrieveRecipientNames(recipientIds);
    }

    // this method is for ajax call
    @RequestMapping(value = "/retrieveRecipientNamesForCustomUserMultiSelect", method = RequestMethod.GET, produces = {"application/json"})
    public Map<String, String> retrieveRecipientNamesForCustomUserMultiSelect(@RequestParam("param") String param) {
        Map<String, String> recipientNamesMap = customFieldTransformerBean.retrieveRecipientNames(param);
        return recipientNamesMap;
    }

    @Override
    public ResponseEntity<List<CustomFieldInfoDataBean>> retrieveAll() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ResponseEntity<CustomFieldInfoDataBean> retrieveById(PrimaryKey<Long> primaryKey) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @RequestMapping(value = "/retriveCustomFieldById", method = RequestMethod.POST, produces = {"application/json"})
    public CustomFieldInfoDataBean retriveCustomFieldById(@RequestBody Long id) {

        CustomFieldInfoDataBean customInfoDataBean = customFieldTransformerBean.retrieveFieldById(id);
        return customInfoDataBean;

    }

    @RequestMapping(value = "/checkFieldIsInvolvedInOtherFields", method = RequestMethod.POST, produces = {"application/json"})
    public Map<String, Boolean> checkFieldIsInvolvedInOtherFields(@RequestBody Long id) {
        Map<String, Boolean> fieldInvolved = new HashMap<>();
        Boolean result = customFieldTransformerBean.checkFieldIsInvolvedInOtherFields(id);
        fieldInvolved.put("fieldInvolved", result);
        return fieldInvolved;

    }

    @RequestMapping(value = "/remove", method = RequestMethod.POST, produces = {"application/json"})
    public Boolean removeCustomFieldById(@RequestBody Long id) {

        Boolean result = customFieldTransformerBean.removeCustomFieldById(id);
        return result;

    }

    @Override
    public ResponseEntity<PrimaryKey<Long>> update(CustomFieldInfoDataBean t) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @RequestMapping(value = "/createcustomfields", method = RequestMethod.POST, consumes = {"application/json"})
    public Map<String, Long> createCustomFields(@RequestBody CustomFieldInfoDataBean customFieldInfoDataBean) throws GenericDatabaseException {
        return customFieldTransformerBean.createCustomFields(customFieldInfoDataBean);
    }

    @RequestMapping(value = "/createcustomfield", method = RequestMethod.POST, consumes = {"application/json"})
    public Map<String, Boolean> createCustomField(@RequestBody CustomFieldInfoDataBean customFieldInfoDataBean) throws GenericDatabaseException {

        Boolean labelAlreadyExist = customFieldTransformerBean.createCustomField(customFieldInfoDataBean, false, null, null);
        Map<String, Boolean> labelAlreadyExistMap = new HashMap<>();
        labelAlreadyExistMap.put("IslabelExist", labelAlreadyExist);
        return labelAlreadyExistMap;
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET, produces = {"application/json"})
    public List<HkSelect2DataBean> search(@RequestParam("q") String searchString) throws GenericDatabaseException {
        return customFieldTransformerBean.search(searchString);
    }

    @RequestMapping(value = "/searchsubentity", method = RequestMethod.GET, produces = {"application/json"})
    public List<HkSelect2DataBean> searchSubEntity(@RequestParam("q") String searchString) throws GenericDatabaseException {
        return customFieldTransformerBean.searchSubEntityFields(searchString);
    }

    @Override
    public ResponseEntity<CustomFieldInfoDataBean> deleteById(PrimaryKey<Long> primaryKey) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ResponseEntity<PrimaryKey<Long>> create(CustomFieldInfoDataBean t) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ResponseEntity<Map<String, Object>> retrievePrerequisite() {
        Map<String, Object> prerequisiteMap = new HashMap();
        Map<String, Object> customFieldPagePrerequisite = customFieldTransformerBean.retrieveCustomFieldPagePrerequisite();
        prerequisiteMap.put("customfieldvalues", customFieldPagePrerequisite);
        return new ResponseEntity<>(prerequisiteMap, ResponseCode.SUCCESS, "", null, true);
    }

    @RequestMapping(value = "/retrieveCustomFieldMastersForDependantField", method = RequestMethod.POST, consumes = {"application/json"})
    public List<SelectItem> retrieveCustomFieldMastersForDependantField(@RequestBody Long featureId) {
        return customFieldTransformerBean.retrieveCustomFieldMastersForDependantField(featureId);
    }

    @RequestMapping(value = "/retrieveCustomFieldValuesForDependantField", method = RequestMethod.POST, consumes = {"application/json"})
    public List<SelectItem> retrieveCustomFieldValuesForDependantField(@RequestBody String featureId) {
        return customFieldTransformerBean.retrieveCustomFieldValuesForDependantField(featureId);
    }

    @RequestMapping(value = "/retrieveSubEntities", method = RequestMethod.POST, consumes = {"application/json"})
    public ResponseEntity<List<Map<String, Object>>> retrieveSubEntities(@RequestBody Long fieldId) {
        List<Map<String, Object>> retrieveSubEntities = customFieldTransformerBean.retrieveSubEntities(fieldId);
        if (!CollectionUtils.isEmpty(retrieveSubEntities)) {
            return new ResponseEntity<>(retrieveSubEntities, ResponseCode.SUCCESS, "", null, true);
        }
        return new ResponseEntity<>(retrieveSubEntities, ResponseCode.FAILURE, "No fields for the subentity", null, true);
    }

    @RequestMapping(value = "/retrieveCustomFieldOfTypeSubEntityByFeatureId", method = RequestMethod.POST, consumes = {"application/json"})
    public List<SelectItem> retrieveCustomFieldOfTypeSubEntityByFeatureId(@RequestBody Long featureId) {
        List<SelectItem> listOfSubEntities = customFieldTransformerBean.retrieveCustomFieldOfTypeSubEntityByFeatureId(featureId);
        return listOfSubEntities;
    }

    @RequestMapping(value = "/createDropDownListForSubEntity", method = RequestMethod.POST, consumes = {"application/json"})
    public List<SelectItem> createDropDownListForSubEntity(@RequestBody Long customFieldId) {
        List<SelectItem> dropDownListForSubEntity = customFieldTransformerBean.createDropDownListForSubEntity(customFieldId);
        return dropDownListForSubEntity;
    }

    @RequestMapping(value = "/saveSubEntitiesValue", method = RequestMethod.POST, consumes = {"application/json"})
    public ResponseEntity<List<Map<String, Object>>> saveSubEntitiesValue(@RequestBody List<SubEntityValueDataBean> SubEntityValueDataBean) {
//        try {
        customFieldTransformerBean.saveSubEntitiesValue(SubEntityValueDataBean);
        return new ResponseEntity<>(null, ResponseCode.SUCCESS, "SubEntities values added successfully", null, true);
//        } 
//        catch (Exception e) {
//            return new ResponseEntity<>(null, ResponseCode.FAILURE, "Failed to add the subentities values", null, true);
//        }
    }

    @RequestMapping(value = "/retrievelistofSubEntitiesValuesByInstanceId", method = RequestMethod.POST, consumes = {"application/json"})
    public List<SubEntityValueDataBean> retrievelistofSubEntitiesValuesByInstanceId(@RequestBody Long instanceId) {
        List<HkSubFormValueDocument> lisOfhkSubFormValueDocuments = customFieldTransformerBean.retrieveSubFormValueByInstanceid(instanceId);
        List<SubEntityValueDataBean> subEntityValueDataBeans = customFieldTransformerBean.convertHkSubFormValueDocumentIntoSubEntity(lisOfhkSubFormValueDocuments);

        return subEntityValueDataBeans;
    }

    @RequestMapping(value = "/retrievedesignationbasedfields", method = RequestMethod.POST, consumes = {"application/json"})
    public Map<String, List<DependentFieldDataBean>> retrieveDesignationBasedFields(@RequestBody String featureName) {
        return customFieldTransformerBean.retrieveDesignationBasedFieldsFromConfiguration(featureName);
    }

    @RequestMapping(value = "/retrieveusers", method = RequestMethod.POST, consumes = {"application/json"})
    @ResponseBody
    public ResponseEntity<List<SelectItem>> retrieveUsers(@RequestBody(required = false) String user) {
        Long companyId = loginDataBean.getCompanyId();
//        System.out.println("Company Id.." + companyId + "user" + user);
        List<UMUser> umUsers = customFieldTransformerBean.retrieveUsersByCompanyByStatus(companyId, user, Boolean.TRUE);
        List<SelectItem> hkSelectItems = customFieldTransformerBean.getSelectItemListFromUserList(umUsers);
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
        List<UMUser> umUsers = customFieldTransformerBean.retrieveUsersByCompanyByDepartments(search, deptIds);
        List<SelectItem> hkSelectItems = customFieldTransformerBean.getSelectItemListFromUserList(umUsers);
        return new ResponseEntity<>(hkSelectItems, ResponseCode.SUCCESS, "", null);
    }

    @RequestMapping(value = "/retrieveusersByDesg", method = RequestMethod.POST, consumes = {"application/json"})
    @ResponseBody
    public ResponseEntity<List<SelectItem>> retrieveUsersByDesg(@RequestBody(required = false) Map details) {
        String search = details.get("search").toString();
        String designationIds = details.get("desgIds").toString();
        List<Long> desgIds = new ArrayList<>();
        if (designationIds != null) {
            String[] desgArr = designationIds.split(",");
            for (String desg : desgArr) {
                desgIds.add(Long.parseLong(desg));
            }
        }
        List<UMUser> umUsers = customFieldTransformerBean.retrieveUsersByCompanyByRoleIds(search, desgIds);
        List<SelectItem> hkSelectItems = customFieldTransformerBean.getSelectItemListFromUserList(umUsers);
        return new ResponseEntity<>(hkSelectItems, ResponseCode.SUCCESS, "", null);
    }

    @RequestMapping(value = "/retrieveDepartmentList", method = RequestMethod.POST, consumes = {"application/json"})
    public ResponseEntity<List<SelectItem>> retrieveDepartmentList(@RequestBody(required = false) String department) {

        Long companyId = loginDataBean.getCompanyId();
        List<UMDepartment> uMDepartments = customFieldTransformerBean.retrieveDepartmentsByCompanyByStatus(companyId, department, Boolean.TRUE);
        List<SelectItem> hkSelectItems = new ArrayList<>();
        if (!CollectionUtils.isEmpty(uMDepartments)) {
            for (UMDepartment uMDepartment : uMDepartments) {
                hkSelectItems.add(new SelectItem(uMDepartment.getId(), uMDepartment.getDeptName(), HkSystemConstantUtil.RecipientCodeType.DEPARTMENT));
            }
        }
        return new ResponseEntity<>(hkSelectItems, ResponseCode.SUCCESS, "", null);
    }

    @RequestMapping(value = "/defaultSelection", method = RequestMethod.POST, consumes = {"application/json"})
    public ResponseEntity<Map<String, String>> defaultSelection(@RequestBody List<String> user) {
        return new ResponseEntity<>(customFieldTransformerBean.retrieveRecipientNames(user), ResponseCode.SUCCESS, "", null);
    }

    @RequestMapping(value = "/retrieveroles", method = RequestMethod.POST, consumes = {"application/json"})
    public ResponseEntity<List<SelectItem>> retrieveRoles(@RequestBody(required = false) String role) {

        Long companyId = loginDataBean.getCompanyId();
        List<UMRole> uMRoles = customFieldTransformerBean.retrieveRolesByCompanyByStatus(companyId, role, Boolean.TRUE);
        List<SelectItem> hkSelectItems = new ArrayList<>();
        if (!CollectionUtils.isEmpty(uMRoles)) {
            for (UMRole uMRole : uMRoles) {
                hkSelectItems.add(new SelectItem(uMRole.getId(), uMRole.getName(), HkSystemConstantUtil.RecipientCodeType.DESIGNATION));
            }
        }
        return new ResponseEntity<>(hkSelectItems, ResponseCode.SUCCESS, "", null);
    }

    @RequestMapping(value = "/retrieveAllFields", method = RequestMethod.GET)
    public Map<Long, String> retrieveAllEntitiesWithCustomFields() {
        Map<Long, String> suggestedOption = customFieldTransformerBean.retrieveAllEntitiesWithCustomFields();
        return suggestedOption;
    }

    // For Currency Component
    @RequestMapping(value = "/retrievecurrencies", method = RequestMethod.GET, produces = {"application/json"})
    public ResponseEntity<List<SelectItem>> retrieveCurrencies() {
        List<SelectItem> selectItems = customFieldTransformerBean.retrieveCurrencyForCombo();
        return new ResponseEntity<List<SelectItem>>(selectItems, ResponseCode.SUCCESS, "", null);
    }

    @RequestMapping(value = "/retrievecurrencyCodeForDynamicForm", method = RequestMethod.GET, produces = {"application/json"})
    public ResponseEntity<List<SelectItem>> retrievecurrencyCodeForDynamicForm() {
        List<SelectItem> selectItems = customFieldTransformerBean.retrieveAllCurrencyByLoginId();
        return new ResponseEntity<List<SelectItem>>(selectItems, ResponseCode.SUCCESS, "", null);
    }

    @RequestMapping(value = "/retrieveusersbydesignation", method = RequestMethod.POST, consumes = {"application/json"})
    public ResponseEntity<List<SelectItem>> retrieveUsersByDesignation(@RequestBody(required = false) List<String> searchCriteria) {
        List<SelectItem> hkSelectItems = null;
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
            if (isDynamic != null) {
                if (isDynamic.equalsIgnoreCase("false")) {
                    List<UMUser> umUsers = customFieldTransformerBean.retrieveUserByDesignation(designationIds, search);
                    hkSelectItems = customFieldTransformerBean.getSelectItemListFromUserList(umUsers);
                } else {
                    List<SyncCenterUserDocument> centerUserDocuments = customFieldTransformerBean.retrieveCenterUsersByDesignation(designationIds, search);
                    hkSelectItems = customFieldTransformerBean.getSelectItemListFromCenterUserList(centerUserDocuments);
                }
            }
            return new ResponseEntity<>(hkSelectItems, ResponseCode.SUCCESS, "", null);
        } else {
            return new ResponseEntity<>(null, ResponseCode.FAILURE, "", null);
        }
    }

    @RequestMapping(value = "/makeValuesforcaraterange", method = RequestMethod.GET, produces = {"application/json"})
    public ResponseEntity<Map<Object, String>> makeValuesForCarateRange() {
        Map<Object, String> makeValuesForCarateRange = customFieldTransformerBean.makeValuesForCarateRange();
        return new ResponseEntity<>(makeValuesForCarateRange, ResponseCode.SUCCESS, "", null);
    }

    @RequestMapping(value = "/makevaluesformachineassets", method = RequestMethod.GET, produces = {"application/json"})
    public ResponseEntity<Map<Long, String>> makeValuesForMachineAssets() {
        Map<Long, String> makeValuesForMachineAssets = customFieldTransformerBean.makeValuesForMachineAssets();
        return new ResponseEntity<>(makeValuesForMachineAssets, ResponseCode.SUCCESS, "", null);
    }

    @RequestMapping(value = "/retrievefranchises", method = RequestMethod.POST, consumes = {"application/json"})
    public ResponseEntity<List<SelectItem>> retrieveFranchises(@RequestBody(required = false) String franchise) {

        List<UMCompany> comapnies = customFieldTransformerBean.searchCompaniesByName(franchise);
        List<SelectItem> hkSelectItems = new ArrayList<>();
        if (!CollectionUtils.isEmpty(comapnies)) {
            for (UMCompany umCompany : comapnies) {
                hkSelectItems.add(new SelectItem(umCompany.getId(), umCompany.getName(), HkSystemConstantUtil.RecipientCodeType.FRANCHISE));
            }
        }
        return new ResponseEntity<>(hkSelectItems, ResponseCode.SUCCESS, "", null);
    }

    @RequestMapping(value = "/retrievecustomfields", method = RequestMethod.POST, produces = {"application/json"})
    public ResponseEntity<List<SelectItem>> retrieveCustomFields(@RequestBody Long instanceId) {
        List<SelectItem> selectItems = customFieldTransformerBean.retrieveCustomFields(instanceId);
        return new ResponseEntity<>(selectItems, ResponseCode.SUCCESS, null, null, false);
    }

    @RequestMapping(value = "/retrievecustomfieldsvaluebykey", method = RequestMethod.POST, produces = {"application/json"})
    public ResponseEntity<List<SelectItem>> retrieveCustomFieldsValueByKey(@RequestBody Map payload) {
        List<SelectItem> selectItems = new ArrayList<>();
        if (!CollectionUtils.isEmpty(payload)) {
            Long fieldId = Long.parseLong(payload.get("fieldId").toString());
            String componentType = payload.get("componentType").toString();
            selectItems = customFieldTransformerBean.retrieveCustomFieldsValueByKey(fieldId, componentType);
        }
        return new ResponseEntity<>(selectItems, ResponseCode.SUCCESS, null, null, false);
    }

    @RequestMapping(value = "/saveexception", method = RequestMethod.POST, produces = {"application/json"})
    public ResponseEntity<List<SelectItem>> saveException(@RequestBody List<SubEntityValueExceptionDataBean> subEntityValueExceptionDataBeans) {
        if (!CollectionUtils.isEmpty(subEntityValueExceptionDataBeans)) {
            customFieldTransformerBean.saveException(subEntityValueExceptionDataBeans);
        }
        return new ResponseEntity<>(null, ResponseCode.SUCCESS, "Value Exception Added successfully.", null, false);
    }

    @RequestMapping(value = "/retrievevalueexceptions", method = RequestMethod.POST, produces = {"application/json"})
    public ResponseEntity<List<SubEntityValueExceptionDataBean>> retrieveValueExceptions(@RequestBody Long instanceId) {
        List<SubEntityValueExceptionDataBean> subEntityValueExceptionDataBeans = new ArrayList<>();
        if (instanceId != null) {
            subEntityValueExceptionDataBeans = customFieldTransformerBean.retrieveValueExceptions(instanceId);
        }
        return new ResponseEntity<>(subEntityValueExceptionDataBeans, ResponseCode.SUCCESS, null, null, false);
    }

    @RequestMapping(value = "/checkuniqueness", method = RequestMethod.POST)
    public ResponseEntity<Boolean> checkuniqueness(@RequestParam("modelValue") String modelValue, @RequestParam("modelName") String modelName, @RequestParam("featureName") String featureName) {

        return new ResponseEntity<>(customFieldTransformerBean.checkUniqueness(modelName, featureName, modelValue), ResponseCode.SUCCESS, null, null, false);
    }

    @RequestMapping(value = "/retrieveprerequisiteforexception", method = RequestMethod.POST, produces = {"application/json"})
    public ResponseEntity<Map<String, Object>> retrievePrerequisiteForException(@RequestBody Long instanceId) {
        Map<String, Object> prerequisiteMap = new HashMap<>();
        if (instanceId != null) {
            prerequisiteMap = customFieldTransformerBean.retrievePrerequisiteForException(instanceId);
        }
        return new ResponseEntity<>(prerequisiteMap, ResponseCode.SUCCESS, null, null, false);
    }

    @RequestMapping(value = "/retrieveExceptionsDependantOnField", method = RequestMethod.GET)
    public List<SelectItem> retrieveExceptionsDependantOnField(@RequestParam("param") String fieldId, @RequestParam("param2") String valueId) {
        List<SelectItem> exceptionsDependantOnField = null;
        if (fieldId != null && !"undefined".equals(fieldId) && StringUtils.hasText(valueId) && !"undefined".equals(valueId) && !valueId.equals("null")) {
            exceptionsDependantOnField = customFieldTransformerBean.retrieveExceptionsDependantOnField(Long.parseLong(fieldId), Long.parseLong(valueId));

        } else {
            exceptionsDependantOnField = customFieldTransformerBean.retrieveExceptionsDependantOnField(Long.parseLong(fieldId), null);

        }
        return exceptionsDependantOnField;
    }

    @RequestMapping(value = "/retrieveDept", method = RequestMethod.GET)
    public List<DepartmentDataBean> retrieveDepartmentList() {
        List<DepartmentDataBean> departments = departmentTransformerBean.retrieveDepartmentListInTreeViewSimple(loginDataBean.getId());
        return departments;
    }

    @RequestMapping(value = "/retrieveDesg", method = RequestMethod.GET)
    public List<DesignationDataBean> retrieveDesignationList() {
        List<DesignationDataBean> designations = designationTransformerBean.retrieveDesignations(true);
        return designations;
    }

    @RequestMapping(value = "/retrieveFieldForUniqueness", method = RequestMethod.POST)
    public Map<String, String> retrieveFieldsForUniqueness(@RequestBody Long featureId) {
        Map<String, String> fieldsByFeatureForUniqueness = customFieldTransformerBean.retrieveFieldsByFeatureForUniqueness(featureId);
        return fieldsByFeatureForUniqueness;
    }

    @RequestMapping(value = "/searchCustomFieldsForDate", method = RequestMethod.GET)
    public Map<String, String> retrieveNumberEntitiesForDate(@RequestParam("search") String search, @RequestParam("featureId") String feature) {
        Map<String, String> suggestedOption = null;
        if (feature != null && !"undefined".equals(feature)) {
            suggestedOption = customFieldTransformerBean.retrieveNumberEntitiesForDate(search, Long.parseLong(feature));
        }
        return suggestedOption;
    }

    @RequestMapping(value = "/retrieveDateEntities", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, String> retrieveDateEntities(@RequestBody(required = false) Map details) {
        Map<String, String> suggestedOption = null;
        String search = details.get("search").toString();
        if (details.containsKey("featureId") && details.get("featureId") != null) {
            String featureId = details.get("featureId").toString();

            if (featureId != null && !"undefined".equals(featureId)) {
                suggestedOption = customFieldTransformerBean.retrieveDateEntities(search, Long.parseLong(featureId));
            }
        }
        return suggestedOption;
    }
}
