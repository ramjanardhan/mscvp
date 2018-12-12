/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mss.ediscv.issues;

import com.mss.ediscv.tradingPartner.TradingPartnerServiceImpl;
import com.mss.ediscv.util.*;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.io.FileUtils;
import com.mss.ediscv.util.LoggerUtility;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author miracle
 */
public class IssuesServiceImpl implements IssuesService {

    private static Logger logger = LogManager.getLogger(IssuesServiceImpl.class.getName());
    int callableStatementUpdateCount;
    private ArrayList<IssueBean> issueList;
    String tmp_Recieved_From = "";
    String tmp_Recieved_ToTime = "";
    String responseString = null;

    public String doCreateIssue(IssuesAction issuesAction, String fileName, String ContentType, File path, HttpServletRequest httpServletRequest) throws ServiceLocatorException {
        String proPath = null;
        String size = null;
        Connection connection = null;
        CallableStatement callableStatement = null;
        if (fileName != null && ContentType != null & path.length() > 0) {
            proPath = com.mss.ediscv.util.Properties.getProperty("mscvp.issueTrackFilePath");
            long bytes = path.length();
            long kilobytes = (bytes / 1024);
            size = kilobytes + " KB";
            File destPath = new File(proPath);
            if (!destPath.exists()) {
                destPath.mkdirs();
            }
            proPath = proPath + "\\" + fileName;
            try {
                FileUtils.copyFile(path, new File(proPath));
            } catch (IOException ioException) {
                LoggerUtility.log(logger, "Exception occurred in doCreateIssue method:: " + ioException.getMessage(), Level.ERROR, ioException.getCause());

            }
        }
        try {
            connection = ConnectionProvider.getInstance().getConnection();
            callableStatement = connection.prepareCall("call spCreateIssue(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
            callableStatement.setString(1, issuesAction.getAssignment());
            callableStatement.setString(2, issuesAction.getCategory());
            callableStatement.setString(3, issuesAction.getPriority());
            callableStatement.setString(4, issuesAction.getGroup());
            callableStatement.setString(5, issuesAction.getSummary());
            callableStatement.setString(6, issuesAction.getDesc());
            callableStatement.setString(7, issuesAction.getTime());
            callableStatement.setString(8, issuesAction.getStatus());
            callableStatement.setString(9, fileName);
            callableStatement.setString(10, ContentType);
            callableStatement.setString(11, proPath);
            callableStatement.setString(12, httpServletRequest.getSession(false).getAttribute(AppConstants.SES_LOGIN_ID).toString());
            callableStatement.setString(13, httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME).toString());
            callableStatement.setString(14, "email");
            callableStatement.setString(15, issuesAction.getUserFlowMap());
            callableStatement.setString(16, issuesAction.getFcategory());
            callableStatement.setString(17, size);
            callableStatement.registerOutParameter(18, Types.VARCHAR);
            int updatedRows = callableStatement.executeUpdate();
            MailManager.sendCreatedIssueDetails(httpServletRequest.getSession(false).getAttribute(AppConstants.SES_LOGIN_ID).toString(), issuesAction.getAssignment(), DataSourceDataProvider.getInstance().getCategoryById(new Integer(issuesAction.getCategory())), DataSourceDataProvider.getInstance().getPriorityById(new Integer(issuesAction.getPriority())), issuesAction.getTime(), issuesAction.getSummary(), issuesAction.getDesc());
            responseString = "<font color='green'>" + callableStatement.getString(18) + "</font>";
        } catch (SQLException sqlException) {
            LoggerUtility.log(logger, "SQLException occurred in doCreateIssue method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
        } catch (Exception exception) {
            LoggerUtility.log(logger, "Exception occurred in doCreateIssue method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
        } finally {
            try {
                if (callableStatement != null) {
                    callableStatement.close();
                    callableStatement = null;
                }
                if (connection != null) {
                    connection.close();
                    connection = null;
                }
            } catch (SQLException sqlException) {
                LoggerUtility.log(logger, "SQLException occurred in doCreateIssue method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
            }

        }
        return responseString;
    }

    public ArrayList<IssueBean> buildIssueQuery(IssuesAction issueAction, HttpServletRequest httpServletRequest) throws ServiceLocatorException {
        String category = issueAction.getCategory();
        String pripority = issueAction.getPriority();
        String assignment = issueAction.getAssignment();
        String tid = issueAction.getTicketId();
        String combinedTokens = "";
        String fromdate = issueAction.getIssuedatepickerfrom();
        String todate = issueAction.getIssuedatepicker();
        String tokenString = null;
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        if (!(assignment == null || assignment.equals("-1") || "".equals(assignment.trim()))) {
            StringTokenizer st = new StringTokenizer(assignment, ",");
            while (st.hasMoreTokens()) {
                combinedTokens = combinedTokens + "'" + st.nextToken() + "',";
            }
            tokenString = combinedTokens.substring(0, combinedTokens.length() - 1);
        }
        StringBuilder issueSearchQuery = new StringBuilder();
        issueSearchQuery.append("SELECT DISTINCT MSCVP_ISSUE.ISS_ID AS ID,MSCVP_ISSUE_ASSIGNED.IA_ASSIGNTO AS ASSIGNTO,"
                + "MSCVP_ISSUE.ISS_CATEGORY AS CATEGORY,MSCVP_ISSUE.ISS_PRIORITY AS PRIORITY,"
                + "MSCVP_ISSUE.ISS_CREATED_DATE AS CREATED_DATE,MSCVP_ISSUE.ISS_DEVELOPER_EST_TIME AS TIME,"
                + "MSCVP_ISSUE_ATTACHMENT.IAT_STATUS AS STATUS FROM mscvp_issue  left outer join mscvp_issue_assigned "
                + "on (ISS_ID=IA_ISS_ID),mscvp_issue_attachment");
        issueSearchQuery.append(" WHERE 1=1 ");
        if (category != null && !"-1".equals(category.trim())) {
            issueSearchQuery.append(WildCardSql.getWildCardSql1("MSCVP_ISSUE.ISS_CATEGORY", category.trim()));
        }
        if (pripority != null && !"-1".equals(pripority.trim())) {
            issueSearchQuery.append(WildCardSql.getWildCardSql1("MSCVP_ISSUE.ISS_PRIORITY", pripority.trim()));
        }
        if (assignment != null && !"".equals(assignment)) {
            issueSearchQuery.append(WildCardSql.getWildCardSqlIN("MSCVP_ISSUE_ASSIGNED.IA_ASSIGNTO", tokenString.trim()));
        }
        if (tid != null && !"".equals(tid.trim())) {
            issueSearchQuery.append(WildCardSql.getWildCardSql1("MSCVP_ISSUE.ISS_ID", tid.trim()));
        }
        if (todate != null && !"".equals(todate)) {
            tmp_Recieved_From = DateUtility.getInstance().DateViewToDBCompare(todate);
            issueSearchQuery.append(" AND MSCVP_ISSUE.ISS_CREATED_DATE <= '" + tmp_Recieved_From + "'");
        }
        if (fromdate != null && !"".equals(fromdate)) {
            tmp_Recieved_From = DateUtility.getInstance().DateViewToDBCompare(fromdate);
            issueSearchQuery.append(" AND MSCVP_ISSUE.ISS_CREATED_DATE >= '" + tmp_Recieved_From + "'");
        }
        String searchQuery = issueSearchQuery.toString();
        httpServletRequest.getSession(false).setAttribute(AppConstants.SES_ISSUE_QUERY, searchQuery);
        try {
            connection = ConnectionProvider.getInstance().getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(searchQuery);
            issueList = new ArrayList<IssueBean>();
            while (resultSet.next()) {
                IssueBean issueBean = new IssueBean();
                issueBean.setCategory(DataSourceDataProvider.getInstance().getCategoryById(new Integer(resultSet.getString("CATEGORY"))));
                issueBean.setAssignTo(resultSet.getString("ASSIGNTO"));
                issueBean.setCreated_date(resultSet.getTimestamp("CREATED_DATE"));
                issueBean.setDevEstTime(resultSet.getString("TIME"));
                issueBean.setPriority(DataSourceDataProvider.getInstance().getPriorityById(new Integer(resultSet.getString("PRIORITY"))));
                issueBean.setStatus(resultSet.getString("STATUS"));
                issueBean.setId(resultSet.getInt("ID"));
                issueList.add(issueBean);
            }
        } catch (SQLException sqlException) {
            LoggerUtility.log(logger, "SQLException occurred in buildIssueQuery method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
        } catch (Exception exception) {
            LoggerUtility.log(logger, "Exception occurred in buildIssueQuery method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                    resultSet = null;
                }
                if (statement != null) {
                    statement.close();
                    statement = null;
                }

                if (connection != null) {
                    connection.close();
                    connection = null;
                }
            } catch (SQLException sqlException) {
                LoggerUtility.log(logger, "SQLException occurred in buildIssueQuery method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
            }
        }
        return issueList;
    }

    public IssuesAction issueEdit(IssuesAction issuesAction) throws ServiceLocatorException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = ConnectionProvider.getInstance().getConnection();
            preparedStatement = connection.prepareStatement("Select  miat.iat_status as status,mia.IA_ASSIGNTO as assignto,mi.ISSU_DESCRIPTION as desc"
                    + ",mi.ISS_CATEGORY as category,mi.ISS_DEVELOPER_EST_TIME as time,mi.ISS_USER_GROUP as group,mi.ISS_PRIORITY as priority,mi.ISS_SUMMARY as summary "
                    + "from mscvp_issue mi left outer join mscvp_issue_assigned mia "
                    + "on (mi.ISS_ID=mia.IA_ISS_ID),mscvp_issue_attachment miat where iss_id = ? and miat.IAT_ISS_ID = ?");
            preparedStatement.setInt(1, issuesAction.getId());
            preparedStatement.setInt(2, issuesAction.getId());
            resultSet = preparedStatement.executeQuery();
            Map assign = new TreeMap();
            while (resultSet.next()) {
                issuesAction.setStatus(resultSet.getString("status"));
                assign.put(resultSet.getString("assignto"), DataSourceDataProvider.getInstance().getNameByLoginId(resultSet.getString("assignto")));
                issuesAction.setDesc(resultSet.getString("desc"));
                issuesAction.setCategory(resultSet.getString("category"));
                issuesAction.setTime(resultSet.getString("time"));
                issuesAction.setGroup(resultSet.getString("group"));
                issuesAction.setPriority(resultSet.getString("priority"));
                issuesAction.setSummary(resultSet.getString("summary"));
            }

            issuesAction.setSelectUsers(assign);
        } catch (SQLException sqlException) {
            LoggerUtility.log(logger, "SQLException occurred in issueEdit method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
            responseString = "<font color='red'>Please try Again!</font>";
        } catch (Exception exception) {
            LoggerUtility.log(logger, "Exception occurred in issueEdit method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
            responseString = "<font color='red'>Please try later!</font>";
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                    resultSet = null;
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                    preparedStatement = null;
                }
                if (connection != null) {
                    connection.close();
                    connection = null;
                }
            } catch (SQLException sqlException) {
                LoggerUtility.log(logger, "SQLException occurred in issueEdit method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
            }
        }
        return issuesAction;
    }

    public String doIssueEdit(IssuesAction issuesAction, HttpServletRequest httpServletRequest) throws ServiceLocatorException {
        Connection connection = null;
        CallableStatement callableStatement = null;
        try {
            connection = ConnectionProvider.getInstance().getConnection();
            callableStatement = connection.prepareCall("call spUpdateIssue(?,?,?,?,?,?,?,?,?,?,?,?,?)");
            callableStatement.setTimestamp(1, DateUtility.getInstance().getCurrentDB2Timestamp());
            callableStatement.setTimestamp(2, DateUtility.getInstance().getCurrentDB2Timestamp());
            callableStatement.setTimestamp(3, DateUtility.getInstance().getCurrentDB2Timestamp());
            callableStatement.setString(4, httpServletRequest.getSession(false).getAttribute(AppConstants.SES_LOGIN_ID).toString());
            callableStatement.setString(5, issuesAction.getCategory());
            callableStatement.setString(6, issuesAction.getPriority());
            callableStatement.setString(7, issuesAction.getSummary());
            callableStatement.setString(8, issuesAction.getDesc());
            callableStatement.setString(9, issuesAction.getTime());
            callableStatement.setString(10, issuesAction.getAssignment());
            callableStatement.setString(11, issuesAction.getStatus());
            callableStatement.setInt(12, issuesAction.getId());
            callableStatement.registerOutParameter(13, Types.VARCHAR);
            responseString = "<font color='green'>" + callableStatement.getString(13) + "</font>";
        } catch (SQLException sqlException) {
            LoggerUtility.log(logger, "SQLException occurred in doIssueEdit method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
            responseString = "<font color='red'>Please try Again!</font>";
        } catch (Exception exception) {
            LoggerUtility.log(logger, "Exception occurred in doIssueEdit method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
            responseString = "<font color='red'>Please try later!</font>";
        } finally {
            try {
                if (callableStatement != null) {
                    callableStatement.close();
                    callableStatement = null;
                }
                if (connection != null) {
                    connection.close();
                    connection = null;
                }
            } catch (SQLException sqlException) {
                LoggerUtility.log(logger, "SQLException occurred in doIssueEdit method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
            }
        }
        return responseString;
    }

    public ArrayList<IssueBean> getMyIssueList(IssuesAction issueAction, HttpServletRequest httpServletRequest) throws ServiceLocatorException {
        String fromdate = issueAction.getIssuedatepickerfrom();
        String todate = issueAction.getIssuedatepicker();
        String createdBy = httpServletRequest.getSession(false).getAttribute(AppConstants.SES_LOGIN_ID).toString();
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        StringBuilder issueSearchQuery = new StringBuilder();
        issueSearchQuery.append("SELECT DISTINCT MSCVP_ISSUE.ISS_ID AS ID,MSCVP_ISSUE_ASSIGNED.IA_ASSIGNTO AS ASSIGNTO,"
                + "MSCVP_ISSUE.ISS_CATEGORY AS CATEGORY,MSCVP_ISSUE.ISS_PRIORITY AS PRIORITY,"
                + "MSCVP_ISSUE.ISS_CREATED_DATE AS CREATED_DATE,MSCVP_ISSUE.ISS_DEVELOPER_EST_TIME AS TIME,"
                + "MSCVP_ISSUE_ATTACHMENT.IAT_STATUS AS STATUS FROM mscvp_issue  left outer join mscvp_issue_assigned "
                + "on (mscvp_issue.ISS_ID=mscvp_issue_assigned.IA_ISS_ID),mscvp_issue_attachment");
        issueSearchQuery.append(" WHERE 1=1 AND MSCVP_ISSUE.ISS_CUSTOMER_ID='" + createdBy + "'");
        if (todate != null && !"".equals(todate)) {
            tmp_Recieved_From = DateUtility.getInstance().DateViewToDBCompare(todate);
            issueSearchQuery.append(" AND MSCVP_ISSUE.ISS_CREATED_DATE <= '" + tmp_Recieved_From + "'");
        }
        if (fromdate != null && !"".equals(fromdate)) {
            tmp_Recieved_From = DateUtility.getInstance().DateViewToDBCompare(fromdate);
            issueSearchQuery.append(" AND MSCVP_ISSUE.ISS_CREATED_DATE >= '" + tmp_Recieved_From + "'");
        }
        String searchQuery = issueSearchQuery.toString();
        httpServletRequest.getSession(false).setAttribute(AppConstants.SES_ISSUE_QUERY, searchQuery);
        try {
            connection = ConnectionProvider.getInstance().getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(searchQuery);
            issueList = new ArrayList<IssueBean>();
            int i = 0;
            while (resultSet.next()) {
                IssueBean issueBean = new IssueBean();
                issueBean.setCategory(DataSourceDataProvider.getInstance().getCategoryById(new Integer(resultSet.getString("CATEGORY"))));
                issueBean.setAssignTo(resultSet.getString("ASSIGNTO"));
                issueBean.setCreated_date(resultSet.getTimestamp("CREATED_DATE"));
                issueBean.setDevEstTime(resultSet.getString("TIME"));
                issueBean.setPriority(DataSourceDataProvider.getInstance().getPriorityById(new Integer(resultSet.getString("PRIORITY"))));
                issueBean.setStatus(resultSet.getString("STATUS"));
                issueBean.setId(resultSet.getInt("ID"));
                issueList.add(issueBean);
            }
        } catch (SQLException sqlException) {
            LoggerUtility.log(logger, "SQLException occurred in getMyIssueList method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
        } catch (Exception exception) {
            LoggerUtility.log(logger, "Exception occurred in getMyIssueList method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                    resultSet = null;
                }
                if (statement != null) {
                    statement.close();
                    statement = null;
                }
                if (connection != null) {
                    connection.close();
                    connection = null;
                }
            } catch (SQLException sqlException) {
                LoggerUtility.log(logger, "SQLException occurred in getMyIssueList method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
            }
        }
        return issueList;
    }

    public ArrayList<IssueBean> getMyTasksList(IssuesAction issueAction, HttpServletRequest httpServletRequest) throws ServiceLocatorException {
        String fromdate = issueAction.getIssuedatepickerfrom();
        String todate = issueAction.getIssuedatepicker();
        String createdBy = httpServletRequest.getSession(false).getAttribute(AppConstants.SES_LOGIN_ID).toString();
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        StringBuilder issueSearchQuery = new StringBuilder();
        issueSearchQuery.append("SELECT DISTINCT MSCVP_ISSUE.ISS_ID AS ID,MSCVP_ISSUE_ASSIGNED.IA_ASSIGNTO AS ASSIGNTO,"
                + "MSCVP_ISSUE.ISS_CATEGORY AS CATEGORY,MSCVP_ISSUE.ISS_PRIORITY AS PRIORITY,"
                + "MSCVP_ISSUE.ISS_CREATED_DATE AS CREATED_DATE,MSCVP_ISSUE.ISS_DEVELOPER_EST_TIME AS TIME,"
                + "MSCVP_ISSUE_ATTACHMENT.IAT_STATUS AS STATUS FROM mscvp_issue  left outer join mscvp_issue_assigned "
                + "on (mscvp_issue.ISS_ID=mscvp_issue_assigned.IA_ISS_ID),mscvp_issue_attachment");
        issueSearchQuery.append(" WHERE 1=1 AND MSCVP_ISSUE_ASSIGNED.IA_ASSIGNTO ='" + createdBy + "'");
        if (todate != null && !"".equals(todate)) {
            tmp_Recieved_From = DateUtility.getInstance().DateViewToDBCompare(todate);
            issueSearchQuery.append(" AND MSCVP_ISSUE.ISS_CREATED_DATE <= '" + tmp_Recieved_From + "'");
        }
        if (fromdate != null && !"".equals(fromdate)) {
            tmp_Recieved_From = DateUtility.getInstance().DateViewToDBCompare(fromdate);
            issueSearchQuery.append(" AND MSCVP_ISSUE.ISS_CREATED_DATE >= '" + tmp_Recieved_From + "'");
        }
        String searchQuery = issueSearchQuery.toString();
        httpServletRequest.getSession(false).setAttribute(AppConstants.SES_ISSUE_QUERY, searchQuery);
        try {
            connection = ConnectionProvider.getInstance().getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(searchQuery);
            issueList = new ArrayList<IssueBean>();
            int i = 0;
            while (resultSet.next()) {
                IssueBean issueBean = new IssueBean();
                issueBean.setCategory(DataSourceDataProvider.getInstance().getCategoryById(new Integer(resultSet.getString("CATEGORY"))));
                issueBean.setAssignTo(resultSet.getString("ASSIGNTO"));
                issueBean.setCreated_date(resultSet.getTimestamp("CREATED_DATE"));
                issueBean.setDevEstTime(resultSet.getString("TIME"));
                issueBean.setPriority(DataSourceDataProvider.getInstance().getPriorityById(new Integer(resultSet.getString("PRIORITY"))));
                issueBean.setStatus(resultSet.getString("STATUS"));
                issueBean.setId(resultSet.getInt("ID"));
                issueList.add(issueBean);
            }
        } catch (SQLException sqlException) {
            LoggerUtility.log(logger, "SQLException occurred in getMyTasksList method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
        } catch (Exception exception) {
            LoggerUtility.log(logger, "Exception occurred in getMyTasksList method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                    resultSet = null;
                }
                if (statement != null) {
                    statement.close();
                    statement = null;
                }
                if (connection != null) {
                    connection.close();
                    connection = null;
                }
            } catch (SQLException sqlException) {
                LoggerUtility.log(logger, "SQLException occurred in getMyTasksList method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
            }

        }
        return issueList;
    }
}
