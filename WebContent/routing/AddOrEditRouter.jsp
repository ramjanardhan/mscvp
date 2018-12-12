<%-- <%@ page contentType="text/html" pageEncoding="UTF-8"%> --%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>


<%@page import="com.mss.ediscv.util.AppConstants"%>



<html>
    <head>
        <meta charset="utf-8">

        <title>Miracle Supply Chain Visibility Portal</title>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
        <meta http-equiv="pragma" content="no-cache" />
        <meta http-equiv="cache-control" content="no-cache" />
        <!-- Tell the browser to be responsive to screen width -->
        <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
        <link rel="stylesheet" href='<s:url value="/includes/bootstrap/css/userdefined.css"/>'>
        <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>
        <script type="text/javascript">
            function doOnLoad()
            {
                var configFlowFlag = $('#configFlowFlag').val();
                var configFlowFlag1 = $('#configFlowFlag1').val();
                if ((configFlowFlag == 'manufacturing') || (configFlowFlag1 == 'manufacturing')) {
                    $("#manufacturing").addClass("active");
                    $("#routing").addClass("active");
                    $("#config").addClass("active");
                    $("#routing i").addClass("text-red");
                } else if ((configFlowFlag == 'logistics') || (configFlowFlag1 == 'logistics')) {
                    $("#logistics").addClass("active");
                    $("#ltrouting").addClass("active");
                    $("#ltconfig").addClass("active");
                    $("#ltrouting i").addClass("text-red");
                }
                document.getElementById('loadingAcoountSearch').style.display = "none";
            }
            function resetvalues()
            {
                document.getElementById("name").value = "";
                document.getElementById("status").value = "ACTIVE";
                document.getElementById("acceptorLookupAlias").value = "";
                document.getElementById("envelope").value = "";
                document.getElementById("internalRouteEmail").value = "";
                document.getElementById("destMailBox").value = "";
                document.getElementById("systemType").value = "";
                document.getElementById("direction").value = "";
            }
        </script>

    </head>
    <%
        String check = null;
        if (request.getAttribute("check") != null) {
            check = request.getAttribute("check").toString();
        }

        //System.out.println("check-->"+check);
    %>

    <body class="hold-transition skin-blue sidebar-mini" onload="doOnLoad();
            setStyle('mainTp', 'routingList');
            loadDestLabel();">  
        <script type="text/javascript" src='<s:url value="/includes/js/wz_tooltip.js"/>'></script>

        <div>
            <s:include value="../includes/template/header.jsp"/>
        </div>
        <div>
            <s:include value="../includes/template/sidemenu.jsp"/>
        </div>
        <!-- Content Wrapper. Contains page content -->
        <div class="content-wrapper">
            <!-- Content Header (Page header) -->


            <!-- Main content --> 
            <section class="content-header">
                <s:if test="%{configFlowFlag1 == 'manufacturing'}">
                    <h1>Routing&nbsp;Edit<small>Manufacturing</small></h1>
                    <ol class="breadcrumb">
                        <li><a href="#"><i class="fa fa-wrench"></i>Manufacturing</a></li>
                        <li class="active">  Routing&nbsp;Edit</li>
                    </ol>  
                </s:if><s:elseif test="%{configFlowFlag1 == 'logistics'}">
                    <h1>Routing&nbsp;Edit<small>Logistics</small></h1>
                    <ol class="breadcrumb">
                        <li><a href="#"><i class="fa fa-truck"></i>Logistics</a></li>
                        <li class="active">  Routing&nbsp;Edit</li>
                    </ol>   
                </s:elseif>
                <s:if test="%{configFlowFlag == 'manufacturing'}">
                    <h1>Routing&nbsp;Add<small>Manufacturing</small></h1>
                    <ol class="breadcrumb">
                        <li><a href="#"><i class="fa fa-wrench"></i>Manufacturing</a></li>
                        <li class="active">  Routing&nbsp;Add</li>
                    </ol> 

                </s:if><s:elseif test="%{configFlowFlag == 'logistics'}">
                    <h1>Routing&nbsp;Add<small>Logistics</small></h1>
                    <ol class="breadcrumb">
                        <li><a href="#"><i class="fa fa-truck"></i>Logistics</a></li>
                        <li class="active">  Routing&nbsp;Add</li>
                    </ol> 
                </s:elseif>
            </section>    
            <br>

            <div id="loadingAcoountSearch" class="loadingImg">
                <span id ="LoadingContent" > <img src="<s:url value="/includes/images/Loader2.gif"/>"   ></span>
            </div>
            <section class="content">

                <div class="box box-primary">
                    <div class="box-body">
                        <div id="text">
                            <div class="content" >
                                <div class="content" id="searchdiv">
                                    <%                                        if (request.getAttribute(AppConstants.REQ_RESULT_MSG) != null) {
                                            String reqponseString = request.getAttribute(AppConstants.REQ_RESULT_MSG).toString();
                                            //request.getSession(false).removeAttribute("responseString");
                                            out.println(reqponseString);
                                        }
                                    %>
                                    <span id="resultMessage"></span>
                                    <div  style="alignment-adjust:central;" >
                                        <%String contextPath = request.getContextPath();
                                        %>



                                        <s:form action="%{formAction}" method="post" name="routingForm" id="routingForm" theme="simple" onsubmit="return doAddRouting();">
                                            <s:hidden name="configFlowFlag" value="%{configFlowFlag}" id="configFlowFlag"/>
                                            <s:hidden name="configFlowFlag1" value="%{configFlowFlag1}" id="configFlowFlag1"/> 
                                            <div class="form-group">
                                                <div class="row">
                                                    <div class="col-sm-12">
                                                        <div class="row">
                                                            <s:hidden name="routingId" value="%{routingId}" id="routingId"/>
                                                            <div class="col-sm-3"> <label>Name* </label>
                                                                <s:textfield cssClass="form-control" name="name" id="name" tabindex="1" value="%{name}"/>
                                                            </div>



                                                            <div  class="col-sm-3">

                                                                <label>Status</label> 
                                                                <s:select list="{'ACTIVE','INACTIVE'}" name="status" id="status" value="%{status}" tabindex="13" cssClass="form-control"/>
                                                            </div>

                                                            <div  class="col-sm-3">
                                                                <label>Acceptor Lookup Alias* </label>  
                                                                <s:textfield cssClass="form-control" name="acceptorLookupAlias" id="acceptorLookupAlias" tabindex="2" value="%{acceptorLookupAlias}" />
                                                            </div>

                                                            <div  class="col-sm-3">
                                                                <label>Envelope </label>  
                                                                <s:select headerKey="" cssClass="form-control" headerValue="" list="#@java.util.LinkedHashMap@{'DIFFERED':'DIFFERED','IMMEDIATE':'IMMEDIATE'}" name="envelope" id="envelope" value="%{envelope}" tabindex="13" />
                                                            </div>


                                                        </div>
                                                        <br>
                                                        <div class="row">
                                                            <div class="col-sm-3">
                                                                <label>Internal Route Email</label>
                                                                <s:textfield cssClass="form-control" name="internalRouteEmail" id="internalRouteEmail" value="%{internalRouteEmail}" tabindex="4" />
                                                            </div>
                                                            <div class="col-sm-3">
                                                                <label>Destination Mailbox </label>
                                                                <s:textfield cssClass="form-control" name="destMailBox" id="destMailBox" value="%{destMailBox}" tabindex="5" /> 
                                                            </div>
                                                            <div class="col-sm-3">
                                                                <label for="ackStatus">System Type</label>
                                                                <s:select list="#@java.util.LinkedHashMap@{'Not Applicable':'Not Applicable'}" cssClass="form-control" name="systemType" id="systemType" value="%{systemType}" tabindex="13" />
                                                            </div>
                                                            <div class="col-sm-3">
                                                                <label for="status">Direction* </label>
                                                                <s:select list="#@java.util.LinkedHashMap@{'INBOUND':'INBOUND','OUTBOUND':'OUTBOUND'}" cssClass="form-control"  name="direction" id="direction" value="%{direction}" tabindex="13" />
                                                            </div>
                                                        </div>


                                                    </div>


                                                </div>
                                                <br>
                                                <div class="row">
                                                    <s:if test="%{formAction == 'doAddRouting'}">
                                                        <div class="col-sm-2"><s:submit value="Save" cssClass="btn btn-primary col-sm-12" tabindex="16"/></div>
                                                        <div class="col-sm-2"> <strong><input type="button" value="Reset" onclick="return resetvalues();" class="btn btn-primary col-sm-12" tabindex="33" /></strong></div>
                                                            </s:if><s:else>
                                                        <div class="col-sm-2"><s:submit value="Update" cssClass="btn btn-primary col-sm-12" tabindex="16"/></div>
                                                        <div class="col-sm-2"> <strong><input type="button" value="Reset" onclick="return resetvalues();" class="btn btn-primary col-sm-12" tabindex="33" /></strong></div>
                                                            </s:else>
                                                            <s:hidden name="sampleValue" id="sampleValue" value="2"/>

                                                </s:form>
                                            </div>
                                        </div>
                                        <!--                                        <a><img src='../includes/images/dtp/cal_plus.gif' alt="nag"width="13" height="9" border="0" onclick="javascript:hideSearch()" id="fsCollImg"/></a>  -->
                                    </div>
                                </div></div>


                            </section>


                        </div>

                        <div>
                            <s:include value="../includes/template/footer.jsp"/>
                        </div>
                        <script language="JavaScript"  src='<s:url value="/includes/js/generalValidations.js"/>'></script>
                        <script language="JavaScript"  src='<s:url value="/includes/js/GeneralAjax.js"/>'></script>
                        <script language="JavaScript"  src='<s:url value="/includes/js/tpvalidations.js"/>'></script>
                        <script src='<s:url value="../includes/bootstrap/js/app.min.js"/>'></script>
                        </body>
                        </html>
