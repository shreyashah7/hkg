/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.center.stock.transformers;

import com.argusoft.hkg.common.constantutil.HkSystemConstantUtil;
import com.argusoft.hkg.nosql.core.HkConfigurationService;
import com.argusoft.hkg.nosql.core.HkCustomFieldService;
import com.argusoft.hkg.nosql.core.HkFeatureService;
import com.argusoft.hkg.nosql.core.HkStockService;
import com.argusoft.hkg.nosql.core.HkUserService;
import com.argusoft.hkg.nosql.model.HkAssociatedDeptDocument;
import com.argusoft.hkg.nosql.model.HkDepartmentConfigDocument;
import com.argusoft.hkg.nosql.model.HkInvoiceDocument;
import com.argusoft.hkg.nosql.model.HkIssueReceiveDocument;
import com.argusoft.hkg.nosql.model.HkLotDocument;
import com.argusoft.hkg.nosql.model.HkPacketDocument;
import com.argusoft.hkg.nosql.model.HkParcelDocument;
import com.argusoft.hkg.nosql.model.HkRoleFeatureModifierDocument;
import com.argusoft.hkg.sync.center.core.HkSystemConfigurationService;
import com.argusoft.hkg.web.center.stock.databeans.IssueReceiveDataBean;
import com.argusoft.hkg.web.center.usermanagement.databeans.CenterDepartmentDataBean;
import com.argusoft.hkg.web.center.usermanagement.databeans.LoginDataBean;
import com.argusoft.hkg.web.center.util.ApplicationUtil;
import com.argusoft.hkg.web.util.SelectItem;
import com.argusoft.sync.center.model.HkDepartmentDocument;
import com.argusoft.sync.center.model.SyncCenterUserDocument;
import com.argusoft.sync.center.model.UmDesignationDocument;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
 * @author siddharth
 */
@Service
public class IssueReceiveTransformer {

    @Autowired
    LoginDataBean loginDataBean;
    @Autowired
    HkCustomFieldService customFieldService;
    @Autowired
    HkStockService stockService;
    @Autowired
    ApplicationUtil applicationUtil;
    @Autowired
    StockTransformer stockTransformer;
    @Autowired
    HkSystemConfigurationService systemConfigurationService;
    @Autowired
    HkFeatureService hkFeatureService;
    @Autowired
    HkConfigurationService hkConfigurationService;
    @Autowired
    HkUserService hkUserService;
    private static final Logger LOGGER = LoggerFactory.getLogger(IssueReceiveTransformer.class);
// List<HkLeaveDocument> leaveDocuments = stockService.retrieveLeavesForUserBetweenFromDateAndToDate(forUsers, fromDate, fromDate);
//                if (!CollectionUtils.isEmpty(leaveDocuments)) {
//                    LOGGER.info("from date" + leaveDocuments.get(0).getFrmDt());
//                    LOGGER.info("to date" + leaveDocuments.get(0).getToDt());
//                    return HkSystemConstantUtil.LeaveStatus.APPROVED;
//                } else {
//                    LOGGER.info("leave doc not found");
//                }

    /**
     * Retrieve configured modifiers for issuereceive feature
     *
     * @return Map of configured modifiers
     */
    public Map<String, List<String>> retrieveModifiers() {
        Map<String, List<String>> result = new HashMap<>();

        //Get feature id from application util
        Long featureId = applicationUtil.getFeatureNameIdMap().get(HkSystemConstantUtil.Feature.ISSUERECEIVE);
        //Retrieve configured modifier document
        List<HkRoleFeatureModifierDocument> modifiers = hkConfigurationService.retrieveModifiersByDesignations(Arrays.asList(loginDataBean.getCurrentDesignation()), loginDataBean.getCompanyId(), featureId);
        if (!CollectionUtils.isEmpty(modifiers)) {
            result.put("Medium", modifiers.get(0).getiRMediums());
            result.put("Types", modifiers.get(0).getiRTypes());
            result.put("Modes", modifiers.get(0).getiRModes());
            result.put("AccessRights", modifiers.get(0).getiRVSRAccessRights());
        }

        return result;
    }

