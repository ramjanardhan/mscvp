<%@page import="com.mss.ediscv.general.UserInfoBean"%>
<%@page import="java.util.Map"%>
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page buffer="50kb" autoFlush="true" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@page import="com.mss.ediscv.doc.DocRepositoryBean"%>
<%@ taglib uri="/WEB-INF/tlds/dbgrid.tld" prefix="grd"%>
<%@ page import="com.freeware.gridtag.*"%>
<%@page import="java.sql.Connection"%>
<%@  page import="com.mss.ediscv.util.AppConstants"%>
<%@ page import="com.mss.ediscv.util.ConnectionProvider"%>
<%@ page import="java.sql.SQLException"%>
<%@ page import = "java.util.ResourceBundle" %>
<!DOCTYPE html>
<meta charset="utf-8">
<title>Miracle Supply Chain Visibility Portal</title>
<!-- Tell the browser to be responsive to screen width -->
<meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
<!-- Bootstrap 3.3.5 -->
<script type="text/javascript" src="//cdn.jsdelivr.net/bootstrap.daterangepicker/2/daterangepicker.js"></script>
<link rel="stylesheet" href='<s:url value="/includes/bootstrap/css/bootstrap.min.css"/>'>
<!-- Font Awesome -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.4.0/css/font-awesome.min.css">
<!-- Ionicons -->
<link rel="stylesheet" href="https://code.ionicframework.com/ionicons/2.0.1/css/ionicons.min.css">
<!-- Theme style -->
<link rel="stylesheet" href='<s:url value="/includes/bootstrap/css/AdminLTE.min.css"/>'>
<!-- AdminLTE Skins. Choose a skin from the css/skins
     folder instead of downloading all of them to reduce the load. -->
<link rel="stylesheet" href='<s:url value="/includes/bootstrap/css/skins/_all-skins.min.css"/>'>
<!-- iCheck -->
<link rel="stylesheet" href='<s:url value="/includes/plugins/iCheck/flat/blue.css"/>'>
<!-- Morris chart -->
<link rel="stylesheet" href='<s:url value="/includes/plugins/morris/morris.css"/>'>
<!-- jvectormap -->
<link rel="stylesheet" href='<s:url value="/includes/plugins/jvectormap/jquery-jvectormap-1.2.2.css"/>'>
<!-- Date Picker -->
<link rel="stylesheet" href='<s:url value="/includes/plugins/datepicker/datepicker3.css"/>'>
<!-- Daterange picker -->
<link rel="stylesheet" href='<s:url value="/includes/plugins/daterangepicker/daterangepicker-bs3.css"/>'>
<!-- bootstrap wysihtml5 - text editor -->
<link rel="stylesheet" href='<s:url value="/includes/plugins/bootstrap-wysihtml5/bootstrap3-wysihtml5.min.css"/>'>
<!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
<!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>
<script type="text/javascript" src='<s:url value="/includes/plugins/datepicker/moment.js"/>'></script>
<script type="text/javascript" src='<s:url value="/includes/plugins/datepicker/daterangepicker.js"/>'></script>
<link rel="stylesheet" href='<s:url value="/includes/bootstrap/css/userdefined.css"/>'>
<script type="text/javascript" src='<s:url value="/includes/bootstrap/js/userdefined.js"/>'></script>

<%-- <body class="hold-transition skin-blue sidebar-mini">--%>
<%-- <s:include value="/includes/template/header.jsp"/> --%>   

