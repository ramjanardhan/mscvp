package com.mss.ediscv.general;

import com.mss.ediscv.util.AppConstants;
import com.mss.ediscv.util.ConnectionProvider;
import com.mss.ediscv.util.DataSourceDataProvider;
import com.mss.ediscv.util.DateUtility;
import com.mss.ediscv.util.LDAPAuthenticationService;
import com.mss.ediscv.util.PasswordUtil;
import com.mss.ediscv.util.ServiceLocator;
import com.mss.ediscv.util.ServiceLocatorException;
import com.opensymphony.xwork2.ActionSupport;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import javax.naming.directory.Attributes;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import com.mss.ediscv.util.LoggerUtility;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.apache.struts2.interceptor.ServletRequestAware;

public class LoginAction extends ActionSupport implements ServletRequestAware {

    private static Logger logger = LogManager.getLogger(LoginAction.class.getName());
    private String resultType = SUCCESS;
    private HttpServletRequest httpServletRequest;
    private String loginId;
    private String password;
    private String changeDb;

    @Override
    public String execute() throws Exception {
        UserInfoBean userInfoBean = null;
        byte[] imgData = null;
        try {
            GeneralService generalService = ServiceLocator.getGeneralService();
            HttpSession userSession = httpServletRequest.getSession(true);
            String dsnName = com.mss.ediscv.util.Properties.getProperty(AppConstants.PROP_PROD_DS_NAME);
            Attributes attributes = LDAPAuthenticationService.authenticate(getLoginId(), getPassword());
            if (attributes == null) {
                getHttpServletRequest().setAttribute(AppConstants.REQ_ERROR_INFO, "<font color=\"red\" size=\"2\">Invalid Credintials Please Contact admin!</font>");
                resultType = INPUT;
            } else {
                userInfoBean = generalService.getUserInfo(getLoginId().trim().toLowerCase(), dsnName);
                if (userInfoBean != null) {
                    String decryptedPwd = PasswordUtil.decryptPwd(userInfoBean.getPassword().trim());
                    if (decryptedPwd.equals(getPassword())) {
                        if ("A".equals(userInfoBean.getActiveFlag())) {
                            Map<Integer, Integer> userRolesMap = new HashMap<Integer, Integer>();
                            userRolesMap = generalService.getUserRoles(userInfoBean.getUserId(), dsnName);
                            String primaryRole = com.mss.ediscv.util.Properties.getProperty(AppConstants.PROP_USER_DEF_ROLE);
                            if (userRolesMap.get(1) != null) {
                                primaryRole = String.valueOf(userRolesMap.get(1));
                            }
                            String primaryFlowId = DataSourceDataProvider.getInstance().getPrimaryFlowID(userInfoBean.getUserId());
                            if (primaryFlowId != null) {
                                userSession.setAttribute(AppConstants.SES_ROLE_ID, primaryRole);
                                userSession.setAttribute(AppConstants.SES_USER_ID, userInfoBean.getUserId());
                                userSession.setAttribute(AppConstants.SES_LOGIN_ID, userInfoBean.getLoginId());
                                userSession.setAttribute(AppConstants.SES_USER_NAME, userInfoBean.getFirstName() + " " + userInfoBean.getLastName());
                                userSession.setAttribute(AppConstants.SES_LAST_LOGIN_TS, userInfoBean.getLastLoginTS().toString());
                                userSession.setAttribute(AppConstants.SES_FIRST_DB, "Production Data");
                                userSession.setAttribute(AppConstants.PROP_CURRENT_DS_NAME, com.mss.ediscv.util.Properties.getProperty(AppConstants.PROP_PROD_DS_NAME));
                                userSession.setAttribute(AppConstants.SES_EMAIL_ID, userInfoBean.getMailId());
                                Map usrFlowMap = DataSourceDataProvider.getInstance().getFlows(userInfoBean.getUserId());
                                userSession.setAttribute(AppConstants.SES_USER_FLOW_MAP, usrFlowMap);
                                userSession.setAttribute(AppConstants.SES_USER_ROLE_NAME, DataSourceDataProvider.getInstance().getRoleNameByRoleId(primaryRole));
                                userSession.setAttribute(AppConstants.SES_STATES_MAP, DataSourceDataProvider.getInstance().getStates());
                                String Resulttype = "input";
                                setResultType(DataSourceDataProvider.getInstance().getFlowNameByFlowID(primaryFlowId));
                                userSession.setAttribute(AppConstants.SES_USER_DEFAULT_FLOWID, primaryFlowId);
                                userSession.setAttribute(AppConstants.MSCVPROLE, DataSourceDataProvider.getInstance().getRoleNameByRoleId(primaryRole));
                                if (userInfoBean.getUserImage() != null) {
                                    imgData = userInfoBean.getUserImage();
                                }
                                userSession.setAttribute(AppConstants.SESSION_USER_IMAGE, imgData);
                            } else {
                                setResultType(INPUT);
                                httpServletRequest.setAttribute(AppConstants.REQ_ERROR_INFO, "<span class=\"resultFailure\"><b>Access Denied, Please contact Admin! </b></span>");
                            }
                        } else {
                            httpServletRequest.setAttribute(AppConstants.REQ_ERROR_INFO, "<span class=\"resultFailure\"><b>Sorry! Your account was InActive, Please contact Admin! </b></span>");
                            setResultType(INPUT);
                        }
                    } else {
                        httpServletRequest.setAttribute(AppConstants.REQ_ERROR_INFO, "<b>Please Login with valid UserId and Password! </b>");
                        setResultType(INPUT);
                    }
                } else {
                    httpServletRequest.setAttribute(AppConstants.REQ_ERROR_INFO, "<span class=\"resultFailure\"><b>Please Login with valid UserId and Password! </b></span>");
                    setResultType(INPUT);
                }
                if (getResultType().equals("input")) {
                    httpServletRequest.getSession(false).removeAttribute(AppConstants.SES_LOGIN_ID);
                }
            }
        } catch (Exception exception) {
            setResultType(ERROR);
             httpServletRequest.getSession(false).setAttribute("exceptionMessage", "Unable to connect Database Please contact System Admin!");
            LoggerUtility.log(logger, "Exception occurred in execute method:: " + exception.getMessage(), Level.ERROR, exception.getCause());

        }
        return getResultType();
    }

