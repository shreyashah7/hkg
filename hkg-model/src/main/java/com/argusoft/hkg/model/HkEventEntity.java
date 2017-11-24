/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.argusoft.hkg.model;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 *
 * @author Mayank
 */
@Entity
@Table(name = "hk_system_event_info")
@NamedQueries({
    @NamedQuery(name = "HkEventEntity.findAll", query = "SELECT h FROM HkEventEntity h")})
public class HkEventEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Long id;
    @Basic(optional = false)
    @Column(name = "event_title", nullable = false, length = 100)
    private String eventTitle;
    @Column(name = "banner_image_name", length = 500)
    private String bannerImageName;
    @Column(name = "label_color", length = 50)
    private String labelColor;
    @Column(name = "content_color", length = 50)
    private String contentColor;
    @Column(length = 1000)
    private String description;
    @Column(length = 1000)
    private String address;
    @Column(name = "frm_dt")
    @Temporal(TemporalType.TIMESTAMP)
    private Date frmDt;
    @Column(name = "to_dt")
    @Temporal(TemporalType.TIMESTAMP)
    private Date toDt;
    @Column(name = "strt_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date strtTime;
    @Column(name = "end_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endTime;
    @Column(name = "published_on")
    @Temporal(TemporalType.TIMESTAMP)
    private Date publishedOn;
    @Basic(optional = false)
    @Column(name = "registration_type", nullable = false, length = 5)
    private String registrationType;
    @Column(name = "registration_last_dt")
    @Temporal(TemporalType.TIMESTAMP)
    private Date registrationLastDt;
    @Column(name = "folder_name", length = 100)
    private String folderName;
    @Basic(optional = false)
    @Column(nullable = false, length = 10)
    private String status;
    @Basic(optional = false)
    @Column(name = "is_archive", nullable = false)
    private boolean isArchive;
    @Basic(optional = false)
    @Column(name = "created_by", nullable = false)
    private long createdBy;
    @Basic(optional = false)
    @Column(name = "created_on", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdOn;
    @Basic(optional = false)
    @Column(name = "last_modified_by", nullable = false)
    private long lastModifiedBy;
    @Basic(optional = false)
    @Column(name = "last_modified_on", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModifiedOn;
    @Basic(optional = false)
    @Column(nullable = false)
    private long franchise;
    @Column(name="registration_form_name")
    private String registrationFormName;
    @Column(name="registration_reminder_sent")
    private Boolean registrationReminderSent;
    @Column(name = "registration_count")
    private Integer registrationCount;
    @Column(name = "not_attending_count")
    private Integer notAttendingCount;
    @Column(name = "adult_count")
    private Integer adultCount;
    @Column(name = "child_count")
    private Integer childCount;
    @Column(name = "guest_count")
    private Integer guestCount;
    @Column(name = "invitation_template_name", length = 200)
    private String invitationTemplateName;
    @JoinColumn(name = "category", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private HkCategoryEntity category;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "hkEventEntity")
    private Set<HkEventRecipientEntity> hkEventRecipientEntitySet;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "event", fetch = FetchType.LAZY)
    private Set<HkEventRegistrationFieldEntity> hkEventRegistrationFieldEntitySet;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "hkEventEntity", fetch = FetchType.LAZY)
    private Set<HkEventRegistrationEntity> hkEventRegistrationEntitySet;

    public HkEventEntity() {
    }

    public HkEventEntity(Long id) {
        this.id = id;
    }

    public HkEventEntity(Long id, String eventTitle, String registrationType, String status, boolean isArchive, long createdBy, Date createdOn, long lastModifiedBy, Date lastModifiedOn, long franchise) {
        this.id = id;
        this.eventTitle = eventTitle;
        this.registrationType = registrationType;
        this.status = status;
        this.isArchive = isArchive;
        this.createdBy = createdBy;
        this.createdOn = createdOn;
        this.lastModifiedBy = lastModifiedBy;
        this.lastModifiedOn = lastModifiedOn;
        this.franchise = franchise;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEventTitle() {
        return eventTitle;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public String getBannerImageName() {
        return bannerImageName;
    }

    public void setBannerImageName(String bannerImageName) {
        this.bannerImageName = bannerImageName;
    }

    public String getLabelColor() {
        return labelColor;
    }

    public void setLabelColor(String labelColor) {
        this.labelColor = labelColor;
    }

    public String getContentColor() {
        return contentColor;
    }

    public void setContentColor(String contentColor) {
        this.contentColor = contentColor;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getFrmDt() {
        return frmDt;
    }

    public void setFrmDt(Date frmDt) {
        this.frmDt = frmDt;
    }

    public Date getToDt() {
        return toDt;
    }

    public void setToDt(Date toDt) {
        this.toDt = toDt;
    }

    public Date getStrtTime() {
        return strtTime;
    }

    public void setStrtTime(Date strtTime) {
        this.strtTime = strtTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getPublishedOn() {
        return publishedOn;
    }

    public void setPublishedOn(Date publishedOn) {
        this.publishedOn = publishedOn;
    }

    public String getRegistrationType() {
        return registrationType;
    }

    public void setRegistrationType(String registrationType) {
        this.registrationType = registrationType;
    }

    public Date getRegistrationLastDt() {
        return registrationLastDt;
    }

    public void setRegistrationLastDt(Date registrationLastDt) {
        this.registrationLastDt = registrationLastDt;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean getIsArchive() {
        return isArchive;
    }

    public void setIsArchive(boolean isArchive) {
        this.isArchive = isArchive;
    }

    public long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(long createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public long getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(long lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Date getLastModifiedOn() {
        return lastModifiedOn;
    }

    public void setLastModifiedOn(Date lastModifiedOn) {
        this.lastModifiedOn = lastModifiedOn;
    }

    public long getFranchise() {
        return franchise;
    }

    public void setFranchise(long franchise) {
        this.franchise = franchise;
    }

    public String getRegistrationFormName() {
        return registrationFormName;
    }

    public void setRegistrationFormName(String registrationFormName) {
        this.registrationFormName = registrationFormName;
    }

    public Integer getRegistrationCount() {
        return registrationCount;
    }

    public void setRegistrationCount(Integer registrationCount) {
        this.registrationCount = registrationCount;
    }

    public Integer getNotAttendingCount() {
        return notAttendingCount;
    }

    public void setNotAttendingCount(Integer notAttendingCount) {
        this.notAttendingCount = notAttendingCount;
    }

    public Integer getAdultCount() {
        return adultCount;
    }

    public void setAdultCount(Integer adultCount) {
        this.adultCount = adultCount;
    }

    public Integer getChildCount() {
        return childCount;
    }

    public void setChildCount(Integer childCount) {
        this.childCount = childCount;
    }

    public Integer getGuestCount() {
        return guestCount;
    }

    public void setGuestCount(Integer guestCount) {
        this.guestCount = guestCount;
    }

    public String getInvitationTemplateName() {
        return invitationTemplateName;
    }

    public void setInvitationTemplateName(String invitationTemplateName) {
        this.invitationTemplateName = invitationTemplateName;
    }

    public HkCategoryEntity getCategory() {
        return category;
    }

    public void setCategory(HkCategoryEntity category) {
        this.category = category;
    }

    public Set<HkEventRecipientEntity> getHkEventRecipientEntitySet() {
        return hkEventRecipientEntitySet;
    }

    public void setHkEventRecipientEntitySet(Set<HkEventRecipientEntity> hkEventRecipientEntitySet) {
        this.hkEventRecipientEntitySet = hkEventRecipientEntitySet;
    }

    public Set<HkEventRegistrationFieldEntity> getHkEventRegistrationFieldEntitySet() {
        return hkEventRegistrationFieldEntitySet;
    }

    public void setHkEventRegistrationFieldEntitySet(Set<HkEventRegistrationFieldEntity> hkEventRegistrationFieldEntitySet) {
        this.hkEventRegistrationFieldEntitySet = hkEventRegistrationFieldEntitySet;
    }

    public Set<HkEventRegistrationEntity> getHkEventRegistrationEntitySet() {
        return hkEventRegistrationEntitySet;
    }

    public void setHkEventRegistrationEntitySet(Set<HkEventRegistrationEntity> hkEventRegistrationEntitySet) {
        this.hkEventRegistrationEntitySet = hkEventRegistrationEntitySet;
    }

    public Boolean getRegistrationReminderSent() {
        return registrationReminderSent;
    }

    public void setRegistrationReminderSent(Boolean registrationReminderSent) {
        this.registrationReminderSent = registrationReminderSent;
    }
    
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof HkEventEntity)) {
            return false;
        }
        HkEventEntity other = (HkEventEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.argusoft.hkg.model.HkEventEntity[ id=" + id + " ]";
    }
    
}
