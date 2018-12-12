<%@page import="com.mss.ediscv.lfc.LtTenderBean"%>
<%@page import="com.mss.ediscv.lfc.PaymentLifecycleBean"%>
<%@page import="com.mss.ediscv.lfc.InvoiceLifecycleBean"%>
<%@page import="com.mss.ediscv.lfc.AsnLifecycleBean"%>
<%@page import="com.mss.ediscv.lfc.PoLifecycleBean"%>
<%@page import="com.mss.ediscv.partner.PartnerBean"%>
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

<html>
    <head>
        <title>Miracle Supply Chain Visibility portal</title>
        <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
        <link rel="stylesheet" href='<s:url value="/includes/plugins/datatables/dataTables.bootstrap.css"/>' type="text/css">

        <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>


        <script language="JavaScript"
        src='<s:url value="/includes/js/jquery-1.9.1.js"></s:url>'></script>
            <script language="JavaScript"
            src='<s:url value="/includes/js/jquery-ui.js"/>'></script>
        <script language="JavaScript"
        src='<s:url value="/includes/js/LfcAjax.js"/>'></script>



        <script type="text/javascript">
            $(function () {
                $('#attach_box').click(function () {
                    $('#sec_box').show();
                    return false;
                });
            });
            $(function () {
                $('#detail_link').click(function () {
                    $('#detail_box').show();
                    return false;
                });
            });
            // New function to show the left grid
            function demo() {
                $(function () {
                    $('#detail_box').show();
                    return false;
                });
            }
            function getDetails(ponum, fileid, type) {
                getLtlfcPODetails(ponum, fileid, type);
            }
            function goBack()
            {
                window.history.go(-1)
            }
            function hide()
            {
                $('#hide-menu1').removeClass('show-menu');
            }
            //            $('body,html').click(function(e){
            //                $('#hide-menu1').removeClass('show-menu');
            //            });
            //            function doOnLoad() {
            //
            //                $("#purchaseorder").addClass("active");
            //                $("#oredermanagement").addClass("active");
            //
            //                $("#manufacturing").addClass("active");
            //
            //                $("#purchaseorder i").addClass("text-red");
            //                document.getElementById('loadingAcoountSearch').style.display = "none";
            //            }
            function doOnLoad()
            {
                $("#ltloadtendering").addClass("active");
                $("#logistics").addClass("active");
                $("#ltloadtendering i").addClass("text-red");
                document.getElementById('loadingAcoountSearch').style.display = "none";
            }
            
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


    </head>
    <body class="hold-transition skin-blue sidebar-mini"  onload="doOnLoad()">
        <div>
            <s:include value="../includes/template/header.jsp"/>
        </div>
        <div>
            <s:include value="../includes/template/sidemenu.jsp"/>
        </div>
        <%String contextPath = request.getContextPath();
        %>
        <div class="content-wrapper">
            <!-- Content Header (Page header) -->
            <!-- Main content --> 
            <section class="content-header">
                <h1>
                    LifeCycle
                    <!--<small>Logistics</small>-->
                </h1>
                <!--                <ol class="breadcrumb">
                                    <li><a href="#"><i class="fa fa-truck"></i>Logistics</a></li>
                                    <li class="active">LifeCycle</li>
                                </ol>-->
            </section>
            
            <div id="gridDiv">     
                <%!String cssValue = "whiteStripe";
                    int resultsetTotal;%>
                <section class="content">
                    <div class="row">
                        <div class="col-xs-12">
                            <div class="box">
                                <div class="box-header">
                                    <h3 class="box-title">Table</h3>
                                </div><!-- /.box-header -->
                                <div class="box-body">
                                    <div style="overflow-x:auto;">   
                                        <div align="right"><input type="button" value="Go back" class="btn btn-effect-ripple btn-primary" onclick="goBack()"></input></div>
                                            <s:hidden id="database" name="database" value="%{database}"/>
                                        <br>
                                        <table align="left" width="100%"
                                               border="0" cellpadding="0" cellspacing="0">
                                            <tr>
                                                <td style="background-color: white;">

                                                    <table id="results"  class="table table-bordered table-hover">
                                                        <%
                                                            java.util.List list = (java.util.List) session.getAttribute(AppConstants.LFC_SES_LTTENDER_LIST);
                                                            System.out.println("list------------   " + list.size());
                                                            if (list.size() != 0) {
                                                                LtTenderBean ltTenderBean;
                                                        %>

                                                        <thead>  <tr class="gridHeader">
                                                                <th>DateTime</th>
                                                                <th>Trans&nbsp;Type</th>
                                                                <th>Shipment&nbsp;#</th>
                                                                <th>Partner</th>
                                                                <th>Type</th>
                                                                <th>Direction</th>

                                                                <th>Status</th>
                                                                <th>Ack Status</th> 
                                                            </tr></thead>
                                                        <tbody>
                                                            <%
                                                                for (int i = 0; i < list.size(); i++) {
                                                                    ltTenderBean = (LtTenderBean) list.get(i);
                                                                    if (i % 2 == 0) {
                                                                        cssValue = "whiteStripe";
                                                                    } else {
                                                                        cssValue = "grayEditSelection";
                                                                    }
                                                            %>

                                                            <%-- Po Start--%>
                                                            <tr>
                                                                <td>
                                                                    <%
                                                                        if (ltTenderBean.getDatetime() != null) {
                                                                            out.println(ltTenderBean.getDatetime().toString().substring(0, ltTenderBean.getDatetime().toString().lastIndexOf(":")));
                                                                        } else {
                                                                            out.println("--");
                                                                        }
                                                                    %>
                                                                </td>
                                                                <td>
                                                                    <%
                                                                        if (ltTenderBean.getTran_type() != null) {
                                                                            out.println(ltTenderBean.getTran_type());
                                                                        } else {
                                                                            out.println("--");
                                                                        }
                                                                    %>
                                                                </td>

                                                                <td>
                                                                    <%
                                                                        if (ltTenderBean.getTran_type() != null) {
                                                                            if (ltTenderBean.getTran_type().equals("204")) {
                                                                    %>
                                                                    <a style="color: deepskyblue" class="linkColor" href="javascript:getDetails('<%=ltTenderBean.getShipmentid()%>','<%=ltTenderBean.getFileId()%>','LOADTENDER');">
                                                                        <%
                                                                            if (ltTenderBean.getShipmentid() != null) {
                                                                                out.println(ltTenderBean.getShipmentid());
                                                                            } else {
                                                                                out.println("--");
                                                                            }
                                                                        %></a>
                                                                        <% }
                                                                            if (ltTenderBean.getTran_type().equals("214")) {
                                                                        %>
                                                                    <a style="color: deepskyblue" class="linkColor" href="javascript:getDetails('<%=ltTenderBean.getShipmentid()%>','<%=ltTenderBean.getFileId()%>','SHIPMENT');">
                                                                        <%
                                                                            if (ltTenderBean.getShipmentid() != null) {
                                                                                out.println(ltTenderBean.getShipmentid());
                                                                            } else {
                                                                                out.println("--");
                                                                            }
                                                                        %></a>
                                                                        <% }
                                                                            if (ltTenderBean.getTran_type().equals("990")) {
                                                                        %>
                                                                    <a style="color: deepskyblue" class="linkColor" href="javascript:getDetails('<%=ltTenderBean.getShipmentid()%>','<%=ltTenderBean.getFileId()%>','RESPONSE');">
                                                                        <%
                                                                            if (ltTenderBean.getShipmentid() != null) {
                                                                                out.println(ltTenderBean.getShipmentid());
                                                                            } else {
                                                                                out.println("--");
                                                                            }
                                                                        %></a>
                                                                        <% }
                                                                            if (ltTenderBean.getTran_type().equals("210")) {
                                                                        %>
                                                                    <a style="color: deepskyblue" class="linkColor" href="javascript:getDetails('<%=ltTenderBean.getShipmentid()%>','<%=ltTenderBean.getFileId()%>','INVOICE');">
                                                                        <%
                                                                            if (ltTenderBean.getShipmentid() != null) {
                                                                                out.println(ltTenderBean.getShipmentid());
                                                                            } else {
                                                                                out.println("--");
                                                                            }
                                                                        %></a>
                                                                        <% }
                                                                            } else
                                                                                out.println("--");%> 

                                                                </td>
                                                                <td>
                                                                    <%
                                                                        if (ltTenderBean.getRecName() != null) {
                                                                            out.println(ltTenderBean.getRecName());
                                                                        } else {
                                                                            out.println("--");
                                                                        }
                                                                    %>
                                                                </td>
                                                                <td>
                                                                    <%
                                                                        if (ltTenderBean.getFileType() != null) {
                                                                            out.println(ltTenderBean.getFileType());
                                                                        } else {
                                                                            out.println("--");
                                                                        }
                                                                    %>
                                                                </td>
                                                                <td>
                                                                    <%
                                                                        if (ltTenderBean.getDirection() != null) {
                                                                            out.println(ltTenderBean.getDirection().toUpperCase());
                                                                        } else {
                                                                            out.println("--");
                                                                        }
                                                                    %>
                                                                </td>

                                                                <%--<td>
                                                                  <%
                                                                    out.println(poLifeCycleBean.getSenderId());
                                                                    %>
                                                                </td>
                                                                <td>
                                                                  <%
                                                                    out.println(poLifeCycleBean.getRecId());
                                                                    %>
                                                                </td>  --%>

                                                                <td>
                                                                    <%
                                                                        if (ltTenderBean.getStatus() != null) {
                                                                            if (ltTenderBean.getStatus().equalsIgnoreCase("ERROR")) {
                                                                                out.println("<font color='red'>" + ltTenderBean.getStatus().toUpperCase() + "</font>");
                                                                            } else if (ltTenderBean.getStatus().equalsIgnoreCase("SUCCESS")) {
                                                                                out.println("<font color='green'>" + ltTenderBean.getStatus().toUpperCase() + "</font>");
                                                                            } else {
                                                                                out.println("<font color='orange'>" + ltTenderBean.getStatus().toUpperCase() + "</font>");
                                                                            }
                                                                        } else {
                                                                            out.println("--");
                                                                        }
                                                                    %>
                                                                </td>
                                                                <td>
                                                                    <%
                                                                        if (ltTenderBean.getAckStatus() != null) {
                                                                            //out.println(poLifeCycleBean.getAckStatus());
                                                                            if (ltTenderBean.getAckStatus().equalsIgnoreCase("REJECTED")) {
                                                                                out.println("<font color='red'>" + ltTenderBean.getAckStatus().toUpperCase() + "</font>");
                                                                            } else if (ltTenderBean.getAckStatus().equalsIgnoreCase("ACCEPTED")) {
                                                                                out.println("<font color='green'>" + ltTenderBean.getAckStatus().toUpperCase() + "</font>");
                                                                            } else {
                                                                                out.println("<font color='orange'>" + ltTenderBean.getAckStatus().toUpperCase() + "</font>");
                                                                            }
                                                                        } else {
                                                                            out.println("--");
                                                                        }
                                                                    %>
                                                                </td> 

                                                                <%}
                                                                }//PAYMENT END FOR loop
                                                                //PAYMENT ENF IF
                                                                // PO if
                                                                else {
                                                                %>
                                                                <td>
                                                                    <%
                                                                            out.println("<img  border='0' align='top'  src='" + contextPath + "/includes/images/alert.gif'/><b>No records found for the given search criteria. Please try a different search criteria!</b>");
                                                                        }
                                                                    %>
                                                                </td>

                                                            </tr>

                                                        </tbody>          
                                                    </table>  
                                                </td>
                                            </tr>
                                        </table>

                                    </div>
                                    <%-- Process butttons  start --%>

                                    <%-- process buttons end--%>
                                    <%-- Grid End --%>

                                </div>
                            </div></div>
                    </div></section>
            </div>
            <div id="hide-menu1" class="hide-menu message ">

                <div class="row col-sm-12">
                    <br>
                    <div class="col-sm-6"> <label class="labelw">Instance Id </label>
                        <s:textfield  cssClass="form-control"  required="required" placeholder="" id="LfcInstanceId" name="LfcInstanceId" readonly="true"/>
                    </div>
                    <div class="col-sm-6"> <label class="labelw"> Shipment # </label>
                        <s:textfield cssClass="form-control" required="required" placeholder="" id="LfcShipment" name="LfcShipment" readonly="true"/>
                    </div>

                    <%--                    <div class="col-sm-6"> <label class="labelw">PO # </label>
                                            <s:textfield  cssClass="form-control"  required="required" placeholder="" id="LfcPONum" name="LfcPONum" readonly="true"/>
                                        </div>--%> </div>

                <%--          <div class="col-sm-6"> <label class="labelw">PO Date :</label>
                              <s:textfield  cssClass="form-control"  required="required" placeholder="" id="LfcPODates" name="LfcPODates" readonly="true"/>
                          </div>
                          <div class="col-sm-6"> <label class="labelw">PO Status :</label>
                              <s:textfield  cssClass="form-control"  required="required" placeholder="" id="LfcStatus1" name="LfcStatus1" readonly="true"/>
                          </div>
                          <div class="col-sm-6"> <label class="labelw">SO # :</label>
                              <s:textfield  cssClass="form-control"  required="required" placeholder="" id="LfcSo" name="LfcSo" readonly="true"/>
                          </div>
                          <div class="col-sm-6"> <label class="labelw">PO Qty :</label>
                              <s:textfield  cssClass="form-control"  required="required" placeholder="" id="LfcPOQty" name="POShipDate" readonly="true"/>
                          </div> 
                <div class="row col-sm-12"> 
                    <div class="col-sm-6"> <label class="labelw">Trans Type </label>
                        <s:textfield  cssClass="form-control"  required="required" placeholder="" id="LfcTransactionType" name="LfcTransactionType" readonly="true"/>
                    </div>
                </div>--%>
                <div class="row col-sm-12">
                    <div class="col-sm-6"> <label class="labelw"> Document Type </label>
                        <s:textfield cssClass="form-control" required="required" placeholder="" id="LfcDocumentType" name="LfcDocumentType" readonly="true"/>
                    </div>
                    <div class="col-sm-6"> <label class="labelw">Transaction Type </label>
                        <s:textfield cssClass="form-control" required="required" placeholder="" id="LfcTransactionType" name="LfcTransactionType" readonly="true"/>
                    </div>
                </div>    
                <br>
                <div id="senderinfo">
                    <div class="row col-sm-12">
                        <div class="col-sm-6"> <h4 style="color: deepskyblue">Seder Info :</h4></div>
                        <div class="col-sm-6"></div>
                        <div class="col-sm-6"></div>

                    </div>
                    <br>
                    <div class="row col-sm-12">
                        <div class="col-sm-6"> <label class="labelw">  Id </label>
                            <s:textfield  cssClass="form-control"  required="required" placeholder="" id="LycPOPartnerId" name="LycPOPartnerId" readonly="true"/>
                        </div>
                        <div class="col-sm-6"> <label class="labelw"> Name </label>
                            <s:textfield  cssClass="form-control"  required="required" placeholder="" id="LycPOPartnerName" name="LycPOPartnerName" readonly="true"/>
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
                            <s:textfield  cssClass="form-control"  required="required" placeholder="" id="LfcPOReceiverId" name="LfcPOReceiverId" readonly="true"/>
                        </div>
                        <div class="col-sm-6"> <label class="labelw"> Name </label>
                            <s:textfield  cssClass="form-control"  required="required" placeholder="" id="LfcPOReceiverName" name="LfcPOReceiverName" readonly="true"/>
                        </div>
                    </div>
                </div> 

                <div class="row col-sm-12 clear">
                    <br/>
                    <div class="col-sm-6"> <label class="labelw"> ISA # </label>
                        <s:textfield  cssClass="form-control"  required="required" placeholder="" id="LfcPOIsa" name="LfcPOIsa" readonly="true"/>
                    </div>
                    <div class="col-sm-6"> <label class="labelw"> GS # </label>
                        <s:textfield cssClass="form-control" required="required" placeholder="" id="LofcGs" name="LofcGs" readonly="true"/>
                    </div>
                    <div class="col-sm-6"> <label class="labelw">  ST # </label>
                        <s:textfield cssClass="form-control" required="required" placeholder="" id="LfccSt" name="LfccSt" readonly="true"/>
                    </div>
                    <div class="col-sm-6"> <label class="labelw"> ISA Date  </label>
                        <s:textfield  cssClass="form-control"  required="required" placeholder="" id="LfcPOISADate" name="LfcPOISADate" readonly="true"/>
                    </div>
                    <div class="col-sm-6"> <label class="labelw">  ISA TIME  </label>
                        <s:textfield  cssClass="form-control"  required="required" placeholder="" id="LfcPOIsATime" name="LfcPOIsATime" readonly="true"/>
                    </div>
                    <div class="col-sm-6"> <label class="labelw"> STATUS </label>
                        <s:textfield  cssClass="form-control"  required="required" placeholder="" id="LfcPOStatus" name="LfcPOStatus" readonly="true"/>
                    </div>
                </div>
                <div class="row col-sm-12" style="margin-top:10px;">
                    <div class="col-sm-6"> <label class="labelw">  PreTranslation  </label></div>
                    <div class="col-sm-6"><div id="LfcPOPreTransition"></div></div>
                </div>
                <div class="row col-sm-12">
                    <div class="col-sm-6"> <label class="labelw"> PostTranslation </label></div>
                    <div class="col-sm-6"><div id="LfcPOPostTransition"></div></div>
                </div>
                <div class="row col-sm-12" >
                    <div class="col-sm-6"> <label class="labelw"> 997 ACKFile </label></div>
                    <div class="col-sm-6"><div id="LfcPOAckFileId"></div>
                    </div>
                </div>
                <%--  <div class="col-sm-6"> <label class="labelw">997ACKFile </label></div>   --%>                   

                <div class="row col-sm-12" id="errorDiv" style="display: none">
                    <div class="col-sm-6"> <label class="labelw"> Error&nbsp;Message </label></div>
                    <div class="col-sm-6" id="InvErrormessage" style="color: red"></div>
                </div>

                <div id="noresult"></div>
                <div class="row col-sm-12" style="margin-top:10px;">
                    <button type="button" class="btn btn-primary col-sm-11" style="margin-left:12px; " id="hide-menu" onclick="hide()" value="X">Close</button>
                </div>
            </div>
        </div>



        <%--  <div id="footer">
           <h2><font color="white">&#169 2013 Miracle Software Systems, Inc. All rights reserved</font></h2>
                  </div>--%>

        <script src='<s:url value="../includes/plugins/datatables/jquery.dataTables.min.js"/>'></script>
        <script src='<s:url value="../includes/plugins/datatables/dataTables.bootstrap.min.js"/>'></script>
        <script src='<s:url value="../includes/bootstrap/js/app.min.js"/>'></script>

        <div>
            <s:include value="../includes/template/footer.jsp"/>
        </div>
    </body>
</html>