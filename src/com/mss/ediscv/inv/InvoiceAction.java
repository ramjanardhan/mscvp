/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mss.ediscv.inv;

import com.mss.ediscv.util.AppConstants;
import com.mss.ediscv.util.DataSourceDataProvider;
import com.mss.ediscv.util.DateUtility;
import com.mss.ediscv.util.ServiceLocator;
import com.opensymphony.xwork2.ActionSupport;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import com.mss.ediscv.util.LoggerUtility;
import java.util.Map;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.interceptor.ServletRequestAware;

/**
 * @author miracle
 */
public class InvoiceAction extends ActionSupport implements ServletRequestAware {

    private static Logger logger = LogManager.getLogger(InvoiceAction.class.getName());
    private HttpServletRequest httpServletRequest;
    private String sqlQuery;
    private String invSearchQuery;
    private String submitFrm;
    private String resultType;
    private String currentDsnName;
    private String invdatepicker;
    private String invdatepickerfrom;
    private String invSenderId;
    private String invSenderName;
    private String invBusId;
    private String invRecName;
    private String invPoNum;
    private String invNum;
    private String sampleValue;
    private List<InvoiceBean> invoiceList;
    private String check;
    private String ackStatus;
    private String status;
    private String corrattribute;
    private String corrattribute1;
    private String corrattribute2;
    private String corrvalue;
    private String corrvalue1;
    private String corrvalue2;
    private List correlationList;
    private List docTypeList;
    private String docType;
    private List senderIdList;
    private List receiverIdList;
    private List senderNameList;
    private List receiverNameList;
    private String reportrange;
    private String database;
    
    
    private Map senderIdMap;
    private Map receiverIdMap;

    public String prepare() throws Exception {
        resultType = LOGIN;
        if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
            HttpSession httpSession = httpServletRequest.getSession(false);
            try {
                httpSession.removeAttribute(AppConstants.SES_PAYMENT_LIST);
                httpSession.removeAttribute(AppConstants.SES_SHIPMENT_LIST);
                httpSession.removeAttribute(AppConstants.SES_DOC_LIST);
                httpSession.removeAttribute(AppConstants.SES_PO_LIST);
                if (getSubmitFrm() != null && (!getSubmitFrm().equals("frmDBGrid"))) {
                    if (httpSession.getAttribute(AppConstants.SES_INV_QUERY) != null) {
                        httpSession.removeAttribute(AppConstants.SES_INV_QUERY);
                    }
                } else if (getSubmitFrm() == null && httpSession.getAttribute(AppConstants.SES_INV_QUERY) != null) {
                    httpSession.removeAttribute(AppConstants.SES_INV_QUERY);
                }
                setCorrelationList(DataSourceDataProvider.getInstance().getCorrelationNames(3, 1));
                setSenderIdMap(DataSourceDataProvider.getInstance().getTradingPartnersMap());
                httpServletRequest.getSession(false).setAttribute(AppConstants.SENDERSID_MAP, getSenderIdMap());
                setReceiverIdMap(DataSourceDataProvider.getInstance().getTradingPartnersMap());
                httpServletRequest.getSession(false).setAttribute(AppConstants.RECEIVERSId_MAP, getReceiverIdMap());
                setSenderNameList(DataSourceDataProvider.getInstance().getSenderNamelist("M"));
                setReceiverNameList(DataSourceDataProvider.getInstance().getReciverNamelist("M"));
                if ("ARCHIVE".equals(getDatabase())) {
                    setDatabase("ARCHIVE");
                } else {
                    setDatabase("MSCVP");
                }
                resultType = SUCCESS;

            } catch (Exception exception) {
                LoggerUtility.log(logger, "Exception occurred in prepare method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
                 httpServletRequest.getSession(false).setAttribute(AppConstants.REQ_EXCEPTION_MSG, exception.getMessage());
                resultType = "error";
            }
        }
        return resultType;
    }

