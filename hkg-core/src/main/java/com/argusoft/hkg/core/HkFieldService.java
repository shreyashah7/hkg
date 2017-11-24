/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.core;

import com.argusoft.hkg.model.HkFeatureSectionEntity;
import com.argusoft.hkg.model.HkFieldEntity;
import com.argusoft.hkg.model.HkFranchiseSetupEntity;
import com.argusoft.hkg.model.HkSectionEntity;
import com.argusoft.hkg.model.HkSubFormFieldEntity;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Service class for fields.
 *
 * @author Mital
 */
public interface HkFieldService {

    /**
     * This method creates the section entity and creates relationship with
     * features (if the list passed with the entity).
     *
     * @param sectionEntity The entity to be saved.
     * @param featureSet The set of feature ids with with this section is
     * associated.
     */
    public void createSection(HkSectionEntity sectionEntity, Set<Long> featureSet);

    /**
     *
     * @param map
     */
    public void createSections(Map<Long, List<HkSectionEntity>> map);

    /**
     * This method updates the section entity and updates the relationship with
     * features as well (if the list is passed with entity).
     *
     * @param sectionEntity The entity to be updated.
     * @param allFeatureSet The set of ids of all the updated features (includes
     * both - new and old, but those which are selected)
     */
    public void updateSection(HkSectionEntity sectionEntity, Set<Long> allFeatureSet);
    
    /**
     * Update multiple sections.
     * Used to update only section name in case of pseudo diamond sections
     * @param sectionEntitys list of section entities. 
     */
    public void updateSections(List<HkSectionEntity> sectionEntitys);

    /**
     * This method removes the section and associated relationship with
     * features.
     *
     * @param sectionEntity The entity to be removed.
     */
    public void removeSection(HkSectionEntity sectionEntity);
    
    /**
     * Remove multiple sections
     * @param sectionEntitys 
     */
    public void removeSections(List<HkSectionEntity> sectionEntitys);

    /**
     * This method removes the section and associated relationship with
     * features.
     *
     * @param sectionId The id of the section to be removed.
     */
    public void removeSection(Long sectionId);

    /**
     *
     * @param featureIds
     * @return
     */
    public List<HkFeatureSectionEntity> retrieveFeatureSectionEntitiesByFeatureIds(List<Long> featureIds);

    /**
     * This method retrieves the Section Entity from given id.
     *
     * @param sectionId The id of the object that is to be retrieved.
     * @param includeFeatures True if feature list is also needed, else false.
     * @return Returns the associated Section Entity object.
     */
    public HkSectionEntity retrieveSectionById(Long sectionId, boolean includeFeatures);

    /**
     * get Feature by id
     *
     * @author Shifa Salheen
     * @param fieldId
     * @return
     */
    public HkFieldEntity retrieveCustomFieldByFieldId(Long fieldId);

    /**
     * This method retrieves the map of sections and its fields. It also adds
     * the general section fields where section object will be null.
     *
     * @param featureId The id of the feature.
     * @param companyId
     * @param isCustomField True if only custom fields are required, false if
     * fixed are required. Null if both required.
     * @param setValuePattern True if validation pattern is required in fields,
     * false if it's not needed.
     * @return Returns the map of sections and its fields.
     */
    public Map<HkSectionEntity, List<HkFieldEntity>> retrieveSectionsByFeatureId(Long featureId, Long companyId, Boolean isCustomField, boolean setValuePattern);

    public Map<Long, Map<Long, Map<HkSectionEntity, List<HkFieldEntity>>>> retrieveCompanyFeatureSectionsMap(List<Long> featureIds, List<Long> companyIds);

    /**
     * This method is used to search the fields by given string.
     *
     * @param searchStr Search string for which searching needs to be done.
     * @param featureIdNameMap
     * @param companyId
     * @return Returns the map - < FeatureId, List< Final Label + Section ID >>
     * e.g. -
     * < 1, {"First Name - General - 12", "Last Name - General - 12"} >
     */
    public Map<Long, List<String>> searchFields(String searchStr, Map<Long, String> featureIdNameMap, Long companyId);

    /**
     * This method is used to search the sub-entity fields by given string.
     *
     * @Author Gautam
     * @param searchStr Search string for which searching needs to be done.
     * @param featureIdNameMap
     * @param companyId
     * @return Returns the map - < SubentityId-FeatureId, List< subEntity label + feature name
     * >> e.g. -
     * < 1-5, {" SubEntity - invoice", "SubEntity2 - invoice"} >
     */
    public Map<String, List<String>> searchSubEntityFields(String searchStr, Map<Long, String> featureIdNameMap, Long companyId);

