/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.database;

import com.argusoft.generic.database.common.GenericDao;
import com.argusoft.hkg.model.HkLocationEntity;
import java.util.List;

/**
 *
 * @author heena
 */
public interface HkLocationDao extends GenericDao<HkLocationEntity, Long> {

    public List<HkLocationEntity> retrieveAllLocations(Boolean isActive);

    public List<HkLocationEntity> getAllLocationsByParent(Long parentLocation);

    public List<HkLocationEntity> getLocationsByLocationType(String locationType, Boolean isActive);

    public List<HkLocationEntity> retrieveLocationsByCriteria(String locationType, Long parentId, Boolean isActive);
}