    public void logUserAccess() throws Exception {
        try {
            if (getHttpServletRequest().getSession(false).getAttribute(AppConstants.SES_LOGIN_ID) != null) {
                String UserId = getHttpServletRequest().getSession(false).getAttribute(AppConstants.SES_LOGIN_ID).toString();
                String forwarded = httpServletRequest.getHeader("X-FORWARDED-FOR");
                String via = httpServletRequest.getHeader("VIA");
                String remote = httpServletRequest.getRemoteAddr();
                String agent = httpServletRequest.getHeader("User-Agent");
                String location = httpServletRequest.getLocalAddr();
                Timestamp accessedtime = DateUtility.getInstance().getCurrentDB2Timestamp();
                Connection connection = null;
                Statement stmt = null;
                boolean isInserted = false;
                String query = null;
                try {
                    connection = ConnectionProvider.getInstance().getConnection();
                    query = "insert into LOGUSERACCESS(LoginId,X_FORWARDED_FOR1,VIA, REMOTE_ADDR,User_Agent,DateAccessed)" + " values('" + UserId + "','" + forwarded + "','" + via + "','" + remote + "','" + agent + "','" + accessedtime + "')";
                    stmt = connection.createStatement();
                    int x = stmt.executeUpdate(query);
                    stmt.close();
                    if (x > 0) {
                        isInserted = true;
                    }
                } catch (SQLException sqlException) {
                    LoggerUtility.log(logger, "SQLException occurred in logUserAccess method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
                } finally {
                    try {
                        if (stmt != null) {
                            stmt.close();
                            stmt = null;
                        }
                        if (connection != null) {
                            connection.close();
                            connection = null;
                        }
                    } catch (SQLException sqle) {
                        throw new ServiceLocatorException(sqle);
                    }
                }
            }
        } catch (Exception exception) {
            LoggerUtility.log(logger, "Exception occurred in logUserAccess method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
            resultType = ERROR;
        }
    }

    public String switchDB() throws Exception {
        HttpSession userSession = httpServletRequest.getSession(false);
        try {
            String db = userSession.getAttribute(AppConstants.SES_FIRST_DB).toString();
            if (db.startsWith("Ar")) {
                userSession.setAttribute(AppConstants.PROP_CURRENT_DS_NAME, com.mss.ediscv.util.Properties.getProperty(AppConstants.PROP_PROD_DS_NAME));
                userSession.setAttribute(AppConstants.SES_FIRST_DB, "Production Data");
            } else {
                userSession.setAttribute(AppConstants.PROP_CURRENT_DS_NAME, com.mss.ediscv.util.Properties.getProperty(AppConstants.PROP_ARCH_DS_NAME));
                userSession.setAttribute(AppConstants.SES_FIRST_DB, "Archive Data");
            }
        } catch (Exception exception) {
            LoggerUtility.log(logger, "Exception occurred in switchDB method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
        }
        return "success";
    }

    public String doLogout() throws Exception {
        try {
            if (httpServletRequest.getSession(false) != null) {
                httpServletRequest.getSession(false).invalidate();
            }
            setResultType(SUCCESS);
        } catch (Exception exception) {
            LoggerUtility.log(logger, "Exception occurred in doLogout method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
            httpServletRequest.getSession(false).setAttribute(AppConstants.REQ_ERROR_INFO, exception.toString());
            setResultType(ERROR);
        }
        return getResultType();
    }

    public void setServletRequest(HttpServletRequest reqObj) {
        this.setHttpServletRequest(reqObj);
    }

    public void setResultType(String resultType) {
        this.resultType = resultType;
    }

    public String getResultType() {
        return resultType;
    }

    public void setHttpServletRequest(HttpServletRequest httpServletRequest) {
        this.httpServletRequest = httpServletRequest;
    }

    public HttpServletRequest getHttpServletRequest() {
        return httpServletRequest;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setChangeDb(String changeDb) {
        this.changeDb = changeDb;
    }

    public String getChangeDb() {
        return changeDb;
    }
}
