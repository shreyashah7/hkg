package com.argusoft.hkg.web.center.stock.transformers;

import com.argusoft.hkg.common.constantutil.HkSystemConstantUtil;
import com.argusoft.hkg.common.functionutil.HkSystemFunctionUtil;
import com.argusoft.hkg.nosql.core.HkConfigurationService;
import com.argusoft.hkg.nosql.core.HkCustomFieldService;
import com.argusoft.hkg.nosql.core.HkFoundationDocumentService;
import com.argusoft.hkg.nosql.core.HkStockService;
import com.argusoft.hkg.nosql.core.HkUserService;
import com.argusoft.hkg.nosql.model.HkFeatureFieldPermissionDocument;
import com.argusoft.hkg.nosql.model.HkInvoiceDocument;
import com.argusoft.hkg.nosql.model.HkLotDocument;
import com.argusoft.hkg.nosql.model.HkPacketDocument;
import com.argusoft.hkg.nosql.model.HkParcelDocument;
import com.argusoft.hkg.nosql.model.HkPersonCapabilityDocument;
import com.argusoft.hkg.nosql.model.HkRoleFeatureModifierDocument;
import com.argusoft.hkg.nosql.model.HkUserGradeStatusDocument;
import com.argusoft.hkg.nosql.model.HkUserGradeSuggestionDocument;
import com.argusoft.hkg.sync.center.core.SyncCenterUserService;
import com.argusoft.hkg.web.center.stock.databeans.AllotmentDataBean;
import com.argusoft.hkg.web.center.stock.databeans.AllotmentSuggestionDataBean;
import com.argusoft.hkg.web.center.stock.databeans.UserGradeSuggestionDataBean;
import com.argusoft.hkg.web.center.usermanagement.databeans.LoginDataBean;
import com.argusoft.hkg.web.center.usermanagement.databeans.UMFeatureDetailDataBean;
import com.argusoft.hkg.web.center.util.ApplicationUtil;
import com.argusoft.hkg.web.util.SelectItem;
import com.argusoft.sync.center.model.HkFieldDocument;
import com.argusoft.sync.center.model.HkLeaveDocument;
import com.argusoft.sync.center.model.HkMasterValueDocument;
import com.argusoft.sync.center.model.SyncCenterUserDocument;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.bson.BasicBSONObject;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 *
 * @author shreya
 */
@Service
public class AllotmentTransformer {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    LoginDataBean loginDataBean;

    @Autowired
    HkCustomFieldService fieldService;

    @Autowired
    HkStockService stockService;

    @Autowired
    SyncCenterUserService syncCenterUserService;

    @Autowired
    ApplicationUtil applicationUtil;

    @Autowired
    StockTransformer stockTransformer;

    @Autowired
    HkConfigurationService configurationService;

    @Autowired
    HkUserService hkUserService;

