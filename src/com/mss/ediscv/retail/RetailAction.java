/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mss.ediscv.retail;

import com.mss.ediscv.util.AppConstants;
import com.mss.ediscv.util.DataSourceDataProvider;
import com.opensymphony.xwork2.ActionSupport;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts2.interceptor.ServletRequestAware;
import com.mss.ediscv.util.LoggerUtility;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author miracle1
 */
public class RetailAction extends ActionSupport implements ServletRequestAware {

    private static Logger logger = LogManager.getLogger(RetailAction.class.getName());
    private HttpServletRequest httpServletRequest;

    public String execute() throws Exception {
        try {
            String defaultFlowId = httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_DEFAULT_FLOWID).toString();
            String defaultFlowName = DataSourceDataProvider.getInstance().getFlowNameByFlowID(defaultFlowId);
            if (!defaultFlowName.equals("Retailer")) {
                defaultFlowId = DataSourceDataProvider.getInstance().getFlowIdByFlowName("Retailer");
                httpServletRequest.getSession(false).setAttribute(AppConstants.SES_USER_DEFAULT_FLOWID, defaultFlowId);
            }
        } catch (Exception exception) {
            LoggerUtility.log(logger, "Exception occurred in getDocumentList method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
        }
        return SUCCESS;
    }

    @Override
    public void setServletRequest(HttpServletRequest httpServletRequest) {
        this.httpServletRequest = httpServletRequest;
    }
}
