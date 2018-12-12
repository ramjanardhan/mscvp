package com.mss.ediscv.util;
/*
 * The WildCardSql class is to search the data and appending wild cards 
 */

public class WildCardSql {

    public static String getWildCardSql(String varName, String varVal) {
        if (varVal.indexOf("*") == -1) {
            return " AND lcase(" + varName + ")=lcase('" + varVal + "') ";
        } else {
            varVal = varVal.replace("*", "%");
            return " AND lcase(" + varName + ") = lcase('" + varVal + "') ";
        }
    }

    public static String getWildCardSql1(String varName, String varVal) {
        if (varVal.indexOf("*") == -1) {
            return " AND lcase(" + varName + ") like lcase('%" + varVal + "%') ";
        } else {
            varVal = varVal.replace("*", "%");
            return " AND lcase(" + varName + ") like lcase('" + varVal + "') ";
        }
    }

    public static String getWildCardSqlIN(String varName, String varVal) {
        if (varVal.indexOf("*") == -1) {
            return " AND " + varName + " IN (" + varVal + ") ";
        } else {
            varVal = varVal.replace("*", "%");
            return " AND " + varName + " IN (" + varVal + ") ";
        }
    }
}