    /**
     * Retrieve Stock(Rough,Lot or packet) details by stock number
     *
     * @param stockId
     * @return
     */
    public Map<String, Map<String, String>> retrieveStockById(Map<String, String> stockId) {
        Map<String, Map<String, String>> result = new HashMap<>();
        if (!CollectionUtils.isEmpty(stockId)) {
            //Retrieve stock details and its parent details
            Map<String, Object> stockDetail = stockService.retrieveStockAndParentdetails(stockId, loginDataBean.getCompanyId(), loginDataBean.getId(), loginDataBean.getDepartment());
            if (!CollectionUtils.isEmpty(stockDetail)) {
                if (stockDetail.containsKey(HkSystemConstantUtil.StockName.INVOICE)) {
                    result.put(HkSystemConstantUtil.StockName.INVOICE, new HashMap<>());
                    if (((HkInvoiceDocument) stockDetail.get(HkSystemConstantUtil.StockName.INVOICE)).getId() != null) {
                        result.get(HkSystemConstantUtil.StockName.INVOICE).put("invoiceId", ((HkInvoiceDocument) stockDetail.get(HkSystemConstantUtil.StockName.INVOICE)).getId().toString());
                    }
                    if (((HkInvoiceDocument) stockDetail.get(HkSystemConstantUtil.StockName.INVOICE)).getFieldValue().toMap().get(HkSystemConstantUtil.InvoiceStaticFields.INVOICE_ID) != null) {
                        result.get(HkSystemConstantUtil.StockName.INVOICE).put("invoiceNumber", ((HkInvoiceDocument) stockDetail.get(HkSystemConstantUtil.StockName.INVOICE)).getFieldValue().toMap().get(HkSystemConstantUtil.InvoiceStaticFields.INVOICE_ID).toString());
                    }
                    if (((HkInvoiceDocument) stockDetail.get(HkSystemConstantUtil.StockName.INVOICE)).getFieldValue().toMap().get(HkSystemConstantUtil.MANDATORY_FIELD.CARAT_OF_INVOICE) != null) {
                        result.get(HkSystemConstantUtil.StockName.INVOICE).put("issueCarat", ((HkInvoiceDocument) stockDetail.get(HkSystemConstantUtil.StockName.INVOICE)).getFieldValue().toMap().get(HkSystemConstantUtil.MANDATORY_FIELD.CARAT_OF_INVOICE).toString());
                    }
                }
                if (stockDetail.containsKey(HkSystemConstantUtil.StockName.PARCEL)) {
                    result.put(HkSystemConstantUtil.StockName.PARCEL, new HashMap<>());

                    if (((HkParcelDocument) stockDetail.get(HkSystemConstantUtil.StockName.PARCEL)).getId() != null) {
                        result.get(HkSystemConstantUtil.StockName.PARCEL).put("parcelId", ((HkParcelDocument) stockDetail.get(HkSystemConstantUtil.StockName.PARCEL)).getId().toString());
                    }
                    if (((HkParcelDocument) stockDetail.get(HkSystemConstantUtil.StockName.PARCEL)).getFieldValue().toMap().get(HkSystemConstantUtil.ParcelStaticFields.PARCEL_ID) != null) {
                        result.get(HkSystemConstantUtil.StockName.PARCEL).put("parcelNumber", ((HkParcelDocument) stockDetail.get(HkSystemConstantUtil.StockName.PARCEL)).getFieldValue().toMap().get(HkSystemConstantUtil.ParcelStaticFields.PARCEL_ID).toString());
                    }
                    if (((HkParcelDocument) stockDetail.get(HkSystemConstantUtil.StockName.PARCEL)).getFieldValue().toMap().get(HkSystemConstantUtil.MANDATORY_FIELD.ORIGINAL_CARAT) != null) {
                        result.get(HkSystemConstantUtil.StockName.PARCEL).put("issueCarat", ((HkParcelDocument) stockDetail.get(HkSystemConstantUtil.StockName.PARCEL)).getFieldValue().toMap().get(HkSystemConstantUtil.MANDATORY_FIELD.ORIGINAL_CARAT).toString());
                    }
                    if (((HkParcelDocument) stockDetail.get(HkSystemConstantUtil.StockName.PARCEL)).getFieldValue().toMap().get(HkSystemConstantUtil.MANDATORY_FIELD.ORIGINAL_PIECES) != null) {
                        result.get(HkSystemConstantUtil.StockName.PARCEL).put("issuePcs", ((HkParcelDocument) stockDetail.get(HkSystemConstantUtil.StockName.PARCEL)).getFieldValue().toMap().get(HkSystemConstantUtil.MANDATORY_FIELD.ORIGINAL_PIECES).toString());
                    }
                }
                if (stockDetail.containsKey(HkSystemConstantUtil.StockName.LOT)) {
                    result.put(HkSystemConstantUtil.StockName.LOT, new HashMap<>());

                    if (((HkLotDocument) stockDetail.get(HkSystemConstantUtil.StockName.LOT)).getId() != null) {
                        result.get(HkSystemConstantUtil.StockName.LOT).put("lotId", ((HkLotDocument) stockDetail.get(HkSystemConstantUtil.StockName.LOT)).getId().toString());
                    }
                    if (((HkLotDocument) stockDetail.get(HkSystemConstantUtil.StockName.LOT)).getFieldValue().toMap().get(HkSystemConstantUtil.LotStaticFieldName.LOT_ID) != null) {
                        result.get(HkSystemConstantUtil.StockName.LOT).put("lotNumber", ((HkLotDocument) stockDetail.get(HkSystemConstantUtil.StockName.LOT)).getFieldValue().toMap().get(HkSystemConstantUtil.LotStaticFieldName.LOT_ID).toString());
                    }
                    if (((HkLotDocument) stockDetail.get(HkSystemConstantUtil.StockName.LOT)).getFieldValue().toMap().get(HkSystemConstantUtil.MANDATORY_FIELD.CARAT_OF_LOT) != null) {
                        result.get(HkSystemConstantUtil.StockName.LOT).put("issueCarat", ((HkLotDocument) stockDetail.get(HkSystemConstantUtil.StockName.LOT)).getFieldValue().toMap().get(HkSystemConstantUtil.MANDATORY_FIELD.CARAT_OF_LOT).toString());
                    }
                    if (((HkLotDocument) stockDetail.get(HkSystemConstantUtil.StockName.LOT)).getFieldValue().toMap().get(HkSystemConstantUtil.LotStaticFieldName.PIECES) != null) {
                        result.get(HkSystemConstantUtil.StockName.LOT).put("issuePcs", ((HkLotDocument) stockDetail.get(HkSystemConstantUtil.StockName.LOT)).getFieldValue().toMap().get(HkSystemConstantUtil.LotStaticFieldName.PIECES).toString());
                    }
                }
                if (stockDetail.containsKey(HkSystemConstantUtil.StockName.PACKET)) {
                    result.put(HkSystemConstantUtil.StockName.PACKET, new HashMap<>());

                    if (((HkPacketDocument) stockDetail.get(HkSystemConstantUtil.StockName.PACKET)).getId() != null) {
                        result.get(HkSystemConstantUtil.StockName.PACKET).put("packetId", ((HkPacketDocument) stockDetail.get(HkSystemConstantUtil.StockName.PACKET)).getId().toString());
                    }
                    if (((HkPacketDocument) stockDetail.get(HkSystemConstantUtil.StockName.PACKET)).getFieldValue().toMap().get(HkSystemConstantUtil.AutoNumber.PACKET_ID) != null) {
                        result.get(HkSystemConstantUtil.StockName.PACKET).put("packetNumber", ((HkPacketDocument) stockDetail.get(HkSystemConstantUtil.StockName.PACKET)).getFieldValue().toMap().get(HkSystemConstantUtil.AutoNumber.PACKET_ID).toString());
                    }
                    if (((HkPacketDocument) stockDetail.get(HkSystemConstantUtil.StockName.PACKET)).getFieldValue().toMap().get(HkSystemConstantUtil.MANDATORY_FIELD.CARAT_OF_PACKET) != null) {
                        result.get(HkSystemConstantUtil.StockName.PACKET).put("issueCarat", ((HkPacketDocument) stockDetail.get(HkSystemConstantUtil.StockName.PACKET)).getFieldValue().toMap().get(HkSystemConstantUtil.MANDATORY_FIELD.CARAT_OF_PACKET).toString());
                    }
                    if (((HkPacketDocument) stockDetail.get(HkSystemConstantUtil.StockName.PACKET)).getFieldValue().toMap().get(HkSystemConstantUtil.PacketStaticFieldName.PIECES) != null) {
                        result.get(HkSystemConstantUtil.StockName.PACKET).put("issuePcs", ((HkPacketDocument) stockDetail.get(HkSystemConstantUtil.StockName.PACKET)).getFieldValue().toMap().get(HkSystemConstantUtil.PacketStaticFieldName.PIECES).toString());
                    }
                }
                //If there is any error, put it in Message
                if (stockDetail.get("Message") != null) {
                    result.put("Message", new HashMap<>());
                    result.get("Message").put("Message", stockDetail.get("Message").toString());
                }
            }
        }
        return result;
    }

