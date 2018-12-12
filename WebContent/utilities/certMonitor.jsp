<%@page import="java.util.Collection"%>
<%@page import="java.util.Set"%>
<%@page import="java.util.LinkedHashMap"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.List"%>
<%@page import="com.mss.ediscv.documentVisibility.DocumentVisibilityBean"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page buffer="50kb" autoFlush="true" %>
<%@ taglib uri="/WEB-INF/tlds/dbgrid.tld" prefix="grd"%>
<%@ page import="com.freeware.gridtag.*"%>
<%@page import="java.sql.Connection"%>
<%@  page import="com.mss.ediscv.util.AppConstants"%>
<%@ page import="com.mss.ediscv.util.ConnectionProvider"%>
<%@ page import="java.sql.SQLException"%>

<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8">
        <title>Miracle Supply Chain Visibility Portal</title>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
        <meta http-equiv="pragma" content="no-cache" />
        <meta http-equiv="cache-control" content="no-cache" />
        <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
        <link rel="stylesheet" href='<s:url value="/includes/bootstrap/css/userdefined.css"/>'>
        <script type="text/javascript" src="//cdn.jsdelivr.net/bootstrap.daterangepicker/2/daterangepicker.js"></script>
        <link rel="stylesheet" href='<s:url value="/includes/plugins/datatables/dataTables.bootstrap.css"/>' type="text/css">
        <link rel="stylesheet" href='<s:url value="/includes/plugins/daterangepicker/daterangepicker.css"/>' type="text/css">
        <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>
        <script>
            function doOnLoad()
            {

                $("#utilities").addClass("active");
                $("#certMonitoring").addClass("active");
                $("#certMonitoring i").addClass("text-red");
                document.getElementById('loadingAcoountSearch').style.display = "none";

            }
            $(function () {
                $('#results').DataTable({
                    "paging": true,
                    "lengthChange": true,
                    "searching": true,
                    "ordering": true,
                    "info": true,
                    "autoWidth": false,
                    "scrollX": true,
                    "scrollY": 300
                });
            });
        </script>
        <style>
            .row{
                margin-bottom: 5px; 
            }
        </style>


    </head>

    <body class="hold-transition skin-blue sidebar-mini" onload="doOnLoad()">    
        <script type="text/javascript" src='<s:url value="/includes/js/wz_tooltip.js"/>'></script>
        <div>
            <s:include value="../includes/template/header.jsp"/>
        </div>
        <div>
            <s:include value="../includes/template/sidemenu.jsp"/>
        </div>
        <!-- Content Wrapper. Contains page content -->
        <div class="content-wrapper">
            <section class="content-header">
                <h1>Search Certificate Monitoring
                    <!--                    <small>Certificate Monitoring</small>-->
                </h1>
                <!--                <ol class="breadcrumb">
                                    <li><a href="#"><i class="fa fa-gavel"></i>Certificate Monitoring</a></li>
                                    <li class="active">Search Certificate Monitoring</li>
                                </ol>-->
            </section>



            <section class="content">

                <div class="box box-primary">

                    <div class="box-body">
                        <div id="text">
                            <div  style="alignment-adjust:central;" >
                                <%String contextPath = request.getContextPath();
                                %>



                                <s:form action="../utilities/certMonitorSearch.action" method="post" name="certForm" theme="simple">
                                    <s:hidden id="docdatepickerfrom" name="docdatepickerfrom" />
                                    <s:hidden id="docdatepicker" name="docdatepicker"/>
                                    <div class="form-group">
                                        <div class="row">
                                            <div class="col-sm-12">
                                                <div class="row">
                                                    <div class="col-sm-3"> <label>Date Range</label>
                                                        <s:textfield name="reportrange"  id="reportrange" cssClass="form-control pull-left"   value="%{reportrange}" onchange="Date1();" tabindex="1" /> 
                                                    </div>
                                                    <script type="text/javascript">
        function Date1()
        {
            var date = document.certForm.reportrange.value;
            var arr = date.split("-");
            var x = arr[1].trim();
            document.getElementById("docdatepickerfrom").value = arr[0];
            document.getElementById("docdatepicker").value = x;
        }
                                                    </script>
                                                    <div class="col-sm-3">
                                                        <label for="certType">Certificate Type <font style="color: red">*</font></label>

                                                        <s:select headerKey="-1" headerValue="--Select Type--" cssClass="form-control" list="#@java.util.LinkedHashMap@{'CA':'CA Certificate','System':'System Certificate','Trusted':'Trusted Certificate'}" name="certType" id="certType" value="%{certType}"  tabindex="2"/> 
                                                    </div>

                                                </div>

                                                <div id="loadingAcoountSearch" class="loadingImg">
                                                    <span id ="LoadingContent" > <img src="<s:url value="/includes/images/Loader2.gif"/>"   ></span>
                                                </div>



                                            </div>
                                        </div>


                                        <span id="span1">
                                        </span>
                                        <div class="row">

                                            <div class="col-sm-2"><s:submit value="Search"  cssClass="btn btn-primary col-sm-12"  onclick="return validateCertType();" tabindex="3"/></div>

                                            <div class="col-sm-2"><strong><input type="button" value="Reset"   class="btn btn-primary col-sm-12" onclick="return resetvalues();" tabindex="4"/></strong></div>

                                            <s:hidden name="sampleValue" id="sampleValue" value="2"/>



                                        </s:form>
                                    </div>
                                </div>
                            </div>
                        </div></div></section>

            <div id="gridDiv">  

                <s:if test="#session.certmonitorList != null"> 
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

                                                        <table id="results"  class="table table-bordered table-hover">
                                                            <thead><tr>
                                                                    <th> VALID TILL </th>
                                                                    <th>VALID FROM </th>
                                                                    <th>CERTIFICATE NAME</th>
                                                                    <th>DAYS</th> 
                                                                </tr> </thead>
                                                            <tbody>
                                                                <%
                                                                    java.util.LinkedList<LinkedHashMap<String, String>> list = (java.util.LinkedList<LinkedHashMap<String, String>>) session.getAttribute(AppConstants.CERTMONITOR_LIST);

                                                                    if (list.size() != 0) {

                                                                        Iterator<LinkedHashMap<String, String>> it = list.iterator();
                                                                        while (it.hasNext()) {
                                                                            LinkedHashMap<String, String> map = it.next();
                                                                            Collection<String> values = map.values();
                                                                            Object[] valuesarray = values.toArray(new Object[values.size()]);


                                                                %>
                                                                <tr>
                                                                    <td>
                                                                        <%                                                                            out.println(valuesarray[2]);
                                                                        %>
                                                                    </td>
                                                                    <td>
                                                                        <%  out.println(valuesarray[1]);
                                                                        %>
                                                                    </td>
                                                                    <td>
                                                                        <%out.println(valuesarray[0]);
                                                                        %>
                                                                    </td>  
                                                                    <td>
                                                                        <%
                                                                            String str = (String) valuesarray[3].toString();
                                                                            StringBuffer sb = new StringBuffer("Expired Since ");
                                                                            StringBuffer sb1 = new StringBuffer("Will Expire in ");
                                                                            StringBuffer sb2 = new StringBuffer(" Days");
                                                                            int days = Integer.parseInt(str);

                                                                            if (days < 0) {
                                                                                str = str.replaceAll("-", "");
                                                                        %>
                                                                        <font style='color:red'> <%out.println(sb.append(str).append(sb2));%></font>
                                                                        <%} else if ((days > 0) && (days < 30)) {
                                                                        %><font style='color:magenta'> <%out.println(sb1.append(str).append(sb2));%></font>
                                                                        <%} else {
                                                                                out.println(sb1.append(days).append(sb2));
                                                                            }%>
                                                                    </td>
                                                                </tr>

                                                                <%
                                                                        }
                                                                    }
                                                                %>

                                                            </tbody>
                                                        </table>
                                                    </td>
                                                </tr>

                                                <tr >
                                                    <td align="right" colspan="28" style="background-color: white;">
                                                        <div align="right" id="pageNavPosition"></div>
                                                    </td>
                                                </tr>
                                                <% session.setAttribute(AppConstants.CERTMONITOR_LIST, null);%>

                                            </table>
                                            <input type="hidden" name="sec_po_list" id="sec_po_list" value="<%=list.size()%>"/>
                                        </div>
                                    </div>


                                    <%-- Grid End --%>
                                </div>
                            </div>
                        </div></section>
                    </s:if>


            </div>
        </div>


    </div>
    <div>
        <s:include value="../includes/template/footer.jsp"/>
    </div>
    <script>
        $('input[name="daterange"]').daterangepicker();
    </script>
    <script language="JavaScript" src='<s:url value="/includes/js/DateValidation.js"/>'></script>
    <script language="JavaScript" src='<s:url value="/includes/js/GridNavigation.js"/>'></script>
    <script language="JavaScript" src='<s:url value="/includes/js/GeneralAjax.js"/>'></script>
    <script language="JavaScript" src='<s:url value="/includes/js/downloadAjax.js"/>'></script>

    <script src='<s:url value="../includes/plugins/daterangepicker/daterangepicker.js"/>'></script>
    <script src='<s:url value="../includes/bootstrap/js/app.min.js"/>'></script>
    <script src='<s:url value="../includes/plugins/datatables/jquery.dataTables.min.js"/>'></script>
    <script src='<s:url value="../includes/plugins/datatables/dataTables.bootstrap.min.js"/>'></script>

    <script>
        function validateCertType()
        {
            var certType = document.getElementById('certType').value;
            if (certType == "-1")
            {
                alert("Please select Certificate Type !!");
                return false;
            }
            return true;
        }


        function resetvalues()
        {
            document.getElementById('docdatepickerfrom').value = "";
            document.getElementById('docdatepicker').value = "";
            document.getElementById('certType').value = "-1";
            document.getElementById('reportrange').value = "";
            $('#gridDiv').hide();



        }





    </script>
</body>
</html>
