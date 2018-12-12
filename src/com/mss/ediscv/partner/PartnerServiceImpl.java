/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mss.ediscv.partner;

import com.mss.ediscv.util.ConnectionProvider;
import com.mss.ediscv.util.DateUtility;
import com.mss.ediscv.util.ServiceLocatorException;
import com.mss.ediscv.util.WildCardSql;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import com.mss.ediscv.util.LoggerUtility;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author miracle
 */
public class PartnerServiceImpl implements PartnerService {

    private static Logger logger = LogManager.getLogger(PartnerServiceImpl.class.getName());

    public String addPartner(PartnerAction partnerAction) throws ServiceLocatorException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        String responseString = null;
        try {
            connection = ConnectionProvider.getInstance().getConnection();
            preparedStatement = connection.prepareStatement("INSERT INTO TP(ID, NAME, FLOW_FLAG, CREATED_BY, CREATED_TS, STATUS) VALUES (?, ?, ?, ?, ?,?)");

            preparedStatement.setString(1, partnerAction.getPartnerIdentifier());
            preparedStatement.setString(2, partnerAction.getPartnerName());
            preparedStatement.setString(3, partnerAction.getFlowFlag());
            preparedStatement.setString(4, partnerAction.getCreatedBy());
            preparedStatement.setTimestamp(5, DateUtility.getInstance().getCurrentDB2Timestamp());
            preparedStatement.setString(6, partnerAction.getStatus());
            
            System.out.println("partnerIdentifier is"+ partnerAction.getPartnerIdentifier());
            System.out.println("partnerName is"+partnerAction.getPartnerName());
            System.out.println("flowflag is"+partnerAction.getFlowFlag());
            System.out.println("created by"+partnerAction.getCreatedBy());
            System.out.println("time is"+DateUtility.getInstance().getCurrentDB2Timestamp());
            System.out.println("status is"+partnerAction.getStatus());
            
            preparedStatement.executeUpdate();
            preparedStatement.close();
            preparedStatement = null;
            preparedStatement = connection.prepareStatement("INSERT INTO TP_DETAILS(TP_ID,TP_NAME,INTERNALIDENTIFIER,APPLICATIONID,STATE) VALUES(?,?,?,?,?)");
            preparedStatement.setString(1, partnerAction.getPartnerIdentifier());
            preparedStatement.setString(2, partnerAction.getPartnerName());
            preparedStatement.setString(3, partnerAction.getInternalIdentifier());
            preparedStatement.setString(4, partnerAction.getApplicationId());
            preparedStatement.setString(5, partnerAction.getCountryCode());
            int i = preparedStatement.executeUpdate();
            if (i > 0) {
                responseString = "<font color='green'>Partner added succesfully.</font>";
            } else {
                responseString = "<font color='red'>Please try again!</font>";
            }
        } catch (SQLException sqlException) {
            responseString = "<font color='red'>Please try with different Id!</font>";
            LoggerUtility.log(logger, "SQLException occurred in PartnerServiceImpl method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
        } catch (Exception exception) {
            responseString = "<font color='red'>Please try again!</font>";
            LoggerUtility.log(logger, "Exception occurred in PartnerServiceImpl method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
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
                LoggerUtility.log(logger, "SQLException occurred in PartnerServiceImpl method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
            }

        }
        return responseString;
    }

    public ArrayList<PartnerBean> buildPartnerQuery(PartnerAction partnerAction) throws ServiceLocatorException {

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String responseString = null;
        ArrayList<PartnerBean> partnerList = new ArrayList<PartnerBean>();
        StringBuilder partnerSearchQuery = new StringBuilder();
        try {
            connection = ConnectionProvider.getInstance().getConnection();
            System.out.println("flowflag is"+partnerAction.getFlowFlag());
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
             System.out.println("buildPartnerQuery query:"+partnerSearchQuery.toString());
            preparedStatement = connection.prepareStatement(partnerSearchQuery.toString());
            resultSet = preparedStatement.executeQuery();
            System.out.println("Query and resultset start time::"+DateUtility.getInstance().getCurrentDB2Timestamp());
            while (resultSet.next()) {
                PartnerBean partnerBean = new PartnerBean();
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
            System.out.println("Resultset end time::"+DateUtility.getInstance().getCurrentDB2Timestamp());
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

    public PartnerAction partnerEdit(PartnerAction partnerAction) throws ServiceLocatorException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String responseString = null;
        StringBuilder partnerSearchQuery = new StringBuilder();
        try {
            connection = ConnectionProvider.getInstance().getConnection();
            System.out.println("partner Identifier is"+partnerAction.getPartnerIdentifier());
            partnerSearchQuery.append("select tp.ID as PartnerId,tp.NAME as PartnerName,tp_details.INTERNALIDENTIFIER,tp_details.APPLICATIONID,tp_details.STATE,tp.STATUS,tp.MODIFIED_TS,tp.MODIFIED_BY,tp.CREATED_TS from tp LEFT OUTER JOIN tp_details on(tp_details.TP_ID=tp.ID) WHERE 1=1 AND tp.ID='" + partnerAction.getPartnerIdentifier() + "'");
            preparedStatement = connection.prepareStatement(partnerSearchQuery.toString());
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                partnerAction.setPartnerIdentifier(resultSet.getString("PartnerId"));
                partnerAction.setPartnerName(resultSet.getString("PartnerName"));
                partnerAction.setInternalIdentifier(resultSet.getString("INTERNALIDENTIFIER"));
                partnerAction.setApplicationId(resultSet.getString("APPLICATIONID"));
                partnerAction.setCountryCode(resultSet.getString("STATE"));
                partnerAction.setStatus(resultSet.getString("STATUS"));
            }
        } catch (SQLException sqlException) {
            responseString = "<font color='red'>Please try with different Id!</font>";
            LoggerUtility.log(logger, "SQLException occurred in partnerEdit method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
        } catch (Exception exception) {
            responseString = "<font color='red'>Please try again!</font>";
            LoggerUtility.log(logger, "Exception occurred in partnerEdit method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
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
                LoggerUtility.log(logger, "SQLException occurred in partnerEdit method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
            }
        }
        return partnerAction;
    }

    public String editPartner(PartnerAction partnerAction) throws ServiceLocatorException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        String responseString = null;
        try {
            connection = ConnectionProvider.getInstance().getConnection();
            preparedStatement = connection.prepareStatement("UPDATE TP SET NAME=?,MODIFIED_BY=?,MODIFIED_TS=?, STATUS=? WHERE ID=?");

            preparedStatement.setString(1, partnerAction.getPartnerName());
            preparedStatement.setString(2, partnerAction.getCreatedBy());
            preparedStatement.setTimestamp(3, DateUtility.getInstance().getCurrentDB2Timestamp());
            preparedStatement.setString(4, partnerAction.getStatus());
            preparedStatement.setString(5, partnerAction.getPartnerIdentifier());
            preparedStatement.executeUpdate();
            preparedStatement.close();
            preparedStatement = null;

            preparedStatement = connection.prepareStatement("UPDATE TP_DETAILS SET TP_NAME=?,INTERNALIDENTIFIER=?,APPLICATIONID=?,STATE=? WHERE TP_ID =?");

            preparedStatement.setString(1, partnerAction.getPartnerName());
            preparedStatement.setString(2, partnerAction.getInternalIdentifier());
            preparedStatement.setString(3, partnerAction.getApplicationId());
            preparedStatement.setString(4, partnerAction.getCountryCode());
            preparedStatement.setString(5, partnerAction.getPartnerIdentifier());

            int i = preparedStatement.executeUpdate();
            if (i > 0) {
                responseString = "<font color='green'>Partner updated succesfully.</font>";
            } else {
                responseString = "<font color='red'>Please try again!</font>";
            }
        } catch (SQLException sqlException) {
            responseString = "<font color='red'>Please try with different Id!</font>";
            LoggerUtility.log(logger, "SQLException occurred in editPartner method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
        } catch (Exception exception) {
            responseString = "<font color='red'>Please try again!</font>";
            LoggerUtility.log(logger, "Exception occurred in editPartner method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
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
                LoggerUtility.log(logger, "SQLException occurred in editPartner method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
            }
        }
        return responseString;
    }

    public String addDeliveryChannelInfo(PartnerAction partnerAction) throws ServiceLocatorException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        String responseString = null;
        try {
            connection = ConnectionProvider.getInstance().getConnection();
            preparedStatement = connection.prepareStatement("INSERT INTO DELIVERYCHNNELINFO(PARTNER_ID, ROUTING_ID, SEQUENCE, BUSINESSPROCESSNAME, TRANSLATIONMAP, DOCEXTRACTMAP, ARCHIVEFLAG, ARCHIVEDIRCTORY, OUTPUTFILENAME, OUTPUTFORMAT, PRODUCERMAILBOX,STATUS,ENCODING) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)");
            preparedStatement.setString(1, partnerAction.getPartnerId());
            preparedStatement.setInt(2, Integer.parseInt(partnerAction.getRouterId()));
            preparedStatement.setInt(3, partnerAction.getSequence());
            preparedStatement.setString(4, partnerAction.getBusinessProcessId());
            preparedStatement.setInt(5, partnerAction.getTranslationId());
            preparedStatement.setInt(6, partnerAction.getDocumentExtarctId());
            preparedStatement.setInt(7, partnerAction.getArchiveFlag());
            preparedStatement.setString(8, partnerAction.getArchiveDirectory());
            preparedStatement.setString(9, partnerAction.getOutputFileName());
            preparedStatement.setString(10, partnerAction.getOutputFormat());
            preparedStatement.setInt(11, partnerAction.getProducerMailBoxId());
            preparedStatement.setString(12, partnerAction.getStatus());
            preparedStatement.setString(13, partnerAction.getEncodingMailBoxId());

            int i = preparedStatement.executeUpdate();
            if (i > 0) {
                responseString = "<font color='green'>Delivery channel information added succesfully.</font>";
            } else {
                responseString = "<font color='red'>Please try again!</font>";
            }
        }  catch (SQLException sqlException) {
            responseString = "<font color='red'>Exception:" + sqlException.getMessage() + "</font>";
            LoggerUtility.log(logger, "SQLException occurred in addDeliveryChannelInfo method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
        } catch (Exception exception) {
            responseString = "<font color='red'>Please try again!</font>";
            LoggerUtility.log(logger, "Exception occurred in addDeliveryChannelInfo method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
        }finally {
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
                LoggerUtility.log(logger, "SQLException occurred in addDeliveryChannelInfo method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
            }
        }
        return responseString;
    }

    public ArrayList<PartnerBean> buildDeliverChannelQuery(PartnerAction partnerAction) throws ServiceLocatorException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String responseString = null;
        ArrayList<PartnerBean> partnerList = new ArrayList<PartnerBean>();
        StringBuilder deliverChannelSearchQuery = new StringBuilder();
        try {
            connection = ConnectionProvider.getInstance().getConnection();
            deliverChannelSearchQuery.append("select DELIVERYCHNNELINFO.DELIVERYCHN_ID,DELIVERYCHNNELINFO.PARTNER_ID as PartnerId,TP.NAME as PartnerName,DELIVERYCHNNELINFO.ROUTING_ID as routingId,ROUTERINFO.ROUTER_NAME as routingName,bp.REL_ID as bpId,bp.RELNAME as bpName,trans.REL_ID as transId,trans.RELNAME as transName,dem.REL_ID as demId,dem.RELNAME as demName,pmb.REL_ID as pmbId,pmb.RELNAME as pmbName,DELEVERYCHANNELDESCRPTION.VALUE as encodingId,DELEVERYCHANNELDESCRPTION.DESCRIPTION as encodingName,SEQUENCE,ARCHIVEFLAG,ARCHIVEDIRCTORY,OUTPUTFILENAME,OUTPUTFORMAT,DELIVERYCHNNELINFO.STATUS  from (((((((DELIVERYCHNNELINFO  JOIN TP on (TP.ID=DELIVERYCHNNELINFO.PARTNER_ID) ) JOIN ROUTERINFO on (ROUTERINFO.ROUTER_ID=DELIVERYCHNNELINFO.ROUTING_ID)) ");
            deliverChannelSearchQuery.append(" JOIN PROCESSRELATEDINFO bp on (bp.REL_ID=DELIVERYCHNNELINFO.BUSINESSPROCESSNAME))");
            deliverChannelSearchQuery.append(" JOIN PROCESSRELATEDINFO trans on (trans.REL_ID=DELIVERYCHNNELINFO.TRANSLATIONMAP))");
            deliverChannelSearchQuery.append(" JOIN PROCESSRELATEDINFO dem on (dem.REL_ID=DELIVERYCHNNELINFO.DOCEXTRACTMAP))");
            deliverChannelSearchQuery.append(" JOIN PROCESSRELATEDINFO pmb on (pmb.REL_ID=DELIVERYCHNNELINFO.PRODUCERMAILBOX))");
            deliverChannelSearchQuery.append(" JOIN DELEVERYCHANNELDESCRPTION on (DELEVERYCHANNELDESCRPTION.VALUE=DELIVERYCHNNELINFO.ENCODING)) WHERE 1=1 ");

            preparedStatement = connection.prepareStatement(deliverChannelSearchQuery.toString());

            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                PartnerBean partnerBean = new PartnerBean();
                partnerBean.setDeliveryChannelId(resultSet.getInt("DELIVERYCHN_ID"));
                partnerBean.setPartnerId(resultSet.getString("PartnerId"));
                partnerBean.setPartnerName(resultSet.getString("PartnerName"));
                partnerBean.setRouterId(resultSet.getString("routingId"));
                partnerBean.setRoutingName(resultSet.getString("routingName"));
                partnerBean.setSequence(resultSet.getInt("SEQUENCE"));
                partnerBean.setBusinessProcessId(resultSet.getString("bpId"));
                partnerBean.setBusinessProcessName(resultSet.getString("bpName"));
                partnerBean.setTranslationId(resultSet.getInt("transId"));
                partnerBean.setTranslationMapName(resultSet.getString("transName"));
                partnerBean.setDocumentExtarctId(resultSet.getInt("demId"));
                partnerBean.setDocExtractMapName(resultSet.getString("demName"));
                partnerBean.setArchiveFlag(resultSet.getInt("ARCHIVEFLAG"));
                partnerBean.setArchiveDirectory(resultSet.getString("ARCHIVEDIRCTORY"));
                partnerBean.setOutputFileName(resultSet.getString("OUTPUTFILENAME"));
                partnerBean.setOutputFormat(resultSet.getString("OUTPUTFORMAT"));
                partnerBean.setProducerMailBoxId(resultSet.getInt("pmbId"));
                partnerBean.setProducerMailBox(resultSet.getString("pmbName"));
                partnerBean.setStatus(resultSet.getString("STATUS"));
                partnerBean.setEncodingMailBoxId(resultSet.getString("encodingId"));
                partnerBean.setEncodingMailBoxName(resultSet.getString("encodingName"));
                partnerList.add(partnerBean);
            }
        }catch (SQLException sqlException) {
            responseString = "<font color='red'>Please try with different Id!</font>";
            LoggerUtility.log(logger, "SQLException occurred in buildDeliverChannelQuery method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
        } catch (Exception exception) {
            responseString = "<font color='red'>Please try again!</font>";
            LoggerUtility.log(logger, "Exception occurred in buildDeliverChannelQuery method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
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
                LoggerUtility.log(logger, "SQLException occurred in buildDeliverChannelQuery method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
            }
        }
        return partnerList;
    }

    public PartnerAction deliveryChannelEdit(PartnerAction partnerAction) throws ServiceLocatorException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String responseString = null;
        StringBuilder deliverChannelSearchQuery = new StringBuilder();
        try {
            connection = ConnectionProvider.getInstance().getConnection();
            deliverChannelSearchQuery.append("select DELIVERYCHNNELINFO.DELIVERYCHN_ID,DELIVERYCHNNELINFO.PARTNER_ID as PartnerId,TP.NAME as PartnerName,DELIVERYCHNNELINFO.ROUTING_ID as routingId,ROUTERINFO.ROUTER_NAME as routingName,bp.REL_ID as bpId,bp.RELNAME as bpName,trans.REL_ID as transId,trans.RELNAME as transName,dem.REL_ID as demId,dem.RELNAME as demName,pmb.REL_ID as pmbId,pmb.RELNAME as pmbName,DELEVERYCHANNELDESCRPTION.VALUE as encodingId,DELEVERYCHANNELDESCRPTION.DESCRIPTION as encodingName,SEQUENCE,ARCHIVEFLAG,ARCHIVEDIRCTORY,OUTPUTFILENAME,OUTPUTFORMAT,DELIVERYCHNNELINFO.STATUS   from (((((((DELIVERYCHNNELINFO  JOIN TP on (TP.ID=DELIVERYCHNNELINFO.PARTNER_ID) ) JOIN ROUTERINFO on (ROUTERINFO.ROUTER_ID=DELIVERYCHNNELINFO.ROUTING_ID)) ");
            deliverChannelSearchQuery.append(" JOIN PROCESSRELATEDINFO bp on (bp.REL_ID=DELIVERYCHNNELINFO.BUSINESSPROCESSNAME))");
            deliverChannelSearchQuery.append(" JOIN PROCESSRELATEDINFO trans on (trans.REL_ID=DELIVERYCHNNELINFO.TRANSLATIONMAP))");
            deliverChannelSearchQuery.append(" JOIN PROCESSRELATEDINFO dem on (dem.REL_ID=DELIVERYCHNNELINFO.DOCEXTRACTMAP))");
            deliverChannelSearchQuery.append(" JOIN PROCESSRELATEDINFO pmb on (pmb.REL_ID=DELIVERYCHNNELINFO.PRODUCERMAILBOX))");
            deliverChannelSearchQuery.append(" JOIN DELEVERYCHANNELDESCRPTION on (DELEVERYCHANNELDESCRPTION.VALUE=DELIVERYCHNNELINFO.ENCODING)) WHERE DELIVERYCHN_ID=" + partnerAction.getDeliveryChannelId());

            preparedStatement = connection.prepareStatement(deliverChannelSearchQuery.toString());
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                partnerAction.setDeliveryChannelId(resultSet.getInt("DELIVERYCHN_ID"));
                partnerAction.setPartnerName(resultSet.getString("PartnerName"));
                partnerAction.setPartnerId(resultSet.getString("PartnerId"));
                partnerAction.setRoutingName(resultSet.getString("routingName"));
                partnerAction.setRouterId(resultSet.getString("routingId"));
                partnerAction.setSequence(resultSet.getInt("SEQUENCE"));
                partnerAction.setBusinessProcessName(resultSet.getString("bpName"));
                partnerAction.setBusinessProcessId(resultSet.getString("bpId"));
                partnerAction.setTranslationMapName(resultSet.getString("transName"));
                partnerAction.setTranslationId(resultSet.getInt("transId"));
                partnerAction.setDocExtractMapName(resultSet.getString("demName"));
                partnerAction.setDocumentExtarctId(resultSet.getInt("demId"));
                partnerAction.setArchiveFlag(resultSet.getInt("ARCHIVEFLAG"));
                partnerAction.setArchiveDirectory(resultSet.getString("ARCHIVEDIRCTORY"));
                partnerAction.setOutputFileName(resultSet.getString("OUTPUTFILENAME"));
                partnerAction.setOutputFormat(resultSet.getString("OUTPUTFORMAT"));
                partnerAction.setProducerMailBoxId(resultSet.getInt("pmbId"));
                partnerAction.setProducerMailBox(resultSet.getString("pmbName"));
                partnerAction.setStatus(resultSet.getString("STATUS"));
                partnerAction.setEncodingMailBoxId(resultSet.getString("encodingId"));
                partnerAction.setEncodingMailBoxName(resultSet.getString("encodingName"));
            }
        } catch (SQLException sqlException) {
            responseString = "<font color='red'>Please try with different Id!</font>";
            LoggerUtility.log(logger, "SQLException occurred in deliveryChannelEdit method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
        } catch (Exception exception) {
            responseString = "<font color='red'>Please try again!</font>";
            LoggerUtility.log(logger, "Exception occurred in deliveryChannelEdit method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
        }  finally {
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
                LoggerUtility.log(logger, "SQLException occurred in deliveryChannelEdit method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
            }
        }
        return partnerAction;
    }

    public String editDeliveryChannel(PartnerAction partnerAction) throws ServiceLocatorException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        String responseString = null;
        try {
            connection = ConnectionProvider.getInstance().getConnection();
            preparedStatement = connection.prepareStatement("UPDATE DELIVERYCHNNELINFO SET PARTNER_ID =?, ROUTING_ID = ?, SEQUENCE=?, BUSINESSPROCESSNAME=?, TRANSLATIONMAP=?, DOCEXTRACTMAP=?, ARCHIVEFLAG=?, ARCHIVEDIRCTORY=?, OUTPUTFILENAME=?, OUTPUTFORMAT=?, PRODUCERMAILBOX=?,STATUS=?,ENCODING=? WHERE DELIVERYCHN_ID=?");
            preparedStatement.setString(1, partnerAction.getPartnerId());
            preparedStatement.setInt(2, Integer.parseInt(partnerAction.getRouterId()));
            preparedStatement.setInt(3, partnerAction.getSequence());
            preparedStatement.setString(4, partnerAction.getBusinessProcessId());
            preparedStatement.setInt(5, partnerAction.getTranslationId());
            preparedStatement.setInt(6, partnerAction.getDocumentExtarctId());
            preparedStatement.setInt(7, partnerAction.getArchiveFlag());
            preparedStatement.setString(8, partnerAction.getArchiveDirectory());
            preparedStatement.setString(9, partnerAction.getOutputFileName());
            preparedStatement.setString(10, partnerAction.getOutputFormat());
            preparedStatement.setInt(11, partnerAction.getProducerMailBoxId());
            preparedStatement.setString(12, partnerAction.getStatus());
            preparedStatement.setString(13, partnerAction.getEncodingMailBoxId());
            preparedStatement.setInt(14, partnerAction.getDeliveryChannelId());
            int i = preparedStatement.executeUpdate();
            if (i > 0) {
                responseString = "<font color='green'>Delivery channel information updated succesfully.</font>";
            } else {
                responseString = "<font color='red'>Please try again!</font>";
            }
        } catch (SQLException sqlException) {
            responseString = "<font color='red'>Exception:" + sqlException.getMessage() + "</font>";
            LoggerUtility.log(logger, "SQLException occurred in editDeliveryChannel method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
        } catch (Exception exception) {
            responseString = "<font color='red'>Please try again!</font>";
            LoggerUtility.log(logger, "Exception occurred in editDeliveryChannel method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
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
                LoggerUtility.log(logger, "SQLException occurred in editDeliveryChannel method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
            }
        }
        return responseString;
    }
}
