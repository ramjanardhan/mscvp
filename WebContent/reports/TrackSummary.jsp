<!DOCTYPE html>
<%@page import="com.mss.ediscv.editracking.TrackInOutBean"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="com.mss.ediscv.util.DataSourceDataProvider"%>
<%@page import="com.mss.ediscv.reports.ReportsBean"%>
<%@taglib prefix="s" uri="/struts-tags" %>
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page buffer="50kb" autoFlush="true" %>
<%@page import="com.mss.ediscv.doc.DocRepositoryBean"%>
<%@page import="java.sql.Connection"%>
<%@page import="com.mss.ediscv.util.AppConstants"%>

<html class=" js canvas canvastext geolocation crosswindowmessaging no-websqldatabase indexeddb hashchange historymanagement draganddrop websockets rgba hsla multiplebgs backgroundsize borderimage borderradius boxshadow opacity cssanimations csscolumns cssgradients no-cssreflections csstransforms no-csstransforms3d csstransitions  video audio localstorage sessionstorage webworkers applicationcache svg smil svgclippaths   fontface">
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <title>Miracle Supply Chain Visibility Portal</title>
        <!-- Tell the browser to be responsive to screen width -->
        <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
        <!-- Bootstrap 3.3.5 -->
        <script type="text/javascript" src="//cdn.jsdelivr.net/bootstrap.daterangepicker/2/daterangepicker.js"></script>
        <link rel="stylesheet" href='<s:url value="/includes/bootstrap/css/userdefined.css"/>'>
        <link rel="stylesheet" href='<s:url value="/includes/bootstrap/css/bootstrap.min.css"/>' type="text/css">
        <!-- Font Awesome -->
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.4.0/css/font-awesome.min.css">
        <!-- Ionicons -->
        <link rel="stylesheet" href="https://code.ionicframework.com/ionicons/2.0.1/css/ionicons.min.css">
        <!-- Theme style -->
        <link rel="stylesheet" href='<s:url value="/includes/bootstrap/css/AdminLTE.min.css"/>' type="text/css">
        <!-- AdminLTE Skins. Choose a skin from the css/skins
             folder instead of downloading all of them to reduce the load. -->
        <link rel="stylesheet" href='<s:url value="/includes/bootstrap/css/skins/_all-skins.min.css"/>' type="text/css">
        <!-- iCheck -->
        <link rel="stylesheet" href='<s:url value="/includes/plugins/iCheck/flat/blue.css"/>' type="text/css">
        <!-- Morris chart -->
        <link rel="stylesheet" href='<s:url value="/includes/plugins/morris/morris.css"/>' type="text/css">
        <!-- jvectormap -->
        <link rel="stylesheet" href='<s:url value="/includes/plugins/jvectormap/jquery-jvectormap-1.2.2.css"/>' type="text/css">
        <link rel="stylesheet" href='<s:url value="/includes/plugins/datatables/dataTables.bootstrap.css"/>' type="text/css">
        <link rel="stylesheet" href='<s:url value="/includes/plugins/daterangepicker/daterangepicker.css"/>' type="text/css"> 
        <!-- Date Picker -->
        <!-- Daterange picker -->
        <link rel="stylesheet" href='<s:url value="/includes/plugins/daterangepicker/daterangepicker.css"/>' type="text/css">
        <!-- bootstrap wysihtml5 - text editor -->
        <link rel="stylesheet" href='<s:url value="/includes/plugins/bootstrap-wysihtml5/bootstrap3-wysihtml5.min.css"/>' type="text/css">
        <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
        <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
        <!--[if lt IE 9]>
            <script src="https://oss.maxcdn.com/html5shiv/3.7.3/html5shiv.min.js"></script>
            <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
        <![endif]-->

        <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>
        <script type="text/javascript" src='<s:url value="../includes/plugins/datepicker/moment.js"/>'></script>
        <script type="text/javascript" src='<s:url value="../includes/plugins/datepicker/daterangepicker.js"/>'></script>
        <link rel="stylesheet" href='<s:url value="/includes/bootstrap/css/userdefined.css"/>'>
        <script type="text/javascript" src='<s:url value="../includes/bootstrap/js/userdefined.js"/>'></script>
        <script language="JavaScript" src='<s:url value="/includes/js/DateValidation.js"/>'></script>
        <script language="JavaScript" src='<s:url value="/includes/js/GeneralAjax.js"/>'></script>
        <script language="JavaScript" src='<s:url value="/includes/js/common.js"/>'></script>
        <script language="JavaScript" src='<s:url value="/includes/js/modernizr-1.5.min.js"/>'></script>
        <script language="JavaScript" src='<s:url value="/includes/js/downloadAjax.js"/>'></script>
        <script language="JavaScript" src='<s:url value="/includes/js/overlay.js"/>'></script>
        <script type="text/javascript">
            $(function() {
                //$("#example1").DataTable();
                $('#results').DataTable({
                    "paging": true,
                    "lengthChange": true,
                    "searching": true,
                    "ordering": true,
                    "info": true,
                    "autoWidth": true,
                    "scrollY": 300,
                    "scrollX": true
                });
            });
            function doOnLoad() {
                $("#editrackingsummary").addClass("active");
                $("#manufacturing").addClass("active");
                $("#reports").addClass("active");
                $("#editrackingsummary i").addClass("text-red");
                document.getElementById('loadingAcoountSearch').style.display = "none";
            }

            function resetValues() {
                document.getElementById('docdatepickerfrom').value = "";
                document.getElementById('docdatepicker').value = "";
                document.getElementById('reportrange').value = "";
                document.getElementById('docNetworkvan').value = "-1";
                $('#gridDiv').hide();
            }
            /* $(document).ready(function() {
             $('ul.sf-menu').sooperfish();
             });*/
        </script>
        <style>
            /*plus_minus for grid */
            .plus_minus {
                background-image: url('../includes/images/plus.png'); 
                visibility: visible;
                width: 14px; 
                background-size: 14px 14px; 
                height: 14px; 
                margin-bottom: -14px;
            }
            .plus_minus_anchor {
                position: relative;
                left: 40px;
            }
        </style>
        <style>
            .row{
                  margin-bottom: 5px; 
            }
            </style>
    </head>
    <%--<body onload="doOnLoad();initDateTime('docdatepickerfrom','docdatepicker','<%=check %>');setStyle('docRep','');">  --%>
    <body onload="doOnLoad();"  class="hold-transition skin-blue sidebar-mini">
        <script type="text/javascript" src='<s:url value="/includes/js/wz_tooltip.js"/>'></script>
        <header>  
            <div id="wrapper">
                <div id="main">
                    <header>
                        <div>
                            <s:include value="/includes/template/header.jsp"/>       
                        </div>
                    </header>
                    <div>
                        <s:include value="/includes/template/sidemenu.jsp"/>
                    </div>	
                </div>
        </header>
        <!-- Start Special Centered Box -->
        <!-- end Special Centered Box -->
        <!-- Content Wrapper. Contains page content -->
        <div class="content-wrapper">
            <!-- Content Header (Page header) -->  
            <section class="content-header">
                <h1>
                    EDI Tracking Summary
                    <!--                <small>Manufacturing</small>-->
                </h1>
                <!--            <ol class="breadcrumb">
                                <li><a href="#"><i class="fa fa-wrench"></i>Manufacturing</a></li>
                                <li class="active">EDI Tracking Summary</li>
                            </ol>-->
            </section>
            
            <section class="content">
                <div class="row">
                    <div class="col-sm-12"> 
                        <!--box-->
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
                                        <div class="col-sm-12">
                                            <s:form action="../reports/trackSummarySearch.action" method="post" name="trackSummaryForm" id="trackSummaryForm" theme="simple">
                                                <div class="form-group">
                                                    <div class="row">
                                                        <div class="col-sm-3"> <label>Date Range</label>
                                                            <s:textfield name="reportrange"  id="reportrange" cssClass="form-control pull-left"   value="%{reportrange}"  tabindex="1"/> 
                                                        </div>
                                                        <script type="text/javascript">
            function Date1() {
                var date = document.trackSummaryForm.reportrange.value;
                var arr = date.split("-");
                var x = arr[1].trim();
                document.getElementById("docdatepickerfrom").value = arr[0];
                document.getElementById("docdatepicker").value = x;
            }
                                                        </script>
                                                        <s:hidden id="docdatepickerfrom" name="docdatepickerfrom" />
                                                        <s:hidden id="docdatepicker" name="docdatepicker"/>
                                                        <!--                                                    <script type="text/javascript">
                                                                                                                $(function() {
                                                            
                                                                                                                    function cb(start, end) {
                                                                                                                        $('#reportrange span').html(start.format('MMMM D, yyyy') + ' - ' + end.format('MMMM D, yyyy'));
                                                                                                                    }
                                                                                                                    cb(moment().subtract(29, 'days'), moment());
                                                        
                                                                                                                    $('#reportrange').daterangepicker({
                                                                                                                        ranges: {
                                                                                                                            'Today': [moment(), moment()],
                                                                   
                                                                                                                            'Last 7 Days': [moment().subtract(6, 'days'), moment()],
                                                                                                                            'Last 30 Days': [moment().subtract(29, 'days'), moment()],
                                                                                                                            'This Month': [moment().startOf('month'), moment().endOf('month')],
                                                                                                                            'Last Month': [moment().subtract(1, 'month').startOf('month'), moment().subtract(1, 'month').endOf('month')],
                                                                                                                            'Transactions Until': [moment()]
                                                                                                                        }
                                                                                                                    }, cb);
                                                        
                                                                                                                });
                                                                                                            </script>-->



                                                        <div  class="col-sm-3">
                                                            <label>NetworkVan</label> 
                                                            <s:select headerKey="-1" cssClass="form-control" headerValue="Select Type" list="networklanlist" name="docNetworkvan" id="docNetworkvan" value="%{docNetworkvan}" tabindex="2" /> 
                                                        </div>
                                                        <div id="loadingAcoountSearch" class="loadingImg">
                                                            <span id ="LoadingContent" > <img src="<s:url value="/includes/images/Loader2.gif"/>"   ></span>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="row">
                                                    <div class="col-sm-2"><s:submit value="Search"  onclick="Date1()" cssClass="btn btn-primary col-sm-12" tabindex="3"/></div>
                                                    <div class="col-sm-2">   <strong><input type="button" value="Reset" class="btn btn-primary col-sm-12"  onclick="return resetValues();" tabindex="4"/></strong></div>
                                                </div>
                                                <s:hidden name="sampleValue" id="sampleValue" value="2"/>
                                            </s:form>
                                        </div></div>
                                </div>
                            </div>
                            <%--  out.print("contextPath-->"+contextPath); --%>
                        </div>
                    </div></section>
            <div id="gridDiv">
                <s:if test="#session.documentReportList != null"> 
                    <%--- GRid start --%>

                    <%!String cssValue = "whiteStripe";
                        int resultsetTotal;
                        int total = 0;
                        int inboundTotal = 0;
                        int outboundTotal = 0;
                        double filesizeTotal = 0;
                        double filesizeTotal1 = 0;
                        List docTypeList = new ArrayList();

                    %>
                    <%
                        try {
                            docTypeList = DataSourceDataProvider.getInstance().getDocumentTypeList("M");
                        } catch (Exception e) {
                        }
                    %>
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
                                                        <%
                                                            java.util.List list = (java.util.List) session.getAttribute(AppConstants.SES_DOCREPORT_LIST);
                                                            if (list.size() != 0) {
                                                                TrackInOutBean trackInOutBean;
                                                        %>
                                                        <div style="text-align: center; font-weight: bold; font-size: 25px; margin-bottom: 20px;">EDI Tracking Summary</div>
                                                        <table align="left" id="results" width="100%"
                                                               border="0" cellpadding="0" cellspacing="0" class="table table-bordered table-hover">
                                                            <thead><tr>
                                                                    <th>Trans&nbsp;Type</th> 
                                                                    <th>Partner</th>
                                                                    <th>File&nbsp;Size</th>
                                                                    <th>In</th>
                                                                    <th>Out</th>
                                                                    <th>Total</th>
                                                                </tr></thead>
                                                            <tbody>
                                                                <tr>                                                            <%
                                                                    int j = 0;
                                                                    for (int i = 0; i < list.size(); i++) {
                                                                        trackInOutBean = (TrackInOutBean) list.get(i);

                                                                        if (i % 2 == 0) {
                                                                            cssValue = "whiteStripe";
                                                                        } else {
                                                                            cssValue = "grayEditSelection";
                                                                        }

                                                                        if (!trackInOutBean.getPname().equalsIgnoreCase("Total")) {%>
                                                                <tr id="toggleText<%=i%>" style="display: none">
                                                                    <%} else {%>
                                                                <tr>
                                                                    <%}%>
                                                                    <td id="toggleTd<%=i%>">
                                                                        <%

                                                                            if (trackInOutBean.getPname().equalsIgnoreCase("Total")) {
                                                                        %><a href="javascript:toggle('total<%=i%>',<%=i%>,<%=j%>)" style="position: relative; left: 64px;" >

                                                                            <div id="total<%=i%>" class="plus_minus">
                                                                            </div>

                                                                        </a><%
                                                                                j = i + 1;
                                                                            }
                                                                            out.println(trackInOutBean.getTransaction_type());
                                                                        %>
                                                                    </td>
                                                                    <td>
                                                                        <%
                                                                            out.println(trackInOutBean.getPname());
                                                                        %>
                                                                    </td>
                                                                    <%--   <td>
                                                                          <%
                                                                          if(trackInOutBean.getInbound()!=0)
                                                                       {
                                                                          out.println(trackInOutBean.getFilesize());
                                                                          if(trackInOutBean.getPname().equalsIgnoreCase("Total"))
                                                                          {
                                                                          filesizeTotal=filesizeTotal+trackInOutBean.getFilesize();
                                                                          } 
                                                                       }
                                                                        if(trackInOutBean.getOutbound()!=0)
                                                                       {
                                                                          out.println(trackInOutBean.getFilesize1());
                                                                          if(trackInOutBean.getPname().equalsIgnoreCase("Total"))
                                                                          {
                                                                          filesizeTotal1=filesizeTotal1+trackInOutBean.getFilesize1();
                                                                          } 
                                                                       }

           %>
                 
          </td>--%>
                                                                    <td>   
                                                                        <%
                                                                            if (trackInOutBean.getPname().equalsIgnoreCase("Total")) {
                                                                                filesizeTotal = filesizeTotal + trackInOutBean.getFilesizeTotal();
                                                                            }
                                                                            if (trackInOutBean.getFilesizeTotal() != 0) {
                                                                                out.println(trackInOutBean.getFilesizeTotal() + "kb");
                                                                            } else {
                                                                                out.println(trackInOutBean.getFilesizeTotal());
                                                                            }
                                                                        %>
                                                                    </td>
                                                                    <td>
                                                                        <%
                                                                            out.println(trackInOutBean.getInbound());
                                                                            if (trackInOutBean.getPname().equalsIgnoreCase("Total")) {
                                                                                inboundTotal = inboundTotal + trackInOutBean.getInbound();
                                                                            }
                                                                        %>
                                                                    </td>
                                                                    <td>
                                                                        <%
                                                                            out.println(trackInOutBean.getOutbound());
                                                                            if (trackInOutBean.getPname().equalsIgnoreCase("Total")) {
                                                                                outboundTotal = outboundTotal + trackInOutBean.getOutbound();
                                                                            }
                                                                        %>
                                                                    </td>  
                                                                    <td>
                                                                        <%
                                                                            if (trackInOutBean.getPname().equalsIgnoreCase("Total")) {
                                                                                total = total + trackInOutBean.getTotal();
                                                                            }
                                                                            out.println(trackInOutBean.getTotal());
                                                                        %>
                                                                    </td>
                                                                </tr>
                                                                <%
                                                                    }
                                                                %>
                                                                <tr>
                                                                    <td colspan="2">Total</td>
                                                                    <td><%= filesizeTotal + "kb"%></td>  
                                                                    <td><%= inboundTotal%></td>
                                                                    <td><%= outboundTotal%></td>
                                                                    <td><%= total%></td>
                                                                </tr> 
                                                            </tbody>
                                                        </table>
                                                        <%

                                                        } else {
                                                        %>
                                                        <table align="left" id="results" width="690px"
                                                               border="0" cellpadding="0" cellspacing="0" class="CSSTableGenerator">
                                                            <tr><td>
                                                                    <%
                                                                        // String contextPath = request.getContextPath();
                                                                        // out.println("<img  border='0' align='top'  src='"+contextPath+"/includes/images/alert.gif'/><b> No Records Found to Display!</b>");
                                                                        out.println("<img  border='0' align='top'  src='" + contextPath + "/includes/images/alert.gif'/><b>No records found for the given search criteria. Please try a different search criteria!</b>");
                                                                    %>
                                                                </td>
                                                            </tr>
                                                            <% }%>
                                                        </table>
                                                    </td>
                                                </tr>
                                                <%
                                                    total = 0;
                                                    inboundTotal = 0;
                                                    outboundTotal = 0;
                                                    filesizeTotal = 0;
                                                    filesizeTotal1 = 0;
                                                    if (list.size() != 0) {
                                                %>
                                                <% }%>
                                            </table>
                                        </div>
                                        <%-- Process butttons  start --%>
                                        <%
                                            if (list.size() != 0) {
                                        %>
                                        <table align="right">
                                            <tr>
                                                <!--                                    <td style="background-color: white;">-->
                                                <td class="gnexcel-space"><strong><input type="button" value="Generate Excel" class="btn btn-primary col-sm-12" onclick="return gridDownload('trackSummary', 'xls');" onmouseover="Tip('Click here to generate an excel Report.')" onmouseout="UnTip()" id="excel"/></strong>
                                                </td>
                                            </tr>
                                        </table> 
                                        <%}%>
                                    </div></div>
                            </div></div>
                    </section>
                    <%-- process buttons end--%>
                    <%-- Grid End --%>
                </s:if> 
                <script language="javascript">
                    function toggle(id, i, k) {
                        for (var j = k; j < i; j++) {
                            var ele = document.getElementById("toggleText" + j);
                            var text = document.getElementById(id);
                            if (ele.style.display == "none") {
                                ele.removeAttribute("style");
                                document.getElementById("toggleTd" + j).style.borderBottom = "none";
                                //text.innerHTML = "-";
                                text.style.backgroundImage = "url('../includes/images/minus.png')";
                            }
                            else {
                                ele.style.display = "none";
                                //text.innerHTML = "+";
                                text.style.backgroundImage = "url('../includes/images/plus.png')";
                            }
                        }
                    }
                </script>
            </div>
            <script>
                $('input[name="daterange"]').daterangepicker();
            </script>
            <script src='<s:url value="../includes/plugins/datatables/jquery.dataTables.min.js"/>'></script>
            <script src='<s:url value="../includes/plugins/datatables/dataTables.bootstrap.min.js"/>'></script>
        </div>
        <div>
            <s:include value="../includes/template/footer.jsp"/>
        </div>

        <!-- jQuery 2.1.4 -->
        <script src='<s:url value="../includes/plugins/jQuery/jQuery-2.1.4.min.js"/>'></script>
        <!-- jQuery UI 1.11.4 -->
        <script src="https://code.jquery.com/ui/1.11.4/jquery-ui.min.js"></script>
        <!-- Resolve conflict in jQuery UI tooltip with Bootstrap tooltip -->
        <script>
                $.widget.bridge('uibutton', $.ui.button);
        </script>
        <!-- Bootstrap 3.3.5 -->
        <script src='<s:url value="../includes/bootstrap/js/bootstrap.min.js"/>'></script>
        <!-- Morris.js charts -->
        <script src="https://cdnjs.cloudflare.com/ajax/libs/raphael/2.1.0/raphael-min.js"></script>
        <script src='<s:url value="../includes/plugins/morris/morris.min.js"/>'></script>
        <!-- Sparkline -->
        <script src='<s:url value="../includes/plugins/sparkline/jquery.sparkline.min.js"/>'></script>
        <!-- jvectormap -->
        <script src='<s:url value="../includes/plugins/jvectormap/jquery-jvectormap-1.2.2.min.js"/>'></script>
        <script src='<s:url value="../includes/plugins/jvectormap/jquery-jvectormap-world-mill-en.js"/>'></script>
        <!-- jQuery Knob Chart -->
        <script src='<s:url value="../includes/plugins/knob/jquery.knob.js"/>'></script>
        <!-- daterangepicker -->
        <!--<script src="https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.10.2/moment.min.js"></script>-->
        <script src='<s:url value="../includes/plugins/daterangepicker/daterangepicker.js"/>'></script>
        <!-- datepicker -->
        <!-- Bootstrap WYSIHTML5 -->
        <script src='<s:url value="../includes/plugins/bootstrap-wysihtml5/bootstrap3-wysihtml5.all.min.js"/>'></script>
        <!-- Slimscroll -->
        <script src='<s:url value="../includes/plugins/slimScroll/jquery.slimscroll.min.js"/>'></script>
        <!-- FastClick -->
        <script src='<s:url value="../includes/plugins/fastclick/fastclick.min.js"/>'></script>
        <!-- AdminLTE App -->
        <script src='<s:url value="../includes/bootstrap/js/app.min.js"/>'></script>
        <!-- AdminLTE dashboard demo (This is only for demo purposes) -->
        <script src='<s:url value="../includes/bootstrap/js/pages/dashboard.js"/>'></script>
        <!-- AdminLTE for demo purposes -->
        <script src="../includes/bootstrap/js/demo.js"></script>
        <%--   	</div> --%>
    </body>
</html>