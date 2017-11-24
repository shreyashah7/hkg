package com.argusoft.hkg.web.util;

import java.util.ResourceBundle;

/**
 *
 * @author shruti
 */
public class PackageUtil {

    public static Boolean isSqlDatabase = Boolean.FALSE;
    public static Boolean isNoSqlDatabase = Boolean.FALSE;

    static {

        ResourceBundle rbJdbc = ResourceBundle.getBundle("jdbc");
        if (rbJdbc.getString("use.sql.database").equalsIgnoreCase("TRUE")) {

            isSqlDatabase = Boolean.TRUE;

        }

        if (rbJdbc.getString("use.nosql.database").equalsIgnoreCase("TRUE")) {
            isNoSqlDatabase = Boolean.TRUE;
        }

    }

    public static Boolean isIsSqlDatabase() {
        return isSqlDatabase;
    }

    public static void setIsSqlDatabase(Boolean isSqlDatabase) {
        PackageUtil.isSqlDatabase = isSqlDatabase;
    }

    public static Boolean isIsNoSqlDatabase() {
        return isNoSqlDatabase;
    }

    public static void setIsNoSqlDatabase(Boolean isNoSqlDatabase) {
        PackageUtil.isNoSqlDatabase = isNoSqlDatabase;
    }

}
