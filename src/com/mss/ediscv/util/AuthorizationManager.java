/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mss.ediscv.util;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * @author miracle
 */
public class AuthorizationManager {
    private static Logger logger = LogManager.getLogger(AuthorizationManager.class.getName());

    public static AuthorizationManager _instance;

    private AuthorizationManager() {
    }

    public static AuthorizationManager getInstance() {
        if (_instance == null) {
            _instance = new AuthorizationManager();
        }
        return _instance;
    }

    public boolean isAuthorizedUser(String accessKey, int roleId) {
        boolean isAuthorized = false;
        try {
            int noOfRoles = Integer.parseInt(SecurityProperties.getProperty("TOTAL_ROLES"));
            String authorizedRoleIds = SecurityProperties.getProperty(accessKey);
            String authorizedRoleIdsArray[] = new String[noOfRoles];
            authorizedRoleIdsArray = authorizedRoleIds.split(",");
            for (int counter = 0; counter < authorizedRoleIdsArray.length; counter++) {
                if (roleId == Integer.parseInt(authorizedRoleIdsArray[counter])) {
                    isAuthorized = true;
                }
            }
        } catch (Exception exception){
            LoggerUtility.log(logger, "Exception occurred in isAuthorizedUser method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
           
        }
        return isAuthorized;
    }

    public boolean isAuthorizedReceiver(String reciverIds, int useriD) {
        boolean isAuthorized = false;
        try {
            String authorizedRoleIdsArray[] = reciverIds.split(",");
            for (int counter = 0; counter < authorizedRoleIdsArray.length; counter++) {
                if (useriD == Integer.parseInt(authorizedRoleIdsArray[counter].trim())) {
                    isAuthorized = true;
                }
            }
        } catch (Exception exception) {
            LoggerUtility.log(logger, "Exception occurred in isAuthorizedReceiver method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
            
        }
        return isAuthorized;
    }
}
