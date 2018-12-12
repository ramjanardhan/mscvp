/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mss.ediscv.simon;

import com.mss.ediscv.util.ConnectionProvider;
import com.mss.ediscv.util.DateUtility;
import com.mss.ediscv.util.LoggerUtility;
import com.mss.ediscv.util.ServiceLocatorException;
import com.mss.ediscv.util.WildCardSql;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author janardhan
 */
public class SimonServiceImpl implements SimonService {

    private static Logger logger = LogManager.getLogger(SimonServiceImpl.class.getName());

    List<SimonBean> transactionSearchList = new ArrayList<SimonBean>();
    String responseString = null;

    public List getDetails(SimonAction simonAction) {

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        StringBuilder detailsQuery = new StringBuilder();
        String corrattribute = simonAction.getReferenceName();
        String corrvalue = simonAction.getReferenceValue();

        detailsQuery.append("SELECT DISTINCT(FILES.FILE_ID)as FILE_ID,FILES.ISA_NUMBER as ISA_NUMBER,"
                + "FILES.FILE_TYPE as FILE_TYPE,"
                + "FILES.TRANSACTION_TYPE as TRANSACTION_TYPE,FILES.DIRECTION as DIRECTION,"
                + "FILES.DATE_TIME_RECEIVED as DATE_TIME_RECEIVED,FILES.STATUS as STATUS,"
                + "TP2.NAME as RECEIVER_NAME,TP1.NAME as SENDER_NAME"
                + ",FILES.FILE_TYPE,FILES.GS_CONTROL_NUMBER AS GS_NUMBER"
                + ",FILES.SENDER_ID,FILES.RECEIVER_ID,FILES.MAP_NAME AS MAP,FILES.MAILBOX_NAME,FILES.FILENAME,PO.PO_NUMBER AS PO_NUMBER,ASN.ASN_NUMBER AS ASN_NUMBER,INVOICE.INVOICE_NUMBER AS INVOICE_NUMBER,TP_DETAILS.APPLICATIONID AS APP_ID "
                + " FROM FILES LEFT OUTER JOIN PO ON(PO.FILE_ID=FILES.FILE_ID) LEFT OUTER JOIN ASN ON (ASN.FILE_ID=FILES.FILE_ID)"
                + "LEFT OUTER JOIN INVOICE ON (INVOICE.FILE_ID=FILES.FILE_ID) LEFT OUTER JOIN TP_DETAILS ON (FILES.SENDER_ID=TP_DETAILS.TP_ID)"
                + "LEFT OUTER JOIN TP TP1 ON (TP1.ID=FILES.SENDER_ID) LEFT OUTER JOIN TP TP2 "
                + "ON (TP2.ID=FILES.RECEIVER_ID)"
                + ""
                + " WHERE 1=1 AND FLOWFLAG like 'M' ");
        if ((corrattribute != null) && (corrattribute.equalsIgnoreCase("PO Number"))) {
            if (corrvalue != null && !"".equals(corrvalue.trim())) {
                detailsQuery.append(WildCardSql.getWildCardSql1("PO.PO_NUMBER", corrvalue.trim().toUpperCase()));
            }
        }

        if ((corrattribute != null) && (corrattribute.equalsIgnoreCase("Shipment ID"))) {
            if (corrvalue != null && !"".equals(corrvalue.trim())) {
                detailsQuery.append(WildCardSql.getWildCardSql1("ASN.ASN_NUMBER", corrvalue.trim().toUpperCase()));
            }
        }

        if ((corrattribute != null) && (corrattribute.equalsIgnoreCase("Invoice Number"))) {
            if (corrvalue != null && !"".equals(corrvalue.trim())) {
                detailsQuery.append(WildCardSql.getWildCardSql1("INVOICE.INVOICE_NUMBER", corrvalue.trim().toUpperCase()));
            }
        }

        if ((simonAction.getPartnerName() != null) && (!"".equals(simonAction.getPartnerName().trim()))) {
            detailsQuery.append(WildCardSql.getWildCardSql1("TP1.NAME", simonAction.getPartnerName().trim().toUpperCase()));
        }
        if ((simonAction.getPartnerName() != null) && (!"".equals(simonAction.getPartnerName().trim()))) {
            detailsQuery.append(WildCardSql.getWildCardSql1("TP2.NAME", simonAction.getPartnerName().trim().toUpperCase()));
        }
        if (simonAction.getApplicationId() != null && !"".equalsIgnoreCase(simonAction.getApplicationId())) {

        }
        if ((simonAction.getRecId() != null) && (!"".equals(simonAction.getRecId().trim()))) {
            detailsQuery.append(WildCardSql.getWildCardSql1("TP2.ID", simonAction.getRecId().trim().toUpperCase()));
        }
        if ((simonAction.getSenderId() != null) && (!"".equals(simonAction.getSenderId().trim()))) {
            detailsQuery.append(WildCardSql.getWildCardSql1("TP1.ID", simonAction.getSenderId().trim().toUpperCase()));
        }
        if (simonAction.getTransactionType() != null && !"-1".equalsIgnoreCase(simonAction.getTransactionType())) {
            detailsQuery.append(WildCardSql.getWildCardSql1("FILES.TRANSACTION_TYPE", simonAction.getTransactionType().trim()));
        }
        if (simonAction.getDocumentType() != null && !"-1".equalsIgnoreCase(simonAction.getDocumentType())) {
            detailsQuery.append(WildCardSql.getWildCardSql1("FILES.FILE_TYPE", simonAction.getDocumentType().trim()));
        }
        if (simonAction.getDirection() != null && !"-1".equalsIgnoreCase(simonAction.getDirection())) {
            detailsQuery.append(WildCardSql.getWildCardSql1("FILES.DIRECTION", simonAction.getDirection()));
        }
        if (simonAction.getStatus() != null && !"-1".equalsIgnoreCase(simonAction.getStatus())) {
            detailsQuery.append(WildCardSql.getWildCardSql1("FILES.STATUS", simonAction.getStatus().trim()));
        }
        if (simonAction.getCountryCode() != null && !"".equalsIgnoreCase(simonAction.getCountryCode())) {
        }
        if (simonAction.getDeliveryChannel() != null && !"".equalsIgnoreCase(simonAction.getDeliveryChannel())) {
        }
        if ((simonAction.getDatepickerfrom() != null) && (!"".equals(simonAction.getDatepickerTo()))) {
            String tmp_Recieved_From = null;
            try {
                tmp_Recieved_From = DateUtility.getInstance().DateViewToDBCompare(simonAction.getDatepickerTo());
            } catch (ServiceLocatorException serviceLocatorException) {
                LoggerUtility.log(logger, "ServiceLocatorException occurred in getDetails method:: " + serviceLocatorException.getMessage(), Level.ERROR, serviceLocatorException.getCause());
            }
            detailsQuery.append(" AND FILES.DATE_TIME_RECEIVED <= '" + tmp_Recieved_From + "'");
        }
        if ((simonAction.getDatepickerfrom() != null) && (!"".equals(simonAction.getDatepickerTo()))) {
            String tmp_Recieved_From = null;
            try {
                tmp_Recieved_From = DateUtility.getInstance().DateViewToDBCompare(simonAction.getDatepickerfrom());
            } catch (ServiceLocatorException serviceLocatorException) {
                LoggerUtility.log(logger, "ServiceLocatorException occurred in getDetails method:: " + serviceLocatorException.getMessage(), Level.ERROR, serviceLocatorException.getCause());
            }
            detailsQuery.append(" AND FILES.DATE_TIME_RECEIVED >= '" + tmp_Recieved_From + "'");
        }
        detailsQuery.append(" FETCH FIRST 50 ROWS ONLY");

        try {
            connection = ConnectionProvider.getInstance().getConnection();

            preparedStatement = connection.prepareStatement(detailsQuery.toString());
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                SimonBean simonBean = new SimonBean();
                if (resultSet.getString("APP_ID") != null && !"".equals(resultSet.getString("APP_ID"))) {
                    simonBean.setApplicationId(resultSet.getString("APP_ID"));
                } else {
                    simonBean.setApplicationId("--");
                }
                String direction = resultSet.getString("DIRECTION");
                if ((direction != null) && ("INBOUND".equalsIgnoreCase(direction))) {
                    simonBean.setPartnerName(resultSet.getString("SENDER_NAME"));
                } else {
                    simonBean.setPartnerName(resultSet.getString("RECEIVER_NAME"));
                }
                simonBean.setDirection(direction);
                simonBean.setTransctionType(resultSet.getString("TRANSACTION_TYPE"));
                simonBean.setDocumentType(resultSet.getString("FILE_TYPE"));
                simonBean.setSenderId(resultSet.getString("SENDER_ID"));
                simonBean.setRecId(resultSet.getString("RECEIVER_ID"));
                simonBean.setIsaControlNumber(resultSet.getString("ISA_NUMBER"));
                simonBean.setGsControlNumber(resultSet.getString("GS_NUMBER"));
                if ((resultSet.getString("TRANSACTION_TYPE").equals("850")) || (resultSet.getString("TRANSACTION_TYPE").equals("ORDRSP")) || (resultSet.getString("TRANSACTION_TYPE").equals("997") || (resultSet.getString("TRANSACTION_TYPE").equals("855")))) {
                    simonBean.setRefNumber("Po Number");
                    simonBean.setRefValue(resultSet.getString("PO_NUMBER"));
                } else if ((resultSet.getString("TRANSACTION_TYPE").equals("856")) || (resultSet.getString("TRANSACTION_TYPE").equals("DESADV"))) {
                    simonBean.setRefNumber("Shipment Id");
                    simonBean.setRefValue(resultSet.getString("ASN_NUMBER"));
                } else if ((resultSet.getString("TRANSACTION_TYPE").equals("810")) || (resultSet.getString("TRANSACTION_TYPE").equals("INVOIC"))) {
                    simonBean.setRefNumber("Invoice Number");
                    simonBean.setRefValue(resultSet.getString("INVOICE_NUMBER"));
                } else {
                    simonBean.setRefNumber("--");
                    simonBean.setRefValue("--");
                }

                simonBean.setStatus(resultSet.getString("STATUS"));
                simonBean.setCreatedDate(resultSet.getString("DATE_TIME_RECEIVED"));
                simonBean.setInstanceId(resultSet.getString("FILE_ID"));
                simonBean.setMapName(resultSet.getString("MAP"));
                simonBean.setFileName(resultSet.getString("FILENAME"));
                simonBean.setMailBoxName(resultSet.getString("MailBOX_NAME"));

                transactionSearchList.add(simonBean);

            }
        } catch (SQLException sqlException) {
            LoggerUtility.log(logger, "SQLException occurred in getDetails method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
        } catch (ServiceLocatorException serviceLocatorException) {
            LoggerUtility.log(logger, "ServiceLocatorException occurred in getDetails method:: " + serviceLocatorException.getMessage(), Level.ERROR, serviceLocatorException.getCause());
        }
        return transactionSearchList;
    }