    /**
     * This method creates/updates the field entities. Set section=null if
     * general (default) section selected. Check for field label uniqueness
     * manually as it's not checked here.
     *
     * @param featureId
     * @param sectionId
     * @param documentName
     * @param fieldList The list of field entities to be created.
     * @param isCustomFields True if fields those are custom are to be saved,
     * false if fixed fields are there.
     * @param companyId Id of the franchise associated
     * @Deprecated
     */
    public void saveFields(Long featureId, Long sectionId, String documentName, List<HkFieldEntity> fieldList, boolean isCustomFields, long companyId);

    /**
     * This method creates/updates the field entities. Set section=null if
     * general (default) section selected. Check for field label uniqueness
     * manually as it's not checked here.
     *
     * @param featureId
     * @param sectionId
     * @author Shifa
     * @param documentName
     * @param customField is the field entity to be created
     * @param isCustomFields True if fields those are custom are to be saved,
     * false if fixed fields are there.
     * @param companyId Id of the franchise associated
     */
    public void saveField(Long featureId, Long sectionId, String documentName, HkFieldEntity customField, boolean isCustomFields, long companyId);

    /**
     * This method creates unique dbfieldname i.e unique key for mongo
     * corresponding to label
     *
     * @param companyId Id of the franchise associated
     * @author Shifa Salheen
     * @param fieldList The list of field entities to be created.
     * @return a map of label as key and dbfieldname as value.
     */
    public Map<String, String> createUniqueKeyForField(List<HkFieldEntity> fieldList, Long companyId);

    /**
     * This method removes the given list of fields. No need to set the status
     * or archive field, it's managed here.
     *
     * @param fieldList The list of fields to be removed.
     */
    public void removeFields(List<HkFieldEntity> fieldList);

    /**
     * This method removes the given list of fields. No need to set the status
     * or archive field, it's managed here.
     *
     * @author Shifa Salheen
     * @param fieldId is the id of customField
     */
    public void removeFieldById(Long fieldId);

    /**
     * This method removes the list of given fields.
     *
     * @param fieldIds The ids of the fields that needs to be removed.
     * @param removedBy The userId of the person who is deleting fields.
     */
    public void removeFields(List<Long> fieldIds, Long removedBy);

    /**
     * This method retrieves the fields by given ids. Used for private purpose
     * on core but can be used outside as well.
     *
     * @param fieldIds Ids of the fields to be retrieved.
     * @param setValuePattern true if values are needed to be set in property -
     * fieldValues, false otherwise. If set to true, the field - "fieldValues"
     * will be set like this: e.g. 1|Red,2|Blue,3|Magenta
     * @return Returns the list of field entities.
     */
    public List<HkFieldEntity> retrieveFieldsByIds(List<Long> fieldIds, boolean setValuePattern);

    /**
     * This method returns the fields by given criteria.
     *
     * @param featureId The id of feature.
     * @param sectionId The id of section.
     * @param isCustomField True if custom fields are required, False otherwise.
     * Null if all are required together.
     * @param status The status of the field. Here: 'I'-Inactive, 'A'-Active
     * @param archiveStatus False if active i.e. non-archived records required,
     * True if archive i.e. deleted record to be retrieved. When null, returns
     * all records.
     * @param setValuePattern true if values are needed to be set in property -
     * fieldValues, false otherwise.
     * @param companyId
     * @return Returns the list of fields which match the given criteria.
     */
    public List<HkFieldEntity> retrieveFieldsByCriteria(Long featureId, Long sectionId, Boolean isCustomField, String status, Boolean archiveStatus, boolean setValuePattern, long companyId);

    /**
     * This method retrieves the fields by given feature list
     *
     * @param featureIds the ids of feature
     * @param isCustomField True if custom fields are required, False otherwise.
     * Null if all are required together.
     * @param companyId
     * @return Returns the list of fields for the the following feature.
     */
    public List<HkFieldEntity> retrieveFieldsByFeatures(List<Long> featureIds, Boolean isCustomField, long companyId);

