/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.center.rapcalc.transformers;

import com.argusoft.hkg.nosql.core.HkCustomFieldService;
import com.argusoft.hkg.nosql.core.HkPlanService;
import com.argusoft.hkg.web.center.usermanagement.databeans.LoginDataBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author shruti
 */
@Service
public class CalcPlanTransformer {

    @Autowired
    private HkPlanService calcPlanService;

    @Autowired
    private HkCustomFieldService customFieldService;

    @Autowired
    private LoginDataBean loginDataBean;

//    public static Map<String, String> getDbFieldMapFromFieldValue(Map<String, Object> fieldValue) {
//        if (!CollectionUtils.isEmpty(fieldValue)) {
//            Map<String, String> dbTypeMap = new HashMap<>();
//            for (Map.Entry<String, Object> entrySet : fieldValue.entrySet()) {
//                String key = entrySet.getKey();
//                String[] split = key.split("\\$");
//                if (split.length >= 3) {
//                    dbTypeMap.put(key, split[2]);
//                }
//            }
//            return dbTypeMap;
//        }
//        return null;
//    }
//
//    public CalcPlanDocument convertCalcPlanDatabeanToCalcPlanDocument(CalcPlanDatabean databean, CalcPlanDocument document) {
//        if (document == null) {
//            document = new CalcPlanDocument();
//        }
//        document.setCreatedBy(databean.getCreatedBy());
//        document.setCreatedOn(databean.getCreatedOn());
//        if (!StringUtils.isEmpty(databean.getId())) {
//            document.setId(new ObjectId(databean.getId()));
//        }
//        document.setIsActive(databean.getIsActive());
//        document.setIsArchive(databean.getIsArchive());
//        document.setLastModifiedBy(databean.getLastModifiedBy());
//        if (!StringUtils.isEmpty(databean.getLot())) {
//            document.setLot(new ObjectId(databean.getLot()));
//        }
//        if (!StringUtils.isEmpty(databean.getPacket())) {
//            document.setPacket(new ObjectId(databean.getPacket()));
//        }
//        document.setSequence(databean.getSequence());
//        document.setStatus(databean.getStatus());
//        //set field value
//        return document;
//    }
//
//    public CalcPlanDatabean convertCalcPlanDatabeanToCalcPlanDocument(CalcPlanDocument document, CalcPlanDatabean databean) {
//        if (databean == null) {
//            databean = new CalcPlanDatabean();
//        }
//        databean.setCreatedBy(document.getCreatedBy());
//        databean.setCreatedOn(document.getCreatedOn());
//        if (document.getId() != null) {
//            databean.setId(document.getId().toString());
//        }
//        databean.setIsActive(document.getIsActive());
//        databean.setIsArchive(document.getIsArchive());
//        databean.setLastModifiedBy(document.getLastModifiedBy());
//        if (document.getLot() != null) {
//            databean.setLot(document.getLot().toString());
//        }
//        if (document.getPacket() != null) {
//            databean.setPacket(document.getPacket().toString());
//        }
//        databean.setSequence(document.getSequence());
//        databean.setStatus(document.getStatus());
//        databean.setPlanCustom(document.getFieldValue().toMap());
//        return databean;
//    }
//
//    public String save(CalcPlanDatabean planDatabean) {
//        if (CollectionUtils.isEmpty(planDatabean.getLotDbType())) {
//            planDatabean.setLotDbType(getDbFieldMapFromFieldValue(planDatabean.getPlanCustom()));
//        }
//        CalcPlanDocument planDocument = convertCalcPlanDatabeanToCalcPlanDocument(planDatabean, null);
//        BasicBSONObject fieldValue = customFieldService.makeBSONObject(planDatabean.getPlanCustom(), planDatabean.getLotDbType(), HkSystemConstantUtil.Feature.PLAN, loginDataBean.getCompanyId(), null);
//        if (fieldValue != null) {
//            planDocument.setFieldValue(fieldValue);
//        }
//        planDocument.setFranchiseId(loginDataBean.getCompanyId());
//        planDocument.setCreatedBy(loginDataBean.getId());
//        planDocument.setLastModifiedBy(loginDataBean.getId());
//        planDocument.setCreatedOn(new Date());
//        planDocument.setModifiedOn(new Date());
//        calcPlanService.save(planDocument);
//        return (planDocument.getId() != null) ? planDocument.getId().toString() : null;
//    }
//
//    public String update(CalcPlanDatabean planDatabean) {
//        if (CollectionUtils.isEmpty(planDatabean.getLotDbType())) {
//            planDatabean.setLotDbType(getDbFieldMapFromFieldValue(planDatabean.getPlanCustom()));
//        }
//        CalcPlanDocument planDocument = convertCalcPlanDatabeanToCalcPlanDocument(planDatabean, null);
//        planDocument.setFranchiseId(loginDataBean.getCompanyId());
//        planDocument.setCreatedByFranchise(loginDataBean.getCompanyId());
//        planDocument.setCreatedBy(loginDataBean.getId());
//        planDocument.setLastModifiedBy(loginDataBean.getId());
//        planDocument.setCreatedOn(new Date());
//        planDocument.setModifiedOn(new Date());
//        calcPlanService.update(planDocument, planDatabean.getLotDbType(), planDatabean.getPlanCustom());
//        return (planDocument.getId() != null) ? planDocument.getId().toString() : null;
//    }
//
//    public List<CalcPlanDatabean> retrieveAll() {
//        List<CalcPlanDocument> retrieveAll = calcPlanService.retrieveAll();
//        if (!CollectionUtils.isEmpty(retrieveAll)) {
//            List<CalcPlanDatabean> databeanList = new LinkedList<>();
//            for (CalcPlanDocument document : retrieveAll) {
//                databeanList.add(convertCalcPlanDatabeanToCalcPlanDocument(document, null));
//            }
//            return databeanList;
//        } else {
//            return null;
//        }
//
//    }
//
//    public List<CalcPlanDatabean> retrieveByLotId(ObjectId lotId) {
//        List<CalcPlanDocument> retrieveAll = calcPlanService.retrieveByLotId(lotId);
//        if (!CollectionUtils.isEmpty(retrieveAll)) {
//            List<CalcPlanDatabean> databeanList = new LinkedList<>();
//            for (CalcPlanDocument document : retrieveAll) {
//                databeanList.add(convertCalcPlanDatabeanToCalcPlanDocument(document, null));
//            }
//            return databeanList;
//        } else {
//            return null;
//        }
//
//    }
//
//    public void deleteAll(List<String> idList) {
//        calcPlanService.deleteAll(idList);
//    }
//
//    public List<CalcPlanDatabean> retrieveByPacketId(ObjectId packetId) {
//        List<CalcPlanDocument> retrieveAll = calcPlanService.retrieveByLotId(packetId);
//        if (!CollectionUtils.isEmpty(retrieveAll)) {
//            List<CalcPlanDatabean> databeanList = new LinkedList<>();
//            for (CalcPlanDocument document : retrieveAll) {
//                databeanList.add(convertCalcPlanDatabeanToCalcPlanDocument(document, null));
//            }
//            return databeanList;
//        } else {
//            return null;
//        }
//    }
}