    public ArrayList<SimonBean> buildPartnerQuery(SimonAction partnerAction) throws ServiceLocatorException {

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        ArrayList<SimonBean> partnerList = new ArrayList<SimonBean>();
        StringBuilder partnerSearchQuery = new StringBuilder();
        try {
            connection = ConnectionProvider.getInstance().getConnection();
            partnerSearchQuery.append("select tp.ID as PartnerId,tp.NAME as PartnerName,tp_details.INTERNALIDENTIFIER,tp_details.APPLICATIONID,tp_details.STATE,tp.STATUS,tp.MODIFIED_TS,tp.MODIFIED_BY,tp.CREATED_TS from tp LEFT OUTER JOIN tp_details on(tp_details.TP_ID=tp.ID) WHERE 1=1 AND tp.FLOW_FLAG='" + partnerAction.getFlowFlag() + "'");
            if (partnerAction.getPartnerIdentifier() != null && !"".equals(partnerAction.getPartnerIdentifier().trim())) {
                partnerSearchQuery.append(WildCardSql.getWildCardSql1("TP_DETAILS.TP_ID", partnerAction.getPartnerIdentifier().trim().toUpperCase()));
            }
            if (partnerAction.getPartnerName() != null && !"".equals(partnerAction.getPartnerName().trim())) {
                partnerSearchQuery.append(WildCardSql.getWildCardSql1("TP_DETAILS.TP_NAME", partnerAction.getPartnerName().trim().toUpperCase()));
            }
            if (partnerAction.getStatus() != null && !"".equals(partnerAction.getStatus().trim())) {
                partnerSearchQuery.append(WildCardSql.getWildCardSql1("TP.STATUS", partnerAction.getStatus().trim().toUpperCase()));
            }
            if (partnerAction.getInternalIdentifier() != null && !"".equals(partnerAction.getInternalIdentifier().trim())) {
                partnerSearchQuery.append(WildCardSql.getWildCardSql1("TP_DETAILS.INTERNALIDENTIFIER", partnerAction.getInternalIdentifier().trim().toUpperCase()));
            }
            if (partnerAction.getCountryCode() != null && !"".equals(partnerAction.getCountryCode().trim())) {
                partnerSearchQuery.append(WildCardSql.getWildCardSql1("TP_DETAILS.STATE", partnerAction.getCountryCode().trim().toUpperCase()));
            }
            if (partnerAction.getApplicationId() != null && !"".equals(partnerAction.getApplicationId().trim())) {
                partnerSearchQuery.append(WildCardSql.getWildCardSql1("TP_DETAILS.APPLICATIONID", partnerAction.getApplicationId().trim().toUpperCase()));
            }
            preparedStatement = connection.prepareStatement(partnerSearchQuery.toString());
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                SimonBean partnerBean = new SimonBean();
                partnerBean.setPartnerIdentifier(resultSet.getString("PartnerId"));
                partnerBean.setPartnerName(resultSet.getString("PartnerName"));
                partnerBean.setInternalIdentifier(resultSet.getString("INTERNALIDENTIFIER"));
                partnerBean.setApplicationId(resultSet.getString("APPLICATIONID"));
                partnerBean.setCountryCode(resultSet.getString("STATE"));
                partnerBean.setStatus(resultSet.getString("STATUS"));
                partnerBean.setCreatedDate(resultSet.getString("CREATED_TS"));
                partnerBean.setChangedBy(resultSet.getString("MODIFIED_BY"));
                partnerBean.setChangedDate(resultSet.getString("MODIFIED_TS"));
                partnerList.add(partnerBean);
            }
        } catch (SQLException sqlException) {
            responseString = "<font color='red'>Please try with different Id!</font>";
            LoggerUtility.log(logger, "SQLException occurred in buildPartnerQuery method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
        } catch (Exception exception) {
            responseString = "<font color='red'>Please try again!</font>";
            LoggerUtility.log(logger, "Exception occurred in buildPartnerQuery method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
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
                LoggerUtility.log(logger, "SQLException occurred in buildPartnerQuery method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
            }

        }
        return partnerList;
    }

    public List getProcessFlows(SimonAction simon) throws ServiceLocatorException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String processFlowsQuery = null;

        ArrayList flowList = new ArrayList();
        try {
            connection = ConnectionProvider.getInstance().getConnection();
            processFlowsQuery = "SELECT PROCESS_ID,DIRECTION,PROCESS_NAME,DESCRIPTION,TP_NAME,TP_ID,TP_SENDER_ID,TP_RECEIVER_ID,TRANSACTION_TYPE,SOURCE_MAIL_BOX,IS_ACTIVE,LOOKUP_ALIAS FROM PROCESS"
                    + " WHERE 1=1 ";
            if (simon.getDocTpName() != null && !"".equalsIgnoreCase(simon.getDocTpName())) {
                processFlowsQuery = processFlowsQuery + " AND lower(TP_NAME) LIKE lower('" + simon.getDocTpName() + "%') ";
            }
            if (simon.getDocTpId() != null && !"".equalsIgnoreCase(simon.getDocTpId())) {
                processFlowsQuery = processFlowsQuery + " AND lower(TP_ID) LIKE lower('" + simon.getDocTpId() + "%') ";
            }
            if (simon.getDocTpReceiverId() != null && !"".equalsIgnoreCase(simon.getDocTpReceiverId())) {
                processFlowsQuery = processFlowsQuery + " AND lower(TP_RECEIVER_ID) LIKE lower('" + simon.getDocTpReceiverId() + "%')";
            }
            if (simon.getDocTpSenderId() != null && !"".equalsIgnoreCase(simon.getDocTpSenderId())) {
                processFlowsQuery = processFlowsQuery + " AND lower(TP_SENDER_ID) LIKE lower('" + simon.getDocTpSenderId() + "%') ";
            }
            if (simon.getDocTransactionType() != null && !"".equalsIgnoreCase(simon.getDocTransactionType())) {
                processFlowsQuery = processFlowsQuery + " AND lower(TRANSACTION_TYPE) LIKE lower('" + simon.getDocTransactionType() + "%') ";
            }
            if (simon.getDocStatus() != null && !"".equalsIgnoreCase(simon.getDocStatus())) {
                processFlowsQuery = processFlowsQuery + " AND lower(IS_ACTIVE) LIKE lower('" + simon.getDocStatus() + "%') ";
            }
            preparedStatement = connection.prepareStatement(processFlowsQuery);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Map flowMap = new HashMap();
                flowMap.put("PROCESS_ID", resultSet.getString("PROCESS_ID"));
                flowMap.put("DIRECTION", resultSet.getString("DIRECTION"));
                flowMap.put("PROCESS_NAME", resultSet.getString("PROCESS_NAME"));
                flowMap.put("DESCRIPTION", resultSet.getString("DESCRIPTION"));
                flowMap.put("TP_NAME", resultSet.getString("TP_NAME"));
                flowMap.put("TP_ID", resultSet.getString("TP_ID"));
                flowMap.put("TP_SENDER_ID", resultSet.getString("TP_SENDER_ID"));
                flowMap.put("TP_RECEIVER_ID", resultSet.getString("TP_RECEIVER_ID"));
                flowMap.put("TRANSACTION_TYPE", resultSet.getString("TRANSACTION_TYPE"));
                flowMap.put("SOURCE_MAIL_BOX", resultSet.getString("SOURCE_MAIL_BOX"));
                flowMap.put("IS_ACTIVE", resultSet.getString("IS_ACTIVE"));
                flowMap.put("LOOKUP_ALIAS", resultSet.getString("LOOKUP_ALIAS"));
                flowList.add(flowMap);

            }

        } catch (SQLException sqlException) {
            LoggerUtility.log(logger, "SQLException occurred in getProcessFlows method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
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
            LoggerUtility.log(logger, "finally SQLException occurred in getProcessFlows method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
        }
        }
        return flowList;
    }

}
