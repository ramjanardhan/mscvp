/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mss.ediscv.logisticreports;

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
public class LogisticReportsAction extends ActionSupport implements ServletRequestAware {

    private static Logger logger = LogManager.getLogger(LogisticReportsAction.class.getName());
    private HttpServletRequest httpServletRequest;
    private String resultType;
    private String formAction;
    private List correlationList;
    private List docTypeList;
    private String docdatepicker;
    private String docdatepickerfrom;
    private String docSenderId;
    private String docSenderName;
    private String docType;
    private List senderIdList;
    private List receiverIdList;
    private List senderNameList;
    private List receiverNameList;
    private String status;
    private String ackStatus;
    private String docBusId;
    private String docRecName;
    private String check;
    private List<LogisticReportsBean> documentList;
    private Map partnerMap;
    private String reportrange;
    private String database;
    
    
    private Map senderIdMap;
    private Map receiverIdMap;

    public String getLogisticReports() throws Exception {
        setResultType(LOGIN);
        try {
            if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
                String defaultFlowId = httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_DEFAULT_FLOWID).toString();
                String defaultFlowName = DataSourceDataProvider.getInstance().getFlowNameByFlowID(defaultFlowId);
                httpServletRequest.getSession(false).removeAttribute(AppConstants.SES_LOG_DOC_LIST);
                httpServletRequest.getSession(false).removeAttribute(AppConstants.SES_LOGSHIPMENT_LIST);
                httpServletRequest.getSession(false).removeAttribute(AppConstants.SES_LOAD_LIST);
                httpServletRequest.getSession(false).removeAttribute(AppConstants.SES_LTRESPONSE_LIST);
                setSenderIdMap(DataSourceDataProvider.getInstance().getTradingPartnersMap());
                    httpServletRequest.getSession(false).setAttribute(AppConstants.SENDERSID_MAP, getSenderIdMap());
                    setReceiverIdMap(DataSourceDataProvider.getInstance().getTradingPartnersMap());
                    httpServletRequest.getSession(false).setAttribute(AppConstants.RECEIVERSId_MAP, getReceiverIdMap());
                setSenderNameList(DataSourceDataProvider.getInstance().getSenderNamelist("L"));
                setReceiverNameList(DataSourceDataProvider.getInstance().getReciverNamelist("L"));
                setDocdatepicker(DateUtility.getInstance().getCurrentMySqlDateTime1());
                setCorrelationList(DataSourceDataProvider.getInstance().getCorrelationNames(0, 2));
                setDocTypeList(DataSourceDataProvider.getInstance().getDocumentTypeList(0, 2));
                if (!defaultFlowName.equals("Logistics")) {
                    defaultFlowId = DataSourceDataProvider.getInstance().getFlowIdByFlowName("Logistics");
                    httpServletRequest.getSession(false).setAttribute(AppConstants.SES_USER_DEFAULT_FLOWID, defaultFlowId);
                }
                if ("ARCHIVE".equals(getDatabase())) {
                    setDatabase("ARCHIVE");
                } else {
                    setDatabase("MSCVP");
                }
                setResultType(SUCCESS);
            }
        } catch (Exception exception) {
            LoggerUtility.log(logger, "Exception occurred in getLogisticReports method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
        }
        return getResultType();
    }

    public String logisticreportsSearch() {
        setResultType(LOGIN);
        if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
            try {
                getLogisticReports();
                if (getCheck() == null) {
                    setCheck("1");
                } else if (getCheck().equals("")) {
                    setCheck("1");
                }
                documentList = ServiceLocator.getLogisticReportsService().getDocumentList(this);

                httpServletRequest.getSession(false).setAttribute(AppConstants.SES_LOG_DOC_LIST, documentList);
                setResultType(SUCCESS);

            } catch (Exception exception) {
                LoggerUtility.log(logger, "Exception occurred in logisticreportsSearch method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
                setResultType("error");
            }
        }
        return getResultType();
    }

    public String getDashboard() throws ServiceLocatorException {
        System.out.println("getDashboard start time::"+DateUtility.getInstance().getCurrentDB2Timestamp());
        setResultType(LOGIN);
        if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
            try {
                String defaultFlowId = httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_DEFAULT_FLOWID).toString();
                String defaultFlowName = DataSourceDataProvider.getInstance().getFlowNameByFlowID(defaultFlowId);
                if (!defaultFlowName.equals("Logistics")) {
                    defaultFlowId = DataSourceDataProvider.getInstance().getFlowIdByFlowName("Logistics");
                    httpServletRequest.getSession(false).setAttribute(AppConstants.SES_USER_DEFAULT_FLOWID, defaultFlowId);
                }
                setDocTypeList(DataSourceDataProvider.getInstance().getDocumentTypeList(0, 2));
                setPartnerMap(DataSourceDataProvider.getInstance().getDashboardPartnerMap("3"));
                setDocdatepicker(DateUtility.getInstance().getCurrentMySqlDateTime1());
                resultType = SUCCESS;
                setResultType(SUCCESS);
            } catch (Exception exception) {
                LoggerUtility.log(logger, "Exception occurred in getDashboard method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
                httpServletRequest.getSession(false).setAttribute(AppConstants.REQ_EXCEPTION_MSG, exception.getMessage());
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

    public String getDocBusId() {
        return docBusId;
    }

    public void setDocBusId(String docBusId) {
        this.docBusId = docBusId;
    }

    public String getDocRecName() {
        return docRecName;
    }

    public void setDocRecName(String docRecName) {
        this.docRecName = docRecName;
    }

    public String getCheck() {
        return check;
    }

    public void setCheck(String check) {
        this.check = check;
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

    public List getSenderIdList() {
        return senderIdList;
    }

    public void setSenderIdList(List senderIdList) {
        this.senderIdList = senderIdList;
    }

    public List getReceiverIdList() {
        return receiverIdList;
    }

    public void setReceiverIdList(List receiverIdList) {
        this.receiverIdList = receiverIdList;
    }

    public List getSenderNameList() {
        return senderNameList;
    }

    public void setSenderNameList(List senderNameList) {
        this.senderNameList = senderNameList;
    }

    public List getReceiverNameList() {
        return receiverNameList;
    }

    public void setReceiverNameList(List receiverNameList) {
        this.receiverNameList = receiverNameList;
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
