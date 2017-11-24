/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.nosql.core;

import com.argusoft.hkg.nosql.model.HkPacketPlanDocument;
import java.util.List;
import java.util.Map;
import org.bson.types.ObjectId;

/**
 *
 * @author shruti
 */
public interface HkPlanService {

    /**
     * Save list of plans
     *
     * @param plans to save
     * @param packetNumber
     * @param userId
     * @param companyId
     * @return Mapping of Plan Id to ObjectId
     */
    public Map<Integer, ObjectId> savePlans(List<HkPacketPlanDocument> plans, String packetNumber, Long userId, Long companyId);

    /**
     * Retrieve Plans by packet number
     *
     * @param packetNumber Packet number
     * @param designationId
     * @param companyId
     * @param userId
     * @return List of plans associated with packet
     */
    public List<HkPacketPlanDocument> retrievePlansByPacketNumber(String packetNumber, Long designationId, Long companyId, Long userId);

    /**
     * Retrieve plans marked as final of specified packet number
     *
     * @param packetId
     * @return Final plans
     */
    public List<HkPacketPlanDocument> retrieveFinalPlans(String packetId);

    /**
     * Finalize selected plan
     *
     * @param planId PlanId to mark as finalized
     * @param userId UserId
     */
    public void finalizePlan(ObjectId planId, Long userId);

}
