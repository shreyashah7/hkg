/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.sync.xmpp.util;

import com.argusoft.hkg.nosql.model.HkAllotmentAggregationTimeDocument;
import com.argusoft.hkg.nosql.model.HkCalcBasPriceDocument;
import com.argusoft.hkg.nosql.model.HkCalcFourCDiscountDocument;
import com.argusoft.hkg.nosql.model.HkCalcMasterDocument;
import com.argusoft.hkg.nosql.model.HkCalcRateDetailDocument;
import com.argusoft.hkg.nosql.model.HkCriteriaSetDocument;
import com.argusoft.hkg.nosql.model.HkDepartmentConfigDocument;
import com.argusoft.hkg.nosql.model.HkInvoiceDocument;
import com.argusoft.hkg.nosql.model.HkIssueDocument;
import com.argusoft.hkg.nosql.model.HkLotDocument;
import com.argusoft.hkg.nosql.model.HkPacketDocument;
import com.argusoft.hkg.nosql.model.HkParcelDocument;
import com.argusoft.hkg.nosql.model.HkPersonCapabilityDocument;
import com.argusoft.hkg.nosql.model.HkPlanAccessPermissionDocument;
import com.argusoft.hkg.nosql.model.HkPlanDocument;
import com.argusoft.hkg.nosql.model.HkPurchaseDocument;
import com.argusoft.hkg.nosql.model.HkRoleFeatureModifierDocument;
import com.argusoft.hkg.nosql.model.HkSellDocument;
import com.argusoft.hkg.nosql.model.HkSubEntityExceptionDocument;
import com.argusoft.hkg.nosql.model.HkSubFormValueDocument;
import com.argusoft.hkg.nosql.model.HkSubLotDocument;
import com.argusoft.hkg.nosql.model.HkTransferDocument;
import com.argusoft.hkg.nosql.model.HkUserGoalStatusDocument;
import com.argusoft.hkg.nosql.model.HkValueExceptionDocument;
import com.argusoft.sync.center.model.HkFieldDocument;
import com.argusoft.sync.center.model.HkSectionDocument;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author shruti
 */
public class SyncDocumentFieldMapper {

    public final static Map<Class<?>, FieldInfo> documentFieldMap = new HashMap<>();

    static {
        List<String> franchiseIdMethods = new LinkedList<>();

        franchiseIdMethods.add("getFranchiseId");
        FieldInfo fieldInfo = new FieldInfo("id", null, null);
        documentFieldMap.put(HkAllotmentAggregationTimeDocument.class, fieldInfo);
        fieldInfo = new FieldInfo("getId", franchiseIdMethods, "getLastModifiedOn", SyncTransferType.ONE_TO_MANY);
        documentFieldMap.put(HkPacketDocument.class, fieldInfo);
        documentFieldMap.put(HkIssueDocument.class, fieldInfo);
        documentFieldMap.put(HkPlanDocument.class, fieldInfo);
        documentFieldMap.put(HkSellDocument.class, fieldInfo);
        documentFieldMap.put(HkTransferDocument.class, fieldInfo);
        documentFieldMap.put(HkUserGoalStatusDocument.class, fieldInfo);
        documentFieldMap.put(HkPlanAccessPermissionDocument.class, fieldInfo);
        fieldInfo = new FieldInfo("getId", franchiseIdMethods, "getLastModifiedOn", SyncTransferType.SEND_TO_MASTERS);
        documentFieldMap.put(HkLotDocument.class, fieldInfo);
        fieldInfo = new FieldInfo("getId", franchiseIdMethods, "getModifiedOn", SyncTransferType.SEND_TO_ALL);
        documentFieldMap.put(HkSubFormValueDocument.class, fieldInfo);
        documentFieldMap.put(HkSubLotDocument.class, fieldInfo);
        fieldInfo = new FieldInfo("getId", null, "getModifiedOn", SyncTransferType.SEND_TO_ALL);
        documentFieldMap.put(HkCalcBasPriceDocument.class, fieldInfo);
        documentFieldMap.put(HkCalcFourCDiscountDocument.class, fieldInfo);
        documentFieldMap.put(HkCalcRateDetailDocument.class, fieldInfo);
        documentFieldMap.put(HkCalcMasterDocument.class, fieldInfo);
        documentFieldMap.put(HkCalcRateDetailDocument.class, fieldInfo);
        fieldInfo = new FieldInfo("getId", franchiseIdMethods, "getLastModifiedOn", SyncTransferType.SEND_TO_ALL);
        documentFieldMap.put(HkInvoiceDocument.class, fieldInfo);
        documentFieldMap.put(HkParcelDocument.class, fieldInfo);
        documentFieldMap.put(HkPurchaseDocument.class, fieldInfo);
        fieldInfo = new FieldInfo("getId", franchiseIdMethods, "getModifiedOn", SyncTransferType.ONE_TO_MANY);
        documentFieldMap.put(HkCriteriaSetDocument.class, fieldInfo);

        franchiseIdMethods = new LinkedList<>();
        franchiseIdMethods.add("getFranchise");
        fieldInfo = new FieldInfo("getId", franchiseIdMethods, "getLastModifiedOn", SyncTransferType.ONE_TO_MANY);
        documentFieldMap.put(HkPersonCapabilityDocument.class, fieldInfo);
        documentFieldMap.put(HkValueExceptionDocument.class, fieldInfo);
        documentFieldMap.put(HkSubEntityExceptionDocument.class, fieldInfo);
        documentFieldMap.put(HkDepartmentConfigDocument.class, fieldInfo);
//        documentFieldMap.put(HkFeatureFieldPermissionDocument.class, fieldInfo);
        documentFieldMap.put(HkFieldDocument.class, fieldInfo);
        documentFieldMap.put(HkRoleFeatureModifierDocument.class, fieldInfo);

        fieldInfo = new FieldInfo("getId", null, "getLastModifiedOn", SyncTransferType.SEND_TO_ALL);
        documentFieldMap.put(HkSectionDocument.class, fieldInfo);

        
    }

