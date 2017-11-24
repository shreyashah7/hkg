/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.bootstrap;

import com.argusoft.hkg.common.constantutil.HkSystemConstantUtil;
import com.argusoft.reportbuilder.common.constantutil.RbReportConstantUtils;
import com.argusoft.reportbuilder.core.RbReportService;
import java.util.HashSet;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 *
 * @author mital
 */
@Service
public class ReportBuilderInitializer {

    @Autowired
    private RbReportService reportService;

    public void createForeignTables() {
        if (!CollectionUtils.isEmpty(HkSystemConstantUtil.featureDocumentMap.values())) {
            Set<String> tableNameSet = new HashSet<>(HkSystemConstantUtil.featureDocumentMap.values());
            reportService.createForeignTables(tableNameSet);
        }
    }

    public void updateTabularRelationshipUsingXls() {
        reportService.updateTabularRelationshipUsingXls();
    }

    public void fillTableSetWithIsArchiveFieldAbsent() {
        RbReportConstantUtils.TABLE_NAMES_WITH_IS_ARCHIVE_ABSENT = reportService.retrieveTablesWithIsArchiveFieldAbsent();
    }
}
