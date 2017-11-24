/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.eventmanagement.databeans;

import com.argusoft.hkg.web.assets.databeans.CategoryDataBean;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.Length;

/**
 *
 * @author kuldeep
 */
public class EventDataBean {

    private Long id;
    private Long category;
    @NotNull(message = "Event title not entered")
    @Length(max = 100, message = "Event Title length exceeded 100")
    private String eventTitle;
    private String labelColor;
    private String contentColor;
    @Length(max = 500, message = "Length of Description exceeded 500")
    private String description;
    @Length(max = 500, message = "Length of Event vanue exceeded 500")
    private String address;
    @NotNull(message = "Event date not entered")
    private Date fromDate;
    private Date toDate;
    private Long createdBy;
    private Date strtTime;
    private Date endTime;
    @NotNull(message = "Published on date not entered")
    private Date publishedOn;
    private String registrationType;
    private Date registrationLastDate;
    private String status;
    private String registrationFormName;
    private Long registrationFormEventId;
    private Integer registrationCount;
    private Integer notAttendingCount;
    private Integer adultCount;
    private Integer recipientAdultCount;
    private Integer recipientChildCount;
    private Integer childCount;
    private Integer guestCount;
    private String folderName;
    private Long franchise;
    private Integer employeeCount;
    private String bannerImageName;
    private String invitationTemplateName;
    private String invitationTemplatePath;
    private CategoryDataBean categoryDataBean;
    private List<EventRegistrationFieldsDataBean> registrationFieldsDataBean;
    private List<EventRecipientDataBean> eventRecipientDataBeanList;
    private String templateHtml;
    private Map<String, Object> eventCustom;// for custom field data
    private Map<String, String> eventDbType; //for get filed wise dbtype
    private Map<String, Object> regCustom;
    private Map<String, String> regDbType;
    private Map<String, Object> invitationCustom;
    private Map<String, String> invitationDbType;
    private List<GuestDataBean> guests;
    private Boolean registrationStatus;
    private String categoryName;
    private boolean registrationFormUpdated;
    private String reason;
    private boolean isAttending;
    private List<String> fileList;
    private Integer pendingCount;
    private Boolean enableRegister;
    private String employeeName;
    private String employeeAddress;
    private String employeeEmail;
    private String employeePhoneNo;
    private String token;
    private Integer userAdultCount;
    private Integer userChildCount;

    public boolean getRegistrationFormUpdated() {
        return registrationFormUpdated;
    }

