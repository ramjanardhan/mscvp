/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mss.ediscv.schdular;

import com.mss.ediscv.util.ConnectionProvider;
import com.mss.ediscv.util.DateUtility;
import com.mss.ediscv.util.PasswordUtil;
import com.mss.ediscv.util.ServiceLocatorException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import com.mss.ediscv.util.LoggerUtility;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.Iterator;
import java.util.List;

/**
 * @author miracle
 */
public class SchdularServiceImpl implements SchdularService {

    private static Logger logger = LogManager.getLogger(SchdularServiceImpl.class.getName());
    String tmp_Recieved_From = "";
    String tmp_Recieved_ToTime = "";
    String responseString = null;
    private ArrayList<SchdularBean> schdularList;

    public ArrayList<SchdularBean> getSchdularList(SchdularAction schdularAction) throws ServiceLocatorException {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            String status = schdularAction.getStatus();
            StringBuilder documentSearchQuery = new StringBuilder();
            documentSearchQuery.append("SELECT SCH_ID,SCH_TITLE,SCH_TYPE,SCH_TS,SCH_STATUS from SCHEDULER ");
            documentSearchQuery.append("WHERE 1=1 ");
            if (status != null && !"-1".equals(status.trim())) {
                documentSearchQuery.append(" AND SCHEDULER.SCH_STATUS='" + status + "' ");
            }
            System.out.println("CommunicationProtocols query:"+documentSearchQuery.toString());
            connection = ConnectionProvider.getInstance().getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(documentSearchQuery.toString());
            schdularList = new ArrayList<SchdularBean>();
            System.out.println("Query and resultset start time::"+DateUtility.getInstance().getCurrentDB2Timestamp());
            while (resultSet.next()) {
                SchdularBean schdularBean = new SchdularBean();
                schdularBean.setId(resultSet.getInt("SCH_ID"));
                schdularBean.setSchtitle(resultSet.getString("SCH_TITLE"));
                schdularBean.setSchtype(resultSet.getString("SCH_TYPE"));
                schdularBean.setSchhrFormat(resultSet.getString("SCH_TS"));
                schdularBean.setStatus(resultSet.getString("SCH_STATUS"));
                schdularList.add(schdularBean);
            }
            System.out.println("Resultset end time::"+DateUtility.getInstance().getCurrentDB2Timestamp());
        } catch (SQLException sqlException) {
            LoggerUtility.log(logger, "SQLException occurred in getSchdularList method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
        } catch (Exception exception) {
            LoggerUtility.log(logger, "Exception occurred in getSchdularList method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
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
                LoggerUtility.log(logger, "finally SQLException occurred in getSchdularList method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
            }
        }
        return schdularList;
    }

    public String SchdularAdd(SchdularAction schdularAction) throws ServiceLocatorException {
        StringBuilder str = new StringBuilder(schdularAction.getSchhours());
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            String time = schdularAction.getSchhours() + " " + schdularAction.getSchhrFormat();
            connection = ConnectionProvider.getInstance().getConnection();
            preparedStatement = connection.prepareStatement("INSERT INTO SCHEDULER(SCH_TITLE,SCH_TYPE,SCH_STATUS,SCH_TS,RECIVER_IDS,EXTRANAL_EMAILIDS,REPORTS_TYPE) VALUES (?,?,?,?,?,?,?)");
            preparedStatement.setString(1, schdularAction.getSchtitle());
            preparedStatement.setString(2, schdularAction.getSchType());
            preparedStatement.setString(3, "Active");
            preparedStatement.setString(4, time);
            preparedStatement.setString(5, schdularAction.getUserEmail());
            preparedStatement.setString(6, schdularAction.getExtranalmailids());
            preparedStatement.setString(7, schdularAction.getReportsType());
            int i = preparedStatement.executeUpdate();
            if (i > 0) {
                responseString = "<font color='green'>Schduler added succesfully.</font>";
            } else {
                responseString = "<font color='red'>Please try again!</font>";
            }
            if (time != null && !time.equals("")) {
                int count = 0;
                preparedStatement = connection.prepareStatement("select count(*) as total from SCHEDULER where SCH_TYPE=? AND SCH_TS=?");
                preparedStatement.setString(1, schdularAction.getSchType());
                String time1 = schdularAction.getSchhours() + " " + schdularAction.getSchhrFormat();
                preparedStatement.setString(2, time1);
                resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    count = resultSet.getInt("total");
                }
                if (count > 1) {
                    responseString = "<font color='green'>SchdulerTime alredy In Running Please try to anthor time.</font>";
                }
            }
        } catch (SQLException sqlException) {
            responseString = "<font color='red'>Please try with different Id!</font>";
            LoggerUtility.log(logger, "SQLException occurred in SchdularAdd method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
        } catch (Exception exception) {
            responseString = "<font color='red'>Please try again!</font>";
            LoggerUtility.log(logger, "Exception occurred in SchdularAdd method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                    preparedStatement = null;
                }
                if (connection != null) {
                    connection.close();
                    connection = null;
                }
            } catch (SQLException sqlException) {
                LoggerUtility.log(logger, "finally SQLException occurred in SchdularAdd method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
            }
        }
        return responseString;
    }

