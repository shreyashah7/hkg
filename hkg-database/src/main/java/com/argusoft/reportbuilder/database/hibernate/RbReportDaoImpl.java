/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.reportbuilder.database.hibernate;

import com.argusoft.generic.database.common.impl.BaseAbstractGenericDao;
import com.argusoft.reportbuilder.database.RbReportDao;
import com.argusoft.reportbuilder.model.RbReport;
import java.util.LinkedList;
import java.util.List;
import org.hibernate.Hibernate;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

/**
 *
 * @author vipul
 */
@Repository
public class RbReportDaoImpl extends BaseAbstractGenericDao<RbReport, Long> implements RbReportDao {
    
    private static final String IS_ARCHIVE = "isArchive";
    private static final String COMPANY = "company";

    @Override
    public List<RbReport> retrieveAllReports(Boolean reportFieldSetRequired,Long companyId) {
        List<Criterion> CriterionList = new LinkedList<>();
        CriterionList.add(Restrictions.eq(IS_ARCHIVE, false));
        CriterionList.add(Restrictions.eq(COMPANY, companyId));
        List<RbReport> reportList = super.findByCriteriaList(CriterionList);
        if (reportFieldSetRequired != null) {
            for (RbReport rbReport : reportList) {
                if (reportFieldSetRequired != null && reportFieldSetRequired) {
                    Hibernate.initialize(rbReport.getRbReportFieldSet());
                }
                Hibernate.initialize(rbReport.getRbReportTableDtls());
//                if (reportAccessSetRequired != null && reportAccessSetRequired) {
//                    Hibernate.initialize(rbReport.getRbReportAccessSet());
//                }
            }
        }
        return reportList;
    }

    @Override
    public RbReport retrieveReportById(Long id, Boolean reportFieldSetRequired, Boolean reportAccessSetRequired) {

        RbReport rbReport = super.retrieveById(id);
        if (reportFieldSetRequired != null && reportFieldSetRequired) {
            Hibernate.initialize(rbReport.getRbReportFieldSet());
        }
       Hibernate.initialize(rbReport.getRbReportTableDtls());
//        if (reportAccessSetRequired != null && reportAccessSetRequired) {
//            Hibernate.initialize(rbReport.getRbReportAccessSet());
//        }
        return rbReport;
    }

}
