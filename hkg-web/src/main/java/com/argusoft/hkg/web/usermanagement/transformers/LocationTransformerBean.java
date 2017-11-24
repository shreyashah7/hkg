/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.usermanagement.transformers;

import com.argusoft.hkg.common.constantutil.HkSystemConstantUtil;
import com.argusoft.hkg.core.HkLocationService;
import com.argusoft.hkg.model.HkLocationEntity;
import com.argusoft.hkg.web.usermanagement.databeans.LocationDataBean;
import com.argusoft.hkg.web.center.usermanagement.databeans.LoginDataBean;
import com.argusoft.hkg.web.usermanagement.wrapper.UserManagementServiceWrapper;
import com.argusoft.hkg.web.util.SelectItem;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 *
 * @author anita
 */
@Service
public class LocationTransformerBean {

    @Autowired
    HkLocationService locationService;

    @Autowired
    LoginDataBean loginDataBean;
    @Autowired
    UserManagementServiceWrapper userManagementServiceWrapper;

    private List<LocationDataBean> sortedHkLocationDataBean = new ArrayList<>();
    private Map<Long, LocationDataBean> idTolocationDatabeanMap = new HashMap<>();

    public Map retieveLocation(String active) {
        List<HkLocationEntity> locationMasters = null;
        if (active.equals("false")) {

            locationMasters = locationService.retrieveAllLocations(Boolean.TRUE);

        } else {

            locationMasters = locationService.retrieveAllLocations(null);

        }

        List<SelectItem> locationDataBeanList = new ArrayList<SelectItem>();
        Map<Long, List<SelectItem>> locationDataBeanMap = new HashMap<Long, List<SelectItem>>();
        for (HkLocationEntity hkLocationEntity : locationMasters) {
            if (hkLocationEntity.getParent() != null) {
                List<SelectItem> list = locationDataBeanMap.get(hkLocationEntity.getParent().getId());
                if (list == null) {
                    list = new ArrayList<>();
                }
                SelectItem selectItem = new SelectItem(hkLocationEntity.getId(), hkLocationEntity.getLocationName(), hkLocationEntity.getLocationType(), hkLocationEntity.getIsActive());
                list.add(selectItem);
                locationDataBeanMap.put(hkLocationEntity.getParent().getId(), list);
            } else {
                SelectItem selectItem = new SelectItem(hkLocationEntity.getId(), hkLocationEntity.getLocationName(), hkLocationEntity.getLocationType(), hkLocationEntity.getIsActive());
                locationDataBeanList.add(selectItem);

            }

        }
        Map locationMap = new HashMap();
        locationMap.put("locationMap", locationDataBeanMap);
        locationMap.put("countryList", locationDataBeanList);

        return locationMap;
    }

    public List<LocationDataBean> retrieveLocationsTree(String active) {
        List<HkLocationEntity> locationEntities = null;
        if (active.equals("false")) {
            locationEntities = locationService.retrieveAllLocations(Boolean.TRUE);
        } else {
            locationEntities = locationService.retrieveAllLocations(null);
        }

        List<LocationDataBean> locationDataBeans = new ArrayList<>();
        List<LocationDataBean> locationFinalList = null;
        if (!CollectionUtils.isEmpty(locationEntities)) {
            for (HkLocationEntity locationEntity : locationEntities) {
                locationDataBeans.add(this.convertLocationModelToLocationDataBean(locationEntity));
            }
        }
        if (!CollectionUtils.isEmpty(locationDataBeans)) {
            sortedHkLocationDataBean = new ArrayList<>();
            idTolocationDatabeanMap = new HashMap<>();
            locationFinalList = new ArrayList<>();

            // convert all to DTO and create a map of id to dto
            for (LocationDataBean locationDataBean : locationDataBeans) {
                idTolocationDatabeanMap.put(locationDataBean.getId(), locationDataBean);
            }

            // sort accordingly
            // see if parent is added in list, then add its children
            for (LocationDataBean locationDataBean : locationDataBeans) {
                addLocationDatabeanToSortedList(locationDataBean);
            }

            for (LocationDataBean systemFeatureDatabean : sortedHkLocationDataBean) {
                Long parentId = systemFeatureDatabean.getParentId();
                if (parentId != null && parentId != 0) {
                    LocationDataBean parentDto = idTolocationDatabeanMap.get(parentId);
                    List<LocationDataBean> childrenOfparent = parentDto.getChildren();

                    if (childrenOfparent == null) {
                        childrenOfparent = new ArrayList<>();
                        parentDto.setChildren(childrenOfparent);
                        childrenOfparent.add(systemFeatureDatabean);
                    } else {
                        childrenOfparent.add(systemFeatureDatabean);
                        Collections.sort(childrenOfparent, new Comparator<LocationDataBean>() {
                            @Override
                            public int compare(final LocationDataBean object1, final LocationDataBean object2) {
                                return object1.getLocationName().compareTo(object2.getLocationName());
                            }
                        });
                    }
                } else {
                    locationFinalList.add(systemFeatureDatabean);
                }
            }
        }
        return locationFinalList;
    }

