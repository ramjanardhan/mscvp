/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mss.ediscv.download;

import com.mss.ediscv.util.ConnectionProvider;
import com.mss.ediscv.util.DateUtility;
import com.mss.ediscv.util.ServiceLocatorException;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.mss.ediscv.util.LoggerUtility;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author miracle
 */
public class DownloadReportActionServiceImpl implements DownloadReportActionService {

    private static Logger logger = LogManager.getLogger(DownloadReportActionServiceImpl.class.getName());
    
    public String getReportattachment(int scheduleId, String startDate) throws ServiceLocatorException {
        
        Connection connection = null;
        Statement statement = null;
        String reportpath = "Nodata";
        String reportattachmentQuery = "";
        ResultSet resultSet = null;
        startDate = DateUtility.getInstance().DateViewToDBCompare(startDate);
        startDate = startDate.substring(0, 10);
        reportattachmentQuery = "SELECT SCH_REPORTPATH from SCH_LOOKUPS where SCH_ID=" + scheduleId + " and date(SCH_RUNDATE) = DATE('" + startDate + "')";
        try {
            connection = ConnectionProvider.getInstance().getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(reportattachmentQuery);
            if (resultSet.next()) {
                reportpath = resultSet.getString("SCH_REPORTPATH");
            }
        } catch (SQLException sqlException) {
                 LoggerUtility.log(logger, "SQLException occurred in getReportattachment method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
            } catch (ServiceLocatorException serviceLocatorException) {
            LoggerUtility.log(logger, "ServiceLocatorException occurred in getReportattachment method:: " + serviceLocatorException.getMessage(), Level.ERROR, serviceLocatorException.getCause());
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
            }  catch (SQLException sqlException) {
                 LoggerUtility.log(logger, "finally SQLException occurred in getReportattachment method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
            } 
        }
        return reportpath;
    }
}