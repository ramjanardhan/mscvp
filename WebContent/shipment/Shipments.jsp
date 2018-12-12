<%@page import="java.util.Map"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page import="com.mss.ediscv.shipment.ShipmentBean"%>
<%@ taglib uri="/WEB-INF/tlds/dbgrid.tld" prefix="grd"%>
<%@ page import="com.freeware.gridtag.*"%>
<%@ page import="java.sql.Connection" %>
<%@ page import="java.sql.SQLException"%>
<%@ page import="com.mss.ediscv.util.AppConstants" %>
<%@ page import="com.mss.ediscv.util.ConnectionProvider" %>
<%@ page import="com.mss.ediscv.util.ServiceLocatorException" %>
<%@ page import="org.apache.log4j.Logger"%>
<html>
    <head>
        <style>
            #gridButtons{
                display: inline-block;
                float:right;
            }
        </style>
        <meta charset="utf-8">
        <title>Miracle Supply Chain Visibility Portal</title>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
        <meta http-equiv="pragma" content="no-cache" />
        <meta http-equiv="cache-control" content="no-cache" />
        <!-- Tell the browser to be responsive to screen width -->
        <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
        <link rel="stylesheet" href='<s:url value="/includes/bootstrap/css/userdefined.css"/>'>
        <link rel="stylesheet" href='<s:url value="/includes/plugins/datatables/dataTables.bootstrap.css"/>' type="text/css">
        <link rel="stylesheet" href='<s:url value="/includes/plugins/daterangepicker/daterangepicker.css"/>' type="text/css">
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
    <body class="hold-transition skin-blue sidebar-mini" onload= "doOnLoad();
            check();"> 
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
                    Shipments 
                    <!--                    <small>Manufacturing</small>-->
                </h1>
                <!--                <ol class="breadcrumb">
                                    <li><a href="#"><i class="fa fa-wrench"></i>Manufacturing</a></li>
                                    <li class="active">Shipments </li>
                                </ol>-->
            </section>

            <section class="content">
                <div class="box box-primary">
                    <div class="box-header with-border">
                        <div class="box-tools pull-right"></div>
                    </div>  
                    <div class="box-body">
                        <div id="text">
                            <div style="alignment-adjust:central;" >
                                <% String contextPath = request.getContextPath(); %>
                                <s:form action="../shipment/shipmentSearch.action" method="post" name="shipmentForm" id="shipmentForm" theme="simple">
                                    <s:hidden id="datepickerfrom" name="datepickerfrom" />
                                    <s:hidden id="datepicker" name="datepicker"/>
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
                                                        <s:textfield name="reportrange"  id="reportrange" cssClass="form-control pull-left"   value="%{reportrange}" onchange="Date1();"  tabindex="1" /> 
                                                    </div>
                                                    <script type="text/javascript">
                                                        function Date1() {
                                                            var date = document.shipmentForm.reportrange.value;
                                                            var arr = date.split("-");
                                                            var x = arr[1].trim();
                                                            document.getElementById("datepickerfrom").value = arr[0];
                                                            document.getElementById("datepicker").value = x;
                                                        }
                                                    </script>
                                                    <div  class="col-sm-3">
                                                        <label>Document Type</label> 
                                                        <s:select headerKey="-1" cssClass="form-control" headerValue="Select Type" list="{'856'}" name="docType" id="docType" value="%{docType}"  tabindex="2"/>
                                                    </div>

                                                    <div class="col-sm-3">
                                                        <label for="ackStatus">Ack Status</label>
                                                        <s:select headerKey="-1" headerValue="Select Type" cssClass="form-control" list="{'Overdue','Accepted','Rejected'}" name="ackStatus" id="ackStatus" value="%{ackStatus}"   tabindex="7"/> 
                                                    </div>
                                                    <div class="col-sm-3">
                                                        <label for="status">Status</label>
                                                        <s:select headerKey="-1" headerValue="Select Type" cssClass="form-control" list="{'Success','Error','Warning'}" name="status" id="status" value="%{status}"  tabindex="8" /> 
                                                    </div>



                                                </div>

                                                <div class="row">



                                                    <div  class="col-sm-3">
                                                        <label>Sender </label>  
                                                        <s:textfield onchange="setSenderId();"  cssClass="form-control" list="senderIdMap" name="docSen" id="docSen" value="%{senderId}" tabindex="5"/>
                                                        <%Map<String, String> senderIdMap = (Map) session.getAttribute(AppConstants.SENDERSID_MAP);

                                                        %>
                                                        <datalist id="senderIdMap">
                                                            <% for (Map.Entry<String, String> entry : senderIdMap.entrySet()) {%>
                                                            <option value="<%=entry.getValue()%>"/>
                                                            <%}%>
                                                        </datalist> 
                                                        <s:hidden name="senderId" id="senderId" />
                                                    </div>
                                                    <div class="col-sm-3">
                                                        <label>Receiver </label>
                                                        <s:textfield onchange="setReceiverId();"  cssClass="form-control" list="receiverIdMap" name="docRec" id="docRec" value="%{buId}" tabindex="6"/>
                                                        <%Map<String, String> receiverIdMap = (Map) session.getAttribute(AppConstants.RECEIVERSId_MAP);

                                                        %>
                                                        <datalist id="receiverIdMap">
                                                            <% for (Map.Entry<String, String> entry : receiverIdMap.entrySet()) {%>
                                                            <option value="<%=entry.getValue()%>"/>
                                                            <%}%>
                                                        </datalist> 
                                                        <s:hidden name="buId" id="buId" />
                                                    </div>




                                                </div>

                                                <div class="row">
                                                    <div class="col-sm-3">
                                                        <label for="corrattribute">Correlation</label>
                                                        <s:select headerKey="-1" headerValue="Select Attribute" cssClass="form-control" list="correlationList" name="corrattribute" id="corrattribute" value="%{corrattribute}"   tabindex="9"/>
                                                    </div>
                                                    <div class="col-sm-3">
                                                        <label for="corrvalue">Value</label>
                                                        <s:textfield cssClass="form-control" name="corrvalue" id="corrvalue" value="%{corrvalue}"  tabindex="10"/>
                                                    </div>

                                                    <div class="col-sm-3"><br>
                                                        <button  type="button" id="addButton" name="addButton" value="Add Div" class="btn btn-success"   style="margin-top:6px ;"  tabindex="11" ><i class="fa fa-plus"></i></button>
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
                                                            <s:select headerKey="-1" headerValue="Select Attribute" cssClass="form-control" list="correlationList" name="corrattribute1" id="corrattribute1" value="%{corrattribute1}"  tabindex="12"/>
                                                        </div>
                                                        <div class="col-sm-3">
                                                            <label for="corrvalue1">Value</label>
                                                            <s:textfield cssClass="form-control" name="corrvalue1" id="corrvalue1" value="%{corrvalue1}"  tabindex="13"/>
                                                        </div>
                                                        <div class="col-sm-2"><br>
                                                            <button  type="button" id="removeButton1" name="removeButton1" value="Remove Div" class="btn btn-warning"   style="margin-top:6px ;" tabindex="14"><i class="fa fa-minus"></i></button>
                                                            &nbsp; <label>Remove Filter</label>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>

                                        <div class="row">
                                            <div class="col-sm-2"><s:submit value="Search"  onclick="return checkCorrelation();" cssClass="btn btn-primary col-sm-12" tabindex="14"/></div>
                                            <div class="col-sm-2"><strong><input type="button" value="Reset"   class="btn btn-primary col-sm-12" onclick="return resetvaluesShipment();" tabindex="15"/></strong></div>
                                                    <s:hidden name="sampleValue" id="sampleValue" value="2"/>
                                                </s:form>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </section>
            <div id="gridDiv">
                <s:if test="#session.shipmentSearchList != null">
                    <section class="content">
                        <div class="row">
                            <div class="col-xs-12">
                                <div class="box">
                                    <!--                                    <div class="box-header">
                                                                            <h3 class="box-title">Table</h3>
                                                                        </div> /.box-header -->
                                    <div class="box-body">
                                        <div style="overflow-x:auto;">                 
                                            <table align="left" width="100%" border="0" cellpadding="0" cellspacing="0">
                                                <tr>
                                                    <td style="background-color: white;">
                                                        <%!String cssValue = "whiteStripe";
                                                            int resultsetTotal;%>
                                                        <table id="results"  class="table table-bordered table-hover">
                                                            <%
                                                                java.util.List list = (java.util.List) session.getAttribute(AppConstants.SES_SHIPMENT_LIST);
                                                                if (list.size() != 0) {
                                                                    ShipmentBean shipmentBean;
                                                            %>
                                                            <input type="hidden" name="sec_shipment_list" id="sec_shipment_list" value="<%=list.size()%>"/> 
                                                            <thead><tr>
                                                                    <th>DateTime</th>
                                                                    <th>InstanceId</th>
                                                                    <th >ASN #</th>
                                                                    <th >Partner</th>  
                                                                    <th>Direction</th>
                                                                    <th>Status</th>
                                                                    <th>Ack&nbsp;Status</th>
                                                            </thead>
                                                            <%
                                                                for (int i = 0; i < list.size(); i++) {
                                                                    shipmentBean = (ShipmentBean) list.get(i);
                                                                    if (i % 2 == 0) {
                                                                        cssValue = "whiteStripe";
                                                                    } else {
                                                                        cssValue = "grayEditSelection";
                                                                    }
                                                            %>
                                                            <tr>
                                                                <td>
                                                                    <%
                                                                        if (shipmentBean.getDate_time_rec().toString().substring(0, shipmentBean.getDate_time_rec().toString().lastIndexOf(":")) != null
                                                                                && !"".equals(shipmentBean.getDate_time_rec().toString().substring(0, shipmentBean.getDate_time_rec().toString().lastIndexOf(":")))) {
                                                                            out.println(shipmentBean.getDate_time_rec().toString().substring(0, shipmentBean.getDate_time_rec().toString().lastIndexOf(":")));
                                                                        } else {
                                                                            out.println("-");
                                                                        }
                                                                    %>
                                                                </td>
                                                                <td>
                                                                    <%
                                                                        if (shipmentBean.getFile_id() != null && !"".equals(shipmentBean.getFile_id())) {
                                                                            out.println(shipmentBean.getFile_id());
                                                                        } else {
                                                                            out.println("-");
                                                                        }
                                                                    %>
                                                                    <input type="hidden" name="Instance<%=i%>" id="Instance<%=i%>" value="<%=shipmentBean.getFile_id()%>"/> 
                                                                </td>
                                                                <td>  <a style="color: deepskyblue" href="javascript:getDetails('<%=shipmentBean.getAsnNo()%>','<%=shipmentBean.getPoNo()%>','<%=shipmentBean.getFile_id()%>');">
                                                                        <%
                                                                            if (shipmentBean.getAsnNo() != null && !"".equals(shipmentBean.getAsnNo())) {
                                                                                out.println(shipmentBean.getAsnNo());
                                                                            } else {
                                                                                out.println("-");
                                                                            }
                                                                        %>
                                                                        <input type="hidden" name="text<%=i%>" id="text<%=i%>" value="<%=shipmentBean.getAsnNo()%>"/> 
                                                                    </a>
                                                                </td>
                                                                <td>
                                                                    <%
                                                                        if (shipmentBean.getPname() != null && !"".equals(shipmentBean.getPname())) {
                                                                            out.println(shipmentBean.getPname());
                                                                        } else {
                                                                            out.println("-");
                                                                        }
                                                                    %>
                                                                </td>
                                                                <td>
                                                                    <%
                                                                        if (shipmentBean.getDirection() != null && !"".equals(shipmentBean.getDirection())) {
                                                                            out.println(shipmentBean.getDirection().toUpperCase());
                                                                        } else {
                                                                            out.println("-");
                                                                        }
                                                                    %>
                                                                </td>
                                                                <td>
                                                                    <%
                                                                        if (shipmentBean.getStatus().equalsIgnoreCase("ERROR")) {
                                                                            out.println("<font color='red'>" + shipmentBean.getStatus().toUpperCase() + "</font>");
                                                                        } else if (shipmentBean.getStatus().equalsIgnoreCase("SUCCESS")) {
                                                                            out.println("<font color='green'>" + shipmentBean.getStatus().toUpperCase() + "</font>");
                                                                        } else {
                                                                            out.println("<font color='orange'>" + shipmentBean.getStatus().toUpperCase() + "</font>");
                                                                        }
                                                                    %>
                                                                </td>
                                                                <td>
                                                                    <%
                                                                        if (shipmentBean.getAckStatus().equalsIgnoreCase("REJECTED")) {
                                                                            out.println("<font color='red'>" + shipmentBean.getAckStatus().toUpperCase() + "</font>");
                                                                        } else if (shipmentBean.getAckStatus().equalsIgnoreCase("ACCEPTED")) {
                                                                            out.println("<font color='green'>" + shipmentBean.getAckStatus().toUpperCase() + "</font>");
                                                                        } else {
                                                                            out.println("<font color='orange'>" + shipmentBean.getAckStatus().toUpperCase() + "</font>");
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
                                                </tbody>
                                            </table></div>
                                            <%-- Process butttons  start --%>
                                            <% // out.println(session.getAttribute(AppConstants.SES_ROLE_ID));
                                                if ((session.getAttribute(AppConstants.SES_ROLE_ID).equals("100") || session.getAttribute(AppConstants.SES_ROLE_ID).equals("104") || session.getAttribute(AppConstants.SES_ROLE_ID).equals("101")) && list.size() != 0) {
                                            %>
                                        <div class="row">
                                            <div  class="gnexcel-space" id="gridButtons" >
                                                <div class="col-sm-2"><input type="button" value="Generate Excel" class="btn btn-effect-ripple btn-primary" onclick="return gridDownload('shipment', 'xls');" onmouseover="Tip('Click here to generate an excel Report.')" onmouseout="UnTip()"  id="excel"/></div>
                                            </div>
                                        </div>
                                        <%}%>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </section>
                    <%-- process buttons end--%>
                </s:if>
            </div>
            <div id="hide-menu1" class="hide-menu message ">
                <div class="row col-sm-12">
                    <br>
                    <div class="col-sm-6">  <label class="labelw">  Instance Id </label>
                        <s:textfield cssClass="form-control"  required="required" placeholder="" id="msfileID" name="msfileID" readonly="true"/>
                    </div>
                    <div class="col-sm-6">  <label class="labelw">  ASN # </label>
                        <s:textfield cssClass="form-control"  required="required" placeholder="" id="msasnNum" name="msasnNum" readonly="true"/>
                    </div>
                </div>
                <div class="row col-sm-12">
                    <div class="col-sm-6">  <label class="labelw">  PO&nbsp;&nbsp;# </label>
                        <s:textfield cssClass="form-control"  required="required" placeholder="" id="mspoNum" name="mspoNum" readonly="true"/>
                    </div>
                    <div class="col-sm-6">  <label class="labelw">  BOL&nbsp;&nbsp;# </label>
                        <s:textfield cssClass="form-control"  required="required" placeholder="" id="msbolNum" name="msbolNum" readonly="true"/>
                    </div>
                </div>
                <div class="row col-sm-12">
                    <div class="col-sm-6">  <label class="labelw">  Ship Date </label>
                        <s:textfield cssClass="form-control"  required="required" placeholder="" id="msshipDate" name="msshipDate" readonly="true"/>
                    </div>
                    <div class="col-sm-6">  <label class="labelw">  Document&nbsp;Type </label>
                        <s:textfield cssClass="form-control"  required="required" placeholder="" id="msdocType" name="msdocType" readonly="true"/>
                    </div>
                </div>
                <div class="row col-sm-12">
                    <div class="col-sm-6">  <label class="labelw">  Transaction&nbsp;Type </label>
                        <s:textfield cssClass="form-control"  required="required" placeholder="" id="mstransType" name="mstransType" readonly="true"/>
                    </div>
                    <div class="col-sm-6">  <label class="labelw">  ISA&nbsp;&nbsp;# </label>
                        <s:textfield cssClass="form-control"  required="required" placeholder="" id="msisaNum" name="msisaNum" readonly="true"/>
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
                        <div class="col-sm-6">  <label class="labelw">  Id </label>
                            <s:textfield cssClass="form-control"  required="required" placeholder="" id="mssenderId" name="mssenderId" readonly="true"/>
                        </div>
                        <div class="col-sm-6">  <label class="labelw">  Name </label>
                            <s:textfield cssClass="form-control"  required="required" placeholder="" id="mssenderName" name="mssenderName" readonly="true"/>
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
                        <div class="col-sm-6">  <label class="labelw">   Id </label>
                            <s:textfield cssClass="form-control"  required="required" placeholder="" id="msreceiverId" name="msreceiverId" readonly="true"/>
                        </div>
                        <div class="col-sm-6">  <label class="labelw">  Name </label>
                            <s:textfield cssClass="form-control"  required="required" placeholder="" id="msreceiverName" name="msreceiverName" readonly="true"/>
                        </div>
                    </div>
                </div>
                <div class="row col-sm-12">
                    <br>
                    <div class="col-sm-6">  <label class="labelw">  ISA # </label>
                        <s:textfield cssClass="form-control"  required="required" placeholder="" id="msgsControlNo" name="msgsControlNo" readonly="true"/>
                    </div>
                    <div class="col-sm-6">  <label class="labelw">  GS # </label>
                        <s:textfield cssClass="form-control"  required="required" placeholder="" id="msstControlNo" name="msstControlNo" readonly="true"/>
                    </div>
                </div>
                <div class="row col-sm-12">
                    <div class="col-sm-6">  <label class="labelw">  ST # </label>
                        <s:textfield cssClass="form-control"  required="required" placeholder="" id="msisaDate" name="msisaDate" readonly="true"/>
                    </div>
                    <div class="col-sm-6">  <label class="labelw">  Status </label>
                        <s:textfield cssClass="form-control"  required="required" placeholder="" id="msstatus" name="msstatus" readonly="true"/>
                    </div>
                </div>
                <div class="row col-sm-12">
                    <div class="col-sm-6">  <label class="labelw">  ISA Date </label>
                        <s:textfield cssClass="form-control"  required="required" placeholder="" id="msisaTime" name="msisaTime" readonly="true"/>
                    </div>
                </div>
                <br/>
                <div class="row col-sm-12" style="margin-top:10px;">
                    <div class="col-sm-6"> <label class="labelw">  Pre-Translation </label></div>
                    <div class="col-sm-6" id="mspreTransFilepath"></div>
                </div>
                <div class="row col-sm-12">
                    <div class="col-sm-6">  <label class="labelw"> Post-Translation </label></div>
                    <div class="col-sm-6" id="mspostTransFilepath"></div>
                </div>
                <div class="row col-sm-12">
                    <div class="col-sm-6">  <label class="labelw">  997&nbsp;Ack&nbsp;File </label></div>
                    <div class="col-sm-6" id="msackFileId"></div>
                </div>
                <div class="row col-sm-12" id="errorDiv" style="display: none">
                    <div class="col-sm-6">  <label class="labelw">  Error&nbsp;Message </label></div>
                    <div class="col-sm-6" id="mserrormessage" style="color: red"></div>
                </div>
                <div class="row col-sm-12" id="errorReportDiv" style="display: none">
                    <div class="col-sm-6"> <label class="labelw">  Error&nbsp;Report </label></div>
                    <div class="col-sm-6"><div id="ErrReport"></div></div>
                </div>
                <div id="noresult"></div>
                <br/>
                <div class="row col-sm-12" style="margin-top:10px;"> <button type="button" class="btn btn-primary col-sm-11" id="hide-menu" onclick="hide()" style="margin-left:12px;" value="X">Close</button></div>
            </div>
        </div>

        <div>
            <s:include value="../includes/template/footer.jsp"/>
        </div>
        <script language="JavaScript"  src='<s:url value="/includes/js/DateValidation.js"/>'></script>
        <script language="JavaScript"  src='<s:url value="/includes/js/generalValidations.js"/>'></script>
        <script language="JavaScript"  src='<s:url value="/includes/js/GeneralAjax.js"/>'></script>
        <script language="JavaScript"  src='<s:url value="/includes/js/downloadAjax.js"/>'></script>
        <script src='<s:url value="../includes/plugins/daterangepicker/daterangepicker.js"/>'></script>
        <script src='<s:url value="../includes/bootstrap/js/app.min.js"/>'></script>
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

                    function getDetails(val, ponum, fileid) {
                        var db = document.forms["shipmentForm"]["database"].value;
                        getAsnDetails(val, ponum, fileid, db);
                    }

                    function checkCorrelation() {
                        var db = document.forms["shipmentForm"]["database"].value;
                        if (db == '') {
                            alert("Please select Database!!!");
                            return false;
                        }
                        var corrattr = document.getElementById('corrattribute').value;
                        var corrval = document.getElementById('corrvalue').value;
                        var corrattr1 = document.getElementById('corrattribute1').value;
                        var corrval1 = document.getElementById('corrvalue1').value;
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
                        var res = Formvalidation(document.getElementById('datepickerfrom').value, document.getElementById('datepicker').value);
                        return res;
                    }

                    function resetvaluesShipment() {
                        $('#hide-menu1').removeClass('show-menu');
                        //$('.myRadio').attr('checked', false);
                        document.getElementById('docRec').value = "";
                        document.getElementById('datepickerfrom').value = "";
                        document.getElementById('datepicker').value = "";
                        document.getElementById('reportrange').value = ""
                        document.getElementById('docType').value = "-1";
                        document.getElementById('docSen').value = "";
//                        document.getElementById('senderName').value = "-1";
//                        document.getElementById('recName').value = "-1";
                        document.getElementById('senderId').value = ""
                        document.getElementById('buId').value = "";
                        document.getElementById('sampleValue').value = "1";
                        document.getElementById('ackStatus').value = "-1";
                        document.getElementById('status').value = "-1";
                        document.getElementById('corrattribute').value = "-1";
                        document.getElementById('corrvalue').value = "";
                        document.getElementById('corrattribute1').value = "-1";
                        document.getElementById('corrvalue1').value = "";
                        var elements = document.getElementsByName('database');
                        elements[0].checked = true;
                        $('#gridDiv').hide();
                        $('#corr').hide();
                        count = 0;
                    }

                    function doOnLoad() {
                        $("#shipments").addClass("active");
                        $("#supplychain").addClass("active");
                        $("#manufacturing").addClass("active");
                        $("#shipments i").addClass("text-red");
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
