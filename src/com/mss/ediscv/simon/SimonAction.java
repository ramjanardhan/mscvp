/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mss.ediscv.simon;

import com.mss.ediscv.logistics.LogisticsAction;
import com.mss.ediscv.util.AppConstants;
import com.mss.ediscv.util.ServiceLocator;
import com.mss.ediscv.util.ServiceLocatorException;
import static com.opensymphony.xwork2.Action.LOGIN;
import static com.opensymphony.xwork2.Action.SUCCESS;
import com.opensymphony.xwork2.ActionSupport;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import com.mss.ediscv.util.LoggerUtility;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.interceptor.ServletRequestAware;

/**
 *
 * @author janardhan
 */
public class SimonAction extends ActionSupport implements ServletRequestAware {

    private static Logger logger = LogManager.getLogger(LogisticsAction.class.getName());
    private HttpServletRequest httpServletRequest;
    private String resultType;
    private String partnerName;
    private String applicationId;
    private String datepickerfrom;
    private String datepickerTo;
    private String reportrange;
    private String senderId;
    private String recId;
    private String transactionType;
    private String documentType;
    private String internalIdentifier;
    private String partnerIdentifier;
    private String createdBy;
    private String flowFlag;
    private String direction;
    private String status;
    private String referenceValue;
    private String referenceName;
    private String referenceValue1;
    private String referenceName1;
    private String referenceValue2;
    private String referenceName2;
    private String countryCode;
    private String instanceId;
    private String mapName;
    private String deliveryChannel;
    private List transactionSearchList;
    private String flag;
    private String fileName;
    private String mailBoxName;
    private String docTpName;
    private String docTpId;
    private String docTpSenderId;
    private String docTpReceiverId;
    private String docTransactionType;
    private String docStatus;

    public String execute() throws Exception {
        setResultType(LOGIN);
        try {
            if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
                httpServletRequest.getSession(false).removeAttribute(AppConstants.TRANSACTION_SEARCH_LIST);

                setResultType(SUCCESS);

            }
        } catch (Exception exception) {
            LoggerUtility.log(logger, "Exception occurred in execute method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
        }
        return getResultType();
    }

    public String getTransactionSearch() throws ServiceLocatorException {
        setResultType(LOGIN);
        try {
            if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
                transactionSearchList = ServiceLocator.getSimonService().getDetails(this);
                setResultType(SUCCESS);
                httpServletRequest.getSession(false).setAttribute(AppConstants.TRANSACTION_SEARCH_LIST, transactionSearchList);
            }
        } catch (Exception exception) {
            LoggerUtility.log(logger, "Exception occurred in getTransactionSearch method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
        }
        return getResultType();
    }

