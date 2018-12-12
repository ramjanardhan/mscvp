/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mss.ediscv.tradingPartner;

import com.mss.ediscv.util.ConnectionProvider;
import com.mss.ediscv.util.DateUtility;
import com.mss.ediscv.util.ServiceLocatorException;
import com.mss.ediscv.util.WildCardSql;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import com.mss.ediscv.util.LoggerUtility;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author miracle1
 */
public class TradingPartnerServiceImpl implements TradingPartnerService {

    private static Logger logger = LogManager.getLogger(TradingPartnerServiceImpl.class.getName());
    String responseString = null;
    private ArrayList<TradingPartnerBean> tradingPartnerList;

    public String addTP(TradingPartnerAction tpAction) throws ServiceLocatorException {
        Connection connection = null;
        CallableStatement callableStatement = null;
        try {
            connection = ConnectionProvider.getInstance().getConnection();
            callableStatement = connection.prepareCall("call spTradingPartner(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

            callableStatement.setString(1, tpAction.getCommId());
            callableStatement.setString(2, tpAction.getCommName());
            callableStatement.setString(3, tpAction.getCreatedBy());
            callableStatement.setTimestamp(4, DateUtility.getInstance().getCurrentDB2Timestamp());
            callableStatement.setString(5, tpAction.getCreatedBy());
            callableStatement.setTimestamp(6, DateUtility.getInstance().getCurrentDB2Timestamp());
            callableStatement.setString(7, tpAction.getTradingPartnerName());
            callableStatement.setString(8, tpAction.getContactName());
            callableStatement.setString(9, tpAction.getBvrUdiCommId());
            callableStatement.setString(10, tpAction.getBvrUdiName());
            callableStatement.setString(11, tpAction.getPhno());
            callableStatement.setString(12, tpAction.getEmail());
            callableStatement.setString(13, tpAction.getAddress());
            callableStatement.setString(14, tpAction.getCity());
            callableStatement.setString(15, tpAction.getState());
            callableStatement.setString(16, tpAction.getZip());
            callableStatement.setString(17, tpAction.getNetwork());
            callableStatement.setString(18, tpAction.getUrl());
            callableStatement.setString(19, tpAction.getBasic());
            callableStatement.setString(20, tpAction.getSoq());
            callableStatement.setString(21, tpAction.getStore());
            callableStatement.setString(22, tpAction.getMaster());
            callableStatement.setString(23, tpAction.getDeveloping());
            callableStatement.setString(24, tpAction.getVendorNo());
            callableStatement.setString(25, tpAction.getOrderDuns());
            callableStatement.setString(26, tpAction.getShipDuns());
            callableStatement.setString(27, tpAction.getPayDuns());
            callableStatement.setString(28, tpAction.getDeptNo());
            callableStatement.setString(29, tpAction.getBuyerName());
            callableStatement.setString(30, tpAction.getBuyerPhone());
            callableStatement.setString(31, tpAction.getBuyerEmail());
            callableStatement.setString(32, tpAction.getCsName());
            callableStatement.setString(33, tpAction.getCsPhone());
            callableStatement.setString(34, tpAction.getCsEmail());
            callableStatement.setInt(35, 1);
            callableStatement.setString(36, tpAction.getDefaultFlowId());
            callableStatement.setString(37, tpAction.getNotes());
            callableStatement.registerOutParameter(38, Types.VARCHAR);
            int updatedRows = callableStatement.executeUpdate();
            responseString = "<font color='green'>" + callableStatement.getString(38) + "</font>";
        } catch (SQLException sqlException) {
            responseString = "<font color='red'>Please try with different Id!</font>";
            LoggerUtility.log(logger, "SQLException occurred in addTP method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
        } catch (Exception exception) {
            responseString = "<font color='red'>Please try again!</font>";
            LoggerUtility.log(logger, "Exception occurred in addTP method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
        } finally {
            try {
                if (callableStatement != null) {
                    callableStatement.close();
                    callableStatement = null;
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

    public ArrayList<TradingPartnerBean> buildTradingQuery(TradingPartnerAction tradingPartnerAction) throws ServiceLocatorException {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        StringBuilder tradingSearchQuery = new StringBuilder();
        try {
            String id = tradingPartnerAction.getTpid();
            String name = tradingPartnerAction.getTpname();
            tradingSearchQuery.append("select tp.name,tp_details.tp_id,tp_details.email,tp_details.phone_number,tp_details.ADDRESS from tp LEFT OUTER JOIN tp_details on(tp_details.TP_ID=tp.ID) ");
            tradingSearchQuery.append(" WHERE 1=1 AND tp.FLOW_FLAG='" + tradingPartnerAction.getDefaultFlowId() + "'");
            if (id != null && !"".equals(id.trim())) {
                tradingSearchQuery.append(WildCardSql.getWildCardSql1("TP_DETAILS.TP_ID", id.trim().toUpperCase()));
            }
            if (name != null && !"".equals(name.trim())) {
                tradingSearchQuery.append(WildCardSql.getWildCardSql1("TP.NAME", name.trim().toUpperCase()));
            }
            String searchQuery = tradingSearchQuery.toString();

            connection = ConnectionProvider.getInstance().getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(searchQuery);
            tradingPartnerList = new ArrayList<TradingPartnerBean>();
            while (resultSet.next()) {
                TradingPartnerBean tradingPartnerBean = new TradingPartnerBean();
                tradingPartnerBean.setId(resultSet.getString("TP_ID"));
                tradingPartnerBean.setName(resultSet.getString("NAME"));
                tradingPartnerBean.setEmail(resultSet.getString("EMAIL"));
                tradingPartnerBean.setPhno(resultSet.getString("PHONE_NUMBER"));
                tradingPartnerBean.setAddress(resultSet.getString("ADDRESS"));
                tradingPartnerList.add(tradingPartnerBean);
            }
        } catch (SQLException sqlException) {
            LoggerUtility.log(logger, "SQLException occurred in buildTradingQuery method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
        } catch (Exception exception) {
            LoggerUtility.log(logger, "Exception occurred in buildTradingQuery method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
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
            LoggerUtility.log(logger, "finally SQLException occurred in buildTradingQuery method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
        } 
        }
        return tradingPartnerList;
    }

    public TradingPartnerAction tpEdit(TradingPartnerAction tradingPartnerAction) throws ServiceLocatorException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = ConnectionProvider.getInstance().getConnection();
            preparedStatement = connection.prepareStatement("select TP.ID as tpId,tp.NAME as tpName,TP_NAME,CONTACT_NAME,BVR_UDI_ID,tp_details.NAME as bvrName,"
                    + "PHONE_NUMBER,EMAIL,ADDRESS,CITY,STATE,ZIP,NETWORK,URL,PO_TYPE_BASIC,PO_TYPE_SOQ,INVOICE_TYPE_STORE,"
                    + "INVOICE_TYPE_MASTER,DEVELOPING,VENDOR,ORDER_DUNS,SHIP_DUNS,PAY_DUNS,DEPARTMENT_NUMBER,BUYER_NAME,"
                    + "BUYER_PHONE,BUYER_EMAIL,CUSTOMER_NAME,NOTES,CUSTOMER_PHONE,CUSTOMER_EMAIL from tp LEFT OUTER JOIN tp_details on (tp.ID=tp_details.TP_ID) where tp.ID=? and tp.FLOW_FLAG=?");
            preparedStatement.setString(1, tradingPartnerAction.getId());
            preparedStatement.setString(2, tradingPartnerAction.getDefaultFlowId());
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                tradingPartnerAction.setCommId(resultSet.getString("tpId"));
                tradingPartnerAction.setCommName(resultSet.getString("tpName"));
                tradingPartnerAction.setTradingPartnerName(resultSet.getString("TP_NAME"));
                tradingPartnerAction.setContactName(resultSet.getString("CONTACT_NAME"));
                tradingPartnerAction.setBvrUdiCommId(resultSet.getString("BVR_UDI_ID"));
                tradingPartnerAction.setBvrUdiName(resultSet.getString("bvrName"));
                tradingPartnerAction.setPhno(resultSet.getString("PHONE_NUMBER"));
                tradingPartnerAction.setEmail(resultSet.getString("EMAIL"));
                tradingPartnerAction.setAddress(resultSet.getString("ADDRESS"));
                tradingPartnerAction.setCity(resultSet.getString("CITY"));
                tradingPartnerAction.setState(resultSet.getString("STATE"));
                tradingPartnerAction.setZip(resultSet.getString("ZIP"));
                tradingPartnerAction.setNetwork(resultSet.getString("NETWORK"));
                tradingPartnerAction.setUrl(resultSet.getString("URL"));
                tradingPartnerAction.setBasic(resultSet.getString("PO_TYPE_BASIC"));
                tradingPartnerAction.setSoq(resultSet.getString("PO_TYPE_SOQ"));
                tradingPartnerAction.setStore(resultSet.getString("INVOICE_TYPE_STORE"));
                tradingPartnerAction.setMaster(resultSet.getString("INVOICE_TYPE_MASTER"));
                tradingPartnerAction.setDeveloping(resultSet.getString("DEVELOPING"));
                tradingPartnerAction.setVendorNo(resultSet.getString("VENDOR"));
                tradingPartnerAction.setOrderDuns(resultSet.getString("ORDER_DUNS"));
                tradingPartnerAction.setShipDuns(resultSet.getString("SHIP_DUNS"));
                tradingPartnerAction.setPayDuns(resultSet.getString("PAY_DUNS"));
                tradingPartnerAction.setDeptNo(resultSet.getString("DEPARTMENT_NUMBER"));
                tradingPartnerAction.setBuyerName(resultSet.getString("BUYER_NAME"));
                tradingPartnerAction.setBuyerPhone(resultSet.getString("BUYER_PHONE"));
                tradingPartnerAction.setBuyerEmail(resultSet.getString("BUYER_EMAIL"));
                tradingPartnerAction.setCsName(resultSet.getString("CUSTOMER_NAME"));
                tradingPartnerAction.setCsPhone(resultSet.getString("CUSTOMER_PHONE"));
                tradingPartnerAction.setCsEmail(resultSet.getString("CUSTOMER_EMAIL"));
                tradingPartnerAction.setNotes(resultSet.getString("NOTES"));
            }
        }catch (SQLException sqlException) {
            LoggerUtility.log(logger, "SQLException occurred in tpEdit method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
        } catch (Exception exception) {
            LoggerUtility.log(logger, "Exception occurred in tpEdit method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
        }  finally {
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
            LoggerUtility.log(logger, "finally SQLException occurred in tpEdit method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
        }
        }
        return tradingPartnerAction;
    }

    public String editTP(TradingPartnerAction tpAction) throws ServiceLocatorException {
        Connection connection = null;
        CallableStatement callableStatement = null;
        try {
            connection = ConnectionProvider.getInstance().getConnection();
            callableStatement = connection.prepareCall("call spTradingPartner(?,?,?,?,?, ?,?,?,?,?, ?,?,?,?,?, ?,?,?,?,?, ?,?,?,?,?, ?,?,?,?,?, ?,?,?,?,?, ?,?,?)");
            callableStatement.setString(1, tpAction.getCommId());
            callableStatement.setString(2, tpAction.getCommName());
            callableStatement.setString(3, tpAction.getCreatedBy());
            callableStatement.setTimestamp(4, DateUtility.getInstance().getCurrentDB2Timestamp());
            callableStatement.setString(5, tpAction.getCreatedBy());
            callableStatement.setTimestamp(6, DateUtility.getInstance().getCurrentDB2Timestamp());
            callableStatement.setString(7, tpAction.getTradingPartnerName());
            callableStatement.setString(8, tpAction.getContactName());
            callableStatement.setString(9, tpAction.getBvrUdiCommId());
            callableStatement.setString(10, tpAction.getBvrUdiName());
            callableStatement.setString(11, tpAction.getPhno());
            callableStatement.setString(12, tpAction.getEmail());
            callableStatement.setString(13, tpAction.getAddress());
            callableStatement.setString(14, tpAction.getCity());
            callableStatement.setString(15, tpAction.getState());
            callableStatement.setString(16, tpAction.getZip());
            callableStatement.setString(17, tpAction.getNetwork());
            callableStatement.setString(18, tpAction.getUrl());
            callableStatement.setString(19, tpAction.getBasic());
            callableStatement.setString(20, tpAction.getSoq());
            callableStatement.setString(21, tpAction.getStore());
            callableStatement.setString(22, tpAction.getMaster());
            callableStatement.setString(23, tpAction.getDeveloping());
            callableStatement.setString(24, tpAction.getVendorNo());
            callableStatement.setString(25, tpAction.getOrderDuns());
            callableStatement.setString(26, tpAction.getShipDuns());
            callableStatement.setString(27, tpAction.getPayDuns());
            callableStatement.setString(28, tpAction.getDeptNo());
            callableStatement.setString(29, tpAction.getBuyerName());
            callableStatement.setString(30, tpAction.getBuyerPhone());
            callableStatement.setString(31, tpAction.getBuyerEmail());
            callableStatement.setString(32, tpAction.getCsName());
            callableStatement.setString(33, tpAction.getCsPhone());
            callableStatement.setString(34, tpAction.getCsEmail());
            callableStatement.setInt(35, 2);
            callableStatement.setString(36, tpAction.getDefaultFlowId());
            callableStatement.setString(37, tpAction.getNotes());
            callableStatement.registerOutParameter(38, Types.VARCHAR);
            int updatedRows = callableStatement.executeUpdate();
            responseString = "<font color='green'>" + callableStatement.getString(38) + "</font>";
        } catch (SQLException sqlException) {
            responseString = "<font color='red'>Please try with different Id!</font>";
            LoggerUtility.log(logger, "SQLException occurred in editTP method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
        } catch (Exception exception) {
            responseString = "<font color='red'>Please try again!</font>";
            LoggerUtility.log(logger, "Exception occurred in editTP method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
        }  finally {
            try {
                if (callableStatement != null) {
                    callableStatement.close();
                    callableStatement = null;
                }
                if (connection != null) {
                    connection.close();
                    connection = null;
                }
            } catch (SQLException sqlException) {
            LoggerUtility.log(logger, "finally SQLException occurred in editTP method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
        }
        }
        return responseString;
    }
}
