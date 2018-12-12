<%@page import="com.mss.ediscv.simon.SimonBean"%>
<%@page import="com.mss.ediscv.documentVisibility.DocumentVisibilityBean"%>
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page buffer="50kb" autoFlush="true" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/WEB-INF/tlds/dbgrid.tld" prefix="grd"%>
<%@ page import="com.freeware.gridtag.*"%>
<%@  page import="com.mss.ediscv.util.AppConstants"%>
<%@ page import="java.sql.SQLException"%>
<%@ page import = "java.util.ResourceBundle" %>
<!DOCTYPE html>
<html class=" js canvas canvastext geolocation crosswindowmessaging no-websqldatabase indexeddb hashchange historymanagement draganddrop websockets rgba hsla multiplebgs backgroundsize borderimage borderradius boxshadow opacity cssanimations csscolumns cssgradients no-cssreflections csstransforms no-csstransforms3d csstransitions  video audio localstorage sessionstorage webworkers applicationcache svg smil svgclippaths   fontface">
    <head>
        <title>Miracle Supply Chain Visibility portal</title>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
        <meta http-equiv="pragma" content="no-cache" />
        <meta http-equiv="cache-control" content="no-cache" />
        <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
        <!-- Bootstrap 3.3.5 -->
        <link rel="stylesheet" href='<s:url value="/includes/bootstrap/css/userdefined.css"/>'>
        <link rel="stylesheet" href='<s:url value="/includes/plugins/datatables/dataTables.bootstrap.css"/>' type="text/css">
        <link rel="stylesheet" href='<s:url value="/includes/plugins/daterangepicker/daterangepicker.css"/>' type="text/css">
        <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>
        <script language="JavaScript" src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
        <script type="text/javascript">
            function doOnLoad() {
                $('#simonscreen').addClass('active');
                $('#transactionSearch').addClass('active');
                $('#transactionSearch i').addClass('text-red');
                document.getElementById('loadingAcoountSearch').style.display = "none";
            }

        </script>
        <style>

            .content_item p{
                font-size: 17px;
            }

            .modal-dialog1 {
                width: 70%;
                margin: 30px auto;
            }

            .contact-form {
                margin: 0 2%;
            }
            .modal-header1 {
                padding: 4px;
                border-bottom: 1px solid #e5e5e5;
                min-height: 16.428571429px;
            }

            .close {
                float: right;
                font-size: 34px;
                font-weight: 700;
                line-height: 1;
                color: #000;
                text-shadow: 0 1px 0 #fff;
                opacity: .2;
            }
            .modal {
                position: absolute;
            }

            .modal-right {
                margin: 30px;
                float: right;
            }




            .navbar-header{
                width: 209px !important; 
                margin: 1px auto !important; 
            }


            .bx_shadow{
                box-shadow: 0pt 2px 5px rgba(105, 108, 109, 0.7), 0px 0px 8px 5px rgba(208, 223, 226, 0.4) inset;
            }
            .register
            {text-align: right;margin: 27px 0;}

            .register img{
                width:34px;
                height:38px;
            }
            table, td, th 
            {    
                border: 1px solid black;
                text-align: left;

            }
            /*th{background-color:  #ffa366;}*/
            table {
                border-collapse: collapse;
                width:65%;
            }

            th, td {
                padding: 15px;
            }
            .transactioninf{
                cursor: pointer;
            }
            .ScrollStyle
            {
                max-height: 500px;
                overflow-y: scroll;
            }
            .ScrollStyle2
            {
                max-height: 100px;
                overflow-y: scroll;
            }
            .table-condensed  {
                border-right: 0;
                border-bottom: 0;
                border-collapse: collapse;
            }
        </style>
        <style>
            .row{
                  margin-bottom: 5px; 
            }
            </style>
    </head>

    <body onload="doOnLoad();" class="hold-transition skin-blue sidebar-mini">    
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

                <h1 style="float:left;">
                    Transaction-Search

                </h1>
                <!--<i class="glyphicon glyphicon-info-sign" onmouseover="help();" style="align:right;color: #00aae7;margin-left: 750px;"></i>-->
                <i style="color: #00aae7;margin-left: 750px" class="glyphicon glyphicon-info-sign" data-toggle="modal" data-target="#modal2" id="b2" onmouseover="ModelOpen();"></i>
            </section>
            <!--            <section class="content-header">
                            <h5 class="span glyphicon glyphicon-play">Basic Search</h5>
                             style="padding-right:10px;padding-left:10px;" 
                        </section>-->
            <div class="row">
                <div class="col-sm-12">
                    <section class="content">
                        <div class="box box-primary">
                            <!--<div class="box-header with-border"></div>-->
                            <div class="box-body">
                                <div id="text">
                                    <div style="alignment-adjust:central;" >
                                        <% String contextPath = request.getContextPath();%>
                                        <s:form action="../simon/transactionSearchDetails.action" method="post" name="transactionSearchForm" id="transactionSearchForm" theme="simple">
                                            <s:hidden id="datepickerfrom" name="datepickerfrom" />
                                            <s:hidden id="datepickerTo" name="datepickerTo"/>
                                            <div class="form-group">
                                                <div class="row">
                                                    <div class="col-sm-12">
                                                        <div class="row">
                                                            <div class="col-sm-2"> <label>Partner Name</label>
                                                                <s:textfield name="partnerName"  id="partnerName" cssClass="form-control "   value="%{partnerName}"  tabindex="1"/>
                                                            </div>
                                                            <div class="col-sm-2" > <label>Application Id</label>
                                                                <s:textfield name="applicationId"  id="applicationId" cssClass="form-control"   value="%{applicationId}"  tabindex="2"/>
                                                            </div>
                                                            <div class="col-sm-2" > <label>Sender Id</label>
                                                                <s:textfield name="senderId"  id="senderId" cssClass="form-control"   value="%{senderId}"  tabindex="3"/>
                                                            </div>
                                                            <div class="col-sm-2" > <label>Receiver Id</label>
                                                                <s:textfield name="recId"  id="recId" cssClass="form-control"   value="%{recId}"  tabindex="4"/>
                                                            </div>
                                                            <div class="col-sm-2" > <label>Transaction Type</label>
                                                                <s:select  headerKey="-1"  headerValue="Select" list="#@java.util.LinkedHashMap@{'850':'850','855':'855','856':'856','810':'810','820':'810','ORDERS':'ORDERS','ORDRSP':'ORDRSP','DESADV':'DESADV','INVOIC':'INVOIC'}" cssClass="form-control"  name="transactionType" id="transactionType" value="%{transactionType}"  tabindex="5"/>
                                                            </div>
                                                            <div class="col-sm-2" > <label>Document Type</label>
                                                                <s:select  headerKey="-1"  headerValue="Select" list="#@java.util.LinkedHashMap@{'EDI':'EDI','APP':'APP','XML':'XML','SAP':'SAP','IDOC':'IDOC'}" cssClass="form-control"  name="documentType" id="documentType" value="%{documentType}"  tabindex="6"/>
                                                            </div>
                                                        </div>
                                                        <div class="row">


                                                            <div class="col-sm-2"> <label>Transaction Direction</label>
                                                                <s:select  headerKey="-1"  headerValue="Select" list="#@java.util.LinkedHashMap@{'Inbound':'InBound','Outbound':'OutBound'}" cssClass="form-control"  name="direction" value="%{direction}" id="direction" tabindex="7"  />
                                                            </div>
                                                            <div class="col-sm-2"> <label>Delivery Channel</label>
                                                                <s:textfield name="deliveryChannel"  id="deliveryChannel" cssClass="form-control"   value="%{deliveryChannel}"  tabindex="8"/>
                                                            </div>
                                                            <div class="col-sm-4"> <label>Created Date</label>
                                                                <s:textfield name="reportrange"  id="reportrange" cssClass="form-control"   value="%{reportrange}" onchange="Date1();" tabindex="9"/>

                                                            </div>
                                                            <script type="text/javascript">
                                                                function Date1() {
                                                                    var date = document.transactionSearchForm.reportrange.value;
                                                                    var arr = date.split("-");
                                                                    var x = arr[1].trim();
                                                                    document.getElementById("datepickerfrom").value = arr[0];
                                                                    document.getElementById("datepickerTo").value = x;

                                                                }
                                                            </script>
                                                            <div class="col-sm-2"> <label>Status</label>
                                                                <s:select  headerKey="-1"  headerValue="Select" list="#@java.util.LinkedHashMap@{'Success':'SUCCESS','Error':'ERROR','Warning':'WARNING'}" cssClass="form-control"  name="status" id="status" value="%{status}" tabindex="10" />
                                                            </div>
                                                            <div class="col-sm-2"> <label>Country Code</label>
                                                                <s:textfield name="countryCode"  id="countryCode" cssClass="form-control"   value="%{countryCode}"  tabindex="11"/>
                                                            </div>

                                                        </div>

                                                        <!--                                                        <div class="row">
                                                                                                                    
                                                                                                                    
                                                                                                                   
                                                                                                                </div>
                                                        
                                                                                                                <br>-->
                                                        <div class="row">
                                                            <div class="col-sm-2"> <label>Reference Name</label>

                                                                <s:select  headerKey="-1"  headerValue="Select" list="#@java.util.LinkedHashMap@{'PO Number':'PO Number','Shipment ID':'Shipment ID','Invoice Number':'Invoice Number'}" cssClass="form-control"  name="referenceName" value="%{referenceName}" id="referenceName" tabindex="12"  />
                                                            </div>   
                                                            <div class="col-sm-2"><label>Reference Value</label>
                                                                <s:textfield name="referenceValue"  id="referenceValue" cssClass="form-control "   value="%{referenceValue}"  tabindex="13"/>
                                                                <!--                                                                <span class="glyphicon glyphicon-plus-sign pull-right" style="top:-23px;margin-right: -17px"></span>-->
                                                            </div>
                                                            <div class="col-sm-2"><br>
                                                                <button  type="button" id="addButton" name="addButton" value="Add Div" class="btn btn-success"   style="margin-top:6px ;" tabindex="14"><i class="fa fa-plus"></i></button>

                                                            </div>
                                                            <div id="loadingAcoountSearch" class="loadingImg">
                                                                <span id ="LoadingContent" > <img src="<s:url value="/includes/images/Loader2.gif"/>"   ></span>
                                                            </div>   
                                                        </div>
                                                        <div id="ref" style="display: none;">
                                                            <div class="row">
                                                                <div class="col-sm-2"> <label for="referenceName1">Reference Name</label>

                                                                    <s:select  headerKey="-1"  headerValue="Select" list="#@java.util.LinkedHashMap@{'PO Number':'PO Number','Shipment ID':'Shipment ID','Invoice Number':'Invoice Number'}" cssClass="form-control"  name="referenceName1" value="%{referenceName1}" id="referenceName1" tabindex="15"  />
                                                                </div> 
                                                                <div class="col-sm-2">
                                                                    <label for="refvalue1">Reference Value</label>
                                                                    <s:textfield cssClass="form-control " name="referenceValue1"  id="referenceValue1" value="%{refvalue1}"  tabindex="16"/>
                                                                </div>
                                                                <div class="col-sm-2"><br>
                                                                    <button  type="button" id="removeButton1" name="removeButton1" value="Remove Div" class="btn btn-warning"   style="margin-top:6px ;"  tabindex="17" ><i class="fa fa-minus"></i></button>

                                                                </div>
                                                            </div>
                                                        </div>
                                                        <div id="ref1" style="display: none;">
                                                            <div class="row">
                                                                <div class="col-sm-2"> <label for="referenceName2">Reference Name</label>

                                                                    <s:select  headerKey="-1"  headerValue="Select" list="#@java.util.LinkedHashMap@{'PO Number':'PO Number','Shipment ID':'Shipment ID','Invoice Number':'Invoice Number'}" cssClass="form-control"  name="referenceName2" value="%{referenceName2}" id="referenceName2" tabindex="18"  />
                                                                </div> 

                                                                <div class="col-sm-2">
                                                                    <label for=refvalue2">Reference Value</label>
                                                                    <s:textfield cssClass="form-control " name="referenceValue2"  id="referenceValue2" value="%{refvalue2}"  tabindex="19"/>
                                                                </div>
                                                                <div class="col-sm-2"><br>
                                                                    <button  type="button" id="removeButton2" name="removeButton2" value="Remove Div" class="btn btn-warning"   style="margin-top:6px ;"  tabindex="20"><i class="fa fa-minus"></i></button>

                                                                </div>
                                                            </div></div>

                                                        <div class="row" >

                                                            <div class="col-sm-2">
                                                                <s:checkbox name="checkMe" fieldValue="true" value="true" label="Ignore Case" tabindex="21"/>
                                                                <label>Ignore Case </label>
                                                            </div> 
                                                            <div class="col-sm-2">
                                                                <s:checkbox name="checkMe" fieldValue="true" value="true" label="Use Wildcards" tabindex="22" />
                                                                <label>Use Wildcards</label>
                                                            </div>
                                                            <div class="col-sm-2" ><s:submit value="Search"  id="hideshow" cssClass="btn btn-primary col-sm-12" tabindex="23" /></div>
                                                            <div class="col-sm-2"><strong><input type="button" value="Reset" class="btn btn-primary col-sm-12" onclick="resetvalues();" tabindex="24"/></strong></div>    
                                                        </div>   






                                                        <!--                                                    <div class="row" style=" width:79%;">
                                                                                                                <div class="col-sm-2">
                                                        <%-- <s:checkbox name="checkMe" fieldValue="true" value="true" label="Ignore Case" tabindex="14"/>
                                                     </div>

                                                        <div class="col-sm-2">
                                                            <s:checkbox name="checkMe" fieldValue="true" value="true" label="Use Wildcards" tabindex="15" />
                                                        </div> --%>
                                                    </div>-->




                                                    </div>
                                                </div>
                                            </s:form>

                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </section>
                </div>  
            </div>

            <!--            <section class="content-header" style="top:-32px">
                            <h5 class="span glyphicon glyphicon-play">Advanced</h5>
                        </section>
            -->




            <div id="gridDiv" style="display:block">     
                <s:if test="#session.transactionSearchList!=null">
                    <%--- GRid start --%>
                    <section class="content">
                        <div class="row" id="hideshow1">
                            <div class="col-xs-12">
                                <div class="box">
