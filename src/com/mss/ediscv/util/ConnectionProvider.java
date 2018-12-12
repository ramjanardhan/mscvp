package com.mss.ediscv.util;

import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class ConnectionProvider {
    private static Logger logger = LogManager.getLogger(ConnectionProvider.class.getName());

    private static ConnectionProvider _instance;
    private DataSource dataSource;
    private DataSource oracleDataSource;
    private Connection connection;

    private ConnectionProvider() {
    }

    public static ConnectionProvider getInstance() {
        if (_instance == null) {
            _instance = new ConnectionProvider();
        }
        return _instance;
    }

    public Connection getConnection() throws ServiceLocatorException {
        try {
            String dsnName = com.mss.ediscv.util.Properties.getProperty("DB.DSN_NAME");
            dataSource = DataServiceLocator.getInstance().getDataSource(dsnName);
            connection = dataSource.getConnection();
        } catch (ServiceLocatorException serviceLocatorException) {
            LoggerUtility.log(logger, "Exception occurred in getConnection method:: " + serviceLocatorException.getMessage(), Level.ERROR, serviceLocatorException.getCause());
        } catch (SQLException sqlexception) {
            LoggerUtility.log(logger, "sqlException occurred in getConnection method:: " + sqlexception.getMessage(), Level.ERROR, sqlexception.getCause());
        }
        return connection;
    }

    public Connection getOracleConnection() throws ServiceLocatorException {
        try {
            String dsnName = com.mss.ediscv.util.Properties.getProperty("ORACLEDB.DSNNAME");
            oracleDataSource = DataServiceLocator.getInstance().getOracleDataSource(dsnName);
            connection = oracleDataSource.getConnection();
        } catch (ServiceLocatorException serviceLocatorException) {
            LoggerUtility.log(logger, "Exception occurred in getOracleConnection method:: " + serviceLocatorException.getMessage(), Level.ERROR, serviceLocatorException.getCause());
        } catch (SQLException sqlexception) {
            LoggerUtility.log(logger, "sqlException occurred in getOracleConnection method:: " + sqlexception.getMessage(), Level.ERROR, sqlexception.getCause());
        }
        return connection;
    }
}
