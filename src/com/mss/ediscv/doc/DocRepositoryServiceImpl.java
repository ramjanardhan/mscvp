/**
 *
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package com.mss.ediscv.doc;

import com.mss.ediscv.util.ConnectionProvider;
import com.mss.ediscv.util.DataSourceDataProvider;
import com.mss.ediscv.util.DateUtility;
import com.mss.ediscv.util.ServiceLocatorException;
import com.mss.ediscv.util.WildCardSql;
import java.sql.*;
import java.util.ArrayList;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.mss.ediscv.util.LoggerUtility;
import java.util.Map;

public class DocRepositoryServiceImpl implements DocRepositoryService {

    String tmp_Recieved_From = "";
    String tmp_Recieved_ToTime = "";
    private ArrayList<DocRepositoryBean> documentList;
    private DocRepositoryBean docRepositoryBean;
    private static Logger logger = LogManager.getLogger(DocRepositoryServiceImpl.class.getName());

    public ArrayList<DocRepositoryBean> buildDocumentQuery(DocRepositoryAction docRepositoryAction) throws ServiceLocatorException {

        String docSenderId = "";
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            String docdatepicker = docRepositoryAction.getDocdatepicker();
            String docdatepickerfrom = docRepositoryAction.getDocdatepickerfrom();
            if ((docRepositoryAction.getDocSenderId() != null) && (!docRepositoryAction.getDocSenderId().equals("-1"))) {
                docSenderId = docRepositoryAction.getDocSenderId();
            }
            String docSenderName = "";
            if ((docRepositoryAction.getDocSenderName() != null) && (!docRepositoryAction.getDocSenderName().equals("-1"))) {
                docSenderName = docRepositoryAction.getDocSenderName();
            }
            String docBusId = "";
            if ((docRepositoryAction.getDocBusId() != null) && (!docRepositoryAction.getDocBusId().equals("-1"))) {
                docBusId = docRepositoryAction.getDocBusId();
            }
            String docRecName = "";
            if ((docRepositoryAction.getDocRecName() != null) && (!docRepositoryAction.getDocRecName().equals("-1"))) {
                docRecName = docRepositoryAction.getDocRecName();
            }
            String doctype = "";
            if ((docRepositoryAction.getDocType() != null) && (!docRepositoryAction.getDocType().equals("-1"))) {
                doctype = docRepositoryAction.getDocType();
            }
            String corrattribute = docRepositoryAction.getCorrattribute();
            String corrvalue = docRepositoryAction.getCorrvalue();
            String corrattribute1 = docRepositoryAction.getCorrattribute1();
            String corrvalue1 = docRepositoryAction.getCorrvalue1();
            String corrattribute2 = docRepositoryAction.getCorrattribute2();
            String corrvalue2 = docRepositoryAction.getCorrvalue2();
            String status = docRepositoryAction.getStatus();
            String ackStatus = docRepositoryAction.getAckStatus();
            StringBuilder documentSearchQuery = new StringBuilder();
            System.out.println("before query:" + DateUtility.getInstance().getCurrentDB2Timestamp());
            if ("ARCHIVE".equals(docRepositoryAction.getDatabase())) {
                documentSearchQuery.append("SELECT DISTINCT(ARCHIVE_FILES.FILE_ID) as FILE_ID,ARCHIVE_FILES.ID as ID,ARCHIVE_FILES.ISA_NUMBER as ISA_NUMBER,"
                        + " ARCHIVE_FILES.FILE_TYPE as FILE_TYPE,ARCHIVE_FILES.FILE_ORIGIN as FILE_ORIGIN, ARCHIVE_FILES.TRANSACTION_TYPE as TRANSACTION_TYPE,ARCHIVE_FILES.SENDER_ID,ARCHIVE_FILES.RECEIVER_ID,"
                        + " ARCHIVE_FILES.DIRECTION as DIRECTION, ARCHIVE_FILES.DATE_TIME_RECEIVED as DATE_TIME_RECEIVED,ARCHIVE_FILES.STATUS as STATUS,"
                        + " ARCHIVE_FILES.ACK_STATUS as ACK_STATUS,ARCHIVE_FILES.SEC_KEY_VAL,"
                        + " ARCHIVE_FILES.REPROCESSSTATUS,ARCHIVE_FILES.FILENAME,ARCHIVE_FILES.GS_CONTROL_NUMBER,ARCHIVE_FILES.PRI_KEY_VAL,ARCHIVE_ASN.BOL_NUMBER"
                        + " FROM ARCHIVE_FILES LEFT OUTER JOIN ARCHIVE_ASN ON (ARCHIVE_ASN.FILE_ID = ARCHIVE_FILES.FILE_ID)");
                documentSearchQuery.append(" WHERE 1=1 AND FLOWFLAG like 'M' ");
                if (((corrattribute != null) && (corrattribute.equalsIgnoreCase("PO Number"))) && (corrvalue != null && !"".equals(corrvalue.trim()))) {
                    documentSearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_FILES.SEC_KEY_VAL", corrvalue.trim().toUpperCase()));
                }
                if (((corrattribute1 != null) && (corrattribute1.equalsIgnoreCase("PO Number"))) && (corrvalue1 != null && !"".equals(corrvalue1.trim()))) {
                    documentSearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_FILES.SEC_KEY_VAL", corrvalue1.trim().toUpperCase()));
                }

                if (((corrattribute2 != null) && (corrattribute2.equalsIgnoreCase("PO Number"))) && (corrvalue2 != null && !"".equals(corrvalue2.trim()))) {
                    documentSearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_FILES.SEC_KEY_VAL", corrvalue2.trim().toUpperCase()));
                }
                if (((corrattribute != null) && (corrattribute.equalsIgnoreCase("Invoice Number") || corrattribute.equalsIgnoreCase("Shipment Number") || corrattribute.equalsIgnoreCase("Cheque Number"))) && (corrvalue != null && !"".equals(corrvalue.trim()))) {
                    documentSearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_FILES.PRI_KEY_VAL", corrvalue.trim().toUpperCase()));
                }
                if (((corrattribute1 != null) && (corrattribute1.equalsIgnoreCase("Invoice Number") || corrattribute1.equalsIgnoreCase("Shipment Number") || corrattribute1.equalsIgnoreCase("Cheque Number"))) && (corrvalue1 != null && !"".equals(corrvalue1.trim()))) {
                    documentSearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_FILES.PRI_KEY_VAL", corrvalue1.trim().toUpperCase()));
                }
                if (((corrattribute2 != null) && (corrattribute2.equalsIgnoreCase("Invoice Number") || corrattribute2.equalsIgnoreCase("Shipment Number") || corrattribute2.equalsIgnoreCase("Cheque Number"))) && (corrvalue2 != null && !"".equals(corrvalue2.trim()))) {
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
                if (((corrattribute != null) && (corrattribute.equalsIgnoreCase("BOL Number"))) && (corrvalue != null && !"".equals(corrvalue.trim()))) {
                    documentSearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_ASN.BOL_NUMBER", corrvalue.trim().toUpperCase()));
                }
                if (((corrattribute1 != null) && (corrattribute1.equalsIgnoreCase("BOL Number"))) && (corrvalue1 != null && !"".equals(corrvalue1.trim()))) {
                    documentSearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_ASN.BOL_NUMBER", corrvalue1.trim().toUpperCase()));
                }
                if (((corrattribute2 != null) && (corrattribute2.equalsIgnoreCase("BOL Number"))) && (corrvalue2 != null && !"".equals(corrvalue2.trim()))) {
                    documentSearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_ASN.BOL_NUMBER", corrvalue2.trim().toUpperCase()));
                }
                if (((corrattribute != null) && (corrattribute.equalsIgnoreCase("GS Number"))) && (corrvalue != null && !"".equals(corrvalue.trim()))) {
                    documentSearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_FILES.GS_CONTROL_NUMBER", corrvalue.trim().toUpperCase()));
                }
                if (((corrattribute1 != null) && (corrattribute1.equalsIgnoreCase("GS Number"))) && (corrvalue1 != null && !"".equals(corrvalue1.trim()))) {
                    documentSearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_FILES.GS_CONTROL_NUMBER", corrvalue1.trim().toUpperCase()));
                }
                if (((corrattribute != null) && (corrattribute2.equalsIgnoreCase("GS Number"))) && (corrvalue2 != null && !"".equals(corrvalue2.trim()))) {
                    documentSearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_FILES.GS_CONTROL_NUMBER", corrvalue2.trim().toUpperCase()));
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

                if ((doctype != null) && (!"".equals(doctype.trim()))) {
                    documentSearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_FILES.TRANSACTION_TYPE", doctype.trim()));
                }
                if ((status != null) && (!"-1".equals(status.trim()))) {
                    documentSearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_FILES.STATUS", status.trim()));
                }
                if ((ackStatus != null) && (!"-1".equals(ackStatus.trim()))) {
                    documentSearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_FILES.ACK_STATUS", ackStatus.trim()));
                }
                 if ((docBusId != null) && (!"".equals(docBusId.trim()))) {
                    documentSearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_FILES.RECEIVER_ID", docBusId.trim().toUpperCase()));
                }
                if ((docSenderId != null) && (!"".equals(docSenderId.trim()))) {
                    documentSearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_FILES.SENDER_ID", docSenderId.trim().toUpperCase()));
                }
//                if ((docSenderName != null) && (!"".equals(docSenderName.trim()))) {
//                    documentSearchQuery.append(WildCardSql.getWildCardSql1("TP1.NAME", docSenderName.trim().toUpperCase()));
//                }
//                if ((docRecName != null) && (!"".equals(docRecName.trim()))) {
//                    documentSearchQuery.append(WildCardSql.getWildCardSql1("TP2.NAME", docRecName.trim().toUpperCase()));
//                }
                if ((docdatepicker != null) && (!"".equals(docdatepicker))) {
                    tmp_Recieved_From = DateUtility.getInstance().DateViewToDBCompare(docdatepicker);
                    documentSearchQuery.append(" AND ARCHIVE_FILES.DATE_TIME_RECEIVED <= '" + tmp_Recieved_From + "'");
                }
                if ((docdatepickerfrom != null) && (!"".equals(docdatepickerfrom))) {
                    tmp_Recieved_From = DateUtility.getInstance().DateViewToDBCompare(docdatepickerfrom);
                    documentSearchQuery.append(" AND ARCHIVE_FILES.DATE_TIME_RECEIVED >= '" + tmp_Recieved_From + "'");
                }
                documentSearchQuery.append(" order by DATE_TIME_RECEIVED DESC fetch first 50 rows only");
            } else {
                documentSearchQuery.append("SELECT DISTINCT(FILES.FILE_ID) as FILE_ID,FILES.ID as ID,FILES.ISA_NUMBER as ISA_NUMBER,"
                        + "FILES.FILE_TYPE as FILE_TYPE,FILES.FILE_ORIGIN as FILE_ORIGIN,FILES.SENDER_ID,FILES.RECEIVER_ID,"
                        + "FILES.TRANSACTION_TYPE as TRANSACTION_TYPE,FILES.DIRECTION as DIRECTION,"
                        + "FILES.DATE_TIME_RECEIVED as DATE_TIME_RECEIVED,FILES.STATUS as STATUS,FILES.ACK_STATUS as ACK_STATUS,"
                        + "FILES.SEC_KEY_VAL,FILES.REPROCESSSTATUS"
                        + ",FILES.FILENAME,FILES.GS_CONTROL_NUMBER,FILES.PRI_KEY_VAL,ASN.BOL_NUMBER"//Added for metrie to get correlation column
                        + " FROM FILES "
                        + "LEFT OUTER JOIN ASN ON (ASN.FILE_ID = FILES.FILE_ID)");
                documentSearchQuery.append(" WHERE 1=1 AND FLOWFLAG like 'M' ");
                if (((corrattribute != null) && (corrattribute.equalsIgnoreCase("PO Number"))) && (corrvalue != null && !"".equals(corrvalue.trim()))) {
                    documentSearchQuery.append(WildCardSql.getWildCardSql1("FILES.SEC_KEY_VAL", corrvalue.trim().toUpperCase()));
                }
                if (((corrattribute1 != null) && (corrattribute1.equalsIgnoreCase("PO Number"))) && (corrvalue1 != null && !"".equals(corrvalue1.trim()))) {
                    documentSearchQuery.append(WildCardSql.getWildCardSql1("FILES.SEC_KEY_VAL", corrvalue1.trim().toUpperCase()));
                }

                if (((corrattribute2 != null) && (corrattribute2.equalsIgnoreCase("PO Number"))) && (corrvalue2 != null && !"".equals(corrvalue2.trim()))) {
                    documentSearchQuery.append(WildCardSql.getWildCardSql1("FILES.SEC_KEY_VAL", corrvalue2.trim().toUpperCase()));
                }
                if (((corrattribute != null) && (corrattribute.equalsIgnoreCase("Invoice Number") || corrattribute.equalsIgnoreCase("Shipment Number") || corrattribute.equalsIgnoreCase("Cheque Number"))) && (corrvalue != null && !"".equals(corrvalue.trim()))) {
                    documentSearchQuery.append(WildCardSql.getWildCardSql1("FILES.PRI_KEY_VAL", corrvalue.trim().toUpperCase()));
                }
                if (((corrattribute1 != null) && (corrattribute1.equalsIgnoreCase("Invoice Number") || corrattribute1.equalsIgnoreCase("Shipment Number") || corrattribute1.equalsIgnoreCase("Cheque Number"))) && (corrvalue1 != null && !"".equals(corrvalue1.trim()))) {
                    documentSearchQuery.append(WildCardSql.getWildCardSql1("FILES.PRI_KEY_VAL", corrvalue1.trim().toUpperCase()));
                }
                if (((corrattribute2 != null) && (corrattribute2.equalsIgnoreCase("Invoice Number") || corrattribute2.equalsIgnoreCase("Shipment Number") || corrattribute2.equalsIgnoreCase("Cheque Number"))) && (corrvalue2 != null && !"".equals(corrvalue2.trim()))) {
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
                if (((corrattribute != null) && (corrattribute.equalsIgnoreCase("BOL Number"))) && (corrvalue != null && !"".equals(corrvalue.trim()))) {
                    documentSearchQuery.append(WildCardSql.getWildCardSql1("ASN.BOL_NUMBER", corrvalue.trim().toUpperCase()));
                }
                if (((corrattribute1 != null) && (corrattribute1.equalsIgnoreCase("BOL Number"))) && (corrvalue1 != null && !"".equals(corrvalue1.trim()))) {
                    documentSearchQuery.append(WildCardSql.getWildCardSql1("ASN.BOL_NUMBER", corrvalue1.trim().toUpperCase()));
                }
                if (((corrattribute2 != null) && (corrattribute2.equalsIgnoreCase("BOL Number"))) && (corrvalue2 != null && !"".equals(corrvalue2.trim()))) {
                    documentSearchQuery.append(WildCardSql.getWildCardSql1("ASN.BOL_NUMBER", corrvalue2.trim().toUpperCase()));
                }
                if (((corrattribute != null) && (corrattribute.equalsIgnoreCase("GS Number"))) && (corrvalue != null && !"".equals(corrvalue.trim()))) {
                    documentSearchQuery.append(WildCardSql.getWildCardSql1("FILES.GS_CONTROL_NUMBER", corrvalue.trim().toUpperCase()));
                }
                if (((corrattribute1 != null) && (corrattribute1.equalsIgnoreCase("GS Number"))) && (corrvalue1 != null && !"".equals(corrvalue1.trim()))) {
                    documentSearchQuery.append(WildCardSql.getWildCardSql1("FILES.GS_CONTROL_NUMBER", corrvalue1.trim().toUpperCase()));
                }
                if (((corrattribute != null) && (corrattribute2.equalsIgnoreCase("GS Number"))) && (corrvalue2 != null && !"".equals(corrvalue2.trim()))) {
                    documentSearchQuery.append(WildCardSql.getWildCardSql1("FILES.GS_CONTROL_NUMBER", corrvalue2.trim().toUpperCase()));
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

                if ((doctype != null) && (!"".equals(doctype.trim()))) {
                    documentSearchQuery.append(WildCardSql.getWildCardSql1("FILES.TRANSACTION_TYPE", doctype.trim()));
                }
                if ((status != null) && (!"-1".equals(status.trim()))) {
                    documentSearchQuery.append(WildCardSql.getWildCardSql1("FILES.STATUS", status.trim()));
                }
                if ((ackStatus != null) && (!"-1".equals(ackStatus.trim()))) {
                    documentSearchQuery.append(WildCardSql.getWildCardSql1("FILES.ACK_STATUS", ackStatus.trim()));
                }
                if ((docBusId != null) && (!"".equals(docBusId.trim()))) {
                    documentSearchQuery.append(WildCardSql.getWildCardSql1("FILES.RECEIVER_ID", docBusId.trim().toUpperCase()));
                }
                if ((docSenderId != null) && (!"".equals(docSenderId.trim()))) {
                    documentSearchQuery.append(WildCardSql.getWildCardSql1("FILES.SENDER_ID", docSenderId.trim().toUpperCase()));
                }
//                if ((docSenderName != null) && (!"".equals(docSenderName.trim()))) {
//                    documentSearchQuery.append(WildCardSql.getWildCardSql1("TP1.NAME", docSenderName.trim().toUpperCase()));
//                }
//                if ((docRecName != null) && (!"".equals(docRecName.trim()))) {
//                    documentSearchQuery.append(WildCardSql.getWildCardSql1("TP2.NAME", docRecName.trim().toUpperCase()));
//                }
                if ((docdatepicker != null) && (!"".equals(docdatepicker))) {
                    tmp_Recieved_From = DateUtility.getInstance().DateViewToDBCompare(docdatepicker);
                    documentSearchQuery.append(" AND FILES.DATE_TIME_RECEIVED <= '" + tmp_Recieved_From + "'");
                }
                if ((docdatepickerfrom != null) && (!"".equals(docdatepickerfrom))) {
                    tmp_Recieved_From = DateUtility.getInstance().DateViewToDBCompare(docdatepickerfrom);
                    documentSearchQuery.append(" AND FILES.DATE_TIME_RECEIVED >= '" + tmp_Recieved_From + "'");
                }
                documentSearchQuery.append(" order by DATE_TIME_RECEIVED DESC fetch first 50 rows only");
            }
            System.out.println("Query:" + documentSearchQuery.toString());
            System.out.println("after query and before conn:" + DateUtility.getInstance().getCurrentDB2Timestamp());
            Map<String, String> tradingPartners = DataSourceDataProvider.getInstance().getAllTradingPartners();
            connection = ConnectionProvider.getInstance().getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(documentSearchQuery.toString());
            documentList = new ArrayList<DocRepositoryBean>();
            System.out.println("after conn & before rs:" + DateUtility.getInstance().getCurrentDB2Timestamp());
            while (resultSet.next()) {
                DocRepositoryBean docRepositoryBean = new DocRepositoryBean();
                docRepositoryBean.setId(resultSet.getInt("ID"));
                docRepositoryBean.setFile_id(resultSet.getString("FILE_ID"));
                docRepositoryBean.setFile_origin(resultSet.getString("FILE_ORIGIN"));
                docRepositoryBean.setFile_type(resultSet.getString("FILE_TYPE"));
                docRepositoryBean.setIsa_number(resultSet.getString("ISA_NUMBER"));
                docRepositoryBean.setTransaction_type(resultSet.getString("TRANSACTION_TYPE"));
                String direction = resultSet.getString("DIRECTION");
                docRepositoryBean.setDirection(direction);
                docRepositoryBean.setDate_time_rec(resultSet.getTimestamp("DATE_TIME_RECEIVED"));
                docRepositoryBean.setStatus(resultSet.getString("STATUS"));
                if (corrattribute != null && !"".equalsIgnoreCase(corrattribute)) {
                    docRepositoryBean.setCorrattribute(corrattribute);
                    if (corrattribute.equalsIgnoreCase("PO Number")) {
                        docRepositoryBean.setCorrvalue(resultSet.getString("SEC_KEY_VAL"));
                    }
                    if (corrattribute.equalsIgnoreCase("Invoice Number") || corrattribute.equalsIgnoreCase("Shipment Number") || corrattribute.equalsIgnoreCase("Cheque Number")) {
                        docRepositoryBean.setCorrvalue(resultSet.getString("PRI_KEY_VAL"));
                    }
                    if (corrattribute.equalsIgnoreCase("ISA Number")) {
                        docRepositoryBean.setCorrvalue(resultSet.getString("ISA_Number"));
                    }
                    if (corrattribute.equalsIgnoreCase("BOL Number")) {
                        docRepositoryBean.setCorrvalue(resultSet.getString("BOL_NUMBER"));
                    }
                    if (corrattribute.equalsIgnoreCase("GS Number")) {
                        docRepositoryBean.setCorrvalue(resultSet.getString("GS_CONTROL_NUMBER"));
                    }
                    if (corrattribute.equalsIgnoreCase("FILE NAME")) {
                        docRepositoryBean.setCorrvalue(resultSet.getString("FILENAME"));
                    }
                }
                if (corrattribute1 != null && !"".equalsIgnoreCase(corrattribute1)) {
                    docRepositoryBean.setCorrattribute1(corrattribute1);
                    if (corrattribute1.equalsIgnoreCase("PO Number")) {
                        docRepositoryBean.setCorrvalue1(resultSet.getString("SEC_KEY_VAL"));
                    }
                    if (corrattribute1.equalsIgnoreCase("Invoice Number") || corrattribute1.equalsIgnoreCase("Shipment Number") || corrattribute1.equalsIgnoreCase("Cheque Number")) {
                        docRepositoryBean.setCorrvalue1(resultSet.getString("PRI_KEY_VAL"));
                    }
                    if (corrattribute1.equalsIgnoreCase("ISA Number")) {
                        docRepositoryBean.setCorrvalue1(resultSet.getString("ISA_Number"));
                    }
                    if (corrattribute1.equalsIgnoreCase("BOL Number")) {
                        docRepositoryBean.setCorrvalue1(resultSet.getString("BOL_NUMBER"));
                    }
                    if (corrattribute1.equalsIgnoreCase("GS Number")) {
                        docRepositoryBean.setCorrvalue1(resultSet.getString("GS_CONTROL_NUMBER"));
                    }
                    if (corrattribute1.equalsIgnoreCase("FILE NAME")) {
                        docRepositoryBean.setCorrvalue1(resultSet.getString("FILENAME"));
                    }
                }
                if (corrattribute2 != null && !"".equalsIgnoreCase(corrattribute2)) {
                    docRepositoryBean.setCorrattribute2(corrattribute2);
                    if (corrattribute2.equalsIgnoreCase("PO Number")) {
                        docRepositoryBean.setCorrvalue2(resultSet.getString("SEC_KEY_VAL"));
                    }
                    if (corrattribute2.equalsIgnoreCase("Invoice Number") || corrattribute2.equalsIgnoreCase("Shipment Number") || corrattribute2.equalsIgnoreCase("Cheque Number")) {
                        docRepositoryBean.setCorrvalue2(resultSet.getString("PRI_KEY_VAL"));
                    }
                    if (corrattribute2.equalsIgnoreCase("ISA Number")) {
                        docRepositoryBean.setCorrvalue2(resultSet.getString("ISA_Number"));
                    }
                    if (corrattribute2.equalsIgnoreCase("BOL Number")) {
                        docRepositoryBean.setCorrvalue2(resultSet.getString("BOL_NUMBER"));
                    }
                    if (corrattribute2.equalsIgnoreCase("GS Number")) {
                        docRepositoryBean.setCorrvalue2(resultSet.getString("GS_CONTROL_NUMBER"));
                    }
                    if (corrattribute2.equalsIgnoreCase("FILE NAME")) {
                        docRepositoryBean.setCorrvalue2(resultSet.getString("FILENAME"));
                    }
                }
                if ((direction != null) && ("INBOUND".equalsIgnoreCase(direction))) {
                    String pname_Sender = "";
                    if (((resultSet.getString("SENDER_ID")) != null)
                            && (((tradingPartners.get(resultSet.getString("SENDER_ID")))) != null)) {
                        pname_Sender = (tradingPartners.get(resultSet.getString("SENDER_ID"))).toString();
                    } else {
                        pname_Sender = "_";
                    }
                    docRepositoryBean.setPname(pname_Sender);
                } else {
                    String pname_Reciever = "";
                    if (((resultSet.getString("RECEIVER_ID")) != null)
                            && (((tradingPartners.get(resultSet.getString("RECEIVER_ID")))) != null)) {
                        pname_Reciever = ((tradingPartners.get(resultSet.getString("RECEIVER_ID"))).toString());
                    } else {
                        pname_Reciever = "_";
                    }
                    docRepositoryBean.setPname(pname_Reciever);
                }

                docRepositoryBean.setPoNumber(resultSet.getString("SEC_KEY_VAL"));
                docRepositoryBean.setReProcessStatus(resultSet.getString("REPROCESSSTATUS"));
                docRepositoryBean.setAckStatus(resultSet.getString("ACK_STATUS"));
                documentList.add(docRepositoryBean);
            }
            System.out.println("after rs:" + DateUtility.getInstance().getCurrentDB2Timestamp());
        } catch (SQLException exception) {
            LoggerUtility.log(logger, "SQLException occurred in buildDocumentQuery method in DocRepositoryServiceImpl:: " + exception.getMessage(), Level.ERROR, exception.getCause());
        } catch (Exception exception) {
            LoggerUtility.log(logger, "Exception occurred in buildDocumentQuery method in DocRepositoryServiceImpl:: " + exception.getMessage(), Level.ERROR, exception.getCause());
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
            } catch (SQLException se) {
                throw new ServiceLocatorException(se);
            }
        }
        return documentList;
    }

    public DocRepositoryBean getDocRepositoryBean() {
        return docRepositoryBean;
    }

    public void setDocRepositoryBean(DocRepositoryBean docRepositoryBean) {
        this.docRepositoryBean = docRepositoryBean;
    }
}