<!--                                    <div class="box-header">
                                        <h3 class="box-title">Table</h3>
                                    </div> /.box-header -->
                                    <div class="box-body" style="overflow-x:auto;">
                                        <%!String cssValue = "whiteStripe";
                                            int resultsetTotal;%>
                                        <div>                 
                                            <table align="left" width="100%" border="0" cellpadding="0" cellspacing="0"  style="border-style: hidden;"
                                                   >
                                                <tr>
                                                    <td style="background-color: white;">
                                                        <div>
                                                            <table id="results"  class="table table-bordered table-hover">

                                                                <thead><tr>
                                                                        <th>Info</th>

                                                                        <th >Partner&nbsp;Name</th>                                                                     
                                                                        <th >Instance&nbsp;Id</th>                                                                     
                                                                        <th >Application&nbsp;Id</th>
                                                                        <th >Direction</th>
                                                                        <th >Transaction&nbsp;Type</th>
                                                                        <th >Document&nbsp;Type</th>
                                                                        <th >Sender&nbsp;Id</th>
                                                                        <th >Receiver&nbsp;Id</th>
                                                                        <th >IChg&nbsp;Control</th>
                                                                        <th >Grp&nbsp;Control</th>
                                                                        <th >Reference&nbsp;Name</th>
                                                                        <th >Reference&nbsp;Value</th>
                                                                        <th >Status</th>
                                                                        <th >Created&nbsp;Date</th>
                                                                    </tr>
                                                                </thead>
                                                                <tbody >
                                                                    <%
                                                                        java.util.List list = (java.util.List) session.getAttribute(AppConstants.TRANSACTION_SEARCH_LIST);

                                                                        if (list.size() != 0) {

                                                                            for (int i = 0; i < list.size(); i++) {
                                                                                SimonBean simonBean = (SimonBean) list.get(i);
                                                                    %>
                                                                    <tr>

                                                                        <td >
                                                                            <%-- <td class="transactioninf" onclick="getTransData('<%=simonBean.getPartnerName()%>', '<%=simonBean.getApplicationId()%>', '<%=simonBean.getDirection()%>', '<%=simonBean.getTransctionType()%>', '<%=simonBean.getDocumentType()%>', '<%=simonBean.getSenderId()%>', '<%=simonBean.getRecId()%>', '<%=simonBean.getIsaControlNumber()%>', '<%=simonBean.getGsControlNumber()%>', '<%=simonBean.getRefNumber()%>', '<%=simonBean.getRefValue()%>', '<%=simonBean.getStatus()%>', '<%=simonBean.getCreatedDate()%>')"><i class="fa fa-search" ></i> 
                                                                                <a href="" data-toggle="modal" data-target="#selfReg" onclick="getTransData(<%=simonBean.getPartnerName()%>);"><i class="fa fa-gears"></i> </a> --%>
                                                                <center>  <a style="color: green;" href='javascript:getTransData("<%=simonBean.getFileName()%>","<%=simonBean.getMailBoxName()%>","<%=simonBean.getMapName()%>","<%=simonBean.getInstanceId()%>","<%=simonBean.getPartnerName()%>", "<%=simonBean.getApplicationId()%>", "<%=simonBean.getDirection()%>", "<%=simonBean.getTransctionType()%>", "<%=simonBean.getDocumentType()%>", "<%=simonBean.getSenderId()%>", "<%=simonBean.getRecId()%>", "<%=simonBean.getIsaControlNumber()%>", "<%=simonBean.getGsControlNumber()%>", "<%=simonBean.getRefNumber()%>", "<%=simonBean.getRefValue()%>", "<%=simonBean.getStatus()%>", "<%=simonBean.getCreatedDate()%>")' id="acceptButton" ><span class="fa fa-search"></span></a></center>
                                                                </td>

                                                                <td>

                                                                    <%out.println(simonBean.getPartnerName());%>
                                                                </td>

                                                                <td>

                                                                    <%out.println(simonBean.getInstanceId());%>
                                                                </td>

                                                                <td><%out.println(simonBean.getApplicationId());%>
                                                                </td>

                                                                <td><%out.println(simonBean.getDirection());%>
                                                                </td>

                                                                <td><%out.println(simonBean.getTransctionType());%>
                                                                </td>

                                                                <td><%out.println(simonBean.getDocumentType());%>
                                                                </td>  

                                                                <td><%out.println(simonBean.getSenderId());%>
                                                                </td>

                                                                <td><%out.println(simonBean.getRecId());%>
                                                                </td>

                                                                <td><%out.println(simonBean.getIsaControlNumber());%>
                                                                </td>

                                                                <td><%out.println(simonBean.getGsControlNumber());%>
                                                                </td>

                                                                <td><%out.println(simonBean.getRefNumber());%>
                                                                </td>

                                                                <td><%out.println(simonBean.getRefValue());%>
                                                                </td>

                                                                <td><%out.println(simonBean.getStatus());%>
                                                                </td>
                                                                <td><%out.println(simonBean.getCreatedDate().toString().substring(0, simonBean.getCreatedDate().toString().lastIndexOf(":")));%>
                                                                </td>

                                                                </tr>
                                                                <%}
                                                                    }%>
                                                                </tbody>

                                                            </table>
                                                        </div>

                                                    </td>
                                                </tr>
                                            </table>
                                        </div>
                                        <%-- Process butttons  start --%>


                                    </div>
                                </div>
                            </div></div></section>
                            <%-- process buttons end--%>
                            <%-- Grid End --%>
                        </s:if>

            </div>
        </div>   
        <div class="modal fade" id="selfReg" data-backdrop="static" data-keyword="false" tabindex="-1" role="dialog" aria-labelledby="selfRegLabel">
            <div class="modal-dialog1" role="document">
                <div class="modal-content " style="margin:2%">
                    <div class="modal-header1"  style="border:0;background-color: #00aae7; ">
                        <button type="button" id="closeButton" style="color:#ff0000" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                        <h4 class="modal-title" id="selfRegLabel" style="text-align:center;padding:5px;color:white;">Transaction Information</h4>
                    </div>
                    <s:form action="" method="post" cssClass="contact-form"  name="" id="" theme="simple">
                        <s:hidden id="docType" name="docType" />
                        <div class="form-group" >
                            <div class="row ScrollStyle" style="border: 1px solid black;">
                                <div class="col-sm-12" >
                                    <div class="col-sm-6" >  
                                        <h4><strong style="color: #ff9900;"> Transaction Information</strong></h4>
                                        <div class="row"><div class="col-sm-7"><label>Transaction ID<span style="padding-left:70px;">:</span></label></div>
                                            <div class="col-sm-5"> <s:textfield style="border:none" name="transactionId"  id="transactionId"  value="%{transactionId}" tabindex="1" readonly="true"/></div></div>
                                        <div class="row"><div class="col-sm-7"><label>Partner Name<span style="padding-left:73px;">:</span></label></div>
                                            <div class="col-sm-5"><s:textfield style="border:none"  name="partnerName_modal"  id="partnerName_modal"  value="%{partnerName_modal}" tabindex="2" readonly="true"/> </div></div>
                                        <div class="row"><div class="col-sm-7"><label>Application ID<span style="padding-left:73px;">:</span></label></div>
                                            <div class="col-sm-5"><s:textfield style="border:none" name="applicationId_modal"  id="applicationId_modal"  value="%{applicationId_modal}" tabindex="3" readonly="true"/> </div></div>
                                        <div class="row"><div class="col-sm-7"> <label>Direction<span style="padding-left:103px;">:</span></label></div>
                                            <div class="col-sm-5"> <s:textfield style="border:none"  name="direction_modal"  id="direction_modal"  value="%{direction_modal}" tabindex="4" readonly="true"/> </div></div>
                                        <div class="row"><div class="col-sm-7"><label>Sender ID<span style="padding-left:100px;">:</span></label></div>
                                            <div class="col-sm-5"><s:textfield style="border:none" name="senderId_modal"  id="senderId_modal"  value="%{senderId_modal}" tabindex="5" readonly="true"/> </div></div>
                                        <div class="row"><div class="col-sm-7"><label>Receiver ID<span style="padding-left:90px;">:</span></label></div>
                                            <div class="col-sm-5"> <s:textfield style="border:none" name="receiverId_modal"  id="receiverId_modal"  value="%{receiverId_modal}" tabindex="6" readonly="true"/> </div></div>
                                        <div class="row"><div class="col-sm-7"><label>Reference Name<span style="padding-left:59px;">:</span></label></div>
                                            <div class="col-sm-5"> <s:textfield style="border:none" name="referenceName_modal"  id="referenceName_modal"  value="%{referenceName_modal}" tabindex="7" readonly="true"/> </div></div>
                                        <div class="row"><div class="col-sm-7"><label>Reference Value<span style="padding-left:60px;">:</span></label></div>
                                            <div class="col-sm-5"><s:textfield style="border:none" name="referenceValue_modal"  id="referenceValue_modal"  value="%{referenceValue_modal}" tabindex="8" readonly="true"/> </div></div>
                                        <div class="row"><div class="col-sm-7"><label>Map Name<span style="padding-left:94px;">:</span></label></div>
                                            <div class="col-sm-5"><s:textfield style="border:none" name="mapName"  id="mapName"  value="%{mapName}" tabindex="9" readonly="true"/> </div></div>
                                        <div class="row"><div class="col-sm-7"><label>Document Type<span style="padding-left:62px;">:</span></label></div>
                                            <div class="col-sm-5"><s:textfield style="border:none" name="documentType_modal"  id="documentType_modal"  value="%{documentType_modal}" tabindex="10" readonly="true"/> </div></div>
                                        <div class="row"><div class="col-sm-7"><label>Transaction Type<span style="padding-left:53px;">:</span></label></div>
                                            <div class="col-sm-5"><s:textfield style="border:none" name="transactionType_modal"  id="transactionType_modal"  value="%{transactionType_modal}" tabindex="11" readonly="true"/></div></div>
                                        <div class="row"><div class="col-sm-7"><label >Created Date<span style="padding-left:80px;">:</span></label></div>
                                            <div class="col-sm-5"><s:textfield style="border:none" name="createdDate_modal"  id="createdDate_modal"  value="%{createdDate_modal}" tabindex="12" readonly="true"/> </div></div>
                                        <!--                                        <div class="row"><div class="col-sm-7"><label>Changed Date<span style="padding-left:73px;">:</span></label></div>
                                                                                    <div class="col-sm-5"> <s:textfield style="border:none"  name="changedDate"  id="changedDate"  value="%{changedDate}" tabindex="13" readonly="true"/> </div></div>-->
                                        <div class="row"><div class="col-sm-7"> <label>Created User<span style="padding-left:79px;">:</span></label></div>
                                            <div class="col-sm-5"><s:textfield style="border:none" name="createdUser"  id="createdUser"  value="%{createdUser}" tabindex="14" readonly="true"/> </div></div>
                                        <!--                                        <div class="row"><div class="col-sm-7"><label>Changed User<span style="padding-left:73px;">:</span></label></div>
                                                                                    <div class="col-sm-5"><s:textfield style="border:none" name="changedUser"  id="changedUser"   value="%{changedUser}" tabindex="15" readonly="true"/> </div></div>-->
                                        <!--                                        <div class="row"><div class="col-sm-7"> </div>
                                                                                    <div class="col-sm-7"><s:select headerKey="-1" headerValue="Sent - Complete" cssClass="form-control" list="#@java.util.LinkedHashMap@{'SentFailed':'Sent - Failed','SentProcessing':'Sent - Processing','ArchivedInternal':'Archived Internal'}" name="currentStatus" id="currentStatus" value="%{currentStatus}" tabindex="16" /></div></div>-->
                                        <div class="row"><div class="col-sm-7"> <label>Current Status<span style="padding-left:70px;">:</span></label></div>
                                            <div class="col-sm-5">  
                                                <!--                                                <span class="glyphicon glyphicon-pencil" style="color:#ffa366" tabindex="17"></span>-->
                                                <s:select headerKey="-1" headerValue="Sent - Complete" cssClass="form-control" list="#@java.util.LinkedHashMap@{'SentFailed':'Sent - Failed','SentProcessing':'Sent - Processing','ArchivedInternal':'Archived Internal'}" name="currentStatus" id="currentStatus" value="%{currentStatus}" tabindex="16" />
                                            </div></div>
                                        <div class="row"><div class="col-sm-7"><label>Delivery Channel<span style="padding-left:55px;">:</span></label></div>
                                            <div class="col-sm-5"><s:textfield style="border:none" name="deliveryChannel"  id="deliveryChannel"  value="%{deliveryChannel}" tabindex="18" readonly="true"/> </div></div> 
                                        <h4><strong>File Links</strong></h4>
                                        <div class="row"><div class="col-sm-10"><label>Pre Translation File<span style="padding-left:40px;">:</span></label></div>
                                            <div class="col-sm-2"><span style="color: #00aae7;" tabindex="19" onclick="preTranslationtextDataView();"> view </span> </div></div>

                                        <div class="row"><div class="col-sm-10"><label>Post Translation File<span style="padding-left:34px;">:</span></label></div>
                                            <div class="col-sm-2"><span style="color: #00aae7;" tabindex="19" onclick="postTranslationtextDataView();"> view </span> </div></div>
                                        <div style="display:none;">     <p id="preTransalationPath" ></p>
                                            <p id="postTransalationPath"></p> </div>
                                    </div>
                                    <div class="col-sm-4">     
                                        <h4><strong style="color: #ff9900;">Envelope Reference</strong></h4> 
                                        <div class="row"><div class="col-sm-7"><label>InterChange<span style="padding-left:43px;">:</span></label></div>
                                            <div class="col-sm-5"> <s:textfield style="border:none" name="interChange"  id="interChange" value="%{interChange}" tabindex="20" readonly="true"/> </div></div>
                                        <div class="row"><div class="col-sm-7"><label>Group<span style="padding-left:80px;">:</span></label></div>
                                            <div class="col-sm-5"><s:textfield style="border:none" name="group"  id="group"  value="%{group}" tabindex="21" readonly="true"/> </div></div>
                                        <div class="row"><div class="col-sm-7"><label >Message<span style="padding-left:65px;">:</span></label></div>
                                            <div class="col-sm-5"> <s:textfield style="border:none" name="message"  id="message" value="%{message}" tabindex="22" readonly="true"/> </div></div>
                                    </div>

                                    <div class="col-sm-2">

                                        <strong><input type="button" value="Create PDF" class="btn btn-primary pull-right"style="margin-top:5px;" tabindex="23"></strong>
                                    </div>

                                </div> <br>
                                <div class="row">
                                    <div class="col-sm-12">
                                        <div class="col-sm-11">
                                            <h4><strong style="color: #00aae7;">Transaction References</strong></h4>
                                            <table style="width:100%;">
                                                <thead><tr>
                                                        <th>Name</th>
                                                        <th>value</th> 
                                                        <th >Created&nbsp;Date</th>
                                                        <th >Created&nbsp;By</th>
                                                    </tr> </thead> 
                                                <tbody>
                                                    <tr>
                                                        <td>AckStatus</td>
                                                        <td id="AckStatusValue"></td>
                                                        <td id="date1">Test</td>
                                                        <td>Admin</td>
                                                    </tr>
                                                    <tr>
                                                        <td>PurchaseOrderNumber</td>
                                                        <td id="PurchaseOrderNumberValue"></td>
                                                        <td id="date2">Test</td>
                                                        <td>Admin</td>
                                                    </tr>
                                                    <tr>
                                                        <td>PreTranslationFile</td>
                                                        <td id="OpenTextArchiveEDICommFileValue"></td>
                                                        <td id="date3">Test</td>
                                                        <td>Admin</td>
                                                    </tr>
                                                    <tr>
                                                        <td>PostTranslationFile</td>
                                                        <td id="OpenTextArchiveMetaDataFileValue"></td>
                                                        <td id="date4">Test</td>
                                                        <td>Admin</td>
                                                    </tr>
                                                    <tr>
                                                        <td>IDocNumber</td>
                                                        <td id="IDocNumberValue"></td>
                                                        <td id="date7">Test</td>
                                                        <td>Admin</td>
                                                    </tr>
                                                    <tr id="856trBol">
                                                        <td>BillOfLading</td>
                                                        <td id="BillOfLadingValue"></td>
                                                        <td id="date5">Test</td>
                                                        <td>Admin</td>
                                                    </tr>
                                                    <tr id="856trShipId">
                                                        <td>ShipmentID</td>
                                                        <td id="ShipmentIDValue"></td>
                                                        <td id="date6">Test</td>
                                                        <td>Admin</td>
                                                    </tr>
                                                    <tr id="810trInvNum">
                                                        <td>InvoiceNumber</td>
                                                        <td id="InvoiceNumberValue"></td>
                                                        <td id="date8">Test</td>
                                                        <td>Admin</td>
                                                    </tr>

                                                    <tr>
                                                        <td>Country</td>
                                                        <td id="State"></td>
                                                        <td id="date9">Test</td>
                                                        <td>Admin</td>
                                                    </tr>
                                                </tbody>
                                            </table> 
                                        </div>
                                    </div></div>
                                <div class="row col-sm-12">
                                    <div class="col-sm-11">
                                        <h4><strong style="color: #00aae7;">Transaction History</strong></h4>
                                        <label>Show All</label>
                                        <s:checkbox name="showAll" fieldValue="true"/>
                                        <table style="width:100%">
                                            <thead><tr>
                                                    <th >Change Value</th>
                                                    <th style="display: none">Old value</th> 
                                                    <th >Value</th>
                                                    <th >Message</th>
                                                    <th >Created Date</th>
                                                    <th >Created By</th>

                                                </tr> </thead> 
                                            <tbody>

                                                <tr>
                                                    <td>Transaction Status</td>
                                                    <td>Sent Complete</td>
                                                    <td id="sentComplete"></td>
                                                    <td id="crd">Test</td>
                                                    <td>Admin</td>
                                                </tr>
                                                <tr>
                                                    <td>Transaction Status</td>
                                                    <td>Translated</td>
                                                    <td id="tanslated">Test</td>
                                                    <td id="crd1">Test</td>
                                                    <td>Admin</td>
                                                </tr>
                                                <tr>
                                                    <td>Transaction Status</td>
                                                    <td>Archive Internal</td>
                                                    <td id="archiveInternal">Test</td>
                                                    <td id="crd2">Test</td>
                                                    <td>Admin</td>
                                                </tr>
                                                <tr>
                                                    <td>Transaction Status</td>
                                                    <td>Test</td>
                                                    <td>Test</td>
                                                    <td>Test</td>
                                                    <td>Admin</td>
                                                </tr>
                                            </tbody>
                                        </table> 
                                    </div> </div>
                                <div class="row col-sm-12" style="display: none">
                                    <div class="col-sm-12">
                                        <h4><strong style="color: #00aae7;">Transaction Children</strong></h4>
                                        <table style="width:100%">
                                            <thead><tr>
                                                    <th >View</th>
                                                    <th>InterChange</th>
                                                    <th>Group</th>
                                                    <th>Message</th>
                                                    <th >Transaction Type</th>
                                                    <th >Document Type</th>
                                                    <th >Reference Value</th>
                                                    <th >Created Date</th>
                                                    <th >Sender Id</th>
                                                    <th >Receiver Id</th>
                                                    <th >Status</th>
                                                </tr> </thead> 
                                            <tbody>
                                                <tr>
                                                    <td>Test</td>
                                                    <td>Test</td>
                                                    <td>Test</td>
                                                    <td>Test</td>
                                                    <td>Test</td>
                                                    <td>Test</td>
                                                    <td>Test</td>
                                                    <td>Test</td>
                                                    <td>Test</td>
                                                    <td>Test</td>
                                                    <td>Test</td>
                                                </tr>
                                                <tr>
                                                    <td>Test</td>
                                                    <td>Test</td>
                                                    <td>Test</td>
                                                    <td>Test</td>
                                                    <td>Test</td>
                                                    <td>Test</td>
                                                    <td>Test</td>
                                                    <td>Test</td>
                                                    <td>Test</td>
                                                    <td>Test</td>
                                                    <td>Test</td>
                                                </tr>
                                            </tbody>
                                        </table>
                                        <div style="visibility: hidden;">End</div>
                                    </div>
                                </div>

                            </div>       
                        </div>       
                    </s:form>
                </div>
            </div>
        </div>
        <div class="modal fade" id="modal2" tabindex="-1" role="dialog" aria-labelledby="modal2Label" aria-hidden="true" >
            <div class="modal-dialog modal-sm modal-right" >
                <div class="modal-content" onmouseleave="ModelHide();">
                    <div class="modal-header" style="border:0;background-color: #00aae7;">
                        <button type="button" id="closeButton" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                        <h3 class="modal-title" id="selfRegLabel" style="text-align:center;padding:7px;color:white;">Help</h3>
                    </div>
                    <div class="modal-body"> 
                        <ul>
                            <li>
                                <p>The transaction list section initially displays the results of the search criteria. By default most recent transactions are displayed. </p>

                            </li>
                            <li>
                                <p>The list section contains a table which displays information based on your configuration. Each row contains a binocular icon that can be clicked on it to reveal additional details about the transaction. </p>

                            </li>
                            <li>
                                <p>The results of the screen can also be exported into a CSV to PDF document.</p>

                            </li>
                        </ul></div>
                </div>
            </div>
        </div>
        <%-- Side box ends--%>
        <div>
            <s:include value="../includes/template/footer.jsp"/>
        </div>
        <script>
            $('input[name="daterange"]').daterangepicker();
        </script>
        <!-- Bootstrap 3.3.5 -->
        <script src='<s:url value="../includes/bootstrap/js/bootstrap.min.js"/>'></script>
        <script src='<s:url value="../includes/bootstrap/js/app.min.js"/>'></script>
        <script src='<s:url value="../includes/js/GeneralAjax.js"/>'></script>
        <script src='<s:url value="../includes/plugins/daterangepicker/daterangepicker.js"/>'></script>
        <script src='<s:url value="../includes/plugins/datatables/jquery.dataTables.min.js"/>'></script>
        <script src='<s:url value="../includes/plugins/datatables/dataTables.bootstrap.min.js"/>'></script>

        <script type="text/javascript" src='<s:url value="../includes/js/DateValidation.js"/>'></script>
        <script type="text/javascript">

            $(function () {
                $('#results').DataTable({
                    "paging": true,
                    "lengthChange": true,
                    "searching": true,
                    "ordering": true,
                    "info": true,
                    "autoWidth": false,
                     "scrollY": 300,
                    "scrollX": true,
                    "order": [[0, 'desc']]
                });
            });
