/*
 * AjaxHandlerService.java
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.mss.ediscv.ajax;

import com.mss.ediscv.util.ServiceLocatorException;

/**
 * @author miracle
 */
public interface AjaxHandlerService {

    public String getPoDetails(String poNumber, String poInst, String database) throws ServiceLocatorException;

    public String getASNDetails(String asnNumber, String poNumber, String fileID, String database) throws ServiceLocatorException;

    public String getInvDetails(String invNumber, String poNumber, String fileID, String database) throws ServiceLocatorException;

    public String getPaymentDetails(String fileId, String database) throws ServiceLocatorException;

    public String getDocDetails(String isaNumber, String poNum, int id, String database) throws ServiceLocatorException;

    public String getReportDeleteDetails(int id) throws ServiceLocatorException;

    public String getDocCopy(String poList, String type, String database) throws ServiceLocatorException;

    public String getDocASNCopy(String asnList, String type) throws ServiceLocatorException;

    public String getInvCopy(String invList, String type) throws ServiceLocatorException;

    public String getPaymentCopy(String paymentList, String type) throws ServiceLocatorException;

    public String getLoadCopy(String loadList, String type, String database) throws ServiceLocatorException;

    public String getLifeCycleDetails(String poNumber, String fileId, String type, String database) throws ServiceLocatorException;

    public String getLtLifecycleDetails(String poNumber, String fileId, String type, String database) throws ServiceLocatorException;

    public String getTpDetails(String tpId) throws ServiceLocatorException;

    public String updateTpDetails(AjaxHandlerAction ajaxHandlerAction) throws ServiceLocatorException;

    public String getTpDetailInformation(String tpId, String defFlowId) throws ServiceLocatorException;

    public String getLogisticsDocDetails(String instanceid, int id, String database) throws ServiceLocatorException;

    public String getLoadTenderingDetails(String instanceid, String ponum, String database) throws ServiceLocatorException;

    public String getLtResponseDetails(String invNum, String ponum, String database) throws ServiceLocatorException;

    public String getLogisticsInvDetails(String invNum, int id, String database) throws ServiceLocatorException;

    public String getLogisticsShipmentDetails(String asnNum, String ponum, int id, String database) throws ServiceLocatorException;

    public String getDocVisibilityDetails(int docId) throws ServiceLocatorException;

    public String getPartnerDetails(String partnerId) throws ServiceLocatorException;

    public String getRoutingDetails(String routingId) throws ServiceLocatorException;

    public String getB2bChannelDetails(String b2bChannelId) throws ServiceLocatorException;

    public String getPartnerInfo(String partnerId) throws ServiceLocatorException;

    public String getRouterInfo(String routerName) throws ServiceLocatorException;

    public String getBusinessProcessInfo(String businessProcessId) throws ServiceLocatorException;

    public String getDeliveryChannelDetails(int deliveryChannelId) throws ServiceLocatorException;

    public String getDashboardDetails(AjaxHandlerAction ajaxHandlerAction) throws ServiceLocatorException;

    public String getReportOverlayDetails(int id, String startDate) throws ServiceLocatorException;

    public String forgotPassword(String userid) throws ServiceLocatorException;

    public int updateMyPwd(AjaxHandlerAction ajaxhandleraction, String loginId) throws ServiceLocatorException;

    public int searchItems(String senderItem, String recItem, String selectedName) throws ServiceLocatorException;

    public int checkCodeListName(String newCodeListName) throws ServiceLocatorException;

    public String addCodeList(String jsonData, String userName, String newCodeListName) throws ServiceLocatorException;

    public String updateCodeList(String listName, String jsonData, String userName, int items) throws ServiceLocatorException;

    public String deleteCodeList(String jsonData) throws ServiceLocatorException;

    public String getInventoryDetails(String fileId, int id, String database) throws ServiceLocatorException;

    public String getMapNamesList(String mapName) throws ServiceLocatorException;

    public String doGetProcessFlow(String jsonData) throws ServiceLocatorException;

    public String doSaveProcessFlow(String jsonData, String userName) throws ServiceLocatorException;

    public String getMailBoxList(String name) throws ServiceLocatorException;

    public String doGetTpList(String name) throws ServiceLocatorException;

    public String getTransactionsBasedOnType(String flowFlag) throws ServiceLocatorException;

    public String getPartnerAndDocType(String flowFlag) throws ServiceLocatorException;

    public String getTransactionReferences(String fileId, String docType, String senderId, String receiverId) throws ServiceLocatorException;

    public String getPrePosttranslationPathData(String preposttranslationpath) throws Exception;
    
    public String appConfigEditTimeinterval(String username, String timeInterval) throws ServiceLocatorException;

    public String appConfigEditTop10EDITP(String username, String allEDItp);

    public String appConfigEditScvpDatabaseValues(String username, String dbData);

    public String appConfigEditEDITransactions(String username, String ediTransactionNamesList);

    public String appConfigEditRAILTransactions(String username, String railTransactionNamesList);

    public String appConfigEditBaseValues(String username, String baseValueData);

    public String appConfigEditTop10RailTP(String username, String allRailtp);

}
