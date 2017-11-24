/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.sync.listner;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import org.springframework.util.CollectionUtils;

/**
 *
 * @author brijesh
 */
public class SyncTransactionLog {

    private static final Map<Long, List<SyncRecordedEntity>> perTransactionRecordedEntities = new Hashtable<>();

    public static Map<Long, List<SyncRecordedEntity>> getPerTransactionRecordedEntities() {
        return perTransactionRecordedEntities;
    }

    public static void initializePerTransactionRecordedEntities(Long transactionId) {
        if (CollectionUtils.isEmpty(perTransactionRecordedEntities.get(transactionId))) {
            perTransactionRecordedEntities.put(transactionId, new ArrayList<>());
        }
    }

    public static boolean isSyncInstance(Object object) {
        return true;
    }

    public static void flushMap(Long transactionId) {
        getPerTransactionRecordedEntities().remove(transactionId);
    }

    public static boolean isEmptyHkRecordedEntityList(Long transactionId) {
        if (CollectionUtils.isEmpty(getPerTransactionRecordedEntities().get(transactionId))) {
            return true;
        }
        return false;
    }
}
