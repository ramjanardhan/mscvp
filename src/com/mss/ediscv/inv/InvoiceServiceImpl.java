/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mss.ediscv.inv;

import com.mss.ediscv.util.ConnectionProvider;
import com.mss.ediscv.util.DataSourceDataProvider;
import com.mss.ediscv.util.LoggerUtility;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.mss.ediscv.util.DateUtility;
import com.mss.ediscv.util.ServiceLocatorException;
import com.mss.ediscv.util.WildCardSql;
import java.sql.*;
import java.util.ArrayList;
import java.util.Map;

/**
 * @author miracle
 */
public class InvoiceServiceImpl implements InvoiceService {

    private static Logger logger = LogManager.getLogger(InvoiceServiceImpl.class.getName());
    String tmp_Recieved_From = "";
    String tmp_Recieved_ToTime = "";
    private ArrayList<InvoiceBean> invoiceList;
    private InvoiceBean invoiceBean;

    public ArrayList<InvoiceBean> buildinvoiceQuery(InvoiceAction invoiceAction) throws ServiceLocatorException {
        String invdatepickerTO = invoiceAction.getInvdatepicker();
        String invdatepickerfrom = invoiceAction.getInvdatepickerfrom();
        String invSenderId = "";
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            if (invoiceAction.getInvSenderId() != null && !invoiceAction.getInvSenderId().equals("-1")) {
                invSenderId = invoiceAction.getInvSenderId();
            }
            String invSenderName = "";
            if (invoiceAction.getInvSenderName() != null && !invoiceAction.getInvSenderName().equals("-1")) {
                invSenderName = invoiceAction.getInvSenderName();
            }
            String invBusId = "";
            if (invoiceAction.getInvBusId() != null && !invoiceAction.getInvBusId().equals("-1")) {
                invBusId = invoiceAction.getInvBusId();
            }
            String invRecName = "";
            if (invoiceAction.getInvRecName() != null && !invoiceAction.getInvRecName().equals("-1")) {
                invRecName = invoiceAction.getInvRecName();
            }
            String status = invoiceAction.getStatus();
            String ackStatus = invoiceAction.getAckStatus();
            String corrattribute = invoiceAction.getCorrattribute();
            String corrvalue = invoiceAction.getCorrvalue();
            String doctype = "";
            if (invoiceAction.getDocType() != null && !invoiceAction.getDocType().equals("-1")) {
                doctype = invoiceAction.getDocType();
            }
            StringBuilder invoiceSearchQuery = new StringBuilder();
            if ("ARCHIVE".equals(invoiceAction.getDatabase())) {
                invoiceSearchQuery.append("SELECT DISTINCT(ARCHIVE_INVOICE.INVOICE_NUMBER) as INVOICE_NUMBER,ARCHIVE_FILES.FILE_ID as FILEID,ARCHIVE_FILES.DIRECTION as DIRECTION,"
                        + "ARCHIVE_INVOICE.PO_NUMBER,ARCHIVE_FILES.SENDER_ID,ARCHIVE_FILES.RECEIVER_ID,ARCHIVE_INVOICE.ITEM_QTY,ARCHIVE_INVOICE.INVOICE_AMOUNT,ARCHIVE_FILES.ACK_STATUS as ACK_STATUS,ARCHIVE_FILES.STATUS as STATUS,ARCHIVE_FILES.DATE_TIME_RECEIVED as DATE_TIME_RECEIVED,ARCHIVE_FILES.REPROCESSSTATUS,"
                        + "INVOICE_DATE FROM ARCHIVE_INVOICE LEFT OUTER JOIN ARCHIVE_FILES ON "
                        + "(ARCHIVE_INVOICE.INVOICE_NUMBER=ARCHIVE_FILES.PRI_KEY_VAL  AND ARCHIVE_INVOICE.FILE_ID = ARCHIVE_FILES.FILE_ID) ");
                invoiceSearchQuery.append(" WHERE 1=1 AND FLOWFLAG like 'M' ");
                if (invdatepickerfrom != null && !"".equals(invdatepickerfrom)) {
                    tmp_Recieved_From = DateUtility.getInstance().DateViewToDBCompare(invdatepickerfrom);
                    invoiceSearchQuery.append(" AND ARCHIVE_FILES.DATE_TIME_RECEIVED >= '" + tmp_Recieved_From + "'");
                }
                if (invdatepickerTO != null && !"".equals(invdatepickerTO)) {
                    tmp_Recieved_ToTime = DateUtility.getInstance().DateViewToDBCompare(invdatepickerTO);
                    invoiceSearchQuery.append(" AND ARCHIVE_FILES.DATE_TIME_RECEIVED <= '" + tmp_Recieved_ToTime + "'");
                }

                if ((corrattribute != null && corrattribute.equalsIgnoreCase("Invoice Number")) && (corrvalue != null && !"".equals(corrvalue.trim()))) {
                    invoiceSearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_FILES.PRI_KEY_VAL", corrvalue.trim().toUpperCase()));
                }
                if (((corrattribute != null) && (corrattribute.equalsIgnoreCase("Direction"))) && (corrvalue != null && !"".equals(corrvalue.trim()))) {
                    invoiceSearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_FILES.DIRECTION", corrvalue.trim().toUpperCase()));
                }
                if (((corrattribute != null) && (corrattribute.equalsIgnoreCase("Instance Id"))) && (corrvalue != null && !"".equals(corrvalue.trim()))) {
                    invoiceSearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_FILES.FILE_ID", corrvalue.trim().toUpperCase()));
                }

                if (doctype != null && !"".equals(doctype.trim())) {
                    invoiceSearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_FILES.TRANSACTION_TYPE", doctype.trim()));
                }
                if (status != null && !"-1".equals(status.trim())) {
                    invoiceSearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_FILES.STATUS", status.trim()));
                }
                if (ackStatus != null && !"-1".equals(ackStatus.trim())) {
                    invoiceSearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_FILES.ACK_STATUS", ackStatus.trim()));
                }
                if (invSenderId != null && !"".equals(invSenderId.trim())) {
                    invoiceSearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_FILES.SENDER_ID", invSenderId.trim().toUpperCase()));
                }
                if (invBusId != null && !"".equals(invBusId.trim())) {
                    invoiceSearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_FILES.RECEIVER_ID", invBusId.trim().toUpperCase()));
                }
//                if (invSenderName != null && !"".equals(invSenderName.trim())) {
//                    invoiceSearchQuery.append(WildCardSql.getWildCardSql1("TP1.NAME", invSenderName.trim().toUpperCase()));
//                }
//                if (invRecName != null && !"".equals(invRecName.trim())) {
//                    invoiceSearchQuery.append(WildCardSql.getWildCardSql1("TP2.NAME", invRecName.trim().toUpperCase()));
//                }
                invoiceSearchQuery.append(" order by DATE_TIME_RECEIVED DESC fetch first 50 rows only");
            } else {
                invoiceSearchQuery.append("SELECT DISTINCT(FILES.FILE_ID) as FILEID,INVOICE.INVOICE_NUMBER as INVOICE_NUMBER,FILES.DIRECTION as DIRECTION,"
                        + "INVOICE.PO_NUMBER,FILES.SENDER_ID,FILES.RECEIVER_ID,INVOICE.ITEM_QTY,INVOICE.INVOICE_AMOUNT,FILES.ACK_STATUS as ACK_STATUS,FILES.STATUS as STATUS,FILES.DATE_TIME_RECEIVED as DATE_TIME_RECEIVED,FILES.REPROCESSSTATUS,"
                        + "INVOICE_DATE FROM INVOICE LEFT OUTER JOIN FILES ON "
                        + "(INVOICE.INVOICE_NUMBER=FILES.PRI_KEY_VAL  AND INVOICE.FILE_ID = FILES.FILE_ID)  ");
                invoiceSearchQuery.append(" WHERE 1=1 AND FLOWFLAG like 'M' ");
                if (invdatepickerfrom != null && !"".equals(invdatepickerfrom)) {
                    tmp_Recieved_From = DateUtility.getInstance().DateViewToDBCompare(invdatepickerfrom);
                    invoiceSearchQuery.append(" AND FILES.DATE_TIME_RECEIVED >= '" + tmp_Recieved_From + "'");
                }
                if (invdatepickerTO != null && !"".equals(invdatepickerTO)) {
                    tmp_Recieved_ToTime = DateUtility.getInstance().DateViewToDBCompare(invdatepickerTO);
                    invoiceSearchQuery.append(" AND FILES.DATE_TIME_RECEIVED <= '" + tmp_Recieved_ToTime + "'");
                }

                if ((corrattribute != null && corrattribute.equalsIgnoreCase("Invoice Number")) && (corrvalue != null && !"".equals(corrvalue.trim()))) {
                    invoiceSearchQuery.append(WildCardSql.getWildCardSql1("INVOICE.INVOICE_NUMBER", corrvalue.trim().toUpperCase()));
                }
                if (((corrattribute != null) && (corrattribute.equalsIgnoreCase("Direction"))) && (corrvalue != null && !"".equals(corrvalue.trim()))) {
                    invoiceSearchQuery.append(WildCardSql.getWildCardSql1("FILES.DIRECTION", corrvalue.trim().toUpperCase()));
                }
                if (((corrattribute != null) && (corrattribute.equalsIgnoreCase("Instance Id"))) && (corrvalue != null && !"".equals(corrvalue.trim()))) {
                    invoiceSearchQuery.append(WildCardSql.getWildCardSql1("FILES.FILE_ID", corrvalue.trim().toUpperCase()));
                }

                if (doctype != null && !"".equals(doctype.trim())) {
                    invoiceSearchQuery.append(WildCardSql.getWildCardSql1("FILES.TRANSACTION_TYPE", doctype.trim()));
                }
                if (status != null && !"-1".equals(status.trim())) {
                    invoiceSearchQuery.append(WildCardSql.getWildCardSql1("FILES.STATUS", status.trim()));
                }
                if (ackStatus != null && !"-1".equals(ackStatus.trim())) {
                    invoiceSearchQuery.append(WildCardSql.getWildCardSql1("FILES.ACK_STATUS", ackStatus.trim()));
                }
                if (invSenderId != null && !"".equals(invSenderId.trim())) {
                    invoiceSearchQuery.append(WildCardSql.getWildCardSql1("FILES.SENDER_ID", invSenderId.trim().toUpperCase()));
                }
                if (invBusId != null && !"".equals(invBusId.trim())) {
                    invoiceSearchQuery.append(WildCardSql.getWildCardSql1("FILES.RECEIVER_ID", invBusId.trim().toUpperCase()));
                }
//                if (invSenderName != null && !"".equals(invSenderName.trim())) {
//                    invoiceSearchQuery.append(WildCardSql.getWildCardSql1("TP1.NAME", invSenderName.trim().toUpperCase()));
//                }
//                if (invRecName != null && !"".equals(invRecName.trim())) {
//                    invoiceSearchQuery.append(WildCardSql.getWildCardSql1("TP2.NAME", invRecName.trim().toUpperCase()));
//                }
                invoiceSearchQuery.append(" order by DATE_TIME_RECEIVED DESC fetch first 50 rows only");
            }
            System.out.println("invoiceSearchQuery query:"+invoiceSearchQuery.toString());
             Map<String, String> tradingPartners = DataSourceDataProvider.getInstance().getAllTradingPartners();
            connection = ConnectionProvider.getInstance().getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(invoiceSearchQuery.toString());
            invoiceList = new ArrayList<InvoiceBean>();
            System.out.println("Query and resultset start time::"+DateUtility.getInstance().getCurrentDB2Timestamp());
            while (resultSet.next()) {
                InvoiceBean invoiceBean = new InvoiceBean();
                invoiceBean.setPoNumber(resultSet.getString("PO_NUMBER"));
                invoiceBean.setFileId(resultSet.getString("FILEID"));
                String direction = resultSet.getString("DIRECTION");
                invoiceBean.setDirection(direction);
                invoiceBean.setItemQty(resultSet.getString("ITEM_QTY"));
                invoiceBean.setInvNumber(resultSet.getString("INVOICE_NUMBER"));
                invoiceBean.setInvAmount(resultSet.getString("INVOICE_AMOUNT"));
                invoiceBean.setInvDate(resultSet.getDate("INVOICE_DATE"));
               if ((direction != null) && ("INBOUND".equalsIgnoreCase(direction))) {
                    String pname_Sender = "";
                    if (((resultSet.getString("SENDER_ID")) != null)
                            && (((tradingPartners.get(resultSet.getString("SENDER_ID")))) != null)) {
                        pname_Sender = (tradingPartners.get(resultSet.getString("SENDER_ID"))).toString();
                    } else {
                        pname_Sender = "_";
                    }
                    invoiceBean.setPname(pname_Sender);
                } else {
                    String pname_Reciever = "";
                    if (((resultSet.getString("RECEIVER_ID")) != null)
                            && (((tradingPartners.get(resultSet.getString("RECEIVER_ID")))) != null)) {
                        pname_Reciever = ((tradingPartners.get(resultSet.getString("RECEIVER_ID"))).toString());
                    } else {
                        pname_Reciever = "_";
                    }
                    invoiceBean.setPname(pname_Reciever);
                }
                invoiceBean.setAckStatus(resultSet.getString("ACK_STATUS"));
                invoiceBean.setStatus(resultSet.getString("STATUS"));
                invoiceBean.setDate_time_rec(resultSet.getTimestamp("DATE_TIME_RECEIVED"));
                invoiceBean.setReProcessStatus(resultSet.getString("REPROCESSSTATUS"));
                invoiceList.add(invoiceBean);
            }
            System.out.println("Resultset end time::"+DateUtility.getInstance().getCurrentDB2Timestamp());
        } catch (SQLException sqlException) {
            LoggerUtility.log(logger, "SQLException occurred in buildinvoiceQuery method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
        } catch (Exception exception) {
            LoggerUtility.log(logger, "Exception occurred in buildinvoiceQuery method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
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
                LoggerUtility.log(logger, "SQLException occurred in buildinvoiceQuery method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
            }
        }
        return invoiceList;
    }

    public InvoiceBean getInvoiceBean() {
        return invoiceBean;
    }

    public void setInvoiceBean(InvoiceBean invoiceBean) {
        this.invoiceBean = invoiceBean;
    }
}
