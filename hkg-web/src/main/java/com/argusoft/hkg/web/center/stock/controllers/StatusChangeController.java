package com.argusoft.hkg.web.center.stock.controllers;

import com.argusoft.generic.database.exception.GenericDatabaseException;
import com.argusoft.hkg.common.constantutil.HkSystemConstantUtil;
import com.argusoft.hkg.web.base.ResponseCode;
import com.argusoft.hkg.web.base.ResponseEntity;
import com.argusoft.hkg.web.center.stock.databeans.StatusChangeDataBean;
import com.argusoft.hkg.web.center.stock.transformers.ChangeStatusTransformer;
import com.argusoft.hkg.web.util.SelectItem;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author brijesh
 */
@RestController
@RequestMapping("/statuschange")
public class StatusChangeController {

    @Autowired
    ChangeStatusTransformer changeStatusTransformer;

    @RequestMapping(value = "/searchlots", method = RequestMethod.POST, consumes = {"application/json"})
    public List<SelectItem> retrieveSearchedStock(@RequestBody StatusChangeDataBean statusChangeDataBean) throws GenericDatabaseException {

        //System.out.println("----retrieveIssueStocks-----feature map-------" + statusChangeDataBean.getFeatureMap());
        List<String> invoiceDbFieldName = new ArrayList<>();
        List<String> parcelDbFieldName = new ArrayList<>();
        List<String> lotDbFieldName = new ArrayList<>();
        List<String> packetDbFieldName = new ArrayList<>();
        for (Map.Entry<String, Object> entrySet : statusChangeDataBean.getFeatureMap().entrySet()) {
            String key = entrySet.getKey();
            Object value = entrySet.getValue();
            if (value.equals(HkSystemConstantUtil.Feature.LOT)) {
                lotDbFieldName.add(key);
            } else if (value.equals(HkSystemConstantUtil.Feature.INVOICE)) {
                invoiceDbFieldName.add(key);
            } else if (value.equals(HkSystemConstantUtil.Feature.PARCEL)) {
                parcelDbFieldName.add(key);
            } else if (value.equals(HkSystemConstantUtil.Feature.PACKET)) {
                packetDbFieldName.add(key);
            }
        }
        List<SelectItem> selectItems = changeStatusTransformer.searchLot(statusChangeDataBean, invoiceDbFieldName, lotDbFieldName, packetDbFieldName, parcelDbFieldName);
        return selectItems;
    }

    @RequestMapping(value = "/retrievelot", method = RequestMethod.POST)
    public List<StatusChangeDataBean> retrieveStockByLotIdOrPacketId(@RequestBody Map reqPayload) throws GenericDatabaseException {
        Map<String, Object> payload = (Map) reqPayload.get("payload");
        Map<String, List<String>> featureDbfieldNameMap = (Map) reqPayload.get("fields");
        List<StatusChangeDataBean> retrievedStatusChangeDataBean = new ArrayList<>();
        List<StatusChangeDataBean> changeDataBeans = new ArrayList<>();
        if (!CollectionUtils.isEmpty(payload) && !CollectionUtils.isEmpty(featureDbfieldNameMap)) {
            List<String> lotIds = (List<String>) payload.get("lotids");
            List<String> packetIds = (List<String>) payload.get("packetids");
            List<ObjectId> lotObjectIds = new ArrayList<>();
            List<ObjectId> packetObjectIds = new ArrayList<>();
            if (!CollectionUtils.isEmpty(packetIds)) {
                for (String packetId : packetIds) {
                    packetObjectIds.add(new ObjectId(packetId));
                }
                retrievedStatusChangeDataBean = changeStatusTransformer.retrieveStockByLotIdOrPacketId(packetObjectIds, HkSystemConstantUtil.Feature.PACKET, featureDbfieldNameMap);

            }
            if (!CollectionUtils.isEmpty(lotIds)) {
                for (String lotId : lotIds) {
                    lotObjectIds.add(new ObjectId(lotId));
                }
                changeDataBeans = changeStatusTransformer.retrieveStockByLotIdOrPacketId(lotObjectIds, HkSystemConstantUtil.Feature.LOT, featureDbfieldNameMap);

            }
            if (!CollectionUtils.isEmpty(changeDataBeans)) {
                retrievedStatusChangeDataBean.addAll(changeDataBeans);
            }
        }
        return retrievedStatusChangeDataBean;
    }

    @RequestMapping(value = "/retrievestatusmap", method = RequestMethod.GET)
    public Map<String, List<String>> retrieveStatusMap() {
        HashMap<String, List<String>> statusMap = new HashMap<>();
        statusMap.putAll(HkSystemConstantUtil.PROPOSED_STATUS_MAP);
        return statusMap;
    }

    @RequestMapping(value = "/onsave", method = RequestMethod.POST)
    public ResponseEntity<Boolean> onSave(@RequestBody Map<String, Object> payload) throws GenericDatabaseException {
        Map<String, Object> lotCustom = (Map<String, Object>) payload.get("lotCustom");
        Map<String, Object> packetCustom = (Map<String, Object>) payload.get("packetCustom");
        Map<String, String> lotdbType = (Map<String, String>) payload.get("lotdbType");
        Map<String, String> packetdbType = (Map<String, String>) payload.get("packetdbType");
        List<String> lotIds = (List<String>) payload.get("lotIds");
        List<String> packetIds = (List<String>) payload.get("packetIds");
        List<ObjectId> lotObjectIds = new ArrayList<>();
        List<ObjectId> packetObjectIds = new ArrayList<>();
        if (!CollectionUtils.isEmpty(lotIds)) {
            for (String lotId : lotIds) {
                lotObjectIds.add(new ObjectId(lotId));
            }
        }
        if (!CollectionUtils.isEmpty(packetIds)) {
            for (String packetId : packetIds) {
                packetObjectIds.add(new ObjectId(packetId));
            }
        }
        String status = (String) payload.get("status");

        Boolean statusChanged = changeStatusTransformer.changeSave(lotCustom, packetCustom, lotdbType, packetdbType, lotObjectIds, packetObjectIds, status);
        if (statusChanged) {
            return new ResponseEntity<>(statusChanged, ResponseCode.SUCCESS, "", null);
        } else {
            return new ResponseEntity<>(statusChanged, ResponseCode.FAILURE, "", null);

        }

    }

}