    public LocationDataBean convertLocationModelToLocationDataBean(HkLocationEntity hkLocationEntity) {
        LocationDataBean dataBean = new LocationDataBean();
        if (hkLocationEntity != null) {
            dataBean.setId(hkLocationEntity.getId());
            dataBean.setLocationName(hkLocationEntity.getLocationName());
            dataBean.setDisplayName(hkLocationEntity.getLocationName());
            String type = hkLocationEntity.getLocationType();
            switch (type) {
                case "C":
                    dataBean.setLocationType("country");
                    break;
                case "S":
                    dataBean.setLocationType("states");
                    break;
                case "D":
                    dataBean.setLocationType("district");
                    break;
                case "T":
                    dataBean.setLocationType("city");
                    break;
            }
            if (hkLocationEntity.getParent() != null) {
                dataBean.setParentId(hkLocationEntity.getParent().getId());
            }
            dataBean.setIsActive(hkLocationEntity.getIsActive());
        }
        return dataBean;
    }

    private void addLocationDatabeanToSortedList(LocationDataBean locationToAddIntosortedOeLocationDataBean) {

        if (!sortedHkLocationDataBean.contains(locationToAddIntosortedOeLocationDataBean)) {
            Long parentId = null;
            if (locationToAddIntosortedOeLocationDataBean.getParentId() != null) {

                parentId = locationToAddIntosortedOeLocationDataBean.getParentId();
            }
            if (parentId != null && parentId != 0) {
                LocationDataBean parentDatabean = idTolocationDatabeanMap.get(parentId);
                if (sortedHkLocationDataBean.contains(parentDatabean)) {
                    sortedHkLocationDataBean.add(locationToAddIntosortedOeLocationDataBean);
                } else {
                    addLocationDatabeanToSortedList(parentDatabean);
                    sortedHkLocationDataBean.add(locationToAddIntosortedOeLocationDataBean);
                }
            } else {
                sortedHkLocationDataBean.add(locationToAddIntosortedOeLocationDataBean);

            }
        }
    }

    public Map saveLocation(LocationDataBean location) {

        Boolean isExist = Boolean.FALSE;
        Map map = new HashMap();
        String lname = location.getLocationName();
        String type = location.getLocationType();
        HkLocationEntity hkLocationMaster = new HkLocationEntity();
        hkLocationMaster.setLocationName(lname);
        hkLocationMaster.setFranchise(loginDataBean.getCompanyId());
        hkLocationMaster.setCreatedBy(loginDataBean.getId());
        hkLocationMaster.setCreatedOn(new Date());
        hkLocationMaster.setIsActive(true);
        hkLocationMaster.setIsArchive(false);
        hkLocationMaster.setLastModifiedOn(new Date());
        hkLocationMaster.setLastModifiedBy(loginDataBean.getId());

        switch (type) {
            case "Country":
                isExist = locationService.isLocationExist(location.getLocationName(), null, null, null);
                hkLocationMaster.setLocationType(HkSystemConstantUtil.LOCATION_TYPE.COUNTRY);
                hkLocationMaster.setParent(null);
                break;
            case "State":
                isExist = locationService.isLocationExist(location.getLocationName(), location.getId(), null, null);
                Long parentCountry = location.getId();
                hkLocationMaster.setLocationType(HkSystemConstantUtil.LOCATION_TYPE.STATE);
                hkLocationMaster.setParent(new HkLocationEntity(parentCountry));
                break;
            case "District":
                isExist = locationService.isLocationExist(location.getLocationName(), location.getId(), null, null);
                Long parentState = location.getId();
                hkLocationMaster.setLocationType(HkSystemConstantUtil.LOCATION_TYPE.DISTRICT);
                hkLocationMaster.setParent(new HkLocationEntity(parentState));
                break;
            case "City":
                isExist = locationService.isLocationExist(location.getLocationName(), location.getId(), null, null);
                Long parentDistrict = location.getId();
                hkLocationMaster.setLocationType(HkSystemConstantUtil.LOCATION_TYPE.CITY);
                hkLocationMaster.setParent(new HkLocationEntity(parentDistrict));
                break;
        }
        if (isExist) {
            map.put("result", "00");
        } else {
            Long id = locationService.createLocation(hkLocationMaster);
            userManagementServiceWrapper.createLocaleForEntity(hkLocationMaster.getLocationName(), "Location", loginDataBean.getId(), loginDataBean.getCompanyId());
            if (id == null) {
                map.put("result", "0");
            } else {
                map.put("result", "1");
            }
        }

        return map;
    }

