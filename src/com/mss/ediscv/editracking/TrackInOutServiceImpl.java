/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mss.ediscv.editracking;

import com.mss.ediscv.util.ConnectionProvider;
import com.mss.ediscv.util.DataSourceDataProvider;
import com.mss.ediscv.util.DateUtility;
import com.mss.ediscv.util.ServiceLocatorException;
import com.mss.ediscv.util.WildCardSql;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import com.mss.ediscv.util.LoggerUtility;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author miracle
 */
public class TrackInOutServiceImpl implements TrackInOutService {

    private static Logger logger = LogManager.getLogger(TrackInOutServiceImpl.class.getName());
    String tmp_Recieved_From = "";
    String tmp_Recieved_ToTime = "";
    String responseString = null;
    private List docTypeList;
    private int inbound;
    private int outbound;
    private double filesize;
    private double filesize1;
    private ArrayList<TrackInOutBean> documentList;
    int fromYear = 0;
    int toYear = 0;
    int fromMonth = 0;
    int toMonth = 0;

    @Override
    public ArrayList<TrackInOutBean> getDocumentList(TrackInOutAction trackInOutAction) throws ServiceLocatorException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
        String docdatepicker = trackInOutAction.getDocdatepicker();
        String docdatepickerfrom = trackInOutAction.getDocdatepickerfrom();
        docTypeList = DataSourceDataProvider.getInstance().getDocumentTypeList("M");
        String temp = "No Value";
        ArrayList temperoryList = new ArrayList();
        ArrayList inboundList = new ArrayList();
        ArrayList outboundList = new ArrayList();
        ArrayList documentTypeList = new ArrayList();
        ArrayList dateMonth = new ArrayList();
        ArrayList dateMonthdocType = new ArrayList();
        StringBuilder documentSearchQuery = new StringBuilder();
        documentSearchQuery.append("select count(DIRECTION) as total from FILES where DIRECTION=? and TRANSACTION_TYPE=? AND FLOWFLAG='M'");
       System.out.println("CommunicationProtocols query:"+documentSearchQuery.toString());
        if (docdatepicker != null && !"".equals(docdatepicker) && docdatepickerfrom != null && !"".equals(docdatepickerfrom)) {
            documentSearchQuery.append(" and year(DATE_TIME_RECEIVED)=? and month(DATE_TIME_RECEIVED)=?");
            fromYear = DateUtility.getInstance().monthYear(docdatepickerfrom, "year");
            toYear = DateUtility.getInstance().monthYear(docdatepicker, "year");
            fromMonth = DateUtility.getInstance().monthYear(docdatepickerfrom, "month");
            toMonth = DateUtility.getInstance().monthYear(docdatepicker, "month");
        }
        inbound = 0;
        outbound = 0;
            connection = ConnectionProvider.getInstance().getConnection();
            preparedStatement = connection.prepareStatement(documentSearchQuery.toString());
            TrackInOutBean trackInOutBean;
            documentList = new ArrayList<TrackInOutBean>();
            System.out.println("Query and resultset start time::"+DateUtility.getInstance().getCurrentDB2Timestamp());
            for (int k = fromYear; k <= toYear; k++) {
                for (int j = fromMonth; j < 13; j++) {
                    for (int i = 0; i < docTypeList.size(); i++) {
                        preparedStatement.setString(1, "INBOUND");
                        preparedStatement.setString(2, (String) docTypeList.get(i));
                        preparedStatement.setInt(3, k);
                        preparedStatement.setInt(4, j);
                        resultSet = preparedStatement.executeQuery();
                        if (resultSet.next()) {
                            inbound = resultSet.getInt("total");
                        }
                        resultSet.close();
                        preparedStatement.setString(1, "OUTBOUND");
                        preparedStatement.setString(2, (String) docTypeList.get(i));
                        preparedStatement.setInt(3, k);
                        preparedStatement.setInt(4, j);
                        resultSet = preparedStatement.executeQuery();
                        if (resultSet.next()) {
                            outbound = resultSet.getInt("total");
                        }
                        resultSet.close();
                        if (inbound != 0 || outbound != 0) {
                            if (!documentTypeList.contains((String) docTypeList.get(i))) {
                                documentTypeList.add((String) docTypeList.get(i));
                            }
                            if (temp != null && !temp.equalsIgnoreCase(k + "/" + j)) {
                                temperoryList.add(k + "/" + j);
                                temp = k + "/" + j;
                                dateMonth.add(k + "/" + j);
                            }
                            temperoryList.add((String) docTypeList.get(i));
                            temperoryList.add(inbound);
                            temperoryList.add(outbound);
                            temperoryList.add(inbound + outbound);
                            if (inbound != 0) {
                                inboundList.add((String) docTypeList.get(i));
                            }
                            if (outbound != 0) {
                                outboundList.add((String) docTypeList.get(i));
                            }
                        }
                    }
                    if (!temperoryList.isEmpty()) {
                        dateMonthdocType.add(temperoryList.clone());
                    }
                    temperoryList.clear();
                    if (j == toMonth && k == toYear) {
                        break;
                    }
                    if (j == 12) {
                        fromMonth = 1;
                    }
                }
            }
            System.out.println("Resultset end time::"+DateUtility.getInstance().getCurrentDB2Timestamp());
            trackInOutBean = new TrackInOutBean();
            trackInOutBean.setDocumentTypeList(documentTypeList);
            trackInOutBean.setDateMonthdocType(dateMonthdocType);
            trackInOutBean.setDateMonth(dateMonth);
            trackInOutBean.setInboundList(inboundList);
            trackInOutBean.setOutboundList(outboundList);
            documentList.add(trackInOutBean);
        } catch (SQLException sqlException) {
            LoggerUtility.log(logger, "SQLException occurred in getDocumentList method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
        } catch (Exception exception) {
            LoggerUtility.log(logger, "Exception occurred in getDocumentList method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
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
                LoggerUtility.log(logger, "finally SQLException occurred in getDocumentList method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
            }
        }
        return documentList;
    }

    @Override
    public ArrayList<TrackInOutBean> getSummaryDetails(TrackInOutAction trackInOutAction) throws ServiceLocatorException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            String docdatepicker = trackInOutAction.getDocdatepicker();
            String docdatepickerfrom = trackInOutAction.getDocdatepickerfrom();
            String docNetworkvan = "";
            if (trackInOutAction.getDocNetworkvan() != null && !trackInOutAction.getDocNetworkvan().equals("-1")) {
                docNetworkvan = trackInOutAction.getDocNetworkvan();
            }
            docTypeList = DataSourceDataProvider.getInstance().getDocumentTypeList("M");
            Map partnerMap = DataSourceDataProvider.getInstance().getDashboardPartnerMap("2");
            int inboundTotal = 0;
            int outboundTotal = 0;
            double filesizeTotal = 0;
            double filesizeTotal1 = 0;
            TrackInOutBean trackInOutBean;
            StringBuilder documentSearchQuery = new StringBuilder();
            documentSearchQuery.append("select count(DIRECTION) as total,cast(SUM(FILE_SIZE)/1024 as decimal(10,2)) as total_size from FILES where DIRECTION=? and TRANSACTION_TYPE=?");
            documentSearchQuery.append(" and (SENDER_ID = ? or RECEIVER_ID=?) AND FLOWFLAG='M'");

            if (docNetworkvan != null && !"".equals(docNetworkvan.trim())) {
                documentSearchQuery.append(WildCardSql.getWildCardSql1("FILES.NETWORK_VAN", docNetworkvan.trim()));
            }
            if (docdatepicker != null && !"".equals(docdatepicker)) {
                tmp_Recieved_From = DateUtility.getInstance().DateViewToDBCompare(docdatepicker);
                documentSearchQuery.append(" AND FILES.DATE_TIME_RECEIVED <= '" + tmp_Recieved_From + "'");
            }
            if (docdatepickerfrom != null && !"".equals(docdatepickerfrom)) {
                tmp_Recieved_From = DateUtility.getInstance().DateViewToDBCompare(docdatepickerfrom);
                documentSearchQuery.append(" AND FILES.DATE_TIME_RECEIVED >= '" + tmp_Recieved_From + "'");
            }
            inbound = 0;
            outbound = 0;
            filesize = 0;
            filesize1 = 0;
            connection = ConnectionProvider.getInstance().getConnection();
            preparedStatement = connection.prepareStatement(documentSearchQuery.toString());
            documentList = new ArrayList<TrackInOutBean>();
            Iterator entries;
            for (int i = 0; i < docTypeList.size(); i++) {
                entries = partnerMap.entrySet().iterator();
                while (entries.hasNext()) {
                    Map.Entry thisEntry = (Map.Entry) entries.next();
                    Object key = thisEntry.getKey();
                    Object value = thisEntry.getValue();
                    preparedStatement.setString(1, "INBOUND");
                    preparedStatement.setString(2, (String) docTypeList.get(i));
                    preparedStatement.setString(3, (String) key);
                    preparedStatement.setString(4, (String) key);
                    resultSet = preparedStatement.executeQuery();
                    if (resultSet.next()) {
                        inbound = resultSet.getInt("total");
                        filesize = resultSet.getDouble("total_size");
                    }
                    resultSet.close();
                    preparedStatement.setString(1, "OUTBOUND");
                    preparedStatement.setString(2, (String) docTypeList.get(i));
                    preparedStatement.setString(3, (String) key);
                    preparedStatement.setString(4, (String) key);
                    resultSet = preparedStatement.executeQuery();
                    if (resultSet.next()) {
                        outbound = resultSet.getInt("total");
                        filesize1 = resultSet.getDouble("total_size");
                    }
                    resultSet.close();
                    if (inbound != 0 || outbound != 0) {
                        trackInOutBean = new TrackInOutBean();
                        trackInOutBean.setTransaction_type("");
                        trackInOutBean.setPname((String) value);
                        trackInOutBean.setFilesize(filesize);
                        trackInOutBean.setFilesize1(filesize1);
                        trackInOutBean.setFilesizeTotal(filesize + filesize1);
                        trackInOutBean.setInbound(inbound);
                        trackInOutBean.setOutbound(outbound);
                        trackInOutBean.setTotal(inbound + outbound);
                        filesizeTotal = filesizeTotal + filesize;
                        filesizeTotal1 = filesizeTotal1 + filesize1;
                        inboundTotal = inboundTotal + inbound;
                        outboundTotal = outboundTotal + outbound;
                        documentList.add(trackInOutBean);
                    }
                }
                if (inboundTotal != 0 || outboundTotal != 0) {
                    trackInOutBean = new TrackInOutBean();
                    trackInOutBean.setTransaction_type((String) docTypeList.get(i));
                    trackInOutBean.setPname("Total");
                    trackInOutBean.setFilesizeTotal(filesizeTotal + filesizeTotal1);
                    trackInOutBean.setInbound(inboundTotal);
                    trackInOutBean.setOutbound(outboundTotal);
                    trackInOutBean.setTotal(inboundTotal + outboundTotal);
                    documentList.add(trackInOutBean);
                    inboundTotal = 0;
                    outboundTotal = 0;
                    filesizeTotal = 0;
                    filesizeTotal1 = 0;
                }
            }
        } catch (SQLException sqlException) {
            LoggerUtility.log(logger, "SQLException occurred in getSummaryDetails method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
        } catch (Exception exception) {
            LoggerUtility.log(logger, "Exception occurred in getSummaryDetails method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
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
                LoggerUtility.log(logger, "finally SQLException occurred in getSummaryDetails method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
            }
        }
        return documentList;
    }

    @Override
    public ArrayList<TrackInOutBean> getInquiryDetails(TrackInOutAction trackInOutAction) throws ServiceLocatorException {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            String docdatepicker = trackInOutAction.getDocdatepicker();
            String docdatepickerfrom = trackInOutAction.getDocdatepickerfrom();
            String doctype = "";
            String partner = "";
            if (trackInOutAction.getDocType() != null && !trackInOutAction.getDocType().equals("-1")) {
                doctype = trackInOutAction.getDocType();
            }
            if (trackInOutAction.getPartnerMapId() != null && !trackInOutAction.getPartnerMapId().equals("-1")) {
                partner = trackInOutAction.getPartnerMapId();
            }
            StringBuilder documentSearchQuery = new StringBuilder();
            documentSearchQuery.append("SELECT FILES.TRANSACTION_TYPE as TRANSACTION_TYPE,FILES.DIRECTION as DIRECTION, "
                    + "FILES.DATE_TIME_RECEIVED as DATE_TIME_RECEIVED,FILES.ACK_STATUS as ACK_STATUS,"
                    + "TP2.NAME as RECEIVER_NAME,TP1.NAME as SENDER_NAME FROM FILES "
                    + "LEFT OUTER JOIN TP TP1 ON (TP1.ID=FILES.SENDER_ID) LEFT OUTER JOIN TP TP2 "
                    + "ON (TP2.ID=FILES.RECEIVER_ID)");
            documentSearchQuery.append(" WHERE 1=1 AND FLOWFLAG='M'");
            if (doctype != null && !"".equals(doctype.trim())) {
                documentSearchQuery.append(WildCardSql.getWildCardSql1("FILES.TRANSACTION_TYPE", doctype.trim()));
            }
            if (partner != null && !"".equals(partner.trim())) {
                documentSearchQuery.append(" AND TP.ID='" + partner.trim().toUpperCase() + "'");
            }
            if (docdatepicker != null && !"".equals(docdatepicker)) {
                tmp_Recieved_From = DateUtility.getInstance().DateViewToDBCompare(docdatepicker);
                documentSearchQuery.append(" AND FILES.DATE_TIME_RECEIVED <= '" + tmp_Recieved_From + "'");
            }
            if (docdatepickerfrom != null && !"".equals(docdatepickerfrom)) {
                tmp_Recieved_From = DateUtility.getInstance().DateViewToDBCompare(docdatepickerfrom);
                documentSearchQuery.append(" AND FILES.DATE_TIME_RECEIVED >= '" + tmp_Recieved_From + "'");
            }
            documentSearchQuery.append(" order by DATE_TIME_RECEIVED DESC fetch first 50 rows only");

            connection = ConnectionProvider.getInstance().getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(documentSearchQuery.toString());
            documentList = new ArrayList<TrackInOutBean>();
            while (resultSet.next()) {
                TrackInOutBean trackInOutBean = new TrackInOutBean();
                trackInOutBean.setTransaction_type(resultSet.getString("TRANSACTION_TYPE"));
                String direction = resultSet.getString("DIRECTION");
                trackInOutBean.setDirection(direction);
                trackInOutBean.setDate_time_rec(resultSet.getTimestamp("DATE_TIME_RECEIVED"));
                trackInOutBean.setPname(resultSet.getString("SENDER_NAME"));
                trackInOutBean.setPname(resultSet.getString("RECEIVER_NAME"));
                trackInOutBean.setAckStatus(resultSet.getString("ACK_STATUS"));
                documentList.add(trackInOutBean);
            }
        } catch (SQLException sqlException) {
            LoggerUtility.log(logger, "SQLException occurred in getInquiryDetails method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
        } catch (Exception exception) {
            LoggerUtility.log(logger, "Exception occurred in getInquiryDetails method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
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
            LoggerUtility.log(logger, "finally SQLException occurred in getInquiryDetails method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
        }
        }
        return documentList;
    }
}
