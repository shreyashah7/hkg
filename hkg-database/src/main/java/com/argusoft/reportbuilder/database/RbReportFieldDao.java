/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.reportbuilder.database;

import com.argusoft.reportbuilder.model.RbReportField;
import java.util.List;

/**
 *
 * @author vipul
 */
public interface RbReportFieldDao {

    public Long createRbReportField(RbReportField rbReportField);

    public void createAllRbReportField(List<RbReportField> rbReportFieldList);
}
