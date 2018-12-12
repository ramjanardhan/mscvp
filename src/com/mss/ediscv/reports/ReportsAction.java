/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mss.ediscv.reports;

import com.mss.ediscv.util.AppConstants;
import com.mss.ediscv.util.DataSourceDataProvider;
import com.mss.ediscv.util.DateUtility;
import com.mss.ediscv.util.ServiceLocator;
import com.opensymphony.xwork2.ActionSupport;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts2.interceptor.ServletRequestAware;
import com.mss.ediscv.util.LoggerUtility;
import com.mss.ediscv.util.ServiceLocatorException;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author miracle
 */
public class ReportsAction extends ActionSupport implements ServletRequestAware {

    private static Logger logger = LogManager.getLogger(ReportsAction.class.getName());
    private HttpServletRequest httpServletRequest;
    private String resultType;
    private String formAction;
    private List correlationList;
    private List docTypeList;
    private String docdatepicker;
    private String docdatepickerfrom;
    private String docSenderId;
    private String docSenderName;
    private String docReceiverId;
    private String docReceiverName;
    private String docType;
    private String status;
    private String ackStatus;
    private List senderIdList;
    private List receiverIdList;
    private List senderNameList;
    private List receiverNameList;
    private String reportrange;
    private List<ReportsBean> documentList;
    private Map partnerMap;
    private String database;
    
    private Map senderIdMap;
    private Map receiverIdMap;

