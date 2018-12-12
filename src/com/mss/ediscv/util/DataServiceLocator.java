package com.mss.ediscv.util;

import com.ibm.db2.jcc.DB2DataSource;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import oracle.jdbc.pool.OracleDataSource;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * This is a Data Service Locator object used to abstract all JNDI usage and to
 * hide the complexities of initial context creation, DataSource lookup.
 * Multiple clients can reuse the Service Locator object to reduce code
 * complexity, provide a single point of control, and improve performance by
 * providing a caching facility.
 * <p>
 * This class reduces the client complexity that results from the client's
 * dependency on and need to perform lookup and creation processes, which are
 * resource-intensive. To eliminate these problems, this pattern provides a
 * mechanism to abstract all dependencies and network details into the Service
 * Locator.
 *
 * <p>
 * Usage: This is a Singleton class, usage is as follows:<br>
 * Use the getInstance method to create an instance of the class.
 *
 * <code>ServiceLocator serviceLocator = ServiceLocator.getInstance();</code>
 *
 * @author MrutyumjayaRao Chennu<mchennu@miraclesoft.com>
 *
 * @version 1.0
 *
 */
public class DataServiceLocator {
    private static Logger logger = LogManager.getLogger(DataServiceLocator.class.getName());

    private Context context;
    private static DataServiceLocator _instance;

    private DataServiceLocator() throws ServiceLocatorException {
        try {
            context = new InitialContext();
        } catch (Exception exception) {
            LoggerUtility.log(logger, "Exception occurred in DataServiceLocator method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
        }
    }

    public static DataServiceLocator getInstance() throws ServiceLocatorException {
        try {
            if (_instance == null) {
                _instance = new DataServiceLocator();
            }
        } catch (Exception exception) {
            LoggerUtility.log(logger, "Exception occurred in getInstance method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
        }
        return _instance;
    }

    public DataSource getDataSource(String dataSourceName) throws ServiceLocatorException {
        DataSource dataSource = null;
        try {
            if (CacheManager.getCache().containsKey(dataSourceName)) {
                dataSource = (DB2DataSource) CacheManager.getCache().get(dataSourceName);
            } else {
                DB2DataSource datasource = new DB2DataSource();
                datasource.setServerName(com.mss.ediscv.util.Properties.getProperty("DB_HOST"));
                datasource.setUser(com.mss.ediscv.util.Properties.getProperty("DB_USER"));
                datasource.setPassword(com.mss.ediscv.util.Properties.getProperty("DB_PASSWORD"));
                datasource.setDriverType(Integer.parseInt(com.mss.ediscv.util.Properties.getProperty("DB_DRIVER_TYPE"))); //Type 4 pure Java JDBC Driver
                datasource.setPortNumber(Integer.parseInt(com.mss.ediscv.util.Properties.getProperty("DB_PORT")));
                datasource.setDatabaseName(com.mss.ediscv.util.Properties.getProperty("DB_NAME"));
                dataSource = datasource;
                CacheManager.getCache().put(dataSourceName, datasource);
            }
        } catch (Exception exception) {
            LoggerUtility.log(logger, "Exception occurred in getDataSource method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
        }
        return dataSource;
    }

    public DataSource getOracleDataSource(String dataSourceName) throws ServiceLocatorException {
        DataSource dataSource = null;
        try {
            OracleDataSource datasource = new OracleDataSource();
            datasource.setServerName(com.mss.ediscv.util.Properties.getProperty("ORACLEDB.Host"));
            datasource.setUser(com.mss.ediscv.util.Properties.getProperty("ORACLEDB.User"));
            datasource.setPassword(com.mss.ediscv.util.Properties.getProperty("ORACLEDB.Password"));
            datasource.setDriverType(com.mss.ediscv.util.Properties.getProperty("ORACLEDB.DriverType"));
            datasource.setPortNumber(Integer.parseInt(com.mss.ediscv.util.Properties.getProperty("ORACLEDB.Port")));
            datasource.setDatabaseName(com.mss.ediscv.util.Properties.getProperty("ORACLEDB.Name"));
            dataSource = datasource;
        } catch (Exception exception) {
            LoggerUtility.log(logger, "Exception occurred in getOracleDataSource method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
        }
        return dataSource;
    }
}