    public static Map<Class<?>, FieldInfo> getDocumentFieldMap() {
        return documentFieldMap;
    }

    public static FieldInfo getFieldInfo(Class<?> documentClass) {
        if (documentFieldMap.containsKey(documentClass)) {
            return documentFieldMap.get(documentClass);
        }
        return null;
    }

    public static class FieldInfo {

        private String idMethodName;
        private List<String> franchiseIdMethodName;
        private String modifiedOnMethodName;
        private int syncTrasferType;

        public FieldInfo() {
        }

        public FieldInfo(String idFieldName, List<String> franchiseIdFieldName, String modifiedOnFieldName) {
            syncTrasferType = SyncTransferType.ONE_TO_MANY;
            this.idMethodName = idFieldName;
            this.franchiseIdMethodName = franchiseIdFieldName;
            this.modifiedOnMethodName = modifiedOnFieldName;
        }

        public FieldInfo(String idFieldName, List<String> franchiseIdFieldName, String modifiedOnFieldName, int syncTrasferType) {
            this.idMethodName = idFieldName;
            this.franchiseIdMethodName = franchiseIdFieldName;
            this.modifiedOnMethodName = modifiedOnFieldName;
            this.syncTrasferType = syncTrasferType;
        }

        public String getIdMethodName() {
            return idMethodName;
        }

        public void setIdMethodName(String idMethodName) {
            this.idMethodName = idMethodName;
        }

        public String getModifiedOnFieldName() {
            return modifiedOnMethodName;
        }

        public void setModifiedOnFieldName(String modifiedOnFieldName) {
            this.modifiedOnMethodName = modifiedOnFieldName;
        }

        public int getSyncTrasferType() {
            return syncTrasferType;
        }

        public void setSyncTrasferType(int syncTrasferType) {
            this.syncTrasferType = syncTrasferType;
        }

        /**
         * @return the franchiseIdMethodName
         */
        public List<String> getFranchiseIdMethodName() {
            return franchiseIdMethodName;
        }

        /**
         * @param franchiseIdMethodName the franchiseIdMethodName to set
         */
        public void setFranchiseIdMethodName(List<String> franchiseIdMethodName) {
            this.franchiseIdMethodName = franchiseIdMethodName;
        }

    }

}
