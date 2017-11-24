/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.nosql.core;

import com.argusoft.hkg.nosql.model.HkCalcBasPriceDocument;
import com.argusoft.hkg.nosql.model.HkCalcFourCDiscountDocument;
import com.argusoft.hkg.nosql.model.HkCalcMasterDocument;
import com.argusoft.hkg.nosql.model.HkCalcRateDetailDocument;
import java.util.List;
import java.util.Map;
import org.bson.types.ObjectId;

/**
 *
 * @author shruti
 */
public interface HkRapCalcService {

    public List<HkCalcMasterDocument> getCalcMastersyMasterCode(String masterCode);
    public HkCalcMasterDocument getCalcMastersyMasterCodeAndValue(String masterCode, String value);
    public HkCalcMasterDocument getCalcMastersyMasterCodeAndMasterValueId(String masterCode, Long value);
    public HkCalcMasterDocument getCalcMastersyMasterGroupNameAndValue(String groupName, String value);

    public void saveCalcMasterDocument(HkCalcMasterDocument calcMasterDocument);

    public void deleteCalcMasterDocument(ObjectId id);

    public HkCalcRateDetailDocument getCalcRateDetailDocumentsByGroupName(String groupName);

    public void save(HkCalcRateDetailDocument calcRateDetailDocument);

    public HkCalcBasPriceDocument retrieveCalcBasePriceDocument(Double carat, Long shape, Long quality, Long color);

    public List<HkCalcRateDetailDocument> retrieveCalcRateDetailDocument(Map<String, Object> fieldValueMap);

    public List<HkCalcRateDetailDocument> retrieveCalcRateDetailDocumentForRange(Map<String, List<Object>> rangeMap);

    public HkCalcFourCDiscountDocument retrieveFourCDiscountDocument(Double carat, Long shape, Long quality, Long color);

    public HkCalcBasPriceDocument retrieveCalcBasePriceDocument(Double carat, List<Long> shape, List<Long> quality, List<Long> color);

    public HkCalcMasterDocument retrieveLastCalcMasterDocument();

}
