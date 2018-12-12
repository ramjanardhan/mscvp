package com.mss.ediscv.general;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import com.mss.ediscv.util.LoggerUtility;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.mss.ediscv.util.ConnectionProvider;
import com.mss.ediscv.util.ServiceLocatorException;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import org.apache.commons.io.IOUtils;

public class GeneralServiceImpl implements GeneralService {

    private static Logger logger = LogManager.getLogger(GeneralServiceImpl.class.getName());

    public GeneralServiceImpl() {
    }

    public UserInfoBean getUserInfo(String loginId, String dsnName) throws ServiceLocatorException {
        UserInfoBean userInfoBean = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String userInfoQuery = "SELECT ID,LOGINID,PASSWD,FNME,LNME,EMAIL,DEPT_ID,ACTIVE,LAST_LOGIN_TS,LAST_LOGOUT_TS,IMAGE FROM M_USER WHERE LOGINID=?";
        try {
            connection = ConnectionProvider.getInstance().getConnection();
            preparedStatement = connection.prepareStatement(userInfoQuery);
            preparedStatement.setString(1, loginId);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                userInfoBean = new UserInfoBean();
                userInfoBean.setUserId(resultSet.getInt("ID"));
                userInfoBean.setLoginId(resultSet.getString("LOGINID"));
                userInfoBean.setPassword(resultSet.getString("PASSWD"));
                userInfoBean.setFirstName(resultSet.getString("FNME"));
                userInfoBean.setLastName(resultSet.getString("LNME"));
                userInfoBean.setMailId(resultSet.getString("EMAIL"));
                userInfoBean.setDeptId(resultSet.getInt("DEPT_ID"));
                userInfoBean.setActiveFlag(resultSet.getString("ACTIVE"));
                userInfoBean.setLastLoginTS(resultSet.getTimestamp("LAST_LOGIN_TS"));
                userInfoBean.setLastLogoutTS(resultSet.getTimestamp("LAST_LOGOUT_TS"));
                if ((resultSet.getBinaryStream("IMAGE") != null)) {
                    InputStream is = resultSet.getBinaryStream("IMAGE");
                    byte[] bytes = IOUtils.toByteArray(is);
                    userInfoBean.setUserImage(bytes);
                } else {
                    File imagefile = new File("C:/MSCVP_DEMO/User_Image/user-icon.png");
                    FileInputStream fis = new FileInputStream(imagefile);
                    byte[] bytes = IOUtils.toByteArray(fis);
                    userInfoBean.setUserImage(bytes);
                }
            }
        } catch (Exception exception) {
            LoggerUtility.log(logger, "Exception occurred in getUserInfo method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
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
            }catch (SQLException sqlException) {
                 LoggerUtility.log(logger, "finally SQLException occurred in getPoDetails method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
            }
        }
        return userInfoBean;
    }

    public Map<Integer, Integer> getUserRoles(int userId, String dsnName) throws ServiceLocatorException {
        Map<Integer, Integer> rolesMap = new HashMap<Integer, Integer>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String userRolesQuery = "SELECT PRIORITY,ROLE_ID FROM M_USER_ROLES WHERE USER_ID=? ORDER BY PRIORITY";
        try {
            connection = ConnectionProvider.getInstance().getConnection();
            preparedStatement = connection.prepareStatement(userRolesQuery);
            preparedStatement.setInt(1, userId);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                rolesMap.put(resultSet.getInt("PRIORITY"), resultSet.getInt("ROLE_ID"));
            }
        } catch (Exception exception) {
            LoggerUtility.log(logger, "Exception occurred in getUserRoles method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
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
                 LoggerUtility.log(logger, "finally SQLException occurred in getUserRoles method:: " + sqlException.getMessage(), Level.ERROR, sqlException.getCause());
            }
        }
        return rolesMap;
    }
}
