/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mss.ediscv.user;

import com.mss.ediscv.util.ServiceLocatorException;
import java.io.File;
import java.util.ArrayList;

/**
 * @author miracle1
 */
public interface UserService {

    public String addUser(UserAction userAction) throws ServiceLocatorException;

    public boolean userCheckExist(UserAction userAction) throws ServiceLocatorException;

    public int updateUserPwd(UserAction userAction) throws ServiceLocatorException;

    public int updateMyPwd(UserAction userAction) throws ServiceLocatorException;

    public ArrayList getSearchUserList(UserAction userAction) throws ServiceLocatorException;

    public UserAction editUser(UserAction userAction) throws ServiceLocatorException;

    public String updateUser(UserAction userAction) throws ServiceLocatorException;

    public UserBean userDetails(int userId) throws ServiceLocatorException;

    public int insertFlows(int[] assignedFlowIds, int employeeId, int primaryFlowId) throws ServiceLocatorException;

    public UserBean userProfile(String userId, String loginId) throws ServiceLocatorException;

    public int updateUserProfile(UserAction userAction, int userId) throws ServiceLocatorException;

    public int uploadImage(File imageUpdate, int userId) throws ServiceLocatorException;
}
