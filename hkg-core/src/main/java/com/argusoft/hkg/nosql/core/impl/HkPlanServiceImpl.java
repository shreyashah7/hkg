package com.argusoft.hkg.nosql.core.impl;

import com.argusoft.generic.database.common.MongoGenericDao;
import com.argusoft.hkg.common.constantutil.HkSystemConstantUtil;
import com.argusoft.hkg.nosql.core.HkConfigurationService;
import com.argusoft.hkg.nosql.core.HkFeatureService;
import com.argusoft.hkg.nosql.core.HkPlanService;
import com.argusoft.hkg.nosql.core.HkStockService;
import com.argusoft.hkg.nosql.model.HkPacketDocument;
import com.argusoft.hkg.nosql.model.HkPacketPlanDocument;
import com.argusoft.hkg.nosql.model.HkRoleFeatureModifierDocument;
import com.argusoft.sync.center.model.SyncCenterFeatureDocument;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 *
 * @author shruti
 */
@Service
public class HkPlanServiceImpl implements HkPlanService {

    @Autowired
    private MongoGenericDao mongoGenericDao;

    @Autowired
    private HkStockService hkStockService;

    @Autowired
    private HkFeatureService featureService;

    @Autowired
    private HkConfigurationService configurationService;

    private static class PacketPlan {

        public static final String PACKET = "packet";
        public static final String LOT = "lot";
        public static final String IS_ACTIVE = "isActive";
        public static final String PACKET_NUMBER = "packetNumber";
        public static final String EMP_ID = "empId";
        public static final String DEPARTMENT_ID = "departmentId";
        public static final String DESIGNATION_ID = "designationId";
        public static final String FINAL_PLAN = "finalPlan";
    }

    @Override
    public Map<Integer, ObjectId> savePlans(List<HkPacketPlanDocument> plans, String packetNumber, Long userId, Long companyId) {
        if (!StringUtils.isEmpty(packetNumber)) {
            Map<String, Object> fieldValues = new HashMap<>();
            fieldValues.put(HkSystemConstantUtil.PacketStaticFieldName.PACKET_ID, packetNumber);
            fieldValues.put(HkSystemConstantUtil.PacketStaticFieldName.IN_STOCK_OF, Arrays.asList(userId + ":E"));
            List<HkPacketDocument> packets = hkStockService.retrievePackets(fieldValues, null, null, null, companyId, false, null, null,null,null);
            if (!CollectionUtils.isEmpty(packets)) {
                HkPacketDocument packet = packets.get(0);
                Map<Integer, ObjectId> planToId = new HashMap<>();
                if (!CollectionUtils.isEmpty(plans) && packet != null) {
                    for (HkPacketPlanDocument _item : plans) {
                        _item.setInvoice(packet.getInvoice());
                        _item.setParcel(packet.getParcel());
                        _item.setLot(packet.getLot());
                        _item.setPacket(packet.getId());
                        mongoGenericDao.update(_item);
                        planToId.put(_item.getPlanId(), _item.getId());
                    }
                }
                return planToId;
            }
        }
        Map<Integer, ObjectId> map = new HashMap<>();
        map.put(-1, null);
        return map;
    }