    public void setRegistrationFormUpdated(boolean registrationFormUpdated) {
        this.registrationFormUpdated = registrationFormUpdated;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
    public Integer getRecipientAdultCount() {
        return recipientAdultCount;
    }

    public void setRecipientAdultCount(Integer recipientAdultCount) {
        this.recipientAdultCount = recipientAdultCount;
    }

    public Integer getUserAdultCount() {
        return userAdultCount;
    }

    public void setUserAdultCount(Integer userAdultCount) {
        this.userAdultCount = userAdultCount;
    }

    public Integer getUserChildCount() {
        return userChildCount;
    }

    public void setUserChildCount(Integer userChildCount) {
        this.userChildCount = userChildCount;
    }

    public Integer getRecipientChildCount() {
        return recipientChildCount;
    }

    public void setRecipientChildCount(Integer recipientChildCount) {
        this.recipientChildCount = recipientChildCount;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getEmployeeAddress() {
        return employeeAddress;
    }

    public void setEmployeeAddress(String employeeAddress) {
        this.employeeAddress = employeeAddress;
    }

    public String getEmployeeEmail() {
        return employeeEmail;
    }

    public void setEmployeeEmail(String employeeEmail) {
        this.employeeEmail = employeeEmail;
    }

    public String getEmployeePhoneNo() {
        return employeePhoneNo;
    }

    public void setEmployeePhoneNo(String employeePhoneNo) {
        this.employeePhoneNo = employeePhoneNo;
    }

    public List<String> getFileList() {
        return fileList;
    }

    public void setFileList(List<String> fileList) {
        this.fileList = fileList;
    }

    public Integer getPendingCount() {
        return pendingCount;
    }

    public Long getFranchise() {
        return franchise;
    }

    public void setFranchise(Long franchise) {
        this.franchise = franchise;
    }

    public Boolean getEnableRegister() {
        return enableRegister;
    }

    public void setEnableRegister(Boolean enableRegister) {
        this.enableRegister = enableRegister;
    }

    public void setPendingCount(Integer pendingCount) {
        this.pendingCount = pendingCount;
    }

    public Long getRegistrationFormEventId() {
        return registrationFormEventId;
    }

    public void setRegistrationFormEventId(Long registrationFormEventId) {
        this.registrationFormEventId = registrationFormEventId;
    }

    public Boolean isRegistrationStatus() {
        return registrationStatus;
    }

    public void setRegistrationStatus(Boolean registrationStatus) {
        this.registrationStatus = registrationStatus;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public List<GuestDataBean> getGuests() {
        return guests;
    }

    public void setGuests(List<GuestDataBean> guests) {
        this.guests = guests;
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

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
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

    public Date getRegistrationLastDate() {
        return registrationLastDate;
    }

    public void setRegistrationLastDate(Date registrationLastDate) {
        this.registrationLastDate = registrationLastDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getBannerImageName() {
        return bannerImageName;
    }

    public void setBannerImageName(String bannerImageName) {
        this.bannerImageName = bannerImageName;
    }

    public String getInvitationTemplateName() {
        return invitationTemplateName;
    }

    public void setInvitationTemplateName(String invitationTemplateName) {
        this.invitationTemplateName = invitationTemplateName;
    }

    public Long getCategory() {
        return category;
    }

    public void setCategory(Long category) {
        this.category = category;
    }

    public List<EventRecipientDataBean> getEventRecipientDataBeanList() {
        return eventRecipientDataBeanList;
    }

    public void setEventRecipientDataBeanList(List<EventRecipientDataBean> eventRecipientDataBeanList) {
        this.eventRecipientDataBeanList = eventRecipientDataBeanList;
    }

    public CategoryDataBean getCategoryDataBean() {
        return categoryDataBean;
    }

    public void setCategoryDataBean(CategoryDataBean categoryDataBean) {
        this.categoryDataBean = categoryDataBean;
    }

    public Integer getEmployeeCount() {
        return employeeCount;
    }

    public void setEmployeeCount(Integer employeeCount) {
        this.employeeCount = employeeCount;
    }

    public String getTemplateHtml() {
        return templateHtml;
    }

    public void setTemplateHtml(String templateHtml) {
        this.templateHtml = templateHtml;
    }

    public String getInvitationTemplatePath() {
        return invitationTemplatePath;
    }

    public void setInvitationTemplatePath(String invitationTemplatePath) {
        this.invitationTemplatePath = invitationTemplatePath;
    }

    public List<EventRegistrationFieldsDataBean> getRegistrationFieldsDataBean() {
        return registrationFieldsDataBean;
    }

    public void setRegistrationFieldsDataBean(List<EventRegistrationFieldsDataBean> registrationFieldsDataBean) {
        this.registrationFieldsDataBean = registrationFieldsDataBean;
    }

    public Map<String, Object> getEventCustom() {
        return eventCustom;
    }

    public void setEventCustom(Map<String, Object> eventCustom) {
        this.eventCustom = eventCustom;
    }

    public Map<String, String> getEventDbType() {
        return eventDbType;
    }

    public void setEventDbType(Map<String, String> eventDbType) {
        this.eventDbType = eventDbType;
    }

    public Map<String, Object> getRegCustom() {
        return regCustom;
    }

    public void setRegCustom(Map<String, Object> regCustom) {
        this.regCustom = regCustom;
    }

    public Map<String, String> getRegDbType() {
        return regDbType;
    }

    public void setRegDbType(Map<String, String> regDbType) {
        this.regDbType = regDbType;
    }

    public Map<String, Object> getInvitationCustom() {
        return invitationCustom;
    }

    public void setInvitationCustom(Map<String, Object> invitationCustom) {
        this.invitationCustom = invitationCustom;
    }

    public Map<String, String> getInvitationDbType() {
        return invitationDbType;
    }

    public void setInvitationDbType(Map<String, String> invitationDbType) {
        this.invitationDbType = invitationDbType;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public boolean isIsAttending() {
        return isAttending;
    }

    public void setIsAttending(boolean isAttending) {
        this.isAttending = isAttending;
    }

    @Override
    public String toString() {
        return "EventDataBean{" + "id=" + id + ", category=" + category + ", eventTitle=" + eventTitle + ", labelColor=" + labelColor + ", contentColor=" + contentColor + ", description=" + description + ", address=" + address + ", fromDate=" + fromDate + ", toDate=" + toDate + ", strtTime=" + strtTime + ", endTime=" + endTime + ", publishedOn=" + publishedOn + ", registrationType=" + registrationType + ", registrationLastDate=" + registrationLastDate + ", status=" + status + ", registrationFormName=" + registrationFormName + ", registrationCount=" + registrationCount + ", notAttendingCount=" + notAttendingCount + ", adultCount=" + adultCount + ", childCount=" + childCount + ", guestCount=" + guestCount + ", employeeCount=" + employeeCount + ", bannerImageName=" + bannerImageName + ", invitationTemplateName=" + invitationTemplateName + ", invitationTemplatePath=" + invitationTemplatePath + ", categoryDataBean=" + categoryDataBean + ", registrationFieldsDataBean=" + registrationFieldsDataBean + ", eventRecipientDataBeanList=" + eventRecipientDataBeanList + ", templateHtml=" + templateHtml + ", eventCustom=" + eventCustom + ", eventDbType=" + eventDbType + ", regCustom=" + regCustom + ", regDbType=" + regDbType + ", invitationCustom=" + invitationCustom + ", invitationDbType=" + invitationDbType + ", guests=" + guests + ", registrationStatus=" + registrationStatus + ", registrationFormUpdated=" + registrationFormUpdated + ", reason=" + reason + ", isAttending=" + isAttending + '}';
    }

}
