
<%@page import="java.util.Map"%>
<%@page import="com.mss.ediscv.logisticsinvoice.LogisticsInvoiceBean"%>
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
<%--<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>--%> 
<%@page buffer="50kb" autoFlush="true" %>
<%--<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>--%>

<!DOCTYPE html>
<html class=" js canvas canvastext geolocation crosswindowmessaging no-websqldatabase indexeddb hashchange historymanagement draganddrop websockets rgba hsla multiplebgs backgroundsize borderimage borderradius boxshadow opacity cssanimations csscolumns cssgradients no-cssreflections csstransforms no-csstransforms3d csstransitions  video audio localstorage sessionstorage webworkers applicationcache svg smil svgclippaths   fontface">
    <head>
        <title>Miracle Supply Chain Visibility portal</title>

        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
        <meta http-equiv="pragma" content="no-cache" />
        <meta http-equiv="cache-control" content="no-cache" />

        <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
        <link rel="stylesheet" href='<s:url value="/includes/bootstrap/css/userdefined.css"/>'>
        <link rel="stylesheet" href='<s:url value="/includes/plugins/datatables/dataTables.bootstrap.css"/>' type="text/css">
        <link rel="stylesheet" href='<s:url value="/includes/plugins/daterangepicker/daterangepicker.css"/>' type="text/css"> 
        <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>
        <script>
            function doOnLoad()
            {
                $("#ltfinance").addClass("active");
                $("#ltinvoice").addClass("active");
                $("#logistics").addClass("active");
                $("#ltinvoice i").addClass("text-red");
                document.getElementById('loadingAcoountSearch').style.display = "none";
            }
        </script>
        <script type="text/javascript">
            function hide()
            {

                $('#hide-menu1').removeClass('show-menu');
            }
