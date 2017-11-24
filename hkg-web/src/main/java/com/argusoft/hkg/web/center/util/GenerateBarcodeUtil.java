/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.center.util;

import com.argusoft.hkg.common.functionutil.FolderManagement;
import static com.argusoft.hkg.common.functionutil.FolderManagement.basePath;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import static net.sf.dynamicreports.report.builder.DynamicReports.bcode;
import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.export;
import static net.sf.dynamicreports.report.builder.DynamicReports.report;
import static net.sf.dynamicreports.report.builder.DynamicReports.stl;
import static net.sf.dynamicreports.report.builder.DynamicReports.template;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.component.HorizontalListBuilder;
import net.sf.dynamicreports.report.constant.HorizontalAlignment;
import net.sf.dynamicreports.report.constant.SplitType;
import net.sf.dynamicreports.report.constant.VerticalAlignment;
import net.sf.dynamicreports.report.exception.DRException;
import net.sf.jasperreports.engine.JRException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 *
 * @author shreya
 */
@Service
public class GenerateBarcodeUtil {

    
    public void createReport(String fileName, String extension, Set<String> barcodeValues) throws FileNotFoundException, DRException, IOException, JRException {
        JasperReportBuilder report = report();
        report.setTemplate(template().setBarcodeHeight(50).setBarcodeWidth(50))
                .title(barcode4j(barcodeValues))
                .setNoDataSplitType(SplitType.PREVENT)
                .setSummarySplitType(SplitType.PREVENT)
                .setTitleSplitType(SplitType.IMMEDIATE)
                .setDetailSplitType(SplitType.IMMEDIATE);
        if (extension.equalsIgnoreCase(".pdf")) {
            StringBuilder tempFilePath = new StringBuilder(basePath);
            tempFilePath.append(File.separator).append(FolderManagement.TEMP).append(File.separator).append(fileName + ".pdf");
            File file = new File(tempFilePath.toString());
            file.createNewFile();
            report.toPdf(export.pdfExporter(tempFilePath.toString()));
        }
    }

    private ComponentBuilder<?, ?> barcode4j(Set<String> barcodeValues) {
        HorizontalListBuilder list = cmp.horizontalFlowList();
        list.setGap(10);
        if (!CollectionUtils.isEmpty(barcodeValues)) {
            for (String barcodeValue : barcodeValues) {
                list.add(
                        barcode("", bcode.code128(barcodeValue)));
                list.newRow();
            }
        }
        return list;
    }

    private ComponentBuilder<?, ?> barcode(String label, ComponentBuilder<?, ?> barcode) {
        return cmp.verticalList(cmp.text(label).setStyle(stl.style(stl.style(stl.style(stl.style().setPadding(2)).bold())
                .setAlignment(HorizontalAlignment.CENTER, VerticalAlignment.MIDDLE))
                .setFontSize(18)), barcode);
    }

    public void downloadReport(String fileNameFromReport, String extension, HttpServletRequest request, HttpServletResponse response) {
        File file = null;
        String fileName = fileNameFromReport;
        StringBuilder tempFilePath = new StringBuilder(basePath);
        tempFilePath.append(File.separator).append(FolderManagement.TEMP).append(File.separator).append(fileName);
        if (extension.equals(".pdf")) {
            file = new File(tempFilePath.toString() + ".pdf");
        }
        if (extension.equals(".xls")) {
            file = new File(tempFilePath.toString() + ".xls");
        }
        InputStream is;
        try {
            is = new FileInputStream(file);
            long length = file.length();
            if (length > Integer.MAX_VALUE) {
            }
            byte[] report = new byte[(int) length];
            int offset = 0;
            int numRead = 0;
            while (offset < report.length && (numRead = is.read(report, offset, report.length - offset)) >= 0) {
                offset += numRead;
            }
            if (offset < report.length) {
                throw new IOException("Could not completely read file " + file.getName());
            }
            is.close();
            if (extension.equals(".pdf")) {
                response.setContentType("application/pdf");
            }
            if (extension.equals(".xls")) {
                response.setContentType("application/vnd.ms-excel");
            }
            if (!StringUtils.isEmpty(fileName)) {
                fileName = fileName + "-" + new Date().getTime();
            }
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName + extension);
            response.getOutputStream().write(report);
            response.setContentLength(report.length);
            response.getOutputStream().flush();
            response.getOutputStream().close();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
