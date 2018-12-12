/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mss.ediscv.purge;

import com.mss.ediscv.util.ConnectionProvider;
import com.mss.ediscv.util.DateUtility;
import com.mss.ediscv.util.ServiceLocatorException;
import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import com.mss.ediscv.util.LoggerUtility;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author miracle
 */
public class PurgeServiceImpl implements PurgeService {

    private static Logger logger = LogManager.getLogger(PurgeServiceImpl.class.getName());
    String responseString = null;
    Calendar cal = new GregorianCalendar();
    java.util.Date now = cal.getTime();
    long time = now.getTime();
    java.sql.Date date = new java.sql.Date(time);
    boolean updateArchHistQueryFlag = false;

    public String purgeProcess(PurgeAction purgeAction, String username, String defaultFlowName) throws ServiceLocatorException {

        String dayCount = purgeAction.getDayCount();
        String transType = purgeAction.getTransType();
        String comments = purgeAction.getComments();
        String user = username;
        String flag = "Purge";
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Map deleteMap = new TreeMap();
        StringBuilder purgeProcessQuery = new StringBuilder();
        try {
            connection = ConnectionProvider.getInstance().getConnection();
            if ("All".equalsIgnoreCase(transType)) {
                purgeProcessQuery.append("select FILE_ID, Transaction_Type from ARCHIVE_FILES where DATE(DATE_TIME_RECEIVED) <  DATE(CURRENT TIMESTAMP - " + dayCount + " DAYS) ");
            } else {
                purgeProcessQuery.append("select FILE_ID, Transaction_Type from ARCHIVE_FILES where DATE(DATE_TIME_RECEIVED) <  DATE(CURRENT TIMESTAMP - " + dayCount + " DAYS) AND TRANSACTION_TYPE=" + transType + " ");
            }
            if (!transType.equals("-1") && !transType.equals("All")) {
                purgeProcessQuery.append(" AND Transaction_Type = '" + transType + "'");
            }
            preparedStatement = connection.prepareStatement(purgeProcessQuery.toString());
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                deleteMap.put(resultSet.getString("FILE_ID"), resultSet.getString("Transaction_Type"));
            }
            Set set = deleteMap.entrySet();
            Iterator i = set.iterator();
            while (i.hasNext()) {
                Map.Entry me = (Map.Entry) i.next();
                deleteReocords((String) me.getKey(), (String) me.getValue(), dayCount, user, comments, date, flag, defaultFlowName);
            }
            responseString = "<font color='green'>Purge Process Completed Successfully</font>";
        } catch (SQLException sqlException) {
            responseString = "<font color='red'>Please try again!</font>";
            LoggerUtility.log(logger, "SQLException occurred in purgeProcess method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
        } catch (Exception exception) {
            responseString = "<font color='red'>Please try again!</font>";
            LoggerUtility.log(logger, "Exception occurred in purgeProcess method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
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
            } catch (SQLException sqlException) {
                LoggerUtility.log(logger, "SQLException occurred in purgeProcess method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
            }
        }
        return responseString;
    }

    public void deleteReocords(String fileId, String transType, String dayCount, String user, String comments, java.sql.Date date, String flag, String defaultFlowName) throws ServiceLocatorException {
        Connection connection = null;
        Statement statement = null;
        String updateArchHistQuery = null;
        try {
            connection = ConnectionProvider.getInstance().getConnection();
            connection.setAutoCommit(false);
            statement = connection.createStatement();
            Timestamp dateTime = DateUtility.getInstance().getCurrentDB2Timestamp();
            String deleteArchFilesQuery = "DELETE FROM ARCHIVE_FILES WHERE File_ID='" + fileId + "'";
            if (!(updateArchHistQueryFlag)) {
                updateArchHistQuery = "insert into ARCHIVE_HISTORY(TRANSACTION_TYPE, DAYS_COUNT, USER, COMMENTS, DATE, FLAG) values ('" + transType + "','" + dayCount + "','" + user + "','" + comments + "','" + dateTime + "','" + flag + "')";
                statement.addBatch(updateArchHistQuery);
                updateArchHistQueryFlag = true;
            }
            String deleteArchtransQuery = "";
            if (transType.equals("850")) {
                deleteArchtransQuery = "DELETE FROM ARCHIVE_PO WHERE File_ID='" + fileId + "'";
            } else if (transType.equals("856")) {
                deleteArchtransQuery = "DELETE FROM ARCHIVE_ASN WHERE File_ID='" + fileId + "'";
            } else if (transType.equals("810")) {
                deleteArchtransQuery = "DELETE FROM ARCHIVE_INVOICE WHERE File_ID='" + fileId + "'";
            } else if (transType.equals("820")) {
                deleteArchtransQuery = "DELETE FROM ARCHIVE_PAYMENT WHERE File_ID='" + fileId + "'";
            } else if (transType.equals("846")) {
                deleteArchtransQuery = "DELETE FROM ARCHIVE_INVENTORY WHERE File_ID='" + fileId + "'";
            }

            statement.addBatch(deleteArchFilesQuery);
            statement.addBatch(deleteArchtransQuery);
            if (transType.equals("204")) {
                deleteArchtransQuery = "DELETE FROM ARCHIVE_TRANSPORT_LOADTENDER WHERE File_ID='" + fileId + "'";
            } else if (transType.equals("990")) {
                deleteArchtransQuery = "DELETE FROM ARCHIVE_TRANSPORT_LT_RESPONSE WHERE File_ID='" + fileId + "'";
            } else if (transType.equals("214")) {
                deleteArchtransQuery = "DELETE FROM ARCHIVE_TRANSPORT_SHIPMENT WHERE File_ID='" + fileId + "'";
            } else if (transType.equals("210")) {
                deleteArchtransQuery = "DELETE FROM ARCHIVE_TRANSPORT_INVOICE WHERE File_ID='" + fileId + "'";
            }

            statement.addBatch(deleteArchFilesQuery);
            statement.addBatch(deleteArchtransQuery);

            int[] count = statement.executeBatch();
            connection.commit();
        } catch (SQLException sqlException) {
            LoggerUtility.log(logger, "SQLException occurred in deleteReocords method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                    statement = null;
                }
                if (connection != null) {
                    connection.close();
                    connection = null;
                }
            } catch (SQLException sqlException) {
                LoggerUtility.log(logger, "finally SQLException occurred in deleteReocords method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
            }
        }
    }

    public String archiveProcess(PurgeAction purgeAction, String username, String flowName) throws ServiceLocatorException {

        String dayCount = purgeAction.getDayCount();
        String transType = purgeAction.getTransType();
        String comments = purgeAction.getComments();
        String user = username;
        String flag = "Archive";
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Map deleteMap = new TreeMap();
        List fileisList = new ArrayList();
        Map filesMap = new HashMap();
        StringBuilder archiveProcessquery = new StringBuilder();
        try {
            connection = ConnectionProvider.getInstance().getConnection();
            if ("All".equalsIgnoreCase(transType)) {
                archiveProcessquery.append("select FILE_ID, TRANSACTION_TYPE from FILES where DATE(DATE_TIME_RECEIVED) <  DATE(CURRENT TIMESTAMP - " + dayCount + " DAYS)");
            } else {
                archiveProcessquery.append("select FILE_ID from FILES where DATE(DATE_TIME_RECEIVED) <  DATE(CURRENT TIMESTAMP - " + dayCount + " DAYS) and TRANSACTION_TYPE=" + transType + " ");
            }
            preparedStatement = connection.prepareStatement(archiveProcessquery.toString());
            resultSet = preparedStatement.executeQuery();
            if ("All".equals(transType)) {
                while (resultSet.next()) {
                    filesMap.put(resultSet.getString("FILE_ID"), resultSet.getString("TRANSACTION_TYPE"));
                }
            } else {
                while (resultSet.next()) {
                    fileisList.add(resultSet.getString("FILE_ID"));
                }
            }
            if ("All".equals(transType)) {
                Iterator<Map.Entry<String, String>> it = filesMap.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<String, String> pair = it.next();
                    archiveReocords((String) pair.getKey().toString(), (String) pair.getValue().toString(), dayCount, user, comments, date, flag, flowName);
                }
            } else {
                for (int i = 0; i < fileisList.size(); i++) {
                    archiveReocords((String) fileisList.get(i).toString(), transType, dayCount, user, comments, date, flag, flowName);
                }
            }
            Set set = deleteMap.entrySet();
            Iterator i = set.iterator();
            while (i.hasNext()) {
                Map.Entry me = (Map.Entry) i.next();
            }
            responseString = "<font color='green'>Archive Process Completed Successfully</font>";
        } catch (SQLException sqlException) {
            responseString = "<font color='red'>Please try again!</font>";
            LoggerUtility.log(logger, "SQLException occurred in archiveProcess method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
        } catch (Exception exception) {
            responseString = "<font color='red'>Please try again!</font>";
            LoggerUtility.log(logger, "Exception occurred in archiveProcess method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
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
            } catch (SQLException sqlException) {
                LoggerUtility.log(logger, "SQLException occurred in archiveProcess method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
            }
        }
        return responseString;
    }

    public void archiveReocords(String fileId, String transType, String dayCount, String user, String comments, java.sql.Date date, String flag, String flowName) throws ServiceLocatorException {
        Connection connection = null;
        Statement statement = null;
        String insertArchFilesQuery = null;
        String deleteFilesQuery = null;
        String updateArchHistQuery = null;
        String insertArchTransQuery = null;
        String deleteArchTransQuery = null;
        try {
            connection = ConnectionProvider.getInstance().getConnection();
            connection.setAutoCommit(true);
            statement = connection.createStatement();
            Timestamp dateTime = DateUtility.getInstance().getCurrentDB2Timestamp();
            if (!(updateArchHistQueryFlag)) {
                insertArchFilesQuery = "insert into ARCHIVE_FILES select f.* from files f where DATE(DATE_TIME_RECEIVED) <  DATE(CURRENT TIMESTAMP - " + dayCount + " DAYS) and Transaction_Type= '" + transType + "' ";
                deleteFilesQuery = "DELETE FROM FILES where DATE(DATE_TIME_RECEIVED) <  DATE(CURRENT TIMESTAMP - " + dayCount + " DAYS) and Transaction_Type= '" + transType + "' ";
                updateArchHistQuery = "insert into ARCHIVE_HISTORY(TRANSACTION_TYPE, DAYS_COUNT, USER, COMMENTS, DATE, FLAG) values ('" + transType + "','" + dayCount + "','" + user + "','" + comments + "','" + dateTime + "','" + flag + "')";

                statement.addBatch(insertArchFilesQuery);
                statement.addBatch(updateArchHistQuery);

                updateArchHistQueryFlag = true;
            }
            if ((transType.equals("850")) || (transType.equals("855"))) {
                insertArchTransQuery = "INSERT INTO ARCHIVE_PO SELECT t.* FROM MSCVP.PO t where file_ID= '" + fileId + "' ";
                deleteArchTransQuery = "DELETE FROM PO WHERE File_ID='" + fileId + "'";
            } else if (transType.equals("856")) {
                insertArchTransQuery = "INSERT INTO ARCHIVE_ASN SELECT t.* FROM MSCVP.ASN t where file_ID= '" + fileId + "' ";
                deleteArchTransQuery = "DELETE FROM ASN WHERE File_ID='" + fileId + "'";
            } else if (transType.equals("810")) {
                insertArchTransQuery = "INSERT INTO ARCHIVE_INVOICE SELECT t.* FROM MSCVP.INVOICE t where file_ID= '" + fileId + "' ";
                deleteArchTransQuery = "DELETE FROM INVOICE WHERE File_ID='" + fileId + "'";
            } else if (transType.equals("820")) {
                insertArchTransQuery = "INSERT INTO ARCHIVE_PAYMENT SELECT t.* FROM MSCVP.PAYMENT t where file_ID= '" + fileId + "' ";
                deleteArchTransQuery = "DELETE FROM PAYMENT WHERE File_ID='" + fileId + "'";
            } else if (transType.equals("846")) {
                insertArchTransQuery = "INSERT INTO ARCHIVE_INVENTORY SELECT t.* FROM MSCVP.INVENTORY t where file_ID= '" + fileId + "' ";
                deleteArchTransQuery = "DELETE FROM INVENTORY WHERE File_ID='" + fileId + "'";
            }
            statement.addBatch(insertArchTransQuery);
            if (transType.equals("204")) {
                insertArchTransQuery = "INSERT INTO ARCHIVE_TRANSPORT_LOADTENDER SELECT t.* FROM MSCVP.TRANSPORT_LOADTENDER t where file_ID= '" + fileId + "' ";
                deleteArchTransQuery = "DELETE FROM TRANSPORT_LOADTENDER WHERE File_ID='" + fileId + "'";
            } else if (transType.equals("990")) {
                insertArchTransQuery = "INSERT INTO ARCHIVE_TRANSPORT_LT_RESPONSE SELECT t.* FROM MSCVP.TRANSPORT_LT_RESPONSE t where file_ID= '" + fileId + "' ";
                deleteArchTransQuery = "DELETE FROM TRANSPORT_LT_RESPONSE WHERE File_ID='" + fileId + "'";
            } else if (transType.equals("214")) {
                insertArchTransQuery = "INSERT INTO ARCHIVE_TRANSPORT_SHIPMENT SELECT t.* FROM MSCVP.TRANSPORT_SHIPMENT t where file_ID= '" + fileId + "' ";
                deleteArchTransQuery = "DELETE FROM TRANSPORT_SHIPMENT WHERE File_ID='" + fileId + "'";
            } else if (transType.equals("210")) {
                insertArchTransQuery = "INSERT INTO ARCHIVE_TRANSPORT_INVOICE SELECT t.* FROM MSCVP.TRANSPORT_INVOICE t where file_ID= '" + fileId + "' ";
                deleteArchTransQuery = "DELETE FROM TRANSPORT_INVOICE WHERE File_ID='" + fileId + "'";
            }
            statement.addBatch(insertArchTransQuery);
            int[] count = statement.executeBatch();
            connection.commit();
        } catch (BatchUpdateException batchUpdateException) {
            LoggerUtility.log(logger, "BatchUpdateException occurred in archiveReocords method:: " + batchUpdateException.getMessage(), Level.ERROR, batchUpdateException.getCause());
        } catch (SQLException sqlException) {
            LoggerUtility.log(logger, "SQLException occurred in archiveReocords method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                    statement = null;
                }
                if (connection != null) {
                    connection.close();
                    connection = null;
                }
            } catch (SQLException sqlException) {
                LoggerUtility.log(logger, "finally SQLException occurred in archiveReocords method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
            }
        }
    }

    @Override
    public List getPurHistoryData(String username, String from, String to, String transType) throws ServiceLocatorException {
        System.out.println("username is"+username);
        System.out.println("from date is"+from);
        System.out.println("to date is"+to);
        System.out.println("transType is"+transType);
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        List<PurgeHistoryBean> list = new ArrayList<PurgeHistoryBean>();
        String fromDate = DateUtility.getInstance().DateViewToDBCompare(from);
        String toDate = DateUtility.getInstance().DateViewToDBCompare(to);
        StringBuilder archiveHistoryQuery = new StringBuilder();
        archiveHistoryQuery.append("SELECT *  FROM ARCHIVE_HISTORY WHERE DATE >='" + fromDate + "' AND DATE <='" + toDate + "'");
        if ((transType != null) && (!"-1".equals(transType.trim())) && (!"All".equals(transType.trim()))) {
            archiveHistoryQuery.append("AND TRANSACTION_TYPE = '" + transType + "'");
        }
        archiveHistoryQuery.append("AND FLAG = 'Purge'");
        try {
            connection = ConnectionProvider.getInstance().getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(archiveHistoryQuery.toString());
            while (resultSet.next()) {
                PurgeHistoryBean purgeHistoryBean = new PurgeHistoryBean();
                purgeHistoryBean.setUser(resultSet.getString("USER"));
                purgeHistoryBean.setTransactionType(resultSet.getString("TRANSACTION_TYPE"));
                purgeHistoryBean.setDaysCount(resultSet.getInt("DAYS_COUNT"));
                purgeHistoryBean.setComments(resultSet.getString("COMMENTS"));
                purgeHistoryBean.setArchiveDate(resultSet.getTimestamp("DATE"));
                list.add(purgeHistoryBean);
            }
        } catch (Exception exception) {
            LoggerUtility.log(logger, "Exception occurred in getPurHistoryData method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                    resultSet = null;
                }
                if (statement != null) {
                    statement.close();
                    statement = null;
                }
                if (connection != null) {
                    connection.close();
                    connection = null;
                }
            } catch (SQLException sqlException) {
                LoggerUtility.log(logger, "finally SQLException occurred in getPurHistoryData method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
            }
        }
        return list;
    }

    public List getArcHistoryData(String username, String from, String to, String transType) throws ServiceLocatorException {
        System.out.println("username is"+username);
        System.out.println("from date is"+from);
        System.out.println("to date is"+to);
        System.out.println("transType is"+transType);
        List<PurgeHistoryBean> list = new ArrayList<PurgeHistoryBean>();
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        String fromDate = DateUtility.getInstance().DateViewToDBCompare(from);
        String toDate = DateUtility.getInstance().DateViewToDBCompare(to);
        StringBuilder archiveHistoryQuery = new StringBuilder();
        archiveHistoryQuery.append("SELECT *  FROM ARCHIVE_HISTORY WHERE DATE >='" + fromDate + "' AND DATE <='" + toDate + "'");
        if ((transType != null) && (!"-1".equals(transType.trim())) && (!"All".equals(transType.trim()))) {
            archiveHistoryQuery.append("AND TRANSACTION_TYPE = '" + transType + "'");
        }
        archiveHistoryQuery.append("AND FLAG = 'Archive'");
        try {
            connection = ConnectionProvider.getInstance().getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(archiveHistoryQuery.toString());
            while (resultSet.next()) {
                PurgeHistoryBean purgeHistoryBean = new PurgeHistoryBean();
                purgeHistoryBean.setUser(resultSet.getString("USER"));
                purgeHistoryBean.setTransactionType(resultSet.getString("TRANSACTION_TYPE"));
                purgeHistoryBean.setDaysCount(resultSet.getInt("DAYS_COUNT"));
                purgeHistoryBean.setComments(resultSet.getString("COMMENTS"));
                purgeHistoryBean.setArchiveDate(resultSet.getTimestamp("DATE"));
                list.add(purgeHistoryBean);
            }

        } catch (Exception exception) {
            LoggerUtility.log(logger, "Exception occurred in getArcHistoryData method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                    resultSet = null;
                }
                if (statement != null) {
                    statement.close();
                    statement = null;
                }
                if (connection != null) {
                    connection.close();
                    connection = null;
                }
            } catch (SQLException sqlException) {
                LoggerUtility.log(logger, "finally SQLException occurred in getArcHistoryData method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
            }
        }
        return list;

    }

}
