/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mss.ediscv.logisticsloadtendering;

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
public class LogisticsLoadAction extends ActionSupport implements ServletRequestAware {

    private static Logger logger = LogManager.getLogger(LogisticsLoadAction.class.getName());
    private HttpServletRequest httpServletRequest;
    private String resultType;
    private String sqlQuery;
    private String docSearchQuery;
    private String submitFrm;
    private String currentDsnName;
    private String docdatepicker;
    private String docdatepickerfrom;
    private String docSenderId;
    private String docSenderName;
    private String docBusId;
    private String docRecName;
    private String docIsa;
    private String docPoNum;
    private boolean ck850;
    private boolean ck855;
    private boolean ck860;
    private boolean ck856;
    private boolean ck810;
    private boolean ck820;
    private String sampleValue;
    private String docType;
    private String ttype;
    private String tnumber;
    private String ponumber;
    private String asnnumber;
    private String invnumber;
    private String bolNum;
    private String ChequeNum;
    private String check;
    private String corrattribute;
    private String corrvalue;
    private String corrattribute1;
    private String corrvalue1;
    private String corrattribute2;
    private String corrvalue2;
    private String status;
    private String ackStatus;
    private List correlationList;
    private List docTypeList;
    private List senderIdList;
    private List receiverIdList;
    private List senderNameList;
    private List receiverNameList;
    private List<LogisticsLoadBean> loadList;
    private String reportrange;
    private String database;
    
    private Map senderIdMap;
    private Map receiverIdMap;

