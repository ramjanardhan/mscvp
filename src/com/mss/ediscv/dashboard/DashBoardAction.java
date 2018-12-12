/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mss.ediscv.dashboard;

import com.mss.ediscv.util.AppConstants;
import com.mss.ediscv.util.DataSourceDataProvider;
import com.mss.ediscv.util.DateUtility;
import com.mss.ediscv.util.ServiceLocator;
import com.mss.ediscv.util.ServiceLocatorException;
import static com.opensymphony.xwork2.Action.LOGIN;
import static com.opensymphony.xwork2.Action.SUCCESS;
import com.opensymphony.xwork2.ActionSupport;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.mss.ediscv.util.LoggerUtility;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.json.JSONArray;

/**
 * @author Narendar
 */
public class DashBoardAction extends ActionSupport implements ServletRequestAware {

    private HttpServletRequest httpServletRequest;
    String resultMessage;
    private String resultType;
    private JSONArray dailyEdiStats;
    private JSONArray ediTransactionStats;
    private JSONArray ediDocumentsVolume;
    private JSONArray ediTpVolume;
    private List docTypeList;
    private List yearList;
    private String topTradingPartners;
    private String xAxisTimeInterval;
    private String xAxisMonthInterval;
    private String direction;
    private String docType;
    private String docdatepicker;
    private String docdatepickerfrom;
    private String fromYear;
    private String fromMonth;
    private String yearmonth;
    private int baseValue;
    private ArrayList dailyStatsEdiDocuments;
    private ArrayList dailyStatsViewInboundCount;
    private ArrayList dailyStatsViewOutboundCount;
    private String monthlyVolumeXAxisValues;
    private ArrayList topTpEdiPartnersInboundCount;
    private ArrayList topTpEdiPartnersOutboundCount;
    private String top10EdiTpPartners;
    private JSONArray dailyFailureRate;
    private Map partnerMap;
    private String tpPartnerId;
    private String businessFlows;
    private DashBoardBean dashBoardBean;
    private ArrayList<DashBoardBean> q2cDashboardList;
    private int monthlyValumeIbCount;
    private int monthlyValumeObCount;

    private ArrayList ediDocuments204IB;
    private ArrayList ediDocuments204OB;
    private ArrayList ediDocuments990IB;
    private ArrayList ediDocuments990OB;
    private ArrayList ediDocuments214IB;
    private ArrayList ediDocuments214OB;
    private ArrayList ediDocuments210IB;
    private ArrayList ediDocuments210OB;

    private ArrayList ediDocuments850IB;
    private ArrayList ediDocuments850OB;
    private ArrayList ediDocuments810IB;
    private ArrayList ediDocuments810OB;
    private ArrayList ediDocuments856IB;
    private ArrayList ediDocuments856OB;
    private ArrayList ediDocuments855IB;
    private ArrayList ediDocuments855OB;

    private String yearOfMonthlyVolumeXAxisValues;
    private String monthlyOfDaysVolumeXAxisValues;
    private ArrayList tpBusinessYearTransTrends;
    private ArrayList tpBusinessMonthTransTrends;
    private ArrayList ediBusinessYearTransTrends;
    private ArrayList ediBusinessMonthTransTrends;

    private char flag;

    private static Logger logger = LogManager.getLogger(DashBoardAction.class.getName());

