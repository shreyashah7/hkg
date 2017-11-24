/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.nosql.core;

import com.argusoft.hkg.nosql.model.HkValueExceptionDocument;
import com.argusoft.hkg.core.util.CategoryType;
import com.argusoft.sync.center.model.HkAssetDocument;
import com.argusoft.sync.center.model.HkCaratRangeDocument;
import com.argusoft.sync.center.model.HkCategoryDocument;
import com.argusoft.sync.center.model.HkMasterDocument;
import com.argusoft.sync.center.model.HkMasterValueDocument;
import com.argusoft.sync.center.model.HkSystemConfigurationDocument;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 *
 * @author akta
 */
public interface HkFoundationDocumentService {

    public List<HkMasterValueDocument> retrieveMasterValueByCode(List<String> code);

    /**
     * @author Shifa Salheen
     * @param valueId
     * @return valueDocument
     */
    public HkMasterValueDocument retrieveValueEntityById(Long valueId);

    /**
     *
     * @param valueIds
     * @return list of valueDocuments
     */
    public List<HkMasterValueDocument> retrieveValueEntities(List<Long> valueIds);

    public List<HkSystemConfigurationDocument> retrieveSystemConfigurationByFranchise(Long franchise);

    public List<HkCaratRangeDocument> retrieveCaratRangeByFranchiseAndStatus(Long franchise, List<String> status, List<Long> caratId);

    public HkCategoryDocument retrieveAssetCategoryByPrefix(String categoryPrefix, Long franchise, CategoryType categoryType);

    public List<HkAssetDocument> retrieveAssets(Long category, Long companyId, boolean forIssue);

    /**
     *
     * @param masterCode
     * @param userPrecedence
     * @param companyId
     * @return
     */
    public HkMasterDocument retrieveMaster(String masterCode, short userPrecedence, Long companyId);

    public List<HkMasterDocument> retrieveMasters(long companyId, short userPrecedence, String type, Boolean isArchive);

    public List<HkMasterValueDocument> retrieveMasterValueByCodeForCustomMasters(String code, String searchQuery);

    public void saveValueExceptions(List<HkValueExceptionDocument> hkValueExceptionDocuments);

    public List<HkValueExceptionDocument> retrieveValueExceptions(Long instanceId, Long companyId);

    public List<HkValueExceptionDocument> retrieveValueExceptionsByCriteriaWithValue(Long dependentOnFieldId, Long companyId, Long selectedValueId);

    public Map<Long, HkMasterValueDocument> retrieveValueEntititiesByCriteria(Long companyId, List<Long> valueId);
    
     
     public List<HkValueExceptionDocument> retrieveValueExceptionsByCriteria(Set<Long> dependentOnFieldId, Long companyId);

    public Map<Long, HkMasterValueDocument> retrieveMapOfIdAndValueEntity(Long companyId);

    public Map<String, List<HkMasterValueDocument>> mapOfKeyIdWithValueEntities(Long companyId);

    /**
     *
     * @param featureList
     * @return map of custom field dbBaseName:customfield id mapping.
     */
    public Map<String, String> retrieveCustomfieldMasterMappingForCalc(List<Long> featureList);
}
