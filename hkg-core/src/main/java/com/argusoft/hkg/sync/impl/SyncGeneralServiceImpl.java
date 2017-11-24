/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.sync.impl;

import com.argusoft.generic.database.common.CommonDAO;
import com.argusoft.hkg.common.functionutil.HkSystemFunctionUtil;
import com.argusoft.hkg.sync.SyncGeneralService;
import com.argusoft.sync.center.model.MetadataDocument;
import com.googlecode.genericdao.search.Search;
import java.util.Map;
import java.util.logging.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author shruti
 */
@Service
@Transactional
public class SyncGeneralServiceImpl implements SyncGeneralService {
    @Autowired
    private CommonDAO commonDAO;
    @Override
    public Object retrueveObjectsFromMetadata(MetadataDocument metadataDocument) {
        Object searchUnique = null;
        try {
            Search search1 = new Search(Class.forName(metadataDocument.getClassName()));
            for (Map.Entry<String, Object> entrySet : metadataDocument.getIdMap().entrySet()) {
                search1.addFilterEqual(HkSystemFunctionUtil.decodeMapKeyWithDot(entrySet.getKey()), entrySet.getValue());
            }
            searchUnique = commonDAO.searchUnique(search1);
            System.out.println("" + searchUnique);
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(SyncGeneralServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return searchUnique;
    }

}