//            $('body,html').click(function(e){
//                $('#hide-menu1').removeClass('show-menu');
//            });
        </script>
        <!--        <script type="text/javascript">
                    function   getLogisticsInvDetailsInfo(InvoiceNumber, id) {
                        getLogisticsInvDetails(InvoiceNumber, id);
                    }
        
                </script> -->

        <script>
            $(function () {
                // $("#example1").DataTable();
                $('#results').DataTable({
                    "paging": true,
                    "lengthChange": true,
                    "searching": true,
                    "ordering": true,
                    "info": true,
                    "autoWidth": true,
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
                    Invoice
                    <!--<small>Logistics</small>-->
                </h1>
                <!--                <ol class="breadcrumb">
                                    <li><a href="#"><i class="fa fa-truck"></i>Logistics</a></li>
                                    <li class="active">Invoice</li>
                                </ol>-->
            </section>

            <section class="content">

                <div class="box box-primary">
                    <!--                    <div class="box-header with-border">
                                            <h3 class="box-title">Invoice</h3>
                                            <div class="box-tools pull-right">
                    
                                            </div>
                                        </div>  -->
                    <div class="box-body">
                        <div id="text">
                            <div style="alignment-adjust:central;" >
                                <%String contextPath = request.getContextPath();
                                %>
                                <s:form action="../logisticsinvoice/invoiceSearch.action" method="post" name="logisticsForm" id="logisticsForm" theme="simple">
                                    <s:hidden id="datepickerfrom" name="datepickerfrom" />
                                    <s:hidden id="datepickerTo" name="datepickerTo"/>
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
            document.getElementById("datepickerfrom").value = arr[0];
            document.getElementById("datepickerTo").value = x;
        }
                                                    </script>

                                                    <div  class="col-sm-4">
                                                        <label>Document Type</label>  
                                                        <s:select headerKey="-1" headerValue="Select Type" list="docTypeList" name="docType" id="docType" value="%{docType}" tabindex="2" cssClass="form-control"/>
                                                    </div>

                                                    <div  class="col-sm-4">
                                                        <label>Status</label>  
                                                        <s:select headerKey="-1" headerValue="Select Type" list="{'Success','Error','Warning'}" name="status" id="status" value="%{status}"  cssClass="form-control" tabindex="7"/> 
                                                    </div>




                                                </div>


                                                <div class="row">




                                                    <div  class="col-sm-4">
                                                        <label>Sender </label>  
                                                        <s:textfield onchange="setSenderId();"  cssClass="form-control" list="senderIdMap" name="docSen" id="docSen" value="%{invSenderId}" tabindex="5"/>
                                                        <%Map<String, String> senderIdMap = (Map) session.getAttribute(AppConstants.SENDERSID_MAP);

                                                        %>
                                                        <datalist id="senderIdMap">
                                                            <% for (Map.Entry<String, String> entry : senderIdMap.entrySet()) {%>
                                                            <option value="<%=entry.getValue()%>"/>
                                                            <%}%>
                                                        </datalist> 
                                                        <s:hidden name="invSenderId" id="invSenderId" />
                                                    </div>
                                                    <div class="col-sm-4">
                                                        <label>Receiver </label>
                                                        <s:textfield onchange="setReceiverId();"  cssClass="form-control" list="receiverIdMap" name="docRec" id="docRec" value="%{invReceiverId}" tabindex="6"/>
                                                        <%Map<String, String> receiverIdMap = (Map) session.getAttribute(AppConstants.RECEIVERSId_MAP);

                                                        %>
                                                        <datalist id="receiverIdMap">
                                                            <% for (Map.Entry<String, String> entry : receiverIdMap.entrySet()) {%>
                                                            <option value="<%=entry.getValue()%>"/>
                                                            <%}%>
                                                        </datalist> 
                                                        <s:hidden name="invReceiverId" id="invReceiverId" />
                                                    </div>




                                                </div>
                                                <div class="row">
                                                    <div class="col-sm-3">
                                                        <label for="corrattribute">Correlation</label>
                                                        <s:select headerKey="-1" headerValue="Select Attribute" cssClass="form-control" list="correlationList" name="corrattribute" id="corrattribute" value="%{corrattribute}"  tabindex="8"/>
                                                    </div>
                                                    <div class="col-sm-3">
                                                        <label for="corrvalue">Value</label>
                                                        <s:textfield cssClass="form-control" name="corrvalue" id="corrvalue" value="%{corrvalue}" tabindex="9"/>
                                                    </div>
                                                    <div class="col-sm-3"><br>
                                                        <button  type="button" id="addButton" name="addButton" value="Add Div" class="btn btn-success"   style="margin-top:6px ;" tabindex="10"><i class="fa fa-plus"></i></button>
                                                        &nbsp; <label>Add Filter</label>
                                                    </div>
                                                </div>
                                                <script>

                                                </script>                                      

                                                <script>
                                                    var count = 0;
                                                </script>                                          

                                                <div id="corr" style="display: none">
                                                    <div class="row">
                                                        <div class="col-sm-3">
                                                            <label for="corrattribute1">Correlation</label>
                                                            <s:select headerKey="-1" headerValue="Select Attribute" cssClass="form-control" list="correlationList" name="corrattribute1" id="corrattribute1" value="%{corrattribute1}" tabindex="11"/>
                                                        </div>
                                                        <div class="col-sm-3">
                                                            <label for="corrvalue1">Value</label>
                                                            <s:textfield cssClass="form-control" name="corrvalue1" id="corrvalue1" value="%{corrvalue1}" tabindex="12"/>
                                                        </div>
                                                        <div class="col-sm-2"><br>
                                                            <button  type="button" id="removeButton1" name="removeButton1" value="Remove Div" class="btn btn-warning"   style="margin-top:6px ;" tabindex="14"><i class="fa fa-minus"></i></button>
                                                            &nbsp; <label>Remove Filter</label>
                                                        </div>
                                                    </div>
                                                </div>


                                            </div>
                                        </div>
                                        <div id="loadingAcoountSearch" class="loadingImg">
                                            <span id ="LoadingContent" > <img src="<s:url value="/includes/images/Loader2.gif"/>"></span>
                                        </div>

                                        <div class="row">
                                            <div class="col-sm-2"> <s:submit value="Search" Class="btn btn-primary col-sm-12" onclick="return checkCorrelation();" tabindex="13"/></div>
                                            <div class="col-sm-2"> <strong><input type="button" value="Reset" class="btn btn-primary col-sm-12" tabindex="14" onclick="return resetvalues();"/></strong></div>
                                                    <s:hidden name="sampleValue" id="sampleValue" value="2"/>
                                                </s:form></div>
                                </div>
                            </div>
                        </div>
                    </div></div>
            </section>

            <div id="gridDiv">


                <s:if test="#session.ltInvoiceList != null"> 
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
                                                   border="0" cellpadding="0" cellspacing="0">
                                                <tr>
                                                    <td style="background-color: white;">

                                                        <table align="left" id="results" width="100%"
                                                               border="0" cellpadding="0" cellspacing="0" class="table table-bordered table-hover">
                                                            <%
                                                                java.util.List list = (java.util.List) session.getAttribute(AppConstants.SES_LTINVOICE_LIST);

                                                                if (list.size() != 0) {
                                                                    LogisticsInvoiceBean logisticsInvoiceBean;
                                                            %>
                                                            <thead> <tr>
                                                                    <th>Inv&nbsp;Date</th>
                                                                    <th>InstanceId</th>
                                                                    <th>Partner</th>

                                                                    <th>Invoice#</th>
                                                                    <th>Shipment</th>
                                                                        <%-- <td >ISA #</td>  --%>

                                                                    <%-- <td >DOC_ORIGIN</td> --%>
                                                                    <th>PO#</th>
                                                                    <th>Item&nbsp;Qty</th>
                                                                    <th>Inv&nbsp;Amount</th>

                                                                    <th>Status</th>




                                                                </tr></thead><tbody>
                                                                <tr>

                                                                    <%
                                                                        for (int i = 0; i < list.size(); i++) {
                                                                            logisticsInvoiceBean = (LogisticsInvoiceBean) list.get(i);

                                                                            if (i % 2 == 0) {
                                                                                cssValue = "whiteStripe";
                                                                            } else {
                                                                                cssValue = "grayEditSelection";
                                                                            }
                                                                    %>
                                                                    <td>
                                                                        <%
                                                                            if (logisticsInvoiceBean.getInvDate() != null && !"".equals(logisticsInvoiceBean.getInvDate())) {
                                                                                out.println(logisticsInvoiceBean.getInvDate());
                                                                            } else {
                                                                                out.println("-");
                                                                            }
                                                                        %>

                                                                    </td>
                                                                    <td>
                                                                        <%
                                                                            if (logisticsInvoiceBean.getInstanceId() != null && !"".equals(logisticsInvoiceBean.getInstanceId())) {
                                                                                out.println(logisticsInvoiceBean.getInstanceId());
                                                                            } else {
                                                                                out.println("-");
                                                                            }
                                                                        %>
                                                                    </td>
                                                                    <td>
                                                                        <%
                                                                            if (logisticsInvoiceBean.getPartner() != null && !"".equals(logisticsInvoiceBean.getPartner())) {
                                                                                out.println(logisticsInvoiceBean.getPartner());
                                                                            } else {
                                                                                out.println("-");
                                                                            }
                                                                        %>
                                                                    </td>

                                                                    <td>  <a style="color: deepskyblue" href="javascript:getLogisticsInvDetails('<%=logisticsInvoiceBean.getInvoiceNumber()%>','<%=logisticsInvoiceBean.getId()%>');"  >
                                                                            <%
                                                                                if (logisticsInvoiceBean.getInvoiceNumber() != null && !"".equals(logisticsInvoiceBean.getInvoiceNumber())) {
                                                                                    out.println(logisticsInvoiceBean.getInvoiceNumber());
                                                                                } else {
                                                                                    out.println("-");
                                                                                }
                                                                            %>
                                                                        </a>
                                                                    </td>


                                                                    <td>
                                                                        <%
                                                                            if (logisticsInvoiceBean.getShipmentId() != null && !"".equals(logisticsInvoiceBean.getShipmentId())) {
                                                                                out.println(logisticsInvoiceBean.getShipmentId());
                                                                            } else {
                                                                                out.println("-");
                                                                            }
                                                                        %>
                                                                    </td>

                                                                    <td>
                                                                        <%
                                                                            if (logisticsInvoiceBean.getPoNumber() != null && !"".equals(logisticsInvoiceBean.getPoNumber())) {
                                                                                out.println(logisticsInvoiceBean.getPoNumber());
                                                                            } else {
                                                                                out.println("-");
                                                                            }
                                                                        %>
                                                                    </td>
                                                                    <td>
                                                                        <%
                                                                            if (logisticsInvoiceBean.getItemQty() != null && !"".equals(logisticsInvoiceBean.getItemQty())) {
                                                                                out.println(logisticsInvoiceBean.getItemQty());
                                                                            } else {
                                                                                out.println("-");
                                                                            }
                                                                        %>

                                                                    </td>
                                                                    <td>
                                                                        <%
                                                                            if (logisticsInvoiceBean.getInvAmount() != null && !"".equals(logisticsInvoiceBean.getInvAmount())) {
                                                                                out.println(logisticsInvoiceBean.getInvAmount());
                                                                            } else {
                                                                                out.println("-");
                                                                            }
                                                                        %>

                                                                    </td>

                                                                    <td>
                                                                        <%
                                                                            if (logisticsInvoiceBean.getStatus() != null && !"".equals(logisticsInvoiceBean.getStatus())) {
                                                                                if (logisticsInvoiceBean.getStatus().equalsIgnoreCase("ERROR")) {
                                                                                    out.println("<font color='red'>" + logisticsInvoiceBean.getStatus().toUpperCase() + "</font>");
                                                                                } else if (logisticsInvoiceBean.getStatus().equalsIgnoreCase("SUCCESS")) {
                                                                                    out.println("<font color='green'>" + logisticsInvoiceBean.getStatus().toUpperCase() + "</font>");
                                                                                } else {
                                                                                    out.println("<font color='orange'>" + logisticsInvoiceBean.getStatus().toUpperCase() + "</font>");
                                                                                }
                                                                            } else {
                                                                                out.println("-");
                                                                            }
                                                                        %>
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
                                                <%                                                    if (list.size() != 0) {
                                                %>

                                                <% }%></tbody>
                                            </table>

                                        </div>
                                        <%-- Process butttons  start --%>
                                        <%
                                            if (list.size() != 0) {
                                        %>
                                        <table align="right">
                                            <tr>
                                                <td class="gnexcel-space" style="background-color: white;">
                                                    <strong><input type="button" value="Generate Excel" class="btn btn-effect-ripple btn-primary" onclick="return gridDownload('ltInvoice', 'xls');" onmouseover="Tip('Click here to generate an excel Report.')" onmouseout="UnTip()" id="excel"/></strong>
                                                </td>
                                            </tr>
                                        </table> 
                                        <%}%>
                                        <%-- process buttons end--%>
                                        <%-- Grid End --%>

                                    </div>
                                </div></div></div>
                    </section></s:if> 
                    <div id="hide-menu1" class="hide-menu message ">

                        <div class="row col-sm-12">

                            <br>
                            <div class="col-sm-6"> <label class="labelw"> Instance Id </label>
                            <s:textfield cssClass="form-control"  required="required" placeholder="" id="InvInstanceid" name="InvInstanceid" readonly="true"/>
                        </div>

                        <div class="col-sm-6"> <label class="labelw"> Shipment # </label>
                            <s:textfield cssClass="form-control"  required="required" placeholder="" id="InvPo" name="InvPo" readonly="true"/>
                        </div>
                    </div> <div class="row col-sm-12">
                        <div class="col-sm-6"> <label class="labelw">Document Type</label>
                            <s:textfield cssClass="form-control"  required="required" placeholder="" id="InvDocpe" name="InvDocpe"  readonly="true"/>
                        </div>
                        <div class="col-sm-6"> <label class="labelw">Transaction Type</label>
                            <s:textfield cssClass="form-control"  required="required" placeholder="" id="InvTransactiontype" name="InvTransactiontype" readonly="true"/>
                        </div>                     

                    </div>
                    <div class="row col-sm-12">
                        <div class="col-sm-6"> <label class="labelw">Invoice # </label>
                            <s:textfield cssClass="form-control"  required="required" placeholder="" id="InvNum" name="InvNum" readonly="true"/>
                        </div>
                        <div class="col-sm-6"> <label class="labelw">Item Quantity  </label>
                            <s:textfield cssClass="form-control"  required="required" placeholder="" id="InvItemQty" name="InvItemQty" readonly="true"/>
                        </div>
                        <div class="col-sm-6"> <label class="labelw">Invoice Amount </label>
                            <s:textfield cssClass="form-control"  required="required" placeholder="" id="InvAmt" name="InvAmt" readonly="true"/>
                        </div>

                    </div>
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
                                <s:textfield cssClass="form-control"  required="required" placeholder="" id="InvSenderid" name="InvSenderid" readonly="true"/>
                            </div>
                            <div class="col-sm-6"> <label class="labelw"> Name </label>
                                <s:textfield cssClass="form-control"  required="required" placeholder="" id="InvSendername" name="InvSendername" readonly="true"/>
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
                                <s:textfield cssClass="form-control"  required="required" placeholder="" id="InvReceiverid" name="InvReceiverid" readonly="true"/>
                            </div>
                            <div class="col-sm-6"> <label class="labelw"> Name </label>
                                <s:textfield cssClass="form-control"  required="required" placeholder="" id="InvReceivername" name="InvReceivername" readonly="true"/>
                            </div>
                        </div>
                    </div>
                    <div class="row col-sm-12 clear">
                        <div class="col-sm-6"> <label class="labelw">  ISA #   </label>
                            <s:textfield cssClass="form-control"  required="required" placeholder="" id="InvIsa" name="InvIsa" readonly="true"/>
                        </div>
                        <div class="col-sm-6"> <label class="labelw"> GS #   </label>
                            <s:textfield cssClass="form-control"  required="required" placeholder="" id="InvGs" name="InvGs" readonly="true"/>
                        </div>
                    </div>

                    <br/>

                    <div class="row col-sm-12">
                        <div class="col-sm-6"> <label class="labelw">  ST #   </label>
                            <s:textfield cssClass="form-control"  required="required" placeholder="" id="InvSt" name="InvSt" readonly="true"/>
                        </div>
                        <div class="col-sm-6"> <label class="labelw">ISA DATE  </label>
                            <s:textfield cssClass="form-control"  required="required" placeholder="" id="InvIsadate" name="InvIsadate" readonly="true"/>
                        </div>

                        <div class="col-sm-6"> <label class="labelw">ISA TIME </label>
                            <s:textfield cssClass="form-control"  required="required" placeholder="" id="InvIsatime" name="InvIsatime" readonly="true"/>
                        </div>
                        <div class="col-sm-6"> <label class="labelw"> STATUS </label>
                            <s:textfield cssClass="form-control"  required="required" placeholder="" id="InvStatus" name="InvStatus" readonly="true"/>
                        </div>
                    </div>

                    <div class="row col-sm-12" style="margin-top:10px;">
                        <div class="col-sm-6"> <label class="labelw">Pre-Translation</label></div>
                        <div class="col-sm-6">  <div id="InvPreTranslation"></div>

                        </div>
                    </div>
                    <div class="row col-sm-12" >
                        <div class="col-sm-6"> <label class="labelw">Post-Translation</label></div>
                        <div class="col-sm-6"> <div id="InvPostTranslation"></div> 

                        </div>
                    </div>
                    <%--<div class="row col-sm-12" >
                        <div class="col-sm-6"> <label class="labelw">Original File</label></div>
                        <div class="col-sm-6">  <div id="InvORGFILEPATH"></div>

                        </div>
                    </div>--%>
                    <div class="row col-sm-12" >
                        <div class="col-sm-6"> <label class="labelw">997 AckFile</label></div>
                        <div class="col-sm-6"> <div id="InvAckfileid"></div>

                        </div>
                    </div>
                    <div class="row col-sm-12" id="errorDiv" style="display: none">
                        <div class="col-sm-6"> <label class="labelw"> Error&nbsp;Message </label></div>
                        <div class="col-sm-6" id="InvErrormessage" style="color: red"></div>
                    </div>

                    <div class="row col-sm-12" id="errorReportDiv" style="display: none">
                        <div class="col-sm-6"> <label class="labelw">  Error&nbsp;Report </label></div>
                        <div class="col-sm-6"><div id="ErrReport"></div></div>
                    </div>

                    <div id="noresult"></div>
                    <div class="col-sm-12" style="margin-top:10px;"><button type="button" class="btn btn-primary col-sm-11" id="hide-menu" onclick="hide()" value="X">Close</button></div>   
                </div> 
            </div>
        </div>
    </div> 

</div>
<div>
    <s:include value="../includes/template/footer.jsp"/>
</div>


<script>
    $('input[name="daterange"]').daterangepicker();
</script>
<script language="JavaScript"  src='<s:url value="/includes/js/DateValidation.js"/>'></script>
<script language="JavaScript"  src='<s:url value="/includes/js/GeneralAjax.js"/>'></script>
<script language="JavaScript"  src='<s:url value="/includes/js/downloadAjax.js"/>'></script>
<script language="JavaScript"  src='<s:url value="/includes/js/Correlation.js"/>'></script> 
<script src='<s:url value="../includes/plugins/daterangepicker/daterangepicker.js"/>'></script>
<script src='<s:url value="../includes/bootstrap/js/app.min.js"/>'></script>
<script src='<s:url value="../includes/plugins/datatables/jquery.dataTables.min.js"/>'></script>
<script src='<s:url value="../includes/plugins/datatables/dataTables.bootstrap.min.js"/>'></script>

<script type="text/javascript">
    function checkCorrelation()
    {
        var db = document.forms["logisticsForm"]["database"].value;
        if (db == '') {
            alert("Please select Database!!!");
            return false;
        }
        var corrattr = document.getElementById('corrattribute').value;
        var corrval = document.getElementById('corrvalue').value;
        if ((corrattr != "-1") && (corrval == "")) {
            alert("Please enter Correlation Value !!");
            return false;
        }
        if ((corrattr == "-1") && (corrval != "")) {
            alert("Please select Correlation !!");
            return false;
        }
    }
    function resetvalues()
    {
        $('#hide-menu1').removeClass('show-menu');
        // $('.myRadio').attr('checked', false);
        document.getElementById('datepickerfrom').value = "";
        document.getElementById('datepickerTo').value = "";
        document.getElementById('reportrange').value = "";
        document.getElementById('docSen').value = "";
        //  document.getElementById('invSenderName').value = "-1";
        document.getElementById('docRec').value = "";
        //document.getElementById('invReceiverName').value = "-1";
        //document.getElementById('docIsa').value="";
        document.getElementById('docSenderId').value = ""
        document.getElementById('docBusId').value = "";
        document.getElementById('corrattribute').value = "-1";
        document.getElementById('corrvalue').value = "";
        document.getElementById('corrattribute1').value = "-1";
        document.getElementById('corrvalue1').value = "";
        document.getElementById('docType').value = "-1";
        document.getElementById('status').value = "-1";
        document.getElementById('reportrange').value = "";
        var elements = document.getElementsByName('database');
        elements[0].checked = true;
        $('#gridDiv').hide();
        $('#corr').hide();
        count = 0;

    }
    $("#addButton").click(function () {
        count++;
        if (count == 1) {
            document.getElementById("corr").style.display = "block";
        } else {
            if (document.getElementById("corr").style.display == "none") {
                count = 0;
            } else {
                count = 1;
                alert('Limit exceeded.... cannot add more fields !!');
            }
        }
    })
    $("#removeButton1").click(function () {
        document.getElementById('corrattribute1').value = "-1";
        document.getElementById('corrvalue1').value = "";
        document.getElementById("corr").style.display = "none";
        count = count - parseInt("1");
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
