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
<%--<%@page language="java" contentType="text/html; 
charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>--%>
<%@page buffer="50kb" autoFlush="true" %>

<html>
    <head>
        <title>Miracle Supply Chain Visibility portal</title>
        <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
        <link rel="stylesheet" href='<s:url value="/includes/plugins/datatables/dataTables.bootstrap.css"/>' type="text/css">
        <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>
        <script language="JavaScript" src='<s:url value="/includes/js/jquery-1.9.1.js"></s:url>'></script>
        <script language="JavaScript" src='<s:url value="/includes/js/jquery-ui.js"/>'></script>
        <script language="JavaScript" src='<s:url value="/includes/js/LfcAjax.js"/>'></script>
        <script type="text/javascript">
            $(function() {
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
                    <!--<small>Manufacturing</small>-->
                </h1>
                <!--                <ol class="breadcrumb">
                                    <li><a href="#"><i class="fa fa-wrench"></i>Manufacturing</a></li>
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
                                        <table align="left" width="100%" border="0" cellpadding="0" cellspacing="0">
                                            <tr>
                                                <td style="background-color: white;">

                                                    <table id="results"  class="table table-bordered table-hover">
                                                        <%
                                                            //LifecycleBeans lifecycleBeans = (LifecycleBeans) 
                                                            // PoLifecycleBean poLifeCycleBean = (PoLifecycleBean) session.getAttribute(AppConstants.SES_LifecycleBeansList);
                                                       /* AsnLifecycleBean asnLifecycleBean = (AsnLifecycleBean) lifecycleBeans.getAsnLifecycleBean();
                                                             InvoiceLifecycleBean invoiceLifecycleBean = (InvoiceLifecycleBean) lifecycleBeans.getInvoiceLifecycleBean();
                                                             PaymentLifecycleBean paymentLifecycleBean = (PaymentLifecycleBean) lifecycleBeans.getPaymentLifecycleBean();*/
                                                            java.util.List list = (java.util.List) session.getAttribute(AppConstants.LFC_SES_PO_LIST);
                                                            //   out.println("NAG-----PO LIST size--->"+list.size()+"\n");
                                                            System.out.println("list.size()-->" + list.size());
                                                            if (list.size() != 0) {
                                                                PoLifecycleBean poLifeCycleBean;
                                                        %>

                                                        <thead>  <tr>
                                                                <th>DateTime</th> 
                                                                <th>Transaction Type</th>
                                                                <th>PO #</th>
                                                                <th>InstanceId</th>
                                                                <th>Type</th>
                                                                <th>Direction</th>
                                                                    <%--<td >SENDER_ID</td>
                                                                    <td >RECEIVER_ID</td>   --%>
                                                                <th>Status</th>
                                                                <th>Ack Status</th> 
                                                                <th>Reprocess</th> 
                                                            </tr></thead>
                                                        <tbody>
                                                            <%
                                                                for (int i = 0; i < list.size(); i++) {
                                                                    poLifeCycleBean = (PoLifecycleBean) list.get(i);

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
                                                                        if (poLifeCycleBean.getDatetimeRec() != null && !"".equals(poLifeCycleBean.getDatetimeRec())) {
                                                                            out.println(poLifeCycleBean.getDatetimeRec().toString().substring(0, poLifeCycleBean.getDatetimeRec().toString().lastIndexOf(":")));
                                                                        } else {
                                                                            out.println("-");
                                                                        }
                                                                    %>
                                                                </td> 
                                                                <td>
                                                                    <%
                                                                        if (poLifeCycleBean.getTranType() != null && !"".equals(poLifeCycleBean.getTranType())) {
                                                                            out.println(poLifeCycleBean.getTranType());
                                                                        } else {
                                                                            out.println("-");
                                                                        }

                                                                    %>
                                                                </td>

                                                                <td><a style="color: deepskyblue" href="javascript:getDetails('<%=poLifeCycleBean.getPoNumber()%>','<%=poLifeCycleBean.getFileId()%>','PO');">
                                                                        <%
                                                                            if (poLifeCycleBean.getPoNumber() != null && !"".equals(poLifeCycleBean.getPoNumber())) {
                                                                                out.println(poLifeCycleBean.getPoNumber());
                                                                            } else {
                                                                                out.println("-");
                                                                            }
                                                                        %></a>

                                                                </td>
                                                                <td>
                                                                    <%
                                                                        if (poLifeCycleBean.getFileId() != null && !"".equals(poLifeCycleBean.getFileId())) {
                                                                            out.println(poLifeCycleBean.getFileId());
                                                                        } else {
                                                                            out.println("-");
                                                                        }
                                                                    %>
                                                                </td>
                                                                <td>
                                                                    <%
                                                                        if (poLifeCycleBean.getFileType() != null && !"".equals(poLifeCycleBean.getFileType())) {
                                                                            out.println(poLifeCycleBean.getFileType());
                                                                        } else {
                                                                            out.println("-");
                                                                        }
                                                                    %>
                                                                </td>



                                                                <td>
                                                                    <%
                                                                        System.out.print("poLifeCycleBean.getDirection().toUpperCase() -->" + poLifeCycleBean.getDirection().toUpperCase());
                                                                        System.out.print("poLifeCycleBean.getDirection() -->" + poLifeCycleBean.getDirection());
                                                                        if (poLifeCycleBean.getDirection() != null && !"".equals(poLifeCycleBean.getDirection())) {
                                                                            out.println(poLifeCycleBean.getDirection().toUpperCase());
                                                                        } else {
                                                                            out.println("-");
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
                                                                        if (poLifeCycleBean.getStatus().equalsIgnoreCase("ERROR")) {
                                                                            out.println("<font color='red'>" + poLifeCycleBean.getStatus().toUpperCase() + "</font>");
                                                                        } else if (poLifeCycleBean.getStatus().equalsIgnoreCase("SUCCESS")) {
                                                                            out.println("<font color='green'>" + poLifeCycleBean.getStatus().toUpperCase() + "</font>");
                                                                        } else {
                                                                            out.println("<font color='orange'>" + poLifeCycleBean.getStatus().toUpperCase() + "</font>");
                                                                        }

                                                                    %>
                                                                </td>
                                                                <td>
                                                                    <%                                                                        //out.println(poLifeCycleBean.getAckStatus());
                                                                        if (poLifeCycleBean.getAckStatus().equalsIgnoreCase("REJECTED")) {
                                                                            out.println("<font color='red'>" + poLifeCycleBean.getAckStatus().toUpperCase() + "</font>");
                                                                        } else if (poLifeCycleBean.getAckStatus().equalsIgnoreCase("ACCEPTED")) {
                                                                            out.println("<font color='green'>" + poLifeCycleBean.getAckStatus().toUpperCase() + "</font>");
                                                                        } else {
                                                                            out.println("<font color='orange'>" + poLifeCycleBean.getAckStatus().toUpperCase() + "</font>");
                                                                        }

                                                                    %>
                                                                </td> 
                                                                <td>
                                                                    <%                                                                        // out.println(poLifeCycleBean.getReProcessStatus());
                                                                        if (poLifeCycleBean.getReProcessStatus() != null && !"".equals(poLifeCycleBean.getReProcessStatus())) {
                                                                            out.println(poLifeCycleBean.getReProcessStatus().toUpperCase());

                                                                        } else {
                                                                            out.println("-");
                                                                        }
                                                                    %>
                                                                </td>

                                                            </tr>
                                                            <%} //PO FOR loop END
                                                            %>

                                                            <%-- Po End--%>
                                                            <%-- ASN start --%>
                                                            <%
                                                                java.util.List asnlist = (java.util.List) session.getAttribute(AppConstants.LFC_SES_ASN_LIST);
                                                                // out.println("NAG-----ASN LIST size--->"+list.size()+"\n");
                                                                System.out.println("asnlist.size()-->" + asnlist.size());
                                                                if (asnlist.size() != 0) {
                                                                    AsnLifecycleBean asnLifecycleBean;
                                                            %>

                                                            <%
                                                                for (int j = 0; j < asnlist.size(); j++) {
                                                                    asnLifecycleBean = (AsnLifecycleBean) asnlist.get(j);

                                                                    if (j % 2 == 0) {
                                                                        cssValue = "whiteStripe";
                                                                    } else {
                                                                        cssValue = "grayEditSelection";
                                                                    }
                                                            %>

                                                            <%-- Asn Start--%>
                                                            <tr>
                                                                <td>
                                                                    <%
                                                                        if (asnLifecycleBean.getDatetimeRec() != null && !"".equals(asnLifecycleBean.getDatetimeRec())) {
                                                                            out.println(asnLifecycleBean.getDatetimeRec().toString().substring(0, asnLifecycleBean.getDatetimeRec().toString().lastIndexOf(":")));
                                                                        } else {
                                                                            out.println("-");
                                                                        }
                                                                    %>
                                                                </td>
                                                                <td>
                                                                    <%
                                                                        if (asnLifecycleBean.getTranType() != null && !"".equals(asnLifecycleBean.getTranType())) {
                                                                            out.println(asnLifecycleBean.getTranType());
                                                                        } else {
                                                                            out.println("-");
                                                                        }

                                                                    %>
                                                                </td>

                                                                <td><a style="color: deepskyblue" href="javascript:getDetails('<%=asnLifecycleBean.getPoNumber()%>','<%=asnLifecycleBean.getFileId()%>','ASN');">
                                                                        <%
                                                                            if (asnLifecycleBean.getPoNumber() != null && !"".equals(asnLifecycleBean.getPoNumber())) {
                                                                                out.println(asnLifecycleBean.getPoNumber());
                                                                            } else {
                                                                                out.println("-");
                                                                            }
                                                                        %>
                                                                    </a>
                                                                </td>
                                                                <td>
                                                                    <%
                                                                        if (asnLifecycleBean.getFileId() != null && !"".equals(asnLifecycleBean.getFileId())) {
                                                                            out.println(asnLifecycleBean.getFileId());
                                                                        } else {
                                                                            out.println("-");
                                                                        }
                                                                    %>
                                                                </td>
                                                                <td>
                                                                    <%
                                                                        if (asnLifecycleBean.getFileType() != null && !"".equals(asnLifecycleBean.getFileType())) {
                                                                            out.println(asnLifecycleBean.getFileType());
                                                                        } else {
                                                                            out.println("-");
                                                                        }
                                                                    %>
                                                                </td>
                                                                <td>
                                                                    <%
                                                                        if (asnLifecycleBean.getDirection() != null && !"".equals(asnLifecycleBean.getDirection())) {
                                                                            out.println(asnLifecycleBean.getDirection().toUpperCase());
                                                                        } else {
                                                                            out.println("-");
                                                                        }
                                                                    %>
                                                                </td>
                                                                <td>
                                                                    <%
                                                                        if (asnLifecycleBean.getStatus().equalsIgnoreCase("ERROR")) {
                                                                            out.println("<font color='red'>" + asnLifecycleBean.getStatus().toUpperCase() + "</font>");
                                                                        } else if (asnLifecycleBean.getStatus().equalsIgnoreCase("SUCCESS")) {
                                                                            out.println("<font color='green'>" + asnLifecycleBean.getStatus().toUpperCase() + "</font>");
                                                                        } else {
                                                                            out.println("<font color='orange'>" + asnLifecycleBean.getStatus().toUpperCase() + "</font>");
                                                                        }
                                                                    %>
                                                                </td>
                                                                <td>
                                                                    <%
                                                                        // out.println(asnLifecycleBean.getAckStatus());
                                                                        if (asnLifecycleBean.getAckStatus().equalsIgnoreCase("REJECT")) {
                                                                            out.println("<font color='red'>" + asnLifecycleBean.getAckStatus().toUpperCase() + "</font>");
                                                                        } else if (asnLifecycleBean.getAckStatus().equalsIgnoreCase("ACCEPTED")) {
                                                                            out.println("<font color='green'>" + asnLifecycleBean.getAckStatus().toUpperCase() + "</font>");
                                                                        } else {
                                                                            out.println("<font color='orange'>" + asnLifecycleBean.getAckStatus().toUpperCase() + "</font>");
                                                                        }
                                                                    %>
                                                                </td>  

                                                                <td>
                                                                    <%
                                                                        // out.println(asnLifecycleBean.getReProcessStatus());
                                                                        if (asnLifecycleBean.getReProcessStatus() != null && !"".equals(asnLifecycleBean.getReProcessStatus())) {
                                                                            out.println(asnLifecycleBean.getReProcessStatus().toUpperCase());
                                                                        } else {
                                                                            out.println("-");
                                                                        }
                                                                    %>
                                                                </td>
                                                            </tr>
                                                            <%
                                                                    }// ASN for loop end

                                                                }// ASN if end
                                                            %>
                                                            <%-- INVOICE START --%>
                                                            <%
                                                                java.util.List invoicelist = (java.util.List) session.getAttribute(AppConstants.LFC_SES_INVOICE_LIST);
                                                                //  out.println("NAG-----INVOICE LIST size--->"+invoicelist.size()+"\n");
                                                                System.out.println("invoicelist.size()-->" + invoicelist.size());
                                                                if (invoicelist.size() != 0) {
                                                                    InvoiceLifecycleBean invoiceLifecycleBean;
                                                            %>
                                                            <%
                                                                for (int k = 0; k < invoicelist.size(); k++) {
                                                                    invoiceLifecycleBean = (InvoiceLifecycleBean) invoicelist.get(k);

                                                                    if (k % 2 == 0) {
                                                                        cssValue = "whiteStripe";
                                                                    } else {
                                                                        cssValue = "grayEditSelection";
                                                                    }
                                                            %>

                                                            <tr>
                                                                <td>
                                                                    <%
                                                                        if (invoiceLifecycleBean.getDatetimeRec() != null && !"".equals(invoiceLifecycleBean.getDatetimeRec())) {
                                                                            out.println(invoiceLifecycleBean.getDatetimeRec().toString().substring(0, invoiceLifecycleBean.getDatetimeRec().toString().lastIndexOf(":")));
                                                                        } else {
                                                                            out.println("-");
                                                                        }
                                                                    %>
                                                                </td> 
                                                                <td>
                                                                    <%
                                                                        if (invoiceLifecycleBean.getTranType() != null && !"".equals(invoiceLifecycleBean.getTranType())) {
                                                                            out.println(invoiceLifecycleBean.getTranType());
                                                                        } else {
                                                                            out.println("-");
                                                                        }
                                                                    %>
                                                                </td>


                                                                <td><a style="color: deepskyblue" href="javascript:getDetails('<%=invoiceLifecycleBean.getPoNumber()%>','<%=invoiceLifecycleBean.getFileId()%>','INV');">
                                                                        <%
                                                                            if (invoiceLifecycleBean.getPoNumber() != null && !"".equals(invoiceLifecycleBean.getPoNumber())) {
                                                                                out.println(invoiceLifecycleBean.getPoNumber());
                                                                            } else {
                                                                                out.println("-");
                                                                            }
                                                                        %></a>
                                                                </td>
                                                                <td>
                                                                    <%
                                                                        if (invoiceLifecycleBean.getFileId() != null && !"".equals(invoiceLifecycleBean.getFileId())) {
                                                                            out.println(invoiceLifecycleBean.getFileId());
                                                                        } else {
                                                                            out.println("-");
                                                                        }
                                                                    %>
                                                                </td>
                                                                <td>
                                                                    <%
                                                                        if (invoiceLifecycleBean.getFileType() != null && !"".equals(invoiceLifecycleBean.getFileType())) {
                                                                            out.println(invoiceLifecycleBean.getFileType());
                                                                        } else {
                                                                            out.println("-");
                                                                        }
                                                                    %>
                                                                </td>



                                                                <td>
                                                                    <%
                                                                        if (invoiceLifecycleBean.getDirection() != null && !"".equals(invoiceLifecycleBean.getDirection())) {
                                                                            out.println(invoiceLifecycleBean.getDirection().toUpperCase());
                                                                        } else {
                                                                            out.println("-");
                                                                        }
                                                                    %>
                                                                </td>
                                                                <%--  <td>
                                                                    <%
                                                                      out.println(invoiceLifecycleBean.getSenderId());
                                                                      %>
                                                                  </td>
                                                                  <td>
                                                                    <%
                                                                      out.println(invoiceLifecycleBean.getRecId());
                                                                      %>
                                                                  </td> --%>

                                                                <td>
                                                                    <%
                                                                        if (invoiceLifecycleBean.getStatus().equalsIgnoreCase("ERROR")) {
                                                                            out.println("<font color='red'>" + invoiceLifecycleBean.getStatus().toUpperCase() + "</font>");
                                                                        } else if (invoiceLifecycleBean.getStatus().equalsIgnoreCase("SUCCESS")) {
                                                                            out.println("<font color='green'>" + invoiceLifecycleBean.getStatus().toUpperCase() + "</font>");
                                                                        } else {
                                                                            out.println("<font color='orange'>" + invoiceLifecycleBean.getStatus().toUpperCase() + "</font>");
                                                                        }
                                                                    %>
                                                                </td>
                                                                <td>
                                                                    <%
                                                                        // out.println(invoiceLifecycleBean.getAckStatus());
                                                                        if (invoiceLifecycleBean.getAckStatus().equalsIgnoreCase("REJECT")) {
                                                                            out.println("<font color='red'>" + invoiceLifecycleBean.getAckStatus().toUpperCase() + "</font>");
                                                                        } else if (invoiceLifecycleBean.getAckStatus().equalsIgnoreCase("ACCEPTED")) {
                                                                            out.println("<font color='green'>" + invoiceLifecycleBean.getAckStatus().toUpperCase() + "</font>");
                                                                        } else {
                                                                            out.println("<font color='orange'>" + invoiceLifecycleBean.getAckStatus().toUpperCase() + "</font>");
                                                                        }
                                                                    %>
                                                                </td> 

                                                                <td>
                                                                    <%
                                                                        // out.println(invoiceLifecycleBean.getReProcessStatus());
                                                                        if (invoiceLifecycleBean.getReProcessStatus() != null && !"".equals(invoiceLifecycleBean.getReProcessStatus())) {
                                                                            out.println(invoiceLifecycleBean.getReProcessStatus().toUpperCase());
                                                                        } else {
                                                                            out.println("-");
                                                                        }
                                                                    %>
                                                                </td>
                                                            </tr>
                                                            <%-- INVOICE END --%>

                                                            <%
                                                                    }// INVOICE FOR
                                                                } //INVOICE IF
                                                            %>

                                                            <%-- payment start --%>
                                                            <%
                                                                java.util.List paymentlist = (java.util.List) session.getAttribute(AppConstants.LFC_SES_PAYMENT_LIST);
                                                                //out.println("NAG-----INVOICE LIST size--->"+paymentlist.size()+"\n");
                                                                System.out.println("paymentlist.size()-->" + paymentlist.size());
                                                                if (paymentlist.size() != 0) {
                                                                    PaymentLifecycleBean paymentLifecycleBean;
                                                            %>
                                                            <%
                                                                for (int l = 0; l < paymentlist.size(); l++) {
                                                                    paymentLifecycleBean = (PaymentLifecycleBean) paymentlist.get(l);

                                                                    if (l % 2 == 0) {
                                                                        cssValue = "whiteStripe";
                                                                    } else {
                                                                        cssValue = "grayEditSelection";
                                                                    }
                                                            %>

                                                            <%-- Payment Start--%>
                                                            <tr>
                                                                <td>
                                                                    <%
                                                                        if (paymentLifecycleBean.getDatetimeRec() != null && !"".equals(paymentLifecycleBean.getDatetimeRec())) {
                                                                            out.println(paymentLifecycleBean.getDatetimeRec().toString().substring(0, paymentLifecycleBean.getDatetimeRec().toString().lastIndexOf(":")));
                                                                        } else {
                                                                            out.println("-");
                                                                        }
                                                                    %>
                                                                </td>
                                                                <td>
                                                                    <%
                                                                        if (paymentLifecycleBean.getTranType() != null && !"".equals(paymentLifecycleBean.getTranType())) {
                                                                            out.println(paymentLifecycleBean.getTranType());
                                                                        } else {
                                                                            out.println("-");
                                                                        }

                                                                    %>
                                                                </td>

                                                                <td><a style="color: deepskyblue" href="javascript:getDetails('<%=paymentLifecycleBean.getPoNumber()%>','<%=paymentLifecycleBean.getFileId()%>','PAYMENT');">
                                                                        <%
                                                                            if (paymentLifecycleBean.getPoNumber() != null && !"".equals(paymentLifecycleBean.getPoNumber())) {
                                                                                out.println(paymentLifecycleBean.getPoNumber());
                                                                            } else {
                                                                                out.println("-");
                                                                            }
                                                                        %></a>
                                                                </td>
                                                                <td>
                                                                    <%
                                                                        if (paymentLifecycleBean.getFileId() != null && !"".equals(paymentLifecycleBean.getFileId())) {
                                                                            out.println(paymentLifecycleBean.getFileId());
                                                                        } else {
                                                                            out.println("-");
                                                                        }
                                                                    %>
                                                                </td>
                                                                <td>
                                                                    <%
                                                                        if (paymentLifecycleBean.getFileType() != null && !"".equals(paymentLifecycleBean.getFileType())) {
                                                                            out.println(paymentLifecycleBean.getFileType());
                                                                        } else {
                                                                            out.println("-");
                                                                        }
                                                                    %>
                                                                </td>
                                                                <td>
                                                                    <%
                                                                        if (paymentLifecycleBean.getDirection() != null && !"".equals(paymentLifecycleBean.getDirection())) {
                                                                            out.println(paymentLifecycleBean.getDirection().toUpperCase());
                                                                        } else {
                                                                            out.println("-");
                                                                        }
                                                                    %>
                                                                </td>
                                                                <%-- <td>
                                                                   <%
                                                                     out.println(paymentLifecycleBean.getSenderId());
                                                                     %>
                                                                 </td>
                                                                 <td>
                                                                   <%
                                                                     out.println(paymentLifecycleBean.getRecId());
                                                                     %>
                                                                 </td>  --%>

                                                                <td>
                                                                    <%
                                                                        if (paymentLifecycleBean.getStatus().equalsIgnoreCase("ERROR")) {
                                                                            out.println("<font color='red'>" + paymentLifecycleBean.getStatus().toUpperCase() + "</font>");
                                                                        } else if (paymentLifecycleBean.getStatus().equalsIgnoreCase("SUCCESS")) {
                                                                            out.println("<font color='green'>" + paymentLifecycleBean.getStatus().toUpperCase() + "</font>");
                                                                        } else {
                                                                            out.println("<font color='orange'>" + paymentLifecycleBean.getStatus().toUpperCase() + "</font>");
                                                                        }
                                                                    %>
                                                                </td>
                                                                <td>
                                                                    <%
                                                                        //out.println("hiiiii");
                                                                        //out.println(paymentLifecycleBean.getAckStatus());
                                                                        if (paymentLifecycleBean.getAckStatus().equalsIgnoreCase("REJECT")) {
                                                                            out.println("<font color='red'>" + paymentLifecycleBean.getAckStatus().toUpperCase() + "</font>");
                                                                        } else if (paymentLifecycleBean.getAckStatus().equalsIgnoreCase("ACCEPTED")) {
                                                                            out.println("<font color='green'>" + paymentLifecycleBean.getAckStatus().toUpperCase() + "</font>");
                                                                        } else {
                                                                            out.println("<font color='orange'>" + paymentLifecycleBean.getAckStatus().toUpperCase() + "</font>");
                                                                        }
                                                                    %>
                                                                </td>  

                                                                <td>
                                                                    <%
                                                                        if (paymentLifecycleBean.getReProcessStatus() != null && !"".equals(paymentLifecycleBean.getReProcessStatus())) {
                                                                            out.println(paymentLifecycleBean.getReProcessStatus().toUpperCase());
                                                                        } else {
                                                                            out.println("-");
                                                                        }
                                                                    %>
                                                                </td>

                                                            </tr>
                                                            <%-- Payment End--%>

                                                            <%-- Payment END --%>
                                                            <%
                                                                    }//PAYMENT END FOR loop
                                                                }//PAYMENT ENF IF
                                                            } // PO if
                                                            else {
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
                                            <%                                                if (list.size() != 0) {
                                            %>
                                            <tr >

                                                <td align="right" colspan="28" style="background-color: white;">
                                                    <!--<div align="right" id="pageNavPosition"></div>-->
                                                </td>
                                            </tr>  <% }%>       </tbody>          
                                        </table>  


                                    </div>
                                    <%-- Process butttons  start --%>

                                    <%-- process buttons end--%>
                                    <%-- Grid End --%>

                                </div>
                            </div></div>
                    </div></section>

                <div id="hide-menu1" class="hide-menu message ">
                    <br>
                    <div class="row col-sm-12">


                        <div class="col-sm-6"> <label class="labelw">Instance Id</label>
                            <s:textfield  cssClass="form-control"  required="required" placeholder="" id="LfcInstanceId" name="LfcInstanceId" readonly="true"/>
                        </div>


                        <div class="col-sm-6"> <label class="labelw">PO #</label>
                            <s:textfield  cssClass="form-control"  required="required" placeholder="" id="LfcPONum" name="LfcPONum" readonly="true"/>
                        </div>
                    </div>
                   <div class="row col-sm-12"> 
                        <div class="col-sm-6"> <label class="labelw">Doc Type</label>
                            <s:textfield  cssClass="form-control"  required="required" placeholder="" id="LfcManpri_key_type" name="LfcManpri_key_type" readonly="true"/>
                        </div>
                        <div class="col-sm-6"> <label class="labelw">Doc Number</label>
                            <s:textfield  cssClass="form-control"  required="required" placeholder="" id="LfcManpri_key_value" name="LfcManpri_key_value" readonly="true"/>
                        </div>
                    </div>
                   <%--  <div class="row col-sm-12">
                        <div class="col-sm-6"> <label class="labelw">SO # </label>
                            <s:textfield  cssClass="form-control"  required="required" placeholder="" id="LfcSo" name="LfcSo" readonly="true"/>
                        </div>
                        <div class="col-sm-6"> <label class="labelw">PO Qty </label>
                            <s:textfield  cssClass="form-control"  required="required" placeholder="" id="LfcPOQty" name="POShipDate" readonly="true"/>
                        </div>
                    </div>--%>
                    <div class="row col-sm-12">
                        <div class="col-sm-6"> <label class="labelw">Transaction Type </label>
                            <s:textfield  cssClass="form-control"  required="required" placeholder="" id="LfcTransactionType" name="LfcTransactionType" readonly="true"/>
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
                            <div class="col-sm-6"> <label class="labelw">  Sender Id </label>
                                <s:textfield  cssClass="form-control"  required="required" placeholder="" id="LycPOPartnerId" name="LycPOPartnerId" readonly="true"/>
                            </div>
                            <div class="col-sm-6"> <label class="labelw"> Sender Name </label>
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
                            <div class="col-sm-6"> <label class="labelw"> Receiver Id </label>
                                <s:textfield  cssClass="form-control"  required="required" placeholder="" id="LfcPOReceiverId" name="LfcPOReceiverId" readonly="true"/>
                            </div>
                            <div class="col-sm-6"> <label class="labelw"> Receiver Name </label>
                                <s:textfield  cssClass="form-control"  required="required" placeholder="" id="LfcPOReceiverName" name="LfcPOReceiverName" readonly="true"/>
                            </div>
                        </div>
                    </div>  
                    <div class="row col-sm-12 clear">
                        <br>
                        <div class="col-sm-6"> <label class="labelw"> ISA # </label>
                            <s:textfield  cssClass="form-control"  required="required" placeholder="" id="LfcPOIsa" name="LfcPOIsa" readonly="true"/>
                        </div>
                           <div class="col-sm-6"> <label class="labelw"> GS </label>
                            <s:textfield  cssClass="form-control"  required="required" placeholder="" id="LfcGS_CONTROL_NUMBER" name="LfcGS_CONTROL_NUMBER" readonly="true"/>
                        </div>
                    </div>


                    <div class="row col-sm-12 clear">
                           <div class="col-sm-6"> <label class="labelw"> ST</label>
                            <s:textfield  cssClass="form-control"  required="required" placeholder="" id="LfcST_CONTROL_NUMBER" name="LfcST_CONTROL_NUMBER" readonly="true"/>
                        </div>
                           <div class="col-sm-6"> <label class="labelw"> ISA Date </label>
                            <s:textfield  cssClass="form-control"  required="required" placeholder="" id="LfcPOISADate" name="LfcPOISADate" readonly="true"/>
                        </div>
                   
                    </div>
                        
                           <div class="row col-sm-12">
                        <div class="col-sm-6"> <label class="labelw">  ISA Time </label>
                            <s:textfield  cssClass="form-control"  required="required" placeholder="" id="LfcPOIsATime" name="LfcPOIsATime" readonly="true"/>
                        </div>
                        <div class="col-sm-6"> <label class="labelw"> Status</label>
                            <s:textfield  cssClass="form-control"  required="required" placeholder="" id="LfcPOStatus" name="LfcPOStatus" readonly="true"/>
                        </div>
                    </div>
                    <br>
                    <div class="row col-sm-12" style="margin-top:10px;" >
                        <div class="col-sm-6"> <label class="labelw">  PreTranslation  </label></div>
                        <div class="col-sm-6"><div id="LfcPOPreTransition"></div></div>
                    </div>
                    <div class="row col-sm-12" >
                        <div class="col-sm-6"> <label class="labelw"> PostTranslation </label></div>
                        <div class="col-sm-6"><div id="LfcPOPostTransition"></div></div>
                    </div>
                    <div class="row col-sm-12" >
                        <div class="col-sm-6"> <label class="labelw">997ACKFile </label></div>                      
                        <div class="col-sm-6"><div id="LfcPOAckFileId"></div></div>
                    </div>


                    <div class="row col-sm-12" id="errorDiv" style="display: none">
                        <div class="col-sm-6"> <label class="labelw"> Error&nbsp;Message </label></div>
                        <div class="col-sm-6" id="InvErrormessage" style="color: red"></div>
                    </div>

                    <div id="noresult"></div>
                    <br>
                    <div class="row col-sm-12" style="margin-top:10px;">
                        <button type="button" class="btn btn-primary col-sm-11" style="margin-left:12px; " id="hide-menu" onclick="hide()" value="X">Close</button>
                    </div>
                </div>
            </div>
        </div>

        <div>
            <s:include value="../includes/template/footer.jsp"/>
        </div>

        <script src='<s:url value="../includes/plugins/datatables/jquery.dataTables.min.js"/>'></script>
        <script src='<s:url value="../includes/plugins/datatables/dataTables.bootstrap.min.js"/>'></script>
        <script src='<s:url value="../includes/bootstrap/js/app.min.js"/>'></script>

        <script type="text/javascript">
                            $(function() {
                                $('#attach_box').click(function() {
                                    $('#sec_box').show();
                                    return false;
                                });
                            });
                            $(function() {
                                $('#detail_link').click(function() {
                                    $('#detail_box').show();
                                    return false;
                                });
                            });
                            // New function to show the left grid
                            function demo() {
                                $(function() {
                                    $('#detail_box').show();
                                    return false;
                                });
                            }
                            function getDetails(ponum, fileid, type) {
                                var db = document.getElementById('database').value;
                                // alert("db jsp---->"+db);
                                getlfcPODetails(ponum, fileid, type, db);
                            }
                            function goBack() {
                                window.history.go(-1)
                            }

                            function hide() {
                                $('#hide-menu1').removeClass('show-menu');
                            }
                            //            $('body,html').click(function(e){
                            //                $('#hide-menu1').removeClass('show-menu');
                            //            });

                            function doOnLoad() {
                                $("#purchaseorder").addClass("active");
                                $("#oredermanagement").addClass("active");
                                $("#manufacturing").addClass("active");
                                $("#purchaseorder i").addClass("text-red");
                                // document.getElementById('loadingAcoountSearch').style.display = "none";
                            }
        </script>
    </body>
</html>

