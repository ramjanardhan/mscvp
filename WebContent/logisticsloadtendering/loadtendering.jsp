<%@page import="java.util.Map"%>
<%@page import="com.mss.ediscv.logisticsloadtendering.LogisticsLoadBean"%>
<%-- <%@ page contentType="text/html" pageEncoding="UTF-8"%> --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page buffer="50kb" autoFlush="true" %>
<%@ taglib prefix="s" uri="/struts-tags" %>


<%@ taglib uri="/WEB-INF/tlds/dbgrid.tld" prefix="grd"%>
<%@ page import="com.freeware.gridtag.*"%>
<%@page import="java.sql.Connection"%>
<%@  page import="com.mss.ediscv.util.AppConstants"%>
<%@ page import="com.mss.ediscv.util.ConnectionProvider"%>
<%@ page import="java.sql.SQLException"%>
<%@ page import = "java.util.ResourceBundle" %>

<!DOCTYPE html>
<html class=" js canvas canvastext geolocation crosswindowmessaging no-websqldatabase indexeddb hashchange historymanagement draganddrop websockets rgba hsla multiplebgs backgroundsize borderimage borderradius boxshadow opacity cssanimations csscolumns cssgradients no-cssreflections csstransforms no-csstransforms3d csstransitions  video audio localstorage sessionstorage webworkers applicationcache svg smil svgclippaths   fontface">
    <head>
        <title>Miracle Supply Chain Visibility portal</title>

        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
        <meta http-equiv="pragma" content="no-cache" />
        <meta http-equiv="cache-control" content="no-cache" />

        <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
        <!-- Bootstrap 3.3.5 -->

        <link rel="stylesheet" href='<s:url value="/includes/plugins/daterangepicker/daterangepicker.css"/>'/>
        <link rel="stylesheet" href='<s:url value="/includes/plugins/datatables/dataTables.bootstrap.css"/>' type="text/css">
        <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>
        <%--   <script language="JavaScript"
        src='<s:url value="/includes/js/generalValidations.js"/>'></script>  --%>
        <script>
            function doOnLoad()
            {
                $("#ltloadtendering").addClass("active");
                $("#logistics").addClass("active");
                $("#ltloadtendering i").addClass("text-red");

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
                    order: [[0, 'desc']]
                });
            });
        </script>

        <style>
            .row{
                margin-bottom: 5px; 
            }
        </style>

    </head>
    <%
        String check = null;
        if (request.getAttribute("check") != null) {
            check = request.getAttribute("check").toString();
        }

        //System.out.println("check-->"+check);
    %>
    <body onload="doOnLoad();
            check();" class="hold-transition skin-blue sidebar-mini">
        <script type="text/javascript" src='<s:url value="/includes/js/wz_tooltip.js"/>'></script>
        <div>
            <s:include value="../includes/template/header.jsp"/>
        </div>
        <div>
            <s:include value="../includes/template/sidemenu.jsp"/>
        </div>
        <div class="content-wrapper">
            <!-- Content Header (Page header) -->


            <!-- Main content --> 

            <section class="content-header">
                <h1>
                    Load Tendering
                    <!--<small>Logistics</small>-->
                </h1>
                <!--                <ol class="breadcrumb">
                                    <li><a href="#"><i class="fa fa-truck"></i>Logistics</a></li>
                                    <li class="active">Load Tendering</li>
                                </ol>-->
            </section>

            <section class="content">

                <div class="box box-primary">
                    <div class="box-header with-border">
                        <div class="box-tools pull-right">

                        </div>
                    </div>  
                    <div class="box-body">
                        <div id="text">

                            <div style="alignment-adjust:central;" >
                                <%String contextPath = request.getContextPath();
                                %>

                                <s:form action="../logisticsloadtendering/ltSearch.action" method="post" name="logisticsForm" id="logisticsForm" theme="simple">
                                    <s:hidden id="docdatepickerfrom" name="docdatepickerfrom" />
                                    <s:hidden id="docdatepicker" name="docdatepicker"/>
                                    <div class="form-group">
                                        <div class="row">
                                            <div class="col-sm-12">
                                                <div class="row">
                                                    <div class="col-sm-3"><label>Database&nbsp;&nbsp;&nbsp;&nbsp;:&nbsp;&nbsp;</label>
                                                        <s:radio cssClass="myRadio" id="database" name="database" value="%{database}" list="#@java.util.LinkedHashMap@{'MSCVP':'LIVE','ARCHIVE':'ARCHIVE'}"/>
                                                    </div>
                                                </div>
                                                <div class="row">
                                                    <div class="col-sm-4"> <label>Date Range</label>
                                                        <s:textfield name="reportrange"  id="reportrange" cssClass="form-control pull-left"   value="%{reportrange}" onchange="Date1();" tabindex="1"/> 
                                                    </div>

                                                    <script type="text/javascript">
        function Date1()
        {
            var date = document.logisticsForm.reportrange.value;
            var arr = date.split("-");
            var x = arr[1].trim();
            document.getElementById("docdatepickerfrom").value = arr[0];
            document.getElementById("docdatepicker").value = x;
        }
                                                    </script>

                                                    <div  class="col-sm-4">

                                                        <label>Document Type</label> 
                                                        <s:select headerKey="-1" cssClass="form-control" headerValue="Select Type" list="docTypeList" name="docType" id="docType" value="%{docType}" tabindex="2"/>
                                                    </div>

                                                    <div class="col-sm-4">
                                                        <label for="status">Status</label>
                                                        <s:select headerKey="-1" headerValue="Select Type" cssClass="form-control" list="{'Success','Error','Warning'}" name="status" id="status" value="%{status}"  tabindex="6"/> 
                                                    </div>



                                                </div>
                                                <div class="row">

                                                    <!--                                                     <div  class="col-sm-3">
                                                                                                            <label>Sender Name</label>  
                                                    <s:select headerKey="-1" cssClass="form-control" headerValue="Select Type" list="senderNameList" name="docSenderName" id="docSenderName" value="%{docSenderName}" tabindex="4" />
                                                </div>
                                                
                                                
                                                 <div class="col-sm-3">
                                                    <label>Receiver Name</label>
                                                    <s:select headerKey="-1" cssClass="form-control" headerValue="Select Type" list="receiverNameList" name="docRecName" id="docRecName" value="%{docRecName}" tabindex="6" />
                                                </div>-->


                                                    <div  class="col-sm-4">
                                                        <label>Sender </label>  
                                                        <s:textfield onchange="setSenderId();"  cssClass="form-control" list="senderIdMap" name="docSen" id="docSen" value="%{docSenderId}" tabindex="5"/>
                                                        <%Map<String, String> senderIdMap = (Map) session.getAttribute(AppConstants.SENDERSID_MAP);

                                                        %>
                                                        <datalist id="senderIdMap">
                                                            <% for (Map.Entry<String, String> entry : senderIdMap.entrySet()) {%>
                                                            <option value="<%=entry.getValue()%>"/>
                                                            <%}%>
                                                        </datalist> 
                                                        <s:hidden name="docSenderId" id="docSenderId" />
                                                    </div>
                                                    <div class="col-sm-4">
                                                        <label>Receiver </label>
                                                        <s:textfield onchange="setReceiverId();"  cssClass="form-control" list="receiverIdMap" name="docRec" id="docRec" value="%{docBusId}" tabindex="6"/>
                                                        <%Map<String, String> receiverIdMap = (Map) session.getAttribute(AppConstants.RECEIVERSId_MAP);

                                                        %>
                                                        <datalist id="receiverIdMap">
                                                            <% for (Map.Entry<String, String> entry : receiverIdMap.entrySet()) {%>
                                                            <option value="<%=entry.getValue()%>"/>
                                                            <%}%>
                                                        </datalist> 
                                                        <s:hidden name="docBusId" id="docBusId" />
                                                    </div>


                                                </div>
                                                <div class="row">
                                                    <div class="col-sm-3">
                                                        <label for="corrattribute">Correlation</label>
                                                        <s:select headerKey="-1" headerValue="Select Attribute" cssClass="form-control" list="correlationList" name="corrattribute" id="corrattribute" value="%{corrattribute}" tabindex="7" />
                                                    </div>
                                                    <div class="col-sm-3">
                                                        <label for="corrvalue">Value</label>
                                                        <s:textfield cssClass="form-control" name="corrvalue" id="corrvalue" value="%{corrvalue}" tabindex="8"/>
                                                    </div>
                                                    <div class="col-sm-3"><br>
                                                        <button  type="button" id="addButton" name="addButton" value="Add Div" class="btn btn-success"   style="margin-top:6px ;" tabindex="9"><i class="fa fa-plus"></i></button>
                                                        &nbsp; <label>Add Filter</label>
                                                    </div>
                                                    <div id="loadingAcoountSearch" class="loadingImg">
                                                        <span id ="LoadingContent" > <img src="<s:url value="/includes/images/Loader2.gif"/>"   ></span>
                                                    </div>
                                                </div>




                                                <script>
                                                    var count = 1;
                                                </script>                                          

                                                <div id="corr" style="display: none">
                                                    <div class="row">
                                                        <div class="col-sm-3">
                                                            <label for="corrattribute1">Correlation</label>
                                                            <s:select headerKey="-1" headerValue="Select Attribute" cssClass="form-control" list="correlationList" name="corrattribute1" id="corrattribute1" value="%{corrattribute1}" tabindex="10"/>
                                                        </div>
                                                        <div class="col-sm-3">
                                                            <label for="corrvalue1">Value</label>
                                                            <s:textfield cssClass="form-control" name="corrvalue1" id="corrvalue1" value="%{corrvalue1}" tabindex="11" />
                                                        </div>
                                                        <div class="col-sm-2"><br>
                                                            <button  type="button" id="removeButton1" name="removeButton1" value="Remove Div" class="btn btn-warning"   style="margin-top:6px ;" tabindex="14"><i class="fa fa-minus"></i></button>
                                                            &nbsp; <label>Remove Filter</label>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div id="corr1" style="display: none">
                                                    <div class="row">
                                                        <div class="col-sm-3">
                                                            <label for="corrattribute2">Correlation</label>
                                                            <s:select headerKey="-1" headerValue="Select Attribute" cssClass="form-control" list="correlationList" name="corrattribute2" id="corrattribute2" value="%{corrattribute2}" tabindex="12" />
                                                        </div>
                                                        <div class="col-sm-3">
                                                            <label for="corrvalue2">Value</label>
                                                            <s:textfield cssClass="form-control" name="corrvalue2" id="corrvalue2" value="%{corrvalue2}" tabindex="13"/>
                                                        </div>
                                                        <div class="col-sm-2"><br>
                                                            <button  type="button" id="removeButton2" name="removeButton2" value="Remove Div" class="btn btn-warning"   style="margin-top:6px ;" tabindex="14"><i class="fa fa-minus"></i></button>
                                                            &nbsp; <label>Remove Filter</label>
                                                        </div>
                                                    </div></div>
                                            </div>
                                        </div>
                                        <span id="span1">
                                        </span>
                                        <div class="row">






                                            <div class="col-sm-2"><s:submit value="Search"  onclick="return checkCorrelation()" cssClass="btn btn-primary col-sm-12" tabindex="14"/></div>

                                            <div class="col-sm-2"><strong><input type="button" value="Reset"  tabindex="15" class="btn btn-primary col-sm-12" onclick="return resetvalues();"/></strong></div>

                                            <s:hidden name="sampleValue" id="sampleValue" value="2"/>

                                        </s:form>

                                    </div>
                                </div>
                            </div>
                        </div></div></div>
            </section>
            <div id="gridDiv">    






                <s:if test="#session.loadList != null"> 
                    <%--- GRid start --%>
                    <section class="content">



                        <div class="row">
                            <div class="col-xs-12">
                                <div class="box">
                                    <!--                                    <div class="box-header">
                                                                            <h3 class="box-title">Table</h3>
                                                                        </div> /.box-header -->
                                    <div class="box-body">
                                        <%!String cssValue = "whiteStripe";
                                            int resultsetTotal;%>
                                        <div style="overflow-x:auto;">   
                                            <table align="left" width="100%"
                                                   border="0" cellpadding="0" cellspacing="0" >
                                                <tr>
                                                    <td style="background-color: white;">
                                                        <!--<div style="overflow-x:auto;">-->   
                                                        <table id="results"  class="table table-bordered table-hover">
                                                            <%
                                                                java.util.List list = (java.util.List) session.getAttribute(AppConstants.SES_LOAD_LIST);

                                                                if (list.size() != 0) {
                                                                    LogisticsLoadBean logisticsLoadBean;
                                                            %>
                                                            <input type="hidden" name="sec_lt_list" id="sec_lt_list" value="<%=list.size()%>"/> 
                                                            <thead><tr>
                                                                    <th >DateTime</th>
                                                                    <th >FileFormat</th> 
                                                                    <th >InstanceID</th>
                                                                    <th >Shipment</th>
                                                                    <th >Partner</th>


                                                                    <%-- <th >ISA #</th>  --%>

                                                                    <%-- <th >DOC_ORIGIN</th> --%>
                                                                    <th >TransType</th>
                                                                    <th >Direction</th>

                                                                    <th >Status</th>
                                                                        <%-- <th >ACK_STATUS</th>  --%>
                                                                    <th >Reprocess</th>
                                                                        <%--<%
                                                                            // out.println(session.getAttribute(AppConstants.SES_ROLE_ID));

                                                                            if (session.getAttribute(AppConstants.SES_ROLE_ID).equals("100") || session.getAttribute(AppConstants.SES_ROLE_ID).equals("104")) {
                                                                        %> --%>
                                                                    <th>#</th>
                                                                        <%-- <%}%> --%>
                                                                </tr>
                                                            </thead>
                                                            <tbody>
                                                                <tr>

                                                                    <%
                                                                        for (int i = 0; i < list.size(); i++) {
                                                                            logisticsLoadBean = (LogisticsLoadBean) list.get(i);

                                                                            if (i % 2 == 0) {
                                                                                cssValue = "whiteStripe";
                                                                            } else {
                                                                                cssValue = "grayEditSelection";
                                                                            }
                                                                    %>
                                                                    <td>
                                                                        <%                                                                            if (logisticsLoadBean.getDate_time_rec() != null && !"".equals(logisticsLoadBean.getDate_time_rec())) {
                                                                                out.println(logisticsLoadBean.getDate_time_rec().toString().substring(0, logisticsLoadBean.getDate_time_rec().toString().lastIndexOf(":")));
                                                                            } else {
                                                                                out.println("-");
                                                                            }
                                                                        %>

                                                                    </td>
                                                                    <td>
                                                                        <%
                                                                            if (logisticsLoadBean.getFile_type() != null && !"".equals(logisticsLoadBean.getFile_type())) {
                                                                                out.println(logisticsLoadBean.getFile_type());
                                                                            } else {
                                                                                out.println("-");
                                                                            }
                                                                        %>

                                                                    </td>
                                                                    <td>
                                                                        <%
                                                                            if (logisticsLoadBean.getFile_id() != null && !"".equals(logisticsLoadBean.getFile_id())) {
                                                                                out.println(logisticsLoadBean.getFile_id());
                                                                            } else {
                                                                                out.println("-");
                                                                            }

                                                                        %>
                                                                        <input type="hidden" name="Instance<%=i%>" id="Instance<%=i%>" value="<%=logisticsLoadBean.getFile_id()%>"/>  
                                                                    </td>

                                                                    <td><a style="color: deepskyblue" href="javascript:getLoadTenderingDetails('<%=logisticsLoadBean.getFile_id()%>','<%=logisticsLoadBean.getPoNumber()%>');">

                                                                            <%
                                                                                if (logisticsLoadBean.getShipmentId() != null && !"".equals(logisticsLoadBean.getShipmentId())) {
                                                                                    out.println(logisticsLoadBean.getShipmentId());
                                                                                } else {
                                                                                    out.println("-");
                                                                                }


                                                                            %>
                                                                            <input type="hidden" name="text<%=i%>" id="text<%=i%>" value="<%=logisticsLoadBean.getShipmentId()%>"/>  
                                                                        </a>   
                                                                    </td>
                                                                    <td>
                                                                        <%
                                                                            if (logisticsLoadBean.getPname() != null && !"".equals(logisticsLoadBean.getPname())) {

                                                                                out.println(logisticsLoadBean.getPname());
                                                                            } else {
                                                                                out.println("-");
                                                                            }


                                                                        %>
                                                                    </td>


                                                                    <td>
                                                                        <%                                                                            if (logisticsLoadBean.getTransaction_type() != null && !"".equals(logisticsLoadBean.getTransaction_type())) {
                                                                                out.println(logisticsLoadBean.getTransaction_type());
                                                                            } else {
                                                                                out.println("-");
                                                                            }
                                                                        %>

                                                                    </td>
                                                                    <td>
                                                                        <%
                                                                            if (logisticsLoadBean.getDirection() != null && !"".equals(logisticsLoadBean.getDirection())) {
                                                                                out.println(logisticsLoadBean.getDirection());
                                                                            } else {
                                                                                out.println("-");
                                                                            }
                                                                        %>

                                                                    </td>  


                                                                    <td>
                                                                        <%
                                                                            if (logisticsLoadBean.getStatus() != null && !"".equals(logisticsLoadBean.getStatus())) {
                                                                                if (logisticsLoadBean.getStatus().equalsIgnoreCase("ERROR")) {
                                                                                    out.println("<font color='red'>" + logisticsLoadBean.getStatus() + "</font>");
                                                                                } else if (logisticsLoadBean.getStatus().equalsIgnoreCase("SUCCESS")) {
                                                                                    out.println("<font color='green'>" + logisticsLoadBean.getStatus() + "</font>");
                                                                                } else {
                                                                                    out.println("<font color='orange'>" + logisticsLoadBean.getStatus() + "</font>");
                                                                                }
                                                                            } else {
                                                                                out.println("-");
                                                                            }
                                                                        %>

                                                                    </td>

                                                                    <td>
                                                                        <%
                                                                            if (logisticsLoadBean.getReProcessStatus() != null && !"".equals(logisticsLoadBean.getReProcessStatus())) {
                                                                                out.println(logisticsLoadBean.getReProcessStatus());

                                                                            } else {
                                                                                out.println("-");
                                                                            }
                                                                        %>

                                                                    </td>

                                                                    <%--<%
                                                                        if (session.getAttribute(AppConstants.SES_ROLE_ID).equals("100") || session.getAttribute(AppConstants.SES_ROLE_ID).equals("104")) {
                                                                    %>--%>
                                                                    <td> &nbsp; &nbsp; 
                                                                        <input type = "checkbox" name ="check_List<%=i%>" id = "check_List<%=i%>" value="<%= logisticsLoadBean.getFile_id()%>"/>&nbsp; &nbsp;  
                                                                    </td> 
                                                                    <%--<%
                                                                        }
                                                                    %>--%>

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
                                                <%                                                    if (list.size() != 0) {
                                                %>
                                                <tr >
                                                    <td align="right" colspan="28" style="background-color: white;">
                                                        <div align="right" id="pageNavPosition"></div>
                                                    </td>
                                                </tr> 

                                                <% }%>
                                                </tbody>
                                            </table>

                                        </div>



                                        <%-- Process butttons  start --%>
                                        <%--<%
                                            // out.println(session.getAttribute(AppConstants.SES_ROLE_ID));

                                            if ((session.getAttribute(AppConstants.SES_ROLE_ID).equals("100") || session.getAttribute(AppConstants.SES_ROLE_ID).equals("104")) && list.size() != 0) {
                                        %> --%>

                                        <div class="row gnexcel-space" style="margin-left: 65%;">
                                            <%--    <strong><input type="button" value="ReTransmit" class="btn btn-effect-ripple btn-primary" onmouseover="Tip('Click here to ReTransmit.')" onmouseout="UnTip()" onclick="return getloadTenderProcess(this, document.getElementById('sec_lt_list').value);" id="pre"/></strong> --%>
                                            <%--<strong><input type="button" value="ReSubmit" class="btn btn-effect-ripple btn-primary" onmouseover="Tip('Click here to Resubmit.')" onmouseout="UnTip()" onclick="return getloadTenderProcess(this, document.getElementById('sec_lt_list').value);" id="post"/></strong>
                                            <strong><input type="button" value="LifeCycle" class="btn btn-effect-ripple btn-primary" onmouseover="Tip('Click here to life cycle.')" onmouseout="UnTip()" onclick="return getLifeCycle(document.getElementById('sec_lt_list').value, 'logistics');" id="post"/></strong>
                                            <strong><input type="button" value="Generate Excel" class="btn btn-effect-ripple btn-primary" onclick="return gridDownload('loadTendering', 'xls');" onmouseover="Tip('Click here to generate an excel Report.')" onmouseout="UnTip()" id="excel"/> </strong>  --%>

<!--                                            <strong><input type="button" value="ReSubmit" class="btn btn-effect-ripple btn-primary" onmouseover="Tip('Click here to Resubmit.')" onmouseout="UnTip()" onclick="return getloadTenderProcess(this, document.getElementById('sec_lt_list').value, document.forms['logisticsForm']['database'].value);" id="post"/></strong>-->
                                            <strong><input type="button" value="LifeCycle" class="btn btn-effect-ripple btn-primary" onmouseover="Tip('Click here to life cycle.')" onmouseout="UnTip()" onclick="return getLifeCycle(document.getElementById('sec_lt_list').value, 'logistics', document.forms['logisticsForm']['database'].value);" id="post"/></strong>
                                            <strong><input type="button" value="Generate Excel" class="btn btn-effect-ripple btn-primary" onclick="return gridDownload('loadTendering', 'xls');" onmouseover="Tip('Click here to generate an excel Report.')" onmouseout="UnTip()" id="excel"/> </strong>  

                                        </div>

                                        <%--<%}%> --%>
                                        <%-- process buttons end--%>

                                    </div>
                                </div></div>
                        </div></section>
                    </s:if> 

            </div> 

            <div id="hide-menu1" class="hide-menu message ">

                <div class="row col-sm-12">

                    <br>
                    <div class="col-sm-6"> <label class="labelw"> Instance Id # </label>
                        <s:textfield cssClass="form-control"  required="required" placeholder="" id="instanceid" name="instanceid" readonly="true"/>
                    </div>
                    <div class="col-sm-6"> <label class="labelw"> Shipment # </label>
                        <s:textfield cssClass="form-control"  required="required" placeholder="" id="shipment" name="shipment" readonly="true"/>
                    </div>
                </div>
                <div class="row col-sm-12">
                    <div class="col-sm-6"> <label class="labelw"> Document Type </label>
                        <s:textfield cssClass="form-control"  required="required" placeholder="" id="documenttype" name="documenttype" readonly="true"/>
                    </div>
                    <div class="col-sm-6"> <label class="labelw">Transaction Type </label>
                        <s:textfield cssClass="form-control"  required="required" placeholder="" id="transactiontype" name="transactiontype" readonly="true"/>
                    </div>
                </div>
                <div class="row col-sm-12"> 
                    <div class="col-sm-6"> <label class="labelw"> Total Weight </label>
                        <s:textfield cssClass="form-control"  required="required" placeholder="" id="totWeight" name="totWeight" readonly="true"/>
                    </div>
                    <div class="col-sm-6"> <label class="labelw"> Pieces </label>
                        <s:textfield cssClass="form-control"  required="required" placeholder="" id="pieces" name="pieces" readonly="true"/>
                    </div>
                </div>
                <%--                <div class="row col-sm-12">
                                 
                   <div class="col-sm-6"> <label class="labelw"> Volume </label>
                                        <s:textfield cssClass="form-control"  required="required" placeholder="" id="Volume" name="Volume" readonly="true"/>
                                    </div>
                                </div>--%>
                <br>
                <div id="senderinfo">
                    <div class="row col-sm-12">
                        <div class="col-sm-6"> <h4 style="color: deepskyblue">Sender Info :</h4></div>
                        <div class="col-sm-6"></div>
                        <div class="col-sm-6"></div>

                    </div>
                    <br>
                    <div class="row col-sm-12">
                        <div class="col-sm-6"> <label class="labelw">  Id </label>
                            <s:textfield cssClass="form-control"  required="required" placeholder="" id="senderid" name="senderid" readonly="true"/>
                        </div>
                        <div class="col-sm-6"> <label class="labelw"> Name </label>
                            <s:textfield cssClass="form-control"  required="required" placeholder="" id="sendername" name="sendername" readonly="true"/>
                        </div>
                    </div>
                </div>
                <br>
                <div id="receiverinfo">
                    <div class="row col-sm-12">
                        <div class="col-sm-6"> <h4 style="color: deepskyblue">Receiver Info:</h4></div>
                        <div class="col-sm-6"></div>
                        <div class="col-sm-6"></div>
                    </div>

                    <br>
                    <div class="row col-sm-12 clear">
                        <div class="col-sm-6"> <label class="labelw">  Id </label>
                            <s:textfield cssClass="form-control"  required="required" placeholder="" id="receiverid" name="receiverid" readonly="true"/>
                        </div>
                        <div class="col-sm-6"> <label class="labelw"> Name </label>
                            <s:textfield cssClass="form-control"  required="required" placeholder="" id="receivername" name="receivername" readonly="true"/>
                        </div>
                    </div>
                </div>
                <div class="row col-sm-12 clear">
                    <div class="col-sm-6"> <label class="labelw">  ISA # </label>
                        <s:textfield cssClass="form-control"  required="required" placeholder="" id="isa" name="isa" readonly="true"/>
                    </div>
                    <div class="col-sm-6"> <label class="labelw"> GS # </label>
                        <s:textfield cssClass="form-control"  required="required" placeholder="" id="gs" name="gs" readonly="true"/>
                    </div>
                </div>

                <div class="row col-sm-12">
                    <div class="col-sm-6"> <label class="labelw">  ST # </label>
                        <s:textfield cssClass="form-control"  required="required" placeholder="" id="st" name="st" readonly="true"/>
                    </div>
                    <div class="col-sm-6"> <label class="labelw">ISA DATE </label>
                        <s:textfield cssClass="form-control"  required="required" placeholder="" id="isadate" name="isadate" readonly="true"/>
                    </div>

                    <div class="col-sm-6"> <label class="labelw">  ISA TIME  </label>
                        <s:textfield cssClass="form-control"  required="required" placeholder="" id="isatime" name="isatime" readonly="true"/>
                    </div>
                    <div class="col-sm-6"> <label class="labelw"> STATUS </label>
                        <s:textfield cssClass="form-control"  required="required" placeholder="" id="togglestatus" name="status" readonly="true"/>
                    </div>
                </div>

                <div class="row col-sm-12" style="margin-top:10px;">
                    <div class="col-sm-6"> <label class="labelw">  Pre Translation  </label></div>
                    <div class="col-sm-6"> <div id="pretranslation"> </div></div>
                </div>
                <div class="row col-sm-12">     
                    <div class="col-sm-6"> <label class="labelw"> Post Translation </label></div>
                    <div class="col-sm-6">   <div id="posttranslation"></div></div>
                </div>
                <div class="row col-sm-12">
                    <div class="col-sm-6"> <label class="labelw">997 AckFile</label></div>
                    <div class="col-sm-6">    <div id="ackfileid"></div></div>
                </div>
                <div class="row col-sm-12" id="errorDiv" style="display: none">
                    <div class="col-sm-6"> <label class="labelw"> Error Message </label></div>
                    <div  class="col-sm-6" id="errormessage" style="color:red;"></div>
                </div>
                <div class="row col-sm-12" id="errorReportDiv" style="display: none">
                    <div class="col-sm-6"> <label class="labelw">  Error&nbsp;Report </label></div>
                    <div class="col-sm-6"><div id="ErrReport"></div></div>
                </div>
                <div id="noresult"></div>
                <div class="col-sm-12" style="margin-top:10px;"><button type="button" class="btn btn-primary col-sm-11" id="hide-menu" onclick="hide()" value="X">Close</button></div>
            </div>  </div>



    </div>
    <div>
        <s:include value="../includes/template/footer.jsp"/>
    </div>


    <script>
        $('input[name="daterange"]').daterangepicker();
    </script>
    <script language="JavaScript" src='<s:url value="/includes/js/DateValidation.js"/>'></script>
    <script language="JavaScript" src='<s:url value="/includes/js/GeneralAjax.js"/>'></script>
    <script language="JavaScript" src='<s:url value="/includes/js/downloadAjax.js"/>'></script>
    <script language="JavaScript" src='<s:url value="/includes/js/Correlation.js"/>'></script>
    <script type="text/javascript" src='<s:url value="../includes/js/lifeCycle.js"/>'></script>
    <script src='<s:url value="../includes/bootstrap/js/bootstrap.min.js"/>'></script>
    <script src='<s:url value="../includes/plugins/daterangepicker/daterangepicker.js"/>'></script>
    <script src='<s:url value="../includes/bootstrap/js/app.min.js"/>'></script>
    <script src='<s:url value="../includes/plugins/datatables/jquery.dataTables.min.js"/>'></script>
    <script src='<s:url value="../includes/plugins/datatables/dataTables.bootstrap.min.js"/>'></script>

    <script type="text/javascript">

        function checkCorrelation() {
            var db = document.forms["logisticsForm"]["database"].value;
            if (db == '') {
                alert("Please select Database!!!");
                return false;
            }
            var corrattr = document.getElementById('corrattribute').value;
            var corrval = document.getElementById('corrvalue').value;

            var corrattr1 = document.getElementById('corrattribute1').value;
            var corrval1 = document.getElementById('corrvalue1').value;

            var corrattr2 = document.getElementById('corrattribute2').value;
            var corrval2 = document.getElementById('corrvalue2').value;

            if ((corrattr != "-1") && (corrval == "")) {
                alert("Please enter Correlation Value !!");
                return false;
            }
            if ((corrattr == "-1") && (corrval != "")) {
                alert("Please select Correlation !!");
                return false;
            }

            if ((corrattr1 != "-1") && (corrval1 == "")) {
                alert("Please enter Correlation Value !!");
                return false;
            }
            if ((corrattr1 == "-1") && (corrval1 != "")) {
                alert("Please select Correlation !!");
                return false;
            }

            if ((corrattr2 != "-1") && (corrval2 == "")) {
                alert("Please enter Correlation Value !!");
                return false;
            }
            if ((corrattr2 == "-1") && (corrval2 != "")) {
                alert("Please select Correlation !!");
                return false;
            }

        }
        function resetvalues()
        {
            $('#hide-menu1').removeClass('show-menu');
            // $('.myRadio').attr('checked', false);
            document.getElementById('docdatepickerfrom').value = "";
            document.getElementById('docdatepicker').value = "";
            document.getElementById('docSen').value = "";
            //   document.getElementById('docSenderName').value = "-1";
            document.getElementById('docRec').value = "";
            // document.getElementById('docRecName').value = "-1";
            document.getElementById('docSenderId').value = ""
            document.getElementById('docBusId').value = "";
            document.getElementById('corrattribute').value = "-1";
            document.getElementById('corrvalue').value = "";
            document.getElementById('docType').value = "-1";
            document.getElementById('corrattribute1').value = "-1";
            document.getElementById('corrvalue1').value = "";
            document.getElementById('corrattribute2').value = "-1";
            document.getElementById('corrvalue2').value = "";
            document.getElementById('status').value = "-1";
            document.getElementById('reportrange').value = "";
            var elements = document.getElementsByName('database');
            elements[0].checked = true;
            $('#detail_box').hide();
            $('#gridDiv').hide();
            $('#corr').hide();
            $('#corr1').hide();
            count = 1;
        }

        function hide()
        {

            $('#hide-menu1').removeClass('show-menu');
        }

        $("#addButton").click(function () {
            if (count == 1) {
                document.getElementById("corr").style.display = "block";
                if ((document.getElementById("corr1").style.display == "block"))
                {
                    count = count + 2;
                } else {
                    count++;
                }
            } else if (count == 2) {
                document.getElementById("corr1").style.display = "block";
                count++;
            } else {
                alert('Limit exceeded.... cannot add more fields !!');
            }
        })
        $("#removeButton1").click(function () {
            document.getElementById('corrattribute1').value = "-1";
            document.getElementById('corrvalue1').value = "";
            document.getElementById("corr").style.display = "none";
            // document.getElementById("addButton").style.display = "block";
            if (count == 3) {
                count = count - parseInt("2");
            } else {
                count = count - parseInt("1");
            }

        })
        $("#removeButton2").click(function () {
            document.getElementById('corrattribute2').value = "-1";
            document.getElementById('corrvalue2').value = "";
            document.getElementById("corr1").style.display = "none";
            if ((document.getElementById("corr").style.display == "block"))
            {
                count = count - parseInt("1");
            }


        })


        function setSenderId() {
            var id = document.getElementById("docSen").value;
            var arr = id.split("(");
            document.getElementById("docSenderId").value = arr[1].slice(0, -1);
            //alert(document.getElementById("docSenderId").value);
        }
        function setReceiverId() {
            var id = document.getElementById("docRec").value;
            var arr = id.split("(");
            document.getElementById("docBusId").value = arr[1].slice(0, -1);
            //alert(document.getElementById("docBusId").value);
        }




    </script>
</body>

</html>
