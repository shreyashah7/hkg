/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.util;

import java.util.List;

/**
 *
 * @author mansi
 */
public class TreeViewDataBean {

    private Object id;
    private String text;
    private String displayName;
    private List<TreeViewDataBean> items;
    private List<TreeViewDataBean> children;
    private boolean isChecked;
    private Integer categoryCount;

    public Object getId() {
        return id;
    }

    public void setId(Object id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<TreeViewDataBean> getItems() {
        return items;
    }

    public void setItems(List<TreeViewDataBean> items) {
        this.items = items;
    }

    public boolean isIsChecked() {
        return isChecked;
    }

    public void setIsChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public List<TreeViewDataBean> getChildren() {
        return children;
    }

    public void setChildren(List<TreeViewDataBean> children) {
        this.children = children;
    }

    public Integer getCategoryCount() {
        return categoryCount;
    }

    public void setCategoryCount(Integer categoryCount) {
        this.categoryCount = categoryCount;
    }

    @Override
    public String toString() {
        return "TreeViewDataBean{" + "id=" + id + ", text=" + text + ", displayName=" + displayName + ", items=" + items + ", children=" + children + ", isChecked=" + isChecked + ", categoryCount=" + categoryCount + '}';
    }

}
