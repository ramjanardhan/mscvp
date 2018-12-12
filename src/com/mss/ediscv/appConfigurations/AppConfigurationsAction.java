package com.mss.ediscv.appConfigurations;

import com.mss.ediscv.util.AppConstants;
import com.mss.ediscv.util.DataSourceDataProvider;
import com.mss.ediscv.util.ServiceLocator;
import static com.opensymphony.xwork2.Action.LOGIN;
import static com.opensymphony.xwork2.Action.SUCCESS;
import com.opensymphony.xwork2.ActionSupport;
import javax.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.mss.ediscv.util.LoggerUtility;
import java.util.List;
import org.apache.struts2.interceptor.ServletRequestAware;

public class AppConfigurationsAction extends ActionSupport implements ServletRequestAware {

    private HttpServletRequest httpServletRequest;
    private String resultType;
    private String timeInterval;
    private String displayRecords;
    //  private String ldapAuthenticationDomain;
    private String ediTP1;
    private String ediTP2;
    private String ediTP3;
    private String ediTP4;
    private String ediTP5;
    private String ediTP6;
    private String ediTP7;
    private String ediTP8;
    private String ediTP9;
    private String ediTP10;
    private String railTP1;
    private String railTP2;
    private String railTP3;
    private String railTP4;
    private String railTP5;
    private String railTP6;
    private String railTP7;
    private String railTP8;
    private String railTP9;
    private String railTP10;
    private String bizlinkThresholdValue;
    private String maxMQDepthValue;
    private String minMQDepthValue;
    private String db_jdbcUrl;
    private String db_userName;
    private String db_password;
    private String db_minPoolSize;
    private String db_maxPoolSize;
    private String db_maxStatement;
    private String db_acquireIncrement;
    private String ediTransactionNamesList;
    private String railTransactionNamesList;
    private String resultMessage;
    private List tpList;
    private List transcationsList;
    private List<AppConfigurationsBean> appConfigurationsDBList;
    private List<AppConfigurationsBean> appConfigurationsBaseValueList;
    private static Logger logger = LogManager.getLogger(AppConfigurationsAction.class.getName());

    public String getAppConfigurations() throws Exception {
        System.out.println("entered in getAppConfigurations method");
        setResultType(LOGIN);
        if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
            setResultType("accessFailed");
            try {
                setTpList(DataSourceDataProvider.getInstance().getPartnerListAppConfig());

                if (httpServletRequest.getSession(false).getAttribute(AppConstants.DB_PROPERTIES_LIST) != null) {
                    httpServletRequest.getSession(false).removeAttribute(AppConstants.DB_PROPERTIES_LIST);
                }

                if (httpServletRequest.getSession(false).getAttribute(AppConstants.BASE_VALUE_PROPERTIES_LIST) != null) {
                    httpServletRequest.getSession(false).removeAttribute(AppConstants.BASE_VALUE_PROPERTIES_LIST);
                }
                resultMessage = ServiceLocator.getAppConfigurationsService().getAppConfigurationsValues(this);
                httpServletRequest.getSession(false).setAttribute(AppConstants.DB_PROPERTIES_LIST, getAppConfigurationsDBList());
                httpServletRequest.getSession(false).setAttribute(AppConstants.BASE_VALUE_PROPERTIES_LIST, getAppConfigurationsBaseValueList());
                setResultType(SUCCESS);
            } catch (Exception ex) {
                LoggerUtility.log(logger, " Exception occurred in getAppConfigurations method:: " + ex.getMessage(), Level.ERROR, ex.getCause());
                httpServletRequest.getSession(false).setAttribute(AppConstants.REQ_EXCEPTION_MSG, ex.getMessage());
                setResultType("error");
            }
        }
        System.out.println("ended in getAppConfigurations method");
        return getResultType();
    }

    @Override
    public void setServletRequest(HttpServletRequest httpServletRequest) {
        this.httpServletRequest = httpServletRequest;
    }

    /**
     * @return the resultType
     */
    public String getResultType() {
        return resultType;
    }

    /**
     * @param resultType the resultType to set
     */
    public void setResultType(String resultType) {
        this.resultType = resultType;
    }

    public String getTimeInterval() {
        return timeInterval;
    }

    public void setTimeInterval(String timeInterval) {
        this.timeInterval = timeInterval;
    }

    public String getDisplayRecords() {
        return displayRecords;
    }

    public void setDisplayRecords(String displayRecords) {
        this.displayRecords = displayRecords;
    }

