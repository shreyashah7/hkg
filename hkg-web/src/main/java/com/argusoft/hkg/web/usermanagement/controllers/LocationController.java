/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.usermanagement.controllers;

import com.argusoft.hkg.web.base.PrimaryKey;
import com.argusoft.hkg.web.base.ResponseCode;
import com.argusoft.hkg.web.base.ResponseEntity;
import com.argusoft.hkg.web.base.controllers.BaseController;
import com.argusoft.hkg.web.usermanagement.databeans.LocationDataBean;
import com.argusoft.hkg.web.usermanagement.transformers.LocationTransformerBean;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author anita
 */
@RestController
@RequestMapping("/location")
public class LocationController extends BaseController<LocationDataBean, Long> {

    @Autowired
    LocationTransformerBean locationTransformerBean;

    @Override
    public ResponseEntity<List<LocationDataBean>> retrieveAll() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ResponseEntity<LocationDataBean> retrieveById(PrimaryKey<Long> primaryKey) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ResponseEntity<PrimaryKey<Long>> update(LocationDataBean t) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ResponseEntity<PrimaryKey<Long>> create(LocationDataBean t) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ResponseEntity<LocationDataBean> deleteById(PrimaryKey<Long> primaryKey) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @RequestMapping(value = "/retrieve/locations", method = RequestMethod.GET)
    public ResponseEntity<Map> retrieveAllLocation(@RequestParam(value = "active") String active) {
        Map map = new HashMap();
        map = locationTransformerBean.retieveLocation(active);
        return new ResponseEntity<>(map, ResponseCode.SUCCESS, "", null);
    }

    @RequestMapping(value = "/retrieve/locationtree", method = RequestMethod.GET)
    public ResponseEntity<List<LocationDataBean>> retriveAllLocationTree(@RequestParam("key") String active) {
        List<LocationDataBean> list = new ArrayList<LocationDataBean>();
        list = locationTransformerBean.retrieveLocationsTree(active);
        return new ResponseEntity<>(list, ResponseCode.SUCCESS, "", null);
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST, consumes = {"application/json"})
    public ResponseEntity<Map> saveAllLocations(@Valid @RequestBody LocationDataBean locationDataBean) {
        Map map = new HashMap();
        map = locationTransformerBean.saveLocation(locationDataBean);
        if (map.get("result").equals("00")) {
            return new ResponseEntity<Map>(map, ResponseCode.FAILURE, "Location already exists.", null);
        } else if (map.get("result").equals("1")) {
//            System.out.println("result is one");
            return new ResponseEntity<Map>(map, ResponseCode.SUCCESS, "Location added successfully.", null);
        } else {
            return new ResponseEntity<Map>(map, ResponseCode.FAILURE, "Error in adding location.", null);
        }
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST, consumes = {"application/json"})
    public ResponseEntity<Map> updateLocations(@Valid @RequestBody LocationDataBean locationDataBean) {
        Map map = new HashMap();
        map = locationTransformerBean.updateLocation(locationDataBean);
        if (map.get("result").equals("00")) {
            return new ResponseEntity<Map>(map, ResponseCode.FAILURE, "Location already exists.", null);
        } else if (map.get("result").equals("1")) {
            return new ResponseEntity<Map>(map, ResponseCode.SUCCESS, "Location successfully updated.", null);
        } else {
            return new ResponseEntity<Map>(map, ResponseCode.FAILURE, "Error in updating location.", null);
        }
    }

    @RequestMapping(value = "/searchByLocationName", method = RequestMethod.GET, produces = {"application/json"})
    public List<LocationDataBean> searchDepartment(@RequestParam("q") String query) {
        List<LocationDataBean> searchLocation = locationTransformerBean.searchLocation(query);
        return searchLocation;
    }

    @Override
    public ResponseEntity<Map<String, Object>> retrievePrerequisite() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
