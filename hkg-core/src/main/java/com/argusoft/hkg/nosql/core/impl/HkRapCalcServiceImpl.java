/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.nosql.core.impl;

import com.argusoft.generic.database.common.MongoGenericDao;
import com.argusoft.hkg.common.constantutil.HkSystemConstantUtil;
import static com.argusoft.hkg.common.constantutil.HkSystemConstantUtil.RAP_CALC_FIELD_MAP;
import com.argusoft.hkg.nosql.core.HkRapCalcService;
import com.argusoft.hkg.nosql.model.HkCalcBasPriceDocument;
import com.argusoft.hkg.nosql.model.HkCalcFourCDiscountDocument;
import com.argusoft.hkg.nosql.model.HkCalcMasterDocument;
import com.argusoft.hkg.nosql.model.HkCalcRateDetailDocument;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.util.CollectionUtils;

/**
 *
 * @author shruti
 */
public class HkRapCalcServiceImpl implements HkRapCalcService {

    private class CALC_MASTER_DOCUMENT {

        public static final String ID = "id";
        public static final String SEQUENCE = "sequence";
        public static final String VALUE = "value";
        public static final String CODE = "code";
        public static final String IS_ACTIVE = "isActive";
        public static final String IS_ARCHIVE = "isArchive";
        public static final String CREATED_ON = "createdOn";
        public static final String MODIFIED_ON = "modifiedOn";
        public static final String MASTER_VALUE_ID = "masterValueId";
    }

    private class CALC_RATE_DETAIL_DOCUMENT {

        public static final String ID = "id";
        public static final String DISCOUNT = "discount";
        public static final String MIX_AMOUNT = "mixamount";
        public static final String GROUP_NAME = "groupName";
        public static final String DISCOUNT_DETAIL_MAP = "discountDetailsMap";
        public static final String CREATED_ON = "craetedOn";
        public static final String MODIFIED_ON = "modifiedOn";
        public static final String IS_ACTIVE = "isActive";
        public static final String IS_ARCHIVE = "isArchive";
    }
    private class CALC_BASE_PRICE_DOCUMENT {

        public static final String ID = "id";
        public static final String LOAD_DATE = "loadDate";
        public static final String BASE_PRICE = "basePrice";
        public static final String SHAPE = "shape";
        public static final String COLOR = "color";
        public static final String MODIFIED_ON = "modifiedOn";
        public static final String IS_ACTIVE = "isActive";
        public static final String IS_ARCHIVE = "isArchive";
        public static final String QUALITY = "clarity";
        public static final String CARAT_FROM = "caratFrom";
        public static final String CARAT_TO = "caratTo";
        public static final String DISCOUNT = "discount";
    }
    private class CALC_FOURC_DOCUMENT {

        public static final String ID = "id";
        public static final String LOAD_DATE = "loadDate";
        public static final String BASE_PRICE = "basePrice";
        public static final String SHAPE = "shape";
        public static final String COLOR = "color";
        public static final String MODIFIED_ON = "modifiedOn";
        public static final String IS_ACTIVE = "isActive";
        public static final String IS_ARCHIVE = "isArchive";
        public static final String QUALITY = "quality";
        public static final String CARAT_FROM = "caratFrom";
        public static final String CARAT_TO = "caratTo";
    }

    @Autowired
    private MongoGenericDao mongoGenericDao;

    @Override
    public List<HkCalcMasterDocument> getCalcMastersyMasterCode(String masterCode) {
        List<Criteria> criterias = new LinkedList<>();
        criterias.add(where(CALC_MASTER_DOCUMENT.CODE).is(masterCode));
        criterias.add(where(CALC_MASTER_DOCUMENT.IS_ACTIVE).is(true));
        return mongoGenericDao.findByCriteria(criterias, HkCalcMasterDocument.class);
    }

    @Override
    public void saveCalcMasterDocument(HkCalcMasterDocument calcMasterDocument) {
        calcMasterDocument.setModifiedOn(new Date());
        mongoGenericDao.update(calcMasterDocument);
    }

    @Override
    public void deleteCalcMasterDocument(ObjectId id) {
        mongoGenericDao.deleteById(id);

    }

    @Override
    public HkCalcRateDetailDocument getCalcRateDetailDocumentsByGroupName(String groupName) {
        List<Criteria> criterias = new LinkedList<>();
        criterias.add(where(CALC_RATE_DETAIL_DOCUMENT.GROUP_NAME).is(groupName));
        criterias.add(where(CALC_RATE_DETAIL_DOCUMENT.IS_ACTIVE).is(true));
        return (HkCalcRateDetailDocument) mongoGenericDao.findOneByCriteria(criterias, HkCalcRateDetailDocument.class);
    }

