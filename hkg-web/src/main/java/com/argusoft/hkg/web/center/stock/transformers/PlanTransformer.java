package com.argusoft.hkg.web.center.stock.transformers;

import com.argusoft.hkg.common.constantutil.HkSystemConstantUtil;
import com.argusoft.hkg.nosql.core.HkConfigurationService;
import com.argusoft.hkg.nosql.core.HkFeatureService;
import com.argusoft.hkg.nosql.core.HkPlanService;
import com.argusoft.hkg.nosql.core.HkStockService;
import com.argusoft.hkg.nosql.core.HkUserService;
import com.argusoft.hkg.nosql.model.HkPacketDocument;
import com.argusoft.hkg.nosql.model.HkPacketPlanDocument;
import com.argusoft.hkg.nosql.model.HkPlanDocument;
import com.argusoft.hkg.nosql.model.HkRoleFeatureModifierDocument;
import com.argusoft.hkg.web.center.stock.databeans.PlanDataBean;
import com.argusoft.hkg.web.center.usermanagement.databeans.LoginDataBean;
import com.argusoft.sync.center.model.SyncCenterFeatureDocument;
import com.argusoft.sync.center.model.SyncCenterUserDocument;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bson.BasicBSONObject;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 *
 * @author rajkumar
 */
@Service
public class PlanTransformer {

    @Autowired
    private HkPlanService planService;

    @Autowired
    private HkStockService stockService;

    @Autowired
    private LoginDataBean loginDataBean;

    @Autowired
    private HkFeatureService featureService;

    @Autowired
    private HkConfigurationService configurationService;

    @Autowired
    private HkUserService userService;

    /**
     * Save plans
     *
     * @param planDataBeans
     * @return Map of planId to its id
     */
    public Map<Integer, ObjectId> savePlans(List<PlanDataBean> planDataBeans) {
        if (!CollectionUtils.isEmpty(planDataBeans)) {
            List<HkPacketPlanDocument> packetPlanDocuments = new ArrayList<>();
            Calendar calendar = Calendar.getInstance();
            planDataBeans.stream().forEach((planDataBean) -> {
                //Convert databean to plan document
                HkPacketPlanDocument packetPlan = this.convertPlanDataBeanToPlanDocument((PlanDataBean) planDataBean, null);
                packetPlan.setLastModifiedBy(loginDataBean.getId());
                packetPlan.setLastModifiedOn(calendar.getTime());
                packetPlanDocuments.add(packetPlan);
            });
            return planService.savePlans(packetPlanDocuments, planDataBeans.get(0).getPacketNumber(), loginDataBean.getId(), loginDataBean.getCompanyId());
        }
        return null;
    }

    /**
     * Convertor to convert plan databean to document
     *
     * @param planDataBean PlanDataBean to Convert
     * @param packetPlanDocument existing plan document
     * @return resulted plan document
     */
    private HkPacketPlanDocument convertPlanDataBeanToPlanDocument(PlanDataBean planDataBean, HkPacketPlanDocument packetPlanDocument) {
        Date today = new Date();
        if (packetPlanDocument == null) {
            packetPlanDocument = new HkPacketPlanDocument();
        }
        if (planDataBean.getId() != null) {
            packetPlanDocument.setId(new ObjectId(planDataBean.getId()));
        }
        if (planDataBean.getInvoiceId() != null) {
            packetPlanDocument.setInvoice(new ObjectId(planDataBean.getInvoiceId()));
        }
        if (planDataBean.getParcelId() != null) {
            packetPlanDocument.setParcel(new ObjectId(planDataBean.getParcelId()));
        }
        if (planDataBean.getLotId() != null) {
            packetPlanDocument.setLot(new ObjectId(planDataBean.getLotId()));
        }
        if (planDataBean.getPacketId() != null) {
            packetPlanDocument.setPacket(new ObjectId(planDataBean.getPacketId()));
        }
        if (planDataBean.getPlanId() != null) {
            packetPlanDocument.setPlanId(planDataBean.getPlanId());
        }
        if (planDataBean.getCopiedFrom() != null) {
            packetPlanDocument.setCopiedFrom(new ObjectId(planDataBean.getCopiedFrom()));
        }
        packetPlanDocument.setPacketNumber(planDataBean.getPacketNumber());
        packetPlanDocument.setIsActive(Boolean.TRUE);
        packetPlanDocument.setFinalPlan(planDataBean.isFinalPlan());
        if (planDataBean.getEmpId() != null) {
            packetPlanDocument.setEmpId(planDataBean.getEmpId());
        } else {
            packetPlanDocument.setEmpId(loginDataBean.getId());
        }
        if (planDataBean.getDesignationId() != null) {
            packetPlanDocument.setDesignationId(planDataBean.getDesignationId());
        } else {
            packetPlanDocument.setDesignationId(loginDataBean.getCurrentDesignation());
        }
        if (planDataBean.getDepartmentId() != null) {
            packetPlanDocument.setDepartmentId(planDataBean.getDepartmentId());
        } else {
            packetPlanDocument.setDepartmentId(loginDataBean.getDepartment());
        }
        if (!CollectionUtils.isEmpty(planDataBean.getTags())) {
            List<HkPlanDocument> plans = new ArrayList<>();
            for (Map<String, Object> map : planDataBean.getTags()) {
                HkPlanDocument planDocument = new HkPlanDocument();
                planDocument.setTag((String) map.get(HkSystemConstantUtil.PlanConstants.TAG));
                map.remove(HkSystemConstantUtil.PlanConstants.TAG);
                map.remove(HkSystemConstantUtil.PlanConstants.PLAN_ID);
                map.remove(HkSystemConstantUtil.PlanConstants.COPIED_FROM);
                map.remove(HkSystemConstantUtil.PlanConstants.INDEX);
                BasicBSONObject bSONObject = new BasicBSONObject(map);
                planDocument.setIsArchive(Boolean.FALSE);
                if (map.get(HkSystemConstantUtil.PlanConstants.IS_ACTIVE) != null) {
                    planDocument.setIsActive((Boolean) map.get(HkSystemConstantUtil.PlanConstants.IS_ACTIVE));
                } else {
                    planDocument.setIsActive(true);
                }
                map.remove(HkSystemConstantUtil.PlanConstants.IS_ACTIVE);
                planDocument.setFieldValue(bSONObject);
                planDocument.setCreatedBy(loginDataBean.getId());
                planDocument.setCreatedOn(today);
                planDocument.setLastModifiedOn(today);
                planDocument.setLastModifiedBy(loginDataBean.getId());
                plans.add(planDocument);
            }
            packetPlanDocument.setHkPlanDocuments(plans);
        }
        return packetPlanDocument;
    }

