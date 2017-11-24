/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.sync.center.core;

import com.argusoft.sync.center.model.HkCaratRangeDocument;
import java.util.List;

/**
 *
 * @author shruti
 */
public interface HkCaratRangeService {

    /**
     * This method is used to retrieve carat range by id
     *
     * @param id
     * @return
     */
    public HkCaratRangeDocument getCaratRangeById(Long id);

    /**
     * This method is used to create/update carat range
     *
     * @author shruti
     * @param document object of HkDepartmentDocument
     * @return id of document
     */
    public Long saveOrUpdateCaratRange(HkCaratRangeDocument document);
    
    public List<HkCaratRangeDocument> retrieveCaratRangeByIds(List<Long> ids);
}