    /**
     * Convertor for IssueReceiveDataBean to IssueReceiveDocument
     *
     * @param issueReceiveDataBean Data bean to convert
     * @param hkIssueReceiveDocument existing document
     * @return resulted IssueReceive Document
     */
    private HkIssueReceiveDocument convertIssueReceiveDataBeanToDocument(IssueReceiveDataBean issueReceiveDataBean, HkIssueReceiveDocument hkIssueReceiveDocument) {
        if (hkIssueReceiveDocument == null) {
            hkIssueReceiveDocument = new HkIssueReceiveDocument();
        }
        if (issueReceiveDataBean.getId() != null) {
            hkIssueReceiveDocument.setId(new ObjectId(issueReceiveDataBean.getId()));
        }
        if (issueReceiveDataBean.getInvoiceId() != null) {
            hkIssueReceiveDocument.setInvoice(new ObjectId(issueReceiveDataBean.getInvoiceId()));
        }
        if (issueReceiveDataBean.getParcelId() != null) {
            hkIssueReceiveDocument.setParcel(new ObjectId(issueReceiveDataBean.getParcelId()));
        }
        if (issueReceiveDataBean.getLotId() != null) {
            hkIssueReceiveDocument.setLot(new ObjectId(issueReceiveDataBean.getLotId()));
        }
        if (issueReceiveDataBean.getPacketId() != null) {
            hkIssueReceiveDocument.setPacket(new ObjectId(issueReceiveDataBean.getPacketId()));
        }
        if (issueReceiveDataBean.getSrcDeptId() != null) {
            hkIssueReceiveDocument.setSourceDepId(issueReceiveDataBean.getSrcDeptId());
        }
        if (issueReceiveDataBean.getDestDeptId() != null) {
            hkIssueReceiveDocument.setDestinationDepId(issueReceiveDataBean.getDestDeptId());
        }
        if (issueReceiveDataBean.getStockDeptId() != null) {
            hkIssueReceiveDocument.setStockDepId(issueReceiveDataBean.getStockDeptId());
        }
        if (!StringUtils.isEmpty(issueReceiveDataBean.getType())) {
            hkIssueReceiveDocument.setType(issueReceiveDataBean.getType());
        }
        if (!StringUtils.isEmpty(issueReceiveDataBean.getStatus())) {
            hkIssueReceiveDocument.setStatus(issueReceiveDataBean.getStatus());
        } else {
            if (issueReceiveDataBean.getRequestType() != null) {
                switch (issueReceiveDataBean.getRequestType()) {
                    case HkSystemConstantUtil.IssueReceiveOperations.ISSUE_INWARD:
                    case HkSystemConstantUtil.IssueReceiveOperations.RECEIVE_INWARD:
                    case HkSystemConstantUtil.IssueReceiveOperations.RECEIVE:
                        hkIssueReceiveDocument.setStatus(HkSystemConstantUtil.IssueReceiveStatus.COMPLETE);
                        break;
                    case HkSystemConstantUtil.IssueReceiveOperations.REQUEST:
                        hkIssueReceiveDocument.setStatus(HkSystemConstantUtil.IssueReceiveStatus.PENDING);
                        break;
                    case HkSystemConstantUtil.IssueReceiveOperations.COLLECT:
                        hkIssueReceiveDocument.setStatus(HkSystemConstantUtil.IssueReceiveStatus.COLLECTED);
                        break;
                    case HkSystemConstantUtil.IssueReceiveOperations.ISSUE:
                        hkIssueReceiveDocument.setStatus(HkSystemConstantUtil.IssueReceiveStatus.ISSUE);
                        break;
                }
            }
        }
        hkIssueReceiveDocument.setIsActive(true);
        hkIssueReceiveDocument.setModifiedBy(loginDataBean.getId());
        if (issueReceiveDataBean.getIssueCarat() != null) {
            hkIssueReceiveDocument.setIssueCarat(issueReceiveDataBean.getIssueCarat());
        }
        if (issueReceiveDataBean.getIssuePcs() != null) {
            hkIssueReceiveDocument.setIssuePcs(issueReceiveDataBean.getIssuePcs());
        }
        if (issueReceiveDataBean.getReceiveCarat() != null) {
            hkIssueReceiveDocument.setReceivedCarat(issueReceiveDataBean.getReceiveCarat());
        }
        if (issueReceiveDataBean.getReceivePcs() != null) {
            hkIssueReceiveDocument.setReceivedPcs(issueReceiveDataBean.getReceivePcs());
        }
        return hkIssueReceiveDocument;
    }

