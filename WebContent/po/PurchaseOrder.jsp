<%@page import="java.util.Map"%>
<%@page import="com.mss.ediscv.po.PurchaseOrderBean"%>
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
        <link rel="stylesheet" href='<s:url value="/includes/plugins/datatables/dataTables.bootstrap.css"/>' type="text/css">
        <link rel="stylesheet" href='<s:url value="/includes/plugins/daterangepicker/daterangepicker.css"/>'>
        <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>
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
            #buttons{
                display: inline-block;
                float: right;
            }
        </style>
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

    <body class="hold-transition skin-blue sidebar-mini" onload="check();
            doOnLoad()">
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
                <h1>Purchase Order
                    <!--                    <small>Manufacturing</small>-->
                </h1>
                <!--                <ol class="breadcrumb">
                                    <li><a href="#"><i class="fa fa-wrench"></i>Manufacturing</a></li>
                                    <li class="active">Purchase Order</li>
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
                                <%String contextPath = request.getContextPath(); %>
                                <s:form action="../po/poSearch.action" method="post" name="purchaseForm" id="purchaseForm" theme="simple">
                                    <s:hidden id="poDateFrom" name="poDateFrom" />
                                    <s:hidden id="poDateTo" name="poDateTo"/>
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
                                                        <s:textfield name="reportrange"  id="reportrange" cssClass="form-control pull-left"   value="%{reportrange}" onchange="Date1();" tabindex="1"/> 
                                                    </div>
                                                    <script type="text/javascript">
                                                        function Date1() {
                                                            var date = document.purchaseForm.reportrange.value;
                                                            var arr = date.split("-");
                                                            var x = arr[1].trim();
                                                            document.getElementById("poDateFrom").value = arr[0];
                                                            document.getElementById("poDateTo").value = x;
                                                        }
                                                    </script>
                                                    <div  class="col-sm-3">
                                                        <label for="docType">Document Type</label> 
                                                        <s:select headerKey="-1" cssClass="form-control" headerValue="Select Type" list="{'850','855'}" name="docType" id="docType" value="%{docType}" tabindex="2"/>
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
                                                        <s:textfield onchange="setSenderId();"  cssClass="form-control" list="senderIdMap" name="docSen" id="docSen" value="%{paSenderId}" tabindex="5"/>
                                                        <%Map<String, String> senderIdMap = (Map) session.getAttribute(AppConstants.SENDERSID_MAP);

                                                        %>
                                                        <datalist id="senderIdMap">
                                                            <% for (Map.Entry<String, String> entry : senderIdMap.entrySet()) {%>
                                                            <option value="<%=entry.getValue()%>"/>
                                                            <%}%>
                                                        </datalist> 
                                                        <s:hidden name="paSenderId" id="paSenderId" />
                                                    </div>
                                                    <div class="col-sm-3">
                                                        <label>Receiver </label>
                                                        <s:textfield onchange="setReceiverId();"  cssClass="form-control" list="receiverIdMap" name="docRec" id="docRec" value="%{paRecId}" tabindex="6"/>
                                                        <%Map<String, String> receiverIdMap = (Map) session.getAttribute(AppConstants.RECEIVERSId_MAP);

                                                        %>
                                                        <datalist id="receiverIdMap">
                                                            <% for (Map.Entry<String, String> entry : receiverIdMap.entrySet()) {%>
                                                            <option value="<%=entry.getValue()%>"/>
                                                            <%}%>
                                                        </datalist> 
                                                        <s:hidden name="paRecId" id="paRecId" />
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
                                                    <div class="col-sm-3"><br>
                                                        <button  type="button" id="addButton" name="addButton" value="Add Div" class="btn btn-success"   style="margin-top:6px ;"tabindex="11" ><i class="fa fa-plus"></i></button>
                                                        &nbsp; <label>Add Filter</label>
                                                    </div>
                                                    <div id="loadingAcoountSearch" class="loadingImg">
                                                        <span id ="LoadingContent" > <img src="<s:url value="/includes/images/Loader2.gif"/>"   ></span>
                                                    </div>
                                                </div>
                                                <script>
                                                    var count = 0;

                                                </script>
                                                <div id="corr" style="display: none">
                                                    <div class="row">
                                                        <div class="col-sm-3">
                                                            <label for="corrattribute1">Correlation</label>
                                                            <s:select headerKey="-1" headerValue="Select Attribute" cssClass="form-control" list="correlationList" name="corrattribute1" id="corrattribute1" value="%{corrattribute1}" tabindex="12"/>
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
                                                            <s:select headerKey="-1" headerValue="Select Attribute" cssClass="form-control" list="correlationList" name="corrattribute2" id="corrattribute2" value="%{corrattribute2}" tabindex="14" />
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

                                        <div class="row">
                                            <div class="col-sm-2"><s:submit value="Search"  onclick="return checkCorrelation();"   cssClass="btn btn-primary col-sm-12" tabindex="16"/></div>
                                            <div class="col-sm-2"><strong><input type="button" value="Reset"   class="btn btn-primary col-sm-12" onclick="return resetvaluesPO();" tabindex="17"/></strong></div>
                                                    <s:hidden name="sampleValue" id="sampleValue" value="2"/>
                                                </s:form>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
            </section>
            <div id="gridDiv">  
                <s:if test="#session.poSearchList != null"> 
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
                                            <table align="left" width="100%" border="0" cellpadding="0" cellspacing="0">
                                                <tr>
                                                    <td style="background-color: white;">
                                                        <table  id="results" class="table table-bordered table-hover">
                                                            <%
                                                                java.util.List list = (java.util.List) session.getAttribute(AppConstants.SES_PO_LIST);
                                                                System.out.println("list.size()-->" + list.size());
                                                                if (list.size() != 0) {
                                                                    PurchaseOrderBean purchaseOrderBean;
                                                            %>
                                                            <input type="hidden" name="sec_po_list" id="sec_po_list" value="<%=list.size()%>"/> 
                                                            <thead><tr>
                                                                    <th>DateTime</th> 
                                                                    <th >InstanceId</th>
                                                                    <th >PO #</th>
                                                                    <th>Transaction Type</th>
                                                                    <th>Partner</th>
                                                                    <th>Direction</th>
                                                                    <th>Status</th> 
                                                                    <th>Ack&nbsp;Status</th>  
                                                                    <th>Reprocess</th> 
                                                                    <td>#</td>
                                                                </tr> </thead>
                                                            <tbody>
                                                                <%
                                                                    for (int i = 0; i < list.size(); i++) {
                                                                        purchaseOrderBean = (PurchaseOrderBean) list.get(i);
                                                                        if (i % 2 == 0) {
                                                                            cssValue = "whiteStripe";
                                                                        } else {
                                                                            cssValue = "grayEditSelection";
                                                                        }
                                                                %>
                                                                <tr>
                                                                    <td>
                                                                        <%
                                                                            if (purchaseOrderBean.getDate_time_rec().toString().substring(0, purchaseOrderBean.getDate_time_rec().toString().lastIndexOf(":")) != null
                                                                                    && !"".equals(purchaseOrderBean.getDate_time_rec().toString().substring(0, purchaseOrderBean.getDate_time_rec().toString().lastIndexOf(":")))) {
                                                                                out.println(purchaseOrderBean.getDate_time_rec().toString().substring(0, purchaseOrderBean.getDate_time_rec().toString().lastIndexOf(":")));
                                                                            } else {
                                                                                out.println("-");
                                                                            }
                                                                        %>
                                                                    </td>  
                                                                    <td>
                                                                        <%
                                                                            if (purchaseOrderBean.getFileId() != null && !"".equals(purchaseOrderBean.getFileId())) {
                                                                                out.println(purchaseOrderBean.getFileId());
                                                                            } else {
                                                                                out.println("-");
                                                                            }
                                                                        %>
                                                                        <input type="hidden" name="Instance<%=i%>" id="Instance<%=i%>" value="<%=purchaseOrderBean.getFileId()%>"/>   
                                                                    </td>
                                                                    <td><a style="color: deepskyblue" href="javascript:getDetails('<%=purchaseOrderBean.getPo()%>','<%=purchaseOrderBean.getFileId()%>');"  >
                                                                            <%
                                                                                if (purchaseOrderBean.getPo() != null && !"".equals(purchaseOrderBean.getPo())) {
                                                                                    out.println(purchaseOrderBean.getPo());
                                                                                } else {
                                                                                    out.println("-");
                                                                                }
                                                                            %> 
                                                                            <input type="hidden" name="text<%=i%>" id="text<%=i%>" value="<%=purchaseOrderBean.getPo()%>"/>
                                                                        </a>
                                                                    </td>
                                                                    <td>
                                                                        <%
                                                                            if (purchaseOrderBean.getTransactionType() != null && !"".equals(purchaseOrderBean.getTransactionType())) {
                                                                                out.println(purchaseOrderBean.getTransactionType());
                                                                            } else {
                                                                                out.println("-");
                                                                            }
                                                                        %>
                                                                    </td>
                                                                    <td>
                                                                        <%
                                                                            if (purchaseOrderBean.getPname() != null && !"".equals(purchaseOrderBean.getPname())) {
                                                                                out.println(purchaseOrderBean.getPname());
                                                                            } else {
                                                                                out.println("-");
                                                                            }
                                                                        %>
                                                                    </td>
                                                                    <td>
                                                                        <%
                                                                            if (purchaseOrderBean.getDirection() != null && !"".equals(purchaseOrderBean.getDirection())) {
                                                                                out.println(purchaseOrderBean.getDirection().toUpperCase());
                                                                            } else {
                                                                                out.println("-");
                                                                            }
                                                                        %>
                                                                    </td>
                                                                    <td>
                                                                        <%
                                                                            if (purchaseOrderBean.getStatus().equalsIgnoreCase("ERROR")) {
                                                                                out.println("<font color='red'>" + purchaseOrderBean.getStatus().toUpperCase() + "</font>");
                                                                            } else if (purchaseOrderBean.getStatus().equalsIgnoreCase("SUCCESS")) {
                                                                                out.println("<font color='green'>" + purchaseOrderBean.getStatus().toUpperCase() + "</font>");
                                                                            } else {
                                                                                out.println("<font color='orange'>" + purchaseOrderBean.getStatus().toUpperCase() + "</font>");
                                                                            }
                                                                        %>
                                                                    </td>  
                                                                    <td>
                                                                        <%
                                                                            if (purchaseOrderBean.getAckStatus().equalsIgnoreCase("REJECTED")) {
                                                                                out.println("<font color='red'>" + purchaseOrderBean.getAckStatus().toUpperCase() + "</font>");
                                                                            } else if (purchaseOrderBean.getAckStatus().equalsIgnoreCase("ACCEPTED")) {
                                                                                out.println("<font color='green'>" + purchaseOrderBean.getAckStatus().toUpperCase() + "</font>");
                                                                            } else {
                                                                                out.println("<font color='orange'>" + purchaseOrderBean.getAckStatus().toUpperCase() + "</font>");
                                                                            }
                                                                        %>
                                                                    </td>  
                                                                    <td>
                                                                        <%                                                                            //out.println(purchaseOrderBean.getReProcessStatus());
                                                                            if (purchaseOrderBean.getReProcessStatus() != null && !"".equals(purchaseOrderBean.getReProcessStatus())) {
                                                                                out.println(purchaseOrderBean.getReProcessStatus().toUpperCase());

                                                                            } else {
                                                                                out.println("-");
                                                                            }

                                                                        %>
                                                                    </td>
                                                                    <td> &nbsp; &nbsp; 
                                                                        <input type = "checkbox" name ="check_List<%=i%>" id = "check_List<%=i%>" value="<%= purchaseOrderBean.getPo()%>"/>&nbsp; &nbsp;  
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
                                                <% if (list.size() != 0) { %>
                                                <tr >
                                                    <td align="right" colspan="28" style="background-color: white;">
                                                        <div align="right" id="pageNavPosition"></div>
                                                    </td>
                                                </tr>
                                                <% }%>  
                                                </tbody>
                                            </table>
                                        </div>
                                    </div>
                                    <%-- Process butttons  start --%>
                                    <% if (list.size() != 0) { %>
                                    <div class="row gnexcel-space">
                                        <div id="buttons">
                                            <%--<div class="col-sm-2" style="margin-right:10%"><input type="button" value="ReTransmit" class="btn btn-effect-ripple btn-primary" onmouseover="Tip('Click here to ReTransmit.')" onmouseout="UnTip()" onclick="return getProces(this, document.getElementById('sec_po_list').value);" id="pre"/></div>--%>
<!--                                            <div class="col-sm-2" style="margin-right:6%"><input type="button" value="ReSubmit" class="btn btn-effect-ripple btn-primary" onmouseover="Tip('Click here to Resubmit.')" onmouseout="UnTip()" onclick="return getProcess(this, document.getElementById('sec_po_list').value, document.forms['purchaseForm']['database'].value);" id="post"/></div>-->
                                            <div class="col-sm-2" style="margin-right:5%"><input type="button" value="LifeCycle" class="btn btn-effect-ripple btn-primary" onmouseover="Tip('Click here to generate Life Cycle.')" onmouseout="UnTip()" onclick="return getLifeCycle(document.getElementById('sec_po_list').value, 'manufacturing', document.forms['purchaseForm']['database'].value);"/></div>
                                            <div class="col-sm-2" style="margin-right:2%"><input type="button" value="Generate Excel" class="btn btn-effect-ripple btn-primary" onclick="return gridDownload('po', 'xls');" onmouseover="Tip('Click here to generate an excel Report.')" onmouseout="UnTip()" id="excel"/> </div>  
                                        </div>
                                    </div>
                                    <%}%>
                                    <%-- process buttons end--%>
                                    <%-- Grid End --%>
                                </div>
                            </div>
                        </div></section>
                </s:if> </div>
            <div id="hide-menu1" class="hide-menu message ">
                <br>
                <div class="row col-sm-12">
                    <div class="col-sm-6"> <label class="labelw">Instance Id </label>
                        <s:textfield  cssClass="form-control"  required="required" placeholder="" id="POInstanceId" name="POInstanceId" readonly="true"/>
                    </div>
                    <div class="col-sm-6"> <label class="labelw">PO # </label>
                        <s:textfield  cssClass="form-control"  required="required" placeholder="" id="PONum" name="PONum" readonly="true"/>
                    </div> 
                </div>
                <div class="row col-sm-12"> 
                    <div class="col-sm-6"> <label class="labelw">Order Date </label>
                        <s:textfield  cssClass="form-control"  required="required" placeholder="" id="PODates" name="PODates" readonly="true"/>
                    </div>
                    <%--<div class="col-sm-6"> <label class="labelw">PO Value :</label>
                        <s:textfield  cssClass="form-control"  required="required" placeholder="" id="POValue" name="POValue" readonly="true"/>
                    </div>
                    <div class="col-sm-6"> <label class="labelw">POQuantity:</label>
                        <s:textfield  cssClass="form-control"  required="required" placeholder="" id="POQty" name="POQty" readonly="true"/>
                    </div>
                    <div class="col-sm-6"> <label class="labelw">SHIP_DATE :</label>
                        <s:textfield  cssClass="form-control"  required="required" placeholder="" id="POShipDate" name="POShipDate" readonly="true"/>
                    </div>
                    <div class="col-sm-6"> <label class="labelw">ROUTINGS :</label>
                        <s:textfield  cssClass="form-control"  required="required" placeholder="" id="PORoutings" name="PORoutings" readonly="true"/>
                    </div>
                    <div class="col-sm-6"> <label class="labelw">INVOICED_AMOUNT</label>
                        <s:textfield  cssClass="form-control"  required="required" placeholder="" id="INVOICED_AMOUNT" name="INVOICED_AMOUNT" readonly="true"/>
                    </div>


                        <div class="col-sm-6"> <label class="labelw">PAYMENTRECEIVED:</label>
                            <s:textfield  cssClass="form-control"  required="required" placeholder="" id="POPaymentReceived" name="POPaymentReceived" readonly="true"/>
                        </div>
                        <div class="col-sm-6"> <label class="labelw">SHIPADDRESSID</label>
                            <s:textfield  cssClass="form-control"  required="required" placeholder="" id="POShipAddrId" name="POShipAddrId" readonly="true"/>
                        </div>
                        <div class="col-sm-6"> <label class="labelw">BILL_ADDRESS_ID:</label>
                            <s:textfield  cssClass="form-control"  required="required" placeholder="" id="POBillAddrId" name="POBillAddrId" readonly="true"/>
                        </div>
                        <div class="col-sm-6"> <label class="labelw">SO&nbsp;# :</label>
                            <s:textfield  cssClass="form-control"  required="required" placeholder="" id="POSoNumber" name="POSoNumber" readonly="true"/>
                        </div> 
                    <div class="col-sm-6"> <label class="labelw">SAPIDOC # </label>
                        <s:textfield  cssClass="form-control"  required="required" placeholder="" id="POSapIdocNum" name="POSapIdocNum" readonly="true"/>
                    </div> --%>
                    <div class="col-sm-6"> <label class="labelw">Doc Format </label>
                        <s:textfield  cssClass="form-control"  required="required" placeholder="" id="PODocumentType" name="PODocumentType" readonly="true"/>
                    </div>
                </div>
                <%--<div class="col-sm-6"> <label class="labelw">IDOC Status :</label>
                    <s:textfield  cssClass="form-control"  required="required" placeholder="" id="PODeilvaryName" name="PODeilvaryName" readonly="true"/>
                </div>--%>

                <div class="row col-sm-12">
                    <div class="col-sm-6"> <label class="labelw">TransactionType</label>
                        <s:textfield  cssClass="form-control"  required="required" placeholder="" id="POTransactionType" name="POTransactionType" readonly="true"/>
                    </div>
                </div> 
                <div class="row col-sm-12">
                    <%--<div class="col-sm-6"> <label class="labelw">ORDER_STATUS</label>
                        <s:textfield  cssClass="form-control"  required="required" placeholder="" id="POOrderStatus" name="POOrderStatus" readonly="true"/>
                    </div>--%>
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
                            <s:textfield  cssClass="form-control"  required="required" placeholder="" id="POSenderId" name="POSenderId" readonly="true"/>
                        </div>
                        <div class="col-sm-6"> <label class="labelw"> Name </label>
                            <s:textfield  cssClass="form-control"  required="required" placeholder="" id="POSenderName" name="POSenderName" readonly="true"/>
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
                            <s:textfield  cssClass="form-control"  required="required" placeholder="" id="POReceiverId" name="POReceiverId" readonly="true"/>
                        </div>
                        <div class="col-sm-6"> <label class="labelw"> Name </label>
                            <s:textfield  cssClass="form-control"  required="required" placeholder="" id="POReceiverName" name="POReceiverName" readonly="true"/>
                        </div>
                    </div>
                </div>
                <div class="row col-sm-12 clear">
                    <br>
                    <div class="col-sm-6"> <label class="labelw">  ISA </label>
                        <s:textfield  cssClass="form-control"  required="required" placeholder="" id="POIsa" name="POIsa" readonly="true"/>
                    </div>
                    <div class="col-sm-6"> <label class="labelw"> GS </label>
                        <s:textfield  cssClass="form-control"  required="required" placeholder="" id="POGs" name="POGs" readonly="true"/>
                    </div>
                </div>
                <div class="row col-sm-12">
                    <div class="col-sm-6"> <label class="labelw">  ST </label>
                        <s:textfield  cssClass="form-control"  required="required" placeholder="" id="POSt" name="POSt" readonly="true"/>
                    </div>
                    <div class="col-sm-6"> <label class="labelw">ISA Date </label>
                        <s:textfield  cssClass="form-control"  required="required" placeholder="" id="POIsADate" name="POIsADate" readonly="true"/>
                    </div>

                    <div class="col-sm-6"> <label class="labelw">  ISA Time  </label>
                        <s:textfield  cssClass="form-control"  required="required" placeholder="" id="POIsATime" name="POIsATime" readonly="true"/>
                    </div>
                    <div class="col-sm-6"> <label class="labelw"> Status </label>
                        <s:textfield  cssClass="form-control"  required="required" placeholder="" id="POStatus" name="POStatus" readonly="true"/>
                    </div>
                </div>
                <div id="sapDeatails" style="display: none;margin-top:10px;">
                    <div class="col-sm-6"> <h4>SAP Details:</h4></div>
                    <div class="row col-sm-12 clear">
                        <%--<div class="col-sm-6"> <label class="labelw"> SAP_USER </label>
                            <s:textfield  cssClass="form-control"  required="required" placeholder="" id="SAP_USER" name="ManStatus" readonly="true"/>
                        </div> --%>
                        <div class="col-sm-6"> <label class="labelw"> PO NUMBER </label>
                            <s:textfield  cssClass="form-control"  required="required" placeholder="" id="PO_NUMBER" name="ManStatus" readonly="true"/>
                        </div>
                        <%--<div class="col-sm-6"> <label class="labelw"> PO_DATE </label>
                            <s:textfield  cssClass="form-control"  required="required" placeholder="" id="PO_DATE" name="ManStatus" readonly="true"/>
                        </div>--%>
                        <div class="col-sm-6"> <label class="labelw"> IDoc CODE </label>
                            <s:textfield  cssClass="form-control"  required="required" placeholder="" id="IDOC_STATUS_CODE" name="ManStatus" readonly="true"/>
                        </div>
                    </div>
                    <div class="row col-sm-12 clear">
                        <div class="col-sm-6"> <label class="labelw"> IDOC NUMBER </label>
                            <s:textfield  cssClass="form-control"  style="width: 253px;" required="required" placeholder="" id="IDOC_NUMBER" name="ManStatus" readonly="true"/>
                        </div>
                    </div>
                    <%--<div class="row col-sm-12 clear">
                        <div class="col-sm-6"> <label class="labelw"> IDOC_DESCRIPTION </label>
                            <s:textfield  cssClass="form-control" style="width: 253px;" required="required" placeholder="" id="IDOC_STATUS_DESCRIPTION" name="ManStatus" readonly="true"/>
                        </div>
                    </div>--%>
                </div>
                <br/>
                <div class="row col-sm-12" style="margin-top:10px;">
                    <div class="col-sm-6"> <label class="labelw">  PreTranslation  </label></div>
                    <div class="col-sm-6"><div id="POPreTransition"></div></div>
                </div>
                <div class="row col-sm-12" >
                    <div class="col-sm-6"> <label class="labelw"> PostTranslation </label></div>
                    <div class="col-sm-6"><div id="POPostTransition"></div></div>
                </div>
                <div class="row col-sm-12" >
                    <div class="col-sm-6"> <label class="labelw">997 ACKFile </label></div>                      
                    <div class="col-sm-6"><div id="POAckFileId"></div></div>
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
                <br>
                <div class="row col-sm-12" style="margin-top:10px;">  <button type="button" class="btn btn-primary col-sm-11" style="margin-left:12px; " id="hide-menu" onclick="hide()" value="X">Close</button></div>
            </div>
        </div>

        <div>
            <s:include value="../includes/template/footer.jsp"/>
        </div>

        <script type="text/javascript" src='<s:url value="../includes/js/DateValidation.js"/>'></script>
        <script type="text/javascript" src='<s:url value="../includes/js/lifeCycle.js"/>'></script>
        <script language="JavaScript"  src='<s:url value="/includes/js/generalValidations.js"/>'></script>
        <script language="JavaScript"  src='<s:url value="/includes/js/GeneralAjax.js"/>'></script>
        <script language="JavaScript"  src='<s:url value="/includes/js/downloadAjax.js"/>'></script>
        <script type="text/javascript" src='<s:url value="../includes/plugins/daterangepicker/daterangepicker.js"/>'></script>
        <script src='<s:url value="/includes/bootstrap/js/app.min.js"/>'></script>
        <script src='<s:url value="../includes/plugins/datatables/jquery.dataTables.min.js"/>'></script>
        <script src='<s:url value="../includes/plugins/datatables/dataTables.bootstrap.min.js"/>'></script>
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

                    // New function to show the left grid
                    function demo() {
                        $(function () {
                            $('#detail_box').show();
                            return false;
                        });
                    }

                    function getDetails(val, val1) {
                        var db = document.forms["purchaseForm"]["database"].value;
                        getPoDetails(val, val1, db);
                    }

                    function resetvaluesPO() {
                        $('#hide-menu1').removeClass('show-menu');
                        //   $('.myRadio').attr('checked', false);
                        document.getElementById('poDateFrom').value = "";
                        document.getElementById('poDateTo').value = "";
                        document.getElementById('reportrange').value = ""
                        document.getElementById('docType').value = "-1";
                        document.getElementById('docSen').value = "";
                        //document.getElementById('poSenderName').value = "-1";
                        document.getElementById('docRec').value = "";
                        //document.getElementById('poRecName').value = "-1";
                        document.getElementById('sampleValue').value = "1";
                        //document.getElementById('ackStatus').value="-1";
                        document.getElementById('paSenderId').value = ""
                        document.getElementById('paRecId').value = "";
                        document.getElementById('ackStatus').value = "-1";
                        document.getElementById('status').value = "-1";
                        document.getElementById('corrattribute').value = "-1";
                        document.getElementById('corrvalue').value = "";
                        document.getElementById('corrattribute1').value = "-1";
                        document.getElementById('corrvalue1').value = "";
                        document.getElementById('corrattribute2').value = "-1";
                        document.getElementById('corrvalue2').value = "";
                        var elements = document.getElementsByName('database');
                        elements[0].checked = true;
                        $('#gridDiv').hide();
                        $('#corr').hide();
                        $('#corr1').hide();
                        count = 1;
                    }

                    function enterDate() {
                        alert("Please select from the Calender !");
                        document.getElementById('poDateFrom').value = '';
                    }

                    function checkCorrelation() {
                        var db = document.forms["purchaseForm"]["database"].value;
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
                        var res = Formvalidation(document.getElementById('poDateFrom').value, document.getElementById('poDateTo').value);
                        return res;
                    }

                    function doOnLoad() {
                        $("#purchaseorder").addClass("active");
                        $("#oredermanagement").addClass("active");

                        $("#manufacturing").addClass("active");

                        $("#purchaseorder i").addClass("text-red");
                        document.getElementById('loadingAcoountSearch').style.display = "none";
                    }

                    function hide() {
                        $('#hide-menu1').removeClass('show-menu');
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
