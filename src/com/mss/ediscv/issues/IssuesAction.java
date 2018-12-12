/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mss.ediscv.issues;

import com.mss.ediscv.util.*;
import com.opensymphony.xwork2.ActionSupport;
import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;
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
public class IssuesAction extends ActionSupport implements ServletRequestAware {

    private static Logger logger = LogManager.getLogger(IssuesAction.class.getName());
    private HttpServletRequest httpServletRequest;
    private String resultType;
    private String submitFrm;
    private String category;
    private String priority;
    private String assignment;
    private String group;
    private String summary;
    private String desc;
    private String time;
    private String status;
    private String ticketId;
    private String project;
    private List<IssueBean> issueList;
    private String fileLocation;
    private String fcategory;
    private String fromtime;
    private String totime;
    private int id;
    private String formAction;
    private String update;
    private Map usersMap;
    private Map priorityMap;
    private Map categoryMap;
    private File file;
    private String contentType;
    private String filename;
    private String check;
    private String issuedatepickerfrom;
    private String issuedatepicker;
    private String userFlowMap;
    private Map selectUsers;
    private String searchType;

    public String prepare() throws Exception {
        resultType = LOGIN;
        if (getHttpServletRequest().getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
            HttpSession httpSession = getHttpServletRequest().getSession(false);
            try {
                httpSession.removeAttribute(AppConstants.SES_PAYMENT_LIST);
                httpSession.removeAttribute(AppConstants.SES_SHIPMENT_LIST);
                httpSession.removeAttribute(AppConstants.SES_DOC_LIST);
                httpSession.removeAttribute(AppConstants.SES_INV_LIST);
                httpSession.removeAttribute(AppConstants.SES_PO_LIST);
                httpSession.removeAttribute(AppConstants.SES_TRADINGPARTNER_LIST);
                httpSession.removeAttribute(AppConstants.SES_ISSUE_LIST);
                setFormAction("../issues/doCreateIssue.action");
                setUsersMap(DataSourceDataProvider.getInstance().getUsers());
                setPriorityMap(DataSourceDataProvider.getInstance().getPriority());
                setCategoryMap(DataSourceDataProvider.getInstance().getCategory());
                resultType = SUCCESS;
            } catch (Exception exception) {
                LoggerUtility.log(logger, "Exception occurred in prepare method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
               httpServletRequest.getSession(false).setAttribute(AppConstants.REQ_EXCEPTION_MSG, exception.getMessage());
                resultType = "error";
            }
        }
        return resultType;
    }

    public String doCreateIssue() throws Exception {
        resultType = LOGIN;
        if (getHttpServletRequest().getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
            try {
                prepare();
                String responseString = "";
                setFormAction("../issues/doSearchIssue.action");
                responseString = ServiceLocator.getIssuesService().doCreateIssue(this, filename, contentType, file, httpServletRequest);
                getHttpServletRequest().setAttribute(AppConstants.REQ_RESULT_MSG, responseString);
                resultType = SUCCESS;
            } catch (Exception exception) {
                LoggerUtility.log(logger, "Exception occurred in doCreateIssue method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
               httpServletRequest.getSession(false).setAttribute(AppConstants.REQ_EXCEPTION_MSG, exception.getMessage());
                resultType = "error";
            }
        }
        return resultType;
    }

    public String doSearchIssue() throws Exception {
        resultType = LOGIN;
        if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
            try {
                prepare();
                if (getCheck() == null) {
                    setCheck("1");
                } else if (getCheck().equals("")) {
                    setCheck("1");
                }
                issueList = ServiceLocator.getIssuesService().buildIssueQuery(this, getHttpServletRequest());
                httpServletRequest.getSession(false).setAttribute(AppConstants.SES_ISSUE_LIST, issueList);
                setSearchType("AllIssues");
                setFormAction("../issues/doSearchIssue.action");
                resultType = SUCCESS;
            } catch (Exception exception) {
                LoggerUtility.log(logger, "Exception occurred in doSearchIssue method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
                 httpServletRequest.getSession(false).setAttribute(AppConstants.REQ_EXCEPTION_MSG, exception.getMessage());
                resultType = "error";
            }
        }
        return resultType;
    }

    public String issueEdit() throws Exception {
        resultType = LOGIN;
        if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
            HttpSession httpSession = getHttpServletRequest().getSession(false);
            try {
                httpSession.removeAttribute(AppConstants.SES_SHIPMENT_LIST);
                httpSession.removeAttribute(AppConstants.SES_DOC_LIST);
                httpSession.removeAttribute(AppConstants.SES_INV_LIST);
                httpSession.removeAttribute(AppConstants.SES_PO_LIST);
                httpSession.removeAttribute(AppConstants.SES_TRADINGPARTNER_LIST);
                httpSession.removeAttribute(AppConstants.SES_ISSUE_LIST);
                setUsersMap(DataSourceDataProvider.getInstance().getUsers());
                setPriorityMap(DataSourceDataProvider.getInstance().getPriority());
                setCategoryMap(DataSourceDataProvider.getInstance().getCategory());
                ServiceLocator.getIssuesService().issueEdit(this);
                setFormAction("../issues/doIssueEdit.action");
                setUpdate("1");
                setSearchType(getSearchType());
                resultType = SUCCESS;
            } catch (Exception exception) {
                LoggerUtility.log(logger, "Exception occurred in issueEdit method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
                 httpServletRequest.getSession(false).setAttribute(AppConstants.REQ_EXCEPTION_MSG, exception.getMessage());
                resultType = "error";
            }
        }
        return resultType;
    }

    public String searchIssuePrepare() throws Exception {
        resultType = LOGIN;
        if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
            HttpSession httpSession = getHttpServletRequest().getSession(false);
            try {
                httpSession.removeAttribute(AppConstants.SES_PAYMENT_LIST);
                httpSession.removeAttribute(AppConstants.SES_SHIPMENT_LIST);
                httpSession.removeAttribute(AppConstants.SES_DOC_LIST);
                httpSession.removeAttribute(AppConstants.SES_INV_LIST);
                httpSession.removeAttribute(AppConstants.SES_PO_LIST);
                httpSession.removeAttribute(AppConstants.SES_TRADINGPARTNER_LIST);
                httpSession.removeAttribute(AppConstants.SES_ISSUE_LIST);
                setUsersMap(DataSourceDataProvider.getInstance().getUsers());
                setPriorityMap(DataSourceDataProvider.getInstance().getPriority());
                setCategoryMap(DataSourceDataProvider.getInstance().getCategory());
                if (getSearchType().equals("MyIssues")) {
                    setFormAction("../issues/doSearchMyIssue.action");
                    httpServletRequest.setAttribute("searchType", "MyIssues");
                } else if (getSearchType().equals("AllIssues")) {
                    setFormAction("../issues/doSearchIssue.action");
                    httpServletRequest.setAttribute("searchType", "AllIssues");
                }
                resultType = SUCCESS;
            } catch (Exception exception) {
                LoggerUtility.log(logger, "Exception occurred in searchIssuePrepare method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
                 httpServletRequest.getSession(false).setAttribute(AppConstants.REQ_EXCEPTION_MSG, exception.getMessage());
                resultType = "error";
            }
        }
        return resultType;
    }

    public String doSearchMyIssue() throws Exception {
        resultType = LOGIN;
        if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
            try {
                prepare();
                if (getCheck() == null) {
                    setCheck("1");
                } else if (getCheck().equals("")) {
                    setCheck("1");
                }
                setSearchType("MyIssues");
                setFormAction("../issues/doSearchMyIssue.action");
                issueList = ServiceLocator.getIssuesService().getMyIssueList(this, getHttpServletRequest());
                httpServletRequest.getSession(false).setAttribute(AppConstants.SES_ISSUE_LIST, issueList);
                resultType = SUCCESS;
            } catch (Exception exception) {
                LoggerUtility.log(logger, "Exception occurred in doSearchMyIssue method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
                 httpServletRequest.getSession(false).setAttribute(AppConstants.REQ_EXCEPTION_MSG, exception.getMessage());
                resultType = "error";
            }
        }
        return resultType;
    }

    public String searchTasksPrepare() throws Exception {
        resultType = LOGIN;
        if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
            HttpSession httpSession = getHttpServletRequest().getSession(false);
            try {
                httpSession.removeAttribute(AppConstants.SES_PAYMENT_LIST);
                httpSession.removeAttribute(AppConstants.SES_SHIPMENT_LIST);
                httpSession.removeAttribute(AppConstants.SES_DOC_LIST);
                httpSession.removeAttribute(AppConstants.SES_INV_LIST);
                httpSession.removeAttribute(AppConstants.SES_PO_LIST);
                httpSession.removeAttribute(AppConstants.SES_TRADINGPARTNER_LIST);
                httpSession.removeAttribute(AppConstants.SES_ISSUE_LIST);
                setUsersMap(DataSourceDataProvider.getInstance().getUsers());
                setPriorityMap(DataSourceDataProvider.getInstance().getPriority());
                setCategoryMap(DataSourceDataProvider.getInstance().getCategory());
                if (getSearchType().equals("MyTasks")) {
                    setFormAction("../issues/doSearchMyTasks.action");
                    httpServletRequest.setAttribute("searchType", "MyTasks");
                } else if (getSearchType().equals("AllTasks")) {
                    setFormAction("../issues/doSearchTasks.action");
                    httpServletRequest.setAttribute("searchType", "AllTasks");
                }
                resultType = SUCCESS;
            } catch (Exception exception) {
                LoggerUtility.log(logger, "Exception occurred in searchTasksPrepare method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
                 httpServletRequest.getSession(false).setAttribute(AppConstants.REQ_EXCEPTION_MSG, exception.getMessage());
                resultType = "error";
            }
        }
        return resultType;
    }

    public String doSearchMyTasks() throws Exception {
        resultType = LOGIN;
        if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
            try {
                prepare();
                if (getCheck() == null) {
                    setCheck("1");
                } else if (getCheck().equals("")) {
                    setCheck("1");
                }
                setSearchType("MyTasks");
                setFormAction("../issues/doSearchMyTasks.action");
                issueList = ServiceLocator.getIssuesService().getMyTasksList(this, getHttpServletRequest());
                httpServletRequest.getSession(false).setAttribute(AppConstants.SES_ISSUE_LIST, issueList);
                resultType = SUCCESS;
            } catch (Exception exception) {
                LoggerUtility.log(logger, "Exception occurred in doSearchMyTasks method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
                httpServletRequest.getSession(false).setAttribute(AppConstants.REQ_EXCEPTION_MSG, exception.getMessage());
                resultType = "error";
            }
        }
        return resultType;
    }

    public String doIssueEdit() throws Exception {
        resultType = LOGIN;
        if (getHttpServletRequest().getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
            try {
                prepare();
                String responseString = "";
                setUpdate("1");
                setFormAction("../issues/doIssueEdit.action");
                StringTokenizer st = new StringTokenizer(getAssignment(), ",");
                selectUsers = new TreeMap();
                while (st.hasMoreTokens()) {
                    String token = null;
                    token = st.nextToken();
                    selectUsers.put(token.trim(), DataSourceDataProvider.getInstance().getNameByLoginId(token.trim()));
                }
                setSelectUsers(selectUsers);
                responseString = ServiceLocator.getIssuesService().doIssueEdit(this, httpServletRequest);
                getHttpServletRequest().setAttribute(AppConstants.REQ_RESULT_MSG, responseString);
                setSearchType(getSearchType());
                resultType = SUCCESS;
            } catch (Exception exception) {
                LoggerUtility.log(logger, "Exception occurred in doIssueEdit method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
                httpServletRequest.getSession(false).setAttribute(AppConstants.REQ_EXCEPTION_MSG, exception.getMessage());
                resultType = "error";
            }
        }
        return resultType;
    }

    public String getSubmitFrm() {
        return submitFrm;
    }

    public Map getSelectUsers() {
        return selectUsers;
    }

    public void setSelectUsers(Map selectUsers) {
        this.selectUsers = selectUsers;
    }

    public void setSubmitFrm(String submitFrm) {
        this.submitFrm = submitFrm;
    }

    public void setServletRequest(HttpServletRequest reqObj) {
        this.setHttpServletRequest(reqObj);
    }

    public void setHttpServletRequest(HttpServletRequest httpServletRequest) {
        this.httpServletRequest = httpServletRequest;
    }

    public HttpServletRequest getHttpServletRequest() {
        return httpServletRequest;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getFileLocation() {
        return fileLocation;
    }

    public void setFileLocation(String fileLocation) {
        this.fileLocation = fileLocation;
    }

    public void setUpload(File file) {
        this.file = file;
    }

    public void setUploadContentType(String contentType) {
        this.contentType = contentType;
    }

    public void setUploadFileName(String filename) {
        this.filename = filename;
    }

    public Map getUsersMap() {
        return usersMap;
    }

    public void setUsersMap(Map usersMap) {
        this.usersMap = usersMap;
    }

    public Map getPriorityMap() {
        return priorityMap;
    }

    public void setPriorityMap(Map priorityMap) {
        this.priorityMap = priorityMap;
    }

    public Map getCategoryMap() {
        return categoryMap;
    }

    public void setCategoryMap(Map categoryMap) {
        this.categoryMap = categoryMap;
    }

    public void setAssignment(String assignment) {
        this.assignment = assignment;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAssignment() {
        return assignment;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserFlowMap() {
        return userFlowMap;
    }

    public void setUserFlowMap(String userFlowMap) {
        this.userFlowMap = userFlowMap;
    }

    public String getIssuedatepickerfrom() {
        return issuedatepickerfrom;
    }

    public void setIssuedatepickerfrom(String issuedatepickerfrom) {
        this.issuedatepickerfrom = issuedatepickerfrom;
    }

    public String getIssuedatepicker() {
        return issuedatepicker;
    }

    public void setIssuedatepicker(String issuedatepicker) {
        this.issuedatepicker = issuedatepicker;
    }

    public String getTicketId() {
        return ticketId;
    }

    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }

    public String getProject() {
        return project;
    }

    public String getUpdate() {
        return update;
    }

    public void setUpdate(String update) {
        this.update = update;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getFcategory() {
        return fcategory;
    }

    public void setFcategory(String fcategory) {
        this.fcategory = fcategory;
    }

    public String getFromtime() {
        return fromtime;
    }

    public void setFromtime(String fromtime) {
        this.fromtime = fromtime;
    }

    public String getTotime() {
        return totime;
    }

    public void setTotime(String totime) {
        this.totime = totime;
    }

    public String getCheck() {
        return check;
    }

    public void setCheck(String check) {
        this.check = check;
    }

    public String getFormAction() {
        return formAction;
    }

    public void setFormAction(String formAction) {
        this.formAction = formAction;
    }

    public String getSearchType() {
        return searchType;
    }

    public void setSearchType(String searchType) {
        this.searchType = searchType;
    }
}
