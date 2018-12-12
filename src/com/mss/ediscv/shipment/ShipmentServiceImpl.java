package com.mss.ediscv.shipment;

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
public class ShipmentServiceImpl implements ShipmentService {

    private static Logger logger = LogManager.getLogger(ShipmentServiceImpl.class.getName());
    String tmp_Recieved_From = "";
    String tmp_Recieved_ToTime = "";
    private ArrayList<ShipmentBean> shipmentList;
    private ShipmentBean shipmentBean;

    public ArrayList<ShipmentBean> buildshipmentSQuery(ShipmentSearchAction shipmentSearchbean) throws ServiceLocatorException {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            String datepickerTo = shipmentSearchbean.getDatepicker();
            String datePickerFrom = shipmentSearchbean.getDatepickerfrom();
            String senderId = "";
            if (shipmentSearchbean.getSenderId() != null && !shipmentSearchbean.getSenderId().equals("-1")) {
                senderId = shipmentSearchbean.getSenderId();
            }
            String senderName = "";
            if (shipmentSearchbean.getSenderName() != null && !shipmentSearchbean.getSenderName().equals("-1")) {
                senderName = shipmentSearchbean.getSenderName();
            }
            String recName = "";
            if (shipmentSearchbean.getRecName() != null && !shipmentSearchbean.getRecName().equals("-1")) {
                recName = shipmentSearchbean.getRecName();
            }
            String recId = "";
            if (shipmentSearchbean.getBuId() != null && !shipmentSearchbean.getBuId().equals("-1")) {
                recId = shipmentSearchbean.getBuId();
            }
            String status = shipmentSearchbean.getStatus();
            String ackStatus = shipmentSearchbean.getAckStatus();
            String corrattribute = shipmentSearchbean.getCorrattribute();
            String corrvalue = shipmentSearchbean.getCorrvalue();
            String corrattribute1 = shipmentSearchbean.getCorrattribute1();
            String corrvalue1 = shipmentSearchbean.getCorrvalue1();
            String doctype = "";
            if (shipmentSearchbean.getDocType() != null && !shipmentSearchbean.getDocType().equals("-1")) {
                doctype = shipmentSearchbean.getDocType();
            }
            StringBuilder shipmentSearchQuery = new StringBuilder();
            if ("ARCHIVE".equals(shipmentSearchbean.getDatabase())) {
                shipmentSearchQuery.append("SELECT DISTINCT(ARCHIVE_ASN.FILE_ID) as FILE_ID,"
                        + "ARCHIVE_ASN.ASN_NUMBER as ASN_NUMBER,ARCHIVE_ASN.PO_NUMBER as PO_NUMBER,"
                        + "ARCHIVE_ASN.BOL_NUMBER as BOL_NUMBER,ARCHIVE_FILES.SENDER_ID,ARCHIVE_FILES.RECEIVER_ID,ARCHIVE_ASN.ISA_NUMBER as ISA_NUMBER,ARCHIVE_ASN.SHIP_DATE as SHIP_DATE, "
                        + "ARCHIVE_FILES.GS_CONTROL_NUMBER as GS_CONTROL_NUMBER,"
                        + " ARCHIVE_FILES.ST_CONTROL_NUMBER as ST_CONTROL_NUMBER, ARCHIVE_FILES.DIRECTION as DIRECTION,"
                        + " ARCHIVE_FILES.STATUS as STATUS, ARCHIVE_FILES.DATE_TIME_RECEIVED as DATE_TIME_RECEIVED ,"
                        + "ARCHIVE_FILES.ACK_STATUS as ACK_STATUS,ARCHIVE_FILES.REPROCESSSTATUS"
                        + " FROM ARCHIVE_ASN LEFT OUTER JOIN ARCHIVE_FILES "
                        + "ON (ARCHIVE_ASN.ASN_NUMBER = ARCHIVE_FILES.PRI_KEY_VAL AND ARCHIVE_ASN.FILE_ID = ARCHIVE_FILES.FILE_ID) ");
                       
                shipmentSearchQuery.append(" WHERE 1=1 AND FLOWFLAG like 'M' ");
                if (datePickerFrom != null && !"".equals(datePickerFrom)) {
                    tmp_Recieved_From = DateUtility.getInstance().DateViewToDBCompare(datePickerFrom);
                    shipmentSearchQuery.append(" AND ARCHIVE_FILES.DATE_TIME_RECEIVED >= '" + tmp_Recieved_From + "'");
                }
                if (datepickerTo != null && !"".equals(datepickerTo)) {
                    tmp_Recieved_ToTime = DateUtility.getInstance().DateViewToDBCompare(datepickerTo);
                    shipmentSearchQuery.append(" AND ARCHIVE_FILES.DATE_TIME_RECEIVED <= '" + tmp_Recieved_ToTime + "'");
                }
                if (((corrattribute != null) && (corrattribute.equalsIgnoreCase("Shipment Number"))) && (corrvalue != null && !"".equals(corrvalue.trim()))) {
                    shipmentSearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_FILES.PRI_KEY_VAL", corrvalue.trim().toUpperCase()));
                }
                if (((corrattribute1 != null) && (corrattribute1.equalsIgnoreCase("Shipment Number"))) && (corrvalue1 != null && !"".equals(corrvalue1.trim()))) {
                    shipmentSearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_FILES.PRI_KEY_VAL", corrvalue1.trim().toUpperCase()));
                }
                if (((corrattribute != null) && (corrattribute.equalsIgnoreCase("BOL Number"))) && (corrvalue != null && !"".equals(corrvalue.trim()))) {
                    shipmentSearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_ASN.BOL_NUMBER", corrvalue.trim().toUpperCase()));
                }
                if (((corrattribute1 != null) && (corrattribute1.equalsIgnoreCase("BOL Number"))) && (corrvalue1 != null && !"".equals(corrvalue1.trim()))) {
                    shipmentSearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_ASN.BOL_NUMBER", corrvalue1.trim().toUpperCase()));
                }
                if (((corrattribute != null) && (corrattribute.equalsIgnoreCase("PO Number"))) && (corrvalue != null && !"".equals(corrvalue.trim()))) {
                    shipmentSearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_ASN.PO_NUMBER", corrvalue.trim().toUpperCase()));
                }
                if (((corrattribute1 != null) && (corrattribute1.equalsIgnoreCase("PO Number"))) && (corrvalue1 != null && !"".equals(corrvalue1.trim()))) {
                    shipmentSearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_ASN.PO_NUMBER", corrvalue1.trim().toUpperCase()));
                }
                if (((corrattribute != null) && (corrattribute.equalsIgnoreCase("Instance Id"))) && (corrvalue != null && !"".equals(corrvalue.trim()))) {
                    shipmentSearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_FILES.FILE_ID", corrvalue.trim().toUpperCase()));
                }
                if (((corrattribute1 != null) && (corrattribute1.equalsIgnoreCase("Instance Id"))) && (corrvalue1 != null && !"".equals(corrvalue1.trim()))) {
                    shipmentSearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_FILES.FILE_ID", corrvalue1.trim().toUpperCase()));
                }
                if (((corrattribute != null) && (corrattribute.equalsIgnoreCase("Direction"))) && (corrvalue != null && !"".equals(corrvalue.trim()))) {
                    shipmentSearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_FILES.DIRECTION", corrvalue.trim().toUpperCase()));
                }
                if (((corrattribute1 != null) && (corrattribute1.equalsIgnoreCase("Direction"))) && (corrvalue1 != null && !"".equals(corrvalue1.trim()))) {
                    shipmentSearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_FILES.DIRECTION", corrvalue1.trim().toUpperCase()));
                }
                if (doctype != null && !"".equals(doctype.trim())) {
                    shipmentSearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_FILES.TRANSACTION_TYPE", doctype.trim()));
                }
                if (status != null && !"-1".equals(status.trim())) {
                    shipmentSearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_FILES.STATUS", status.trim()));
                }
                if (ackStatus != null && !"-1".equals(ackStatus.trim())) {
                    shipmentSearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_FILES.ACK_STATUS", ackStatus.trim()));
                }
                if (senderId != null && !"".equals(senderId.trim())) {
                    shipmentSearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_FILES.SENDER_ID", senderId.trim().toUpperCase()));
                }
                if (recId != null && !"".equals(recId.trim())) {
                    shipmentSearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_FILES.RECEIVER_ID", recId.trim().toUpperCase()));
                }
//                if (senderName != null && !"".equals(senderName.trim())) {
//                    shipmentSearchQuery.append(WildCardSql.getWildCardSql1("TP1.NAME", senderName.trim().toUpperCase()));
//                }
//                if (recName != null && !"".equals(recName.trim())) {
//                    shipmentSearchQuery.append(WildCardSql.getWildCardSql1("TP2.NAME", recName.trim().toUpperCase()));
//                }
                shipmentSearchQuery.append("order by DATE_TIME_RECEIVED DESC fetch first 50 rows only");
            } else {
                shipmentSearchQuery.append("SELECT DISTINCT(ASN.FILE_ID) as FILE_ID,"
                        + "ASN.ASN_NUMBER as ASN_NUMBER,ASN.PO_NUMBER as PO_NUMBER,FILES.SENDER_ID,FILES.RECEIVER_ID,"
                        + "ASN.BOL_NUMBER as BOL_NUMBER,ASN.ISA_NUMBER as ISA_NUMBER,ASN.SHIP_DATE as SHIP_DATE, "
                        + "FILES.GS_CONTROL_NUMBER as GS_CONTROL_NUMBER,"
                        + " FILES.ST_CONTROL_NUMBER as ST_CONTROL_NUMBER, FILES.DIRECTION as DIRECTION,"
                        + " FILES.STATUS as STATUS, FILES.DATE_TIME_RECEIVED as DATE_TIME_RECEIVED ,"
                        + "FILES.ACK_STATUS as ACK_STATUS,FILES.REPROCESSSTATUS"
                        + " FROM ASN LEFT OUTER JOIN FILES "
                        + "ON (ASN.ASN_NUMBER = FILES.PRI_KEY_VAL AND ASN.FILE_ID = FILES.FILE_ID) ");
                       
                shipmentSearchQuery.append(" WHERE 1=1 AND FLOWFLAG like 'M' ");
                if (datePickerFrom != null && !"".equals(datePickerFrom)) {
                    tmp_Recieved_From = DateUtility.getInstance().DateViewToDBCompare(datePickerFrom);
                    shipmentSearchQuery.append(" AND FILES.DATE_TIME_RECEIVED >= '" + tmp_Recieved_From + "'");
                }
                if (datepickerTo != null && !"".equals(datepickerTo)) {
                    tmp_Recieved_ToTime = DateUtility.getInstance().DateViewToDBCompare(datepickerTo);
                    shipmentSearchQuery.append(" AND FILES.DATE_TIME_RECEIVED <= '" + tmp_Recieved_ToTime + "'");
                }
                if (((corrattribute != null) && (corrattribute.equalsIgnoreCase("Shipment Number"))) && (corrvalue != null && !"".equals(corrvalue.trim()))) {
                    shipmentSearchQuery.append(WildCardSql.getWildCardSql1("FILES.PRI_KEY_VAL", corrvalue.trim().toUpperCase()));
                }
                if (((corrattribute1 != null) && (corrattribute1.equalsIgnoreCase("Shipment Number"))) && (corrvalue1 != null && !"".equals(corrvalue1.trim()))) {
                    shipmentSearchQuery.append(WildCardSql.getWildCardSql1("FILES.PRI_KEY_VAL", corrvalue1.trim().toUpperCase()));
                }
                if (((corrattribute != null) && (corrattribute.equalsIgnoreCase("BOL Number"))) && (corrvalue != null && !"".equals(corrvalue.trim()))) {
                    shipmentSearchQuery.append(WildCardSql.getWildCardSql1("ASN.BOL_NUMBER", corrvalue.trim().toUpperCase()));
                }
                if (((corrattribute1 != null) && (corrattribute1.equalsIgnoreCase("BOL Number"))) && (corrvalue1 != null && !"".equals(corrvalue1.trim()))) {
                    shipmentSearchQuery.append(WildCardSql.getWildCardSql1("ASN.BOL_NUMBER", corrvalue1.trim().toUpperCase()));
                }
                if (((corrattribute != null) && (corrattribute.equalsIgnoreCase("PO Number"))) && (corrvalue != null && !"".equals(corrvalue.trim()))) {
                    shipmentSearchQuery.append(WildCardSql.getWildCardSql1("ASN.PO_NUMBER", corrvalue.trim().toUpperCase()));
                }
                if (((corrattribute1 != null) && (corrattribute1.equalsIgnoreCase("PO Number"))) && (corrvalue1 != null && !"".equals(corrvalue1.trim()))) {
                    shipmentSearchQuery.append(WildCardSql.getWildCardSql1("ASN.PO_NUMBER", corrvalue1.trim().toUpperCase()));
                }
                if (((corrattribute != null) && (corrattribute.equalsIgnoreCase("Instance Id"))) && (corrvalue != null && !"".equals(corrvalue.trim()))) {
                    shipmentSearchQuery.append(WildCardSql.getWildCardSql1("FILES.FILE_ID", corrvalue.trim().toUpperCase()));
                }
                if (((corrattribute1 != null) && (corrattribute1.equalsIgnoreCase("Instance Id"))) && (corrvalue1 != null && !"".equals(corrvalue1.trim()))) {
                    shipmentSearchQuery.append(WildCardSql.getWildCardSql1("FILES.FILE_ID", corrvalue1.trim().toUpperCase()));
                }
                if (((corrattribute != null) && (corrattribute.equalsIgnoreCase("Direction"))) && (corrvalue != null && !"".equals(corrvalue.trim()))) {
                    shipmentSearchQuery.append(WildCardSql.getWildCardSql1("FILES.DIRECTION", corrvalue.trim().toUpperCase()));
                }
                if (((corrattribute1 != null) && (corrattribute1.equalsIgnoreCase("Direction"))) && (corrvalue1 != null && !"".equals(corrvalue1.trim()))) {
                    shipmentSearchQuery.append(WildCardSql.getWildCardSql1("FILES.DIRECTION", corrvalue1.trim().toUpperCase()));
                }
                if (doctype != null && !"".equals(doctype.trim())) {
                    shipmentSearchQuery.append(WildCardSql.getWildCardSql1("FILES.TRANSACTION_TYPE", doctype.trim()));
                }
                if (status != null && !"-1".equals(status.trim())) {
                    shipmentSearchQuery.append(WildCardSql.getWildCardSql1("FILES.STATUS", status.trim()));
                }
                if (ackStatus != null && !"-1".equals(ackStatus.trim())) {
                    shipmentSearchQuery.append(WildCardSql.getWildCardSql1("FILES.ACK_STATUS", ackStatus.trim()));
                }
                if (senderId != null && !"".equals(senderId.trim())) {
                    shipmentSearchQuery.append(WildCardSql.getWildCardSql1("FILES.SENDER_ID", senderId.trim().toUpperCase()));
                }
                if (recId != null && !"".equals(recId.trim())) {
                    shipmentSearchQuery.append(WildCardSql.getWildCardSql1("FILES.RECEIVER_ID", recId.trim().toUpperCase()));
                }
//                if (senderName != null && !"".equals(senderName.trim())) {
//                    shipmentSearchQuery.append(WildCardSql.getWildCardSql1("TP1.NAME", senderName.trim().toUpperCase()));
//                }
//                if (recName != null && !"".equals(recName.trim())) {
//                    shipmentSearchQuery.append(WildCardSql.getWildCardSql1("TP2.NAME", recName.trim().toUpperCase()));
//                }
                shipmentSearchQuery.append("order by DATE_TIME_RECEIVED DESC fetch first 50 rows only");
            }
            Map<String, String> tradingPartners = DataSourceDataProvider.getInstance().getAllTradingPartners();
            System.out.println("CommunicationProtocols query:"+shipmentSearchQuery.toString());
            String searchQuery = shipmentSearchQuery.toString();
            connection = ConnectionProvider.getInstance().getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(searchQuery);
            shipmentList = new ArrayList<ShipmentBean>();
            System.out.println("Query and resultset start time::"+DateUtility.getInstance().getCurrentDB2Timestamp());
            while (resultSet.next()) {
                ShipmentBean shipmentBean = new ShipmentBean();
                shipmentBean.setAsnNo(resultSet.getString("ASN_NUMBER"));
                shipmentBean.setPoNo(resultSet.getString("PO_NUMBER"));
                shipmentBean.setBolNo(resultSet.getString("BOL_NUMBER"));
                shipmentBean.setIsa(resultSet.getString("ISA_NUMBER"));
                shipmentBean.setShipmentDate(resultSet.getString("SHIP_DATE"));
                shipmentBean.setGsCtrl(resultSet.getString("GS_CONTROL_NUMBER"));
                shipmentBean.setStCtrl(resultSet.getString("ST_CONTROL_NUMBER"));
                String direction = resultSet.getString("DIRECTION");
                shipmentBean.setDirection(direction);
               if ((direction != null) && ("INBOUND".equalsIgnoreCase(direction))) {
                    String pname_Sender = "";
                    if (((resultSet.getString("SENDER_ID")) != null)
                            && (((tradingPartners.get(resultSet.getString("SENDER_ID")))) != null)) {
                        pname_Sender = (tradingPartners.get(resultSet.getString("SENDER_ID"))).toString();
                    } else {
                        pname_Sender = "_";
                    }
                    shipmentBean.setPname(pname_Sender);
                } else {
                    String pname_Reciever = "";
                    if (((resultSet.getString("RECEIVER_ID")) != null)
                            && (((tradingPartners.get(resultSet.getString("RECEIVER_ID")))) != null)) {
                        pname_Reciever = ((tradingPartners.get(resultSet.getString("RECEIVER_ID"))).toString());
                    } else {
                        pname_Reciever = "_";
                    }
                    shipmentBean.setPname(pname_Reciever);
                }
                shipmentBean.setStatus(resultSet.getString("STATUS"));
                shipmentBean.setDate_time_rec(resultSet.getTimestamp("DATE_TIME_RECEIVED"));
                shipmentBean.setFile_id(resultSet.getString("FILE_ID"));
                shipmentBean.setAckStatus(resultSet.getString("ACK_STATUS"));
                shipmentBean.setReProcessStatus(resultSet.getString("REPROCESSSTATUS"));
                shipmentList.add(shipmentBean);
            }
            System.out.println("Resultset end time::"+DateUtility.getInstance().getCurrentDB2Timestamp());
        } catch (SQLException sqlException) {
            LoggerUtility.log(logger, "SQLException occurred in buildshipmentSQuery method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
        } catch (Exception exception) {
            LoggerUtility.log(logger, "Exception occurred in buildshipmentSQuery method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
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
                LoggerUtility.log(logger, "Finally SQLException occurred in buildshipmentSQuery method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
            }
        }
        return shipmentList;
    }

    public ArrayList<ShipmentBean> getShipmentList() {
        return shipmentList;
    }

    public void setShipmentList(ArrayList<ShipmentBean> shipmentList) {
        this.shipmentList = shipmentList;
    }
}