    @Override
    public HkCalcMasterDocument getCalcMastersyMasterCodeAndValue(String masterCode, String value) {
        List<Criteria> criterias = new LinkedList<>();
        criterias.add(where(CALC_MASTER_DOCUMENT.CODE).is(masterCode));
        criterias.add(where(CALC_MASTER_DOCUMENT.VALUE).is(value));
        criterias.add(where(CALC_MASTER_DOCUMENT.IS_ACTIVE).is(true));
        return (HkCalcMasterDocument) mongoGenericDao.findOneByCriteria(criterias, HkCalcMasterDocument.class);
    }

    @Override
    public HkCalcMasterDocument getCalcMastersyMasterCodeAndMasterValueId(String masterCode, Long value) {
        List<Criteria> criterias = new LinkedList<>();
        criterias.add(where(CALC_MASTER_DOCUMENT.CODE).is(masterCode));
        criterias.add(where(CALC_MASTER_DOCUMENT.MASTER_VALUE_ID).is(value));
        criterias.add(where(CALC_MASTER_DOCUMENT.IS_ACTIVE).is(true));
        System.out.println("mongogenericdao: " + mongoGenericDao);
        return (HkCalcMasterDocument) mongoGenericDao.findOneByCriteria(criterias, HkCalcMasterDocument.class);
    }

    @Override
    public HkCalcMasterDocument getCalcMastersyMasterGroupNameAndValue(String groupName, String value) {
//        List<Criteria> criterias = new LinkedList<>();
//        criterias.add(where(CALC_MASTER_DOCUMENT.GROUP_NAME).is(groupName));
//        criterias.add(where(CALC_MASTER_DOCUMENT.VALUE).is(value));
//        criterias.add(where(CALC_MASTER_DOCUMENT.IS_ACTIVE).is(true));
//        return (HkCalcMasterDocument) mongoGenericDao.findOneByCriteria(criterias, HkCalcMasterDocument.class);
        return null;
    }

    @Override
    public void save(HkCalcRateDetailDocument calcRateDetailDocument) {
        mongoGenericDao.update(calcRateDetailDocument);
    }

    @Override
    public HkCalcBasPriceDocument retrieveCalcBasePriceDocument(Double carat, Long shape, Long quality, Long color
    //  .Date rDate
    ) {
        if (carat != null && shape != null && quality != null && color != null) {
            List<Criteria> criterias = new LinkedList<>();
//            HkCalcMasterDocument calcMaster = getCalcMastersyMasterCodeAndMasterValueId(HkSystemConstantUtil.RAP_CALC_FIELD_MAP.get(HkSystemConstantUtil.FourC.SHAPE).toString(), Long.valueOf(shape.toString()));
//            HkCalcMasterDocument colorCalcMaster = getCalcMastersyMasterCodeAndMasterValueId(HkSystemConstantUtil.RAP_CALC_FIELD_MAP.get(HkSystemConstantUtil.FourC.COLOR).toString(), Long.valueOf(color.toString()));
//            HkCalcMasterDocument qualCalcMaster = getCalcMastersyMasterCodeAndMasterValueId(HkSystemConstantUtil.RAP_CALC_FIELD_MAP.get(HkSystemConstantUtil.FourC.QUALITY).toString(), Long.valueOf(quality.toString()));

            criterias.add(new Criteria().andOperator(where(CALC_BASE_PRICE_DOCUMENT.QUALITY).is(quality),
                    where(CALC_BASE_PRICE_DOCUMENT.SHAPE).is(shape),
                    where(CALC_BASE_PRICE_DOCUMENT.COLOR).is(color),
                    where(CALC_BASE_PRICE_DOCUMENT.CARAT_FROM).lte(carat),
                    where(CALC_BASE_PRICE_DOCUMENT.CARAT_TO).gte(carat))
            //            ,where(CALC_BASE_PRICE_DOCUMENT.LOAD_DATE).lte(rDate)
            );

                       return (HkCalcBasPriceDocument) mongoGenericDao.findOneByCriteria(criterias, HkCalcBasPriceDocument.class);
        }
        return null;
    }