    public String getReports() throws Exception {
        setResultType(LOGIN);
        try {
            if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
                String defaultFlowId = httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_DEFAULT_FLOWID).toString();
                String defaultFlowName = DataSourceDataProvider.getInstance().getFlowNameByFlowID(defaultFlowId);
                if (!defaultFlowName.equals("Manufacturing")) {
                    defaultFlowId = DataSourceDataProvider.getInstance().getFlowIdByFlowName("Manufacturing");
                    httpServletRequest.getSession(false).setAttribute(AppConstants.SES_USER_DEFAULT_FLOWID, defaultFlowId);
                }
                setDocTypeList(DataSourceDataProvider.getInstance().getDocumentTypeList("M"));
                setSenderIdMap(DataSourceDataProvider.getInstance().getTradingPartnersMap());
                httpServletRequest.getSession(false).setAttribute(AppConstants.SENDERSID_MAP, getSenderIdMap());
                setReceiverIdMap(DataSourceDataProvider.getInstance().getTradingPartnersMap());
                httpServletRequest.getSession(false).setAttribute(AppConstants.RECEIVERSId_MAP, getReceiverIdMap());;
                setSenderNameList(DataSourceDataProvider.getInstance().getSenderNamelist("M"));
                setReceiverNameList(DataSourceDataProvider.getInstance().getReciverNamelist("M"));
                if ("ARCHIVE".equals(getDatabase())) {
                    setDatabase("ARCHIVE");
                } else {
                    setDatabase("MSCVP");
                }
                if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_DOCREPORT_LIST) != null) {
                    httpServletRequest.getSession(false).removeAttribute(AppConstants.SES_DOCREPORT_LIST);
                }
                resultType = SUCCESS;
                setResultType(SUCCESS);
            }
        } catch (Exception exception) {
            LoggerUtility.log(logger, "Exception occurred in getReports method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
        }
        return getResultType();
    }

    public String reportsSearch() throws ServiceLocatorException {
        System.out.println("reportsSearch start time::"+DateUtility.getInstance().getCurrentDB2Timestamp());
        setResultType(LOGIN);
        if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
            try {
                documentList = ServiceLocator.getReportsService().getDocumentList(this);
                if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_DOCREPORT_LIST) != null) {
                    httpServletRequest.getSession(false).removeAttribute(AppConstants.SES_DOCREPORT_LIST);
                }
                setDocTypeList(DataSourceDataProvider.getInstance().getDocumentTypeList("M"));
                setSenderIdList(DataSourceDataProvider.getInstance().getSenderIdlist("M"));
                setReceiverIdList(DataSourceDataProvider.getInstance().getReciverIdlist("M"));
                setSenderNameList(DataSourceDataProvider.getInstance().getSenderNamelist("M"));
                setReceiverNameList(DataSourceDataProvider.getInstance().getReciverNamelist("M"));
                if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_DOCREPORT_LIST) != null) {
                    httpServletRequest.getSession(false).removeAttribute(AppConstants.SES_DOCREPORT_LIST);
                }
                httpServletRequest.getSession(false).setAttribute(AppConstants.SES_DOCREPORT_LIST, documentList);
                resultType = SUCCESS;
                setResultType(SUCCESS);
            } catch (Exception exception) {
                LoggerUtility.log(logger, "Exception occurred in reportsSearch method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
                setResultType(ERROR);
            }
        }
         System.out.println("reportsSearch end time::"+DateUtility.getInstance().getCurrentDB2Timestamp());
        return getResultType();
    }

    public String getDashboard() throws ServiceLocatorException {
        System.out.println("getDashboard start time::"+DateUtility.getInstance().getCurrentDB2Timestamp());
        setResultType(LOGIN);
        if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
            try {
                String defaultFlowId = httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_DEFAULT_FLOWID).toString();
                String defaultFlowName = DataSourceDataProvider.getInstance().getFlowNameByFlowID(defaultFlowId);
                if (!defaultFlowName.equals("Manufacturing")) {
                    defaultFlowId = DataSourceDataProvider.getInstance().getFlowIdByFlowName("Manufacturing");
                    httpServletRequest.getSession(false).setAttribute(AppConstants.SES_USER_DEFAULT_FLOWID, defaultFlowId);
                }
                setDocTypeList(DataSourceDataProvider.getInstance().getDocumentTypeList("M"));
                setPartnerMap(DataSourceDataProvider.getInstance().getDashboardPartnerMap("2"));
                resultType = SUCCESS;
                setResultType(SUCCESS);
            } catch (Exception exception) {
                LoggerUtility.log(logger, "Exception occurred in getDashboard method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
                setResultType(ERROR);
            }
        }
        System.out.println("getDashboard end time::"+DateUtility.getInstance().getCurrentDB2Timestamp());
        return getResultType();
    }

    @Override
    public void setServletRequest(HttpServletRequest httpServletRequest) {
        this.httpServletRequest = httpServletRequest;
    }

    public String getResultType() {
        return resultType;
    }

    public void setResultType(String resultType) {
        this.resultType = resultType;
    }

    public String getFormAction() {
        return formAction;
    }

    public void setFormAction(String formAction) {
        this.formAction = formAction;
    }

    public List getCorrelationList() {
        return correlationList;
    }

    public void setCorrelationList(List correlationList) {
        this.correlationList = correlationList;
    }

    public List getDocTypeList() {
        return docTypeList;
    }

    public void setDocTypeList(List docTypeList) {
        this.docTypeList = docTypeList;
    }

    public String getDocSenderId() {
        return docSenderId;
    }

    public void setDocSenderId(String docSenderId) {
        this.docSenderId = docSenderId;
    }

    public String getDocSenderName() {
        return docSenderName;
    }

    public void setDocSenderName(String docSenderName) {
        this.docSenderName = docSenderName;
    }

    public String getDocReceiverId() {
        return docReceiverId;
    }

    public void setDocReceiverId(String docReceiverId) {
        this.docReceiverId = docReceiverId;
    }

    public String getDocReceiverName() {
        return docReceiverName;
    }

    public void setDocReceiverName(String docReceiverName) {
        this.docReceiverName = docReceiverName;
    }

    public String getDocType() {
        return docType;
    }

    public void setDocType(String docType) {
        this.docType = docType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAckStatus() {
        return ackStatus;
    }

    public void setAckStatus(String ackStatus) {
        this.ackStatus = ackStatus;
    }

    public Map getPartnerMap() {
        return partnerMap;
    }

    public void setPartnerMap(Map partnerMap) {
        this.partnerMap = partnerMap;
    }

    public String getDocdatepicker() {
        return docdatepicker;
    }

    public void setDocdatepicker(String docdatepicker) {
        this.docdatepicker = docdatepicker;
    }

    public String getDocdatepickerfrom() {
        return docdatepickerfrom;
    }

    public void setDocdatepickerfrom(String docdatepickerfrom) {
        this.docdatepickerfrom = docdatepickerfrom;
    }

    public List getReceiverIdList() {
        return receiverIdList;
    }

    public void setReceiverIdList(List receiverIdList) {
        this.receiverIdList = receiverIdList;
    }

    public List getReceiverNameList() {
        return receiverNameList;
    }

    public void setReceiverNameList(List receiverNameList) {
        this.receiverNameList = receiverNameList;
    }

    public List getSenderIdList() {
        return senderIdList;
    }

    public void setSenderIdList(List senderIdList) {
        this.senderIdList = senderIdList;
    }

    public List getSenderNameList() {
        return senderNameList;
    }

    public void setSenderNameList(List senderNameList) {
        this.senderNameList = senderNameList;
    }

    public String getReportrange() {
        return reportrange;
    }

    public void setReportrange(String reportrange) {
        this.reportrange = reportrange;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public Map getSenderIdMap() {
        return senderIdMap;
    }

    public void setSenderIdMap(Map senderIdMap) {
        this.senderIdMap = senderIdMap;
    }

    public Map getReceiverIdMap() {
        return receiverIdMap;
    }

    public void setReceiverIdMap(Map receiverIdMap) {
        this.receiverIdMap = receiverIdMap;
    }

}
