<%@page import="java.util.Map"%>
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page buffer="50kb" autoFlush="true" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<%@page import="com.mss.ediscv.inv.InvoiceBean"%>
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
        <style>
            #inv_buttons{
                display: inline-block;
                float: right;
            }

        </style>
        <script>
            function doOnLoad() {
                $("#invoices").addClass("active");
                $("#financials").addClass("active");
                $("#manufacturing").addClass("active");
                $("#invoices i").addClass("text-red");
                document.getElementById('loadingAcoountSearch').style.display = "none";
            }
            function hide() {
                $('#hide-menu1').removeClass('show-menu');
            }
            //        $('body,html').click(function (e) {
            //            $('#hide-menu1').removeClass('show-menu');
            //        });
        </script>

        <meta charset="utf-8">

        <title>Miracle Supply Chain Visibility Portal</title>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
        <meta http-equiv="pragma" content="no-cache" />
        <meta http-equiv="cache-control" content="no-cache" />
        <!-- Tell the browser to be responsive to screen width -->
        <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
        <!-- Bootstrap 3.3.5 -->
        <link rel="stylesheet" href='<s:url value="/includes/bootstrap/css/userdefined.css"/>'>
        <link rel="stylesheet" href='<s:url value="/includes/bootstrap/css/bootstrap.min.css"/>'>
        <link rel="stylesheet" href='<s:url value="/includes/plugins/datatables/dataTables.bootstrap.css"/>' type="text/css">
        <link rel="stylesheet" href='<s:url value="/includes/plugins/daterangepicker/daterangepicker.css"/>' type="text/css">
        <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>
        <script language="JavaScript"  src='<s:url value="/includes/js/DateValidation.js"/>'></script>
        <script language="JavaScript"  src='<s:url value="/includes/js/generalValidations.js"/>'></script>
        <script language="JavaScript"  src='<s:url value="/includes/js/GeneralAjax.js"/>'></script>
        <script language="JavaScript"  src='<s:url value="/includes/js/downloadAjax.js"/>'></script>
        <script type="text/javascript">
            $(function () {
                //$("#example1").DataTable();
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

    <body class="hold-transition skin-blue sidebar-mini" onload="doOnLoad();
            check();">
        <script type="text/javascript" src='<s:url value="/includes/js/wz_tooltip.js"/>'></script>
        <script type="text/javascript">
        function check() {
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
        </script>

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
                    Invoice
                    <!--                <small>Manufacturing</small>-->
                </h1>
                <!--            <ol class="breadcrumb">
                                <li><a href="#"><i class="fa fa-wrench"></i>Manufacturing</a></li>
                                <li class="active">Invoice</li>
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
                                <% String contextPath = request.getContextPath(); %>
                                <s:form action="../inv/invoiceSearch.action" method="post" name="invoiceForm" id="invoiceForm" theme="simple">
                                    <div class="form-group">
                                        <div class="row">
                                            <div class="col-sm-12">
                                                <div class="row">
                                                    <div class="col-sm-3"><label>Database&nbsp;&nbsp;&nbsp;&nbsp;:&nbsp;&nbsp;</label>
                                                        <s:radio cssClass="myRadio" id="database" name="database" value="%{database}" list="#@java.util.LinkedHashMap@{'MSCVP':'LIVE','ARCHIVE':'ARCHIVE'}"/>
                                                    </div>
                                                </div>
                                                <div class="row">
                                                    <div class="col-sm-3"> <label for="reportrange">Date Range</label>
                                                        <s:textfield  id="reportrange" name="reportrange" cssClass="form-control" class="form-control pull-left"  value="%{reportrange}" onchange="MyDate();" tabindex="1"/>  
                                                    </div>
                                                    <script type="text/javascript">
                                                        function MyDate() {
                                                            var date = document.invoiceForm.reportrange.value;
                                                            var arr = date.split("-");
                                                            var x = arr[1].trim();
                                                            document.getElementById("invdatepickerfrom").value = arr[0];
                                                            document.getElementById("invdatepicker").value = x;
                                                            var datefrom = document.getElementById("invdatepickerfrom").value;
                                                            var dateto = document.getElementById("invdatepicker").value;
                                                        }
                                                    </script>
                                                    <s:hidden id="invdatepickerfrom" name="invdatepickerfrom" />
                                                    <s:hidden id="invdatepicker" name="invdatepicker"/>
                                                    <div  class="col-sm-3">
                                                        <label for="docType">Document Type</label> 
                                                        <s:select headerKey="-1" cssClass="form-control" headerValue="Select Type" list="{'810'}" name="docType" id="docType" value="%{docType}" tabindex="2"/>
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
                                                    <div class="col-sm-3">
                                                        <label>Receiver </label>
                                                        <s:textfield onchange="setReceiverId();"  cssClass="form-control" list="receiverIdMap" name="docRec" id="docRec" value="%{invBusId}" tabindex="6"/>
                                                        <%Map<String, String> receiverIdMap = (Map) session.getAttribute(AppConstants.RECEIVERSId_MAP);

                                                        %>
                                                        <datalist id="receiverIdMap">
                                                            <% for (Map.Entry<String, String> entry : receiverIdMap.entrySet()) {%>
                                                            <option value="<%=entry.getValue()%>"/>
                                                            <%}%>
                                                        </datalist> 
                                                        <s:hidden name="invBusId" id="invBusId" />
                                                    </div>

                                                </div>

                                                <div class="row">
                                                    <div class="col-sm-3">
                                                        <label for="corrattribute">Correlation</label>
                                                        <s:select headerKey="-1" headerValue="Select Attribute" cssClass="form-control" list="correlationList" name="corrattribute" id="corrattribute" value="%{corrattribute}" tabindex="9" />
                                                    </div>
                                                    <div class="col-sm-3">
                                                        <label for="corrvalue">Value</label>
                                                        <s:textfield cssClass="form-control" name="corrvalue" id="corrvalue" value="%{corrvalue}" tabindex="10"/>
                                                    </div>
                                                    <div id="loadingAcoountSearch" class="loadingImg">
                                                        <span id ="LoadingContent" > <img src="<s:url value="/includes/images/Loader2.gif"/>"   ></span>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                        <div>

                                            <div class="row">
                                                <div class="col-sm-2"><s:submit value="Search"  onclick="return checkCorrelation();" cssClass="btn btn-primary col-sm-12" tabindex="11"/></div>
                                                <div class="col-sm-2"><strong><input type="button" value="Reset"   class="btn btn-primary col-sm-12" onclick="return resetvaluesInvoice();" tabindex="12"/></strong></div>
                                                        <s:hidden name="sampleValue" id="sampleValue" value="2"/>
                                                    </s:form>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        </section>

                        <div id="gridDiv">
                            <s:if test="#session.invoiceList != null"> 
                                <%--- GRid start --%><section class="content">
                                    <div class="row">
                                        <div class="col-xs-12">
                                            <div class="box">
                                                <!--                                                <div class="box-header">
                                                                                                    <h3 class="box-title">Table</h3>
                                                                                                </div>-->
                                                <div class="box-body">
                                                    <%!String cssValue = "whiteStripe";
                                                        int resultsetTotal;%>
                                                    <div style="overflow-x:auto;">                 

                                                        <table align="left" width="100%"
                                                               border="0" cellpadding="0" cellspacing="0">
                                                            <tr>
                                                                <td style="background-color: white;">

                                                                    <table id="results" class="table table-bordered table-hover">
                                                                        <%
                                                                            java.util.List list = (java.util.List) session.getAttribute(AppConstants.SES_INV_LIST);

                                                                            if (list.size() != 0) {
                                                                                InvoiceBean invoiceBean;
                                                                        %>
                                                                        <input type="hidden" name="sec_invoice_list" id="sec_invoice_list" value="<%=list.size()%>"/>
                                                                        <thead>  <tr>
                                                                                <th>DateTime</th>
                                                                                <th>InstanceId</th> 
                                                                                <th >Invoice #</th>
                                                                                <th >Partner</th>
                                                                                <th >PO #</th>
                                                                                <th >Item&nbsp;Qty</th>
                                                                                <th>Invoice&nbsp;Amount</th>
                                                                                <th>Status</th>
                                                                                <th>Ack&nbsp;Status</th>
                                                                            </tr></thead>
                                                                        <tr>

                                                                            <%
                                                                                for (int i = 0; i < list.size(); i++) {
                                                                                    invoiceBean = (InvoiceBean) list.get(i);

                                                                                    if (i % 2 == 0) {
                                                                                        cssValue = "whiteStripe";
                                                                                    } else {
                                                                                        cssValue = "grayEditSelection";
                                                                                    }
                                                                            %>
                                                                            <td>
                                                                                <%
                                                                                    if (invoiceBean.getDate_time_rec().toString().substring(0, invoiceBean.getDate_time_rec().toString().lastIndexOf(":")) != null
                                                                                            && !"".equals(invoiceBean.getDate_time_rec().toString().substring(0, invoiceBean.getDate_time_rec().toString().lastIndexOf(":")))) {
                                                                                        out.println(invoiceBean.getDate_time_rec().toString().substring(0, invoiceBean.getDate_time_rec().toString().lastIndexOf(":")));
                                                                                    } else {
                                                                                        out.println("-");
                                                                                    }
                                                                                %>
                                                                            </td>
                                                                            <td>
                                                                                <%
                                                                                    if (invoiceBean.getFileId() != null && !"".equals(invoiceBean.getFileId())) {
                                                                                        out.println(invoiceBean.getFileId());
                                                                                    } else {
                                                                                        out.println("-");
                                                                                    }
                                                                                %>
                                                                                <input type="hidden" name="Instance<%=i%>" id="Instance<%=i%>" value="<%=invoiceBean.getFileId()%>"/>
                                                                            </td>
                                                                            <td>  <a style="color: deepskyblue" href="javascript:getInvDetails('<%=invoiceBean.getInvNumber()%>','<%=invoiceBean.getPoNumber()%>','<%=invoiceBean.getFileId()%>');"  >
                                                                                    <%
                                                                                        if (invoiceBean.getInvNumber() != null && !"".equals(invoiceBean.getInvNumber())) {
                                                                                            out.println(invoiceBean.getInvNumber());
                                                                                        } else {
                                                                                            out.println("-");
                                                                                        }
                                                                                    %>
                                                                                    <input type="hidden" name="text<%=i%>" id="text<%=i%>" value="<%=invoiceBean.getInvNumber()%>"/>
                                                                                </a>
                                                                            </td>
                                                                            <td>
                                                                                <%
                                                                                    if (invoiceBean.getPname() != null && !"".equals(invoiceBean.getPname())) {
                                                                                        out.println(invoiceBean.getPname());
                                                                                    } else {
                                                                                        out.println("-");
                                                                                    }
                                                                                %>
                                                                            </td>
                                                                            <td>
                                                                                <%
                                                                                    if (invoiceBean.getPoNumber() != null && !"".equals(invoiceBean.getPoNumber())) {
                                                                                        out.println(invoiceBean.getPoNumber());
                                                                                    } else {
                                                                                        out.println("-");
                                                                                    }
                                                                                %>
                                                                            </td>
                                                                            <td>
                                                                                <%
                                                                                    if (invoiceBean.getItemQty() != null && !"".equals(invoiceBean.getItemQty())) {
                                                                                        out.println(invoiceBean.getItemQty());
                                                                                    } else {
                                                                                        out.println("-");
                                                                                    }
                                                                                %>

                                                                            </td>
                                                                            <td>
                                                                                <%
                                                                                    if (invoiceBean.getInvAmount() != null && !"".equals(invoiceBean.getInvAmount())) {
                                                                                        out.println("$" + invoiceBean.getInvAmount());
                                                                                    } else {
                                                                                        out.println("-");
                                                                                    }
                                                                                %>
                                                                            </td>
                                                                            <td>
                                                                                <%
                                                                                    // out.println(invoiceBean.getStatus());
                                                                                    if (invoiceBean.getStatus().equalsIgnoreCase("ERROR")) {
                                                                                        out.println("<font color='red'>" + invoiceBean.getStatus().toUpperCase() + "</font>");
                                                                                    } else if (invoiceBean.getStatus().equalsIgnoreCase("SUCCESS")) {
                                                                                        out.println("<font color='green'>" + invoiceBean.getStatus().toUpperCase() + "</font>");
                                                                                    } else {
                                                                                        out.println("<font color='orange'>" + invoiceBean.getStatus().toUpperCase() + "</font>");
                                                                                    }
                                                                                %>
                                                                            </td>
                                                                            <td>
                                                                                <%
                                                                                    if (invoiceBean.getAckStatus().equalsIgnoreCase("REJECT")) {
                                                                                        out.println("<font color='red'>" + invoiceBean.getAckStatus().toUpperCase() + "</font>");
                                                                                    } else if (invoiceBean.getAckStatus().equalsIgnoreCase("ACCEPTED")) {
                                                                                        out.println("<font color='green'>" + invoiceBean.getAckStatus().toUpperCase() + "</font>");
                                                                                    } else {
                                                                                        out.println("<font color='orange'>" + invoiceBean.getAckStatus().toUpperCase() + "</font>");
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
                                                                                        out.println("<img  border='0' align='top'  src='" + contextPath + "/includes/images/alert.gif'/><b>No records found for the given search criteria. Please try a different search criteria!</b>");
                                                                                    }

                                                                                %>
                                                                            </td>
                                                                        </tr>
                                                                    </table>
                                                                </td>
                                                            </tr>
                                                            <%                                                            if (list.size() != 0) {
                                                            %>
                                                            <tr >
                                                                <!--                                                            <td align="right" colspan="28" style="background-color: white;">-->
                                                                <!--                                                                <div align="right" id="pageNavPosition"></div>-->
                                                                <!--                                                            </td>-->
                                                            </tr>   
                                                            <% }%></tbody>
                                                        </table>
                                                    </div>
                                                    <%-- Grid End --%>
                                                    <%-- Process butttons  start --%>
                                                    <%
                                                        if (list.size() != 0) {
                                                    %>
                                                    <div class="row gnexcel-space">
                                                        <div id="inv_buttons">
                                                            <div class="col-sm-2"><input type="button" value="Generate Excel" class="btn btn-effect-ripple btn-primary" onclick="return gridDownload('invoice', 'xls');" onmouseover="Tip('Click here to generate an excel Report.')" onmouseout="UnTip()" id="excel"/></div>
                                                        </div> 
                                                    </div>
                                                    <%}%>
                                                    <%-- process buttons end--%> 
                                                </div> </div>    </div>    </div>   
                                </section>
                            </s:if> 
                        </div>

                        <div id="hide-menu1" class="hide-menu message ">

                            <div class="row col-sm-12">
                                <br>
                                <div class="col-sm-6"> <label class="labelw"> Instance Id </label>
                                    <s:textfield cssClass="form-control"  required="required" placeholder="" id="mifileID" name="mifileID" readonly="true"/>
                                </div>
                                <div class="col-sm-6"> <label class="labelw"> Invoice # </label>
                                    <s:textfield cssClass="form-control"  required="required" placeholder="" id="miinvNum" name="miinvNum" readonly="true"/>
                                </div>
                            </div>
                            <div class="row col-sm-12">
                                <div class="col-sm-6"> <label class="labelw"> PO # </label>
                                    <s:textfield cssClass="form-control"  required="required" placeholder="" id="mipoNum" name="mipoNum" readonly="true"/>
                                </div>
                                <div class="col-sm-6"> <label class="labelw"> Invoice Amount </label>
                                    <s:textfield cssClass="form-control"  required="required" placeholder="" id="miinvAmt" name="miinvAmt" readonly="true"/>
                                </div>
                            </div>
                            <div class="row col-sm-12">
                                <div class="col-sm-6"> <label class="labelw"> Invoice Date </label>
                                    <s:textfield cssClass="form-control"  required="required" placeholder="" id="miinvDate" name="miinvDate" readonly="true"/>
                                </div>
                                <div class="col-sm-6"> <label class="labelw"> Item Qty </label>
                                    <s:textfield cssClass="form-control"  required="required" placeholder="" id="miitemQty" name="miitemQty" readonly="true"/>
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
                                    <div class="col-sm-6"> <label class="labelw"> Id </label>
                                        <s:textfield cssClass="form-control"  required="required" placeholder="" id="misenderId" name="misenderId" readonly="true"/>
                                    </div>
                                    <div class="col-sm-6"> <label class="labelw"> Name </label>
                                        <s:textfield cssClass="form-control"  required="required" placeholder="" id="misenderName" name="misenderName" readonly="true"/>
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
                                        <s:textfield cssClass="form-control"  required="required" placeholder="" id="mireceiverId" name="mireceiverId" readonly="true"/>
                                    </div>
                                    <div class="col-sm-6"> <label class="labelw"> Name </label>
                                        <s:textfield cssClass="form-control"  required="required" placeholder="" id="mireceiverName" name="mireceiverName" readonly="true"/>
                                    </div>
                                </div>
                            </div>
                            <div class="row col-sm-12">
                                <br>
                                <div class="col-sm-6"> <label class="labelw"> ISA # </label>
                                    <s:textfield cssClass="form-control"  required="required" placeholder="" id="miisaNum" name="miisaNum" readonly="true"/>
                                </div>
                                <div class="col-sm-6"> <label class="labelw"> GS # </label>
                                    <s:textfield cssClass="form-control"  required="required" placeholder="" id="migsControlNo" name="migsControlNo" readonly="true"/>
                                </div>
                            </div>
                            <div class="row col-sm-12">
                                <div class="col-sm-6"> <label class="labelw"> ST # </label>
                                    <s:textfield cssClass="form-control"  required="required" placeholder="" id="mistControlNo" name="mistControlNo" readonly="true"/>
                                </div>
                                <div class="col-sm-6"> <label class="labelw"> Status </label>
                                    <s:textfield cssClass="form-control"  required="required" placeholder="" id="mistatus" name="mistatus" readonly="true"/>
                                </div>
                            </div>
                            <div class="row col-sm-12">
                                <div class="col-sm-6"> <label class="labelw"> ISA Date </label>
                                    <s:textfield cssClass="form-control"  required="required" placeholder="" id="miisaDate" name="miisaDate" readonly="true"/>
                                </div>
                                <div class="col-sm-6"> <label class="labelw"> ISA TIme </label>
                                    <s:textfield cssClass="form-control"  required="required" placeholder="" id="miisaTime" name="miisaTime" readonly="true"/>
                                </div>
                            </div>
                            <div class="row col-sm-12">
                                <div class="col-sm-6"> <label class="labelw"> Transaction Type </label>
                                    <s:textfield cssClass="form-control"  required="required" placeholder="" id="mitransType" name="mitransType" readonly="true"/>
                                </div>
                                <div class="col-sm-6"> <label class="labelw"> Doc Type </label>
                                    <s:textfield cssClass="form-control"  required="required" placeholder="" id="mifileType" name="mifileType" readonly="true"/>
                                </div>
                            </div>
                            <%-- <div id="sapDiv" style="display: none">
                                 <div class="row col-sm-12">
                                     <div class="col-sm-6"> <label class="labelw"> SAP User </label>
                                         <s:textfield cssClass="form-control"  required="required" placeholder="" id="misapUser" name="misapUser" readonly="true"/>
                                     </div>
                                     <div class="col-sm-6"> <label class="labelw"> iDoc # </label>
                                         <s:textfield cssClass="form-control"  required="required" placeholder="" id="miidocNo" name="miidocNo" readonly="true"/>
                                     </div>
                                 </div>
                                 <div class="row col-sm-12">
                                     <div class="col-sm-6"> <label class="labelw"> PO # </label>
                                         <s:textfield cssClass="form-control"  required="required" placeholder="" id="mipoNo" name="mipoNo" readonly="true"/>
                                     </div>
                                     <div class="col-sm-6"> <label class="labelw"> PO Date </label>
                                         <s:textfield cssClass="form-control"  required="required" placeholder="" id="mipoDate" name="mipoDate" readonly="true"/>
                                     </div>
                                 </div>
                                 <div class="row col-sm-12">
                                     <div class="col-sm-6"> <label labelw> iDoc Status Code </label>
                                         <s:textfield cssClass="form-control"  required="required" placeholder="" id="miidocStatusCode" name="miidocStatusCode" readonly="true"/>
                                     </div>
                                     <div class="col-sm-6"> <label labelw> iDoc Status Desc </label>
                                         <input type="Text"  class="form-control"  required="required" placeholder="" id="miidocStatusDesc" name="miidocStatusDesc" readonly="true"/>
                                     </div>
                                 </div>
                             </div> --%>
                            <div class="row col-sm-12" style="margin-top:10px;">
                                <div class="col-sm-6"> <label class="labelw"> Pre-Translation </label></div>
                                <div class="col-sm-6" id="mipreTransFilepath"></div>
                            </div>
                            <div class="row col-sm-12">
                                <div class="col-sm-6"> <label class="labelw"> Post-Translation </label></div>
                                <div class="col-sm-6" id="mipostTransFilepath"></div>
                            </div>
                            <div class="row col-sm-12">
                                <div class="col-sm-6"> <label class="labelw"> 997&nbsp;Ack&nbsp;File </label></div>
                                <div class="col-sm-6" id="miackFileId"></div>
                            </div>
                            <div class="row col-sm-12" id="errorDiv" style="display: none">
                                <div class="col-sm-6"> <label class="labelw"> Error&nbsp;Message </label></div>
                                <div class="col-sm-6" id="mierrormessage" style="color: red"></div>
                            </div>
                            <div class="row col-sm-12" id="errorReportDiv" style="display: none">
                                <div class="col-sm-6"> <label class="labelw">  Error&nbsp;Report </label></div>
                                <div class="col-sm-6"><div id="ErrReport"></div></div>
                            </div>
                            <div id="noresult"></div>
                            <br>
                            <div class="row col-sm-12" style="margin-top:10px;"> <button type="button" class="btn btn-primary col-sm-11" style="margin-left:12px; " id="hide-menu" onclick="hide()" value="X">Close</button></div>
                        </div>
                    </div>

                    <div>
                        <s:include value="../includes/template/footer.jsp"/>
                    </div>
                    <script>
                        $('input[name="daterange"]').daterangepicker();
                    </script>

                    <script src='<s:url value="/includes/bootstrap/js/bootstrap.min.js"/>'></script>
                    <script src='<s:url value="/includes/plugins/daterangepicker/daterangepicker.js"/>'></script>
                    <script src='<s:url value="/includes/bootstrap/js/app.min.js"/>'></script>
                    <script src='<s:url value="../includes/plugins/datatables/jquery.dataTables.min.js"/>'></script>
                    <script src='<s:url value="../includes/plugins/datatables/dataTables.bootstrap.min.js"/>'></script>
                    <script type="text/javascript">

                        function checkCorrelation() {
                            var db = document.forms["invoiceForm"]["database"].value;
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
                        function resetvaluesInvoice()
                        {
                            $('#hide-menu1').removeClass('show-menu');
                            // $('.myRadio').attr('checked', false);
                            document.getElementById('reportrange').value = '';
                            document.getElementById('invdatepickerfrom').value = '';

                            document.getElementById('invdatepicker').value = '';

                            document.getElementById('docSen').value = "";

                            //    document.getElementById('invSenderName').value = "-1";

                            document.getElementById('docRec').value = "";
                            document.getElementById('invSenderId').value = ""
                            document.getElementById('invBusId').value = "";

                            //     document.getElementById('invRecName').value = "-1";
                            document.getElementById('corrattribute').value = "-1";
                            document.getElementById('corrvalue').value = "";

                            document.getElementById('sampleValue').value = "1";
                            document.getElementById('ackStatus').value = "-1";
                            document.getElementById('status').value = "-1";
                            document.getElementById('docType').value = "-1";
                            var elements = document.getElementsByName('database');
                            elements[0].checked = true;
                            $('#gridDiv').hide();

                        }


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