    /**
     * Retrieve plans by packet number
     *
     * @param packetNumber
     * @return Plans associated with the packet
     */
    public Map retrievePlansByPacket(String packetNumber) {
        List<PlanDataBean> planDataBeans = new ArrayList<>();
        Map result = new HashMap();
        if (!StringUtils.isEmpty(packetNumber)) {
            Map<String, Object> fieldValues = new HashMap<>();
            fieldValues.put(HkSystemConstantUtil.PacketStaticFieldName.PACKET_ID, packetNumber);

            //Retrieve packet to get inStockOf user
            List<HkPacketDocument> packets = stockService.retrievePackets(fieldValues, null, null, null, loginDataBean.getCompanyId(), false, null, null,null,null);
            if (!CollectionUtils.isEmpty(packets)) {
                //Retrieve plans associated with document
                List<HkPacketPlanDocument> plans = planService.retrievePlansByPacketNumber(packetNumber, loginDataBean.getCurrentDesignation(), loginDataBean.getCompanyId(), loginDataBean.getId());
                if (!CollectionUtils.isEmpty(plans)) {
                    planDataBeans = this.convertPacketPlanDocumentToDataBean(plans, null, packets.get(0));
                    //Prepare userIds list
                    List<Long> users = new ArrayList<>();
                    for (PlanDataBean planDataBean : planDataBeans) {
                        users.add(planDataBean.getEmpId());
                    }
                    //Retrieve users list
                    List<SyncCenterUserDocument> usersList = userService.retrieveUsers(users);
                    if (!CollectionUtils.isEmpty(usersList)) {
                        Map<Long, String> userIdToName = new HashMap<>();
                        for (SyncCenterUserDocument syncCenterUserDocument : usersList) {
                            userIdToName.put(syncCenterUserDocument.getId(), syncCenterUserDocument.getUserCode() + "-" + syncCenterUserDocument.getFirstName() + " " + syncCenterUserDocument.getLastName());
                        }
                        for (PlanDataBean planDataBean : planDataBeans) {
                            planDataBean.setEmpName(userIdToName.get(planDataBean.getEmpId()));
                        }
                    }
                }
                result.put("result", planDataBeans);
                return result;
            } else {
                result.put("error", "Packet does not exist");
                return result;
            }
        }
        result.put("error", "Invalid data");
        return result;

    }