//            $("#hideshow").click(function () {
//                $("#hideshow1").show();
//                return false;
//            });


            function resetvalues()

            {

                document.getElementById("partnerName").value = "";
                document.getElementById("applicationId").value = "";
                document.getElementById("senderId").value = "";
                document.getElementById("recId").value = "";
                document.getElementById("deliveryChannel").value = "";
                document.getElementById("reportrange").value = "";
                document.getElementById("countryCode").value = "";
                document.getElementById("referenceName").value = "-1";
                document.getElementById("refValue").value = "";
                document.getElementById("transactionType").value = "-1";
                document.getElementById("documentType").value = "-1";
                document.getElementById("direction").value = "-1";
                document.getElementById("status").value = "-1";
                document.getElementById("datepickerfrom").value = "";
                document.getElementById("datepickerTo").value = "";
                        $('#gridDiv').hide();


            }

            function getTransData(fileName, mailBoxName, map, instanceId, partnerName, applicationId, direction, transactionType, documentType, senderId, receiverId, isaNumber, gsNumber, refName, refValue, status, createdDate)
            {
                //alert("hi");
                // alert("pname "+partnerName);

                document.getElementById('mapName').value = map;
                document.getElementById('transactionId').value = instanceId;
                document.getElementById('partnerName_modal').value = partnerName;
                document.getElementById('applicationId_modal').value = applicationId;
                document.getElementById('direction_modal').value = direction;
                document.getElementById('transactionType_modal').value = transactionType;
                document.getElementById('documentType_modal').value = documentType;
                document.getElementById('senderId_modal').value = senderId;
                document.getElementById('receiverId_modal').value = receiverId;
                //  document.getElementById('isaNumber_modal').value=isaNumber;
                //  document.getElementById('gsNumber_modal').value=gsNumber;


                document.getElementById('referenceName_modal').value = refName;
                document.getElementById('referenceValue_modal').value = refValue;
                // document.getElementById('partnerName_modal').value=partnerName;
                document.getElementById('createdDate_modal').value = createdDate;
                document.getElementById('interChange').value = isaNumber;
                document.getElementById('group').value = gsNumber;
                document.getElementById('message').value = transactionType;
                document.getElementById('createdUser').value = "admin";

                document.getElementById('810trInvNum').style.display = "none";
                document.getElementById('856trBol').style.display = "none";
                document.getElementById('856trShipId').style.display = "none";

                document.getElementById('tanslated').innerHTML = map;
                document.getElementById('sentComplete').innerHTML = fileName;
                document.getElementById('archiveInternal').innerHTML = mailBoxName;

                var dateTimeReceived = createdDate.substring(0, 16);
                document.getElementById('crd').innerHTML = dateTimeReceived;
                document.getElementById('crd1').innerHTML = dateTimeReceived;
                document.getElementById('crd2').innerHTML = dateTimeReceived;



                //if (transactionType == '810' || transactionType == '850' || transactionType == '856' || transactionType == 'INVOIC' || transactionType == 'DESADV' || transactionType == 'ORDRSP' || transactionType == '855' || transactionType == '997') {
                    getTransactionReferences(instanceId, transactionType, senderId, receiverId);
               // }
                if (transactionType == '810' || transactionType == 'INVOIC') {
                    document.getElementById('810trInvNum').style.display = "table-row";
                    document.getElementById('856trBol').style.display = "none";
                    document.getElementById('856trShipId').style.display = "none";
                }
                if (transactionType == '856' || transactionType == 'DESADV') {
                    document.getElementById('856trBol').style.display = "table-row";
                    document.getElementById('856trShipId').style.display = "table-row";
                    document.getElementById('810trInvNum').style.display = "none";
                }


            }
