/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mss.ediscv.logisticsdoc;

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
public class LogisticsDocServiceImpl implements LogisticsDocService {

    private static Logger logger = LogManager.getLogger(LogisticsDocServiceImpl.class.getName());
    String tmp_Recieved_From = "";
    String tmp_Recieved_ToTime = "";
    int callableStatementUpdateCount;
    private ArrayList<LogisticsDocBean> documentList;
    private LogisticsDocBean logisticsBean;

    public ArrayList<LogisticsDocBean> buildDocumentQuery(LogisticsDocAction logisticsDocAction) throws ServiceLocatorException {

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
                documentSearchQuery.append("SELECT DISTINCT(ARCHIVE_FILES.FILE_ID) as FILE_ID,ARCHIVE_FILES.ID as ID,"
                        + "ARCHIVE_FILES.ISA_NUMBER as ISA_NUMBER,ARCHIVE_FILES.FILE_TYPE as FILE_TYPE,ARCHIVE_FILES.SENDER_ID,ARCHIVE_FILES.RECEIVER_ID,ARCHIVE_FILES.PRI_KEY_VAL as PRI_KEY_VAL,"
                        + "ARCHIVE_FILES.FILE_ORIGIN as FILE_ORIGIN,ARCHIVE_FILES.TRANSACTION_TYPE as TRANSACTION_TYPE,"
                        + "ARCHIVE_FILES.DIRECTION as DIRECTION,ARCHIVE_FILES.DATE_TIME_RECEIVED as DATE_TIME_RECEIVED,"
                        + "ARCHIVE_FILES.STATUS as STATUS,ARCHIVE_FILES.ACK_STATUS as ACK_STATUS,"
                        + "ARCHIVE_FILES.SEC_KEY_VAL,ARCHIVE_FILES.REPROCESSSTATUS,ARCHIVE_FILES.FILENAME "
                        + "FROM ARCHIVE_FILES LEFT OUTER JOIN ARCHIVE_Transport_loadtender ten on (ten.FILE_ID=ARCHIVE_FILES.FILE_ID and ten.SHIPMENT_ID=ARCHIVE_FILES.SEC_KEY_VAL) ");
                        
                documentSearchQuery.append(" WHERE 1=1 AND FLOWFLAG LIKE '%L%'");
                if (((corrattribute != null) && (corrattribute.equalsIgnoreCase("PO Number"))) && (corrvalue != null && !"".equals(corrvalue.trim()))) {
                    documentSearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_FILES.SEC_KEY_VAL", corrvalue.trim().toUpperCase()));
                }
                if (((corrattribute1 != null) && (corrattribute1.equalsIgnoreCase("PO Number"))) && (corrvalue1 != null && !"".equals(corrvalue1.trim()))) {
                    documentSearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_FILES.SEC_KEY_VAL", corrvalue1.trim().toUpperCase()));
                }
                if (((corrattribute2 != null) && (corrattribute2.equalsIgnoreCase("PO Number"))) && (corrvalue2 != null && !"".equals(corrvalue2.trim()))) {
                    documentSearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_FILES.SEC_KEY_VAL", corrvalue2.trim().toUpperCase()));
                }
                if (((corrattribute != null) && (corrattribute.equalsIgnoreCase("Ref Number"))) && (corrvalue != null && !"".equals(corrvalue.trim()))) {
                    documentSearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_FILES.SEC_KEY_VAL", corrvalue.trim().toUpperCase()));
                }
                if (((corrattribute1 != null) && (corrattribute1.equalsIgnoreCase("Ref Number"))) && (corrvalue1 != null && !"".equals(corrvalue1.trim()))) {
                    documentSearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_FILES.SEC_KEY_VAL", corrvalue1.trim().toUpperCase()));
                }
                if (((corrattribute2 != null) && (corrattribute2.equalsIgnoreCase("Ref Number"))) && (corrvalue2 != null && !"".equals(corrvalue2.trim()))) {
                    documentSearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_FILES.SEC_KEY_VAL", corrvalue2.trim().toUpperCase()));
                }
                if ((((corrattribute != null) && (corrattribute.equalsIgnoreCase("Invoice Number")) || ((corrattribute != null) && (corrattribute.equalsIgnoreCase("Shipment Number"))))) && (corrvalue != null && !"".equals(corrvalue.trim()))) {
                    documentSearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_FILES.PRI_KEY_VAL", corrvalue.trim().toUpperCase()));
                }
                if ((((corrattribute1 != null) && (corrattribute1.equalsIgnoreCase("Invoice Number")) || ((corrattribute1 != null) && corrattribute1.equalsIgnoreCase("Shipment Number")))) && (corrvalue1 != null && !"".equals(corrvalue1.trim()))) {
                    documentSearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_FILES.PRI_KEY_VAL", corrvalue1.trim().toUpperCase()));
                }
                if ((((corrattribute2 != null) && (corrattribute2.equalsIgnoreCase("Invoice Number"))) || ((corrattribute2 != null) && corrattribute2.equalsIgnoreCase("Shipment Number"))) && (corrvalue2 != null && !"".equals(corrvalue2.trim()))) {
                    documentSearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_FILES.PRI_KEY_VAL", corrvalue2.trim().toUpperCase()));
                }
                if (((corrattribute != null) && (corrattribute.equalsIgnoreCase("ISA Number"))) && (corrvalue != null && !"".equals(corrvalue.trim()))) {
                    documentSearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_FILES.ISA_Number", corrvalue.trim().toUpperCase()));
                }
                if (((corrattribute1 != null) && (corrattribute1.equalsIgnoreCase("ISA Number"))) && (corrvalue1 != null && !"".equals(corrvalue1.trim()))) {
                    documentSearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_FILES.ISA_Number", corrvalue1.trim().toUpperCase()));
                }
                if (((corrattribute2 != null) && (corrattribute2.equalsIgnoreCase("ISA Number"))) && (corrvalue2 != null && !"".equals(corrvalue2.trim()))) {
                    documentSearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_FILES.ISA_Number", corrvalue2.trim().toUpperCase()));
                }
                if (((corrattribute != null) && (corrattribute.equalsIgnoreCase("GS Number"))) && (corrvalue != null && !"".equals(corrvalue.trim()))) {
                    documentSearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_FILES.GS_CONTROL_Number", corrvalue.trim().toUpperCase()));
                }
                if (((corrattribute1 != null) && (corrattribute1.equalsIgnoreCase("GS Number"))) && (corrvalue1 != null && !"".equals(corrvalue1.trim()))) {
                    documentSearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_FILES.GS_CONTROL_Number", corrvalue1.trim().toUpperCase()));
                }
                if (((corrattribute2 != null) && (corrattribute2.equalsIgnoreCase("GS Number"))) && (corrvalue2 != null && !"".equals(corrvalue2.trim()))) {
                    documentSearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_FILES.GS_CONTROL_Number", corrvalue2.trim().toUpperCase()));
                }
                if (((corrattribute != null) && (corrattribute.equalsIgnoreCase("BOL Number"))) && (corrvalue != null && !"".equals(corrvalue.trim()))) {
                    documentSearchQuery.append(WildCardSql.getWildCardSql1("ten.BOL_NUMBER", corrvalue.trim().toUpperCase()));
                }
                if (((corrattribute1 != null) && (corrattribute1.equalsIgnoreCase("BOL Number"))) && (corrvalue1 != null && !"".equals(corrvalue1.trim()))) {
                    documentSearchQuery.append(WildCardSql.getWildCardSql1("ten.BOL_NUMBER", corrvalue1.trim().toUpperCase()));
                }
                if (((corrattribute2 != null) && (corrattribute2.equalsIgnoreCase("BOL Number"))) && (corrvalue2 != null && !"".equals(corrvalue2.trim()))) {
                    documentSearchQuery.append(WildCardSql.getWildCardSql1("ten.BOL_NUMBER", corrvalue2.trim().toUpperCase()));
                }
                if (((corrattribute != null) && (corrattribute.equalsIgnoreCase("CO Number"))) && (corrvalue != null && !"".equals(corrvalue.trim()))) {
                    documentSearchQuery.append(WildCardSql.getWildCardSql1("ten.CO_NUMBER", corrvalue.trim().toUpperCase()));
                }
                if (((corrattribute1 != null) && (corrattribute1.equalsIgnoreCase("CO Number"))) && (corrvalue1 != null && !"".equals(corrvalue1.trim()))) {
                    documentSearchQuery.append(WildCardSql.getWildCardSql1("ten.CO_NUMBER", corrvalue1.trim().toUpperCase()));
                }
                if (((corrattribute2 != null) && (corrattribute2.equalsIgnoreCase("CO Number"))) && (corrvalue2 != null && !"".equals(corrvalue2.trim()))) {
                    documentSearchQuery.append(WildCardSql.getWildCardSql1("ten.CO_NUMBER", corrvalue2.trim().toUpperCase()));
                }
                if (((corrattribute != null) && (corrattribute.equalsIgnoreCase("FILE NAME"))) && (corrvalue != null && !"".equals(corrvalue.trim()))) {
                    documentSearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_FILES.FILENAME", corrvalue.trim().toUpperCase()));
                }
                if (((corrattribute1 != null) && (corrattribute1.equalsIgnoreCase("FILE NAME"))) && (corrvalue1 != null && !"".equals(corrvalue1.trim()))) {
                    documentSearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_FILES.FILENAME", corrvalue1.trim().toUpperCase()));
                }
                if (((corrattribute2 != null) && (corrattribute2.equalsIgnoreCase("FILE NAME"))) && (corrvalue2 != null && !"".equals(corrvalue2.trim()))) {
                    documentSearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_FILES.FILENAME", corrvalue2.trim().toUpperCase()));
                }
                if (((corrattribute != null) && (corrattribute.equalsIgnoreCase("Instance Id"))) && (corrvalue != null && !"".equals(corrvalue.trim()))) {
                    documentSearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_FILES.FILE_ID", corrvalue.trim().toUpperCase()));
                }
                if (((corrattribute1 != null) && (corrattribute1.equalsIgnoreCase("Instance Id"))) && (corrvalue1 != null && !"".equals(corrvalue1.trim()))) {
                    documentSearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_FILES.FILE_ID", corrvalue1.trim().toUpperCase()));
                }
                if (((corrattribute2 != null) && (corrattribute2.equalsIgnoreCase("Instance Id"))) && (corrvalue2 != null && !"".equals(corrvalue2.trim()))) {
                    documentSearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_FILES.FILE_ID", corrvalue2.trim().toUpperCase()));
                }
                if (((corrattribute != null) && (corrattribute.equalsIgnoreCase("Direction"))) && (corrvalue != null && !"".equals(corrvalue.trim()))) {
                    documentSearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_FILES.DIRECTION", corrvalue.trim().toUpperCase()));
                }
                if (((corrattribute1 != null) && (corrattribute1.equalsIgnoreCase("Direction"))) && (corrvalue1 != null && !"".equals(corrvalue1.trim()))) {
                    documentSearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_FILES.DIRECTION", corrvalue1.trim().toUpperCase()));
                }
                if (((corrattribute2 != null) && (corrattribute2.equalsIgnoreCase("Direction"))) && (corrvalue2 != null && !"".equals(corrvalue2.trim()))) {
                    documentSearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_FILES.DIRECTION", corrvalue2.trim().toUpperCase()));
                }

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
//                    documentSearchQuery.append(WildCardSql.getWildCardSql1("TP1.NAME", docSenderName.trim().toUpperCase()));
//                }
//                if (docRecName != null && !"-1".equals(docRecName.trim())) {
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
                documentSearchQuery.append("SELECT DISTINCT(FILES.FILE_ID) as FILE_ID,FILES.ID as ID,"
                        + "FILES.ISA_NUMBER as ISA_NUMBER,FILES.FILE_TYPE as FILE_TYPE,FILES.PRI_KEY_VAL as PRI_KEY_VAL,"
                        + "FILES.FILE_ORIGIN as FILE_ORIGIN,FILES.SENDER_ID,FILES.RECEIVER_ID,FILES.TRANSACTION_TYPE as TRANSACTION_TYPE,"
                        + "FILES.DIRECTION as DIRECTION,FILES.DATE_TIME_RECEIVED as DATE_TIME_RECEIVED,"
                        + "FILES.STATUS as STATUS,FILES.ACK_STATUS as ACK_STATUS,"
                        + "FILES.SEC_KEY_VAL,FILES.REPROCESSSTATUS,FILES.FILENAME "
                        + "FROM FILES LEFT OUTER JOIN Transport_loadtender ten on (ten.FILE_ID=FILES.FILE_ID and ten.SHIPMENT_ID=FILES.SEC_KEY_VAL) ");
                documentSearchQuery.append(" WHERE 1=1 AND FLOWFLAG LIKE '%L%'");
                if (((corrattribute != null) && (corrattribute.equalsIgnoreCase("PO Number"))) && (corrvalue != null && !"".equals(corrvalue.trim()))) {
                    documentSearchQuery.append(WildCardSql.getWildCardSql1("FILES.SEC_KEY_VAL", corrvalue.trim().toUpperCase()));
                }
                if (((corrattribute1 != null) && (corrattribute1.equalsIgnoreCase("PO Number"))) && (corrvalue1 != null && !"".equals(corrvalue1.trim()))) {
                    documentSearchQuery.append(WildCardSql.getWildCardSql1("FILES.SEC_KEY_VAL", corrvalue1.trim().toUpperCase()));
                }
                if (((corrattribute2 != null) && (corrattribute2.equalsIgnoreCase("PO Number"))) && (corrvalue2 != null && !"".equals(corrvalue2.trim()))) {
                    documentSearchQuery.append(WildCardSql.getWildCardSql1("FILES.SEC_KEY_VAL", corrvalue2.trim().toUpperCase()));
                }
                if (((corrattribute != null) && (corrattribute.equalsIgnoreCase("Ref Number"))) && (corrvalue != null && !"".equals(corrvalue.trim()))) {
                    documentSearchQuery.append(WildCardSql.getWildCardSql1("FILES.SEC_KEY_VAL", corrvalue.trim().toUpperCase()));
                }
                if (((corrattribute1 != null) && (corrattribute1.equalsIgnoreCase("Ref Number"))) && (corrvalue1 != null && !"".equals(corrvalue1.trim()))) {
                    documentSearchQuery.append(WildCardSql.getWildCardSql1("FILES.SEC_KEY_VAL", corrvalue1.trim().toUpperCase()));
                }
                if (((corrattribute2 != null) && (corrattribute2.equalsIgnoreCase("Ref Number"))) && (corrvalue2 != null && !"".equals(corrvalue2.trim()))) {
                    documentSearchQuery.append(WildCardSql.getWildCardSql1("FILES.SEC_KEY_VAL", corrvalue2.trim().toUpperCase()));
                }
                if ((((corrattribute != null) && (corrattribute.equalsIgnoreCase("Invoice Number")) || ((corrattribute != null) && (corrattribute.equalsIgnoreCase("Shipment Number"))))) && (corrvalue != null && !"".equals(corrvalue.trim()))) {
                    documentSearchQuery.append(WildCardSql.getWildCardSql1("FILES.PRI_KEY_VAL", corrvalue.trim().toUpperCase()));
                }
                if ((((corrattribute1 != null) && (corrattribute1.equalsIgnoreCase("Invoice Number")) || ((corrattribute1 != null) && corrattribute1.equalsIgnoreCase("Shipment Number")))) && (corrvalue1 != null && !"".equals(corrvalue1.trim()))) {
                    documentSearchQuery.append(WildCardSql.getWildCardSql1("FILES.PRI_KEY_VAL", corrvalue1.trim().toUpperCase()));
                }
                if ((((corrattribute2 != null) && (corrattribute2.equalsIgnoreCase("Invoice Number"))) || ((corrattribute2 != null) && corrattribute2.equalsIgnoreCase("Shipment Number"))) && (corrvalue2 != null && !"".equals(corrvalue2.trim()))) {
                    documentSearchQuery.append(WildCardSql.getWildCardSql1("FILES.PRI_KEY_VAL", corrvalue2.trim().toUpperCase()));
                }
                if (((corrattribute != null) && (corrattribute.equalsIgnoreCase("ISA Number"))) && (corrvalue != null && !"".equals(corrvalue.trim()))) {
                    documentSearchQuery.append(WildCardSql.getWildCardSql1("FILES.ISA_Number", corrvalue.trim().toUpperCase()));
                }
                if (((corrattribute1 != null) && (corrattribute1.equalsIgnoreCase("ISA Number"))) && (corrvalue1 != null && !"".equals(corrvalue1.trim()))) {
                    documentSearchQuery.append(WildCardSql.getWildCardSql1("FILES.ISA_Number", corrvalue1.trim().toUpperCase()));
                }
                if (((corrattribute2 != null) && (corrattribute2.equalsIgnoreCase("ISA Number"))) && (corrvalue2 != null && !"".equals(corrvalue2.trim()))) {
                    documentSearchQuery.append(WildCardSql.getWildCardSql1("FILES.ISA_Number", corrvalue2.trim().toUpperCase()));
                }
                if (((corrattribute != null) && (corrattribute.equalsIgnoreCase("GS Number"))) && (corrvalue != null && !"".equals(corrvalue.trim()))) {
                    documentSearchQuery.append(WildCardSql.getWildCardSql1("FILES.GS_CONTROL_Number", corrvalue.trim().toUpperCase()));
                }
                if (((corrattribute1 != null) && (corrattribute1.equalsIgnoreCase("GS Number"))) && (corrvalue1 != null && !"".equals(corrvalue1.trim()))) {
                    documentSearchQuery.append(WildCardSql.getWildCardSql1("FILES.GS_CONTROL_Number", corrvalue1.trim().toUpperCase()));
                }
                if (((corrattribute2 != null) && (corrattribute2.equalsIgnoreCase("GS Number"))) && (corrvalue2 != null && !"".equals(corrvalue2.trim()))) {
                    documentSearchQuery.append(WildCardSql.getWildCardSql1("FILES.GS_CONTROL_Number", corrvalue2.trim().toUpperCase()));
                }
                if (((corrattribute != null) && (corrattribute.equalsIgnoreCase("BOL Number"))) && (corrvalue != null && !"".equals(corrvalue.trim()))) {
                    documentSearchQuery.append(WildCardSql.getWildCardSql1("ten.BOL_NUMBER", corrvalue.trim().toUpperCase()));
                }
                if (((corrattribute1 != null) && (corrattribute1.equalsIgnoreCase("BOL Number"))) && (corrvalue1 != null && !"".equals(corrvalue1.trim()))) {
                    documentSearchQuery.append(WildCardSql.getWildCardSql1("ten.BOL_NUMBER", corrvalue1.trim().toUpperCase()));
                }
                if (((corrattribute2 != null) && (corrattribute2.equalsIgnoreCase("BOL Number"))) && (corrvalue2 != null && !"".equals(corrvalue2.trim()))) {
                    documentSearchQuery.append(WildCardSql.getWildCardSql1("ten.BOL_NUMBER", corrvalue2.trim().toUpperCase()));
                }
                if (((corrattribute != null) && (corrattribute.equalsIgnoreCase("CO Number"))) && (corrvalue != null && !"".equals(corrvalue.trim()))) {
                    documentSearchQuery.append(WildCardSql.getWildCardSql1("ten.CO_NUMBER", corrvalue.trim().toUpperCase()));
                }
                if (((corrattribute1 != null) && (corrattribute1.equalsIgnoreCase("CO Number"))) && (corrvalue1 != null && !"".equals(corrvalue1.trim()))) {
                    documentSearchQuery.append(WildCardSql.getWildCardSql1("ten.CO_NUMBER", corrvalue1.trim().toUpperCase()));
                }
                if (((corrattribute2 != null) && (corrattribute2.equalsIgnoreCase("CO Number"))) && (corrvalue2 != null && !"".equals(corrvalue2.trim()))) {
                    documentSearchQuery.append(WildCardSql.getWildCardSql1("ten.CO_NUMBER", corrvalue2.trim().toUpperCase()));
                }
                if (((corrattribute != null) && (corrattribute.equalsIgnoreCase("FILE NAME"))) && (corrvalue != null && !"".equals(corrvalue.trim()))) {
                    documentSearchQuery.append(WildCardSql.getWildCardSql1("FILES.FILENAME", corrvalue.trim().toUpperCase()));
                }
                if (((corrattribute1 != null) && (corrattribute1.equalsIgnoreCase("FILE NAME"))) && (corrvalue1 != null && !"".equals(corrvalue1.trim()))) {
                    documentSearchQuery.append(WildCardSql.getWildCardSql1("FILES.FILENAME", corrvalue1.trim().toUpperCase()));
                }
                if (((corrattribute2 != null) && (corrattribute2.equalsIgnoreCase("FILE NAME"))) && (corrvalue2 != null && !"".equals(corrvalue2.trim()))) {
                    documentSearchQuery.append(WildCardSql.getWildCardSql1("FILES.FILENAME", corrvalue2.trim().toUpperCase()));
                }

                if (((corrattribute != null) && (corrattribute.equalsIgnoreCase("Instance Id"))) && (corrvalue != null && !"".equals(corrvalue.trim()))) {
                    documentSearchQuery.append(WildCardSql.getWildCardSql1("FILES.FILE_ID", corrvalue.trim().toUpperCase()));
                }
                if (((corrattribute1 != null) && (corrattribute1.equalsIgnoreCase("Instance Id"))) && (corrvalue1 != null && !"".equals(corrvalue1.trim()))) {
                    documentSearchQuery.append(WildCardSql.getWildCardSql1("FILES.FILE_ID", corrvalue1.trim().toUpperCase()));
                }
                if (((corrattribute2 != null) && (corrattribute2.equalsIgnoreCase("Instance Id"))) && (corrvalue2 != null && !"".equals(corrvalue2.trim()))) {
                    documentSearchQuery.append(WildCardSql.getWildCardSql1("FILES.FILE_ID", corrvalue2.trim().toUpperCase()));
                }
                if (((corrattribute != null) && (corrattribute.equalsIgnoreCase("Direction"))) && (corrvalue != null && !"".equals(corrvalue.trim()))) {
                    documentSearchQuery.append(WildCardSql.getWildCardSql1("FILES.DIRECTION", corrvalue.trim().toUpperCase()));
                }
                if (((corrattribute1 != null) && (corrattribute1.equalsIgnoreCase("Direction"))) && (corrvalue1 != null && !"".equals(corrvalue1.trim()))) {
                    documentSearchQuery.append(WildCardSql.getWildCardSql1("FILES.DIRECTION", corrvalue1.trim().toUpperCase()));
                }
                if (((corrattribute2 != null) && (corrattribute2.equalsIgnoreCase("Direction"))) && (corrvalue2 != null && !"".equals(corrvalue2.trim()))) {
                    documentSearchQuery.append(WildCardSql.getWildCardSql1("FILES.DIRECTION", corrvalue2.trim().toUpperCase()));
                }
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
            System.out.println("buildDocumentQuery query:" + documentSearchQuery.toString());
            System.out.println("Before Connection::" + DateUtility.getInstance().getCurrentDB2Timestamp());
            connection = ConnectionProvider.getInstance().getConnection();
            System.out.println("After Connection::" + DateUtility.getInstance().getCurrentDB2Timestamp());
            Map<String, String> tradingPartners = DataSourceDataProvider.getInstance().getAllTradingPartners();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(documentSearchQuery.toString());
            documentList = new ArrayList<LogisticsDocBean>();
            System.out.println("Query and resultset start time::" + DateUtility.getInstance().getCurrentDB2Timestamp());
            while (resultSet.next()) {
                LogisticsDocBean logisticsdocBean = new LogisticsDocBean();
                logisticsdocBean.setId(resultSet.getInt("ID"));
                logisticsdocBean.setFile_id(resultSet.getString("FILE_ID"));
                logisticsdocBean.setFile_origin(resultSet.getString("FILE_ORIGIN"));
                logisticsdocBean.setFile_type(resultSet.getString("FILE_TYPE"));
                logisticsdocBean.setIsa_number(resultSet.getString("ISA_NUMBER"));
                logisticsdocBean.setTransaction_type(resultSet.getString("TRANSACTION_TYPE"));
                logisticsdocBean.setDirection(resultSet.getString("DIRECTION"));
                logisticsdocBean.setDate_time_rec(resultSet.getTimestamp("DATE_TIME_RECEIVED"));
                logisticsdocBean.setStatus(resultSet.getString("STATUS"));
                String pname_Reciever = "";
                if (((resultSet.getString("RECEIVER_ID")) != null)
                        && (((tradingPartners.get(resultSet.getString("RECEIVER_ID")))) != null)) {
                    pname_Reciever = ((tradingPartners.get(resultSet.getString("RECEIVER_ID"))).toString());
                } else {
                    pname_Reciever = "_";
                }
                logisticsdocBean.setPname(pname_Reciever);
                logisticsdocBean.setReProcessStatus(resultSet.getString("REPROCESSSTATUS"));
                logisticsdocBean.setAckStatus(resultSet.getString("ACK_STATUS"));
                logisticsdocBean.setFile_name(resultSet.getString("FILENAME"));
                logisticsdocBean.setShipmentId(resultSet.getString("PRI_KEY_VAL"));
                documentList.add(logisticsdocBean);
            }
            System.out.println("Resultset end time::" + DateUtility.getInstance().getCurrentDB2Timestamp());
        } catch (SQLException sqlException) {
            LoggerUtility.log(logger, "SQLException occurred in buildDocumentQuery method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
        } catch (Exception exception) {
            LoggerUtility.log(logger, "Exception occurred in buildDocumentQuery method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
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
                LoggerUtility.log(logger, "finally SQLException occurred in buildDocumentQuery method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
            }
        }
        return documentList;
    }

}
