
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mss.ediscv.po;

import com.mss.ediscv.util.ConnectionProvider;
import com.mss.ediscv.util.DataSourceDataProvider;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import com.mss.ediscv.util.DateUtility;
import com.mss.ediscv.util.ServiceLocatorException;
import com.mss.ediscv.util.WildCardSql;
import com.mss.ediscv.util.LoggerUtility;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.ArrayList;
import java.util.Map;

/**
 * @author miracle
 */
public class PurchaseOrderServiceImpl implements PurchaseOrderService {

    private static Logger logger = LogManager.getLogger(PurchaseOrderServiceImpl.class.getName());
    String tmp_Recieved_From = "";
    String tmp_Recieved_ToTime = "";
    private ArrayList<PurchaseOrderBean> purchaseList;
    private PurchaseOrderBean purchaseBean;

    public ArrayList<PurchaseOrderBean> buildPurchaseQuery(PurchaseOrderAction purchaseOrderAction) throws ServiceLocatorException {

        String poRecId = "";
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            String poDateTo = purchaseOrderAction.getPoDateTo();
            String poDateFrom = purchaseOrderAction.getPoDateFrom();
            if (purchaseOrderAction.getPoRecId() != null && !purchaseOrderAction.getPoRecId().equals("-1")) {
                poRecId = purchaseOrderAction.getPoRecId();
            }
            String poRecName = "";
            if (purchaseOrderAction.getPoRecName() != null && !purchaseOrderAction.getPoRecName().equals("-1")) {
                poRecName = purchaseOrderAction.getPoRecName();
            }
            String poSenderId = "";
            if (purchaseOrderAction.getPoSenderId() != null && !purchaseOrderAction.getPoSenderId().equals("-1")) {
                poSenderId = purchaseOrderAction.getPoSenderId();
            }
            String poSenderName = "";
            if (purchaseOrderAction.getPoSenderId() != null && !purchaseOrderAction.getPoSenderName().equals("-1")) {
                poSenderName = purchaseOrderAction.getPoSenderName();
            }
            String ackStatus = purchaseOrderAction.getAckStatus();
            String status = purchaseOrderAction.getStatus();
            String corrattribute = purchaseOrderAction.getCorrattribute();
            String corrvalue = purchaseOrderAction.getCorrvalue();
            String corrattribute1 = purchaseOrderAction.getCorrattribute1();
            String corrvalue1 = purchaseOrderAction.getCorrvalue1();
            String corrattribute2 = purchaseOrderAction.getCorrattribute2();
            String corrvalue2 = purchaseOrderAction.getCorrvalue2();
            String doctype = "";
            if ((purchaseOrderAction.getDocType() != null) && (!purchaseOrderAction.getDocType().equals("-1"))) {
                doctype = purchaseOrderAction.getDocType();
            }
            StringBuilder purchaseSearchQuery = new StringBuilder();
            if ("ARCHIVE".equals(purchaseOrderAction.getDatabase())) {
                purchaseSearchQuery.append("SELECT DISTINCT(ARCHIVE_FILES.FILE_ID) as FILE_ID,ARCHIVE_PO.PO_NUMBER as PO_NUMBER,ARCHIVE_FILES.TRANSACTION_TYPE as TRANSACTION_TYPE,ARCHIVE_PO.SO_NUMBER as SO_NUMBER,"
                        + "ARCHIVE_PO.SAP_IDOC_NUMBER as SAP_IDOC_NUMBER,ARCHIVE_FILES.SENDER_ID,ARCHIVE_FILES.RECEIVER_ID,ARCHIVE_PO.ORDER_DATE as ORDER_DATE,ARCHIVE_PO.SHIP_DATE as SHIP_DATE,"
                        + "ARCHIVE_PO.ORDER_STATUS AS ORDER_STATUS,ARCHIVE_PO.ISA_CONTROL_NUMBER as ISA_CONTROL_NUMBER,ARCHIVE_PO.ITEM_QTY as ITEM_QTY,"
                        + "ARCHIVE_FILES.DIRECTION as DIRECTION,ARCHIVE_FILES.GS_CONTROL_NUMBER as GS_CONTROL_NUMBER,ARCHIVE_FILES.STATUS as STATUS ,ARCHIVE_FILES.ACK_STATUS as ACK_STATUS,"
                        + "ARCHIVE_FILES.DATE_TIME_RECEIVED as DATE_TIME_RECEIVED,ARCHIVE_FILES.REPROCESSSTATUS "
                        + "FROM ARCHIVE_PO "
                        + "LEFT OUTER JOIN ARCHIVE_FILES ON (ARCHIVE_PO.PO_NUMBER=ARCHIVE_FILES.PRI_KEY_VAL AND ARCHIVE_PO.FILE_ID = ARCHIVE_FILES.FILE_ID)");
                        
                purchaseSearchQuery.append(" WHERE 1=1 AND ARCHIVE_FILES.FLOWFLAG like 'M' ");
                if (((corrattribute != null) && (corrattribute.equalsIgnoreCase("PO Number"))) && (corrvalue != null && !"".equals(corrvalue.trim()))) {
                    purchaseSearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_FILES.PRI_KEY_VAL", corrvalue.trim().toUpperCase()));
                }
                if (((corrattribute1 != null) && (corrattribute1.equalsIgnoreCase("PO Number"))) && (corrvalue1 != null && !"".equals(corrvalue1.trim()))) {
                    purchaseSearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_FILES.PRI_KEY_VAL", corrvalue1.trim().toUpperCase()));
                }
                if (((corrattribute2 != null) && (corrattribute2.equalsIgnoreCase("PO Number"))) && (corrvalue2 != null && !"".equals(corrvalue2.trim()))) {
                    purchaseSearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_FILES.PRI_KEY_VAL", corrvalue2.trim().toUpperCase()));
                }
                if (((corrattribute != null) && (corrattribute.equalsIgnoreCase("ISA Number"))) && (corrvalue != null && !"".equals(corrvalue.trim()))) {
                    purchaseSearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_FILES.ISA_Number", corrvalue.trim().toUpperCase()));
                }
                if (((corrattribute1 != null) && (corrattribute1.equalsIgnoreCase("ISA Number"))) && (corrvalue1 != null && !"".equals(corrvalue1.trim()))) {
                    purchaseSearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_FILES.ISA_Number", corrvalue1.trim().toUpperCase()));
                }
                if (((corrattribute2 != null) && (corrattribute2.equalsIgnoreCase("ISA Number"))) && (corrvalue2 != null && !"".equals(corrvalue2.trim()))) {
                    purchaseSearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_FILES.ISA_Number", corrvalue2.trim().toUpperCase()));
                }
                if (((corrattribute != null) && (corrattribute.equalsIgnoreCase("GS Number"))) && (corrvalue != null && !"".equals(corrvalue.trim()))) {
                    purchaseSearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_FILES.GS_CONTROL_NUMBER", corrvalue.trim().toUpperCase()));
                }
                if (((corrattribute1 != null) && corrattribute1.equalsIgnoreCase("GS Number")) && (corrvalue1 != null && !"".equals(corrvalue1.trim()))) {
                    purchaseSearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_FILES.GS_CONTROL_NUMBER", corrvalue1.trim().toUpperCase()));
                }
                if (((corrattribute2 != null) && (corrattribute2.equalsIgnoreCase("GS Number"))) && (corrvalue2 != null && !"".equals(corrvalue2.trim()))) {
                    purchaseSearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_FILES.GS_CONTROL_NUMBER", corrvalue2.trim().toUpperCase()));
                }

                if (((corrattribute != null) && (corrattribute.equalsIgnoreCase("Instance Id"))) && (corrvalue != null && !"".equals(corrvalue.trim()))) {
                    purchaseSearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_FILES.FILE_ID", corrvalue.trim().toUpperCase()));
                }
                if (((corrattribute1 != null) && (corrattribute1.equalsIgnoreCase("Instance Id"))) && (corrvalue1 != null && !"".equals(corrvalue1.trim()))) {
                    purchaseSearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_FILES.FILE_ID", corrvalue1.trim().toUpperCase()));
                }
                if (((corrattribute2 != null) && (corrattribute2.equalsIgnoreCase("Instance Id"))) && (corrvalue2 != null && !"".equals(corrvalue2.trim()))) {
                    purchaseSearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_FILES.FILE_ID", corrvalue2.trim().toUpperCase()));
                }
                if (((corrattribute != null) && (corrattribute.equalsIgnoreCase("Direction"))) && (corrvalue != null && !"".equals(corrvalue.trim()))) {
                    purchaseSearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_FILES.DIRECTION", corrvalue.trim().toUpperCase()));
                }
                if (((corrattribute1 != null) && (corrattribute1.equalsIgnoreCase("Direction"))) && (corrvalue1 != null && !"".equals(corrvalue1.trim()))) {
                    purchaseSearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_FILES.DIRECTION", corrvalue1.trim().toUpperCase()));
                }
                if (((corrattribute2 != null) && (corrattribute2.equalsIgnoreCase("Direction"))) && (corrvalue2 != null && !"".equals(corrvalue2.trim()))) {
                    purchaseSearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_FILES.DIRECTION", corrvalue2.trim().toUpperCase()));
                }
                if (status != null && !"-1".equals(status.trim())) {
                    purchaseSearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_FILES.STATUS", status.trim()));
                }
                if (ackStatus != null && !"-1".equals(ackStatus.trim())) {
                    purchaseSearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_FILES.ACK_STATUS", ackStatus.trim()));
                }
                if (doctype != null && !"".equals(doctype.trim())) {
                    purchaseSearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_FILES.TRANSACTION_TYPE", doctype.trim()));
                }
                if (poDateFrom != null && !"".equals(poDateFrom)) {
                    tmp_Recieved_From = DateUtility.getInstance().DateViewToDBCompare(poDateFrom);
                    purchaseSearchQuery.append(" AND ARCHIVE_FILES.DATE_TIME_RECEIVED >= '" + tmp_Recieved_From + "'");
                }
                if (poDateTo != null && !"".equals(poDateTo)) {
                    tmp_Recieved_ToTime = DateUtility.getInstance().DateViewToDBCompare(poDateTo);
                    purchaseSearchQuery.append(" AND ARCHIVE_FILES.DATE_TIME_RECEIVED <= '" + tmp_Recieved_ToTime + "'");
                }
                
                if (poSenderId != null && !"".equals(poSenderId.trim())) {
                    purchaseSearchQuery.append(WildCardSql.getWildCardSql1(" ARCHIVE_FILES.SENDER_ID", poSenderId.trim().toUpperCase()));
                }
                if (poRecId != null && !"".equals(poRecId.trim())) {
                    purchaseSearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_FILES.RECEIVER_ID", poRecId.trim().toUpperCase()));
                }
                if (poSenderName != null && !"".equals(poSenderName.trim())) {
                    purchaseSearchQuery.append(WildCardSql.getWildCardSql1("TP1.NAME", poSenderName.trim().toUpperCase()));
                }
                if (poRecName != null && !"".equals(poRecName.trim())) {
                    purchaseSearchQuery.append(WildCardSql.getWildCardSql1("TP2.NAME", poRecName.trim().toUpperCase()));
                }
                purchaseSearchQuery.append("order by DATE_TIME_RECEIVED DESC fetch first 50 rows only");
            } else {
                purchaseSearchQuery.append("SELECT DISTINCT(FILES.FILE_ID) as FILE_ID,PO.PO_NUMBER as PO_NUMBER,FILES.TRANSACTION_TYPE as TRANSACTION_TYPE,PO.SO_NUMBER as SO_NUMBER,"
                        + "PO.SAP_IDOC_NUMBER as SAP_IDOC_NUMBER,FILES.SENDER_ID,FILES.RECEIVER_ID,PO.ORDER_DATE as ORDER_DATE,PO.SHIP_DATE as SHIP_DATE,"
                        + "PO.ORDER_STATUS AS ORDER_STATUS,PO.ISA_CONTROL_NUMBER as ISA_CONTROL_NUMBER,PO.ITEM_QTY as ITEM_QTY,"
                        + "FILES.DIRECTION as DIRECTION,FILES.GS_CONTROL_NUMBER as GS_CONTROL_NUMBER,FILES.STATUS as STATUS ,FILES.ACK_STATUS as ACK_STATUS ,"
                        + "FILES.DATE_TIME_RECEIVED as DATE_TIME_RECEIVED,FILES.REPROCESSSTATUS "
                        + "FROM PO "
                        + "LEFT OUTER JOIN FILES ON (PO.PO_NUMBER=FILES.PRI_KEY_VAL AND PO.FILE_ID = FILES.FILE_ID)");
                        
                purchaseSearchQuery.append(" WHERE 1=1 AND FILES.FLOWFLAG like 'M' ");
                if (((corrattribute != null) && (corrattribute.equalsIgnoreCase("PO Number"))) && (corrvalue != null && !"".equals(corrvalue.trim()))) {
                    purchaseSearchQuery.append(WildCardSql.getWildCardSql1("FILES.PRI_KEY_VAL", corrvalue.trim().toUpperCase()));
                }
                if (((corrattribute1 != null) && (corrattribute1.equalsIgnoreCase("PO Number"))) && (corrvalue1 != null && !"".equals(corrvalue1.trim()))) {
                    purchaseSearchQuery.append(WildCardSql.getWildCardSql1("FILES.PRI_KEY_VAL", corrvalue1.trim().toUpperCase()));
                }
                if (((corrattribute2 != null) && (corrattribute2.equalsIgnoreCase("PO Number"))) && (corrvalue2 != null && !"".equals(corrvalue2.trim()))) {
                    purchaseSearchQuery.append(WildCardSql.getWildCardSql1("FILES.PRI_KEY_VAL", corrvalue2.trim().toUpperCase()));
                }
                if (((corrattribute != null) && (corrattribute.equalsIgnoreCase("ISA Number"))) && (corrvalue != null && !"".equals(corrvalue.trim()))) {
                    purchaseSearchQuery.append(WildCardSql.getWildCardSql1("FILES.ISA_Number", corrvalue.trim().toUpperCase()));
                }
                if (((corrattribute1 != null) && (corrattribute1.equalsIgnoreCase("ISA Number"))) && (corrvalue1 != null && !"".equals(corrvalue1.trim()))) {
                    purchaseSearchQuery.append(WildCardSql.getWildCardSql1("FILES.ISA_Number", corrvalue1.trim().toUpperCase()));
                }
                if (((corrattribute2 != null) && (corrattribute2.equalsIgnoreCase("ISA Number"))) && (corrvalue2 != null && !"".equals(corrvalue2.trim()))) {
                    purchaseSearchQuery.append(WildCardSql.getWildCardSql1("FILES.ISA_Number", corrvalue2.trim().toUpperCase()));
                }
                if (((corrattribute != null) && (corrattribute.equalsIgnoreCase("GS Number"))) && (corrvalue != null && !"".equals(corrvalue.trim()))) {
                    purchaseSearchQuery.append(WildCardSql.getWildCardSql1("FILES.GS_CONTROL_NUMBER", corrvalue.trim().toUpperCase()));
                }
                if (((corrattribute1 != null) && corrattribute1.equalsIgnoreCase("GS Number")) && (corrvalue1 != null && !"".equals(corrvalue1.trim()))) {
                    purchaseSearchQuery.append(WildCardSql.getWildCardSql1("FILES.GS_CONTROL_NUMBER", corrvalue1.trim().toUpperCase()));
                }
                if (((corrattribute2 != null) && (corrattribute2.equalsIgnoreCase("GS Number"))) && (corrvalue2 != null && !"".equals(corrvalue2.trim()))) {
                    purchaseSearchQuery.append(WildCardSql.getWildCardSql1("FILES.GS_CONTROL_NUMBER", corrvalue2.trim().toUpperCase()));
                }

                if (((corrattribute != null) && (corrattribute.equalsIgnoreCase("Instance Id"))) && (corrvalue != null && !"".equals(corrvalue.trim()))) {
                    purchaseSearchQuery.append(WildCardSql.getWildCardSql1("FILES.FILE_ID", corrvalue.trim().toUpperCase()));
                }
                if (((corrattribute1 != null) && (corrattribute1.equalsIgnoreCase("Instance Id"))) && (corrvalue1 != null && !"".equals(corrvalue1.trim()))) {
                    purchaseSearchQuery.append(WildCardSql.getWildCardSql1("FILES.FILE_ID", corrvalue1.trim().toUpperCase()));
                }
                if (((corrattribute2 != null) && (corrattribute2.equalsIgnoreCase("Instance Id"))) && (corrvalue2 != null && !"".equals(corrvalue2.trim()))) {
                    purchaseSearchQuery.append(WildCardSql.getWildCardSql1("FILES.FILE_ID", corrvalue2.trim().toUpperCase()));
                }
                if (((corrattribute != null) && (corrattribute.equalsIgnoreCase("Direction"))) && (corrvalue != null && !"".equals(corrvalue.trim()))) {
                    purchaseSearchQuery.append(WildCardSql.getWildCardSql1("FILES.DIRECTION", corrvalue.trim().toUpperCase()));
                }
                if (((corrattribute1 != null) && (corrattribute1.equalsIgnoreCase("Direction"))) && (corrvalue1 != null && !"".equals(corrvalue1.trim()))) {
                    purchaseSearchQuery.append(WildCardSql.getWildCardSql1("FILES.DIRECTION", corrvalue1.trim().toUpperCase()));
                }
                if (((corrattribute2 != null) && (corrattribute2.equalsIgnoreCase("Direction"))) && (corrvalue2 != null && !"".equals(corrvalue2.trim()))) {
                    purchaseSearchQuery.append(WildCardSql.getWildCardSql1("FILES.DIRECTION", corrvalue2.trim().toUpperCase()));
                }
                if (status != null && !"-1".equals(status.trim())) {
                    purchaseSearchQuery.append(WildCardSql.getWildCardSql1("FILES.STATUS", status.trim()));
                }
                if (ackStatus != null && !"-1".equals(ackStatus.trim())) {
                    purchaseSearchQuery.append(WildCardSql.getWildCardSql1("FILES.ACK_STATUS", ackStatus.trim()));
                }
                if (doctype != null && !"".equals(doctype.trim())) {
                    purchaseSearchQuery.append(WildCardSql.getWildCardSql1("FILES.TRANSACTION_TYPE", doctype.trim()));
                }
                if (poDateFrom != null && !"".equals(poDateFrom)) {
                    tmp_Recieved_From = DateUtility.getInstance().DateViewToDBCompare(poDateFrom);
                    purchaseSearchQuery.append(" AND FILES.DATE_TIME_RECEIVED >= '" + tmp_Recieved_From + "'");
                }
                if (poDateTo != null && !"".equals(poDateTo)) {
                    tmp_Recieved_ToTime = DateUtility.getInstance().DateViewToDBCompare(poDateTo);
                    purchaseSearchQuery.append(" AND FILES.DATE_TIME_RECEIVED <= '" + tmp_Recieved_ToTime + "'");
                }
                
                if (poSenderId != null && !"".equals(poSenderId.trim())) {
                    purchaseSearchQuery.append(WildCardSql.getWildCardSql1("FILES.SENDER_ID", poSenderId.trim().toUpperCase()));
                }
                if (poRecId != null && !"".equals(poRecId.trim())) {
                    purchaseSearchQuery.append(WildCardSql.getWildCardSql1("FILES.RECEIVER_ID", poRecId.trim().toUpperCase()));
                }
//                if (poSenderName != null && !"".equals(poSenderName.trim())) {
//                    purchaseSearchQuery.append(WildCardSql.getWildCardSql1("TP1.NAME", poSenderName.trim().toUpperCase()));
//                }
//                if (poRecName != null && !"".equals(poRecName.trim())) {
//                    purchaseSearchQuery.append(WildCardSql.getWildCardSql1("TP2.NAME", poRecName.trim().toUpperCase()));
//                }
                purchaseSearchQuery.append("order by DATE_TIME_RECEIVED DESC fetch first 50 rows only");
            }
            System.out.println("purchaseSearchQuery query:"+purchaseSearchQuery.toString());
            Map<String, String> tradingPartners = DataSourceDataProvider.getInstance().getAllTradingPartners();
            connection = ConnectionProvider.getInstance().getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(purchaseSearchQuery.toString());
            purchaseList = new ArrayList<PurchaseOrderBean>();
            System.out.println("Query and resultset start time::"+DateUtility.getInstance().getCurrentDB2Timestamp());
            while (resultSet.next()) {
                PurchaseOrderBean purchaseOrderBean = new PurchaseOrderBean();
                purchaseOrderBean.setFileId(resultSet.getString("FILE_ID"));
                purchaseOrderBean.setPo(resultSet.getString("PO_Number"));
                purchaseOrderBean.setTransactionType(resultSet.getString("TRANSACTION_TYPE"));
                purchaseOrderBean.setSo(resultSet.getString("SO_Number"));
                purchaseOrderBean.setSapIdoc(resultSet.getString("SAP_IDOC_Number"));
                purchaseOrderBean.setPoDate(resultSet.getString("Order_Date"));
                purchaseOrderBean.setShipDate(resultSet.getString("Ship_Date"));
                purchaseOrderBean.setPoStatus(resultSet.getString("Order_Status"));
                purchaseOrderBean.setIsaControl(resultSet.getString("ISA_Control_Number"));
                purchaseOrderBean.setItemQty(resultSet.getString("Item_Qty"));
                purchaseOrderBean.setStatus(resultSet.getString("STATUS"));
                String direction = resultSet.getString("DIRECTION");
                purchaseOrderBean.setDirection(direction);
                if ((direction != null) && ("INBOUND".equalsIgnoreCase(direction))) {
                    String pname_Sender = "";
                    if (((resultSet.getString("SENDER_ID")) != null)
                            && (((tradingPartners.get(resultSet.getString("SENDER_ID")))) != null)) {
                        pname_Sender = (tradingPartners.get(resultSet.getString("SENDER_ID"))).toString();
                    } else {
                        pname_Sender = "_";
                    }
                    purchaseOrderBean.setPname(pname_Sender);
                } else {
                    String pname_Reciever = "";
                    if (((resultSet.getString("RECEIVER_ID")) != null)
                            && (((tradingPartners.get(resultSet.getString("RECEIVER_ID")))) != null)) {
                        pname_Reciever = ((tradingPartners.get(resultSet.getString("RECEIVER_ID"))).toString());
                    } else {
                        pname_Reciever = "_";
                    }
                    purchaseOrderBean.setPname(pname_Reciever);
                }
                purchaseOrderBean.setGsControlNumber(resultSet.getString("GS_CONTROL_NUMBER"));
                purchaseOrderBean.setReProcessStatus(resultSet.getString("REPROCESSSTATUS"));
                purchaseOrderBean.setAckStatus(resultSet.getString("ACK_STATUS"));
                purchaseOrderBean.setDate_time_rec(resultSet.getTimestamp("DATE_TIME_RECEIVED"));
                purchaseList.add(purchaseOrderBean);
            }
            System.out.println("Resultset end time::"+DateUtility.getInstance().getCurrentDB2Timestamp());
        } catch (SQLException sqlException) {
            LoggerUtility.log(logger, "SQLException occurred in buildPurchaseQuery method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
        } catch (Exception exception) {
            LoggerUtility.log(logger, "Exception occurred in buildPurchaseQuery method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
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
                LoggerUtility.log(logger, "finally SQLException occurred in buildPurchaseQuery method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
            }
        }
        return purchaseList;
    }

    public PurchaseOrderBean getPurchaseBean() {
        return purchaseBean;
    }

    public void setPurchaseBean(PurchaseOrderBean purchaseBean) {
        this.purchaseBean = purchaseBean;
    }
}