    /**
     * This method checks if the given field label exists in given section id.
     *
     * @param fieldLabel The label of the field.
     * @param sectionId The id of section.
     * @return Returns true if the label already exist, false otherwise.
     */
    public boolean isFieldLabelExistent(String fieldLabel, Long sectionId);

    /**
     * This method retrieves the ids of the features that have some active
     * field. Used to show the features on the right side of the existing page.
     *
     * @param isCustomField True if custom fields are required, False otherwise.
     * Null if all are required together.
     * @param companyId
     * @return Returns the list of feature ids.
     */
    public Set<Long> retrieveFeaturesForExistingFields(Boolean isCustomField, Long companyId);

    /**
     * @param companyId
     * @return list of HkFieldEntity
     */
    public List<HkFieldEntity> retrieveAllFieldsByCompanyId(Long companyId);

    /**
     * Retrieve list of custom fields by company Id
     *
     * @param companyId
     * @return
     */
    public List<HkFieldEntity> retrieveAllCustomFieldsByCompanyId(Long companyId);

    /**
     * @param companyId
     * @return list of HkFieldEntity whose dependant field is set to true
     *
     * @author Shifa
     */
    public List<HkFieldEntity> retrieveAllDependantFieldsByCompanyId(Long companyId);

    /**
     * @param hkFieldEntityList list of hkFieldEntityList
     */
    public void createAllFields(List<HkFieldEntity> hkFieldEntityList);

    /**
     * @param featureId
     * @param componentType
     * @return list of HkFieldEntity
     */
    public List<HkFieldEntity> retrieveAllFieldsByFeatureId(Long featureId, String componentType);

    /**
     * @param listOfFieldTypes
     * @param componentTypes
     * @return map
     */
    public Map<Long, List<HkFieldEntity>> retrieveFeatureWiseCustomFieldList(List<String> listOfFieldTypes, List<String> componentTypes);

    /**
     * @Author dhwani
     * @param fieldEntitys list of HkFieldEntity to store
     */
    public void createFields(List<HkFieldEntity> fieldEntitys);

    /**
     * @return set of hksubEntities
     * @Author Shifa
     * @param hkFieldEntity is the fieldEntity
     */
    public Set<HkSubFormFieldEntity> retrieveSetOfSubEntitiesFromFieldId(HkFieldEntity hkFieldEntity);

    /**
     * @return list of hksubEntities
     * @Author Shifa
     * @param companyId is the franchise id
     */
    public List<String> retrieveAllSubEntitiesDbFieldName(Long companyId);

    /**
     * Retrieve list of sub form entities by field ids
     *
     * @param fieldIds
     * @return
     */
    public List<HkSubFormFieldEntity> retrieveSetOfSubEntitiesByListOfFieldIds(List<Long> fieldIds);

    /**
     * @return set of hksubEntities
     * @Author Shifa
     * @param hkFieldEntity is the fieldEntity
     */
    public List<HkSubFormFieldEntity> retrieveListOfSubEntitiesAssociatedWithFieldId(HkFieldEntity hkFieldEntity);

    /**
     * @return hksubEntity
     * @Author Shifa
     * @param hkSubFormFieldId is the id of subfieldEntity
     */
    public List<HkSubFormFieldEntity> retrieveSubFormEntityById(List<Long> hkSubFormFieldId);

    /**
     *
     * @Author Shifa
     * @param hkSubFormFieldEntity is the subfieldEntity
     */
    public void saveSubEntityField(List<HkSubFormFieldEntity> hkSubFormFieldEntity);

    /**
     *
     *
     * @Author Shifa
     * @param fieldIds is the fieldIds which needs to be discarded
     * @param dbSubEntityName is the subfieldEntity name which is mongoKey
     * @param companyId is the id of franchise
     * @return the number of sunEntities matching with the input name
     */
    public int uniqueSubEntityName(String dbSubEntityName, Long companyId, List<Long> fieldIds);

    /**
     *
     * @param companyId
     * @Author Shifa
     * @param dbFieldName is the unique name of customfield which is mongoKey
     *
     * @return the map with dbFieldName as key and HkFieldEntity as value
     */
    public Map<String, HkFieldEntity> retrieveMapOfDBFieldNameWithEntity(List<String> dbFieldName, Long companyId);