    @Override
    public HkCalcBasPriceDocument retrieveCalcBasePriceDocument(Double carat, List<Long> shape, List<Long> quality, List<Long> color) {
        if (carat != null && shape != null && quality != null && color != null) {
            List<Criteria> criterias = new LinkedList<>();

            criterias.add(new Criteria().andOperator(where(CALC_BASE_PRICE_DOCUMENT.QUALITY).in(quality),
                    where(CALC_BASE_PRICE_DOCUMENT.SHAPE).in(shape),
                    where(CALC_BASE_PRICE_DOCUMENT.COLOR).in(color),
                    new Criteria().orOperator(where(CALC_BASE_PRICE_DOCUMENT.CARAT_FROM).lte(carat), where(CALC_BASE_PRICE_DOCUMENT.CARAT_TO).gte(carat))));

            return (HkCalcBasPriceDocument) mongoGenericDao.findOneByCriteria(criterias, HkCalcBasPriceDocument.class);
        }
        return null;
    }

    @Override
    public HkCalcFourCDiscountDocument retrieveFourCDiscountDocument(Double carat, Long shape, Long quality, Long color
    //,    Date rDate
    ) {
        if (carat != null && shape != null && quality != null && color != null) {
            List<Criteria> criterias = new LinkedList<>();
            criterias.add(new Criteria().andOperator(where(CALC_FOURC_DOCUMENT.QUALITY).is(quality),
                    where(CALC_FOURC_DOCUMENT.SHAPE).is(shape),
                    where(CALC_FOURC_DOCUMENT.COLOR).is(color),
                    where(CALC_FOURC_DOCUMENT.CARAT_FROM).lte(carat), where(CALC_FOURC_DOCUMENT.CARAT_TO).gte(carat)
            //            ,where(CALC_FOURC_DOCUMENT.LOAD_DATE).lte(rDate)
            ));

            return (HkCalcFourCDiscountDocument) mongoGenericDao.findOneByCriteria(criterias, HkCalcFourCDiscountDocument.class);
        }
        return null;
    }

    @Override
    public List<HkCalcRateDetailDocument> retrieveCalcRateDetailDocument(Map<String, Object> fieldValueMap) {
        if (!CollectionUtils.isEmpty(fieldValueMap)) {
            List<Criteria> criterias = new LinkedList<Criteria>();
            List<Criteria> existcriteriaList = new LinkedList<Criteria>();
            Criteria[] existsCriterias;
            LinkedList<Criteria> criterias1 = new LinkedList<>();
            int i = 0;

            for (Map.Entry<String, Object> entrySet : fieldValueMap.entrySet()) {
                String key = entrySet.getKey();
                Object value = entrySet.getValue();
                if (value != null) {

                    String id = HkSystemConstantUtil.RAP_CALC_FIELD_MAP.get(key);
                    //add carat and rdate exception
                    if (key.equals(HkSystemConstantUtil.FourC.R_DATE)) {
                        //For RDate both from value shoud be same or less than givan date. For any document both from and to date values will be same.
                        criterias1.add(new Criteria().orOperator(where("discountDetailsMap." + id).exists(false), where("discountDetailsMap." + id + ".from.value").lte(value)));
                    } else if (key.equals(HkSystemConstantUtil.FourC.CARAT) || key.equals(HkSystemConstantUtil.FourC.GRAPH_CARAT)) {
                        criterias1.add(new Criteria().orOperator(where("discountDetailsMap." + id).exists(false), new Criteria().andOperator(where("discountDetailsMap." + id + ".from.value").lte(value), where("discountDetailsMap." + id + ".to.value").gte(value))));
                    } else {
                        HkCalcMasterDocument calcMaster = getCalcMastersyMasterCodeAndMasterValueId(id, Long.valueOf(value.toString()));
//                        System.out.println("calcMaster " + calcMaster);
                        if (calcMaster != null) {
                            criterias1.add(new Criteria().orOperator(where("discountDetailsMap." + id).exists(false), new Criteria().andOperator(where("discountDetailsMap." + id + ".from.sequence").lte(calcMaster.getSequence()), where("discountDetailsMap." + id + ".to.sequence").gte(calcMaster.getSequence()))));
                        }
                    }
                    existcriteriaList.add(where("discountDetailsMap." + id).exists(true));
                }
            }
            Criteria[] criArray = new Criteria[criterias1.size() + 1];
            criterias1.toArray(criArray);
            existsCriterias = new Criteria[existcriteriaList.size()];
            existcriteriaList.toArray(existsCriterias);
            criArray[criterias1.size()] = new Criteria().orOperator(existsCriterias);
            Criteria criteria = new Criteria().andOperator(criArray);
            Query query = new Query(criteria);
            System.out.println("CRITERIA:" + query.toString());
            criterias.add(criteria);
            return (List<HkCalcRateDetailDocument>) mongoGenericDao.findByCriteria(criterias, HkCalcRateDetailDocument.class);
        }
        return null;
    }

