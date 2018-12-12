<!DOCTYPE html>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page import="com.mss.ediscv.util.AppConstants"%>
<html>
    <head>
    </script>
    <meta charset="utf-8">
    <title>Miracle Supply Chain Visibility Portal</title>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <meta http-equiv="pragma" content="no-cache" />
    <meta http-equiv="cache-control" content="no-cache" />
    <!-- Tell the browser to be responsive to screen width -->
    <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
    <link rel="stylesheet" href='<s:url value="/includes/bootstrap/css/userdefined.css"/>'>
    <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>

    <script>
        function doOnLoad()
        {
            var configFlowFlag = $('#configFlowFlag').val();
            var configFlowFlag1 = $('#configFlowFlag1').val();
            if ((configFlowFlag == 'manufacturing') || (configFlowFlag1 == 'manufacturing')) {
                $("#manufacturing").addClass("active");
                $("#b2bchannel").addClass("active");
                $("#config").addClass("active");
                $("#b2bchannel i").addClass("text-red");
            } else if ((configFlowFlag == 'logistics') || (configFlowFlag1 == 'logistics')) {
                $("#logistics").addClass("active");
                $("#ltb2bchannel").addClass("active");
                $("#ltconfig").addClass("active");
                $("#ltb2bchannel i").addClass("text-red");
            }
            document.getElementById('loadingAcoountSearch').style.display = "none";
        }
    </script>

