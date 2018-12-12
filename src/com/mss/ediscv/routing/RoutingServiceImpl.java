/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mss.ediscv.routing;

import com.mss.ediscv.util.ConnectionProvider;
import com.mss.ediscv.util.DateUtility;
import com.mss.ediscv.util.ServiceLocatorException;
import com.mss.ediscv.util.WildCardSql;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import com.mss.ediscv.util.LoggerUtility;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author miracle
 */
public class RoutingServiceImpl implements RoutingService {

    private static Logger logger = LogManager.getLogger(RoutingServiceImpl.class.getName());
    String responseString = null;

    public String addRouting(RoutingAction routingAction) throws ServiceLocatorException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = ConnectionProvider.getInstance().getConnection();
            preparedStatement = connection.prepareStatement("INSERT INTO ROUTERINFO(ROUTER_NAME, STATUS, ACCEPTORLOOKUPALIAS, INTERNALROUTEREMAIL, DESTMAILBOX, SYSTEMTYPE, DIRECTION, CREATEDDATE,ENVELOPE) VALUES(?,?,?,?,?,?,?,?,?)");

            preparedStatement.setString(1, routingAction.getName());
            preparedStatement.setString(2, routingAction.getStatus());
            preparedStatement.setString(3, routingAction.getAcceptorLookupAlias());
            preparedStatement.setString(4, routingAction.getInternalRouteEmail());
            preparedStatement.setString(5, routingAction.getDestMailBox());
            preparedStatement.setString(6, routingAction.getSystemType());
            preparedStatement.setString(7, routingAction.getDirection());
            preparedStatement.setTimestamp(8, DateUtility.getInstance().getCurrentDB2Timestamp());
            preparedStatement.setString(9, routingAction.getEnvelope());

