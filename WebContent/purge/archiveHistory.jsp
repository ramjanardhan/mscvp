<%@page import="com.mss.ediscv.purge.PurgeHistoryBean"%>
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
                $("#purging").addClass("active");
                $("#archiveHistory").addClass("active");
                $("#archiveHistory i").addClass("text-red");

                // document.getElementById('loadingAcoountSearch').style.display = "none";
            }

            $(function() {
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
                    Archive History
                    <!--                    <small>History</small>-->
                </h1>
                <!--                <ol class="breadcrumb">
                                    <li><a href="#"><i class="fa fa-remove"></i>History</a></li>
                                    <li class="active">Archive History</li>
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

                                <s:form action="../purge/arcHis.action" method="post" name="arcHisForm" id="arcHisForm" theme="simple">
                                    <div class="form-group">
                                        <div class="row">
                                            <div class="col-sm-12">
                                                <div class="row">

                                                    <%
                                                        //REQ_RESULT_MSG
                                                        if (request.getAttribute(AppConstants.REQ_RESULT_MSG) != null) {
                                                            String responseString = request.getAttribute(AppConstants.REQ_RESULT_MSG).toString();
                                                            //request.getSession(false).removeAttribute("responseString");
                                                            out.println(responseString);
                                                        }
                                                    %>
                                                    <s:hidden name="datepickerfrom" id="datepickerfrom" value=""/>
                                                    <s:hidden name="datepicker" id="datepicker" value=""/>

                                                    <s:hidden name="sampleValue" id="sampleValue" value="2"/>
                                                    <div class="col-sm-3"> <label>Date Range<font color="red">*</font></label>
                                                            <s:textfield name="reportrange"  id="reportrange" cssClass="form-control pull-left"    value="%{reportrange}" onchange="Date1()" tabindex="1"/> 
                                                    </div>
                                                    <div class="col-sm-3">
                                                        <label>Transaction&nbsp;Type<font color="red">*</font></label>

                                                        <s:select headerKey="-1" headerValue="Select Type" cssClass="form-control"  list="flowName"  name="transType" id="transType" tabindex="2"/> 
                                                        <%--   <%
                                                               String flowId=session.getAttribute("userDefaultFlowID").toString();
                                                               if(flowId.equals("2"))
                                                               {%>
                                                       <s:select headerKey="-1" headerValue="Select Type" cssClass="form-control" list="#@java.util.LinkedHashMap@{'850':'PO','856':'Shipments','810':'Invoice','820':'Payments'}" name="transType" id="transType" tabindex="2"/> 
                                                       <%} else if(flowId.equals("3")){%>
                                                         <s:select headerKey="-1" headerValue="Select Type" cssClass="form-control" list="#@java.util.LinkedHashMap@{'204':'Load Tender','990':'Response','214':'Shipment','210':'Invoice'}" name="transType" id="transType" tabindex="2"/> 
                                                       <%}%> --%>
                                                    </div>
                                                </div>
                                                


                                                <div class ="row">
                                                    <div class="col-sm-2"><s:submit value="Search History"  onclick="return checkValues();"  cssClass="btn btn-primary col-sm-12" tabindex="3"/></div>
                                                    <div class="col-sm-2"><strong><input type="button" value="Reset"   class="btn btn-primary col-sm-12" onclick="return resetvalues();" tabindex="4"/></strong></div>
                                                </div>


                                            </s:form>

                                        </div>
                                    </div>
                                </div>
                            </div></div></div>
            </section>
            <div id="gridDiv">  
                <s:if test="#session.archiveHistorylist!=null"> 

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
                                                                    java.util.List list = (java.util.List) session.getAttribute(AppConstants.ARCHIVEHISTORY_LIST);
                                                                    if (list.size() != 0) {
                                                                        PurgeHistoryBean purgeHistoryBean;
                                                                %>
                                                                <thead><tr>
                                                                        <th>User</th> 
                                                                        <th >Days Count</th>
                                                                        <th >Transaction Type</th>
                                                                        <th >Comments</th>
                                                                        <th >Archive Date</th>
                                                                </thead>
                                                                <tbody>
                                                                    <%
                                                                        for (int i = 0; i < list.size(); i++) {
                                                                            purgeHistoryBean = (PurgeHistoryBean) list.get(i);
                                                                    %>

                                                                    <tr>
                                                                        <td><%
                                                                            out.println(purgeHistoryBean.getUser());
                                                                            %></td>
                                                                        <td><%
                                                                            out.println(purgeHistoryBean.getDaysCount());
                                                                            %></td>
                                                                        <td><%
                                                                            out.println(purgeHistoryBean.getTransactionType());
                                                                            %></td>
                                                                        <td><%
                                                                            out.println(purgeHistoryBean.getComments());
                                                                            %></td>
                                                                        <td> <% out.println(purgeHistoryBean.getArchiveDate().toString().substring(0, purgeHistoryBean.getArchiveDate().toString().lastIndexOf(":")));%> </td>

                                                                    </tr>

                                                                    <%}%>

                                                                </tbody><%
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

                                                        </div></td></tr></table>
                                        </div></div>
                                </div>
                            </div></section>

                </s:if>   

            </div>
            <script>
                $(function() {
                //$("#example1").DataTable();
                $('#results').DataTable({
                "paging": true,
                        "lengthChange": true,
                        "searching": true,
                        "ordering": true,
                        "info": true,
                        "autoWidth": false,
                        order: [[0, 'desc']]
                });            </script>
        </div>

        <div>
            <s:include value="../includes/template/footer.jsp"/>
        </div>


        <script language="JavaScript" src='<s:url value="/includes/js/DateValidation.js"/>'></script>
        <script src='<s:url value="../includes/bootstrap/js/bootstrap.min.js"/>'></script>
        <script src='<s:url value="../includes/plugins/daterangepicker/daterangepicker.js"/>'></script>
        <script src='<s:url value="../includes/plugins/datatables/jquery.dataTables.min.js"/>'></script>
        <script src='<s:url value="../includes/plugins/datatables/dataTables.bootstrap.min.js"/>'></script>
        <script src='<s:url value="../includes/bootstrap/js/app.min.js"/>'></script>

        <script type="text/javascript">

                function checkValues() {
                var date = document.arcHisForm.reportrange.value;
                        var transType = document.getElementById("transType").value;
                        if (date == "")
                {
                alert("Please enter Date Range !!");
                        return false;
                }
                if (transType == "-1")
                {
                alert("Please select Transaction Type !!");
                        return false;
                }
                }
        function Date1()
        {
        var date = document.arcHisForm.reportrange.value;
                var arr = date.split("-");
                var x = arr[1].trim();
                document.getElementById("datepickerfrom").value = arr[0];
                document.getElementById("datepicker").value = x;
        }

        function resetvalues()
        {
        document.getElementById('reportrange').value = "";
                document.getElementById('transType').value = "-1";
                //document.getElementById("datepickerfrom").value = "";
                //document.getElementById("datepicker").value = "";

                $('#gridDiv').hide();
        }

        </script>
    </body>

</html>
