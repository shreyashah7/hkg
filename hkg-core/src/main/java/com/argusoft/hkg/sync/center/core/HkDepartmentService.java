/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.sync.center.core;

import com.argusoft.sync.center.model.HkDepartmentDocument;

/**
 *
 * @author shruti
 */
public interface HkDepartmentService {

    /**
     * This method is used to retrieve Department by id
     *
     * @param id
     * @return
     */
    public HkDepartmentDocument getDepartmentById(Long id);

    /**
     * This method is used to create/update Department
     *
     * @author shruti
     * @param document object of HkDepartmentDocument
     * @return id of document
     */
    public Long saveOrUpdateDepartment(HkDepartmentDocument document);
}
