<%-- 
    Document   : TrackSummary
    Created on : Aug 10, 2015, 3:08:55 PM
    Author     : miracle
--%>


<%@page import="com.mss.ediscv.schdular.SchdularBean"%>
<%@page import="com.mss.ediscv.reports.ReportsBean"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%-- <!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"> --%>
<!DOCTYPE html>
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page buffer="50kb" autoFlush="true" %>

<%@page import="com.mss.ediscv.doc.DocRepositoryBean"%>

<%@page import="java.sql.Connection"%>
<%@  page import="com.mss.ediscv.util.AppConstants"%>


<html class=" js canvas canvastext geolocation crosswindowmessaging no-websqldatabase indexeddb hashchange historymanagement draganddrop websockets rgba hsla multiplebgs backgroundsize borderimage borderradius boxshadow opacity cssanimations csscolumns cssgradients no-cssreflections csstransforms no-csstransforms3d csstransitions  video audio localstorage sessionstorage webworkers applicationcache svg smil svgclippaths   fontface">
    <head>

        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
        <meta http-equiv="pragma" content="no-cache" />
        <meta http-equiv="cache-control" content="no-cache" />
        <title>Miracle Supply Chain Visibility Portal</title>
        <!-- Tell the browser to be responsive to screen width -->
        <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
        <link rel="stylesheet" href='<s:url value="/includes/bootstrap/css/userdefined.css"/>'>
        <link rel="stylesheet" href='<s:url value="/includes/plugins/datatables/dataTables.bootstrap.css"/>' type="text/css">
        <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>

        <style>
            div#overlay {
                display: none;
                z-index: 2;
                background: #000;
                position: fixed;
                width: 100%;
                height: 100%;
                top: 0px;
                left: 0px;
                text-align: center;
            }
            div#specialBox {
                display: none;
                position: absolute;
                z-index: 3;
                margin: 10px auto 0px auto;
                width: 403px; 
                height: 200;
                background: #FFF;
                left: 38%;

                color: #000;
            }

        </style>

        <script type="text/javascript">
            function goToSchdularReport()
            {
                var configFlowFlag = document.getElementById("configFlowFlag").value;
                //                alert(configFlowFlag);
                window.location = "../partner/getSchedularAdd.action?configFlowFlag=" + configFlowFlag;
            }
            function getReportDelete(id) {
                getDeleteReport(id);
            }
            function doOnLoad() {
                var configFlowFlag = $('#configFlowFlag').val();
                if (configFlowFlag == 'manufacturing') {
                    $("#manufacturing").addClass("active");
                    $("#scheduler").addClass("active");
                    $("#config").addClass("active");
                    $("#scheduler i").addClass("text-red");
                } else if (configFlowFlag == 'logistics') {
                    $("#logistics").addClass("active");
                    $("#ltscheduler").addClass("active");
                    $("#ltconfig").addClass("active");
                    $("#ltscheduler i").addClass("text-red");
                }

                document.getElementById('loadingAcoountSearch').style.display = "none";
            }
            $(function () {
                // $("#example1").DataTable();
                $('#results').DataTable({
                    "paging": true,
                    "lengthChange": true,
                    "searching": true,
                    "ordering": true,
                    "info": true,
                    "autoWidth": false,
                    "scrollY": 300,
                    "scrollX": true,
                });
            });
        </script>
    </head>
    <%--<body onload="doOnLoad();initDateTime('docdatepickerfrom','docdatepicker','<%=check %>');setStyle('docRep','');">  --%>
    <body onload="doOnLoad()"  class="hold-transition skin-blue sidebar-mini">
        <script type="text/javascript" src='<s:url value="/includes/js/wz_tooltip.js"/>'></script>
    <header>  
        <div id="wrapper">
            <div id="main">
                <header>
                    <div>
                        <s:include value="/includes/template/header.jsp"/>       

                    </div>
                </header>
                <div>
                    <s:include value="/includes/template/sidemenu.jsp"/>
                </div>	
            </div>
    </header>
    <!-- Content Wrapper. Contains page content -->
    <div class="content-wrapper">
        <div id="erroroverlay"></div>
        <!-- End Overlay --> 
        <section class="content-header">
            <s:if test="%{configFlowFlag == 'manufacturing'}">
                <h1>Scheduler Task
                    <!--                    <small> Manufacturing</small>-->
                </h1>
                <!--                <ol class="breadcrumb">
                                    <li><a href="#"><i class="fa fa-wrench"></i>Manufacturing</a></li>
                                    <li class="active">  Scheduler Task</li>
                                </ol>-->
            </s:if><s:elseif test="%{configFlowFlag == 'logistics'}">
                <h1>Scheduler Task
                    <!--                    <small> Logistics</small>-->
                </h1>
                <!--                <ol class="breadcrumb">
                                    <li><a href="#"><i class="fa fa-truck"></i>Logistics</a></li>
                                    <li class="active">  Scheduler Task</li>
                                </ol>-->
            </s:elseif>
        </section>  

        <section class="content">
            <div class="row">
                <div class="col-sm-12"> 
                    <!--box-->
                    <%String contextPath = request.getContextPath();%>
                    <div class="box box-primary">
                        <!--                        <div class="box-header with-border">
                                                    <h3 class="box-title">Scheduler Task</h3>
                                                    <div class="box-tools pull-right">
                        
                                                    </div>
                                                </div> /.box-header -->
                        <div class="box-body">
                            <div id="text">

                                <!-- /.col (right) -->
                                <!-- First Row -->
                                <div id="overlay"></div>              
                                <div id="specialBox">
                                    <s:form theme="simple"  align="center" name="ProjectResources" action="%{currentAction}" method="post" enctype="multipart/form-data" onsubmit="return validateForm();"   >
                                        <table align="center" border="0" cellspacing="0" style="width:100%;">
                                            <tr style="background-color: #8ce2f7;">                               
                                                <td colspan="3" style="background-color: #8ce2f7">
                                                    <h3 style="color:darkblue;" align="left">
                                                        <span id="headerLabel"></span>

                                                        <td colspan="3" style="background-color: #8ce2f7;position: absolute; right: 0px;" align="right">
                                                            <a style="color: deepskyblue" href="#" onclick="BDMOverlay('0')" >
                                                                <img src="../includes/images/dtp/close.gif" /> 
                                                            </a>  
                                                        </td>
                                                    </h3>
                                                </td>
                                            </tr>
                                            <tr>

                                                <td class="lableLeft">Date From </td>
                                                <td><%-- <input type="text" id="datepickerfrom" /> --%>
                                                    <%--  <input type="text" name="datepickerfrom" id="datepickerfrom" class="inputStyle" tabindex="2" /> --%>
                                                    <s:textfield cssClass="inputStyle" name="schStartdate" id="schStartdate"  value="%{schStartdate}" tabindex="1"  onclick="CalenderOnChange(this);" onblur="return enterSchedulerDates();"/>
                                                    <s:hidden name="scheduleid" id="scheduleid" value="2"/>
                                                    <a style="color: deepskyblue" href="javascript:copyValuTo('schStartdate');"></a>
                                                </td>
                                                <td>
                                                    <label id="downloadLink"> 

                                                        <s:a style="color: deepskyblue" href="javascript:DownloadSchedulerReport()" onmouseover="Tip('Click here to download Report.')" onmouseout="UnTip()">
                                                            <%

                                                                out.println("download");
                                                            %> 
                                                        </s:a>
                                                    </label>
                                                    <label id="downloadMessage"></label>
                                                </td>

                                            </tr>
                                        </table>
                                    </s:form> 
                                </div>

                                <div class="row">
                                    <div class="col-sm-12">

                                        <s:form action="../partner/getSchedularsearch.action" method="post" name="reportsattachForm" id="reportsattachForm" theme="simple">
                                            <s:hidden name="configFlowFlag" value="%{configFlowFlag}" id="configFlowFlag"/>
                                            <div class="form-group">
                                                <div class="row">

                                                    <div  class="col-sm-4">
                                                        <label for="Status">Status</label> 

                                                        <s:select headerKey="-1" cssClass="form-control" headerValue="Select Type" list="{'Active','InActive'}" name="status" id="status" value="%{status}" tabindex="1" />
                                                    </div>

                                                    <div id="loadingAcoountSearch" class="loadingImg">
                                                        <span id ="LoadingContent" > <img src="<s:url value="/includes/images/Loader2.gif"/>"   ></span>
                                                    </div>

                                                </div>
                                            </div>


                                            <div class="row">
                                                <div class="col-sm-2"><s:submit value="Search"   cssClass="btn btn-primary col-sm-12" tabindex="2"/></div>
                                                <div class="col-sm-2">   <strong><input type="button" value="Add" class="btn btn-primary col-sm-12"  onclick="goToSchdularReport();" tabindex="3"/></strong></div>
                                            </div>
                                            <br>
                                            <s:hidden name="sampleValue" id="sampleValue" value="2"/>

                                        </s:form>
                                    </div></div>
                            </div>


                        </div>
                        <%--  out.print("contextPath-->"+contextPath); --%>
                    </div>
                </div></section>



        <s:if test="#session.schdularList != null"> 
            <%--   <div class="content" id="gridDiv"> --%>
            <section class="content">
                <div class="row">
                    <div class="col-xs-12">
                        <div class="box">
