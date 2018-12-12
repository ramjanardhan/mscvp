<%-- 
    Document   : TrackSummary
    Created on : Aug 10, 2015, 3:08:55 PM
    Author     : miracle
--%>


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

        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <title>Miracle Supply Chain Visibility Portal</title>
        <!-- Tell the browser to be responsive to screen width -->
        <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
        <link rel="stylesheet" href='<s:url value="/includes/bootstrap/css/userdefined.css"/>'>
        <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>
        <script type="text/javascript"> 
            function doOnLoad() 
            {
                var configFlowFlag = $('#configFlowFlag').val();
                var configFlowFlag1 = $('#configFlowFlag1').val();
                if((configFlowFlag == 'manufacturing') || (configFlowFlag1 == 'manufacturing')){
                    $("#manufacturing").addClass("active");
                    $("#scheduler").addClass("active");
                    $("#config").addClass("active");
                    $("#scheduler i").addClass("text-red");
                } else if((configFlowFlag == 'logistics') || (configFlowFlag1 == 'logistics')){
                    $("#logistics").addClass("active");
                    $("#ltscheduler").addClass("active");
                    $("#ltconfig").addClass("active");
                    $("#ltscheduler i").addClass("text-red");
                }
                document.getElementById('loadingAcoountSearch').style.display="none";
            }
            function checkUservalues() {
                var schtitle = document.getElementById('schtitle').value;
                var schType = document.getElementById('schType').value;
                var schhours = document.getElementById('schhours').value;
                var userEmail = document.getElementById('userEmail').value;
                var reportsType = document.getElementById('reportsType').value;
                if(schtitle=="") {
                    alert("Please enter Schedule Title !!");
                    return false;
                }
                if(schType=="-1") {
                    alert("Please select Schedule Type !!");
                    return false;
                }
                if(schhours=="-1") {
                    alert("Please enter Schedule Time !!");
                    return false;
                }
                if(userEmail=="") {
                    alert("Please select Active Users !!");
                    return false;
                }
                if(reportsType=="-1") {
                    alert("Please select Reports Type !!");
                    return false;
                }
            }
 
            function Reportstype()
            {
                var reporttype = document.getElementById('reportsType').value;
                if(reporttype =="EditrackingIn/Out" || reporttype =="EditrackingSummary" || reporttype =="EditrackingInquiry" || reporttype =="Dashbord"){
                    //                    alert("Undaer processing(InActive)");
                    return false;
                }
            }

            function resetvalues()
            {
                document.getElementById('schtitle').value="";
                document.getElementById('schType').value="-1";
                document.getElementById('schhours').value="-1";
                document.getElementById('schhrFormat').value="AM"; 
                document.getElementById('extranalmailids').value="";
                 document.getElementById('reportsType').value="";
            }   
            function getUserList(){
                  var configFlowFlag = $('#configFlowFlag').val();
                 if((configFlowFlag == 'manufacturing')){
                location.href = "../partner/getSchedularsearch.action?configFlowFlag="+configFlowFlag;
                 }
                  else if((configFlowFlag == 'logistics')){
                       location.href = "../partner/getSchedularsearch.action?configFlowFlag="+configFlowFlag;
                  }
                return true;
            }
            function goToSchdularReport() {
                window.location="../webconfig/getSchedularAdd.action";
            }   
            function getReportDelete(id){
                getDeleteReport(id);
            }
        </script>
       <style>
            .row{
                  margin-bottom: 5px; 
            }
            </style>
    </head>



    <%--<body onload="doOnLoad();initDateTime('docdatepickerfrom','docdatepicker','<%=check %>');setStyle('docRep','');">  --%>

    <body  class="hold-transition skin-blue sidebar-mini" onload="doOnLoad()">
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

    <!-- Content Wrapper. Contains page content -->
    <div class="content-wrapper">
        <div id="erroroverlay"></div>
        <!-- End Overlay -->
        <!-- Start Special Centered Box -->
        <!-- Content Header (Page header) -->  
        <s:hidden name="userPageId" value="%{userPageId}" id="userPageId"/>
        <section class="content-header">
            <s:if test="%{configFlowFlag == 'manufacturing'}">
                <% if (request.getAttribute("userPageId").toString().equals("0")) {%>
                <h1>Scheduler Creation
<!--                    <small> Manufacturing</small>-->
                </h1>    
                <%} else {%>
                <h1>Update Scheduler
<!--                    <small> Manufacturing</small>-->
                </h1> 
                <%}%>
