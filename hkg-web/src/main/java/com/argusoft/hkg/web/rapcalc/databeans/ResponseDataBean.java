/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.rapcalc.databeans;

/**
 *
 * @author shruti
 */
public class ResponseDataBean {
    private String queryName;
    private String data;
    private boolean isLastChunk;
    /**
     * @return the queryName
     */
    public String getQueryName() {
        return queryName;
    }

    /**
     * @param queryName the queryName to set
     */
    public void setQueryName(String queryName) {
        this.queryName = queryName;
    }

    /**
     * @return the data
     */
    public String getData() {
        return data;
    }

    /**
     * @param data the data to set
     */
    public void setData(String data) {
        this.data = data;
    }

    /**
     * @return the isLastChunk
     */
    public boolean getIsLastChunk() {
        return isLastChunk;
    }

    /**
     * @param isLastChunk the isLastChunk to set
     */
    public void setIsLastChunk(boolean isLastChunk) {
        this.isLastChunk = isLastChunk;
    }

}
