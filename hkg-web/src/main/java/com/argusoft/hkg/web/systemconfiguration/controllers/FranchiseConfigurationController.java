/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.systemconfiguration.controllers;

import com.argusoft.generic.database.exception.GenericDatabaseException;
import com.argusoft.hkg.common.functionutil.FolderManagement;
import com.argusoft.hkg.web.base.PrimaryKey;
import com.argusoft.hkg.web.base.ResponseCode;
import com.argusoft.hkg.web.base.ResponseEntity;
import com.argusoft.hkg.web.base.controllers.BaseController;
import com.argusoft.hkg.web.systemconfiguration.transformers.FranchiseConfigrationTransformerBean;
import com.argusoft.hkg.web.center.usermanagement.databeans.LoginDataBean;
import com.argusoft.hkg.web.usermanagement.transformers.FeatureTransformerBean;
import com.argusoft.hkg.web.util.SelectItem;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author kuldeep
 */
@RestController
@RequestMapping("/franchiseconfig")
public class FranchiseConfigurationController extends BaseController<Map, Long> {

    @Autowired
    FranchiseConfigrationTransformerBean franchiseConfigrationTransformerBean;
    @Autowired
    FeatureTransformerBean featureTransformerBean;
    @Autowired
    LoginDataBean loginDataBean;

    @Override
    public ResponseEntity<List<Map>> retrieveAll() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ResponseEntity<Map> retrieveById(@RequestBody PrimaryKey<Long> primaryKey) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ResponseEntity<PrimaryKey<Long>> update(@RequestBody Map configurationMap) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ResponseEntity<PrimaryKey<Long>> create(@RequestBody Map configurationMap) {
        try {
            franchiseConfigrationTransformerBean.createConfiguration(configurationMap);
        } catch (GenericDatabaseException ex) {
            Logger.getLogger(FranchiseConfigurationController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ResponseEntity<>(null, ResponseCode.SUCCESS, "Configuration saved succesfully.", null, false);
    }

    @Override
    public ResponseEntity<Map> deleteById(PrimaryKey<Long> primaryKey) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @RequestMapping(value = "/retrieveallconfig", method = RequestMethod.GET)
    public ResponseEntity<Map<String, String>> retrieveAllConfigurations() {
        return new ResponseEntity<>(franchiseConfigrationTransformerBean.retrieveAllConfiguration(), ResponseCode.SUCCESS, null, null);
    }

    @RequestMapping(value = "/checkoldpassword", method = RequestMethod.POST)
    public ResponseEntity<PrimaryKey<Long>> checkOldPassword(@RequestBody String oldPassword) {
        if (franchiseConfigrationTransformerBean.checkOldPassword(oldPassword)) {
            return new ResponseEntity<>(null, ResponseCode.SUCCESS, null, null, false);
        } else {
            return new ResponseEntity<>(null, ResponseCode.WARNING, "Old password does not match", null, false);
        }
    }

    @RequestMapping(value = "/retrieveemptypes", method = RequestMethod.GET)
    public ResponseEntity<List<SelectItem>> retrieveEmployeeTypes() {
        return new ResponseEntity<>(franchiseConfigrationTransformerBean.retrieveEmployeeTypes(), ResponseCode.SUCCESS, null, null);
    }

    @RequestMapping(value = "/uploadfile", method = RequestMethod.POST)
    public ResponseEntity<Map> uploadFile(@RequestBody String[] info) throws FileNotFoundException, IOException {
        String filename = info[0];
        String avtarFileFormat = franchiseConfigrationTransformerBean.uploadFile(filename);
        Map result = new HashMap();
        result.put("res", avtarFileFormat);
        return new ResponseEntity<>(result, ResponseCode.SUCCESS, "", null);
    }

    @RequestMapping(value = "/getimage", method = RequestMethod.GET)
    @ResponseBody
    public FileSystemResource getFile(@RequestParam("file_name") String fileName) {
        if (fileName != null && !fileName.equals("")) {
            String pathFromImageName = FolderManagement.getPathOfImage(fileName);
            return new FileSystemResource(pathFromImageName);
        }
        return null;
    }

    @RequestMapping(value = "/removeFileFromTemp", method = RequestMethod.POST, consumes = {"application/json"})
    @ResponseStatus(value = HttpStatus.OK)
    public void removeFileFromTemp(@RequestBody String name) throws IOException {
        FolderManagement.removeFile(name);
    }

    @Override
    public ResponseEntity<Map<String, Object>> retrievePrerequisite() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


    @RequestMapping(value = "/retrieveFranchiseDesignations", method = RequestMethod.GET)
    public List<SelectItem> retrieveFranchiseDesignation() {
        List<SelectItem> listFranchiseDesignations = franchiseConfigrationTransformerBean.retrieveDesignations(false);
        return listFranchiseDesignations;
    }
}