    @Override
    public List<HkPacketPlanDocument> retrievePlansByPacketNumber(String packetNumber, Long designationId, Long companyId, Long userId) {
        List<Criteria> criterias = new LinkedList<>();
        //Retrieve modifiers of the plan to retrieve plans according to permissions
        SyncCenterFeatureDocument featureDoc = featureService.retireveFeatureByName(HkSystemConstantUtil.Feature.ESTIMATE_PREDICTION);
        List<HkRoleFeatureModifierDocument> modifiers = configurationService.retrieveModifiersByDesignations(Arrays.asList(designationId), companyId, featureDoc.getId());

        if (!CollectionUtils.isEmpty(modifiers)) {
            List<String> planAccess = modifiers.get(0).getPlanAccess();
            //Retrieve configured receipients for plan
            List<Map<String, String>> recepients = modifiers.get(0).getShowPlanUsers();

            if (!CollectionUtils.isEmpty(planAccess)) {
                //If SHOW_PLANS modifier selected than apply filters
                if (planAccess.indexOf(HkSystemConstantUtil.EstimatedPlanModifier.SHOW_PLANS) != -1) {
                    if (!CollectionUtils.isEmpty(recepients)) {
                        Criteria criteria = new Criteria();
                        List<Criteria> orCriterias = new LinkedList<>();
                        //Loop through all configured combination of recipients and status combination
                        for (Map<String, String> map : recepients) {
                            List<Criteria> andCriteriaMain = new ArrayList<>();
                            Criteria andCriteriaInner = new Criteria();
                            List<Criteria> orCriteriasInner = new LinkedList<>();

                            //Get configured receipients's codes from map
                            String configuredReceipients = map.get(HkSystemConstantUtil.PlanConstants.USERS);
                            //Split code by comma to get Id and type of recipients
                            List<String> recepientsCodes = Arrays.asList(configuredReceipients.split(","));
                            List<Long> empIds = new ArrayList<>();
                            List<Long> designationIds = new ArrayList<>();
                            List<Long> departmentIds = new ArrayList<>();
                            final String SEPARATOR = ":";
                            //Sort out recipients in group of employee,department and designation
                            for (String recipientCode : recepientsCodes) {
                                StringTokenizer tokenizer = new StringTokenizer(recipientCode, SEPARATOR);
                                if (tokenizer.countTokens() == 2) {
                                    Long id = new Long(tokenizer.nextToken());
                                    String type = tokenizer.nextToken();
                                    switch (type) {
                                        case HkSystemConstantUtil.RecipientCodeType.EMPLOYEE:
                                            empIds.add(id);
                                            break;
                                        case HkSystemConstantUtil.RecipientCodeType.DEPARTMENT:
                                            departmentIds.add(id);
                                            break;
                                        case HkSystemConstantUtil.RecipientCodeType.DESIGNATION:
                                            designationIds.add(id);
                                            break;
                                    }
                                }
                            }
                            //If employee ids are not empty than apply filter of employee ids
                            if (!CollectionUtils.isEmpty(empIds)) {
                                empIds.add(userId);
                                orCriteriasInner.add(where(PacketPlan.EMP_ID).in(empIds));
                            }
                            //If department ids are not empty than apply filter of department ids
                            if (!CollectionUtils.isEmpty(departmentIds)) {
                                orCriteriasInner.add(where(PacketPlan.DEPARTMENT_ID).in(departmentIds));
                            }
                            //If designation ids are not empty than apply filter of designation ids
                            if (!CollectionUtils.isEmpty(designationIds)) {
                                orCriteriasInner.add(where(PacketPlan.DESIGNATION_ID).in(designationIds));
                            }
                            if (!CollectionUtils.isEmpty(orCriteriasInner)) {
                                andCriteriaMain.add(new Criteria().orOperator(orCriteriasInner.toArray(new Criteria[0])));
                            }
                            Criteria statusCriteria = new Criteria();

                            //Get status from the configuration
                            String status = map.get(HkSystemConstantUtil.PlanConstants.STATUS);
                            if (!StringUtils.isEmpty(status)) {
                                //Apply status filter if it is present
                                if (status.equalsIgnoreCase(HkSystemConstantUtil.PlanStatus.SUBMITTED)) {
                                    statusCriteria = Criteria.where(PacketPlan.FINAL_PLAN).is(false);
                                } else if (status.equalsIgnoreCase(HkSystemConstantUtil.PlanStatus.FINALIZED)) {
                                    statusCriteria = Criteria.where(PacketPlan.FINAL_PLAN).is(true);
                                } else {
                                    statusCriteria = Criteria.where(PacketPlan.FINAL_PLAN).in(Arrays.asList(true, false));
                                }
                                andCriteriaMain.add(statusCriteria);
                            }
                            if (!CollectionUtils.isEmpty(andCriteriaMain)) {
                                //AND operation between recipients criteria and status criteria
                                andCriteriaInner.andOperator(andCriteriaMain.toArray(new Criteria[0]));
                                orCriterias.add(andCriteriaInner);
                            }
                        }
                        if (!CollectionUtils.isEmpty(orCriterias)) {
                            //OR operation between all the filters
                            criteria.orOperator(orCriterias.toArray(new Criteria[0]));
                            criterias.add(criteria);
                        }
                    }
                } else {
                    //If SHOW_PLAN is not configured than apply filter on empid to retrieve their plans
                    criterias.add(where(PacketPlan.EMP_ID).is(userId));
                }
            } else {
                criterias.add(where(PacketPlan.EMP_ID).is(userId));
            }
        } else {
            //No modifiers than retrieve empId's all modifiers
            criterias.add(where(PacketPlan.EMP_ID).is(userId));
        }

        criterias.add(where(PacketPlan.IS_ACTIVE).is(Boolean.TRUE));
        criterias.add(where(PacketPlan.PACKET_NUMBER).is(packetNumber));

        return mongoGenericDao.findByCriteria(criterias, HkPacketPlanDocument.class);
    }

    @Override
    public List<HkPacketPlanDocument> retrieveFinalPlans(String packetId) {
        List<HkPacketPlanDocument> result = new ArrayList<>();
        if (!StringUtils.isEmpty(packetId)) {

            List<Criteria> criterias = new LinkedList<>();
            criterias.add(where(PacketPlan.PACKET_NUMBER).is(packetId));
            criterias.add(where(PacketPlan.FINAL_PLAN).is(true));
            criterias.add(where(PacketPlan.IS_ACTIVE).is(true));

            result = mongoGenericDao.findByCriteria(criterias, HkPacketPlanDocument.class);
        }
        return result;
    }

    @Override
    public void finalizePlan(ObjectId planId, Long id) {
        if (planId != null) {
            HkPacketPlanDocument plan = (HkPacketPlanDocument) mongoGenericDao.retrieveById(planId, HkPacketPlanDocument.class);
            if (plan != null) {
                HkPacketDocument packet = hkStockService.retrievePacketById(plan.getPacket(),true);
                packet.setFinalPlanId(planId);
                packet.setLastModifiedBy(id);
                packet.setLastModifiedOn(new Date());
                mongoGenericDao.update(packet);
            }
        }
    }
}
