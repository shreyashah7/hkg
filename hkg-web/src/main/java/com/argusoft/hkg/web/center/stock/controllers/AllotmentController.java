package com.argusoft.hkg.web.center.stock.controllers;

import com.argusoft.generic.database.exception.GenericDatabaseException;
import com.argusoft.hkg.common.constantutil.HkSystemConstantUtil;
import com.argusoft.hkg.web.base.PrimaryKey;
import com.argusoft.hkg.web.base.ResponseCode;
import com.argusoft.hkg.web.base.ResponseEntity;
import com.argusoft.hkg.web.base.controllers.BaseController;
import com.argusoft.hkg.web.center.stock.databeans.AllotmentDataBean;
import com.argusoft.hkg.web.center.stock.databeans.UserGradeSuggestionDataBean;
import com.argusoft.hkg.web.center.stock.transformers.AllotmentTransformer;
import com.argusoft.hkg.web.util.SelectItem;
import java.text.ParseException;
import java.util.ArrayList;
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
 * @author shreya
 */
@RestController
@RequestMapping("/allotment")
public class AllotmentController {

    @Autowired
    AllotmentTransformer allotTransformer;

    @RequestMapping(value = "/retrievesearcheddata", method = RequestMethod.POST)
    public ResponseEntity<List<SelectItem>> retrieveSearchedData(@RequestBody AllotmentDataBean allotmentDataBean) throws GenericDatabaseException {
        List<SelectItem> selectItems = allotTransformer.retrieveSearchedData(allotmentDataBean);
        return new ResponseEntity<>(selectItems, ResponseCode.SUCCESS, null, null);
    }

    @RequestMapping(value = "/retrieveusergradesuggestion", method = RequestMethod.POST)
    public ResponseEntity<List<UserGradeSuggestionDataBean>> retrieveUserGradeSuggestion(@RequestBody List<String> packetIds) throws GenericDatabaseException, ParseException {
        if (!CollectionUtils.isEmpty(packetIds)) {
            List<ObjectId> actualPacketIds = new ArrayList<>();
            for (String packetId : packetIds) {
                actualPacketIds.add(new ObjectId(packetId));
            }
            List<UserGradeSuggestionDataBean> userGradeSuggestionDataBeans = allotTransformer.retrieveUserGradeSuggestion(actualPacketIds);
            return new ResponseEntity<>(userGradeSuggestionDataBeans, ResponseCode.SUCCESS, null, null);
        }
        return new ResponseEntity<>(null, ResponseCode.FAILURE, "No Suggestions available", null);
    }

    @RequestMapping(value = "/retrieveusers", method = RequestMethod.POST, consumes = {"application/json"})
    public ResponseEntity<List<SelectItem>> retrieveUsers(@RequestBody(required = false) String user) {
        List<SelectItem> selectItems = allotTransformer.retrieveUsers(user);
        return new ResponseEntity<>(selectItems, ResponseCode.SUCCESS, null, null, false);
    }
    
    @RequestMapping(value = "/retrieveusergradesuggestionbyuserid", method = RequestMethod.POST, consumes = {"application/json"})
    public ResponseEntity<UserGradeSuggestionDataBean> retrieveUserGradeSuggestionByUserId(@RequestBody Long userId) throws ParseException {
        UserGradeSuggestionDataBean userGradeSuggestionDataBean = allotTransformer.retrieveUserGradeSuggestionByUserId(userId);
        return new ResponseEntity<>(userGradeSuggestionDataBean, ResponseCode.SUCCESS, null, null, false);
    }
    
    @RequestMapping(value = "/retrievepacketsavailableinstock", method = RequestMethod.GET, produces = {"application/json"})
    public ResponseEntity<List<SelectItem>> retrievePacketsAvailableInStock() {
        List<SelectItem> selectItems = allotTransformer.retrievePacketsAvailableInStock();
        return new ResponseEntity<>(selectItems, ResponseCode.SUCCESS, null, null, false);
    }
    
    @RequestMapping(value = "/allotpacket", method = RequestMethod.POST, produces = {"application/json"})
    public ResponseEntity<List<SelectItem>> allotPackets(@RequestBody List<UserGradeSuggestionDataBean> userGradeSuggestionDataBeans) {
        Boolean result = allotTransformer.allotPackets(userGradeSuggestionDataBeans);
        if(result){
            return new ResponseEntity<>(null, ResponseCode.SUCCESS, "Packets alloted successfully", null, false);
        }else{
            return new ResponseEntity<>(null, ResponseCode.SUCCESS, "Packets cannot be alloted.", null, false);
        }
        
    }

    
}
