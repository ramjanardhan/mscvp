/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mss.ediscv.Interceptor;

import com.mss.ediscv.util.LoggerUtility;
import com.mss.ediscv.util.PasswordUtil;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author miracle-pc
 */
public class MyInterceptor implements Interceptor {

    private static Logger logger = LogManager.getLogger(MyInterceptor.class.getName());

    @Override
    public void destroy() {
    }

    @Override
    public void init() {
    }

    @Override
    public String intercept(ActionInvocation actionInvocation) throws Exception {
        PasswordUtil passwordUtil = new PasswordUtil();
        try {
            ActionContext context = actionInvocation.getInvocationContext();
            HttpServletRequest request;
            HttpServletResponse response;
            request = (HttpServletRequest) context.get("com.opensymphony.xwork2.dispatcher.HttpServletRequest");
            response = (HttpServletResponse) context.get("com.opensymphony.xwork2.dispatcher.HttpServletResponse");
            String str = request.getParameter("checkboxvalue");
            if (str.equals("1")) {
                String uname = request.getParameter("loginId");
                String password = passwordUtil.encryptPwd(request.getParameter("password"));
                Cookie c = new Cookie(uname, password);
                c.setMaxAge(60 * 60 * 24);
                response.addCookie(c);
                if (c != null) {
                    LoggerUtility.log(logger, "Found Cookies Name and Value", Level.INFO, null);
                } else {
                    LoggerUtility.log(logger, "No Found Cookies Name and Value", Level.INFO, null);
                }
            }
        } catch (Exception exception) {
            LoggerUtility.log(logger, "Exception occurred in intercept method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
        }
        return actionInvocation.invoke();
    }
}