    public String getDeliveryChannelList() throws ServiceLocatorException {
        setResultType(LOGIN);
        try {
            if (getHttpServletRequest().getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
                setFlowFlag(getHttpServletRequest().getSession(false).getAttribute(AppConstants.SES_USER_DEFAULT_FLOWID).toString());
                setCreatedBy(getHttpServletRequest().getSession(false).getAttribute(AppConstants.SES_LOGIN_ID).toString());
                ArrayList<SimonBean> partnerList = ServiceLocator.getDeliveryChannelList().buildPartnerQuery(this);
                getHttpServletRequest().getSession(false).setAttribute(AppConstants.SES_DELIVERY_PARTNER_LIST, partnerList);
                List processFlowList = ServiceLocator.getDeliveryChannelList().getProcessFlows(this);
                getHttpServletRequest().getSession(false).setAttribute(AppConstants.SES_DELIVERY_PROCESS_FLOW_GRID, processFlowList);
                resultType = SUCCESS;
            }
        } catch (Exception exception) {
            LoggerUtility.log(logger, "Exception occurred in getDeliveryChannelList method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
        }
        return resultType;

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

    public HttpServletRequest getHttpServletRequest() {
        return httpServletRequest;
    }

    public void setHttpServletRequest(HttpServletRequest httpServletRequest) {
        this.httpServletRequest = httpServletRequest;
    }

    public String getPartnerName() {
        return partnerName;
    }

    public void setPartnerName(String partnerName) {
        this.partnerName = partnerName;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public String getDatepickerfrom() {
        return datepickerfrom;
    }

    public void setDatepickerfrom(String datepickerfrom) {
        this.datepickerfrom = datepickerfrom;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getMailBoxName() {
        return mailBoxName;
    }

    public String getReferenceValue1() {
        return referenceValue1;
    }

    public void setReferenceValue1(String referenceValue1) {
        this.referenceValue1 = referenceValue1;
    }

    public String getReferenceName1() {
        return referenceName1;
    }

    public void setReferenceName1(String referenceName1) {
        this.referenceName1 = referenceName1;
    }

    public String getReferenceValue2() {
        return referenceValue2;
    }

    public void setReferenceValue2(String referenceValue2) {
        this.referenceValue2 = referenceValue2;
    }

    public String getReferenceName2() {
        return referenceName2;
    }

    public void setReferenceName2(String referenceName2) {
        this.referenceName2 = referenceName2;
    }

    public void setMailBoxName(String mailBoxName) {
        this.mailBoxName = mailBoxName;
    }

    public String getDatepickerTo() {
        return datepickerTo;
    }

    public void setDatepickerTo(String datepickerTo) {
        this.datepickerTo = datepickerTo;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getMapName() {
        return mapName;
    }

    public void setMapName(String mapName) {
        this.mapName = mapName;
    }

    public String getRecId() {
        return recId;
    }

    public void setRecId(String recId) {
        this.recId = recId;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static Logger getLogger() {
        return logger;
    }

    public static void setLogger(Logger logger) {
        SimonAction.logger = logger;
    }

    public String getReportrange() {
        return reportrange;
    }

    public void setReportrange(String reportrange) {
        this.reportrange = reportrange;
    }

    public String getReferenceValue() {
        return referenceValue;
    }

    public void setReferenceValue(String referenceValue) {
        this.referenceValue = referenceValue;
    }

    public String getReferenceName() {
        return referenceName;
    }

    public void setReferenceName(String referenceName) {
        this.referenceName = referenceName;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getDeliveryChannel() {
        return deliveryChannel;
    }

    public void setDeliveryChannel(String deliveryChannel) {
        this.deliveryChannel = deliveryChannel;
    }

    public List getTransactionSearchList() {
        return transactionSearchList;
    }

    public void setTransactionSearchList(List transactionSearchList) {
        this.transactionSearchList = transactionSearchList;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getInternalIdentifier() {
        return internalIdentifier;
    }

    public void setInternalIdentifier(String internalIdentifier) {
        this.internalIdentifier = internalIdentifier;
    }

    public String getPartnerIdentifier() {
        return partnerIdentifier;
    }

    public void setPartnerIdentifier(String partnerIdentifier) {
        this.partnerIdentifier = partnerIdentifier;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getFlowFlag() {
        return flowFlag;
    }

    public void setFlowFlag(String flowFlag) {
        this.flowFlag = flowFlag;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getDocTpName() {
        return docTpName;
    }

    public void setDocTpName(String docTpName) {
        this.docTpName = docTpName;
    }

    public String getDocTpId() {
        return docTpId;
    }

    public void setDocTpId(String docTpId) {
        this.docTpId = docTpId;
    }

    public String getDocTpSenderId() {
        return docTpSenderId;
    }

    public void setDocTpSenderId(String docTpSenderId) {
        this.docTpSenderId = docTpSenderId;
    }

    public String getDocTpReceiverId() {
        return docTpReceiverId;
    }

    public void setDocTpReceiverId(String docTpReceiverId) {
        this.docTpReceiverId = docTpReceiverId;
    }

    public String getDocTransactionType() {
        return docTransactionType;
    }

    public void setDocTransactionType(String docTransactionType) {
        this.docTransactionType = docTransactionType;
    }

    public String getDocStatus() {
        return docStatus;
    }

    public void setDocStatus(String docStatus) {
        this.docStatus = docStatus;
    }

}
