/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mss.ediscv.utilities;

import com.mss.ediscv.util.ConnectionProvider;
import com.mss.ediscv.util.ServiceLocatorException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.mss.ediscv.util.LoggerUtility;

/**
 *
 * @author miracle
 */
public class CertMonitorServiceImpl implements CertMonitorService {

    private static Logger logger = LogManager.getLogger(CertMonitorServiceImpl.class.getName());

    public List getCertMonitorData(String certType, String dateFrom, String dateTo) throws ServiceLocatorException {

        List<LinkedHashMap> al = new LinkedList<LinkedHashMap>();
        String cType = certType;
        Connection connection = null;
        ResultSet resultSet = null;
        String date = null;
        String dateto = null;
        if ((dateFrom != null) && !"".equalsIgnoreCase(dateFrom)) {
            date = dateFrom.replace("/", "-").substring(0, 10);
            dateto = dateTo.replace("/", "-").substring(0, 10);
        }
        try {
            Class.forName("oracle.jdbc.OracleDriver");
            connection = DriverManager.getConnection("jdbc:oracle:thin:@192.168.1.179:1521:orcl", "si_user", "SI_admin1");
            Statement statement = connection.createStatement();

            if ("TRUSTED".equalsIgnoreCase(cType)) {
                if ((dateFrom != null) && !"".equalsIgnoreCase(dateFrom)) {
                    resultSet = statement.executeQuery("SELECT NAME AS CERTIFICATE_NAME,NOT_BEFORE as VALID_FROM , NOT_AFTER as VALID_TILL , (to_date (NOT_AFTER,'dd-MM-yyyy') - to_date(SYSDATE,'dd-MM-yyyy')) AS DAYS FROM TRUSTED_CERT_INFO WHERE NOT_BEFORE > to_date('" + date + "','mm-dd-yyyy') AND NOT_AFTER < to_date('" + dateto + "','mm-dd-yyyy') ORDER BY DAYS ");
                } else {
                    resultSet = statement.executeQuery("SELECT NAME AS CERTIFICATE_NAME,NOT_BEFORE as VALID_FROM , NOT_AFTER as VALID_TILL , (to_date (NOT_AFTER,'dd-MM-yyyy') - to_date(SYSDATE,'dd-MM-yyyy')) AS DAYS FROM TRUSTED_CERT_INFO ORDER BY DAYS ");
                }
            } else if ("CA".equalsIgnoreCase(cType)) {
                if ((dateFrom != null) && !"".equalsIgnoreCase(dateFrom)) {
                    resultSet = statement.executeQuery("SELECT NAME AS CERTIFICATE_NAME,NOT_BEFORE as VALID_FROM , NOT_AFTER as VALID_TILL , (to_date (NOT_AFTER,'dd-MM-yyyy') - to_date(SYSDATE,'dd-MM-yyyy')) AS DAYS FROM CA_CERT_INFO WHERE NOT_BEFORE > to_date('" + date + "','mm-dd-yyyy') AND NOT_AFTER < to_date('" + dateto + "','mm-dd-yyyy')  ORDER BY DAYS");
                } else {
                    resultSet = statement.executeQuery("SELECT NAME AS CERTIFICATE_NAME,NOT_BEFORE as VALID_FROM , NOT_AFTER as VALID_TILL , (to_date (NOT_AFTER,'dd-MM-yyyy') - to_date(SYSDATE,'dd-MM-yyyy')) AS DAYS FROM CA_CERT_INFO   ORDER BY DAYS");
                }
            } else if ("SYSTEM".equalsIgnoreCase(cType)) {
                if ((dateFrom != null) && !"".equalsIgnoreCase(dateFrom)) {
                    resultSet = statement.executeQuery("SELECT NAME AS CERTIFICATE_NAME,NOT_BEFORE as VALID_FROM , NOT_AFTER as VALID_TILL ,(to_date (NOT_AFTER,'dd-MM-yyyy') - to_date(SYSDATE,'dd-MM-yyyy'))  AS DAYS FROM CERTS_AND_PRI_KEY WHERE NOT_BEFORE > to_date('" + date + "','mm-dd-yyyy') AND NOT_AFTER < to_date('" + dateto + "','mm-dd-yyyy')   ORDER BY DAYS");
                } else {
                    resultSet = statement.executeQuery("SELECT NAME AS CERTIFICATE_NAME,NOT_BEFORE as VALID_FROM , NOT_AFTER as VALID_TILL ,(to_date (NOT_AFTER,'dd-MM-yyyy') - to_date(SYSDATE,'dd-MM-yyyy'))  AS DAYS FROM CERTS_AND_PRI_KEY   ORDER BY DAYS");
                }
            }
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            int columnCount = resultSetMetaData.getColumnCount();
            LinkedHashMap linkedHashMap;

            while (resultSet.next()) {
                linkedHashMap = new LinkedHashMap();
                for (int i = 1; i <= columnCount; i++) {
                    linkedHashMap.put(resultSetMetaData.getColumnName(i), resultSet.getObject(i));
                }
                al.add(linkedHashMap);
            }

        } catch (SQLException sqlexception) {
            LoggerUtility.log(logger, "sqlException occurred in getCertMonitorData method:: " + sqlexception.getMessage(), Level.ERROR, sqlexception.getCause());
        } catch (ClassNotFoundException classNotFoundException) {
            LoggerUtility.log(logger, "Exception occurred in getCertMonitorData method:: " + classNotFoundException.getMessage(), Level.ERROR, classNotFoundException.getCause());
        }
        return al;
    }