    public String getDailyStatsView() throws ServiceLocatorException {
        System.out.println("getDailyStatsView start time::" + DateUtility.getInstance().getCurrentDB2Timestamp());
        setResultType(LOGIN);
        if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
            try {

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                setBaseValue(Integer.parseInt(com.mss.ediscv.util.Properties.getProperty("DocumentsBaseValue")));

                ArrayList timeInervalArray = DateUtility.getInstance().getdailyHours();
                setDailyEdiStats(ServiceLocator.DashBoardService().getDailyEdiStats(getFlag()));

                setDailyStatsEdiDocuments(ServiceLocator.DashBoardService().getDailyStatsEDIDocuments(timeInervalArray, sdf, getFlag()));

                setDailyFailureRate(ServiceLocator.DashBoardService().getDailyFailureRate(getFlag()));

                setTopTpEdiPartnersInboundCount(ServiceLocator.DashBoardService().getTopTpEdiInboundCount(getFlag()));
                setTopTpEdiPartnersOutboundCount(ServiceLocator.DashBoardService().getTopTpEdiOutboundCount(getFlag()));

                setDailyStatsViewInboundCount(ServiceLocator.DashBoardService().getDSVIBMonthlyVolumes(getFlag()));
                setMonthlyValumeIbCount(ServiceLocator.DashBoardService().getMonthlyIbVolumes(getFlag()));
                setDailyStatsViewOutboundCount(ServiceLocator.DashBoardService().getDSVOBMonthlyVolumes(getFlag()));
                setMonthlyValumeObCount(ServiceLocator.DashBoardService().getMonthlyObVolumes(getFlag()));

                timeInervalArray.remove(0);
                String x = timeInervalArray.toString();
                setXAxisTimeInterval(x.substring(1, ((x.length()) - 1)));
                ArrayList monthlyVolumeXAxisValues = DateUtility.getInstance().getDaysInMonth();
                String x1 = monthlyVolumeXAxisValues.toString();
                setMonthlyVolumeXAxisValues(x1.substring(1, ((x1.length()) - 1)));
                if (flag == 'M') {
                    setTop10EdiTpPartners(com.mss.ediscv.util.Properties.getProperty("Top10EdiTradingPartners"));
                } else if (flag == 'L') {
                    setTop10EdiTpPartners(com.mss.ediscv.util.Properties.getProperty("Top10LogisticsTradingPartners"));
                }

                setResultType(SUCCESS);
            } catch (Exception exception) {
                LoggerUtility.log(logger, "Exception occurred in getDailyStatsView method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
                httpServletRequest.getSession(false).setAttribute(AppConstants.REQ_EXCEPTION_MSG, exception.getMessage());
                setResultType("error");
            }
        }
        System.out.println("getDailyStatsView end time::" + DateUtility.getInstance().getCurrentDB2Timestamp());
        return getResultType();

    }

    public String getBusinessFlow() throws ServiceLocatorException {
        System.out.println("getBusinessFlow start time::" + DateUtility.getInstance().getCurrentDB2Timestamp());
        setResultType(LOGIN);
        if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
            try {

                setEdiTransactionStats(ServiceLocator.DashBoardService().getEdiTransactionStats(getFlag()));

                setEdiDocumentsVolume(ServiceLocator.DashBoardService().getEdiDocumentsVolume(getFlag()));

                setEdiTpVolume(ServiceLocator.DashBoardService().getEdiTradingPartnerVolume(getFlag()));

                setResultType(SUCCESS);
            } catch (Exception exception) {
                LoggerUtility.log(logger, "Exception occurred in getBusinessFlow method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
                httpServletRequest.getSession(false).setAttribute(AppConstants.REQ_EXCEPTION_MSG, exception.getMessage());
                setResultType("error");
            }
        }
        System.out.println("getBusinessFlow end time::" + DateUtility.getInstance().getCurrentDB2Timestamp());
        return getResultType();

    }

    public String getEDIDocuments() {
        setResultType(LOGIN);
        if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                setBaseValue(Integer.parseInt(com.mss.ediscv.util.Properties.getProperty("DocumentsBaseValue")));
                ArrayList timeInervalArray = DateUtility.getInstance().LastXHoursYMinutesInterval();
                if (timeInervalArray.size() > 1) {
                    ServiceLocator.DashBoardService().getTransactionalVolumes(timeInervalArray, sdf, this);

                    if (getFlag() == 'L') {
                        setResultType("LSUCCESS");
                    } else {
                        setResultType("MSUCCESS");

                    }
                    timeInervalArray.remove(0);
                    String x = timeInervalArray.toString();
                    setXAxisTimeInterval(x.substring(1, ((x.length()) - 1)));
                } else {
                    httpServletRequest.getSession(false).setAttribute(AppConstants.REQ_RESULT_MSG, "<font color='red'>Please try after 12:15 AM</font>");
                }
            } catch (Exception exception) {
                LoggerUtility.log(logger, "Exception occurred in getEDIDocuments method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
                httpServletRequest.getSession(false).setAttribute(AppConstants.REQ_EXCEPTION_MSG, exception.getMessage());
                setResultType("error");
            }
        }

