/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mss.ediscv.reports;

import com.mss.ediscv.util.ConnectionProvider;
import com.mss.ediscv.util.DataSourceDataProvider;
import com.mss.ediscv.util.DateUtility;
import com.mss.ediscv.util.ServiceLocatorException;
import com.mss.ediscv.util.WildCardSql;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import com.mss.ediscv.util.LoggerUtility;
import java.util.Map;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author miracle
 */
public class ReportsServiceImpl implements ReportsService {

    String tmp_Recieved_From = "";
    String tmp_Recieved_ToTime = "";
    private static Logger logger = LogManager.getLogger(ReportsServiceImpl.class.getName());
    String responseString = null;
    private ArrayList<ReportsBean> documentList;

    public ArrayList<ReportsBean> getDocumentList(ReportsAction reportsAction) throws ServiceLocatorException {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            String docdatepicker = reportsAction.getDocdatepicker();
            String docdatepickerfrom = reportsAction.getDocdatepickerfrom();
            String docSenderId = "";
            if (reportsAction.getDocSenderId() != null && !reportsAction.getDocSenderId().equals("-1")) {
                docSenderId = reportsAction.getDocSenderId();
            }
            String docSenderName = "";
            if (reportsAction.getDocSenderName() != null && !reportsAction.getDocSenderName().equals("-1")) {
                docSenderName = reportsAction.getDocSenderName();
            }
            String docBusId = "";
            if (reportsAction.getDocReceiverId() != null && !reportsAction.getDocReceiverId().equals("-1")) {
                docBusId = reportsAction.getDocReceiverId();
            }
            String docRecName = "";
            if (reportsAction.getDocReceiverName() != null && !reportsAction.getDocReceiverName().equals("-1")) {
                docRecName = reportsAction.getDocReceiverName();
            }
            String doctype = "";
            if (reportsAction.getDocType() != null && !reportsAction.getDocType().equals("-1")) {
                doctype = reportsAction.getDocType();
            }
            String status = reportsAction.getStatus();
            String ackStatus = reportsAction.getAckStatus();
            StringBuilder documentSearchQuery = new StringBuilder();
            if ("ARCHIVE".equals(reportsAction.getDatabase())) {
                documentSearchQuery.append("SELECT DISTINCT(ARCHIVE_FILES.FILE_ID) as FILE_ID,ARCHIVE_FILES.ISA_NUMBER as ISA_NUMBER,ARCHIVE_FILES.SENDER_ID,ARCHIVE_FILES.RECEIVER_ID,"
                        + "ARCHIVE_FILES.FILE_TYPE as FILE_TYPE,ARCHIVE_FILES.FILE_ORIGIN as FILE_ORIGIN,"
                        + "ARCHIVE_FILES.TRANSACTION_TYPE as TRANSACTION_TYPE,ARCHIVE_FILES.DIRECTION as DIRECTION,"
                        + "ARCHIVE_FILES.DATE_TIME_RECEIVED as DATE_TIME_RECEIVED,ARCHIVE_FILES.STATUS as STATUS,ARCHIVE_FILES.ACK_STATUS as ACK_STATUS,"
                        + "ARCHIVE_FILES.SEC_KEY_VAL,ARCHIVE_FILES.REPROCESSSTATUS,ARCHIVE_FILES.ERR_MESSAGE as ERR_MESSAGE FROM ARCHIVE_FILES "
                        + "LEFT OUTER JOIN ARCHIVE_ASN ON (ARCHIVE_ASN.FILE_ID = ARCHIVE_FILES.FILE_ID)");
                documentSearchQuery.append(" WHERE 1=1 AND FLOWFLAG like 'M' ");
                if (doctype != null && !"".equals(doctype.trim())) {
                    documentSearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_FILES.TRANSACTION_TYPE", doctype.trim()));
                }
                if (status != null && !"-1".equals(status.trim())) {
                    documentSearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_FILES.STATUS", status.trim()));
                }
                if (ackStatus != null && !"-1".equals(ackStatus.trim())) {
                    documentSearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_FILES.ACK_STATUS", ackStatus.trim()));
                }
                if (docBusId != null && !"".equals(docBusId.trim())) {
                    documentSearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_FILES.RECEIVER_ID", docBusId.trim().toUpperCase()));
                }
                if (docSenderId != null && !"".equals(docSenderId.trim())) {
                    documentSearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_FILES.SENDER_ID", docSenderId.trim().toUpperCase()));
                }
//                if (docSenderName != null && !"".equals(docSenderName.trim())) {
//                    documentSearchQuery.append(WildCardSql.getWildCardSql1("TP1.NAME", docSenderName.trim().toUpperCase()));
//                }
//                if (docRecName != null && !"".equals(docRecName.trim())) {
//                    documentSearchQuery.append(WildCardSql.getWildCardSql1("TP2.NAME", docRecName.trim().toUpperCase()));
//                }
                if (docdatepicker != null && !"".equals(docdatepicker)) {
                    tmp_Recieved_From = DateUtility.getInstance().DateViewToDBCompare(docdatepicker);
                    documentSearchQuery.append(" AND ARCHIVE_FILES.DATE_TIME_RECEIVED <= '" + tmp_Recieved_From + "'");
                }
                if (docdatepickerfrom != null && !"".equals(docdatepickerfrom)) {
                    tmp_Recieved_From = DateUtility.getInstance().DateViewToDBCompare(docdatepickerfrom);
                    documentSearchQuery.append(" AND ARCHIVE_FILES.DATE_TIME_RECEIVED >= '" + tmp_Recieved_From + "'");
                }
                documentSearchQuery.append(" order by DATE_TIME_RECEIVED DESC fetch first 50 rows only");
            } else {
                documentSearchQuery.append("SELECT DISTINCT(FILES.FILE_ID) as FILE_ID,FILES.ISA_NUMBER as ISA_NUMBER,"
                        + "FILES.FILE_TYPE as FILE_TYPE,FILES.FILE_ORIGIN as FILE_ORIGIN,FILES.SENDER_ID,FILES.RECEIVER_ID,"
                        + "FILES.TRANSACTION_TYPE as TRANSACTION_TYPE,FILES.DIRECTION as DIRECTION,"
                        + "FILES.DATE_TIME_RECEIVED as DATE_TIME_RECEIVED,FILES.STATUS as STATUS,FILES.ACK_STATUS as ACK_STATUS,"
                        + "FILES.SEC_KEY_VAL,FILES.REPROCESSSTATUS,FILES.ERR_MESSAGE as ERR_MESSAGE FROM FILES "
                        + "LEFT OUTER JOIN ASN ON (ASN.FILE_ID = FILES.FILE_ID)");
                documentSearchQuery.append(" WHERE 1=1 AND FLOWFLAG like 'M' ");
                if (doctype != null && !"".equals(doctype.trim())) {
                    documentSearchQuery.append(WildCardSql.getWildCardSql1("FILES.TRANSACTION_TYPE", doctype.trim()));
                }
                if (status != null && !"-1".equals(status.trim())) {
                    documentSearchQuery.append(WildCardSql.getWildCardSql1("FILES.STATUS", status.trim()));
                }
                if (ackStatus != null && !"-1".equals(ackStatus.trim())) {
                    documentSearchQuery.append(WildCardSql.getWildCardSql1("FILES.ACK_STATUS", ackStatus.trim()));
                }
                
                if (docBusId != null && !"".equals(docBusId.trim())) {
                    documentSearchQuery.append(WildCardSql.getWildCardSql1("FILES.RECEIVER_ID", docBusId.trim().toUpperCase()));
                }
                if (docSenderId != null && !"".equals(docSenderId.trim())) {
                    documentSearchQuery.append(WildCardSql.getWildCardSql1("FILES.SENDER_ID", docSenderId.trim().toUpperCase()));
                }
//                if (docSenderName != null && !"".equals(docSenderName.trim())) {
//                    documentSearchQuery.append(WildCardSql.getWildCardSql1("TP1.NAME", docSenderName.trim().toUpperCase()));
//                }
//                if (docRecName != null && !"".equals(docRecName.trim())) {
//                    documentSearchQuery.append(WildCardSql.getWildCardSql1("TP2.NAME", docRecName.trim().toUpperCase()));
//                }
                if (docdatepicker != null && !"".equals(docdatepicker)) {
                    System.out.println("date is"+docdatepicker);
                    tmp_Recieved_From = DateUtility.getInstance().DateViewToDBCompare(docdatepicker);
                    documentSearchQuery.append(" AND FILES.DATE_TIME_RECEIVED <= '" + tmp_Recieved_From + "'");
                }
                if (docdatepickerfrom != null && !"".equals(docdatepickerfrom)) {
                    System.out.println("date is"+docdatepickerfrom);
                    tmp_Recieved_From = DateUtility.getInstance().DateViewToDBCompare(docdatepickerfrom);
                    documentSearchQuery.append(" AND FILES.DATE_TIME_RECEIVED >= '" + tmp_Recieved_From + "'");
                }
                documentSearchQuery.append(" order by DATE_TIME_RECEIVED DESC fetch first 50 rows only");
            }
            System.out.println("documentSearchQuery query:"+documentSearchQuery.toString());
            
           Map<String, String> tradingPartners = DataSourceDataProvider.getInstance().getAllTradingPartners();
            connection = ConnectionProvider.getInstance().getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(documentSearchQuery.toString());
            documentList = new ArrayList<ReportsBean>();
            System.out.println("Query and resultset start time::"+DateUtility.getInstance().getCurrentDB2Timestamp());
            while (resultSet.next()) {
                ReportsBean reportsBean = new ReportsBean();
                reportsBean.setFile_id(resultSet.getString("FILE_ID"));
                reportsBean.setFile_origin(resultSet.getString("FILE_ORIGIN"));
                reportsBean.setFile_type(resultSet.getString("FILE_TYPE"));
                reportsBean.setIsa_number(resultSet.getString("ISA_NUMBER"));
                reportsBean.setTransaction_type(resultSet.getString("TRANSACTION_TYPE"));
                String direction = resultSet.getString("DIRECTION");
                reportsBean.setDirection(direction);
                reportsBean.setDate_time_rec(resultSet.getTimestamp("DATE_TIME_RECEIVED"));
                reportsBean.setStatus(resultSet.getString("STATUS"));
                if ((direction != null) && ("INBOUND".equalsIgnoreCase(direction))) {
                    String pname_Sender = "";
                    if (((resultSet.getString("SENDER_ID")) != null)
                            && (((tradingPartners.get(resultSet.getString("SENDER_ID")))) != null)) {
                        pname_Sender = (tradingPartners.get(resultSet.getString("SENDER_ID"))).toString();
                    } else {
                        pname_Sender = "_";
                    }
                    reportsBean.setPname(pname_Sender);
                } else {
                    String pname_Reciever = "";
                    if (((resultSet.getString("RECEIVER_ID")) != null)
                            && (((tradingPartners.get(resultSet.getString("RECEIVER_ID")))) != null)) {
                        pname_Reciever = ((tradingPartners.get(resultSet.getString("RECEIVER_ID"))).toString());
                    } else {
                        pname_Reciever = "_";
                    }
                    reportsBean.setPname(pname_Reciever);
                }
                reportsBean.setPoNumber(resultSet.getString("SEC_KEY_VAL"));
                reportsBean.setReProcessStatus(resultSet.getString("REPROCESSSTATUS"));
                reportsBean.setAckStatus(resultSet.getString("ACK_STATUS"));
                reportsBean.setErrorMessage(resultSet.getString("ERR_MESSAGE"));
                documentList.add(reportsBean);
            }
            System.out.println("Resultset end time::"+DateUtility.getInstance().getCurrentDB2Timestamp());
        } catch (SQLException sqlException) {
            LoggerUtility.log(logger, "SQLException occurred in getDocumentList method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
        } catch (Exception exception) {
            LoggerUtility.log(logger, "Exception occurred in getDocumentList method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
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
            LoggerUtility.log(logger, "finally SQLException occurred in getDocumentList method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
        }
        }
        return documentList;
    }
}