//            function help()
//            {
//                $('#help').modal();
//            }
            function  ModelOpen() {
                $('#modal2').modal({
                    show: true,
                    backdrop: false
                })
            }
            function  ModelHide() {
                $('#modal2').modal('hide');
            }
            function check()
            {
                var value1 = document.getElementById("referenceName1").value;
                if (value1 != "-1")
                    document.getElementById("ref").style.display = "block";
                else
                    document.getElementById("ref").style.display = "none";
                var value2 = document.getElementById("referenceName2").value;
                if (value2 != "-1")
                    document.getElementById("ref1").style.display = "block";
                else
                    document.getElementById("ref1").style.display = "none";
            }
//                            function Date1()
//                            {
//                                var date = document.documentForm.reportrange.value;
//                                var arr = date.split("-");
//                                var x = arr[1].trim();
//                                document.getElementById("docdatepickerfrom").value = arr[0];
//                                document.getElementById("docdatepicker").value = x;
//                            }
            var count = 0;
            $("#addButton").click(function () {
                count++;
                if (count == 1)
                    document.getElementById("ref").style.display = "block";
                else if (count == 2)
                    document.getElementById("ref1").style.display = "block";
                else
                    alert('Limit exceeded.... cannot add more fields !!');
            })
            $("#removeButton1").click(function () {
                document.getElementById('referenceName1').value = "-1";
                document.getElementById('refvalue1').value = "";
                document.getElementById("ref").style.display = "none";
                // document.getElementById("addButton").style.display = "block";
                count = count - parseInt("1");
            })
            $("#removeButton2").click(function () {
                document.getElementById('referenceName2').value = "-1";
                document.getElementById('refvalue2').value = "";
                document.getElementById("ref1").style.display = "none";
                // document.getElementById("addButton").style.display = "block";
                count = count - parseInt("1");
            })
        </script>
    </body>
</html>
