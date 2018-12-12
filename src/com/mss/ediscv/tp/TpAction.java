/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mss.ediscv.tp;

import com.mss.ediscv.util.AppConstants;
import com.mss.ediscv.util.ServiceLocator;
import com.opensymphony.xwork2.ActionSupport;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import com.mss.ediscv.util.LoggerUtility;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.interceptor.ServletRequestAware;

/**
 * @author miracle
 */
public class TpAction extends ActionSupport implements ServletRequestAware {

    private static Logger logger = LogManager.getLogger(TpAction.class.getName());
    private HttpServletRequest httpServletRequest;
    private String sqlQuery;
    private String tpSearchQuery;
    private String submitFrm;
    private String resultType;
    private String id;
    private String name;
    private String tpInPath;
    private String tpOutPath;
    private String contact;
    private String phno;
    private String dept;
    private String commid;
    private String qualifier;
    private ArrayList tpList;

    public String prepare() throws Exception {
        setResultType(LOGIN);
        try {
            if (getHttpServletRequest().getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
                HttpSession httpSession = getHttpServletRequest().getSession(false);

                httpSession.removeAttribute(AppConstants.SES_PAYMENT_LIST);
                httpSession.removeAttribute(AppConstants.SES_SHIPMENT_LIST);
                httpSession.removeAttribute(AppConstants.SES_DOC_LIST);
                httpSession.removeAttribute(AppConstants.SES_INV_LIST);
                httpSession.removeAttribute(AppConstants.SES_PO_LIST);
                if (getSubmitFrm() != null && (!getSubmitFrm().equals("frmDBGrid"))) {
                    if (httpSession.getAttribute(AppConstants.SES_TP_LIST) != null) {
                        httpSession.removeAttribute(AppConstants.SES_TP_LIST);
                    }
                } else if (getSubmitFrm() == null && httpSession.getAttribute(AppConstants.SES_TP_LIST) != null) {
                    httpSession.removeAttribute(AppConstants.SES_TP_LIST);
                }
                setResultType(SUCCESS);
            }
        } catch (Exception exception) {
            LoggerUtility.log(logger, "Exception occurred in prepare method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
            setResultType("error");
        }
        return getResultType();
    }

    public String doAddTP() throws Exception {
        resultType = LOGIN;
        if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
            try {
                prepare();
                String responseString = ServiceLocator.getTpService().addTP(this);
                httpServletRequest.getSession(false).setAttribute("responseString", responseString);
                resultType = SUCCESS;
            } catch (Exception exception) {
                LoggerUtility.log(logger, "Exception occurred in doAddTP method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
                httpServletRequest.getSession(false).setAttribute(AppConstants.REQ_EXCEPTION_MSG, exception.getMessage());
                resultType = "error";
            }
        }
        return resultType;
    }

    public String getTpList() throws Exception {
        resultType = LOGIN;
        if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
            try {
                prepare();
                tpList = ServiceLocator.getTpService().getTpList(this);
                httpServletRequest.getSession(false).setAttribute(AppConstants.SES_TP_LIST, tpList);
                resultType = SUCCESS;
            } catch (Exception exception) {
                LoggerUtility.log(logger, "Exception occurred in doAddTP method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
                httpServletRequest.getSession(false).setAttribute(AppConstants.REQ_EXCEPTION_MSG, exception.getMessage());
                resultType = "error";
            }
        }
        return resultType;
    }

    public HttpServletRequest getHttpServletRequest() {
        return httpServletRequest;
    }

    public void setHttpServletRequest(HttpServletRequest httpServletRequest) {
        this.httpServletRequest = httpServletRequest;
    }

    public String getSqlQuery() {
        return sqlQuery;
    }

    public void setSqlQuery(String sqlQuery) {
        this.sqlQuery = sqlQuery;
    }

    public String getTpSearchQuery() {
        return tpSearchQuery;
    }

    public void setTpSearchQuery(String tpSearchQuery) {
        this.tpSearchQuery = tpSearchQuery;
    }

    public String getSubmitFrm() {
        return submitFrm;
    }

    public void setSubmitFrm(String submitFrm) {
        this.submitFrm = submitFrm;
    }

    public String getResultType() {
        return resultType;
    }

    public void setResultType(String resultType) {
        this.resultType = resultType;
    }

    @Override
    public void setServletRequest(HttpServletRequest hsr) {
        this.setHttpServletRequest(hsr);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTpInPath() {
        return tpInPath;
    }

    public void setTpInPath(String tpInPath) {
        this.tpInPath = tpInPath;
    }

    public String getTpOutPath() {
        return tpOutPath;
    }

    public void setTpOutPath(String tpOutPath) {
        this.tpOutPath = tpOutPath;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getPhno() {
        return phno;
    }

    public void setPhno(String phno) {
        this.phno = phno;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public String getCommid() {
        return commid;
    }

    public void setCommid(String commid) {
        this.commid = commid;
    }

    public String getQualifier() {
        return qualifier;
    }

    public void setQualifier(String qualifier) {
        this.qualifier = qualifier;
    }
}
