package com.mss.ediscv.tp;

import com.mss.ediscv.util.ConnectionProvider;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import com.mss.ediscv.util.LoggerUtility;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.mss.ediscv.util.ServiceLocatorException;
import java.io.File;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * @author miracle
 */
public class TpServiceImpl implements TpService {

    String tmp_Recieved_From = "";
    String tmp_Recieved_ToTime = "";
    String responseString = null;
    private ArrayList<TpBean> tpList;
    private static Logger logger = LogManager.getLogger(TpServiceImpl.class.getName());

    public String addTP(TpAction tpAction) throws ServiceLocatorException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        Statement statement = null;
        ResultSet resultSet = null;
        createTpDirectoy(tpAction);
        try {
            String id = tpAction.getId();
            String name = tpAction.getName();
            connection = ConnectionProvider.getInstance().getConnection();
            preparedStatement = connection.prepareStatement("INSERT INTO TP VALUES(?,?,?,?,?,?,?)");
            preparedStatement.setString(1, id);
            preparedStatement.setString(2, name.toUpperCase());
            preparedStatement.setString(3, tpAction.getContact());
            preparedStatement.setString(4, tpAction.getPhno());
            preparedStatement.setString(5, tpAction.getDept());
            preparedStatement.setString(6, tpAction.getCommid());
            preparedStatement.setString(7, tpAction.getQualifier());
            int count = preparedStatement.executeUpdate();
            if (count > 0) {
                tpAction.setId("");
                tpAction.setName("");
                tpAction.setContact("");
                tpAction.setPhno("");
                tpAction.setDept("");
                tpAction.setCommid("");
                tpAction.setQualifier("");
                responseString = "<font color='green'>Trading Partner Inserted Successfully!! </font>";
            } else {
                responseString = "<font color='red'>Sorry ! Please try again.</font>";
            }
        } catch (SQLException sqlException) {
            responseString = "<font color='red'>Please try with different Id!</font>";
            LoggerUtility.log(logger, "SQLException occurred in addTP method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
        } catch (Exception exception) {
            responseString = "<font color='red'>Please try again!</font>";
            LoggerUtility.log(logger, "Exception occurred in addTP method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
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
                LoggerUtility.log(logger, "SQLException occurred in addTP method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
            }
        }
        return responseString;
    }

    public ArrayList getTpList(TpAction tpAction) throws ServiceLocatorException {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        StringBuilder shipmentSearchQuery = new StringBuilder();
        shipmentSearchQuery.append("SELECT * FROM TP");
        String searchQuery = shipmentSearchQuery.toString();
        try {
            connection = ConnectionProvider.getInstance().getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(searchQuery);
            tpList = new ArrayList<TpBean>();
            while (resultSet.next()) {
                TpBean tpBean = new TpBean();
                tpBean.setId(resultSet.getString("ID"));
                tpBean.setName(resultSet.getString("NAME"));
                tpBean.setContact(resultSet.getString("CONTACT_INFO"));
                tpBean.setPhno(resultSet.getString("VENDOR_NUMBER"));
                tpBean.setDept(resultSet.getString("DEPARTMENTS"));
                tpBean.setCommid(resultSet.getString("EDI_COMM_ID"));
                tpBean.setQualifier(resultSet.getString("QUALIFIER"));
                tpList.add(tpBean);
            }
        } catch (SQLException sqlException) {
            LoggerUtility.log(logger, "SQLException occurred in getTpList method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
        } catch (Exception exception) {
            LoggerUtility.log(logger, "Exception occurred in getTpList method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
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
                LoggerUtility.log(logger, "finally SQLException occurred in getTpList method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
            }
        }
        return tpList;
    }

    public void createTpDirectoy(TpAction tpAction) {
        try {
            String name = tpAction.getName();
            File outboundFile = new File(com.mss.ediscv.util.Properties.getProperty("mscvp.tpPath") + name + "\\" + "OUTBOUND");
            File inboundFile = new File(com.mss.ediscv.util.Properties.getProperty("mscvp.tpPath") + name + "\\" + "INBOUND");
            outboundFile.mkdirs();
            inboundFile.mkdirs();
            tpAction.setTpOutPath(outboundFile.getAbsolutePath());
            tpAction.setTpInPath(inboundFile.getAbsolutePath());
        } catch (Exception exception) {
            LoggerUtility.log(logger, "Exception occurred in createTpDirectoy method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
        }
    }
}
