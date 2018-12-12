/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mss.ediscv.tradingPartner;

import com.mss.ediscv.util.AppConstants;
import com.mss.ediscv.util.AuthorizationManager;
import com.mss.ediscv.util.ServiceLocator;
import com.opensymphony.xwork2.ActionSupport;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import com.mss.ediscv.util.LoggerUtility;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.interceptor.ServletRequestAware;

/**
 * @author miracle1
 */
public class TradingPartnerAction extends ActionSupport implements ServletRequestAware {

    private static Logger logger = LogManager.getLogger(TradingPartnerAction.class.getName());
    private HttpServletRequest httpServletRequest;
    private String id;
    private String commId;
    private String commName;
    private String phno;
    private String email;
    private String address;
    private String city;
    private String state;
    private String zip;
    private String qualifier;
    private String network;
    private String asurl;
    private String ascert;
    private String version;
    private String vendorNo;
    private String deptNo;
    private String duns;
    private String orderDuns;
    private String shipDuns;
    private String billingDuns;
    private String buyerId;
    private String resultType;
    private String createdBy;
    private Map statesMap;
    private String tppageId;
    private String tpid;
    private String tpname;
    private List<TradingPartnerBean> tradingList;
    private String formAction;
    private String tpType;
    private String tpStatus;
    private String url;
    private String basic;
    private String soq;
    private String store;
    private String master;
    private String developing;
    private String payDuns;
    private String buyerName;
    private String buyerPhone;
    private String buyerEmail;
    private String csName;
    private String csPhone;
    private String csEmail;
    private String tradingPartnerName;
    private String contactName;
    private String bvrUdiCommId;
    private String bvrUdiName;
    private String defaultFlowId;
    private String notes;

