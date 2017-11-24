/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.reportbuilder.database;

import com.argusoft.generic.database.common.GenericDao;
import com.argusoft.reportbuilder.model.RbReport;
import java.util.List;

/**
 *
 * @author kvithlani
 */
public interface RbReportDao extends GenericDao<RbReport, Long> {

    public List<RbReport> retrieveAllReports(Boolean reportFieldSetRequired,Long companyId);

    public RbReport retrieveReportById(Long id, Boolean reportFieldSetRequired, Boolean reportAccessSetRequired);

}