    /**
     * Retrieve IssueReceiveDocument By Slip Number
     *
     * @param stock should contain type of request(i.e RT,CL,RI etc.) and slip
     * number and slip date
     * @return Stock that are part of that slip number on specified date
     */
    public List<IssueReceiveDataBean> retrieveStockBySlip(IssueReceiveDataBean stock) {
        List<HkIssueReceiveDocument> issuereceieveDocuments = new ArrayList<>();
        //Retrieve documents based on request type
        if (stock.getRequestType().equals(HkSystemConstantUtil.IssueReceiveOperations.REQUEST)) {
            issuereceieveDocuments = stockService.retrieveAvailableStockToRequest(loginDataBean.getId(), loginDataBean.getCompanyId(), loginDataBean.getCurrentDesignation());
        } else if (stock.getRequestType().equals(HkSystemConstantUtil.IssueReceiveOperations.RETURN)) {
            issuereceieveDocuments = stockService.retrieveIssueReceiveDocumentsForReturn(loginDataBean.getId(), loginDataBean.getDepartment(), loginDataBean.getCompanyId());
        } else if (!stock.getRequestType().equals(HkSystemConstantUtil.IssueReceiveOperations.RECEIVE_INWARD)) {
            issuereceieveDocuments = stockService.retrieveIssueReceiveDocumentsBySlipNo(stock.getSlipDate(), stock.getSlipNo(), loginDataBean.getId(), loginDataBean.getDepartment(), loginDataBean.getCompanyId(), stock.getRequestType());
        } else {
            issuereceieveDocuments = stockService.retrieveIssueReceiveDocumentsBySlipNoForDirectReceive(stock.getSlipDate(), stock.getSlipNo(), loginDataBean.getDepartment(), loginDataBean.getCompanyId(), stock.getRequestType());
        }
        if (!CollectionUtils.isEmpty(issuereceieveDocuments)) {
            List<ObjectId> invoiceIds = new ArrayList<>();
            List<ObjectId> parcelIds = new ArrayList<>();
            List<ObjectId> lotIds = new ArrayList<>();
            List<ObjectId> packetIds = new ArrayList<>();

            for (HkIssueReceiveDocument hkIssueReceiveDocument : issuereceieveDocuments) {
                if (hkIssueReceiveDocument.getInvoice() != null) {
                    invoiceIds.add(hkIssueReceiveDocument.getInvoice());
                }
                if (hkIssueReceiveDocument.getParcel() != null) {
                    parcelIds.add(hkIssueReceiveDocument.getParcel());
                }
                if (hkIssueReceiveDocument.getLot() != null) {
                    lotIds.add(hkIssueReceiveDocument.getLot());
                }
                if (hkIssueReceiveDocument.getPacket() != null) {
                    packetIds.add(hkIssueReceiveDocument.getPacket());
                }
            }

            //Retrieve details of related parrent entities
            List<HkInvoiceDocument> invoices = stockService.retrieveInvoices(invoiceIds, null, null);
            List<HkParcelDocument> parcels = stockService.retrieveParcels(parcelIds, null, null);
            List<HkLotDocument> lots = stockService.retrieveLotsByIds(lotIds, null, null, null, null, true);
            List<HkPacketDocument> packets = stockService.retrievePacketsByIds(packetIds, null, null, null, true);
            Map<String, String> invoiceIdToObj = new HashMap<>();
            if (!CollectionUtils.isEmpty(invoices)) {
                for (HkInvoiceDocument hkInvoiceDocument : invoices) {
                    if (hkInvoiceDocument.getFieldValue().get(HkSystemConstantUtil.InvoiceStaticFields.INVOICE_ID) != null) {
                        invoiceIdToObj.put(hkInvoiceDocument.getId().toString(), hkInvoiceDocument.getFieldValue().get(HkSystemConstantUtil.InvoiceStaticFields.INVOICE_ID).toString());
                    }
                }
            }
            Map<String, String> parcelIdToObj = new HashMap<>();
            if (!CollectionUtils.isEmpty(parcels)) {
                for (HkParcelDocument hkParcelDocument : parcels) {
                    if (hkParcelDocument.getFieldValue().get(HkSystemConstantUtil.ParcelStaticFields.PARCEL_ID) != null) {
                        parcelIdToObj.put(hkParcelDocument.getId().toString(), hkParcelDocument.getFieldValue().get(HkSystemConstantUtil.ParcelStaticFields.PARCEL_ID).toString());
                    }
                }
            }
            Map<String, String> lotIdToObj = new HashMap<>();
            if (!CollectionUtils.isEmpty(lots)) {
                for (HkLotDocument hkLotDocument : lots) {
                    if (hkLotDocument.getFieldValue().get(HkSystemConstantUtil.LotStaticFieldName.LOT_ID) != null) {
                        lotIdToObj.put(hkLotDocument.getId().toString(), hkLotDocument.getFieldValue().get(HkSystemConstantUtil.LotStaticFieldName.LOT_ID).toString());
                    }
                }
            }
            Map<String, String> packetIdToObj = new HashMap<>();
            if (!CollectionUtils.isEmpty(packets)) {
                for (HkPacketDocument hkPacketDocument : packets) {
                    if (hkPacketDocument.getFieldValue().get(HkSystemConstantUtil.PacketStaticFieldName.PACKET_ID) != null) {
                        packetIdToObj.put(hkPacketDocument.getId().toString(), hkPacketDocument.getFieldValue().get(HkSystemConstantUtil.PacketStaticFieldName.PACKET_ID).toString());
                    }
                }
            }
            List<IssueReceiveDataBean> result = new ArrayList<>();
            Set<Long> userIds = new HashSet<>();
            Set<Long> departmentIds = new HashSet<>();
            for (HkIssueReceiveDocument issu : issuereceieveDocuments) {
                if (issu.getCreatedBy() != null) {
                    userIds.add(issu.getCreatedBy());
                }
                if (issu.getSourceDepId() != null) {
                    departmentIds.add(issu.getSourceDepId());
                }
            }
            List<SyncCenterUserDocument> users = hkUserService.retrieveUsers(new ArrayList<>(userIds));
            List<HkDepartmentDocument> departments = hkUserService.retrieveDepartmentsByIds(new ArrayList<>(departmentIds));
            Map<Long, String> userIdToName = new HashMap<>();
            Map<Long, String> deptIdToName = new HashMap<>();
            if (!CollectionUtils.isEmpty(users)) {
                for (SyncCenterUserDocument syncCenterUserDocument : users) {
                    userIdToName.put(syncCenterUserDocument.getId(), syncCenterUserDocument.getFirstName() + " " + syncCenterUserDocument.getLastName());
                }
            }
            if (!CollectionUtils.isEmpty(departments)) {
                for (HkDepartmentDocument department : departments) {
                    deptIdToName.put(department.getId(), department.getDeptName());
                }
            }
            //Convert documents to databean
            System.out.println("invoiceIdToObj:::" + invoiceIdToObj);
            for (HkIssueReceiveDocument issu : issuereceieveDocuments) {
                IssueReceiveDataBean issueReceiveBean = this.convertIssueReceiveDocumentToDataBean(issu, null);
                System.out.println("issueReceiveBean.getInvoiceId()::" + issueReceiveBean.getInvoiceId());
                issueReceiveBean.setInvoiceNumber(invoiceIdToObj.get(issueReceiveBean.getInvoiceId()));
                issueReceiveBean.setParcelNumber(parcelIdToObj.get(issueReceiveBean.getParcelId()));
                issueReceiveBean.setLotNumber(lotIdToObj.get(issueReceiveBean.getLotId()));
                issueReceiveBean.setPacketNumber(packetIdToObj.get(issueReceiveBean.getPacketId()));
                issueReceiveBean.setCreatedByUserName(userIdToName.get(issu.getCreatedBy()));
                issueReceiveBean.setSourceDepartmentName(deptIdToName.get(issu.getSourceDepId()));
                if (issu.getFieldValue() != null) {
                    issueReceiveBean.setFieldValue(issu.getFieldValue().toMap());
                }

                result.add(issueReceiveBean);
            }
            return result;
        }

        return null;
    }

    /**
     * Convertor to convert IssueReceiveDocument to DataBean
     *
     * @param hkIssueReceiveDocument Document to convert
     * @param issueReceiveDataBean Existing databean or null
     * @return Converted DataBean
     */
    private IssueReceiveDataBean convertIssueReceiveDocumentToDataBean(HkIssueReceiveDocument hkIssueReceiveDocument, IssueReceiveDataBean issueReceiveDataBean) {
        if (issueReceiveDataBean == null) {
            issueReceiveDataBean = new IssueReceiveDataBean();
        }
        if (hkIssueReceiveDocument.getId() != null) {
            issueReceiveDataBean.setId(hkIssueReceiveDocument.getId().toString());
        }
        if (hkIssueReceiveDocument.getInvoice() != null) {
            issueReceiveDataBean.setInvoiceId(hkIssueReceiveDocument.getInvoice().toString());
        }
        if (hkIssueReceiveDocument.getParcel() != null) {
            issueReceiveDataBean.setParcelId(hkIssueReceiveDocument.getParcel().toString());
        }
        if (hkIssueReceiveDocument.getLot() != null) {
            issueReceiveDataBean.setLotId(hkIssueReceiveDocument.getLot().toString());
        }
        if (hkIssueReceiveDocument.getPacket() != null) {
            issueReceiveDataBean.setPacketId(hkIssueReceiveDocument.getPacket().toString());
        }
        issueReceiveDataBean.setIssueCarat(hkIssueReceiveDocument.getIssueCarat());
        issueReceiveDataBean.setIssuePcs(hkIssueReceiveDocument.getIssuePcs());
        issueReceiveDataBean.setReceiveCarat(hkIssueReceiveDocument.getReceivedCarat());
        issueReceiveDataBean.setReceivePcs(hkIssueReceiveDocument.getReceivedPcs());

        issueReceiveDataBean.setType(hkIssueReceiveDocument.getType());
        issueReceiveDataBean.setStatus(hkIssueReceiveDocument.getStatus());
        issueReceiveDataBean.setSrcDeptId(hkIssueReceiveDocument.getSourceDepId());
        issueReceiveDataBean.setDestDeptId(hkIssueReceiveDocument.getDestinationDepId());
        issueReceiveDataBean.setStockDeptId(hkIssueReceiveDocument.getStockDepId());
        issueReceiveDataBean.setSrcFranchiseId(hkIssueReceiveDocument.getSourceFranchiseId());
        issueReceiveDataBean.setDestFranchiseId(hkIssueReceiveDocument.getDestinationFranchiseId());
        issueReceiveDataBean.setStockFranchiseId(hkIssueReceiveDocument.getStockFranchiseId());
        issueReceiveDataBean.setReceivedBy(hkIssueReceiveDocument.getReceiveBy());
        issueReceiveDataBean.setReceivedOn(hkIssueReceiveDocument.getReceivedOn());
        issueReceiveDataBean.setCollectedBy(hkIssueReceiveDocument.getCollectedBy());
        issueReceiveDataBean.setCollectedOn(hkIssueReceiveDocument.getCollectedOn());
        issueReceiveDataBean.setIssuedBy(hkIssueReceiveDocument.getIssuedBy());
        issueReceiveDataBean.setIssuedOn(hkIssueReceiveDocument.getIssuedOn());
        issueReceiveDataBean.setIssueTo(hkIssueReceiveDocument.getIssueTo());
        issueReceiveDataBean.setIsActive(hkIssueReceiveDocument.getIsActive());
        issueReceiveDataBean.setSlipDate(hkIssueReceiveDocument.getSlipDate());
        issueReceiveDataBean.setSlipNo(hkIssueReceiveDocument.getSlipNo());
        return issueReceiveDataBean;
    }

