/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mss.ediscv.payments;

import com.mss.ediscv.util.ConnectionProvider;
import com.mss.ediscv.util.DataSourceDataProvider;
import java.sql.Connection;
import java.sql.ResultSet;
import com.mss.ediscv.util.LoggerUtility;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.mss.ediscv.util.DateUtility;
import com.mss.ediscv.util.ServiceLocatorException;
import com.mss.ediscv.util.WildCardSql;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Map;

/**
 * @author miracle
 */
public class PaymentsServiceImpl implements PaymentsService {

    private static Logger logger = LogManager.getLogger(PaymentsServiceImpl.class.getName());
    String tmp_Recieved_From = "";
    String tmp_Recieved_ToTime = "";
    private ArrayList<PaymentBean> paymentList;
    private PaymentBean paymentBean;

    public ArrayList<PaymentBean> buildpaymentSQuery(PaymentsAction paymentsAction) throws ServiceLocatorException {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            String paDateTo = paymentsAction.getPaDateTo();
            String paDateFrom = paymentsAction.getPaDateFrom();
            String paSenderId = "";
            if (paymentsAction.getPaSenderId() != null && !paymentsAction.getPaSenderId().equals("-1")) {
                paSenderId = paymentsAction.getPaSenderId();
            }
            String paSenderName = "";
            if (paymentsAction.getPaSenderName() != null && !paymentsAction.getPaSenderName().equals("-1")) {
                paSenderName = paymentsAction.getPaSenderName();
            }
            String paRecId = "";
            if (paymentsAction.getPaRecId() != null && !paymentsAction.getPaRecId().equals("-1")) {
                paRecId = paymentsAction.getPaRecId();
            }
            String paRecName = "";
            if (paymentsAction.getPaRecName() != null && !paymentsAction.getPaRecName().equals("-1")) {
                paRecName = paymentsAction.getPaRecName();
            }
            String status = paymentsAction.getStatus();
            String ackStatus = paymentsAction.getAckStatus();
            String corrattribute = paymentsAction.getCorrattribute();
            String corrvalue = paymentsAction.getCorrvalue();
            String corrattribute1 = paymentsAction.getCorrattribute1();
            String corrvalue1 = paymentsAction.getCorrvalue1();
            String doctype = "";
            if (paymentsAction.getDocType() != null && !paymentsAction.getDocType().equals("-1")) {
                doctype = paymentsAction.getDocType();
            }
            StringBuilder paymentSearchQuery = new StringBuilder();
            if ("ARCHIVE".equals(paymentsAction.getDatabase())) {
                paymentSearchQuery.append("SELECT DISTINCT(ARCHIVE_PAYMENT.FILE_ID) as FILE_ID,ARCHIVE_FILES.TRANSACTION_TYPE, "
                        + "ARCHIVE_PAYMENT.DATE as Date,ARCHIVE_FILES.SENDER_ID,ARCHIVE_FILES.RECEIVER_ID, ARCHIVE_PAYMENT.Check_Amount as Check_Amount,ARCHIVE_FILES.DATE_TIME_RECEIVED as DATE_TIME_RECEIVED, "
                        + "ARCHIVE_PAYMENT.Check_Number as Check_Number,ARCHIVE_PAYMENT.INVOICE_NUMBER as INVOICE_NUMBER,ARCHIVE_PAYMENT.PO_NUMBER as PO_NUMBER,"
                        + "ARCHIVE_FILES.ACK_STATUS as ACK_STATUS,ARCHIVE_FILES.STATUS as STATUS,ARCHIVE_FILES.REPROCESSSTATUS FROM ARCHIVE_PAYMENT ");
                paymentSearchQuery.append("LEFT OUTER JOIN ARCHIVE_FILES ON (ARCHIVE_PAYMENT.FILE_ID=ARCHIVE_FILES.FILE_ID) ");

                paymentSearchQuery.append("WHERE 1=1 AND FLOWFLAG like 'M' ");
                if (paDateFrom != null && !"".equals(paDateFrom)) {
                    tmp_Recieved_From = DateUtility.getInstance().DateViewToDBCompare(paDateFrom);
                    paymentSearchQuery.append(" AND ARCHIVE_FILES.DATE_TIME_RECEIVED >= '" + tmp_Recieved_From + "'");
                }
                if (paDateTo != null && !"".equals(paDateTo)) {
                    tmp_Recieved_ToTime = DateUtility.getInstance().DateViewToDBCompare(paDateTo);
                    paymentSearchQuery.append(" AND ARCHIVE_FILES.DATE_TIME_RECEIVED <= '" + tmp_Recieved_ToTime + "'");
                }
                if (((corrattribute != null) && (corrattribute.equalsIgnoreCase("Cheque Number"))) && (corrvalue != null && !"".equals(corrvalue.trim()))) {
                    paymentSearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_FILES.PRI_KEY_VAL", corrvalue.trim().toUpperCase()));
                }
                if (((corrattribute != null) && (corrattribute.equalsIgnoreCase("Cheque Amount"))) && (corrvalue != null && !"".equals(corrvalue.trim()))) {
                    paymentSearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_PAYMENT.Check_Amount", corrvalue.trim().toUpperCase()));
                }
                if (((corrattribute1 != null) && (corrattribute1.equalsIgnoreCase("Cheque Number"))) && (corrvalue1 != null && !"".equals(corrvalue1.trim()))) {
                    paymentSearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_FILES.PRI_KEY_VAL", corrvalue1.trim().toUpperCase()));
                }
                if (((corrattribute1 != null) && (corrattribute1.equalsIgnoreCase("Cheque Amount"))) && (corrvalue1 != null && !"".equals(corrvalue1.trim()))) {
                    paymentSearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_PAYMENT.Check_Amount", corrvalue1.trim().toUpperCase()));
                }
                if (((corrattribute != null) && (corrattribute.equalsIgnoreCase("Instance Id"))) && (corrvalue != null && !"".equals(corrvalue.trim()))) {
                    paymentSearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_FILES.FILE_ID", corrvalue.trim().toUpperCase()));
                }
                if (((corrattribute1 != null) && (corrattribute1.equalsIgnoreCase("Instance Id"))) && (corrvalue1 != null && !"".equals(corrvalue1.trim()))) {
                    paymentSearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_FILES.FILE_ID", corrvalue1.trim().toUpperCase()));
                }
                if (((corrattribute != null) && (corrattribute.equalsIgnoreCase("Direction"))) && (corrvalue != null && !"".equals(corrvalue.trim()))) {
                    paymentSearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_FILES.DIRECTION", corrvalue.trim().toUpperCase()));
                }
                if (((corrattribute1 != null) && (corrattribute1.equalsIgnoreCase("Direction"))) && (corrvalue1 != null && !"".equals(corrvalue1.trim()))) {
                    paymentSearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_FILES.DIRECTION", corrvalue1.trim().toUpperCase()));
                }
                if (doctype != null && !"".equals(doctype.trim())) {
                    paymentSearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_FILES.TRANSACTION_TYPE", doctype.trim()));
                }
                if (status != null && !"-1".equals(status.trim())) {
                    paymentSearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_FILES.STATUS", status.trim()));
                }
                if (ackStatus != null && !"-1".equals(ackStatus.trim())) {
                    paymentSearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_FILES.ACK_STATUS", ackStatus.trim()));
                }
                if (paSenderId != null && !"".equals(paSenderId.trim())) {
                    paymentSearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_FILES.SENDER_ID", paSenderId.trim().toUpperCase()));
                }
                if (paRecId != null && !"".equals(paRecId.trim())) {
                    paymentSearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_FILES.RECEIVER_ID", paRecId.trim().toUpperCase()));
                }

//                if (paSenderName != null && !"".equals(paSenderName.trim())) {
//                    paymentSearchQuery.append(WildCardSql.getWildCardSql1("TP1.NAME", paSenderName.trim().toUpperCase()));
//                }
//               
//                if (paRecName != null && !"".equals(paRecName.trim())) {
//                    paymentSearchQuery.append(WildCardSql.getWildCardSql1("TP2.NAME", paRecName.trim().toUpperCase()));
//                }
                paymentSearchQuery.append("order by DATE_TIME_RECEIVED DESC fetch first 50 rows only");
            } else {
                paymentSearchQuery.append("SELECT DISTINCT(PAYMENT.FILE_ID) as FILE_ID,FILES.TRANSACTION_TYPE, "
                        + "PAYMENT.DATE as Date, PAYMENT.Check_Amount as Check_Amount,FILES.SENDER_ID,FILES.RECEIVER_ID,FILES.DATE_TIME_RECEIVED as DATE_TIME_RECEIVED, "
                        + "PAYMENT.Check_Number as Check_Number,PAYMENT.INVOICE_NUMBER as INVOICE_NUMBER,PAYMENT.PO_NUMBER as PO_NUMBER,"
                        + "FILES.ACK_STATUS as ACK_STATUS,FILES.STATUS as STATUS,FILES.REPROCESSSTATUS FROM PAYMENT ");
                paymentSearchQuery.append("LEFT OUTER JOIN FILES ON (PAYMENT.FILE_ID=FILES.FILE_ID) ");
                paymentSearchQuery.append("WHERE 1=1 AND FLOWFLAG like 'M' ");
                if (paDateFrom != null && !"".equals(paDateFrom)) {
                    tmp_Recieved_From = DateUtility.getInstance().DateViewToDBCompare(paDateFrom);
                    paymentSearchQuery.append(" AND FILES.DATE_TIME_RECEIVED >= '" + tmp_Recieved_From + "'");
                }
                if (paDateTo != null && !"".equals(paDateTo)) {
                    tmp_Recieved_ToTime = DateUtility.getInstance().DateViewToDBCompare(paDateTo);
                    paymentSearchQuery.append(" AND FILES.DATE_TIME_RECEIVED <= '" + tmp_Recieved_ToTime + "'");
                }
                if (((corrattribute != null) && (corrattribute.equalsIgnoreCase("Cheque Number"))) && (corrvalue != null && !"".equals(corrvalue.trim()))) {
                    paymentSearchQuery.append(WildCardSql.getWildCardSql1("FILES.PRI_KEY_VAL", corrvalue.trim().toUpperCase()));
                }
                if (((corrattribute != null) && (corrattribute.equalsIgnoreCase("Cheque Amount"))) && (corrvalue != null && !"".equals(corrvalue.trim()))) {
                    paymentSearchQuery.append(WildCardSql.getWildCardSql1("PAYMENT.Check_Amount", corrvalue.trim().toUpperCase()));
                }
                if (((corrattribute1 != null) && (corrattribute1.equalsIgnoreCase("Cheque Number"))) && (corrvalue1 != null && !"".equals(corrvalue1.trim()))) {
                    paymentSearchQuery.append(WildCardSql.getWildCardSql1("FILES.PRI_KEY_VAL", corrvalue1.trim().toUpperCase()));
                }
                if (((corrattribute1 != null) && (corrattribute1.equalsIgnoreCase("Cheque Amount"))) && (corrvalue1 != null && !"".equals(corrvalue1.trim()))) {
                    paymentSearchQuery.append(WildCardSql.getWildCardSql1("PAYMENT.Check_Amount", corrvalue1.trim().toUpperCase()));
                }
                if (((corrattribute != null) && (corrattribute.equalsIgnoreCase("Instance Id"))) && (corrvalue != null && !"".equals(corrvalue.trim()))) {
                    paymentSearchQuery.append(WildCardSql.getWildCardSql1("FILES.FILE_ID", corrvalue.trim().toUpperCase()));
                }
                if (((corrattribute1 != null) && (corrattribute1.equalsIgnoreCase("Instance Id"))) && (corrvalue1 != null && !"".equals(corrvalue1.trim()))) {
                    paymentSearchQuery.append(WildCardSql.getWildCardSql1("FILES.FILE_ID", corrvalue1.trim().toUpperCase()));
                }
                if (((corrattribute != null) && (corrattribute.equalsIgnoreCase("Direction"))) && (corrvalue != null && !"".equals(corrvalue.trim()))) {
                    paymentSearchQuery.append(WildCardSql.getWildCardSql1("FILES.DIRECTION", corrvalue.trim().toUpperCase()));
                }
                if (((corrattribute1 != null) && (corrattribute1.equalsIgnoreCase("Direction"))) && (corrvalue1 != null && !"".equals(corrvalue1.trim()))) {
                    paymentSearchQuery.append(WildCardSql.getWildCardSql1("FILES.DIRECTION", corrvalue1.trim().toUpperCase()));
                }
                if (doctype != null && !"".equals(doctype.trim())) {
                    paymentSearchQuery.append(WildCardSql.getWildCardSql1("FILES.TRANSACTION_TYPE", doctype.trim()));
                }
                if (status != null && !"-1".equals(status.trim())) {
                    paymentSearchQuery.append(WildCardSql.getWildCardSql1("FILES.STATUS", status.trim()));
                }
                if (ackStatus != null && !"-1".equals(ackStatus.trim())) {
                    paymentSearchQuery.append(WildCardSql.getWildCardSql1("FILES.ACK_STATUS", ackStatus.trim()));
                }

                if (paSenderId != null && !"".equals(paSenderId.trim())) {
                    paymentSearchQuery.append(WildCardSql.getWildCardSql1("FILES.SENDER_ID", paSenderId.trim().toUpperCase()));
                }

                if (paRecId != null && !"".equals(paRecId.trim())) {
                    paymentSearchQuery.append(WildCardSql.getWildCardSql1("FILES.RECEIVER_ID", paRecId.trim().toUpperCase()));
                }

//                if (paSenderName != null && !"".equals(paSenderName.trim())) {
//                    paymentSearchQuery.append(WildCardSql.getWildCardSql1("TP1.NAME", paSenderName.trim().toUpperCase()));
//                }
//                if (paRecName != null && !"".equals(paRecName.trim())) {
//                    paymentSearchQuery.append(WildCardSql.getWildCardSql1("TP2.NAME", paRecName.trim().toUpperCase()));
//                }
                paymentSearchQuery.append("order by DATE_TIME_RECEIVED DESC fetch first 50 rows only");
            }
            System.out.println("paymentSearchQuery query:" + paymentSearchQuery.toString());
            Map<String, String> tradingPartners = DataSourceDataProvider.getInstance().getAllTradingPartners();
            connection = ConnectionProvider.getInstance().getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(paymentSearchQuery.toString());
            paymentList = new ArrayList<PaymentBean>();
            System.out.println("Query and resultset start time::" + DateUtility.getInstance().getCurrentDB2Timestamp());
            while (resultSet.next()) {
                PaymentBean paymentBean = new PaymentBean();
                paymentBean.setSenderId(resultSet.getString("Sender_ID"));
                paymentBean.setSenderName(resultSet.getString("Sender_Name"));
                paymentBean.setDate(resultSet.getString("Date"));
                paymentBean.setCheckAmount(resultSet.getString("Check_Amount"));
                paymentBean.setCheckNumber(resultSet.getString("Check_Number"));
                paymentBean.setFileId(resultSet.getString("FILE_ID"));
                paymentBean.setReceiverName(resultSet.getString("ReceiverName"));

                String pname_Reciever = "";
                if (((resultSet.getString("RECEIVER_ID")) != null)
                        && (((tradingPartners.get(resultSet.getString("RECEIVER_ID")))) != null)) {
                    pname_Reciever = ((tradingPartners.get(resultSet.getString("RECEIVER_ID"))).toString());
                } else {
                    pname_Reciever = "_";
                }
                paymentBean.setReceiverName(pname_Reciever);

                paymentBean.setInvNumber(resultSet.getString("INVOICE_NUMBER"));
                paymentBean.setPonumber(resultSet.getString("PO_NUMBER"));
                paymentBean.setAckStatus(resultSet.getString("ACK_STATUS"));
                paymentBean.setStatus(resultSet.getString("STATUS"));
                paymentBean.setTransType(resultSet.getString("TRANSACTION_TYPE"));
                paymentBean.setDate_time_rec(resultSet.getTimestamp("DATE_TIME_RECEIVED"));
                paymentBean.setReProcessStatus(resultSet.getString("REPROCESSSTATUS"));
                paymentList.add(paymentBean);
            }
            System.out.println("Resultset end time::" + DateUtility.getInstance().getCurrentDB2Timestamp());
        } catch (SQLException sqlException) {
            LoggerUtility.log(logger, "SQLException occurred in buildpaymentSQuery method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
        } catch (Exception exception) {
            LoggerUtility.log(logger, "Exception occurred in buildpaymentSQuery method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
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
                LoggerUtility.log(logger, "SQLException occurred in buildpaymentSQuery method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
            }
        }
        return paymentList;
    }

}
