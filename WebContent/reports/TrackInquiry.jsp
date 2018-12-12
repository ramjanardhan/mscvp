<%@page import="com.mss.ediscv.editracking.TrackInOutBean"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="com.mss.ediscv.util.DataSourceDataProvider"%>

<%@ taglib prefix="s" uri="/struts-tags" %>
<%-- <!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"> --%>
<!DOCTYPE html>
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page buffer="50kb" autoFlush="true" %>

<%@page import="com.mss.ediscv.doc.DocRepositoryBean"%>

<%@page import="java.sql.Connection"%>
<%@  page import="com.mss.ediscv.util.AppConstants"%>


<html class=" js canvas canvastext geolocation crosswindowmessaging no-websqldatabase indexeddb hashchange historymanagement draganddrop websockets rgba hsla multiplebgs backgroundsize borderimage borderradius boxshadow opacity cssanimations csscolumns cssgradients no-cssreflections csstransforms no-csstransforms3d csstransitions  video audio localstorage sessionstorage webworkers applicationcache svg smil svgclippaths   fontface">
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <title>Miracle Supply Chain Visibility Portal</title>
        <!-- Tell the browser to be responsive to screen width -->
        <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
        <link rel="stylesheet" href='<s:url value="/includes/bootstrap/css/userdefined.css"/>'>
        <link rel="stylesheet" href='<s:url value="/includes/plugins/datatables/dataTables.bootstrap.css"/>' type="text/css">
        <link rel="stylesheet" href='<s:url value="/includes/plugins/daterangepicker/daterangepicker.css"/>' type="text/css"> 
        <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>


        <script>
            var myCalendar;
            function doOnLoad() {
                $("#editrackingenquiry").addClass("active");
                $("#reports").addClass("active");

                $("#manufacturing").addClass("active");


                $("#editrackingenquiry i").addClass("text-red");
                document.getElementById('loadingAcoountSearch').style.display = "none";

            }
        </script>
        <script type="text/javascript">

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

            function resetValues()
            {

                document.getElementById('docdatepickerfrom').value = "";
                document.getElementById('docdatepicker').value = "";
                document.getElementById('docType').value = "-1";
                document.getElementById('partnerMapId').value = "-1";
                document.getElementById('reportrange').value = "";
                $('#gridDiv').hide();

            }
            /* $(document).ready(function() {
             $('ul.sf-menu').sooperfish();
             });*/






        </script>

        <style>
            .loadingImg {
                width: 100%;
                height: 100%;
                top: 0px;
                left: 0px;
                position: fixed;
                display: block;
                opacity: 0.7;
                background-color: #9999C2;
                z-index: 99;
                text-align: center;
            }

            #LoadingContent {
                position: absolute;
                top: 50%;
                left: 50%;
                z-index: 100;
            }
            #LoadingContent > img{
                width:150px;
            }
        </style>
        <style>
            .row{
                  margin-bottom: 5px; 
            }
            </style>

    </head>

    <%--<body onload="doOnLoad();initDateTime('docdatepickerfrom','docdatepicker','<%=check %>');setStyle('docRep','');">  --%>

    <body class="hold-transition skin-blue sidebar-mini" onload="doOnLoad()">
        <script type="text/javascript" src='<s:url value="/includes/js/wz_tooltip.js"/>'></script>

        <div>
            <s:include value="/includes/template/header.jsp"/>       

        </div>

        <div>
            <s:include value="/includes/template/sidemenu.jsp"/>
        </div>	

        <!-- Start Special Centered Box -->

        <div class="content-wrapper">
            <!-- Content Header (Page header) -->  
            <section class="content-header">
                <h1>
                    EDI Track Inquiry
<!--                    <small>Manufacturing</small>-->
                </h1>
