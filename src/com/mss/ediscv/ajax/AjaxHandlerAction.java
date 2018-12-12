/*
 * AjaxHandlerAction.java
 *
 * Created on June 11, 2008, 12:22 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.mss.ediscv.ajax;

import com.mss.ediscv.util.AppConstants;
import com.mss.ediscv.util.DataSourceDataProvider;
import com.mss.ediscv.util.LoggerUtility;
import com.mss.ediscv.util.ServiceLocator;
import static com.opensymphony.xwork2.Action.LOGIN;
import static com.opensymphony.xwork2.Action.SUCCESS;
import com.opensymphony.xwork2.ActionSupport;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

/**
 *
 * @author miracle
 */
public class AjaxHandlerAction extends ActionSupport implements ServletRequestAware, ServletResponseAware {

    private static Logger logger = LogManager.getLogger(AjaxHandlerAction.class.getName());
    private HttpServletRequest httpServletRequest;
    private HttpServletResponse httpServletResponse;
    private int id;
    private String responseString;
    private String poNumber;
    private String poInst;
    private String asnNumber;
    private String invNumber;
    private String isaNumber;
    private String poList;
    private String asnList;
    private String invList;
    private String paymentList;
    private String type;
    private String fileId;
    private String tpId;
    private String name;
    private String dept;
    private String commid;
    private String contact;
    private String phno;
    private String qualifier;
    private String refId;
    private String loadList;
    private int docId;
    private String partnerId;
    private String routingId;
    private String b2bChannelId;
    private String routerId;
    private String businessProcessId;
    private int deliveryChannelId;
    private String startDate;
    private String endDate;
    private String senderId;
    private String receiverId;
    private String docType;
    private String ackStatus;
    private String status;
    private String direction;
    private String oldPwd;
    private String newPwd;
    private String cnfrmPwd;
    private String senderItem;
    private String recItem;
    private String database;
    private String selectedName;
    private String newListName;
    private List listNameMap;
    private String json;
    private String listName;
    private int items;
    private String modifieddate;
    private String flag;
    private String jsonData;
    private String prePostTranslationpath;
     private String timeInterval;
     private String allEDItp;
     private String allRailtp;
     private String dbData;
     private String ediTransactionNamesList;
    private String railTransactionNamesList;
    private String baseValueData;

    public AjaxHandlerAction() {
    }

