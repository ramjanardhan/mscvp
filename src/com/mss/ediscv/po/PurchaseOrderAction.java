/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mss.ediscv.po;

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
public class PurchaseOrderAction extends ActionSupport implements ServletRequestAware {

    private static Logger logger = LogManager.getLogger(PurchaseOrderAction.class.getName());
    private HttpServletRequest httpServletRequest;
    private String sqlQuery;
    private String poSearchList;
    private String submitFrm;
    private String resultType;
    private String currentDsnName;
    private String poDateTo;
    private String poDateFrom;
    private String poRecId;
    private String poRecName;
    private String poSenderId;
    private String poSenderName;
    private String poNumber;
    private String sampleValue;
    private String check;
    private String ackStatus;
    private String gsNumber;
    private String status;
    private List correlationList;
    private List docTypeList;
    private String corrattribute;
    private String corrvalue;
    private String corrattribute1;
    private String corrvalue1;
    private String corrattribute2;
    private String corrvalue2;
    private String docType;
    private List senderIdList;
    private List receiverIdList;
    private List senderNameList;
    private List receiverNameList;
    private String reportrange;
    private List<PurchaseOrderBean> purchaseOrderList;
    private String database;
    
    private Map senderIdMap;
    private Map receiverIdMap;

    public String prepare() throws Exception {
        resultType = LOGIN;
        try {
            if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
                HttpSession httpSession = httpServletRequest.getSession(false);

                httpSession.removeAttribute(AppConstants.SES_PAYMENT_LIST);
                httpSession.removeAttribute(AppConstants.SES_SHIPMENT_LIST);
                httpSession.removeAttribute(AppConstants.SES_DOC_LIST);
                httpSession.removeAttribute(AppConstants.SES_INV_LIST);
                if (getSubmitFrm() != null && (!getSubmitFrm().equals("frmDBGrid"))) {
                    if (httpSession.getAttribute(AppConstants.SES_PO_LIST) != null) {
                        httpSession.removeAttribute(AppConstants.SES_PO_LIST);
                    }
                } else if (getSubmitFrm() == null && httpSession.getAttribute(AppConstants.SES_PO_LIST) != null) {
                    httpSession.removeAttribute(AppConstants.SES_PO_LIST);
                }
                setCorrelationList(DataSourceDataProvider.getInstance().getCorrelationNames(1, 1));
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

    public String getPurchaseSearchQuery() throws ServiceLocatorException {
        System.out.println("getPurchaseSearchQuery start time::"+DateUtility.getInstance().getCurrentDB2Timestamp());
        resultType = LOGIN;
        try {
            if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
                prepare();
                if (getCheck() == null) {
                    setCheck("1");
                } else if (getCheck().equals("")) {
                    setCheck("1");
                }
                purchaseOrderList = ServiceLocator.getPurchaseService().buildPurchaseQuery(this);
                httpServletRequest.getSession(false).setAttribute(AppConstants.SES_PO_LIST, purchaseOrderList);
                resultType = SUCCESS;
            }
        } catch (Exception exception) {
            LoggerUtility.log(logger, "Exception occurred in getPurchaseSearchQuery method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
            httpServletRequest.getSession(false).setAttribute(AppConstants.REQ_EXCEPTION_MSG, exception.getMessage());
            resultType = "error";
        }
        System.out.println("getPurchaseSearchQuery end time::"+DateUtility.getInstance().getCurrentDB2Timestamp());
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

    public void setPoSearchList(String poSearchList) {
        this.poSearchList = poSearchList;
    }

    public String getPoSearchList() {
        return poSearchList;
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

    public String getPoDateTo() {
        return poDateTo;
    }

    public void setPoDateTo(String poDateTo) {
        this.poDateTo = poDateTo;
    }

    public String getPoDateFrom() {
        return poDateFrom;
    }

    public void setPoDateFrom(String poDateFrom) {
        this.poDateFrom = poDateFrom;
    }

    public String getPoRecId() {
        return poRecId;
    }

    public void setPoRecId(String poRecId) {
        this.poRecId = poRecId;
    }

    public String getPoRecName() {
        return poRecName;
    }

    public void setPoRecName(String poRecName) {
        this.poRecName = poRecName;
    }

    public String getPoSenderId() {
        return poSenderId;
    }

    public void setPoSenderId(String poSenderId) {
        this.poSenderId = poSenderId;
    }

    public String getPoSenderName() {
        return poSenderName;
    }

    public void setPoSenderName(String poSenderName) {
        this.poSenderName = poSenderName;
    }

    public String getPoNumber() {
        return poNumber;
    }

    public void setPoNumber(String poNumber) {
        this.poNumber = poNumber;
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

    public String getGsNumber() {
        return gsNumber;
    }

    public void setGsNumber(String gsNumber) {
        this.gsNumber = gsNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getCorrvalue2() {
        return corrvalue2;
    }

    public void setCorrvalue2(String corrvalue2) {
        this.corrvalue2 = corrvalue2;
    }

    public String getCorrattribute2() {
        return corrattribute2;
    }

    public void setCorrattribute2(String corrattribute2) {
        this.corrattribute2 = corrattribute2;
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
