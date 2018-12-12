/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mss.ediscv.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author miracle
 */
public class DataSourceDataProvider {

    private static Logger logger = LogManager.getLogger(DataSourceDataProvider.class.getName());
    private static DataSourceDataProvider _instance;

    private DataSourceDataProvider() {

    }

    public static DataSourceDataProvider getInstance() {

        if (_instance == null) {
            _instance = new DataSourceDataProvider();
        }
        return _instance;
    }

    public List getCorrelationNames(int st, int groupId) throws ServiceLocatorException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String getCorrelationNamesQuery = null;
        connection = ConnectionProvider.getInstance().getConnection();
        List correlationList = new ArrayList();
        try {
            if (st == 0) {
                getCorrelationNamesQuery = "SELECT DISTINCT(NAME) FROM CORRELATION where GROUP_ID=" + groupId + " order by NAME";
            } else {
                getCorrelationNamesQuery = "SELECT DISTINCT(NAME) FROM CORRELATION where GROUP_ID=" + groupId + " AND MODE_ID=" + st + " order by NAME";
            }
            System.out.println("getCorrelationNames query:" + getCorrelationNamesQuery.toString());
            preparedStatement = connection.prepareStatement(getCorrelationNamesQuery);
            resultSet = preparedStatement.executeQuery();
            System.out.println("Query and resultset start time::" + DateUtility.getInstance().getCurrentDB2Timestamp());
            while (resultSet.next()) {
                correlationList.add(resultSet.getString("NAME"));
            }
            System.out.println("Resultset end time::" + DateUtility.getInstance().getCurrentDB2Timestamp());
        } catch (SQLException sqlException) {
            LoggerUtility.log(logger, "sqlException occurred in getCorrelationNames method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
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
                LoggerUtility.log(logger, "Finally sqlException occurred in getCorrelationNames method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
            }
        }
        return correlationList;
    }

    public List getDocumentTypeList(String flowFlag) throws ServiceLocatorException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String getDocumentTypeListQuery = null;
        System.out.println("Before Connection::" + DateUtility.getInstance().getCurrentDB2Timestamp());
        connection = ConnectionProvider.getInstance().getConnection();
        System.out.println("After Connection::" + DateUtility.getInstance().getCurrentDB2Timestamp());
        List documentTypeMap = new ArrayList();
        try {
            getDocumentTypeListQuery = "SELECT unique(FILES.TRANSACTION_TYPE) FROM FILES where FILES.FLOWFLAG='" + flowFlag + "' AND FILES.TRANSACTION_TYPE is not null";
            System.out.println("DocumentTypeList query:" + getDocumentTypeListQuery.toString());
            preparedStatement = connection.prepareStatement(getDocumentTypeListQuery);
            resultSet = preparedStatement.executeQuery();
            System.out.println("Query and resultset start time::" + DateUtility.getInstance().getCurrentDB2Timestamp());
            while (resultSet.next()) {
                if (resultSet.getString("TRANSACTION_TYPE") != null && !"".equals(resultSet.getString("TRANSACTION_TYPE"))) {
                    documentTypeMap.add(resultSet.getString("TRANSACTION_TYPE"));
                }
            }
            System.out.println("Resultset end time::" + DateUtility.getInstance().getCurrentDB2Timestamp());
        } catch (SQLException sqlException) {
            LoggerUtility.log(logger, "sqlException occurred in getDocumentTypeList method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
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
                LoggerUtility.log(logger, "finally sqlException occurred in getDocumentTypeList method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
            }
        }
        return documentTypeMap;
    }