    public String prepare() throws Exception {
        setResultType(LOGIN);
        try {
            if (getHttpServletRequest().getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
                setResultType("accessFailed");
                int userRoleId = Integer.parseInt(httpServletRequest.getSession(false).getAttribute(AppConstants.SES_ROLE_ID).toString());
                if (AuthorizationManager.getInstance().isAuthorizedUser("L_TP_CREATE", userRoleId)) {
                    HttpSession httpSession = getHttpServletRequest().getSession(false);

                    httpSession.removeAttribute(AppConstants.SES_PAYMENT_LIST);
                    httpSession.removeAttribute(AppConstants.SES_SHIPMENT_LIST);
                    httpSession.removeAttribute(AppConstants.SES_DOC_LIST);
                    httpSession.removeAttribute(AppConstants.SES_INV_LIST);
                    httpSession.removeAttribute(AppConstants.SES_PO_LIST);
                    httpSession.removeAttribute(AppConstants.SES_TRADINGPARTNER_LIST);
                    setStatesMap((HashMap) httpSession.getAttribute(AppConstants.SES_STATES_MAP));
                    setFormAction("../tradingPartner/doAddTradingPartner.action");
                    setResultType(SUCCESS);
                    setTppageId("0");
                }
            }
        } catch (Exception exception) {
            LoggerUtility.log(logger, "Exception occurred in prepare method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
            setResultType("error");
        }
        return getResultType();
    }

    /**
     * @return @throws Exception
     */
    public String doAdd() throws Exception {
        setResultType(LOGIN);
        try {
            if (getHttpServletRequest().getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
                setResultType("accessFailed");
                int userRoleId = Integer.parseInt(httpServletRequest.getSession(false).getAttribute(AppConstants.SES_ROLE_ID).toString());
                if (AuthorizationManager.getInstance().isAuthorizedUser("L_TP_CREATE", userRoleId)) {
                    HttpSession httpSession = getHttpServletRequest().getSession(false);
                    prepare();
                    setDeveloping("Y");
                    setUrl("http://");
                }
            }
        } catch (Exception exception) {
            LoggerUtility.log(logger, "Exception occurred in doAdd method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
            setResultType("error");
        }
        return getResultType();
    }

    public String doAddTP() throws Exception {
        resultType = LOGIN;
        try {
            if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
                setResultType("accessFailed");
                int userRoleId = Integer.parseInt(httpServletRequest.getSession(false).getAttribute(AppConstants.SES_ROLE_ID).toString());
                if (AuthorizationManager.getInstance().isAuthorizedUser("L_TP_CREATE", userRoleId)) {

                    String responseString = "";
                    setDefaultFlowId(httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_DEFAULT_FLOWID).toString());
                    setCreatedBy(httpServletRequest.getSession(false).getAttribute(AppConstants.SES_LOGIN_ID).toString());
                    responseString = ServiceLocator.getTradingPartnerService().addTP(this);
                    prepare();
                    httpServletRequest.setAttribute(AppConstants.REQ_RESULT_MSG, responseString);
                    resetValues();
                    resultType = SUCCESS;
                }
            }
        } catch (Exception exception) {
            LoggerUtility.log(logger, "Exception occurred in doAddTP method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
            httpServletRequest.getSession(false).setAttribute(AppConstants.REQ_EXCEPTION_MSG, exception.getMessage());
            setResultType("error");
        }
        return resultType;
    }

    public String getTradingSearchQuery() throws Exception {
        resultType = LOGIN;
        try {
            if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
                resultType = "accessFailed";
                int userRoleId = Integer.parseInt(httpServletRequest.getSession(false).getAttribute(AppConstants.SES_ROLE_ID).toString());
                if (AuthorizationManager.getInstance().isAuthorizedUser("L_TP_SEARCH", userRoleId)) {

                    setDefaultFlowId(httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_DEFAULT_FLOWID).toString());
                    setTradingList(ServiceLocator.getTradingPartnerService().buildTradingQuery(this));
                    httpServletRequest.getSession(false).setAttribute(AppConstants.SES_TRADINGPARTNER_LIST, getTradingList());
                    resultType = SUCCESS;
                }
            }
        } catch (Exception exception) {
            LoggerUtility.log(logger, "Exception occurred in getTradingSearchQuery method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
            httpServletRequest.getSession(false).setAttribute(AppConstants.REQ_EXCEPTION_MSG, exception.getMessage());
            setResultType("error");
        }
        return resultType;
    }

    public String backToSearchList() throws Exception {
        setResultType("accessFailed");
        try {
            int userRoleId = Integer.parseInt(httpServletRequest.getSession(false).getAttribute(AppConstants.SES_ROLE_ID).toString());
            if (AuthorizationManager.getInstance().isAuthorizedUser("L_TP_SEARCH", userRoleId)) {
                getTradingSearchQuery();
                setResultType(SUCCESS);
            }
        } catch (Exception exception) {
            LoggerUtility.log(logger, "Exception occurred in backToSearchList method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
        }
        return getResultType();
    }

    public String tpEdit() throws Exception {
        resultType = LOGIN;
        if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
            setResultType("accessFailed");
            int userRoleId = Integer.parseInt(httpServletRequest.getSession(false).getAttribute(AppConstants.SES_ROLE_ID).toString());
            if (AuthorizationManager.getInstance().isAuthorizedUser("L_TP_SEARCH", userRoleId)) {
                try {
                    setDefaultFlowId(httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_DEFAULT_FLOWID).toString());
                    setStatesMap((HashMap) httpServletRequest.getSession(false).getAttribute(AppConstants.SES_STATES_MAP));
                    ServiceLocator.getTradingPartnerService().tpEdit(this);
                    setFormAction("../tradingPartner/doEditTradingPartner.action");
                    setTppageId("1");
                    setSearchValues();
                    resultType = SUCCESS;

                } catch (Exception exception) {
                    LoggerUtility.log(logger, "Exception occurred in tpEdit method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
                    httpServletRequest.getSession(false).setAttribute(AppConstants.REQ_EXCEPTION_MSG, exception.getMessage());
                    resultType = "error";
                }
            }
        }
        return resultType;
    }