    public String getPoDetails() {
        if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
            try {
                responseString = ServiceLocator.getAjaxHandlerService().getPoDetails(getPoNumber(), getPoInst(), getDatabase());
                httpServletResponse.setContentType("text/xml");
                httpServletResponse.getWriter().write(responseString);
            } catch (Exception exception) {
                LoggerUtility.log(logger, "Exception occurred in getPoDetails method in AjaxHandlerAction:: " + exception.getMessage(), Level.ERROR, exception.getCause());
            }
        }
        return null;
    }

    public String getAsnDetails() {
        if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
            try {
                responseString = ServiceLocator.getAjaxHandlerService().getASNDetails(getAsnNumber(), getPoNumber(), getFileId(), getDatabase());
                httpServletResponse.setContentType("text/xml");
                httpServletResponse.getWriter().write(responseString);
            } catch (Exception exception) {
                LoggerUtility.log(logger, "Exception occurred in getAsnDetails method in AjaxHandlerAction:: " + exception.getMessage(), Level.ERROR, exception.getCause());
            }
        }
        return null;
    }

    public String getInvDetails() {
        if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
            try {
                responseString = ServiceLocator.getAjaxHandlerService().getInvDetails(getInvNumber(), getPoNumber(), getFileId(), getDatabase());
                httpServletResponse.setContentType("text/xml");
                httpServletResponse.getWriter().write(responseString);
            } catch (Exception exception) {
                LoggerUtility.log(logger, "Exception occurred in getInvDetails method in AjaxHandlerAction:: " + exception.getMessage(), Level.ERROR, exception.getCause());
            }
        }
        return null;
    }

    public String getDocDetails() {
        if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
            try {
                responseString = ServiceLocator.getAjaxHandlerService().getDocDetails(getIsaNumber(), getPoNumber(), getId(), getDatabase());
                httpServletResponse.setContentType("text/xml");
                httpServletResponse.getWriter().write(responseString);
            } catch (Exception exception) {
                LoggerUtility.log(logger, "Exception occurred in getDocDetails method in AjaxHandlerAction:: " + exception.getMessage(), Level.ERROR, exception.getCause());
            }
        }
        return null;
    }

    public String getReportDeleteDetails() {
        if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
            try {
                responseString = ServiceLocator.getAjaxHandlerService().getReportDeleteDetails(getId());
                httpServletResponse.setContentType("text/xml");
                httpServletResponse.getWriter().write(responseString);
            } catch (Exception exception) {
                LoggerUtility.log(logger, "Exception occurred in getReportDeleteDetails method in AjaxHandlerAction:: " + exception.getMessage(), Level.ERROR, exception.getCause());
            }
        }
        return null;
    }

    public String getPaymentDetails() {
        if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
            try {
                responseString = ServiceLocator.getAjaxHandlerService().getPaymentDetails(getFileId(), getDatabase());
                httpServletResponse.setContentType("text/xml");
                httpServletResponse.getWriter().write(responseString);
            } catch (Exception exception) {
                LoggerUtility.log(logger, "Exception occurred in getPaymentDetails method in AjaxHandlerAction:: " + exception.getMessage(), Level.ERROR, exception.getCause());
            }
        }
        return null;
    }

    public String getDocCopy() {
        if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
            try {
                responseString = ServiceLocator.getAjaxHandlerService().getDocCopy(getPoList(), getType(), getDatabase());
                httpServletResponse.setContentType("text/xml");
                httpServletResponse.getWriter().write(responseString);
            } catch (Exception exception) {
                LoggerUtility.log(logger, "Exception occurred in getDocCopy method in AjaxHandlerAction:: " + exception.getMessage(), Level.ERROR, exception.getCause());
            }
        }
        return null;
    }

    public String getDocASNCopy() {
        if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
            try {
                responseString = ServiceLocator.getAjaxHandlerService().getDocASNCopy(getAsnList(), getType());
                httpServletResponse.setContentType("text/xml");
                httpServletResponse.getWriter().write(responseString);
            } catch (Exception exception) {
                LoggerUtility.log(logger, "Exception occurred in getDocASNCopy method in AjaxHandlerAction:: " + exception.getMessage(), Level.ERROR, exception.getCause());
            }
        }
        return null;
    }

    public String getInvCopy() {
        if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
            try {
                responseString = ServiceLocator.getAjaxHandlerService().getInvCopy(getInvList(), getType().toString());
                httpServletResponse.setContentType("text/xml");
                httpServletResponse.getWriter().write(responseString);
            } catch (Exception exception) {
                LoggerUtility.log(logger, "Exception occurred in getInvCopy method in AjaxHandlerAction:: " + exception.getMessage(), Level.ERROR, exception.getCause());
            }
        }
        return null;
    }

    public String getPaymentCopy() {
        if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
            try {
                responseString = ServiceLocator.getAjaxHandlerService().getInvCopy(getPaymentList(), getType());
                httpServletResponse.setContentType("text/xml");
                httpServletResponse.getWriter().write(responseString);
            } catch (Exception exception) {
                LoggerUtility.log(logger, "Exception occurred in getPaymentCopy method in AjaxHandlerAction:: " + exception.getMessage(), Level.ERROR, exception.getCause());
            }
        }
        return null;
    }

    public String getLoadCopy() {
        if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
            try {
                responseString = ServiceLocator.getAjaxHandlerService().getLoadCopy(getLoadList(), getType(), getDatabase());
                httpServletResponse.setContentType("text/xml");
                httpServletResponse.getWriter().write(responseString);
            } catch (Exception exception) {
                LoggerUtility.log(logger, "Exception occurred in getLoadCopy method in AjaxHandlerAction:: " + exception.getMessage(), Level.ERROR, exception.getCause());
            }
        }
        return null;
    }

    public String getLifecycleDetails() {
        if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
            try {
                responseString = ServiceLocator.getAjaxHandlerService().getLifeCycleDetails(getPoNumber(), getFileId(), getType(), getDatabase());
                httpServletResponse.setContentType("text/xml");
                httpServletResponse.getWriter().write(responseString);
            } catch (Exception exception) {
                LoggerUtility.log(logger, "Exception occurred in getLifecycleDetails method in AjaxHandlerAction:: " + exception.getMessage(), Level.ERROR, exception.getCause());
            }
        }
        return null;
    }

    public String getLtLifecycleDetails() {
        if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
            try {
                responseString = ServiceLocator.getAjaxHandlerService().getLtLifecycleDetails(getPoNumber(), getFileId(), getType(), getDatabase());
                httpServletResponse.setContentType("text/xml");
                httpServletResponse.getWriter().write(responseString);
            } catch (Exception exception) {
                LoggerUtility.log(logger, "Exception occurred in getLtLifecycleDetails method in AjaxHandlerAction:: " + exception.getMessage(), Level.ERROR, exception.getCause());
            }
        }
        return null;
    }

    public String getTpDetails() {
        if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
            try {
                responseString = ServiceLocator.getAjaxHandlerService().getTpDetails(getTpId());
                httpServletResponse.setContentType("text/xml");
                httpServletResponse.getWriter().write(responseString);
            } catch (Exception exception) {
                LoggerUtility.log(logger, "Exception occurred in getTpDetails method in AjaxHandlerAction:: " + exception.getMessage(), Level.ERROR, exception.getCause());
            }
        }
        return null;
    }

    public String updateTpDetails() {
        if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
            try {
                responseString = ServiceLocator.getAjaxHandlerService().updateTpDetails(this);
                httpServletResponse.setContentType("text/xml");
                httpServletResponse.getWriter().write(responseString);
            } catch (Exception exception) {
                LoggerUtility.log(logger, "Exception occurred in updateTpDetails method in AjaxHandlerAction:: " + exception.getMessage(), Level.ERROR, exception.getCause());
            }
        }
        return null;
    }

    public String getTpDetailInformation() {
        if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
            try {
                responseString = ServiceLocator.getAjaxHandlerService().getTpDetailInformation(getTpId(), httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_DEFAULT_FLOWID).toString());
                httpServletResponse.setContentType("text/xml");
                httpServletResponse.getWriter().write(responseString);
            } catch (Exception exception) {
                LoggerUtility.log(logger, "Exception occurred in getTpDetailInformation method in AjaxHandlerAction:: " + exception.getMessage(), Level.ERROR, exception.getCause());
            }
        }
        return null;
    }

    public String getLogisticsDocDetails() {
        if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
            try {
                responseString = ServiceLocator.getAjaxHandlerService().getLogisticsDocDetails(getIsaNumber(), getId(), getDatabase());
                httpServletResponse.setContentType("text/xml");
                httpServletResponse.getWriter().write(responseString);
            } catch (Exception exception) {
                LoggerUtility.log(logger, "Exception occurred in getLogisticsDocDetails method in AjaxHandlerAction:: " + exception.getMessage(), Level.ERROR, exception.getCause());
            }
        }
        return null;
    }

    public String getLoadTenderingDetails() {
        if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
            try {
                responseString = ServiceLocator.getAjaxHandlerService().getLoadTenderingDetails(getIsaNumber(), getPoNumber(), getDatabase());
                httpServletResponse.setContentType("text/xml");
                httpServletResponse.getWriter().write(responseString);
            } catch (Exception exception) {
                LoggerUtility.log(logger, "Exception occurred in getLoadTenderingDetails method in AjaxHandlerAction:: " + exception.getMessage(), Level.ERROR, exception.getCause());
            }
        }
        return null;
    }

    public String getLtResponseDetails() {
        if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
            try {
                responseString = ServiceLocator.getAjaxHandlerService().getLtResponseDetails(getFileId(), getRefId(), getDatabase());
                httpServletResponse.setContentType("text/xml");
                httpServletResponse.getWriter().write(responseString);
            } catch (Exception exception) {
                LoggerUtility.log(logger, "Exception occurred in getLtResponseDetails method in AjaxHandlerAction:: " + exception.getMessage(), Level.ERROR, exception.getCause());
            }
        }
        return null;
    }

    public String getLogisticsInvDetails() {
        if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
            try {
                responseString = ServiceLocator.getAjaxHandlerService().getLogisticsInvDetails(getInvNumber(), getId(), getDatabase());
                httpServletResponse.setContentType("text/xml");
                httpServletResponse.getWriter().write(responseString);
            } catch (Exception exception) {
                LoggerUtility.log(logger, "Exception occurred in getLogisticsInvDetails method in AjaxHandlerAction:: " + exception.getMessage(), Level.ERROR, exception.getCause());
            }
        }
        return null;
    }

    public String getLogisticsShipmentDetails() {
        if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
            try {
                responseString = ServiceLocator.getAjaxHandlerService().getLogisticsShipmentDetails(getAsnNumber(), getPoNumber(), getId(), getDatabase());
                httpServletResponse.setContentType("text/xml");
                httpServletResponse.getWriter().write(responseString);
            } catch (Exception exception) {
                LoggerUtility.log(logger, "Exception occurred in getLogisticsShipmentDetails method in AjaxHandlerAction:: " + exception.getMessage(), Level.ERROR, exception.getCause());
            }
        }
        return null;
    }

    public String getDocVisibilityDetails() {
        if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
            try {
                responseString = ServiceLocator.getAjaxHandlerService().getDocVisibilityDetails(getDocId());
                httpServletResponse.setContentType("text/xml");
                httpServletResponse.getWriter().write(responseString);
            } catch (Exception exception) {
                LoggerUtility.log(logger, "Exception occurred in getDocVisibilityDetails method in AjaxHandlerAction:: " + exception.getMessage(), Level.ERROR, exception.getCause());
            }
        }
        return null;
    }

    public String getPartnerDetails() {
        if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
            try {
                responseString = ServiceLocator.getAjaxHandlerService().getPartnerDetails(getPartnerId());
                
                httpServletResponse.setContentType("text/xml");
                httpServletResponse.getWriter().write(responseString);
            } catch (Exception exception) {
                LoggerUtility.log(logger, "Exception occurred in getPartnerDetails method in AjaxHandlerAction:: " + exception.getMessage(), Level.ERROR, exception.getCause());
            }
        }
        return null;
    }

    public String getRoutingDetails() {
        if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
            try {
                responseString = ServiceLocator.getAjaxHandlerService().getRoutingDetails(getRoutingId());
                httpServletResponse.setContentType("text/xml");
                httpServletResponse.getWriter().write(responseString);
            } catch (Exception exception) {
                LoggerUtility.log(logger, "Exception occurred in getRoutingDetails method in AjaxHandlerAction:: " + exception.getMessage(), Level.ERROR, exception.getCause());
            }
        }
        return null;
    }

    public String getB2bChannelDetails() {
        if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
            try {
                responseString = ServiceLocator.getAjaxHandlerService().getB2bChannelDetails(getB2bChannelId());
                httpServletResponse.setContentType("text/xml");
                httpServletResponse.getWriter().write(responseString);
            } catch (Exception exception) {
                LoggerUtility.log(logger, "Exception occurred in getB2bChannelDetails method in AjaxHandlerAction:: " + exception.getMessage(), Level.ERROR, exception.getCause());
            }
        }
        return null;
    }

    public String getPartnerInfo() {
        if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
            try {
                responseString = ServiceLocator.getAjaxHandlerService().getPartnerInfo(getPartnerId());
                httpServletResponse.setContentType("text");
                httpServletResponse.getWriter().write(responseString);
            } catch (Exception exception) {
                LoggerUtility.log(logger, "Exception occurred in getPartnerInfo method in AjaxHandlerAction:: " + exception.getMessage(), Level.ERROR, exception.getCause());
            }
        }
        return null;
    }

    public String getRouterInfo() {
        if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
            try {
                responseString = ServiceLocator.getAjaxHandlerService().getRouterInfo(getRouterId());
                httpServletResponse.setContentType("text");
                httpServletResponse.getWriter().write(responseString);
            } catch (Exception exception) {
                LoggerUtility.log(logger, "Exception occurred in getRouterInfo method in AjaxHandlerAction:: " + exception.getMessage(), Level.ERROR, exception.getCause());
            }
        }
        return null;
    }

    public String getBusinessProcessInfo() {
        if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
            try {
                responseString = ServiceLocator.getAjaxHandlerService().getBusinessProcessInfo(getBusinessProcessId());
                httpServletResponse.setContentType("text");
                httpServletResponse.getWriter().write(responseString);
            } catch (Exception exception) {
                LoggerUtility.log(logger, "Exception occurred in getBusinessProcessInfo method in AjaxHandlerAction:: " + exception.getMessage(), Level.ERROR, exception.getCause());
            }
        }
        return null;
    }

    public String getDeliveryChannelDetails() {
        if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
            try {
                responseString = ServiceLocator.getAjaxHandlerService().getDeliveryChannelDetails(getDeliveryChannelId());
                httpServletResponse.setContentType("text/xml");
                httpServletResponse.getWriter().write(responseString);
            } catch (Exception exception) {
                LoggerUtility.log(logger, "Exception occurred in getDeliveryChannelDetails method in AjaxHandlerAction:: " + exception.getMessage(), Level.ERROR, exception.getCause());
            }
        }
        return null;
    }

    public String getDashboardDetails() {
        if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
            try {
                responseString = ServiceLocator.getAjaxHandlerService().getDashboardDetails(this);
                httpServletResponse.setContentType("text");
                httpServletResponse.getWriter().write(responseString);
            } catch (Exception exception) {
                LoggerUtility.log(logger, "Exception occurred in getDashboardDetails method in AjaxHandlerAction:: " + exception.getMessage(), Level.ERROR, exception.getCause());
            }
        }
        return null;
    }

    public String getReportOverlayDetails() {
        if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
            try {
                responseString = ServiceLocator.getAjaxHandlerService().getReportOverlayDetails(getId(), getStartDate()).toString();
                httpServletResponse.setContentType("text/xml");
                httpServletResponse.getWriter().write(responseString);
            } catch (Exception exception) {
                LoggerUtility.log(logger, "Exception occurred in getReportOverlayDetails method in AjaxHandlerAction:: " + exception.getMessage(), Level.ERROR, exception.getCause());
            }
        }
        return null;
    }

    public String forgotPassword() throws Exception {
        try {
            String userid = getUserid();
            String response = ServiceLocator.getAjaxHandlerService().forgotPassword(userid);
            httpServletResponse.setContentType("text/html");
            if (response.equals("success")) {
                httpServletResponse.getWriter().write(SUCCESS);
            } else {
                httpServletResponse.getWriter().write(ERROR);
            }
        } catch (Exception exception) {
            LoggerUtility.log(logger, "Exception occurred in forgotPassword method in AjaxHandlerAction:: " + exception.getMessage(), Level.ERROR, exception.getCause());
        }
        return null;
    }

    public String changePassword() {
        if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
            try {
                String loginId = httpServletRequest.getSession(false).getAttribute(AppConstants.SES_LOGIN_ID).toString();
                int n;
                n = ServiceLocator.getAjaxHandlerService().updateMyPwd(this, loginId);
                if (n == 1) {
                    responseString = "<font color=\"green\" size=\"3\">Password updated successfully</font>";
                } else if (n == 100) {
                    responseString = "<font color=\"red\" size=\"3\">New Password and Confirm Password must be same!</font>";
                } else if (n == 200) {
                    responseString = "<font color=\"red\" size=\"3\">Please enter correct Old Password!</font>";
                } else {
                    responseString = "<font color=\"red\" size=\"3\">Password updation failed!</font>";
                }
                httpServletResponse.setContentType("text/html");
                httpServletResponse.getWriter().write(responseString);

            } catch (Exception exception) {
                LoggerUtility.log(logger, "Exception occurred in changePassword method in AjaxHandlerAction:: " + exception.getMessage(), Level.ERROR, exception.getCause());
            }
        }
        return null;
    }

    public String searchItems() {
        if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
            try {
                int n;
                n = ServiceLocator.getAjaxHandlerService().searchItems(getSenderItem(), getRecItem(), getSelectedName());
                if (n > 0) {
                    responseString = "Failure";
                } else {
                    responseString = "Success";
                }
                httpServletResponse.setContentType("text/html");
                httpServletResponse.getWriter().write(responseString);

            } catch (Exception exception) {
                LoggerUtility.log(logger, "Exception occurred in searchItems method in AjaxHandlerAction:: " + exception.getMessage(), Level.ERROR, exception.getCause());
            }
        }
        return null;
    }

    public String checkCodeListName() {
        if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
            try {
                int n;
                n = ServiceLocator.getAjaxHandlerService().checkCodeListName(getNewListName());
                if (n > 0) {
                    responseString = "Failure";
                } else {
                    responseString = "Success";
                }
                httpServletResponse.setContentType("text/html");
                httpServletResponse.getWriter().write(responseString);

            } catch (Exception exception) {
                LoggerUtility.log(logger, "Exception occurred in checkCodeListName method in AjaxHandlerAction:: " + exception.getMessage(), Level.ERROR, exception.getCause());
            }
        }
        return null;
    }

    public String doCodeVersionUpdate() throws Exception {
        String resultType = LOGIN;
        if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME).toString() != null) {
            try {
                String resultMessage = "";
                String userName = httpServletRequest.getSession(false).getAttribute(AppConstants.SES_LOGIN_ID).toString();
                List codeList1 = new ArrayList();
                codeList1 = (List) httpServletRequest.getSession(false).getAttribute(AppConstants.CODE_LIST);
                int codeListSize = codeList1.size();
                resultMessage = ServiceLocator.getAjaxHandlerService().updateCodeList(getListName(), getJson(), userName, codeListSize);
                httpServletRequest.getSession(false).setAttribute(AppConstants.REQ_RESULT_MSG, resultMessage);
                setListNameMap(DataSourceDataProvider.getInstance().getListName());
                setListName("-1");
                resultType = SUCCESS;
                httpServletResponse.setContentType("text");
                httpServletResponse.getWriter().write(resultMessage);

            } catch (Exception exception) {
                LoggerUtility.log(logger, "Exception occurred in doCodeVersionUpdate method in AjaxHandlerAction:: " + exception.getMessage(), Level.ERROR, exception.getCause());
            }
        }
        return null;
    }

    public String doCodeListAdd() throws Exception {
        String resultType = LOGIN;
        if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME).toString() != null) {
            try {
                String userName = httpServletRequest.getSession(false).getAttribute(AppConstants.SES_LOGIN_ID).toString();
                String resultMessage = "";
                resultMessage = ServiceLocator.getAjaxHandlerService().addCodeList(getJson(), userName, getNewListName());
                httpServletRequest.getSession(false).removeAttribute(AppConstants.CODE_LIST);
                resultType = SUCCESS;
                httpServletResponse.setContentType("text");
                httpServletResponse.getWriter().write(resultMessage);

            } catch (Exception exception) {
                LoggerUtility.log(logger, "Exception occurred in doCodeListAdd method in AjaxHandlerAction:: " + exception.getMessage(), Level.ERROR, exception.getCause());
            }
        }
        return null;
    }

    public String doCodeListDelete() throws Exception {
        String resultType = LOGIN;
        if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME).toString() != null) {
            try {
                String resultMessage = "";
                resultMessage = ServiceLocator.getAjaxHandlerService().deleteCodeList(getJson());
                httpServletRequest.getSession(false).setAttribute(AppConstants.REQ_RESULT_MSG, resultMessage);
                setListNameMap(DataSourceDataProvider.getInstance().getListName());
                resultType = SUCCESS;
                httpServletResponse.setContentType("text");
                httpServletResponse.getWriter().write(resultMessage);

            } catch (Exception exception) {
                LoggerUtility.log(logger, "Exception occurred in doCodeListDelete method in AjaxHandlerAction:: " + exception.getMessage(), Level.ERROR, exception.getCause());
            }
        }
        return null;
    }

    public String getInventoryDetails() {
        if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
            try {
                responseString = ServiceLocator.getAjaxHandlerService().getInventoryDetails(getFileId(), getId(), getDatabase());
                httpServletResponse.setContentType("text/xml");
                httpServletResponse.getWriter().write(responseString);
            } catch (Exception exception) {
                LoggerUtility.log(logger, "Exception occurred in getInventoryDetails method in AjaxHandlerAction:: " + exception.getMessage(), Level.ERROR, exception.getCause());
            }
        }
        return null;
    }

    public String doGetMapList() {
        if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
            try {
                responseString = ServiceLocator.getAjaxHandlerService().getMapNamesList(getName());
                httpServletResponse.setContentType("text");
                httpServletResponse.getWriter().write(responseString);
            } catch (Exception exception) {
                LoggerUtility.log(logger, "Exception occurred in doGetMapList method in AjaxHandlerAction:: " + exception.getMessage(), Level.ERROR, exception.getCause());
            }
        }
        return null;
    }

    public String doGetProcessFlow() {
        if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
            try {
                responseString = ServiceLocator.getAjaxHandlerService().doGetProcessFlow(getJsonData());
                httpServletResponse.setContentType("text");
                httpServletResponse.getWriter().write(responseString);
            } catch (Exception exception) {
                LoggerUtility.log(logger, "Exception occurred in doGetProcessFlow method in AjaxHandlerAction:: " + exception.getMessage(), Level.ERROR, exception.getCause());
            }
        }
        return null;
    }

    public String doSaveProcessFlow() {
        if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
            try {
                String userName = httpServletRequest.getSession(false).getAttribute(AppConstants.SES_LOGIN_ID).toString();
                responseString = ServiceLocator.getAjaxHandlerService().doSaveProcessFlow(getJsonData(), userName);
                httpServletResponse.setContentType("text");
                httpServletResponse.getWriter().write(responseString);
            } catch (Exception exception) {
                LoggerUtility.log(logger, "Exception occurred in doSaveProcessFlow method in AjaxHandlerAction:: " + exception.getMessage(), Level.ERROR, exception.getCause());
            }
        }
        return null;
    }

    public String doGetMailBoxList() {
        if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
            try {
                responseString = ServiceLocator.getAjaxHandlerService().getMailBoxList(getName());
                httpServletResponse.setContentType("text");
                httpServletResponse.getWriter().write(responseString);
            } catch (Exception exception) {
                LoggerUtility.log(logger, "Exception occurred in doGetMailBoxList method in AjaxHandlerAction:: " + exception.getMessage(), Level.ERROR, exception.getCause());
            }
        }
        return null;
    }

    public String doGetTpList() {
        if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
            try {
                responseString = ServiceLocator.getAjaxHandlerService().doGetTpList(getName());
                httpServletResponse.setContentType("text");
                httpServletResponse.getWriter().write(responseString);
            } catch (Exception exception) {
                LoggerUtility.log(logger, "Exception occurred in doGetTpList method in AjaxHandlerAction:: " + exception.getMessage(), Level.ERROR, exception.getCause());
            }
        }
        return null;
    }

    public String getTransactionsBasedOnType() {
        if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
            try {
                responseString = ServiceLocator.getAjaxHandlerService().getTransactionsBasedOnType(getFlag());
                httpServletResponse.setContentType("text/xml");
                httpServletResponse.getWriter().write(responseString);
            } catch (Exception exception) {
                LoggerUtility.log(logger, "Exception occurred in getTransactionsBasedOnType method in AjaxHandlerAction:: " + exception.getMessage(), Level.ERROR, exception.getCause());
            }
        }
        return null;
    }

    public String getPartnerAndDocType() {
        if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
            try {
                responseString = ServiceLocator.getAjaxHandlerService().getPartnerAndDocType(getFlag());
                httpServletResponse.setContentType("text/xml");
                httpServletResponse.getWriter().write(responseString);
            } catch (Exception exception) {
                LoggerUtility.log(logger, "Exception occurred in getPartnerAndDocType method in AjaxHandlerAction:: " + exception.getMessage(), Level.ERROR, exception.getCause());
            }
        }
        return null;
    }

    public String getTransactionReferences() {
        if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
            try {
                responseString = ServiceLocator.getAjaxHandlerService().getTransactionReferences(getFileId(), getDocType(), getSenderId(), getReceiverId());
                httpServletResponse.setContentType("text/xml");
                httpServletResponse.getWriter().write(responseString);
            } catch (Exception exception) {
                LoggerUtility.log(logger, "Exception occurred in getTransactionReferences method in AjaxHandlerAction:: " + exception.getMessage(), Level.ERROR, exception.getCause());
            }
        }
        return null;
    }

    public String getPrePosttranslationPathData() {
        if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
            try {
                responseString = ServiceLocator.getAjaxHandlerService().getPrePosttranslationPathData(getPrePostTranslationpath());
                httpServletResponse.setContentType("text/html");
                httpServletResponse.getWriter().write(responseString);
            } catch (Exception exception) {
                LoggerUtility.log(logger, "Exception occurred in getPrePosttranslationPathData method in AjaxHandlerAction:: " + exception.getMessage(), Level.ERROR, exception.getCause());
            }
        }
        return null;
    }
    
     public String appConfigEditTimeinterval() {
         System.out.println("entered inappConfigEditTimeinterval method");
        if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
            try {
                String username = (String) httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME);
                String responseString = ServiceLocator.getAjaxHandlerService().appConfigEditTimeinterval(username, getTimeInterval());
                httpServletResponse.setContentType("text");
                httpServletResponse.getWriter().write(responseString);
            } catch (Exception ex) {
                LoggerUtility.log(logger, " Exception occurred in appConfigEditTimeinterval :: " + ex.getMessage(), Level.ERROR, ex.getCause());
            }
        }
        System.out.println("ended in appConfigEditTimeinterval method");
        return null;
    }

     public String appConfigEditTop10EDITP() {
         System.out.println("entered in appConfigEditTop10EDITP method");
        if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
            try {
                String username = (String) httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME);
                String responseString = ServiceLocator.getAjaxHandlerService().appConfigEditTop10EDITP(username, getAllEDItp());
                httpServletResponse.setContentType("text");
                httpServletResponse.getWriter().write(responseString);
            } catch (Exception ex) {
                LoggerUtility.log(logger, " Exception occurred in appConfigEditTop10EDITP :: " + ex.getMessage(), Level.ERROR, ex.getCause());
            }
        }
        System.out.println("ended in appConfigEditTop10EDITP method");
        return null;
    }
     
         public String appConfigEditTop10RailTP() {
        if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
            try {
                String username = (String) httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME);
                String responseString = ServiceLocator.getAjaxHandlerService().appConfigEditTop10RailTP(username, getAllRailtp());
                httpServletResponse.setContentType("text");
                httpServletResponse.getWriter().write(responseString);
            } catch (Exception ex) {
                LoggerUtility.log(logger, " Exception occurred in appConfigEditTop10RailTP :: " + ex.getMessage(), Level.ERROR, ex.getCause());
            }
        }
        return null;
    }

     
      public String appConfigEditScvpDatabaseValues() {
          System.out.println("entered in appConfigEditScvpDatabaseValues method");
        if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
            try {
                String username = (String) httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME);
                String responseString = ServiceLocator.getAjaxHandlerService().appConfigEditScvpDatabaseValues(username, getDbData());
                httpServletResponse.setContentType("text");
                httpServletResponse.getWriter().write(responseString);
            } catch (Exception ex) {
                LoggerUtility.log(logger, " Exception occurred in appConfigEditScvpDatabaseValues :: " + ex.getMessage(), Level.ERROR, ex.getCause());
            }
        }
        System.out.println("ended in appConfigEditScvpDatabaseValues method");
        return null;
    }

      public String appConfigEditTransactions() {
          System.out.println("entered in appConfigEditTransactions method");
        if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
            try {
                String username = (String) httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME);
                String responseString1 = ServiceLocator.getAjaxHandlerService().appConfigEditEDITransactions(username, getEdiTransactionNamesList());
                String responseString2 = ServiceLocator.getAjaxHandlerService().appConfigEditRAILTransactions(username, getRailTransactionNamesList());
                httpServletResponse.setContentType("text");
                httpServletResponse.getWriter().write(responseString1);
            } catch (Exception ex) {
                LoggerUtility.log(logger, " Exception occurred in appConfigEditTransactions :: " + ex.getMessage(), Level.ERROR, ex.getCause());
            }
        }
        System.out.println("ended in appConfigEditTransactions method");
        return null;
    }

      public String appConfigEditBaseValues() {
          System.out.println("entered in appConfigEditBaseValues method");
        if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
            try {
                String username = (String) httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME);
                String responseString = ServiceLocator.getAjaxHandlerService().appConfigEditBaseValues(username, getBaseValueData());
                httpServletResponse.setContentType("text");
                httpServletResponse.getWriter().write(responseString);
            } catch (Exception ex) {
                LoggerUtility.log(logger, " Exception occurred in appConfigEditBaseValues :: " + ex.getMessage(), Level.ERROR, ex.getCause());
            }
        }
        System.out.println("ended in appConfigEditBaseValues method");
        return null;
    }

     
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setServletRequest(HttpServletRequest httpServletRequest) {
        this.httpServletRequest = httpServletRequest;
    }

    public void setServletResponse(HttpServletResponse httpServletResponse) {
        this.httpServletResponse = httpServletResponse;
    }

    public String getPoNumber() {
        return poNumber;
    }

    public void setPoNumber(String poNumber) {
        this.poNumber = poNumber;
    }

    public String getAsnNumber() {
        return asnNumber;
    }

    public void setAsnNumber(String asnNumber) {
        this.asnNumber = asnNumber;
    }

    public String getInvNumber() {
        return invNumber;
    }

    public void setInvNumber(String invNumber) {
        this.invNumber = invNumber;
    }

    public String getIsaNumber() {
        return isaNumber;
    }

    public void setIsaNumber(String isaNumber) {
        this.isaNumber = isaNumber;
    }

    public String getPoList() {
        return poList;
    }

    public void setPoList(String poList) {
        this.poList = poList;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPoInst() {
        return poInst;
    }

    public void setPoInst(String poInst) {
        this.poInst = poInst;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getTpId() {
        return tpId;
    }

    public void setTpId(String tpId) {
        this.tpId = tpId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public String getCommid() {
        return commid;
    }

    public void setCommid(String commid) {
        this.commid = commid;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getPhno() {
        return phno;
    }

    public void setPhno(String phno) {
        this.phno = phno;
    }

    public String getQualifier() {
        return qualifier;
    }

    public void setQualifier(String qualifier) {
        this.qualifier = qualifier;
    }

    public String getRefId() {
        return refId;
    }

    public void setRefId(String refId) {
        this.refId = refId;
    }

    public String getLoadList() {
        return loadList;
    }

    public void setLoadList(String loadList) {
        this.loadList = loadList;
    }

    public int getDocId() {
        return docId;
    }

    public void setDocId(int docId) {
        this.docId = docId;
    }

    public String getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(String partnerId) {
        this.partnerId = partnerId;
    }

    public String getRoutingId() {
        return routingId;
    }

    public void setRoutingId(String routingId) {
        this.routingId = routingId;
    }

    public String getB2bChannelId() {
        return b2bChannelId;
    }

    public void setB2bChannelId(String b2bChannelId) {
        this.b2bChannelId = b2bChannelId;
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

    public int getDeliveryChannelId() {
        return deliveryChannelId;
    }

    public void setDeliveryChannelId(int deliveryChannelId) {
        this.deliveryChannelId = deliveryChannelId;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getDocType() {
        return docType;
    }

    public void setDocType(String docType) {
        this.docType = docType;
    }

    public String getAckStatus() {
        return ackStatus;
    }

    public void setAckStatus(String ackStatus) {
        this.ackStatus = ackStatus;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getAsnList() {
        return asnList;
    }

    public void setAsnList(String asnList) {
        this.asnList = asnList;
    }

    public String getInvList() {
        return invList;
    }

    public void setInvList(String invList) {
        this.invList = invList;
    }

    public String getPaymentList() {
        return paymentList;
    }

    public void setPaymentList(String paymentList) {
        this.paymentList = paymentList;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }
    private String userid;

    public String getCnfrmPwd() {
        return cnfrmPwd;
    }

    public void setCnfrmPwd(String cnfrmPwd) {
        this.cnfrmPwd = cnfrmPwd;
    }

    public String getNewPwd() {
        return newPwd;
    }

    public void setNewPwd(String newPwd) {
        this.newPwd = newPwd;
    }

    public String getOldPwd() {
        return oldPwd;
    }

    public void setOldPwd(String oldPwd) {
        this.oldPwd = oldPwd;
    }

    public String getSenderItem() {
        return senderItem;
    }

    public void setSenderItem(String senderItem) {
        this.senderItem = senderItem;
    }

    public String getRecItem() {
        return recItem;
    }

    public void setRecItem(String recItem) {
        this.recItem = recItem;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getSelectedName() {
        return selectedName;
    }

    public void setSelectedName(String selectedName) {
        this.selectedName = selectedName;
    }

    public String getNewListName() {
        return newListName;
    }

    public void setNewListName(String newListName) {
        this.newListName = newListName;
    }

    public List getListNameMap() {
        return listNameMap;
    }

    public void setListNameMap(List listNameMap) {
        this.listNameMap = listNameMap;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    public String getListName() {
        return listName;
    }

    public void setListName(String listName) {
        this.listName = listName;
    }

    public String getResponseString() {
        return responseString;
    }

    public void setResponseString(String responseString) {
        this.responseString = responseString;
    }

    public int getItems() {
        return items;
    }

    public void setItems(int items) {
        this.items = items;
    }

    public String getModifieddate() {
        return modifieddate;
    }

    public void setModifieddate(String modifieddate) {
        this.modifieddate = modifieddate;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getJsonData() {
        return jsonData;
    }

    public void setJsonData(String jsonData) {
        this.jsonData = jsonData;
    }

    public String getPrePostTranslationpath() {
        return prePostTranslationpath;
    }

    public void setPrePostTranslationpath(String prePostTranslationpath) {
        this.prePostTranslationpath = prePostTranslationpath;
    }

    public String getTimeInterval() {
        return timeInterval;
    }

    public void setTimeInterval(String timeInterval) {
        this.timeInterval = timeInterval;
    }

    public String getAllEDItp() {
        return allEDItp;
    }

    public void setAllEDItp(String allEDItp) {
        this.allEDItp = allEDItp;
    }

    public String getDbData() {
        return dbData;
    }

    public void setDbData(String dbData) {
        this.dbData = dbData;
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

    public String getBaseValueData() {
        return baseValueData;
    }

    public void setBaseValueData(String baseValueData) {
        this.baseValueData = baseValueData;
    }

    public String getAllRailtp() {
        return allRailtp;
    }

    public void setAllRailtp(String allRailtp) {
        this.allRailtp = allRailtp;
    }

}
