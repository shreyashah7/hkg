/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.core;

import com.argusoft.hkg.model.HkLocationEntity;
import java.util.List;

/**
 *
 * @author heena
 */
public interface HkLocationService {

    public Long createLocation(HkLocationEntity hKLocationEntity);

    public void updateLocation(HkLocationEntity hKLocationEntity);

    public List<HkLocationEntity> retrieveAllLocations(Boolean isActive);

    public boolean isLocationExist(String locationName, Long parentId, Long locationId, String operation);

    public HkLocationEntity retrieveOeLocationById(Long id);

    public List<HkLocationEntity> retrieveLocationsByCriteria(String locationType, Long parentId, Boolean isActive);

    /**
     * get names of locations same as suggested name
     *
     * @param locationName suggested name for location
     * @param companyId franchise id
     * @return list of similar location names
     */
    public List<String> retrieveSimilarNameSuggestion(String locationName, Long companyId);

    public List<HkLocationEntity> searchLocationByName(String locationName, Long companyId);

}
