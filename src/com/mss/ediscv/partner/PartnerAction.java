/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mss.ediscv.partner;

import com.mss.ediscv.util.AppConstants;
import com.mss.ediscv.util.DataSourceDataProvider;
import com.mss.ediscv.util.DateUtility;
import com.mss.ediscv.util.ServiceLocator;
import com.opensymphony.xwork2.ActionSupport;
import java.util.ArrayList;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts2.interceptor.ServletRequestAware;
import com.mss.ediscv.util.LoggerUtility;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author miracle
 */
public class PartnerAction extends ActionSupport implements ServletRequestAware {

    private static Logger logger = LogManager.getLogger(PartnerAction.class.getName());
    private HttpServletRequest httpServletRequest;
    private String resultType;
    private String formAction;
    private String partnerId;
    private String partnerName;
    private String internalIdentifier;
    private String partnerIdentifier;
    private String applicationId;
    private String countryCode;
    private String status;
    private String createdBy;
    private String flowFlag;
    private String routingName;
    private int sequence = 1;
    private String routerId;
    private String businessProcessId;
    private String translationMapName;
    private String docExtractMapName;
    private int archiveFlag;
    private String archiveDirectory;
    private String outputFileName;
    private String outputFormat;
    private String producerMailBox;
    private int translationId;
    private int documentExtarctId;
    private int producerMailBoxId;
    private Map partnerMap;
    private Map routerMap;
    private Map businessProcessMap;
    private Map translationMap;
    private Map documentExtractMap;
    private Map producerMailMap;
    private String encodingMailBoxId;
    private Map encodingMailMap;
    private int deliveryChannelId;
    private String businessProcessName;
    private String encodingMailBoxName;
    private String configFlowFlag;
    private String configFlowFlag1;