            int i = preparedStatement.executeUpdate();
            if (i > 0) {
                responseString = "<font color='green'>Routing added succesfully.</font>";
            } else {
                responseString = "<font color='red'>Please try again!</font>";
            }
        } catch (SQLException sqlException) {
            responseString = "<font color='red'>Please try with different Id!</font>";
            LoggerUtility.log(logger, "SQLException occurred in addRouting method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
        } catch (Exception exception) {
            responseString = "<font color='red'>Please try again!</font>";
            LoggerUtility.log(logger, "Exception occurred in addRouting method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
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
                LoggerUtility.log(logger, "finally SQLException occurred in addRouting method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
            }
        }
        return responseString;
    }

    public ArrayList<RoutingBean> buildRoutingQuery(RoutingAction routingAction) throws ServiceLocatorException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        ArrayList<RoutingBean> routingList = new ArrayList<RoutingBean>();
        StringBuilder routingSearchQuery = new StringBuilder();
        try {
            connection = ConnectionProvider.getInstance().getConnection();
            routingSearchQuery.append("SELECT * FROM ROUTERINFO WHERE 1=1 ");
            if (routingAction.getName() != null && !"".equals(routingAction.getName().trim())) {
                routingSearchQuery.append(WildCardSql.getWildCardSql1("ROUTER_NAME", routingAction.getName().trim().toUpperCase()));
            }
            if (routingAction.getStatus() != null && !"".equals(routingAction.getStatus().trim())) {
                routingAction.setStatus(routingAction.getStatus().replace(",", ""));
                routingSearchQuery.append(WildCardSql.getWildCardSql1("STATUS", routingAction.getStatus().trim().toUpperCase()));
            }
            if (routingAction.getAcceptorLookupAlias() != null && !"".equals(routingAction.getAcceptorLookupAlias().trim())) {
                routingSearchQuery.append(WildCardSql.getWildCardSql1("ACCEPTORLOOKUPALIAS", routingAction.getAcceptorLookupAlias().trim().toUpperCase()));
            }
            if (routingAction.getEnvelope() != null && !"".equals(routingAction.getEnvelope().trim())) {
                routingSearchQuery.append(WildCardSql.getWildCardSql1("ENVELOPE", routingAction.getEnvelope().trim().toUpperCase()));
            }
            if (routingAction.getInternalRouteEmail() != null && !"".equals(routingAction.getInternalRouteEmail().trim())) {
                routingSearchQuery.append(WildCardSql.getWildCardSql1("INTERNALROUTEREMAIL", routingAction.getInternalRouteEmail().trim().toUpperCase()));
            }
            if (routingAction.getDestMailBox() != null && !"".equals(routingAction.getDestMailBox().trim())) {
                routingSearchQuery.append(WildCardSql.getWildCardSql1("DESTMAILBOX", routingAction.getDestMailBox().trim().toUpperCase()));
            }
            if (routingAction.getSystemType() != null && !"".equals(routingAction.getSystemType().trim())) {
                routingSearchQuery.append(WildCardSql.getWildCardSql1("SYSTEMTYPE", routingAction.getSystemType().trim().toUpperCase()));
            }
            if (routingAction.getDirection() != null && !"".equals(routingAction.getDirection().trim())) {
                routingSearchQuery.append(WildCardSql.getWildCardSql1("DIRECTION", routingAction.getDirection().trim().toUpperCase()));
            }
            preparedStatement = connection.prepareStatement(routingSearchQuery.toString());

            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                RoutingBean routingBean = new RoutingBean();
                routingBean.setRoutingId(resultSet.getInt("ROUTER_ID"));
                routingBean.setName(resultSet.getString("ROUTER_NAME"));
                routingBean.setStatus(resultSet.getString("STATUS"));
                routingBean.setAcceptorLookupAlias(resultSet.getString("ACCEPTORLOOKUPALIAS"));
                routingBean.setInternalRouteEmail(resultSet.getString("INTERNALROUTEREMAIL"));
                routingBean.setDestMailBox(resultSet.getString("DESTMAILBOX"));
                routingBean.setSystemType(resultSet.getString("SYSTEMTYPE"));
                routingBean.setDirection(resultSet.getString("DIRECTION"));
                routingBean.setEnvelope(resultSet.getString("ENVELOPE"));
                routingBean.setCreatedDate(resultSet.getString("CREATEDDATE"));
                routingBean.setChangedDate(resultSet.getString("MODIFIEDDATE"));
                routingList.add(routingBean);
            }
        } catch (SQLException sqlException) {
            responseString = "<font color='red'>Please try with different Id!</font>";
            LoggerUtility.log(logger, "SQLException occurred in buildRoutingQuery method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
        } catch (Exception exception) {
            responseString = "<font color='red'>Please try again!</font>";
            LoggerUtility.log(logger, "Exception occurred in buildRoutingQuery method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
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
                LoggerUtility.log(logger, "finally SQLException occurred in buildRoutingQuery method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
            }
        }
        return routingList;
    }

    public RoutingAction getRouting(RoutingAction routingAction) throws ServiceLocatorException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        StringBuilder routingSearchQuery = new StringBuilder();
        try {
            connection = ConnectionProvider.getInstance().getConnection();
            routingSearchQuery.append("SELECT * FROM ROUTERINFO WHERE ROUTER_ID=" + routingAction.getRoutingId());
            preparedStatement = connection.prepareStatement(routingSearchQuery.toString());
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                routingAction.setRoutingId(resultSet.getInt("ROUTER_ID"));
                routingAction.setName(resultSet.getString("ROUTER_NAME"));
                routingAction.setStatus(resultSet.getString("STATUS"));
                routingAction.setAcceptorLookupAlias(resultSet.getString("ACCEPTORLOOKUPALIAS"));
                routingAction.setInternalRouteEmail(resultSet.getString("INTERNALROUTEREMAIL"));
                routingAction.setDestMailBox(resultSet.getString("DESTMAILBOX"));
                routingAction.setSystemType(resultSet.getString("SYSTEMTYPE"));
                routingAction.setDirection(resultSet.getString("DIRECTION"));
                routingAction.setEnvelope(resultSet.getString("ENVELOPE"));
            }
        } catch (SQLException sqlException) {
            responseString = "<font color='red'>Please try with different Id!</font>";
            LoggerUtility.log(logger, "SQLException occurred in getRouting method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
        } catch (Exception exception) {
            responseString = "<font color='red'>Please try again!</font>";
            LoggerUtility.log(logger, "Exception occurred in getRouting method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
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
                LoggerUtility.log(logger, "finally SQLException occurred in getRouting method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
            }
        }
        return routingAction;
    }

    public String editRouting(RoutingAction routingAction) throws ServiceLocatorException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = ConnectionProvider.getInstance().getConnection();
            preparedStatement = connection.prepareStatement("UPDATE ROUTERINFO SET ROUTER_NAME=?, STATUS=?, ACCEPTORLOOKUPALIAS=?, INTERNALROUTEREMAIL=?, DESTMAILBOX=?, SYSTEMTYPE=?, DIRECTION=?, MODIFIEDDATE=?,ENVELOPE=? WHERE ROUTER_ID=?");

            preparedStatement.setString(1, routingAction.getName());
            preparedStatement.setString(2, routingAction.getStatus());
            preparedStatement.setString(3, routingAction.getAcceptorLookupAlias());
            preparedStatement.setString(4, routingAction.getInternalRouteEmail());
            preparedStatement.setString(5, routingAction.getDestMailBox());
            preparedStatement.setString(6, routingAction.getSystemType());
            preparedStatement.setString(7, routingAction.getDirection());
            preparedStatement.setTimestamp(8, DateUtility.getInstance().getCurrentDB2Timestamp());
            preparedStatement.setString(9, routingAction.getEnvelope());
            preparedStatement.setInt(10, routingAction.getRoutingId());

            int i = preparedStatement.executeUpdate();
            if (i > 0) {
                responseString = "<font color='green'>Routing updated succesfully.</font>";
            } else {
                responseString = "<font color='red'>Please try again!</font>";
            }
        } catch (SQLException sqlException) {
            responseString = "<font color='red'>Please try with different Id!</font>";
            LoggerUtility.log(logger, "SQLException occurred in editRouting method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
        } catch (Exception exception) {
            responseString = "<font color='red'>Please try again!</font>";
            LoggerUtility.log(logger, "Exception occurred in editRouting method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
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
                LoggerUtility.log(logger, "finally SQLException occurred in editRouting method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
            }
        }
        return responseString;
    }
}
