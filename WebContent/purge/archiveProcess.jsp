<%@page import="com.mss.ediscv.logisticsloadtendering.LogisticsLoadBean"%>
<%-- <%@ page contentType="text/html" pageEncoding="UTF-8"%> --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page buffer="50kb" autoFlush="true" %>
<%@ taglib prefix="s" uri="/struts-tags" %>


<%@ taglib uri="/WEB-INF/tlds/dbgrid.tld" prefix="grd"%>
<%@ page import="com.freeware.gridtag.*"%>
<%@page import="java.sql.Connection"%>
<%@ page import="com.mss.ediscv.util.AppConstants"%>
<%@ page import="com.mss.ediscv.util.ConnectionProvider"%>
<%@ page import="java.sql.SQLException"%>
<%@ page import = "java.util.ResourceBundle" %>

<!DOCTYPE html>
<html class=" js canvas canvastext geolocation crosswindowmessaging no-websqldatabase indexeddb hashchange historymanagement draganddrop websockets rgba hsla multiplebgs backgroundsize borderimage borderradius boxshadow opacity cssanimations csscolumns cssgradients no-cssreflections csstransforms no-csstransforms3d csstransitions video audio localstorage sessionstorage webworkers applicationcache svg smil svgclippaths fontface">
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
        <%-- <script language="JavaScript"
        src='<s:url value="/includes/js/generalValidations.js"/>'></script> --%>
        <script>
            function doOnLoad()
            {
                $("#purging").addClass("active");
                $("#archiveProcess").addClass("active");
                $("#archiveProcess i").addClass("text-red");

                document.getElementById('loadingAcoountSearch').style.display = "none";
            }
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
    <body onload="doOnLoad();" class="hold-transition skin-blue sidebar-mini">
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
                    Archive Process
                    <!-- <small>Archiving</small>-->
                </h1>
                <!-- <ol class="breadcrumb">
                <li><a ><i class="fa fa-remove"></i>Archiving</a></li>
                <li class="active">Archive Process</li>
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
                                <div style="alignment-adjust:central;">
                                    <%
                                        //REQ_RESULT_MSG
                                        if (request.getAttribute(AppConstants.REQ_RESULT_MSG) != null) {
                                            String responseString = request.getAttribute(AppConstants.REQ_RESULT_MSG).toString();
                                            //request.getSession(false).removeAttribute("responseString");
                                            out.println(responseString);
                                        }
                                    %>
                                </div>
                                <s:form action="../purge/archiveProcess.action" method="post" name="arcProcessForm" id="arcProcessForm" theme="simple">
                                    <div class="form-group">
                                        <div class="row">
                                            <div class="col-sm-12">
                                                <div class="row">
                                                    <div class="col-sm-3">
                                                        <label>Day Count<font color="red">*</font></label>
                                                            <s:textfield cssClass="form-control" name="dayCount" id="dayCount" onblur="return validatenumber(this);" tabindex="1"/>
                                                    </div>
                                                    <div class="col-sm-3">
                                                        <label>Transaction&nbsp;Type<font color="red">*</font></label>

                                                        <s:select headerKey="-1" headerValue="Select Type" cssClass="form-control" list="flowName" name="transType" id="transType" tabindex="2"/> 
                                                        <%-- <%
                                                        String flowId=session.getAttribute("userDefaultFlowID").toString();
                                                        if(flowId.equals("2"))
                                                        {%>
                                                        <s:select headerKey="-1" headerValue="Select Type" cssClass="form-control" list="#@java.util.LinkedHashMap@{'850':'PO','856':'Shipments','810':'Invoice','820':'Payments'}" name="transType" id="transType" tabindex="2"/> 
                                                        <%} else if(flowId.equals("3")){%>
                                                        <s:select headerKey="-1" headerValue="Select Type" cssClass="form-control" list="#@java.util.LinkedHashMap@{'204':'Load Tender','990':'Response','214':'Shipment','210':'Invoice'}" name="transType" id="transType" tabindex="2"/> 
                                                        <%}%> --%>
                                                    </div>
                                                    <div class="col-sm-3">
                                                        <label>Comments<font color = "red">*</font></label>
                                                            <s:textfield cssClass="form-control" name="comments" id="commentId" tabindex="3" />

                                                    </div></div> </div></div>

                                        <div class="row"> 

                                            <div class="col-sm-2"><s:submit value="Archive Data" onclick="return checkValues();" cssClass="btn btn-primary col-sm-12" tabindex="4"/></div>

                                            <div class="col-sm-2"><strong><input type="reset" value="Reset" class="btn btn-primary col-sm-12" onclick="return resetvalues();" tabindex="5"/></strong></div>

                                            <s:hidden name="sampleValue" id="sampleValue" value="2"/>

                                        </s:form>

                                    </div>
                                </div>
                            </div>
                        </div></div></div>
            </section>

            <script>
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
                        "scrollX": true
                    });
                });
            </script>
        </div>



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

        function checkValues() {
            var returnType = false;
            var days = document.getElementById('dayCount').value;
            var transactionType = document.getElementById('transType').value;
            var comment = document.getElementById('commentId').value;
            if (days == "") {
                alert("Please enter Day Count !!");
                return false;
            }
            if (transactionType == "-1") {
                alert(" Please select Transaction Type !!");
                return false;
            }
            if (comment == "") {
                alert(" Please add your Comments !!");
                return false;
            }
            var r = confirm("Confirm to Archive " + days + " days post " + transactionType + " transaction records");
            if (r == true)
            {
                returnType = true;
            } else
            {
                returnType = false;
            }
            return returnType;

        }


    </script>
</body>

</html>