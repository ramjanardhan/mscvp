<!DOCTYPE html>


<%-- <%@ page contentType="text/html" pageEncoding="UTF-8"%> --%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>


<%@page import="com.mss.ediscv.util.AppConstants"%>
<html>
    <head>

        <meta charset="utf-8">
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
                if ((configFlowFlag == 'manufacturing') || (configFlowFlag1 == 'manufacturing')) {
                    $("#manufacturing").addClass("active");
                    $("#partner").addClass("active");
                    $("#config").addClass("active");
                    $("#partner i").addClass("text-red");
                } else if ((configFlowFlag == 'logistics') || (configFlowFlag1 == 'logistics')) {
                    $("#logistics").addClass("active");
                    $("#ltpartner").addClass("active");
                    $("#ltconfig").addClass("active");
                    $("#ltpartner i").addClass("text-red");

                }
                document.getElementById('loadingAcoountSearch').style.display = "none";
            }
            function goToAddPartner() {
                var configFlowFlag = document.getElementById("configFlowFlag").value;
                window.location = "../webconfig/addPartner.action?configFlowFlag=" + configFlowFlag;
            }
            function resetvalues1()
            {
                document.getElementById("partnerName").value = "";
                document.getElementById("status").value = "ACTIVE";
                document.getElementById("partnerIdentifier").value = "";
                document.getElementById("internalIdentifier").value = "";
                document.getElementById("applicationId").value = "";
                document.getElementById("countryCode").value = "";

                $('#gridDiv').hide();
                // alert("test");
            }

            function getPartnerList() {
                var configFlowFlag1 = $('#configFlowFlag1').val();
                var status = $('#status').val();
                    location.href = "../partner/partnerSearch.action?configFlowFlag=" + configFlowFlag1 + "&status=" + status;
                   // location.href = "../partner/partnerSearch.action?configFlowFlag=" + configFlowFlag1 + "&status=" + status;
                //   return true;
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
    <body class="hold-transition skin-blue sidebar-mini" onload="doOnLoad();
            setStyle('mainTp', 'partnerList');"><script type="text/javascript" src='<s:url value="/includes/js/wz_tooltip.js"/>'></script>

        <div class="wrapper">
            <s:include value="/includes/template/header.jsp"/>  
            <s:include value="/includes/template/sidemenu.jsp"/> 

        </div>

        <!-- Left side column. contains the logo and sidebar -->


        <!-- Content Wrapper. Contains page content -->
        <div class="content-wrapper">
            <!-- Content Header (Page header) -->


            <!-- Main content --> 

            <s:if test="%{configFlowFlag1 == 'manufacturing'}">
                <section class="content-header">
                    <h1>Partner Edit
                        <!--                        <small>Manufacturing</small>-->
                    </h1>
                    <!--                    <ol class="breadcrumb">
                                            <li><a href="#"><i class="fa fa-wrench"></i>Manufacturing</a></li>
                                            <li class="active"> Partner Edit</li>
                                        </ol>  </section> -->
                </s:if><s:elseif test="%{configFlowFlag1 == 'logistics'}">
                    <section class="content-header">
                        <h1>Partner Edit
                            <!--                        <small>Logistics</small>-->
                        </h1>
                        <!--                    <ol class="breadcrumb">
                                                <li><a href="#"><i class="fa fa-truck"></i>Logistics</a></li>
                                                <li class="active">Partner Edit</li>
                                            </ol> -->
                    </section>
                </s:elseif>
                <s:if test="%{configFlowFlag == 'manufacturing'}">
                    <section class="content-header">
                        <h1>Add Partner
                            <!--                        <small>Manufacturing</small>-->
                        </h1>
                        <!--                    <ol class="breadcrumb">
                                                <li><a href="#"><i class="fa fa-wrench"></i>Manufacturing</a></li>
                                                <li class="active">  Add Partner</li>
                                            </ol>-->
                    </section>
                </s:if><s:elseif test="%{configFlowFlag == 'logistics'}">
                    <section class="content-header">
                        <h1>Add Partner
                            <!--                        <small>Logistics</small>-->
                        </h1>
                        <!--                    <ol class="breadcrumb">
                                                <li><a href="#"><i class="fa fa-truck"></i>Logistics</a></li>
                                                <li class="active">  Add Partner</li>
                                            </ol>-->
                    </section>
                </s:elseif>
               
                <section class="content">
                    <div class="box box-primary">
                        <div class="box-body" style="padding-top:0px;">
                            <div id="text">
                                <!-- /.col (right) -->
                                <!-- First Row -->
                                <s:hidden name="tppageId" value="%{tppageId}" id="tppageId"/>

                                <%--  <s:textfield value="%{formAction}"/>--%>
                                <div class="content" >
                                    <div class="content_item" id="searchdiv">
                                        <s:if test="%{formAction == 'doAddPartner'}">
                                            <h3></h3>   
                                        </s:if><s:else>
                                            <h3></h3>   
                                        </s:else>
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

                                            <s:form action="%{formAction}" method="post" name="partnerForm" id="partnerForm" theme="simple" onsubmit="return doAddPartner();">
                                                <s:hidden name="sampleValue" id="sampleValue" value="2"/>
                                                <s:hidden name="configFlowFlag" value="%{configFlowFlag}" id="configFlowFlag"/> 
                                                <s:hidden name="configFlowFlag1" value="%{configFlowFlag1}" id="configFlowFlag1"/> 
                                                <s:hidden name="formAction" id="formAction" value="%{formAction}"/>
                                                <div class="form-group">
                                                    <div class="row">
                                                        <div class="col-sm-12">
                                                            <div class="row">
                                                                <s:hidden name="partnerId" value="%{partnerId}" id="partnerId"/>
                                                                <div class="col-xs-3"> <label for="partnerName ">Partner Name <font color="red">*</font></label>

                                                                    <s:textfield cssClass="form-control" name="partnerName" id="partnerName" value="%{partnerName}" tabindex="1"/>
                                                                </div>



                                                                <div  class="col-xs-3">
                                                                    <label for="status">Status</label> 
                                                                    <s:select list="#@java.util.LinkedHashMap@{'ACTIVE':'ACTIVE','INACTIVE':'INACTIVE'}" name="status" id="status" value="%{status}"  cssClass="form-control" tabindex="2"/>
                                                                </div>

                                                                <div  class="col-xs-3">
                                                                    <label for="internalIdentifier">Internal Identifier </label>  
                                                                    <s:textfield cssClass="form-control" name="internalIdentifier" id="internalIdentifier"  value="%{internalIdentifier}" tabindex="3"/>
                                                                </div>

                                                                <div  class="col-xs-3">
                                                                    <label for="partnerIdentifier">Partner Identifier <font color="red">*</font></label>  
                                                                        <s:textfield cssClass="form-control" name="partnerIdentifier" id="partnerIdentifier"  value="%{partnerIdentifier}" tabindex="4"/>
                                                                </div>
                                                                <div id="loadingAcoountSearch" class="loadingImg">
                                                                    <span id ="LoadingContent" > <img src="<s:url value="/includes/images/Loader2.gif"/>"   ></span>
                                                                </div>

                                                            </div>
                                                            
                                                            <div class="row">
                                                                <div class="col-xs-3">
                                                                    <label for="applicationId">Application ID</label>
                                                                    <s:textfield cssClass="form-control" name="applicationId" id="applicationId" value="%{applicationId}" tabindex="5" />
                                                                </div>
                                                                <div class="col-xs-3">
                                                                    <label for="corrvalue">Country Code</label>
                                                                    <s:textfield cssClass="form-control" name="countryCode" id="countryCode" value="%{countryCode}" tabindex="6" />
                                                                </div>

                                                            </div>
                                                            <p>

                                                            </p>


                                                        </div>

                                                    </div>
                                                    <div>

                                                        
                                                        <div class="row">
                                                            <s:if test="%{formAction == 'doAddPartner'}">
                                                                <div class="col-sm-2"><s:submit value="Save" cssClass="btn btn-primary col-sm-12" tabindex="7"/></div>
                                                                <div class="col-sm-2"> <strong><input type="button" value="Reset" onclick="return resetvalues1();" class="btn btn-primary col-sm-12" tabindex="8" /></strong></div>
                                                                    </s:if><s:else>
                                                                <div class="col-sm-2"><s:submit value="Update" cssClass="btn btn-primary col-sm-12" tabindex="9"/></div>
                                                             <%--   <s:url var="myUrl" action="../partner/partnerSearch.action">
                                                                    <s:param name="status" value="%{status}"/>
                                                                </s:url>  --%>
                                                                <div class="col-sm-2">  
                                                                    <strong><input type="button" value="BackToList" class="btn btn-primary col-sm-12" onclick="return getPartnerList();" tabindex="9"/></strong></div>        
                                                                <!-- <div class="col-sm-2"> <strong><input type="button" value="Reset" onclick="return resetPartvalues();" class="btn btn-primary col-sm-12" tabindex="10" /></strong></div>-->
                                                                <!-- <div class="col-sm-2">  <strong><input type="button" value="BackToList" class="btn btn-primary col-sm-12" onclick="return getPartnerList();" tabindex="11"/></strong></div>-->
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

    <script language="JavaScript"  src='<s:url value="/includes/js/generalValidations.js"/>'></script>
    <script language="JavaScript"  src='<s:url value="/includes/js/GeneralAjax.js"/>'></script>
    <script language="JavaScript"  src='<s:url value="/includes/js/tpvalidations.js"/>'></script>
    <script src='<s:url value="/includes/bootstrap/js/app.min.js"/>'></script>
</body>
</html>
