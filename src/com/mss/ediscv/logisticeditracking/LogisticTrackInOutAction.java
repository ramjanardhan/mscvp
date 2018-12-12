/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mss.ediscv.logisticeditracking;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.ServletRequestAware;
import com.mss.ediscv.util.LoggerUtility;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.mss.ediscv.util.AppConstants;
import com.mss.ediscv.util.DataSourceDataProvider;
import com.mss.ediscv.util.DateUtility;
import com.mss.ediscv.util.ServiceLocator;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

/**
 * @author miracle
 */
public class LogisticTrackInOutAction extends ActionSupport implements ServletRequestAware {

    private static Logger logger = LogManager.getLogger(LogisticTrackInOutAction.class.getName());
    private HttpServletRequest httpServletRequest;
    private String docdatepicker;
    private String docdatepickerfrom;
    private String resultType;
    private String formAction;
    private List docTypeList;
    private List networklanlist;
    private String docType;
    private String partnerMapId;
    private String docNetworkvan;
    private String reportrange;
    private List<LogisticTrackInOutBean> documentList;
    private Map partnerMap;

    public String getTrackDetails() throws Exception {
        setResultType(LOGIN);
        try {
            if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
                List docList;
                docList = DataSourceDataProvider.getInstance().getDocumentTypeList(0, 2);
                setDocTypeList(docList);
                setPartnerMap(DataSourceDataProvider.getInstance().getDashboardPartnerMap("3"));
                setNetworklanlist(DataSourceDataProvider.getInstance().getNetworkVanList());
                setDocdatepicker(DateUtility.getInstance().getCurrentMySqlDateTime1());
                if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_DOCREPORT_LIST) != null) {
                    httpServletRequest.getSession(false).removeAttribute(AppConstants.SES_DOCREPORT_LIST);
                }
                resultType = SUCCESS;
                setResultType(SUCCESS);
            }
        } catch (Exception exception) {
            LoggerUtility.log(logger, "Exception occurred in getTrackDetails method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
        }
        return getResultType();
    }

    public String trackInOutSearch() {
        setResultType(LOGIN);
        if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
            try {
                documentList = ServiceLocator.getLogisticTrackInOutService().getDocumentList(this);
                if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_DOCREPORT_LIST) != null) {
                    httpServletRequest.getSession(false).removeAttribute(AppConstants.SES_DOCREPORT_LIST);
                }
                httpServletRequest.getSession(false).setAttribute(AppConstants.SES_DOCREPORT_LIST, documentList);
                resultType = SUCCESS;
                setResultType(SUCCESS);

            } catch (Exception exception) {
                LoggerUtility.log(logger, "Exception occurred in trackInOutSearch method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
                setResultType(ERROR);
            }
        }
        return getResultType();
    }

    public String trackSummarySearch() {
        setResultType(LOGIN);
        if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
            try {
                setNetworklanlist(DataSourceDataProvider.getInstance().getNetworkVanList());
                documentList = ServiceLocator.getLogisticTrackInOutService().getSummaryDetails(this);
                if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_DOCREPORT_LIST) != null) {
                    httpServletRequest.getSession(false).removeAttribute(AppConstants.SES_DOCREPORT_LIST);
                }
                httpServletRequest.getSession(false).setAttribute(AppConstants.SES_DOCREPORT_LIST, documentList);
                resultType = SUCCESS;
                setResultType(SUCCESS);
            } catch (Exception exception) {
                LoggerUtility.log(logger, "Exception occurred in trackSummarySearch method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
                setResultType(ERROR);
            }
        }
        return getResultType();
    }

    public String trackInquirySearch() {
        setResultType(LOGIN);
        if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
            try {
                List docList;
                docList = DataSourceDataProvider.getInstance().getDocumentTypeList(0, 2);
                setDocTypeList(docList);
                setPartnerMap(DataSourceDataProvider.getInstance().getDashboardPartnerMap("3"));
                documentList = ServiceLocator.getLogisticTrackInOutService().getInquiryDetails(this);
                if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_DOCREPORT_LIST) != null) {
                    httpServletRequest.getSession(false).removeAttribute(AppConstants.SES_DOCREPORT_LIST);
                }
                httpServletRequest.getSession(false).setAttribute(AppConstants.SES_DOCREPORT_LIST, documentList);
                resultType = SUCCESS;
                setResultType(SUCCESS);
            } catch (Exception exception) {
                LoggerUtility.log(logger, "Exception occurred in trackInquirySearch method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
                setResultType(ERROR);
            }
        }
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

    public String getFormAction() {
        return formAction;
    }

    public void setFormAction(String formAction) {
        this.formAction = formAction;
    }

    public Map getPartnerMap() {
        return partnerMap;
    }

    public void setPartnerMap(Map partnerMap) {
        this.partnerMap = partnerMap;
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

    public List getDocTypeList() {
        return docTypeList;
    }

    public void setDocTypeList(List docTypeList) {
        this.docTypeList = docTypeList;
    }

    public List<LogisticTrackInOutBean> getDocumentList() {
        return documentList;
    }

    public void setDocumentList(List<LogisticTrackInOutBean> documentList) {
        this.documentList = documentList;
    }

    public String getDocType() {
        return docType;
    }

    public void setDocType(String docType) {
        this.docType = docType;
    }

    public String getPartnerMapId() {
        return partnerMapId;
    }

    public void setPartnerMapId(String partnerMapId) {
        this.partnerMapId = partnerMapId;
    }

    public String getDocNetworkvan() {
        return docNetworkvan;
    }

    public void setDocNetworkvan(String docNetworkvan) {
        this.docNetworkvan = docNetworkvan;
    }

    public List getNetworklanlist() {
        return networklanlist;
    }

    public void setNetworklanlist(List networklanlist) {
        this.networklanlist = networklanlist;
    }

    public String getReportrange() {
        return reportrange;
    }

    public void setReportrange(String reportrange) {
        this.reportrange = reportrange;
    }
}
