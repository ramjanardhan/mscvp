package com.mss.ediscv.dashboard;

import com.mss.ediscv.util.ConnectionProvider;
import com.mss.ediscv.util.DateUtility;
import com.mss.ediscv.util.ServiceLocatorException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.mss.ediscv.util.LoggerUtility;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DashBoardServiceImpl implements DashBoardService {


    private static Logger logger = LogManager.getLogger(DashBoardServiceImpl.class.getName());

    @Override
    public JSONArray getDailyEdiStats(char flag) throws ServiceLocatorException {
        JSONArray jsonArray = new JSONArray();
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        String currentDate = DateUtility.getInstance().getCurrentDB2TS();
        try {
            String[] transactions = com.mss.ediscv.util.Properties.getProperty("EdiTransactionsL").split(",");
            if (flag == 'L') {
                transactions = com.mss.ediscv.util.Properties.getProperty("EdiTransactionsL").split(",");
            } else if (flag == 'M') {
                transactions = com.mss.ediscv.util.Properties.getProperty("EdiTransactionsM").split(",");
            }
            int var[] = new int[transactions.length];
            int varValue[] = new int[transactions.length];
            StringBuilder ediTransForQuote = new StringBuilder();
            for (int i = 0; i < transactions.length; i++) {
                var[i] = Integer.parseInt(transactions[i]);
                varValue[i] = 0;
                ediTransForQuote.append("'" + transactions[i] + "',");
            }
            String ediTrans = (ediTransForQuote.toString()).substring(0, (ediTransForQuote.toString()).length() - 1);
            System.out.println("Before Connection::" + DateUtility.getInstance().getCurrentDB2Timestamp());
            connection = ConnectionProvider.getInstance().getConnection();
            System.out.println("after Connection::" + DateUtility.getInstance().getCurrentDB2Timestamp());
            String dailyEdiStatsQuery = "select TRANSACTION_TYPE from files where TRANSACTION_TYPE in (" + ediTrans + ") and DATE(DATE_TIME_RECEIVED) =  DATE('" + currentDate + "') and TRANSACTION_TYPE Is not null and FLOWFLAG='" + flag + "'";
            System.out.println("DailyEdiStats query:" + dailyEdiStatsQuery.toString());
            statement = connection.createStatement();
            resultSet = statement.executeQuery(dailyEdiStatsQuery);
            System.out.println("Query and resultset start time::" + DateUtility.getInstance().getCurrentDB2Timestamp());
            while (resultSet.next()) {
                for (int i = 0; i < var.length; i++) {
                    if (var[i] == Integer.parseInt(resultSet.getString("TRANSACTION_TYPE"))) {
                        varValue[i] = varValue[i] + 1;
                    }
                }
            }
            System.out.println("Resultset end time::" + DateUtility.getInstance().getCurrentDB2Timestamp());
            resultSet.close();
            resultSet = null;
            for (int i = 0; i < var.length; i++) {
                JSONObject obj = new JSONObject();
                obj.put("name", Integer.toString(var[i]));
                obj.put("y", varValue[i]);
                jsonArray.put(obj);
            }
        } catch (SQLException sqlexception) {
            LoggerUtility.log(logger, "sqlException occurred in getDailyEdiStats method:: " + sqlexception.getMessage(), Level.ERROR, sqlexception.getCause());
        } catch (ServiceLocatorException serviceLocatorException) {
            LoggerUtility.log(logger, "Exception occurred in getDailyEdiStats method:: " + serviceLocatorException.getMessage(), Level.ERROR, serviceLocatorException.getCause());
        } catch (JSONException jsonexception) {
            LoggerUtility.log(logger, "Exception occurred in getDailyEdiStats method:: " + jsonexception.getMessage(), Level.ERROR, jsonexception.getCause());
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
            } catch (SQLException sqlexception) {
                LoggerUtility.log(logger, "sqlException occurred in getDailyEdiStats method:: " + sqlexception.getMessage(), Level.ERROR, sqlexception.getCause());

            }
        }
        return jsonArray;
    }

    @Override
    public ArrayList getDailyStatsEDIDocuments(ArrayList timeInervalArray, SimpleDateFormat sdf, char flag) throws ServiceLocatorException {
        ArrayList dailyStatsEDIDoc = new ArrayList();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            String[] transactions = com.mss.ediscv.util.Properties.getProperty("EdiTransactionsL").split(",");
            if (flag == 'L') {
                transactions = com.mss.ediscv.util.Properties.getProperty("EdiTransactionsL").split(",");
            } else if (flag == 'M') {
                transactions = com.mss.ediscv.util.Properties.getProperty("EdiTransactionsM").split(",");
            }
            StringBuilder ediTransForQuote = new StringBuilder();
            for (int i = 0; i < transactions.length; i++) {
                ediTransForQuote.append("'" + transactions[i] + "',");
            }
            String ediTrans = (ediTransForQuote.toString()).substring(0, (ediTransForQuote.toString()).length() - 1);
            System.out.println("Before Connection::" + DateUtility.getInstance().getCurrentDB2Timestamp());
            connection = ConnectionProvider.getInstance().getConnection();
            System.out.println("After Connection::" + DateUtility.getInstance().getCurrentDB2Timestamp());
            String dailyStatsEDIDocumentsQuery = "select count(*) as ediCount from files where TRANSACTION_TYPE in (" + ediTrans + ")  and DATE_TIME_RECEIVED >=cast(? as timestamp) and DATE_TIME_RECEIVED <=cast(? as timestamp) and FLOWFLAG='" + flag + "'";
            System.out.println("DailyStatsEDIDocuments query:" + dailyStatsEDIDocumentsQuery.toString());
            preparedStatement = connection.prepareStatement(dailyStatsEDIDocumentsQuery);
            for (int i = 0; i < timeInervalArray.size() - 1; i++) {
                int count = 0;
                String fromDate = sdf.format(new Date());
                fromDate = fromDate + (" " + timeInervalArray.get(i)) + ":00.000";
                String toDate = sdf.format(new Date());
                toDate = toDate + (" " + timeInervalArray.get(i + 1)) + ":00.000";
                preparedStatement.setString(1, fromDate);
                preparedStatement.setString(2, toDate);
                resultSet = preparedStatement.executeQuery();
                System.out.println("Query and resultset start time::" + DateUtility.getInstance().getCurrentDB2Timestamp());
                while (resultSet.next()) {
                    count = resultSet.getInt("ediCount");
                }
                System.out.println("Resultset end time::" + DateUtility.getInstance().getCurrentDB2Timestamp());
                dailyStatsEDIDoc.add(count);
                resultSet.close();
                resultSet = null;
            }
        } catch (SQLException sqlexception) {
            LoggerUtility.log(logger, "sqlException occurred in getDailyStatsEDIDocuments method:: " + sqlexception.getMessage(), Level.ERROR, sqlexception.getCause());
        } catch (ServiceLocatorException serviceLocatorException) {
            LoggerUtility.log(logger, "Exception occurred in getDailyStatsEDIDocuments method:: " + serviceLocatorException.getMessage(), Level.ERROR, serviceLocatorException.getCause());
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
                LoggerUtility.log(logger, "sqlException occurred in getDailyStatsEDIDocuments method:: " + sqlexception.getMessage(), Level.ERROR, sqlexception.getCause());
            }
        }
        return dailyStatsEDIDoc;
    }

    @Override
    public JSONArray getDailyFailureRate(char flag) {
        JSONArray dailyFailureRate = new JSONArray();
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        double successPercentage = 0;
        double failurePercentage = 0;
        double pendingPercentage = 0;
        float successCount = 0;
        float failureCount = 0;
        float pendingCount = 0;
        float totalCount = 0;
        try {
            String currentDate = DateUtility.getInstance().getCurrentDB2TS();
            System.out.println("Before Connection::" + DateUtility.getInstance().getCurrentDB2Timestamp());
            connection = ConnectionProvider.getInstance().getConnection();
            System.out.println("After Connection::" + DateUtility.getInstance().getCurrentDB2Timestamp());
            String dailyFailureRateQuery = "SELECT (SELECT COUNT(*) as TOTALCOUNT FROM FILES WHERE FLOWFLAG = '"+flag+"' AND (DATE(DATE_TIME_RECEIVED) =  DATE('" + currentDate + "')) ) as totalCount, "
                    + " (SELECT COUNT(*) as SUCCESSCOUNT FROM FILES WHERE  (STATUS='SUCCESS' and (TRANSACTION_TYPE ='997' OR (ACK_STATUS='ACCEPTED' OR ACK_STATUS='SUCCESS'))) and FLOWFLAG = '"+flag+"' AND (DATE(DATE_TIME_RECEIVED) =  DATE('" + currentDate + "'))) as SuccessCount, "
                    + " (SELECT COUNT(*) as FAILURECOUNT FROM FILES WHERE (STATUS='ERROR' OR ACK_STATUS='Rejected') and FLOWFLAG = '"+flag+"' AND (DATE(DATE_TIME_RECEIVED) =  DATE('" + currentDate + "'))) as failureCount, "
                    + " (SELECT COUNT(*) as PENDINGCOUNT FROM FILES WHERE (STATUS='SUCCESS' and (ACK_STATUS='overdue' OR ACK_STATUS='OVERDUE')) and FLOWFLAG = '"+flag+"' AND (DATE(DATE_TIME_RECEIVED) =  DATE('" + currentDate + "'))) as pendingCount  from SYSIBM.SYSDUMMY1";
            System.out.println("DailyFailureRate query:" + dailyFailureRateQuery.toString());
            statement = connection.createStatement();
            resultSet = statement.executeQuery(dailyFailureRateQuery);
            System.out.println("Query and resultset start time::" + DateUtility.getInstance().getCurrentDB2Timestamp());
            if (resultSet.next()) {
                totalCount = resultSet.getInt("totalCount");
                successCount = resultSet.getInt("SuccessCount");
                failureCount = resultSet.getInt("failureCount");
                pendingCount = resultSet.getInt("pendingCount");
            }
            System.out.println("Resultset end time::" + DateUtility.getInstance().getCurrentDB2Timestamp());
            successPercentage = ((successCount * 100) / totalCount);
            failurePercentage = ((failureCount * 100) / totalCount);
            pendingPercentage = ((pendingCount * 100) / totalCount);

            String name = "name";
            String y = "y";
            JSONObject count = new JSONObject();
            JSONObject count1 = new JSONObject();
            JSONObject count2 = new JSONObject();
            count.put(name, "Success");
            count.put(y, (double) (Math.round(successPercentage * 100.0) / 100.0));
            dailyFailureRate.put(count);
            count1.put(name, "Failure");
            count1.put(y, (double) (Math.round(failurePercentage * 100.0) / 100.0));
            dailyFailureRate.put(count1);
            count2.put(name, "Pending");
            count2.put(y, (double) (Math.round(pendingPercentage * 100.0) / 100.0));
            dailyFailureRate.put(count2);
        } catch (Exception exception) {
            LoggerUtility.log(logger, "Exception occurred in getDailyFailureRate method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
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
            } catch (SQLException sqlexception) {
                LoggerUtility.log(logger, "sqlException occurred in getDailyFailureRate method:: " + sqlexception.getMessage(), Level.ERROR, sqlexception.getCause());
            }
        }
        return dailyFailureRate;
    }

    @Override
    public ArrayList getTopTpEdiInboundCount(char flag) throws ServiceLocatorException {
        ArrayList tpIbCount = new ArrayList();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String currentDate = DateUtility.getInstance().getCurrentDB2TS();
        String[] topTpNames = new String[0];
        if (flag == 'M') {
            topTpNames = com.mss.ediscv.util.Properties.getProperty("Top10EdiTradingPartners").split(",");
        } else if (flag == 'L') {
            topTpNames = com.mss.ediscv.util.Properties.getProperty("Top10LogisticsTradingPartners").split(",");
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:MM");
            System.out.println("Before Connection::" + DateUtility.getInstance().getCurrentDB2Timestamp());
            connection = ConnectionProvider.getInstance().getConnection();
            System.out.println("After Connection::" + DateUtility.getInstance().getCurrentDB2Timestamp());
            String topTpEdiInboundCountQuery = "SELECT COUNT(*) AS INBOUNDCOUNT FROM FILES LEFT OUTER JOIN TP ON (TP.ID=FILES.SENDER_ID) WHERE TP.NAME=? and (DATE(DATE_TIME_RECEIVED) =  DATE('" + currentDate + "')) and FLOWFLAG='" + flag + "' ";
            System.out.println("TopTpEdiInboundCount query:" + topTpEdiInboundCountQuery.toString());
            preparedStatement = connection.prepareStatement(topTpEdiInboundCountQuery);
            for (int i = 0; i < topTpNames.length; i++) {
                preparedStatement.setString(1, topTpNames[i]);
                resultSet = preparedStatement.executeQuery();
                System.out.println("Query and resultset start time::" + DateUtility.getInstance().getCurrentDB2Timestamp());
                if (resultSet.next()) {
                    tpIbCount.add(resultSet.getInt("INBOUNDCOUNT"));
                }
                System.out.println("Resultset end time::" + DateUtility.getInstance().getCurrentDB2Timestamp());
                resultSet.close();
                resultSet = null;
            }
        } catch (Exception exception) {
            LoggerUtility.log(logger, "Exception occurred in getTopTpEdiInboundCount method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
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
                LoggerUtility.log(logger, "sqlException occurred in getTopTpEdiInboundCount method:: " + sqlexception.getMessage(), Level.ERROR, sqlexception.getCause());
            }
        }
        return tpIbCount;
    }

    @Override
    public ArrayList getTopTpEdiOutboundCount(char flag) throws ServiceLocatorException {
        ArrayList tpObCount = new ArrayList();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String currentDate = DateUtility.getInstance().getCurrentDB2TS();
        String[] topTpNames = new String[0];
        if (flag == 'M') {
            topTpNames = com.mss.ediscv.util.Properties.getProperty("Top10EdiTradingPartners").split(",");
        } else if (flag == 'L') {
            topTpNames = com.mss.ediscv.util.Properties.getProperty("Top10LogisticsTradingPartners").split(",");
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:MM");
            System.out.println("Before Connection::" + DateUtility.getInstance().getCurrentDB2Timestamp());
            connection = ConnectionProvider.getInstance().getConnection();
            System.out.println("After Connection::" + DateUtility.getInstance().getCurrentDB2Timestamp());
            String topTpEdiOutboundCountQuery = "SELECT COUNT(*) AS OUTBOUNDCOUNT FROM FILES LEFT OUTER JOIN TP ON (TP.ID=FILES.RECEIVER_ID) WHERE TP.NAME=? and (DATE(DATE_TIME_RECEIVED) = DATE('" + currentDate + "')) and FLOWFLAG='" + flag + "'";
            System.out.println("TopTpEdiOutboundCount query:" + topTpEdiOutboundCountQuery.toString());
            preparedStatement = connection.prepareStatement(topTpEdiOutboundCountQuery);
            for (int i = 0; i < topTpNames.length; i++) {
                preparedStatement.setString(1, topTpNames[i]);
                resultSet = preparedStatement.executeQuery();
                System.out.println("Query and resultset start time::" + DateUtility.getInstance().getCurrentDB2Timestamp());
                if (resultSet.next()) {
                    tpObCount.add(resultSet.getInt("OUTBOUNDCOUNT"));
                }
                System.out.println("Resultset end time::" + DateUtility.getInstance().getCurrentDB2Timestamp());
                resultSet.close();
                resultSet = null;
            }
        } catch (Exception exception) {
            LoggerUtility.log(logger, "Exception occurred in getTopTpEdiOutboundCount method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
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
                LoggerUtility.log(logger, "sqlException occurred in getTopTpEdiOutboundCount method:: " + sqlexception.getMessage(), Level.ERROR, sqlexception.getCause());
            }
        }
        return tpObCount;
    }

    @Override
    public ArrayList getDSVIBMonthlyVolumes(char flag) throws ServiceLocatorException {
        ArrayList ibCount = new ArrayList();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            System.out.println("Before Connection::" + DateUtility.getInstance().getCurrentDB2Timestamp());
            connection = ConnectionProvider.getInstance().getConnection();
            System.out.println("Before Connection::" + DateUtility.getInstance().getCurrentDB2Timestamp());
            String dsvIBMonthlyVolumesQuery = "SELECT COUNT(*) as IBCOUNT FROM FILES WHERE DIRECTION='INBOUND' AND FLOWFLAG = '"+String.valueOf(flag)+"' AND DATE(DATE_TIME_RECEIVED) = ? ";
            System.out.println("DSVIBMonthlyVolumes query:" + dsvIBMonthlyVolumesQuery.toString());
            Date date1 = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
            String strDate = formatter.format(date1);
            String[] date2 = strDate.split("/");
            int date = Integer.parseInt(date2[2]);
            int month = Integer.parseInt(date2[1]);
            int year = Integer.parseInt(date2[0]);
            preparedStatement = connection.prepareStatement(dsvIBMonthlyVolumesQuery);
            for (int i = date; i > 0; i--) {
                String datetimereceived = year + "-" + month + "-" + i;
                preparedStatement.setString(1, datetimereceived);
                resultSet = preparedStatement.executeQuery();
                System.out.println("Query and resultset start time::" + DateUtility.getInstance().getCurrentDB2Timestamp());
                if (resultSet.next()) {
                    ibCount.add(resultSet.getInt("IBCOUNT"));
                }
                System.out.println("Resultset end time::" + DateUtility.getInstance().getCurrentDB2Timestamp());
                resultSet.close();
                resultSet = null;
            }
        } catch (Exception exception) {
            LoggerUtility.log(logger, "Exception occurred in getDSVIBMonthlyVolumes method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
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
                LoggerUtility.log(logger, "sqlException occurred in getDSVIBMonthlyVolumes method:: " + sqlexception.getMessage(), Level.ERROR, sqlexception.getCause());
            }
        }
        Collections.reverse(ibCount);
        return ibCount;
    }

    @Override
    public ArrayList getDSVOBMonthlyVolumes(char flag) throws ServiceLocatorException {
        ArrayList obCount = new ArrayList();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            System.out.println("Before Connection::" + DateUtility.getInstance().getCurrentDB2Timestamp());
            connection = ConnectionProvider.getInstance().getConnection();
            System.out.println("After Connection::" + DateUtility.getInstance().getCurrentDB2Timestamp());
            String dsvOBMonthlyVolumes = "SELECT COUNT(*) as OBCOUNT FROM FILES WHERE DIRECTION='OUTBOUND' AND FLOWFLAG = '"+String.valueOf(flag)+"' AND DATE(DATE_TIME_RECEIVED) =  ? ";
            System.out.println("DSVOBMonthlyVolumes query:" + dsvOBMonthlyVolumes.toString());
            Date date1 = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
            String strDate = formatter.format(date1);
            String[] date2 = strDate.split("/");
            int date = Integer.parseInt(date2[2]);
            int month = Integer.parseInt(date2[1]);
            int year = Integer.parseInt(date2[0]);
            preparedStatement = connection.prepareStatement(dsvOBMonthlyVolumes);
            for (int i = date; i > 0; i--) {
                String datetimereceived = year + "-" + month + "-" + i;
                preparedStatement.setString(1, datetimereceived);
                resultSet = preparedStatement.executeQuery();
                System.out.println("Query and resultset start time::" + DateUtility.getInstance().getCurrentDB2Timestamp());
                if (resultSet.next()) {
                    obCount.add(resultSet.getInt("OBCOUNT"));
                }
                System.out.println("Resultset end time::" + DateUtility.getInstance().getCurrentDB2Timestamp());
                resultSet.close();
                resultSet = null;
            }
        } catch (Exception exception) {
            LoggerUtility.log(logger, "Exception occurred in getDSVOBMonthlyVolumes method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
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
                LoggerUtility.log(logger, "sqlException occurred in getDSVOBMonthlyVolumes method:: " + sqlexception.getMessage(), Level.ERROR, sqlexception.getCause());
            }
        }
        Collections.reverse(obCount);
        return obCount;
    }

    @Override
    public int getMonthlyIbVolumes(char flag) throws ServiceLocatorException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        int Ibcount = 0;
        try {
            System.out.println("Before Connection::" + DateUtility.getInstance().getCurrentDB2Timestamp());
            connection = ConnectionProvider.getInstance().getConnection();
            System.out.println("After Connection::" + DateUtility.getInstance().getCurrentDB2Timestamp());
            String monthlyIbVolumesQuery = "SELECT COUNT(*) as IBCOUNT FROM FILES WHERE DIRECTION='INBOUND' AND FLOWFLAG = '"+String.valueOf(flag)+"' AND year(DATE_TIME_RECEIVED)=? and month(DATE_TIME_RECEIVED)=?";
            System.out.println("MonthlyIbVolumes query:" + monthlyIbVolumesQuery.toString());
            Date date1 = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            String strDate = formatter.format(date1);
            String[] date2 = strDate.split("/");
            int date = Integer.parseInt(date2[0]);
            int month = Integer.parseInt(date2[1]);
            int year = Integer.parseInt(date2[2]);
            preparedStatement = connection.prepareStatement(monthlyIbVolumesQuery);
            preparedStatement.setInt(1, year);
            preparedStatement.setInt(2, month);
            resultSet = preparedStatement.executeQuery();
            System.out.println("Query and resultset start time::" + DateUtility.getInstance().getCurrentDB2Timestamp());
            while (resultSet.next()) {
                Ibcount = resultSet.getInt("IBCOUNT");
            }
            System.out.println("Resultset end time::" + DateUtility.getInstance().getCurrentDB2Timestamp());
        } catch (SQLException sqlexception) {
            LoggerUtility.log(logger, "sqlException occurred in getMonthlyIbVolumes method:: " + sqlexception.getMessage(), Level.ERROR, sqlexception.getCause());
        } catch (ServiceLocatorException serviceLocatorException) {
            LoggerUtility.log(logger, "Exception occurred in getMonthlyIbVolumes method:: " + serviceLocatorException.getMessage(), Level.ERROR, serviceLocatorException.getCause());
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
                LoggerUtility.log(logger, "sqlException occurred in getMonthlyIbVolumes method:: " + sqlexception.getMessage(), Level.ERROR, sqlexception.getCause());
            }
        }
        return Ibcount;
    }

    @Override
    public int getMonthlyObVolumes(char flag) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        int Obcount = 0;
        try {
            System.out.println("Before Connection::" + DateUtility.getInstance().getCurrentDB2Timestamp());
            connection = ConnectionProvider.getInstance().getConnection();
            System.out.println("After Connection::" + DateUtility.getInstance().getCurrentDB2Timestamp());
            String monthlyObVolumesQuery = "SELECT COUNT(*) as OBCOUNT FROM FILES WHERE DIRECTION='OUTBOUND' AND FLOWFLAG = '"+String.valueOf(flag)+"' AND year(DATE_TIME_RECEIVED)=? and month(DATE_TIME_RECEIVED)=?";
            System.out.println("MonthlyObVolumes query:" + monthlyObVolumesQuery.toString());
            Date date1 = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            String strDate = formatter.format(date1);
            String[] date2 = strDate.split("/");
            int date = Integer.parseInt(date2[0]);
            int month = Integer.parseInt(date2[1]);
            int year = Integer.parseInt(date2[2]);
            preparedStatement = connection.prepareStatement(monthlyObVolumesQuery);
            preparedStatement.setInt(1, year);
            preparedStatement.setInt(2, month);
            resultSet = preparedStatement.executeQuery();
            System.out.println("Query and resultset start time::" + DateUtility.getInstance().getCurrentDB2Timestamp());
            while (resultSet.next()) {
                Obcount = resultSet.getInt("OBCOUNT");
            }
            System.out.println("Resultset end time::" + DateUtility.getInstance().getCurrentDB2Timestamp());
        } catch (SQLException sqlexception) {
            LoggerUtility.log(logger, "sqlException occurred in getMonthlyIbVolumes method:: " + sqlexception.getMessage(), Level.ERROR, sqlexception.getCause());
        } catch (ServiceLocatorException serviceLocatorException) {
            LoggerUtility.log(logger, "Exception occurred in getMonthlyIbVolumes method:: " + serviceLocatorException.getMessage(), Level.ERROR, serviceLocatorException.getCause());

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
                LoggerUtility.log(logger, "sqlException occurred in getMonthlyIbVolumes method:: " + sqlexception.getMessage(), Level.ERROR, sqlexception.getCause());
            }
        }
        return Obcount;
    }

    @Override
    public JSONArray getEdiTransactionStats(char flag) {
        JSONArray jsonArray = new JSONArray();
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            int completedTotal = 0;
            int failedTotal = 0;
            int pendingTotal = 0;
            String currentDate = DateUtility.getInstance().getCurrentDB2TS();
            String[] transactions = com.mss.ediscv.util.Properties.getProperty("EdiTransactionsL").split(",");
            if (flag == 'L') {
                transactions = com.mss.ediscv.util.Properties.getProperty("EdiTransactionsL").split(",");
            } else if (flag == 'M') {
                transactions = com.mss.ediscv.util.Properties.getProperty("EdiTransactionsM").split(",");
            }
            StringBuilder ediTransForQuote = new StringBuilder();
            for (int i = 0; i < transactions.length; i++) {
                ediTransForQuote.append("'" + transactions[i] + "',");
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:MM");
            String ediTrans = (ediTransForQuote.toString()).substring(0, (ediTransForQuote.toString()).length() - 1);
            System.out.println("Before Connection::" + DateUtility.getInstance().getCurrentDB2Timestamp());
            connection = ConnectionProvider.getInstance().getConnection();
            System.out.println("After Connection::" + DateUtility.getInstance().getCurrentDB2Timestamp());
            String ediTransactionStatsQuery = "SELECT (select count(STATUS) as completedTotal from files where (DATE(DATE_TIME_RECEIVED) =  DATE('" + currentDate + "')) and TRANSACTION_TYPE in (" + ediTrans + ") and STATUS='SUCCESS' and ACK_STATUS='ACCEPTED' and FLOWFLAG='" + flag + "') as completedTotal, "
                    + " (select count(STATUS) as pendingTotal from files where (DATE(DATE_TIME_RECEIVED) =  DATE('" + currentDate + "')) and TRANSACTION_TYPE in (" + ediTrans + ") and STATUS='SUCCESS' and (ACK_STATUS='overdue' OR ACK_STATUS='OVERDUE') and FLOWFLAG='" + flag + "') as pendingTotal, "
                    + " (select count(STATUS) as failureTotal from files where (DATE(DATE_TIME_RECEIVED) =  DATE('" + currentDate + "')) and TRANSACTION_TYPE in (" + ediTrans + ") and STATUS='ERROR' OR ACK_STATUS='Rejected' and FLOWFLAG='" + flag + "') as failureTotal from SYSIBM.SYSDUMMY1";
            System.out.println("EdiTransactionStats query:" + ediTransactionStatsQuery.toString());
            statement = connection.createStatement();
            resultSet = statement.executeQuery(ediTransactionStatsQuery);
            System.out.println("Query and resultset start time::" + DateUtility.getInstance().getCurrentDB2Timestamp());
            if (resultSet.next()) {
                completedTotal = resultSet.getInt("completedTotal");
                pendingTotal = resultSet.getInt("pendingTotal");
                failedTotal = resultSet.getInt("failureTotal");
            }
            System.out.println("Resultset end time::" + DateUtility.getInstance().getCurrentDB2Timestamp());
            JSONObject pendingJsonObj = new JSONObject();
            pendingJsonObj.put("name", "Pending");
            pendingJsonObj.put("y", pendingTotal);

            JSONObject failedJsonObj = new JSONObject();
            failedJsonObj.put("name", "Failed");
            failedJsonObj.put("y", failedTotal);

            JSONObject completedJsonObj = new JSONObject();
            completedJsonObj.put("name", "Completed");
            completedJsonObj.put("y", completedTotal);

            jsonArray.put(pendingJsonObj);
            jsonArray.put(failedJsonObj);
            jsonArray.put(completedJsonObj);

        } catch (SQLException sqlexception) {
            LoggerUtility.log(logger, "Exception occurred in getEdiTransactionStats method:: " + sqlexception.getMessage(), Level.ERROR, sqlexception.getCause());
        } catch (ServiceLocatorException ServiceLocatorException) {
            LoggerUtility.log(logger, "Exception occurred in getEdiTransactionStats method:: " + ServiceLocatorException.getMessage(), Level.ERROR, ServiceLocatorException.getCause());
        } catch (JSONException jsonexception) {
            LoggerUtility.log(logger, "Exception occurred in getEdiTransactionStats method:: " + jsonexception.getMessage(), Level.ERROR, jsonexception.getCause());
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
            } catch (SQLException sqlexception) {
                LoggerUtility.log(logger, "sqlException occurred in getEdiTransactionStats method:: " + sqlexception.getMessage(), Level.ERROR, sqlexception.getCause());
            }
        }
        return jsonArray;
    }

    @Override
    public JSONArray getEdiDocumentsVolume(char flag) {
        JSONArray jsonArray = new JSONArray();
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            String[] transactions = com.mss.ediscv.util.Properties.getProperty("EdiTransactionsL").split(",");
            if (flag == 'L') {
                transactions = com.mss.ediscv.util.Properties.getProperty("EdiTransactionsL").split(",");
            } else if (flag == 'M') {
                transactions = com.mss.ediscv.util.Properties.getProperty("EdiTransactionsM").split(",");
            }
            int var[] = new int[transactions.length];
            int varValue[] = new int[transactions.length];
            StringBuilder ediTransForQuote = new StringBuilder();
            for (int i = 0; i < transactions.length; i++) {
                var[i] = Integer.parseInt(transactions[i]);
                varValue[i] = 0;
                ediTransForQuote.append("'" + transactions[i] + "',");
            }
            String ediTrans = (ediTransForQuote.toString()).substring(0, (ediTransForQuote.toString()).length() - 1);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:MM");
            String currentDate = DateUtility.getInstance().getCurrentDB2TS();

            System.out.println("Before Connection::" + DateUtility.getInstance().getCurrentDB2Timestamp());
            connection = ConnectionProvider.getInstance().getConnection();
            System.out.println("After Connection::" + DateUtility.getInstance().getCurrentDB2Timestamp());
            String ediDocumentsVolumeQuery = "select TRANSACTION_TYPE from files where (DATE(DATE_TIME_RECEIVED) =  DATE('" + currentDate + "')) and TRANSACTION_TYPE in (" + ediTrans + ") and TRANSACTION_TYPE Is not null and FLOWFLAG='" + flag + "'";
            System.out.println("EdiDocumentsVolume query:" + ediDocumentsVolumeQuery.toString());
            statement = connection.createStatement();
            resultSet = statement.executeQuery(ediDocumentsVolumeQuery);
            System.out.println("Query and resultset start time::" + DateUtility.getInstance().getCurrentDB2Timestamp());
            while (resultSet.next()) {
                for (int i = 0; i < var.length; i++) {
                    if (var[i] == Integer.parseInt(resultSet.getString("TRANSACTION_TYPE"))) {
                        varValue[i] = varValue[i] + 1;
                    }
                }
            }
            System.out.println("Resultset end time::" + DateUtility.getInstance().getCurrentDB2Timestamp());
            resultSet.close();
            for (int i = 0; i < var.length; i++) {
                JSONObject obj = new JSONObject();
                obj.put("name", Integer.toString(var[i]));
                obj.put("y", varValue[i]);
                jsonArray.put(obj);
            }
        } catch (SQLException sqlexception) {
            LoggerUtility.log(logger, "sqlException occurred in getEdiDocumentsVolume method:: " + sqlexception.getMessage(), Level.ERROR, sqlexception.getCause());
        } catch (ServiceLocatorException serviceLocatorException) {
            LoggerUtility.log(logger, "Exception occurred in getEdiDocumentsVolume method:: " + serviceLocatorException.getMessage(), Level.ERROR, serviceLocatorException.getCause());
        } catch (JSONException jsonexception) {
            LoggerUtility.log(logger, "Exception occurred in getEdiDocumentsVolume method:: " + jsonexception.getMessage(), Level.ERROR, jsonexception.getCause());
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
            } catch (SQLException sqlexception) {
                LoggerUtility.log(logger, "sqlException occurred in getEdiDocumentsVolume method:: " + sqlexception.getMessage(), Level.ERROR, sqlexception.getCause());
            }
        }
        return jsonArray;
    }

    @Override
    public JSONArray getEdiTradingPartnerVolume(char flag) {
        JSONArray jsonArray = new JSONArray();
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        String[] tpdetails = com.mss.ediscv.util.Properties.getProperty("Top4LogisticsTradingPartners").split(",");
        try {
            if (flag == 'M') {
                tpdetails = com.mss.ediscv.util.Properties.getProperty("Top4EDITradingPartners").split(",");
            } else if (flag == 'L') {
                tpdetails = com.mss.ediscv.util.Properties.getProperty("Top4LogisticsTradingPartners").split(",");
            }
            int count[] = new int[tpdetails.length];
            int i = 0;
            int j = 0;
            String[] transactions = com.mss.ediscv.util.Properties.getProperty("EdiTransactionsL").split(",");
            if (flag == 'L') {
                transactions = com.mss.ediscv.util.Properties.getProperty("EdiTransactionsL").split(",");
            } else if (flag == 'M') {
                transactions = com.mss.ediscv.util.Properties.getProperty("EdiTransactionsM").split(",");
            }
            StringBuilder ediTransForQuote = new StringBuilder();
            for (int k = 0; k < transactions.length; k++) {
                ediTransForQuote.append("'" + transactions[k] + "',");
            }
            String ediTrans = (ediTransForQuote.toString()).substring(0, (ediTransForQuote.toString()).length() - 1);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:MM");
            System.out.println("Before Connection::" + DateUtility.getInstance().getCurrentDB2Timestamp());
            connection = ConnectionProvider.getInstance().getConnection();
            System.out.println("After Connection::" + DateUtility.getInstance().getCurrentDB2Timestamp());
            String currentDate = DateUtility.getInstance().getCurrentDB2TS();
            for (i = 0; i < tpdetails.length; i++) {
                String ediTradingPartnerVolumeQuery = "select count(TP.NAME) as tpNameCount from tp left outer join FILES on (FILES.SENDER_ID=TP.ID OR FILES.RECEIVER_ID=TP.ID) where (DATE(DATE_TIME_RECEIVED) =  DATE('" + currentDate + "')) and FILES.TRANSACTION_TYPE in (" + ediTrans + ") and TP.FLOW_FLAG=3 and TP.NAME='" + tpdetails[i] + "' and FLOWFLAG='" + flag + "'";
                System.out.println("EdiTradingPartnerVolume query:" + ediTradingPartnerVolumeQuery.toString());
                statement = connection.createStatement();
                resultSet = statement.executeQuery(ediTradingPartnerVolumeQuery);
                System.out.println("Query and resultset start time::" + DateUtility.getInstance().getCurrentDB2Timestamp());
                while (resultSet.next()) {
                    count[i] = resultSet.getInt("tpNameCount");
                }
                System.out.println("Resultset end time::" + DateUtility.getInstance().getCurrentDB2Timestamp());
                resultSet.close();
                statement.close();
            }

            for (j = 0; j < tpdetails.length; j++) {
                JSONObject objtp = new JSONObject();
                objtp.put("name", tpdetails[j]);
                objtp.put("y", count[j]);
                jsonArray.put(objtp);
            }

        } catch (SQLException sqlexception) {
            LoggerUtility.log(logger, "sqlException occurred in getEdiTradingPartnerVolume method:: " + sqlexception.getMessage(), Level.ERROR, sqlexception.getCause());
        } catch (ServiceLocatorException serviceLocatorException) {
            LoggerUtility.log(logger, "Exception occurred in getEdiTradingPartnerVolume method:: " + serviceLocatorException.getMessage(), Level.ERROR, serviceLocatorException.getCause());
        } catch (JSONException jsonexception) {
            LoggerUtility.log(logger, "Exception occurred in getEdiTradingPartnerVolume method:: " + jsonexception.getMessage(), Level.ERROR, jsonexception.getCause());
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
            } catch (SQLException sqlexception) {
                LoggerUtility.log(logger, "sqlException occurred in getEdiTradingPartnerVolume method:: " + sqlexception.getMessage(), Level.ERROR, sqlexception.getCause());
            }
        }
        return jsonArray;
    }

    @Override
    public ArrayList getEDIDocuments204IB(ArrayList timeInervalArray, SimpleDateFormat sdf, char flag) {
        ArrayList inbound204 = new ArrayList();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            System.out.println("Before Connection::" + DateUtility.getInstance().getCurrentDB2Timestamp());
            connection = ConnectionProvider.getInstance().getConnection();
            System.out.println("After Connection::" + DateUtility.getInstance().getCurrentDB2Timestamp());
            String ediDocuments204IBQuery = "select count(*) as inbound204Count from files where TRANSACTION_TYPE ='204' and DIRECTION ='INBOUND' and DATE_TIME_RECEIVED >=cast(? as timestamp) and DATE_TIME_RECEIVED <=cast(? as timestamp) and FLOWFLAG='" + flag + "'";
            System.out.println("EDIDocuments204IB query:" + ediDocuments204IBQuery.toString());
            preparedStatement = connection.prepareStatement(ediDocuments204IBQuery);
            for (int i = 0; i < timeInervalArray.size() - 1; i++) {
                int count = 0;
                String fromDate = sdf.format(new Date());
                fromDate = fromDate + (" " + timeInervalArray.get(i)) + ":00.000";
                String toDate = sdf.format(new Date());
                toDate = toDate + (" " + timeInervalArray.get(i + 1)) + ":00.000";

                preparedStatement.setString(1, fromDate);
                preparedStatement.setString(2, toDate);
                resultSet = preparedStatement.executeQuery();
                System.out.println("Query and resultset start time::" + DateUtility.getInstance().getCurrentDB2Timestamp());
                while (resultSet.next()) {
                    count = resultSet.getInt("inbound204Count");
                }
                System.out.println("Resultset end time::" + DateUtility.getInstance().getCurrentDB2Timestamp());
                inbound204.add(count);
                resultSet.close();
                resultSet = null;
            }
        } catch (SQLException sqlexception) {
            LoggerUtility.log(logger, "sqlException occurred in getEDIDocuments204IB method:: " + sqlexception.getMessage(), Level.ERROR, sqlexception.getCause());
        } catch (ServiceLocatorException serviceLocatorException) {
            LoggerUtility.log(logger, "Exception occurred in getEDIDocuments204IB method:: " + serviceLocatorException.getMessage(), Level.ERROR, serviceLocatorException.getCause());
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
                LoggerUtility.log(logger, "sqlException occurred in getEDIDocuments204IB method:: " + sqlexception.getMessage(), Level.ERROR, sqlexception.getCause());
            }
        }
        return inbound204;
    }

    @Override
    public ArrayList getEDIDocuments204OB(ArrayList timeInervalArray, SimpleDateFormat sdf, char flag) {
        ArrayList outbound204 = new ArrayList();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            System.out.println("Before Connection::" + DateUtility.getInstance().getCurrentDB2Timestamp());
            connection = ConnectionProvider.getInstance().getConnection();
            System.out.println("After Connection::" + DateUtility.getInstance().getCurrentDB2Timestamp());
            String ediDocuments204OBQuery = "select count(*) as outbound204Count from files where TRANSACTION_TYPE = '204' and DIRECTION = 'OUTBOUND' and DATE_TIME_RECEIVED >=cast(? as timestamp) and DATE_TIME_RECEIVED <=cast(? as timestamp) and FLOWFLAG='" + flag + "'";
            System.out.println("EDIDocuments204OB query:" + ediDocuments204OBQuery.toString());
            preparedStatement = connection.prepareStatement(ediDocuments204OBQuery);
            for (int i = 0; i < timeInervalArray.size() - 1; i++) {
                int count = 0;
                String fromDate = sdf.format(new Date());
                fromDate = fromDate + (" " + timeInervalArray.get(i)) + ":00.000";
                String toDate = sdf.format(new Date());
                toDate = toDate + (" " + timeInervalArray.get(i + 1)) + ":00.000";

                preparedStatement.setString(1, fromDate);
                preparedStatement.setString(2, toDate);
                resultSet = preparedStatement.executeQuery();
                System.out.println("Query and resultset start time::" + DateUtility.getInstance().getCurrentDB2Timestamp());
                while (resultSet.next()) {
                    count = resultSet.getInt("outbound204Count");
                }
                System.out.println("Resultset end time::" + DateUtility.getInstance().getCurrentDB2Timestamp());
                outbound204.add(count);
                resultSet.close();
                resultSet = null;
            }
        } catch (SQLException sqlexception) {
            LoggerUtility.log(logger, "sqlException occurred in getEDIDocuments204OB method:: " + sqlexception.getMessage(), Level.ERROR, sqlexception.getCause());;
        } catch (ServiceLocatorException serviceLocatorException) {
            LoggerUtility.log(logger, "Exception occurred in getEDIDocuments204OB method:: " + serviceLocatorException.getMessage(), Level.ERROR, serviceLocatorException.getCause());
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
                LoggerUtility.log(logger, "sqlException occurred in getEDIDocuments204IB method:: " + sqlexception.getMessage(), Level.ERROR, sqlexception.getCause());
            }
        }
        return outbound204;
    }

    @Override
    public ArrayList getEDIDocuments990IB(ArrayList timeInervalArray, SimpleDateFormat sdf, char flag) {
        ArrayList inbound990 = new ArrayList();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            System.out.println("Before Connection::" + DateUtility.getInstance().getCurrentDB2Timestamp());
            connection = ConnectionProvider.getInstance().getConnection();
            System.out.println("Before Connection::" + DateUtility.getInstance().getCurrentDB2Timestamp());
            String ediDocuments990IBQuery = "select count(*) as inbound990Count from files where TRANSACTION_TYPE = '990' and DIRECTION = 'INBOUND' and DATE_TIME_RECEIVED >=cast(? as timestamp) and DATE_TIME_RECEIVED <=cast(? as timestamp) and FLOWFLAG='" + flag + "'";
            System.out.println("CommunicationProtocols query:" + ediDocuments990IBQuery.toString());
            preparedStatement = connection.prepareStatement(ediDocuments990IBQuery);
            for (int i = 0; i < timeInervalArray.size() - 1; i++) {
                int count = 0;
                String fromDate = sdf.format(new Date());
                fromDate = fromDate + (" " + timeInervalArray.get(i)) + ":00.000";
                String toDate = sdf.format(new Date());
                toDate = toDate + (" " + timeInervalArray.get(i + 1)) + ":00.000";

                preparedStatement.setString(1, fromDate);
                preparedStatement.setString(2, toDate);
                resultSet = preparedStatement.executeQuery();
                System.out.println("Query and resultset start time::" + DateUtility.getInstance().getCurrentDB2Timestamp());
                while (resultSet.next()) {
                    count = resultSet.getInt("inbound990Count");
                }
                System.out.println("Resultset end time::" + DateUtility.getInstance().getCurrentDB2Timestamp());
                inbound990.add(count);
                resultSet.close();
                resultSet = null;
            }
        } catch (SQLException sqlexception) {
            LoggerUtility.log(logger, "sqlException occurred in getEDIDocuments990IB method:: " + sqlexception.getMessage(), Level.ERROR, sqlexception.getCause());
        } catch (ServiceLocatorException serviceLocatorException) {
            LoggerUtility.log(logger, "Exception occurred in getEDIDocuments990IB method:: " + serviceLocatorException.getMessage(), Level.ERROR, serviceLocatorException.getCause());
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
                LoggerUtility.log(logger, "sqlException occurred in getEDIDocuments990IB method:: " + sqlexception.getMessage(), Level.ERROR, sqlexception.getCause());
            }
        }
        return inbound990;
    }

    @Override
    public ArrayList getEDIDocuments990OB(ArrayList timeInervalArray, SimpleDateFormat sdf, char flag) {
        ArrayList outbound990 = new ArrayList();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            System.out.println("Before Connection::" + DateUtility.getInstance().getCurrentDB2Timestamp());
            connection = ConnectionProvider.getInstance().getConnection();
            System.out.println("After Connection::" + DateUtility.getInstance().getCurrentDB2Timestamp());
            String ediDocuments990OBQuery = "select count(*) as outbound990Count from files where TRANSACTION_TYPE = '990' and DIRECTION = 'OUTBOUND' and DATE_TIME_RECEIVED >=cast(? as timestamp) and DATE_TIME_RECEIVED <=cast(? as timestamp) and FLOWFLAG='" + flag + "'";
            System.out.println("EDIDocuments990OB query:" + ediDocuments990OBQuery.toString());
            preparedStatement = connection.prepareStatement(ediDocuments990OBQuery);
            for (int i = 0; i < timeInervalArray.size() - 1; i++) {
                int count = 0;
                String fromDate = sdf.format(new Date());
                fromDate = fromDate + (" " + timeInervalArray.get(i)) + ":00.000";
                String toDate = sdf.format(new Date());
                toDate = toDate + (" " + timeInervalArray.get(i + 1)) + ":00.000";

                preparedStatement.setString(1, fromDate);
                preparedStatement.setString(2, toDate);
                resultSet = preparedStatement.executeQuery();
                System.out.println("Query and resultset start time::" + DateUtility.getInstance().getCurrentDB2Timestamp());
                while (resultSet.next()) {
                    count = resultSet.getInt("outbound990Count");
                }
                System.out.println("Resultset end time::" + DateUtility.getInstance().getCurrentDB2Timestamp());
                outbound990.add(count);
                resultSet.close();
                resultSet = null;
            }
        } catch (SQLException sqlexception) {
            LoggerUtility.log(logger, "sqlException occurred in getEDIDocuments990OB method:: " + sqlexception.getMessage(), Level.ERROR, sqlexception.getCause());
        } catch (ServiceLocatorException serviceLocatorException) {
            LoggerUtility.log(logger, "Exception occurred in getEDIDocuments990OB method:: " + serviceLocatorException.getMessage(), Level.ERROR, serviceLocatorException.getCause());
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
                LoggerUtility.log(logger, "sqlException occurred in getEDIDocuments990OB method:: " + sqlexception.getMessage(), Level.ERROR, sqlexception.getCause());
            }
        }
        return outbound990;
    }

    @Override
    public ArrayList getEDIDocuments214IB(ArrayList timeInervalArray, SimpleDateFormat sdf, char flag) {
        ArrayList inbound214 = new ArrayList();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            System.out.println("Before Connection::" + DateUtility.getInstance().getCurrentDB2Timestamp());
            connection = ConnectionProvider.getInstance().getConnection();
            System.out.println("After Connection::" + DateUtility.getInstance().getCurrentDB2Timestamp());
            String ediDocuments214IBQuery = "select count(*) as inbound214Count from files where TRANSACTION_TYPE = '214' and DIRECTION = 'INBOUND' and DATE_TIME_RECEIVED >=cast(? as timestamp) and DATE_TIME_RECEIVED <=cast(? as timestamp) and FLOWFLAG='" + flag + "'";
            System.out.println("EDIDocuments214IB query:" + ediDocuments214IBQuery.toString());
            preparedStatement = connection.prepareStatement(ediDocuments214IBQuery);
            for (int i = 0; i < timeInervalArray.size() - 1; i++) {
                int count = 0;
                String fromDate = sdf.format(new Date());
                fromDate = fromDate + (" " + timeInervalArray.get(i)) + ":00.000";
                String toDate = sdf.format(new Date());
                toDate = toDate + (" " + timeInervalArray.get(i + 1)) + ":00.000";

                preparedStatement.setString(1, fromDate);
                preparedStatement.setString(2, toDate);
                resultSet = preparedStatement.executeQuery();
                System.out.println("Query and resultset start time::" + DateUtility.getInstance().getCurrentDB2Timestamp());
                while (resultSet.next()) {
                    count = resultSet.getInt("inbound214Count");
                }
                System.out.println("Resultset end time::" + DateUtility.getInstance().getCurrentDB2Timestamp());
                inbound214.add(count);
                resultSet.close();
                resultSet = null;
            }
        } catch (SQLException sqlexception) {
            LoggerUtility.log(logger, "sqlException occurred in getEDIDocuments214IB method:: " + sqlexception.getMessage(), Level.ERROR, sqlexception.getCause());
        } catch (ServiceLocatorException serviceLocatorException) {
            LoggerUtility.log(logger, "Exception occurred in getEDIDocuments214IB method:: " + serviceLocatorException.getMessage(), Level.ERROR, serviceLocatorException.getCause());
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
                LoggerUtility.log(logger, "sqlException occurred in getEDIDocuments214IB method:: " + sqlexception.getMessage(), Level.ERROR, sqlexception.getCause());
            }
        }
        return inbound214;
    }

    @Override
    public ArrayList getEDIDocuments214OB(ArrayList timeInervalArray, SimpleDateFormat sdf, char flag) {
        ArrayList outbound214 = new ArrayList();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            System.out.println("Before Connection::" + DateUtility.getInstance().getCurrentDB2Timestamp());
            connection = ConnectionProvider.getInstance().getConnection();
            System.out.println("After Connection::" + DateUtility.getInstance().getCurrentDB2Timestamp());
            String ediDocuments214OBQuery = "select count(*) as outbound214Count from files where TRANSACTION_TYPE = '214' and DIRECTION = 'OUTBOUND' and DATE_TIME_RECEIVED >=cast(? as timestamp) and DATE_TIME_RECEIVED <=cast(? as timestamp) and FLOWFLAG='" + flag + "'";
            System.out.println("EDIDocuments214OB query:" + ediDocuments214OBQuery.toString());
            preparedStatement = connection.prepareStatement(ediDocuments214OBQuery);
            for (int i = 0; i < timeInervalArray.size() - 1; i++) {
                int count = 0;
                String fromDate = sdf.format(new Date());
                fromDate = fromDate + (" " + timeInervalArray.get(i)) + ":00.000";
                String toDate = sdf.format(new Date());
                toDate = toDate + (" " + timeInervalArray.get(i + 1)) + ":00.000";

                preparedStatement.setString(1, fromDate);
                preparedStatement.setString(2, toDate);
                resultSet = preparedStatement.executeQuery();
                System.out.println("Query and resultset start time::" + DateUtility.getInstance().getCurrentDB2Timestamp());
                while (resultSet.next()) {
                    count = resultSet.getInt("outbound214Count");
                }
                System.out.println("Resultset end time::" + DateUtility.getInstance().getCurrentDB2Timestamp());
                outbound214.add(count);
                resultSet.close();
                resultSet = null;
            }
        } catch (SQLException sqlexception) {
            LoggerUtility.log(logger, "sqlException occurred in getEDIDocuments214OB method:: " + sqlexception.getMessage(), Level.ERROR, sqlexception.getCause());
        } catch (ServiceLocatorException serviceLocatorException) {
            LoggerUtility.log(logger, "Exception occurred in getEDIDocuments214OB method:: " + serviceLocatorException.getMessage(), Level.ERROR, serviceLocatorException.getCause());
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
                LoggerUtility.log(logger, "sqlException occurred in getEDIDocuments214OB method:: " + sqlexception.getMessage(), Level.ERROR, sqlexception.getCause());
            }
        }
        return outbound214;
    }

    @Override
    public ArrayList getEDIDocuments210IB(ArrayList timeInervalArray, SimpleDateFormat sdf, char flag) {
        ArrayList inbound210 = new ArrayList();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            System.out.println("Before Connection::" + DateUtility.getInstance().getCurrentDB2Timestamp());
            connection = ConnectionProvider.getInstance().getConnection();
            System.out.println("After Connection::" + DateUtility.getInstance().getCurrentDB2Timestamp());
            String ediDocuments210IBQuery = "select count(*) as inbound210Count from files where TRANSACTION_TYPE = '210' and DIRECTION = 'INBOUND' and DATE_TIME_RECEIVED >=cast(? as timestamp) and DATE_TIME_RECEIVED <=cast(? as timestamp) and FLOWFLAG='" + flag + "'";
            System.out.println("EDIDocuments210IB query:" + ediDocuments210IBQuery.toString());
            preparedStatement = connection.prepareStatement(ediDocuments210IBQuery);
            for (int i = 0; i < timeInervalArray.size() - 1; i++) {
                int count = 0;
                String fromDate = sdf.format(new Date());
                fromDate = fromDate + (" " + timeInervalArray.get(i)) + ":00.000";
                String toDate = sdf.format(new Date());
                toDate = toDate + (" " + timeInervalArray.get(i + 1)) + ":00.000";

                preparedStatement.setString(1, fromDate);
                preparedStatement.setString(2, toDate);
                resultSet = preparedStatement.executeQuery();
                System.out.println("Query and resultset start time::" + DateUtility.getInstance().getCurrentDB2Timestamp());
                while (resultSet.next()) {
                    count = resultSet.getInt("inbound210Count");
                }
                System.out.println("Resultset end time::" + DateUtility.getInstance().getCurrentDB2Timestamp());
                inbound210.add(count);
                resultSet.close();
                resultSet = null;
            }
        } catch (SQLException sqlexception) {
            LoggerUtility.log(logger, "sqlException occurred in getEDIDocuments210IB method:: " + sqlexception.getMessage(), Level.ERROR, sqlexception.getCause());
        } catch (ServiceLocatorException serviceLocatorException) {
            LoggerUtility.log(logger, "Exception occurred in getEDIDocuments210IB method:: " + serviceLocatorException.getMessage(), Level.ERROR, serviceLocatorException.getCause());
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
                LoggerUtility.log(logger, "sqlException occurred in getEDIDocuments214IB method:: " + sqlexception.getMessage(), Level.ERROR, sqlexception.getCause());
            }
        }
        return inbound210;
    }

    @Override
    public ArrayList getEDIDocuments210OB(ArrayList timeInervalArray, SimpleDateFormat sdf, char flag) {
        ArrayList outbound210 = new ArrayList();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            System.out.println("Before Connection::" + DateUtility.getInstance().getCurrentDB2Timestamp());
            connection = ConnectionProvider.getInstance().getConnection();
            System.out.println("After Connection::" + DateUtility.getInstance().getCurrentDB2Timestamp());
            String ediDocuments210OBQuery = "select count(*) as outbound210Count from files where TRANSACTION_TYPE = '210' and DIRECTION = 'OUTBOUND' and DATE_TIME_RECEIVED >=cast(? as timestamp) and DATE_TIME_RECEIVED <=cast(? as timestamp) and FLOWFLAG='" + flag + "'";
            System.out.println("EDIDocuments210OB query:" + ediDocuments210OBQuery.toString());
            preparedStatement = connection.prepareStatement(ediDocuments210OBQuery);
            for (int i = 0; i < timeInervalArray.size() - 1; i++) {
                int count = 0;
                String fromDate = sdf.format(new Date());
                fromDate = fromDate + (" " + timeInervalArray.get(i)) + ":00.000";
                String toDate = sdf.format(new Date());
                toDate = toDate + (" " + timeInervalArray.get(i + 1)) + ":00.000";

                preparedStatement.setString(1, fromDate);
                preparedStatement.setString(2, toDate);
                resultSet = preparedStatement.executeQuery();
                System.out.println("Query and resultset start time::" + DateUtility.getInstance().getCurrentDB2Timestamp());
                while (resultSet.next()) {
                    count = resultSet.getInt("outbound210Count");
                }
                System.out.println("Resultset end time::" + DateUtility.getInstance().getCurrentDB2Timestamp());
                outbound210.add(count);
                resultSet.close();
                resultSet = null;
            }
        } catch (SQLException sqlexception) {
            LoggerUtility.log(logger, "sqlException occurred in getEDIDocuments210OB method:: " + sqlexception.getMessage(), Level.ERROR, sqlexception.getCause());
        } catch (ServiceLocatorException serviceLocatorException) {
            LoggerUtility.log(logger, "Exception occurred in getEDIDocuments210OB method:: " + serviceLocatorException.getMessage(), Level.ERROR, serviceLocatorException.getCause());
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
                LoggerUtility.log(logger, "sqlException occurred in getEDIDocuments210OB method:: " + sqlexception.getMessage(), Level.ERROR, sqlexception.getCause());
            }
        }
        return outbound210;
    }

    @Override
    public ArrayList getEDIDocuments850IB(ArrayList timeInervalArray, SimpleDateFormat sdf, char flag) {
        ArrayList inbound850 = new ArrayList();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            System.out.println("Before Connection::" + DateUtility.getInstance().getCurrentDB2Timestamp());
            connection = ConnectionProvider.getInstance().getConnection();
            System.out.println("After Connection::" + DateUtility.getInstance().getCurrentDB2Timestamp());
            String ediDocuments850IBQuery = "select count(*) as inbound850Count from files where TRANSACTION_TYPE ='850' and DIRECTION ='INBOUND' and DATE_TIME_RECEIVED >=cast(? as timestamp) and DATE_TIME_RECEIVED <=cast(? as timestamp) and FLOWFLAG='" + flag + "'";
            System.out.println("EDIDocuments850IB query:" + ediDocuments850IBQuery.toString());
            preparedStatement = connection.prepareStatement(ediDocuments850IBQuery);
            for (int i = 0; i < timeInervalArray.size() - 1; i++) {
                int count = 0;
                String fromDate = sdf.format(new Date());
                fromDate = fromDate + (" " + timeInervalArray.get(i)) + ":00.000";
                String toDate = sdf.format(new Date());
                toDate = toDate + (" " + timeInervalArray.get(i + 1)) + ":00.000";

                preparedStatement.setString(1, fromDate);
                preparedStatement.setString(2, toDate);
                resultSet = preparedStatement.executeQuery();
                System.out.println("Query and resultset start time::" + DateUtility.getInstance().getCurrentDB2Timestamp());
                while (resultSet.next()) {
                    count = resultSet.getInt("inbound850Count");
                }
                System.out.println("Resultset end time::" + DateUtility.getInstance().getCurrentDB2Timestamp());
                inbound850.add(count);
                resultSet.close();
                resultSet = null;
            }
        } catch (SQLException sqlexception) {
            LoggerUtility.log(logger, "sqlException occurred in getEDIDocuments850IB method:: " + sqlexception.getMessage(), Level.ERROR, sqlexception.getCause());
        } catch (ServiceLocatorException serviceLocatorException) {
            LoggerUtility.log(logger, "Exception occurred in getEDIDocuments850IB method:: " + serviceLocatorException.getMessage(), Level.ERROR, serviceLocatorException.getCause());
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
                LoggerUtility.log(logger, "sqlException occurred in getEDIDocuments850IB method:: " + sqlexception.getMessage(), Level.ERROR, sqlexception.getCause());
            }
        }
        return inbound850;
    }

    @Override
    public ArrayList getEDIDocuments850OB(ArrayList timeInervalArray, SimpleDateFormat sdf, char flag) {
        ArrayList outbound850 = new ArrayList();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            System.out.println("Before Connection::" + DateUtility.getInstance().getCurrentDB2Timestamp());
            connection = ConnectionProvider.getInstance().getConnection();
            System.out.println("After Connection::" + DateUtility.getInstance().getCurrentDB2Timestamp());
            String ediDocuments850OBQuery = "select count(*) as outbound850Count from files where TRANSACTION_TYPE = '850' and DIRECTION = 'OUTBOUND' and DATE_TIME_RECEIVED >=cast(? as timestamp) and DATE_TIME_RECEIVED <=cast(? as timestamp) and FLOWFLAG='" + flag + "'";
            System.out.println("EDIDocuments850OB query:" + ediDocuments850OBQuery.toString());
            preparedStatement = connection.prepareStatement(ediDocuments850OBQuery);
            for (int i = 0; i < timeInervalArray.size() - 1; i++) {
                int count = 0;
                String fromDate = sdf.format(new Date());
                fromDate = fromDate + (" " + timeInervalArray.get(i)) + ":00.000";
                String toDate = sdf.format(new Date());
                toDate = toDate + (" " + timeInervalArray.get(i + 1)) + ":00.000";

                preparedStatement.setString(1, fromDate);
                preparedStatement.setString(2, toDate);
                resultSet = preparedStatement.executeQuery();
                System.out.println("Query and resultset start time::" + DateUtility.getInstance().getCurrentDB2Timestamp());
                while (resultSet.next()) {
                    count = resultSet.getInt("outbound850Count");
                }
                System.out.println("Resultset end time::" + DateUtility.getInstance().getCurrentDB2Timestamp());
                outbound850.add(count);
                resultSet.close();
                resultSet = null;
            }
        } catch (SQLException sqlexception) {
            LoggerUtility.log(logger, "sqlException occurred in getEDIDocuments850OB method:: " + sqlexception.getMessage(), Level.ERROR, sqlexception.getCause());
        } catch (ServiceLocatorException serviceLocatorException) {
            LoggerUtility.log(logger, "Exception occurred in getEDIDocuments850OB method:: " + serviceLocatorException.getMessage(), Level.ERROR, serviceLocatorException.getCause());
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
                LoggerUtility.log(logger, "sqlException occurred in getEDIDocuments850IB method:: " + sqlexception.getMessage(), Level.ERROR, sqlexception.getCause());
            }
        }
        return outbound850;
    }

    @Override
    public ArrayList getEDIDocuments810IB(ArrayList timeInervalArray, SimpleDateFormat sdf, char flag) {
        ArrayList inbound810 = new ArrayList();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            System.out.println("Before Connection::" + DateUtility.getInstance().getCurrentDB2Timestamp());
            connection = ConnectionProvider.getInstance().getConnection();
            System.out.println("After Connection::" + DateUtility.getInstance().getCurrentDB2Timestamp());
            String ediDocuments810IBQuery = "select count(*) as inbound810Count from files where TRANSACTION_TYPE ='810' and DIRECTION ='INBOUND' and DATE_TIME_RECEIVED >=cast(? as timestamp) and DATE_TIME_RECEIVED <=cast(? as timestamp) and FLOWFLAG='" + flag + "'";
            System.out.println("EDIDocuments810IB query:" + ediDocuments810IBQuery.toString());
            preparedStatement = connection.prepareStatement(ediDocuments810IBQuery);
            for (int i = 0; i < timeInervalArray.size() - 1; i++) {
                int count = 0;
                String fromDate = sdf.format(new Date());
                fromDate = fromDate + (" " + timeInervalArray.get(i)) + ":00.000";
                String toDate = sdf.format(new Date());
                toDate = toDate + (" " + timeInervalArray.get(i + 1)) + ":00.000";

                preparedStatement.setString(1, fromDate);
                preparedStatement.setString(2, toDate);
                resultSet = preparedStatement.executeQuery();
                System.out.println("Query and resultset start time::" + DateUtility.getInstance().getCurrentDB2Timestamp());
                while (resultSet.next()) {
                    count = resultSet.getInt("inbound810Count");
                }
                System.out.println("Resultset end time::" + DateUtility.getInstance().getCurrentDB2Timestamp());
                inbound810.add(count);
                resultSet.close();
                resultSet = null;
            }
        } catch (SQLException sqlexception) {
            LoggerUtility.log(logger, "sqlException occurred in getEDIDocuments850IB method:: " + sqlexception.getMessage(), Level.ERROR, sqlexception.getCause());
        } catch (ServiceLocatorException serviceLocatorException) {
            LoggerUtility.log(logger, "Exception occurred in getEDIDocuments850IB method:: " + serviceLocatorException.getMessage(), Level.ERROR, serviceLocatorException.getCause());
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
                LoggerUtility.log(logger, "sqlException occurred in getEDIDocuments850IB method:: " + sqlexception.getMessage(), Level.ERROR, sqlexception.getCause());
            }
        }
        return inbound810;
    }

    @Override
    public ArrayList getEDIDocuments810OB(ArrayList timeInervalArray, SimpleDateFormat sdf, char flag) {
        ArrayList outbound810 = new ArrayList();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            System.out.println("Before Connection::" + DateUtility.getInstance().getCurrentDB2Timestamp());
            connection = ConnectionProvider.getInstance().getConnection();
            System.out.println("After Connection::" + DateUtility.getInstance().getCurrentDB2Timestamp());
            String ediDocuments810OBQuery = "select count(*) as outbound810Count from files where TRANSACTION_TYPE = '810' and DIRECTION = 'OUTBOUND' and DATE_TIME_RECEIVED >=cast(? as timestamp) and DATE_TIME_RECEIVED <=cast(? as timestamp) and FLOWFLAG='" + flag + "'";
            System.out.println("EDIDocuments810OB query:" + ediDocuments810OBQuery.toString());
            preparedStatement = connection.prepareStatement(ediDocuments810OBQuery);
            for (int i = 0; i < timeInervalArray.size() - 1; i++) {
                int count = 0;
                String fromDate = sdf.format(new Date());
                fromDate = fromDate + (" " + timeInervalArray.get(i)) + ":00.000";
                String toDate = sdf.format(new Date());
                toDate = toDate + (" " + timeInervalArray.get(i + 1)) + ":00.000";

                preparedStatement.setString(1, fromDate);
                preparedStatement.setString(2, toDate);
                resultSet = preparedStatement.executeQuery();
                System.out.println("Query and resultset start time::" + DateUtility.getInstance().getCurrentDB2Timestamp());
                while (resultSet.next()) {
                    count = resultSet.getInt("outbound810Count");
                }
                System.out.println("Resultset end time::" + DateUtility.getInstance().getCurrentDB2Timestamp());
                outbound810.add(count);
                resultSet.close();
                resultSet = null;
            }
        } catch (SQLException sqlexception) {
            LoggerUtility.log(logger, "sqlException occurred in getEDIDocuments810OB method:: " + sqlexception.getMessage(), Level.ERROR, sqlexception.getCause());
        } catch (ServiceLocatorException serviceLocatorException) {
            LoggerUtility.log(logger, "Exception occurred in getEDIDocuments810OB method:: " + serviceLocatorException.getMessage(), Level.ERROR, serviceLocatorException.getCause());
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
                LoggerUtility.log(logger, "sqlException occurred in getEDIDocuments810OB method:: " + sqlexception.getMessage(), Level.ERROR, sqlexception.getCause());
            }
        }
        return outbound810;
    }

    @Override
    public ArrayList getEDIDocuments856IB(ArrayList timeInervalArray, SimpleDateFormat sdf, char flag) {
        ArrayList inbound856 = new ArrayList();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            System.out.println("Before Connection::" + DateUtility.getInstance().getCurrentDB2Timestamp());
            connection = ConnectionProvider.getInstance().getConnection();
            System.out.println("After Connection::" + DateUtility.getInstance().getCurrentDB2Timestamp());
            String ediDocuments856IBQuery = "select count(*) as inbound856Count from files where TRANSACTION_TYPE ='856' and DIRECTION ='INBOUND' and DATE_TIME_RECEIVED >=cast(? as timestamp) and DATE_TIME_RECEIVED <=cast(? as timestamp) and FLOWFLAG='" + flag + "'";
            System.out.println("EDIDocuments856IB query:" + ediDocuments856IBQuery.toString());
            preparedStatement = connection.prepareStatement(ediDocuments856IBQuery);
            for (int i = 0; i < timeInervalArray.size() - 1; i++) {
                int count = 0;
                String fromDate = sdf.format(new Date());
                fromDate = fromDate + (" " + timeInervalArray.get(i)) + ":00.000";
                String toDate = sdf.format(new Date());
                toDate = toDate + (" " + timeInervalArray.get(i + 1)) + ":00.000";

                preparedStatement.setString(1, fromDate);
                preparedStatement.setString(2, toDate);
                resultSet = preparedStatement.executeQuery();
                System.out.println("Query and resultset start time::" + DateUtility.getInstance().getCurrentDB2Timestamp());
                while (resultSet.next()) {
                    count = resultSet.getInt("inbound856Count");
                }
                System.out.println("Resultset end time::" + DateUtility.getInstance().getCurrentDB2Timestamp());
                inbound856.add(count);
                resultSet.close();
                resultSet = null;
            }
        } catch (SQLException sqlexception) {
            LoggerUtility.log(logger, "sqlException occurred in getEDIDocuments856IB method:: " + sqlexception.getMessage(), Level.ERROR, sqlexception.getCause());
        } catch (ServiceLocatorException serviceLocatorException) {
            LoggerUtility.log(logger, "Exception occurred in getEDIDocuments856IB method:: " + serviceLocatorException.getMessage(), Level.ERROR, serviceLocatorException.getCause());
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
                LoggerUtility.log(logger, "sqlException occurred in getEDIDocuments856IB method:: " + sqlexception.getMessage(), Level.ERROR, sqlexception.getCause());
            }
        }
        return inbound856;
    }

    @Override
    public ArrayList getEDIDocuments856OB(ArrayList timeInervalArray, SimpleDateFormat sdf, char flag) {
        ArrayList outbound856 = new ArrayList();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            System.out.println("Before Connection::" + DateUtility.getInstance().getCurrentDB2Timestamp());
            connection = ConnectionProvider.getInstance().getConnection();
            System.out.println("After Connection::" + DateUtility.getInstance().getCurrentDB2Timestamp());
            String ediDocuments856OBQuery = "select count(*) as outbound856Count from files where TRANSACTION_TYPE = '856' and DIRECTION = 'OUTBOUND' and DATE_TIME_RECEIVED >=cast(? as timestamp) and DATE_TIME_RECEIVED <=cast(? as timestamp) and FLOWFLAG='" + flag + "'";
            System.out.println("EDIDocuments856OB query:" + ediDocuments856OBQuery.toString());
            preparedStatement = connection.prepareStatement(ediDocuments856OBQuery);
            for (int i = 0; i < timeInervalArray.size() - 1; i++) {
                int count = 0;
                String fromDate = sdf.format(new Date());
                fromDate = fromDate + (" " + timeInervalArray.get(i)) + ":00.000";
                String toDate = sdf.format(new Date());
                toDate = toDate + (" " + timeInervalArray.get(i + 1)) + ":00.000";

                preparedStatement.setString(1, fromDate);
                preparedStatement.setString(2, toDate);
                resultSet = preparedStatement.executeQuery();
                System.out.println("Query and resultset start time::" + DateUtility.getInstance().getCurrentDB2Timestamp());
                while (resultSet.next()) {
                    count = resultSet.getInt("outbound856Count");
                }
                System.out.println("Resultset end time::" + DateUtility.getInstance().getCurrentDB2Timestamp());
                outbound856.add(count);
                resultSet.close();
                resultSet = null;
            }
        } catch (SQLException sqlexception) {
            LoggerUtility.log(logger, "sqlException occurred in getEDIDocuments856OB method:: " + sqlexception.getMessage(), Level.ERROR, sqlexception.getCause());
        } catch (ServiceLocatorException serviceLocatorException) {
            LoggerUtility.log(logger, "Exception occurred in getEDIDocuments856OB method:: " + serviceLocatorException.getMessage(), Level.ERROR, serviceLocatorException.getCause());
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
                LoggerUtility.log(logger, "sqlException occurred in getEDIDocuments856OB method:: " + sqlexception.getMessage(), Level.ERROR, sqlexception.getCause());
            }
        }
        return outbound856;
    }

    @Override
    public ArrayList getEDIDocuments855IB(ArrayList timeInervalArray, SimpleDateFormat sdf, char flag) {
        ArrayList inbound855 = new ArrayList();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            System.out.println("Before Connection::" + DateUtility.getInstance().getCurrentDB2Timestamp());
            connection = ConnectionProvider.getInstance().getConnection();
            System.out.println("After Connection::" + DateUtility.getInstance().getCurrentDB2Timestamp());
            String ediDocuments855IBQuery = "select count(*) as inbound855Count from files where TRANSACTION_TYPE ='855' and DIRECTION ='INBOUND' and DATE_TIME_RECEIVED >=cast(? as timestamp) and DATE_TIME_RECEIVED <=cast(? as timestamp) and FLOWFLAG='" + flag + "'";
            System.out.println("EDIDocuments855IB query:" + ediDocuments855IBQuery.toString());
            preparedStatement = connection.prepareStatement(ediDocuments855IBQuery);
            for (int i = 0; i < timeInervalArray.size() - 1; i++) {
                int count = 0;
                String fromDate = sdf.format(new Date());
                fromDate = fromDate + (" " + timeInervalArray.get(i)) + ":00.000";
                String toDate = sdf.format(new Date());
                toDate = toDate + (" " + timeInervalArray.get(i + 1)) + ":00.000";

                preparedStatement.setString(1, fromDate);
                preparedStatement.setString(2, toDate);
                resultSet = preparedStatement.executeQuery();
                System.out.println("Query and resultset start time::" + DateUtility.getInstance().getCurrentDB2Timestamp());
                while (resultSet.next()) {
                    count = resultSet.getInt("inbound855Count");
                }
                System.out.println("Resultset end time::" + DateUtility.getInstance().getCurrentDB2Timestamp());
                inbound855.add(count);
                resultSet.close();
                resultSet = null;
            }
        } catch (SQLException sqlexception) {
            LoggerUtility.log(logger, "sqlException occurred in getEDIDocuments855IB method:: " + sqlexception.getMessage(), Level.ERROR, sqlexception.getCause());
        } catch (ServiceLocatorException serviceLocatorException) {
            LoggerUtility.log(logger, "Exception occurred in getEDIDocuments855IB method:: " + serviceLocatorException.getMessage(), Level.ERROR, serviceLocatorException.getCause());
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
                LoggerUtility.log(logger, "sqlException occurred in getEDIDocuments855IB method:: " + sqlexception.getMessage(), Level.ERROR, sqlexception.getCause());
            }
        }
        return inbound855;
    }

    @Override
    public ArrayList getEDIDocuments855OB(ArrayList timeInervalArray, SimpleDateFormat sdf, char flag) {
        ArrayList outbound855 = new ArrayList();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            System.out.println("Before Connection::" + DateUtility.getInstance().getCurrentDB2Timestamp());
            connection = ConnectionProvider.getInstance().getConnection();
            System.out.println("After Connection::" + DateUtility.getInstance().getCurrentDB2Timestamp());
            String ediDocuments855OBQuery = "select count(*) as outbound855Count from files where TRANSACTION_TYPE = '855' and DIRECTION = 'OUTBOUND' and DATE_TIME_RECEIVED >=cast(? as timestamp) and DATE_TIME_RECEIVED <=cast(? as timestamp) and FLOWFLAG='" + flag + "'";
            System.out.println("EDIDocuments855OB query:" + ediDocuments855OBQuery.toString());
            preparedStatement = connection.prepareStatement(ediDocuments855OBQuery);
            for (int i = 0; i < timeInervalArray.size() - 1; i++) {
                int count = 0;
                String fromDate = sdf.format(new Date());
                fromDate = fromDate + (" " + timeInervalArray.get(i)) + ":00.000";
                String toDate = sdf.format(new Date());
                toDate = toDate + (" " + timeInervalArray.get(i + 1)) + ":00.000";

                preparedStatement.setString(1, fromDate);
                preparedStatement.setString(2, toDate);
                resultSet = preparedStatement.executeQuery();
                System.out.println("Query and resultset start time::" + DateUtility.getInstance().getCurrentDB2Timestamp());
                while (resultSet.next()) {
                    count = resultSet.getInt("outbound855Count");
                }
                System.out.println("Resultset end time::" + DateUtility.getInstance().getCurrentDB2Timestamp());
                outbound855.add(count);
                resultSet.close();
                resultSet = null;
            }
        } catch (SQLException sqlexception) {
            LoggerUtility.log(logger, "sqlException occurred in getEDIDocuments855OB method:: " + sqlexception.getMessage(), Level.ERROR, sqlexception.getCause());
        } catch (ServiceLocatorException serviceLocatorException) {
            LoggerUtility.log(logger, "Exception occurred in getEDIDocuments855OB method:: " + serviceLocatorException.getMessage(), Level.ERROR, serviceLocatorException.getCause());
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
                LoggerUtility.log(logger, "sqlException occurred in getEDIDocuments855OB method:: " + sqlexception.getMessage(), Level.ERROR, sqlexception.getCause());
            }
        }
        return outbound855;
    }

    @Override
    public ArrayList getEdiBusinessYearsTrendsResult(DashBoardAction dashBoardAction) {

        StringBuilder ediBusinessYearsTrendsResultQuery = new StringBuilder();
        ArrayList ediBusinessTrendsList = new ArrayList();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String year = dashBoardAction.getFromYear();
        List fromYear = new ArrayList();
        int count = 0;
        try {

            if (dashBoardAction.getBusinessFlows().equalsIgnoreCase("Logistics")) {
                ediBusinessYearsTrendsResultQuery.append("select count(DIRECTION) as total from FILES where DIRECTION=? and TRANSACTION_TYPE=? and FLOWFLAG='L'");
            } else {
                ediBusinessYearsTrendsResultQuery.append("select count(DIRECTION) as total from FILES where DIRECTION=? and TRANSACTION_TYPE=? and FLOWFLAG='M'");
            }
            if (year != null && !"-1".equals(year)) {
                ediBusinessYearsTrendsResultQuery.append(" and month(DATE_TIME_RECEIVED)=? and year(DATE_TIME_RECEIVED)=? ");
                fromYear = DateUtility.getInstance().getYearofMonth(year);
            }
            connection = ConnectionProvider.getInstance().getConnection();
            preparedStatement = connection.prepareStatement(ediBusinessYearsTrendsResultQuery.toString());
            for (int k = 1; k <= fromYear.size(); k++) {

                preparedStatement.setString(1, dashBoardAction.getDirection());
                preparedStatement.setString(2, dashBoardAction.getDocType());
                preparedStatement.setInt(3, k);
                preparedStatement.setString(4, year);
                resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    count = resultSet.getInt("total");
                }
                ediBusinessTrendsList.add(count);
                resultSet.close();
                resultSet = null;
            }

        } catch (SQLException sqlexception) {
            LoggerUtility.log(logger, "sqlException occurred in getEdiBusinessYearsTrendsResult method:: " + sqlexception.getMessage(), Level.ERROR, sqlexception.getCause());
        } catch (ServiceLocatorException serviceLocatorException) {
            LoggerUtility.log(logger, "Exception occurred in getEdiBusinessYearsTrendsResult method:: " + serviceLocatorException.getMessage(), Level.ERROR, serviceLocatorException.getCause());
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
                LoggerUtility.log(logger, "sqlException occurred in getEdiBusinessYearsTrendsResult method:: " + sqlexception.getMessage(), Level.ERROR, sqlexception.getCause());
            }
        }

        return ediBusinessTrendsList;
    }

    @Override
    public ArrayList getEdiBusinessMonthsTrendsResult(DashBoardAction dashBoardAction) throws ServiceLocatorException {

        StringBuilder ediBusinessMonthsTrendsResultQuery = new StringBuilder();
        ArrayList ediBusinessTrendsList = new ArrayList();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String year = dashBoardAction.getFromYear();
        String month = dashBoardAction.getFromMonth();
        List fromDates = new ArrayList();
        int count = 0;
        try {

            if (dashBoardAction.getBusinessFlows().equalsIgnoreCase("Logistics")) {
                ediBusinessMonthsTrendsResultQuery.append("select count(DIRECTION) as total from FILES where DIRECTION=? and TRANSACTION_TYPE=? and FLOWFLAG='L'");
            } else {
                ediBusinessMonthsTrendsResultQuery.append("select count(DIRECTION) as total from FILES where DIRECTION=? and TRANSACTION_TYPE=? and FLOWFLAG='R'");
            }
            if (year != null && !"-1".equals(year) && month != null && !"-1".equals(month)) {
                ediBusinessMonthsTrendsResultQuery.append(" and DATE(DATE_TIME_RECEIVED) =  ?");
                fromDates = DateUtility.getInstance().getMonthofDate(year, month);
            }
            connection = ConnectionProvider.getInstance().getConnection();
            preparedStatement = connection.prepareStatement(ediBusinessMonthsTrendsResultQuery.toString());
            for (int k = 1; k <= fromDates.size(); k++) {
                String date = year + "-" + month + "-" + k;
                preparedStatement.setString(1, dashBoardAction.getDirection());
                preparedStatement.setString(2, dashBoardAction.getDocType());
                preparedStatement.setString(3, date);
                resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    count = resultSet.getInt("total");
                }
                ediBusinessTrendsList.add(count);
                resultSet.close();
                resultSet = null;
            }

        } catch (SQLException sqlexception) {
            LoggerUtility.log(logger, "sqlException occurred in getEdiBusinessMonthsTrendsResult method:: " + sqlexception.getMessage(), Level.ERROR, sqlexception.getCause());
        } catch (ServiceLocatorException serviceLocatorException) {
            LoggerUtility.log(logger, "Exception occurred in getEdiBusinessMonthsTrendsResult method:: " + serviceLocatorException.getMessage(), Level.ERROR, serviceLocatorException.getCause());
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
                LoggerUtility.log(logger, "sqlException occurred in getEdiBusinessMonthsTrendsResult method:: " + sqlexception.getMessage(), Level.ERROR, sqlexception.getCause());
            }
        }

        return ediBusinessTrendsList;
    }

    @Override
    public ArrayList getTpBusinessYearsTrendsResult(DashBoardAction dashBoardAction) {
        StringBuilder tpBusinessYearsTrendsResultQuery = new StringBuilder();
        ArrayList tpBusinessTrendsList = new ArrayList();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String year = dashBoardAction.getFromYear();
        List fromYear = new ArrayList();
        int count = 0;
        try {
            tpBusinessYearsTrendsResultQuery.append("select count(DIRECTION) as total from FILES where (SENDER_ID = ? or RECEIVER_ID=?) and DIRECTION=? and TRANSACTION_TYPE=?");
            if (year != null && !"-1".equals(year)) {
                tpBusinessYearsTrendsResultQuery.append(" and month(DATE_TIME_RECEIVED)=? and year(DATE_TIME_RECEIVED)=? ");
                fromYear = DateUtility.getInstance().getYearofMonth(year);
            }
            connection = ConnectionProvider.getInstance().getConnection();
            preparedStatement = connection.prepareStatement(tpBusinessYearsTrendsResultQuery.toString());
            for (int k = 1; k <= fromYear.size(); k++) {
                preparedStatement.setString(1, dashBoardAction.getTpPartnerId());
                preparedStatement.setString(2, dashBoardAction.getTpPartnerId());
                preparedStatement.setString(3, dashBoardAction.getDirection());
                preparedStatement.setString(4, dashBoardAction.getDocType());
                preparedStatement.setInt(5, k);
                preparedStatement.setString(6, year);
                resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    count = resultSet.getInt("total");
                }
                tpBusinessTrendsList.add(count);
                resultSet.close();
                resultSet = null;
            }

        } catch (SQLException sqlexception) {
            LoggerUtility.log(logger, "sqlException occurred in getTpBusinessYearsTrendsResult method:: " + sqlexception.getMessage(), Level.ERROR, sqlexception.getCause());
        } catch (ServiceLocatorException serviceLocatorException) {
            LoggerUtility.log(logger, "Exception occurred in getTpBusinessYearsTrendsResult method:: " + serviceLocatorException.getMessage(), Level.ERROR, serviceLocatorException.getCause());
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
                LoggerUtility.log(logger, "sqlException occurred in getTpBusinessYearsTrendsResult method:: " + sqlexception.getMessage(), Level.ERROR, sqlexception.getCause());
            }
        }
        return tpBusinessTrendsList;
    }

    @Override
    public ArrayList getTpBusinessMonthsTrendsResult(DashBoardAction dashBoardAction) throws ServiceLocatorException {
        StringBuilder tpBusinessMonthsTrendsResultQuery = new StringBuilder();
        ArrayList tpBusinessTrendsList = new ArrayList();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String year = dashBoardAction.getFromYear();
        String month = dashBoardAction.getFromMonth();
        List fromDates = new ArrayList();
        int count = 0;
        try {
            tpBusinessMonthsTrendsResultQuery.append("select count(DIRECTION) as total from FILES where (SENDER_ID = ? or RECEIVER_ID=?) and DIRECTION=? and TRANSACTION_TYPE=?");
            if (year != null && !"-1".equals(year) && month != null && !"-1".equals(month)) {
                tpBusinessMonthsTrendsResultQuery.append(" and DATE(DATE_TIME_RECEIVED) =  ?");
                fromDates = DateUtility.getInstance().getMonthofDate(year, month);
            }
            connection = ConnectionProvider.getInstance().getConnection();
            preparedStatement = connection.prepareStatement(tpBusinessMonthsTrendsResultQuery.toString());
            for (int k = 1; k <= fromDates.size(); k++) {
                String date = year + "-" + month + "-" + k;
                preparedStatement.setString(1, dashBoardAction.getTpPartnerId());
                preparedStatement.setString(2, dashBoardAction.getTpPartnerId());
                preparedStatement.setString(3, dashBoardAction.getDirection());
                preparedStatement.setString(4, dashBoardAction.getDocType());
                preparedStatement.setString(5, date);
                resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    count = resultSet.getInt("total");
                }
                tpBusinessTrendsList.add(count);
                resultSet.close();
                resultSet = null;
            }

        } catch (SQLException sqlexception) {
            LoggerUtility.log(logger, "sqlException occurred in getTpBusinessMonthsTrendsResult method:: " + sqlexception.getMessage(), Level.ERROR, sqlexception.getCause());
        } catch (ServiceLocatorException serviceLocatorException) {
            LoggerUtility.log(logger, "Exception occurred in getTpBusinessMonthsTrendsResult method:: " + serviceLocatorException.getMessage(), Level.ERROR, serviceLocatorException.getCause());
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
                LoggerUtility.log(logger, "sqlException occurred in getTpBusinessMonthsTrendsResult method:: " + sqlexception.getMessage(), Level.ERROR, sqlexception.getCause());
            }
        }
        return tpBusinessTrendsList;
    }

    @Override
    public boolean getTransactionalVolumes(ArrayList timeInervalArray, SimpleDateFormat sdf, DashBoardAction dashBoardAction) {
        String[] manufaturingTransctions = {"850", "855", "856", "810"};
        String[] logisticTransctions = {"204", "990", "214", "210"};
        String[] transactions = null;
        if (dashBoardAction.getFlag() == 'L') {
            transactions = logisticTransctions;
        } else {
            transactions = manufaturingTransctions;
        }
        String[] directions = {"INBOUND", "OUTBOUND",};
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        boolean transactionalVolumesDone = false;
        int count = 0;
        try {
            String transactionalVolumesQuery = "select count(*) as TRANS_COUNT from files where DATE_TIME_RECEIVED >=cast(? as timestamp) and DATE_TIME_RECEIVED <=cast(? as timestamp) and TRANSACTION_TYPE =? and DIRECTION =? and FLOWFLAG=?";
            connection = ConnectionProvider.getInstance().getConnection();
            preparedStatement = connection.prepareStatement(transactionalVolumesQuery);
            for (int j = 0; j < transactions.length; j++) {
                for (int k = 0; k < directions.length; k++) {
                    ArrayList transactionalCount = new ArrayList();
                    for (int i = 0; i < timeInervalArray.size() - 1; i++) {
                        //int count = 0;
                        String fromDate = sdf.format(new Date());
                        fromDate = fromDate + (" " + timeInervalArray.get(i)) + ":00.000";
                        String toDate = sdf.format(new Date());
                        toDate = toDate + (" " + timeInervalArray.get(i + 1)) + ":00.000";

                        preparedStatement.setString(1, fromDate);
                        preparedStatement.setString(2, toDate);
                        preparedStatement.setString(3, transactions[j]);
                        preparedStatement.setString(4, directions[k]);
                        preparedStatement.setString(5, Character.toString(dashBoardAction.getFlag()));
                        resultSet = preparedStatement.executeQuery();
                        while (resultSet.next()) {
                            count = resultSet.getInt("TRANS_COUNT");
                        }
                        transactionalCount.add(count);
                        resultSet.close();
                        resultSet = null;
                    }

                    if ("204".equalsIgnoreCase(transactions[j]) && "INBOUND".equals(directions[k])) {
                        dashBoardAction.setEdiDocuments204IB(transactionalCount);

                    } else if ("204".equalsIgnoreCase(transactions[j]) && "OUTBOUND".equals(directions[k])) {
                        dashBoardAction.setEdiDocuments204OB(transactionalCount);
                    } else if ("990".equalsIgnoreCase(transactions[j]) && "INBOUND".equals(directions[k])) {
                        dashBoardAction.setEdiDocuments990IB(transactionalCount);

                    } else if ("990".equalsIgnoreCase(transactions[j]) && "OUTBOUND".equals(directions[k])) {
                        dashBoardAction.setEdiDocuments990OB(transactionalCount);
                    } else if ("214".equalsIgnoreCase(transactions[j]) && "INBOUND".equals(directions[k])) {
                        dashBoardAction.setEdiDocuments214IB(transactionalCount);
                    } else if ("214".equalsIgnoreCase(transactions[j]) && "OUTBOUND".equals(directions[k])) {
                        dashBoardAction.setEdiDocuments214OB(transactionalCount);
                    } else if ("210".equalsIgnoreCase(transactions[j]) && "INBOUND".equals(directions[k])) {
                        dashBoardAction.setEdiDocuments210IB(transactionalCount);
                    } else if ("210".equalsIgnoreCase(transactions[j]) && "OUTBOUND".equals(directions[k])) {
                        dashBoardAction.setEdiDocuments210OB(transactionalCount);
                    } else if ("850".equalsIgnoreCase(transactions[j]) && "INBOUND".equals(directions[k])) {
                        dashBoardAction.setEdiDocuments850IB(transactionalCount);
                    } else if ("850".equalsIgnoreCase(transactions[j]) && "OUTBOUND".equals(directions[k])) {
                        dashBoardAction.setEdiDocuments850OB(transactionalCount);
                    } else if ("855".equalsIgnoreCase(transactions[j]) && "INBOUND".equals(directions[k])) {
                        dashBoardAction.setEdiDocuments855IB(transactionalCount);
                    } else if ("855".equalsIgnoreCase(transactions[j]) && "OUTBOUND".equals(directions[k])) {
                        dashBoardAction.setEdiDocuments855OB(transactionalCount);
                    } else if ("856".equalsIgnoreCase(transactions[j]) && "INBOUND".equals(directions[k])) {
                        dashBoardAction.setEdiDocuments856IB(transactionalCount);
                    } else if ("856".equalsIgnoreCase(transactions[j]) && "OUTBOUND".equals(directions[k])) {
                        dashBoardAction.setEdiDocuments856OB(transactionalCount);
                    } else if ("810".equalsIgnoreCase(transactions[j]) && "INBOUND".equals(directions[k])) {
                        dashBoardAction.setEdiDocuments810IB(transactionalCount);
                    } else if ("810".equalsIgnoreCase(transactions[j]) && "OUTBOUND".equals(directions[k])) {
                        dashBoardAction.setEdiDocuments810OB(transactionalCount);
                    }
                }
            }
            transactionalVolumesDone = true;
        } catch (SQLException sqlexception) {
            LoggerUtility.log(logger, "sqlException occurred in getEDIDocuments204IB method:: " + sqlexception.getMessage(), Level.ERROR, sqlexception.getCause());
        } catch (ServiceLocatorException serviceLocatorException) {
            LoggerUtility.log(logger, "Exception occurred in getEDIDocuments204IB method:: " + serviceLocatorException.getMessage(), Level.ERROR, serviceLocatorException.getCause());
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
                LoggerUtility.log(logger, "sqlException occurred in getEDIDocuments204IB method:: " + sqlexception.getMessage(), Level.ERROR, sqlexception.getCause());
            }
        }
        return transactionalVolumesDone;
    }
}
