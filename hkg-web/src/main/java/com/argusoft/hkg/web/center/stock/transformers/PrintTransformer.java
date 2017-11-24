package com.argusoft.hkg.web.center.stock.transformers;

import com.argusoft.generic.database.exception.GenericDatabaseException;
import com.argusoft.hkg.common.constantutil.HkSystemConstantUtil;
import com.argusoft.hkg.common.functionutil.FolderManagement;
import static com.argusoft.hkg.common.functionutil.FolderManagement.TEMP;
import static com.argusoft.hkg.common.functionutil.FolderManagement.basePath;
import static com.argusoft.hkg.common.functionutil.FolderManagement.checkIsExists;
import com.argusoft.hkg.nosql.core.HkCustomFieldService;
import com.argusoft.hkg.nosql.core.HkFeatureService;
import com.argusoft.hkg.nosql.core.HkStockService;
import com.argusoft.hkg.nosql.model.HkFeatureFieldPermissionDocument;
import com.argusoft.hkg.nosql.model.HkInvoiceDocument;
import com.argusoft.hkg.nosql.model.HkLotDocument;
import com.argusoft.hkg.nosql.model.HkPacketDocument;
import com.argusoft.hkg.nosql.model.HkParcelDocument;
import com.argusoft.hkg.web.center.stock.databeans.PrintDataBean;
import com.argusoft.hkg.web.center.usermanagement.databeans.LoginDataBean;
import com.argusoft.hkg.web.center.usermanagement.databeans.UMFeatureDetailDataBean;
import com.argusoft.hkg.web.center.util.ApplicationUtil;
import com.argusoft.hkg.web.center.util.BarcodeUtil;
import com.argusoft.hkg.web.reportbuilder.transformers.ReportBuilderTemplate;
import com.argusoft.hkg.web.util.SelectItem;
import com.argusoft.sync.center.model.HkFieldDocument;
import com.argusoft.sync.center.model.HkSectionDocument;
import com.argusoft.sync.center.model.SyncCenterFeatureDocument;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.base.expression.AbstractSimpleExpression;
import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.export;
import static net.sf.dynamicreports.report.builder.DynamicReports.report;
import net.sf.dynamicreports.report.builder.component.HorizontalListBuilder;
import net.sf.dynamicreports.report.builder.component.SubreportBuilder;
import net.sf.dynamicreports.report.constant.SplitType;
import net.sf.dynamicreports.report.datasource.DRDataSource;
import net.sf.dynamicreports.report.definition.ReportParameters;
import net.sf.dynamicreports.report.exception.DRException;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import org.bson.BasicBSONObject;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 *
 * @author rajkumar
 */
@Service
public class PrintTransformer {

    private static final Logger LOGGER = LoggerFactory.getLogger(PrintTransformer.class);
    @Autowired
    HkStockService stockService;

    @Autowired
    HkCustomFieldService fieldService;

    @Autowired
    LoginDataBean loginDataBean;

    @Autowired
    BarcodeUtil barcodeUtil;

    @Autowired
    ApplicationUtil applicationUtil;

    @Autowired
    HkFeatureService featureService;

    @Autowired
    StockTransformer stockTransformer;

