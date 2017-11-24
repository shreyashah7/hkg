package com.argusoft.hkg.web.center.stock.databeans;

import java.util.List;
import java.util.Map;

/**
 *
 * @author rajkumar
 */
public class FinalizeServiceDataBean {

    private String id;
    private String planObjectId;
    private String sequentialPlanId;
    private String workAllocationId;
    private String planType;
    private String referencePlan;
    private String improveFromId;
    private Boolean hasPacket;
    private String tag;
    private Long colorId;
    private Long clarityId;
    private Long cutId;
    private Long caratId;
    private Long createdBy;
    private String nameOfCreatedBy;
    private Object caratValue;
    private Long flurosceneId;
    private Object price;
    private String currencyCode;
    private Map<String, Object> finalizeCustom;
    private Map<String, String> finalizeDbType;
    private List<FinalizeServiceDataBean> finalizeServiceDataBeans;
    private List<String> deletedIds;
    private Boolean breakage;
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPlanObjectId() {
        return planObjectId;
    }

    public void setPlanObjectId(String planObjectId) {
        this.planObjectId = planObjectId;
    }

    public String getSequentialPlanId() {
        return sequentialPlanId;
    }

    public void setSequentialPlanId(String sequentialPlanId) {
        this.sequentialPlanId = sequentialPlanId;
    }

    public String getWorkAllocationId() {
        return workAllocationId;
    }

    public void setWorkAllocationId(String workAllocationId) {
        this.workAllocationId = workAllocationId;
    }

    public String getPlanType() {
        return planType;
    }

    public void setPlanType(String planType) {
        this.planType = planType;
    }

    public String getReferencePlan() {
        return referencePlan;
    }

    public void setReferencePlan(String referencePlan) {
        this.referencePlan = referencePlan;
    }

    public Boolean isHasPacket() {
        return hasPacket;
    }

    public void setHasPacket(Boolean hasPacket) {
        this.hasPacket = hasPacket;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Long getColorId() {
        return colorId;
    }

    public void setColorId(Long colorId) {
        this.colorId = colorId;
    }

    public Long getClarityId() {
        return clarityId;
    }

    public void setClarityId(Long clarityId) {
        this.clarityId = clarityId;
    }

    public Long getCutId() {
        return cutId;
    }

    public void setCutId(Long cutId) {
        this.cutId = cutId;
    }

    public Long getCaratId() {
        return caratId;
    }

    public void setCaratId(Long caratId) {
        this.caratId = caratId;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public String getNameOfCreatedBy() {
        return nameOfCreatedBy;
    }

    public void setNameOfCreatedBy(String nameOfCreatedBy) {
        this.nameOfCreatedBy = nameOfCreatedBy;
    }

    public Object getCaratValue() {
        return caratValue;
    }

    public void setCaratValue(Object caratValue) {
        this.caratValue = caratValue;
    }

    public Long getFlurosceneId() {
        return flurosceneId;
    }

    public void setFlurosceneId(Long flurosceneId) {
        this.flurosceneId = flurosceneId;
    }

    public Object getPrice() {
        return price;
    }

    public void setPrice(Object price) {
        this.price = price;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public Map<String, Object> getFinalizeCustom() {
        return finalizeCustom;
    }

    public void setFinalizeCustom(Map<String, Object> finalizeCustom) {
        this.finalizeCustom = finalizeCustom;
    }

    public Map<String, String> getFinalizeDbType() {
        return finalizeDbType;
    }

    public void setFinalizeDbType(Map<String, String> finalizeDbType) {
        this.finalizeDbType = finalizeDbType;
    }

    public List<FinalizeServiceDataBean> getFinalizeServiceDataBeans() {
        return finalizeServiceDataBeans;
    }

    public void setFinalizeServiceDataBeans(List<FinalizeServiceDataBean> finalizeServiceDataBeans) {
        this.finalizeServiceDataBeans = finalizeServiceDataBeans;
    }

    public List<String> getDeletedIds() {
        return deletedIds;
    }

    public void setDeletedIds(List<String> deletedIds) {
        this.deletedIds = deletedIds;
    }

    public Boolean isBreakage() {
        return breakage;
    }

    public void setBreakage(Boolean breakage) {
        this.breakage = breakage;
    }

    public String getImproveFromId() {
        return improveFromId;
    }

    public void setImproveFromId(String improveFromId) {
        this.improveFromId = improveFromId;
    }

    @Override
    public String toString() {
        return "FinalizeServiceDataBean{" + "id=" + id + ", planObjectId=" + planObjectId + ", sequentialPlanId=" + sequentialPlanId + ", workAllocationId=" + workAllocationId + ", planType=" + planType + ", referencePlan=" + referencePlan + ", improveFromId=" + improveFromId + ", hasPacket=" + hasPacket + ", tag=" + tag + ", colorId=" + colorId + ", clarityId=" + clarityId + ", cutId=" + cutId + ", caratId=" + caratId + ", createdBy=" + createdBy + ", nameOfCreatedBy=" + nameOfCreatedBy + ", caratValue=" + caratValue + ", flurosceneId=" + flurosceneId + ", price=" + price + ", currencyCode=" + currencyCode + ", finalizeCustom=" + finalizeCustom + ", finalizeDbType=" + finalizeDbType + ", deletedIds=" + deletedIds + ", breakage=" + breakage + '}';
    }

    
    


}