    public void setSearchValues() {
        setTpid(getTpid());
        setTpname(getTpname());
    }

    public String doEditTradingPartner() throws Exception {
        resultType = LOGIN;
        try {
            if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
                setResultType("accessFailed");
                int userRoleId = Integer.parseInt(httpServletRequest.getSession(false).getAttribute(AppConstants.SES_ROLE_ID).toString());
                if (AuthorizationManager.getInstance().isAuthorizedUser("L_TP_SEARCH", userRoleId)) {

                    String responseString = "";
                    setCreatedBy(httpServletRequest.getSession(false).getAttribute(AppConstants.SES_LOGIN_ID).toString());
                    responseString = ServiceLocator.getTradingPartnerService().editTP(this);
                    prepare();
                    setFormAction("../tradingPartner/doEditTradingPartner.action");
                    setSearchValues();
                    httpServletRequest.setAttribute(AppConstants.REQ_RESULT_MSG, responseString);
                    setTppageId("1");
                    resultType = SUCCESS;
                }
            }
        } catch (Exception exception) {
            LoggerUtility.log(logger, "Exception occurred in doEditTradingPartner method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
            httpServletRequest.getSession(false).setAttribute(AppConstants.REQ_EXCEPTION_MSG, exception.getMessage());
            resultType = "error";
        }
        return resultType;
    }

    public void resetValues() {
        setCommId("");
        setCommName("");
        setTradingPartnerName("");
        setContactName("");
        setBvrUdiCommId("");
        setBvrUdiName("");
        setPhno("");
        setEmail("");
        setAddress("");
        setCity("");
        setState("-1");
        setZip("");
        setNetwork("-1");
        setUrl("");
        setBasic("false");
        setSoq("false");
        setStore("false");
        setMaster("false");
        setDeveloping("Y");
        setVendorNo("");
        setOrderDuns("");
        setShipDuns("");
        setPayDuns("");
        setDeptNo("");
        setBuyerName("");
        setBuyerPhone("");
        setBuyerEmail("");
        setCsName("");
        setCsPhone("");
        setCsEmail("");
        setNotes("");
    }

    @Override
    public void setServletRequest(HttpServletRequest httpServletRequest) {
        this.httpServletRequest = httpServletRequest;
    }

    public HttpServletRequest getHttpServletRequest() {
        return httpServletRequest;
    }

    public String getCommId() {
        return commId;
    }

    public void setCommId(String commId) {
        this.commId = commId;
    }

    public String getCommName() {
        return commName;
    }

    public void setCommName(String commName) {
        this.commName = commName;
    }

    public String getPhno() {
        return phno;
    }

