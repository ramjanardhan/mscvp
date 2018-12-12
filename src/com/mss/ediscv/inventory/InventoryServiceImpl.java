package com.mss.ediscv.inventory;

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
public class InventoryServiceImpl implements InventoryService {

    private static Logger logger = LogManager.getLogger(InventoryServiceImpl.class.getName());
    String tmp_Recieved_From = "";
    String tmp_Recieved_ToTime = "";
    private ArrayList<InventoryBean> inventoryList;

    public ArrayList<InventoryBean> buildInventorySearchQuery(InventoryAction inventoryAction) throws ServiceLocatorException {
        String datepickerTo = inventoryAction.getDocdatepicker();
        String datePickerFrom = inventoryAction.getDocdatepickerfrom();
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        String senderId = "";
        try {
            if (inventoryAction.getSenderId() != null && !inventoryAction.getSenderId().equals("-1")) {
                senderId = inventoryAction.getSenderId();
            }
            String senderName = "";
            if (inventoryAction.getSenderName() != null && !inventoryAction.getSenderName().equals("-1")) {
                senderName = inventoryAction.getSenderName();
            }
            String recName = "";
            if (inventoryAction.getRecName() != null && !inventoryAction.getRecName().equals("-1")) {
                recName = inventoryAction.getRecName();
            }
            String recId = "";
            if (inventoryAction.getBuId() != null && !inventoryAction.getBuId().equals("-1")) {
                recId = inventoryAction.getBuId();
            }
            String status = inventoryAction.getStatus();
            String ackStatus = inventoryAction.getAckStatus();
            String corrattribute = inventoryAction.getCorrattribute();
            String corrvalue = inventoryAction.getCorrvalue();
            String corrattribute1 = inventoryAction.getCorrattribute1();
            String corrattribute2 = inventoryAction.getCorrattribute2();
            String corrvalue1 = inventoryAction.getCorrvalue1();
            String corrvalue2 = inventoryAction.getCorrvalue2();
            String doctype = "";
            if (inventoryAction.getDocType() != null && !inventoryAction.getDocType().equals("-1")) {
                doctype = inventoryAction.getDocType();
            }
            StringBuilder inventorySearchQuery = new StringBuilder();
            if ("ARCHIVE".equals(inventoryAction.getDatabase())) {
                inventorySearchQuery.append("select DISTINCT(ARCHIVE_INVENTORY.FILE_ID) as FILE_ID,ARCHIVE_INVENTORY.ID,ARCHIVE_FILES.SENDER_ID,ARCHIVE_FILES.RECEIVER_ID,ARCHIVE_FILES.ISA_NUMBER,ARCHIVE_FILES.GS_CONTROL_NUMBER,ARCHIVE_FILES.SENDER_ID,ARCHIVE_FILES.RECEIVER_ID,ARCHIVE_INVENTORY.REFERENCE_NUMBER,ARCHIVE_INVENTORY.REPORTING_DATE,"
                        + "ARCHIVE_FILES.DIRECTION,ARCHIVE_FILES.STATUS,ARCHIVE_INVENTORY.VENDOR_NAME,ARCHIVE_INVENTORY.VENDOR_LOCATION from ARCHIVE_INVENTORY JOIN "
                        + "ARCHIVE_FILES ON (ARCHIVE_FILES.FILE_ID=ARCHIVE_INVENTORY.FILE_ID) where 1=1 ");

                if (datePickerFrom != null && !"".equals(datePickerFrom)) {
                    tmp_Recieved_From = DateUtility.getInstance().DateViewToDBCompare(datePickerFrom);
                    inventorySearchQuery.append(" AND ARCHIVE_FILES.DATE_TIME_RECEIVED >= '" + tmp_Recieved_From + "'");
                }
                if (datepickerTo != null && !"".equals(datepickerTo)) {
                    tmp_Recieved_ToTime = DateUtility.getInstance().DateViewToDBCompare(datepickerTo);
                    inventorySearchQuery.append(" AND ARCHIVE_FILES.DATE_TIME_RECEIVED <= '" + tmp_Recieved_ToTime + "'");
                }
                if (((corrattribute != null) && (corrattribute.equalsIgnoreCase("REPORTING_PERIOD"))) && (corrvalue != null && !"".equals(corrvalue.trim()))) {
                    inventorySearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_INVENTORY.REFERENCE_NUMBER", corrvalue.trim().toUpperCase()));
                }
                if (((corrattribute1 != null) && (corrattribute1.equalsIgnoreCase("REPORTING_PERIOD"))) && (corrvalue1 != null && !"".equals(corrvalue1.trim()))) {
                    inventorySearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_INVENTORY.REFERENCE_NUMBER", corrvalue1.trim().toUpperCase()));
                }
                if (((corrattribute2 != null) && (corrattribute2.equalsIgnoreCase("REPORTING_PERIOD"))) && (corrvalue2 != null && !"".equals(corrvalue2.trim()))) {
                    inventorySearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_INVENTORY.REFERENCE_NUMBER", corrvalue2.trim().toUpperCase()));
                }
                if (((corrattribute1 != null) && (corrattribute1.equalsIgnoreCase("REPORTING_DATE"))) && (corrvalue1 != null && !"".equals(corrvalue1.trim()))) {
                    inventorySearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_INVENTORY.REPORTING_DATE", corrvalue1.trim().toUpperCase()));
                }
                if (((corrattribute != null) && (corrattribute.equalsIgnoreCase("REPORTING_DATE"))) && (corrvalue != null && !"".equals(corrvalue.trim()))) {
                    inventorySearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_INVENTORY.REPORTING_DATE", corrvalue.trim().toUpperCase()));
                }
                if (((corrattribute2 != null) && (corrattribute2.equalsIgnoreCase("REPORTING_DATE"))) && (corrvalue2 != null && !"".equals(corrvalue2.trim()))) {
                    inventorySearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_INVENTORY.REPORTING_DATE", corrvalue2.trim().toUpperCase()));
                }
                if (((corrattribute != null) && (corrattribute.equalsIgnoreCase("VENDOR_NAME"))) && (corrvalue != null && !"".equals(corrvalue.trim()))) {
                    inventorySearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_INVENTORY.VENDOR_NAME", corrvalue.trim().toUpperCase()));
                }
                if (((corrattribute1 != null) && (corrattribute1.equalsIgnoreCase("VENDOR_NAME"))) && (corrvalue1 != null && !"".equals(corrvalue1.trim()))) {
                    inventorySearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_INVENTORY.VENDOR_NAME", corrvalue.trim().toUpperCase()));
                }
                if (((corrattribute2 != null) && (corrattribute2.equalsIgnoreCase("VENDOR_NAME"))) && (corrvalue2 != null && !"".equals(corrvalue2.trim()))) {
                    inventorySearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_INVENTORY.VENDOR_NAME", corrvalue2.trim().toUpperCase()));
                }
                if (((corrattribute != null) && (corrattribute.equalsIgnoreCase("VENDOR_LOCATION"))) && (corrvalue != null && !"".equals(corrvalue.trim()))) {
                    inventorySearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_INVENTORY.VENDOR_LOCATION", corrvalue.trim().toUpperCase()));
                }
                if (((corrattribute1 != null) && (corrattribute1.equalsIgnoreCase("VENDOR_LOCATION"))) && (corrvalue1 != null && !"".equals(corrvalue1.trim()))) {
                    inventorySearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_INVENTORY.VENDOR_LOCATION", corrvalue1.trim().toUpperCase()));
                }
                if (((corrattribute2 != null) && (corrattribute2.equalsIgnoreCase("VENDOR_LOCATION"))) && (corrvalue2 != null && !"".equals(corrvalue2.trim()))) {
                    inventorySearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_INVENTORY.VENDOR_LOCATION", corrvalue2.trim().toUpperCase()));
                }
                if (((corrattribute != null) && (corrattribute.equalsIgnoreCase("INSTANCE_ID"))) && (corrvalue != null && !"".equals(corrvalue.trim()))) {
                    inventorySearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_INVENTORY.FILE_ID", corrvalue.trim().toUpperCase()));
                }
                if (((corrattribute1 != null) && (corrattribute1.equalsIgnoreCase("INSTANCE_ID"))) && (corrvalue1 != null && !"".equals(corrvalue1.trim()))) {
                    inventorySearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_INVENTORY.FILE_ID", corrvalue1.trim().toUpperCase()));
                }
                if (((corrattribute2 != null) && (corrattribute2.equalsIgnoreCase("INSTANCE_ID"))) && (corrvalue2 != null && !"".equals(corrvalue2.trim()))) {
                    inventorySearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_INVENTORY.FILE_ID", corrvalue2.trim().toUpperCase()));
                }
                if (doctype != null && !"".equals(doctype.trim())) {
                    inventorySearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_FILES.TRANSACTION_TYPE", doctype.trim()));
                }
                if (status != null && !"-1".equals(status.trim())) {
                    inventorySearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_FILES.STATUS", status.trim()));
                }
                if (ackStatus != null && !"-1".equals(ackStatus.trim())) {
                    inventorySearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_FILES.ACK_STATUS", ackStatus.trim()));
                }
                
                if (senderId != null && !"".equals(senderId.trim())) {
                    inventorySearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_FILES.SENDER_ID", senderId.trim().toUpperCase()));
                }
                if (recId != null && !"".equals(recId.trim())) {
                    inventorySearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_FILES.RECEIVER_ID", recId.trim().toUpperCase()));
                }
//                if (senderName != null && !"".equals(senderName.trim())) {
//                    inventorySearchQuery.append(WildCardSql.getWildCardSql1("TP1.NAME", senderName.trim().toUpperCase()));
//                }
//                if (recName != null && !"".equals(recName.trim())) {
//                    inventorySearchQuery.append(WildCardSql.getWildCardSql1("TP2.NAME", recName.trim().toUpperCase()));
//                }
                inventorySearchQuery.append(" fetch first 50 rows only");
              
            } else {
                inventorySearchQuery.append("select DISTINCT(FILES.FILE_ID) as FILE_ID,FILES.SENDER_ID,FILES.RECEIVER_ID,INVENTORY.ID,FILES.ISA_NUMBER,FILES.GS_CONTROL_NUMBER,FILES.SENDER_ID,FILES.RECEIVER_ID,INVENTORY.REFERENCE_NUMBER,INVENTORY.REPORTING_DATE,"
                        + "FILES.DIRECTION,FILES.STATUS,INVENTORY.VENDOR_NAME,INVENTORY.VENDOR_LOCATION from INVENTORY JOIN "
                        + "FILES ON (FILES.FILE_ID=INVENTORY.FILE_ID) where 1=1 ");
                if (datePickerFrom != null && !"".equals(datePickerFrom)) {
                    tmp_Recieved_From = DateUtility.getInstance().DateViewToDBCompare(datePickerFrom);
                    inventorySearchQuery.append(" AND FILES.DATE_TIME_RECEIVED >= '" + tmp_Recieved_From + "'");
                }
                if (datepickerTo != null && !"".equals(datepickerTo)) {
                    tmp_Recieved_ToTime = DateUtility.getInstance().DateViewToDBCompare(datepickerTo);
                    inventorySearchQuery.append(" AND FILES.DATE_TIME_RECEIVED <= '" + tmp_Recieved_ToTime + "'");
                }
                if (((corrattribute != null) && (corrattribute.equalsIgnoreCase("REPORTING_PERIOD"))) && (corrvalue != null && !"".equals(corrvalue.trim()))) {
                    inventorySearchQuery.append(WildCardSql.getWildCardSql1("INVENTORY.REFERENCE_NUMBER", corrvalue.trim().toUpperCase()));
                }
                if (((corrattribute1 != null) && (corrattribute1.equalsIgnoreCase("REPORTING_PERIOD"))) && (corrvalue1 != null && !"".equals(corrvalue1.trim()))) {
                    inventorySearchQuery.append(WildCardSql.getWildCardSql1("INVENTORY.REFERENCE_NUMBER", corrvalue1.trim().toUpperCase()));
                }
                if (((corrattribute2 != null) && (corrattribute2.equalsIgnoreCase("REPORTING_PERIOD"))) && (corrvalue2 != null && !"".equals(corrvalue2.trim()))) {
                    inventorySearchQuery.append(WildCardSql.getWildCardSql1("INVENTORY.REFERENCE_NUMBER", corrvalue2.trim().toUpperCase()));
                }
                if (((corrattribute1 != null) && (corrattribute1.equalsIgnoreCase("REPORTING_DATE"))) && (corrvalue1 != null && !"".equals(corrvalue1.trim()))) {
                    inventorySearchQuery.append(WildCardSql.getWildCardSql1("INVENTORY.REPORTING_DATE", corrvalue1.trim().toUpperCase()));
                }
                if (((corrattribute != null) && (corrattribute.equalsIgnoreCase("REPORTING_DATE"))) && (corrvalue != null && !"".equals(corrvalue.trim()))) {
                    inventorySearchQuery.append(WildCardSql.getWildCardSql1("INVENTORY.REPORTING_DATE", corrvalue.trim().toUpperCase()));
                }
                if (((corrattribute2 != null) && (corrattribute2.equalsIgnoreCase("REPORTING_DATE"))) && (corrvalue2 != null && !"".equals(corrvalue2.trim()))) {
                    inventorySearchQuery.append(WildCardSql.getWildCardSql1("INVENTORY.REPORTING_DATE", corrvalue2.trim().toUpperCase()));
                }
                if (((corrattribute != null) && (corrattribute.equalsIgnoreCase("VENDOR_NAME"))) && (corrvalue != null && !"".equals(corrvalue.trim()))) {
                    inventorySearchQuery.append(WildCardSql.getWildCardSql1("INVENTORY.VENDOR_NAME", corrvalue.trim().toUpperCase()));
                }
                if (((corrattribute1 != null) && (corrattribute1.equalsIgnoreCase("VENDOR_NAME"))) && (corrvalue1 != null && !"".equals(corrvalue1.trim()))) {
                    inventorySearchQuery.append(WildCardSql.getWildCardSql1("INVENTORY.VENDOR_NAME", corrvalue1.trim().toUpperCase()));
                }
                if (((corrattribute2 != null) && (corrattribute2.equalsIgnoreCase("VENDOR_NAME"))) && (corrvalue2 != null && !"".equals(corrvalue2.trim()))) {
                    inventorySearchQuery.append(WildCardSql.getWildCardSql1("INVENTORY.VENDOR_NAME", corrvalue2.trim().toUpperCase()));
                }
                if (((corrattribute != null) && (corrattribute.equalsIgnoreCase("VENDOR_LOCATION"))) && (corrvalue != null && !"".equals(corrvalue.trim()))) {
                    inventorySearchQuery.append(WildCardSql.getWildCardSql1("INVENTORY.VENDOR_LOCATION", corrvalue.trim().toUpperCase()));
                }
                if (((corrattribute1 != null) && (corrattribute1.equalsIgnoreCase("VENDOR_LOCATION"))) && (corrvalue1 != null && !"".equals(corrvalue1.trim()))) {
                    inventorySearchQuery.append(WildCardSql.getWildCardSql1("INVENTORY.VENDOR_LOCATION", corrvalue1.trim().toUpperCase()));
                }
                if (((corrattribute2 != null) && (corrattribute2.equalsIgnoreCase("VENDOR_LOCATION"))) && (corrvalue2 != null && !"".equals(corrvalue2.trim()))) {
                    inventorySearchQuery.append(WildCardSql.getWildCardSql1("INVENTORY.VENDOR_LOCATION", corrvalue2.trim().toUpperCase()));
                }
                if (((corrattribute != null) && (corrattribute.equalsIgnoreCase("INSTANCE_ID"))) && (corrvalue != null && !"".equals(corrvalue.trim()))) {
                    inventorySearchQuery.append(WildCardSql.getWildCardSql1("INVENTORY.FILE_ID", corrvalue.trim().toUpperCase()));
                }
                if (((corrattribute1 != null) && (corrattribute1.equalsIgnoreCase("INSTANCE_ID"))) && (corrvalue1 != null && !"".equals(corrvalue1.trim()))) {
                    inventorySearchQuery.append(WildCardSql.getWildCardSql1("INVENTORY.FILE_ID", corrvalue1.trim().toUpperCase()));
                }
                if (((corrattribute2 != null) && (corrattribute2.equalsIgnoreCase("INSTANCE_ID"))) && (corrvalue2 != null && !"".equals(corrvalue2.trim()))) {
                    inventorySearchQuery.append(WildCardSql.getWildCardSql1("INVENTORY.FILE_ID", corrvalue2.trim().toUpperCase()));
                }
                if (doctype != null && !"".equals(doctype.trim())) {
                    inventorySearchQuery.append(WildCardSql.getWildCardSql1("FILES.TRANSACTION_TYPE", doctype.trim()));
                }
                if (status != null && !"-1".equals(status.trim())) {
                    inventorySearchQuery.append(WildCardSql.getWildCardSql1("FILES.STATUS", status.trim()));
                }
                if (ackStatus != null && !"-1".equals(ackStatus.trim())) {
                    inventorySearchQuery.append(WildCardSql.getWildCardSql1("FILES.ACK_STATUS", ackStatus.trim()));
                }
                
                if (senderId != null && !"".equals(senderId.trim())) {
                    inventorySearchQuery.append(WildCardSql.getWildCardSql1("FILES.SENDER_ID", senderId.trim().toUpperCase()));
                }
                if (recId != null && !"".equals(recId.trim())) {
                    inventorySearchQuery.append(WildCardSql.getWildCardSql1("FILES.RECEIVER_ID", recId.trim().toUpperCase()));
                }
//                if (senderName != null && !"".equals(senderName.trim())) {
//                    inventorySearchQuery.append(WildCardSql.getWildCardSql1("TP1.NAME", senderName.trim().toUpperCase()));
//                }
//                if (recName != null && !"".equals(recName.trim())) {
//                    inventorySearchQuery.append(WildCardSql.getWildCardSql1("TP2.NAME", recName.trim().toUpperCase()));
//                }
                inventorySearchQuery.append(" fetch first 50 rows only");
            }
             System.out.println("inventorySearchQuery query:"+inventorySearchQuery.toString());
             Map<String, String> tradingPartners = DataSourceDataProvider.getInstance().getAllTradingPartners();
            connection = ConnectionProvider.getInstance().getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(inventorySearchQuery.toString());
            inventoryList = new ArrayList<InventoryBean>();
           System.out.println("Query and resultset start time::"+DateUtility.getInstance().getCurrentDB2Timestamp());
            while (resultSet.next()) {
                InventoryBean inventoryBean = new InventoryBean();
                inventoryBean.setInventory_id(resultSet.getInt("ID"));
                inventoryBean.setReportingPeriod(resultSet.getString("REFERENCE_NUMBER"));
                inventoryBean.setReportingDate(resultSet.getString("REPORTING_DATE"));
                inventoryBean.setVendorName(resultSet.getString("VENDOR_NAME"));
                inventoryBean.setVendorLocation(resultSet.getString("VENDOR_LOCATION"));
                inventoryBean.setIsaNum(resultSet.getString("ISA_NUMBER"));
                inventoryBean.setGsCtrl(resultSet.getString("GS_CONTROL_NUMBER"));
                String direction = resultSet.getString("DIRECTION");
                inventoryBean.setDirection(direction);
                 if ((direction != null) && ("INBOUND".equalsIgnoreCase(direction))) {
                    String pname_Sender = "";
                    if (((resultSet.getString("SENDER_ID")) != null)
                            && (((tradingPartners.get(resultSet.getString("SENDER_ID")))) != null)) {
                        pname_Sender = (tradingPartners.get(resultSet.getString("SENDER_ID"))).toString();
                    } else {
                        pname_Sender = "_";
                    }
                    inventoryBean.setPname(pname_Sender);
                } else {
                    String pname_Reciever = "";
                    if (((resultSet.getString("RECEIVER_ID")) != null)
                            && (((tradingPartners.get(resultSet.getString("RECEIVER_ID")))) != null)) {
                        pname_Reciever = ((tradingPartners.get(resultSet.getString("RECEIVER_ID"))).toString());
                    } else {
                        pname_Reciever = "_";
                    }
                    inventoryBean.setPname(pname_Reciever);
                }
                inventoryBean.setStatus(resultSet.getString("STATUS"));
                inventoryBean.setFile_id(resultSet.getString("FILE_ID"));
                inventoryList.add(inventoryBean);
            }
             System.out.println("Resultset end time::"+DateUtility.getInstance().getCurrentDB2Timestamp());
        } catch (SQLException sqlException) {
            LoggerUtility.log(logger, "SQLException occurred in getPoDetails method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
        } catch (Exception exception) {
            LoggerUtility.log(logger, "Exception occurred in buildInventorySearchQuery method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
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
                LoggerUtility.log(logger, "SQLException occurred in buildInventorySearchQuery method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
            }
        }
        return inventoryList;
    }

    public ArrayList<InventoryBean> getShipmentList() {
        return inventoryList;
    }

    public void setShipmentList(ArrayList<InventoryBean> inventoryList) {
        this.inventoryList = inventoryList;
    }
}