<!--                            <div class="box-header">
                                <h3 class="box-title">Table</h3>
                            </div>-->
                            <div class="box-body">

                                <%!String cssValue = "whiteStripe";
                                    int resultsetTotal;%>


                                <div style="overflow-x:auto;">       
                                    <div id="loadingImage" align="center" style="color: green;display: none;"><img  src="../includes/images/ajax-loader.gif" /></div>
                                    <table align="left" width="100%"
                                           border="0" cellpadding="0" cellspacing="0">
                                        <tr>
                                            <td style="background-color: white;">


                                                <table  id="results" class="table table-bordered table-hover">
                                                    <%
                                                        java.util.List list = (java.util.List) session.getAttribute(AppConstants.SES_SCHDULAR_LIST);

                                                        if (list.size() != 0) {
                                                            SchdularBean schdularBean;
                                                    %>
                                                    <thead> <tr>
                                                            <th>Title</th>                
                                                            <th>Type</th> 
                                                            <th>Scheduler&nbsp;Time</th>
                                                            <th>Status</th>
                                                            <th>Delete</th>
                                                            <th>Download</th>
                                                        </tr>
                                                    </thead> <tbody>  <tr>

                                                            <%
                                                                for (int i = 0; i < list.size(); i++) {
                                                                    schdularBean = (SchdularBean) list.get(i);

                                                                    if (i % 2 == 0) {
                                                                        cssValue = "whiteStripe";
                                                                    } else {
                                                                        cssValue = "grayEditSelection";
                                                                    }
                                                            %>

                                                            <td><%-- <a href="#"> --%>
                                                                <%
                                                                    int id = schdularBean.getId();
                                                                %>
                                                                <s:url var="myUrl" action="../partner/getSchedularEdit.action">
                                                                    <s:param name="id"><%=id%></s:param>                        
                                                                    <s:param name="status" value="%{status}"></s:param>  
                                                                    <s:param name="configFlowFlag" value="%{configFlowFlag}"></s:param>  
                                                                    <s:param name="configFlowFlag1" value="%{configFlowFlag}"></s:param>
                                                                </s:url>

                                                                <s:a style="color: deepskyblue" href='%{#myUrl}' onmouseover="Tip('Click here to Edit Partner.')" onmouseout="UnTip()">
                                                                    <%

                                                                        out.println(schdularBean.getSchtitle());
                                                                    %> 
                                                                </s:a>

                                                                <%--  </a> --%>
                                                            </td>


                                                            <td>

                                                                <%
                                                                    if (schdularBean.getSchtype() != null && !"".equals(schdularBean.getSchtype())) {
                                                                        out.println(schdularBean.getSchtype());
                                                                    } else {
                                                                        out.println("-");
                                                                    }

                                                                %>

                                                            </td>   

                                                            <td>
                                                                <%                                                                    if (schdularBean.getSchhrFormat() != null && !"".equals(schdularBean.getSchhrFormat())) {
                                                                        out.println(schdularBean.getSchhrFormat());
                                                                    } else {
                                                                        out.println("-");
                                                                    }
                                                                %>

                                                            </td>
                                                            <td>
                                                                <%
                                                                    if (schdularBean.getStatus() != null && !"".equals(schdularBean.getStatus())) {
                                                                        out.println(schdularBean.getStatus());
                                                                    } else {
                                                                        out.println("-");
                                                                    }
                                                                %>

                                                            </td> 


                                                            <td>

                                                                <a style="color: red" href="javascript:getReportDelete('<%=schdularBean.getId()%>');">

                                                                    <%
                                                                        out.println("delete");
                                                                    %>
                                                                </a>
                                                            </td>

                                                            <td> <a style="color: deepskyblue" href="javascript:BDMOverlay('<%=schdularBean.getId()%>');">
                                                                    <%
                                                                        out.println("Download");
                                                                    %>
                                                                </a>
                                                            </td>

                                                        </tr>

                                                        <%
                                                            }
                                                        } else {
                                                        %>
                                                        <tr><td>
                                                                <%
                                                                        // String contextPath = request.getContextPath();
                                                                        // out.println("<img  border='0' align='top'  src='"+contextPath+"/includes/images/alert.gif'/><b> No Records Found to Display!</b>");
                                                                        out.println("<img  border='0' align='top'  src='" + contextPath + "/includes/images/alert.gif'/><b>No records found for the given search criteria. Please try a different search criteria!</b>");
                                                                    }

                                                                %>

                                                            </td>
                                                        </tr>
                                                </table>

                                            </td>
                                        </tr>
                                        <%                                            if (list.size() != 0) {
                                        %>
                                        <tr >
                                            <!--                                        <td align="right" colspan="28" style="background-color: white;">
                                            
                                                                                        <div id="load" style="color: green;display: none;">Loading..</div>
                                                                                        <div id="resultMessage"></div>
                                                                                        <div align="right" id="pageNavPosition">hello</div>
                                                                                    </td>-->
                                        </tr> 
                                        <% }%></tbody>
                                    </table>

                                </div>
                            </div></div></div>
            </section>
            <%--   </div> --%>
        </s:if>     



    </div>

</div>
</div> 
<div>
    <s:include value="../includes/template/footer.jsp"/>
</div>
<script language="JavaScript"  src='<s:url value="/includes/js/GeneralAjax.js"/>'></script>
<script language="JavaScript"  src='<s:url value="/includes/js/common.js"/>'></script>
<script language="JavaScript"  src='<s:url value="/includes/js/downloadAjax.js"/>'></script>
<script language="JavaScript"  src='<s:url value="/includes/js/overlay.js"/>'></script>
<script src='<s:url value="../includes/bootstrap/js/app.min.js"/>'></script>
<script src='<s:url value="../includes/plugins/datatables/jquery.dataTables.min.js"/>'></script>
<script src='<s:url value="../includes/plugins/datatables/dataTables.bootstrap.min.js"/>'></script>
</body>
</html>