<%--                <ol class="breadcrumb">
                    <li><a href="#"><i class="fa fa-wrench"></i>Manufacturing</a></li>
                    <% if (request.getAttribute("userPageId").toString().equals("0")) {%>
                    <li class="active"> Scheduler Creation </li>
                    <%} else {%>
                    <li class="active">  Update Scheduler </li>
                    <%}%>
                </ol>--%>
            </s:if><s:elseif test="%{configFlowFlag == 'logistics'}">
                <% if (request.getAttribute("userPageId").toString().equals("0")) {%>
                <h1>Scheduler Creation
<!--                    <small> Logistics</small>-->
                </h1>    
                <%} else {%>
                <h1>Update Scheduler
<!--                    <small> Logistics</small>-->
                </h1> 
                <%}%>
<!--                <ol class="breadcrumb">
                    <li><a href="#"><i class="fa fa-truck"></i>Logistics</a></li>
                    <% if (request.getAttribute("userPageId").toString().equals("0")) {%>
                    <li class="active"> Scheduler Creation </li>
                    <%} else {%>
                    <li class="active">  Update Scheduler </li>
                    <%}%>
                </ol>-->
            </s:elseif>

        </section>
        <div id="loadingAcoountSearch" class="loadingImg">
            <span id ="LoadingContent" > <img src="<s:url value="/includes/images/Loader2.gif"/>"   ></span>
        </div>
        <section class="content">
            <div class="row">
                <div class="col-sm-12"> 

                    <!--box-->
                    <div class="box box-primary">
                        <div class="box-header with-border">
                            <%
                                if (request.getAttribute(AppConstants.REQ_RESULT_MSG) != null) {
                                    String reqponseString = request.getAttribute(AppConstants.REQ_RESULT_MSG).toString();
                                    //request.getSession(false).removeAttribute("responseString");
                                    out.println(reqponseString);
                                }
                            %>

                            <div style="alignment-adjust:central;" >
                                <% String contextPath = request.getContextPath();%>
                                <div class="box-tools pull-right">

                                </div>
                            </div><!-- /.box-header -->
                            <div class="box-body">
                                <div id="text">

                                    <!-- /.col (right) -->
                                    <!-- First Row -->
                                    <div id="overlay"></div>              
                                    <div id="specialBox">
                                        <s:form action="%{currentAction}" method="post" name="schdularForm" id="schdularForm" theme="simple">
                                            <s:hidden name="configFlowFlag" value="%{configFlowFlag}" id="configFlowFlag"/>
                                            <s:hidden name="configFlowFlag1" value="%{configFlowFlag1}" id="configFlowFlag1"/> 
                                            <s:hidden name="id" value="%{id}" id="id"/>
                                            <div class="form-group">
                                                <div class="row">
                                                    <div class="col-sm-12">
                                                        <div class="row">
                                                            <div class="col-sm-4"> <label for="schtitle">Schedule Title </label><font style="color:red">*</font>
                                                                <s:textfield cssClass="form-control" name="schtitle" id="schtitle"  value="%{schtitle}" tabindex="1" />
                                                            </div>

                                                        </div>
                                                        <div class="row">

                                                            <div  class="col-sm-4">

                                                                <label for="schType">Schedule Type</label><font style="color:red">*</font> 
                                                                <s:select headerKey="-1" cssClass="form-control"  headerValue="Select Type" list="{'Daily','Weekely'}" name="schType" id="schType" value="%{schType}"   onchange="Showfun();" tabindex="2"/>
                                                            </div>

                                                            <div  class="col-sm-4">
                                                                <label for="schhrFormat">Schedule&nbsp;Time</label><font style="color:red">*</font> 



                                                                <s:select headerKey="-1" cssClass="form-control" headerValue="HH" list="{'01','02','03','04','05','06','07','08','09','10','11','12'}" name="schhours" id="schhours" value="%{schhours}" tabindex="3" />
                                                            </div>
                                                            <div class="col-sm-4"><br> <s:select  list="{'AM','PM'}" name="schhrFormat" id="schhrFormat" value="%{schhrFormat}"  cssClass="form-control" tabindex="4"/></div>
                                                        </div>


                                                        <div class="row">

                                                            <div  class="col-sm-4">
                                                                <label for="userEmail">Active Users</label><font style="color:red">*</font>
                                                                <s:select headerKey="-1" multiple="true" required="true" list="userMap" name="userEmail" id="userEmail" value="receiverids"  cssClass="form-control" tabindex="5"/>

                                                            </div>
                                                            <div class="col-sm-4">
                                                                <label for="extranalmailids">External User EmailIds</label>
                                                                <s:textarea  name="extranalmailids" id="extranalmailids"  value="%{extranalmailids}"  cssClass="form-control" onchange="SchEmailValidator(this);checkEmails(this);" tabindex="6"/>
                                                            </div>  

                                                            <div class="col-sm-4">
                                                                <label for="reportsType">Reports&nbsp;Type</label><font style="color:red">*</font>
                                                                <s:select headerKey="-1" cssClass="form-control"  headerValue="All" list="{'ExcelReport'}" name="reportsType" id="reportsType" value="%{reportsType}"   onchange="Reportstype();" tabindex="7"/>
                                                            </div>

                                                        </div>

                                                    </div>

                                                    <script>
   
                                                    </script>                                      


                                                </div>
                                            </div>
                                            <span id="span1">
                                            </span>
                                            <div class="row">
                                                <%
                                                    // out.println("pageId-->"+request.getAttribute("tppageId").toString());
                                                    if (request.getAttribute("userPageId").toString().equals("0")) {
                                                %>
                                                <div class="col-sm-2"> <s:submit value="Add" cssClass="btn btn-primary col-sm-12" onclick="return checkUservalues();" tabindex="8"/></div>
                                                <div class="col-sm-2"><strong><input type="button" value="Reset"  tabindex="9" class="btn btn-primary col-sm-12" onclick="return resetvalues();"/></strong></div>
                                                <div class="col-sm-2">  <strong><input type="button" value="BackToList" class="btn btn-primary col-sm-12" onclick="return getUserList();" tabindex="10"/></strong></div>
                                                        <%} else {%>
                                                <div class="col-sm-2"> <s:submit value="Update" cssClass="btn btn-primary col-sm-12"  tabindex="11" onclick="return checkUservalues();"/></div>

                                                <div class="col-sm-2">  <strong><input type="button" value="BackToList" class="btn btn-primary col-sm-12" onclick="return getUserList();" tabindex="12"/></strong></div>

                                                <%}%>

                                                <s:hidden name="sampleValue" id="sampleValue" value="2"/>

                                            </s:form>
                                        </div>

                                    </div>


                                </div>


                            </div>
                            <%--  out.print("contextPath-->"+contextPath); --%>
                        </div>
                    </div>
                    </div>
                    </div>
                        </section>


                </div>
                <script type="text/javascript">
                    var pager = new Pager('results', 10); 
                    pager.init(); 
                    pager.showPageNav('pager', 'pageNavPosition'); 
                    pager.showPage(1);
                </script>
         
    <div>
        <s:include value="../includes/template/footer.jsp"/>
    </div>
    <script>
        $('input[name="daterange"]').daterangepicker();
    </script>
    <script language="JavaScript"  src='<s:url value="/includes/js/GeneralAjax.js"/>'></script>
    <script language="JavaScript"  src='<s:url value="/includes/js/downloadAjax.js"/>'></script>
    <script language="JavaScript"  src='<s:url value="/includes/js/overlay.js"/>'></script>
    <script src='<s:url value="../includes/bootstrap/js/app.min.js"/>'></script>
    
</body>
</html>