    public String execute() throws Exception {
        setResultType(LOGIN);
        try {
            if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
                setResultType("accessFailed");
                int userRoleId = Integer.parseInt(httpServletRequest.getSession(false).getAttribute(AppConstants.SES_ROLE_ID).toString());
                if (AuthorizationManager.getInstance().isAuthorizedUser("LOADTENDER", userRoleId)) {
                    String defaultFlowId = httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_DEFAULT_FLOWID).toString();
                    String defaultFlowName = DataSourceDataProvider.getInstance().getFlowNameByFlowID(defaultFlowId);
                    httpServletRequest.getSession(false).removeAttribute(AppConstants.SES_LOG_DOC_LIST);
                    httpServletRequest.getSession(false).removeAttribute(AppConstants.SES_LOGSHIPMENT_LIST);
                    httpServletRequest.getSession(false).removeAttribute(AppConstants.SES_LOAD_LIST);
                    httpServletRequest.getSession(false).removeAttribute(AppConstants.SES_LTRESPONSE_LIST);
                    setCorrelationList(DataSourceDataProvider.getInstance().getCorrelationNames(1, 2));
                    setDocTypeList(DataSourceDataProvider.getInstance().getDocumentTypeList(1, 2));
                    setSenderIdMap(DataSourceDataProvider.getInstance().getTradingPartnersMap());
                    httpServletRequest.getSession(false).setAttribute(AppConstants.SENDERSID_MAP, getSenderIdMap());
                    setReceiverIdMap(DataSourceDataProvider.getInstance().getTradingPartnersMap());
                    httpServletRequest.getSession(false).setAttribute(AppConstants.RECEIVERSId_MAP, getReceiverIdMap());
                    setSenderNameList(DataSourceDataProvider.getInstance().getSenderNamelist("L"));
                    setReceiverNameList(DataSourceDataProvider.getInstance().getReciverNamelist("L"));
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

    public String getloadSearchQuery() throws Exception {
        System.out.println("getloadSearchQuery start time::" + DateUtility.getInstance().getCurrentDB2Timestamp());
        setResultType(LOGIN);
        if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
            setResultType("accessFailed");
            int userRoleId = Integer.parseInt(httpServletRequest.getSession(false).getAttribute(AppConstants.SES_ROLE_ID).toString());
            if (AuthorizationManager.getInstance().isAuthorizedUser("LOADTENDER", userRoleId)) {
                try {
                    execute();
                    if (getCheck() == null) {
                        setCheck("1");
                    } else if (getCheck().equals("")) {
                        setCheck("1");
                    }
                    loadList = ServiceLocator.getLoadService().buildLoadQuery(this);
                    httpServletRequest.getSession(false).setAttribute(AppConstants.SES_LOAD_LIST, loadList);
                    setResultType(SUCCESS);
                } catch (Exception exception) {
                    LoggerUtility.log(logger, "Exception occurred in getloadSearchQuery method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
                    httpServletRequest.getSession(false).setAttribute(AppConstants.REQ_EXCEPTION_MSG, exception.getMessage());
                    setResultType("error");
                }
            }
        }
        System.out.println("getloadSearchQuery end time::" + DateUtility.getInstance().getCurrentDB2Timestamp());
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

    /**
     * @return the submitFrm
     */
    public String getSubmitFrm() {
        return submitFrm;
    }

    /**
     * @param submitFrm the submitFrm to set
     */
    public void setSubmitFrm(String submitFrm) {
        this.submitFrm = submitFrm;
    }

    public String getCurrentDsnName() {
        return currentDsnName;
    }

    public void setCurrentDsnName(String currentDsnName) {
        this.currentDsnName = currentDsnName;
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

    public String getDocIsa() {
        return docIsa;
    }

    public void setDocIsa(String docIsa) {
        this.docIsa = docIsa;
    }

    public String getDocPoNum() {
        return docPoNum;
    }

    public void setDocPoNum(String docPoNum) {
        this.docPoNum = docPoNum;
    }

    public boolean isCk850() {
        return ck850;
    }

    public void setCk850(boolean ck850) {
        this.ck850 = ck850;
    }

    public boolean isCk855() {
        return ck855;
    }

    public void setCk855(boolean ck855) {
        this.ck855 = ck855;
    }

    public boolean isCk860() {
        return ck860;
    }

    public void setCk860(boolean ck860) {
        this.ck860 = ck860;
    }

    public boolean isCk856() {
        return ck856;
    }

    public void setCk856(boolean ck856) {
        this.ck856 = ck856;
    }

    public boolean isCk810() {
        return ck810;
    }

    public void setCk810(boolean ck810) {
        this.ck810 = ck810;
    }

    public boolean isCk820() {
        return ck820;
    }

    public void setCk820(boolean ck820) {
        this.ck820 = ck820;
    }

    public String getSampleValue() {
        return sampleValue;
    }

    public void setSampleValue(String sampleValue) {
        this.sampleValue = sampleValue;
    }

    public String getDocType() {
        return docType;
    }

    public void setDocType(String docType) {
        this.docType = docType;
    }

    /**
     * @return the ttype
     */
    public String getTtype() {
        return ttype;
    }

    public void setTtype(String ttype) {
        this.ttype = ttype;
    }

    public String getTnumber() {
        return tnumber;
    }

    public void setTnumber(String tnumber) {
        this.tnumber = tnumber;
    }

    public String getPonumber() {
        return ponumber;
    }

    public void setPonumber(String ponumber) {
        this.ponumber = ponumber;
    }

    public String getAsnnumber() {
        return asnnumber;
    }

    public void setAsnnumber(String asnnumber) {
        this.asnnumber = asnnumber;
    }

    public String getInvnumber() {
        return invnumber;
    }

    public void setInvnumber(String invnumber) {
        this.invnumber = invnumber;
    }

    public String getBolNum() {
        return bolNum;
    }

    public void setBolNum(String bolNum) {
        this.bolNum = bolNum;
    }

    public String getChequeNum() {
        return ChequeNum;
    }

    public void setChequeNum(String ChequeNum) {
        this.ChequeNum = ChequeNum;
    }

    public String getCheck() {
        return check;
    }

    public void setCheck(String check) {
        this.check = check;
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

    public String getCorrattribute2() {
        return corrattribute2;
    }

    public void setCorrattribute2(String corrattribute2) {
        this.corrattribute2 = corrattribute2;
    }

    public String getCorrvalue2() {
        return corrvalue2;
    }

    public void setCorrvalue2(String corrvalue2) {
        this.corrvalue2 = corrvalue2;
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
