/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mss.ediscv.user;

import com.mss.ediscv.util.ConnectionProvider;
import com.mss.ediscv.util.DataSourceDataProvider;
import com.mss.ediscv.util.DateUtility;
import com.mss.ediscv.util.MailManager;
import com.mss.ediscv.util.PasswordUtil;
import com.mss.ediscv.util.ServiceLocatorException;
import com.mss.ediscv.util.WildCardSql;
import java.io.File;
import java.io.FileInputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Random;
import org.apache.log4j.Logger;

/**
 * @author miracle1
 */
public class UserServiceImpl implements UserService {

    private static Logger logger = Logger.getLogger(UserServiceImpl.class.getName());

    String responseString = null;
    
    Statement statement = null;
    ResultSet resultSet = null;
    ArrayList userList = null;
    UserBean userBean = null;
    String resultMessage;

    public String addUser(UserAction userAction) throws ServiceLocatorException {
        String fname = userAction.getFname();
        String lname = userAction.getLname();
        String email = userAction.getEmail();
        String ophno = userAction.getOphno();
        PasswordUtil passwordUtil = new PasswordUtil();
        String deptno = userAction.getDeptId();
        String status = userAction.getStatus();
        String role = userAction.getRole();
        String generatedPassword = passwordUtil.encryptPwd(generatePassword(8));
        String loginId = generateUserId(email);
        String createdBy = userAction.getCreatedBy();
        Timestamp activate_ts = DateUtility.getInstance().getCurrentDB2Timestamp();
        int i = 0;
        Connection connection = null;
        CallableStatement callableStatement = null;
        try {
            connection = ConnectionProvider.getInstance().getConnection();
            callableStatement = connection.prepareCall("call SPCREATEUSER(?,?,?,?,?, ?,?,?,?,?, ?,?,?,?,?, ?,?,?)");
            callableStatement.setString(1, loginId);
            callableStatement.setString(2, generatedPassword);
            callableStatement.setString(3, fname);
            callableStatement.setString(4, lname);
            callableStatement.setString(5, email);
            callableStatement.setString(6, ophno);
            callableStatement.setInt(7, Integer.parseInt(deptno));
            callableStatement.setString(8, status);
            callableStatement.setString(9, createdBy);
            callableStatement.setString(10, createdBy);
            callableStatement.setTimestamp(11, activate_ts);
            callableStatement.setInt(12, Integer.parseInt(role));
            callableStatement.setTimestamp(13, activate_ts);
            callableStatement.setString(14, createdBy);
            callableStatement.setInt(15, 1);
            callableStatement.setTimestamp(16, activate_ts);
            callableStatement.setTimestamp(17, activate_ts);
            callableStatement.registerOutParameter(18, Types.VARCHAR);
            int updatedRows = callableStatement.executeUpdate();
            responseString = "<font color='green'>" + callableStatement.getString(18) + "</font>";
            if (email != null && !email.equals("")) {
                MailManager sendMail = new MailManager();
                sendMail.sendUserIdPwd(loginId, fname + " " + lname, passwordUtil.decryptPwd(generatedPassword));
            }
        } catch (SQLException e) {
            responseString = "<font color='red'>Please try with different Id!</font>";
            e.printStackTrace();
        } catch (Exception ex) {
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
            } catch (SQLException se) {
                throw new ServiceLocatorException(se);
            }
        }
        return responseString;
    }

    public String generateUserId(String mailId) {
        int atOccurance = mailId.indexOf("@");
        return mailId.substring(0, atOccurance).toLowerCase();
    }

    public String generatePassword(int noOfCharacters) {
        String generatedPwd = "";
        Random random = new Random(System.currentTimeMillis());
        long randomOne = random.nextLong();
        long randomTwo = random.nextLong();
        String hashCodeOne = Long.toHexString(randomOne);
        String hashCodeTwo = Long.toHexString(randomTwo);
        generatedPwd = hashCodeOne + hashCodeTwo;
        if (generatedPwd.length() > noOfCharacters) {
            generatedPwd = generatedPwd.substring(0, noOfCharacters);
        }
        return generatedPwd.toUpperCase();
    }

    public boolean userCheckExist(UserAction userAction) throws ServiceLocatorException {
        boolean isUserExist = false;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            String loginId = generateUserId(userAction.getEmail());
            connection = ConnectionProvider.getInstance().getConnection();
            preparedStatement = connection.prepareStatement("SELECT LOGINID FROM M_USER where LOGINID =?");
            preparedStatement.setString(1, loginId);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                isUserExist = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception ex) {
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
            } catch (SQLException se) {
                throw new ServiceLocatorException(se);
            }
        }
        return isUserExist;
    }

    public int updateUserPwd(UserAction userAction) throws ServiceLocatorException {
        Connection connection = null;
        int updatedRows = 0;
        PasswordUtil passwordUtility = new PasswordUtil();
        connection = ConnectionProvider.getInstance().getConnection();
        String updateUserPwdQuery="";
        try {
            if (userAction.getNewPwd().equalsIgnoreCase(userAction.getConfirmPwd())) {
                String encryptPass = passwordUtility.encryptPwd(userAction.getNewPwd());
                updateUserPwdQuery = "UPDATE M_USER SET PASSWD='" + encryptPass + "' WHERE LOGINID='" + userAction.getLoginId() + "'";
                statement = connection.createStatement();
                updatedRows = statement.executeUpdate(updateUserPwdQuery);
            }
        } catch (SQLException sqle) {
            throw new ServiceLocatorException(sqle);
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                    statement = null;
                }
                if (connection != null) {
                    connection.close();
                    connection = null;
                }
            } catch (SQLException se) {
                throw new ServiceLocatorException(se);
            }
        }
        return updatedRows;
    }

    @Override
    public int updateMyPwd(UserAction userAction) throws ServiceLocatorException {
        int updatedRows = 0;
        String password = null;
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        PasswordUtil passwordUtility = new PasswordUtil();
        String updateMyPwdQuery = "SELECT LOGINID,PASSWD FROM M_USER WHERE LOGINID='" + userAction.getLoginId() + "'";
        try {
            connection = ConnectionProvider.getInstance().getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(updateMyPwdQuery);
            while (resultSet.next()) {
                password = resultSet.getString("PASSWD");
            }
            password = passwordUtility.decryptPwd(password);
            if (userAction.getOldPwd().equals(password)) {
                String encryptPass = passwordUtility.encryptPwd(userAction.getNewPwd());
                updateMyPwdQuery = "UPDATE M_USER SET PASSWD='" + encryptPass + "' WHERE LOGINID='" + userAction.getLoginId() + "'";
                statement = connection.createStatement();
                updatedRows = statement.executeUpdate(updateMyPwdQuery);
            } else {
                updatedRows = 100;
            }
        } catch (SQLException se) {
            throw new ServiceLocatorException(se);
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
            } catch (SQLException se) {
                throw new ServiceLocatorException(se);
            }
        }
        return updatedRows;
    }

    @Override
    public ArrayList getSearchUserList(UserAction userAction) throws ServiceLocatorException {
        StringBuilder userSearchQuery = new StringBuilder();
        String fname = userAction.getFname();
        String lname = userAction.getLname();
        String loginId = userAction.getLoginId();
        String status = userAction.getStatus();
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        userSearchQuery.append("SELECT ID,FNME,LNME,EMAIL,OFFICE_PHONE,ACTIVE,ROLE_ID,LOGINID FROM M_USER LEFT OUTER JOIN M_USER_ROLES on(M_USER_ROLES.USER_ID=M_USER.ID)");
        userSearchQuery.append(" WHERE 1=1");
        userSearchQuery.append(" AND M_USER_ROLES.ROLE_ID != 1");
        if (fname != null && !"".equals(fname.trim())) {
            userSearchQuery.append(WildCardSql.getWildCardSql1("FNME", fname.trim()));
        }
        if (lname != null && !"".equals(lname.trim())) {
            userSearchQuery.append(WildCardSql.getWildCardSql1("LNME", lname.trim()));
        }
        if (loginId != null && !"".equals(loginId.trim())) {
            userSearchQuery.append(WildCardSql.getWildCardSql1("LOGINID", loginId.trim()));
        }
        if (status != null && !"-1".equals(status.trim())) {
            userSearchQuery.append(WildCardSql.getWildCardSql1("ACTIVE", status.trim()));
        }
        String searchQuery = userSearchQuery.toString();
        try {
            connection = ConnectionProvider.getInstance().getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(searchQuery);
            userList = new ArrayList();
            while (resultSet.next()) {
                UserBean userBean = new UserBean();
                userBean.setName(resultSet.getString("FNME") + " " + resultSet.getString("LNME"));
                userBean.setEmail(resultSet.getString("EMAIL"));
                userBean.setOphno(resultSet.getString("OFFICE_PHONE"));
                userBean.setLoginId(resultSet.getString("LOGINID"));
                userBean.setId(resultSet.getString("ID"));
                if (resultSet.getString("ACTIVE").equals("A")) {
                    userBean.setStatus("Active");
                } else if (resultSet.getString("ACTIVE").equals("I")) {
                    userBean.setStatus("InActive");
                } else if (resultSet.getString("ACTIVE").equals("T")) {
                    userBean.setStatus("Terminated");
                } else {
                    userBean.setStatus("-");
                }
                userBean.setRoleId(DataSourceDataProvider.getInstance().getRoleNameByRoleId(resultSet.getString("ROLE_ID")));
                userList.add(userBean);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception ex) {
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
            } catch (SQLException se) {
                throw new ServiceLocatorException(se);
            }
        }
        return userList;
    }

    public UserAction editUser(UserAction userAction) throws ServiceLocatorException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = ConnectionProvider.getInstance().getConnection();
            preparedStatement = connection.prepareStatement("select ID,FNME,LNME,EMAIL,OFFICE_PHONE,DEPT_ID,ACTIVE,ROLE_ID  from M_USER LEFT OUTER JOIN M_USER_ROLES on (M_USER.ID=M_USER_ROLES.USER_ID) where M_USER.ID=?");
            preparedStatement.setString(1, userAction.getId());
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                userAction.setId(resultSet.getString("ID"));
                userAction.setFname(resultSet.getString("FNME"));
                userAction.setLname(resultSet.getString("LNME"));
                userAction.setEmail(resultSet.getString("EMAIL"));
                userAction.setOphno(resultSet.getString("OFFICE_PHONE"));
                userAction.setDeptId(resultSet.getString("DEPT_ID"));
                userAction.setStatus(resultSet.getString("ACTIVE"));
                userAction.setRole(resultSet.getString("ROLE_ID"));
            }
        } catch (SQLException e) {
            responseString = "<font color='red'>Please try Again!</font>";
            e.printStackTrace();
        } catch (Exception ex) {
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
            } catch (SQLException se) {
                throw new ServiceLocatorException(se);
            }
        }
        return userAction;
    }

    public String updateUser(UserAction userAction) throws ServiceLocatorException {
        Connection connection = null;
        CallableStatement callableStatement = null;
        String id = userAction.getId();
        String fname = userAction.getFname();
        String lname = userAction.getLname();
        String ophno = userAction.getOphno();
        String deptId = userAction.getDeptId();
        String status = userAction.getStatus();
        String roleId = userAction.getRole();
        String modified_BY = userAction.getCreatedBy();
        try {
            Timestamp modified_TS = DateUtility.getInstance().getCurrentDB2Timestamp();
            String deactivated_BY = null;
            Timestamp deactivated_TS = null;
            if (!status.equals("A")) {
                deactivated_BY = userAction.getCreatedBy();
                deactivated_TS = modified_TS;
            }
            connection = ConnectionProvider.getInstance().getConnection();
            callableStatement = connection.prepareCall("call SPUPDATEUSER(?,?,?,?,?, ?,?,?,?,?, ?,?)");
            callableStatement.setInt(1, Integer.parseInt(id));
            callableStatement.setString(2, fname);
            callableStatement.setString(3, lname);
            callableStatement.setString(4, ophno);
            callableStatement.setInt(5, Integer.parseInt(deptId));
            callableStatement.setString(6, status);
            callableStatement.setInt(7, Integer.parseInt(roleId));
            callableStatement.setString(8, modified_BY);
            callableStatement.setTimestamp(9, modified_TS);
            callableStatement.setString(10, deactivated_BY);
            callableStatement.setTimestamp(11, deactivated_TS);
            callableStatement.registerOutParameter(12, Types.VARCHAR);
            responseString = "<font color='green'>" + callableStatement.getString(12) + "</font>";
        } catch (SQLException e) {
            responseString = "<font color='red'>Please try with different Id!</font>";
            e.printStackTrace();
        } catch (Exception ex) {
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
            } catch (SQLException se) {
                throw new ServiceLocatorException(se);
            }
        }
        return responseString;
    }

    public UserBean userDetails(int userId) throws ServiceLocatorException {
        userBean = new UserBean();
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        String userDetailsQuery="";
        userDetailsQuery = "select LOGINID,ROLE_ID,concat(FNME ,LNME) as USERNAME   from M_USER LEFT OUTER JOIN M_USER_ROLES on (M_USER.ID=M_USER_ROLES.USER_ID) WHERE ID=" + userId;
        try {
            connection = ConnectionProvider.getInstance().getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(userDetailsQuery);
            userBean.setUserId(userId);
            while (resultSet.next()) {
                userBean.setLoginId(resultSet.getString("LOGINID"));
                userBean.setPrimaryRole(DataSourceDataProvider.getInstance().getRoleNameByRoleId(resultSet.getString("ROLE_ID")));
                userBean.setName(resultSet.getString("USERNAME"));
            }
            userBean.setPrimaryFlow(DataSourceDataProvider.getInstance().getPrimaryFlowID(userId));
        } catch (SQLException se) {
            throw new ServiceLocatorException(se);
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
            } catch (SQLException se) {
                throw new ServiceLocatorException(se);
            }
        }
        return userBean;
    }

    public int insertFlows(int[] assignedFlowIds, int employeeId, int primaryFlowId) throws ServiceLocatorException {
        int insertedRows = 0;
        int deletedRows = 0;
        Connection connection = null;
        Statement statement = null;
        String insertFlowsQuery="";
        try {
            connection = ConnectionProvider.getInstance().getConnection();
            statement = connection.createStatement();
            insertFlowsQuery = "DELETE FROM M_USER_FLOWS_ACTION WHERE USER_ID=" + employeeId;
            deletedRows = statement.executeUpdate(insertFlowsQuery);
            statement.close();
            statement = null;
            statement = connection.createStatement();
            for (int counter = 0; counter < assignedFlowIds.length; counter++) {
                if (assignedFlowIds[counter] == primaryFlowId) {
                    insertFlowsQuery = "Insert into M_USER_FLOWS_ACTION(PRIORITY,USER_ID,FLOWID) values(1," + employeeId + ", " + assignedFlowIds[counter] + ")";
                } else {
                    insertFlowsQuery = "Insert into M_USER_FLOWS_ACTION(PRIORITY,USER_ID,FLOWID) values(2," + employeeId + ", " + assignedFlowIds[counter] + ")";
                }
                insertedRows = statement.executeUpdate(insertFlowsQuery);
            }
        } catch (Exception e) {
            throw new ServiceLocatorException(e);
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                    statement = null;
                }
                if (connection != null) {
                    connection.close();
                    connection = null;
                }
            } catch (SQLException se) {
                throw new ServiceLocatorException(se);
            }
        }
        return insertedRows;
    }

    public UserBean userProfile(String userId, String loginId) throws ServiceLocatorException {
        System.out.println("user id is"+userId);
         System.out.println("login id is"+loginId);
        UserBean userBean = new UserBean();
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        String userProfileQuery="";
        try {
            System.out.println("Before Connection::"+DateUtility.getInstance().getCurrentDB2Timestamp());
            connection = ConnectionProvider.getInstance().getConnection();
            System.out.println("After Connection::"+DateUtility.getInstance().getCurrentDB2Timestamp());
            statement = connection.createStatement();
            userProfileQuery = "select * from M_USER WHERE ID=" + userId + " and LOGINID='" + loginId + "'";
           System.out.println("userProfile query:"+userProfileQuery.toString());
            resultSet = statement.executeQuery(userProfileQuery);
            userBean.setId(userId);
            userBean.setLoginId(loginId);
            System.out.println("Query and resultset start time::"+DateUtility.getInstance().getCurrentDB2Timestamp());
            while (resultSet.next()) {
                userBean.setName((resultSet.getString("FNME")) + "." + (resultSet.getString("LNME")));
                userBean.setEmail(resultSet.getString("EMAIL"));
                userBean.setGender(resultSet.getString("GENDER"));
                userBean.setLocation(resultSet.getString("LOCATION"));
                userBean.setDob(resultSet.getDate("DOB"));
                userBean.setDesignation(resultSet.getString("DESIGNATION"));
                userBean.setOrganization(resultSet.getString("ORGANIZATION"));
                userBean.setPhonenumber(resultSet.getString("OFFICE_PHONE"));
                userBean.setEducation(resultSet.getString("EDUCATION"));
            }
            System.out.println("resultset end time::"+DateUtility.getInstance().getCurrentDB2Timestamp());
        } catch (SQLException se) {
            throw new ServiceLocatorException(se);
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
            } catch (SQLException se) {
                throw new ServiceLocatorException(se);
            }
        }
        return userBean;
    }

    public int updateUserProfile(UserAction userAction, int userId) throws ServiceLocatorException {
        System.out.println("userId is "+userId);
        int updatedRows = 0;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        String updateUserProfileQuery="";
        updateUserProfileQuery = "UPDATE M_USER set EDUCATION=?,DESIGNATION=?,LOCATION=?,ORGANIZATION=?,DOB=?,GENDER=?,OFFICE_PHONE=? where Id=" + userId;
        try {
            connection = ConnectionProvider.getInstance().getConnection();
            preparedStatement = connection.prepareStatement(updateUserProfileQuery);
            preparedStatement.setString(1, userAction.getEducation());
            preparedStatement.setString(2, userAction.getDesignation());
            preparedStatement.setString(3, userAction.getLocation());
            preparedStatement.setString(4, userAction.getOrganization());
            preparedStatement.setDate(5, userAction.getDob());
            preparedStatement.setString(6, userAction.getGender());
            preparedStatement.setString(7, userAction.getPhonenumber());
            updatedRows = preparedStatement.executeUpdate();
        } catch (SQLException se) {
            throw new ServiceLocatorException(se);
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                    preparedStatement = null;
                }
                if (connection != null) {
                    connection.close();
                    connection = null;
                }
            } catch (SQLException se) {
                throw new ServiceLocatorException(se);
            }
        }
        return updatedRows;
    }

    @Override
    public int uploadImage(File imageUpdate, int userId) throws ServiceLocatorException {
        String uploadImageQuery = "UPDATE MSCVP.M_USER SET IMAGE = ? WHERE ID=?";
        int n = 0;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = ConnectionProvider.getInstance().getConnection();
            preparedStatement = connection.prepareStatement(uploadImageQuery);
            FileInputStream is = new FileInputStream(imageUpdate);
            preparedStatement.setBinaryStream(1, is, (int) imageUpdate.length());
            preparedStatement.setInt(2, userId);
            n = preparedStatement.executeUpdate();
        } catch (Exception se) {
            se.printStackTrace();
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                    preparedStatement = null;
                }
                if (connection != null) {
                    connection.close();
                    connection = null;
                }
            } catch (SQLException se) {
                throw new ServiceLocatorException(se);
            }
        }
        return n;
    }
}
