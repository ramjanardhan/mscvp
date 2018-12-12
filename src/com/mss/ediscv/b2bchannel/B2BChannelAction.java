/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mss.ediscv.b2bchannel;

import com.mss.ediscv.util.AppConstants;
import com.mss.ediscv.util.DataSourceDataProvider;
import com.mss.ediscv.util.ServiceLocator;
import com.opensymphony.xwork2.ActionSupport;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.mss.ediscv.util.LoggerUtility;

/**
 * @author miracle
 */
public class B2BChannelAction extends ActionSupport implements ServletRequestAware {

    private static Logger logger = LogManager.getLogger(B2BChannelAction.class.getName());
    private HttpServletRequest httpServletRequest;
    private String resultType;
    private String formAction;
    private int b2bChannelId;
    private String partnerName;
    private String status;
    private String direction;
    private String protocol;
    private String host;
    private String userName;
    private String producerMailBox;
    private String consumerMailBox;
    private String pollingCode;
    private String appId;
    private String senderId;
    private String receiverId;
    private String createdBy;
    private String flowFlag;
    private String configFlowFlag;
    private String configFlowFlag1;

    public String getB2BChannelList() throws Exception {
        setResultType(LOGIN);
        try {
            if (getHttpServletRequest().getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
                String defaultFlowId = httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_DEFAULT_FLOWID).toString();
                if (getConfigFlowFlag().equals("logistics")) {
                    defaultFlowId = DataSourceDataProvider.getInstance().getFlowIdByFlowName("Logistics");
                    httpServletRequest.getSession(false).setAttribute(AppConstants.SES_USER_DEFAULT_FLOWID, defaultFlowId);
                } else if (getConfigFlowFlag().equals("manufacturing")) {
                    defaultFlowId = DataSourceDataProvider.getInstance().getFlowIdByFlowName("Manufacturing");
                    httpServletRequest.getSession(false).setAttribute(AppConstants.SES_USER_DEFAULT_FLOWID, defaultFlowId);
                }
                if (getHttpServletRequest().getSession(false).getAttribute(AppConstants.SES_B2BCHANNEL_LIST) != null) {
                    getHttpServletRequest().getSession(false).removeAttribute(AppConstants.SES_B2BCHANNEL_LIST);
                }
            }
            resultType = SUCCESS;
        } catch (Exception exception) {
            LoggerUtility.log(logger, "Exception occurred in getB2BChannelList method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
            httpServletRequest.getSession(false).setAttribute(AppConstants.REQ_EXCEPTION_MSG, exception.getMessage());
        }
        return resultType;
    }

    public String addB2BChannel() throws Exception {
        setResultType(LOGIN);
        try{
        if (getHttpServletRequest().getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
            setFormAction("doAddB2BChannel");
            resultType = SUCCESS;
        }
        } catch(Exception exception){
            LoggerUtility.log(logger, "Exception occurred in addB2BChannel method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
            httpServletRequest.getSession(false).setAttribute(AppConstants.REQ_EXCEPTION_MSG, exception.getMessage());
        }
        return resultType;
    }

    public String doAddB2BChannel() throws Exception {
        setResultType(LOGIN);
        try {
        if (getHttpServletRequest().getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
            setCreatedBy(getHttpServletRequest().getSession(false).getAttribute(AppConstants.SES_LOGIN_ID).toString());
            setFormAction("doAddB2BChannel");
            String resultMessage = ServiceLocator.getB2BChannelService().addB2BChannel(this);
            getHttpServletRequest().setAttribute(AppConstants.REQ_RESULT_MSG, resultMessage);
            resultType = SUCCESS;
        }
        }catch(Exception exception){
             LoggerUtility.log(logger, "Exception occurred in doAddB2BChannel method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
            httpServletRequest.getSession(false).setAttribute(AppConstants.REQ_EXCEPTION_MSG, exception.getMessage());
        }
        return resultType;
    }

    public String b2BChannelListSearch() throws Exception {
        setResultType(LOGIN);
        try {
        if (getHttpServletRequest().getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
            ArrayList<B2BChannelBean> b2bChannelList = ServiceLocator.getB2BChannelService().buildB2BChannelQuery(this);
            getHttpServletRequest().getSession(false).setAttribute(AppConstants.SES_B2BCHANNEL_LIST, b2bChannelList);
            resultType = SUCCESS;
        }
        }catch(Exception exception){
            LoggerUtility.log(logger, "Exception occurred in b2BChannelListSearch method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
            httpServletRequest.getSession(false).setAttribute(AppConstants.REQ_EXCEPTION_MSG, exception.getMessage());
        }
        return resultType;
    }

    public String b2bchannelEdit() throws Exception {
        setResultType(LOGIN);
        try {
        if (getHttpServletRequest().getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
            setFormAction("doEditB2BChannel");
            ServiceLocator.getB2BChannelService().b2BChannelEdit(this);
            resultType = SUCCESS;
        }
        }catch(Exception exception){
            LoggerUtility.log(logger, "Exception occurred in b2bchannelEdit method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
            httpServletRequest.getSession(false).setAttribute(AppConstants.REQ_EXCEPTION_MSG, exception.getMessage());
        }
        return resultType;
    }

    public String doEditB2BChannel() throws Exception {
        setResultType(LOGIN);
        try {
        if (getHttpServletRequest().getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
            setCreatedBy(getHttpServletRequest().getSession(false).getAttribute(AppConstants.SES_LOGIN_ID).toString());
            setFormAction("doEditB2BChannel");
            String resultMessage = ServiceLocator.getB2BChannelService().doEditB2BChannel(this);
            getHttpServletRequest().setAttribute(AppConstants.REQ_RESULT_MSG, resultMessage);
            resultType = SUCCESS;
        }
        }catch(Exception exception){
             LoggerUtility.log(logger, "Exception occurred in doEditB2BChannel method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
            httpServletRequest.getSession(false).setAttribute(AppConstants.REQ_EXCEPTION_MSG, exception.getMessage());
        }
        return resultType;
    }

    @Override
    public void setServletRequest(HttpServletRequest httpServletRequest) {
        this.httpServletRequest = httpServletRequest;
    }

    public HttpServletRequest getHttpServletRequest() {
        return httpServletRequest;
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

    public int getB2bChannelId() {
        return b2bChannelId;
    }

    public void setB2bChannelId(int b2bChannelId) {
        this.b2bChannelId = b2bChannelId;
    }

    public String getPartnerName() {
        return partnerName;
    }

    public void setPartnerName(String partnerName) {
        this.partnerName = partnerName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getProducerMailBox() {
        return producerMailBox;
    }

    public void setProducerMailBox(String producerMailBox) {
        this.producerMailBox = producerMailBox;
    }

    public String getConsumerMailBox() {
        return consumerMailBox;
    }

    public void setConsumerMailBox(String consumerMailBox) {
        this.consumerMailBox = consumerMailBox;
    }

    public String getPollingCode() {
        return pollingCode;
    }

    public void setPollingCode(String pollingCode) {
        this.pollingCode = pollingCode;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
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

    public String getConfigFlowFlag() {
        return configFlowFlag;
    }

    public void setConfigFlowFlag(String configFlowFlag) {
        this.configFlowFlag = configFlowFlag;
    }

    public String getConfigFlowFlag1() {
        return configFlowFlag1;
    }

    public void setConfigFlowFlag1(String configFlowFlag1) {
        this.configFlowFlag1 = configFlowFlag1;
    }
}
