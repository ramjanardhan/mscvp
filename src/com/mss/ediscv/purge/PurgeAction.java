/*
 * Author : Santosh Kola
 * Created Date : 07/01/2013
 * 
 */
package com.mss.ediscv.purge;

import com.mss.ediscv.util.AppConstants;
import com.mss.ediscv.util.AuthorizationManager;
import com.mss.ediscv.util.DataSourceDataProvider;
import com.mss.ediscv.util.ServiceLocator;
import com.mss.ediscv.util.ServiceLocatorException;
import com.opensymphony.xwork2.ActionSupport;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts2.interceptor.ServletRequestAware;
import com.mss.ediscv.util.LoggerUtility;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author miracle
 */
public class PurgeAction extends ActionSupport implements ServletRequestAware {

    private static Logger logger = LogManager.getLogger(PurgeAction.class.getName());
    private HttpServletRequest httpServletRequest;
    private String datepickerfrom;
    private String datepicker;
    private String transType;
    private String resultType;
    private String resultMessage;
    private String dayCount;
    private String comments;
    private String reportrange;
    private Map flowName;
    private String docdatepickerfrom;

    public String getArcProPage() {
        try {
            Map userFlow = (Map) httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_FLOW_MAP);
            setFlowName(DataSourceDataProvider.getInstance().getTransactionType(userFlow));
        } catch (Exception exception) {
            LoggerUtility.log(logger, "Exception occurred in getArcProPage method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
        }
        return SUCCESS;
    }

    public String getArcHisPage() {
        try {
            Map userFlow = (Map) httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_FLOW_MAP);
            setFlowName(DataSourceDataProvider.getInstance().getTransactionType(userFlow));
        } catch (Exception exception) {
            LoggerUtility.log(logger, "Exception occurred in getArcHisPage method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
        }
        return SUCCESS;
    }

    public String getPurProPage() {
        try {
            Map userFlow = (Map) httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_FLOW_MAP);
            setFlowName(DataSourceDataProvider.getInstance().getTransactionType(userFlow));
        } catch (Exception exception) {
            LoggerUtility.log(logger, "Exception occurred in getPurProPage method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
        }
        return SUCCESS;
    }

    public String getPurHisPage() {
        try {
            Map userFlow = (Map) httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_FLOW_MAP);
            setFlowName(DataSourceDataProvider.getInstance().getTransactionType(userFlow));
        } catch (Exception exception) {
            LoggerUtility.log(logger, "Exception occurred in getPurHisPage method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
        }
        return SUCCESS;
    }

    public String doArchiveProcess() throws ServiceLocatorException {
        resultType = LOGIN;
        try {
            String username = (String) httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME);
            String defaultFlowId = httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_DEFAULT_FLOWID).toString();
            String defaultFlowName = DataSourceDataProvider.getInstance().getFlowNameByFlowID(defaultFlowId);

            if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
                resultType = "accessFailed";
                int userRoleId = Integer.parseInt(httpServletRequest.getSession(false).getAttribute(AppConstants.SES_ROLE_ID).toString());

                if (AuthorizationManager.getInstance().isAuthorizedUser("PURGE_PROCESS", userRoleId)) {
                    try {
                        resultMessage = ServiceLocator.getPurgeService().archiveProcess(this, username, defaultFlowName);
                        setDayCount("");
                        setTransType("-1");
                        Map userFlow = (Map) httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_FLOW_MAP);
                        setFlowName(DataSourceDataProvider.getInstance().getTransactionType(userFlow));
                        httpServletRequest.setAttribute(AppConstants.REQ_RESULT_MSG, resultMessage);
                        resultType = SUCCESS;
                    } catch (Exception e) {
                        resultType = ERROR;
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception exception) {
            LoggerUtility.log(logger, "Exception occurred in doArchiveProcess method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
        }
        return resultType;
    }