    public String getInvoiceSearchQuery() throws Exception {
         System.out.println("InvoiceSearchQuery start time::"+DateUtility.getInstance().getCurrentDB2Timestamp());
        resultType = LOGIN;
        if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
            try {
                prepare();
                if (getCheck() == null) {
                    setCheck("1");
                } else if (getCheck().equals("")) {
                    setCheck("1");
                }
                invoiceList = ServiceLocator.getInvoiceService().buildinvoiceQuery(this);
                httpServletRequest.getSession(false).setAttribute(AppConstants.SES_INV_LIST, invoiceList);
                resultType = SUCCESS;
            } catch (Exception exception) {
                LoggerUtility.log(logger, "Exception occurred in getInvoiceSearchQuery method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
                httpServletRequest.getSession(false).setAttribute(AppConstants.REQ_EXCEPTION_MSG, exception.getMessage());
                resultType = "error";
            }
        }
        System.out.println("InvoiceSearchQuery end time::"+DateUtility.getInstance().getCurrentDB2Timestamp());
        return resultType;
    }

    public void setServletRequest(HttpServletRequest reqObj) {
        this.setHttpServletRequest(reqObj);
    }

    public void setHttpServletRequest(HttpServletRequest httpServletRequest) {
        this.httpServletRequest = httpServletRequest;
    }

    public void setSqlQuery(String sqlQuery) {
        this.sqlQuery = sqlQuery;
    }

    public String getSqlQuery() {
        return sqlQuery;
    }

    public void setInvSearchQuery(String invSearchQuery) {
        this.invSearchQuery = invSearchQuery;
    }

    public String getInvSearchQuery() {
        return invSearchQuery;
    }

    public void setSubmitFrm(String submitFrm) {
        this.submitFrm = submitFrm;
    }

    public String getSubmitFrm() {
        return submitFrm;
    }

    public void setResultType(String resultType) {
        this.resultType = resultType;
    }

    public String getResultType() {
        return resultType;
    }

    public void setCurrentDsnName(String currentDsnName) {
        this.currentDsnName = currentDsnName;
    }

    public String getCurrentDsnName() {
        return currentDsnName;
    }

    public String getInvNum() {
        return invNum;
    }

    public void setInvNum(String invNum) {
        this.invNum = invNum;
    }

    public String getInvdatepicker() {
        return invdatepicker;
    }

    public void setInvdatepicker(String invdatepicker) {
        this.invdatepicker = invdatepicker;
    }

    public String getInvdatepickerfrom() {
        return invdatepickerfrom;
    }

    public void setInvdatepickerfrom(String invdatepickerfrom) {
        this.invdatepickerfrom = invdatepickerfrom;
    }

    public String getInvSenderId() {
        return invSenderId;
    }

    public void setInvSenderId(String invSenderId) {
        this.invSenderId = invSenderId;
    }

    public String getInvSenderName() {
        return invSenderName;
    }

    public void setInvSenderName(String invSenderName) {
        this.invSenderName = invSenderName;
    }

    public String getInvBusId() {
        return invBusId;
    }

    public void setInvBusId(String invBusId) {
        this.invBusId = invBusId;
    }

    public String getInvRecName() {
        return invRecName;
    }

    public void setInvRecName(String invRecName) {
        this.invRecName = invRecName;
    }

    public String getInvPoNum() {
        return invPoNum;
    }

    public void setInvPoNum(String invPoNum) {
        this.invPoNum = invPoNum;
    }

    public String getSampleValue() {
        return sampleValue;
    }

    public void setSampleValue(String sampleValue) {
        this.sampleValue = sampleValue;
    }

    public String getCheck() {
        return check;
    }

    public void setCheck(String check) {
        this.check = check;
    }

    public String getAckStatus() {
        return ackStatus;
    }

    public void setAckStatus(String ackStatus) {
        this.ackStatus = ackStatus;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public List getCorrelationList() {
        return correlationList;
    }

    public void setCorrelationList(List correlationList) {
        this.correlationList = correlationList;
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

    public String getCorrattribute1() {
        return corrattribute1;
    }

    public void setCorrattribute1(String corrattribute1) {
        this.corrattribute1 = corrattribute1;
    }

    public String getCorrattribute2() {
        return corrattribute2;
    }

    public void setCorrattribute2(String corrattribute2) {
        this.corrattribute2 = corrattribute2;
    }

    public String getCorrvalue1() {
        return corrvalue1;
    }

    public void setCorrvalue1(String corrvalue1) {
        this.corrvalue1 = corrvalue1;
    }

    public String getCorrvalue2() {
        return corrvalue2;
    }

    public void setCorrvalue2(String corrvalue2) {
        this.corrvalue2 = corrvalue2;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getReportrange() {
        return reportrange;
    }

    public void setReportrange(String reportrange) {
        this.reportrange = reportrange;
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
