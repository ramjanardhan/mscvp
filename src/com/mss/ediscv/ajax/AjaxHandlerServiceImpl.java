/*
 * AjaxHandlerServiceImpl.java
 * Created on June 11, 2008, 12:57 AM
 *greensheetListSearch
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.mss.ediscv.ajax;

import com.mss.ediscv.lfc.AsnLifecycleBean;
import com.mss.ediscv.lfc.InvoiceLifecycleBean;
import com.mss.ediscv.lfc.LtInvoicesBean;
import com.mss.ediscv.lfc.LtResponsesBean;
import com.mss.ediscv.lfc.LtShipmentsBean;
import com.mss.ediscv.lfc.LtTenderBean;
import com.mss.ediscv.lfc.PaymentLifecycleBean;
import com.mss.ediscv.lfc.PoLifecycleBean;
import com.mss.ediscv.util.ConnectionProvider;
import com.mss.ediscv.util.DataSourceDataProvider;
import com.mss.ediscv.util.DateUtility;
import com.mss.ediscv.util.FileUtility;
import com.mss.ediscv.util.LifecycleUtility;
import com.mss.ediscv.util.LoggerUtility;
import com.mss.ediscv.util.MailManager;
import com.mss.ediscv.util.PasswordUtil;
import com.mss.ediscv.util.ServiceLocatorException;
import com.mss.ediscv.util.WildCardSql;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringTokenizer;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.Level;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author miracle
 */
public class AjaxHandlerServiceImpl implements AjaxHandlerService {

    private HttpServletRequest httpServletRequest;
    private Statement statement;
    private PoLifecycleBean poLifecycleBean;
    private AsnLifecycleBean asnLifecycleBean;
    private InvoiceLifecycleBean invoiceLifecycleBean;
    private PaymentLifecycleBean paymentLifecycleBean;
    private LtTenderBean ltTenderBean;
    private LtResponsesBean ltResponsesBean;
    private LtShipmentsBean ltShipmentsBean;
    private LtInvoicesBean ltInvoicesBean;
    private static Logger logger = LogManager.getLogger(AjaxHandlerServiceImpl.class.getName());

    public AjaxHandlerServiceImpl() {
    }

    @Override
    public String getPoDetails(String poNumber, String poInst, String database) throws ServiceLocatorException {
        System.out.println("poNumber is"+poNumber);
        System.out.println("poInst is"+poInst);
        System.out.println("database is"+database);
        boolean isGetting = false;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String poDetailsQuery = "";

        StringBuilder sb = new StringBuilder();
        try {
            if ("ARCHIVE".equals(database)) {
                poDetailsQuery = "SELECT ARCHIVE_FILES.FILE_TYPE,ARCHIVE_FILES.TRANSACTION_TYPE as TRANSACTION_TYPE,ARCHIVE_FILES.ISA_NUMBER,"
                        + "ARCHIVE_FILES.GS_CONTROL_NUMBER,ARCHIVE_FILES.ST_CONTROL_NUMBER,ARCHIVE_PO.FILE_ID as FILE_ID,ARCHIVE_PO.PO_Number,ARCHIVE_PO.Order_Date,ARCHIVE_PO.PO_VALUE,ARCHIVE_PO.SAP_IDOC_Number,"
                        + "ARCHIVE_FILES.SENDER_ID,ARCHIVE_FILES.RECEIVER_ID,ARCHIVE_PO.DELIVERY_STATUS,ARCHIVE_PO.SHIP_DATE,ARCHIVE_PO.ROUTINGS, ARCHIVE_PO.INVOICED_AMOUNT,ARCHIVE_PO.PAYMENT_RECEIVED,ARCHIVE_PO.SHIP_ADDRESS_ID,ARCHIVE_PO.BILL_ADDRESS_ID,"
                        + "ARCHIVE_PO.ITEM_QTY,ARCHIVE_FILES.PRE_TRANS_FILEPATH,ARCHIVE_FILES.POST_TRANS_FILEPATH,ARCHIVE_FILES.ERROR_REPORT_FILEPATH as ERROR_REPORT_FILEPATH,ARCHIVE_FILES.SENDER_ID,ARCHIVE_FILES.RECEIVER_ID, "
                        + "ARCHIVE_FILES.ORG_FILEPATH,ARCHIVE_FILES.ERR_MESSAGE,ARCHIVE_PO.SO_NUMBER,ARCHIVE_FILES.STATUS,ARCHIVE_FILES.ISA_DATE,ARCHIVE_FILES.ISA_TIME,"
                        + "ARCHIVE_FILES.ACK_FILE_ID as ACK_FILE_ID,ARCHIVE_PO.ORDER_STATUS AS ORDER_STATUS "
                        + "FROM ARCHIVE_PO LEFT OUTER JOIN ARCHIVE_FILES ON (ARCHIVE_PO.PO_NUMBER = ARCHIVE_FILES.PRI_KEY_VAL and ARCHIVE_PO.FILE_ID = ARCHIVE_FILES.FILE_ID) "
                        + "WHERE FLOWFLAG like 'M' AND ARCHIVE_PO.PO_NUMBER LIKE '%" + poNumber + "%' and ARCHIVE_FILES.FILE_ID "
                        + "LIKE '%" + poInst + "%'";
            } else {
                poDetailsQuery = "SELECT FILES.FILE_TYPE,Files.TRANSACTION_TYPE as TRANSACTION_TYPE,FILES.ISA_NUMBER,"
                        + "FILES.GS_CONTROL_NUMBER,FILES.ST_CONTROL_NUMBER,PO.FILE_ID as FILE_ID,PO.PO_Number,PO.Order_Date,PO.PO_VALUE,PO.SAP_IDOC_Number,"
                        + "FILES.SENDER_ID,FILES.RECEIVER_ID,PO.DELIVERY_STATUS,PO.SHIP_DATE,PO.ROUTINGS, PO.INVOICED_AMOUNT,PO.PAYMENT_RECEIVED,PO.SHIP_ADDRESS_ID,PO.BILL_ADDRESS_ID,"
                        + "PO.ITEM_QTY,FILES.PRE_TRANS_FILEPATH,FILES.POST_TRANS_FILEPATH,FILES.ERROR_REPORT_FILEPATH as ERROR_REPORT_FILEPATH, "
                        + "FILES.ORG_FILEPATH,FILES.ERR_MESSAGE,PO.SO_NUMBER,FILES.STATUS,FILES.ISA_DATE,FILES.ISA_TIME,"
                        + "FILES.ACK_FILE_ID as ACK_FILE_ID,PO.ORDER_STATUS AS ORDER_STATUS "
                        + "FROM PO LEFT OUTER JOIN FILES ON (PO.PO_NUMBER = FILES.PRI_KEY_VAL and PO.FILE_ID = FILES.FILE_ID) "
                        + "WHERE FLOWFLAG like 'M' AND PO.PO_NUMBER LIKE '%" + poNumber + "%' and FILES.FILE_ID "
                        + "LIKE '%" + poInst + "%'";
            }
            System.out.println("po query is -------------" + poDetailsQuery);
            Map<String, String> tradingPartners = DataSourceDataProvider.getInstance().getAllTradingPartners();
            System.out.println("after map");
            connection = ConnectionProvider.getInstance().getConnection();
            System.out.println("after connection");
            preparedStatement = connection.prepareStatement(poDetailsQuery);
            System.out.println("after prepare statement");
            resultSet = preparedStatement.executeQuery();
            System.out.println("after result set");
            sb.append("<xml version=\"1.0\">");
            sb.append("<DETAILS>");
            while (resultSet.next()) {
                sb.append("<DETAIL><VALID>true</VALID>");
                if (resultSet.getString("FILE_ID") != null && !"".equals(resultSet.getString("FILE_ID"))) {
                    sb.append("<FILEID>" + resultSet.getString("FILE_ID") + "</FILEID>");
                } else {
                    sb.append("<FILEID>NO</FILEID>");
                }
                if (resultSet.getString("PO_Number") != null && !"".equals(resultSet.getString("PO_Number"))) {
                    sb.append("<PONUM>" + resultSet.getString("PO_Number") + "</PONUM>");
                } else {
                    sb.append("<PONUM>NO</PONUM>");
                }
                if (resultSet.getString("Order_Date") != null && !"".equals(resultSet.getString("Order_Date"))) {
                    sb.append("<PODATE>" + resultSet.getString("Order_Date") + "</PODATE>");
                } else {
                    sb.append("<PODATE>NO</PODATE>");
                }
                if (resultSet.getString("PO_VALUE") != null && !"".equals(resultSet.getString("PO_VALUE"))) {
                    sb.append("<POVALUE>" + resultSet.getString("PO_VALUE") + "</POVALUE>");
                } else {
                    sb.append("<POVALUE>NO</POVALUE>");
                }
                if (resultSet.getString("SHIP_DATE") != null && !"".equals(resultSet.getString("SHIP_DATE"))) {
                    sb.append("<SHIP_DATE>" + resultSet.getString("SHIP_DATE") + "</SHIP_DATE>");
                } else {
                    sb.append("<SHIP_DATE>NO</SHIP_DATE>");
                }
                if (resultSet.getString("ROUTINGS") != null && !"".equals(resultSet.getString("ROUTINGS"))) {
                    sb.append("<ROUTINGS>" + resultSet.getString("ROUTINGS") + "</ROUTINGS>");
                } else {
                    sb.append("<ROUTINGS>NO</ROUTINGS>");
                }
                if (resultSet.getString("INVOICED_AMOUNT") != null && !"".equals(resultSet.getString("INVOICED_AMOUNT"))) {
                    sb.append("<INVOICED_AMOUNT>" + resultSet.getString("INVOICED_AMOUNT") + "</INVOICED_AMOUNT>");
                } else {
                    sb.append("<INVOICED_AMOUNT>NO</INVOICED_AMOUNT>");
                }
                if (resultSet.getString("PAYMENT_RECEIVED") != null && !"".equals(resultSet.getString("PAYMENT_RECEIVED"))) {
                    sb.append("<PAYMENT_RECEIVED>" + resultSet.getString("PAYMENT_RECEIVED") + "</PAYMENT_RECEIVED>");
                } else {
                    sb.append("<PAYMENT_RECEIVED>NO</PAYMENT_RECEIVED>");
                }
                if (resultSet.getString("SHIP_ADDRESS_ID") != null && !"".equals(resultSet.getString("SHIP_ADDRESS_ID"))) {
                    sb.append("<SHIP_ADDRESS_ID>" + resultSet.getString("SHIP_ADDRESS_ID") + "</SHIP_ADDRESS_ID>");
                } else {
                    sb.append("<SHIP_ADDRESS_ID>NO</SHIP_ADDRESS_ID>");
                }
                if (resultSet.getString("BILL_ADDRESS_ID") != null && !"".equals(resultSet.getString("BILL_ADDRESS_ID"))) {
                    sb.append("<BILL_ADDRESS_ID>" + resultSet.getString("BILL_ADDRESS_ID") + "</BILL_ADDRESS_ID>");
                } else {
                    sb.append("<BILL_ADDRESS_ID>NO</BILL_ADDRESS_ID>");
                }
                if (resultSet.getString("ISA_NUMBER") != null && !"".equals(resultSet.getString("ISA_NUMBER"))) {
                    sb.append("<ISA_NUMBER>" + resultSet.getString("ISA_NUMBER") + "</ISA_NUMBER>");
                } else {
                    sb.append("<ISA_NUMBER>NO</ISA_NUMBER>");
                }
                if (resultSet.getString("FILE_TYPE") != null && !"".equals(resultSet.getString("FILE_TYPE"))) {
                    sb.append("<FILE_TYPE>" + resultSet.getString("FILE_TYPE") + "</FILE_TYPE>");
                } else {
                    sb.append("<FILE_TYPE>NO</FILE_TYPE>");
                }
                if (resultSet.getString("GS_CONTROL_NUMBER") != null && !"".equals(resultSet.getString("GS_CONTROL_NUMBER"))) {
                    sb.append("<GS_CONTROL_NUMBER>" + resultSet.getString("GS_CONTROL_NUMBER") + "</GS_CONTROL_NUMBER>");
                } else {
                    sb.append("<GS_CONTROL_NUMBER>NO</GS_CONTROL_NUMBER>");
                }
                if (resultSet.getString("ST_CONTROL_NUMBER") != null && !"".equals(resultSet.getString("ST_CONTROL_NUMBER"))) {
                    sb.append("<ST_CONTROL_NUMBER>" + resultSet.getString("ST_CONTROL_NUMBER") + "</ST_CONTROL_NUMBER>");
                } else {
                    sb.append("<ST_CONTROL_NUMBER>NO</ST_CONTROL_NUMBER>");
                }
                if (resultSet.getString("SAP_IDOC_Number") != null && !"".equals(resultSet.getString("SAP_IDOC_Number"))) {
                    sb.append("<SAPIDOCNUM>" + resultSet.getString("SAP_IDOC_Number") + "</SAPIDOCNUM>");
                } else {
                    sb.append("<SAPIDOCNUM>NO</SAPIDOCNUM>");
                }
                if (resultSet.getString("SENDER_ID") != null && !"".equals(resultSet.getString("SENDER_ID"))) {
                    sb.append("<SENDER_ID>" + resultSet.getString("SENDER_ID") + "</SENDER_ID>");
                } else {
                    sb.append("<SENDER_ID>NO</SENDER_ID>");
                }
                if (resultSet.getString("RECEIVER_ID") != null && !"".equals(resultSet.getString("RECEIVER_ID"))) {
                    sb.append("<RECEIVER_ID>" + resultSet.getString("RECEIVER_ID") + "</RECEIVER_ID>");
                } else {
                    sb.append("<RECEIVER_ID>NO</RECEIVER_ID>");
                }
                if (resultSet.getString("SENDER_ID") != null && (((tradingPartners.get(resultSet.getString("SENDER_ID")))) != null)) {
                    sb.append("<SENDER_NAME>" + (tradingPartners.get(resultSet.getString("SENDER_ID"))).toString() + "</SENDER_NAME>");
                } else {
                    sb.append("<SENDER_NAME>--</SENDER_NAME>");
                }
                if (resultSet.getString("RECEIVER_ID") != null && (((tradingPartners.get(resultSet.getString("RECEIVER_ID")))) != null)) {
                    sb.append("<RECEIVER_NAME>" + (tradingPartners.get(resultSet.getString("RECEIVER_ID"))).toString() + "</RECEIVER_NAME>");
                } else {
                    sb.append("<RECEIVER_NAME>--</RECEIVER_NAME>");
                }
                if (resultSet.getString("DELIVERY_STATUS") != null && !"".equals(resultSet.getString("DELIVERY_STATUS"))) {
                    sb.append("<DELSTATUS>" + resultSet.getString("DELIVERY_STATUS").toUpperCase() + "</DELSTATUS>");
                } else {
                    sb.append("<DELSTATUS>NO</DELSTATUS>");
                }
                if (resultSet.getString("ITEM_QTY") != null && !"".equals(resultSet.getString("ITEM_QTY"))) {
                    sb.append("<ITEMQTY>" + resultSet.getString("ITEM_QTY") + "</ITEMQTY>");
                } else {
                    sb.append("<ITEMQTY>NO</ITEMQTY>");
                }
                if (resultSet.getString("STATUS") != null && !"".equals(resultSet.getString("STATUS"))) {
                    sb.append("<STATUS>" + resultSet.getString("STATUS") + "</STATUS>");
                } else {
                    sb.append("<STATUS>NO</STATUS>");
                }
                if (resultSet.getString("TRANSACTION_TYPE") != null && !"".equals(resultSet.getString("TRANSACTION_TYPE"))) {
                    sb.append("<TRANSACTION_TYPE>" + resultSet.getString("TRANSACTION_TYPE") + "</TRANSACTION_TYPE>");
                } else {
                    sb.append("<TRANSACTION_TYPE>NO</TRANSACTION_TYPE>");
                }
                if (resultSet.getString("PRE_TRANS_FILEPATH") != null) {
                    if (new File(resultSet.getString("PRE_TRANS_FILEPATH")).exists() && new File(resultSet.getString("PRE_TRANS_FILEPATH")).isFile()) {
                        sb.append("<PRETRANSFILEPATH>" + resultSet.getString("PRE_TRANS_FILEPATH") + "</PRETRANSFILEPATH>");
                    } else {
                        sb.append("<PRETRANSFILEPATH>No File</PRETRANSFILEPATH>");
                    }
                } else {
                    sb.append("<PRETRANSFILEPATH>No File</PRETRANSFILEPATH>");
                }
                if (resultSet.getString("POST_TRANS_FILEPATH") != null) {
                    if (new File(resultSet.getString("POST_TRANS_FILEPATH")).exists() && new File(resultSet.getString("POST_TRANS_FILEPATH")).isFile()) {
                        sb.append("<POSTTRANSFILEPATH>" + resultSet.getString("POST_TRANS_FILEPATH") + "</POSTTRANSFILEPATH>");
                    } else {
                        sb.append("<POSTTRANSFILEPATH>No File</POSTTRANSFILEPATH>");
                    }
                } else {
                    sb.append("<POSTTRANSFILEPATH>No File</POSTTRANSFILEPATH>");
                }
                if (resultSet.getString("ORG_FILEPATH") != null) {
                    if (new File(resultSet.getString("ORG_FILEPATH")).exists() && new File(resultSet.getString("ORG_FILEPATH")).isFile()) {
                        sb.append("<ORGFILEPATH>" + resultSet.getString("ORG_FILEPATH") + "</ORGFILEPATH>");
                    } else {
                        sb.append("<ORGFILEPATH>No File</ORGFILEPATH>");
                    }
                } else {
                    sb.append("<ORGFILEPATH>No File</ORGFILEPATH>");
                }
                if (resultSet.getString("ERROR_REPORT_FILEPATH") != null) {
                    if (new File(resultSet.getString("ERROR_REPORT_FILEPATH")).exists() && new File(resultSet.getString("ERROR_REPORT_FILEPATH")).isFile()) {
                        sb.append("<ERROR_REPORT_FILEPATH>" + resultSet.getString("ERROR_REPORT_FILEPATH") + "</ERROR_REPORT_FILEPATH>");
                    } else {
                        sb.append("<ERROR_REPORT_FILEPATH>No File</ERROR_REPORT_FILEPATH>");
                    }
                } else {
                    sb.append("<ERROR_REPORT_FILEPATH>No File</ERROR_REPORT_FILEPATH>");
                }
                if (resultSet.getString("ERR_MESSAGE") != null && !"".equals(resultSet.getString("ERR_MESSAGE"))) {
                    sb.append("<ERR_MESSAGE>" + resultSet.getString("ERR_MESSAGE") + "</ERR_MESSAGE>");
                } else {
                    sb.append("<ERR_MESSAGE>NO MSG</ERR_MESSAGE>");
                }
                if (resultSet.getString("SO_NUMBER") != null && !"".equals(resultSet.getString("SO_NUMBER"))) {
                    sb.append("<SO_NUMBER>" + resultSet.getString("SO_NUMBER") + "</SO_NUMBER>");
                } else {
                    sb.append("<SO_NUMBER>NO</SO_NUMBER>");
                }
                if (resultSet.getString("ACK_FILE_ID") != null) {
                    if (new File(resultSet.getString("ACK_FILE_ID")).exists() && new File(resultSet.getString("ACK_FILE_ID")).isFile()) {
                        sb.append("<ACKFILEID>" + resultSet.getString("ACK_FILE_ID") + "</ACKFILEID>");
                    } else {
                        sb.append("<ACKFILEID>No File</ACKFILEID>");
                    }
                } else {
                    sb.append("<ACKFILEID>No File</ACKFILEID>");
                }
                if (resultSet.getString("ORDER_STATUS") != null && !"".equals(resultSet.getString("ORDER_STATUS"))) {
                    sb.append("<ORDER_STATUS>" + resultSet.getString("ORDER_STATUS") + "</ORDER_STATUS>");
                } else {
                    sb.append("<ORDER_STATUS>NO</ORDER_STATUS>");
                }
                sb.append("<ISA_DATE>" + resultSet.getString("ISA_DATE") + "</ISA_DATE>");
                sb.append("<ISA_TIME>" + resultSet.getString("ISA_TIME") + "</ISA_TIME>");
                String sapDetails = DataSourceDataProvider.getInstance().getSapDetails(poInst, poNumber);
                if (!sapDetails.equals("None")) {
                    sb.append("<SAP_DETAILS>YES</SAP_DETAILS>");
                    String sapDetailsInfo[] = sapDetails.split("\\|");
                    sb.append("<SAP_USER>" + sapDetailsInfo[0] + "</SAP_USER>");
                    sb.append("<IDOC_NUMBER>" + sapDetailsInfo[1] + "</IDOC_NUMBER>");
                    sb.append("<PO_NUMBER>" + sapDetailsInfo[2] + "</PO_NUMBER>");
                    sb.append("<PO_DATE>" + sapDetailsInfo[3] + "</PO_DATE>");
                    sb.append("<IDOC_STATUS_CODE>" + sapDetailsInfo[4] + "</IDOC_STATUS_CODE>");
                    sb.append("<IDOC_STATUS_DESCRIPTION>" + sapDetailsInfo[5] + "</IDOC_STATUS_DESCRIPTION>");
                } else {
                    sb.append("<SAP_DETAILS>NO</SAP_DETAILS>");
                }
                sb.append("</DETAIL>");
                isGetting = true;
            }
            if (!isGetting) {
                isGetting = false;
                sb.append("<DETAIL><VALID>false</VALID></DETAIL>");
            }
            sb.append("</DETAILS>");
            sb.append("</xml>");
        } catch (SQLException sqlException) {
            LoggerUtility.log(logger, "SQLException occurred in getPoDetails method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
        } catch (ServiceLocatorException serviceLocatorException) {
            LoggerUtility.log(logger, "ServiceLocatorException occurred in getPoDetails method:: " + serviceLocatorException.getMessage(), Level.ERROR, serviceLocatorException.getCause());
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                    resultSet = null;
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                    preparedStatement = null;
                }
                if (connection != null) {
                    connection.close();
                    connection = null;
                }
            } catch (SQLException sqlException) {
                LoggerUtility.log(logger, "SQLException occurred in getPoDetails method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
            }
        }
        return sb.toString();
    }

    @Override
    public String getASNDetails(String asnNumber, String poNumber, String fileID, String database) throws ServiceLocatorException {
        System.out.println("asnNumber is"+asnNumber);
        System.out.println("asnNumber is"+poNumber);
        System.out.println("asnNumber is"+fileID);
        System.out.println("asnNumber is"+database);
        boolean isGetting = false;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        StringBuilder sb = new StringBuilder();
        String asnDetailsQuery = "";
        try {
            if ("ARCHIVE".equals(database)) {
                asnDetailsQuery = "SELECT ARCHIVE_FILES.FILE_TYPE,ARCHIVE_FILES.TRANSACTION_TYPE as TRANSACTION_TYPE,ARCHIVE_FILES.ST_CONTROL_NUMBER as ST_CONTROL_NUMBER,"
                        + "ARCHIVE_FILES.GS_CONTROL_NUMBER as GS_CONTROL_NUMBER,ARCHIVE_FILES.SENDER_ID,ARCHIVE_FILES.RECEIVER_ID,ARCHIVE_ASN.FILE_ID as FILE_ID,ARCHIVE_ASN.ASN_NUMBER as ASN_NUMBER,ARCHIVE_ASN.PO_NUMBER as PO_NUMBER,"
                        + "ARCHIVE_ASN.BOL_NUMBER as BOL_NUMBER,ARCHIVE_FILES.ISA_NUMBER as ISA_NUMBER,ARCHIVE_FILES.ISA_DATE as  ISA_DATE,ARCHIVE_FILES.ISA_TIME, "
                        + " ARCHIVE_FILES.PRE_TRANS_FILEPATH,ARCHIVE_FILES.POST_TRANS_FILEPATH,ARCHIVE_FILES.SENDER_ID,ARCHIVE_FILES.RECEIVER_ID,"
                        + "ARCHIVE_FILES.ORG_FILEPATH,ARCHIVE_FILES.ERR_MESSAGE,ARCHIVE_FILES.STATUS,ARCHIVE_FILES.ERROR_REPORT_FILEPATH as ERROR_REPORT_FILEPATH,"
                        + "ARCHIVE_FILES.ACK_FILE_ID as ACK_FILE_ID,ARCHIVE_ASN.SHIP_DATE as SHIP_DATE "
                        + "FROM ARCHIVE_ASN LEFT OUTER JOIN ARCHIVE_FILES ON (ARCHIVE_ASN.ASN_NUMBER = ARCHIVE_FILES.PRI_KEY_VAL AND ARCHIVE_ASN.FILE_ID = ARCHIVE_FILES.FILE_ID) "
                        + "WHERE FLOWFLAG like 'M' AND ASN_NUMBER LIKE '%" + asnNumber + "%' AND ARCHIVE_ASN.PO_NUMBER LIKE '%" + poNumber + "%'"
                        + " AND ARCHIVE_ASN.FILE_ID LIKE '" + fileID + "'";
            } else {
                asnDetailsQuery = "SELECT FILES.FILE_TYPE,Files.TRANSACTION_TYPE as TRANSACTION_TYPE,FILES.ST_CONTROL_NUMBER as ST_CONTROL_NUMBER,FILES.GS_CONTROL_NUMBER as GS_CONTROL_NUMBER,ASN.FILE_ID as FILE_ID,ASN.ASN_NUMBER as ASN_NUMBER,ASN.PO_NUMBER as PO_NUMBER,"
                        + "ASN.BOL_NUMBER as BOL_NUMBER,FILES.SENDER_ID,FILES.RECEIVER_ID,FILES.ISA_NUMBER as ISA_NUMBER,FILES.ISA_DATE as  ISA_DATE,FILES.ISA_TIME, "
                        + " FILES.PRE_TRANS_FILEPATH,FILES.POST_TRANS_FILEPATH,FILES.SENDER_ID,FILES.RECEIVER_ID,"
                        + "FILES.ORG_FILEPATH,FILES.ERR_MESSAGE,FILES.STATUS,FILES.ERROR_REPORT_FILEPATH as ERROR_REPORT_FILEPATH,"
                        + "FILES.ACK_FILE_ID as ACK_FILE_ID,ASN.SHIP_DATE as SHIP_DATE "
                        + "FROM ASN LEFT OUTER JOIN FILES ON (ASN.ASN_NUMBER = FILES.PRI_KEY_VAL AND ASN.FILE_ID = FILES.FILE_ID) "
                        + "WHERE FLOWFLAG like 'M' AND ASN_NUMBER LIKE '%" + asnNumber + "%' AND ASN.PO_NUMBER LIKE '%" + poNumber + "%'"
                        + " AND ASN.FILE_ID LIKE '" + fileID + "'";
            }
            Map<String, String> tradingPartners = DataSourceDataProvider.getInstance().getAllTradingPartners();
            connection = ConnectionProvider.getInstance().getConnection();
            preparedStatement = connection.prepareStatement(asnDetailsQuery);
            resultSet = preparedStatement.executeQuery();
            sb.append("<xml version=\"1.0\">");
            sb.append("<DETAILS>");
            while (resultSet.next()) {
                sb.append("<DETAIL><VALID>true</VALID>");
                if (resultSet.getString("FILE_ID") != null && !"".equals(resultSet.getString("FILE_ID"))) {
                    sb.append("<FILEID>" + resultSet.getString("FILE_ID") + "</FILEID>");
                } else {
                    sb.append("<FILEID>NO</FILEID>");
                }
                if (resultSet.getString("ASN_NUMBER") != null && !"".equals(resultSet.getString("ASN_NUMBER"))) {
                    sb.append("<ASNNUMBER>" + resultSet.getString("ASN_NUMBER") + "</ASNNUMBER>");
                } else {
                    sb.append("<ASNNUMBER>NO</ASNNUMBER>");
                }
                if (resultSet.getString("PO_NUMBER") != null && !"".equals(resultSet.getString("PO_NUMBER"))) {
                    sb.append("<PONUMBER>" + resultSet.getString("PO_NUMBER") + "</PONUMBER>");
                } else {
                    sb.append("<PONUMBER>NO</PONUMBER>");
                }
                if (resultSet.getString("BOL_NUMBER") != null && !"".equals(resultSet.getString("BOL_NUMBER"))) {
                    sb.append("<BOLNUMBER>" + resultSet.getString("BOL_NUMBER") + "</BOLNUMBER>");
                } else {
                    sb.append("<BOLNUMBER>NO</BOLNUMBER>");
                }
                if (resultSet.getString("ISA_NUMBER") != null && !"".equals(resultSet.getString("ISA_NUMBER"))) {
                    sb.append("<ISANUMBER>" + resultSet.getString("ISA_NUMBER") + "</ISANUMBER>");
                } else {
                    sb.append("<ISANUMBER>NO</ISANUMBER>");
                }
                if (resultSet.getString("ISA_DATE") != null && !"".equals(resultSet.getString("ISA_DATE"))) {
                    sb.append("<ISADATE>" + resultSet.getString("ISA_DATE") + "</ISADATE>");
                } else {
                    sb.append("<ISADATE>NO</ISADATE>");
                }
                if (resultSet.getString("ISA_TIME") != null && !"".equals(resultSet.getString("ISA_TIME"))) {
                    sb.append("<ISATIME>" + resultSet.getString("ISA_TIME") + "</ISATIME>");
                } else {
                    sb.append("<ISATIME>NO</ISATIME>");
                }
                if (resultSet.getString("STATUS") != null && !"".equals(resultSet.getString("STATUS"))) {
                    sb.append("<STATUS>" + resultSet.getString("STATUS") + "</STATUS>");
                } else {
                    sb.append("<STATUS>NO</STATUS>");
                }
                if (resultSet.getString("SHIP_DATE") != null && !"".equals(resultSet.getString("SHIP_DATE"))) {
                    sb.append("<SHIPDATE>" + resultSet.getString("SHIP_DATE") + "</SHIPDATE>");
                } else {
                    sb.append("<SHIPDATE>NO</SHIPDATE>");
                }
                if (resultSet.getString("TRANSACTION_TYPE") != null && !"".equals(resultSet.getString("TRANSACTION_TYPE"))) {
                    sb.append("<TRANSACTION_TYPE>" + resultSet.getString("TRANSACTION_TYPE") + "</TRANSACTION_TYPE>");
                } else {
                    sb.append("<TRANSACTION_TYPE>NO</TRANSACTION_TYPE>");
                }
                if (resultSet.getString("SENDER_ID") != null && !"".equals(resultSet.getString("SENDER_ID"))) {
                    sb.append("<SENDER_ID>" + resultSet.getString("SENDER_ID") + "</SENDER_ID>");
                } else {
                    sb.append("<SENDER_ID>NO</SENDER_ID>");
                }
                if (resultSet.getString("RECEIVER_ID") != null && !"".equals(resultSet.getString("RECEIVER_ID"))) {
                    sb.append("<RECEIVER_ID>" + resultSet.getString("RECEIVER_ID") + "</RECEIVER_ID>");
                } else {
                    sb.append("<RECEIVER_ID>NO</RECEIVER_ID>");
                }
                if (resultSet.getString("FILE_TYPE") != null && !"".equals(resultSet.getString("FILE_TYPE"))) {
                    sb.append("<FILE_TYPE>" + resultSet.getString("FILE_TYPE") + "</FILE_TYPE>");
                } else {
                    sb.append("<FILE_TYPE>NO</FILE_TYPE>");
                }
//                if (resultSet.getString("SENDER_NAME") != null && !"".equals(resultSet.getString("SENDER_NAME"))) {
//                    sb.append("<SENDER_NAME>" + resultSet.getString("SENDER_NAME") + "</SENDER_NAME>");
//                } else {
//                    sb.append("<SENDER_NAME>NO</SENDER_NAME>");
//                }
//                if (resultSet.getString("RECEIVER_NAME") != null && !"".equals(resultSet.getString("RECEIVER_NAME"))) {
//                    sb.append("<RECEIVER_NAME>" + resultSet.getString("RECEIVER_NAME") + "</RECEIVER_NAME>");
//                } else {
//                    sb.append("<RECEIVER_NAME>NO</RECEIVER_NAME>");
//                }
//                
                if (resultSet.getString("SENDER_ID") != null && (((tradingPartners.get(resultSet.getString("SENDER_ID")))) != null)) {
                    sb.append("<SENDER_NAME>" + (tradingPartners.get(resultSet.getString("SENDER_ID"))).toString() + "</SENDER_NAME>");
                } else {
                    sb.append("<SENDER_NAME>--</SENDER_NAME>");
                }
                if (resultSet.getString("RECEIVER_ID") != null && (((tradingPartners.get(resultSet.getString("RECEIVER_ID")))) != null)) {
                    sb.append("<RECEIVER_NAME>" + (tradingPartners.get(resultSet.getString("RECEIVER_ID"))).toString() + "</RECEIVER_NAME>");
                } else {
                    sb.append("<RECEIVER_NAME>--</RECEIVER_NAME>");
                }

                if (resultSet.getString("PRE_TRANS_FILEPATH") != null) {
                    if (new File(resultSet.getString("PRE_TRANS_FILEPATH")).exists() && new File(resultSet.getString("PRE_TRANS_FILEPATH")).isFile()) {
                        sb.append("<PRETRANSFILEPATH>" + resultSet.getString("PRE_TRANS_FILEPATH") + "</PRETRANSFILEPATH>");
                    } else {
                        sb.append("<PRETRANSFILEPATH>No File</PRETRANSFILEPATH>");
                    }
                } else {
                    sb.append("<PRETRANSFILEPATH>No File</PRETRANSFILEPATH>");
                }
                if (resultSet.getString("POST_TRANS_FILEPATH") != null) {
                    if (new File(resultSet.getString("POST_TRANS_FILEPATH")).exists() && new File(resultSet.getString("POST_TRANS_FILEPATH")).isFile()) {
                        sb.append("<POSTTRANSFILEPATH>" + resultSet.getString("POST_TRANS_FILEPATH") + "</POSTTRANSFILEPATH>");
                    } else {
                        sb.append("<POSTTRANSFILEPATH>No File</POSTTRANSFILEPATH>");
                    }
                } else {
                    sb.append("<POSTTRANSFILEPATH>No File</POSTTRANSFILEPATH>");
                }
                if (resultSet.getString("ORG_FILEPATH") != null) {
                    if (new File(resultSet.getString("ORG_FILEPATH")).exists() && new File(resultSet.getString("ORG_FILEPATH")).isFile()) {
                        sb.append("<ORGFILEPATH>" + resultSet.getString("ORG_FILEPATH") + "</ORGFILEPATH>");
                    } else {
                        sb.append("<ORGFILEPATH>No File</ORGFILEPATH>");
                    }
                } else {
                    sb.append("<ORGFILEPATH>No File</ORGFILEPATH>");
                }
                if (resultSet.getString("ACK_FILE_ID") != null) {
                    if (new File(resultSet.getString("ACK_FILE_ID")).exists() && new File(resultSet.getString("ACK_FILE_ID")).isFile()) {
                        sb.append("<ACKFILEID>" + resultSet.getString("ACK_FILE_ID") + "</ACKFILEID>");
                    } else {
                        sb.append("<ACKFILEID>No File</ACKFILEID>");
                    }
                } else {
                    sb.append("<ACKFILEID>No File</ACKFILEID>");
                }
                if (resultSet.getString("ERR_MESSAGE") != null && !"".equals(resultSet.getString("ERR_MESSAGE"))) {
                    sb.append("<ERR_MESSAGE>" + resultSet.getString("ERR_MESSAGE") + "</ERR_MESSAGE>");
                } else {
                    sb.append("<ERR_MESSAGE>NO MSG</ERR_MESSAGE>");
                }
                if (resultSet.getString("ERROR_REPORT_FILEPATH") != null) {
                    if (new File(resultSet.getString("ERROR_REPORT_FILEPATH")).exists() && new File(resultSet.getString("ERROR_REPORT_FILEPATH")).isFile()) {
                        sb.append("<ERROR_REPORT_FILEPATH>" + resultSet.getString("ERROR_REPORT_FILEPATH") + "</ERROR_REPORT_FILEPATH>");
                    } else {
                        sb.append("<ERROR_REPORT_FILEPATH>No File</ERROR_REPORT_FILEPATH>");
                    }
                } else {
                    sb.append("<ERROR_REPORT_FILEPATH>No File</ERROR_REPORT_FILEPATH>");
                }
                if (resultSet.getString("ST_CONTROL_NUMBER") != null && !"".equals(resultSet.getString("ST_CONTROL_NUMBER"))) {
                    sb.append("<ST_CONTROL_NUMBER>" + resultSet.getString("ST_CONTROL_NUMBER") + "</ST_CONTROL_NUMBER>");
                } else {
                    sb.append("<ST_CONTROL_NUMBER>NO</ST_CONTROL_NUMBER>");
                }
                if (resultSet.getString("GS_CONTROL_NUMBER") != null && !"".equals(resultSet.getString("GS_CONTROL_NUMBER"))) {
                    sb.append("<GS_CONTROL_NUMBER>" + resultSet.getString("GS_CONTROL_NUMBER") + "</GS_CONTROL_NUMBER>");
                } else {
                    sb.append("<GS_CONTROL_NUMBER>NO</GS_CONTROL_NUMBER>");
                }
                String sapDetails = DataSourceDataProvider.getInstance().getSapDetails(resultSet.getString("FILE_ID"), resultSet.getString("PO_NUMBER"));
                if (!sapDetails.equals("None")) {
                    sb.append("<SAP_DETAILS>YES</SAP_DETAILS>");
                    String sapDetailsInfo[] = sapDetails.split("\\|");
                    sb.append("<SAP_USER>" + sapDetailsInfo[0] + "</SAP_USER>");
                    sb.append("<IDOC_NUMBER>" + sapDetailsInfo[1] + "</IDOC_NUMBER>");
                    sb.append("<PO_NUMBER>" + sapDetailsInfo[2] + "</PO_NUMBER>");
                    sb.append("<PO_DATE>" + sapDetailsInfo[3] + "</PO_DATE>");
                    sb.append("<IDOC_STATUS_CODE>" + sapDetailsInfo[4] + "</IDOC_STATUS_CODE>");
                    sb.append("<IDOC_STATUS_DESCRIPTION>" + sapDetailsInfo[5] + "</IDOC_STATUS_DESCRIPTION>");
                } else {
                    sb.append("<SAP_DETAILS>NO</SAP_DETAILS>");
                }
                sb.append("</DETAIL>");
                isGetting = true;
            }
            if (!isGetting) {
                isGetting = false;
                sb.append("<DETAIL><VALID>false</VALID></DETAIL>");
            }
            sb.append("</DETAILS>");
            sb.append("</xml>");
        } catch (SQLException sqlException) {
            LoggerUtility.log(logger, "SQLException occurred in getASNDetails method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
        } catch (ServiceLocatorException serviceLocatorException) {
            LoggerUtility.log(logger, "ServiceLocatorException occurred in getASNDetails method:: " + serviceLocatorException.getMessage(), Level.ERROR, serviceLocatorException.getCause());
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                    resultSet = null;
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                    preparedStatement = null;
                }
                if (connection != null) {
                    connection.close();
                    connection = null;
                }
            } catch (SQLException sqlException) {
                LoggerUtility.log(logger, "finally SQLException occurred in getASNDetails method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
            }
        }
        return sb.toString();
    }

    @Override
    public String getInvDetails(String invNumber, String poNumber, String fileID, String database) throws ServiceLocatorException {

        System.out.println("getInvDetails are" + invNumber);
        System.out.println("getInvDetails are" + poNumber);
        System.out.println("getInvDetails are" + fileID);
        System.out.println("getInvDetails are" + database);
        boolean isGetting = false;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        StringBuilder sb = new StringBuilder();
        String invDetailsQuery = "";
        try {
            if ("ARCHIVE".equals(database)) {
                invDetailsQuery = "SELECT ARCHIVE_FILES.FILE_TYPE,ARCHIVE_FILES.TRANSACTION_TYPE as TRANSACTION_TYPE,ARCHIVE_FILES.ST_CONTROL_NUMBER as ST_CONTROL_NUMBER,ARCHIVE_FILES.GS_CONTROL_NUMBER as GS_CONTROL_NUMBER,"
                        + "ARCHIVE_INVOICE.FILE_ID as FILE_ID,ARCHIVE_FILES.SENDER_ID, ARCHIVE_FILES.RECEIVER_ID,ARCHIVE_INVOICE.INVOICE_NUMBER as INVOICE_NUMBER,ARCHIVE_INVOICE.PO_NUMBER as PO_NUMBER,"
                        + "ARCHIVE_INVOICE.ITEM_QTY as ITEM_QTY,ARCHIVE_INVOICE.INVOICE_AMOUNT as INVOICE_AMOUNT,ARCHIVE_INVOICE.INVOICE_DATE as INVOICE_DATE,"
                        + "ARCHIVE_FILES.ISA_NUMBER as ISA_NUMBER,ARCHIVE_FILES.ISA_DATE as ISA_DATE,ARCHIVE_FILES.ISA_TIME as ISA_TIME,"
                        + " ARCHIVE_FILES.PRE_TRANS_FILEPATH,ARCHIVE_FILES.POST_TRANS_FILEPATH,ARCHIVE_FILES.SENDER_ID,ARCHIVE_FILES.RECEIVER_ID,"
                        + "ARCHIVE_FILES.ORG_FILEPATH,ARCHIVE_FILES.ERR_MESSAGE,ARCHIVE_FILES.STATUS,ARCHIVE_FILES.ERROR_REPORT_FILEPATH as ERROR_REPORT_FILEPATH,"
                        + "ARCHIVE_FILES.ACK_FILE_ID as ACK_FILE_ID "
                        + " FROM ARCHIVE_INVOICE LEFT OUTER JOIN ARCHIVE_FILES ON (ARCHIVE_INVOICE.INVOICE_NUMBER = ARCHIVE_FILES.PRI_KEY_VAL AND ARCHIVE_INVOICE.FILE_ID = ARCHIVE_FILES.FILE_ID) "
                        + " WHERE FLOWFLAG like 'M' AND ARCHIVE_INVOICE.INVOICE_NUMBER LIKE '%" + invNumber + "%' AND ARCHIVE_INVOICE.PO_NUMBER LIKE '%" + poNumber + "%'"
                        + " AND ARCHIVE_INVOICE.FILE_ID='" + fileID + "'";
            } else {
                invDetailsQuery = "SELECT FILES.FILE_TYPE,Files.TRANSACTION_TYPE as TRANSACTION_TYPE,FILES.ST_CONTROL_NUMBER as ST_CONTROL_NUMBER,FILES.GS_CONTROL_NUMBER as GS_CONTROL_NUMBER,"
                        + "INVOICE.FILE_ID as FILE_ID,FILES.SENDER_ID,FILES.RECEIVER_ID,INVOICE.INVOICE_NUMBER as INVOICE_NUMBER,INVOICE.PO_NUMBER as PO_NUMBER,"
                        + "INVOICE.ITEM_QTY as ITEM_QTY,INVOICE.INVOICE_AMOUNT as INVOICE_AMOUNT,INVOICE.INVOICE_DATE as INVOICE_DATE,"
                        + "Files.ISA_NUMBER as ISA_NUMBER,Files.ISA_DATE as ISA_DATE,Files.ISA_TIME as ISA_TIME,"
                        + " FILES.PRE_TRANS_FILEPATH,FILES.POST_TRANS_FILEPATH,FILES.SENDER_ID,FILES.RECEIVER_ID,"
                        + "FILES.ORG_FILEPATH,FILES.ERR_MESSAGE,FILES.STATUS,FILES.ERROR_REPORT_FILEPATH as ERROR_REPORT_FILEPATH,"
                        + "FILES.ACK_FILE_ID as ACK_FILE_ID "
                        + " FROM INVOICE LEFT OUTER JOIN FILES ON (INVOICE.INVOICE_NUMBER = FILES.PRI_KEY_VAL AND INVOICE.FILE_ID = FILES.FILE_ID) "
                        + " WHERE FLOWFLAG like 'M' AND INVOICE.INVOICE_NUMBER LIKE '%" + invNumber + "%' AND INVOICE.PO_NUMBER LIKE '%" + poNumber + "%'"
                        + " AND INVOICE.FILE_ID='" + fileID + "'";
            }

            Map<String, String> tradingPartners = DataSourceDataProvider.getInstance().getAllTradingPartners();
            connection = ConnectionProvider.getInstance().getConnection();
            preparedStatement = connection.prepareStatement(invDetailsQuery);
            resultSet = preparedStatement.executeQuery();
            sb.append("<xml version=\"1.0\">");
            sb.append("<DETAILS>");
            while (resultSet.next()) {
                sb.append("<DETAIL><VALID>true</VALID>");
                if (resultSet.getString("FILE_ID") != null && !"".equals(resultSet.getString("FILE_ID"))) {
                    sb.append("<FILEID>" + resultSet.getString("FILE_ID") + "</FILEID>");
                } else {
                    sb.append("<FILEID>--</FILEID>");
                }
                if (resultSet.getString("INVOICE_NUMBER") != null && !"".equals(resultSet.getString("INVOICE_NUMBER"))) {
                    sb.append("<INVNUMBER>" + resultSet.getString("INVOICE_NUMBER") + "</INVNUMBER>");
                } else {
                    sb.append("<INVNUMBER>--</INVNUMBER>");
                }
                if (resultSet.getString("PO_NUMBER") != null && !"".equals(resultSet.getString("PO_NUMBER"))) {
                    sb.append("<PONUMBER>" + resultSet.getString("PO_NUMBER") + "</PONUMBER>");
                } else {
                    sb.append("<PONUMBER>--</PONUMBER>");
                }
                if (resultSet.getString("ITEM_QTY") != null && !"".equals(resultSet.getString("ITEM_QTY"))) {
                    sb.append("<ITEMQTY>" + resultSet.getString("ITEM_QTY") + "</ITEMQTY>");
                } else {
                    sb.append("<ITEMQTY>--</ITEMQTY>");
                }
                if (resultSet.getString("INVOICE_AMOUNT") != null && !"".equals(resultSet.getString("INVOICE_AMOUNT"))) {
                    sb.append("<INVAMT>" + resultSet.getString("INVOICE_AMOUNT") + "</INVAMT>");
                } else {
                    sb.append("<INVAMT>--</INVAMT>");
                }
                if (resultSet.getString("INVOICE_DATE") != null && !"".equals(resultSet.getString("INVOICE_DATE"))) {
                    sb.append("<INVDATE>" + resultSet.getString("INVOICE_DATE") + "</INVDATE>");
                } else {
                    sb.append("<INVDATE>NO</INVDATE>");
                }
                if (resultSet.getString("ISA_NUMBER") != null && !"".equals(resultSet.getString("ISA_NUMBER"))) {
                    sb.append("<ISANUM>" + resultSet.getString("ISA_NUMBER") + "</ISANUM>");
                } else {
                    sb.append("<ISANUM>--</ISANUM>");
                }
                if (resultSet.getString("ISA_DATE") != null && !"".equals(resultSet.getString("ISA_DATE"))) {
                    sb.append("<ISADATE>" + resultSet.getString("ISA_DATE") + "</ISADATE>");
                } else {
                    sb.append("<ISADATE>--</ISADATE>");
                }
                if (resultSet.getString("ISA_TIME") != null && !"".equals(resultSet.getString("ISA_TIME"))) {
                    sb.append("<ISATIME>" + resultSet.getString("ISA_TIME") + "</ISATIME>");
                } else {
                    sb.append("<ISATIME>--</ISATIME>");
                }
                if (resultSet.getString("STATUS") != null && !"".equals(resultSet.getString("STATUS"))) {
                    sb.append("<STATUS>" + resultSet.getString("STATUS") + "</STATUS>");
                } else {
                    sb.append("<STATUS>--</STATUS>");
                }
                if (resultSet.getString("FILE_TYPE") != null && !"".equals(resultSet.getString("FILE_TYPE"))) {
                    sb.append("<FILETYPE>" + resultSet.getString("FILE_TYPE") + "</FILETYPE>");
                } else {
                    sb.append("<FILETYPE>--</FILETYPE>");
                }
                if (resultSet.getString("SENDER_ID") != null && !"".equals(resultSet.getString("SENDER_ID"))) {
                    sb.append("<SENDER_ID>" + resultSet.getString("SENDER_ID") + "</SENDER_ID>");
                } else {
                    sb.append("<SENDER_ID>--</SENDER_ID>");
                }
                if (resultSet.getString("RECEIVER_ID") != null && !"".equals(resultSet.getString("RECEIVER_ID"))) {
                    sb.append("<RECEIVER_ID>" + resultSet.getString("RECEIVER_ID") + "</RECEIVER_ID>");
                } else {
                    sb.append("<RECEIVER_ID>--</RECEIVER_ID>");
                }
                if (resultSet.getString("SENDER_ID") != null && (((tradingPartners.get(resultSet.getString("SENDER_ID")))) != null)) {
                    sb.append("<SENDER_NAME>" + (tradingPartners.get(resultSet.getString("SENDER_ID"))).toString() + "</SENDER_NAME>");
                } else {
                    sb.append("<SENDER_NAME>--</SENDER_NAME>");
                }
                if (resultSet.getString("RECEIVER_ID") != null && (((tradingPartners.get(resultSet.getString("RECEIVER_ID")))) != null)) {
                    sb.append("<RECEIVER_NAME>" + (tradingPartners.get(resultSet.getString("RECEIVER_ID"))).toString() + "</RECEIVER_NAME>");
                } else {
                    sb.append("<RECEIVER_NAME>--</RECEIVER_NAME>");
                }
                if (resultSet.getString("PRE_TRANS_FILEPATH") != null) {
                    if (new File(resultSet.getString("PRE_TRANS_FILEPATH")).exists() && new File(resultSet.getString("PRE_TRANS_FILEPATH")).isFile()) {
                        sb.append("<PRETRANSFILEPATH>" + resultSet.getString("PRE_TRANS_FILEPATH") + "</PRETRANSFILEPATH>");
                    } else {
                        sb.append("<PRETRANSFILEPATH>No File</PRETRANSFILEPATH>");
                    }
                } else {
                    sb.append("<PRETRANSFILEPATH>No File</PRETRANSFILEPATH>");
                }
                if (resultSet.getString("POST_TRANS_FILEPATH") != null) {
                    if (new File(resultSet.getString("POST_TRANS_FILEPATH")).exists() && new File(resultSet.getString("POST_TRANS_FILEPATH")).isFile()) {
                        sb.append("<POSTTRANSFILEPATH>" + resultSet.getString("POST_TRANS_FILEPATH") + "</POSTTRANSFILEPATH>");
                    } else {
                        sb.append("<POSTTRANSFILEPATH>No File</POSTTRANSFILEPATH>");
                    }
                } else {
                    sb.append("<POSTTRANSFILEPATH>No File</POSTTRANSFILEPATH>");
                }
                if (resultSet.getString("ORG_FILEPATH") != null) {
                    if (new File(resultSet.getString("ORG_FILEPATH")).exists() && new File(resultSet.getString("ORG_FILEPATH")).isFile()) {
                        sb.append("<ORGFILEPATH>" + resultSet.getString("ORG_FILEPATH") + "</ORGFILEPATH>");
                    } else {
                        sb.append("<ORGFILEPATH>No File</ORGFILEPATH>");
                    }
                } else {
                    sb.append("<ORGFILEPATH>No File</ORGFILEPATH>");
                }
                if (resultSet.getString("ACK_FILE_ID") != null) {
                    if (new File(resultSet.getString("ACK_FILE_ID")).exists() && new File(resultSet.getString("ACK_FILE_ID")).isFile()) {
                        sb.append("<ACKFILEID>" + resultSet.getString("ACK_FILE_ID") + "</ACKFILEID>");
                    } else {
                        sb.append("<ACKFILEID>No File</ACKFILEID>");
                    }
                } else {
                    sb.append("<ACKFILEID>No File</ACKFILEID>");
                }
                if (resultSet.getString("ERR_MESSAGE") != null && !"".equals(resultSet.getString("ERR_MESSAGE"))) {
                    sb.append("<ERR_MESSAGE>" + resultSet.getString("ERR_MESSAGE") + "</ERR_MESSAGE>");
                } else {
                    sb.append("<ERR_MESSAGE>NO MSG</ERR_MESSAGE>");
                }
                if (resultSet.getString("ERROR_REPORT_FILEPATH") != null) {
                    if (new File(resultSet.getString("ERROR_REPORT_FILEPATH")).exists() && new File(resultSet.getString("ERROR_REPORT_FILEPATH")).isFile()) {
                        sb.append("<ERROR_REPORT_FILEPATH>" + resultSet.getString("ERROR_REPORT_FILEPATH") + "</ERROR_REPORT_FILEPATH>");
                    } else {
                        sb.append("<ERROR_REPORT_FILEPATH>No File</ERROR_REPORT_FILEPATH>");
                    }
                } else {
                    sb.append("<ERROR_REPORT_FILEPATH>No File</ERROR_REPORT_FILEPATH>");
                }
                if (resultSet.getString("TRANSACTION_TYPE") != null && !"".equals(resultSet.getString("TRANSACTION_TYPE"))) {
                    sb.append("<TRANSACTION_TYPE>" + resultSet.getString("TRANSACTION_TYPE") + "</TRANSACTION_TYPE>");
                } else {
                    sb.append("<TRANSACTION_TYPE>--</TRANSACTION_TYPE>");
                }
                if (resultSet.getString("ST_CONTROL_NUMBER") != null && !"".equals(resultSet.getString("ST_CONTROL_NUMBER"))) {
                    sb.append("<ST_CONTROL_NUMBER>" + resultSet.getString("ST_CONTROL_NUMBER") + "</ST_CONTROL_NUMBER>");
                } else {
                    sb.append("<ST_CONTROL_NUMBER>--</ST_CONTROL_NUMBER>");
                }
                if (resultSet.getString("GS_CONTROL_NUMBER") != null && !"".equals(resultSet.getString("GS_CONTROL_NUMBER"))) {
                    sb.append("<GS_CONTROL_NUMBER>" + resultSet.getString("GS_CONTROL_NUMBER") + "</GS_CONTROL_NUMBER>");
                } else {
                    sb.append("<GS_CONTROL_NUMBER>--</GS_CONTROL_NUMBER>");
                }
                String sapDetails = DataSourceDataProvider.getInstance().getSapDetails(resultSet.getString("FILE_ID"), resultSet.getString("PO_NUMBER"));

                if (!sapDetails.equals("None")) {
                    sb.append("<SAP_DETAILS>YES</SAP_DETAILS>");
                    String sapDetailsInfo[] = sapDetails.split("\\|");
                    sb.append("<SAP_USER>" + sapDetailsInfo[0] + "</SAP_USER>");
                    sb.append("<IDOC_NUMBER>" + sapDetailsInfo[1] + "</IDOC_NUMBER>");
                    sb.append("<PO_NUMBER>" + sapDetailsInfo[2] + "</PO_NUMBER>");
                    sb.append("<PO_DATE>" + sapDetailsInfo[3] + "</PO_DATE>");
                    sb.append("<IDOC_STATUS_CODE>" + sapDetailsInfo[4] + "</IDOC_STATUS_CODE>");
                    sb.append("<IDOC_STATUS_DESCRIPTION>" + sapDetailsInfo[5] + "</IDOC_STATUS_DESCRIPTION>");
                } else {
                    sb.append("<SAP_DETAILS>NO</SAP_DETAILS>");
                }
                sb.append("</DETAIL>");
                isGetting = true;
            }
            if (!isGetting) {
                isGetting = false;
                sb.append("<DETAIL><VALID>false</VALID></DETAIL>");
            }
            sb.append("</DETAILS>");
            sb.append("</xml>");
        } catch (SQLException sqlException) {
            LoggerUtility.log(logger, "SQLException occurred in getInvDetails method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
        } catch (ServiceLocatorException serviceLocatorException) {
            LoggerUtility.log(logger, "ServiceLocatorException occurred in getInvDetails method:: " + serviceLocatorException.getMessage(), Level.ERROR, serviceLocatorException.getCause());
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                    resultSet = null;
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                    preparedStatement = null;
                }
                if (connection != null) {
                    connection.close();
                    connection = null;
                }
            } catch (SQLException sqlException) {
                LoggerUtility.log(logger, "finally SQLException occurred in getInvDetails method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
            }
        }
        return sb.toString();
    }

    @Override
    public String getDocDetails(String instanceid, String ponum, int id, String database) throws ServiceLocatorException {
        System.out.println("getDocDetails are " + instanceid);
        System.out.println("getDocDetails are " + ponum);
        System.out.println("getDocDetails are " + id);
        System.out.println("getDocDetails are " + database);
        boolean isGetting = false;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        StringBuilder sb = new StringBuilder();
        String docDetailsQuery = "";

        try {
            if ("ARCHIVE".equals(database)) {
                docDetailsQuery = "select ARCHIVE_FILES.STATUS,ARCHIVE_FILES.DIRECTION as DIRECTION,ARCHIVE_FILES.FILE_ID,ARCHIVE_FILES.FILE_TYPE,ARCHIVE_FILES.SENDER_ID,ARCHIVE_FILES.RECEIVER_ID,"
                        + "ARCHIVE_FILES.PRE_TRANS_FILEPATH,ARCHIVE_FILES.POST_TRANS_FILEPATH,ARCHIVE_FILES.SEC_KEY_VAL as SEC_KEY_VAL,ARCHIVE_FILES.PRI_KEY_TYPE as PRI_KEY_TYPE,"
                        + "ARCHIVE_FILES.PRI_KEY_VAL as PRI_KEY_VAL,ARCHIVE_FILES.ORG_FILEPATH as ORG_FILEPATH,ARCHIVE_FILES.ISA_NUMBER as ISA_NUMBER,ARCHIVE_FILES.ISA_DATE as ISA_DATE,"
                        + "ARCHIVE_FILES.ISA_TIME as ISA_TIME,ARCHIVE_FILES.TRANSACTION_TYPE as TRANSACTION_TYPE,ARCHIVE_FILES.GS_CONTROL_NUMBER as GS_CONTROL_NUMBER,"
                        + "ARCHIVE_FILES.ST_CONTROL_NUMBER as ST_CONTROL_NUMBER,TP1.NAME as SENDER_NAME,TP2.NAME as RECEIVER_NAME,ARCHIVE_FILES.ERR_MESSAGE,"
                        + "ARCHIVE_FILES.ACK_FILE_ID as ACK_FILE_ID, ARCHIVE_FILES.ERROR_REPORT_FILEPATH as ERROR_REPORT_FILEPATH from ARCHIVE_FILES "
                        + "LEFT OUTER JOIN TP TP1 ON (TP1.ID = ARCHIVE_FILES.SENDER_ID) "
                        + "LEFT OUTER JOIN TP TP2 ON (TP2.ID = ARCHIVE_FILES.RECEIVER_ID) "
                        + "where FLOWFLAG like 'M' AND ARCHIVE_FILES.FILE_ID LIKE '%" + instanceid + "%' AND ARCHIVE_FILES.ID =" + id;
            } else {
                docDetailsQuery = "select FILES.STATUS,FILES.DIRECTION as DIRECTION,FILES.FILE_ID,FILES.FILE_TYPE,FILES.SENDER_ID,FILES.RECEIVER_ID,"
                        + "FILES.PRE_TRANS_FILEPATH,FILES.POST_TRANS_FILEPATH,FILES.SEC_KEY_VAL as SEC_KEY_VAL,FILES.PRI_KEY_TYPE as PRI_KEY_TYPE,"
                        + "FILES.PRI_KEY_VAL as PRI_KEY_VAL,FILES.ORG_FILEPATH as ORG_FILEPATH,FILES.ISA_NUMBER as ISA_NUMBER,FILES.ISA_DATE as ISA_DATE,"
                        + "FILES.ISA_TIME as ISA_TIME,FILES.TRANSACTION_TYPE as TRANSACTION_TYPE,FILES.GS_CONTROL_NUMBER as GS_CONTROL_NUMBER,"
                        + "FILES.ST_CONTROL_NUMBER as ST_CONTROL_NUMBER,TP1.NAME as SENDER_NAME,TP2.NAME as RECEIVER_NAME,FILES.ERR_MESSAGE,"
                        + "FILES.ACK_FILE_ID as ACK_FILE_ID, FILES.ERROR_REPORT_FILEPATH as ERROR_REPORT_FILEPATH from FILES "
                        + "LEFT OUTER JOIN TP TP1 ON (TP1.ID=FILES.SENDER_ID) "
                        + "LEFT OUTER JOIN TP TP2 ON (TP2.ID = FILES.RECEIVER_ID) "
                        + "where FLOWFLAG like 'M' AND FILES.FILE_ID LIKE '%" + instanceid + "%' AND FILES.ID =" + id;
            }
            connection = ConnectionProvider.getInstance().getConnection();
            preparedStatement = connection.prepareStatement(docDetailsQuery);
            resultSet = preparedStatement.executeQuery();
            sb.append("<xml version=\"1.0\">");
            sb.append("<DETAILS>");
            while (resultSet.next()) {
                sb.append("<DETAIL><VALID>true</VALID>");
                if (resultSet.getString("FILE_ID") != null && !"".equals(resultSet.getString("FILE_ID"))) {
                    sb.append("<FILEID>" + resultSet.getString("FILE_ID") + "</FILEID>");
                } else {
                    sb.append("<FILEID>--</FILEID>");
                }
                if (resultSet.getString("FILE_TYPE") != null && !"".equals(resultSet.getString("FILE_TYPE"))) {
                    sb.append("<FILETYPE>" + resultSet.getString("FILE_TYPE") + "</FILETYPE>");
                } else {
                    sb.append("<FILETYPE>--</FILETYPE>");
                }
                if (resultSet.getString("SENDER_ID") != null && !"".equals(resultSet.getString("SENDER_ID"))) {
                    sb.append("<SENDERID>" + resultSet.getString("SENDER_ID") + "</SENDERID>");
                } else {
                    sb.append("<SENDERID>--</SENDERID>");
                }
                if (resultSet.getString("RECEIVER_ID") != null && !"".equals(resultSet.getString("RECEIVER_ID"))) {
                    sb.append("<RECEIVERID>" + resultSet.getString("RECEIVER_ID") + "</RECEIVERID>");
                } else {
                    sb.append("<RECEIVERID>--</RECEIVERID>");
                }
                if (resultSet.getString("SENDER_NAME") == null) {
                    sb.append("<SENDER_NAME>--</SENDER_NAME>");
                } else {
                    sb.append("<SENDER_NAME>" + resultSet.getString("SENDER_NAME") + "</SENDER_NAME>");
                }
                if (resultSet.getString("RECEIVER_NAME") == null) {
                    sb.append("<RECEIVER_NAME>--</RECEIVER_NAME>");
                } else {
                    sb.append("<RECEIVER_NAME>" + resultSet.getString("RECEIVER_NAME") + "</RECEIVER_NAME>");
                }
                if (resultSet.getString("DIRECTION") != null && !"".equals(resultSet.getString("DIRECTION"))) {
                    sb.append("<DIRECTION>" + resultSet.getString("DIRECTION").toLowerCase() + "</DIRECTION>");
                } else {
                    sb.append("<DIRECTION>--</DIRECTION>");
                }
                if (resultSet.getString("ISA_NUMBER") != null && !"".equals(resultSet.getString("ISA_NUMBER"))) {
                    sb.append("<ISA_NUMBER>" + resultSet.getString("ISA_NUMBER") + "</ISA_NUMBER>");
                } else {
                    sb.append("<ISA_NUMBER>--</ISA_NUMBER>");
                }
                if (resultSet.getString("ISA_DATE") != null && !"".equals(resultSet.getString("ISA_DATE"))) {
                    sb.append("<ISA_DATE>" + resultSet.getString("ISA_DATE") + "</ISA_DATE>");
                } else {
                    sb.append("<ISA_DATE>--</ISA_DATE>");
                }
                if (resultSet.getString("ISA_TIME") != null && !"".equals(resultSet.getString("ISA_TIME"))) {
                    sb.append("<ISA_TIME>" + resultSet.getString("ISA_TIME") + "</ISA_TIME>");
                } else {
                    sb.append("<ISA_TIME>--</ISA_TIME>");
                }
                if (resultSet.getString("GS_CONTROL_NUMBER") != null && !"".equalsIgnoreCase(resultSet.getString("GS_CONTROL_NUMBER"))) {
                    sb.append("<GS_CONTROL_NUMBER>" + resultSet.getString("GS_CONTROL_NUMBER") + "</GS_CONTROL_NUMBER>");
                } else {
                    sb.append("<GS_CONTROL_NUMBER>--</GS_CONTROL_NUMBER>");
                }
                if (resultSet.getString("ST_CONTROL_NUMBER") != null && !"".equals(resultSet.getString("ST_CONTROL_NUMBER"))) {
                    sb.append("<ST_CONTROL_NUMBER>" + resultSet.getString("ST_CONTROL_NUMBER") + "</ST_CONTROL_NUMBER>");
                } else {
                    sb.append("<ST_CONTROL_NUMBER>--</ST_CONTROL_NUMBER>");
                }
                if (resultSet.getString("TRANSACTION_TYPE") != null && !"".equals(resultSet.getString("TRANSACTION_TYPE"))) {
                    sb.append("<TRANSACTION_TYPE>" + resultSet.getString("TRANSACTION_TYPE") + "</TRANSACTION_TYPE>");
                } else {
                    sb.append("<TRANSACTION_TYPE>--</TRANSACTION_TYPE>");
                }
                if (resultSet.getString("STATUS") != null && !"".equals(resultSet.getString("STATUS"))) {
                    sb.append("<STATUS>" + resultSet.getString("STATUS") + "</STATUS>");
                } else {
                    sb.append("<STATUS>--</STATUS>");
                }
                if (resultSet.getString("SEC_KEY_VAL") != null && !"".equalsIgnoreCase(resultSet.getString("SEC_KEY_VAL"))) {
                    sb.append("<SEC_KEY_VAL>" + resultSet.getString("SEC_KEY_VAL") + "</SEC_KEY_VAL>");
                } else {
                    sb.append("<SEC_KEY_VAL>--</SEC_KEY_VAL>");
                }
                if (resultSet.getString("PRI_KEY_TYPE") != null && resultSet.getString("PRI_KEY_TYPE").equalsIgnoreCase("PO")) {
                    sb.append("<PRI_KEY_TYPE>PO</PRI_KEY_TYPE>");
                } else if (resultSet.getString("PRI_KEY_TYPE") != null && resultSet.getString("PRI_KEY_TYPE").equalsIgnoreCase("ASN")) {
                    sb.append("<PRI_KEY_TYPE> ASN </PRI_KEY_TYPE>");
                } else if (resultSet.getString("PRI_KEY_TYPE") != null && resultSet.getString("PRI_KEY_TYPE").equalsIgnoreCase("IN")) {
                    sb.append("<PRI_KEY_TYPE> Invoice </PRI_KEY_TYPE>");
                } else if (resultSet.getString("PRI_KEY_TYPE") != null && resultSet.getString("PRI_KEY_TYPE").equalsIgnoreCase("PAYMENT")) {
                    sb.append("<PRI_KEY_TYPE> Cheque </PRI_KEY_TYPE>");
                } else if (resultSet.getString("PRI_KEY_TYPE") != null && resultSet.getString("PRI_KEY_TYPE").equalsIgnoreCase("Inventory")) {
                    sb.append("<PRI_KEY_TYPE> Inventory </PRI_KEY_TYPE>");
                } else {
                    sb.append("<PRI_KEY_TYPE>--</PRI_KEY_TYPE>");
                }
                if (resultSet.getString("PRI_KEY_VAL") != null && !"".equals(resultSet.getString("PRI_KEY_VAL"))) {
                    sb.append("<PRI_KEY_VAL>" + resultSet.getString("PRI_KEY_VAL") + "</PRI_KEY_VAL>");
                } else {
                    sb.append("<PRI_KEY_VAL>--</PRI_KEY_VAL>");
                }
                if (resultSet.getString("PRE_TRANS_FILEPATH") != null) {
                    if (new File(resultSet.getString("PRE_TRANS_FILEPATH")).exists() && new File(resultSet.getString("PRE_TRANS_FILEPATH")).isFile()) {
                        sb.append("<PRETRANSFILEPATH>" + resultSet.getString("PRE_TRANS_FILEPATH") + "</PRETRANSFILEPATH>");
                    } else {
                        sb.append("<PRETRANSFILEPATH>No File</PRETRANSFILEPATH>");
                    }
                } else {
                    sb.append("<PRETRANSFILEPATH>No File</PRETRANSFILEPATH>");
                }

                if (resultSet.getString("POST_TRANS_FILEPATH") != null) {
                    if (new File(resultSet.getString("POST_TRANS_FILEPATH")).exists() && new File(resultSet.getString("POST_TRANS_FILEPATH")).isFile()) {
                        sb.append("<POSTTRANSFILEPATH>" + resultSet.getString("POST_TRANS_FILEPATH") + "</POSTTRANSFILEPATH>");
                    } else {
                        sb.append("<POSTTRANSFILEPATH>No File</POSTTRANSFILEPATH>");
                    }
                } else {
                    sb.append("<POSTTRANSFILEPATH>No File</POSTTRANSFILEPATH>");
                }
                if (resultSet.getString("ORG_FILEPATH") != null) {
                    if (new File(resultSet.getString("ORG_FILEPATH")).exists() && new File(resultSet.getString("ORG_FILEPATH")).isFile()) {
                        sb.append("<ORG_FILEPATH>" + resultSet.getString("ORG_FILEPATH") + "</ORG_FILEPATH>");
                    } else {
                        sb.append("<ORG_FILEPATH>No File</ORG_FILEPATH>");
                    }
                } else {
                    sb.append("<ORG_FILEPATH>No File</ORG_FILEPATH>");
                }
                if (resultSet.getString("ACK_FILE_ID") != null) {
                    if (new File(resultSet.getString("ACK_FILE_ID")).exists() && new File(resultSet.getString("ACK_FILE_ID")).isFile()) {
                        sb.append("<ACKFILEID>" + resultSet.getString("ACK_FILE_ID") + "</ACKFILEID>");
                    } else {
                        sb.append("<ACKFILEID>No File</ACKFILEID>");
                    }
                } else {
                    sb.append("<ACKFILEID>No File</ACKFILEID>");
                }
                if (resultSet.getString("ERR_MESSAGE") != null && !"".equals(resultSet.getString("ERR_MESSAGE"))) {
                    sb.append("<ERR_MESSAGE>" + resultSet.getString("ERR_MESSAGE") + "</ERR_MESSAGE>");
                } else {
                    sb.append("<ERR_MESSAGE>NO MSG</ERR_MESSAGE>");
                }
                if (resultSet.getString("ERROR_REPORT_FILEPATH") != null) {
                    if (new File(resultSet.getString("ERROR_REPORT_FILEPATH")).exists() && new File(resultSet.getString("ERROR_REPORT_FILEPATH")).isFile()) {
                        sb.append("<ERROR_REPORT_FILEPATH>" + resultSet.getString("ERROR_REPORT_FILEPATH") + "</ERROR_REPORT_FILEPATH>");
                    } else {
                        sb.append("<ERROR_REPORT_FILEPATH>No File</ERROR_REPORT_FILEPATH>");
                    }
                } else {
                    sb.append("<ERROR_REPORT_FILEPATH>No File</ERROR_REPORT_FILEPATH>");
                }
                String sapDetails = DataSourceDataProvider.getInstance().getSapDetails(instanceid, ponum);
                if (!sapDetails.equals("None")) {
                    sb.append("<SAP_DETAILS>YES</SAP_DETAILS>");
                    String sapDetailsInfo[] = sapDetails.split("\\|");
                    sb.append("<SAP_USER>" + sapDetailsInfo[0] + "</SAP_USER>");
                    sb.append("<IDOC_NUMBER>" + sapDetailsInfo[1] + "</IDOC_NUMBER>");
                    sb.append("<PO_NUMBER>" + sapDetailsInfo[2] + "</PO_NUMBER>");
                    sb.append("<PO_DATE>" + sapDetailsInfo[3] + "</PO_DATE>");
                    sb.append("<IDOC_STATUS_CODE>" + sapDetailsInfo[4] + "</IDOC_STATUS_CODE>");
                    sb.append("<IDOC_STATUS_DESCRIPTION>" + sapDetailsInfo[5] + "</IDOC_STATUS_DESCRIPTION>");
                } else {
                    sb.append("<SAP_DETAILS>NO</SAP_DETAILS>");
                }
                sb.append("</DETAIL>");
                isGetting = true;
            }
            if (!isGetting) {
                isGetting = false;
                sb.append("<DETAIL><VALID>false</VALID></DETAIL>");
            }
            sb.append("</DETAILS>");
            sb.append("</xml>");
        } catch (SQLException sqlException) {
            LoggerUtility.log(logger, "SQLException occurred in getDocDetails method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
        } catch (ServiceLocatorException serviceLocatorException) {
            LoggerUtility.log(logger, "ServiceLocatorException occurred in getDocDetails method:: " + serviceLocatorException.getMessage(), Level.ERROR, serviceLocatorException.getCause());
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                    resultSet = null;
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                    preparedStatement = null;
                }
                if (connection != null) {
                    connection.close();
                    connection = null;
                }
            } catch (SQLException sqlException) {
                LoggerUtility.log(logger, "finally SQLException occurred in getDocDetails method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
            }
        }
        return sb.toString();
    }

    @Override
    public String getPaymentDetails(String fileId, String database) throws ServiceLocatorException {
        System.out.println("getPaymentDetails is"+fileId);
        System.out.println("getPaymentDetails is"+database);
        boolean isGetting = false;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        StringBuilder sb = new StringBuilder();
        String paymentDetailsQuery = "";
        if ("ARCHIVE".equals(database)) {
            paymentDetailsQuery = "SELECT ARCHIVE_FILES.FILE_TYPE,ARCHIVE_FILES.ISA_NUMBER,ARCHIVE_FILES.GS_CONTROL_NUMBER,ARCHIVE_FILES.ST_CONTROL_NUMBER,"
                    + "ARCHIVE_FILES.TRANSACTION_TYPE,ARCHIVE_FILES.SENDER_ID,ARCHIVE_FILES.RECEIVER_ID,ARCHIVE_FILES.SEC_KEY_VAL,ARCHIVE_PAYMENT.INVOICE_NUMBER,ARCHIVE_FILES.FILE_ID as FILE_ID, ARCHIVE_PAYMENT.Check_Number as Check_Number, "
                    + "ARCHIVE_FILES.SENDER_ID as SENDER_ID, ARCHIVE_FILES.RECEIVER_ID as RECEIVER_ID,"
                    + "ARCHIVE_FILES.PRE_TRANS_FILEPATH as PRE_TRANS_FILEPATH, ARCHIVE_FILES.POST_TRANS_FILEPATH as POST_TRANS_FILEPATH, "
                    + "ARCHIVE_FILES.ORG_FILEPATH as ORG_FILEPATH,ARCHIVE_FILES.ISA_DATE as ISA_DATE,ARCHIVE_FILES.ISA_TIME as ISA_TIME,ARCHIVE_FILES.ERROR_REPORT_FILEPATH as ERROR_REPORT_FILEPATH,"
                    + "ARCHIVE_FILES.ERR_MESSAGE as ERR_MESSAGE,ARCHIVE_FILES.STATUS as STATUS, ARCHIVE_FILES.ACK_FILE_ID as ACK_FILE_ID "
                    + "FROM ARCHIVE_PAYMENT LEFT OUTER JOIN ARCHIVE_FILES ON (ARCHIVE_PAYMENT.FILE_ID=ARCHIVE_FILES.FILE_ID) "
                    + "WHERE FLOWFLAG like 'M' AND ARCHIVE_FILES.FILE_ID = '" + fileId + "'";
        } else {
            paymentDetailsQuery = "SELECT FILES.FILE_TYPE,FILES.ISA_NUMBER,FILES.GS_CONTROL_NUMBER,FILES.ST_CONTROL_NUMBER,FILES.TRANSACTION_TYPE,FILES.SEC_KEY_VAL,PAYMENT.INVOICE_NUMBER,FILES.FILE_ID as FILE_ID, PAYMENT.Check_Number as Check_Number, "
                    + "FILES.SENDER_ID as SENDER_ID, FILES.RECEIVER_ID as RECEIVER_ID,"
                    + "FILES.PRE_TRANS_FILEPATH as PRE_TRANS_FILEPATH,FILES.SENDER_ID,FILES.RECEIVER_ID, FILES.POST_TRANS_FILEPATH as POST_TRANS_FILEPATH, "
                    + "FILES.ORG_FILEPATH as ORG_FILEPATH,FILES.ISA_DATE as ISA_DATE,FILES.ISA_TIME as ISA_TIME,FILES.ERROR_REPORT_FILEPATH as ERROR_REPORT_FILEPATH,"
                    + "FILES.ERR_MESSAGE as ERR_MESSAGE,FILES.STATUS as STATUS, FILES.ACK_FILE_ID as ACK_FILE_ID "
                    + "FROM Payment LEFT OUTER JOIN FILES ON (PAYMENT.FILE_ID=FILES.FILE_ID) "
                    + "WHERE FLOWFLAG like 'M' AND FILES.FILE_ID = '" + fileId + "'";
        }

        try {
            Map<String, String> tradingPartners = DataSourceDataProvider.getInstance().getAllTradingPartners();
            connection = ConnectionProvider.getInstance().getConnection();
            preparedStatement = connection.prepareStatement(paymentDetailsQuery);
            resultSet = preparedStatement.executeQuery();
            sb.append("<xml version=\"1.0\">");
            sb.append("<DETAILS>");
            while (resultSet.next()) {
                sb.append("<DETAIL><VALID>true</VALID>");
                if (resultSet.getString("FILE_ID") != null && !"".equals(resultSet.getString("FILE_ID"))) {
                    sb.append("<FILE_ID>" + resultSet.getString("FILE_ID") + "</FILE_ID>");
                } else {
                    sb.append("<FILE_ID>--</FILE_ID>");
                }
                if (resultSet.getString("ISA_DATE") != null && !"".equals(resultSet.getString("ISA_DATE"))) {
                    sb.append("<ISA_DATE>" + resultSet.getString("ISA_DATE") + "</ISA_DATE>");
                } else {
                    sb.append("<ISA_DATE>--</ISA_DATE>");
                }
                if (resultSet.getString("ISA_TIME") != null && !"".equals(resultSet.getString("ISA_TIME"))) {
                    sb.append("<ISA_TIME>" + resultSet.getString("ISA_TIME") + "</ISA_TIME>");
                } else {
                    sb.append("<ISA_TIME>--</ISA_TIME>");
                }
                if (resultSet.getString("SENDER_ID") != null && !"".equals(resultSet.getString("SENDER_ID"))) {
                    sb.append("<SENDER_ID>" + resultSet.getString("SENDER_ID") + "</SENDER_ID>");
                } else {
                    sb.append("<SENDER_ID>--</SENDER_ID>");
                }
                if (resultSet.getString("RECEIVER_ID") != null && !"".equals(resultSet.getString("RECEIVER_ID"))) {
                    sb.append("<RECEIVER_ID>" + resultSet.getString("RECEIVER_ID") + "</RECEIVER_ID>");
                } else {
                    sb.append("<RECEIVER_ID>--</RECEIVER_ID>");
                }
                if (resultSet.getString("FILE_TYPE") != null && !"".equals(resultSet.getString("FILE_TYPE"))) {
                    sb.append("<FILE_TYPE>" + resultSet.getString("FILE_TYPE") + "</FILE_TYPE>");
                } else {
                    sb.append("<FILE_TYPE>--</FILE_TYPE>");
                }
                if (resultSet.getString("SENDER_ID") != null && (((tradingPartners.get(resultSet.getString("SENDER_ID")))) != null)) {
                    sb.append("<SENDER_NAME>" + (tradingPartners.get(resultSet.getString("SENDER_ID"))).toString() + "</SENDER_NAME>");
                } else {
                    sb.append("<SENDER_NAME>--</SENDER_NAME>");
                }
                if (resultSet.getString("RECEIVER_ID") != null && (((tradingPartners.get(resultSet.getString("RECEIVER_ID")))) != null)) {
                    sb.append("<RECEIVER_NAME>" + (tradingPartners.get(resultSet.getString("RECEIVER_ID"))).toString() + "</RECEIVER_NAME>");
                } else {
                    sb.append("<RECEIVER_NAME>--</RECEIVER_NAME>");
                }
                if (resultSet.getString("STATUS") != null && !"".equals(resultSet.getString("STATUS"))) {
                    sb.append("<STATUS>" + resultSet.getString("STATUS") + "</STATUS>");
                } else {
                    sb.append("<STATUS>--</STATUS>");
                }
                if (resultSet.getString("Check_Number") != null && !"".equals(resultSet.getString("Check_Number"))) {
                    sb.append("<Check_Number>" + resultSet.getString("Check_Number") + "</Check_Number>");
                } else {
                    sb.append("<Check_Number>NO</Check_Number>");
                }
                if (resultSet.getString("ISA_NUMBER") != null && !"".equals(resultSet.getString("ISA_NUMBER"))) {
                    sb.append("<ISA_NUMBER>" + resultSet.getString("ISA_NUMBER") + "</ISA_NUMBER>");
                } else {
                    sb.append("<ISA_NUMBER>--</ISA_NUMBER>");
                }
                if (resultSet.getString("GS_CONTROL_NUMBER") != null && !"".equals(resultSet.getString("GS_CONTROL_NUMBER"))) {
                    sb.append("<GS_CONTROL_NUMBER>" + resultSet.getString("GS_CONTROL_NUMBER") + "</GS_CONTROL_NUMBER>");
                } else {
                    sb.append("<GS_CONTROL_NUMBER>--</GS_CONTROL_NUMBER>");
                }
                if (resultSet.getString("ST_CONTROL_NUMBER") != null && !"".equals(resultSet.getString("ST_CONTROL_NUMBER"))) {
                    sb.append("<ST_CONTROL_NUMBER>" + resultSet.getString("ST_CONTROL_NUMBER") + "</ST_CONTROL_NUMBER>");
                } else {
                    sb.append("<ST_CONTROL_NUMBER>--</ST_CONTROL_NUMBER>");
                }
                if (resultSet.getString("TRANSACTION_TYPE") != null && !"".equals(resultSet.getString("TRANSACTION_TYPE"))) {
                    sb.append("<TRANSACTION_TYPE>" + resultSet.getString("TRANSACTION_TYPE") + "</TRANSACTION_TYPE>");
                } else {
                    sb.append("<TRANSACTION_TYPE>--</TRANSACTION_TYPE>");
                }
                if (resultSet.getString("SEC_KEY_VAL") != null && !"".equals(resultSet.getString("SEC_KEY_VAL"))) {
                    sb.append("<SEC_KEY_VAL>" + resultSet.getString("SEC_KEY_VAL") + "</SEC_KEY_VAL>");
                } else {
                    sb.append("<SEC_KEY_VAL>NO</SEC_KEY_VAL>");
                }
                if (resultSet.getString("INVOICE_NUMBER") != null && !"".equals(resultSet.getString("INVOICE_NUMBER"))) {
                    sb.append("<INVOICE_NUMBER>" + resultSet.getString("INVOICE_NUMBER") + "</INVOICE_NUMBER>");
                } else {
                    sb.append("<INVOICE_NUMBER>NO</INVOICE_NUMBER>");
                }
                if (resultSet.getString("PRE_TRANS_FILEPATH") != null) {
                    if (new File(resultSet.getString("PRE_TRANS_FILEPATH")).exists() && new File(resultSet.getString("PRE_TRANS_FILEPATH")).isFile()) {
                        sb.append("<PRETRANSFILEPATH>" + resultSet.getString("PRE_TRANS_FILEPATH") + "</PRETRANSFILEPATH>");
                    } else {
                        sb.append("<PRETRANSFILEPATH>No File</PRETRANSFILEPATH>");
                    }
                } else {
                    sb.append("<PRETRANSFILEPATH>No File</PRETRANSFILEPATH>");
                }
                if (resultSet.getString("POST_TRANS_FILEPATH") != null) {
                    if (new File(resultSet.getString("POST_TRANS_FILEPATH")).exists() && new File(resultSet.getString("POST_TRANS_FILEPATH")).isFile()) {
                        sb.append("<POSTTRANSFILEPATH>" + resultSet.getString("POST_TRANS_FILEPATH") + "</POSTTRANSFILEPATH>");
                    } else {
                        sb.append("<POSTTRANSFILEPATH>No File</POSTTRANSFILEPATH>");
                    }
                } else {
                    sb.append("<POSTTRANSFILEPATH>No File</POSTTRANSFILEPATH>");
                }
                if (resultSet.getString("ORG_FILEPATH") != null) {
                    if (new File(resultSet.getString("ORG_FILEPATH")).exists() && new File(resultSet.getString("ORG_FILEPATH")).isFile()) {
                        sb.append("<ORGFILEPATH>" + resultSet.getString("ORG_FILEPATH") + "</ORGFILEPATH>");
                    } else {
                        sb.append("<ORGFILEPATH>No File</ORGFILEPATH>");
                    }
                } else {
                    sb.append("<ORGFILEPATH>No File</ORGFILEPATH>");
                }
                if (resultSet.getString("ACK_FILE_ID") != null) {
                    if (new File(resultSet.getString("ACK_FILE_ID")).exists() && new File(resultSet.getString("ACK_FILE_ID")).isFile()) {
                        sb.append("<ACKFILE>" + resultSet.getString("ACK_FILE_ID") + "</ACKFILE>");
                    } else {
                        sb.append("<ACKFILE>No File</ACKFILE>");
                    }
                } else {
                    sb.append("<ACKFILE>No File</ACKFILE>");
                }
                if (resultSet.getString("ERR_MESSAGE") != null && !"".equals(resultSet.getString("ERR_MESSAGE"))) {
                    sb.append("<ERR_MESSAGE>" + resultSet.getString("ERR_MESSAGE") + "</ERR_MESSAGE>");
                } else {
                    sb.append("<ERR_MESSAGE>NO MSG</ERR_MESSAGE>");
                }
                if (resultSet.getString("ERROR_REPORT_FILEPATH") != null) {
                    if (new File(resultSet.getString("ERROR_REPORT_FILEPATH")).exists() && new File(resultSet.getString("ERROR_REPORT_FILEPATH")).isFile()) {
                        sb.append("<ERROR_REPORT_FILEPATH>" + resultSet.getString("ERROR_REPORT_FILEPATH") + "</ERROR_REPORT_FILEPATH>");
                    } else {
                        sb.append("<ERROR_REPORT_FILEPATH>No File</ERROR_REPORT_FILEPATH>");
                    }
                } else {
                    sb.append("<ERROR_REPORT_FILEPATH>No File</ERROR_REPORT_FILEPATH>");
                }
                String sapDetails = DataSourceDataProvider.getInstance().getSapDetails(resultSet.getString("FILE_ID"), resultSet.getString("SEC_KEY_VAL"));

                if (!sapDetails.equals("None")) {
                    sb.append("<SAP_DETAILS>YES</SAP_DETAILS>");
                    String sapDetailsInfo[] = sapDetails.split("\\|");
                    sb.append("<SAP_USER>" + sapDetailsInfo[0] + "</SAP_USER>");
                    sb.append("<IDOC_NUMBER>" + sapDetailsInfo[1] + "</IDOC_NUMBER>");
                    sb.append("<PO_NUMBER>" + sapDetailsInfo[2] + "</PO_NUMBER>");
                    sb.append("<PO_DATE>" + sapDetailsInfo[3] + "</PO_DATE>");
                    sb.append("<IDOC_STATUS_CODE>" + sapDetailsInfo[4] + "</IDOC_STATUS_CODE>");
                    sb.append("<IDOC_STATUS_DESCRIPTION>" + sapDetailsInfo[5] + "</IDOC_STATUS_DESCRIPTION>");
                } else {
                    sb.append("<SAP_DETAILS>NO</SAP_DETAILS>");
                }
                sb.append("</DETAIL>");
                isGetting = true;
            }
            if (!isGetting) {
                isGetting = false;
                sb.append("<DETAIL><VALID>false</VALID></DETAIL>");
            }
            sb.append("</DETAILS>");
            sb.append("</xml>");
        } catch (SQLException sqlException) {
            LoggerUtility.log(logger, "SQLException occurred in getPaymentDetails method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
        } catch (ServiceLocatorException serviceLocatorException) {
            LoggerUtility.log(logger, "ServiceLocatorException occurred in getPaymentDetails method:: " + serviceLocatorException.getMessage(), Level.ERROR, serviceLocatorException.getCause());
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                    resultSet = null;
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                    preparedStatement = null;
                }
                if (connection != null) {
                    connection.close();
                    connection = null;
                }
            } catch (SQLException sqlException) {
                LoggerUtility.log(logger, "finally SQLException occurred in getPaymentDetails method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
            }
        }
        return sb.toString();
    }

    @Override
    public String getDocCopy(String poList, String type, String database) throws ServiceLocatorException {
        String resultString = "";
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        StringBuilder sb = new StringBuilder();
        String docCopyQuery = "";
        StringTokenizer st = new StringTokenizer(poList, "@");
        while (st.hasMoreTokens()) {
            String firstToken = st.nextToken();
            StringTokenizer st1 = new StringTokenizer(firstToken, "*");
            while (st1.hasMoreTokens()) {
                String poNum = st1.nextToken();
                String fileId = st1.nextToken();
                if (poNum != null && fileId != null) {
                    if (type.equals("POST")) {
                        if ("ARCHIVE".equals(database)) {
                            docCopyQuery = "select distinct(ARCHIVE_PO.PO_NUMBER) as PO_NUMBER,ARCHIVE_FILES.FILE_ID,"
                                    + "POST_TRANS_FILEPATH,RE_TRANSLATE_FILEPATH from ARCHIVE_PO "
                                    + "LEFT OUTER JOIN ARCHIVE_FILES ON (ARCHIVE_PO.PO_NUMBER=ARCHIVE_FILES.PRI_KEY_VAL) where "
                                    + "ARCHIVE_PO.PO_NUMBER LIKE ('" + poNum + "') and ARCHIVE_FILES.FILE_ID LIKE '" + fileId + "'";
                        } else {
                            docCopyQuery = "select distinct(PO.PO_NUMBER) as PO_NUMBER,FILES.FILE_ID,"
                                    + "POST_TRANS_FILEPATH,RE_TRANSLATE_FILEPATH from PO "
                                    + "LEFT OUTER JOIN FILES ON (PO.PO_NUMBER=FILES.PRI_KEY_VAL) where "
                                    + "PO.PO_NUMBER LIKE ('" + poNum + "') and FILES.FILE_ID LIKE '" + fileId + "'";
                        }
                        boolean isGetting = false;
                        try {
                            connection = ConnectionProvider.getInstance().getConnection();
                            preparedStatement = connection.prepareStatement(docCopyQuery);
                            resultSet = preparedStatement.executeQuery();
                            int i = 0;
                            Map srcDest = new HashMap();
                            while (resultSet.next()) {
                                if (resultSet.getString(3) != null && resultSet.getString(4) != null) {
                                    srcDest.put(new File(resultSet.getString(3)), new File(resultSet.getString(4)));
                                    isGetting = true;
                                }
                            }
                            String res = null;
                            if (!srcDest.isEmpty()) {
                                res = FileUtility.getInstance().copyPostMapFiles(srcDest);
                            } else {
                                res = "Error";
                            }
                            if (res.equals("Success")) {
                                String result = DataSourceDataProvider.getInstance().UpdateReProcessStatus("RETRANSMITTED", fileId, poNum, "M");
                                resultString = resultString + "PO : " + poNum + " was retransmitted successfully." + "\n";
                            } else {
                                resultString = resultString + "PO : " + poNum + " = " + "Source file not found!" + "\n";
                            }
                        } catch (SQLException sqlException) {
                            LoggerUtility.log(logger, "SQLException occurred in getDocCopy method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
                        } catch (ServiceLocatorException serviceLocatorException) {
                            LoggerUtility.log(logger, "ServiceLocatorException occurred in getDocCopy method:: " + serviceLocatorException.getMessage(), Level.ERROR, serviceLocatorException.getCause());
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
                                LoggerUtility.log(logger, "SQLException occurred in getDocCopy method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
                            }
                        }
                    } else {
                        if ("ARCHIVE".equals(database)) {
                            docCopyQuery = "select distinct(ARCHIVE_PO.PO_NUMBER) as PO_NUMBER,ARCHIVE_FILES.FILE_ID,"
                                    + "PRE_TRANS_FILEPATH,RE_SUBMIT_FILEPATH,ARCHIVE_FILES.SENDER_ID,ARCHIVE_FILES.RECEIVER_ID,"
                                    + "ARCHIVE_FILES.TRANSACTION_TYPE from ARCHIVE_PO LEFT OUTER JOIN ARCHIVE_FILES "
                                    + "ON (ARCHIVE_PO.PO_NUMBER=ARCHIVE_FILES.PRI_KEY_VAL) where ARCHIVE_PO.PO_NUMBER LIKE ('" + poNum + "') "
                                    + "and ARCHIVE_FILES.FILE_ID LIKE '" + fileId + "'";
                        } else {
                            docCopyQuery = "select distinct(PO.PO_NUMBER) as PO_NUMBER,FILES.FILE_ID,"
                                    + "PRE_TRANS_FILEPATH,RE_SUBMIT_FILEPATH,FILES.SENDER_ID,FILES.RECEIVER_ID,FILES.TRANSACTION_TYPE from PO LEFT OUTER JOIN FILES "
                                    + "ON (PO.PO_NUMBER=FILES.PRI_KEY_VAL) where PO.PO_NUMBER LIKE ('" + poNum + "') "
                                    + "and FILES.FILE_ID LIKE '" + fileId + "'";
                        }
                        boolean isGetting = false;
                        try {
                            connection = ConnectionProvider.getInstance().getConnection();
                            preparedStatement = connection.prepareStatement(docCopyQuery);
                            resultSet = preparedStatement.executeQuery();
                            int i = 0;
                            Map srcDest = new HashMap();
                            while (resultSet.next()) {
                                String newFilePath = resultSet.getString(4) + "|" + poNum + "|" + fileId + "|" + resultSet.getString(5) + "|" + resultSet.getString(6) + "|" + resultSet.getString(7);
                                if (resultSet.getString(3) != null && resultSet.getString(4) != null) {
                                    srcDest.put(new File(resultSet.getString(3)), new File(resultSet.getString(4)));
                                    isGetting = true;
                                }
                            }
                            String res = null;
                            if (!srcDest.isEmpty()) {
                                res = FileUtility.getInstance().copyPreMapFiles(srcDest);
                            } else {
                                res = "Error";
                            }
                            if (res.equals("Success")) {
                                String result = DataSourceDataProvider.getInstance().UpdateReProcessStatus("RESUBMITTED", fileId, poNum, "M");
                                resultString = resultString + "PO : " + poNum + " was resubmission successfully." + "\n";

                                try {
                                    String https_url = "http://192.168.1.179:8765/RetailResubmit";
                                    URL url = new URL(https_url);
                                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                                    con.setDoOutput(true);
                                    con.connect();
                                    String response = con.getResponseMessage();
                                } catch (Exception exception) {
                                    LoggerUtility.log(logger, "Exception occurred in getDocCopy method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
                                }
                            } else {
                                resultString = resultString + "PO : " + poNum + " = " + "Source file not found!" + "\n";
                            }

                        } catch (SQLException sqlException) {
                            LoggerUtility.log(logger, "SQLException occurred in getDocCopy method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
                        } catch (ServiceLocatorException serviceLocatorException) {
                            LoggerUtility.log(logger, "ServiceLocatorException occurred in getDocCopy method:: " + serviceLocatorException.getMessage(), Level.ERROR, serviceLocatorException.getCause());
                        } finally {
                            try {

                                if (resultSet != null) {
                                    resultSet.close();
                                    resultSet = null;
                                }
                                if (preparedStatement != null) {
                                    preparedStatement.close();
                                    preparedStatement = null;
                                }
                                if (connection != null) {
                                    connection.close();
                                    connection = null;
                                }
                            } catch (SQLException sqlException) {
                                LoggerUtility.log(logger, "finally SQLException occurred in getDocCopy method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
                            }
                        }
                    }
                }
            }
        }
        return resultString;
    }

    @Override
    public String getDocASNCopy(String asnList, String type) throws ServiceLocatorException {
        String resultString = "";
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String docASNCopyQuery = "";
        StringTokenizer st = new StringTokenizer(asnList, "^");
        while (st.hasMoreTokens()) {
            String firstToken = st.nextToken();
            StringTokenizer st1 = new StringTokenizer(firstToken, "|");
            while (st1.hasMoreTokens()) {
                String asnNum = st1.nextToken();
                String fileId = st1.nextToken();
                StringBuilder sb = new StringBuilder();
                if (asnNum != null && fileId != null) {
                    if (type.equals("POST")) {
                        docASNCopyQuery = "select distinct(ASN.ASN_NUMBER) as ASN_NUMBER,FILES.FILE_ID,"
                                + "POST_TRANS_FILEPATH,RE_TRANSLATE_FILEPATH from ASN LEFT OUTER JOIN FILES "
                                + "ON (ASN.ASN_NUMBER=FILES.PRI_KEY_VAL)"
                                + "where ASN.ASN_NUMBER LIKE ('" + asnNum + "') and FILES.FILE_ID LIKE '" + fileId + "'";
                        boolean isGetting = false;
                        try {
                            connection = ConnectionProvider.getInstance().getConnection();
                            preparedStatement = connection.prepareStatement(docASNCopyQuery);
                            resultSet = preparedStatement.executeQuery();
                            int i = 0;
                            Map srcDest = new HashMap();
                            while (resultSet.next()) {
                                if (resultSet.getString(3) != null && resultSet.getString(4) != null) {
                                    srcDest.put(new File(resultSet.getString(3)), new File(resultSet.getString(4)));
                                    isGetting = true;
                                }
                            }
                            String res = null;
                            if (!srcDest.isEmpty()) {
                                res = FileUtility.getInstance().copyPostMapFiles(srcDest);
                            } else {
                                res = "Error";
                            }
                            if (res.equals("Success")) {
                                String result = DataSourceDataProvider.getInstance().UpdateReProcessStatus("RETRANSMITTED", fileId, asnNum, "M");
                                resultString = resultString + "PO : " + asnNum + " was retransmitted successfully." + "\n";
                            } else {
                                resultString = resultString + "PO : " + asnNum + " = " + "Source file not found!" + "\n";
                            }
                        } catch (SQLException sqlException) {
                            LoggerUtility.log(logger, "SQLException occurred in getDocASNCopy method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
                        } catch (ServiceLocatorException serviceLocatorException) {
                            LoggerUtility.log(logger, "ServiceLocatorException occurred in getDocASNCopy method:: " + serviceLocatorException.getMessage(), Level.ERROR, serviceLocatorException.getCause());
                        } finally {
                            try {
                                if (resultSet != null) {
                                    resultSet.close();
                                    resultSet = null;
                                }
                                if (preparedStatement != null) {
                                    preparedStatement.close();
                                    preparedStatement = null;
                                }
                                if (connection != null) {
                                    connection.close();
                                    connection = null;
                                }
                            } catch (SQLException sqlException) {
                                LoggerUtility.log(logger, "finally SQLException occurred in getDocASNCopy method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
                            }
                        }
                    } else {
                        docASNCopyQuery = "select distinct(ASN.ASN_NUMBER) as ASN_NUMBER,FILES.FILE_ID,"
                                + "PRE_TRANS_FILEPATH,RE_SUBMIT_FILEPATH,FILES.SENDER_ID,FILES.RECEIVER_ID,FILES.TRANSACTION_TYPE from ASN "
                                + "LEFT OUTER JOIN FILES ON (ASN.ASN_NUMBER=FILES.PRI_KEY_VAL)"
                                + "where ASN.ASN_NUMBER LIKE ('" + asnNum + "') and FILES.FILE_ID LIKE '" + fileId + "'";
                        boolean isGetting = false;
                        try {
                            connection = ConnectionProvider.getInstance().getConnection();
                            preparedStatement = connection.prepareStatement(docASNCopyQuery);
                            resultSet = preparedStatement.executeQuery();
                            int i = 0;
                            Map srcDest = new HashMap();
                            while (resultSet.next()) {
                                String newFilePath = resultSet.getString(4) + "|" + asnNum + "|" + fileId + "|" + resultSet.getString(5) + "|" + resultSet.getString(6) + "|" + resultSet.getString(7);
                                if (resultSet.getString(3) != null && resultSet.getString(4) != null) {
                                    srcDest.put(new File(resultSet.getString(3)), newFilePath);
                                    isGetting = true;
                                }
                            }
                            String res = null;
                            if (!srcDest.isEmpty()) {
                                res = FileUtility.getInstance().copyPreMapFiles(srcDest);
                            } else {
                                res = "Error";
                            }
                            if (res.equals("Success")) {
                                String result = DataSourceDataProvider.getInstance().UpdateReProcessStatus("RESUBMITTED", fileId, asnNum, "M");
                                resultString = resultString + "ASN : " + asnNum + " was resubmission successfully." + "\n";
                            } else {
                                resultString = resultString + "ASN : " + asnNum + " = " + "Source file not found!" + "\n";
                            }
                        } catch (SQLException sqlException) {
                            LoggerUtility.log(logger, "SQLException occurred in getDocASNCopy method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
                        } catch (ServiceLocatorException serviceLocatorException) {
                            LoggerUtility.log(logger, "ServiceLocatorException occurred in getDocASNCopy method:: " + serviceLocatorException.getMessage(), Level.ERROR, serviceLocatorException.getCause());
                        } finally {
                            try {

                                if (resultSet != null) {
                                    resultSet.close();
                                    resultSet = null;
                                }
                                if (preparedStatement != null) {
                                    preparedStatement.close();
                                    preparedStatement = null;
                                }
                                if (connection != null) {
                                    connection.close();
                                    connection = null;
                                }
                            } catch (SQLException sqlException) {
                                LoggerUtility.log(logger, "finally SQLException occurred in getDocASNCopy method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
                            }
                        }
                    }
                }
            }
        }
        return resultString;
    }

    @Override
    public String getInvCopy(String invList, String type) throws ServiceLocatorException {
        String resultString = "";
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        StringBuilder sb = new StringBuilder();
        String invCopyQuery = "";
        StringTokenizer st = new StringTokenizer(invList, "^");
        while (st.hasMoreTokens()) {
            String firstToken = st.nextToken();
            StringTokenizer st1 = new StringTokenizer(firstToken, "|");
            while (st1.hasMoreTokens()) {
                String invNum = st1.nextToken();
                String fileId = st1.nextToken();
                if (invNum != null && fileId != null) {
                    if (type.equals("POST")) {
                        invCopyQuery = "select distinct(INVOICE.INVOICE_NUMBER) as INVOICE_NUMBER,FILES.FILE_ID,"
                                + "POST_TRANS_FILEPATH,RE_TRANSLATE_FILEPATH from INVOICE LEFT OUTER JOIN FILES "
                                + "ON (INVOICE.INVOICE_NUMBER=FILES.PRI_KEY_VAL)"
                                + "where INVOICE.INVOICE_NUMBER LIKE ('" + invNum + "') and FILES.FILE_ID LIKE '" + fileId + "'";
                        boolean isGetting = false;
                        try {
                            connection = ConnectionProvider.getInstance().getConnection();
                            preparedStatement = connection.prepareStatement(invCopyQuery);
                            resultSet = preparedStatement.executeQuery();
                            int i = 0;
                            Map srcDest = new HashMap();
                            while (resultSet.next()) {
                                if (resultSet.getString(3) != null && resultSet.getString(4) != null) {
                                    srcDest.put(new File(resultSet.getString(3)), new File(resultSet.getString(4)));
                                    isGetting = true;
                                }
                            }
                            String res = null;
                            if (!srcDest.isEmpty()) {
                                res = FileUtility.getInstance().copyPostMapFiles(srcDest);
                            } else {
                                res = "Error";
                            }
                            if (res.equals("Success")) {
                                String result = DataSourceDataProvider.getInstance().UpdateReProcessStatus("RETRANSMITTED", fileId, invNum, "M");
                                resultString = resultString + "INV : " + invNum + " was retransmitted successfully." + "\n";
                            } else {
                                resultString = resultString + "INV : " + invNum + " = " + "Source file not found!" + "\n";
                            }
                        } catch (SQLException sqlException) {
                            LoggerUtility.log(logger, "SQLException occurred in getInvCopy method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
                        } catch (ServiceLocatorException serviceLocatorException) {
                            LoggerUtility.log(logger, "ServiceLocatorException occurred in getInvCopy method:: " + serviceLocatorException.getMessage(), Level.ERROR, serviceLocatorException.getCause());
                        } finally {
                            try {

                                if (resultSet != null) {
                                    resultSet.close();
                                    resultSet = null;
                                }
                                if (preparedStatement != null) {
                                    preparedStatement.close();
                                    preparedStatement = null;
                                }
                                if (connection != null) {
                                    connection.close();
                                    connection = null;
                                }
                            } catch (SQLException sqlException) {
                                LoggerUtility.log(logger, "SQLException occurred in getInvCopy method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
                            }
                        }
                    } else {
                        invCopyQuery = "select distinct(INVOICE.INVOICE_NUMBER) as INVOICE_NUMBER,FILES.FILE_ID,"
                                + "PRE_TRANS_FILEPATH,RE_SUBMIT_FILEPATH,FILES.SENDER_ID,FILES.RECEIVER_ID,FILES.TRANSACTION_TYPE from INVOICE "
                                + "LEFT OUTER JOIN FILES ON (INVOICE.INVOICE_NUMBER=FILES.PRI_KEY_VAL)"
                                + "where INVOICE.INVOICE_NUMBER LIKE ('" + invNum + "') and FILES.FILE_ID LIKE '" + fileId + "'";
                        boolean isGetting = false;
                        try {
                            connection = ConnectionProvider.getInstance().getConnection();
                            preparedStatement = connection.prepareStatement(invCopyQuery);
                            resultSet = preparedStatement.executeQuery();
                            int i = 0;
                            Map srcDest = new HashMap();
                            while (resultSet.next()) {
                                String newFilePath = resultSet.getString(4) + "|" + invNum + "|" + fileId + "|" + resultSet.getString(5) + "|" + resultSet.getString(6) + "|" + resultSet.getString(7);
                                if (resultSet.getString(3) != null && resultSet.getString(4) != null) {
                                    srcDest.put(new File(resultSet.getString(3)), newFilePath);
                                    isGetting = true;
                                }
                            }
                            String res = null;
                            if (!srcDest.isEmpty()) {
                                res = FileUtility.getInstance().copyPreMapFiles(srcDest);
                            } else {
                                res = "Error";
                            }
                            if (res.equals("Success")) {
                                String result = DataSourceDataProvider.getInstance().UpdateReProcessStatus("RESUBMITTED", fileId, invNum, "M");
                                resultString = resultString + "INV : " + invNum + " was resubmission successfully." + "\n";
                            } else {
                                resultString = resultString + "INV : " + invNum + " = " + "Source file not found!" + "\n";
                            }
                        } catch (SQLException sqlException) {
                            LoggerUtility.log(logger, "SQLException occurred in getInvCopy method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
                        } catch (ServiceLocatorException serviceLocatorException) {
                            LoggerUtility.log(logger, "ServiceLocatorException occurred in getInvCopy method:: " + serviceLocatorException.getMessage(), Level.ERROR, serviceLocatorException.getCause());
                        } finally {
                            try {
                                if (resultSet != null) {
                                    resultSet.close();
                                    resultSet = null;
                                }
                                if (preparedStatement != null) {
                                    preparedStatement.close();
                                    preparedStatement = null;
                                }
                                if (connection != null) {
                                    connection.close();
                                    connection = null;
                                }
                            } catch (SQLException sqlException) {
                                LoggerUtility.log(logger, "finally SQLException occurred in getInvCopy method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
                            }
                        }
                    }
                }
            }
        }
        return resultString;
    }

    @Override
    public String getPaymentCopy(String paymentList, String type) throws ServiceLocatorException {
        String resultString = "";
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String paymentCopyQuery = "";
        StringTokenizer st = new StringTokenizer(paymentList, "^");
        while (st.hasMoreTokens()) {
            String firstToken = st.nextToken();
            StringTokenizer st1 = new StringTokenizer(firstToken, "|");
            while (st1.hasMoreTokens()) {
                String invNum = st1.nextToken();
                String fileId = st1.nextToken();
                if (invNum != null && fileId != null) {
                    if (type.equals("POST")) {
                        paymentCopyQuery = "select distinct(PAYMENT.CHECK_NUMBER) as CHECK_NUMBER,FILES.FILE_ID,"
                                + "POST_TRANS_FILEPATH,RE_TRANSLATE_FILEPATH from PAYMENT LEFT OUTER JOIN FILES "
                                + "ON (PAYMENT.CHECK_NUMBER=FILES.PRI_KEY_VAL)"
                                + "where PAYMENT.CHECK_NUMBER LIKE ('" + invNum + "') and FILES.FILE_ID LIKE '" + fileId + "'";
                        boolean isGetting = false;
                        try {
                            connection = ConnectionProvider.getInstance().getConnection();
                            preparedStatement = connection.prepareStatement(paymentCopyQuery);
                            resultSet = preparedStatement.executeQuery();
                            Map srcDest = new HashMap();
                            while (resultSet.next()) {
                                if (resultSet.getString(3) != null && resultSet.getString(4) != null) {
                                    srcDest.put(new File(resultSet.getString(3)), new File(resultSet.getString(4)));
                                    isGetting = true;
                                }
                            }
                            String res = null;
                            if (!srcDest.isEmpty()) {
                                res = FileUtility.getInstance().copyPostMapFiles(srcDest);
                            } else {
                                res = "Error";
                            }
                            if (res.equals("Success")) {
                                resultString = resultString + "INV : " + invNum + " was retransmitted successfully." + "\n";
                            } else {
                                resultString = resultString + "INV : " + invNum + " = " + "Source file not found!" + "\n";
                            }
                        } catch (SQLException sqlException) {
                            LoggerUtility.log(logger, "SQLException occurred in getPaymentCopy method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
                        } catch (ServiceLocatorException serviceLocatorException) {
                            LoggerUtility.log(logger, "ServiceLocatorException occurred in getPaymentCopy method:: " + serviceLocatorException.getMessage(), Level.ERROR, serviceLocatorException.getCause());
                        } finally {
                            try {
                                if (resultSet != null) {
                                    resultSet.close();
                                    resultSet = null;
                                }
                                if (preparedStatement != null) {
                                    preparedStatement.close();
                                    preparedStatement = null;
                                }
                                if (connection != null) {
                                    connection.close();
                                    connection = null;
                                }
                            } catch (SQLException sqlException) {
                                LoggerUtility.log(logger, "finally SQLException occurred in getPaymentCopy method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
                            }
                        }
                    } else {
                        paymentCopyQuery = "select distinct(PAYMENT.CHECK_NUMBER) as CHECK_NUMBER,FILES.FILE_ID,"
                                + "PRE_TRANS_FILEPATH,RE_SUBMIT_FILEPATH,FILES.SENDER_ID,FILES.RECEIVER_ID,FILES.TRANSACTION_TYPE from PAYMENT "
                                + "LEFT OUTER JOIN FILES ON (PAYMENT.CHECK_NUMBER=FILES.PRI_KEY_VAL)"
                                + "where PAYMENT.CHECK_NUMBER LIKE ('" + invNum + "') and FILES.FILE_ID LIKE '" + fileId + "'";
                        boolean isGetting = false;
                        try {
                            connection = ConnectionProvider.getInstance().getConnection();
                            preparedStatement = connection.prepareStatement(paymentCopyQuery);
                            resultSet = preparedStatement.executeQuery();
                            int i = 0;
                            Map srcDest = new HashMap();
                            while (resultSet.next()) {
                                String newFilePath = resultSet.getString(4) + "|" + invNum + "|" + fileId + "|" + resultSet.getString(5) + "|" + resultSet.getString(6) + "|" + resultSet.getString(7);
                                if (resultSet.getString(3) != null && resultSet.getString(4) != null) {
                                    srcDest.put(new File(resultSet.getString(3)), newFilePath);
                                    isGetting = true;
                                }
                            }
                            String res = null;
                            if (!srcDest.isEmpty()) {
                                res = FileUtility.getInstance().copyPreMapFiles(srcDest);
                            } else {
                                res = "Error";
                            }
                            if (res.equals("Success")) {
                                resultString = resultString + "INV : " + invNum + " was resubmission successfully." + "\n";
                            } else {
                                resultString = resultString + "INV : " + invNum + " = " + "Source file not found!" + "\n";
                            }
                        } catch (SQLException sqlException) {
                            LoggerUtility.log(logger, "SQLException occurred in getPaymentCopy method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
                        } catch (ServiceLocatorException serviceLocatorException) {
                            LoggerUtility.log(logger, "ServiceLocatorException occurred in getPaymentCopy method:: " + serviceLocatorException.getMessage(), Level.ERROR, serviceLocatorException.getCause());
                        } finally {
                            try {
                                if (resultSet != null) {
                                    resultSet.close();
                                    resultSet = null;
                                }
                                if (preparedStatement != null) {
                                    preparedStatement.close();
                                    preparedStatement = null;
                                }
                                if (connection != null) {
                                    connection.close();
                                    connection = null;
                                }
                            } catch (SQLException sqlException) {
                                LoggerUtility.log(logger, "finally SQLException occurred in getPaymentCopy method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
                            }
                        }
                    }
                }
            }
        }
        return resultString;
    }

    @Override
    public String getLoadCopy(String loadList, String type, String database) throws ServiceLocatorException {
        String resultString = "";
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String loadCopyQuery = "";
        StringTokenizer st = new StringTokenizer(loadList, "@");
        while (st.hasMoreTokens()) {
            String firstToken = st.nextToken();
            StringTokenizer st1 = new StringTokenizer(firstToken, "*");
            while (st1.hasMoreTokens()) {
                String shipmentId = st1.nextToken();
                String fileId = st1.nextToken();
                StringBuilder sb = new StringBuilder();
                if (shipmentId != null && fileId != null) {
                    if (type.equals("POST")) {
                        if ("ARCHIVE".equals(database)) {
                            loadCopyQuery = "select distinct(ARCHIVE_TRANSPORT_LOADTENDER.SHIPMENT_ID) as SHIPMENT_ID,ARCHIVE_FILES.FILE_ID,"
                                    + "POST_TRANS_FILEPATH,RE_TRANSLATE_FILEPATH from ARCHIVE_TRANSPORT_LOADTENDER "
                                    + "LEFT OUTER JOIN ARCHIVE_FILES ON (ARCHIVE_TRANSPORT_LOADTENDER.SHIPMENT_ID=ARCHIVE_FILES.PRI_KEY_VAL) where "
                                    + "ARCHIVE_TRANSPORT_LOADTENDER.SHIPMENT_ID LIKE ('" + shipmentId + "') and ARCHIVE_FILES.FILE_ID LIKE '" + fileId + "'";
                        } else {
                            loadCopyQuery = "select distinct(TRANSPORT_LOADTENDER.SHIPMENT_ID) as SHIPMENT_ID,FILES.FILE_ID,"
                                    + "POST_TRANS_FILEPATH,RE_TRANSLATE_FILEPATH from TRANSPORT_LOADTENDER "
                                    + "LEFT OUTER JOIN FILES ON (TRANSPORT_LOADTENDER.SHIPMENT_ID=FILES.PRI_KEY_VAL) where "
                                    + "TRANSPORT_LOADTENDER.SHIPMENT_ID LIKE ('" + shipmentId + "') and FILES.FILE_ID LIKE '" + fileId + "'";
                        }

                        boolean isGetting = false;
                        try {
                            connection = ConnectionProvider.getInstance().getConnection();
                            preparedStatement = connection.prepareStatement(loadCopyQuery);
                            resultSet = preparedStatement.executeQuery();
                            int i = 0;
                            Map srcDest = new HashMap();
                            while (resultSet.next()) {
                                if (resultSet.getString(3) != null && resultSet.getString(4) != null) {
                                    srcDest.put(new File(resultSet.getString(3)), new File(resultSet.getString(4)));
                                    isGetting = true;
                                }
                            }
                            String res = null;
                            if (!srcDest.isEmpty()) {
                                res = FileUtility.getInstance().loadTenderCopyPostMapFiles(srcDest);
                            } else {
                                res = "Error";
                            }
                            if (res.equals("Success")) {
                                resultString = resultString + "ShipmentId : " + shipmentId + " was retransmitted successfully." + "\n";
                            } else {
                                resultString = resultString + "ShipmentId : " + shipmentId + " = " + "Source file not found!" + "\n";
                            }

                        } catch (SQLException sqlException) {
                            LoggerUtility.log(logger, "SQLException occurred in getLoadCopy method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
                        } catch (ServiceLocatorException serviceLocatorException) {
                            LoggerUtility.log(logger, "ServiceLocatorException occurred in getLoadCopy method:: " + serviceLocatorException.getMessage(), Level.ERROR, serviceLocatorException.getCause());
                        } finally {
                            try {
                                if (resultSet != null) {
                                    resultSet.close();
                                    resultSet = null;
                                }
                                if (preparedStatement != null) {
                                    preparedStatement.close();
                                    preparedStatement = null;
                                }
                                if (connection != null) {
                                    connection.close();
                                    connection = null;
                                }
                            } catch (SQLException sqlException) {
                                LoggerUtility.log(logger, "finally SQLException occurred in getLoadCopy method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
                            }
                        }

                    } else {
                        if ("ARCHIVE".equals(database)) {
                            loadCopyQuery = "select distinct(ARCHIVE_TRANSPORT_LOADTENDER.SHIPMENT_ID) as SHIPMENT_ID,ARCHIVE_FILES.FILE_ID,"
                                    + "PRE_TRANS_FILEPATH,RE_SUBMIT_FILEPATH,ARCHIVE_FILES.SENDER_ID,ARCHIVE_FILES.RECEIVER_ID,ARCHIVE_FILES.TRANSACTION_TYPE from ARCHIVE_TRANSPORT_LOADTENDER LEFT OUTER JOIN ARCHIVE_FILES "
                                    + "ON (ARCHIVE_TRANSPORT_LOADTENDER.SHIPMENT_ID=ARCHIVE_FILES.PRI_KEY_VAL) where ARCHIVE_TRANSPORT_LOADTENDER.SHIPMENT_ID LIKE ('" + shipmentId + "') "
                                    + "and ARCHIVE_FILES.FILE_ID LIKE '" + fileId + "'";
                        } else {
                            loadCopyQuery = "select distinct(TRANSPORT_LOADTENDER.SHIPMENT_ID) as SHIPMENT_ID,FILES.FILE_ID,"
                                    + "PRE_TRANS_FILEPATH,RE_SUBMIT_FILEPATH,FILES.SENDER_ID,FILES.RECEIVER_ID,FILES.TRANSACTION_TYPE from TRANSPORT_LOADTENDER LEFT OUTER JOIN FILES "
                                    + "ON (TRANSPORT_LOADTENDER.SHIPMENT_ID=FILES.PRI_KEY_VAL) where TRANSPORT_LOADTENDER.SHIPMENT_ID LIKE ('" + shipmentId + "') "
                                    + "and FILES.FILE_ID LIKE '" + fileId + "'";
                        }

                        boolean isGetting = false;
                        try {
                            connection = ConnectionProvider.getInstance().getConnection();
                            preparedStatement = connection.prepareStatement(loadCopyQuery);
                            resultSet = preparedStatement.executeQuery();
                            int i = 0;
                            Map srcDest = new HashMap();
                            while (resultSet.next()) {
                                if (resultSet.getString(3) != null && resultSet.getString(4) != null) {

                                    srcDest.put(new File(resultSet.getString(3)), new File(resultSet.getString(4)));
                                    isGetting = true;
                                }
                            }

                            String res = null;
                            if (!srcDest.isEmpty()) {
                                res = FileUtility.getInstance().loadTenderCopyPreMapFiles(srcDest);
                            } else {
                                res = "Error";
                            }
                            if (res.equals("Success")) {
                                resultString = resultString + "ShipmentId : " + shipmentId + " was resubmission successfully." + "\n";

                                String https_url = "http://192.168.1.179:8765/LogisticResubmit";
                                try {
                                    URL url = new URL(https_url);
                                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                                    con.setDoOutput(true);
                                    con.connect();
                                    String response = con.getResponseMessage();
                                } catch (Exception exception) {
                                    LoggerUtility.log(logger, "Exception occurred in getLoadCopy method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
                                }
                            } else {
                                resultString = resultString + "ShipmentId : " + shipmentId + " = " + "Source file not found!" + "\n";
                            }

                        } catch (SQLException sqlException) {
                            LoggerUtility.log(logger, "SQLException occurred in getLoadCopy method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
                        } catch (ServiceLocatorException serviceLocatorException) {
                            LoggerUtility.log(logger, "ServiceLocatorException occurred in getLoadCopy method:: " + serviceLocatorException.getMessage(), Level.ERROR, serviceLocatorException.getCause());
                        } finally {
                            try {
                                if (resultSet != null) {
                                    resultSet.close();
                                    resultSet = null;
                                }
                                if (preparedStatement != null) {
                                    preparedStatement.close();
                                    preparedStatement = null;
                                }
                                if (connection != null) {
                                    connection.close();
                                    connection = null;
                                }
                            } catch (SQLException sqlException) {
                                LoggerUtility.log(logger, "finally SQLException occurred in getLoadCopy method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
                            }
                        }
                    }
                }
            }
        }
        return resultString;
    }

    @Override
    public String getLifeCycleDetails(String poNumber, String fileId, String type, String database) throws ServiceLocatorException {
        String resultXml = "";
        LifecycleUtility lifecycleUtility = new LifecycleUtility();
        try {
            if (type.equalsIgnoreCase("PO")) {
                poLifecycleBean = lifecycleUtility.getLFCPoDetails(poNumber, fileId, database);
                resultXml = getPoDetails(poLifecycleBean);
            }
            if (type.equalsIgnoreCase("ASN")) {
                asnLifecycleBean = lifecycleUtility.getLFCAsnDetails(poNumber, fileId, database);
                resultXml = getASNDetails(asnLifecycleBean);
            }

            if (type.equalsIgnoreCase("INV")) {
                invoiceLifecycleBean = lifecycleUtility.getLFCInvoiceDetails(poNumber, fileId, database);
                resultXml = getINVDetails(invoiceLifecycleBean);
            }
            if (type.equalsIgnoreCase("PAYMENT")) {
                paymentLifecycleBean = lifecycleUtility.getLFCPaymentDetails(poNumber, fileId, database);
                resultXml = getPayDetails(paymentLifecycleBean);
            }
        } catch (Exception exception) {
            LoggerUtility.log(logger, "Exception occurred in getLifeCycleDetails method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
        }
        return resultXml;

    }

    private String getPoDetails(PoLifecycleBean poLifecycleBean) throws ServiceLocatorException {
        StringBuilder sb = new StringBuilder();
        boolean isGetting = false;

        sb.append("<xml version=\"1.0\">");
        sb.append("<DETAILS>");
        try {
            if (poLifecycleBean.getRes().equals("1")) {
                sb.append("<DETAIL><VALID>true</VALID>");
                sb.append("<FILEID>" + poLifecycleBean.getFileId() + "</FILEID>");
                sb.append("<SENDER_ID>" + poLifecycleBean.getSenderId() + "</SENDER_ID>");
                sb.append("<RECEIVER_ID>" + poLifecycleBean.getRecId() + "</RECEIVER_ID>");
                sb.append("<SENDER_NAME>" + poLifecycleBean.getSenName() + "</SENDER_NAME>");
                sb.append("<RECEIVER_NAME>" + poLifecycleBean.getRecName() + "</RECEIVER_NAME>");
                sb.append("<TRAN_NUMBER>" + poLifecycleBean.getTranType() + "</TRAN_NUMBER>");
                sb.append("<PO_NUMBER>" + poLifecycleBean.getPoNumber() + "</PO_NUMBER>");
                sb.append("<PO_DATE>" + poLifecycleBean.getPodate() + "</PO_DATE>");
                sb.append("<PO_STATUS>" + poLifecycleBean.getPoStatus() + "</PO_STATUS>");
                sb.append("<SO_NUMBER>" + poLifecycleBean.getSoNumber() + "</SO_NUMBER>");
                sb.append("<SAPIDOC_NUMBER>" + poLifecycleBean.getSapIdocNum() + "</SAPIDOC_NUMBER>");
                sb.append("<ITEM_QTY>" + poLifecycleBean.getIteamQty() + "</ITEM_QTY>");
                sb.append("<ASN_NUMBER>" + poLifecycleBean.getAsnNumber() + "</ASN_NUMBER>");
                sb.append("<BOL_NUMBER>" + poLifecycleBean.getBolNumber() + "</BOL_NUMBER>");
                sb.append("<ISA_NUMBER>" + poLifecycleBean.getIsaCtrlNum() + "</ISA_NUMBER>");
                sb.append("<ISA_DATE>" + poLifecycleBean.getIsaDate() + "</ISA_DATE>");
                sb.append("<ISA_TIME>" + poLifecycleBean.getIsaTime() + "</ISA_TIME>");
                sb.append("<INV_NUMBER>" + poLifecycleBean.getInvNumber() + "</INV_NUMBER>");
                sb.append("<INV_AMOUNT>" + poLifecycleBean.getInvAmt() + "</INV_AMOUNT>");
                sb.append("<CHEQUE_NUMBER>" + poLifecycleBean.getChequeNum() + "</CHEQUE_NUMBER>");
                sb.append("<TRANS_TYPE>" + poLifecycleBean.getTranType() + "</TRANS_TYPE>");
                sb.append("<DATETIME>" + poLifecycleBean.getDatetimeRec() + "</DATETIME>");
                sb.append("<STATUS>" + poLifecycleBean.getStatus() + "</STATUS>");
                sb.append("<DIRECTION>" + poLifecycleBean.getDirection().toLowerCase() + "</DIRECTION>");
                sb.append("<GS_CONTROL_NUMBER>" + poLifecycleBean.getGsCtrlNum() + "</GS_CONTROL_NUMBER>");
                sb.append("<ST_CONTROL_NUMBER>" + poLifecycleBean.getStCtrlNum() + "</ST_CONTROL_NUMBER>");
                sb.append("<PRI_KEY_TYPE>" + poLifecycleBean.getPrimary_key_type() + "</PRI_KEY_TYPE>");
                sb.append("<PRI_KEY_VAL>" + poLifecycleBean.getPrimary_key_val() + "</PRI_KEY_VAL>");
                if (poLifecycleBean.getPreFile() != null) {
                    if (new File(poLifecycleBean.getPreFile()).exists() && new File(poLifecycleBean.getPreFile()).isFile()) {
                        sb.append("<PRETRANSFILEPATH>" + poLifecycleBean.getPreFile() + "</PRETRANSFILEPATH>");
                    } else {
                        sb.append("<PRETRANSFILEPATH>No File</PRETRANSFILEPATH>");
                    }
                } else {
                    sb.append("<PRETRANSFILEPATH>No File</PRETRANSFILEPATH>");
                }

                if (poLifecycleBean.getPostTranFile() != null) {
                    if (new File(poLifecycleBean.getPostTranFile()).exists() && new File(poLifecycleBean.getPostTranFile()).isFile()) {
                        sb.append("<POSTTRANSFILEPATH>" + poLifecycleBean.getPostTranFile() + "</POSTTRANSFILEPATH>");
                    } else {
                        sb.append("<POSTTRANSFILEPATH>No File</POSTTRANSFILEPATH>");
                    }
                } else {
                    sb.append("<POSTTRANSFILEPATH>No File</POSTTRANSFILEPATH>");
                }
                if (poLifecycleBean.getOrgFile() != null) {
                    if (new File(poLifecycleBean.getOrgFile()).exists() && new File(poLifecycleBean.getOrgFile()).isFile()) {
                        sb.append("<ORGFILEPATH>" + poLifecycleBean.getOrgFile() + "</ORGFILEPATH>");
                    } else {
                        sb.append("<ORGFILEPATH>No File</ORGFILEPATH>");
                    }
                } else {
                    sb.append("<ORGFILEPATH>No File</ORGFILEPATH>");
                }

                if (poLifecycleBean.getAckFile() != null) {
                    if (new File(poLifecycleBean.getAckFile()).exists() && new File(poLifecycleBean.getAckFile()).isFile()) {
                        sb.append("<ACKFILE>" + poLifecycleBean.getAckFile() + "</ACKFILE>");
                    } else {
                        sb.append("<ACKFILE>No File</ACKFILE>");
                    }
                } else {
                    sb.append("<ACKFILE>No File</ACKFILE>");
                }
                sb.append("</DETAIL>");
                isGetting = true;
            }
            if (!isGetting) {
                isGetting = false;
                sb.append("<DETAIL><VALID>false</VALID></DETAIL>");
            }
            sb.append("</DETAILS>");
            sb.append("</xml>");
        } catch (Exception exception) {
            LoggerUtility.log(logger, "Exception occurred in getPoDetails method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
        }
        return sb.toString();
    }

    private String getASNDetails(AsnLifecycleBean asnLifecycleBean) throws ServiceLocatorException {
        StringBuilder sb = new StringBuilder();
        boolean isGetting = false;

        sb.append("<xml version=\"1.0\">");
        sb.append("<DETAILS>");
        try {
            if (asnLifecycleBean.getRes().equals("1")) {
                sb.append("<DETAIL><VALID>true</VALID>");
                sb.append("<FILEID>" + asnLifecycleBean.getFileId() + "</FILEID>");
                sb.append("<SENDER_ID>" + asnLifecycleBean.getSenderId() + "</SENDER_ID>");
                sb.append("<RECEIVER_ID>" + asnLifecycleBean.getRecId() + "</RECEIVER_ID>");
                sb.append("<SENDER_NAME>" + asnLifecycleBean.getSenName() + "</SENDER_NAME>");
                sb.append("<RECEIVER_NAME>" + asnLifecycleBean.getRecName() + "</RECEIVER_NAME>");
                sb.append("<STATUS>" + asnLifecycleBean.getStatus() + "</STATUS>");
                sb.append("<PO_NUMBER>" + asnLifecycleBean.getPoNumber() + "</PO_NUMBER>");
                sb.append("<PO_DATE>" + asnLifecycleBean.getPodate() + "</PO_DATE>");
                sb.append("<PO_STATUS>" + asnLifecycleBean.getPoStatus() + "</PO_STATUS>");
                sb.append("<SO_NUMBER>" + asnLifecycleBean.getSoNumber() + "</SO_NUMBER>");
                sb.append("<SAPIDOC_NUMBER>" + asnLifecycleBean.getSapIdocNum() + "</SAPIDOC_NUMBER>");
                sb.append("<ITEM_QTY>" + asnLifecycleBean.getIteamQty() + "</ITEM_QTY>");
                sb.append("<DATETIME>" + asnLifecycleBean.getDatetimeRec() + "</DATETIME>");
                sb.append("<ASN_NUMBER>" + asnLifecycleBean.getAsnNumber() + "</ASN_NUMBER>");
                sb.append("<BOL_NUMBER>" + asnLifecycleBean.getBolNumber() + "</BOL_NUMBER>");
                sb.append("<ISA_NUMBER>" + asnLifecycleBean.getIsaCtrlNum() + "</ISA_NUMBER>");
                sb.append("<ISA_DATE>" + asnLifecycleBean.getIsaDate() + "</ISA_DATE>");
                sb.append("<ISA_TIME>" + asnLifecycleBean.getIsaTime() + "</ISA_TIME>");
                sb.append("<INV_NUMBER>" + asnLifecycleBean.getInvNumber() + "</INV_NUMBER>");
                sb.append("<INV_AMOUNT>" + asnLifecycleBean.getInvAmt() + "</INV_AMOUNT>");
                sb.append("<CHEQUE_NUMBER>" + asnLifecycleBean.getChequeNum() + "</CHEQUE_NUMBER>");
                sb.append("<TRANS_TYPE>" + asnLifecycleBean.getTranType() + "</TRANS_TYPE>");
                sb.append("<DIRECTION>" + asnLifecycleBean.getDirection().toLowerCase() + "</DIRECTION>");
                sb.append("<GS_CONTROL_NUMBER>" + asnLifecycleBean.getGsCtrlNum() + "</GS_CONTROL_NUMBER>");
                sb.append("<ST_CONTROL_NUMBER>" + asnLifecycleBean.getStCtrlNum() + "</ST_CONTROL_NUMBER>");
                sb.append("<PRI_KEY_TYPE>" + asnLifecycleBean.getPrimary_key_type() + "</PRI_KEY_TYPE>");
                sb.append("<PRI_KEY_VAL>" + asnLifecycleBean.getPrimary_key_val() + "</PRI_KEY_VAL>");

                if (asnLifecycleBean.getPreFile() != null) {
                    if (new File(asnLifecycleBean.getPreFile()).exists() && new File(asnLifecycleBean.getPreFile()).isFile()) {
                        sb.append("<PRETRANSFILEPATH>" + asnLifecycleBean.getPreFile() + "</PRETRANSFILEPATH>");
                    } else {
                        sb.append("<PRETRANSFILEPATH>No File</PRETRANSFILEPATH>");
                    }
                } else {
                    sb.append("<PRETRANSFILEPATH>No File</PRETRANSFILEPATH>");
                }

                if (asnLifecycleBean.getPostTranFile() != null) {
                    if (new File(asnLifecycleBean.getPostTranFile()).exists() && new File(asnLifecycleBean.getPostTranFile()).isFile()) {
                        sb.append("<POSTTRANSFILEPATH>" + asnLifecycleBean.getPostTranFile() + "</POSTTRANSFILEPATH>");
                    } else {
                        sb.append("<POSTTRANSFILEPATH>No File</POSTTRANSFILEPATH>");
                    }
                } else {
                    sb.append("<POSTTRANSFILEPATH>No File</POSTTRANSFILEPATH>");
                }

                if (asnLifecycleBean.getOrgFile() != null) {
                    if (new File(asnLifecycleBean.getOrgFile()).exists() && new File(asnLifecycleBean.getOrgFile()).isFile()) {
                        sb.append("<ORGFILEPATH>" + asnLifecycleBean.getOrgFile() + "</ORGFILEPATH>");
                    } else {
                        sb.append("<ORGFILEPATH>No File</ORGFILEPATH>");
                    }
                } else {
                    sb.append("<ORGFILEPATH>No File</ORGFILEPATH>");
                }

                if (asnLifecycleBean.getAckFile() != null) {
                    if (new File(asnLifecycleBean.getAckFile()).exists() && new File(asnLifecycleBean.getAckFile()).isFile()) {
                        sb.append("<ACKFILE>" + asnLifecycleBean.getAckFile() + "</ACKFILE>");
                    } else {
                        sb.append("<ACKFILE>No File</ACKFILE>");
                    }
                } else {
                    sb.append("<ACKFILE>No File</ACKFILE>");
                }

                sb.append("</DETAIL>");
                isGetting = true;
            }
            if (!isGetting) {
                isGetting = false;
                sb.append("<DETAIL><VALID>false</VALID></DETAIL>");
            }
            sb.append("</DETAILS>");
            sb.append("</xml>");
        } catch (Exception exception) {
            LoggerUtility.log(logger, "Exception occurred in getASNDetails method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
        }
        return sb.toString();
    }

    private String getINVDetails(InvoiceLifecycleBean invoiceLifecycleBean) throws ServiceLocatorException {
        StringBuilder sb = new StringBuilder();
        boolean isGetting = false;

        sb.append("<xml version=\"1.0\">");
        sb.append("<DETAILS>");
        try {
            if (invoiceLifecycleBean.getRes().equals("1")) {
                sb.append("<DETAIL><VALID>true</VALID>");
                sb.append("<FILEID>" + invoiceLifecycleBean.getFileId() + "</FILEID>");
                sb.append("<SENDER_ID>" + invoiceLifecycleBean.getSenderId() + "</SENDER_ID>");
                sb.append("<RECEIVER_ID>" + invoiceLifecycleBean.getRecId() + "</RECEIVER_ID>");
                sb.append("<SENDER_NAME>" + invoiceLifecycleBean.getSenName() + "</SENDER_NAME>");
                sb.append("<RECEIVER_NAME>" + invoiceLifecycleBean.getRecName() + "</RECEIVER_NAME>");
                sb.append("<TRAN_NUMBER>" + invoiceLifecycleBean.getTranType() + "</TRAN_NUMBER>");
                sb.append("<STATUS>" + invoiceLifecycleBean.getStatus() + "</STATUS>");
                sb.append("<DIRECTION>" + invoiceLifecycleBean.getDirection().toLowerCase() + "</DIRECTION>");
                sb.append("<PO_NUMBER>" + invoiceLifecycleBean.getPoNumber() + "</PO_NUMBER>");
                sb.append("<PO_DATE>" + invoiceLifecycleBean.getPodate() + "</PO_DATE>");
                sb.append("<PO_STATUS>" + invoiceLifecycleBean.getPoStatus() + "</PO_STATUS>");
                sb.append("<SO_NUMBER>" + invoiceLifecycleBean.getSoNumber() + "</SO_NUMBER>");
                sb.append("<SAPIDOC_NUMBER>" + invoiceLifecycleBean.getSapIdocNum() + "</SAPIDOC_NUMBER>");
                sb.append("<ITEM_QTY>" + invoiceLifecycleBean.getIteamQty() + "</ITEM_QTY>");
                sb.append("<DATETIME>" + invoiceLifecycleBean.getDatetimeRec() + "</DATETIME>");
                sb.append("<ASN_NUMBER>" + invoiceLifecycleBean.getAsnNumber() + "</ASN_NUMBER>");
                sb.append("<BOL_NUMBER>" + invoiceLifecycleBean.getBolNumber() + "</BOL_NUMBER>");
                sb.append("<ISA_NUMBER>" + invoiceLifecycleBean.getIsaCtrlNum() + "</ISA_NUMBER>");
                sb.append("<ISA_DATE>" + invoiceLifecycleBean.getIsaDate() + "</ISA_DATE>");
                sb.append("<ISA_TIME>" + invoiceLifecycleBean.getIsaTime() + "</ISA_TIME>");
                sb.append("<INV_NUMBER>" + invoiceLifecycleBean.getInvNumber() + "</INV_NUMBER>");
                sb.append("<INV_AMOUNT>" + invoiceLifecycleBean.getInvAmt() + "</INV_AMOUNT>");
                sb.append("<CHEQUE_NUMBER>" + invoiceLifecycleBean.getChequeNum() + "</CHEQUE_NUMBER>");
                sb.append("<TRANS_TYPE>" + invoiceLifecycleBean.getTranType() + "</TRANS_TYPE>");
                sb.append("<GS_CONTROL_NUMBER>" + invoiceLifecycleBean.getGsCtrlNum() + "</GS_CONTROL_NUMBER>");
                sb.append("<ST_CONTROL_NUMBER>" + invoiceLifecycleBean.getStCtrlNum() + "</ST_CONTROL_NUMBER>");
                sb.append("<PRI_KEY_TYPE>" + invoiceLifecycleBean.getPrimary_key_type() + "</PRI_KEY_TYPE>");
                sb.append("<PRI_KEY_VAL>" + invoiceLifecycleBean.getPrimary_key_val() + "</PRI_KEY_VAL>");
                if (invoiceLifecycleBean.getPreFile() != null) {
                    if (new File(invoiceLifecycleBean.getPreFile()).exists() && new File(invoiceLifecycleBean.getPreFile()).isFile()) {
                        sb.append("<PRETRANSFILEPATH>" + invoiceLifecycleBean.getPreFile() + "</PRETRANSFILEPATH>");
                    } else {
                        sb.append("<PRETRANSFILEPATH>No File</PRETRANSFILEPATH>");
                    }
                } else {
                    sb.append("<PRETRANSFILEPATH>No File</PRETRANSFILEPATH>");
                }

                if (invoiceLifecycleBean.getPostTranFile() != null) {
                    if (new File(invoiceLifecycleBean.getPostTranFile()).exists() && new File(invoiceLifecycleBean.getPostTranFile()).isFile()) {
                        sb.append("<POSTTRANSFILEPATH>" + invoiceLifecycleBean.getPostTranFile() + "</POSTTRANSFILEPATH>");
                    } else {
                        sb.append("<POSTTRANSFILEPATH>No File</POSTTRANSFILEPATH>");
                    }
                } else {
                    sb.append("<POSTTRANSFILEPATH>No File</POSTTRANSFILEPATH>");
                }

                if (invoiceLifecycleBean.getOrgFile() != null) {
                    if (new File(invoiceLifecycleBean.getOrgFile()).exists() && new File(invoiceLifecycleBean.getOrgFile()).isFile()) {
                        sb.append("<ORGFILEPATH>" + invoiceLifecycleBean.getOrgFile() + "</ORGFILEPATH>");
                    } else {
                        sb.append("<ORGFILEPATH>No File</ORGFILEPATH>");
                    }
                } else {
                    sb.append("<ORGFILEPATH>No File</ORGFILEPATH>");
                }

                if (invoiceLifecycleBean.getAckFile() != null) {
                    if (new File(invoiceLifecycleBean.getAckFile()).exists() && new File(invoiceLifecycleBean.getAckFile()).isFile()) {
                        sb.append("<ACKFILE>" + invoiceLifecycleBean.getAckFile() + "</ACKFILE>");
                    } else {
                        sb.append("<ACKFILE>No File</ACKFILE>");
                    }
                } else {
                    sb.append("<ACKFILE>No File</ACKFILE>");
                }

                sb.append("</DETAIL>");
                isGetting = true;
            }
            if (!isGetting) {
                isGetting = false;
                sb.append("<DETAIL><VALID>false</VALID></DETAIL>");
            }
            sb.append("</DETAILS>");
            sb.append("</xml>");
        } catch (Exception exception) {
            LoggerUtility.log(logger, "Exception occurred in getINVDetails method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
        }
        return sb.toString();

    }

    private String getPayDetails(PaymentLifecycleBean paymentLifecycleBean) throws ServiceLocatorException {
        StringBuilder sb = new StringBuilder();
        boolean isGetting = false;

        sb.append("<xml version=\"1.0\">");
        sb.append("<DETAILS>");
        try {
            if (paymentLifecycleBean.getRes().equals("1")) {
                sb.append("<DETAIL><VALID>true</VALID>");
                sb.append("<FILEID>" + paymentLifecycleBean.getFileId() + "</FILEID>");
                sb.append("<SENDER_ID>" + paymentLifecycleBean.getSenderId() + "</SENDER_ID>");
                sb.append("<RECEIVER_ID>" + paymentLifecycleBean.getRecId() + "</RECEIVER_ID>");
                sb.append("<SENDER_NAME>" + paymentLifecycleBean.getSenName() + "</SENDER_NAME>");
                sb.append("<RECEIVER_NAME>" + paymentLifecycleBean.getRecName() + "</RECEIVER_NAME>");
                sb.append("<DIRECTION>" + paymentLifecycleBean.getDirection().toLowerCase() + "</DIRECTION>");
                sb.append("<STATUS>" + paymentLifecycleBean.getStatus() + "</STATUS>");
                sb.append("<TRAN_NUMBER>" + paymentLifecycleBean.getTranType() + "</TRAN_NUMBER>");
                sb.append("<PO_NUMBER>" + paymentLifecycleBean.getPoNumber() + "</PO_NUMBER>");
                sb.append("<PO_DATE>" + paymentLifecycleBean.getPodate() + "</PO_DATE>");
                sb.append("<PO_STATUS>" + paymentLifecycleBean.getPoStatus() + "</PO_STATUS>");
                sb.append("<SO_NUMBER>" + paymentLifecycleBean.getSoNumber() + "</SO_NUMBER>");
                sb.append("<SAPIDOC_NUMBER>" + paymentLifecycleBean.getSapIdocNum() + "</SAPIDOC_NUMBER>");
                sb.append("<ITEM_QTY>" + paymentLifecycleBean.getIteamQty() + "</ITEM_QTY>");
                sb.append("<DATETIME>" + paymentLifecycleBean.getDatetimeRec() + "</DATETIME>");
                sb.append("<ASN_NUMBER>" + paymentLifecycleBean.getAsnNumber() + "</ASN_NUMBER>");
                sb.append("<BOL_NUMBER>" + paymentLifecycleBean.getBolNumber() + "</BOL_NUMBER>");
                sb.append("<ISA_NUMBER>" + paymentLifecycleBean.getIsaCtrlNum() + "</ISA_NUMBER>");
                sb.append("<ISA_DATE>" + paymentLifecycleBean.getIsaDate() + "</ISA_DATE>");
                sb.append("<ISA_TIME>" + paymentLifecycleBean.getIsaTime() + "</ISA_TIME>");
                sb.append("<INV_NUMBER>" + paymentLifecycleBean.getInvNumber() + "</INV_NUMBER>");
                sb.append("<INV_AMOUNT>" + paymentLifecycleBean.getInvAmt() + "</INV_AMOUNT>");
                sb.append("<GS_CONTROL_NUMBER>" + paymentLifecycleBean.getGsCtrlNum() + "</GS_CONTROL_NUMBER>");
                sb.append("<ST_CONTROL_NUMBER>" + paymentLifecycleBean.getStCtrlNum() + "</ST_CONTROL_NUMBER>");
                sb.append("<PRI_KEY_TYPE>" + paymentLifecycleBean.getPrimary_key_type() + "</PRI_KEY_TYPE>");
                sb.append("<PRI_KEY_VAL>" + paymentLifecycleBean.getPrimary_key_val() + "</PRI_KEY_VAL>");
                if (paymentLifecycleBean.getChequeNum() != null && !"".equals(paymentLifecycleBean.getChequeNum())) {
                    sb.append("<CHEQUE_NUMBER>" + paymentLifecycleBean.getChequeNum() + "</CHEQUE_NUMBER>");
                } else {
                    sb.append("<CHEQUE_NUMBER>0</CHEQUE_NUMBER>");
                }
                sb.append("<TRANS_TYPE>" + paymentLifecycleBean.getTranType() + "</TRANS_TYPE>");

                if (paymentLifecycleBean.getPreFile() != null) {
                    if (new File(paymentLifecycleBean.getPreFile()).exists() && new File(paymentLifecycleBean.getPreFile()).isFile()) {
                        sb.append("<PRETRANSFILEPATH>" + paymentLifecycleBean.getPreFile() + "</PRETRANSFILEPATH>");
                    } else {
                        sb.append("<PRETRANSFILEPATH>No File</PRETRANSFILEPATH>");
                    }
                } else {
                    sb.append("<PRETRANSFILEPATH>No File</PRETRANSFILEPATH>");
                }

                if (paymentLifecycleBean.getPostTranFile() != null) {
                    if (new File(paymentLifecycleBean.getPostTranFile()).exists() && new File(paymentLifecycleBean.getPostTranFile()).isFile()) {
                        sb.append("<POSTTRANSFILEPATH>" + paymentLifecycleBean.getPostTranFile() + "</POSTTRANSFILEPATH>");
                    } else {
                        sb.append("<POSTTRANSFILEPATH>No File</POSTTRANSFILEPATH>");
                    }
                } else {
                    sb.append("<POSTTRANSFILEPATH>No File</POSTTRANSFILEPATH>");
                }

                if (paymentLifecycleBean.getOrgFile() != null) {
                    if (new File(paymentLifecycleBean.getOrgFile()).exists() && new File(paymentLifecycleBean.getOrgFile()).isFile()) {
                        sb.append("<ORGFILEPATH>" + paymentLifecycleBean.getOrgFile() + "</ORGFILEPATH>");
                    } else {
                        sb.append("<ORGFILEPATH>No File</ORGFILEPATH>");
                    }
                } else {
                    sb.append("<ORGFILEPATH>No File</ORGFILEPATH>");
                }

                if (paymentLifecycleBean.getAckFile() != null) {
                    if (new File(paymentLifecycleBean.getAckFile()).exists() && new File(paymentLifecycleBean.getAckFile()).isFile()) {
                        sb.append("<ACKFILE>" + paymentLifecycleBean.getAckFile() + "</ACKFILE>");
                    } else {
                        sb.append("<ACKFILE>No File</ACKFILE>");
                    }
                } else {
                    sb.append("<ACKFILE>No File</ACKFILE>");
                }

                sb.append("</DETAIL>");
                isGetting = true;
            }
            if (!isGetting) {
                isGetting = false;
                sb.append("<DETAIL><VALID>false</VALID></DETAIL>");
            }
            sb.append("</DETAILS>");
            sb.append("</xml>");
        } catch (Exception exception) {
            LoggerUtility.log(logger, "Exception occurred in getPayDetails method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
        }
        return sb.toString();
    }

    @Override
    public String getLtLifecycleDetails(String poNumber, String fileId, String type, String database) throws ServiceLocatorException {
        String resultXml = "";
        LifecycleUtility lifecycleUtility = new LifecycleUtility();
        try {
            if (type.equalsIgnoreCase("LOADTENDER")) {
                ltTenderBean = lifecycleUtility.getLtLoadtenderDetails(poNumber, fileId, database);
                resultXml = getLtLoadtenderDetailsXml(ltTenderBean);
            }
            if (type.equalsIgnoreCase("RESPONSE")) {
                ltResponsesBean = lifecycleUtility.getLtResponseDetails(poNumber, fileId, database);
                resultXml = getLtResponseDetailsXml(ltResponsesBean);
            }
            if (type.equalsIgnoreCase("SHIPMENT")) {
                ltShipmentsBean = lifecycleUtility.getLtShipmentDetails(poNumber, fileId, database);
                resultXml = getLtShipmentDetailsXml(ltShipmentsBean);
            }
            if (type.equalsIgnoreCase("INVOICE")) {
                ltInvoicesBean = lifecycleUtility.getLtInvoiceDetails(poNumber, fileId, database);
                resultXml = getLtInvoiceDetailsXml(ltInvoicesBean);
            }
        } catch (Exception exception) {
            LoggerUtility.log(logger, "Exception occurred in getLtLifecycleDetails method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
        }
        return resultXml;
    }

    private String getLtLoadtenderDetailsXml(LtTenderBean ltTenderBean) throws ServiceLocatorException {
        StringBuilder sb = new StringBuilder();
        boolean isGetting = false;
        sb.append("<xml version=\"1.0\">");
        sb.append("<DETAILS>");
        try {
            if (ltTenderBean.getRes().equals("1")) {
                sb.append("<DETAIL><VALID>true</VALID>");
                sb.append("<FILEID>" + ltTenderBean.getFileId() + "</FILEID>");
                sb.append("<FILETYPE>" + ltTenderBean.getFileType() + "</FILETYPE>");
                sb.append("<SHIPMENTID>" + ltTenderBean.getShipmentid() + "</SHIPMENTID>");
                sb.append("<SENDER_ID>" + ltTenderBean.getSenderId() + "</SENDER_ID>");
                sb.append("<RECEIVER_ID>" + ltTenderBean.getRecId() + "</RECEIVER_ID>");
                sb.append("<SENDER_NAME>" + ltTenderBean.getSenName() + "</SENDER_NAME>");
                sb.append("<RECEIVER_NAME>" + ltTenderBean.getRecName() + "</RECEIVER_NAME>");
                sb.append("<PO_NUMBER>" + ltTenderBean.getPoNumber() + "</PO_NUMBER>");
                sb.append("<TRAN_NUMBER>" + ltTenderBean.getTran_type() + "</TRAN_NUMBER>");
                sb.append("<ISA_NUMBER>" + ltTenderBean.getIsaNum() + "</ISA_NUMBER>");
                sb.append("<ISA_DATE>" + ltTenderBean.getIsaDate() + "</ISA_DATE>");
                sb.append("<ISA_TIME>" + ltTenderBean.getIsaTime() + "</ISA_TIME>");
                sb.append("<TRANS_TYPE>" + ltTenderBean.getTran_type() + "</TRANS_TYPE>");
                sb.append("<DATETIME>" + ltTenderBean.getDatetime() + "</DATETIME>");
                sb.append("<STATUS>" + ltTenderBean.getStatus() + "</STATUS>");
                sb.append("<STCTRLNUM>" + ltTenderBean.getStCtrlNum() + "</STCTRLNUM>");
                sb.append("<GSCTRLNUM>" + ltTenderBean.getGsCtrlNum() + "</GSCTRLNUM>");
                sb.append("<DIRECTION>" + ltTenderBean.getDirection().toLowerCase() + "</DIRECTION>");
                if (ltTenderBean.getPreFile() != null) {
                    if (new File(ltTenderBean.getPreFile()).exists()) {
                        sb.append("<PRETRANSFILEPATH>" + ltTenderBean.getPreFile() + "</PRETRANSFILEPATH>");
                    } else {
                        sb.append("<PRETRANSFILEPATH>No File</PRETRANSFILEPATH>");
                    }
                } else {
                    sb.append("<PRETRANSFILEPATH>No File</PRETRANSFILEPATH>");
                }
                if (ltTenderBean.getPostTranFile() != null) {
                    if (new File(ltTenderBean.getPostTranFile()).exists()) {
                        sb.append("<POSTTRANSFILEPATH>" + ltTenderBean.getPostTranFile() + "</POSTTRANSFILEPATH>");
                    } else {
                        sb.append("<POSTTRANSFILEPATH>No File</POSTTRANSFILEPATH>");
                    }
                } else {
                    sb.append("<POSTTRANSFILEPATH>No File</POSTTRANSFILEPATH>");
                }
                if (ltTenderBean.getAckFile() != null) {
                    if (new File(ltTenderBean.getAckFile()).exists()) {
                        sb.append("<ACKFILE>" + ltTenderBean.getAckFile() + "</ACKFILE>");
                    } else {
                        sb.append("<ACKFILE>No File</ACKFILE>");
                    }
                } else {
                    sb.append("<ACKFILE>No File</ACKFILE>");
                }
                if (ltTenderBean.getComments() != null && !"".equals(ltTenderBean.getComments())) {
                    sb.append("<COMMENTS>" + ltTenderBean.getComments().trim() + "</COMMENTS>");
                } else {
                    sb.append("<COMMENTS>NO</COMMENTS>");
                }
                if (ltTenderBean.getModFilePath() != null && !"".equals(ltTenderBean.getModFilePath())) {
                    if (new File(ltTenderBean.getModFilePath()).exists()) {
                        sb.append("<MODPOSTTRANSFILEPATH>" + ltTenderBean.getModFilePath().trim() + "</MODPOSTTRANSFILEPATH>");
                    } else {
                        sb.append("<MODPOSTTRANSFILEPATH>No File</MODPOSTTRANSFILEPATH>");
                    }
                } else {
                    sb.append("<MODPOSTTRANSFILEPATH>No File</MODPOSTTRANSFILEPATH>");
                }
                sb.append("<ERRORMSG>" + ltTenderBean.getErrorMessage() + "</ERRORMSG>");
                sb.append("</DETAIL>");
                isGetting = true;
            }
            if (!isGetting) {
                isGetting = false;
                sb.append("<DETAIL><VALID>false</VALID></DETAIL>");
            }
            sb.append("</DETAILS>");
            sb.append("</xml>");
        } catch (Exception exception) {
            LoggerUtility.log(logger, "Exception occurred in getLtLoadtenderDetailsXml method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
        }
        return sb.toString();
    }

    private String getLtResponseDetailsXml(LtResponsesBean ltResponsesBean) throws ServiceLocatorException {
        StringBuilder sb = new StringBuilder();
        boolean isGetting = false;
        sb.append("<xml version=\"1.0\">");
        sb.append("<DETAILS>");
        try {
            if (ltResponsesBean.getRes() != null && ltResponsesBean.getRes().equals("1")) {
                sb.append("<DETAIL><VALID>true</VALID>");
                sb.append("<FILEID>" + ltResponsesBean.getFileId() + "</FILEID>");
                sb.append("<FILETYPE>" + ltResponsesBean.getFileType() + "</FILETYPE>");
                sb.append("<SHIPMENTID>" + ltResponsesBean.getShipmentid() + "</SHIPMENTID>");
                sb.append("<SENDER_ID>" + ltResponsesBean.getSenderId() + "</SENDER_ID>");
                sb.append("<RECEIVER_ID>" + ltResponsesBean.getRecId() + "</RECEIVER_ID>");
                sb.append("<SENDER_NAME>" + ltResponsesBean.getSenName() + "</SENDER_NAME>");
                sb.append("<RECEIVER_NAME>" + ltResponsesBean.getRecName() + "</RECEIVER_NAME>");
                sb.append("<PO_NUMBER>" + ltResponsesBean.getPoNumber() + "</PO_NUMBER>");
                sb.append("<TRAN_NUMBER>" + ltResponsesBean.getTran_type() + "</TRAN_NUMBER>");
                sb.append("<STATUS>" + ltResponsesBean.getStatus() + "</STATUS>");
                sb.append("<DATETIME>" + ltResponsesBean.getDatetime() + "</DATETIME>");
                sb.append("<ISA_NUMBER>" + ltResponsesBean.getIsaNum() + "</ISA_NUMBER>");
                sb.append("<ISA_DATE>" + ltResponsesBean.getIsaDate() + "</ISA_DATE>");
                sb.append("<ISA_TIME>" + ltResponsesBean.getIsaTime() + "</ISA_TIME>");
                sb.append("<TRANS_TYPE>" + ltResponsesBean.getTran_type() + "</TRANS_TYPE>");
                sb.append("<DIRECTION>" + ltResponsesBean.getDirection().toLowerCase() + "</DIRECTION>");
                sb.append("<STCTRLNUM>" + ltResponsesBean.getStCtrlNum() + "</STCTRLNUM>");
                sb.append("<GSCTRLNUM>" + ltResponsesBean.getGsCtrlNum() + "</GSCTRLNUM>");
                if (ltResponsesBean.getPreFile() != null) {
                    if (new File(ltResponsesBean.getPreFile()).exists()) {
                        sb.append("<PRETRANSFILEPATH>" + ltResponsesBean.getPreFile() + "</PRETRANSFILEPATH>");
                    } else {
                        sb.append("<PRETRANSFILEPATH>No File</PRETRANSFILEPATH>");
                    }
                } else {
                    sb.append("<PRETRANSFILEPATH>No File</PRETRANSFILEPATH>");
                }
                if (ltResponsesBean.getPostTranFile() != null) {
                    if (new File(ltResponsesBean.getPostTranFile()).exists()) {
                        sb.append("<POSTTRANSFILEPATH>" + ltResponsesBean.getPostTranFile() + "</POSTTRANSFILEPATH>");
                    } else {
                        sb.append("<POSTTRANSFILEPATH>No File</POSTTRANSFILEPATH>");
                    }
                } else {
                    sb.append("<POSTTRANSFILEPATH>No File</POSTTRANSFILEPATH>");
                }
                if (ltResponsesBean.getAckFile() != null) {
                    if (new File(ltResponsesBean.getAckFile()).exists()) {
                        sb.append("<ACKFILE>" + ltResponsesBean.getAckFile() + "</ACKFILE>");
                    } else {
                        sb.append("<ACKFILE>No File</ACKFILE>");
                    }
                } else {
                    sb.append("<ACKFILE>No File</ACKFILE>");
                }
                sb.append("<ERRORMSG>" + ltResponsesBean.getErrorMessage() + "</ERRORMSG>");
                sb.append("</DETAIL>");
                isGetting = true;
            }
            if (!isGetting) {
                isGetting = false;
                sb.append("<DETAIL><VALID>false</VALID></DETAIL>");
            }
            sb.append("</DETAILS>");
            sb.append("</xml>");
        } catch (Exception exception) {
            LoggerUtility.log(logger, "Exception occurred in getLtResponseDetailsXml method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
        }
        return sb.toString();
    }

    private String getLtShipmentDetailsXml(LtShipmentsBean ltShipmentsBean) throws ServiceLocatorException {
        StringBuilder sb = new StringBuilder();
        boolean isGetting = false;
        sb.append("<xml version=\"1.0\">");
        sb.append("<DETAILS>");
        try {
            if (ltShipmentsBean.getRes().equals("1")) {
                sb.append("<DETAIL><VALID>true</VALID>");
                sb.append("<FILEID>" + ltShipmentsBean.getFileId() + "</FILEID>");
                sb.append("<FILETYPE>" + ltShipmentsBean.getFileType() + "</FILETYPE>");
                sb.append("<SHIPMENTID>" + ltShipmentsBean.getShipmentid() + "</SHIPMENTID>");
                sb.append("<SENDER_ID>" + ltShipmentsBean.getSenderId() + "</SENDER_ID>");
                sb.append("<RECEIVER_ID>" + ltShipmentsBean.getRecId() + "</RECEIVER_ID>");
                sb.append("<SENDER_NAME>" + ltShipmentsBean.getSenName() + "</SENDER_NAME>");
                sb.append("<RECEIVER_NAME>" + ltShipmentsBean.getRecName() + "</RECEIVER_NAME>");
                sb.append("<PO_NUMBER>" + ltShipmentsBean.getPoNumber() + "</PO_NUMBER>");
                sb.append("<TRAN_NUMBER>" + ltShipmentsBean.getTran_type() + "</TRAN_NUMBER>");
                sb.append("<STATUS>" + ltShipmentsBean.getStatus() + "</STATUS>");
                sb.append("<DIRECTION>" + ltShipmentsBean.getDirection().toLowerCase() + "</DIRECTION>");
                sb.append("<DATETIME>" + ltShipmentsBean.getDatetime() + "</DATETIME>");
                sb.append("<ISA_NUMBER>" + ltShipmentsBean.getIsaNum() + "</ISA_NUMBER>");
                sb.append("<ISA_DATE>" + ltShipmentsBean.getIsaDate() + "</ISA_DATE>");
                sb.append("<ISA_TIME>" + ltShipmentsBean.getIsaTime() + "</ISA_TIME>");
                sb.append("<TRANS_TYPE>" + ltShipmentsBean.getTran_type() + "</TRANS_TYPE>");
                sb.append("<STCTRLNUM>" + ltShipmentsBean.getStCtrlNum() + "</STCTRLNUM>");
                sb.append("<GSCTRLNUM>" + ltShipmentsBean.getGsCtrlNum() + "</GSCTRLNUM>");
                if (ltShipmentsBean.getPreFile() != null) {
                    if (new File(ltShipmentsBean.getPreFile()).exists()) {
                        sb.append("<PRETRANSFILEPATH>" + ltShipmentsBean.getPreFile() + "</PRETRANSFILEPATH>");
                    } else {
                        sb.append("<PRETRANSFILEPATH>No File</PRETRANSFILEPATH>");
                    }
                } else {
                    sb.append("<PRETRANSFILEPATH>No File</PRETRANSFILEPATH>");
                }
                if (ltShipmentsBean.getPostTranFile() != null) {
                    if (new File(ltShipmentsBean.getPostTranFile()).exists()) {
                        sb.append("<POSTTRANSFILEPATH>" + ltShipmentsBean.getPostTranFile() + "</POSTTRANSFILEPATH>");
                    } else {
                        sb.append("<POSTTRANSFILEPATH>No File</POSTTRANSFILEPATH>");
                    }
                } else {
                    sb.append("<POSTTRANSFILEPATH>No File</POSTTRANSFILEPATH>");
                }
                if (ltShipmentsBean.getAckFile() != null) {
                    if (new File(ltShipmentsBean.getAckFile()).exists()) {
                        sb.append("<ACKFILE>" + ltShipmentsBean.getAckFile() + "</ACKFILE>");
                    } else {
                        sb.append("<ACKFILE>No File</ACKFILE>");
                    }
                } else {
                    sb.append("<ACKFILE>No File</ACKFILE>");
                }
                sb.append("<ERRORMSG>" + ltShipmentsBean.getErrorMessage() + "</ERRORMSG>");
                sb.append("</DETAIL>");
                isGetting = true;
            }
            if (!isGetting) {
                isGetting = false;
                sb.append("<DETAIL><VALID>false</VALID></DETAIL>");
            }
            sb.append("</DETAILS>");
            sb.append("</xml>");
        } catch (Exception exception) {
            LoggerUtility.log(logger, "Exception occurred in getLtShipmentDetailsXml method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
        }
        return sb.toString();
    }

    private String getLtInvoiceDetailsXml(LtInvoicesBean ltInvoicesBean) throws ServiceLocatorException {
        StringBuilder sb = new StringBuilder();
        boolean isGetting = false;
        sb.append("<xml version=\"1.0\">");
        sb.append("<DETAILS>");
        try {
            if (ltInvoicesBean.getRes().equals("1")) {
                sb.append("<DETAIL><VALID>true</VALID>");
                sb.append("<FILEID>" + ltInvoicesBean.getFileId() + "</FILEID>");
                sb.append("<FILETYPE>" + ltInvoicesBean.getFileType() + "</FILETYPE>");
                sb.append("<SHIPMENTID>" + ltInvoicesBean.getShipmentid() + "</SHIPMENTID>");
                sb.append("<SENDER_ID>" + ltInvoicesBean.getSenderId() + "</SENDER_ID>");
                sb.append("<RECEIVER_ID>" + ltInvoicesBean.getRecId() + "</RECEIVER_ID>");
                sb.append("<SENDER_NAME>" + ltInvoicesBean.getSenName() + "</SENDER_NAME>");
                sb.append("<RECEIVER_NAME>" + ltInvoicesBean.getRecName() + "</RECEIVER_NAME>");
                sb.append("<DIRECTION>" + ltInvoicesBean.getDirection().toLowerCase() + "</DIRECTION>");
                sb.append("<STATUS>" + ltInvoicesBean.getStatus() + "</STATUS>");
                sb.append("<TRAN_NUMBER>" + ltInvoicesBean.getTran_type() + "</TRAN_NUMBER>");
                sb.append("<PO_NUMBER>" + ltInvoicesBean.getPoNumber() + "</PO_NUMBER>");
                sb.append("<DATETIME>" + ltInvoicesBean.getDatetime() + "</DATETIME>");
                sb.append("<ISA_NUMBER>" + ltInvoicesBean.getIsaNum() + "</ISA_NUMBER>");
                sb.append("<ISA_DATE>" + ltInvoicesBean.getIsaDate() + "</ISA_DATE>");
                sb.append("<ISA_TIME>" + ltInvoicesBean.getIsaTime() + "</ISA_TIME>");
                sb.append("<TRANS_TYPE>" + ltInvoicesBean.getTran_type() + "</TRANS_TYPE>");
                sb.append("<STCTRLNUM>" + ltInvoicesBean.getStCtrlNum() + "</STCTRLNUM>");
                sb.append("<GSCTRLNUM>" + ltInvoicesBean.getGsCtrlNum() + "</GSCTRLNUM>");
                if (ltInvoicesBean.getPreFile() != null) {
                    if (new File(ltInvoicesBean.getPreFile()).exists()) {
                        sb.append("<PRETRANSFILEPATH>" + ltInvoicesBean.getPreFile() + "</PRETRANSFILEPATH>");
                    } else {
                        sb.append("<PRETRANSFILEPATH>No File</PRETRANSFILEPATH>");
                    }
                } else {
                    sb.append("<PRETRANSFILEPATH>No File</PRETRANSFILEPATH>");
                }
                if (ltInvoicesBean.getPostTranFile() != null) {
                    if (new File(ltInvoicesBean.getPostTranFile()).exists()) {
                        sb.append("<POSTTRANSFILEPATH>" + ltInvoicesBean.getPostTranFile() + "</POSTTRANSFILEPATH>");
                    } else {
                        sb.append("<POSTTRANSFILEPATH>No File</POSTTRANSFILEPATH>");
                    }
                } else {
                    sb.append("<POSTTRANSFILEPATH>No File</POSTTRANSFILEPATH>");
                }
                if (ltInvoicesBean.getAckFile() != null) {
                    if (new File(ltInvoicesBean.getAckFile()).exists()) {
                        sb.append("<ACKFILE>" + ltInvoicesBean.getAckFile() + "</ACKFILE>");
                    } else {
                        sb.append("<ACKFILE>No File</ACKFILE>");
                    }
                } else {
                    sb.append("<ACKFILE>No File</ACKFILE>");
                }
                sb.append("<ERRORMSG>" + ltInvoicesBean.getErrorMessage() + "</ERRORMSG>");
                sb.append("</DETAIL>");
                isGetting = true;
            }
            if (!isGetting) {
                isGetting = false;
                sb.append("<DETAIL><VALID>false</VALID></DETAIL>");
            }
            sb.append("</DETAILS>");
            sb.append("</xml>");
        } catch (Exception exception) {
            LoggerUtility.log(logger, "Exception occurred in getLtInvoiceDetailsXml method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
        }
        return sb.toString();
    }

    @Override
    public String getTpDetails(String tpId) throws ServiceLocatorException {

        boolean isGetting = false;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        StringBuilder sb = new StringBuilder();
        String tpDetailsQuery = "";
        tpDetailsQuery = "SELECT * FROM TP WHERE ID='" + tpId + "'";
        try {
            connection = ConnectionProvider.getInstance().getConnection();
            preparedStatement = connection.prepareStatement(tpDetailsQuery);
            resultSet = preparedStatement.executeQuery();
            sb.append("<xml version=\"1.0\">");
            sb.append("<DETAILS>");
            while (resultSet.next()) {
                sb.append("<DETAIL><VALID>true</VALID>");
                if (resultSet.getString("ID") != null && !"".equals(resultSet.getString("ID"))) {
                    sb.append("<ID>" + resultSet.getString("ID") + "</ID>");
                } else {
                    sb.append("<ID>--</ID>");
                }
                if (resultSet.getString("NAME") != null && !"".equals(resultSet.getString("NAME"))) {
                    sb.append("<NAME>" + resultSet.getString("NAME") + "</NAME>");
                } else {
                    sb.append("<NAME>--</NAME>");
                }

                if (resultSet.getString("CONTACT_INFO") != null && !"".equals(resultSet.getString("CONTACT_INFO"))) {
                    sb.append("<CONTACT_INFO>" + resultSet.getString("CONTACT_INFO") + "</CONTACT_INFO>");
                } else {
                    sb.append("<CONTACT_INFO>--</CONTACT_INFO>");
                }

                if (resultSet.getString("VENDOR_NUMBER") != null && !"".equals(resultSet.getString("VENDOR_NUMBER"))) {
                    sb.append("<VENDOR_NUMBER>" + resultSet.getString("VENDOR_NUMBER") + "</VENDOR_NUMBER>");
                } else {
                    sb.append("<VENDOR_NUMBER>--</VENDOR_NUMBER>");
                }

                if (resultSet.getString("DEPARTMENTS") != null && !"".equals(resultSet.getString("DEPARTMENTS"))) {
                    sb.append("<DEPARTMENTS>" + resultSet.getString("DEPARTMENTS") + "</DEPARTMENTS>");
                } else {
                    sb.append("<DEPARTMENTS>--</DEPARTMENTS>");
                }

                if (resultSet.getString("EDI_COMM_ID") != null && !"".equals(resultSet.getString("EDI_COMM_ID"))) {
                    sb.append("<EDI_COMM_ID>" + resultSet.getString("EDI_COMM_ID") + "</EDI_COMM_ID>");
                } else {
                    sb.append("<EDI_COMM_ID>--</EDI_COMM_ID>");
                }

                if (resultSet.getString("QUALIFIER") != null && !"".equals(resultSet.getString("QUALIFIER"))) {
                    sb.append("<QUALIFIER>" + resultSet.getString("QUALIFIER") + "</QUALIFIER>");
                } else {
                    sb.append("<QUALIFIER>--</QUALIFIER>");
                }

                sb.append("</DETAIL>");
                isGetting = true;
            }
            if (!isGetting) {
                isGetting = false;
                sb.append("<DETAIL><VALID>false</VALID></DETAIL>");
            }
            sb.append("</DETAILS>");
            sb.append("</xml>");
        } catch (SQLException sqlException) {
            LoggerUtility.log(logger, "SQLException occurred in getTpDetails method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
        } catch (ServiceLocatorException serviceLocatorException) {
            LoggerUtility.log(logger, "ServiceLocatorException occurred in getTpDetails method:: " + serviceLocatorException.getMessage(), Level.ERROR, serviceLocatorException.getCause());
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
                LoggerUtility.log(logger, "finally SQLException occurred in getTpDetails method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
            }
        }
        return sb.toString();
    }

    @Override
    public String updateTpDetails(AjaxHandlerAction ajaxHandlerAction) throws ServiceLocatorException {
        String updateTpDetailsquery = "";
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        StringBuilder sb = new StringBuilder();
        try {
            String id = ajaxHandlerAction.getTpId();
            updateTpDetailsquery = "UPDATE TP SET NAME='" + ajaxHandlerAction.getName() + "'"
                    + ",CONTACT_INFO='" + ajaxHandlerAction.getContact() + "',VENDOR_NUMBER='" + ajaxHandlerAction.getPhno() + ""
                    + "',DEPARTMENTS='" + ajaxHandlerAction.getDept() + "',EDI_COMM_ID='" + ajaxHandlerAction.getCommid() + "'"
                    + ",QUALIFIER='" + ajaxHandlerAction.getQualifier() + "' WHERE ID='" + id + "'";
            connection = ConnectionProvider.getInstance().getConnection();
            statement = connection.createStatement();
            int count = statement.executeUpdate(updateTpDetailsquery);
            if (count > 0) {
                sb.append("Trading Partner " + id + " Successfully Updated!");
            } else {
                sb.append("Sorry ! Please Try again.");
            }
        } catch (SQLException sqlException) {
            LoggerUtility.log(logger, "SQLException occurred in updateTpDetails method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
        } catch (ServiceLocatorException serviceLocatorException) {
            LoggerUtility.log(logger, "ServiceLocatorException occurred in updateTpDetails method:: " + serviceLocatorException.getMessage(), Level.ERROR, serviceLocatorException.getCause());
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
                LoggerUtility.log(logger, "SQLException occurred in updateTpDetails method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
            }
        }
        return sb.toString();
    }

    public String getTpDetailsByName(String name) throws ServiceLocatorException {
        boolean isGetting = false;
        Connection connection = null;
        PreparedStatement PrepareStatement = null;
        ResultSet resultSet = null;
        StringBuilder sb = new StringBuilder();
        String tpDetailsByNameQuery = "";
        tpDetailsByNameQuery = "select tp_details.tp_id as id,tp_details.city as city,tp_details.zip as zip,"
                + "tp_details.qualifier as qualifier,tp_details.network as network,tp_details.vendor_number as vendor,"
                + "tp_details.department_number as department,tp_details.duns as duns,tp_details.ship_duns as ship,"
                + "tp_details.billing_duns as billing,tp_details.order_duns as order,"
                + "tp_details.as2_url as url,tp_details.as2_cert as cert "
                + "from tp LEFT OUTER JOIN tp_details on (tp_details.TP_ID=tp.ID) where tp.name='" + name + "'";
        try {
            connection = ConnectionProvider.getInstance().getConnection();
            PrepareStatement = connection.prepareStatement(tpDetailsByNameQuery);
            resultSet = PrepareStatement.executeQuery();
            sb.append("<xml version=\"1.0\">");
            sb.append("<DETAILS>");
            while (resultSet.next()) {
                sb.append("<DETAIL><VALID>true</VALID>");
                if (resultSet.getString("id") != null && !"".equals(resultSet.getString("id"))) {
                    sb.append("<ID>").append(resultSet.getString("id")).append("</ID>");
                } else {
                    sb.append("<ID>").append("NO").append("</ID>");
                }

                if (resultSet.getString("city") != null && !"".equals(resultSet.getString("city"))) {
                    sb.append("<CITY>").append(resultSet.getString("city")).append("</CITY>");

                } else {
                    sb.append("<CITY>").append("NO").append("</CITY>");
                }

                if (resultSet.getString("zip") != null && !"".equals(resultSet.getString("zip"))) {
                    sb.append("<ZIP>").append(resultSet.getString("zip")).append("</ZIP>");

                } else {
                    sb.append("<ZIP>").append("NO").append("</ZIP>");
                }

                if (resultSet.getString("qualifier") != null && !"".equals(resultSet.getString("qualifier"))) {
                    sb.append("<QUALIFIER>").append(resultSet.getString("qualifier")).append("</QUALIFIER>");

                } else {
                    sb.append("<QUALIFIER>").append("NO").append("</QUALIFIER>");
                }

                if (resultSet.getString("network") != null && !"".equals(resultSet.getString("network"))) {
                    sb.append("<NETWORK>").append(resultSet.getString("network")).append("</NETWORK>");

                } else {
                    sb.append("<NETWORK>").append("NO").append("</NETWORK>");
                }
                if (resultSet.getString("vendor") != null && !"".equals(resultSet.getString("vendor"))) {
                    sb.append("<VENDOR>").append(resultSet.getString("vendor")).append("</VENDOR>");

                } else {
                    sb.append("<VENDOR>").append("NO").append("</VENDOR>");
                }

                if (resultSet.getString("department") != null && !"".equals(resultSet.getString("department"))) {
                    sb.append("<DEPARTMENT>").append(resultSet.getString("department")).append("</DEPARTMENT>");

                } else {
                    sb.append("<DEPARTMENT>").append("NO").append("</DEPARTMENT>");
                }

                if (resultSet.getString("duns") != null && !"".equals(resultSet.getString("duns"))) {
                    sb.append("<DUNS>").append(resultSet.getString("duns")).append("</DUNS>");

                } else {
                    sb.append("<DUNS>").append("NO").append("</DUNS>");
                }

                if (resultSet.getString("ship") != null && !"".equals(resultSet.getString("ship"))) {
                    sb.append("<SHIP>").append(resultSet.getString("ship")).append("</SHIP>");

                } else {
                    sb.append("<SHIP>").append("NO").append("</SHIP>");
                }

                if (resultSet.getString("billing") != null && !"".equals(resultSet.getString("billing"))) {
                    sb.append("<BILLING>").append(resultSet.getString("billing")).append("</BILLING>");

                } else {
                    sb.append("<BILLING>").append("NO").append("</BILLING>");
                }

                if (resultSet.getString("order") != null && !"".equals(resultSet.getString("order"))) {
                    sb.append("<ORDER>").append(resultSet.getString("order")).append("</ORDER>");

                } else {
                    sb.append("<ORDER>").append("NO").append("</ORDER>");
                }

                if (resultSet.getString("url") != null && !"".equals(resultSet.getString("url"))) {
                    sb.append("<URL>").append(resultSet.getString("url")).append("</URL>");

                } else {
                    sb.append("<URL>").append("NO").append("</URL>");
                }

                if (resultSet.getString("cert") != null && !"".equals(resultSet.getString("cert"))) {
                    sb.append("<CERT>").append(resultSet.getString("cert")).append("</CERT>");

                } else {
                    sb.append("<CERT>").append("NO").append("</CERT>");
                }
                sb.append("</DETAIL>");
                isGetting = true;
            }
            if (!isGetting) {
                isGetting = false;
                sb.append("<DETAIL><VALID>false</VALID></DETAIL>");
            }

            sb.append("</DETAILS>");
            sb.append("</xml>");
        } catch (SQLException sqlException) {
            LoggerUtility.log(logger, "SQLException occurred in getTpDetailsByName method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
        } catch (ServiceLocatorException serviceLocatorException) {
            LoggerUtility.log(logger, "ServiceLocatorException occurred in getTpDetailsByName method:: " + serviceLocatorException.getMessage(), Level.ERROR, serviceLocatorException.getCause());
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                    resultSet = null;
                }
                if (PrepareStatement != null) {
                    PrepareStatement.close();
                    PrepareStatement = null;
                }
                if (connection != null) {
                    connection.close();
                    connection = null;
                }
            } catch (SQLException sqlException) {
                LoggerUtility.log(logger, "Exception occurred in getTpDetailsByName method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
            }
        }
        return sb.toString();
    }

    @Override
    public String getTpDetailInformation(String tpId, String defFlowId) throws ServiceLocatorException {
        boolean isGetting = false;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        StringBuilder sb = new StringBuilder();
        try {
            connection = ConnectionProvider.getInstance().getConnection();
            preparedStatement = connection.prepareStatement("select TP.ID as tpId,tp.NAME as tpName,TP_NAME,CONTACT_NAME,BVR_UDI_ID,tp_details.NAME as bvrName,"
                    + "PHONE_NUMBER,EMAIL,ADDRESS,CITY,STATE,ZIP,NETWORK,URL,PO_TYPE_BASIC,PO_TYPE_SOQ,INVOICE_TYPE_STORE,"
                    + "INVOICE_TYPE_MASTER,DEVELOPING,VENDOR,ORDER_DUNS,SHIP_DUNS,PAY_DUNS,DEPARTMENT_NUMBER,BUYER_NAME,"
                    + "BUYER_PHONE,BUYER_EMAIL,CUSTOMER_NAME,CUSTOMER_PHONE,CUSTOMER_EMAIL from tp LEFT OUTER JOIN tp_details on (tp.ID=tp_details.TP_ID) where tp.ID=? and tp.FLOW_FLAG=?");
            preparedStatement.setString(1, tpId);
            preparedStatement.setString(2, defFlowId);
            resultSet = preparedStatement.executeQuery();
            sb.append("<xml version=\"1.0\">");
            sb.append("<DETAILS>");
            while (resultSet.next()) {
                sb.append("<DETAIL><VALID>true</VALID>");
                if (resultSet.getString("tpId") != null && !"".equals(resultSet.getString("tpId"))) {
                    sb.append("<ID>").append(resultSet.getString("tpId")).append("</ID>");
                } else {
                    sb.append("<ID>").append("NO").append("</ID>");
                }

                if (resultSet.getString("CITY") != null && !"".equals(resultSet.getString("CITY"))) {
                    sb.append("<CITY>").append(resultSet.getString("CITY")).append("</CITY>");

                } else {
                    sb.append("<CITY>").append("NO").append("</CITY>");
                }

                if (resultSet.getString("ZIP") != null && !"".equals(resultSet.getString("ZIP"))) {
                    sb.append("<ZIP>").append(resultSet.getString("ZIP")).append("</ZIP>");

                } else {
                    sb.append("<ZIP>").append("NO").append("</ZIP>");
                }

                if (resultSet.getString("NETWORK") != null && !"".equals(resultSet.getString("NETWORK"))) {
                    sb.append("<NETWORK>").append(resultSet.getString("NETWORK")).append("</NETWORK>");

                } else {
                    sb.append("<NETWORK>").append("NO").append("</NETWORK>");
                }
                if (resultSet.getString("VENDOR") != null && !"".equals(resultSet.getString("VENDOR"))) {
                    sb.append("<VENDOR>").append(resultSet.getString("VENDOR")).append("</VENDOR>");

                } else {
                    sb.append("<VENDOR>").append("NO").append("</VENDOR>");
                }

                if (resultSet.getString("DEPARTMENT_NUMBER") != null && !"".equals(resultSet.getString("DEPARTMENT_NUMBER"))) {
                    sb.append("<DEPARTMENT>").append(resultSet.getString("DEPARTMENT_NUMBER")).append("</DEPARTMENT>");

                } else {
                    sb.append("<DEPARTMENT>").append("NO").append("</DEPARTMENT>");
                }

                if (resultSet.getString("SHIP_DUNS") != null && !"".equals(resultSet.getString("SHIP_DUNS"))) {
                    sb.append("<SHIP>").append(resultSet.getString("SHIP_DUNS")).append("</SHIP>");

                } else {
                    sb.append("<SHIP>").append("NO").append("</SHIP>");
                }

                if (resultSet.getString("PAY_DUNS") != null && !"".equals(resultSet.getString("PAY_DUNS"))) {
                    sb.append("<PAY_DUNS>").append(resultSet.getString("PAY_DUNS")).append("</PAY_DUNS>");

                } else {
                    sb.append("<PAY_DUNS>").append("NO").append("</PAY_DUNS>");
                }

                if (resultSet.getString("ORDER_DUNS") != null && !"".equals(resultSet.getString("ORDER_DUNS"))) {
                    sb.append("<ORDER>").append(resultSet.getString("ORDER_DUNS")).append("</ORDER>");

                } else {
                    sb.append("<ORDER>").append("NO").append("</ORDER>");
                }

                if (resultSet.getString("URL") != null && !"".equals(resultSet.getString("URL"))) {
                    sb.append("<URL>").append(resultSet.getString("URL")).append("</URL>");

                } else {
                    sb.append("<URL>").append("NO").append("</URL>");
                }

                sb.append("</DETAIL>");
                isGetting = true;
            }
            if (!isGetting) {
                isGetting = false;
                sb.append("<DETAIL><VALID>false</VALID></DETAIL>");
            }

            sb.append("</DETAILS>");
            sb.append("</xml>");
        } catch (SQLException sqlException) {
            LoggerUtility.log(logger, "SQLException occurred in getTpDetailInformation method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
        } catch (ServiceLocatorException serviceLocatorException) {
            LoggerUtility.log(logger, "ServiceLocatorException occurred in getTpDetailInformation method:: " + serviceLocatorException.getMessage(), Level.ERROR, serviceLocatorException.getCause());
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                    resultSet = null;
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                    preparedStatement = null;
                }
                if (connection != null) {
                    connection.close();
                    connection = null;
                }
            } catch (SQLException sqlException) {
                LoggerUtility.log(logger, "finally SQLException occurred in getTpDetailInformation method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
            }
        }
        return sb.toString();
    }

    @Override
    public String getLogisticsDocDetails(String instanceid, int id, String database) throws ServiceLocatorException {
        System.out.println("get doc logistics doc details are" + instanceid);
        System.out.println("get doc logistics doc details are" + id);
        System.out.println("get doc logistics doc details are" + database);
        boolean isGetting = false;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        StringBuilder sb = new StringBuilder();
        String logisticsDocDetailsQuery = "";
        if ("ARCHIVE".equals(database)) {
            logisticsDocDetailsQuery = "select ARCHIVE_FILES.FILE_ID,ARCHIVE_FILES.FILE_TYPE,ARCHIVE_FILES.SENDER_ID,"
                    + "ARCHIVE_FILES.RECEIVER_ID,ARCHIVE_FILES.PRE_TRANS_FILEPATH,"
                    + "ARCHIVE_FILES.POST_TRANS_FILEPATH,ARCHIVE_FILES.SEC_KEY_VAL as SEC_KEY_VAL,"
                    + "ARCHIVE_FILES.PRI_KEY_TYPE as PRI_KEY_TYPE,ARCHIVE_FILES.PRI_KEY_VAL as PRI_KEY_VAL,"
                    + "ARCHIVE_FILES.ORG_FILEPATH as ORG_FILEPATH,ARCHIVE_FILES.ISA_NUMBER as ISA_NUMBER,"
                    + "ARCHIVE_FILES.TRANSACTION_TYPE as TRANSACTION_TYPE,"
                    + "ARCHIVE_FILES.GS_CONTROL_NUMBER as GS_CONTROL_NUMBER,"
                    + "ARCHIVE_FILES.ST_CONTROL_NUMBER as ST_CONTROL_NUMBER,"
                    + "ARCHIVE_FILES.ERR_MESSAGE,ARCHIVE_FILES.ERROR_REPORT_FILEPATH as ERROR_REPORT_FILEPATH,"
                    + "ARCHIVE_FILES.ACK_FILE_ID as ACK_FILE_ID,ARCHIVE_FILES.ISA_DATE as ISA_DATE,"
                    + "ARCHIVE_FILES.ISA_TIME as ISA_TIME,ARCHIVE_FILES.STATUS as STATUS,ARCHIVE_FILES.DIRECTION as DIRECTION,tl.BOL_NUMBER as BOL_NUMBER ,tl.CO_NUMBER as CO_NUMBER,tl.PO_NUMBER as PO_NUMBER  "
                    + "FROM ARCHIVE_FILES LEFT OUTER JOIN ARCHIVE_Transport_loadtender tl on (tl.FILE_ID=ARCHIVE_FILES.FILE_ID and tl.SHIPMENT_ID=ARCHIVE_FILES.SEC_KEY_VAL) "
                    + "WHERE ARCHIVE_FILES.FILE_ID LIKE '%" + instanceid + "%' and ARCHIVE_FILES.ID =" + id;
        } else {
            logisticsDocDetailsQuery = "select FILES.FILE_ID,FILES.FILE_TYPE,FILES.SENDER_ID,"
                    + "FILES.RECEIVER_ID,FILES.PRE_TRANS_FILEPATH,"
                    + "FILES.POST_TRANS_FILEPATH,FILES.SEC_KEY_VAL as SEC_KEY_VAL,"
                    + "FILES.PRI_KEY_TYPE as PRI_KEY_TYPE,FILES.PRI_KEY_VAL as PRI_KEY_VAL,"
                    + "FILES.ORG_FILEPATH as ORG_FILEPATH,FILES.ISA_NUMBER as ISA_NUMBER,"
                    + "FILES.TRANSACTION_TYPE as TRANSACTION_TYPE,"
                    + "FILES.GS_CONTROL_NUMBER as GS_CONTROL_NUMBER,"
                    + "FILES.ST_CONTROL_NUMBER as ST_CONTROL_NUMBER,"
                    + "FILES.ERR_MESSAGE,FILES.ERROR_REPORT_FILEPATH as ERROR_REPORT_FILEPATH,"
                    + "FILES.ACK_FILE_ID as ACK_FILE_ID,FILES.ISA_DATE as ISA_DATE,"
                    + "FILES.ISA_TIME as ISA_TIME,FILES.STATUS as STATUS,FILES.DIRECTION as DIRECTION,tl.BOL_NUMBER as BOL_NUMBER ,tl.CO_NUMBER as CO_NUMBER,tl.PO_NUMBER as PO_NUMBER  "
                    + "FROM FILES LEFT OUTER JOIN Transport_loadtender tl on (tl.FILE_ID=FILES.FILE_ID and tl.SHIPMENT_ID=FILES.SEC_KEY_VAL) "
                    + "WHERE FILES.FILE_ID LIKE '%" + instanceid + "%' and FILES.ID =" + id;
        }
        try {
            Map<String, String> tradingPartners = DataSourceDataProvider.getInstance().getAllTradingPartners();
            System.out.println("TP map reference:" + tradingPartners);
            System.out.println("TP map:" + tradingPartners.toString());
            connection = ConnectionProvider.getInstance().getConnection();
            preparedStatement = connection.prepareStatement(logisticsDocDetailsQuery);
            resultSet = preparedStatement.executeQuery();
            sb.append("<xml version=\"1.0\">");
            sb.append("<DETAILS>");
            while (resultSet.next()) {
                sb.append("<DETAIL><VALID>true</VALID>");
                if (resultSet.getString("FILE_ID") != null && !"".equals(resultSet.getString("FILE_ID"))) {
                    sb.append("<FILEID>" + resultSet.getString("FILE_ID") + "</FILEID>");
                } else {
                    sb.append("<FILEID>--</FILEID>");
                }
                if (resultSet.getString("FILE_TYPE") != null && !"".equals(resultSet.getString("FILE_TYPE"))) {
                    sb.append("<FILETYPE>" + resultSet.getString("FILE_TYPE") + "</FILETYPE>");
                } else {
                    sb.append("<FILETYPE>--</FILETYPE>");
                }
                if (resultSet.getString("SENDER_ID") != null && !"".equals(resultSet.getString("SENDER_ID"))) {
                    sb.append("<SENDERID>" + resultSet.getString("SENDER_ID") + "</SENDERID>");
                } else {
                    sb.append("<SENDERID>--</SENDERID>");
                }
                if (resultSet.getString("RECEIVER_ID") != null && !"".equals(resultSet.getString("RECEIVER_ID"))) {
                    sb.append("<RECEIVERID>" + resultSet.getString("RECEIVER_ID") + "</RECEIVERID>");
                } else {
                    sb.append("<RECEIVERID>--</RECEIVERID>");
                }
                if (resultSet.getString("DIRECTION") != null && !"".equals(resultSet.getString("DIRECTION"))) {
                    sb.append("<DIRECTION>" + resultSet.getString("DIRECTION") + "</DIRECTION>");
                } else {
                    sb.append("<DIRECTION>--</DIRECTION>");
                }
                if (resultSet.getString("BOL_NUMBER") != null && !"".equals(resultSet.getString("BOL_NUMBER"))) {
                    sb.append("<BOL_NUMBER>" + resultSet.getString("BOL_NUMBER").trim() + "</BOL_NUMBER>");
                } else {
                    sb.append("<BOL_NUMBER>--</BOL_NUMBER>");
                }

                if (resultSet.getString("CO_NUMBER") != null && !"".equals(resultSet.getString("CO_NUMBER"))) {
                    sb.append("<CO_NUMBER>" + resultSet.getString("CO_NUMBER").trim() + "</CO_NUMBER>");
                } else {
                    sb.append("<CO_NUMBER>--</CO_NUMBER>");
                }

                if (resultSet.getString("PO_NUMBER") != null && !"".equals(resultSet.getString("PO_NUMBER"))) {
                    sb.append("<PO_NUMBER>" + resultSet.getString("PO_NUMBER").trim() + "</PO_NUMBER>");
                } else {
                    sb.append("<PO_NUMBER>--</PO_NUMBER>");
                }

                if (resultSet.getString("SENDER_ID") != null && (((tradingPartners.get(resultSet.getString("SENDER_ID")))) != null)) {
                    sb.append("<SENDER_NAME>" + (tradingPartners.get(resultSet.getString("SENDER_ID"))).toString() + "</SENDER_NAME>");
                } else {
                    sb.append("<SENDER_NAME>--</SENDER_NAME>");
                }
                if (resultSet.getString("RECEIVER_ID") != null && (((tradingPartners.get(resultSet.getString("RECEIVER_ID")))) != null)) {
                    sb.append("<RECEIVER_NAME>" + (tradingPartners.get(resultSet.getString("RECEIVER_ID"))).toString() + "</RECEIVER_NAME>");
                } else {
                    sb.append("<RECEIVER_NAME>--</RECEIVER_NAME>");
                }

                if (resultSet.getString("SEC_KEY_VAL") != null && !"".equals(resultSet.getString("SEC_KEY_VAL"))) {
                    sb.append("<SEC_KEY_VAL>" + resultSet.getString("SEC_KEY_VAL") + "</SEC_KEY_VAL>");
                } else {
                    sb.append("<SEC_KEY_VAL>--</SEC_KEY_VAL>");
                }
                if (resultSet.getString("ISA_NUMBER") != null && !"".equals(resultSet.getString("ISA_NUMBER"))) {
                    sb.append("<ISA_NUMBER>" + resultSet.getString("ISA_NUMBER") + "</ISA_NUMBER>");
                } else {
                    sb.append("<ISA_NUMBER>--</ISA_NUMBER>");
                }
                if (resultSet.getString("GS_CONTROL_NUMBER") != null && !"".equals(resultSet.getString("GS_CONTROL_NUMBER"))) {
                    sb.append("<GS_CONTROL_NUMBER>" + resultSet.getString("GS_CONTROL_NUMBER") + "</GS_CONTROL_NUMBER>");
                } else {
                    sb.append("<GS_CONTROL_NUMBER>--</GS_CONTROL_NUMBER>");
                }
                if (resultSet.getString("ST_CONTROL_NUMBER") != null && !"".equals(resultSet.getString("ST_CONTROL_NUMBER"))) {
                    sb.append("<ST_CONTROL_NUMBER>" + resultSet.getString("ST_CONTROL_NUMBER") + "</ST_CONTROL_NUMBER>");
                } else {
                    sb.append("<ST_CONTROL_NUMBER>--</ST_CONTROL_NUMBER>");
                }
                if (resultSet.getString("TRANSACTION_TYPE") != null && !"".equals(resultSet.getString("TRANSACTION_TYPE"))) {
                    sb.append("<TRANSACTION_TYPE>" + resultSet.getString("TRANSACTION_TYPE") + "</TRANSACTION_TYPE>");
                } else {
                    sb.append("<TRANSACTION_TYPE>--</TRANSACTION_TYPE>");
                }
                if (resultSet.getString("ISA_DATE") != null && !"".equals(resultSet.getString("ISA_DATE"))) {
                    sb.append("<ISA_DATE>" + resultSet.getString("ISA_DATE") + "</ISA_DATE>");
                } else {
                    sb.append("<ISA_DATE>--</ISA_DATE>");
                }
                if (resultSet.getString("ISA_TIME") != null && !"".equals(resultSet.getString("ISA_TIME"))) {
                    sb.append("<ISA_TIME>" + resultSet.getString("ISA_TIME") + "</ISA_TIME>");
                } else {
                    sb.append("<ISA_TIME>--</ISA_TIME>");
                }
                if (resultSet.getString("TRANSACTION_TYPE") != null && !"".equals(resultSet.getString("TRANSACTION_TYPE"))) {
                    sb.append("<TRANSACTION_TYPE>" + resultSet.getString("TRANSACTION_TYPE") + "</TRANSACTION_TYPE>");
                } else {
                    sb.append("<TRANSACTION_TYPE>--</TRANSACTION_TYPE>");
                }
                if (resultSet.getString("STATUS") != null && !"".equals(resultSet.getString("STATUS"))) {
                    sb.append("<STATUS>" + resultSet.getString("STATUS") + "</STATUS>");
                } else {
                    sb.append("<STATUS>--</STATUS>");
                }

                if (resultSet.getString("PRI_KEY_TYPE") != null && resultSet.getString("PRI_KEY_TYPE").equalsIgnoreCase("RID")) {
                    sb.append("<PRI_KEY_TYPE>RID</PRI_KEY_TYPE>");
                }
                if (resultSet.getString("PRI_KEY_TYPE") != null && resultSet.getString("PRI_KEY_TYPE").equalsIgnoreCase("SID")) {
                    sb.append("<PRI_KEY_TYPE> SID </PRI_KEY_TYPE>");
                } else if (resultSet.getString("PRI_KEY_TYPE") != null) {
                    sb.append("<PRI_KEY_TYPE> " + resultSet.getString("PRI_KEY_TYPE") + " </PRI_KEY_TYPE>");
                }

                if (resultSet.getString("PRI_KEY_VAL") != null && !"".equals(resultSet.getString("PRI_KEY_VAL"))) {
                    sb.append("<PRI_KEY_VAL>" + resultSet.getString("PRI_KEY_VAL") + "</PRI_KEY_VAL>");
                } else {
                    sb.append("<PRI_KEY_VAL>--</PRI_KEY_VAL>");
                }

                if (resultSet.getString("PRE_TRANS_FILEPATH") != null) {
                    if (new File(resultSet.getString("PRE_TRANS_FILEPATH")).exists() && new File(resultSet.getString("PRE_TRANS_FILEPATH")).isFile()) {
                        sb.append("<PRETRANSFILEPATH>" + resultSet.getString("PRE_TRANS_FILEPATH") + "</PRETRANSFILEPATH>");
                    } else {
                        sb.append("<PRETRANSFILEPATH>No File</PRETRANSFILEPATH>");
                    }
                } else {
                    sb.append("<PRETRANSFILEPATH>No File</PRETRANSFILEPATH>");
                }

                if (resultSet.getString("POST_TRANS_FILEPATH") != null) {
                    if (new File(resultSet.getString("POST_TRANS_FILEPATH")).exists() && new File(resultSet.getString("POST_TRANS_FILEPATH")).isFile()) {
                        sb.append("<POSTTRANSFILEPATH>" + resultSet.getString("POST_TRANS_FILEPATH") + "</POSTTRANSFILEPATH>");
                    } else {
                        sb.append("<POSTTRANSFILEPATH>No File</POSTTRANSFILEPATH>");
                    }
                } else {
                    sb.append("<POSTTRANSFILEPATH>No File</POSTTRANSFILEPATH>");
                }

                if (resultSet.getString("ORG_FILEPATH") != null) {
                    if (new File(resultSet.getString("ORG_FILEPATH")).exists() && new File(resultSet.getString("ORG_FILEPATH")).isFile()) {
                        sb.append("<ORG_FILEPATH>" + resultSet.getString("ORG_FILEPATH") + "</ORG_FILEPATH>");
                    } else {
                        sb.append("<ORG_FILEPATH>No File</ORG_FILEPATH>");
                    }
                } else {
                    sb.append("<ORG_FILEPATH>No File</ORG_FILEPATH>");
                }

                if (resultSet.getString("ACK_FILE_ID") != null) {
                    if (new File(resultSet.getString("ACK_FILE_ID")).exists() && new File(resultSet.getString("ACK_FILE_ID")).isFile()) {
                        sb.append("<ACKFILEID>" + resultSet.getString("ACK_FILE_ID") + "</ACKFILEID>");
                    } else {
                        sb.append("<ACKFILEID>No File</ACKFILEID>");
                    }
                } else {
                    sb.append("<ACKFILEID>No File</ACKFILEID>");
                }

                if (resultSet.getString("ERR_MESSAGE") != null && !"".equals(resultSet.getString("ERR_MESSAGE"))) {
                    sb.append("<ERR_MESSAGE>" + resultSet.getString("ERR_MESSAGE") + "</ERR_MESSAGE>");
                } else {
                    sb.append("<ERR_MESSAGE>NO MSG</ERR_MESSAGE>");
                }
                if (resultSet.getString("ERROR_REPORT_FILEPATH") != null) {
                    if (new File(resultSet.getString("ERROR_REPORT_FILEPATH")).exists() && new File(resultSet.getString("ERROR_REPORT_FILEPATH")).isFile()) {
                        sb.append("<ERROR_REPORT_FILEPATH>" + resultSet.getString("ERROR_REPORT_FILEPATH") + "</ERROR_REPORT_FILEPATH>");
                    } else {
                        sb.append("<ERROR_REPORT_FILEPATH>No File</ERROR_REPORT_FILEPATH>");
                    }
                } else {
                    sb.append("<ERROR_REPORT_FILEPATH>No File</ERROR_REPORT_FILEPATH>");
                }

                sb.append("</DETAIL>");
                isGetting = true;
            }
            if (!isGetting) {
                isGetting = false;
                sb.append("<DETAIL><VALID>false</VALID></DETAIL>");
            }
            sb.append("</DETAILS>");
            sb.append("</xml>");
        } catch (SQLException sqlException) {
            LoggerUtility.log(logger, "SQLException occurred in getLogisticsDocDetails method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
        } catch (ServiceLocatorException serviceLocatorException) {
            LoggerUtility.log(logger, "ServiceLocatorException occurred in getLogisticsDocDetails method:: " + serviceLocatorException.getMessage(), Level.ERROR, serviceLocatorException.getCause());
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                    resultSet = null;
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                    preparedStatement = null;
                }
                if (connection != null) {
                    connection.close();
                    connection = null;
                }
            } catch (SQLException sqlException) {
                LoggerUtility.log(logger, "SQLException occurred in getLogisticsDocDetails method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
            }
        }
        return sb.toString();
    }

    @Override
    public String getLoadTenderingDetails(String instanceid, String ponum, String database) throws ServiceLocatorException {
        System.out.println("getLoadTenderingDetails are" + instanceid);
        System.out.println("getLoadTenderingDetails are" + ponum);
        System.out.println("getLoadTenderingDetails are" + database);
        boolean isGetting = false;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        StringBuilder sb = new StringBuilder();
        String loadTenderingDetailsQuery = "";
        if ("ARCHIVE".equals(database)) {
            loadTenderingDetailsQuery = "SELECT ARCHIVE_FILES.FILE_ID,ARCHIVE_FILES.FILE_TYPE,ARCHIVE_FILES.SENDER_ID,ARCHIVE_FILES.RECEIVER_ID,"
                    + "ARCHIVE_FILES.PRE_TRANS_FILEPATH,ARCHIVE_FILES.SENDER_ID,ARCHIVE_FILES.RECEIVER_ID,ARCHIVE_FILES.POST_TRANS_FILEPATH,ARCHIVE_FILES.SEC_KEY_VAL as SEC_KEY_VAL,"
                    + "ARCHIVE_FILES.PRI_KEY_TYPE as PRI_KEY_TYPE,ARCHIVE_FILES.PRI_KEY_VAL as PRI_KEY_VAL,"
                    + "ARCHIVE_FILES.ORG_FILEPATH as ORG_FILEPATH,ARCHIVE_FILES.ISA_NUMBER as ISA_NUMBER,"
                    + "ARCHIVE_FILES.TRANSACTION_TYPE as TRANSACTION_TYPE,ARCHIVE_FILES.GS_CONTROL_NUMBER as GS_CONTROL_NUMBER,"
                    + "ARCHIVE_FILES.ST_CONTROL_NUMBER as ST_CONTROL_NUMBER,"
                    + "ARCHIVE_FILES.ERR_MESSAGE,ARCHIVE_FILES.ACK_FILE_ID as ACK_FILE_ID,ARCHIVE_FILES.DIRECTION as DIRECTION , ARCHIVE_FILES.ISA_DATE as ISA_DATE,ARCHIVE_FILES.ISA_TIME as ISA_TIME,ARCHIVE_FILES.STATUS, "
                    + "ARCHIVE_Transport_loadtender.BOL_NUMBER as BOL_NUMBER,ARCHIVE_Transport_loadtender.CO_NUMBER as CO_NUMBER, ARCHIVE_FILES.ERROR_REPORT_FILEPATH as ERROR_REPORT_FILEPATH, "
                    + "ARCHIVE_Transport_loadtender.TOTAL_WEIGHT as TOTAL_WEIGHT,ARCHIVE_Transport_loadtender.TOTAL_VOLUME as TOTAL_VOLUME,"
                    + "ARCHIVE_Transport_loadtender.TOTAL_PIECES as TOTAL_PIECES,ARCHIVE_Transport_loadtender.PO_NUMBER as PO_NUMBER FROM ARCHIVE_FILES  "
                    + "LEFT OUTER JOIN ARCHIVE_Transport_loadtender  ON (ARCHIVE_FILES.FILE_ID=ARCHIVE_Transport_loadtender.FILE_ID and ARCHIVE_FILES.SEC_KEY_VAL=ARCHIVE_Transport_loadtender.SHIPMENT_ID)   "
                    + "where ARCHIVE_FILES.FILE_ID LIKE '%" + instanceid + "%' and ARCHIVE_FILES.SEC_KEY_VAL LIKE '%" + ponum + "%'";
        } else {
            loadTenderingDetailsQuery = "SELECT FILES.FILE_ID,FILES.FILE_TYPE,FILES.SENDER_ID,FILES.RECEIVER_ID,"
                    + "FILES.PRE_TRANS_FILEPATH,FILES.POST_TRANS_FILEPATH,FILES.SEC_KEY_VAL as SEC_KEY_VAL,"
                    + "FILES.PRI_KEY_TYPE as PRI_KEY_TYPE,FILES.PRI_KEY_VAL as PRI_KEY_VAL,"
                    + "FILES.ORG_FILEPATH as ORG_FILEPATH,FILES.ISA_NUMBER as ISA_NUMBER,"
                    + "FILES.TRANSACTION_TYPE as TRANSACTION_TYPE,FILES.GS_CONTROL_NUMBER as GS_CONTROL_NUMBER,"
                    + "FILES.ST_CONTROL_NUMBER as ST_CONTROL_NUMBER,TP1.NAME as SENDER_NAME,TP2.NAME as RECEIVER_NAME,"
                    + "FILES.ERR_MESSAGE,FILES.ACK_FILE_ID as ACK_FILE_ID,FILES.DIRECTION as DIRECTION , FILES.ISA_DATE as ISA_DATE,FILES.ISA_TIME as ISA_TIME,FILES.STATUS, "
                    + "Transport_loadtender.BOL_NUMBER as BOL_NUMBER,Transport_loadtender.CO_NUMBER as CO_NUMBER, FILES.ERROR_REPORT_FILEPATH as ERROR_REPORT_FILEPATH, "
                    + "Transport_loadtender.TOTAL_WEIGHT as TOTAL_WEIGHT,Transport_loadtender.TOTAL_VOLUME as TOTAL_VOLUME, "
                    + "Transport_loadtender.TOTAL_PIECES as TOTAL_PIECES,Transport_loadtender.PO_NUMBER as PO_NUMBER FROM FILES  LEFT OUTER JOIN Transport_loadtender  ON (FILES.FILE_ID=Transport_loadtender.FILE_ID and FILES.SEC_KEY_VAL=Transport_loadtender.SHIPMENT_ID)   LEFT OUTER JOIN TP TP1 ON(TP1.ID=FILES.SENDER_ID ) LEFT OUTER JOIN TP TP2 ON(TP2.ID=FILES.RECEIVER_ID) "
                    + "where FILES.FILE_ID LIKE '%" + instanceid + "%' and FILES.SEC_KEY_VAL LIKE '%" + ponum + "%'";
        }

        try {
            Map<String, String> tradingPartners = DataSourceDataProvider.getInstance().getAllTradingPartners();
            connection = ConnectionProvider.getInstance().getConnection();
            preparedStatement = connection.prepareStatement(loadTenderingDetailsQuery);
            resultSet = preparedStatement.executeQuery();
            sb.append("<xml version=\"1.0\">");
            sb.append("<DETAILS>");
            while (resultSet.next()) {
                sb.append("<DETAIL><VALID>true</VALID>");
                if (resultSet.getString("FILE_ID") != null && !"".equals(resultSet.getString("FILE_ID"))) {
                    sb.append("<FILEID>" + resultSet.getString("FILE_ID").trim() + "</FILEID>");
                } else {
                    sb.append("<FILEID>--</FILEID>");
                }
                if (resultSet.getString("FILE_TYPE") != null && !"".equals(resultSet.getString("FILE_TYPE"))) {
                    sb.append("<FILETYPE>" + resultSet.getString("FILE_TYPE").trim() + "</FILETYPE>");
                } else {
                    sb.append("<FILETYPE>--</FILETYPE>");
                }
                if (resultSet.getString("SENDER_ID") != null && !"".equals(resultSet.getString("SENDER_ID"))) {
                    sb.append("<SENDERID>" + resultSet.getString("SENDER_ID").trim() + "</SENDERID>");
                } else {
                    sb.append("<SENDERID>--</SENDERID>");
                }
                if (resultSet.getString("RECEIVER_ID") != null && !"".equals(resultSet.getString("RECEIVER_ID"))) {
                    sb.append("<RECEIVERID>" + resultSet.getString("RECEIVER_ID").trim() + "</RECEIVERID>");
                } else {
                    sb.append("<RECEIVERID>--</RECEIVERID>");
                }
                if (resultSet.getString("DIRECTION") != null && !"".equals(resultSet.getString("DIRECTION"))) {
                    sb.append("<DIRECTION>" + resultSet.getString("DIRECTION").trim() + "</DIRECTION>");
                } else {
                    sb.append("<DIRECTION>--</DIRECTION>");
                }
                if (resultSet.getString("ISA_DATE") != null && !"".equals(resultSet.getString("ISA_DATE"))) {
                    sb.append("<ISA_DATE>" + resultSet.getString("ISA_DATE").trim() + "</ISA_DATE>");
                } else {
                    sb.append("<ISA_DATE>--</ISA_DATE>");
                }
                if (resultSet.getString("ISA_TIME") != null && !"".equals(resultSet.getString("ISA_TIME"))) {
                    sb.append("<ISA_TIME>" + resultSet.getString("ISA_TIME").trim() + "</ISA_TIME>");
                } else {
                    sb.append("<ISA_TIME>--</ISA_TIME>");
                }
                if (resultSet.getString("STATUS") != null && !"".equals(resultSet.getString("STATUS"))) {
                    sb.append("<STATUS>" + resultSet.getString("STATUS").trim() + "</STATUS>");
                } else {
                    sb.append("<STATUS>--</STATUS>");
                }
                if (resultSet.getString("BOL_NUMBER") != null && !"".equals(resultSet.getString("BOL_NUMBER"))) {
                    sb.append("<BOL_NUMBER>" + resultSet.getString("BOL_NUMBER").trim() + "</BOL_NUMBER>");
                } else {
                    sb.append("<BOL_NUMBER>--</BOL_NUMBER>");
                }

                if (resultSet.getString("PO_NUMBER") != null && !"".equals(resultSet.getString("PO_NUMBER"))) {
                    sb.append("<PO_NUMBER>" + resultSet.getString("PO_NUMBER").trim() + "</PO_NUMBER>");
                } else {
                    sb.append("<PO_NUMBER>--</PO_NUMBER>");
                }
                if (resultSet.getString("CO_NUMBER") != null && !"".equals(resultSet.getString("CO_NUMBER"))) {
                    sb.append("<CO_NUMBER>" + resultSet.getString("CO_NUMBER").trim() + "</CO_NUMBER>");
                } else {
                    sb.append("<CO_NUMBER>--</CO_NUMBER>");
                }
                if (resultSet.getString("TOTAL_WEIGHT") != null && !"".equals(resultSet.getString("TOTAL_WEIGHT"))) {
                    sb.append("<TOTAL_WEIGHT>" + resultSet.getString("TOTAL_WEIGHT").trim() + "</TOTAL_WEIGHT>");
                } else {
                    sb.append("<TOTAL_WEIGHT>--</TOTAL_WEIGHT>");
                }
                if (resultSet.getString("TOTAL_PIECES") != null && !"".equals(resultSet.getString("TOTAL_PIECES"))) {
                    sb.append("<TOTAL_PIECES>" + resultSet.getString("TOTAL_PIECES").trim() + "</TOTAL_PIECES>");
                } else {
                    sb.append("<TOTAL_PIECES>--</TOTAL_PIECES>");
                }
                if (resultSet.getString("TOTAL_VOLUME") != null && !"".equals(resultSet.getString("TOTAL_VOLUME"))) {
                    sb.append("<TOTAL_VOLUME>" + resultSet.getString("TOTAL_VOLUME").trim() + "</TOTAL_VOLUME>");
                } else {
                    sb.append("<TOTAL_VOLUME>--</TOTAL_VOLUME>");
                }

                if (resultSet.getString("SENDER_ID") != null && (((tradingPartners.get(resultSet.getString("SENDER_ID")))) != null)) {
                    sb.append("<SENDER_NAME>" + (tradingPartners.get(resultSet.getString("SENDER_ID"))).toString() + "</SENDER_NAME>");
                } else {
                    sb.append("<SENDER_NAME>--</SENDER_NAME>");
                }
                if (resultSet.getString("RECEIVER_ID") != null && (((tradingPartners.get(resultSet.getString("RECEIVER_ID")))) != null)) {
                    sb.append("<RECEIVER_NAME>" + (tradingPartners.get(resultSet.getString("RECEIVER_ID"))).toString() + "</RECEIVER_NAME>");
                } else {
                    sb.append("<RECEIVER_NAME>--</RECEIVER_NAME>");
                }
                if (resultSet.getString("ST_CONTROL_NUMBER") != null && !"".equals(resultSet.getString("ST_CONTROL_NUMBER"))) {
                    sb.append("<ST_CONTROL_NUMBER>" + resultSet.getString("ST_CONTROL_NUMBER").trim() + "</ST_CONTROL_NUMBER>");
                } else {
                    sb.append("<ST_CONTROL_NUMBER>--</ST_CONTROL_NUMBER>");
                }
                if (resultSet.getString("ISA_NUMBER") != null && !"".equals(resultSet.getString("ISA_NUMBER"))) {
                    sb.append("<ISA_NUMBER>" + resultSet.getString("ISA_NUMBER").trim() + "</ISA_NUMBER>");
                } else {
                    sb.append("<ISA_NUMBER>--</ISA_NUMBER>");
                }
                if (resultSet.getString("FILE_ID") != null && !"".equals(resultSet.getString("FILE_ID"))) {
                    sb.append("<GS_CONTROL_NUMBER>" + resultSet.getString("GS_CONTROL_NUMBER").trim() + "</GS_CONTROL_NUMBER>");
                } else {
                    sb.append("<FILEID>--</FILEID>");
                }
                if (resultSet.getString("TRANSACTION_TYPE") != null && !"".equals(resultSet.getString("TRANSACTION_TYPE"))) {
                    sb.append("<TRANSACTION_TYPE>" + resultSet.getString("TRANSACTION_TYPE").trim() + "</TRANSACTION_TYPE>");
                } else {
                    sb.append("<TRANSACTION_TYPE>--</TRANSACTION_TYPE>");
                }
                if (resultSet.getString("SEC_KEY_VAL") != null && !"".equals(resultSet.getString("SEC_KEY_VAL"))) {
                    sb.append("<SEC_KEY_VAL>" + resultSet.getString("SEC_KEY_VAL").trim() + "</SEC_KEY_VAL>");
                } else {
                    sb.append("<SEC_KEY_VAL>--</SEC_KEY_VAL>");
                }

                if (resultSet.getString("PRI_KEY_TYPE") != null && resultSet.getString("PRI_KEY_TYPE").equalsIgnoreCase("RID")) {
                    sb.append("<PRI_KEY_TYPE>RID</PRI_KEY_TYPE>");
                } else if (resultSet.getString("PRI_KEY_TYPE") != null && resultSet.getString("PRI_KEY_TYPE").equalsIgnoreCase("SID")) {
                    sb.append("<PRI_KEY_TYPE> SID </PRI_KEY_TYPE>");
                } else if (resultSet.getString("PRI_KEY_TYPE") != null && resultSet.getString("PRI_KEY_TYPE").equalsIgnoreCase("BOL")) {
                    sb.append("<PRI_KEY_TYPE> BOL </PRI_KEY_TYPE>");
                } else {
                    sb.append("<PRI_KEY_TYPE>--</PRI_KEY_TYPE>");
                }

                if (resultSet.getString("PRI_KEY_VAL") != null && !"".equals(resultSet.getString("PRI_KEY_VAL"))) {
                    sb.append("<PRI_KEY_VAL>" + resultSet.getString("PRI_KEY_VAL").trim() + "</PRI_KEY_VAL>");
                } else {
                    sb.append("<PRI_KEY_VAL>--</PRI_KEY_VAL>");
                }

                if (resultSet.getString("PRE_TRANS_FILEPATH") != null) {
                    if (new File(resultSet.getString("PRE_TRANS_FILEPATH")).exists() && new File(resultSet.getString("PRE_TRANS_FILEPATH")).isFile()) {
                        sb.append("<PRETRANSFILEPATH>" + resultSet.getString("PRE_TRANS_FILEPATH").trim() + "</PRETRANSFILEPATH>");
                    } else {
                        sb.append("<PRETRANSFILEPATH>No File</PRETRANSFILEPATH>");
                    }
                } else {
                    sb.append("<PRETRANSFILEPATH>No File</PRETRANSFILEPATH>");
                }

                if (resultSet.getString("POST_TRANS_FILEPATH") != null) {
                    if (new File(resultSet.getString("POST_TRANS_FILEPATH")).exists() && new File(resultSet.getString("POST_TRANS_FILEPATH")).isFile()) {
                        sb.append("<POSTTRANSFILEPATH>" + resultSet.getString("POST_TRANS_FILEPATH").trim() + "</POSTTRANSFILEPATH>");
                    } else {
                        sb.append("<POSTTRANSFILEPATH>No File</POSTTRANSFILEPATH>");
                    }
                } else {
                    sb.append("<POSTTRANSFILEPATH>No File</POSTTRANSFILEPATH>");
                }

                if (resultSet.getString("ACK_FILE_ID") != null) {
                    if (new File(resultSet.getString("ACK_FILE_ID")).exists() && new File(resultSet.getString("ACK_FILE_ID")).isFile()) {
                        sb.append("<ACKFILEID>" + resultSet.getString("ACK_FILE_ID").trim() + "</ACKFILEID>");
                    } else {
                        sb.append("<ACKFILEID>No File</ACKFILEID>");
                    }
                } else {
                    sb.append("<ACKFILEID>No File</ACKFILEID>");
                }

                if (resultSet.getString("ERROR_REPORT_FILEPATH") != null) {
                    if (new File(resultSet.getString("ERROR_REPORT_FILEPATH")).exists() && new File(resultSet.getString("ERROR_REPORT_FILEPATH")).isFile()) {
                        sb.append("<ERROR_REPORT_FILEPATH>" + resultSet.getString("ERROR_REPORT_FILEPATH") + "</ERROR_REPORT_FILEPATH>");
                    } else {
                        sb.append("<ERROR_REPORT_FILEPATH>No File</ERROR_REPORT_FILEPATH>");
                    }
                } else {
                    sb.append("<ERROR_REPORT_FILEPATH>No File</ERROR_REPORT_FILEPATH>");
                }

                if (resultSet.getString("ERR_MESSAGE") != null && !"".equals(resultSet.getString("ERR_MESSAGE"))) {
                    sb.append("<ERR_MESSAGE>" + resultSet.getString("ERR_MESSAGE").trim() + "</ERR_MESSAGE>");
                } else {
                    sb.append("<ERR_MESSAGE>NO MSG</ERR_MESSAGE>");
                }

                sb.append("</DETAIL>");
                isGetting = true;
            }
            if (!isGetting) {
                isGetting = false;
                sb.append("<DETAIL><VALID>false</VALID></DETAIL>");
            }

            sb.append("</DETAILS>");
            sb.append("</xml>");
        } catch (SQLException sqlException) {
            LoggerUtility.log(logger, "SQLException occurred in getLoadTenderingDetails method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
        } catch (ServiceLocatorException serviceLocatorException) {
            LoggerUtility.log(logger, "ServiceLocatorException occurred in getLoadTenderingDetails method:: " + serviceLocatorException.getMessage(), Level.ERROR, serviceLocatorException.getCause());
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                    resultSet = null;
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                    preparedStatement = null;
                }
                if (connection != null) {
                    connection.close();
                    connection = null;
                }
            } catch (SQLException sqlException) {
                LoggerUtility.log(logger, "finally SQLException occurred in getLoadTenderingDetails method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
            }
        }
        return sb.toString();
    }

    @Override
    public String getLtResponseDetails(String fileId, String refId, String database) throws ServiceLocatorException {
        System.out.println("getLtResponseDetails are" + fileId);
        System.out.println("getLtResponseDetails are" + refId);
        System.out.println("getLtResponseDetails are" + database);
        boolean isGetting = false;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        StringBuilder sb = new StringBuilder();
        String ltResponseDetailsQuery = "";
        if ("ARCHIVE".equals(database)) {
            ltResponseDetailsQuery = "SELECT DISTINCT(ARCHIVE_FILES.FILE_ID) as FILE_ID,ARCHIVE_TRANSPORT_LT_RESPONSE.SHIPMENT_ID as SHIPMENT_ID,"
                    + "ARCHIVE_FILES.FILE_TYPE as FILE_TYPE,"
                    + "ARCHIVE_FILES.TRANSACTION_TYPE as TRANSACTION_TYPE,ARCHIVE_FILES.SENDER_ID,ARCHIVE_FILES.RECEIVER_ID,"
                    + "ARCHIVE_FILES.ISA_NUMBER as ISA_NUMBER,ARCHIVE_FILES.GS_CONTROL_NUMBER as GS_CONTROL_NUMBER,"
                    + "ARCHIVE_FILES.ST_CONTROL_NUMBER as ST_CONTROL_NUMBER,ARCHIVE_FILES.ISA_DATE as ISA_DATE,ARCHIVE_FILES.ISA_TIME as ISA_TIME, ARCHIVE_FILES.ERROR_REPORT_FILEPATH as ERROR_REPORT_FILEPATH,"
                    + "ARCHIVE_FILES.STATUS as STATUS,ARCHIVE_FILES.PRE_TRANS_FILEPATH,ARCHIVE_FILES.POST_TRANS_FILEPATH,ARCHIVE_FILES.ACK_FILE_ID,ARCHIVE_FILES.ERR_MESSAGE,ARCHIVE_FILES.SEC_KEY_VAL as REFERENCE"
                    + " FROM ARCHIVE_TRANSPORT_LT_RESPONSE LEFT OUTER JOIN ARCHIVE_FILES ON (ARCHIVE_TRANSPORT_LT_RESPONSE.FILE_ID =ARCHIVE_FILES.FILE_ID)"
                    + "WHERE 1=1 AND ARCHIVE_TRANSPORT_LT_RESPONSE.FILE_ID = '" + fileId + "' AND ARCHIVE_TRANSPORT_LT_RESPONSE.REF_ID='" + refId + "'  AND ARCHIVE_FILES.FLOWFLAG = 'L'  ";
        } else {
            ltResponseDetailsQuery = "SELECT DISTINCT(FILES.FILE_ID) as FILE_ID,TRANSPORT_LT_RESPONSE.SHIPMENT_ID as SHIPMENT_ID,"
                    + "FILES.FILE_TYPE as FILE_TYPE,"
                    + "FILES.TRANSACTION_TYPE as TRANSACTION_TYPE,FILES.SENDER_ID,FILES.RECEIVER_ID,"
                    + "FILES.ISA_NUMBER as ISA_NUMBER,FILES.GS_CONTROL_NUMBER as GS_CONTROL_NUMBER,"
                    + "FILES.ST_CONTROL_NUMBER as ST_CONTROL_NUMBER,FILES.ISA_DATE as ISA_DATE,FILES.ISA_TIME as ISA_TIME, FILES.ERROR_REPORT_FILEPATH as ERROR_REPORT_FILEPATH,"
                    + "FILES.STATUS as STATUS,FILES.PRE_TRANS_FILEPATH,FILES.POST_TRANS_FILEPATH,FILES.ACK_FILE_ID,FILES.ERR_MESSAGE,FILES.SEC_KEY_VAL as REFERENCE"
                    + " FROM TRANSPORT_LT_RESPONSE LEFT OUTER JOIN FILES ON (TRANSPORT_LT_RESPONSE.FILE_ID =FILES.FILE_ID)"
                    + "WHERE 1=1 AND TRANSPORT_LT_RESPONSE.FILE_ID = '" + fileId + "' AND TRANSPORT_LT_RESPONSE.REF_ID='" + refId + "'  AND FILES.FLOWFLAG = 'L'  ";
        }
        System.out.println("LT queryyyyy is" + ltResponseDetailsQuery.toString());
        try {
            Map<String, String> tradingPartners = DataSourceDataProvider.getInstance().getAllTradingPartners();
            connection = ConnectionProvider.getInstance().getConnection();
            preparedStatement = connection.prepareStatement(ltResponseDetailsQuery);
            resultSet = preparedStatement.executeQuery();
            sb.append("<xml version=\"1.0\">");
            sb.append("<DETAILS>");
            while (resultSet.next()) {
                sb.append("<DETAIL><VALID>true</VALID>");
                if (resultSet.getString("FILE_ID") != null && !"".equals(resultSet.getString("FILE_ID"))) {
                    sb.append("<FILE_ID>" + resultSet.getString("FILE_ID") + "</FILE_ID>");
                } else {
                    sb.append("<FILE_ID>--</FILE_ID>");
                }
                if (resultSet.getString("SHIPMENT_ID") != null && !"".equals(resultSet.getString("SHIPMENT_ID"))) {
                    sb.append("<SHIPMENT_ID>" + resultSet.getString("SHIPMENT_ID") + "</SHIPMENT_ID>");
                } else {
                    sb.append("<SHIPMENT_ID>--</SHIPMENT_ID>");
                }
                if (resultSet.getString("FILE_TYPE") != null && !"".equals(resultSet.getString("FILE_TYPE"))) {
                    sb.append("<FILE_TYPE>" + resultSet.getString("FILE_TYPE") + "</FILE_TYPE>");
                } else {
                    sb.append("<FILETYPE>--</FILETYPE>");
                }
                if (resultSet.getString("TRANSACTION_TYPE") != null && !"".equals(resultSet.getString("TRANSACTION_TYPE"))) {
                    sb.append("<TRANSACTION_TYPE>" + resultSet.getString("TRANSACTION_TYPE") + "</TRANSACTION_TYPE>");
                } else {
                    sb.append("<TRANSACTION_TYPE>--</TRANSACTION_TYPE>");
                }
                if (resultSet.getString("SENDER_ID") != null && !"".equals(resultSet.getString("SENDER_ID"))) {
                    sb.append("<SENDER_ID>" + resultSet.getString("SENDER_ID") + "</SENDER_ID>");
                } else {
                    sb.append("<SENDER_ID>--</SENDER_ID>");
                }

                if (resultSet.getString("RECEIVER_ID") != null && !"".equals(resultSet.getString("RECEIVER_ID"))) {
                    sb.append("<RECEIVER_ID>" + resultSet.getString("RECEIVER_ID") + "</RECEIVER_ID>");
                } else {
                    sb.append("<RECEIVER_ID>--</RECEIVER_ID>");
                }

                if (resultSet.getString("SENDER_ID") != null && (((tradingPartners.get(resultSet.getString("SENDER_ID")))) != null)) {
                    sb.append("<SENDER_NAME>" + (tradingPartners.get(resultSet.getString("SENDER_ID"))).toString() + "</SENDER_NAME>");
                } else {
                    sb.append("<SENDER_NAME>--</SENDER_NAME>");
                }
                if (resultSet.getString("RECEIVER_ID") != null && (((tradingPartners.get(resultSet.getString("RECEIVER_ID")))) != null)) {
                    sb.append("<RECEIVER_NAME>" + (tradingPartners.get(resultSet.getString("RECEIVER_ID"))).toString() + "</RECEIVER_NAME>");
                } else {
                    sb.append("<RECEIVER_NAME>--</RECEIVER_NAME>");
                }

                if (resultSet.getString("ISA_NUMBER") != null && !"".equals(resultSet.getString("ISA_NUMBER"))) {
                    sb.append("<ISA_NUMBER>" + resultSet.getString("ISA_NUMBER") + "</ISA_NUMBER>");
                } else {
                    sb.append("<ISA_NUMBER>--</ISA_NUMBER>");
                }
                if (resultSet.getString("GS_CONTROL_NUMBER") != null && !"".equals(resultSet.getString("GS_CONTROL_NUMBER"))) {
                    sb.append("<GS_CONTROL_NUMBER>" + resultSet.getString("GS_CONTROL_NUMBER") + "</GS_CONTROL_NUMBER>");
                } else {
                    sb.append("<GS_CONTROL_NUMBER>--</GS_CONTROL_NUMBER>");
                }
                if (resultSet.getString("ST_CONTROL_NUMBER") != null && !"".equals(resultSet.getString("ST_CONTROL_NUMBER"))) {
                    sb.append("<ST_CONTROL_NUMBER>" + resultSet.getString("ST_CONTROL_NUMBER") + "</ST_CONTROL_NUMBER>");
                } else {
                    sb.append("<ST_CONTROL_NUMBER>--</ST_CONTROL_NUMBER>");
                }
                if (resultSet.getString("ISA_DATE") != null && !"".equals(resultSet.getString("ISA_DATE"))) {
                    sb.append("<ISA_DATE>" + resultSet.getString("ISA_DATE") + "</ISA_DATE>");
                } else {
                    sb.append("<ISA_DATE>--</ISA_DATE>");
                }
                if (resultSet.getString("ISA_TIME") != null && !"".equals(resultSet.getString("ISA_TIME"))) {
                    sb.append("<ISA_TIME>" + resultSet.getString("ISA_TIME") + "</ISA_TIME>");
                } else {
                    sb.append("<ISA_TIME>--</ISA_TIME>");
                }
                if (resultSet.getString("STATUS") != null && !"".equals(resultSet.getString("STATUS"))) {
                    sb.append("<STATUS>" + resultSet.getString("STATUS") + "</STATUS>");
                } else {
                    sb.append("<STATUS>--</STATUS>");
                }
                if (resultSet.getString("REFERENCE") != null && !"".equals(resultSet.getString("REFERENCE"))) {
                    sb.append("<REFERENCE>" + resultSet.getString("REFERENCE") + "</REFERENCE>");
                } else {
                    sb.append("<REFERENCE>--</REFERENCE>");
                }
                if (resultSet.getString("PRE_TRANS_FILEPATH") != null && !"".equals(resultSet.getString("PRE_TRANS_FILEPATH"))) {
                    if (new File(resultSet.getString("PRE_TRANS_FILEPATH")).exists() && new File(resultSet.getString("PRE_TRANS_FILEPATH")).isFile()) {
                        sb.append("<PRETRANSFILEPATH>" + resultSet.getString("PRE_TRANS_FILEPATH") + "</PRETRANSFILEPATH>");
                    } else {
                        sb.append("<PRETRANSFILEPATH>No File</PRETRANSFILEPATH>");
                    }
                } else {
                    sb.append("<PRETRANSFILEPATH>No File</PRETRANSFILEPATH>");
                }

                if (resultSet.getString("POST_TRANS_FILEPATH") != null && !"".equals(resultSet.getString("POST_TRANS_FILEPATH"))) {
                    if (new File(resultSet.getString("POST_TRANS_FILEPATH")).exists() && new File(resultSet.getString("POST_TRANS_FILEPATH")).isFile()) {
                        sb.append("<POSTTRANSFILEPATH>" + resultSet.getString("POST_TRANS_FILEPATH") + "</POSTTRANSFILEPATH>");
                    } else {
                        sb.append("<POSTTRANSFILEPATH>No File</POSTTRANSFILEPATH>");
                    }
                } else {
                    sb.append("<POSTTRANSFILEPATH>No File</POSTTRANSFILEPATH>");
                }
                if (resultSet.getString("ACK_FILE_ID") != null && !"".equals(resultSet.getString("ACK_FILE_ID"))) {
                    if (new File(resultSet.getString("ACK_FILE_ID")).exists() && new File(resultSet.getString("ACK_FILE_ID")).isFile()) {
                        sb.append("<ACKFILEID>" + resultSet.getString("ACK_FILE_ID") + "</ACKFILEID>");
                    } else {
                        sb.append("<ACKFILEID>No File</ACKFILEID>");
                    }
                } else {
                    sb.append("<ACKFILEID>No File</ACKFILEID>");
                }

                if (resultSet.getString("ERR_MESSAGE") != null && !"".equals(resultSet.getString("ERR_MESSAGE"))) {
                    sb.append("<ERR_MESSAGE>" + resultSet.getString("ERR_MESSAGE") + "</ERR_MESSAGE>");
                } else {
                    sb.append("<ERR_MESSAGE>NO MSG</ERR_MESSAGE>");
                }
                if (resultSet.getString("ERROR_REPORT_FILEPATH") != null) {
                    if (new File(resultSet.getString("ERROR_REPORT_FILEPATH")).exists() && new File(resultSet.getString("ERROR_REPORT_FILEPATH")).isFile()) {
                        sb.append("<ERROR_REPORT_FILEPATH>" + resultSet.getString("ERROR_REPORT_FILEPATH") + "</ERROR_REPORT_FILEPATH>");
                    } else {
                        sb.append("<ERROR_REPORT_FILEPATH>No File</ERROR_REPORT_FILEPATH>");
                    }
                } else {
                    sb.append("<ERROR_REPORT_FILEPATH>No File</ERROR_REPORT_FILEPATH>");
                }

                sb.append("</DETAIL>");
                isGetting = true;
            }
            if (!isGetting) {
                isGetting = false;
                sb.append("<DETAIL><VALID>false</VALID></DETAIL>");
            }

            sb.append("</DETAILS>");
            sb.append("</xml>");
        } catch (SQLException sqlException) {
            LoggerUtility.log(logger, "SQLException occurred in getLtResponseDetails method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
        } catch (ServiceLocatorException serviceLocatorException) {
            LoggerUtility.log(logger, "ServiceLocatorException occurred in getLtResponseDetails method:: " + serviceLocatorException.getMessage(), Level.ERROR, serviceLocatorException.getCause());
        } finally {
            try {

                if (resultSet != null) {
                    resultSet.close();
                    resultSet = null;
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                    preparedStatement = null;
                }
                if (connection != null) {
                    connection.close();
                    connection = null;
                }
            } catch (SQLException sqlException) {
                LoggerUtility.log(logger, "finally SQLException occurred in getLtResponseDetails method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
            }
        }
        return sb.toString();
    }

    @Override
    public String getLogisticsInvDetails(String invNumber, int id, String database) throws ServiceLocatorException {
        System.out.println("getLogisticsInvDetails are" + invNumber);
        System.out.println("getLogisticsInvDetails are" + id);
        System.out.println("getLogisticsInvDetails are" + database);
        boolean isGetting = false;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        StringBuilder sb = new StringBuilder();
        String logisticsInvDetailsQuery = "";
        if ("ARCHIVE".equals(database)) {
            logisticsInvDetailsQuery = "SELECT ARCHIVE_FILES.TRANSACTION_TYPE as TRANSACTION_TYPE,ARCHIVE_FILES.ST_CONTROL_NUMBER as ST_CONTROL_NUMBER,ARCHIVE_FILES.GS_CONTROL_NUMBER as GS_CONTROL_NUMBER,"
                    + "TRANSPORT_INVOICE.FILE_ID as FILE_ID,ARCHIVE_FILES.SENDER_ID,ARCHIVE_FILES.RECEIVER_ID,TRANSPORT_INVOICE.INVOICE_NUMBER as INVOICE_NUMBER,TRANSPORT_INVOICE.PO_NUMBER as PO_NUMBER,"
                    + "TRANSPORT_INVOICE.TOTAL_WEIGHT as TOTAL_WEIGHT,TRANSPORT_INVOICE.TOTAL_AMOUNT as TOTAL_AMOUNT,"
                    + "ARCHIVE_FILES.ISA_NUMBER as ISA_NUMBER,ARCHIVE_FILES.ISA_DATE as ISA_DATE,ARCHIVE_FILES.ISA_TIME as ISA_TIME,"
                    + " ARCHIVE_FILES.PRE_TRANS_FILEPATH,ARCHIVE_FILES.POST_TRANS_FILEPATH,"
                    + "ARCHIVE_FILES.ORG_FILEPATH,ARCHIVE_FILES.ERR_MESSAGE,ARCHIVE_FILES.STATUS,ARCHIVE_FILES.ERROR_REPORT_FILEPATH as ERROR_REPORT_FILEPATH,"
                    + "ARCHIVE_FILES.ACK_FILE_ID as ACK_FILE_ID,ARCHIVE_FILES.FILE_TYPE as FILE_TYPE "
                    + " FROM TRANSPORT_INVOICE "
                    + "LEFT OUTER JOIN ARCHIVE_FILES ON (TRANSPORT_INVOICE.FILE_ID =ARCHIVE_FILES.FILE_ID) "
                    + " WHERE TRANSPORT_INVOICE.INVOICE_NUMBER LIKE '%" + invNumber + "%' AND TRANSPORT_INVOICE.ID =" + id;
        } else {
            logisticsInvDetailsQuery = "SELECT FILES.TRANSACTION_TYPE as TRANSACTION_TYPE,FILES.ST_CONTROL_NUMBER as ST_CONTROL_NUMBER,FILES.GS_CONTROL_NUMBER as GS_CONTROL_NUMBER,"
                    + "TRANSPORT_INVOICE.FILE_ID as FILE_ID,FILES.SENDER_ID,FILES.RECEIVER_ID,TRANSPORT_INVOICE.INVOICE_NUMBER as INVOICE_NUMBER,TRANSPORT_INVOICE.PO_NUMBER as PO_NUMBER,"
                    + "TRANSPORT_INVOICE.TOTAL_WEIGHT as TOTAL_WEIGHT,TRANSPORT_INVOICE.TOTAL_AMOUNT as TOTAL_AMOUNT,"
                    + "FILES.ISA_NUMBER as ISA_NUMBER,FILES.ISA_DATE as ISA_DATE,FILES.ISA_TIME as ISA_TIME,"
                    + " FILES.PRE_TRANS_FILEPATH,FILES.POST_TRANS_FILEPATH,"
                    + "FILES.ORG_FILEPATH,FILES.ERR_MESSAGE,FILES.STATUS,FILES.ERROR_REPORT_FILEPATH as ERROR_REPORT_FILEPATH,"
                    + "FILES.ACK_FILE_ID as ACK_FILE_ID,FILES.FILE_TYPE as FILE_TYPE "
                    + " FROM TRANSPORT_INVOICE "
                    + "LEFT OUTER JOIN FILES ON (TRANSPORT_INVOICE.FILE_ID =FILES.FILE_ID) "
                    + " WHERE TRANSPORT_INVOICE.INVOICE_NUMBER LIKE '%" + invNumber + "%' AND TRANSPORT_INVOICE.ID =" + id;
        }
        try {
            System.out.println("Invoice query is------" + logisticsInvDetailsQuery);
            Map<String, String> tradingPartners = DataSourceDataProvider.getInstance().getAllTradingPartners();
            System.out.println("after map");
            connection = ConnectionProvider.getInstance().getConnection();
            System.out.println("after connection");
            preparedStatement = connection.prepareStatement(logisticsInvDetailsQuery);
            System.out.println("after prepare statement");
            resultSet = preparedStatement.executeQuery();
            System.out.println("after result set");
            sb.append("<xml version=\"1.0\">");
            sb.append("<DETAILS>");
            while (resultSet.next()) {
                System.out.println("entered in while");
                sb.append("<DETAIL><VALID>true</VALID>");
                System.out.println("before file id");
                if (resultSet.getString("FILE_ID") != null && !"".equals(resultSet.getString("FILE_ID"))) {
                    sb.append("<FILEID>" + resultSet.getString("FILE_ID") + "</FILEID>");
                } else {
                    sb.append("<FILEID>--</FILEID>");
                }
                System.out.println("before file type");
                if (resultSet.getString("FILE_TYPE") != null && !"".equals(resultSet.getString("FILE_TYPE"))) {
                    sb.append("<FILETYPE>" + resultSet.getString("FILE_TYPE") + "</FILETYPE>");
                } else {
                    sb.append("<FILETYPE>--</FILETYPE>");
                }
                System.out.println("before invoice number");
                if (resultSet.getString("INVOICE_NUMBER") != null && !"".equals(resultSet.getString("INVOICE_NUMBER"))) {
                    sb.append("<INVNUMBER>" + resultSet.getString("INVOICE_NUMBER") + "</INVNUMBER>");
                } else {
                    sb.append("<INVNUMBER>--</INVNUMBER>");
                }
                System.out.println("po number");
                if (resultSet.getString("PO_NUMBER") != null && !"".equals(resultSet.getString("PO_NUMBER"))) {
                    sb.append("<PONUMBER>" + resultSet.getString("PO_NUMBER") + "</PONUMBER>");
                } else {
                    sb.append("<PONUMBER>--</PONUMBER>");
                }
                System.out.println("total weight");
                if (resultSet.getString("TOTAL_WEIGHT") != null && !"".equals(resultSet.getString("TOTAL_WEIGHT"))) {
                    sb.append("<ITEMQTY>" + resultSet.getString("TOTAL_WEIGHT") + "</ITEMQTY>");
                } else {
                    sb.append("<ITEMQTY>--</ITEMQTY>");
                }
                System.out.println("total  amount");
                if (resultSet.getString("TOTAL_AMOUNT") != null && !"".equals(resultSet.getString("TOTAL_AMOUNT"))) {
                    sb.append("<INVAMT>" + resultSet.getString("TOTAL_AMOUNT") + "</INVAMT>");
                } else {
                    sb.append("<INVAMT>--</INVAMT>");
                }
                if (resultSet.getString("ISA_NUMBER") != null && !"".equals(resultSet.getString("ISA_NUMBER"))) {
                    sb.append("<ISANUM>" + resultSet.getString("ISA_NUMBER") + "</ISANUM>");
                } else {
                    sb.append("<ISANUM>--</ISANUM>");
                }
                if (resultSet.getString("ISA_DATE") != null && !"".equals(resultSet.getString("ISA_DATE"))) {
                    sb.append("<ISADATE>" + resultSet.getString("ISA_DATE") + "</ISADATE>");
                } else {
                    sb.append("<ISADATE>--</ISADATE>");
                }
                if (resultSet.getString("ISA_TIME") != null && !"".equals(resultSet.getString("ISA_TIME"))) {
                    sb.append("<ISATIME>" + resultSet.getString("ISA_TIME") + "</ISATIME>");
                } else {
                    sb.append("<ISATIME>--</ISATIME>");
                }
                if (resultSet.getString("STATUS") != null && !"".equals(resultSet.getString("STATUS"))) {
                    sb.append("<STATUS>" + resultSet.getString("STATUS") + "</STATUS>");
                } else {
                    sb.append("<STATUS>--</STATUS>");
                }
                if (resultSet.getString("SENDER_ID") != null && !"".equals(resultSet.getString("SENDER_ID"))) {
                    sb.append("<SENDER_ID>" + resultSet.getString("SENDER_ID") + "</SENDER_ID>");
                } else {
                    sb.append("<SENDER_ID>--</SENDER_ID>");
                }
                if (resultSet.getString("RECEIVER_ID") != null && !"".equals(resultSet.getString("RECEIVER_ID"))) {
                    sb.append("<RECEIVER_ID>" + resultSet.getString("RECEIVER_ID") + "</RECEIVER_ID>");
                } else {
                    sb.append("<RECEIVER_ID>--</RECEIVER_ID>");
                }
                System.out.println("sender name");
                if (resultSet.getString("SENDER_ID") != null && (((tradingPartners.get(resultSet.getString("SENDER_ID")))) != null)) {
                    sb.append("<SENDER_NAME>" + (tradingPartners.get(resultSet.getString("SENDER_ID"))).toString() + "</SENDER_NAME>");
                } else {
                    sb.append("<SENDER_NAME>--</SENDER_NAME>");
                }
                System.out.println("receiver name");
                if (resultSet.getString("RECEIVER_ID") != null && (((tradingPartners.get(resultSet.getString("RECEIVER_ID")))) != null)) {
                    sb.append("<RECEIVER_NAME>" + (tradingPartners.get(resultSet.getString("RECEIVER_ID"))).toString() + "</RECEIVER_NAME>");
                } else {
                    sb.append("<RECEIVER_NAME>--</RECEIVER_NAME>");
                }
                if (resultSet.getString("PRE_TRANS_FILEPATH") != null) {
                    if (new File(resultSet.getString("PRE_TRANS_FILEPATH")).exists() && new File(resultSet.getString("PRE_TRANS_FILEPATH")).isFile()) {
                        sb.append("<PRETRANSFILEPATH>" + resultSet.getString("PRE_TRANS_FILEPATH") + "</PRETRANSFILEPATH>");
                    } else {
                        sb.append("<PRETRANSFILEPATH>No File</PRETRANSFILEPATH>");
                    }
                } else {
                    sb.append("<PRETRANSFILEPATH>No File</PRETRANSFILEPATH>");
                }

                if (resultSet.getString("POST_TRANS_FILEPATH") != null) {
                    if (new File(resultSet.getString("POST_TRANS_FILEPATH")).exists() && new File(resultSet.getString("POST_TRANS_FILEPATH")).isFile()) {
                        sb.append("<POSTTRANSFILEPATH>" + resultSet.getString("POST_TRANS_FILEPATH") + "</POSTTRANSFILEPATH>");
                    } else {
                        sb.append("<POSTTRANSFILEPATH>No File</POSTTRANSFILEPATH>");
                    }
                } else {
                    sb.append("<POSTTRANSFILEPATH>No File</POSTTRANSFILEPATH>");
                }

                if (resultSet.getString("ORG_FILEPATH") != null) {
                    if (new File(resultSet.getString("ORG_FILEPATH")).exists() && new File(resultSet.getString("ORG_FILEPATH")).isFile()) {
                        sb.append("<ORGFILEPATH>" + resultSet.getString("ORG_FILEPATH") + "</ORGFILEPATH>");
                    } else {
                        sb.append("<ORGFILEPATH>No File</ORGFILEPATH>");
                    }
                } else {
                    sb.append("<ORGFILEPATH>No File</ORGFILEPATH>");
                }

                if (resultSet.getString("ACK_FILE_ID") != null) {
                    if (new File(resultSet.getString("ACK_FILE_ID")).exists() && new File(resultSet.getString("ACK_FILE_ID")).isFile()) {
                        sb.append("<ACKFILEID>" + resultSet.getString("ACK_FILE_ID") + "</ACKFILEID>");
                    } else {
                        sb.append("<ACKFILEID>No File</ACKFILEID>");
                    }
                } else {
                    sb.append("<ACKFILEID>No File</ACKFILEID>");
                }

                if (resultSet.getString("ERR_MESSAGE") != null && !"".equals(resultSet.getString("ERR_MESSAGE"))) {
                    sb.append("<ERR_MESSAGE>" + resultSet.getString("ERR_MESSAGE") + "</ERR_MESSAGE>");
                } else {
                    sb.append("<ERR_MESSAGE>NO MSG</ERR_MESSAGE>");
                }

                if (resultSet.getString("TRANSACTION_TYPE") != null && !"".equals(resultSet.getString("TRANSACTION_TYPE"))) {
                    sb.append("<TRANSACTION_TYPE>" + resultSet.getString("TRANSACTION_TYPE") + "</TRANSACTION_TYPE>");
                } else {
                    sb.append("<TRANSACTION_TYPE>--</TRANSACTION_TYPE>");
                }
                if (resultSet.getString("ST_CONTROL_NUMBER") != null && !"".equals(resultSet.getString("ST_CONTROL_NUMBER"))) {
                    sb.append("<ST_CONTROL_NUMBER>" + resultSet.getString("ST_CONTROL_NUMBER") + "</ST_CONTROL_NUMBER>");
                } else {
                    sb.append("<ST_CONTROL_NUMBER>--</ST_CONTROL_NUMBER>");
                }
                if (resultSet.getString("GS_CONTROL_NUMBER") != null && !"".equals(resultSet.getString("GS_CONTROL_NUMBER"))) {
                    sb.append("<GS_CONTROL_NUMBER>" + resultSet.getString("GS_CONTROL_NUMBER") + "</GS_CONTROL_NUMBER>");
                } else {
                    sb.append("<GS_CONTROL_NUMBER>--</GS_CONTROL_NUMBER>");
                }
                if (resultSet.getString("ERROR_REPORT_FILEPATH") != null) {
                    if (new File(resultSet.getString("ERROR_REPORT_FILEPATH")).exists() && new File(resultSet.getString("ERROR_REPORT_FILEPATH")).isFile()) {
                        sb.append("<ERROR_REPORT_FILEPATH>" + resultSet.getString("ERROR_REPORT_FILEPATH") + "</ERROR_REPORT_FILEPATH>");
                    } else {
                        sb.append("<ERROR_REPORT_FILEPATH>No File</ERROR_REPORT_FILEPATH>");
                    }
                } else {
                    sb.append("<ERROR_REPORT_FILEPATH>No File</ERROR_REPORT_FILEPATH>");
                }

                sb.append("</DETAIL>");
                isGetting = true;
            }
            if (!isGetting) {
                isGetting = false;
                sb.append("<DETAIL><VALID>false</VALID></DETAIL>");
            }

            sb.append("</DETAILS>");
            sb.append("</xml>");
        } catch (SQLException sqlException) {
            LoggerUtility.log(logger, "SQLException occurred in getLogisticsInvDetails method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
        } catch (ServiceLocatorException serviceLocatorException) {
            LoggerUtility.log(logger, "ServiceLocatorException occurred in getLogisticsInvDetails method:: " + serviceLocatorException.getMessage(), Level.ERROR, serviceLocatorException.getCause());
        } finally {
            try {

                if (resultSet != null) {
                    resultSet.close();
                    resultSet = null;
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                    preparedStatement = null;
                }
                if (connection != null) {
                    connection.close();
                    connection = null;
                }
            } catch (SQLException sqlException) {
                LoggerUtility.log(logger, "finally SQLException occurred in getLogisticsInvDetails method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
            }
        }
        return sb.toString();
    }

    @Override
    public String getLogisticsShipmentDetails(String asnNumber, String poNumber, int id, String database) throws ServiceLocatorException {
        System.out.println("getLogisticsShipmentDetails are" + asnNumber);
        System.out.println("getLogisticsShipmentDetails are" + poNumber);
        System.out.println("getLogisticsShipmentDetails are" + id);
        System.out.println("getLogisticsShipmentDetails are" + database);
        boolean isGetting = false;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        StringBuilder sb = new StringBuilder();
        String logisticsShipmentDetailsQuery = "";
        if ("ARCHIVE".equals(database)) {
            logisticsShipmentDetailsQuery = "SELECT ARCHIVE_FILES.TRANSACTION_TYPE as TRANSACTION_TYPE,ARCHIVE_FILES.ST_CONTROL_NUMBER as ST_CONTROL_NUMBER,ARCHIVE_FILES.GS_CONTROL_NUMBER as GS_CONTROL_NUMBER,"
                    + "ARCHIVE_TRANSPORT_SHIPMENT.FILE_ID as FILE_ID,ARCHIVE_FILES.SENDER_ID,ARCHIVE_FILES.RECEIVER_ID,ARCHIVE_TRANSPORT_SHIPMENT.SHIPMENT_ID as SHIPMENT_ID,ARCHIVE_TRANSPORT_SHIPMENT.PO_NUMBER as PO_NUMBER,"
                    + "ARCHIVE_TRANSPORT_SHIPMENT.TOTAL_WEIGHT as TOTAL_WEIGHT,ARCHIVE_TRANSPORT_SHIPMENT.TOTAL_VOLUME as TOTAL_VOLUME,"
                    + "ARCHIVE_FILES.ISA_NUMBER as ISA_NUMBER,ARCHIVE_FILES.ISA_DATE as ISA_DATE,ARCHIVE_FILES.ISA_TIME as ISA_TIME,"
                    + " ARCHIVE_FILES.PRE_TRANS_FILEPATH,ARCHIVE_FILES.POST_TRANS_FILEPATH,ARCHIVE_FILES.SENDER_ID,ARCHIVE_FILES.RECEIVER_ID,"
                    + "ARCHIVE_FILES.ORG_FILEPATH,ARCHIVE_FILES.ERR_MESSAGE,ARCHIVE_FILES.STATUS,ARCHIVE_FILES.ERROR_REPORT_FILEPATH as ERROR_REPORT_FILEPATH,"
                    + "ARCHIVE_FILES.ACK_FILE_ID as ACK_FILE_ID,ARCHIVE_FILES.FILE_TYPE as FILE_TYPE "
                    + " FROM ARCHIVE_TRANSPORT_SHIPMENT "
                    + "LEFT OUTER JOIN ARCHIVE_FILES ON (ARCHIVE_TRANSPORT_SHIPMENT.FILE_ID =ARCHIVE_FILES.FILE_ID) "
                    + " WHERE ARCHIVE_TRANSPORT_SHIPMENT.SHIPMENT_ID LIKE '%" + asnNumber + "%'  AND ARCHIVE_TRANSPORT_SHIPMENT.ID =" + id;
        } else {
            logisticsShipmentDetailsQuery = "SELECT FILES.TRANSACTION_TYPE as TRANSACTION_TYPE,FILES.ST_CONTROL_NUMBER as ST_CONTROL_NUMBER,FILES.GS_CONTROL_NUMBER as GS_CONTROL_NUMBER,"
                    + "TRANSPORT_SHIPMENT.FILE_ID as FILE_ID,FILES.SENDER_ID,FILES.RECEIVER_ID,TRANSPORT_SHIPMENT.SHIPMENT_ID as SHIPMENT_ID,TRANSPORT_SHIPMENT.PO_NUMBER as PO_NUMBER,"
                    + "TRANSPORT_SHIPMENT.TOTAL_WEIGHT as TOTAL_WEIGHT,TRANSPORT_SHIPMENT.TOTAL_VOLUME as TOTAL_VOLUME,"
                    + "FILES.ISA_NUMBER as ISA_NUMBER,FILES.ISA_DATE as ISA_DATE,FILES.ISA_TIME as ISA_TIME,"
                    + " FILES.PRE_TRANS_FILEPATH,FILES.POST_TRANS_FILEPATH,FILES.SENDER_ID,FILES.RECEIVER_ID,"
                    + "FILES.ORG_FILEPATH,FILES.ERR_MESSAGE,FILES.STATUS,FILES.ERROR_REPORT_FILEPATH as ERROR_REPORT_FILEPATH,"
                    + "FILES.ACK_FILE_ID as ACK_FILE_ID,FILES.FILE_TYPE as FILE_TYPE "
                    + " FROM TRANSPORT_SHIPMENT "
                    + "LEFT OUTER JOIN FILES ON (TRANSPORT_SHIPMENT.FILE_ID =FILES.FILE_ID) "
                    + " WHERE TRANSPORT_SHIPMENT.SHIPMENT_ID LIKE '%" + asnNumber + "%'  AND TRANSPORT_SHIPMENT.ID =" + id;
        }
        try {
            Map<String, String> tradingPartners = DataSourceDataProvider.getInstance().getAllTradingPartners();
            connection = ConnectionProvider.getInstance().getConnection();
            preparedStatement = connection.prepareStatement(logisticsShipmentDetailsQuery);
            resultSet = preparedStatement.executeQuery();
            sb.append("<xml version=\"1.0\">");
            sb.append("<DETAILS>");
            while (resultSet.next()) {
                sb.append("<DETAIL><VALID>true</VALID>");
                if (resultSet.getString("FILE_ID") != null && !"".equals(resultSet.getString("FILE_ID"))) {
                    sb.append("<FILEID>" + resultSet.getString("FILE_ID") + "</FILEID>");
                } else {
                    sb.append("<FILEID>--</FILEID>");
                }
                if (resultSet.getString("FILE_TYPE") != null && !"".equals(resultSet.getString("FILE_TYPE"))) {
                    sb.append("<FILETYPE>" + resultSet.getString("FILE_TYPE") + "</FILETYPE>");
                } else {
                    sb.append("<FILETYPE>--</FILETYPE>");
                }
                if (resultSet.getString("SHIPMENT_ID") != null && !"".equals(resultSet.getString("SHIPMENT_ID"))) {
                    sb.append("<ASNNUMBER>" + resultSet.getString("SHIPMENT_ID") + "</ASNNUMBER>");
                } else {
                    sb.append("<ASNNUMBER>--</ASNNUMBER>");
                }
                if (resultSet.getString("PO_NUMBER") != null && !"".equals(resultSet.getString("PO_NUMBER"))) {
                    sb.append("<PONUMBER>" + resultSet.getString("PO_NUMBER") + "</PONUMBER>");
                } else {
                    sb.append("<PONUMBER>--</PONUMBER>");
                }
                if (resultSet.getString("TOTAL_WEIGHT") != null && !"".equals(resultSet.getString("TOTAL_WEIGHT"))) {
                    sb.append("<ITEMQTY>" + resultSet.getString("TOTAL_WEIGHT") + "</ITEMQTY>");
                } else {
                    sb.append("<ITEMQTY>--</ITEMQTY>");
                }
                if (resultSet.getString("TOTAL_VOLUME") != null && !"".equals(resultSet.getString("TOTAL_VOLUME"))) {
                    sb.append("<ASNVOLUME>" + resultSet.getString("TOTAL_VOLUME") + "</ASNVOLUME>");
                } else {
                    sb.append("<ASNVOLUME>--</ASNVOLUME>");
                }
                if (resultSet.getString("ISA_NUMBER") != null && !"".equals(resultSet.getString("ISA_NUMBER"))) {
                    sb.append("<ISANUM>" + resultSet.getString("ISA_NUMBER") + "</ISANUM>");
                } else {
                    sb.append("<ISANUM>--</ISANUM>");
                }
                if (resultSet.getString("ISA_DATE") != null && !"".equals(resultSet.getString("ISA_DATE"))) {
                    sb.append("<ISADATE>" + resultSet.getString("ISA_DATE") + "</ISADATE>");
                } else {
                    sb.append("<ISADATE>--</ISADATE>");
                }
                if (resultSet.getString("ISA_TIME") != null && !"".equals(resultSet.getString("ISA_TIME"))) {
                    sb.append("<ISATIME>" + resultSet.getString("ISA_TIME") + "</ISATIME>");
                } else {
                    sb.append("<ISATIME>--</ISATIME>");
                }
                if (resultSet.getString("STATUS") != null && !"".equals(resultSet.getString("STATUS"))) {
                    sb.append("<STATUS>" + resultSet.getString("STATUS") + "</STATUS>");
                } else {
                    sb.append("<STATUS>--</STATUS>");
                }
                if (resultSet.getString("SENDER_ID") != null && !"".equals(resultSet.getString("SENDER_ID"))) {
                    sb.append("<SENDER_ID>" + resultSet.getString("SENDER_ID") + "</SENDER_ID>");
                } else {
                    sb.append("<SENDER_ID>--</SENDER_ID>");
                }
                if (resultSet.getString("FILE_ID") != null && !"".equals(resultSet.getString("FILE_ID"))) {
                    sb.append("<RECEIVER_ID>" + resultSet.getString("RECEIVER_ID") + "</RECEIVER_ID>");
                } else {
                    sb.append("<FILEID>--</FILEID>");
                }
                if (resultSet.getString("SENDER_ID") != null && !"".equals(resultSet.getString("SENDER_ID"))) {
                    sb.append("<SENDER_ID>" + resultSet.getString("SENDER_ID") + "</SENDER_ID>");
                } else {
                    sb.append("<SENDER_ID>--</SENDER_ID>");
                }
                if (resultSet.getString("RECEIVER_ID") != null && !"".equals(resultSet.getString("RECEIVER_ID"))) {
                    sb.append("<RECEIVER_ID>" + resultSet.getString("RECEIVER_ID") + "</RECEIVER_ID>");
                } else {
                    sb.append("<RECEIVER_ID>--</RECEIVER_ID>");
                }
                if (resultSet.getString("SENDER_ID") != null && (((tradingPartners.get(resultSet.getString("SENDER_ID")))) != null)) {
                    sb.append("<SENDER_NAME>" + (tradingPartners.get(resultSet.getString("SENDER_ID"))).toString() + "</SENDER_NAME>");
                } else {
                    sb.append("<SENDER_NAME>--</SENDER_NAME>");
                }
                if (resultSet.getString("RECEIVER_ID") != null && (((tradingPartners.get(resultSet.getString("RECEIVER_ID")))) != null)) {
                    sb.append("<RECEIVER_NAME>" + (tradingPartners.get(resultSet.getString("RECEIVER_ID"))).toString() + "</RECEIVER_NAME>");
                } else {
                    sb.append("<RECEIVER_NAME>--</RECEIVER_NAME>");
                }

                if (resultSet.getString("PRE_TRANS_FILEPATH") != null) {
                    if (new File(resultSet.getString("PRE_TRANS_FILEPATH")).exists() && new File(resultSet.getString("PRE_TRANS_FILEPATH")).isFile()) {
                        sb.append("<PRETRANSFILEPATH>" + resultSet.getString("PRE_TRANS_FILEPATH") + "</PRETRANSFILEPATH>");
                    } else {
                        sb.append("<PRETRANSFILEPATH>No File</PRETRANSFILEPATH>");
                    }
                } else {
                    sb.append("<PRETRANSFILEPATH>No File</PRETRANSFILEPATH>");
                }

                if (resultSet.getString("POST_TRANS_FILEPATH") != null) {
                    if (new File(resultSet.getString("POST_TRANS_FILEPATH")).exists() && new File(resultSet.getString("POST_TRANS_FILEPATH")).isFile()) {
                        sb.append("<POSTTRANSFILEPATH>" + resultSet.getString("POST_TRANS_FILEPATH") + "</POSTTRANSFILEPATH>");
                    } else {
                        sb.append("<POSTTRANSFILEPATH>No File</POSTTRANSFILEPATH>");
                    }
                } else {
                    sb.append("<POSTTRANSFILEPATH>No File</POSTTRANSFILEPATH>");
                }

                if (resultSet.getString("ORG_FILEPATH") != null) {
                    if (new File(resultSet.getString("ORG_FILEPATH")).exists() && new File(resultSet.getString("ORG_FILEPATH")).isFile()) {
                        sb.append("<ORGFILEPATH>" + resultSet.getString("ORG_FILEPATH") + "</ORGFILEPATH>");
                    } else {
                        sb.append("<ORGFILEPATH>No File</ORGFILEPATH>");
                    }
                } else {
                    sb.append("<ORGFILEPATH>No File</ORGFILEPATH>");
                }

                if (resultSet.getString("ACK_FILE_ID") != null) {
                    if (new File(resultSet.getString("ACK_FILE_ID")).exists() && new File(resultSet.getString("ACK_FILE_ID")).isFile()) {
                        sb.append("<ACKFILEID>" + resultSet.getString("ACK_FILE_ID") + "</ACKFILEID>");
                    } else {
                        sb.append("<ACKFILEID>No File</ACKFILEID>");
                    }
                } else {
                    sb.append("<ACKFILEID>No File</ACKFILEID>");
                }

                if (resultSet.getString("ERR_MESSAGE") != null && !"".equals(resultSet.getString("ERR_MESSAGE"))) {
                    sb.append("<ERR_MESSAGE>" + resultSet.getString("ERR_MESSAGE") + "</ERR_MESSAGE>");
                } else {
                    sb.append("<ERR_MESSAGE>NO MSG</ERR_MESSAGE>");
                }

                if (resultSet.getString("TRANSACTION_TYPE") != null && !"".equals(resultSet.getString("TRANSACTION_TYPE"))) {
                    sb.append("<TRANSACTION_TYPE>" + resultSet.getString("TRANSACTION_TYPE") + "</TRANSACTION_TYPE>");
                } else {
                    sb.append("<TRANSACTION_TYPE>--</TRANSACTION_TYPE>");
                }
                if (resultSet.getString("ST_CONTROL_NUMBER") != null && !"".equals(resultSet.getString("ST_CONTROL_NUMBER"))) {
                    sb.append("<ST_CONTROL_NUMBER>" + resultSet.getString("ST_CONTROL_NUMBER") + "</ST_CONTROL_NUMBER>");
                } else {
                    sb.append("<ST_CONTROL_NUMBER>--</ST_CONTROL_NUMBER>");
                }
                if (resultSet.getString("GS_CONTROL_NUMBER") != null && !"".equals(resultSet.getString("GS_CONTROL_NUMBER"))) {
                    sb.append("<GS_CONTROL_NUMBER>" + resultSet.getString("GS_CONTROL_NUMBER") + "</GS_CONTROL_NUMBER>");
                } else {
                    sb.append("<GS_CONTROL_NUMBER>--</GS_CONTROL_NUMBER>");
                }

                if (resultSet.getString("ERROR_REPORT_FILEPATH") != null) {
                    if (new File(resultSet.getString("ERROR_REPORT_FILEPATH")).exists() && new File(resultSet.getString("ERROR_REPORT_FILEPATH")).isFile()) {
                        sb.append("<ERROR_REPORT_FILEPATH>" + resultSet.getString("ERROR_REPORT_FILEPATH") + "</ERROR_REPORT_FILEPATH>");
                    } else {
                        sb.append("<ERROR_REPORT_FILEPATH>No File</ERROR_REPORT_FILEPATH>");
                    }
                } else {
                    sb.append("<ERROR_REPORT_FILEPATH>No File</ERROR_REPORT_FILEPATH>");
                }

                sb.append("</DETAIL>");
                isGetting = true;
            }
            if (!isGetting) {
                isGetting = false;
                sb.append("<DETAIL><VALID>false</VALID></DETAIL>");
            }
            sb.append("</DETAILS>");
            sb.append("</xml>");
        } catch (SQLException sqlException) {
            LoggerUtility.log(logger, "SQLException occurred in getLogisticsShipmentDetails method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
        } catch (ServiceLocatorException serviceLocatorException) {
            LoggerUtility.log(logger, "ServiceLocatorException occurred in getLogisticsShipmentDetails method:: " + serviceLocatorException.getMessage(), Level.ERROR, serviceLocatorException.getCause());
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                    resultSet = null;
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                    preparedStatement = null;
                }
                if (connection != null) {
                    connection.close();
                    connection = null;
                }
            } catch (SQLException sqlException) {
                LoggerUtility.log(logger, "finally SQLException occurred in getLogisticsShipmentDetails method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
            }
        }
        return sb.toString();
    }

    @Override
    public String getDocVisibilityDetails(int docId) throws ServiceLocatorException {
        Connection connection = null;
        CallableStatement callableStatement = null;
        StringBuilder sb = new StringBuilder();
        String responseString = null;
        boolean isGetting = false;
        try {
            connection = ConnectionProvider.getInstance().getConnection();
            callableStatement = connection.prepareCall("CALL SPDOCVISIBILITY(?,?)");
            callableStatement.setInt(1, docId);
            callableStatement.registerOutParameter(2, Types.VARCHAR);
            boolean isExecute = callableStatement.execute();
            responseString = callableStatement.getString(2);
            sb.append("<xml version=\"1.0\">");
            sb.append("<DETAILS>");
            if (responseString != null && !"".equals(responseString)) {
                if (!"".equals(responseString.trim())) {
                    sb.append("<DETAIL><VALID>true</VALID>");
                    String detailInfo[] = responseString.split("\\^");

                    if (detailInfo[0] != null && !"".equals(detailInfo[0])) {
                        if (!"".equals(detailInfo[0].trim())) {
                            String commonInfo[] = detailInfo[0].split("\\|");
                            sb.append("<FILE_ID>" + commonInfo[0] + "</FILE_ID>");
                            sb.append("<PARENT_FILE_ID>" + commonInfo[1] + "</PARENT_FILE_ID>");
                            sb.append("<FILE_TYPE>" + commonInfo[2] + "</FILE_TYPE>");
                            sb.append("<FILE_ORIGIN>" + commonInfo[3] + "</FILE_ORIGIN>");
                            sb.append("<TRAN_MESS_TYPE>" + commonInfo[4] + "</TRAN_MESS_TYPE>");
                            sb.append("<SENDER_ID>" + commonInfo[5] + "</SENDER_ID>");
                            sb.append("<RECEIVER_ID>" + commonInfo[6] + "</RECEIVER_ID>");
                            sb.append("<INTERCHANGE_CONTROLNO>" + commonInfo[7] + "</INTERCHANGE_CONTROLNO>");
                            sb.append("<FUNCTIONAL_CONTROLNO>" + commonInfo[8] + "</FUNCTIONAL_CONTROLNO>");
                            sb.append("<MESSAGE_CONTROLNO>" + commonInfo[9] + "</MESSAGE_CONTROLNO>");
                            sb.append("<DATE_TIME_RECEIVED>" + commonInfo[10] + "</DATE_TIME_RECEIVED>");
                            sb.append("<DIRECTION>" + commonInfo[11] + "</DIRECTION>");
                            sb.append("<STATUS>" + commonInfo[12] + "</STATUS>");
                            sb.append("<ERR_MESSAGE>" + commonInfo[13] + "</ERR_MESSAGE>");
                            sb.append("<ACK_STATUS>" + commonInfo[14] + "</ACK_STATUS>");
                            sb.append("<ID>" + commonInfo[15] + "</ID>");
                            sb.append("<ISA_TIME>" + commonInfo[16] + "</ISA_TIME>");
                            sb.append("<ISA_DATE>" + commonInfo[17] + "</ISA_DATE>");
                        }

                    }

                    if (detailInfo[1] != null && !"".equals(detailInfo[1])) {
                        if (!"".equals(detailInfo[1].trim())) {
                            String appFieldInfo[] = detailInfo[1].split("\\*");
                            sb.append("<APPFIELDS>");
                            for (int i = 0; i < appFieldInfo.length; i++) {
                                sb.append("<APPFIELD label=\"" + appFieldInfo[i].split("\\@")[0] + "\">");
                                if (appFieldInfo[i].split("\\@").length == 2) {
                                    sb.append(appFieldInfo[i].split("\\@")[1]);
                                } else {
                                    sb.append("_");
                                }
                                sb.append("</APPFIELD>");
                            }
                            sb.append("</APPFIELDS>");
                        }
                    }
                    sb.append("</DETAIL>");
                    isGetting = true;
                }
            }
            if (!isGetting) {
                isGetting = false;
                sb.append("<DETAIL><VALID>false</VALID></DETAIL>");
            }
            sb.append("</DETAILS>");
            sb.append("</xml>");
        } catch (SQLException sqlException) {
            LoggerUtility.log(logger, "SQLException occurred in getDocVisibilityDetails method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
        } catch (ServiceLocatorException serviceLocatorException) {
            LoggerUtility.log(logger, "ServiceLocatorException occurred in getDocVisibilityDetails method:: " + serviceLocatorException.getMessage(), Level.ERROR, serviceLocatorException.getCause());
        } finally {
            try {
                if (callableStatement != null) {
                    callableStatement.close();
                    callableStatement = null;
                }

                if (connection != null) {
                    connection.close();
                    connection = null;
                }
            } catch (SQLException sqlException) {
                LoggerUtility.log(logger, "SQLException occurred in getDocVisibilityDetails method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
            }
        }
        return sb.toString();
    }

    @Override
    public String getPartnerDetails(String partnerId) throws ServiceLocatorException {
        System.out.println("PartnerId is in partnerDetails   "+partnerId);
        boolean isGetting = false;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        StringBuilder sb = new StringBuilder();
        String partnerDetailsQuery = "";
        partnerDetailsQuery = "select tp.ID as TP_ID,tp.NAME as TP_NAME,tp_details.INTERNALIDENTIFIER,tp_details.APPLICATIONID,tp_details.STATE,tp.STATUS,tp.MODIFIED_TS,tp.MODIFIED_BY,tp.CREATED_TS from tp LEFT OUTER JOIN tp_details on(tp_details.TP_ID=tp.ID) WHERE 1=1 AND tp.ID='" + partnerId + "'";
        try {
            connection = ConnectionProvider.getInstance().getConnection();
            preparedStatement = connection.prepareStatement(partnerDetailsQuery);
            resultSet = preparedStatement.executeQuery();
            sb.append("<xml version=\"1.0\">");
            sb.append("<DETAILS>");
            while (resultSet.next()) {
                sb.append("<DETAIL><VALID>true</VALID>");
                if (resultSet.getString("TP_ID") != null && !"".equals(resultSet.getString("TP_ID"))) {
                    sb.append("<TP_ID>" + resultSet.getString("TP_ID") + "</TP_ID>");
                } else {
                    sb.append("<TP_ID>--</TP_ID>");
                }
                if (resultSet.getString("TP_NAME") != null && !"".equals(resultSet.getString("TP_NAME"))) {
                    sb.append("<TP_NAME>" + resultSet.getString("TP_NAME") + "</TP_NAME>");
                } else {
                    sb.append("<TP_NAME>--</TP_NAME>");
                }
                if (resultSet.getString("INTERNALIDENTIFIER") != null && !"".equals(resultSet.getString("INTERNALIDENTIFIER"))) {
                    sb.append("<INTERNALIDENTIFIER>" + resultSet.getString("INTERNALIDENTIFIER") + "</INTERNALIDENTIFIER>");
                } else {
                    sb.append("<INTERNALIDENTIFIER>--</INTERNALIDENTIFIER>");
                }
                if (resultSet.getString("APPLICATIONID") != null && !"".equals(resultSet.getString("APPLICATIONID"))) {
                    sb.append("<APPLICATIONID>" + resultSet.getString("APPLICATIONID") + "</APPLICATIONID>");
                } else {
                    sb.append("<APPLICATIONID>--</APPLICATIONID>");
                }
                if (resultSet.getString("STATE") != null && !"".equals(resultSet.getString("STATE"))) {
                    sb.append("<STATE>" + resultSet.getString("STATE") + "</STATE>");
                } else {
                    sb.append("<STATE>--</STATE>");
                }
                if (resultSet.getString("MODIFIED_TS") != null && !"".equals(resultSet.getString("MODIFIED_TS"))) {
                    sb.append("<MODIFIED_TS>" + resultSet.getString("MODIFIED_TS") + "</MODIFIED_TS>");
                } else {
                    sb.append("<MODIFIED_TS>--</MODIFIED_TS>");
                }
                if (resultSet.getString("MODIFIED_BY") != null && !"".equals(resultSet.getString("MODIFIED_BY"))) {
                    sb.append("<MODIFIED_BY>" + resultSet.getString("MODIFIED_BY") + "</MODIFIED_BY>");
                } else {
                    sb.append("<MODIFIED_BY>--</MODIFIED_BY>");
                }
                if (resultSet.getString("CREATED_TS") != null && !"".equals(resultSet.getString("CREATED_TS"))) {
                    sb.append("<CREATED_TS>" + resultSet.getString("CREATED_TS") + "</CREATED_TS>");
                } else {
                    sb.append("<CREATED_TS>--</CREATED_TS>");
                }

                sb.append("</DETAIL>");
                isGetting = true;
            }
            if (!isGetting) {
                isGetting = false;
                sb.append("<DETAIL><VALID>false</VALID></DETAIL>");
            }

            sb.append("</DETAILS>");
            sb.append("</xml>");
        } catch (SQLException sqlException) {
            LoggerUtility.log(logger, "SQLException occurred in getPartnerDetails method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
        } catch (ServiceLocatorException serviceLocatorException) {
            LoggerUtility.log(logger, "ServiceLocatorException occurred in getPartnerDetails method:: " + serviceLocatorException.getMessage(), Level.ERROR, serviceLocatorException.getCause());
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                    resultSet = null;
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                    preparedStatement = null;
                }
                if (connection != null) {
                    connection.close();
                    connection = null;
                }
            } catch (SQLException sqlException) {
                LoggerUtility.log(logger, "finally SQLException occurred in getPartnerDetails method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
            }
        }
        return sb.toString();
    }

    @Override
    public String getRoutingDetails(String routingId) throws ServiceLocatorException {
        boolean isGetting = false;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        StringBuilder sb = new StringBuilder();
        String routingDetailsQuery = "";
        routingDetailsQuery = "SELECT * FROM ROUTERINFO WHERE ROUTER_ID=" + routingId;
        try {
            connection = ConnectionProvider.getInstance().getConnection();
            preparedStatement = connection.prepareStatement(routingDetailsQuery);
            resultSet = preparedStatement.executeQuery();
            sb.append("<xml version=\"1.0\">");
            sb.append("<DETAILS>");
            while (resultSet.next()) {
                sb.append("<DETAIL><VALID>true</VALID>");
                sb.append("<ROUTER_ID>" + resultSet.getString("ROUTER_ID") + "</ROUTER_ID>");
                sb.append("<ROUTER_NAME>" + resultSet.getString("ROUTER_NAME") + "</ROUTER_NAME>");

                if (resultSet.getString("STATUS") != null && !"".equals(resultSet.getString("STATUS"))) {
                    sb.append("<STATUS>" + resultSet.getString("STATUS") + "</STATUS>");
                } else {
                    sb.append("<STATUS>--</STATUS>");
                }
                if (resultSet.getString("ACCEPTORLOOKUPALIAS") != null && !"".equals(resultSet.getString("ACCEPTORLOOKUPALIAS"))) {
                    sb.append("<ACCEPTORLOOKUPALIAS>" + resultSet.getString("ACCEPTORLOOKUPALIAS") + "</ACCEPTORLOOKUPALIAS>");
                } else {
                    sb.append("<ACCEPTORLOOKUPALIAS>--</ACCEPTORLOOKUPALIAS>");
                }
                if (resultSet.getString("INTERNALROUTEREMAIL") != null && !"".equals(resultSet.getString("INTERNALROUTEREMAIL"))) {
                    sb.append("<INTERNALROUTEREMAIL>" + resultSet.getString("INTERNALROUTEREMAIL") + "</INTERNALROUTEREMAIL>");
                } else {
                    sb.append("<INTERNALROUTEREMAIL>--</INTERNALROUTEREMAIL>");
                }
                if (resultSet.getString("DESTMAILBOX") != null && !"".equals(resultSet.getString("DESTMAILBOX"))) {
                    sb.append("<DESTMAILBOX>" + resultSet.getString("DESTMAILBOX") + "</DESTMAILBOX>");
                } else {
                    sb.append("<DESTMAILBOX>--</DESTMAILBOX>");
                }
                if (resultSet.getString("SYSTEMTYPE") != null && !"".equals(resultSet.getString("SYSTEMTYPE"))) {
                    sb.append("<SYSTEMTYPE>" + resultSet.getString("SYSTEMTYPE") + "</SYSTEMTYPE>");
                } else {
                    sb.append("<SYSTEMTYPE>--</SYSTEMTYPE>");
                }
                if (resultSet.getString("DIRECTION") != null && !"".equals(resultSet.getString("DIRECTION"))) {
                    sb.append("<DIRECTION>" + resultSet.getString("DIRECTION") + "</DIRECTION>");
                } else {
                    sb.append("<DIRECTION>--</DIRECTION>");
                }
                if (resultSet.getString("ENVELOPE") != null && !"".equals(resultSet.getString("ENVELOPE"))) {
                    sb.append("<ENVELOPE>" + resultSet.getString("ENVELOPE") + "</ENVELOPE>");
                } else {
                    sb.append("<ENVELOPE>--</ENVELOPE>");
                }

                sb.append("<CREATEDDATE>" + resultSet.getString("CREATEDDATE") + "</CREATEDDATE>");

                if (resultSet.getString("MODIFIEDDATE") != null && !"".equals(resultSet.getString("MODIFIEDDATE"))) {
                    sb.append("<MODIFIEDDATE>" + resultSet.getString("MODIFIEDDATE") + "</MODIFIEDDATE>");
                } else {
                    sb.append("<MODIFIEDDATE>--</MODIFIEDDATE>");
                }

                sb.append("</DETAIL>");
                isGetting = true;
            }
            if (!isGetting) {
                isGetting = false;
                sb.append("<DETAIL><VALID>false</VALID></DETAIL>");
            }
            sb.append("</DETAILS>");
            sb.append("</xml>");
        } catch (SQLException sqlException) {
            LoggerUtility.log(logger, "SQLException occurred in getRoutingDetails method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
        } catch (ServiceLocatorException serviceLocatorException) {
            LoggerUtility.log(logger, "ServiceLocatorException occurred in getRoutingDetails method:: " + serviceLocatorException.getMessage(), Level.ERROR, serviceLocatorException.getCause());
        } finally {
            try {

                if (resultSet != null) {
                    resultSet.close();
                    resultSet = null;
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                    preparedStatement = null;
                }
                if (connection != null) {
                    connection.close();
                    connection = null;
                }
            } catch (SQLException sqlException) {
                LoggerUtility.log(logger, "finally SQLException occurred in getRoutingDetails method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
            }
        }
        return sb.toString();
    }

    @Override
    public String getB2bChannelDetails(String b2bChannelId) throws ServiceLocatorException {
        boolean isGetting = false;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        StringBuilder sb = new StringBuilder();
        String b2bChannelDetailsQuery = "";
        b2bChannelDetailsQuery = "SELECT * FROM B2BCHANNELSLIST WHERE B2BCHANNELS_ID=" + b2bChannelId;
        try {
            connection = ConnectionProvider.getInstance().getConnection();
            preparedStatement = connection.prepareStatement(b2bChannelDetailsQuery);
            resultSet = preparedStatement.executeQuery();
            sb.append("<xml version=\"1.0\">");
            sb.append("<DETAILS>");
            while (resultSet.next()) {
                sb.append("<DETAIL><VALID>true</VALID>");
                sb.append("<B2BCHANNELS_ID>" + resultSet.getString("B2BCHANNELS_ID") + "</B2BCHANNELS_ID>");
                sb.append("<TP_ID>" + resultSet.getString("TP_ID") + "</TP_ID>");

                if (resultSet.getString("STATUS") != null && !"".equals(resultSet.getString("STATUS"))) {
                    sb.append("<STATUS>" + resultSet.getString("STATUS") + "</STATUS>");
                } else {
                    sb.append("<STATUS>--</STATUS>");
                }
                if (resultSet.getString("DIRECTION") != null && !"".equals(resultSet.getString("DIRECTION"))) {
                    sb.append("<DIRECTION>" + resultSet.getString("DIRECTION") + "</DIRECTION>");
                } else {
                    sb.append("<DIRECTION>--</DIRECTION>");
                }
                if (resultSet.getString("PROTOCOL") != null && !"".equals(resultSet.getString("PROTOCOL"))) {
                    sb.append("<PROTOCOL>" + resultSet.getString("PROTOCOL") + "</PROTOCOL>");
                } else {
                    sb.append("<PROTOCOL>--</PROTOCOL>");
                }
                if (resultSet.getString("HOST") != null && !"".equals(resultSet.getString("HOST"))) {
                    sb.append("<HOST>" + resultSet.getString("HOST") + "</HOST>");
                } else {
                    sb.append("<HOST>--</HOST>");
                }
                if (resultSet.getString("USERNAME") != null && !"".equals(resultSet.getString("USERNAME"))) {
                    sb.append("<USERNAME>" + resultSet.getString("USERNAME") + "</USERNAME>");
                } else {
                    sb.append("<USERNAME>--</USERNAME>");
                }
                if (resultSet.getString("PRODUCERMAILBOX") != null && !"".equals(resultSet.getString("PRODUCERMAILBOX"))) {
                    sb.append("<PRODUCERMAILBOX>" + resultSet.getString("PRODUCERMAILBOX") + "</PRODUCERMAILBOX>");
                } else {
                    sb.append("<PRODUCERMAILBOX>--</PRODUCERMAILBOX>");
                }
                if (resultSet.getString("CONSUMERMAILBOX") != null && !"".equals(resultSet.getString("CONSUMERMAILBOX"))) {
                    sb.append("<CONSUMERMAILBOX>" + resultSet.getString("CONSUMERMAILBOX") + "</CONSUMERMAILBOX>");
                } else {
                    sb.append("<CONSUMERMAILBOX>--</CONSUMERMAILBOX>");
                }
                if (resultSet.getString("POOLINGCODE") != null && !"".equals(resultSet.getString("POOLINGCODE"))) {
                    sb.append("<POOLINGCODE>" + resultSet.getString("POOLINGCODE") + "</POOLINGCODE>");
                } else {
                    sb.append("<POOLINGCODE>--</POOLINGCODE>");
                }

                if (resultSet.getString("APPID") != null && !"".equals(resultSet.getString("APPID"))) {
                    sb.append("<APPID>" + resultSet.getString("APPID") + "</APPID>");
                } else {
                    sb.append("<APPID>--</APPID>");
                }

                if (resultSet.getString("SENDERID") != null && !"".equals(resultSet.getString("SENDERID"))) {
                    sb.append("<SENDERID>" + resultSet.getString("SENDERID") + "</SENDERID>");
                } else {
                    sb.append("<SENDERID>--</SENDERID>");
                }

                if (resultSet.getString("RECEIVERID") != null && !"".equals(resultSet.getString("RECEIVERID"))) {
                    sb.append("<RECEIVERID>" + resultSet.getString("RECEIVERID") + "</RECEIVERID>");
                } else {
                    sb.append("<RECEIVERID>--</RECEIVERID>");
                }

                sb.append("</DETAIL>");
                isGetting = true;
            }
            if (!isGetting) {
                isGetting = false;
                sb.append("<DETAIL><VALID>false</VALID></DETAIL>");
            }
            sb.append("</DETAILS>");
            sb.append("</xml>");
        } catch (SQLException sqlException) {
            LoggerUtility.log(logger, "Exception occurred in getB2bChannelDetails method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
        } catch (ServiceLocatorException serviceLocatorException) {
            LoggerUtility.log(logger, "Exception occurred in getB2bChannelDetails method:: " + serviceLocatorException.getMessage(), Level.ERROR, serviceLocatorException.getCause());
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                    resultSet = null;
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                    preparedStatement = null;
                }
                if (connection != null) {
                    connection.close();
                    connection = null;
                }
            } catch (SQLException sqlException) {
                LoggerUtility.log(logger, "finally SQLException occurred in getB2bChannelDetails method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
            }
        }
        return sb.toString();
    }

    public String getPartnerInfo(String partnerId) throws ServiceLocatorException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String partnerInfoQuery = "";
        partnerInfoQuery = "select tp.ID as TP_ID,tp.NAME as TP_NAME,tp_details.INTERNALIDENTIFIER,tp_details.APPLICATIONID,tp_details.STATE,tp.STATUS,tp.MODIFIED_TS,tp.MODIFIED_BY,tp.CREATED_TS from tp LEFT OUTER JOIN tp_details on(tp_details.TP_ID=tp.ID) WHERE 1=1 AND tp.ID='" + partnerId + "'";
        String response = "None";

        try {
            connection = ConnectionProvider.getInstance().getConnection();
            preparedStatement = connection.prepareStatement(partnerInfoQuery);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                response = resultSet.getString("APPLICATIONID") + "|" + resultSet.getString("INTERNALIDENTIFIER") + "|" + resultSet.getString("TP_ID");
            }
        } catch (SQLException sqlException) {
            LoggerUtility.log(logger, "SQLException occurred in getPartnerInfo method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
        } catch (ServiceLocatorException serviceLocatorException) {
            LoggerUtility.log(logger, "ServiceLocatorException occurred in getPartnerInfo method:: " + serviceLocatorException.getMessage(), Level.ERROR, serviceLocatorException.getCause());
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                    resultSet = null;
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                    preparedStatement = null;
                }
                if (connection != null) {
                    connection.close();
                    connection = null;
                }
            } catch (SQLException sqlException) {
                LoggerUtility.log(logger, "finally SQLException occurred in getPartnerInfo method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
            }
        }
        return response;
    }

    @Override
    public String getRouterInfo(String routerName) throws ServiceLocatorException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String resultString = "";
        String routerInfoQuery = "";
        routerInfoQuery = "select ACCEPTORLOOKUPALIAS,DESTMAILBOX from ROUTERINFO where ROUTER_ID =" + routerName;
        try {
            connection = ConnectionProvider.getInstance().getConnection();
            preparedStatement = connection.prepareStatement(routerInfoQuery);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                resultString = resultSet.getString("ACCEPTORLOOKUPALIAS") + "|" + resultSet.getString("DESTMAILBOX");
            }
        } catch (SQLException sqlException) {
            LoggerUtility.log(logger, "SQLException occurred in getRouterInfo method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
        } catch (ServiceLocatorException serviceLocatorException) {
            LoggerUtility.log(logger, "ServiceLocatorException occurred in getRouterInfo method:: " + serviceLocatorException.getMessage(), Level.ERROR, serviceLocatorException.getCause());
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                    resultSet = null;
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                    preparedStatement = null;
                }
                if (connection != null) {
                    connection.close();
                    connection = null;
                }
            } catch (SQLException sqlException) {
                LoggerUtility.log(logger, "finally SQLException occurred in getRouterInfo method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
            }
        }
        return resultString;
    }

    @Override
    public String getBusinessProcessInfo(String businessProcessId) throws ServiceLocatorException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String resultString = "";
        String businessProcessInfoQuery = "";
        businessProcessInfoQuery = "SELECT INVKOVEMETHOD,MULTIPLEMSG,ADAPTER FROM PROCESSRELATEDINFO WHERE REL_ID =" + businessProcessId;
        try {
            connection = ConnectionProvider.getInstance().getConnection();
            preparedStatement = connection.prepareStatement(businessProcessInfoQuery);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                resultString = resultSet.getString("INVKOVEMETHOD") + "|" + resultSet.getString("MULTIPLEMSG") + "|" + resultSet.getString("ADAPTER");
            }

        } catch (SQLException sqlException) {
            LoggerUtility.log(logger, "SQLException occurred in getBusinessProcessInfo method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
        } catch (ServiceLocatorException serviceLocatorException) {
            LoggerUtility.log(logger, "ServiceLocatorException occurred in getBusinessProcessInfo method:: " + serviceLocatorException.getMessage(), Level.ERROR, serviceLocatorException.getCause());
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                    resultSet = null;
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                    preparedStatement = null;
                }
                if (connection != null) {
                    connection.close();
                    connection = null;
                }
            } catch (SQLException sqlException) {
                LoggerUtility.log(logger, "SQLException occurred in getBusinessProcessInfo method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
            }
        }
        return resultString;
    }

    @Override
    public String getDeliveryChannelDetails(int deliveryChannelId) throws ServiceLocatorException {
        boolean isGetting = false;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        StringBuilder sb = new StringBuilder();
        StringBuilder deliveryChannelDetailsQuery = new StringBuilder();
        try {
            deliveryChannelDetailsQuery.append("select DELIVERYCHNNELINFO.DELIVERYCHN_ID,DELIVERYCHNNELINFO.PARTNER_ID as PartnerId,TP.NAME as PartnerName,DELIVERYCHNNELINFO.ROUTING_ID as routingId,ROUTERINFO.ROUTER_NAME as routingName,bp.REL_ID as bpId,bp.RELNAME as bpName,trans.REL_ID as transId,trans.RELNAME as transName,dem.REL_ID as demId,dem.RELNAME as demName,pmb.REL_ID as pmbId,pmb.RELNAME as pmbName,DELEVERYCHANNELDESCRPTION.VALUE as encodingId,DELEVERYCHANNELDESCRPTION.DESCRIPTION as encodingName,SEQUENCE,ARCHIVEFLAG,ARCHIVEDIRCTORY,OUTPUTFILENAME,OUTPUTFORMAT,DELIVERYCHNNELINFO.STATUS   from (((((((DELIVERYCHNNELINFO  JOIN TP on (TP.ID=DELIVERYCHNNELINFO.PARTNER_ID) ) JOIN ROUTERINFO on (ROUTERINFO.ROUTER_ID=DELIVERYCHNNELINFO.ROUTING_ID)) ");
            deliveryChannelDetailsQuery.append(" JOIN PROCESSRELATEDINFO bp on (bp.REL_ID=DELIVERYCHNNELINFO.BUSINESSPROCESSNAME))");
            deliveryChannelDetailsQuery.append(" JOIN PROCESSRELATEDINFO trans on (trans.REL_ID=DELIVERYCHNNELINFO.TRANSLATIONMAP))");
            deliveryChannelDetailsQuery.append(" JOIN PROCESSRELATEDINFO dem on (dem.REL_ID=DELIVERYCHNNELINFO.DOCEXTRACTMAP))");
            deliveryChannelDetailsQuery.append(" JOIN PROCESSRELATEDINFO pmb on (pmb.REL_ID=DELIVERYCHNNELINFO.PRODUCERMAILBOX))");
            deliveryChannelDetailsQuery.append(" JOIN DELEVERYCHANNELDESCRPTION on (DELEVERYCHANNELDESCRPTION.VALUE=DELIVERYCHNNELINFO.ENCODING)) WHERE DELIVERYCHN_ID=" + deliveryChannelId);
            connection = ConnectionProvider.getInstance().getConnection();
            preparedStatement = connection.prepareStatement(deliveryChannelDetailsQuery.toString());
            resultSet = preparedStatement.executeQuery();
            sb.append("<xml version=\"1.0\">");
            sb.append("<DETAILS>");
            while (resultSet.next()) {
                sb.append("<DETAIL><VALID>true</VALID>");
                sb.append("<PARTNER_ID>" + resultSet.getString("PartnerId") + "</PARTNER_ID>");
                sb.append("<PartnerName>" + resultSet.getString("PartnerName") + "</PartnerName>");

                if (resultSet.getString("routingName") != null && !"".equals(resultSet.getString("routingName"))) {
                    sb.append("<ROUTER_NAME>" + resultSet.getString("routingName") + "</ROUTER_NAME>");
                } else {
                    sb.append("<ROUTER_NAME>--</ROUTER_NAME>");
                }
                if (resultSet.getString("bpName") != null && !"".equals(resultSet.getString("bpName"))) {
                    sb.append("<bpName>" + resultSet.getString("bpName") + "</bpName>");
                } else {
                    sb.append("<bpName>--</bpName>");
                }
                if (resultSet.getString("transName") != null && !"".equals(resultSet.getString("transName"))) {
                    sb.append("<transName>" + resultSet.getString("transName") + "</transName>");
                } else {
                    sb.append("<transName>--</transName>");
                }
                if (resultSet.getString("demName") != null && !"".equals(resultSet.getString("demName"))) {
                    sb.append("<demName>" + resultSet.getString("demName") + "</demName>");
                } else {
                    sb.append("<demName>--</demName>");
                }
                if (resultSet.getString("pmbName") != null && !"".equals(resultSet.getString("pmbName"))) {
                    sb.append("<pmbName>" + resultSet.getString("pmbName") + "</pmbName>");
                } else {
                    sb.append("<pmbName>--</pmbName>");
                }
                if (resultSet.getString("encodingName") != null && !"".equals(resultSet.getString("encodingName"))) {
                    sb.append("<encodingName>" + resultSet.getString("encodingName") + "</encodingName>");
                } else {
                    sb.append("<encodingName>--</encodingName>");
                }
                if (resultSet.getString("SEQUENCE") != null && !"".equals(resultSet.getString("SEQUENCE"))) {
                    sb.append("<SEQUENCE>" + resultSet.getString("SEQUENCE") + "</SEQUENCE>");
                } else {
                    sb.append("<SEQUENCE>--</SEQUENCE>");
                }

                if (resultSet.getString("ARCHIVEFLAG") != null && !"".equals(resultSet.getString("ARCHIVEFLAG"))) {
                    sb.append("<ARCHIVEFLAG>" + resultSet.getString("ARCHIVEFLAG") + "</ARCHIVEFLAG>");
                } else {
                    sb.append("<ARCHIVEFLAG>--</ARCHIVEFLAG>");
                }

                if (resultSet.getString("ARCHIVEDIRCTORY") != null && !"".equals(resultSet.getString("ARCHIVEDIRCTORY"))) {
                    sb.append("<ARCHIVEDIRCTORY>" + resultSet.getString("ARCHIVEDIRCTORY") + "</ARCHIVEDIRCTORY>");
                } else {
                    sb.append("<ARCHIVEDIRCTORY>--</ARCHIVEDIRCTORY>");
                }

                if (resultSet.getString("OUTPUTFILENAME") != null && !"".equals(resultSet.getString("OUTPUTFILENAME"))) {
                    sb.append("<OUTPUTFILENAME>" + resultSet.getString("OUTPUTFILENAME") + "</OUTPUTFILENAME>");
                } else {
                    sb.append("<OUTPUTFILENAME>--</OUTPUTFILENAME>");
                }

                if (resultSet.getString("OUTPUTFORMAT") != null && !"".equals(resultSet.getString("OUTPUTFORMAT"))) {
                    sb.append("<OUTPUTFORMAT>" + resultSet.getString("OUTPUTFORMAT") + "</OUTPUTFORMAT>");
                } else {
                    sb.append("<OUTPUTFORMAT>--</OUTPUTFORMAT>");
                }
                if (resultSet.getString("STATUS") != null && !"".equals(resultSet.getString("STATUS"))) {
                    sb.append("<STATUS>" + resultSet.getString("STATUS") + "</STATUS>");
                } else {
                    sb.append("<STATUS>--</STATUS>");
                }
                sb.append("</DETAIL>");
                isGetting = true;
            }
            if (!isGetting) {
                isGetting = false;
                sb.append("<DETAIL><VALID>false</VALID></DETAIL>");
            }

            sb.append("</DETAILS>");
            sb.append("</xml>");
        } catch (SQLException sqlException) {
            LoggerUtility.log(logger, "SQLException occurred in getDeliveryChannelDetails method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
        } catch (ServiceLocatorException serviceLocatorException) {
            LoggerUtility.log(logger, "ServiceLocatorException occurred in getDeliveryChannelDetails method:: " + serviceLocatorException.getMessage(), Level.ERROR, serviceLocatorException.getCause());
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                    resultSet = null;
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                    preparedStatement = null;
                }
                if (connection != null) {
                    connection.close();
                    connection = null;
                }
            } catch (SQLException sqlException) {
                LoggerUtility.log(logger, "SQLException occurred in getDeliveryChannelDetails method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
            }
        }
        return sb.toString();
    }

   @Override
public String getDashboardDetails(AjaxHandlerAction ajaxHandlerAction) throws ServiceLocatorException {
Connection connection = null;
ResultSet resultSet = null;
String resultString = "";
String inboundString = "";
String outboundString = "";
PreparedStatement preparedStatement = null;
String tmp_Recieved_From = "";
String tmp_Recieved_ToTime = "";
String dashboardDetailsQuery = "";
try {
if (ajaxHandlerAction.getFlag().equals("M")) {
dashboardDetailsQuery = "select count(DIRECTION) as total from FILES where (SENDER_ID = ? or RECEIVER_ID=?) and DIRECTION=? AND FLOWFLAG='M'";
}
if (ajaxHandlerAction.getFlag().equals("L")) {
dashboardDetailsQuery = "select count(DIRECTION) as total from FILES where (SENDER_ID = ? or RECEIVER_ID=?) and DIRECTION=? AND FLOWFLAG='L'";
}
if (!"".equals(ajaxHandlerAction.getStartDate()) && ajaxHandlerAction.getStartDate() != null) {
tmp_Recieved_From = DateUtility.getInstance().DateViewToDBCompare(ajaxHandlerAction.getStartDate());
dashboardDetailsQuery = dashboardDetailsQuery + "AND FILES.DATE_TIME_RECEIVED >= '" + tmp_Recieved_From + "' ";
}
if (!"".equals(ajaxHandlerAction.getEndDate()) && ajaxHandlerAction.getEndDate() != null) {
tmp_Recieved_ToTime = DateUtility.getInstance().DateViewToDBCompare(ajaxHandlerAction.getEndDate());
dashboardDetailsQuery = dashboardDetailsQuery + "AND FILES.DATE_TIME_RECEIVED <= '" + tmp_Recieved_ToTime + "' ";
}

if (!"-1".equals(ajaxHandlerAction.getStatus()) && ajaxHandlerAction.getStatus() != null) {
dashboardDetailsQuery = dashboardDetailsQuery + WildCardSql.getWildCardSql1("FILES.STATUS", ajaxHandlerAction.getStatus().trim());

}
if (!"-1".equals(ajaxHandlerAction.getDocType()) && ajaxHandlerAction.getDocType() != null) {
dashboardDetailsQuery = dashboardDetailsQuery + WildCardSql.getWildCardSql1("FILES.TRANSACTION_TYPE", ajaxHandlerAction.getDocType().trim());

}
connection = ConnectionProvider.getInstance().getConnection();
preparedStatement = connection.prepareStatement(dashboardDetailsQuery);
int inboundTotal = 0;
int outboundTotal = 0;
Map partnerMap = null;
if ("ALL".equalsIgnoreCase(ajaxHandlerAction.getPartnerId())) {
if (ajaxHandlerAction.getFlag().equals("M")) {
partnerMap = DataSourceDataProvider.getInstance().getDashboardPartnerMap("2");
} else {
partnerMap = DataSourceDataProvider.getInstance().getDashboardPartnerMap("3");
}
Iterator entries = partnerMap.entrySet().iterator();

while (entries.hasNext()) {
Entry thisEntry = (Entry) entries.next();
Object key = thisEntry.getKey();
Object value = thisEntry.getValue();
preparedStatement.setString(1, (String) key);
preparedStatement.setString(2, (String) key);
preparedStatement.setString(3, "INBOUND");
resultSet = preparedStatement.executeQuery();
if (resultSet.next()) {
inboundTotal = resultSet.getInt("total");
}
resultSet.close();
preparedStatement.setString(1, (String) key);
preparedStatement.setString(2, (String) key);
preparedStatement.setString(3, "OUTBOUND");
resultSet = preparedStatement.executeQuery();
if (resultSet.next()) {
outboundTotal = resultSet.getInt("total");
}
if (inboundTotal != 0) {
inboundString = inboundString + (String) key + "_" + (String) value + "$" + inboundTotal + "@";
}
if (outboundTotal != 0) {
outboundString = outboundString + (String) key + "_" + (String) value + "$" + outboundTotal + "@";
}
inboundTotal = 0;
outboundTotal = 0;
resultSet.close();
}
} else {
String partnerName = DataSourceDataProvider.getInstance().getPartnerNameById(ajaxHandlerAction.getPartnerId());
preparedStatement.setString(1, ajaxHandlerAction.getPartnerId());
preparedStatement.setString(2, ajaxHandlerAction.getPartnerId());
preparedStatement.setString(3, "INBOUND");
resultSet = preparedStatement.executeQuery();
if (resultSet.next()) {
inboundTotal = resultSet.getInt("total");
}
resultSet.close();
preparedStatement.setString(1, ajaxHandlerAction.getPartnerId());
preparedStatement.setString(2, ajaxHandlerAction.getPartnerId());
preparedStatement.setString(3, "OUTBOUND");
resultSet = preparedStatement.executeQuery();
if (resultSet.next()) {
outboundTotal = resultSet.getInt("total");
}
if (inboundTotal != 0) {
inboundString = inboundString + ajaxHandlerAction.getPartnerId() + "_" + partnerName + "$" + inboundTotal + "@";
}
if (outboundTotal != 0) {
outboundString = outboundString + ajaxHandlerAction.getPartnerId() + "_" + partnerName + "$" + outboundTotal + "@";
}
resultSet.close();
}
resultString = inboundString + "*" + outboundString;
} catch (SQLException sqlException) {
LoggerUtility.log(logger, "SQLException occurred in getDashboardDetails method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
} catch (ServiceLocatorException serviceLocatorException) {
LoggerUtility.log(logger, "ServiceLocatorException occurred in getDashboardDetails method:: " + serviceLocatorException.getMessage(), Level.ERROR, serviceLocatorException.getCause());
} finally {
try {
if (resultSet != null) {
resultSet.close();
resultSet = null;
}
if (preparedStatement != null) {
preparedStatement.close();
preparedStatement = null;
}
if (connection != null) {
connection.close();
connection = null;
}
} catch (SQLException sqlException) {
LoggerUtility.log(logger, "SQLException occurred in getDashboardDetails method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
}
}
       //System.out.println("resultString is "+resultString);
return resultString;
}

    public String getReportDeleteDetails(int id) throws ServiceLocatorException {
        String reportDeleteDetailsQuery = "";
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        StringBuffer sb = new StringBuffer();
        reportDeleteDetailsQuery = "UPDATE SCHEDULER SET SCH_STATUS = 'InActive' WHERE SCH_ID =" + id;
        try {
            connection = ConnectionProvider.getInstance().getConnection();
            statement = connection.createStatement();
            int count = statement.executeUpdate(reportDeleteDetailsQuery);
            if (count > 0) {
                sb.append("Report " + id + " Successfully Deleted!");
            } else {
                sb.append("Sorry ! Please Try again.");
            }
        } catch (SQLException sqlException) {
            LoggerUtility.log(logger, "SQLException occurred in getReportDeleteDetails method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
        } catch (ServiceLocatorException serviceLocatorException) {
            LoggerUtility.log(logger, "ServiceLocatorException occurred in getReportDeleteDetails method:: " + serviceLocatorException.getMessage(), Level.ERROR, serviceLocatorException.getCause());
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
                LoggerUtility.log(logger, "finally SQLException occurred in getReportDeleteDetails method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
            }
        }
        return sb.toString();
    }

    @Override
    public String getReportOverlayDetails(int id, String startDate) throws ServiceLocatorException {
        String reportOverlayDetailsQuery = "";
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        String reportpath = "Nodata";
        startDate = DateUtility.getInstance().DateViewToDBCompare(startDate);
        startDate = startDate.substring(0, 10);
        reportOverlayDetailsQuery = "SELECT SCH_REPORTPATH from SCH_LOOKUPS where SCH_ID=" + id + " and date(SCH_RUNDATE) = DATE('" + startDate + "')";
        try {
            connection = ConnectionProvider.getInstance().getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(reportOverlayDetailsQuery);
            if (resultSet.next()) {
                reportpath = resultSet.getString("SCH_REPORTPATH");
            }
        } catch (SQLException sqlException) {
            LoggerUtility.log(logger, "SQLException occurred in getReportOverlayDetails method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
        } catch (ServiceLocatorException serviceLocatorException) {
            LoggerUtility.log(logger, "ServiceLocatorException occurred in getReportOverlayDetails method:: " + serviceLocatorException.getMessage(), Level.ERROR, serviceLocatorException.getCause());
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
                LoggerUtility.log(logger, "finally SQLException occurred in getReportOverlayDetails method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
            }
        }
        return reportpath;
    }

    @Override
    public String forgotPassword(String userid) throws ServiceLocatorException {
        String str = null;
        String email = null;
        String fname = null;
        String lname = null;
        String name = null;
        String password = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        PasswordUtil passwordUtil = new PasswordUtil();
        String forgotPasswordQuery = "SELECT PASSWD,EMAIL,FNME,LNME FROM M_USER WHERE LOGINID=?";
        try {
            connection = ConnectionProvider.getInstance().getConnection();
            preparedStatement = connection.prepareStatement(forgotPasswordQuery);
            preparedStatement.setString(1, userid);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {

                password = passwordUtil.decryptPwd(resultSet.getString(1));
                email = resultSet.getString(2);
                fname = resultSet.getString(3);
                lname = resultSet.getString(4);

            }
            name = fname + " " + lname;
            MailManager m = new MailManager();
            str = m.sendPwd(email, password, userid, name);
        } catch (Exception exception) {
            LoggerUtility.log(logger, "Exception occurred in forgotPassword method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                    resultSet = null;
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                    preparedStatement = null;
                }
                if (connection != null) {
                    connection.close();
                    connection = null;
                }
            } catch (SQLException sqlException) {
                LoggerUtility.log(logger, "finally SQLException occurred in forgotPassword method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
            }
        }
        return str;
    }

    @Override
    public int updateMyPwd(AjaxHandlerAction ajaxhandleraction, String loginId) throws ServiceLocatorException {
        int updatedRows = 0;
        String password = null;
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        PasswordUtil passwordUtility = new PasswordUtil();
        String updateMyPwdquery = "SELECT LOGINID,PASSWD FROM M_USER WHERE LOGINID='" + loginId + "'";

        try {
            connection = ConnectionProvider.getInstance().getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(updateMyPwdquery);
            while (resultSet.next()) {
                password = resultSet.getString("PASSWD");
            }
            password = passwordUtility.decryptPwd(password);
            if (ajaxhandleraction.getOldPwd().equals(password)) {
                if (ajaxhandleraction.getNewPwd().equals(ajaxhandleraction.getCnfrmPwd())) {
                    String encryptPass = passwordUtility.encryptPwd(ajaxhandleraction.getNewPwd());
                    updateMyPwdquery = "UPDATE M_USER SET PASSWD='" + encryptPass + "' WHERE LOGINID='" + loginId + "'";
                    statement = connection.createStatement();
                    updatedRows = statement.executeUpdate(updateMyPwdquery);
                } else {
                    updatedRows = 100;
                }
            } else {
                updatedRows = 200;
            }
        } catch (SQLException sqlException) {
            LoggerUtility.log(logger, "SQLException occurred in updateMyPwd method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                    statement = null;
                }
                if (connection != null) {
                    connection.close();
                    connection = null;
                }
            } catch (SQLException sqlException) {
                LoggerUtility.log(logger, "finally SQLException occurred in updateMyPwd method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
            }
        }
        return updatedRows;
    }

    @Override
    public int searchItems(String senderItem, String recItem, String selectedName) throws ServiceLocatorException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String searchItemsquery = null;
        String searchItemsQuery = null;
        String ver = null;
        int count = 0;
        connection = ConnectionProvider.getInstance().getOracleConnection();
        try {
            if (selectedName != null && !"-1".equals(selectedName)) {
                searchItemsQuery = "SELECT DEFAULT_VERSION from CODELIST_XREF_VERS where LIST_NAME='" + selectedName + "'";
                preparedStatement = connection.prepareStatement(searchItemsQuery);
                resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    ver = resultSet.getString("DEFAULT_VERSION");
                }
                searchItemsquery = "SELECT COUNT(*) AS COUNT FROM CODELIST_XREF_ITEM WHERE SENDER_ITEM='" + senderItem + "' AND RECEIVER_ITEM='" + recItem + "' AND LIST_NAME='" + selectedName + "'AND LIST_VERSION='" + ver + "'";
                preparedStatement = connection.prepareStatement(searchItemsquery);
                resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    count = resultSet.getInt("COUNT");
                }
            }
        } catch (SQLException sqlException) {
            LoggerUtility.log(logger, "SQLException occurred in searchItems method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                    resultSet = null;
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                    preparedStatement = null;
                }
                if (connection != null) {
                    connection.close();
                    connection = null;
                }
            } catch (SQLException sqlException) {
                LoggerUtility.log(logger, "finally SQLException occurred in searchItems method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
            }
        }
        return count;
    }

    @Override
    public int checkCodeListName(String newCodeListName) throws ServiceLocatorException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String checkCodeListNameQuery = null;
        int count = 0;
        connection = ConnectionProvider.getInstance().getOracleConnection();
        try {
            checkCodeListNameQuery = "SELECT COUNT(*) AS COUNT FROM CODELIST_XREF_VERS  WHERE LIST_NAME='" + newCodeListName + "'";
            preparedStatement = connection.prepareStatement(checkCodeListNameQuery);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                count = resultSet.getInt("COUNT");
            }
        } catch (SQLException sqlException) {
            LoggerUtility.log(logger, "SQLException occurred in checkCodeListName method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                    resultSet = null;
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                    preparedStatement = null;
                }
                if (connection != null) {
                    connection.close();
                    connection = null;
                }
            } catch (SQLException sqlException) {
                LoggerUtility.log(logger, "finally SQLException occurred in checkCodeListName method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
            }
        }
        return count;
    }

    @Override
    public String addCodeList(String jsonData, String userName, String newCodeListName) throws ServiceLocatorException {
        Connection connection = null;
        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;
        PreparedStatement preparedStatement1 = null;
        PreparedStatement preparedStatement2 = null;
        String addCodeListQuery = null;
        connection = ConnectionProvider.getInstance().getOracleConnection();
        JSONArray array = null;
        JSONObject jsonObj = null;
        int updatedRows = 0;
        int updatedRows1 = 0;
        int updatedRows2 = 0;
        int count = 0;
        try {
            array = new JSONArray(jsonData);
        } catch (JSONException jsonException) {
            LoggerUtility.log(logger, "JSONException occurred in addCodeList method:: " + jsonException.getMessage(), Level.ERROR, jsonException.getCause());
        }
        try {
            addCodeListQuery = "SELECT COUNT(*) AS COUNT FROM CODELIST_XREF_VERS  WHERE LIST_NAME='" + newCodeListName + "'";
            preparedStatement = connection.prepareStatement(addCodeListQuery);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                count = resultSet.getInt("COUNT");
            }
            if (count == 0) {
                addCodeListQuery = "INSERT INTO SI_USER.CODELIST_XREF_ITEM "
                        + "(LIST_NAME, SENDER_ID, RECEIVER_ID, LIST_VERSION, SENDER_ITEM, RECEIVER_ITEM, TEXT1, TEXT2, TEXT3, TEXT4, DESCRIPTION, TEXT5, TEXT6, TEXT7, TEXT8, TEXT9)"
                        + " VALUES (?, ?, ?,? , ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                String addCodeListQuery1 = "INSERT INTO SI_USER.CODELIST_XREF_VERS"
                        + "	(LIST_NAME, SENDER_ID, RECEIVER_ID, DEFAULT_VERSION, LIST_VERSION)"
                        + "VALUES (?, ?, ?, ?, ?)";
                String addCodeListQuery2 = "INSERT INTO SI_USER.CODE_LIST_XREF"
                        + "	(LIST_NAME, SENDER_ID, RECEIVER_ID, LIST_VERSION, STATUS, COMMENTS,  USERNAME, CREATE_DATE)"
                        + "VALUES (?, ?, ?, ?,? ,?,?, ?)";
                for (int i = 0; i < array.length(); i++) {
                    jsonObj = array.getJSONObject(i);
                    preparedStatement = connection.prepareStatement(addCodeListQuery);
                    preparedStatement.setString(1, jsonObj.getString("listName1"));
                    preparedStatement.setString(2, "");
                    preparedStatement.setString(3, "");
                    preparedStatement.setInt(4, 1);
                    preparedStatement.setString(5, jsonObj.getString("senderItem"));
                    preparedStatement.setString(6, jsonObj.getString("recItem"));
                    if (!"".equalsIgnoreCase(jsonObj.getString("text1").trim()) && jsonObj.getString("text1") != null) {
                        preparedStatement.setString(7, jsonObj.getString("text1"));
                    } else {
                        preparedStatement.setString(7, "");
                    }
                    if (!"".equalsIgnoreCase(jsonObj.getString("text2").trim()) && jsonObj.getString("text2") != null) {
                        preparedStatement.setString(8, jsonObj.getString("text2"));
                    } else {
                        preparedStatement.setString(8, "");
                    }
                    if (!"".equalsIgnoreCase(jsonObj.getString("text3").trim()) && jsonObj.getString("text3") != null) {
                        preparedStatement.setString(9, jsonObj.getString("text3"));
                    } else {
                        preparedStatement.setString(9, "");
                    }
                    if (!"".equalsIgnoreCase(jsonObj.getString("text4").trim()) && jsonObj.getString("text4") != null) {
                        preparedStatement.setString(10, jsonObj.getString("text4"));
                    } else {
                        preparedStatement.setString(10, "");
                    }
                    preparedStatement.setString(11, jsonObj.getString("desc"));
                    if (!"".equalsIgnoreCase(jsonObj.getString("text5").trim()) && jsonObj.getString("text5") != null) {
                        preparedStatement.setString(12, jsonObj.getString("text5"));
                    } else {
                        preparedStatement.setString(12, "");
                    }
                    if (!"".equalsIgnoreCase(jsonObj.getString("text6").trim()) && jsonObj.getString("text6") != null) {
                        preparedStatement.setString(13, jsonObj.getString("text6"));
                    } else {
                        preparedStatement.setString(13, "");
                    }
                    if (!"".equalsIgnoreCase(jsonObj.getString("text7").trim()) && jsonObj.getString("text7") != null) {
                        preparedStatement.setString(14, jsonObj.getString("text7"));
                    } else {
                        preparedStatement.setString(14, "");
                    }
                    if (!"".equalsIgnoreCase(jsonObj.getString("text8").trim()) && jsonObj.getString("text8") != null) {
                        preparedStatement.setString(15, jsonObj.getString("text8"));
                    } else {
                        preparedStatement.setString(15, "");
                    }
                    if (!"".equalsIgnoreCase(jsonObj.getString("text9").trim()) && jsonObj.getString("text9") != null) {
                        preparedStatement.setString(16, jsonObj.getString("text9"));
                    } else {
                        preparedStatement.setString(16, "");
                    }
                    preparedStatement1 = connection.prepareStatement(addCodeListQuery1);
                    preparedStatement1.setString(1, jsonObj.getString("listName1"));
                    preparedStatement1.setString(2, "");
                    preparedStatement1.setString(3, "");
                    preparedStatement1.setInt(4, 1);
                    preparedStatement1.setInt(5, 1);
                    preparedStatement2 = connection.prepareStatement(addCodeListQuery2);
                    preparedStatement2.setString(1, jsonObj.getString("listName1"));
                    preparedStatement2.setString(2, "");
                    preparedStatement2.setString(3, "");
                    preparedStatement2.setInt(4, 1);
                    preparedStatement2.setInt(5, 1);
                    preparedStatement2.setString(6, "");
                    preparedStatement2.setString(7, userName);
                    preparedStatement2.setTimestamp(8, DateUtility.getInstance().getCurrentDB2Timestamp());
                    updatedRows = preparedStatement.executeUpdate();

                    if (i == 0) {
                        updatedRows1 = preparedStatement1.executeUpdate();
                        updatedRows2 = preparedStatement2.executeUpdate();
                    }
                }
                if (updatedRows > 0 && updatedRows1 > 0 && updatedRows2 > 0) {
                    doCacheRefresh();
                    return "Inserted successfully";
                } else {
                    return "Please Try Again";
                }
            } else {
                return "Code list name already exists ..Please try with new one ";
            }
        } catch (SQLException sqlException) {
            LoggerUtility.log(logger, "SQLException occurred in addCodeList method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
        } catch (JSONException jsonException) {
            LoggerUtility.log(logger, "JSONException occurred in addCodeList method:: " + jsonException.getMessage(), Level.ERROR, jsonException.getCause());
        } finally {
            try {

                if (preparedStatement != null) {
                    preparedStatement.close();
                    preparedStatement = null;
                }
                if (connection != null) {
                    connection.close();
                    connection = null;
                }
            } catch (SQLException sqlException) {
                LoggerUtility.log(logger, "finally SQLException occurred in addCodeList method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
            }
        }
        return null;
    }

    @Override
    public String deleteCodeList(String jsonData) throws ServiceLocatorException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        String deleteCodeListQuery = null;
        String deleteCodeListQuery1 = null;
        String deleteCodeListQuery2 = null;
        connection = ConnectionProvider.getInstance().getOracleConnection();
        JSONArray array = null;
        JSONObject jsonObj = null;
        int updatedRows = 0;
        try {
            array = new JSONArray(jsonData);
        } catch (JSONException jsonException) {
            LoggerUtility.log(logger, "JSONException occurred in deleteCodeList method:: " + jsonException.getMessage(), Level.ERROR, jsonException.getCause());
        }
        try {
            deleteCodeListQuery = "DELETE FROM SI_USER.CODELIST_XREF_ITEM WHERE LIST_NAME=? AND LIST_VERSION=? AND SENDER_ITEM=? AND RECEIVER_ITEM=?";
            deleteCodeListQuery1 = "DELETE FROM SI_USER.CODELIST_XREF_VERS WHERE LIST_NAME=? AND LIST_VERSION=?";
            deleteCodeListQuery2 = "DELETE FROM SI_USER.CODE_LIST_XREF WHERE LIST_NAME=? AND LIST_VERSION=?";
            for (int i = 0; i < array.length(); i++) {
                jsonObj = array.getJSONObject(i);
                preparedStatement = connection.prepareStatement(deleteCodeListQuery);
                preparedStatement.setString(1, jsonObj.getString("listName1"));
                preparedStatement.setInt(2, Integer.parseInt(jsonObj.getString("listVerson")));
                preparedStatement.setString(3, jsonObj.getString("senderItem"));
                preparedStatement.setString(4, jsonObj.getString("recItem"));
                updatedRows = preparedStatement.executeUpdate();
            }
        } catch (SQLException sqlException) {
            LoggerUtility.log(logger, "Exception occurred in deleteCodeList method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
        } catch (JSONException jsonException) {
            LoggerUtility.log(logger, "JSONException occurred in deleteCodeList method:: " + jsonException.getMessage(), Level.ERROR, jsonException.getCause());
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                    preparedStatement = null;
                }
                if (connection != null) {
                    connection.close();
                    connection = null;
                }
            } catch (SQLException sqlException) {
                LoggerUtility.log(logger, "finally Exception occurred in deleteCodeList method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
            }
        }
        if (updatedRows > 0) {
            return "Deleted successfully";
        } else {
            return "Please Try Again";
        }
    }

    @Override
    public String updateCodeList(String listName, String jsonData, String userName, int listitems) throws ServiceLocatorException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        PreparedStatement preparedStatement1 = null;
        PreparedStatement preparedStatement2 = null;
        String updateCodeList = null;
        String updateCodeList1 = null;
        String updateCodeList2 = null;
        String updateQueryString = null;
        int updatedRows = 0;
        int updatedRows1 = 0;
        int updatedRows2 = 0;
        JSONArray array = null;
        JSONObject jsonObj = null;
        connection = ConnectionProvider.getInstance().getOracleConnection();

        try {
            array = new JSONArray(jsonData);
        } catch (JSONException jsonException) {
            LoggerUtility.log(logger, "JSONException occurred in updateCodeList method:: " + jsonException.getMessage(), Level.ERROR, jsonException.getCause());
        }
        try {
            int addVersion = 0;
            for (int i = 0; i < array.length(); i++) {
                int listNameMap = 0;
                jsonObj = array.getJSONObject(i);
                updateCodeList = "SELECT  LIST_VERSION FROM CODELIST_XREF_VERS WHERE LIST_NAME ='" + jsonObj.getString("listName1") + "'";
                preparedStatement = connection.prepareStatement(updateCodeList);
                resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    listNameMap = resultSet.getInt("LIST_VERSION");
                }
                if (listNameMap > 0) {
                    if (i == 0) {
                        addVersion = listNameMap + 1;
                        updateQueryString = "UPDATE CODELIST_XREF_VERS SET LIST_VERSION=?, DEFAULT_VERSION=?, SENDER_ID=?, RECEIVER_ID=?  WHERE LIST_NAME=?";
                        preparedStatement = connection.prepareStatement(updateQueryString);
                        preparedStatement.setInt(1, addVersion);
                        preparedStatement.setInt(2, addVersion);
                        preparedStatement.setString(3, "");
                        preparedStatement.setString(4, "");
                        preparedStatement.setString(5, jsonObj.getString("listName1"));
                        updatedRows = preparedStatement.executeUpdate();
                        preparedStatement.close();
                    }
                    updateCodeList1 = "INSERT INTO SI_USER.CODELIST_XREF_ITEM "
                            + "(LIST_NAME, SENDER_ID, RECEIVER_ID, LIST_VERSION, SENDER_ITEM, RECEIVER_ITEM, TEXT1, TEXT2, TEXT3, TEXT4, DESCRIPTION, TEXT5, TEXT6, TEXT7, TEXT8, TEXT9)"
                            + " VALUES (?, ?, ?,? , ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                    updateCodeList2 = "INSERT INTO SI_USER.CODE_LIST_XREF"
                            + "(LIST_NAME, SENDER_ID, RECEIVER_ID, LIST_VERSION, STATUS, COMMENTS,  USERNAME, CREATE_DATE)"
                            + "VALUES (?, ?, ?, ?,? ,?,?, ?)";
                    jsonObj = array.getJSONObject(i);
                    preparedStatement1 = connection.prepareStatement(updateCodeList1);
                    preparedStatement1.setString(1, jsonObj.getString("listName1"));
                    preparedStatement1.setString(2, "");
                    preparedStatement1.setString(3, "");
                    preparedStatement1.setInt(4, addVersion);
                    preparedStatement1.setString(5, jsonObj.getString("senderItem"));
                    preparedStatement1.setString(6, jsonObj.getString("recItem"));
                    if (!"".equalsIgnoreCase(jsonObj.getString("text1").trim()) && jsonObj.getString("text1") != null) {
                        preparedStatement1.setString(7, jsonObj.getString("text1"));
                    } else {
                        preparedStatement1.setString(7, "");
                    }
                    if (!"".equalsIgnoreCase(jsonObj.getString("text2").trim()) && jsonObj.getString("text2") != null) {
                        preparedStatement1.setString(8, jsonObj.getString("text2"));
                    } else {
                        preparedStatement1.setString(8, "");
                    }
                    if (!"".equalsIgnoreCase(jsonObj.getString("text3").trim()) && jsonObj.getString("text3") != null) {
                        preparedStatement1.setString(9, jsonObj.getString("text3"));
                    } else {
                        preparedStatement1.setString(9, "");
                    }
                    if (!"".equalsIgnoreCase(jsonObj.getString("text4").trim()) && jsonObj.getString("text4") != null) {
                        preparedStatement1.setString(10, jsonObj.getString("text4"));
                    } else {
                        preparedStatement1.setString(10, "");
                    }
                    preparedStatement1.setString(11, jsonObj.getString("desc"));
                    if (!"".equalsIgnoreCase(jsonObj.getString("text5").trim()) && jsonObj.getString("text5") != null) {
                        preparedStatement1.setString(12, jsonObj.getString("text5"));
                    } else {
                        preparedStatement1.setString(12, "");
                    }
                    if (!"".equalsIgnoreCase(jsonObj.getString("text6").trim()) && jsonObj.getString("text6") != null) {
                        preparedStatement1.setString(13, jsonObj.getString("text6"));
                    } else {
                        preparedStatement1.setString(13, "");
                    }
                    if (!"".equalsIgnoreCase(jsonObj.getString("text7").trim()) && jsonObj.getString("text7") != null) {
                        preparedStatement1.setString(14, jsonObj.getString("text7"));
                    } else {
                        preparedStatement1.setString(14, "");
                    }
                    if (!"".equalsIgnoreCase(jsonObj.getString("text8").trim()) && jsonObj.getString("text8") != null) {
                        preparedStatement1.setString(15, jsonObj.getString("text8"));
                    } else {
                        preparedStatement1.setString(15, "");
                    }
                    if (!"".equalsIgnoreCase(jsonObj.getString("text9").trim()) && jsonObj.getString("text9") != null) {
                        preparedStatement1.setString(16, jsonObj.getString("text9"));
                    } else {
                        preparedStatement1.setString(16, "");
                    }
                    updatedRows1 = preparedStatement1.executeUpdate();
                    preparedStatement1.close();
                    if (i == 0) {
                        preparedStatement2 = connection.prepareStatement(updateCodeList2);
                        preparedStatement2.setString(1, jsonObj.getString("listName1"));
                        preparedStatement2.setString(2, "");
                        preparedStatement2.setString(3, "");
                        preparedStatement2.setInt(4, addVersion);
                        preparedStatement2.setInt(5, 1);
                        preparedStatement2.setString(6, "");
                        preparedStatement2.setString(7, userName);
                        preparedStatement2.setTimestamp(8, DateUtility.getInstance().getCurrentDB2Timestamp());
                        updatedRows2 = preparedStatement2.executeUpdate();
                        preparedStatement2.close();
                    }
                }
            }
        } catch (SQLException sqlException) {
            LoggerUtility.log(logger, "SQLException occurred in updateCodeList method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
        } catch (JSONException jsonException) {
            LoggerUtility.log(logger, "JSONException occurred in updateCodeList method:: " + jsonException.getMessage(), Level.ERROR, jsonException.getCause());
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                    resultSet = null;
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                    preparedStatement = null;
                }
                if (connection != null) {
                    connection.close();
                    connection = null;
                }
            } catch (SQLException sqlException) {
                LoggerUtility.log(logger, "finally SQLException occurred in updateCodeList method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
            }
        }
        if (updatedRows > 0 && updatedRows1 > 0 && updatedRows2 > 0) {
            doCacheRefresh();
            return "Updated successfully";
        } else {
            return "Please Try Again";
        }

    }

    public void doCacheRefresh() {
        String response = "";
        String https_url = "http://192.168.1.179:8765/Cache";
        URL url;
        try {
            url = new URL(https_url);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setDoOutput(true);
            con.connect();
            response = con.getResponseMessage();
        } catch (Exception exception) {
            LoggerUtility.log(logger, "Exception occurred in doCacheRefresh method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
        }
    }

    @Override
    public String getInventoryDetails(String instanceid, int id, String database) throws ServiceLocatorException {
        System.out.println("getPaymentDetailsInfo are "+instanceid);
        System.out.println("getPaymentDetailsInfo are "+id);
        System.out.println("getPaymentDetailsInfo are "+database);
        boolean isGetting = false;
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        StringBuilder sb = new StringBuilder();
        String inventoryDetailsquery = "";
        if ("ARCHIVE".equals(database)) {
            inventoryDetailsquery = "select DISTINCT(ARCHIVE_INVENTORY.FILE_ID) as FILE_ID,ARCHIVE_INVENTORY.ID,ARCHIVE_FILES.ISA_NUMBER,ARCHIVE_FILES.GS_CONTROL_NUMBER,ARCHIVE_FILES.SENDER_ID,ARCHIVE_FILES.RECEIVER_ID,ARCHIVE_INVENTORY.REFERENCE_NUMBER,ARCHIVE_INVENTORY.REPORTING_DATE,"
                    + "ARCHIVE_FILES.DIRECTION,ARCHIVE_FILES.STATUS,ARCHIVE_FILES.SENDER_ID,ARCHIVE_FILES.RECEIVER_ID,ARCHIVE_INVENTORY.VENDOR_NAME,ARCHIVE_INVENTORY.VENDOR_LOCATION,ARCHIVE_INVENTORY.ITEMS_COUNT,TP1.NAME as SENDER_NAME,ARCHIVE_FILES.ISA_DATE as ISA_DATE,ARCHIVE_FILES.ISA_TIME as ISA_TIME,"
                    + "ARCHIVE_FILES.TRANSACTION_TYPE as TRANSACTION_TYPE,ARCHIVE_FILES.FILE_TYPE,ARCHIVE_FILES.PRI_KEY_TYPE,"
                    + "ARCHIVE_FILES.PRE_TRANS_FILEPATH,ARCHIVE_FILES.POST_TRANS_FILEPATH,ARCHIVE_FILES.ACK_FILE_ID as ACK_FILE_ID,ARCHIVE_FILES.ORG_FILEPATH as ORG_FILEPATH,ARCHIVE_FILES.ERR_MESSAGE from ARCHIVE_INVENTORY JOIN "
                    + "ARCHIVE_FILES ON (ARCHIVE_FILES.FILE_ID=ARCHIVE_INVENTORY.FILE_ID) "
                    + "where FLOWFLAG like 'M' AND ARCHIVE_INVENTORY.FILE_ID LIKE '%" + instanceid + "%' AND ARCHIVE_INVENTORY.ID =" + id;
        } else {
            inventoryDetailsquery = "select DISTINCT(INVENTORY.FILE_ID) as FILE_ID,FILES.SENDER_ID,FILES.RECEIVER_ID,INVENTORY.ID,FILES.ISA_NUMBER,FILES.GS_CONTROL_NUMBER,FILES.SENDER_ID,FILES.RECEIVER_ID,INVENTORY.REFERENCE_NUMBER,INVENTORY.REPORTING_DATE,"
                    + "FILES.DIRECTION,FILES.STATUS,INVENTORY.VENDOR_NAME,INVENTORY.VENDOR_LOCATION,INVENTORY.ITEMS_COUNT,FILES.ISA_DATE as ISA_DATE,FILES.ISA_TIME as ISA_TIME,"
                    + "FILES.TRANSACTION_TYPE as TRANSACTION_TYPE,FILES.FILE_TYPE,FILES.PRI_KEY_TYPE,"
                    + "FILES.PRE_TRANS_FILEPATH,FILES.POST_TRANS_FILEPATH,FILES.ACK_FILE_ID as ACK_FILE_ID,FILES.ORG_FILEPATH as ORG_FILEPATH,FILES.ERR_MESSAGE from INVENTORY JOIN "
                    + "FILES ON (FILES.FILE_ID=INVENTORY.FILE_ID) "
                    + "where FLOWFLAG like 'M' AND INVENTORY.FILE_ID LIKE '%" + instanceid + "%' AND INVENTORY.ID =" + id;
        }
        try {
            Map<String, String> tradingPartners = DataSourceDataProvider.getInstance().getAllTradingPartners();
            connection = ConnectionProvider.getInstance().getConnection();
            statement = connection.prepareStatement(inventoryDetailsquery);
            resultSet = statement.executeQuery();
            sb.append("<xml version=\"1.0\">");
            sb.append("<DETAILS>");
            while (resultSet.next()) {
                sb.append("<DETAIL><VALID>true</VALID>");
                if (resultSet.getString("FILE_ID") != null && !"".equals(resultSet.getString("FILE_ID"))) {
                    sb.append("<FILEID>" + resultSet.getString("FILE_ID") + "</FILEID>");
                } else {
                    sb.append("<FILEID>--</FILEID>");
                }
                if (resultSet.getString("FILE_TYPE") != null && !"".equals(resultSet.getString("FILE_TYPE"))) {
                    sb.append("<FILETYPE>" + resultSet.getString("FILE_TYPE") + "</FILETYPE>");
                } else {
                    sb.append("<FILETYPE>--</FILETYPE>");
                }
                if (resultSet.getString("SENDER_ID") != null && !"".equals(resultSet.getString("SENDER_ID"))) {
                    sb.append("<SENDERID>" + resultSet.getString("SENDER_ID") + "</SENDERID>");
                } else {
                    sb.append("<SENDERID>--</SENDERID>");
                }
                if (resultSet.getString("RECEIVER_ID") != null && !"".equals(resultSet.getString("RECEIVER_ID"))) {
                    sb.append("<RECEIVERID>" + resultSet.getString("RECEIVER_ID") + "</RECEIVERID>");
                } else {
                    sb.append("<RECEIVERID>--</RECEIVERID>");
                }
                if (resultSet.getString("SENDER_ID") != null && (((tradingPartners.get(resultSet.getString("SENDER_ID")))) != null)) {
                    sb.append("<SENDER_NAME>" + (tradingPartners.get(resultSet.getString("SENDER_ID"))).toString() + "</SENDER_NAME>");
                } else {
                    sb.append("<SENDER_NAME>--</SENDER_NAME>");
                }
                if (resultSet.getString("RECEIVER_ID") != null && (((tradingPartners.get(resultSet.getString("RECEIVER_ID")))) != null)) {
                    sb.append("<RECEIVER_NAME>" + (tradingPartners.get(resultSet.getString("RECEIVER_ID"))).toString() + "</RECEIVER_NAME>");
                } else {
                    sb.append("<RECEIVER_NAME>--</RECEIVER_NAME>");
                }
                if (resultSet.getString("DIRECTION") != null && !"".equals(resultSet.getString("DIRECTION"))) {
                    sb.append("<DIRECTION>" + resultSet.getString("DIRECTION").toLowerCase() + "</DIRECTION>");
                } else {
                    sb.append("<DIRECTION>--</DIRECTION>");
                }
                if (resultSet.getString("ISA_NUMBER") != null && !"".equals(resultSet.getString("ISA_NUMBER"))) {
                    sb.append("<ISA_NUMBER>" + resultSet.getString("ISA_NUMBER") + "</ISA_NUMBER>");
                } else {
                    sb.append("<ISA_NUMBER>--</ISA_NUMBER>");
                }
                if (resultSet.getString("ISA_DATE") != null && !"".equals(resultSet.getString("ISA_DATE"))) {
                    sb.append("<ISA_DATE>" + resultSet.getString("ISA_DATE") + "</ISA_DATE>");
                } else {
                    sb.append("<ISA_DATE>--</ISA_DATE>");
                }
                if (resultSet.getString("ISA_TIME") != null && !"".equals(resultSet.getString("ISA_TIME"))) {
                    sb.append("<ISA_TIME>" + resultSet.getString("ISA_TIME") + "</ISA_TIME>");
                } else {
                    sb.append("<ISA_TIME>--</ISA_TIME>");
                }
                if (resultSet.getString("PRI_KEY_TYPE") != null && !"".equals(resultSet.getString("PRI_KEY_TYPE"))) {
                    sb.append("<PRI_KEY_TYPE>" + resultSet.getString("PRI_KEY_TYPE") + "</PRI_KEY_TYPE>");
                } else {
                    sb.append("<PRI_KEY_TYPE>--</PRI_KEY_TYPE>");
                }
                if (resultSet.getString("GS_CONTROL_NUMBER") != null && !"".equalsIgnoreCase(resultSet.getString("GS_CONTROL_NUMBER"))) {
                    sb.append("<GS_CONTROL_NUMBER>" + resultSet.getString("GS_CONTROL_NUMBER") + "</GS_CONTROL_NUMBER>");
                } else {
                    sb.append("<GS_CONTROL_NUMBER>--</GS_CONTROL_NUMBER>");
                }
                if (resultSet.getString("TRANSACTION_TYPE") != null && !"".equals(resultSet.getString("TRANSACTION_TYPE"))) {
                    sb.append("<TRANSACTION_TYPE>" + resultSet.getString("TRANSACTION_TYPE") + "</TRANSACTION_TYPE>");
                } else {
                    sb.append("<TRANSACTION_TYPE>--</TRANSACTION_TYPE>");
                }
                if (resultSet.getString("REFERENCE_NUMBER") != null && !"".equals(resultSet.getString("REFERENCE_NUMBER"))) {
                    sb.append("<REFERENCE_NUMBER>" + resultSet.getString("REFERENCE_NUMBER") + "</REFERENCE_NUMBER>");
                } else {
                    sb.append("<REFERENCE_NUMBER>--</REFERENCE_NUMBER>");
                }
                if (resultSet.getString("STATUS") != null && !"".equals(resultSet.getString("STATUS"))) {
                    sb.append("<STATUS>" + resultSet.getString("STATUS") + "</STATUS>");
                } else {
                    sb.append("<STATUS>--</STATUS>");
                }
                if (resultSet.getString("ITEMS_COUNT") != null && !"".equals(resultSet.getString("ITEMS_COUNT"))) {
                    sb.append("<ITEMS_COUNT>" + resultSet.getString("ITEMS_COUNT") + "</ITEMS_COUNT>");
                } else {
                    sb.append("<ITEMS_COUNT>--</ITEMS_COUNT>");
                }

                if (resultSet.getString("PRE_TRANS_FILEPATH") != null) {
                    if (new File(resultSet.getString("PRE_TRANS_FILEPATH")).exists() && new File(resultSet.getString("PRE_TRANS_FILEPATH")).isFile()) {
                        sb.append("<PRETRANSFILEPATH>" + resultSet.getString("PRE_TRANS_FILEPATH") + "</PRETRANSFILEPATH>");
                    } else {
                        sb.append("<PRETRANSFILEPATH>No File</PRETRANSFILEPATH>");
                    }
                } else {
                    sb.append("<PRETRANSFILEPATH>No File</PRETRANSFILEPATH>");
                }

                if (resultSet.getString("POST_TRANS_FILEPATH") != null) {
                    if (new File(resultSet.getString("POST_TRANS_FILEPATH")).exists() && new File(resultSet.getString("POST_TRANS_FILEPATH")).isFile()) {
                        sb.append("<POSTTRANSFILEPATH>" + resultSet.getString("POST_TRANS_FILEPATH") + "</POSTTRANSFILEPATH>");
                    } else {
                        sb.append("<POSTTRANSFILEPATH>No File</POSTTRANSFILEPATH>");
                    }
                } else {
                    sb.append("<POSTTRANSFILEPATH>No File</POSTTRANSFILEPATH>");
                }
                if (resultSet.getString("ORG_FILEPATH") != null) {
                    if (new File(resultSet.getString("ORG_FILEPATH")).exists() && new File(resultSet.getString("ORG_FILEPATH")).isFile()) {
                        sb.append("<ORG_FILEPATH>" + resultSet.getString("ORG_FILEPATH") + "</ORG_FILEPATH>");
                    } else {
                        sb.append("<ORG_FILEPATH>No File</ORG_FILEPATH>");
                    }
                } else {
                    sb.append("<ORG_FILEPATH>No File</ORG_FILEPATH>");
                }
                if (resultSet.getString("ACK_FILE_ID") != null) {
                    if (new File(resultSet.getString("ACK_FILE_ID")).exists() && new File(resultSet.getString("ACK_FILE_ID")).isFile()) {
                        sb.append("<ACKFILEID>" + resultSet.getString("ACK_FILE_ID") + "</ACKFILEID>");
                    } else {
                        sb.append("<ACKFILEID>No File</ACKFILEID>");
                    }
                } else {
                    sb.append("<ACKFILEID>No File</ACKFILEID>");
                }
                if (resultSet.getString("ERR_MESSAGE") != null && !"".equals(resultSet.getString("ERR_MESSAGE"))) {
                    sb.append("<ERR_MESSAGE>" + resultSet.getString("ERR_MESSAGE") + "</ERR_MESSAGE>");
                } else {
                    sb.append("<ERR_MESSAGE>NO MSG</ERR_MESSAGE>");
                }

                sb.append("</DETAIL>");
                isGetting = true;
            }
            if (!isGetting) {
                isGetting = false;
                sb.append("<DETAIL><VALID>false</VALID></DETAIL>");
            }

            sb.append("</DETAILS>");
            sb.append("</xml>");
        } catch (SQLException sqlException) {
            LoggerUtility.log(logger, "SQLException occurred in getInventoryDetails method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
        } catch (ServiceLocatorException serviceLocatorException) {
            LoggerUtility.log(logger, "ServiceLocatorException occurred in getInventoryDetails method:: " + serviceLocatorException.getMessage(), Level.ERROR, serviceLocatorException.getCause());
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
                LoggerUtility.log(logger, "finally SQLException occurred in getInventoryDetails method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
            }
        }
        return sb.toString();
    }

    @Override
    public String getMapNamesList(String mapName) throws ServiceLocatorException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        JSONObject mainJson = null;
        boolean isGetting = false;
        String result = "";
        String mapNamesListQuery = "";
        mapNamesListQuery = "SELECT DISTINCT M.MAP_NAME FROM MAP M INNER JOIN MAP_VERSIONS MV ON MV.MAP_NAME = M.MAP_NAME AND MV.DEFAULT_VERSION = M.MAP_VERSION WHERE M.STATUS = 1 AND lower(M.MAP_NAME) LIKE ? ANd ROWNUM<250  ORDER BY M.MAP_NAME";
        try {
            connection = ConnectionProvider.getInstance().getOracleConnection();
            preparedStatement = connection.prepareStatement(mapNamesListQuery);
            preparedStatement.setString(1, "%" + mapName.toLowerCase() + "%");
            resultSet = preparedStatement.executeQuery();

            mainJson = new JSONObject();
            JSONArray ja = new JSONArray();
            JSONObject subJson = null;
            while (resultSet.next()) {
                subJson = new JSONObject();
                subJson.put("name", resultSet.getString("MAP_NAME"));
                ja.put(subJson);
                isGetting = true;
            }
            mainJson.put("mapList", ja);
            mainJson.put("isGetting", isGetting);
            result = mainJson.toString();
        } catch (SQLException sqlException) {
            LoggerUtility.log(logger, "SQLException occurred in getMapNamesList method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
        } catch (JSONException jsonException) {
            LoggerUtility.log(logger, "JSONException occurred in getMapNamesList method:: " + jsonException.getMessage(), Level.ERROR, jsonException.getCause());
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                    resultSet = null;
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                    preparedStatement = null;
                }
                if (connection != null) {
                    connection.close();
                    connection = null;
                }
            } catch (SQLException sqlException) {
                LoggerUtility.log(logger, "finally SQLException occurred in getMapNamesList method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
            }
        }

        return result;
    }

    @Override
    public String doGetProcessFlow(String jsonData) throws ServiceLocatorException {
        JSONObject jObject = null;
        JSONObject jsonResult = new JSONObject();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String doGetProcessFlowQuery = "";
        try {
            jObject = new JSONObject(jsonData);
            String processId = jObject.getString("processId");

            doGetProcessFlowQuery = "select FLOW_DATA from PROCESS where PROCESS_ID=?";
            connection = ConnectionProvider.getInstance().getConnection();
            preparedStatement = connection.prepareStatement(doGetProcessFlowQuery);
            preparedStatement.setString(1, processId);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                jsonResult.put("flowData", resultSet.getString("FLOW_DATA"));
            } else {
                jsonResult.put("flowData", "");
            }

        } catch (Exception exception) {
            LoggerUtility.log(logger, "Exception occurred in doGetProcessFlow method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
        } finally {
            try {

                if (resultSet != null) {
                    resultSet.close();
                    resultSet = null;
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                    preparedStatement = null;
                }
                if (connection != null) {
                    connection.close();
                    connection = null;
                }
            } catch (SQLException sqlException) {
                LoggerUtility.log(logger, "finally SQLException occurred in doGetProcessFlow method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
            }
        }
        return jsonResult.toString();
    }

    @Override
    public String doSaveProcessFlow(String jsonData, String userName) throws ServiceLocatorException {
        Connection connection = null;
        JSONObject jObject = null;
        JSONObject jsonResult = new JSONObject();;
        JSONObject operators = null;
        JSONObject links = null;
        try {
            jObject = new JSONObject(jsonData);
            operators = jObject.getJSONObject("operators");
            links = jObject.getJSONObject("links");
            ArrayList process = new ArrayList();
            ArrayList maps = new ArrayList();
            ArrayList targets = new ArrayList();
            ArrayList fromOperatorConnectors = new ArrayList();
            ArrayList toOperatorsConnectors = new ArrayList();
            Iterator itr = operators.keys();
            while (itr.hasNext()) {
                String element = (String) itr.next();
                if (element.contains("process")) {
                    process.add(element);
                } else if (element.contains("map")) {
                    maps.add(element);
                } else if (element.contains("target")) {
                    targets.add(element);
                }
            }
            itr = links.keys();
            while (itr.hasNext()) {
                String element = (String) itr.next();

                String fromOperator = links.getJSONObject(element).getString("fromOperator");
                String fromConnector = links.getJSONObject(element).getString("fromConnector");
                fromOperatorConnectors.add(fromOperator + "#" + fromConnector);

                String toOperator = links.getJSONObject(element).getString("toOperator");
                String toConnector = links.getJSONObject(element).getString("toConnector");
                toOperatorsConnectors.add(toOperator + "#" + toConnector);

            }
            connection = ConnectionProvider.getInstance().getConnection();
            connection.setAutoCommit(false);
            int processId = 0;
            int myprocessId = operators.getJSONObject((String) process.get(0)).getJSONObject("properties").getInt("processId");
            if (myprocessId != 0) {
                processId = myprocessId;
                updateProcessNode(connection, operators.getJSONObject((String) process.get(0)).getJSONObject("properties"), userName);
            } else {
                processId = insertProcessNode(connection, operators.getJSONObject((String) process.get(0)).getJSONObject("properties"), userName);
            }
            operators.getJSONObject((String) process.get(0)).getJSONObject("properties").put("processId", processId);
            JSONObject outputs = operators.getJSONObject((String) process.get(0)).getJSONObject("properties").getJSONObject("outputs");
            ArrayList orderMaps = new ArrayList();
            int index = 0;
            for (int i = 1; i <= outputs.length(); i++) {
                index = fromOperatorConnectors.indexOf(process.get(0) + "#output_" + i);
                orderMaps.add(toOperatorsConnectors.get(index));

                boolean flag = true;
                while (flag) {
                    String nextFrom = ((String) toOperatorsConnectors.get(index)).split(Pattern.quote("#"))[0] + "#output_1";
                    index = fromOperatorConnectors.indexOf(nextFrom);
                    String mappp = (String) toOperatorsConnectors.get(index);

                    if (mappp.contains("target")) {
                        flag = false;
                    } else {
                        orderMaps.add(toOperatorsConnectors.get(index));
                    }
                }
            }

            for (int i = 0; i < maps.size(); i++) {
                if (!orderMaps.contains(maps.get(i) + "#input_1")) {
                    orderMaps.add(maps.get(i) + "#input_1");
                }
            }

            for (int i = 0; i < orderMaps.size(); i++) {
                String mapNode = ((String) orderMaps.get(i)).split(Pattern.quote("#"))[0];
                int myMapId = operators.getJSONObject(mapNode).getJSONObject("properties").getInt("mapId");
                if (myMapId != 0) {
                    updateMapNode(connection, operators.getJSONObject(mapNode).getJSONObject("properties"), processId, userName);
                } else {
                    int mapId = insertMapNode(connection, operators.getJSONObject(mapNode).getJSONObject("properties"), processId, userName);
                    operators.getJSONObject(mapNode).getJSONObject("properties").put("mapId", mapId);

                }

            }
            for (int i = 0; i < orderMaps.size(); i++) {
                index = fromOperatorConnectors.indexOf(((String) orderMaps.get(i)).split(Pattern.quote("#"))[0] + "#output_1");
                boolean flag = true;
                String mapp = (String) toOperatorsConnectors.get(index);
                if (mapp.contains("target")) {
                    int myTargetId = operators.getJSONObject(mapp.split(Pattern.quote("#"))[0]).getJSONObject("properties").getInt("targetId");
                    if (myTargetId != 0) {
                        updateTargetNode(connection, operators.getJSONObject(mapp.split(Pattern.quote("#"))[0]).getJSONObject("properties"), operators.getJSONObject(((String) orderMaps.get(i)).split(Pattern.quote("#"))[0]).getJSONObject("properties"), processId, userName);
                    } else {
                        int targetId = insertTargetNode(connection, operators.getJSONObject(mapp.split(Pattern.quote("#"))[0]).getJSONObject("properties"), operators.getJSONObject(((String) orderMaps.get(i)).split(Pattern.quote("#"))[0]).getJSONObject("properties"), processId, userName);
                        operators.getJSONObject(mapp.split(Pattern.quote("#"))[0]).getJSONObject("properties").put("targetId", targetId);
                    }
                }
            }
            updateFlowData(connection, processId, jObject.toString());
            connection.commit();
            jsonResult.put("result", "success");
            jsonResult.put("flowData", jObject);
            jsonResult.put("processId", processId);

        } catch (Exception exception) {
            LoggerUtility.log(logger, "Exception occurred in doSaveProcessFlow method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
            try {
                jsonResult.put("result", "fail");
                connection.rollback();
            } catch (SQLException sqlException) {
                LoggerUtility.log(logger, "SQLException occurred in doSaveProcessFlow method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
            } catch (JSONException jsonException) {
                LoggerUtility.log(logger, "JSONException occurred in doSaveProcessFlow method:: " + jsonException.getMessage(), Level.ERROR, jsonException.getCause());
            }
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                    connection = null;
                }
            } catch (SQLException sqlException) {
                LoggerUtility.log(logger, "finally SQLException occurred in doSaveProcessFlow method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
            }
        }
        return jsonResult.toString();
    }

    public int updateFlowData(Connection connection, int processId, String flowData) throws Exception {
        int count = 0;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            String updateFlowDataQuery = "UPDATE PROCESS SET  FLOW_DATA = ? WHERE PROCESS_ID=?  ";
            preparedStatement = connection.prepareStatement(updateFlowDataQuery);
            preparedStatement.setString(1, flowData);
            preparedStatement.setInt(2, processId);
            count = preparedStatement.executeUpdate();

        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                    resultSet = null;
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                    preparedStatement = null;
                }

            } catch (SQLException sqlException) {
                LoggerUtility.log(logger, "finally SQLException occurred in updateFlowData method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
            }
        }
        return count;
    }

    public int insertTargetNode(Connection connection, JSONObject jTarget, JSONObject jMap, int processId, String userName) throws Exception {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        int targetId = 0;
        String insertTargetNodeQuery = "";
        try {
            if (jMap.getString("mapType").equals("Translation")) {
                insertTargetNodeQuery = "INSERT INTO PSTEP_DEST(PSTEP_ID, TARGET_MAILBOX, PRE_PROCESS_ID, POST_PROCESS_ID, TRANSLATION_MAP, IS_ACTIVE, CREATED_BY, CREATED_DATE,MULTI_MAP) ";
            } else {
                insertTargetNodeQuery = "INSERT INTO PSTEP_DEST(PSTEP_ID, TARGET_MAILBOX, PRE_PROCESS_ID, POST_PROCESS_ID, EXTRACT_MAP, IS_ACTIVE, CREATED_BY, CREATED_DATE,MULTI_MAP) ";
            }

            insertTargetNodeQuery = insertTargetNodeQuery + " VALUES(?,?,?,?,?,?,?,?,?)";
            preparedStatement = connection.prepareStatement(insertTargetNodeQuery, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, jMap.getInt("mapId"));
            preparedStatement.setString(2, jTarget.getString("targetMailBox"));
            preparedStatement.setInt(3, processId);
            preparedStatement.setInt(4, processId);
            preparedStatement.setString(5, jMap.getString("map"));
            preparedStatement.setString(6, jTarget.getString("status"));
            preparedStatement.setString(7, userName);
            preparedStatement.setTimestamp(8, DateUtility.getInstance().getCurrentDB2Timestamp());
            preparedStatement.setString(9, jMap.getString("multiple"));
            preparedStatement.executeUpdate();
            resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                targetId = Integer.parseInt(String.valueOf(resultSet.getInt(1)));
            }
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                    resultSet = null;
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                    preparedStatement = null;
                }
            } catch (SQLException sqlException) {
                LoggerUtility.log(logger, "finally SQLException occurred in insertTargetNode method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
            }
        }
        return targetId;
    }

    public int updateTargetNode(Connection connection, JSONObject jTarget, JSONObject jMap, int processId, String userName) throws Exception {
        PreparedStatement preparedStatement = null;
        int targetId = 0;
        String updateTargetNodeQuery = "";
        try {
            if (jMap.getString("mapType").equals("Translation")) {
                updateTargetNodeQuery = "UPDATE  PSTEP_DEST SET PSTEP_ID=?, TARGET_MAILBOX=?, PRE_PROCESS_ID=?, POST_PROCESS_ID=?, TRANSLATION_MAP=?, IS_ACTIVE=?, LAST_UPDATED_BY=?, LAST_UPDATED_DT=?,MULTI_MAP=? WHERE DEST_ID=? ";
            } else {
                updateTargetNodeQuery = "UPDATE  PSTEP_DEST SET PSTEP_ID=?, TARGET_MAILBOX=?, PRE_PROCESS_ID=?, POST_PROCESS_ID=?, EXTRACT_MAP=?, IS_ACTIVE=?, LAST_UPDATED_BY=?, LAST_UPDATED_DT=?,MULTI_MAP=? WHERE DEST_ID=? ";

            }
            preparedStatement = connection.prepareStatement(updateTargetNodeQuery);
            preparedStatement.setInt(1, jMap.getInt("mapId"));
            preparedStatement.setString(2, jTarget.getString("targetMailBox"));
            preparedStatement.setInt(3, processId);
            preparedStatement.setInt(4, processId);
            preparedStatement.setString(5, jMap.getString("map"));
            preparedStatement.setString(6, jTarget.getString("status"));
            preparedStatement.setString(7, userName);
            preparedStatement.setTimestamp(8, DateUtility.getInstance().getCurrentDB2Timestamp());
            preparedStatement.setString(9, jMap.getString("multiple"));
            preparedStatement.setInt(10, jTarget.getInt("targetId"));
            preparedStatement.executeUpdate();

        } finally {
            try {

                if (preparedStatement != null) {
                    preparedStatement.close();
                    preparedStatement = null;
                }

            } catch (SQLException sqlException) {
                LoggerUtility.log(logger, "finally SQLException occurred in updateTargetNode method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
            }
        }
        return targetId;
    }

    public int insertMapNode(Connection connection, JSONObject jObject, int processId, String userName) throws Exception {
        int mapId = 0;
        String insertMapNodeQuery = "";
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            if (jObject.getString("mapType").equals("Translation")) {
                insertMapNodeQuery = "INSERT INTO PROCESS_STEPS(SEQ_NUMBER, TRANSLATION_MAP, PRE_PROCESS_ID, POST_PROCESS_ID, IS_ACTIVE, CREATED_BY, CREATED_DATE,MULTI_MAP) ";
            } else {
                insertMapNodeQuery = "INSERT INTO PROCESS_STEPS(SEQ_NUMBER, EXTRACT_MAP, PRE_PROCESS_ID, POST_PROCESS_ID, IS_ACTIVE, CREATED_BY, CREATED_DATE,MULTI_MAP) ";
            }
            insertMapNodeQuery = insertMapNodeQuery + " VALUES(?,?,?,?,?,?,?,?)";

            preparedStatement = connection.prepareStatement(insertMapNodeQuery, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, jObject.getInt("sequence"));
            preparedStatement.setString(2, jObject.getString("map"));
            preparedStatement.setInt(3, processId);
            preparedStatement.setInt(4, processId);
            preparedStatement.setString(5, jObject.getString("status"));
            preparedStatement.setString(6, userName);

            preparedStatement.setTimestamp(7, DateUtility.getInstance().getCurrentDB2Timestamp());
            preparedStatement.setString(8, jObject.getString("multiple"));
            preparedStatement.executeUpdate();
            resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                mapId = Integer.parseInt(String.valueOf(resultSet.getInt(1)));
            }
        } finally {
            try {

                if (resultSet != null) {
                    resultSet.close();
                    resultSet = null;
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                    preparedStatement = null;
                }

            } catch (SQLException sqlException) {
                LoggerUtility.log(logger, "finally SQLException occurred in insertMapNode method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
            }
        }
        return mapId;
    }

    public int updateMapNode(Connection connection, JSONObject jObject, int processId, String userName) throws Exception {
        int mapId = 0;
        PreparedStatement preparedStatement = null;
        String updateMapNodeQuery = "";
        try {
            if (jObject.getString("mapType").equals("Translation")) {
                updateMapNodeQuery = "UPDATE  PROCESS_STEPS SET SEQ_NUMBER=?, TRANSLATION_MAP=?, PRE_PROCESS_ID=?, POST_PROCESS_ID=?, IS_ACTIVE=?, LAST_UPDATED_BY=?, LAST_UPDATED_DT=?,MULTI_MAP=? WHERE PSTEP_ID=? ";
            } else {
                updateMapNodeQuery = "UPDATE  PROCESS_STEPS SET SEQ_NUMBER=?, EXTRACT_MAP=?, PRE_PROCESS_ID=?, POST_PROCESS_ID=?, IS_ACTIVE=?, LAST_UPDATED_BY=?, LAST_UPDATED_DT=?,MULTI_MAP=? WHERE PSTEP_ID=?  ";
            }
            preparedStatement = connection.prepareStatement(updateMapNodeQuery);
            preparedStatement.setInt(1, jObject.getInt("sequence"));
            preparedStatement.setString(2, jObject.getString("map"));
            preparedStatement.setInt(3, processId);
            preparedStatement.setInt(4, processId);
            preparedStatement.setString(5, jObject.getString("status"));
            preparedStatement.setString(6, userName);
            preparedStatement.setTimestamp(7, DateUtility.getInstance().getCurrentDB2Timestamp());
            preparedStatement.setString(8, jObject.getString("multiple"));
            preparedStatement.setInt(9, jObject.getInt("mapId"));
            preparedStatement.executeUpdate();
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                    preparedStatement = null;
                }
            } catch (SQLException sqlException) {
                LoggerUtility.log(logger, "finally SQLException occurred in updateMapNode method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
            }
        }
        return mapId;
    }

    public int insertProcessNode(Connection connection, JSONObject jObject, String userName) throws Exception {
        int processId = 0;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            String insertProcessNodeQuery = "INSERT INTO PROCESS(DIRECTION,PROCESS_NAME,DESCRIPTION,TP_NAME,TP_ID,TP_SENDER_ID,TP_RECEIVER_ID,TRANSACTION_TYPE,SOURCE_MAIL_BOX,IS_ACTIVE,CREATED_BY,CREATED_DATE,LOOKUP_ALIAS) ";
            insertProcessNodeQuery = insertProcessNodeQuery + " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)";
            preparedStatement = connection.prepareStatement(insertProcessNodeQuery, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, jObject.getString("direction"));
            preparedStatement.setString(2, jObject.getString("title"));
            preparedStatement.setString(3, jObject.getString("description"));
            preparedStatement.setString(4, jObject.getString("tpName"));
            preparedStatement.setString(5, jObject.getString("tpId"));
            preparedStatement.setString(6, jObject.getString("tpSenderId"));
            preparedStatement.setString(7, jObject.getString("tpReceiverId"));
            preparedStatement.setString(8, jObject.getString("transactionType"));
            preparedStatement.setString(9, jObject.getString("sourceMailBox"));
            preparedStatement.setString(10, jObject.getString("status"));
            preparedStatement.setString(11, userName);
            preparedStatement.setTimestamp(12, DateUtility.getInstance().getCurrentDB2Timestamp());
            preparedStatement.setString(13, jObject.getString("lookupAlias"));
            preparedStatement.executeUpdate();
            resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                processId = Integer.parseInt(String.valueOf(resultSet.getInt(1)));
            }
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                    resultSet = null;
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                    preparedStatement = null;
                }

            } catch (SQLException sqlException) {
                LoggerUtility.log(logger, "SQLException occurred in insertProcessNode method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
            }
        }
        return processId;
    }

    public int updateProcessNode(Connection connection, JSONObject jObject, String userName) throws Exception {
        int processId = 0;
        PreparedStatement preparedStatement = null;
        try {
            String updateProcessNodeQuery = "UPDATE  PROCESS SET DIRECTION=?,PROCESS_NAME=?,DESCRIPTION=?,TP_NAME=?,TP_ID=?,TP_SENDER_ID=?,TP_RECEIVER_ID=?,TRANSACTION_TYPE=?,SOURCE_MAIL_BOX=?,IS_ACTIVE=?,LAST_UPDATED_BY=?,LAST_UPDATED_DT=?,LOOKUP_ALIAS=? WHERE PROCESS_ID=? ";

            preparedStatement = connection.prepareStatement(updateProcessNodeQuery);
            preparedStatement.setString(1, jObject.getString("direction"));
            preparedStatement.setString(2, jObject.getString("title"));
            preparedStatement.setString(3, jObject.getString("description"));
            preparedStatement.setString(4, jObject.getString("tpName"));
            preparedStatement.setString(5, jObject.getString("tpId"));
            preparedStatement.setString(6, jObject.getString("tpSenderId"));
            preparedStatement.setString(7, jObject.getString("tpReceiverId"));
            preparedStatement.setString(8, jObject.getString("transactionType"));
            preparedStatement.setString(9, jObject.getString("sourceMailBox"));
            preparedStatement.setString(10, jObject.getString("status"));
            preparedStatement.setString(11, userName);
            preparedStatement.setTimestamp(12, DateUtility.getInstance().getCurrentDB2Timestamp());
            preparedStatement.setString(13, jObject.getString("lookupAlias"));
            preparedStatement.setInt(14, jObject.getInt("processId"));
            preparedStatement.executeUpdate();

        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                    preparedStatement = null;
                }

            } catch (SQLException sqlException) {
                LoggerUtility.log(logger, "finally SQLException occurred in updateProcessNode method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
            }
        }
        return processId;
    }

    @Override
    public String getMailBoxList(String name) throws ServiceLocatorException {
        String mailBoxListQuery = "";
        JSONObject mainJson = null;
        boolean isGetting = false;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String result = "";
        mailBoxListQuery = "select path_up from MBX_MAILBOX where lower(path_up)  LIKE ? ANd ROWNUM<250  ORDER BY path_up";
        try {
            connection = ConnectionProvider.getInstance().getOracleConnection();
            preparedStatement = connection.prepareStatement(mailBoxListQuery);
            preparedStatement.setString(1, "%" + name.toLowerCase() + "%");
            resultSet = preparedStatement.executeQuery();

            mainJson = new JSONObject();
            JSONArray ja = new JSONArray();
            JSONObject subJson = null;

            while (resultSet.next()) {
                subJson = new JSONObject();
                subJson.put("name", resultSet.getString("path_up"));
                ja.put(subJson);
                isGetting = true;
            }

            mainJson.put("mailBoxList", ja);
            mainJson.put("isGetting", isGetting);
            result = mainJson.toString();
        } catch (SQLException sqlException) {
            LoggerUtility.log(logger, "SQLException occurred in getMailBoxList method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
        } catch (JSONException jsonException) {
            LoggerUtility.log(logger, "JSONException occurred in getMailBoxList method:: " + jsonException.getMessage(), Level.ERROR, jsonException.getCause());
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                    resultSet = null;
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                    preparedStatement = null;
                }
                if (connection != null) {
                    connection.close();
                    connection = null;
                }
            } catch (SQLException sqlException) {
                LoggerUtility.log(logger, "finally SQLException occurred in getMailBoxList method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
            }
        }
        return result;
    }

    @Override
    public String doGetTpList(String name) throws ServiceLocatorException {

        JSONObject mainJson = null;
        boolean isGetting = false;
        String result = "";
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String doGetTpListQuery = "";
        doGetTpListQuery = "select ID,NAME from TP where STATUS='ACTIVE' AND (lower(ID) like ? OR lower(NAME) like ? )";
        try {
            connection = ConnectionProvider.getInstance().getConnection();
            preparedStatement = connection.prepareStatement(doGetTpListQuery);
            preparedStatement.setString(1, "%" + name.toLowerCase() + "%");
            preparedStatement.setString(2, "%" + name.toLowerCase() + "%");
            resultSet = preparedStatement.executeQuery();

            mainJson = new JSONObject();
            JSONArray ja = new JSONArray();
            JSONObject subJson = null;

            while (resultSet.next()) {
                subJson = new JSONObject();
                subJson.put("id", resultSet.getString("ID"));
                subJson.put("name", resultSet.getString("NAME"));
                ja.put(subJson);
                isGetting = true;
            }
            mainJson.put("tpList", ja);
            mainJson.put("isGetting", isGetting);
            result = mainJson.toString();
        } catch (SQLException sqlException) {
            LoggerUtility.log(logger, "SQLException occurred in doGetTpList method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
        } catch (JSONException jsonException) {
            LoggerUtility.log(logger, "JSONException occurred in doGetTpList method:: " + jsonException.getMessage(), Level.ERROR, jsonException.getCause());
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                    resultSet = null;
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                    preparedStatement = null;
                }
                if (connection != null) {
                    connection.close();
                    connection = null;
                }
            } catch (SQLException sqlException) {
                LoggerUtility.log(logger, "SQLException occurred in doGetTpList method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
            }
        }
        return result;

    }

    @Override
    public String getTransactionsBasedOnType(String flowFlag) throws ServiceLocatorException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        StringBuilder transactions = new StringBuilder();
        String transactionsBasedOnTypeQuery = "";
        try {
            connection = ConnectionProvider.getInstance().getConnection();
            if ("Logistics".equalsIgnoreCase(flowFlag)) {
                transactionsBasedOnTypeQuery = "SELECT DISTINCT(TRANSACTION_TYPE) AS TRANSACTION_TYPE FROM FILES where FLOWFLAG = 'L' AND TRANSACTION_TYPE is not null AND TRANSACTION_TYPE != '' order by TRANSACTION_TYPE";

            } else if ("Manufacturing".equalsIgnoreCase(flowFlag)) {
                transactionsBasedOnTypeQuery = "SELECT DISTINCT(TRANSACTION_TYPE) AS TRANSACTION_TYPE FROM FILES where FLOWFLAG = 'M' AND TRANSACTION_TYPE is not null AND TRANSACTION_TYPE != '' order by TRANSACTION_TYPE";

            } else {
                transactionsBasedOnTypeQuery = "SELECT DISTINCT(TRANSACTION_TYPE) AS TRANSACTION_TYPE FROM FILES where TRANSACTION_TYPE is not null AND TRANSACTION_TYPE != '' order by TRANSACTION_TYPE";
            }
            preparedStatement = connection.prepareStatement(transactionsBasedOnTypeQuery);
            resultSet = preparedStatement.executeQuery();
            transactions.append("<xml version=\"1.0\">");
            transactions.append("<TRANSACTIONS>");
            while (resultSet.next()) {
                String transcation = resultSet.getString("TRANSACTION_TYPE");
                transactions.append("<TRANSACTION_TYPE transaction=\"" + transcation + "\">");
                transactions.append(transcation);
                transactions.append("</TRANSACTION_TYPE>");
            }
            transactions.append("</TRANSACTIONS>");
            transactions.append("</xml>");
        } catch (SQLException sqlException) {
            LoggerUtility.log(logger, "SQLException occurred in getTransactionsBasedOnType method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
        } catch (ServiceLocatorException serviceLocatorException) {
            LoggerUtility.log(logger, "ServiceLocatorException occurred in getTransactionsBasedOnType method:: " + serviceLocatorException.getMessage(), Level.ERROR, serviceLocatorException.getCause());
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                    resultSet = null;
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                    preparedStatement = null;
                }
                if (connection != null) {
                    connection.close();
                    connection = null;
                }
            } catch (SQLException sqlException) {
                LoggerUtility.log(logger, "SQLException occurred in getTransactionsBasedOnType method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
            }
        }
        return transactions.toString();
    }

    @Override
    public String getPartnerAndDocType(String flowFlag) throws ServiceLocatorException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        PreparedStatement preparedStatement1 = null;
        ResultSet resultSet = null;
        ResultSet resultSet1 = null;
        String partnerquery = null;
        StringBuilder transactions = new StringBuilder();
        String partnerAndDocTypeQuery = "";
        try {
            connection = ConnectionProvider.getInstance().getConnection();
            String partnerFlag = null;
            if ("Logistics".equalsIgnoreCase(flowFlag)) {
                partnerFlag = "3";
                partnerAndDocTypeQuery = "SELECT DISTINCT(TRANSACTION_TYPE) AS TRANSACTION_TYPE FROM FILES where FLOWFLAG = 'L' AND TRANSACTION_TYPE is not null order by TRANSACTION_TYPE";
                partnerquery = "SELECT ID,NAME From TP WHERE FLOW_FLAG='" + partnerFlag + "'";
            } else if ("Manufacturing".equalsIgnoreCase(flowFlag)) {
                partnerFlag = "2";
                partnerAndDocTypeQuery = "SELECT DISTINCT(TRANSACTION_TYPE) AS TRANSACTION_TYPE FROM FILES where FLOWFLAG = 'M' AND TRANSACTION_TYPE is not null order by TRANSACTION_TYPE";
                partnerquery = "SELECT ID,NAME From TP WHERE FLOW_FLAG='" + partnerFlag + "'";
            } else {
                partnerAndDocTypeQuery = "SELECT DISTINCT(TRANSACTION_TYPE) AS TRANSACTION_TYPE FROM FILES where TRANSACTION_TYPE is not null order by TRANSACTION_TYPE";
            }
            preparedStatement = connection.prepareStatement(partnerAndDocTypeQuery);
            preparedStatement1 = connection.prepareStatement(partnerquery);
            resultSet = preparedStatement.executeQuery();
            resultSet1 = preparedStatement1.executeQuery();
            transactions.append("<xml version=\"1.0\">");
            transactions.append("<TRANSACTIONS>");
            while (resultSet.next()) {
                String transcation = resultSet.getString("TRANSACTION_TYPE");
                transactions.append("<TRANSACTION_TYPE transaction=\"" + transcation + "\">");
                transactions.append(transcation);
                transactions.append("</TRANSACTION_TYPE>");
            }
            transactions.append("</TRANSACTIONS>");
            transactions.append("<PARTNER>");
            while (resultSet1.next()) {
                String partner = resultSet1.getString("NAME");
                String pid = resultSet1.getString("ID");
                transactions.append("<NAME  id=\"" + pid + "\">");
                transactions.append(partner);
                transactions.append("</NAME>");
            }
            transactions.append("</PARTNER>");
            transactions.append("</xml>");
        } catch (SQLException sqlException) {
            LoggerUtility.log(logger, "SQLException occurred in getPartnerAndDocType method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
        } catch (ServiceLocatorException serviceLocatorException) {
            LoggerUtility.log(logger, "ServiceLocatorException occurred in getPartnerAndDocType method:: " + serviceLocatorException.getMessage(), Level.ERROR, serviceLocatorException.getCause());
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                    resultSet1.close();
                    resultSet = null;
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                    preparedStatement1.close();
                    preparedStatement = null;
                }
                if (connection != null) {
                    connection.close();
                    connection = null;
                }
            } catch (SQLException sqlException) {
                LoggerUtility.log(logger, "SQLException occurred in getPartnerAndDocType method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
            }
        }
        return transactions.toString();
    }

    @Override
    public String getTransactionReferences(String fileId, String docType, String senderId, String receiverId) throws ServiceLocatorException {
        boolean isGetting = false;
        Connection connection = null;
        PreparedStatement statement = null;
        PreparedStatement statement1 = null;
        ResultSet resultSet = null;
        ResultSet resultSet1 = null;
        StringBuilder sb = new StringBuilder();
        String transactionReferencesQuery1 = "";
        String transactionReferencesQuery = "";
        if (("856".equalsIgnoreCase(docType)) || ("DESADV".equalsIgnoreCase(docType))) {
            transactionReferencesQuery = "SELECT FILES.ACK_STATUS,ASN.BOL_NUMBER,ASN.PO_NUMBER,FILES.PRE_TRANS_FILEPATH,FILES.POST_TRANS_FILEPATH,ASN.ASN_NUMBER,FILES.IDOC_NUM,"
                    + "FILES.DATE_TIME_RECEIVED FROM FILES LEFT OUTER JOIN ASN ON (FILES.FILE_ID = ASN.FILE_ID) WHERE FILES.FILE_ID = '" + fileId + "'";
        } else if (("850".equalsIgnoreCase(docType)) || ("855".equalsIgnoreCase(docType)) || ("997".equalsIgnoreCase(docType)) || ("ORDRSP".equalsIgnoreCase(docType))) {
            transactionReferencesQuery = "SELECT FILES.ACK_STATUS,PO.PO_NUMBER,FILES.PRE_TRANS_FILEPATH,FILES.POST_TRANS_FILEPATH,FILES.IDOC_NUM,"
                    + "FILES.DATE_TIME_RECEIVED FROM FILES LEFT OUTER JOIN PO ON (FILES.FILE_ID = PO.FILE_ID) WHERE FILES.FILE_ID = '" + fileId + "'";
        } else if (("810".equalsIgnoreCase(docType)) || ("INVOIC".equalsIgnoreCase(docType))) {
            transactionReferencesQuery = "SELECT FILES.ACK_STATUS,INVOICE.INVOICE_NUMBER,INVOICE.PO_NUMBER,FILES.PRE_TRANS_FILEPATH,FILES.POST_TRANS_FILEPATH,FILES.IDOC_NUM,"
                    + "FILES.DATE_TIME_RECEIVED FROM FILES LEFT OUTER JOIN INVOICE ON (FILES.FILE_ID = INVOICE.FILE_ID) WHERE FILES.FILE_ID = '" + fileId + "'";
        }
        transactionReferencesQuery1 = "SELECT TP_DETAILS.STATE FROM TP_DETAILS WHERE (TP_DETAILS.TP_ID='" + senderId + "' OR TP_DETAILS.TP_ID='" + receiverId + "')";
        try {
            connection = ConnectionProvider.getInstance().getConnection();
            statement = connection.prepareStatement(transactionReferencesQuery);
            resultSet = statement.executeQuery();

            if (senderId != null || receiverId != null) {
                statement1 = connection.prepareStatement(transactionReferencesQuery1);
                resultSet1 = statement1.executeQuery();
            }
            sb.append("<xml version=\"1.0\">");
            sb.append("<DETAILS>");
            while (resultSet.next()) {
                sb.append("<DETAIL><VALID>true</VALID>");
                sb.append("<DOC_TYPE>" + docType + "</DOC_TYPE>");
                if (resultSet.getString("ACK_STATUS") != null && !"".equals(resultSet.getString("ACK_STATUS"))) {
                    sb.append("<ACK_STATUS>" + resultSet.getString("ACK_STATUS") + "</ACK_STATUS>");
                } else {
                    sb.append("<ACK_STATUS>--</ACK_STATUS>");
                }
                if (resultSet.getString("PO_NUMBER") != null && !"".equals(resultSet.getString("PO_NUMBER"))) {
                    sb.append("<PO_NUMBER>" + resultSet.getString("PO_NUMBER") + "</PO_NUMBER>");
                } else {
                    sb.append("<PO_NUMBER>--</PO_NUMBER>");
                }
                if (resultSet.getString("IDOC_NUM") != null && !"".equals(resultSet.getString("IDOC_NUM"))) {
                    sb.append("<IDOC_NUM>" + resultSet.getString("IDOC_NUM") + "</IDOC_NUM>");
                } else {
                    sb.append("<IDOC_NUM>--</IDOC_NUM>");
                }
                if (resultSet.getString("DATE_TIME_RECEIVED") != null && !"".equals(resultSet.getString("DATE_TIME_RECEIVED"))) {
                    sb.append("<DATE_TIME_RECEIVED>" + resultSet.getString("DATE_TIME_RECEIVED") + "</DATE_TIME_RECEIVED>");
                } else {
                    sb.append("<DATE_TIME_RECEIVED>--</DATE_TIME_RECEIVED>");
                }

                if (resultSet.getString("PRE_TRANS_FILEPATH") != null) {
                    sb.append("<PRE_TRANS_FILEPATH>" + resultSet.getString("PRE_TRANS_FILEPATH") + "</PRE_TRANS_FILEPATH>");
                } else {
                    sb.append("<PRE_TRANS_FILEPATH>No File</PRE_TRANS_FILEPATH>");
                }

                if (resultSet.getString("POST_TRANS_FILEPATH") != null) {
                    sb.append("<POST_TRANS_FILEPATH>" + resultSet.getString("POST_TRANS_FILEPATH") + "</POST_TRANS_FILEPATH>");
                } else {
                    sb.append("<POST_TRANS_FILEPATH>No File</POST_TRANS_FILEPATH>");
                }
                if ("856".equalsIgnoreCase(docType)) {
                    if (resultSet.getString("BOL_NUMBER") != null && !"".equals(resultSet.getString("BOL_NUMBER"))) {
                        sb.append("<BOL_NUMBER>" + resultSet.getString("BOL_NUMBER") + "</BOL_NUMBER>");
                    } else {
                        sb.append("<BOL_NUMBER>--</BOL_NUMBER>");
                    }
                    if (resultSet.getString("ASN_NUMBER") != null && !"".equals(resultSet.getString("ASN_NUMBER"))) {
                        sb.append("<ASN_NUMBER>" + resultSet.getString("ASN_NUMBER") + "</ASN_NUMBER>");
                    } else {
                        sb.append("<ASN_NUMBER>--</ASN_NUMBER>");
                    }
                }
                if ("810".equalsIgnoreCase(docType)) {
                    if (resultSet.getString("INVOICE_NUMBER") != null && !"".equals(resultSet.getString("INVOICE_NUMBER"))) {
                        sb.append("<INVOICE_NUMBER>" + resultSet.getString("INVOICE_NUMBER") + "</INVOICE_NUMBER>");
                    } else {
                        sb.append("<INVOICE_NUMBER>--</INVOICE_NUMBER>");
                    }
                }
                if (resultSet1 != null) {
                    resultSet1.next();
                    sb.append("<STATE>" + resultSet1.getString("STATE") + "</STATE>");
                }

                sb.append("</DETAIL>");
                isGetting = true;
            }
            if (!isGetting) {
                isGetting = false;
                sb.append("<DETAIL><VALID>false</VALID></DETAIL>");
            }
            sb.append("</DETAILS>");
            sb.append("</xml>");
        } catch (SQLException sqlException) {
            LoggerUtility.log(logger, "SQLException occurred in getTransactionReferences method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
        } catch (ServiceLocatorException serviceLocatorException) {
            LoggerUtility.log(logger, "ServiceLocatorException occurred in getTransactionReferences method:: " + serviceLocatorException.getMessage(), Level.ERROR, serviceLocatorException.getCause());
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                    resultSet = null;
                }
                if (resultSet1 != null) {
                    resultSet1.close();
                    resultSet1 = null;
                }
                if (statement != null) {
                    statement.close();
                    statement = null;
                }
                if (statement1 != null) {
                    statement1.close();
                    statement1 = null;
                }
                if (connection != null) {
                    connection.close();
                    connection = null;
                }
            } catch (SQLException sqlException) {
                LoggerUtility.log(logger, "finally SQLException occurred in getTransactionReferences method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
            }
        }
        return sb.toString();
    }

    @Override
    public String getPrePosttranslationPathData(String preposttranslationpath) throws Exception {
        String file = preposttranslationpath;
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String line = null;
            StringBuilder stringBuilder = new StringBuilder();
            String ls = System.getProperty("line.separator");

            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
                stringBuilder.append(ls);
            }
            return stringBuilder.toString();
        } catch (Exception exception) {
            LoggerUtility.log(logger, "Exception occurred in getPrePosttranslationPathData method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
        } finally {
            reader.close();
        }
        return "";
    }

    public String appConfigEditTimeinterval(String username, String timeInterval) {
        System.out.println("entered in appConfigEditTimeinterval method");
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String message = null;
        try {
            com.mss.ediscv.util.Properties.setProperty("TimeInterval", timeInterval);
            int count = 0;
            Timestamp currentDateTime = DateUtility.getInstance().getCurrentDB2Timestamp();
            connection = ConnectionProvider.getInstance().getConnection();
            String appConfigEditTimeintervalQuery = "select count(*) as timeIntervalExisted from scvp_config where CATEGORY='TIME_INTERVAL'";
            preparedStatement = connection.prepareStatement(appConfigEditTimeintervalQuery);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                count = count + (resultSet.getInt("timeIntervalExisted"));
            }
            if (resultSet != null) {
                resultSet.close();
                resultSet = null;
            }
            if (preparedStatement != null) {
                preparedStatement.close();
                preparedStatement = null;
            }
            if (count > 0) {
                String timeIntervalUpdateQuery = "UPDATE SCVP_CONFIG SET PROPERTYVALUE = ?,MODIFIEDDATE = ?, MODIFIEDBY = ? WHERE PROPERTYNAME = 'TIME_IN_MINUTES' AND CATEGORY = 'TIME_INTERVAL'";
                preparedStatement = connection.prepareStatement(timeIntervalUpdateQuery);
                preparedStatement.setString(1, timeInterval);
                preparedStatement.setTimestamp(2, currentDateTime);
                preparedStatement.setString(3, username);
                int updatedRows = preparedStatement.executeUpdate();
                if (updatedRows > 0) {
                    message = "<font color='green'>Time interval updated successfully.</font>";
                } else {
                    message = "<font color='red'>Please try again!!</font>";
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                    preparedStatement = null;
                }
            } else {
                String timeIntervalInsertQuery = "INSERT INTO SCVP_CONFIG(PROPERTYNAME, PROPERTYVALUE, CATEGORY, DISPLAYORDER, CREATEDDATE, CREATEDBY) VALUES (?, ?, ?, ?, ?,?)";
                preparedStatement = connection.prepareStatement(timeIntervalInsertQuery);
                preparedStatement.setString(1, "TIME_IN_MINUTES");
                preparedStatement.setString(2, timeInterval);
                preparedStatement.setString(3, "TIME_INTERVAL");
                preparedStatement.setInt(4, 1);
                preparedStatement.setTimestamp(5, currentDateTime);
                preparedStatement.setString(6, username);
                int insertedRows = preparedStatement.executeUpdate();
                if (insertedRows > 0) {
                    message = "<font color='green'>Time interval inserted successfully.</font>";
                } else {
                    message = "<font color='red'>Please try again!!</font>";
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                    preparedStatement = null;
                }
            }
        } catch (SQLException sqle) {
            LoggerUtility.log(logger, " SqlException occurred in appConfigEditTimeinterval  :: " + sqle.getMessage(), Level.ERROR, sqle.getCause());
        } catch (ServiceLocatorException sle) {
            LoggerUtility.log(logger, " ServiceLocatorException occurred in appConfigEditTimeinterval  :: " + sle.getMessage(), Level.ERROR, sle.getCause());
        } catch (Exception ex) {
            LoggerUtility.log(logger, " Exception occurred in appConfigEditDisplayRecords  :: " + ex.getMessage(), Level.ERROR, ex.getCause());
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                    resultSet = null;
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                    preparedStatement = null;
                }
                if (connection != null) {
                    connection.close();
                    connection = null;
                }
            } catch (SQLException sqlex) {
                LoggerUtility.log(logger, " Finally block SqlException occurred in appConfigEditTimeinterval  :: " + sqlex.getMessage(), Level.ERROR, sqlex.getCause());
            }
        }
        System.out.println("ended in appConfigEditTimeinterval method");
        return message;
    }

    public String appConfigEditTop10EDITP(String username, String allEDItp) {
        System.out.println("entered in appConfigEditTop10EDITP method");
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String message = null;
        try {
            com.mss.ediscv.util.Properties.setProperty("Top10EdiTradingPartners", allEDItp);
            int count = 0;
            Timestamp currentDateTime = DateUtility.getInstance().getCurrentDB2Timestamp();
            connection = ConnectionProvider.getInstance().getConnection();
            String checkTop10EDITPQuery = "select count(*) as Top10EDITPExisted from scvp_config where CATEGORY='MANUFACTURING_TOP_TEN_TP'";
            preparedStatement = connection.prepareStatement(checkTop10EDITPQuery);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                count = count + (resultSet.getInt("Top10EDITPExisted"));
            }
            if (resultSet != null) {
                resultSet.close();
                resultSet = null;
            }
            if (preparedStatement != null) {
                preparedStatement.close();
                preparedStatement = null;
            }
            if (count > 0) {
                String top10EDITPUpdateQuery = "UPDATE SCVP_CONFIG SET PROPERTYVALUE = ?,MODIFIEDDATE = ?, MODIFIEDBY = ? WHERE PROPERTYNAME = 'MANUFACTURING_TP_NAMES' AND CATEGORY = 'MANUFACTURING_TOP_TEN_TP'";
                preparedStatement = connection.prepareStatement(top10EDITPUpdateQuery);
                preparedStatement.setString(1, allEDItp);
                preparedStatement.setTimestamp(2, currentDateTime);
                preparedStatement.setString(3, username);
                int updatedRows = preparedStatement.executeUpdate();
                if (updatedRows > 0) {
                    message = "<font color='green'>Top ten EDI partners updated successfully.</font>";
                } else {
                    message = "<font color='red'>Please try again!!</font>";
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                    preparedStatement = null;
                }
            } else {
                String top10EDIInsertQuery = "INSERT INTO SCVP_CONFIG(PROPERTYNAME, PROPERTYVALUE, CATEGORY, DISPLAYORDER, CREATEDDATE, CREATEDBY) VALUES (?, ?, ?, ?, ?,?)";
                preparedStatement = connection.prepareStatement(top10EDIInsertQuery);
                preparedStatement.setString(1, "MANUFACTURING_TP_NAMES");
                preparedStatement.setString(2, allEDItp);
                preparedStatement.setString(3, "MANUFACTURING_TOP_TEN_TP");
                preparedStatement.setInt(4, 1);
                preparedStatement.setTimestamp(5, currentDateTime);
                preparedStatement.setString(6, username);
                int insertedRows = preparedStatement.executeUpdate();
                if (insertedRows > 0) {
                    message = "<font color='green'>Top ten EDI partners inserted successfully.</font>";
                } else {
                    message = "<font color='red'>Please try again!!</font>";
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                    preparedStatement = null;
                }
            }
        } catch (SQLException sqle) {
            LoggerUtility.log(logger, " SqlException occurred in appConfigEditTop10EDITP  :: " + sqle.getMessage(), Level.ERROR, sqle.getCause());
        } catch (ServiceLocatorException sle) {
            LoggerUtility.log(logger, " ServiceLocatorException occurred in appConfigEditTop10EDITP  :: " + sle.getMessage(), Level.ERROR, sle.getCause());
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                    resultSet = null;
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                    preparedStatement = null;
                }
                if (connection != null) {
                    connection.close();
                    connection = null;
                }
            } catch (SQLException sqlex) {
                LoggerUtility.log(logger, " Finally block SqlException occurred in appConfigEditTop10EDITP  :: " + sqlex.getMessage(), Level.ERROR, sqlex.getCause());
            }
        }
        System.out.println("ended in appConfigEditTop10EDITP method");
        return message;
    }

    public String appConfigEditScvpDatabaseValues(String username, String dbData) {
        System.out.println("entered in appConfigEditScvpDatabaseValues method");
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String message = null;
        JSONArray dbArray = null;
        JSONObject jsonObject = null;
        try {

            dbArray = new JSONArray(dbData);
            Timestamp currentDateTime = DateUtility.getInstance().getCurrentDB2Timestamp();
            connection = ConnectionProvider.getInstance().getConnection();
            for (int i = 0; i < dbArray.length(); i++) {
                jsonObject = dbArray.getJSONObject(i);
                String dbPropertyName = jsonObject.getString("dbPropertyName");
                String dbPropertyValue = jsonObject.getString("dbPropertyValue");
                String dbDisplayOrder = jsonObject.getString("dbDisplayOrder");

                com.mss.ediscv.util.Properties.setProperty(dbPropertyName, dbPropertyValue);
                int count = 0;
                String checkScvpDBQuery = "SELECT COUNT(*) AS SCVP_DATABASE_EXISTED FROM SCVP_CONFIG WHERE PROPERTYNAME='" + dbPropertyName + "' AND CATEGORY='SCVP_DATABASE'";
                preparedStatement = connection.prepareStatement(checkScvpDBQuery);
                resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    count = count + (resultSet.getInt("SCVP_DATABASE_EXISTED"));
                }
                if (resultSet != null) {
                    resultSet.close();
                    resultSet = null;
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                    preparedStatement = null;
                }
                if (count > 0) {
                    String scvpDBUpdateQuery = "UPDATE SCVP_CONFIG SET PROPERTYVALUE = ?, DISPLAYORDER = ?, MODIFIEDDATE = ?, MODIFIEDBY = ? WHERE PROPERTYNAME = ? AND CATEGORY = 'SCVP_DATABASE'";
                    preparedStatement = connection.prepareStatement(scvpDBUpdateQuery);
                    preparedStatement.setString(1, dbPropertyValue);
                    preparedStatement.setString(2, dbDisplayOrder);
                    preparedStatement.setTimestamp(3, currentDateTime);
                    preparedStatement.setString(4, username);
                    preparedStatement.setString(5, dbPropertyName);
                    int updatedRows = preparedStatement.executeUpdate();
                    if (updatedRows > 0) {
                        message = "<font color='green'>Database properties updated successfully.</font>";
                    } else {
                        message = "<font color='red'>Please try again!!</font>";
                    }
                    if (preparedStatement != null) {
                        preparedStatement.close();
                        preparedStatement = null;
                    }
                } else {
                    String scvpDBInsertQuery = "INSERT INTO SCVP_CONFIG(PROPERTYNAME, PROPERTYVALUE, CATEGORY, DISPLAYORDER, CREATEDDATE, CREATEDBY) VALUES (?, ?, ?, ?, ?, ?)";
                    preparedStatement = connection.prepareStatement(scvpDBInsertQuery);
                    preparedStatement.setString(1, dbPropertyName);
                    preparedStatement.setString(2, dbPropertyValue);
                    preparedStatement.setString(3, "SCVP_DATABASE");
                    preparedStatement.setString(4, dbDisplayOrder);
                    preparedStatement.setTimestamp(5, currentDateTime);
                    preparedStatement.setString(6, username);
                    int insertedRows = preparedStatement.executeUpdate();
                    if (insertedRows > 0) {
                        message = "<font color='green'>Database properties inserted successfully.</font>";
                    } else {
                        message = "<font color='red'>Please try again!!</font>";
                    }
                    if (preparedStatement != null) {
                        preparedStatement.close();
                        preparedStatement = null;
                    }
                }
            }
        } catch (JSONException ex) {
            java.util.logging.Logger.getLogger(AjaxHandlerServiceImpl.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (SQLException sqle) {
            LoggerUtility.log(logger, " SqlException occurred in appConfigEditScvpDatabaseValues  :: " + sqle.getMessage(), Level.ERROR, sqle.getCause());
        } catch (ServiceLocatorException sle) {
            LoggerUtility.log(logger, " ServiceLocatorException occurred in appConfigEditScvpDatabaseValues  :: " + sle.getMessage(), Level.ERROR, sle.getCause());
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                    resultSet = null;
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                    preparedStatement = null;
                }
                if (connection != null) {
                    connection.close();
                    connection = null;
                }
            } catch (SQLException sqlex) {
                LoggerUtility.log(logger, " Finally block SqlException occurred in appConfigEditScvpDatabaseValues  :: " + sqlex.getMessage(), Level.ERROR, sqlex.getCause());
            }
        }
        System.out.println("ended in appConfigEditScvpDatabaseValues method");
        return message;
    }

    public String appConfigEditEDITransactions(String username, String ediTransactionNamesList) {
        System.out.println("entered in appConfigEditEDITransactions method");
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String message = null;
        String category;
        String propertyName;
        try {
            com.mss.ediscv.util.Properties.setProperty("EdiTransactionsM", ediTransactionNamesList);
            int count = 0;
            Timestamp currentDateTime = DateUtility.getInstance().getCurrentDB2Timestamp();
            connection = ConnectionProvider.getInstance().getConnection();
            String checkTransactionsQuery = "select count(*) as transactionsExisted from scvp_config where CATEGORY='EDI_TRANSACTIONS'";
            preparedStatement = connection.prepareStatement(checkTransactionsQuery);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                count = count + (resultSet.getInt("transactionsExisted"));
            }
            if (resultSet != null) {
                resultSet.close();
                resultSet = null;
            }
            if (preparedStatement != null) {
                preparedStatement.close();
                preparedStatement = null;
            }
            if (count > 0) {
                String adaptersUpdateQuery = "UPDATE SCVP_CONFIG SET PROPERTYVALUE = ?,MODIFIEDDATE = ?, MODIFIEDBY = ? WHERE PROPERTYNAME = 'EDI_TRANSACTIONS' AND CATEGORY = 'EDI_TRANSACTIONS'";
                preparedStatement = connection.prepareStatement(adaptersUpdateQuery);
                preparedStatement.setString(1, ediTransactionNamesList);
                preparedStatement.setTimestamp(2, currentDateTime);
                preparedStatement.setString(3, username);
                int updatedRows = preparedStatement.executeUpdate();
                if (updatedRows > 0) {
                    message = "<font color='green'>Transactions updated successfully.</font>";
                } else {
                    message = "<font color='red'>Please try again!!</font>";
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                    preparedStatement = null;
                }
            } else {
                String adaptersInsertQuery = "INSERT INTO SCVP_CONFIG(PROPERTYNAME, PROPERTYVALUE, CATEGORY, DISPLAYORDER, CREATEDDATE, CREATEDBY) VALUES (?, ?, ?, ?, ?,?)";
                preparedStatement = connection.prepareStatement(adaptersInsertQuery);
                preparedStatement.setString(1, "EDI_TRANSACTIONS");
                preparedStatement.setString(2, ediTransactionNamesList);
                preparedStatement.setString(3, "EDI_TRANSACTIONS");
                preparedStatement.setInt(4, 1);
                preparedStatement.setTimestamp(5, currentDateTime);
                preparedStatement.setString(6, username);
                int insertedRows = preparedStatement.executeUpdate();
                if (insertedRows > 0) {
                    message = "<font color='green'>Transactions inserted successfully.</font>";
                } else {
                    message = "<font color='red'>Please try again!!</font>";
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                    preparedStatement = null;
                }
            }
        } catch (SQLException sqle) {
            LoggerUtility.log(logger, " SqlException occurred in appConfigEditEDITransactions  :: " + sqle.getMessage(), Level.ERROR, sqle.getCause());
        } catch (ServiceLocatorException sle) {
            LoggerUtility.log(logger, " ServiceLocatorException occurred in appConfigEditEDITransactions  :: " + sle.getMessage(), Level.ERROR, sle.getCause());
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                    resultSet = null;
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                    preparedStatement = null;
                }
                if (connection != null) {
                    connection.close();
                    connection = null;
                }
            } catch (SQLException sqlex) {
                LoggerUtility.log(logger, " Finally block SqlException occurred in appConfigEditEDITransactions :: " + sqlex.getMessage(), Level.ERROR, sqlex.getCause());
            }
        }
        System.out.println("ended in appConfigEditEDITransactions method");
        return message;
    }

    public String appConfigEditRAILTransactions(String username, String railTransactionNamesList) {
        System.out.println("entered in appConfigEditRAILTransactions method");
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String message = null;
        try {
            com.mss.ediscv.util.Properties.setProperty("EdiTransactionsL", railTransactionNamesList);
            int count = 0;
            Timestamp currentDateTime = DateUtility.getInstance().getCurrentDB2Timestamp();
            connection = ConnectionProvider.getInstance().getConnection();
            String checkTransactionsQuery = "select count(*) as transactionsExisted from scvp_config where CATEGORY='RAIL_TRANSACTIONS'";
            preparedStatement = connection.prepareStatement(checkTransactionsQuery);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                count = count + (resultSet.getInt("transactionsExisted"));
            }
            if (resultSet != null) {
                resultSet.close();
                resultSet = null;
            }
            if (preparedStatement != null) {
                preparedStatement.close();
                preparedStatement = null;
            }
            if (count > 0) {
                String adaptersUpdateQuery = "UPDATE SCVP_CONFIG SET PROPERTYVALUE = ?,MODIFIEDDATE = ?, MODIFIEDBY = ? WHERE PROPERTYNAME = 'RAIL_TRANSACTIONS' AND CATEGORY = 'RAIL_TRANSACTIONS'";
                preparedStatement = connection.prepareStatement(adaptersUpdateQuery);
                preparedStatement.setString(1, railTransactionNamesList);
                preparedStatement.setTimestamp(2, currentDateTime);
                preparedStatement.setString(3, username);
                int updatedRows = preparedStatement.executeUpdate();
                if (updatedRows > 0) {
                    message = "<font color='green'>Transactions updated successfully.</font>";
                } else {
                    message = "<font color='red'>Please try again!!</font>";
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                    preparedStatement = null;
                }
            } else {
                String adaptersInsertQuery = "INSERT INTO SCVP_CONFIG(PROPERTYNAME, PROPERTYVALUE, CATEGORY, DISPLAYORDER, CREATEDDATE, CREATEDBY) VALUES (?, ?, ?, ?, ?,?)";
                preparedStatement = connection.prepareStatement(adaptersInsertQuery);
                preparedStatement.setString(1, "RAIL_TRANSACTIONS");
                preparedStatement.setString(2, railTransactionNamesList);
                preparedStatement.setString(3, "RAIL_TRANSACTIONS");
                preparedStatement.setInt(4, 1);
                preparedStatement.setTimestamp(5, currentDateTime);
                preparedStatement.setString(6, username);
                int insertedRows = preparedStatement.executeUpdate();
                if (insertedRows > 0) {
                    message = "<font color='green'>Transactions inserted successfully.</font>";
                } else {
                    message = "<font color='red'>Please try again!!</font>";
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                    preparedStatement = null;
                }
            }
        } catch (SQLException sqle) {
            LoggerUtility.log(logger, " SqlException occurred in appConfigEditRAILTransactions  :: " + sqle.getMessage(), Level.ERROR, sqle.getCause());
        } catch (ServiceLocatorException sle) {
            LoggerUtility.log(logger, " ServiceLocatorException occurred in appConfigEditRAILTransactions  :: " + sle.getMessage(), Level.ERROR, sle.getCause());
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                    resultSet = null;
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                    preparedStatement = null;
                }
                if (connection != null) {
                    connection.close();
                    connection = null;
                }
            } catch (SQLException sqlex) {
                LoggerUtility.log(logger, " Finally block SqlException occurred in appConfigEditEDITransactions :: " + sqlex.getMessage(), Level.ERROR, sqlex.getCause());
            }
        }
        System.out.println("ended in appConfigEditRAILTransactions method");
        return message;
    }

    public String appConfigEditBaseValues(String username, String baseValueData) {
        System.out.println("entered in appConfigEditBaseValues method");
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String message = null;
        JSONArray baseValueArray = null;
        JSONObject jsonObject = null;
        try {
            baseValueArray = new JSONArray(baseValueData);
            Timestamp currentDateTime = DateUtility.getInstance().getCurrentDB2Timestamp();
            connection = ConnectionProvider.getInstance().getConnection();
            for (int i = 0; i < baseValueArray.length(); i++) {
                jsonObject = baseValueArray.getJSONObject(i);
                String baseValuePropertyName = "DocumentBaseValue" + jsonObject.getString("baseValuePropertyName");
                String baseValuePropertyValue = jsonObject.getString("baseValuePropertyValue");
                String baseValueDisplayOrder = jsonObject.getString("baseValueDisplayOrder");

                com.mss.ediscv.util.Properties.setProperty(baseValuePropertyName, baseValuePropertyValue);
                int count = 0;
                String checkBaseValueDBQuery = "SELECT COUNT(*) AS BASE_VALUE_EXISTED FROM SCVP_CONFIG WHERE PROPERTYNAME='" + baseValuePropertyName + "' AND CATEGORY='BASE_VALUE'";
                preparedStatement = connection.prepareStatement(checkBaseValueDBQuery);
                resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    count = count + (resultSet.getInt("BASE_VALUE_EXISTED"));
                }
                if (resultSet != null) {
                    resultSet.close();
                    resultSet = null;
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                    preparedStatement = null;
                }
                if (count > 0) {
                    String baseVlaueUpdateQuery = "UPDATE SCVP_CONFIG SET PROPERTYVALUE = ?, DISPLAYORDER = ?, MODIFIEDDATE = ?, MODIFIEDBY = ? WHERE PROPERTYNAME = ? AND CATEGORY = 'BASE_VALUE'";
                    preparedStatement = connection.prepareStatement(baseVlaueUpdateQuery);
                    preparedStatement.setString(1, baseValuePropertyValue);
                    preparedStatement.setString(2, baseValueDisplayOrder);
                    preparedStatement.setTimestamp(3, currentDateTime);
                    preparedStatement.setString(4, username);
                    preparedStatement.setString(5, baseValuePropertyName);
                    int updatedRows = preparedStatement.executeUpdate();
                    if (updatedRows > 0) {
                        message = "<font color='green'>Planned outage properties updated successfully.</font>";
                    } else {
                        message = "<font color='red'>Please try again!!</font>";
                    }
                    if (preparedStatement != null) {
                        preparedStatement.close();
                        preparedStatement = null;
                    }
                } else {
                    String baseVlaueInsertQuery = "INSERT INTO SCVP_CONFIG(PROPERTYNAME, PROPERTYVALUE, CATEGORY, DISPLAYORDER, CREATEDDATE, CREATEDBY) VALUES (?, ?, ?, ?, ?, ?)";
                    preparedStatement = connection.prepareStatement(baseVlaueInsertQuery);
                    preparedStatement.setString(1, baseValuePropertyName);
                    preparedStatement.setString(2, baseValuePropertyValue);
                    preparedStatement.setString(3, "BASE_VALUE");
                    preparedStatement.setString(4, baseValueDisplayOrder);
                    preparedStatement.setTimestamp(5, currentDateTime);
                    preparedStatement.setString(6, username);
                    int insertedRows = preparedStatement.executeUpdate();
                    if (insertedRows > 0) {
                        message = "<font color='green'>Base vlaue properties inserted successfully.</font>";
                    } else {
                        message = "<font color='red'>Please try again!!</font>";
                    }
                    if (preparedStatement != null) {
                        preparedStatement.close();
                        preparedStatement = null;
                    }
                }
            }
        } catch (JSONException ex) {
            java.util.logging.Logger.getLogger(AjaxHandlerServiceImpl.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (SQLException sqle) {
            LoggerUtility.log(logger, " SqlException occurred in appConfigEditBaseValues  :: " + sqle.getMessage(), Level.ERROR, sqle.getCause());
        } catch (ServiceLocatorException sle) {
            LoggerUtility.log(logger, " ServiceLocatorException occurred in appConfigEditBaseValues  :: " + sle.getMessage(), Level.ERROR, sle.getCause());
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                    resultSet = null;
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                    preparedStatement = null;
                }
                if (connection != null) {
                    connection.close();
                    connection = null;
                }
            } catch (SQLException sqlex) {
                LoggerUtility.log(logger, " Finally block SqlException occurred in appConfigEditBaseValues  :: " + sqlex.getMessage(), Level.ERROR, sqlex.getCause());
            }
        }
        System.out.println("ended in appConfigEditBaseValues method");
        return message;
    }

    public String appConfigEditTop10RailTP(String username, String allRailtp) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String message = null;
        try {
            com.mss.ediscv.util.Properties.setProperty("Top10LogisticsTradingPartners", allRailtp);
            int count = 0;
            Timestamp currentDateTime = DateUtility.getInstance().getCurrentDB2Timestamp();
            connection = ConnectionProvider.getInstance().getConnection();
            String checkTop10RailTPQuery = "select count(*) as Top10RAILTPExisted from scvp_config where CATEGORY='LOGISTIC_TOP_TEN_TP'";
            preparedStatement = connection.prepareStatement(checkTop10RailTPQuery);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                count = count + (resultSet.getInt("Top10RAILTPExisted"));
            }
            if (resultSet != null) {
                resultSet.close();
                resultSet = null;
            }
            if (preparedStatement != null) {
                preparedStatement.close();
                preparedStatement = null;
            }
            if (count > 0) {
                String top10RailTPUpdateQuery = "UPDATE SCVP_CONFIG SET PROPERTYVALUE = ?,MODIFIEDDATE = ?, MODIFIEDBY = ? WHERE PROPERTYNAME = 'LOGISTIC_TP_NAMES' AND CATEGORY = 'LOGISTIC_TOP_TEN_TP'";
                preparedStatement = connection.prepareStatement(top10RailTPUpdateQuery);
                preparedStatement.setString(1, allRailtp);
                preparedStatement.setTimestamp(2, currentDateTime);
                preparedStatement.setString(3, username);
                int updatedRows = preparedStatement.executeUpdate();
                if (updatedRows > 0) {
                    message = "<font color='green'>Top ten Rail partners updated successfully.</font>";
                } else {
                    message = "<font color='red'>Please try again!!</font>";
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                    preparedStatement = null;
                }
            } else {
                String top10RailInsertQuery = "INSERT INTO SCVP_CONFIG(PROPERTYNAME, PROPERTYVALUE, CATEGORY, DISPLAYORDER, CREATEDDATE, CREATEDBY) VALUES (?, ?, ?, ?, ?,?)";
                preparedStatement = connection.prepareStatement(top10RailInsertQuery);
                preparedStatement.setString(1, "LOGISTIC_TP_NAMES");
                preparedStatement.setString(2, allRailtp);
                preparedStatement.setString(3, "LOGISTIC_TOP_TEN_TP");
                preparedStatement.setInt(4, 1);
                preparedStatement.setTimestamp(5, currentDateTime);
                preparedStatement.setString(6, username);
                int insertedRows = preparedStatement.executeUpdate();
                if (insertedRows > 0) {
                    message = "<font color='green'>Top ten Rail partners inserted successfully.</font>";
                } else {
                    message = "<font color='red'>Please try again!!</font>";
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                    preparedStatement = null;
                }
            }
        } catch (SQLException sqle) {
            LoggerUtility.log(logger, " SqlException occurred in appConfigEditTop10RailTP  :: " + sqle.getMessage(), Level.ERROR, sqle.getCause());
        } catch (ServiceLocatorException sle) {
            LoggerUtility.log(logger, " ServiceLocatorException occurred in appConfigEditTop10RailTP  :: " + sle.getMessage(), Level.ERROR, sle.getCause());
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                    resultSet = null;
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                    preparedStatement = null;
                }
                if (connection != null) {
                    connection.close();
                    connection = null;
                }
            } catch (SQLException sqlex) {
                LoggerUtility.log(logger, " Finally block SqlException occurred in appConfigEditTop10RailTP  :: " + sqlex.getMessage(), Level.ERROR, sqlex.getCause());
            }
        }
        return message;
    }

    public HttpServletRequest getHttpServletRequest() {
        return httpServletRequest;
    }

    public void setHttpServletRequest(HttpServletRequest httpServletRequest) {
        this.httpServletRequest = httpServletRequest;
    }
}
