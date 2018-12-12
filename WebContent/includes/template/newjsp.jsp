<%-- 
    Document   : sideMenu
    Created on : May 6, 2016, 8:00:50 PM
    Author     : miracle
--%>

<%@page import="java.util.Map"%>
<%@page import="com.mss.ediscv.util.AppConstants"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <title>Miracle Supply Chain Visibility Portal</title>
        <!-- Tell the browser to be responsive to screen width -->
        <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
        <!-- Bootstrap 3.3.5 -->    
        <script language="JavaScript" src='<s:url value="//cdn.jsdelivr.net/bootstrap.daterangepicker/2/daterangepicker.js"></s:url>'></script>
        <link rel="stylesheet" href='<s:url value="../bootstrap/css/bootstrap.min.css"/>'type="text/css">
        <!-- Font Awesome -->
        <script src="https://maxcdn.bootstrapcdn.com/font-awesome/4.4.0/css/font-awesome.min.css"></script>
        <!-- Ionicons -->
        <script src="https://code.ionicframework.com/ionicons/2.0.1/css/ionicons.min.css"></script>
        <!-- Theme style -->
        <link rel="stylesheet" href='<s:url value="../bootstrap/css/AdminLTE.min.css"/>' type="text/css">
        <!-- AdminLTE Skins. Choose a skin from the css/skins
             folder instead of downloading all of them to reduce the load. -->
        <link rel="stylesheet" href='<s:url value="../bootstrap/css/skins/_all-skins.min.css"/>' type="text/css">
        <!-- Date Picker -->
        <link rel="stylesheet" href='<s:url value="../plugins/datepicker/datepicker3.css"/>' type="text/css">
        <!-- Daterange picker -->
        <link rel="stylesheet" href='<s:url value="../plugins/daterangepicker/daterangepicker-bs3.css"/>' type="text/css">
        <!-- bootstrap wysihtml5 - text editor -->
        <link rel="stylesheet" href='<s:url value="../plugins/bootstrap-wysihtml5/bootstrap3-wysihtml5.min.css"/>' type="text/css">
        <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
        <link rel="stylesheet" href='<s:url value="../bootstrap/css/userdefined.css"/>' type="text/css">

        <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>
        <script language="JavaScript" src='<s:url value="../plugins/datepicker/moment.js"></s:url>'></script>
        <script language="JavaScript" src='<s:url value="../plugins/datepicker/daterangepicker.js"></s:url>'></script>
        <script language="JavaScript" src='<s:url value="../bootstrap/js/userdefined.js"></s:url>'></script>
    </head>
    <body>
        <!-- Left side column. contains the logo and sidebar -->
        <aside class="main-sidebar">
            <!-- sidebar: style can be found in sidebar.less -->
            <section class="sidebar">
                <!-- Sidebar user panel -->
                <div class="user-panel">
                    <div class="pull-left image">
                        <img src="../img/user.png" class="img-circle" alt="User Image">
                    </div>
                    <div class="pull-left info">
                        <div><font color="white">&nbsp;<s:property value="#session.userName" /></font></div>
                        <a>  <s:if test="%{#session.loginId != null && #session.mscvpRole != null}">
                                <font color="white"><s:property value="#session.mscvpRole"/></font>
                            </s:if></a>
                    </div>
                </div>
                <%
                    int userId = (Integer) session.getAttribute(AppConstants.SES_USER_ID);
                    Map usrFlowMap = (Map) session.getAttribute(AppConstants.SES_USER_FLOW_MAP);
                    System.out.println("userId-----&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&" + userId);
                    System.out.println("usrFlowMap-----&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&" + usrFlowMap);
                %>
                <!-- sidebar menu: : style can be found in sidebar.less -->
                <ul class="sidebar-menu">
                    <li class="header">MAIN NAVIGATION</li>
                    <%
                        if (userId != 0) {
                            if (usrFlowMap.containsValue("msscvp")) {
                    %> 
                    <li class="treeview">
              <a href="#">
                <i class="fa fa-user"></i> <span>Admin</span>
                <i class="fa fa-angle-left pull-right"></i>
              </a>
              <ul class="treeview-menu">
                <li><a href="index.html"><i class="fa fa-circle-o"></i> User Creation</a></li>
              </ul>
            </li>
                    <%} else{%>
                    <li class=" treeview">
                        <a href="#">
                            <i class="fa fa-line-chart"></i> <span>Dashboard</span> 
                        </a>
                    </li>
                    <li class=" treeview">
                        <a href="../dashboard/profile.html">
                            <i class="fa fa-user"></i> <span>Profile</span> 
                        </a>
                    </li>
                    <% 
                    if (usrFlowMap.containsValue("Logistics")) {
                    %> 
                    <li class="treeview">
                        <a href="#">
                            <i class="fa fa-truck"></i> <span>Logistics</span>
                            <i class="fa fa-angle-left pull-right"></i>
                        </a>
                        <ul class="treeview-menu">
                            <li><a href="../logistics/doc-repository.html"><i class="fa fa-circle-o"></i> Doc Repository</a></li>
                            <li><a href="../logistics/load-tendering.html"><i class="fa fa-circle-o"></i> Load Tendering</a></li>
                            <li><a href="../logistics/response.html"><i class="fa fa-circle-o"></i> Response</a></li>
                            <li>
                                <a href="#"><i class="fa fa-circle-o"></i> Supply Chain <i class="fa fa-angle-left pull-right"></i></a>
                                <ul class="treeview-menu">
                                    <li><a href="../logistics/shipment.html"><i class="fa fa-circle-o"></i> Shipment</a></li>
                                </ul>
                            </li>
                            <li>
                                <a href="#"><i class="fa fa-circle-o"></i> Finance <i class="fa fa-angle-left pull-right"></i></a>
                                <ul class="treeview-menu">
                                    <li><a href="../logistics/invoice.html"><i class="fa fa-circle-o"></i> Invoice</a></li>
                                </ul>
                            </li>
                            <li>
                                <a href="#"><i class="fa fa-circle-o"></i> Config<i class="fa fa-angle-left pull-right"></i></a>
                                <ul class="treeview-menu">
                                    <li><a href="../logistics/partner-search.html"><i class="fa fa-circle-o"></i> Partner</a></li>
                                    <li><a href="../logistics/routing-search.html"><i class="fa fa-circle-o"></i> Routing</a></li>
                                    <li><a href="../logistics/b2b-channel-search.html"><i class="fa fa-circle-o"></i>B2B Channel</a></li>
                                    <li><a href="../logistics/delivery-channel.html"><i class="fa fa-circle-o"></i>Delivery Channel</a></li>
                                    <li><a href="../logistics/schedular.html"><i class="fa fa-circle-o"></i>Scheduler</a></li>
                                </ul>
                            </li>
                        </ul>
                    </li>
                    <%                            }
                        if (usrFlowMap.containsValue("Manufacturing")) {
                    %> 
                    <li class="active treeview">
                        <a href="#">
                            <i class="fa fa-wrench"></i> <span>Manufacturing</span>
                            <i class="fa fa-angle-left pull-right"></i>
                        </a>
                        <ul class="treeview-menu">
                            <li class="active"><a class="active" href="#"><i class="fa fa-circle-o text-red"></i> Document Repository</a></li>

                            <li>
                                <a href="#"><i class="fa fa-circle-o"></i> Order Management <i class="fa fa-angle-left pull-right"></i></a>
                                <ul class="treeview-menu">
                                    <li><a href="purchase-management.html"><i class="fa fa-circle-o"></i> Purchase Order</a></li>
                                </ul>
                            </li>
                            <li>
                                <a href="#"><i class="fa fa-circle-o"></i> Supply Chain <i class="fa fa-angle-left pull-right"></i></a>
                                <ul class="treeview-menu">
                                    <li><a href="shipments.html"><i class="fa fa-circle-o"></i> Shipments </a></li>
                                </ul>
                            </li>
                            <li>
                                <a href="#"><i class="fa fa-circle-o"></i> Financials <i class="fa fa-angle-left pull-right"></i></a>
                                <ul class="treeview-menu">
                                    <li><a href="financials-invoices.html"><i class="fa fa-circle-o"></i>Invoices</a></li>
                                    <li><a href="financials-payments.html"><i class="fa fa-circle-o"></i>Payments</a></li>
                                </ul>
                            </li>
                            <li>
                                <a href="#"><i class="fa fa-circle-o"></i> Reports <i class="fa fa-angle-left pull-right"></i></a>
                                <ul class="treeview-menu">
                                    <li><a href="excel-reports.html"><i class="fa fa-circle-o"></i>Excel Reports</a></li>
                                    <li><a href="edi-tracking-in-out.html"><i class="fa fa-circle-o"></i>EDI Tracking IN/OUT</a></li>
                                    <li><a href="edi-tracking-summary.html"><i class="fa fa-circle-o"></i>EDI Tracking Summary</a></li>
                                    <li><a href="edi-tracking-inquiry.html"><i class="fa fa-circle-o"></i>EDI Tracking Enquiry</a></li>
                                    <li><a href="dashboard.html"><i class="fa fa-circle-o"></i>Dashboard</a></li>
                                </ul>
                            </li>
                            <li>
                                <a href="#"><i class="fa fa-circle-o"></i> Config<i class="fa fa-angle-left pull-right"></i></a>
                                <ul class="treeview-menu">
                                    <li><a href="partner-search.html"><i class="fa fa-circle-o"></i>Partner</a></li>
                                    <li><a href="routing-search.html"><i class="fa fa-circle-o"></i>Routing</a></li>
                                    <li><a href="b2b-channel-search.html"><i class="fa fa-circle-o"></i>B2B Channel</a></li>
                                    <li><a href="delivery-channel.html"><i class="fa fa-circle-o"></i>Delivery Channel</a></li>
                                    <li><a href="schedular.html"><i class="fa fa-circle-o"></i>Scheduler</a></li>
                                </ul>
                            </li>
                        </ul>
                    </li>
                    <%                            }
                        if (usrFlowMap.containsValue("DocumentVisibility")) {
                    %>
                    <li class="treeview">
                        <a href="#">
                            <i class="fa fa-book"></i> <span>Document Visibility</span>
                            <i class="fa fa-angle-left pull-right"></i>
                        </a>
                        <ul class="treeview-menu">
                            <li><a href="../document-visibility/document-repository.html"><i class="fa fa-circle-o"></i> Doc Repository
                                </a></li>
                        </ul>
                    </li>
                    <%}
                    }
                        }%>
                </ul>
            </section>
            <!-- /.sidebar -->
        </aside>
        <script>
            $('input[name="daterange"]').daterangepicker();
        </script>

        <!-- jQuery 2.1.4 -->
        <script language="JavaScript" src='<s:url value="../plugins/jQuery/jQuery-2.1.4.min.js"></s:url>'></script>
        <!-- jQuery UI 1.11.4 -->
        <script src="https://code.jquery.com/ui/1.11.4/jquery-ui.min.js"></script>
        <!-- Resolve conflict in jQuery UI tooltip with Bootstrap tooltip -->
        <script>
            $.widget.bridge('uibutton', $.ui.button);
        </script>
        <!-- Bootstrap 3.3.5 -->
        <script language="JavaScript" src='<s:url value="../bootstrap/js/bootstrap.min.js"></s:url>'></script>
        <!-- Morris.js charts -->
        <script language="JavaScript" src='<s:url value="https://cdnjs.cloudflare.com/ajax/libs/raphael/2.1.0/raphael-min.js"></s:url>'></script>
        <script language="JavaScript" src='<s:url value="../plugins/morris/morris.min.js"></s:url>'></script>
        <!-- Sparkline -->
        <script language="JavaScript" src='<s:url value="../plugins/sparkline/jquery.sparkline.min.js"></s:url>'></script>
        <!-- jvectormap -->
        <script language="JavaScript" src='<s:url value="../plugins/jvectormap/jquery-jvectormap-1.2.2.min.js"></s:url>'></script>
        <script language="JavaScript" src='<s:url value="../plugins/jvectormap/jquery-jvectormap-world-mill-en.js"></s:url>'></script>
        <!-- jQuery Knob Chart -->
        <script language="JavaScript" src='<s:url value="../plugins/knob/jquery.knob.js"></s:url>'></script>
        <!-- daterangepicker -->
        <script src="https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.10.2/moment.min.js"></script>
        <script language="JavaScript" src='<s:url value="../plugins/daterangepicker/daterangepicker.js"></s:url>'></script>
        <!-- datepicker -->
        <script language="JavaScript" src='<s:url value="../plugins/datepicker/bootstrap-datepicker.js"></s:url>'></script>
        <!-- Bootstrap WYSIHTML5 -->
        <script language="JavaScript" src='<s:url value="../plugins/bootstrap-wysihtml5/bootstrap3-wysihtml5.all.min.js"></s:url>'></script>
        <!-- Slimscroll -->
        <script language="JavaScript" src='<s:url value="../plugins/slimScroll/jquery.slimscroll.min.js"></s:url>'></script>
        <!-- FastClick -->
        <script language="JavaScript" src='<s:url value="../plugins/fastclick/fastclick.min.js"></s:url>'></script>
        <!-- AdminLTE App -->
        <script language="JavaScript" src='<s:url value="../bootstrap/js/app.min.js"></s:url>'></script>
        <!-- AdminLTE dashboard demo (This is only for demo purposes) -->
        <script language="JavaScript" src='<s:url value="../bootstrap/js/pages/dashboard.js"></s:url>'></script>
        <!-- AdminLTE for demo purposes -->
        <script language="JavaScript" src='<s:url value="../bootstrap/js/demo.js"></s:url>'></script>
    </body>
</html>

