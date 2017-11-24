/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.core.impl;

import com.argusoft.generic.database.search.SearchFactory;
import com.argusoft.hkg.core.HkLocationService;
import com.argusoft.hkg.core.common.HkCoreService;
import com.argusoft.hkg.database.HkLocationDao;
import com.argusoft.hkg.model.HkAssetEntity;
import com.argusoft.hkg.model.HkLocationEntity;
import com.googlecode.genericdao.search.Search;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

/**
 *
 * @author heena
 */
@Service("hKLocationServiceImpl")
@Transactional
public class HkLocationServiceImpl extends HkCoreService implements HkLocationService {

    @Autowired
    private HkLocationDao hKLocationDao;

    private static class Fields {

        public static final String COMPANY = "company";
        public static final String LOCATION_NAME = "locationName";
    }

    @Override
    public Long createLocation(HkLocationEntity hKLocationEntity) {
        return hKLocationDao.create(hKLocationEntity);
    }

    @Override
    public void updateLocation(HkLocationEntity hKLocationEntity) {
        hKLocationDao.update(hKLocationEntity);
    }

    @Override
    public List<HkLocationEntity> retrieveAllLocations(Boolean isActive) {
        return hKLocationDao.retrieveAllLocations(isActive);
    }

    @Override
    public boolean isLocationExist(String locationName, Long parentId, Long locationId, String operation) {
        boolean codeExist = false;
        if (operation == null) {
            if (parentId != null) {
                HkLocationEntity parentLocation = hKLocationDao.retrieveById(parentId);
                if (parentLocation != null) {
                    List<HkLocationEntity> hkLocationEntitys = hKLocationDao.getAllLocationsByParent(parentId);
                    if (!CollectionUtils.isEmpty(hkLocationEntitys)) {
                        for (HkLocationEntity hkLocationEntity : hkLocationEntitys) {
                            if (hkLocationEntity.getLocationName().equalsIgnoreCase(locationName)) {
                                return true;
                            }
                        }
                    }
                }
            } else {
                List<HkLocationEntity> hkLocationEntitys = hKLocationDao.getLocationsByLocationType("C", true);
                if (!CollectionUtils.isEmpty(hkLocationEntitys)) {
                    for (HkLocationEntity hkLocationEntity : hkLocationEntitys) {
                        if (hkLocationEntity.getLocationName().equalsIgnoreCase(locationName)) {
                            return true;
                        }
                    }
                }
            }
            return codeExist;
        } else if (operation.equalsIgnoreCase("Update")) {
            if (parentId != null) {
                HkLocationEntity parentLocation = hKLocationDao.retrieveById(parentId);
                if (parentLocation != null) {
                    List<HkLocationEntity> hkLocationEntitys = hKLocationDao.getAllLocationsByParent(parentId);
                    if (!CollectionUtils.isEmpty(hkLocationEntitys)) {
                        for (HkLocationEntity hkLocationEntity : hkLocationEntitys) {
                            if (locationId != null && !hkLocationEntity.getId().equals(locationId)) {
                                if (hkLocationEntity.getLocationName().equalsIgnoreCase(locationName)) {
                                    return true;
                                }
                            }
                        }
                    }
                }
            } else {
                List<HkLocationEntity> hkLocationEntitys = hKLocationDao.getLocationsByLocationType("C", true);
                if (!CollectionUtils.isEmpty(hkLocationEntitys)) {
                    for (HkLocationEntity hkLocationEntity : hkLocationEntitys) {
                        if (locationId != null && !hkLocationEntity.getId().equals(locationId)) {
                            if (hkLocationEntity.getLocationName().equalsIgnoreCase(locationName)) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return codeExist;
    }

    @Override
    public HkLocationEntity retrieveOeLocationById(Long id) {
        return hKLocationDao.retrieveById(id);
    }

    @Override
    public List<HkLocationEntity> retrieveLocationsByCriteria(String locationType, Long parentId, Boolean isActive) {
        return hKLocationDao.retrieveLocationsByCriteria(locationType, parentId, isActive);
    }

    @Override
    public List<String> retrieveSimilarNameSuggestion(String locationName, Long companyId) {
        Search search = SearchFactory.getActiveSearch(HkLocationEntity.class);
        search.addFilterEqual(Fields.COMPANY, companyId);
        StringBuilder locationNameBuilder = new StringBuilder("%");
        locationNameBuilder.append(locationName);
        locationNameBuilder.append("%");
        search.addFilterILike(Fields.LOCATION_NAME, locationNameBuilder.toString());

        search.addField(Fields.LOCATION_NAME);

        return commonDao.search(search);
    }

    @Override
    public List<HkLocationEntity> searchLocationByName(String locationName, Long companyId) {
        Search search = SearchFactory.getActiveSearch(HkLocationEntity.class);
        search.addFilterEqual(Fields.COMPANY, companyId);
        if (locationName != null) {
            search.addFilterILike(Fields.LOCATION_NAME, "%" + locationName + "%");
        }
        return commonDao.search(search);
    }

}
