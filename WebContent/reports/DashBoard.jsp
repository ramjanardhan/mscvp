
<%@page import="com.mss.ediscv.reports.ReportsBean"%>
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
        <title>Miracle Supply Chain Visibility portal</title>
        <!-- Tell the browser to be responsive to screen width -->
        <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
        <link rel="stylesheet" href='<s:url value="/includes/bootstrap/css/userdefined.css"/>'>
        <link rel="stylesheet" href='<s:url value="/includes/plugins/daterangepicker/daterangepicker.css"/>' type="text/css"> 
        <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>
        <script type="text/javascript" src="https://www.google.com/jsapi"></script>

        <script>
            google.load("visualization", "1", {packages: ["corechart"]});
            function doOnLoad() {
                $("#dashboardLM").addClass("active");
                 $("#dashboardM").addClass("active");
                //  $("#dashboard2").addClass("active");
                $("#dashboard").addClass("active");
                $("#dashboard i").addClass("text-red");
                document.getElementById('loadingAcoountSearch').style.display = "none";
            }
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



            function resetvaluesManuDash()
            {
                document.getElementById('docdatepickerfrom').value = "";
                document.getElementById('docdatepicker').value = "";
                document.getElementById('docType').value = "-1";
                document.getElementById('reportrange').value = "";
                document.getElementById('status').value = "-1";
                document.getElementById('ackStatus').value = "-1";
                $('#gridDiv').hide();
                $('#detail_box').hide();

            }
            /* $(document).ready(function() {
             $('ul.sf-menu').sooperfish();
             });*/






        </script>

        <style>
            #excels{

                margin-left: 81%;
            }
            #excel{
                float:right;
                margin-right: -21%;
            }
            .space_1{
                padding-top: 2%;
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
            <s:include value="../includes/template/header.jsp"/>
        </div>
        <div>
            <s:include value="../includes/template/sidemenu.jsp"/>
        </div>

        <div class="content-wrapper">
            <!-- Content Header (Page header) -->


            <!-- Main content --> 

            <section class="content-header">

                <%String contextPath = request.getContextPath();
                %>
                <h1>
                    Dashboard
<!--                    <small>Manufacturing</small>-->
                </h1>
<!--                <ol class="breadcrumb">
                    <li><a href="#"><i class="fa fa-wrench"></i>Manufacturing</a></li>
                    <li class="active">Dashboard</li>
                </ol>-->
            </section>

            <section class="content">

                <div class="box box-primary">
                    <div class="box-header with-border">
                        <!--<h3 class="box-title">Dashboard</h3>-->
                        <div class="box-tools pull-right">

                        </div>
                    </div><!-- /.box-header -->
                    <div class="box-body">
                        <div id="text">


                            <!-- /.col (right) -->
                            <!-- First Row -->
                            <s:form action="#" method="post" name="dashboardForm" id="dashboardForm" theme="simple">
                                <div class="form-group">
                                    <div class="row">

                                        <div class="col-sm-12">
                                            <div class="row">
                                                <div class="col-sm-3"> <label>Date Range</label>
                                                    <s:textfield name="reportrange"  id="reportrange" cssClass="form-control pull-left"   value="%{reportrange}" onchange="Date1();" tabindex="1"/> 
                                                </div>

                                                <script type="text/javascript">
        function Date1()
        {
            // alert("in to date1");
           if(document.dashboardForm.reportrange.value!=null) {
            var date = document.dashboardForm.reportrange.value;
            // alert(date);
            var arr = date.split("-");

            var x = arr[1].trim();
            //alert("assigning");
            document.getElementById("docdatepickerfrom").value = arr[0];
            document.getElementById("docdatepicker").value = x;

            var datefrom = document.getElementById("docdatepickerfrom").value;
          //  alert(datefrom);
            var dateto = document.getElementById("docdatepicker").value;
            //alert(dateto);
        }
        }
                                                </script>

                                                <s:hidden id="docdatepickerfrom" name="docdatepickerfrom" />
                                                <s:hidden id="docdatepicker" name="docdatepicker"/>
                                                <!--                                                                    <script type="text/javascript">
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
                                                <div class="col-sm-3"> <label for="docType">Document Type</label>
                                                    <s:select headerKey="-1" headerValue="Select Type" list="docTypeList" name="docType" id="docType" value="%{docType}"  cssClass="form-control" tabindex="2"/>
                                                </div>

                                                <div class="col-sm-3">  <label for="partnerMapId">Trading Partner Name </label>
                                                    <s:select  
                                                        name="partnerMapId" 
                                                        id="partnerMapId"
                                                        headerKey="ALL"
                                                        headerValue="All"
                                                        list="partnerMap" 
                                                        tabindex="3" cssClass="form-control"
                                                        value="" />
                                                </div><!-- /.box -->
                                            </div>


                                            
                                            <div class="row">

                                                <div class="col-sm-3" for="ackStatus"><label>Ack Status</label>
                                                    <s:select headerKey="-1" headerValue="Select Type" list="{'Overdue','Accepted','Rejected'}" name="ackStatus" id="ackStatus" value="%{ackStatus}"  cssClass="form-control" tabindex="4"/> 
                                                </div>
                                                <div class="col-sm-3" >   <label for="status">Status</label>
                                                    <s:select headerKey="-1" headerValue="Select Type" list="{'Success','Error','Warning'}" name="status" id="status" value="%{status}"  cssClass="form-control" tabindex="5"/> 
                                                </div>

                                            </div> 
                                        </div><!-- /.box -->
                                    </div>  
                                       
                                    <div class="row">
                                        <div class="col-sm-2"> <strong><input type="button" value="Generate" class="btn btn-primary col-sm-12"  onclick="getDashboardDeatls('M')" tabindex="6"/></strong>

                                        </div>
                                        <div class="col-sm-2"><strong><input type="reset" value="Reset" class="btn btn-primary col-sm-12"  onclick="return resetvaluesManuDash();" tabindex="7"/></strong> </div>


                                        <s:hidden name="sampleValue" id="sampleValue" value="2"/>
                                        <div id="loadingAcoountSearch" class="loadingImg">
                                            <span id ="LoadingContent" > <img src="<s:url value="/includes/images/Loader2.gif"/>"   ></span>
                                        </div>
                                    </div>
                                    <!--box-->
                                </s:form>

                            </div>


                        </div></div></div>
            </section>
            <%--- GRid start --%>

            <section class="content">

                <div class="content grid_space" id="gridDiv" style="display: none;overflow: hidden">
                    <div class="content_item">

<!--                        <div id="loadingImage" align="center"><img  src="../includes/images/ajax-loader.gif" /></div>-->
                            <s:hidden id="inboundTrans" name="inboundTrans"/>
                            <s:hidden id="outboundTrans" name="outboundTrans"/>
                        <div id="tblCharts" style="display: none" class="grid_space">
                            <div class="row">

                                <div class="col-md-5">    
                                    <div id="inboundPiechart" style="width: 400px; height: 350px;" ></div></div>
                                <div class="col-md-5">
                                    <div id="outboundPiechart" style="width: 400px; height: 350px;" ></div></div>
                            </div>

                            <div id="dashboardButtons" class="col-sm-12 gnexcel-space">
                                <div class="col-sm-10" class="space_1"> 
                                    <strong class="col-sm-2 pull-right"><input type="button" value="Generate Excel" class="btn btn-primary" onclick="return gridDashboardDownload('dashboard', 'xls', document.getElementById('inboundTrans').value, document.getElementById('outboundTrans').value);" onmouseover="Tip('Click here to generate an excel Report.')" onmouseout="UnTip()"/> </strong> 
                                    <strong class="col-sm-2 pull-right"><input type="button" value="Generate PDF" class="btn btn-primary" onclick="return gridDashboardDownload('dashboard', 'pdf', document.getElementById('inboundTrans').value, document.getElementById('outboundTrans').value);" onmouseover="Tip('Click here to generate an pdf Report.')" onmouseout="UnTip()"/> </strong>  
                                </div>

                            </div>

                            <%-- process buttons end--%>
                            <%-- Grid End --%>

                        </div></div></div>
            </section>

        </div> 


        <script type="text/javascript">
            var pager = new Pager('results', 10);
            pager.init();
            pager.showPageNav('pager', 'pageNavPosition');
            pager.showPage(1);
        </script>


        <%--    <div id="footer">  --%>
        <div>
            <s:include value="../includes/template/footer.jsp"/>
        </div>  
        <%--   	</div> --%>
        <script>
            $('input[name="daterange"]').daterangepicker();
        </script>
        <script type="text/javascript" src='<s:url value="../includes/plugins/datepicker/daterangepicker.js"/>'></script>
        <script language="JavaScript"  src='<s:url value="/includes/js/GeneralAjax.js"/>'></script>
        <script language="JavaScript"  src='<s:url value="/includes/js/downloadAjax.js"/>'></script>
        <script type="text/javascript" src='<s:url value="../includes/js/DateValidation.js"/>'></script>
        <script language="JavaScript"  src='<s:url value="/includes/js/overlay.js"/>'></script>
        <script src='<s:url value="../includes/plugins/daterangepicker/daterangepicker.js"/>'></script>
        <script src='<s:url value="../includes/bootstrap/js/app.min.js"/>'></script>
        <!-- AdminLTE dashboard demo (This is only for demo purposes) -->
        <script src='<s:url value="../includes/bootstrap/js/pages/dashboard.js"/>'></script>
    </body>
</html>