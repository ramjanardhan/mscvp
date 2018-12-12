package com.mss.ediscv.general;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.struts2.StrutsStatics;
import com.mss.ediscv.util.LoggerUtility;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author miracle
 */
public class LoginInterceptor extends AbstractInterceptor implements StrutsStatics {

    private static Logger logger = LogManager.getLogger(LoginInterceptor.class.getName());

    public LoginInterceptor() {
    }

    public String intercept(ActionInvocation actionInvocation) throws Exception {
        try {
            ActionContext context = actionInvocation.getInvocationContext();
            HttpServletRequest request = (HttpServletRequest) context.get(HTTP_REQUEST);
            HttpSession session = request.getSession(false);
            if (session == null) {
                return "sessionExpire";
            }
        } catch (Exception exception) {
            LoggerUtility.log(logger, "Exception occurred in intercept method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
        }
        return actionInvocation.invoke();
    }
}