    /**
     * Request for IssueReceive
     *
     * @param issueReceiveDataBeans
     */
    public void requestIssueReceive(List<IssueReceiveDataBean> issueReceiveDataBeans) {
        if (!CollectionUtils.isEmpty(issueReceiveDataBeans)) {
            Date today = new Date();
            List<HkIssueReceiveDocument> listToSave = new ArrayList<>();
            for (IssueReceiveDataBean issueReceiveDataBean : issueReceiveDataBeans) {
                HkIssueReceiveDocument hkIssueReceiveDocument = new HkIssueReceiveDocument();

                if (issueReceiveDataBean.getInvoiceId() != null) {
                    hkIssueReceiveDocument.setInvoice(new ObjectId(issueReceiveDataBean.getInvoiceId()));
                }
                if (issueReceiveDataBean.getParcelId() != null) {
                    hkIssueReceiveDocument.setParcel(new ObjectId(issueReceiveDataBean.getParcelId()));
                }
                if (issueReceiveDataBean.getLotId() != null) {
                    hkIssueReceiveDocument.setLot(new ObjectId(issueReceiveDataBean.getLotId()));
                }
                if (issueReceiveDataBean.getPacketId() != null) {
                    hkIssueReceiveDocument.setPacket(new ObjectId(issueReceiveDataBean.getPacketId()));
                }
                hkIssueReceiveDocument.setSourceDepId(issueReceiveDataBean.getSrcDeptId());
                hkIssueReceiveDocument.setDestinationDepId(issueReceiveDataBean.getDestDeptId());

                hkIssueReceiveDocument.setSourceDesignationId(loginDataBean.getCurrentDesignation());
                hkIssueReceiveDocument.setDestinationDesignationId(issueReceiveDataBean.getDestinationDesignationId());
                hkIssueReceiveDocument.setModifier(issueReceiveDataBean.getModifier());
                hkIssueReceiveDocument.setType(issueReceiveDataBean.getType());
                hkIssueReceiveDocument.setModifiedBy(loginDataBean.getId());
                hkIssueReceiveDocument.setModifiedOn(today);
                hkIssueReceiveDocument.setCreatedBy(loginDataBean.getId());
                hkIssueReceiveDocument.setCreatedOn(today);
                hkIssueReceiveDocument.setIssueCarat(issueReceiveDataBean.getIssueCarat());
                hkIssueReceiveDocument.setIssuePcs(issueReceiveDataBean.getIssuePcs());
                if (!CollectionUtils.isEmpty(issueReceiveDataBean.getFieldValue())) {
                    //makeBson Object from fieldValue of custom fields
                    BasicBSONObject bSONObject = customFieldService.makeBSONObject(issueReceiveDataBean.getFieldValue(), issueReceiveDataBean.getFieldDbType(), HkSystemConstantUtil.Feature.ISSUE, loginDataBean.getCompanyId(), null, null);
                    hkIssueReceiveDocument.setFieldValue(bSONObject);
                }
                listToSave.add(hkIssueReceiveDocument);
            }
            stockService.issueReceiverequest(listToSave, loginDataBean.getCompanyId());
        }
    }

    /**
     * Collect requested stock
     *
     * @param stock Databeans that contains ids of requested stock and received
     * carats and pieces
     */
    public void collect(List<IssueReceiveDataBean> stock) {
        if (!CollectionUtils.isEmpty(stock)) {
            List<ObjectId> stockIds = new ArrayList<>();
            Map<ObjectId, Map<String, Object>> fielValsMap = new HashMap<>();
            Map<String, String> fieldDbType = stock.get(0).getFieldDbType();
            for (IssueReceiveDataBean issueReceiveDataBean : stock) {
                if (issueReceiveDataBean.getId() != null) {
                    stockIds.add(new ObjectId(issueReceiveDataBean.getId()));
                }
                fielValsMap.put(new ObjectId(issueReceiveDataBean.getId()), issueReceiveDataBean.getFieldValue());
            }

            stockService.issueReceiveCollect(stockIds, loginDataBean.getId(), loginDataBean.getDepartment(), loginDataBean.getCompanyId(), fielValsMap, fieldDbType);
        }
    }

    /**
     * Issue Stock to destination department or designation
     *
     * @param stock DataBean with Ids of stock to receive
     * @return Slip Number generated
     */
    public String issue(List<IssueReceiveDataBean> stock) {
        if (!CollectionUtils.isEmpty(stock)) {
            List<ObjectId> issuereceiveIds = new ArrayList<>();
            Long destinationFranchiseId = stock.get(0).getDestFranchiseId();
            Map<ObjectId, Long> issueToMap = new HashMap<>();
            Map<ObjectId, Map<String, Object>> fielValsMap = new HashMap<>();
            Map<String, String> fieldDbType = stock.get(0).getFieldDbType();
            for (IssueReceiveDataBean issueReceiveDataBean : stock) {
                issuereceiveIds.add(new ObjectId(issueReceiveDataBean.getId()));
                issueToMap.put(new ObjectId(issueReceiveDataBean.getId()), issueReceiveDataBean.getIssueTo());
                fielValsMap.put(new ObjectId(issueReceiveDataBean.getId()), issueReceiveDataBean.getFieldValue());

            }
            return stockService.issueReceiveIssue(issuereceiveIds, destinationFranchiseId, loginDataBean.getId(), issueToMap, fielValsMap, fieldDbType);
        }
        return null;
    }