    public Map updateLocation(LocationDataBean location) {

        Map map = new HashMap();
        String lname = location.getLocationName();
        Boolean isExist = Boolean.FALSE;
        Boolean status = location.getIsActive();
        Long id = location.getId();
        HkLocationEntity hkLocationMaster = locationService.retrieveOeLocationById(id);
        String type = hkLocationMaster.getLocationType();
        hkLocationMaster.setLocationName(lname);
        hkLocationMaster.setIsActive(status);
        hkLocationMaster.setLastModifiedOn(new Date());
        switch (type) {
            case "C":
                isExist = locationService.isLocationExist(location.getLocationName(), null, hkLocationMaster.getId(), "Update");
                break;
            case "S":
                isExist = locationService.isLocationExist(location.getLocationName(), location.getParentId(), hkLocationMaster.getId(), "Update");
                break;
            case "D":
                isExist = locationService.isLocationExist(location.getLocationName(), location.getParentId(), hkLocationMaster.getId(), "Update");
                break;
            case "T":
                isExist = locationService.isLocationExist(location.getLocationName(), location.getParentId(), hkLocationMaster.getId(), "Update");
                break;
        }
        if (isExist) {
            map.put("result", "00");
        } else {
            locationService.updateLocation(hkLocationMaster);
            userManagementServiceWrapper.createLocaleForEntity(hkLocationMaster.getLocationName(), "Location", loginDataBean.getId(), loginDataBean.getCompanyId());
            map.put("result", "1");
        }
        return map;
    }

    public List<SelectItem> retrieveLocationHeirarchyByType(String locationType, Boolean status) {
        List<HkLocationEntity> locationEntities;
        if (status) {
            locationEntities = locationService.retrieveLocationsByCriteria(locationType, null, status);
        } else {
            locationEntities = locationService.retrieveLocationsByCriteria(locationType, null, null);
        }
        List<SelectItem> selectItems = new ArrayList<>();
        if (!CollectionUtils.isEmpty(locationEntities)) {
            for (HkLocationEntity locationEntity : locationEntities) {
                StringBuilder valueString = new StringBuilder(locationEntity.getLocationName());
                Long id = locationEntity.getId();
                Boolean isActive = locationEntity.getIsActive();
                StringBuilder keyString = new StringBuilder(locationEntity.getId().toString());
                HkLocationEntity parent = locationEntity.getParent();
                while (parent != null) {
                    keyString.append("#");
                    keyString.append(parent.getId().toString());
                    valueString.append(", ");
                    valueString.append(parent.getLocationName());
                    parent = parent.getParent();
                }
                SelectItem selectItem = new SelectItem(keyString.toString(), valueString.toString());
                selectItem.setIsActive(isActive);
                selectItems.add(selectItem);
            }
        }
        return selectItems;
    }

    public List<LocationDataBean> searchLocation(String name) {
        List<HkLocationEntity> searchLocationByName = locationService.searchLocationByName(name, loginDataBean.getCompanyId());
        List<LocationDataBean> locations = new ArrayList<>();
        if (!CollectionUtils.isEmpty(searchLocationByName)) {
            for (HkLocationEntity hkLocationEntity : searchLocationByName) {
                LocationDataBean dataBean = new LocationDataBean();
                dataBean.setId(hkLocationEntity.getId());
                dataBean.setLocationName(hkLocationEntity.getLocationName());
                locations.add(dataBean);
            }
        }
        return locations;
    }

}