//    public String getLdapAuthenticationDomain() {
//        return ldapAuthenticationDomain;
//    }
//
//    public void setLdapAuthenticationDomain(String ldapAuthenticationDomain) {
//        this.ldapAuthenticationDomain = ldapAuthenticationDomain;
//    }
    public String getEdiTP1() {
        return ediTP1;
    }

    public void setEdiTP1(String ediTP1) {
        this.ediTP1 = ediTP1;
    }

    public String getEdiTP2() {
        return ediTP2;
    }

    public void setEdiTP2(String ediTP2) {
        this.ediTP2 = ediTP2;
    }

    public String getEdiTP3() {
        return ediTP3;
    }

    public void setEdiTP3(String ediTP3) {
        this.ediTP3 = ediTP3;
    }

    public String getEdiTP4() {
        return ediTP4;
    }

    public void setEdiTP4(String ediTP4) {
        this.ediTP4 = ediTP4;
    }

    public String getEdiTP5() {
        return ediTP5;
    }

    public void setEdiTP5(String ediTP5) {
        this.ediTP5 = ediTP5;
    }

    public String getEdiTP6() {
        return ediTP6;
    }

    public void setEdiTP6(String ediTP6) {
        this.ediTP6 = ediTP6;
    }

    public String getEdiTP7() {
        return ediTP7;
    }

    public void setEdiTP7(String ediTP7) {
        this.ediTP7 = ediTP7;
    }

    public String getEdiTP8() {
        return ediTP8;
    }

    public void setEdiTP8(String ediTP8) {
        this.ediTP8 = ediTP8;
    }

    public String getEdiTP9() {
        return ediTP9;
    }

    public void setEdiTP9(String ediTP9) {
        this.ediTP9 = ediTP9;
    }

    public String getEdiTP10() {
        return ediTP10;
    }

    public void setEdiTP10(String ediTP10) {
        this.ediTP10 = ediTP10;
    }

    public String getRailTP1() {
        return railTP1;
    }

    public void setRailTP1(String railTP1) {
        this.railTP1 = railTP1;
    }

    public String getRailTP2() {
        return railTP2;
    }

    public void setRailTP2(String railTP2) {
        this.railTP2 = railTP2;
    }

    public String getRailTP3() {
        return railTP3;
    }

    public void setRailTP3(String railTP3) {
        this.railTP3 = railTP3;
    }

    public String getRailTP4() {
        return railTP4;
    }

    public void setRailTP4(String railTP4) {
        this.railTP4 = railTP4;
    }

    public String getRailTP5() {
        return railTP5;
    }

    public void setRailTP5(String railTP5) {
        this.railTP5 = railTP5;
    }

    public String getRailTP6() {
        return railTP6;
    }

    public void setRailTP6(String railTP6) {
        this.railTP6 = railTP6;
    }

    public String getRailTP7() {
        return railTP7;
    }

    public void setRailTP7(String railTP7) {
        this.railTP7 = railTP7;
    }

    public String getRailTP8() {
        return railTP8;
    }

    public void setRailTP8(String railTP8) {
        this.railTP8 = railTP8;
    }

    public String getRailTP9() {
        return railTP9;
    }

    public void setRailTP9(String railTP9) {
        this.railTP9 = railTP9;
    }

    public String getRailTP10() {
        return railTP10;
    }

    public void setRailTP10(String railTP10) {
        this.railTP10 = railTP10;
    }

    public String getBizlinkThresholdValue() {
        return bizlinkThresholdValue;
    }

    public void setBizlinkThresholdValue(String bizlinkThresholdValue) {
        this.bizlinkThresholdValue = bizlinkThresholdValue;
    }

    public String getMaxMQDepthValue() {
        return maxMQDepthValue;
    }

    public void setMaxMQDepthValue(String maxMQDepthValue) {
        this.maxMQDepthValue = maxMQDepthValue;
    }

    public String getMinMQDepthValue() {
        return minMQDepthValue;
    }

    public void setMinMQDepthValue(String minMQDepthValue) {
        this.minMQDepthValue = minMQDepthValue;
    }