    /**
     * Receive stock assigned to logged in user
     *
     * @param stock Ids of stock to receive
     */
    public void receive(List<IssueReceiveDataBean> stock) {
        if (!CollectionUtils.isEmpty(stock)) {
            List<ObjectId> stockIds = new ArrayList<>();
            Map<ObjectId, Map<String, Object>> fielValsMap = new HashMap<>();
            Map<String, String> fieldDbType = stock.get(0).getFieldDbType();
            //Prepare list stock ids to collect
            for (IssueReceiveDataBean issueReceiveDataBean : stock) {
                if (issueReceiveDataBean.getId() != null) {
                    stockIds.add(new ObjectId(issueReceiveDataBean.getId()));
                    fielValsMap.put(new ObjectId(issueReceiveDataBean.getId()), issueReceiveDataBean.getFieldValue());
                }
            }

            stockService.issueReceiveReceive(stockIds, loginDataBean.getId(), fielValsMap, fieldDbType);
        }
    }

    /**
     * Issue direct to any user of any department or designation
     *
     * @param stock Details of stock to issue direcetly
     * @return Slip Number generated
     */
    public Integer issueInward(List<IssueReceiveDataBean> stock) {
        if (!CollectionUtils.isEmpty(stock)) {
            List<HkIssueReceiveDocument> listToSave = new ArrayList<>();
            Long destDeptId = stock.get(0).getDestDeptId();
            Long destDesigId = stock.get(0).getDestinationDesignationId();
            Long issueTo = stock.get(0).getIssueTo();
            for (IssueReceiveDataBean issueReceiveDataBean : stock) {
                HkIssueReceiveDocument hkIssueReceiveDocument = new HkIssueReceiveDocument();

                if (issueReceiveDataBean.getInvoiceId() != null) {
                    hkIssueReceiveDocument.setInvoice(new ObjectId(issueReceiveDataBean.getInvoiceId()));
                }
                if (issueReceiveDataBean.getParcelId() != null) {
                    hkIssueReceiveDocument.setParcel(new ObjectId(issueReceiveDataBean.getParcelId()));
                }
                if (issueReceiveDataBean.getLotId() != null) {
                    hkIssueReceiveDocument.setLot(new ObjectId(issueReceiveDataBean.getLotId()));
                }
                if (issueReceiveDataBean.getPacketId() != null) {
                    hkIssueReceiveDocument.setPacket(new ObjectId(issueReceiveDataBean.getPacketId()));
                }

                hkIssueReceiveDocument.setModifier(issueReceiveDataBean.getModifier());
//                hkIssueReceiveDocument.setType(issueReceiveDataBean.getType());

                hkIssueReceiveDocument.setIssueCarat(issueReceiveDataBean.getIssueCarat());
                hkIssueReceiveDocument.setIssuePcs(issueReceiveDataBean.getIssuePcs());
                if (!CollectionUtils.isEmpty(issueReceiveDataBean.getFieldValue())) {
                    BasicBSONObject bSONObject = customFieldService.makeBSONObject(issueReceiveDataBean.getFieldValue(), issueReceiveDataBean.getFieldDbType(), HkSystemConstantUtil.Feature.ISSUE, loginDataBean.getCompanyId(), null, null);
                    hkIssueReceiveDocument.setFieldValue(bSONObject);
                }
                listToSave.add(hkIssueReceiveDocument);
            }
            return stockService.issueReceiveInwardIssue(listToSave, loginDataBean.getDepartment(), destDeptId, loginDataBean.getCurrentDesignation(), destDesigId, issueTo, loginDataBean.getId(), loginDataBean.getCompanyId());
        }
        return null;
    }

    /**
     * Receive stock issued directly
     *
     * @param stock Stock Ids to receive with received carat and pieces
     */
    public void receiveInward(List<IssueReceiveDataBean> stock) {
        if (!CollectionUtils.isEmpty(stock)) {
            List<ObjectId> objectIds = new ArrayList<>();
            Map<ObjectId, Double> objectIdToCarat = new HashMap<>();
            Map<ObjectId, Integer> objectIdToPc = new HashMap<>();
            Map<ObjectId, Map<String, Object>> fielValsMap = new HashMap<>();
            Map<String, String> fieldDbType = stock.get(0).getFieldDbType();
            //Prepare list of stock ids to receive and received carat and pieces
            for (IssueReceiveDataBean issueReceiveDataBean : stock) {
                objectIds.add(new ObjectId(issueReceiveDataBean.getId()));
                objectIdToCarat.put(new ObjectId(issueReceiveDataBean.getId()), issueReceiveDataBean.getReceiveCarat());
                objectIdToPc.put(new ObjectId(issueReceiveDataBean.getId()), issueReceiveDataBean.getReceivePcs());
                fielValsMap.put(new ObjectId(issueReceiveDataBean.getId()), issueReceiveDataBean.getFieldValue());
            }

            stockService.issueReceiveReceiveInward(objectIds, objectIdToCarat, objectIdToPc, fielValsMap, fieldDbType);
        }
    }

    /**
     * Retrieve department and child list in tree view
     *
     * @param department department Id
     * @return List of child department
     */
    public List<CenterDepartmentDataBean> retrieveDepartmentListInTreeViewSimple(Long department) {
        List<CenterDepartmentDataBean> departmentDataBeans = new ArrayList<>();
        CenterDepartmentDataBean departmentDataBean = new CenterDepartmentDataBean();
        List<HkDepartmentDocument> hkDepartmentDocuments;
        List<SyncCenterUserDocument> centerUserDocuments;
        hkDepartmentDocuments = hkUserService.retrieveDepartmentsForTreeView(department);

        if (!CollectionUtils.isEmpty(hkDepartmentDocuments)) {
            List<Long> deptIds = new ArrayList<>();
            Map<Long, HkDepartmentDocument> deptIdToDoc = new HashMap<>();
            Map<Long, List<HkDepartmentDocument>> parentToChilds = new HashMap<>();
            for (HkDepartmentDocument hkDepartmentDocument : hkDepartmentDocuments) {
                deptIds.add(hkDepartmentDocument.getId());
                deptIdToDoc.put(hkDepartmentDocument.getId(), hkDepartmentDocument);
                if (hkDepartmentDocument.getParentId() != null) {
                    if (parentToChilds.get(hkDepartmentDocument.getParentId()) == null) {
                        parentToChilds.put(hkDepartmentDocument.getParentId(), new ArrayList<>());
                    }
                    parentToChilds.get(hkDepartmentDocument.getParentId()).add(hkDepartmentDocument);
                }
            }
            centerUserDocuments = hkUserService.retrieveUsersByDepartmentIds(null, null, deptIds, Boolean.TRUE, true, null);
            if (!CollectionUtils.isEmpty(centerUserDocuments)) {
                Map<Long, List<SyncCenterUserDocument>> deptToUsers = new HashMap<>();
                for (SyncCenterUserDocument syncCenterUserDocument : centerUserDocuments) {
                    if (deptToUsers.get(syncCenterUserDocument.getDepartmentId()) == null) {
                        deptToUsers.put(syncCenterUserDocument.getDepartmentId(), new ArrayList<>());
                    }
                    deptToUsers.get(syncCenterUserDocument.getDepartmentId()).add(syncCenterUserDocument);
                }

                if (deptIdToDoc.get(department) != null) {
                    departmentDataBean.setId(department);
                    departmentDataBean.setDisplayName(deptIdToDoc.get(department).getDeptName());
                    departmentDataBean = this.prepareTreeView(departmentDataBean, deptToUsers, deptIdToDoc, parentToChilds);
                }
            }
        }
        if (departmentDataBean.getId() == null) {
            departmentDataBean.setDisplayName("No users");
        }
        departmentDataBeans.add(departmentDataBean);
        return departmentDataBeans;
    }