    @Override
    public List doCodeListItems(String selectedName) throws ServiceLocatorException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String doCodeListItemsQuery = null;
        connection = ConnectionProvider.getInstance().getOracleConnection();
        List codeList = new ArrayList();
        CodeListBean codeListBean = null;
        try {
            doCodeListItemsQuery = "SELECT * FROM CODELIST_XREF_ITEM WHERE LIST_NAME='" + selectedName + "' AND LIST_VERSION=(SELECT DEFAULT_VERSION from CODELIST_XREF_VERS where LIST_NAME='" + selectedName + "' )";
            preparedStatement = connection.prepareStatement(doCodeListItemsQuery);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                codeListBean = new CodeListBean();

                codeListBean.setListName(resultSet.getString("LIST_NAME"));
                codeListBean.setSender_id(resultSet.getString("SENDER_ID"));
                codeListBean.setReceiver_id(resultSet.getString("RECEIVER_ID"));
                codeListBean.setList_version(resultSet.getString("LIST_VERSION"));
                codeListBean.setSender_item(resultSet.getString("SENDER_ITEM"));
                codeListBean.setReceiver_item(resultSet.getString("RECEIVER_ITEM"));
                if (resultSet.getString("TEXT1") != null && !"".equalsIgnoreCase(resultSet.getString("TEXT1"))) {
                    codeListBean.setText1(resultSet.getString("TEXT1"));
                } else {
                    codeListBean.setText1("");
                }
                if (resultSet.getString("TEXT2") != null && !"".equalsIgnoreCase(resultSet.getString("TEXT2"))) {
                    codeListBean.setText2(resultSet.getString("TEXT2"));
                } else {
                    codeListBean.setText2("");
                }
                if (resultSet.getString("TEXT3") != null && !"".equalsIgnoreCase(resultSet.getString("TEXT3"))) {
                    codeListBean.setText3(resultSet.getString("TEXT3"));
                } else {
                    codeListBean.setText3("");
                }
                if (!"".equalsIgnoreCase(resultSet.getString("TEXT4")) && resultSet.getString("TEXT4") != null) {
                    codeListBean.setText4(resultSet.getString("TEXT4"));
                } else {
                    codeListBean.setText4("");
                }
                codeListBean.setDescription(resultSet.getString("DESCRIPTION"));
                if (!"".equalsIgnoreCase(resultSet.getString("TEXT5")) && resultSet.getString("TEXT5") != null) {
                    codeListBean.setText5(resultSet.getString("TEXT5"));
                } else {
                    codeListBean.setText5("");
                }
                if (!"".equalsIgnoreCase(resultSet.getString("TEXT6")) && resultSet.getString("TEXT6") != null) {
                    codeListBean.setText6(resultSet.getString("TEXT6"));
                } else {
                    codeListBean.setText6("");
                }
                if (!"".equalsIgnoreCase(resultSet.getString("TEXT7")) && resultSet.getString("TEXT7") != null) {
                    codeListBean.setText7(resultSet.getString("TEXT7"));
                } else {
                    codeListBean.setText7("");
                }
                if (!"".equalsIgnoreCase(resultSet.getString("TEXT8")) && resultSet.getString("TEXT8") != null) {
                    codeListBean.setText8(resultSet.getString("TEXT8"));
                } else {
                    codeListBean.setText8("");
                }
                if (!"".equalsIgnoreCase(resultSet.getString("TEXT9")) && resultSet.getString("TEXT9") != null) {
                    codeListBean.setText9(resultSet.getString("TEXT9"));
                } else {
                    codeListBean.setText9("");
                }
                codeList.add(codeListBean);
            }
        } catch (SQLException sqlexception) {
            LoggerUtility.log(logger, "sqlException occurred in doCodeListItems method:: " + sqlexception.getMessage(), Level.ERROR, sqlexception.getCause());
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                    resultSet = null;
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                    preparedStatement = null;
                }
                if (connection != null) {
                    connection.close();
                    connection = null;
                }
            } catch (SQLException sqlexception) {
                LoggerUtility.log(logger, "sqlException occurred in doCodeListItems method:: " + sqlexception.getMessage(), Level.ERROR, sqlexception.getCause());
            }
        }
        return codeList;
    }

    @Override
    public List getCodeListNames(String selectedName) throws ServiceLocatorException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String codeListNamesQuery = null;
        connection = ConnectionProvider.getInstance().getOracleConnection();
        List listNameMap = new ArrayList();
        try {
            codeListNamesQuery = "SELECT  DISTINCT(LIST_NAME) FROM CODELIST_XREF_ITEM WHERE UPPER(LIST_NAME) LIKE '%" + selectedName.trim().toUpperCase() + "%'";
            preparedStatement = connection.prepareStatement(codeListNamesQuery);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                listNameMap.add(resultSet.getString("LIST_NAME"));
            }
        } catch (SQLException sqlexception) {
            LoggerUtility.log(logger, "sqlException occurred in getCodeListNames method:: " + sqlexception.getMessage(), Level.ERROR, sqlexception.getCause());
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                    resultSet = null;
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                    preparedStatement = null;
                }
                if (connection != null) {
                    connection.close();
                    connection = null;
                }
            } catch (SQLException sqlexception) {
                LoggerUtility.log(logger, "sqlException occurred in getCodeListNames method:: " + sqlexception.getMessage(), Level.ERROR, sqlexception.getCause());
            }
        }
        return listNameMap;
    }

    public List getProcessFlows(CertMonitorAction certMonitorAction) throws ServiceLocatorException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String getProcessFlowsQuery = null;
        ArrayList flowList = new ArrayList();
        try {
            connection = ConnectionProvider.getInstance().getConnection();
            getProcessFlowsQuery = "SELECT PROCESS_ID,DIRECTION,PROCESS_NAME,DESCRIPTION,TP_NAME,TP_ID,TP_SENDER_ID,TP_RECEIVER_ID,TRANSACTION_TYPE,SOURCE_MAIL_BOX,IS_ACTIVE,LOOKUP_ALIAS FROM PROCESS" + " WHERE 1=1 ";
            if (certMonitorAction.getDocTpName() != null && !"".equalsIgnoreCase(certMonitorAction.getDocTpName())) {
                getProcessFlowsQuery = getProcessFlowsQuery + " AND lower(TP_NAME) LIKE lower('" + certMonitorAction.getDocTpName() + "%') ";
            }
            if (certMonitorAction.getDocTpId() != null && !"".equalsIgnoreCase(certMonitorAction.getDocTpId())) {
                getProcessFlowsQuery = getProcessFlowsQuery + " AND lower(TP_ID) LIKE lower('" + certMonitorAction.getDocTpId() + "%') ";
            }
            if (certMonitorAction.getDocTpReceiverId() != null && !"".equalsIgnoreCase(certMonitorAction.getDocTpReceiverId())) {
                getProcessFlowsQuery = getProcessFlowsQuery + " AND lower(TP_RECEIVER_ID) LIKE lower('" + certMonitorAction.getDocTpReceiverId() + "%')";
            }
            if (certMonitorAction.getDocTpSenderId() != null && !"".equalsIgnoreCase(certMonitorAction.getDocTpSenderId())) {
                getProcessFlowsQuery = getProcessFlowsQuery + " AND lower(TP_SENDER_ID) LIKE lower('" + certMonitorAction.getDocTpSenderId() + "%') ";
            }
            if (certMonitorAction.getDocTransactionType() != null && !"".equalsIgnoreCase(certMonitorAction.getDocTransactionType())) {
                getProcessFlowsQuery = getProcessFlowsQuery + " AND lower(TRANSACTION_TYPE) LIKE lower('" + certMonitorAction.getDocTransactionType() + "%') ";
            }
            if (certMonitorAction.getDocStatus() != null && !"".equalsIgnoreCase(certMonitorAction.getDocStatus())) {
                getProcessFlowsQuery = getProcessFlowsQuery + " AND lower(IS_ACTIVE) LIKE lower('" + certMonitorAction.getDocStatus() + "%') ";
            }
            preparedStatement = connection.prepareStatement(getProcessFlowsQuery);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Map flowMap = new HashMap();
                flowMap.put("PROCESS_ID", resultSet.getString("PROCESS_ID"));
                flowMap.put("DIRECTION", resultSet.getString("DIRECTION"));
                flowMap.put("PROCESS_NAME", resultSet.getString("PROCESS_NAME"));
                flowMap.put("DESCRIPTION", resultSet.getString("DESCRIPTION"));
                flowMap.put("TP_NAME", resultSet.getString("TP_NAME"));
                flowMap.put("TP_ID", resultSet.getString("TP_ID"));
                flowMap.put("TP_SENDER_ID", resultSet.getString("TP_SENDER_ID"));
                flowMap.put("TP_RECEIVER_ID", resultSet.getString("TP_RECEIVER_ID"));
                flowMap.put("TRANSACTION_TYPE", resultSet.getString("TRANSACTION_TYPE"));
                flowMap.put("SOURCE_MAIL_BOX", resultSet.getString("SOURCE_MAIL_BOX"));
                flowMap.put("IS_ACTIVE", resultSet.getString("IS_ACTIVE"));
                flowMap.put("LOOKUP_ALIAS", resultSet.getString("LOOKUP_ALIAS"));
                flowList.add(flowMap);
            }
        } catch (SQLException sqlexception) {
            LoggerUtility.log(logger, "sqlException occurred in getProcessFlows method:: " + sqlexception.getMessage(), Level.ERROR, sqlexception.getCause());
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                    resultSet = null;
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                    preparedStatement = null;
                }
                if (connection != null) {
                    connection.close();
                    connection = null;
                }
            } catch (SQLException sqlexception) {

                LoggerUtility.log(logger, "sqlException occurred in getProcessFlows method:: " + sqlexception.getMessage(), Level.ERROR, sqlexception.getCause());
            }
        }
        return flowList;
    }
}
