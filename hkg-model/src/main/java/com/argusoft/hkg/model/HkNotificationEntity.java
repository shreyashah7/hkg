package com.argusoft.hkg.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Mayank
 */
@Entity
@Table(name = "hk_notification_info")
public class HkNotificationEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private String id;
    @Basic(optional = false)
    @Column(name = "notification_type", nullable = false, length = 20)
    private String notificationType;
    @Basic(optional = false)
    @Column(name = "on_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date onDate;
    @Basic(optional = false)
    @Column(nullable = false, length = 5000)
    private String description;
    @Column(name = "instance_type", length = 50)
    private String instanceType;
    @Column(name = "instance_id")
    private Long instanceId;
    @Basic(optional = false)
    @Column(nullable = false)
    private long franchise;
    @Basic(optional = false)
    @Column(name = "is_archive", nullable = false)
    private boolean isArchive;
    @Column(name = "notification_configuration_type")
    private String notificationConfigurationType;
    @Column(name = "notification_configuration")
    private Long notificationConfiguration;
    @Column(name = "mail_status")
    private String mailStatus;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "hkNotificationEntity")
    private Set<HkNotificationRecipientEntity> hkNotificationRecipientEntitySet;

    public HkNotificationEntity() {
    }

    public HkNotificationEntity(String id) {
        this.id = id;
    }

    public HkNotificationEntity(String id, String notificationType, Date onDate, String description, long franchise, boolean isArchive) {
        this.id = id;
        this.notificationType = notificationType;
        this.onDate = onDate;
        this.description = description;
        this.franchise = franchise;
        this.isArchive = isArchive;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(String notificationType) {
        this.notificationType = notificationType;
    }

    public Date getOnDate() {
        return onDate;
    }

    public void setOnDate(Date onDate) {
        this.onDate = onDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getInstanceType() {
        return instanceType;
    }

    public void setInstanceType(String instanceType) {
        this.instanceType = instanceType;
    }

    public Long getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(Long instanceId) {
        this.instanceId = instanceId;
    }

    public long getFranchise() {
        return franchise;
    }

    public void setFranchise(long franchise) {
        this.franchise = franchise;
    }

    public boolean getIsArchive() {
        return isArchive;
    }

    public void setIsArchive(boolean isArchive) {
        this.isArchive = isArchive;
    }

    public Set<HkNotificationRecipientEntity> getHkNotificationRecipientEntitySet() {
        return hkNotificationRecipientEntitySet;
    }

    public void setHkNotificationRecipientEntitySet(Set<HkNotificationRecipientEntity> hkNotificationRecipientEntitySet) {
        this.hkNotificationRecipientEntitySet = hkNotificationRecipientEntitySet;
    }

    public String getNotificationConfigurationType() {
        return notificationConfigurationType;
    }

    public void setNotificationConfigurationType(String notificationConfigurationType) {
        this.notificationConfigurationType = notificationConfigurationType;
    }

    public Long getNotificationConfiguration() {
        return notificationConfiguration;
    }

    public void setNotificationConfiguration(Long notificationConfiguration) {
        this.notificationConfiguration = notificationConfiguration;
    }

    public String getMailStatus() {
        return mailStatus;
    }

    public void setMailStatus(String mailStatus) {
        this.mailStatus = mailStatus;
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
        if (!(object instanceof HkNotificationEntity)) {
            return false;
        }
        HkNotificationEntity other = (HkNotificationEntity) object;
        if ((this.id == null && other.id == null) || (this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.argusoft.hkg.model.HkNotificationEntity[ id=" + id + " ]";
    }

}