    public List<SelectItem> searchLot(PrintDataBean printDataBean) {
        List<SelectItem> result = new ArrayList<>();
        Long franchise = loginDataBean.getCompanyId();
        Map<String, Map<String, Object>> featureCustomMapValue = printDataBean.getFeatureCustomMapValue();
        Map<String, Object> invoiceFieldMap = new HashMap<>();
        Map<String, Object> parcelFieldMap = new HashMap<>();
        Map<String, Object> lotFieldMap = new HashMap<>();
        Map<String, Object> packetFieldMap = new HashMap<>();
        Map<String, List<String>> featureDbFieldMap = printDataBean.getFeatureDbFieldMap();
        List<String> invoiceDbField = null;
        List<String> parcelDbField = null;
        List<String> lotDbField = null;
        List<String> packetDbField = null;
        if (!CollectionUtils.isEmpty(featureDbFieldMap)) {
            if (!CollectionUtils.isEmpty(featureDbFieldMap.get("invoice"))) {
                invoiceDbField = featureDbFieldMap.get("invoice");
            }
            if (!CollectionUtils.isEmpty(featureDbFieldMap.get("parcel"))) {
                parcelDbField = featureDbFieldMap.get("parcel");
            }
            if (!CollectionUtils.isEmpty(featureDbFieldMap.get("lot"))) {
                lotDbField = featureDbFieldMap.get("lot");
            }
            if (!CollectionUtils.isEmpty(featureDbFieldMap.get("packet"))) {
                packetDbField = featureDbFieldMap.get("packet");
            }
        }
        if (!CollectionUtils.isEmpty(featureCustomMapValue)) {
            for (Map.Entry<String, Map<String, Object>> entry : featureCustomMapValue.entrySet()) {
                String featureName = entry.getKey();
                Map<String, Object> map = entry.getValue();
                if (!StringUtils.isEmpty(featureName)) {
                    if (featureName.equalsIgnoreCase(HkSystemConstantUtil.Feature.INVOICE)) {

                        if (!CollectionUtils.isEmpty(map)) {
                            for (Map.Entry<String, Object> entry1 : map.entrySet()) {
                                String key = entry1.getKey();
                                Object value = entry1.getValue();
                                if (value != null) {
                                    invoiceFieldMap.put(key, value);
                                }
                            }
                        }
                    } else if (featureName.equalsIgnoreCase(HkSystemConstantUtil.Feature.PARCEL)) {
                        if (!CollectionUtils.isEmpty(map)) {
                            for (Map.Entry<String, Object> entry1 : map.entrySet()) {
                                String key = entry1.getKey();
                                Object value = entry1.getValue();
                                parcelFieldMap.put(key, value);
                            }
                        }
                    } else if (featureName.equalsIgnoreCase(HkSystemConstantUtil.Feature.LOT)) {

                        if (!CollectionUtils.isEmpty(map)) {
                            for (Map.Entry<String, Object> entry1 : map.entrySet()) {
                                String key = entry1.getKey();
                                Object value = entry1.getValue();

                                lotFieldMap.put(key, value);
                            }
                        }
                    } else if (featureName.equalsIgnoreCase(HkSystemConstantUtil.Feature.PACKET)) {
                        for (Map.Entry<String, Object> entry1 : map.entrySet()) {
                            String key = entry1.getKey();
                            Object value = entry1.getValue();

                            packetFieldMap.put(key, value);
                        }
                    }
                }
            }
            Set<HkPacketDocument> packetDocuments = new HashSet<>();
            List<HkInvoiceDocument> invoiceDocuments = null;
            if (!CollectionUtils.isEmpty(invoiceFieldMap)) {
                Iterator<Map.Entry<String, Object>> iter = invoiceFieldMap.entrySet().iterator();
                while (iter.hasNext()) {
                    Map.Entry<String, Object> entry = iter.next();
                    String key = entry.getKey();
                    Object value = entry.getValue();
                    if (key.contains("$MS$") || key.contains("$UMS$") || key.contains("$AG$")) {
                        if (value instanceof String && !StringUtils.isEmpty(value)) {
                            List<String> items = Arrays.asList(value.toString().split(","));
                            invoiceFieldMap.put(key, items);
                        } else if (value instanceof Collection && !CollectionUtils.isEmpty((Collection<?>) value)) {
                            List<String> items = Arrays.asList(value.toString().split(","));
                            invoiceFieldMap.put(key, items);
                        } else {
                            iter.remove();
                        }
                    }
                }
                UMFeatureDetailDataBean feature = applicationUtil.getuMFeatureDetailDataBeanMap().get(HkSystemConstantUtil.Feature.INVOICE);
                if (feature != null && !CollectionUtils.isEmpty(invoiceFieldMap)) {
                    invoiceDocuments = stockService.retrieveInvoices(invoiceFieldMap, franchise, Boolean.FALSE, null, null,null,null);
                }
            }
            List<ObjectId> invoiceIds = null;
            if (!CollectionUtils.isEmpty(invoiceDocuments)) {
                invoiceIds = new ArrayList<>();
                for (HkInvoiceDocument invoiceDocument : invoiceDocuments) {
                    invoiceIds.add(invoiceDocument.getId());
                }
            }
            List<HkParcelDocument> parcelDocuments = null;
            if (!CollectionUtils.isEmpty(parcelFieldMap) || !CollectionUtils.isEmpty(invoiceIds)) {
                Iterator<Map.Entry<String, Object>> iter = parcelFieldMap.entrySet().iterator();
                while (iter.hasNext()) {
                    Map.Entry<String, Object> entry = iter.next();
                    String key = entry.getKey();
                    Object value = entry.getValue();
                    if (key.contains("$MS$") || key.contains("$UMS$") || key.contains("$AG$")) {
                        if (value instanceof String && !StringUtils.isEmpty(value)) {
                            List<String> items = Arrays.asList(value.toString().split(","));
                            parcelFieldMap.put(key, items);
                        } else if (value instanceof Collection && !CollectionUtils.isEmpty((Collection<?>) value)) {
                            List<String> items = Arrays.asList(value.toString().split(","));
                            parcelFieldMap.put(key, items);
                        } else {
                            iter.remove();
                        }
                    }
                }
                UMFeatureDetailDataBean feature = applicationUtil.getuMFeatureDetailDataBeanMap().get(HkSystemConstantUtil.Feature.PARCEL);
                if (feature != null && (!CollectionUtils.isEmpty(parcelFieldMap) || !CollectionUtils.isEmpty(invoiceIds))) {
//                    Map<String, String> uiFieldMap = fieldService.retrieveCustomUIFieldNameWithComponentTypes(feature.getId(), loginDataBean.getCompanyId());
                    parcelDocuments = stockService.retrieveParcels(parcelFieldMap, invoiceIds, null, franchise, Boolean.FALSE, null, HkSystemConstantUtil.StockStatus.NEW_ROUGH,null,null);
                }
            }
            List<ObjectId> parcelIds = null;
            if (!CollectionUtils.isEmpty(parcelDocuments)) {
                parcelIds = new ArrayList<>();
                for (HkParcelDocument parcelDocument : parcelDocuments) {
                    parcelIds.add(parcelDocument.getId());
                }
            }
            Set<HkLotDocument> lotDocuments = new HashSet<>();
            List<ObjectId> lotIds = null;
            if (!CollectionUtils.isEmpty(invoiceIds) || !CollectionUtils.isEmpty(parcelIds) || !CollectionUtils.isEmpty(lotFieldMap)) {
                Set<HkLotDocument> lotDocumentsWithPackets = new HashSet<>();
                Set<HkLotDocument> lotDocumentsWOPackets = new HashSet<>();
                Set<HkLotDocument> lotDocuments1 = new HashSet<>();
                if (!CollectionUtils.isEmpty(lotFieldMap)) {
                    Iterator<Map.Entry<String, Object>> iter = lotFieldMap.entrySet().iterator();
                    while (iter.hasNext()) {
                        Map.Entry<String, Object> entry = iter.next();
                        String key = entry.getKey();
                        Object value = entry.getValue();
                        if (key.contains("$MS$") || key.contains("$UMS$") || key.contains("$AG$")) {
                            if (value instanceof String && !StringUtils.isEmpty(value)) {
                                List<String> items = Arrays.asList(value.toString().split(","));
                                lotFieldMap.put(key, items);
                            } else if (value instanceof Collection && !CollectionUtils.isEmpty((Collection<?>) value)) {
                                List<String> items = Arrays.asList(value.toString().split(","));
                                lotFieldMap.put(key, items);
                            } else {
                                iter.remove();
                            }
                        }
                    }
                    List<HkLotDocument> retrieveLots = stockService.retrieveLots(lotFieldMap, null, null, null, franchise, Boolean.FALSE);
                    if (!CollectionUtils.isEmpty(retrieveLots)) {
                        lotDocuments1.addAll(retrieveLots);
                    }
                }
                if (!CollectionUtils.isEmpty(invoiceIds) || !CollectionUtils.isEmpty(parcelIds)) {
                    List<HkLotDocument> retrieveLots = stockService.retrieveLots(null, invoiceIds, parcelIds, null, franchise, Boolean.FALSE);
                    if (!CollectionUtils.isEmpty(retrieveLots)) {
                        lotDocuments1.addAll(retrieveLots);
                    }
                }
                if (!CollectionUtils.isEmpty(lotDocuments1)) {
                    for (HkLotDocument hkLotDocument : lotDocuments1) {
                        if (!hkLotDocument.isHasPacket()) {
                            lotDocumentsWOPackets.add(hkLotDocument);
                            lotDocuments.add(hkLotDocument);
                        } else {
                            lotDocumentsWithPackets.add(hkLotDocument);
                        }
                    }
                }
                if (!CollectionUtils.isEmpty(lotDocumentsWithPackets)) {
                    lotIds = new ArrayList<>();
                    for (HkLotDocument lotDocument : lotDocumentsWithPackets) {
                        lotIds.add(lotDocument.getId());
                    }
                }
                if (!CollectionUtils.isEmpty(lotDocumentsWithPackets)) {
                    packetDocuments.addAll(stockService.retrievePackets(null, null, null, lotIds, franchise, Boolean.FALSE, null, null,null,null));
                }
            }
            if (!CollectionUtils.isEmpty(packetFieldMap)) {
                for (Map.Entry<String, Object> entrySet : packetFieldMap.entrySet()) {
                    String key = entrySet.getKey();
                    Object value = entrySet.getValue();
                    if (key.contains("$MS$") || key.contains("$UMS$") || key.contains("$AG$")) {
                        if (!StringUtils.isEmpty(value)) {
                            List<String> items = Arrays.asList(value.toString().split(","));
                            packetFieldMap.put(key, items);
                        }
                    }
                }
                packetDocuments.addAll(stockService.retrievePackets(packetFieldMap, null, null, null, franchise, Boolean.FALSE, null, null,null,null));
            }
            List<SelectItem> selectItems = new ArrayList<>();
            if (!CollectionUtils.isEmpty(lotDocuments)) {
                invoiceIds = new ArrayList<>();
                parcelIds = new ArrayList<>();
                for (HkLotDocument lotDocument : lotDocuments) {
                    invoiceIds.add(lotDocument.getInvoice());
                    parcelIds.add(lotDocument.getParcel());
                }
                List<HkInvoiceDocument> hkInvoiceDocuments = stockService.retrieveInvoices(invoiceIds, franchise, Boolean.FALSE);
                List<HkParcelDocument> hkParcelDocuments = stockService.retrieveParcels(parcelIds, franchise, Boolean.FALSE);
                if (!CollectionUtils.isEmpty(hkInvoiceDocuments) && !CollectionUtils.isEmpty(hkParcelDocuments)) {
                    for (HkLotDocument lotDocument : lotDocuments) {
                        Map<String, Object> map = new HashMap<>();
                        SelectItem selectItem = new SelectItem(lotDocument.getId().toString(), lotDocument.getParcel().toString(), lotDocument.getInvoice().toString());
//                        map.putAll(lotDocument.getFieldValue().toMap());
//                        selectItem.setCustom1(lotDocument.getFieldValue().toMap());
                        for (HkInvoiceDocument hkInvoiceDocument : hkInvoiceDocuments) {
                            if (hkInvoiceDocument.getId().toString().equals(lotDocument.getInvoice().toString())) {
                                BasicBSONObject invoiceFieldValue = hkInvoiceDocument.getFieldValue();
                                if (invoiceFieldValue != null && !CollectionUtils.isEmpty(invoiceDbField)) {
                                    for (String dbField : invoiceDbField) {
                                        if (invoiceFieldValue.toMap().containsKey(dbField)) {
                                            map.put(dbField, invoiceFieldValue.toMap().get(dbField));
                                        }
                                    }
                                }
//                                map.putAll(hkInvoiceDocument.getFieldValue().toMap());
//                                selectItem.setCustom3(hkInvoiceDocument.getFieldValue().toMap());
                                break;
                            }
                        }
                        for (HkParcelDocument hkParcelDocument : hkParcelDocuments) {
                            if (hkParcelDocument.getId().toString().equals(lotDocument.getParcel().toString())) {
                                BasicBSONObject parcelFieldValue = hkParcelDocument.getFieldValue();
                                if (parcelFieldValue != null && !CollectionUtils.isEmpty(parcelDbField)) {
                                    for (String dbField : parcelDbField) {
                                        if (parcelFieldValue.toMap().containsKey(dbField)) {
                                            map.put(dbField, parcelFieldValue.toMap().get(dbField));
                                        }
                                    }
                                }
//                                map.putAll(hkParcelDocument.getFieldValue().toMap());
//                                selectItem.setCustom4(hkParcelDocument.getFieldValue().toMap());
                                break;
                            }
                        }
//                        selectItem.setCustom5(lotDocument.getFieldValue().toMap());
                        BasicBSONObject fieldValue = lotDocument.getFieldValue();
                        if (fieldValue != null && !CollectionUtils.isEmpty(lotDbField)) {
                            if (lotDbField.indexOf(HkSystemConstantUtil.AutoNumber.LOT_ID) == -1) {
                                map.put(HkSystemConstantUtil.AutoNumber.LOT_ID, fieldValue.toMap().get(HkSystemConstantUtil.AutoNumber.LOT_ID));
                            }
                            for (String dbField : lotDbField) {
                                if (fieldValue.toMap().containsKey(dbField)) {
                                    map.put(dbField, fieldValue.toMap().get(dbField));
                                }
                            }
                        } else {
                            map.put(HkSystemConstantUtil.AutoNumber.LOT_ID, fieldValue.toMap().get(HkSystemConstantUtil.AutoNumber.LOT_ID));
                        }
                        selectItem.setCategoryCustom(map);
                        selectItems.add(selectItem);
                    }
                    result.addAll(selectItems);
                }

            }
            if (!CollectionUtils.isEmpty(packetDocuments)) {
                selectItems = new ArrayList<>();
                invoiceIds = new ArrayList<>();
                parcelIds = new ArrayList<>();
                lotIds = new ArrayList<>();
                for (HkPacketDocument packetDocument : packetDocuments) {
                    invoiceIds.add(packetDocument.getInvoice());
                    parcelIds.add(packetDocument.getParcel());
                    lotIds.add(packetDocument.getLot());
                }
                List<HkInvoiceDocument> hkInvoiceDocuments = stockService.retrieveInvoices(invoiceIds, franchise, Boolean.FALSE);
                List<HkParcelDocument> hkParcelDocuments = stockService.retrieveParcels(parcelIds, franchise, Boolean.FALSE);
                List<HkLotDocument> hklotDocuments = stockService.retrieveLotsByIds(lotIds, franchise, Boolean.FALSE, null, null, Boolean.TRUE);
                Map<ObjectId, HkLotDocument> mapOfLots = new HashMap();
                if (!CollectionUtils.isEmpty(hklotDocuments)) {
                    for (HkLotDocument hkLotDocument : hklotDocuments) {
                        mapOfLots.put(hkLotDocument.getId(), hkLotDocument);
                    }
                }

                if (!CollectionUtils.isEmpty(hkInvoiceDocuments) && !CollectionUtils.isEmpty(hkParcelDocuments)) {
                    for (HkPacketDocument packetDocument : packetDocuments) {
                        Map<String, Object> map = new HashMap<>();
                        SelectItem selectItem = new SelectItem(packetDocument.getLot().toString(), packetDocument.getParcel().toString(), packetDocument.getInvoice().toString(), packetDocument.getId().toString());
                        BasicBSONObject fieldValue = packetDocument.getFieldValue();
                        if (fieldValue != null && !CollectionUtils.isEmpty(packetDbField)) {
                            if (packetDbField.indexOf(HkSystemConstantUtil.AutoNumber.PACKET_ID) == -1) {
                                map.put(HkSystemConstantUtil.AutoNumber.PACKET_ID, fieldValue.toMap().get(HkSystemConstantUtil.AutoNumber.PACKET_ID));
                            }
                            for (String dbField : packetDbField) {
                                if (fieldValue.toMap().containsKey(dbField)) {
                                    map.put(dbField, fieldValue.toMap().get(dbField));
                                }
                            }
                        } else {
                            map.put(HkSystemConstantUtil.AutoNumber.PACKET_ID, fieldValue.toMap().get(HkSystemConstantUtil.AutoNumber.PACKET_ID));
                        }
                        for (HkInvoiceDocument hkInvoiceDocument : hkInvoiceDocuments) {
                            if (hkInvoiceDocument.getId().toString().equals(packetDocument.getInvoice().toString())) {
                                BasicBSONObject invoiceFieldValue = hkInvoiceDocument.getFieldValue();
                                if (invoiceFieldValue != null && !CollectionUtils.isEmpty(invoiceDbField)) {
                                    for (String dbField : invoiceDbField) {
                                        if (invoiceFieldValue.toMap().containsKey(dbField)) {
                                            map.put(dbField, invoiceFieldValue.toMap().get(dbField));
                                        }
                                    }
                                }
                                break;
                            }
                        }
                        for (HkParcelDocument hkParcelDocument : hkParcelDocuments) {
                            if (hkParcelDocument.getId().toString().equals(packetDocument.getParcel().toString())) {
                                BasicBSONObject parcelFieldValue = hkParcelDocument.getFieldValue();
                                if (parcelFieldValue != null && !CollectionUtils.isEmpty(parcelDbField)) {
                                    for (String dbField : parcelDbField) {
                                        if (parcelFieldValue.toMap().containsKey(dbField)) {
                                            map.put(dbField, parcelFieldValue.toMap().get(dbField));
                                        }
                                    }
                                }
                                break;
                            }
                        }
                        for (HkLotDocument hkLotDocument : hklotDocuments) {
                            if (hkLotDocument.getId().toString().equals(packetDocument.getLot().toString())) {
                                BasicBSONObject lotFieldValue = hkLotDocument.getFieldValue();
                                if (lotFieldValue != null && !CollectionUtils.isEmpty(lotDbField)) {
                                    for (String dbField : lotDbField) {
                                        if (lotFieldValue.toMap().containsKey(dbField)) {
                                            map.put(dbField, lotFieldValue.toMap().get(dbField));
                                        }
                                    }
                                }
                                break;
                            }
                        }
                        selectItem.setCategoryCustom(map);

                        selectItems.add(selectItem);
                    }
                    result.addAll(selectItems);
                }

            }
            return result;
        } else {
            return null;
        }
    }

