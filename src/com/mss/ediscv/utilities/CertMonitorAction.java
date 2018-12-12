
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mss.ediscv.utilities;

import com.mss.ediscv.util.AppConstants;
import com.mss.ediscv.util.DataSourceDataProvider;
import com.mss.ediscv.util.ServiceLocator;
import com.mss.ediscv.util.ServiceLocatorException;
import com.opensymphony.xwork2.ActionSupport;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.mss.ediscv.util.LoggerUtility;


/**
 *
 * @author miracle
 */
public class CertMonitorAction extends ActionSupport implements ServletRequestAware, ServletResponseAware {
private static Logger logger = LogManager.getLogger(CertMonitorAction.class.getName());
    private String certType;
    HttpServletRequest hsrequest;
    HttpServletResponse hsresponse;
    private String docdatepickerfrom;
    private String docdatepicker;
    private String reportrange;
    private String listName;
    private String name;
    private String selectedName;
    private List listNameMap;
    private String json;
    private int items;
    private String modifieddate;
    private String processId;
    private String docTpName;
    private String docTpId;
    private String docTpSenderId;
    private String docTpReceiverId;
    private String docTransactionType;
    private String docStatus;

    public String getCertMonitor() throws Exception {
        String resultType = LOGIN;
        try {
            List list = ServiceLocator.getCertMonitorService().getCertMonitorData(getCertType(), getDocdatepickerfrom(), getDocdatepicker());
            hsrequest.getSession(false).setAttribute(AppConstants.CERTMONITOR_LIST, list);
            resultType = SUCCESS;
        } catch (Exception exception) {
             LoggerUtility.log(logger, "Exception occurred in getCertMonitor method:: " +exception.getMessage(), Level.ERROR, exception.getCause());
        }
        return resultType;

    }

    public String codeList() throws ServiceLocatorException {
        String resultType = LOGIN;
        
        try {
            if (hsrequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME).toString() != null) {
                hsrequest.getSession(false).removeAttribute(AppConstants.CODE_LIST);
                setListNameMap(DataSourceDataProvider.getInstance().getListName());
                resultType = SUCCESS;
            }
        } catch (Exception exception) {
             LoggerUtility.log(logger, "Exception occurred in codeList method:: " +exception.getMessage(), Level.ERROR, exception.getCause());
        }
        return resultType;
    }

    public String getCodeListItems() throws Exception {
        String resultType = LOGIN;
        if (hsrequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME).toString() != null) {
            try {
                hsrequest.getSession(false).removeAttribute(AppConstants.CODE_LIST);
                Date d = new Date();
                SimpleDateFormat sd = new SimpleDateFormat("MM/dd/yyyy");
                setListNameMap(DataSourceDataProvider.getInstance().getListName());
                List codeList = new ArrayList();
                codeList = ServiceLocator.getCertMonitorService().doCodeListItems(getListName());
                hsrequest.getSession(false).setAttribute(AppConstants.CODE_LIST, codeList);
                setItems(codeList.size());
                if (codeList.size() == 0) {
                    setSelectedName("");
                    setModifieddate("");
                } else {
                    setModifieddate(sd.format(d));
                }
                resultType = SUCCESS;
            } catch (Exception exception) {
               LoggerUtility.log(logger, "Exception occurred in getCodeListItems method:: " +exception.getMessage(), Level.ERROR, exception.getCause());
            }
        }
        return resultType;
    }

    public String getCodeListName() throws Exception {
        String resultType = LOGIN;
        if (hsrequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME).toString() != null) {
            try {
                hsrequest.getSession(false).removeAttribute(AppConstants.CODE_LIST);
                setListNameMap(ServiceLocator.getCertMonitorService().getCodeListNames(getName()));
                resultType = SUCCESS;
            } catch (Exception exception) {
                 LoggerUtility.log(logger, "Exception occurred in getCodeListName method:: " +exception.getMessage(), Level.ERROR, exception.getCause());
            }
        }
        return resultType;
    }

    public String doGetProcessFlows() throws Exception {
        String resultType = LOGIN;
        hsrequest.getSession(false).removeAttribute(AppConstants.PROCESSFLOW_LIST);
        if (hsrequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME).toString() != null) {
            try {
                List processFlowList = null;
                processFlowList = ServiceLocator.getCertMonitorService().getProcessFlows(this);
                hsrequest.getSession(false).setAttribute(AppConstants.PROCESSFLOW_LIST, processFlowList);
                resultType = SUCCESS;
            } catch (Exception exception) {
                 LoggerUtility.log(logger, "Exception occurred in doGetProcessFlows method:: " +exception.getMessage(), Level.ERROR, exception.getCause());
            }
        }
        return resultType;
    }

    public String addProcessFlow() throws Exception {
        String resultType = LOGIN;
        try {
            if (hsrequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME).toString() != null) {
                resultType = SUCCESS;
            }
        } catch (Exception exception) {
           LoggerUtility.log(logger, "Exception occurred in addProcessFlow method:: " +exception.getMessage(), Level.ERROR, exception.getCause());
        }
        return resultType;
    }

    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
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

@Override
    public void setServletRequest(HttpServletRequest hsrequest) {
        this.hsrequest = hsrequest;
    }

    public HttpServletRequest getServletRequest() {
        return hsrequest;
    }

@Override
    public void setServletResponse(HttpServletResponse hsresponse) {

        this.hsresponse = hsresponse;
    }

    public HttpServletResponse getServletResponse() {

        return hsresponse;
    }

    public String getCertType() {
        return certType;
    }

    public void setCertType(String certType) {
        this.certType = certType;
    }

    public String getDocdatepickerfrom() {
        return docdatepickerfrom;
    }

    public void setDocdatepickerfrom(String docdatepickerfrom) {
        this.docdatepickerfrom = docdatepickerfrom;
    }

    public String getDocdatepicker() {
        return docdatepicker;
    }

    public void setDocdatepicker(String docdatepicker) {
        this.docdatepicker = docdatepicker;
    }

    public String getReportrange() {
        return reportrange;
    }

    public void setReportrange(String reportrange) {
        this.reportrange = reportrange;
    }

    public String getListName() {
        return listName;
    }

    public void setListName(String listName) {
        this.listName = listName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSelectedName() {
        return selectedName;
    }

    public void setSelectedName(String selectedName) {
        this.selectedName = selectedName;
    }

    public List getListNameMap() {
        return listNameMap;
    }

    public void setListNameMap(List listNameMap) {
        this.listNameMap = listNameMap;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    public int getItems() {
        return items;
    }

    public void setItems(int items) {
        this.items = items;
    }

    public String getModifieddate() {
        return modifieddate;
    }

    public void setModifieddate(String modifieddate) {
        this.modifieddate = modifieddate;
    }

}