    /**
     * Convertor to convert Plan document to databean
     *
     * @param hkPacketPlanDocuments
     * @param planDataBeans
     * @param packetDocument
     * @return resulted databean
     */
    private List<PlanDataBean> convertPacketPlanDocumentToDataBean(List<HkPacketPlanDocument> hkPacketPlanDocuments, List<PlanDataBean> planDataBeans, HkPacketDocument packetDocument) {
        if (planDataBeans == null) {
            planDataBeans = new ArrayList<>();
        }
        Integer index = 0;
        for (HkPacketPlanDocument hkPacketPlanDocument : hkPacketPlanDocuments) {
            PlanDataBean planDataBean = new PlanDataBean();

            planDataBean.setId(hkPacketPlanDocument.getId().toString());
            if (hkPacketPlanDocument.getInvoice() != null) {
                planDataBean.setInvoiceId(hkPacketPlanDocument.getInvoice().toString());
            }
            if (hkPacketPlanDocument.getParcel() != null) {
                planDataBean.setParcelId(hkPacketPlanDocument.getParcel().toString());
            }
            if (hkPacketPlanDocument.getLot() != null) {
                planDataBean.setLotId(hkPacketPlanDocument.getLot().toString());
            }
            if (hkPacketPlanDocument.getPacket() != null) {
                planDataBean.setPacketId(hkPacketPlanDocument.getPacket().toString());
            }
            if (hkPacketPlanDocument.getPacketNumber() != null) {
                planDataBean.setPacketNumber(hkPacketPlanDocument.getPacketNumber().toString());
            }
            planDataBean.setEmpId(hkPacketPlanDocument.getEmpId());
            planDataBean.setDesignationId(hkPacketPlanDocument.getDesignationId());
            planDataBean.setDepartmentId(hkPacketPlanDocument.getDepartmentId());
            planDataBean.setFinalPlan(hkPacketPlanDocument.isFinalPlan());
            planDataBean.setPlanId(hkPacketPlanDocument.getPlanId());
            if (packetDocument != null && !CollectionUtils.isEmpty(packetDocument.getFieldValue())) {
                Map<String, Object> fieldValues = packetDocument.getFieldValue().toMap();
                Object inStock = fieldValues.get(HkSystemConstantUtil.PacketStaticFieldName.IN_STOCK_OF);
                if (inStock != null) {
                    List<String> stockOf = Arrays.asList(inStock.toString().split(","));
                    if (!CollectionUtils.isEmpty(stockOf)) {
                        String userId = stockOf.get(0).split(":")[0];
                        planDataBean.setPacketInStockOf(Long.parseLong(userId));
                    }
                }
            }
            if (!CollectionUtils.isEmpty(hkPacketPlanDocument.getHkPlanDocuments())) {

                List<Map<String, Object>> listOfMap = new ArrayList<>();
                for (HkPlanDocument hkPlanDocument : hkPacketPlanDocument.getHkPlanDocuments()) {
                    if (hkPlanDocument.getIsActive()) {
                        Map<String, Object> map = new HashMap<>();
                        map.put(HkSystemConstantUtil.PlanConstants.PLAN_ID, hkPacketPlanDocument.getPlanId());
                        map.put(HkSystemConstantUtil.PlanConstants.TAG, hkPlanDocument.getTag());
                        map.put(HkSystemConstantUtil.PlanConstants.INDEX, index++);
                        if (hkPacketPlanDocument.getCopiedFrom() != null) {
                            map.put(HkSystemConstantUtil.PlanConstants.COPIED_FROM, hkPacketPlanDocument.getCopiedFrom().toString());
                        }
                        map.putAll(hkPlanDocument.getFieldValue().toMap());
                        listOfMap.add(map);
                    }
                }
                planDataBean.setTags(listOfMap);
            }
            planDataBeans.add(planDataBean);
        }
        return planDataBeans;
    }

    /**
     * Retrieve configured modifiers to permit permissions on the page
     *
     * @return List of modifiers configured
     */
    public List<String> retrieveModifiers() {
        List<String> result = new ArrayList<>();

        SyncCenterFeatureDocument featureDoc = featureService.retireveFeatureByName(HkSystemConstantUtil.Feature.ESTIMATE_PREDICTION);
        List<HkRoleFeatureModifierDocument> modifiers = configurationService.retrieveModifiersByDesignations(Arrays.asList(loginDataBean.getCurrentDesignation()), loginDataBean.getCompanyId(), featureDoc.getId());
        if (!CollectionUtils.isEmpty(modifiers)) {
            result = modifiers.get(0).getPlanAccess();
        }

        return result;
    }

    /**
     * Retrieve Final plans associated with packet
     *
     * @param packetId
     * @return Final plans
     */
    public List<PlanDataBean> retrieveFinalPlans(String packetId) {
        //Retrieve list of final plans for specified packets
        List<HkPacketPlanDocument> plans = planService.retrieveFinalPlans(packetId);
        if (!CollectionUtils.isEmpty(plans)) {
            List<Long> userIds = new ArrayList<>();
            //Prepare users list to view on page
            plans.stream().forEach((hkPacketPlanDocument) -> {
                userIds.add(hkPacketPlanDocument.getEmpId());
            });
            List<SyncCenterUserDocument> users = userService.retrieveUsers(userIds);
            Map<Long, String> userMap = new HashMap<>();
            users.stream().forEach((syncCenterUserDocument) -> {
                userMap.put(syncCenterUserDocument.getId(), syncCenterUserDocument.getUserCode() + "-" + syncCenterUserDocument.getFirstName() + " " + syncCenterUserDocument.getLastName());
            });
            List<PlanDataBean> planDatabeans = this.convertPacketPlanDocumentToDataBean(plans, null, null);
            planDatabeans.stream().forEach((planDataBean) -> {
                planDataBean.setEmpName(userMap.get(planDataBean.getEmpId()));
            });
            return planDatabeans;
        }
        return null;
    }

    /**
     * Finalize plan
     *
     * @param planId Plan Id to finalize
     */
    public void finalizePlan(ObjectId planId) {
        if (planId != null) {
            planService.finalizePlan(planId, loginDataBean.getId());
        }
    }
}
