/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mss.ediscv.documentVisibility;

import com.mss.ediscv.util.ConnectionProvider;
import com.mss.ediscv.util.DateUtility;
import com.mss.ediscv.util.ServiceLocatorException;
import com.mss.ediscv.util.WildCardSql;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import com.mss.ediscv.util.LoggerUtility;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DocumentVisibilityServiceImpl implements DocumentVisibilityService {

    private static Logger logger = LogManager.getLogger(DocumentVisibilityServiceImpl.class.getName());
    String tmp_Recieved_From = "";
    String tmp_Recieved_ToTime = "";
    private ArrayList<DocumentVisibilityBean> documentList;

    public ArrayList<DocumentVisibilityBean> buildDocumentQuery(DocumentVisibilityAction documentVisibilityAction, HttpServletRequest httpServletRequest) throws ServiceLocatorException {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        StringBuilder documentSearchQuery = new StringBuilder();
        try {
            documentSearchQuery.append("SELECT ID,FILE_ID,FILE_TYPE,TRAN_MESS_TYPE,SENDER_ID,RECEIVER_ID,INTERCHANGE_CONTROLNO,FUNCTIONAL_CONTROLNO,MESSAGE_CONTROLNO,DATE_TIME_RECEIVED,REPROCESSSTATUS,STATUS,ACK_STATUS FROM DOCUMENT_TRACKING WHERE 1=1 ");
            if (documentVisibilityAction.getDocdatepicker() != null && !"".equals(documentVisibilityAction.getDocdatepicker())) {
                tmp_Recieved_From = DateUtility.getInstance().DateViewToDBCompare(documentVisibilityAction.getDocdatepicker());
                documentSearchQuery.append(" AND DATE_TIME_RECEIVED <= '" + tmp_Recieved_From + "'");
            }
            if (documentVisibilityAction.getDocdatepickerfrom() != null && !"".equals(documentVisibilityAction.getDocdatepickerfrom())) {
                tmp_Recieved_From = DateUtility.getInstance().DateViewToDBCompare(documentVisibilityAction.getDocdatepickerfrom());
                documentSearchQuery.append(" AND DATE_TIME_RECEIVED >= '" + tmp_Recieved_From + "'");
            }
            if (documentVisibilityAction.getDocSenderId() != null && !"".equals(documentVisibilityAction.getDocSenderId().trim())) {
                documentSearchQuery.append(WildCardSql.getWildCardSql1("SENDER_ID", documentVisibilityAction.getDocSenderId().trim().toUpperCase()));
            }
            if (documentVisibilityAction.getDocReceiverId() != null && !"".equals(documentVisibilityAction.getDocReceiverId().trim())) {
                documentSearchQuery.append(WildCardSql.getWildCardSql1("RECEIVER_ID", documentVisibilityAction.getDocReceiverId().trim().toUpperCase()));
            }
            if (documentVisibilityAction.getStatus() != null && !"-1".equals(documentVisibilityAction.getStatus().trim())) {
                documentSearchQuery.append(WildCardSql.getWildCardSql1("STATUS", documentVisibilityAction.getStatus().trim()));
            }
            if (documentVisibilityAction.getAckStatus() != null && !"-1".equals(documentVisibilityAction.getAckStatus().trim())) {
                documentSearchQuery.append(WildCardSql.getWildCardSql1("ACK_STATUS", documentVisibilityAction.getAckStatus().trim()));
            }
            System.out.println("documentSearchQuery query:" + documentSearchQuery.toString());
            connection = ConnectionProvider.getInstance().getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(documentSearchQuery.toString());
            documentList = new ArrayList<DocumentVisibilityBean>();
           System.out.println("Query and resultset start time::"+DateUtility.getInstance().getCurrentDB2Timestamp());
            while (resultSet.next()) {
                DocumentVisibilityBean documentVisibilityBean = new DocumentVisibilityBean();
                documentVisibilityBean.setId(resultSet.getInt("ID"));
                documentVisibilityBean.setInstanceId(resultSet.getString("FILE_ID"));
                if (resultSet.getString("INTERCHANGE_CONTROLNO") != null && !"".equals(resultSet.getString("INTERCHANGE_CONTROLNO"))) {
                    documentVisibilityBean.setInterchange_ControlNo(resultSet.getString("INTERCHANGE_CONTROLNO"));
                } else {
                    documentVisibilityBean.setInterchange_ControlNo("--");
                }
                documentVisibilityBean.setTransaction_type(resultSet.getString("TRAN_MESS_TYPE"));
                documentVisibilityBean.setDate_time_rec(resultSet.getTimestamp("DATE_TIME_RECEIVED"));
                documentVisibilityBean.setSenderId(resultSet.getString("SENDER_ID"));
                documentVisibilityBean.setFile_type(resultSet.getString("FILE_TYPE"));
                documentVisibilityBean.setReceiverId(resultSet.getString("RECEIVER_ID"));
                documentVisibilityBean.setReProcessStatus(resultSet.getString("REPROCESSSTATUS"));
                documentVisibilityBean.setStatus(resultSet.getString("STATUS"));
                if (resultSet.getString("FUNCTIONAL_CONTROLNO") != null && !"".equals(resultSet.getString("FUNCTIONAL_CONTROLNO"))) {
                    documentVisibilityBean.setFunctional_ControlNo(resultSet.getString("FUNCTIONAL_CONTROLNO"));
                } else {
                    documentVisibilityBean.setFunctional_ControlNo("--");
                }
                if (resultSet.getString("MESSAGE_CONTROLNO") != null && !"".equals(resultSet.getString("MESSAGE_CONTROLNO"))) {
                    documentVisibilityBean.setMessage_ControlNo(resultSet.getString("MESSAGE_CONTROLNO"));
                } else {
                    documentVisibilityBean.setMessage_ControlNo("--");
                }
                documentList.add(documentVisibilityBean);
            }
            System.out.println("Resultset end time::"+DateUtility.getInstance().getCurrentDB2Timestamp());
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
                LoggerUtility.log(logger, "SQLException occurred in buildDocumentQuery method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
            }
        }
        return documentList;
    }
}