        return getResultType();

    }

    public String getBusinessTrendsDocument() throws ServiceLocatorException {
        System.out.println("getBusinessTrendsDocument start time::" + DateUtility.getInstance().getCurrentDB2Timestamp());
        setResultType(LOGIN);
        if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
            try {
                String type = httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_FLOW_MAP).toString();
                if (type.contains("Logistics")) {
                    setDocTypeList(DataSourceDataProvider.getInstance().getDocumentTypeList("L"));
                } else {
                    setDocTypeList(DataSourceDataProvider.getInstance().getDocumentTypeList("M"));
                }
                setYearList(DateUtility.getInstance().getYearlyList());
                if ("Monthly".equals(getYearmonth())) {
                    setYearmonth("Monthly");
                } else {
                    setYearmonth("Yearly");
                }
                setResultType(SUCCESS);
            } catch (Exception exception) {
                LoggerUtility.log(logger, "Exception occurred in getBusinessTrendsDocument method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
                httpServletRequest.getSession(false).setAttribute(AppConstants.REQ_EXCEPTION_MSG, exception.getMessage());
                setResultType("error");
            }
        }
        System.out.println("getBusinessTrendsDocument end time::" + DateUtility.getInstance().getCurrentDB2Timestamp());
        return getResultType();
    }

    public String ediBusinessSearchTrends() {
        setResultType(LOGIN);
        if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
            try {
                if (getBusinessFlows().equalsIgnoreCase("Logistics")) {
                    setDocTypeList(DataSourceDataProvider.getInstance().getDocumentTypeList("L"));
                } else {
                    setDocTypeList(DataSourceDataProvider.getInstance().getDocumentTypeList("M"));
                }
                setYearList(DateUtility.getInstance().getYearlyList());
                if ("Monthly".equals(getYearmonth())) {
                    setYearmonth("Monthly");
                } else {
                    setYearmonth("Yearly");
                }

                if (getYearmonth().equalsIgnoreCase("Monthly")) {

                    if ((!"-1".equals(getFromYear()) && getFromYear() != null) && (!"-1".equals(getFromMonth()) && getFromMonth() != null)) {

                        ArrayList XaxisMonthOfDays = DateUtility.getInstance().XaxisMonthofDays(getFromYear(), getFromMonth());

                        String monthx1 = XaxisMonthOfDays.toString();

                        setMonthlyOfDaysVolumeXAxisValues(monthx1.substring(1, ((monthx1.length()) - 1)));

                        ArrayList ediBusinessMonthTrend = ServiceLocator.DashBoardService().getEdiBusinessMonthsTrendsResult(this);

                        setEdiBusinessMonthTransTrends(ediBusinessMonthTrend);
                    }
                } else {

                    if (!"-1".equals(getFromYear()) && getFromYear() != null) {

                        ArrayList XaxisYearOfMonth = DateUtility.getInstance().XaxisYearofMonth(getFromYear());

                        String yearx1 = XaxisYearOfMonth.toString();

                        setYearOfMonthlyVolumeXAxisValues(yearx1.substring(1, ((yearx1.length()) - 1)));

                        ArrayList ediBusinessYearTrend = ServiceLocator.DashBoardService().getEdiBusinessYearsTrendsResult(this);

                        setEdiBusinessYearTransTrends(ediBusinessYearTrend);
                    }
                }

                setResultType(SUCCESS);
            } catch (Exception exception) {
                LoggerUtility.log(logger, "Exception occurred in ediBusinessSearchTrends method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
                httpServletRequest.getSession(false).setAttribute(AppConstants.REQ_EXCEPTION_MSG, exception.getMessage());
                setResultType("error");
            }
        }

        return getResultType();
    }

    public String getBusinessTrendsTP() throws ServiceLocatorException {
        System.out.println("getBusinessTrendsTP start time::" + DateUtility.getInstance().getCurrentDB2Timestamp());
        setResultType(LOGIN);
        try {
            if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
                String type = httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_FLOW_MAP).toString();
                setYearList(DateUtility.getInstance().getYearlyList());
                if (type.contains("Logistics")) {
                    setDocTypeList(DataSourceDataProvider.getInstance().getDocumentTypeList("L"));
                    setPartnerMap(DataSourceDataProvider.getInstance().getDashboardPartnerMap("3"));
                } else {
                    setDocTypeList(DataSourceDataProvider.getInstance().getDocumentTypeList("M"));
                    setPartnerMap(DataSourceDataProvider.getInstance().getDashboardPartnerMap("2"));
                }
                if ("Monthly".equals(getYearmonth())) {
                    setYearmonth("Monthly");
                } else {
                    setYearmonth("Yearly");
                }
                setResultType(SUCCESS);
            }

        } catch (Exception exception) {
            LoggerUtility.log(logger, "Exception occurred in getBusinessTrendsTP method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
        }
        System.out.println("getBusinessTrendsTP start time::" + DateUtility.getInstance().getCurrentDB2Timestamp());
        return getResultType();
    }

    public String tpBusinessSearchTrends() {
        setResultType(LOGIN);
        if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
            try {
                if (getBusinessFlows().equalsIgnoreCase("Logistics")) {
                    setDocTypeList(DataSourceDataProvider.getInstance().getDocumentTypeList("L"));
                    setPartnerMap(DataSourceDataProvider.getInstance().getDashboardPartnerMap("3"));
                } else {
                    setDocTypeList(DataSourceDataProvider.getInstance().getDocumentTypeList("M"));
                    setPartnerMap(DataSourceDataProvider.getInstance().getDashboardPartnerMap("2"));
                }
                setYearList(DateUtility.getInstance().getYearlyList());
                if ("Monthly".equals(getYearmonth())) {
                    setYearmonth("Monthly");
                } else {
                    setYearmonth("Yearly");
                }
                if (getYearmonth().equalsIgnoreCase("Monthly")) {
                    if ((!"-1".equals(getFromYear()) && getFromYear() != null) && (!"-1".equals(getFromMonth()) && getFromMonth() != null)) {
                        ArrayList XaxisMonthOfDays = DateUtility.getInstance().XaxisMonthofDays(getFromYear(), getFromMonth());
                        String monthx1 = XaxisMonthOfDays.toString();
                        setMonthlyOfDaysVolumeXAxisValues(monthx1.substring(1, ((monthx1.length()) - 1)));
                        ArrayList tpBusinessMonthTrend = ServiceLocator.DashBoardService().getTpBusinessMonthsTrendsResult(this);
                        setTpBusinessMonthTransTrends(tpBusinessMonthTrend);
                    }
                } else {
                    if (!"-1".equals(getFromYear()) && getFromYear() != null) {
                        ArrayList XaxisYearOfMonth = DateUtility.getInstance().XaxisYearofMonth(getFromYear());
                        String yearx1 = XaxisYearOfMonth.toString();
                        setYearOfMonthlyVolumeXAxisValues(yearx1.substring(1, ((yearx1.length()) - 1)));
                        ArrayList tpBusinessYearTrend = ServiceLocator.DashBoardService().getTpBusinessYearsTrendsResult(this);
                        setTpBusinessYearTransTrends(tpBusinessYearTrend);
                    }
                }
                setResultType(SUCCESS);
            } catch (Exception exception) {
                LoggerUtility.log(logger, "Exception occurred in tpBusinessSearchTrends method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
                httpServletRequest.getSession(false).setAttribute(AppConstants.REQ_EXCEPTION_MSG, exception.getMessage());
                setResultType("error");
            }
        }
        return getResultType();
    }

    public String getResultType() {
        return resultType;
    }

    public void setResultType(String resultType) {
        this.resultType = resultType;
    }

    @Override
    public void setServletRequest(HttpServletRequest httpServletRequest) {
        this.httpServletRequest = httpServletRequest;
    }

    public JSONArray getEdiTransactionStats() {
        return ediTransactionStats;
    }

    public void setEdiTransactionStats(JSONArray ediTransactionStats) {
        this.ediTransactionStats = ediTransactionStats;
    }

    public JSONArray getEdiDocumentsVolume() {
        return ediDocumentsVolume;
    }

    public void setEdiDocumentsVolume(JSONArray ediDocumentsVolume) {
        this.ediDocumentsVolume = ediDocumentsVolume;
    }

    public JSONArray getEdiTpVolume() {
        return ediTpVolume;
    }

    public void setEdiTpVolume(JSONArray ediTpVolume) {
        this.ediTpVolume = ediTpVolume;
    }

    public List getDocTypeList() {
        return docTypeList;
    }

    public void setDocTypeList(List docTypeList) {
        this.docTypeList = docTypeList;
    }

    public String getXAxisTimeInterval() {
        return xAxisTimeInterval;
    }

    public void setXAxisTimeInterval(String xAxisTimeInterval) {
        this.xAxisTimeInterval = xAxisTimeInterval;
    }

    public int getBaseValue() {
        return baseValue;
    }

    public void setBaseValue(int baseValue) {
        this.baseValue = baseValue;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getDocType() {
        return docType;
    }

    public void setDocType(String docType) {
        this.docType = docType;
    }

    public String getDocdatepicker() {
        return docdatepicker;
    }

    public void setDocdatepicker(String docdatepicker) {
        this.docdatepicker = docdatepicker;
    }

    public String getDocdatepickerfrom() {
        return docdatepickerfrom;
    }

    public void setDocdatepickerfrom(String docdatepickerfrom) {
        this.docdatepickerfrom = docdatepickerfrom;
    }

    public String getxAxisMonthInterval() {
        return xAxisMonthInterval;
    }

    public void setxAxisMonthInterval(String xAxisMonthInterval) {
        this.xAxisMonthInterval = xAxisMonthInterval;
    }

    public List getYearList() {
        return yearList;
    }

    public void setYearList(List yearList) {
        this.yearList = yearList;
    }

    public String getFromYear() {
        return fromYear;
    }

    public void setFromYear(String fromYear) {
        this.fromYear = fromYear;
    }

    public String getYearmonth() {
        return yearmonth;
    }

    public void setYearmonth(String yearmonth) {
        this.yearmonth = yearmonth;
    }

    public String getFromMonth() {
        return fromMonth;
    }

    public void setFromMonth(String fromMonth) {
        this.fromMonth = fromMonth;
    }

    public String getTopTradingPartners() {
        return topTradingPartners;
    }

    public void setTopTradingPartners(String topTradingPartners) {
        this.topTradingPartners = topTradingPartners;
    }

    public JSONArray getDailyEdiStats() {
        return dailyEdiStats;
    }

    public void setDailyEdiStats(JSONArray dailyEdiStats) {
        this.dailyEdiStats = dailyEdiStats;
    }

    public ArrayList getDailyStatsEdiDocuments() {
        return dailyStatsEdiDocuments;
    }

    public void setDailyStatsEdiDocuments(ArrayList dailyStatsEdiDocuments) {
        this.dailyStatsEdiDocuments = dailyStatsEdiDocuments;
    }

    public ArrayList getDailyStatsViewInboundCount() {
        return dailyStatsViewInboundCount;
    }

    public void setDailyStatsViewInboundCount(ArrayList dailyStatsViewInboundCount) {
        this.dailyStatsViewInboundCount = dailyStatsViewInboundCount;
    }

    public ArrayList getDailyStatsViewOutboundCount() {
        return dailyStatsViewOutboundCount;
    }

    public void setDailyStatsViewOutboundCount(ArrayList dailyStatsViewOutboundCount) {
        this.dailyStatsViewOutboundCount = dailyStatsViewOutboundCount;
    }

    public String getMonthlyVolumeXAxisValues() {
        return monthlyVolumeXAxisValues;
    }

    public void setMonthlyVolumeXAxisValues(String monthlyVolumeXAxisValues) {
        this.monthlyVolumeXAxisValues = monthlyVolumeXAxisValues;
    }

    public JSONArray getDailyFailureRate() {
        return dailyFailureRate;
    }

    public void setDailyFailureRate(JSONArray dailyFailureRate) {
        this.dailyFailureRate = dailyFailureRate;
    }

    public String getxAxisTimeInterval() {
        return xAxisTimeInterval;
    }

    public void setxAxisTimeInterval(String xAxisTimeInterval) {
        this.xAxisTimeInterval = xAxisTimeInterval;
    }

    public Map getPartnerMap() {
        return partnerMap;
    }

    public void setPartnerMap(Map partnerMap) {
        this.partnerMap = partnerMap;
    }

    public String getTpPartnerId() {
        return tpPartnerId;
    }

    public void setTpPartnerId(String tpPartnerId) {
        this.tpPartnerId = tpPartnerId;
    }

    public String getBusinessFlows() {
        return businessFlows;
    }

    public void setBusinessFlows(String businessFlows) {
        this.businessFlows = businessFlows;
    }

    public DashBoardBean getDashBoardBean() {
        return dashBoardBean;
    }

    public void setDashBoardBean(DashBoardBean dashBoardBean) {
        this.dashBoardBean = dashBoardBean;
    }

    public String getTop10EdiTpPartners() {
        return top10EdiTpPartners;
    }

    public void setTop10EdiTpPartners(String top10EdiTpPartners) {
        this.top10EdiTpPartners = top10EdiTpPartners;
    }

    public ArrayList getTopTpEdiPartnersInboundCount() {
        return topTpEdiPartnersInboundCount;
    }

    public void setTopTpEdiPartnersInboundCount(ArrayList topTpEdiPartnersInboundCount) {
        this.topTpEdiPartnersInboundCount = topTpEdiPartnersInboundCount;
    }

    public ArrayList getTopTpEdiPartnersOutboundCount() {
        return topTpEdiPartnersOutboundCount;
    }

    public void setTopTpEdiPartnersOutboundCount(ArrayList topTpEdiPartnersOutboundCount) {
        this.topTpEdiPartnersOutboundCount = topTpEdiPartnersOutboundCount;
    }

    public int getMonthlyValumeIbCount() {
        return monthlyValumeIbCount;
    }

    public void setMonthlyValumeIbCount(int monthlyValumeIbCount) {
        this.monthlyValumeIbCount = monthlyValumeIbCount;
    }

    public int getMonthlyValumeObCount() {
        return monthlyValumeObCount;
    }

    public void setMonthlyValumeObCount(int monthlyValumeObCount) {
        this.monthlyValumeObCount = monthlyValumeObCount;
    }

    public HttpServletRequest getHttpServletRequest() {
        return httpServletRequest;
    }

    public void setHttpServletRequest(HttpServletRequest httpServletRequest) {
        this.httpServletRequest = httpServletRequest;
    }

    public String getResultMessage() {
        return resultMessage;
    }

    public void setResultMessage(String resultMessage) {
        this.resultMessage = resultMessage;
    }

    public ArrayList<DashBoardBean> getQ2cDashboardList() {
        return q2cDashboardList;
    }

    public void setQ2cDashboardList(ArrayList<DashBoardBean> q2cDashboardList) {
        this.q2cDashboardList = q2cDashboardList;
    }

    public ArrayList getEdiDocuments204IB() {
        return ediDocuments204IB;
    }

    public void setEdiDocuments204IB(ArrayList ediDocuments204IB) {
        this.ediDocuments204IB = ediDocuments204IB;
    }

    public ArrayList getEdiDocuments204OB() {
        return ediDocuments204OB;
    }

    public void setEdiDocuments204OB(ArrayList ediDocuments204OB) {
        this.ediDocuments204OB = ediDocuments204OB;
    }

    public ArrayList getEdiDocuments990IB() {
        return ediDocuments990IB;
    }

    public void setEdiDocuments990IB(ArrayList ediDocuments990IB) {
        this.ediDocuments990IB = ediDocuments990IB;
    }

    public ArrayList getEdiDocuments990OB() {
        return ediDocuments990OB;
    }

    public void setEdiDocuments990OB(ArrayList ediDocuments990OB) {
        this.ediDocuments990OB = ediDocuments990OB;
    }

    public ArrayList getEdiDocuments214IB() {
        return ediDocuments214IB;
    }

    public void setEdiDocuments214IB(ArrayList ediDocuments214IB) {
        this.ediDocuments214IB = ediDocuments214IB;
    }

    public ArrayList getEdiDocuments214OB() {
        return ediDocuments214OB;
    }

    public void setEdiDocuments214OB(ArrayList ediDocuments214OB) {
        this.ediDocuments214OB = ediDocuments214OB;
    }

    public ArrayList getEdiDocuments210IB() {
        return ediDocuments210IB;
    }

    public void setEdiDocuments210IB(ArrayList ediDocuments210IB) {
        this.ediDocuments210IB = ediDocuments210IB;
    }

    public ArrayList getEdiDocuments210OB() {
        return ediDocuments210OB;
    }

    public void setEdiDocuments210OB(ArrayList ediDocuments210OB) {
        this.ediDocuments210OB = ediDocuments210OB;
    }

    public ArrayList getEdiDocuments850IB() {
        return ediDocuments850IB;
    }

    public void setEdiDocuments850IB(ArrayList ediDocuments850IB) {
        this.ediDocuments850IB = ediDocuments850IB;
    }

    public ArrayList getEdiDocuments850OB() {
        return ediDocuments850OB;
    }

    public void setEdiDocuments850OB(ArrayList ediDocuments850OB) {
        this.ediDocuments850OB = ediDocuments850OB;
    }

    public ArrayList getEdiDocuments810IB() {
        return ediDocuments810IB;
    }

    public void setEdiDocuments810IB(ArrayList ediDocuments810IB) {
        this.ediDocuments810IB = ediDocuments810IB;
    }

    public ArrayList getEdiDocuments810OB() {
        return ediDocuments810OB;
    }

    public void setEdiDocuments810OB(ArrayList ediDocuments810OB) {
        this.ediDocuments810OB = ediDocuments810OB;
    }

    public ArrayList getEdiDocuments856IB() {
        return ediDocuments856IB;
    }

    public void setEdiDocuments856IB(ArrayList ediDocuments856IB) {
        this.ediDocuments856IB = ediDocuments856IB;
    }

    public ArrayList getEdiDocuments856OB() {
        return ediDocuments856OB;
    }

    public void setEdiDocuments856OB(ArrayList ediDocuments856OB) {
        this.ediDocuments856OB = ediDocuments856OB;
    }

    public ArrayList getEdiDocuments855IB() {
        return ediDocuments855IB;
    }

    public void setEdiDocuments855IB(ArrayList ediDocuments855IB) {
        this.ediDocuments855IB = ediDocuments855IB;
    }

    public ArrayList getEdiDocuments855OB() {
        return ediDocuments855OB;
    }

    public void setEdiDocuments855OB(ArrayList ediDocuments855OB) {
        this.ediDocuments855OB = ediDocuments855OB;
    }

    public static Logger getLogger() {
        return logger;
    }

    public static void setLogger(Logger logger) {
        DashBoardAction.logger = logger;
    }

    public String getYearOfMonthlyVolumeXAxisValues() {
        return yearOfMonthlyVolumeXAxisValues;
    }

    public void setYearOfMonthlyVolumeXAxisValues(String yearOfMonthlyVolumeXAxisValues) {
        this.yearOfMonthlyVolumeXAxisValues = yearOfMonthlyVolumeXAxisValues;
    }

    public String getMonthlyOfDaysVolumeXAxisValues() {
        return monthlyOfDaysVolumeXAxisValues;
    }

    public void setMonthlyOfDaysVolumeXAxisValues(String monthlyOfDaysVolumeXAxisValues) {
        this.monthlyOfDaysVolumeXAxisValues = monthlyOfDaysVolumeXAxisValues;
    }

    public ArrayList getTpBusinessYearTransTrends() {
        return tpBusinessYearTransTrends;
    }

    public void setTpBusinessYearTransTrends(ArrayList tpBusinessYearTransTrends) {
        this.tpBusinessYearTransTrends = tpBusinessYearTransTrends;
    }

    public ArrayList getTpBusinessMonthTransTrends() {
        return tpBusinessMonthTransTrends;
    }

    public void setTpBusinessMonthTransTrends(ArrayList tpBusinessMonthTransTrends) {
        this.tpBusinessMonthTransTrends = tpBusinessMonthTransTrends;
    }

    public ArrayList getEdiBusinessYearTransTrends() {
        return ediBusinessYearTransTrends;
    }

    public void setEdiBusinessYearTransTrends(ArrayList ediBusinessYearTransTrends) {
        this.ediBusinessYearTransTrends = ediBusinessYearTransTrends;
    }

    public ArrayList getEdiBusinessMonthTransTrends() {
        return ediBusinessMonthTransTrends;
    }

    public void setEdiBusinessMonthTransTrends(ArrayList ediBusinessMonthTransTrends) {
        this.ediBusinessMonthTransTrends = ediBusinessMonthTransTrends;
    }

    public char getFlag() {
        return flag;
    }

    public void setFlag(char flag) {
        this.flag = flag;
    }

}
