<!DOCTYPE html>

<%-- <%@ page contentType="text/html" pageEncoding="UTF-8"%> --%>
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

<html>
    <head>
        <title>Miracle Supply Chain Visibility portal</title>
        <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
        <link rel="stylesheet" href='<s:url value="/includes/bootstrap/css/userdefined.css"/>'>
        <link rel="stylesheet" href='<s:url value="/includes/plugins/datatables/dataTables.bootstrap.css"/>' type="text/css">
        <script type="text/javascript">
            function doOnLoad()
            {
                var configFlowFlag = $('#configFlowFlag').val();
                var configFlowFlag1 = $('#configFlowFlag1').val();
                if ((configFlowFlag == 'manufacturing') || (configFlowFlag1 == 'manufacturing')) {
                    $("#manufacturing").addClass("active");
                    $("#deliverychannel").addClass("active");
                    $("#config").addClass("active");
                    $("#deliverychannel i").addClass("text-red");
                } else if ((configFlowFlag == 'logistics') || (configFlowFlag1 == 'logistics')) {
                    $("#logistics").addClass("active");
                    $("#ltdeliverychannel").addClass("active");
                    $("#ltconfig").addClass("active");
                    $("#ltdeliverychannel i").addClass("text-red");
                }
                document.getElementById('loadingAcoountSearch').style.display = "none";
            }

            function resetvalues()
            {
                document.getElementById("partnerId").value = "-1";
                document.getElementById("routerId").value = "-1";
                document.getElementById("sequence").value = "";
                document.getElementById("businessProcessId").value = "-1";
                document.getElementById("translationMapId").value = "-1";
                document.getElementById("documentExtractMapId").value = "-1";
                document.getElementById("archiveFlag").value = "YES";
                document.getElementById("archiveDirectory").value = "";
                document.getElementById("outputFileName").value = "";
                document.getElementById("outputFormat").value = "APP";
                document.getElementById("producerMailMapId").value = "";
                document.getElementById("status").value = "ACTIVE";
                document.getElementById("encodingMailMapId").value = "";

                // alert("test");
            }
        </script>

    </head>

    <body class="hold-transition skin-blue sidebar-mini"  onload="doOnLoad();
            setStyle('mainTp', 'delChnInfo')">
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
                    <h1>Delivery&nbsp;Channel&nbsp;Information Edit
                        <!--                        <small>Manufacturing</small>-->
                    </h1>
                    <!--                    <ol class="breadcrumb">
                                            <li><a href="#"><i class="fa fa-wrench"></i>Manufacturing</a></li>
                                            <li class="active">Delivery&nbsp;Channel&nbsp;Information Edit</li>
                                        </ol>   -->
                </s:if><s:elseif test="%{configFlowFlag1 == 'logistics'}">
                    <h1>Delivery&nbsp;Channel&nbsp;Information Edit<small>Logistics</small></h1>
                    <ol class="breadcrumb">
                        <li><a href="#"><i class="fa fa-truck"></i>Logistics</a></li>
                        <li class="active">Delivery&nbsp;Channel&nbsp;Information Edit</li>
                    </ol>   
                </s:elseif>

                <s:if test="%{configFlowFlag == 'manufacturing'}">
                    <h1>Delivery&nbsp;Channel&nbsp;Information Add
                        <!--                        <small>Manufacturing</small>-->
                    </h1>
                    <!--                    <ol class="breadcrumb">
                                            <li><a href="#"><i class="fa fa-wrench"></i>Manufacturing</a></li>
                                            <li class="active"> Delivery&nbsp;Channel&nbsp;Information Add</li>
                                        </ol>-->
                </s:if><s:elseif test="%{configFlowFlag == 'logistics'}">
                    <h1>Delivery&nbsp;Channel&nbsp;Information Add<small>Logistics</small></h1>
                    <ol class="breadcrumb">
                        <li><a href="#"><i class="fa fa-truck"></i>Logistics</a></li>
                        <li class="active"> Delivery&nbsp;Channel&nbsp;Information Add</li>
                    </ol>
                </s:elseif>
            </section>

            <br>
            <div id="loadingAcoountSearch" class="loadingImg">
                <span id ="LoadingContent" > <img src="<s:url value="/includes/images/Loader2.gif"/>"   ></span>
            </div>
            <section class="content">
                <div class="row">

                    <div class="box box-primary">
                        <div class="box-body">
                            <div class="col-sm-12"> 


                                <%
                                    if (request.getAttribute(AppConstants.REQ_RESULT_MSG) != null) {
                                        String reqponseString = request.getAttribute(AppConstants.REQ_RESULT_MSG).toString();
                                        //request.getSession(false).removeAttribute("responseString");
                                        out.println(reqponseString);
                                    }
                                %>
                                <div id="text">
                                    <div  style="alignment-adjust:central;" >
                                        <%String contextPath = request.getContextPath();
                                        %>

                                        <s:form action="%{formAction}" method="post" name="deliveryChannelForm" id="deliveryChannelForm" theme="simple">
                                            <s:hidden name="deliveryChannelId" value="%{deliveryChannelId}" id="deliveryChannelId"/>
                                            <s:hidden name="configFlowFlag" value="%{configFlowFlag}" id="configFlowFlag"/> 
                                            <s:hidden name="configFlowFlag1" value="%{configFlowFlag1}" id="configFlowFlag1"/> 
                                            <div class="form-group">
                                                <div class="row">
                                                    <div class="col-sm-12">
                                                        <div class="row">
                                                            <div class="col-sm-3"> <label>Partner&nbsp; Name</label>
                                                                <s:select headerKey="-1" cssClass="form-control" headerValue="Select Type" list="partnerMap" name="partnerId" id="partnerId" value="%{partnerId}"  />
                                                                <s:hidden name="partnerId" value="%{partnerId}" id="partnerId"/>
                                                            </div>
                                                            <div class="col-sm-3"> <label>Routing&nbsp; Name</label>
                                                                <s:select headerKey="-1" cssClass="form-control" headerValue="Select Type" list="routerMap" name="routerId" id="routerId" value="%{routerId}"  />
                                                                <s:hidden name="routerId" value="%{routerId}" id="routerId"/>
                                                            </div>
                                                            <div class="col-sm-3"><label>Sequence</label>
                                                                <s:textfield cssClass="form-control"  name="sequence" id="sequence" tabindex="1" value="%{sequence}" />   
                                                            </div>   
                                                            <div class="col-sm-3"> <label>Business&nbsp;Process&nbsp;Name</label>
                                                                <s:select headerKey="-1" cssClass="form-control" headerValue="Select Type" list="businessProcessMap" name="businessProcessId" id="businessProcessId" value="%{businessProcessId}"  />
                                                                <s:hidden name="businessProcessId" value="%{businessProcessId}" id="businessProcessId"/>
                                                            </div>
                                                        </div>
                                                        <br>
                                                        <div class="row">
                                                            <div class="col-sm-3"> <label>Translation&nbsp;Map&nbsp;Name</label>
                                                                <s:select headerKey="-1" cssClass="form-control" headerValue="Select Type" list="translationMap" name="translationMapId" id="translationMapId" value="%{translationMapId}"  />
                                                                <s:hidden name="translationId" value="%{translationId}" id="translationId"/>
                                                            </div>
                                                            <div class="col-sm-3"> <label>Document&nbsp;Extract&nbsp;Map&nbsp;Name</label>
                                                                <s:select headerKey="-1" cssClass="form-control" headerValue="Select Type" list="documentExtractMap" name="documentExtractMapId" id="documentExtractMapId" value="%{documentExtractMapId}"  />
                                                                <s:hidden name="documentExtarctId" value="%{documentExtarctId}" id="documentExtarctId"/>
                                                            </div>
                                                            <div class="col-sm-3"><label>Archive &nbsp; Flag</label>
                                                                <s:select list="#@java.util.LinkedHashMap@{'0':'YES','1':'NO'}"  cssClass="form-control" name="archiveFlag" id="archiveFlag" value="%{archiveFlag}" tabindex="13"/>
                                                            </div>
                                                            <div class="col-sm-3"><label>Archive &nbsp;Directory</label>
                                                                <s:textfield cssClass="form-control" name="archiveDirectory" id="archiveDirectory" value="%{archiveDirectory}" tabindex="2"/>
                                                            </div>
                                                        </div>
                                                        <br>
                                                        <div class="row">
                                                            <div class="col-sm-3"><label>Output &nbsp;File&nbsp;Name</label>
                                                                <s:textfield cssClass="form-control" name="outputFileName" id="outputFileName" value="%{outputFileName}" tabindex="3"/>
                                                            </div>
                                                            <div class="col-sm-3"><label>Output &nbsp;Format</label>
                                                                <s:select list="#@java.util.LinkedHashMap@{'APP':'APP','EDI':'EDI','XML':'XML'}"  cssClass="form-control" name="outputFormat" id="outputFormat" value="%{outputFormat}" tabindex="13"/>
                                                            </div>  
                                                            <div class="col-sm-3"><label>Producer&nbsp;Mail&nbsp;Box</label>
                                                                <s:select list="producerMailMap" cssClass="form-control"  name="producerMailMapId" id="producerMailMapId" value="%{producerMailMapId}" tabindex="4"/>
                                                                <s:hidden name="producerMailBoxId" value="%{producerMailBoxId}" id="producerMailBoxId"/>
                                                            </div> 
                                                            <div class="col-sm-3"><label>Status</label>
                                                                <s:select list="#@java.util.LinkedHashMap@{'ACTIVE':'ACTIVE','INACTIVE':'INACTIVE'}"  cssClass="form-control" name="status" id="status" value="%{status}" tabindex="5"/>
                                                            </div> 
                                                        </div>
                                                        <br>
                                                        <div class="row">
                                                            <div class="col-sm-3"><label>Encoding</label>
                                                                <s:select list="encodingMailMap" cssClass="form-control" name="encodingMailMapId" id="encodingMailMapId" value="%{encodingMailMapId}" tabindex="6"/>  
                                                                <s:hidden name="encodingMailBoxId" value="%{encodingMailBoxId}" id="encodingMailBoxId"/>
                                                            </div>
                                                        </div></div></div></div>
                                            <br>
                                            <span id="span1">
                                            </span>
                                            <div class="row">
                                                <div class="col-sm-2"><s:submit value="Save" Class="btn btn-primary col-sm-12" tabindex="7"/></div>

                                                <div class="col-sm-2"><strong><input type="button" value="Reset"  tabindex="8" class="btn btn-primary col-sm-12" onclick="return resetvalues();"/></strong></div>

                                            </s:form>       
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="col-sm-1"></div>
                </div>
            </section>
            <script>
                $(function () {
                    $("#example1").DataTable();
                    $('#results').DataTable({
                        "paging": true,
                        "lengthChange": true,
                        "searching": true,
                        "ordering": true,
                        "info": true,
                        "autoWidth": false,
                        "scrollY": 300,
                        "scrollX": true,
                    });
                });
            </script>


            <!--
                                <script type="text/javascript">
                                    var pager = new Pager('results', 10); 
                                    pager.init(); 
                                    pager.showPageNav('pager', 'pageNavPosition'); 
                                    pager.showPage(1);
                                </script>-->
        </div>
        <div>
            <s:include value="../includes/template/footer.jsp"/>
        </div>


        <script>
            $('input[name="daterange"]').daterangepicker();
        </script>
        <script language="JavaScript" src='<s:url value="/includes/js/generalValidations.js"/>'></script>
        <script language="JavaScript" src='<s:url value="/includes/js/GeneralAjax.js"/>'></script>
        <script src='<s:url value="../includes/bootstrap/js/app.min.js"/>'></script>
        <script src='<s:url value="../includes/plugins/datatables/jquery.dataTables.min.js"/>'></script>
        <script src='<s:url value="../includes/plugins/datatables/dataTables.bootstrap.min.js"/>'></script>




    </body>


</html>



