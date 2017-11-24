package com.argusoft.hkg.web.center.stock.databeans;

import java.util.List;
import java.util.Map;

/**
 *
 * @author rajkumar
 */
public class PlanDataBean {

    private String id;
    private String invoiceId;
    private String parcelId;
    private String lotId;
    private String packetId;
    private Integer planId;
    private String copiedFrom;
    private String packetNumber;
    private Long empId;
    private Long designationId;
    private Long departmentId;
    private String empName;
    private Boolean finalPlan;
    private Long packetInStockOf;
    private List<Map<String, Object>> tags;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    public String getParcelId() {
        return parcelId;
    }

    public void setParcelId(String parcelId) {
        this.parcelId = parcelId;
    }

    public String getLotId() {
        return lotId;
    }

    public void setLotId(String lotId) {
        this.lotId = lotId;
    }

    public String getPacketId() {
        return packetId;
    }

    public void setPacketId(String packetId) {
        this.packetId = packetId;
    }

    public Integer getPlanId() {
        return planId;
    }

    public void setPlanId(Integer planId) {
        this.planId = planId;
    }

    public List<Map<String, Object>> getTags() {
        return tags;
    }

    public void setTags(List<Map<String, Object>> tags) {
        this.tags = tags;
    }

    public String getCopiedFrom() {
        return copiedFrom;
    }

    public void setCopiedFrom(String copiedFrom) {
        this.copiedFrom = copiedFrom;
    }

    public String getPacketNumber() {
        return packetNumber;
    }

    public void setPacketNumber(String packetNumber) {
        this.packetNumber = packetNumber;
    }

    public Long getEmpId() {
        return empId;
    }

    public void setEmpId(Long empId) {
        this.empId = empId;
    }

    public Boolean isFinalPlan() {
        return finalPlan;
    }

    public void setFinalPlan(Boolean finalPlan) {
        this.finalPlan = finalPlan;
    }

    public Long getPacketInStockOf() {
        return packetInStockOf;
    }

    public void setPacketInStockOf(Long packetInStockOf) {
        this.packetInStockOf = packetInStockOf;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public Long getDesignationId() {
        return designationId;
    }

    public void setDesignationId(Long designationId) {
        this.designationId = designationId;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    @Override
    public String toString() {
        return "PlanDataBean{" + "id=" + id + ", invoiceId=" + invoiceId + ", parcelId=" + parcelId + ", lotId=" + lotId + ", packetId=" + packetId + ", planId=" + planId + ", copiedFrom=" + copiedFrom + ", packetNumber=" + packetNumber + ", empId=" + empId + ", designationId=" + designationId + ", departmentId=" + departmentId + ", empName=" + empName + ", finalPlan=" + finalPlan + ", packetInStockOf=" + packetInStockOf + ", tags=" + tags + '}';
    }

}
