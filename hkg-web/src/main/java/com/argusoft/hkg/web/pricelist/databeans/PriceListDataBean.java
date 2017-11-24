package com.argusoft.hkg.web.pricelist.databeans;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * @author rajkumar
 */
public class PriceListDataBean {

    private Long id;
    private Map<Integer, Object[]> data;
    private String fileName;
    private Date uploadedOn;
    private String displayName;
    private List<PriceListDataBean> children;

    public Map<Integer, Object[]> getData() {
        return data;
    }

    public void setData(Map<Integer, Object[]> data) {
        this.data = data;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getUploadedOn() {
        return uploadedOn;
    }

    public void setUploadedOn(Date uploadedOn) {
        this.uploadedOn = uploadedOn;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public List<PriceListDataBean> getChildren() {
        return children;
    }

    public void setChildren(List<PriceListDataBean> children) {
        this.children = children;
    }

    @Override
    public String toString() {
        return "PriceListDataBean{" + "id=" + id + ", data=" + data + ", fileName=" + fileName + ", uploadedOn=" + uploadedOn + ", displayName=" + displayName + ", children=" + children + '}';
    }

}