    public String generatePrintData(Map<String, String> payload, Map<String, String> payload1, Map<String, String> idToPrint, HttpServletResponse response, HttpServletRequest request) throws DRException, IOException {
        JasperReportBuilder genInforeport = report();

        genInforeport.setTemplate(ReportBuilderTemplate.reportTemplate)
                .setDetailSplitType(SplitType.PREVENT);

        JasperReportBuilder genInfoReport = report().setTemplate(ReportBuilderTemplate.reportTemplate).setDetailSplitType(SplitType.PREVENT);
        HorizontalListBuilder genInfo = cmp.horizontalList();
        if (!CollectionUtils.isEmpty(payload)) {

            for (Map.Entry<String, String> entry : payload.entrySet()) {
                genInfo.add(ReportBuilderTemplate.labelWithNoBorder(entry.getKey(), 120))
                        .add(ReportBuilderTemplate.contentWithNoBorder(entry.getValue(), 170)).newRow();;
            }
        }
        if (!CollectionUtils.isEmpty(payload1)) {
            for (Map.Entry<String, String> entry : payload1.entrySet()) {
                genInfo.add(ReportBuilderTemplate.labelWithNoBorder(entry.getKey(), 120))
                        .add(ReportBuilderTemplate.contentWithNoBorder(entry.getValue(), 170)).newRow();;

            }
        }
        genInfoReport.detail(genInfo);
        SubreportBuilder genInfoReportTemp = cmp.subreport(genInfoReport).setDataSource(new HorizontalListDataSourceExpression());

        genInforeport.detail(genInfoReportTemp);

        SubreportBuilder sereniumGenInfoReport = cmp.subreport(genInforeport)
                .setDataSource(new HorizontalListDataSourceExpression());
        JasperReportBuilder mainReport = report();
        StringBuilder tempFilePath1 = new StringBuilder(basePath);
        if (!CollectionUtils.isEmpty(idToPrint)) {
            if (!StringUtils.isEmpty(idToPrint.get("id"))) {
                barcodeUtil.generateBarcode(idToPrint.get("id"));
                tempFilePath1.append(File.separator).append(FolderManagement.TEMP).append(File.separator).append(idToPrint.get("id"));
            }
        }
        mainReport.title(
                ReportBuilderTemplate.createTitleComponent(tempFilePath1.toString(), null, request,"RIGHT",Boolean.FALSE))
                .detail(
                        sereniumGenInfoReport
                )
                .pageFooter(ReportBuilderTemplate.createFooterComponent())
                .setDataSource(new JREmptyDataSource(1));

        String reportName = "printData";
        File tempDir = checkIsExists(FolderManagement.getBasePath(), TEMP, null);
        StringBuilder tempFilePath = new StringBuilder(tempDir.getPath());
        tempFilePath.append(File.separator).append(reportName);
        String path = tempFilePath.toString() + ".pdf";
        File file = new File(path);
        file.createNewFile();
        mainReport.toPdf(export.pdfExporter(path));
        return reportName;
    }