    public void setPhno(String phno) {
        this.phno = phno;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getQualifier() {
        return qualifier;
    }

    public void setQualifier(String qualifier) {
        this.qualifier = qualifier;
    }

    public String getNetwork() {
        return network;
    }

    public void setNetwork(String network) {
        this.network = network;
    }

    public String getAsurl() {
        return asurl;
    }

    public void setAsurl(String asurl) {
        this.asurl = asurl;
    }

    public String getAscert() {
        return ascert;
    }

    public void setAscert(String ascert) {
        this.ascert = ascert;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getVendorNo() {
        return vendorNo;
    }

    public void setVendorNo(String vendorNo) {
        this.vendorNo = vendorNo;
    }

    public String getDeptNo() {
        return deptNo;
    }

    public void setDeptNo(String deptNo) {
        this.deptNo = deptNo;
    }

    public String getDuns() {
        return duns;
    }

    public void setDuns(String duns) {
        this.duns = duns;
    }

    public String getOrderDuns() {
        return orderDuns;
    }

    public void setOrderDuns(String orderDuns) {
        this.orderDuns = orderDuns;
    }

    public String getShipDuns() {
        return shipDuns;
    }

    public void setShipDuns(String shipDuns) {
        this.shipDuns = shipDuns;
    }

    public String getBillingDuns() {
        return billingDuns;
    }

    public void setBillingDuns(String billingDuns) {
        this.billingDuns = billingDuns;
    }

    public String getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(String buyerId) {
        this.buyerId = buyerId;
    }

    public String getResultType() {
        return resultType;
    }

    public void setResultType(String resultType) {
        this.resultType = resultType;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Map getStatesMap() {
        return statesMap;
    }

    public void setStatesMap(Map statesMap) {
        this.statesMap = statesMap;
    }

    public String getTppageId() {
        return tppageId;
    }

    public void setTppageId(String tppageId) {
        this.tppageId = tppageId;
    }

    public String getTpid() {
        return tpid;
    }

    public void setTpid(String tpid) {
        this.tpid = tpid;
    }

    public String getTpname() {
        return tpname;
    }

    public void setTpname(String tpname) {
        this.tpname = tpname;
    }

    public List<TradingPartnerBean> getTradingList() {
        return tradingList;
    }

    public void setTradingList(List<TradingPartnerBean> tradingList) {
        this.tradingList = tradingList;
    }

    public String getFormAction() {
        return formAction;
    }

    public void setFormAction(String formAction) {
        this.formAction = formAction;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTpType() {
        return tpType;
    }

    public void setTpType(String tpType) {
        this.tpType = tpType;
    }

    public String getTpStatus() {
        return tpStatus;
    }

    public void setTpStatus(String tpStatus) {
        this.tpStatus = tpStatus;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getBasic() {
        return basic;
    }

    public void setBasic(String basic) {
        this.basic = basic;
    }

    public String getSoq() {
        return soq;
    }

    public void setSoq(String soq) {
        this.soq = soq;
    }

    public String getStore() {
        return store;
    }

    public void setStore(String store) {
        this.store = store;
    }

    public String getMaster() {
        return master;
    }

    public void setMaster(String master) {
        this.master = master;
    }

    public String getDeveloping() {
        return developing;
    }

    public void setDeveloping(String developing) {
        this.developing = developing;
    }

    public String getPayDuns() {
        return payDuns;
    }

    public void setPayDuns(String payDuns) {
        this.payDuns = payDuns;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }

    public String getBuyerPhone() {
        return buyerPhone;
    }

    public void setBuyerPhone(String buyerPhone) {
        this.buyerPhone = buyerPhone;
    }

    public String getBuyerEmail() {
        return buyerEmail;
    }

    public void setBuyerEmail(String buyerEmail) {
        this.buyerEmail = buyerEmail;
    }

    public String getCsName() {
        return csName;
    }

    public void setCsName(String csName) {
        this.csName = csName;
    }

    public String getCsPhone() {
        return csPhone;
    }

    public void setCsPhone(String csPhone) {
        this.csPhone = csPhone;
    }

    public String getCsEmail() {
        return csEmail;
    }

    public void setCsEmail(String csEmail) {
        this.csEmail = csEmail;
    }

    public String getTradingPartnerName() {
        return tradingPartnerName;
    }

    public void setTradingPartnerName(String tradingPartnerName) {
        this.tradingPartnerName = tradingPartnerName;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getBvrUdiCommId() {
        return bvrUdiCommId;
    }

    public void setBvrUdiCommId(String bvrUdiCommId) {
        this.bvrUdiCommId = bvrUdiCommId;
    }

    public String getBvrUdiName() {
        return bvrUdiName;
    }

    public void setBvrUdiName(String bvrUdiName) {
        this.bvrUdiName = bvrUdiName;
    }

    public String getDefaultFlowId() {
        return defaultFlowId;
    }

    public void setDefaultFlowId(String defaultFlowId) {
        this.defaultFlowId = defaultFlowId;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