    /**
     * Prepate tree view recursively
     *
     * @param departmentDataBean department data bean of parent
     * @param deptToUsers department to users map
     * @param deptIdToDoc department to its document map
     * @param parentToChilds parent id to child ids map
     * @return Prepared tree view databean
     */
    private CenterDepartmentDataBean prepareTreeView(CenterDepartmentDataBean departmentDataBean, Map<Long, List<SyncCenterUserDocument>> deptToUsers, Map<Long, HkDepartmentDocument> deptIdToDoc, Map<Long, List<HkDepartmentDocument>> parentToChilds) {
        List<CenterDepartmentDataBean> childs = new ArrayList<>();
        if (deptToUsers.get(departmentDataBean.getId()) != null) {
            for (SyncCenterUserDocument userDocument : deptToUsers.get(departmentDataBean.getId())) {
                CenterDepartmentDataBean departmentDataBean1 = new CenterDepartmentDataBean();
                departmentDataBean1.setId(userDocument.getId());
                departmentDataBean1.setDisplayName(userDocument.getFirstName() + " " + userDocument.getLastName());
                departmentDataBean1.setParentId(departmentDataBean.getId());

                childs.add(departmentDataBean1);
            }

        }
        if (parentToChilds.get(departmentDataBean.getId()) != null) {
            for (HkDepartmentDocument hkDepartmentDocument : parentToChilds.get(departmentDataBean.getId())) {
                CenterDepartmentDataBean departmentDataBean1 = new CenterDepartmentDataBean();
                departmentDataBean1.setId(hkDepartmentDocument.getId());
                departmentDataBean1.setDisplayName(hkDepartmentDocument.getDeptName());
                departmentDataBean1.setParentId(departmentDataBean.getId());

                childs.add(this.prepareTreeView(departmentDataBean1, deptToUsers, deptIdToDoc, parentToChilds));
            }
        }
        departmentDataBean.setChildren(childs);
        return departmentDataBean;
    }

    /**
     * Reject stock to collect
     *
     * @param stock Ids of stock to reject
     */
    public void reject(List<IssueReceiveDataBean> stock) {
        if (!CollectionUtils.isEmpty(stock)) {
            List<ObjectId> stockIds = new ArrayList<>();
            for (IssueReceiveDataBean issueReceiveDataBean : stock) {
                if (issueReceiveDataBean.getId() != null) {
                    stockIds.add(new ObjectId(issueReceiveDataBean.getId()));
                }
            }
            stockService.issueReceiveReject(stockIds, loginDataBean.getId(), loginDataBean.getDepartment(), loginDataBean.getCompanyId());
        }
    }

    /**
     * Retrieve associated department of department passed in parameter
     *
     * @param deptId Department id of main department
     * @return List of associated departments
     */
    public List<SelectItem> retrieveAssociatedDepts(Long deptId) {
        if (deptId == null) {
            deptId = loginDataBean.getDepartment();
        }
        List<SelectItem> result = new ArrayList<>();
        Map<Long, HkAssociatedDeptDocument> associatedDepts = hkConfigurationService.retrieveMapOfAssociatedDepartmentsForDept(deptId);
        if (!CollectionUtils.isEmpty(associatedDepts)) {
            for (Iterator<Map.Entry<Long, HkAssociatedDeptDocument>> it = associatedDepts.entrySet().iterator(); it.hasNext();) {
                Map.Entry<Long, HkAssociatedDeptDocument> entry = it.next();
                if (entry.getValue().getIsArchive()) {
                    it.remove();
                }
            }
        }
        HkDepartmentConfigDocument deptConfig = hkConfigurationService.retrieveDocumentByDepartmentId(deptId, loginDataBean.getCompanyId());
        if (!CollectionUtils.isEmpty(associatedDepts)) {
            Set<Long> deptIds = associatedDepts.keySet();
            Map<Long, HkDepartmentDocument> depts = hkUserService.retrieveDepartmentMapByIds(new ArrayList<>(deptIds));
            if (!CollectionUtils.isEmpty(depts)) {
                for (Map.Entry<Long, HkDepartmentDocument> entry : depts.entrySet()) {
                    if (!entry.getKey().equals(loginDataBean.getDepartment())) {
                        SelectItem item = new SelectItem(entry.getKey(), deptConfig.getStockRoom() != null ? deptConfig.getStockRoom().toString() : null, entry.getValue().getDeptName());
                        item.setCommonId(associatedDepts.get(entry.getKey()).getMedium());
                        result.add(item);
                    }
                }
            }
        }
        return result;
    }

    /**
     * Return stock to issuer
     *
     * @param stock Ids of stock to return
     */
    public void returnStock(List<IssueReceiveDataBean> stock) {
        if (!CollectionUtils.isEmpty(stock)) {
            List<ObjectId> objectIds = new ArrayList<>();

            for (IssueReceiveDataBean issueReceiveDataBean : stock) {
                objectIds.add(new ObjectId(issueReceiveDataBean.getId()));
            }

            stockService.returnStock(objectIds, loginDataBean.getCompanyId(), loginDataBean.getId(), loginDataBean.getCurrentDesignation());
        }
    }