    /**
     * DESC : doc type as list
     *
     * @return
     * @throws ServiceLocatorException
     */
    public List getDocumentTypeList(int st, int groupId) throws ServiceLocatorException {
        System.out.println("document type list st is "+st);
        System.out.println("document type list groupId is"+groupId);
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String documentTypeListQuery = null;
        List documentTypeMap = new ArrayList();
        try {
            System.out.println("Before Connection::" + DateUtility.getInstance().getCurrentDB2Timestamp());
            connection = ConnectionProvider.getInstance().getConnection();
            System.out.println("After Connection::" + DateUtility.getInstance().getCurrentDB2Timestamp());
            if (st == 0) {
                documentTypeListQuery = "SELECT NAME FROM DOCUMENTTYPES where GROUP_ID=" + groupId;
            } else {
                documentTypeListQuery = "SELECT NAME FROM DOCUMENTTYPES where GROUP_ID=" + groupId + " AND MODE_ID=" + st;
            }
            System.out.println("DocumentTypeList query:" + documentTypeListQuery.toString());
            preparedStatement = connection.prepareStatement(documentTypeListQuery);
            resultSet = preparedStatement.executeQuery();
            System.out.println("Query and resultset start time::" + DateUtility.getInstance().getCurrentDB2Timestamp());
            while (resultSet.next()) {
                documentTypeMap.add(resultSet.getString("NAME"));
            }
            System.out.println("Resultset end time::" + DateUtility.getInstance().getCurrentDB2Timestamp());
        } catch (SQLException sqlException) {
            LoggerUtility.log(logger, "sqlException occurred in getDocumentTypeList method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
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
                LoggerUtility.log(logger, "Finally sqlException occurred in getDocumentTypeList method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
            }
        }
        return documentTypeMap;
    }
    /*
     * 
     * Map for Reteriving States
     */

    public Map getStates() throws ServiceLocatorException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String statesQuery = null;
        connection = ConnectionProvider.getInstance().getConnection();
        Map statesMap = new HashMap();
        try {
            statesQuery = "SELECT DESCRIPTION,NAME FROM TBLLKSTATES ORDER BY DESCRIPTION";
            preparedStatement = connection.prepareStatement(statesQuery);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                statesMap.put(resultSet.getString("DESCRIPTION"), resultSet.getString("NAME"));
            }
        } catch (SQLException sqlexception) {
            LoggerUtility.log(logger, "sqlException occurred in getStates method:: " + sqlexception.getMessage(), Level.ERROR, sqlexception.getCause());
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
                LoggerUtility.log(logger, "Finally sqlException occurred in getStates method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
            }
        }
        return statesMap;
    }

    /**
     * DESC: to get category map
     */
    public Map getCategory() throws ServiceLocatorException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String categoryQuery = null;
        connection = ConnectionProvider.getInstance().getConnection();
        Map categoryMap = new HashMap();
        try {
            categoryQuery = "SELECT CAT_ID,CAT_NAME FROM MSCVP_CATEGORY OREDER BY CAT_ID";
            preparedStatement = connection.prepareStatement(categoryQuery);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                categoryMap.put(resultSet.getString("CAT_ID"), resultSet.getString("CAT_NAME"));
            }
        } catch (SQLException sqlException) {
            LoggerUtility.log(logger, "sqlException occurred in getCategory method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
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
                LoggerUtility.log(logger, "Finally sqlException occurred in getCategory method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
            }
        }
        return categoryMap;
    }

    /**
     * DESC: priority map
     *
     */
    public Map getPriority() throws ServiceLocatorException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String priorityQuery = null;
        connection = ConnectionProvider.getInstance().getConnection();
        Map priorityMap = new HashMap();
        try {
            priorityQuery = "SELECT PRI_ID,PRI_NAME FROM MSCVP_PRIORITY ORDER BY PR_ID";
            preparedStatement = connection.prepareStatement(priorityQuery);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                priorityMap.put(resultSet.getString("PRI_ID"), resultSet.getString("PRI_NAME"));
            }
        } catch (SQLException sqlexception) {
            LoggerUtility.log(logger, "sqlException occurred in getPriority method:: " + sqlexception.getMessage(), Level.ERROR, sqlexception.getCause());
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
                LoggerUtility.log(logger, "Finally sqlException occurred in getPriority method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
            }
        }
        return priorityMap;
    }

    public String getEmailByLoginId(String loginid) throws ServiceLocatorException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String emailByLoginIdQuery = null;
        connection = ConnectionProvider.getInstance().getConnection();
        String emailId = null;
        try {
            emailByLoginIdQuery = "select EMAIL from M_user where LOGINID LIKE '%" + loginid + "%'";
            preparedStatement = connection.prepareStatement(emailByLoginIdQuery);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                emailId = resultSet.getString("EMAIL");
            }
        } catch (SQLException sqlException) {
            LoggerUtility.log(logger, "sqlException occurred in getEmailByLoginId method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
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
                LoggerUtility.log(logger, "sqlException occurred in getEmailByLoginId method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
            }
        }
        return emailId;
    }

    public String getNameByLoginId(String loginid) throws ServiceLocatorException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String nameByLoginIdQuery = null;
        connection = ConnectionProvider.getInstance().getConnection();
        String name = null;

        try {
            nameByLoginIdQuery = "select concat(FNME,LNME) as name from M_user where LOGINID LIKE '%" + loginid + "%'";
            preparedStatement = connection.prepareStatement(nameByLoginIdQuery);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                name = resultSet.getString("name");
            }
        } catch (SQLException sqlException) {
            LoggerUtility.log(logger, "sqlException occurred in getNameByLoginId method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
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
                LoggerUtility.log(logger, "Finally sqlException occurred in getNameByLoginId method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
            }
        }
        return name;
    }

    public Map getFlows(int empId) throws ServiceLocatorException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String flowsQuery = null;
        connection = ConnectionProvider.getInstance().getConnection();
        Map flowMap = new HashMap();
        try {
            flowsQuery = "SELECT PRIORITY,MSCVP_FLOWS.FLOWNAME,MSCVP_FLOWS.ID FROM MSCVP_FLOWS LEFT OUTER JOIN M_USER_FLOWS_ACTION ON (MSCVP_FLOWS.ID = M_USER_FLOWS_ACTION.FLOWID) WHERE USER_ID=" + empId + " order by PRIORITY";
            preparedStatement = connection.prepareStatement(flowsQuery);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                flowMap.put(resultSet.getString("ID"), resultSet.getString("FLOWNAME"));
            }
        } catch (SQLException sqlException) {
            LoggerUtility.log(logger, "sqlException occurred in getFlows method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
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
                LoggerUtility.log(logger, "Finally sqlException occurred in getFlows method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
            }
        }
        return flowMap;
    }

    /**
     * to get the roleName
     */
    public String getRoleNameByRoleId(String roleId) throws ServiceLocatorException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String roleNameByRoleIdQuery = null;
        connection = ConnectionProvider.getInstance().getConnection();
        String roleName = null;
        try {
            roleNameByRoleIdQuery = "select ROLE_NAME from MSCVP_ROLES where Id=" + roleId;
            preparedStatement = connection.prepareStatement(roleNameByRoleIdQuery);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                roleName = resultSet.getString("ROLE_NAME");
            }
        } catch (SQLException sqlException) {
            LoggerUtility.log(logger, "sqlException occurred in getRoleNameByRoleId method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
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
                LoggerUtility.log(logger, "sqlException occurred in getRoleNameByRoleId method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
            }
        }

        return roleName;
    }

    /**
     * To getPriorityById
     */
    public String getPriorityById(int id) throws ServiceLocatorException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String priorityByIdQuery = null;
        connection = ConnectionProvider.getInstance().getConnection();
        String priorityName = null;
        try {
            priorityByIdQuery = "SELECT PRI_NAME FROM MSCVP_PRIORITY WHERE PRI_ID =" + id;
            preparedStatement = connection.prepareStatement(priorityByIdQuery);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                priorityName = resultSet.getString("PRI_NAME");
            }
        } catch (SQLException sqlException) {
            LoggerUtility.log(logger, "sqlException occurred in getPriorityById method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
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
                LoggerUtility.log(logger, "Finally sqlException occurred in getPriorityById method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
            }
        }
        return priorityName;
    }

    /**
     * To getCategoryById
     *
     */
    public String getCategoryById(int id) throws ServiceLocatorException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String categoryByIdQuery = null;
        connection = ConnectionProvider.getInstance().getConnection();
        String categoryName = null;
        try {
            categoryByIdQuery = "SELECT CAT_NAME FROM MSCVP_CATEGORY WHERE CAT_ID =" + id;
            preparedStatement = connection.prepareStatement(categoryByIdQuery);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                categoryName = resultSet.getString("CAT_NAME");
            }
        } catch (SQLException sqlException) {
            LoggerUtility.log(logger, "sqlException occurred in getCategoryById method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
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
                LoggerUtility.log(logger, "Finally sqlException occurred in getCategoryById method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
            }
        }
        return categoryName;
    }

    /**
     * To get user map
     */
    public Map getUsers() throws ServiceLocatorException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String usersQuery = null;
        connection = ConnectionProvider.getInstance().getConnection();
        Map usersMap = new HashMap();
        try {
            usersQuery = "select LOGINID,concat(FNME,LNME) as cname from M_USER as MU left outer join M_USER_ROLES as MUR on (MU.id=MUR.USER_ID)WHERE MUR.ROLE_ID != 1";
            preparedStatement = connection.prepareStatement(usersQuery);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                usersMap.put(resultSet.getString("LOGINID"), resultSet.getString("cname"));
            }
        } catch (SQLException sqlException) {
            LoggerUtility.log(logger, "sqlException occurred in getUsers method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
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
                LoggerUtility.log(logger, "Finally sqlException occurred in getUsers method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
            }
        }
        return usersMap;
    }

    /*
     * 
     * For Assgetting Assigned Flows
     * Date : )5/03/2013
     * Author : Santosh
     */
    public Map getAssignedFlows(int userId) throws ServiceLocatorException {

        Map assignedRoleMap = new TreeMap();
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        connection = ConnectionProvider.getInstance().getConnection();
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT MSCVP_FLOWS.ID,MSCVP_FLOWS.FLOWNAME FROM MSCVP_FLOWS LEFT OUTER JOIN M_USER_FLOWS_ACTION ON (MSCVP_FLOWS.ID = M_USER_FLOWS_ACTION.FLOWID) where USER_ID=" + userId + " AND MSCVP_FLOWS.ID != 1 ORDER BY FLOWID");
            while (resultSet.next()) {
                assignedRoleMap.put(resultSet.getString("ID"), resultSet.getString("FLOWNAME"));
            }
        } catch (SQLException sqlException) {
            LoggerUtility.log(logger, "sqlException occurred in getAssignedFlows method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
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
                LoggerUtility.log(logger, "Finally sqlException occurred in getAssignedFlows method:: " + sqlexception.getMessage(), Level.ERROR, sqlexception.getCause());
            }
        }
        return assignedRoleMap;
    }

    public Map getNotAssignedFlows(int userId) throws ServiceLocatorException {

        Map notAssignedFlowMap = new TreeMap();
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        connection = ConnectionProvider.getInstance().getConnection();
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT MSCVP_FLOWS.ID,MSCVP_FLOWS.FLOWNAME FROM MSCVP_FLOWS LEFT OUTER JOIN M_USER_FLOWS_ACTION ON (MSCVP_FLOWS.ID = M_USER_FLOWS_ACTION.FLOWID) WHERE MSCVP_FLOWS.ID NOT IN (SELECT M_USER_FLOWS_ACTION.FLOWID FROM M_USER_FLOWS_ACTION WHERE M_USER_FLOWS_ACTION.USER_ID=" + userId + ") AND MSCVP_FLOWS.ID != 1 ORDER BY MSCVP_FLOWS.ID");
            while (resultSet.next()) {
                notAssignedFlowMap.put(resultSet.getString("ID"), resultSet.getString("FLOWNAME"));
            }
        } catch (SQLException sqlException) {
            LoggerUtility.log(logger, "sqlException occurred in getNotAssignedFlows method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
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
                LoggerUtility.log(logger, "Finally sqlException occurred in getNotAssignedFlows method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
            }
        }

        return notAssignedFlowMap;
    }

    public Map getFlowbyflowKey(String flowsKey) throws ServiceLocatorException {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        connection = ConnectionProvider.getInstance().getConnection();
        Map flowsMap = new TreeMap();
        if (CacheManager.getCache().containsKey(flowsKey)) {
            flowsMap = (Map) CacheManager.getCache().get(flowsKey);
        } else {
            try {
                statement = connection.createStatement();
                resultSet = statement.executeQuery("SELECT ID,FLOWNAME FROM MSCVP_FLOWS where Id != 1");
                while (resultSet.next()) {
                    flowsMap.put(resultSet.getString("ID"), resultSet.getString("FLOWNAME"));
                }
                CacheManager.getCache().put(flowsKey, flowsMap);
            } catch (SQLException sqlexception) {
                LoggerUtility.log(logger, "sqlException occurred in getFlowbyflowKey method:: " + sqlexception.getMessage(), Level.ERROR, sqlexception.getCause());
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
                    LoggerUtility.log(logger, "Finally sqlException occurred in getFlowbyflowKey method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
                }
            }
        }
        return flowsMap;

    }

    public String getPrimaryFlowID(int userId) throws ServiceLocatorException {

        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        String primaryFlowId = null;
        connection = ConnectionProvider.getInstance().getConnection();
        try {
            statement = connection.createStatement();
            if (userId != 10000) {
                resultSet = statement.executeQuery("SELECT MSCVP_FLOWS.ID FROM MSCVP_FLOWS  LEFT OUTER JOIN M_USER_FLOWS_ACTION ON (MSCVP_FLOWS.ID = M_USER_FLOWS_ACTION.FLOWID) where USER_ID=" + userId + " AND MSCVP_FLOWS.ID != 1 AND M_USER_FLOWS_ACTION.PRIORITY = 1");
            } else {
                resultSet = statement.executeQuery("SELECT MSCVP_FLOWS.ID FROM MSCVP_FLOWS  LEFT OUTER JOIN M_USER_FLOWS_ACTION ON (MSCVP_FLOWS.ID = M_USER_FLOWS_ACTION.FLOWID) where USER_ID=" + userId + " AND M_USER_FLOWS_ACTION.PRIORITY = 1");
            }
            while (resultSet.next()) {
                primaryFlowId = resultSet.getString("ID");
            }
        } catch (SQLException sqlException) {
            LoggerUtility.log(logger, "sqlException occurred in getPrimaryFlowID method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
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
                LoggerUtility.log(logger, "Finally sqlException occurred in getPrimaryFlowID method:: " + sqlexception.getMessage(), Level.ERROR, sqlexception.getCause());
            }
        }
        return primaryFlowId;
    }

    public String getFlowNameByFlowID(String flowId) throws ServiceLocatorException {

        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        String flowName = null;
        try {
            System.out.println("Before Connection::" + DateUtility.getInstance().getCurrentDB2Timestamp());
            connection = ConnectionProvider.getInstance().getConnection();
            System.out.println("After Connection::" + DateUtility.getInstance().getCurrentDB2Timestamp());
            String a = "SELECT FLOWNAME FROM MSCVP_FLOWS where Id =" + flowId;
            statement = connection.createStatement();
            resultSet = statement.executeQuery(a);
            System.out.println("FlowNameByFlowID query:" + a.toString());
            System.out.println("Query and resultset start time::" + DateUtility.getInstance().getCurrentDB2Timestamp());
            while (resultSet.next()) {
                flowName = resultSet.getString("FLOWNAME");
            }
            System.out.println("Resultset end time::" + DateUtility.getInstance().getCurrentDB2Timestamp());
        } catch (SQLException sqlException) {
            LoggerUtility.log(logger, "sqlException occurred in getFlowNameByFlowID method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
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
                LoggerUtility.log(logger, "Finally sqlException occurred in getFlowNameByFlowID method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
            }
        }
        return flowName;
    }

    public String getFlowIdByFlowName(String flowName) throws ServiceLocatorException {

        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        String flowId = null;
        try {
            System.out.println("Before Connection::" + DateUtility.getInstance().getCurrentDB2Timestamp());
            connection = ConnectionProvider.getInstance().getConnection();
            System.out.println("After Connection::" + DateUtility.getInstance().getCurrentDB2Timestamp());
            String a = "SELECT ID FROM MSCVP_FLOWS where FLOWNAME ='" + flowName + "'";
            statement = connection.createStatement();
            resultSet = statement.executeQuery(a);
            System.out.println("FlowIdByFlowName query:" + a.toString());
            System.out.println("Query and resultset start time::" + DateUtility.getInstance().getCurrentDB2Timestamp());
            while (resultSet.next()) {
                flowId = resultSet.getString("ID");
            }
            System.out.println("Resultset end time::" + DateUtility.getInstance().getCurrentDB2Timestamp());
        } catch (SQLException sqlException) {
            LoggerUtility.log(logger, "sqlException occurred in getFlowIdByFlowName method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
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
                LoggerUtility.log(logger, "Finally sqlException occurred in getFlowIdByFlowName method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
            }
        }
        return flowId;
    }

    /**
     * MSSCVP roles *
     */
    public Map getMsscvpRoles() throws ServiceLocatorException {

        Map assignedRoleMap = new TreeMap();
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        connection = ConnectionProvider.getInstance().getConnection();
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery("select ID,Role_NAME From MSCVP_ROLES");
            while (resultSet.next()) {
                assignedRoleMap.put(resultSet.getString("ID"), resultSet.getString("Role_NAME"));
            }
        } catch (SQLException sqlException) {
            LoggerUtility.log(logger, "sqlException occurred in getMsscvpRoles method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
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
                LoggerUtility.log(logger, "Finally sqlException occurred in getMsscvpRoles method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
            }
        }
        return assignedRoleMap;
    }

    public String UpdateReProcessStatus(String Status, String fileId, String sec_key_Value, String flowFlag) throws ServiceLocatorException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        int i = 0;
        String updateReProcessStatusQuery = null;
        String result = null;
        connection = ConnectionProvider.getInstance().getConnection();
        try {
            updateReProcessStatusQuery = "UPDATE FILES SET REPROCESSSTATUS = '" + Status + "' WHERE FILE_ID LIKE '" + fileId + "' AND SEC_KEY_VAL LIKE '" + sec_key_Value + "' AND FLOWFLAG like '" + flowFlag + "'";
            preparedStatement = connection.prepareStatement(updateReProcessStatusQuery);
            i = preparedStatement.executeUpdate();
            if (i == 1) {
                result = "success";
            } else {
                result = "fail";
            }
        } catch (SQLException sqlException) {
            LoggerUtility.log(logger, "sqlException occurred in UpdateReProcessStatus method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
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
                LoggerUtility.log(logger, "Finally sqlException occurred in UpdateReProcessStatus method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
            }
        }
        return result;
    }

    public String getSapDetails(String instanceId, String poNumber) throws ServiceLocatorException {

        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        String responseString = "None";
        connection = ConnectionProvider.getInstance().getConnection();
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery("select SAP_USER, IDOC_NUMBER, PO_NUMBER, PO_DATE, IDOC_STATUS_CODE, IDOC_STATUS_DESCRIPTION from SAP_RECONCILIATION_DETAILS WHERE INSTANCE_ID='" + instanceId + "' AND PO_NUMBER='" + poNumber + "'");
            if (resultSet.next()) {
                responseString = "";

                if (!"".equals(resultSet.getString("SAP_USER")) && resultSet.getString("SAP_USER") != null) {
                    if (!"".equals(resultSet.getString("SAP_USER").trim())) {
                        responseString = resultSet.getString("SAP_USER");
                    } else {
                        responseString = "--";
                    }
                } else {
                    responseString = "--";
                }
                if (!"".equals(resultSet.getString("IDOC_NUMBER")) && resultSet.getString("IDOC_NUMBER") != null) {
                    if (!"".equals(resultSet.getString("IDOC_NUMBER").trim())) {
                        responseString = responseString + "|" + resultSet.getString("IDOC_NUMBER");
                    } else {
                        responseString = responseString + "|" + "--";
                    }
                } else {
                    responseString = responseString + "|" + "--";
                }
                if (!"".equals(resultSet.getString("PO_NUMBER")) && resultSet.getString("PO_NUMBER") != null) {
                    if (!"".equals(resultSet.getString("PO_NUMBER").trim())) {
                        responseString = responseString + "|" + resultSet.getString("PO_NUMBER");
                    } else {
                        responseString = responseString + "|" + "--";
                    }
                } else {
                    responseString = responseString + "|" + "--";
                }
                if (!"".equals(resultSet.getString("PO_DATE")) && resultSet.getString("PO_DATE") != null) {
                    if (!"".equals(resultSet.getString("PO_DATE").trim())) {
                        responseString = responseString + "|" + resultSet.getString("PO_DATE");
                    } else {
                        responseString = responseString + "|" + "--";
                    }
                } else {
                    responseString = responseString + "|" + "--";
                }
                if (!"".equals(resultSet.getString("IDOC_STATUS_CODE")) && resultSet.getString("IDOC_STATUS_CODE") != null) {
                    if (!"".equals(resultSet.getString("IDOC_STATUS_CODE").trim())) {
                        responseString = responseString + "|" + resultSet.getString("IDOC_STATUS_CODE");
                    } else {
                        responseString = responseString + "|" + "--";
                    }
                } else {
                    responseString = responseString + "|" + "--";
                }
                if (!"".equals(resultSet.getString("IDOC_STATUS_DESCRIPTION")) && resultSet.getString("IDOC_STATUS_DESCRIPTION") != null) {
                    if (!"".equals(resultSet.getString("IDOC_STATUS_DESCRIPTION").trim())) {
                        responseString = responseString + "|" + resultSet.getString("IDOC_STATUS_DESCRIPTION");
                    } else {
                        responseString = responseString + "|" + "--";
                    }
                } else {
                    responseString = responseString + "|" + "--";
                }
            }
        } catch (SQLException sqlException) {
            LoggerUtility.log(logger, "sqlException occurred in getSapDetails method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
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
                LoggerUtility.log(logger, "Finally sqlException occurred in getSapDetails method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
            }
        }
        return responseString;
    }

    /**
     * MSSCVP roles *
     */
    public Map getPartnerMap() throws ServiceLocatorException {

        Map partnerMap = new TreeMap();
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        connection = ConnectionProvider.getInstance().getConnection();
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT NAME,ID From TP where STATUS='ACTIVE' AND NAME !='METRIE'");
            while (resultSet.next()) {
                partnerMap.put(resultSet.getString("ID"), resultSet.getString("NAME"));
            }
        } catch (SQLException sqlException) {
            LoggerUtility.log(logger, "sqlException occurred in getPartnerMap method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
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
                LoggerUtility.log(logger, "Finally sqlException occurred in copyFiles method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
            }
        }
        return partnerMap;
    }

    /**
     * MSSCVP roles *
     */
    public Map getRouterMap() throws ServiceLocatorException {

        Map routerMap = new TreeMap();
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        connection = ConnectionProvider.getInstance().getConnection();
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT ROUTER_ID, ROUTER_NAME FROM ROUTERINFO");
            while (resultSet.next()) {
                routerMap.put(resultSet.getString("ROUTER_ID"), resultSet.getString("ROUTER_NAME"));
            }
        } catch (SQLException sqlexception) {
            LoggerUtility.log(logger, "sqlException occurred in getRouterMap method:: " + sqlexception.getMessage(), Level.ERROR, sqlexception.getCause());
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
                LoggerUtility.log(logger, "Finally sqlException occurred in getRouterMap method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
            }
        }
        return routerMap;
    }

    /**
     * MSSCVP roles *
     */
    public Map getRelationMap(String mapType) throws ServiceLocatorException {

        Map processMap = new TreeMap();
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        connection = ConnectionProvider.getInstance().getConnection();
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT REL_ID,RELNAME FROM PROCESSRELATEDINFO WHERE FLAG='" + mapType + "'");
            while (resultSet.next()) {
                processMap.put(resultSet.getString("REL_ID"), resultSet.getString("RELNAME"));
            }

        } catch (SQLException sqlException) {
            LoggerUtility.log(logger, "sqlException occurred in getRelationMap method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
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
                LoggerUtility.log(logger, "Finally sqlException occurred in getRelationMap method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
            }
        }
        return processMap;
    }

    public Map getEncodeMap() throws ServiceLocatorException {

        Map processMap = new TreeMap();
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        connection = ConnectionProvider.getInstance().getConnection();
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT VALUE,DESCRIPTION FROM DELEVERYCHANNELDESCRPTION WHERE STATUS='Active'");
            while (resultSet.next()) {
                processMap.put(resultSet.getString("VALUE"), resultSet.getString("DESCRIPTION"));
            }
        } catch (SQLException sqlException) {
            LoggerUtility.log(logger, "sqlException occurred in getEncodeMap method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
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
                LoggerUtility.log(logger, "Finally sqlException occurred in getEncodeMap method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
            }
        }
        return processMap;
    }

    public Map getDashboardPartnerMap(String flowFlag) throws ServiceLocatorException {

        Map partnerMap = new TreeMap();
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        String getDashboardPartnerMapQuery = null;
        try {
            System.out.println("Before Connection::" + DateUtility.getInstance().getCurrentDB2Timestamp());
            connection = ConnectionProvider.getInstance().getConnection();
            System.out.println("After Connection::" + DateUtility.getInstance().getCurrentDB2Timestamp());
            statement = connection.createStatement();
            getDashboardPartnerMapQuery = "SELECT ID,NAME From TP WHERE FLOW_FLAG='" + flowFlag + "'";
            System.out.println("DashboardPartnerMap query:" + getDashboardPartnerMapQuery.toString());
            resultSet = statement.executeQuery(getDashboardPartnerMapQuery);
            System.out.println("Query and resultset start time::" + DateUtility.getInstance().getCurrentDB2Timestamp());
            while (resultSet.next()) {
                partnerMap.put(resultSet.getString("ID"), resultSet.getString("NAME"));
            }
            System.out.println("Resultset end time::" + DateUtility.getInstance().getCurrentDB2Timestamp());
        } catch (SQLException sqlException) {
            LoggerUtility.log(logger, "sqlException occurred in getDashboardPartnerMap method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
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
                LoggerUtility.log(logger, "sqlFinally Exception occurred in getDashboardPartnerMap method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
            }
        }
        return partnerMap;
    }

    public List getSenderIdlist(String flowFlag) throws ServiceLocatorException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String senderIdlistQuery = null;
        connection = ConnectionProvider.getInstance().getConnection();
        List sendeIdMap = new ArrayList();
        try {
            senderIdlistQuery = "SELECT unique(TP.ID) FROM TP JOIN FILES ON (TP.ID=FILES.SENDER_ID) AND FILES.FLOWFLAG='" + flowFlag + "'";
            System.out.println("getSenderIdlist query:" + senderIdlistQuery.toString());
            preparedStatement = connection.prepareStatement(senderIdlistQuery);
            resultSet = preparedStatement.executeQuery();
            System.out.println("Query and resultset start time::" + DateUtility.getInstance().getCurrentDB2Timestamp());
            while (resultSet.next()) {
                if (resultSet.getString("ID") != null && !"".equals(resultSet.getString("ID"))) {
                    sendeIdMap.add(resultSet.getString("ID"));
                }
            }
            System.out.println("Resultset end time::" + DateUtility.getInstance().getCurrentDB2Timestamp());
        } catch (SQLException sqlException) {
            LoggerUtility.log(logger, "sqlException occurred in getSenderIdlist method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
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
                LoggerUtility.log(logger, "sqlException occurred in getSenderIdlist method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
            }
        }

        return sendeIdMap;
    }

    public List getReciverIdlist(String flowFlag) throws ServiceLocatorException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String reciverIdlistQuery = null;
        connection = ConnectionProvider.getInstance().getConnection();
        List receiverIdMap = new ArrayList();
        try {
            reciverIdlistQuery = "SELECT unique(TP.ID) FROM TP JOIN FILES ON (TP.ID=FILES.RECEIVER_ID) AND FILES.FLOWFLAG='" + flowFlag + "'";
            System.out.println("getReciverIdlist query:" + reciverIdlistQuery.toString());
            preparedStatement = connection.prepareStatement(reciverIdlistQuery);
            resultSet = preparedStatement.executeQuery();
            System.out.println("Query and resultset start time::" + DateUtility.getInstance().getCurrentDB2Timestamp());
            while (resultSet.next()) {
                if (resultSet.getString("ID") != null && !"".equals(resultSet.getString("ID"))) {
                    receiverIdMap.add(resultSet.getString("ID"));
                }
            }
            System.out.println("Resultset end time::" + DateUtility.getInstance().getCurrentDB2Timestamp());
        } catch (SQLException sqlException) {
            LoggerUtility.log(logger, "sqlException occurred in getReciverIdlist method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
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
                LoggerUtility.log(logger, "sqlException occurred in getReciverIdlist method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
            }
        }
        return receiverIdMap;
    }

    public List getSenderNamelist(String flowFlag) throws ServiceLocatorException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String senderNamelistQuery = null;
        connection = ConnectionProvider.getInstance().getConnection();
        List SenderNameMap = new ArrayList();
        try {
            senderNamelistQuery = "SELECT unique(TP.NAME) FROM TP JOIN FILES ON (TP.ID=FILES.SENDER_ID) AND FILES.FLOWFLAG='" + flowFlag + "'";
            System.out.println("getSenderNamelist query:" + senderNamelistQuery.toString());
            preparedStatement = connection.prepareStatement(senderNamelistQuery);
            resultSet = preparedStatement.executeQuery();
            System.out.println("Query and resultset start time::" + DateUtility.getInstance().getCurrentDB2Timestamp());
            while (resultSet.next()) {
                if (resultSet.getString("NAME") != null && !"".equals(resultSet.getString("NAME"))) {
                    SenderNameMap.add(resultSet.getString("NAME"));
                }
            }
            System.out.println("Resultset end time::" + DateUtility.getInstance().getCurrentDB2Timestamp());
        } catch (SQLException sqlException) {
            LoggerUtility.log(logger, "sqlException occurred in getSenderNamelist method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
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
                LoggerUtility.log(logger, "sqlFinally Exception occurred in getSenderNamelist method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
            }
        }
        return SenderNameMap;
    }

    public List getReciverNamelist(String flowFlag) throws ServiceLocatorException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String reciverNamelistQuery = null;
        connection = ConnectionProvider.getInstance().getConnection();
        List receiverNameMap = new ArrayList();
        try {
            reciverNamelistQuery = "SELECT unique(TP.NAME) FROM TP JOIN FILES ON (TP.ID=FILES.RECEIVER_ID) AND FILES.FLOWFLAG='" + flowFlag + "'";
            System.out.println("getReciverNamelist query:" + reciverNamelistQuery.toString());
            preparedStatement = connection.prepareStatement(reciverNamelistQuery);
            resultSet = preparedStatement.executeQuery();
            System.out.println("Query and resultset start time::" + DateUtility.getInstance().getCurrentDB2Timestamp());
            while (resultSet.next()) {
                if (resultSet.getString("NAME") != null && !"".equals(resultSet.getString("NAME"))) {
                    receiverNameMap.add(resultSet.getString("NAME"));
                }
            }
            System.out.println("Resultset end time::" + DateUtility.getInstance().getCurrentDB2Timestamp());
        } catch (SQLException sqlException) {
            LoggerUtility.log(logger, "sqlException occurred in getReciverNamelist method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
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
                LoggerUtility.log(logger, "sqlFinally Exception occurred in getReciverNamelist method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
            }
        }
        return receiverNameMap;
    }

    public String getEmail() throws ServiceLocatorException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String emailQuery = null;
        connection = ConnectionProvider.getInstance().getConnection();
        String emailId = null;
        String responseString = "";
        try {
            emailQuery = "select EMAIL from M_USER LEFT OUTER JOIN M_USER_ROLES on (M_USER.ID=M_USER_ROLES.USER_ID) where ROLE_ID=100 AND ACTIVE='A'";
            preparedStatement = connection.prepareStatement(emailQuery);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                emailId = resultSet.getString("EMAIL");
                responseString += emailId + "|";
            }
        } catch (SQLException sqlException) {
            LoggerUtility.log(logger, "sqlException occurred in getEmail method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
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
                LoggerUtility.log(logger, "Finally  sqlException occurred in getEmail method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
            }
        }

        return responseString;
    }

    public Map getUserMap() throws ServiceLocatorException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String userMapQuery = null;
        connection = ConnectionProvider.getInstance().getConnection();
        Map userMap = new TreeMap();

        try {
            userMapQuery = "select ID,concat(FNME,LNME) as cname from M_USER as MU left outer join M_USER_ROLES as MUR on (MU.id=MUR.USER_ID)WHERE MUR.ROLE_ID != 1 AND ACTIVE='A' ";
            preparedStatement = connection.prepareStatement(userMapQuery);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                userMap.put(resultSet.getString("ID"), resultSet.getString("cname"));
            }
        } catch (SQLException sqlException) {
            LoggerUtility.log(logger, "sqlException occurred in getUserMap method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
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
                LoggerUtility.log(logger, "Finally sqlException occurred in getUserMap method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
            }
        }

        return userMap;
    }

    public int getUserIds(String loginid) throws ServiceLocatorException {
        PreparedStatement preparedStatement = null;
        Connection connection = null;
        ResultSet resultSet = null;
        String userIdsQuery = null;
        int userId = 0;
        connection = ConnectionProvider.getInstance().getConnection();
        try {
            userIdsQuery = "select ID from M_user where LOGINID LIKE '%" + loginid + "%'";
            preparedStatement = connection.prepareStatement(userIdsQuery);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                userId = resultSet.getInt("ID");
            }
        } catch (SQLException sqlException) {
            LoggerUtility.log(logger, "sqlException occurred in getUserIds method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
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
                LoggerUtility.log(logger, "Finally sqlException occurred in getUserIds method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
            }
        }
        return userId;
    }

    public List getNetworkVanList() throws ServiceLocatorException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String networkVanListQuery = null;
        System.out.println("Before Connection::" + DateUtility.getInstance().getCurrentDB2Timestamp());
        connection = ConnectionProvider.getInstance().getConnection();
        System.out.println("After Connection::" + DateUtility.getInstance().getCurrentDB2Timestamp());
        List NetworkvanMap = new ArrayList();
        try {
            networkVanListQuery = "SELECT unique(FILES.NETWORK_VAN) FROM FILES where FILES.NETWORK_VAN is not null";
            System.out.println("CommunicationProtocols query:" + networkVanListQuery.toString());
            preparedStatement = connection.prepareStatement(networkVanListQuery);
            resultSet = preparedStatement.executeQuery();
            System.out.println("Query and resultset start time::" + DateUtility.getInstance().getCurrentDB2Timestamp());
            while (resultSet.next()) {
                if (resultSet.getString("NETWORK_VAN") != null && !"".equals(resultSet.getString("NETWORK_VAN"))) {
                    NetworkvanMap.add(resultSet.getString("NETWORK_VAN"));
                }
            }
            System.out.println("Resultset end time::" + DateUtility.getInstance().getCurrentDB2Timestamp());
        } catch (SQLException sqlException) {
            LoggerUtility.log(logger, "sqlException occurred in getNetworkVanList method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
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
                LoggerUtility.log(logger, "Finally sqlException occurred in getNetworkVanList method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
            }
        }

        return NetworkvanMap;
    }

    public List getListName() throws ServiceLocatorException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String listNameQuery = null;
        connection = ConnectionProvider.getInstance().getOracleConnection();
        List listNameMap = new ArrayList();
        try {
            listNameQuery = "SELECT  DISTINCT(LIST_NAME) FROM CODELIST_XREF_ITEM";
            preparedStatement = connection.prepareStatement(listNameQuery);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                listNameMap.add(resultSet.getString("LIST_NAME"));
            }
        } catch (SQLException sqlException) {
            LoggerUtility.log(logger, "sqlException occurred in getListName method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
            throw new ServiceLocatorException(sqlException);
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
                LoggerUtility.log(logger, "Finally sqlException occurred in getListName method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
            }
        }
        return listNameMap;
    }

    public String getPartnerNameById(String parterId) throws ServiceLocatorException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String partnerNameByIdQuery = null;
        connection = ConnectionProvider.getInstance().getConnection();
        String partnerName = null;
        try {
            partnerNameByIdQuery = "SELECT ID,NAME From TP WHERE ID='" + parterId + "'";
            preparedStatement = connection.prepareStatement(partnerNameByIdQuery);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                partnerName = resultSet.getString("NAME");
            }
        } catch (SQLException sqlException) {
            LoggerUtility.log(logger, "sqlException occurred in getPartnerNameById method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
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
                LoggerUtility.log(logger, "Finally sqlException occurred in getPartnerNameById method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
            }
        }
        return partnerName;
    }

    public Map getTransactionType(Map flowMap) {
        Map flows = new LinkedHashMap();
        flows.put("All", "All");
        if (flowMap.containsValue("Manufacturing")) {
            flows.put("850", "Manufacturing PO");
            flows.put("856", "Manufacturing Shipments");
            flows.put("810", "Manufacturing Invoice");
            flows.put("820", "Manufacturing Payments");
            flows.put("846", "Manufacturing Inventory");
        }
        if (flowMap.containsValue("Logistics")) {
            flows.put("204", "Logistics Load Tender");
            flows.put("990", "Logistics Response");
            flows.put("214", "Logistics Shipment");
            flows.put("210", "Logistics Invoice");

        }

        return flows;
    }

    public List getPartnerListAppConfig() throws ServiceLocatorException {
        List partnerList = new ArrayList(); // Key-Description
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        String queryString = null;
        connection = ConnectionProvider.getInstance().getConnection();
        try {
            statement = connection.createStatement();
            queryString = "SELECT DISTINCT(NAME) AS TPNAME FROM TP WHERE STATUS='ACTIVE' ORDER BY NAME ASC";
            resultSet = statement.executeQuery(queryString);
            while (resultSet.next()) {
                if (resultSet.getString("TPNAME") != null && !"".equals(resultSet.getString("TPNAME"))) {
                    partnerList.add(resultSet.getString("TPNAME"));
                }
            }
        } catch (SQLException ex) {
            LoggerUtility.log(logger, " SQLException occurred in Get DashBoard  getPartnerList :: " + ex.getMessage(), Level.ERROR, ex.getCause());
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
                LoggerUtility.log(logger, " Finally Block SQLException occurred in Get DashBoard  PartnerMap :: " + ex.getMessage(), Level.ERROR, ex.getCause());
            }
        }
        return partnerList;
    }

    public Map getTradingPartnersMap() throws ServiceLocatorException {
        Map sendeIdMap = new TreeMap(); // Key-Description
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        String queryString = null;
        connection = ConnectionProvider.getInstance().getConnection();
        try {
            statement = connection.createStatement();
            queryString = "SELECT ID, NAME || '(' || ID || ')' AS TPNAME FROM TP ORDER BY NAME ASC";
            resultSet = statement.executeQuery(queryString);
            while (resultSet.next()) {
                sendeIdMap.put(resultSet.getString("ID"), resultSet.getString("TPNAME").trim().toUpperCase());
            }
            sendeIdMap = sortIntStringByValue(sendeIdMap);
        } catch (SQLException ex) {
            LoggerUtility.log(logger, " SQLException occurred in Get DashBoard  PartnerMap :: " + ex.getMessage(),
                    Level.ERROR, ex.getCause());
        } finally {
            try {
                // resultSet Object Checking if it's null then close and set
                // null
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
                LoggerUtility.log(logger,
                        " Finally Block SQLException occurred in Get DashBoard  PartnerMap :: " + ex.getMessage(),
                        Level.ERROR, ex.getCause());
            }
        }
        return sendeIdMap;
    }

    public static Map sortIntStringByValue(Map<String, String> map) {
        Map result = new LinkedHashMap();
        Set<Entry<String, String>> set = map.entrySet();
        List<Entry<String, String>> list = new ArrayList<Entry<String, String>>(set);
        Collections.sort(list, new Comparator<Map.Entry<String, String>>() {

            public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2) {
                return (o1.getValue().trim().toUpperCase()).compareTo(o2.getValue().trim().toUpperCase());
            }
        });
        for (Map.Entry<String, String> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }

    public Map<String, String> getAllTradingPartners() throws ServiceLocatorException {
        Map<String, String> sendersMap = new TreeMap<String, String>(); // Key-Description
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        String queryString = null;
        connection = ConnectionProvider.getInstance().getConnection();
        try {
            statement = connection.createStatement();
            queryString = "SELECT ID,NAME FROM TP ORDER BY ID ASC";
            resultSet = statement.executeQuery(queryString);
            while (resultSet.next()) {
                sendersMap.put(resultSet.getString("ID"), resultSet.getString("NAME"));
            }
        } catch (SQLException ex) {
            LoggerUtility.log(logger, " SQLException occurred in Get DashBoard sendersMap :: " + ex.getMessage(),
                    Level.ERROR, ex.getCause());
        } finally {
            try {
				// resultSet Object Checking if it's null then close and set
                // null
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
                LoggerUtility.log(logger,
                        " Finally Block SQLException occurred in Get DashBoard PartnerMap :: " + ex.getMessage(),
                        Level.ERROR, ex.getCause());
            }
        }
        return sendersMap;
    }

}
