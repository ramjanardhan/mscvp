/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mss.ediscv.logisticsshipment;

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
public class LtShipmentServiceImpl implements LtShipmentService {

 private static Logger logger = LogManager.getLogger(LtShipmentServiceImpl.class.getName());
    String tmp_Recieved_From = "";
    String tmp_Recieved_ToTime = "";
    private ArrayList<LtShipmentBean> ltShipmentBeanList;
    private LtShipmentBean ltShipmentBean;

    public ArrayList getLtShipmentList(LogisticsShipmentAction logisticsShipmentAction) throws ServiceLocatorException {

        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            String datepickerTo = logisticsShipmentAction.getDatepickerTo();
            String datepickerfrom = logisticsShipmentAction.getDatepickerfrom();
            String senderId = logisticsShipmentAction.getSenderId();
            String senderName = logisticsShipmentAction.getSenderName();
            String receiverId = logisticsShipmentAction.getReceiverId();
            String receiverName = logisticsShipmentAction.getReceiverName();
            String doctype = "";
            if ((logisticsShipmentAction.getDocType() != null) && (!logisticsShipmentAction.getDocType().equals("-1"))) {
                doctype = logisticsShipmentAction.getDocType();
            }
            String corrattribute = logisticsShipmentAction.getCorrattribute();
            String corrvalue = logisticsShipmentAction.getCorrvalue();
            String corrattribute1 = logisticsShipmentAction.getCorrattribute1();
            String corrvalue1 = logisticsShipmentAction.getCorrvalue1();
            String status = logisticsShipmentAction.getStatus();
            String ackStatus = logisticsShipmentAction.getAckStatus();
            StringBuilder ltShipmentSearchQuery = new StringBuilder();
            if ("ARCHIVE".equals(logisticsShipmentAction.getDatabase())) {
                ltShipmentSearchQuery.append("SELECT DISTINCT (ARCHIVE_FILES.FILE_ID) as FILE_ID,ARCHIVE_TRANSPORT_SHIPMENT.STOP_SEQ_NUM,"
                        + "ARCHIVE_FILES.ISA_NUMBER as ISA_NUMBER,ARCHIVE_FILES.SENDER_ID,ARCHIVE_FILES.RECEIVER_ID,ARCHIVE_FILES.FILE_TYPE as FILE_TYPE,ARCHIVE_FILES.CARRIER_STATUS  as CARRIER_STATUS,"
                        + "ARCHIVE_FILES.FILE_ORIGIN as FILE_ORIGIN,ARCHIVE_FILES.TRANSACTION_TYPE as TRANSACTION_TYPE,"
                        + "ARCHIVE_FILES.DIRECTION as DIRECTION,ARCHIVE_FILES.DATE_TIME_RECEIVED as DATE_TIME_RECEIVED,"
                        + "ARCHIVE_FILES.STATUS as STATUS,ARCHIVE_FILES.ACK_STATUS as ACK_STATUS,"
                        + "ARCHIVE_FILES.SEC_KEY_VAL,ARCHIVE_FILES.REPROCESSSTATUS,ARCHIVE_TRANSPORT_SHIPMENT.SHIPMENT_ID,ARCHIVE_TRANSPORT_SHIPMENT.ID as ID,ARCHIVE_TRANSPORT_SHIPMENT.PO_NUMBER "
                        + "FROM ARCHIVE_TRANSPORT_SHIPMENT "
                        + "LEFT OUTER JOIN ARCHIVE_FILES ON (ARCHIVE_TRANSPORT_SHIPMENT.FILE_ID =ARCHIVE_FILES.FILE_ID)");
                       
                ltShipmentSearchQuery.append(" WHERE 1=1 AND ARCHIVE_FILES.FLOWFLAG = 'L' ");
                if (((corrattribute != null) && (corrattribute.equalsIgnoreCase("BOL Number"))) && (corrvalue != null && !"".equals(corrvalue.trim()))) {
                    ltShipmentSearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_TRANSPORT_SHIPMENT.BOL_NUMBER", corrvalue.trim().toUpperCase()));
                }
                if (((corrattribute1 != null) && (corrattribute1.equalsIgnoreCase("BOL Number"))) && (corrvalue1 != null && !"".equals(corrvalue1.trim()))) {
                    ltShipmentSearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_TRANSPORT_SHIPMENT.BOL_NUMBER", corrvalue1.trim().toUpperCase()));
                }
                if ((corrattribute != null) && (corrattribute.equalsIgnoreCase("Shipment Number"))) {
                    if (corrvalue != null && !"".equals(corrvalue.trim())) {
                        ltShipmentSearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_TRANSPORT_SHIPMENT.SHIPMENT_ID", corrvalue.trim().toUpperCase()));
                    }
                }
                if ((corrattribute1 != null) && (corrattribute1.equalsIgnoreCase("Shipment Number"))) {
                    if (corrvalue1 != null && !"".equals(corrvalue1.trim())) {
                        ltShipmentSearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_TRANSPORT_SHIPMENT.SHIPMENT_ID", corrvalue1.trim().toUpperCase()));
                    }
                }
                if ((corrattribute != null) && (corrattribute.equalsIgnoreCase("PO Number"))) {
                    if (corrvalue != null && !"".equals(corrvalue.trim())) {
                        ltShipmentSearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_TRANSPORT_SHIPMENT.PO_NUMBER", corrvalue.trim().toUpperCase()));
                    }
                }
                if (((corrattribute1 != null) && (corrattribute1.equalsIgnoreCase("PO Number"))) && (corrvalue1 != null && !"".equals(corrvalue1.trim()))) {
                    ltShipmentSearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_TRANSPORT_SHIPMENT.PO_NUMBER", corrvalue1.trim().toUpperCase()));
                }
                if (((corrattribute != null) && (corrattribute.equalsIgnoreCase("Stop Seq Number"))) && (corrvalue != null && !"".equals(corrvalue.trim()))) {
                    ltShipmentSearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_TRANSPORT_SHIPMENT.STOP_SEQ_NUM", corrvalue.trim().toUpperCase()));
                }
                if (((corrattribute1 != null) && (corrattribute1.equalsIgnoreCase("Stop Seq Number"))) && (corrvalue1 != null && !"".equals(corrvalue1.trim()))) {
                    ltShipmentSearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_TRANSPORT_SHIPMENT.STOP_SEQ_NUM", corrvalue1.trim().toUpperCase()));
                }
                if (((corrattribute != null) && (corrattribute.equalsIgnoreCase("Instance Id"))) && (corrvalue != null && !"".equals(corrvalue.trim()))) {
                    ltShipmentSearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_FILES.FILE_ID", corrvalue.trim().toUpperCase()));
                }
                if (((corrattribute1 != null) && (corrattribute1.equalsIgnoreCase("Instance Id"))) && (corrvalue1 != null && !"".equals(corrvalue1.trim()))) {
                    ltShipmentSearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_FILES.FILE_ID", corrvalue1.trim().toUpperCase()));
                }
                if (((corrattribute != null) && (corrattribute.equalsIgnoreCase("Direction"))) && (corrvalue != null && !"".equals(corrvalue.trim()))) {
                    ltShipmentSearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_FILES.DIRECTION", corrvalue.trim().toUpperCase()));
                }
                if (((corrattribute1 != null) && (corrattribute1.equalsIgnoreCase("Direction"))) && (corrvalue1 != null && !"".equals(corrvalue1.trim()))) {
                    ltShipmentSearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_FILES.DIRECTION", corrvalue1.trim().toUpperCase()));
                }

                if (doctype != null && !"".equals(doctype.trim())) {
                    ltShipmentSearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_FILES.TRANSACTION_TYPE", doctype.trim()));
                }
                if (status != null && !"-1".equals(status.trim())) {
                    ltShipmentSearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_FILES.STATUS", status.trim()));
                }
                if (ackStatus != null && !"-1".equals(ackStatus.trim())) {
                    ltShipmentSearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_FILES.ACK_STATUS", ackStatus.trim()));
                }
                
               
                if (receiverId != null && !"-1".equals(receiverId.trim())) {
                    ltShipmentSearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_FILES.RECEIVER_ID", receiverId.trim().toUpperCase()));
                }
                if (senderId != null && !"-1".equals(senderId.trim())) {
                    ltShipmentSearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_FILES.SENDER_ID", senderId.trim().toUpperCase()));
                }
//                if (senderName != null && !"-1".equals(senderName.trim())) {
//                    ltShipmentSearchQuery.append(WildCardSql.getWildCardSql1("TP1.NAME", senderName.trim().toUpperCase()));
//                }
//                if (receiverName != null && !"-1".equals(receiverName.trim())) {
//                    ltShipmentSearchQuery.append(WildCardSql.getWildCardSql1("TP2.NAME", receiverName.trim().toUpperCase()));
//                }
                if (datepickerTo != null && !"".equals(datepickerTo)) {
                    tmp_Recieved_From = DateUtility.getInstance().DateViewToDBCompare(datepickerTo);
                    ltShipmentSearchQuery.append(" AND ARCHIVE_FILES.DATE_TIME_RECEIVED <= '" + tmp_Recieved_From + "'");
                }
                if (datepickerfrom != null && !"".equals(datepickerfrom)) {
                    tmp_Recieved_From = DateUtility.getInstance().DateViewToDBCompare(datepickerfrom);
                    ltShipmentSearchQuery.append(" AND ARCHIVE_FILES.DATE_TIME_RECEIVED >= '" + tmp_Recieved_From + "'");
                }
                ltShipmentSearchQuery.append(" order by DATE_TIME_RECEIVED DESC fetch first 50 rows only");
            } else {
                ltShipmentSearchQuery.append("SELECT DISTINCT (FILES.FILE_ID) as FILE_ID,TRANSPORT_SHIPMENT.STOP_SEQ_NUM,"
                        + "FILES.ISA_NUMBER as ISA_NUMBER,FILES.FILE_TYPE as FILE_TYPE,FILES.SENDER_ID,FILES.RECEIVER_ID,FILES.CARRIER_STATUS  as CARRIER_STATUS,"
                        + "FILES.FILE_ORIGIN as FILE_ORIGIN,FILES.TRANSACTION_TYPE as TRANSACTION_TYPE,"
                        + "FILES.DIRECTION as DIRECTION,FILES.DATE_TIME_RECEIVED as DATE_TIME_RECEIVED,"
                        + "FILES.STATUS as STATUS,FILES.ACK_STATUS as ACK_STATUS,"
                        + "FILES.SEC_KEY_VAL,FILES.REPROCESSSTATUS,TRANSPORT_SHIPMENT.SHIPMENT_ID,TRANSPORT_SHIPMENT.ID as ID,TRANSPORT_SHIPMENT.PO_NUMBER "
                        + "FROM TRANSPORT_SHIPMENT "
                        + "LEFT OUTER JOIN FILES ON (TRANSPORT_SHIPMENT.FILE_ID =FILES.FILE_ID)");
                        
                ltShipmentSearchQuery.append(" WHERE 1=1 AND FILES.FLOWFLAG = 'L' ");
                if (((corrattribute != null) && (corrattribute.equalsIgnoreCase("BOL Number"))) && (corrvalue != null && !"".equals(corrvalue.trim()))) {
                    ltShipmentSearchQuery.append(WildCardSql.getWildCardSql1("TRANSPORT_SHIPMENT.BOL_NUMBER", corrvalue.trim().toUpperCase()));
                }
                if (((corrattribute1 != null) && (corrattribute1.equalsIgnoreCase("BOL Number"))) && (corrvalue1 != null && !"".equals(corrvalue1.trim()))) {
                    ltShipmentSearchQuery.append(WildCardSql.getWildCardSql1("TRANSPORT_SHIPMENT.BOL_NUMBER", corrvalue1.trim().toUpperCase()));
                }
                if (((corrattribute != null) && (corrattribute.equalsIgnoreCase("Shipment Number"))) && (corrvalue != null && !"".equals(corrvalue.trim()))) {
                    ltShipmentSearchQuery.append(WildCardSql.getWildCardSql1("TRANSPORT_SHIPMENT.SHIPMENT_ID", corrvalue.trim().toUpperCase()));
                }
                if (((corrattribute1 != null) && (corrattribute1.equalsIgnoreCase("Shipment Number"))) && (corrvalue1 != null && !"".equals(corrvalue1.trim()))) {
                    ltShipmentSearchQuery.append(WildCardSql.getWildCardSql1("TRANSPORT_SHIPMENT.SHIPMENT_ID", corrvalue1.trim().toUpperCase()));
                }
                if (((corrattribute != null) && (corrattribute.equalsIgnoreCase("PO Number"))) && (corrvalue != null && !"".equals(corrvalue.trim()))) {
                    ltShipmentSearchQuery.append(WildCardSql.getWildCardSql1("TRANSPORT_SHIPMENT.PO_NUMBER", corrvalue.trim().toUpperCase()));
                }
                if (((corrattribute1 != null) && (corrattribute1.equalsIgnoreCase("PO Number"))) && (corrvalue1 != null && !"".equals(corrvalue1.trim()))) {
                    ltShipmentSearchQuery.append(WildCardSql.getWildCardSql1("TRANSPORT_SHIPMENT.PO_NUMBER", corrvalue1.trim().toUpperCase()));
                }
                if (((corrattribute != null) && (corrattribute.equalsIgnoreCase("Stop Seq Number"))) && (corrvalue != null && !"".equals(corrvalue.trim()))) {
                    ltShipmentSearchQuery.append(WildCardSql.getWildCardSql1("TRANSPORT_SHIPMENT.STOP_SEQ_NUM", corrvalue.trim().toUpperCase()));
                }
                if (((corrattribute1 != null) && (corrattribute1.equalsIgnoreCase("Stop Seq Number"))) && (corrvalue1 != null && !"".equals(corrvalue1.trim()))) {
                    ltShipmentSearchQuery.append(WildCardSql.getWildCardSql1("TRANSPORT_SHIPMENT.STOP_SEQ_NUM", corrvalue1.trim().toUpperCase()));
                }
                if (((corrattribute != null) && (corrattribute.equalsIgnoreCase("Instance Id"))) && (corrvalue != null && !"".equals(corrvalue.trim()))) {
                    ltShipmentSearchQuery.append(WildCardSql.getWildCardSql1("FILES.FILE_ID", corrvalue.trim().toUpperCase()));
                }
                if (((corrattribute1 != null) && (corrattribute1.equalsIgnoreCase("Instance Id"))) && (corrvalue1 != null && !"".equals(corrvalue1.trim()))) {
                    ltShipmentSearchQuery.append(WildCardSql.getWildCardSql1("FILES.FILE_ID", corrvalue1.trim().toUpperCase()));
                }
                if (((corrattribute != null) && (corrattribute.equalsIgnoreCase("Direction"))) && (corrvalue != null && !"".equals(corrvalue.trim()))) {
                    ltShipmentSearchQuery.append(WildCardSql.getWildCardSql1("FILES.DIRECTION", corrvalue.trim().toUpperCase()));
                }
                if (((corrattribute1 != null) && (corrattribute1.equalsIgnoreCase("Direction"))) && (corrvalue1 != null && !"".equals(corrvalue1.trim()))) {
                    ltShipmentSearchQuery.append(WildCardSql.getWildCardSql1("FILES.DIRECTION", corrvalue1.trim().toUpperCase()));
                }
                if (doctype != null && !"".equals(doctype.trim())) {
                    ltShipmentSearchQuery.append(WildCardSql.getWildCardSql1("FILES.TRANSACTION_TYPE", doctype.trim()));
                }
                if (status != null && !"-1".equals(status.trim())) {
                    ltShipmentSearchQuery.append(WildCardSql.getWildCardSql1("FILES.STATUS", status.trim()));
                }
                if (ackStatus != null && !"-1".equals(ackStatus.trim())) {
                    ltShipmentSearchQuery.append(WildCardSql.getWildCardSql1("FILES.ACK_STATUS", ackStatus.trim()));
                }
               
                if (receiverId != null && !"-1".equals(receiverId.trim())) {
                    ltShipmentSearchQuery.append(WildCardSql.getWildCardSql1("FILES.RECEIVER_ID", receiverId.trim().toUpperCase()));
                }
                if (senderId != null && !"-1".equals(senderId.trim())) {
                    ltShipmentSearchQuery.append(WildCardSql.getWildCardSql1("FILES.SENDER_ID", senderId.trim().toUpperCase()));
                }
                if (senderName != null && !"-1".equals(senderName.trim())) {
                    ltShipmentSearchQuery.append(WildCardSql.getWildCardSql1("TP1.NAME", senderName.trim().toUpperCase()));
                }
                if (receiverName != null && !"-1".equals(receiverName.trim())) {
                    ltShipmentSearchQuery.append(WildCardSql.getWildCardSql1("TP2.NAME", receiverName.trim().toUpperCase()));
                }
                if (datepickerTo != null && !"".equals(datepickerTo)) {
                    tmp_Recieved_From = DateUtility.getInstance().DateViewToDBCompare(datepickerTo);
                    ltShipmentSearchQuery.append(" AND FILES.DATE_TIME_RECEIVED <= '" + tmp_Recieved_From + "'");
                }
                if (datepickerfrom != null && !"".equals(datepickerfrom)) {
                    tmp_Recieved_From = DateUtility.getInstance().DateViewToDBCompare(datepickerfrom);
                    ltShipmentSearchQuery.append(" AND FILES.DATE_TIME_RECEIVED >= '" + tmp_Recieved_From + "'");
                }
                ltShipmentSearchQuery.append(" order by DATE_TIME_RECEIVED DESC fetch first 50 rows only");
            }
            System.out.println("getLtShipmentList query:"+ltShipmentSearchQuery.toString());
            connection = ConnectionProvider.getInstance().getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(ltShipmentSearchQuery.toString());
            ltShipmentBeanList = new ArrayList<LtShipmentBean>();
            Map<String, String> tradingPartners = DataSourceDataProvider.getInstance().getAllTradingPartners();
           System.out.println("Query and resultset start time::"+DateUtility.getInstance().getCurrentDB2Timestamp());
            while (resultSet.next()) {
                ltShipmentBean = new LtShipmentBean();
                ltShipmentBean.setInstanceId(resultSet.getString("FILE_ID"));
                ltShipmentBean.setDateTime(resultSet.getTimestamp("DATE_TIME_RECEIVED"));
                ltShipmentBean.setAsnNum(resultSet.getString("SHIPMENT_ID"));
                ltShipmentBean.setDirection(resultSet.getString("DIRECTION"));
                ltShipmentBean.setStatus(resultSet.getString("STATUS"));
                String pname_Reciever = "";
                if (((resultSet.getString("RECEIVER_ID")) != null)
                        && (((tradingPartners.get(resultSet.getString("RECEIVER_ID")))) != null)) {
                    pname_Reciever = ((tradingPartners.get(resultSet.getString("RECEIVER_ID"))).toString());
                } else {
                    pname_Reciever = "_";
                }
                ltShipmentBean.setPartner(pname_Reciever);
                ltShipmentBean.setPoNum(resultSet.getString("PO_NUMBER"));
                ltShipmentBean.setAckStatus(resultSet.getString("ACK_STATUS"));
                ltShipmentBean.setCarrierStatus(resultSet.getString("CARRIER_STATUS"));
                ltShipmentBean.setId(resultSet.getInt("ID"));
                ltShipmentBeanList.add(ltShipmentBean);
            }
             System.out.println("Resultset end time::"+DateUtility.getInstance().getCurrentDB2Timestamp());
        } catch (SQLException sqlException) {
                 LoggerUtility.log(logger, "SQLException occurred in getLtShipmentList method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
            }
catch (Exception exception) {
             LoggerUtility.log(logger, "Exception occurred in getLtShipmentList method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
        }finally {
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
            }  catch (SQLException sqlException) {
                 LoggerUtility.log(logger, "finally SQLException occurred in getLtShipmentList method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
            }
        }
        return ltShipmentBeanList;
    }
}