    @Override
    public List<HkCalcRateDetailDocument> retrieveCalcRateDetailDocumentForRange(Map<String, List<Object>> rangeMap) {
        if (!CollectionUtils.isEmpty(rangeMap)) {
            List<Criteria> criterias = new LinkedList<Criteria>();
            List<Criteria> existcriteriaList = new LinkedList<Criteria>();
            Criteria[] existsCriterias;
            LinkedList<Criteria> criterias1 = new LinkedList<>();
            int i = 0;

            for (Map.Entry<String, List<Object>> entrySet : rangeMap.entrySet()) {
                String key = entrySet.getKey();
                List<Object> value = entrySet.getValue();
                if (!CollectionUtils.isEmpty(value) && value.size() == 2) {

                    String id = HkSystemConstantUtil.RAP_CALC_FIELD_MAP.get(key);
                    //add carat and rdate exception

                    // for carat and rdate there will be only one value. (acc. to current requirements).
                    if (key.equals(HkSystemConstantUtil.FourC.R_DATE)) {
                        //For RDate both from and to values shoud be same and they should be equal to given value
                        criterias1.add(new Criteria().orOperator(where("discountDetailsMap." + id).exists(false), new Criteria().andOperator(where("discountDetailsMap." + id + ".from.value").lte(value.get(0)), where("discountDetailsMap." + id + ".to.value").lte(value.get(0)))));
                    } else if (key.equals(HkSystemConstantUtil.FourC.CARAT) || key.equals(HkSystemConstantUtil.FourC.GRAPH_CARAT)) {
                        criterias1.add(new Criteria().orOperator(where("discountDetailsMap." + id).exists(false), new Criteria().andOperator(where("discountDetailsMap." + id + ".from.value").lte(value.get(0)), where("discountDetailsMap." + id + ".to.value").gte(value.get(0)))));
                    } else {
                        HkCalcMasterDocument calcMasterFrom = getCalcMastersyMasterCodeAndMasterValueId(id, Long.valueOf(value.get(0).toString()));
                        HkCalcMasterDocument calcMasterTo = getCalcMastersyMasterCodeAndMasterValueId(id, Long.valueOf(value.get(1).toString()));
//                        System.out.println("calcMaster " + calcMaster);
                        if (calcMasterFrom != null && calcMasterTo != null) {
                            criterias1.add(new Criteria().orOperator(where("discountDetailsMap." + id).exists(false), new Criteria().andOperator(where("discountDetailsMap." + id + ".from.sequence").lte(calcMasterFrom.getSequence()), where("discountDetailsMap." + id + ".to.sequence").gte(calcMasterTo.getSequence()))));
                        }
                    }
                    existcriteriaList.add(where("discountDetailsMap." + id).exists(true));
                }
            }
            Criteria[] criArray = new Criteria[criterias1.size() + 1];
            criterias1.toArray(criArray);
            existsCriterias = new Criteria[existcriteriaList.size()];
            existcriteriaList.toArray(existsCriterias);
            criArray[criterias1.size()] = new Criteria().orOperator(existsCriterias);
            Criteria criteria = new Criteria().andOperator(criArray);
            Query query = new Query(criteria);
            System.out.println("CRITERIA:" + query.toString());
            criterias.add(criteria);
            return (List<HkCalcRateDetailDocument>) mongoGenericDao.findByCriteria(criterias, HkCalcRateDetailDocument.class);
        }
        return null;
    }

    public HkCalcMasterDocument retrieveLastCalcMasterDocument() {

        Query query = new Query(where(CALC_MASTER_DOCUMENT.CODE).is(RAP_CALC_FIELD_MAP.get(HkSystemConstantUtil.FourC.R_DATE)));
        query.limit(1);
        query.with(new Sort(Sort.Direction.DESC, CALC_MASTER_DOCUMENT.MODIFIED_ON));
        List<HkCalcMasterDocument> documents = mongoGenericDao.getMongoTemplate().find(query, HkCalcMasterDocument.class);
        return (!CollectionUtils.isEmpty(documents)) ? documents.get(0) : null;
    }
}
