/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mss.ediscv.ltResponse;

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
public class LtResponseServiceImpl implements LtResponseService {

    private static Logger logger = LogManager.getLogger(LtResponseServiceImpl.class.getName());
    String tmp_Recieved_From = "";
    String tmp_Recieved_ToTime = "";
    private ArrayList<LtResponseBean> ltResponseList;
    private LtResponseBean ltResponseBean;

    public ArrayList<LtResponseBean> getLtResponseList(LtResponse ltResponse) throws ServiceLocatorException {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            String datepickerTo = ltResponse.getDatepickerTo();
            String datepickerfrom = ltResponse.getDatepickerfrom();
            String senderId = ltResponse.getSenderId();
            String senderName = ltResponse.getSenderName();
            String receiverId = ltResponse.getReceiverId();
            String receiverName = ltResponse.getReceiverName();
            String doctype = "";
            if ((ltResponse.getDocType() != null) && (!ltResponse.getDocType().equals("-1"))) {
                doctype = ltResponse.getDocType();
            }
            String corrattribute = ltResponse.getCorrattribute();
            String corrvalue = ltResponse.getCorrvalue();
            String corrattribute1 = ltResponse.getCorrattribute1();
            String corrvalue1 = ltResponse.getCorrvalue1();
            String status = ltResponse.getStatus();
            String ackStatus = ltResponse.getAckStatus();
            StringBuilder ltResponseSearchQuery = new StringBuilder();
            if ("ARCHIVE".equals(ltResponse.getDatabase())) {
                ltResponseSearchQuery.append("SELECT DISTINCT(ARCHIVE_FILES.FILE_ID) as FILE_ID,"
                        + "ARCHIVE_TRANSPORT_LT_RESPONSE.REF_ID,ARCHIVE_FILES.SENDER_ID,ARCHIVE_FILES.RECEIVER_ID,ARCHIVE_TRANSPORT_LT_RESPONSE.SHIPMENT_ID as SHIPMENT_ID,"
                        + "ARCHIVE_FILES.ISA_NUMBER as ISA_NUMBER,ARCHIVE_FILES.FILE_TYPE as FILE_TYPE,"
                        + "ARCHIVE_FILES.FILE_ORIGIN as FILE_ORIGIN,ARCHIVE_FILES.TRANSACTION_TYPE as TRANSACTION_TYPE,"
                        + "ARCHIVE_FILES.DIRECTION as DIRECTION,ARCHIVE_FILES.DATE_TIME_RECEIVED as DATE_TIME_RECEIVED,"
                        + "ARCHIVE_FILES.STATUS as STATUS,ARCHIVE_FILES.ACK_STATUS as ACK_STATUS,"
                        + "ARCHIVE_FILES.SEC_KEY_VAL,ARCHIVE_FILES.REPROCESSSTATUS "
                        + "FROM ARCHIVE_TRANSPORT_LT_RESPONSE "
                        + "LEFT OUTER JOIN ARCHIVE_FILES ON (ARCHIVE_TRANSPORT_LT_RESPONSE.FILE_ID =ARCHIVE_FILES.FILE_ID)");
                        
                ltResponseSearchQuery.append(" WHERE 1=1 AND ARCHIVE_FILES.FLOWFLAG = 'L' ");
                if (((corrattribute != null) && (corrattribute.equalsIgnoreCase("Ref Number"))) && (corrvalue != null && !"".equals(corrvalue.trim()))) {
                    ltResponseSearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_TRANSPORT_LT_RESPONSE.REF_ID", corrvalue.trim().toUpperCase()));
                }
                if (((corrattribute1 != null) && (corrattribute1.equalsIgnoreCase("Ref Number"))) && (corrvalue1 != null && !"".equals(corrvalue1.trim()))) {
                    ltResponseSearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_TRANSPORT_LT_RESPONSE.REF_ID", corrvalue1.trim().toUpperCase()));
                }
                if (((corrattribute != null) && (corrattribute.equalsIgnoreCase("Shipment Number"))) && (corrvalue != null && !"".equals(corrvalue.trim()))) {
                    ltResponseSearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_TRANSPORT_LT_RESPONSE.SHIPMENT_ID", corrvalue.trim().toUpperCase()));
                }
                if (((corrattribute1 != null) && (corrattribute1.equalsIgnoreCase("Shipment Number"))) && (corrvalue1 != null && !"".equals(corrvalue1.trim()))) {
                    ltResponseSearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_TRANSPORT_LT_RESPONSE.SHIPMENT_ID", corrvalue1.trim().toUpperCase()));
                }
                if (((corrattribute != null) && (corrattribute.equalsIgnoreCase("Instance Id"))) && (corrvalue != null && !"".equals(corrvalue.trim()))) {
                    ltResponseSearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_FILES.FILE_ID", corrvalue.trim().toUpperCase()));
                }
                if (((corrattribute1 != null) && (corrattribute1.equalsIgnoreCase("Instance Id"))) && (corrvalue1 != null && !"".equals(corrvalue1.trim()))) {
                    ltResponseSearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_FILES.FILE_ID", corrvalue1.trim().toUpperCase()));
                }
                if ((corrattribute != null) && (corrattribute.equalsIgnoreCase("Direction"))) {
                    if (corrvalue != null && !"".equals(corrvalue.trim())) {
                        ltResponseSearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_FILES.DIRECTION", corrvalue.trim().toUpperCase()));
                    }
                }
                if (((corrattribute1 != null) && (corrattribute1.equalsIgnoreCase("Direction"))) && (corrvalue1 != null && !"".equals(corrvalue1.trim()))) {
                    ltResponseSearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_FILES.DIRECTION", corrvalue1.trim().toUpperCase()));
                }
                if (doctype != null && !"".equals(doctype.trim())) {
                    ltResponseSearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_FILES.TRANSACTION_TYPE", doctype.trim()));
                }
                if (status != null && !"-1".equals(status.trim())) {
                    ltResponseSearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_FILES.STATUS", status.trim()));
                }
                if (ackStatus != null && !"-1".equals(ackStatus.trim())) {
                    ltResponseSearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_FILES.ACK_STATUS", ackStatus.trim()));
                }
               
                if (receiverId != null && !"-1".equals(receiverId.trim())) {
                    ltResponseSearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_FILES.RECEIVER_ID", receiverId.trim().toUpperCase()));
                }
                if (senderId != null && !"-1".equals(senderId.trim())) {
                    ltResponseSearchQuery.append(WildCardSql.getWildCardSql1(" ARCHIVE_FILES.SENDER_ID", senderId.trim().toUpperCase()));
                }
                if (senderName != null && !"-1".equals(senderName.trim())) {
                    ltResponseSearchQuery.append(WildCardSql.getWildCardSql1("TP1.NAME", senderName.trim().toUpperCase()));
                }
                if (receiverName != null && !"-1".equals(receiverName.trim())) {
                    ltResponseSearchQuery.append(WildCardSql.getWildCardSql1("TP2.NAME", receiverName.trim().toUpperCase()));
                }
                if (datepickerTo != null && !"".equals(datepickerTo)) {
                    tmp_Recieved_From = DateUtility.getInstance().DateViewToDBCompare(datepickerTo);
                    ltResponseSearchQuery.append(" AND ARCHIVE_FILES.DATE_TIME_RECEIVED <= '" + tmp_Recieved_From + "'");
                }
                if (datepickerfrom != null && !"".equals(datepickerfrom)) {
                    tmp_Recieved_From = DateUtility.getInstance().DateViewToDBCompare(datepickerfrom);
                    ltResponseSearchQuery.append(" AND ARCHIVE_FILES.DATE_TIME_RECEIVED >= '" + tmp_Recieved_From + "'");
                }
                ltResponseSearchQuery.append(" order by DATE_TIME_RECEIVED DESC fetch first 50 rows only");
            } else {
                ltResponseSearchQuery.append("SELECT DISTINCT(FILES.FILE_ID) as FILE_ID,TRANSPORT_LT_RESPONSE.REF_ID,TRANSPORT_LT_RESPONSE.SHIPMENT_ID as SHIPMENT_ID,"
                        + "FILES.ISA_NUMBER as ISA_NUMBER,FILES.FILE_TYPE as FILE_TYPE,FILES.SENDER_ID,FILES.RECEIVER_ID,"
                        + "FILES.FILE_ORIGIN as FILE_ORIGIN,FILES.TRANSACTION_TYPE as TRANSACTION_TYPE,"
                        + "FILES.DIRECTION as DIRECTION,FILES.DATE_TIME_RECEIVED as DATE_TIME_RECEIVED,"
                        + "FILES.STATUS as STATUS,FILES.ACK_STATUS as ACK_STATUS,"
                        + "FILES.SEC_KEY_VAL,FILES.REPROCESSSTATUS "
                        + "FROM TRANSPORT_LT_RESPONSE "
                        + "LEFT OUTER JOIN FILES ON (TRANSPORT_LT_RESPONSE.FILE_ID =FILES.FILE_ID)");
                      
                ltResponseSearchQuery.append(" WHERE 1=1 AND FILES.FLOWFLAG = 'L' ");
                if (((corrattribute != null) && (corrattribute.equalsIgnoreCase("Ref Number"))) && (corrvalue != null && !"".equals(corrvalue.trim()))) {
                    ltResponseSearchQuery.append(WildCardSql.getWildCardSql1("TRANSPORT_LT_RESPONSE.REF_ID", corrvalue.trim().toUpperCase()));
                }
                if (((corrattribute1 != null) && (corrattribute1.equalsIgnoreCase("Ref Number"))) && (corrvalue1 != null && !"".equals(corrvalue1.trim()))) {
                    ltResponseSearchQuery.append(WildCardSql.getWildCardSql1("TRANSPORT_LT_RESPONSE.REF_ID", corrvalue1.trim().toUpperCase()));
                }
                if (((corrattribute != null) && (corrattribute.equalsIgnoreCase("Shipment Number"))) && (corrvalue != null && !"".equals(corrvalue.trim()))) {
                    ltResponseSearchQuery.append(WildCardSql.getWildCardSql1("TRANSPORT_LT_RESPONSE.SHIPMENT_ID", corrvalue.trim().toUpperCase()));
                }
                if (((corrattribute1 != null) && (corrattribute1.equalsIgnoreCase("Shipment Number"))) && (corrvalue1 != null && !"".equals(corrvalue1.trim()))) {
                    ltResponseSearchQuery.append(WildCardSql.getWildCardSql1("TRANSPORT_LT_RESPONSE.SHIPMENT_ID", corrvalue1.trim().toUpperCase()));
                }
                if (((corrattribute != null) && (corrattribute.equalsIgnoreCase("Instance Id"))) && (corrvalue != null && !"".equals(corrvalue.trim()))) {
                    ltResponseSearchQuery.append(WildCardSql.getWildCardSql1("FILES.FILE_ID", corrvalue.trim().toUpperCase()));
                }
                if (((corrattribute1 != null) && (corrattribute1.equalsIgnoreCase("Instance Id"))) && (corrvalue1 != null && !"".equals(corrvalue1.trim()))) {
                    ltResponseSearchQuery.append(WildCardSql.getWildCardSql1("FILES.FILE_ID", corrvalue1.trim().toUpperCase()));
                }
                if (((corrattribute != null) && (corrattribute.equalsIgnoreCase("Direction"))) && (corrvalue != null && !"".equals(corrvalue.trim()))) {
                    ltResponseSearchQuery.append(WildCardSql.getWildCardSql1("FILES.DIRECTION", corrvalue.trim().toUpperCase()));
                }
                if (((corrattribute1 != null) && (corrattribute1.equalsIgnoreCase("Direction"))) && (corrvalue1 != null && !"".equals(corrvalue1.trim()))) {
                    ltResponseSearchQuery.append(WildCardSql.getWildCardSql1("FILES.DIRECTION", corrvalue1.trim().toUpperCase()));
                }
                if (doctype != null && !"".equals(doctype.trim())) {
                    ltResponseSearchQuery.append(WildCardSql.getWildCardSql1("FILES.TRANSACTION_TYPE", doctype.trim()));
                }
                if (status != null && !"-1".equals(status.trim())) {
                    ltResponseSearchQuery.append(WildCardSql.getWildCardSql1("FILES.STATUS", status.trim()));
                }
                if (ackStatus != null && !"-1".equals(ackStatus.trim())) {
                    ltResponseSearchQuery.append(WildCardSql.getWildCardSql1("FILES.ACK_STATUS", ackStatus.trim()));
                }
               
                if (receiverId != null && !"-1".equals(receiverId.trim())) {
                    ltResponseSearchQuery.append(WildCardSql.getWildCardSql1("FILES.RECEIVER_ID", receiverId.trim().toUpperCase()));
                }
                if (senderId != null && !"-1".equals(senderId.trim())) {
                    ltResponseSearchQuery.append(WildCardSql.getWildCardSql1("FILES.SENDER_ID", senderId.trim().toUpperCase()));
                }
                if (senderName != null && !"-1".equals(senderName.trim())) {
                    ltResponseSearchQuery.append(WildCardSql.getWildCardSql1("TP1.NAME", senderName.trim().toUpperCase()));
                }
                if (receiverName != null && !"-1".equals(receiverName.trim())) {
                    ltResponseSearchQuery.append(WildCardSql.getWildCardSql1("TP2.NAME", receiverName.trim().toUpperCase()));
                }
                if (datepickerTo != null && !"".equals(datepickerTo)) {
                    tmp_Recieved_From = DateUtility.getInstance().DateViewToDBCompare(datepickerTo);
                    ltResponseSearchQuery.append(" AND FILES.DATE_TIME_RECEIVED <= '" + tmp_Recieved_From + "'");
                }
                if (datepickerfrom != null && !"".equals(datepickerfrom)) {
                    tmp_Recieved_From = DateUtility.getInstance().DateViewToDBCompare(datepickerfrom);
                    ltResponseSearchQuery.append(" AND FILES.DATE_TIME_RECEIVED >= '" + tmp_Recieved_From + "'");
                }
                ltResponseSearchQuery.append(" order by DATE_TIME_RECEIVED DESC fetch first 50 rows only");
            }
            System.out.println("ltResponseSearchQuery query:"+ltResponseSearchQuery.toString());
            connection = ConnectionProvider.getInstance().getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(ltResponseSearchQuery.toString());
            ltResponseList = new ArrayList<LtResponseBean>();
            Map<String, String> tradingPartners = DataSourceDataProvider.getInstance().getAllTradingPartners();
            System.out.println("Query and resultset start time::"+DateUtility.getInstance().getCurrentDB2Timestamp());
            while (resultSet.next()) {
                LtResponseBean ltResponseBean = new LtResponseBean();
                ltResponseBean.setFileId(resultSet.getString("FILE_ID"));
                ltResponseBean.setFileOrgin(resultSet.getString("FILE_ORIGIN"));
                ltResponseBean.setFileType(resultSet.getString("FILE_TYPE"));
                ltResponseBean.setIsaNum(resultSet.getString("ISA_NUMBER"));
                ltResponseBean.setTransType(resultSet.getString("TRANSACTION_TYPE"));
                ltResponseBean.setDirection(resultSet.getString("DIRECTION"));
                ltResponseBean.setStatus(resultSet.getString("STATUS"));
               
                String pname_Reciever = "";
                if (((resultSet.getString("RECEIVER_ID")) != null)
                        && (((tradingPartners.get(resultSet.getString("RECEIVER_ID")))) != null)) {
                    pname_Reciever = ((tradingPartners.get(resultSet.getString("RECEIVER_ID"))).toString());
                } else {
                    pname_Reciever = "_";
                }
                ltResponseBean.setPartnerName(pname_Reciever);
                
                ltResponseBean.setPoNum(resultSet.getString("SEC_KEY_VAL"));
                ltResponseBean.setReprocess(resultSet.getString("REPROCESSSTATUS"));
                ltResponseBean.setAckStatus(resultSet.getString("ACK_STATUS"));
                ltResponseBean.setRefId(resultSet.getString("REF_ID"));
                ltResponseBean.setShipmentId(resultSet.getString("SHIPMENT_ID"));
                ltResponseList.add(ltResponseBean);
            }
             System.out.println("Resultset end time::"+DateUtility.getInstance().getCurrentDB2Timestamp());
        } catch (SQLException sqlException) {
            LoggerUtility.log(logger, "SQLException occurred in getLtResponseList method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
        } catch (Exception exception) {
            LoggerUtility.log(logger, "Exception occurred in getLtResponseList method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
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
                LoggerUtility.log(logger, "SQLException occurred in getLtResponseList method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
            }
        }
        return ltResponseList;
    }

}
