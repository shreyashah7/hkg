package com.argusoft.hkg.nosql.model;

import java.util.Date;
import javax.persistence.Id;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Document to store loss carat of packet issued
 *
 * @author rajkumar
 */
@Document(collection = "packetcaratloss")
public class HkPacketCaratLossDocument {

    @Id
    private ObjectId id;

    //Loss of carat from packet
    private ObjectId packet;

    //Employee id of employee who loss carat
    private Long empId;

    //Department Id
    private Long departmentId;

    //Designation Id
    private Long designationId;

    //Franchise Id
    private Long companyId;

    private Long createdBy;

    private Date createdOn;

    private Long modifiedBy;

    private Date modifiedOn;

    //How many carat losses
    private Double lossCarat;

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public ObjectId getPacket() {
        return packet;
    }

    public void setPacket(ObjectId packet) {
        this.packet = packet;
    }

    public Long getEmpId() {
        return empId;
    }

    public void setEmpId(Long empId) {
        this.empId = empId;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public Long getDesignationId() {
        return designationId;
    }

    public void setDesignationId(Long designationId) {
        this.designationId = designationId;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public Long getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(Long modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public Date getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(Date modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    public Double getLossCarat() {
        return lossCarat;
    }

    public void setLossCarat(Double lossCarat) {
        this.lossCarat = lossCarat;
    }

    @Override
    public String toString() {
        return "HkLossDocument{" + "id=" + id + ", packet=" + packet + ", empId=" + empId + ", departmentId=" + departmentId + ", designationId=" + designationId + ", companyId=" + companyId + ", createdBy=" + createdBy + ", createdOn=" + createdOn + ", modifiedBy=" + modifiedBy + ", modifiedOn=" + modifiedOn + ", lossCarat=" + lossCarat + '}';
    }

}