    /**
     *
     * @param companyId is the companyId of the franchise
     * @param featureId is the feature Id
     * @param componentType is the type of custom field component
     * @param isArchive
     * @return the list of hkfieldEntity
     * @author Shifa
     */
    public List<HkFieldEntity> retrieveFieldByFeatureAndComponentType(Long companyId, Long featureId, String componentType, Boolean isArchive);

    /**
     * This method retrieves a map of fieldname withe entity for all the
     * entities whose formula contains the feature
     *
     * @param companyId is the companyId of franchise
     * @param featureName is the featurename
     * @param componentType is the type of custom field component
     * @author Shifa Salheen
     * @return
     */
    public Map<String, HkFieldEntity> retrieveMapOfDbFieldNameForFeatureInvolvedInFormula(Long companyId, String featureName, String componentType);

    /**
     * @author Shifa Salheen
     * @return a map of key as fieldId and value as featureId
     */
    public Map<Long, HkFieldEntity> retrieveMapOfFieldIdAndHkFieldEntity();

    /**
     * This method is used for pointer where we need the fieldValues associated
     * with fieldId fieldValues is a transient field
     *
     * @author Shifa Salheen
     * @return
     */
    public Map<Long, String> retrieveMapOfFieldIdAndValueMasterDetailAssociated();

    public Map<String, String> retrieveUIFieldNameWithComponentTypes(List<String> UIFieldList);

//    public Map<String, String> retrieveCustomUIFieldNameWithComponentTypes(Long featureId, Long companyId);
    /**
     * This method is used to retrieve a map of feature name with dbfield list
     *
     * @author Shifa
     * @param featureId is the feature Id
     * @param franchiseId is the company Id
     * @param featureType is the type of feature
     * @param componentType is the type of component
     * @return map with key as dbfield name and value as formula without
     * delimiter
     * @author Shifa Salheen
     */
    public Map<String, String> retrieveFeatureNameWithDbFieldListForFormula(Long featureId, Long franchiseId, String featureType, String componentType);

    /**
     * Retrieve franchise setup information by franchise id
     *
     * @param franchise
     * @param setupItem
     * @return
     */
    public HkFranchiseSetupEntity retrieveFranchiseSetupByFranchiseSetupItem(Long franchise, String setupItem);

    /**
     * Create franchise setup information
     *
     * @param franchiseSetupEntity
     */
    public void createOrUpdateFranchiseSetup(HkFranchiseSetupEntity franchiseSetupEntity);

    /**
     *
     * @author Shifa Salheen
     * @return map of sectionName Id Map
     */
    public Map<String, Long> sectionNameIdMap();

    /**
     *
     * @author Shifa Salheen
     * @return map of feature Name with list of sections associated with feature
     */
    public Map<String, List<String>> featureWithSectionDetails();

    /**
     *
     * @param isSection flag to check whether to bring section related custom
     * field or feature related
     * @param sectionId is section Id
     * @param featureId is the featureId
     * @author Shifa Salheen
     * @param companyId
     * @return
     */
    public List<HkFieldEntity> retrieveFeatureOrSectionRelatedCustomFields(Boolean isSection, Long sectionId, Long featureId, Long companyId);

    /**
     *
     * @param sequenceNumberWithFieldId is a map where key is fieldId and value
     * is sequence number
     * @author Shifa Salheen
     */
    public void updateSequenceNumber(Map<Long, String> sequenceNumberWithFieldId);

    /**
     *
     * @param companyId is the frachise id return a list of dbfieldnames without
     * seperator i.e. without component type and field type
     * @author Shifa Salheen
     */
    public List<String> retrieveDbFieldNamesWithoutSeperatorByCompanyId(Long companyId);

    public int retrieveNumberOfLabelsForFeature(String label, Long companyId, Long featureId, Boolean isUpdate, Long fieldId);

    /**
     * This method retrieves custom fields which have constraints
     *
     * @return a map with key as fieldId and values as constraintValue
     */
    public Map<Long, String> retrieveCustomFieldsWithConstraintsValue();

    /**
     *
     * @param componentTypes
     * @param companyId
     * @return
     */
    public List<HkFieldEntity> retrieveCustomFieldEntitysByComponentTypes(List<String> componentTypes, Long companyId, List<Long> featureIds);

    public List<HkFieldEntity> retrieveCustomFieldByFeatureAndComponentTypes(Long companyId, Long featureId, List<String> componentTypes, String search);
    public List<HkFieldEntity> retrieveFieldByFieldLabel(String label, Long featureId);
}
