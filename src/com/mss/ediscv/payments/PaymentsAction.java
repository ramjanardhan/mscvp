/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mss.ediscv.payments;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.struts2.interceptor.ServletRequestAware;
import com.mss.ediscv.util.AppConstants;
import com.mss.ediscv.util.DataSourceDataProvider;
import com.mss.ediscv.util.DateUtility;
import com.mss.ediscv.util.ServiceLocator;
import com.opensymphony.xwork2.ActionSupport;
import java.util.List;
import com.mss.ediscv.util.LoggerUtility;
import com.mss.ediscv.util.ServiceLocatorException;
import java.util.Map;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author miracle
 */
public class PaymentsAction extends ActionSupport implements ServletRequestAware {

    private static Logger logger = LogManager.getLogger(PaymentsAction.class.getName());
    private HttpServletRequest httpServletRequest;
    private String sqlQuery;
    private String paSearchQuery;
    private String submitFrm;
    private String resultType;
    private String paDateTo;
    private String paDateFrom;
    private String paSenderId;
    private String paSenderName;
    private String paRecId;
    private String paRecName;
    private String paChequeNo;
    private String paChequeAmt;
    private String sampleValue;
    private String check;
    private String ackStatus;
    private String status;
    private List<PaymentBean> paymentList;
    private String currentDsnName;
    private String corrattribute;
    private String corrvalue;
    private List correlationList;
    private List docTypeList;
    private String corrattribute1;
    private String corrattribute2;
    private String corrvalue1;
    private String corrvalue2;
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
        try {
            if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
                HttpSession httpSession = httpServletRequest.getSession(false);

                httpSession.removeAttribute(AppConstants.SES_SHIPMENT_LIST);
                httpSession.removeAttribute(AppConstants.SES_DOC_LIST);
                httpSession.removeAttribute(AppConstants.SES_INV_LIST);
                httpSession.removeAttribute(AppConstants.SES_PO_LIST);
                if (getSubmitFrm() != null && (!getSubmitFrm().equals("frmDBGrid"))) {
                    if (httpSession.getAttribute(AppConstants.SES_PAYMENT_LIST) != null) {
                        httpSession.removeAttribute(AppConstants.SES_PAYMENT_LIST);
                    }
                } else if (getSubmitFrm() == null && httpSession.getAttribute(AppConstants.SES_PAYMENT_LIST) != null) {
                    httpSession.removeAttribute(AppConstants.SES_PAYMENT_LIST);
                }
                setCorrelationList(DataSourceDataProvider.getInstance().getCorrelationNames(4, 1));
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
            }
        } catch (Exception exception) {
            LoggerUtility.log(logger, "Exception occurred in prepare method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
             httpServletRequest.getSession(false).setAttribute(AppConstants.REQ_EXCEPTION_MSG, exception.getMessage());
            resultType = "error";
        }
        return resultType;
    }

    public String getPaymentSearchQuery() throws ServiceLocatorException {
        System.out.println("getPaymentSearchQuery start time::"+DateUtility.getInstance().getCurrentDB2Timestamp());
        resultType = LOGIN;
        try {
            if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
                prepare();
                if (getCheck() == null) {
                    setCheck("1");
                } else if (getCheck().equals("")) {
                    setCheck("1");
                }
                paymentList = ServiceLocator.getPaymentService().buildpaymentSQuery(this);
                httpServletRequest.getSession(false).setAttribute(AppConstants.SES_PAYMENT_LIST, paymentList);
                resultType = SUCCESS;
            }
        } catch (Exception exception) {
            LoggerUtility.log(logger, "Exception occurred in getPaymentSearchQuery method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
           httpServletRequest.getSession(false).setAttribute(AppConstants.REQ_EXCEPTION_MSG, exception.getMessage());
            resultType = "error";
        }
         System.out.println("getPaymentSearchQuery start time::"+DateUtility.getInstance().getCurrentDB2Timestamp());
        return resultType;
    }

    public String getPaChequeAmt() {
        return paChequeAmt;
    }

    public void setPaChequeAmt(String paChequeAmt) {
        this.paChequeAmt = paChequeAmt;
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

    public void setPaSearchQuery(String paSearchQuery) {
        this.paSearchQuery = paSearchQuery;
    }

    public String getPaSearchQuery() {
        return paSearchQuery;
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

    public String getPaDateTo() {
        return paDateTo;
    }

    public void setPaDateTo(String paDateTo) {
        this.paDateTo = paDateTo;
    }

    public String getPaDateFrom() {
        return paDateFrom;
    }

    public void setPaDateFrom(String paDateFrom) {
        this.paDateFrom = paDateFrom;
    }

    public String getPaSenderId() {
        return paSenderId;
    }

    public void setPaSenderId(String paSenderId) {
        this.paSenderId = paSenderId;
    }

    public String getPaSenderName() {
        return paSenderName;
    }

    public void setPaSenderName(String paSenderName) {
        this.paSenderName = paSenderName;
    }

    public String getPaRecId() {
        return paRecId;
    }

    /**
     * @param paRecId the paRecId to set
     */
    public void setPaRecId(String paRecId) {
        this.paRecId = paRecId;
    }

    public String getPaRecName() {
        return paRecName;
    }

    public void setPaRecName(String paRecName) {
        this.paRecName = paRecName;
    }

    public String getPaChequeNo() {
        return paChequeNo;
    }

    public void setPaChequeNo(String paChequeNo) {
        this.paChequeNo = paChequeNo;
    }

    public List<PaymentBean> getPaymentList() {
        return paymentList;
    }

    public void setPaymentList(List<PaymentBean> paymentList) {
        this.paymentList = paymentList;
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

    public String getReportrange() {
        return reportrange;
    }

    public void setReportrange(String reportrange) {
        this.reportrange = reportrange;
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