//    public String getAdaptorNames() {
//        return adaptorNames;
//    }
//
//    public void setAdaptorNames(String adaptorNames) {
//        this.adaptorNames = adaptorNames;
//    }
    public String getDb_jdbcUrl() {
        return db_jdbcUrl;
    }

    public void setDb_jdbcUrl(String db_jdbcUrl) {
        this.db_jdbcUrl = db_jdbcUrl;
    }

    public String getDb_userName() {
        return db_userName;
    }

    public void setDb_userName(String db_userName) {
        this.db_userName = db_userName;
    }

    public String getDb_password() {
        return db_password;
    }

    public void setDb_password(String db_password) {
        this.db_password = db_password;
    }

    public String getDb_minPoolSize() {
        return db_minPoolSize;
    }

    public void setDb_minPoolSize(String db_minPoolSize) {
        this.db_minPoolSize = db_minPoolSize;
    }

    public String getDb_maxPoolSize() {
        return db_maxPoolSize;
    }

    public void setDb_maxPoolSize(String db_maxPoolSize) {
        this.db_maxPoolSize = db_maxPoolSize;
    }

    public String getDb_maxStatement() {
        return db_maxStatement;
    }

    public void setDb_maxStatement(String db_maxStatement) {
        this.db_maxStatement = db_maxStatement;
    }

    public String getDb_acquireIncrement() {
        return db_acquireIncrement;
    }

    public void setDb_acquireIncrement(String db_acquireIncrement) {
        this.db_acquireIncrement = db_acquireIncrement;
    }

//    public String getOutageMailsDisplayCount() {
//        return outageMailsDisplayCount;
//    }
//
//    public void setOutageMailsDisplayCount(String outageMailsDisplayCount) {
//        this.outageMailsDisplayCount = outageMailsDisplayCount;
//    }
//
//    public String getOutageSeverName() {
//        return outageSeverName;
//    }
//
//    public void setOutageSeverName(String outageSeverName) {
//        this.outageSeverName = outageSeverName;
//    }
//
//    public String getOutageMailId() {
//        return outageMailId;
//    }
//
//    public void setOutageMailId(String outageMailId) {
//        this.outageMailId = outageMailId;
//    }
//
//    public String getOutageMailPassword() {
//        return outageMailPassword;
//    }
//
//    public void setOutageMailPassword(String outageMailPassword) {
//        this.outageMailPassword = outageMailPassword;
//    }
//
//    public String getOutagePort() {
//        return outagePort;
//    }
//
//    public void setOutagePort(String outagePort) {
//        this.outagePort = outagePort;
//    }
//
//    public String getOutageSocketFactoryPort() {
//        return outageSocketFactoryPort;
//    }
//
//    public void setOutageSocketFactoryPort(String outageSocketFactoryPort) {
//        this.outageSocketFactoryPort = outageSocketFactoryPort;
//    }
//
//    public List<AppConfigurationsBean> getAppConfigurationsBMCList() {
//        return appConfigurationsBMCList;
//    }
//
//    public void setAppConfigurationsBMCList(List<AppConfigurationsBean> appConfigurationsBMCList) {
//        this.appConfigurationsBMCList = appConfigurationsBMCList;
//    }
    public List<AppConfigurationsBean> getAppConfigurationsDBList() {
        return appConfigurationsDBList;
    }

    public void setAppConfigurationsDBList(List<AppConfigurationsBean> appConfigurationsDBList) {
        this.appConfigurationsDBList = appConfigurationsDBList;
    }

//    public List<AppConfigurationsBean> getAppConfigurationsOutagesList() {
//        return appConfigurationsOutagesList;
//    }
//
//    public void setAppConfigurationsOutagesList(List<AppConfigurationsBean> appConfigurationsOutagesList) {
//        this.appConfigurationsOutagesList = appConfigurationsOutagesList;
//    }
    public List<AppConfigurationsBean> getAppConfigurationsBaseValueList() {
        return appConfigurationsBaseValueList;
    }

    public void setAppConfigurationsBaseValueList(List<AppConfigurationsBean> appConfigurationsBaseValueList) {
        this.appConfigurationsBaseValueList = appConfigurationsBaseValueList;
    }

    public String getEdiTransactionNamesList() {
        return ediTransactionNamesList;
    }

    public void setEdiTransactionNamesList(String ediTransactionNamesList) {
        this.ediTransactionNamesList = ediTransactionNamesList;
    }

    public String getRailTransactionNamesList() {
        return railTransactionNamesList;
    }

    public void setRailTransactionNamesList(String railTransactionNamesList) {
        this.railTransactionNamesList = railTransactionNamesList;
    }

    public List getTpList() {
        return tpList;
    }

    public void setTpList(List tpList) {
        this.tpList = tpList;
    }

    public List getTranscationsList() {
        return transcationsList;
    }

    public void setTranscationsList(List transcationsList) {
        this.transcationsList = transcationsList;
    }

}
