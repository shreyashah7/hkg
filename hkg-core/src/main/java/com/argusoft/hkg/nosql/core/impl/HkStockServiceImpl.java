/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.nosql.core.impl;

import com.argusoft.generic.core.mongotransaction.MongoTransaction;
import com.argusoft.generic.database.common.MongoGenericDao;
import com.argusoft.hkg.common.constantutil.HkSystemConstantUtil;
import com.argusoft.hkg.nosql.core.HkConfigurationService;
import com.argusoft.hkg.nosql.core.HkCustomFieldService;
import com.argusoft.hkg.nosql.core.HkFeatureService;
import com.argusoft.hkg.nosql.core.HkStockService;
import com.argusoft.hkg.nosql.core.HkUserService;
import com.argusoft.hkg.nosql.model.GenericDocument;
import com.argusoft.hkg.nosql.model.HkDepartmentConfigDocument;
import com.argusoft.hkg.nosql.model.HkFinalMakeableDocument;
import com.argusoft.hkg.nosql.model.HkInvoiceDocument;
import com.argusoft.hkg.nosql.model.HkIssueDocument;
import com.argusoft.hkg.nosql.model.HkIssueReceiveDocument;
import com.argusoft.hkg.nosql.model.HkPacketCaratLossDocument;
import com.argusoft.hkg.nosql.model.HkLotDocument;
import com.argusoft.hkg.nosql.model.HkLotStockDetailDocument;
import com.argusoft.hkg.nosql.model.HkPacketDocument;
import com.argusoft.hkg.nosql.model.HkPacketStockDetailDocument;
import com.argusoft.hkg.nosql.model.HkParcelDocument;
import com.argusoft.hkg.nosql.model.HkPersonCapabilityDocument;
import com.argusoft.hkg.nosql.model.HkPlanAccessPermissionDocument;
import com.argusoft.hkg.nosql.model.HkPlanDocument;
import com.argusoft.hkg.nosql.model.HkPurchaseDocument;
import com.argusoft.hkg.nosql.model.HkRoleFeatureModifierDocument;
import com.argusoft.hkg.nosql.model.HkRoughMakeableDocument;
import com.argusoft.hkg.nosql.model.HkRoughStockDetailDocument;
import com.argusoft.hkg.nosql.model.HkSellDocument;
import com.argusoft.hkg.nosql.model.HkSellParcelDetailDocument;
import com.argusoft.hkg.nosql.model.HkSlipNoGeneratorDocument;
import com.argusoft.hkg.nosql.model.HkStatusHistoryDocument;
import com.argusoft.hkg.nosql.model.HkSubLotDocument;
import com.argusoft.hkg.nosql.model.HkTransferDocument;
import com.argusoft.hkg.nosql.model.HkUserGradeStatusDocument;
import com.argusoft.hkg.nosql.model.HkUserGradeSuggestionDocument;
import com.argusoft.hkg.sync.center.core.SyncCenterUserService;
import com.argusoft.sync.center.model.HkCaratRangeDocument;
import com.argusoft.sync.center.model.HkFieldDocument;
import com.argusoft.sync.center.model.HkLeaveDocument;
import com.argusoft.sync.center.model.HkPriceListDetailDocument;
import com.argusoft.sync.center.model.HkPriceListDocument;
import com.argusoft.sync.center.model.HkSystemConfigurationDocument;
import com.argusoft.sync.center.model.SyncCenterFeatureDocument;
import com.argusoft.sync.center.model.SyncCenterUserDocument;
import com.argusoft.sync.center.model.UmCompanyDocument;
import static java.lang.Boolean.TRUE;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import org.bson.BasicBSONObject;
import org.bson.types.ObjectId;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.hibernate.criterion.Order;
import static org.springframework.data.mongodb.core.query.Criteria.where;

/**
 *
 * @author mmodi
 */
@Service
public class HkStockServiceImpl implements HkStockService, ApplicationContextAware {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(HkStockServiceImpl.class);
    ApplicationContext applicationContext;

    @Autowired
    HkFormulaExecution hkFormulaExecution;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public static class HkPersonCapabilityField {

        private static final String STATUS = "status";
        private static final String PROPERTY_NAME = "propertyName";
        private static final String PROPERTY_VALUE = "propertyValue";
        private static final String FRANCHISE = "franchise";
        private static final String FOR_PERSON = "forPerson";
        private static final String SUCCESS_RATIO = "successRatio";

    }

    private static final String IS_ARCHIVE = "isArchive";

    private static class ActivityFlowNodeRouteField {

        public static final String CURRENT_NODE = "currentNode";
        public static final String NODE_STATUS = "nodeStatus";
        public static final String NEXT_NODE = "nextNode";
    }

    private static class LeaveField {

        private static final String FOR_USER = "forUser";
        private static final String FROM_DATE = "frmDt";
        private static final String TO_DATE = "toDt";
    }

    private static class SytemConfig {

        public static final String ID = "_id";

    }

    private static class PriceListFields {

        public static final String FRANCHISE = "franchise";
    }

    private static class InvoiceFields {

        private static final String IS_ARCHIVE = "isArchive";
        private static final String FRANCHISE = "franchiseId";
        private static final String FIELD_VALUE = "fieldValue";
        private static final String OBJECT_ID = "_id";
        private static final String HAVE_VALUE = "haveValue";
        private static final String HAVE_LOT = "haveLot";
        private static final String STATUS = "status";
        private static final String INVOICE_DATE = "invoiceDate$DT$Date";
        private static final String YEAR = "year";
        private static final String SEQUENCE_NUMBER = "sequenceNumber";
        private static final String CREATED_ON = "createdOn";
    }

    private static class ParcelFields {

        private static final String IS_ARCHIVE = "isArchive";
        private static final String FRANCHISE = "franchiseId";
        private static final String INVOICE_ID = "invoice";
        private static final String PARCEL_ID = "_id";
        private static final String FIELD_VALUE = "fieldValue";
        private static final String OBJECT_ID = "_id";
        private static final String HAVE_VALUE = "haveValue";
        private static final String STATUS = "status";
        private static final String HAVE_LOT = "haveLot";
        private static final String PARCEL_SEQUENCE_ID = "parcelId$AG$String";
        private static final String YEAR = "year";
        private static final String SEQUENCE_NUMBER = "sequenceNumber";
        private static final String ASSOCIATED_PURCHASES = "associatedPurchases";
        private static final String REMARKS = "remarks$TF$String";
        private static final String IN_STOCK_OF = "in_stock_of_parcel$UMS$String";
        private static final String STOCK_CARAT = "stockCarat";
        private static final String STOCK_PIECES = "stockPieces";
        private static final String CREATED_ON = "createdOn";
    }

    private static class RoughStockDetailFields {

        public static final String PARCEL = "parcel.id";
        private static final String OBJECT_ID = "_id";
        public static final String ACTION = "action";
        public static final String LOT_ID = "lotId";
    }

    private static class LotFields {

        private static final String IS_ARCHIVE = "isArchive";
        private static final String FRANCHISE = "franchiseId";
        private static final String FIELD_VALUE = "fieldValue";
        private static final String INVOICE_ID = "invoice";
        private static final String PARCEL_ID = "parcel";
        private static final String LOT_Id = "_id";
        private static final String STATUS = "status";
        private static final String HAVE_VALUE = "haveValue";
        private static final String HAS_PACKET = "hasPacket";
        private static final String YEAR = "year";
        private static final String SEQUENCE_NUMBER = "sequenceNumber";
        private static final String LOT_NUMBER = "lotID$AG$String";
    }

    public static class UMFeatureDetail {

        private static final String MENU_LABEL = "menuLabel";
        private static final String MENU_TYPE = "menuType";
        public static final String ID = "id";
        public static final String FEATURE_NAME = "name";

    }

    private static class PacketFields {

        private static final String IS_ARCHIVE = "isArchive";
        private static final String FRANCHISE = "franchiseId";
        private static final String FIELD_VALUE = "fieldValue";
        private static final String INVOICE_ID = "invoice";
        private static final String PARCEL_ID = "parcel";
        private static final String LOT_ID = "lot";
        private static final String PACKET_ID = "id";
        private static final String STATUS = "status";
        private static final String HAVE_VALUE = "haveValue";
        private static final String PACKET_SEQ_ID = "packetID$AG$String";
        private static final String SEQUENCE_NUMBER = "sequenceNumber";
        private static final String ISSUE_RECEIVE_STATUS = "issueReceiveStatus";
        private static final String SPLIT_FROM = "splitFrom";
    }

    private static class ChangeStatusFields {

        private static final String IS_ARCHIVE = "isArchive";
        private static final String FRANCHISE = "franchiseId";
        private static final String LOT_Id = "lot";
        private static final String STATUS = "status";
        private static final String HAS_PACKET = "hasPacket";
        private static final String PARCEL_ID = "parcel";
        private static final String INVOICE_ID = "invoice";
        private static final String FIELD_VALUE = "fieldValue";
    }

    private static class EODFields {

        private static final String STATUS = "status";
        private static final String FIELD_VALUE = "fieldValue";
        private static final String ISSUE_DOCUMENT = "issueDocument";
        private static final String EOD_ISSUE_DOCUMENT = "eodIssueDocument";
        private static final String HAS_PACKET = "hasPacket";
        private static final String FRANCHISE = "franchiseId";
        private static final String CREATED_BY = "createdBy";
    }

    private static class IssueFields {

        private static final String IS_ARCHIVE = "isArchive";
        private static final String FRANCHISE = "franchiseId";
        private static final String PACKET = "packet";
        private static final String LOT = "lot";
        private static final String ISSUE_TO = "issueTo";
        private static final String CREATED_ON = "createdOn";
        private static final String ISSUE_ID = "_id";
    }

    private static final class PlanFields {

        private static final String FRANCHISE = "franchiseId";
        private static final String NODE_ID = "activityNode";
        private static final String STATUS = "status";
        private static final String PLAN_TYPE = "planType";
        private static final String CREATED_BY = "createdBy";
        private static final String LOT = "lot";
        private static final String PACKET = "packet";
        private static final String ID = "_id";
        private static final String REFERENCE_ID = "referencePlan";
        private static final String SEPARETOR = ":";
        private static final String FIELD_VALUE = "fieldValue";
        private static final String CREATED_ON = "createdOn";
    }

    private static final class CarateRangeFields {

        private static final String MAX = "maxValue";
        private static final String MIN = "minValue";
        private static final String STATUS = "status";
        public static final String ID = "id";
    }

    private static final class SodFields {

        private static final String STATUS = "status";
        private static final String FIELD_VALUE = "fieldValue";
        private static final String ISSUE_DOCUMENT = "issueDocument";
        private static final String EOD_ISSUE_DOCUMENT = "eodIssueDocument";
        private static final String HAS_PACKET = "hasPacket";
        private static final String FRANCHISE = "franchiseId";
        private static final String CREATED_BY = "createdBy";
    }

    private static final class SellFields {

        private static final String STATUS = "status";
        private static final String INVOICE = "invoice";
        private static final String PARCEL = "parcels";
        private static final String PACKET = "packet";
        private static final String LOT = "lot";
        private static final String FRANCHISE = "franchiseId";
        private static final String CREATED_BY = "createdBy";
    }

    private static final class TransferFields {

        private static final String STATUS = "status";
        private static final String INVOICE = "invoice";
        private static final String PARCEL = "parcel";
        private static final String PACKET = "packet";
        private static final String LOT = "lot";
    }

    private static final class TrackFields {

        private static final String IS_ARCHIVE = "isArchive";
        private static final String FRANCHISE = "franchiseId";
        private static final String LOT_Id = "_id";
        private static final String PARCEL_ID = "parcel";
        private static final String INVOICE_ID = "invoice";
        private static final String FIELD_VALUE = "fieldValue";
    }

    private static final class SubLotFields {

        private static final String IS_ARCHIVE = "isArchive";
        private static final String FRANCHISE = "franchiseId";
        private static final String SUB_LOT_ID = "_id";
        private static final String PARCEL_ID = "parcel";
        private static final String INVOICE_ID = "invoice";
        private static final String FIELD_VALUE = "fieldValue";
        private static final String ASSOCIATED_LOT = "associatedLot";
    }

    private static final class IssueReceiveFields {

        private static final String ID = "_id";
        private static final String SOURCE_DEPT = "sourceDepId";
        private static final String SOURCE_FRANCHISE_ID = "sourceFranchiseId";
        private static final String DESTINATION_DEPT = "destinationDepId";
        private static final String DESTINATION_FRANCHISE_ID = "destinationFranchiseId";
        private static final String STOCK_DEPT = "stockDepId";
        private static final String STOCK_FRANCHISE_ID = "stockFranchiseId";
        private static final String STATUS = "status";
        private static final String SLIP_NO = "slipNo";
        private static final String SLIP_DATE = "slipDate";
        private static final String IS_ACTIVE = "isActive";
        private static final String PARCEL = "parcel";
        private static final String LOT = "lot";
        private static final String PACKET = "packet";
        private static final String TYPE = "type";
        private static final String ISSUE_BY = "issuedBy";
        private static final String ISSUE_TO = "issueTo";
    }

    public static final class UserGradeStatusFields {

        private static final String FRANCHISE = "franchise";
        private static final String DEPARTMENT = "department";
        private static final String DESIGNATIONS = "designations";
        private static final String USER = "userId";
    }

    private static class PurchaseFields {

        private static final String IS_ARCHIVE = "isArchive";
        private static final String FRANCHISE = "franchiseId";
        private static final String FIELD_VALUE = "fieldValue";
        private static final String OBJECT_ID = "_id";
        private static final String SEQUENCE_NUMBER = "sequenceNumber";
        private static final String YEAR = "year";
        private static final String ASSOCIATED_PARCEL = "associatedParcels";
        private static final String CREATED_ON = "createdOn";
    }

    private static class SlipNumberGeneratorFields {

        private static final String SLIP_DATE = "slipDate";
        private static final String DEPARTMENT = "department";
    }

    private static class MakebleFields {

        private static final String PACKET_ID = "packetId";
    }
    @Autowired
    private MongoGenericDao mongoGenericDao;

    @Autowired
    HkCustomFieldService customFieldService;

    @Autowired
    SyncCenterUserService syncCenterUserService;

    @Autowired
    HkConfigurationService configurationService;

    @Autowired
    HkUserService userService;

    @Autowired
    HkFeatureService featureService;

//------------------------------------------ Stock Service Starts -------------------------------------//
    @Override
    @MongoTransaction
    public void updatePersonCapabilityForProperty(Long forPerson, Map<String, Long> expectedPropertyValueMap, Map<String, Long> actualPropertyValueMap, Long franchise) {
        List<HkPersonCapabilityDocument> personCapabilityDocuments = null;
        List<Criteria> criterias = new ArrayList<>();
        List<HkPersonCapabilityDocument> addPersonCapabilityDocuments = new ArrayList<>();

        Date currentDate = new Date();

        List<String> propertyNameList = new LinkedList<>();

        if (!CollectionUtils.isEmpty(expectedPropertyValueMap)) {
            propertyNameList.addAll(expectedPropertyValueMap.keySet());
        }
        //Based on forPerson, PropertyNameList(expectedPropertyValueMap.keySet()) and Status field searching the PersonCapabilityDocument
        if (forPerson != null && !CollectionUtils.isEmpty(propertyNameList) && franchise != null) {
            criterias.add(Criteria.where(HkPersonCapabilityField.PROPERTY_VALUE).in(new LinkedList<>(expectedPropertyValueMap.values())));
            criterias.add(Criteria.where(HkPersonCapabilityField.STATUS).is(HkSystemConstantUtil.ACTIVE));
            criterias.add(Criteria.where(HkPersonCapabilityField.FOR_PERSON).is(forPerson));
            criterias.add(Criteria.where(HkPersonCapabilityField.FRANCHISE).is(franchise));

            personCapabilityDocuments = mongoGenericDao.findByCriteria(criterias, HkPersonCapabilityDocument.class);
        }

        //Based on data retrieved preparing the  Map of String(propertyName), HkPersonCapabilityDocument.
        Map<String, HkPersonCapabilityDocument> propertyNamePersonDocumentMap = new LinkedHashMap<>();
        if (!CollectionUtils.isEmpty(personCapabilityDocuments)) {
            for (HkPersonCapabilityDocument hkPersonCapabilityDocument : personCapabilityDocuments) {
                propertyNamePersonDocumentMap.put(hkPersonCapabilityDocument.getPropertyName(), hkPersonCapabilityDocument);
            }
        }
        //If the map is not null then Iterating expectedPropertyValueMap based on keySet
        if (!CollectionUtils.isEmpty(actualPropertyValueMap)) {
            for (String propertyName : propertyNameList) {
                if (actualPropertyValueMap.containsKey(propertyName) && !StringUtils.isEmpty(propertyName)) {
                    Long expectedValue = expectedPropertyValueMap.get(propertyName);
                    Long actualValue = actualPropertyValueMap.get(propertyName);
                    HkPersonCapabilityDocument hkPersonCapabilityDocument = null;
                    if (!CollectionUtils.isEmpty(propertyNamePersonDocumentMap)) {
                        hkPersonCapabilityDocument = propertyNamePersonDocumentMap.get(propertyName);
                    }
                    //If the property name does not exists in the documents then creating new documents.
                    if (hkPersonCapabilityDocument != null && hkPersonCapabilityDocument.getPropertyValue().equals(expectedValue)) {
                        //If document already exists for the property name updating the documents based on the criteria.
                        hkPersonCapabilityDocument.setLastModifiedOn(currentDate);
                        Integer totalFinalPlans = hkPersonCapabilityDocument.getTotalFinalPlans();
                        if (totalFinalPlans != null) {
                            totalFinalPlans += 1;
                        } else {
                            totalFinalPlans = 1;
                        }
                        hkPersonCapabilityDocument.setTotalFinalPlans(totalFinalPlans);
                        //If the expected value and actual value of property matches then incrementing the succeededFinalPlans by 1.
                        if (expectedValue != null && actualValue != null && expectedValue.equals(actualValue)) {
                            Integer succeededFinalPlans = hkPersonCapabilityDocument.getSucceededFinalPlans();
                            if (succeededFinalPlans != null) {
                                succeededFinalPlans += 1;
                            } else {
                                succeededFinalPlans = 1;
                            }
                            hkPersonCapabilityDocument.setSucceededFinalPlans(succeededFinalPlans);
                        } else {
                            //If they do not matches the incrementing the failedFinalPlans by 1.
                            Integer failedFinalPlans = hkPersonCapabilityDocument.getFailedFinalPlans();
                            if (failedFinalPlans != null) {
                                failedFinalPlans += 1;
                            } else {
                                failedFinalPlans = 1;
                            }
                            hkPersonCapabilityDocument.setFailedFinalPlans(failedFinalPlans);
                        }
                        hkPersonCapabilityDocument.setSuccessRatio(((float) hkPersonCapabilityDocument.getSucceededFinalPlans() / (float) hkPersonCapabilityDocument.getTotalFinalPlans()));
                        mongoGenericDao.update(hkPersonCapabilityDocument);
                    } else {
                        hkPersonCapabilityDocument = new HkPersonCapabilityDocument();
                        hkPersonCapabilityDocument.setCreatedOn(currentDate);
                        hkPersonCapabilityDocument.setForPerson(forPerson);
                        hkPersonCapabilityDocument.setPropertyName(propertyName);
                        hkPersonCapabilityDocument.setPropertyValue(expectedValue);
                        hkPersonCapabilityDocument.setFranchise(franchise);
                        hkPersonCapabilityDocument.setLastModifiedOn(currentDate);
                        hkPersonCapabilityDocument.setStatus(HkSystemConstantUtil.ACTIVE);
                        hkPersonCapabilityDocument.setTotalFinalPlans(1);
                        if (!StringUtils.isEmpty(expectedValue) && !StringUtils.isEmpty(actualValue) && expectedValue.equals(actualValue)) {
                            hkPersonCapabilityDocument.setSucceededFinalPlans(1);
                            hkPersonCapabilityDocument.setFailedFinalPlans(0);
                        } else {
                            hkPersonCapabilityDocument.setSucceededFinalPlans(0);
                            hkPersonCapabilityDocument.setFailedFinalPlans(1);
                        }
                        hkPersonCapabilityDocument.setSuccessRatio(((float) hkPersonCapabilityDocument.getSucceededFinalPlans() / (float) hkPersonCapabilityDocument.getTotalFinalPlans()));
                        addPersonCapabilityDocuments.add(hkPersonCapabilityDocument);
                    }
                }
            }
            if (!CollectionUtils.isEmpty(addPersonCapabilityDocuments)) {
                mongoGenericDao.createAll(addPersonCapabilityDocuments);
            }
        }

    }

    @Override
    @MongoTransaction
    public void updateBreakagePercentage(Long forPerson, boolean haveBreakage, Long franchise) {

        HkPersonCapabilityDocument personCapabilityDocument = null;
        List<Criteria> criterias = new ArrayList<>();
        Date currentDate = new Date();

        if (forPerson != null && franchise != null) {
            criterias.add(Criteria.where(HkPersonCapabilityField.PROPERTY_NAME).is(HkSystemConstantUtil.PlanStaticFieldName.BREAKAGE));
            criterias.add(Criteria.where(HkPersonCapabilityField.STATUS).is(HkSystemConstantUtil.ACTIVE));
            criterias.add(Criteria.where(HkPersonCapabilityField.FOR_PERSON).is(forPerson));
            criterias.add(Criteria.where(HkPersonCapabilityField.FRANCHISE).is(franchise));

            personCapabilityDocument = (HkPersonCapabilityDocument) mongoGenericDao.findOneByCriteria(criterias, HkPersonCapabilityDocument.class);
        }

        if (personCapabilityDocument == null) {
            personCapabilityDocument = new HkPersonCapabilityDocument();
            personCapabilityDocument.setCreatedOn(currentDate);
            personCapabilityDocument.setForPerson(forPerson);
            personCapabilityDocument.setPropertyName(HkSystemConstantUtil.PlanStaticFieldName.BREAKAGE);
            personCapabilityDocument.setFranchise(franchise);
            personCapabilityDocument.setLastModifiedOn(currentDate);
            personCapabilityDocument.setStatus(HkSystemConstantUtil.ACTIVE);
            personCapabilityDocument.setTotalFinalPlans(1);
            if (haveBreakage) {
                personCapabilityDocument.setSucceededFinalPlans(0);
                personCapabilityDocument.setFailedFinalPlans(1);
            } else {
                personCapabilityDocument.setSucceededFinalPlans(1);
                personCapabilityDocument.setFailedFinalPlans(0);
            }
            personCapabilityDocument.setBreakagePercentage(((float) (100 * personCapabilityDocument.getFailedFinalPlans()) / (float) personCapabilityDocument.getTotalFinalPlans()));
            mongoGenericDao.create(personCapabilityDocument);
        } else {
            personCapabilityDocument.setLastModifiedOn(currentDate);
            if (personCapabilityDocument.getTotalFinalPlans() == null) {
                personCapabilityDocument.setTotalFinalPlans(1);
            } else {
                personCapabilityDocument.setTotalFinalPlans(personCapabilityDocument.getTotalFinalPlans() + 1);
            }
            if (haveBreakage) {
                if (personCapabilityDocument.getFailedFinalPlans() == null) {
                    personCapabilityDocument.setFailedFinalPlans(1);
                } else {
                    personCapabilityDocument.setFailedFinalPlans(personCapabilityDocument.getFailedFinalPlans() + 1);
                }
            } else {
                if (personCapabilityDocument.getSucceededFinalPlans() == null) {
                    personCapabilityDocument.setSucceededFinalPlans(1);
                } else {
                    personCapabilityDocument.setSucceededFinalPlans(personCapabilityDocument.getSucceededFinalPlans() + 1);
                }
            }
            personCapabilityDocument.setBreakagePercentage(((float) (100 * personCapabilityDocument.getFailedFinalPlans()) / (float) personCapabilityDocument.getTotalFinalPlans()));
            mongoGenericDao.update(personCapabilityDocument);
        }
    }

    @Override
    public HkSystemConfigurationDocument retrieveSystemConfig(String id) {
        HkSystemConfigurationDocument centerSystemConfigurationDocument = null;
        List<Criteria> criterias = new ArrayList<>();
        if (!StringUtils.isEmpty(id) && StringUtils.hasText(id)) {
            criterias.add(Criteria.where(SytemConfig.ID).is(id));
        }

        if (!CollectionUtils.isEmpty(criterias)) {
            centerSystemConfigurationDocument = (HkSystemConfigurationDocument) mongoGenericDao.findOneByCriteria(criterias, HkSystemConfigurationDocument.class);
        }
        return centerSystemConfigurationDocument;
    }

    @Override
    public List<HkLeaveDocument> retrieveLeavesForUserBetweenFromDateAndToDate(List<Long> forUsers, Date fromDate, Date toDate, Boolean currentlyOnLeave) {
        List<HkLeaveDocument> leaveDocuments = null;
        if (!CollectionUtils.isEmpty(forUsers) && fromDate != null && toDate != null) {
            List<Criteria> criterias = new ArrayList<>();
            if (currentlyOnLeave != null && !currentlyOnLeave) {
                criterias.add(Criteria.where(LeaveField.FOR_USER).in(forUsers)
                        .andOperator(Criteria.where(LeaveField.FROM_DATE).lte(toDate), Criteria.where(LeaveField.TO_DATE).gte(fromDate)));
            } else if (currentlyOnLeave != null && currentlyOnLeave) {
                criterias.add(Criteria.where(LeaveField.FOR_USER).in(forUsers)
                        .andOperator(Criteria.where(LeaveField.FROM_DATE).lte(fromDate), Criteria.where(LeaveField.TO_DATE).gte(fromDate)));
            }
//            criterias.add(Criteria.where(LeaveField.FROM_DATE).lte(toDate)
//                    .andOperator(Criteria.where(LeaveField.TO_DATE).gte(fromDate)));
            leaveDocuments = mongoGenericDao.findByCriteria(criterias, HkLeaveDocument.class);
        }
        return leaveDocuments;
    }

    @Override
    public Map<Long, Date> retrievePriceListIdAndDate(Long franchise) {
        List<Criteria> criterias = new ArrayList<>();
        Map<Long, Date> priceListAndDateMap = null;
        if (franchise != null) {
            criterias.add(Criteria.where(PriceListFields.FRANCHISE).is(franchise));
            List<HkPriceListDocument> priceListDocuments = mongoGenericDao.findByCriteria(criterias, HkPriceListDocument.class);
            priceListDocuments.sort(new Comparator<HkPriceListDocument>() {
                @Override
                public int compare(HkPriceListDocument o1, HkPriceListDocument o2) {
                    return o2.getUploadedOn().compareTo(o1.getLastModifiedOn());
                }
            });
            if (!CollectionUtils.isEmpty(priceListDocuments)) {
                HkSystemConfigurationDocument systemConfigurationDocument = this.retrieveSystemConfig(HkSystemConstantUtil.FranchiseConfiguration.NO_OF_SNAP_IDS);
                if (systemConfigurationDocument != null) {
                    String noOfSnapIds = systemConfigurationDocument.getKeyValue();
                    if (StringUtils.hasText(noOfSnapIds)) {
                        priceListAndDateMap = new HashMap();
                        for (HkPriceListDocument hkPriceListDocument : priceListDocuments) {
                            if (priceListAndDateMap.size() == Integer.parseInt(noOfSnapIds)) {
                                break;
                            } else {
                                priceListAndDateMap.put(hkPriceListDocument.getId(), hkPriceListDocument.getUploadedOn());
                            }
                        }
                    }
                }
            }
        }
        return priceListAndDateMap;
    }

    @Override
    public List<HkPriceListDetailDocument> retrievePriceListDetailDocumentsFromPriceListId(Long id) {
        List<HkPriceListDetailDocument> hkPriceListDetailDocuments = null;
        if (id != null) {

            HkPriceListDocument hkPriceListDocument = (HkPriceListDocument) mongoGenericDao.retrieveById(id, HkPriceListDocument.class);
            if (hkPriceListDocument != null) {
                if (!CollectionUtils.isEmpty(hkPriceListDocument.getHkPriceListDetailEntityCollection())) {
                    hkPriceListDetailDocuments = new ArrayList<>(hkPriceListDocument.getHkPriceListDetailEntityCollection());
                }
            }
        }
        return hkPriceListDetailDocuments;
    }

    @Override
    public Map<Long, String> caratRangeMap(List<Long> caratIds) {

        Map<Long, String> caratRangeMap = null;
        if (!CollectionUtils.isEmpty(caratIds)) {
            List<HkCaratRangeDocument> documents = null;
            List<Criteria> criterias = new ArrayList<>();
            criterias.add(Criteria.where(CarateRangeFields.ID).in(caratIds));
            documents = mongoGenericDao.findByCriteria(criterias, HkCaratRangeDocument.class);
            if (!CollectionUtils.isEmpty(documents)) {
                caratRangeMap = new HashMap<>();
                for (HkCaratRangeDocument caratRangeEntity : documents) {
                    String range = caratRangeEntity.getMinValue() + "-" + caratRangeEntity.getMaxValue();
                    caratRangeMap.put(caratRangeEntity.getId(), range);
                }
            }
        }
        return caratRangeMap;
    }

//    @Override
//    @MongoTransaction
//    public void reCaluclatePriceForPlannedPlans(HkPriceListDocument priceListDocument, Long franchise, Long modifyby) {
//        if (priceListDocument != null && franchise != null) {
//
//            //fetch the plans of modifier type plan
//            List<HkLotDocument> lots = this.retrieveLotsByIds(null, franchise, Boolean.FALSE, Arrays.asList(HkSystemConstantUtil.StockStatus.IN_PRODUCTION, HkSystemConstantUtil.StockStatus.TERMINATED), null, Boolean.FALSE);
//            List<HkPacketDocument> packets = this.retrievePacketsByIds(null, franchise, Boolean.FALSE, Arrays.asList(HkSystemConstantUtil.StockStatus.IN_PRODUCTION, HkSystemConstantUtil.StockStatus.TERMINATED), Boolean.FALSE);
//
//            if (!CollectionUtils.isEmpty(lots) || !CollectionUtils.isEmpty(packets)) {
//                List<ObjectId> lotIds = null;
//                List<ObjectId> packetIds = null;
//                //make list of lot ids
//                if (!CollectionUtils.isEmpty(lots)) {
//                    lotIds = new ArrayList<>();
//                    for (HkLotDocument lot : lots) {
//                        lotIds.add(lot.getId());
//                    }
//                }
//                //make list of packet ids
//                if (!CollectionUtils.isEmpty(packets)) {
//                    packetIds = new ArrayList<>();
//                    for (HkPacketDocument packet : packets) {
//                        packetIds.add(packet.getId());
//                    }
//                }
//                Collection<HkPriceListDetailDocument> priceListDetailDocuments = priceListDocument.getHkPriceListDetailEntityCollection();
//                if (!CollectionUtils.isEmpty(priceListDetailDocuments) && (!CollectionUtils.isEmpty(lotIds) || !CollectionUtils.isEmpty(packetIds))) {
//                    List<HkPlanDocument> planDocuments = this.retrievePlansAccordingToModifierForLotOrPacket(HkSystemConstantUtil.SERVICE_MODIFIER.PLAN, lotIds, packetIds);
//                    HkPriceListDetailDocument matchedPriceListDetailDocument = null;
//                    String currency = null;
//                    //calculate new price and replace the old one in field value
//                    for (HkPlanDocument plan : planDocuments) {
//                        Double price = null;
//                        if (plan.getFieldValue() != null) {
//                            Long cut = null;
//                            Long color = null;
//                            Long clarity = null;
//                            Long carateRange = null;
//                            Long fluorescence = null;
//                            currency = null;
//                            BasicBSONObject bson = plan.getFieldValue();
//                            if (bson.containsField(HkSystemConstantUtil.PlanStaticFieldName.CUT)) {
//                                cut = Long.parseLong(bson.get(HkSystemConstantUtil.PlanStaticFieldName.CUT).toString());
//                            }
//                            if (bson.containsField(HkSystemConstantUtil.PlanStaticFieldName.COLOR)) {
//                                color = Long.parseLong(bson.get(HkSystemConstantUtil.PlanStaticFieldName.COLOR).toString());
//                            }
//                            if (bson.containsField(HkSystemConstantUtil.PlanStaticFieldName.CLARITY)) {
//                                clarity = Long.parseLong(bson.get(HkSystemConstantUtil.PlanStaticFieldName.CLARITY).toString());
//                            }
//                            if (bson.containsField(HkSystemConstantUtil.PlanStaticFieldName.CARATE_RANGE)) {
//                                carateRange = Long.parseLong(bson.get(HkSystemConstantUtil.PlanStaticFieldName.CARATE_RANGE).toString());
//                            }
//                            if (bson.containsField(HkSystemConstantUtil.PlanStaticFieldName.FLUROSCENE)) {
//                                fluorescence = Long.parseLong(bson.get(HkSystemConstantUtil.PlanStaticFieldName.FLUROSCENE).toString());
//                            }
//                            if (bson.containsField(HkSystemConstantUtil.PlanStaticFieldName.FLUROSCENE)) {
//                                fluorescence = Long.parseLong(bson.get(HkSystemConstantUtil.PlanStaticFieldName.FLUROSCENE).toString());
//                            }
//                            if (bson.containsField(HkSystemConstantUtil.PlanStaticFieldName.CURRENCY_CODE)) {
//                                currency = bson.get(HkSystemConstantUtil.PlanStaticFieldName.CURRENCY_CODE).toString();
//                            }
//                            for (HkPriceListDetailDocument hkPriceListDetailDocument : priceListDetailDocuments) {
//                                if (hkPriceListDetailDocument.getClarity().equals(clarity)) {
//                                    if (hkPriceListDetailDocument.getColor().equals(color)) {
//                                        if (hkPriceListDetailDocument.getCut().equals(cut)) {
//                                            if (hkPriceListDetailDocument.getCaratRange().equals(carateRange)) {
//                                                if (fluorescence != null && hkPriceListDetailDocument.getFluorescence() != null) {
//                                                    if (hkPriceListDetailDocument.getFluorescence().equals(fluorescence)) {
//                                                        matchedPriceListDetailDocument = hkPriceListDetailDocument.clone();
//                                                        break;
//                                                    }
//                                                } else {
//                                                    matchedPriceListDetailDocument = hkPriceListDetailDocument.clone();
//                                                    break;
//                                                }
//                                            }
//                                        }
//                                    }
//                                }
//                            }
//                            if (matchedPriceListDetailDocument != null) {
//                                price = matchedPriceListDetailDocument.getHkgPrice();
//                                bson.put(HkSystemConstantUtil.PlanStaticFieldName.VALUE_OF_PLAN, price);
//                            }
//                        }
//                        //update audit trails
//                        plan.setLastModifiedBy(modifyby);
//                        plan.setLastModifiedOn(new Date());
//                        //update price history document
//                        List<HkPriceHistoryDocument> priceHistoryDocuments = plan.getHkPriceHistoryList();
//                        if (price != null) {
//                            if (CollectionUtils.isEmpty(priceHistoryDocuments)) {
//                                priceHistoryDocuments = new ArrayList<>();
//                            }
//                            HkPriceHistoryDocument priceHistoryDocument = new HkPriceHistoryDocument();
//                            priceHistoryDocument.setByUser(modifyby);
//                            if (currency != null) {
//                                priceHistoryDocument.setCurrency(currency);
//                            }
//                            priceHistoryDocument.setOnDate(new Date());
//                            priceHistoryDocument.setPrice(price);
//                            if (StringUtils.hasText(plan.getStatus())) {
//                                priceHistoryDocument.setStatus(plan.getStatus());
//                            }
//                            priceHistoryDocuments.add(priceHistoryDocument);
//                            plan.setHkPriceHistoryList(priceHistoryDocuments);
//                            mongoGenericDao.update(plan);
//                        }
//                    }
//                }
//            }
//        }
//    }
    //------------------------------------------ Stock Service Ends -------------------------------------//
    //------------------------------------------ Invoice Service Starts -------------------------------------//
    @Override
    public Integer getNextInvoiceSequence() {
        Integer sequenceNumber = 0;
        Query query = new Query();
        query.addCriteria(Criteria.where(InvoiceFields.IS_ARCHIVE).is(Boolean.FALSE));
        query.addCriteria(Criteria.where(InvoiceFields.YEAR).is(Calendar.getInstance().get(Calendar.YEAR)));
        query.with(new Sort(Sort.Direction.DESC, InvoiceFields.SEQUENCE_NUMBER)).limit(1);
        HkInvoiceDocument hkInvoiceDocument = mongoGenericDao.getMongoTemplate().findOne(query, HkInvoiceDocument.class);
        if (hkInvoiceDocument != null) {
            sequenceNumber = hkInvoiceDocument.getSequenceNumber();
        }
        return sequenceNumber;
    }

    @Override
    public Boolean isInvoiceSequenceExist(Integer invoiceId, ObjectId invoiceObjectId) {
        Boolean invoiceExist = Boolean.FALSE;
        List<HkInvoiceDocument> invoiceDocuments = new ArrayList<>();
        List<Criteria> criterias = new ArrayList<>();
        criterias.add(Criteria.where(InvoiceFields.YEAR).is(Calendar.getInstance().get(Calendar.YEAR)));
        if (invoiceId != null) {
            criterias.add(Criteria.where(InvoiceFields.SEQUENCE_NUMBER).is(invoiceId));
        }
        if (invoiceObjectId != null) {
            criterias.add(Criteria.where(InvoiceFields.OBJECT_ID).ne(invoiceObjectId));
        }
        criterias.add(Criteria.where(InvoiceFields.IS_ARCHIVE).is(Boolean.FALSE));
        invoiceDocuments = mongoGenericDao.findByCriteria(criterias, HkInvoiceDocument.class);
        if (!CollectionUtils.isEmpty(invoiceDocuments)) {
            invoiceExist = Boolean.TRUE;
        }
        return invoiceExist;
    }

    @Override
    @MongoTransaction
    public String saveInvoice(Map<String, Object> invoiceCustomMap, Map<String, String> invoiceDbTypeMap, Map<String, String> uiFieldMap, Long franchise, Long createdBy, Integer sequenceNumber, Map<String, String> autogeneratedLabelMap, List<HkFieldDocument> totalFormulaList, Long featureId) {

        HkInvoiceDocument invoiceDocument = new HkInvoiceDocument();
        invoiceDocument.setFranchiseId(0L);
        invoiceDocument.setCreatedByFranchise(franchise);
        invoiceDocument.setIsArchive(Boolean.FALSE);
        invoiceDocument.setCreatedBy(createdBy);
        invoiceDocument.setLastModifiedBy(createdBy);
        invoiceDocument.setCreatedOn(new Date());
        invoiceDocument.setLastModifiedOn(new Date());
        if (sequenceNumber == null) {
            sequenceNumber = this.getNextInvoiceSequence();
            sequenceNumber++;
        }
        invoiceDocument.setSequenceNumber(sequenceNumber);
        invoiceDocument.setYear(Calendar.getInstance().get(Calendar.YEAR));
        invoiceDocument.setStatus(HkSystemConstantUtil.StockStatus.NEW_ROUGH);
        BasicBSONObject fieldValues = null;
        if (!CollectionUtils.isEmpty(invoiceCustomMap) && !CollectionUtils.isEmpty(invoiceDbTypeMap)) {
            fieldValues = customFieldService.makeBSONObject(invoiceCustomMap, invoiceDbTypeMap, uiFieldMap, HkSystemConstantUtil.Feature.INVOICE, 0l, null, Boolean.FALSE, null, autogeneratedLabelMap);
            if (fieldValues.containsField(HkSystemConstantUtil.InvoiceStaticFields.IS_HIDDEN)) {
                Boolean hasValue = (Boolean) fieldValues.get(HkSystemConstantUtil.InvoiceStaticFields.IS_HIDDEN);
                invoiceDocument.setHaveValue(hasValue);
            } else {
                invoiceDocument.setHaveValue(Boolean.FALSE);
                fieldValues.put(HkSystemConstantUtil.InvoiceStaticFields.IS_HIDDEN, Boolean.FALSE);
            }
        } else {
            invoiceDocument.setHaveValue(Boolean.FALSE);
            fieldValues = new BasicBSONObject();
            fieldValues.put(HkSystemConstantUtil.InvoiceStaticFields.IS_HIDDEN, Boolean.FALSE);
        }
        if (!fieldValues.containsField(HkSystemConstantUtil.InvoiceStaticFields.INVOICE_ID)) {
            fieldValues.put(HkSystemConstantUtil.InvoiceStaticFields.INVOICE_ID, Calendar.getInstance().get(Calendar.YEAR) + "-" + sequenceNumber);
        }
        if (!fieldValues.containsField(HkSystemConstantUtil.InvoiceStaticFieldName.IN_STOCK_OF)) {
            fieldValues.put(HkSystemConstantUtil.InvoiceStaticFieldName.IN_STOCK_OF, createdBy);
        }
        invoiceDocument.setFieldValue(fieldValues);
        Map<String, Double> finalFormulaEvalMap = hkFormulaExecution.mapOfEvaluatedFormulaValueWithDbField(totalFormulaList, franchise, invoiceDocument, null, null, null, null, null, null, null, featureId, HkSystemConstantUtil.FeatureForFormulaEvaluation.ADD_INVOICE);
        if (finalFormulaEvalMap != null && !finalFormulaEvalMap.isEmpty()) {
            for (Map.Entry<String, Double> entrySet : finalFormulaEvalMap.entrySet()) {
                invoiceDocument.getFieldValue().put(entrySet.getKey(), entrySet.getValue());
            }
        }
        mongoGenericDao.create(invoiceDocument);
        return invoiceDocument.getId().toString();
    }

    @Override
    public HkInvoiceDocument retrieveInvoiceById(ObjectId invoiceId) {
        HkInvoiceDocument invoiceDocument = (HkInvoiceDocument) mongoGenericDao.retrieveById(invoiceId, HkInvoiceDocument.class);
        String pointerComponentType = HkSystemConstantUtil.CustomField.ComponentType.POINTER + "@*" + HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN;
        Map<String, String> uiFieldMap = null;
        Map<String, Object> customMap = invoiceDocument.getFieldValue().toMap();
        uiFieldMap = this.createFieldNameWithComponentType(customMap);
        if (!CollectionUtils.isEmpty(uiFieldMap)) {
            for (Map.Entry<String, String> field : uiFieldMap.entrySet()) {
                if (field.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN) || field.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.USER_MULTISELECT) || field.getValue().equals(pointerComponentType)) {
                    if (customMap != null && !customMap.isEmpty() && customMap.containsKey(field.getKey())) {
                        String value = customMap.get(field.getKey()).toString().replaceAll("\\[", "").replaceAll("\\]", "");
                        customMap.put(field.getKey(), value);
                    }
                }
            }
            invoiceDocument.getFieldValue().putAll(customMap);
        }
        return invoiceDocument;
    }

    @Override
    public List<HkInvoiceDocument> retrieveAllInvoice(Long franchise, Boolean isActive) {
        List<Criteria> criterias = new ArrayList<>();
        List<HkInvoiceDocument> documents = null;
        criterias.add(Criteria.where(InvoiceFields.FRANCHISE).is(franchise));
        if (isActive != null) {
            criterias.add(Criteria.where(InvoiceFields.IS_ARCHIVE).is(isActive));
        }
        HkSystemConfigurationDocument systemConfigurationDocument = this.retrieveSystemConfig(HkSystemConstantUtil.FranchiseConfiguration.CURR_VISIBILITY_STATUS);
        if (systemConfigurationDocument != null) {
            if (systemConfigurationDocument.getKeyValue() != null) {
                if (systemConfigurationDocument.getKeyValue().equalsIgnoreCase(HkSystemConstantUtil.SWITCH_OFF)) {
                    criterias.add(Criteria.where(InvoiceFields.HAVE_VALUE).is(Boolean.FALSE));
                }
            }
        }
        documents = mongoGenericDao.findByCriteria(criterias, HkInvoiceDocument.class);
        return documents;
    }

    @Override
    public long getCountofInvoiceDocumentsFromCriteria(Long franchise, Boolean isActive, List<ObjectId> invoiceIds) {
        List<Criteria> criterias = new LinkedList<>();
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -1);
        Date afterDate = cal.getTime();
        criterias.add(Criteria.where(InvoiceFields.FRANCHISE).is(franchise));
        if (isActive != null) {
            criterias.add(Criteria.where(InvoiceFields.IS_ARCHIVE).is(isActive));
        }
        if (!CollectionUtils.isEmpty(invoiceIds)) {
            criterias.add(Criteria.where(InvoiceFields.OBJECT_ID).in(invoiceIds));
        }
        criterias.add(Criteria.where(InvoiceFields.STATUS).is(HkSystemConstantUtil.StockStatus.NEW_ROUGH));
        criterias.add(Criteria.where(InvoiceFields.CREATED_ON).gte(afterDate).lte(new Date()));
        HkSystemConfigurationDocument systemConfigurationDocument = this.retrieveSystemConfig(HkSystemConstantUtil.FranchiseConfiguration.CURR_VISIBILITY_STATUS);
        if (systemConfigurationDocument != null) {
            if (systemConfigurationDocument.getKeyValue() != null) {
                if (systemConfigurationDocument.getKeyValue().equalsIgnoreCase(HkSystemConstantUtil.SWITCH_OFF)) {
                    criterias.add(Criteria.where(InvoiceFields.HAVE_VALUE).is(Boolean.FALSE));
                }
            }
        }
        return mongoGenericDao.count(criterias, HkInvoiceDocument.class);
    }

    @Override
    public long getCountofParcelDocumentsFromCriteria(Long franchise, Boolean isActive, ObjectId invoiceId) {
        List<Criteria> criterias = new LinkedList<>();
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -1);
        Date afterDate = cal.getTime();
        criterias.add(Criteria.where(ParcelFields.FRANCHISE).is(franchise));
        if (isActive != null) {
            criterias.add(Criteria.where(ParcelFields.IS_ARCHIVE).is(isActive));
        }
        if (invoiceId != null) {
            criterias.add(Criteria.where(ParcelFields.INVOICE_ID).is(invoiceId));
        }
        criterias.add(Criteria.where(InvoiceFields.STATUS).is(HkSystemConstantUtil.StockStatus.NEW_ROUGH));
        criterias.add(Criteria.where(InvoiceFields.CREATED_ON).gte(afterDate).lte(new Date()));
        HkSystemConfigurationDocument systemConfigurationDocument = this.retrieveSystemConfig(HkSystemConstantUtil.FranchiseConfiguration.CURR_VISIBILITY_STATUS);
        if (systemConfigurationDocument != null) {
            if (systemConfigurationDocument.getKeyValue() != null) {
                if (systemConfigurationDocument.getKeyValue().equalsIgnoreCase(HkSystemConstantUtil.SWITCH_OFF)) {
                    criterias.add(Criteria.where(ParcelFields.HAVE_VALUE).is(Boolean.FALSE));
                }
            }
        }
        return mongoGenericDao.count(criterias, HkParcelDocument.class);
    }

    @Override
    public long getCountofPurchaseDocumentsFromCriteria(Long franchise, Boolean isActive,Map<String, Object> fieldValues, List<ObjectId> purchaseIds) {
        List<Criteria> criterias = new LinkedList<>();
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -1);
        Date afterDate = cal.getTime();
        
        Map<String, String> uiFieldMap = null;
        Map<String, Object> dateCriteriaMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(fieldValues)) {
            uiFieldMap = this.createFieldNameWithComponentType(fieldValues);
            for (Map.Entry<String, Object> entry : fieldValues.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                if (uiFieldMap.containsKey(key)) {
                    String cType = uiFieldMap.get(key);
                    // Specific logic for Date type component
                    if (cType.equals(HkSystemConstantUtil.CustomField.ComponentType.DATE)) {
                        if (value != null) {
                            SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS\'Z\'");
                            formatDate.setTimeZone(TimeZone.getTimeZone("UTC"));
                            Date parseDate = null;
                            Date parseIncDate = null;
                            try {
                                // parseDate gives the formatted date
                                parseDate = formatDate.parse(value.toString());
                                Calendar c = Calendar.getInstance();
                                c.setTime(parseDate);
                                c.add(Calendar.DATE, 1);
                                // incremented date contains next date i.e the current date +1
//                                String incrementedDate = formatDate.format(c.getTime()); // new date
                                parseIncDate = new Date(c.getTimeInMillis());

                            } catch (ParseException ex) {
                                Logger.getLogger(HkStockServiceImpl.class
                                        .getName()).log(Level.SEVERE, null, ex);
                            }
                            if (!(key.startsWith("from") || key.startsWith("to"))) {
                                criterias.add(Criteria.where(PurchaseFields.FIELD_VALUE + "." + key).gt(parseDate).lt(parseIncDate));
                            } else {
                                if (key.startsWith("from")) {
                                    dateCriteriaMap.put(key, parseDate);
                                } else {
                                    dateCriteriaMap.put(key, parseIncDate);
                                }
                            }
                        }
                    } // Code added for handling image and file but since we are not showing image and file components in search it is of no use as for now
                    else if (cType.equals(HkSystemConstantUtil.CustomField.ComponentType.IMAGE) || cType.equals(HkSystemConstantUtil.CustomField.ComponentType.UPLOAD)) {
                        criterias.add(Criteria.where(PurchaseFields.FIELD_VALUE + "." + key).regex(value.toString()));
                    } else {
                        if (value instanceof Collection) {
                            Collection coll = (Collection) value;
                            if (!CollectionUtils.isEmpty(coll)) {
                                criterias.add(Criteria.where(PurchaseFields.FIELD_VALUE + "." + key).in(coll));
                            }
                        } else if (value instanceof Date) {
                            long date = Long.parseLong(value.toString());
                            criterias.add(Criteria.where(PurchaseFields.FIELD_VALUE + "." + key).is(new Date(date)));
                        } else {
                            if (value != null && value.toString().length() > 0) {
                                criterias.add(Criteria.where(PurchaseFields.FIELD_VALUE + "." + key).is(value));
                            }
                        }
                    }
                }
            }
        }
        if (!CollectionUtils.isEmpty(purchaseIds)) {
            criterias.add(Criteria.where(PurchaseFields.OBJECT_ID).in(purchaseIds));
        }
        criterias.add(Criteria.where(PurchaseFields.FRANCHISE).is(franchise));
        if (isActive != null) {
            criterias.add(Criteria.where(PurchaseFields.IS_ARCHIVE).is(isActive));
        }
        criterias.add(Criteria.where(PurchaseFields.CREATED_ON).gte(afterDate).lte(new Date()));
        return mongoGenericDao.count(criterias, HkPurchaseDocument.class);
    }

    @Override
    public List<HkInvoiceDocument> retrieveAllInvoiceByCriteria(Long franchise, Boolean isActive, Integer offset, Integer limit) {
        Query query = new Query();
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -1);
        Date afterDate = cal.getTime();
        List<HkInvoiceDocument> documents = null;
        query.addCriteria(Criteria.where(InvoiceFields.FRANCHISE).is(franchise));
        if (isActive != null) {
            query.addCriteria(Criteria.where(InvoiceFields.IS_ARCHIVE).is(isActive));
        }
        query.addCriteria(Criteria.where(InvoiceFields.STATUS).is(HkSystemConstantUtil.StockStatus.NEW_ROUGH));
        query.addCriteria(Criteria.where(InvoiceFields.CREATED_ON).gte(afterDate).lte(new Date()));
        HkSystemConfigurationDocument systemConfigurationDocument = this.retrieveSystemConfig(HkSystemConstantUtil.FranchiseConfiguration.CURR_VISIBILITY_STATUS);
        if (systemConfigurationDocument != null) {
            if (systemConfigurationDocument.getKeyValue() != null) {
                if (systemConfigurationDocument.getKeyValue().equalsIgnoreCase(HkSystemConstantUtil.SWITCH_OFF)) {
                    query.addCriteria(Criteria.where(InvoiceFields.HAVE_VALUE).is(Boolean.FALSE));
                }
            }
        }
        query.with(new Sort(Sort.Direction.DESC, Arrays.asList(InvoiceFields.YEAR, InvoiceFields.SEQUENCE_NUMBER)));
        query.skip((int) offset);
        query.limit((int) limit);
        documents = mongoGenericDao.getMongoTemplate().find(query, HkInvoiceDocument.class);
        if (!CollectionUtils.isEmpty(documents)) {
            for (HkInvoiceDocument invoiceDocument : documents) {
                String pointerComponentType = HkSystemConstantUtil.CustomField.ComponentType.POINTER + "@*" + HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN;
                Map<String, String> uiFieldMap = null;
                Map<String, Object> customMap = invoiceDocument.getFieldValue().toMap();
                uiFieldMap = this.createFieldNameWithComponentType(customMap);
                if (!CollectionUtils.isEmpty(uiFieldMap)) {
                    for (Map.Entry<String, String> field : uiFieldMap.entrySet()) {
                        if (field.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN) || field.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.USER_MULTISELECT) || field.getValue().equals(pointerComponentType)) {
                            if (customMap != null && !customMap.isEmpty() && customMap.containsKey(field.getKey())) {
                                String value = customMap.get(field.getKey()).toString().replaceAll("\\[", "").replaceAll("\\]", "");
                                customMap.put(field.getKey(), value);
                            }
                        }
                    }
                    invoiceDocument.getFieldValue().putAll(customMap);
                }
            }

        }
        return documents;
    }

    @Override
    public List<HkInvoiceDocument> retrieveInvoices(Map<String, Object> fieldValues, long franchise, Boolean isArchive, List<ObjectId> invoiceIds, Boolean haveLot, Integer offset, Integer limit) {
        Query query = new Query();
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -1);
        Date afterDate = cal.getTime();
        List<HkInvoiceDocument> invoiceDocuments = null;
        Map<String, String> uiFieldMap = this.createFieldNameWithComponentType(fieldValues);
        Map<String, Object> dateCriteriaMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(fieldValues)) {
            for (Map.Entry<String, Object> entry : fieldValues.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                if (uiFieldMap.containsKey(key)) {
                    String cType = uiFieldMap.get(key);
                    // Specific logic for Date type component
                    if (cType.equals(HkSystemConstantUtil.CustomField.ComponentType.DATE)) {
                        if (value != null) {
                            SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS\'Z\'");
                            formatDate.setTimeZone(TimeZone.getTimeZone("UTC"));
                            Date parseDate = null;
                            Date parseIncDate = null;
                            try {
                                // parseDate gives the formatted date
                                parseDate = formatDate.parse(value.toString());
                                Calendar c = Calendar.getInstance();
                                c.setTime(parseDate);
                                c.add(Calendar.DATE, 1);
                                // incremented date contains next date i.e the current date +1
                                parseIncDate = new Date(c.getTimeInMillis());
                            } catch (ParseException ex) {
                                Logger.getLogger(HkStockServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            if (!(key.startsWith("from") || key.startsWith("to"))) {
                                query.addCriteria(Criteria.where(InvoiceFields.FIELD_VALUE + "." + key).gt(parseDate).lt(parseIncDate));
                            } else {
                                if (key.startsWith("from")) {
                                    dateCriteriaMap.put(key, parseDate);
                                } else {
                                    dateCriteriaMap.put(key, parseIncDate);
                                }
                            }
                        }
                    } // Code added for handling image and file but since we are not showing image and file components in search it is of no use as for now
                    else if (cType.equals(HkSystemConstantUtil.CustomField.ComponentType.IMAGE) || cType.equals(HkSystemConstantUtil.CustomField.ComponentType.UPLOAD)) {
                        query.addCriteria(Criteria.where(InvoiceFields.FIELD_VALUE + "." + key).regex(value.toString()));
                    } else {
                        if (value instanceof Collection) {
                            Collection coll = (Collection) value;
                            if (!CollectionUtils.isEmpty(coll)) {
                                query.addCriteria(Criteria.where(InvoiceFields.FIELD_VALUE + "." + key).in(coll));
                            }
                        } else if (value instanceof Date) {
                            long date = Long.parseLong(value.toString());
                            query.addCriteria(Criteria.where(InvoiceFields.FIELD_VALUE + "." + key).is(new Date(date)));
                        } else {
                            if (value != null && value.toString().length() > 0) {
                                query.addCriteria(Criteria.where(InvoiceFields.FIELD_VALUE + "." + key).is(value));
                            }
                        }
                    }
                }

            }

        }
        query.addCriteria(Criteria.where(InvoiceFields.CREATED_ON).gte(afterDate).lte(new Date()));
        if (!CollectionUtils.isEmpty(invoiceIds)) {
            query.addCriteria(Criteria.where(InvoiceFields.OBJECT_ID).in(invoiceIds));
        }
        query.addCriteria(Criteria.where(InvoiceFields.FRANCHISE).in(this.getCompnies(franchise)));
        if (isArchive != null) {
            query.addCriteria(Criteria.where(InvoiceFields.IS_ARCHIVE).is(isArchive));
        }
        query.addCriteria(Criteria.where(InvoiceFields.STATUS).is(HkSystemConstantUtil.StockStatus.NEW_ROUGH));
        if (haveLot != null) {
            query.addCriteria(Criteria.where(InvoiceFields.HAVE_LOT).is(haveLot));
        }
        HkSystemConfigurationDocument systemConfigurationDocument = this.retrieveSystemConfig(HkSystemConstantUtil.FranchiseConfiguration.CURR_VISIBILITY_STATUS);
        if (systemConfigurationDocument != null) {
            if (systemConfigurationDocument.getKeyValue() != null) {
                if (systemConfigurationDocument.getKeyValue().equalsIgnoreCase(HkSystemConstantUtil.SWITCH_OFF)) {
                    query.addCriteria(Criteria.where(InvoiceFields.HAVE_VALUE).is(Boolean.FALSE));
                }
            }
        }
        if (!CollectionUtils.isEmpty(dateCriteriaMap)) {
            Set<String> removedFields = new HashSet<>();
            Iterator<Map.Entry<String, Object>> itr = dateCriteriaMap.entrySet().iterator();
            while (itr.hasNext()) {
                Map.Entry<String, Object> entryMap = itr.next();
                String key = entryMap.getKey();
                Date value = (Date) entryMap.getValue();

                if (!removedFields.contains(key)) {
                    if (key.startsWith("from")) {
                        if (dateCriteriaMap.containsKey("to" + key.substring(4))) {
                            Date toValue = (Date) dateCriteriaMap.get("to" + key.substring(4));
                            query.addCriteria(Criteria.where(InvoiceFields.FIELD_VALUE + "." + key.substring(4)).gte(value).lt(toValue));
                            removedFields.add("to" + key.substring(4));
                            removedFields.add(key);
                        } else {
                            query.addCriteria(Criteria.where(InvoiceFields.FIELD_VALUE + "." + key.substring(4)).gte(value));
                            removedFields.add(key);
                        }
                    } else {
                        if (dateCriteriaMap.containsKey("from" + key.substring(2))) {
                            Date toValue = (Date) dateCriteriaMap.get("from" + key.substring(2));
                            query.addCriteria(Criteria.where(InvoiceFields.FIELD_VALUE + "." + key.substring(2)).gte(toValue).lt(value));
                            removedFields.add("from" + key.substring(2));
                            removedFields.add(key);
                        } else {
                            query.addCriteria(Criteria.where(InvoiceFields.FIELD_VALUE + "." + key.substring(2)).lt(value));
                            removedFields.add(key);
                        }
                    }
                }
            }
        }
        query.with(new Sort(Sort.Direction.DESC, Arrays.asList(InvoiceFields.YEAR, InvoiceFields.SEQUENCE_NUMBER)));
        if (offset != null) {
            query.skip((int) offset);
        }
        if (limit != null) {
            query.limit((int) limit);
        }
        invoiceDocuments = mongoGenericDao.getMongoTemplate().find(query, HkInvoiceDocument.class);
        List<HkInvoiceDocument> finalList = null;

        if (!CollectionUtils.isEmpty(invoiceDocuments)) {
            finalList = new ArrayList<>();
            String pointerComponentType = HkSystemConstantUtil.CustomField.ComponentType.POINTER + "@*" + HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN;

            for (HkInvoiceDocument invoiceDocument : invoiceDocuments) {
                HkInvoiceDocument temp = invoiceDocument;
                Map<String, Object> customMap = invoiceDocument.getFieldValue().toMap();
                uiFieldMap = this.createFieldNameWithComponentType(customMap);
                if (!CollectionUtils.isEmpty(customMap)) {

                    for (Map.Entry<String, String> field : uiFieldMap.entrySet()) {

                        if (field.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN) || field.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.USER_MULTISELECT) || field.getValue().equals(pointerComponentType)) {
                            if (customMap != null && !customMap.isEmpty() && customMap.containsKey(field.getKey())) {
                                String value = customMap.get(field.getKey()).toString().replaceAll("\\[", "").replaceAll("\\]", "");
                                customMap.put(field.getKey(), value);
                            }
                        }
                    }
                    temp.getFieldValue().putAll(customMap);
                    finalList.add(temp);
                }
            }

        }
        return finalList;
    }

    @Override
    public List<HkInvoiceDocument> retrieveInvoices(List<ObjectId> invoiceIds, Long franchise, Boolean isArchive) {
        List<HkInvoiceDocument> invoiceDocuments = null;
        List<HkInvoiceDocument> finalList = null;
        Map<String, String> uiFieldMap = null;
        List<Criteria> criterias = new ArrayList<>();
        if (!CollectionUtils.isEmpty(invoiceIds)) {
            criterias.add(Criteria.where(InvoiceFields.OBJECT_ID).in(invoiceIds));
        }
        if (franchise != null) {
            criterias.add(Criteria.where(InvoiceFields.FRANCHISE).in(this.getCompnies(franchise)));
        }
        if (isArchive != null) {
            criterias.add(Criteria.where(InvoiceFields.IS_ARCHIVE).is(isArchive));
        }
        HkSystemConfigurationDocument systemConfigurationDocument = this.retrieveSystemConfig(HkSystemConstantUtil.FranchiseConfiguration.CURR_VISIBILITY_STATUS);
        if (systemConfigurationDocument != null) {
            if (systemConfigurationDocument.getKeyValue() != null) {
                if (systemConfigurationDocument.getKeyValue().equalsIgnoreCase(HkSystemConstantUtil.SWITCH_OFF)) {
                    criterias.add(Criteria.where(InvoiceFields.HAVE_VALUE).is(Boolean.FALSE));
                }
            }
        }
        invoiceDocuments = mongoGenericDao.findByCriteria(criterias, HkInvoiceDocument.class);
        if (!CollectionUtils.isEmpty(invoiceDocuments)) {
            finalList = new ArrayList<>();
//            uiFieldList = new ArrayList<>();
//            for (HkInvoiceDocument invoiceDocument : invoiceDocuments) {
//                if (invoiceDocument.getFieldValue() != null) {
//                    Map<String, Object> map = invoiceDocument.getFieldValue().toMap();
//                    if (!CollectionUtils.isEmpty(map)) {
//                        for (Map.Entry<String, Object> entrySet : map.entrySet()) {
//                            String key = entrySet.getKey();
//                            Object value = entrySet.getValue();
//                            uiFieldList.add(key);
//                        }
//                    }
//                }
//            }
            //Map<String, String> uiFieldListWithComponentType = customFieldService.retrieveUIFieldNameWithComponentTypes(uiFieldList);
            String pointerComponentType = HkSystemConstantUtil.CustomField.ComponentType.POINTER + "@*" + HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN;

            for (HkInvoiceDocument invoiceDocument : invoiceDocuments) {
                HkInvoiceDocument temp = invoiceDocument;
                Map<String, Object> customMap = invoiceDocument.getFieldValue().toMap();
                uiFieldMap = this.createFieldNameWithComponentType(customMap);
                if (!CollectionUtils.isEmpty(uiFieldMap)) {

                    for (Map.Entry<String, String> field : uiFieldMap.entrySet()) {
                        if (field.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN) || field.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.USER_MULTISELECT) || field.getValue().equals(pointerComponentType)) {
                            if (customMap != null && !customMap.isEmpty() && customMap.containsKey(field.getKey())) {
                                String value = customMap.get(field.getKey()).toString().replaceAll("\\[", "").replaceAll("\\]", "");
                                customMap.put(field.getKey(), value);
                            }
                        }
                    }
                    temp.getFieldValue().putAll(customMap);
                    finalList.add(temp);
                }
            }

        }
        return finalList;
    }

    @Override
    public List<ObjectId> retrieveInvoiceIds(Map<String, Object> fieldValues, long franchise, Boolean isArchive) {
        List<ObjectId> ids = null;
        List<HkInvoiceDocument> invoiceDocuments = null;
        List<Criteria> criterias = new ArrayList<>();
        if (!CollectionUtils.isEmpty(fieldValues)) {
            for (Map.Entry<String, Object> entry : fieldValues.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                if (value instanceof Collection) {
                    Collection coll = (Collection) value;
                    if (!CollectionUtils.isEmpty(coll)) {
                        criterias.add(Criteria.where(InvoiceFields.FIELD_VALUE + "." + key).in(coll));
                    }
                } else if (value instanceof Date) {
                    long date = Long.parseLong(value.toString());
                    criterias.add(Criteria.where(InvoiceFields.FIELD_VALUE + "." + key).is(new Date(date)));
                } else {
                    if (value != null && value.toString().length() > 0) {
                        criterias.add(Criteria.where(InvoiceFields.FIELD_VALUE + "." + key).is(value));
                    }
                }
            }
        }
        criterias.add(Criteria.where(InvoiceFields.FRANCHISE).in(this.getCompnies(franchise)));
        if (isArchive != null) {
            criterias.add(Criteria.where(InvoiceFields.IS_ARCHIVE).is(isArchive));
        }
        HkSystemConfigurationDocument systemConfigurationDocument = this.retrieveSystemConfig(HkSystemConstantUtil.FranchiseConfiguration.CURR_VISIBILITY_STATUS);
        if (systemConfigurationDocument != null) {
            if (systemConfigurationDocument.getKeyValue() != null) {
                if (systemConfigurationDocument.getKeyValue().equalsIgnoreCase(HkSystemConstantUtil.SWITCH_OFF)) {
                    criterias.add(Criteria.where(InvoiceFields.HAVE_VALUE).is(Boolean.FALSE));
                }
            }
        }
        invoiceDocuments = mongoGenericDao.findByCriteria(criterias, HkInvoiceDocument.class);
        if (!CollectionUtils.isEmpty(invoiceDocuments)) {
            ids = new LinkedList<>();
            for (HkInvoiceDocument hkInvoiceDocument : invoiceDocuments) {
                ids.add(hkInvoiceDocument.getId());
            }
        }
        return ids;
    }

    @Override
    @MongoTransaction
    public Boolean updateInvoice(ObjectId id, Map<String, Object> invoiceCustomMap, Map<String, String> invoiceDbTypeMap, Map<String, HkFieldDocument> mapForFeatureInvolvedInFormula, List<HkFieldDocument> totalFormulaList, Map<Long, String> featureIdWithNameMap, Long franchise, Long updatedBy, Long featureId, Integer invoiceSequence, Map<String, String> autogeneratedLabelMap) {
        Boolean result = Boolean.FALSE;
        if (id != null) {
            Map<String, String> uiFieldMap = null;
            if (invoiceCustomMap != null && !invoiceCustomMap.isEmpty()) {
                uiFieldMap = this.createFieldNameWithComponentType(invoiceCustomMap);
            }
            HkInvoiceDocument invoiceDocument = (HkInvoiceDocument) mongoGenericDao.retrieveById(id, HkInvoiceDocument.class);
            if (invoiceSequence != null) {
                invoiceDocument.setSequenceNumber(invoiceSequence);
            }
            BasicBSONObject fieldValues = customFieldService.makeBSONObject(invoiceCustomMap, invoiceDbTypeMap, uiFieldMap, HkSystemConstantUtil.Feature.INVOICE, 0l, null, Boolean.TRUE, invoiceDocument.getFieldValue().toMap(), autogeneratedLabelMap);
            invoiceDocument.setFieldValue(fieldValues);
//            invoiceDocument.getFieldValue().putAll(fieldValues.toMap());
            invoiceDocument.setLastModifiedBy(updatedBy);
            invoiceDocument.setLastModifiedOn(new Date());
            mongoGenericDao.update(invoiceDocument);

            if (invoiceDocument != null) {
                //-------------------------------Code For Formula First Scenario-------------------------------------
                HkStockServiceImpl hkStockServiceImpl = applicationContext.getBean(this.getClass());

                // --- Call generic method of formula for first scenario -------------------------
                Map<String, Double> finalFormulaEvalMap = hkFormulaExecution.mapOfEvaluatedFormulaValueWithDbField(totalFormulaList, franchise, invoiceDocument, null, null, null, null, null, null, null, featureId, HkSystemConstantUtil.FeatureForFormulaEvaluation.EDIT_INVOICE);
                if (finalFormulaEvalMap != null && !finalFormulaEvalMap.isEmpty()) {
                    for (Map.Entry<String, Double> entrySet : finalFormulaEvalMap.entrySet()) {
                        invoiceDocument.getFieldValue().put(entrySet.getKey(), entrySet.getValue());
                    }
                }
                //---- First scenario ends -----------------------------
                mongoGenericDao.update(invoiceDocument);
//                this.evaluateFirstScenarioForEditInvoice(franchise, dbFieldWithFormulaMap, invoiceDocument, featureId);

                HkInvoiceDocument invoiceDocument1 = new HkInvoiceDocument();
                //-------------------------------Code For Formula Second Scenario-------------------------------------
                hkStockServiceImpl.evaluateSecondScenarioForEditInvoice(mapForFeatureInvolvedInFormula, franchise, featureIdWithNameMap, invoiceDocument1);
                result = Boolean.TRUE;
                return result;

            } else {
                return result;
            }

        } else {
            return result;
        }
    }

    @Async
    @MongoTransaction
    public void evaluateSecondScenarioForEditInvoice(Map<String, HkFieldDocument> mapForFeatureInvolvedInFormula, Long franchise, Map<Long, String> featureIdWithNameMap, HkInvoiceDocument invoiceDocument) {
        // Code added By Shifa on 19 feb 2015 for formula Evaluation For Second scenario i.e. on lot update ,update all other douments where lot formula is involved
        //Map<String, HkFieldEntity> mapForFeatureInvolvedInFormula = fieldService.retrieveMapOfDbFieldNameForFeatureInvolvedInFormula(franchise, HkSystemConstantUtil.Feature.INVOICE, HkSystemConstantUtil.CustomField.ComponentType.FORMULA);
        //MM-22-03-2015 Added in Trnasformer Layer
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("js");
        Map<ObjectId, List<HkLotDocument>> parcelIdAndLotDocumentMap = this.retrieveParcelIdAndLotDocumentMap(franchise, Boolean.FALSE);
        Map<ObjectId, List<HkPacketDocument>> parcelIdAndPacketDocumentMap = this.retrieveParcelIdAndPacketDocumentMap(franchise, Boolean.FALSE);
        Map<ObjectId, List<HkPacketDocument>> lotIdAndPacketDocumentMap = this.retrieveLotIdAndPacketDocumentMap(franchise, Boolean.FALSE);
        List<ObjectId> objectInvIds = new ArrayList<>();
        objectInvIds.add(invoiceDocument.getId());
        List<HkParcelDocument> parcelDocForInv = this.retrieveParcels(null, objectInvIds, null, franchise, Boolean.FALSE, null, HkSystemConstantUtil.StockStatus.NEW_ROUGH, null, null);
        List<HkLotDocument> lotDocForInv = this.retrieveLots(null, objectInvIds, null, null, franchise, Boolean.FALSE);
        List<HkPacketDocument> packDocForInv = this.retrievePackets(null, objectInvIds, null, null, franchise, Boolean.FALSE, null, null, null, null);
        Map<ObjectId, HkInvoiceDocument> invoiceIdAndDocumentMap = this.retrieveInvoiceIdAndDocumentMap(franchise, Boolean.FALSE);
        Map<ObjectId, HkParcelDocument> parcelIdAndDocumentMap = this.retrieveParcelIdAndDocumentMap(franchise, Boolean.FALSE);
        Map<ObjectId, HkLotDocument> lotIdAndDocumentMap = this.retrieveLotIdAndDocumentMap(franchise, Boolean.FALSE);

        if (mapForFeatureInvolvedInFormula != null && !mapForFeatureInvolvedInFormula.isEmpty()) {
            for (Map.Entry<String, HkFieldDocument> entrySet : mapForFeatureInvolvedInFormula.entrySet()) {
                String featureName = featureIdWithNameMap.get(entrySet.getValue().getFeature());

// Code If the dbField is Parcel
                if (featureName.equalsIgnoreCase(HkSystemConstantUtil.Feature.PARCEL)) {
                    if (!CollectionUtils.isEmpty(parcelDocForInv)) {
                        for (HkParcelDocument parcelDoc : parcelDocForInv) {
                            String[] formulaValue = entrySet.getValue().getFormulaValue().split("\\|");
                            List<String> modifiedFormula = new ArrayList<>();
                            for (int l = 0; l < formulaValue.length; l++) {
                                if (formulaValue[l].contains(HkSystemConstantUtil.Feature.INVOICE)) {
                                    String formulaSubPart[] = formulaValue[l].split("\\.");
                                    Map invoiceDoc = invoiceIdAndDocumentMap.get(invoiceDocument.getId()).getFieldValue().toMap();
                                    if (invoiceDoc.containsKey(formulaSubPart[1])) {
                                        String invoiceVal = invoiceDoc.get(formulaSubPart[1]).toString();
                                        if (invoiceVal != null) {
                                            formulaValue[l] = invoiceVal;
                                        }
                                    }
                                }
                                if (formulaValue[l].contains(HkSystemConstantUtil.Feature.PARCEL)) {
                                    String formulaSubPart[] = formulaValue[l].split("\\.");
                                    String parcelVal = parcelDoc.getFieldValue().get(formulaSubPart[1]).toString();
                                    if (parcelVal != null) {
                                        formulaValue[l] = parcelVal;
                                    }
                                }
                                if (formulaValue[l].contains(HkSystemConstantUtil.Feature.LOT)) {
                                    String formulaSubPart[] = formulaValue[l].split("\\.");
                                    if (l >= 2 && (formulaValue[l - 2].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.SUM)
                                            || formulaValue[l - 2].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MIN)
                                            || formulaValue[l - 2].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MAX)
                                            || formulaValue[l - 2].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.AVG)
                                            || formulaValue[l - 2].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.COUNT))) {
                                        if (parcelIdAndLotDocumentMap != null && !parcelIdAndDocumentMap.isEmpty()) {
                                            if (parcelIdAndLotDocumentMap.containsKey(parcelDoc.getId())) {
                                                List<HkLotDocument> lotDocsForParcelDoc = parcelIdAndLotDocumentMap.get(parcelDoc.getId());
                                                Double aggregrateFunctionResult = this.calculateLotAggegateFunction(lotDocsForParcelDoc, formulaSubPart[1], formulaValue[l - 2]);
                                                if (aggregrateFunctionResult != null) {
                                                    formulaValue[l] = aggregrateFunctionResult.toString();
                                                }
                                            }

                                        }
                                    }

                                }
                                if (formulaValue[l].contains(HkSystemConstantUtil.Feature.PACKET)) {
                                    String formulaSubPart[] = formulaValue[l].split("\\.");
                                    if (l >= 2 && (formulaValue[l - 2].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.SUM)
                                            || formulaValue[l - 2].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MIN)
                                            || formulaValue[l - 2].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MAX)
                                            || formulaValue[l - 2].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.AVG)
                                            || formulaValue[l - 2].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.COUNT))) {
                                        if (parcelIdAndPacketDocumentMap != null && !parcelIdAndPacketDocumentMap.isEmpty()) {
                                            if (parcelIdAndPacketDocumentMap.containsKey(parcelDoc.getId())) {
                                                List<HkPacketDocument> packetDocsForParcelDoc = parcelIdAndPacketDocumentMap.get(parcelDoc.getId());
                                                Double aggregrateFunctionResult = this.calculatePacketAggegateFunction(packetDocsForParcelDoc, formulaSubPart[1], formulaValue[l - 2]);
                                                if (aggregrateFunctionResult != null) {
                                                    formulaValue[l] = aggregrateFunctionResult.toString();
                                                }
                                            }
                                        }

                                    }
                                }

                            }
                            //
                            for (String oldFormula : formulaValue) {
                                if (!(oldFormula.equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.SUM)) && !(oldFormula.equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MIN))
                                        && !(oldFormula.equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MAX)) && !(oldFormula.equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.AVG))
                                        && !(oldFormula.equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.COUNT)) && !(oldFormula.equalsIgnoreCase("(")) && !(oldFormula.equalsIgnoreCase(")"))) {
                                    modifiedFormula.add(oldFormula);
                                }
                            }
                            String[] newForm = new String[modifiedFormula.size()];
                            newForm = modifiedFormula.toArray(newForm);

                            String finalModFormula = Arrays.toString(newForm).replaceAll("|", "").replaceAll(",", "");
                            String finalModFormulaString = finalModFormula.replace("[", "").replace("]", "");
                            try {
                                Object form_eval = engine.eval(finalModFormulaString);
                                parcelDoc.getFieldValue().put(entrySet.getKey(), Double.parseDouble(form_eval.toString()));
                                mongoGenericDao.update(parcelDoc);
                            } catch (ScriptException | NumberFormatException e) {
                                continue;
                            }

                        }

                    }

                }
                        // Code for handling parcel feature ends here

                // Code if the feature is lot starts here
                if (featureName.equalsIgnoreCase(HkSystemConstantUtil.Feature.LOT)) {
                    if (!CollectionUtils.isEmpty(lotDocForInv)) {
                        for (HkLotDocument lotDoc : lotDocForInv) {
                            String[] formulaValue = entrySet.getValue().getFormulaValue().split("\\|");
                            List<String> modifiedFormula = new ArrayList<>();
                            for (int l = 0; l < formulaValue.length; l++) {
                                if (formulaValue[l].contains(HkSystemConstantUtil.Feature.INVOICE)) {
                                    String formulaSubPart[] = formulaValue[l].split("\\.");
                                    Map invoiceDoc = invoiceIdAndDocumentMap.get(invoiceDocument.getId()).getFieldValue().toMap();
                                    if (invoiceDoc.containsKey(formulaSubPart[1])) {
                                        String invoiceVal = invoiceDoc.get(formulaSubPart[1]).toString();
                                        if (invoiceVal != null) {
                                            formulaValue[l] = invoiceVal;
                                        }
                                    }
                                }
                                if (formulaValue[l].contains(HkSystemConstantUtil.Feature.PARCEL)) {
                                    String formulaSubPart[] = formulaValue[l].split("\\.");
                                    Map parcelDoc = parcelIdAndDocumentMap.get(lotDoc.getParcel()).getFieldValue().toMap();
                                    if (parcelDoc.containsKey(formulaSubPart[1])) {
                                        String parcelVal = parcelDoc.get(formulaSubPart[1]).toString();
                                        if (parcelVal != null) {
                                            formulaValue[l] = parcelVal;
                                        }
                                    }
                                }
                                if (formulaValue[l].contains(HkSystemConstantUtil.Feature.LOT)) {
                                    String formulaSubPart[] = formulaValue[l].split("\\.");
                                    String parcelVal = lotDoc.getFieldValue().get(formulaSubPart[1]).toString();
                                    if (parcelVal != null) {
                                        formulaValue[l] = parcelVal;
                                    }

                                }
                                if (formulaValue[l].contains(HkSystemConstantUtil.Feature.PACKET)) {
                                    String formulaSubPart[] = formulaValue[l].split("\\.");
                                    if (l >= 2 && (formulaValue[l - 2].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.SUM)
                                            || formulaValue[l - 2].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MIN)
                                            || formulaValue[l - 2].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MAX)
                                            || formulaValue[l - 2].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.AVG)
                                            || formulaValue[l - 2].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.COUNT))) {
                                        if (lotIdAndPacketDocumentMap != null && !lotIdAndPacketDocumentMap.isEmpty()) {
                                            if (lotIdAndPacketDocumentMap.containsKey(lotDoc.getId())) {
                                                List<HkPacketDocument> packetDocsForLotDoc = lotIdAndPacketDocumentMap.get(lotDoc.getId());
                                                Double aggregrateFunctionResult = this.calculatePacketAggegateFunction(packetDocsForLotDoc, formulaSubPart[1], formulaValue[l - 2]);
                                                if (aggregrateFunctionResult != null) {
                                                    formulaValue[l] = aggregrateFunctionResult.toString();
                                                }
                                            }
                                        }

                                    }
                                }

                            }
                            //
                            for (String oldFormula : formulaValue) {
                                if (!(oldFormula.equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.SUM)) && !(oldFormula.equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MIN))
                                        && !(oldFormula.equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MAX)) && !(oldFormula.equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.AVG))
                                        && !(oldFormula.equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.COUNT)) && !(oldFormula.equalsIgnoreCase("(")) && !(oldFormula.equalsIgnoreCase(")"))) {
                                    modifiedFormula.add(oldFormula);
                                }
                            }
                            String[] newForm = new String[modifiedFormula.size()];
                            newForm = modifiedFormula.toArray(newForm);

                            String finalModFormula = Arrays.toString(newForm).replaceAll("|", "").replaceAll(",", "");
                            String finalModFormulaString = finalModFormula.replace("[", "").replace("]", "");
                            try {
                                Object form_eval = engine.eval(finalModFormulaString);
                                lotDoc.getFieldValue().put(entrySet.getKey(), Double.parseDouble(form_eval.toString()));
                                mongoGenericDao.update(lotDoc);
                            } catch (ScriptException | NumberFormatException e) {

                                continue;
                            }

                        }

                    }

                }
                        // Code for handling lot ends here

                // Code if the feature is Packet starts here
                if (featureName.equalsIgnoreCase(HkSystemConstantUtil.Feature.PACKET)) {
                    if (!CollectionUtils.isEmpty(packDocForInv)) {
                        for (HkPacketDocument packetDoc : packDocForInv) {
                            String[] formulaValue = entrySet.getValue().getFormulaValue().split("\\|");
                            List<String> modifiedFormula = new ArrayList<>();
                            for (int l = 0; l < formulaValue.length; l++) {
                                if (formulaValue[l].contains(HkSystemConstantUtil.Feature.INVOICE)) {
                                    String formulaSubPart[] = formulaValue[l].split("\\.");
                                    Map invoiceDoc = invoiceIdAndDocumentMap.get(invoiceDocument.getId()).getFieldValue().toMap();
                                    if (invoiceDoc.containsKey(formulaSubPart[1])) {
                                        String invoiceVal = invoiceDoc.get(formulaSubPart[1]).toString();
                                        if (invoiceVal != null) {
                                            formulaValue[l] = invoiceVal;
                                        }
                                    }
                                }
                                if (formulaValue[l].contains(HkSystemConstantUtil.Feature.PARCEL)) {
                                    String formulaSubPart[] = formulaValue[l].split("\\.");
                                    Map parcelDoc = parcelIdAndDocumentMap.get(packetDoc.getParcel()).getFieldValue().toMap();
                                    if (parcelDoc.containsKey(formulaSubPart[1])) {
                                        String parcelVal = parcelDoc.get(formulaSubPart[1]).toString();
                                        if (parcelVal != null) {
                                            formulaValue[l] = parcelVal;
                                        }
                                    }
                                }
                                if (formulaValue[l].contains(HkSystemConstantUtil.Feature.LOT)) {
                                    String formulaSubPart[] = formulaValue[l].split("\\.");
                                    Map lotDoc = lotIdAndDocumentMap.get(packetDoc.getLot()).getFieldValue().toMap();
                                    if (lotDoc.containsKey(formulaSubPart[1])) {
                                        String lotVal = lotDoc.get(formulaSubPart[1]).toString();
                                        if (lotVal != null) {
                                            formulaValue[l] = lotVal;
                                        }
                                    }

                                }
                                if (formulaValue[l].contains(HkSystemConstantUtil.Feature.PACKET)) {
                                    String formulaSubPart[] = formulaValue[l].split("\\.");
                                    String packetVal = packetDoc.getFieldValue().get(formulaSubPart[1]).toString();
                                    if (packetVal != null) {
                                        formulaValue[l] = packetVal;
                                    }
                                }

                            }
                            //
                            for (String oldFormula : formulaValue) {
                                if (!(oldFormula.equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.SUM)) && !(oldFormula.equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MIN))
                                        && !(oldFormula.equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MAX)) && !(oldFormula.equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.AVG))
                                        && !(oldFormula.equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.COUNT)) && !(oldFormula.equalsIgnoreCase("(")) && !(oldFormula.equalsIgnoreCase(")"))) {
                                    modifiedFormula.add(oldFormula);
                                }
                            }
                            String[] newForm = new String[modifiedFormula.size()];
                            newForm = modifiedFormula.toArray(newForm);

                            String finalModFormula = Arrays.toString(newForm).replaceAll("|", "").replaceAll(",", "");
                            String finalModFormulaString = finalModFormula.replace("[", "").replace("]", "");
                            try {
                                Object form_eval = engine.eval(finalModFormulaString);
                                packetDoc.getFieldValue().put(entrySet.getKey(), Double.parseDouble(form_eval.toString()));
                                mongoGenericDao.update(packetDoc);
                            } catch (ScriptException | NumberFormatException e) {
                                continue;
                            }

                        }

                    }

                }
                // Code for handling Packet ends here

            }

        }
// Code for second scenario ends here
    }

    //------------------------------------------ Invoice Service Ends -------------------------------------//
    //----------------------------------------Parcel Service starts ---------------------------------------//
    @Override
    public HkParcelDocument
            retrieveParcelById(ObjectId parcelId) {
        HkParcelDocument document = (HkParcelDocument) mongoGenericDao.retrieveById(parcelId, HkParcelDocument.class
        );
        if (document
                != null) {
            String pointerComponentType = HkSystemConstantUtil.CustomField.ComponentType.POINTER + "@*" + HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN;
            Map<String, String> uiFieldMap = null;
            Map<String, Object> customMap = document.getFieldValue().toMap();
            uiFieldMap = this.createFieldNameWithComponentType(customMap);
            if (!CollectionUtils.isEmpty(uiFieldMap)) {
                for (Map.Entry<String, String> field : uiFieldMap.entrySet()) {
                    if (field.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN) || field.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.USER_MULTISELECT) || field.getValue().equals(pointerComponentType)) {
                        if (customMap != null && !customMap.isEmpty() && customMap.containsKey(field.getKey())) {
                            String value = customMap.get(field.getKey()).toString().replaceAll("\\[", "").replaceAll("\\]", "");
                            customMap.put(field.getKey(), value);
                        }
                    }
                }
                document.getFieldValue().putAll(customMap);
            }
        }
        return document;
    }

    @Override
    public List<HkParcelDocument> retrieveAllParcel(Long franchise, Boolean isArchive, List<String> statusList, List<ObjectId> parcelIds) {
        List<Criteria> criterias = new ArrayList<>();
        List<HkParcelDocument> documents = null;
        criterias.add(Criteria.where(ParcelFields.FRANCHISE).in(this.getCompnies(franchise)));
        if (isArchive != null) {
            criterias.add(Criteria.where(ParcelFields.IS_ARCHIVE).is(isArchive));
        }
        criterias.add(Criteria.where(ParcelFields.STOCK_PIECES).gt(0l));
        criterias.add(Criteria.where(ParcelFields.STOCK_CARAT).gt(0d));
        if (!CollectionUtils.isEmpty(statusList) && CollectionUtils.isEmpty(parcelIds)) {
            criterias.add(Criteria.where(ParcelFields.STATUS).in(statusList));
        } else if (!CollectionUtils.isEmpty(statusList) && !CollectionUtils.isEmpty(parcelIds)) {
            criterias.add(new Criteria().orOperator(Criteria.where(ParcelFields.STATUS).in(statusList), Criteria.where(ParcelFields.PARCEL_ID).in(parcelIds)));
        } else if (CollectionUtils.isEmpty(statusList) && !CollectionUtils.isEmpty(parcelIds)) {
            criterias.add(Criteria.where(ParcelFields.PARCEL_ID).in(parcelIds));

        }
        documents = mongoGenericDao.findByCriteria(criterias, HkParcelDocument.class);
        if (!CollectionUtils.isEmpty(documents)) {
            for (HkParcelDocument hkParcelDocument : documents) {
                String pointerComponentType = HkSystemConstantUtil.CustomField.ComponentType.POINTER + "@*" + HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN;
                Map<String, String> uiFieldMap = null;
                Map<String, Object> customMap = hkParcelDocument.getFieldValue().toMap();
                uiFieldMap = this.createFieldNameWithComponentType(customMap);
                if (!CollectionUtils.isEmpty(uiFieldMap)) {
                    for (Map.Entry<String, String> field : uiFieldMap.entrySet()) {
                        if (field.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN) || field.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.USER_MULTISELECT) || field.getValue().equals(pointerComponentType)) {
                            if (customMap != null && !customMap.isEmpty() && customMap.containsKey(field.getKey())) {
                                String value = customMap.get(field.getKey()).toString().replaceAll("\\[", "").replaceAll("\\]", "");
                                customMap.put(field.getKey(), value);
                            }
                        }
                    }
                    hkParcelDocument.getFieldValue().putAll(customMap);
                }
            }
        }
        return documents;
    }

    @Override
    @MongoTransaction
    public void deleteParcel(ObjectId parcelId, Long updatedBy) {
        HkParcelDocument document = (HkParcelDocument) mongoGenericDao.retrieveById(parcelId, HkParcelDocument.class);
        document.setStatus(HkSystemConstantUtil.StockStatus.ARCHIVED);

        document.setLastModifiedBy(updatedBy);

        document.setLastModifiedOn(
                new Date());
        document.setIsArchive(
                true);
        mongoGenericDao.update(document);

        this.convertRoughStockDetail(document.getStockPieces(), (Double) document.getFieldValue().get(HkSystemConstantUtil.ParcelStaticFieldName.ORIGINAL_RATES), document.getStockCarat(), document, HkSystemConstantUtil.RoughStockActions.DEBIT, HkSystemConstantUtil.RoughStockOperations.ROUGH, updatedBy, null, null);
    }

    @Override
    public List<HkParcelDocument> retrieveParcels(Map<String, Object> fieldValues, List<ObjectId> invoiceIds, List<ObjectId> parcelIds, long franchise, Boolean isArchieve, Boolean haveLot, String status, Integer offset, Integer limit) {
        Query query = new Query();
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -1);
        Date afterDate = cal.getTime();
        List<HkParcelDocument> parcelDocuments = null;
        Map<String, String> uiFieldMap = null;
        Map<String, Object> dateCriteriaMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(fieldValues)) {
            uiFieldMap = this.createFieldNameWithComponentType(fieldValues);

            for (Map.Entry<String, Object> entry : fieldValues.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                if (uiFieldMap.containsKey(key)) {
                    String cType = uiFieldMap.get(key);
                    // Specific logic for Date type component
                    if (cType.equals(HkSystemConstantUtil.CustomField.ComponentType.DATE)) {
                        if (value != null) {
                            SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS\'Z\'");
                            formatDate.setTimeZone(TimeZone.getTimeZone("UTC"));
                            Date parseDate = null;
                            Date parseIncDate = null;
                            try {
                                // parseDate gives the formatted date
                                parseDate = formatDate.parse(value.toString());
                                Calendar c = Calendar.getInstance();
                                c.setTime(parseDate);
                                c.add(Calendar.DATE, 1);
                                // incremented date contains next date i.e the current date +1
//                                String incrementedDate = formatDate.format(c.getTime()); // new date
                                parseIncDate = new Date(c.getTimeInMillis());

                            } catch (ParseException ex) {
                                Logger.getLogger(HkStockServiceImpl.class
                                        .getName()).log(Level.SEVERE, null, ex);
                            }
                            if (!(key.startsWith("from") || key.startsWith("to"))) {
                                query.addCriteria(Criteria.where(ParcelFields.FIELD_VALUE + "." + key).gt(parseDate).lt(parseIncDate));
                            } else {
                                if (key.startsWith("from")) {
                                    dateCriteriaMap.put(key, parseDate);
                                } else {
                                    dateCriteriaMap.put(key, parseIncDate);
                                }
                            }
                        }
                    } // Code added for handling image and file but since we are not showing image and file components in search it is of no use as for now
                    else if (cType.equals(HkSystemConstantUtil.CustomField.ComponentType.IMAGE) || cType.equals(HkSystemConstantUtil.CustomField.ComponentType.UPLOAD)) {
                        query.addCriteria(Criteria.where(ParcelFields.FIELD_VALUE + "." + key).regex(value.toString()));
                    } else {
                        if (value instanceof Collection) {
                            Collection coll = (Collection) value;
                            if (!CollectionUtils.isEmpty(coll)) {
                                query.addCriteria(Criteria.where(ParcelFields.FIELD_VALUE + "." + key).in(coll));
                            }
                        } else if (value instanceof Date) {
                            long date = Long.parseLong(value.toString());
                            query.addCriteria(Criteria.where(ParcelFields.FIELD_VALUE + "." + key).is(new Date(date)));
                        } else {
                            if (value != null && value.toString().length() > 0) {
                                query.addCriteria(Criteria.where(ParcelFields.FIELD_VALUE + "." + key).is(value));
                            }
                        }
                    }
                }
            }
        }
        query.addCriteria(Criteria.where(ParcelFields.CREATED_ON).gte(afterDate).lte(new Date()));
        if (!CollectionUtils.isEmpty(invoiceIds)) {
            query.addCriteria(Criteria.where(ParcelFields.INVOICE_ID).in(invoiceIds));
        }
        if (!CollectionUtils.isEmpty(parcelIds)) {
            query.addCriteria(Criteria.where(ParcelFields.OBJECT_ID).in(parcelIds));
        }
        query.addCriteria(Criteria.where(ParcelFields.FRANCHISE).in(this.getCompnies(franchise)));
        if (isArchieve != null) {
            query.addCriteria(Criteria.where(ParcelFields.IS_ARCHIVE).is(isArchieve));
        }
        if (status != null) {
            query.addCriteria(Criteria.where(ParcelFields.STATUS).is(status));
        }
        if (haveLot != null) {
            query.addCriteria(Criteria.where(ParcelFields.HAVE_LOT).is(haveLot));
        }
        HkSystemConfigurationDocument systemConfigurationDocument = this.retrieveSystemConfig(HkSystemConstantUtil.FranchiseConfiguration.CURR_VISIBILITY_STATUS);
        if (systemConfigurationDocument != null) {
            if (systemConfigurationDocument.getKeyValue() != null) {
                if (systemConfigurationDocument.getKeyValue().equalsIgnoreCase(HkSystemConstantUtil.SWITCH_OFF)) {
                    query.addCriteria(Criteria.where(ParcelFields.HAVE_VALUE).is(Boolean.FALSE));
                }
            }
        }
        if (!CollectionUtils.isEmpty(dateCriteriaMap)) {
            Set<String> removedFields = new HashSet<>();
            Iterator<Map.Entry<String, Object>> itr = dateCriteriaMap.entrySet().iterator();
            while (itr.hasNext()) {
                Map.Entry<String, Object> entryMap = itr.next();
                String key = entryMap.getKey();
                Date value = (Date) entryMap.getValue();

                if (!removedFields.contains(key)) {
                    if (key.startsWith("from")) {
                        if (dateCriteriaMap.containsKey("to" + key.substring(4))) {
                            Date toValue = (Date) dateCriteriaMap.get("to" + key.substring(4));
                            query.addCriteria(Criteria.where(ParcelFields.FIELD_VALUE + "." + key.substring(4)).gte(value).lt(toValue));
                            removedFields.add("to" + key.substring(4));
                            removedFields.add(key);
                        } else {
                            query.addCriteria(Criteria.where(ParcelFields.FIELD_VALUE + "." + key.substring(4)).gte(value));
                            removedFields.add(key);
                        }
                    } else {
                        if (dateCriteriaMap.containsKey("from" + key.substring(2))) {
                            Date toValue = (Date) dateCriteriaMap.get("from" + key.substring(2));
                            query.addCriteria(Criteria.where(ParcelFields.FIELD_VALUE + "." + key.substring(2)).gte(toValue).lt(value));
                            removedFields.add("from" + key.substring(2));
                            removedFields.add(key);
                        } else {
                            query.addCriteria(Criteria.where(ParcelFields.FIELD_VALUE + "." + key.substring(2)).lt(value));
                            removedFields.add(key);

                        }
                    }
                }
            }
        }
        query.with(new Sort(Sort.Direction.DESC, Arrays.asList(InvoiceFields.YEAR, InvoiceFields.SEQUENCE_NUMBER)));
        if (offset != null) {
            query.skip((int) offset);
        }
        if (limit != null) {
            query.limit((int) limit);
        }
        parcelDocuments = mongoGenericDao.getMongoTemplate().find(query, HkParcelDocument.class);
        List<String> uiFieldList = null;
        List<HkParcelDocument> finalList = null;

        if (!CollectionUtils.isEmpty(parcelDocuments)) {
            finalList = new ArrayList<>();
            String pointerComponentType = HkSystemConstantUtil.CustomField.ComponentType.POINTER + "@*" + HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN;

            for (HkParcelDocument parcelDocument : parcelDocuments) {
                HkParcelDocument temp = parcelDocument;
                Map<String, Object> customMap = parcelDocument.getFieldValue().toMap();
                uiFieldMap = this.createFieldNameWithComponentType(customMap);
                if (!CollectionUtils.isEmpty(uiFieldMap)) {
                    for (Map.Entry<String, String> field : uiFieldMap.entrySet()) {
                        if (field.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN) || field.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.USER_MULTISELECT) || field.getValue().equals(pointerComponentType)) {
                            if (customMap != null && !customMap.isEmpty() && customMap.containsKey(field.getKey())) {
                                String value = customMap.get(field.getKey()).toString().replaceAll("\\[", "").replaceAll("\\]", "");
                                customMap.put(field.getKey(), value);
                            }
                        }
                    }
                    temp.getFieldValue().putAll(customMap);
                    finalList.add(temp);
                }
            }
            if (CollectionUtils.isEmpty(finalList)) {
                finalList = parcelDocuments;
            }
        }
        return finalList;
    }

    @Override
    public List<HkParcelDocument> retrieveParcels(List<ObjectId> parcelIds, Long franchise, Boolean isArchive) {
        List<HkParcelDocument> parcelDocuments = null;
        Map<String, String> uiFieldMap = null;
        List<HkParcelDocument> finalList = null;
        List<Criteria> criterias = new ArrayList<>();
        if (!CollectionUtils.isEmpty(parcelIds)) {
            criterias.add(Criteria.where(ParcelFields.OBJECT_ID).in(parcelIds));
        }
        if (franchise != null) {
            criterias.add(Criteria.where(ParcelFields.FRANCHISE).in(this.getCompnies(franchise)));
        }
        if (isArchive != null) {
            criterias.add(Criteria.where(ParcelFields.IS_ARCHIVE).is(isArchive));
        }
        HkSystemConfigurationDocument systemConfigurationDocument = this.retrieveSystemConfig(HkSystemConstantUtil.FranchiseConfiguration.CURR_VISIBILITY_STATUS);
        if (systemConfigurationDocument != null) {
            if (systemConfigurationDocument.getKeyValue() != null) {
                if (systemConfigurationDocument.getKeyValue().equalsIgnoreCase(HkSystemConstantUtil.SWITCH_OFF)) {
                    criterias.add(Criteria.where(ParcelFields.HAVE_VALUE).is(Boolean.FALSE));

                }
            }
        }
        parcelDocuments = mongoGenericDao.findByCriteria(criterias, HkParcelDocument.class
        );
        if (!CollectionUtils.isEmpty(parcelDocuments)) {
            finalList = new ArrayList<>();
            String pointerComponentType = HkSystemConstantUtil.CustomField.ComponentType.POINTER + "@*" + HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN;

            for (HkParcelDocument parcelDocument : parcelDocuments) {
                HkParcelDocument temp = parcelDocument;
                Map<String, Object> customMap = parcelDocument.getFieldValue().toMap();
                uiFieldMap = this.createFieldNameWithComponentType(customMap);
                if (!CollectionUtils.isEmpty(uiFieldMap)) {
                    for (Map.Entry<String, String> field : uiFieldMap.entrySet()) {
                        if (field.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN) || field.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.USER_MULTISELECT) || field.getValue().equals(pointerComponentType)) {
                            if (customMap != null && !customMap.isEmpty() && customMap.containsKey(field.getKey())) {
                                String value = customMap.get(field.getKey()).toString().replaceAll("\\[", "").replaceAll("\\]", "");
                                customMap.put(field.getKey(), value);
                            }
                        }
                    }
                    temp.getFieldValue().putAll(customMap);
                    finalList.add(temp);
                }
            }

        }
        return finalList;
    }

    @Override
    public List<ObjectId> retrieveParcelIds(Map<String, Object> fieldValues, List<ObjectId> invoiceIds, long franchise, Boolean isActive) {
        List<ObjectId> ids = null;
        List<HkParcelDocument> parcelDocuments = null;
        List<Criteria> criterias = new ArrayList<>();
        if (!CollectionUtils.isEmpty(fieldValues)) {
            for (Map.Entry<String, Object> entry : fieldValues.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                if (value instanceof Collection) {
                    Collection coll = (Collection) value;
                    if (!CollectionUtils.isEmpty(coll)) {
                        criterias.add(Criteria.where(ParcelFields.FIELD_VALUE + "." + key).in(coll));
                    }
                } else if (value instanceof Date) {
                    long date = Long.parseLong(value.toString());
                    criterias.add(Criteria.where(ParcelFields.FIELD_VALUE + "." + key).is(new Date(date)));
                } else {
                    if (value != null && value.toString().length() > 0) {
                        criterias.add(Criteria.where(ParcelFields.FIELD_VALUE + "." + key).is(value));
                    }
                }
            }
        }
        if (!CollectionUtils.isEmpty(invoiceIds)) {
            criterias.add(Criteria.where(ParcelFields.INVOICE_ID).in(invoiceIds));
        }
        criterias.add(Criteria.where(ParcelFields.FRANCHISE).in(this.getCompnies(franchise)));
        if (isActive != null) {
            criterias.add(Criteria.where(ParcelFields.IS_ARCHIVE).is(isActive));
        }
        HkSystemConfigurationDocument systemConfigurationDocument = this.retrieveSystemConfig(HkSystemConstantUtil.FranchiseConfiguration.CURR_VISIBILITY_STATUS);
        if (systemConfigurationDocument != null) {
            if (systemConfigurationDocument.getKeyValue() != null) {
                if (systemConfigurationDocument.getKeyValue().equalsIgnoreCase(HkSystemConstantUtil.SWITCH_OFF)) {
                    criterias.add(Criteria.where(ParcelFields.HAVE_VALUE).is(Boolean.FALSE));

                }
            }
        }
        parcelDocuments = mongoGenericDao.findByCriteria(criterias, HkParcelDocument.class
        );
        if (!CollectionUtils.isEmpty(parcelDocuments)) {
            ids = new LinkedList<>();
            for (HkParcelDocument hkParcelDocument : parcelDocuments) {
                ids.add(hkParcelDocument.getId());
            }
        }
        return ids;
    }

    @Override
    @MongoTransaction
    public Boolean updateParcel(ObjectId id, Map<String, Object> parcelCustomMap, Map<String, String> parcelDbTypeMap, Map<String, HkFieldDocument> mapForFeatureInvolvedInFormula, Map<String, String> uiFieldMap, Map<String, String> lotUIFieldMap, List<HkFieldDocument> totalformulaList, Map<Long, String> featureIdWithNameMap, Long franchise, Long updatedBy, Long featureId, Integer parcelSequence, Map<String, String> autoGeneratedLabelMap) {
        Boolean result = Boolean.FALSE;
        if (id != null) {
            Long pieces = null;
            Double carat = null;
            Double rate = null;
            HkParcelDocument parcelDocument = (HkParcelDocument) mongoGenericDao.retrieveById(id, HkParcelDocument.class
            );
            BasicBSONObject fieldValues = customFieldService.makeBSONObject(parcelCustomMap, parcelDbTypeMap, uiFieldMap, HkSystemConstantUtil.Feature.PARCEL, franchise, null, Boolean.TRUE, parcelDocument.getFieldValue().toMap(), autoGeneratedLabelMap);
            if (parcelDocument
                    != null) {
                Map<String, Object> toMap = parcelDocument.getFieldValue().toMap();
                if (toMap.containsKey(HkSystemConstantUtil.ParcelStaticFieldName.ORIGINAL_CARAT) && toMap.get(HkSystemConstantUtil.ParcelStaticFieldName.ORIGINAL_CARAT) != null) {
                    parcelDocument.setStockCarat((Double) toMap.get(HkSystemConstantUtil.ParcelStaticFieldName.ORIGINAL_CARAT));
                    carat = (Double) toMap.get(HkSystemConstantUtil.ParcelStaticFieldName.ORIGINAL_CARAT);
                }
                if (toMap.containsKey(HkSystemConstantUtil.ParcelStaticFieldName.ORIGINAL_PIECES) && toMap.get(HkSystemConstantUtil.ParcelStaticFieldName.ORIGINAL_PIECES) != null) {
                    parcelDocument.setStockPieces((Long) toMap.get(HkSystemConstantUtil.ParcelStaticFieldName.ORIGINAL_PIECES));
                    pieces = (Long) toMap.get(HkSystemConstantUtil.ParcelStaticFieldName.ORIGINAL_PIECES);
                }
                if (toMap.containsKey(HkSystemConstantUtil.ParcelStaticFieldName.ORIGINAL_RATES) && toMap.get(HkSystemConstantUtil.ParcelStaticFieldName.ORIGINAL_RATES) != null) {
                    rate = (Double) toMap.get(HkSystemConstantUtil.ParcelStaticFieldName.ORIGINAL_RATES);
                }
                if (parcelSequence != null) {
                    parcelDocument.setSequenceNumber(parcelSequence);
                }
//                parcelDocument.getFieldValue().putAll(fieldValues.toMap());
                parcelDocument.setLastModifiedBy(updatedBy);
                parcelDocument.setLastModifiedOn(new Date());
                parcelDocument.setFieldValue(fieldValues);

                //---------------------------Formula First Scenario----------------------------------
//                this.evaluateFormulaForFirstScenarioForUpdateParcel(parcelDocument, dbFieldWithFormulaMap, franchise, featureId);
                Map<String, Double> finalFormulaEvalMap = hkFormulaExecution.mapOfEvaluatedFormulaValueWithDbField(totalformulaList, franchise, null, parcelDocument, null, null, null, parcelDocument.getInvoice(), parcelDocument.getId(), null, featureId, HkSystemConstantUtil.FeatureForFormulaEvaluation.EDIT_PARCEL);
                if (finalFormulaEvalMap != null && !finalFormulaEvalMap.isEmpty()) {
                    for (Map.Entry<String, Double> entrySet : finalFormulaEvalMap.entrySet()) {
                        parcelDocument.getFieldValue().put(entrySet.getKey(), entrySet.getValue());
                    }
                }
                mongoGenericDao.update(parcelDocument);
                List<HkRoughStockDetailDocument> hkRoughStockDetailDocuments = this.retrieveRoughStockDetailByParcel(id);
                if (!CollectionUtils.isEmpty(hkRoughStockDetailDocuments)) {
                    this.convertRoughStockDetail(pieces, rate, carat, parcelDocument, HkSystemConstantUtil.RoughStockActions.CREDIT, HkSystemConstantUtil.RoughStockOperations.ROUGH, updatedBy, hkRoughStockDetailDocuments.get(0), null);
                }

//---------------------------Formula Second Scenario----------------------------------
                HkStockServiceImpl hkStockServiceImpl = applicationContext.getBean(this.getClass());
                hkStockServiceImpl.evaluateFormulaForSecondScenarioForUpdateParcel(id, parcelDocument, franchise, mapForFeatureInvolvedInFormula, featureIdWithNameMap);
                result = Boolean.TRUE;
            }
        }
        return result;
    }

    @Override
    public Integer getNextParcelSequence() {
        Integer sequenceNumber = 0;
        Query query = new Query();
        query.addCriteria(Criteria.where(ParcelFields.IS_ARCHIVE).is(Boolean.FALSE));
        query.addCriteria(Criteria.where(ParcelFields.YEAR).is(Calendar.getInstance().get(Calendar.YEAR)));
        query.with(new Sort(Sort.Direction.DESC, ParcelFields.SEQUENCE_NUMBER)).limit(1);
        HkParcelDocument hkParcelDocument = mongoGenericDao.getMongoTemplate().findOne(query, HkParcelDocument.class
        );
        if (hkParcelDocument
                != null) {
            sequenceNumber = hkParcelDocument.getSequenceNumber();
        }
        return sequenceNumber;
    }

    @Override
    public Boolean isParcelSequenceExist(Integer parcelId, ObjectId parcelObjectId) {
        Boolean parcelExist = Boolean.FALSE;
        List<HkParcelDocument> parcelDocuments = new ArrayList<>();
        List<Criteria> criterias = new ArrayList<>();
        if (parcelId != null) {
            criterias.add(Criteria.where(ParcelFields.SEQUENCE_NUMBER).is(parcelId));
        }
        criterias.add(Criteria.where(ParcelFields.YEAR).is(Calendar.getInstance().get(Calendar.YEAR)));
        if (parcelObjectId != null) {
            criterias.add(Criteria.where(ParcelFields.OBJECT_ID).ne(parcelObjectId));
        }
        criterias.add(Criteria.where(InvoiceFields.IS_ARCHIVE).is(Boolean.FALSE));
        parcelDocuments
                = mongoGenericDao.findByCriteria(criterias, HkParcelDocument.class
                );
        if (!CollectionUtils.isEmpty(parcelDocuments)) {
            parcelExist = Boolean.TRUE;
        }
        return parcelExist;
    }

    @Override
    @MongoTransaction
    public String createParcel(BasicBSONObject basicBSONObject, List<HkFieldDocument> totalformulaList, Long franchise, Long createdBy, ObjectId invoiceId, Long featureId, Integer parcelSequence) {
        Long pieces = null;
        Double carat = null;
        Double rate = null;
        List<HkParcelDocument> parcelDocumentsForFormulaScenarios = null;
        if (basicBSONObject != null) {
            parcelDocumentsForFormulaScenarios = new ArrayList<>();
            Boolean hasValue = Boolean.FALSE;
            HkInvoiceDocument invoiceDocument = null;

            if (invoiceId != null) {
                invoiceDocument = (HkInvoiceDocument) mongoGenericDao.retrieveById(invoiceId, HkInvoiceDocument.class
                );
                if (invoiceDocument
                        != null) {
                    hasValue = invoiceDocument.getHaveValue();
                }
            }
            HkParcelDocument parcelDocument = new HkParcelDocument();
            parcelDocument.setInvoice(invoiceId);
            parcelDocument.setCreatedByFranchise(franchise);
            parcelDocument.setIsLinked(Boolean.FALSE);
            parcelDocument.setFranchiseId(0L);
            parcelDocument.setIsArchive(Boolean.FALSE);
            parcelDocument.setCreatedBy(createdBy);
            parcelDocument.setLastModifiedBy(createdBy);
            parcelDocument.setYear(Calendar.getInstance().get(Calendar.YEAR));
            parcelDocument.setCreatedOn(new Date());
            parcelDocument.setLastModifiedOn(new Date());
            parcelDocument.setStatus(HkSystemConstantUtil.StockStatus.NEW_ROUGH);
            parcelDocument.setHaveValue(hasValue);
            if (parcelSequence == null) {
                parcelSequence = this.getNextParcelSequence();
                parcelSequence++;
            }
            parcelDocument.setSequenceNumber(parcelSequence);
            parcelDocument.setYear(Calendar.getInstance().get(Calendar.YEAR));
            if (!basicBSONObject.containsField(HkSystemConstantUtil.ParcelStaticFields.PARCEL_ID) || basicBSONObject.get(HkSystemConstantUtil.ParcelStaticFields.PARCEL_ID) == null) {
                basicBSONObject.put(HkSystemConstantUtil.ParcelStaticFields.PARCEL_ID, Calendar.getInstance().get(Calendar.YEAR) + "-" + parcelSequence);
            }
            if (!basicBSONObject.containsField(HkSystemConstantUtil.ParcelStaticFieldName.IN_STOCK_OF) || basicBSONObject.get(HkSystemConstantUtil.ParcelStaticFieldName.IN_STOCK_OF) == null) {
                if (invoiceDocument != null) {
                    if (invoiceDocument.getFieldValue() != null && invoiceDocument.getFieldValue().containsField(HkSystemConstantUtil.InvoiceStaticFieldName.IN_STOCK_OF)) {
                        @SuppressWarnings("unchecked")
                        List<String> inStockOf = (ArrayList<String>) invoiceDocument.getFieldValue().get(HkSystemConstantUtil.InvoiceStaticFieldName.IN_STOCK_OF);
                        basicBSONObject.put(HkSystemConstantUtil.ParcelStaticFieldName.IN_STOCK_OF, inStockOf);
                    }

                }
            }
            if (basicBSONObject.containsField(HkSystemConstantUtil.ParcelStaticFieldName.ORIGINAL_CARAT) && basicBSONObject.get(HkSystemConstantUtil.ParcelStaticFieldName.ORIGINAL_CARAT) != null) {
                parcelDocument.setStockCarat((Double) basicBSONObject.get(HkSystemConstantUtil.ParcelStaticFieldName.ORIGINAL_CARAT));
                carat = (Double) basicBSONObject.get(HkSystemConstantUtil.ParcelStaticFieldName.ORIGINAL_CARAT);
            }
            if (basicBSONObject.containsField(HkSystemConstantUtil.ParcelStaticFieldName.ORIGINAL_PIECES) && basicBSONObject.get(HkSystemConstantUtil.ParcelStaticFieldName.ORIGINAL_PIECES) != null) {
                parcelDocument.setStockPieces((Long) basicBSONObject.get(HkSystemConstantUtil.ParcelStaticFieldName.ORIGINAL_PIECES));
                pieces = (Long) basicBSONObject.get(HkSystemConstantUtil.ParcelStaticFieldName.ORIGINAL_PIECES);
            }
            if (basicBSONObject.containsField(HkSystemConstantUtil.ParcelStaticFieldName.ORIGINAL_RATES) && basicBSONObject.get(HkSystemConstantUtil.ParcelStaticFieldName.ORIGINAL_RATES) != null) {
                rate = (Double) basicBSONObject.get(HkSystemConstantUtil.ParcelStaticFieldName.ORIGINAL_RATES);
            }
            parcelDocument.setFieldValue(basicBSONObject);
            ///----- Call for generic method of formula first scenario
            Map<String, Double> finalFormulaEvalMap = hkFormulaExecution.mapOfEvaluatedFormulaValueWithDbField(totalformulaList, franchise, invoiceDocument, parcelDocument, null, null, null, invoiceId, null, null, featureId, HkSystemConstantUtil.FeatureForFormulaEvaluation.ADD_PARCEL);
            if (finalFormulaEvalMap != null && !finalFormulaEvalMap.isEmpty()) {
                for (Map.Entry<String, Double> entrySet : finalFormulaEvalMap.entrySet()) {
                    parcelDocument.getFieldValue().put(entrySet.getKey(), entrySet.getValue());
                }
            }
            //----- code ends here
            mongoGenericDao.create(parcelDocument);
            this.convertRoughStockDetail(pieces, rate, carat, parcelDocument, HkSystemConstantUtil.RoughStockActions.CREDIT, HkSystemConstantUtil.RoughStockOperations.ROUGH, createdBy, null, null);
            //---------------------Add in list for formula
            parcelDocumentsForFormulaScenarios.add(parcelDocument);

//            this.evaluateFormulaForFirstScenarioForAddParcel(dbFieldWithFormulaMap, franchise, parcelDocumentsForFormulaScenarios, invoiceId, featureId);
            return parcelDocument.getId().toString();
        }
        return null;
    }

    public void convertRoughStockDetail(Long pieces, Double rate, Double carat, HkParcelDocument hkParcelDocument, String action, String operation, Long createdBy, HkRoughStockDetailDocument hkRoughStockDetailDocument, ObjectId lotId) {
        if (hkRoughStockDetailDocument == null) {
            hkRoughStockDetailDocument = new HkRoughStockDetailDocument();
            hkRoughStockDetailDocument.setCreatedBy(createdBy);
            hkRoughStockDetailDocument.setCreatedOn(new Date());
        }
        hkRoughStockDetailDocument.setParcel(hkParcelDocument.getId());
//        if (!StringUtils.isEmpty(action)) {
        hkRoughStockDetailDocument.setAction(action);
//        }
//        if (!StringUtils.isEmpty(operation)) {
        hkRoughStockDetailDocument.setOperation(operation);
//        }
//        if (pieces != null) {
        hkRoughStockDetailDocument.setPieces(pieces);
//        }
//        if (carat != null) {
        hkRoughStockDetailDocument.setCarat(carat);
//        }
//        if (rate != null) {
        hkRoughStockDetailDocument.setRate(rate);
//        }
        hkRoughStockDetailDocument.setLotId(lotId);
//        hkRoughStockDetailDocument.setYear(hkParcelDocument.getYear());
        hkRoughStockDetailDocument.setEmpId(hkRoughStockDetailDocument.getLastModifiedBy());
        this.createOrUpdateRoughStockDetail(hkRoughStockDetailDocument);
    }

    private void createLotStockDetail(Long pieces, Double rate, Double carat, HkLotDocument hkLotDocument, String action, String operation, Long createdBy, ObjectId packetId, HkLotStockDetailDocument hkLotStockDetailDocument) {
        if (hkLotStockDetailDocument == null) {
            hkLotStockDetailDocument = new HkLotStockDetailDocument();
            hkLotStockDetailDocument.setCreatedBy(createdBy);
            hkLotStockDetailDocument.setCreatedOn(new Date());
        }
        hkLotStockDetailDocument.setLot(hkLotDocument.getId());
        hkLotStockDetailDocument.setOperation(operation);
        hkLotStockDetailDocument.setPieces(pieces);
        hkLotStockDetailDocument.setCarat(carat);
        hkLotStockDetailDocument.setRate(rate);
        hkLotStockDetailDocument.setPacketId(packetId);
        hkLotStockDetailDocument.setAction(operation);
        hkLotStockDetailDocument.setEmpId(hkLotStockDetailDocument.getLastModifiedBy());
        hkLotStockDetailDocument.setAction(action);
        mongoGenericDao.update(hkLotStockDetailDocument);
    }

    private void convertPacketStockDetail(Long pieces, Double rate, Double carat, HkPacketDocument hkPacketDocument, String action, String operation, Long createdBy, HkPacketStockDetailDocument hkPacketStockDetailDocument) {
        if (hkPacketStockDetailDocument == null) {
            hkPacketStockDetailDocument = new HkPacketStockDetailDocument();
            hkPacketStockDetailDocument.setCreatedBy(createdBy);
            hkPacketStockDetailDocument.setCreatedOn(new Date());
        }
        System.out.println("hkPacketDocument:::" + hkPacketDocument);
        hkPacketStockDetailDocument.setPacket(hkPacketDocument.getId());
        hkPacketStockDetailDocument.setAction(action);
        hkPacketStockDetailDocument.setOperation(operation);
        hkPacketStockDetailDocument.setPieces(pieces);
        hkPacketStockDetailDocument.setCarat(carat);
        hkPacketStockDetailDocument.setRate(rate);
        hkPacketStockDetailDocument.setEmpId(hkPacketStockDetailDocument.getLastModifiedBy());
        mongoGenericDao.update(hkPacketStockDetailDocument);
    }

    @MongoTransaction
    @Override
    public void createOrUpdateRoughStockDetail(HkRoughStockDetailDocument hkRoughStockDetailDocument) {
        if (hkRoughStockDetailDocument != null) {
            mongoGenericDao.update(hkRoughStockDetailDocument);
        }
    }

    @MongoTransaction
    @Override
    public void updateRoughStockDetailsWithParcel(List<HkRoughStockDetailDocument> hkRoughStockDetailDocuments, HkSellDocument hkSellDocument) {
        if (!CollectionUtils.isEmpty(hkRoughStockDetailDocuments)) {
            for (HkRoughStockDetailDocument hkRoughStockDetailDocument : hkRoughStockDetailDocuments) {
                HkParcelDocument hkParcelDocument = retrieveParcelById(hkRoughStockDetailDocument.getParcel());
                if (hkParcelDocument != null) {
                    if (hkSellDocument != null) {
                        List<HkSellParcelDetailDocument> hkSellParcelDetailDocuments = hkSellDocument.getHkSellParcelDetailDocuments();
                        if (!CollectionUtils.isEmpty(hkSellParcelDetailDocuments)) {
                            for (HkSellParcelDetailDocument hkSellParcelDetailDocument : hkSellParcelDetailDocuments) {
                                if (hkSellParcelDetailDocument.getParcel().equals(hkParcelDocument.getId())) {
                                    if (hkParcelDocument.getStockCarat() != null && hkSellParcelDetailDocument.getSellCarats() != null) {
                                        Double stockCarat = hkParcelDocument.getStockCarat();
                                        Double newCaratValue = (stockCarat + hkSellParcelDetailDocument.getSellCarats()) - hkRoughStockDetailDocument.getCarat();
                                        hkParcelDocument.setStockCarat(newCaratValue);
                                    }
                                    if (hkParcelDocument.getStockPieces() != null && hkSellParcelDetailDocument.getSellPieces() != null) {
                                        Long stockPieces = hkParcelDocument.getStockPieces();
                                        Long newPiecesValue = (stockPieces + hkSellParcelDetailDocument.getSellPieces()) - hkRoughStockDetailDocument.getPieces();
                                        hkParcelDocument.setStockPieces(newPiecesValue);
                                    }
                                    mongoGenericDao.update(hkParcelDocument);
                                }
                            }
                        }
                    } else {
                        if (hkParcelDocument.getStockCarat() != null) {
                            Double stockCarat = hkParcelDocument.getStockCarat();
                            Double newCaratValue = stockCarat - hkRoughStockDetailDocument.getCarat();
                            hkParcelDocument.setStockCarat(newCaratValue);
                        }
                        if (hkParcelDocument.getStockPieces() != null) {
                            Long stockPieces = hkParcelDocument.getStockPieces();
                            Long newPiecesValue = stockPieces - hkRoughStockDetailDocument.getPieces();
                            hkParcelDocument.setStockPieces(newPiecesValue);
                        }
                        mongoGenericDao.update(hkParcelDocument);
                    }

                }
                hkRoughStockDetailDocument.setParcel(hkParcelDocument.getId());
                mongoGenericDao.update(hkRoughStockDetailDocument);
            }

        }
    }

    public List<HkRoughStockDetailDocument> retrieveRoughStockDetailByParcel(ObjectId parcelId) {
        List<HkRoughStockDetailDocument> hkRoughStockDetailDocuments = null;
        List<Criteria> criterias = new ArrayList<>();
        if (parcelId != null) {
            criterias.add(Criteria.where(RoughStockDetailFields.PARCEL).is(parcelId));

        }
        criterias.add(Criteria.where(RoughStockDetailFields.ACTION).is(HkSystemConstantUtil.RoughStockActions.CREDIT));
        hkRoughStockDetailDocuments
                = mongoGenericDao.findByCriteria(criterias, HkRoughStockDetailDocument.class
                );
        return hkRoughStockDetailDocuments;
    }

    public HkRoughStockDetailDocument retrieveRoughStockDetailByLot(ObjectId lotId) {
        HkRoughStockDetailDocument hkRoughStockDetailDocument = null;
        List<Criteria> criterias = new ArrayList<>();
        if (lotId != null) {
            criterias.add(Criteria.where(RoughStockDetailFields.LOT_ID).is(lotId));

        }
        hkRoughStockDetailDocument = (HkRoughStockDetailDocument) mongoGenericDao.findOneByCriteria(criterias, HkRoughStockDetailDocument.class
        );
        return hkRoughStockDetailDocument;
    }

    public HkRoughStockDetailDocument
            retrieveRoughStockDetailById(ObjectId roughStockId) {
        return (HkRoughStockDetailDocument) mongoGenericDao.retrieveById(roughStockId, HkRoughStockDetailDocument.class
        );
    }

    public List<HkRoughStockDetailDocument> retrieveRoughStockDetailByIds(List<ObjectId> roughStockIds) {
        List<HkRoughStockDetailDocument> hkRoughStockDetailDocuments = null;
        List<Criteria> criterias = new ArrayList<>();
        if (roughStockIds != null) {
            criterias.add(Criteria.where(RoughStockDetailFields.OBJECT_ID).in(roughStockIds));

        }
        hkRoughStockDetailDocuments = mongoGenericDao.findByCriteria(criterias, HkRoughStockDetailDocument.class
        );
        return hkRoughStockDetailDocuments;
    }

    @Async
    @MongoTransaction
    public void evaluateFormulaForSecondScenarioForUpdateParcel(ObjectId id, HkParcelDocument parcelDocument, Long franchise, Map<String, HkFieldDocument> mapForFeatureInvolvedInFormula, Map<Long, String> featureIdWithNameMap) {
        // Code added By Shifa on 19 feb 2015 for formula Evaluation For Second scenario i.e. on lot update ,update all other douments where lot formula is involved
        // Map<String, HkFieldEntity> mapForFeatureInvolvedInFormula = fieldService.retrieveMapOfDbFieldNameForFeatureInvolvedInFormula(franchise, HkSystemConstantUtil.Feature.PARCEL, HkSystemConstantUtil.CustomField.ComponentType.FORMULA);
        List<ObjectId> objectInvIds = new ArrayList<>();
        objectInvIds.add(id);
        List<HkLotDocument> lotDocForParcel = this.retrieveLots(null, null, objectInvIds, null, franchise, Boolean.FALSE);
        List<HkPacketDocument> packDocForParcel = this.retrievePackets(null, objectInvIds, null, null, franchise, false, null, null, null, null);
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("js");

        Map<ObjectId, HkInvoiceDocument> invoiceIdAndDocumentMap = this.retrieveInvoiceIdAndDocumentMap(franchise, Boolean.FALSE);
        Map<ObjectId, HkParcelDocument> parcelIdAndDocumentMap = this.retrieveParcelIdAndDocumentMap(franchise, Boolean.FALSE);
        Map<ObjectId, HkLotDocument> lotIdAndDocumentMap = this.retrieveLotIdAndDocumentMap(franchise, Boolean.FALSE);

        Map<ObjectId, List<HkLotDocument>> parcelIdAndLotDocumentMap = this.retrieveParcelIdAndLotDocumentMap(franchise, Boolean.FALSE);
        Map<ObjectId, List<HkPacketDocument>> parcelIdAndPacketDocumentMap = this.retrieveParcelIdAndPacketDocumentMap(franchise, Boolean.FALSE);
        Map<ObjectId, List<HkPacketDocument>> lotIdAndPacketDocumentMap = this.retrieveLotIdAndPacketDocumentMap(franchise, Boolean.FALSE);
        Map<ObjectId, List<HkParcelDocument>> invoiceIdAndParcelDocumentMap = this.retrieveInvoiceIdAndParcelDocumentMap(franchise, Boolean.FALSE);
        Map<ObjectId, List<HkLotDocument>> invoiceIdAndLotDocumentMap = this.retrieveInvoiceIdAndLotDocumentMap(franchise, Boolean.FALSE);
        Map<ObjectId, List<HkPacketDocument>> invoiceIdAndPacketDocumentMap = this.retrieveInvoiceIdAndPacketDocumentMap(franchise, Boolean.FALSE);
        HkInvoiceDocument invoiceDocument = invoiceIdAndDocumentMap.get(parcelDocument.getInvoice());
        if (mapForFeatureInvolvedInFormula != null && !mapForFeatureInvolvedInFormula.isEmpty()) {
            for (Map.Entry<String, HkFieldDocument> entrySet : mapForFeatureInvolvedInFormula.entrySet()) {
                String featureName = featureIdWithNameMap.get(entrySet.getValue().getFeature());

                // Code If the dbField is Invoice
                if (featureName.equalsIgnoreCase(HkSystemConstantUtil.Feature.INVOICE)) {
                    String[] formulaValue = entrySet.getValue().getFormulaValue().split("\\|");
                    List<String> modifiedFormula = new ArrayList<>();
                    for (int l = 0; l < formulaValue.length; l++) {
                        if (formulaValue[l].contains(HkSystemConstantUtil.Feature.INVOICE)) {
                            String formulaSubPart[] = formulaValue[l].split("\\.");
                            Map invoicelDoc = invoiceIdAndDocumentMap.get(parcelDocument.getInvoice()).getFieldValue().toMap();
                            if (invoicelDoc.containsKey(formulaSubPart[1])) {
                                String invoiceVal = invoicelDoc.get(formulaSubPart[1]).toString();
                                if (invoiceVal != null) {
                                    formulaValue[l] = invoiceVal;
                                }
                            }
                        }
                        if (formulaValue[l].contains(HkSystemConstantUtil.Feature.PARCEL)) {
                            String formulaSubPart[] = formulaValue[l].split("\\.");
                            if (l >= 2 && (formulaValue[l - 2].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.SUM)
                                    || formulaValue[l - 2].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MIN)
                                    || formulaValue[l - 2].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MAX)
                                    || formulaValue[l - 2].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.AVG)
                                    || formulaValue[l - 2].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.COUNT))) {
                                if (invoiceIdAndParcelDocumentMap != null && !invoiceIdAndParcelDocumentMap.isEmpty()) {
                                    if (invoiceIdAndParcelDocumentMap.containsKey(parcelDocument.getInvoice())) {
                                        List<HkParcelDocument> parcelDocsForInvoice = invoiceIdAndParcelDocumentMap.get(parcelDocument.getInvoice());
                                        Double aggregrateFunctionResult = this.calculateParcelAggegateFunction(parcelDocsForInvoice, formulaSubPart[1], formulaValue[l - 2]);
                                        if (aggregrateFunctionResult != null) {
                                            formulaValue[l] = aggregrateFunctionResult.toString();
                                        }
                                    }

                                }

                            }
                        }
                        if (formulaValue[l].contains(HkSystemConstantUtil.Feature.LOT)) {
                            String formulaSubPart[] = formulaValue[l].split("\\.");
                            if (l >= 2 && (formulaValue[l - 2].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.SUM)
                                    || formulaValue[l - 2].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MIN)
                                    || formulaValue[l - 2].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MAX)
                                    || formulaValue[l - 2].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.AVG)
                                    || formulaValue[l - 2].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.COUNT))) {
                                if (invoiceIdAndLotDocumentMap != null && !invoiceIdAndLotDocumentMap.isEmpty()) {
                                    if (invoiceIdAndLotDocumentMap.containsKey(parcelDocument.getInvoice())) {
                                        List<HkLotDocument> lotDocsForInvoice = invoiceIdAndLotDocumentMap.get(parcelDocument.getInvoice());
                                        Double aggregrateFunctionResult = this.calculateLotAggegateFunction(lotDocsForInvoice, formulaSubPart[1], formulaValue[l - 2]);
                                        if (aggregrateFunctionResult != null) {
                                            formulaValue[l] = aggregrateFunctionResult.toString();
                                        }
                                    }

                                }

                            }

                        }
                        if (formulaValue[l].contains(HkSystemConstantUtil.Feature.PACKET)) {
                            String formulaSubPart[] = formulaValue[l].split("\\.");
                            if (l >= 2 && (formulaValue[l - 2].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.SUM)
                                    || formulaValue[l - 2].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MIN)
                                    || formulaValue[l - 2].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MAX)
                                    || formulaValue[l - 2].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.AVG)
                                    || formulaValue[l - 2].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.COUNT))) {
                                if (invoiceIdAndPacketDocumentMap != null && !invoiceIdAndPacketDocumentMap.isEmpty()) {
                                    if (invoiceIdAndPacketDocumentMap.containsKey(parcelDocument.getInvoice())) {
                                        List<HkPacketDocument> packetDocsForInvoice = invoiceIdAndPacketDocumentMap.get(parcelDocument.getInvoice());
                                        Double aggregrateFunctionResult = this.calculatePacketAggegateFunction(packetDocsForInvoice, formulaSubPart[1], formulaValue[l - 2]);
                                        if (aggregrateFunctionResult != null) {
                                            formulaValue[l] = aggregrateFunctionResult.toString();
                                        }
                                    }

                                }

                            }
                        }

                    }
                    //
                    for (String oldFormula : formulaValue) {
                        if (!(oldFormula.equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.SUM)) && !(oldFormula.equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MIN))
                                && !(oldFormula.equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MAX)) && !(oldFormula.equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.AVG))
                                && !(oldFormula.equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.COUNT)) && !(oldFormula.equalsIgnoreCase("(")) && !(oldFormula.equalsIgnoreCase(")"))) {
                            modifiedFormula.add(oldFormula);
                        }
                    }
                    String[] newForm = new String[modifiedFormula.size()];
                    newForm = modifiedFormula.toArray(newForm);

                    String finalModFormula = Arrays.toString(newForm).replaceAll("|", "").replaceAll(",", "");
                    String finalModFormulaString = finalModFormula.replace("[", "").replace("]", "");
                    //
                    try {
                        Object form_eval = engine.eval(finalModFormulaString);
                        invoiceDocument.getFieldValue().put(entrySet.getKey(), Double.parseDouble(form_eval.toString()));
                        mongoGenericDao.update(invoiceDocument);
                    } catch (ScriptException | NumberFormatException e) {
                        continue;
                    }

                }
                // Code for handling invoice feature ends here

                // Code if the feature is lot starts here
                if (featureName.equalsIgnoreCase(HkSystemConstantUtil.Feature.LOT)) {
                    if (!CollectionUtils.isEmpty(lotDocForParcel)) {
                        for (HkLotDocument lotDoc : lotDocForParcel) {
                            String[] formulaValue = entrySet.getValue().getFormulaValue().split("\\|");
                            List<String> modifiedFormula = new ArrayList<>();
                            for (int l = 0; l < formulaValue.length; l++) {
                                if (formulaValue[l].contains(HkSystemConstantUtil.Feature.INVOICE)) {
                                    String formulaSubPart[] = formulaValue[l].split("\\.");
                                    Map invoicelDoc = invoiceIdAndDocumentMap.get(lotDoc.getInvoice()).getFieldValue().toMap();
                                    if (invoicelDoc.containsKey(formulaSubPart[1])) {
                                        String invoiceVal = invoicelDoc.get(formulaSubPart[1]).toString();
                                        if (invoiceVal != null) {
                                            formulaValue[l] = invoiceVal;
                                        }
                                    }
                                }
                                if (formulaValue[l].contains(HkSystemConstantUtil.Feature.PARCEL)) {
                                    String formulaSubPart[] = formulaValue[l].split("\\.");
                                    String parcelVal = parcelDocument.getFieldValue().get(formulaSubPart[1]).toString();
                                    if (parcelVal != null) {
                                        formulaValue[l] = parcelVal;
                                    }
                                }
                                if (formulaValue[l].contains(HkSystemConstantUtil.Feature.LOT)) {
                                    String formulaSubPart[] = formulaValue[l].split("\\.");
                                    String lotVal = lotDoc.getFieldValue().get(formulaSubPart[1]).toString();
                                    if (lotVal != null) {
                                        formulaValue[l] = lotVal;
                                    }

                                }
                                if (formulaValue[l].contains(HkSystemConstantUtil.Feature.PACKET)) {
                                    String formulaSubPart[] = formulaValue[l].split("\\.");
                                    if (l >= 2 && (formulaValue[l - 2].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.SUM)
                                            || formulaValue[l - 2].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MIN)
                                            || formulaValue[l - 2].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MAX)
                                            || formulaValue[l - 2].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.AVG)
                                            || formulaValue[l - 2].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.COUNT))) {
                                        if (lotIdAndPacketDocumentMap != null && !lotIdAndPacketDocumentMap.isEmpty()) {
                                            if (lotIdAndPacketDocumentMap.containsKey(lotDoc.getId())) {
                                                List<HkPacketDocument> packetDocsForLotDoc = lotIdAndPacketDocumentMap.get(lotDoc.getId());
                                                Double aggregrateFunctionResult = this.calculatePacketAggegateFunction(packetDocsForLotDoc, formulaSubPart[1], formulaValue[l - 2]);
                                                if (aggregrateFunctionResult != null) {
                                                    formulaValue[l] = aggregrateFunctionResult.toString();
                                                }
                                            }
                                        }

                                    }
                                }

                            }
                            //
                            for (String oldFormula : formulaValue) {
                                if (!(oldFormula.equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.SUM)) && !(oldFormula.equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MIN))
                                        && !(oldFormula.equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MAX)) && !(oldFormula.equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.AVG))
                                        && !(oldFormula.equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.COUNT)) && !(oldFormula.equalsIgnoreCase("(")) && !(oldFormula.equalsIgnoreCase(")"))) {
                                    modifiedFormula.add(oldFormula);
                                }
                            }
                            String[] newForm = new String[modifiedFormula.size()];
                            newForm = modifiedFormula.toArray(newForm);

                            String finalModFormula = Arrays.toString(newForm).replaceAll("|", "").replaceAll(",", "");
                            String finalModFormulaString = finalModFormula.replace("[", "").replace("]", "");
                            //
                            try {
                                Object form_eval = engine.eval(finalModFormulaString);
                                lotDoc.getFieldValue().put(entrySet.getKey(), Double.parseDouble(form_eval.toString()));
                                mongoGenericDao.update(lotDoc);
                            } catch (ScriptException | NumberFormatException e) {
                                continue;
                            }

                        }

                    }

                }
                        // Code for handling lot ends here

                // Code if the feature is Packet starts here
                if (featureName.equalsIgnoreCase(HkSystemConstantUtil.Feature.PACKET)) {
                    if (!CollectionUtils.isEmpty(packDocForParcel)) {
                        for (HkPacketDocument packetDoc : packDocForParcel) {
                            String[] formulaValue = entrySet.getValue().getFormulaValue().split("\\|");
                            List<String> modifiedFormula = new ArrayList<>();
                            for (int l = 0; l < formulaValue.length; l++) {
                                if (formulaValue[l].contains(HkSystemConstantUtil.Feature.INVOICE)) {
                                    String formulaSubPart[] = formulaValue[l].split("\\.");
                                    Map invoiceDoc = invoiceIdAndDocumentMap.get(packetDoc.getInvoice()).getFieldValue().toMap();
                                    if (invoiceDoc.containsKey(formulaSubPart[1])) {
                                        String invoiceVal = invoiceDoc.get(formulaSubPart[1]).toString();
                                        if (invoiceVal != null) {
                                            formulaValue[l] = invoiceVal;
                                        }
                                    }
                                }
                                if (formulaValue[l].contains(HkSystemConstantUtil.Feature.PARCEL)) {
                                    String formulaSubPart[] = formulaValue[l].split("\\.");
                                    Map parcelDoc = parcelIdAndDocumentMap.get(packetDoc.getParcel()).getFieldValue().toMap();
                                    if (parcelDoc.containsKey(formulaSubPart[1])) {
                                        String parcelVal = parcelDoc.get(formulaSubPart[1]).toString();
                                        if (parcelVal != null) {
                                            formulaValue[l] = parcelVal;
                                        }
                                    }
                                }
                                if (formulaValue[l].contains(HkSystemConstantUtil.Feature.LOT)) {
                                    String formulaSubPart[] = formulaValue[l].split("\\.");
                                    Map lotDoc = lotIdAndDocumentMap.get(packetDoc.getLot()).getFieldValue().toMap();
                                    if (lotDoc.containsKey(formulaSubPart[1])) {
                                        String lotVal = lotDoc.get(formulaSubPart[1]).toString();
                                        if (lotVal != null) {
                                            formulaValue[l] = lotVal;
                                        }
                                    }

                                }
                                if (formulaValue[l].contains(HkSystemConstantUtil.Feature.PACKET)) {
                                    String formulaSubPart[] = formulaValue[l].split("\\.");
                                    String packetVal = packetDoc.getFieldValue().get(formulaSubPart[1]).toString();
                                    if (packetVal != null) {
                                        formulaValue[l] = packetVal;
                                    }
                                }

                            }
                            //
                            for (String oldFormula : formulaValue) {
                                if (!(oldFormula.equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.SUM)) && !(oldFormula.equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MIN))
                                        && !(oldFormula.equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MAX)) && !(oldFormula.equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.AVG))
                                        && !(oldFormula.equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.COUNT)) && !(oldFormula.equalsIgnoreCase("(")) && !(oldFormula.equalsIgnoreCase(")"))) {
                                    modifiedFormula.add(oldFormula);
                                }
                            }
                            String[] newForm = new String[modifiedFormula.size()];
                            newForm = modifiedFormula.toArray(newForm);

                            String finalModFormula = Arrays.toString(newForm).replaceAll("|", "").replaceAll(",", "");
                            String finalModFormulaString = finalModFormula.replace("[", "").replace("]", "");
                            try {
                                Object form_eval = engine.eval(finalModFormulaString);
                                packetDoc.getFieldValue().put(entrySet.getKey(), Double.parseDouble(form_eval.toString()));
                                mongoGenericDao.update(packetDoc);
                            } catch (ScriptException | NumberFormatException e) {
                                continue;
                            }

                        }

                    }

                }
                // Code for handling Packet ends here

            }

        }

// Code for second scenario ends here
    }

    @MongoTransaction
    @Override
    public Boolean mergeParcel(ObjectId parentInvoice, List<ObjectId> mergedFrom, List<BasicBSONObject> basicBSONObjects, Long franchise, Long createdBy) {
        Boolean result = Boolean.FALSE;
        if (!CollectionUtils.isEmpty(basicBSONObjects)) {
            HkInvoiceDocument invoiceDocument = null;
            HkParcelDocument parcelDocument = new HkParcelDocument();
            if (parentInvoice != null) {
                parcelDocument.setInvoice(parentInvoice);
                invoiceDocument
                        = (HkInvoiceDocument) mongoGenericDao.retrieveById(parentInvoice, HkInvoiceDocument.class
                        );
                if (invoiceDocument
                        != null) {
                    if (invoiceDocument.getHaveValue() != null) {
                        parcelDocument.setHaveValue(invoiceDocument.getHaveValue());
                    } else {
                        parcelDocument.setHaveValue(Boolean.FALSE);
                    }
                } else {
                    return result;
                }
            }

            parcelDocument.setFranchiseId(franchise);
            parcelDocument.setIsArchive(Boolean.FALSE);
            parcelDocument.setCreatedBy(createdBy);
            parcelDocument.setLastModifiedBy(createdBy);
            parcelDocument.setCreatedOn(new Date());
            parcelDocument.setLastModifiedOn(new Date());
            parcelDocument.setHaveLot(Boolean.FALSE);
            parcelDocument.setStatus(HkSystemConstantUtil.StockStatus.NEW_ROUGH);
            parcelDocument.setYear(new Date().getYear());
            List<HkParcelDocument> listToUpdate = new ArrayList<>();
            List<HkParcelDocument> parcelDocuments = this.retrieveParcelsbyIds(mergedFrom);
            if (!CollectionUtils.isEmpty(mergedFrom)) {
                for (HkParcelDocument hkParcelDocument : parcelDocuments) {
                    hkParcelDocument.setLastModifiedBy(createdBy);
                    hkParcelDocument.setLastModifiedOn(new Date());
                    if (hkParcelDocument.getStockPieces() == 0l || hkParcelDocument.getStockCarat() == new Double(0)) {
                        hkParcelDocument.setStatus(HkSystemConstantUtil.StockStatus.MERGED);
                    } else {
                        hkParcelDocument.setStatus(HkSystemConstantUtil.StockStatus.PARTIALLY_MERGED);
                    }
                    listToUpdate.add(hkParcelDocument);
                }
            }
            parcelDocument.setMergedFrom(mergedFrom);
            for (HkParcelDocument hkParcelDocument : listToUpdate) {
                mongoGenericDao.update(hkParcelDocument);
            }
            BasicBSONObject fieldValues = basicBSONObjects.get(0);
            if (fieldValues != null) {
                if (!fieldValues.containsField(HkSystemConstantUtil.ParcelStaticFields.PARCEL_ID) || fieldValues.get(HkSystemConstantUtil.ParcelStaticFields.PARCEL_ID) == null) {
                    Integer parcelSequence = this.getNextParcelSequence();
                    if (parcelSequence != null) {
                        parcelSequence++;
                        fieldValues.put(HkSystemConstantUtil.ParcelStaticFields.PARCEL_ID, Calendar.getInstance().get(Calendar.YEAR) + "-" + parcelSequence);
                    }
                }
                if (fieldValues.containsField(HkSystemConstantUtil.ParcelStaticFieldName.ORIGINAL_CARAT) && fieldValues.get(HkSystemConstantUtil.ParcelStaticFieldName.ORIGINAL_CARAT) != null) {
                    parcelDocument.setStockCarat((Double) fieldValues.get(HkSystemConstantUtil.ParcelStaticFieldName.ORIGINAL_CARAT));
                }
                if (fieldValues.containsField(HkSystemConstantUtil.ParcelStaticFieldName.ORIGINAL_PIECES) && fieldValues.get(HkSystemConstantUtil.ParcelStaticFieldName.ORIGINAL_PIECES) != null) {
                    parcelDocument.setStockPieces((Long) fieldValues.get(HkSystemConstantUtil.ParcelStaticFieldName.ORIGINAL_PIECES));
                }
            }
            parcelDocument.setFieldValue(fieldValues);
            mongoGenericDao.create(parcelDocument);
            HkRoughStockDetailDocument hkRoughStockDetailDocument = new HkRoughStockDetailDocument();
            hkRoughStockDetailDocument.setCreatedBy(createdBy);
            hkRoughStockDetailDocument.setCreatedOn(new Date());
            this.convertRoughStockDetail(parcelDocument.getStockPieces(), (Double) fieldValues.get(HkSystemConstantUtil.ParcelStaticFieldName.ORIGINAL_RATES), parcelDocument.getStockCarat(), parcelDocument, HkSystemConstantUtil.RoughStockActions.CREDIT, HkSystemConstantUtil.RoughStockOperations.MERGE, createdBy, hkRoughStockDetailDocument, null);
            result = Boolean.TRUE;
        }
        return result;
    }

    private List<HkParcelDocument> retrieveParcelsbyIds(List<ObjectId> objectIds) {
        List<HkParcelDocument> parcelDocuments = null;
        List<Criteria> criterias = new ArrayList<>();
        if (objectIds != null) {
            criterias.add(Criteria.where(ParcelFields.PARCEL_ID).in(objectIds));

        }
        parcelDocuments = mongoGenericDao.findByCriteria(criterias, HkParcelDocument.class
        );
        return parcelDocuments;
    }

//----------------------------------------Parcel Service ends ---------------------------------------//
//------------------------------------------ Lot Service Starts -------------------------------------//
    @Override
    public HkLotDocument retrieveLotById(ObjectId lotId) {
        HkLotDocument document = null;

        if (lotId != null) {
            document = (HkLotDocument) mongoGenericDao.retrieveById(lotId, HkLotDocument.class
            );
            String pointerComponentType = HkSystemConstantUtil.CustomField.ComponentType.POINTER + "@*" + HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN;
            Map<String, String> uiFieldMap = null;
            Map<String, Object> customMap = document.getFieldValue().toMap();
            uiFieldMap = this.createFieldNameWithComponentType(customMap);

            if (!CollectionUtils.isEmpty(uiFieldMap)) {
                for (Map.Entry<String, String> field : uiFieldMap.entrySet()) {
                    if (field.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN) || field.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.USER_MULTISELECT) || field.getValue().equals(pointerComponentType)) {
                        if (customMap != null && !customMap.isEmpty() && customMap.containsKey(field.getKey())) {
                            String value = customMap.get(field.getKey()).toString().replaceAll("\\[", "").replaceAll("\\]", "");
                            customMap.put(field.getKey(), value);
                        }
                    }
                }
                document.getFieldValue().putAll(customMap);
            }
        }
        return document;
    }

    @Override
    public List<HkLotDocument> retrieveAllLot(Long franchise, Boolean isActive, List<String> statusList) {
        List<Criteria> criterias = new ArrayList<>();
        List<HkLotDocument> documents = null;
        criterias.add(Criteria.where(LotFields.FRANCHISE).is(franchise));
        if (!CollectionUtils.isEmpty(statusList)) {
            criterias.add(Criteria.where(LotFields.STATUS).in(statusList));
        }
        if (isActive != null) {
            criterias.add(Criteria.where(LotFields.IS_ARCHIVE).is(isActive));
        }
        HkSystemConfigurationDocument systemConfigurationDocument = this.retrieveSystemConfig(HkSystemConstantUtil.FranchiseConfiguration.CURR_VISIBILITY_STATUS);
        if (systemConfigurationDocument != null) {
            if (systemConfigurationDocument.getKeyValue() != null) {
                if (systemConfigurationDocument.getKeyValue().equalsIgnoreCase(HkSystemConstantUtil.SWITCH_OFF)) {
                    criterias.add(Criteria.where(LotFields.HAVE_VALUE).is(Boolean.FALSE));

                }
            }
        }
        documents = mongoGenericDao.findByCriteria(criterias, HkLotDocument.class
        );
        return documents;
    }

    @Override
    @MongoTransaction
    public
            void deleteLot(ObjectId lotId, Long updatedBy) {
        HkLotDocument lotDocument = (HkLotDocument) mongoGenericDao.retrieveById(lotId, HkLotDocument.class
        );
        lotDocument.setStatus(HkSystemConstantUtil.StockStatus.ARCHIVED);

        lotDocument.setLastModifiedBy(updatedBy);

        lotDocument.setLastModifiedOn(
                new Date());
        lotDocument.setIsArchive(
                true);
        ObjectId parcel = lotDocument.getParcel();
        HkParcelDocument hkParcelDocument = this.retrieveParcelById(parcel);

        hkParcelDocument.setStockCarat(hkParcelDocument.getStockCarat() + lotDocument.getStockCarat());
        hkParcelDocument.setStockPieces(hkParcelDocument.getStockPieces() + lotDocument.getStockPieces());
        mongoGenericDao.update(hkParcelDocument);

        mongoGenericDao.update(lotDocument);

        associateLotWithSubLots(lotDocument.getSubLots(), null);

        this.convertRoughStockDetail(lotDocument.getStockPieces(), null, lotDocument.getStockCarat(), hkParcelDocument, HkSystemConstantUtil.RoughStockActions.CREDIT, HkSystemConstantUtil.RoughStockOperations.LOT, updatedBy, null, lotDocument.getId());
    }

    @Override
    public List<HkLotDocument> retrieveLots(Map<String, Object> fieldValues, List<ObjectId> invoiceIds, List<ObjectId> parcelIds, List<ObjectId> lotIds, Long franchise, Boolean isActive) {
        List<HkLotDocument> lotDocuments = null;
        Map<String, String> uiFieldMap = null;
        List<Criteria> criterias = new ArrayList<>();
        Map<String, Object> dateCriteriaMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(fieldValues)) {
            uiFieldMap = this.createFieldNameWithComponentType(fieldValues);
            for (Map.Entry<String, Object> entry : fieldValues.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();

                if (uiFieldMap.containsKey(key)) {
                    String cType = uiFieldMap.get(key);
                    // Specific logic for Date type component
                    if (cType.equals(HkSystemConstantUtil.CustomField.ComponentType.DATE)) {
                        if (value != null) {
                            SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS\'Z\'");
                            formatDate.setTimeZone(TimeZone.getTimeZone("UTC"));
                            Date parseDate = null;
                            Date parseIncDate = null;
                            try {
                                // parseDate gives the formatted date
                                parseDate = formatDate.parse(value.toString());
                                Calendar c = Calendar.getInstance();
                                c.setTime(parseDate);
                                c.add(Calendar.DATE, 1);
                                // incremented date contains next date i.e the current date +1
//                                String incrementedDate = formatDate.format(c.getTime()); // new date
                                parseIncDate = new Date(c.getTimeInMillis());

                            } catch (ParseException ex) {
                                Logger.getLogger(HkStockServiceImpl.class
                                        .getName()).log(Level.SEVERE, null, ex);
                            }
                            if (!(key.startsWith("from") || key.startsWith("to"))) {
                                criterias.add(Criteria.where(LotFields.FIELD_VALUE + "." + key).gt(parseDate).lt(parseIncDate));
                            } else {
                                if (key.startsWith("from")) {
                                    dateCriteriaMap.put(key, parseDate);
                                } else {
                                    dateCriteriaMap.put(key, parseIncDate);
                                }
                            }
                        }
                    } // Code added for handling image and file but since we are not showing image and file components in search it is of no use as for now
                    else if (cType.equals(HkSystemConstantUtil.CustomField.ComponentType.IMAGE) || cType.equals(HkSystemConstantUtil.CustomField.ComponentType.UPLOAD)) {
                        criterias.add(Criteria.where(LotFields.FIELD_VALUE + "." + key).regex(value.toString()));
                    } else {
                        if (value instanceof Collection) {
                            Collection coll = (Collection) value;
                            if (!CollectionUtils.isEmpty(coll)) {
                                criterias.add(Criteria.where(LotFields.FIELD_VALUE + "." + key).in(coll));
                            }
                        } else if (value instanceof Date) {
                            long date = Long.parseLong(value.toString());
                            criterias.add(Criteria.where(LotFields.FIELD_VALUE + "." + key).is(new Date(date)));
                        } else {
                            if (value != null && value.toString().length() > 0) {
                                criterias.add(Criteria.where(LotFields.FIELD_VALUE + "." + key).is(value));
                            }
                        }
                    }
                }

            }
        }
        if (!CollectionUtils.isEmpty(invoiceIds)) {
            criterias.add(Criteria.where(LotFields.INVOICE_ID).in(invoiceIds));
        }
        if (!CollectionUtils.isEmpty(parcelIds)) {
            criterias.add(Criteria.where(LotFields.PARCEL_ID).in(parcelIds));
        }
        if (!CollectionUtils.isEmpty(lotIds)) {
            criterias.add(Criteria.where(LotFields.LOT_Id).in(lotIds));
        }
        if (isActive != null) {
            criterias.add(Criteria.where(LotFields.IS_ARCHIVE).is(isActive));
        }
        if (franchise != null) {
            criterias.add(Criteria.where(LotFields.FRANCHISE).is(franchise));
        }
        HkSystemConfigurationDocument systemConfigurationDocument = this.retrieveSystemConfig(HkSystemConstantUtil.FranchiseConfiguration.CURR_VISIBILITY_STATUS);
        if (systemConfigurationDocument != null) {
            if (systemConfigurationDocument.getKeyValue() != null) {
                if (systemConfigurationDocument.getKeyValue().equalsIgnoreCase(HkSystemConstantUtil.SWITCH_OFF)) {
                    criterias.add(Criteria.where(LotFields.HAVE_VALUE).is(Boolean.FALSE));
                }
            }
        }
        if (!CollectionUtils.isEmpty(dateCriteriaMap)) {
            Set<String> removedFields = new HashSet<>();
            Iterator<Map.Entry<String, Object>> itr = dateCriteriaMap.entrySet().iterator();
            while (itr.hasNext()) {
                Map.Entry<String, Object> entryMap = itr.next();
                String key = entryMap.getKey();
                Date value = (Date) entryMap.getValue();

                if (!removedFields.contains(key)) {
                    if (key.startsWith("from")) {
                        if (dateCriteriaMap.containsKey("to" + key.substring(4))) {
                            Date toValue = (Date) dateCriteriaMap.get("to" + key.substring(4));
                            criterias.add(Criteria.where(LotFields.FIELD_VALUE + "." + key.substring(4)).gte(value).lt(toValue));
                            removedFields.add("to" + key.substring(4));
                            removedFields.add(key);
                        } else {
                            criterias.add(Criteria.where(LotFields.FIELD_VALUE + "." + key.substring(4)).gte(value));
                            removedFields.add(key);
                        }
                    } else {
                        if (dateCriteriaMap.containsKey("from" + key.substring(2))) {
                            Date toValue = (Date) dateCriteriaMap.get("from" + key.substring(2));
                            criterias.add(Criteria.where(LotFields.FIELD_VALUE + "." + key.substring(2)).gte(toValue).lt(value));
                            removedFields.add("from" + key.substring(2));
                            removedFields.add(key);
                        } else {
                            criterias.add(Criteria.where(LotFields.FIELD_VALUE + "." + key.substring(2)).lt(value));
                            removedFields.add(key);

                        }
                    }
                }
            }
        }
        lotDocuments = mongoGenericDao.findByCriteria(criterias, HkLotDocument.class
        );
        List<HkLotDocument> finalList = null;

        if (!CollectionUtils.isEmpty(lotDocuments)) {
            finalList = new ArrayList<>();
            String pointerComponentType = HkSystemConstantUtil.CustomField.ComponentType.POINTER + "@*" + HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN;

            for (HkLotDocument lotDocument : lotDocuments) {
                HkLotDocument temp = lotDocument;
                Map<String, Object> customMap = lotDocument.getFieldValue().toMap();
                uiFieldMap = this.createFieldNameWithComponentType(customMap);
                if (!CollectionUtils.isEmpty(uiFieldMap)) {
                    for (Map.Entry<String, String> field : uiFieldMap.entrySet()) {
                        if (field.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN) || field.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.USER_MULTISELECT) || field.getValue().equals(pointerComponentType)) {
                            if (customMap != null && !customMap.isEmpty() && customMap.containsKey(field.getKey())) {
                                String value = customMap.get(field.getKey()).toString().replaceAll("\\[", "").replaceAll("\\]", "");
                                customMap.put(field.getKey(), value);
                            }
                        }
                    }
                    temp.getFieldValue().putAll(customMap);
                    finalList.add(temp);
                }
            }

        }
        return finalList;
    }

    @Override
    @MongoTransaction
    public String updateLot(ObjectId id, Boolean hasPacket, Map<String, Object> lotCustomMap, Map<String, String> lotDbTypeMap, Map<String, HkFieldDocument> mapForFeatureInvolvedInFormula, Map<String, String> uiFieldMap, List<HkFieldDocument> totalFormulaList, Map<Long, String> featureIdWithNameMap, Long franchise, Long updatedBy, Long featureId, Long sequenceNumber, List<ObjectId> subLots) {
        if (id != null) {
            HkLotDocument lotDocument = (HkLotDocument) mongoGenericDao.retrieveById(id, HkLotDocument.class
            );
            Long pieces = null;
            Double carat = null;
            BasicBSONObject fieldValues = customFieldService.makeBSONObject(lotCustomMap, lotDbTypeMap, uiFieldMap, HkSystemConstantUtil.Feature.LOT, franchise, null, Boolean.TRUE, lotDocument.getFieldValue().toMap(), null);
            List<HkLotDocument> lotDocumentsForFormulaFirstScenario = null;
            if (lotDocument
                    != null) {
                lotDocumentsForFormulaFirstScenario = new ArrayList<>();
                lotDocument.setFieldValue(fieldValues);
//                lotDocument.getFieldValue().putAll(fieldValues.touMap());
                lotDocument.setLastModifiedBy(updatedBy);
                lotDocument.setLastModifiedOn(new Date());
                lotDocument.setSequenceNumber(sequenceNumber);
                lotDocument.setSubLots(subLots);
                if (hasPacket != null) {
                    lotDocument.setHasPacket(hasPacket);
                }
                if (fieldValues.containsField(HkSystemConstantUtil.LotStaticFieldName.CARAT) && fieldValues.get(HkSystemConstantUtil.LotStaticFieldName.CARAT) != null) {
                    carat = (Double) fieldValues.get(HkSystemConstantUtil.LotStaticFieldName.CARAT);
                }
                if (fieldValues.containsField(HkSystemConstantUtil.LotStaticFieldName.PIECES) && fieldValues.get(HkSystemConstantUtil.LotStaticFieldName.PIECES) != null) {
                    pieces = (Long) fieldValues.get(HkSystemConstantUtil.LotStaticFieldName.PIECES);
                }
                HkParcelDocument hkParcelDocument = null;
                HkRoughStockDetailDocument roughtStockDetailDocLot = null;
                if (carat != null || pieces != null) {
                    ObjectId parcelId = lotDocument.getParcel();
                    hkParcelDocument = this.retrieveParcelById(parcelId);
                    roughtStockDetailDocLot = retrieveRoughStockDetailByLot(lotDocument.getId());

                    if (hkParcelDocument != null) {
                        if (hkParcelDocument.getStockCarat() != null && carat != null && roughtStockDetailDocLot.getCarat() != null) {
                            Double stockCarat = hkParcelDocument.getStockCarat();
                            Double newCaratValue = stockCarat - carat + roughtStockDetailDocLot.getCarat();
                            hkParcelDocument.setStockCarat(newCaratValue);
                        }
                        if (hkParcelDocument.getStockPieces() != null && pieces != null && roughtStockDetailDocLot.getPieces() != null) {
                            Long stockPieces = hkParcelDocument.getStockPieces();
                            Long newPiecesValue = stockPieces - pieces + roughtStockDetailDocLot.getPieces();
                            hkParcelDocument.setStockPieces(newPiecesValue);
                        }
                        mongoGenericDao.update(hkParcelDocument);
                    }
                }
                // Code For First Scenario formula update Lot
//                this.evaluateFirstScenarioForUpdateLot(dbFieldWithFormulaMap, franchise, lotDocumentsForFormulaFirstScenario, featureId);
                Map<String, Double> finalFormulaEvalMap = hkFormulaExecution.mapOfEvaluatedFormulaValueWithDbField(totalFormulaList, franchise, null, hkParcelDocument, lotDocument, null, null, lotDocument.getInvoice(), lotDocument.getParcel(), lotDocument.getId(), featureId, HkSystemConstantUtil.FeatureForFormulaEvaluation.EDIT_LOT);
                if (finalFormulaEvalMap != null && !finalFormulaEvalMap.isEmpty()) {
                    for (Map.Entry<String, Double> entrySet : finalFormulaEvalMap.entrySet()) {
                        lotDocument.getFieldValue().put(entrySet.getKey(), entrySet.getValue());
                    }
                }

// Code for first scenario ends
                mongoGenericDao.update(lotDocument);
                if (roughtStockDetailDocLot != null) {
                    convertRoughStockDetail(pieces, null, carat, hkParcelDocument, HkSystemConstantUtil.RoughStockActions.DEBIT, HkSystemConstantUtil.RoughStockOperations.LOT, updatedBy, roughtStockDetailDocLot, id);
                }
                associateLotWithSubLots(subLots, id);
                lotDocumentsForFormulaFirstScenario.add(lotDocument);

                // Call Second Scenario Formula
                HkStockServiceImpl hkStockServiceImpl = applicationContext.getBean(this.getClass());
                hkStockServiceImpl.evaluateSecondScenarioForUpdateLot(franchise, mapForFeatureInvolvedInFormula, featureIdWithNameMap, lotDocumentsForFormulaFirstScenario);
                // Code for second scenario ends here
                Object get = lotDocument.getFieldValue().get(HkSystemConstantUtil.AutoNumber.LOT_ID);
                return get.toString();
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    @Async
    @MongoTransaction
    public void evaluateSecondScenarioForUpdateLot(Long franchise, Map<String, HkFieldDocument> mapForFeatureInvolvedInFormula, Map<Long, String> featureIdWithNameMap, List<HkLotDocument> lotDocuments) {
        // Code For Second scenario
        if (!CollectionUtils.isEmpty(lotDocuments)) {
            //---------------------Required Calls---------------------------------------------
            Map<ObjectId, List<HkLotDocument>> parcelIdAndLotDocumentMap = this.retrieveParcelIdAndLotDocumentMap(franchise, Boolean.FALSE);
            Map<ObjectId, List<HkPacketDocument>> parcelIdAndPacketDocumentMap = this.retrieveParcelIdAndPacketDocumentMap(franchise, Boolean.FALSE);
            Map<ObjectId, List<HkPacketDocument>> lotIdAndPacketDocumentMap = this.retrieveLotIdAndPacketDocumentMap(franchise, Boolean.FALSE);
            Map<ObjectId, List<HkParcelDocument>> invoiceIdAndParcelDocumentMap = this.retrieveInvoiceIdAndParcelDocumentMap(franchise, Boolean.FALSE);
            Map<ObjectId, List<HkLotDocument>> invoiceIdAndLotDocumentMap = this.retrieveInvoiceIdAndLotDocumentMap(franchise, Boolean.FALSE);
            Map<ObjectId, List<HkPacketDocument>> invoiceIdAndPacketDocumentMap = this.retrieveInvoiceIdAndPacketDocumentMap(franchise, Boolean.FALSE);
            Map<ObjectId, HkInvoiceDocument> invoiceIdAndDocumentMap = this.retrieveInvoiceIdAndDocumentMap(franchise, Boolean.FALSE);
            Map<ObjectId, HkParcelDocument> parcelIdAndDocumentMap = this.retrieveParcelIdAndDocumentMap(franchise, Boolean.FALSE);
            Map<ObjectId, HkLotDocument> lotIdAndDocumentMap = this.retrieveLotIdAndDocumentMap(franchise, Boolean.FALSE);
            for (HkLotDocument lotDocument : lotDocuments) {

                List<ObjectId> objectLotIds = new ArrayList<>();
                objectLotIds.add(lotDocument.getId());
                List<HkPacketDocument> packetDocForLot = null;

                HkInvoiceDocument invoiceDocument = null;
                if (invoiceIdAndDocumentMap.containsKey(lotDocument.getInvoice())) {
                    invoiceDocument = invoiceIdAndDocumentMap.get(lotDocument.getInvoice());
                }
                HkParcelDocument parcelDocument = null;
                if (parcelIdAndDocumentMap.containsKey(lotDocument.getParcel())) {
                    parcelDocument = parcelIdAndDocumentMap.get(lotDocument.getParcel());
                }

                ScriptEngineManager manager = new ScriptEngineManager();
                ScriptEngine engine = manager.getEngineByName("js");
                // Code added By Shifa on 18 feb 2015for formula Evaluation For Second scenario i.e. on lot update ,update all other douments where lot formula is involved
                //Map<String, HkFieldEntity> mapForFeatureInvolvedInFormula = fieldService.retrieveMapOfDbFieldNameForFeatureInvolvedInFormula(franchise, HkSystemConstantUtil.Feature.PARCEL, HkSystemConstantUtil.CustomField.ComponentType.FORMULA);
                //MM:22-03-2015 Move code to trasformer layer

                if (mapForFeatureInvolvedInFormula != null && !mapForFeatureInvolvedInFormula.isEmpty()) {
                    for (Map.Entry<String, HkFieldDocument> entrySet : mapForFeatureInvolvedInFormula.entrySet()) {
                        String featureName = featureIdWithNameMap.get(entrySet.getValue().getFeature());

                        // Code If the dbField is Invoice
                        if (featureName.equalsIgnoreCase(HkSystemConstantUtil.Feature.INVOICE)) {
                            String[] formulaValue = entrySet.getValue().getFormulaValue().split("\\|");
                            List<String> modifiedFormula = new ArrayList<>();
                            for (int l = 0; l < formulaValue.length; l++) {
                                if (formulaValue[l].contains(HkSystemConstantUtil.Feature.INVOICE)) {
                                    String formulaSubPart[] = formulaValue[l].split("\\.");
                                    Map invoicelDoc = invoiceIdAndDocumentMap.get(lotDocument.getInvoice()).getFieldValue().toMap();
                                    if (invoicelDoc.containsKey(formulaSubPart[1])) {
                                        String invoiceVal = invoicelDoc.get(formulaSubPart[1]).toString();
                                        if (invoiceVal != null) {
                                            formulaValue[l] = invoiceVal;
                                        }
                                    }
                                }
                                if (formulaValue[l].contains(HkSystemConstantUtil.Feature.PARCEL)) {
                                    String formulaSubPart[] = formulaValue[l].split("\\.");
                                    if (l >= 2 && (formulaValue[l - 2].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.SUM)
                                            || formulaValue[l - 2].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MIN)
                                            || formulaValue[l - 2].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MAX)
                                            || formulaValue[l - 2].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.AVG)
                                            || formulaValue[l - 2].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.COUNT))) {
                                        if (invoiceIdAndParcelDocumentMap != null && !invoiceIdAndParcelDocumentMap.isEmpty()) {
                                            if (invoiceIdAndParcelDocumentMap.containsKey(lotDocument.getInvoice())) {
                                                List<HkParcelDocument> parcelDocsForInvoice = invoiceIdAndParcelDocumentMap.get(lotDocument.getInvoice());
                                                Double aggregrateFunctionResult = this.calculateParcelAggegateFunction(parcelDocsForInvoice, formulaSubPart[1], formulaValue[l - 2]);
                                                if (aggregrateFunctionResult != null) {
                                                    formulaValue[l] = aggregrateFunctionResult.toString();
                                                }
                                            }

                                        }

                                    }
                                }
                                if (formulaValue[l].contains(HkSystemConstantUtil.Feature.LOT)) {
                                    String formulaSubPart[] = formulaValue[l].split("\\.");
                                    if (l >= 2 && (formulaValue[l - 2].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.SUM)
                                            || formulaValue[l - 2].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MIN)
                                            || formulaValue[l - 2].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MAX)
                                            || formulaValue[l - 2].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.AVG)
                                            || formulaValue[l - 2].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.COUNT))) {
                                        if (invoiceIdAndLotDocumentMap != null && !invoiceIdAndLotDocumentMap.isEmpty()) {
                                            if (invoiceIdAndLotDocumentMap.containsKey(lotDocument.getInvoice())) {
                                                List<HkLotDocument> lotDocsForInvoice = invoiceIdAndLotDocumentMap.get(lotDocument.getInvoice());
                                                Double aggregrateFunctionResult = this.calculateLotAggegateFunction(lotDocsForInvoice, formulaSubPart[1], formulaValue[l - 2]);
                                                if (aggregrateFunctionResult != null) {
                                                    formulaValue[l] = aggregrateFunctionResult.toString();
                                                }
                                            }

                                        }

                                    }

                                }
                                if (formulaValue[l].contains(HkSystemConstantUtil.Feature.PACKET)) {
                                    String formulaSubPart[] = formulaValue[l].split("\\.");
                                    if (l >= 2 && (formulaValue[l - 2].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.SUM)
                                            || formulaValue[l - 2].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MIN)
                                            || formulaValue[l - 2].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MAX)
                                            || formulaValue[l - 2].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.AVG)
                                            || formulaValue[l - 2].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.COUNT))) {
                                        if (invoiceIdAndPacketDocumentMap != null && !invoiceIdAndPacketDocumentMap.isEmpty()) {
                                            if (invoiceIdAndPacketDocumentMap.containsKey(lotDocument.getInvoice())) {
                                                List<HkPacketDocument> packetDocsForInvoice = invoiceIdAndPacketDocumentMap.get(lotDocument.getInvoice());
                                                Double aggregrateFunctionResult = this.calculatePacketAggegateFunction(packetDocsForInvoice, formulaSubPart[1], formulaValue[l - 2]);
                                                if (aggregrateFunctionResult != null) {
                                                    formulaValue[l] = aggregrateFunctionResult.toString();
                                                }
                                            }

                                        }

                                    }
                                }

                            }
                            //
                            for (String oldFormula : formulaValue) {
                                if (!(oldFormula.equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.SUM)) && !(oldFormula.equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MIN))
                                        && !(oldFormula.equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MAX)) && !(oldFormula.equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.AVG))
                                        && !(oldFormula.equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.COUNT)) && !(oldFormula.equalsIgnoreCase("(")) && !(oldFormula.equalsIgnoreCase(")"))) {
                                    modifiedFormula.add(oldFormula);
                                }
                            }
                            String[] newForm = new String[modifiedFormula.size()];
                            newForm = modifiedFormula.toArray(newForm);

                            String finalModFormula = Arrays.toString(newForm).replaceAll("|", "").replaceAll(",", "");
                            String finalModFormulaString = finalModFormula.replace("[", "").replace("]", "");
                            try {
                                Object form_eval = engine.eval(finalModFormulaString);
                                invoiceDocument.getFieldValue().put(entrySet.getKey(), Double.parseDouble(form_eval.toString()));
                                mongoGenericDao.update(invoiceDocument);
                            } catch (ScriptException | NumberFormatException e) {
                                continue;
                            }

                        }
                        // Code for handling invoice feature ends here
// Code If the dbField is Parcel
                        if (featureName.equalsIgnoreCase(HkSystemConstantUtil.Feature.PARCEL)) {
                            String[] formulaValue = entrySet.getValue().getFormulaValue().split("\\|");
                            List<String> modifiedFormula = new ArrayList<>();
                            for (int l = 0; l < formulaValue.length; l++) {
                                if (formulaValue[l].contains(HkSystemConstantUtil.Feature.INVOICE)) {
                                    String formulaSubPart[] = formulaValue[l].split("\\.");
                                    Map invoicelDoc = invoiceIdAndDocumentMap.get(lotDocument.getInvoice()).getFieldValue().toMap();
                                    if (invoicelDoc.containsKey(formulaSubPart[1])) {
                                        String parcelVal = invoicelDoc.get(formulaSubPart[1]).toString();
                                        if (parcelVal != null) {
                                            formulaValue[l] = parcelVal;
                                        }
                                    }
                                }
                                if (formulaValue[l].contains(HkSystemConstantUtil.Feature.PARCEL)) {
                                    String formulaSubPart[] = formulaValue[l].split("\\.");
                                    Map parcelDoc = parcelIdAndDocumentMap.get(lotDocument.getParcel()).getFieldValue().toMap();
                                    if (parcelDoc.containsKey(formulaSubPart[1])) {
                                        String parcelVal = parcelDoc.get(formulaSubPart[1]).toString();
                                        if (parcelVal != null) {
                                            formulaValue[l] = parcelVal;
                                        }
                                    }
                                }
                                if (formulaValue[l].contains(HkSystemConstantUtil.Feature.LOT)) {
                                    String formulaSubPart[] = formulaValue[l].split("\\.");
                                    if (l >= 2 && (formulaValue[l - 2].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.SUM)
                                            || formulaValue[l - 2].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MIN)
                                            || formulaValue[l - 2].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MAX)
                                            || formulaValue[l - 2].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.AVG)
                                            || formulaValue[l - 2].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.COUNT))) {
                                        if (parcelIdAndLotDocumentMap != null && !parcelIdAndDocumentMap.isEmpty()) {
                                            if (parcelIdAndLotDocumentMap.containsKey(lotDocument.getParcel())) {
                                                List<HkLotDocument> lotDocsForParcelDoc = parcelIdAndLotDocumentMap.get(lotDocument.getParcel());
                                                Double aggregrateFunctionResult = this.calculateLotAggegateFunction(lotDocsForParcelDoc, formulaSubPart[1], formulaValue[l - 2]);
                                                if (aggregrateFunctionResult != null) {
                                                    formulaValue[l] = aggregrateFunctionResult.toString();
                                                }
                                            }

                                        }
                                    }

                                }
                                if (formulaValue[l].contains(HkSystemConstantUtil.Feature.PACKET)) {
                                    String formulaSubPart[] = formulaValue[l].split("\\.");
                                    if (l >= 2 && (formulaValue[l - 2].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.SUM)
                                            || formulaValue[l - 2].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MIN)
                                            || formulaValue[l - 2].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MAX)
                                            || formulaValue[l - 2].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.AVG)
                                            || formulaValue[l - 2].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.COUNT))) {
                                        if (parcelIdAndPacketDocumentMap != null && !parcelIdAndPacketDocumentMap.isEmpty()) {
                                            if (parcelIdAndPacketDocumentMap.containsKey(lotDocument.getParcel())) {
                                                List<HkPacketDocument> packetDocsForParcelDoc = parcelIdAndPacketDocumentMap.get(lotDocument.getParcel());
                                                Double aggregrateFunctionResult = this.calculatePacketAggegateFunction(packetDocsForParcelDoc, formulaSubPart[1], formulaValue[l - 2]);
                                                if (aggregrateFunctionResult != null) {
                                                    formulaValue[l] = aggregrateFunctionResult.toString();
                                                }
                                            }
                                        }

                                    }
                                }

                            }
                            //
                            for (String oldFormula : formulaValue) {
                                if (!(oldFormula.equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.SUM)) && !(oldFormula.equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MIN))
                                        && !(oldFormula.equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MAX)) && !(oldFormula.equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.AVG))
                                        && !(oldFormula.equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.COUNT)) && !(oldFormula.equalsIgnoreCase("(")) && !(oldFormula.equalsIgnoreCase(")"))) {
                                    modifiedFormula.add(oldFormula);
                                }
                            }
                            String[] newForm = new String[modifiedFormula.size()];
                            newForm = modifiedFormula.toArray(newForm);

                            String finalModFormula = Arrays.toString(newForm).replaceAll("|", "").replaceAll(",", "");
                            String finalModFormulaString = finalModFormula.replace("[", "").replace("]", "");
                            try {
                                Object form_eval = engine.eval(finalModFormulaString);
                                parcelDocument.getFieldValue().put(entrySet.getKey(), Double.parseDouble(form_eval.toString()));
                                mongoGenericDao.update(parcelDocument);
                            } catch (ScriptException | NumberFormatException e) {
                                continue;
                            }

                        }
                        // Code for handling parcel feature ends here

                        // Code if the feature is Packet starts here
                        if (featureName.equalsIgnoreCase(HkSystemConstantUtil.Feature.PACKET)) {
                            if (!CollectionUtils.isEmpty(packetDocForLot)) {
                                for (HkPacketDocument packetDoc : packetDocForLot) {
                                    String[] formulaValue = entrySet.getValue().getFormulaValue().split("\\|");
                                    List<String> modifiedFormula = new ArrayList<>();
                                    for (int l = 0; l < formulaValue.length; l++) {
                                        if (formulaValue[l].contains(HkSystemConstantUtil.Feature.INVOICE)) {
                                            String formulaSubPart[] = formulaValue[l].split("\\.");
                                            Map invoiceDoc = invoiceIdAndDocumentMap.get(packetDoc.getInvoice()).getFieldValue().toMap();
                                            if (invoiceDoc.containsKey(formulaSubPart[1])) {
                                                String invoiceVal = invoiceDoc.get(formulaSubPart[1]).toString();
                                                if (invoiceVal != null) {
                                                    formulaValue[l] = invoiceVal;
                                                }
                                            }
                                        }
                                        if (formulaValue[l].contains(HkSystemConstantUtil.Feature.PARCEL)) {
                                            String formulaSubPart[] = formulaValue[l].split("\\.");
                                            Map parcelDoc = parcelIdAndDocumentMap.get(packetDoc.getParcel()).getFieldValue().toMap();
                                            if (parcelDoc.containsKey(formulaSubPart[1])) {
                                                String parcelVal = parcelDoc.get(formulaSubPart[1]).toString();
                                                if (parcelVal != null) {
                                                    formulaValue[l] = parcelVal;
                                                }
                                            }
                                        }
                                        if (formulaValue[l].contains(HkSystemConstantUtil.Feature.LOT)) {
                                            String formulaSubPart[] = formulaValue[l].split("\\.");
                                            Map lotDoc = lotIdAndDocumentMap.get(packetDoc.getLot()).getFieldValue().toMap();
                                            if (lotDoc.containsKey(formulaSubPart[1])) {
                                                String lotVal = lotDoc.get(formulaSubPart[1]).toString();
                                                if (lotVal != null) {
                                                    formulaValue[l] = lotVal;
                                                }
                                            }

                                        }
                                        if (formulaValue[l].contains(HkSystemConstantUtil.Feature.PACKET)) {
                                            String formulaSubPart[] = formulaValue[l].split("\\.");
                                            String packetVal = packetDoc.getFieldValue().get(formulaSubPart[1]).toString();
                                            if (packetVal != null) {
                                                formulaValue[l] = packetVal;
                                            }
                                        }

                                    }
                                    //
                                    for (String oldFormula : formulaValue) {
                                        if (!(oldFormula.equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.SUM)) && !(oldFormula.equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MIN))
                                                && !(oldFormula.equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MAX)) && !(oldFormula.equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.AVG))
                                                && !(oldFormula.equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.COUNT)) && !(oldFormula.equalsIgnoreCase("(")) && !(oldFormula.equalsIgnoreCase(")"))) {
                                            modifiedFormula.add(oldFormula);
                                        }
                                    }
                                    String[] newForm = new String[modifiedFormula.size()];
                                    newForm = modifiedFormula.toArray(newForm);

                                    String finalModFormula = Arrays.toString(newForm).replaceAll("|", "").replaceAll(",", "");
                                    String finalModFormulaString = finalModFormula.replace("[", "").replace("]", "");
                                    try {
                                        Object form_eval = engine.eval(finalModFormulaString);
                                        packetDoc.getFieldValue().put(entrySet.getKey(), Double.parseDouble(form_eval.toString()));
                                        mongoGenericDao.update(packetDoc);
                                    } catch (ScriptException | NumberFormatException e) {
                                        continue;
                                    }

                                }

                            }

                        }
                        // Code for handling Packet ends here

                    }

                }
            }

        }
// Code for second scenario ends here
    }

    @Override
    @MongoTransaction
    public String mergeLot(ObjectId parentParcel, List<ObjectId> mergedFrom, List<BasicBSONObject> basicBSONObjects, Long franchise, Long createdBy, List<ObjectId> workAllocationIds) {
        if (!CollectionUtils.isEmpty(basicBSONObjects)) {
            String lotNumber = null; //customFieldService.generateNextFieldSequence(HkSystemConstantUtil.AutoNumber.LOT_ID, franchise);
            if (lotNumber != null && mergedFrom != null) {
                HkParcelDocument parcelDocument = null;
                HkLotDocument lotDocument = new HkLotDocument();
                if (parentParcel != null) {
                    lotDocument.setParcel(parentParcel);
                    parcelDocument
                            = (HkParcelDocument) mongoGenericDao.retrieveById(parentParcel, HkParcelDocument.class
                            );
                    if (parcelDocument
                            != null) {
                        lotDocument.setInvoice(parcelDocument.getInvoice());
                        if (parcelDocument.getHaveValue() != null) {
                            lotDocument.setHaveValue(parcelDocument.getHaveValue());
                        } else {
                            lotDocument.setHaveValue(Boolean.FALSE);
                        }
                    } else {
                        return null;
                    }
                }

                lotDocument.setFranchiseId(franchise);
                lotDocument.setIsArchive(Boolean.FALSE);
                lotDocument.setCreatedBy(createdBy);
                lotDocument.setLastModifiedBy(createdBy);
                lotDocument.setCreatedOn(new Date());
                lotDocument.setLastModifiedOn(new Date());
                lotDocument.setHasPacket(Boolean.FALSE);
                lotDocument.setStatus(HkSystemConstantUtil.StockStatus.IN_PRODUCTION);
                List<HkLotDocument> listToBeupdate = new ArrayList<>();
                List<HkLotDocument> lotDocuments = this.retrieveLotsbyIds(mergedFrom);
                if (!CollectionUtils.isEmpty(mergedFrom)) {
                    for (HkLotDocument hkLotDocument : lotDocuments) {
                        if (!hkLotDocument.isHasPacket()) {
                            hkLotDocument.setStatus(HkSystemConstantUtil.StockStatus.MERGED);
                            hkLotDocument.setIsArchive(Boolean.TRUE);
                            hkLotDocument.setLastModifiedBy(createdBy);
                            hkLotDocument.setLastModifiedOn(new Date());
                            listToBeupdate.add(hkLotDocument);
                        } else {
                            mergedFrom.remove(hkLotDocument);
                        }
                    }
                }
                lotDocument.setMergedFrom(mergedFrom);
                listToBeupdate.add(lotDocument);
//                mongoGenericDao.createAll(listToBeupdate);
                for (HkLotDocument hkLotDocument : listToBeupdate) {
                    mongoGenericDao.update(hkLotDocument);
                }
                BasicBSONObject fieldValues = basicBSONObjects.get(0);
                fieldValues.put(HkSystemConstantUtil.AutoNumber.LOT_ID, lotNumber);
                lotDocument.setFieldValue(fieldValues);
                mongoGenericDao.update(lotDocument);
                return lotNumber;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    private List<HkLotDocument> retrieveLotsbyIds(List<ObjectId> objectIds) {
        List<HkLotDocument> lotDocuments = null;
        List<Criteria> criterias = new ArrayList<>();
        if (objectIds != null) {
            criterias.add(Criteria.where(LotFields.LOT_Id).in(objectIds));

        }
        lotDocuments = mongoGenericDao.findByCriteria(criterias, HkLotDocument.class
        );
        return lotDocuments;
    }

    @Override
    @MongoTransaction
    public HkLotDocument createLot(BasicBSONObject basicBSONObject, List<HkFieldDocument> totalformulaList, Long franchise, Long createdByFranchise, Long createdBy, ObjectId invoiceId, ObjectId parcelId, Long featureId, Long sequenceNumber, List<ObjectId> subLots) {
        Long pieces = null;
        Double carat = null;
        List<HkLotDocument> lotDocumentsForFormulaScenario = new ArrayList<>();
        if (basicBSONObject != null) {
            Boolean hasValue = Boolean.FALSE;
            HkInvoiceDocument invoiceDocument = null;

            if (invoiceId != null) {
                invoiceDocument = (HkInvoiceDocument) mongoGenericDao.retrieveById(invoiceId, HkInvoiceDocument.class
                );
                if (invoiceDocument
                        != null) {
                    hasValue = invoiceDocument.getHaveValue();
                }
            }
            HkParcelDocument parcel = null;

            if (parcelId != null) {
                parcel = (HkParcelDocument) mongoGenericDao.retrieveById(parcelId, HkParcelDocument.class
                );
            }
            HkLotDocument lotDocument = new HkLotDocument();
            if (invoiceId != null) {
                lotDocument.setInvoice(invoiceId);
            }
            if (parcelId != null) {
                lotDocument.setParcel(parcelId);
            }
            lotDocument.setFranchiseId(franchise);
            lotDocument.setCreatedByFranchise(createdByFranchise);
            lotDocument.setIsArchive(Boolean.FALSE);
            lotDocument.setCreatedBy(createdBy);
            lotDocument.setLastModifiedBy(createdBy);
            lotDocument.setCreatedOn(new Date());
            lotDocument.setLastModifiedOn(new Date());
            lotDocument.setHasPacket(Boolean.FALSE);
            lotDocument.setYear(Calendar.getInstance().get(Calendar.YEAR));
            lotDocument.setSequenceNumber(sequenceNumber);
            lotDocument.setStatus(HkSystemConstantUtil.StockStatus.PENDING);
            lotDocument.setHaveValue(hasValue);
            if (!basicBSONObject.containsField(HkSystemConstantUtil.LotStaticFieldName.IN_STOCK_OF) || basicBSONObject.get(HkSystemConstantUtil.LotStaticFieldName.IN_STOCK_OF) == null) {
                if (parcel != null) {
                    if (parcel.getFieldValue() != null && parcel.getFieldValue().containsField(HkSystemConstantUtil.ParcelStaticFieldName.IN_STOCK_OF)) {
                        List<String> inStockOf = (ArrayList<String>) parcel.getFieldValue().get(HkSystemConstantUtil.ParcelStaticFieldName.IN_STOCK_OF);
                        basicBSONObject.put(HkSystemConstantUtil.LotStaticFieldName.IN_STOCK_OF, inStockOf);
                    }

                }
            }
            if (basicBSONObject.containsField(HkSystemConstantUtil.LotStaticFieldName.CARAT) && basicBSONObject.get(HkSystemConstantUtil.LotStaticFieldName.CARAT) != null) {
                lotDocument.setStockCarat((Double) basicBSONObject.get(HkSystemConstantUtil.LotStaticFieldName.CARAT));
                carat = (Double) basicBSONObject.get(HkSystemConstantUtil.LotStaticFieldName.CARAT);
            }
            if (basicBSONObject.containsField(HkSystemConstantUtil.LotStaticFieldName.PIECES) && basicBSONObject.get(HkSystemConstantUtil.LotStaticFieldName.PIECES) != null) {
                lotDocument.setStockPieces((Long) basicBSONObject.get(HkSystemConstantUtil.LotStaticFieldName.PIECES));
                pieces = (Long) basicBSONObject.get(HkSystemConstantUtil.LotStaticFieldName.PIECES);
            }
            lotDocument.setFieldValue(basicBSONObject);
            if (!CollectionUtils.isEmpty(subLots)) {
                lotDocument.setSubLots(subLots);
            }
            //--- Call formula first scenario generic method
            Map<String, Double> finalFormulaEvalMap = hkFormulaExecution.mapOfEvaluatedFormulaValueWithDbField(totalformulaList, franchise, invoiceDocument, parcel, lotDocument, null, null, lotDocument.getInvoice(), lotDocument.getParcel(), lotDocument.getId(), featureId, HkSystemConstantUtil.FeatureForFormulaEvaluation.ADD_LOT);
            if (finalFormulaEvalMap != null && !finalFormulaEvalMap.isEmpty()) {
                for (Map.Entry<String, Double> entrySet : finalFormulaEvalMap.entrySet()) {
                    lotDocument.getFieldValue().put(entrySet.getKey(), entrySet.getValue());
                }
            }
//--- formula ends
            mongoGenericDao.create(lotDocument);
            associateLotWithSubLots(lotDocument.getSubLots(), lotDocument.getId());
            if (invoiceDocument != null) {
                invoiceDocument.setHaveLot(Boolean.TRUE);
                mongoGenericDao.update(invoiceDocument);
            }
            if (parcel != null) {
                if (parcel.getStockCarat() != null && carat != null) {
                    Double stockCarat = parcel.getStockCarat();
                    Double newCaratValue = stockCarat - carat;
                    parcel.setStockCarat(newCaratValue);
                }
                if (parcel.getStockPieces() != null && pieces != null) {
                    Long stockPieces = parcel.getStockPieces();
                    Long newPiecesValue = stockPieces - pieces;
                    parcel.setStockPieces(newPiecesValue);
                }
                mongoGenericDao.update(parcel);
            }
            this.convertRoughStockDetail(pieces, null, carat, parcel, HkSystemConstantUtil.RoughStockActions.DEBIT, HkSystemConstantUtil.RoughStockOperations.LOT, createdBy, null, lotDocument.getId());
            lotDocumentsForFormulaScenario.add(lotDocument);

            return lotDocument;
        }

        return null;

    }

    private void associateLotWithSubLots(List<ObjectId> subLots, ObjectId lotId) {
        if (!CollectionUtils.isEmpty(subLots)) {
            for (ObjectId subLot : subLots) {
                HkSubLotDocument subLotDoc = (HkSubLotDocument) mongoGenericDao.retrieveById(subLot, HkSubLotDocument.class
                );
                subLotDoc.setAssociatedLot(lotId);

                mongoGenericDao.update(subLotDoc);
            }
        }
    }

    @Override
    @MongoTransaction
    public List<String> splitLot(ObjectId parentParcel, ObjectId splitFrom, List<BasicBSONObject> basicBSONObjects, Long franchise, Long createdBy, ObjectId workAllocationId
    ) {
        List<String> lotNumberList = null;
        ObjectId invoiceId = null;
        List<ObjectId> splittedLots = null;
        if (!CollectionUtils.isEmpty(basicBSONObjects) && parentParcel != null && splitFrom != null) {
            splittedLots = new ArrayList<>();
            HkParcelDocument parcelDocument = (HkParcelDocument) mongoGenericDao.retrieveById(parentParcel, HkParcelDocument.class
            );
            HkLotDocument oldLot = (HkLotDocument) mongoGenericDao.retrieveById(splitFrom, HkLotDocument.class);
            if (parcelDocument
                    != null) {
                lotNumberList = new ArrayList<>();
                invoiceId = parcelDocument.getInvoice();
                for (BasicBSONObject basicBSONObject : basicBSONObjects) {
                    String lotNumber = null; //customFieldService.generateNextFieldSequence(HkSystemConstantUtil.AutoNumber.LOT_ID, franchise);
                    if (lotNumber != null) {
                        HkLotDocument lotDocument = new HkLotDocument();
                        lotDocument.setParcel(parentParcel);
                        if (invoiceId != null) {
                            lotDocument.setInvoice(invoiceId);
                        }
                        lotDocument.setFranchiseId(franchise);
                        lotDocument.setIsArchive(Boolean.FALSE);
                        lotDocument.setCreatedBy(createdBy);
                        lotDocument.setLastModifiedBy(createdBy);
                        lotDocument.setCreatedOn(new Date());
                        lotDocument.setLastModifiedOn(new Date());
                        lotDocument.setHasPacket(Boolean.FALSE);
                        lotDocument.setSplitFrom(splitFrom);
                        lotDocument.setStatus(HkSystemConstantUtil.StockStatus.IN_PRODUCTION);
                        basicBSONObject.put(HkSystemConstantUtil.AutoNumber.LOT_ID, lotNumber);
                        if (parcelDocument != null && parcelDocument.getHaveValue() != null) {
                            lotDocument.setHaveValue(parcelDocument.getHaveValue());
                        }
                        if (!basicBSONObject.containsField(HkSystemConstantUtil.LotStaticFieldName.IN_STOCK_OF) || basicBSONObject.get(HkSystemConstantUtil.LotStaticFieldName.IN_STOCK_OF) == null) {
                            if (oldLot != null) {
                                if (oldLot.getFieldValue() != null && oldLot.getFieldValue().containsField(HkSystemConstantUtil.LotStaticFieldName.IN_STOCK_OF)) {
                                    List<String> inStockOf = (ArrayList<String>) oldLot.getFieldValue().get(HkSystemConstantUtil.LotStaticFieldName.IN_STOCK_OF);
                                    basicBSONObject.put(HkSystemConstantUtil.LotStaticFieldName.IN_STOCK_OF, inStockOf);
                                }

                            }
                        }
                        lotNumberList.add(lotNumber);
                        lotDocument.setFieldValue(basicBSONObject);
                        mongoGenericDao.create(lotDocument);
                        splittedLots.add(lotDocument.getId());
                    }
                }

                if (!CollectionUtils.isEmpty(splittedLots)) {
                    if (oldLot != null) {
                        oldLot.setSplitTo(splittedLots);
                        oldLot.setStatus(HkSystemConstantUtil.StockStatus.SPLITTED);
                        oldLot.setIsArchive(Boolean.TRUE);
                        oldLot.setLastModifiedBy(createdBy);
                        oldLot.setLastModifiedOn(new Date());
                        mongoGenericDao.update(oldLot);
                    }
                }
            }
        } else {
            return null;
        }
        return lotNumberList;
    }

    @Override
    public List<HkLotDocument> retrieveLotsByIds(List<ObjectId> lotIds, Long franchise, Boolean isArchive, List<String> status, Boolean hasPacket, Boolean isConversionRequired) {
        List<HkLotDocument> lotDocuments = null;
        List<HkLotDocument> finalList = null;
        List<Criteria> criterias = new ArrayList<>();
        if (!CollectionUtils.isEmpty(lotIds)) {
            criterias.add(Criteria.where(LotFields.LOT_Id).in(lotIds));
        }
        if (franchise != null) {
            criterias.add(Criteria.where(LotFields.FRANCHISE).is(franchise));
        }
        if (isArchive != null) {
            criterias.add(Criteria.where(LotFields.IS_ARCHIVE).is(isArchive));
        }
        if (!StringUtils.isEmpty(status)) {
            criterias.add(Criteria.where(LotFields.STATUS).in(status));
        }
        if (hasPacket != null) {
            criterias.add(Criteria.where(LotFields.HAS_PACKET).is(hasPacket));

        }
        lotDocuments = mongoGenericDao.findByCriteria(criterias, HkLotDocument.class
        );

        if (isConversionRequired) {
            if (!CollectionUtils.isEmpty(lotDocuments)) {
                finalList = new ArrayList<>();
                for (HkLotDocument lotDocument : lotDocuments) {
                    HkLotDocument temp = lotDocument;
                    Map<String, Object> customMap = lotDocument.getFieldValue().toMap();
                    Map<String, String> uiFieldMap = new HashMap<>();
                    if (!CollectionUtils.isEmpty(customMap)) {
                        for (Map.Entry<String, Object> custom : customMap.entrySet()) {
                            String[] split = custom.getKey().split("\\" + HkSystemConstantUtil.SEPERATOR_FOR_CUSTOM_FIELD_LABEL);
                            if (split != null && split.length >= 1) {
                                uiFieldMap.put(custom.getKey(), split[1]);
                            }
                        }
                    }
                    if (!CollectionUtils.isEmpty(uiFieldMap)) {
                        for (Map.Entry<String, String> field : uiFieldMap.entrySet()) {
                            if (field.getValue().equals(HkSystemConstantUtil.CustomField.COMPONENT_CODE_MAP.get(HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN)) || field.getValue().equals(HkSystemConstantUtil.CustomField.COMPONENT_CODE_MAP.get(HkSystemConstantUtil.CustomField.ComponentType.USER_MULTISELECT))) {
                                if (customMap != null && !customMap.isEmpty() && customMap.containsKey(field.getKey())) {
                                    String value = customMap.get(field.getKey()).toString().replaceAll("\\[", "").replaceAll("\\]", "");
                                    customMap.put(field.getKey(), value);
                                }
                            }
                        }
                        temp.getFieldValue().putAll(customMap);
                        finalList.add(temp);
                    }
                }

            }
            return finalList;
        } else {
            return lotDocuments;
        }

    }

    @Override
    public Long getNextLotSequence(Long franchise) {
        Long sequenceNumber = 0l;
        Query query = new Query();
        query.addCriteria(Criteria.where(LotFields.FRANCHISE).is(franchise));
        query.addCriteria(Criteria.where(LotFields.IS_ARCHIVE).is(Boolean.FALSE));
        query.addCriteria(Criteria.where(LotFields.YEAR).is(Calendar.getInstance().get(Calendar.YEAR)));
        query.with(new Sort(Sort.Direction.DESC, LotFields.SEQUENCE_NUMBER)).limit(1);

        HkLotDocument hkLotDocument = mongoGenericDao.getMongoTemplate().findOne(query, HkLotDocument.class
        );
        if (hkLotDocument
                != null && hkLotDocument.getSequenceNumber()
                != null) {
            sequenceNumber = hkLotDocument.getSequenceNumber();
        }
        return sequenceNumber;
    }

    @Override
    public Boolean isLotSequenceExist(Long sequenceNumber, Long franchise) {
        Boolean lotExist = Boolean.FALSE;
        List<HkLotDocument> lotDocuments = new ArrayList<>();
        List<Criteria> criterias = new ArrayList<>();
        if (franchise != null) {
            criterias.add(Criteria.where(LotFields.FRANCHISE).is(franchise));
        }
        criterias.add(Criteria.where(LotFields.YEAR).is(Calendar.getInstance().get(Calendar.YEAR)));
        if (sequenceNumber != null) {
            criterias.add(Criteria.where(LotFields.SEQUENCE_NUMBER).is(sequenceNumber));

        }
        lotDocuments = mongoGenericDao.findByCriteria(criterias, HkLotDocument.class
        );
        if (!CollectionUtils.isEmpty(lotDocuments)) {
            lotExist = Boolean.TRUE;
        }
        return lotExist;
    }

//------------------------------------------ Lot Service Ends -------------------------------------//
//------------------------------------------ Diamond Service Starts ----------------------------------------//
    @Override
    public Map<ObjectId, HkInvoiceDocument> retrieveInvoiceIdAndDocumentMap(Long franchise, Boolean isArchive
    ) {
        List<Criteria> criterias = new ArrayList<>();
        Map<ObjectId, HkInvoiceDocument> idInvoiceDocument = null;
        List<HkInvoiceDocument> documents = null;
        criterias.add(Criteria.where(PlanFields.FRANCHISE).in(this.getCompnies(franchise)));
        if (isArchive != null) {
            criterias.add(Criteria.where(IS_ARCHIVE).is(isArchive));

        }
        documents = mongoGenericDao.findByCriteria(criterias, HkInvoiceDocument.class
        );
        if (!CollectionUtils.isEmpty(documents)) {
            idInvoiceDocument = new HashMap<>();
            for (HkInvoiceDocument document : documents) {
                idInvoiceDocument.put(document.getId(), document);
            }
        }
        return idInvoiceDocument;
    }

    @Override
    public Map<ObjectId, HkParcelDocument> retrieveParcelIdAndDocumentMap(Long franchise, Boolean isArchive
    ) {
        List<Criteria> criterias = new ArrayList<>();
        Map<ObjectId, HkParcelDocument> idParcelDocument = null;
        List<HkParcelDocument> parceldocuments = null;
        criterias.add(Criteria.where(PlanFields.FRANCHISE).in(this.getCompnies(franchise)));
        if (isArchive != null) {
            criterias.add(Criteria.where(IS_ARCHIVE).is(isArchive));

        }
        parceldocuments = mongoGenericDao.findByCriteria(criterias, HkParcelDocument.class
        );
        if (!CollectionUtils.isEmpty(parceldocuments)) {
            idParcelDocument = new HashMap<>();
            for (HkParcelDocument document : parceldocuments) {
                idParcelDocument.put(document.getId(), document);
            }
        }
        return idParcelDocument;
    }

    @Override
    public Map<ObjectId, HkLotDocument> retrieveLotIdAndDocumentMap(Long franchise, Boolean isArchive
    ) {
        List<Criteria> criterias = new ArrayList<>();
        Map<ObjectId, HkLotDocument> idLotDocument = null;
        List<HkLotDocument> documents = null;
        criterias.add(Criteria.where(PlanFields.FRANCHISE).is(franchise));
        if (isArchive != null) {
            criterias.add(Criteria.where(IS_ARCHIVE).is(isArchive));

        }
        documents = mongoGenericDao.findByCriteria(criterias, HkLotDocument.class
        );
        if (!CollectionUtils.isEmpty(documents)) {
            idLotDocument = new HashMap<>();
            for (HkLotDocument document : documents) {
                idLotDocument.put(document.getId(), document);
            }
        }
        return idLotDocument;
    }

    @Override
    public Map<ObjectId, HkPacketDocument> retrievePacketIdAndDocumentMap(Long franchise, Boolean isArchive
    ) {
        List<Criteria> criterias = new ArrayList<>();
        Map<ObjectId, HkPacketDocument> idPacketDocument = null;
        List<HkPacketDocument> documents = null;
        criterias.add(Criteria.where(PlanFields.FRANCHISE).is(franchise));
        if (isArchive != null) {
            criterias.add(Criteria.where(IS_ARCHIVE).is(isArchive));

        }
        documents = mongoGenericDao.findByCriteria(criterias, HkPacketDocument.class
        );
        if (!CollectionUtils.isEmpty(documents)) {
            idPacketDocument = new HashMap<>();
            for (HkPacketDocument document : documents) {
                idPacketDocument.put(document.getId(), document);
            }
        }
        return idPacketDocument;
    }

    @Override
    public Double calculateParcelAggegateFunction(List<HkParcelDocument> parcelDocument, String propertyName, String OperationType
    ) {
        Double aggregrateResult = null;

        List<Double> valuesToBeEvaluated = null;
        if (!CollectionUtils.isEmpty(parcelDocument)) {
            valuesToBeEvaluated = new ArrayList<>();
            for (HkParcelDocument document : parcelDocument) {
                Map fieldValueMap = document.getFieldValue().toMap();
                if (fieldValueMap.containsKey(propertyName)) {
                    String toString = document.getFieldValue().get(propertyName).toString();
                    valuesToBeEvaluated.add(Double.parseDouble(toString));
                }
            }
            if (!CollectionUtils.isEmpty(valuesToBeEvaluated)) {
                switch (OperationType) {
                    case HkSystemConstantUtil.AggregrateFunctions.SUM:
                        Double sum = new Double(0);
                        for (Double value : valuesToBeEvaluated) {

                            sum = sum + value;
                        }
                        aggregrateResult = sum;
                        break;
                    case HkSystemConstantUtil.AggregrateFunctions.COUNT:
                        Double count = new Double(0);
                        for (Double value : valuesToBeEvaluated) {
                            count++;
                        }
                        aggregrateResult = count;
                        break;
                    case HkSystemConstantUtil.AggregrateFunctions.AVG:
                        Double avg = new Double(0);
                        Double totalNum = new Double(0);
                        Double Sum_totalNum = new Double(0);
                        for (Double value : valuesToBeEvaluated) {
                            totalNum++;
                            Sum_totalNum = Sum_totalNum + value;
                        }
                        Double averageVal = Sum_totalNum / totalNum;
                        aggregrateResult = averageVal;
                        break;
                    case HkSystemConstantUtil.AggregrateFunctions.MIN:
                        aggregrateResult = Collections.min(valuesToBeEvaluated);
                        break;
                    case HkSystemConstantUtil.AggregrateFunctions.MAX:
                        aggregrateResult = Collections.max(valuesToBeEvaluated);
                        break;

                }
            }
        }
        return aggregrateResult;
    }

    @Override
    public Double calculateLotAggegateFunction(List<HkLotDocument> lotDocument, String propertyName, String OperationType
    ) {
        Double aggregrateResult = null;

        List<Double> valuesToBeEvaluated = null;
        if (!CollectionUtils.isEmpty(lotDocument)) {
            valuesToBeEvaluated = new ArrayList<>();
            for (HkLotDocument document : lotDocument) {
                Map fieldValueMap = document.getFieldValue().toMap();
                if (fieldValueMap.containsKey(propertyName)) {
                    String toString = document.getFieldValue().get(propertyName).toString();
                    valuesToBeEvaluated.add(Double.parseDouble(toString));
                }

            }
            if (!CollectionUtils.isEmpty(valuesToBeEvaluated)) {
                switch (OperationType) {
                    case HkSystemConstantUtil.AggregrateFunctions.SUM:
                        Double sum = new Double(0);
                        for (Double value : valuesToBeEvaluated) {

                            sum = sum + value;
                        }
                        aggregrateResult = sum;
                        break;
                    case HkSystemConstantUtil.AggregrateFunctions.COUNT:
                        Double count = new Double(0);
                        for (Double value : valuesToBeEvaluated) {
                            count++;
                        }
                        aggregrateResult = count;
                        break;
                    case HkSystemConstantUtil.AggregrateFunctions.AVG:
                        Double avg = new Double(0);
                        Double totalNum = new Double(0);
                        Double Sum_totalNum = new Double(0);
                        for (Double value : valuesToBeEvaluated) {
                            totalNum++;
                            Sum_totalNum = Sum_totalNum + value;
                        }
                        Double averageVal = Sum_totalNum / totalNum;
                        aggregrateResult = averageVal;
                        break;
                    case HkSystemConstantUtil.AggregrateFunctions.MIN:
                        aggregrateResult = Collections.min(valuesToBeEvaluated);
                        break;
                    case HkSystemConstantUtil.AggregrateFunctions.MAX:
                        aggregrateResult = Collections.max(valuesToBeEvaluated);
                        break;

                }
            }
        }
        return aggregrateResult;
    }

    @Override
    public Double calculatePacketAggegateFunction(List<HkPacketDocument> packetDocument, String propertyName, String OperationType
    ) {
        Double aggregrateResult = null;

        List<Double> valuesToBeEvaluated = null;
        if (!CollectionUtils.isEmpty(packetDocument)) {
            valuesToBeEvaluated = new ArrayList<>();
            for (HkPacketDocument document : packetDocument) {
                Map fieldValueMap = document.getFieldValue().toMap();
                if (fieldValueMap.containsKey(propertyName)) {
                    String toString = document.getFieldValue().get(propertyName).toString();
                    valuesToBeEvaluated.add(Double.parseDouble(toString));
                }
            }
            if (!CollectionUtils.isEmpty(valuesToBeEvaluated)) {
                switch (OperationType) {
                    case HkSystemConstantUtil.AggregrateFunctions.SUM:
                        Double sum = new Double(0);
                        for (Double value : valuesToBeEvaluated) {

                            sum = sum + value;
                        }
                        aggregrateResult = sum;
                        break;
                    case HkSystemConstantUtil.AggregrateFunctions.COUNT:
                        Double count = new Double(0);
                        for (Double value : valuesToBeEvaluated) {
                            count++;
                        }
                        aggregrateResult = count;
                        break;
                    case HkSystemConstantUtil.AggregrateFunctions.AVG:
                        Double avg = new Double(0);
                        Double totalNum = new Double(0);
                        Double Sum_totalNum = new Double(0);
                        for (Double value : valuesToBeEvaluated) {
                            totalNum++;
                            Sum_totalNum = Sum_totalNum + value;
                        }
                        Double averageVal = Sum_totalNum / totalNum;
                        aggregrateResult = averageVal;
                        break;
                    case HkSystemConstantUtil.AggregrateFunctions.MIN:
                        aggregrateResult = Collections.min(valuesToBeEvaluated);
                        break;
                    case HkSystemConstantUtil.AggregrateFunctions.MAX:
                        aggregrateResult = Collections.max(valuesToBeEvaluated);
                        break;

                }
            }
        }
        return aggregrateResult;
    }

    @Override
    public Map<ObjectId, List<HkLotDocument>> retrieveParcelIdAndLotDocumentMap(Long franchise, Boolean isArchive
    ) {
        Map<ObjectId, List<HkLotDocument>> parcelIdLotDocs = null;
        List<HkLotDocument> listOfLotDocs = this.retrieveAllLot(franchise, isArchive, null);
        if (!CollectionUtils.isEmpty(listOfLotDocs)) {
            parcelIdLotDocs = new HashMap<>();
            for (HkLotDocument listOfLotDoc : listOfLotDocs) {

                if (parcelIdLotDocs.containsKey(listOfLotDoc.getParcel())) {
                    List<HkLotDocument> get = parcelIdLotDocs.get(listOfLotDoc.getParcel());
                    get.add(listOfLotDoc);
                    parcelIdLotDocs.put(listOfLotDoc.getParcel(), get);
                } else {
                    List<HkLotDocument> lotDoc = new ArrayList<>();
                    lotDoc.add(listOfLotDoc);
                    parcelIdLotDocs.put(listOfLotDoc.getParcel(), lotDoc);
                }

            }
        }
        return parcelIdLotDocs;
    }

    @Override
    public Map<ObjectId, List<HkPacketDocument>> retrieveParcelIdAndPacketDocumentMap(Long franchise, Boolean isArchive
    ) {
        Map<ObjectId, List<HkPacketDocument>> parcelIdPacketDocs = null;
        List<HkPacketDocument> packetDocLists = this.retrieveAllPacket(franchise, isArchive);
        if (!CollectionUtils.isEmpty(packetDocLists)) {
            parcelIdPacketDocs = new HashMap<>();
            for (HkPacketDocument packetDoc : packetDocLists) {

                if (parcelIdPacketDocs.containsKey(packetDoc.getParcel())) {
                    List<HkPacketDocument> get = parcelIdPacketDocs.get(packetDoc.getParcel());
                    get.add(packetDoc);
                    parcelIdPacketDocs.put(packetDoc.getParcel(), get);
                } else {
                    List<HkPacketDocument> packetDocs = new ArrayList<>();
                    packetDocs.add(packetDoc);
                    parcelIdPacketDocs.put(packetDoc.getParcel(), packetDocs);
                }

            }
        }
        return parcelIdPacketDocs;
    }

    @Override
    public Map<ObjectId, List<HkPacketDocument>> retrieveLotIdAndPacketDocumentMap(Long franchise, Boolean isArchive
    ) {
        Map<ObjectId, List<HkPacketDocument>> lotIdPacketDocs = null;
        List<HkPacketDocument> packetDocLists = this.retrieveAllPacket(franchise, isArchive);
        if (!CollectionUtils.isEmpty(packetDocLists)) {
            lotIdPacketDocs = new HashMap<>();
            for (HkPacketDocument packetDoc : packetDocLists) {

                if (lotIdPacketDocs.containsKey(packetDoc.getLot())) {
                    List<HkPacketDocument> get = lotIdPacketDocs.get(packetDoc.getLot());
                    get.add(packetDoc);
                    lotIdPacketDocs.put(packetDoc.getLot(), get);
                } else {
                    List<HkPacketDocument> packetDocs = new ArrayList<>();
                    packetDocs.add(packetDoc);
                    lotIdPacketDocs.put(packetDoc.getLot(), packetDocs);
                }

            }
        }
        return lotIdPacketDocs;

    }

    @Override
    public Map<ObjectId, List<HkPacketDocument>> retrieveInvoiceIdAndPacketDocumentMap(Long franchise, Boolean isArchive
    ) {
        Map<ObjectId, List<HkPacketDocument>> invoiceIdPacketDocs = null;
        List<HkPacketDocument> packetDocLists = this.retrieveAllPacket(franchise, isArchive);
        if (!CollectionUtils.isEmpty(packetDocLists)) {
            invoiceIdPacketDocs = new HashMap<>();
            for (HkPacketDocument packetDoc : packetDocLists) {

                if (invoiceIdPacketDocs.containsKey(packetDoc.getInvoice())) {
                    List<HkPacketDocument> get = invoiceIdPacketDocs.get(packetDoc.getInvoice());
                    get.add(packetDoc);
                    invoiceIdPacketDocs.put(packetDoc.getInvoice(), get);
                } else {
                    List<HkPacketDocument> packetDocs = new ArrayList<>();
                    packetDocs.add(packetDoc);
                    invoiceIdPacketDocs.put(packetDoc.getInvoice(), packetDocs);
                }

            }
        }
        return invoiceIdPacketDocs;
    }

    @Override
    public Map<ObjectId, List<HkParcelDocument>> retrieveInvoiceIdAndParcelDocumentMap(Long franchise, Boolean isArchive
    ) {

        Map<ObjectId, List<HkParcelDocument>> invoiceIdParcelDocs = null;
        List<HkParcelDocument> listOfParcelDocs = this.retrieveAllParcel(franchise, isArchive, null, null);
        if (!CollectionUtils.isEmpty(listOfParcelDocs)) {
            invoiceIdParcelDocs = new HashMap<>();
            for (HkParcelDocument listOfParcelDoc : listOfParcelDocs) {

                if (invoiceIdParcelDocs.containsKey(listOfParcelDoc.getInvoice())) {
                    List<HkParcelDocument> get = invoiceIdParcelDocs.get(listOfParcelDoc.getInvoice());
                    get.add(listOfParcelDoc);
                    invoiceIdParcelDocs.put(listOfParcelDoc.getInvoice(), get);
                } else {
                    List<HkParcelDocument> lotDoc = new ArrayList<>();
                    lotDoc.add(listOfParcelDoc);
                    invoiceIdParcelDocs.put(listOfParcelDoc.getInvoice(), lotDoc);
                }

            }
        }
        return invoiceIdParcelDocs;
    }

    @Override
    public Map<ObjectId, List<HkLotDocument>> retrieveInvoiceIdAndLotDocumentMap(Long franchise, Boolean isArchive
    ) {
        Map<ObjectId, List<HkLotDocument>> invoiceIdLotDocs = null;
        List<HkLotDocument> listOfLotDocs = this.retrieveAllLot(franchise, isArchive, null);
        if (!CollectionUtils.isEmpty(listOfLotDocs)) {
            invoiceIdLotDocs = new HashMap<>();
            for (HkLotDocument listOfLotDoc : listOfLotDocs) {

                if (invoiceIdLotDocs.containsKey(listOfLotDoc.getInvoice())) {
                    List<HkLotDocument> get = invoiceIdLotDocs.get(listOfLotDoc.getInvoice());
                    get.add(listOfLotDoc);
                    invoiceIdLotDocs.put(listOfLotDoc.getInvoice(), get);
                } else {
                    List<HkLotDocument> lotDoc = new ArrayList<>();
                    lotDoc.add(listOfLotDoc);
                    invoiceIdLotDocs.put(listOfLotDoc.getInvoice(), lotDoc);
                }

            }
        }
        return invoiceIdLotDocs;
    }

    //------------------------------------------ Diamond Service Ends -------------------------------------------//
    //------------------------------------------ Packet Service Starts ------------------------------------------//
    @Override
    public HkPacketDocument retrievePacketById(ObjectId packetId, Boolean isConversionReqd) {
        HkPacketDocument document = (HkPacketDocument) mongoGenericDao.retrieveById(packetId, HkPacketDocument.class);
        if (isConversionReqd) {
            String pointerComponentType = HkSystemConstantUtil.CustomField.ComponentType.POINTER + "@*" + HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN;
            Map<String, String> uiFieldMap = null;
            Map<String, Object> customMap = document.getFieldValue().toMap();
            uiFieldMap = this.createFieldNameWithComponentType(customMap);

            if (!CollectionUtils.isEmpty(uiFieldMap)) {
                for (Map.Entry<String, String> field : uiFieldMap.entrySet()) {
                    if (field.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN) || field.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.USER_MULTISELECT) || field.getValue().equals(pointerComponentType)) {
                        if (customMap != null && !customMap.isEmpty() && customMap.containsKey(field.getKey())) {
                            String value = customMap.get(field.getKey()).toString().replaceAll("\\[", "").replaceAll("\\]", "");
                            customMap.put(field.getKey(), value);
                        }
                    }
                    document.getFieldValue().putAll(customMap);
                }
            }
        }
        return document;
    }

    @Override
    public List<HkPacketDocument> retrieveAllPacket(Long franchise, Boolean isActive
    ) {
        List<Criteria> criterias = new ArrayList<>();
        List<HkPacketDocument> documents = null;
        criterias.add(Criteria.where(PacketFields.FRANCHISE).is(franchise));
        if (isActive != null) {
            criterias.add(Criteria.where(PacketFields.IS_ARCHIVE).is(isActive));
        }
        HkSystemConfigurationDocument systemConfigurationDocument = this.retrieveSystemConfig(HkSystemConstantUtil.FranchiseConfiguration.CURR_VISIBILITY_STATUS);
        if (systemConfigurationDocument != null) {
            if (systemConfigurationDocument.getKeyValue() != null) {
                if (systemConfigurationDocument.getKeyValue().equalsIgnoreCase(HkSystemConstantUtil.SWITCH_OFF)) {
                    criterias.add(Criteria.where(PacketFields.HAVE_VALUE).is(Boolean.FALSE));

                }
            }
        }
        documents = mongoGenericDao.findByCriteria(criterias, HkPacketDocument.class
        );
        return documents;
    }

    @Override
    @MongoTransaction
    public Boolean deletePacket(ObjectId packetId
    ) {
        Boolean success = false;
        if (packetId != null) {
            System.out.println("packetId" + packetId);
            HkPacketDocument packetDocument = (HkPacketDocument) mongoGenericDao.retrieveById(packetId, HkPacketDocument.class
            );
            if (packetDocument
                    != null) {
                packetDocument.setIsArchive(true);
                packetDocument.setStatus(HkSystemConstantUtil.StockStatus.ARCHIVED);
                mongoGenericDao.update(packetDocument);
                System.out.println("packetDocument.getLot() " + packetDocument.getLot());
                HkLotDocument lotDocument = (HkLotDocument) mongoGenericDao.retrieveById(packetDocument.getLot(), HkLotDocument.class);
                System.out.println("lotDocument" + lotDocument);
                if (lotDocument != null) {
                    lotDocument.setLastModifiedOn(new Date());
                    if (packetDocument.getFieldValue() != null) {
                        System.out.println("" + packetDocument.getFieldValue());
                        Object packetStockCarat = packetDocument.getFieldValue().get(HkSystemConstantUtil.PacketStaticFieldName.CARAT);
                        Object packetStockPieces = packetDocument.getFieldValue().get(HkSystemConstantUtil.PacketStaticFieldName.PIECES);
                        if (packetStockCarat != null && packetStockPieces != null) {
                            lotDocument.setStockCarat(lotDocument.getStockCarat() + (Double) packetStockCarat);
                            lotDocument.setStockPieces(lotDocument.getStockPieces() + (Long) packetStockPieces);
                            mongoGenericDao.update(lotDocument);
                            success = true;
                        }
                    }
                }
            }
        }
        return success;
    }

    @Override
    public List<HkPacketDocument> retrievePackets(Map<String, Object> fieldValues, List<ObjectId> invoiceIds, List<ObjectId> parcelIds, List<ObjectId> lotIds, long franchise, Boolean isArchive, String inStockOf, String status, ObjectId splitFrom, Long pieces
    ) {
        List<HkPacketDocument> packetDocuments = null;
        List<Criteria> criterias = new ArrayList<>();
        Map<String, String> uiFieldMap = this.createFieldNameWithComponentType(fieldValues);
        Map<String, Object> dateCriteriaMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(fieldValues)) {
            for (Map.Entry<String, Object> entry : fieldValues.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                if (uiFieldMap.containsKey(key)) {
                    String cType = uiFieldMap.get(key);
                    // Specific logic for Date type component
                    if (cType.equals(HkSystemConstantUtil.CustomField.ComponentType.DATE)) {
                        if (value != null) {
                            SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS\'Z\'");
                            formatDate.setTimeZone(TimeZone.getTimeZone("UTC"));
                            Date parseDate = null;
                            Date parseIncDate = null;
                            try {
                                // parseDate gives the formatted date
                                parseDate = formatDate.parse(value.toString());
                                Calendar c = Calendar.getInstance();
                                c.setTime(parseDate);
                                c.add(Calendar.DATE, 1);
                                // incremented date contains next date i.e the current date +1
//                                String incrementedDate = formatDate.format(c.getTime()); // new date
                                parseIncDate = new Date(c.getTimeInMillis());

                            } catch (ParseException ex) {
                                Logger.getLogger(HkStockServiceImpl.class
                                        .getName()).log(Level.SEVERE, null, ex);
                            }
                            if (!(key.startsWith("from") || key.startsWith("to"))) {
                                criterias.add(Criteria.where(PacketFields.FIELD_VALUE + "." + key).gt(parseDate).lt(parseIncDate));
                            } else {
                                if (key.startsWith("from")) {
                                    dateCriteriaMap.put(key, parseDate);
                                } else {
                                    dateCriteriaMap.put(key, parseIncDate);
                                }
                            }
                        }
                    } // Code added for handling image and file but since we are not showing image and file components in search it is of no use as for now
                    else if (cType.equals(HkSystemConstantUtil.CustomField.ComponentType.IMAGE) || cType.equals(HkSystemConstantUtil.CustomField.ComponentType.UPLOAD)) {
                        criterias.add(Criteria.where(PacketFields.FIELD_VALUE + "." + key).regex(value.toString()));
                    } else {
                        if (value instanceof Collection) {
                            Collection coll = (Collection) value;
                            if (!CollectionUtils.isEmpty(coll)) {
                                criterias.add(Criteria.where(PacketFields.FIELD_VALUE + "." + key).in(coll));
                            }
                        } else if (value instanceof Date) {
                            long date = Long.parseLong(value.toString());
                            criterias.add(Criteria.where(PacketFields.FIELD_VALUE + "." + key).is(new Date(date)));
                        } else {
                            if (value != null && value.toString().length() > 0) {
                                criterias.add(Criteria.where(PacketFields.FIELD_VALUE + "." + key).is(value));
                            }
                        }
                    }
                }

            }
        }
        if (!CollectionUtils.isEmpty(invoiceIds)) {
            criterias.add(Criteria.where(PacketFields.INVOICE_ID).in(invoiceIds));
        }

        if (!StringUtils.isEmpty(inStockOf)) {
            criterias.add(Criteria.where(PacketFields.FIELD_VALUE + "." + HkSystemConstantUtil.PacketStaticFieldName.IN_STOCK_OF).in(Arrays.asList(inStockOf)));
        }
        if (pieces != null) {
            criterias.add(Criteria.where(PacketFields.FIELD_VALUE + "." + HkSystemConstantUtil.PacketStaticFieldName.PIECES).gt(pieces));
        }
        if (!CollectionUtils.isEmpty(parcelIds)) {
            criterias.add(Criteria.where(PacketFields.PARCEL_ID).in(parcelIds));
        }
        if (!CollectionUtils.isEmpty(lotIds)) {
            criterias.add(Criteria.where(PacketFields.LOT_ID).in(lotIds));
        }
        if (!StringUtils.isEmpty(status)) {
            criterias.add(Criteria.where(PacketFields.STATUS).is(status));
        }
        criterias.add(Criteria.where(PacketFields.FRANCHISE).is(franchise));
        if (isArchive != null) {
            criterias.add(Criteria.where(PacketFields.IS_ARCHIVE).is(isArchive));
        }
        if (splitFrom != null) {
            criterias.add(Criteria.where(PacketFields.SPLIT_FROM).is(splitFrom));
        }
        HkSystemConfigurationDocument systemConfigurationDocument = this.retrieveSystemConfig(HkSystemConstantUtil.FranchiseConfiguration.CURR_VISIBILITY_STATUS);
        if (systemConfigurationDocument != null) {
            if (systemConfigurationDocument.getKeyValue() != null) {
                if (systemConfigurationDocument.getKeyValue().equalsIgnoreCase(HkSystemConstantUtil.SWITCH_OFF)) {
                    criterias.add(Criteria.where(PacketFields.HAVE_VALUE).is(Boolean.FALSE));
                }
            }
        }
        if (!CollectionUtils.isEmpty(dateCriteriaMap)) {
            Set<String> removedFields = new HashSet<>();
            Iterator<Map.Entry<String, Object>> itr = dateCriteriaMap.entrySet().iterator();
            while (itr.hasNext()) {
                Map.Entry<String, Object> entryMap = itr.next();
                String key = entryMap.getKey();
                Date value = (Date) entryMap.getValue();

                if (!removedFields.contains(key)) {
                    if (key.startsWith("from")) {
                        if (dateCriteriaMap.containsKey("to" + key.substring(4))) {
                            Date toValue = (Date) dateCriteriaMap.get("to" + key.substring(4));
                            criterias.add(Criteria.where(PacketFields.FIELD_VALUE + "." + key.substring(4)).gte(value).lt(toValue));
                            removedFields.add("to" + key.substring(4));
                            removedFields.add(key);
                        } else {
                            criterias.add(Criteria.where(PacketFields.FIELD_VALUE + "." + key.substring(4)).gte(value));
                            removedFields.add(key);
                        }
                    } else {
                        if (dateCriteriaMap.containsKey("from" + key.substring(2))) {
                            Date toValue = (Date) dateCriteriaMap.get("from" + key.substring(2));
                            criterias.add(Criteria.where(PacketFields.FIELD_VALUE + "." + key.substring(2)).gte(toValue).lt(value));
                            removedFields.add("from" + key.substring(2));
                            removedFields.add(key);
                        } else {
                            criterias.add(Criteria.where(PacketFields.FIELD_VALUE + "." + key.substring(2)).lt(value));
                            removedFields.add(key);

                        }
                    }
                }
            }
        }
        packetDocuments = mongoGenericDao.findByCriteria(criterias, HkPacketDocument.class
        );
        List<HkPacketDocument> finalList = null;

        if (!CollectionUtils.isEmpty(packetDocuments)) {
            finalList = new ArrayList<>();
            String pointerComponentType = HkSystemConstantUtil.CustomField.ComponentType.POINTER + "@*" + HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN;

            for (HkPacketDocument packetDocument : packetDocuments) {
                HkPacketDocument temp = packetDocument;
                Map<String, Object> customMap = packetDocument.getFieldValue().toMap();
                uiFieldMap = this.createFieldNameWithComponentType(customMap);
                if (!CollectionUtils.isEmpty(uiFieldMap)) {
                    for (Map.Entry<String, String> field : uiFieldMap.entrySet()) {
                        if (field.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN) || field.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.USER_MULTISELECT) || field.getValue().equals(pointerComponentType)) {
                            if (customMap != null && !customMap.isEmpty() && customMap.containsKey(field.getKey())) {
                                String value = customMap.get(field.getKey()).toString().replaceAll("\\[", "").replaceAll("\\]", "");
                                customMap.put(field.getKey(), value);
                            }
                        }
                    }
                    temp.getFieldValue().putAll(customMap);
                    finalList.add(temp);
                }
            }

        }
        return finalList;
    }

    @Override
    public List<ObjectId> retrievePacketIds(Map<String, Object> fieldValues, List<ObjectId> invoiceIds, List<ObjectId> parcelIds, List<ObjectId> lotIds, long franchise, Boolean isActive
    ) {
        List<ObjectId> ids = null;
        List<HkPacketDocument> packetDocuments = null;
        List<Criteria> criterias = new ArrayList<>();
        if (!CollectionUtils.isEmpty(fieldValues)) {
            for (Map.Entry<String, Object> entry : fieldValues.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                if (value instanceof Collection) {
                    Collection coll = (Collection) value;
                    criterias.add(Criteria.where(PacketFields.FIELD_VALUE + "." + key).in(coll));
                } else {
                    criterias.add(Criteria.where(PacketFields.FIELD_VALUE + "." + key).is(value));
                }
            }
        }
        if (!CollectionUtils.isEmpty(invoiceIds)) {
            criterias.add(Criteria.where(PacketFields.INVOICE_ID).in(invoiceIds));
        }
        if (!CollectionUtils.isEmpty(parcelIds)) {
            criterias.add(Criteria.where(PacketFields.PARCEL_ID).in(parcelIds));
        }
        if (!CollectionUtils.isEmpty(lotIds)) {
            criterias.add(Criteria.where(PacketFields.LOT_ID).in(lotIds));
        }
        criterias.add(Criteria.where(PacketFields.FRANCHISE).is(franchise));
        if (isActive != null) {
            criterias.add(Criteria.where(PacketFields.IS_ARCHIVE).is(isActive));
        }
        HkSystemConfigurationDocument systemConfigurationDocument = this.retrieveSystemConfig(HkSystemConstantUtil.FranchiseConfiguration.CURR_VISIBILITY_STATUS);
        if (systemConfigurationDocument != null) {
            if (systemConfigurationDocument.getKeyValue() != null) {
                if (systemConfigurationDocument.getKeyValue().equalsIgnoreCase(HkSystemConstantUtil.SWITCH_OFF)) {
                    criterias.add(Criteria.where(PacketFields.HAVE_VALUE).is(Boolean.FALSE));

                }
            }
        }
        packetDocuments = mongoGenericDao.findByCriteria(criterias, HkPacketDocument.class
        );
        if (!CollectionUtils.isEmpty(packetDocuments)) {
            ids = new LinkedList<>();
            for (HkPacketDocument hkPacketDocument : packetDocuments) {
                ids.add(hkPacketDocument.getId());
            }
        }
        return ids;
    }

    @Override
    @MongoTransaction
    public String updatePacket(ObjectId id, Map<String, Object> lotCustomMap, Map<String, String> lotDbTypeMap, Map<String, HkFieldDocument> mapForFeatureInvolvedInFormula, Map<String, String> uiFieldMap, List<HkFieldDocument> totalformulaList, Map<Long, String> featureIdWithNameMap, Long franchise, Long updatedBy, Long featureId) {

        if (id != null) {
            Long pieces = null;
            Double carat = null;
            List<HkPacketDocument> packetDocumentsForFormulaScenarios = null;
            HkPacketDocument packetDocument = (HkPacketDocument) mongoGenericDao.retrieveById(id, HkPacketDocument.class
            );
            BasicBSONObject fieldValues = customFieldService.makeBSONObject(lotCustomMap, lotDbTypeMap, uiFieldMap, HkSystemConstantUtil.Feature.LOT, franchise, null, Boolean.TRUE, packetDocument.getFieldValue().toMap(), null);
            if (packetDocument
                    != null) {
                packetDocumentsForFormulaScenarios = new ArrayList<>();
//                packetDocument.getFieldValue().putAll(fieldValues.toMap());
                packetDocument.setLastModifiedBy(updatedBy);
                packetDocument.setLastModifiedOn(new Date());
                packetDocument.setFieldValue(fieldValues);

                if (fieldValues.containsField(HkSystemConstantUtil.PacketStaticFieldName.CARAT) && fieldValues.get(HkSystemConstantUtil.PacketStaticFieldName.CARAT) != null) {
                    carat = (Double) fieldValues.get(HkSystemConstantUtil.PacketStaticFieldName.CARAT);
                }
                if (fieldValues.containsField(HkSystemConstantUtil.PacketStaticFieldName.PIECES) && fieldValues.get(HkSystemConstantUtil.PacketStaticFieldName.PIECES) != null) {
                    pieces = (Long) fieldValues.get(HkSystemConstantUtil.PacketStaticFieldName.PIECES);
                }
                if (carat != null || pieces != null) {
                    ObjectId lotId = packetDocument.getLot();
                    HkLotDocument hkLotDocument = this.retrieveLotById(lotId);
                    if (hkLotDocument != null) {
                        if (hkLotDocument.getStockCarat() != null && carat != null) {
                            Double stockCarat = hkLotDocument.getStockCarat();
                            Double newCaratValue = stockCarat - carat;
                            hkLotDocument.setStockCarat(newCaratValue);
                        }
                        if (hkLotDocument.getStockPieces() != null && pieces != null) {
                            Long stockPieces = hkLotDocument.getStockPieces();
                            Long newPiecesValue = stockPieces - pieces;
                            hkLotDocument.setStockPieces(newPiecesValue);
                        }
                        mongoGenericDao.update(hkLotDocument);
                    }
                }
                //------------------------Call Formula First Scenario--------------------------------------
//                this.evaluateFirstScenarioForUpdatePacket(franchise, dbFieldWithFormulaMap, packetDocumentsForFormulaScenarios, featureId);
                Map<String, Double> finalFormulaEvalMap = hkFormulaExecution.mapOfEvaluatedFormulaValueWithDbField(totalformulaList, franchise, null, null, null, packetDocument, null, packetDocument.getInvoice(), packetDocument.getParcel(), packetDocument.getLot(), featureId, HkSystemConstantUtil.FeatureForFormulaEvaluation.EDIT_PACKET);
                if (finalFormulaEvalMap != null && !finalFormulaEvalMap.isEmpty()) {
                    for (Map.Entry<String, Double> entrySet : finalFormulaEvalMap.entrySet()) {
                        packetDocument.getFieldValue().put(entrySet.getKey(), entrySet.getValue());
                    }
                }
                mongoGenericDao.update(packetDocument);
                // Add packet document which needs to be updated for formula
                packetDocumentsForFormulaScenarios.add(packetDocument);

//------------------------ Call Formula Second Scenario------------------------------------
                HkStockServiceImpl hkStockServiceImpl = applicationContext.getBean(this.getClass());
                hkStockServiceImpl.evaluateSecondScenarioForUpdatePacket(franchise, mapForFeatureInvolvedInFormula, featureIdWithNameMap, packetDocumentsForFormulaScenarios);
                Object get = packetDocument.getFieldValue().get(HkSystemConstantUtil.AutoNumber.PACKET_ID);
                LOGGER.info("packetDocument.getFieldValue()" + packetDocument.getFieldValue());
                return packetDocument.getFieldValue().get(HkSystemConstantUtil.AutoNumber.PACKET_ID).toString();
            } else {
                return null;
            }
        } else {
            return null;
        }
    }
    //    @Async
    //    @MongoTransaction

    @Async
    @MongoTransaction
    public void evaluateSecondScenarioForUpdatePacket(Long franchise, Map<String, HkFieldDocument> mapForFeatureInvolvedInFormula, Map<Long, String> featureIdWithNameMap, List<HkPacketDocument> packetDocuments
    ) {

        // Code added By Shifa on 19 feb 2015 for formula Evaluation For Second scenario i.e. on lot update ,update all other douments where lot formula is involved
// Code For Second scenario
        if (!CollectionUtils.isEmpty(packetDocuments)) {

            Map<ObjectId, List<HkLotDocument>> parcelIdAndLotDocumentMap = this.retrieveParcelIdAndLotDocumentMap(franchise, Boolean.FALSE);
            Map<ObjectId, List<HkPacketDocument>> parcelIdAndPacketDocumentMap = this.retrieveParcelIdAndPacketDocumentMap(franchise, Boolean.FALSE);
            Map<ObjectId, List<HkPacketDocument>> lotIdAndPacketDocumentMap = this.retrieveLotIdAndPacketDocumentMap(franchise, Boolean.FALSE);
            Map<ObjectId, List<HkParcelDocument>> invoiceIdAndParcelDocumentMap = this.retrieveInvoiceIdAndParcelDocumentMap(franchise, Boolean.FALSE);
            Map<ObjectId, List<HkLotDocument>> invoiceIdAndLotDocumentMap = this.retrieveInvoiceIdAndLotDocumentMap(franchise, Boolean.FALSE);
            Map<ObjectId, List<HkPacketDocument>> invoiceIdAndPacketDocumentMap = this.retrieveInvoiceIdAndPacketDocumentMap(franchise, Boolean.FALSE);
            Map<ObjectId, HkInvoiceDocument> invoiceIdAndDocumentMap = this.retrieveInvoiceIdAndDocumentMap(franchise, Boolean.FALSE);
            Map<ObjectId, HkParcelDocument> parcelIdAndDocumentMap = this.retrieveParcelIdAndDocumentMap(franchise, Boolean.FALSE);
            Map<ObjectId, HkLotDocument> lotIdAndDocumentMap = this.retrieveLotIdAndDocumentMap(franchise, Boolean.FALSE);
            for (HkPacketDocument packetDocument : packetDocuments) {

                HkInvoiceDocument invoiceDocument = null;
                HkParcelDocument parcelDocument = null;
                HkLotDocument lotDocument = null;
                if (invoiceIdAndDocumentMap.containsKey(packetDocument.getInvoice())) {
                    invoiceDocument = invoiceIdAndDocumentMap.get(packetDocument.getInvoice());
                }
                if (parcelIdAndDocumentMap.containsKey(packetDocument.getParcel())) {
                    parcelDocument = parcelIdAndDocumentMap.get(packetDocument.getParcel());
                }
                if (lotIdAndDocumentMap.containsKey(packetDocument.getLot())) {
                    lotDocument = lotIdAndDocumentMap.get(packetDocument.getLot());
                }

                ScriptEngineManager manager = new ScriptEngineManager();
                ScriptEngine engine = manager.getEngineByName("js");

                if (mapForFeatureInvolvedInFormula != null && !mapForFeatureInvolvedInFormula.isEmpty()) {
                    for (Map.Entry<String, HkFieldDocument> entrySet : mapForFeatureInvolvedInFormula.entrySet()) {
                        String featureName = featureIdWithNameMap.get(entrySet.getValue().getFeature());

                        // Code If the dbField is Invoice
                        if (featureName.equalsIgnoreCase(HkSystemConstantUtil.Feature.INVOICE)) {
                            String[] formulaValue = entrySet.getValue().getFormulaValue().split("\\|");
                            List<String> modifiedFormula = new ArrayList<>();
                            for (int l = 0; l < formulaValue.length; l++) {
                                if (formulaValue[l].contains(HkSystemConstantUtil.Feature.INVOICE)) {
                                    String formulaSubPart[] = formulaValue[l].split("\\.");
                                    Map invoicelDoc = invoiceIdAndDocumentMap.get(packetDocument.getInvoice()).getFieldValue().toMap();
                                    if (invoicelDoc.containsKey(formulaSubPart[1])) {
                                        String parcelVal = invoicelDoc.get(formulaSubPart[1]).toString();
                                        if (parcelVal != null) {
                                            formulaValue[l] = parcelVal;
                                        }
                                    }
                                }
                                if (formulaValue[l].contains(HkSystemConstantUtil.Feature.PARCEL)) {
                                    String formulaSubPart[] = formulaValue[l].split("\\.");
                                    if (l >= 2 && (formulaValue[l - 2].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.SUM)
                                            || formulaValue[l - 2].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MIN)
                                            || formulaValue[l - 2].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MAX)
                                            || formulaValue[l - 2].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.AVG)
                                            || formulaValue[l - 2].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.COUNT))) {
                                        if (invoiceIdAndParcelDocumentMap != null && !invoiceIdAndParcelDocumentMap.isEmpty()) {
                                            if (invoiceIdAndParcelDocumentMap.containsKey(packetDocument.getInvoice())) {
                                                List<HkParcelDocument> parcelDocsForInvoice = invoiceIdAndParcelDocumentMap.get(packetDocument.getInvoice());
                                                Double aggregrateFunctionResult = this.calculateParcelAggegateFunction(parcelDocsForInvoice, formulaSubPart[1], formulaValue[l - 2]);
                                                if (aggregrateFunctionResult != null) {
                                                    formulaValue[l] = aggregrateFunctionResult.toString();
                                                }
                                            }

                                        }

                                    }
                                }
                                if (formulaValue[l].contains(HkSystemConstantUtil.Feature.LOT)) {
                                    String formulaSubPart[] = formulaValue[l].split("\\.");
                                    if (l >= 2 && (formulaValue[l - 2].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.SUM)
                                            || formulaValue[l - 2].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MIN)
                                            || formulaValue[l - 2].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MAX)
                                            || formulaValue[l - 2].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.AVG)
                                            || formulaValue[l - 2].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.COUNT))) {
                                        if (invoiceIdAndLotDocumentMap != null && !invoiceIdAndLotDocumentMap.isEmpty()) {
                                            if (invoiceIdAndLotDocumentMap.containsKey(packetDocument.getInvoice())) {
                                                List<HkLotDocument> lotDocsForInvoice = invoiceIdAndLotDocumentMap.get(packetDocument.getInvoice());
                                                Double aggregrateFunctionResult = this.calculateLotAggegateFunction(lotDocsForInvoice, formulaSubPart[1], formulaValue[l - 2]);
                                                if (aggregrateFunctionResult != null) {
                                                    formulaValue[l] = aggregrateFunctionResult.toString();
                                                }
                                            }

                                        }

                                    }

                                }
                                if (formulaValue[l].contains(HkSystemConstantUtil.Feature.PACKET)) {
                                    String formulaSubPart[] = formulaValue[l].split("\\.");
                                    if (l >= 2 && (formulaValue[l - 2].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.SUM)
                                            || formulaValue[l - 2].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MIN)
                                            || formulaValue[l - 2].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MAX)
                                            || formulaValue[l - 2].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.AVG)
                                            || formulaValue[l - 2].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.COUNT))) {
                                        if (invoiceIdAndPacketDocumentMap != null && !invoiceIdAndPacketDocumentMap.isEmpty()) {
                                            if (invoiceIdAndPacketDocumentMap.containsKey(packetDocument.getInvoice())) {
                                                List<HkPacketDocument> packetDocsForInvoice = invoiceIdAndPacketDocumentMap.get(packetDocument.getInvoice());
                                                Double aggregrateFunctionResult = this.calculatePacketAggegateFunction(packetDocsForInvoice, formulaSubPart[1], formulaValue[l - 2]);
                                                if (aggregrateFunctionResult != null) {
                                                    formulaValue[l] = aggregrateFunctionResult.toString();
                                                }
                                            }

                                        }

                                    }
                                }

                            }
                            //
                            for (String oldFormula : formulaValue) {
                                if (!(oldFormula.equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.SUM)) && !(oldFormula.equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MIN))
                                        && !(oldFormula.equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MAX)) && !(oldFormula.equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.AVG))
                                        && !(oldFormula.equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.COUNT)) && !(oldFormula.equalsIgnoreCase("(")) && !(oldFormula.equalsIgnoreCase(")"))) {
                                    modifiedFormula.add(oldFormula);
                                }
                            }
                            String[] newForm = new String[modifiedFormula.size()];
                            newForm = modifiedFormula.toArray(newForm);

                            String finalModFormula = Arrays.toString(newForm).replaceAll("|", "").replaceAll(",", "");
                            String finalModFormulaString = finalModFormula.replace("[", "").replace("]", "");
                            try {
                                Object form_eval = engine.eval(finalModFormulaString);
                                invoiceDocument.getFieldValue().put(entrySet.getKey(), Double.parseDouble(form_eval.toString()));
                                mongoGenericDao.update(invoiceDocument);
                            } catch (ScriptException | NumberFormatException e) {
                                continue;
                            }

                        }
                        // Code for handling invoice feature ends here
// Code If the dbField is Parcel
                        if (featureName.equalsIgnoreCase(HkSystemConstantUtil.Feature.PARCEL)) {
                            String[] formulaValue = entrySet.getValue().getFormulaValue().split("\\|");
                            List<String> modifiedFormula = new ArrayList<>();
                            for (int l = 0; l < formulaValue.length; l++) {
                                if (formulaValue[l].contains(HkSystemConstantUtil.Feature.INVOICE)) {
                                    String formulaSubPart[] = formulaValue[l].split("\\.");
                                    Map invoicelDoc = invoiceIdAndDocumentMap.get(packetDocument.getInvoice()).getFieldValue().toMap();
                                    if (invoicelDoc.containsKey(formulaSubPart[1])) {
                                        String invoiceVal = invoicelDoc.get(formulaSubPart[1]).toString();
                                        if (invoiceVal != null) {
                                            formulaValue[l] = invoiceVal;
                                        }
                                    }
                                }
                                if (formulaValue[l].contains(HkSystemConstantUtil.Feature.PARCEL)) {
                                    String formulaSubPart[] = formulaValue[l].split("\\.");
                                    Map parcelDoc = parcelIdAndDocumentMap.get(packetDocument.getParcel()).getFieldValue().toMap();
                                    if (parcelDoc.containsKey(formulaSubPart[1])) {
                                        String parcelVal = parcelDoc.get(formulaSubPart[1]).toString();
                                        if (parcelVal != null) {
                                            formulaValue[l] = parcelVal;
                                        }
                                    }
                                }
                                if (formulaValue[l].contains(HkSystemConstantUtil.Feature.LOT)) {
                                    String formulaSubPart[] = formulaValue[l].split("\\.");
                                    if (l >= 2 && (formulaValue[l - 2].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.SUM)
                                            || formulaValue[l - 2].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MIN)
                                            || formulaValue[l - 2].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MAX)
                                            || formulaValue[l - 2].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.AVG)
                                            || formulaValue[l - 2].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.COUNT))) {
                                        if (parcelIdAndLotDocumentMap != null && !parcelIdAndDocumentMap.isEmpty()) {
                                            if (parcelIdAndLotDocumentMap.containsKey(packetDocument.getParcel())) {
                                                List<HkLotDocument> lotDocsForParcelDoc = parcelIdAndLotDocumentMap.get(packetDocument.getParcel());
                                                Double aggregrateFunctionResult = this.calculateLotAggegateFunction(lotDocsForParcelDoc, formulaSubPart[1], formulaValue[l - 2]);
                                                if (aggregrateFunctionResult != null) {
                                                    formulaValue[l] = aggregrateFunctionResult.toString();
                                                }
                                            }

                                        }
                                    }

                                }
                                if (formulaValue[l].contains(HkSystemConstantUtil.Feature.PACKET)) {
                                    String formulaSubPart[] = formulaValue[l].split("\\.");
                                    if (l >= 2 && (formulaValue[l - 2].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.SUM)
                                            || formulaValue[l - 2].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MIN)
                                            || formulaValue[l - 2].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MAX)
                                            || formulaValue[l - 2].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.AVG)
                                            || formulaValue[l - 2].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.COUNT))) {
                                        if (parcelIdAndPacketDocumentMap != null && !parcelIdAndPacketDocumentMap.isEmpty()) {
                                            if (parcelIdAndPacketDocumentMap.containsKey(packetDocument.getParcel())) {
                                                List<HkPacketDocument> packetDocsForParcelDoc = parcelIdAndPacketDocumentMap.get(packetDocument.getParcel());
                                                Double aggregrateFunctionResult = this.calculatePacketAggegateFunction(packetDocsForParcelDoc, formulaSubPart[1], formulaValue[l - 2]);
                                                if (aggregrateFunctionResult != null) {
                                                    formulaValue[l] = aggregrateFunctionResult.toString();
                                                }
                                            }
                                        }

                                    }
                                }

                            }
                            //
                            for (String oldFormula : formulaValue) {
                                if (!(oldFormula.equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.SUM)) && !(oldFormula.equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MIN))
                                        && !(oldFormula.equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MAX)) && !(oldFormula.equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.AVG))
                                        && !(oldFormula.equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.COUNT)) && !(oldFormula.equalsIgnoreCase("(")) && !(oldFormula.equalsIgnoreCase(")"))) {
                                    modifiedFormula.add(oldFormula);
                                }
                            }
                            String[] newForm = new String[modifiedFormula.size()];
                            newForm = modifiedFormula.toArray(newForm);

                            String finalModFormula = Arrays.toString(newForm).replaceAll("|", "").replaceAll(",", "");
                            String finalModFormulaString = finalModFormula.replace("[", "").replace("]", "");
                            try {
                                Object form_eval = engine.eval(finalModFormulaString);
                                parcelDocument.getFieldValue().put(entrySet.getKey(), Double.parseDouble(form_eval.toString()));
                                mongoGenericDao.update(parcelDocument);
                            } catch (ScriptException | NumberFormatException e) {
                                continue;
                            }

                        }
                        // Code for handling parcel feature ends here

                        // Code if the feature is lot starts here
                        if (featureName.equalsIgnoreCase(HkSystemConstantUtil.Feature.LOT)) {
                            String[] formulaValue = entrySet.getValue().getFormulaValue().split("\\|");
                            List<String> modifiedFormula = new ArrayList<>();
                            for (int l = 0; l < formulaValue.length; l++) {
                                if (formulaValue[l].contains(HkSystemConstantUtil.Feature.INVOICE)) {
                                    String formulaSubPart[] = formulaValue[l].split("\\.");
                                    Map invoicelDoc = invoiceIdAndDocumentMap.get(packetDocument.getInvoice()).getFieldValue().toMap();
                                    if (invoicelDoc.containsKey(formulaSubPart[1])) {
                                        String invoiceVal = invoicelDoc.get(formulaSubPart[1]).toString();
                                        if (invoiceVal != null) {
                                            formulaValue[l] = invoiceVal;
                                        }
                                    }
                                }
                                if (formulaValue[l].contains(HkSystemConstantUtil.Feature.PARCEL)) {
                                    String formulaSubPart[] = formulaValue[l].split("\\.");
                                    Map parcelDoc = parcelIdAndDocumentMap.get(packetDocument.getParcel()).getFieldValue().toMap();
                                    if (parcelDoc.containsKey(formulaSubPart[1])) {
                                        String parcelVal = parcelDoc.get(formulaSubPart[1]).toString();
                                        if (parcelVal != null) {
                                            formulaValue[l] = parcelVal;
                                        }
                                    }
                                }
                                if (formulaValue[l].contains(HkSystemConstantUtil.Feature.LOT)) {
                                    String formulaSubPart[] = formulaValue[l].split("\\.");
                                    Map lotDoc = lotIdAndDocumentMap.get(packetDocument.getLot()).getFieldValue().toMap();
                                    if (lotDoc.containsKey(formulaSubPart[1])) {
                                        String lotVal = lotDoc.get(formulaSubPart[1]).toString();
                                        if (lotVal != null) {
                                            formulaValue[l] = lotVal;
                                        }
                                    }

                                }
                                if (formulaValue[l].contains(HkSystemConstantUtil.Feature.PACKET)) {
                                    String formulaSubPart[] = formulaValue[l].split("\\.");
                                    if (l >= 2 && (formulaValue[l - 2].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.SUM)
                                            || formulaValue[l - 2].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MIN)
                                            || formulaValue[l - 2].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MAX)
                                            || formulaValue[l - 2].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.AVG)
                                            || formulaValue[l - 2].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.COUNT))) {
                                        if (lotIdAndPacketDocumentMap != null && !lotIdAndPacketDocumentMap.isEmpty()) {
                                            if (lotIdAndPacketDocumentMap.containsKey(packetDocument.getLot())) {
                                                List<HkPacketDocument> packetDocsForLotDoc = lotIdAndPacketDocumentMap.get(packetDocument.getLot());
                                                Double aggregrateFunctionResult = this.calculatePacketAggegateFunction(packetDocsForLotDoc, formulaSubPart[1], formulaValue[l - 2]);
                                                if (aggregrateFunctionResult != null) {
                                                    formulaValue[l] = aggregrateFunctionResult.toString();
                                                }
                                            }
                                        }

                                    }
                                }

                            }
                            //
                            for (String oldFormula : formulaValue) {
                                if (!(oldFormula.equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.SUM)) && !(oldFormula.equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MIN))
                                        && !(oldFormula.equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MAX)) && !(oldFormula.equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.AVG))
                                        && !(oldFormula.equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.COUNT)) && !(oldFormula.equalsIgnoreCase("(")) && !(oldFormula.equalsIgnoreCase(")"))) {
                                    modifiedFormula.add(oldFormula);
                                }
                            }
                            String[] newForm = new String[modifiedFormula.size()];
                            newForm = modifiedFormula.toArray(newForm);

                            String finalModFormula = Arrays.toString(newForm).replaceAll("|", "").replaceAll(",", "");
                            String finalModFormulaString = finalModFormula.replace("[", "").replace("]", "");
                            try {
                                Object form_eval = engine.eval(finalModFormulaString);
                                lotDocument.getFieldValue().put(entrySet.getKey(), Double.parseDouble(form_eval.toString()));
                                mongoGenericDao.update(lotDocument);
                            } catch (ScriptException | NumberFormatException e) {
                                continue;
                            }

                        }
                        // Code for handling lot ends here

                    }

                }
            }
        }
    }

    @Override
    @MongoTransaction
    public ObjectId mergePacket(ObjectId parentLot, List<ObjectId> mergedFrom, List<BasicBSONObject> basicBSONObjects, Map<String, String> uiFieldMap, Long franchise, Long createdBy, List<ObjectId> workAllocationIds
    ) {
        if (!CollectionUtils.isEmpty(basicBSONObjects) && !CollectionUtils.isEmpty(workAllocationIds)) {

            String packetNumber = null; //customFieldService.generateNextFieldSequence(HkSystemConstantUtil.AutoNumber.PACKET_ID, franchise);
            if (packetNumber != null && mergedFrom != null) {
                HkPacketDocument packetDocument = new HkPacketDocument();
                if (parentLot != null) {
                    packetDocument.setLot(parentLot);
                    HkLotDocument lot = (HkLotDocument) mongoGenericDao.retrieveById(parentLot, HkLotDocument.class
                    );
                    if (lot
                            != null) {
                        if (lot.getParcel() != null) {
                            packetDocument.setParcel(lot.getParcel());
                        }
                        if (lot.getInvoice() != null) {
                            packetDocument.setInvoice(lot.getInvoice());
                        }
                        if (lot.getHaveValue() != null) {
                            packetDocument.setHaveValue(lot.getHaveValue());
                        } else {
                            packetDocument.setHaveValue(Boolean.FALSE);
                        }
                    } else {
                        return null;
                    }
                } else {
                    return null;
                }
                packetDocument.setFranchiseId(franchise);
                packetDocument.setIsArchive(Boolean.FALSE);
                packetDocument.setCreatedBy(createdBy);
                packetDocument.setLastModifiedBy(createdBy);
                packetDocument.setCreatedOn(new Date());
                packetDocument.setLastModifiedOn(new Date());
                packetDocument.setStatus(HkSystemConstantUtil.StockStatus.NEW_ROUGH);
                List<HkPacketDocument> listToBeupdate = new ArrayList<>();
                List<HkPacketDocument> packetDocuments = this.retrievePacketsbyIds(mergedFrom);
                if (!CollectionUtils.isEmpty(mergedFrom)) {
                    for (HkPacketDocument packet : packetDocuments) {
                        packet.setStatus(HkSystemConstantUtil.StockStatus.MERGED);
                        packet.setIsArchive(Boolean.TRUE);
                        packet.setLastModifiedBy(createdBy);
                        packet.setLastModifiedOn(new Date());
                        listToBeupdate.add(packet);
                    }
                }
                packetDocument.setMergedFrom(mergedFrom);
                listToBeupdate.add(packetDocument);
//                mongoGenericDao.createAll(listToBeupdate);
                for (HkPacketDocument packet : listToBeupdate) {
                    mongoGenericDao.update(packet);
                }
                BasicBSONObject fieldValues = basicBSONObjects.get(0);
                fieldValues.put(HkSystemConstantUtil.AutoNumber.PACKET_ID, packetNumber);
                packetDocument.setFieldValue(fieldValues);
                mongoGenericDao.update(packetDocument);
                return packetDocument.getId();
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    private List<HkPacketDocument> retrievePacketsbyIds(List<ObjectId> objectIds) {
        List<HkPacketDocument> packetDocuments = null;
        List<Criteria> criterias = new ArrayList<>();
        if (objectIds != null) {
            criterias.add(Criteria.where(PacketFields.PACKET_ID).in(objectIds));

        }
        packetDocuments = mongoGenericDao.findByCriteria(criterias, HkPacketDocument.class
        );
        return packetDocuments;
    }

    @Override
    @MongoTransaction
    public HkPacketDocument createpacket(BasicBSONObject basicBSONObject, List<HkFieldDocument> totalformulaList, Long franchise, Long createdBy, ObjectId invoiceId, ObjectId parcelId, ObjectId lotId, Long featureId, Long sequenceNumber) {
//        List<String> packetNumbers = null;
        List<HkPacketDocument> packetDocumentsToBeUpdated = null;
        Double carat = null;
        Long pieces = null;
        Map<String, String> map = null;
        if (basicBSONObject != null && franchise != null && createdBy != null && invoiceId != null && parcelId != null && lotId != null && sequenceNumber != null) {
//            packetNumbers = new ArrayList<>();
            packetDocumentsToBeUpdated = new ArrayList<>();
            Boolean hasValue = Boolean.FALSE;
            HkInvoiceDocument invoiceDocument = null;
            if (invoiceId != null) {
                invoiceDocument = (HkInvoiceDocument) mongoGenericDao.retrieveById(invoiceId, HkInvoiceDocument.class);
                if (invoiceDocument
                        != null) {
                    hasValue = invoiceDocument.getHaveValue();
                }
            }
            HkLotDocument lot = null;

            if (lotId != null) {
                lot = (HkLotDocument) mongoGenericDao.retrieveById(lotId, HkLotDocument.class
                );
            }
            HkPacketDocument packetDocument = new HkPacketDocument();
            if (invoiceId != null) {
                packetDocument.setInvoice(invoiceId);
            }
            if (parcelId != null) {
                packetDocument.setParcel(parcelId);
            }
            if (lotId != null) {
                packetDocument.setLot(lotId);
            }
            packetDocument.setFranchiseId(lot.getFranchiseId());
            packetDocument.setCreatedByFranchise(franchise);
            packetDocument.setIsArchive(Boolean.FALSE);
            packetDocument.setCreatedBy(createdBy);
            packetDocument.setLastModifiedBy(createdBy);
            packetDocument.setCreatedOn(new Date());
            packetDocument.setSequenceNumber(sequenceNumber);
            packetDocument.setLastModifiedOn(new Date());

            packetDocument.setStatus(HkSystemConstantUtil.StockStatus.PENDING);
            packetDocument.setHaveValue(hasValue);
            if (!basicBSONObject.containsField(HkSystemConstantUtil.PacketStaticFieldName.IN_STOCK_OF) || basicBSONObject.get(HkSystemConstantUtil.PacketStaticFieldName.IN_STOCK_OF) == null) {
                if (lot != null) {
                    if (lot.getFieldValue() != null && lot.getFieldValue().containsField(HkSystemConstantUtil.LotStaticFieldName.IN_STOCK_OF)) {
                        List<String> inStockOf = (ArrayList<String>) lot.getFieldValue().get(HkSystemConstantUtil.LotStaticFieldName.IN_STOCK_OF);
                        basicBSONObject.put(HkSystemConstantUtil.PacketStaticFieldName.IN_STOCK_OF, inStockOf);
                    }

                }
            }
            if (basicBSONObject.containsField(HkSystemConstantUtil.PacketStaticFieldName.CARAT) && basicBSONObject.get(HkSystemConstantUtil.PacketStaticFieldName.CARAT) != null) {
                carat = (Double) basicBSONObject.get(HkSystemConstantUtil.PacketStaticFieldName.CARAT);
            }
            if (basicBSONObject.containsField(HkSystemConstantUtil.PacketStaticFieldName.PIECES) && basicBSONObject.get(HkSystemConstantUtil.PacketStaticFieldName.PIECES) != null) {
                pieces = (Long) basicBSONObject.get(HkSystemConstantUtil.PacketStaticFieldName.PIECES);
            }
            packetDocument.setFieldValue(basicBSONObject);
            // Call formula generic method first scenario
//              this.evaluateFormulaForFirstScenarioOfAddPacket(packetDocumentsToBeUpdated, totalformulaList, franchise, invoiceId, parcelId, lotId, featureId);
            Map<String, Double> finalFormulaEvalMap = hkFormulaExecution.mapOfEvaluatedFormulaValueWithDbField(totalformulaList, franchise, invoiceDocument, null, lot, packetDocument, null, packetDocument.getInvoice(), packetDocument.getParcel(), packetDocument.getLot(), featureId, HkSystemConstantUtil.FeatureForFormulaEvaluation.ADD_PACKET);
            if (finalFormulaEvalMap != null && !finalFormulaEvalMap.isEmpty()) {
                for (Map.Entry<String, Double> entrySet : finalFormulaEvalMap.entrySet()) {
                    packetDocument.getFieldValue().put(entrySet.getKey(), entrySet.getValue());
                }
            }

            mongoGenericDao.create(packetDocument);
            map = new HashMap<>();
            map.put(basicBSONObject.get(PacketFields.PACKET_SEQ_ID).toString(), packetDocument.getId().toString());
            //------------------Add To the list For Formula-----------------------
            packetDocumentsToBeUpdated.add(packetDocument);
            if (lot != null) {
                if (lot.getStockCarat() != null && carat != null) {
                    Double stockCarat = lot.getStockCarat();
                    Double newCaratValue = stockCarat - carat;
                    lot.setStockCarat(newCaratValue);
                }
                if (lot.getStockPieces() != null && pieces != null) {
                    Long stockPieces = lot.getStockPieces();
                    Long newPiecesValue = stockPieces - pieces;
                    lot.setStockPieces(newPiecesValue);
                }
                lot.setHasPacket(Boolean.TRUE);
                mongoGenericDao.update(lot);
            }
            //------------------Call Formula----------------------------
            return packetDocument;
        }
        return null;
    }

    @Override
    @MongoTransaction
    public List<String> splitPacket(ObjectId parentLot, ObjectId splitFrom, List<BasicBSONObject> basicBSONObjects, Long franchise, Long createdBy, Map<String, String> stockDbType) {
        List<String> packetNumberList = null;
        ObjectId invoiceId = null;
        ObjectId parcelId = null;
        List<ObjectId> splittedPackets = null;
//        System.out.println("---------------------in core----------splitPacket");
//        System.out.println("basicBSONObjects" + basicBSONObjects);
//        System.out.println("parentLot : " + parentLot);
//        System.out.println("splitFrom : " + splitFrom);
        if (!CollectionUtils.isEmpty(basicBSONObjects) && parentLot != null && splitFrom != null) {
            HkPacketDocument oldPacket = this.retrievePacketById(splitFrom, false);
            splittedPackets = new ArrayList<>();
            HkLotDocument lotDocument = (HkLotDocument) mongoGenericDao.retrieveById(parentLot, HkLotDocument.class);
            if (lotDocument != null) {
//                System.out.println("lotDocument found");
                packetNumberList = new ArrayList<>();
                invoiceId = lotDocument.getInvoice();
                parcelId = lotDocument.getParcel();
                BasicBSONObject oldPacketFieldValue = oldPacket.getFieldValue();
                Double remainingCarat = new Double(oldPacketFieldValue.getString(HkSystemConstantUtil.PacketStaticFieldName.CARAT));
                Long remainingPieces = new Long(oldPacketFieldValue.getString(HkSystemConstantUtil.PacketStaticFieldName.PIECES));
                for (BasicBSONObject basicBSONObject : basicBSONObjects) {
                    remainingCarat = remainingCarat - (Double) basicBSONObject.get(HkSystemConstantUtil.PacketStaticFieldName.CARAT);
                    remainingPieces = remainingPieces - (Long) basicBSONObject.get(HkSystemConstantUtil.PacketStaticFieldName.PIECES);
                    String packetNumber = null; //customFieldService.generateNextFieldSequence(HkSystemConstantUtil.AutoNumber.PACKET_ID, franchise);
//                    if (packetNumber != null) {
                    HkPacketDocument packetDocument = new HkPacketDocument();
                    packetDocument.setLot(parentLot);
                    if (parcelId != null) {
                        packetDocument.setParcel(parcelId);
                    }
                    if (invoiceId != null) {
                        packetDocument.setInvoice(invoiceId);
                    }
                    packetDocument.setFranchiseId(franchise);
                    packetDocument.setIsArchive(Boolean.FALSE);
                    packetDocument.setCreatedBy(createdBy);
                    packetDocument.setLastModifiedBy(createdBy);
                    packetDocument.setCreatedOn(new Date());
                    packetDocument.setLastModifiedOn(new Date());
                    packetDocument.setSplitFrom(splitFrom);
                    if (lotDocument != null && lotDocument.getHaveValue() != null) {
                        packetDocument.setHaveValue(lotDocument.getHaveValue());
                    }
                    packetDocument.setStatus(HkSystemConstantUtil.StockStatus.NEW_ROUGH);
                    // changed code by dmehta for copying all other values from old packet - 02/11/2015
                    packetNumberList.add(basicBSONObject.getString(HkSystemConstantUtil.PacketStaticFieldName.PACKET_ID));
                    Map toMap = oldPacketFieldValue.toMap();
                    toMap.putAll(basicBSONObject.toMap());
                    packetDocument.setFieldValue(new BasicBSONObject(toMap));
                    mongoGenericDao.create(packetDocument);
                    splittedPackets.add(packetDocument.getId());
//                    }
                }

                if (!CollectionUtils.isEmpty(splittedPackets)) {
//                    System.out.println("splittedPackets");
                    if (oldPacket != null) {
                        oldPacket.setSplitTo(splittedPackets);
//                        oldPacket.setStatus(HkSystemConstantUtil.StockStatus.SPLITTED);
//                        oldPacket.setIsArchive(Boolean.TRUE);
                        BasicBSONObject fieldValue = oldPacket.getFieldValue();
                        fieldValue.put(HkSystemConstantUtil.PacketStaticFieldName.CARAT, remainingCarat);
                        fieldValue.put(HkSystemConstantUtil.PacketStaticFieldName.PIECES, remainingPieces);
                        oldPacket.setFieldValue(fieldValue);
                        oldPacket.setLastModifiedBy(createdBy);
                        oldPacket.setLastModifiedOn(new Date());
                        mongoGenericDao.update(oldPacket);
                    }
                }
            }
        } else {
            return null;
        }
        return packetNumberList;
    }

    @Override
    public List<HkPacketDocument> retrievePacketsByIds(List<ObjectId> packetIds, Long franchise, Boolean isArchive, List<String> status, Boolean isConversionRequired) {

        List<HkPacketDocument> packetDocuments = null;
        List<HkPacketDocument> finalList = null;
        List<Criteria> criterias = new ArrayList<>();
        if (!CollectionUtils.isEmpty(packetIds)) {
            criterias.add(Criteria.where(PacketFields.PACKET_ID).in(packetIds));
        }
        if (franchise != null) {
            criterias.add(Criteria.where(PacketFields.FRANCHISE).is(franchise));
        }
        if (isArchive != null) {
            criterias.add(Criteria.where(PacketFields.IS_ARCHIVE).is(isArchive));
        }
        if (!StringUtils.isEmpty(status)) {
            criterias.add(Criteria.where(PacketFields.STATUS).in(status));

        }
        packetDocuments = mongoGenericDao.findByCriteria(criterias, HkPacketDocument.class
        );
        if (isConversionRequired) {
            if (!CollectionUtils.isEmpty(packetDocuments)) {
                finalList = new ArrayList<>();

                //Map<String, String> uiFieldListWithComponentType = customFieldService.retrieveUIFieldNameWithComponentTypes(uiFieldList);
                String pointerComponentType = HkSystemConstantUtil.CustomField.ComponentType.POINTER + "@*" + HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN;

                for (HkPacketDocument packetDocument : packetDocuments) {

                    HkPacketDocument temp = packetDocument;
                    Map<String, Object> customMap = packetDocument.getFieldValue().toMap();
                    Map<String, String> uiFieldMap = new HashMap<>();
                    if (!CollectionUtils.isEmpty(customMap)) {
                        for (Map.Entry<String, Object> custom : customMap.entrySet()) {
                            String[] split = custom.getKey().split("\\" + HkSystemConstantUtil.SEPERATOR_FOR_CUSTOM_FIELD_LABEL);
                            if (split != null && split.length >= 1) {
                                uiFieldMap.put(custom.getKey(), split[1]);
                            }
                        }
                    }
                    if (!CollectionUtils.isEmpty(uiFieldMap)) {
                        for (Map.Entry<String, String> field : uiFieldMap.entrySet()) {
                            if (field.getValue().equals(HkSystemConstantUtil.CustomField.COMPONENT_CODE_MAP.get(HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN)) || field.getValue().equals(HkSystemConstantUtil.CustomField.COMPONENT_CODE_MAP.get(HkSystemConstantUtil.CustomField.ComponentType.USER_MULTISELECT))) {
                                if (customMap != null && !customMap.isEmpty() && customMap.containsKey(field.getKey())) {
                                    String value = customMap.get(field.getKey()).toString().replaceAll("\\[", "").replaceAll("\\]", "");
                                    customMap.put(field.getKey(), value);
                                }
                            }
                        }
                        temp.getFieldValue().putAll(customMap);
                        finalList.add(temp);
                    }
                }

            }
            return finalList;
        } else {
            return packetDocuments;
        }

    }

    @Override
    public Boolean isPacketSequenceExist(Long sequenceNumber, Long franchise, ObjectId lotId) {
        Boolean packetExist = Boolean.FALSE;
        List<HkPacketDocument> hkPacketDocuments = new ArrayList<>();
        List<Criteria> criterias = new ArrayList<>();
        if (franchise != null) {
            criterias.add(Criteria.where(PacketFields.FRANCHISE).is(franchise));
        }
        if (lotId != null) {
            criterias.add(Criteria.where(PacketFields.LOT_ID).is(lotId));
        }
        if (sequenceNumber != null) {
            criterias.add(Criteria.where(PacketFields.SEQUENCE_NUMBER).is(sequenceNumber));

        }
        hkPacketDocuments = mongoGenericDao.findByCriteria(criterias, HkPacketDocument.class
        );
        if (!CollectionUtils.isEmpty(hkPacketDocuments)) {
            packetExist = Boolean.TRUE;
        }
        return packetExist;
    }

    @Override
    public Long getNextPacketSequence(Long franchise, ObjectId lotObjectId) {
        Long sequenceNumber = 0l;
        Query query = new Query();
        query.addCriteria(Criteria.where(PacketFields.FRANCHISE).is(franchise));
        query.addCriteria(Criteria.where(PacketFields.IS_ARCHIVE).is(Boolean.FALSE));
        query.addCriteria(Criteria.where(PacketFields.LOT_ID).is(lotObjectId));
        query.with(new Sort(Sort.Direction.DESC, PacketFields.SEQUENCE_NUMBER)).limit(1);
        HkPacketDocument hkPacketDocument = mongoGenericDao.getMongoTemplate().findOne(query, HkPacketDocument.class
        );
        if (hkPacketDocument
                != null && hkPacketDocument.getSequenceNumber()
                != null) {
            sequenceNumber = hkPacketDocument.getSequenceNumber();
        }
        sequenceNumber = sequenceNumber + 1l;
        return sequenceNumber;
    }

//------------------------------------------ Packet Service Ends---------------------------------------------//
//------------------------------------------ Allocation Service Starts -------------------------------------//
    @Override
    @MongoTransaction
    public Boolean manualStockAllocation(List<ObjectId> stockIds, BasicBSONObject basicBSONObject, boolean isPacket, Long franchise, long allotTo, List<ObjectId> workAllocationIds, long createdBy, Map<String, String> dbFieldWithFormulaMap, Map<String, HkFieldDocument> mapForFeatureInvolvedInFormula, Map<Long, String> featureIdWithNameMap
    ) {
        Boolean response = Boolean.FALSE;
        if (!CollectionUtils.isEmpty(stockIds)) {
            if (!isPacket) {
                //not packet, so fetch all the selected lots
                List<HkLotDocument> lots = this.retrieveLotsByIds(stockIds, franchise, Boolean.FALSE, Arrays.asList(HkSystemConstantUtil.StockStatus.IN_PRODUCTION), null, Boolean.FALSE);

// Make a list of lots which you want to send for update For Formula
                List<HkLotDocument> lotDocumentsForFormulaFirstScenario = null;

                if (!CollectionUtils.isEmpty(lots)) {

                    if (basicBSONObject != null) {
                        for (HkLotDocument hkLotDocument : lots) {
                            lotDocumentsForFormulaFirstScenario = new ArrayList<>();

                            //setting field values, here allot_to is must from web - I am not going to set it manually
                            BasicBSONObject fieldValue = hkLotDocument.getFieldValue();
                            if (fieldValue == null) {
                                fieldValue = new BasicBSONObject();
                            }
                            fieldValue.putAll(basicBSONObject.toMap());
                            if (fieldValue != null) {
                                hkLotDocument.setFieldValue(fieldValue);
                            }
                            //set last modified by and last modified on
                            hkLotDocument.setLastModifiedBy(createdBy);
                            hkLotDocument.setLastModifiedOn(new Date());

                            //update lot document
                            mongoGenericDao.update(hkLotDocument);
                            lotDocumentsForFormulaFirstScenario.add(hkLotDocument);

                        }
                    }

                }
                //----------------- Call For First Scenario where we evaluate same formula from server Side-------------------
                // Commented by Shifa on 19 October as this method has no usages
//                this.evaluateFirstScenarioForUpdateLot(dbFieldWithFormulaMap, franchise, lotDocumentsForFormulaFirstScenario,featureId);
                // -------------------Call formula Second scenario---------------------------------------
                HkStockServiceImpl hkStockServiceImpl = applicationContext.getBean(this.getClass());
                hkStockServiceImpl.evaluateSecondScenarioForUpdateLot(franchise, mapForFeatureInvolvedInFormula, featureIdWithNameMap, lotDocumentsForFormulaFirstScenario);
                //---------------------- Formula Second scenario ends here---------------------------------

            } else {

                List<HkPacketDocument> packets = this.retrievePacketsByIds(stockIds, franchise, Boolean.FALSE, Arrays.asList(HkSystemConstantUtil.StockStatus.IN_PRODUCTION), Boolean.FALSE);
                List<HkPacketDocument> packetDocumentsForFormulaScenarios = null;
                if (!CollectionUtils.isEmpty(packets)) {
                    packetDocumentsForFormulaScenarios = new ArrayList<>();
                    if (basicBSONObject != null) {
                        for (HkPacketDocument hkPacketDocument : packets) {

                            //setting field values, here allot_to is must from web - I am not going to set it manually
                            BasicBSONObject fieldValue = hkPacketDocument.getFieldValue();
                            if (fieldValue == null) {
                                fieldValue = new BasicBSONObject();
                            }
                            fieldValue.putAll(basicBSONObject.toMap());
                            if (fieldValue != null) {
                                hkPacketDocument.setFieldValue(fieldValue);
                            }
                            //set last modified by and last modified on
                            hkPacketDocument.setLastModifiedBy(createdBy);
                            hkPacketDocument.setLastModifiedOn(new Date());

                            //update packet document
                            mongoGenericDao.update(hkPacketDocument);
                            // Add packets which needs to be updated
                            packetDocumentsForFormulaScenarios.add(hkPacketDocument);

                        }
                        //-------------------------------Call for first scenario formula-----------------------
//                   // Commented by Shifa on 19 October as this method has no usages
//                        this.evaluateFirstScenarioForUpdatePacket(franchise, dbFieldWithFormulaMap, packetDocumentsForFormulaScenarios, featureId);
                        //---------------------- Call For Second Scenario-------------------------------------
                        HkStockServiceImpl hkStockServiceImpl = applicationContext.getBean(this.getClass());
                        hkStockServiceImpl.evaluateSecondScenarioForUpdatePacket(franchise, mapForFeatureInvolvedInFormula, featureIdWithNameMap, packetDocumentsForFormulaScenarios);
                    }
                }
            }
            response = Boolean.TRUE;
        }
        return response;
    }

    @Override
    public List<HkPersonCapabilityDocument> retrievePersonCapabilityWithBreakagePercentage(String propertyName, Long franchiseId) {
        if (propertyName != null && franchiseId != null) {
            String dbPropertyName = null;
            if (propertyName.equalsIgnoreCase("cut")) {
                dbPropertyName = HkSystemConstantUtil.PlanStaticFieldName.CUT;
            } else if (propertyName.equalsIgnoreCase("clarity")) {
                dbPropertyName = HkSystemConstantUtil.PlanStaticFieldName.CLARITY;
            } else if (propertyName.equalsIgnoreCase("color")) {
                dbPropertyName = HkSystemConstantUtil.PlanStaticFieldName.COLOR;
            } else if (propertyName.equalsIgnoreCase("carat")) {
                dbPropertyName = HkSystemConstantUtil.PlanStaticFieldName.CARAT;
            }
            if (dbPropertyName != null) {
                Query query = new Query();
                query.fields().include(HkPersonCapabilityField.FOR_PERSON);
                query.addCriteria(Criteria.where(HkPersonCapabilityField.FRANCHISE).is(franchiseId));
                query.addCriteria(Criteria.where(HkPersonCapabilityField.PROPERTY_NAME).is(dbPropertyName));
                query.addCriteria(Criteria.where(HkPersonCapabilityField.STATUS).is(HkSystemConstantUtil.ACTIVE));
                query.with(new Sort(Sort.Direction.DESC, HkPersonCapabilityField.SUCCESS_RATIO));
//                query.limit(4);

                List<HkPersonCapabilityDocument> personCapabilityDocuments = mongoGenericDao.getMongoTemplate().find(query, HkPersonCapabilityDocument.class
                );

                if (!CollectionUtils.isEmpty(personCapabilityDocuments)) {
                    Set<Long> userIds = new HashSet<>();
                    for (HkPersonCapabilityDocument document : personCapabilityDocuments) {
                        userIds.add(document.getForPerson());
                    }
                    List<Criteria> criterias = new ArrayList<>();

                    criterias.add(Criteria.where(HkPersonCapabilityField.STATUS).is(HkSystemConstantUtil.ACTIVE));
                    criterias.add(Criteria.where(HkPersonCapabilityField.FOR_PERSON).in(userIds));
                    criterias.add(Criteria.where(HkPersonCapabilityField.FRANCHISE).is(franchiseId));

                    personCapabilityDocuments = mongoGenericDao.findByCriteria(criterias, HkPersonCapabilityDocument.class);
                }
                return personCapabilityDocuments;
            }

        }
        return null;
    }

    //------------------------------------------ Allocation Service Ends -------------------------------------//
    //------------------------------------------ Change Status Service Starts--------------------------------//
    @Override
    @MongoTransaction
    public Boolean changeStatus(BasicBSONObject fieldValues, List<ObjectId> packetIds, List<ObjectId> lotIds, Date dueDate, String status, Long franchise, Long createdBy, Map<String, String> dbFieldWithFormulaMap, Map<String, HkFieldDocument> mapForFeatureInvolvedInFormula, Map<Long, String> featureIdWithNameMap) {
        Boolean response = Boolean.FALSE;
        Map<String, Object> newMap = new HashMap<>();
        if (fieldValues != null) {
            newMap = fieldValues.toMap();
        }

        if ((!CollectionUtils.isEmpty(packetIds) || !CollectionUtils.isEmpty(lotIds)) && !StringUtils.isEmpty(status)) {
            List<HkPacketDocument> packetDocumentsForFormulaScenarios = null;
            if (!CollectionUtils.isEmpty(packetIds)) {

                List<HkPacketDocument> packetDocuments = this.retrievePacketsByIds(packetIds, franchise, false, null, Boolean.FALSE);
                if (!CollectionUtils.isEmpty(packetDocuments)) {
                    packetDocumentsForFormulaScenarios = new ArrayList<>();
                    for (HkPacketDocument packetDocument : packetDocuments) {
                        packetDocument.setStatus(status);
                        packetDocument.setLastModifiedBy(createdBy);
                        packetDocument.setLastModifiedOn(new Date());
                        if (dueDate != null) {
                            BasicBSONObject fieldValue = packetDocument.getFieldValue();
                            if (fieldValue != null) {
                                fieldValue.put(HkSystemConstantUtil.PacketStaticFieldName.DUE_DATE, dueDate);
                            } else {
                                fieldValue = new BasicBSONObject();
                                fieldValue.put(HkSystemConstantUtil.PacketStaticFieldName.DUE_DATE, dueDate);
                            }
                        }
                        if (!status.equals(HkSystemConstantUtil.StockStatus.IN_PRODUCTION) && !status.equals(HkSystemConstantUtil.StockStatus.NEW_ROUGH)) {
                            packetDocument.setIssueDocument(null);
                        }
                        if (fieldValues != null) {
                            Map<String, Object> toMap = packetDocument.getFieldValue().toMap();
                            if (!CollectionUtils.isEmpty(toMap) && !CollectionUtils.isEmpty(newMap)) {
                                for (Map.Entry<String, Object> entry : toMap.entrySet()) {
                                    String key = entry.getKey();
                                    Object value = entry.getValue();
                                    if (newMap.containsKey(key)) {
                                        packetDocument.getFieldValue().remove(key);
                                    }
                                }
                            }
                            packetDocument.getFieldValue().putAll(newMap);
                        }
                        HkStatusHistoryDocument statusHistoryDocument = new HkStatusHistoryDocument();
                        statusHistoryDocument.setByUser(createdBy);
                        statusHistoryDocument.setOnDate(new Date());
                        statusHistoryDocument.setStatus(status);
                        List<HkStatusHistoryDocument> statusHistoryList = packetDocument.getStatusHistoryList();
                        if (!CollectionUtils.isEmpty(statusHistoryList)) {
                            statusHistoryList.add(statusHistoryDocument);
                            packetDocument.setStatusHistoryList(statusHistoryList);
                        } else {
                            statusHistoryList = new LinkedList<>();
                            statusHistoryList.add(statusHistoryDocument);
                            packetDocument.setStatusHistoryList(statusHistoryList);
                        }

                        mongoGenericDao.update(packetDocument);
                        // Add the packet Documents which needs to be updated
                        packetDocumentsForFormulaScenarios.add(packetDocument);

                    }
                    //-------------------------- Call First Scenario formula for packet
                    // Commented by Shifa on 19 October as this method has no usages
//                    this.evaluateFirstScenarioForUpdatePacket(franchise, dbFieldWithFormulaMap, packetDocumentsForFormulaScenarios, featureId);
                    //-------------------------- Call Second Scenario formula for Packet
                    HkStockServiceImpl hkStockServiceImpl = applicationContext.getBean(this.getClass());
                    hkStockServiceImpl.evaluateSecondScenarioForUpdatePacket(franchise, mapForFeatureInvolvedInFormula, featureIdWithNameMap, packetDocumentsForFormulaScenarios);
                }
            }
            List<HkLotDocument> lotDocumentsForFormulaFirstScenario = null;
            if (!CollectionUtils.isEmpty(lotIds)) {
                List<HkLotDocument> lotDocuments = this.retrieveLotsByIds(lotIds, franchise, false, null, null, Boolean.FALSE);
                if (!CollectionUtils.isEmpty(lotDocuments)) {
                    lotDocumentsForFormulaFirstScenario = new ArrayList<>();
                    for (HkLotDocument lotDocument : lotDocuments) {
                        lotDocument.setStatus(status);
                        lotDocument.setLastModifiedBy(createdBy);
                        lotDocument.setLastModifiedOn(new Date());
                        if (dueDate != null) {
                            BasicBSONObject fieldValue = lotDocument.getFieldValue();
                            if (fieldValue != null) {
                                fieldValue.put(HkSystemConstantUtil.LotStaticFieldName.DUE_DATE, dueDate);
                            } else {
                                fieldValue = new BasicBSONObject();
                                fieldValue.put(HkSystemConstantUtil.LotStaticFieldName.DUE_DATE, dueDate);
                            }
                            lotDocument.setFieldValue(fieldValue);
                        }
                        if (fieldValues != null) {
                            Map<String, Object> toMap = lotDocument.getFieldValue().toMap();
                            if (!CollectionUtils.isEmpty(toMap) && !CollectionUtils.isEmpty(newMap)) {
                                for (Map.Entry<String, Object> entry : toMap.entrySet()) {
                                    String key = entry.getKey();
                                    Object value = entry.getValue();
                                    if (newMap.containsKey(key)) {
                                        lotDocument.getFieldValue().remove(key);
                                    }
                                }
                            }
                            lotDocument.getFieldValue().putAll(newMap);
                        }
                        if (!status.equals(HkSystemConstantUtil.StockStatus.IN_PRODUCTION) && !status.equals(HkSystemConstantUtil.StockStatus.NEW_ROUGH)) {
                            lotDocument.setIssueDocument(null);
                        }
                        HkStatusHistoryDocument statusHistoryDocument = new HkStatusHistoryDocument();
                        statusHistoryDocument.setByUser(createdBy);
                        statusHistoryDocument.setOnDate(new Date());
                        statusHistoryDocument.setStatus(status);
                        List<HkStatusHistoryDocument> statusHistoryList = lotDocument.getStatusHistoryList();
                        if (!CollectionUtils.isEmpty(statusHistoryList)) {
                            statusHistoryList.add(statusHistoryDocument);
                            lotDocument.setStatusHistoryList(statusHistoryList);
                        } else {
                            statusHistoryList = new LinkedList<>();
                            statusHistoryList.add(statusHistoryDocument);
                            lotDocument.setStatusHistoryList(statusHistoryList);
                        }
                        mongoGenericDao.update(lotDocument);
                        // Add lots which has to be updated For Formula
                        lotDocumentsForFormulaFirstScenario.add(lotDocument);

                    }
                    //--------------------- Update First Scenario Formula------------------------------
                    // Commented by Shifa on 19 Ocober as this method ha sno occurences
//                    this.evaluateFirstScenarioForUpdateLot(dbFieldWithFormulaMap, franchise, lotDocumentsForFormulaFirstScenario,featureId);
                    //--------------------- Update Second Scenario Formula ----------------------------
                    HkStockServiceImpl hkStockServiceImpl = applicationContext.getBean(this.getClass());
                    hkStockServiceImpl.evaluateSecondScenarioForUpdateLot(franchise, mapForFeatureInvolvedInFormula, featureIdWithNameMap, lotDocumentsForFormulaFirstScenario);
                    // Code for second scenario ends here
                }
            }
            List<ObjectId> lotOrPacketIds = new ArrayList<>();
            if (!CollectionUtils.isEmpty(lotIds)) {
                lotOrPacketIds.addAll(lotIds);
            } else if (!CollectionUtils.isEmpty(packetIds)) {
                lotOrPacketIds.addAll(packetIds);
            }
//            if (!CollectionUtils.isEmpty(lotOrPacketIds)) {
//                List<HkWorkAllotmentDocument> workAllotmentDocuments = workAllotmentService.retrieveWorkAllotmentsForUserByStatus(null, HkSystemConstantUtil.ActivityServiceStatus.IN_PROCESS, null, franchise, lotOrPacketIds);
//                if (!status.equals(HkSystemConstantUtil.StockStatus.IN_PRODUCTION) && !status.equals(HkSystemConstantUtil.StockStatus.NEW_ROUGH)) {
//                    if (!CollectionUtils.isEmpty(workAllotmentDocuments)) {
//                        for (HkWorkAllotmentDocument workAllotmentDocument : workAllotmentDocuments) {
//                            workAllotmentDocument.setStatus(HkSystemConstantUtil.ActivityServiceStatus.TERMINATED);
//                            mongoGenericDao.update(workAllotmentDocument);
//                        }
//                    }
//                }
//            }
            response = Boolean.TRUE;
        }
        return response;
    }

    @Override
    public List retrieveLotsByCriteria(Map<String, Object> fieldValues, List<ObjectId> invoiceIds, List<ObjectId> parcelIds, List<ObjectId> lotIds, List<String> statusList, List<String> proposedStatusList, Long franchise, Boolean isArchive, Boolean isPacket
    ) {
        List lotOrPacketDocuments = null;
        List<String> newStatusAfterProposedStatusList = new ArrayList<>();
        List<Criteria> criterias = new ArrayList<>();
        Set<String> finalStatusSet = new HashSet<>();
        Map<String, String> uiFieldMap = this.createFieldNameWithComponentType(fieldValues);
        Map<String, Object> dateCriteriaMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(fieldValues)) {
            for (Map.Entry<String, Object> entry : fieldValues.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                if (uiFieldMap.containsKey(key)) {
                    String cType = uiFieldMap.get(key);
                    // Specific logic for Date type component
                    if (cType.equals(HkSystemConstantUtil.CustomField.ComponentType.DATE)) {
                        if (value != null) {
                            SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS\'Z\'");
                            formatDate.setTimeZone(TimeZone.getTimeZone("UTC"));
                            Date parseDate = null;
                            Date parseIncDate = null;
                            try {
                                // parseDate gives the formatted date
                                parseDate = formatDate.parse(value.toString());
                                Calendar c = Calendar.getInstance();
                                c.setTime(parseDate);
                                c.add(Calendar.DATE, 1);
                                // incremented date contains next date i.e the current date +1
//                                String incrementedDate = formatDate.format(c.getTime()); // new date
                                parseIncDate = new Date(c.getTimeInMillis());

                            } catch (ParseException ex) {
                                Logger.getLogger(HkStockServiceImpl.class
                                        .getName()).log(Level.SEVERE, null, ex);
                            }
                            if (!(key.startsWith("from") || key.startsWith("to"))) {
                                criterias.add(Criteria.where(ChangeStatusFields.FIELD_VALUE + "." + key).gt(parseDate).lt(parseIncDate));
                            } else {
                                if (key.startsWith("from")) {
                                    dateCriteriaMap.put(key, parseDate);
                                } else {
                                    dateCriteriaMap.put(key, parseIncDate);
                                }
                            }
                        }
                    } // Code added for handling image and file but since we are not showing image and file components in search it is of no use as for now
                    else if (cType.equals(HkSystemConstantUtil.CustomField.ComponentType.IMAGE) || cType.equals(HkSystemConstantUtil.CustomField.ComponentType.UPLOAD)) {
                        criterias.add(Criteria.where(ChangeStatusFields.FIELD_VALUE + "." + key).regex(value.toString()));
                    } else {
                        if (value instanceof Collection) {
                            Collection coll = (Collection) value;
                            if (!CollectionUtils.isEmpty(coll)) {
                                criterias.add(Criteria.where(ChangeStatusFields.FIELD_VALUE + "." + key).in(coll));
                            }
                        } else if (value instanceof Date) {
                            long date = Long.parseLong(value.toString());
                            criterias.add(Criteria.where(ChangeStatusFields.FIELD_VALUE + "." + key).is(new Date(date)));
                        } else {
                            if (value != null && value.toString().length() > 0) {
                                criterias.add(Criteria.where(ChangeStatusFields.FIELD_VALUE + "." + key).is(value));
                            }
                        }
                    }
                }

            }
        }
        if (!CollectionUtils.isEmpty(invoiceIds)) {
            criterias.add(Criteria.where(ChangeStatusFields.INVOICE_ID).in(invoiceIds));
        }
        if (!CollectionUtils.isEmpty(parcelIds)) {
            criterias.add(Criteria.where(ChangeStatusFields.PARCEL_ID).in(parcelIds));
        }
        if (!CollectionUtils.isEmpty(lotIds)) {
            criterias.add(Criteria.where(ChangeStatusFields.LOT_Id).in(lotIds));
        }
        if (!CollectionUtils.isEmpty(proposedStatusList)) {
            for (String proposedStatus : proposedStatusList) {
                if (!CollectionUtils.isEmpty(HkSystemConstantUtil.PROPOSED_STATUS_MAP)) {
                    for (Map.Entry<String, List<String>> entrySet : HkSystemConstantUtil.PROPOSED_STATUS_MAP.entrySet()) {
                        if (entrySet.getValue().contains(proposedStatus)) {
                            newStatusAfterProposedStatusList.add(entrySet.getKey());
                        }
                    }
                }
            }
        }
        if (!CollectionUtils.isEmpty(newStatusAfterProposedStatusList)) {
            finalStatusSet.addAll(newStatusAfterProposedStatusList);
        }
        if (!CollectionUtils.isEmpty(statusList)) {
            finalStatusSet.addAll(statusList);
        }
        if (!CollectionUtils.isEmpty(finalStatusSet)) {
            criterias.add(Criteria.where(ChangeStatusFields.STATUS).in(finalStatusSet));
        }
        if (franchise != null) {
            criterias.add(Criteria.where(ChangeStatusFields.FRANCHISE).is(franchise));
        }
        if (isArchive != null) {
            criterias.add(Criteria.where(ChangeStatusFields.IS_ARCHIVE).is(isArchive));
        }
        if (!CollectionUtils.isEmpty(dateCriteriaMap)) {
            Set<String> removedFields = new HashSet<>();
            Iterator<Map.Entry<String, Object>> itr = dateCriteriaMap.entrySet().iterator();
            while (itr.hasNext()) {
                Map.Entry<String, Object> entryMap = itr.next();
                String key = entryMap.getKey();
                Date value = (Date) entryMap.getValue();

                if (!removedFields.contains(key)) {
                    if (key.startsWith("from")) {
                        if (dateCriteriaMap.containsKey("to" + key.substring(4))) {
                            Date toValue = (Date) dateCriteriaMap.get("to" + key.substring(4));
                            criterias.add(Criteria.where(ChangeStatusFields.FIELD_VALUE + "." + key.substring(4)).gte(value).lt(toValue));
                            removedFields.add("to" + key.substring(4));
                            removedFields.add(key);
                        } else {
                            criterias.add(Criteria.where(ChangeStatusFields.FIELD_VALUE + "." + key.substring(4)).gte(value));
                            removedFields.add(key);
                        }
                    } else {
                        if (dateCriteriaMap.containsKey("from" + key.substring(2))) {
                            Date toValue = (Date) dateCriteriaMap.get("from" + key.substring(2));
                            criterias.add(Criteria.where(ChangeStatusFields.FIELD_VALUE + "." + key.substring(2)).gte(toValue).lt(value));
                            removedFields.add("from" + key.substring(2));
                            removedFields.add(key);
                        } else {
                            criterias.add(Criteria.where(ChangeStatusFields.FIELD_VALUE + "." + key.substring(2)).lt(value));
                            removedFields.add(key);

                        }
                    }
                }
            }
        }
        if (isPacket) {
            lotOrPacketDocuments = mongoGenericDao.findByCriteria(criterias, HkPacketDocument.class
            );
        } else {
            lotOrPacketDocuments = mongoGenericDao.findByCriteria(criterias, HkLotDocument.class
            );
        }
        return lotOrPacketDocuments;
    }

    //------------------------------------------ Change Status Service Ends----------------------------------//
    //------------------------------------------ EOD Srvice Starts ----------------------------------//
    @Override
    public List retrieveLotsOrPacketsForIssueForEod(BasicBSONObject bsonObject, Boolean isPacket, Long franchise
    ) {
        List<Criteria> criterias = new ArrayList<>();
        List lotOrPacketDocuments = new ArrayList<>();
        //adding criteria for checking in_stock_of from lot or packet documents by equating it to bson object value.
        if (bsonObject != null) {
            if (isPacket) {
                if (bsonObject.containsField(HkSystemConstantUtil.PacketStaticFieldName.IN_STOCK_OF)) {
                    ArrayList<String> inStockOfUser = (ArrayList<String>) bsonObject.get(HkSystemConstantUtil.PacketStaticFieldName.IN_STOCK_OF);
                    criterias.add(Criteria.where(EODFields.FIELD_VALUE + "." + HkSystemConstantUtil.PacketStaticFieldName.IN_STOCK_OF).in(inStockOfUser));
                }
            } else {
                if (bsonObject.containsField(HkSystemConstantUtil.LotStaticFieldName.IN_STOCK_OF)) {
                    ArrayList<String> inStockOfUser = (ArrayList<String>) bsonObject.get(HkSystemConstantUtil.LotStaticFieldName.IN_STOCK_OF);
                    criterias.add(Criteria.where(EODFields.FIELD_VALUE + "." + HkSystemConstantUtil.LotStaticFieldName.IN_STOCK_OF).in(inStockOfUser));
                }
            }

        }
        //adding criteria for getting lots or packets from the logged in franchise.
        if (franchise != null) {
            criterias.add(Criteria.where(EODFields.FRANCHISE).is(franchise));
        }
        //adding criteria for getting lots or packets of in_production status.
        criterias.add(Criteria.where(EODFields.STATUS).is(HkSystemConstantUtil.StockStatus.IN_PRODUCTION));

        //adding criteria for having issue documents null or not present in lot or packet documents.
        criterias.add(Criteria.where(EODFields.ISSUE_DOCUMENT).exists(false).orOperator(Criteria.where(EODFields.ISSUE_DOCUMENT).is(null)));

        //adding criteria for having eod issue documents null or not present in lot or packet documents.
        criterias.add(Criteria.where(EODFields.EOD_ISSUE_DOCUMENT).exists(false).orOperator(Criteria.where(EODFields.ISSUE_DOCUMENT).is(null)));

        //if isPacket flag true means retrieve packets from packet Documents else lots from lot documents.
        if (isPacket) {
            lotOrPacketDocuments = mongoGenericDao.findByCriteria(criterias, HkPacketDocument.class
            );
            return lotOrPacketDocuments;
        } else {
            criterias.add(Criteria.where(EODFields.HAS_PACKET).is(false));
            lotOrPacketDocuments
                    = mongoGenericDao.findByCriteria(criterias, HkLotDocument.class
                    );
            return lotOrPacketDocuments;
        }
    }

    @Override
    public List retrieveLotsOrPacketsForRecieveForEod(BasicBSONObject bsonObject, Boolean isPacket, Long franchise
    ) {
        List<Criteria> criterias = new ArrayList<>();
        List<HkIssueDocument> issueDocuments = new ArrayList<>();
        List lotOrPacketDocuments = new ArrayList<>();
        List<ObjectId> lotIds = new ArrayList<>();
        List<ObjectId> packetIds = new ArrayList<>();
        //adding criteria for checking in_stock_of_dept from issue documents by equating it to bson object value.
        if (bsonObject != null) {
            if (bsonObject.containsField(HkSystemConstantUtil.IssueStaticFieldName.IN_STOCK_OF_DEPT)) {
                ArrayList<String> inStockOfDept = (ArrayList<String>) bsonObject.get(HkSystemConstantUtil.IssueStaticFieldName.IN_STOCK_OF_DEPT);
                criterias.add(Criteria.where(EODFields.FIELD_VALUE + "." + HkSystemConstantUtil.IssueStaticFieldName.IN_STOCK_OF_DEPT).in(inStockOfDept));
            }
        }
        //adding criteria for getting issue documents of the logged in franchise.
        if (franchise != null) {
            criterias.add(Criteria.where(EODFields.FRANCHISE).is(franchise));
        }

        //adding criteria for getting issue documents of in_transit_safe status.
        criterias.add(Criteria.where(EODFields.STATUS).is(HkSystemConstantUtil.EODIssueStatus.IN_TRANSIT_SAFE));
        issueDocuments
                = mongoGenericDao.findByCriteria(criterias, HkIssueDocument.class
                );
        //getting lot ids and packetIds from the issue documents present.
        if (!CollectionUtils.isEmpty(issueDocuments)) {
            for (HkIssueDocument issueDocument : issueDocuments) {
                if (issueDocument.getLot() != null) {
                    lotIds.add(issueDocument.getLot());
                }
                if (issueDocument.getPacket() != null) {
                    packetIds.add(issueDocument.getPacket());
                }
            }
        }
        //isPacket flag is true the retrieve packet documents else retrieve lot documents.
        if (isPacket) {
            if (!CollectionUtils.isEmpty(packetIds)) {
                lotOrPacketDocuments = this.retrievePacketsByIds(packetIds, franchise, false, null, Boolean.FALSE);
            }
        } else {
            if (!CollectionUtils.isEmpty(lotIds)) {
                lotOrPacketDocuments = this.retrieveLotsByIds(lotIds, franchise, false, null, null, Boolean.FALSE);
            }
        }
        return lotOrPacketDocuments;
    }

    @Override
    public List retrieveLotsOrPacketsForReturnForEod(Long createdBy, Boolean isPacket, Long franchise
    ) {
        List<Criteria> criterias = new ArrayList<>();
        List<HkIssueDocument> issueDocuments = new ArrayList<>();
        List lotOrPacketDocuments = new ArrayList<>();
        List<ObjectId> lotIds = new ArrayList<>();
        List<ObjectId> packetIds = new ArrayList<>();
        //adding criteria for matching the created by field of issue document to the given created by value.
        if (createdBy != null) {
            criterias.add(Criteria.where(EODFields.CREATED_BY).is(createdBy));
        }
        //adding criteria for getting issue documents of the logged in franchise.
        if (franchise != null) {
            criterias.add(Criteria.where(EODFields.FRANCHISE).is(franchise));
        }
        //adding criteria for getting issue documents of in_transit_safe status.
        criterias.add(Criteria.where(EODFields.STATUS).is(HkSystemConstantUtil.EODIssueStatus.IN_TRANSIT_SAFE));
        issueDocuments
                = mongoGenericDao.findByCriteria(criterias, HkIssueDocument.class
                );
        //getting lot ids and packetIds from the issue documents present.
        if (!CollectionUtils.isEmpty(issueDocuments)) {
            for (HkIssueDocument issueDocument : issueDocuments) {
                if (issueDocument.getLot() != null) {
                    lotIds.add(issueDocument.getLot());
                }
                if (issueDocument.getPacket() != null) {
                    packetIds.add(issueDocument.getPacket());
                }
            }
        }
        //isPacket flag is true the retrieve packet documents else retrieve lot documents.
        if (isPacket) {
            if (!CollectionUtils.isEmpty(packetIds)) {
                lotOrPacketDocuments = this.retrievePacketsByIds(packetIds, franchise, false, null, Boolean.FALSE);
            }
        } else {
            if (!CollectionUtils.isEmpty(lotIds)) {
                lotOrPacketDocuments = this.retrieveLotsByIds(lotIds, franchise, false, null, null, Boolean.FALSE);
            }
        }
        return lotOrPacketDocuments;
    }

    @Override
    @MongoTransaction
    public Boolean issueLotsOrPacketsForEod(BasicBSONObject basicBSONObject, List<ObjectId> lotIds, List<ObjectId> packetIds, long issuedBy, Map<String, String> dbFieldWithFormulaMap, Long franchise
    ) {
        Boolean response = Boolean.FALSE;
        List<HkIssueDocument> issueDocumentsForFormulaScenarios = new ArrayList<>();
        if (!CollectionUtils.isEmpty(lotIds)) {

            List<HkLotDocument> lotDocuments = this.retrieveLotsByIds(lotIds, null, Boolean.FALSE, null, null, Boolean.FALSE);
            if (!CollectionUtils.isEmpty(lotDocuments)) {

                for (HkLotDocument hkLotDocument : lotDocuments) {
                    //creating EOD issue document and setting it's ref to lot also
                    HkIssueDocument issueDocument = new HkIssueDocument();
                    issueDocument.setCreatedBy(issuedBy);
                    issueDocument.setCreatedOn(new Date());
                    if (basicBSONObject != null) {
                        issueDocument.setFieldValue(basicBSONObject);
                    }
                    issueDocument.setInvoice(hkLotDocument.getInvoice());
                    issueDocument.setParcel(hkLotDocument.getParcel());
                    issueDocument.setStatus(HkSystemConstantUtil.EODIssueStatus.IN_TRANSIT_SAFE);
                    issueDocument.setLastModifiedBy(issuedBy);
                    issueDocument.setLastModifiedOn(new Date());
                    issueDocument.setLot(hkLotDocument.getId());
                    issueDocument.setFranchiseId(hkLotDocument.getFranchiseId());
                    //creating status history document for that issue document.
                    HkStatusHistoryDocument historyDocument = new HkStatusHistoryDocument();
                    historyDocument.setByUser(issuedBy);
                    historyDocument.setOnDate(new Date());
                    historyDocument.setStatus(HkSystemConstantUtil.EODIssueStatus.IN_TRANSIT_SAFE);
                    //making the list of status history document and adding the created status history document into it.
                    List<HkStatusHistoryDocument> historyDocuments = new ArrayList<>();
                    historyDocuments.add(historyDocument);
                    //Storing the list of status history documents into issue document.
                    issueDocument.setStatusHistoryList(historyDocuments);

                    mongoGenericDao.create(issueDocument);
                    issueDocumentsForFormulaScenarios.add(issueDocument);
                    //setting the newly created issue documents into lot document and updating it.
                    hkLotDocument.setEodIssueDocument(issueDocument);
                    mongoGenericDao.update(hkLotDocument);
                }
                response = Boolean.TRUE;
            }
        }
        // if packets issued changed their in stock of
        if (!CollectionUtils.isEmpty(packetIds)) {
            List<HkPacketDocument> packetDocuments = this.retrievePacketsByIds(packetIds, null, Boolean.FALSE, null, Boolean.FALSE);
            if (!CollectionUtils.isEmpty(packetDocuments)) {
                for (HkPacketDocument hkPacketDoc : packetDocuments) {

                    //creating EOD issue document and setting it's ref to lot also
                    HkIssueDocument issueDocument = new HkIssueDocument();
                    issueDocument.setCreatedBy(issuedBy);
                    issueDocument.setCreatedOn(new Date());
                    if (basicBSONObject != null) {
                        issueDocument.setFieldValue(basicBSONObject);
                    }
                    issueDocument.setInvoice(hkPacketDoc.getInvoice());
                    issueDocument.setParcel(hkPacketDoc.getParcel());
                    issueDocument.setStatus(HkSystemConstantUtil.EODIssueStatus.IN_TRANSIT_SAFE);
                    issueDocument.setLastModifiedBy(issuedBy);
                    issueDocument.setLastModifiedOn(new Date());
                    issueDocument.setLot(hkPacketDoc.getId());
                    issueDocument.setFranchiseId(hkPacketDoc.getFranchiseId());
                    //creating status history document for that issue document.
                    HkStatusHistoryDocument historyDocument = new HkStatusHistoryDocument();
                    historyDocument.setByUser(issuedBy);
                    historyDocument.setOnDate(new Date());
                    historyDocument.setStatus(HkSystemConstantUtil.EODIssueStatus.IN_TRANSIT_SAFE);
                    //making the list of status history document and adding the created status history document into it.
                    List<HkStatusHistoryDocument> historyDocuments = new ArrayList<>();
                    historyDocuments.add(historyDocument);
                    //Storing the list of status history documents into issue document.
                    issueDocument.setStatusHistoryList(historyDocuments);
                    mongoGenericDao.create(issueDocument);
                    issueDocumentsForFormulaScenarios.add(issueDocument);
                    //setting the newly created issue documents into lot document and updating it.
                    hkPacketDoc.setEodIssueDocument(issueDocument);
                    mongoGenericDao.update(hkPacketDoc);
                }
                response = Boolean.TRUE;
            }
        }
        //----------------------------Call FormulA-----------------------------------------
        return response;
    }

    @Override
    @MongoTransaction
    public void receiveLotsOrPacketsForEod(List<HkIssueDocument> issueDocuments, Long franchise
    ) {
        if (!CollectionUtils.isEmpty(issueDocuments)) {
            for (HkIssueDocument issueDocument : issueDocuments) {
                //here status will set as IN SAFE
                issueDocument.setStatus(HkSystemConstantUtil.EODIssueStatus.IN_SAFE);
                //also make and entry to status history table
                HkStatusHistoryDocument historyDocument = new HkStatusHistoryDocument();
                historyDocument.setByUser(issueDocument.getCreatedBy());
                historyDocument.setOnDate(new Date());
                historyDocument.setStatus(HkSystemConstantUtil.EODIssueStatus.IN_SAFE);
                //fetch the old status list and append new status to history
                List<HkStatusHistoryDocument> historyDocuments = issueDocument.getStatusHistoryList();
                if (CollectionUtils.isEmpty(historyDocuments)) {
                    historyDocuments = new ArrayList<>();
                }
                historyDocuments.add(historyDocument);
                //setting back the list of status history
                issueDocument.setStatusHistoryList(historyDocuments);
                //update issue document
                mongoGenericDao.update(issueDocument);
            }
        }
    }

    @Override
    @MongoTransaction
    public void returnLotsOrPacketsForEod(List<HkIssueDocument> issueDocuments, Long franchise
    ) {
        if (!CollectionUtils.isEmpty(issueDocuments)) {
            List<ObjectId> lotIds = new ArrayList<>();
            List<ObjectId> packetIds = new ArrayList<>();
            Long issuedBy = null;
            for (HkIssueDocument issueDocument : issueDocuments) {
                //getting issued To and issuedby whom fields
                issuedBy = issueDocument.getCreatedBy();
                //get ids of lots and packets
                if (issueDocument.getLot() != null) {
                    lotIds.add(issueDocument.getLot());
                }
                if (issueDocument.getPacket() != null) {
                    packetIds.add(issueDocument.getPacket());
                }
                //here status will set as RETURNED FROM SAFE
                issueDocument.setStatus(HkSystemConstantUtil.EODIssueStatus.RETURN_FROM_SAFE);
                //also make and entry to status history table
                HkStatusHistoryDocument historyDocument = new HkStatusHistoryDocument();
                historyDocument.setByUser(issuedBy);
                historyDocument.setOnDate(new Date());
                historyDocument.setStatus(HkSystemConstantUtil.EODIssueStatus.RETURN_FROM_SAFE);
                //fetch the old status list and append new status to history
                List<HkStatusHistoryDocument> historyDocuments = issueDocument.getStatusHistoryList();
                if (CollectionUtils.isEmpty(historyDocuments)) {
                    historyDocuments = new ArrayList<>();
                }
                historyDocuments.add(historyDocument);
                issueDocument.setStatusHistoryList(historyDocuments);
                mongoGenericDao.update(issueDocument);
            }

            // set EOD issue document refrence as null in lots/packets
            if (!CollectionUtils.isEmpty(lotIds)) {
                List<HkLotDocument> lotDocuments = this.retrieveLotsByIds(lotIds, null, Boolean.FALSE, null, null, Boolean.FALSE);
                if (!CollectionUtils.isEmpty(lotDocuments)) {
                    for (HkLotDocument hkLotDocument : lotDocuments) {
                        hkLotDocument.setEodIssueDocument(null);
                        mongoGenericDao.update(hkLotDocument);
                    }
                }
            }

            if (!CollectionUtils.isEmpty(packetIds)) {
                List<HkPacketDocument> packetDocumentsDocuments = this.retrievePacketsByIds(packetIds, null, Boolean.FALSE, null, Boolean.FALSE);
                if (!CollectionUtils.isEmpty(packetDocumentsDocuments)) {
                    for (HkPacketDocument packetDocument : packetDocumentsDocuments) {
                        packetDocument.setEodIssueDocument(null);
                        mongoGenericDao.update(packetDocument);
                    }
                }
            }

        }
    }

    //------------------------------------------ EOD Srvice Ends ----------------------------------//
    //------------------------------------------ Issue Srvice Ends ----------------------------------//
    @Override
    @MongoTransaction
    public ObjectId saveOrUpdateIssue(HkIssueDocument document) {
        mongoGenericDao.update(document);
        return document.getId();
    }

    @Override
    public HkIssueDocument
            retrieveIssueById(ObjectId issueId
            ) {
        HkIssueDocument document = (HkIssueDocument) mongoGenericDao.retrieveById(issueId, HkIssueDocument.class
        );
        return document;
    }

    @Override
    public List<HkIssueDocument> retrieveAllIssue(Long franchise, Boolean isActive
    ) {
        List<Criteria> criterias = new ArrayList<>();
        List<HkIssueDocument> documents = null;
        criterias.add(Criteria.where(IssueFields.FRANCHISE).is(franchise));
        if (isActive != null) {
            criterias.add(Criteria.where(IssueFields.IS_ARCHIVE).is(isActive));

        }
        documents = mongoGenericDao.findByCriteria(criterias, HkIssueDocument.class
        );
        return documents;
    }

    @Override
    @MongoTransaction
    public
            void deleteIssue(ObjectId issueId
            ) {
        HkIssueDocument document = (HkIssueDocument) mongoGenericDao.retrieveById(issueId, HkIssueDocument.class
        );
        document.setIsArchive(
                true);
        mongoGenericDao.update(document);
    }

    //------------------------------------------ Issue Srvice Ends ----------------------------------//
    //------------------------------------------ Plan Service Starts --------------------------------//
    @Override
    @MongoTransaction
    public void savePlanAccess(List<HkPlanAccessPermissionDocument> accessPermissionDocument
    ) {
        mongoGenericDao.createAll(accessPermissionDocument);
    }

    @Override
    public List<HkPlanAccessPermissionDocument> retrieveActivePlanAccessByNode(Long nodeId, Long franchise
    ) {
        List<Criteria> criterias = new ArrayList<>();
        List<HkPlanAccessPermissionDocument> documents = null;
        if (franchise != null) {
            criterias.add(Criteria.where(PlanFields.FRANCHISE).is(franchise));
        }
        if (nodeId != null) {
            criterias.add(Criteria.where(PlanFields.NODE_ID).is(nodeId));
        }
        criterias.add(Criteria.where(PlanFields.STATUS).is(HkSystemConstantUtil.ACTIVE));

        documents
                = mongoGenericDao.findByCriteria(criterias, HkPlanAccessPermissionDocument.class
                );
        return documents;
    }

    @Override
    @MongoTransaction
    public void deactivatePlanAccess(Long id, Long companyId
    ) {
        List<Criteria> criterias = new ArrayList<>();
        List<HkPlanAccessPermissionDocument> documents = null;
        criterias.add(Criteria.where(PlanFields.FRANCHISE).is(companyId));
        criterias.add(Criteria.where(PlanFields.NODE_ID).is(id));
        criterias.add(Criteria.where(PlanFields.STATUS).is(HkSystemConstantUtil.ACTIVE));

        documents
                = mongoGenericDao.findByCriteria(criterias, HkPlanAccessPermissionDocument.class
                );
        if (!CollectionUtils.isEmpty(criterias)) {
            for (HkPlanAccessPermissionDocument hkPlanAccessPermissionDocument : documents) {
                hkPlanAccessPermissionDocument.setStatus(HkSystemConstantUtil.ARCHIVED);
                mongoGenericDao.update(hkPlanAccessPermissionDocument);
            }

        }
    }

//-------------------------------- WRITE SERVICE / FINALIZE SERVICE METHODS --------------------------------------------
    @Override
    public List<HkPlanDocument> retrieveExistingPlanEntities(String planType, long nodeId, long franchise, ObjectId objectId, Boolean isPacket, Long loggedIn
    ) {
        List<HkPlanDocument> planDocuments = null;
        List<Criteria> criterias = new ArrayList<>();
        //type of plan i.e. estimate/plan/parameter
        if (planType != null && loggedIn != null && objectId != null && isPacket != null) {

            //retrive all the permissions for accessing plan
            List<HkPlanAccessPermissionDocument> accessPermissionDocuments = this.retrieveActivePlanAccessByNode(nodeId, franchise);

            if (!CollectionUtils.isEmpty(accessPermissionDocuments)) {
                Logger.getLogger(HkStockServiceImpl.class
                        .getName()).info("permission doc found");
                List<Criteria> dynamicCriterias = new ArrayList<>();
                List<Long> departmentIds = new ArrayList<>();
                List<Long> designationIds = new ArrayList<>();
                // fetch all the departments or designations if we found from plan access permissions
                for (HkPlanAccessPermissionDocument hkPlanAccessPermissionDocument : accessPermissionDocuments) {
                    List<String> accessToUsers = hkPlanAccessPermissionDocument.getAccessToUsers();
                    if (!CollectionUtils.isEmpty(accessToUsers)) {
                        for (String selectedUsers : accessToUsers) {
                            if (StringUtils.hasText(selectedUsers)) {
                                String[] split = selectedUsers.split(PlanFields.SEPARETOR);
                                if (split != null) {
                                    String type = split[1];
                                    if (StringUtils.hasText(type)) {
                                        if (type.equalsIgnoreCase(HkSystemConstantUtil.RecipientCodeType.DEPARTMENT)) {
                                            departmentIds.add(Long.parseLong(split[0]));
                                        }
                                        if (type.equalsIgnoreCase(HkSystemConstantUtil.RecipientCodeType.DESIGNATION)) {
                                            designationIds.add(Long.parseLong(split[0]));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                //maps for department wise users and designation wise users
                Map<Long, List<Long>> departmentUserMap = new HashMap<>();
                Map<Long, List<Long>> designationUserMap = new HashMap<>();

                //fill the map for department wise users if any department found in plan access permission
                if (!CollectionUtils.isEmpty(departmentIds)) {
                    List<SyncCenterUserDocument> centerUsers = syncCenterUserService.retrieveUsersByDepartmentIds(departmentIds, franchise);
//                    List<SyncCenterUserDocument> centerUsers = null;
                    if (!CollectionUtils.isEmpty(centerUsers)) {
                        Logger.getLogger(HkStockServiceImpl.class.getName()).info("center users found");
                        centerUsers.stream().forEach((syncCenterUserDocument) -> {
                            List<Long> userList;
                            if (!departmentUserMap.containsKey(syncCenterUserDocument.getDepartmentId())) {
                                userList = new ArrayList<>();
                                userList.add(syncCenterUserDocument.getId());
                            } else {
                                userList = departmentUserMap.get(syncCenterUserDocument.getDepartmentId());
                                userList.add(syncCenterUserDocument.getId());
                            }
                            departmentUserMap.put(syncCenterUserDocument.getDepartmentId(), userList);
                        });
                    } else {
                        Logger.getLogger(HkStockServiceImpl.class.getName()).info("center users not found");
                    }
                } else {
                    Logger.getLogger(HkStockServiceImpl.class.getName()).info("department ids not found");
                }

                //fill the map for designation wise users if any designation found in plan access permission
                if (!CollectionUtils.isEmpty(designationIds)) {
                    List<SyncCenterUserDocument> centerUsers = syncCenterUserService.retrieveUsersByRoleId(designationIds, null, franchise);
//                    List<SyncCenterUserDocument> centerUsers = null;
                    if (!CollectionUtils.isEmpty(centerUsers)) {
                        Logger.getLogger(HkStockServiceImpl.class.getName()).info("role found---center user found");
                        centerUsers.stream().forEach((syncCenterUserDocument) -> {
                            if (!CollectionUtils.isEmpty(syncCenterUserDocument.getRoleId())) {
                                List<Long> userList;
                                for (Long role : syncCenterUserDocument.getRoleId()) {
                                    if (role != null) {
                                        if (!designationUserMap.containsKey(role)) {
                                            userList = new ArrayList<>();
                                            userList.add(syncCenterUserDocument.getId());
                                        } else {
                                            userList = designationUserMap.get(role);
                                            userList.add(syncCenterUserDocument.getId());
                                        }
                                        designationUserMap.put(role, userList);
                                    }
                                }
                            }
                        });
                    } else {
                        Logger.getLogger(HkStockServiceImpl.class.getName()).info("role found---but center user not found");
                    }
                } else {
                    Logger.getLogger(HkStockServiceImpl.class.getName()).info("role not found");
                }

                for (HkPlanAccessPermissionDocument planAccessPermission : accessPermissionDocuments) {
                    List<String> accessToUsers = planAccessPermission.getAccessToUsers();
                    //first get the list of status
                    List<String> statusList = planAccessPermission.getAccessToStatuses();
                    List<Long> userList = new ArrayList<>();
                    if (!CollectionUtils.isEmpty(accessToUsers) && !CollectionUtils.isEmpty(statusList)) {

                        //second fill the user list for criteria
                        for (String selectedUsers : accessToUsers) {
                            if (StringUtils.hasText(selectedUsers)) {
                                String[] split = selectedUsers.split(PlanFields.SEPARETOR);
                                if (split != null) {
                                    String type = split[1];
                                    Long id = Long.parseLong(split[0]);

                                    if (StringUtils.hasText(type)) {
                                        if (type.equalsIgnoreCase(HkSystemConstantUtil.RecipientCodeType.EMPLOYEE)) {
                                            userList.add(id);
                                        }
                                        if (type.equalsIgnoreCase(HkSystemConstantUtil.RecipientCodeType.DEPARTMENT)) {
                                            if (!CollectionUtils.isEmpty(departmentUserMap)) {
                                                userList.addAll(departmentUserMap.get(id));
                                            }
                                        }
                                        if (type.equalsIgnoreCase(HkSystemConstantUtil.RecipientCodeType.DESIGNATION)) {
                                            if (!CollectionUtils.isEmpty(designationUserMap)) {
                                                userList.addAll(designationUserMap.get(id));
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        //now make criteria : userList and statusList matched
                        if (isPacket) {
                            dynamicCriterias.add(new Criteria().andOperator(
                                    Criteria.where(PlanFields.PLAN_TYPE).is(planType),
                                    Criteria.where(PlanFields.CREATED_BY).in(userList),
                                    Criteria.where(PlanFields.STATUS).in(statusList)));
//                                    Criteria.where(PACKET).is(objectId)));
                        } else {
                            dynamicCriterias.add(new Criteria().andOperator(
                                    Criteria.where(PlanFields.PLAN_TYPE).is(planType),
                                    Criteria.where(PlanFields.CREATED_BY).in(userList),
                                    Criteria.where(PlanFields.STATUS).in(statusList)));
//                                    Criteria.where(LOT).is(objectId)));
                        }

                    }
                }

                //put all the cretirias in oring
                if (!CollectionUtils.isEmpty(dynamicCriterias)) {
                    if (isPacket) {
                        dynamicCriterias.add(new Criteria().andOperator(
                                Criteria.where(PlanFields.PLAN_TYPE).is(planType),
                                Criteria.where(PlanFields.CREATED_BY).is(loggedIn),
                                Criteria.where(PlanFields.PACKET).is(objectId),
                                Criteria.where(PlanFields.STATUS).ne(HkSystemConstantUtil.ShowPlanStatus.ARCHIVED)));
                    } else {
                        dynamicCriterias.add(new Criteria().andOperator(
                                Criteria.where(PlanFields.PLAN_TYPE).is(planType),
                                Criteria.where(PlanFields.CREATED_BY).is(loggedIn),
                                Criteria.where(PlanFields.LOT).is(objectId),
                                Criteria.where(PlanFields.STATUS).ne(HkSystemConstantUtil.ShowPlanStatus.ARCHIVED)));
                    }
                    Criteria[] ctirteriaArray = new Criteria[dynamicCriterias.size()];
                    for (int index = 0; index < ctirteriaArray.length; index++) {
                        ctirteriaArray[index] = dynamicCriterias.get(index);
                    }
                    criterias.add(new Criteria().orOperator(ctirteriaArray));
                }

                planDocuments = mongoGenericDao.findByCriteria(criterias, HkPlanDocument.class);
            } else {
                Logger.getLogger(HkStockServiceImpl.class
                        .getName()).info("permision doc not found");
            }
        }
        return planDocuments;
    }

    @Override
    public Double calculatePriceFromPriceList(Long color, Long cut, Long clarity, Long fluorescence, Long caratId
    ) {
        Double price = 0d;
        if (color != null && cut != null && clarity != null && caratId != null) {
            HkPriceListDetailDocument priceListDetailDocument = new HkPriceListDetailDocument();
            priceListDetailDocument.setCaratRange(caratId);
            priceListDetailDocument.setClarity(clarity);
            priceListDetailDocument.setColor(color);
            priceListDetailDocument.setCut(cut);
            if (fluorescence != null) {
                priceListDetailDocument.setFluorescence(fluorescence);
            }
            List<Criteria> criterias = new ArrayList<>();
            HkPriceListDetailDocument matchedPriceListDetailDocument = null;
            criterias.add(Criteria.where(CarateRangeFields.STATUS).is(HkSystemConstantUtil.ACTIVE));
            List<HkPriceListDocument> priceListDocuments = mongoGenericDao.findByCriteria(criterias, HkPriceListDocument.class
            );
            if (!CollectionUtils.isEmpty(priceListDocuments)) {
                HkPriceListDocument priceListDocument = priceListDocuments.get(0);
                if (priceListDocument != null && !CollectionUtils.isEmpty(priceListDocument.getHkPriceListDetailEntityCollection())) {
                    for (HkPriceListDetailDocument hkPriceListDetailDocument : priceListDocument.getHkPriceListDetailEntityCollection()) {
                        if (hkPriceListDetailDocument.getClarity().equals(clarity)) {
                            if (hkPriceListDetailDocument.getColor().equals(color)) {
                                if (hkPriceListDetailDocument.getCut().equals(cut)) {
                                    if (hkPriceListDetailDocument.getCaratRange().equals(caratId)) {
                                        if (fluorescence != null && hkPriceListDetailDocument.getFluorescence() != null) {
                                            if (hkPriceListDetailDocument.getFluorescence().equals(fluorescence)) {
                                                matchedPriceListDetailDocument = hkPriceListDetailDocument.clone();
                                                break;
                                            }
                                        } else {
                                            matchedPriceListDetailDocument = hkPriceListDetailDocument.clone();
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                if (matchedPriceListDetailDocument != null) {
                    price = matchedPriceListDetailDocument.getHkgPrice();
                }
            }
        }
        return price;
    }

    @Override
    public Long retrieveCaratRangeFromCaratValue(Double caratValue) {
        Long carateRangeId = null;
        List<Criteria> criterias = new ArrayList<>();
        criterias.add(Criteria.where(CarateRangeFields.STATUS).is(HkSystemConstantUtil.ACTIVE));
        criterias.add(new Criteria().andOperator(
                Criteria.where(CarateRangeFields.MIN).lte(caratValue),
                Criteria.where(CarateRangeFields.MAX).gte(caratValue)));
        List<HkCaratRangeDocument> caratRangeDocuments = mongoGenericDao.findByCriteria(criterias, HkCaratRangeDocument.class
        );
        if (!CollectionUtils.isEmpty(caratRangeDocuments)) {
            HkCaratRangeDocument caratRangeDocument = caratRangeDocuments.get(0);
            carateRangeId = caratRangeDocument.getId();
        }
        return carateRangeId;
    }

//    @Override
//    //Remember to set hasMultiple = true
//    @MongoTransaction
//    public String savePlans(String planType, Map<String, BasicBSONObject> basicBSONObject, Long franchise, Long createdBy, ObjectId lotId, ObjectId packetId, Map<String, Double> priceMap, Map<String, String> currencyCodeMap, Map<String, String> dbFieldWithFormulaMap, String improveFrom) {
//        ObjectId planId = null;
//        List<HkPlanDocument> planDocumentsForFormulaScenarios = new ArrayList<>();
//
//        List<ObjectId> plans = new ArrayList<>();
//        if (StringUtils.hasText(planType) && (lotId != null || packetId != null)) {
//
//            if (!CollectionUtils.isEmpty(basicBSONObject)) {
//                String planID = customFieldService.generateNextFieldSequence(HkSystemConstantUtil.AutoNumber.PLAN_ID, franchise);
//                if (StringUtils.hasText(planID)) {
//                    if (basicBSONObject.containsKey("A")) {
//                        HkPlanDocument planDocument = new HkPlanDocument();
//                        //set plan type
//                        planDocument.setPlanType(planType);
//                        //necessary fields
//                        planDocument.setCreatedBy(createdBy);
//                        planDocument.setCreatedOn(new Date());
//                        planDocument.setLastModifiedBy(createdBy);
//                        planDocument.setLastModifiedOn(new Date());
//                        planDocument.setFranchiseId(franchise);
//                        //set status
//                        planDocument.setStatus(HkSystemConstantUtil.ShowPlanStatus.ENTERED);
////                        when plan gets improved
//                        if (StringUtils.hasLength(improveFrom) && StringUtils.hasText(improveFrom)) {
//                            LOGGER.info("improveFrom id " + improveFrom);
//                            planDocument.setImprovedFrom(new ObjectId(improveFrom));
//
//                        }
//                        //setting lot/parcel/packet/invoice according to lot or packet
//                        if (packetId != null) {
//                            HkPacketDocument packetDocument = (HkPacketDocument) mongoGenericDao.retrieveById(packetId, HkPacketDocument.class
//                            );
//                            if (packetDocument
//                                    != null) {
//                                planDocument.setInvoice(packetDocument.getInvoice());
//                                planDocument.setParcel(packetDocument.getParcel());
//                                planDocument.setLot(packetDocument.getLot());
//                                planDocument.setPacket(packetDocument.getId());
//                            }
//                        }
//
//                        if (lotId != null) {
//                            HkLotDocument lotDocument = (HkLotDocument) mongoGenericDao.retrieveById(lotId, HkLotDocument.class
//                            );
//                            if (lotDocument
//                                    != null) {
//                                planDocument.setInvoice(lotDocument.getInvoice());
//                                planDocument.setParcel(lotDocument.getParcel());
//                                planDocument.setLot(lotDocument.getId());
//                            }
//                        }
//
////set the price history list
//                        HkPriceHistoryDocument priceHistoryDocument = null;
//                        if (!CollectionUtils.isEmpty(priceMap) && !CollectionUtils.isEmpty(currencyCodeMap)) {
//                            priceHistoryDocument = new HkPriceHistoryDocument();
//                            priceHistoryDocument.setByUser(createdBy);
//                            if (!CollectionUtils.isEmpty(currencyCodeMap) && currencyCodeMap.containsKey("A")) {
//                                String currencyCode = currencyCodeMap.get("A");
//                                priceHistoryDocument.setCurrency(currencyCode);
//                            }
//                            priceHistoryDocument.setOnDate(new Date());
//                            if (priceMap.containsKey("A")) {
//                                Double price = priceMap.get("A");
//                                priceHistoryDocument.setPrice(price);
//                            }
//                            priceHistoryDocument.setStatus(HkSystemConstantUtil.ShowPlanStatus.ENTERED);
//                            List<HkPriceHistoryDocument> priceHistoryDocuments = new ArrayList<>();
//                            priceHistoryDocuments.add(priceHistoryDocument);
//                            planDocument.setHkPriceHistoryList(priceHistoryDocuments);
//                        }
//                        //set status history lisSTATUSt
//                        HkStatusHistoryDocument statusHistoryDocument = new HkStatusHistoryDocument();
//                        statusHistoryDocument.setByUser(createdBy);
//                        statusHistoryDocument.setOnDate(new Date());
//                        statusHistoryDocument.setStatus(HkSystemConstantUtil.ShowPlanStatus.ENTERED);
//                        List<HkStatusHistoryDocument> statusHistoryDocuments = new ArrayList<>();
//                        statusHistoryDocuments.add(statusHistoryDocument);
//                        planDocument.setStatusHistoryList(statusHistoryDocuments);
//                        //set the fieldVales
//                        BasicBSONObject bSONObject = basicBSONObject.get("A");
//                        if (bSONObject != null) {
//                            String planNumber = planID + "-" + "A";
//                            bSONObject.put(HkSystemConstantUtil.AutoNumber.PLAN_ID, planNumber);
//                            planDocument.setFieldValue(bSONObject);
//                            planDocument.setTag("A");
//
//                        }
//                        mongoGenericDao.create(planDocument);
//                        //---------Add list for formula
//                        planDocumentsForFormulaScenarios.add(planDocument);
//
//                        if (planDocument.getId() != null) {
//                            planId = planDocument.getId();
//                            for (Map.Entry<String, BasicBSONObject> entry : basicBSONObject.entrySet()) {
//                                String tag = entry.getKey();
//                                BasicBSONObject basicBSON = entry.getValue();
//                                if (!tag.equalsIgnoreCase("A")) {
//                                    HkPlanDocument plnDoc = planDocument.clone();
//                                    basicBSON.put(HkSystemConstantUtil.AutoNumber.PLAN_ID, planID + "-" + tag);
//                                    plnDoc.setFieldValue(basicBSON);
//                                    plnDoc.setReferencePlan(planId);
//                                    plnDoc.setTag(tag);
//                                    plnDoc.setFranchiseId(franchise);
//                                    HkPriceHistoryDocument priceHistoryDoc = new HkPriceHistoryDocument();
//                                    priceHistoryDoc.setByUser(createdBy);
//                                    if (currencyCodeMap.containsKey(tag)) {
//                                        String currencyCode = currencyCodeMap.get(tag);
//                                        priceHistoryDoc.setCurrency(currencyCode);
//                                    }
//                                    priceHistoryDoc.setOnDate(new Date());
//                                    if (priceMap.containsKey(tag)) {
//                                        Double price = priceMap.get(tag);
//                                        priceHistoryDoc.setPrice(price);
//                                    }
//                                    if (priceHistoryDocument != null) {
//                                        priceHistoryDocument.setOverrideValue(Boolean.FALSE);
//                                    }
//                                    priceHistoryDoc.setStatus(HkSystemConstantUtil.ShowPlanStatus.ENTERED);
//                                    List<HkPriceHistoryDocument> priceHistoryDocumentList = new ArrayList<>();
//                                    priceHistoryDocumentList.add(priceHistoryDoc);
//                                    planDocument.setHkPriceHistoryList(priceHistoryDocumentList);
//                                    //set status history lisSTATUSt
//                                    HkStatusHistoryDocument statusHistoryDoc = new HkStatusHistoryDocument();
//                                    statusHistoryDoc.setByUser(createdBy);
//                                    statusHistoryDoc.setOnDate(new Date());
//                                    statusHistoryDoc.setStatus(HkSystemConstantUtil.ShowPlanStatus.ENTERED);
//                                    List<HkStatusHistoryDocument> statusHistoryDocumentList = new ArrayList<>();
//                                    statusHistoryDocumentList.add(statusHistoryDoc);
//                                    planDocument.setStatusHistoryList(statusHistoryDocumentList);
//
//                                    mongoGenericDao.create(plnDoc);
//                                    planDocumentsForFormulaScenarios.add(plnDoc);
//
//                                    plans.add(plnDoc.getId());
//                                }
//                            }
//                        }
//                        plans.add(planId);
//
//                    } else {
//                        return null;
//                    }
//                } else {
//                    return null;
//                }
//            }
//            //setting field values
//
//            //---------------------------Call Formula---------------------------------------------------------------------
//            this.evaluateFormulaForFirstScenarioForPlan(franchise, dbFieldWithFormulaMap, planDocumentsForFormulaScenarios);
//        } else {
//            return null;
//        }
//        if (!CollectionUtils.isEmpty(plans)) {
//            return plans.toString();
//        } else {
//            return null;
//        }
//    }
    @Override
    public List<HkPlanDocument> retrieveEnteredPlansByLoggedInUsers(Long loggedInuser, Long franchise
    ) {
        List<HkPlanDocument> planDocuments = null;
        if (loggedInuser != null && franchise != null) {
            List<Criteria> criterias = new ArrayList<>();
            // retrieve all submitted and entered plans
            criterias.add(new Criteria().orOperator(Criteria.where(PlanFields.STATUS).is(HkSystemConstantUtil.ShowPlanStatus.ENTERED),
                    Criteria.where(PlanFields.STATUS).is(HkSystemConstantUtil.ShowPlanStatus.SUBMITTED)));
            //created by logged in users
            criterias.add(Criteria.where(PlanFields.CREATED_BY).is(loggedInuser));
            criterias.add(Criteria.where(PlanFields.FRANCHISE).is(franchise));
            planDocuments
                    = mongoGenericDao.findByCriteria(criterias, HkPlanDocument.class
                    );
        }
        return planDocuments;
    }

//    @Override
//    @MongoTransaction
//    public Boolean submitPlans(Long loggedInUser, List<ObjectId> planObjectId, List<ObjectId> workAllocationIds, Long franchise, ObjectId bestPlanID, Map<String, String> dbFieldWithFormulaMap
//    ) {
//        List<HkPlanDocument> planDocuments = null;
//        List<Criteria> criterias = new ArrayList<>();
//        List<HkPlanDocument> planDocumentsForFormulaScenarios = new ArrayList<>();
//        if (bestPlanID != null) {
//            HkPlanDocument bestPlanDocument = null;
//            LOGGER.info("bestPlanID" + bestPlanID);
//            bestPlanDocument
//                    = (HkPlanDocument) mongoGenericDao.retrieveById(bestPlanID, HkPlanDocument.class
//                    );
//            if (bestPlanDocument
//                    != null) {
//                LOGGER.info("bestPlanID doc found");
//                Map<String, Boolean> bestMap = new HashMap<>();
//                bestMap.put("##bestplanflag", Boolean.TRUE);
//                bestPlanDocument.getFieldValue().putAll(bestMap);
//                mongoGenericDao.update(bestPlanDocument);
//            }
//        }
//        if (!CollectionUtils.isEmpty(planObjectId)) {
//            criterias.add(Criteria.where(PlanFields.ID).in(planObjectId));
//            planDocuments
//                    = mongoGenericDao.findByCriteria(criterias, HkPlanDocument.class
//                    );
//        }
//        Boolean response = Boolean.FALSE;
//
//        if (!CollectionUtils.isEmpty(planDocuments) && !CollectionUtils.isEmpty(workAllocationIds) && franchise != null && loggedInUser != null) {
//            Logger.getLogger(HkStockServiceImpl.class
//                    .getName()).info("plan doc found");
////            planDocuments.stream().forEach((planDocument) ->
//            for (HkPlanDocument planDocument : planDocuments) {
//                planDocument.setLastModifiedOn(new Date());
//                planDocument.setStatus(HkSystemConstantUtil.ShowPlanStatus.SUBMITTED);
//                // setting price and status history on update
//                if (planDocument.getFieldValue() != null) {
//                    BasicBSONObject fieldValue = planDocument.getFieldValue();
//                    if (fieldValue.containsField(HkSystemConstantUtil.PlanStaticFieldName.CURRENCY_CODE) && fieldValue.containsField(HkSystemConstantUtil.PlanStaticFieldName.VALUE_OF_PLAN)) {
//
//                        String currencyCode = fieldValue.get(HkSystemConstantUtil.PlanStaticFieldName.CURRENCY_CODE).toString();
//                        Double price = ((Integer) fieldValue.get(HkSystemConstantUtil.PlanStaticFieldName.VALUE_OF_PLAN)).doubleValue();
//                        if (currencyCode != null && price != null) {
//                            HkPriceHistoryDocument priceHistoryDocument = new HkPriceHistoryDocument();
//                            priceHistoryDocument.setByUser(loggedInUser);
//                            if (StringUtils.hasText(currencyCode)) {
//                                priceHistoryDocument.setCurrency(currencyCode);
//                            }
//                            priceHistoryDocument.setOnDate(new Date());
//                            priceHistoryDocument.setPrice(price);
//                            priceHistoryDocument.setStatus(HkSystemConstantUtil.ShowPlanStatus.SUBMITTED);
//                            priceHistoryDocument.setOverrideValue(Boolean.FALSE);
//                            List<HkPriceHistoryDocument> priceHistoryDocuments;
//                            if (!CollectionUtils.isEmpty(planDocument.getHkPriceHistoryList())) {
//                                priceHistoryDocuments = planDocument.getHkPriceHistoryList();
//                            } else {
//                                priceHistoryDocuments = new ArrayList<>();
//                            }
//                            priceHistoryDocuments.add(priceHistoryDocument);
//                            planDocument.setHkPriceHistoryList(priceHistoryDocuments);
//                        }
//                    }
//                }
//                //set status history list
//                HkStatusHistoryDocument statusHistoryDocument = new HkStatusHistoryDocument();
//                statusHistoryDocument.setByUser(loggedInUser);
//                statusHistoryDocument.setOnDate(new Date());
//                statusHistoryDocument.setStatus(HkSystemConstantUtil.ShowPlanStatus.SUBMITTED);
//                List<HkStatusHistoryDocument> statusHistoryDocuments;
//                if (!CollectionUtils.isEmpty(planDocument.getStatusHistoryList())) {
//                    statusHistoryDocuments = planDocument.getStatusHistoryList();
//                } else {
//                    statusHistoryDocuments = new ArrayList<>();
//                }
//                statusHistoryDocuments.add(statusHistoryDocument);
//                planDocument.setStatusHistoryList(statusHistoryDocuments);
//
//                mongoGenericDao.update(planDocument);
//                planDocumentsForFormulaScenarios.add(planDocument);
//            }
//            List<HkWorkAllotmentDocument> workAllotmentDocuments = workAllotmentService.retrieveworkAllotmentsByIds(workAllocationIds, franchise);
//
//            if (!CollectionUtils.isEmpty(workAllotmentDocuments)) {
//                for (HkWorkAllotmentDocument hkWorkAllotmentDocument : workAllotmentDocuments) {
//                    hkWorkAllotmentDocument.setStatus(HkSystemConstantUtil.ActivityServiceStatus.PARTIAL_COMPLETED);
//                }
//                activityFlowService.routeActivityFlow(workAllotmentDocuments, loggedInUser);
//            }
//            response = Boolean.TRUE;
//        }
////------------------------Call Formula------------------------
//        this.evaluateFormulaForFirstScenarioForPlan(franchise, dbFieldWithFormulaMap, planDocumentsForFormulaScenarios);
//        return response;
//    }
    @Override
    public List<HkPlanDocument> retrievePlansByIds(List<ObjectId> planIds, Long franchise
    ) {
        List<HkPlanDocument> planDocuments = null;
        if (franchise != null) {
            List<Criteria> criterias = new ArrayList<>();

            if (!CollectionUtils.isEmpty(planIds)) {
                criterias.add(Criteria.where(PlanFields.ID).in(planIds));
            }
            criterias.add(Criteria.where(PlanFields.FRANCHISE).is(franchise));

            planDocuments
                    = mongoGenericDao.findByCriteria(criterias, HkPlanDocument.class
                    );
        }
        return planDocuments;
    }

    @Override
    public List<HkPlanDocument> retrievePlansByReferenceId(ObjectId refrenceId, Long franchise
    ) {
        List<HkPlanDocument> planDocuments = null;
        if (refrenceId != null && franchise != null) {
            List<Criteria> criterias = new ArrayList<>();
            criterias.add(Criteria.where(PlanFields.REFERENCE_ID).in(refrenceId));
            criterias.add(Criteria.where(PlanFields.FRANCHISE).is(franchise));

            planDocuments
                    = mongoGenericDao.findByCriteria(criterias, HkPlanDocument.class
                    );
        }
        return planDocuments;
    }

//    @Override
//    @MongoTransaction
//    public Boolean editPlans(ObjectId mainTag, Map<String, BasicBSONObject> oldTabData, List<ObjectId> deletePlanIds, Map<String, BasicBSONObject> newlyAddedTabs, Long franchise, Long updatedBy, Map<String, Double> priceMap, Map<String, String> currencyCodeMap, Map<String, String> dbFieldWithFormulaMap
//    ) {
//        Boolean editedPlans = Boolean.FALSE;
//        List<HkPlanDocument> planDocumentsForFormulaScenarios = new ArrayList<>();
//        if (franchise != null && updatedBy != null) {
//            if (!CollectionUtils.isEmpty(oldTabData)) {
//                LOGGER.info("oldTabData is not empty::", oldTabData);
//                List<ObjectId> planIds = new ArrayList<>();
//                for (Map.Entry<String, BasicBSONObject> entry : oldTabData.entrySet()) {
//                    planIds.add(new ObjectId(entry.getKey()));
//                }
//                List<HkPlanDocument> planDocuments = this.retrievePlansByIds(planIds, franchise);
//                for (HkPlanDocument hkPlanDocument : planDocuments) {
//                    LOGGER.info("plan doc found in edit plan");
//                    BasicBSONObject newMap = oldTabData.get(hkPlanDocument.getId().toString());
//                    hkPlanDocument.getFieldValue().putAll(newMap.toMap());
//                    hkPlanDocument.setLastModifiedBy(updatedBy);
//                    hkPlanDocument.setLastModifiedOn(new Date());
//
//                    mongoGenericDao.update(hkPlanDocument);
//                    planDocumentsForFormulaScenarios.add(hkPlanDocument);
//                }
//            } else {
//                LOGGER.info("oldTabData is empty");
//            }
//
//            if (!CollectionUtils.isEmpty(deletePlanIds)) {
//                LOGGER.info("deletePlanIds found");
//                List<HkPlanDocument> deletePlans = this.retrievePlansByIds(deletePlanIds, franchise);
//                if (!CollectionUtils.isEmpty(deletePlans)) {
//                    for (HkPlanDocument hkPlanDocument : deletePlans) {
//                        hkPlanDocument.setLastModifiedBy(updatedBy);
//                        hkPlanDocument.setLastModifiedOn(new Date());
//                        hkPlanDocument.setStatus(HkSystemConstantUtil.ShowPlanStatus.ARCHIVED);
//                        mongoGenericDao.update(hkPlanDocument);
//                    }
//                }
//            } else {
//                LOGGER.info("deletePlanIds nott found");
//
//            }
//            if (mainTag != null && !CollectionUtils.isEmpty(newlyAddedTabs) && !CollectionUtils.isEmpty(currencyCodeMap)
//                    && !CollectionUtils.isEmpty(priceMap)) {
//                HkPlanDocument mainDoc = (HkPlanDocument) mongoGenericDao.retrieveById(mainTag, HkPlanDocument.class
//                );
//                LOGGER.info(
//                        "mainDoc is:::", mainDoc);
//                if (mainDoc
//                        != null) {
//                    LOGGER.info("newlyAddedTabs:::", newlyAddedTabs);
//                    for (Map.Entry<String, BasicBSONObject> entry : newlyAddedTabs.entrySet()) {
//                        String tag = entry.getKey();
//                        BasicBSONObject bSONObject = entry.getValue();
//                        HkPlanDocument planDocument = new HkPlanDocument();
//                        //set plan type
//                        planDocument.setPlanType(mainDoc.getPlanType());
//                        //necessary fields
//                        planDocument.setCreatedBy(updatedBy);
//                        planDocument.setCreatedOn(new Date());
//                        planDocument.setLastModifiedBy(updatedBy);
//                        planDocument.setLastModifiedOn(new Date());
//                        planDocument.setFranchiseId(franchise);
//                        planDocument.setReferencePlan(mainTag);
//                        //set status
//                        planDocument.setStatus(HkSystemConstantUtil.ShowPlanStatus.ENTERED);
//                        //setting lot/parcel/packet/invoice according to lot or packet
//                        if (mainDoc.getInvoice() != null && mainDoc.getParcel() != null && mainDoc.getPacket() != null) {
//                            planDocument.setInvoice(mainDoc.getInvoice());
//                            planDocument.setParcel(mainDoc.getParcel());
//                            planDocument.setLot(mainDoc.getLot());
//                            if (mainDoc.getPacket() != null) {
//                                planDocument.setPacket(mainDoc.getPacket());
//                            }
//                        }
//                        //set the price history list
//                        HkPriceHistoryDocument priceHistoryDocument = new HkPriceHistoryDocument();
//                        priceHistoryDocument.setByUser(updatedBy);
//                        if (currencyCodeMap.containsKey(tag)) {
//                            String currencyCode = currencyCodeMap.get(tag);
//                            priceHistoryDocument.setCurrency(currencyCode);
//                        }
//                        priceHistoryDocument.setOnDate(new Date());
//                        if (priceMap.containsKey(tag)) {
//                            Double price = priceMap.get(tag);
//                            priceHistoryDocument.setPrice(price);
//                        }
//                        priceHistoryDocument.setStatus(HkSystemConstantUtil.ShowPlanStatus.ENTERED);
//                        priceHistoryDocument.setOverrideValue(Boolean.FALSE);
//                        List<HkPriceHistoryDocument> priceHistoryDocuments = new ArrayList<>();
//                        priceHistoryDocuments.add(priceHistoryDocument);
//                        planDocument.setHkPriceHistoryList(priceHistoryDocuments);
//                        //set status history lisSTATUSt
//                        HkStatusHistoryDocument statusHistoryDocument = new HkStatusHistoryDocument();
//                        statusHistoryDocument.setByUser(updatedBy);
//                        statusHistoryDocument.setOnDate(new Date());
//                        statusHistoryDocument.setStatus(HkSystemConstantUtil.ShowPlanStatus.ENTERED);
//                        List<HkStatusHistoryDocument> statusHistoryDocuments = new ArrayList<>();
//                        statusHistoryDocuments.add(statusHistoryDocument);
//                        planDocument.setStatusHistoryList(statusHistoryDocuments);
//                        //set the fieldVales
//                        if (bSONObject != null) {
//                            if (mainDoc.getFieldValue() != null) {
//                                BasicBSONObject fieldValue = mainDoc.getFieldValue();
//                                if (fieldValue.containsField(HkSystemConstantUtil.AutoNumber.PLAN_ID)) {
//                                    String planId = fieldValue.get(HkSystemConstantUtil.AutoNumber.PLAN_ID).toString();
//                                    String newPlanId = planId.substring(0, planId.length() - 2);
//                                    String planNumber = newPlanId + "-" + tag;
//                                    bSONObject.put(HkSystemConstantUtil.AutoNumber.PLAN_ID, planNumber);
//                                }
//                            }
//                            planDocument.setFieldValue(bSONObject);
//                            planDocument.setTag(tag);
//                        }
//                        LOGGER.info("Plan documnet saving::::", planDocument);
//
//                        mongoGenericDao.create(planDocument);
//                        planDocumentsForFormulaScenarios.add(planDocument);
//                    }
//                }
//
//            }
//            editedPlans = Boolean.TRUE;
//        }
//        // Call Formula First Scenario method
//        this.evaluateFormulaForFirstScenarioForPlan(franchise, dbFieldWithFormulaMap, planDocumentsForFormulaScenarios);
//        return editedPlans;
//    }
//    @Override
//    public HkPlanDocument retrievePlanByTypeByUser(String planType, Long forUser, Long franchise
//    ) {
//        HkPlanDocument planDocument = null;
//        if (StringUtils.hasText(planType) && forUser != null && franchise != null) {
//            List<Criteria> criterias = new ArrayList<>();
//            criterias.add(Criteria.where(PlanFields.PLAN_TYPE).is(planType));
//            criterias.add(Criteria.where(PlanFields.CREATED_BY).is(forUser));
//            criterias.add(Criteria.where(PlanFields.FRANCHISE).is(franchise));
//            List<HkPlanDocument> planDocuments = mongoGenericDao.findByCriteria(criterias, HkPlanDocument.class
//            );
//            if (!CollectionUtils.isEmpty(planDocuments)) {
//                planDocument = planDocuments.get(0);
//            }
//        }
//        return planDocument;
//    }
    @Override
    public List<HkPlanDocument> retrievePreviousFinalizedPlansForLotOrPacket(String status, ObjectId lot, ObjectId packet, String planType) {
        List<HkPlanDocument> planDocuments = null;
        if (status != null && (lot != null || packet != null) && StringUtils.hasText(planType)) {
            List<Criteria> criterias = new ArrayList<>();
            if (lot != null && packet == null) {
                criterias.add(new Criteria().andOperator(
                        Criteria.where(PlanFields.STATUS).is(status),
                        Criteria.where(PlanFields.PLAN_TYPE).is(planType),
                        Criteria.where(PlanFields.LOT).is(lot)));
            } else {
                criterias = new ArrayList<>();
                criterias.add(new Criteria().andOperator(
                        Criteria.where(PlanFields.STATUS).is(status),
                        Criteria.where(PlanFields.PLAN_TYPE).is(planType),
                        Criteria.where(PlanFields.PACKET).is(packet)));

            }
            planDocuments = mongoGenericDao.findByCriteria(criterias, HkPlanDocument.class
            );
        }
        return planDocuments;
    }

    //------------------------------------------ Sell Service Starts -------------------------------//
    @Override
    @MongoTransaction
    public String sellLot(BasicBSONObject basicBSONObject, Long franchise, Long createdBy, ObjectId lotId, ObjectId packetId, Map<String, String> dbFieldWithFormulaMap) {
        String sellNumber = null;
        HkLotDocument lotDocument = null;
        HkPacketDocument packetDocument = null;
        sellNumber = null; //customFieldService.generateNextFieldSequence(HkSystemConstantUtil.AutoNumber.SELL_ID, franchise);
        if (!StringUtils.isEmpty(sellNumber)) {
            HkSellDocument sellDocument = new HkSellDocument();
            sellDocument.setCreatedBy(createdBy);
            sellDocument.setCreatedOn(new Date());
            sellDocument.setLastModifiedBy(createdBy);
            sellDocument.setLastModifiedOn(new Date());
            sellDocument.setFranchiseId(franchise);

            if (lotId != null) {
                lotDocument = (HkLotDocument) mongoGenericDao.retrieveById(lotId, HkLotDocument.class
                );
            } else if (packetId != null) {
                packetDocument = this.retrievePacketById(packetId, true);
            }
            sellDocument.setStatus(HkSystemConstantUtil.StockStatus.SOLD);
            if (basicBSONObject != null) {
                basicBSONObject.put(HkSystemConstantUtil.AutoNumber.SELL_ID, sellNumber);
            } else {
                basicBSONObject = new BasicBSONObject();
                basicBSONObject.put(HkSystemConstantUtil.AutoNumber.SELL_ID, sellNumber);
            }
            sellDocument.setFieldValue(basicBSONObject);
            mongoGenericDao.create(sellDocument);
            HkSellDocument sellDocumentToBeUpdated = sellDocument;
            if (sellDocument.getId() != null && lotId != null && lotDocument != null) {
                lotDocument.setStatus(HkSystemConstantUtil.StockStatus.SOLD);
                lotDocument.setLastModifiedBy(createdBy);
                lotDocument.setLastModifiedOn(new Date());
                lotDocument.setIssueDocument(null);
                HkStatusHistoryDocument statusHistoryDocument = new HkStatusHistoryDocument();
                statusHistoryDocument.setByUser(createdBy);
                statusHistoryDocument.setOnDate(new Date());
                statusHistoryDocument.setStatus(HkSystemConstantUtil.StockStatus.SOLD);
                List<HkStatusHistoryDocument> statusHistoryList = lotDocument.getStatusHistoryList();
                if (!CollectionUtils.isEmpty(statusHistoryList)) {
                    statusHistoryList.add(statusHistoryDocument);
                    lotDocument.setStatusHistoryList(statusHistoryList);
                } else {
                    statusHistoryList = new LinkedList<>();
                    statusHistoryList.add(statusHistoryDocument);
                    lotDocument.setStatusHistoryList(statusHistoryList);
                }
                mongoGenericDao.update(lotDocument);

            } else if (sellDocument.getId() != null && packetId != null && packetDocument != null) {
                packetDocument.setStatus(HkSystemConstantUtil.StockStatus.SOLD);
                packetDocument.setLastModifiedBy(createdBy);
                packetDocument.setLastModifiedOn(new Date());
                packetDocument.setIssueDocument(null);

                HkStatusHistoryDocument statusHistoryDocument = new HkStatusHistoryDocument();
                statusHistoryDocument.setByUser(createdBy);
                statusHistoryDocument.setOnDate(new Date());
                statusHistoryDocument.setStatus(HkSystemConstantUtil.StockStatus.SOLD);
                List<HkStatusHistoryDocument> statusHistoryList = packetDocument.getStatusHistoryList();
                if (!CollectionUtils.isEmpty(statusHistoryList)) {
                    statusHistoryList.add(statusHistoryDocument);
                    packetDocument.setStatusHistoryList(statusHistoryList);
                } else {
                    statusHistoryList = new LinkedList<>();
                    statusHistoryList.add(statusHistoryDocument);
                    packetDocument.setStatusHistoryList(statusHistoryList);
                }
                mongoGenericDao.update(packetDocument);
            }
            //--------------------Call Formula Async method--------------

        }
        return sellNumber;
    }

    @Override
    @MongoTransaction
    public void sellParcel(HkSellDocument sellDocument, Long createdBy, Long franchise) {
        mongoGenericDao.update(sellDocument);
        if (sellDocument.getId() != null && !CollectionUtils.isEmpty(sellDocument.getParcels())) {
            List<HkParcelDocument> hkParcelDocuments = this.retrieveParcelsbyIds(sellDocument.getParcels());
            if (!CollectionUtils.isEmpty(hkParcelDocuments)) {
                for (HkParcelDocument hkParcelDocument : hkParcelDocuments) {
                    if (hkParcelDocument.getStockPieces() == 0l || hkParcelDocument.getStockCarat() == new Double(0)) {
                        hkParcelDocument.setStatus(HkSystemConstantUtil.StockStatus.SOLD);
                    } else {
                        hkParcelDocument.setStatus(HkSystemConstantUtil.StockStatus.PARTIALLY_SOLD);
                    }

                    hkParcelDocument.setLastModifiedBy(createdBy);
                    hkParcelDocument.setLastModifiedOn(new Date());
                    mongoGenericDao.update(hkParcelDocument);
                }
            }
            //-----TODO: when parcel gets sold need to add the entry in status history table....
        }
    }

    @Override
    public List<HkSellDocument> retrieveSellDocuments(Map<String, Object> fieldValues, List<ObjectId> parcelIds, Long franchiseId, Long createdBy) {
        List<HkSellDocument> sellDocuments = new ArrayList<>();
        List<Criteria> criterias = new ArrayList<>();
        Map<String, String> uiFieldMap = this.createFieldNameWithComponentType(fieldValues);
        Map<String, Object> dateCriteriaMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(fieldValues)) {
            for (Map.Entry<String, Object> entry : fieldValues.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                if (uiFieldMap.containsKey(key)) {
                    String cType = uiFieldMap.get(key);
                    // Specific logic for Date type component
                    if (cType.equals(HkSystemConstantUtil.CustomField.ComponentType.DATE)) {
                        if (value != null) {
                            SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS\'Z\'");
                            formatDate.setTimeZone(TimeZone.getTimeZone("UTC"));
                            Date parseDate = null;
                            Date parseIncDate = null;
                            try {
                                // parseDate gives the formatted date
                                parseDate = formatDate.parse(value.toString());
                                Calendar c = Calendar.getInstance();
                                c.setTime(parseDate);
                                c.add(Calendar.DATE, 1);
                                // incremented date contains next date i.e the current date +1
//                                String incrementedDate = formatDate.format(c.getTime()); // new date
                                parseIncDate = new Date(c.getTimeInMillis());

                            } catch (ParseException ex) {
                                Logger.getLogger(HkStockServiceImpl.class
                                        .getName()).log(Level.SEVERE, null, ex);
                            }
                            if (!(key.startsWith("from") || key.startsWith("to"))) {
                                criterias.add(Criteria.where(ChangeStatusFields.FIELD_VALUE + "." + key).gt(parseDate).lt(parseIncDate));
                            } else {
                                if (key.startsWith("from")) {
                                    dateCriteriaMap.put(key, parseDate);
                                } else {
                                    dateCriteriaMap.put(key, parseIncDate);
                                }
                            }
                        }
                    } // Code added for handling image and file but since we are not showing image and file components in search it is of no use as for now
                    else if (cType.equals(HkSystemConstantUtil.CustomField.ComponentType.IMAGE) || cType.equals(HkSystemConstantUtil.CustomField.ComponentType.UPLOAD)) {
                        criterias.add(Criteria.where(ChangeStatusFields.FIELD_VALUE + "." + key).regex(value.toString()));
                    } else {
                        if (value instanceof Collection) {
                            Collection coll = (Collection) value;
                            if (!CollectionUtils.isEmpty(coll)) {
                                criterias.add(Criteria.where(ChangeStatusFields.FIELD_VALUE + "." + key).in(coll));
                            }
                        } else if (value instanceof Date) {
                            long date = Long.parseLong(value.toString());
                            criterias.add(Criteria.where(ChangeStatusFields.FIELD_VALUE + "." + key).is(new Date(date)));
                        } else {
                            if (value != null && value.toString().length() > 0) {
                                criterias.add(Criteria.where(ChangeStatusFields.FIELD_VALUE + "." + key).is(value));
                            }
                        }
                    }
                }

            }
        }
        if (!CollectionUtils.isEmpty(parcelIds)) {
            criterias.add(Criteria.where(SellFields.PARCEL).in(parcelIds));
        }
        if (!CollectionUtils.isEmpty(dateCriteriaMap)) {
            Set<String> removedFields = new HashSet<>();
            Iterator<Map.Entry<String, Object>> itr = dateCriteriaMap.entrySet().iterator();
            while (itr.hasNext()) {
                Map.Entry<String, Object> entryMap = itr.next();
                String key = entryMap.getKey();
                Date value = (Date) entryMap.getValue();

                if (!removedFields.contains(key)) {
                    if (key.startsWith("from")) {
                        if (dateCriteriaMap.containsKey("to" + key.substring(4))) {
                            Date toValue = (Date) dateCriteriaMap.get("to" + key.substring(4));
                            criterias.add(Criteria.where(ChangeStatusFields.FIELD_VALUE + "." + key.substring(4)).gte(value).lt(toValue));
                            removedFields.add("to" + key.substring(4));
                            removedFields.add(key);
                        } else {
                            criterias.add(Criteria.where(ChangeStatusFields.FIELD_VALUE + "." + key.substring(4)).gte(value));
                            removedFields.add(key);
                        }
                    } else {
                        if (dateCriteriaMap.containsKey("from" + key.substring(2))) {
                            Date toValue = (Date) dateCriteriaMap.get("from" + key.substring(2));
                            criterias.add(Criteria.where(ChangeStatusFields.FIELD_VALUE + "." + key.substring(2)).gte(toValue).lt(value));
                            removedFields.add("from" + key.substring(2));
                            removedFields.add(key);
                        } else {
                            criterias.add(Criteria.where(ChangeStatusFields.FIELD_VALUE + "." + key.substring(2)).lt(value));
                            removedFields.add(key);
                        }
                    }
                }
            }
        }
        if (franchiseId != null) {
            criterias.add(Criteria.where(SellFields.FRANCHISE).is(franchiseId));
        }
        if (createdBy != null) {
            criterias.add(Criteria.where(SellFields.CREATED_BY).is(createdBy));

        }
        sellDocuments = mongoGenericDao.findByCriteria(criterias, HkSellDocument.class
        );
        return sellDocuments;
    }

    @MongoTransaction
    @Override
    public HkSellDocument
            retrieveSellDocumentById(ObjectId sellObjectId) {
        return (HkSellDocument) mongoGenericDao.retrieveById(sellObjectId, HkSellDocument.class
        );
    }

    @MongoTransaction
    @Override
    public ObjectId saveOrUpdateSellDocument(HkSellDocument hkSellDocument) {
        mongoGenericDao.update(hkSellDocument);
        return hkSellDocument.getId();
    }

    //------------------------------------------ Sell Service Ends ---------------------------------//
    //------------------------------------------ Sod Service Starts ---------------------------------//
    @Override
    public List retrieveLotsOrPacketsForIssueForSod(BasicBSONObject bsonObject, Boolean isPacket, Long franchise
    ) {
        List<Criteria> criterias = new ArrayList<>();
        List<HkIssueDocument> issueDocuments = new ArrayList<>();
        List lotOrPacketDocuments = new ArrayList<>();
        List<ObjectId> lotIds = new ArrayList<>();
        List<ObjectId> packetIds = new ArrayList<>();
        //adding criteria for checking in_stock_of_dept from issue documents by equating it to bson object value.
        if (bsonObject != null) {
            if (bsonObject.containsField(HkSystemConstantUtil.IssueStaticFieldName.IN_STOCK_OF_DEPT)) {
                ArrayList<String> inStockOfDept = (ArrayList<String>) bsonObject.get(HkSystemConstantUtil.IssueStaticFieldName.IN_STOCK_OF_DEPT);
                criterias.add(Criteria.where(SodFields.FIELD_VALUE + "." + HkSystemConstantUtil.IssueStaticFieldName.IN_STOCK_OF_DEPT).is(inStockOfDept));
            }
        }
        //adding criteria for getting issue documents of the logged in franchise.
        if (franchise != null) {
            criterias.add(Criteria.where(SodFields.FRANCHISE).is(franchise));
        }
        List<String> statusList = new ArrayList<>();
        statusList.add(HkSystemConstantUtil.EODIssueStatus.IN_SAFE);
        statusList.add(HkSystemConstantUtil.EODIssueStatus.RETURN_FROM_ISSUER);

        //adding criteria for getting issue documents of in_safe or return_from_safe status.
        criterias.add(Criteria.where(SodFields.STATUS).in(statusList));
        issueDocuments
                = mongoGenericDao.findByCriteria(criterias, HkIssueDocument.class
                );
        //getting lot ids and packetIds from the issue documents present.
        if (!CollectionUtils.isEmpty(issueDocuments)) {
            for (HkIssueDocument issueDocument : issueDocuments) {
                if (issueDocument.getLot() != null) {
                    lotIds.add(issueDocument.getLot());
                }
                if (issueDocument.getPacket() != null) {
                    packetIds.add(issueDocument.getPacket());
                }
            }
        }
        //isPacket flag is true the retrieve packet documents else retrieve lot documents.
        if (isPacket) {
            if (!CollectionUtils.isEmpty(packetIds)) {
                lotOrPacketDocuments = this.retrievePacketsByIds(packetIds, franchise, false, null, Boolean.FALSE);
            }
        } else {
            if (!CollectionUtils.isEmpty(lotIds)) {
                lotOrPacketDocuments = this.retrieveLotsByIds(lotIds, franchise, false, null, null, Boolean.FALSE);
            }
        }
        return lotOrPacketDocuments;
    }

    @Override
    public List retrieveLotsOrPacketsForRecieveForSod(BasicBSONObject bsonObject, Boolean isPacket, Long franchise
    ) {
        List<Criteria> criterias = new ArrayList<>();
        List<HkIssueDocument> issueDocuments = new ArrayList<>();
        List lotOrPacketDocuments = new ArrayList<>();
        List<ObjectId> lotIds = new ArrayList<>();
        List<ObjectId> packetIds = new ArrayList<>();
        //adding criteria for checking in_stock_of from issue documents by equating it to bson object value.
        if (bsonObject != null) {
            if (bsonObject.containsField(HkSystemConstantUtil.IssueStaticFieldName.IN_STOCK_OF)) {
                ArrayList<String> inStockOfUser = (ArrayList<String>) bsonObject.get(HkSystemConstantUtil.IssueStaticFieldName.IN_STOCK_OF);
                criterias.add(Criteria.where(SodFields.FIELD_VALUE + "." + HkSystemConstantUtil.IssueStaticFieldName.IN_STOCK_OF).is(inStockOfUser));
            }
        }
        //adding criteria for getting issue documents of the logged in franchise.
        if (franchise != null) {
            criterias.add(Criteria.where(SodFields.FRANCHISE).is(franchise));
        }

        //adding criteria for getting issue documents of in_transit_issuer status.
        criterias.add(Criteria.where(SodFields.STATUS).is(HkSystemConstantUtil.EODIssueStatus.IN_TRANSIT_ISSUER));
        issueDocuments
                = mongoGenericDao.findByCriteria(criterias, HkIssueDocument.class
                );
        //getting lot ids and packetIds from the issue documents present.
        if (!CollectionUtils.isEmpty(issueDocuments)) {
            for (HkIssueDocument issueDocument : issueDocuments) {
                if (issueDocument.getLot() != null) {
                    lotIds.add(issueDocument.getLot());
                }
                if (issueDocument.getPacket() != null) {
                    packetIds.add(issueDocument.getPacket());
                }
            }
        }
        //isPacket flag is true the retrieve packet documents else retrieve lot documents.
        if (isPacket) {
            if (!CollectionUtils.isEmpty(packetIds)) {
                lotOrPacketDocuments = this.retrievePacketsByIds(packetIds, franchise, false, null, Boolean.FALSE);
            }
        } else {
            if (!CollectionUtils.isEmpty(lotIds)) {
                lotOrPacketDocuments = this.retrieveLotsByIds(lotIds, franchise, false, null, null, Boolean.FALSE);
            }
        }
        return lotOrPacketDocuments;
    }

    @Override
    public List retrieveLotsOrPacketsForReturnForSod(BasicBSONObject bsonObject, Boolean isPacket, Long franchise
    ) {
        List<Criteria> criterias = new ArrayList<>();
        List<HkIssueDocument> issueDocuments = new ArrayList<>();
        List lotOrPacketDocuments = new ArrayList<>();
        List<ObjectId> lotIds = new ArrayList<>();
        List<ObjectId> packetIds = new ArrayList<>();
        //adding criteria for checking in_stock_of_dept from issue documents by equating it to bson object value.
        if (bsonObject != null) {
            if (bsonObject.containsField(HkSystemConstantUtil.IssueStaticFieldName.IN_STOCK_OF_DEPT)) {
                ArrayList<String> inStockOfDept = (ArrayList<String>) bsonObject.get(HkSystemConstantUtil.IssueStaticFieldName.IN_STOCK_OF_DEPT);
                criterias.add(Criteria.where(SodFields.FIELD_VALUE + "." + HkSystemConstantUtil.IssueStaticFieldName.IN_STOCK_OF_DEPT).is(inStockOfDept));
            }
        }
        //adding criteria for getting issue documents of the logged in franchise.
        if (franchise != null) {
            criterias.add(Criteria.where(SodFields.FRANCHISE).is(franchise));
        }

        //adding criteria for getting issue documents of in_transit_issuer status.
        criterias.add(Criteria.where(SodFields.STATUS).is(HkSystemConstantUtil.EODIssueStatus.IN_TRANSIT_ISSUER));
        issueDocuments
                = mongoGenericDao.findByCriteria(criterias, HkIssueDocument.class
                );
        //getting lot ids and packetIds from the issue documents present.
        if (!CollectionUtils.isEmpty(issueDocuments)) {
            for (HkIssueDocument issueDocument : issueDocuments) {
                if (issueDocument.getLot() != null) {
                    lotIds.add(issueDocument.getLot());
                }
                if (issueDocument.getPacket() != null) {
                    packetIds.add(issueDocument.getPacket());
                }
            }
        }
        //isPacket flag is true the retrieve packet documents else retrieve lot documents.
        if (isPacket) {
            if (!CollectionUtils.isEmpty(packetIds)) {
                lotOrPacketDocuments = this.retrievePacketsByIds(packetIds, franchise, false, null, Boolean.FALSE);
            }
        } else {
            if (!CollectionUtils.isEmpty(lotIds)) {
                lotOrPacketDocuments = this.retrieveLotsByIds(lotIds, franchise, false, null, null, Boolean.FALSE);
            }
        }
        return lotOrPacketDocuments;
    }

    @Override
    @MongoTransaction
    public Boolean issueLotsOrPacketsForSod(BasicBSONObject basicBSONObject, List<HkIssueDocument> issueDocuments, Map<String, String> dbFieldWithFormulaMap, Long franchise
    ) {
        Boolean response = Boolean.FALSE;
        List<HkIssueDocument> issueDocumentsForFormulaScenarios = null;
        if (!CollectionUtils.isEmpty(issueDocuments)) {
            issueDocumentsForFormulaScenarios = new ArrayList<>();
            for (HkIssueDocument issueDocument : issueDocuments) {
                //here status will set as IN TRANSIT ISSUER
                issueDocument.setStatus(HkSystemConstantUtil.EODIssueStatus.IN_TRANSIT_ISSUER);
                if (issueDocument.getFieldValue() != null) {
                    issueDocument.getFieldValue().toMap().putAll(basicBSONObject);
                }
                //also make and entry to status history table
                HkStatusHistoryDocument historyDocument = new HkStatusHistoryDocument();
                historyDocument.setByUser(issueDocument.getCreatedBy());
                historyDocument.setOnDate(new Date());
                historyDocument.setStatus(HkSystemConstantUtil.EODIssueStatus.IN_TRANSIT_ISSUER);
                //fetch the old status list and append new status to history
                List<HkStatusHistoryDocument> historyDocuments = issueDocument.getStatusHistoryList();
                if (CollectionUtils.isEmpty(historyDocuments)) {
                    historyDocuments = new ArrayList<>();
                }
                historyDocuments.add(historyDocument);
                //setting back the list of status history
                issueDocument.setLastModifiedOn(new Date());
                issueDocument.setStatusHistoryList(historyDocuments);
                //update issue document
                mongoGenericDao.update(issueDocument);
                // Create a list of issue Document which needs to be updated
                issueDocumentsForFormulaScenarios.add(issueDocument);
            }

            //-------------------------Call Formula Scenario-------------------------------------------------------
//            this.evaluateFormulaForFirstScenarioForIssue(franchise, dbFieldWithFormulaMap, issueDocumentsForFormulaScenarios);
            response = Boolean.TRUE;
        }
        return response;
    }

    @Override
    @MongoTransaction
    public void receiveLotsOrPacketsForSod(List<HkIssueDocument> issueDocuments, Long franchise
    ) {
        List<ObjectId> lotIds = new ArrayList<>();
        List<ObjectId> packetIds = new ArrayList<>();
        if (!CollectionUtils.isEmpty(issueDocuments)) {
            for (HkIssueDocument issueDocument : issueDocuments) {
                //here status will set as WITH ISSUER
                issueDocument.setStatus(HkSystemConstantUtil.EODIssueStatus.WITH_ISSUER);

                if (issueDocument.getLot() != null) {
                    lotIds.add(issueDocument.getLot());
                }
                if (issueDocument.getPacket() != null) {
                    packetIds.add(issueDocument.getPacket());
                }
                //also make and entry to status history table
                HkStatusHistoryDocument historyDocument = new HkStatusHistoryDocument();
                historyDocument.setByUser(issueDocument.getCreatedBy());
                historyDocument.setOnDate(new Date());
                historyDocument.setStatus(HkSystemConstantUtil.EODIssueStatus.WITH_ISSUER);
                //fetch the old status list and append new status to history
                List<HkStatusHistoryDocument> historyDocuments = issueDocument.getStatusHistoryList();
                if (CollectionUtils.isEmpty(historyDocuments)) {
                    historyDocuments = new ArrayList<>();
                }
                historyDocuments.add(historyDocument);
                //setting back the list of status history
                issueDocument.setStatusHistoryList(historyDocuments);
                //update issue document
                mongoGenericDao.update(issueDocument);
            }
            if (!CollectionUtils.isEmpty(lotIds)) {
                List<HkLotDocument> lotDocuments = this.retrieveLotsByIds(lotIds, null, Boolean.FALSE, null, null, Boolean.FALSE);
                if (!CollectionUtils.isEmpty(lotDocuments)) {
                    for (HkLotDocument hkLotDocument : lotDocuments) {
                        hkLotDocument.setEodIssueDocument(null);
                        mongoGenericDao.update(hkLotDocument);
                    }
                }
            }

            if (!CollectionUtils.isEmpty(packetIds)) {
                List<HkPacketDocument> packetDocumentsDocuments = this.retrievePacketsByIds(packetIds, null, Boolean.FALSE, null, Boolean.FALSE);
                if (!CollectionUtils.isEmpty(packetDocumentsDocuments)) {
                    for (HkPacketDocument packetDocument : packetDocumentsDocuments) {
                        packetDocument.setEodIssueDocument(null);
                        mongoGenericDao.update(packetDocument);
                    }
                }
            }
        }
    }

    @Override
    @MongoTransaction
    public void returnLotsOrPacketsForSod(List<HkIssueDocument> issueDocuments, Long franchise
    ) {
        if (!CollectionUtils.isEmpty(issueDocuments)) {
            Long issuedBy = null;
            for (HkIssueDocument issueDocument : issueDocuments) {
                //getting issued To and issuedby whom fields
                issuedBy = issueDocument.getCreatedBy();
                //here status will set as RETURNED FROM ISSUER
                issueDocument.setStatus(HkSystemConstantUtil.EODIssueStatus.RETURN_FROM_ISSUER);
                //also make and entry to status history table
                HkStatusHistoryDocument historyDocument = new HkStatusHistoryDocument();
                historyDocument.setByUser(issuedBy);
                historyDocument.setOnDate(new Date());
                historyDocument.setStatus(HkSystemConstantUtil.EODIssueStatus.RETURN_FROM_ISSUER);
                //fetch the old status list and append new status to history
                List<HkStatusHistoryDocument> historyDocuments = issueDocument.getStatusHistoryList();
                if (CollectionUtils.isEmpty(historyDocuments)) {
                    historyDocuments = new ArrayList<>();
                }
                historyDocuments.add(historyDocument);
                issueDocument.setStatusHistoryList(historyDocuments);
                mongoGenericDao.update(issueDocument);
            }
        }
    }

    //------------------------------------------ Sod Service Ends ---------------------------------//
    //------------------------------------------ Track Service Starts -----------------------------//
    @Override
    public List retrieveLotsOrPacketsByCriteria(Map<String, Object> fieldValues, List<ObjectId> invoiceIds, List<ObjectId> parcelIds, List<ObjectId> lotIds, Long franchise, Boolean isArchive, Boolean isPacket) {
        List lotOrPacketDocuments = null;
        List<Criteria> criterias = new ArrayList<>();
        Map<String, String> uiFieldMap = this.createFieldNameWithComponentType(fieldValues);
        Map<String, Object> dateCriteriaMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(fieldValues)) {
            for (Map.Entry<String, Object> entry : fieldValues.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                if (uiFieldMap.containsKey(key)) {
                    String cType = uiFieldMap.get(key);
                    // Specific logic for Date type component
                    if (cType.equals(HkSystemConstantUtil.CustomField.ComponentType.DATE)) {
                        if (value != null) {
                            SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS\'Z\'");
                            formatDate.setTimeZone(TimeZone.getTimeZone("UTC"));
                            Date parseDate = null;
                            Date parseIncDate = null;
                            try {
                                // parseDate gives the formatted date
                                parseDate = formatDate.parse(value.toString());
                                Calendar c = Calendar.getInstance();
                                c.setTime(parseDate);
                                c.add(Calendar.DATE, 1);
                                // incremented date contains next date i.e the current date +1
//                                String incrementedDate = formatDate.format(c.getTime()); // new date
                                parseIncDate = new Date(c.getTimeInMillis());

                            } catch (ParseException ex) {
                                Logger.getLogger(HkStockServiceImpl.class
                                        .getName()).log(Level.SEVERE, null, ex);
                            }
                            if (!(key.startsWith("from") || key.startsWith("to"))) {
                                criterias.add(Criteria.where(TrackFields.FIELD_VALUE + "." + key).gt(parseDate).lt(parseIncDate));
                            } else {
                                if (key.startsWith("from")) {
                                    dateCriteriaMap.put(key, parseDate);
                                } else {
                                    dateCriteriaMap.put(key, parseIncDate);
                                }
                            }
                        }
                    } // Code added for handling image and file but since we are not showing image and file components in search it is of no use as for now
                    else if (cType.equals(HkSystemConstantUtil.CustomField.ComponentType.IMAGE) || cType.equals(HkSystemConstantUtil.CustomField.ComponentType.UPLOAD)) {
                        criterias.add(Criteria.where(TrackFields.FIELD_VALUE + "." + key).regex(value.toString()));
                    } else {
                        if (value instanceof Collection) {
                            Collection coll = (Collection) value;
                            if (!CollectionUtils.isEmpty(coll)) {
                                criterias.add(Criteria.where(TrackFields.FIELD_VALUE + "." + key).in(coll));
                            }
                        } else if (value instanceof Date) {
                            long date = Long.parseLong(value.toString());
                            criterias.add(Criteria.where(TrackFields.FIELD_VALUE + "." + key).is(new Date(date)));
                        } else {
                            if (value != null && value.toString().length() > 0) {
                                criterias.add(Criteria.where(TrackFields.FIELD_VALUE + "." + key).is(value));
                            }
                        }
                    }
                }

            }
        }
        if (!CollectionUtils.isEmpty(invoiceIds)) {
            criterias.add(Criteria.where(TrackFields.INVOICE_ID).in(invoiceIds));
        }
        if (!CollectionUtils.isEmpty(parcelIds)) {
            criterias.add(Criteria.where(TrackFields.PARCEL_ID).in(parcelIds));
        }
        if (!CollectionUtils.isEmpty(lotIds)) {
            criterias.add(Criteria.where(TrackFields.LOT_Id).in(lotIds));
        }
        if (franchise != null) {
            criterias.add(Criteria.where(TrackFields.FRANCHISE).is(franchise));
        }
        if (isArchive != null) {
            criterias.add(Criteria.where(TrackFields.IS_ARCHIVE).is(isArchive));

        }
        if (!CollectionUtils.isEmpty(dateCriteriaMap)) {
            Set<String> removedFields = new HashSet<>();
            Iterator<Map.Entry<String, Object>> itr = dateCriteriaMap.entrySet().iterator();
            while (itr.hasNext()) {
                Map.Entry<String, Object> entryMap = itr.next();
                String key = entryMap.getKey();
                Date value = (Date) entryMap.getValue();

                if (!removedFields.contains(key)) {
                    if (key.startsWith("from")) {
                        if (dateCriteriaMap.containsKey("to" + key.substring(4))) {
                            Date toValue = (Date) dateCriteriaMap.get("to" + key.substring(4));
                            criterias.add(Criteria.where(TrackFields.FIELD_VALUE + "." + key.substring(4)).gte(value).lt(toValue));
                            removedFields.add("to" + key.substring(4));
                            removedFields.add(key);
                        } else {
                            criterias.add(Criteria.where(TrackFields.FIELD_VALUE + "." + key.substring(4)).gte(value));
                            removedFields.add(key);
                        }
                    } else {
                        if (dateCriteriaMap.containsKey("from" + key.substring(2))) {
                            Date toValue = (Date) dateCriteriaMap.get("from" + key.substring(2));
                            criterias.add(Criteria.where(TrackFields.FIELD_VALUE + "." + key.substring(2)).gte(toValue).lt(value));
                            removedFields.add("from" + key.substring(2));
                            removedFields.add(key);
                        } else {
                            criterias.add(Criteria.where(TrackFields.FIELD_VALUE + "." + key.substring(2)).lt(value));
                            removedFields.add(key);

                        }
                    }
                }
            }
        }
        if (isPacket) {
            lotOrPacketDocuments = mongoGenericDao.findByCriteria(criterias, HkPacketDocument.class
            );
        } else {
            lotOrPacketDocuments = mongoGenericDao.findByCriteria(criterias, HkLotDocument.class
            );
        }
        return lotOrPacketDocuments;
    }

    //------------------------------------------ Track Service Ends -------------------------------//
    //------------------------------------------ Transfer Service Starts ---------------------------------//
    @Override
    @MongoTransaction
    public String transferLot(BasicBSONObject basicBSONObject, Long franchise, Long createdBy, ObjectId lotId, ObjectId packetId, Map<String, String> dbFieldWithFormulaMap
    ) {
        String transferNumber = null;
        HkLotDocument lotDocument = null;
        HkPacketDocument packetDocument = null;
        transferNumber = null; //customFieldService.generateNextFieldSequence(HkSystemConstantUtil.AutoNumber.TRANSFER_ID, franchise);
        if (!StringUtils.isEmpty(transferNumber)) {

            HkTransferDocument tranferDocument = new HkTransferDocument();
            tranferDocument.setCreatedBy(createdBy);
            tranferDocument.setCreatedOn(new Date());
            tranferDocument.setLastModifiedBy(createdBy);
            tranferDocument.setLastModifiedOn(new Date());
            tranferDocument.setFranchiseId(franchise);

            if (lotId != null) {
                lotDocument = (HkLotDocument) mongoGenericDao.retrieveById(lotId, HkLotDocument.class
                );
                if (lotDocument
                        != null) {
                    ObjectId invoice = lotDocument.getInvoice();
                    ObjectId parcel = lotDocument.getParcel();
                    if (invoice != null) {
                        tranferDocument.setInvoice(invoice);
                    }
                    if (parcel != null) {
                        tranferDocument.setParcel(parcel);
                    }
                    tranferDocument.setLot(lotId);
                }
            } else if (packetId != null) {
                packetDocument = this.retrievePacketById(packetId, true);
                if (packetDocument != null) {
                    ObjectId invoice = packetDocument.getInvoice();
                    ObjectId parcel = packetDocument.getParcel();
                    if (invoice != null) {
                        tranferDocument.setInvoice(invoice);
                    }
                    if (parcel != null) {
                        tranferDocument.setParcel(parcel);
                    }
                    tranferDocument.setPacket(packetId);
                }
            }
            tranferDocument.setStatus(HkSystemConstantUtil.StockStatus.TRANSFERRED);
            if (basicBSONObject != null) {
                basicBSONObject.put(HkSystemConstantUtil.AutoNumber.TRANSFER_ID, transferNumber);
            } else {
                basicBSONObject = new BasicBSONObject();
                basicBSONObject.put(HkSystemConstantUtil.AutoNumber.TRANSFER_ID, transferNumber);
            }
            tranferDocument.setFieldValue(basicBSONObject);
            mongoGenericDao.create(tranferDocument);
            HkTransferDocument transferDocumentToBeUpdated = tranferDocument;
            if (tranferDocument.getId() != null && lotId != null && lotDocument != null) {
                lotDocument.setStatus(HkSystemConstantUtil.StockStatus.TRANSFERRED);
                lotDocument.setLastModifiedBy(createdBy);
                lotDocument.setLastModifiedOn(new Date());
                lotDocument.setIssueDocument(null);
                HkStatusHistoryDocument statusHistoryDocument = new HkStatusHistoryDocument();
                statusHistoryDocument.setByUser(createdBy);
                statusHistoryDocument.setOnDate(new Date());
                statusHistoryDocument.setStatus(HkSystemConstantUtil.StockStatus.TRANSFERRED);
                List<HkStatusHistoryDocument> statusHistoryList = lotDocument.getStatusHistoryList();
                if (!CollectionUtils.isEmpty(statusHistoryList)) {
                    statusHistoryList.add(statusHistoryDocument);
                    lotDocument.setStatusHistoryList(statusHistoryList);
                } else {
                    statusHistoryList = new LinkedList<>();
                    statusHistoryList.add(statusHistoryDocument);
                    lotDocument.setStatusHistoryList(statusHistoryList);
                }
                mongoGenericDao.update(lotDocument);
            } else if (tranferDocument.getId() != null && packetId != null && packetDocument != null) {
                packetDocument.setStatus(HkSystemConstantUtil.StockStatus.TRANSFERRED);
                packetDocument.setLastModifiedBy(createdBy);
                packetDocument.setLastModifiedOn(new Date());
                packetDocument.setIssueDocument(null);
                HkStatusHistoryDocument statusHistoryDocument = new HkStatusHistoryDocument();
                statusHistoryDocument.setByUser(createdBy);
                statusHistoryDocument.setOnDate(new Date());
                statusHistoryDocument.setStatus(HkSystemConstantUtil.StockStatus.TRANSFERRED);
                List<HkStatusHistoryDocument> statusHistoryList = packetDocument.getStatusHistoryList();
                if (!CollectionUtils.isEmpty(statusHistoryList)) {
                    statusHistoryList.add(statusHistoryDocument);
                    packetDocument.setStatusHistoryList(statusHistoryList);
                } else {
                    statusHistoryList = new LinkedList<>();
                    statusHistoryList.add(statusHistoryDocument);
                    packetDocument.setStatusHistoryList(statusHistoryList);
                }
                mongoGenericDao.update(packetDocument);
            }
            //------------------Call Formula Async method
        }
        return transferNumber;
    }

    public List<HkTransferDocument> retrieveTransferDocuments(Map<String, Object> fieldValues, List<ObjectId> invoiceIds, List<ObjectId> parcelIds, List<ObjectId> lotIds, List<ObjectId> packetIds) {
        List<HkTransferDocument> hkTransferDocuments = new ArrayList<>();
        List<Criteria> criterias = new ArrayList<>();
        Map<String, String> uiFieldMap = this.createFieldNameWithComponentType(fieldValues);
        Map<String, Object> dateCriteriaMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(fieldValues)) {
            for (Map.Entry<String, Object> entry : fieldValues.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                if (uiFieldMap.containsKey(key)) {
                    String cType = uiFieldMap.get(key);
                    // Specific logic for Date type component
                    if (cType.equals(HkSystemConstantUtil.CustomField.ComponentType.DATE)) {
                        if (value != null) {
                            SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS\'Z\'");
                            formatDate.setTimeZone(TimeZone.getTimeZone("UTC"));
                            Date parseDate = null;
                            Date parseIncDate = null;
                            try {
                                // parseDate gives the formatted date
                                parseDate = formatDate.parse(value.toString());
                                Calendar c = Calendar.getInstance();
                                c.setTime(parseDate);
                                c.add(Calendar.DATE, 1);
                                // incremented date contains next date i.e the current date +1
//                                String incrementedDate = formatDate.format(c.getTime()); // new date
                                parseIncDate = new Date(c.getTimeInMillis());

                            } catch (ParseException ex) {
                                Logger.getLogger(HkStockServiceImpl.class
                                        .getName()).log(Level.SEVERE, null, ex);
                            }
                            if (!(key.startsWith("from") || key.startsWith("to"))) {
                                criterias.add(Criteria.where(ChangeStatusFields.FIELD_VALUE + "." + key).gt(parseDate).lt(parseIncDate));
                            } else {
                                if (key.startsWith("from")) {
                                    dateCriteriaMap.put(key, parseDate);
                                } else {
                                    dateCriteriaMap.put(key, parseIncDate);
                                }
                            }
                        }
                    } // Code added for handling image and file but since we are not showing image and file components in search it is of no use as for now
                    else if (cType.equals(HkSystemConstantUtil.CustomField.ComponentType.IMAGE) || cType.equals(HkSystemConstantUtil.CustomField.ComponentType.UPLOAD)) {
                        criterias.add(Criteria.where(ChangeStatusFields.FIELD_VALUE + "." + key).regex(value.toString()));
                    } else {
                        if (value instanceof Collection) {
                            Collection coll = (Collection) value;
                            if (!CollectionUtils.isEmpty(coll)) {
                                criterias.add(Criteria.where(ChangeStatusFields.FIELD_VALUE + "." + key).in(coll));
                            }
                        } else if (value instanceof Date) {
                            long date = Long.parseLong(value.toString());
                            criterias.add(Criteria.where(ChangeStatusFields.FIELD_VALUE + "." + key).is(new Date(date)));
                        } else {
                            if (value != null && value.toString().length() > 0) {
                                criterias.add(Criteria.where(ChangeStatusFields.FIELD_VALUE + "." + key).is(value));
                            }
                        }
                    }
                }

            }
        }
        if (!CollectionUtils.isEmpty(invoiceIds)) {
            criterias.add(Criteria.where(TransferFields.INVOICE).in(invoiceIds));
        }
        if (!CollectionUtils.isEmpty(parcelIds)) {
            criterias.add(Criteria.where(TransferFields.PARCEL).in(parcelIds));
        }
        if (!CollectionUtils.isEmpty(lotIds)) {
            criterias.add(Criteria.where(TransferFields.LOT).in(lotIds));
        }
        if (!CollectionUtils.isEmpty(packetIds)) {
            criterias.add(Criteria.where(TransferFields.PACKET).in(packetIds));
        }
        if (!CollectionUtils.isEmpty(dateCriteriaMap)) {
            Set<String> removedFields = new HashSet<>();
            Iterator<Map.Entry<String, Object>> itr = dateCriteriaMap.entrySet().iterator();
            while (itr.hasNext()) {
                Map.Entry<String, Object> entryMap = itr.next();
                String key = entryMap.getKey();
                Date value = (Date) entryMap.getValue();

                if (!removedFields.contains(key)) {
                    if (key.startsWith("from")) {
                        if (dateCriteriaMap.containsKey("to" + key.substring(4))) {
                            Date toValue = (Date) dateCriteriaMap.get("to" + key.substring(4));
                            criterias.add(Criteria.where(ChangeStatusFields.FIELD_VALUE + "." + key.substring(4)).gte(value).lt(toValue));
                            removedFields.add("to" + key.substring(4));
                            removedFields.add(key);
                        } else {
                            criterias.add(Criteria.where(ChangeStatusFields.FIELD_VALUE + "." + key.substring(4)).gte(value));
                            removedFields.add(key);
                        }
                    } else {
                        if (dateCriteriaMap.containsKey("from" + key.substring(2))) {
                            Date toValue = (Date) dateCriteriaMap.get("from" + key.substring(2));
                            criterias.add(Criteria.where(ChangeStatusFields.FIELD_VALUE + "." + key.substring(2)).gte(toValue).lt(value));
                            removedFields.add("from" + key.substring(2));
                            removedFields.add(key);
                        } else {
                            criterias.add(Criteria.where(ChangeStatusFields.FIELD_VALUE + "." + key.substring(2)).lt(value));
                            removedFields.add(key);

                        }
                    }
                }
            }
        }
        hkTransferDocuments = mongoGenericDao.findByCriteria(criterias, HkTransferDocument.class
        );
        return hkTransferDocuments;
    }
//    @MongoTransaction

    //------------------------------------------ Transfer Service Ends ---------------------------------//
    @Override
    public Map<String, String> createFieldNameWithComponentType(Map<String, Object> fieldValues
    ) {
        Map<String, String> componentCodeMap = new HashMap<>(HkSystemConstantUtil.CustomField.COMPONENT_CODE_MAP);
        Map<String, String> codeMap = null;
        if (componentCodeMap != null && !componentCodeMap.isEmpty()) {
            codeMap = new HashMap<>();
            for (Map.Entry<String, String> entry : componentCodeMap.entrySet()) {
                codeMap.put(entry.getValue(), entry.getKey());

            }
        }
        Map<String, String> fieldWithComponentMap = new HashMap<>();
        if (fieldValues != null && !fieldValues.isEmpty()) {
            for (Map.Entry<String, Object> custom : fieldValues.entrySet()) {
                String[] split = custom.getKey().split("\\" + HkSystemConstantUtil.SEPERATOR_FOR_CUSTOM_FIELD_LABEL);
                if (split != null && split.length >= 1) {
                    fieldWithComponentMap.put(custom.getKey(), codeMap.get(split[1]));
                }
            }
        }
        return fieldWithComponentMap;
    }

    @Override
    public List<HkIssueDocument> retrieveIssueByIds(List<ObjectId> issueId) {
        List<HkIssueDocument> issueDocuments = null;
        List<HkIssueDocument> finalList = null;
        List<Criteria> criterias = new ArrayList<>();
        if (!CollectionUtils.isEmpty(issueId)) {
            criterias.add(Criteria.where(IssueFields.ISSUE_ID).in(issueId));
            issueDocuments
                    = mongoGenericDao.findByCriteria(criterias, HkIssueDocument.class
                    );
            if (!CollectionUtils.isEmpty(issueDocuments)) {
                finalList = new ArrayList<>();
//            uiFieldList = new ArrayList<>();
//            for (HkInvoiceDocument invoiceDocument : invoiceDocuments) {
//                if (invoiceDocument.getFieldValue() != null) {
//                    Map<String, Object> map = invoiceDocument.getFieldValue().toMap();
//                    if (!CollectionUtils.isEmpty(map)) {
//                        for (Map.Entry<String, Object> entrySet : map.entrySet()) {
//                            String key = entrySet.getKey();
//                            Object value = entrySet.getValue();
//                            uiFieldList.add(key);
//                        }
//                    }
//                }
//            }
                //Map<String, String> uiFieldListWithComponentType = customFieldService.retrieveUIFieldNameWithComponentTypes(uiFieldList);
                String pointerComponentType = HkSystemConstantUtil.CustomField.ComponentType.POINTER + "@*" + HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN;
                Map<String, String> uiFieldMap = null;
                for (HkIssueDocument issueDocument : issueDocuments) {
                    HkIssueDocument temp = issueDocument;
                    Map<String, Object> customMap = issueDocument.getFieldValue().toMap();
                    uiFieldMap = this.createFieldNameWithComponentType(customMap);
                    if (!CollectionUtils.isEmpty(uiFieldMap)) {

                        for (Map.Entry<String, String> field : uiFieldMap.entrySet()) {
                            if (field.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN) || field.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.USER_MULTISELECT) || field.getValue().equals(pointerComponentType)) {
                                if (customMap != null && !customMap.isEmpty() && customMap.containsKey(field.getKey())) {
                                    String value = customMap.get(field.getKey()).toString().replaceAll("\\[", "").replaceAll("\\]", "");
                                    customMap.put(field.getKey(), value);
                                }
                            }
                        }
                        temp.getFieldValue().putAll(customMap);
                        finalList.add(temp);
                        return finalList;
                    }
                }
            }
        }
        return issueDocuments;
    }

    @Override
    @MongoTransaction
    public Boolean
            deleteInvoice(ObjectId invoiceId, Long updatedBy) {
        if (invoiceId != null && updatedBy != null) {
            HkInvoiceDocument invoiceDocument = (HkInvoiceDocument) mongoGenericDao.retrieveById(invoiceId, HkInvoiceDocument.class
            );
            if (invoiceDocument
                    != null) {
                invoiceDocument.setLastModifiedBy(updatedBy);
                invoiceDocument.setLastModifiedOn(new Date());
                invoiceDocument.setStatus(HkSystemConstantUtil.StockStatus.ARCHIVED);
                invoiceDocument.setIsArchive(Boolean.TRUE);
                mongoGenericDao.update(invoiceDocument);
                return true;
            }
        }
        return false;
    }

    private List<Long> getCompnies(Long companyId) {
        List<Long> companyIds = new ArrayList<>();
        companyIds.add(0l);
        if (companyId != null && !companyId.equals(0l)) {
            companyIds.add(companyId);
        }
        return companyIds;
    }

    @Override
    @MongoTransaction
    public String saveSubLot(Map<String, Object> subLotCustomMap, Map<String, String> subLotDbTypeMap, Map<String, String> uiFieldMap, Long franchise, Long createdBy, ObjectId parcelId, List<HkFieldDocument> totalformulaList, Long featureId) {
        HkSubLotDocument subLotDocument = new HkSubLotDocument();
        subLotDocument.setFranchiseId(franchise);
        subLotDocument.setCreatedByFranchise(franchise);
        subLotDocument.setIsArchive(Boolean.FALSE);
        subLotDocument.setCreatedBy(createdBy);
        subLotDocument.setLastModifiedBy(createdBy);
        subLotDocument.setCreatedOn(new Date());
        subLotDocument.setModifiedOn(new Date());
        subLotDocument.setParcel(parcelId);
        HkParcelDocument parcelDocument = retrieveParcelById(parcelId);
        subLotDocument.setInvoice(parcelDocument.getInvoice());
        mongoGenericDao.create(subLotDocument);
        if (!CollectionUtils.isEmpty(subLotCustomMap) && !CollectionUtils.isEmpty(subLotDbTypeMap)) {
            BasicBSONObject fieldValues = customFieldService.makeBSONObject(subLotCustomMap, subLotDbTypeMap, uiFieldMap, HkSystemConstantUtil.Feature.SUB_LOT, franchise, null, Boolean.FALSE, null, null);
            subLotDocument.setFieldValue(fieldValues);
        } else {
            BasicBSONObject fieldValues = new BasicBSONObject();
            subLotDocument.setFieldValue(fieldValues);
        }
        Map<String, Double> finalFormulaEvalMap = hkFormulaExecution.mapOfEvaluatedFormulaValueWithDbField(totalformulaList, franchise, null, null, null, null, subLotDocument, null, null, null, featureId, HkSystemConstantUtil.FeatureForFormulaEvaluation.EDIT_SUB_LOT);
        if (finalFormulaEvalMap != null && !finalFormulaEvalMap.isEmpty()) {
            for (Map.Entry<String, Double> entrySet : finalFormulaEvalMap.entrySet()) {
                subLotDocument.getFieldValue().put(entrySet.getKey(), entrySet.getValue());
            }
        }
        mongoGenericDao.update(subLotDocument);

//        this.evaluateFormulaForFirstScenarioForAddAndUpdateSubLot(franchise, dbFieldWithFormulaMap, subLotDocument, featureId);
        return subLotDocument.getId().toString();
    }

    @Override
    public List<HkSubLotDocument> retrieveSubLotsByCriteria(List<ObjectId> parcelIds, List<ObjectId> subLotIds, Long franchise, Boolean isArchive, Boolean excludeSubLotWithAssociatedLot, ObjectId includeSubLotWithAssociatedLot) {
        List<HkSubLotDocument> hkSubLotDocuments = null;
        Map<String, String> uiFieldMap = null;
        List<HkSubLotDocument> finalList = null;
        List<Criteria> criterias = new ArrayList<>();
        if (!CollectionUtils.isEmpty(parcelIds)) {
            criterias.add(Criteria.where(SubLotFields.PARCEL_ID).in(parcelIds));
        }
        if (!CollectionUtils.isEmpty(subLotIds)) {
            criterias.add(Criteria.where(SubLotFields.SUB_LOT_ID).in(subLotIds));
        }
        if (franchise != null) {
            criterias.add(Criteria.where(SubLotFields.FRANCHISE).in(this.getCompnies(franchise)));
        }
        if (isArchive != null) {
            criterias.add(Criteria.where(SubLotFields.IS_ARCHIVE).is(isArchive));
        }
        if (excludeSubLotWithAssociatedLot != null && excludeSubLotWithAssociatedLot == true) {
            if (includeSubLotWithAssociatedLot != null) {
                Criteria associateLot = new Criteria();
                associateLot.orOperator(Criteria.where(SubLotFields.ASSOCIATED_LOT).is(null),
                        Criteria.where(SubLotFields.ASSOCIATED_LOT).is(includeSubLotWithAssociatedLot));
                criterias.add(associateLot);
            } else {
                criterias.add(Criteria.where(SubLotFields.ASSOCIATED_LOT).is(null));

            }
        }

        hkSubLotDocuments = mongoGenericDao.findByCriteria(criterias, HkSubLotDocument.class
        );
        if (!CollectionUtils.isEmpty(hkSubLotDocuments)) {
            finalList = new ArrayList<>();
            String pointerComponentType = HkSystemConstantUtil.CustomField.ComponentType.POINTER + "@*" + HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN;

            for (HkSubLotDocument hkSubLotDocument : hkSubLotDocuments) {
                HkSubLotDocument temp = hkSubLotDocument;
                Map<String, Object> customMap = hkSubLotDocument.getFieldValue().toMap();
                uiFieldMap = this.createFieldNameWithComponentType(customMap);
                if (!CollectionUtils.isEmpty(uiFieldMap)) {
                    for (Map.Entry<String, String> field : uiFieldMap.entrySet()) {
                        if (field.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN) || field.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.USER_MULTISELECT) || field.getValue().equals(pointerComponentType)) {
                            if (customMap != null && !customMap.isEmpty() && customMap.containsKey(field.getKey())) {
                                String value = customMap.get(field.getKey()).toString().replaceAll("\\[", "").replaceAll("\\]", "");
                                customMap.put(field.getKey(), value);
                            }
                        }
                    }
                    temp.getFieldValue().putAll(customMap);
                    finalList.add(temp);
                }
            }

        }
        return finalList;
    }

    @Override
    public List<HkSubLotDocument> retrieveSubLotByIds(List<ObjectId> subLotIds) {
        List<HkSubLotDocument> hkSubLotDocuments = null;
        List<Criteria> criterias = new ArrayList<>();
        if (subLotIds != null) {
            criterias.add(Criteria.where(SubLotFields.SUB_LOT_ID).in(subLotIds));

        }
        hkSubLotDocuments = mongoGenericDao.findByCriteria(criterias, HkSubLotDocument.class
        );
        return hkSubLotDocuments;
    }

    public List<? extends GenericDocument> retrieveAllDocumentsByCriteria(Long franchise, Boolean isActive, Class<? extends GenericDocument> class1) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -1);
        Date afterDate = cal.getTime();
        List<Criteria> criterias = new ArrayList<>();
        List<? extends GenericDocument> documents = null;
        criterias.add(Criteria.where(InvoiceFields.FRANCHISE).is(franchise));
        if (isActive != null) {
            criterias.add(Criteria.where(InvoiceFields.IS_ARCHIVE).is(isActive));
        }
        documents = mongoGenericDao.findByCriteria(criterias, class1);
        if (!CollectionUtils.isEmpty(documents)) {
            for (GenericDocument genericDocument : documents) {
                String pointerComponentType = HkSystemConstantUtil.CustomField.ComponentType.POINTER + "@*" + HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN;
                Map<String, String> uiFieldMap = null;
                Map<String, Object> customMap = genericDocument.getFieldValue().toMap();
                uiFieldMap = this.createFieldNameWithComponentType(customMap);
                if (!CollectionUtils.isEmpty(uiFieldMap)) {
                    for (Map.Entry<String, String> field : uiFieldMap.entrySet()) {
                        if (field.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN) || field.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.USER_MULTISELECT) || field.getValue().equals(pointerComponentType)) {
                            if (customMap != null && !customMap.isEmpty() && customMap.containsKey(field.getKey())) {
                                String value = customMap.get(field.getKey()).toString().replaceAll("\\[", "").replaceAll("\\]", "");
                                customMap.put(field.getKey(), value);
                            }
                        }
                    }
                    genericDocument.getFieldValue().putAll(customMap);
                }
            }

        }
        return documents;
    }

    @Override
    public GenericDocument retrieveDocumentById(ObjectId documentId, Class<?> class1) {
        GenericDocument genericDocument = (GenericDocument) mongoGenericDao.retrieveById(documentId, class1);
        String pointerComponentType = HkSystemConstantUtil.CustomField.ComponentType.POINTER + "@*" + HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN;
        Map<String, String> uiFieldMap = null;
        Map<String, Object> customMap = genericDocument.getFieldValue().toMap();
        uiFieldMap = this.createFieldNameWithComponentType(customMap);
        if (!CollectionUtils.isEmpty(uiFieldMap)) {
            for (Map.Entry<String, String> field : uiFieldMap.entrySet()) {
                if (field.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN) || field.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.USER_MULTISELECT) || field.getValue().equals(pointerComponentType)) {
                    if (customMap != null && !customMap.isEmpty() && customMap.containsKey(field.getKey())) {
                        String value = customMap.get(field.getKey()).toString().replaceAll("\\[", "").replaceAll("\\]", "");
                        customMap.put(field.getKey(), value);
                    }
                }
            }
            genericDocument.getFieldValue().putAll(customMap);
        }
        return genericDocument;
    }

    @Override
    public Boolean updateSublot(ObjectId id, Map<String, Object> sublotCustomMap, Map<String, String> sublotDbTypeMap, Map<String, HkFieldDocument> mapForFeatureInvolvedInFormula, List<HkFieldDocument> totalformulaList, Map<Long, String> featureIdWithNameMap, Long franchise, Long updatedBy, Long featureId) {
        Boolean result = Boolean.FALSE;
        if (id != null) {
            Map<String, String> uiFieldMap = null;
            if (sublotCustomMap != null && !sublotCustomMap.isEmpty()) {
                uiFieldMap = this.createFieldNameWithComponentType(sublotCustomMap);

            }
            HkSubLotDocument sublotDocument = (HkSubLotDocument) mongoGenericDao.retrieveById(id, HkSubLotDocument.class
            );

            List<ObjectId> objectSublotIds = new ArrayList<>();

            objectSublotIds.add(id);

            BasicBSONObject fieldValues = customFieldService.makeBSONObject(sublotCustomMap, sublotDbTypeMap, uiFieldMap, HkSystemConstantUtil.Feature.SUB_LOT, franchise, id, Boolean.TRUE, sublotDocument.getFieldValue().toMap(), null);

            sublotDocument.getFieldValue()
                    .putAll(fieldValues.toMap());
            sublotDocument.setLastModifiedBy(updatedBy);

            sublotDocument.setModifiedOn(
                    new Date());
            ////System.out.println("Before ::: " + new Date());

            Map<String, Double> finalFormulaEvalMap = hkFormulaExecution.mapOfEvaluatedFormulaValueWithDbField(totalformulaList, franchise, null, null, null, null, sublotDocument, null, null, null, featureId, HkSystemConstantUtil.FeatureForFormulaEvaluation.EDIT_SUB_LOT);
            if (finalFormulaEvalMap != null && !finalFormulaEvalMap.isEmpty()) {
                for (Map.Entry<String, Double> entrySet : finalFormulaEvalMap.entrySet()) {
                    sublotDocument.getFieldValue().put(entrySet.getKey(), entrySet.getValue());
                }
            }
            mongoGenericDao.update(sublotDocument);

            //---------------------------Formula First Scenario----------------------------------
//            this.evaluateFormulaForFirstScenarioForAddAndUpdateSubLot(franchise, dbFieldWithFormulaMap, sublotDocument, featureId);
            result = TRUE;
        }
        return result;
    }

    @Override
    public Boolean deleteDocument(ObjectId id, Long updatedBy, Class<? extends GenericDocument> class1) {
        if (id != null && updatedBy != null) {
            GenericDocument genericDocument = class1.cast(mongoGenericDao.retrieveById(id, class1));
            if (genericDocument != null) {
//                genericDocument.setLastModifiedBy(updatedBy);
                genericDocument.setModifiedOn(new Date());
//                genericDocument.setStatus(HkSystemConstantUtil.StockStatus.ARCHIVED);
                genericDocument.setIsArchive(Boolean.TRUE);
                mongoGenericDao.update(genericDocument);
                return true;
            }
        }
        return false;
    }

    public HkUserGradeStatusDocument retrieveUserGradeStatusByUserId(Long userId) {
        List<Criteria> criterias = new ArrayList<>();
        List<HkUserGradeStatusDocument> hkUserGradeStatusDocuments = new ArrayList<>();
        if (userId != null) {
            criterias.add(Criteria.where(UserGradeStatusFields.USER).is(userId));

        }
        hkUserGradeStatusDocuments = mongoGenericDao.findByCriteria(criterias, HkUserGradeStatusDocument.class
        );
        if (!CollectionUtils.isEmpty(hkUserGradeStatusDocuments)) {
            return hkUserGradeStatusDocuments.get(0);
        }

        return null;
    }

    @Override
    public List<HkUserGradeSuggestionDocument> allocateDiamondToEmpByGrade(List<ObjectId> packetIds, Long franchise, List<Long> empIds, Long department, List<Long> designations) {
        //retrieve Packets for thier grades and made a map according to grade id and packets belongs to that grade
        List<HkPacketDocument> packetDocuments = this.retrievePacketsbyIds(packetIds);
        Map<Long, List<HkPacketDocument>> gradeWisePacketMap = null;
        List<HkUserGradeSuggestionDocument> suggestionList = null;
        //System.out.println("packetDocuments :" + packetDocuments);
        if (!CollectionUtils.isEmpty(packetDocuments)) {
            gradeWisePacketMap = new HashMap<>();
            for (HkPacketDocument packet : packetDocuments) {
                //System.out.println("inside loop");
                if (packet.getFieldValue() != null && packet.getFieldValue().containsField(HkSystemConstantUtil.PacketStaticFieldName.GRADE)) {
                    Long grade = (Long) packet.getFieldValue().get(HkSystemConstantUtil.PacketStaticFieldName.GRADE);
                    if (grade != null) {
                        List<HkPacketDocument> packetDocumentList;
                        if (gradeWisePacketMap.containsKey(grade)) {
                            packetDocumentList = gradeWisePacketMap.get(grade);
                            packetDocumentList.add(packet);
                        } else {
                            packetDocumentList = new ArrayList<>();
                            packetDocumentList.add(packet);
                        }
                        gradeWisePacketMap.put(grade, packetDocumentList);
                    }
                }
            }

            //retriev all user grade status documents for franchise
            List<HkUserGradeStatusDocument> userGradeStatusDocuments = this.retrieveUserGradeStatusDocuments(empIds, franchise, Boolean.FALSE, department, designations);
            Map<Long, List<HkUserGradeStatusDocument>> gradeWiseEmpMap = null;
            Map<Long, HkUserGradeStatusDocument> userGradeStatusMap = null;
            Map<Long, Integer> empStockMap = null;
            //fill the map for grade wise emp list with
            //System.out.println("userGradeStatusDocuments :" + userGradeStatusDocuments);
            if (!CollectionUtils.isEmpty(userGradeStatusDocuments)) {
                gradeWiseEmpMap = new HashMap<>();
                userGradeStatusMap = new HashMap<>();
                empStockMap = new HashMap<>();
                suggestionList = new ArrayList<>();
                for (HkUserGradeStatusDocument userGradeStatusDocument : userGradeStatusDocuments) {
                    userGradeStatusMap.put(userGradeStatusDocument.getUserId(), userGradeStatusDocument);
                    if (userGradeStatusDocument.getGrade() != null && userGradeStatusDocument.getUserId() != null) {
                        List<HkUserGradeStatusDocument> empUserGradeList;
                        if (gradeWiseEmpMap.containsKey(userGradeStatusDocument.getGrade())) {
                            empUserGradeList = gradeWiseEmpMap.get(userGradeStatusDocument.getGrade());
                        } else {
                            empUserGradeList = new ArrayList<>();
                        }
                        empUserGradeList.add(userGradeStatusDocument);
                        gradeWiseEmpMap.put(userGradeStatusDocument.getGrade(), empUserGradeList);
                        empStockMap.put(userGradeStatusDocument.getUserId(), userGradeStatusDocument.getNoOfDiamondsInStock());
                    }
                }
            }

            Map<Long, Integer> empNewStockMap = new HashMap<>();
            //System.out.println("Grade wise packet :: " + gradeWisePacketMap);
            //System.out.println("gradeWiseEmpMap :" + gradeWiseEmpMap);
            for (Map.Entry<Long, List<HkPacketDocument>> entry : gradeWisePacketMap.entrySet()) {
                Long grade = entry.getKey();
                List<HkPacketDocument> packetList = entry.getValue();
                if (grade != null && !CollectionUtils.isEmpty(packetList)) {

                    if (!CollectionUtils.isEmpty(gradeWiseEmpMap)) {
                        //System.out.println("Grade : "+grade);
                        if (gradeWiseEmpMap.containsKey(grade)) {
                            //retrieve map of emp and grade status by the grade
                            List<HkUserGradeStatusDocument> empUserGradeList = gradeWiseEmpMap.get(grade);

                            //sort the map according to no of stock as per user
                            if (!CollectionUtils.isEmpty(empUserGradeList)) {
                                //System.out.println("Inside if ***************************");
                                Collections.sort(empUserGradeList, new Comparator() {
                                    @Override
                                    public int compare(Object o1, Object o2) {
                                        HkUserGradeStatusDocument oldUserGrade = (HkUserGradeStatusDocument) o1;
                                        HkUserGradeStatusDocument newUserGrade = (HkUserGradeStatusDocument) o2;
                                        return (oldUserGrade.getNoOfDiamondsInStock()).compareTo(newUserGrade.getNoOfDiamondsInStock())
                                                & (oldUserGrade.getLastAllocatedOn()).compareTo(newUserGrade.getLastAllocatedOn());
                                    }
                                });
                                int packetSize = packetList.size();
                                int empCount = empUserGradeList.size();
                                int current = 0;
                                int next = 1;
                                //if there is only one person with this grade so allot all the packets to this person and make the packetsize to 0
                                if (next == empCount) {
                                    //System.out.println("next == emp count");
                                    HkUserGradeStatusDocument userGrade = empUserGradeList.get(0);
                                    userGrade.setNoOfDiamondsInStock(userGrade.getNoOfDiamondsInStock() + packetSize);
                                    empNewStockMap.put(userGrade.getUserId(), packetSize);
                                    packetSize = 0;
                                } else {
                                    //itterate the loop till all employee
                                    while (next != empCount) {
                                        HkUserGradeStatusDocument currentUserGrade = empUserGradeList.get(current);
                                        HkUserGradeStatusDocument nextUserGrade = empUserGradeList.get(next);
                                        while (currentUserGrade.getNoOfDiamondsInStock() < nextUserGrade.getNoOfDiamondsInStock() && packetSize > 0) {
                                            //System.out.println("1." +currentUserGrade.getNoOfDiamondsInStock() +" 2."+ nextUserGrade.getNoOfDiamondsInStock());
                                            for (int counter = 0; counter <= current; counter++) {
                                                if (packetSize > 0) {
                                                    HkUserGradeStatusDocument userGrade = empUserGradeList.get(counter);
                                                    userGrade.setNoOfDiamondsInStock(userGrade.getNoOfDiamondsInStock() + 1);
                                                    int stock = 0;
                                                    if (empNewStockMap.containsKey(userGrade.getUserId())) {
                                                        stock = empNewStockMap.get(userGrade.getUserId());
                                                    }
                                                    empNewStockMap.put(userGrade.getUserId(), stock + 1);
                                                    packetSize--;
                                                } else {
                                                    break;
                                                }
                                            }
                                        }
                                        current = next;
                                        next = current + 1;
                                        Collections.sort(empUserGradeList, new Comparator() {
                                            @Override
                                            public int compare(Object o1, Object o2) {
                                                HkUserGradeStatusDocument oldUserGrade = (HkUserGradeStatusDocument) o1;
                                                HkUserGradeStatusDocument newUserGrade = (HkUserGradeStatusDocument) o2;
                                                return (oldUserGrade.getNoOfDiamondsInStock()).compareTo(newUserGrade.getNoOfDiamondsInStock())
                                                        & (oldUserGrade.getLastAllocatedOn()).compareTo(newUserGrade.getLastAllocatedOn());
                                            }
                                        });
                                    }
                                }

                                if (packetSize > 0) {
                                    int noOfEmp = empUserGradeList.size();
                                    int toBeDivide = packetSize / noOfEmp;
                                    int remainingPack = packetSize % noOfEmp;
                                    //iterate the loop till divide into same between emp
                                    //i.e. if 87 packets are there and 10 emp. So first it will divide 8 to all

                                    for (HkUserGradeStatusDocument userGrade : empUserGradeList) {
                                        if (packetSize > 0) {
                                            int packetAllocated = toBeDivide;
                                            if (remainingPack > 0) {
                                                packetAllocated++;
                                                remainingPack--;
                                            }
                                            userGrade.setNoOfDiamondsInStock(userGrade.getNoOfDiamondsInStock() + packetAllocated);
                                            int stock = 0;
                                            if (empNewStockMap.containsKey(userGrade.getUserId())) {
                                                stock = empNewStockMap.get(userGrade.getUserId());
                                            }
                                            empNewStockMap.put(userGrade.getUserId(), stock + packetAllocated);
                                            packetSize = packetSize - packetAllocated;
                                        } else {
                                            break;
                                        }

                                    }
                                }
                            } else {
                                //System.out.println("In else ************");
                                break;
                            }
                        }
                    }
                }
            }
            //System.out.println("empNewStockMap:" + empNewStockMap);
            for (Map.Entry<Long, List<HkPacketDocument>> entry : gradeWisePacketMap.entrySet()) {
                Long grade = entry.getKey();
                List<HkPacketDocument> packetList = entry.getValue();
                List<HkUserGradeStatusDocument> userGrades = gradeWiseEmpMap.get(grade);
                if (!CollectionUtils.isEmpty(userGrades)) {
                    List<Long> emps = new ArrayList<>();
                    for (HkUserGradeStatusDocument hkUserGradeStatusDocument : userGrades) {
                        emps.add(hkUserGradeStatusDocument.getUserId());
                    }
                    for (int packet = 0; packet < packetList.size(); packet++) {
                        for (Long id : emps) {
                            if (empNewStockMap.containsKey(id)) {
                                Integer newStock = empNewStockMap.get(id);
                                HkUserGradeSuggestionDocument userGradeSuggestionDocument = new HkUserGradeSuggestionDocument();
                                userGradeSuggestionDocument.setUserId(id);
                                userGradeSuggestionDocument.setNewStock(newStock);
                                userGradeSuggestionDocument.setPacketId(packetList.get(packet).getId());
                                if (!CollectionUtils.isEmpty(userGradeStatusMap) && userGradeStatusMap.containsKey(id)) {
                                    HkUserGradeStatusDocument userGradeStatusDocument = userGradeStatusMap.get(id);
                                    if (userGradeStatusDocument != null) {
                                        userGradeSuggestionDocument.setCurrentStock(empStockMap.get(id));
                                        userGradeSuggestionDocument.setGrade(userGradeStatusDocument.getGrade());
                                        userGradeSuggestionDocument.setGoingToGrade(userGradeStatusDocument.getGoingToGrade());
                                        userGradeSuggestionDocument.setTotalStock(newStock + empStockMap.get(id));
                                    }
                                }
                                suggestionList.add(userGradeSuggestionDocument);
                            } else {
                                break;
                            }
                        }
                    }
                }
            }
//            if (!CollectionUtils.isEmpty(empNewStockMap)) {
//                for (Map.Entry<Long, Integer> entry : empNewStockMap.entrySet()) {
//                    Long empId = entry.getKey();
//                    Integer newStock = entry.getValue();
//                    HkUserGradeSuggestionDocument userGradeSuggestionDocument = new HkUserGradeSuggestionDocument();
//                    userGradeSuggestionDocument.setUserId(empId);
//                    userGradeSuggestionDocument.setNewStock(newStock);
//                    if (!CollectionUtils.isEmpty(userGradeStatusMap) && userGradeStatusMap.containsKey(empId)) {
//                        HkUserGradeStatusDocument userGradeStatusDocument = userGradeStatusMap.get(empId);
//                        if (userGradeStatusDocument != null) {
//                            userGradeSuggestionDocument.setCurrentStock(empStockMap.get(empId));
//                            userGradeSuggestionDocument.setGrade(userGradeStatusDocument.getGrade());
//                            userGradeSuggestionDocument.setGoingToGrade(userGradeStatusDocument.getGoingToGrade());
//                            userGradeSuggestionDocument.setTotalStock(newStock + empStockMap.get(empId));
//                        }
//                    }
//                    suggestionList.add(userGradeSuggestionDocument);
//                }
//            }

        }
        return suggestionList;
    }

    public List<HkUserGradeStatusDocument> retrieveUserGradeStatusDocuments(List<Long> empIds, Long franchise, Boolean isArchive, Long department, List<Long> designations) {
        List<HkUserGradeStatusDocument> userGradeStatusDocuments = null;
        List<Criteria> criterias = new ArrayList<>();
        if (franchise != null) {
            criterias.add(Criteria.where(UserGradeStatusFields.FRANCHISE).is(franchise));
            criterias.add(Criteria.where(IS_ARCHIVE).is(isArchive));
            if (department != null) {
                criterias.add(Criteria.where(UserGradeStatusFields.DEPARTMENT).is(department));
            }
            if (!CollectionUtils.isEmpty(designations)) {
                criterias.add(Criteria.where(UserGradeStatusFields.DESIGNATIONS).in(designations));
            }
        }
        if (!CollectionUtils.isEmpty(empIds)) {
            criterias.add(Criteria.where(UserGradeStatusFields.USER).in(empIds));

        }

        if (!CollectionUtils.isEmpty(criterias)) {
            userGradeStatusDocuments = mongoGenericDao.findByCriteria(criterias, HkUserGradeStatusDocument.class
            );
        }
        return userGradeStatusDocuments;
    }

    //------------------------------------------ Issue Receive Servie Starts -------------------------------//
    @Override
    @MongoTransaction
    public void saveIssueReceieveDocuments(List<HkIssueReceiveDocument> issueReceiveDocuments) {
        if (!CollectionUtils.isEmpty(issueReceiveDocuments)) {
            Date modifiedDate = new Date();
            for (HkIssueReceiveDocument hkIssueReceiveDocument : issueReceiveDocuments) {
                if (hkIssueReceiveDocument.getCreatedOn() == null) {
                    hkIssueReceiveDocument.setCreatedOn(modifiedDate);
                }
                hkIssueReceiveDocument.setModifiedOn(modifiedDate);
                mongoGenericDao.update(hkIssueReceiveDocument);
            }
        }
    }

    @Override
    public List<HkIssueReceiveDocument> retrieveIssueReceiveDocumentsBySlipNo(Date slipDate, String slipNo, Long userId, Long department, Long franchise, String requestType) {
//        if (slipDate != null) {
        List<Criteria> criterias = new ArrayList<>();
        if (slipDate != null) {
//            Calendar calendar = Calendar.getInstance();
//            calendar.setTime(slipDate);
//            calendar.set(Calendar.HOUR, 0);
//            calendar.set(Calendar.MINUTE, 0);
//            calendar.set(Calendar.MILLISECOND, 0);
//            calendar.set(Calendar.SECOND, 0);
            DateTime dateTime = new DateTime(slipDate).withZone(DateTimeZone.UTC).withTime(0, 0, 0, 0);
            criterias.add(Criteria.where(IssueReceiveFields.SLIP_DATE).is(dateTime));
        }
        criterias.add(Criteria.where(IssueReceiveFields.IS_ACTIVE).is(true));
        if (slipNo != null) {
            criterias.add(Criteria.where(IssueReceiveFields.SLIP_NO).is(slipNo));
        }
        if (requestType.equals(HkSystemConstantUtil.IssueReceiveOperations.COLLECT)) {
            criterias.add(Criteria.where(IssueReceiveFields.STATUS).is(HkSystemConstantUtil.IssueReceiveStatus.PENDING));
        } else if (requestType.equals(HkSystemConstantUtil.IssueReceiveOperations.RECEIVE)) {
            criterias.add(Criteria.where(IssueReceiveFields.STATUS).is(HkSystemConstantUtil.IssueReceiveStatus.ISSUE));
            criterias.add(new Criteria()
                    .orOperator(Criteria.where(IssueReceiveFields.ISSUE_TO).exists(false),
                            Criteria.where(IssueReceiveFields.ISSUE_TO).is(""),
                            Criteria.where(IssueReceiveFields.ISSUE_TO).is(userId)));
        } else if (requestType.equals(HkSystemConstantUtil.IssueReceiveOperations.ISSUE)) {
            criterias.add(Criteria.where(IssueReceiveFields.STATUS).is(HkSystemConstantUtil.IssueReceiveStatus.COLLECTED));
        }
        if (department != null) {
            if (!StringUtils.isEmpty(requestType)) {
                if (requestType.equals(HkSystemConstantUtil.IssueReceiveOperations.COLLECT) || requestType.equals(HkSystemConstantUtil.IssueReceiveOperations.ISSUE)) {
                    criterias.add(Criteria.where(IssueReceiveFields.STOCK_DEPT).is(department));
                } else if (requestType.equals(HkSystemConstantUtil.IssueReceiveOperations.RECEIVE)) {
                    List<Long> depts = userService.retrieveAllParentDepartments(department);
                    depts.add(department);
                    criterias.add(Criteria.where(IssueReceiveFields.DESTINATION_DEPT).in(depts));
                }
            }
        }
        if (franchise != null) {
            if (!StringUtils.isEmpty(requestType)) {
                if (requestType.equals(HkSystemConstantUtil.IssueReceiveOperations.COLLECT) || requestType.equals(HkSystemConstantUtil.IssueReceiveOperations.ISSUE)) {
                    criterias.add(Criteria.where(IssueReceiveFields.STOCK_FRANCHISE_ID).is(franchise));
                } else if (requestType.equals(HkSystemConstantUtil.IssueReceiveOperations.RECEIVE)) {
                    criterias.add(Criteria.where(IssueReceiveFields.DESTINATION_FRANCHISE_ID).is(franchise));

                }
            }

        }
        return mongoGenericDao.findByCriteria(criterias, HkIssueReceiveDocument.class
        );

    }

    @Override
    public List<HkIssueReceiveDocument> retrieveIssueReceiveDocumentsBySlipNoForDirectReceive(Date slipDate, String slipNo, Long department, Long franchise, String requestType) {

        List<Criteria> criterias = new ArrayList<>();
        if (slipDate != null) {

            DateTime dateTime = new DateTime(slipDate).withZone(DateTimeZone.UTC).withTime(0, 0, 0, 0);
            criterias.add(Criteria.where(IssueReceiveFields.SLIP_DATE).is(dateTime));
        }
        if (slipNo != null) {
            criterias.add(Criteria.where(IssueReceiveFields.SLIP_NO).is(slipNo));
        }
        criterias.add(Criteria.where(IssueReceiveFields.STATUS).is(HkSystemConstantUtil.IssueReceiveStatus.COMPLETE));
        criterias.add(Criteria.where(IssueReceiveFields.TYPE).is(HkSystemConstantUtil.IssueReceiveTypes.DIRECT_ISSUE));
        List<HkIssueReceiveDocument> issuerReceiveDocs = mongoGenericDao.findByCriteria(criterias, HkIssueReceiveDocument.class
        );

        if (!CollectionUtils.isEmpty(issuerReceiveDocs)) {
            for (HkIssueReceiveDocument hkIssueReceiveDocument : issuerReceiveDocs) {
                Double issuedCarat = hkIssueReceiveDocument.getIssueCarat();
                Integer issuedPcs = hkIssueReceiveDocument.getIssuePcs();
                List<Criteria> criterias1 = new ArrayList<>();
                if (slipDate != null) {

                    DateTime dateTime = new DateTime(slipDate).withZone(DateTimeZone.UTC).withTime(0, 0, 0, 0);
                    criterias1.add(Criteria.where(IssueReceiveFields.SLIP_DATE).is(dateTime));
                }
                criterias1.add(Criteria.where(IssueReceiveFields.IS_ACTIVE).is(true));
                if (slipNo != null) {
                    criterias1.add(Criteria.where(IssueReceiveFields.SLIP_NO).is(slipNo));
                }
                criterias1.add(Criteria.where(IssueReceiveFields.STATUS).is(HkSystemConstantUtil.IssueReceiveStatus.COMPLETE));
                criterias1.add(Criteria.where(IssueReceiveFields.TYPE).is(HkSystemConstantUtil.IssueReceiveTypes.DIRECT_RECEIVE));
                if (hkIssueReceiveDocument.getPacket() != null) {
                    criterias1.add(Criteria.where(IssueReceiveFields.PACKET).is(hkIssueReceiveDocument.getPacket()));
                } else if (hkIssueReceiveDocument.getLot() != null) {
                    criterias1.add(Criteria.where(IssueReceiveFields.LOT).is(hkIssueReceiveDocument.getLot()));
                } else if (hkIssueReceiveDocument.getParcel() != null) {
                    criterias1.add(Criteria.where(IssueReceiveFields.PARCEL).is(hkIssueReceiveDocument.getParcel()));
                }
                List<HkIssueReceiveDocument> issuerReceiveDocs1 = mongoGenericDao.findByCriteria(criterias1, HkIssueReceiveDocument.class);

                if (!CollectionUtils.isEmpty(issuerReceiveDocs1)) {
                    for (HkIssueReceiveDocument hkIssueReceiveDocument1 : issuerReceiveDocs1) {
                        issuedCarat -= hkIssueReceiveDocument1.getReceivedCarat();
                        issuedPcs -= hkIssueReceiveDocument1.getReceivedPcs();
                    }
                }

                hkIssueReceiveDocument.setReceivedCarat(issuedCarat);
                hkIssueReceiveDocument.setReceivedPcs(issuedPcs);
            }
            return issuerReceiveDocs;
        }

        return null;
    }

    @Override
    public List<HkIssueReceiveDocument> retrieveIssueReceiveDocumentsByStatusAndDepartment(String status, Long sourceDept, Long destinationDept, Long stockDept) {

        List<Criteria> criterias = new ArrayList<>();
        if (!StringUtils.isEmpty(status)) {
            criterias.add(Criteria.where(IssueReceiveFields.STATUS).is(status));
        }
        if (sourceDept != null) {
            criterias.add(Criteria.where(IssueReceiveFields.SOURCE_DEPT).is(sourceDept));
        }
        if (destinationDept != null) {
            criterias.add(Criteria.where(IssueReceiveFields.DESTINATION_DEPT).is(destinationDept));
        }
        if (stockDept != null) {
            criterias.add(Criteria.where(IssueReceiveFields.STOCK_DEPT).is(stockDept));
        }
        criterias.add(Criteria.where(IssueReceiveFields.IS_ACTIVE).is(true));

        return mongoGenericDao.findByCriteria(criterias, HkIssueReceiveDocument.class
        );

    }

    @Override
    public Map<String, Object> retrieveStockAndParentdetails(Map<String, String> payload, Long franchise, Long userId, Long department) {
        Map<String, Object> result = new HashMap<>();
        String requestType = payload.get("req_type");
        payload.remove("req_type");
        List<String> Array = null;
        if (!StringUtils.isEmpty(requestType) && !CollectionUtils.isEmpty(payload) && (requestType.equals(HkSystemConstantUtil.IssueReceiveOperations.ISSUE_INWARD) || requestType.equals(HkSystemConstantUtil.IssueReceiveOperations.REQUEST) || requestType.equals(HkSystemConstantUtil.IssueReceiveOperations.ISSUE))) {
            if (payload.containsKey(HkSystemConstantUtil.StockName.PARCEL)) {
                Map<String, Object> fieldValues = new HashMap<>();

                fieldValues.put(HkSystemConstantUtil.ParcelStaticFields.PARCEL_ID, payload.get(HkSystemConstantUtil.StockName.PARCEL));
                List<HkParcelDocument> parcels = this.retrieveParcels(fieldValues, null, null, franchise, false, null, null, null, null);
                if (!CollectionUtils.isEmpty(parcels)) {
                    if (requestType.equals(HkSystemConstantUtil.IssueReceiveOperations.ISSUE_INWARD) || requestType.equals(HkSystemConstantUtil.IssueReceiveOperations.REQUEST)) {
                        Array = new ArrayList<>();
                        Array.add(userId.toString() + ":E");
                        fieldValues.put(HkSystemConstantUtil.ParcelStaticFieldName.IN_STOCK_OF, Array);

                        String value = parcels.get(0).getFieldValue().get(HkSystemConstantUtil.ParcelStaticFieldName.IN_STOCK_OF).toString();
                        List<String> values = new ArrayList<>();
                        String[] valueArray = value.replace("\"", "").split(",");
                        for (String v : valueArray) {
                            values.add(v.replace("\"", ""));
                        }
                        if (this.equalLists(values, Array)) {
                            if (!(HkSystemConstantUtil.IssueReceiveStockStatus.PENDING == null ? parcels.get(0).getIssueReceiveStatus() == null : HkSystemConstantUtil.IssueReceiveStockStatus.PENDING.equals(parcels.get(0).getIssueReceiveStatus()))) {
                                result.put(HkSystemConstantUtil.StockName.PARCEL, parcels.get(0));
                                HkInvoiceDocument invoice = this.retrieveInvoiceById(parcels.get(0).getInvoice());
                                result.put(HkSystemConstantUtil.StockName.INVOICE, invoice);
                            } else {
                                result.put("Message", "Parcel is already in process");
                            }
                        } else {
                            result.put("Message", "Parcel is not in stock of current user");
                        }
                    } else if (requestType.equals(HkSystemConstantUtil.IssueReceiveOperations.ISSUE)) {
                        //Check in stock of department
                        Array = new ArrayList<>();
                        Array.add(department.toString());
                        if (this.equalLists((ArrayList<String>) parcels.get(0).getFieldValue().get(HkSystemConstantUtil.ParcelStaticFieldName.IN_STOCK_OF_DEPT), Array)) {
                            result.put(HkSystemConstantUtil.StockName.PARCEL, parcels.get(0));
                            HkInvoiceDocument invoice = this.retrieveInvoiceById(parcels.get(0).getInvoice());
                            result.put(HkSystemConstantUtil.StockName.INVOICE, invoice);
                        } else {
                            result.put("Message", "Parcel is not in stock of current department");
                        }
                    }

                } else {
                    result.put("Message", "Parcel not exist with this parcelID");
                }
            } else if (payload.containsKey(HkSystemConstantUtil.StockName.LOT)) {
                Map<String, Object> fieldValues = new HashMap<>();
                String status = null;
                fieldValues.put(HkSystemConstantUtil.LotStaticFieldName.LOT_ID, payload.get(HkSystemConstantUtil.StockName.LOT));

                List<HkLotDocument> lots = this.retrieveLotsByCriteria(fieldValues, null, null, null, null, null, franchise, false, false);
                if (!CollectionUtils.isEmpty(lots)) {
                    if (requestType.equals(HkSystemConstantUtil.IssueReceiveOperations.ISSUE_INWARD) || requestType.equals(HkSystemConstantUtil.IssueReceiveOperations.REQUEST)) {
                        Array = new ArrayList<>();
                        Array.add(userId.toString() + ":E");
                        fieldValues.put(HkSystemConstantUtil.LotStaticFieldName.IN_STOCK_OF, Array);
                        if (this.equalLists((ArrayList<String>) lots.get(0).getFieldValue().get(HkSystemConstantUtil.LotStaticFieldName.IN_STOCK_OF), Array)) {
                            if (!(HkSystemConstantUtil.IssueReceiveStockStatus.PENDING == null ? lots.get(0).getIssueReceiveStatus() == null : HkSystemConstantUtil.IssueReceiveStockStatus.PENDING.equals(lots.get(0).getIssueReceiveStatus()))) {
                                result.put(HkSystemConstantUtil.StockName.LOT, lots.get(0));

                                HkInvoiceDocument invoice = this.retrieveInvoiceById(lots.get(0).getInvoice());
                                HkParcelDocument parcel = this.retrieveParcelById(lots.get(0).getParcel());

                                result.put(HkSystemConstantUtil.StockName.INVOICE, invoice);
                                result.put(HkSystemConstantUtil.StockName.PARCEL, parcel);
                            } else {
                                result.put("Message", "Lot is already in process");
                            }
                        } else {
                            result.put("Message", "Lot is not in stock of current user");
                        }
                    } else if (requestType.equals(HkSystemConstantUtil.IssueReceiveOperations.ISSUE)) {
                        //Check in stock of department
                        Array = new ArrayList<>();
                        Array.add(department.toString());
                        if (this.equalLists((ArrayList<String>) lots.get(0).getFieldValue().get(HkSystemConstantUtil.LotStaticFieldName.IN_STOCK_OF_DEPT), Array)) {
                            result.put(HkSystemConstantUtil.StockName.LOT, lots.get(0));

                            HkInvoiceDocument invoice = this.retrieveInvoiceById(lots.get(0).getInvoice());
                            HkParcelDocument parcel = this.retrieveParcelById(lots.get(0).getParcel());

                            result.put(HkSystemConstantUtil.StockName.INVOICE, invoice);
                            result.put(HkSystemConstantUtil.StockName.PARCEL, parcel);
                        } else {
                            result.put("Message", "Lot is not in stock of current department");
                        }
                    }

                } else {
                    result.put("Message", "Lot not exist with this lotID");
                }

            } else if (payload.containsKey(HkSystemConstantUtil.StockName.PACKET)) {
                Map<String, Object> fieldValues = new HashMap<>();
                fieldValues.put(HkSystemConstantUtil.AutoNumber.PACKET_ID, payload.get(HkSystemConstantUtil.StockName.PACKET));
                List<HkPacketDocument> packets = this.retrievePackets(fieldValues, null, null, null, franchise, false, null, null, null, null);
                if (!CollectionUtils.isEmpty(packets)) {
                    if (requestType.equals(HkSystemConstantUtil.IssueReceiveOperations.ISSUE_INWARD) || requestType.equals(HkSystemConstantUtil.IssueReceiveOperations.REQUEST)) {
                        Array = new ArrayList<>();
                        Array.add(userId.toString() + ":E");
                        fieldValues.put(HkSystemConstantUtil.PacketStaticFieldName.IN_STOCK_OF, Array);
                        String value = packets.get(0).getFieldValue().get(HkSystemConstantUtil.PacketStaticFieldName.IN_STOCK_OF).toString();
                        List<String> values = new ArrayList<>();
                        String[] valueArray = value.replace("\"", "").split(",");
                        for (String v : valueArray) {
                            values.add(v.replace("\"", ""));
                        }
                        if (this.equalLists(values, Array)) {
                            if (!(HkSystemConstantUtil.IssueReceiveStockStatus.PENDING == null ? packets.get(0).getIssueReceiveStatus() == null : HkSystemConstantUtil.IssueReceiveStockStatus.PENDING.equals(packets.get(0).getIssueReceiveStatus()))) {
                                result.put(HkSystemConstantUtil.StockName.PACKET, packets.get(0));

                                HkInvoiceDocument invoice = this.retrieveInvoiceById(packets.get(0).getInvoice());
                                HkParcelDocument parcel = this.retrieveParcelById(packets.get(0).getParcel());
                                HkLotDocument lot = this.retrieveLotById(packets.get(0).getLot());

                                result.put(HkSystemConstantUtil.StockName.INVOICE, invoice);
                                result.put(HkSystemConstantUtil.StockName.PARCEL, parcel);
                                result.put(HkSystemConstantUtil.StockName.LOT, lot);
                            } else {
                                result.put("Message", "Packet is already in process");
                            }
                        } else {
                            result.put("Message", "Packet is not in stock of current user");
                        }
                    } else if (requestType.equals(HkSystemConstantUtil.IssueReceiveOperations.ISSUE)) {
                        //Check in stock of department
                        Array = new ArrayList<>();
                        Array.add(department.toString());
                        if (this.equalLists((ArrayList<String>) packets.get(0).getFieldValue().get(HkSystemConstantUtil.PacketStaticFieldName.IN_STOCK_OF_DEPT), Array)) {
                            result.put(HkSystemConstantUtil.StockName.PACKET, packets.get(0));

                            HkInvoiceDocument invoice = this.retrieveInvoiceById(packets.get(0).getInvoice());
                            HkParcelDocument parcel = this.retrieveParcelById(packets.get(0).getParcel());
                            HkLotDocument lot = this.retrieveLotById(packets.get(0).getLot());

                            result.put(HkSystemConstantUtil.StockName.INVOICE, invoice);
                            result.put(HkSystemConstantUtil.StockName.PARCEL, parcel);
                            result.put(HkSystemConstantUtil.StockName.LOT, lot);
                        } else {
                            result.put("Message", "Packet is not in stock of current department");
                        }
                    }

                } else {
                    result.put("Message", "Packet not exist with this packetID");
                }
            }
        }

        return result;
    }

    @Override
    @MongoTransaction
    public void issueReceiverequest(List<HkIssueReceiveDocument> issueReceiveDocuments, Long companyId) {
        if (!CollectionUtils.isEmpty(issueReceiveDocuments)) {

            List<ObjectId> parcelIds = new ArrayList<>();
            List<ObjectId> lotIds = new ArrayList<>();
            List<ObjectId> packetIds = new ArrayList<>();
            String type = "";
            Date modifiedOn = new Date();
            for (HkIssueReceiveDocument hkIssueReceiveDocument : issueReceiveDocuments) {

                hkIssueReceiveDocument.setStatus(HkSystemConstantUtil.IssueReceiveStatus.PENDING);
                hkIssueReceiveDocument.setIsActive(true);
                hkIssueReceiveDocument.setSourceFranchiseId(companyId);
                hkIssueReceiveDocument.setDestinationFranchiseId(companyId);
                hkIssueReceiveDocument.setStockFranchiseId(companyId);
                hkIssueReceiveDocument.setModifiedOn(modifiedOn);

                HkDepartmentConfigDocument deptConfig = configurationService.retrieveDocumentByDepartmentId(hkIssueReceiveDocument.getDestinationDepId(), companyId);
                if (deptConfig != null) {
                    hkIssueReceiveDocument.setStockDepId(deptConfig.getStockRoom());
                    hkIssueReceiveDocument.setStockFranchiseId(companyId);
                }
                if (hkIssueReceiveDocument.getPacket() != null) {
                    packetIds.add(hkIssueReceiveDocument.getPacket());
                } else if (hkIssueReceiveDocument.getLot() != null) {
                    lotIds.add(hkIssueReceiveDocument.getLot());
                } else if (hkIssueReceiveDocument.getParcel() != null) {
                    parcelIds.add(hkIssueReceiveDocument.getParcel());
                }
            }
            if (!CollectionUtils.isEmpty(parcelIds)) {
                List<HkParcelDocument> parcels = this.retrieveParcelsbyIds(parcelIds);
                if (!CollectionUtils.isEmpty(parcels)) {
                    for (HkParcelDocument hkParcelDocument : parcels) {
                        hkParcelDocument.setIssueReceiveStatus(HkSystemConstantUtil.IssueReceiveStockStatus.PENDING);
                        mongoGenericDao.update(hkParcelDocument);
                    }
                }
            }
            if (!CollectionUtils.isEmpty(lotIds)) {
                List<HkLotDocument> lots = this.retrieveLotsbyIds(lotIds);
                if (!CollectionUtils.isEmpty(lots)) {
                    for (HkLotDocument hkLotDocument : lots) {
                        hkLotDocument.setIssueReceiveStatus(HkSystemConstantUtil.IssueReceiveStockStatus.PENDING);
                        mongoGenericDao.update(hkLotDocument);
                    }
                }
            }
            if (!CollectionUtils.isEmpty(packetIds)) {
                List<HkPacketDocument> packets = this.retrievePacketsbyIds(packetIds);
                if (!CollectionUtils.isEmpty(packets)) {
                    for (HkPacketDocument hkPacketDocument : packets) {
                        hkPacketDocument.setIssueReceiveStatus(HkSystemConstantUtil.IssueReceiveStockStatus.PENDING);
                        mongoGenericDao.update(hkPacketDocument);
                    }
                }
            }

            mongoGenericDao.createAll(issueReceiveDocuments);
        }
    }

    @Override
    @MongoTransaction
    public void issueReceiveCollect(List<ObjectId> issueReceiveDocumentsIds, Long userId, Long collectorDepartmentId, Long collectorFranchiseId, Map<ObjectId, Map<String, Object>> fielValsMap, Map<String, String> fieldDbType) {
        if (!CollectionUtils.isEmpty(issueReceiveDocumentsIds)) {
            List<HkIssueReceiveDocument> issueReceiveDocuments = this.retrieveIssueReceiveDocumentsByIds(issueReceiveDocumentsIds);
            List<ObjectId> parcelIds = new ArrayList<>();
            List<ObjectId> lotIds = new ArrayList<>();
            List<ObjectId> packetIds = new ArrayList<>();
            String type = "";
            Calendar calendar = Calendar.getInstance();
            for (HkIssueReceiveDocument hkIssueReceiveDocument : issueReceiveDocuments) {
                hkIssueReceiveDocument.setReceivedCarat(hkIssueReceiveDocument.getIssueCarat());
                hkIssueReceiveDocument.setReceivedPcs(hkIssueReceiveDocument.getIssuePcs());
                if (hkIssueReceiveDocument.getPacket() != null) {
                    HkPacketDocument packet = this.retrievePacketById(hkIssueReceiveDocument.getPacket(), true);
                    HkPacketStockDetailDocument packetStockDetailDocument = new HkPacketStockDetailDocument();
                    packetStockDetailDocument.setDepId(hkIssueReceiveDocument.getSourceDepId());
                    packetStockDetailDocument.setFrenchiseId(hkIssueReceiveDocument.getSourceFranchiseId());
                    packetStockDetailDocument.setCreatedBy(userId);
                    packetStockDetailDocument.setCreatedOn(new Date());
                    this.convertPacketStockDetail(Long.parseLong(hkIssueReceiveDocument.getReceivedPcs().toString()), null, hkIssueReceiveDocument.getReceivedCarat(), packet, HkSystemConstantUtil.RoughStockActions.DEBIT, HkSystemConstantUtil.RoughStockOperations.COLLECT, hkIssueReceiveDocument.getCreatedBy(), packetStockDetailDocument);
                    packetStockDetailDocument = new HkPacketStockDetailDocument();
                    packetStockDetailDocument.setDepId(hkIssueReceiveDocument.getStockDepId());
                    packetStockDetailDocument.setFrenchiseId(hkIssueReceiveDocument.getStockFranchiseId());
                    packetStockDetailDocument.setCreatedBy(userId);
                    packetStockDetailDocument.setCreatedOn(new Date());
                    this.convertPacketStockDetail(Long.parseLong(hkIssueReceiveDocument.getReceivedPcs().toString()), null, hkIssueReceiveDocument.getReceivedCarat(), packet, HkSystemConstantUtil.RoughStockActions.CREDIT, HkSystemConstantUtil.RoughStockOperations.COLLECT, userId, packetStockDetailDocument);
                } else if (hkIssueReceiveDocument.getLot() != null) {
                    HkLotDocument lot = this.retrieveLotById(hkIssueReceiveDocument.getLot());
                    HkLotStockDetailDocument lotStockDetailDocument = new HkLotStockDetailDocument();
                    lotStockDetailDocument.setDepId(hkIssueReceiveDocument.getSourceDepId());
                    lotStockDetailDocument.setFrenchiseId(hkIssueReceiveDocument.getSourceFranchiseId());
                    lotStockDetailDocument.setCreatedBy(userId);
                    lotStockDetailDocument.setCreatedOn(new Date());
                    this.createLotStockDetail(Long.parseLong(hkIssueReceiveDocument.getReceivedPcs().toString()), null, hkIssueReceiveDocument.getReceivedCarat(), lot, HkSystemConstantUtil.RoughStockActions.DEBIT, HkSystemConstantUtil.RoughStockOperations.COLLECT, hkIssueReceiveDocument.getCreatedBy(), null, lotStockDetailDocument);
                    lotStockDetailDocument = new HkLotStockDetailDocument();
                    lotStockDetailDocument.setDepId(hkIssueReceiveDocument.getStockDepId());
                    lotStockDetailDocument.setFrenchiseId(hkIssueReceiveDocument.getStockFranchiseId());
                    lotStockDetailDocument.setCreatedBy(userId);
                    lotStockDetailDocument.setCreatedOn(new Date());
                    this.createLotStockDetail(Long.parseLong(hkIssueReceiveDocument.getReceivedPcs().toString()), null, hkIssueReceiveDocument.getReceivedCarat(), lot, HkSystemConstantUtil.RoughStockActions.CREDIT, HkSystemConstantUtil.RoughStockOperations.COLLECT, userId, null, null);
                } else if (hkIssueReceiveDocument.getParcel() != null) {
                    HkParcelDocument parcel = this.retrieveParcelById(hkIssueReceiveDocument.getParcel());
                    HkRoughStockDetailDocument roughStockDetailDocument = new HkRoughStockDetailDocument();
                    roughStockDetailDocument.setDepId(hkIssueReceiveDocument.getSourceDepId());
                    roughStockDetailDocument.setFrenchiseId(hkIssueReceiveDocument.getSourceFranchiseId());
                    roughStockDetailDocument.setCreatedBy(userId);
                    roughStockDetailDocument.setCreatedOn(new Date());
                    this.convertRoughStockDetail(Long.parseLong(hkIssueReceiveDocument.getReceivedPcs().toString()), null, hkIssueReceiveDocument.getReceivedCarat(), parcel, HkSystemConstantUtil.RoughStockActions.DEBIT, HkSystemConstantUtil.RoughStockOperations.COLLECT, hkIssueReceiveDocument.getCreatedBy(), roughStockDetailDocument, null);
                    roughStockDetailDocument = new HkRoughStockDetailDocument();
                    roughStockDetailDocument.setDepId(hkIssueReceiveDocument.getStockDepId());
                    roughStockDetailDocument.setFrenchiseId(hkIssueReceiveDocument.getStockFranchiseId());
                    roughStockDetailDocument.setCreatedBy(userId);
                    roughStockDetailDocument.setCreatedOn(new Date());
                    this.convertRoughStockDetail(Long.parseLong(hkIssueReceiveDocument.getReceivedPcs().toString()), null, hkIssueReceiveDocument.getReceivedCarat(), parcel, HkSystemConstantUtil.RoughStockActions.CREDIT, HkSystemConstantUtil.RoughStockOperations.COLLECT, userId, roughStockDetailDocument, null);
                }
                hkIssueReceiveDocument.setStatus(HkSystemConstantUtil.IssueReceiveStatus.COLLECTED);
                hkIssueReceiveDocument.setCollectedBy(userId);
                hkIssueReceiveDocument.setCollectedOn(calendar.getTime());

                if (!CollectionUtils.isEmpty(fielValsMap) && fielValsMap.get(hkIssueReceiveDocument.getId()) != null) {
                    BasicBSONObject bSONObject = customFieldService.makeBSONObject(fielValsMap.get(hkIssueReceiveDocument.getId()), fieldDbType, HkSystemConstantUtil.Feature.ISSUE, collectorFranchiseId, null, null);
                    Map map3 = new HashMap();
                    if (hkIssueReceiveDocument.getFieldValue() != null) {
                        map3.putAll(hkIssueReceiveDocument.getFieldValue().toMap());
                    }
                    if (bSONObject != null) {
                        map3.putAll(bSONObject.toMap());
                    }
                    hkIssueReceiveDocument.setFieldValue(new BasicBSONObject(map3));
                }
                if (hkIssueReceiveDocument.getPacket() != null) {
                    packetIds.add(hkIssueReceiveDocument.getPacket());
                } else if (hkIssueReceiveDocument.getLot() != null) {
                    lotIds.add(hkIssueReceiveDocument.getLot());
                } else if (hkIssueReceiveDocument.getParcel() != null) {
                    parcelIds.add(hkIssueReceiveDocument.getParcel());
                }
                hkIssueReceiveDocument.setModifiedOn(calendar.getTime());
                mongoGenericDao.update(hkIssueReceiveDocument);
            }
            if (!CollectionUtils.isEmpty(parcelIds)) {
                List<HkParcelDocument> parcels = this.retrieveParcelsbyIds(parcelIds);
                if (!CollectionUtils.isEmpty(parcels)) {
                    for (HkParcelDocument hkParcelDocument : parcels) {
                        Map toMap = hkParcelDocument.getFieldValue().toMap();
                        toMap.put(HkSystemConstantUtil.ParcelStaticFieldName.IN_STOCK_OF, Arrays.asList(userId + ":E"));
                        BasicBSONObject bSONObject = new BasicBSONObject(toMap);
                        hkParcelDocument.setFieldValue(bSONObject);
                        hkParcelDocument.setModifiedOn(calendar.getTime());
                        mongoGenericDao.update(hkParcelDocument);
                    }
                }
            }

            if (!CollectionUtils.isEmpty(lotIds)) {
                List<HkLotDocument> lots = this.retrieveLotsbyIds(lotIds);
                if (!CollectionUtils.isEmpty(lots)) {
                    for (HkLotDocument hkLotDocument : lots) {
                        Map toMap = hkLotDocument.getFieldValue().toMap();
                        toMap.put(HkSystemConstantUtil.LotStaticFieldName.IN_STOCK_OF, Arrays.asList(userId + ":E"));
                        BasicBSONObject bSONObject = new BasicBSONObject(toMap);
                        hkLotDocument.setFieldValue(bSONObject);
                        hkLotDocument.setModifiedOn(calendar.getTime());
                        mongoGenericDao.update(hkLotDocument);
                    }
                }
            }

            if (!CollectionUtils.isEmpty(packetIds)) {
                List<HkPacketDocument> packets = this.retrievePacketsbyIds(packetIds);
                if (!CollectionUtils.isEmpty(packets)) {
                    for (HkPacketDocument hkPacketDocument : packets) {
                        Map toMap = hkPacketDocument.getFieldValue().toMap();
                        toMap.put(HkSystemConstantUtil.PacketStaticFieldName.IN_STOCK_OF, Arrays.asList(userId + ":E"));
                        BasicBSONObject bSONObject = new BasicBSONObject(toMap);
                        hkPacketDocument.setFieldValue(bSONObject);
                        hkPacketDocument.setModifiedOn(calendar.getTime());
                        mongoGenericDao.update(hkPacketDocument);
                    }
                }
            }

        }
    }

    private HkIssueReceiveDocument retrieveIssueReceiveDocumentsByStock(ObjectId parcelId, ObjectId lotId, ObjectId packetId, Long stockDeptId, Long stockFranchiseId, String requestType) {
        List<Criteria> criterias = new ArrayList<>();
        List<HkIssueReceiveDocument> issueReceiveDocuments = new ArrayList<>();

        criterias.add(Criteria.where(IssueReceiveFields.IS_ACTIVE).is(true));
        if (!StringUtils.isEmpty(requestType) && requestType.equals(HkSystemConstantUtil.IssueReceiveOperations.COLLECT)) {
            criterias.add(Criteria.where(IssueReceiveFields.STATUS).is(HkSystemConstantUtil.IssueReceiveStatus.PENDING));
        } else if (!StringUtils.isEmpty(requestType) && requestType.equals(HkSystemConstantUtil.IssueReceiveOperations.ISSUE)) {
            criterias.add(Criteria.where(IssueReceiveFields.STATUS).is(HkSystemConstantUtil.IssueReceiveStatus.COLLECTED));
        }
        criterias.add(Criteria.where(IssueReceiveFields.TYPE).is(HkSystemConstantUtil.IssueReceiveTypes.REQUEST));
        criterias.add(Criteria.where(IssueReceiveFields.STOCK_DEPT).is(stockDeptId));
        criterias.add(Criteria.where(IssueReceiveFields.STOCK_FRANCHISE_ID).is(stockFranchiseId));

        if (packetId != null) {
            criterias.add(Criteria.where(IssueReceiveFields.PACKET).is(packetId));
        } else if (lotId != null) {
            criterias.add(Criteria.where(IssueReceiveFields.LOT).is(lotId));
        } else if (parcelId != null) {
            criterias.add(Criteria.where(IssueReceiveFields.PARCEL).is(parcelId));

        }

        issueReceiveDocuments = mongoGenericDao.findByCriteria(criterias, HkIssueReceiveDocument.class
        );
        if (!CollectionUtils.isEmpty(issueReceiveDocuments)) {
            return issueReceiveDocuments.get(0);
        }

        return null;
    }

    @Override
    public Map retrieveStockForCollectOrIssue(Map<String, String> stockDetails, Long stockDeptId, Long stockFranchiseId, String requestType) {
        Map result = new HashMap();
        if (!CollectionUtils.isEmpty(stockDetails) && stockDeptId != null && stockFranchiseId != null) {
            if (stockDetails.containsKey(HkSystemConstantUtil.StockName.PACKET)) {
                Map<String, Object> fieldValues = new HashMap<>();
                fieldValues.put(HkSystemConstantUtil.AutoNumber.PACKET_ID, stockDetails.get(HkSystemConstantUtil.StockName.PACKET));
                List<HkPacketDocument> packets = this.retrievePackets(fieldValues, null, null, null, 0, false, null, null, null, null);
                if (!CollectionUtils.isEmpty(packets)) {
                    HkIssueReceiveDocument issueDoc = this.retrieveIssueReceiveDocumentsByStock(null, null, packets.get(0).getId(), stockDeptId, stockFranchiseId, requestType);
                    if (issueDoc != null) {
                        result.put("issueReceiveDocument", issueDoc);
                        result.put("packetNumber", stockDetails.get(HkSystemConstantUtil.StockName.PACKET));
                        HkLotDocument lot = this.retrieveLotById(issueDoc.getLot());
                        if (lot != null) {
                            result.put("lotNumber", lot.getFieldValue().toMap().get(HkSystemConstantUtil.LotStaticFieldName.LOT_ID));
                        }
                        HkParcelDocument parcel = this.retrieveParcelById(issueDoc.getParcel());
                        if (parcel != null) {
                            result.put("parcelNumber", parcel.getFieldValue().toMap().get(HkSystemConstantUtil.ParcelStaticFields.PARCEL_ID));
                        }
                        HkInvoiceDocument invoice = this.retrieveInvoiceById(issueDoc.getInvoice());
                        if (invoice != null) {
                            result.put("invoiceNumber", invoice.getFieldValue().toMap().get(HkSystemConstantUtil.InvoiceStaticFields.INVOICE_ID));
                        }
                    } else {
                        result.put("error", "No record found");
                    }
                } else {
                    result.put("error", "No record found");
                }
            } else if (stockDetails.containsKey(HkSystemConstantUtil.StockName.LOT)) {
                Map<String, Object> fieldValues = new HashMap<>();
                fieldValues.put(HkSystemConstantUtil.AutoNumber.LOT_ID, stockDetails.get(HkSystemConstantUtil.StockName.LOT));
                List<HkLotDocument> lots = this.retrieveLotsByCriteria(fieldValues, null, null, null, null, null, null, false, false);
                if (!CollectionUtils.isEmpty(lots)) {
                    HkIssueReceiveDocument issueDoc = this.retrieveIssueReceiveDocumentsByStock(null, lots.get(0).getId(), null, stockDeptId, stockFranchiseId, requestType);
                    if (issueDoc != null) {
                        result.put("issueReceiveDocument", issueDoc);
                        result.put("lotNumber", stockDetails.get(HkSystemConstantUtil.StockName.LOT));
                        HkParcelDocument parcel = this.retrieveParcelById(issueDoc.getParcel());
                        if (parcel != null) {
                            result.put("parcelNumber", parcel.getFieldValue().toMap().get(HkSystemConstantUtil.ParcelStaticFields.PARCEL_ID));
                        }
                        HkInvoiceDocument invoice = this.retrieveInvoiceById(issueDoc.getInvoice());
                        if (invoice != null) {
                            result.put("invoiceNumber", invoice.getFieldValue().toMap().get(HkSystemConstantUtil.InvoiceStaticFields.INVOICE_ID));
                        }
                    } else {
                        result.put("error", "No record found");
                    }
                } else {
                    result.put("error", "No record found");
                }
            } else if (stockDetails.containsKey(HkSystemConstantUtil.StockName.PARCEL)) {
                Map<String, Object> fieldValues = new HashMap<>();
                fieldValues.put(HkSystemConstantUtil.ParcelStaticFields.PARCEL_ID, stockDetails.get(HkSystemConstantUtil.StockName.PARCEL));
                List<HkParcelDocument> parcels = this.retrieveParcels(fieldValues, null, null, 0, false, null, null, null, null);
                if (!CollectionUtils.isEmpty(parcels)) {
                    HkIssueReceiveDocument issueDoc = this.retrieveIssueReceiveDocumentsByStock(parcels.get(0).getId(), null, null, stockDeptId, stockFranchiseId, requestType);
                    if (issueDoc != null) {
                        result.put("issueReceiveDocument", issueDoc);
                        result.put("parcelNumber", stockDetails.get(HkSystemConstantUtil.StockName.PARCEL));
                        HkInvoiceDocument invoice = this.retrieveInvoiceById(issueDoc.getInvoice());
                        if (invoice != null) {
                            result.put("invoiceNumber", invoice.getFieldValue().toMap().get(HkSystemConstantUtil.InvoiceStaticFields.INVOICE_ID));
                        }
                    } else {
                        result.put("error", "No record found");
                    }
                } else {
                    result.put("error", "No record found");
                }
            }
        }
        return result;
    }

    @Override
    public List<HkIssueReceiveDocument> retrieveIssueReceiveDocumentsByIds(List<ObjectId> objectIds) {
        if (!CollectionUtils.isEmpty(objectIds)) {
            List<Criteria> criterias = new ArrayList<>();

            criterias.add(Criteria.where(IssueReceiveFields.ID).in(objectIds));

            return mongoGenericDao.findByCriteria(criterias, HkIssueReceiveDocument.class
            );
        }
        return null;
    }

    @Override
    @MongoTransaction
    public String issueReceiveIssue(List<ObjectId> issuereceiveIds, Long destinationFranchiseId, Long userId, Map<ObjectId, Long> issueToMap, Map<ObjectId, Map<String, Object>> fieldVals, Map<String, String> fieldDbType) {
        if (!CollectionUtils.isEmpty(issuereceiveIds)) {
            List<HkIssueReceiveDocument> issueReceiveDocs = this.retrieveIssueReceiveDocumentsByIds(issuereceiveIds);
            if (!CollectionUtils.isEmpty(issueReceiveDocs)) {
                DateTime dateTime = new DateTime().withZone(DateTimeZone.UTC).withTime(0, 0, 0, 0);
                HkSlipNoGeneratorDocument slip = this.generateNextSlipNumber(issueReceiveDocs.get(0).getDestinationDepId(), dateTime.toDate());
                Integer slipNo = slip.getSlipNo();
                Date modifiedOn = new Date();
                String franchiseCode = "";
                if (issueReceiveDocs.get(0).getStockFranchiseId() != destinationFranchiseId) {
                    UmCompanyDocument franchise = userService.retrieveFranchiseById(issueReceiveDocs.get(0).getStockFranchiseId());
                    franchiseCode += franchise.getCompanyCode() + "~";
                }
                franchiseCode += slipNo.toString();
                Set<ObjectId> packetIds = new HashSet<>();
                Set<ObjectId> lotIds = new HashSet<>();
                Set<ObjectId> parcelIds = new HashSet<>();
                for (HkIssueReceiveDocument hkIssueReceiveDocument : issueReceiveDocs) {
                    hkIssueReceiveDocument.setStatus(HkSystemConstantUtil.IssueReceiveStatus.ISSUE);
                    hkIssueReceiveDocument.setDestinationFranchiseId(destinationFranchiseId);
                    hkIssueReceiveDocument.setIssuedBy(userId);
                    hkIssueReceiveDocument.setIssuedOn(new Date());
                    hkIssueReceiveDocument.setIssueTo(issueToMap.get(hkIssueReceiveDocument.getId()));
                    hkIssueReceiveDocument.setModifiedBy(userId);
                    hkIssueReceiveDocument.setModifiedOn(dateTime.toDate());
                    hkIssueReceiveDocument.setSlipDate(dateTime.toDate());
                    hkIssueReceiveDocument.setSlipNo(franchiseCode);
                    if (!CollectionUtils.isEmpty(fieldVals) && fieldVals.get(hkIssueReceiveDocument.getId()) != null) {
                        BasicBSONObject bSONObject = customFieldService.makeBSONObject(fieldVals.get(hkIssueReceiveDocument.getId()), fieldDbType, HkSystemConstantUtil.Feature.ISSUE, 0l, null, null);
                        Map map3 = new HashMap();
                        if (hkIssueReceiveDocument.getFieldValue() != null) {
                            map3.putAll(hkIssueReceiveDocument.getFieldValue().toMap());
                        }
                        if (bSONObject != null) {
                            map3.putAll(bSONObject.toMap());
                        }
                        if (!CollectionUtils.isEmpty(map3)) {
                            hkIssueReceiveDocument.setFieldValue(new BasicBSONObject(map3));
                        }
                    }
                    hkIssueReceiveDocument.setModifiedOn(modifiedOn);
                    mongoGenericDao.update(hkIssueReceiveDocument);
                    if (hkIssueReceiveDocument.getStockFranchiseId() != destinationFranchiseId) {
                        if (hkIssueReceiveDocument.getPacket() != null) {
                            packetIds.add(hkIssueReceiveDocument.getPacket());
                            lotIds.add(hkIssueReceiveDocument.getLot());
                            parcelIds.add(hkIssueReceiveDocument.getParcel());
                        } else if (hkIssueReceiveDocument.getLot() != null) {
                            lotIds.add(hkIssueReceiveDocument.getLot());
                            parcelIds.add(hkIssueReceiveDocument.getParcel());
                        } else if (hkIssueReceiveDocument.getParcel() != null) {
                            parcelIds.add(hkIssueReceiveDocument.getParcel());
                        }
                    }
                }
                Calendar calendar = Calendar.getInstance();
                if (!CollectionUtils.isEmpty(packetIds)) {
                    for (ObjectId objectId : packetIds) {
                        HkPacketDocument packet = this.retrievePacketById(objectId, true);
                        if (packet != null) {
                            packet.setIssueToFranchise(destinationFranchiseId);
                            packet.setModifiedOn(calendar.getTime());
                            mongoGenericDao.update(packet);
                        }
                    }
                }
                if (!CollectionUtils.isEmpty(lotIds)) {
                    for (ObjectId objectId : lotIds) {
                        HkLotDocument lot = this.retrieveLotById(objectId);
                        if (lot != null) {
                            lot.setIssueToFranchise(destinationFranchiseId);
                            lot.setModifiedOn(calendar.getTime());
                            mongoGenericDao.update(lot);
                        }
                    }
                }
                if (!CollectionUtils.isEmpty(parcelIds)) {
                    for (ObjectId objectId : parcelIds) {
                        HkParcelDocument parcel = this.retrieveParcelById(objectId);
                        if (parcel != null) {
                            parcel.setIssueToFranchise(destinationFranchiseId);
                            parcel.setModifiedOn(calendar.getTime());
                            mongoGenericDao.update(parcel);
                        }
                    }
                }
                return franchiseCode;
            }
        }
        return null;
    }

    @Override
    @MongoTransaction
    public void issueReceiveReceive(List<ObjectId> stockIds, Long userid, Map<ObjectId, Map<String, Object>> fieldVals, Map<String, String> fieldDbType) {
        if (!CollectionUtils.isEmpty(stockIds)) {
            Calendar calendar = Calendar.getInstance();
            List<HkIssueReceiveDocument> issueReceiveDocs = this.retrieveIssueReceiveDocumentsByIds(stockIds);
            if (!CollectionUtils.isEmpty(issueReceiveDocs)) {

                List<ObjectId> packetIds = new ArrayList<>();
                List<ObjectId> lotIds = new ArrayList<>();
                List<ObjectId> parcelIds = new ArrayList<>();
                for (HkIssueReceiveDocument hkIssueReceiveDocument : issueReceiveDocs) {
                    hkIssueReceiveDocument.setReceiveBy(userid);
                    hkIssueReceiveDocument.setReceivedOn(calendar.getTime());
                    hkIssueReceiveDocument.setStatus(HkSystemConstantUtil.IssueReceiveStatus.COMPLETE);
                    hkIssueReceiveDocument.setIsActive(false);
                    hkIssueReceiveDocument.setModifiedBy(userid);
                    hkIssueReceiveDocument.setModifiedOn(calendar.getTime());
                    if (hkIssueReceiveDocument.getPacket() != null) {
                        HkPacketDocument packet = this.retrievePacketById(hkIssueReceiveDocument.getPacket(), true);
                        HkPacketStockDetailDocument packetStockDetailDocument = new HkPacketStockDetailDocument();
                        packetStockDetailDocument.setDepId(hkIssueReceiveDocument.getStockDepId());
                        packetStockDetailDocument.setFrenchiseId(hkIssueReceiveDocument.getStockFranchiseId());
                        packetStockDetailDocument.setCreatedBy(userid);
                        packetStockDetailDocument.setCreatedOn(new Date());
                        this.convertPacketStockDetail(Long.parseLong(hkIssueReceiveDocument.getReceivedPcs().toString()), null, hkIssueReceiveDocument.getReceivedCarat(), packet, HkSystemConstantUtil.RoughStockActions.DEBIT, HkSystemConstantUtil.RoughStockOperations.RECEIVE, hkIssueReceiveDocument.getCollectedBy(), packetStockDetailDocument);
                        packetStockDetailDocument = new HkPacketStockDetailDocument();
                        packetStockDetailDocument.setDepId(hkIssueReceiveDocument.getDestinationDepId());
                        packetStockDetailDocument.setFrenchiseId(hkIssueReceiveDocument.getDestinationFranchiseId());
                        packetStockDetailDocument.setCreatedBy(userid);
                        packetStockDetailDocument.setCreatedOn(new Date());
                        this.convertPacketStockDetail(Long.parseLong(hkIssueReceiveDocument.getReceivedPcs().toString()), null, hkIssueReceiveDocument.getReceivedCarat(), packet, HkSystemConstantUtil.RoughStockActions.CREDIT, HkSystemConstantUtil.RoughStockOperations.RECEIVE, userid, packetStockDetailDocument);
                    } else if (hkIssueReceiveDocument.getLot() != null) {
                        HkLotDocument lot = this.retrieveLotById(hkIssueReceiveDocument.getLot());
                        HkLotStockDetailDocument lotStockDetailDocument = new HkLotStockDetailDocument();
                        lotStockDetailDocument.setDepId(hkIssueReceiveDocument.getStockDepId());
                        lotStockDetailDocument.setFrenchiseId(hkIssueReceiveDocument.getStockFranchiseId());
                        lotStockDetailDocument.setCreatedBy(userid);
                        lotStockDetailDocument.setCreatedOn(new Date());
                        this.createLotStockDetail(Long.parseLong(hkIssueReceiveDocument.getReceivedPcs().toString()), null, hkIssueReceiveDocument.getReceivedCarat(), lot, HkSystemConstantUtil.RoughStockActions.DEBIT, HkSystemConstantUtil.RoughStockOperations.RECEIVE, hkIssueReceiveDocument.getCollectedBy(), null, lotStockDetailDocument);
                        lotStockDetailDocument = new HkLotStockDetailDocument();
                        lotStockDetailDocument.setDepId(hkIssueReceiveDocument.getDestinationDepId());
                        lotStockDetailDocument.setFrenchiseId(hkIssueReceiveDocument.getDestinationFranchiseId());
                        lotStockDetailDocument.setCreatedBy(userid);
                        lotStockDetailDocument.setCreatedOn(new Date());
                        this.createLotStockDetail(Long.parseLong(hkIssueReceiveDocument.getReceivedPcs().toString()), null, hkIssueReceiveDocument.getReceivedCarat(), lot, HkSystemConstantUtil.RoughStockActions.CREDIT, HkSystemConstantUtil.RoughStockOperations.RECEIVE, userid, null, lotStockDetailDocument);
                    } else if (hkIssueReceiveDocument.getParcel() != null) {
                        HkParcelDocument parcel = this.retrieveParcelById(hkIssueReceiveDocument.getParcel());
                        HkRoughStockDetailDocument roughStockDetailDocument = new HkRoughStockDetailDocument();
                        roughStockDetailDocument.setDepId(hkIssueReceiveDocument.getStockDepId());
                        roughStockDetailDocument.setFrenchiseId(hkIssueReceiveDocument.getStockFranchiseId());
                        roughStockDetailDocument.setCreatedBy(userid);
                        roughStockDetailDocument.setCreatedOn(new Date());
                        this.convertRoughStockDetail(Long.parseLong(hkIssueReceiveDocument.getReceivedPcs().toString()), null, hkIssueReceiveDocument.getReceivedCarat(), parcel, HkSystemConstantUtil.RoughStockActions.DEBIT, HkSystemConstantUtil.RoughStockOperations.RECEIVE, hkIssueReceiveDocument.getCollectedBy(), roughStockDetailDocument, null);
                        roughStockDetailDocument = new HkRoughStockDetailDocument();
                        roughStockDetailDocument.setDepId(hkIssueReceiveDocument.getDestinationDepId());
                        roughStockDetailDocument.setFrenchiseId(hkIssueReceiveDocument.getDestinationFranchiseId());
                        roughStockDetailDocument.setCreatedBy(userid);
                        roughStockDetailDocument.setCreatedOn(new Date());
                        this.convertRoughStockDetail(Long.parseLong(hkIssueReceiveDocument.getReceivedPcs().toString()), null, hkIssueReceiveDocument.getReceivedCarat(), parcel, HkSystemConstantUtil.RoughStockActions.CREDIT, HkSystemConstantUtil.RoughStockOperations.RECEIVE, userid, roughStockDetailDocument, null);
                    }
                    if (hkIssueReceiveDocument.getPacket() != null) {
                        packetIds.add(hkIssueReceiveDocument.getPacket());
                    } else if (hkIssueReceiveDocument.getLot() != null) {
                        lotIds.add(hkIssueReceiveDocument.getLot());
                    } else if (hkIssueReceiveDocument.getParcel() != null) {
                        parcelIds.add(hkIssueReceiveDocument.getParcel());
                    }
                    if (!CollectionUtils.isEmpty(fieldVals) && fieldVals.get(hkIssueReceiveDocument.getId()) != null) {
                        BasicBSONObject bSONObject = customFieldService.makeBSONObject(fieldVals.get(hkIssueReceiveDocument.getId()), fieldDbType, HkSystemConstantUtil.Feature.ISSUE, 0l, null, null);
                        Map map3 = new HashMap();
                        if (hkIssueReceiveDocument.getFieldValue() != null) {
                            map3.putAll(hkIssueReceiveDocument.getFieldValue().toMap());
                        }
                        if (bSONObject != null) {
                            map3.putAll(bSONObject.toMap());
                        }
                        if (!CollectionUtils.isEmpty(map3)) {
                            hkIssueReceiveDocument.setFieldValue(new BasicBSONObject(map3));
                        }
                    }
                    mongoGenericDao.update(hkIssueReceiveDocument);
                }

                if (!CollectionUtils.isEmpty(packetIds)) {
                    for (ObjectId objectId : packetIds) {
                        HkPacketDocument packet = this.retrievePacketById(objectId, true);
                        HkLotDocument lot = this.retrieveLotById(packet.getLot());
                        HkParcelDocument parcel = this.retrieveParcelById(packet.getParcel());
                        if (lot != null) {
                            lot.setIssueToFranchise(null);
                            mongoGenericDao.update(lot);
                        }
                        if (parcel != null) {
                            parcel.setIssueToFranchise(null);
                            mongoGenericDao.update(parcel);
                        }
                        if (packet != null) {
                            packet.setIssueReceiveStatus(HkSystemConstantUtil.IssueReceiveStockStatus.COMPLETED);
                            packet.setModifiedOn(calendar.getTime());
                            Map toMap = packet.getFieldValue().toMap();

                            toMap.put(HkSystemConstantUtil.PacketStaticFieldName.IN_STOCK_OF, Arrays.asList(userid + ":E"));
                            BasicBSONObject bSONObject = new BasicBSONObject(toMap);
                            packet.setFieldValue(bSONObject);
                            packet.setIssueToFranchise(null);
                            mongoGenericDao.update(packet);
                        }
                    }
                }
                if (!CollectionUtils.isEmpty(lotIds)) {
                    for (ObjectId objectId : lotIds) {
                        HkLotDocument lot = this.retrieveLotById(objectId);
                        HkParcelDocument parcel = this.retrieveParcelById(lot.getParcel());
                        if (parcel != null) {
                            parcel.setIssueToFranchise(null);
                            mongoGenericDao.update(parcel);
                        }
                        if (lot != null) {
                            lot.setIssueReceiveStatus(HkSystemConstantUtil.IssueReceiveStockStatus.COMPLETED);
                            lot.setModifiedOn(calendar.getTime());
                            Map toMap = lot.getFieldValue().toMap();

                            toMap.put(HkSystemConstantUtil.LotStaticFieldName.IN_STOCK_OF, Arrays.asList(userid + ":E"));
                            BasicBSONObject bSONObject = new BasicBSONObject(toMap);
                            lot.setFieldValue(bSONObject);
                            lot.setIssueToFranchise(null);
                            mongoGenericDao.update(lot);
                        }
                    }
                }
                if (!CollectionUtils.isEmpty(parcelIds)) {
                    for (ObjectId objectId : parcelIds) {
                        HkParcelDocument parcel = this.retrieveParcelById(objectId);
                        if (parcel != null) {
                            parcel.setIssueReceiveStatus(HkSystemConstantUtil.IssueReceiveStockStatus.COMPLETED);
                            parcel.setModifiedOn(calendar.getTime());
                            Map toMap = parcel.getFieldValue().toMap();

                            toMap.put(HkSystemConstantUtil.ParcelStaticFieldName.IN_STOCK_OF, Arrays.asList(userid + ":E"));
                            BasicBSONObject bSONObject = new BasicBSONObject(toMap);
                            parcel.setFieldValue(bSONObject);
                            parcel.setIssueToFranchise(null);
                            mongoGenericDao.update(parcel);
                        }
                    }
                }
            }
        }
    }

    @Override
    @MongoTransaction
    public Integer issueReceiveInwardIssue(List<HkIssueReceiveDocument> issueReceiveDocuments, Long department, Long destDeptId, Long currentDesignation, Long destDesigId, Long issueTo, Long currentUserid, Long franchiseId) {
        if (!CollectionUtils.isEmpty(issueReceiveDocuments)) {
            Calendar calendar = Calendar.getInstance();
            List<ObjectId> packetIds = new ArrayList<>();
            List<ObjectId> lotIds = new ArrayList<>();
            List<ObjectId> parcelIds = new ArrayList<>();
            DateTime dateTime = new DateTime().withZone(DateTimeZone.UTC).withTime(0, 0, 0, 0);
            HkSlipNoGeneratorDocument slip = this.generateNextSlipNumber(department, dateTime.toDate());
            Integer slipNo = slip.getSlipNo();
            for (HkIssueReceiveDocument hkIssueReceiveDocument : issueReceiveDocuments) {
                if (hkIssueReceiveDocument.getPacket() != null) {
                    HkPacketDocument packet = this.retrievePacketById(hkIssueReceiveDocument.getPacket(), false);
                    HkPacketStockDetailDocument packetStockDetailDocument = new HkPacketStockDetailDocument();
                    packetStockDetailDocument.setDepId(department);
                    packetStockDetailDocument.setCreatedBy(currentUserid);
                    packetStockDetailDocument.setCreatedOn(new Date());
                    this.convertPacketStockDetail(Long.parseLong(hkIssueReceiveDocument.getIssuePcs().toString()), null, hkIssueReceiveDocument.getIssueCarat(), packet, HkSystemConstantUtil.RoughStockActions.DEBIT, HkSystemConstantUtil.RoughStockOperations.DIRECT_ISSUE, currentUserid, packetStockDetailDocument);
                    packetStockDetailDocument = new HkPacketStockDetailDocument();
                    packetStockDetailDocument.setDepId(destDeptId);
                    packetStockDetailDocument.setCreatedBy(currentUserid);
                    packetStockDetailDocument.setCreatedOn(new Date());
                    this.convertPacketStockDetail(Long.parseLong(hkIssueReceiveDocument.getIssuePcs().toString()), null, hkIssueReceiveDocument.getIssueCarat(), packet, HkSystemConstantUtil.RoughStockActions.CREDIT, HkSystemConstantUtil.RoughStockOperations.DIRECT_ISSUE, issueTo, packetStockDetailDocument);
                } else if (hkIssueReceiveDocument.getLot() != null) {
                    HkLotDocument lot = this.retrieveLotById(hkIssueReceiveDocument.getLot());
                    HkLotStockDetailDocument lotStockDetailDocument = new HkLotStockDetailDocument();
                    lotStockDetailDocument.setDepId(department);
                    lotStockDetailDocument.setCreatedBy(currentUserid);
                    lotStockDetailDocument.setCreatedOn(new Date());
                    this.createLotStockDetail(Long.parseLong(hkIssueReceiveDocument.getIssuePcs().toString()), null, hkIssueReceiveDocument.getIssueCarat(), lot, HkSystemConstantUtil.RoughStockActions.DEBIT, HkSystemConstantUtil.RoughStockOperations.DIRECT_ISSUE, currentUserid, null, lotStockDetailDocument);
                    lotStockDetailDocument = new HkLotStockDetailDocument();
                    lotStockDetailDocument.setDepId(destDeptId);
                    lotStockDetailDocument.setCreatedBy(currentUserid);
                    lotStockDetailDocument.setCreatedOn(new Date());
                    this.createLotStockDetail(Long.parseLong(hkIssueReceiveDocument.getIssuePcs().toString()), null, hkIssueReceiveDocument.getIssueCarat(), lot, HkSystemConstantUtil.RoughStockActions.CREDIT, HkSystemConstantUtil.RoughStockOperations.DIRECT_ISSUE, issueTo, null, lotStockDetailDocument);
                } else if (hkIssueReceiveDocument.getParcel() != null) {
                    HkParcelDocument parcel = this.retrieveParcelById(hkIssueReceiveDocument.getParcel());
                    HkRoughStockDetailDocument roughStockDetailDocument = new HkRoughStockDetailDocument();
                    roughStockDetailDocument.setDepId(department);
                    roughStockDetailDocument.setCreatedBy(currentUserid);
                    roughStockDetailDocument.setCreatedOn(new Date());
                    this.convertRoughStockDetail(Long.parseLong(hkIssueReceiveDocument.getIssuePcs().toString()), null, hkIssueReceiveDocument.getIssueCarat(), parcel, HkSystemConstantUtil.RoughStockActions.DEBIT, HkSystemConstantUtil.RoughStockOperations.DIRECT_ISSUE, currentUserid, roughStockDetailDocument, null);
                    roughStockDetailDocument = new HkRoughStockDetailDocument();
                    roughStockDetailDocument.setDepId(destDeptId);
                    roughStockDetailDocument.setCreatedBy(currentUserid);
                    roughStockDetailDocument.setCreatedOn(new Date());
                    this.convertRoughStockDetail(Long.parseLong(hkIssueReceiveDocument.getIssuePcs().toString()), null, hkIssueReceiveDocument.getIssueCarat(), parcel, HkSystemConstantUtil.RoughStockActions.CREDIT, HkSystemConstantUtil.RoughStockOperations.DIRECT_ISSUE, issueTo, roughStockDetailDocument, null);
                }
                hkIssueReceiveDocument.setStatus(HkSystemConstantUtil.IssueReceiveStatus.COMPLETE);
                hkIssueReceiveDocument.setType(HkSystemConstantUtil.IssueReceiveTypes.DIRECT_ISSUE);

                hkIssueReceiveDocument.setSourceFranchiseId(franchiseId);
                hkIssueReceiveDocument.setDestinationFranchiseId(franchiseId);
                hkIssueReceiveDocument.setSourceDepId(department);
                if (destDeptId == null) {
                    hkIssueReceiveDocument.setDestinationDepId(department);
                } else {
                    hkIssueReceiveDocument.setDestinationDepId(destDeptId);
                }
                hkIssueReceiveDocument.setSourceDesignationId(currentDesignation);
                hkIssueReceiveDocument.setDestinationDesignationId(destDesigId);
                hkIssueReceiveDocument.setIssueTo(issueTo);
                hkIssueReceiveDocument.setIssuedBy(currentUserid);
                hkIssueReceiveDocument.setCreatedBy(currentUserid);
                hkIssueReceiveDocument.setCreatedOn(calendar.getTime());
                hkIssueReceiveDocument.setModifiedBy(currentUserid);
                hkIssueReceiveDocument.setModifiedOn(calendar.getTime());
                hkIssueReceiveDocument.setSlipDate(dateTime.toDate());
                hkIssueReceiveDocument.setSlipNo(slipNo.toString());

                if (hkIssueReceiveDocument.getPacket() != null) {
                    packetIds.add(hkIssueReceiveDocument.getPacket());
                } else if (hkIssueReceiveDocument.getLot() != null) {
                    lotIds.add(hkIssueReceiveDocument.getLot());
                } else if (hkIssueReceiveDocument.getParcel() != null) {
                    parcelIds.add(hkIssueReceiveDocument.getParcel());
                }
            }
            if (!CollectionUtils.isEmpty(packetIds)) {
                for (ObjectId objectId : packetIds) {
                    HkPacketDocument packet = this.retrievePacketById(objectId, true);
                    packet.setIssueReceiveStatus(HkSystemConstantUtil.IssueReceiveStockStatus.COMPLETED);
                    packet.setModifiedOn(calendar.getTime());
                    Map toMap = packet.getFieldValue().toMap();

                    toMap.put(HkSystemConstantUtil.PacketStaticFieldName.IN_STOCK_OF, Arrays.asList(issueTo + ":E"));
                    BasicBSONObject bSONObject = new BasicBSONObject(toMap);
                    packet.setFieldValue(bSONObject);

                    mongoGenericDao.update(packet);
                }
            }
            if (!CollectionUtils.isEmpty(lotIds)) {
                for (ObjectId objectId : lotIds) {
                    HkLotDocument lot = this.retrieveLotById(objectId);
                    if (lot != null) {
                        lot.setIssueReceiveStatus(HkSystemConstantUtil.IssueReceiveStockStatus.COMPLETED);
                        lot.setModifiedOn(calendar.getTime());
                        Map toMap = lot.getFieldValue().toMap();

                        toMap.put(HkSystemConstantUtil.LotStaticFieldName.IN_STOCK_OF, Arrays.asList(issueTo + ":E"));
                        BasicBSONObject bSONObject = new BasicBSONObject(toMap);
                        lot.setFieldValue(bSONObject);

                        mongoGenericDao.update(lot);
                    }
                }
            }
            if (!CollectionUtils.isEmpty(parcelIds)) {
                for (ObjectId objectId : parcelIds) {
                    HkParcelDocument parcel = this.retrieveParcelById(objectId);
                    if (parcel != null) {
                        parcel.setIssueReceiveStatus(HkSystemConstantUtil.IssueReceiveStockStatus.COMPLETED);
                        parcel.setModifiedOn(calendar.getTime());
                        Map toMap = parcel.getFieldValue().toMap();

                        toMap.put(HkSystemConstantUtil.ParcelStaticFieldName.IN_STOCK_OF, Arrays.asList(issueTo + ":E"));
                        BasicBSONObject bSONObject = new BasicBSONObject(toMap);
                        parcel.setFieldValue(bSONObject);

                        mongoGenericDao.update(parcel);
                    }
                }
            }
            mongoGenericDao.createAll(issueReceiveDocuments);
            return slipNo;
        }
        return null;
    }

    @Override
    @MongoTransaction
    public void issueReceiveReceiveInward(List<ObjectId> objectIds, Map<ObjectId, Double> objectIdToCarat, Map<ObjectId, Integer> objectIdToPc, Map<ObjectId, Map<String, Object>> fieldVals, Map<String, String> fieldDbType) {
        if (!CollectionUtils.isEmpty(objectIds)) {
            List<HkIssueReceiveDocument> issueReceiveDocs = this.retrieveIssueReceiveDocumentsByIds(objectIds);
            if (!CollectionUtils.isEmpty(issueReceiveDocs)) {
                List<HkIssueReceiveDocument> listToSave = new ArrayList<>();
                Calendar calendar = Calendar.getInstance();
                List<ObjectId> packetIds = new ArrayList<>();
                List<ObjectId> lotIds = new ArrayList<>();
                List<ObjectId> parcelIds = new ArrayList<>();
                Long issueTo = issueReceiveDocs.get(0).getIssuedBy();
                for (HkIssueReceiveDocument hkIssueReceiveDocument : issueReceiveDocs) {
                    HkIssueReceiveDocument receiveDocument = new HkIssueReceiveDocument();
                    if (hkIssueReceiveDocument.getPacket() != null) {
                        HkPacketDocument packet = this.retrievePacketById(hkIssueReceiveDocument.getPacket(), true);
                        HkPacketStockDetailDocument packetStockDetailDocument = new HkPacketStockDetailDocument();
                        packetStockDetailDocument.setDepId(hkIssueReceiveDocument.getDestinationDepId());
                        packetStockDetailDocument.setFrenchiseId(hkIssueReceiveDocument.getDestinationFranchiseId());
                        packetStockDetailDocument.setCreatedBy(hkIssueReceiveDocument.getCreatedBy());
                        packetStockDetailDocument.setCreatedOn(new Date());
                        this.convertPacketStockDetail(Long.parseLong(objectIdToPc.get(hkIssueReceiveDocument.getId()).toString()), null, objectIdToCarat.get(hkIssueReceiveDocument.getId()), packet, HkSystemConstantUtil.RoughStockActions.DEBIT, HkSystemConstantUtil.RoughStockOperations.DIRECT_RECEIVE, hkIssueReceiveDocument.getIssueTo(), packetStockDetailDocument);
                        packetStockDetailDocument = new HkPacketStockDetailDocument();
                        packetStockDetailDocument.setDepId(hkIssueReceiveDocument.getSourceDepId());
                        packetStockDetailDocument.setFrenchiseId(hkIssueReceiveDocument.getSourceFranchiseId());
                        packetStockDetailDocument.setCreatedBy(hkIssueReceiveDocument.getCreatedBy());
                        packetStockDetailDocument.setCreatedOn(new Date());
                        this.convertPacketStockDetail(Long.parseLong(objectIdToPc.get(hkIssueReceiveDocument.getId()).toString()), null, objectIdToCarat.get(hkIssueReceiveDocument.getId()), packet, HkSystemConstantUtil.RoughStockActions.CREDIT, HkSystemConstantUtil.RoughStockOperations.DIRECT_RECEIVE, hkIssueReceiveDocument.getIssuedBy(), packetStockDetailDocument);
                    } else if (hkIssueReceiveDocument.getLot() != null) {
                        HkLotDocument lot = this.retrieveLotById(hkIssueReceiveDocument.getLot());
                        HkLotStockDetailDocument lotStockDetailDocument = new HkLotStockDetailDocument();
                        lotStockDetailDocument.setDepId(hkIssueReceiveDocument.getDestinationDepId());
                        lotStockDetailDocument.setFrenchiseId(hkIssueReceiveDocument.getDestinationFranchiseId());
                        lotStockDetailDocument.setCreatedBy(hkIssueReceiveDocument.getCreatedBy());
                        lotStockDetailDocument.setCreatedOn(new Date());
                        this.createLotStockDetail(Long.parseLong(objectIdToPc.get(hkIssueReceiveDocument.getId()).toString()), null, objectIdToCarat.get(hkIssueReceiveDocument.getId()), lot, HkSystemConstantUtil.RoughStockActions.DEBIT, HkSystemConstantUtil.RoughStockOperations.DIRECT_RECEIVE, hkIssueReceiveDocument.getIssueTo(), null, lotStockDetailDocument);
                        lotStockDetailDocument = new HkLotStockDetailDocument();
                        lotStockDetailDocument.setDepId(hkIssueReceiveDocument.getSourceDepId());
                        lotStockDetailDocument.setFrenchiseId(hkIssueReceiveDocument.getSourceFranchiseId());
                        lotStockDetailDocument.setCreatedBy(hkIssueReceiveDocument.getCreatedBy());
                        lotStockDetailDocument.setCreatedOn(new Date());
                        this.createLotStockDetail(Long.parseLong(objectIdToPc.get(hkIssueReceiveDocument.getId()).toString()), null, objectIdToCarat.get(hkIssueReceiveDocument.getId()), lot, HkSystemConstantUtil.RoughStockActions.CREDIT, HkSystemConstantUtil.RoughStockOperations.DIRECT_RECEIVE, hkIssueReceiveDocument.getIssuedBy(), null, lotStockDetailDocument);
                    } else if (hkIssueReceiveDocument.getParcel() != null) {
                        HkParcelDocument parcel = this.retrieveParcelById(hkIssueReceiveDocument.getParcel());
                        HkRoughStockDetailDocument roughStockDetailDocument = new HkRoughStockDetailDocument();
                        roughStockDetailDocument.setDepId(hkIssueReceiveDocument.getDestinationDepId());
                        roughStockDetailDocument.setFrenchiseId(hkIssueReceiveDocument.getDestinationFranchiseId());
                        roughStockDetailDocument.setCreatedBy(hkIssueReceiveDocument.getCreatedBy());
                        roughStockDetailDocument.setCreatedOn(new Date());
                        this.convertRoughStockDetail(Long.parseLong(objectIdToPc.get(hkIssueReceiveDocument.getId()).toString()), null, objectIdToCarat.get(hkIssueReceiveDocument.getId()), parcel, HkSystemConstantUtil.RoughStockActions.DEBIT, HkSystemConstantUtil.RoughStockOperations.DIRECT_RECEIVE, hkIssueReceiveDocument.getIssueTo(), roughStockDetailDocument, null);
                        roughStockDetailDocument = new HkRoughStockDetailDocument();
                        roughStockDetailDocument.setDepId(hkIssueReceiveDocument.getSourceDepId());
                        roughStockDetailDocument.setFrenchiseId(hkIssueReceiveDocument.getSourceFranchiseId());
                        roughStockDetailDocument.setCreatedBy(hkIssueReceiveDocument.getCreatedBy());
                        roughStockDetailDocument.setCreatedOn(new Date());
                        this.convertRoughStockDetail(Long.parseLong(objectIdToPc.get(hkIssueReceiveDocument.getId()).toString()), null, objectIdToCarat.get(hkIssueReceiveDocument.getId()), parcel, HkSystemConstantUtil.RoughStockActions.CREDIT, HkSystemConstantUtil.RoughStockOperations.DIRECT_RECEIVE, hkIssueReceiveDocument.getIssuedBy(), roughStockDetailDocument, null);
                    }
                    receiveDocument.setInvoice(hkIssueReceiveDocument.getInvoice());
                    receiveDocument.setParcel(hkIssueReceiveDocument.getParcel());
                    receiveDocument.setLot(hkIssueReceiveDocument.getLot());
                    receiveDocument.setPacket(hkIssueReceiveDocument.getPacket());
                    receiveDocument.setType(HkSystemConstantUtil.IssueReceiveTypes.DIRECT_RECEIVE);
                    receiveDocument.setStatus(HkSystemConstantUtil.IssueReceiveStatus.COMPLETE);

                    receiveDocument.setSourceDepId(hkIssueReceiveDocument.getDestinationDepId());
                    receiveDocument.setDestinationDepId(hkIssueReceiveDocument.getSourceDepId());
                    receiveDocument.setSourceFranchiseId(hkIssueReceiveDocument.getDestinationFranchiseId());
                    receiveDocument.setDestinationFranchiseId(hkIssueReceiveDocument.getSourceFranchiseId());

                    receiveDocument.setIssueCarat(hkIssueReceiveDocument.getIssueCarat());
                    receiveDocument.setIssuePcs(hkIssueReceiveDocument.getIssuePcs());

                    receiveDocument.setReceivedCarat(objectIdToCarat.get(hkIssueReceiveDocument.getId()));
                    receiveDocument.setReceivedPcs(objectIdToPc.get(hkIssueReceiveDocument.getId()));

                    receiveDocument.setSlipDate(hkIssueReceiveDocument.getSlipDate());
                    receiveDocument.setSlipNo(hkIssueReceiveDocument.getSlipNo());
                    receiveDocument.setIsActive(Boolean.TRUE);

                    receiveDocument.setIssuedBy(hkIssueReceiveDocument.getIssueTo());
                    receiveDocument.setIssueTo(hkIssueReceiveDocument.getIssuedBy());

                    receiveDocument.setCreatedBy(hkIssueReceiveDocument.getIssuedBy());
                    receiveDocument.setModifiedBy(hkIssueReceiveDocument.getIssuedBy());
                    receiveDocument.setCreatedOn(calendar.getTime());
                    receiveDocument.setModifiedOn(calendar.getTime());
                    receiveDocument.setModifier(hkIssueReceiveDocument.getModifier());

                    List<Criteria> criterias1 = new ArrayList<>();
                    if (hkIssueReceiveDocument.getSlipDate() != null) {
                        DateTime dateTime = new DateTime(hkIssueReceiveDocument.getSlipDate()).withZone(DateTimeZone.UTC).withTime(0, 0, 0, 0);
                        criterias1.add(Criteria.where(IssueReceiveFields.SLIP_DATE).is(dateTime));
                    }
                    criterias1.add(Criteria.where(IssueReceiveFields.IS_ACTIVE).is(true));
                    if (hkIssueReceiveDocument.getSlipNo() != null) {
                        criterias1.add(Criteria.where(IssueReceiveFields.SLIP_NO).is(hkIssueReceiveDocument.getSlipNo()));
                    }
                    criterias1.add(Criteria.where(IssueReceiveFields.STATUS).is(HkSystemConstantUtil.IssueReceiveStatus.COMPLETE));
                    criterias1.add(Criteria.where(IssueReceiveFields.TYPE).is(HkSystemConstantUtil.IssueReceiveTypes.DIRECT_RECEIVE));
                    if (hkIssueReceiveDocument.getPacket() != null) {
                        criterias1.add(Criteria.where(IssueReceiveFields.PACKET).is(hkIssueReceiveDocument.getPacket()));
                    } else if (hkIssueReceiveDocument.getLot() != null) {
                        criterias1.add(Criteria.where(IssueReceiveFields.LOT).is(hkIssueReceiveDocument.getLot()));
                    } else if (hkIssueReceiveDocument.getParcel() != null) {
                        criterias1.add(Criteria.where(IssueReceiveFields.PARCEL).is(hkIssueReceiveDocument.getParcel()));

                    }
                    List<HkIssueReceiveDocument> issuerReceiveDocs1 = mongoGenericDao.findByCriteria(criterias1, HkIssueReceiveDocument.class
                    );
                    Double issuedCarat = hkIssueReceiveDocument.getIssueCarat();
                    Integer issuedPcs = hkIssueReceiveDocument.getIssuePcs();

                    if (!CollectionUtils.isEmpty(issuerReceiveDocs1)) {
                        for (HkIssueReceiveDocument hkIssueReceiveDocument1 : issuerReceiveDocs1) {
                            issuedCarat -= hkIssueReceiveDocument1.getReceivedCarat();
                            issuedPcs -= hkIssueReceiveDocument1.getReceivedPcs();
                        }
                    }

                    issuedCarat -= objectIdToCarat.get(hkIssueReceiveDocument.getId());
                    issuedPcs -= objectIdToPc.get(hkIssueReceiveDocument.getId());

                    if (issuedPcs == 0) {
                        if (hkIssueReceiveDocument.getPacket() != null) {
                            if (issuedCarat > 0) {
                                HkPacketCaratLossDocument hkLossDocument = new HkPacketCaratLossDocument();

                                hkLossDocument.setLossCarat(issuedCarat);
                                hkLossDocument.setPacket(hkIssueReceiveDocument.getPacket());
                                hkLossDocument.setEmpId(receiveDocument.getIssuedBy());
                                hkLossDocument.setDepartmentId(receiveDocument.getSourceDepId());
                                hkLossDocument.setDesignationId(receiveDocument.getSourceDesignationId());
                                hkLossDocument.setCompanyId(receiveDocument.getSourceFranchiseId());
                                hkLossDocument.setCreatedBy(receiveDocument.getIssuedBy());
                                hkLossDocument.setCreatedOn(calendar.getTime());
                                hkLossDocument.setModifiedBy(receiveDocument.getIssuedBy());
                                hkLossDocument.setModifiedOn(calendar.getTime());

                                mongoGenericDao.create(hkLossDocument);
                            }
                            packetIds.add(hkIssueReceiveDocument.getPacket());
                        } else if (hkIssueReceiveDocument.getLot() != null) {
                            lotIds.add(hkIssueReceiveDocument.getLot());
                        } else if (hkIssueReceiveDocument.getParcel() != null) {
                            parcelIds.add(hkIssueReceiveDocument.getParcel());
                        }
                    }

                    if (!CollectionUtils.isEmpty(fieldVals)
                            && fieldVals.get(hkIssueReceiveDocument.getId()) != null) {
                        BasicBSONObject bSONObject = customFieldService.makeBSONObject(fieldVals.get(hkIssueReceiveDocument.getId()), fieldDbType, HkSystemConstantUtil.Feature.ISSUE, 0l, null, null);
                        receiveDocument.setFieldValue(bSONObject);
                    }

                    listToSave.add(receiveDocument);
                }
                mongoGenericDao.createAll(listToSave);
                if (!CollectionUtils.isEmpty(packetIds)) {
                    for (ObjectId objectId : packetIds) {
                        HkPacketDocument packet = this.retrievePacketById(objectId, true);
                        packet.setIssueReceiveStatus(HkSystemConstantUtil.IssueReceiveStockStatus.COMPLETED);
                        packet.setModifiedOn(calendar.getTime());
                        Map toMap = packet.getFieldValue().toMap();

                        toMap.put(HkSystemConstantUtil.PacketStaticFieldName.IN_STOCK_OF, Arrays.asList(issueTo + ":E"));
                        BasicBSONObject bSONObject = new BasicBSONObject(toMap);
                        packet.setFieldValue(bSONObject);

                        mongoGenericDao.update(packet);
                    }
                }
                if (!CollectionUtils.isEmpty(lotIds)) {
                    for (ObjectId objectId : lotIds) {
                        HkLotDocument lot = this.retrieveLotById(objectId);
                        if (lot != null) {
                            lot.setIssueReceiveStatus(HkSystemConstantUtil.IssueReceiveStockStatus.COMPLETED);
                            lot.setModifiedOn(calendar.getTime());
                            Map toMap = lot.getFieldValue().toMap();

                            toMap.put(HkSystemConstantUtil.LotStaticFieldName.IN_STOCK_OF, Arrays.asList(issueTo + ":E"));
                            BasicBSONObject bSONObject = new BasicBSONObject(toMap);
                            lot.setFieldValue(bSONObject);

                            mongoGenericDao.update(lot);
                        }
                    }
                }
                if (!CollectionUtils.isEmpty(parcelIds)) {
                    for (ObjectId objectId : parcelIds) {
                        HkParcelDocument parcel = this.retrieveParcelById(objectId);
                        if (parcel != null) {
                            parcel.setIssueReceiveStatus(HkSystemConstantUtil.IssueReceiveStockStatus.COMPLETED);
                            parcel.setModifiedOn(calendar.getTime());
                            Map toMap = parcel.getFieldValue().toMap();

                            toMap.put(HkSystemConstantUtil.ParcelStaticFieldName.IN_STOCK_OF, Arrays.asList(issueTo + ":E"));
                            BasicBSONObject bSONObject = new BasicBSONObject(toMap);
                            parcel.setFieldValue(bSONObject);

                            mongoGenericDao.update(parcel);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void issueRequestAllotmentRequest(Map<String, List<ObjectId>> stockDetail, Map<ObjectId, Long> issueTo, Long createdByComapnyId, Long createdBy, Long createdByDepartmentId) {
        if (!CollectionUtils.isEmpty(stockDetail)) {

            List<HkParcelDocument> parcels = null;
            List<HkLotDocument> lots = null;
            List<HkPacketDocument> packets = null;
            List<HkIssueReceiveDocument> issuereceiveDocs = new ArrayList<>();

            if (!CollectionUtils.isEmpty(stockDetail.get(HkSystemConstantUtil.StockName.PACKET))) {
                List<ObjectId> packetId = stockDetail.get(HkSystemConstantUtil.StockName.PACKET);
                packets = this.retrievePacketsbyIds(packetId);

                for (HkPacketDocument packet : packets) {
                    Map<String, Object> payload = new HashMap<>();
                    payload.put(HkSystemConstantUtil.StockName.PACKET, packet);
                    HkIssueReceiveDocument issuereceiveDoc = this.prepareIssueRequestAllotmentRequest(payload, createdByComapnyId, createdBy, createdByDepartmentId, issueTo.get(packet.getId()));
                    issuereceiveDocs.add(issuereceiveDoc);
                }
            } else if (!CollectionUtils.isEmpty(stockDetail.get(HkSystemConstantUtil.StockName.LOT))) {
                List<ObjectId> lotId = stockDetail.get(HkSystemConstantUtil.StockName.LOT);
                lots = this.retrieveLotsbyIds(lotId);

                for (HkLotDocument lot : lots) {

                    Map<String, Object> payload = new HashMap<>();
                    payload.put(HkSystemConstantUtil.StockName.LOT, lot);
                    HkIssueReceiveDocument issuereceiveDoc = this.prepareIssueRequestAllotmentRequest(payload, createdByComapnyId, createdBy, createdByDepartmentId, issueTo.get(lot.getId()));
                    issuereceiveDocs.add(issuereceiveDoc);
                }
            } else if (!CollectionUtils.isEmpty(stockDetail.get(HkSystemConstantUtil.StockName.PARCEL))) {
                List<ObjectId> parcelId = stockDetail.get(HkSystemConstantUtil.StockName.PARCEL);
                parcels = this.retrieveParcelsbyIds(parcelId);

                for (HkParcelDocument parcel : parcels) {

                    Map<String, Object> payload = new HashMap<>();
                    payload.put(HkSystemConstantUtil.StockName.PARCEL, parcel);
                    HkIssueReceiveDocument issuereceiveDoc = this.prepareIssueRequestAllotmentRequest(payload, createdByComapnyId, createdBy, createdByDepartmentId, issueTo.get(parcel.getId()));
                    issuereceiveDocs.add(issuereceiveDoc);
                }
            }
            this.issueReceiverequest(issuereceiveDocs, createdByComapnyId);
        }
    }

    private HkIssueReceiveDocument prepareIssueRequestAllotmentRequest(Map<String, Object> stockDetail, Long createdByComapnyId, Long createdBy, Long createdByDepartmentId, Long issueTo) {
        HkIssueReceiveDocument issueReceiveDocument = new HkIssueReceiveDocument();
        Calendar calendar = Calendar.getInstance();
        if (stockDetail.get(HkSystemConstantUtil.StockName.PACKET) != null) {
            HkPacketDocument packet = (HkPacketDocument) stockDetail.get(HkSystemConstantUtil.StockName.PACKET);
            issueReceiveDocument.setPacket(packet.getId());
            issueReceiveDocument.setLot(packet.getLot());
            issueReceiveDocument.setParcel(packet.getParcel());
            issueReceiveDocument.setInvoice(packet.getInvoice());

            if (packet.getFieldValue() != null) {
                Map fieldVals = packet.getFieldValue().toMap();
                if (fieldVals.get(HkSystemConstantUtil.PacketStaticFieldName.CARAT) != null) {
                    issueReceiveDocument.setIssueCarat((Double) fieldVals.get(HkSystemConstantUtil.PacketStaticFieldName.CARAT));
                }
                if (fieldVals.get(HkSystemConstantUtil.PacketStaticFieldName.PIECES) != null) {
                    issueReceiveDocument.setIssuePcs((Integer) fieldVals.get(HkSystemConstantUtil.PacketStaticFieldName.PIECES));
                }
            }
        } else if (stockDetail.get(HkSystemConstantUtil.StockName.LOT) != null) {
            HkLotDocument lot = (HkLotDocument) stockDetail.get(HkSystemConstantUtil.StockName.LOT);
            issueReceiveDocument.setLot(lot.getId());
            issueReceiveDocument.setParcel(lot.getParcel());
            issueReceiveDocument.setInvoice(lot.getInvoice());

            if (lot.getFieldValue() != null) {
                Map fieldVals = lot.getFieldValue().toMap();
                if (fieldVals.get(HkSystemConstantUtil.LotStaticFieldName.CARAT) != null) {
                    issueReceiveDocument.setIssueCarat((Double) fieldVals.get(HkSystemConstantUtil.LotStaticFieldName.CARAT));
                }
                if (fieldVals.get(HkSystemConstantUtil.LotStaticFieldName.PIECES) != null) {
                    issueReceiveDocument.setIssuePcs((Integer) fieldVals.get(HkSystemConstantUtil.LotStaticFieldName.PIECES));
                }
            }
        } else if (stockDetail.get(HkSystemConstantUtil.StockName.PARCEL) != null) {
            HkParcelDocument parcel = (HkParcelDocument) stockDetail.get(HkSystemConstantUtil.StockName.PARCEL);
            issueReceiveDocument.setParcel(parcel.getId());
            issueReceiveDocument.setInvoice(parcel.getInvoice());

            if (parcel.getFieldValue() != null) {
                Map fieldVals = parcel.getFieldValue().toMap();
                if (fieldVals.get(HkSystemConstantUtil.ParcelStaticFieldName.ORIGINAL_CARAT) != null) {
                    issueReceiveDocument.setIssueCarat((Double) fieldVals.get(HkSystemConstantUtil.ParcelStaticFieldName.ORIGINAL_CARAT));
                }
                if (fieldVals.get(HkSystemConstantUtil.ParcelStaticFieldName.ORIGINAL_PIECES) != null) {
                    issueReceiveDocument.setIssuePcs((Integer) fieldVals.get(HkSystemConstantUtil.ParcelStaticFieldName.ORIGINAL_PIECES));
                }
            }
        }
        List<SyncCenterUserDocument> receiver = userService.retrieveUsersByIds(Arrays.asList(issueTo));
        if (!CollectionUtils.isEmpty(receiver)) {
            issueReceiveDocument.setDestinationDepId(receiver.get(0).getDepartmentId());
        }
        issueReceiveDocument.setSourceDepId(createdByDepartmentId);
        issueReceiveDocument.setSourceFranchiseId(createdByComapnyId);

        issueReceiveDocument.setType(HkSystemConstantUtil.IssueReceiveTypes.REQUEST);

        issueReceiveDocument.setCreatedBy(createdBy);
        issueReceiveDocument.setModifiedBy(createdBy);
        issueReceiveDocument.setCreatedOn(calendar.getTime());
        issueReceiveDocument.setModifiedOn(calendar.getTime());

        return issueReceiveDocument;
    }

    private HkSlipNoGeneratorDocument generateNextSlipNumber(Long department, Date slipDate) {
        List<Criteria> criterias = new ArrayList<>();
        criterias.add(Criteria.where(SlipNumberGeneratorFields.SLIP_DATE).is(slipDate));
        criterias.add(Criteria.where(SlipNumberGeneratorFields.DEPARTMENT).is(department));

        HkSlipNoGeneratorDocument slipNos = (HkSlipNoGeneratorDocument) mongoGenericDao.findOneByCriteria(criterias, HkSlipNoGeneratorDocument.class
        );
        if (slipNos
                == null) {
            slipNos = new HkSlipNoGeneratorDocument();
            slipNos.setDepartment(department);
            slipNos.setSlipDate(slipDate);
            slipNos.setSlipNo(1);
            mongoGenericDao.create(slipNos);
        } else {
            slipNos.setSlipNo(slipNos.getSlipNo() + 1);
            mongoGenericDao.update(slipNos);
        }
        return slipNos;

    }

    private boolean equalLists(List<String> one, List<String> two) {
        if (one == null && two == null) {
            return true;
        }

        if ((one == null && two != null)
                || one != null && two == null
                || one.size() != two.size()) {
            return false;
        }

        one = new ArrayList<String>(one);
        two = new ArrayList<String>(two);

        Collections.sort(one);
        Collections.sort(two);
        return one.equals(two);
    }

    @Override
    @MongoTransaction
    public void issueReceiveReject(List<ObjectId> stockIds, Long id, Long departmentId, Long comapanyId) {
        if (!CollectionUtils.isEmpty(stockIds)) {
            List<Criteria> criterias = new ArrayList<>();

            criterias.add(Criteria.where(IssueReceiveFields.ID).in(stockIds));
            criterias.add(Criteria.where(IssueReceiveFields.STOCK_DEPT).is(departmentId));
            criterias.add(Criteria.where(IssueReceiveFields.STOCK_FRANCHISE_ID).is(comapanyId));

            List<HkIssueReceiveDocument> issueReceiveDocs = mongoGenericDao.findByCriteria(criterias, HkIssueReceiveDocument.class
            );
            if (!CollectionUtils.isEmpty(issueReceiveDocs)) {
                List<ObjectId> packetIds = new ArrayList<>();
                List<ObjectId> lotIds = new ArrayList<>();
                List<ObjectId> parcelIds = new ArrayList<>();
                Map<ObjectId, Long> packetToIssuedBy = new HashMap<>();
                Map<ObjectId, Long> lotToIssuedBy = new HashMap<>();
                Map<ObjectId, Long> parcelToIssuedBy = new HashMap<>();

                Calendar calendar = Calendar.getInstance();
                Date today = calendar.getTime();

                for (HkIssueReceiveDocument hkIssueReceiveDocument : issueReceiveDocs) {
                    if (hkIssueReceiveDocument.getPacket() != null) {
                        packetIds.add(hkIssueReceiveDocument.getPacket());
                        packetToIssuedBy.put(hkIssueReceiveDocument.getPacket(), hkIssueReceiveDocument.getCreatedBy());
                    } else if (hkIssueReceiveDocument.getLot() != null) {
                        lotIds.add(hkIssueReceiveDocument.getLot());
                        lotToIssuedBy.put(hkIssueReceiveDocument.getLot(), hkIssueReceiveDocument.getCreatedBy());
                    } else if (hkIssueReceiveDocument.getParcel() != null) {
                        parcelIds.add(hkIssueReceiveDocument.getParcel());
                        parcelToIssuedBy.put(hkIssueReceiveDocument.getParcel(), hkIssueReceiveDocument.getCreatedBy());
                    }

                    hkIssueReceiveDocument.setStatus(HkSystemConstantUtil.IssueReceiveStatus.REJECTED);
                    hkIssueReceiveDocument.setModifiedBy(id);
                    hkIssueReceiveDocument.setModifiedOn(today);
                    mongoGenericDao.update(hkIssueReceiveDocument);
                }

                if (!CollectionUtils.isEmpty(packetIds) && !CollectionUtils.isEmpty(packetToIssuedBy)) {
                    List<HkPacketDocument> packets = this.retrievePacketsbyIds(packetIds);
                    if (!CollectionUtils.isEmpty(packets)) {
                        for (HkPacketDocument hkPacketDocument : packets) {
                            Map fieldValue = hkPacketDocument.getFieldValue().toMap();
                            if (packetToIssuedBy.get(hkPacketDocument.getId()) != null) {
                                fieldValue.put(HkSystemConstantUtil.PacketStaticFieldName.IN_STOCK_OF, Arrays.asList(packetToIssuedBy.get(hkPacketDocument.getId()) + ":E"));
                            }
                            hkPacketDocument.setFieldValue(new BasicBSONObject(fieldValue));
                            hkPacketDocument.setIssueReceiveStatus(HkSystemConstantUtil.IssueReceiveStockStatus.REJECTED);
                            hkPacketDocument.setModifiedOn(today);
                            mongoGenericDao.update(hkPacketDocument);
                        }
                    }
                }

                if (!CollectionUtils.isEmpty(lotIds) && !CollectionUtils.isEmpty(lotToIssuedBy)) {
                    List<HkLotDocument> lots = this.retrieveLotsbyIds(lotIds);
                    if (!CollectionUtils.isEmpty(lots)) {
                        for (HkLotDocument hkLotDocument : lots) {
                            Map fieldValue = hkLotDocument.getFieldValue().toMap();
                            if (lotToIssuedBy.get(hkLotDocument.getId()) != null) {
                                fieldValue.put(HkSystemConstantUtil.LotStaticFieldName.IN_STOCK_OF, Arrays.asList(lotToIssuedBy.get(hkLotDocument.getId()) + ":E"));
                            }
                            hkLotDocument.setFieldValue(new BasicBSONObject(fieldValue));
                            hkLotDocument.setIssueReceiveStatus(HkSystemConstantUtil.IssueReceiveStockStatus.REJECTED);
                            hkLotDocument.setModifiedOn(today);
                            mongoGenericDao.update(hkLotDocument);
                        }
                    }
                }

                if (!CollectionUtils.isEmpty(parcelIds) && !CollectionUtils.isEmpty(parcelToIssuedBy)) {
                    List<HkParcelDocument> parcels = this.retrieveParcelsbyIds(parcelIds);
                    if (!CollectionUtils.isEmpty(parcels)) {
                        for (HkParcelDocument hkParcelDocument : parcels) {
                            Map fieldValue = hkParcelDocument.getFieldValue().toMap();
                            if (parcelToIssuedBy.get(hkParcelDocument.getId()) != null) {
                                fieldValue.put(HkSystemConstantUtil.ParcelStaticFieldName.IN_STOCK_OF, Arrays.asList(parcelToIssuedBy.get(hkParcelDocument.getId()) + ":E"));
                            }
                            hkParcelDocument.setFieldValue(new BasicBSONObject(fieldValue));
                            hkParcelDocument.setIssueReceiveStatus(HkSystemConstantUtil.IssueReceiveStockStatus.REJECTED);
                            hkParcelDocument.setModifiedOn(today);
                            mongoGenericDao.update(hkParcelDocument);
                        }
                    }
                }
            }
        }
    }

    @Override
    public List<HkIssueReceiveDocument> retrieveIssueReceiveDocumentsForReturn(Long id, Long department, Long companyId) {
        List<Criteria> criterias = new ArrayList<>();
        criterias.add(Criteria.where(IssueReceiveFields.STATUS).is(HkSystemConstantUtil.IssueReceiveStatus.COMPLETE));
        criterias.add(Criteria.where(IssueReceiveFields.TYPE).is(HkSystemConstantUtil.IssueReceiveTypes.REQUEST));
        criterias.add(Criteria.where(IssueReceiveFields.DESTINATION_DEPT).is(department));
        criterias.add(Criteria.where(IssueReceiveFields.DESTINATION_FRANCHISE_ID).is(companyId));

        List<HkIssueReceiveDocument> issueReceiveDocuments = mongoGenericDao.findByCriteria(criterias, HkIssueReceiveDocument.class
        );

        List<HkIssueReceiveDocument> result = new ArrayList<>();
        Map<String, Map<ObjectId, HkIssueReceiveDocument>> map = new HashMap<>();

        if (!CollectionUtils.isEmpty(issueReceiveDocuments)) {
            for (HkIssueReceiveDocument hkIssueReceiveDocument : issueReceiveDocuments) {
                if (hkIssueReceiveDocument.getPacket() != null) {
                    HkPacketDocument packet = this.retrievePacketById(hkIssueReceiveDocument.getPacket(), true);
                    List<String> Array = new ArrayList<>();
                    if (packet.getFieldValue() != null) {
                        Array = new ArrayList<>();
                        Array.add(id.toString() + ":E");

                        String value = packet.getFieldValue().get(HkSystemConstantUtil.PacketStaticFieldName.IN_STOCK_OF).toString();
                        List<String> values = new ArrayList<>();
                        String[] valueArray = value.replace("\"", "").split(",");
                        for (String v : valueArray) {
                            values.add(v.replace("\"", ""));
                        }
                        if (this.equalLists(values, Array) && (packet.getIssueReceiveStatus() == null || !packet.getIssueReceiveStatus().equals(HkSystemConstantUtil.IssueReceiveStockStatus.PENDING))) {
                            if (map.get(HkSystemConstantUtil.StockName.PACKET) == null) {
                                result.add(hkIssueReceiveDocument);
                                map.put(HkSystemConstantUtil.StockName.PACKET, new HashMap<>());
                                map.get(HkSystemConstantUtil.StockName.PACKET).put(packet.getId(), hkIssueReceiveDocument);
                            } else if (map.get(HkSystemConstantUtil.StockName.PACKET).get(packet.getId()) == null) {
                                result.add(hkIssueReceiveDocument);
                                map.get(HkSystemConstantUtil.StockName.PACKET).put(packet.getId(), hkIssueReceiveDocument);
                            } else {
                                HkIssueReceiveDocument hkIssue = map.get(HkSystemConstantUtil.StockName.PACKET).get(packet.getId());
                                if (hkIssue.getCreatedOn().after(hkIssueReceiveDocument.getCreatedOn())) {
                                    result.add(hkIssueReceiveDocument);
                                    map.get(HkSystemConstantUtil.StockName.PACKET).put(packet.getId(), hkIssueReceiveDocument);
                                }
                            }

                        }
                    }
                } else if (hkIssueReceiveDocument.getLot() != null) {
                    HkLotDocument lot = this.retrieveLotById(hkIssueReceiveDocument.getLot());
                    List<String> Array = new ArrayList<>();
                    if (lot.getFieldValue() != null) {
                        Array = new ArrayList<>();
                        Array.add(id.toString() + ":E");

                        String value = lot.getFieldValue().get(HkSystemConstantUtil.LotStaticFieldName.IN_STOCK_OF).toString();
                        List<String> values = new ArrayList<>();
                        String[] valueArray = value.replace("\"", "").split(",");
                        for (String v : valueArray) {
                            values.add(v.replace("\"", ""));
                        }
                        if (this.equalLists(values, Array) && (lot.getIssueReceiveStatus() == null || !lot.getIssueReceiveStatus().equals(HkSystemConstantUtil.IssueReceiveStockStatus.PENDING))) {
                            if (map.get(HkSystemConstantUtil.StockName.LOT) == null) {
                                result.add(hkIssueReceiveDocument);
                                map.put(HkSystemConstantUtil.StockName.LOT, new HashMap<>());
                                map.get(HkSystemConstantUtil.StockName.LOT).put(lot.getId(), hkIssueReceiveDocument);
                            } else if (map.get(HkSystemConstantUtil.StockName.LOT).get(lot.getId()) == null) {
                                result.add(hkIssueReceiveDocument);
                                map.get(HkSystemConstantUtil.StockName.LOT).put(lot.getId(), hkIssueReceiveDocument);
                            } else {
                                HkIssueReceiveDocument hkIssue = map.get(HkSystemConstantUtil.StockName.LOT).get(lot.getId());
                                if (hkIssue.getCreatedOn().after(hkIssueReceiveDocument.getCreatedOn())) {
                                    result.add(hkIssueReceiveDocument);
                                    map.get(HkSystemConstantUtil.StockName.LOT).put(lot.getId(), hkIssueReceiveDocument);
                                }
                            }
                        }
                    }
                } else if (hkIssueReceiveDocument.getParcel() != null) {
                    HkParcelDocument parcel = this.retrieveParcelById(hkIssueReceiveDocument.getParcel());
                    List<String> Array = new ArrayList<>();
                    if (parcel.getFieldValue() != null) {
                        Array = new ArrayList<>();
                        Array.add(id.toString() + ":E");

                        String value = parcel.getFieldValue().get(HkSystemConstantUtil.ParcelStaticFieldName.IN_STOCK_OF).toString();
                        List<String> values = new ArrayList<>();
                        String[] valueArray = value.replace("\"", "").split(",");
                        for (String v : valueArray) {
                            values.add(v.replace("\"", ""));
                        }
                        if (this.equalLists(values, Array) && (parcel.getIssueReceiveStatus() == null || !parcel.getIssueReceiveStatus().equals(HkSystemConstantUtil.IssueReceiveStockStatus.PENDING))) {
                            if (map.get(HkSystemConstantUtil.StockName.PARCEL) == null) {
                                result.add(hkIssueReceiveDocument);
                                map.put(HkSystemConstantUtil.StockName.PARCEL, new HashMap<>());
                                map.get(HkSystemConstantUtil.StockName.PARCEL).put(parcel.getId(), hkIssueReceiveDocument);
                            } else if (map.get(HkSystemConstantUtil.StockName.PARCEL).get(parcel.getId()) == null) {
                                result.add(hkIssueReceiveDocument);
                                map.get(HkSystemConstantUtil.StockName.PARCEL).put(parcel.getId(), hkIssueReceiveDocument);
                            } else {
                                HkIssueReceiveDocument hkIssue = map.get(HkSystemConstantUtil.StockName.PARCEL).get(parcel.getId());
                                if (hkIssue.getCreatedOn().after(hkIssueReceiveDocument.getCreatedOn())) {
                                    result.add(hkIssueReceiveDocument);
                                    map.get(HkSystemConstantUtil.StockName.PARCEL).put(parcel.getId(), hkIssueReceiveDocument);
                                }
                            }
                        }
                    }
                }
            }
        }
        return result;
    }

    @Override
    public void returnStock(List<ObjectId> objectIds, Long companyId, Long userId, Long designation) {
        if (!CollectionUtils.isEmpty(objectIds)) {
            List<HkIssueReceiveDocument> issuereceiveDocs = this.retrieveIssueReceiveDocumentsByIds(objectIds);
            if (!CollectionUtils.isEmpty(issuereceiveDocs)) {
                List<HkIssueReceiveDocument> listToSave = new ArrayList<>();
                Calendar calendar = Calendar.getInstance();
                Date today = calendar.getTime();
                for (HkIssueReceiveDocument issueReceiveDocument : issuereceiveDocs) {
                    HkIssueReceiveDocument hkIssueReceiveDocument = new HkIssueReceiveDocument();

                    hkIssueReceiveDocument.setInvoice(issueReceiveDocument.getInvoice());
                    hkIssueReceiveDocument.setParcel(issueReceiveDocument.getParcel());
                    hkIssueReceiveDocument.setLot(issueReceiveDocument.getLot());
                    hkIssueReceiveDocument.setPacket(issueReceiveDocument.getPacket());

                    hkIssueReceiveDocument.setSourceDepId(issueReceiveDocument.getDestinationDepId());
                    hkIssueReceiveDocument.setDestinationDepId(issueReceiveDocument.getSourceDepId());

                    hkIssueReceiveDocument.setSourceDesignationId(designation);
                    hkIssueReceiveDocument.setDestinationDesignationId(issueReceiveDocument.getSourceDesignationId());
                    hkIssueReceiveDocument.setModifier(issueReceiveDocument.getModifier());
                    hkIssueReceiveDocument.setType(issueReceiveDocument.getType());
                    hkIssueReceiveDocument.setModifiedBy(userId);
                    hkIssueReceiveDocument.setModifiedOn(today);
                    hkIssueReceiveDocument.setCreatedBy(userId);
                    hkIssueReceiveDocument.setCreatedOn(today);
                    hkIssueReceiveDocument.setIssueCarat(issueReceiveDocument.getIssueCarat());
                    hkIssueReceiveDocument.setIssuePcs(issueReceiveDocument.getIssuePcs());

                    listToSave.add(hkIssueReceiveDocument);
                }

                this.issueReceiverequest(listToSave, companyId);
            }
        }
    }

    @Override
    public List<HkIssueReceiveDocument> retrievePendingIssuedStock(Long userId, Long department, Long companyId, String requestType) {
        List<Criteria> criterias = new ArrayList<>();

        criterias.add(Criteria.where(IssueReceiveFields.TYPE).is(HkSystemConstantUtil.IssueReceiveTypes.REQUEST));
        criterias.add(Criteria.where(IssueReceiveFields.STATUS).is(HkSystemConstantUtil.IssueReceiveStatus.ISSUE));
        criterias.add(Criteria.where(IssueReceiveFields.IS_ACTIVE).is(Boolean.TRUE));

        if (requestType.equals(HkSystemConstantUtil.IssueReceiveOperations.ISSUE)) {
            criterias.add(Criteria.where(IssueReceiveFields.ISSUE_BY).is(userId));
        } else if (requestType.equals(HkSystemConstantUtil.IssueReceiveOperations.RECEIVE)) {

            criterias.add(Criteria.where(IssueReceiveFields.DESTINATION_FRANCHISE_ID).is(companyId));
            criterias.add(Criteria.where(IssueReceiveFields.DESTINATION_DEPT).is(department));
            criterias.add(new Criteria()
                    .orOperator(Criteria.where(IssueReceiveFields.ISSUE_TO).exists(false),
                            Criteria.where(IssueReceiveFields.ISSUE_TO).is(""),
                            Criteria.where(IssueReceiveFields.ISSUE_TO).is(userId)));

        }

        return mongoGenericDao.findByCriteria(criterias, HkIssueReceiveDocument.class
        );
    }

    @Override
    public List<HkIssueReceiveDocument> retrieveAvailableStockToRequest(Long userId, Long companyId, Long designationId) {
        List<HkIssueReceiveDocument> result = new ArrayList<>();
        SyncCenterFeatureDocument featureDoc = featureService.retireveFeatureByName(HkSystemConstantUtil.Feature.ISSUERECEIVE);
        List<HkRoleFeatureModifierDocument> modifiers = configurationService.retrieveModifiersByDesignations(Arrays.asList(designationId), companyId, featureDoc.getId());
        if (!CollectionUtils.isEmpty(modifiers)) {
            HkRoleFeatureModifierDocument modifierDocument = modifiers.get(0);
            if (!CollectionUtils.isEmpty(modifierDocument.getiRMediums())) {
                Map<String, Object> fieldValues = new HashMap<>();
                List<String> Array = null;
                List<HkParcelDocument> parcels = null;
                List<HkLotDocument> lots = null;
                List<HkPacketDocument> packets = null;
                Array = new ArrayList<>();
                Array.add(userId.toString() + ":E");

                if (modifierDocument.getiRMediums().contains(HkSystemConstantUtil.Modifiers.Medium.ROUGH)) {
                    fieldValues.put(HkSystemConstantUtil.ParcelStaticFieldName.IN_STOCK_OF, Array);
                    parcels = this.retrieveParcels(fieldValues, null, null, companyId, false, null, null, null, null);
                }
                if (modifierDocument.getiRMediums().contains(HkSystemConstantUtil.Modifiers.Medium.LOT)) {
                    fieldValues = new HashMap<>();
                    fieldValues.put(HkSystemConstantUtil.LotStaticFieldName.IN_STOCK_OF, Array);
                    lots = this.retrieveLots(fieldValues, null, null, null, companyId, false);
                }
                if (modifierDocument.getiRMediums().contains(HkSystemConstantUtil.Modifiers.Medium.PACKET)) {
                    fieldValues = new HashMap<>();
                    fieldValues.put(HkSystemConstantUtil.PacketStaticFieldName.IN_STOCK_OF, Array);
                    packets = this.retrievePackets(fieldValues, null, null, null, companyId, false, null, null, null, null);
                }
                if (!CollectionUtils.isEmpty(parcels)) {
                    for (HkParcelDocument hkParcelDocument : parcels) {
                        if (hkParcelDocument.getIssueReceiveStatus() == null || !hkParcelDocument.getIssueReceiveStatus().equalsIgnoreCase(HkSystemConstantUtil.IssueReceiveStockStatus.PENDING)) {
                            HkIssueReceiveDocument hkIssueReceiveDocument = new HkIssueReceiveDocument();
                            hkIssueReceiveDocument.setParcel(hkParcelDocument.getId());
                            hkIssueReceiveDocument.setInvoice(hkParcelDocument.getInvoice());
                            if (hkParcelDocument.getFieldValue().get(HkSystemConstantUtil.ParcelStaticFieldName.ORIGINAL_CARAT) != null) {
                                hkIssueReceiveDocument.setIssueCarat(Double.valueOf(hkParcelDocument.getFieldValue().get(HkSystemConstantUtil.ParcelStaticFieldName.ORIGINAL_CARAT).toString()));
                            }
                            if (hkParcelDocument.getFieldValue().get(HkSystemConstantUtil.ParcelStaticFieldName.ORIGINAL_PIECES) != null) {
                                hkIssueReceiveDocument.setIssuePcs(Integer.valueOf(hkParcelDocument.getFieldValue().get(HkSystemConstantUtil.ParcelStaticFieldName.ORIGINAL_PIECES).toString()));
                            }
                            result.add(hkIssueReceiveDocument);
                        }
                    }
                }
                if (!CollectionUtils.isEmpty(lots)) {
                    for (HkLotDocument hkLotDocument : lots) {
                        if (hkLotDocument.getIssueReceiveStatus() == null || !hkLotDocument.getIssueReceiveStatus().equalsIgnoreCase(HkSystemConstantUtil.IssueReceiveStockStatus.PENDING)) {
                            HkIssueReceiveDocument hkIssueReceiveDocument = new HkIssueReceiveDocument();
                            hkIssueReceiveDocument.setLot(hkLotDocument.getId());
                            hkIssueReceiveDocument.setParcel(hkLotDocument.getParcel());
                            hkIssueReceiveDocument.setInvoice(hkLotDocument.getInvoice());
                            if (hkLotDocument.getFieldValue().get(HkSystemConstantUtil.LotStaticFieldName.CARAT) != null) {
                                hkIssueReceiveDocument.setIssueCarat(Double.valueOf(hkLotDocument.getFieldValue().get(HkSystemConstantUtil.LotStaticFieldName.CARAT).toString()));
                            }
                            if (hkLotDocument.getFieldValue().get(HkSystemConstantUtil.LotStaticFieldName.PIECES) != null) {
                                hkIssueReceiveDocument.setIssuePcs(Integer.valueOf(hkLotDocument.getFieldValue().get(HkSystemConstantUtil.LotStaticFieldName.PIECES).toString()));
                            }
                            result.add(hkIssueReceiveDocument);
                        }
                    }
                }
                if (!CollectionUtils.isEmpty(packets)) {
                    for (HkPacketDocument hkPacketDocument : packets) {
                        if (hkPacketDocument.getIssueReceiveStatus() == null || !hkPacketDocument.getIssueReceiveStatus().equalsIgnoreCase(HkSystemConstantUtil.IssueReceiveStockStatus.PENDING)) {
                            HkIssueReceiveDocument hkIssueReceiveDocument = new HkIssueReceiveDocument();
                            hkIssueReceiveDocument.setPacket(hkPacketDocument.getId());
                            hkIssueReceiveDocument.setLot(hkPacketDocument.getLot());
                            hkIssueReceiveDocument.setParcel(hkPacketDocument.getParcel());
                            hkIssueReceiveDocument.setInvoice(hkPacketDocument.getInvoice());
                            if (hkPacketDocument.getFieldValue().get(HkSystemConstantUtil.PacketStaticFieldName.CARAT) != null) {
                                hkIssueReceiveDocument.setIssueCarat(Double.valueOf(hkPacketDocument.getFieldValue().get(HkSystemConstantUtil.PacketStaticFieldName.CARAT).toString()));
                            }
                            if (hkPacketDocument.getFieldValue().get(HkSystemConstantUtil.PacketStaticFieldName.PIECES) != null) {
                                hkIssueReceiveDocument.setIssuePcs(Integer.valueOf(hkPacketDocument.getFieldValue().get(HkSystemConstantUtil.PacketStaticFieldName.PIECES).toString()));
                            }
                            result.add(hkIssueReceiveDocument);
                        }
                    }
                }
            }

        }

        return result;
    }

    @Override
    public Integer getNextPurchaseSequence() {
        Integer sequenceNumber = 0;
        Query query = new Query();
        query.addCriteria(Criteria.where(PurchaseFields.IS_ARCHIVE).is(Boolean.FALSE));
        query.addCriteria(Criteria.where(PurchaseFields.YEAR).is(Calendar.getInstance().get(Calendar.YEAR)));
        query.with(new Sort(Sort.Direction.DESC, PurchaseFields.SEQUENCE_NUMBER)).limit(1);
        HkPurchaseDocument purchaseDocument = mongoGenericDao.getMongoTemplate().findOne(query, HkPurchaseDocument.class
        );
        if (purchaseDocument
                != null) {
            sequenceNumber = purchaseDocument.getSequenceNumber();
        }
        return sequenceNumber;
    }

    @Override
    public Boolean isPurchaseSequenceExist(Integer purchaseNumber, ObjectId purchaseObjectId) {
        Boolean invoiceExist = Boolean.FALSE;
        List<HkPurchaseDocument> purchaseDocuments = new ArrayList<>();
        List<Criteria> criterias = new ArrayList<>();
//        criterias.add(Criteria.where(LotFields.YEAR).is(Calendar.getInstance().get(Calendar.YEAR)));
        if (purchaseNumber != null) {
            criterias.add(Criteria.where(PurchaseFields.SEQUENCE_NUMBER).is(purchaseNumber));
        }
        if (purchaseObjectId != null) {
            criterias.add(Criteria.where(PurchaseFields.OBJECT_ID).ne(purchaseObjectId));
        }
        criterias.add(Criteria.where(PurchaseFields.IS_ARCHIVE).is(Boolean.FALSE));
        purchaseDocuments
                = mongoGenericDao.findByCriteria(criterias, HkPurchaseDocument.class
                );
        if (!CollectionUtils.isEmpty(purchaseDocuments)) {
            invoiceExist = Boolean.TRUE;
        }
        return invoiceExist;
    }

    @MongoTransaction
    @Override
    public String savePurchase(BasicBSONObject fieldValue, Long franchise, Long createdBy, Integer purchaseSequence) {

        HkPurchaseDocument purchaseDocument = new HkPurchaseDocument();
        purchaseDocument.setFranchiseId(0L);
        purchaseDocument.setCreatedByFranchise(franchise);
        purchaseDocument.setIsArchive(Boolean.FALSE);
        purchaseDocument.setCreatedBy(createdBy);
        purchaseDocument.setLastModifiedBy(createdBy);
        purchaseDocument.setCreatedOn(new Date());
        purchaseDocument.setLastModifiedOn(new Date());
        if (purchaseSequence == null) {
            purchaseSequence = this.getNextPurchaseSequence();
            purchaseSequence++;
        }
        purchaseDocument.setYear(Calendar.getInstance().get(Calendar.YEAR));
        purchaseDocument.setSequenceNumber(purchaseSequence);

        if (fieldValue != null) {
            if (!fieldValue.containsField(HkSystemConstantUtil.RoughPurchaseStaticFields.ROUGH_PURCHASE_ID)) {
                fieldValue.put(HkSystemConstantUtil.RoughPurchaseStaticFields.ROUGH_PURCHASE_ID, Calendar.getInstance().get(Calendar.YEAR) + "-" + purchaseSequence);
            }
            purchaseDocument.setFieldValue(fieldValue);
        }
        mongoGenericDao.create(purchaseDocument);
        return purchaseDocument.getId().toString();
    }

    @Override
    public List<HkPurchaseDocument> retrievePurchases(Map<String, Object> fieldValues, List<ObjectId> purchaseIds, long franchise, Boolean isArchieve, Integer offset, Integer limit) {
        Query query = new Query();
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -1);
        Date afterDate = cal.getTime();
        List<HkPurchaseDocument> purchaseDocuments = null;
        Map<String, String> uiFieldMap = null;
        Map<String, Object> dateCriteriaMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(fieldValues)) {
            uiFieldMap = this.createFieldNameWithComponentType(fieldValues);

            for (Map.Entry<String, Object> entry : fieldValues.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                if (uiFieldMap.containsKey(key)) {
                    String cType = uiFieldMap.get(key);
                    // Specific logic for Date type component
                    if (cType.equals(HkSystemConstantUtil.CustomField.ComponentType.DATE)) {
                        if (value != null) {
                            SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS\'Z\'");
                            formatDate.setTimeZone(TimeZone.getTimeZone("UTC"));
                            Date parseDate = null;
                            Date parseIncDate = null;
                            try {
                                // parseDate gives the formatted date
                                parseDate = formatDate.parse(value.toString());
                                Calendar c = Calendar.getInstance();
                                c.setTime(parseDate);
                                c.add(Calendar.DATE, 1);
                                // incremented date contains next date i.e the current date +1
//                                String incrementedDate = formatDate.format(c.getTime()); // new date
                                parseIncDate = new Date(c.getTimeInMillis());

                            } catch (ParseException ex) {
                                Logger.getLogger(HkStockServiceImpl.class
                                        .getName()).log(Level.SEVERE, null, ex);
                            }
                            if (!(key.startsWith("from") || key.startsWith("to"))) {
                                query.addCriteria(Criteria.where(PurchaseFields.FIELD_VALUE + "." + key).gt(parseDate).lt(parseIncDate));
                            } else {
                                if (key.startsWith("from")) {
                                    dateCriteriaMap.put(key, parseDate);
                                } else {
                                    dateCriteriaMap.put(key, parseIncDate);
                                }
                            }
                        }
                    } // Code added for handling image and file but since we are not showing image and file components in search it is of no use as for now
                    else if (cType.equals(HkSystemConstantUtil.CustomField.ComponentType.IMAGE) || cType.equals(HkSystemConstantUtil.CustomField.ComponentType.UPLOAD)) {
                        query.addCriteria(Criteria.where(PurchaseFields.FIELD_VALUE + "." + key).regex(value.toString()));
                    } else {
                        if (value instanceof Collection) {
                            Collection coll = (Collection) value;
                            if (!CollectionUtils.isEmpty(coll)) {
                                query.addCriteria(Criteria.where(PurchaseFields.FIELD_VALUE + "." + key).in(coll));
                            }
                        } else if (value instanceof Date) {
                            long date = Long.parseLong(value.toString());
                            query.addCriteria(Criteria.where(PurchaseFields.FIELD_VALUE + "." + key).is(new Date(date)));
                        } else {
                            if (value != null && value.toString().length() > 0) {
                                query.addCriteria(Criteria.where(PurchaseFields.FIELD_VALUE + "." + key).is(value));
                            }
                        }
                    }
                }
            }
        }
        query.addCriteria(Criteria.where(PurchaseFields.CREATED_ON).gte(afterDate).lte(new Date()));
        if (!CollectionUtils.isEmpty(purchaseIds)) {
            query.addCriteria(Criteria.where(PurchaseFields.OBJECT_ID).in(purchaseIds));
        }
        query.addCriteria(Criteria.where(PurchaseFields.FRANCHISE).in(this.getCompnies(franchise)));
        if (isArchieve != null) {
            query.addCriteria(Criteria.where(PurchaseFields.IS_ARCHIVE).is(isArchieve));
        }

        if (!CollectionUtils.isEmpty(dateCriteriaMap)) {
            Set<String> removedFields = new HashSet<>();
            Iterator<Map.Entry<String, Object>> itr = dateCriteriaMap.entrySet().iterator();
            while (itr.hasNext()) {
                Map.Entry<String, Object> entryMap = itr.next();
                String key = entryMap.getKey();
                Date value = (Date) entryMap.getValue();

                if (!removedFields.contains(key)) {
                    if (key.startsWith("from")) {
                        if (dateCriteriaMap.containsKey("to" + key.substring(4))) {
                            Date toValue = (Date) dateCriteriaMap.get("to" + key.substring(4));
                            query.addCriteria(Criteria.where(PurchaseFields.FIELD_VALUE + "." + key.substring(4)).gte(value).lt(toValue));
                            removedFields.add("to" + key.substring(4));
                            removedFields.add(key);
                        } else {
                            query.addCriteria(Criteria.where(PurchaseFields.FIELD_VALUE + "." + key.substring(4)).gte(value));
                            removedFields.add(key);
                        }
                    } else {
                        if (dateCriteriaMap.containsKey("from" + key.substring(2))) {
                            Date toValue = (Date) dateCriteriaMap.get("from" + key.substring(2));
                            query.addCriteria(Criteria.where(PurchaseFields.FIELD_VALUE + "." + key.substring(2)).gte(toValue).lt(value));
                            removedFields.add("from" + key.substring(2));
                            removedFields.add(key);
                        } else {
                            query.addCriteria(Criteria.where(PurchaseFields.FIELD_VALUE + "." + key.substring(2)).lt(value));
                            removedFields.add(key);

                        }
                    }
                }
            }
        }
        query.with(new Sort(Sort.Direction.DESC, Arrays.asList(InvoiceFields.YEAR, InvoiceFields.SEQUENCE_NUMBER)));
        if (offset != null) {
            query.skip((int) offset);
        }
        if (limit != null) {
            query.limit((int) limit);
        }
        purchaseDocuments = mongoGenericDao.getMongoTemplate().find(query, HkPurchaseDocument.class);
        List<String> uiFieldList = null;
        List<HkPurchaseDocument> finalList = null;

        if (!CollectionUtils.isEmpty(purchaseDocuments)) {
            finalList = new ArrayList<>();
            String pointerComponentType = HkSystemConstantUtil.CustomField.ComponentType.POINTER + "@*" + HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN;

            for (HkPurchaseDocument purchaseDocument : purchaseDocuments) {
                HkPurchaseDocument temp = purchaseDocument;
                Map<String, Object> customMap = purchaseDocument.getFieldValue().toMap();
                uiFieldMap = this.createFieldNameWithComponentType(customMap);
                if (!CollectionUtils.isEmpty(uiFieldMap)) {
                    for (Map.Entry<String, String> field : uiFieldMap.entrySet()) {
                        if (field.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN) || field.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.USER_MULTISELECT) || field.getValue().equals(pointerComponentType)) {
                            if (customMap != null && !customMap.isEmpty() && customMap.containsKey(field.getKey())) {
                                String value = customMap.get(field.getKey()).toString().replaceAll("\\[", "").replaceAll("\\]", "");
                                customMap.put(field.getKey(), value);
                            }
                        }
                    }
                    temp.getFieldValue().putAll(customMap);
                    finalList.add(temp);
                }
            }
            if (CollectionUtils.isEmpty(finalList)) {
                finalList = purchaseDocuments;
            }
        }
        return finalList;
    }

    @MongoTransaction
    @Override
    public Boolean updatePurchase(ObjectId id, Map<String, Object> purchaseCustomMap, Map<String, String> purchaseDbTypeMap, Long franchise, Long updatedBy, Integer purchaseSequence, Map<String, String> autogeneratedLabelMap) {
        Boolean response = Boolean.FALSE;
        if (id != null) {
            Map<String, String> uiFieldMap = null;
            if (purchaseCustomMap != null && !purchaseCustomMap.isEmpty()) {
                uiFieldMap = this.createFieldNameWithComponentType(purchaseCustomMap);

            }
            HkPurchaseDocument hkPurchaseDocument = (HkPurchaseDocument) mongoGenericDao.retrieveById(id, HkPurchaseDocument.class
            );
            if (hkPurchaseDocument
                    != null) {
                if (purchaseSequence != null) {
                    hkPurchaseDocument.setSequenceNumber(purchaseSequence);
                }
                BasicBSONObject fieldValues = customFieldService.makeBSONObject(purchaseCustomMap, purchaseDbTypeMap, uiFieldMap, HkSystemConstantUtil.Feature.ROUGH_PURCHASE, franchise, id, Boolean.TRUE, hkPurchaseDocument.getFieldValue().toMap(), autogeneratedLabelMap);
//                hkPurchaseDocument.getFieldValue().putAll(fieldValues.toMap());
                hkPurchaseDocument.setLastModifiedBy(updatedBy);
                hkPurchaseDocument.setLastModifiedOn(new Date());
                hkPurchaseDocument.setFieldValue(fieldValues);
                mongoGenericDao.update(hkPurchaseDocument);
                response = Boolean.TRUE;
            }

        }
        return response;
    }

    public HkPurchaseDocument retrievePurchaseDocumentById(ObjectId purchaseId) {
        HkPurchaseDocument purchaseDocument = null;

        if (purchaseId != null) {
            purchaseDocument = (HkPurchaseDocument) mongoGenericDao.retrieveById(purchaseId, HkPurchaseDocument.class
            );
        }
        return purchaseDocument;
    }

    @MongoTransaction
    @Override
    public Boolean deletePurchase(ObjectId purchaseId, Long deletedBy) {
        Boolean response = Boolean.FALSE;
        if (purchaseId != null) {
            HkPurchaseDocument purchaseDocument = this.retrievePurchaseDocumentById(purchaseId);
            purchaseDocument.setIsArchive(Boolean.TRUE);
            purchaseDocument.setLastModifiedBy(deletedBy);
            purchaseDocument.setLastModifiedOn(new Date());
            mongoGenericDao.update(purchaseDocument);
            response = Boolean.TRUE;
        }
        return response;
    }

    @MongoTransaction
    @Override
    public List<HkPurchaseDocument> retrieveNotAssociatedRoughPurchase() {
        List<HkPurchaseDocument> purchaseDocuments = new ArrayList<>();
        List<Criteria> criterias = new ArrayList<>();
        Map<String, String> uiFieldMap = null;
        //adding criteria for having associated parcels null or not present in purchase documents.
        criterias.add(Criteria.where(PurchaseFields.ASSOCIATED_PARCEL).exists(false));
        purchaseDocuments
                = mongoGenericDao.findByCriteria(criterias, HkPurchaseDocument.class
                );

        List<HkPurchaseDocument> finalList = null;

        if (!CollectionUtils.isEmpty(purchaseDocuments)) {
            finalList = new ArrayList<>();
            String pointerComponentType = HkSystemConstantUtil.CustomField.ComponentType.POINTER + "@*" + HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN;

            for (HkPurchaseDocument purchaseDocument : purchaseDocuments) {
                HkPurchaseDocument temp = purchaseDocument;
                Map<String, Object> customMap = purchaseDocument.getFieldValue().toMap();
                uiFieldMap = this.createFieldNameWithComponentType(customMap);
                if (!CollectionUtils.isEmpty(uiFieldMap)) {
                    for (Map.Entry<String, String> field : uiFieldMap.entrySet()) {
                        if (field.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN) || field.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.USER_MULTISELECT) || field.getValue().equals(pointerComponentType)) {
                            if (customMap != null && !customMap.isEmpty() && customMap.containsKey(field.getKey())) {
                                String value = customMap.get(field.getKey()).toString().replaceAll("\\[", "").replaceAll("\\]", "");
                                customMap.put(field.getKey(), value);
                            }
                        }
                    }
                    temp.getFieldValue().putAll(customMap);
                    finalList.add(temp);
                }
            }
            if (CollectionUtils.isEmpty(finalList)) {
                finalList = purchaseDocuments;
            }
        }
        return finalList;
    }

    @MongoTransaction
    @Override
    public List<HkParcelDocument> retrieveNotAssociatedParcels() {
        List<HkParcelDocument> parcelDocuments = new ArrayList<>();
        Map<String, String> uiFieldMap = null;
        List<Criteria> criterias = new ArrayList<>();
        //adding criteria for having associated parcels null or not present in purchase documents.
        criterias.add(Criteria.where(ParcelFields.ASSOCIATED_PURCHASES).exists(false));
        parcelDocuments
                = mongoGenericDao.findByCriteria(criterias, HkParcelDocument.class
                );

        List<HkParcelDocument> finalList = null;

        if (!CollectionUtils.isEmpty(parcelDocuments)) {
            finalList = new ArrayList<>();
            String pointerComponentType = HkSystemConstantUtil.CustomField.ComponentType.POINTER + "@*" + HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN;

            for (HkParcelDocument hkParcelDocument : parcelDocuments) {
                HkParcelDocument temp = hkParcelDocument;
                Map<String, Object> customMap = hkParcelDocument.getFieldValue().toMap();
                uiFieldMap = this.createFieldNameWithComponentType(customMap);
                if (!CollectionUtils.isEmpty(uiFieldMap)) {
                    for (Map.Entry<String, String> field : uiFieldMap.entrySet()) {
                        if (field.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN) || field.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.USER_MULTISELECT) || field.getValue().equals(pointerComponentType)) {
                            if (customMap != null && !customMap.isEmpty() && customMap.containsKey(field.getKey())) {
                                String value = customMap.get(field.getKey()).toString().replaceAll("\\[", "").replaceAll("\\]", "");
                                customMap.put(field.getKey(), value);
                            }
                        }
                    }
                    temp.getFieldValue().putAll(customMap);
                    finalList.add(temp);
                }
            }
            if (CollectionUtils.isEmpty(finalList)) {
                finalList = parcelDocuments;
            }
        }
        return finalList;
    }

    @MongoTransaction
    @Override
    public Boolean linkRoughParcelWithPurchase(List<ObjectId> parcelIds, List<ObjectId> purchaseIds) {
        Boolean result = Boolean.FALSE;
        if (!CollectionUtils.isEmpty(parcelIds) && !CollectionUtils.isEmpty(purchaseIds)) {
            List<HkParcelDocument> hkParcelDocuments = this.retrieveParcels(parcelIds, 0l, Boolean.FALSE);
            List<HkPurchaseDocument> hkPurchaseDocuments = this.retrievePurchases(null, purchaseIds, 0l, Boolean.FALSE, null, null);
            if (!CollectionUtils.isEmpty(hkPurchaseDocuments)) {
                for (HkPurchaseDocument hkPurchaseDocument : hkPurchaseDocuments) {
                    hkPurchaseDocument.setAssociatedParcels(parcelIds);
                    mongoGenericDao.update(hkPurchaseDocument);
                }
            }
            if (!CollectionUtils.isEmpty(hkParcelDocuments)) {
                for (HkParcelDocument hkParcelDocument : hkParcelDocuments) {
                    hkParcelDocument.setAssociatedPurchases(purchaseIds);
                    hkParcelDocument.setIsLinked(Boolean.TRUE);
                    if (hkParcelDocument.getFieldValue() != null) {
                        Map<String, Object> map = hkParcelDocument.getFieldValue().toMap();
                        if (!map.containsKey(ParcelFields.REMARKS) || map.get(ParcelFields.REMARKS) == null
                                && hkPurchaseDocuments.get(0) != null && hkPurchaseDocuments.get(0).getFieldValue() != null
                                && hkPurchaseDocuments.get(0).getFieldValue().containsField(ParcelFields.REMARKS)
                                && hkPurchaseDocuments.get(0).getFieldValue().get(ParcelFields.REMARKS) != null) {
                            map.put(ParcelFields.REMARKS, hkPurchaseDocuments.get(0).getFieldValue().get(ParcelFields.REMARKS));
                        }
                        hkParcelDocument.setFieldValue(new BasicBSONObject(map));
                    }
                    mongoGenericDao.update(hkParcelDocument);
                }
            }

            result = Boolean.TRUE;

        }
        return result;
    }

    @MongoTransaction
    @Override
    public List<HkPurchaseDocument> retrievePurchaseByCriteria(List<ObjectId> parcelIds) {
        List<Criteria> criterias = new ArrayList<>();
        List<HkPurchaseDocument> purchaseDocuments = new ArrayList<>();
        criterias.add(Criteria.where(PurchaseFields.ASSOCIATED_PARCEL).in(parcelIds));
        criterias.add(Criteria.where(ParcelFields.IS_ARCHIVE).is(Boolean.FALSE));
        purchaseDocuments
                = mongoGenericDao.findByCriteria(criterias, HkPurchaseDocument.class
                );
        return purchaseDocuments;
    }

    @MongoTransaction
    @Override
    public Boolean deLinkRoughParcelWithPurchase(ObjectId parcelId) {
        Boolean result = Boolean.FALSE;
        if (parcelId != null) {
            HkParcelDocument hkParcelDocument = this.retrieveParcelById(parcelId);
            hkParcelDocument.setIsLinked(Boolean.FALSE);
            hkParcelDocument.setAssociatedPurchases(null);
            if (hkParcelDocument.getFieldValue() != null) {
                Map<String, Object> map = hkParcelDocument.getFieldValue().toMap();
                if (map.containsKey(ParcelFields.REMARKS) && map.get(ParcelFields.REMARKS) != null) {
                    map.remove(ParcelFields.REMARKS);
                }
                hkParcelDocument.setFieldValue(new BasicBSONObject(map));
            }

            List<HkPurchaseDocument> hkPurchaseDocuments = this.retrievePurchaseByCriteria(Arrays.asList(parcelId));
            if (!CollectionUtils.isEmpty(hkPurchaseDocuments)) {
                for (HkPurchaseDocument hkPurchaseDocument : hkPurchaseDocuments) {
                    List<ObjectId> parcelIds = new ArrayList<>();
                    if (!CollectionUtils.isEmpty(hkPurchaseDocument.getAssociatedParcels())) {
                        for (Iterator<ObjectId> iter = hkPurchaseDocument.getAssociatedParcels().listIterator(); iter.hasNext();) {
                            ObjectId assParcelId = iter.next();
                            if (!assParcelId.equals(parcelId)) {
                                parcelIds.add(assParcelId);
                            }
                        }
                        if (!CollectionUtils.isEmpty(parcelIds)) {
                            hkPurchaseDocument.setAssociatedParcels(parcelIds);
                        } else {
                            hkPurchaseDocument.setAssociatedParcels(null);
                        }

                        mongoGenericDao.update(hkPurchaseDocument);
                    }
                }
            }
            mongoGenericDao.update(hkParcelDocument);
            result = Boolean.TRUE;
        }
        return result;
    }

    @MongoTransaction
    public String saveRoughMakeable(Map<String, Object> roughMakeableCustomMap, Map<String, String> roughMakeableDbTypeMap, List<String> uiFieldList, Long franchise, Long createdBy, ObjectId packetId) {

        HkRoughMakeableDocument roughMakeableDoc = new HkRoughMakeableDocument();
        roughMakeableDoc.setFranchiseId(franchise);
        roughMakeableDoc.setCreatedByFranchise(franchise);
        roughMakeableDoc.setIsArchive(Boolean.FALSE);
        roughMakeableDoc.setCreatedBy(createdBy);
        roughMakeableDoc.setLastModifiedBy(createdBy);
        roughMakeableDoc.setCreatedOn(new Date());
        roughMakeableDoc.setModifiedOn(new Date());
        roughMakeableDoc.setPacketId(packetId);

        if (!CollectionUtils.isEmpty(roughMakeableCustomMap) && !CollectionUtils.isEmpty(roughMakeableDbTypeMap)) {
            Map<String, String> uiFieldNameWithCompType = customFieldService.retrieveUIFieldNameWithComponentTypes(uiFieldList);
            BasicBSONObject fieldValues = customFieldService.makeBSONObject(roughMakeableCustomMap, roughMakeableDbTypeMap, uiFieldNameWithCompType, HkSystemConstantUtil.Feature.ROUGH_MAKEABLE, franchise, null, Boolean.FALSE, null, null);
            roughMakeableDoc.setFieldValue(fieldValues);
        } else {
            BasicBSONObject fieldValues = new BasicBSONObject();
            roughMakeableDoc.setFieldValue(fieldValues);
        }
        mongoGenericDao.create(roughMakeableDoc);
        return roughMakeableDoc.getId().toString();
    }

    @Override
    public List<HkParcelDocument> retrieveAllottedParcels(Long userId) {
        List<Criteria> criterias = new LinkedList<>();
        criterias.add(where(ParcelFields.FIELD_VALUE + "." + ParcelFields.IN_STOCK_OF).is(userId + ":E"));
        criterias.add(where(IS_ARCHIVE).is(false));
        List<HkParcelDocument> parcelDocuments = mongoGenericDao.findByCriteria(criterias, HkParcelDocument.class);
//        System.out.println("PARCELS:: " + parcelDocuments);
        return parcelDocuments;
    }

    public HkRoughMakeableDocument retrieveRoughMakeableByPacketId(String packetId) {
        List<Criteria> criteriaList = new ArrayList<>();
        criteriaList.add(Criteria.where(IS_ARCHIVE).is(false));
        criteriaList.add(Criteria.where(MakebleFields.PACKET_ID).is(new ObjectId(packetId)));
        HkRoughMakeableDocument roughMakeableDocumentByPktId = (HkRoughMakeableDocument) mongoGenericDao.findOneByCriteria(criteriaList, HkRoughMakeableDocument.class);
        roughMakeableDocumentByPktId = replaceMultiselectDropdownArrayWithString(roughMakeableDocumentByPktId);
        return roughMakeableDocumentByPktId;
    }

    private <T extends GenericDocument> T replaceMultiselectDropdownArrayWithString(T subClassOfGenericDocument) {
        if (subClassOfGenericDocument != null) {
            String pointerComponentType = HkSystemConstantUtil.CustomField.ComponentType.POINTER + "@*" + HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN;
            Map<String, String> uiFieldMap = null;
            Map<String, Object> customMap = subClassOfGenericDocument.getFieldValue().toMap();
            uiFieldMap = this.createFieldNameWithComponentType(customMap);

            if (!CollectionUtils.isEmpty(uiFieldMap)) {
                for (Map.Entry<String, String> field : uiFieldMap.entrySet()) {
                    if (field.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN) || field.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.USER_MULTISELECT) || field.getValue().equals(pointerComponentType)) {
                        if (customMap != null && !customMap.isEmpty() && customMap.containsKey(field.getKey())) {
                            String value = customMap.get(field.getKey()).toString().replaceAll("\\[", "").replaceAll("\\]", "");
                            customMap.put(field.getKey(), value);
                        }
                    }
                }
                subClassOfGenericDocument.getFieldValue().putAll(customMap);
            }
            return subClassOfGenericDocument;
        } else {
            return null;
        }
    }

    @MongoTransaction
    public String updateRoughMakeable(Map<String, Object> roughMakeableCustomMap, Map<String, String> roughMakeableDbTypeMap, List<String> uiFieldList, Long franchise, Long modifiedBy, String roughMakeableId) {
        HkRoughMakeableDocument roughmakeableToUpdate = (HkRoughMakeableDocument) mongoGenericDao.retrieveById(new ObjectId(roughMakeableId), HkRoughMakeableDocument.class);
        roughmakeableToUpdate.setFranchiseId(franchise);
        roughmakeableToUpdate.setLastModifiedBy(modifiedBy);
        roughmakeableToUpdate.setModifiedOn(new Date());
        if (!CollectionUtils.isEmpty(roughMakeableCustomMap) && !CollectionUtils.isEmpty(roughMakeableDbTypeMap)) {
            Map<String, String> uiFieldNameWithCompType = customFieldService.retrieveUIFieldNameWithComponentTypes(uiFieldList);
            BasicBSONObject fieldValues = customFieldService.makeBSONObject(roughMakeableCustomMap, roughMakeableDbTypeMap, uiFieldNameWithCompType, HkSystemConstantUtil.Feature.ROUGH_MAKEABLE, franchise, null, Boolean.FALSE, null, null);
            roughmakeableToUpdate.setFieldValue(fieldValues);
        } else {
            BasicBSONObject fieldValues = new BasicBSONObject();
            roughmakeableToUpdate.setFieldValue(fieldValues);
        }
        mongoGenericDao.update(roughmakeableToUpdate);
        return roughmakeableToUpdate.getId().toString();
    }

    public String saveFinalMakeable(Map<String, Object> finalMakeableCustomMap, Map<String, String> finalMakeableDbTypeMap, List<String> uiFieldList, Long franchise, Long createdBy, ObjectId packetId) {
        HkFinalMakeableDocument finalMakeableDoc = new HkFinalMakeableDocument();
        finalMakeableDoc.setFranchiseId(franchise);
        finalMakeableDoc.setCreatedByFranchise(franchise);
        finalMakeableDoc.setIsArchive(Boolean.FALSE);
        finalMakeableDoc.setCreatedBy(createdBy);
        finalMakeableDoc.setLastModifiedBy(createdBy);
        finalMakeableDoc.setCreatedOn(new Date());
        finalMakeableDoc.setModifiedOn(new Date());
        finalMakeableDoc.setPacketId(packetId);

        if (!CollectionUtils.isEmpty(finalMakeableCustomMap) && !CollectionUtils.isEmpty(finalMakeableDbTypeMap)) {
            Map<String, String> uiFieldNameWithCompType = customFieldService.retrieveUIFieldNameWithComponentTypes(uiFieldList);
            BasicBSONObject fieldValues = customFieldService.makeBSONObject(finalMakeableCustomMap, finalMakeableDbTypeMap, uiFieldNameWithCompType, HkSystemConstantUtil.Feature.ROUGH_MAKEABLE, franchise, null, Boolean.FALSE, null, null);
            finalMakeableDoc.setFieldValue(fieldValues);
        } else {
            BasicBSONObject fieldValues = new BasicBSONObject();
            finalMakeableDoc.setFieldValue(fieldValues);
        }
        mongoGenericDao.create(finalMakeableDoc);
        return finalMakeableDoc.getId().toString();
    }

    public HkFinalMakeableDocument retrieveFinalMakeableByPacketId(String packetId) {
        List<Criteria> criteriaList = new ArrayList<>();
        criteriaList.add(Criteria.where(IS_ARCHIVE).is(false));
        criteriaList.add(Criteria.where(MakebleFields.PACKET_ID).is(new ObjectId(packetId)));
        HkFinalMakeableDocument finalMakeableDocumentByPktId = (HkFinalMakeableDocument) mongoGenericDao.findOneByCriteria(criteriaList, HkFinalMakeableDocument.class);
        finalMakeableDocumentByPktId = replaceMultiselectDropdownArrayWithString(finalMakeableDocumentByPktId);
        return finalMakeableDocumentByPktId;
    }

    public String updateFinalMakeable(Map<String, Object> finalMakeableCustomMap, Map<String, String> finalMakeableDbTypeMap, List<String> uiFieldList, Long franchise, Long modifiedBy, String finalMakeableId) {
        HkFinalMakeableDocument finalmakeableToUpdate = (HkFinalMakeableDocument) mongoGenericDao.retrieveById(new ObjectId(finalMakeableId), HkFinalMakeableDocument.class);
        finalmakeableToUpdate.setFranchiseId(franchise);
        finalmakeableToUpdate.setLastModifiedBy(modifiedBy);
        finalmakeableToUpdate.setModifiedOn(new Date());
        if (!CollectionUtils.isEmpty(finalMakeableCustomMap) && !CollectionUtils.isEmpty(finalMakeableDbTypeMap)) {
            Map<String, String> uiFieldNameWithCompType = customFieldService.retrieveUIFieldNameWithComponentTypes(uiFieldList);
            BasicBSONObject fieldValues = customFieldService.makeBSONObject(finalMakeableCustomMap, finalMakeableDbTypeMap, uiFieldNameWithCompType, HkSystemConstantUtil.Feature.ROUGH_MAKEABLE, franchise, null, Boolean.FALSE, null, null);
            finalmakeableToUpdate.setFieldValue(fieldValues);
        } else {
            BasicBSONObject fieldValues = new BasicBSONObject();
            finalmakeableToUpdate.setFieldValue(fieldValues);
        }
        mongoGenericDao.update(finalmakeableToUpdate);
        return finalmakeableToUpdate.getId().toString();
    }

    public Map<String, String> mapOfDbFieldAndFormulaForAllFeatures(List<HkFieldDocument> fieldDocuments) {
        Map<String, String> mapOfDbFieldAndFormulaForAllFeatures = null;
        if (!CollectionUtils.isEmpty(fieldDocuments)) {
            mapOfDbFieldAndFormulaForAllFeatures = new HashMap<>();
            for (HkFieldDocument fieldDocument : fieldDocuments) {
                mapOfDbFieldAndFormulaForAllFeatures.put(fieldDocument.getDbFieldName(), fieldDocument.getFormulaValue());
            }
        }
        return mapOfDbFieldAndFormulaForAllFeatures;

    }

}