    @Autowired
    HkFoundationDocumentService hkFoundationDocumentService;

//    //pass default property name as "cut"
//    @Deprecated
//    public List<AllotmentSuggestionDataBean> employeeAllocationSuggestion(String propertyName, Long currentNode) throws ParseException {
//        List<AllotmentSuggestionDataBean> allotmentSuggestionDataBeans = null;
//        if (propertyName != null && currentNode != null) {
//            String dbPropertyName = null;
//            if (propertyName.equalsIgnoreCase("cut")) {
//                dbPropertyName = HkSystemConstantUtil.PlanStaticFieldName.CUT;
//            } else if (propertyName.equalsIgnoreCase("clarity")) {
//                dbPropertyName = HkSystemConstantUtil.PlanStaticFieldName.CLARITY;
//            } else if (propertyName.equalsIgnoreCase("color")) {
//                dbPropertyName = HkSystemConstantUtil.PlanStaticFieldName.COLOR;
//            } else if (propertyName.equalsIgnoreCase("carat")) {
//                dbPropertyName = HkSystemConstantUtil.PlanStaticFieldName.CARAT;
//            }
//            List<Long> nextNodeDesignationIds = null;
//
//            List<HkPersonCapabilityDocument> personCapabilityDocuments = stockService.retrievePersonCapabilityWithBreakagePercentage(propertyName, loginDataBean.getCompanyId());
//            if (!CollectionUtils.isEmpty(personCapabilityDocuments)) {
//                Set<Long> userIds = new HashSet<>();
//                for (HkPersonCapabilityDocument document : personCapabilityDocuments) {
//                    userIds.add(document.getForPerson());
//                }
////            List<Long> designationIds = stockService.retrieveDesignationIdsForPossibleNextNodes(currentNode);
//                LOGGER.info("userIds=" + userIds);
//                List<SyncCenterUserDocument> centerUsers = syncCenterUserService.retrieveUsersByIds(new ArrayList<>(userIds));
//                LOGGER.info("centerUsers=" + centerUsers);
//
//                //Remove users those don't belong to next node designations.
//                if (!CollectionUtils.isEmpty(nextNodeDesignationIds) && !CollectionUtils.isEmpty(centerUsers)) {
//                    Iterator<SyncCenterUserDocument> itr = centerUsers.iterator();
//                    while (itr.hasNext()) {
//                        SyncCenterUserDocument user = itr.next();
//                        if (user.getRoleId() != null) {
//                            boolean isRoleExists = false;
//                            for (Long roleId : user.getRoleId()) {
//                                if (nextNodeDesignationIds.contains(roleId)) {
//                                    isRoleExists = true;
//                                    break;
//                                }
//                            }
//                            if (!isRoleExists) {
//                                itr.remove();
//                            }
//                        } else {
//                            itr.remove();
//                        }
//                    }
//                }
//                LOGGER.info("Designation users=" + centerUsers);
//                personCapabilityDocuments.sort(new Comparator<HkPersonCapabilityDocument>() {
//                    @Override
//                    public int compare(HkPersonCapabilityDocument o1, HkPersonCapabilityDocument o2) {
//
//                        switch (propertyName.toLowerCase()) {
//                            case "cut":
//                                if (!o2.getPropertyName().equals(HkSystemConstantUtil.PlanStaticFieldName.CUT)) {
//                                    if (!o1.getPropertyName().equals(HkSystemConstantUtil.PlanStaticFieldName.CUT)) {
//                                        return 0; //equal
//                                    } else {
//                                        return -1; // null is before other strings
//                                    }
//                                } else // this.member != null
//                                if (!o1.getPropertyName().equals(HkSystemConstantUtil.PlanStaticFieldName.CUT)) {
//                                    return 1;  // all other strings are after null
//                                } else {
//                                    return o2.getSuccessRatio().compareTo(o1.getSuccessRatio());
//                                }
//                            case "clarity":
//                                if (!o2.getPropertyName().equals(HkSystemConstantUtil.PlanStaticFieldName.CLARITY)) {
//                                    if (!o1.getPropertyName().equals(HkSystemConstantUtil.PlanStaticFieldName.CLARITY)) {
//                                        return 0; //equal
//                                    } else {
//                                        return -1; // null is before other strings
//                                    }
//                                } else // this.member != null
//                                if (!o1.getPropertyName().equals(HkSystemConstantUtil.PlanStaticFieldName.CLARITY)) {
//                                    return 1;  // all other strings are after null
//                                } else {
//                                    return o2.getSuccessRatio().compareTo(o1.getSuccessRatio());
//                                }
//                            case "color":
//                                if (!o2.getPropertyName().equals(HkSystemConstantUtil.PlanStaticFieldName.COLOR)) {
//                                    if (!o1.getPropertyName().equals(HkSystemConstantUtil.PlanStaticFieldName.COLOR)) {
//                                        return 0; //equal
//                                    } else {
//                                        return -1; // null is before other strings
//                                    }
//                                } else // this.member != null
//                                if (!o1.getPropertyName().equals(HkSystemConstantUtil.PlanStaticFieldName.COLOR)) {
//                                    return 1;  // all other strings are after null
//                                } else {
//                                    return o2.getSuccessRatio().compareTo(o1.getSuccessRatio());
//                                }
//                            case "carat":
//                                if (!o2.getPropertyName().equals(HkSystemConstantUtil.PlanStaticFieldName.CARAT)) {
//                                    if (!o1.getPropertyName().equals(HkSystemConstantUtil.PlanStaticFieldName.CARAT)) {
//                                        return 0; //equal
//                                    } else {
//                                        return -1; // null is before other strings
//                                    }
//                                } else // this.member != null
//                                if (!o1.getPropertyName().equals(HkSystemConstantUtil.PlanStaticFieldName.CARAT)) {
//                                    return 1;  // all other strings are after null
//                                } else {
//                                    return o2.getSuccessRatio().compareTo(o1.getSuccessRatio());
//                                }
//                            default:
//                                return 0;
//                        }
//                    }
//                });
//                LOGGER.info("Person capability after sorting=" + personCapabilityDocuments);
//                //Retrieve top users according to property name
//                List<SyncCenterUserDocument> top4users = new ArrayList<>();
//                List<Long> existingUsers = new ArrayList<>();
//                for (HkPersonCapabilityDocument personCapabilityDocument : personCapabilityDocuments) {
//                    int count = 0;
//                    if (personCapabilityDocument.getPropertyName().equals(dbPropertyName)) {
//                        if (!CollectionUtils.isEmpty(centerUsers)) {
//                            for (SyncCenterUserDocument centerUser : centerUsers) {
//                                if (personCapabilityDocument.getForPerson().equals(centerUser.getId()) && !existingUsers.contains(centerUser.getId())) {
//                                    top4users.add(centerUser);
//                                    existingUsers.add(centerUser.getId());
//                                    count++;
//                                    break;
//                                }
//                            }
//                        } else {
//                            break;
//                        }
//                    }
//                    if (count >= 4) {
//                        break;
//                    }
//                }
//                LOGGER.info("Top four users to be used=" + top4users);
//
//                if (!CollectionUtils.isEmpty(top4users)) {
//                    List<Long> forUsers = new ArrayList<>();
//                    top4users.stream().forEach((syncCenterUserDocument) -> {
//                        forUsers.add(syncCenterUserDocument.getId());
//                    });
//                    Calendar cal = Calendar.getInstance();
//                    cal.set(Calendar.HOUR_OF_DAY, 0);
//                    cal.set(Calendar.MINUTE, 0);
//                    cal.set(Calendar.SECOND, 0);
//                    cal.set(Calendar.MILLISECOND, 0);
//                    Date fromDate = cal.getTime();
//                    cal.add(Calendar.DATE, 2);
//                    cal.set(Calendar.HOUR_OF_DAY, 0);
//                    cal.set(Calendar.MINUTE, 0);
//                    cal.set(Calendar.SECOND, 0);
//                    cal.set(Calendar.MILLISECOND, 0);
//                    Date toDate = cal.getTime();
//                    List<HkLeaveDocument> leaveDocuments = stockService.retrieveLeavesForUserBetweenFromDateAndToDate(forUsers, fromDate, toDate);
//                    Map<Long, List<String>> userWiseLeaveMap = null;
//                    LOGGER.info("leaveDocuments=" + leaveDocuments);
//                    if (!CollectionUtils.isEmpty(leaveDocuments)) {
//                        userWiseLeaveMap = new HashMap<>();
//                        for (HkLeaveDocument hkLeaveDocument : leaveDocuments) {
//                            List<Date> dates = HkSystemFunctionUtil.getDatesBetween(hkLeaveDocument.getFrmDt(), hkLeaveDocument.getToDt());
//                            List<String> statusDateList = new ArrayList<>();
//                            for (Date date : dates) {
//                                String dateStatus = date.toString() + "@" + hkLeaveDocument.getStatus();
//                            }
//                            userWiseLeaveMap.put(hkLeaveDocument.getForUser(), statusDateList);
//                        }
//                    }
//
//                    Map<Long, Integer> userWorkAllocationCountMap = null; //hkWorkAllotmentService.retrieveWorkAllocationCountForUsers(forUsers, Arrays.asList(HkSystemConstantUtil.ActivityServiceStatus.IN_PROCESS));
//
//                    allotmentSuggestionDataBeans = new ArrayList<>();
//                    for (SyncCenterUserDocument syncCenterUserDocument : top4users) {
//                        List<SelectItem> leaveData = new ArrayList<>();
//
//                        AllotmentSuggestionDataBean allotmentSuggestionDb = new AllotmentSuggestionDataBean();
//                        if (syncCenterUserDocument.getId() != null) {
//                            allotmentSuggestionDb.setUserId(syncCenterUserDocument.getId());
//                        }
//                        if (syncCenterUserDocument.getUserCode() != null) {
//                            allotmentSuggestionDb.setUserCode(syncCenterUserDocument.getUserCode());
//                        }
//                        if (StringUtils.hasText(syncCenterUserDocument.getFirstName())) {
//                            if (StringUtils.hasText(syncCenterUserDocument.getLastName())) {
//                                allotmentSuggestionDb.setEmployeeName(syncCenterUserDocument.getFirstName() + " " + syncCenterUserDocument.getLastName());
//                            } else {
//                                allotmentSuggestionDb.setEmployeeName(syncCenterUserDocument.getFirstName());
//                            }
//                        }
//                        // ************************* here the logoc for 4c according to person capabilities is remaining ****************** //
//
//                        for (HkPersonCapabilityDocument document : personCapabilityDocuments) {
//                            if (document.getForPerson().equals(syncCenterUserDocument.getId())) {
//                                switch (document.getPropertyName()) {
//                                    case HkSystemConstantUtil.PlanStaticFieldName.CUT:
//                                        allotmentSuggestionDb.setCut(document.getSuccessRatio());
//                                        break;
//                                    case HkSystemConstantUtil.PlanStaticFieldName.CLARITY:
//                                        allotmentSuggestionDb.setClarity(document.getSuccessRatio());
//                                        break;
//                                    case HkSystemConstantUtil.PlanStaticFieldName.COLOR:
//                                        allotmentSuggestionDb.setColor(document.getSuccessRatio());
//                                        break;
//                                    case HkSystemConstantUtil.PlanStaticFieldName.CARAT:
//                                        allotmentSuggestionDb.setCarat(document.getSuccessRatio());
//                                        break;
//                                    case HkSystemConstantUtil.PlanStaticFieldName.BREAKAGE:
//                                        allotmentSuggestionDb.setBreakage(document.getBreakagePercentage());
//                                        break;
//                                }
//                            }
//                        }
//
//                        // ************************* here the logoc for leave of an employee within 3 days  ****************** //
//                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
//                        Calendar calendar = Calendar.getInstance();
//                        calendar.set(Calendar.HOUR_OF_DAY, 0);
//                        calendar.set(Calendar.MINUTE, 0);
//                        calendar.set(Calendar.SECOND, 0);
//                        calendar.set(Calendar.MILLISECOND, 0);
//                        Date firstDay = calendar.getTime();
//
//                        calendar.add(Calendar.DATE, 1);
//                        calendar.set(Calendar.HOUR_OF_DAY, 0);
//                        calendar.set(Calendar.MINUTE, 0);
//                        calendar.set(Calendar.SECOND, 0);
//                        calendar.set(Calendar.MILLISECOND, 0);
//                        Date secondDay = calendar.getTime();
//
//                        calendar.add(Calendar.DATE, 1);
//                        calendar.set(Calendar.HOUR_OF_DAY, 0);
//                        calendar.set(Calendar.MINUTE, 0);
//                        calendar.set(Calendar.SECOND, 0);
//                        calendar.set(Calendar.MILLISECOND, 0);
//                        Date thirdDay = calendar.getTime();
//
//                        if (!CollectionUtils.isEmpty(userWiseLeaveMap) && userWiseLeaveMap.containsKey(syncCenterUserDocument.getId())) {
//                            allotmentSuggestionDb.setPresence(Boolean.FALSE);
//                            List<String> leaveDatesStatus = userWiseLeaveMap.get(syncCenterUserDocument.getId());
//                            if (!CollectionUtils.isEmpty(leaveDatesStatus)) {
//                                for (String dateStatus : leaveDatesStatus) {
//                                    if (StringUtils.hasText(dateStatus)) {
//                                        String[] split = dateStatus.split("@");
//                                        if (split != null) {
//                                            Date date = sdf.parse(split[0]);
//                                            Calendar c1 = Calendar.getInstance();
//                                            c1.setTime(date);
//                                            c1.set(Calendar.HOUR_OF_DAY, 0);
//                                            c1.set(Calendar.MINUTE, 0);
//                                            c1.set(Calendar.SECOND, 0);
//                                            c1.set(Calendar.MILLISECOND, 0);
//                                            SelectItem leave = new SelectItem();
//                                            if (date.equals(firstDay)) {
//                                                String dt = sdf.format(firstDay);
//                                                leave.setId(dt);
//                                            } else if (date.equals(secondDay)) {
//                                                String dt = sdf.format(secondDay);
//                                                leave.setId(dt);
//                                            } else if (date.equals(thirdDay)) {
//                                                String dt = sdf.format(thirdDay);
//                                                leave.setId(dt);
//                                            }
//                                            if (StringUtils.hasText(split[1])) {
//                                                String status = split[1];
//                                                if (status.equalsIgnoreCase(HkSystemConstantUtil.LeaveStatus.APPROVED)) {
//                                                    leave.setStatus(HkSystemConstantUtil.LEAVE_STATUS_FOR_ALLOCATION.LEAVE_APPROVED);
//                                                } else if (status.equalsIgnoreCase(HkSystemConstantUtil.LeaveStatus.PENDING)) {
//                                                    leave.setStatus(HkSystemConstantUtil.LEAVE_STATUS_FOR_ALLOCATION.LEAVE_APPLIED);
//                                                } else {
//                                                    leave.setStatus(HkSystemConstantUtil.LEAVE_STATUS_FOR_ALLOCATION.PRESENT);
//                                                }
//                                            }
//                                            leaveData.add(leave);
//                                        }
//                                    }
//                                }
//                            }
//
//                        } else {
//                            allotmentSuggestionDb.setPresence(Boolean.TRUE);
//                            SelectItem leave = new SelectItem();
//                            String dt = sdf.format(firstDay);
//                            leave.setId(dt);
//                            leave.setStatus(HkSystemConstantUtil.LEAVE_STATUS_FOR_ALLOCATION.PRESENT);
//                            leaveData.add(leave);
//
//                            leave = new SelectItem();
//                            dt = sdf.format(secondDay);
//                            leave.setId(dt);
//                            leave.setStatus(HkSystemConstantUtil.LEAVE_STATUS_FOR_ALLOCATION.PRESENT);
//                            leaveData.add(leave);
//
//                            leave = new SelectItem();
//                            dt = sdf.format(thirdDay);
//                            leave.setId(dt);
//                            leave.setStatus(HkSystemConstantUtil.LEAVE_STATUS_FOR_ALLOCATION.PRESENT);
//                            leaveData.add(leave);
//                        }
//                        allotmentSuggestionDb.setLeaveDetails(leaveData);
//                        // ************************* here the logoc for counting workload of a person ****************** //
//                        if (!CollectionUtils.isEmpty(userWorkAllocationCountMap) && userWorkAllocationCountMap.containsKey(syncCenterUserDocument.getId())) {
//                            allotmentSuggestionDb.setWorkLoad(userWorkAllocationCountMap.get(syncCenterUserDocument.getId()));
//                        } else {
//                            allotmentSuggestionDb.setWorkLoad(0);
//                        }
//                        allotmentSuggestionDataBeans.add(allotmentSuggestionDb);
//                    }
//                }
//            }
//        }
//        return allotmentSuggestionDataBeans;
//    }
    //------------------Added by Shreya For automatic allocation according to new changes-----------------
    public List<SelectItem> retrieveSearchedData(AllotmentDataBean allotmentDataBean) {
        Long franchise = loginDataBean.getCompanyId();
        List<SelectItem> selectItems = null;
        if (allotmentDataBean != null) {
            if (allotmentDataBean.getStockCustom() != null) {
                Map<String, Object> featureDbFieldMap = allotmentDataBean.getStockCustom();
                List<String> invoiceDbField = new ArrayList<>();
                List<String> parcelDbField = new ArrayList<>();
                List<String> lotDbField = new ArrayList<>();
                List<String> packetDbField = new ArrayList<>();
                if (!CollectionUtils.isEmpty(featureDbFieldMap)) {
                    for (Map.Entry<String, Object> entrySet : featureDbFieldMap.entrySet()) {
                        String dbFieldName = entrySet.getKey();
                        Object feature = entrySet.getValue();
                        if (feature.equals(HkSystemConstantUtil.Feature.LOT)) {
                            lotDbField.add(dbFieldName);
                        } else if (feature.equals(HkSystemConstantUtil.Feature.INVOICE)) {
                            invoiceDbField.add(dbFieldName);
                        } else if (feature.equals(HkSystemConstantUtil.Feature.PARCEL)) {
                            parcelDbField.add(dbFieldName);
                        } else if (feature.equals(HkSystemConstantUtil.Feature.PACKET)) {
                            packetDbField.add(dbFieldName);
                        }
                    }
                }
                Map<String, Map<String, Object>> featureCustomMapValue = allotmentDataBean.getFeatureCustomMapValue();
                Map<String, Object> invoiceFieldMap = new HashMap<>();
                Map<String, Object> parcelFieldMap = new HashMap<>();
                Map<String, Object> lotFieldMap = new HashMap<>();
                Map<String, Object> packetFieldMap = new HashMap<>();
                if (!CollectionUtils.isEmpty(featureCustomMapValue)) {
                    for (Map.Entry<String, Map<String, Object>> entry : featureCustomMapValue.entrySet()) {
                        String featureName = entry.getKey();
                        Map<String, Object> map = entry.getValue();
                        if (!StringUtils.isEmpty(featureName)) {
                            if (featureName.equalsIgnoreCase(HkSystemConstantUtil.Feature.INVOICE)) {
                                if (!CollectionUtils.isEmpty(map)) {
                                    this.prepareAndValidateFieldMap(map, invoiceFieldMap);
                                }
                            } else if (featureName.equalsIgnoreCase(HkSystemConstantUtil.Feature.PARCEL)) {
                                if (!CollectionUtils.isEmpty(map)) {
                                    this.prepareAndValidateFieldMap(map, parcelFieldMap);
                                }
                            } else if (featureName.equalsIgnoreCase(HkSystemConstantUtil.Feature.LOT)) {
                                if (!CollectionUtils.isEmpty(map)) {
                                    this.prepareAndValidateFieldMap(map, lotFieldMap);
                                }
                            } else if (featureName.equalsIgnoreCase(HkSystemConstantUtil.Feature.PACKET)) {
                                if (!CollectionUtils.isEmpty(map)) {
                                    this.prepareAndValidateFieldMap(map, packetFieldMap);
                                }
                            }
                        }
                    }
                    List<HkInvoiceDocument> invoiceDocuments = null;
                    if (!CollectionUtils.isEmpty(invoiceFieldMap)) {
                        invoiceDocuments = stockService.retrieveInvoices(invoiceFieldMap, franchise, Boolean.FALSE, null, null,null,null);
                    }
                    List<ObjectId> invoiceIds = null;
                    if (!CollectionUtils.isEmpty(invoiceDocuments)) {
                        invoiceIds = new ArrayList<>();
                        for (HkInvoiceDocument invoiceDocument : invoiceDocuments) {
                            invoiceIds.add(invoiceDocument.getId());
                        }
                    }
                    List<HkParcelDocument> parcelDocuments = null;
                    if (!CollectionUtils.isEmpty(parcelFieldMap) || !CollectionUtils.isEmpty(invoiceIds)) {
                        parcelDocuments = stockService.retrieveParcels(parcelFieldMap, invoiceIds, null, franchise, Boolean.FALSE, null, HkSystemConstantUtil.StockStatus.NEW_ROUGH,null,null);
                    }
                    List<ObjectId> parcelIds = null;
                    if (!CollectionUtils.isEmpty(parcelDocuments)) {
                        parcelIds = new ArrayList<>();
                        for (HkParcelDocument parcelDocument : parcelDocuments) {
                            parcelIds.add(parcelDocument.getId());
                        }
                    }
                    List<HkLotDocument> lotDocuments = null;
                    if (!CollectionUtils.isEmpty(invoiceIds) || !CollectionUtils.isEmpty(parcelIds) || !CollectionUtils.isEmpty(lotFieldMap)) {
                        lotDocuments = stockService.retrieveLots(lotFieldMap, invoiceIds, parcelIds, null, franchise, Boolean.FALSE);
                    }
                    List<ObjectId> lotIds = new ArrayList<>();
                    if (!CollectionUtils.isEmpty(lotDocuments)) {
                        for (HkLotDocument hkLotDocument : lotDocuments) {
                            lotIds.add(hkLotDocument.getId());
                        }
                    }
                    List<HkPacketDocument> packetDocuments = null;
                    if (!CollectionUtils.isEmpty(invoiceIds) || !CollectionUtils.isEmpty(parcelIds) || !CollectionUtils.isEmpty(lotIds) || (!CollectionUtils.isEmpty(packetFieldMap))) {
                        packetDocuments = stockService.retrievePackets(packetFieldMap, invoiceIds, parcelIds, lotIds, franchise, Boolean.FALSE, null, null,null,null);
                    }
                    if (!CollectionUtils.isEmpty(packetDocuments)) {
                        selectItems = new ArrayList<>();
                        invoiceIds = new ArrayList<>();
                        parcelIds = new ArrayList<>();
                        lotIds = new ArrayList<>();
                        for (HkPacketDocument packetDocument : packetDocuments) {
                            invoiceIds.add(packetDocument.getInvoice());
                            parcelIds.add(packetDocument.getParcel());
                            lotIds.add(packetDocument.getLot());
                        }
                        List<HkInvoiceDocument> hkInvoiceDocuments = stockService.retrieveInvoices(invoiceIds, franchise, Boolean.FALSE);
                        List<HkParcelDocument> hkParcelDocuments = stockService.retrieveParcels(parcelIds, franchise, Boolean.FALSE);
                        List<HkLotDocument> hkLotDocuments = stockService.retrieveLots(null, null, null, lotIds, franchise, Boolean.FALSE);

                        if (!CollectionUtils.isEmpty(hkInvoiceDocuments) && !CollectionUtils.isEmpty(hkParcelDocuments) && (!CollectionUtils.isEmpty(hkLotDocuments))) {
                            for (HkPacketDocument packetDocument : packetDocuments) {
                                Map<String, Object> map = new HashMap<>();
                                SelectItem selectItem = new SelectItem(packetDocument.getId().toString(), packetDocument.getLot().toString(), packetDocument.getParcel().toString(), packetDocument.getInvoice().toString());
                                BasicBSONObject fieldValue = packetDocument.getFieldValue();
                                if (fieldValue != null && !CollectionUtils.isEmpty(packetDbField)) {
                                    if (packetDocument.getIssueReceiveStatus() != null) {
                                        if (packetDocument.getIssueReceiveStatus().equals(HkSystemConstantUtil.IssueReceiveStockStatus.PENDING)) {
                                            map.put("status", "Already Alloted");
                                        } else if (packetDocument.getIssueReceiveStatus().equals(HkSystemConstantUtil.IssueReceiveStockStatus.COMPLETED)) {
                                            map.put("status", "Completed");
                                        }
                                    } else {
                                        map.put("status", packetDocument.getIssueReceiveStatus());
                                    }
                                    if (packetDbField.indexOf(HkSystemConstantUtil.AutoNumber.PACKET_ID) == -1) {
                                        map.put(HkSystemConstantUtil.AutoNumber.PACKET_ID, fieldValue.toMap().get(HkSystemConstantUtil.AutoNumber.PACKET_ID));
                                    }
                                    for (String dbField : packetDbField) {
                                        if (fieldValue.toMap().containsKey(dbField) && fieldValue.toMap().get(dbField) != null) {
                                            map.put(dbField, fieldValue.toMap().get(dbField));
                                        }
                                    }
                                } else if (fieldValue != null) {
                                    if (packetDocument.getIssueReceiveStatus() != null) {
                                        if (packetDocument.getIssueReceiveStatus().equals(HkSystemConstantUtil.IssueReceiveStockStatus.PENDING)) {
                                            map.put("status", "Already Alloted");
                                        } else if (packetDocument.getIssueReceiveStatus().equals(HkSystemConstantUtil.IssueReceiveStockStatus.COMPLETED)) {
                                            map.put("status", "Completed");
                                        }
                                    } else {
                                        map.put("status", packetDocument.getIssueReceiveStatus());
                                    }
                                    map.put(HkSystemConstantUtil.AutoNumber.PACKET_ID, fieldValue.toMap().get(HkSystemConstantUtil.AutoNumber.PACKET_ID));
                                }
                                if (!CollectionUtils.isEmpty(invoiceDbField)) {
                                    for (HkInvoiceDocument hkInvoiceDocument : hkInvoiceDocuments) {
                                        if (hkInvoiceDocument.getId().toString().equals(packetDocument.getInvoice().toString())) {
                                            BasicBSONObject invoiceFieldValue = hkInvoiceDocument.getFieldValue();
                                            if (invoiceFieldValue != null) {
                                                for (String dbField : invoiceDbField) {
                                                    if (invoiceFieldValue.toMap().containsKey(dbField)) {
                                                        map.put(dbField, invoiceFieldValue.toMap().get(dbField));
                                                    }
                                                }
                                            }
                                            break;
                                        }
                                    }
                                }
                                if (!CollectionUtils.isEmpty(parcelDbField)) {
                                    for (HkParcelDocument hkParcelDocument : hkParcelDocuments) {
                                        if (hkParcelDocument.getId().toString().equals(packetDocument.getParcel().toString())) {
                                            for (String dbField : parcelDbField) {
                                                if (hkParcelDocument.getFieldValue().toMap().containsKey(dbField)) {
                                                    map.put(dbField, hkParcelDocument.getFieldValue().toMap().get(dbField));
                                                }
                                            }
                                            break;
                                        }
                                    }
                                }
                                if (!CollectionUtils.isEmpty(lotDbField)) {
                                    for (HkLotDocument hkLotDocument : hkLotDocuments) {
                                        if (hkLotDocument.getId().toString().equals(packetDocument.getLot().toString())) {
                                            if (lotDbField.indexOf(HkSystemConstantUtil.AutoNumber.LOT_ID) == -1 && hkLotDocument.getFieldValue() != null) {
                                                map.put(HkSystemConstantUtil.AutoNumber.LOT_ID, hkLotDocument.getFieldValue().toMap().get(HkSystemConstantUtil.AutoNumber.LOT_ID));
                                            }
                                            for (String dbField : lotDbField) {
                                                if (hkLotDocument.getFieldValue().toMap().containsKey(dbField)) {
                                                    map.put(dbField, hkLotDocument.getFieldValue().toMap().get(dbField));
                                                }
                                            }
                                            break;
                                        }
                                    }
                                } else {
                                    for (HkLotDocument hkLotDocument : hkLotDocuments) {
                                        if (hkLotDocument.getId().toString().equals(packetDocument.getLot().toString())) {
                                            if (lotDbField.indexOf(HkSystemConstantUtil.AutoNumber.LOT_ID) == -1 && hkLotDocument.getFieldValue() != null) {
                                                map.put(HkSystemConstantUtil.AutoNumber.LOT_ID, hkLotDocument.getFieldValue().toMap().get(HkSystemConstantUtil.AutoNumber.LOT_ID));
                                            }
                                            break;
                                        }
                                    }
                                }
                                selectItem.setCategoryCustom(map);
                                selectItems.add(selectItem);
                            }
                        }
                    }
                }
            }
        }
        return selectItems;
    }