</head>
<body class="hold-transition skin-blue sidebar-mini" onload="doOnLoad();
        setStyle('mainTp', 'b2bChannelList');">
    <script type="text/javascript" src='<s:url value="/includes/js/wz_tooltip.js"/>'></script>
    <div>
        <s:include value="/includes/template/header.jsp"/>
    </div>
    <div>
        <s:include value="/includes/template/sidemenu.jsp"/> 
    </div>
    <!-- Left side column. contains the logo and sidebar -->
    <!-- Content Wrapper. Contains page content -->
    <div class="content-wrapper">
        <!-- Content Header (Page header) -->
        <!-- Main content --> 
        <section class="content-header">
            <s:if test="%{configFlowFlag1 == 'manufacturing'}">

                <h1>B2BChannel&nbsp;Edit<small>Manufacturing</small></h1>
                <ol class="breadcrumb">
                    <li><a href="#"><i class="fa fa-wrench"></i>Manufacturing</a></li>
                    <li class="active"> B2BChannel&nbsp;Edit</li>
                </ol>   
            </s:if><s:elseif test="%{configFlowFlag1 == 'logistics'}">

                <h1>B2BChannel&nbsp;Edit<small>Logistics</small></h1>
                <ol class="breadcrumb">
                    <li><a href="#"><i class="fa fa-truck"></i>Logistics</a></li>
                    <li class="active"> B2BChannel&nbsp;Edit</li>
                </ol>   
            </s:elseif>

            <s:if test="%{configFlowFlag == 'manufacturing'}">
                <h1>B2BChannel&nbsp;Add<small>Manufacturing</small></h1>
                <ol class="breadcrumb">
                    <li><a href="#"><i class="fa fa-wrench"></i>Manufacturing</a></li>
                    <li class="active">  B2BChannel&nbsp;Add</li>
                </ol>

            </s:if><s:elseif test="%{configFlowFlag == 'logistics'}">
                <h1>B2BChannel&nbsp;Add<small>Logistics</small></h1>
                <ol class="breadcrumb">
                    <li><a href="#"><i class="fa fa-truck"></i>Logistics</a></li>
                    <li class="active">  B2BChannel&nbsp;Add</li>
                </ol>
            </s:elseif>
        </section>
        <br>
        <div id="loadingAcoountSearch" class="loadingImg">
            <span id ="LoadingContent" > <img src="<s:url value="/includes/images/Loader2.gif"/>"   ></span>
        </div>
        <section class="content">
            <div class="row">
                <div class="col-sm-12"> 
                    <!--box-->
                    <div class="box box-primary">
                        <div class="box-body">
                            <div id="text">
                                <!-- /.col (right) -->
                                <!-- First Row -->
                                <div class="content" >
                                    <div class="content_item" id="searchdiv">
                                        <s:if test="%{formAction == 'doAddB2BChannel'}">
                                            <h3></h3>   
                                        </s:if><s:else>
                                            <h3></h3>   
                                        </s:else>

                                        <%
                                            if (request.getAttribute(AppConstants.REQ_RESULT_MSG) != null) {
                                                String reqponseString = request.getAttribute(AppConstants.REQ_RESULT_MSG).toString();
                                                out.println(reqponseString);
                                            }
                                        %>
                                        <span id="resultMessage"></span>
                                        <div  style="alignment-adjust:central;" >
                                            <% String contextPath = request.getContextPath();%>
                                            <s:form action="%{formAction}" method="post" name="b2bChannelForm" id="b2bChannelForm" theme="simple" onsubmit="return doAddB2BChannel();">
                                                <s:hidden name="sampleValue" id="sampleValue" value="2"/>
                                                <s:hidden name="configFlowFlag" value="%{configFlowFlag}" id="configFlowFlag"/>
                                                <s:hidden name="configFlowFlag1" value="%{configFlowFlag1}" id="configFlowFlag1"/> 
                                                <div class="form-group">
                                                    <div class="row">
                                                        <div class="col-sm-12">
                                                            <div class="row">
                                                                <s:hidden name="b2bChannelId" value="%{b2bChannelId}" id="b2bChannelId"/>
                                                                <div class="col-sm-3"> <label for="partnerName ">Partner Name <font color="black">*</font></label>
                                                                        <s:textfield cssClass="form-control" name="partnerName" id="partnerName" tabindex="1" value="%{partnerName}"/>
                                                                </div>
                                                                <div  class="col-sm-3">
                                                                    <label for="status">Status</label> 
                                                                    <s:select headerKey="" cssClass="form-control" headerValue="Select Type" list="#@java.util.LinkedHashMap@{'ACTIVE':'ACTIVE','INACTIVE':'INACTIVE'}" name="status" id="status" value="%{status}" tabindex="13" />
                                                                </div>
                                                                <div  class="col-sm-3">
                                                                    <label for="direction">Direction <font color="black">*</font></label>  
                                                                        <s:select headerKey="" cssClass="form-control" headerValue="Select Type" list="#@java.util.LinkedHashMap@{'INBOUND':'INBOUND','OUTBOUND':'OUTBOUND'}" name="direction" id="direction" value="%{direction}" tabindex="13" />
                                                                </div>
                                                                <div  class="col-sm-3">
                                                                    <label for="protocol">Protocol <font color="black">*</font></label>  
                                                                        <s:select headerKey="" cssClass="form-control" headerValue="Select Type" list="#@java.util.LinkedHashMap@{'FTP Client GET':'FTP Client GET','FTP Client PUT':'FTP Client PUT','Inbound Mailbox Routing':'Inbound Mailbox Routing'}" name="protocol" id="protocol" value="%{protocol}" tabindex="13" />
                                                                </div>
                                                            </div><br>
                                                            <div class="row">
                                                                <div class="col-sm-3">
                                                                    <label for="host">Host <font color="black">*</font></label>
                                                                        <s:textfield cssClass="form-control" name="host" id="host" value="%{host}" tabindex="4" />
                                                                </div>
                                                                <div class="col-sm-3">
                                                                    <label for="userName">Username <font color="black">*</font></label>
                                                                        <s:textfield cssClass="form-control" name="userName" id="userName" value="%{userName}" tabindex="5" /> 
                                                                </div>
                                                                <div class="col-sm-3">
                                                                    <label for="producerMailBox">Producer Mailbox <font color="black">*</font></label>
                                                                        <s:textfield cssClass="form-control" name="producerMailBox" id="producerMailBox" value="%{producerMailBox}" tabindex="5" /> 
                                                                </div>
                                                                <div class="col-sm-3">
                                                                    <label for="consumerMailBox">Consumer Mailbox <font color="black">*</font></label>
                                                                        <s:textfield cssClass="form-control" name="consumerMailBox" id="consumerMailBox" value="%{consumerMailBox}" tabindex="5" /> 
                                                                </div>
                                                            </div><br>
                                                            <div class="row">
                                                                <div class="col-sm-3">
                                                                    <label for="pollingCode">Polling Code <font color="black">*</font></label>
                                                                        <s:textfield cssClass="form-control" name="pollingCode" id="pollingCode" value="%{pollingCode}" tabindex="5" /> 
                                                                </div>
                                                                <div class="col-sm-3">
                                                                    <label for="appId">App ID <font color="black">*</font></label>
                                                                        <s:textfield cssClass="form-control" name="appId" id="appId" value="%{appId}" tabindex="5" /> 
                                                                </div>
                                                                <div class="col-sm-3">
                                                                    <label for="senderId">Sender Id <font color="black">*</font></label>
                                                                        <s:textfield cssClass="form-control" name="senderId" id="senderId" value="%{senderId}" tabindex="5" /> 
                                                                </div>
                                                                <div class="col-sm-3">
                                                                    <label for="receiverId">Receiver Id <font color="black">*</font></label>
                                                                        <s:textfield cssClass="form-control" name="receiverId" id="receiverId" value="%{receiverId}" tabindex="5" />
                                                                </div>
                                                            </div>
                                                            <p>
                                                            </p>
                                                        </div>
                                                    </div>
                                                    <div>
                                                        <br>
                                                        <div class="row">
                                                            <s:if test="%{formAction == 'doAddB2BChannel'}">
                                                                <div class="col-sm-2"><s:submit value="Save" cssClass="btn btn-primary col-sm-12" tabindex="16" /></div>
                                                                <div class="col-sm-2"> <strong><input type="button" value="Reset" onclick="return resetvalues();" class="btn btn-primary col-sm-12" tabindex="16" /></strong></div>
                                                                    </s:if><s:else>
                                                                <div class="col-sm-2"><s:submit value="Update" cssClass="btn btn-primary col-sm-12" tabindex="16" /></div>
                                                                <div class="col-sm-2"> <strong><input type="button" value="Reset" onclick="return resetvalues();" class="btn btn-primary col-sm-12" tabindex="17" /></strong></div>
                                                                    </s:else>
                                                                </s:form>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div><!-- /.box-body -->
                                <!-- /.box-footer -->
                            </div><!-- /.box -->
                            <!--                    <a><img src='../includes/images/dtp/cal_plus.gif' alt="nag"width="13" height="9" border="0" onclick="javascript:hideSearch()" id="fsCollImg"/></a>  -->
                            <!--box-->
                        </div>
                        <div class="col-sm-1"></div>
                    </div>
                    </section>
                    <!--main content-->
                </div><!-- /.content-wrapper -->

            </div><!-- ./wrapper -->
            <div>
                <s:include value="../includes/template/footer.jsp"/>
            </div>       
            <script language="JavaScript" src='<s:url value="/includes/js/generalValidations.js"/>'></script>
            <script language="JavaScript" src='<s:url value="/includes/js/GeneralAjax.js"/>'></script>
            <script language="JavaScript" src='<s:url value="/includes/js/tpvalidations.js"/>'></script>
            <script src='<s:url value="/includes/bootstrap/js/app.min.js"/>'></script>
            <script type="text/javascript">

                                                                    function resetvalues()
                                                                    {
                                                                        document.getElementById("partnerName").value = "";
                                                                        document.getElementById("status").value = "ACTIVE";
                                                                        document.getElementById("direction").value = "";
                                                                        document.getElementById("protocol").value = "";
                                                                        document.getElementById("host").value = "";
                                                                        document.getElementById("userName").value = "";
                                                                        document.getElementById("producerMailBox").value = "";
                                                                        document.getElementById("consumerMailBox").value = "";
                                                                        document.getElementById("pollingCode").value = "";
                                                                        document.getElementById("appId").value = "";
                                                                        document.getElementById("senderId").value = "";
                                                                        document.getElementById("receiverId").value = "";

                                                                        // alert("test");
                                                                    }
            </script>
            </body>
            </html>