    public SchdularAction schdularEdit(SchdularAction schdularAction) throws ServiceLocatorException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = ConnectionProvider.getInstance().getConnection();
            preparedStatement = connection.prepareStatement("SELECT SCH_ID,SCH_TITLE,SCH_TYPE,SCH_TS,RECIVER_IDS,EXTRANAL_EMAILIDS,REPORTS_TYPE from SCHEDULER WHERE SCH_ID=?");
            preparedStatement.setInt(1, schdularAction.getId());
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                schdularAction.setId(resultSet.getInt("SCH_ID"));
                schdularAction.setSchtitle(resultSet.getString("SCH_TITLE"));
                schdularAction.setSchType(resultSet.getString("SCH_TYPE"));
                String time1 = resultSet.getString("SCH_TS");
                String[] parts = time1.split(" ");
                String hours = parts[0];
                String hoursformate = parts[1];
                schdularAction.setSchhours(hours);
                schdularAction.setSchhrFormat(hoursformate);
                String Email = resultSet.getString("RECIVER_IDS");
                String[] reciverids = Email.split(",");
                List<String> wordList = Arrays.asList(reciverids);
                Iterator<String> iter = wordList.iterator();
                List<String> copy = new ArrayList<String>();
                while (iter.hasNext()) {
                    copy.add(iter.next().trim());
                }
                schdularAction.setReceiverids(copy);
                schdularAction.setExtranalmailids(resultSet.getString("EXTRANAL_EMAILIDS"));
                schdularAction.setReportsType(resultSet.getString("REPORTS_TYPE"));
            }
        } catch (SQLException sqlException) {
            responseString = "<font color='red'>Please try with different Id!</font>";
            LoggerUtility.log(logger, "SQLException occurred in schdularEdit method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
        } catch (Exception exception) {
            responseString = "<font color='red'>Please try again!</font>";
            LoggerUtility.log(logger, "Exception occurred in schdularEdit method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
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
                LoggerUtility.log(logger, "finally SQLException occurred in schdularEdit method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
            }
        }
        return schdularAction;
    }

    public String updateSchdular(SchdularAction schdularAction) throws ServiceLocatorException {
        String time = schdularAction.getSchhours() + " " + schdularAction.getSchhrFormat();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = ConnectionProvider.getInstance().getConnection();
            preparedStatement = connection.prepareStatement("UPDATE SCHEDULER SET SCH_TITLE=?,SCH_TYPE=?,SCH_TS=?,RECIVER_IDS=?,EXTRANAL_EMAILIDS=?,REPORTS_TYPE=? WHERE SCH_ID='" + schdularAction.getId() + "'");
            preparedStatement.setString(1, schdularAction.getSchtitle());
            preparedStatement.setString(2, schdularAction.getSchType());
            preparedStatement.setString(3, time);
            preparedStatement.setString(4, schdularAction.getUserEmail());
            preparedStatement.setString(5, schdularAction.getExtranalmailids());
            preparedStatement.setString(6, schdularAction.getReportsType());
            int i = preparedStatement.executeUpdate();
            if (i > 0) {
                responseString = "<font color='green'>Schdular updated succesfully.</font>";
            } else {
                responseString = "<font color='red'>Please try again!</font>";
            }
        } catch (SQLException sqlException) {
            responseString = "<font color='red'>Please try with different Id!</font>";
            LoggerUtility.log(logger, "SQLException occurred in updateSchdular method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
        } catch (Exception exception) {
            responseString = "<font color='red'>Please try again!</font>";
            LoggerUtility.log(logger, "Exception occurred in updateSchdular method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                    preparedStatement = null;
                }
                if (connection != null) {
                    connection.close();
                    connection = null;
                }
            } catch (SQLException sqlException) {
                LoggerUtility.log(logger, "finally SQLException occurred in updateSchdular method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
            }
        }
        return responseString;
    }

    public boolean getAuthdownloadUsercheck(SchdularAction schdularAction) throws ServiceLocatorException {

        boolean isUserExist = false;
        String password = null;
        String username = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        PasswordUtil passwordUtility = new PasswordUtil();
        try {
            connection = ConnectionProvider.getInstance().getConnection();
            preparedStatement = connection.prepareStatement("SELECT LOGINID,PASSWD FROM M_USER WHERE LOGINID=?");
            preparedStatement.setString(1, schdularAction.getLoginId());
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                username = resultSet.getString("LOGINID");
                password = resultSet.getString("PASSWD");
                String decryptedPwd = passwordUtility.decryptPwd(password);
                if (decryptedPwd.equals(schdularAction.getPassword())) {
                    isUserExist = true;
                }
            }
        } catch (SQLException sqlException) {
            LoggerUtility.log(logger, "SQLException occurred in getAuthdownloadUsercheck method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
        } catch (Exception exception) {
            LoggerUtility.log(logger, "Exception occurred in getAuthdownloadUsercheck method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
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
                LoggerUtility.log(logger, "finally SQLException occurred in getAuthdownloadUsercheck method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
            }
        }
        return isUserExist;
    }

    public String SchdularRecordPath(SchdularAction schdularAction) throws ServiceLocatorException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String reportpath = null;
        try {
            connection = ConnectionProvider.getInstance().getConnection();
            preparedStatement = connection.prepareStatement("SELECT SCH_REPORTPATH from SCH_LOOKUPS where SCH_REF_ID=?");
            preparedStatement.setInt(1, schdularAction.getScheduleRefId());
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                reportpath = resultSet.getString("SCH_REPORTPATH");
            }
        } catch (SQLException sqlException) {
            LoggerUtility.log(logger, "SQLException occurred in SchdularRecordPath method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
        } catch (Exception exception) {
            LoggerUtility.log(logger, "Exception occurred in SchdularRecordPath method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
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
                LoggerUtility.log(logger, "finally SQLException occurred in SchdularRecordPath method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
            }
        }
        return reportpath;
    }

    public String SchdularEmailids(SchdularAction schdularAction) throws ServiceLocatorException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String Emailids = null;
        try {
            connection = ConnectionProvider.getInstance().getConnection();
            preparedStatement = connection.prepareStatement("SELECT RECIVER_IDS FROM SCHEDULER LEFT OUTER JOIN SCH_LOOKUPS ON (SCHEDULER.SCH_ID = SCH_LOOKUPS.SCH_ID) where SCH_REF_ID=?");
            preparedStatement.setInt(1, schdularAction.getScheduleRefId());
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Emailids = resultSet.getString("RECIVER_IDS");
            }
        } catch (SQLException sqlException) {
            LoggerUtility.log(logger, "SQLException occurred in SchdularEmailids method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
        } catch (Exception exception) {
            LoggerUtility.log(logger, "Exception occurred in SchdularEmailids method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
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
                LoggerUtility.log(logger, "finally SQLException occurred in SchdularEmailids method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
            }
        }
        return Emailids;
    }
}