    public String doPurgeProcess() throws ServiceLocatorException {
        resultType = LOGIN;
        if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
            resultType = "accessFailed";
            String username = (String) httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME);
            String defaultFlowId = httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_DEFAULT_FLOWID).toString();

            String defaultFlowName = DataSourceDataProvider.getInstance().getFlowNameByFlowID(defaultFlowId);
            int userRoleId = Integer.parseInt(httpServletRequest.getSession(false).getAttribute(AppConstants.SES_ROLE_ID).toString());
            if (AuthorizationManager.getInstance().isAuthorizedUser("PURGE_PROCESS", userRoleId)) {
                try {
                    resultMessage = ServiceLocator.getPurgeService().purgeProcess(this, username, defaultFlowName);
                    setDayCount("");
                    setTransType("-1");
                    Map userFlow = (Map) httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_FLOW_MAP);
                    setFlowName(DataSourceDataProvider.getInstance().getTransactionType(userFlow));
                    httpServletRequest.setAttribute(AppConstants.REQ_RESULT_MSG, resultMessage);
                    resultType = SUCCESS;

                } catch (Exception exception) {
                    resultType = ERROR;
                    LoggerUtility.log(logger, "Exception occurred in doPurgeProcess method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
                }
            }
        }
        return resultType;
    }

    public String getPurHistory() {

        httpServletRequest.getSession(false).removeAttribute(AppConstants.PURGEHISTORY_LIST);
        Map userFlow = (Map) httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_FLOW_MAP);
        setFlowName(DataSourceDataProvider.getInstance().getTransactionType(userFlow));
        try {
            String username = (String) httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME);
            List purgeHistorylist = null;
            purgeHistorylist = ServiceLocator.getPurgeService().getPurHistoryData(username, getDatepickerfrom(), getDatepicker(), getTransType());
            httpServletRequest.getSession(false).setAttribute(AppConstants.PURGEHISTORY_LIST, purgeHistorylist);
        } catch (Exception exception) {
            LoggerUtility.log(logger, "Exception occurred in getPurHistory method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
        }
        return "success";
    }

    public String getArcHistory() {
        httpServletRequest.getSession(false).removeAttribute(AppConstants.ARCHIVEHISTORY_LIST);
        Map userFlow = (Map) httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_FLOW_MAP);
        setFlowName(DataSourceDataProvider.getInstance().getTransactionType(userFlow));
        try {
            String username = (String) httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME);
            List archiveHistorylist = null;
            archiveHistorylist = ServiceLocator.getPurgeService().getArcHistoryData(username, getDatepickerfrom(), getDatepicker(), getTransType());
            httpServletRequest.getSession(false).setAttribute(AppConstants.ARCHIVEHISTORY_LIST, archiveHistorylist);

        } catch (Exception exception) {
            LoggerUtility.log(logger, "Exception occurred in getArcHistory method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
        }
        return "success";
    }

    @Override
    public void setServletRequest(HttpServletRequest httpServletRequest) {
        this.httpServletRequest = httpServletRequest;
    }

    public HttpServletRequest getHttpServletRequest() {
        return httpServletRequest;
    }

    public String getDatepickerfrom() {
        return datepickerfrom;
    }

    public void setDatepickerfrom(String datepickerfrom) {
        this.datepickerfrom = datepickerfrom;
    }

    public String getDatepicker() {
        return datepicker;
    }

    public void setDatepicker(String datepicker) {
        this.datepicker = datepicker;
    }

    public String getTransType() {
        return transType;
    }

    public void setTransType(String transType) {
        this.transType = transType;
    }

    public String getDayCount() {
        return dayCount;
    }

    public void setDayCount(String dayCount) {
        this.dayCount = dayCount;
    }

    public String getReportrange() {
        return reportrange;
    }

    public void setReportrange(String reportrange) {
        this.reportrange = reportrange;
    }

    public Map getFlowName() {
        return flowName;
    }

    public void setFlowName(Map flowName) {
        this.flowName = flowName;
    }

    public String getDocdatepickerfrom() {
        return docdatepickerfrom;
    }

    public void setDocdatepickerfrom(String docdatepickerfrom) {
        this.docdatepickerfrom = docdatepickerfrom;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}
