/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mss.ediscv.ltResponse;

import com.mss.ediscv.util.AppConstants;
import com.mss.ediscv.util.AuthorizationManager;
import com.mss.ediscv.util.DataSourceDataProvider;
import com.mss.ediscv.util.DateUtility;
import com.mss.ediscv.util.ServiceLocator;
import com.opensymphony.xwork2.ActionSupport;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import com.mss.ediscv.util.LoggerUtility;
import java.util.Map;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.interceptor.ServletRequestAware;

/**
 * @author miracle
 */
public class LtResponse extends ActionSupport implements ServletRequestAware {

    private static Logger logger = LogManager.getLogger(LtResponse.class.getName());
    private HttpServletRequest httpServletRequest;
    private String resultType;
    private String sqlQuery;
    private String docSearchQuery;
    private String submitFrm;
    private String currentDsnName;
    private String status;
    private String senderId;
    private String senderName;
    private String receiverId;
    private String receiverName;
    private String datepickerfrom;
    private String datepickerTo;
    private String ackStatus;
    private List correlationList;
    private String docType;
    private List docTypeList;
    private List senderIdList;
    private List receiverIdList;
    private List senderNameList;
    private List receiverNameList;
    private String corrattribute;
    private String corrvalue;
    private String corrattribute1;
    private String corrvalue1;
    private String reportrange;
    private String check;
    private List<LtResponseBean> ltResponseList;
    private String database;
    
     private Map senderIdMap;
    private Map receiverIdMap;

    public String execute() throws Exception {
        setResultType(LOGIN);
        try {
            if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
                setResultType("accessFailed");
                int userRoleId = Integer.parseInt(httpServletRequest.getSession(false).getAttribute(AppConstants.SES_ROLE_ID).toString());
                if (AuthorizationManager.getInstance().isAuthorizedUser("L_RESPONSE", userRoleId)) {
                    String defaultFlowId = httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_DEFAULT_FLOWID).toString();
                    String defaultFlowName = DataSourceDataProvider.getInstance().getFlowNameByFlowID(defaultFlowId);
                    httpServletRequest.getSession(false).removeAttribute(AppConstants.SES_LTRESPONSE_LIST);
                    httpServletRequest.getSession(false).removeAttribute(AppConstants.SES_LOG_DOC_LIST);
                    httpServletRequest.getSession(false).removeAttribute(AppConstants.SES_LOGSHIPMENT_LIST);
                    httpServletRequest.getSession(false).removeAttribute(AppConstants.SES_LOAD_LIST);
                    setSenderIdMap(DataSourceDataProvider.getInstance().getTradingPartnersMap());
                    httpServletRequest.getSession(false).setAttribute(AppConstants.SENDERSID_MAP, getSenderIdMap());
                    setReceiverIdMap(DataSourceDataProvider.getInstance().getTradingPartnersMap());
                    httpServletRequest.getSession(false).setAttribute(AppConstants.RECEIVERSId_MAP, getReceiverIdMap());
                    setSenderNameList(DataSourceDataProvider.getInstance().getSenderNamelist("L"));
                    setReceiverNameList(DataSourceDataProvider.getInstance().getReciverNamelist("L"));
                    setCorrelationList(DataSourceDataProvider.getInstance().getCorrelationNames(2, 2));
                    setDocTypeList(DataSourceDataProvider.getInstance().getDocumentTypeList(2, 2));
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
            }
        } catch (Exception exception) {
            LoggerUtility.log(logger, "Exception occurred in execute method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
        }
        return getResultType();
    }

    public String getLtResponseSearchList() throws Exception {
        System.out.println("getLtResponseSearchList start time::"+DateUtility.getInstance().getCurrentDB2Timestamp());
        setResultType(LOGIN);
        try {
            if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
                setResultType("accessFailed");
                int userRoleId = Integer.parseInt(httpServletRequest.getSession(false).getAttribute(AppConstants.SES_ROLE_ID).toString());
                if (AuthorizationManager.getInstance().isAuthorizedUser("L_RESPONSE", userRoleId)) {
                    execute();
                    if (getCheck() == null) {
                        setCheck("1");
                    } else if (getCheck().equals("")) {
                        setCheck("1");
                    }
                    ltResponseList = ServiceLocator.getLtResponseService().getLtResponseList(this);
                    httpServletRequest.getSession(false).setAttribute(AppConstants.SES_LTRESPONSE_LIST, ltResponseList);
                    setResultType(SUCCESS);
                }
            }
        } catch (Exception exception) {
            LoggerUtility.log(logger, "Exception occurred in getLtResponseSearchList method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
            setResultType(ERROR);
        }
         System.out.println("getLtResponseSearchList end time::"+DateUtility.getInstance().getCurrentDB2Timestamp());
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

    public String getSqlQuery() {
        return sqlQuery;
    }

    public void setSqlQuery(String sqlQuery) {
        this.sqlQuery = sqlQuery;
    }

    public String getDocSearchQuery() {
        return docSearchQuery;
    }

    public void setDocSearchQuery(String docSearchQuery) {
        this.docSearchQuery = docSearchQuery;
    }

    public String getSubmitFrm() {
        return submitFrm;
    }

    public void setSubmitFrm(String submitFrm) {
        this.submitFrm = submitFrm;
    }

    public String getCurrentDsnName() {
        return currentDsnName;
    }

    public void setCurrentDsnName(String currentDsnName) {
        this.currentDsnName = currentDsnName;
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

    public List getCorrelationList() {
        return correlationList;
    }

    public void setCorrelationList(List correlationList) {
        this.correlationList = correlationList;
    }

    public String getCorrattribute() {
        return corrattribute;
    }

    public void setCorrattribute(String corrattribute) {
        this.corrattribute = corrattribute;
    }

    public String getCorrvalue() {
        return corrvalue;
    }

    public void setCorrvalue(String corrvalue) {
        this.corrvalue = corrvalue;
    }

    public String getCorrattribute1() {
        return corrattribute1;
    }

    public void setCorrattribute1(String corrattribute1) {
        this.corrattribute1 = corrattribute1;
    }

    public String getCorrvalue1() {
        return corrvalue1;
    }

    public void setCorrvalue1(String corrvalue1) {
        this.corrvalue1 = corrvalue1;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getDatepickerfrom() {
        return datepickerfrom;
    }

    public void setDatepickerfrom(String datepickerfrom) {
        this.datepickerfrom = datepickerfrom;
    }

    public String getDatepickerTo() {
        return datepickerTo;
    }

    public void setDatepickerTo(String datepickerTo) {
        this.datepickerTo = datepickerTo;
    }

    public String getDocType() {
        return docType;
    }

    public void setDocType(String docType) {
        this.docType = docType;
    }

    public List getDocTypeList() {
        return docTypeList;
    }

    public void setDocTypeList(List docTypeList) {
        this.docTypeList = docTypeList;
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

    public String getCheck() {
        return check;
    }

    public void setCheck(String check) {
        this.check = check;
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
