package com.mss.ediscv.general;

import java.util.Map;

import com.mss.ediscv.util.ServiceLocatorException;

public interface GeneralService {

    public UserInfoBean getUserInfo(String loginId, String dsnName) throws ServiceLocatorException;

    public Map<Integer, Integer> getUserRoles(int userId, String dsnName) throws ServiceLocatorException;
}