    private Map<String, Object> prepareAndValidateFieldMap(Map<String, Object> map, Map<String, Object> fieldMap) {
        Iterator<Map.Entry<String, Object>> iter = map.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<String, Object> entry = iter.next();
            String key = entry.getKey();
            Object value = entry.getValue();
            if (key.contains("$MS$") || key.contains("$UMS$") || key.contains("$AG$")) {
                if (value instanceof String && !StringUtils.isEmpty(value)) {
                    List<String> items = Arrays.asList(value.toString().split(","));
                    fieldMap.put(key, items);
                } else if (value instanceof Collection && !CollectionUtils.isEmpty((Collection<?>) value)) {
                    List<String> items = Arrays.asList(value.toString().split(","));
                    fieldMap.put(key, items);
                } else {
                    iter.remove();
                }
            } else {
                fieldMap.put(key, value);
            }
        }
        return fieldMap;
    }

    public List<UserGradeSuggestionDataBean> retrieveUserGradeSuggestion(List<ObjectId> packetIds) throws ParseException {
        List<UserGradeSuggestionDataBean> userGradeSuggestionDataBeans = new ArrayList<>();
        UMFeatureDetailDataBean feature = applicationUtil.getuMFeatureDetailDataBeanMap().get(HkSystemConstantUtil.Feature.ALLOTMENT);
        if (feature != null) {
            List<HkRoleFeatureModifierDocument> hkRoleFeatureModifierDocuments = configurationService.retrieveModifiersByDesignations(loginDataBean.getRoleIds(), loginDataBean.getCompanyId(), feature.getId());
            List<Long> asDesignationList = new ArrayList<>();
            List<Long> userIds = new ArrayList<>();
            List<ObjectId> packetIdList = new ArrayList<>();
            if (!CollectionUtils.isEmpty(hkRoleFeatureModifierDocuments)) {
                for (HkRoleFeatureModifierDocument hkRoleFeatureModifierDocument : hkRoleFeatureModifierDocuments) {
                    asDesignationList.addAll(hkRoleFeatureModifierDocument.getAsDesignation());
                }
                if (!CollectionUtils.isEmpty(asDesignationList)) {
                    List<SyncCenterUserDocument> syncCenterUserDocuments = hkUserService.retrieveUsersByRoleIds(loginDataBean.getCompanyId(), null, asDesignationList, Boolean.TRUE, false, null);
                    if (!CollectionUtils.isEmpty(syncCenterUserDocuments)) {
                        for (SyncCenterUserDocument syncCenterUserDocument : syncCenterUserDocuments) {
                            userIds.add(syncCenterUserDocument.getId());
                        }
                    }
                }
                if (!CollectionUtils.isEmpty(userIds) && !CollectionUtils.isEmpty(asDesignationList)) {
                    Map<Long, List<String>> userWiseLeaveMap = this.prepareUserWiseLeaveMap(userIds);
                    Map<Long, String> userIdNameMap = this.prepareUserIdNameMap(userIds);
                    List<Long> gradeMasterValue = new ArrayList<>();
                    List<HkUserGradeSuggestionDocument> hkUserGradeSuggestionDocuments = stockService.allocateDiamondToEmpByGrade(packetIds, loginDataBean.getCompanyId(), userIds, loginDataBean.getDepartment(), asDesignationList);
                    if (!CollectionUtils.isEmpty(hkUserGradeSuggestionDocuments) && !CollectionUtils.isEmpty(userIdNameMap)) {
                        for (HkUserGradeSuggestionDocument hkUserGradeSuggestionDocument : hkUserGradeSuggestionDocuments) {
                            gradeMasterValue.add(hkUserGradeSuggestionDocument.getGrade());
                            gradeMasterValue.add(hkUserGradeSuggestionDocument.getGoingToGrade());
                            packetIdList.add(hkUserGradeSuggestionDocument.getPacketId());
                        }
                        Map<String, String> packetDetails = this.prepareMapOfPacketDetail(packetIds);
                        Map<Long, String> gradeIdNameMap = this.prepareMasterValues(gradeMasterValue);
                        if (!CollectionUtils.isEmpty(gradeIdNameMap)) {
                            for (HkUserGradeSuggestionDocument hkUserGradeSuggestionDocument : hkUserGradeSuggestionDocuments) {
                                UserGradeSuggestionDataBean userGradeSuggestionDataBean = new UserGradeSuggestionDataBean();
                                userGradeSuggestionDataBean = this.convertUserGradeSuggestionModelToDataBean(hkUserGradeSuggestionDocument, userGradeSuggestionDataBean);
                                userGradeSuggestionDataBean.setUserName(userIdNameMap.get(userGradeSuggestionDataBean.getUserId()));
                                userGradeSuggestionDataBean.setGradeName(gradeIdNameMap.get(userGradeSuggestionDataBean.getGrade()));
                                userGradeSuggestionDataBean.setGoingToGradeName(gradeIdNameMap.get(userGradeSuggestionDataBean.getGoingToGrade()));
                                userGradeSuggestionDataBean.setPacketId(packetDetails.get(userGradeSuggestionDataBean.getPacketObjectId()));
                                List<SelectItem> leaveData = this.prepareLeaveData(userWiseLeaveMap, userGradeSuggestionDataBean);
                                userGradeSuggestionDataBean.setLeaveData(leaveData);
                                userGradeSuggestionDataBeans.add(userGradeSuggestionDataBean);
                            }
                        }
                    }
                }

            }
        }
        return userGradeSuggestionDataBeans;
    }

    private Map<String, String> prepareMapOfPacketDetail(List<ObjectId> packetIds) {
        Map<String, String> packetDetails = new HashMap<>();
        if (!CollectionUtils.isEmpty(packetIds)) {
            List<HkPacketDocument> hkPacketDocuments = stockService.retrievePacketsByIds(packetIds, loginDataBean.getCompanyId(), Boolean.FALSE, null, Boolean.FALSE);
            if (!CollectionUtils.isEmpty(hkPacketDocuments)) {
                for (HkPacketDocument hkPacketDocument : hkPacketDocuments) {
                    Long grade = (Long) hkPacketDocument.getFieldValue().get(HkSystemConstantUtil.PacketStaticFieldName.GRADE);
                    HkMasterValueDocument hkMasterValueDocument = hkFoundationDocumentService.retrieveValueEntityById(grade);
                    if (grade != null && hkMasterValueDocument != null) {
                        packetDetails.put(hkPacketDocument.getId().toString(), hkPacketDocument.getFieldValue().get(HkSystemConstantUtil.PacketStaticFieldName.PACKET_ID).toString() + "(" + hkMasterValueDocument.getValueName() + ")");
                    } else {
                        packetDetails.put(hkPacketDocument.getId().toString(), hkPacketDocument.getFieldValue().get(HkSystemConstantUtil.PacketStaticFieldName.PACKET_ID).toString());
                    }
                }
            }
        }
        return packetDetails;
    }

    private List<Long> retrieveUsersFromModifier() {
        List<Long> userIds = new ArrayList<>();
        UMFeatureDetailDataBean feature = applicationUtil.getuMFeatureDetailDataBeanMap().get(HkSystemConstantUtil.Feature.ALLOTMENT);
        if (feature != null) {
            List<HkRoleFeatureModifierDocument> hkRoleFeatureModifierDocuments = configurationService.retrieveModifiersByDesignations(loginDataBean.getRoleIds(), loginDataBean.getCompanyId(), feature.getId());
            List<Long> asDesignationList = new ArrayList<>();
            if (!CollectionUtils.isEmpty(hkRoleFeatureModifierDocuments)) {
                for (HkRoleFeatureModifierDocument hkRoleFeatureModifierDocument : hkRoleFeatureModifierDocuments) {
                    asDesignationList.addAll(hkRoleFeatureModifierDocument.getAsDesignation());
                }
                if (!CollectionUtils.isEmpty(asDesignationList)) {
                    List<SyncCenterUserDocument> syncCenterUserDocuments = hkUserService.retrieveUsersByRoleIds(loginDataBean.getCompanyId(), null, asDesignationList, Boolean.TRUE, false, null);
                    if (!CollectionUtils.isEmpty(syncCenterUserDocuments)) {
                        for (SyncCenterUserDocument syncCenterUserDocument : syncCenterUserDocuments) {
                            userIds.add(syncCenterUserDocument.getId());
                        }
                    }
                }
            }
        }
        return userIds;
    }

    private UserGradeSuggestionDataBean convertUserGradeSuggestionModelToDataBean(HkUserGradeSuggestionDocument hkUserGradeSuggestionDocument, UserGradeSuggestionDataBean userGradeSuggestionDataBean) {
        userGradeSuggestionDataBean.setCurrentStock(hkUserGradeSuggestionDocument.getCurrentStock());
        userGradeSuggestionDataBean.setGoingToGrade(hkUserGradeSuggestionDocument.getGoingToGrade());
        userGradeSuggestionDataBean.setGrade(hkUserGradeSuggestionDocument.getGrade());
        userGradeSuggestionDataBean.setNewStock(hkUserGradeSuggestionDocument.getNewStock());
        userGradeSuggestionDataBean.setTotalStock(hkUserGradeSuggestionDocument.getTotalStock());
        userGradeSuggestionDataBean.setUserId(hkUserGradeSuggestionDocument.getUserId());
        userGradeSuggestionDataBean.setActualUserId(hkUserGradeSuggestionDocument.getUserId());
        userGradeSuggestionDataBean.setPacketObjectId(hkUserGradeSuggestionDocument.getPacketId().toString());
        return userGradeSuggestionDataBean;
    }

    private Map<Long, String> prepareUserIdNameMap(List<Long> userIds) {
        List<SyncCenterUserDocument> centerUserDocuments = hkUserService.retrieveUsers(userIds);
        Map<Long, String> userIdNameMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(centerUserDocuments)) {
            for (SyncCenterUserDocument centerUserDocument : centerUserDocuments) {
                if (centerUserDocument.getFirstName() != null && centerUserDocument.getLastName() != null) {
                    String userName = centerUserDocument.getUserCode() + "-" + centerUserDocument.getFirstName() + " " + centerUserDocument.getLastName();
                    userIdNameMap.put(centerUserDocument.getId(), userName);
                }
            }
        }
        return userIdNameMap;
    }

    private Map<Long, String> prepareMasterValues(List<Long> gradeMasterValues) {
        Map<Long, String> gradeIdNameMap = new HashMap<>();
        List<HkMasterValueDocument> masterValueDocuments = hkFoundationDocumentService.retrieveValueEntities(gradeMasterValues);
        if (!CollectionUtils.isEmpty(masterValueDocuments)) {
            for (HkMasterValueDocument masterValueDocument : masterValueDocuments) {
                gradeIdNameMap.put(masterValueDocument.getId(), masterValueDocument.getValueName());
            }
        }
        return gradeIdNameMap;
    }

    public List<SelectItem> retrieveUsers(String user) {
        List<SelectItem> selectItems = new ArrayList<>();
        List<Long> userIds = this.retrieveUsersFromModifier();
        if (!CollectionUtils.isEmpty(userIds)) {
            List<SyncCenterUserDocument> syncCenterUserDocuments = hkUserService.retrieveUsersByCriteria(user, loginDataBean.getCompanyId(), Boolean.TRUE, userIds);
            if (!CollectionUtils.isEmpty(syncCenterUserDocuments)) {
                for (SyncCenterUserDocument centerUserDocument : syncCenterUserDocuments) {
                    String userName = centerUserDocument.getUserCode() + "-" + centerUserDocument.getFirstName() + " " + centerUserDocument.getLastName();
                    SelectItem selectItem = new SelectItem(centerUserDocument.getId(), userName);
                    selectItems.add(selectItem);
                }

            }
        }
        return selectItems;
    }

    public UserGradeSuggestionDataBean retrieveUserGradeSuggestionByUserId(Long userId) throws ParseException {
        UserGradeSuggestionDataBean userGradeSuggestionDataBean = null;
        if (userId != null) {
            HkUserGradeStatusDocument hkUserGradeStatusDocument = stockService.retrieveUserGradeStatusByUserId(userId);
            if (hkUserGradeStatusDocument != null) {
                List<Long> gradeValues = new ArrayList<>();
                gradeValues.add(hkUserGradeStatusDocument.getGrade());
                gradeValues.add(hkUserGradeStatusDocument.getGoingToGrade());
                Map<Long, String> prepareMasterValues = this.prepareMasterValues(gradeValues);
                Map<Long, String> prepareUserIdNameMap = this.prepareUserIdNameMap(Arrays.asList(userId));
                Map<Long, List<String>> userWiseLeaveMap = this.prepareUserWiseLeaveMap(Arrays.asList(userId));
                userGradeSuggestionDataBean = new UserGradeSuggestionDataBean();
                userGradeSuggestionDataBean.setCurrentStock(hkUserGradeStatusDocument.getNoOfDiamondsInStock());
                userGradeSuggestionDataBean.setGoingToGrade(hkUserGradeStatusDocument.getGoingToGrade());
                userGradeSuggestionDataBean.setGrade(hkUserGradeStatusDocument.getGrade());
                userGradeSuggestionDataBean.setUserId(userId);
                userGradeSuggestionDataBean.setUserName(prepareUserIdNameMap.get(userId));
                userGradeSuggestionDataBean.setGoingToGradeName(prepareMasterValues.get(hkUserGradeStatusDocument.getGoingToGrade()));
                userGradeSuggestionDataBean.setGradeName(prepareMasterValues.get(hkUserGradeStatusDocument.getGrade()));
                List<SelectItem> leaveData = this.prepareLeaveData(userWiseLeaveMap, userGradeSuggestionDataBean);
                userGradeSuggestionDataBean.setLeaveData(leaveData);
            }
        }
        return userGradeSuggestionDataBean;
    }

    public List<SelectItem> retrievePacketsAvailableInStock() {
        List<SelectItem> selectItems = new ArrayList<>();
        List<HkPacketDocument> hkPacketDocuments = stockService.retrievePackets(null, null, null, null, loginDataBean.getCompanyId(), Boolean.FALSE, loginDataBean.getId() + ":E", null,null,null);
        if (!CollectionUtils.isEmpty(hkPacketDocuments)) {
            List<Long> grades = new ArrayList<>();
            for (HkPacketDocument hkPacketDocument : hkPacketDocuments) {
                Long grade = (Long) hkPacketDocument.getFieldValue().get(HkSystemConstantUtil.PacketStaticFieldName.GRADE);
                if (grade != null) {
                    grades.add(grade);
                }
            }
            Map<Long, String> mapOfGradeIdName = this.prepareMasterValues(grades);
            for (HkPacketDocument hkPacketDocument : hkPacketDocuments) {
                if (hkPacketDocument.getFieldValue() != null && hkPacketDocument.getFieldValue().get(HkSystemConstantUtil.PacketStaticFieldName.PACKET_ID) != null) {
                    SelectItem selectItem = null;
                    Long grade = (Long) hkPacketDocument.getFieldValue().get(HkSystemConstantUtil.PacketStaticFieldName.GRADE);
                    if (grade != null && mapOfGradeIdName != null && mapOfGradeIdName.get(grade) != null) {
                        selectItem = new SelectItem(hkPacketDocument.getId().toString(), hkPacketDocument.getFieldValue().get(HkSystemConstantUtil.PacketStaticFieldName.PACKET_ID).toString() + "(" + mapOfGradeIdName.get(grade) + ")");
                    } else {
                        selectItem = new SelectItem(hkPacketDocument.getId().toString(), hkPacketDocument.getFieldValue().get(HkSystemConstantUtil.PacketStaticFieldName.PACKET_ID).toString());
                    }
                    selectItems.add(selectItem);
                }
            }
        }
        return selectItems;
    }

    public Boolean allotPackets(List<UserGradeSuggestionDataBean> userGradeSuggestionDataBeans) {
        Boolean result = Boolean.FALSE;
        if (!CollectionUtils.isEmpty(userGradeSuggestionDataBeans)) {
            Map<String, List<ObjectId>> entityIdsMap = new HashMap<>();
            Map<ObjectId, Long> packetIdUserIdMap = new HashMap<>();
            List<ObjectId> packetIds = new ArrayList<>();
            for (UserGradeSuggestionDataBean userGradeSuggestionDataBean : userGradeSuggestionDataBeans) {
                packetIds.add(new ObjectId(userGradeSuggestionDataBean.getPacketObjectId()));
                packetIdUserIdMap.put(new ObjectId(userGradeSuggestionDataBean.getPacketObjectId()), userGradeSuggestionDataBean.getUserId());
            }
            entityIdsMap.put("Packet", packetIds);
            stockService.issueRequestAllotmentRequest(entityIdsMap, packetIdUserIdMap, loginDataBean.getCompanyId(), loginDataBean.getId(), loginDataBean.getDepartment());
            result = Boolean.TRUE;
        }
        return result;
    }

    public Map<Long, List<String>> prepareUserWiseLeaveMap(List<Long> userIds) throws ParseException {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date fromDate = cal.getTime();
        cal.add(Calendar.DATE, 2);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date toDate = cal.getTime();
        List<HkLeaveDocument> leaveDocuments = stockService.retrieveLeavesForUserBetweenFromDateAndToDate(userIds, fromDate, toDate,false);
        Map<Long, List<String>> userWiseLeaveMap = null;
        LOGGER.info("leaveDocuments=" + leaveDocuments);
        if (!CollectionUtils.isEmpty(leaveDocuments)) {
            userWiseLeaveMap = new HashMap<>();
            List<Date> dateDiff = HkSystemFunctionUtil.getDatesBetween(fromDate, toDate);
            List<String> statusDateList = null;
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            for (HkLeaveDocument hkLeaveDocument : leaveDocuments) {
                List<Date> dates = HkSystemFunctionUtil.getDatesBetween(hkLeaveDocument.getFrmDt(), hkLeaveDocument.getToDt());
                if (userWiseLeaveMap.get(hkLeaveDocument.getForUser()) != null) {
                    statusDateList = userWiseLeaveMap.get(hkLeaveDocument.getForUser());
                } else {
                    statusDateList = new ArrayList<>();
                }
                for (Date date : dates) {
                    String dateStatus = sdf.format(date) + "@" + hkLeaveDocument.getStatus();
                    statusDateList.add(dateStatus);
                }
                userWiseLeaveMap.put(hkLeaveDocument.getForUser(), statusDateList);
            }
            for (Map.Entry<Long, List<String>> entrySet : userWiseLeaveMap.entrySet()) {
                List<String> dates = entrySet.getValue();
                for (Date dateDifference : dateDiff) {
                    int count = 0;
                    for (String date : dates) {
                        String[] split = date.split("@");
                        if (dateDifference.equals(sdf.parse(split[0]))) {
                            count++;
                        }
                    }
                    if (count == 0) {
                        String dateStatus = sdf.format(dateDifference) + "@" + "CMPLT";
                        dates.add(dateStatus);
                    }
                }
            }
        }
        return userWiseLeaveMap;
    }

    private List<SelectItem> prepareLeaveData(Map<Long, List<String>> userWiseLeaveMap, UserGradeSuggestionDataBean userGradeSuggestionDataBean) throws ParseException {
        List<SelectItem> leaveData = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date firstDay = calendar.getTime();

        calendar.add(Calendar.DATE, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date secondDay = calendar.getTime();

        calendar.add(Calendar.DATE, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date thirdDay = calendar.getTime();

        if (!CollectionUtils.isEmpty(userWiseLeaveMap) && userWiseLeaveMap.containsKey(userGradeSuggestionDataBean.getUserId())) {
            userGradeSuggestionDataBean.setPresence(Boolean.FALSE);
            List<String> leaveDatesStatus = userWiseLeaveMap.get(userGradeSuggestionDataBean.getUserId());
            if (!CollectionUtils.isEmpty(leaveDatesStatus)) {
                int leaveCount = 0;
                for (String dateStatus : leaveDatesStatus) {
                    if (StringUtils.hasText(dateStatus)) {
                        String[] split = dateStatus.split("@");
                        if (split != null) {
                            Date date = sdf.parse(split[0]);
                            Calendar c1 = Calendar.getInstance();
                            c1.setTime(date);
                            c1.set(Calendar.HOUR_OF_DAY, 0);
                            c1.set(Calendar.MINUTE, 0);
                            c1.set(Calendar.SECOND, 0);
                            c1.set(Calendar.MILLISECOND, 0);
                            SelectItem leave = new SelectItem();
                            if (date.equals(firstDay)) {
                                String dt = sdf.format(firstDay);
                                leave.setId(dt);
                            } else if (date.equals(secondDay)) {
                                String dt = sdf.format(secondDay);
                                leave.setId(dt);
                            } else if (date.equals(thirdDay)) {
                                String dt = sdf.format(thirdDay);
                                leave.setId(dt);
                            }
                            if (StringUtils.hasText(split[1])) {
                                String status = split[1];
                                if (status.equalsIgnoreCase(HkSystemConstantUtil.LeaveStatus.APPROVED)) {
                                    leaveCount++;
                                    leave.setStatus(HkSystemConstantUtil.LEAVE_STATUS_FOR_ALLOCATION.LEAVE_APPROVED);
                                } else if (status.equalsIgnoreCase(HkSystemConstantUtil.LeaveStatus.PENDING)) {
                                    leaveCount++;
                                    leave.setStatus(HkSystemConstantUtil.LEAVE_STATUS_FOR_ALLOCATION.LEAVE_APPLIED);
                                } else {
                                    leave.setStatus(HkSystemConstantUtil.LEAVE_STATUS_FOR_ALLOCATION.PRESENT);
                                }
                            }
                            leaveData.add(leave);
                        }
                    }
                }
                userGradeSuggestionDataBean.setLeaveCount(leaveCount);
            }

        } else {
            userGradeSuggestionDataBean.setPresence(Boolean.TRUE);
            userGradeSuggestionDataBean.setLeaveCount(0);
            SelectItem leave = new SelectItem();
            String dt = sdf.format(firstDay);
            leave.setId(dt);
            leave.setStatus(HkSystemConstantUtil.LEAVE_STATUS_FOR_ALLOCATION.PRESENT);
            leaveData.add(leave);

            leave = new SelectItem();
            dt = sdf.format(secondDay);
            leave.setId(dt);
            leave.setStatus(HkSystemConstantUtil.LEAVE_STATUS_FOR_ALLOCATION.PRESENT);
            leaveData.add(leave);

            leave = new SelectItem();
            dt = sdf.format(thirdDay);
            leave.setId(dt);
            leave.setStatus(HkSystemConstantUtil.LEAVE_STATUS_FOR_ALLOCATION.PRESENT);
            leaveData.add(leave);
        }
        return leaveData;
    }

}
