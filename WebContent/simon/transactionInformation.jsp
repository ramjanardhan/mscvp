<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page buffer="50kb" autoFlush="true" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@page import="com.mss.ediscv.doc.DocRepositoryBean"%>
<%@page import="com.mss.ediscv.partner.PartnerBean"%>
<%@ taglib uri="/WEB-INF/tlds/dbgrid.tld" prefix="grd"%>
<%@ page import="com.freeware.gridtag.*"%>
<%@page import="java.sql.Connection"%>
<%@  page import="com.mss.ediscv.util.AppConstants"%>
<%@ page import="com.mss.ediscv.util.ConnectionProvider"%>
<%@ page import="java.sql.SQLException"%>
<%@ page import = "java.util.ResourceBundle" %>

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


        <script>
            function doOnLoad()
            {
                $("#simonscreen").addClass("active");
                $("#transactionInformation").addClass("active");
                $("#transactionInformation i").addClass("text-red");
                //  document.getElementById('loadingAcoountSearch').style.display = "none";
            }

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
                    "order": [[0, 'desc']]
                });
            });
        </script>
        <style>

            table, td, th 
            {    
                border: 1px solid black;
                text-align: left;

            }
            /*th{background-color:  #ffa366;}*/
            table {
                border-collapse: collapse;
                width:50%;
            }

            th, td {
                padding: 15px;
            }
        </style>
    </head>
    <%
        String check = null;
        if (request.getAttribute("check") != null) {
            check = request.getAttribute("check").toString();
        }
    %>
    <body class="hold-transition skin-blue sidebar-mini" onload="doOnLoad();">    
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
                <!--                <h1>
                                    Transaction Information
                                                        <small>Manufacturing</small>
                                </h1>-->
                <!--                <ol class="breadcrumb">
                                    <li><a href="#"><i class="fa fa-wrench"></i>Manufacturing</a></li>
                                    <li class="active">Document Repository</li>
                                </ol>-->
            </section>
            <!--<br>-->
            <section class="content">
                <div class="box box-primary">
                    <div class="box-header with-border">

                        <div class="box-tools pull-right">
                        </div>
                    </div>  

                    <thead>
                    <div class="box-body">
                        <div id="text">
                            <div  style="alignment-adjust:central;" >
                                <%String contextPath = request.getContextPath();%>
                                <s:form action="%{formAction}" method="post" name="partnerForm" id="partnerForm" theme="simple">
                                    <s:hidden name="formAction" id="formAction" value="%{formAction}"/>
                                    <div id="resMsg" style="color: red"></div>
                                    <div class="form-group">
                                        <div class="row">
                                            <div class="col-sm-12">

                                                <!--<div class="row">-->
                                                <div class="col-sm-4"> 
                                                    <h4><strong> Transaction Information</strong></h4>
                                                    <div class="row"><div class="col-sm-5"><label>Transaction ID</label></div>
                                                        <div class="col-sm-5"> <s:textfield name="transactionId"  id="transactionId"  value="%{transactionId}" tabindex="1"/></div></div><br>
                                                    <div class="row"><div class="col-sm-5"><label>Partner Name</label></div>
                                                        <div class="col-sm-5"><s:textfield name="partnerName"  id="partnerName"  value="%{partnerName}" tabindex="2"/> </div></div><br>
                                                    <div class="row"><div class="col-sm-5"><label>Application ID</label></div>
                                                        <div class="col-sm-5"><s:textfield name="applicationId"  id="applicationId"  value="%{applicationId}" tabindex="3"/> </div></div><br>
                                                    <div class="row"><div class="col-sm-5"> <label>Direction</label></div>
                                                        <div class="col-sm-5"> <s:textfield name="direction"  id="direction"  value="%{direction}" tabindex="4"/> </div></div><br>
                                                    <div class="row"><div class="col-sm-5"><label>Sender ID</label></div>
                                                        <div class="col-sm-5"><s:textfield name="senderId"  id="senderId"  value="%{senderId}" tabindex="5"/> </div></div><br>
                                                    <div class="row"><div class="col-sm-5"><label>Receiver ID</label></div>
                                                        <div class="col-sm-5"> <s:textfield name="receiverId"  id="receiverId"  value="%{receiverId}" tabindex="6"/> </div></div><br>
                                                    <div class="row"><div class="col-sm-5"><label>Reference Name</label></div>
                                                        <div class="col-sm-5"> <s:textfield name="referenceName"  id="referenceName"  value="%{referenceNamen}" tabindex="7"/> </div></div><br>
                                                    <div class="row"><div class="col-sm-5"><label>Reference Value</label></div>
                                                        <div class="col-sm-5"><s:textfield name="referenceValue"  id="referenceValue"  value="%{referenceValue}" tabindex="8"/> </div></div><br>
                                                    <div class="row"><div class="col-sm-5"><label>Map Name</label></div>
                                                        <div class="col-sm-5"><s:textfield name="mapName"  id="mapName"  value="%{mapName}" tabindex="9"/> </div></div><br>
                                                    <div class="row"><div class="col-sm-5"><label>Document Type</label></div>
                                                        <div class="col-sm-5"><s:textfield name="documentType"  id="documentType"  value="%{documentType}" tabindex="10"/> </div></div><br>
                                                    <div class="row"><div class="col-sm-5"><label>Transaction Type</label></div>
                                                        <div class="col-sm-5"><s:textfield name="transactionType"  id="transactionType"  value="%{transactionType}" tabindex="11"/></div></div><br>
                                                    <div class="row"><div class="col-sm-5"><label >Created Date</label></div>
                                                        <div class="col-sm-5"><s:textfield name="createdDate"  id="createdDate"  value="%{createdDate}" tabindex="12"/> </div></div><br>
                                                    <div class="row"><div class="col-sm-5"><label>Changed Date</label></div>
                                                        <div class="col-sm-5"> <s:textfield name="changedDate"  id="changedDate"  value="%{changedDate}" tabindex="13"/> </div></div><br>
                                                    <div class="row"><div class="col-sm-5"> <label>Created User</label></div>
                                                        <div class="col-sm-5"><s:textfield name="createdUser"  id="createdUser"  value="%{createdUser}" tabindex="14"/> </div></div><br>
                                                    <div class="row"><div class="col-sm-5"><label>Changed User</label></div>
                                                        <div class="col-sm-5"><s:textfield name="changedUser"  id="changedUser"   value="%{changedUser}" tabindex="15"/> </div></div><br>
                                                    <div class="row"><div class="col-sm-5"> </div>
                                                        <div class="col-sm-7"><s:select headerKey="-1" headerValue="Archived Internal" cssClass="form-control" list="{'Archived External','Error'}" name="status" id="archivedExternal" value="%{archivedExternal}" tabindex="16" /></div></div><br>
                                                    <div class="row"><div class="col-sm-5"> <label>Current Status</label></div>
                                                        <div class="col-sm-5">  <span class="glyphicon glyphicon-pencil" style="color:#ffa366"></span></div></div><br>
                                                    <div class="row"><div class="col-sm-5"><label>Delivery Channel</label></div>
                                                        <div class="col-sm-5"><s:textfield name="deliveryChannel"  id="deliveryChannel"  value="%{deliveryChannel}" tabindex="17"/> </div></div><br>
                                                </div>
                                                <div class="col-sm-4"><h4><strong>Envelope Reference</strong></h4> 
                                                    <div class="row"><div class="col-sm-5"><label>Inter Change</label></div>
                                                        <div class="col-sm-5"> <s:textfield name="interChange"  id="interChange" value="%{interChange}" tabindex="18"/> </div></div><br> 
                                                    <div class="row"><div class="col-sm-5"><label class="inline">Group</label></div>
                                                        <div class="col-sm-5"><s:textfield name="group<"  id="group"  value="%{group}" tabindex="19"/> </div></div><br>
                                                    <div class="row"><div class="col-sm-5"><label >Message</label></div>
                                                        <div class="col-sm-5"> <s:textfield name="message"  id="message" value="%{message}" tabindex="20"/> </div></div><br>
                                                </div>
                                                <div class="col-sm-4">
                                                    <strong><input type="button" value="Create PDF" class="btn btn-default pull-right" tabindex="21"></strong>
                                                </div> 


                                                <!--</div>-->




                                            </div>
                                        </div>

                                        <br>
                                        <span id="span1">
                                        </span>
                                        <div class="row">
                                            <!--                                             <div class="col-sm-2"> <strong><input type="button" value="Add" class="btn btn-primary col-sm-12" tabindex="22"></strong></div>
                                                                                         <div class="col-sm-2"> <strong><input type="button" value="Export" class="btn btn-primary col-sm-12" tabindex="23"></strong></div>
                                                                                         <div class="col-sm-2"> <strong><input type="button" value="Export All" class="btn btn-primary col-sm-12" tabindex="24"></strong></div>
                                                                                        <div class="col-sm-2"><s:submit value="Search"  onclick="return checkCorrelation();"   cssClass="btn btn-primary col-sm-12" tabindex="25"/></div>
                                            -->
                                            </td>
                                            <s:hidden name="sampleValue" id="sampleValue" value="2"/>
                                        </s:form>
                                    </div>
                                    <div class="row col-sm-12">
                                        <h4><strong>File Links</strong></h4>
                                        <div class="row"><div class="col-sm-2"><label>Internal</label></div>
                                            <a href="#"> view </a></div><br>
                                    </div>  
                                    <div class="row col-sm-12">
                                        <h4><strong>Transaction References</strong></h4>
                                        <table>
                                            <thead><tr>
                                                    <th>Name  <i class="fa fa-sort"></i></th>
                                                    <th>value <i class="fa fa-sort"></i></th> 
                                                    <th >Created Date <i class="fa fa-sort"></i></th>
                                                    <th >Created By <i class="fa fa-sort"></i></th>

                                                </tr> </thead> 
                                            <tbody>
                                                <tr>
                                                    <td>Test</td>
                                                    <td>Test</td>
                                                    <td>Test</td>
                                                    <td>Test</td>
                                                </tr>
                                            </tbody>
                                        </table> 
                                    </div>
                                    <div class="row col-sm-12">
                                        <h4><strong>Transaction History</strong></h4>
                                        <label>Show All</label>
                                        <s:checkbox name="showAll" fieldValue="true"/>
                                        <table style="width:100%">
                                            <thead><tr>
                                                    <th >Change Value</th>
                                                    <th>Old value</th> 
                                                    <th >New value</th>
                                                    <th >Message</th>
                                                    <th >Created Date</th>
                                                    <th >Created By</th>

                                                </tr> </thead> 
                                            <tbody>
                                                <tr>
                                                    <td>Test</td>
                                                    <td>Test</td>
                                                    <td>Test</td>
                                                    <td>Test</td>
                                                    <td>Test</td>
                                                    <td>Test</td>
                                                </tr>
                                                <tr>
                                                    <td>Test</td>
                                                    <td>Test</td>
                                                    <td>Test</td>
                                                    <td>Test</td>
                                                    <td>Test</td>
                                                    <td>Test</td>
                                                </tr>
                                            </tbody>
                                        </table> 
                                    </div> 
                                    <div class="row col-sm-12">
                                        <h4><strong>Transaction Children</strong></h4>
                                        <table style="width:100%">
                                            <thead><tr>
                                                    <th >View</th>
                                                    <th>InterChange</th>
                                                    <th>Group</th>
                                                    <th>Message</th>
                                                    <th >Transaction Type</th>
                                                    <th >Document Type</th>
                                                    <th >Reference Value</th>
                                                    <th >Created Date</th>
                                                    <th >Sender Id</th>
                                                    <th >Receiver Id</th>
                                                    <th >Status</th>
                                                </tr> </thead> 
                                            <tbody>
                                                <tr>
                                                    <td>Test</td>
                                                    <td>Test</td>
                                                    <td>Test</td>
                                                    <td>Test</td>
                                                    <td>Test</td>
                                                    <td>Test</td>
                                                    <td>Test</td>
                                                    <td>Test</td>
                                                    <td>Test</td>
                                                    <td>Test</td>
                                                    <td>Test</td>
                                                </tr>

                                            </tbody>
                                        </table> 
                                    </div>  
                                </div> 
                            </div>
                        </div>
                    </div></div>

            </section>
            <!-- Control Sidebar -->
            <!-- /.control-sidebar -->
            <!-- Add the sidebar's background. This div must be placed
                 immediately after the control sidebar -->
            <!-- ./wrapper -->
            <div id="gridDiv">     
                <s:if test="#session.documentList != null"> 
                    <!--                    <%--- GRid start --%>
                    <%!String cssValue = "whiteStripe";
                        int resultsetTotal;%>
                    <section class="content">
                        <div class="row">
                            <div class="col-xs-12">
                                <div class="box">
                                    <div class="box-header">
                                        <h3 class="box-title">Table</h3>
                                    </div> /.box-header 
                                    <div class="box-body">
                                        <div style="overflow-x:auto;">                 
                                            <table align="left" width="100%"
                                                   border="0" cellpadding="0" cellspacing="0" >
                                                <tr>
                                                    <td style="background-color: white;">
                                                        <div style="overflow-x:auto;"> 
                                                            <table id="results"  class="table table-bordered table-hover">
                    <%
                        java.util.List list = (java.util.List) session.getAttribute(AppConstants.SES_DOC_LIST);
                        if (list.size() != 0) {
                            DocRepositoryBean docRepositoryBean;
                    %>
                    <thead><tr>
                            <th >DateTime</th>
                            <th>File&nbsp;Format</th> 
                            <th >InstanceId</th>
                            <th >Partner</th>
                            <th >Trans&nbsp;Type</th>
                            <th >Direction</th>
                            <th >Status</th>
                            <th >Reprocess</th>
                            <th >ACK_STATUS</th></tr> </thead>
                    <tbody>
                    <%
//                                                                        String corrattribute;
//                                                                        String corrattribute1;
//                                                                        String corrattribute2;
                        for (int i = 0; i < list.size(); i++) {
                            docRepositoryBean = (DocRepositoryBean) list.get(i);
//                                                                            corrattribute = docRepositoryBean.getCorrattribute();
//                                                                            corrattribute1 = docRepositoryBean.getCorrattribute1();
//                                                                            corrattribute2 = docRepositoryBean.getCorrattribute2();

                            //if (corrattribute != "-1" && !"-1".equalsIgnoreCase(corrattribute) && i == 0) {
                    %>
                    <%--<td><%=corrattribute%> </td> 
                    <%}
                        if (corrattribute1 != "-1" && !"-1".equalsIgnoreCase(corrattribute1) && i == 0) {%>
                    <td ><%=corrattribute1%> </td> 
                    <%}
                        if (corrattribute2 != "-1" && !"-1".equalsIgnoreCase(corrattribute2) && i == 0) {%>
                    <td ><%=corrattribute2%> </td> 
                    <%
                        }
                    %> --%>
                    </tr>
                    <tr>
                        <td>
                    <%  if (docRepositoryBean.getDate_time_rec().toString().substring(0, docRepositoryBean.getDate_time_rec().toString().lastIndexOf(":")) != null
                                && !"".equals(docRepositoryBean.getDate_time_rec().toString().substring(0, docRepositoryBean.getDate_time_rec().toString().lastIndexOf(":")))) {
                            out.println(docRepositoryBean.getDate_time_rec().toString().substring(0, docRepositoryBean.getDate_time_rec().toString().lastIndexOf(":")));
                        } else {
                            out.println("-");
                        }
                        //out.println(docRepositoryBean.getDate_time_rec().toString().substring(0, docRepositoryBean.getDate_time_rec().toString().lastIndexOf(":")));
                    %>
                </td> 
                <td>
                    <%                                                                            if (docRepositoryBean.getFile_type() != null && !"".equals(docRepositoryBean.getFile_type())) {
                            out.println(docRepositoryBean.getFile_type());
                        } else {
                            out.println("-");
                        }
                        //out.println(docRepositoryBean.getFile_type());
%>
                                                                                            </td>
                                                                                            <td><a href="javascript:getDetails('<%=docRepositoryBean.getFile_id()%>','<%=docRepositoryBean.getPoNumber()%>','<%=docRepositoryBean.getId()%>');">
                    <%
                        if (docRepositoryBean.getFile_id() != null && !"".equals(docRepositoryBean.getFile_id())) {
                            out.println(docRepositoryBean.getFile_id());
                        } else {
                            out.println("-");
                        }
                        // out.println(docRepositoryBean.getFile_id());
                    %>
                </a>
            </td>
            <td>
                    <%
                        if (docRepositoryBean.getPname() != null && !"".equals(docRepositoryBean.getPname())) {
                            out.println(docRepositoryBean.getPname());
                        } else {
                            out.println("-");
                        }
                    %>
                </td>
                <td>
                    <%                                                                            //out.println(docRepositoryBean.getTransaction_type());
                        if (docRepositoryBean.getTransaction_type() != null && !"".equals(docRepositoryBean.getTransaction_type())) {
                            out.println(docRepositoryBean.getTransaction_type());
                        } else {
                            out.println("-");
                        }
                    %>
                </td>
                <td>
                    <%
                        if (docRepositoryBean.getDirection() != null && !"".equals(docRepositoryBean.getDirection())) {
                            out.println(docRepositoryBean.getDirection().toUpperCase());
                        } else {
                            out.println("-");
                        }
                    %>
                </td>  
                <td>
                    <%
                        if (docRepositoryBean.getStatus().equalsIgnoreCase("ERROR")) {
                            out.println("<font color='red'>" + docRepositoryBean.getStatus().toUpperCase() + "</font>");
                        } else if (docRepositoryBean.getStatus().equalsIgnoreCase("SUCCESS")) {
                            out.println("<font color='green'>" + docRepositoryBean.getStatus().toUpperCase() + "</font>");
                        } else {
                            out.println("<font color='orange'>" + docRepositoryBean.getStatus().toUpperCase() + "</font>");
                        }
                    %>
                </td>
                <td>
                    <%
                        if (docRepositoryBean.getReProcessStatus() != null && !"".equals(docRepositoryBean.getReProcessStatus())) {
                            out.println(docRepositoryBean.getReProcessStatus());
                        } else {
                            out.println("-");
                        }
                    %>
                </td>
                <td>
                    <%
                        if (docRepositoryBean.getAckStatus() != null && !"".equals(docRepositoryBean.getAckStatus())) {
                            out.println(docRepositoryBean.getAckStatus().toUpperCase());
                        } else {
                            out.println("-");
                        }
                        //out.println(docRepositoryBean.getAckStatus());
                    %>
                </td>
                    <%--<%if (docRepositoryBean.getCorrvalue() != null && !"".equalsIgnoreCase(docRepositoryBean.getCorrvalue())) {%>
                    <td ><%=docRepositoryBean.getCorrvalue()%> </td> 
                    <%}
                        if (docRepositoryBean.getCorrvalue1() != null && !"".equalsIgnoreCase(docRepositoryBean.getCorrvalue1())) {%>
                    <td ><%=docRepositoryBean.getCorrvalue1()%> </td> 
                    <%}
                        if (docRepositoryBean.getCorrvalue2() != null && !"".equalsIgnoreCase(docRepositoryBean.getCorrvalue2())) {%>
                    <td ><%=docRepositoryBean.getCorrvalue2()%> </td> 
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
                                                                                    <div align="right" id="pageNavPosition">hello</div>
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
                            <td style="background-color: white;">
                                <strong><input type="button" value="Generate Excel" class="btn btn-effect-ripple btn-primary" onclick="return gridDownload('document', 'xls');" onmouseover="Tip('Click here to generate an excel Report.')" onmouseout="UnTip()" id="excel"/></strong>
                            </td>
                        </tr>
                    </table> 
                    <%}%>
                </div>
            </div></div>
    </div></section>-->
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
                            <div class="col-sm-6"> <label class="labelw">  Purchase Order</label>
                                <s:textfield  cssClass="form-control"  required="required" placeholder="" id="ManPurchaseOrder" name="ManPurchaseOrder" readonly="true"/>
                            </div>
                        </div>
                        <div class="row col-sm-12" id="prikeytypeandvalue" style="display:none"> 
                            <div class="col-sm-6"><label class="labelw">Doc Type</label>
                                <s:textfield cssClass="form-control"  required="required" placeholder="" id="Manpri_key_type" name="Manpri_key_type" readonly="true"/>
                            </div>
                            <div class="col-sm-6"> <label class="labelw">Doc Number</label>
                                <s:textfield  cssClass="form-control"  required="required" placeholder="" id="Manpri_key_value" name="Manpri_key_value" readonly="true"/>
                            </div>
                        </div>
                        <div class="row col-sm-12">
                            <div class="col-sm-6"> <label class="labelw"> Doc Format</label>
                                <s:textfield  cssClass="form-control"  required="required" placeholder="" id="ManDocumentType" name="ManDocumentType" readonly="true"/>
                            </div>
                            <div class="col-sm-6"> <label class="labelw"> Transaction Type</label>
                                <s:textfield cssClass="form-control"  required="required" placeholder="" id="ManTransactionType" name="ManTransactionType" readonly="true"/>
                            </div>
                        </div>
                        <br>
                        <div id="senderinfo">
                            <div class="row col-sm-12">
                                <div class="col-sm-6"> <h4>Sender Info:</h4></div>
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
                                <div class="col-sm-6"> <h4>Receiver Info:</h4></div>
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
                            <div class="col-sm-6"> <label class="labelw"> ST</label>
                                <s:textfield  cssClass="form-control"  required="required" placeholder="" id="ManSt" name="ManSt" readonly="true"/>
                            </div>
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
                        <div class="row col-sm-12" id="errorReportDiv" style="display: none">
                            <div class="col-sm-6"> <label class="labelw">  Error&nbsp;Report </label></div>
                            <div class="col-sm-6"><div id="ErrReport"></div></div>
                        </div>
                        <%--<div class="row col-sm-12 clear" style="visibility: hidden">
                            <div class="col-sm-6"> <label class="labelw"> SAP_USER </label>
                                <s:textfield  cssClass="form-control"  required="required" placeholder="" id="SAP_USER" name="ManStatus" readonly="true"/>
                            </div>
                            <div class="col-sm-6"> <label class="labelw">  IDOC_NUMBER </label>
                                <s:textfield  cssClass="form-control"  required="required" placeholder="" id="IDOC_NUMBER" name="ManStatus" readonly="true"/>
                            </div>
                            <div class="col-sm-6"> <label class="labelw">  PO_NUMBER </label>
                                <s:textfield  cssClass="form-control"  required="required" placeholder="" id="PO_NUMBER" name="ManStatus" readonly="true"/>
                            </div>
                            <div class="col-sm-6"><label class="labelw">  PO_DATE </label>
                                <s:textfield  cssClass="form-control"  required="required" placeholder="" id="PO_DATE" name="ManStatus" readonly="true"/>
                            </div>
                            <div class="col-sm-6"> <label class="labelw"> IDOC_STATUS_CODE </label>
                                <s:textfield  cssClass="form-control"  required="required" placeholder="" id="IDOC_STATUS_CODE" name="ManStatus" readonly="true"/>
                            </div>
                            <div class="col-sm-6"> <label class="labelw">  IDOC_STATUS_DESCRIPTION </label>
                                <s:textfield  cssClass="form-control"  required="required" placeholder="" id="IDOC_STATUS_DESCRIPTION" name="ManStatus" readonly="true"/>
                            </div>
                        </div> --%>
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

        </script>
    </body>
</html>