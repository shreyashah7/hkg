/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.center.stock.controllers;

import com.argusoft.hkg.common.constantutil.HkSystemConstantUtil;
import com.argusoft.hkg.common.functionutil.FolderManagement;
import com.argusoft.hkg.web.base.PrimaryKey;
import com.argusoft.hkg.web.base.ResponseCode;
import com.argusoft.hkg.web.base.ResponseEntity;
import com.argusoft.hkg.web.base.controllers.BaseController;
import com.argusoft.hkg.web.center.stock.databeans.PacketDataBean;
import com.argusoft.hkg.web.center.stock.transformers.PacketTransformer;
import com.argusoft.hkg.web.util.SelectItem;
import com.sun.jmx.mbeanserver.Util;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import net.sf.dynamicreports.report.exception.DRException;
import net.sf.jasperreports.engine.JRException;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author shreya
 */
@RestController
@RequestMapping("/packet")
public class PacketController extends BaseController<PacketDataBean, Long> {

    @Autowired
    PacketTransformer packetTransformer;
    private static final Logger LOGGER = LoggerFactory.getLogger(PacketController.class);

    @Override
    public ResponseEntity<List<PacketDataBean>> retrieveAll() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ResponseEntity<PacketDataBean> retrieveById(PrimaryKey<Long> primaryKey) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ResponseEntity<PrimaryKey<Long>> create(PacketDataBean t) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ResponseEntity<PrimaryKey<Long>> update(@RequestBody PacketDataBean packetDataBean) {
        String packetId = packetTransformer.updatePacket(packetDataBean);
        if (StringUtils.hasText(packetId)) {
            return new ResponseEntity<>(null, ResponseCode.SUCCESS, "Packet updated successfully, [packet id:" + packetId + "]", null, true);
        } else {
            return new ResponseEntity<>(null, ResponseCode.FAILURE, "Packet can not be updated", null, true);
        }
    }

    @RequestMapping(value = "/createpacket", method = RequestMethod.POST, produces = {"application/json"})
    public ResponseEntity<Map<String, Object>> createPacket(@RequestBody PacketDataBean packetDataBean) {
        Map<String, Object> packetMap = null;
        packetMap = packetTransformer.createPacket(packetDataBean);
        System.out.println("packetMap :" + packetMap);
        if (!CollectionUtils.isEmpty(packetMap) && packetMap.get(HkSystemConstantUtil.PacketStaticFieldName.PACKET_ID) != null) {
            String packetId = packetMap.get(HkSystemConstantUtil.PacketStaticFieldName.PACKET_ID).toString();
            return new ResponseEntity<>(packetMap, ResponseCode.SUCCESS, "Packet created successfully, packet id:" + packetId, null, true);
        } else {
            return new ResponseEntity<>(null, ResponseCode.FAILURE, "Packet can not be added", null, false);
        }
    }

    @Override
    public ResponseEntity<PacketDataBean> deleteById(PrimaryKey<Long> primaryKey) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ResponseEntity<Map<String, Object>> retrievePrerequisite() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @RequestMapping(value = "/retrievepacketbylotid", method = RequestMethod.POST, produces = {"application/json"})
    public ResponseEntity<List<SelectItem>> retrievePacketByLotId(@RequestBody Map<String, Object> map) {
        return new ResponseEntity<>(packetTransformer.retrievePacketByLotId(map), ResponseCode.SUCCESS, "", null);
    }

    @RequestMapping(value = "/retrievesearchedpacket", method = RequestMethod.POST, produces = {"application/json"})
    public ResponseEntity<List<SelectItem>> retrieveSearchedPacket(@RequestBody PacketDataBean packetDataBean) {
        return new ResponseEntity<>(packetTransformer.searchPacket(packetDataBean), ResponseCode.SUCCESS, "", null, true);
    }

    @RequestMapping(value = "/retrievepacketbyid", method = RequestMethod.POST, produces = {"application/json"})
    public ResponseEntity<List<SelectItem>> retrievePacketById(@RequestBody Map<String, Object> featureDbfieldNameMap) {
        List<SelectItem> selectItem = packetTransformer.retrievePacketById(featureDbfieldNameMap);
        LOGGER.info("select item" + selectItem);
        return new ResponseEntity<>(selectItem, ResponseCode.SUCCESS, "", null, true);
    }

    @RequestMapping(value = "/generateBarcode", method = RequestMethod.POST)
    public Map<String, String> generateBarcode(@RequestBody String payload) throws IOException, FileNotFoundException, DRException, JRException {
        Map<String, String> tempFilePath = new HashMap<>();
        if (!StringUtils.isEmpty(payload)) {
            String filePath = packetTransformer.printBarcode(payload);
            tempFilePath.put("tempFilePath", filePath);
        }
        return tempFilePath;
    }

    @RequestMapping(value = "/getimage/{name}", method = RequestMethod.GET, produces = {"application/pdf"})
    @ResponseBody
    public FileSystemResource getFile(@PathVariable("name") String fileName, HttpServletRequest request) {
        //System.out.println("fileName :"+fileName);
        if (fileName != null && !fileName.equals("")) {
            String pathFromImageName = FolderManagement.getOnlyPathOfFile(fileName + ".pdf");
            //System.out.println("pathFromImageName :"+pathFromImageName);
            return new FileSystemResource(pathFromImageName);
        }
        return null;
    }

    @RequestMapping(value = "/ispacketsequencexist", method = RequestMethod.PUT, produces = {"application/json"})
    public ResponseEntity<Boolean> isPacketSequenceNumberExist(@RequestBody Map<String, String> map) {
        System.out.println("map in isPacketSequenceNumberExist" + map);
        boolean fieldSequenceExist = packetTransformer.isPacketSequenceExist(map);
        return new ResponseEntity<>(fieldSequenceExist, ResponseCode.SUCCESS, null, null, false);
    }

    @RequestMapping(value = "/getnextpacketsequence", method = RequestMethod.PUT, produces = {"application/json"})
    public ResponseEntity<Map<String, Object>> getNextPacketSequence(@RequestBody String lotObjectId) {
        Map<String, Object> nextPacketSequence = packetTransformer.getNextPacketSequence(lotObjectId);
        if (!CollectionUtils.isEmpty(nextPacketSequence)) {
            return new ResponseEntity<>(nextPacketSequence, ResponseCode.SUCCESS, null, null, true);
        }
        return new ResponseEntity<>(null, ResponseCode.FAILURE, "Failed to retrieve sequence number", null, false);
    }

    @RequestMapping(value = "/deletepacket", method = RequestMethod.PUT, produces = {"application/json"})
    public ResponseEntity<Boolean> deletePacket(@RequestBody String packetId) {
        Boolean success = packetTransformer.deletePacket(packetId);
        if (success) {
            return new ResponseEntity<>(success, ResponseCode.SUCCESS, "Packet deleted successfully", null, false);
        }
        return new ResponseEntity<>(success, ResponseCode.FAILURE, "Failed to delete packet, please try again.", null, false);
    }

    @RequestMapping(value = "/retrievesplitpacket", method = RequestMethod.POST, produces = {"application/json"})
    public ResponseEntity<List<SelectItem>> retrieveSplitPacket(@RequestBody Map<String, Object> map) {
        if (!CollectionUtils.isEmpty(map)) {
            return new ResponseEntity<>(packetTransformer.retrieveSplitPacket(map), ResponseCode.SUCCESS, null, null, false);
        } else {
            return new ResponseEntity<>(null, ResponseCode.SUCCESS, "Packet deleted successfully", null, false);
        }
    }
}
