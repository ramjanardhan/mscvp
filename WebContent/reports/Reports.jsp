<%@page import="java.util.Map"%>
<!DOCTYPE HTML>
<%@page import="com.mss.ediscv.reports.ReportsBean"%>
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

<html class=" js canvas canvastext geolocation crosswindowmessaging no-websqldatabase indexeddb hashchange historymanagement draganddrop websockets rgba hsla multiplebgs backgroundsize borderimage borderradius boxshadow opacity cssanimations csscolumns cssgradients no-cssreflections csstransforms no-csstransforms3d csstransitions  video audio localstorage sessionstorage webworkers applicationcache svg smil svgclippaths   fontface">
    <head>
        <title>Miracle Supply Chain Visibility portal</title>
        <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
        <link rel="stylesheet" href='<s:url value="/includes/bootstrap/css/userdefined.css"/>'>
        <link rel="stylesheet" href='<s:url value="/includes/plugins/datatables/dataTables.bootstrap.css"/>' type="text/css">
        <link rel="stylesheet" href='<s:url value="/includes/plugins/daterangepicker/daterangepicker.css"/>' type="text/css">
        <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>
        <style>
            .content-wrapper{
                min-height: 552px !important;
            }
        </style>
        <script type="text/javascript">
            $(function () {
                // $("#example1").DataTable();
                $('#results').DataTable({
                    "paging": true,
                    "lengthChange": true,
                    "searching": true,
                    "ordering": true,
                    "info": true,
                    "autoWidth": true,
                    "scrollY": 300,
                    "scrollX": true,
                    order: [[0, 'desc']]
                });
            });
            var myCalendar;
            function doOnLoad() {
                $("#excelreports").addClass("active");
                $("#reports").addClass("active");
                $("#manufacturing").addClass("active");
                $("#excelreports i").addClass("text-red");
                document.getElementById('loadingAcoountSearch').style.display = "none";
            }

            function resetvaluesExcelReport() {
                // $('.myRadio').attr('checked', false);
                document.getElementById('docSen').value = "";
               // document.getElementById('docSenderName').value = "-1";
                document.getElementById('docRec').value = "";
                document.getElementById('docSenderId').value = "";
                document.getElementById('docReceiverId').value = "";
              //  document.getElementById('docReceiverName').value = "-1";
                document.getElementById('reportrange').value = "";
                document.getElementById('docdatepickerfrom').value = "";
                document.getElementById('docdatepicker').value = "";
                document.getElementById('docType').value = "-1";
                document.getElementById('status').value = "-1";
                document.getElementById('ackStatus').value = "-1";
                var elements = document.getElementsByName('database');
                elements[0].checked = true;
                $('#gridDiv').hide();
            }
            /* $(document).ready(function() {
             $('ul.sf-menu').sooperfish();
             });*/
        </script>
        <style>
            .row{
                margin-bottom: 5px; 
            }
        </style>
    </head>
    <%--<body onload="doOnLoad();initDateTime('docdatepickerfrom','docdatepicker','<%=check %>');setStyle('docRep','');">  --%>
    <body onload="doOnLoad();
            check();" class="hold-transition skin-blue sidebar-mini">
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
                <h1> Excel Reports
                    <!--                    <small>Manufacturing</small>-->
                </h1>
                <!--                <ol class="breadcrumb">
                                    <li><a href="#"><i class="fa fa-wrench"></i>Manufacturing</a></li>
                                    <li class="active"> Excel Reports</li>
                                </ol>-->
            </section>

            <section class="content">
                <div class="box box-primary">
                    <div class="box-body">
                        <div id="text">
                            <div style="alignment-adjust:central;" >
                                <% String contextPath = request.getContextPath(); %>
                                <s:form action="../reports/reportsSearch.action" method="post" name="reportsForm" id="reportsForm" theme="simple">
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
                                                        <s:textfield name="reportrange"  id="reportrange" cssClass="form-control pull-left"   value="%{reportrange}"  onchange="Date1()" tabindex="1"/> 
                                                    </div>
                                                    <script type="text/javascript">
        function Date1()
        {
            var date = document.reportsForm.reportrange.value;
            var arr = date.split("-");
            var x = arr[1].trim();
            document.getElementById("docdatepickerfrom").value = arr[0];
            document.getElementById("docdatepicker").value = x;
        }
                                                    </script>

                                                    <s:hidden id="docdatepickerfrom" name="docdatepickerfrom" />
                                                    <s:hidden id="docdatepicker" name="docdatepicker"/>


                                                    <div  class="col-sm-3">
                                                        <label>Document Type</label>
                                                        <s:select headerKey="-1" cssClass="form-control" headerValue="Select Type" list="docTypeList" name="docType" id="docType" value="%{docType}" tabindex="6"/>
                                                    </div>  
                                                    <div  class="col-sm-3">
                                                        <label>Status</label> 
                                                        <s:select headerKey="-1" cssClass="form-control" headerValue="Select Type" list="{'Success','Error','Warning'}" name="status" id="status" value="%{status}" tabindex="7"/>
                                                    </div>  
                                                    <div  class="col-sm-3">
                                                        <label>Ack Status</label> 
                                                        <s:select headerKey="-1" cssClass="form-control" headerValue="Select Type" list="{'Overdue','Accepted','Rejected'}" name="ackStatus" id="ackStatus" value="%{ackStatus}" tabindex="8"/>
                                                    </div>


                                                </div>

                                                <div class="row">




                                                    <div  class="col-sm-3">
                                                        <label>Sender </label>  
                                                        <s:textfield onchange="setSenderId();"  cssClass="form-control" list="senderIdMap" name="docSen" id="docSen" value="%{docSenderId}" tabindex="5"/>
                                                        <%Map<String, String> senderIdMap = (Map) session.getAttribute(AppConstants.SENDERSID_MAP);

                                                        %>
                                                        <datalist id="senderIdMap">
                                                            <% for (Map.Entry<String, String> entry : senderIdMap.entrySet()) {%>
                                                            <option value="<%=entry.getValue()%>"/>
                                                            <%}%>
                                                        </datalist> 
                                                        <s:hidden name="docSenderId" id="docSenderId" />
                                                    </div>
                                                    <div class="col-sm-3">
                                                        <label>Receiver </label>
                                                        <s:textfield onchange="setReceiverId();"  cssClass="form-control" list="receiverIdMap" name="docRec" id="docRec" value="%{docReceiverId}" tabindex="6"/>
                                                        <%Map<String, String> receiverIdMap = (Map) session.getAttribute(AppConstants.RECEIVERSId_MAP);

                                                        %>
                                                        <datalist id="receiverIdMap">
                                                            <% for (Map.Entry<String, String> entry : receiverIdMap.entrySet()) {%>
                                                            <option value="<%=entry.getValue()%>"/>
                                                            <%}%>
                                                        </datalist> 
                                                        <s:hidden name="docReceiverId" id="docReceiverId" />
                                                    </div>



                                                    <div id="loadingAcoountSearch" class="loadingImg">
                                                        <span id ="LoadingContent" > <img src="<s:url value="/includes/images/Loader2.gif"/>"   ></span>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>

                                        <div class="row">
                                            <div class="col-sm-2"><s:submit value="Search" onclick="return checkReport();" cssClass="btn btn-primary col-sm-12" tabindex="9"/></div>
                                            <div class="col-sm-2"><strong><input type="button" value="Reset"   class="btn btn-primary col-sm-12" onclick="return resetvaluesExcelReport();" tabindex="10"/></strong></div>
                                                    <s:hidden name="sampleValue" id="sampleValue" value="2"/>
                                                </s:form>
                                    </div>
                                </div>
                            </div>
                        </div></div>
                </div>
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
                                            <table align="left" width="100%" border="0" cellpadding="0" cellspacing="0">
                                                <tr>
                                                    <td style="background-color: white;">
                                                        <div class="grid_overflow">
                                                            <table id="results"  class="table table-bordered table-hover">
                                                                <%
                                                                    java.util.List list = (java.util.List) session.getAttribute(AppConstants.SES_DOCREPORT_LIST);
                                                                    System.out.println("list size-->" + list.size());
                                                                    if (list.size() != 0) {
                                                                        ReportsBean docRepositoryBean;
                                                                %>
                                                                <thead> <tr>
                                                                        <th>DateTime</th>
                                                                        <th>File&nbsp;Format</th> 
                                                                        <th>InstanceId</th>
                                                                        <th>Partner</th>
                                                                        <th>Trans&nbsp;Type</th>
                                                                        <th >Direction</th>
                                                                        <th>Status</th>
                                                                        <th>Reprocess</th>
                                                                    </tr>
                                                                </thead>
                                                                <tbody>
                                                                    <%
                                                                        for (int i = 0; i < list.size(); i++) {
                                                                            docRepositoryBean = (ReportsBean) list.get(i);
                                                                            if (i % 2 == 0) {
                                                                                cssValue = "whiteStripe";
                                                                            } else {
                                                                                cssValue = "grayEditSelection";
                                                                            }
                                                                    %>
                                                                <td>
                                                                    <%
                                                                        if (docRepositoryBean.getDate_time_rec().toString().substring(0, docRepositoryBean.getDate_time_rec().toString().lastIndexOf(":")) != null
                                                                                && !"".equals(docRepositoryBean.getDate_time_rec().toString().substring(0, docRepositoryBean.getDate_time_rec().toString().lastIndexOf(":")))) {
                                                                            out.println(docRepositoryBean.getDate_time_rec().toString().substring(0, docRepositoryBean.getDate_time_rec().toString().lastIndexOf(":")));
                                                                        } else {
                                                                            out.println("-");
                                                                        }
                                                                    %>
                                                                </td> 
                                                                <td>
                                                                    <%
                                                                        if (docRepositoryBean.getFile_type() != null && !"".equals(docRepositoryBean.getFile_type())) {
                                                                            out.println(docRepositoryBean.getFile_type());
                                                                        } else {
                                                                            out.println("-");
                                                                        }
                                                                    %>
                                                                </td>
                                                                <td>
                                                                    <%
                                                                        if (docRepositoryBean.getFile_id() != null && !"".equals(docRepositoryBean.getFile_id())) {
                                                                            out.println(docRepositoryBean.getFile_id());
                                                                        } else {
                                                                            out.println("-");
                                                                        }
                                                                    %>
                                                                    <%--  </a> --%>
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
                                                                    <%
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
                                                                        if (docRepositoryBean.getStatus().equalsIgnoreCase("ERROR")) {%> <a href="#" onclick="getErrorMessage('<%=docRepositoryBean.getStatus().toUpperCase()%>', '<%=docRepositoryBean.getErrorMessage()%>')" onmouseover="Tip('Click here to view Error Info.')" onmouseout="UnTip()">        
                                                                        <%
                                                                            out.println("<font color='red'>" + docRepositoryBean.getStatus().toUpperCase() + "</font>");
                                                                        %></a><%
                                                                            } else if (docRepositoryBean.getStatus().equalsIgnoreCase("SUCCESS")) {
                                                                                out.println("<font color='green'>" + docRepositoryBean.getStatus().toUpperCase() + "</font>");
                                                                            } else {
                                                                                out.println("<font color='orange'>" + docRepositoryBean.getStatus().toUpperCase() + "</font>");
                                                                            }
                                                                        %>
                                                                </td>
                                                                <%--   <td>
                                                                           <%
                                                                             //out.println(poLifeCycleBean.getAckStatus());
                                                                             if(docRepositoryBean.getAckStatus().equalsIgnoreCase("REJECTED")){       
                                                                                  out.println("<font color='red'>"+docRepositoryBean.getAckStatus()+"</font>");
                                                                              }else if(docRepositoryBean.getAckStatus().equalsIgnoreCase("ACCEPTED")){
                                                                                  out.println("<font color='green'>"+docRepositoryBean.getAckStatus()+"</font>");
                                                                              }else {
                                                                                   out.println("<font color='orange'>"+docRepositoryBean.getAckStatus()+"</font>");
                                                                                }
                                                                             %>
                                                                         </td> --%>
                                                                <td>
                                                                    <%                                                                        //out.println(docRepositoryBean.getReProcessStatus());
                                                                        if (docRepositoryBean.getReProcessStatus() != null && !"".equals(docRepositoryBean.getReProcessStatus())) {
                                                                            out.println(docRepositoryBean.getReProcessStatus().toUpperCase());

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
                                                            <%                                                                if (list.size() != 0) {
                                                            %>
                                                <tr >
                                                    <!--                                                        <td align="right" colspan="28" style="background-color: white;">
                                                                                                                <div align="right" id="pageNavPosition">hello</div>-->

                                                </tr> 
                                                <% }%>
                                                </tbody></table>
                                        </div>
                                        <%-- Process butttons  start --%>
                                        <%
                                            if (list.size() != 0) {
                                        %>
                                        <table align="right">
                                            <tr>
                                                <td class="gnexcel-space" style="background-color: white;">
                                                    <strong><input type="button" value="Generate Excel" class="btn btn-effect-ripple btn-primary" onclick="return gridDownload('documentReport', 'xls');" onmouseover="Tip('Click here to generate an excel Report.')" onmouseout="UnTip()" id="excel"/></strong>
                                                </td>
                                            </tr>
                                        </table> 
                                        <%}%>
                                    </div>
                                </div>
                            </div>
                        </div> </section>
                        <%-- process buttons end--%>
                        <%-- Grid End --%>
                    </s:if>
            </div> 
        </div> 

        <div>
            <s:include value="../includes/template/footer.jsp"/>
        </div>
        <script type="text/javascript" src='<s:url value="../includes/js/DateValidation.js"/>'></script>
        <script language="JavaScript"  src='<s:url value="/includes/js/GeneralAjax.js"/>'></script>
        <script language="JavaScript"  src='<s:url value="/includes/js/downloadAjax.js"/>'></script>
        <!-- Bootstrap 3.3.5 -->
        <script src='<s:url value="../includes/plugins/daterangepicker/daterangepicker.js"/>'></script>
        <script src='<s:url value="../includes/bootstrap/js/app.min.js"/>'></script>
        <script src='<s:url value="../includes/plugins/datatables/jquery.dataTables.min.js"/>'></script>
        <script src='<s:url value="../includes/plugins/datatables/dataTables.bootstrap.min.js"/>'></script>

        <script type="text/javascript">
                                                        $('input[name="daterange"]').daterangepicker();
                                                        function checkReport() {
                                                            var db = document.forms["reportsForm"]["database"].value;
                                                            if (db == '') {
                                                                alert("Please select Database!!!");
                                                                return false;
                                                            }
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
