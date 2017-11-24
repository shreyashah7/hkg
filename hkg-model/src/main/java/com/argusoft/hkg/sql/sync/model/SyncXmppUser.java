package com.argusoft.hkg.sql.sync.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

/**
 *
 * @author Shruti
 */

@Entity
@Table(name = "hk_xmpp_user",
        uniqueConstraints = {
            @UniqueConstraint(columnNames = {"franchise"}),
            @UniqueConstraint(columnNames = {"user_name"})})

public class SyncXmppUser implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
//    @NotNull
//    @Index(name = "usernameIndex")
    @Basic(optional = false)
    @Column(name = "user_name")
    private String userName;
    @Column(name = "password")
    private String password;
    //Type of user could be master/center
    @Column(name = "type")
    private String type;
    @Column(name = "email_address")
    private String emailAddress;
    @Basic(optional = true)
    @Column(name = "created_by")
    private long createdBy;
    @Basic(optional = false)
    @Column(name = "created_on")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdOn;
    @Column(name = "modified_by")
    private Long modifiedBy;
    @Column(name = "modified_on")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedOn;
    @Column(name = "expired_on")
    @Temporal(TemporalType.TIMESTAMP)
    private Date expiredOn;
    @Basic(optional = false)
    @Column(name = "is_active")
    private boolean isActive;
    @Basic(optional = false)
    @Column(name = "is_archive")
    private boolean isArchive;
    @Column(name = "status")
    private String status;
    @Column(name = "inactive_reason")
    private String inactiveReason;
    @Column(name = "last_login_on")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastLoginOn;
//    @NotNull
    @Column(name = "franchise")
    private Long franchise;

    @Column(name = "user_created_in_tigase", columnDefinition = "boolean default false")
    private Boolean userCreatedInTigase;

    public SyncXmppUser() {
    }

    public SyncXmppUser(Long id) {
        this.id = id;
    }

    public SyncXmppUser(Long id, String userName, long createdBy, Date createdOn, boolean isActive, boolean isArchive) {
        this.id = id;
        this.userName = userName;
        this.createdBy = createdBy;
        this.createdOn = createdOn;
        this.isActive = isActive;
        this.isArchive = isArchive;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 83 * hash + Objects.hashCode(this.userName);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SyncXmppUser other = (SyncXmppUser) obj;
        if (!Objects.equals(this.userName, other.userName)) {
            return false;
        }
        return true;
    }

  

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
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

    public Date getExpiredOn() {
        return expiredOn;
    }

    public void setExpiredOn(Date expiredOn) {
        this.expiredOn = expiredOn;
    }

    public boolean isIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public boolean isIsArchive() {
        return isArchive;
    }

    public void setIsArchive(boolean isArchive) {
        this.isArchive = isArchive;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getInactiveReason() {
        return inactiveReason;
    }

    public void setInactiveReason(String inactiveReason) {
        this.inactiveReason = inactiveReason;
    }

    public Date getLastLoginOn() {
        return lastLoginOn;
    }

    public void setLastLoginOn(Date lastLoginOn) {
        this.lastLoginOn = lastLoginOn;
    }

    public Long getFranchise() {
        return franchise;
    }

    public void setFranchise(Long franchise) {
        this.franchise = franchise;
    }

    public Boolean getUserCreatedInTigase() {
        return userCreatedInTigase;
    }

    public void setUserCreatedInTigase(Boolean userCreatedInTigase) {
        this.userCreatedInTigase = userCreatedInTigase;
    }

    @Override
    public String toString() {
        return "HkXmppUser{" + "id=" + id + ", userName=" + userName + ", password=" + password + ", type=" + type + ", emailAddress=" + emailAddress + ", createdBy=" + createdBy + ", createdOn=" + createdOn + ", modifiedBy=" + modifiedBy + ", modifiedOn=" + modifiedOn + ", expiredOn=" + expiredOn + ", isActive=" + isActive + ", isArchive=" + isArchive + ", status=" + status + ", inactiveReason=" + inactiveReason + ", lastLoginOn=" + lastLoginOn + ", franchise=" + franchise + '}';
    }

}