    public PrintDataBean retrieveSearchedLotsAndPacketsNew(Map<String, List<String>> featureDbfieldNameMap) {
        PrintDataBean stockDataBean = new PrintDataBean();
        Long companyId = loginDataBean.getCompanyId();
        List<HkLotDocument> lotDocuments = new ArrayList<>();
        List<HkPacketDocument> packetDocuments = new ArrayList<>();
//        List<ObjectId> workAllotmentIds = new ArrayList<>();
//        lotDocuments = hkWorkAllotmentService.retrieveLotsOrPacketsFromWorkAllocation(null, Arrays.asList(loginDataBean.getId()), null, Arrays.asList(HkSystemConstantUtil.ActivityServiceStatus.IN_PROCESS), Arrays.asList(HkSystemConstantUtil.SERVICE_CODE.PRINT_DYNAMIC), companyId, Boolean.FALSE, Boolean.TRUE);
//        packetDocuments = hkWorkAllotmentService.retrieveLotsOrPacketsFromWorkAllocation(null, Arrays.asList(loginDataBean.getId()), null, Arrays.asList(HkSystemConstantUtil.ActivityServiceStatus.IN_PROCESS), Arrays.asList(HkSystemConstantUtil.SERVICE_CODE.PRINT_DYNAMIC), companyId, Boolean.TRUE, Boolean.TRUE);

        List<SelectItem> lotItems = null;
        List<SelectItem> packetItems = null;
        if ((!CollectionUtils.isEmpty(lotDocuments) || !CollectionUtils.isEmpty(packetDocuments)) && !CollectionUtils.isEmpty(featureDbfieldNameMap)) {
            List<String> invoiceDbFieldNames = null;
            List<String> parcelDbFieldNames = null;
            List<String> lotDbFieldNames = null;
            List<String> packetDbFieldNames = null;
            for (Map.Entry<String, List<String>> entry : featureDbfieldNameMap.entrySet()) {
                String featureName = entry.getKey();
                if (featureName.equalsIgnoreCase(HkSystemConstantUtil.Feature.INVOICE)) {
                    invoiceDbFieldNames = entry.getValue();
                } else if (featureName.equalsIgnoreCase(HkSystemConstantUtil.Feature.PARCEL)) {
                    parcelDbFieldNames = entry.getValue();
                } else if (featureName.equalsIgnoreCase(HkSystemConstantUtil.Feature.LOT)) {
                    lotDbFieldNames = entry.getValue();
                } else if (featureName.equalsIgnoreCase(HkSystemConstantUtil.Feature.PACKET)) {
                    packetDbFieldNames = entry.getValue();
                }
            }
            Map<String, String> uiFieldMapForParcel = new HashMap<>();
            Map<String, String> uiFieldMapForInvoice = new HashMap<>();
            SyncCenterFeatureDocument invoiceFeature = null;
            SyncCenterFeatureDocument parcelFeature = null;
            SyncCenterFeatureDocument lotFeature = null;
            if (!CollectionUtils.isEmpty(lotDocuments)) {
                List<ObjectId> invoiceIds = new ArrayList<>();
                List<ObjectId> parcelIds = new ArrayList<>();
                for (HkLotDocument lotDocument : lotDocuments) {
                    invoiceIds.add(lotDocument.getInvoice());
                    parcelIds.add(lotDocument.getParcel());
//                    workAllotmentIds.add(lotDocument.getWorkAllotmentId());
                }
                List<HkInvoiceDocument> invoiceDocuments = null;
                invoiceFeature = featureService.retireveFeatureByName(HkSystemConstantUtil.Feature.INVOICE);
                if (invoiceFeature != null) {
//                    uiFieldMapForInvoice = customFieldService.retrieveCustomUIFieldNameWithComponentTypes(invoiceFeature.getId(), companyId);
                    invoiceDocuments = stockService.retrieveInvoices(null, companyId, Boolean.FALSE, invoiceIds, null,null,null);
                }
                List<HkParcelDocument> parcelDocuments = null;
                parcelFeature = featureService.retireveFeatureByName(HkSystemConstantUtil.Feature.PARCEL);
                if (parcelFeature != null) {
//                    uiFieldMapForParcel = customFieldService.retrieveCustomUIFieldNameWithComponentTypes(parcelFeature.getId(), companyId);
                    parcelDocuments = stockService.retrieveParcels(null, null, parcelIds, companyId, Boolean.FALSE, null, HkSystemConstantUtil.StockStatus.NEW_ROUGH,null,null);
                }
                lotItems = new ArrayList<>();
                for (HkLotDocument lotDocument : lotDocuments) {
                    Map<String, Object> mapToSentOnUI = new HashMap<>();
                    SelectItem selectItem = new SelectItem(null, lotDocument.getParcel().toString(), lotDocument.getInvoice().toString(), lotDocument.getId().toString());

                    BasicBSONObject fieldValue = lotDocument.getFieldValue();
                    if (fieldValue != null) {
                        Map fieldValueMapFromDb = fieldValue.toMap();
                        if (CollectionUtils.isEmpty(lotDbFieldNames) || lotDbFieldNames.indexOf(HkSystemConstantUtil.AutoNumber.LOT_ID) == -1) {
                            mapToSentOnUI.put(HkSystemConstantUtil.AutoNumber.LOT_ID, fieldValue.toMap().get(HkSystemConstantUtil.AutoNumber.LOT_ID));
                        }
                        if (!CollectionUtils.isEmpty(fieldValueMapFromDb) && !CollectionUtils.isEmpty(lotDbFieldNames)) {
                            for (String lotField : lotDbFieldNames) {
                                mapToSentOnUI.put(lotField, fieldValueMapFromDb.get(lotField));
                            }
                        }
                    }

                    if (!CollectionUtils.isEmpty(invoiceDocuments)) {
                        for (HkInvoiceDocument invoiceDocument : invoiceDocuments) {
                            if (invoiceDocument.getId().toString().equals(lotDocument.getInvoice().toString())) {
                                BasicBSONObject fieldValue2 = invoiceDocument.getFieldValue();
                                if (fieldValue2 != null) {
                                    Map fieldValueMapFromDb = fieldValue2.toMap();
                                    if (!CollectionUtils.isEmpty(fieldValueMapFromDb) && !CollectionUtils.isEmpty(invoiceDbFieldNames)) {
                                        for (String invoiceField : invoiceDbFieldNames) {
                                            mapToSentOnUI.put(invoiceField, fieldValueMapFromDb.get(invoiceField));
                                        }
                                    }
                                    break;
                                }
                            }
                        }
                    }
                    if (!CollectionUtils.isEmpty(parcelDocuments)) {
                        for (HkParcelDocument parcelDocument : parcelDocuments) {
                            if (parcelDocument.getId().toString().equals(lotDocument.getParcel().toString())) {
                                BasicBSONObject fieldValue3 = parcelDocument.getFieldValue();
                                if (fieldValue3 != null) {
                                    Map fieldValueMapFromDb = fieldValue3.toMap();
                                    if (!CollectionUtils.isEmpty(fieldValueMapFromDb) && !CollectionUtils.isEmpty(parcelDbFieldNames)) {
                                        for (String parcelField : parcelDbFieldNames) {
                                            mapToSentOnUI.put(parcelField, fieldValueMapFromDb.get(parcelField));
                                        }
                                    }
                                }
                                selectItem.setParcelId(parcelDocument.getId().toString());
                                break;
                            }
                        }
                    }
                    Map<Object, Object> newMap = new HashMap<>();
                    newMap.put("haveValue", lotDocument.getHaveValue());
                    selectItem.setCustom1(newMap);
                    selectItem.setCategoryCustom(mapToSentOnUI);
                    lotItems.add(selectItem);
                }
            }
//            retrieve packet info
            if (!CollectionUtils.isEmpty(packetDocuments)) {
                List<ObjectId> invoiceIds = new ArrayList<>();
                List<ObjectId> parcelIds = new ArrayList<>();
                List<ObjectId> lotIds = new ArrayList<>();
                for (HkPacketDocument packetDocument : packetDocuments) {
                    invoiceIds.add(packetDocument.getInvoice());
                    parcelIds.add(packetDocument.getParcel());
                    lotIds.add(packetDocument.getLot());
//                    workAllotmentIds.add(packetDocument.getWorkAllotmentId());
                }
                List<HkInvoiceDocument> invoiceDocuments = null;
                if (!CollectionUtils.isEmpty(uiFieldMapForInvoice)) {
                    invoiceDocuments = stockService.retrieveInvoices(null, companyId, Boolean.FALSE, invoiceIds, null,null,null);
                } else {
                    invoiceFeature = featureService.retireveFeatureByName(HkSystemConstantUtil.Feature.INVOICE);
                    if (invoiceFeature != null) {
                        invoiceDocuments = stockService.retrieveInvoices(null, companyId, Boolean.FALSE, invoiceIds, null,null,null);
                    }
                }
                List<HkParcelDocument> parcelDocuments = null;
                if (!CollectionUtils.isEmpty(uiFieldMapForParcel)) {
                    parcelDocuments = stockService.retrieveParcels(null, null, parcelIds, companyId, Boolean.FALSE, null, HkSystemConstantUtil.StockStatus.NEW_ROUGH,null,null);
                } else {
                    parcelFeature = featureService.retireveFeatureByName(HkSystemConstantUtil.Feature.PARCEL);
                    if (parcelFeature != null) {
                        parcelDocuments = stockService.retrieveParcels(null, null, parcelIds, companyId, Boolean.FALSE, null, HkSystemConstantUtil.StockStatus.NEW_ROUGH,null,null);
                    }
                }
                lotFeature = featureService.retireveFeatureByName(HkSystemConstantUtil.Feature.LOT);
                if (lotFeature != null) {
                    lotDocuments = stockService.retrieveLots(null, null, null, lotIds, companyId, Boolean.FALSE);
                }
                packetItems = new ArrayList<>();
                for (HkPacketDocument packetDocument : packetDocuments) {
                    Map<String, Object> mapToSentOnUI = new HashMap<>();
                    SelectItem selectItem = new SelectItem(null, packetDocument.getParcel().toString(), packetDocument.getInvoice().toString(), packetDocument.getLot().toString());

                    BasicBSONObject fieldValue = packetDocument.getFieldValue();
                    if (fieldValue != null) {
                        Map fieldValueMapFromDb = fieldValue.toMap();
                        if (CollectionUtils.isEmpty(packetDbFieldNames) || packetDbFieldNames.indexOf(HkSystemConstantUtil.AutoNumber.PACKET_ID) == -1) {
                            mapToSentOnUI.put(HkSystemConstantUtil.AutoNumber.PACKET_ID, fieldValue.toMap().get(HkSystemConstantUtil.AutoNumber.PACKET_ID));
                        }
                        if (!CollectionUtils.isEmpty(fieldValueMapFromDb) && !CollectionUtils.isEmpty(packetDbFieldNames)) {
                            for (String packetField : packetDbFieldNames) {
                                mapToSentOnUI.put(packetField, fieldValueMapFromDb.get(packetField));
                            }
                        }
                    }

                    for (HkInvoiceDocument invoiceDocument : invoiceDocuments) {
                        if (invoiceDocument.getId().toString().equals(packetDocument.getInvoice().toString())) {
                            BasicBSONObject fieldValue1 = invoiceDocument.getFieldValue();
                            if (fieldValue1 != null) {
                                Map fieldValueMapFromDb = fieldValue1.toMap();
                                if (!CollectionUtils.isEmpty(fieldValueMapFromDb) && !CollectionUtils.isEmpty(invoiceDbFieldNames)) {
                                    for (String invoiceField : invoiceDbFieldNames) {
                                        mapToSentOnUI.put(invoiceField, fieldValueMapFromDb.get(invoiceField));
                                    }
                                }
                                break;
                            }
                        }
                    }
                    for (HkParcelDocument parcelDocument : parcelDocuments) {
                        if (parcelDocument.getId().toString().equals(packetDocument.getParcel().toString())) {
                            BasicBSONObject fieldValue4 = parcelDocument.getFieldValue();
                            if (fieldValue4 != null) {
                                Map fieldValueMapFromDb = fieldValue4.toMap();
                                if (!CollectionUtils.isEmpty(fieldValueMapFromDb) && !CollectionUtils.isEmpty(parcelDbFieldNames)) {
                                    for (String parcelField : parcelDbFieldNames) {
                                        mapToSentOnUI.put(parcelField, fieldValueMapFromDb.get(parcelField));
                                    }
                                }
                            }
                            break;
                        }
                    }

                    for (HkLotDocument lotDocument : lotDocuments) {
                        if (lotDocument.getId().toString().equals(packetDocument.getLot().toString())) {
                            BasicBSONObject fieldValue5 = lotDocument.getFieldValue();
                            if (fieldValue5 != null) {
                                Map fieldValueMapFromDb = fieldValue5.toMap();
                                if (!CollectionUtils.isEmpty(fieldValueMapFromDb) && !CollectionUtils.isEmpty(lotDbFieldNames)) {
                                    for (String lotField : lotDbFieldNames) {
                                        mapToSentOnUI.put(lotField, fieldValueMapFromDb.get(lotField));
                                    }
                                }
                                selectItem.setLotId(lotDocument.getFieldValue().toMap().get(HkSystemConstantUtil.AutoNumber.LOT_ID).toString());
                                break;
                            }
                        }
                    }
                    Map<Object, Object> newMap = new HashMap<>();
                    newMap.put("haveValue", packetDocument.getHaveValue());
                    selectItem.setCustom1(newMap);
                    //If packet than store packet ID in status field
                    selectItem.setStatus(packetDocument.getId().toString());
                    selectItem.setCategoryCustom(mapToSentOnUI);
                    packetItems.add(selectItem);
                }
            }
            if (!CollectionUtils.isEmpty(lotItems)) {
                if (!CollectionUtils.isEmpty(packetItems)) {
                    lotItems.addAll(packetItems);
                }
                stockDataBean.setPrintList(lotItems);
//                if (!CollectionUtils.isEmpty(workAllotmentIds)) {
//                    DynamicServiceInitDataBean nodeInfo = stockTransformer.retrieveNodeInfo(workAllotmentIds, companyId, HkSystemConstantUtil.SERVICE_CODE.PRINT_DYNAMIC);
//                    stockDataBean.setDynamicServiceInitBean(nodeInfo);
//                }
                return stockDataBean;
            }
        }
//        if (!CollectionUtils.isEmpty(workAllotmentIds)) {
//            DynamicServiceInitDataBean nodeInfo = stockTransformer.retrieveNodeInfo(workAllotmentIds, companyId, HkSystemConstantUtil.SERVICE_CODE.PRINT_DYNAMIC);
//            stockDataBean.setDynamicServiceInitBean(nodeInfo);
//        }
        stockDataBean.setPrintList(packetItems);

        return stockDataBean;
    }

