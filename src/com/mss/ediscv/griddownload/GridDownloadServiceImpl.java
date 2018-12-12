/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mss.ediscv.griddownload;

import com.mss.ediscv.util.ConnectionProvider;
import com.mss.ediscv.util.DateUtility;
import com.mss.ediscv.util.ServiceLocatorException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import com.mss.ediscv.util.LoggerUtility;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author miracle
 */
public class GridDownloadServiceImpl implements GridDownloadService {

    private static Logger logger = LogManager.getLogger(GridDownloadServiceImpl.class.getName());

    public String getReportattachment(int scheduleId, String startDate) throws ServiceLocatorException {
        Connection connection = null;
        Statement statement = null;
        String reportPath = "Nodata";
        String reportattachmentQuery = "";
        ResultSet resultSet = null;
        try {
            startDate = DateUtility.getInstance().DateViewToDBCompare(startDate);
            startDate = startDate.substring(0, 10);
            reportattachmentQuery = "SELECT SCH_REPORTPATH from SCH_LOOKUPS where SCH_ID=" + scheduleId + " and date(SCH_RUNDATE) = DATE('" + startDate + "')";
            connection = ConnectionProvider.getInstance().getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(reportattachmentQuery);
            if (resultSet.next()) {
                reportPath = resultSet.getString("SCH_REPORTPATH");
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
            } catch (SQLException sqlException) {
                LoggerUtility.log(logger, "SQLException occurred in getReportattachment method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
            }
        }
        return reportPath;
    }
}
