<%@page import="java.util.Map"%>
<%@page import="com.mss.ediscv.inventory.InventoryBean"%>
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
<%--<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>--%>
<%@page buffer="50kb" autoFlush="true" %>


<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8">
        <title>Miracle Supply Chain Visibility Portal</title>
        <!-- Tell the browser to be responsive to screen width -->
        <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
        <link rel="stylesheet" href='<s:url value="/includes/bootstrap/css/userdefined.css"/>'>
        <link rel="stylesheet" href='<s:url value="/includes/plugins/datatables/dataTables.bootstrap.css"/>' type="text/css">
        <link rel="stylesheet" href='<s:url value="/includes/plugins/daterangepicker/daterangepicker.css"/>' type="text/css">

        <script language="JavaScript" src='<s:url value="/includes/js/jquery-1.9.1.js"></s:url>'></script>
        <script language="JavaScript" src='<s:url value="/includes/js/DateValidation.js"/>'></script>
        <script language="JavaScript" src='<s:url value="/includes/js/GeneralAjax.js"/>'></script>
        <script language="JavaScript" src='<s:url value="/includes/js/downloadAjax.js"/>'></script>

        <script>
            $(function () {
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
    %>
    <body class="hold-transition skin-blue sidebar-mini" onload="doOnLoad() & check();">  
        <script type="text/javascript" src='<s:url value="/includes/js/wz_tooltip.js"/>'></script>
        <div>
            <s:include value="../includes/template/header.jsp"/>
        </div>
        <div>
            <s:include value="../includes/template/sidemenu.jsp"/>
        </div>
        <!-- Content Wrapper. Contains page content -->
        <div class="content-wrapper">
            <!-- Content Header (Page header) -->
            <!-- Main content --> 
            <section class="content-header">
                <h1>
                    Inventory
                    <!--                    <small>Manufacturing</small>-->
                </h1>
                <!--                <ol class="breadcrumb">
                                    <li><a href="#"><i class="fa fa-wrench"></i>Manufacturing</a></li>
                                    <li class="active">Document Repository</li>
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
                            <div  style="alignment-adjust:central;" >
                                <%String contextPath = request.getContextPath();%>
                                <s:form action="../inventory/inventorySearch.action" method="post" name="inventoryForm" id="inventoryForm" theme="simple">
                                    <s:hidden id="docdatepickerfrom" name="docdatepickerfrom" />
                                    <s:hidden id="docdatepicker" name="docdatepicker"/>
                                    <div id="resMsg" style="color: red"></div>
                                    <div class="form-group">
                                        <div class="row">
                                            <div class="col-sm-12">
                                                <div class="row">
                                                    <div class="col-sm-3"><label>Database&nbsp;&nbsp;&nbsp;&nbsp;:&nbsp;&nbsp;</label>
                                                        <s:radio cssClass="myRadio" id="database" name="database" value="%{database}" list="#@java.util.LinkedHashMap@{'MSCVP':'LIVE','ARCHIVE':'ARCHIVE'}"/>
                                                    </div>
                                                </div>
                                                <div class="row">
                                                    <div class="col-sm-3"> <label>Date Range</label>
                                                        <s:textfield name="reportrange"  id="reportrange" cssClass="form-control pull-left" value="%{reportrange}"  onchange="Date1()" tabindex="1"/> 
                                                    </div>
                                                    <div  class="col-sm-3">
                                                        <label>Document Type</label> 
                                                        <s:select headerKey="-1" cssClass="form-control" headerValue="Select Type" list="{'846'}" name="docType" id="docType" value="%{docType}" tabindex="2"/>
                                                    </div>

                                                    <div class="col-sm-3">
                                                        <label for="ackStatus">Ack Status</label>
                                                        <s:select headerKey="-1" headerValue="Select Type" cssClass="form-control" list="{'Overdue','Accepted','Rejected'}" name="ackStatus" id="ackStatus" value="%{ackStatus}" tabindex="7" /> 
                                                    </div>
                                                    <div class="col-sm-3">
                                                        <label for="status">Status</label>
                                                        <s:select headerKey="-1" headerValue="Select Type" cssClass="form-control" list="{'Success','Error','Warning'}" name="status" id="status" value="%{status}" tabindex="8" /> 
                                                    </div>


                                                </div>

                                                <div class="row">




                                                    <div  class="col-sm-3">
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
                                                    <div class="col-sm-3">
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
                                                        <s:select headerKey="-1" headerValue="Select Attribute" cssClass="form-control" list="#@java.util.LinkedHashMap@{'REPORTING_PERIOD':'Reporting Period','REPORTING_DATE':'Reporting Date','VENDOR_NAME':'Vendor Name','VENDOR_LOCATION':'Vendor Location','INSTANCE_ID':'Instance Id'}" name="corrattribute" id="corrattribute" value="%{corrattribute}" tabindex="9" />
                                                    </div>
                                                    <div class="col-sm-3">
                                                        <label for="corrvalue">Value</label>
                                                        <s:textfield cssClass="form-control" name="corrvalue" id="corrvalue" value="%{corrvalue}" tabindex="10"/>
                                                    </div>
                                                    <div class="col-sm-3"><br>
                                                        <button  type="button" id="addButton" name="addButton" value="Add Div" class="btn btn-success"   style="margin-top:6px ;" tabindex="11"><i class="fa fa-plus"></i></button>
                                                        &nbsp; <label>Add Filter</label>
                                                    </div>
                                                    <div id="loadingAcoountSearch" class="loadingImg">
                                                        <span id ="LoadingContent" > <img src="<s:url value="/includes/images/Loader2.gif"/>"   ></span>
                                                    </div>

                                                </div>
                                                <div id="corr" style="display: none">
                                                    <div class="row">
                                                        <div class="col-sm-3">
                                                            <label for="corrattribute1">Correlation</label>
                                                            <s:select headerKey="-1" headerValue="Select Attribute" cssClass="form-control" list="#@java.util.LinkedHashMap@{'REPORTING_PERIOD':'Reporting Period','REPORTING_DATE':'Reporting Date','VENDOR_NAME':'Vendor Name','VENDOR_LOCATION':'Vendor Location','INSTANCE_ID':'Instance Id'}" name="corrattribute1" id="corrattribute1" value="%{corrattribute1}" tabindex="12"/>
                                                        </div>
                                                        <div class="col-sm-3">
                                                            <label for="corrvalue1">Value</label>
                                                            <s:textfield cssClass="form-control" name="corrvalue1" id="corrvalue1" value="%{corrvalue1}" tabindex="13"/>
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
                                                            <s:select headerKey="-1" headerValue="Select Attribute" cssClass="form-control" list="#@java.util.LinkedHashMap@{'REPORTING_PERIOD':'Reporting Period','REPORTING_DATE':'Reporting Date','VENDOR_NAME':'Vendor Name','VENDOR_LOCATION':'Vendor Location','INSTANCE_ID':'Instance Id'}" name="corrattribute2" id="corrattribute2" value="%{corrattribute2}"  tabindex="14"/>
                                                        </div>
                                                        <div class="col-sm-3">
                                                            <label for="corrvalue2">Value</label>
                                                            <s:textfield cssClass="form-control" name="corrvalue2" id="corrvalue2" value="%{corrvalue2}" tabindex="15"/>
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
                                            <div class="col-sm-2"><s:submit value="Search"  onclick="return checkCorrelation();"   cssClass="btn btn-primary col-sm-12" tabindex="16"/></div>
                                            <div class="col-sm-2"><strong><input type="button" value="Reset"   class="btn btn-primary col-sm-12" onclick="return resetvaluesManufacteringDocRep();" tabindex="17"/></strong></div>
                                            </td>
                                            <s:hidden name="sampleValue" id="sampleValue" value="2"/>
                                        </s:form>
                                    </div>
                                </div>
                            </div>
                        </div></div>
                    <!-- Control Sidebar -->
                    <!-- /.control-sidebar -->
                    <!-- Add the sidebar's background. This div must be placed
                         immediately after the control sidebar -->
                    <!-- ./wrapper -->
            </section>
            <div id="gridDiv">     
                <s:if test="#session.inventoryList != null"> 
                    <%--- GRid start --%>
                    <%!String cssValue = "whiteStripe";
                        int resultsetTotal;%>
                    <section class="content">
                        <div class="row">
                            <div class="col-xs-12">
                                <div class="box">
                                    <!--                                    <div class="box-header">
                                                                            <h3 class="box-title">Table</h3>
                                                                        </div> /.box-header -->
                                    <div class="box-body">
                                        <div style="overflow-x:auto;">                 
                                            <table align="left" width="100%"
                                                   border="0" cellpadding="0" cellspacing="0" >
                                                <tr>
                                                    <td style="background-color: white;">
                                                        <div style="overflow-x:auto;"> 
                                                            <table id="results"  class="table table-bordered table-hover">
                                                                <%
                                                                    java.util.List list = (java.util.List) session.getAttribute(AppConstants.SES_INVENTORY_LIST);
                                                                    if (list.size() != 0) {
                                                                        InventoryBean inventoryBean;
                                                                %>
                                                                <thead><tr>
                                                                        <!--<th >DateTime</th>-->
                                                                        <th >InstanceId</th>
                                                                        <th >Partner</th>
                                                                        <th >Direction</th>
                                                                        <th >Reporting_Period</th>
                                                                        <th >Reporting_Date</th>
                                                                        <th >Vendor_Name</th>
                                                                        <th >Vendor_Location</th>
                                                                        <th >Status</th>
                                                                <tbody>
                                                                    <%
//                                                                        String corrattribute;
//                                                                        String corrattribute1;
//                                                                        String corrattribute2;
                                                                        for (int i = 0; i < list.size(); i++) {
                                                                            inventoryBean = (InventoryBean) list.get(i);
//                                                   
                                                                    %>

                                                                    </tr>
                                                                    <tr>
                                                                        <%-- <td>
                                                                             <%  if (inventoryBean.getDate_time_rec().toString().substring(0, inventoryBean.getDate_time_rec().toString().lastIndexOf(":")) != null
                                                                                         && !"".equals(inventoryBean.getDate_time_rec().toString().substring(0, inventoryBean.getDate_time_rec().toString().lastIndexOf(":")))) {
                                                                                     out.println(inventoryBean.getDate_time_rec().toString().substring(0, inventoryBean.getDate_time_rec().toString().lastIndexOf(":")));
                                                                                 } else {
                                                                                     out.println("-");
                                                                                 }
                                                                                 //out.println(inventoryBean.getDate_time_rec().toString().substring(0, inventoryBean.getDate_time_rec().toString().lastIndexOf(":")));
                                                                             %>
                                                                         </td> --%>

                                                                        <td>
                                                                            <a style="color: deepskyblue" href="javascript:getDetails('<%=inventoryBean.getFile_id()%>','<%=inventoryBean.getInventory_id()%>');">
                                                                                <%
                                                                                    if (inventoryBean.getFile_id() != null && !"".equals(inventoryBean.getFile_id())) {
                                                                                        out.println(inventoryBean.getFile_id());
                                                                                    } else {
                                                                                        out.println("-");
                                                                                    }
                                                                                   // out.println(inventoryBean.getFile_id());
                                                                                %>
                                                                            </a>
                                                                        </td>
                                                                        <td>
                                                                            <%
                                                                                if (inventoryBean.getPname() != null && !"".equals(inventoryBean.getPname())) {
                                                                                    out.println(inventoryBean.getPname());
                                                                                } else {
                                                                                    out.println("-");
                                                                                }
                                                                            %>
                                                                        </td>

                                                                        <td>
                                                                            <%
                                                                                if (inventoryBean.getDirection() != null && !"".equals(inventoryBean.getDirection())) {
                                                                                    out.println(inventoryBean.getDirection().toUpperCase());
                                                                                } else {
                                                                                    out.println("-");
                                                                                }
                                                                            %>
                                                                        </td>  
                                                                        <td>
                                                                            <%
                                                                                if (inventoryBean.getReportingPeriod() != null && !"".equals(inventoryBean.getReportingPeriod())) {
                                                                                    out.println(inventoryBean.getReportingPeriod().toUpperCase());
                                                                                } else {
                                                                                    out.println("-");
                                                                                }
                                                                            %>
                                                                        </td>  
                                                                        <td>
                                                                            <%
                                                                                if (inventoryBean.getReportingDate() != null && !"".equals(inventoryBean.getReportingDate())) {
                                                                                    out.println(inventoryBean.getReportingDate().toUpperCase());
                                                                                } else {
                                                                                    out.println("-");
                                                                                }
                                                                            %>
                                                                        </td>  
                                                                        <td>
                                                                            <%
                                                                                if (inventoryBean.getVendorName() != null && !"".equals(inventoryBean.getVendorName())) {
                                                                                    out.println(inventoryBean.getVendorName().toUpperCase());
                                                                                } else {
                                                                                    out.println("-");
                                                                                }
                                                                            %>
                                                                        </td>  
                                                                        <td>
                                                                            <%
                                                                                if (inventoryBean.getVendorLocation() != null && !"".equals(inventoryBean.getVendorLocation())) {
                                                                                    out.println(inventoryBean.getVendorLocation().toUpperCase());
                                                                                } else {
                                                                                    out.println("-");
                                                                                }
                                                                            %>
                                                                        </td>  
                                                                        <td>
                                                                            <%
                                                                                if (inventoryBean.getStatus().equalsIgnoreCase("ERROR")) {
                                                                                    out.println("<font color='red'>" + inventoryBean.getStatus().toUpperCase() + "</font>");
                                                                                } else if (inventoryBean.getStatus().equalsIgnoreCase("SUCCESS")) {
                                                                                    out.println("<font color='green'>" + inventoryBean.getStatus().toUpperCase() + "</font>");
                                                                                } else {
                                                                                    out.println("<font color='orange'>" + inventoryBean.getStatus().toUpperCase() + "</font>");
                                                                                }
                                                                            %>
                                                                        </td>


                                                                        <%--<%if (inventoryBean.getCorrvalue() != null && !"".equalsIgnoreCase(inventoryBean.getCorrvalue())) {%>
                                                                        <td ><%=inventoryBean.getCorrvalue()%> </td> 
                                                                        <%}
                                                                            if (inventoryBean.getCorrvalue1() != null && !"".equalsIgnoreCase(inventoryBean.getCorrvalue1())) {%>
                                                                        <td ><%=inventoryBean.getCorrvalue1()%> </td> 
                                                                        <%}
                                                                            if (inventoryBean.getCorrvalue2() != null && !"".equalsIgnoreCase(inventoryBean.getCorrvalue2())) {%>
                                                                        <td ><%=inventoryBean.getCorrvalue2()%> </td> 
                                                                        <%}
                                                                        %> --%>
                                                                    </tr>
                                                                    <%
                                                                        }
                                                                    } else {
                                                                    %>
                                                                    <tr><td>
                                                                            <%
                                                                                    out.println("<img  border='0' align='top'  src='" + contextPath + "/includes/images/alert.gif'/><b>No records found for the given search criteria. Please try a different search criteria!</b>");
                                                                                }
                                                                            %>
                                                                        </td>
                                                                    </tr>
                                                            </table>
                                                    </td>
                                                </tr>
                                                <%
                                                    if (list.size() != 0) {
                                                %>
                                                <tr >
                                                    <td align="right" colspan="28" style="background-color: white;">
                                                        <!--                                                        <div align="right" id="pageNavPosition">hello</div>-->
                                                    </td>
                                                </tr> 
                                                <% }%>       </tbody>
                                            </table>
                                        </div>
                                        <%-- Process butttons  start --%>
                                        <%
                                            if (list.size() != 0) {
                                        %>
                                        <table align="right">
                                            <tr>
                                                <td class="gnexcel-space" style="background-color: white;">
                                                    <strong><input type="button" value="Generate Excel" class="btn btn-effect-ripple btn-primary" onclick="return gridDownload('inventory', 'xls');" onmouseover="Tip('Click here to generate an excel Report.')" onmouseout="UnTip()" id="excel"/></strong>
                                                </td>
                                            </tr>
                                        </table> 
                                        <%}%>
                                    </div>
                                </div></div>
                        </div></section>
                    <!-- /.col -->
                    <!-- /.box -->
                    <!-- /.box -->
                    <!-- /.row -->
                    <%-- process buttons end--%>
                    <%-- Grid End --%>
                    <div id="hide-menu1" class="hide-menu message ">

                        <div class="row col-sm-12">

                            <br>
                            <div class="col-sm-6"> <label class="labelw"> File ID</label>
                                <s:textfield cssClass="form-control"  required="required" placeholder="" id="ManFileId" name="ManFileId"  readonly="true"/>
                            </div>
                            <div class="col-sm-6"> <label class="labelw"> Doc Format</label>
                                <s:textfield  cssClass="form-control"  required="required" placeholder="" id="ManDocumentType" name="ManDocumentType" readonly="true"/>
                            </div>
                        </div>
                        <div class="row col-sm-12"> 
                            <div class="col-sm-6"><label class="labelw">Doc Type</label>
                                <s:textfield cssClass="form-control"  required="required" placeholder="" id="Manpri_key_type" name="Manpri_key_type" readonly="true"/>
                            </div>
                            <div class="col-sm-6"> <label class="labelw">Doc Number</label>
                                <s:textfield  cssClass="form-control"  required="required" placeholder="" id="Manpri_key_value" name="Manpri_key_value" readonly="true"/>
                            </div>
                        </div>
                        <div class="row col-sm-12">
                            <div class="col-sm-6"> <label class="labelw"> Transaction Type</label>
                                <s:textfield cssClass="form-control"  required="required" placeholder="" id="ManTransactionType" name="ManTransactionType" readonly="true"/>
                            </div>
                            <div class="col-sm-6"> <label class="labelw">Item Count</label>
                                <s:textfield  cssClass="form-control"  required="required" placeholder="" id="itemCount" name="itemCount" readonly="true"/>
                            </div>
                        </div>
                        <br>
                        <div id="senderinfo">
                            <div class="row col-sm-12">
                                <div class="col-sm-6"> <h4 style="color: deepskyblue">Sender Info:</h4></div>
                                <div class="col-sm-6"></div>
                                <div class="col-sm-6"></div>

                            </div>
                            <br>
                            <div class="row col-sm-12">
                                <div class="col-sm-6"> <label class="labelw">  Sender Id</label>
                                    <s:textfield  cssClass="form-control"  required="required" placeholder="" id="ManSenderId" name="ManSenderId" readonly="true"/>
                                </div>
                                <div class="col-sm-6"><label class="labelw">  Sender Name</label>
                                    <s:textfield  cssClass="form-control"  required="required" placeholder="" id="ManSenderName" name="ManSenderName" readonly="true"/>
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
                                <div class="col-sm-6"><label class="labelw">  Receiver Id</label>
                                    <s:textfield  cssClass="form-control"  required="required" placeholder="" id="ManReceiverId" name="ManReceiverId" readonly="true"/>
                                </div>
                                <div class="col-sm-6"><label class="labelw">  Receiver Name</label>
                                    <s:textfield  cssClass="form-control"  required="required" placeholder="" id="ManReceiverName" name="ManReceiverName" readonly="true"/>
                                </div>
                            </div>
                        </div>
                        <div class="row col-sm-12 clear" >
                            <br>
                            <div class="col-sm-6"> <label class="labelw">   ISA</label>
                                <s:textfield  cssClass="form-control"  required="required" placeholder="" id="ManISA" name="ManISA" readonly="true"/>
                            </div>
                            <div class="col-sm-6"><label class="labelw">  GS</label>
                                <s:textfield  cssClass="form-control"  required="required" placeholder="" id="ManGs" name="ManGs" readonly="true"/>
                            </div>
                        </div>


                        <br/>

                        <div class="row col-sm-12" >
                            <%--                            <div class="col-sm-6"> <label class="labelw"> ST</label>
                                                            <s:textfield  cssClass="form-control"  required="required" placeholder="" id="ManSt" name="ManSt" readonly="true"/>
                                                        </div>--%>
                            <div class="col-sm-6"> <label class="labelw"> ISA Date</label>
                                <s:textfield  cssClass="form-control"  required="required" placeholder="" id="ManIsADate" name="ManIsADate" readonly="true"/>
                            </div>

                            <div class="col-sm-6"> <label class="labelw"> ISA Time</label>
                                <s:textfield  cssClass="form-control"  required="required" placeholder="" id="ManIsATime" name="ManIsATime" readonly="true"/>
                            </div>
                            <div class="col-sm-6"> <label class="labelw">  Status </label>
                                <s:textfield  cssClass="form-control"  required="required" placeholder="" id="ManStatus" name="ManStatus" readonly="true"/>
                            </div>
                        </div>

                        <div class="row col-sm-12" style="margin-top:10px;">
                            <div class="col-sm-6"> <label class="labelw"> Pre Translation</label></div>
                            <div class="col-sm-6"><div id="ManPreTranslation"></div>
                            </div></div>
                        <div class="row col-sm-12" >
                            <div class="col-sm-6"><label class="labelw"> Post Translation</label></div>
                            <div class="col-sm-6"> <div id="ManPostTranslation"></div></div>
                        </div>
                        <div class="row col-sm-12" >
                            <div class="col-sm-6"> <label class="labelw"> 997 ACKFile</label></div>
                            <div class="col-sm-6"> <div id="ManAckFileId"></div></div>   
                        </div>


                        <div class="row col-sm-12" id="errorDiv" style="display: none">
                            <div class="col-sm-6"> <label class="labelw">  Error&nbsp;Message </label></div>
                            <div class="col-sm-6" id="InvErrormessage" style="color: red"></div>
                        </div>


                        <div class="row col-sm-12" id="ManNullValues" style="display: none">
                            <div class="col-sm-6"> <label class="labelw"> display null values;</label></div></div>
                        <div id="noresult"></div>
                        <div class="row col-sm-12" style="margin-top:10px;">  <button type="button" class="btn btn-primary col-sm-11" style="margin-left:12px; " id="hide-menu" onclick="hide()" value="X">Close</button></div>
                    </div>


                </s:if> 
            </div>

        </div>
        <div>
            <s:include value="../includes/template/footer.jsp"/>
        </div>

        <!-- jQuery 2.1.4 -->

        <!-- jQuery UI 1.11.4 -->


        <!-- daterangepicker -->

        <script src='<s:url value="../includes/plugins/daterangepicker/daterangepicker.js"/>'></script>

        <!-- AdminLTE App -->
        <script src='<s:url value="../includes/bootstrap/js/app.min.js"/>'></script>
        <script src='<s:url value="../includes/plugins/datatables/jquery.dataTables.min.js"/>'></script>
        <script src='<s:url value="../includes/plugins/datatables/dataTables.bootstrap.min.js"/>'></script>


        <script>
                            function doOnLoad()
                            {
                                $("#inventory").addClass("active");
                                $("#manufacturing").addClass("active");
                                $("#inventory i").addClass("text-red");
                                document.getElementById('loadingAcoountSearch').style.display = "none";
                            }

                            function getDetails(val, id) {
                                var db = document.forms["inventoryForm"]["database"].value;
                                getInventoryDetails(val, id, db);
                            }
                            function checkCorrelation() {
                                //alert("checkCorrelation");
                                var db = document.forms["inventoryForm"]["database"].value;
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
                                    // document.getElementById('resMsg').innerHTML = "please enter Correlation Value!";
                                    alert("Please enter Correlation Value !!");
                                    return false;
                                }
                                if ((corrattr == "-1") && (corrval != "")) {
                                    // document.getElementById('resMsg').innerHTML = "please select Correlation!";
                                    alert("Please select Correlation !!");
                                    return false;
                                }
                                if ((corrattr1 != "-1") && (corrval1 == "")) {
                                    //document.getElementById('resMsg').innerHTML = "please enter Correlation Value!";
                                    alert("Please enter Correlation Value !!");
                                    return false;
                                }
                                if ((corrattr1 == "-1") && (corrval1 != "")) {
                                    //document.getElementById('resMsg').innerHTML = "please select Correlation!";
                                    alert("Please select Correlation !!");
                                    return false;
                                }
                                if ((corrattr2 != "-1") && (corrval2 == "")) {
                                    //document.getElementById('resMsg').innerHTML = "please enter Correlation Value!";
                                    alert("Please enter Correlation Value !!");
                                    return false;
                                }
                                if ((corrattr2 == "-1") && (corrval2 != "")) {
                                    //document.getElementById('resMsg').innerHTML = "please select Correlation!";
                                    alert("Please select Correlation !!");
                                    return false;
                                }
                            }
                            function resetvaluesManufacteringDocRep() {
                                $('#hide-menu1').removeClass('show-menu');
                                //  $('.myRadio').attr('checked', false);
                                document.getElementById('docdatepickerfrom').value = "";
                                document.getElementById('docdatepicker').value = "";
                                document.getElementById('docSen').value = "";
                                // document.getElementById('docSenderName').value = "-1";
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
                                document.getElementById('ackStatus').value = "-1";
                                document.getElementById('status').value = "-1";
                                document.getElementById('reportrange').value = "";
                                var elements = document.getElementsByName('database');
                                elements[0].checked = true;
                                $('#gridDiv').hide();
                                $('#corr').hide();
                                $('#corr1').hide();
                                count = 1;
                            }

                            function hide()
                            {

                                $('#hide-menu1').removeClass('show-menu');
                            }
//                $('body,html').click(function (e) {
//                    $('#hide-menu1').removeClass('show-menu');
//                });


                            function check()
                            {
                                var value1 = document.getElementById("corrattribute1").value;
                                if (value1 != "-1")
                                    document.getElementById("corr").style.display = "block";
                                else
                                    document.getElementById("corr").style.display = "none";
                                var value2 = document.getElementById("corrattribute2").value;
                                if (value2 != "-1")
                                    document.getElementById("corr1").style.display = "block";
                                else
                                    document.getElementById("corr1").style.display = "none";
                            }
                            function Date1()
                            {
                                var date = document.inventoryForm.reportrange.value;
                                var arr = date.split("-");
                                var x = arr[1].trim();
                                document.getElementById("docdatepickerfrom").value = arr[0];
                                document.getElementById("docdatepicker").value = x;
                            }
                            var count = 1;
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


