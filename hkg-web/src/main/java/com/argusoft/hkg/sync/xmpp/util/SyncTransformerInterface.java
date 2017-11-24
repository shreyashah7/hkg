/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.sync.xmpp.util;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * @author shruti
 */
public interface SyncTransformerInterface {

    public boolean isUpdatable(Date currentModifiedOndate, Date newModifiedOnDate);

    /**
     * This method is used to save object. Note: Make sure you check modified on
     * date.
     *
     * @param object
     * @return
     */
    public void save(Object object) throws Exception;

    public Object convertEntityToDocument(Object entityObject);

    public int getSyncTransferType();
    public int getSyncTransferType(Class<?> class1);

    /**
     * returns if the document related to this transformer is of static type.
     * i.e there will be only one document in this collection and all
     * allterations to this collection would reflect on the same document. Note:
     * If this flag is true, it will be added after all documents of the same
     * transaction have been added. Note: If this flag is true, only last
     * updated copy would be sent.
     *
     * @return
     */
    public boolean isStaticDocumentType();

    /**
     * returns map of parameters that are used to take decision of recipient of
     * entity transformed by transformer. For eg. Map<String, String>
     * queryMap=new HashMap<>(); queryMap.put("FranchiseId",1);
     *
     * @return
     */
    public Map<String, List<String>> getQueryParameters();

    /**
     * For future use.
     *
     * @return
     */
    public Map<String, Object> getidMap();

}