<div class="wrapper">


    <!-- Left side column. contains the logo and sidebar -->
    <aside class="main-sidebar" style="padding-top: 0%;z-index: 1000;">
        <header class="main-header">
            <a href="index2.html" class="logo">
                <!-- mini logo for sidebar mini 50x50 pixels -->
                <span class="logo-mini"><b>SCV</b></span>
                <!-- logo for regular state and mobile devices -->
                <span class="logo-lg"><strong>MIRACLE</strong> SCVP</span>
            </a>
        </header> 
        <!-- sidebar: style can be found in sidebar.less -->
        <section class="sidebar">
            <!-- Sidebar user panel -->
            <div class="user-panel">
                <div class="pull-left image">
                    <s:url id="lImage" action="renderImage" namespace="/user"> </s:url>
                    <img src="<s:property value="#lImage" />" class="img-circle" alt="User Image"/>
                </div>
                <div class="pull-left info">
                    <div><font color="white"><s:property value="#session.userName" /></font></div>
                    <a><s:if test="%{#session.loginId != null && #session.mscvpRole != null}">
                            <font color="white"><s:property value="#session.mscvpRole"/></font>
                        </s:if></a>
                </div>
            </div>
            <%
                int userId = (Integer) session.getAttribute(AppConstants.SES_USER_ID);
                Map usrFlowMap = (Map) session.getAttribute(AppConstants.SES_USER_FLOW_MAP);
                System.out.println("usrFlowMap:"+usrFlowMap);
            %>
            <!-- sidebar menu: : style can be found in sidebar.less -->
            <ul class="sidebar-menu">

                <li class=" treeview" id="profile">
                    <a href="<s:url action="../user/getProfile.action"/>"> <i class="fa fa-user"></i> <span>Profile</span> </a>
                </li>
                <% if (userId != 0) {
                        if (usrFlowMap.containsValue("msscvpAdmin")) {%> 
                <li class="treeview" id="admintree">
                    <a href="#">
                        <i class="fa fa-user"></i> <span>Admin</span>
                        <i class="fa fa-angle-left pull-right"></i>
                    </a>
                    <ul class="treeview-menu">
                        <li id="createusr"><a href="<s:url action="../user/userAction.action"/>"><i class="fa fa-circle-o"></i> User Creation</a></li>
                        <li id="searchusr"><a href="<s:url action="../user/doSearchUser.action"/>"><i class="fa fa-circle-o"></i> User Search</a></li>
                        <li id="resetUsrPwd"><a href="<s:url action="../user/resetUserPwd.action"/>"><i class="fa fa-circle-o"></i> Reset User Password</a></li>
                    </ul>
                </li>
                <%} else {
                    if ((usrFlowMap.containsValue("Logistics")) || (usrFlowMap.containsValue("Manufacturing"))) {%>
                 <li class=" treeview" id="dashboardLM">
                    <a href="#">
                        <i class="fa fa-line-chart"></i> <span>Dashboards</span> 
                        <i class="fa fa-angle-left pull-right"></i>
                    </a>
                    <ul class="treeview-menu">
                        <%     if (usrFlowMap.containsValue("Manufacturing")) {%> 
                        
                        <li id="dashboardM"><a href="#"><i class="fa fa-circle-o"></i>Manufacturing Dashboards<i class="fa fa-angle-left pull-right"></i></a>
                             <ul class="treeview-menu">
                                 <li id="dashboard"><a href="<s:url action="../reports/dashboard.action"/>"><i class="fa fa-circle-o"></i>Manufacturing Dashboard</a></li>
                                <li id="MdailyStatsView"><a href="<s:url action="../dashboard/getDailyStatsView.action"><s:param name='flag'>M</s:param></s:url>"><i class="fa fa-circle-o"></i>Daily Stats View</a></li> 
<%--                               <li id="MBusinessFlow"><a href="<s:url action="../dashboard/getBusinessFlow.action"><s:param name='flag'>M</s:param></s:url>"><i class="fa fa-circle-o"></i>Business Flow View</a></li> 
                                <li id="MEdiDocument"><a href="<s:url action="../dashboard/getEDIDocuments.action"><s:param name='flag'>M</s:param></s:url>"><i class="fa fa-circle-o"></i>Transactional View</a></li> --%>
                            </ul>
                            </li>
                            <% }
  if (usrFlowMap.containsValue("Logistics")) {%>
                        <li id="dashboardL"><a href="#"><i class="fa fa-circle-o"></i>Logistics Dashboards<i class="fa fa-angle-left pull-right"></i></a>
                              <ul class="treeview-menu">
                                  <li id="ltdashboard"><a href="<s:url action="../logisticsReports/dashboard.action"/>"><i class="fa fa-circle-o"></i>Logistics Dashboard</a>
                                  <li id="LdailyStatsView"><a href="<s:url action="../dashboard/getDailyStatsView.action?"><s:param name='flag'>L</s:param></s:url>"><i class="fa fa-circle-o"></i>Daily Stats View</a></li> 
<%--                               <li id="LBusinessFlow"><a href="<s:url action="../dashboard/getBusinessFlow.action"><s:param name='flag'>L</s:param></s:url>"><i class="fa fa-circle-o"></i>Business Flow View</a></li> 
                               <li id="LEdiDocument"><a href="<s:url action="../dashboard/getEDIDocuments.action"><s:param name='flag'>L</s:param></s:url>"><i class="fa fa-circle-o"></i>Transactional View</a></li>--%>
                                
                            </ul>
                         </li>
                        <% }
                             %>
<%--                           <li id="ltBusinessTrends">
                                    <a href="#"><i class="fa fa-circle-o"></i>Business Trend View<i class="fa fa-angle-left pull-right"></i></a>
                                    <ul class="treeview-menu">
                                        <li id="ltBusinessTrendsDocument"><a href="<s:url action="../dashboard/getBusinessTrendsDocument.action"/>"><i class="fa fa-circle-o"></i>Transactional Trend</a></li>
                                        <li id="ltBusinessTrendsTP"><a href="<s:url action="../dashboard/getBusinessTrendsTP.action"/>"><i class="fa fa-circle-o"></i>TP Trend</a></li>
                                    </ul> 
                            </li>--%>
                            
                    </ul>
                </li>
<%--                 <%  if (usrFlowMap.containsValue("DocumentVisibility")) {%>
                 <li class=" treeview" id="dvdocrepository">
                    <a href="<s:url action="../documentVisibility/DocumentVisibility.action"/>"> <i class="fa fa-user"></i> <span>Document Visibility</span> </a>
                </li>
                <% }%> --%>
                <% if (session.getAttribute(AppConstants.SES_ROLE_ID).equals("100") || session.getAttribute(AppConstants.SES_ROLE_ID).equals("101") || session.getAttribute(AppConstants.SES_ROLE_ID).equals("104") || session.getAttribute(AppConstants.SES_ROLE_ID).equals("105") || session.getAttribute(AppConstants.SES_ROLE_ID).equals("103") || session.getAttribute(AppConstants.SES_ROLE_ID).equals("102")) {%>
                <li class=" treeview" id="purging">
                    <a href="#">
                        <i class="fa fa-remove"></i> <span>Archiving/Purging</span> 
                        <i class="fa fa-angle-left pull-right"></i>
                    </a>
                    <%if (session.getAttribute(AppConstants.SES_ROLE_ID).equals("100")) {%>
                    <ul class="treeview-menu">
                        <li id="archiveProcess" class=" treeview"><a href="<s:url action="../purge/arcPro.action"/>"><i class="fa fa-circle-o"></i>Archive Process</a></li>
                        <li id="archiveHistory" class=" treeview"><a href="<s:url action="../purge/arcHis.action"/>"><i class="fa fa-circle-o"></i>Archive History</a></li>
                        <li id="purgeProcess" class=" treeview"><a href="<s:url action="../purge/purPro.action"/>"><i class="fa fa-circle-o"></i>Purge Process</a></li>                        
                        <li id="purgeHisory" class=" treeview"><a href="<s:url action="../purge/purHis.action"/>"><i class="fa fa-circle-o"></i>Purge History</a></li>
                    </ul>
                    <%}%>
                    <% if (session.getAttribute(AppConstants.SES_ROLE_ID).equals("101") || session.getAttribute(AppConstants.SES_ROLE_ID).equals("104") || session.getAttribute(AppConstants.SES_ROLE_ID).equals("105") || session.getAttribute(AppConstants.SES_ROLE_ID).equals("103") || session.getAttribute(AppConstants.SES_ROLE_ID).equals("102")) {%>
                    <ul class="treeview-menu">
                        <li id="archiveHistory" class=" treeview"><a href="<s:url action="../purge/arcHis.action"/>"><i class="fa fa-circle-o"></i>Archive History</a></li>
                        <li id="purgeHisory" class=" treeview"><a href="<s:url action="../purge/purHis.action"/>"><i class="fa fa-circle-o"></i>Purge History</a></li>
                    </ul>
                    <%}%>
                </li>


                <% }
                    }%>
                <%
                    
                    if (usrFlowMap.containsValue("Manufacturing")) {%>
                <li class="treeview" id="manufacturing">
                    <a href="#">
                        <i class="fa fa-wrench"></i> <span>Manufacturing</span>
                        <i class="fa fa-angle-left pull-right"></i>
                    </a>
                    <ul class="treeview-menu">
                        <% if (session.getAttribute(AppConstants.SES_ROLE_ID).equals("102") || session.getAttribute(AppConstants.SES_ROLE_ID).equals("100") || session.getAttribute(AppConstants.SES_ROLE_ID).equals("101") || session.getAttribute(AppConstants.SES_ROLE_ID).equals("104") || session.getAttribute(AppConstants.SES_ROLE_ID).equals("105") || session.getAttribute(AppConstants.SES_ROLE_ID).equals("103")) {%>
                        <li id="docrepository"><a href="<s:url action="../doc/orderToCash"/>"><i class="fa fa-circle-o"></i> Document Repository</a></li>

                        <li id="oredermanagement">
                            <a href="#"><i class="fa fa-circle-o"></i> Order Management <i class="fa fa-angle-left pull-right"></i></a>
                            <ul class="treeview-menu">
                                <li id="purchaseorder"><a href="<s:url action="../po/purchaseOrder"/>"><i class="fa fa-circle-o"></i> Purchase Order</a></li>
                            </ul>
                        </li>
                        <li id="supplychain">
                            <a href="#"><i class="fa fa-circle-o"></i> Supply Chain <i class="fa fa-angle-left pull-right"></i></a>
                            <ul class="treeview-menu">
                                <li id="shipments"><a href="<s:url action="../shipment/shipmentAction"/>"><i class="fa fa-circle-o"></i> Shipments </a></li>
                            </ul>
                        </li>
                        <li id="financials">
                            <a href="#"><i class="fa fa-circle-o"></i> Financials <i class="fa fa-angle-left pull-right"></i></a>
                            <ul class="treeview-menu">
                                <li id="invoices"><a href="<s:url action="../inv/invoiceAction"/>"><i class="fa fa-circle-o"></i>Invoices</a></li>
                                <li id="payments"><a href="<s:url action="../payment/paymentAction"/>"><i class="fa fa-circle-o"></i>Payments</a></li>
                            </ul>
                        </li>
                        <li id="inventory"><a href="<s:url action="../inventory/inventoryAction.action"/>"><i class="fa fa-circle-o"></i> Inventory</a></li>
                        <li id="reports">
                            <a href="#"><i class="fa fa-circle-o"></i>Manufacturing Reports <i class="fa fa-angle-left pull-right"></i></a>
                            <ul class="treeview-menu">
                                <li id="excelreports"><a href="<s:url action="../reports/getReports"/>"><i class="fa fa-circle-o"></i>Excel Reports</a></li>
                                <li id="editrackinginout"><a href="<s:url action="../reports/getTrackInOut"/>"><i class="fa fa-circle-o"></i>EDI Tracking IN/OUT</a></li>
                                <li id="editrackingsummary"><a href="<s:url action="../reports/getTrackSummary"/>"><i class="fa fa-circle-o"></i>EDI Tracking Summary</a></li>
                                <li id="editrackingenquiry"><a href="<s:url action="../reports/getTrackInquiry"/>"><i class="fa fa-circle-o"></i>EDI Tracking Inquiry</a></li>
                                    <%--  <li id="dashboard"><a href="<s:url action="../reports/dashboard.action"/>"><i class="fa fa-circle-o"></i>Dashboard</a></li>  --%>
                            </ul>
                        </li>
                        <li id="config">
                            <a href="#"><i class="fa fa-circle-o"></i> Config<i class="fa fa-angle-left pull-right"></i></a>
                            <ul class="treeview-menu">
                                <li id="partner"><a href="<s:url action="../partner/getPartnerList.action"><s:param name="configFlowFlag" value="'manufacturing'"/></s:url>"><i class="fa fa-circle-o"></i>Partner</a></li>
                                    <%--<li id="routing"><a href="<s:url action="../routing/getRoutingList.action"><s:param name="configFlowFlag" value="'manufacturing'"/></s:url>"><i class="fa fa-circle-o"></i>Routing</a></li>
                                    <li id="b2bchannel"><a href="<s:url action="../b2bchannel/getB2BChannelList.action"><s:param name="configFlowFlag" value="'manufacturing'"/></s:url>"><i class="fa fa-circle-o"></i>B2B Channel</a></li>
                                    <li id="deliverychannel"><a href="<s:url action="../partner/deliveryChannelList.action"><s:param name="configFlowFlag" value="'manufacturing'"/></s:url>"><i class="fa fa-circle-o"></i>Delivery Channel</a></li>--%>
                                <li id="scheduler"><a href="<s:url action="../partner/getSchedular.action"> <s:param name="configFlowFlag" value="'manufacturing'"/></s:url>"><i class="fa fa-circle-o"></i>Scheduler</a></li>
                                </ul>
                            </li>
                        </ul>
                    </li>
<%--                    <li class="treeview" id="simonscreen">
                        <a href="#">
                            <i class="fa fa-gavel"></i> <span>V3.0</span>
                            <i class="fa fa-angle-left pull-right"></i>
                        </a>         

                        <ul class="treeview-menu">
                            <li id="transactionInformation"><a href="<s:url action="../simon/transactionInformation.action"/>"><i class="fa fa-circle-o"></i> Transaction Information</a></li>
                       <li id="transactionSearch"><a href="<s:url action="../simon/transactionSearch.action"/>"><i class="fa fa-circle-o"></i> Transaction Search</a></li>
                        <li id="dvdocrepository"><a href="<s:url action="../documentVisibility/DocumentVisibility.action"/>"><i class="fa fa-circle-o"></i> Doc Repository</a></li> 
                        <li id="partnerList"><a href="<s:url action="../simon/partnerList.action"/>"><i class="fa fa-circle-o"></i> Partner List</a></li>
                        <li id="deliveryChannelList"><a href="<s:url action="../simon/deliveryChannelList.action"><s:param name='flag'>0</s:param></s:url>"><i class="fa fa-circle-o"></i> Delivery Channel List  </a></li>
                    </ul>
                </li>--%>
                <% }
                    }
                    if (usrFlowMap.containsValue("Logistics")) {
                %> 
                <li class="treeview" id="logistics">
                    <a href="#">
                        <i class="fa fa-truck"></i> <span>Logistics</span>
                        <i class="fa fa-angle-left pull-right"></i>
                    </a>
                    <ul class="treeview-menu" >
                        <%
                       if (session.getAttribute(AppConstants.SES_ROLE_ID).equals("100") || session.getAttribute(AppConstants.SES_ROLE_ID).equals("102") || session.getAttribute(AppConstants.SES_ROLE_ID).equals("141") || session.getAttribute(AppConstants.SES_ROLE_ID).equals("142") || session.getAttribute(AppConstants.SES_ROLE_ID).equals("103")) {%>
                        <li id="ltdocrepository"><a href="<s:url action="../logisticsdoc/Logistics.action"/>"><i class="fa fa-circle-o"></i> Doc Repository</a></li>
                        <li id="ltloadtendering">
                            <a href="<s:url action="../logisticsloadtendering/loadtendering.action"/>"><i class="fa fa-circle-o"></i> Load Tendering</a></li>

                        <li id="ltresponse"><a href="<s:url action="../ltResponse/ltResponse.action"/>"><i class="fa fa-circle-o"></i> Response</a></li>

                        <li id="ltsupplychain">
                            <a href="#"><i class="fa fa-circle-o"></i> Supply Chain <i class="fa fa-angle-left pull-right"></i></a>
                            <ul class="treeview-menu">
                                <li id="ltshipment"><a href="<s:url action="../logisticsshipment/ltshipment.action"/>"><i class="fa fa-circle-o"></i> Shipment</a></li>
                            </ul>
                        </li>

                        <li id="ltfinance"> 
                            <a href="#"><i class="fa fa-circle-o"></i> Finance <i class="fa fa-angle-left pull-right"></i></a>
                            <ul class="treeview-menu">
                                <li id="ltinvoice"><a href="<s:url action="../logisticsinvoice/ltinvoice.action"/>"><i class="fa fa-circle-o"></i> Invoice</a></li>
                            </ul>
                        </li>

                        <li id="ltreports">
                            <a href="#"><i class="fa fa-circle-o"></i> Logistic&nbsp;Reports <i class="fa fa-angle-left pull-right"></i></a>
                            <ul class="treeview-menu">
                                <li id="ltexcelreports"><a href="<s:url action="../logisticsReports/getLogisticReports"/>"><i class="fa fa-circle-o"></i> Excel&nbsp;Reports </a></li>
                                    <%--    <li id="ltdashboard"><a href="<s:url action="../logisticsReports/dashboard.action"/>"><i class="fa fa-circle-o"></i> Dashboard</a></li> --%>
                            </ul>
                        </li>

                        <li id="ltconfig">
                            <a href="#"><i class="fa fa-circle-o"></i> Config<i class="fa fa-angle-left pull-right"></i></a>
                            <ul class="treeview-menu">
                                <li id="ltpartner"><a href="<s:url action="../partner/getPartnerList.action"> <s:param name="configFlowFlag" value="'logistics'"/></s:url>"><i class="fa fa-circle-o"></i> Partner</a></li>
                                    <%--<li id="ltrouting"><a href="<s:url action="../routing/getRoutingList.action"> <s:param name="configFlowFlag" value="'logistics'"/></s:url>"><i class="fa fa-circle-o"></i> Routing</a></li>
                                    <li id="ltb2bchannel"><a href="<s:url action="../b2bchannel/getB2BChannelList.action"> <s:param name="configFlowFlag" value="'logistics'"/></s:url>"><i class="fa fa-circle-o"></i>B2B Channel</a></li>
                                    <li id="ltdeliverychannel"><a href="<s:url action="../partner/deliveryChannelList.action"> <s:param name="configFlowFlag" value="'logistics'"/></s:url>"><i class="fa fa-circle-o"></i>Delivery Channel</a></li>--%>
                                <li id="ltscheduler"><a href="<s:url action="../partner/getSchedular.action"> <s:param name="configFlowFlag" value="'logistics'"/></s:url>"><i class="fa fa-circle-o"></i>Scheduler</a></li>
                                    <%--<li id="purgeProcess"><a href="<s:url action="../purge/purging.action"> <s:param name="configFlowFlag" value="'logistics'"/></s:url>"><i class="fa fa-circle-o"></i>Purge Process</a></li>--%>
                            </ul>
                        </li>
                    </ul>
                </li>

                <%
                        }
                    }
                %>
                <% if (session.getAttribute(AppConstants.SES_ROLE_ID).equals("100") || session.getAttribute(AppConstants.SES_ROLE_ID).equals("101") || session.getAttribute(AppConstants.SES_ROLE_ID).equals("104") || session.getAttribute(AppConstants.SES_ROLE_ID).equals("105") || session.getAttribute(AppConstants.SES_ROLE_ID).equals("103") || session.getAttribute(AppConstants.SES_ROLE_ID).equals("102")) {%>
                <li class="treeview" id="utilities">
                    <a href="#"><i class="fa fa-gavel"></i><span>Utilities</span><i class="fa fa-angle-left pull-right"></i></a>
                    <ul class="treeview-menu">
<%--                        <li id="certMonitoring"><a href="<s:url action="../utilities/certMonitor.action"/>"><i class="fa fa-circle-o"></i> Certificate Monitoring</a></li>
                        <li id="codeList"><a href="<s:url action="../utilities/codeList.action"/>"><i class="fa fa-circle-o"></i> Code List</a></li>
                        <li id="processFlowMenu"><a href="../utilities/getProcessFlows.action"/><i class="fa fa-circle-o"></i> Process Flow</a></li>--%>
                        <li id="appConstants"><a href="../appConfigurations/getAppConfigurations.action"/><i class="fa fa-circle-o"></i> App Configurations</a></li>
                    </ul>
                    </ul>
                </li>
                <%
                            }
                        }
                    }
                %>
            </ul>
        </section>
        <!-- /.sidebar -->
    </aside>
    <!-- Content Wrapper. Contains page content -->
</div>



