/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mss.ediscv.logisticsinvoice;

import com.mss.ediscv.util.ConnectionProvider;
import com.mss.ediscv.util.DataSourceDataProvider;
import com.mss.ediscv.util.DateUtility;
import com.mss.ediscv.util.ServiceLocatorException;
import com.mss.ediscv.util.WildCardSql;
import java.sql.*;
import java.util.ArrayList;
import com.mss.ediscv.util.LoggerUtility;
import java.util.Map;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author miracle
 */
public class LogisticsInvoiceServiceImpl implements LogisticsInvoiceService {

    String tmp_Recieved_From = "";
    String tmp_Recieved_ToTime = "";
    private ArrayList<LogisticsInvoiceBean> invList;
    private LogisticsInvoiceBean logisticsBean;
    private static Logger logger = LogManager.getLogger(LogisticsInvoiceServiceImpl.class.getName());

    public ArrayList<LogisticsInvoiceBean> buildLogInvoiceQuery(LogisticsInvoiceAction logisticsInvoiceAction) throws ServiceLocatorException {

        String doctype = "";
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            StringBuilder invSearchQuery = new StringBuilder();
            String datepickerTo = logisticsInvoiceAction.getDatepickerTo();
            String datepickerFrom = logisticsInvoiceAction.getDatepickerfrom();
            String invSenderId = logisticsInvoiceAction.getInvSenderId();
            String invSenderName = logisticsInvoiceAction.getInvSenderName();
            String invRecId = logisticsInvoiceAction.getInvReceiverId();
            String invRecName = logisticsInvoiceAction.getInvReceiverName();
            if ((logisticsInvoiceAction.getDocType() != null) && (!logisticsInvoiceAction.getDocType().equals("-1"))) {
                doctype = logisticsInvoiceAction.getDocType();
            }
            String corrattribute = logisticsInvoiceAction.getCorrattribute();
            String corrvalue = logisticsInvoiceAction.getCorrvalue();
            String corrattribute1 = logisticsInvoiceAction.getCorrattribute1();
            String corrvalue1 = logisticsInvoiceAction.getCorrvalue1();
            String status = logisticsInvoiceAction.getStatus();
            String ackStatus = logisticsInvoiceAction.getAckStatus();
            if ("ARCHIVE".equals(logisticsInvoiceAction.getDatabase())) {
                invSearchQuery.append("SELECT DISTINCT(ARCHIVE_FILES.FILE_ID) as FILE_ID, "
                        + "ARCHIVE_TRANSPORT_INVOICE.INVOICE_NUMBER as INVOICE_NUMBER,ARCHIVE_TRANSPORT_INVOICE.ID as ID,"
                        + "ARCHIVE_TRANSPORT_INVOICE.PO_NUMBER as PO_NUMBER,ARCHIVE_FILES.SENDER_ID,ARCHIVE_FILES.RECEIVER_ID,"
                        + "ARCHIVE_TRANSPORT_INVOICE.SHIPMENT_ID as SHIPMENT_ID,"
                        + "ARCHIVE_TRANSPORT_INVOICE.TOTAL_WEIGHT as TOTAL_WEIGHT,"
                        + "ARCHIVE_TRANSPORT_INVOICE.TOTAL_AMOUNT as TOTAL_AMOUNT,"
                        + "ARCHIVE_TRANSPORT_INVOICE.INVOICE_DATE as INVOICE_DATE,ARCHIVE_FILES.DATE_TIME_RECEIVED,"
                        + "ARCHIVE_FILES.STATUS as STATUS,"
                        + "ARCHIVE_FILES.ACK_STATUS as ACK_STATUS"
                        + " FROM ARCHIVE_TRANSPORT_INVOICE "
                        + "LEFT OUTER JOIN ARCHIVE_FILES ON (ARCHIVE_TRANSPORT_INVOICE.FILE_ID =ARCHIVE_FILES.FILE_ID) ");
                       
                invSearchQuery.append(" WHERE 1=1 ");
                if (datepickerFrom != null && !"".equals(datepickerFrom)) {
                    tmp_Recieved_From = DateUtility.getInstance().DateViewToDBCompare(datepickerFrom);
                    invSearchQuery.append(" AND ARCHIVE_FILES.DATE_TIME_RECEIVED >= '" + tmp_Recieved_From + "'");
                }
                if (datepickerTo != null && !"".equals(datepickerTo)) {
                    tmp_Recieved_ToTime = DateUtility.getInstance().DateViewToDBCompare(datepickerTo);
                    invSearchQuery.append(" AND ARCHIVE_FILES.DATE_TIME_RECEIVED <= '" + tmp_Recieved_ToTime + "'");
                }
                if (((corrattribute != null) && (corrattribute.equalsIgnoreCase("Invoice Number"))) && (corrvalue != null && !"".equals(corrvalue.trim()))) {
                    invSearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_FILES.PRI_KEY_VAL", corrvalue.trim().toUpperCase()));
                }
                if (((corrattribute1 != null) && (corrattribute1.equalsIgnoreCase("Invoice Number"))) && (corrvalue1 != null && !"".equals(corrvalue1.trim()))) {
                    invSearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_FILES.PRI_KEY_VAL", corrvalue1.trim().toUpperCase()));
                }
                if (((corrattribute != null) && (corrattribute.equalsIgnoreCase("PO Number"))) && (corrvalue != null && !"".equals(corrvalue.trim()))) {
                    invSearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_TRANSPORT_INVOICE.PO_NUMBER", corrvalue.trim().toUpperCase()));
                }
                if (((corrattribute1 != null) && (corrattribute1.equalsIgnoreCase("PO Number"))) && (corrvalue1 != null && !"".equals(corrvalue1.trim()))) {
                    invSearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_TRANSPORT_INVOICE.PO_NUMBER", corrvalue1.trim().toUpperCase()));
                }
                if (((corrattribute != null) && (corrattribute.equalsIgnoreCase("Shipment Number"))) && (corrvalue != null && !"".equals(corrvalue.trim()))) {
                    invSearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_TRANSPORT_INVOICE.SHIPMENT_ID", corrvalue.trim().toUpperCase()));
                }
                if (((corrattribute1 != null) && (corrattribute1.equalsIgnoreCase("Shipment Number"))) && (corrvalue1 != null && !"".equals(corrvalue1.trim()))) {
                    invSearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_TRANSPORT_INVOICE.SHIPMENT_ID", corrvalue1.trim().toUpperCase()));
                }
                if (((corrattribute != null) && (corrattribute.equalsIgnoreCase("Instance Id"))) && (corrvalue != null && !"".equals(corrvalue.trim()))) {
                    invSearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_FILES.FILE_ID", corrvalue.trim().toUpperCase()));
                }
                if (((corrattribute1 != null) && (corrattribute1.equalsIgnoreCase("Instance Id"))) && (corrvalue1 != null && !"".equals(corrvalue1.trim()))) {
                    invSearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_FILES.FILE_ID", corrvalue1.trim().toUpperCase()));
                }

                if (((corrattribute != null) && (corrattribute.equalsIgnoreCase("Direction"))) && (corrvalue != null && !"".equals(corrvalue.trim()))) {
                    invSearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_FILES.DIRECTION", corrvalue.trim().toUpperCase()));
                }
                if (((corrattribute1 != null) && (corrattribute1.equalsIgnoreCase("Direction"))) && (corrvalue1 != null && !"".equals(corrvalue1.trim()))) {
                    invSearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_FILES.DIRECTION", corrvalue1.trim().toUpperCase()));
                }

                if (doctype != null && !"".equals(doctype.trim())) {
                    invSearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_FILES.TRANSACTION_TYPE", doctype.trim()));
                }
                if (status != null && !"-1".equals(status.trim())) {
                    invSearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_FILES.STATUS", status.trim()));
                }
                if (ackStatus != null && !"-1".equals(ackStatus.trim())) {
                    invSearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_FILES.ACK_STATUS", ackStatus.trim()));
                }
                if (invSenderId != null && !"-1".equals(invSenderId.trim())) {
                    invSearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_FILES.SENDER_ID", invSenderId.trim().toUpperCase()));
                }
                if (invRecId != null && !"-1".equals(invRecId.trim())) {
                    invSearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_FILES.RECEIVER_ID", invRecId.trim().toUpperCase()));
                }
//                if (invSenderName != null && !"-1".equals(invSenderName.trim())) {
//                    invSearchQuery.append(WildCardSql.getWildCardSql1("TP1.NAME", invSenderName.trim().toUpperCase()));
//                }
//                if (invRecName != null && !"-1".equals(invRecName.trim())) {
//                    invSearchQuery.append(WildCardSql.getWildCardSql1("TP2.NAME", invRecName.trim().toUpperCase()));
//                }
                invSearchQuery.append(" order by DATE_TIME_RECEIVED DESC fetch first 50 rows only");
            } else {
                invSearchQuery.append("SELECT DISTINCT(FILES.FILE_ID) as FILE_ID, "
                        + "TRANSPORT_INVOICE.INVOICE_NUMBER as INVOICE_NUMBER,TRANSPORT_INVOICE.ID as ID,"
                        + "TRANSPORT_INVOICE.PO_NUMBER as PO_NUMBER,FILES.SENDER_ID,FILES.RECEIVER_ID,"
                        + "TRANSPORT_INVOICE.SHIPMENT_ID as SHIPMENT_ID,"
                        + "TRANSPORT_INVOICE.TOTAL_WEIGHT as TOTAL_WEIGHT,"
                        + "TRANSPORT_INVOICE.TOTAL_AMOUNT as TOTAL_AMOUNT,"
                        + "TRANSPORT_INVOICE.INVOICE_DATE as INVOICE_DATE,FILES.DATE_TIME_RECEIVED,"
                        + "FILES.STATUS as STATUS,"
                        + "FILES.ACK_STATUS as ACK_STATUS"
                        + " FROM TRANSPORT_INVOICE "
                        + "LEFT OUTER JOIN FILES ON (TRANSPORT_INVOICE.FILE_ID =FILES.FILE_ID) ");
                       
                invSearchQuery.append(" WHERE 1=1 ");
                if (datepickerFrom != null && !"".equals(datepickerFrom)) {
                    tmp_Recieved_From = DateUtility.getInstance().DateViewToDBCompare(datepickerFrom);
                    invSearchQuery.append(" AND FILES.DATE_TIME_RECEIVED >= '" + tmp_Recieved_From + "'");
                }
                if (datepickerTo != null && !"".equals(datepickerTo)) {
                    tmp_Recieved_ToTime = DateUtility.getInstance().DateViewToDBCompare(datepickerTo);
                    invSearchQuery.append(" AND FILES.DATE_TIME_RECEIVED <= '" + tmp_Recieved_ToTime + "'");
                }
                if (((corrattribute != null) && (corrattribute.equalsIgnoreCase("Invoice Number"))) && (corrvalue != null && !"".equals(corrvalue.trim()))) {
                    invSearchQuery.append(WildCardSql.getWildCardSql1("FILES.PRI_KEY_VAL", corrvalue.trim().toUpperCase()));
                }
                if (((corrattribute1 != null) && (corrattribute1.equalsIgnoreCase("Invoice Number"))) && (corrvalue1 != null && !"".equals(corrvalue1.trim()))) {
                    invSearchQuery.append(WildCardSql.getWildCardSql1("FILES.PRI_KEY_VAL", corrvalue1.trim().toUpperCase()));
                }
                if (((corrattribute != null) && (corrattribute.equalsIgnoreCase("PO Number"))) && (corrvalue != null && !"".equals(corrvalue.trim()))) {
                    invSearchQuery.append(WildCardSql.getWildCardSql1("TRANSPORT_INVOICE.PO_NUMBER", corrvalue.trim().toUpperCase()));
                }
                if ((((corrattribute1 != null) && (corrattribute1.equalsIgnoreCase("PO Number"))) && (corrvalue1 != null && !"".equals(corrvalue1.trim())))) {
                    invSearchQuery.append(WildCardSql.getWildCardSql1("TRANSPORT_INVOICE.PO_NUMBER", corrvalue1.trim().toUpperCase()));
                }
                if (((corrattribute != null) && (corrattribute.equalsIgnoreCase("Shipment Number"))) && (corrvalue != null && !"".equals(corrvalue.trim()))) {
                    invSearchQuery.append(WildCardSql.getWildCardSql1("TRANSPORT_INVOICE.SHIPMENT_ID", corrvalue.trim().toUpperCase()));
                }
                if (((corrattribute1 != null) && (corrattribute1.equalsIgnoreCase("Shipment Number"))) && (corrvalue1 != null && !"".equals(corrvalue1.trim()))) {
                    invSearchQuery.append(WildCardSql.getWildCardSql1("TRANSPORT_INVOICE.SHIPMENT_ID", corrvalue1.trim().toUpperCase()));
                }
                if (((corrattribute != null) && (corrattribute.equalsIgnoreCase("Instance Id"))) && (corrvalue != null && !"".equals(corrvalue.trim()))) {
                    invSearchQuery.append(WildCardSql.getWildCardSql1("FILES.FILE_ID", corrvalue.trim().toUpperCase()));
                }
                if (((corrattribute1 != null) && (corrattribute1.equalsIgnoreCase("Instance Id"))) && (corrvalue1 != null && !"".equals(corrvalue1.trim()))) {
                    invSearchQuery.append(WildCardSql.getWildCardSql1("FILES.FILE_ID", corrvalue1.trim().toUpperCase()));
                }

                if (((corrattribute != null) && (corrattribute.equalsIgnoreCase("Direction"))) && (corrvalue != null && !"".equals(corrvalue.trim()))) {
                    invSearchQuery.append(WildCardSql.getWildCardSql1("FILES.DIRECTION", corrvalue.trim().toUpperCase()));
                }
                if (((corrattribute1 != null) && (corrattribute1.equalsIgnoreCase("Direction"))) && (corrvalue1 != null && !"".equals(corrvalue1.trim()))) {
                    invSearchQuery.append(WildCardSql.getWildCardSql1("FILES.DIRECTION", corrvalue1.trim().toUpperCase()));
                }

                if (doctype != null && !"".equals(doctype.trim())) {
                    invSearchQuery.append(WildCardSql.getWildCardSql1("FILES.TRANSACTION_TYPE", doctype.trim()));
                }
                if (status != null && !"-1".equals(status.trim())) {
                    invSearchQuery.append(WildCardSql.getWildCardSql1("FILES.STATUS", status.trim()));
                }
                if (ackStatus != null && !"-1".equals(ackStatus.trim())) {
                    invSearchQuery.append(WildCardSql.getWildCardSql1("FILES.ACK_STATUS", ackStatus.trim()));
                }
            
                if (invSenderId != null && !"-1".equals(invSenderId.trim())) {
                    invSearchQuery.append(WildCardSql.getWildCardSql1("FILES.SENDER_ID", invSenderId.trim().toUpperCase()));
                }
                if (invRecId != null && !"-1".equals(invRecId.trim())) {
                    invSearchQuery.append(WildCardSql.getWildCardSql1("FILES.RECEIVER_ID", invRecId.trim().toUpperCase()));
                }
//                if (invSenderName != null && !"-1".equals(invSenderName.trim())) {
//                    invSearchQuery.append(WildCardSql.getWildCardSql1("TP1.NAME", invSenderName.trim().toUpperCase()));
//                }
//                if (invRecName != null && !"-1".equals(invRecName.trim())) {
//                    invSearchQuery.append(WildCardSql.getWildCardSql1("TP2.NAME", invRecName.trim().toUpperCase()));
//                }
                invSearchQuery.append(" order by DATE_TIME_RECEIVED DESC fetch first 50 rows only");
            }
            System.out.println("invSearchQuery query:"+invSearchQuery.toString());
            connection = ConnectionProvider.getInstance().getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(invSearchQuery.toString());
            invList = new ArrayList<LogisticsInvoiceBean>();
              Map<String, String> tradingPartners = DataSourceDataProvider.getInstance().getAllTradingPartners();
            System.out.println("Query and resultset start time::"+DateUtility.getInstance().getCurrentDB2Timestamp());
            while (resultSet.next()) {
                LogisticsInvoiceBean logisticsInvoiceBean = new LogisticsInvoiceBean();
                logisticsInvoiceBean.setInstanceId(resultSet.getString("FILE_ID"));
                String pname_Reciever = "";
                if (((resultSet.getString("RECEIVER_ID")) != null)
                        && (((tradingPartners.get(resultSet.getString("RECEIVER_ID")))) != null)) {
                    pname_Reciever = ((tradingPartners.get(resultSet.getString("RECEIVER_ID"))).toString());
                } else {
                    pname_Reciever = "_";
                }
                logisticsInvoiceBean.setPartner(pname_Reciever);
                
                logisticsInvoiceBean.setInvoiceNumber(resultSet.getString("INVOICE_NUMBER"));
                logisticsInvoiceBean.setPoNumber(resultSet.getString("PO_NUMBER"));
                logisticsInvoiceBean.setItemQty(resultSet.getString("TOTAL_WEIGHT"));
                logisticsInvoiceBean.setInvAmount(resultSet.getString("TOTAL_AMOUNT"));
                logisticsInvoiceBean.setShipmentId(resultSet.getString("SHIPMENT_ID"));
                if (resultSet.getString("INVOICE_DATE") != null) {
                    logisticsInvoiceBean.setInvDate(resultSet.getString("INVOICE_DATE"));
                } else {
                    logisticsInvoiceBean.setInvDate("");
                }
                logisticsInvoiceBean.setStatus(resultSet.getString("STATUS"));
                logisticsInvoiceBean.setAckStatus(resultSet.getString("ACK_STATUS"));
                logisticsInvoiceBean.setId(resultSet.getInt("ID"));
                invList.add(logisticsInvoiceBean);
            }
            System.out.println("Resultset end time::"+DateUtility.getInstance().getCurrentDB2Timestamp());
        } catch (SQLException sqlException) {
            LoggerUtility.log(logger, "SQLException occurred in buildLogInvoiceQuery method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
        } catch (Exception exception) {
            LoggerUtility.log(logger, "Exception occurred in buildLogInvoiceQuery method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
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
                LoggerUtility.log(logger, "finally SQLException occurred in buildLogInvoiceQuery method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
            }
        }
        return invList;
    }
}
