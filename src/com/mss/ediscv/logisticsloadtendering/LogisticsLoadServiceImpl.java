/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mss.ediscv.logisticsloadtendering;

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
public class LogisticsLoadServiceImpl implements LogisticsLoadService {

    private static Logger logger = LogManager.getLogger(LogisticsLoadServiceImpl.class.getName());
    String tmp_Recieved_From = "";
    String tmp_Recieved_ToTime = "";
    private ArrayList<LogisticsLoadBean> documentList;

    public ArrayList<LogisticsLoadBean> buildLoadQuery(LogisticsLoadAction logisticsDocAction) throws ServiceLocatorException {

        String doctype = "";
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            String docdatepicker = logisticsDocAction.getDocdatepicker();
            String docdatepickerfrom = logisticsDocAction.getDocdatepickerfrom();
            String docSenderId = logisticsDocAction.getDocSenderId();
            String docSenderName = logisticsDocAction.getDocSenderName();
            String docBusId = logisticsDocAction.getDocBusId();
            String docRecName = logisticsDocAction.getDocRecName();

            if ((logisticsDocAction.getDocType() != null) && (!logisticsDocAction.getDocType().equals("-1"))) {
                doctype = logisticsDocAction.getDocType();
            }
            String corrattribute = logisticsDocAction.getCorrattribute();
            String corrvalue = logisticsDocAction.getCorrvalue();
            String corrattribute1 = logisticsDocAction.getCorrattribute1();
            String corrvalue1 = logisticsDocAction.getCorrvalue1();
            String corrattribute2 = logisticsDocAction.getCorrattribute2();
            String corrvalue2 = logisticsDocAction.getCorrvalue2();
            String status = logisticsDocAction.getStatus();
            String ackStatus = logisticsDocAction.getAckStatus();
            StringBuilder documentSearchQuery = new StringBuilder();
            if ("ARCHIVE".equals(logisticsDocAction.getDatabase())) {
                documentSearchQuery.append("SELECT tf.FILE_ID as file_id,tf.ISA_NUMBER as isa_number,tl.SHIPMENT_ID as SHIPMENT_ID,"
                        + "tf.FILE_TYPE as file_type,tf.SENDER_ID,tf.RECEIVER_ID,tf.FILE_ORIGIN as file_origin,tf.TRANSACTION_TYPE as tran_type,"
                        + "tf.ACK_STATUS as ack_status,tf.DIR"
                        + "ECTION as direction,tf.DATE_TIME_RECEIVED as datetime,"
                        + "tf.STATUS as status,tf.SEC_KEY_VAL as secval,tf.REPROCESSSTATUS as REPROCESSSTATUS "
                        + "FROM ARCHIVE_Transport_loadtender tl LEFT OUTER JOIN ARCHIVE_FILES TF ON "
                        + "(tl.FILE_ID=tf.FILE_ID and tl.SHIPMENT_ID=tf.SEC_KEY_VAL)  ");
                    
                documentSearchQuery.append(" WHERE 1=1 AND tf.FLOWFLAG LIKE '%L%'");
                if (((corrattribute != null) && (corrattribute.equalsIgnoreCase("Shipment Number"))) && (corrvalue != null && !"".equals(corrvalue.trim()))) {
                    documentSearchQuery.append(WildCardSql.getWildCardSql1("tl.SHIPMENT_ID", corrvalue.trim().toUpperCase()));
                }
                if (((corrattribute1 != null) && (corrattribute1.equalsIgnoreCase("Shipment Number"))) && (corrvalue1 != null && !"".equals(corrvalue1.trim()))) {
                    documentSearchQuery.append(WildCardSql.getWildCardSql1("tl.SHIPMENT_ID", corrvalue1.trim().toUpperCase()));
                }
                if (((corrattribute2 != null) && (corrattribute2.equalsIgnoreCase("Shipment Number"))) && (corrvalue2 != null && !"".equals(corrvalue2.trim()))) {
                    documentSearchQuery.append(WildCardSql.getWildCardSql1("tl.SHIPMENT_ID", corrvalue2.trim().toUpperCase()));
                }
                if (((corrattribute != null) && (corrattribute.equalsIgnoreCase("BOL Number"))) && (corrvalue != null && !"".equals(corrvalue.trim()))) {
                    documentSearchQuery.append(WildCardSql.getWildCardSql1("tl.BOL_NUMBER", corrvalue.trim().toUpperCase()));
                }
                if (((corrattribute1 != null) && (corrattribute1.equalsIgnoreCase("BOL Number"))) && (corrvalue1 != null && !"".equals(corrvalue1.trim()))) {
                    documentSearchQuery.append(WildCardSql.getWildCardSql1("tl.BOL_NUMBER", corrvalue1.trim().toUpperCase()));
                }
                if (((corrattribute2 != null) && (corrattribute2.equalsIgnoreCase("BOL Number"))) && (corrvalue2 != null && !"".equals(corrvalue2.trim()))) {
                    documentSearchQuery.append(WildCardSql.getWildCardSql1("tl.BOL_NUMBER", corrvalue2.trim().toUpperCase()));
                }
                if (((corrattribute != null) && (corrattribute.equalsIgnoreCase("PO Number"))) && (corrvalue != null && !"".equals(corrvalue.trim()))) {
                    documentSearchQuery.append(WildCardSql.getWildCardSql1("tl.PO_NUMBER", corrvalue.trim().toUpperCase()));
                }
                if (((corrattribute1 != null) && (corrattribute1.equalsIgnoreCase("PO Number"))) && (corrvalue1 != null && !"".equals(corrvalue1.trim()))) {
                    documentSearchQuery.append(WildCardSql.getWildCardSql1("tl.PO_NUMBER", corrvalue1.trim().toUpperCase()));
                }
                if (((corrattribute2 != null) && (corrattribute2.equalsIgnoreCase("PO Number"))) && (corrvalue2 != null && !"".equals(corrvalue2.trim()))) {
                    documentSearchQuery.append(WildCardSql.getWildCardSql1("tl.PO_NUMBER", corrvalue2.trim().toUpperCase()));
                }
                if (((corrattribute != null) && (corrattribute.equalsIgnoreCase("CO Number"))) && (corrvalue != null && !"".equals(corrvalue.trim()))) {
                    documentSearchQuery.append(WildCardSql.getWildCardSql1("tl.CO_NUMBER", corrvalue.trim().toUpperCase()));
                }
                if (((corrattribute1 != null) && (corrattribute1.equalsIgnoreCase("CO Number"))) && (corrvalue1 != null && !"".equals(corrvalue1.trim()))) {
                    documentSearchQuery.append(WildCardSql.getWildCardSql1("tl.CO_NUMBER", corrvalue1.trim().toUpperCase()));
                }
                if (((corrattribute2 != null) && (corrattribute2.equalsIgnoreCase("CO Number"))) && (corrvalue2 != null && !"".equals(corrvalue2.trim()))) {
                    documentSearchQuery.append(WildCardSql.getWildCardSql1("tl.CO_NUMBER", corrvalue2.trim().toUpperCase()));
                }
                if (((corrattribute != null) && (corrattribute.equalsIgnoreCase("Instance Id"))) && (corrvalue != null && !"".equals(corrvalue.trim()))) {
                    documentSearchQuery.append(WildCardSql.getWildCardSql1("TF.FILE_ID", corrvalue.trim().toUpperCase()));
                }
                if (((corrattribute1 != null) && (corrattribute1.equalsIgnoreCase("Instance Id"))) && (corrvalue1 != null && !"".equals(corrvalue1.trim()))) {
                    documentSearchQuery.append(WildCardSql.getWildCardSql1("TF.FILE_ID", corrvalue1.trim().toUpperCase()));
                }
                if (((corrattribute2 != null) && (corrattribute2.equalsIgnoreCase("Instance Id"))) && (corrvalue2 != null && !"".equals(corrvalue2.trim()))) {
                    documentSearchQuery.append(WildCardSql.getWildCardSql1("TF.FILE_ID", corrvalue2.trim().toUpperCase()));
                }
                if (((corrattribute != null) && (corrattribute.equalsIgnoreCase("Direction"))) && (corrvalue != null && !"".equals(corrvalue.trim()))) {
                    documentSearchQuery.append(WildCardSql.getWildCardSql1("TF.DIRECTION", corrvalue.trim().toUpperCase()));
                }
                if (((corrattribute1 != null) && (corrattribute1.equalsIgnoreCase("Direction"))) && (corrvalue1 != null && !"".equals(corrvalue1.trim()))) {
                    documentSearchQuery.append(WildCardSql.getWildCardSql1("TF.DIRECTION", corrvalue1.trim().toUpperCase()));
                }
                if (((corrattribute2 != null) && (corrattribute2.equalsIgnoreCase("Direction"))) && (corrvalue2 != null && !"".equals(corrvalue2.trim()))) {
                    documentSearchQuery.append(WildCardSql.getWildCardSql1("TF.DIRECTION", corrvalue2.trim().toUpperCase()));
                }

                if (doctype != null && !"".equals(doctype.trim())) {
                    documentSearchQuery.append(WildCardSql.getWildCardSql1("TF.TRANSACTION_TYPE", doctype.trim()));
                }
                if (status != null && !"-1".equals(status.trim())) {
                    documentSearchQuery.append(WildCardSql.getWildCardSql1("TF.STATUS", status.trim()));
                }
                if (ackStatus != null && !"-1".equals(ackStatus.trim())) {
                    documentSearchQuery.append(WildCardSql.getWildCardSql1("TF.ACK_STATUS", ackStatus.trim()));
                }
                if (docBusId != null && !"-1".equals(docBusId.trim())) {
                    documentSearchQuery.append(WildCardSql.getWildCardSql1("TF.RECEIVER_ID", docBusId.trim().toUpperCase()));
                }
                if (docSenderId != null && !"-1".equals(docSenderId.trim())) {
                    documentSearchQuery.append(WildCardSql.getWildCardSql1("TF.SENDER_ID", docSenderId.trim().toUpperCase()));
                }
//                if (docSenderName != null && !"-1".equals(docSenderName.trim())) {
//                    documentSearchQuery.append(WildCardSql.getWildCardSql1("TP1.NAME", docSenderName.trim().toUpperCase()));
//                }
//                if (docRecName != null && !"-1".equals(docRecName.trim())) {
//                    documentSearchQuery.append(WildCardSql.getWildCardSql1("TP2.NAME", docRecName.trim().toUpperCase()));
//                }
                if (docdatepicker != null && !"".equals(docdatepicker)) {
                    tmp_Recieved_From = DateUtility.getInstance().DateViewToDBCompare(docdatepicker);
                    documentSearchQuery.append(" AND tf.DATE_TIME_RECEIVED <= '" + tmp_Recieved_From + "'");
                }
                if (docdatepickerfrom != null && !"".equals(docdatepickerfrom)) {
                    tmp_Recieved_From = DateUtility.getInstance().DateViewToDBCompare(docdatepickerfrom);
                    documentSearchQuery.append(" AND tf.DATE_TIME_RECEIVED >= '" + tmp_Recieved_From + "'");
                }
                documentSearchQuery.append(" order by DATE_TIME_RECEIVED DESC fetch first 50 rows only");
            } else {
                documentSearchQuery.append("SELECT tf.FILE_ID as file_id,tf.ISA_NUMBER as isa_number,tl.SHIPMENT_ID as SHIPMENT_ID,"
                        + "tf.FILE_TYPE as file_type,tf.SENDER_ID,tf.RECEIVER_ID,tf.FILE_ORIGIN as file_origin,tf.TRANSACTION_TYPE as tran_type,"
                        + "tf.ACK_STATUS as ack_status,tf.DIR"
                        + "ECTION as direction,tf.DATE_TIME_RECEIVED as datetime,"
                        + "tf.STATUS as status,tf.SEC_KEY_VAL as secval,tf.REPROCESSSTATUS as REPROCESSSTATUS "
                        + "FROM Transport_loadtender tl LEFT OUTER JOIN FILES TF ON "
                        + "(tl.FILE_ID=tf.FILE_ID and tl.SHIPMENT_ID=tf.SEC_KEY_VAL)  ");
                        System.out.println("hiiii vimala");
                documentSearchQuery.append(" WHERE 1=1 AND tf.FLOWFLAG LIKE '%L%'");
                if (((corrattribute != null) && (corrattribute.equalsIgnoreCase("Shipment Number"))) && (corrvalue != null && !"".equals(corrvalue.trim()))) {
                    documentSearchQuery.append(WildCardSql.getWildCardSql1("tl.SHIPMENT_ID", corrvalue.trim().toUpperCase()));
                }
                if (((corrattribute1 != null) && (corrattribute1.equalsIgnoreCase("Shipment Number"))) && (corrvalue1 != null && !"".equals(corrvalue1.trim()))) {
                    documentSearchQuery.append(WildCardSql.getWildCardSql1("tl.SHIPMENT_ID", corrvalue1.trim().toUpperCase()));
                }
                if (((corrattribute2 != null) && (corrattribute2.equalsIgnoreCase("Shipment Number"))) && (corrvalue2 != null && !"".equals(corrvalue2.trim()))) {
                    documentSearchQuery.append(WildCardSql.getWildCardSql1("tl.SHIPMENT_ID", corrvalue2.trim().toUpperCase()));
                }
                if (((corrattribute != null) && (corrattribute.equalsIgnoreCase("BOL Number"))) && (corrvalue != null && !"".equals(corrvalue.trim()))) {
                    documentSearchQuery.append(WildCardSql.getWildCardSql1("tl.BOL_NUMBER", corrvalue.trim().toUpperCase()));
                }
                if (((corrattribute1 != null) && (corrattribute1.equalsIgnoreCase("BOL Number"))) && (corrvalue1 != null && !"".equals(corrvalue1.trim()))) {
                    documentSearchQuery.append(WildCardSql.getWildCardSql1("tl.BOL_NUMBER", corrvalue1.trim().toUpperCase()));
                }
                if ((((corrattribute2 != null) && (corrattribute2.equalsIgnoreCase("BOL Number"))) && (corrvalue2 != null && !"".equals(corrvalue2.trim())))) {
                    documentSearchQuery.append(WildCardSql.getWildCardSql1("tl.BOL_NUMBER", corrvalue2.trim().toUpperCase()));
                }
                if (((corrattribute != null) && (corrattribute.equalsIgnoreCase("PO Number"))) && (corrvalue != null && !"".equals(corrvalue.trim()))) {
                    documentSearchQuery.append(WildCardSql.getWildCardSql1("tl.PO_NUMBER", corrvalue.trim().toUpperCase()));
                }
                if (((corrattribute1 != null) && (corrattribute1.equalsIgnoreCase("PO Number"))) && (corrvalue1 != null && !"".equals(corrvalue1.trim()))) {
                    documentSearchQuery.append(WildCardSql.getWildCardSql1("tl.PO_NUMBER", corrvalue1.trim().toUpperCase()));
                }
                if (((corrattribute2 != null) && (corrattribute2.equalsIgnoreCase("PO Number"))) && (corrvalue2 != null && !"".equals(corrvalue2.trim()))) {
                    documentSearchQuery.append(WildCardSql.getWildCardSql1("tl.PO_NUMBER", corrvalue2.trim().toUpperCase()));
                }
                if (((corrattribute != null) && (corrattribute.equalsIgnoreCase("CO Number"))) && (corrvalue != null && !"".equals(corrvalue.trim()))) {
                    documentSearchQuery.append(WildCardSql.getWildCardSql1("tl.CO_NUMBER", corrvalue.trim().toUpperCase()));
                }
                if (((corrattribute1 != null) && (corrattribute1.equalsIgnoreCase("CO Number"))) && (corrvalue1 != null && !"".equals(corrvalue1.trim()))) {
                    documentSearchQuery.append(WildCardSql.getWildCardSql1("tl.CO_NUMBER", corrvalue1.trim().toUpperCase()));
                }
                if (((corrattribute2 != null) && (corrattribute2.equalsIgnoreCase("CO Number"))) && (corrvalue2 != null && !"".equals(corrvalue2.trim()))) {
                    documentSearchQuery.append(WildCardSql.getWildCardSql1("tl.CO_NUMBER", corrvalue2.trim().toUpperCase()));
                }
                if (((corrattribute != null) && (corrattribute.equalsIgnoreCase("Instance Id"))) && (corrvalue != null && !"".equals(corrvalue.trim()))) {
                    documentSearchQuery.append(WildCardSql.getWildCardSql1("TF.FILE_ID", corrvalue.trim().toUpperCase()));
                }
                if (((corrattribute1 != null) && (corrattribute1.equalsIgnoreCase("Instance Id"))) && (corrvalue1 != null && !"".equals(corrvalue1.trim()))) {
                    documentSearchQuery.append(WildCardSql.getWildCardSql1("TF.FILE_ID", corrvalue1.trim().toUpperCase()));
                }
                if (((corrattribute2 != null) && (corrattribute2.equalsIgnoreCase("Instance Id"))) && (corrvalue2 != null && !"".equals(corrvalue2.trim()))) {
                    documentSearchQuery.append(WildCardSql.getWildCardSql1("TF.FILE_ID", corrvalue2.trim().toUpperCase()));
                }
                if (((corrattribute != null) && (corrattribute.equalsIgnoreCase("Direction"))) && (corrvalue != null && !"".equals(corrvalue.trim()))) {
                    documentSearchQuery.append(WildCardSql.getWildCardSql1("TF.DIRECTION", corrvalue.trim().toUpperCase()));
                }
                if (((corrattribute1 != null) && (corrattribute1.equalsIgnoreCase("Direction"))) && (corrvalue1 != null && !"".equals(corrvalue1.trim()))) {
                    documentSearchQuery.append(WildCardSql.getWildCardSql1("TF.DIRECTION", corrvalue1.trim().toUpperCase()));
                }
                if (((corrattribute2 != null) && (corrattribute2.equalsIgnoreCase("Direction"))) && (corrvalue2 != null && !"".equals(corrvalue2.trim()))) {
                    documentSearchQuery.append(WildCardSql.getWildCardSql1("TF.DIRECTION", corrvalue2.trim().toUpperCase()));
                }

                if (doctype != null && !"".equals(doctype.trim())) {
                    documentSearchQuery.append(WildCardSql.getWildCardSql1("TF.TRANSACTION_TYPE", doctype.trim()));
                }
                if (status != null && !"-1".equals(status.trim())) {
                    documentSearchQuery.append(WildCardSql.getWildCardSql1("TF.STATUS", status.trim()));
                }
                if (ackStatus != null && !"-1".equals(ackStatus.trim())) {
                    documentSearchQuery.append(WildCardSql.getWildCardSql1("TF.ACK_STATUS", ackStatus.trim()));
                }
               
                if (docBusId != null && !"-1".equals(docBusId.trim())) {
                    documentSearchQuery.append(WildCardSql.getWildCardSql1("TF.RECEIVER_ID", docBusId.trim().toUpperCase()));
                }
                if (docSenderId != null && !"-1".equals(docSenderId.trim())) {
                    documentSearchQuery.append(WildCardSql.getWildCardSql1("TF.SENDER_ID", docSenderId.trim().toUpperCase()));
                }
//                if (docSenderName != null && !"-1".equals(docSenderName.trim())) {
//                    documentSearchQuery.append(WildCardSql.getWildCardSql1("TP1.NAME", docSenderName.trim().toUpperCase()));
//                }
//                if (docRecName != null && !"-1".equals(docRecName.trim())) {
//                    documentSearchQuery.append(WildCardSql.getWildCardSql1("TP2.NAME", docRecName.trim().toUpperCase()));
//                }
                if (docdatepicker != null && !"".equals(docdatepicker)) {
                    tmp_Recieved_From = DateUtility.getInstance().DateViewToDBCompare(docdatepicker);
                    documentSearchQuery.append(" AND tf.DATE_TIME_RECEIVED <= '" + tmp_Recieved_From + "'");
                }
                if (docdatepickerfrom != null && !"".equals(docdatepickerfrom)) {
                    tmp_Recieved_From = DateUtility.getInstance().DateViewToDBCompare(docdatepickerfrom);
                    documentSearchQuery.append(" AND tf.DATE_TIME_RECEIVED >= '" + tmp_Recieved_From + "'");
                }
                documentSearchQuery.append(" order by DATE_TIME_RECEIVED DESC fetch first 50 rows only");
            }
            System.out.println("buildLoadQuery query:"+documentSearchQuery.toString());
            connection = ConnectionProvider.getInstance().getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(documentSearchQuery.toString());
            documentList = new ArrayList<LogisticsLoadBean>();
             System.out.println("Query and resultset start time::"+DateUtility.getInstance().getCurrentDB2Timestamp());
             Map<String, String> tradingPartners = DataSourceDataProvider.getInstance().getAllTradingPartners();
            while (resultSet.next()) {
                LogisticsLoadBean logisticsdocBean = new LogisticsLoadBean();
                logisticsdocBean.setFile_id(resultSet.getString("file_id"));
                logisticsdocBean.setFile_origin(resultSet.getString("file_origin"));
                logisticsdocBean.setFile_type(resultSet.getString("file_type"));
                logisticsdocBean.setIsa_number(resultSet.getString("isa_number"));
                logisticsdocBean.setTransaction_type(resultSet.getString("tran_type"));
                logisticsdocBean.setDirection(resultSet.getString("direction"));
                logisticsdocBean.setDate_time_rec(resultSet.getTimestamp("datetime"));
                logisticsdocBean.setStatus(resultSet.getString("status"));
                String pname_Reciever = "";
                if (((resultSet.getString("RECEIVER_ID")) != null)
                        && (((tradingPartners.get(resultSet.getString("RECEIVER_ID")))) != null)) {
                    pname_Reciever = ((tradingPartners.get(resultSet.getString("RECEIVER_ID"))).toString());
                } else {
                    pname_Reciever = "_";
                }
                logisticsdocBean.setPname(pname_Reciever);
                logisticsdocBean.setPoNumber(resultSet.getString("secval"));
                logisticsdocBean.setReProcessStatus(resultSet.getString("REPROCESSSTATUS"));
                logisticsdocBean.setAckStatus(resultSet.getString("ack_status"));
                logisticsdocBean.setShipmentId(resultSet.getString("SHIPMENT_ID"));
                documentList.add(logisticsdocBean);
            }
            System.out.println("Resultset end time::"+DateUtility.getInstance().getCurrentDB2Timestamp());
        } catch (SQLException sqlException) {
            LoggerUtility.log(logger, "SQLException occurred in buildLoadQuery method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
        } catch (Exception exception) {
            LoggerUtility.log(logger, "Exception occurred in buildLoadQuery method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
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
                LoggerUtility.log(logger, "finally SQLException occurred in buildLoadQuery method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
            }
        }
        return documentList;
    }
}
