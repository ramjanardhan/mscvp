/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mss.ediscv.routing;

import com.mss.ediscv.util.AppConstants;
import com.mss.ediscv.util.DataSourceDataProvider;
import com.mss.ediscv.util.ServiceLocator;
import com.opensymphony.xwork2.ActionSupport;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts2.interceptor.ServletRequestAware;
import com.mss.ediscv.util.LoggerUtility;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author miracle
 */
public class RoutingAction extends ActionSupport implements ServletRequestAware {

    private static Logger logger = LogManager.getLogger(RoutingAction.class.getName());
    private HttpServletRequest httpServletRequest;
    private String resultType;
    private String formAction;
    private int routingId;
    private String name;
    private String acceptorLookupAlias;
    private String direction;
    private String internalRouteEmail;
    private String systemType;
    private String status;
    private String destMailBox;
    private String envelope;
    private String configFlowFlag;
    private String configFlowFlag1;

    public String addRouting() throws Exception {
        setResultType(LOGIN);
        try {
            if (getHttpServletRequest().getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
                setFormAction("doAddRouting");
                resultType = SUCCESS;
            }
        } catch (Exception exception) {
            LoggerUtility.log(logger, "Exception occurred in addRouting method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
        }
        return resultType;
    }

    public String doAddRouting() throws Exception {
        setResultType(LOGIN);
        try {
            if (getHttpServletRequest().getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
                setFormAction("doAddRouting");
                String resultMessage = ServiceLocator.getRoutingService().addRouting(this);
                getHttpServletRequest().setAttribute(AppConstants.REQ_RESULT_MSG, resultMessage);
                resultType = SUCCESS;
            }
        } catch (Exception exception) {
            LoggerUtility.log(logger, "Exception occurred in doAddRouting method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
        }
        return resultType;
    }

    public String getRoutingList() throws Exception {
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
                if (getHttpServletRequest().getSession(false).getAttribute(AppConstants.SES_ROUTING_LIST) != null) {
                    getHttpServletRequest().getSession(false).removeAttribute(AppConstants.SES_ROUTING_LIST);
                }
                resultType = SUCCESS;
            }
        } catch (Exception exception) {
            LoggerUtility.log(logger, "Exception occurred in getRoutingList method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
        }
        return resultType;
    }

    public String routingSearch() throws Exception {
        setResultType(LOGIN);
        try {
            if (getHttpServletRequest().getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
                ArrayList<RoutingBean> routingList = ServiceLocator.getRoutingService().buildRoutingQuery(this);
                getHttpServletRequest().getSession(false).setAttribute(AppConstants.SES_ROUTING_LIST, routingList);
                resultType = SUCCESS;
            }
        } catch (Exception exception) {
            LoggerUtility.log(logger, "Exception occurred in routingSearch method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
        }
        return resultType;
    }

    public String routingEdit() throws Exception {
        setResultType(LOGIN);
        try {
            if (getHttpServletRequest().getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
                setFormAction("doEditRouting");
                ServiceLocator.getRoutingService().getRouting(this);
                resultType = SUCCESS;
            }
        } catch (Exception exception) {
            LoggerUtility.log(logger, "Exception occurred in routingEdit method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
        }
        return resultType;
    }

    public String doEditRouting() throws Exception {
        setResultType(LOGIN);
        try {
            if (getHttpServletRequest().getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
                setFormAction("doEditRouting");
                String resultMessage = ServiceLocator.getRoutingService().editRouting(this);
                getHttpServletRequest().setAttribute(AppConstants.REQ_RESULT_MSG, resultMessage);
                resultType = SUCCESS;
            }
        } catch (Exception exception) {
            LoggerUtility.log(logger, "Exception occurred in doEditRouting method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
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

    public int getRoutingId() {
        return routingId;
    }

    public void setRoutingId(int routingId) {
        this.routingId = routingId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAcceptorLookupAlias() {
        return acceptorLookupAlias;
    }

    public void setAcceptorLookupAlias(String acceptorLookupAlias) {
        this.acceptorLookupAlias = acceptorLookupAlias;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getInternalRouteEmail() {
        return internalRouteEmail;
    }

    public void setInternalRouteEmail(String internalRouteEmail) {
        this.internalRouteEmail = internalRouteEmail;
    }

    public String getSystemType() {
        return systemType;
    }

    public void setSystemType(String systemType) {
        this.systemType = systemType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDestMailBox() {
        return destMailBox;
    }

    public void setDestMailBox(String destMailBox) {
        this.destMailBox = destMailBox;
    }

    public String getEnvelope() {
        return envelope;
    }

    public void setEnvelope(String envelope) {
        this.envelope = envelope;
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