    public SelectItem retrievePacket(Map<String, List<String>> featureDbfieldNameMap) {
        SelectItem selectItem = null;
        if (!CollectionUtils.isEmpty(featureDbfieldNameMap)) {
            List<String> ids = featureDbfieldNameMap.get("packetId");
            Boolean invoiceFieldExist = false;
            Boolean parcelFieldExist = false;
            Boolean lotFieldExist = false;
            Boolean packetFieldExist = false;
            List<String> invoiceDbFieldNames = null;
            List<String> parcelDbFieldNames = null;
            List<String> lotDbFieldNames = null;
            List<String> packetDbFieldNames = null;
            for (Map.Entry<String, List<String>> entry : featureDbfieldNameMap.entrySet()) {
                String featureName = entry.getKey();
                if (featureName.equalsIgnoreCase("invoiceDbFieldName")) {
                    invoiceFieldExist = true;
                    invoiceDbFieldNames = featureDbfieldNameMap.get("invoiceDbFieldName");
                } else if (featureName.equalsIgnoreCase("parcelDbFieldName")) {
                    parcelFieldExist = true;
                    parcelDbFieldNames = featureDbfieldNameMap.get("parcelDbFieldName");
                } else if (featureName.equalsIgnoreCase("lotDbFieldName")) {
                    lotFieldExist = true;
                    lotDbFieldNames = featureDbfieldNameMap.get("lotDbFieldName");
                } else if (featureName.equalsIgnoreCase("packetDbFieldName")) {
                    packetFieldExist = true;
                    packetDbFieldNames = featureDbfieldNameMap.get("packetDbFieldName");
                }
            }

            if (!CollectionUtils.isEmpty(ids)) {
                HkPacketDocument packetDocument = stockService.retrievePacketById(new ObjectId(ids.get(0)),true);
                String pointerComponentType = HkSystemConstantUtil.CustomField.ComponentType.POINTER + "@*" + HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN;
                Map<String, String> uiFieldMap = null;
                Map<String, Object> customMap = packetDocument.getFieldValue().toMap();
                uiFieldMap = stockService.createFieldNameWithComponentType(customMap);
                if (!CollectionUtils.isEmpty(uiFieldMap)) {
                    for (Map.Entry<String, String> field : uiFieldMap.entrySet()) {
                        if (field.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN) || field.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.USER_MULTISELECT) || field.getValue().equals(pointerComponentType)) {
                            if (customMap != null && !customMap.isEmpty() && customMap.containsKey(field.getKey())) {
                                String value = customMap.get(field.getKey()).toString().replaceAll("\\[", "").replaceAll("\\]", "");
                                customMap.put(field.getKey(), value);
                            }
                        }
                    }
                    packetDocument.getFieldValue().putAll(customMap);
                }
                if (packetDocument != null && !CollectionUtils.isEmpty(featureDbfieldNameMap)) {
                    ObjectId packetId = packetDocument.getId();
                    ObjectId invoiceId = packetDocument.getInvoice();
                    ObjectId parcelId = packetDocument.getParcel();
                    ObjectId lotId = packetDocument.getLot();
                    selectItem = new SelectItem(packetId.toString(), lotId.toString(), parcelId.toString(), invoiceId.toString());
                    if (invoiceFieldExist) {
                        HkInvoiceDocument invoiceDocument = stockService.retrieveInvoiceById(invoiceId);
                        Map<String, Object> invoiceFieldValueMap = invoiceDocument.getFieldValue().toMap();
                        if (!CollectionUtils.isEmpty(invoiceDbFieldNames)) {
                            Map<Object, Object> invoiceMap = new HashMap<>();
                            for (String invoiceField : invoiceDbFieldNames) {
                                Object value = invoiceFieldValueMap.get(invoiceField);
                                invoiceMap.put(invoiceField, value);
                            }
                            selectItem.setCustom1(invoiceMap);
                        }
                    }
                    if (parcelFieldExist) {
                        HkParcelDocument parcelDocument = stockService.retrieveParcelById(parcelId);
                        String pointerComponentType1 = HkSystemConstantUtil.CustomField.ComponentType.POINTER + "@*" + HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN;
                        Map<String, String> uiFieldMap1 = null;
                        Map<String, Object> customMap1 = parcelDocument.getFieldValue().toMap();
                        uiFieldMap1 = stockService.createFieldNameWithComponentType(customMap1);
                        if (!CollectionUtils.isEmpty(uiFieldMap1)) {
                            for (Map.Entry<String, String> field : uiFieldMap1.entrySet()) {
                                if (field.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN) || field.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.USER_MULTISELECT) || field.getValue().equals(pointerComponentType1)) {
                                    if (customMap1 != null && !customMap1.isEmpty() && customMap1.containsKey(field.getKey())) {
                                        String value = customMap1.get(field.getKey()).toString().replaceAll("\\[", "").replaceAll("\\]", "");
                                        customMap1.put(field.getKey(), value);
                                    }
                                }
                            }
                            parcelDocument.getFieldValue().putAll(customMap1);
                        }
                        Map<String, Object> parcelFieldValueMap = new HashMap();
                        if (parcelDocument != null) {
                            parcelFieldValueMap = parcelDocument.getFieldValue().toMap();
                        }
                        if (!CollectionUtils.isEmpty(parcelDbFieldNames) && parcelDocument != null) {
                            Map<Object, Object> parcelMap = new HashMap<>();
                            for (String parcelField : parcelDbFieldNames) {
                                Object value = parcelFieldValueMap.get(parcelField);
                                parcelMap.put(parcelField, value);
                            }

                            selectItem.setCustom3(parcelMap);
                        }
                    }
                    if (lotFieldExist) {
                        HkLotDocument lotDocument = stockService.retrieveLotById(lotId);
                        String pointerComponentType2 = HkSystemConstantUtil.CustomField.ComponentType.POINTER + "@*" + HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN;
                        Map<String, String> uiFieldMap2 = null;
                        Map<String, Object> customMap2 = lotDocument.getFieldValue().toMap();
                        uiFieldMap2 = stockService.createFieldNameWithComponentType(customMap2);
                        if (!CollectionUtils.isEmpty(uiFieldMap2)) {
                            for (Map.Entry<String, String> field : uiFieldMap2.entrySet()) {
                                if (field.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN) || field.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.USER_MULTISELECT) || field.getValue().equals(pointerComponentType2)) {
                                    if (customMap2 != null && !customMap2.isEmpty() && customMap2.containsKey(field.getKey())) {
                                        String value = customMap2.get(field.getKey()).toString().replaceAll("\\[", "").replaceAll("\\]", "");
                                        customMap2.put(field.getKey(), value);
                                    }
                                }
                            }
                            lotDocument.getFieldValue().putAll(customMap2);
                        }
                        Map<String, Object> lotFieldValueMap = new HashMap();
                        if (lotDocument != null) {
                            if (lotDocument.getFieldValue() != null) {
                                lotFieldValueMap = lotDocument.getFieldValue().toMap();
                            }
                        }
                        if (!CollectionUtils.isEmpty(lotDbFieldNames) && !CollectionUtils.isEmpty(lotFieldValueMap)) {
                            Map<Object, Object> lotMap = new HashMap<>();
                            for (String lotField : lotDbFieldNames) {
                                Object value = lotFieldValueMap.get(lotField);
                                lotMap.put(lotField, value);
                            }

                            selectItem.setCustom4(lotMap);
                        }
                    }
                    if (packetFieldExist) {
                        Map<String, Object> packetFieldValueMap = packetDocument.getFieldValue().toMap();
                        if (!CollectionUtils.isEmpty(packetDbFieldNames)) {
                            Map<Object, Object> packetMap = new HashMap<>();
                            for (String packetField : packetDbFieldNames) {
                                Object value = packetFieldValueMap.get(packetField);
                                packetMap.put(packetField, value);
                            }
                            selectItem.setCustom5(packetMap);
                        }
                    }

                } else {
                    //System.out.println("no map value");
                }
            }

        }
        return selectItem;
    }

    private class HorizontalListDataSourceExpression extends AbstractSimpleExpression<JRDataSource> {

        private static final long serialVersionUID = 1L;

        @Override
        public JRDataSource evaluate(ReportParameters reportParameters) {
            DRDataSource dataSource = new DRDataSource("");
            dataSource.add("");
            return dataSource;
        }
    }

    public PrintDataBean retrieveSearchedLotsAndPackets() throws GenericDatabaseException {

        List<String> listOfModel = new ArrayList<>();
        List<Map<Object, Object>> listOfTableHeader = new ArrayList<>();
        List<ObjectId> invoiceIds = new ArrayList<>();
        List<HkLotDocument> lotDocuments = new ArrayList<>();
        List<HkPacketDocument> packetDocuments = new ArrayList<>();
        List<ObjectId> parcelIds = new ArrayList<>();
        List<ObjectId> lotIds = new ArrayList<>();
        List<Long> forUserIds = new ArrayList<>();
        List<String> statusList = new ArrayList<>();
        List<String> serviceCodeList = new ArrayList<>();
        List<SelectItem> selectItems = new ArrayList<>();
        List<Map<Object, Object>> featureMapGeneralSection = new ArrayList<>();
        featureMapGeneralSection = retrieveFieldsConfigureForSearch();
        for (Map<Object, Object> feature : featureMapGeneralSection) {
            Map<Object, Object> tableHeder = new HashMap<>();
            listOfModel.add((String) feature.get("model"));
            tableHeder.put("name", feature.get("model"));
            tableHeder.put("displayName", feature.get("label"));
            listOfTableHeader.add(tableHeder);
        }

        Long franchise = loginDataBean.getCompanyId();

        forUserIds.add(loginDataBean.getId());
        statusList.add(HkSystemConstantUtil.ActivityServiceStatus.IN_PROCESS);
        serviceCodeList.add(HkSystemConstantUtil.SERVICE_CODE.PRINT_DYNAMIC);
//        if (!CollectionUtils.isEmpty(forUserIds) || !CollectionUtils.isEmpty(statusList) || !CollectionUtils.isEmpty(serviceCodeList)) {
//            lotDocuments = hkWorkAllotmentService.retrieveLotsOrPacketsFromWorkAllocation(null, forUserIds, null, statusList, serviceCodeList, franchise, Boolean.FALSE, Boolean.TRUE);
//            packetDocuments = hkWorkAllotmentService.retrieveLotsOrPacketsFromWorkAllocation(null, forUserIds, null, statusList, serviceCodeList, franchise, Boolean.TRUE, Boolean.TRUE);
//        }

        if (!CollectionUtils.isEmpty(lotDocuments)) {

            for (HkLotDocument lotDocument : lotDocuments) {
                invoiceIds.add(lotDocument.getInvoice());
                parcelIds.add(lotDocument.getParcel());
            }
            List<HkInvoiceDocument> hkInvoiceDocuments = stockService.retrieveInvoices(invoiceIds, franchise, Boolean.FALSE);
            List<HkParcelDocument> hkParcelDocuments = stockService.retrieveParcels(parcelIds, franchise, Boolean.FALSE);
            if (!CollectionUtils.isEmpty(hkInvoiceDocuments) && !CollectionUtils.isEmpty(hkParcelDocuments)) {
                for (HkLotDocument lotDocument : lotDocuments) {
                    Map<String, Object> map = new HashMap<>();

                    SelectItem selectItem = new SelectItem(null, lotDocument.getParcel().toString(), lotDocument.getInvoice().toString(), lotDocument.getId().toString());

                    List uiFieldListForLot = new ArrayList<>();
                    if (lotDocument.getFieldValue() != null) {
                        Map<String, Object> map1 = lotDocument.getFieldValue().toMap();
                        if (!CollectionUtils.isEmpty(map1)) {
                            for (Map.Entry<String, Object> entrySet : map1.entrySet()) {
                                String key = entrySet.getKey();
                                Object value = entrySet.getValue();
                                uiFieldListForLot.add(key);
                            }
                        }
                    }
                    Map<String, String> uiFieldListWithComponentTypeForLot = fieldService.retrieveUIFieldNameWithComponentTypes(uiFieldListForLot);
                    String pointerComponentType = HkSystemConstantUtil.CustomField.ComponentType.POINTER + "@*" + HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN;

                    HkLotDocument oldlotDocument = lotDocument;
                    Map<String, Object> customMapforLot = lotDocument.getFieldValue().toMap();

                    if (!CollectionUtils.isEmpty(uiFieldListWithComponentTypeForLot)) {
                        for (Map.Entry<String, String> field : uiFieldListWithComponentTypeForLot.entrySet()) {
                            if (field.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN) || field.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.USER_MULTISELECT) || field.getValue().equals(pointerComponentType)) {
                                if (customMapforLot != null && !customMapforLot.isEmpty() && customMapforLot.containsKey(field.getKey())) {
                                    String value = customMapforLot.get(field.getKey()).toString().replaceAll("\\[", "").replaceAll("\\]", "");
                                    customMapforLot.put(field.getKey(), value);
                                }
                            }
                        }
                        oldlotDocument.getFieldValue().putAll(customMapforLot);
                    }

                    for (String model : listOfModel) {
                        if (oldlotDocument.getFieldValue().toMap().containsKey(model)) {
                            map.put(model, oldlotDocument.getFieldValue().toMap().get(model));
                        }
                    }
                    for (HkInvoiceDocument hkInvoiceDocument : hkInvoiceDocuments) {
                        for (String model : listOfModel) {
                            if (hkInvoiceDocument.getFieldValue().toMap().containsKey(model)) {
                                map.put(model, hkInvoiceDocument.getFieldValue().toMap().get(model));
                            }
                        }
                    }

                    List uiFieldListForParcel = new ArrayList<>();
                    for (HkParcelDocument parcelDocument : hkParcelDocuments) {
                        if (parcelDocument.getFieldValue() != null) {
                            Map<String, Object> map1 = parcelDocument.getFieldValue().toMap();
                            if (!CollectionUtils.isEmpty(map1)) {
                                for (Map.Entry<String, Object> entrySet : map1.entrySet()) {
                                    String key = entrySet.getKey();
                                    uiFieldListForParcel.add(key);
                                }
                            }
                        }
                    }

                    Map<String, String> uiFieldListWithComponentTypeForParcel = fieldService.retrieveUIFieldNameWithComponentTypes(uiFieldListForParcel);
                    for (HkParcelDocument hkParcelDocument : hkParcelDocuments) {
                        HkParcelDocument oldparcelDocument = hkParcelDocument;
                        Map<String, Object> customMap = hkParcelDocument.getFieldValue().toMap();

                        if (!CollectionUtils.isEmpty(uiFieldListWithComponentTypeForParcel)) {
                            for (Map.Entry<String, String> field : uiFieldListWithComponentTypeForParcel.entrySet()) {
                                if (field.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN) || field.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.USER_MULTISELECT) || field.getValue().equals(pointerComponentType)) {
                                    if (customMap != null && !customMap.isEmpty() && customMap.containsKey(field.getKey())) {
                                        String value = customMap.get(field.getKey()).toString().replaceAll("\\[", "").replaceAll("\\]", "");
                                        customMap.put(field.getKey(), value);
                                    }
                                }
                            }
                            oldparcelDocument.getFieldValue().putAll(customMap);
                        }

                        if (hkParcelDocument.getId().toString().equals(lotDocument.getParcel().toString())) {
                            for (String model : listOfModel) {
                                if (oldparcelDocument.getFieldValue().toMap().containsKey(model)) {
                                    map.put(model, oldparcelDocument.getFieldValue().toMap().get(model));
                                }
                            }
                            break;
                        }
                    }
                    selectItem.setCategoryCustom(map);
                    selectItems.add(selectItem);
                }

            }
        }
        if (!CollectionUtils.isEmpty(packetDocuments)) {
            invoiceIds = new ArrayList<>();
            parcelIds = new ArrayList<>();
            lotIds = new ArrayList<>();
            for (HkPacketDocument packetDocument : packetDocuments) {
                invoiceIds.add(packetDocument.getInvoice());
                parcelIds.add(packetDocument.getParcel());
                lotIds.add(packetDocument.getLot());
            }
            List<HkInvoiceDocument> hkInvoiceDocuments = stockService.retrieveInvoices(invoiceIds, franchise, Boolean.FALSE);
            List<HkParcelDocument> hkParcelDocuments = stockService.retrieveParcels(parcelIds, franchise, Boolean.FALSE);
            List<HkLotDocument> hkLotDocuments = stockService.retrieveLotsByIds(lotIds, franchise, Boolean.FALSE, null, null, Boolean.TRUE);

            if (!CollectionUtils.isEmpty(hkInvoiceDocuments) && !CollectionUtils.isEmpty(hkParcelDocuments) && !CollectionUtils.isEmpty(hkLotDocuments)) {
                for (HkPacketDocument packetDocument : packetDocuments) {
                    Map<String, Object> map = new HashMap<>();

                    SelectItem selectItem = new SelectItem(null, packetDocument.getParcel().toString(), packetDocument.getInvoice().toString(), packetDocument.getLot().toString());

                    List uiFieldListForPacket = new ArrayList<>();
                    if (packetDocument.getFieldValue() != null) {
                        Map<String, Object> map1 = packetDocument.getFieldValue().toMap();
                        if (!CollectionUtils.isEmpty(map1)) {
                            for (Map.Entry<String, Object> entrySet : map1.entrySet()) {
                                String key = entrySet.getKey();
                                uiFieldListForPacket.add(key);
                            }
                        }
                    }
                    Map<String, String> uiFieldListWithComponentTypeForPacket = fieldService.retrieveUIFieldNameWithComponentTypes(uiFieldListForPacket);
                    String pointerComponentType = HkSystemConstantUtil.CustomField.ComponentType.POINTER + "@*" + HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN;

                    HkPacketDocument oldpacketDocument = packetDocument;
                    Map<String, Object> customMapforPacket = packetDocument.getFieldValue().toMap();

                    if (!CollectionUtils.isEmpty(uiFieldListWithComponentTypeForPacket)) {
                        for (Map.Entry<String, String> field : uiFieldListWithComponentTypeForPacket.entrySet()) {
                            if (field.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN) || field.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.USER_MULTISELECT) || field.getValue().equals(pointerComponentType)) {
                                if (customMapforPacket != null && !customMapforPacket.isEmpty() && customMapforPacket.containsKey(field.getKey())) {
                                    String value = customMapforPacket.get(field.getKey()).toString().replaceAll("\\[", "").replaceAll("\\]", "");
                                    customMapforPacket.put(field.getKey(), value);
                                }
                            }
                        }
                        oldpacketDocument.getFieldValue().putAll(customMapforPacket);
                    }

                    for (String model : listOfModel) {
                        if (oldpacketDocument.getFieldValue().toMap().containsKey(model)) {
                            map.put(model, oldpacketDocument.getFieldValue().toMap().get(model));
                        }
                    }
                    for (HkInvoiceDocument hkInvoiceDocument : hkInvoiceDocuments) {
                        if (hkInvoiceDocument.getId().toString().equals(packetDocument.getInvoice().toString())) {
                            for (String model : listOfModel) {
                                if (hkInvoiceDocument.getFieldValue().toMap().containsKey(model)) {
                                    map.put(model, hkInvoiceDocument.getFieldValue().toMap().get(model));
                                }
                            }
                            break;
                        }
                    }

                    List uiFieldListForParcel = new ArrayList<>();
                    for (HkParcelDocument parcelDocument : hkParcelDocuments) {
                        if (parcelDocument.getFieldValue() != null) {
                            Map<String, Object> map1 = parcelDocument.getFieldValue().toMap();
                            if (!CollectionUtils.isEmpty(map1)) {
                                for (Map.Entry<String, Object> entrySet : map1.entrySet()) {
                                    String key = entrySet.getKey();
                                    uiFieldListForParcel.add(key);
                                }
                            }
                        }
                    }

                    Map<String, String> uiFieldListWithComponentTypeForParcel = fieldService.retrieveUIFieldNameWithComponentTypes(uiFieldListForParcel);
                    for (HkParcelDocument hkParcelDocument : hkParcelDocuments) {
                        HkParcelDocument oldparcelDocument = hkParcelDocument;
                        Map<String, Object> customMap = hkParcelDocument.getFieldValue().toMap();

                        if (!CollectionUtils.isEmpty(uiFieldListWithComponentTypeForParcel)) {
                            for (Map.Entry<String, String> field : uiFieldListWithComponentTypeForParcel.entrySet()) {
                                if (field.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN) || field.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.USER_MULTISELECT) || field.getValue().equals(pointerComponentType)) {
                                    if (customMap != null && !customMap.isEmpty() && customMap.containsKey(field.getKey())) {
                                        String value = customMap.get(field.getKey()).toString().replaceAll("\\[", "").replaceAll("\\]", "");
                                        customMap.put(field.getKey(), value);
                                    }
                                }
                            }
                            oldparcelDocument.getFieldValue().putAll(customMap);
                        }

                        if (hkParcelDocument.getId().toString().equals(packetDocument.getParcel().toString())) {
                            for (String model : listOfModel) {
                                if (oldparcelDocument.getFieldValue().toMap().containsKey(model)) {
                                    map.put(model, oldparcelDocument.getFieldValue().toMap().get(model));
                                }
                            }
                            break;
                        }
                    }

                    List uiFieldListForLot = new ArrayList<>();
                    for (HkLotDocument lotDocument : hkLotDocuments) {
                        if (lotDocument.getFieldValue() != null) {
                            Map<String, Object> map1 = lotDocument.getFieldValue().toMap();
                            if (!CollectionUtils.isEmpty(map1)) {
                                for (Map.Entry<String, Object> entrySet : map1.entrySet()) {
                                    String key = entrySet.getKey();
                                    uiFieldListForLot.add(key);
                                }
                            }
                        }
                    }
                    Map<String, String> uiFieldListWithComponentTypeForLot = fieldService.retrieveUIFieldNameWithComponentTypes(uiFieldListForLot);
                    for (HkLotDocument hkLotDocument : hkLotDocuments) {
                        HkLotDocument oldlotDocument = hkLotDocument;
                        Map<String, Object> customMap = hkLotDocument.getFieldValue().toMap();

                        if (!CollectionUtils.isEmpty(uiFieldListWithComponentTypeForLot)) {
                            for (Map.Entry<String, String> field : uiFieldListWithComponentTypeForLot.entrySet()) {
                                if (field.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN) || field.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.USER_MULTISELECT) || field.getValue().equals(pointerComponentType)) {
                                    if (customMap != null && !customMap.isEmpty() && customMap.containsKey(field.getKey())) {
                                        String value = customMap.get(field.getKey()).toString().replaceAll("\\[", "").replaceAll("\\]", "");
                                        customMap.put(field.getKey(), value);
                                    }
                                }
                            }
                            oldlotDocument.getFieldValue().putAll(customMap);
                        }

                        if (hkLotDocument.getId().toString().equals(packetDocument.getParcel().toString())) {
                            for (String model : listOfModel) {
                                if (oldlotDocument.getFieldValue().toMap().containsKey(model)) {
                                    map.put(model, oldlotDocument.getFieldValue().toMap().get(model));
                                }
                            }
                            break;
                        }
                    }
                    selectItem.setCategoryCustom(map);
                    selectItems.add(selectItem);
                }

            }
        }
        return new PrintDataBean(listOfTableHeader, selectItems, null, null);
    }

    private List<Map<Object, Object>> retrieveFieldsConfigureForSearch() {
        List<Map<Object, Object>> result = new ArrayList<>();
        List<HkFieldDocument> fieldEntityList = null;
        //Retrieved FeatureId 
        UMFeatureDetailDataBean feature = applicationUtil.getuMFeatureDetailDataBeanMap().get("dynamicPrint");
        Map<HkSectionDocument, List<HkFeatureFieldPermissionDocument>> fieldPermissionEntitys = new HashMap<>();
        if (feature != null) {
            fieldPermissionEntitys = fieldService.retrieveFeatureFieldPermissionForSearch(feature.getId(), loginDataBean.getRoleIds());
            if (!CollectionUtils.isEmpty(fieldPermissionEntitys)) {
                fieldEntityList = new LinkedList<>();
                for (Map.Entry<HkSectionDocument, List<HkFeatureFieldPermissionDocument>> entry : fieldPermissionEntitys.entrySet()) {
                    List<HkFeatureFieldPermissionDocument> list = entry.getValue();
                    if (!CollectionUtils.isEmpty(list)) {
                        for (HkFeatureFieldPermissionDocument hkFeatureFieldPermissionEntity : list) {
                            if (hkFeatureFieldPermissionEntity.getHkFieldEntity() != null) {
                                fieldEntityList.add(hkFeatureFieldPermissionEntity.getHkFieldEntity());
                                Map<Object, Object> map = new HashMap<>();
                                map.put("model", hkFeatureFieldPermissionEntity.getHkFieldEntity().getDbFieldName());
                                map.put("label", hkFeatureFieldPermissionEntity.getHkFieldEntity().getFieldLabel());
                                result.add(map);
                            }
                        }
                    }
                }

            }
        }
        return result;
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