    /**
     * Retrieve Details of stock which are issued but not received by receiver
     *
     * @param requestType type of request (i.e IS or RC)
     * @return List of IssueReceivedatBean which are not received
     */
    public List<IssueReceiveDataBean> retrievePendingIssued(String requestType) {

        if (!StringUtils.isEmpty(requestType)) {
            List<HkIssueReceiveDocument> issuereceieveDocuments = stockService.retrievePendingIssuedStock(loginDataBean.getId(), loginDataBean.getDepartment(), loginDataBean.getCompanyId(), requestType);

            if (!CollectionUtils.isEmpty(issuereceieveDocuments)) {
                List<ObjectId> invoiceIds = new ArrayList<>();
                List<ObjectId> parcelIds = new ArrayList<>();
                List<ObjectId> lotIds = new ArrayList<>();
                List<ObjectId> packetIds = new ArrayList<>();

                for (HkIssueReceiveDocument hkIssueReceiveDocument : issuereceieveDocuments) {
                    if (hkIssueReceiveDocument.getInvoice() != null) {
                        invoiceIds.add(hkIssueReceiveDocument.getInvoice());
                    }
                    if (hkIssueReceiveDocument.getParcel() != null) {
                        parcelIds.add(hkIssueReceiveDocument.getParcel());
                    }
                    if (hkIssueReceiveDocument.getLot() != null) {
                        lotIds.add(hkIssueReceiveDocument.getLot());
                    }
                    if (hkIssueReceiveDocument.getPacket() != null) {
                        packetIds.add(hkIssueReceiveDocument.getPacket());
                    }
                }
                //Prepare stock details
                List<HkInvoiceDocument> invoices = stockService.retrieveInvoices(invoiceIds, null, null);
                List<HkParcelDocument> parcels = stockService.retrieveParcels(parcelIds, null, null);
                List<HkLotDocument> lots = stockService.retrieveLotsByIds(lotIds, null, null, null, null, true);
                List<HkPacketDocument> packets = stockService.retrievePacketsByIds(packetIds, null, null, null, true);
                Map<String, String> invoiceIdToObj = new HashMap<>();
                if (!CollectionUtils.isEmpty(invoices)) {
                    for (HkInvoiceDocument hkInvoiceDocument : invoices) {
                        if (hkInvoiceDocument.getFieldValue().get(HkSystemConstantUtil.InvoiceStaticFields.INVOICE_ID) != null) {
                            invoiceIdToObj.put(hkInvoiceDocument.getId().toString(), hkInvoiceDocument.getFieldValue().get(HkSystemConstantUtil.InvoiceStaticFields.INVOICE_ID).toString());
                        }
                    }
                }
                Map<String, String> parcelIdToObj = new HashMap<>();
                if (!CollectionUtils.isEmpty(parcels)) {
                    for (HkParcelDocument hkParcelDocument : parcels) {
                        if (hkParcelDocument.getFieldValue().get(HkSystemConstantUtil.ParcelStaticFields.PARCEL_ID) != null) {
                            parcelIdToObj.put(hkParcelDocument.getId().toString(), hkParcelDocument.getFieldValue().get(HkSystemConstantUtil.ParcelStaticFields.PARCEL_ID).toString());
                        }
                    }
                }
                Map<String, String> lotIdToObj = new HashMap<>();
                if (!CollectionUtils.isEmpty(lots)) {
                    for (HkLotDocument hkLotDocument : lots) {
                        if (hkLotDocument.getFieldValue().get(HkSystemConstantUtil.LotStaticFieldName.LOT_ID) != null) {
                            lotIdToObj.put(hkLotDocument.getId().toString(), hkLotDocument.getFieldValue().get(HkSystemConstantUtil.LotStaticFieldName.LOT_ID).toString());
                        }
                    }
                }
                Map<String, String> packetIdToObj = new HashMap<>();
                if (!CollectionUtils.isEmpty(packets)) {
                    for (HkPacketDocument hkPacketDocument : packets) {
                        if (hkPacketDocument.getFieldValue().get(HkSystemConstantUtil.PacketStaticFieldName.PACKET_ID) != null) {
                            packetIdToObj.put(hkPacketDocument.getId().toString(), hkPacketDocument.getFieldValue().get(HkSystemConstantUtil.PacketStaticFieldName.PACKET_ID).toString());
                        }
                    }
                }
                List<IssueReceiveDataBean> result = new ArrayList<>();
                Set<Long> userIds = new HashSet<>();
                Set<Long> departmentIds = new HashSet<>();
                for (HkIssueReceiveDocument issu : issuereceieveDocuments) {
                    userIds.add(issu.getCreatedBy());
                    userIds.add(issu.getIssueTo());
                    departmentIds.add(issu.getSourceDepId());
                    departmentIds.add(issu.getDestinationDepId());
                }
                List<SyncCenterUserDocument> users = hkUserService.retrieveUsers(new ArrayList<>(userIds));
                List<HkDepartmentDocument> departments = hkUserService.retrieveDepartmentsByIds(new ArrayList<>(departmentIds));
                Map<Long, String> userIdToName = new HashMap<>();
                Map<Long, String> deptIdToName = new HashMap<>();
                if (!CollectionUtils.isEmpty(users)) {
                    for (SyncCenterUserDocument syncCenterUserDocument : users) {
                        userIdToName.put(syncCenterUserDocument.getId(), syncCenterUserDocument.getFirstName() + " " + syncCenterUserDocument.getLastName());
                    }
                }
                if (!CollectionUtils.isEmpty(departments)) {
                    for (HkDepartmentDocument department : departments) {
                        deptIdToName.put(department.getId(), department.getDeptName());
                    }
                }
                for (HkIssueReceiveDocument issu : issuereceieveDocuments) {
                    IssueReceiveDataBean issueReceiveBean = this.convertIssueReceiveDocumentToDataBean(issu, null);

                    issueReceiveBean.setInvoiceNumber(invoiceIdToObj.get(issueReceiveBean.getInvoiceId()));
                    issueReceiveBean.setParcelNumber(parcelIdToObj.get(issueReceiveBean.getParcelId()));
                    issueReceiveBean.setLotNumber(lotIdToObj.get(issueReceiveBean.getLotId()));
                    issueReceiveBean.setPacketNumber(packetIdToObj.get(issueReceiveBean.getPacketId()));
                    issueReceiveBean.setCreatedByUserName(userIdToName.get(issu.getCreatedBy()));
                    issueReceiveBean.setSourceDepartmentName(deptIdToName.get(issu.getSourceDepId()));
                    issueReceiveBean.setDestinationDepartmentName(deptIdToName.get(issu.getDestinationDepId()));
                    issueReceiveBean.setSlipNo(issu.getSlipNo());
                    issueReceiveBean.setSlipDate(issu.getSlipDate());
                    if (issu.getIssueTo() != null) {
                        issueReceiveBean.setIssueToUserName(userIdToName.get(issu.getIssueTo()));
                    }
                    result.add(issueReceiveBean);
                }
                return result;
            }
        }
        return null;
    }

    /**
     * Retrieve designation list of associated with department
     *
     * @param depId department Id
     * @return List of designations associated with this department
     */
    public List<SelectItem> retrieveDesignationByDepartment(Long depId) {
        List<UmDesignationDocument> listUMRole = hkUserService.retrieveDesignationsByDepartment(depId);
        List<SelectItem> selectItems = new ArrayList<>();
        if (!CollectionUtils.isEmpty(listUMRole)) {
            Collections.sort(listUMRole, new Comparator() {
                @Override
                public int compare(Object o1, Object o2) {
                    UmDesignationDocument role1 = (UmDesignationDocument) o1;
                    UmDesignationDocument role2 = (UmDesignationDocument) o2;
                    return role1.getModifiedOn().compareTo(role2.getModifiedOn());
                }

            });
            for (UmDesignationDocument designationDocument : listUMRole) {
                if (designationDocument.getName() != null) {
                    SelectItem selectItem = new SelectItem(designationDocument.getId(), designationDocument.getName());
                    selectItems.add(selectItem);
                }
            }
        }
        return selectItems;
    }
}
