/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.reportbuilder.database.hibernate;

import com.argusoft.generic.database.common.impl.BaseAbstractGenericDao;
import com.argusoft.reportbuilder.database.RbReportFieldDao;
import com.argusoft.reportbuilder.model.RbReportField;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 *
 * @author vipul
 */
@Repository
public class RbReportFieldDaoImpl extends BaseAbstractGenericDao<RbReportField, Long> implements RbReportFieldDao {

    @Override
    public Long createRbReportField(RbReportField rbReportField) {
        return super.create(rbReportField);
    }

    @Override
    public void createAllRbReportField(List<RbReportField> rbReportFieldList) {
        super.createOrUpdateAll(rbReportFieldList);

    }

}
