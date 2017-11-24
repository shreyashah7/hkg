/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.sync.xmpp.util;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author shruti
 */
public abstract class SyncTransformerAdapter implements SyncTransformerInterface {
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(SyncTransformerAdapter.class);
    public static final String FRANCHISE = "FranchiseId";

    @Override
    public final boolean isUpdatable(Date currentModifiedOndate, Date newModifiedOnDate) {
        if (currentModifiedOndate != null && newModifiedOnDate != null) {
            int compareResult = currentModifiedOndate.compareTo(newModifiedOnDate);
            if (compareResult > 0) {
                return false;
            }
        } else {
            LOGGER.warn("Modified on should not be null. This can create severe data integrety issues. current modifiedon date is: " + currentModifiedOndate + ", new modifiedon date is" + newModifiedOnDate);
        }
        return true;
    }

    @Override
    public int getSyncTransferType() {
        return SyncTransferType.NONE;
    }

    @Override
    public boolean isStaticDocumentType() {
        return false;
    }

    public Object convertEntityToDocument(Object entityObject, Class<?> documentClass, Object toObj) {
        Map<String, String> fieldMap = HkSyncConstantUtil.ENTITY_FIELD_MAP.get(entityObject.getClass());
        Class entityClass = entityObject.getClass();
        Field entityField = null;
        Field documentField = null;
        if (fieldMap != null) {
            try {
                Object documentObject = (toObj == null) ? documentClass.newInstance() : toObj;
                for (Map.Entry<String, String> entrySet : fieldMap.entrySet()) {
                    try {
                        entityField = entityClass.getDeclaredField(entrySet.getKey());
                        documentField = documentClass.getDeclaredField(entrySet.getValue());
                        entityField.setAccessible(true);
                        documentField.setAccessible(true);
                        Class<?> type = documentField.getType();
//                        if (type.isPrimitive()) {
//                            if (type.equals(int.class) || type.equals(long.class)) {
//                                documentField.set(documentObject, BigInteger.valueOf((long) entityField.get(entityObject)));
//                            } else if (type.equals(float.class) || type.equals(double.class)) {
//                                documentField.set(documentObject, BigDecimal.valueOf((double) entityField.get(entityObject)));
//                            }
//                        } else if (documentField.getType().equals(Long.class)) {
//                            documentField.set(documentObject, BigInteger.valueOf((Long) entityField.get(entityObject)));
//                        }
                        documentField.set(documentObject, entityField.get(entityObject));
                    } catch (NoSuchFieldException | SecurityException ex) {
                        Logger.getLogger(SyncTransformerAdapter.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                return documentObject;
            } catch (InstantiationException | IllegalAccessException ex) {
                Logger.getLogger(SyncTransformerAdapter.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        return null;
    }
    @Override
    public Map<String, List<String>> getQueryParameters() {
        return null;
    }

    @Override
    public int getSyncTransferType(Class<?> class1) {
        return SyncTransferType.ONE_TO_MANY;
    }

    @Override
    public Object convertEntityToDocument(Object entityObject) {
        return null;
    }

    @Override
    public Map<String, Object> getidMap() {
        return null;
    }
}