<!--                <ol class="breadcrumb">
                    <li><a href="#"><i class="fa fa-wrench"></i>Manufacturing</a></li>
                    <li class="active">EDI Track Inquiry</li>
                </ol>-->
            </section>
            
            <section class="content">

                <div class="box box-primary">
                    <div class="box-header with-border">
                        <%String contextPath = request.getContextPath();
                        %> 
                        <div class="box-tools pull-right">

                        </div>
                    </div><!-- /.box-header -->
                    <div class="box-body">
                        <div id="text">

                            <!-- /.col (right) -->
                            <!-- First Row -->
                            <div class="row">

                                <div style="alignment-adjust:central;">


                                    <s:form action="../reports/trackInquirySearch.action" method="post" name="trackInquiryForm" id="trackInquiryForm" theme="simple">

                                        <div class="form-group">
                                            <div class="row">
                                                <div class="col-sm-12">
                                                <div class="col-sm-3"> <label>Date Range</label>

                                                    <s:textfield name="reportrange"  id="reportrange" cssClass="form-control pull-left"   value="%{reportrange}"  tabindex="1"/> 
                                                </div>

                                                <script type="text/javascript">
        function Date1()
        {
            //                                                            alert("into date1");
            var date = document.trackInquiryForm.reportrange.value;
            var arr = date.split("-");
            var x = arr[1].trim();
            document.getElementById("docdatepickerfrom").value = arr[0];
            document.getElementById("docdatepicker").value = x;
        }
                                                </script>

                                                <s:hidden id="docdatepickerfrom" name="docdatepickerfrom" />
                                                <s:hidden id="docdatepicker" name="docdatepicker"/>

                                                <div class="col-sm-3"> <label>Document Type</label>

                                                    <s:select headerKey="-1" headerValue="Select Type" list="docTypeList" name="docType" id="docType" value="%{docType}"  cssClass="form-control" tabindex="2"/>
                                                </div>
                                                <div class="col-sm-3"> <label>Partner&nbsp;Name</label>

                                                    <s:select headerKey="-1" headerValue="Select Type" list="partnerMap" name="partnerMapId" id="partnerMapId"  cssClass="form-control"  value="%{partnerMapId}" tabindex="3"/>
                                                </div>

                                                <%-- return compareDates(document.getElementById('docdatepickerfrom').value,document.getElementById('docdatepicker').value); --%>
                                            </div>
                                        </div>
                                            
                                            <div clas="row">
                                                <div class="col-sm-2">   <s:submit value="Search" cssClass="btn btn-primary col-sm-12"  onclick="Date1()" tabindex="4"/></div>

                                                <div class="col-sm-2"> <strong><input type="button" value="Reset" class="btn btn-primary col-sm-12"  onclick="return resetValues();" tabindex="5"/></strong> </div>
                                            </div>
                                            <s:hidden name="sampleValue" id="sampleValue" value="2"/>
                                            <div id="loadingAcoountSearch" class="loadingImg">
                                                <span id ="LoadingContent" > <img src="<s:url value="/includes/images/Loader2.gif"/>"   ></span>
                                            </div>

                                        </s:form>
                                    </div>
                                </div>

                            </div>
                        </div></div></div>
                        <%--  out.print("contextPath-->"+contextPath); --%>
            </section>


            <div id="gridDiv">     

                <s:if test="#session.documentReportList != null"> 
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
                                                        <div id="grid_overflow">

                                                            <table align="left" id="results" width="100%"
                                                                   border="0" cellpadding="0" cellspacing="0" class="table table-bordered table-hover">
                                                                <%
                                                                    java.util.List list = (java.util.List) session.getAttribute(AppConstants.SES_DOCREPORT_LIST);

                                                                    if (list.size() != 0) {
                                                                        TrackInOutBean trackInOutBean;
                                                                %>
                                                                <thead> <tr>
                                                                        <th>Trans&nbsp;Type</th>
                                                                        <th>Date Sent</th>
                                                                        <th>Date Acked</th>
                                                                        <th>ACK Code</th>
                                                                        <th>Partner</th>
                                                                    </tr></thead>

                                                                <tr>

                                                                    <%
                                                                        for (int i = 0; i < list.size(); i++) {
                                                                            trackInOutBean = (TrackInOutBean) list.get(i);

                                                                            if (i % 2 == 0) {
                                                                                cssValue = "whiteStripe";
                                                                            } else {
                                                                                cssValue = "grayEditSelection";
                                                                            }
                                                                    %>


                                                                    <td>
                                                                        <%
                                                                            if (trackInOutBean.getTransaction_type() != null && !"".equals(trackInOutBean.getTransaction_type())) {
                                                                                out.println(trackInOutBean.getTransaction_type());
                                                                            } else {
                                                                                out.println("-");
                                                                            }
                                                                        %>

                                                                    </td>


                                                                    <td>
                                                                        <%
                                                                            if (trackInOutBean.getDate_time_rec().toString().substring(0, trackInOutBean.getDate_time_rec().toString().lastIndexOf(":")) != null
                                                                                    && !"".equals(trackInOutBean.getDate_time_rec().toString().substring(0, trackInOutBean.getDate_time_rec().toString().lastIndexOf(":")))) {
                                                                                out.println(trackInOutBean.getDate_time_rec().toString().substring(0, trackInOutBean.getDate_time_rec().toString().lastIndexOf(":")));
                                                                            } else {
                                                                                out.println("-");
                                                                            }
                                                                        %>

                                                                    </td>  
                                                                    <td>
                                                                        <%
                                                                            if (trackInOutBean.getDate_time_rec().toString().substring(0, trackInOutBean.getDate_time_rec().toString().lastIndexOf(":")) != null
                                                                                    && !"".equals(trackInOutBean.getDate_time_rec().toString().substring(0, trackInOutBean.getDate_time_rec().toString().lastIndexOf(":")))) {
                                                                                out.println(trackInOutBean.getDate_time_rec().toString().substring(0, trackInOutBean.getDate_time_rec().toString().lastIndexOf(":")));
                                                                            } else {
                                                                                out.println("-");
                                                                            }
                                                                        %>

                                                                    </td>

                                                                    <td>
                                                                        <%
                                                                            //out.println(poLifeCycleBean.getAckStatus());
                                                                            if (trackInOutBean.getAckStatus().equalsIgnoreCase("REJECTED")) {
                                                                                out.println("REJECTED");
                                                                            } else if (trackInOutBean.getAckStatus().equalsIgnoreCase("ACCEPTED")) {
                                                                                out.println("ACCEPTED");
                                                                            } else {
                                                                                out.println("<font color='orange'>" + trackInOutBean.getAckStatus() + "</font>");
                                                                            }

                                                                        %>
                                                                    </td> 

                                                                    <td> 
                                                                        <%                                                                            if (trackInOutBean.getPname() != null && !"".equals(trackInOutBean.getPname())) {
                                                                                out.println(trackInOutBean.getPname());
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
                                                            </table></td></tr>
                                                            <%                                                            if (list.size() != 0) {
                                                            %>
                                                <tr >
                                                    <!--<td align="right" colspan="28" style="background-color: white;">-->
                                                    <!--                                                        <div align="right" id="pageNavPosition">hello</div>-->
                                                    <!--                                                </td>-->
                                                </tr> 

                                                <% }%></tbody>
                                            </table> </div> 






                                        <%
                                            if (list.size() != 0) {
                                        %>
                                        <table align="right">
                                            <tr>
                                                <td class="gnexcel-space">
                                                    <strong><input type="button" value="Generate Excel" class="btn btn-primary col-sm-12" onclick="return gridDownload('trackInquiry', 'xls');" data-toggle="tooltip" title="Click here to generate an excel Report." onmouseover="Tip('Click here to generate an excel Report.')" onmouseout="UnTip()" id="excel"/></strong>
                                                </td>
                                            </tr>
                                        </table> 
                                        <%}%>
                                    </div></div>
                            </div>
                        </div>
                    </section>



                </s:if> 


            </div> 


        </div>
        <div>
            <s:include value="../includes/template/footer.jsp"/>
        </div>

        <script type="text/javascript" src='<s:url value="../includes/js/DateValidation.js"/>'></script>
        <script language="JavaScript"  src='<s:url value="/includes/js/GeneralAjax.js"/>'></script>
        <script language="JavaScript"  src='<s:url value="/includes/js/downloadAjax.js"/>'></script>
        <script language="JavaScript"  src='<s:url value="/includes/js/overlay.js"/>'></script>


        <script>
                                                        $('input[name="daterange"]').daterangepicker();
        </script>

        <script src='<s:url value="../includes/bootstrap/js/bootstrap.min.js"/>'></script>
        <script src='<s:url value="../includes/plugins/daterangepicker/daterangepicker.js"/>'></script>
        <script src='<s:url value="../includes/bootstrap/js/app.min.js"/>'></script>
        <script src='<s:url value="../includes/plugins/datatables/jquery.dataTables.min.js"/>'></script>
        <script src='<s:url value="../includes/plugins/datatables/dataTables.bootstrap.min.js"/>'></script>


    </body>
</html>