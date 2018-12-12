/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mss.ediscv.logisticreports;

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
public class LogisticReportsServiceImpl implements LogisticReportsService {

    private static Logger logger = LogManager.getLogger(LogisticReportsServiceImpl.class.getName());
    String tmp_Recieved_From = "";
    String tmp_Recieved_ToTime = "";
    String responseString = null;
    private ArrayList<LogisticReportsBean> documentList;

    public ArrayList<LogisticReportsBean> getDocumentList(LogisticReportsAction logisticreportsAction) throws ServiceLocatorException {

        String doctype = "";
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
        String docdatepicker = logisticreportsAction.getDocdatepicker();
        String docdatepickerfrom = logisticreportsAction.getDocdatepickerfrom();
        String docSenderId = logisticreportsAction.getDocSenderId();
        String docSenderName = logisticreportsAction.getDocSenderName();
        String docBusId = logisticreportsAction.getDocBusId();
        String docRecName = logisticreportsAction.getDocRecName();
            System.out.println("DATE is********"+docdatepicker); 
            System.out.println("FromDATE is********"+docdatepickerfrom);
        if ((logisticreportsAction.getDocType() != null) && (!logisticreportsAction.getDocType().equals("-1"))) {
                doctype = logisticreportsAction.getDocType();
            }
            String status = logisticreportsAction.getStatus();
            String ackStatus = logisticreportsAction.getAckStatus();
            StringBuilder documentSearchQuery = new StringBuilder();
            if ("ARCHIVE".equals(logisticreportsAction.getDatabase())) {
                documentSearchQuery.append("SELECT DISTINCT(ARCHIVE_FILES.FILE_ID) as FILE_ID,"
                        + "ARCHIVE_FILES.ISA_NUMBER as ISA_NUMBER,ARCHIVE_FILES.FILE_TYPE as FILE_TYPE,ARCHIVE_FILES.SENDER_ID,ARCHIVE_FILES.RECEIVER_ID,"
                        + "ARCHIVE_FILES.FILE_ORIGIN as FILE_ORIGIN,ARCHIVE_FILES.TRANSACTION_TYPE as TRANSACTION_TYPE,"
                        + "ARCHIVE_FILES.DIRECTION as DIRECTION,ARCHIVE_FILES.DATE_TIME_RECEIVED as DATE_TIME_RECEIVED,"
                        + "ARCHIVE_FILES.STATUS as STATUS,ARCHIVE_FILES.ACK_STATUS as ACK_STATUS,"
                        + "ARCHIVE_FILES.SEC_KEY_VAL,ARCHIVE_FILES.REPROCESSSTATUS,ARCHIVE_FILES.FILENAME "
                        + "FROM ARCHIVE_FILES LEFT OUTER JOIN ARCHIVE_Transport_loadtender ten on (ten.FILE_ID=ARCHIVE_FILES.FILE_ID and ten.SHIPMENT_ID=ARCHIVE_FILES.SEC_KEY_VAL) ");
                       
                documentSearchQuery.append(" WHERE 1=1 AND FLOWFLAG LIKE '%L%'");
                if (doctype != null && !"".equals(doctype.trim())) {
                    documentSearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_FILES.TRANSACTION_TYPE", doctype.trim()));
                }
                if (status != null && !"-1".equals(status.trim())) {
                    documentSearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_FILES.STATUS", status.trim()));
                }
                if (ackStatus != null && !"-1".equals(ackStatus.trim())) {
                    documentSearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_FILES.ACK_STATUS", ackStatus.trim()));
                }
               
                if (docBusId != null && !"-1".equals(docBusId.trim())) {
                    documentSearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_FILES.RECEIVER_ID", docBusId.trim().toUpperCase()));
                }
                if (docSenderId != null && !"-1".equals(docSenderId.trim())) {
                    documentSearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_FILES.SENDER_ID", docSenderId.trim().toUpperCase()));
                }
//                if (docSenderName != null && !"-1".equals(docSenderName.trim())) {
//                    documentSearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_FILES.SENDER_ID", docSenderName.trim().toUpperCase()));
//                }
//                if (docRecName != null && !"-1".equals(docRecName.trim())) {
//                    documentSearchQuery.append(WildCardSql.getWildCardSql1("TP2.NAME", docRecName.trim().toUpperCase()));
//                }
                if (docdatepicker != null && !"".equals(docdatepicker)) {
                    System.out.println("date is*****"+docdatepicker);
                    tmp_Recieved_From = DateUtility.getInstance().DateViewToDBCompare(docdatepicker);
                    documentSearchQuery.append(" AND ARCHIVE_FILES.DATE_TIME_RECEIVED <= '" + tmp_Recieved_From + "'");
                }
                if (docdatepickerfrom != null && !"".equals(docdatepickerfrom)) {
                    System.out.println("date is*****"+docdatepickerfrom);
                    tmp_Recieved_From = DateUtility.getInstance().DateViewToDBCompare(docdatepickerfrom);
                    documentSearchQuery.append(" AND ARCHIVE_FILES.DATE_TIME_RECEIVED >= '" + tmp_Recieved_From + "'");
                }
                documentSearchQuery.append(" order by DATE_TIME_RECEIVED DESC fetch first 50 rows only");
            } else {
                documentSearchQuery.append("SELECT DISTINCT(FILES.FILE_ID) as FILE_ID,"
                        + "FILES.ISA_NUMBER as ISA_NUMBER,FILES.FILE_TYPE as FILE_TYPE,FILES.SENDER_ID,FILES.RECEIVER_ID,"
                        + "FILES.FILE_ORIGIN as FILE_ORIGIN,FILES.TRANSACTION_TYPE as TRANSACTION_TYPE,"
                        + "FILES.DIRECTION as DIRECTION,FILES.DATE_TIME_RECEIVED as DATE_TIME_RECEIVED,"
                        + "FILES.STATUS as STATUS,FILES.ACK_STATUS as ACK_STATUS,"
                        + "FILES.SEC_KEY_VAL,FILES.REPROCESSSTATUS,FILES.FILENAME "
                        + "FROM FILES LEFT OUTER JOIN Transport_loadtender ten on (ten.FILE_ID=FILES.FILE_ID and ten.SHIPMENT_ID=FILES.SEC_KEY_VAL) ");
                       
                documentSearchQuery.append(" WHERE 1=1 AND FLOWFLAG LIKE '%L%'");
                if (doctype != null && !"".equals(doctype.trim())) {
                    documentSearchQuery.append(WildCardSql.getWildCardSql1("FILES.TRANSACTION_TYPE", doctype.trim()));
                }
                if (status != null && !"-1".equals(status.trim())) {
                    documentSearchQuery.append(WildCardSql.getWildCardSql1("FILES.STATUS", status.trim()));
                }
                if (ackStatus != null && !"-1".equals(ackStatus.trim())) {
                    documentSearchQuery.append(WildCardSql.getWildCardSql1("FILES.ACK_STATUS", ackStatus.trim()));
                }
               
                if (docBusId != null && !"-1".equals(docBusId.trim())) {
                    documentSearchQuery.append(WildCardSql.getWildCardSql1("FILES.RECEIVER_ID", docBusId.trim().toUpperCase()));
                }
                if (docSenderId != null && !"-1".equals(docSenderId.trim())) {
                    documentSearchQuery.append(WildCardSql.getWildCardSql1("FILES.SENDER_ID", docSenderId.trim().toUpperCase()));
                }
//                if (docSenderName != null && !"-1".equals(docSenderName.trim())) {
//                    documentSearchQuery.append(WildCardSql.getWildCardSql1("TP1.NAME", docSenderName.trim().toUpperCase()));
//                }
//                if (docRecName != null && !"-1".equals(docRecName.trim())) {
//                    documentSearchQuery.append(WildCardSql.getWildCardSql1("TP2.NAME", docRecName.trim().toUpperCase()));
//                }
                if (docdatepicker != null && !"".equals(docdatepicker)) {
                    tmp_Recieved_From = DateUtility.getInstance().DateViewToDBCompare(docdatepicker);
                    documentSearchQuery.append(" AND FILES.DATE_TIME_RECEIVED <= '" + tmp_Recieved_From + "'");
                }
                if (docdatepickerfrom != null && !"".equals(docdatepickerfrom)) {
                    tmp_Recieved_From = DateUtility.getInstance().DateViewToDBCompare(docdatepickerfrom);
                    documentSearchQuery.append(" AND FILES.DATE_TIME_RECEIVED >= '" + tmp_Recieved_From + "'");
                }
                documentSearchQuery.append(" order by DATE_TIME_RECEIVED DESC fetch first 50 rows only");
            }
            System.out.println("Reports data is"+documentSearchQuery);
            connection = ConnectionProvider.getInstance().getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(documentSearchQuery.toString());
             Map<String, String> tradingPartners = DataSourceDataProvider.getInstance().getAllTradingPartners();
            documentList = new ArrayList<LogisticReportsBean>();
            while (resultSet.next()) {
                LogisticReportsBean logisticsreportBean = new LogisticReportsBean();
                logisticsreportBean.setFile_id(resultSet.getString("FILE_ID"));
                logisticsreportBean.setFile_origin(resultSet.getString("FILE_ORIGIN"));
                logisticsreportBean.setFile_type(resultSet.getString("FILE_TYPE"));
                logisticsreportBean.setIsa_number(resultSet.getString("ISA_NUMBER"));
                logisticsreportBean.setTransaction_type(resultSet.getString("TRANSACTION_TYPE"));
                logisticsreportBean.setDirection(resultSet.getString("DIRECTION"));
                logisticsreportBean.setDate_time_rec(resultSet.getTimestamp("DATE_TIME_RECEIVED"));
                logisticsreportBean.setStatus(resultSet.getString("STATUS"));
                 String pname_Reciever = "";
                if (((resultSet.getString("RECEIVER_ID")) != null)
                        && (((tradingPartners.get(resultSet.getString("RECEIVER_ID")))) != null)) {
                    pname_Reciever = ((tradingPartners.get(resultSet.getString("RECEIVER_ID"))).toString());
                } else {
                    pname_Reciever = "_";
                }
                logisticsreportBean.setPname(pname_Reciever);
                logisticsreportBean.setPoNumber(resultSet.getString("SEC_KEY_VAL"));
                logisticsreportBean.setReProcessStatus(resultSet.getString("REPROCESSSTATUS"));
                logisticsreportBean.setAckStatus(resultSet.getString("ACK_STATUS"));
                logisticsreportBean.setFile_name(resultSet.getString("FILENAME"));
                documentList.add(logisticsreportBean);
            }
        }catch (SQLException sqlException) {
                 LoggerUtility.log(logger, "SQLException occurred in getDocumentList method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
            }
catch (Exception exception) {
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
