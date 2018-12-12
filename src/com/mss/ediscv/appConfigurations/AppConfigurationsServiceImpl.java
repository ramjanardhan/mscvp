package com.mss.ediscv.appConfigurations;

import com.mss.ediscv.util.ConnectionProvider;
import com.mss.ediscv.util.ServiceLocatorException;
import java.sql.*;
import com.mss.ediscv.util.LoggerUtility;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AppConfigurationsServiceImpl implements AppConfigurationsService {
    Connection connection = null;
    Statement statement = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    CallableStatement callableStatement = null;
    private List<AppConfigurationsBean> appConfigurationsDBList;
    private List<AppConfigurationsBean> appConfigurationsBaseValueList;
    private static Logger logger = LogManager.getLogger(AppConfigurationsServiceImpl.class.getName());

    public String getAppConfigurationsValues(AppConfigurationsAction appConfigurationsAction) throws ServiceLocatorException {
        System.out.println("entered in AppConfigurationsServiceImpl method");
        String message = "";
        AppConfigurationsBean appConfigurationsDBBean = null;
        AppConfigurationsBean appConfigurationsOutagesBean = null;
        try {
            connection = ConnectionProvider.getInstance().getConnection();
            String appConfigurationsQuery = "SELECT PK_ID, PROPERTYNAME, PROPERTYVALUE, CATEGORY, DISPLAYORDER FROM SCVP_CONFIG ORDER BY DISPLAYORDER ASC";
            statement = connection.createStatement();
            resultSet = statement.executeQuery(appConfigurationsQuery);
            appConfigurationsDBList = new ArrayList<AppConfigurationsBean>();
            appConfigurationsBaseValueList = new ArrayList<AppConfigurationsBean>();
            while (resultSet.next()) {
                if ("TIME_INTERVAL".equalsIgnoreCase(resultSet.getString("CATEGORY"))) {
                    appConfigurationsAction.setTimeInterval(resultSet.getString("PROPERTYVALUE"));
                } else if ("DISPLAY_RECORDS".equalsIgnoreCase(resultSet.getString("CATEGORY"))) {
                    appConfigurationsAction.setDisplayRecords(resultSet.getString("PROPERTYVALUE"));
                } else if ("MANUFACTURING_TOP_TEN_TP".equalsIgnoreCase(resultSet.getString("CATEGORY"))) {
                    String[] ediTopTP = (resultSet.getString("PROPERTYVALUE")).split(",");
                    appConfigurationsAction.setEdiTP1(ediTopTP[0]);
                    appConfigurationsAction.setEdiTP2(ediTopTP[1]);
                    appConfigurationsAction.setEdiTP3(ediTopTP[2]);
                    appConfigurationsAction.setEdiTP4(ediTopTP[3]);
                    appConfigurationsAction.setEdiTP5(ediTopTP[4]);
                    appConfigurationsAction.setEdiTP6(ediTopTP[5]);
                    appConfigurationsAction.setEdiTP7(ediTopTP[6]);
                    appConfigurationsAction.setEdiTP8(ediTopTP[7]);
                    appConfigurationsAction.setEdiTP9(ediTopTP[8]);
                    appConfigurationsAction.setEdiTP10(ediTopTP[9]);
                } else if ("LOGISTIC_TOP_TEN_TP".equalsIgnoreCase(resultSet.getString("CATEGORY"))) {
                    String[] railTopTP = (resultSet.getString("PROPERTYVALUE")).split(",");
                    appConfigurationsAction.setRailTP1(railTopTP[0]);
                    appConfigurationsAction.setRailTP2(railTopTP[1]);
                    appConfigurationsAction.setRailTP3(railTopTP[2]);
                    appConfigurationsAction.setRailTP4(railTopTP[3]);
                    appConfigurationsAction.setRailTP5(railTopTP[4]);
                    appConfigurationsAction.setRailTP6(railTopTP[5]);
                    appConfigurationsAction.setRailTP7(railTopTP[6]);
                    appConfigurationsAction.setRailTP8(railTopTP[7]);
                    appConfigurationsAction.setRailTP9(railTopTP[8]);
                    appConfigurationsAction.setRailTP10(railTopTP[9]);
                } else if ("EDI_TRANSACTIONS".equalsIgnoreCase(resultSet.getString("CATEGORY"))) {
                    appConfigurationsAction.setEdiTransactionNamesList(resultSet.getString("PROPERTYVALUE"));
                } else if ("RAIL_TRANSACTIONS".equalsIgnoreCase(resultSet.getString("CATEGORY"))) {
                    appConfigurationsAction.setRailTransactionNamesList(resultSet.getString("PROPERTYVALUE"));
                } else if ("SCVP_DATABASE".equalsIgnoreCase(resultSet.getString("CATEGORY"))) {
                    appConfigurationsDBBean = new AppConfigurationsBean();
                    appConfigurationsDBBean.setPropertyId(resultSet.getInt("PK_ID"));
                    appConfigurationsDBBean.setPropertyName(resultSet.getString("PROPERTYNAME"));
                    appConfigurationsDBBean.setPropertyValue(resultSet.getString("PROPERTYVALUE"));
                    appConfigurationsDBBean.setDisplayOrder(resultSet.getString("DISPLAYORDER"));
                    appConfigurationsDBList.add(appConfigurationsDBBean);
                } else if ("BASE_VALUE".equalsIgnoreCase(resultSet.getString("CATEGORY"))) {
                    appConfigurationsOutagesBean = new AppConfigurationsBean();
                    appConfigurationsOutagesBean.setPropertyId(resultSet.getInt("PK_ID"));
                    appConfigurationsOutagesBean.setPropertyName(resultSet.getString("PROPERTYNAME").replaceAll("DocumentBaseValue", ""));
                    appConfigurationsOutagesBean.setPropertyValue(resultSet.getString("PROPERTYVALUE"));
                    appConfigurationsOutagesBean.setDisplayOrder(resultSet.getString("DISPLAYORDER"));
                    appConfigurationsBaseValueList.add(appConfigurationsOutagesBean);
                }
            }
            LoggerUtility.log(logger, "DB properties list size..." + appConfigurationsDBList.size(), Level.INFO, null);
            LoggerUtility.log(logger, "Base Value properties list size..." + appConfigurationsBaseValueList.size(), Level.INFO, null);
            appConfigurationsAction.setAppConfigurationsDBList(appConfigurationsDBList);
            appConfigurationsAction.setAppConfigurationsBaseValueList(appConfigurationsBaseValueList);
        } catch (SQLException ex) {
            LoggerUtility.log(logger, " SQLException occurred in getAppConfigurations:: " + ex.getMessage(), Level.ERROR, ex.getCause());
        } catch (Exception ex) {
            LoggerUtility.log(logger, " Exception occurred in getAppConfigurations:: " + ex.getMessage(), Level.ERROR, ex.getCause());
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
            } catch (SQLException ex) {
                LoggerUtility.log(logger, "Finally block SQLException occurred in getAppConfigurations:: " + ex.getMessage(), Level.ERROR, ex.getCause());
            }
        }
        System.out.println("ended in AppConfigurationsServiceImpl method");
        return message;
    }

}