    public String addPartner() throws Exception {
        setResultType(LOGIN);
        try {
            if (getHttpServletRequest().getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
                setFormAction("doAddPartner");
                resultType = SUCCESS;
            }
        } catch (Exception exception) {
            LoggerUtility.log(logger, "Exception occurred in addPartner method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
        }
        return resultType;
    }

    public String doAddPartner() throws Exception {
        setResultType(LOGIN);
        try {
            if (getHttpServletRequest().getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
                setFormAction("doAddPartner");
                setFlowFlag(getHttpServletRequest().getSession(false).getAttribute(AppConstants.SES_USER_DEFAULT_FLOWID).toString());
                setCreatedBy(getHttpServletRequest().getSession(false).getAttribute(AppConstants.SES_LOGIN_ID).toString());
                String resultMessage = ServiceLocator.getPartnerService().addPartner(this);
                getHttpServletRequest().setAttribute(AppConstants.REQ_RESULT_MSG, resultMessage);
                resultType = SUCCESS;
            }
        } catch (Exception exception) {
            LoggerUtility.log(logger, "Exception occurred in doAddPartner method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
        }
        return resultType;
    }

    public String getPartnerList() throws Exception {
        setResultType(LOGIN);
        try {
            if (getHttpServletRequest().getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
                String defaultFlowId = httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_DEFAULT_FLOWID).toString();
                if (getConfigFlowFlag().equals("logistics")) {
                    defaultFlowId = DataSourceDataProvider.getInstance().getFlowIdByFlowName("Logistics");
                    httpServletRequest.getSession(false).setAttribute(AppConstants.SES_USER_DEFAULT_FLOWID, defaultFlowId);
                } else if (getConfigFlowFlag().equals("manufacturing")) {
                    defaultFlowId = DataSourceDataProvider.getInstance().getFlowIdByFlowName("Manufacturing");
                    httpServletRequest.getSession(false).setAttribute(AppConstants.SES_USER_DEFAULT_FLOWID, defaultFlowId);
                }
                if (getHttpServletRequest().getSession(false).getAttribute(AppConstants.SES_PARTNER_LIST) != null) {
                    getHttpServletRequest().getSession(false).removeAttribute(AppConstants.SES_PARTNER_LIST);
                }
                resultType = SUCCESS;
            }
        } catch (Exception exception) {
            LoggerUtility.log(logger, "Exception occurred in getPartnerList method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
        }
        return resultType;
    }

    public String partnerSearch() throws Exception {
        System.out.println("partnerSearch start time::"+DateUtility.getInstance().getCurrentDB2Timestamp());
        setResultType(LOGIN);
        try {
            if (getHttpServletRequest().getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
                setFlowFlag(getHttpServletRequest().getSession(false).getAttribute(AppConstants.SES_USER_DEFAULT_FLOWID).toString());
                
                System.out.println("set flow flag value is======="+getHttpServletRequest().getSession(false).getAttribute(AppConstants.SES_USER_DEFAULT_FLOWID).toString());
                
                setCreatedBy(getHttpServletRequest().getSession(false).getAttribute(AppConstants.SES_LOGIN_ID).toString());
                ArrayList<PartnerBean> partnerList = ServiceLocator.getPartnerService().buildPartnerQuery(this);
                getHttpServletRequest().getSession(false).setAttribute(AppConstants.SES_PARTNER_LIST, partnerList);
                resultType = SUCCESS;
            }
        } catch (Exception exception) {
            LoggerUtility.log(logger, "Exception occurred in partnerSearch method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
        }
        System.out.println("partnerSearch end time::"+DateUtility.getInstance().getCurrentDB2Timestamp());
        return resultType;
    }

    public String partnerEdit() throws Exception {
        setResultType(LOGIN);
        try {
            if (getHttpServletRequest().getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
                setFlowFlag(getHttpServletRequest().getSession(false).getAttribute(AppConstants.SES_USER_DEFAULT_FLOWID).toString());
                setCreatedBy(getHttpServletRequest().getSession(false).getAttribute(AppConstants.SES_LOGIN_ID).toString());
                ServiceLocator.getPartnerService().partnerEdit(this);
                setFormAction("doEditPartner");
                resultType = SUCCESS;
            }
        } catch (Exception exception) {
            LoggerUtility.log(logger, "Exception occurred in partnerEdit method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
        }
        return resultType;
    }

    public String doEditPartner() throws Exception {
        setResultType(LOGIN);
        try {
            if (getHttpServletRequest().getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
                setFormAction("doEditPartner");
                setFlowFlag(getHttpServletRequest().getSession(false).getAttribute(AppConstants.SES_USER_DEFAULT_FLOWID).toString());
                setCreatedBy(getHttpServletRequest().getSession(false).getAttribute(AppConstants.SES_LOGIN_ID).toString());
                String resultMessage = ServiceLocator.getPartnerService().editPartner(this);
                getHttpServletRequest().setAttribute(AppConstants.REQ_RESULT_MSG, resultMessage);
                resultType = SUCCESS;
            }
        } catch (Exception exception) {
            LoggerUtility.log(logger, "Exception occurred in doEditPartner method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
        }
        return resultType;
    }

    public String addDeliveryChannelInfo() throws Exception {
        setResultType(LOGIN);
        try {
            if (getHttpServletRequest().getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
                setFormAction("doAddDeliveryChannelInfo");
                setPartnerMap(DataSourceDataProvider.getInstance().getPartnerMap());
                setRouterMap(DataSourceDataProvider.getInstance().getRouterMap());
                setBusinessProcessMap(DataSourceDataProvider.getInstance().getRelationMap("BP"));
                setTranslationMap(DataSourceDataProvider.getInstance().getRelationMap("TRAN"));
                setDocumentExtractMap(DataSourceDataProvider.getInstance().getRelationMap("DEM"));
                setProducerMailMap(DataSourceDataProvider.getInstance().getRelationMap("PMB"));
                setEncodingMailMap(DataSourceDataProvider.getInstance().getEncodeMap());
                resultType = SUCCESS;
            }
        } catch (Exception exception) {
            LoggerUtility.log(logger, "Exception occurred in addDeliveryChannelInfo method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
        }
        return resultType;
    }

    public String doAddDeliveryChannelInfo() throws Exception {
        setResultType(LOGIN);
        try {
            if (getHttpServletRequest().getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
                setFormAction("doAddDeliveryChannelInfo");
                setPartnerMap(DataSourceDataProvider.getInstance().getPartnerMap());
                setRouterMap(DataSourceDataProvider.getInstance().getRouterMap());
                setBusinessProcessMap(DataSourceDataProvider.getInstance().getRelationMap("BP"));
                setTranslationMap(DataSourceDataProvider.getInstance().getRelationMap("TRAN"));
                setDocumentExtractMap(DataSourceDataProvider.getInstance().getRelationMap("DEM"));
                setProducerMailMap(DataSourceDataProvider.getInstance().getRelationMap("PMB"));
                setEncodingMailMap(DataSourceDataProvider.getInstance().getEncodeMap());
                String resultMessage = ServiceLocator.getPartnerService().addDeliveryChannelInfo(this);
                getHttpServletRequest().setAttribute(AppConstants.REQ_RESULT_MSG, resultMessage);
                resultType = SUCCESS;
            }
        } catch (Exception exception) {
            LoggerUtility.log(logger, "Exception occurred in doAddDeliveryChannelInfo method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
        }
        return resultType;
    }

    public String deliveryChannelList() throws Exception {
        setResultType(LOGIN);
        try {
            if (getHttpServletRequest().getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
                String defaultFlowId = httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_DEFAULT_FLOWID).toString();
                if (getConfigFlowFlag().equals("logistics")) {
                    defaultFlowId = DataSourceDataProvider.getInstance().getFlowIdByFlowName("Logistics");
                    httpServletRequest.getSession(false).setAttribute(AppConstants.SES_USER_DEFAULT_FLOWID, defaultFlowId);
                } else if (getConfigFlowFlag().equals("manufacturing")) {
                    defaultFlowId = DataSourceDataProvider.getInstance().getFlowIdByFlowName("Manufacturing");
                    httpServletRequest.getSession(false).setAttribute(AppConstants.SES_USER_DEFAULT_FLOWID, defaultFlowId);
                }
                setPartnerMap(DataSourceDataProvider.getInstance().getPartnerMap());
                setRouterMap(DataSourceDataProvider.getInstance().getRouterMap());
                setBusinessProcessMap(DataSourceDataProvider.getInstance().getRelationMap("BP"));
                setTranslationMap(DataSourceDataProvider.getInstance().getRelationMap("TRAN"));
                setDocumentExtractMap(DataSourceDataProvider.getInstance().getRelationMap("DEM"));
                setProducerMailMap(DataSourceDataProvider.getInstance().getRelationMap("PMB"));
                setEncodingMailMap(DataSourceDataProvider.getInstance().getEncodeMap());
                resultType = SUCCESS;
            }
        } catch (Exception exception) {
            LoggerUtility.log(logger, "Exception occurred in deliveryChannelList method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
        }
        return resultType;
    }

    public String deliveryChannelSearch() throws Exception {
        setResultType(LOGIN);
        try {
            if (getHttpServletRequest().getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
                setPartnerMap(DataSourceDataProvider.getInstance().getPartnerMap());
                setRouterMap(DataSourceDataProvider.getInstance().getRouterMap());
                setBusinessProcessMap(DataSourceDataProvider.getInstance().getRelationMap("BP"));
                setTranslationMap(DataSourceDataProvider.getInstance().getRelationMap("TRAN"));
                setDocumentExtractMap(DataSourceDataProvider.getInstance().getRelationMap("DEM"));
                setProducerMailMap(DataSourceDataProvider.getInstance().getRelationMap("PMB"));
                setEncodingMailMap(DataSourceDataProvider.getInstance().getEncodeMap());
                ArrayList<PartnerBean> deliveryChannelList = ServiceLocator.getPartnerService().buildDeliverChannelQuery(this);
                getHttpServletRequest().getSession(false).setAttribute(AppConstants.SES_DELIVERY_CHANNEL_LIST, deliveryChannelList);
                resultType = SUCCESS;
            }
        } catch (Exception exception) {
            LoggerUtility.log(logger, "Exception occurred in deliveryChannelSearch method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
        }
        return resultType;
    }

    public String deliveryChannelEdit() throws Exception {
        setResultType(LOGIN);
        try {
            if (getHttpServletRequest().getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
                setPartnerMap(DataSourceDataProvider.getInstance().getPartnerMap());
                setRouterMap(DataSourceDataProvider.getInstance().getRouterMap());
                setBusinessProcessMap(DataSourceDataProvider.getInstance().getRelationMap("BP"));
                setTranslationMap(DataSourceDataProvider.getInstance().getRelationMap("TRAN"));
                setDocumentExtractMap(DataSourceDataProvider.getInstance().getRelationMap("DEM"));
                setProducerMailMap(DataSourceDataProvider.getInstance().getRelationMap("PMB"));
                setEncodingMailMap(DataSourceDataProvider.getInstance().getEncodeMap());
                setFlowFlag(getHttpServletRequest().getSession(false).getAttribute(AppConstants.SES_USER_DEFAULT_FLOWID).toString());
                setCreatedBy(getHttpServletRequest().getSession(false).getAttribute(AppConstants.SES_LOGIN_ID).toString());
                ServiceLocator.getPartnerService().deliveryChannelEdit(this);
                setFormAction("doEditDeliveryChannelInfo");
                resultType = SUCCESS;
            }
        } catch (Exception exception) {
            LoggerUtility.log(logger, "Exception occurred in deliveryChannelEdit method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
        }
        return resultType;
    }

    public String doEditDeliveryChannelInfo() throws Exception {
        setResultType(LOGIN);
        try {
            if (getHttpServletRequest().getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
                setFormAction("doAddDeliveryChannelInfo");
                setPartnerMap(DataSourceDataProvider.getInstance().getPartnerMap());
                setRouterMap(DataSourceDataProvider.getInstance().getRouterMap());
                setBusinessProcessMap(DataSourceDataProvider.getInstance().getRelationMap("BP"));
                setTranslationMap(DataSourceDataProvider.getInstance().getRelationMap("TRAN"));
                setDocumentExtractMap(DataSourceDataProvider.getInstance().getRelationMap("DEM"));
                setProducerMailMap(DataSourceDataProvider.getInstance().getRelationMap("PMB"));
                setEncodingMailMap(DataSourceDataProvider.getInstance().getEncodeMap());
                String resultMessage = ServiceLocator.getPartnerService().editDeliveryChannel(this);
                getHttpServletRequest().setAttribute(AppConstants.REQ_RESULT_MSG, resultMessage);
                resultType = SUCCESS;
            }
        } catch (Exception exception) {
            LoggerUtility.log(logger, "Exception occurred in doEditDeliveryChannelInfo method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
        }
        return resultType;
    }

    @Override
    public void setServletRequest(HttpServletRequest httpServletRequest) {
        this.httpServletRequest = httpServletRequest;
    }

    public HttpServletRequest getHttpServletRequest() {
        return httpServletRequest;
    }

    public String getResultType() {
        return resultType;
    }

    public void setResultType(String resultType) {
        this.resultType = resultType;
    }

    public String getFormAction() {
        return formAction;
    }

    public void setFormAction(String formAction) {
        this.formAction = formAction;
    }

    public String getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(String partnerId) {
        this.partnerId = partnerId;
    }

    public String getPartnerName() {
        return partnerName;
    }

    public void setPartnerName(String partnerName) {
        this.partnerName = partnerName;
    }

    public String getInternalIdentifier() {
        return internalIdentifier;
    }

    public void setInternalIdentifier(String internalIdentifier) {
        this.internalIdentifier = internalIdentifier;
    }

    public String getPartnerIdentifier() {
        return partnerIdentifier;
    }

    public void setPartnerIdentifier(String partnerIdentifier) {
        this.partnerIdentifier = partnerIdentifier;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getFlowFlag() {
        return flowFlag;
    }

    public void setFlowFlag(String flowFlag) {
        this.flowFlag = flowFlag;
    }

    public Map getPartnerMap() {
        return partnerMap;
    }

    public void setPartnerMap(Map partnerMap) {
        this.partnerMap = partnerMap;
    }

    public Map getRouterMap() {
        return routerMap;
    }

    public void setRouterMap(Map routerMap) {
        this.routerMap = routerMap;
    }

    public Map getBusinessProcessMap() {
        return businessProcessMap;
    }

    /**
     * @param businessProcessMap the businessProcessMap to set
     */
    public void setBusinessProcessMap(Map businessProcessMap) {
        this.businessProcessMap = businessProcessMap;
    }

    public String getRoutingName() {
        return routingName;
    }

    public void setRoutingName(String routingName) {
        this.routingName = routingName;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public String getRouterId() {
        return routerId;
    }

    public void setRouterId(String routerId) {
        this.routerId = routerId;
    }

    public String getBusinessProcessId() {
        return businessProcessId;
    }

    public void setBusinessProcessId(String businessProcessId) {
        this.businessProcessId = businessProcessId;
    }

    public String getTranslationMapName() {
        return translationMapName;
    }

    public void setTranslationMapName(String translationMapName) {
        this.translationMapName = translationMapName;
    }

    public String getDocExtractMapName() {
        return docExtractMapName;
    }

    public void setDocExtractMapName(String docExtractMapName) {
        this.docExtractMapName = docExtractMapName;
    }

    public int getArchiveFlag() {
        return archiveFlag;
    }

    public void setArchiveFlag(int archiveFlag) {
        this.archiveFlag = archiveFlag;
    }

    public String getArchiveDirectory() {
        return archiveDirectory;
    }

    public void setArchiveDirectory(String archiveDirectory) {
        this.archiveDirectory = archiveDirectory;
    }

    public String getOutputFileName() {
        return outputFileName;
    }

    public void setOutputFileName(String outputFileName) {
        this.outputFileName = outputFileName;
    }

    public String getOutputFormat() {
        return outputFormat;
    }

    public void setOutputFormat(String outputFormat) {
        this.outputFormat = outputFormat;
    }

    public String getProducerMailBox() {
        return producerMailBox;
    }

    public void setProducerMailBox(String producerMailBox) {
        this.producerMailBox = producerMailBox;
    }

    public Map getTranslationMap() {
        return translationMap;
    }

    public void setTranslationMap(Map translationMap) {
        this.translationMap = translationMap;
    }

    public Map getDocumentExtractMap() {
        return documentExtractMap;
    }

    public void setDocumentExtractMap(Map documentExtractMap) {
        this.documentExtractMap = documentExtractMap;
    }

    public Map getProducerMailMap() {
        return producerMailMap;
    }

    public void setProducerMailMap(Map producerMailMap) {
        this.producerMailMap = producerMailMap;
    }

    public int getTranslationId() {
        return translationId;
    }

    public void setTranslationId(int translationId) {
        this.translationId = translationId;
    }

    public int getDocumentExtarctId() {
        return documentExtarctId;
    }

    public void setDocumentExtarctId(int documentExtarctId) {
        this.documentExtarctId = documentExtarctId;
    }

    public int getProducerMailBoxId() {
        return producerMailBoxId;
    }

    public void setProducerMailBoxId(int producerMailBoxId) {
        this.producerMailBoxId = producerMailBoxId;
    }

    public String getEncodingMailBoxId() {
        return encodingMailBoxId;
    }

    public void setEncodingMailBoxId(String encodingMailBoxId) {
        this.encodingMailBoxId = encodingMailBoxId;
    }

    public Map getEncodingMailMap() {
        return encodingMailMap;
    }

    public void setEncodingMailMap(Map encodingMailMap) {
        this.encodingMailMap = encodingMailMap;
    }

    public int getDeliveryChannelId() {
        return deliveryChannelId;
    }

    public void setDeliveryChannelId(int deliveryChannelId) {
        this.deliveryChannelId = deliveryChannelId;
    }

    public String getBusinessProcessName() {
        return businessProcessName;
    }

    public void setBusinessProcessName(String businessProcessName) {
        this.businessProcessName = businessProcessName;
    }

    public String getEncodingMailBoxName() {
        return encodingMailBoxName;
    }

    public void setEncodingMailBoxName(String encodingMailBoxName) {
        this.encodingMailBoxName = encodingMailBoxName;
    }

    public String getConfigFlowFlag() {
        return configFlowFlag;
    }

    public void setConfigFlowFlag(String configFlowFlag) {
        this.configFlowFlag = configFlowFlag;
    }

    public String getConfigFlowFlag1() {
        return configFlowFlag1;
    }

    public void setConfigFlowFlag1(String configFlowFlag1) {
        this.configFlowFlag1 = configFlowFlag1;
    }
}
